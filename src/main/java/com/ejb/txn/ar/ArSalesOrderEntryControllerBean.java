/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArSalesOrderEntryControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.ar.ArTaxCodeDetails;
import com.util.mod.ar.ArModCustomerDetails;
import com.util.mod.ar.ArModSalesOrderDetails;
import com.util.mod.ar.ArModSalesOrderLineDetails;
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

@Stateless(name = "ArSalesOrderEntryControllerEJB")
public class ArSalesOrderEntryControllerBean extends EJBContextClass implements ArSalesOrderEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    ArApprovalController arApprovalController;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalArSalesOrderHome arSalesOrderHome;
    @EJB
    private LocalArSalesOrderLineHome arSalesOrderLineHome;
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
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvPriceLevelHome invPriceLevelHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalInvLineItemTemplateHome invLineItemTemplateHome;
    @EJB
    private LocalInvTagHome invTagHome;

    public Integer saveArSoEntry(ArModSalesOrderDetails details, String PYT_NM, String TC_NM, String FC_NM, String CST_CSTMR_CODE, ArrayList solList, boolean isDraft, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException, GlobalNoApprovalApproverFoundException, GlobalNoApprovalRequesterFoundException, GlobalRecordAlreadyAssignedException {

        Debug.print("ArSalesOrderEntryControllerBean saveArSoEntry");

        LocalArSalesOrder arSalesOrder = null;

        try {

            // validate if sales order is already deleted

            try {

                if (details.getSoCode() != null) {

                    arSalesOrder = arSalesOrderHome.findByPrimaryKey(details.getSoCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // sales order void

            if (details.getSoCode() != null && details.getSoVoid() == EJBCommon.TRUE) {

                Collection arSalesOrderLines = arSalesOrder.getArSalesOrderLines();

                for (Object salesOrderLine : arSalesOrderLines) {

                    LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) salesOrderLine;

                    if (!arSalesOrderLine.getArSalesOrderInvoiceLines().isEmpty()) {
                        throw new GlobalRecordAlreadyAssignedException();
                    }
                }

                arSalesOrder.setSoVoid(EJBCommon.TRUE);
                arSalesOrder.setSoLastModifiedBy(details.getSoLastModifiedBy());
                arSalesOrder.setSoDateLastModified(details.getSoDateLastModified());

                return arSalesOrder.getSoCode();
            }

            // validate if sales order is already posted, void, approved or pending

            if (details.getSoCode() != null) {
                // report Parameter saved
                arSalesOrder.setReportParameter(details.getReportParameter());
                if (arSalesOrder.getSoApprovalStatus() != null) {

                    if (arSalesOrder.getSoApprovalStatus().equals("APPROVED") || arSalesOrder.getSoApprovalStatus().equals("N/A")) {

                        return arSalesOrder.getSoCode();

                    } else if (arSalesOrder.getSoApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (arSalesOrder.getSoPosted() == EJBCommon.TRUE) {

                    return arSalesOrder.getSoCode();

                } else if (arSalesOrder.getSoVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // validate if document number is unique document number is automatic then set next sequence

            LocalArSalesOrder arExistingSalesOrder = null;

            try {

                arExistingSalesOrder = arSalesOrderHome.findBySoDocumentNumberAndBrCode(details.getSoDocumentNumber(), AD_BRNCH, AD_CMPNY);

            } catch (FinderException ex) {
            }

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            if (details.getSoCode() == null) {

                String documentType = details.getSoDocumentType();

                try {
                    if (documentType != null) {
                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, AD_CMPNY);
                    } else {
                        documentType = "AR SALES ORDER";
                    }
                } catch (FinderException ex) {
                    documentType = "AR SALES ORDER";
                }

                try {

                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, AD_CMPNY);

                } catch (FinderException ex) {

                }

                try {

                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {

                }

                if (arExistingSalesOrder != null) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getSoDocumentNumber() == null || details.getSoDocumentNumber().trim().length() == 0)) {

                    while (true) {

                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                            try {

                                arSalesOrderHome.findBySoDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                            } catch (FinderException ex) {

                                details.setSoDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {

                                arSalesOrderHome.findBySoDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                            } catch (FinderException ex) {

                                details.setSoDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }

            } else {

                if (arExistingSalesOrder != null && !arExistingSalesOrder.getSoCode().equals(details.getSoCode())) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (arSalesOrder.getSoDocumentNumber() != details.getSoDocumentNumber() && (details.getSoDocumentNumber() == null || details.getSoDocumentNumber().trim().length() == 0)) {

                    details.setSoDocumentNumber(arSalesOrder.getSoDocumentNumber());
                }
            }

            // validate if conversion date exists

            try {

                if (details.getSoConversionDate() != null) {

                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
                    LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

                    if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getSoConversionDate(), AD_CMPNY);

                    } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getSoConversionDate(), AD_CMPNY);
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
            if (details.getSoCode() == null) {

                arSalesOrder = arSalesOrderHome.create(details.getSoDate(), details.getSoDocumentNumber(), details.getSoReferenceNumber(), details.getSoTransactionType(), details.getSoDescription(), details.getSoShippingLine(), details.getSoPort(), details.getSoBillTo(), details.getSoShipTo(), details.getSoConversionDate(), details.getSoConversionRate(), EJBCommon.FALSE, details.getSoMobile(), null, EJBCommon.FALSE, null, details.getSoCreatedBy(), details.getSoDateCreated(), details.getSoLastModifiedBy(), details.getSoDateLastModified(), null, null, null, null, EJBCommon.FALSE, EJBCommon.FALSE, details.getSoMemo(), details.getSoTransactionStatus(), AD_BRNCH, AD_CMPNY);

            } else {

                // check if critical fields are changed
                Debug.print("2");
                if (!arSalesOrder.getArTaxCode().getTcName().equals(TC_NM) || !arSalesOrder.getArCustomer().getCstCustomerCode().equals(CST_CSTMR_CODE) || !arSalesOrder.getAdPaymentTerm().getPytName().equals(PYT_NM) || solList.size() != arSalesOrder.getArSalesOrderLines().size()) {

                    isRecalculate = true;
                    Debug.print("3");
                } else if (solList.size() == arSalesOrder.getArSalesOrderLines().size()) {
                    Debug.print("4");
                    Iterator ilIter = arSalesOrder.getArSalesOrderLines().iterator();
                    Iterator ilListIter = solList.iterator();

                    while (ilIter.hasNext()) {

                        LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) ilIter.next();
                        ArModSalesOrderLineDetails mdetails = (ArModSalesOrderLineDetails) ilListIter.next();
                        if (!arSalesOrderLine.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getSolIiName()) || !arSalesOrderLine.getInvItemLocation().getInvItem().getIiDescription().equals(mdetails.getSolIiDescription()) || !arSalesOrderLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getSolLocName()) || !arSalesOrderLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getSolUomName()) || arSalesOrderLine.getSolQuantity() != mdetails.getSolQuantity() || arSalesOrderLine.getSolUnitPrice() != mdetails.getSolUnitPrice() || arSalesOrderLine.getSolTax() != mdetails.getSolTax() || arSalesOrderLine.getSolMisc() != null && (!arSalesOrderLine.getSolMisc().equals(mdetails.getSolMisc()))) {

                            isRecalculate = true;
                            break;
                        }
                        isRecalculate = false;
                    }
                } else {
                    isRecalculate = false;
                }

                arSalesOrder.setSoDate(details.getSoDate());
                arSalesOrder.setSoDocumentType(details.getSoDocumentType());
                arSalesOrder.setSoDocumentNumber(details.getSoDocumentNumber());
                arSalesOrder.setSoReferenceNumber(details.getSoReferenceNumber());
                arSalesOrder.setSoTransactionType(details.getSoTransactionType());
                arSalesOrder.setSoTransactionType(details.getSoTransactionType());
                arSalesOrder.setSoDescription(details.getSoDescription());
                arSalesOrder.setSoShippingLine(details.getSoShippingLine());
                arSalesOrder.setSoPort(details.getSoPort());
                arSalesOrder.setSoBillTo(details.getSoBillTo());
                arSalesOrder.setSoShipTo(details.getSoShipTo());
                arSalesOrder.setSoVoid(details.getSoVoid());
                arSalesOrder.setSoMobile(details.getSoMobile());
                arSalesOrder.setSoConversionDate(details.getSoConversionDate());
                arSalesOrder.setSoConversionRate(details.getSoConversionRate());
                arSalesOrder.setSoLastModifiedBy(details.getSoLastModifiedBy());
                arSalesOrder.setSoDateLastModified(details.getSoDateLastModified());
                arSalesOrder.setSoReasonForRejection(null);
                arSalesOrder.setSoMemo(details.getSoMemo());
                arSalesOrder.setSoTransactionStatus(details.getSoTransactionStatus());
            }

            arSalesOrder.setSoDocumentType(details.getSoDocumentType());

            arSalesOrder.setReportParameter(details.getReportParameter());

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, AD_CMPNY);
            arSalesOrder.setArCustomer(arCustomer);

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY);
            arSalesOrder.setAdPaymentTerm(adPaymentTerm);

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);
            arSalesOrder.setArTaxCode(arTaxCode);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
            arSalesOrder.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalArSalesperson arSalesperson = details.getSoSlpSalespersonCode() == null ? null : arSalespersonHome.findBySlpSalespersonCode(details.getSoSlpSalespersonCode(), AD_CMPNY);

            if (arSalesperson != null) {
                arSalesOrder.setArSalesperson(arSalesperson);
            }

            double ABS_TOTAL_AMOUNT = 0d;
            Debug.print("5");
            if (isRecalculate) {

                // remove all sales order line items

                Collection arSalesOrderLines = arSalesOrder.getArSalesOrderLines();

                Iterator i = arSalesOrderLines.iterator();
                Debug.print("6");
                while (i.hasNext()) {

                    LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) i.next();

                    Collection invTags = arSalesOrderLine.getInvTags();

                    Iterator x = invTags.iterator();

                    while (x.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) x.next();

                        x.remove();

                        //		  	   	    	invTag.entityRemove();
                        em.remove(invTag);
                    }

                    Debug.print("7");
                    i.remove();

                    //					arSalesOrderLine.entityRemove();
                    em.remove(arSalesOrderLine);
                    Debug.print("8");
                }

                // add new purchase order line item

                i = solList.iterator();

                LocalInvItemLocation invItemLocation = null;

                while (i.hasNext()) {

                    ArModSalesOrderLineDetails mSolDetails = (ArModSalesOrderLineDetails) i.next();

                    LocalArSalesOrderLine arSalesOrderLine = arSalesOrderLineHome.create(mSolDetails.getSolLine(), mSolDetails.getSolLineIDesc(), mSolDetails.getSolQuantity(), mSolDetails.getSolUnitPrice(), mSolDetails.getSolAmount(), mSolDetails.getSolDiscount1(), mSolDetails.getSolDiscount2(), mSolDetails.getSolDiscount3(), mSolDetails.getSolDiscount4(), mSolDetails.getSolTotalDiscount(), 0d, mSolDetails.getSolMisc(), mSolDetails.getSolTax(), AD_CMPNY);

                    arSalesOrder.addArSalesOrderLine(arSalesOrderLine);

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mSolDetails.getSolLocName(), mSolDetails.getSolIiName(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mSolDetails.getSolLine()));
                    }

                    ABS_TOTAL_AMOUNT += arSalesOrderLine.getSolAmount();

                    invItemLocation.addArSalesOrderLine(arSalesOrderLine);

                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mSolDetails.getSolUomName(), AD_CMPNY);

                    invUnitOfMeasure.addArSalesOrderLine(arSalesOrderLine);

                    if (arSalesOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc() == 1) {
                        this.createInvTagList(arSalesOrderLine, mSolDetails.getSolTagList(), AD_CMPNY);
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
                        arSalesOrder.setSoOrderStatus(OrderStatus);
                    }
                }
            }

            if (OrderStatus == "") {
                OrderStatus = "Good";
            }

            arSalesOrder.setSoOrderStatus(OrderStatus);

            // set sales order approval status

            String SO_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);

                // check if ar sales order approval is enabled

                if (adApproval.getAprEnableArSalesOrder() == EJBCommon.FALSE) {

                    SO_APPRVL_STATUS = "N/A";

                } else {

                    // check if sales order is self approved

                    LocalAdUser adUser = adUserHome.findByUsrName(details.getSoLastModifiedBy(), AD_CMPNY);

                    SO_APPRVL_STATUS = arApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getSoLastModifiedBy(), adUser.getUsrDescription(), "AR SALES ORDER", arSalesOrder.getSoCode(), arSalesOrder.getSoDocumentNumber(), arSalesOrder.getSoDate(), AD_BRNCH, AD_CMPNY);
                }

                arSalesOrder.setSoApprovalStatus(SO_APPRVL_STATUS);

                // set post purchase order

                if (SO_APPRVL_STATUS.equals("N/A")) {

                    arSalesOrder.setSoPosted(EJBCommon.TRUE);
                    arSalesOrder.setSoPosted(EJBCommon.TRUE);
                    arSalesOrder.setSoOrderStatus("Good");
                    arSalesOrder.setSoPostedBy(arSalesOrder.getSoLastModifiedBy());
                    arSalesOrder.setSoDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                }
                // arSalesOrder.setSoOrderStatus("Good");

            }

            // REFERENCE NUMBER GENERATOR
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            String newReferenceNumber = this.createCodedReferenceNumber(arSalesOrder, adCompany);

            arSalesOrder.setSoReferenceNumber(newReferenceNumber);

            return arSalesOrder.getSoCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalRecordAlreadyAssignedException |
                 GlobalNoApprovalRequesterFoundException | GlobalNoApprovalApproverFoundException |
                 GlobalInvItemLocationNotFoundException | GlobalTransactionAlreadyVoidException |
                 GlobalTransactionAlreadyPendingException | GlobalPaymentTermInvalidException |
                 GlobalConversionDateNotExistException | GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteArSoEntry(Integer SO_CODE, String AD_USR, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException {
        Debug.print("ArSalesOrderEntryControllerBean deleteArSoEntry");
        try {

            Debug.print("SO_CODE=" + SO_CODE);
            Debug.print("AD_USR=" + AD_USR);

            LocalArSalesOrder arSalesOrder = arSalesOrderHome.findByPrimaryKey(SO_CODE);
            Debug.print("1");
            if (arSalesOrder.getSoApprovalStatus() != null && arSalesOrder.getSoApprovalStatus().equals("PENDING")) {
                Debug.print("2");
                Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AR SALES ORDER", arSalesOrder.getSoCode(), AD_CMPNY);

                for (Object approvalQueue : adApprovalQueues) {

                    LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                    //                    adApprovalQueue.entityRemove();
                    em.remove(adApprovalQueue);
                }
                Debug.print("3");
            }

            adDeleteAuditTrailHome.create("AR SALES ORDER", arSalesOrder.getSoDate(), arSalesOrder.getSoDocumentNumber(), arSalesOrder.getSoReferenceNumber(), 0d, AD_USR, new Date(), AD_CMPNY);
            Debug.print("4" + arSalesOrder.getSoCode() + "SO Number" + arSalesOrder.getSoDocumentNumber());
            //		    arSalesOrder.entityRemove();
            em.remove(arSalesOrder);

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
        Debug.print("ArSalesOrderEntryControllerBean getArTcByTcName");
        ArrayList list = new ArrayList();
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

        Debug.print("ArSalesOrderEntryControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer AD_CMPNY) {
        Debug.print("ArSalesOrderEntryControllerBean getInvGpQuantityPrecisionUnit");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfArJournalLineNumber(Integer AD_CMPNY) {
        Debug.print("ArSalesEntryControllerBean getAdPrfArJournalLineNumber");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfArInvoiceLineNumber();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModSalesOrderDetails getArSoBySoCode(Integer SO_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArSalesOrderEntryControllerBean getArSoBySoCode");
        try {

            LocalArSalesOrder arSalesOrder = null;

            try {

                arSalesOrder = arSalesOrderHome.findByPrimaryKey(SO_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get sales order lines if any

            Collection arSalesOrderLines = arSalesOrder.getArSalesOrderLines();

            for (Object salesOrderLine : arSalesOrderLines) {

                LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) salesOrderLine;

                ArModSalesOrderLineDetails solDetails = new ArModSalesOrderLineDetails();

                solDetails.setSolCode(arSalesOrderLine.getSolCode());
                solDetails.setSolLine(arSalesOrderLine.getSolLine());
                solDetails.setSolIiName(arSalesOrderLine.getInvItemLocation().getInvItem().getIiName());
                solDetails.setSolLocName(arSalesOrderLine.getInvItemLocation().getInvLocation().getLocName());
                solDetails.setSolQuantity(arSalesOrderLine.getSolQuantity());
                solDetails.setSolUomName(arSalesOrderLine.getInvUnitOfMeasure().getUomName());
                solDetails.setSolUnitPrice(arSalesOrderLine.getSolUnitPrice());
                solDetails.setSolIiDescription(arSalesOrderLine.getInvItemLocation().getInvItem().getIiDescription());
                solDetails.setSolEDesc(arSalesOrderLine.getSolLineIDesc());
                solDetails.setSolDiscount1(arSalesOrderLine.getSolDiscount1());
                solDetails.setSolDiscount2(arSalesOrderLine.getSolDiscount2());
                solDetails.setSolDiscount3(arSalesOrderLine.getSolDiscount3());
                solDetails.setSolDiscount4(arSalesOrderLine.getSolDiscount4());
                solDetails.setSolTotalDiscount(arSalesOrderLine.getSolTotalDiscount());
                solDetails.setSolMisc(arSalesOrderLine.getSolMisc());
                solDetails.setSolAmount(arSalesOrderLine.getSolAmount());
                solDetails.setSolTax(arSalesOrderLine.getSolTax());

                solDetails.setTraceMisc(arSalesOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc());
                Debug.print(solDetails.getTraceMisc() + "<== getTraceMisc under getInvAdjByAdjCode controllerbean");
                if (solDetails.getTraceMisc() == 1) {

                    ArrayList tagList = new ArrayList();

                    tagList = this.getInvTagList(arSalesOrderLine);
                    solDetails.setSolTagList(tagList);
                    solDetails.setTraceMisc(solDetails.getTraceMisc());
                }

                list.add(solDetails);
            }

            ArModSalesOrderDetails mSoDetails = new ArModSalesOrderDetails();

            mSoDetails.setSoCode(arSalesOrder.getSoCode());
            mSoDetails.setSoDate(arSalesOrder.getSoDate());
            mSoDetails.setSoDocumentType(arSalesOrder.getSoDocumentType());
            mSoDetails.setSoDocumentNumber(arSalesOrder.getSoDocumentNumber());
            mSoDetails.setSoReferenceNumber(arSalesOrder.getSoReferenceNumber());
            mSoDetails.setSoTransactionType(arSalesOrder.getSoTransactionType());
            mSoDetails.setSoDescription(arSalesOrder.getSoDescription());
            mSoDetails.setSoShippingLine(arSalesOrder.getSoShippingLine());
            mSoDetails.setSoPort(arSalesOrder.getSoPort());
            mSoDetails.setSoVoid(arSalesOrder.getSoVoid());
            mSoDetails.setSoMobile(arSalesOrder.getSoMobile());
            mSoDetails.setSoBillTo(arSalesOrder.getSoBillTo());
            mSoDetails.setSoShipTo(arSalesOrder.getSoShipTo());
            mSoDetails.setSoConversionDate(arSalesOrder.getSoConversionDate());
            mSoDetails.setSoConversionRate(arSalesOrder.getSoConversionRate());
            mSoDetails.setSoApprovalStatus(arSalesOrder.getSoApprovalStatus());
            mSoDetails.setSoPosted(arSalesOrder.getSoPosted());
            mSoDetails.setSoCreatedBy(arSalesOrder.getSoCreatedBy());
            mSoDetails.setSoDateCreated(arSalesOrder.getSoDateCreated());
            mSoDetails.setSoLastModifiedBy(arSalesOrder.getSoLastModifiedBy());
            mSoDetails.setSoDateLastModified(arSalesOrder.getSoDateLastModified());
            mSoDetails.setSoApprovedRejectedBy(arSalesOrder.getSoApprovedRejectedBy());
            mSoDetails.setSoDateApprovedRejected(arSalesOrder.getSoDateApprovedRejected());
            mSoDetails.setSoPostedBy(arSalesOrder.getSoPostedBy());
            mSoDetails.setSoDatePosted(arSalesOrder.getSoDatePosted());
            mSoDetails.setSoReasonForRejection(arSalesOrder.getSoReasonForRejection());
            mSoDetails.setSoCstCustomerCode(arSalesOrder.getArCustomer().getCstCustomerCode());
            mSoDetails.setSoCstName(arSalesOrder.getArCustomer().getCstName());
            mSoDetails.setSoPytName(arSalesOrder.getAdPaymentTerm().getPytName());
            mSoDetails.setSoTcName(arSalesOrder.getArTaxCode().getTcName());
            mSoDetails.setSoFcName(arSalesOrder.getGlFunctionalCurrency().getFcName());
            mSoDetails.setSoTcRate(arSalesOrder.getArTaxCode().getTcRate());
            mSoDetails.setSoTcType(arSalesOrder.getArTaxCode().getTcType());
            mSoDetails.setSoApprovedRejectedBy(arSalesOrder.getSoApprovedRejectedBy());
            mSoDetails.setSoDateApprovedRejected(arSalesOrder.getSoDateApprovedRejected());
            mSoDetails.setSoMemo(arSalesOrder.getSoMemo());
            mSoDetails.setSoTransactionStatus(arSalesOrder.getSoTransactionStatus());
            mSoDetails.setSoOrderStatus(arSalesOrder.getSoOrderStatus());
            mSoDetails.setReportParameter(arSalesOrder.getReportParameter());

            if (arSalesOrder.getArSalesperson() != null) {

                mSoDetails.setSoSlpSalespersonCode(arSalesOrder.getArSalesperson().getSlpSalespersonCode());
                mSoDetails.setSoSlpName(arSalesOrder.getArSalesperson().getSlpName());
            }

            // get So Advance if have
            double SO_ADVNC_AMNT = 0d;

            for (Object o : arSalesOrder.getCmAdjustments()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) o;

                if (cmAdjustment.getAdjVoid() == EJBCommon.FALSE && cmAdjustment.getAdjReconciled() == EJBCommon.FALSE) {
                    SO_ADVNC_AMNT += cmAdjustment.getAdjAmount();
                }
            }

            mSoDetails.setSoAdvanceAmount(SO_ADVNC_AMNT);

            mSoDetails.setSoSolList(list);

            return mSoDetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModCustomerDetails getArCstByCstCustomerCode(String CST_CSTMR_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArSalesOrderEntryControllerBean getArCstByCstCustomerCode");
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
        Debug.print("ArSalesOrderEntryControllerBean getInvIiSalesPriceByIiNameAndUomName");
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

    public ArrayList getAdApprovalNotifiedUsersBySoCode(Integer SO_CODE, Integer AD_CMPNY) {
        Debug.print("ArSalesOrderEntryControllerBean getAdApprovalNotifiedUsersBySoCode");
        ArrayList list = new ArrayList();
        try {

            LocalArSalesOrder arSalesOrder = arSalesOrderHome.findByPrimaryKey(SO_CODE);

            if (arSalesOrder.getSoPosted() == EJBCommon.TRUE) {

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
        Debug.print("ArSalesOrderEntryControllerBean getFrRateByFrNameAndFrDate");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);

            double CONVERSION_RATE = 1;

            // Get functional currency rate

            Debug.print("FC_NM=" + FC_NM);
            Debug.print("CONVERSION_DATE=" + CONVERSION_DATE);

            if (!FC_NM.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), CONVERSION_DATE, AD_CMPNY);

                Debug.print("pasok 1");
                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();
                Debug.print("rate is : " + CONVERSION_RATE);
            }

            // Get set of book functional currency rate if necessary
            Debug.print("2nd curr: " + adCompany.getGlFunctionalCurrency().getFcName());
            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                Debug.print("fcCode = " + adCompany.getGlFunctionalCurrency().getFcCode());

                Debug.print("conversion date = " + CONVERSION_DATE);

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, AD_CMPNY);
                Debug.print("pasok 2");
                CONVERSION_RATE = CONVERSION_RATE / glCompanyFunctionalCurrencyRate.getFrXToUsd();
            }
            Debug.print("pasok end");
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
        Debug.print("ArSalesOrderEntryControllerBean getInvLitByCstLitName");
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
        Debug.print("ArSalesOrderEntryControllerBean getInvIiClassByIiName");
        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);

            return invItem.getIiClass();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfArEnablePaymentTerm(Integer AD_CMPNY) {
        Debug.print("ArSalesOrderEntryControllerBean getAdPrfArEnablePaymentTerm");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfArEnablePaymentTerm();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public boolean getSoTraceMisc(String II_NAME, Integer AD_CMPNY) {
        Debug.print("ArSalesOrderEntryControllerBean getSoTraceMisc");
        Collection invLocations = null;
        ArrayList list = new ArrayList();
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

    public byte getAdPrfArDisableSalesPrice(Integer AD_CMPNY) {
        Debug.print("ArSalesOrderEntryControllerBean getAdPrfArDisableSalesPrice");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfArDisableSalesPrice();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private String createCodedReferenceNumber(LocalArSalesOrder arSalesOrder, LocalAdCompany adCompany) {
        Debug.print("create createCodedReferenceNumber");
        String newReferenceNumber = "";
        if (!arSalesOrder.getSoReferenceNumber().equals("")) {
            return arSalesOrder.getSoReferenceNumber();
        }
        // ICEI
        if (adCompany.getCmpShortName().equals("ICEI") && arSalesOrder.getSoDocumentType() != null) {

            // "TransactionType" + "-" + "last two digit date year" + "-" + "sonumber"

            // get trans
            // String transTypeWord =
            // arSalesOrder.getSoTransactionType().equals("")?"F":arSalesOrder.getSoTransactionType();

            // get trans
            if (arSalesOrder.getSoDocumentType().length() > 0) {
                Debug.print("sad");
                String transTypeWord = arSalesOrder.getSoDocumentType();

                // get date year (2 digit)
                DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
                String formattedDate = df.format(arSalesOrder.getSoDate());

                // get SO Number
                String soNumber = arSalesOrder.getSoDocumentNumber();

                newReferenceNumber = transTypeWord + "-" + formattedDate + "-" + soNumber;
            }
        }
        return newReferenceNumber;
    }

    public boolean getInvTraceMisc(String II_NAME, Integer AD_CMPNY) {
        Debug.print("ArSalesOrderEntryControllerBean getInvLocAll");
        Collection invLocations = null;
        ArrayList list = new ArrayList();
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

    private void createInvTagList(LocalArSalesOrderLine arSalesOrderLine, ArrayList list, Integer AD_CMPNY) throws Exception {
        Debug.print("ArSalesOrderEntryControllerBean createInvTagList");
        try {
            // Iterator t = apPurchaseOrderLine.getInvTag().iterator();
            Iterator t = list.iterator();

            LocalInvTag invTag = null;
            while (t.hasNext()) {
                InvModTagListDetails tgLstDetails = (InvModTagListDetails) t.next();
                if (tgLstDetails.getTgCode() == null) {
                    invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), AD_CMPNY, tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());

                    invTag.setArSalesOrderLine(arSalesOrderLine);
                    invTag.setInvItemLocation(arSalesOrderLine.getInvItemLocation());
                    LocalAdUser adUser = null;
                    try {
                        adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), AD_CMPNY);
                    } catch (FinderException ex) {

                    }
                    invTag.setAdUser(adUser);
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    private ArrayList getInvTagList(LocalArSalesOrderLine arSalesOrderLine) {
        ArrayList list = new ArrayList();
        Collection invTags = arSalesOrderLine.getInvTags();
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

    public byte getAdPrfSalesOrderSalespersonRequired(Integer AD_CMPNY) {
        Debug.print("ArSalesOrderEntryControllerBean getAdPrfSalesOrderSalespersonRequired");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfArSoSalespersonRequired();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArSalesOrderEntryControllerBean ejbCreate");
    }

}