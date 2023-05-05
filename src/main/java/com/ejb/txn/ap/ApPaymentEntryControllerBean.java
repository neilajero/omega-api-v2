/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApPaymentEntryControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ap.*;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.dao.inv.LocalInvItemLocationHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureConversionHome;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.*;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ap.ApCHKCheckNumberNotUniqueException;
import com.ejb.exception.ap.ApCHKVoucherHasNoWTaxCodeException;
import com.ejb.exception.ap.ApVOUOverapplicationNotAllowedException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ap.ApCheckDetails;
import com.util.mod.ap.ApModAppliedVoucherDetails;
import com.util.mod.ap.ApModCheckDetails;
import com.util.mod.ap.ApModSupplierDetails;
import com.util.mod.ap.ApModVoucherPaymentScheduleDetails;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Stateless(name = "ApPaymentEntryControllerEJB")
public class ApPaymentEntryControllerBean extends EJBContextClass implements ApPaymentEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    ApApprovalController apApprovalController;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
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
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdBranchBankAccountHome adBranchBankAccountHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalAdDiscountHome adDiscountHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalApAppliedVoucherHome apAppliedVoucherHome;
    @EJB
    private LocalApCheckBatchHome apCheckBatchHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApPurchaseOrderHome apPurchaseOrderHome;
    @EJB
    private LocalApSupplierBalanceHome apSupplierBalanceHome;
    @EJB
    private LocalApVoucherPaymentScheduleHome apVoucherPaymentScheduleHome;
    @EJB
    private LocalGlForexLedgerHome glForexLedgerHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;


    public ArrayList getGlFcAllWithDefault(Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean getGlFcAllWithDefault");

        Collection glFunctionalCurrencies = null;

        LocalGlFunctionalCurrency glFunctionalCurrency = null;
        LocalAdCompany adCompany = null;

        ArrayList list = new ArrayList();

        try {

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAllEnabled(EJBCommon.getGcCurrentDateWoTime().getTime(), companyCode);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glFunctionalCurrencies.isEmpty()) {

            return null;
        }

        for (Object functionalCurrency : glFunctionalCurrencies) {

            glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;

            GlModFunctionalCurrencyDetails mdetails = new GlModFunctionalCurrencyDetails(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), adCompany.getGlFunctionalCurrency().getFcName().equals(glFunctionalCurrency.getFcName()) ? EJBCommon.TRUE : EJBCommon.FALSE);

            list.add(mdetails);
        }

        return list;
    }

    public ArrayList getAdBaAll(Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean getAdBaAll");

        ArrayList list = new ArrayList();

        try {

            Collection adBankAccounts = adBankAccountHome.findEnabledBaAll(AD_BRNCH, companyCode);

            for (Object bankAccount : adBankAccounts) {

                LocalAdBankAccount adBankAccount = (LocalAdBankAccount) bankAccount;

                list.add(adBankAccount.getBaName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModCheckDetails getApChkByChkCode(Integer CHK_CODE, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApPaymentEntryControllerBean getApChkByChkCode");

        try {

            LocalApCheck apCheck = null;

            try {
                Debug.print("CHK_CODE=" + CHK_CODE);

                apCheck = apCheckHome.findByPrimaryKey(CHK_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList chkAvList = new ArrayList();

            // get applied vouchers

            Collection apAppliedVouchers = apCheck.getApAppliedVouchers();

            for (Object appliedVoucher : apAppliedVouchers) {

                LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) appliedVoucher;

                ApModAppliedVoucherDetails mdetails = new ApModAppliedVoucherDetails();

                mdetails.setAvApplyAmount(apAppliedVoucher.getAvApplyAmount());
                mdetails.setAvTaxWithheld(apAppliedVoucher.getAvTaxWithheld());
                mdetails.setAvDiscountAmount(apAppliedVoucher.getAvDiscountAmount());
                mdetails.setAvAllocatedPaymentAmount(apAppliedVoucher.getAvAllocatedPaymentAmount());
                mdetails.setAvVpsCode(apAppliedVoucher.getApVoucherPaymentSchedule().getVpsCode());
                mdetails.setAvVpsVouCode(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouCode());
                mdetails.setAvVpsNumber(apAppliedVoucher.getApVoucherPaymentSchedule().getVpsNumber());
                mdetails.setAvVpsDueDate(apAppliedVoucher.getApVoucherPaymentSchedule().getVpsDueDate());
                mdetails.setAvVpsAmountDue(EJBCommon.roundIt(apAppliedVoucher.getApVoucherPaymentSchedule().getVpsAmountDue() - apAppliedVoucher.getApVoucherPaymentSchedule().getVpsAmountPaid(), this.getGlFcPrecisionUnit(companyCode)));
                mdetails.setAvVpsVouDocumentNumber(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDocumentNumber());
                mdetails.setAvVpsVouReferenceNumber(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouReferenceNumber());
                mdetails.setAvVpsVouDescription(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDescription());
                mdetails.setAvVpsVouFcName(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getGlFunctionalCurrency().getFcName());

                mdetails.setAvVpsConversionDate(EJBCommon.convertSQLDateToString(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouConversionDate()));
                mdetails.setAvVpsConversionRate(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouConversionRate());
                chkAvList.add(mdetails);
            }

            ApModCheckDetails mChkDetails = new ApModCheckDetails();

            mChkDetails.setChkCode(apCheck.getChkCode());
            mChkDetails.setChkDate(apCheck.getChkDate());
            mChkDetails.setChkCheckDate(apCheck.getChkCheckDate());
            mChkDetails.setChkNumber(apCheck.getChkNumber());
            mChkDetails.setChkDocumentNumber(apCheck.getChkDocumentNumber());
            mChkDetails.setChkReferenceNumber(apCheck.getChkReferenceNumber());
            mChkDetails.setChkAmount(apCheck.getChkAmount());
            mChkDetails.setChkVoid(apCheck.getChkVoid());
            mChkDetails.setChkDescription(apCheck.getChkDescription());
            mChkDetails.setChkConversionDate(apCheck.getChkConversionDate());
            mChkDetails.setChkConversionRate(apCheck.getChkConversionRate());
            mChkDetails.setChkApprovalStatus(apCheck.getChkApprovalStatus());
            mChkDetails.setChkReasonForRejection(apCheck.getChkReasonForRejection());
            mChkDetails.setChkPosted(apCheck.getChkPosted());
            mChkDetails.setChkVoid(apCheck.getChkVoid());
            mChkDetails.setChkCrossCheck(apCheck.getChkCrossCheck());
            mChkDetails.setChkVoidApprovalStatus(apCheck.getChkVoidApprovalStatus());
            mChkDetails.setChkVoidPosted(apCheck.getChkVoidPosted());
            mChkDetails.setChkCreatedBy(apCheck.getChkCreatedBy());
            mChkDetails.setChkDateCreated(apCheck.getChkDateCreated());
            mChkDetails.setChkLastModifiedBy(apCheck.getChkLastModifiedBy());
            mChkDetails.setChkDateLastModified(apCheck.getChkDateLastModified());
            mChkDetails.setChkApprovedRejectedBy(apCheck.getChkApprovedRejectedBy());
            mChkDetails.setChkDateApprovedRejected(apCheck.getChkDateApprovedRejected());
            mChkDetails.setChkPostedBy(apCheck.getChkPostedBy());
            mChkDetails.setChkDatePosted(apCheck.getChkDatePosted());
            mChkDetails.setChkFcName(apCheck.getGlFunctionalCurrency().getFcName());
            mChkDetails.setChkSplSupplierCode(apCheck.getApSupplier().getSplSupplierCode());
            mChkDetails.setChkBaName(apCheck.getAdBankAccount().getBaName());
            mChkDetails.setChkCbName(apCheck.getApCheckBatch() != null ? apCheck.getApCheckBatch().getCbName() : null);
            mChkDetails.setChkAvList(chkAvList);
            mChkDetails.setChkSplName(apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName());
            mChkDetails.setChkMemo(apCheck.getChkMemo());

            return mChkDetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModSupplierDetails getApSplBySplSupplierCode(String SPL_SPPLR_CODE, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApPaymentEntryControllerBean getApSplBySplSupplierCode");

        try {

            LocalApSupplier apSupplier = null;

            try {

                apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, companyCode);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ApModSupplierDetails mdetails = new ApModSupplierDetails();

            mdetails.setSplStBaName(apSupplier.getAdBankAccount().getBaName());
            mdetails.setSplName(apSupplier.getSplName());

            return mdetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApVpsBySplSupplierCode(String SPL_SPPLR_CODE, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApPaymentEntryControllerBean getApVpsBySplSupplierCode");

        ArrayList list = new ArrayList();

        try {

            Collection apVoucherPaymentSchedules = apVoucherPaymentScheduleHome.findOpenVpsByVpsLockAndSplSupplierCode(EJBCommon.FALSE, SPL_SPPLR_CODE, AD_BRNCH, companyCode);

            if (apVoucherPaymentSchedules.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode);

            for (Object voucherPaymentSchedule : apVoucherPaymentSchedules) {

                LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) voucherPaymentSchedule;

                Debug.print("DOC NUMBER: " + apVoucherPaymentSchedule.getApVoucher().getVouDocumentNumber());
                // verification if vps is already closed
                if (EJBCommon.roundIt(apVoucherPaymentSchedule.getVpsAmountDue(), precisionUnit) == EJBCommon.roundIt(apVoucherPaymentSchedule.getVpsAmountPaid(), precisionUnit)) {
                    continue;
                }

                Debug.print("DOC NUMBER PASSED: " + apVoucherPaymentSchedule.getApVoucher().getVouDocumentNumber());

                ApModVoucherPaymentScheduleDetails mdetails = new ApModVoucherPaymentScheduleDetails();

                mdetails.setVpsCode(apVoucherPaymentSchedule.getVpsCode());
                mdetails.setVpsNumber(apVoucherPaymentSchedule.getVpsNumber());
                mdetails.setVpsDueDate(apVoucherPaymentSchedule.getVpsDueDate());
                mdetails.setVpsAmountDue(EJBCommon.roundIt(apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid(), this.getGlFcPrecisionUnit(companyCode)));
                mdetails.setVpsVouCode(apVoucherPaymentSchedule.getApVoucher().getVouCode());
                mdetails.setVpsVouDocumentNumber(apVoucherPaymentSchedule.getApVoucher().getVouDocumentNumber());
                mdetails.setVpsVouReferenceNumber(apVoucherPaymentSchedule.getApVoucher().getVouReferenceNumber());
                mdetails.setVpsVouDescription(apVoucherPaymentSchedule.getApVoucher().getVouDescription());
                mdetails.setVpsVouFcName(apVoucherPaymentSchedule.getApVoucher().getGlFunctionalCurrency().getFcName());
                mdetails.setVpsVouConversionDate(apVoucherPaymentSchedule.getApVoucher().getVouConversionDate());
                mdetails.setVpsVouConversionRate(apVoucherPaymentSchedule.getApVoucher().getVouConversionRate());

                // calculate default discount
                short VOUCHER_AGE = (short) ((new Date().getTime() - apVoucherPaymentSchedule.getApVoucher().getVouDate().getTime()) / (1000 * 60 * 60 * 24));

                double VPS_DSCNT_AMNT = 0d;

                Collection adDiscounts = adDiscountHome.findByPsLineNumberAndPytName(apVoucherPaymentSchedule.getVpsNumber(), apVoucherPaymentSchedule.getApVoucher().getAdPaymentTerm().getPytName(), companyCode);

                for (Object discount : adDiscounts) {

                    LocalAdDiscount adDiscount = (LocalAdDiscount) discount;

                    if (adDiscount.getDscPaidWithinDay() >= VOUCHER_AGE) {

                        VPS_DSCNT_AMNT = EJBCommon.roundIt(mdetails.getVpsAmountDue() * adDiscount.getDscDiscountPercent() / 100, this.getGlFcPrecisionUnit(companyCode));

                        break;
                    }
                }

                // calculate default tax withheld

                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

                double APPLY_AMOUNT = mdetails.getVpsAmountDue() - VPS_DSCNT_AMNT;
                double W_TAX_AMOUNT = 0d;

                if (apVoucherPaymentSchedule.getApVoucher().getApWithholdingTaxCode().getWtcRate() != 0 && adPreference.getPrfApWTaxRealization().equals("PAYMENT")) {

                    LocalApTaxCode apTaxCode = null;

                    if (apVoucherPaymentSchedule.getApVoucher().getApPurchaseOrderLines().isEmpty()) {
                        apTaxCode = apVoucherPaymentSchedule.getApVoucher().getApTaxCode();
                    } else {

                        Collection apPurchaseOrderLines = apVoucherPaymentSchedule.getApVoucher().getApPurchaseOrderLines();
                        ArrayList apPoList = new ArrayList(apPurchaseOrderLines);
                        apTaxCode = ((LocalApPurchaseOrderLine) apPoList.get(0)).getApPurchaseOrder().getApTaxCode();
                    }

                    double NET_AMOUNT = 0d;

                    if (apTaxCode.getTcType().equals("INCLUSIVE") || apTaxCode.getTcType().equals("EXCLUSIVE")) {

                        NET_AMOUNT = EJBCommon.roundIt(APPLY_AMOUNT / (1 + (apTaxCode.getTcRate() / 100)), this.getGlFcPrecisionUnit(companyCode));

                    } else {

                        NET_AMOUNT = APPLY_AMOUNT;
                    }

                    W_TAX_AMOUNT = EJBCommon.roundIt(NET_AMOUNT * (apVoucherPaymentSchedule.getApVoucher().getApWithholdingTaxCode().getWtcRate() / 100), this.getGlFcPrecisionUnit(companyCode));

                    APPLY_AMOUNT -= W_TAX_AMOUNT;
                }

                mdetails.setVpsAvApplyAmount(APPLY_AMOUNT);
                mdetails.setVpsAvTaxWithheld(W_TAX_AMOUNT);
                mdetails.setVpsAvDiscountAmount(VPS_DSCNT_AMNT);
                System.out.println("DOC NUMBER ADDED: " + apVoucherPaymentSchedule.getApVoucher().getVouDocumentNumber());
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

    public Integer saveApChkEntry(ApCheckDetails details, String BA_NM, String FC_NM, String SPL_SPPLR_CODE, String CB_NM, ArrayList avList, boolean isDraft, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, ApCHKCheckNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, ApVOUOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, ApCHKVoucherHasNoWTaxCodeException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ApPaymentEntryControllerBean saveApChkEntry");

        LocalApCheck apCheck = null;

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // validate if check is already deleted

            try {

                if (details.getChkCode() != null) {

                    apCheck = apCheckHome.findByPrimaryKey(details.getChkCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if check is already posted, void, approved or pending

            if (details.getChkCode() != null && details.getChkVoid() == EJBCommon.FALSE) {

                if (apCheck.getChkApprovalStatus() != null) {

                    if (apCheck.getChkApprovalStatus().equals("APPROVED") || apCheck.getChkApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (apCheck.getChkApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (apCheck.getChkPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (apCheck.getChkVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // check void

            if (details.getChkCode() != null && details.getChkVoid() == EJBCommon.TRUE) {

                if (apCheck.getChkVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }

                if (apCheck.getChkPosted() == EJBCommon.TRUE) {

                    // generate approval status

                    String CHK_APPRVL_STATUS = null;

                    if (!isDraft) {

                        LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                        // check if ap check approval is enabled

                        if (adApproval.getAprEnableApCheck() == EJBCommon.FALSE) {

                            CHK_APPRVL_STATUS = "N/A";

                        } else {

                            // check if check is self approved

                            LocalAdUser adUser = adUserHome.findByUsrName(details.getChkLastModifiedBy(), companyCode);

                            CHK_APPRVL_STATUS = apApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getChkLastModifiedBy(), adUser.getUsrDescription(), "AP CHECK", apCheck.getChkCode(), apCheck.getChkDocumentNumber(), apCheck.getChkDate(), apCheck.getChkAmount(), AD_BRNCH, companyCode);
                        }
                    }

                    // reverse distribution records

                    Collection apDistributionRecords = apCheck.getApDistributionRecords();
                    ArrayList list = new ArrayList();

                    Iterator i = apDistributionRecords.iterator();

                    while (i.hasNext()) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                        list.add(apDistributionRecord);
                    }

                    i = list.iterator();

                    while (i.hasNext()) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                        this.addApDrEntry(apCheck.getApDrNextLine(), apDistributionRecord.getDrClass(), apDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, apDistributionRecord.getDrAmount(), EJBCommon.TRUE, apDistributionRecord.getGlChartOfAccount().getCoaCode(), apCheck, apDistributionRecord.getApAppliedVoucher(), AD_BRNCH, companyCode);
                    }

                    if (CHK_APPRVL_STATUS != null && CHK_APPRVL_STATUS.equals("N/A") && adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                        apCheck.setChkVoid(EJBCommon.TRUE);
                        this.executeApChkPost(apCheck.getChkCode(), details.getChkLastModifiedBy(), AD_BRNCH, companyCode);
                    }

                    // set void approval status

                    apCheck.setChkVoidApprovalStatus(CHK_APPRVL_STATUS);

                } else {

                    // release voucher lock

                    Collection apLockedAppliedVouchers = apCheck.getApAppliedVouchers();

                    for (Object apLockedAppliedVoucher : apLockedAppliedVouchers) {

                        LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) apLockedAppliedVoucher;

                        apAppliedVoucher.getApVoucherPaymentSchedule().setVpsLock(EJBCommon.FALSE);
                    }
                }

                apCheck.setChkVoid(EJBCommon.TRUE);
                apCheck.setChkLastModifiedBy(details.getChkLastModifiedBy());
                apCheck.setChkDateLastModified(details.getChkDateLastModified());

                return apCheck.getChkCode();
            }

            // validate if document number is unique document number is automatic then set
            // next sequence

            try {

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                if (details.getChkCode() == null) {

                    try {

                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AP CHECK", companyCode);

                    } catch (FinderException ex) {
                    }

                    try {

                        adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

                    } catch (FinderException ex) {
                    }

                    LocalApCheck apExistingCheck = null;

                    try {

                        apExistingCheck = apCheckHome.findByChkDocumentNumberAndBrCode(details.getChkDocumentNumber(), AD_BRNCH, companyCode);

                    } catch (FinderException ex) {
                    }

                    if (apExistingCheck != null) {

                        throw new GlobalDocumentNumberNotUniqueException();
                    }

                    if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getChkDocumentNumber() == null || details.getChkDocumentNumber().trim().length() == 0)) {

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    apCheckHome.findByChkDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, companyCode);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                } catch (FinderException ex) {

                                    details.setChkDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;
                                }

                            } else {

                                try {

                                    apCheckHome.findByChkDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, companyCode);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                } catch (FinderException ex) {

                                    details.setChkDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;
                                }
                            }
                        }
                    }

                } else {

                    LocalApCheck apExistingCheck = null;

                    try {

                        apExistingCheck = apCheckHome.findByChkDocumentNumberAndBrCode(details.getChkDocumentNumber(), AD_BRNCH, companyCode);

                    } catch (FinderException ex) {
                    }

                    if (apExistingCheck != null && !apExistingCheck.getChkCode().equals(details.getChkCode())) {

                        throw new GlobalDocumentNumberNotUniqueException();
                    }

                    if (apCheck.getChkDocumentNumber() != details.getChkDocumentNumber() && (details.getChkDocumentNumber() == null || details.getChkDocumentNumber().trim().length() == 0)) {

                        details.setChkDocumentNumber(apCheck.getChkDocumentNumber());
                    }
                }

            } catch (GlobalDocumentNumberNotUniqueException ex) {

                getSessionContext().setRollbackOnly();
                throw ex;

            } catch (Exception ex) {

                Debug.printStackTrace(ex);
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }

            // validate if check number is unique check is automatic then set next sequence

            if (details.getChkCode() == null) {

                LocalApCheck apExistingCheck = null;

                try {

                    apExistingCheck = apCheckHome.findByChkNumberAndBaName(details.getChkNumber(), BA_NM, companyCode);

                } catch (FinderException ex) {
                }

                if (apExistingCheck != null) {

                    throw new ApCHKCheckNumberNotUniqueException();
                }

                LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);

                if (details.getChkNumber() == null || details.getChkNumber().trim().length() == 0) {

                    while (true) {

                        try {

                            apCheckHome.findByChkNumberAndBaName(adBankAccount.getBaNextCheckNumber(), BA_NM, companyCode);
                            adBankAccount.setBaNextCheckNumber(EJBCommon.incrementStringNumber(adBankAccount.getBaNextCheckNumber()));

                        } catch (FinderException ex) {

                            details.setChkNumber(adBankAccount.getBaNextCheckNumber());
                            adBankAccount.setBaNextCheckNumber(EJBCommon.incrementStringNumber(adBankAccount.getBaNextCheckNumber()));
                            break;
                        }
                    }
                }

            } else {

                LocalApCheck apExistingCheck = null;

                try {

                    apExistingCheck = apCheckHome.findByChkNumberAndBaName(details.getChkNumber(), BA_NM, companyCode);

                } catch (FinderException ex) {
                }

                if (apExistingCheck != null && !apExistingCheck.getChkCode().equals(details.getChkCode())) {

                    throw new ApCHKCheckNumberNotUniqueException();
                }

                if (apCheck.getChkNumber() != details.getChkNumber() && (details.getChkNumber() == null || details.getChkNumber().trim().length() == 0)) {

                    details.setChkNumber(apCheck.getChkNumber());
                }
            }

            // validate if conversion date exists

            try {

                if (details.getChkConversionDate() != null) {

                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getChkConversionDate(), companyCode);

                    details.setChkConversionRate(glFunctionalCurrencyRate.getFrXToUsd());

                } else {
                    if (details.getChkConversionRate() == 0) {
                        details.setChkConversionRate(1d);
                    }
                }

            } catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // used in checking if check should re-generate distribution records and
            // re-calculate taxes
            boolean isRecalculate = true;

            // create check

            if (details.getChkCode() == null) {

                apCheck = apCheckHome.create("PAYMENT", details.getChkDescription(), details.getChkDate(), details.getChkCheckDate(), details.getChkNumber(), details.getChkDocumentNumber(), details.getChkReferenceNumber(), null, null, null, null, null, EJBCommon.FALSE, EJBCommon.FALSE, null, null, null, 0d, 0d, 0d, 0d, 0d, details.getChkLoan(), details.getChkLoanGenerated(), details.getChkConversionDate(), details.getChkConversionRate(), 0d, details.getChkAmount(), null, null, EJBCommon.FALSE, EJBCommon.FALSE, details.getChkCrossCheck(), null, EJBCommon.FALSE, details.getChkCreatedBy(), details.getChkDateCreated(), details.getChkLastModifiedBy(), details.getChkDateLastModified(), null, null, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, details.getChkMemo(), null, AD_BRNCH, companyCode);

            } else {

                // check if critical fields are changed

                if (!apCheck.getAdBankAccount().getBaName().equals(BA_NM) || !apCheck.getApSupplier().getSplSupplierCode().equals(SPL_SPPLR_CODE) || apCheck.getChkConversionRate() != details.getChkConversionRate() || avList.size() != apCheck.getApAppliedVouchers().size()) {

                    isRecalculate = true;

                } else if (avList.size() == apCheck.getApAppliedVouchers().size()) {

                    Iterator avIter = apCheck.getApAppliedVouchers().iterator();
                    Iterator avListIter = avList.iterator();

                    while (avIter.hasNext()) {

                        LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) avIter.next();
                        ApModAppliedVoucherDetails mdetails = (ApModAppliedVoucherDetails) avListIter.next();

                        if (!apAppliedVoucher.getApVoucherPaymentSchedule().getVpsCode().equals(mdetails.getAvVpsCode()) || apAppliedVoucher.getAvApplyAmount() != mdetails.getAvApplyAmount() || apAppliedVoucher.getAvTaxWithheld() != mdetails.getAvTaxWithheld() || apAppliedVoucher.getAvDiscountAmount() != mdetails.getAvDiscountAmount() || apAppliedVoucher.getAvAllocatedPaymentAmount() != mdetails.getAvAllocatedPaymentAmount()) {

                            isRecalculate = true;
                            break;
                        }

                        isRecalculate = false;
                    }

                } else {

                    isRecalculate = false;
                }

                apCheck.setChkDescription(details.getChkDescription());
                apCheck.setChkDate(details.getChkDate());
                apCheck.setChkCheckDate(details.getChkCheckDate());
                apCheck.setChkNumber(details.getChkNumber());
                apCheck.setChkDocumentNumber(details.getChkDocumentNumber());
                apCheck.setChkReferenceNumber(details.getChkReferenceNumber());
                apCheck.setChkConversionDate(details.getChkConversionDate());
                apCheck.setChkConversionRate(details.getChkConversionRate());
                apCheck.setChkAmount(details.getChkAmount());
                apCheck.setChkCrossCheck(details.getChkCrossCheck());
                apCheck.setChkLastModifiedBy(details.getChkLastModifiedBy());
                apCheck.setChkDateLastModified(details.getChkDateLastModified());
                apCheck.setChkReasonForRejection(null);
                apCheck.setChkMemo(details.getChkMemo());
                apCheck.setChkSupplierName(null);

                apCheck.setChkLoan(details.getChkLoan());
                apCheck.setChkLoanGenerated(details.getChkLoanGenerated());
            }

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);
            apCheck.setAdBankAccount(adBankAccount);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
            apCheck.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, companyCode);
            apCheck.setApSupplier(apSupplier);

            if (details.getChkSupplierName().length() > 0 && !apSupplier.getSplName().equals(details.getChkSupplierName())) {
                apCheck.setChkSupplierName(details.getChkSupplierName());
            }

            try {

                LocalApCheckBatch apCheckBatch = apCheckBatchHome.findByPaymentCbName(CB_NM, AD_BRNCH, companyCode);
                apCheck.setApCheckBatch(apCheckBatch);

            } catch (FinderException ex) {

            }

            if (isRecalculate) {

                // remove all distribution records

                Collection apDistributionRecords = apCheck.getApDistributionRecords();

                Iterator i = apDistributionRecords.iterator();

                while (i.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                    i.remove();

                    // apDistributionRecord.entityRemove();
                    em.remove(apDistributionRecord);
                }

                // release vps locks and remove all applied vouchers

                Collection apAppliedVouchers = apCheck.getApAppliedVouchers();

                i = apAppliedVouchers.iterator();

                while (i.hasNext()) {

                    LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) i.next();

                    apAppliedVoucher.getApVoucherPaymentSchedule().setVpsLock(EJBCommon.FALSE);

                    i.remove();

                    // apAppliedVoucher.entityRemove();
                    em.remove(apAppliedVoucher);
                }

                // add new applied vouchers and distribution record

                double totalApplyAmount = 0d;
                double AV_FRX_GN_LSS = 0d;

                double VOU_CONVERSION_RT = 0d;
                double CHK_CONVERSION_RT = 0d;
                LocalApDistributionRecord apDistributionRecord = null;

                i = avList.iterator();

                while (i.hasNext()) {

                    ApModAppliedVoucherDetails mAvDetails = (ApModAppliedVoucherDetails) i.next();
                    totalApplyAmount += mAvDetails.getAvApplyAmount();

                    LocalApAppliedVoucher apAppliedVoucher = this.addApAvEntry(mAvDetails, apCheck, companyCode);

                    VOU_CONVERSION_RT = apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouConversionRate();
                    CHK_CONVERSION_RT = apAppliedVoucher.getApCheck().getChkConversionRate();

                    AV_FRX_GN_LSS += apAppliedVoucher.getAvForexGainLoss();

                    // get payable account
                    apDistributionRecord = apDistributionRecordHome.findByDrClassAndVouCode("PAYABLE", apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouCode(), companyCode);
                }

                // add cash distribution

                LocalAdBranchBankAccount adBranchBankAccount = null;

                try {
                    adBranchBankAccount = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(apCheck.getAdBankAccount().getBaCode(), AD_BRNCH, companyCode);

                } catch (FinderException ex) {

                }

                // create cash distribution record

                double APPLY_AMOUNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), totalApplyAmount, companyCode);

                double PAYABLE_AMOUNT = this.convertForeignToFunctionalCurrency(apDistributionRecord.getApVoucher().getGlFunctionalCurrency().getFcCode(), apDistributionRecord.getApVoucher().getGlFunctionalCurrency().getFcName(), apDistributionRecord.getApVoucher().getVouConversionDate(), apDistributionRecord.getApVoucher().getVouConversionRate(), totalApplyAmount, companyCode);

                if (CHK_CONVERSION_RT > VOU_CONVERSION_RT) {

                    this.addApDrEntry(apCheck.getApDrNextLine(), "FOREX GAIN", EJBCommon.FALSE, Math.abs(AV_FRX_GN_LSS), EJBCommon.FALSE, adPreference.getPrfMiscPosGiftCertificateAccount(), apCheck, null, AD_BRNCH, companyCode);
                }

                if (VOU_CONVERSION_RT > CHK_CONVERSION_RT) {

                    this.addApDrEntry(apCheck.getApDrNextLine(), "FOREX LOSS", EJBCommon.TRUE, Math.abs(AV_FRX_GN_LSS), EJBCommon.FALSE, adPreference.getPrfMiscPosGiftCertificateAccount(), apCheck, null, AD_BRNCH, companyCode);
                }

                this.addApDrEntry(apCheck.getApDrNextLine(), "PAYABLE", EJBCommon.TRUE, PAYABLE_AMOUNT, EJBCommon.FALSE, apDistributionRecord.getGlChartOfAccount().getCoaCode(), apCheck, null, AD_BRNCH, companyCode);

                this.addApDrEntry(apCheck.getApDrNextLine(), "CASH", EJBCommon.FALSE, APPLY_AMOUNT, EJBCommon.FALSE, adBranchBankAccount.getBbaGlCoaCashAccount(), apCheck, null, AD_BRNCH, companyCode);
            }

            // generate approval status

            String CHK_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if ap check approval is enabled

                if (adApproval.getAprEnableApCheck() == EJBCommon.FALSE) {

                    CHK_APPRVL_STATUS = "N/A";

                } else {

                    // check if check is self approved

                    LocalAdUser adUser = adUserHome.findByUsrName(details.getChkLastModifiedBy(), companyCode);

                    CHK_APPRVL_STATUS = apApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getChkLastModifiedBy(), adUser.getUsrDescription(), "AP CHECK", apCheck.getChkCode(), apCheck.getChkDocumentNumber(), apCheck.getChkDate(), apCheck.getChkAmount(), AD_BRNCH, companyCode);
                }
            }

            if (CHK_APPRVL_STATUS != null && CHK_APPRVL_STATUS.equals("N/A") && adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeApChkPost(apCheck.getChkCode(), apCheck.getChkLastModifiedBy(), AD_BRNCH, companyCode);
            }

            // set check approval status

            apCheck.setChkApprovalStatus(CHK_APPRVL_STATUS);

            return apCheck.getChkCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalBranchAccountNumberInvalidException |
                 GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
                 GlJREffectiveDateNoPeriodExistException | GlobalNoApprovalApproverFoundException |
                 GlobalNoApprovalRequesterFoundException | GlobalTransactionAlreadyLockedException |
                 ApCHKVoucherHasNoWTaxCodeException | ApVOUOverapplicationNotAllowedException |
                 GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
                 GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
                 GlobalConversionDateNotExistException | ApCHKCheckNumberNotUniqueException |
                 GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteApChkEntry(Integer CHK_CODE, String AD_USR, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("ApPaymentEntryControllerBean deleteApChkEntry");

        try {

            LocalApCheck apCheck = apCheckHome.findByPrimaryKey(CHK_CODE);

            if (apCheck.getChkApprovalStatus() != null && apCheck.getChkApprovalStatus().equals("PENDING")) {

                Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AP CHECK", apCheck.getChkCode(), companyCode);

                for (Object approvalQueue : adApprovalQueues) {

                    LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                    // adApprovalQueue.entityRemove();
                    em.remove(adApprovalQueue);
                }
            }

            // release lock and remove applied voucher

            Collection apAppliedVouchers = apCheck.getApAppliedVouchers();

            Iterator i = apAppliedVouchers.iterator();

            while (i.hasNext()) {

                LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) i.next();

                apAppliedVoucher.getApVoucherPaymentSchedule().setVpsLock(EJBCommon.FALSE);

                i.remove();

                // apAppliedVoucher.entityRemove();
                em.remove(apAppliedVoucher);
            }

            adDeleteAuditTrailHome.create("AP CHECK", apCheck.getChkDate(), apCheck.getChkDocumentNumber(), apCheck.getChkReferenceNumber(), apCheck.getChkAmount(), AD_USR, new Date(), companyCode);

            // apCheck.entityRemove();
            em.remove(apCheck);

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByChkCode(Integer CHK_CODE, Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean getAdApprovalNotifiedUsersByChkCode");

        ArrayList list = new ArrayList();

        try {

            LocalApCheck apCheck = apCheckHome.findByPrimaryKey(CHK_CODE);

            if (apCheck.getChkVoid() == EJBCommon.FALSE && apCheck.getChkPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;

            } else if (apCheck.getChkVoid() == EJBCommon.TRUE && apCheck.getChkVoidPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AP CHECK", CHK_CODE, companyCode);

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

    public byte getAdPrfEnableApCheckBatch(Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean getAdPrfEnableApCheckBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfEnableApCheckBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApOpenCbAll(Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean getApOpenCbAll");

        ArrayList list = new ArrayList();

        try {

            Collection apCheckBatches = apCheckBatchHome.findOpenCbByCbType("PAYMENT", AD_BRNCH, companyCode);

            for (Object checkBatch : apCheckBatches) {

                LocalApCheckBatch apCheckBatch = (LocalApCheckBatch) checkBatch;

                list.add(apCheckBatch.getCbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfApUseSupplierPulldown(Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean getAdPrfApUseSupplierPulldown");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfApUseSupplierPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvPurchaseRequisitionMisc1(Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean getAdLvPurchaseRequisitionMisc1");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC1", companyCode);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }
            Debug.print("MISC1=" + list.size());

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvPurchaseRequisitionMisc2(Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean getAdLvPurchaseRequisitionMisc2");


        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC2", companyCode);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }
            Debug.print("PR T7=" + list.size());

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvPurchaseRequisitionMisc3(Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean getAdLvPurchaseRequisitionMisc3");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC3", companyCode);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }
            Debug.print("PR T9=" + list.size());

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvPurchaseRequisitionMisc4(Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean getAdLvPurchaseRequisitionMisc4");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC4", companyCode);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }
            Debug.print("PR T9=" + list.size());

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvPurchaseRequisitionMisc5(Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean getAdLvPurchaseRequisitionMisc5");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC5", companyCode);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }
            Debug.print("PR T9=" + list.size());

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvPurchaseRequisitionMisc6(Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean getAdLvPurchaseRequisitionMisc6");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC6", companyCode);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }
            Debug.print("PR T9=" + list.size());

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getAdPrfApDefaultCheckDate(Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean getAdPrfApDefaultCheckDate");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfApDefaultCheckDate();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("GlJournalEntryControllerBean getFrRateByFrNameAndFrDate");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);

            double CONVERSION_RATE = 1;

            // Get functional currency rate

            if (!FC_NM.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), CONVERSION_DATE, companyCode);

                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, companyCode);

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

    // private methods

    private LocalApAppliedVoucher addApAvEntry(ApModAppliedVoucherDetails mdetails, LocalApCheck apCheck, Integer companyCode) throws ApVOUOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, ApCHKVoucherHasNoWTaxCodeException {

        Debug.print("ApPaymentEntryControllerBean addApDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // get functional currency name

            String FC_NM = adCompany.getGlFunctionalCurrency().getFcName();

            // validate overapplication
            Debug.print("vpscode : " + mdetails.getAvVpsCode());
            LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = apVoucherPaymentScheduleHome.findByPrimaryKey(mdetails.getAvVpsCode());

            if (EJBCommon.roundIt(apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid(), this.getGlFcPrecisionUnit(companyCode)) < EJBCommon.roundIt(mdetails.getAvApplyAmount() + mdetails.getAvTaxWithheld() + mdetails.getAvDiscountAmount(), this.getGlFcPrecisionUnit(companyCode))) {

                throw new ApVOUOverapplicationNotAllowedException(apVoucherPaymentSchedule.getApVoucher().getVouDocumentNumber() + "-" + apVoucherPaymentSchedule.getVpsNumber());
            }

            // validate if vps already locked

            if (apVoucherPaymentSchedule.getVpsLock() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyLockedException(apVoucherPaymentSchedule.getApVoucher().getVouDocumentNumber() + "-" + apVoucherPaymentSchedule.getVpsNumber());
            }

            // validate voucher wtax code if necessary

            if (mdetails.getAvTaxWithheld() > 0 && apVoucherPaymentSchedule.getApVoucher().getApWithholdingTaxCode().getGlChartOfAccount() == null) {

                throw new ApCHKVoucherHasNoWTaxCodeException(apVoucherPaymentSchedule.getApVoucher().getVouDocumentNumber() + "-" + apVoucherPaymentSchedule.getVpsNumber());
            }

            double AV_FRX_GN_LSS = 0d;

            Debug.print("VOUCHER CONVERSION RATE=" + apVoucherPaymentSchedule.getApVoucher().getVouConversionRate());
            Debug.print("PAYMENT CONVERSION RATE=" + apCheck.getChkConversionRate());

            if (apVoucherPaymentSchedule.getApVoucher().getVouConversionRate() != apCheck.getChkConversionRate()) {

                double VOUCHER_AMOUNT = EJBCommon.roundIt(apVoucherPaymentSchedule.getVpsAmountDue() / apVoucherPaymentSchedule.getApVoucher().getVouConversionRate(), this.getGlFcPrecisionUnit(companyCode));
                double EXPECTED_CHK_AMOUNT = EJBCommon.roundIt(mdetails.getAvApplyAmount() / apVoucherPaymentSchedule.getApVoucher().getVouConversionRate(), this.getGlFcPrecisionUnit(companyCode));
                double ACTUAL_CHK_AMOUNT = EJBCommon.roundIt(mdetails.getAvApplyAmount() / apCheck.getChkConversionRate(), this.getGlFcPrecisionUnit(companyCode));

                Debug.print("VOUCHER_AMOUNT=" + VOUCHER_AMOUNT);
                Debug.print("EXPECTED_CHK_AMOUNT=" + EXPECTED_CHK_AMOUNT);
                Debug.print("ACTUAL_CHK_AMOUNT=" + ACTUAL_CHK_AMOUNT);

                AV_FRX_GN_LSS = EXPECTED_CHK_AMOUNT - ACTUAL_CHK_AMOUNT;
            }

            // create applied voucher

            LocalApAppliedVoucher apAppliedVoucher = apAppliedVoucherHome.create(mdetails.getAvApplyAmount(), 0d, 0d, 0d, AV_FRX_GN_LSS, companyCode);

            apAppliedVoucher.setApCheck(apCheck);
            apAppliedVoucher.setApVoucherPaymentSchedule(apVoucherPaymentSchedule);

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

    private void addApDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSD, Integer COA_CODE, LocalApCheck apCheck, LocalApAppliedVoucher apAppliedVoucher, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ApPaymentEntryControllerBean addApDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, companyCode);

                if (glChartOfAccount.getCoaEnable() == EJBCommon.FALSE) {
                    throw new GlobalBranchAccountNumberInvalidException();
                }

            } catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.create(DR_LN, DR_CLSS, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_DBT, EJBCommon.FALSE, DR_RVRSD, companyCode);

            apDistributionRecord.setApCheck(apCheck);

            apDistributionRecord.setGlChartOfAccount(glChartOfAccount);

            // to be used by gl journal interface for cross currency receipts
            if (apAppliedVoucher != null) {

                apDistributionRecord.setApAppliedVoucher(apAppliedVoucher);
            }

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpCostPrecisionUnit(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getInvGpCostPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvCostPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double getInvFifoCost(Date CST_DT, Integer IL_CODE, double CST_QTY, double CST_COST, boolean isAdjustFifo, Integer AD_BRNCH, Integer companyCode) {


        try {

            Collection invFifoCostings = invCostingHome.findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(CST_DT, IL_CODE, AD_BRNCH, companyCode);

            if (invFifoCostings.size() > 0) {

                Iterator x = invFifoCostings.iterator();

                if (isAdjustFifo) {

                    // executed during POST transaction

                    double totalCost = 0d;
                    double cost;

                    if (CST_QTY < 0) {

                        // for negative quantities
                        double neededQty = -(CST_QTY);

                        while (x.hasNext() && neededQty != 0) {

                            LocalInvCosting invFifoCosting = (LocalInvCosting) x.next();

                            if (invFifoCosting.getApPurchaseOrderLine() != null || invFifoCosting.getApVoucherLineItem() != null) {
                                cost = invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived();
                            } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                                cost = invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold();
                            } else {
                                cost = invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity();
                            }

                            if (neededQty <= invFifoCosting.getCstRemainingLifoQuantity()) {

                                invFifoCosting.setCstRemainingLifoQuantity(invFifoCosting.getCstRemainingLifoQuantity() - neededQty);
                                totalCost += (neededQty * cost);
                                neededQty = 0d;
                            } else {

                                neededQty -= invFifoCosting.getCstRemainingLifoQuantity();
                                totalCost += (invFifoCosting.getCstRemainingLifoQuantity() * cost);
                                invFifoCosting.setCstRemainingLifoQuantity(0);
                            }
                        }

                        // if needed qty is not yet satisfied but no more quantities to fetch, get the
                        // default
                        // cost
                        if (neededQty != 0) {

                            LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(IL_CODE);
                            totalCost += (neededQty * invItemLocation.getInvItem().getIiUnitCost());
                        }

                        cost = totalCost / -CST_QTY;
                    } else {

                        // for positive quantities
                        cost = CST_COST;
                    }
                    return cost;
                } else {

                    // executed during ENTRY transaction

                    LocalInvCosting invFifoCosting = (LocalInvCosting) x.next();

                    if (invFifoCosting.getApPurchaseOrderLine() != null || invFifoCosting.getApVoucherLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived(), this.getInvGpCostPrecisionUnit(companyCode));
                    } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold(), this.getInvGpCostPrecisionUnit(companyCode));
                    } else {
                        return EJBCommon.roundIt(invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity(), this.getInvGpCostPrecisionUnit(companyCode));
                    }
                }
            } else {

                // most applicable in 1st entries of data
                LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(IL_CODE);
                return invItemLocation.getInvItem().getIiUnitCost();
            }

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT / CONVERSION_RATE;
        }

        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private void executeApChkPost(Integer CHK_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("ApPaymentEntryControllerBean executeApChkPost");

        LocalApCheck apCheck = null;

        try {

            // validate if check is already deleted

            try {

                apCheck = apCheckHome.findByPrimaryKey(CHK_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if check is already posted

            if (apCheck.getChkVoid() == EJBCommon.FALSE && apCheck.getChkPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

                // validate if check void is already posted

            } else if (apCheck.getChkVoid() == EJBCommon.TRUE && apCheck.getChkVoidPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidPostedException();
            }

            // post check

            if (apCheck.getChkVoid() == EJBCommon.FALSE && apCheck.getChkPosted() == EJBCommon.FALSE) {

                double CHK_AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), companyCode);

                double AV_FRX_GN_LSS = 0d;

                if (apCheck.getChkType().equals("PAYMENT")) {

                    // increase amount paid in voucher payment schedules and voucher

                    Collection apAppliedVouchers = apAppliedVoucherHome.findByChkCode(apCheck.getChkCode(), companyCode);

                    for (Object appliedVoucher : apAppliedVouchers) {

                        LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) appliedVoucher;

                        AV_FRX_GN_LSS += apAppliedVoucher.getAvForexGainLoss();
                        LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = apAppliedVoucher.getApVoucherPaymentSchedule();

                        double AMOUNT_PAID = apAppliedVoucher.getAvApplyAmount() + apAppliedVoucher.getAvDiscountAmount() + apAppliedVoucher.getAvTaxWithheld();

                        apVoucherPaymentSchedule.setVpsAmountPaid(EJBCommon.roundIt(apVoucherPaymentSchedule.getVpsAmountPaid() + AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        apVoucherPaymentSchedule.getApVoucher().setVouAmountPaid(EJBCommon.roundIt(apVoucherPaymentSchedule.getApVoucher().getVouAmountPaid() + AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        // release voucher lock

                        apVoucherPaymentSchedule.setVpsLock(EJBCommon.FALSE);
                    }

                    // decrease supplier balance

                }

                // decrease bank balance

                LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(apCheck.getAdBankAccount().getBaCode());

                try {

                    // find bankaccount balance before or equal check date

                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(apCheck.getChkCheckDate(), apCheck.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    if (!adBankAccountBalances.isEmpty()) {

                        // get last check

                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(apCheck.getChkCheckDate())) {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(apCheck.getChkCheckDate(), adBankAccountBalance.getBabBalance() - CHK_AMNT - AV_FRX_GN_LSS, "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - CHK_AMNT - AV_FRX_GN_LSS);
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(apCheck.getChkCheckDate(), (0 - CHK_AMNT - AV_FRX_GN_LSS), "BOOK", companyCode);

                        adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(apCheck.getChkCheckDate(), apCheck.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - CHK_AMNT - AV_FRX_GN_LSS);
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

                // set check post status

                apCheck.setChkPosted(EJBCommon.TRUE);
                apCheck.setChkPostedBy(USR_NM);
                apCheck.setChkDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

                // set Relese Check Upon Posting if Bank Account is CASH

                if (apCheck.getAdBankAccount().getBaIsCashAccount() == EJBCommon.TRUE) {

                    apCheck.setChkReleased(EJBCommon.TRUE);
                    apCheck.setChkDateReleased(apCheck.getChkCheckDate());
                }

            } else if (apCheck.getChkVoid() == EJBCommon.TRUE && apCheck.getChkVoidPosted() == EJBCommon.FALSE) {

                // VOIDING CHECK

                double CHK_AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), companyCode);
                double AV_FRX_GN_LSS = 0d;
                if (apCheck.getChkType().equals("PAYMENT")) {

                    // decrease amount paid in voucher payment schedules and voucher

                    Collection apAppliedVouchers = apCheck.getApAppliedVouchers();

                    for (Object appliedVoucher : apAppliedVouchers) {

                        LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) appliedVoucher;

                        LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = apAppliedVoucher.getApVoucherPaymentSchedule();
                        AV_FRX_GN_LSS += apAppliedVoucher.getAvForexGainLoss();
                        double AMOUNT_PAID = apAppliedVoucher.getAvApplyAmount() + apAppliedVoucher.getAvDiscountAmount() + apAppliedVoucher.getAvTaxWithheld();

                        apVoucherPaymentSchedule.setVpsAmountPaid(EJBCommon.roundIt(apVoucherPaymentSchedule.getVpsAmountPaid() - AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));
                        apVoucherPaymentSchedule.getApVoucher().setVouAmountPaid(EJBCommon.roundIt(apVoucherPaymentSchedule.getApVoucher().getVouAmountPaid() - AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));
                    }

                    // increase supplier balance

                }

                // increase bank balance

                LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(apCheck.getAdBankAccount().getBaCode());

                try {

                    // find bankaccount balance before or equal check date

                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(apCheck.getChkCheckDate(), apCheck.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    if (!adBankAccountBalances.isEmpty()) {

                        // get last check

                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(apCheck.getChkCheckDate())) {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(apCheck.getChkCheckDate(), adBankAccountBalance.getBabBalance() + CHK_AMNT - AV_FRX_GN_LSS, "BOOK", companyCode);

                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + CHK_AMNT - AV_FRX_GN_LSS);
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(apCheck.getChkCheckDate(), CHK_AMNT - AV_FRX_GN_LSS, "BOOK", companyCode);

                        adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(apCheck.getChkCheckDate(), apCheck.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + CHK_AMNT - AV_FRX_GN_LSS);
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

                // set check post status

                apCheck.setChkVoidPosted(EJBCommon.TRUE);
                apCheck.setChkPostedBy(USR_NM);
                apCheck.setChkDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
            }

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            if (adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(apCheck.getChkDate(), companyCode);

                } catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), apCheck.getChkDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if check is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection apDistributionRecords = null;

                if (apCheck.getChkVoid() == EJBCommon.FALSE) {

                    apDistributionRecords = apDistributionRecordHome.findImportableDrByDrReversedAndChkCode(EJBCommon.FALSE, apCheck.getChkCode(), companyCode);

                } else {

                    apDistributionRecords = apDistributionRecordHome.findImportableDrByDrReversedAndChkCode(EJBCommon.TRUE, apCheck.getChkCode(), companyCode);
                }

                Iterator j = apDistributionRecords.iterator();
                boolean firstFlag = true;

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                    double DR_AMNT = apDistributionRecord.getDrAmount();

                    if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                        TOTAL_DEBIT += DR_AMNT;

                    } else {

                        TOTAL_CREDIT += DR_AMNT;
                    }
                }

                TOTAL_DEBIT = EJBCommon.roundIt(TOTAL_DEBIT, adCompany.getGlFunctionalCurrency().getFcPrecision());
                TOTAL_CREDIT = EJBCommon.roundIt(TOTAL_CREDIT, adCompany.getGlFunctionalCurrency().getFcPrecision());

                if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.TRUE && TOTAL_DEBIT != TOTAL_CREDIT) {

                    LocalGlSuspenseAccount glSuspenseAccount = null;

                    try {

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS PAYABLES", "CHECKS", companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (apDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (apDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                    }

                    LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                    glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

                } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                    throw new GlobalJournalNotBalanceException();
                }

                // create journal batch if necessary

                LocalGlJournalBatch glJournalBatch = null;
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

                try {

                    if (adPreference.getPrfEnableApCheckBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " " + apCheck.getApCheckBatch().getCbName(), AD_BRNCH, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " CHECKS", AD_BRNCH, companyCode);
                    }

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                    if (adPreference.getPrfEnableApCheckBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " " + apCheck.getApCheckBatch().getCbName(), "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " CHECKS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);
                    }
                }

                // create journal entry
                String supplierName = apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName();

                LocalGlJournal glJournal = glJournalHome.create(apCheck.getChkReferenceNumber(), apCheck.getChkDescription(), apCheck.getChkDate(), 0.0d, null, apCheck.getChkDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), apCheck.getApSupplier().getSplTin(), supplierName, EJBCommon.FALSE, null, AD_BRNCH, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS PAYABLES", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("CHECKS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = apDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                    double DR_AMNT = apDistributionRecord.getDrAmount();

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(apDistributionRecord.getDrLine(), apDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    glJournalLine.setGlChartOfAccount(apDistributionRecord.getGlChartOfAccount());
                    glJournalLine.setGlJournal(glJournal);

                    apDistributionRecord.setDrImported(EJBCommon.TRUE);
                }

                // post journal to gl

                Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
                for (Object journalLine : glJournalLines) {

                    LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                    // post current to current acv

                    this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                    // post to subsequent acvs (propagate)

                    Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                    for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                        this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                    }

                    // post to subsequent years if necessary

                    Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                    if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                        adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                        LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), companyCode);

                        for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                            LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                            String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                            // post to subsequent acvs of subsequent set of book(propagate)

                            Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                            for (Object accountingCalendarValue : glAccountingCalendarValues) {

                                LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                                if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                                } else { // revenue & expense

                                    this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                                }
                            }

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                                break;
                            }
                        }
                    }
                }
            }

        } catch (GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyVoidPostedException |
                 GlobalTransactionAlreadyPostedException | GlobalRecordAlreadyDeletedException |
                 GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void post(Date CHK_DT, double CHK_AMNT, LocalApSupplier apSupplier, Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean post");

        try {

            // find supplier balance before or equal voucher date

            Collection apSupplierBalances = apSupplierBalanceHome.findByBeforeOrEqualVouDateAndSplSupplierCode(CHK_DT, apSupplier.getSplSupplierCode(), companyCode);

            if (!apSupplierBalances.isEmpty()) {

                // get last voucher

                ArrayList apSupplierBalanceList = new ArrayList(apSupplierBalances);

                LocalApSupplierBalance apSupplierBalance = (LocalApSupplierBalance) apSupplierBalanceList.get(apSupplierBalanceList.size() - 1);

                if (apSupplierBalance.getSbDate().before(CHK_DT)) {

                    // create new balance

                    LocalApSupplierBalance apNewSupplierBalance = apSupplierBalanceHome.create(CHK_DT, apSupplierBalance.getSbBalance() + CHK_AMNT, companyCode);

                    apNewSupplierBalance.setApSupplier(apSupplier);

                } else { // equals to voucher date

                    apSupplierBalance.setSbBalance(apSupplierBalance.getSbBalance() + CHK_AMNT);
                }

            } else {

                // create new balance

                LocalApSupplierBalance apNewSupplierBalance = apSupplierBalanceHome.create(CHK_DT, CHK_AMNT, companyCode);

                apNewSupplierBalance.setApSupplier(apSupplier);
            }

            // propagate to subsequent balances if necessary

            apSupplierBalances = apSupplierBalanceHome.findByAfterVouDateAndSplSupplierCode(CHK_DT, apSupplier.getSplSupplierCode(), companyCode);

            for (Object supplierBalance : apSupplierBalances) {

                LocalApSupplierBalance apSupplierBalance = (LocalApSupplierBalance) supplierBalance;

                apSupplierBalance.setSbBalance(apSupplierBalance.getSbBalance() + CHK_AMNT);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean postToGl");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode);

            String ACCOUNT_TYPE = glChartOfAccount.getCoaAccountType();
            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

            if (((ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("EXPENSE")) && isDebit == EJBCommon.TRUE) || (!ACCOUNT_TYPE.equals("ASSET") && !ACCOUNT_TYPE.equals("EXPENSE") && isDebit == EJBCommon.FALSE)) {

                glChartOfAccountBalance.setCoabEndingBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() + JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() + JL_AMNT, FC_EXTNDD_PRCSN));
                }

            } else {

                glChartOfAccountBalance.setCoabEndingBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() - JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() - JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }

            if (isCurrentAcv) {

                if (isDebit == EJBCommon.TRUE) {

                    glChartOfAccountBalance.setCoabTotalDebit(EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalDebit() + JL_AMNT, FC_EXTNDD_PRCSN));

                } else {

                    glChartOfAccountBalance.setCoabTotalCredit(EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalCredit() + JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInv(LocalApVoucherLineItem apVoucherLineItem, Date CST_DT, double CST_QTY_RCVD, double CST_ITM_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean postToInv");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = apVoucherLineItem.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_RCVD = EJBCommon.roundIt(CST_QTY_RCVD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ITM_CST = EJBCommon.roundIt(CST_ITM_CST, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            // create costing

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, CST_QTY_RCVD, CST_ITM_CST, 0d, 0d, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, AD_BRNCH, companyCode);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setApVoucherLineItem(apVoucherLineItem);

            invCosting.setCstQuantity(CST_QTY_RCVD);
            invCosting.setCstCost(CST_ITM_CST);
            // propagate balance if necessary

            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

            for (Object costing : invCostings) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_QTY_RCVD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ITM_CST);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double QTY_RCVD, Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean convertByUomFromAndItemAndQuantity");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(QTY_RCVD * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApPaymentEntryControllerBean ejbCreate");
    }

}