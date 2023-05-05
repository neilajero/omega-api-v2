/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArJobOrderEntryControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.ar.ArTaxCodeDetails;
import com.util.mod.ar.*;
import com.util.mod.inv.InvModLineItemDetails;
import com.util.mod.inv.InvModTagListDetails;
import com.util.mod.inv.InvModUnitOfMeasureDetails;

import jakarta.ejb.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Stateless(name = "ArJobOrderEntryControllerEJB")
public class ArJobOrderEntryControllerBean extends EJBContextClass implements ArJobOrderEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalArJobOrderHome arJobOrderHome;
    @EJB
    private LocalArJobOrderLineHome arJobOrderLineHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalArSalespersonHome arSalespersonHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalArJobOrderAssignmentHome arJobOrderAssignmentHome;
    @EJB
    private LocalArPersonelHome arPersonelHome;
    @EJB
    private LocalArJobOrderTypeHome arJobOrderTypeHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvPriceLevelHome invPriceLevelHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private ILocalGlChartOfAccountHome glCoaHome;
    @EJB
    private LocalInvLineItemTemplateHome invLineItemTemplateHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalInvTagHome invTagHome;

    public Integer saveArJoEntry(ArModJobOrderDetails details, String PYT_NM, String TC_NM, String FC_NM, String CST_CSTMR_CODE, ArrayList jolList, boolean isDraft, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyVoidException, GlobalJournalNotBalanceException, GlobalInvItemLocationNotFoundException, GlobalNoApprovalApproverFoundException, GlobalNoApprovalRequesterFoundException, GlobalRecordAlreadyAssignedException {

        Debug.print("ArJobOrderEntryControllerBean saveArJoEntry");

        LocalArJobOrder arJobOrder = null;
        try {

            // validate if sales order is already deleted

            try {

                if (details.getJoCode() != null) {

                    arJobOrder = arJobOrderHome.findByPrimaryKey(details.getJoCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // sales order void

            if (details.getJoCode() != null && details.getJoVoid() == EJBCommon.TRUE) {

                Collection arJobOrderLines = arJobOrder.getArJobOrderLines();

                for (Object jobOrderLine : arJobOrderLines) {

                    LocalArJobOrderLine arJobOrderLine = (LocalArJobOrderLine) jobOrderLine;

                    if (!arJobOrderLine.getArJobOrderInvoiceLines().isEmpty()) {
                        throw new GlobalRecordAlreadyAssignedException();
                    }
                }

                arJobOrder.setJoVoid(EJBCommon.TRUE);
                arJobOrder.setJoLastModifiedBy(details.getJoLastModifiedBy());
                arJobOrder.setJoDateLastModified(details.getJoDateLastModified());

                return arJobOrder.getJoCode();
            }

            // validate if sales order is already posted, void, approved or pending

            if (details.getJoCode() != null) {
                // report Parameter saved
                arJobOrder.setReportParameter(details.getReportParameter());
                if (arJobOrder.getJoApprovalStatus() != null) {

                    if (arJobOrder.getJoApprovalStatus().equals("APPROVED") || arJobOrder.getJoApprovalStatus().equals("N/A")) {

                        return arJobOrder.getJoCode();

                    } else if (arJobOrder.getJoApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (arJobOrder.getJoPosted() == EJBCommon.TRUE) {

                    return arJobOrder.getJoCode();

                } else if (arJobOrder.getJoVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // validate if document number is unique document number is automatic then set next sequence

            LocalArJobOrder arExistingJobOrder = null;

            try {

                arExistingJobOrder = arJobOrderHome.findByJoDocumentNumberAndBrCode(details.getJoDocumentNumber(), AD_BRNCH, AD_CMPNY);

            } catch (FinderException ex) {
            }

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            if (details.getJoCode() == null) {

                String documentType = details.getJoDocumentType();

                try {
                    if (documentType != null) {
                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, AD_CMPNY);
                    } else {
                        documentType = "AR JOB ORDER";
                    }
                } catch (FinderException ex) {
                    documentType = "AR JOB ORDER";
                }

                try {

                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, AD_CMPNY);

                } catch (FinderException ex) {

                }

                try {

                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {

                }

                if (arExistingJobOrder != null) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getJoDocumentNumber() == null || details.getJoDocumentNumber().trim().length() == 0)) {

                    while (true) {

                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                            try {

                                arJobOrderHome.findByJoDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                            } catch (FinderException ex) {

                                details.setJoDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {

                                arJobOrderHome.findByJoDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                            } catch (FinderException ex) {

                                details.setJoDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }

            } else {

                if (arExistingJobOrder != null && !arExistingJobOrder.getJoCode().equals(details.getJoCode())) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (arJobOrder.getJoDocumentNumber() != details.getJoDocumentNumber() && (details.getJoDocumentNumber() == null || details.getJoDocumentNumber().trim().length() == 0)) {

                    details.setJoDocumentNumber(arJobOrder.getJoDocumentNumber());
                }
            }

            // validate if conversion date exists

            try {

                if (details.getJoConversionDate() != null) {

                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
                    LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

                    if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getJoConversionDate(), AD_CMPNY);

                    } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getJoConversionDate(), AD_CMPNY);
                    }
                }

            } catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // validate if payment term has at least one payment schedule

            if (adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY).getAdPaymentSchedules().isEmpty()) {

                throw new GlobalPaymentTermInvalidException();
            }

            boolean isRecalculate = true;

            // create sales order
            Debug.print("1");
            if (details.getJoCode() == null) {

                arJobOrder = arJobOrderHome.create(details.getJoDate(), details.getJoDocumentNumber(), details.getJoReferenceNumber(), details.getJoTransactionType(), details.getJoDescription(), details.getJoBillTo(), details.getJoShipTo(), details.getJoTechnician(), details.getJoConversionDate(), details.getJoConversionRate(), EJBCommon.FALSE, details.getJoMobile(), null, EJBCommon.FALSE, null, details.getJoCreatedBy(), details.getJoDateCreated(), details.getJoLastModifiedBy(), details.getJoDateLastModified(), null, null, null, null, EJBCommon.FALSE, EJBCommon.FALSE, details.getJoMemo(), details.getJoJobOrderStatus(), AD_BRNCH, AD_CMPNY);

            } else {

                // check if critical fields are changed
                Debug.print("2");
                if (!arJobOrder.getArTaxCode().getTcName().equals(TC_NM) || !arJobOrder.getArCustomer().getCstCustomerCode().equals(CST_CSTMR_CODE) || !arJobOrder.getAdPaymentTerm().getPytName().equals(PYT_NM) || jolList.size() != arJobOrder.getArJobOrderLines().size()) {

                    isRecalculate = true;
                    Debug.print("3");
                } else if (jolList.size() == arJobOrder.getArJobOrderLines().size()) {
                    Debug.print("4");
                    Iterator ilIter = arJobOrder.getArJobOrderLines().iterator();
                    Iterator ilListIter = jolList.iterator();

                    while (ilIter.hasNext()) {

                        LocalArJobOrderLine arJobOrderLine = (LocalArJobOrderLine) ilIter.next();
                        ArModJobOrderLineDetails mdetails = (ArModJobOrderLineDetails) ilListIter.next();
                        Debug.print("4a");
                        Debug.print(arJobOrderLine.getInvItemLocation().getInvItem().getIiName() + "===" + mdetails.getJolIiName());
                        Debug.print(arJobOrderLine.getInvItemLocation().getInvItem().getIiDescription() + "===" + mdetails.getJolIiDescription());
                        Debug.print(arJobOrderLine.getInvItemLocation().getInvLocation().getLocName() + "===" + mdetails.getJolLocName());
                        Debug.print(arJobOrderLine.getInvUnitOfMeasure().getUomName() + "===" + mdetails.getJolUomName());
                        Debug.print(arJobOrderLine.getJolQuantity() + "===" + mdetails.getJolQuantity());
                        Debug.print(arJobOrderLine.getJolUnitPrice() + "===" + mdetails.getJolUnitPrice());

                        if (!arJobOrderLine.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getJolIiName()) || !arJobOrderLine.getInvItemLocation().getInvItem().getIiDescription().equals(mdetails.getJolIiDescription()) || !arJobOrderLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getJolLocName()) || !arJobOrderLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getJolUomName()) || arJobOrderLine.getJolQuantity() != mdetails.getJolQuantity() || arJobOrderLine.getJolUnitPrice() != mdetails.getJolUnitPrice() || arJobOrderLine.getJolTax() != mdetails.getJolTax() || arJobOrderLine.getJolMisc() != null && (!arJobOrderLine.getJolMisc().equals(mdetails.getJolMisc()))) {

                            isRecalculate = true;

                            //	mdetails.getJaList().clear();
                            Debug.print("4b");
                            break;
                        }
                        Debug.print("4c");
                        //	isRecalculate = false;

                    }
                    Debug.print("4d");
                } else {
                    Debug.print("4");
                    //	isRecalculate = false;

                }

                arJobOrder.setJoDate(details.getJoDate());
                arJobOrder.setJoDocumentType(details.getJoDocumentType());
                arJobOrder.setJoDocumentNumber(details.getJoDocumentNumber());
                arJobOrder.setJoReferenceNumber(details.getJoReferenceNumber());
                arJobOrder.setJoTransactionType(details.getJoTransactionType());
                arJobOrder.setJoTransactionType(details.getJoTransactionType());
                arJobOrder.setJoDescription(details.getJoDescription());
                arJobOrder.setJoBillTo(details.getJoBillTo());
                arJobOrder.setJoShipTo(details.getJoShipTo());
                arJobOrder.setJoTechnician(details.getJoTechnician());
                arJobOrder.setJoVoid(details.getJoVoid());
                arJobOrder.setJoMobile(details.getJoMobile());
                arJobOrder.setJoConversionDate(details.getJoConversionDate());
                arJobOrder.setJoConversionRate(details.getJoConversionRate());
                arJobOrder.setJoLastModifiedBy(details.getJoLastModifiedBy());
                arJobOrder.setJoDateLastModified(details.getJoDateLastModified());
                arJobOrder.setJoReasonForRejection(null);
                arJobOrder.setJoMemo(details.getJoMemo());
                arJobOrder.setJoJobOrderStatus(details.getJoJobOrderStatus());
            }

            LocalArJobOrderType arJobOrderType = arJobOrderTypeHome.findByJotName(details.getJoType(), AD_CMPNY);

            arJobOrder.setArJobOrderType(arJobOrderType);

            arJobOrder.setJoDocumentType(details.getJoDocumentType());

            arJobOrder.setReportParameter(details.getReportParameter());

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, AD_CMPNY);

            arJobOrder.setArCustomer(arCustomer);

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY);

            arJobOrder.setAdPaymentTerm(adPaymentTerm);

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);

            arJobOrder.setArTaxCode(arTaxCode);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);

            arJobOrder.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalArSalesperson arSalesperson = details.getJoSlpSalespersonCode() == null ? null : arSalespersonHome.findBySlpSalespersonCode(details.getJoSlpSalespersonCode(), AD_CMPNY);

            if (arSalesperson != null) {
                arJobOrder.setArSalesperson(arSalesperson);
            }

            double ABS_TOTAL_AMOUNT = 0d;
            Debug.print("5");
            if (isRecalculate) {

                // remove all sales order line items

                Collection arJobOrderLines = arJobOrder.getArJobOrderLines();

                Iterator i = arJobOrderLines.iterator();
                Debug.print("6");
                while (i.hasNext()) {

                    LocalArJobOrderLine arJobOrderLine = (LocalArJobOrderLine) i.next();

                    Collection invTags = arJobOrderLine.getInvTags();

                    Iterator x = invTags.iterator();

                    while (x.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) x.next();

                        x.remove();

                        //		  	   	    	invTag.entityRemove();
                        em.remove(invTag);
                    }

                    Debug.print("7");
                    i.remove();

                    //					arJobOrderLine.entityRemove();
                    em.remove(arJobOrderLine);
                    Debug.print("8");
                }

                // add new purchase order line item

                i = jolList.iterator();

                LocalInvItemLocation invItemLocation = null;

                while (i.hasNext()) {

                    ArModJobOrderLineDetails mJolDetails = (ArModJobOrderLineDetails) i.next();

                    LocalArJobOrderLine arJobOrderLine = arJobOrderLineHome.create(mJolDetails.getJolLine(), mJolDetails.getJolLineIDesc(), mJolDetails.getJolQuantity(), mJolDetails.getJolUnitPrice(), mJolDetails.getJolAmount(), mJolDetails.getJolDiscount1(), mJolDetails.getJolDiscount2(), mJolDetails.getJolDiscount3(), mJolDetails.getJolDiscount4(), mJolDetails.getJolTotalDiscount(), 0d, mJolDetails.getJolMisc(), mJolDetails.getJolTax(), AD_CMPNY);

                    arJobOrderLine.setArJobOrder(arJobOrder);

                    ArrayList jaList = mJolDetails.getJaList();

                    Debug.print("1 ja size is: " + mJolDetails.getJaList().size());
                    Debug.print("1 ja size is: " + mJolDetails.getJaList().size());

                    for (Object o : jaList) {

                        ArModJobOrderAssignmentDetails jaDetails = (ArModJobOrderAssignmentDetails) o;

                        LocalArJobOrderAssignment arJobOrderAssignment = arJobOrderAssignmentHome.create(jaDetails.getJaLine(), jaDetails.getJaRemarks(), jaDetails.getJaQuantity(), jaDetails.getJaUnitCost(), jaDetails.getJaAmount(), jaDetails.getJaSo(), AD_CMPNY);

                        LocalArPersonel arPersonel = arPersonelHome.findByPeIdNumber(jaDetails.getJaPeIdNumber(), AD_CMPNY);

                        arJobOrderAssignment.setArPersonel(arPersonel);

                        arJobOrderAssignment.setArJobOrderLine(arJobOrderLine);
                    }

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mJolDetails.getJolLocName(), mJolDetails.getJolIiName(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mJolDetails.getJolLine()));
                    }

                    ABS_TOTAL_AMOUNT += arJobOrderLine.getJolAmount();

                    arJobOrderLine.setInvItemLocation(invItemLocation);

                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mJolDetails.getJolUomName(), AD_CMPNY);

                    arJobOrderLine.setInvUnitOfMeasure(invUnitOfMeasure);

                    if (arJobOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc() == 1) {
                        this.createInvTagList(arJobOrderLine, mJolDetails.getJolTagList(), AD_CMPNY);
                    }
                }
            }

            // ORDER STATUS

            Iterator invcIter = arCustomer.getArInvoices().iterator();
            // arSalesOrder.setSoOrderStatus("Good");
            String OrderStatus = "";

            while (invcIter.hasNext()) {

                LocalArInvoice arInvoice = (LocalArInvoice) invcIter.next();

                if (arInvoice.getInvPosted() == EJBCommon.FALSE) {
                    continue;
                }

                for (Object o : arInvoice.getArInvoicePaymentSchedules()) {

                    LocalArInvoicePaymentSchedule ips = (LocalArInvoicePaymentSchedule) o;

                    try {
                        if (ips.getIpsAmountPaid() == 0 && !OrderStatus.contains("Bad1")) {
                            OrderStatus = "Bad1";
                        }

                        if (ips.getIpsAmountPaid() < ips.getIpsAmountDue() && !OrderStatus.contains("Bad2")) {

                            if (OrderStatus != "") {
                                OrderStatus = OrderStatus + "/";
                            }

                            OrderStatus = OrderStatus + "Bad2";
                        }
                        int result = 0;

                        try {
                            result = ips.getIpsDueDate().compareTo(((LocalArAppliedInvoice) ips.getArAppliedInvoices().toArray()[0]).getArReceipt().getRctDate());
                            System.out.print("HERE IS: " + result);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        if (result == -1 && !OrderStatus.contains("Bad3")) {
                            if (OrderStatus != "") {
                                OrderStatus = OrderStatus + "/";
                            }

                            OrderStatus = OrderStatus + "Bad3";
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        arJobOrder.setJoOrderStatus(OrderStatus);
                    }
                }
            }

            if (OrderStatus == "") {
                OrderStatus = "Good";
            }

            arJobOrder.setJoOrderStatus(OrderStatus);

            // set sales order approval status

            String SO_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);

                // check if ar sales order approval is enabled

                if (adApproval.getAprEnableArSalesOrder() == EJBCommon.FALSE) {

                    SO_APPRVL_STATUS = "N/A";

                } else {

                    // check if sales order is self approved

                    LocalAdAmountLimit adAmountLimit = null;

                    try {

                        adAmountLimit = adAmountLimitHome.findByAdcTypeAndAuTypeAndUsrName("AR SALES ORDER", "REQUESTER", details.getJoLastModifiedBy(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new GlobalNoApprovalRequesterFoundException();
                    }

                    if (ABS_TOTAL_AMOUNT <= adAmountLimit.getCalAmountLimit()) {

                        SO_APPRVL_STATUS = "N/A";

                    } else {

                        // for approval, create approval queue

                        Collection adAmountLimits = adAmountLimitHome.findByAdcTypeAndGreaterThanCalAmountLimit("AR SALES ORDER", adAmountLimit.getCalAmountLimit(), AD_CMPNY);

                        if (adAmountLimits.isEmpty()) {

                            Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), AD_CMPNY);

                            if (adApprovalUsers.isEmpty()) {

                                throw new GlobalNoApprovalApproverFoundException();
                            }

                            for (Object approvalUser : adApprovalUsers) {

                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, AD_CMPNY, adApprovalQueueHome, arJobOrder, adAmountLimit, adApprovalUser);

                                adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                            }

                        } else {

                            boolean isApprovalUsersFound = false;

                            Iterator i = adAmountLimits.iterator();

                            while (i.hasNext()) {

                                LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();

                                if (ABS_TOTAL_AMOUNT <= adNextAmountLimit.getCalAmountLimit()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), AD_CMPNY);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, AD_CMPNY, adApprovalQueueHome, arJobOrder, adAmountLimit, adApprovalUser);

                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                    }

                                    break;

                                } else if (!i.hasNext()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adNextAmountLimit.getCalCode(), AD_CMPNY);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;
                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, AD_CMPNY, adApprovalQueueHome, arJobOrder, adAmountLimit, adApprovalUser);
                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                    }

                                    break;
                                }

                                adAmountLimit = adNextAmountLimit;
                            }

                            if (!isApprovalUsersFound) {

                                throw new GlobalNoApprovalApproverFoundException();
                            }
                        }

                        SO_APPRVL_STATUS = "PENDING";
                    }
                }

                arJobOrder.setJoApprovalStatus(SO_APPRVL_STATUS);

                // set post purchase order

                if (SO_APPRVL_STATUS.equals("N/A")) {

                    arJobOrder.setJoPosted(EJBCommon.TRUE);
                    arJobOrder.setJoPosted(EJBCommon.TRUE);
                    arJobOrder.setJoOrderStatus("Good");
                    arJobOrder.setJoPostedBy(arJobOrder.getJoLastModifiedBy());
                    arJobOrder.setJoDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                }
                // arSalesOrder.setSoOrderStatus("Good");

            }

            // REFERENCE NUMBER GENERATOR
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            String newReferenceNumber = this.createCodedReferenceNumber(arJobOrder, adCompany);

            arJobOrder.setJoReferenceNumber(newReferenceNumber);

            return arJobOrder.getJoCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalRecordAlreadyAssignedException |
                 GlobalNoApprovalRequesterFoundException | GlobalNoApprovalApproverFoundException |
                 GlobalInvItemLocationNotFoundException | GlobalTransactionAlreadyVoidException |
                 GlobalTransactionAlreadyPendingException | GlobalPaymentTermInvalidException |
                 GlobalConversionDateNotExistException | GlobalJournalNotBalanceException |
                 GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteArJoEntry(Integer JO_CODE, String AD_USR, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException {
        Debug.print("ArJobOrderEntryControllerBean deleteArSoEntry");
        try {

            Debug.print("JO_CODE=" + JO_CODE);
            Debug.print("AD_USR=" + AD_USR);

            LocalArJobOrder arJobOrder = arJobOrderHome.findByPrimaryKey(JO_CODE);
            Debug.print("1");
            if (arJobOrder.getJoApprovalStatus() != null && arJobOrder.getJoApprovalStatus().equals("PENDING")) {
                Debug.print("2");
                Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AR SALES ORDER", arJobOrder.getJoCode(), AD_CMPNY);

                for (Object approvalQueue : adApprovalQueues) {

                    LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                    //                    adApprovalQueue.entityRemove();
                    em.remove(adApprovalQueue);
                }
                Debug.print("3");
            }

            adDeleteAuditTrailHome.create("AR JOB ORDER", arJobOrder.getJoDate(), arJobOrder.getJoDocumentNumber(), arJobOrder.getJoReferenceNumber(), 0d, AD_USR, new Date(), AD_CMPNY);
            Debug.print("4" + arJobOrder.getJoCode() + "SO Number" + arJobOrder.getJoDocumentNumber());
            //            arJobOrder.entityRemove();
            em.remove(arJobOrder);

            Debug.print("5");
        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArTaxCodeDetails getArTcByTcName(String TC_NM, Integer AD_CMPNY) {
        Debug.print("ArJobOrderEntryControllerBean getArTcByTcName");
        try {

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);

            ArTaxCodeDetails details = new ArTaxCodeDetails();
            details.setTcType(arTaxCode.getTcType());
            details.setTcRate(arTaxCode.getTcRate());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {
        Debug.print("ArJobOrderEntryControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer AD_CMPNY) {
        Debug.print("ArJobOrderEntryControllerBean getInvGpQuantityPrecisionUnit");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfArJournalLineNumber(Integer AD_CMPNY) {
        Debug.print("ArJobEntryControllerBean getAdPrfArJournalLineNumber");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfArInvoiceLineNumber();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModJobOrderDetails getArJoBySoCode(Integer JO_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArJobOrderEntryControllerBean getArSoBySoCode");
        try {

            LocalArJobOrder arJobOrder = null;

            try {

                arJobOrder = arJobOrderHome.findByPrimaryKey(JO_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get sales order lines if any

            Collection arJobOrderLines = arJobOrder.getArJobOrderLines();

            for (Object jobOrderLine : arJobOrderLines) {

                LocalArJobOrderLine arJobOrderLine = (LocalArJobOrderLine) jobOrderLine;

                ArModJobOrderLineDetails jolDetails = new ArModJobOrderLineDetails();

                jolDetails.setJolCode(arJobOrderLine.getJolCode());
                jolDetails.setJolLine(arJobOrderLine.getJolLine());
                jolDetails.setJolIiName(arJobOrderLine.getInvItemLocation().getInvItem().getIiName());
                jolDetails.setJolLocName(arJobOrderLine.getInvItemLocation().getInvLocation().getLocName());
                jolDetails.setJolQuantity(arJobOrderLine.getJolQuantity());
                jolDetails.setJolUomName(arJobOrderLine.getInvUnitOfMeasure().getUomName());
                jolDetails.setJolUnitPrice(arJobOrderLine.getJolUnitPrice());
                jolDetails.setJolIiDescription(arJobOrderLine.getInvItemLocation().getInvItem().getIiDescription());
                jolDetails.setJolEDesc(arJobOrderLine.getJolLineIDesc());
                jolDetails.setJolDiscount1(arJobOrderLine.getJolDiscount1());
                jolDetails.setJolDiscount2(arJobOrderLine.getJolDiscount2());
                jolDetails.setJolDiscount3(arJobOrderLine.getJolDiscount3());
                jolDetails.setJolDiscount4(arJobOrderLine.getJolDiscount4());
                jolDetails.setJolTotalDiscount(arJobOrderLine.getJolTotalDiscount());
                jolDetails.setJolMisc(arJobOrderLine.getJolMisc());
                jolDetails.setJolAmount(arJobOrderLine.getJolAmount());
                jolDetails.setJolTax(arJobOrderLine.getJolTax());
                jolDetails.setJolIiServices(arJobOrderLine.getInvItemLocation().getInvItem().getIiServices() == EJBCommon.TRUE);
                jolDetails.setJolIiJobServices(arJobOrderLine.getInvItemLocation().getInvItem().getIiJobServices() == EJBCommon.TRUE);
                jolDetails.setTraceMisc(arJobOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc());

                Debug.print("job services is : " + arJobOrderLine.getInvItemLocation().getInvItem().getIiJobServices());
                Debug.print(jolDetails.getTraceMisc() + "<== getTraceMisc under getInvAdjByAdjCode controllerbean");
                if (jolDetails.getTraceMisc() == 1) {

                    ArrayList tagList = new ArrayList();

                    tagList = this.getInvTagList(arJobOrderLine);
                    jolDetails.setJolTagList(tagList);
                    jolDetails.setTraceMisc(jolDetails.getTraceMisc());
                }

                double totalHours = 0d;
                double totalRatePerHours = 0d;

                for (Object o : arJobOrderLine.getArJobOrderAssignments()) {

                    LocalArJobOrderAssignment arJobOrderAssignment = (LocalArJobOrderAssignment) o;

                    ArModJobOrderAssignmentDetails jaDetails = new ArModJobOrderAssignmentDetails();
                    jaDetails.setJaCode(jaDetails.getJaCode());
                    jaDetails.setJaLine(arJobOrderAssignment.getJaLine());
                    jaDetails.setJaRemarks(arJobOrderAssignment.getJaRemarks());
                    jaDetails.setJaPeIdNumber(arJobOrderAssignment.getArPersonel().getPeIdNumber());
                    jaDetails.setJaQuantity(arJobOrderAssignment.getJaQuantity());
                    jaDetails.setJaUnitCost(arJobOrderAssignment.getJaUnitCost());
                    jaDetails.setJaAmount(arJobOrderAssignment.getJaAmount());
                    jaDetails.setJaSo(arJobOrderAssignment.getJaSo());
                    jaDetails.setJaPeName(arJobOrderAssignment.getArPersonel().getPeName());

                    if (arJobOrderAssignment.getJaSo() == EJBCommon.TRUE) {
                        totalHours += arJobOrderAssignment.getJaQuantity();
                        totalRatePerHours += arJobOrderAssignment.getJaAmount();
                    }

                    jolDetails.saveJaList(jaDetails);
                }

                jolDetails.setJolTotalHours(totalHours);
                jolDetails.setJolTotalRatePerHours(totalRatePerHours);

                list.add(jolDetails);
            }

            ArModJobOrderDetails mJoDetails = new ArModJobOrderDetails();

            mJoDetails.setJoType(arJobOrder.getArJobOrderType() != null ? arJobOrder.getArJobOrderType().getJotName() : "");
            mJoDetails.setJoCode(arJobOrder.getJoCode());
            mJoDetails.setJoDate(arJobOrder.getJoDate());
            mJoDetails.setJoDocumentType(arJobOrder.getJoDocumentType());
            mJoDetails.setJoDocumentNumber(arJobOrder.getJoDocumentNumber());
            mJoDetails.setJoReferenceNumber(arJobOrder.getJoReferenceNumber());
            mJoDetails.setJoTransactionType(arJobOrder.getJoTransactionType());
            mJoDetails.setJoDescription(arJobOrder.getJoDescription());
            mJoDetails.setJoVoid(arJobOrder.getJoVoid());
            mJoDetails.setJoMobile(arJobOrder.getJoMobile());
            mJoDetails.setJoBillTo(arJobOrder.getJoBillTo());
            mJoDetails.setJoShipTo(arJobOrder.getJoShipTo());
            mJoDetails.setJoTechnician(arJobOrder.getJoTechnician());
            mJoDetails.setJoConversionDate(arJobOrder.getJoConversionDate());
            mJoDetails.setJoConversionRate(arJobOrder.getJoConversionRate());
            mJoDetails.setJoApprovalStatus(arJobOrder.getJoApprovalStatus());
            mJoDetails.setJoPosted(arJobOrder.getJoPosted());
            mJoDetails.setJoCreatedBy(arJobOrder.getJoCreatedBy());
            mJoDetails.setJoDateCreated(arJobOrder.getJoDateCreated());
            mJoDetails.setJoLastModifiedBy(arJobOrder.getJoLastModifiedBy());
            mJoDetails.setJoDateLastModified(arJobOrder.getJoDateLastModified());
            mJoDetails.setJoApprovedRejectedBy(arJobOrder.getJoApprovedRejectedBy());
            mJoDetails.setJoDateApprovedRejected(arJobOrder.getJoDateApprovedRejected());
            mJoDetails.setJoPostedBy(arJobOrder.getJoPostedBy());
            mJoDetails.setJoDatePosted(arJobOrder.getJoDatePosted());
            mJoDetails.setJoReasonForRejection(arJobOrder.getJoReasonForRejection());
            mJoDetails.setJoCstCustomerCode(arJobOrder.getArCustomer().getCstCustomerCode());
            mJoDetails.setJoCstName(arJobOrder.getArCustomer().getCstName());
            mJoDetails.setJoPytName(arJobOrder.getAdPaymentTerm().getPytName());
            mJoDetails.setJoTcName(arJobOrder.getArTaxCode().getTcName());
            mJoDetails.setJoFcName(arJobOrder.getGlFunctionalCurrency().getFcName());
            mJoDetails.setJoTcRate(arJobOrder.getArTaxCode().getTcRate());
            mJoDetails.setJoTcType(arJobOrder.getArTaxCode().getTcType());
            mJoDetails.setJoApprovedRejectedBy(arJobOrder.getJoApprovedRejectedBy());
            mJoDetails.setJoDateApprovedRejected(arJobOrder.getJoDateApprovedRejected());
            mJoDetails.setJoMemo(arJobOrder.getJoMemo());

            mJoDetails.setJoJobOrderStatus(arJobOrder.getJoJobOrderStatus());
            mJoDetails.setJoOrderStatus(arJobOrder.getJoOrderStatus());
            mJoDetails.setReportParameter(arJobOrder.getReportParameter());

            if (arJobOrder.getArSalesperson() != null) {

                mJoDetails.setJoSlpSalespersonCode(arJobOrder.getArSalesperson().getSlpSalespersonCode());
                mJoDetails.setJoSlpName(arJobOrder.getArSalesperson().getSlpName());
            }

            // get So Advance if have
            double JO_ADVNC_AMNT = 0d;

            for (Object o : arJobOrder.getCmAdjustments()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) o;

                if (cmAdjustment.getAdjVoid() == EJBCommon.FALSE && cmAdjustment.getAdjReconciled() == EJBCommon.FALSE) {
                    JO_ADVNC_AMNT += cmAdjustment.getAdjAmount();
                }
            }

            mJoDetails.setJoAdvanceAmount(JO_ADVNC_AMNT);

            mJoDetails.setJoJolList(list);

            return mJoDetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModCustomerDetails getArCstByCstCustomerCode(String CST_CSTMR_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArJobOrderEntryControllerBean getArCstByCstCustomerCode");
        try {

            LocalArCustomer arCustomer = null;

            try {

                arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, AD_CMPNY);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArModCustomerDetails mdetails = new ArModCustomerDetails();

            mdetails.setCstPytName(arCustomer.getAdPaymentTerm() != null ? arCustomer.getAdPaymentTerm().getPytName() : null);
            mdetails.setCstName(arCustomer.getCstName());

            if (arCustomer.getArSalesperson() != null && arCustomer.getCstArSalesperson2() == null) {

                mdetails.setCstSlpSalespersonCode(arCustomer.getArSalesperson().getSlpSalespersonCode());
                mdetails.setCstSlpName(arCustomer.getArSalesperson().getSlpName());

            } else if (arCustomer.getArSalesperson() == null && arCustomer.getCstArSalesperson2() != null) {

                LocalArSalesperson arSalesperson2 = null;
                arSalesperson2 = arSalespersonHome.findByPrimaryKey(arCustomer.getCstArSalesperson2());

                mdetails.setCstSlpSalespersonCode(arSalesperson2.getSlpSalespersonCode());
                mdetails.setCstSlpName(arSalesperson2.getSlpName());
            }
            if (arCustomer.getArSalesperson() != null && arCustomer.getCstArSalesperson2() != null) {

                mdetails.setCstSlpSalespersonCode(arCustomer.getArSalesperson().getSlpSalespersonCode());
                mdetails.setCstSlpName(arCustomer.getArSalesperson().getSlpName());
                LocalArSalesperson arSalesperson2 = null;
                arSalesperson2 = arSalespersonHome.findByPrimaryKey(arCustomer.getCstArSalesperson2());

                mdetails.setCstSlpSalespersonCode2(arSalesperson2.getSlpSalespersonCode());
                mdetails.setCstSlpName2(arSalesperson2.getSlpName());
            }

            if (arCustomer.getArCustomerClass().getArTaxCode() != null) {

                mdetails.setCstCcTcName(arCustomer.getArCustomerClass().getArTaxCode().getTcName());
                mdetails.setCstCcTcRate(arCustomer.getArCustomerClass().getArTaxCode().getTcRate());
                mdetails.setCstCcTcType(arCustomer.getArCustomerClass().getArTaxCode().getTcType());
            }

            if (arCustomer.getInvLineItemTemplate() != null) {
                mdetails.setCstLitName(arCustomer.getInvLineItemTemplate().getLitName());
            }

            return mdetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvUomByIiName(String II_NM, Integer AD_CMPNY) {
        Debug.print("ArSalesOrderEntryControllerBean getInvUomByIiName");
        ArrayList list = new ArrayList();
        try {

            LocalInvItem invItem = null;
            LocalInvUnitOfMeasure invItemUnitOfMeasure = null;

            invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);
            invItemUnitOfMeasure = invItem.getInvUnitOfMeasure();

            Collection invUnitOfMeasures = null;
            for (Object o : invUnitOfMeasureHome.findByUomAdLvClass(invItemUnitOfMeasure.getUomAdLvClass(), AD_CMPNY)) {

                LocalInvUnitOfMeasure invUnitOfMeasure = (LocalInvUnitOfMeasure) o;
                InvModUnitOfMeasureDetails details = new InvModUnitOfMeasureDetails();
                details.setUomName(invUnitOfMeasure.getUomName());

                if (invUnitOfMeasure.getUomName().equals(invItemUnitOfMeasure.getUomName())) {

                    details.setDefault(true);
                }

                list.add(details);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getIiSalesPriceByInvCstCustomerCodeAndIiNameAndUomName(String CST_CSTMR_CODE, String II_NM, String UOM_NM, Integer AD_CMPNY) {
        Debug.print("ArJobOrderEntryControllerBean getInvIiSalesPriceByIiNameAndUomName");
        try {

            LocalArCustomer arCustomer = null;

            try {

                arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, AD_CMPNY);

            } catch (FinderException ex) {

                return 0d;
            }

            double unitPrice = 0d;

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);

            LocalInvPriceLevel invPriceLevel = null;

            try {

                invPriceLevel = invPriceLevelHome.findByIiNameAndAdLvPriceLevel(II_NM, arCustomer.getCstDealPrice(), AD_CMPNY);

                if (invPriceLevel.getPlAmount() == 0) {

                    unitPrice = invItem.getIiSalesPrice();

                } else {

                    unitPrice = invPriceLevel.getPlAmount();
                }

            } catch (FinderException ex) {

                unitPrice = invItem.getIiSalesPrice();
            }

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            return EJBCommon.roundIt(unitPrice * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(AD_CMPNY));

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModJobOrderTypeDetails getArJobOrderTypeByJotNm(String JOT_NM, Integer AD_CMPNY) {
        Debug.print("ArJobOrderEntryControllerBean getArJobOrderTypeByJotNm");
        try {

            ArModJobOrderTypeDetails details = new ArModJobOrderTypeDetails();

            LocalArJobOrderType arJobOrderType = arJobOrderTypeHome.findByJotName(JOT_NM, AD_CMPNY);

            details.setJotCode(arJobOrderType.getJotCode());
            details.setJotName(arJobOrderType.getJotName());
            details.setJotDescription(arJobOrderType.getJotDescription());
            details.setJotDocumentType(arJobOrderType.getJotDocumentType());
            details.setJotReportType(arJobOrderType.getJotReportType());
            details.setJotEnable(arJobOrderType.getJotEnable());
            if (arJobOrderType.getJotGlCoaJobOrderAccount() != null) {

                try {
                    LocalGlChartOfAccount glCoa = glCoaHome.findByCoaAccountNumber(arJobOrderType.getJotGlCoaJobOrderAccount().toString(), AD_CMPNY);
                    details.setJotGlCoaJobOrderAccount(arJobOrderType.getJotGlCoaJobOrderAccount());
                    details.setJotGlCoaJobOrderAccountDescription(glCoa.getCoaAccountDescription());
                } catch (FinderException ex) {
                    return details;
                }
            }

            if (arJobOrderType.getArTaxCode() != null) {
                details.setJotTcName(arJobOrderType.getArTaxCode().getTcName());
            }

            if (arJobOrderType.getArWithholdingTaxCode() != null) {
                details.setJotTcName(arJobOrderType.getArWithholdingTaxCode().getWtcName());
            }

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersBySoCode(Integer SO_CODE, Integer AD_CMPNY) {
        Debug.print("ArJobOrderEntryControllerBean getAdApprovalNotifiedUsersBySoCode");
        ArrayList list = new ArrayList();
        try {

            LocalArJobOrder arJobOrder = arJobOrderHome.findByPrimaryKey(SO_CODE);

            if (arJobOrder.getJoPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AR SALES ORDER", SO_CODE, AD_CMPNY);

            for (Object approvalQueue : adApprovalQueues) {

                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                list.add(adApprovalQueue.getAdUser().getUsrDescription());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer AD_CMPNY) throws GlobalConversionDateNotExistException {
        Debug.print("ArJobOrderEntryControllerBean getFrRateByFrNameAndFrDate");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);

            double CONVERSION_RATE = 1;

            // Get functional currency rate

            if (!FC_NM.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), CONVERSION_DATE, AD_CMPNY);

                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, AD_CMPNY);

                CONVERSION_RATE = CONVERSION_RATE / glCompanyFunctionalCurrencyRate.getFrXToUsd();
            }

            return CONVERSION_RATE;

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalConversionDateNotExistException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvLitByCstLitName(String CST_LIT_NAME, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArJobOrderEntryControllerBean getInvLitByCstLitName");
        try {

            LocalInvLineItemTemplate invLineItemTemplate = null;

            try {

                invLineItemTemplate = invLineItemTemplateHome.findByLitName(CST_LIT_NAME, AD_CMPNY);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get line items if any

            Collection invLineItems = invLineItemTemplate.getInvLineItems();

            if (!invLineItems.isEmpty()) {

                for (Object lineItem : invLineItems) {

                    LocalInvLineItem invLineItem = (LocalInvLineItem) lineItem;

                    InvModLineItemDetails liDetails = new InvModLineItemDetails();

                    liDetails.setLiCode(invLineItem.getLiCode());
                    liDetails.setLiLine(invLineItem.getLiLine());
                    liDetails.setLiIiName(invLineItem.getInvItemLocation().getInvItem().getIiName());
                    liDetails.setLiLocName(invLineItem.getInvItemLocation().getInvLocation().getLocName());
                    liDetails.setLiUomName(invLineItem.getInvUnitOfMeasure().getUomName());
                    liDetails.setLiIiDescription(invLineItem.getInvItemLocation().getInvItem().getIiDescription());

                    list.add(liDetails);
                }
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getInvIiClassByIiName(String II_NM, Integer AD_CMPNY) {
        Debug.print("ArJobOrderEntryControllerBean getInvIiClassByIiName");
        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);

            return invItem.getIiClass();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfArEnablePaymentTerm(Integer AD_CMPNY) {
        Debug.print("ArJobOrderEntryControllerBean getAdPrfArEnablePaymentTerm");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfArEnablePaymentTerm();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public boolean getJoTraceMisc(String II_NAME, Integer AD_CMPNY) {
        Debug.print("ArJobOrderEntryControllerBean getJoTraceMisc");
        boolean isTraceMisc = false;
        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NAME, AD_CMPNY);
            if (invItem.getIiTraceMisc() == 1) {
                isTraceMisc = true;
            }
            return isTraceMisc;
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public boolean getJoJobServices(String II_NAME, Integer AD_CMPNY) {
        Debug.print("ArJobOrderEntryControllerBean getJoTraceMisc");
        boolean isJobServices = false;
        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NAME, AD_CMPNY);

            Debug.print("controller job services is: " + invItem.getIiJobServices());
            if (invItem.getIiJobServices() == EJBCommon.TRUE) {
                isJobServices = true;
            }
            return isJobServices;
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfArDisableSalesPrice(Integer AD_CMPNY) {
        Debug.print("ArJobOrderEntryControllerBean getAdPrfArDisableSalesPrice");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfArDisableSalesPrice();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private String createCodedReferenceNumber(LocalArJobOrder arJobOrder, LocalAdCompany adCompany) {
        Debug.print("ArJobOrderEntryControllerBean createCodedReferenceNumber");
        String newReferenceNumber = "";
        if (!arJobOrder.getJoReferenceNumber().equals("")) {
            return arJobOrder.getJoReferenceNumber();
        }
        // ICEI
        if (adCompany.getCmpShortName().equals("ICEI") && arJobOrder.getJoDocumentType() != null) {

            // "TransactionType" + "-" + "last two digit date year" + "-" + "sonumber"

            // get trans
            // String transTypeWord =
            // arSalesOrder.getSoTransactionType().equals("")?"F":arSalesOrder.getSoTransactionType();

            // get trans
            String transTypeWord = arJobOrder.getJoDocumentType().equals("Air Freight") ? "AF" : arJobOrder.getJoTransactionType();

            // get date year (2 digit)
            DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
            String formattedDate = df.format(arJobOrder.getJoDate());

            // get SO Number
            String joNumber = arJobOrder.getJoDocumentNumber();

            newReferenceNumber = transTypeWord + "-" + formattedDate + "-" + joNumber;
        }
        return newReferenceNumber;
    }

    public boolean getInvTraceMisc(String II_NAME, Integer AD_CMPNY) {
        Debug.print("ArJobOrderEntryControllerBean getInvLocAll");
        boolean isTraceMisc = false;
        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NAME, AD_CMPNY);
            if (invItem.getIiTraceMisc() == 1) {
                isTraceMisc = true;
            }
            return isTraceMisc;
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void createInvTagList(LocalArJobOrderLine arJobOrderLine, ArrayList list, Integer AD_CMPNY) throws Exception {
        Debug.print("ArJobOrderEntryControllerBean createInvTagList");
        try {
            Debug.print("aabot?");
            // Iterator t = apPurchaseOrderLine.getInvTag().iterator();
            Iterator t = list.iterator();

            LocalInvTag invTag = null;
            Debug.print("umabot?");
            while (t.hasNext()) {
                InvModTagListDetails tgLstDetails = (InvModTagListDetails) t.next();
                Debug.print(tgLstDetails.getTgCustodian() + "<== custodian");
                Debug.print(tgLstDetails.getTgSpecs() + "<== specs");
                Debug.print(tgLstDetails.getTgPropertyCode() + "<== propertyCode");
                Debug.print(tgLstDetails.getTgExpiryDate() + "<== expiryDate");
                Debug.print(tgLstDetails.getTgSerialNumber() + "<== serial number");

                if (tgLstDetails.getTgCode() == null) {
                    Debug.print("ngcreate ba?");
                    invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), AD_CMPNY, tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());

                    invTag.setArJobOrderLine(arJobOrderLine);
                    invTag.setInvItemLocation(arJobOrderLine.getInvItemLocation());
                    LocalAdUser adUser = null;
                    try {
                        adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), AD_CMPNY);
                    } catch (FinderException ex) {

                    }
                    invTag.setAdUser(adUser);
                    Debug.print("ngcreate ba?");
                }
            }

        } catch (Exception ex) {
            throw ex;
        }
    }

    private ArrayList getInvTagList(LocalArJobOrderLine arJobOrderLine) {
        ArrayList list = new ArrayList();
        Collection invTags = arJobOrderLine.getInvTags();
        for (Object tag : invTags) {
            LocalInvTag invTag = (LocalInvTag) tag;
            InvModTagListDetails tgLstDetails = new InvModTagListDetails();
            tgLstDetails.setTgPropertyCode(invTag.getTgPropertyCode());
            tgLstDetails.setTgSpecs(invTag.getTgSpecs());
            tgLstDetails.setTgExpiryDate(invTag.getTgExpiryDate());
            tgLstDetails.setTgSerialNumber(invTag.getTgSerialNumber());
            try {

                tgLstDetails.setTgCustodian(invTag.getAdUser().getUsrName());
            } catch (Exception ex) {
                tgLstDetails.setTgCustodian("");
            }

            list.add(tgLstDetails);

            Debug.print(tgLstDetails.getTgPropertyCode() + "<== property code inside controllerbean ");
            Debug.print(tgLstDetails.getTgSpecs() + "<== specs inside controllerbean ");
            Debug.print(list + "<== taglist inside controllerbean ");
        }
        return list;
    }

    public ArrayList getAllJobOrderTypeName(Integer AD_CMPNY) {
        Debug.print("ArJobOrderEntryControllerBean getAllJobOrderType");
        try {

            Collection arJobOrderTypes = arJobOrderTypeHome.findJotAll(AD_CMPNY);
            ArrayList list = new ArrayList();

            for (Object jobOrderType : arJobOrderTypes) {

                LocalArJobOrderType arJobOrderType = (LocalArJobOrderType) jobOrderType;

                list.add(arJobOrderType.getJotName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer getArJolCodeByJolLineAndJoCode(short JOL_LN, Integer JO_CODE, Integer AD_BRNCH, Integer AD_CMPNY) {
        Debug.print("ArJobOrderEntryControllerBean getArJolCodeByJolLineAndJoCode");
        try {

            LocalArJobOrderLine arJobOrderLine = arJobOrderLineHome.findByJoCodeAndJolLineAndBrCode(JO_CODE, JOL_LN, AD_BRNCH, AD_CMPNY);

            return arJobOrderLine.getJolCode();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalAdApprovalQueue createApprovalQueue(Integer branchCode, Integer companyCode, LocalAdApprovalQueueHome adApprovalQueueHome, LocalArJobOrder arJobOrder, LocalAdAmountLimit adAmountLimit, LocalAdApprovalUser adApprovalUser) throws CreateException {

        return adApprovalQueueHome.AqForApproval(EJBCommon.TRUE).AqDocument("AR SALES ORDER").AqDocumentCode(arJobOrder.getJoCode()).AqDocumentNumber(arJobOrder.getJoDocumentNumber()).AqDate(arJobOrder.getJoDate()).AqAndOr(adAmountLimit.getCalAndOr()).AqUserOr(adApprovalUser.getAuOr()).AqAdBranch(branchCode).AqAdCompany(companyCode).buildApprovalQueue();
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArJobOrderEntryControllerBean ejbCreate");
    }

}