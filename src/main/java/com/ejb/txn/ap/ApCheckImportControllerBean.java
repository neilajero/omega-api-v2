/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class ApCheckImportControllerBean
 * @created December 27, 2005 02:23 PM
 * @author Farrah S. Garing
 */
package com.ejb.txn.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdAmountLimitHome;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.dao.ad.LocalAdApprovalQueueHome;
import com.ejb.dao.ad.LocalAdApprovalUserHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ad.LocalAdDocumentSequenceAssignmentHome;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApAppliedVoucher;
import com.ejb.dao.ap.LocalApAppliedVoucherHome;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.entities.ap.LocalApCheckBatch;
import com.ejb.dao.ap.LocalApCheckBatchHome;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.entities.ap.LocalApVoucherPaymentSchedule;
import com.ejb.dao.ap.LocalApVoucherPaymentScheduleHome;
import com.ejb.exception.ap.ApCHKCheckNumberNotUniqueException;
import com.ejb.exception.ap.ApCHKVoucherHasNoWTaxCodeException;
import com.ejb.exception.ap.ApVOUOverapplicationNotAllowedException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalJournalNotBalanceException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordInvalidException;
import com.ejb.exception.global.GlobalTransactionAlreadyLockedException;
import com.ejb.exception.global.GlobalTransactionAlreadyPostedException;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.util.mod.ap.ApModAppliedVoucherDetails;
import com.util.mod.ap.ApModCheckDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApCheckImportControllerEJB")
public class ApCheckImportControllerBean extends EJBContextClass implements ApCheckImportController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalApAppliedVoucherHome apAppliedVoucherHome;
    @EJB
    private LocalApCheckBatchHome apCheckBatchHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalApVoucherPaymentScheduleHome apVoucherPaymentScheduleHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;

    public void importCheck(ArrayList list, String FC_NM, String CB_NM, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalDocumentNumberNotUniqueException, ApCHKCheckNumberNotUniqueException, ApCHKVoucherHasNoWTaxCodeException, GlobalNoRecordFoundException, GlobalJournalNotBalanceException, ApVOUOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, GlobalRecordInvalidException, GlobalTransactionAlreadyPostedException {

        Debug.print("ApCheckImportControllerBean saveApChkEntry");

        LocalApCheck apCheck = null;

        try {

            Iterator i = list.iterator();

            while (i.hasNext()) {

                ApModCheckDetails details = (ApModCheckDetails) i.next();

                details = validate(details);

                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

                // validate if document number is unique document number is automatic then set
                // next sequence

                LocalApCheck apExistingCheck = null;

                try {

                    apExistingCheck = apCheckHome.findByChkDocumentNumberAndBrCode(details.getChkDocumentNumber(), AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {
                }

                if (apExistingCheck != null) {

                    throw new GlobalDocumentNumberNotUniqueException(details.getChkDocumentNumber());
                }

                // validate if check number is unique check is automatic then set next sequence

                try {

                    apExistingCheck = apCheckHome.findByChkNumberAndBaName(details.getChkNumber(), details.getChkBaName(), AD_CMPNY);

                } catch (FinderException ex) {

                }

                if (apExistingCheck != null) {

                    throw new ApCHKCheckNumberNotUniqueException(details.getChkNumber() + " for bank account: " + details.getChkBaName());
                }

                LocalAdBankAccount adBankAccount = null;

                try {

                    adBankAccount = adBankAccountHome.findByBaName(details.getChkBaName(), AD_CMPNY);

                } catch (FinderException ex) {

                    throw new GlobalNoRecordFoundException("Bank Account " + details.getChkBaName());
                }

                if (details.getChkNumber() == null || details.getChkNumber().trim().length() == 0) {

                    while (true) {

                        try {

                            apCheckHome.findByChkNumberAndBaName(adBankAccount.getBaNextCheckNumber(), details.getChkBaName(), AD_CMPNY);
                            adBankAccount.setBaNextCheckNumber(EJBCommon.incrementStringNumber(adBankAccount.getBaNextCheckNumber()));

                        } catch (FinderException ex) {

                            details.setChkNumber(adBankAccount.getBaNextCheckNumber());
                            adBankAccount.setBaNextCheckNumber(EJBCommon.incrementStringNumber(adBankAccount.getBaNextCheckNumber()));
                            break;
                        }
                    }
                }

                // create check

                apCheck = apCheckHome.create("PAYMENT", null, details.getChkDate(), details.getChkCheckDate(), null, null, null, null, null, details.getChkNumber(), details.getChkDocumentNumber(), details.getChkReferenceNumber(), EJBCommon.FALSE, EJBCommon.FALSE, null, null, null, 0d, 0d, 0d, 0d, 0d, EJBCommon.FALSE, EJBCommon.FALSE, details.getChkConversionDate(), 1.0, 0d, details.getChkAmount(), null, null, EJBCommon.FALSE, EJBCommon.FALSE, details.getChkCrossCheck(), null, EJBCommon.FALSE, details.getChkCreatedBy(), details.getChkDateCreated(), details.getChkLastModifiedBy(), details.getChkDateLastModified(), null, null, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, null, null, AD_BRNCH, AD_CMPNY);

                apCheck.setAdBankAccount(adBankAccount);

                try {

                    LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
                    apCheck.setGlFunctionalCurrency(glFunctionalCurrency);

                } catch (FinderException ex) {

                    throw new GlobalNoRecordFoundException("Functional Currency " + FC_NM);
                }

                LocalApSupplier apSupplier = null;

                try {

                    apSupplier = apSupplierHome.findBySplSupplierCode(details.getChkSplName(), AD_CMPNY);
                    apCheck.setApSupplier(apSupplier);

                } catch (FinderException ex) {

                    throw new GlobalNoRecordFoundException("Supplier " + details.getChkSplName());
                }

                LocalApCheckBatch apCheckBatch = null;

                try {

                    apCheckBatch = apCheckBatchHome.findByCbName(CB_NM, AD_BRNCH, AD_CMPNY);
                    apCheck.setApCheckBatch(apCheckBatch);

                } catch (FinderException ex) {

                    // create new batch and validate

                    apCheckBatch = apCheckBatchHome.create(CB_NM, "CHECKBATCH" + CB_NM, "OPEN", "MISC", apCheck.getChkDateCreated(), apCheck.getChkCreatedBy(), "", AD_BRNCH, AD_CMPNY);
                    apCheckBatch.addApCheck(apCheck);
                }

                // remove all distribution records

                Collection apDistributionRecords = apCheck.getApDistributionRecords();

                Iterator iDr = apDistributionRecords.iterator();

                while (iDr.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                    iDr.remove();

                    // apDistributionRecord.entityRemove();
                    em.remove(apDistributionRecord);
                }

                // release vps locks and remove all applied vouchers

                Collection apAppliedVouchers = apCheck.getApAppliedVouchers();

                iDr = apAppliedVouchers.iterator();

                while (iDr.hasNext()) {

                    LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) iDr.next();

                    apAppliedVoucher.getApVoucherPaymentSchedule().setVpsLock(EJBCommon.FALSE);

                    iDr.remove();

                    // apAppliedVoucher.entityRemove();
                    em.remove(apAppliedVoucher);
                }

                // add new applied vouchers and distribution record

                Integer payableAccount = null;
                double payableAmount = 0d;

                ArrayList avList = details.getChkAvList();
                iDr = avList.iterator();

                while (iDr.hasNext()) {

                    ApModAppliedVoucherDetails mAvDetails = (ApModAppliedVoucherDetails) iDr.next();

                    // validate if voucher exists

                    Collection apVoucherPaymentSchedules = null;

                    LocalApVoucher apVoucher = null;

                    try {

                        apVoucher = apVoucherHome.findByVouDocumentNumber(mAvDetails.getAvVpsVouDocumentNumber(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new GlobalNoRecordFoundException("Voucher " + mAvDetails.getAvVpsVouDocumentNumber());
                    }

                    // check if check's supplier matches voucher's supplier
                    if (!apSupplier.getSplName().equals(apVoucher.getApSupplier().getSplName())) {

                        throw new GlobalRecordInvalidException(details.getChkDocumentNumber() + ":Supplier " + details.getChkSplName() + " - " + apVoucher.getApSupplier().getSplName());
                    }

                    if (apVoucher.getVouPosted() == EJBCommon.FALSE) {

                        throw new GlobalTransactionAlreadyPostedException("Voucher " + mAvDetails.getAvVpsVouDocumentNumber() + " for cv # " + details.getChkDocumentNumber());
                    }

                    if (apVoucher.getVouAmountDue() == apVoucher.getVouAmountPaid()) {

                        throw new ApVOUOverapplicationNotAllowedException(mAvDetails.getAvVpsChkDocumentNumber() + " for Voucher:" + apVoucher.getVouDocumentNumber());
                    }

                    try {

                        apVoucherPaymentSchedules = apVoucherPaymentScheduleHome.findOpenVpsByVouDocumentNumber(mAvDetails.getAvVpsVouDocumentNumber(), AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new GlobalNoRecordFoundException("Voucher " + mAvDetails.getAvVpsVouDocumentNumber());
                    }

                    Iterator itrVPS = apVoucherPaymentSchedules.iterator();
                    double totalPayment = mAvDetails.getAvApplyAmount();
                    LocalApAppliedVoucher apAppliedVoucher = null;

                    while (itrVPS.hasNext() && totalPayment > 0d) {

                        LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) itrVPS.next();
                        ApModAppliedVoucherDetails mDetails = new ApModAppliedVoucherDetails();

                        Collection listAV = apVoucherPaymentSchedule.getApAppliedVouchers();
                        Iterator iAV = listAV.iterator();
                        double APPLY_AMNT = 0d;
                        double DSCNT_AMNT = 0d;

                        while (iAV.hasNext()) {

                            LocalApAppliedVoucher existingApAppliedVoucher = (LocalApAppliedVoucher) iAV.next();

                            if (existingApAppliedVoucher.getApCheck().getChkVoidPosted() == EJBCommon.FALSE) {

                                APPLY_AMNT = APPLY_AMNT + existingApAppliedVoucher.getAvApplyAmount();
                                DSCNT_AMNT = DSCNT_AMNT + existingApAppliedVoucher.getAvDiscountAmount();
                            }
                        }

                        if (itrVPS.hasNext() && totalPayment > apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid() - APPLY_AMNT - DSCNT_AMNT) {

                            mDetails.setAvApplyAmount(apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid() - APPLY_AMNT - DSCNT_AMNT);

                        } else {

                            mDetails.setAvApplyAmount(totalPayment);
                            mDetails.setAvDiscountAmount(mAvDetails.getAvDiscountAmount());
                        }

                        if (mDetails.getAvApplyAmount() == 0) {

                            continue;
                        }

                        totalPayment = totalPayment - mDetails.getAvApplyAmount();
                        mDetails.setAvVpsCode(apVoucherPaymentSchedule.getVpsCode());
                        mDetails.setAvVpsVouDocumentNumber(mAvDetails.getAvVpsVouDocumentNumber());
                        mDetails.setAvVpsChkDocumentNumber(mAvDetails.getAvVpsChkDocumentNumber());

                        apAppliedVoucher = this.addApAvEntry(mDetails, apCheck, apCheckBatch, AD_CMPNY);

                        // get payable account

                        LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.findByDrClassAndVouCode("PAYABLE", apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouCode(), AD_CMPNY);

                        // check if applied voucher is a functional currency

                        if (apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getGlFunctionalCurrency().getFcCode().equals(adCompany.getGlFunctionalCurrency().getFcCode()) && apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getGlFunctionalCurrency().getFcCode().equals(apCheck.getGlFunctionalCurrency().getFcCode()) && (payableAccount == null || apDistributionRecord.getGlChartOfAccount().getCoaCode().equals(payableAccount))) {

                            payableAccount = apDistributionRecord.getGlChartOfAccount().getCoaCode();
                            payableAmount += (apAppliedVoucher.getAvApplyAmount() + apAppliedVoucher.getAvTaxWithheld() + apAppliedVoucher.getAvDiscountAmount());

                        } else {

                            this.addApDrEntry(apCheck.getApDrNextLine(), "PAYABLE", EJBCommon.TRUE, apAppliedVoucher.getAvApplyAmount() + apAppliedVoucher.getAvTaxWithheld() + apAppliedVoucher.getAvDiscountAmount(), EJBCommon.FALSE, apDistributionRecord.getGlChartOfAccount().getCoaCode(), apCheck, apAppliedVoucher, AD_CMPNY);
                        }

                        // create discount distribution records if necessary

                        if (apAppliedVoucher.getAvDiscountAmount() != 0) {

                            short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

                            // get discount percent

                            double DISCOUNT_PERCENT = EJBCommon.roundIt(apAppliedVoucher.getAvDiscountAmount() / (apAppliedVoucher.getAvApplyAmount() + apAppliedVoucher.getAvTaxWithheld() + apAppliedVoucher.getAvDiscountAmount()), (short) 6);
                            DISCOUNT_PERCENT = EJBCommon.roundIt(DISCOUNT_PERCENT * ((apAppliedVoucher.getAvApplyAmount() + apAppliedVoucher.getAvTaxWithheld() + apAppliedVoucher.getAvDiscountAmount()) / apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouAmountDue()), (short) 6);

                            Collection apDiscountDistributionRecords = apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getApDistributionRecords();

                            // get total debit and credit for rounding difference calculation

                            double TOTAL_DEBIT = 0d;
                            double TOTAL_CREDIT = 0d;
                            boolean isRoundingDifferenceCalculated = false;

                            Iterator j = apDiscountDistributionRecords.iterator();

                            while (j.hasNext()) {

                                LocalApDistributionRecord apDiscountDistributionRecord = (LocalApDistributionRecord) j.next();

                                if (apDiscountDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                                    TOTAL_DEBIT += EJBCommon.roundIt(apDiscountDistributionRecord.getDrAmount() * DISCOUNT_PERCENT, precisionUnit);

                                } else {

                                    TOTAL_CREDIT += EJBCommon.roundIt(apDiscountDistributionRecord.getDrAmount() * DISCOUNT_PERCENT, precisionUnit);
                                }
                            }

                            j = apDiscountDistributionRecords.iterator();

                            while (j.hasNext()) {

                                LocalApDistributionRecord apDiscountDistributionRecord = (LocalApDistributionRecord) j.next();

                                if (apDiscountDistributionRecord.getDrClass().equals("PAYABLE")) continue;

                                double DR_AMNT = EJBCommon.roundIt(apDiscountDistributionRecord.getDrAmount() * DISCOUNT_PERCENT, precisionUnit);

                                // calculate rounding difference if necessary

                                if (apDiscountDistributionRecord.getDrDebit() == EJBCommon.TRUE && TOTAL_DEBIT != TOTAL_CREDIT && !isRoundingDifferenceCalculated) {

                                    DR_AMNT = DR_AMNT + TOTAL_CREDIT - TOTAL_DEBIT;

                                    isRoundingDifferenceCalculated = true;
                                }

                                this.addApDrEntry(apCheck.getApDrNextLine(), apDiscountDistributionRecord.getDrClass(), apDiscountDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, DR_AMNT, EJBCommon.FALSE, apDiscountDistributionRecord.getGlChartOfAccount().getCoaCode(), apCheck, apAppliedVoucher, AD_CMPNY);
                            }
                        }
                    }

                    // create tax withheld distribution record if necessary

                    if (mAvDetails.getAvTaxWithheld() > 0) {

                        this.addApDrEntry(apCheck.getApDrNextLine(), "W-TAX", EJBCommon.FALSE, apAppliedVoucher.getAvTaxWithheld(), EJBCommon.FALSE, apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getApWithholdingTaxCode().getGlChartOfAccount().getCoaCode(), apCheck, apAppliedVoucher, AD_CMPNY);
                    }
                }

                if (payableAccount != null) {

                    this.addApDrEntry(apCheck.getApDrNextLine(), "PAYABLE", EJBCommon.TRUE, payableAmount, EJBCommon.FALSE, payableAccount, apCheck, null, AD_CMPNY);
                }

                // create cash distribution record

                this.addApDrEntry(apCheck.getApDrNextLine(), "CASH", EJBCommon.FALSE, apCheck.getChkAmount(), EJBCommon.FALSE, apCheck.getAdBankAccount().getBaCoaGlCashAccount(), apCheck, null, AD_CMPNY);
            }

        } catch (GlobalDocumentNumberNotUniqueException | GlobalTransactionAlreadyPostedException |
                 GlobalTransactionAlreadyLockedException | GlobalRecordInvalidException |
                 ApVOUOverapplicationNotAllowedException | GlobalJournalNotBalanceException |
                 GlobalNoRecordFoundException | ApCHKVoucherHasNoWTaxCodeException |
                 ApCHKCheckNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApCheckImportControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private void addApDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSD, Integer COA_CODE, LocalApCheck apCheck, LocalApAppliedVoucher apAppliedVoucher, Integer AD_CMPNY) {

        Debug.print("ApCheckImportControllerBean addApDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(COA_CODE);

            // create distribution record

            LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.create(DR_LN, DR_CLSS, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_DBT, EJBCommon.FALSE, DR_RVRSD, AD_CMPNY);

            apCheck.addApDistributionRecord(apDistributionRecord);
            glChartOfAccount.addApDistributionRecord(apDistributionRecord);

            // to be used by gl journal interface for cross currency receipts
            if (apAppliedVoucher != null) {

                apAppliedVoucher.addApDistributionRecord(apDistributionRecord);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalApAppliedVoucher addApAvEntry(ApModAppliedVoucherDetails mdetails, LocalApCheck apCheck, LocalApCheckBatch apCheckBatch, Integer AD_CMPNY) throws ApVOUOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, ApCHKVoucherHasNoWTaxCodeException {

        Debug.print("ApCheckImportControllerBean addApAvEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            // get functional currency name

            String FC_NM = adCompany.getGlFunctionalCurrency().getFcName();

            // validate overapplication

            LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = apVoucherPaymentScheduleHome.findByPrimaryKey(mdetails.getAvVpsCode());

            // validate if vps already locked

            if (apVoucherPaymentSchedule.getVpsLock() == EJBCommon.TRUE) {

                Collection apChecks = apCheckBatch.getApChecks();

                Iterator i = apChecks.iterator();

                boolean found = false;
                while (i.hasNext()) {

                    LocalApCheck existingApCheck = (LocalApCheck) i.next();

                    Collection apAppliedVouchers = existingApCheck.getApAppliedVouchers();

                    for (Object appliedVoucher : apAppliedVouchers) {

                        LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) appliedVoucher;

                        if (apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDocumentNumber().equals(mdetails.getAvVpsVouDocumentNumber())) {
                            found = true;
                            break;
                        }
                    }
                }

                if (!found) {

                    throw new GlobalTransactionAlreadyLockedException(apVoucherPaymentSchedule.getApVoucher().getVouDocumentNumber() + "-" + apVoucherPaymentSchedule.getVpsNumber());
                }
            }

            Collection list = apVoucherPaymentSchedule.getApAppliedVouchers();

            Iterator i = list.iterator();
            double APPLY_AMNT = 0d;
            double TX_WTHHLD = 0d;
            double DSCNT_AMNT = 0d;

            while (i.hasNext()) {

                LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) i.next();

                if (apAppliedVoucher.getApCheck().getChkVoidPosted() == EJBCommon.FALSE) {

                    APPLY_AMNT = APPLY_AMNT + apAppliedVoucher.getAvApplyAmount();
                    TX_WTHHLD = TX_WTHHLD + apAppliedVoucher.getAvTaxWithheld();
                    DSCNT_AMNT = DSCNT_AMNT + apAppliedVoucher.getAvDiscountAmount();
                }
            }

            APPLY_AMNT = APPLY_AMNT + mdetails.getAvApplyAmount();
            TX_WTHHLD = TX_WTHHLD + mdetails.getAvTaxWithheld();
            DSCNT_AMNT = DSCNT_AMNT + mdetails.getAvDiscountAmount();

            if (EJBCommon.roundIt(apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid(), this.getGlFcPrecisionUnit(AD_CMPNY)) < EJBCommon.roundIt(APPLY_AMNT + TX_WTHHLD + DSCNT_AMNT, this.getGlFcPrecisionUnit(AD_CMPNY))) {

                throw new ApVOUOverapplicationNotAllowedException(mdetails.getAvVpsChkDocumentNumber() + " for Voucher:" + apVoucherPaymentSchedule.getApVoucher().getVouDocumentNumber());
            }

            // validate voucher wtax code if necessary

            if (mdetails.getAvTaxWithheld() > 0 && apVoucherPaymentSchedule.getApVoucher().getApWithholdingTaxCode().getGlChartOfAccount() == null) {

                throw new ApCHKVoucherHasNoWTaxCodeException(apVoucherPaymentSchedule.getApVoucher().getVouDocumentNumber() + "-" + apVoucherPaymentSchedule.getVpsNumber());
            }

            double AV_FRX_GN_LSS = 0d;

            if (!FC_NM.equals(apVoucherPaymentSchedule.getApVoucher().getGlFunctionalCurrency().getFcName()) || !FC_NM.equals(apCheck.getGlFunctionalCurrency().getFcName())) {

                double AV_ALLCTD_PYMNT_AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), mdetails.getAvAllocatedPaymentAmount(), AD_CMPNY);

                double AV_APPLY_AMNT = this.convertForeignToFunctionalCurrency(apVoucherPaymentSchedule.getApVoucher().getGlFunctionalCurrency().getFcCode(), apVoucherPaymentSchedule.getApVoucher().getGlFunctionalCurrency().getFcName(), apVoucherPaymentSchedule.getApVoucher().getVouConversionDate(), apVoucherPaymentSchedule.getApVoucher().getVouConversionRate(), mdetails.getAvApplyAmount(), AD_CMPNY);

                double AV_TX_WTHHLD = this.convertForeignToFunctionalCurrency(apVoucherPaymentSchedule.getApVoucher().getGlFunctionalCurrency().getFcCode(), apVoucherPaymentSchedule.getApVoucher().getGlFunctionalCurrency().getFcName(), apVoucherPaymentSchedule.getApVoucher().getVouConversionDate(), apVoucherPaymentSchedule.getApVoucher().getVouConversionRate(), mdetails.getAvTaxWithheld(), AD_CMPNY);

                double AV_DSCNT_AMNT = this.convertForeignToFunctionalCurrency(apVoucherPaymentSchedule.getApVoucher().getGlFunctionalCurrency().getFcCode(), apVoucherPaymentSchedule.getApVoucher().getGlFunctionalCurrency().getFcName(), apVoucherPaymentSchedule.getApVoucher().getVouConversionDate(), apVoucherPaymentSchedule.getApVoucher().getVouConversionRate(), mdetails.getAvDiscountAmount(), AD_CMPNY);

                AV_FRX_GN_LSS = EJBCommon.roundIt((AV_ALLCTD_PYMNT_AMNT + AV_TX_WTHHLD + AV_DSCNT_AMNT) - (AV_APPLY_AMNT + AV_TX_WTHHLD + AV_DSCNT_AMNT), this.getGlFcPrecisionUnit(AD_CMPNY));
            }

            // create applied voucher

            LocalApAppliedVoucher apAppliedVoucher = apAppliedVoucherHome.create(mdetails.getAvApplyAmount(), mdetails.getAvTaxWithheld(), mdetails.getAvDiscountAmount(), mdetails.getAvAllocatedPaymentAmount(), AV_FRX_GN_LSS, AD_CMPNY);

            apCheck.addApAppliedVoucher(apAppliedVoucher);
            apVoucherPaymentSchedule.addApAppliedVoucher(apAppliedVoucher);

            // lock voucher

            apVoucherPaymentSchedule.setVpsLock(EJBCommon.TRUE);

            return apAppliedVoucher;

        } catch (ApVOUOverapplicationNotAllowedException | ApCHKVoucherHasNoWTaxCodeException |
                 GlobalTransactionAlreadyLockedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("ApCheckImportControllerBean convertForeignToFunctionalCurrency");

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

    private ApModCheckDetails validate(ApModCheckDetails mDetails) throws GlobalRecordInvalidException, GlobalJournalNotBalanceException {

        Debug.print("ApCheckImportControllerBean validate");

        try {

            String bank = null;
            String checkNumber = null;
            double TOTAL_AMOUNT = 0d;

            ArrayList list = mDetails.getChkAvList();

            for (Object o : list) {

                ApModAppliedVoucherDetails details = (ApModAppliedVoucherDetails) o;

                if (bank == null) {

                    bank = details.getAvVpsChkBankAccount();

                } else if (!details.getAvVpsChkBankAccount().equals(bank)) {

                    throw new GlobalRecordInvalidException(details.getAvVpsChkDocumentNumber() + ":Bank Account " + bank + " - " + details.getAvVpsChkBankAccount());
                }

                if (checkNumber == null) {

                    checkNumber = details.getAvVpsChkCheckNumber();

                } else if (!details.getAvVpsChkCheckNumber().equals(checkNumber)) {

                    throw new GlobalRecordInvalidException(details.getAvVpsChkDocumentNumber() + ":Check Number " + checkNumber + " - " + details.getAvVpsChkCheckNumber());
                }

                TOTAL_AMOUNT = TOTAL_AMOUNT + details.getAvApplyAmount();
            }

            if (TOTAL_AMOUNT != mDetails.getChkAmount()) {

                throw new GlobalJournalNotBalanceException(mDetails.getChkDocumentNumber());
            }

            mDetails.setChkNumber(checkNumber);
            mDetails.setChkBaName(bank);

            return mDetails;

        } catch (GlobalRecordInvalidException | GlobalJournalNotBalanceException ex) {

            throw ex;

        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApCheckImportControllerBean ejbCreate");
    }
}