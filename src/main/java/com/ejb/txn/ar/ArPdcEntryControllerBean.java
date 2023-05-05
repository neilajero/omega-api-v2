/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArPdcEntryControllerBean
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
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.dao.inv.LocalInvItemLocationHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureConversionHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureHome;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;
import com.ejb.entities.inv.LocalInvUnitOfMeasureConversion;
import com.ejb.exception.ar.ArINVOverapplicationNotAllowedException;
import com.ejb.exception.ar.ArRCTInvoiceHasNoWTaxCodeException;
import com.ejb.exception.global.*;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ar.ArPdcDetails;
import com.util.ar.ArStandardMemoLineDetails;
import com.util.mod.ar.*;
import com.util.mod.inv.InvModUnitOfMeasureDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Stateless(name = "ArPdcEntryControllerEJB")
public class ArPdcEntryControllerBean extends EJBContextClass implements ArPdcEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalArPdcHome arPdcHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalAdDiscountHome adDiscountHome;
    @EJB
    private LocalAdPaymentScheduleHome adPaymentScheduleHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalArAppliedInvoiceHome arAppliedInvoiceHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArInvoiceLineHome arInvoiceLineHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;

    public ArrayList getInvUomByIiName(String II_NM, Integer AD_CMPNY) {

        Debug.print("ArPdcEntryControllerBean getInvUomByIiName");
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

    public double getInvIiSalesPriceByIiNameAndUomName(String II_NM, String UOM_NM, Integer AD_CMPNY) {

        Debug.print("ArPdcEntryControllerBean getInvIiSalesPriceByIiNameAndUomName");
        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            return EJBCommon.roundIt(invItem.getIiSalesPrice() * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(AD_CMPNY));

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfArInvoiceLineNumber(Integer AD_CMPNY) {

        Debug.print("ArPdcEntryControllerBean getAdPrfArInvoiceLineNumber");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfArInvoiceLineNumber();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModPdcDetails getArPdcByPdcCode(Integer PDC_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArPdcEntryControllerBean getArPdcByPdcCode");
        try {

            LocalArPdc arPdc = null;
            LocalArTaxCode arTaxCode = null;

            try {

                arPdc = arPdcHome.findByPrimaryKey(PDC_CODE);
                arTaxCode = arPdc.getArTaxCode();

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get invoice line items & applied invoices if any

            Collection arInvoiceLineItems = arPdc.getArInvoiceLineItems();
            Collection arAppliedInvoices = arPdc.getArAppliedInvoices();

            if (!arInvoiceLineItems.isEmpty()) {

                Debug.print("Hi Items");

                // Items

                for (Object invoiceLineItem : arInvoiceLineItems) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;

                    ArModInvoiceLineItemDetails iliDetails = new ArModInvoiceLineItemDetails();

                    iliDetails.setIliCode(arInvoiceLineItem.getIliCode());
                    iliDetails.setIliLine(arInvoiceLineItem.getIliLine());
                    iliDetails.setIliQuantity(arInvoiceLineItem.getIliQuantity());
                    iliDetails.setIliUnitPrice(arInvoiceLineItem.getIliUnitPrice());
                    iliDetails.setIliIiName(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                    iliDetails.setIliLocName(arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName());
                    iliDetails.setIliUomName(arInvoiceLineItem.getInvUnitOfMeasure().getUomName());
                    iliDetails.setIliIiDescription(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiDescription());

                    list.add(iliDetails);
                }

            } else if (!arAppliedInvoices.isEmpty()) {

                Debug.print("Hi PR");

                // Provisional Receipt

                for (Object appliedInvoice : arAppliedInvoices) {

                    LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoice;

                    ArModAppliedInvoiceDetails aiDetails = new ArModAppliedInvoiceDetails();

                    aiDetails.setAiApplyAmount(arAppliedInvoice.getAiApplyAmount());
                    aiDetails.setAiCreditableWTax(arAppliedInvoice.getAiCreditableWTax());
                    aiDetails.setAiDiscountAmount(arAppliedInvoice.getAiDiscountAmount());
                    aiDetails.setAiAppliedDeposit(arAppliedInvoice.getAiAppliedDeposit());
                    aiDetails.setAiAllocatedPaymentAmount(arAppliedInvoice.getAiAllocatedPaymentAmount());
                    aiDetails.setAiIpsCode(arAppliedInvoice.getArInvoicePaymentSchedule().getIpsCode());
                    aiDetails.setAiIpsNumber(arAppliedInvoice.getArInvoicePaymentSchedule().getIpsNumber());
                    aiDetails.setAiIpsDueDate(arAppliedInvoice.getArInvoicePaymentSchedule().getIpsDueDate());
                    aiDetails.setAiIpsAmountDue(arAppliedInvoice.getArInvoicePaymentSchedule().getIpsAmountDue() - arAppliedInvoice.getArInvoicePaymentSchedule().getIpsAmountPaid());
                    aiDetails.setAiIpsInvNumber(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvNumber());
                    aiDetails.setAiIpsInvReferenceNumber(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvReferenceNumber());
                    aiDetails.setAiIpsInvFcName(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName());

                    list.add(aiDetails);
                }

            } else {

                Debug.print("Hi Memo");

                // get invoice lines (memo lines)

                Collection arInvoiceLines = arPdc.getArInvoiceLines();

                for (Object invoiceLine : arInvoiceLines) {

                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) invoiceLine;

                    ArModInvoiceLineDetails mdetails = new ArModInvoiceLineDetails();

                    mdetails.setIlCode(arInvoiceLine.getIlCode());
                    mdetails.setIlDescription(arInvoiceLine.getIlDescription());
                    mdetails.setIlQuantity(arInvoiceLine.getIlQuantity());
                    mdetails.setIlUnitPrice(arInvoiceLine.getIlUnitPrice());
                    if (arTaxCode.getTcType().equals("INCLUSIVE")) {

                        mdetails.setIlAmount(arInvoiceLine.getIlAmount() + arInvoiceLine.getIlTaxAmount());

                    } else {

                        mdetails.setIlAmount(arInvoiceLine.getIlAmount());
                    }
                    mdetails.setIlTax(arInvoiceLine.getIlTax());
                    mdetails.setIlSmlName(arInvoiceLine.getArStandardMemoLine().getSmlName());

                    list.add(mdetails);
                }
            }

            ArModPdcDetails mPdcDetails = new ArModPdcDetails();

            mPdcDetails.setPdcCode(arPdc.getPdcCode());
            mPdcDetails.setPdcStatus(arPdc.getPdcStatus());
            mPdcDetails.setPdcLvShift(arPdc.getPdcLvShift());
            mPdcDetails.setPdcCheckNumber(arPdc.getPdcCheckNumber());
            mPdcDetails.setPdcReferenceNumber(arPdc.getPdcReferenceNumber());
            mPdcDetails.setPdcDateReceived(arPdc.getPdcDateReceived());
            mPdcDetails.setPdcMaturityDate(arPdc.getPdcMaturityDate());
            mPdcDetails.setPdcDescription(arPdc.getPdcDescription());
            mPdcDetails.setPdcCancelled(arPdc.getPdcCancelled());
            mPdcDetails.setPdcAmount(arPdc.getPdcAmount());
            mPdcDetails.setPdcConversionRate(arPdc.getPdcConversionRate());
            mPdcDetails.setPdcConversionDate(arPdc.getPdcConversionDate());
            mPdcDetails.setPdcLvFreight(arPdc.getPdcLvFreight());
            mPdcDetails.setPdcApprovalStatus(arPdc.getPdcApprovalStatus());
            mPdcDetails.setPdcPosted(arPdc.getPdcPosted());
            mPdcDetails.setPdcCreatedBy(arPdc.getPdcCreatedBy());
            mPdcDetails.setPdcDateCreated(arPdc.getPdcDateCreated());
            mPdcDetails.setPdcLastModifiedBy(arPdc.getPdcLastModifiedBy());
            mPdcDetails.setPdcDateLastModified(arPdc.getPdcDateLastModified());
            mPdcDetails.setPdcApprovedRejectedBy(arPdc.getPdcApprovedRejectedBy());
            mPdcDetails.setPdcDateApprovedRejected(arPdc.getPdcDateApprovedRejected());
            mPdcDetails.setPdcPostedBy(arPdc.getPdcPostedBy());
            mPdcDetails.setPdcDatePosted(arPdc.getPdcDatePosted());
            mPdcDetails.setPdcFcName(arPdc.getGlFunctionalCurrency().getFcName());

            if (arPdc.getArTaxCode() != null && arPdc.getArWithholdingTaxCode() != null && arPdc.getAdPaymentTerm() != null) {
                mPdcDetails.setPdcTcName(arPdc.getArTaxCode().getTcName());
                mPdcDetails.setPdcWtcName(arPdc.getArWithholdingTaxCode().getWtcName());
                mPdcDetails.setPdcPytName(arPdc.getAdPaymentTerm().getPytName());
            }

            if (arPdc.getAdBankAccount() != null) {
                mPdcDetails.setRctBaName(arPdc.getAdBankAccount().getBaName());
            }

            mPdcDetails.setPdcCstCustomerCode(arPdc.getArCustomer().getCstCustomerCode());
            mPdcDetails.setPdcEffectivityDate(arPdc.getPdcEffectivityDate());

            if (!arInvoiceLineItems.isEmpty()) {

                mPdcDetails.setPdcIliList(list);

            } else if (!arAppliedInvoices.isEmpty()) {

                mPdcDetails.setPdcRctList(list);

            } else {

                mPdcDetails.setPdcIlList(list);
            }

            return mPdcDetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModCustomerDetails getArCstByCstCustomerCode(String CST_CSTMR_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArPdcEntryControllerBean getArCstByCstCustomerCode");
        try {

            LocalArCustomer arCustomer = null;

            try {

                arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, AD_CMPNY);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArModCustomerDetails mdetails = new ArModCustomerDetails();

            mdetails.setCstPytName(arCustomer.getAdPaymentTerm() != null ? arCustomer.getAdPaymentTerm().getPytName() : null);
            mdetails.setCstCcTcName(arCustomer.getArCustomerClass().getArTaxCode() != null ? arCustomer.getArCustomerClass().getArTaxCode().getTcName() : null);
            mdetails.setCstCcWtcName(arCustomer.getArCustomerClass().getArWithholdingTaxCode() != null ? arCustomer.getArCustomerClass().getArWithholdingTaxCode().getWtcName() : null);
            if (arCustomer.getAdBankAccount() != null) {
                mdetails.setCstCtBaName(arCustomer.getAdBankAccount().getBaName());
            }

            mdetails.setCstName(arCustomer.getCstName());

            return mdetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArStandardMemoLineDetails getArSmlBySmlName(String SML_NM, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArPdcEntryControllerBean getArSmlBySmlName");
        try {

            LocalArStandardMemoLine arStandardMemoLine = null;

            try {

                arStandardMemoLine = arStandardMemoLineHome.findBySmlName(SML_NM, AD_CMPNY);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArStandardMemoLineDetails details = new ArStandardMemoLineDetails();
            details.setSmlDescription(arStandardMemoLine.getSmlDescription());
            details.setSmlUnitPrice(arStandardMemoLine.getSmlUnitPrice());
            details.setSmlTax(arStandardMemoLine.getSmlTax());

            return details;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveArPdcEntry(ArPdcDetails details, String PYT_NM, String TC_NM, String WTC_NM, String FC_NM, String CST_CSTMR_CODE, ArrayList ilList, boolean isDraft, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException {

        Debug.print("ArPdcEntryControllerBean saveArPdcEntry");
        LocalArPdc arPdc = null;
        try {

            // validate if pdc is already deleted

            try {

                if (details.getPdcCode() != null) {

                    arPdc = arPdcHome.findByPrimaryKey(details.getPdcCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if pdc is already posted, cancelled, approved or pending

            if (details.getPdcCode() != null && details.getPdcCancelled() == EJBCommon.FALSE) {

                if (arPdc.getPdcApprovalStatus() != null) {

                    if (arPdc.getPdcApprovalStatus().equals("APPROVED") || arPdc.getPdcApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (arPdc.getPdcApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (arPdc.getPdcPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (arPdc.getPdcCancelled() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // check if reference number is unique
            LocalArPdc arExistingPdc = null;

            try {

                arExistingPdc = arPdcHome.findPdcByReferenceNumber(details.getPdcReferenceNumber(), AD_CMPNY);

                if (!arExistingPdc.getPdcCode().equals(details.getPdcCode())) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }

            } catch (FinderException ex) {
            }

            // pdc cancelled

            if (details.getPdcCode() != null && details.getPdcCancelled() == EJBCommon.TRUE) {

                arPdc.setPdcCancelled(EJBCommon.TRUE);
                arPdc.setPdcStatus("CANCELLED");
                arPdc.setPdcLastModifiedBy(details.getPdcLastModifiedBy());
                arPdc.setPdcDateLastModified(details.getPdcDateLastModified());

                return arPdc.getPdcCode();
            }

            // validate if conversion date exists

            try {

                if (details.getPdcConversionDate() != null) {

                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
                    LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

                    if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getPdcConversionDate(), AD_CMPNY);

                    } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getPdcConversionDate(), AD_CMPNY);
                    }
                }

            } catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // validate if payment term has at least one payment schedule

            if (adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY).getAdPaymentSchedules().isEmpty()) {

                throw new GlobalPaymentTermInvalidException();
            }

            // used in checking if invoice should re-generate distribution
            // records and re-calculate taxes
            boolean isRecalculate = true;

            // create pdc

            if (details.getPdcCode() == null) {

                arPdc = arPdcHome.create(details.getPdcStatus(), details.getPdcLvShift(), details.getPdcCheckNumber(), details.getPdcReferenceNumber(), details.getPdcDateReceived(), details.getPdcMaturityDate(), details.getPdcDescription(), EJBCommon.FALSE, details.getPdcAmount(), details.getPdcConversionRate(), details.getPdcConversionDate(), details.getPdcLvFreight(), null, EJBCommon.FALSE, details.getPdcCreatedBy(), details.getPdcDateCreated(), details.getPdcLastModifiedBy(), details.getPdcDateLastModified(), null, null, null, null, EJBCommon.FALSE, details.getPdcEffectivityDate(), AD_BRNCH, AD_CMPNY);

            } else {

                // check if critical fields are changed

                if (!arPdc.getArTaxCode().getTcName().equals(TC_NM) || !arPdc.getArWithholdingTaxCode().getWtcName().equals(WTC_NM) || !arPdc.getArCustomer().getCstCustomerCode().equals(CST_CSTMR_CODE) || !arPdc.getAdPaymentTerm().getPytName().equals(PYT_NM) || !arPdc.getPdcEffectivityDate().equals(details.getPdcEffectivityDate()) || ilList.size() != arPdc.getArInvoiceLines().size()) {

                    isRecalculate = true;

                } else if (ilList.size() == arPdc.getArInvoiceLines().size()) {

                    Iterator ilIter = arPdc.getArInvoiceLines().iterator();
                    Iterator ilListIter = ilList.iterator();

                    while (ilIter.hasNext()) {

                        LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) ilIter.next();
                        ArModInvoiceLineDetails mdetails = (ArModInvoiceLineDetails) ilListIter.next();

                        if (!arInvoiceLine.getArStandardMemoLine().getSmlName().equals(mdetails.getIlSmlName()) || arInvoiceLine.getIlQuantity() != mdetails.getIlQuantity() || arInvoiceLine.getIlUnitPrice() != mdetails.getIlUnitPrice() || arInvoiceLine.getIlTax() != mdetails.getIlTax()) {

                            isRecalculate = true;
                            break;
                        }

                        arInvoiceLine.setIlDescription(mdetails.getIlDescription());

                        isRecalculate = false;
                    }

                } else {

                    isRecalculate = false;
                }

                arPdc.setPdcStatus(details.getPdcStatus());
                arPdc.setPdcLvShift(details.getPdcLvShift());
                arPdc.setPdcCheckNumber(details.getPdcCheckNumber());
                arPdc.setPdcReferenceNumber(details.getPdcReferenceNumber());
                arPdc.setPdcDateReceived(details.getPdcDateReceived());
                arPdc.setPdcMaturityDate(details.getPdcMaturityDate());
                arPdc.setPdcDescription(details.getPdcDescription());
                arPdc.setPdcAmount(details.getPdcAmount());
                arPdc.setPdcConversionDate(details.getPdcConversionDate());
                arPdc.setPdcConversionRate(details.getPdcConversionRate());
                arPdc.setPdcLvFreight(details.getPdcLvFreight());
                arPdc.setPdcLastModifiedBy(details.getPdcLastModifiedBy());
                arPdc.setPdcDateLastModified(details.getPdcDateLastModified());
                arPdc.setPdcEffectivityDate(details.getPdcEffectivityDate());
            }

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY);
            adPaymentTerm.addArPdc(arPdc);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
            glFunctionalCurrency.addArPdc(arPdc);

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);
            arTaxCode.addArPdc(arPdc);

            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(WTC_NM, AD_CMPNY);
            arWithholdingTaxCode.addArPdc(arPdc);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, AD_CMPNY);
            arCustomer.addArPdc(arPdc);

            if (isRecalculate) {

                // remove all invoice lines

                Collection arInvoiceLines = arPdc.getArInvoiceLines();

                Iterator i = arInvoiceLines.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) i.next();

                    i.remove();

                    //					arInvoiceLine.entityRemove();
                    em.remove(arInvoiceLine);
                }

                // remove all invoice line items

                Collection arInvoiceLineItems = arPdc.getArInvoiceLineItems();

                i = arInvoiceLineItems.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();

                    i.remove();

                    //					arInvoiceLineItem.entityRemove();
                    em.remove(arInvoiceLineItem);
                }

                // release ips locks and remove all applied invoices

                Collection arAppliedInvoices = arPdc.getArAppliedInvoices();

                i = arAppliedInvoices.iterator();

                while (i.hasNext()) {

                    LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) i.next();

                    arAppliedInvoice.getArInvoicePaymentSchedule().setIpsLock(EJBCommon.FALSE);

                    i.remove();

                    //					arAppliedInvoice.entityRemove();
                    em.remove(arAppliedInvoice);
                }

                // add new invoice lines

                double TOTAL_TAX = 0d;
                double TOTAL_LINE = 0d;

                i = ilList.iterator();

                while (i.hasNext()) {

                    ArModInvoiceLineDetails mPdcDetails = (ArModInvoiceLineDetails) i.next();

                    LocalArInvoiceLine arInvoiceLine = this.addArIlEntry(mPdcDetails, arPdc, AD_CMPNY);

                    TOTAL_LINE += arInvoiceLine.getIlAmount();
                    TOTAL_TAX += arInvoiceLine.getIlTaxAmount();
                }

                // get wtax if any

                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

                double W_TAX_AMOUNT = 0d;

                if (arWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals("INVOICE")) {

                    W_TAX_AMOUNT = EJBCommon.roundIt(TOTAL_LINE * (arWithholdingTaxCode.getWtcRate() / 100), this.getGlFcPrecisionUnit(AD_CMPNY));
                }

                // get payment discount if any

                double DISCOUNT_AMT = 0d;

                if (adPaymentTerm.getPytDiscountOnInvoice() == EJBCommon.TRUE) {

                    Collection adPaymentSchedules = adPaymentScheduleHome.findByPytCode(adPaymentTerm.getPytCode(), AD_CMPNY);
                    ArrayList adPaymentScheduleList = new ArrayList(adPaymentSchedules);
                    LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) adPaymentScheduleList.get(0);

                    Collection adDiscounts = adDiscountHome.findByPsCode(adPaymentSchedule.getPsCode(), AD_CMPNY);
                    ArrayList adDiscountList = new ArrayList(adDiscounts);
                    LocalAdDiscount adDiscount = (LocalAdDiscount) adDiscountList.get(0);

                    double rate = adDiscount.getDscDiscountPercent();
                    DISCOUNT_AMT = (TOTAL_LINE + TOTAL_TAX) * (rate / 100d);
                }

                // set pdc amount

                arPdc.setPdcAmount(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT_AMT);
            }

            String PDC_APPRVL_STATUS = null;

            if (!isDraft) {

                // set pdc post status

                arPdc.setPdcPosted(EJBCommon.TRUE);
                arPdc.setPdcPostedBy(arPdc.getPdcLastModifiedBy());
                arPdc.setPdcDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                PDC_APPRVL_STATUS = "N/A";
            }

            // set pdc approval status

            arPdc.setPdcApprovalStatus(PDC_APPRVL_STATUS);

            return arPdc.getPdcCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalTransactionAlreadyVoidException |
                 GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
                 GlobalTransactionAlreadyApprovedException | GlobalPaymentTermInvalidException |
                 GlobalConversionDateNotExistException | GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveArPdcIliEntry(ArPdcDetails details, String PYT_NM, String TC_NM, String WTC_NM, String FC_NM, String CST_CSTMR_CODE, ArrayList iliList, boolean isDraft, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException {

        Debug.print("ArPdcEntryControllerBean saveArPdcIliEntry");
        LocalArPdc arPdc = null;
        try {

            // validate if invoice is already deleted

            try {

                if (details.getPdcCode() != null) {

                    arPdc = arPdcHome.findByPrimaryKey(details.getPdcCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if invoice is already posted, void, arproved or pending

            if (details.getPdcCode() != null) {

                if (arPdc.getPdcApprovalStatus() != null) {

                    if (arPdc.getPdcApprovalStatus().equals("APPROVED") || arPdc.getPdcApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (arPdc.getPdcApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (arPdc.getPdcPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (arPdc.getPdcCancelled() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // check if reference number is unique
            LocalArPdc arExistingPdc = null;

            try {

                arExistingPdc = arPdcHome.findPdcByReferenceNumber(details.getPdcReferenceNumber(), AD_CMPNY);

                if (!arExistingPdc.getPdcCode().equals(details.getPdcCode())) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }

            } catch (FinderException ex) {
            }

            // pdc cancelled

            if (details.getPdcCode() != null && details.getPdcCancelled() == EJBCommon.TRUE && arPdc.getPdcPosted() == EJBCommon.FALSE) {

                arPdc.setPdcCancelled(EJBCommon.TRUE);
                arPdc.setPdcStatus("CANCELLED");
                arPdc.setPdcLastModifiedBy(details.getPdcLastModifiedBy());
                arPdc.setPdcDateLastModified(details.getPdcDateLastModified());

                return arPdc.getPdcCode();
            }

            // validate if conversion date exists

            try {

                if (details.getPdcConversionDate() != null) {

                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getPdcConversionDate(), AD_CMPNY);
                }

            } catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // validate if payment term has at least one payment schedule

            if (adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY).getAdPaymentSchedules().isEmpty()) {

                throw new GlobalPaymentTermInvalidException();
            }

            boolean isRecalculate = true;

            // create pdc

            if (details.getPdcCode() == null) {

                arPdc = arPdcHome.create(details.getPdcStatus(), details.getPdcLvShift(), details.getPdcCheckNumber(), details.getPdcReferenceNumber(), details.getPdcDateReceived(), details.getPdcMaturityDate(), details.getPdcDescription(), EJBCommon.FALSE, details.getPdcAmount(), details.getPdcConversionRate(), details.getPdcConversionDate(), details.getPdcLvFreight(), null, EJBCommon.FALSE, details.getPdcCreatedBy(), details.getPdcDateCreated(), details.getPdcLastModifiedBy(), details.getPdcDateLastModified(), null, null, null, null, EJBCommon.FALSE, details.getPdcEffectivityDate(), AD_BRNCH, AD_CMPNY);

            } else {

                // check if critical fields are changed

                if (!arPdc.getArTaxCode().getTcName().equals(TC_NM) || !arPdc.getArWithholdingTaxCode().getWtcName().equals(WTC_NM) || !arPdc.getArCustomer().getCstCustomerCode().equals(CST_CSTMR_CODE) || !arPdc.getAdPaymentTerm().getPytName().equals(PYT_NM) || !arPdc.getPdcEffectivityDate().equals(details.getPdcEffectivityDate()) || iliList.size() != arPdc.getArInvoiceLineItems().size()) {

                    isRecalculate = true;

                } else if (iliList.size() == arPdc.getArInvoiceLineItems().size()) {

                    Iterator iliIter = arPdc.getArInvoiceLineItems().iterator();
                    Iterator iliListIter = iliList.iterator();

                    while (iliIter.hasNext()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) iliIter.next();
                        ArModInvoiceLineItemDetails mdetails = (ArModInvoiceLineItemDetails) iliListIter.next();

                        if (!arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getIliIiName()) || !arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getIliLocName()) || !arInvoiceLineItem.getInvUnitOfMeasure().getUomName().equals(mdetails.getIliUomName()) || arInvoiceLineItem.getIliQuantity() != mdetails.getIliQuantity() || arInvoiceLineItem.getIliUnitPrice() != mdetails.getIliUnitPrice()) {

                            isRecalculate = true;
                            break;
                        }

                        isRecalculate = false;
                    }

                } else {

                    isRecalculate = false;
                }

                arPdc.setPdcStatus(details.getPdcStatus());
                arPdc.setPdcLvShift(details.getPdcLvShift());
                arPdc.setPdcCheckNumber(details.getPdcCheckNumber());
                arPdc.setPdcReferenceNumber(details.getPdcReferenceNumber());
                arPdc.setPdcDateReceived(details.getPdcDateReceived());
                arPdc.setPdcMaturityDate(details.getPdcMaturityDate());
                arPdc.setPdcDescription(details.getPdcDescription());
                arPdc.setPdcAmount(details.getPdcAmount());
                arPdc.setPdcConversionDate(details.getPdcConversionDate());
                arPdc.setPdcConversionRate(details.getPdcConversionRate());
                arPdc.setPdcLvFreight(details.getPdcLvFreight());
                arPdc.setPdcLastModifiedBy(details.getPdcLastModifiedBy());
                arPdc.setPdcDateLastModified(details.getPdcDateLastModified());
                arPdc.setPdcEffectivityDate(details.getPdcEffectivityDate());
            }

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY);
            adPaymentTerm.addArPdc(arPdc);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
            glFunctionalCurrency.addArPdc(arPdc);

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);
            arTaxCode.addArPdc(arPdc);

            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(WTC_NM, AD_CMPNY);
            arWithholdingTaxCode.addArPdc(arPdc);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, AD_CMPNY);
            arCustomer.addArPdc(arPdc);

            if (isRecalculate) {

                // remove all invoice line items

                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

                Collection arInvoiceLineItems = arPdc.getArInvoiceLineItems();

                Iterator i = arInvoiceLineItems.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();

                    i.remove();

                    //					arInvoiceLineItem.entityRemove();
                    em.remove(arInvoiceLineItem);
                }

                // remove all invoice lines

                Collection arInvoiceLines = arPdc.getArInvoiceLines();

                i = arInvoiceLines.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) i.next();

                    i.remove();

                    //					arInvoiceLine.entityRemove();
                    em.remove(arInvoiceLine);
                }

                // release ips locks and remove all applied invoices

                Collection arAppliedInvoices = arPdc.getArAppliedInvoices();

                i = arAppliedInvoices.iterator();

                while (i.hasNext()) {

                    LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) i.next();

                    arAppliedInvoice.getArInvoicePaymentSchedule().setIpsLock(EJBCommon.FALSE);

                    i.remove();

                    //					arAppliedInvoice.entityRemove();
                    em.remove(arAppliedInvoice);
                }

                // add new invoice line item

                double TOTAL_TAX = 0d;
                double TOTAL_LINE = 0d;

                i = iliList.iterator();

                LocalInvItemLocation invItemLocation = null;

                while (i.hasNext()) {

                    ArModInvoiceLineItemDetails mIliDetails = (ArModInvoiceLineItemDetails) i.next();

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mIliDetails.getIliLocName(), mIliDetails.getIliIiName(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mIliDetails.getIliLine()));
                    }

                    LocalArInvoiceLineItem arInvoiceLineItem = this.addArIliEntry(mIliDetails, arPdc, invItemLocation, AD_CMPNY);

                    TOTAL_LINE += arInvoiceLineItem.getIliAmount();
                    TOTAL_TAX += arInvoiceLineItem.getIliTaxAmount();
                }

                // get wtax if any

                double W_TAX_AMOUNT = 0d;

                if (arWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals("INVOICE")) {

                    W_TAX_AMOUNT = EJBCommon.roundIt(TOTAL_LINE * (arWithholdingTaxCode.getWtcRate() / 100), this.getGlFcPrecisionUnit(AD_CMPNY));
                }

                // get payment discount if any

                double DISCOUNT_AMT = 0d;

                if (adPaymentTerm.getPytDiscountOnInvoice() == EJBCommon.TRUE) {

                    Collection adPaymentSchedules = adPaymentScheduleHome.findByPytCode(adPaymentTerm.getPytCode(), AD_CMPNY);
                    ArrayList adPaymentScheduleList = new ArrayList(adPaymentSchedules);
                    LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) adPaymentScheduleList.get(0);

                    Collection adDiscounts = adDiscountHome.findByPsCode(adPaymentSchedule.getPsCode(), AD_CMPNY);
                    ArrayList adDiscountList = new ArrayList(adDiscounts);
                    LocalAdDiscount adDiscount = (LocalAdDiscount) adDiscountList.get(0);

                    double rate = adDiscount.getDscDiscountPercent();
                    DISCOUNT_AMT = (TOTAL_LINE + TOTAL_TAX) * (rate / 100d);
                }

                // set pdc amount

                arPdc.setPdcAmount(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT_AMT);
            }

            // generate approval status

            String PDC_APPRVL_STATUS = null;

            if (!isDraft) {

                // set pdc post status

                arPdc.setPdcPosted(EJBCommon.TRUE);
                arPdc.setPdcPostedBy(arPdc.getPdcLastModifiedBy());
                arPdc.setPdcDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                PDC_APPRVL_STATUS = "N/A";
            }

            // set invoice approval status

            arPdc.setPdcApprovalStatus(PDC_APPRVL_STATUS);

            return arPdc.getPdcCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalInvItemLocationNotFoundException |
                 GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
                 GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
                 GlobalPaymentTermInvalidException | GlobalConversionDateNotExistException |
                 GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveArRctEntry(ArPdcDetails details, String BA_NM, String FC_NM, String CST_CSTMR_CODE, ArrayList ilList, boolean isDraft, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, ArINVOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, ArRCTInvoiceHasNoWTaxCodeException {

        Debug.print("ArPdcEntryControllerBean saveAr0RctEntry");
        LocalArPdc arPdc = null;
        try {

            // validate if pdc is already deleted

            try {

                if (details.getPdcCode() != null) {

                    arPdc = arPdcHome.findByPrimaryKey(details.getPdcCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if pdc is already posted, cancelled, approved or pending

            if (details.getPdcCode() != null && details.getPdcCancelled() == EJBCommon.FALSE) {

                if (arPdc.getPdcApprovalStatus() != null) {

                    if (arPdc.getPdcApprovalStatus().equals("APPROVED") || arPdc.getPdcApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (arPdc.getPdcApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (arPdc.getPdcPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (arPdc.getPdcCancelled() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // check if reference number is unique

            LocalArPdc arExistingPdc = null;

            if (details.getPdcCode() == null) {

                try {

                    arExistingPdc = arPdcHome.findPdcByReferenceNumber(details.getPdcReferenceNumber(), AD_CMPNY);

                } catch (FinderException ex) {
                }

                if (arExistingPdc != null && arExistingPdc.getPdcPosted() == 1) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

            } else {

                try {

                    arExistingPdc = arPdcHome.findPdcByReferenceNumber(details.getPdcReferenceNumber(), AD_CMPNY);

                } catch (FinderException ex) {
                }

                if (arExistingPdc != null && !arExistingPdc.getPdcCode().equals(details.getPdcCode())) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }
            }

            // pdc cancelled

            if (details.getPdcCode() != null && details.getPdcCancelled() == EJBCommon.TRUE) {

                arPdc.setPdcCancelled(EJBCommon.TRUE);
                arPdc.setPdcStatus("CANCELLED");
                arPdc.setPdcLastModifiedBy(details.getPdcLastModifiedBy());
                arPdc.setPdcDateLastModified(details.getPdcDateLastModified());

                return arPdc.getPdcCode();
            }

            // validate if conversion date exists

            try {

                if (details.getPdcConversionDate() != null) {

                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
                    LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

                    if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getPdcConversionDate(), AD_CMPNY);

                    } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getPdcConversionDate(), AD_CMPNY);
                    }
                }

            } catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // used in checking if invoice should re-generate distribution
            // records and re-calculate taxes
            boolean isRecalculate = true;

            // create pdc

            if (details.getPdcCode() == null) {

                arPdc = arPdcHome.create(details.getPdcStatus(), details.getPdcLvShift(), details.getPdcCheckNumber(), details.getPdcReferenceNumber(), details.getPdcDateReceived(), details.getPdcMaturityDate(), details.getPdcDescription(), EJBCommon.FALSE, details.getPdcAmount(), details.getPdcConversionRate(), details.getPdcConversionDate(), details.getPdcLvFreight(), null, EJBCommon.FALSE, details.getPdcCreatedBy(), details.getPdcDateCreated(), details.getPdcLastModifiedBy(), details.getPdcDateLastModified(), null, null, null, null, EJBCommon.FALSE, details.getPdcEffectivityDate(), AD_BRNCH, AD_CMPNY);

            } else {

                // check if critical fields are changed

                isRecalculate = !arPdc.getAdBankAccount().getBaName().equals(BA_NM) || !arPdc.getArCustomer().getCstCustomerCode().equals(CST_CSTMR_CODE) || ilList.size() != arPdc.getArAppliedInvoices().size();

                arPdc.setPdcStatus(details.getPdcStatus());
                arPdc.setPdcLvShift(details.getPdcLvShift());
                arPdc.setPdcCheckNumber(details.getPdcCheckNumber());
                arPdc.setPdcReferenceNumber(details.getPdcReferenceNumber());
                arPdc.setPdcDateReceived(details.getPdcDateReceived());
                arPdc.setPdcMaturityDate(details.getPdcMaturityDate());
                arPdc.setPdcDescription(details.getPdcDescription());
                arPdc.setPdcAmount(details.getPdcAmount());
                arPdc.setPdcConversionDate(details.getPdcConversionDate());
                arPdc.setPdcConversionRate(details.getPdcConversionRate());
                arPdc.setPdcLvFreight(details.getPdcLvFreight());
                arPdc.setPdcLastModifiedBy(details.getPdcLastModifiedBy());
                arPdc.setPdcDateLastModified(details.getPdcDateLastModified());
                arPdc.setPdcEffectivityDate(details.getPdcEffectivityDate());
            }

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
            glFunctionalCurrency.addArPdc(arPdc);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, AD_CMPNY);
            arCustomer.addArPdc(arPdc);

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, AD_CMPNY);
            adBankAccount.addArPdc(arPdc);

            if (isRecalculate) {

                // remove all invoice lines

                Collection arInvoiceLines = arPdc.getArInvoiceLines();

                Iterator i = arInvoiceLines.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) i.next();

                    i.remove();

                    //					arInvoiceLine.entityRemove();
                    em.remove(arInvoiceLine);
                }

                // remove all invoice line items

                Collection arInvoiceLineItems = arPdc.getArInvoiceLineItems();

                i = arInvoiceLineItems.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();

                    i.remove();

                    //					arInvoiceLineItem.entityRemove();
                    em.remove(arInvoiceLineItem);
                }

                // release ips locks and remove all applied invoices

                Collection arAppliedInvoices = arPdc.getArAppliedInvoices();

                Debug.print("applied invoice size : " + arAppliedInvoices.size());

                i = arAppliedInvoices.iterator();

                while (i.hasNext()) {

                    Debug.print("Hello");

                    LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) i.next();

                    arAppliedInvoice.getArInvoicePaymentSchedule().setIpsLock(EJBCommon.FALSE);

                    Debug.print("Lock : " + arAppliedInvoice.getArInvoicePaymentSchedule().getIpsLock());

                    i.remove();

                    //					arAppliedInvoice.entityRemove();
                    em.remove(arAppliedInvoice);
                }

                // add new applied vouchers and distribution record

                i = ilList.iterator();

                while (i.hasNext()) {

                    ArModAppliedInvoiceDetails mAiDetails = (ArModAppliedInvoiceDetails) i.next();

                    this.addArAiEntry(mAiDetails, arPdc, AD_CMPNY);
                }
            }

            String PDC_APPRVL_STATUS = null;

            if (!isDraft) {

                // set pdc post status

                arPdc.setPdcPosted(EJBCommon.TRUE);
                arPdc.setPdcPostedBy(arPdc.getPdcLastModifiedBy());
                arPdc.setPdcDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
                PDC_APPRVL_STATUS = "N/A";
            }

            // set pdc approval status

            arPdc.setPdcApprovalStatus(PDC_APPRVL_STATUS);

            return arPdc.getPdcCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalTransactionAlreadyLockedException |
                 ArRCTInvoiceHasNoWTaxCodeException | ArINVOverapplicationNotAllowedException |
                 GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
                 GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
                 GlobalConversionDateNotExistException | GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalArAppliedInvoice addArAiEntry(ArModAppliedInvoiceDetails mdetails, LocalArPdc arPdc, Integer AD_CMPNY) throws ArINVOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, ArRCTInvoiceHasNoWTaxCodeException {

        Debug.print("ArPdcEntryControllerBean addArAiEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            // get functional currency name

            String FC_NM = adCompany.getGlFunctionalCurrency().getFcName();

            // validate overapplication

            LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arInvoicePaymentScheduleHome.findByPrimaryKey(mdetails.getAiIpsCode());

            if (EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), this.getGlFcPrecisionUnit(AD_CMPNY)) < EJBCommon.roundIt(mdetails.getAiApplyAmount() + mdetails.getAiCreditableWTax() + mdetails.getAiDiscountAmount() + mdetails.getAiAppliedDeposit(), this.getGlFcPrecisionUnit(AD_CMPNY))) {

                throw new ArINVOverapplicationNotAllowedException(arInvoicePaymentSchedule.getArInvoice().getInvNumber() + "-" + arInvoicePaymentSchedule.getIpsNumber());
            }

            // validate if ips already locked

            if (arInvoicePaymentSchedule.getIpsLock() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyLockedException(arInvoicePaymentSchedule.getArInvoice().getInvNumber() + "-" + arInvoicePaymentSchedule.getIpsNumber());
            }

            // validate invoice wtax code if necessary

            if (mdetails.getAiCreditableWTax() != 0 && arInvoicePaymentSchedule.getArInvoice().getArWithholdingTaxCode().getGlChartOfAccount() == null && (adPreference.getArWithholdingTaxCode() == null || adPreference.getArWithholdingTaxCode().getGlChartOfAccount() == null)) {

                throw new ArRCTInvoiceHasNoWTaxCodeException(arInvoicePaymentSchedule.getArInvoice().getInvNumber() + "-" + arInvoicePaymentSchedule.getIpsNumber());
            }

            double AI_FRX_GN_LSS = 0d;

            if (!FC_NM.equals(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName()) || !FC_NM.equals(arPdc.getGlFunctionalCurrency().getFcName())) {

                double AI_ALLCTD_PYMNT_AMNT = this.convertForeignToFunctionalCurrency(arPdc.getGlFunctionalCurrency().getFcCode(), arPdc.getGlFunctionalCurrency().getFcName(), arPdc.getPdcConversionDate(), arPdc.getPdcConversionRate(), mdetails.getAiAllocatedPaymentAmount(), AD_CMPNY);

                double AI_APPLY_AMNT = this.convertForeignToFunctionalCurrency(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcCode(), arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName(), arInvoicePaymentSchedule.getArInvoice().getInvConversionDate(), arInvoicePaymentSchedule.getArInvoice().getInvConversionRate(), mdetails.getAiApplyAmount(), AD_CMPNY);

                double AI_CRDTBL_W_TX = this.convertForeignToFunctionalCurrency(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcCode(), arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName(), arInvoicePaymentSchedule.getArInvoice().getInvConversionDate(), arInvoicePaymentSchedule.getArInvoice().getInvConversionRate(), mdetails.getAiCreditableWTax(), AD_CMPNY);

                double AI_DSCNT_AMNT = this.convertForeignToFunctionalCurrency(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcCode(), arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName(), arInvoicePaymentSchedule.getArInvoice().getInvConversionDate(), arInvoicePaymentSchedule.getArInvoice().getInvConversionRate(), mdetails.getAiDiscountAmount(), AD_CMPNY);

                double AI_APPLD_DPST = this.convertForeignToFunctionalCurrency(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcCode(), arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName(), arInvoicePaymentSchedule.getArInvoice().getInvConversionDate(), arInvoicePaymentSchedule.getArInvoice().getInvConversionRate(), mdetails.getAiAppliedDeposit(), AD_CMPNY);

                AI_FRX_GN_LSS = EJBCommon.roundIt((AI_ALLCTD_PYMNT_AMNT + AI_CRDTBL_W_TX + AI_DSCNT_AMNT + AI_APPLD_DPST) - (AI_APPLY_AMNT + AI_CRDTBL_W_TX + AI_DSCNT_AMNT + AI_APPLD_DPST), this.getGlFcPrecisionUnit(AD_CMPNY));
            }

            // create applied voucher

            LocalArAppliedInvoice arAppliedInvoice = arAppliedInvoiceHome.create(mdetails.getAiApplyAmount(), mdetails.getAiPenaltyApplyAmount(), mdetails.getAiCreditableWTax(), mdetails.getAiDiscountAmount(), mdetails.getAiRebate(), mdetails.getAiAppliedDeposit(), mdetails.getAiAllocatedPaymentAmount(), AI_FRX_GN_LSS, EJBCommon.FALSE, AD_CMPNY);

            arPdc.addArAppliedInvoice(arAppliedInvoice);
            arInvoicePaymentSchedule.addArAppliedInvoice(arAppliedInvoice);

            // lock voucher

            arInvoicePaymentSchedule.setIpsLock(EJBCommon.TRUE);

            return arAppliedInvoice;

        } catch (ArINVOverapplicationNotAllowedException | ArRCTInvoiceHasNoWTaxCodeException |
                 GlobalTransactionAlreadyLockedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteArPdcEntry(Integer PDC_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException {

        Debug.print("ArPdcEntryControllerBean deleteArPdcEntry");
        try {

            LocalArPdc arPdc = arPdcHome.findByPrimaryKey(PDC_CODE);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            Collection arInvoiceLineItems = arPdc.getArInvoiceLineItems();

            for (Object invoiceLineItem : arInvoiceLineItems) {

                LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;
                double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), AD_CMPNY);
                arInvoiceLineItem.getInvItemLocation().setIlCommittedQuantity(arInvoiceLineItem.getInvItemLocation().getIlCommittedQuantity() - convertedQuantity);
            }

            // release lock and remove applied invoice

            Collection arAppliedInvoices = arPdc.getArAppliedInvoices();

            Iterator i = arAppliedInvoices.iterator();

            while (i.hasNext()) {

                LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) i.next();

                arAppliedInvoice.getArInvoicePaymentSchedule().setIpsLock(EJBCommon.FALSE);

                i.remove();

                //    	   	   arAppliedInvoice.entityRemove();
                em.remove(arAppliedInvoice);
            }

            //			arPdc.entityRemove();
            em.remove(arPdc);

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArPdcEntryControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableInvShift(Integer AD_CMPNY) {

        Debug.print("ArPdcEntryControllerBean getAdPrfEnableInvShift");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvEnableShift();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArPdcEntryControllerBean getInvGpQuantityPrecisionUnit");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer AD_CMPNY) throws GlobalConversionDateNotExistException {

        Debug.print("ArPdcEntryControllerBean getFrRateByFrNameAndFrDate");
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

    public double getArRctDepositAmountByCstCustomerCode(String CST_CSTMR_CODE, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArPdcEntryControllerBean getArRctDepositAmountByCstCustomerCode");
        try {

            double depositAmount = 0d;
            double draftAppliedDeposit = 0d;

            Collection arReceipts = null;

            try {

                arReceipts = arReceiptHome.findOpenDepositEnabledPostedRctByCstCustomerCode(CST_CSTMR_CODE, AD_CMPNY);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            Iterator i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                depositAmount += arReceipt.getRctAmount() - arReceipt.getRctAppliedDeposit();
            }

            Collection arAppliedInvoices = null;

            try {

                arAppliedInvoices = arAppliedInvoiceHome.findUnpostedAiWithDepositByCstCustomerCode(CST_CSTMR_CODE, AD_BRNCH, AD_CMPNY);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            i = arAppliedInvoices.iterator();

            while (i.hasNext()) {

                LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) i.next();

                draftAppliedDeposit += arAppliedInvoice.getAiAppliedDeposit();
            }

            return depositAmount - draftAppliedDeposit;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArIpsByCstCustomerCode(String CST_CSTMR_CODE, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArPdcEntryControllerBean getArIpsByCstCustomerCode");
        ArrayList list = new ArrayList();
        try {

            Collection arInvoicePaymentSchedules = arInvoicePaymentScheduleHome.findOpenIpsByIpsLockAndCstCustomerCodeAndBrCode(EJBCommon.FALSE, CST_CSTMR_CODE, AD_BRNCH, AD_CMPNY);

            Debug.print("Cotroller, IPS size : " + arInvoicePaymentSchedules.size());

            if (arInvoicePaymentSchedules.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

            for (Object invoicePaymentSchedule : arInvoicePaymentSchedules) {

                LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) invoicePaymentSchedule;

                // verification if ips is already closed
                if (EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountDue(), precisionUnit) == EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountPaid(), precisionUnit)) {
                    continue;
                }

                ArModInvoicePaymentScheduleDetails mdetails = new ArModInvoicePaymentScheduleDetails();

                mdetails.setIpsCode(arInvoicePaymentSchedule.getIpsCode());
                mdetails.setIpsNumber(arInvoicePaymentSchedule.getIpsNumber());
                mdetails.setIpsDueDate(arInvoicePaymentSchedule.getIpsDueDate());
                mdetails.setIpsAmountDue(arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid());
                mdetails.setIpsInvNumber(arInvoicePaymentSchedule.getArInvoice().getInvNumber());
                mdetails.setIpsInvReferenceNumber(arInvoicePaymentSchedule.getArInvoice().getInvReferenceNumber());
                mdetails.setIpsInvFcName(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName());

                // calculate default discount

                short INVOICE_AGE = (short) ((new Date().getTime() - arInvoicePaymentSchedule.getArInvoice().getInvDate().getTime()) / (1000 * 60 * 60 * 24));

                double IPS_DSCNT_AMNT = 0d;

                if (arInvoicePaymentSchedule.getArInvoice().getAdPaymentTerm().getPytDiscountOnInvoice() == EJBCommon.FALSE) {

                    Collection adDiscounts = adDiscountHome.findByPsLineNumberAndPytName(arInvoicePaymentSchedule.getIpsNumber(), arInvoicePaymentSchedule.getArInvoice().getAdPaymentTerm().getPytName(), AD_CMPNY);

                    for (Object discount : adDiscounts) {

                        LocalAdDiscount adDiscount = (LocalAdDiscount) discount;

                        if (adDiscount.getDscPaidWithinDay() >= INVOICE_AGE) {

                            IPS_DSCNT_AMNT = EJBCommon.roundIt(mdetails.getIpsAmountDue() * adDiscount.getDscDiscountPercent() / 100, this.getGlFcPrecisionUnit(AD_CMPNY));

                            break;
                        }
                    }
                }

                // calculate default tax withheld

                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

                double APPLY_AMOUNT = mdetails.getIpsAmountDue() - IPS_DSCNT_AMNT;
                double W_TAX_AMOUNT = 0d;

                if (arInvoicePaymentSchedule.getArInvoice().getArWithholdingTaxCode().getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals("COLLECTION")) {

                    LocalArTaxCode arTaxCode = arInvoicePaymentSchedule.getArInvoice().getArTaxCode();

                    double NET_AMOUNT = 0d;

                    if (arTaxCode.getTcType().equals("INCLUSIVE") || arTaxCode.getTcType().equals("EXCLUSIVE")) {

                        NET_AMOUNT = EJBCommon.roundIt(APPLY_AMOUNT / (1 + (arTaxCode.getTcRate() / 100)), this.getGlFcPrecisionUnit(AD_CMPNY));

                    } else {

                        NET_AMOUNT = APPLY_AMOUNT;
                    }

                    W_TAX_AMOUNT = EJBCommon.roundIt(NET_AMOUNT * (arInvoicePaymentSchedule.getArInvoice().getArWithholdingTaxCode().getWtcRate() / 100), this.getGlFcPrecisionUnit(AD_CMPNY));

                    APPLY_AMOUNT -= W_TAX_AMOUNT;
                }

                mdetails.setIpsAiApplyAmount(APPLY_AMOUNT);
                mdetails.setIpsAiCreditableWTax(W_TAX_AMOUNT);
                mdetails.setIpsAiDiscountAmount(IPS_DSCNT_AMNT);

                list.add(mdetails);
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private LocalArInvoiceLine addArIlEntry(ArModInvoiceLineDetails mdetails, LocalArPdc arPdc, Integer AD_CMPNY) {

        Debug.print("ArPdcEntryControllerBean addArIlEntry");
        try {

            short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

            double IL_AMNT = 0d;
            double IL_TAX_AMNT = 0d;

            if (mdetails.getIlTax() == EJBCommon.TRUE) {

                // calculate net amount

                LocalArTaxCode arTaxCode = arPdc.getArTaxCode();

                if (arTaxCode.getTcType().equals("INCLUSIVE")) {

                    IL_AMNT = EJBCommon.roundIt(mdetails.getIlAmount() / (1 + (arTaxCode.getTcRate() / 100)), precisionUnit);

                } else {

                    // tax exclusive, none, zero rated or exempt

                    IL_AMNT = mdetails.getIlAmount();
                }

                // calculate tax

                if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {

                    if (arTaxCode.getTcType().equals("INCLUSIVE")) {

                        IL_TAX_AMNT = EJBCommon.roundIt(mdetails.getIlAmount() - IL_AMNT, precisionUnit);

                    } else if (arTaxCode.getTcType().equals("EXCLUSIVE")) {

                        IL_TAX_AMNT = EJBCommon.roundIt(mdetails.getIlAmount() * arTaxCode.getTcRate() / 100, precisionUnit);

                    } else {

                        // tax none zero-rated or exempt

                    }
                }

            } else {

                IL_AMNT = mdetails.getIlAmount();
            }

            LocalArInvoiceLine arInvoiceLine = arInvoiceLineHome.create(mdetails.getIlDescription(), mdetails.getIlQuantity(), mdetails.getIlUnitPrice(), IL_AMNT, IL_TAX_AMNT, mdetails.getIlTax(), AD_CMPNY);

            arPdc.addArInvoiceLine(arInvoiceLine);

            LocalArStandardMemoLine arStandardMemoLine = arStandardMemoLineHome.findBySmlName(mdetails.getIlSmlName(), AD_CMPNY);
            arStandardMemoLine.addArInvoiceLine(arInvoiceLine);

            return arInvoiceLine;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalArInvoiceLineItem addArIliEntry(ArModInvoiceLineItemDetails mdetails, LocalArPdc arPdc, LocalInvItemLocation invItemLocation, Integer AD_CMPNY) {

        Debug.print("ArPdcEntryControllerBean addArIliEntry");
        try {

            short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

            double ILI_AMNT = 0d;
            double ILI_TAX_AMNT = 0d;

            // calculate net amount

            LocalArTaxCode arTaxCode = arPdc.getArTaxCode();

            if (arTaxCode.getTcType().equals("INCLUSIVE")) {

                ILI_AMNT = EJBCommon.roundIt(mdetails.getIliAmount() / (1 + (arTaxCode.getTcRate() / 100)), precisionUnit);

            } else {

                // tax exclusive, none, zero rated or exempt

                ILI_AMNT = mdetails.getIliAmount();
            }

            // calculate tax

            if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {

                if (arTaxCode.getTcType().equals("INCLUSIVE")) {

                    ILI_TAX_AMNT = EJBCommon.roundIt(mdetails.getIliAmount() - ILI_AMNT, precisionUnit);

                } else if (arTaxCode.getTcType().equals("EXCLUSIVE")) {

                    ILI_TAX_AMNT = EJBCommon.roundIt(mdetails.getIliAmount() * arTaxCode.getTcRate() / 100, precisionUnit);

                } else {

                    // tax none zero-rated or exempt

                }
            }

            LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.IliLine(mdetails.getIliLine()).IliQuantity(mdetails.getIliQuantity()).IliUnitPrice(mdetails.getIliUnitPrice()).IliAmount(ILI_AMNT).IliTaxAmount(ILI_TAX_AMNT).IliDiscount1(mdetails.getIliDiscount1()).IliDiscount2(mdetails.getIliDiscount2()).IliDiscount3(mdetails.getIliDiscount3()).IliDiscount4(mdetails.getIliDiscount4()).IliTotalDiscount(mdetails.getIliTotalDiscount()).IliTax(EJBCommon.TRUE).IliAdCompany(AD_CMPNY).buildInvoiceLineItem();

            arPdc.addArInvoiceLineItem(arInvoiceLineItem);

            invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getIliUomName(), AD_CMPNY);
            invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);

            return arInvoiceLineItem;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("ArReceiptEntryControllerBean convertForeignToFunctionalCurrency");
        LocalAdCompany adCompany = null;
        // get company and extended precision
        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        // Convert to functional currency if necessary
        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT / CONVERSION_RATE;
        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double QTY_SLD, Integer AD_CMPNY) {

        Debug.print("ArPdcEntryControllerBean convertByUomFromAndItemAndQuantity");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            return EJBCommon.roundIt(QTY_SLD * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArPdcEntryControllerBean ejbCreate");
    }

}