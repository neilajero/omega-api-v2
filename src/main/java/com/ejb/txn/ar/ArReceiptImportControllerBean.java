/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArReceiptImportControllerBean
 * @created January 13, 2006, 9:00 AM
 * @author AlizaD.J. Anos
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
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.entities.inv.*;
import com.ejb.exception.ar.ArINVInvoiceDoesNotExist;
import com.ejb.exception.ar.ArINVOverapplicationNotAllowedException;
import com.ejb.exception.ar.ArRICustomerRequiredException;
import com.ejb.exception.global.*;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ar.ArReceiptImportPreferenceLineDetails;
import com.util.mod.ar.*;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "ArReceiptImportControllerEJB")
public class ArReceiptImportControllerBean extends EJBContextClass implements ArReceiptImportController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalArReceiptImportPreferenceHome arReceiptImportPreferenceHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArReceiptBatchHome arReceiptBatchHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalArAppliedInvoiceHome arAppliedInvoiceHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;
    @EJB
    private LocalArInvoiceLineHome arInvoiceLineHome;
    @EJB
    private LocalArAutoAccountingSegmentHome arAutoAccountingSegmentHome;
    @EJB
    private LocalAdBranchStandardMemoLineHome adBranchStandardMemoLineHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;

    public ArModReceiptImportPreferenceDetails getArRipByRipType(String RIP_TYPE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArReceiptImportControllerBean getArRipByRipType");
        try {

            LocalArReceiptImportPreference arReceiptImportPreference = null;

            try {

                arReceiptImportPreference = arReceiptImportPreferenceHome.findByRipType(RIP_TYPE, AD_CMPNY);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArModReceiptImportPreferenceDetails details = new ArModReceiptImportPreferenceDetails();

            details.setRipType(arReceiptImportPreference.getRipType());
            details.setRipIsSummarized(arReceiptImportPreference.getRipIsSummarized());
            details.setRipCustomerColumn(arReceiptImportPreference.getRipCustomerColumn());
            details.setRipCustomerFile(arReceiptImportPreference.getRipCustomerFile());
            details.setRipDateColumn(arReceiptImportPreference.getRipDateColumn());
            details.setRipDateFile(arReceiptImportPreference.getRipDateFile());
            details.setRipReceiptNumberColumn(arReceiptImportPreference.getRipReceiptNumberColumn());
            details.setRipReceiptNumberFile(arReceiptImportPreference.getRipReceiptNumberFile());
            details.setRipReceiptNumberLineColumn(arReceiptImportPreference.getRipReceiptNumberLineColumn());
            details.setRipReceiptNumberLineFile(arReceiptImportPreference.getRipReceiptNumberLineFile());
            details.setRipReceiptAmountColumn(arReceiptImportPreference.getRipReceiptAmountColumn());
            details.setRipReceiptAmountFile(arReceiptImportPreference.getRipReceiptAmountFile());
            details.setRipTcColumn(arReceiptImportPreference.getRipTcColumn());
            details.setRipTcFile(arReceiptImportPreference.getRipTcFile());
            details.setRipWtcColumn(arReceiptImportPreference.getRipWtcColumn());
            details.setRipWtcFile(arReceiptImportPreference.getRipWtcFile());
            details.setRipItemNameColumn(arReceiptImportPreference.getRipItemNameColumn());
            details.setRipItemNameFile(arReceiptImportPreference.getRipItemNameFile());
            details.setRipLocationNameColumn(arReceiptImportPreference.getRipLocationNameColumn());
            details.setRipLocationNameFile(arReceiptImportPreference.getRipLocationNameFile());
            details.setRipUomNameColumn(arReceiptImportPreference.getRipUomNameColumn());
            details.setRipUomNameFile(arReceiptImportPreference.getRipUomNameFile());
            details.setRipQuantityColumn(arReceiptImportPreference.getRipQuantityColumn());
            details.setRipQuantityFile(arReceiptImportPreference.getRipQuantityFile());
            details.setRipUnitPriceColumn(arReceiptImportPreference.getRipUnitPriceColumn());
            details.setRipUnitPriceFile(arReceiptImportPreference.getRipUnitPriceFile());
            details.setRipTotalColumn(arReceiptImportPreference.getRipTotalColumn());
            details.setRipTotalFile(arReceiptImportPreference.getRipTotalFile());
            details.setRipMemoLineColumn(arReceiptImportPreference.getRipMemoLineColumn());
            details.setRipMemoLineFile(arReceiptImportPreference.getRipMemoLineFile());
            details.setRipInvoiceNumberColumn(arReceiptImportPreference.getRipInvoiceNumberColumn());
            details.setRipInvoiceNumberFile(arReceiptImportPreference.getRipInvoiceNumberFile());
            details.setRipApplyAmountColumn(arReceiptImportPreference.getRipApplyAmountColumn());
            details.setRipApplyAmountFile(arReceiptImportPreference.getRipApplyAmountFile());
            details.setRipApplyDiscountColumn(arReceiptImportPreference.getRipApplyDiscountColumn());
            details.setRipApplyDiscountFile(arReceiptImportPreference.getRipApplyDiscountFile());

            Collection arReceiptImportPreferenceLines = arReceiptImportPreference.getArReceiptImportPreferenceLines();

            for (Object receiptImportPreferenceLine : arReceiptImportPreferenceLines) {

                LocalArReceiptImportPreferenceLine arReceiptImportPreferenceLine = (LocalArReceiptImportPreferenceLine) receiptImportPreferenceLine;

                ArReceiptImportPreferenceLineDetails rilDetails = new ArReceiptImportPreferenceLineDetails();

                rilDetails.setRilType(arReceiptImportPreferenceLine.getRilType());
                rilDetails.setRilName(arReceiptImportPreferenceLine.getRilName());
                rilDetails.setRilGlAccountNumber(arReceiptImportPreferenceLine.getRilGlAccountNumber());
                rilDetails.setRilBankAccountName(arReceiptImportPreferenceLine.getRilBankAccountName());
                rilDetails.setRilColumn(arReceiptImportPreferenceLine.getRilColumn());
                rilDetails.setRilFile(arReceiptImportPreferenceLine.getRilFile());
                rilDetails.setRilAmountColumn(arReceiptImportPreferenceLine.getRilAmountColumn());

                details.saveRipRilList(rilDetails);
            }

            return details;

        } catch (GlobalNoRecordFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArReceiptImportControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void importRctAi(ArrayList rctList, boolean isSummarized, String RB_NM, String TC_NM, String CRTD_BY,
                            Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalDocumentNumberNotUniqueException, GlobalTransactionAlreadyLockedException,
            GlobalNotAllTransactionsArePostedException, ArINVInvoiceDoesNotExist, GlobalNoRecordFoundException,
            ArINVOverapplicationNotAllowedException, GlobalRecordInvalidException, GlobalRecordInvalidForCurrentBranchException,
            GlobalRecordDisabledException, GlobalJournalNotBalanceException, GlobalAmountInvalidException, ArRICustomerRequiredException {

        Debug.print("ArReceiptImportControllerBean importRctAi");
        LocalArReceipt arReceipt = null;
        try {

            LocalArReceiptBatch arReceiptBatch = arReceiptBatchHome.create(RB_NM, null, "OPEN", "COLLECTION", new java.util.Date(), CRTD_BY, AD_BRNCH, AD_CMPNY);

            for (Object o : rctList) {

                ArModReceiptDetails details = (ArModReceiptDetails) o;

                if (details.getRctAmount() < 0) {

                    throw new GlobalAmountInvalidException("total amount for receipt " + details.getRctNumber());
                }

                Date RCPT_DATE = null;
                Date CURR_DATE = EJBCommon.getGcCurrentDateWoTime().getTime();

                if (isSummarized) {

                    RCPT_DATE = CURR_DATE;

                } else {

                    RCPT_DATE = details.getRctDate();
                }

                // validate if document number is unique document number is automatic then set next sequence

                String RCPT_NMBR = null;

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                if (isSummarized) {
                    // generate document number
                    try {

                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR RECEIPT", AD_CMPNY);

                    } catch (FinderException ex) {

                    }

                    try {

                        adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {

                    }

                    if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A') {

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    arReceiptHome.findByRctNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                } catch (FinderException ex) {

                                    RCPT_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;
                                }

                            } else {

                                try {

                                    arReceiptHome.findByRctNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                } catch (FinderException ex) {

                                    RCPT_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;
                                }
                            }
                        }
                    }

                } else {

                    LocalArReceipt arExistingReceipt = null;

                    try {

                        arExistingReceipt = arReceiptHome.findByRctNumberAndBrCode(details.getRctNumber(), AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {
                    }

                    if (arExistingReceipt != null && !arExistingReceipt.getRctCode().equals(details.getRctCode())) {

                        throw new GlobalDocumentNumberNotUniqueException(details.getRctNumber());
                    }

                    RCPT_NMBR = details.getRctNumber();
                }

                // create receipt

                arReceipt = arReceiptHome.create("COLLECTION", null, RCPT_DATE, RCPT_NMBR, null, null, null, null, null, null, null, null, 0d, 0d, 0d, 0d, 0d, 0d, 0d, null, details.getRctConversionRate(), null, null, EJBCommon.FALSE, 0d, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, null, null, null, null, null, CRTD_BY, CURR_DATE, CRTD_BY, CURR_DATE, null, null, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, null, null, EJBCommon.FALSE, EJBCommon.FALSE, null, AD_BRNCH, AD_CMPNY);

                // add customer
                LocalArCustomer arCustomer = null;

                if (details.getRctCstName() == null || details.getRctCstName().equals("")) {

                    throw new ArRICustomerRequiredException(arReceipt.getRctNumber());
                }

                try {

                    arCustomer = arCustomerHome.findByCstName(details.getRctCstName(), AD_BRNCH, AD_CMPNY);

                    if (arCustomer.getCstEnable() == EJBCommon.TRUE) {

                        arCustomer.addArReceipt(arReceipt);

                    } else {

                        throw new GlobalRecordDisabledException("Customer: " + details.getRctCstName());
                    }

                } catch (FinderException ex) {

                    throw new GlobalRecordInvalidForCurrentBranchException("Customer: " + details.getRctCstName());
                }

                LocalAdBankAccount adBankAccount = arCustomer.getAdBankAccount();
                adBankAccount.addArReceipt(arReceipt);

                // add withholding tax

                try {

                    LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName("NONE", AD_CMPNY);

                    if (arWithholdingTaxCode.getWtcEnable() == EJBCommon.TRUE) {

                        arWithholdingTaxCode.addArReceipt(arReceipt);

                    } else {

                        throw new GlobalRecordDisabledException("Withholding Tax: NONE");
                    }

                } catch (FinderException ex) {

                    throw new GlobalNoRecordFoundException("Withholding Tax: NONE");
                }

                // add tax
                LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("NONE", AD_CMPNY);
                arTaxCode.addArReceipt(arReceipt);

                // add receipt batch
                arReceiptBatch.addArReceipt(arReceipt);

                // add functional currency
                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(details.getRctFcName(), AD_CMPNY);

                if (glFunctionalCurrency.getFcEnable() == EJBCommon.TRUE) {

                    glFunctionalCurrency.addArReceipt(arReceipt);

                } else {

                    throw new GlobalRecordDisabledException("Currency: " + details.getRctFcName());
                }

                // add new applied invoices and distribution record

                double TOTAL_AMOUNT = 0d;
                double TOTAL_LINE = 0d;
                double TOTAL_DISC = 0d;

                for (int i = 0; i < details.getRctAiListSize(); i++) {

                    ArModAppliedInvoiceDetails mAiDetails = details.getRctAiListByIndex(i);

                    if (mAiDetails.getAiApplyAmount() < 0) {

                        throw new GlobalAmountInvalidException("apply amount for " + mAiDetails.getAiIpsInvNumber() + " of receipt " + arReceipt.getRctNumber());
                    }

                    if (mAiDetails.getAiDiscountAmount() < 0) {

                        throw new GlobalAmountInvalidException("discount amount for " + mAiDetails.getAiIpsInvNumber() + " of receipt " + arReceipt.getRctNumber());
                    }

                    TOTAL_AMOUNT += mAiDetails.getAiApplyAmount();

                    LocalArInvoice arInvoice = null;

                    try {

                        arInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(mAiDetails.getAiIpsInvNumber(), EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);
                        if (arInvoice.getInvPosted() == EJBCommon.FALSE) {

                            throw new GlobalNotAllTransactionsArePostedException(mAiDetails.getAiIpsInvNumber());
                        }

                    } catch (FinderException ex) {

                        throw new ArINVInvoiceDoesNotExist(mAiDetails.getAiIpsInvNumber());
                    }

                    // check if receipt's customer matches invoice's customer

                    if (!arCustomer.getCstCustomerCode().equals(arInvoice.getArCustomer().getCstCustomerCode())) {

                        throw new GlobalRecordInvalidException(arInvoice.getInvNumber() + ": Customer " + arCustomer.getCstName() + " - " + arInvoice.getArCustomer().getCstName());
                    }

                    Collection arInvoicePaymentSchedules = arInvoicePaymentScheduleHome.findOpenIpsByInvNumber(mAiDetails.getAiIpsInvNumber(), AD_BRNCH, AD_CMPNY);

                    Iterator ipsIter = arInvoicePaymentSchedules.iterator();

                    while (ipsIter.hasNext()) {

                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) ipsIter.next();

                        ArModAppliedInvoiceDetails mDetails = new ArModAppliedInvoiceDetails();

                        mDetails.setAiIpsCode(arInvoicePaymentSchedule.getIpsCode());
                        mDetails.setAiReceiptNumber(mAiDetails.getAiReceiptNumber());
                        mDetails.setAiIpsInvNumber(mAiDetails.getAiIpsInvNumber());
                        mDetails.setAiIpsInvReferenceNumber(mAiDetails.getAiIpsInvReferenceNumber());

                        if (mAiDetails.getAiApplyAmount() == 0) {

                            break;
                        }

                        Collection arAppliedInvoices = arAppliedInvoiceHome.findByIpsCode(arInvoicePaymentSchedule.getIpsCode(), AD_CMPNY);

                        Iterator aiIter = arAppliedInvoices.iterator();

                        double TOTAL_PAID = 0d;

                        while (aiIter.hasNext()) {

                            LocalArAppliedInvoice existingArAppliedInvoice = (LocalArAppliedInvoice) aiIter.next();

                            TOTAL_PAID += existingArAppliedInvoice.getAiApplyAmount();
                        }

                        if (TOTAL_PAID == arInvoicePaymentSchedule.getIpsAmountDue() && arInvoicePaymentSchedule.getIpsLock() == EJBCommon.TRUE) {

                            if (ipsIter.hasNext()) {
                                continue;
                            } else {
                                throw new ArINVOverapplicationNotAllowedException(arInvoicePaymentSchedule.getArInvoice().getInvNumber());
                            }
                        }

                        Collection arReceipts = arReceiptBatch.getArReceipts();

                        double TOTAL_APPLY_AMOUNT = 0d;

                        Iterator rIter = arReceipts.iterator();

                        boolean found = true;

                        while (rIter.hasNext()) {

                            LocalArReceipt arRcpt = (LocalArReceipt) rIter.next();

                            Collection arRcptAppliedInvoices = arRcpt.getArAppliedInvoices();

                            for (Object arRcptAppliedInvoice : arRcptAppliedInvoices) {

                                LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) arRcptAppliedInvoice;

                                if (arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvNumber().equals(mAiDetails.getAiIpsInvNumber())) {
                                    TOTAL_APPLY_AMOUNT += arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiDiscountAmount();
                                    found = true;
                                }
                            }
                        }

                        double BALANCE_AMOUNT = 0d;

                        if (found == true) {

                            BALANCE_AMOUNT = arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid() - TOTAL_APPLY_AMOUNT;

                        } else {

                            BALANCE_AMOUNT = arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid();
                        }

                        if (ipsIter.hasNext() && mAiDetails.getAiApplyAmount() > BALANCE_AMOUNT) {

                            mDetails.setAiApplyAmount(BALANCE_AMOUNT);
                            mAiDetails.setAiApplyAmount(mAiDetails.getAiApplyAmount() - BALANCE_AMOUNT);

                        } else {

                            mDetails.setAiApplyAmount(mAiDetails.getAiApplyAmount());
                            mDetails.setAiDiscountAmount(mAiDetails.getAiDiscountAmount());
                            mAiDetails.setAiApplyAmount(0);
                        }

                        LocalArAppliedInvoice arAppliedInvoice = this.addArAiEntry(mDetails, arReceipt, arReceiptBatch, AD_CMPNY);

                        TOTAL_LINE += arAppliedInvoice.getAiApplyAmount();
                        TOTAL_DISC += arAppliedInvoice.getAiDiscountAmount();

                        // create discount distribution records if necessary

                        if (arAppliedInvoice.getAiDiscountAmount() != 0) {

                            short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

                            // get discount percent

                            double DISCOUNT_PERCENT = EJBCommon.roundIt(arAppliedInvoice.getAiDiscountAmount() / (arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount()), (short) 6);
                            DISCOUNT_PERCENT = EJBCommon.roundIt(DISCOUNT_PERCENT * ((arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount()) / arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvAmountDue()), (short) 6);

                            Collection arDiscountDistributionRecords = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArDistributionRecords();

                            // get total debit and credit for rounding difference calculation

                            double TOTAL_DEBIT = 0d;
                            double TOTAL_CREDIT = 0d;
                            boolean isRoundingDifferenceCalculated = false;

                            Iterator j = arDiscountDistributionRecords.iterator();

                            while (j.hasNext()) {

                                LocalArDistributionRecord arDiscountDistributionRecord = (LocalArDistributionRecord) j.next();

                                if (arDiscountDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                                    TOTAL_DEBIT += EJBCommon.roundIt(arDiscountDistributionRecord.getDrAmount() * DISCOUNT_PERCENT, precisionUnit);

                                } else {

                                    TOTAL_CREDIT += EJBCommon.roundIt(arDiscountDistributionRecord.getDrAmount() * DISCOUNT_PERCENT, precisionUnit);
                                }
                            }

                            j = arDiscountDistributionRecords.iterator();

                            while (j.hasNext()) {

                                LocalArDistributionRecord arDiscountDistributionRecord = (LocalArDistributionRecord) j.next();

                                if (arDiscountDistributionRecord.getDrClass().equals("RECEIVABLE")) {
                                    continue;
                                }

                                double DR_AMNT = EJBCommon.roundIt(arDiscountDistributionRecord.getDrAmount() * DISCOUNT_PERCENT, precisionUnit);

                                // calculate rounding difference if necessary

                                if (arDiscountDistributionRecord.getDrDebit() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT && !isRoundingDifferenceCalculated) {

                                    DR_AMNT = DR_AMNT + TOTAL_DEBIT - TOTAL_CREDIT;

                                    isRoundingDifferenceCalculated = true;
                                }

                                if (arDiscountDistributionRecord.getDrClass().equals("REVENUE")) {

                                    this.addArDrEntry(arReceipt.getArDrNextLine(), "SALES DISCOUNT", EJBCommon.TRUE, DR_AMNT, EJBCommon.FALSE, arReceipt.getAdBankAccount().getBaCoaGlSalesDiscount(), arReceipt, arAppliedInvoice, AD_BRNCH, AD_CMPNY);

                                } else {

                                    this.addArDrEntry(arReceipt.getArDrNextLine(), arDiscountDistributionRecord.getDrClass(), arDiscountDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, DR_AMNT, EJBCommon.FALSE, arDiscountDistributionRecord.getGlChartOfAccount().getCoaCode(), arReceipt, arAppliedInvoice, AD_BRNCH, AD_CMPNY);
                                }
                            }
                        }

                        // get receivable account

                        LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode("RECEIVABLE", arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(), AD_CMPNY);

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "RECEIVABLE", EJBCommon.FALSE, arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount(), EJBCommon.FALSE, arDistributionRecord.getGlChartOfAccount().getCoaCode(), arReceipt, arAppliedInvoice, AD_BRNCH, AD_CMPNY);
                    }
                }

                if (details.getRctAmount() != TOTAL_LINE) {

                    throw new GlobalJournalNotBalanceException(arReceipt.getRctNumber());
                }

                // create cash distribution record

                this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE, TOTAL_AMOUNT, EJBCommon.FALSE, arReceipt.getAdBankAccount().getBaCoaGlCashAccount(), arReceipt, null, AD_BRNCH, AD_CMPNY);

                arReceipt.setRctAmount(TOTAL_AMOUNT);
            }

        } catch (GlobalDocumentNumberNotUniqueException | ArRICustomerRequiredException | GlobalAmountInvalidException |
                 GlobalJournalNotBalanceException | GlobalRecordDisabledException |
                 GlobalRecordInvalidForCurrentBranchException | GlobalRecordInvalidException |
                 ArINVOverapplicationNotAllowedException | GlobalNoRecordFoundException | ArINVInvoiceDoesNotExist |
                 GlobalNotAllTransactionsArePostedException | GlobalTransactionAlreadyLockedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void importRctIli(ArrayList miscRctList, boolean isSummarized, String RB_NM, String TC_NM,
                             boolean taxableServiceCharge, boolean taxableDiscount, String CRTD_BY,
                             Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalInvItemLocationNotFoundException, GlobalBranchAccountNumberInvalidException,
            GlobalNoRecordFoundException, GlobalDocumentNumberNotUniqueException,
            GlobalRecordInvalidForCurrentBranchException, GlobalRecordInvalidException,
            GlobalRecordDisabledException, GlobalJournalNotBalanceException,
            GlobalAmountInvalidException, GlobalRecordAlreadyExistException {

        Debug.print("ArReceiptImportControllerBean importRctIli");
        LocalArWithholdingTaxCode arWithholdingTaxCode = null;
        LocalArTaxCode arTaxCode = null;
        LocalArReceipt arReceipt = null;
        try {

            Date CURR_DATE = EJBCommon.getGcCurrentDateWoTime().getTime();

            // check if POS data already exists

            ArModReceiptDetails rctDetails = (ArModReceiptDetails) miscRctList.get(0);

            Collection arReceipts = arReceiptHome.findByRctDateAndBaName(rctDetails.getRctDate(), "POS%", AD_CMPNY);

            if (!arReceipts.isEmpty()) {

                throw new GlobalRecordAlreadyExistException(EJBCommon.convertSQLDateToString(rctDetails.getRctDate()));
            }

            LocalArReceiptBatch arReceiptBatch = arReceiptBatchHome.create(RB_NM, null, "OPEN", "MISC", CURR_DATE, CRTD_BY, AD_BRNCH, AD_CMPNY);

            for (Object value : miscRctList) {

                ArModReceiptDetails details = (ArModReceiptDetails) value;

                if (details.getRctAmount() < 0) {

                    throw new GlobalAmountInvalidException("total amount for receipt " + details.getRctNumber());
                }

                // validate if receipt number is unique receipt number is automatic then set next sequence

                String RCPT_NMBR = null;

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                if (isSummarized) {

                    try {

                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR RECEIPT", AD_CMPNY);

                    } catch (FinderException ex) {

                    }

                    try {

                        adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {

                    }

                    if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A') {

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    arReceiptHome.findByRctNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                } catch (FinderException ex) {

                                    RCPT_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;
                                }

                            } else {

                                try {

                                    arReceiptHome.findByRctNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                } catch (FinderException ex) {

                                    RCPT_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;
                                }
                            }
                        }
                    }

                } else {

                    LocalArReceipt arExistingReceipt = null;

                    try {

                        arExistingReceipt = arReceiptHome.findByRctNumberAndBrCode(details.getRctNumber(), AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {
                    }

                    if (arExistingReceipt != null && !arExistingReceipt.getRctCode().equals(details.getRctCode())) {

                        throw new GlobalDocumentNumberNotUniqueException(details.getRctNumber());
                    }

                    RCPT_NMBR = details.getRctNumber();
                }

                // create misc receipt

                arReceipt = arReceiptHome.create("MISC", null, details.getRctDate(), RCPT_NMBR, null, null, null, null, null, null, null, null, 0d, 0d, 0d, 0d, 0d, 0d, 0d, null, details.getRctConversionRate(), null, null, EJBCommon.FALSE, 0d, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, null, null, null, null, null, CRTD_BY, CURR_DATE, CRTD_BY, CURR_DATE, null, null, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, null, null, EJBCommon.FALSE, EJBCommon.FALSE, null, AD_BRNCH, AD_CMPNY);

                // add bank account
                LocalAdBankAccount adBankAccount = null;

                try {

                    adBankAccount = adBankAccountHome.findByBaNameAndBrCode(details.getRctBaName(), AD_BRNCH, AD_CMPNY);

                    if (adBankAccount.getBaEnable() == EJBCommon.TRUE) {

                        adBankAccount.addArReceipt(arReceipt);

                    } else {

                        throw new GlobalRecordDisabledException("Bank Account: " + details.getRctBaName());
                    }

                } catch (FinderException ex) {

                    throw new GlobalRecordInvalidForCurrentBranchException("Bank Account: " + details.getRctBaName());
                }

                // add customer
                LocalArCustomer arCustomer = null;

                if (details.getRctCstName() == null || details.getRctCstName().equals("")) {

                    throw new GlobalRecordInvalidException();
                }

                try {

                    arCustomer = arCustomerHome.findByCstName(details.getRctCstName(), AD_BRNCH, AD_CMPNY);

                    if (arCustomer.getCstEnable() == EJBCommon.TRUE) {

                        arCustomer.addArReceipt(arReceipt);

                    } else {

                        throw new GlobalRecordDisabledException("Customer: " + details.getRctCstName());
                    }

                } catch (FinderException ex) {

                    throw new GlobalRecordInvalidForCurrentBranchException("Customer: " + details.getRctCstName());
                }

                try {

                    arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName("NONE", AD_CMPNY);
                    if (arWithholdingTaxCode.getWtcEnable() == EJBCommon.TRUE) {

                        arWithholdingTaxCode.addArReceipt(arReceipt);

                    } else {

                        throw new GlobalRecordDisabledException("Withholding Tax: NONE");
                    }

                } catch (FinderException ex) {

                    throw new GlobalNoRecordFoundException("Withholding Tax: NONE");
                }

                try {

                    arTaxCode = arTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);
                    arTaxCode.addArReceipt(arReceipt);

                } catch (FinderException ex) {

                    throw new GlobalNoRecordFoundException("Tax: " + TC_NM);
                }

                arReceiptBatch.addArReceipt(arReceipt);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(details.getRctFcName(), AD_CMPNY);

                if (glFunctionalCurrency.getFcEnable() == EJBCommon.TRUE) {

                    glFunctionalCurrency.addArReceipt(arReceipt);

                } else {

                    throw new GlobalRecordDisabledException("Currency: " + details.getRctFcName());
                }

                // add new invoice line items and distribution record

                double TOTAL_LINE = 0d;
                double TOTAL_TAX = 0d;
                short ILI_LINE = 1;

                ArrayList iliList = details.getInvIliList();

                Iterator i = iliList.iterator();

                LocalInvItemLocation invItemLocation = null;

                while (i.hasNext()) {

                    ArModInvoiceLineItemDetails mIliDetails = (ArModInvoiceLineItemDetails) i.next();

                    if (mIliDetails.getIliUnitPrice() < 0) {

                        throw new GlobalAmountInvalidException("unit price for item " + mIliDetails.getIliIiName() + " of receipt " + arReceipt.getRctNumber());
                    }

                    if (mIliDetails.getIliQuantity() <= 0) {

                        throw new GlobalAmountInvalidException("quantity for item " + mIliDetails.getIliIiName() + " of receipt " + arReceipt.getRctNumber());
                    }

                    try {

                        LocalInvLocation invLocation = invLocationHome.findByLocName(mIliDetails.getIliLocName(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new GlobalNoRecordFoundException("Location: " + mIliDetails.getIliLocName());
                    }

                    try {

                        LocalInvItem invItem = invItemHome.findByIiName(mIliDetails.getIliIiName(), AD_CMPNY);

                        if (invItem.getIiEnable() == EJBCommon.FALSE) {

                            throw new GlobalRecordDisabledException("Item: " + mIliDetails.getIliIiName());
                        }

                    } catch (FinderException ex) {

                        throw new GlobalNoRecordFoundException("Item: " + mIliDetails.getIliIiName());
                    }

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mIliDetails.getIliLocName(), mIliDetails.getIliIiName(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(mIliDetails.getIliIiName() + "-" + mIliDetails.getIliLocName());
                    }

                    if (mIliDetails.getIliUomName().equals("")) {

                        mIliDetails.setIliUomName(invItemLocation.getInvItem().getInvUnitOfMeasure().getUomName());
                    }

                    mIliDetails.setIliLine(ILI_LINE);
                    ILI_LINE++;

                    mIliDetails.setIliAmount(mIliDetails.getIliUnitPrice() * mIliDetails.getIliQuantity());

                    LocalArInvoiceLineItem arInvoiceLineItem = this.addArIliEntry(mIliDetails, arReceipt, invItemLocation, AD_CMPNY);

                    // add cost of sales distribution and inventory

                    double COST = 0d;

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

                        COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                    } catch (FinderException ex) {

                        COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), AD_CMPNY);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {

                    }

                    if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock")) {

                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arReceipt, AD_BRNCH, AD_CMPNY);

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlInventoryAccount(), arReceipt, AD_BRNCH, AD_CMPNY);

                        } else {

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arReceipt, AD_BRNCH, AD_CMPNY);

                            this.addArDrIliEntry(arReceipt.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arReceipt, AD_BRNCH, AD_CMPNY);
                        }

                        // add quantity to item location committed quantity

                        double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), AD_CMPNY);
                        invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);
                    }

                    // add inventory sale distributions

                    if (adBranchItemLocation != null) {

                        this.addArDrIliEntry(arReceipt.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(), adBranchItemLocation.getBilCoaGlSalesAccount(), arReceipt, AD_BRNCH, AD_CMPNY);

                    } else {

                        this.addArDrIliEntry(arReceipt.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arInvoiceLineItem.getIliAmount(), arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arReceipt, AD_BRNCH, AD_CMPNY);
                    }

                    TOTAL_LINE += arInvoiceLineItem.getIliAmount();
                    TOTAL_TAX += arInvoiceLineItem.getIliTaxAmount();
                }

                double SERVICE_CHARGE_AMOUNT = 0d;
                double DISCOUNT_AMOUNT = 0d;
                double GIFT_CHECK_AMOUNT = 0d;
                double SERVICE_CHARGE_TAX = 0d;
                double DISCOUNT_TAX = 0d;

                short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

                ArrayList drList = details.getRctDrList();

                for (Object o : drList) {

                    ArModDistributionRecordDetails drDetails = (ArModDistributionRecordDetails) o;

                    if (drDetails.getDrClass().equals("SERVICE CHARGE") && drDetails.getDrAmount() > 0) {

                        LocalGlChartOfAccount glServiceChargeAccount = null;

                        try {

                            glServiceChargeAccount = glChartOfAccountHome.findByCoaAccountNumber(drDetails.getDrCoaAccountNumber(), AD_CMPNY);

                        } catch (FinderException ex) {

                            throw new GlobalNoRecordFoundException("Service Charge Account " + drDetails.getDrCoaAccountNumber());
                        }

                        Collection arDistributionRecords = null;

                        try {

                            arDistributionRecords = arDistributionRecordHome.findDrsByDrClassAndRctCode("SERVICE", arReceipt.getRctCode(), AD_CMPNY);

                        } catch (FinderException ex) {

                        }

                        if (taxableServiceCharge && !arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {

                            SERVICE_CHARGE_TAX = this.getTaxAmount(arTaxCode, drDetails.getDrAmount(), precisionUnit);

                            if (arTaxCode.getTcType().equals("INCLUSIVE")) {

                                SERVICE_CHARGE_AMOUNT = drDetails.getDrAmount() - SERVICE_CHARGE_TAX;

                            } else {

                                SERVICE_CHARGE_AMOUNT = drDetails.getDrAmount();
                            }

                        } else {

                            SERVICE_CHARGE_AMOUNT = drDetails.getDrAmount();
                        }

                        if (arDistributionRecords.isEmpty()) {

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "SERVICE", EJBCommon.FALSE, SERVICE_CHARGE_AMOUNT, EJBCommon.FALSE, glServiceChargeAccount.getCoaCode(), arReceipt, null, AD_BRNCH, AD_CMPNY);

                        } else {

                            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) arDistributionRecords.iterator().next();

                            arDistributionRecord.setDrAmount(arDistributionRecord.getDrAmount() + SERVICE_CHARGE_AMOUNT);

                            SERVICE_CHARGE_AMOUNT = arDistributionRecord.getDrAmount();
                        }

                    } else if (drDetails.getDrClass().equals("DISCOUNT") && drDetails.getDrAmount() > 0) {

                        LocalGlChartOfAccount glDiscountAccount = null;

                        try {

                            glDiscountAccount = glChartOfAccountHome.findByCoaAccountNumber(drDetails.getDrCoaAccountNumber(), AD_CMPNY);

                        } catch (FinderException ex) {

                            throw new GlobalNoRecordFoundException("Discount Account " + drDetails.getDrCoaAccountNumber());
                        }

                        Collection arDistributionRecords = null;

                        try {

                            arDistributionRecords = arDistributionRecordHome.findDrsByDrClassAndRctCode("DISCOUNT", arReceipt.getRctCode(), AD_CMPNY);

                        } catch (FinderException ex) {

                        }

                        if (taxableDiscount && !arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {

                            DISCOUNT_TAX = this.getTaxAmount(arTaxCode, drDetails.getDrAmount(), precisionUnit);

                            if (arTaxCode.getTcType().equals("INCLUSIVE")) {

                                DISCOUNT_AMOUNT = drDetails.getDrAmount() - DISCOUNT_TAX;

                            } else {

                                DISCOUNT_AMOUNT = drDetails.getDrAmount();
                            }

                        } else {

                            DISCOUNT_AMOUNT = drDetails.getDrAmount();
                        }

                        if (arDistributionRecords.isEmpty()) {

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "DISCOUNT", EJBCommon.TRUE, DISCOUNT_AMOUNT, EJBCommon.FALSE, glDiscountAccount.getCoaCode(), arReceipt, null, AD_BRNCH, AD_CMPNY);

                        } else {

                            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) arDistributionRecords.iterator().next();

                            arDistributionRecord.setDrAmount(arDistributionRecord.getDrAmount() + DISCOUNT_AMOUNT);

                            DISCOUNT_AMOUNT = arDistributionRecord.getDrAmount();
                        }

                    } else if (drDetails.getDrClass().equals("GIFT CHECK") && drDetails.getDrAmount() > 0) {

                        LocalGlChartOfAccount glGiftCheckAccount = null;

                        try {

                            glGiftCheckAccount = glChartOfAccountHome.findByCoaAccountNumber(drDetails.getDrCoaAccountNumber(), AD_CMPNY);

                        } catch (FinderException ex) {

                            throw new GlobalNoRecordFoundException("Gift Check Account " + drDetails.getDrCoaAccountNumber());
                        }

                        Collection arDistributionRecords = null;

                        try {

                            arDistributionRecords = arDistributionRecordHome.findDrsByDrClassAndRctCode("GIFT CHECK", arReceipt.getRctCode(), AD_CMPNY);

                        } catch (FinderException ex) {

                        }

                        if (arDistributionRecords.isEmpty()) {

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "GIFT CHECK", EJBCommon.TRUE, drDetails.getDrAmount(), EJBCommon.FALSE, glGiftCheckAccount.getCoaCode(), arReceipt, null, AD_BRNCH, AD_CMPNY);

                            GIFT_CHECK_AMOUNT = drDetails.getDrAmount();

                        } else {

                            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) arDistributionRecords.iterator().next();

                            arDistributionRecord.setDrAmount(arDistributionRecord.getDrAmount() + drDetails.getDrAmount());

                            GIFT_CHECK_AMOUNT = arDistributionRecord.getDrAmount();
                        }
                    }
                }

                // add tax distribution if necessary

                TOTAL_TAX = TOTAL_TAX + SERVICE_CHARGE_TAX - DISCOUNT_TAX;

                if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, EJBCommon.FALSE, arTaxCode.getGlChartOfAccount().getCoaCode(), arReceipt, null, AD_BRNCH, AD_CMPNY);
                }

                // add wtax distribution if necessary

                double W_TAX_AMOUNT = 0d;

                if (arWithholdingTaxCode.getWtcRate() != 0) {

                    W_TAX_AMOUNT = EJBCommon.roundIt(TOTAL_LINE * (arWithholdingTaxCode.getWtcRate() / 100), this.getGlFcPrecisionUnit(AD_CMPNY));

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "W-TAX", EJBCommon.TRUE, W_TAX_AMOUNT, EJBCommon.FALSE, arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), arReceipt, null, AD_BRNCH, AD_CMPNY);
                }

                double RCPT_AMNT = 0d;

                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

                if (adCompany.getCmpShortName().equals("obi") && arReceipt.getAdBankAccount().getBaName().equals("POS MC")) {

                    RCPT_AMNT = details.getRctAmount() + GIFT_CHECK_AMOUNT + SERVICE_CHARGE_AMOUNT + SERVICE_CHARGE_TAX;

                } else {

                    RCPT_AMNT = details.getRctAmount() + GIFT_CHECK_AMOUNT;
                }

                if (EJBCommon.roundIt(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT + SERVICE_CHARGE_AMOUNT - DISCOUNT_AMOUNT, this.getGlFcPrecisionUnit(AD_CMPNY)) != EJBCommon.roundIt(RCPT_AMNT, this.getGlFcPrecisionUnit(AD_CMPNY))) {

                    throw new GlobalJournalNotBalanceException(arReceipt.getRctNumber());
                }

                if (TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT + SERVICE_CHARGE_AMOUNT - DISCOUNT_AMOUNT - GIFT_CHECK_AMOUNT > 0) {

                    // add cash distribution

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT + SERVICE_CHARGE_AMOUNT - DISCOUNT_AMOUNT - GIFT_CHECK_AMOUNT, EJBCommon.FALSE, arReceipt.getAdBankAccount().getBaCoaGlCashAccount(), arReceipt, null, AD_BRNCH, AD_CMPNY);
                }

                // set receipt amount

                arReceipt.setRctAmount(EJBCommon.roundIt(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT + SERVICE_CHARGE_AMOUNT - DISCOUNT_AMOUNT, this.getGlFcPrecisionUnit(AD_CMPNY)));
            }

        } catch (GlobalInvItemLocationNotFoundException | GlobalRecordAlreadyExistException |
                 GlobalAmountInvalidException | GlobalJournalNotBalanceException | GlobalRecordDisabledException |
                 GlobalRecordInvalidException | GlobalRecordInvalidForCurrentBranchException |
                 GlobalDocumentNumberNotUniqueException | GlobalNoRecordFoundException |
                 GlobalBranchAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void importRctIl(ArrayList miscRctList, boolean isSummarized, String RB_NM, String TC_NM,
                            boolean taxableServiceCharge, boolean taxableDiscount, String CRTD_BY,
                            Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalDocumentNumberNotUniqueException, GlobalNoRecordFoundException,
            GlobalRecordInvalidForCurrentBranchException, GlobalRecordDisabledException,
            GlobalRecordInvalidException, GlobalBranchAccountNumberInvalidException,
            GlobalJournalNotBalanceException, GlobalAmountInvalidException,
            GlobalRecordAlreadyExistException {

        Debug.print("ArReceiptImportControllerBean importRctIl");
        LocalArReceipt arReceipt = null;
        LocalArWithholdingTaxCode arWithholdingTaxCode = null;
        LocalArTaxCode arTaxCode = null;
        try {

            Date CURR_DATE = EJBCommon.getGcCurrentDateWoTime().getTime();

            LocalArReceiptBatch arReceiptBatch = arReceiptBatchHome.create(RB_NM, null, "OPEN", "MISC", CURR_DATE, CRTD_BY, AD_BRNCH, AD_CMPNY);

            // check if POS data already exists

            ArModReceiptDetails rctDetails = (ArModReceiptDetails) miscRctList.get(0);

            Collection arReceipts = arReceiptHome.findByRctDateAndBaName(rctDetails.getRctDate(), "POS%", AD_CMPNY);

            if (!arReceipts.isEmpty()) {

                throw new GlobalRecordAlreadyExistException(EJBCommon.convertSQLDateToString(rctDetails.getRctDate()));
            }

            for (Object item : miscRctList) {

                ArModReceiptDetails details = (ArModReceiptDetails) item;

                if (details.getRctAmount() < 0) {

                    throw new GlobalAmountInvalidException("total amount for receipt " + details.getRctNumber());
                }

                String RCPT_NMBR = null;

                // validate if receipt number is unique receipt number is automatic then set next sequence

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                if (isSummarized) {

                    try {

                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR RECEIPT", AD_CMPNY);

                    } catch (FinderException ex) {

                    }

                    try {

                        adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {

                    }

                    if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A') {

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    arReceiptHome.findByRctNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                } catch (FinderException ex) {

                                    RCPT_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;
                                }

                            } else {

                                try {

                                    arReceiptHome.findByRctNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                } catch (FinderException ex) {

                                    RCPT_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;
                                }
                            }
                        }
                    }

                } else {

                    LocalArReceipt arExistingReceipt = null;

                    try {

                        arExistingReceipt = arReceiptHome.findByRctNumberAndBrCode(details.getRctNumber(), AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {
                    }

                    if (arExistingReceipt != null && !arExistingReceipt.getRctCode().equals(details.getRctCode())) {

                        throw new GlobalDocumentNumberNotUniqueException(details.getRctNumber());
                    }

                    RCPT_NMBR = details.getRctNumber();
                }

                // create misc receipt

                arReceipt = arReceiptHome.create("MISC", null, details.getRctDate(), RCPT_NMBR, null, null, null, null, null, null, null, null, details.getRctAmount(), 0d, 0d, 0d, 0d, 0d, 0d, null, details.getRctConversionRate(), null, null, EJBCommon.FALSE, 0d, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, null, null, null, null, null, CRTD_BY, CURR_DATE, CRTD_BY, CURR_DATE, null, null, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, null, null, EJBCommon.FALSE, EJBCommon.FALSE, null, AD_BRNCH, AD_CMPNY);

                LocalAdBankAccount adBankAccount = null;

                try {

                    adBankAccount = adBankAccountHome.findByBaNameAndBrCode(details.getRctBaName(), AD_BRNCH, AD_CMPNY);

                    if (adBankAccount.getBaEnable() == EJBCommon.TRUE) {

                        adBankAccount.addArReceipt(arReceipt);

                    } else {

                        throw new GlobalRecordDisabledException("Bank Account: " + details.getRctBaName());
                    }

                } catch (FinderException ex) {

                    throw new GlobalRecordInvalidForCurrentBranchException("Bank Account: " + details.getRctBaName());
                }

                // add customer
                LocalArCustomer arCustomer = null;

                if (details.getRctCstName() == null || details.getRctCstName().equals("")) {

                    throw new GlobalRecordInvalidException();
                }

                try {

                    arCustomer = arCustomerHome.findByCstName(details.getRctCstName(), AD_BRNCH, AD_CMPNY);

                    if (arCustomer.getCstEnable() == EJBCommon.TRUE) {

                        arCustomer.addArReceipt(arReceipt);

                    } else {

                        throw new GlobalRecordDisabledException("Customer: " + details.getRctCstName());
                    }

                } catch (FinderException ex) {

                    throw new GlobalRecordInvalidForCurrentBranchException("Customer: " + details.getRctCstName());
                }

                try {

                    arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName("NONE", AD_CMPNY);
                    if (arWithholdingTaxCode.getWtcEnable() == EJBCommon.TRUE) {

                        arWithholdingTaxCode.addArReceipt(arReceipt);

                    } else {

                        throw new GlobalRecordDisabledException("Withholding Tax: NONE");
                    }

                } catch (FinderException ex) {

                    throw new GlobalNoRecordFoundException("Withholding Tax: NONE");
                }

                try {

                    arTaxCode = arTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);
                    arTaxCode.addArReceipt(arReceipt);

                } catch (FinderException ex) {

                    throw new GlobalNoRecordFoundException("Tax: " + TC_NM);
                }

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(details.getRctFcName(), AD_CMPNY);

                if (glFunctionalCurrency.getFcEnable() == EJBCommon.TRUE) {

                    glFunctionalCurrency.addArReceipt(arReceipt);

                } else {

                    throw new GlobalRecordDisabledException("Currency: " + details.getRctFcName());
                }

                arReceiptBatch.addArReceipt(arReceipt);

                // add new invoice lines

                double TOTAL_TAX = 0d;
                double TOTAL_LINE = 0d;

                ArrayList ilList = details.getInvIlList();

                for (Object value : ilList) {

                    ArModInvoiceLineDetails mIlDetails = (ArModInvoiceLineDetails) value;

                    if (mIlDetails.getIlUnitPrice() < 0) {

                        throw new GlobalAmountInvalidException("unit price for memo line " + mIlDetails.getIlSmlName() + " of receipt " + arReceipt.getRctNumber());
                    }

                    if (mIlDetails.getIlQuantity() <= 0) {

                        throw new GlobalAmountInvalidException("quantity for memo line " + mIlDetails.getIlSmlName() + " of receipt " + arReceipt.getRctNumber());
                    }

                    LocalArStandardMemoLine arStandardMemoLine = null;

                    try {

                        arStandardMemoLine = arStandardMemoLineHome.findBySmlNameAndBrCode(mIlDetails.getIlSmlName(), AD_BRNCH, AD_CMPNY);

                        if (arStandardMemoLine.getSmlEnable() == EJBCommon.FALSE) {

                            throw new GlobalRecordDisabledException("Standard Memo Line: " + mIlDetails.getIlSmlName());
                        }

                    } catch (FinderException ex) {

                        throw new GlobalRecordInvalidForCurrentBranchException("Standard Memo Line: " + mIlDetails.getIlSmlName());
                    }

                    mIlDetails.setIlTax(arStandardMemoLine.getSmlTax());
                    mIlDetails.setIlAmount(mIlDetails.getIlUnitPrice() * mIlDetails.getIlQuantity());

                    LocalArInvoiceLine arInvoiceLine = this.addArIlEntry(mIlDetails, arReceipt, AD_CMPNY);

                    // add revenue/credit distributions

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arInvoiceLine.getIlAmount(), EJBCommon.FALSE, this.getArGlCoaRevenueAccount(arInvoiceLine, AD_BRNCH, AD_CMPNY), arReceipt, null, AD_BRNCH, AD_CMPNY);

                    TOTAL_TAX += arInvoiceLine.getIlTaxAmount();
                    TOTAL_LINE += arInvoiceLine.getIlAmount();
                }

                double SERVICE_CHARGE_AMOUNT = 0d;
                double DISCOUNT_AMOUNT = 0d;
                double GIFT_CHECK_AMOUNT = 0d;
                double DISCOUNT_TAX = 0d;
                double SERVICE_CHARGE_TAX = 0d;

                short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

                ArrayList drList = details.getRctDrList();

                for (Object o : drList) {

                    ArModDistributionRecordDetails drDetails = (ArModDistributionRecordDetails) o;

                    if (drDetails.getDrClass().equals("SERVICE CHARGE") && drDetails.getDrAmount() > 0) {

                        LocalGlChartOfAccount glServiceChargeAccount = null;

                        try {

                            glServiceChargeAccount = glChartOfAccountHome.findByCoaAccountNumber(drDetails.getDrCoaAccountNumber(), AD_CMPNY);

                        } catch (FinderException ex) {

                            throw new GlobalNoRecordFoundException("Service Charge Account " + drDetails.getDrCoaAccountNumber());
                        }

                        Collection arDistributionRecords = null;

                        try {

                            arDistributionRecords = arDistributionRecordHome.findDrsByDrClassAndRctCode("SERVICE", arReceipt.getRctCode(), AD_CMPNY);

                        } catch (FinderException ex) {

                        }

                        if (taxableServiceCharge && !arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {

                            SERVICE_CHARGE_TAX = this.getTaxAmount(arTaxCode, drDetails.getDrAmount(), precisionUnit);

                            if (arTaxCode.getTcType().equals("INCLUSIVE")) {

                                SERVICE_CHARGE_AMOUNT = drDetails.getDrAmount() - SERVICE_CHARGE_TAX;

                            } else {

                                SERVICE_CHARGE_AMOUNT = drDetails.getDrAmount();
                            }

                        } else {

                            SERVICE_CHARGE_AMOUNT = drDetails.getDrAmount();
                        }

                        if (arDistributionRecords.isEmpty()) {

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "SERVICE", EJBCommon.FALSE, SERVICE_CHARGE_AMOUNT, EJBCommon.FALSE, glServiceChargeAccount.getCoaCode(), arReceipt, null, AD_BRNCH, AD_CMPNY);

                        } else {

                            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) arDistributionRecords.iterator().next();

                            arDistributionRecord.setDrAmount(arDistributionRecord.getDrAmount() + SERVICE_CHARGE_AMOUNT);

                            SERVICE_CHARGE_AMOUNT = arDistributionRecord.getDrAmount();
                        }

                    } else if (drDetails.getDrClass().equals("DISCOUNT") && drDetails.getDrAmount() > 0) {

                        LocalGlChartOfAccount glDiscountAccount = null;

                        try {

                            glDiscountAccount = glChartOfAccountHome.findByCoaAccountNumber(drDetails.getDrCoaAccountNumber(), AD_CMPNY);

                        } catch (FinderException ex) {

                            throw new GlobalNoRecordFoundException("Discount Account " + drDetails.getDrCoaAccountNumber());
                        }

                        Collection arDistributionRecords = null;

                        try {

                            arDistributionRecords = arDistributionRecordHome.findDrsByDrClassAndRctCode("DISCOUNT", arReceipt.getRctCode(), AD_CMPNY);

                        } catch (FinderException ex) {

                        }

                        if (taxableDiscount && !arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {

                            DISCOUNT_TAX = this.getTaxAmount(arTaxCode, drDetails.getDrAmount(), precisionUnit);

                            if (arTaxCode.getTcType().equals("INCLUSIVE")) {

                                DISCOUNT_AMOUNT = drDetails.getDrAmount() - DISCOUNT_TAX;

                            } else {

                                DISCOUNT_AMOUNT = drDetails.getDrAmount();
                            }

                        } else {

                            DISCOUNT_AMOUNT = drDetails.getDrAmount();
                        }

                        if (arDistributionRecords.isEmpty()) {

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "DISCOUNT", EJBCommon.TRUE, DISCOUNT_AMOUNT, EJBCommon.FALSE, glDiscountAccount.getCoaCode(), arReceipt, null, AD_BRNCH, AD_CMPNY);

                        } else {

                            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) arDistributionRecords.iterator().next();

                            arDistributionRecord.setDrAmount(arDistributionRecord.getDrAmount() + DISCOUNT_AMOUNT);

                            DISCOUNT_AMOUNT = arDistributionRecord.getDrAmount();
                        }

                    } else if (drDetails.getDrClass().equals("GIFT CHECK") && drDetails.getDrAmount() > 0) {

                        LocalGlChartOfAccount glGiftCheckAccount = null;

                        try {

                            glGiftCheckAccount = glChartOfAccountHome.findByCoaAccountNumber(drDetails.getDrCoaAccountNumber(), AD_CMPNY);

                        } catch (FinderException ex) {

                            throw new GlobalNoRecordFoundException("Gift Check Account " + drDetails.getDrCoaAccountNumber());
                        }

                        Collection arDistributionRecords = null;

                        try {

                            arDistributionRecords = arDistributionRecordHome.findDrsByDrClassAndRctCode("GIFT CHECK", arReceipt.getRctCode(), AD_CMPNY);

                        } catch (FinderException ex) {

                        }

                        if (arDistributionRecords.isEmpty()) {

                            this.addArDrEntry(arReceipt.getArDrNextLine(), "GIFT CHECK", EJBCommon.TRUE, drDetails.getDrAmount(), EJBCommon.FALSE, glGiftCheckAccount.getCoaCode(), arReceipt, null, AD_BRNCH, AD_CMPNY);

                            GIFT_CHECK_AMOUNT = drDetails.getDrAmount();

                        } else {

                            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) arDistributionRecords.iterator().next();

                            arDistributionRecord.setDrAmount(arDistributionRecord.getDrAmount() + drDetails.getDrAmount());

                            GIFT_CHECK_AMOUNT = arDistributionRecord.getDrAmount();
                        }
                    }
                }

                // add tax distribution if necessary

                TOTAL_TAX = TOTAL_TAX + SERVICE_CHARGE_TAX - DISCOUNT_TAX;

                if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, EJBCommon.FALSE, arTaxCode.getGlChartOfAccount().getCoaCode(), arReceipt, null, AD_BRNCH, AD_CMPNY);
                }

                // add wtax distribution if necessary

                double W_TAX_AMOUNT = 0d;

                if (arWithholdingTaxCode.getWtcRate() != 0) {

                    W_TAX_AMOUNT = EJBCommon.roundIt(TOTAL_LINE * (arWithholdingTaxCode.getWtcRate() / 100), this.getGlFcPrecisionUnit(AD_CMPNY));

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "W-TAX", EJBCommon.TRUE, W_TAX_AMOUNT, EJBCommon.FALSE, arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), arReceipt, null, AD_BRNCH, AD_CMPNY);
                }

                double RCPT_AMNT = details.getRctAmount() + GIFT_CHECK_AMOUNT;

                if (RCPT_AMNT != EJBCommon.roundIt(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT + SERVICE_CHARGE_AMOUNT - DISCOUNT_AMOUNT, this.getGlFcPrecisionUnit(AD_CMPNY))) {

                    throw new GlobalJournalNotBalanceException(arReceipt.getRctNumber());
                }

                // add cash distribution

                this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT + SERVICE_CHARGE_AMOUNT - DISCOUNT_AMOUNT - GIFT_CHECK_AMOUNT, EJBCommon.FALSE, arReceipt.getAdBankAccount().getBaCoaGlCashAccount(), arReceipt, null, AD_BRNCH, AD_CMPNY);

                // set receipt amount

                arReceipt.setRctAmount(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT + SERVICE_CHARGE_AMOUNT - DISCOUNT_AMOUNT);
            }

        } catch (GlobalNoRecordFoundException | GlobalRecordAlreadyExistException | GlobalAmountInvalidException |
                 GlobalJournalNotBalanceException | GlobalBranchAccountNumberInvalidException |
                 GlobalRecordInvalidException | GlobalRecordDisabledException |
                 GlobalRecordInvalidForCurrentBranchException | GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private LocalArAppliedInvoice addArAiEntry(ArModAppliedInvoiceDetails mdetails, LocalArReceipt arReceipt,
                                               LocalArReceiptBatch arReceiptBatch, Integer AD_CMPNY)
            throws GlobalTransactionAlreadyLockedException, ArINVOverapplicationNotAllowedException {

        Debug.print("ArReceiptImportControllerBean addArAiEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            // get functional currency name

            String FC_NM = adCompany.getGlFunctionalCurrency().getFcName();

            // validate overapplication

            LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arInvoicePaymentScheduleHome.findByPrimaryKey(mdetails.getAiIpsCode());

            if (EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), this.getGlFcPrecisionUnit(AD_CMPNY)) < EJBCommon.roundIt(mdetails.getAiApplyAmount() + mdetails.getAiCreditableWTax() + mdetails.getAiDiscountAmount(), this.getGlFcPrecisionUnit(AD_CMPNY))) {

                throw new ArINVOverapplicationNotAllowedException(arInvoicePaymentSchedule.getArInvoice().getInvNumber());
            }

            Collection arAppliedInvoices = null;
            LocalArAppliedInvoice arAppliedInvoice = null;

            arAppliedInvoices = arInvoicePaymentSchedule.getArAppliedInvoices();

            Iterator appInvcIter = arAppliedInvoices.iterator();

            double TOTAL_AMOUNT = 0d;

            while (appInvcIter.hasNext()) {

                arAppliedInvoice = (LocalArAppliedInvoice) appInvcIter.next();

                if (arAppliedInvoice.getArReceipt().getRctVoidPosted() == EJBCommon.FALSE) {

                    TOTAL_AMOUNT += arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiDiscountAmount();
                }
            }

            if (TOTAL_AMOUNT + mdetails.getAiApplyAmount() + mdetails.getAiDiscountAmount() > arInvoicePaymentSchedule.getIpsAmountDue()) {

                throw new ArINVOverapplicationNotAllowedException(arInvoicePaymentSchedule.getArInvoice().getInvNumber());
            }

            // validate if ips already locked

            if (arInvoicePaymentSchedule.getIpsLock() == EJBCommon.TRUE) {

                Collection arReceipts = arReceiptBatch.getArReceipts();

                Iterator i = arReceipts.iterator();

                boolean found = false;
                while (i.hasNext()) {

                    LocalArReceipt existingArReceipt = (LocalArReceipt) i.next();

                    arAppliedInvoices = existingArReceipt.getArAppliedInvoices();

                    for (Object appliedInvoice : arAppliedInvoices) {

                        arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoice;

                        if (arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvNumber().equals(mdetails.getAiIpsInvNumber())) {
                            found = true;
                        }
                    }
                }

                if (found == false) {

                    throw new GlobalTransactionAlreadyLockedException(arInvoicePaymentSchedule.getArInvoice().getInvNumber());
                }
            }

            double AI_FRX_GN_LSS = 0d;

            if (!FC_NM.equals(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName()) || !FC_NM.equals(arReceipt.getGlFunctionalCurrency().getFcName())) {

                double AI_ALLCTD_PYMNT_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), mdetails.getAiAllocatedPaymentAmount(), AD_CMPNY);

                double AI_APPLY_AMNT = this.convertForeignToFunctionalCurrency(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcCode(), arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName(), arInvoicePaymentSchedule.getArInvoice().getInvConversionDate(), arInvoicePaymentSchedule.getArInvoice().getInvConversionRate(), mdetails.getAiApplyAmount(), AD_CMPNY);

                double AI_CRDTBL_W_TX = this.convertForeignToFunctionalCurrency(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcCode(), arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName(), arInvoicePaymentSchedule.getArInvoice().getInvConversionDate(), arInvoicePaymentSchedule.getArInvoice().getInvConversionRate(), mdetails.getAiCreditableWTax(), AD_CMPNY);

                double AI_DSCNT_AMNT = this.convertForeignToFunctionalCurrency(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcCode(), arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName(), arInvoicePaymentSchedule.getArInvoice().getInvConversionDate(), arInvoicePaymentSchedule.getArInvoice().getInvConversionRate(), mdetails.getAiDiscountAmount(), AD_CMPNY);

                AI_FRX_GN_LSS = EJBCommon.roundIt((AI_ALLCTD_PYMNT_AMNT + AI_CRDTBL_W_TX + AI_DSCNT_AMNT) - (AI_APPLY_AMNT + AI_CRDTBL_W_TX + AI_DSCNT_AMNT), this.getGlFcPrecisionUnit(AD_CMPNY));
            }

            // create applied invoice

            arAppliedInvoice = arAppliedInvoiceHome.create(mdetails.getAiApplyAmount(), mdetails.getAiPenaltyApplyAmount(), mdetails.getAiCreditableWTax(), mdetails.getAiDiscountAmount(), mdetails.getAiRebate(), 0d, mdetails.getAiAllocatedPaymentAmount(), AI_FRX_GN_LSS, EJBCommon.FALSE, AD_CMPNY);

            arReceipt.addArAppliedInvoice(arAppliedInvoice);
            arInvoicePaymentSchedule.addArAppliedInvoice(arAppliedInvoice);

            // lock invoice

            arInvoicePaymentSchedule.setIpsLock(EJBCommon.TRUE);

            return arAppliedInvoice;

        } catch (GlobalTransactionAlreadyLockedException | ArINVOverapplicationNotAllowedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSD,
                              Integer COA_CODE, LocalArReceipt arReceipt, LocalArAppliedInvoice arAppliedInvoice,
                              Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArReceiptImportControllerBean addArDrEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, AD_CMPNY);

            } catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, DR_RVRSD, AD_CMPNY);

            arReceipt.addArDistributionRecord(arDistributionRecord);
            glChartOfAccount.addArDistributionRecord(arDistributionRecord);

            // to be used by gl journal interface for cross currency receipts
            if (arAppliedInvoice != null) {

                arAppliedInvoice.addArDistributionRecord(arDistributionRecord);
            }

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE,
                                                      double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("ArReceiptImportControllerBean convertForeignToFunctionalCurrency");
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

    private LocalArInvoiceLineItem addArIliEntry(ArModInvoiceLineItemDetails mdetails, LocalArReceipt arReceipt,
                                                 LocalInvItemLocation invItemLocation, Integer AD_CMPNY)
            throws GlobalRecordDisabledException {

        Debug.print("ArReceiptImportControllerBean addArIliEntry");
        try {

            short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

            double ILI_AMNT = 0d;
            double ILI_TAX_AMNT = 0d;

            // calculate net amount

            LocalArTaxCode arTaxCode = arReceipt.getArTaxCode();

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

            LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.create(mdetails.getIliLine(), mdetails.getIliQuantity(), mdetails.getIliUnitPrice(), ILI_AMNT, ILI_TAX_AMNT, EJBCommon.FALSE, mdetails.getIliDiscount1(), mdetails.getIliDiscount2(), mdetails.getIliDiscount3(), mdetails.getIliDiscount4(), mdetails.getIliTotalDiscount(), EJBCommon.TRUE, AD_CMPNY);

            arReceipt.addArInvoiceLineItem(arInvoiceLineItem);

            invItemLocation.addArInvoiceLineItem(arInvoiceLineItem);

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getIliUomName(), AD_CMPNY);

            if (invUnitOfMeasure.getUomEnable() == EJBCommon.FALSE) {

                throw new GlobalRecordDisabledException("Unit of measure: " + invUnitOfMeasure.getUomName());
            }

            invUnitOfMeasure.addArInvoiceLineItem(arInvoiceLineItem);

            return arInvoiceLineItem;

        } catch (GlobalRecordDisabledException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrIliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE,
                                 LocalArReceipt arReceipt, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArReceiptImportControllerBean addArDrIliEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, AD_CMPNY);

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

            arReceipt.addArDistributionRecord(arDistributionRecord);
            glChartOfAccount.addArDistributionRecord(arDistributionRecord);

        } catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException(ex.getMessage());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalArInvoiceLine addArIlEntry(ArModInvoiceLineDetails mdetails, LocalArReceipt arReceipt, Integer AD_CMPNY) {

        Debug.print("ArReceiptImportControllerBean addArIlEntry");
        try {

            short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

            double IL_AMNT = 0d;
            double IL_TAX_AMNT = 0d;

            if (mdetails.getIlTax() == EJBCommon.TRUE) {

                // calculate net amount

                LocalArTaxCode arTaxCode = arReceipt.getArTaxCode();

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

            arReceipt.addArInvoiceLine(arInvoiceLine);

            LocalArStandardMemoLine arStandardMemoLine = arStandardMemoLineHome.findBySmlName(mdetails.getIlSmlName(), AD_CMPNY);
            arStandardMemoLine.addArInvoiceLine(arInvoiceLine);

            return arInvoiceLine;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private Integer getArGlCoaRevenueAccount(LocalArInvoiceLine arInvoiceLine, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArReceiptImportControllerBean getArGlCoaRevenueAccount");
        // generate revenue account
        try {

            StringBuilder GL_COA_ACCNT = new StringBuilder();

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGenField genField = adCompany.getGenField();

            String FL_SGMNT_SPRTR = String.valueOf(genField.getFlSegmentSeparator());

            LocalAdBranchStandardMemoLine adBranchStandardMemoLine = null;

            try {

                adBranchStandardMemoLine = adBranchStandardMemoLineHome.findBSMLBySMLCodeAndBrCode(arInvoiceLine.getArStandardMemoLine().getSmlCode(), AD_BRNCH, AD_CMPNY);

            } catch (FinderException ex) {

            }

            Collection arAutoAccountingSegments = arAutoAccountingSegmentHome.findByAaAccountType("REVENUE", AD_CMPNY);

            for (Object autoAccountingSegment : arAutoAccountingSegments) {

                LocalArAutoAccountingSegment arAutoAccountingSegment = (LocalArAutoAccountingSegment) autoAccountingSegment;

                LocalGlChartOfAccount glChartOfAccount = null;

                if (arAutoAccountingSegment.getAasClassType().equals("AR CUSTOMER")) {

                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arInvoiceLine.getArReceipt().getArCustomer().getCstGlCoaRevenueAccount());

                    StringTokenizer st = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), FL_SGMNT_SPRTR);

                    int ctr = 0;
                    while (st.hasMoreTokens()) {

                        ++ctr;

                        if (ctr == arAutoAccountingSegment.getAasSegmentNumber()) {

                            GL_COA_ACCNT.append(FL_SGMNT_SPRTR).append(st.nextToken());

                            break;

                        } else {

                            st.nextToken();
                        }
                    }

                } else if (arAutoAccountingSegment.getAasClassType().equals("AR STANDARD MEMO LINE")) {

                    if (adBranchStandardMemoLine != null) {

                        try {

                            glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlAccount());

                        } catch (FinderException ex) {

                        }

                    } else {

                        glChartOfAccount = arInvoiceLine.getArStandardMemoLine().getGlChartOfAccount();
                    }

                    StringTokenizer st = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), FL_SGMNT_SPRTR);

                    int ctr = 0;
                    while (st.hasMoreTokens()) {

                        ++ctr;

                        if (ctr == arAutoAccountingSegment.getAasSegmentNumber()) {

                            GL_COA_ACCNT.append(FL_SGMNT_SPRTR).append(st.nextToken());

                            break;

                        } else {

                            st.nextToken();
                        }
                    }
                }
            }

            GL_COA_ACCNT = new StringBuilder(GL_COA_ACCNT.substring(1));

            try {

                LocalGlChartOfAccount glGeneratedChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(GL_COA_ACCNT.toString(), AD_CMPNY);

                return glGeneratedChartOfAccount.getCoaCode();

            } catch (FinderException ex) {

                if (adBranchStandardMemoLine != null) {

                    LocalGlChartOfAccount glChartOfAccount = null;

                    try {

                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlAccount());

                    } catch (FinderException e) {

                    }

                    return glChartOfAccount.getCoaCode();

                } else {

                    return arInvoiceLine.getArStandardMemoLine().getGlChartOfAccount().getCoaCode();
                }
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure,
                                                      LocalInvItem invItem, double ADJST_QTY, Integer AD_CMPNY) {

        Debug.print("ArReceiptImportControllerBean convertByUomFromAndItemAndQuantity");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            return EJBCommon.roundIt(ADJST_QTY * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getTaxAmount(LocalArTaxCode arTaxCode, double TAXABLE_AMOUNT, short precisionUnit) {

        double AMNT = 0d;
        double TAX_AMNT = 0d;
        if (arTaxCode.getTcType().equals("INCLUSIVE")) {

            AMNT = EJBCommon.roundIt(TAXABLE_AMOUNT / (1 + (arTaxCode.getTcRate() / 100)), precisionUnit);

        } else {

            // tax exclusive

            AMNT = TAXABLE_AMOUNT;
        }
        // calculate tax
        if (arTaxCode.getTcType().equals("INCLUSIVE")) {

            TAX_AMNT = EJBCommon.roundIt(TAXABLE_AMOUNT - AMNT, precisionUnit);

        } else if (arTaxCode.getTcType().equals("EXCLUSIVE")) {

            TAX_AMNT = EJBCommon.roundIt(TAXABLE_AMOUNT * arTaxCode.getTcRate() / 100, precisionUnit);
        }
        return TAX_AMNT;
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArReceiptEntryControllerBean ejbCreate");
    }

}