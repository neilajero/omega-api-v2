/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApDebitMemoEntryControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ap.*;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.*;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.ap.ApVOUOverapplicationNotAllowedException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.ap.ApVoucherDetails;
import com.util.mod.ap.ApModDistributionRecordDetails;
import com.util.mod.ap.ApModVoucherDetails;
import com.util.mod.ap.ApModVoucherLineItemDetails;
import com.util.mod.inv.InvModUnitOfMeasureDetails;

import jakarta.ejb.*;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Stateless(name = "ApDebitMemoEntryControllerEJB")
public class ApDebitMemoEntryControllerBean extends EJBContextClass implements ApDebitMemoEntryController {

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
    private LocalApVoucherBatchHome apVoucherBatchHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalAdBranchSupplierHome adBranchSupplierHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApVoucherLineItemHome apVoucherLineItemHome;
    @EJB
    private LocalApVoucherPaymentScheduleHome apVoucherPaymentScheduleHome;
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
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;


    public ArrayList getInvUomByIiName(String II_NM, Integer companyCode) {

        Debug.print("ApDebitMemoEntryControllerBean getInvUomByIiName");

        ArrayList list = new ArrayList();

        try {

            LocalInvItem invItem = null;
            LocalInvUnitOfMeasure invItemUnitOfMeasure = null;

            invItem = invItemHome.findByIiName(II_NM, companyCode);
            invItemUnitOfMeasure = invItem.getInvUnitOfMeasure();

            Collection invUnitOfMeasures = null;
            for (Object o : invUnitOfMeasureHome.findByUomAdLvClass(invItemUnitOfMeasure.getUomAdLvClass(), companyCode)) {

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

    public double getInvIiUnitCostByIiNameAndUomName(String VOU_DM_VCHR_NMBR, String II_NM, String LOC_NM, String UOM_NM, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApVoucherEntryControllerBean getInvIiUnitCostByIiNameAndUomName");

        try {

            LocalApVoucher apVoucher = null;
            LocalInvItem invItem = null;
            double COST = 0d;

            try {

                apVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(VOU_DM_VCHR_NMBR, EJBCommon.FALSE, branchCode, companyCode);

                if (apVoucher.getVouPosted() != EJBCommon.TRUE) {

                    throw new GlobalNoRecordFoundException();
                }

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            try {

                invItem = invItemHome.findByIiName(II_NM, companyCode);
                LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndRemainingQuantityNotEqualToZeroAndIiNameAndLocName(II_NM, LOC_NM, branchCode, companyCode);

                COST = this.convertFunctionalToForeignCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity()), companyCode);

            } catch (FinderException ex) {

                COST = invItem.getIiUnitCost();
            }

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(invItem.getIiUnitCost() * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(companyCode));

        } catch (GlobalNoRecordFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfApJournalLineNumber(Integer companyCode) {

        Debug.print("ApDebitMemoEntryControllerBean getAdPrfApJournalLineNumber");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfApJournalLineNumber();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModVoucherDetails getApVouByVouCode(Integer VOU_CODE, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApDebitMemoEntryControllerBean getApVouByVouCode");

        try {

            LocalApVoucher apDebitMemo = null;

            try {

                apDebitMemo = apVoucherHome.findByPrimaryKey(VOU_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get voucher line items if any

            Collection apVoucherLineItems = apDebitMemo.getApVoucherLineItems();

            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

            if (!apVoucherLineItems.isEmpty()) {

                for (Object voucherLineItem : apVoucherLineItems) {

                    LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) voucherLineItem;

                    ApModVoucherLineItemDetails vliDetails = new ApModVoucherLineItemDetails();

                    vliDetails.setVliCode(apVoucherLineItem.getVliCode());
                    vliDetails.setVliLine(apVoucherLineItem.getVliLine());
                    vliDetails.setVliQuantity(apVoucherLineItem.getVliQuantity());
                    vliDetails.setVliUnitCost(apVoucherLineItem.getVliUnitCost());
                    vliDetails.setVliIiName(apVoucherLineItem.getInvItemLocation().getInvItem().getIiName());
                    vliDetails.setVliLocName(apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName());
                    vliDetails.setVliUomName(apVoucherLineItem.getInvUnitOfMeasure().getUomName());
                    vliDetails.setVliIiDescription(apVoucherLineItem.getInvItemLocation().getInvItem().getIiDescription());

                    list.add(vliDetails);
                }

            } else {

                // get distribution records

                Collection apDistributionRecords = apDistributionRecordHome.findByVouCode(apDebitMemo.getVouCode(), companyCode);

                short lineNumber = 1;

                Iterator i = apDistributionRecords.iterator();

                TOTAL_DEBIT = 0d;
                TOTAL_CREDIT = 0d;

                while (i.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                    ApModDistributionRecordDetails mdetails = new ApModDistributionRecordDetails();

                    mdetails.setDrCode(apDistributionRecord.getDrCode());
                    mdetails.setDrLine(lineNumber);
                    mdetails.setDrClass(apDistributionRecord.getDrClass());
                    mdetails.setDrDebit(apDistributionRecord.getDrDebit());
                    mdetails.setDrAmount(apDistributionRecord.getDrAmount());
                    mdetails.setDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                    mdetails.setDrCoaAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                    if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                        TOTAL_DEBIT += apDistributionRecord.getDrAmount();

                    } else {

                        TOTAL_CREDIT += apDistributionRecord.getDrAmount();
                    }

                    list.add(mdetails);

                    lineNumber++;
                }
            }

            ApModVoucherDetails mVouDetails = new ApModVoucherDetails();

            mVouDetails.setVouCode(apDebitMemo.getVouCode());
            mVouDetails.setVouDescription(apDebitMemo.getVouDescription());
            mVouDetails.setVouDate(apDebitMemo.getVouDate());
            mVouDetails.setVouDocumentNumber(apDebitMemo.getVouDocumentNumber());
            mVouDetails.setVouDmVoucherNumber(apDebitMemo.getVouDmVoucherNumber());
            mVouDetails.setVouBillAmount(apDebitMemo.getVouBillAmount());
            mVouDetails.setVouTotalDebit(TOTAL_DEBIT);
            mVouDetails.setVouTotalCredit(TOTAL_CREDIT);
            mVouDetails.setVouApprovalStatus(apDebitMemo.getVouApprovalStatus());
            mVouDetails.setVouReasonForRejection(apDebitMemo.getVouReasonForRejection());
            mVouDetails.setVouPosted(apDebitMemo.getVouPosted());
            mVouDetails.setVouVoid(apDebitMemo.getVouVoid());
            mVouDetails.setVouCreatedBy(apDebitMemo.getVouCreatedBy());
            mVouDetails.setVouDateCreated(apDebitMemo.getVouDateCreated());
            mVouDetails.setVouLastModifiedBy(apDebitMemo.getVouLastModifiedBy());
            mVouDetails.setVouDateLastModified(apDebitMemo.getVouDateLastModified());
            mVouDetails.setVouApprovedRejectedBy(apDebitMemo.getVouApprovedRejectedBy());
            mVouDetails.setVouDateApprovedRejected(apDebitMemo.getVouDateApprovedRejected());
            mVouDetails.setVouPostedBy(apDebitMemo.getVouPostedBy());
            mVouDetails.setVouDatePosted(apDebitMemo.getVouDatePosted());
            mVouDetails.setVouSplSupplierCode(apDebitMemo.getApSupplier().getSplSupplierCode());
            mVouDetails.setVouVbName(apDebitMemo.getApVoucherBatch() != null ? apDebitMemo.getApVoucherBatch().getVbName() : null);
            mVouDetails.setVouSplName(apDebitMemo.getApSupplier().getSplName());

            if (!apVoucherLineItems.isEmpty()) {

                mVouDetails.setVouVliList(list);
                mVouDetails.setVouAmountDue(apDebitMemo.getVouAmountDue());

            } else {

                mVouDetails.setVouDrList(list);
            }

            return mVouDetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApDrByVouDmVoucherNumberAndVouBillAmountAndSplSupplierCodeBrCode(String VOU_DM_VCHR_NMBR, double VOU_BLL_AMNT, String SPL_SPPLR_CODE, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException, ApVOUOverapplicationNotAllowedException {

        Debug.print("ApDebitMemoEntryControllerBean getApDrByVouDmVoucherNumberAndVouBillAmount");

        ArrayList list = new ArrayList();

        try {

            LocalApVoucher apVoucher = null;

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode);

            // validate if voucher exist or overapplied

            try {
                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
                apVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndSplSupplierCodeBrCode(VOU_DM_VCHR_NMBR, EJBCommon.FALSE, SPL_SPPLR_CODE, branchCode, companyCode);

                if (apVoucher.getVouPosted() != EJBCommon.TRUE) {

                    throw new GlobalNoRecordFoundException();

                } else if (VOU_BLL_AMNT > EJBCommon.roundIt(apVoucher.getVouAmountDue() - apVoucher.getVouAmountPaid(), precisionUnit)) {
                    if (adPreference.getPrfApDebitMemoOverrideCost() != 1) {
                        throw new ApVOUOverapplicationNotAllowedException();
                    }
                }

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            // get amount percent

            double AMOUNT_PERCENT = VOU_BLL_AMNT / apVoucher.getVouAmountDue();

            Collection apDistributionRecords = apVoucher.getApDistributionRecords();

            // get total debit and credit for rounding difference calculation

            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;
            boolean isRoundingDifferenceCalculated = false;

            Iterator i = apDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                    TOTAL_DEBIT += EJBCommon.roundIt(apDistributionRecord.getDrAmount() * AMOUNT_PERCENT, precisionUnit);

                } else {

                    TOTAL_CREDIT += EJBCommon.roundIt(apDistributionRecord.getDrAmount() * AMOUNT_PERCENT, precisionUnit);
                }
            }

            // get default debit memo lines

            short lineNumber = 1;

            i = apDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                ApModDistributionRecordDetails mdetails = new ApModDistributionRecordDetails();

                mdetails.setDrLine(lineNumber++);
                mdetails.setDrClass(apDistributionRecord.getDrClass());
                mdetails.setDrDebit(apDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE);

                double DR_AMNT = EJBCommon.roundIt(apDistributionRecord.getDrAmount() * AMOUNT_PERCENT, precisionUnit);

                // calculate rounding difference if necessary

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE && TOTAL_DEBIT != TOTAL_CREDIT && !isRoundingDifferenceCalculated) {

                    DR_AMNT = DR_AMNT + TOTAL_CREDIT - TOTAL_DEBIT;

                    isRoundingDifferenceCalculated = true;
                }

                mdetails.setDrAmount(DR_AMNT);
                mdetails.setDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                list.add(mdetails);
            }

            return list;

        } catch (GlobalNoRecordFoundException | ApVOUOverapplicationNotAllowedException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveApVouEntry(ApVoucherDetails details, String SPL_SPPLR_CODE, String VB_NM, ArrayList drList, boolean isDraft, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalNoRecordFoundException, GlobalDocumentNumberNotUniqueException, GlobalBranchAccountNumberInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, ApVOUOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("ApDebitMemoEntryControllerBean saveApVouEntry");

        LocalApVoucher apDebitMemo = null;
        LocalApVoucher apVoucher = null;


        try {

            // validate if debit memo is already deleted

            try {

                if (details.getVouCode() != null) {

                    apDebitMemo = apVoucherHome.findByPrimaryKey(details.getVouCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if debit memo is already posted, void, approved or pending or locked

            if (details.getVouCode() != null) {

                if (apDebitMemo.getVouApprovalStatus() != null) {

                    if (apDebitMemo.getVouApprovalStatus().equals("APPROVED") || apDebitMemo.getVouApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (apDebitMemo.getVouApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (apDebitMemo.getVouPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (apDebitMemo.getVouVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // debit memo void

            if (details.getVouCode() != null && details.getVouVoid() == EJBCommon.TRUE && apDebitMemo.getVouPosted() == EJBCommon.FALSE) {

                // release lock

                LocalApVoucher apLockedVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(apDebitMemo.getVouDmVoucherNumber(), EJBCommon.FALSE, branchCode, companyCode);

                Collection apLockedVoucherPaymentSchedules = apLockedVoucher.getApVoucherPaymentSchedules();

                for (Object apLockedVoucherPaymentSchedule : apLockedVoucherPaymentSchedules) {

                    LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) apLockedVoucherPaymentSchedule;

                    apVoucherPaymentSchedule.setVpsLock(EJBCommon.FALSE);
                }

                apDebitMemo.setVouVoid(EJBCommon.TRUE);
                apDebitMemo.setVouLastModifiedBy(details.getVouLastModifiedBy());
                apDebitMemo.setVouDateLastModified(details.getVouDateLastModified());

                return apDebitMemo.getVouCode();
            }

            // validate if voucher number exists

            try {

                apVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(details.getVouDmVoucherNumber(), EJBCommon.FALSE, branchCode, companyCode);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            // validate if document number is unique document number is automatic then set
            // next sequence

            try {

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                if (details.getVouCode() == null) {

                    try {

                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AP DEBIT MEMO", companyCode);

                    } catch (FinderException ex) {
                    }

                    try {

                        adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);

                    } catch (FinderException ex) {
                    }

                    LocalApVoucher apExistingDebitMemo = null;

                    try {

                        apExistingDebitMemo = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(details.getVouDocumentNumber(), EJBCommon.TRUE, branchCode, companyCode);

                    } catch (FinderException ex) {
                    }

                    if (apExistingDebitMemo != null) {

                        throw new GlobalDocumentNumberNotUniqueException();
                    }

                    if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getVouDocumentNumber() == null || details.getVouDocumentNumber().trim().length() == 0)) {

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), EJBCommon.TRUE, branchCode, companyCode);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                } catch (FinderException ex) {

                                    details.setVouDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;
                                }

                            } else {

                                try {

                                    apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), EJBCommon.TRUE, branchCode, companyCode);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                } catch (FinderException ex) {

                                    details.setVouDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;
                                }
                            }
                        }
                    }

                } else {

                    LocalApVoucher apExistingDebitMemo = null;

                    try {

                        apExistingDebitMemo = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(details.getVouDocumentNumber(), EJBCommon.TRUE, branchCode, companyCode);

                    } catch (FinderException ex) {
                    }

                    if (apExistingDebitMemo != null && !apExistingDebitMemo.getVouCode().equals(details.getVouCode())) {

                        throw new GlobalDocumentNumberNotUniqueException();
                    }

                    if (apDebitMemo.getVouDocumentNumber() != details.getVouDocumentNumber() && (details.getVouDocumentNumber() == null || details.getVouDocumentNumber().trim().length() == 0)) {

                        details.setVouDocumentNumber(apDebitMemo.getVouDocumentNumber());
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

            // validate if voucher entered is already locked by dm or check

            Collection apValidateVoucherPaymentSchedules = apVoucherPaymentScheduleHome.findByVpsLockAndVouCode(EJBCommon.TRUE, apVoucher.getVouCode(), companyCode);

            if (details.getVouCode() == null && !apValidateVoucherPaymentSchedules.isEmpty() || details.getVouCode() != null && !details.getVouDmVoucherNumber().equals(apDebitMemo.getVouDmVoucherNumber()) && !apValidateVoucherPaymentSchedules.isEmpty()) {

                throw new GlobalTransactionAlreadyLockedException();
            }

            // create voucher

            if (details.getVouCode() == null) {

                apDebitMemo = apVoucherHome.create(EJBCommon.TRUE, details.getVouDescription(), details.getVouDate(), details.getVouDocumentNumber(), null, details.getVouDmVoucherNumber(), null, 0d, details.getVouBillAmount(), 0d, 0d, null, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, details.getVouCreatedBy(), details.getVouDateCreated(), details.getVouLastModifiedBy(), details.getVouDateLastModified(), null, null, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, branchCode, companyCode);

            } else {

                // release lock

                LocalApVoucher apLockedVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(apDebitMemo.getVouDmVoucherNumber(), EJBCommon.FALSE, branchCode, companyCode);

                Collection apLockedVoucherPaymentSchedules = apLockedVoucher.getApVoucherPaymentSchedules();

                for (Object apLockedVoucherPaymentSchedule : apLockedVoucherPaymentSchedules) {

                    LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) apLockedVoucherPaymentSchedule;

                    apVoucherPaymentSchedule.setVpsLock(EJBCommon.FALSE);
                }

                apDebitMemo.setVouDescription(details.getVouDescription());
                apDebitMemo.setVouDate(details.getVouDate());
                apDebitMemo.setVouDocumentNumber(details.getVouDocumentNumber());
                apDebitMemo.setVouDmVoucherNumber(details.getVouDmVoucherNumber());
                apDebitMemo.setVouBillAmount(details.getVouBillAmount());
                apDebitMemo.setVouLastModifiedBy(details.getVouLastModifiedBy());
                apDebitMemo.setVouDateLastModified(details.getVouDateLastModified());
                apDebitMemo.setVouReasonForRejection(null);
            }

            LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, companyCode);
            apSupplier.addApVoucher(apDebitMemo);

            try {

                LocalApVoucherBatch apVoucherBatch = apVoucherBatchHome.findByVbName(VB_NM, branchCode, companyCode);
                apVoucherBatch.addApVoucher(apDebitMemo);

            } catch (FinderException ex) {

            }

            // remove all demo memo lines

            Collection apVoucherLineItems = apDebitMemo.getApVoucherLineItems();

            Iterator i = apVoucherLineItems.iterator();

            while (i.hasNext()) {

                LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) i.next();

                i.remove();

                // apVoucherLineItem.entityRemove();
                em.remove(apVoucherLineItem);
            }

            // remove all distribution records

            Collection apDistributionRecords = apDebitMemo.getApDistributionRecords();

            i = apDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                i.remove();

                // apDistributionRecord.entityRemove();
                em.remove(apDistributionRecord);
            }

            // add new distribution records

            i = drList.iterator();

            while (i.hasNext()) {

                ApModDistributionRecordDetails mDrDetails = (ApModDistributionRecordDetails) i.next();
                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
                if (mDrDetails.getDrClass().equals("PAYABLE")) {
                    if (adPreference.getPrfApDebitMemoOverrideCost() != 1) {
                        if (apDebitMemo.getVouBillAmount() > EJBCommon.roundIt(apVoucher.getVouAmountDue() - apVoucher.getVouAmountPaid(), this.getGlFcPrecisionUnit(companyCode))) {

                            throw new ApVOUOverapplicationNotAllowedException();
                        }
                    }
                }

                this.addApDrEntry(mDrDetails, apDebitMemo, branchCode, companyCode);
            }

            // create new voucher payment schedule lock

            Collection apVoucherPaymentSchedules = apVoucher.getApVoucherPaymentSchedules();

            i = apVoucherPaymentSchedules.iterator();

            while (i.hasNext()) {

                LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) i.next();

                apVoucherPaymentSchedule.setVpsLock(EJBCommon.TRUE);
            }

            // generate approval status

            String VOU_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if ap voucher approval is enabled

                if (adApproval.getAprEnableApDebitMemo() == EJBCommon.FALSE) {

                    VOU_APPRVL_STATUS = "N/A";

                } else {

                    // check if voucher is self approved

                    LocalAdUser adUser = adUserHome.findByUsrName(details.getVouLastModifiedBy(), companyCode);

                    VOU_APPRVL_STATUS = apApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getVouLastModifiedBy(), adUser.getUsrDescription(), "AP DEBIT MEMO", apVoucher.getVouCode(), apVoucher.getVouDocumentNumber(), apVoucher.getVouDate(), apVoucher.getVouBillAmount(), branchCode, companyCode);
                }
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (VOU_APPRVL_STATUS != null && VOU_APPRVL_STATUS.equals("N/A") && adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeApDebitMemoPost(apDebitMemo.getVouCode(), apDebitMemo.getVouLastModifiedBy(), branchCode, companyCode);
            }

            // set debit memo approval status

            apDebitMemo.setVouApprovalStatus(VOU_APPRVL_STATUS);

            return apDebitMemo.getVouCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException | GlJREffectiveDateNoPeriodExistException |
                 GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
                 GlobalBranchAccountNumberInvalidException | GlobalTransactionAlreadyLockedException |
                 ApVOUOverapplicationNotAllowedException | GlobalTransactionAlreadyVoidException |
                 GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
                 GlobalTransactionAlreadyApprovedException | GlobalDocumentNumberNotUniqueException |
                 GlobalNoRecordFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveApVouVliEntry(ApVoucherDetails details, String SPL_SPPLR_CODE, String VB_NM, ArrayList vliList, boolean isDraft, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalNoRecordFoundException, GlobalDocumentNumberNotUniqueException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, ApVOUOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApDebitMemoEntryControllerBean saveApVouEntry");

        LocalApVoucher apDebitMemo = null;
        LocalApVoucher apVoucher = null;


        try {

            // validate if debit memo is already deleted

            try {

                if (details.getVouCode() != null) {

                    apDebitMemo = apVoucherHome.findByPrimaryKey(details.getVouCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if debit memo is already posted, void, approved or pending or locked

            if (details.getVouCode() != null) {

                if (apDebitMemo.getVouApprovalStatus() != null) {

                    if (apDebitMemo.getVouApprovalStatus().equals("APPROVED") || apDebitMemo.getVouApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (apDebitMemo.getVouApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (apDebitMemo.getVouPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (apDebitMemo.getVouVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // debit memo void

            if (details.getVouCode() != null && details.getVouVoid() == EJBCommon.TRUE && apDebitMemo.getVouPosted() == EJBCommon.FALSE) {

                // release lock

                LocalApVoucher apLockedVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(apDebitMemo.getVouDmVoucherNumber(), EJBCommon.FALSE, branchCode, companyCode);

                Collection apLockedVoucherPaymentSchedules = apLockedVoucher.getApVoucherPaymentSchedules();

                for (Object apLockedVoucherPaymentSchedule : apLockedVoucherPaymentSchedules) {

                    LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) apLockedVoucherPaymentSchedule;

                    apVoucherPaymentSchedule.setVpsLock(EJBCommon.FALSE);
                }

                apDebitMemo.setVouVoid(EJBCommon.TRUE);
                apDebitMemo.setVouLastModifiedBy(details.getVouLastModifiedBy());
                apDebitMemo.setVouDateLastModified(details.getVouDateLastModified());

                return apDebitMemo.getVouCode();
            }

            // validate if voucher number exists

            try {

                apVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(details.getVouDmVoucherNumber(), EJBCommon.FALSE, branchCode, companyCode);

                if (apVoucher.getVouPosted() != EJBCommon.TRUE) {

                    throw new GlobalNoRecordFoundException();
                }

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            // validate if document number is unique document number is automatic then set
            // next sequence

            try {

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                if (details.getVouCode() == null) {

                    try {

                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AP DEBIT MEMO", companyCode);

                    } catch (FinderException ex) {
                    }

                    try {

                        adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);

                    } catch (FinderException ex) {
                    }

                    LocalApVoucher apExistingDebitMemo = null;

                    try {

                        apExistingDebitMemo = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(details.getVouDocumentNumber(), EJBCommon.TRUE, branchCode, companyCode);

                    } catch (FinderException ex) {
                    }

                    if (apExistingDebitMemo != null) {

                        throw new GlobalDocumentNumberNotUniqueException();
                    }

                    if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getVouDocumentNumber() == null || details.getVouDocumentNumber().trim().length() == 0)) {

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), EJBCommon.TRUE, branchCode, companyCode);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                } catch (FinderException ex) {

                                    details.setVouDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;
                                }

                            } else {

                                try {

                                    apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), EJBCommon.TRUE, branchCode, companyCode);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                } catch (FinderException ex) {

                                    details.setVouDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;
                                }
                            }
                        }
                    }

                } else {

                    LocalApVoucher apExistingDebitMemo = null;

                    try {

                        apExistingDebitMemo = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(details.getVouDocumentNumber(), EJBCommon.TRUE, branchCode, companyCode);

                    } catch (FinderException ex) {
                    }

                    if (apExistingDebitMemo != null && !apExistingDebitMemo.getVouCode().equals(details.getVouCode())) {

                        throw new GlobalDocumentNumberNotUniqueException();
                    }

                    if (apDebitMemo.getVouDocumentNumber() != details.getVouDocumentNumber() && (details.getVouDocumentNumber() == null || details.getVouDocumentNumber().trim().length() == 0)) {

                        details.setVouDocumentNumber(apDebitMemo.getVouDocumentNumber());
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

            // validate if voucher entered is already locked by dm or check

            Collection apValidateVoucherPaymentSchedules = apVoucherPaymentScheduleHome.findByVpsLockAndVouCode(EJBCommon.TRUE, apVoucher.getVouCode(), companyCode);

            if (details.getVouCode() == null && !apValidateVoucherPaymentSchedules.isEmpty() || details.getVouCode() != null && !details.getVouDmVoucherNumber().equals(apDebitMemo.getVouDmVoucherNumber()) && !apValidateVoucherPaymentSchedules.isEmpty()) {

                throw new GlobalTransactionAlreadyLockedException();
            }

            // used in checking if debit memo should re-generate distribution records and
            // re-calculate
            // taxes

            boolean isRecalculate = true;

            // create voucher

            if (details.getVouCode() == null) {

                apDebitMemo = apVoucherHome.create(EJBCommon.TRUE, details.getVouDescription(), details.getVouDate(), details.getVouDocumentNumber(), null, details.getVouDmVoucherNumber(), null, 0d, 0d, 0d, 0d, null, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, details.getVouCreatedBy(), details.getVouDateCreated(), details.getVouLastModifiedBy(), details.getVouDateLastModified(), null, null, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, branchCode, companyCode);

            } else {

                // release lock

                LocalApVoucher apLockedVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(apDebitMemo.getVouDmVoucherNumber(), EJBCommon.FALSE, branchCode, companyCode);

                Collection apLockedVoucherPaymentSchedules = apLockedVoucher.getApVoucherPaymentSchedules();

                for (Object apLockedVoucherPaymentSchedule : apLockedVoucherPaymentSchedules) {

                    LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) apLockedVoucherPaymentSchedule;

                    apVoucherPaymentSchedule.setVpsLock(EJBCommon.FALSE);
                }

                // check if critical fields are changed

                if (!apDebitMemo.getApSupplier().getSplSupplierCode().equals(SPL_SPPLR_CODE) || !apDebitMemo.getVouDmVoucherNumber().equals(details.getVouDmVoucherNumber()) || vliList.size() != apDebitMemo.getApVoucherLineItems().size() || !(apDebitMemo.getVouDate().equals(details.getVouDate()))) {

                    isRecalculate = true;

                } else if (vliList.size() == apDebitMemo.getApVoucherLineItems().size()) {

                    Iterator vliIter = apDebitMemo.getApVoucherLineItems().iterator();
                    Iterator vliListIter = vliList.iterator();

                    while (vliIter.hasNext()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) vliIter.next();
                        ApModVoucherLineItemDetails mdetails = (ApModVoucherLineItemDetails) vliListIter.next();

                        if (!apVoucherLineItem.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getVliIiName()) || !apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getVliLocName()) || !apVoucherLineItem.getInvUnitOfMeasure().getUomName().equals(mdetails.getVliUomName()) || apVoucherLineItem.getVliQuantity() != mdetails.getVliQuantity() || apVoucherLineItem.getVliUnitCost() != mdetails.getVliUnitCost()) {

                            isRecalculate = true;
                            break;
                        }

                        isRecalculate = false;
                    }

                } else {

                    isRecalculate = false;
                }

                apDebitMemo.setVouDescription(details.getVouDescription());
                apDebitMemo.setVouDate(details.getVouDate());
                apDebitMemo.setVouDocumentNumber(details.getVouDocumentNumber());
                apDebitMemo.setVouDmVoucherNumber(details.getVouDmVoucherNumber());
                apDebitMemo.setVouLastModifiedBy(details.getVouLastModifiedBy());
                apDebitMemo.setVouDateLastModified(details.getVouDateLastModified());
                apDebitMemo.setVouReasonForRejection(null);
            }

            LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, companyCode);
            apSupplier.addApVoucher(apDebitMemo);

            LocalApTaxCode apTaxCode = apVoucher.getApTaxCode();

            LocalApWithholdingTaxCode apWithholdingTaxCode = apVoucher.getApWithholdingTaxCode();

            try {

                LocalApVoucherBatch apVoucherBatch = apVoucherBatchHome.findByVbName(VB_NM, branchCode, companyCode);
                apVoucherBatch.addApVoucher(apDebitMemo);

            } catch (FinderException ex) {

            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (isRecalculate) {

                // remove all demo memo lines

                Collection apVoucherLineItems = apDebitMemo.getApVoucherLineItems();

                Iterator i = apVoucherLineItems.iterator();

                while (i.hasNext()) {

                    LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) i.next();

                    i.remove();

                    // apVoucherLineItem.entityRemove();
                    em.remove(apVoucherLineItem);
                }

                // remove all distribution records

                Collection apDistributionRecords = apDebitMemo.getApDistributionRecords();

                i = apDistributionRecords.iterator();

                while (i.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                    i.remove();

                    // apDistributionRecord.entityRemove();
                    em.remove(apDistributionRecord);
                }

                // add new invoice lines and distribution record

                double TOTAL_TAX = 0d;
                double TOTAL_LINE = 0d;

                i = vliList.iterator();

                LocalInvItemLocation invItemLocation = null;

                while (i.hasNext()) {

                    ApModVoucherLineItemDetails mVliDetails = (ApModVoucherLineItemDetails) i.next();

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mVliDetails.getVliLocName(), mVliDetails.getVliIiName(), companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mVliDetails.getVliLine()));
                    }

                    // start date validation
                    Debug.print("Check A");

                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(apDebitMemo.getVouDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    Debug.print("Check B");
                    LocalApVoucherLineItem apVoucherLineItem = this.addApVliEntry(mVliDetails, apDebitMemo, apTaxCode, invItemLocation, companyCode);

                    // add cost of sales distribution and inventory

                    // double COST = apVoucherLineItem.getVliUnitCost();

                    double QTY_RCVD = this.convertByUomFromAndItemAndQuantity(apVoucherLineItem.getInvUnitOfMeasure(), apVoucherLineItem.getInvItemLocation().getInvItem(), apVoucherLineItem.getVliQuantity(), companyCode);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(invItemLocation.getIlCode(), branchCode, companyCode);

                    } catch (FinderException ex) {
                    }

                    if (adBranchItemLocation != null) {

                        this.addApDrEntry(apDebitMemo.getApDrNextLine(), "EXPENSE", EJBCommon.FALSE, apVoucherLineItem.getVliAmount(), adBranchItemLocation.getBilCoaGlInventoryAccount(), apDebitMemo, branchCode, companyCode);

                    } else {

                        this.addApDrEntry(apDebitMemo.getApDrNextLine(), "EXPENSE", EJBCommon.FALSE, apVoucherLineItem.getVliAmount(), invItemLocation.getIlGlCoaInventoryAccount(), apDebitMemo, branchCode, companyCode);
                    }

                    // TOTAL_LINE += COST * QTY_RCVD;
                    TOTAL_LINE += apVoucherLineItem.getVliAmount();
                    TOTAL_TAX += apVoucherLineItem.getVliTaxAmount();
                }

                // add tax distribution if necessary

                if (!apTaxCode.getTcType().equals("NONE") && !apTaxCode.getTcType().equals("EXEMPT")) {

                    this.addApDrEntry(apDebitMemo.getApDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, apTaxCode.getGlChartOfAccount().getCoaCode(), apDebitMemo, branchCode, companyCode);
                }

                // add wtax distribution if necessary

                double W_TAX_AMOUNT = 0d;

                if (apWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfApWTaxRealization().equals("VOUCHER")) {

                    W_TAX_AMOUNT = EJBCommon.roundIt(TOTAL_LINE * (apWithholdingTaxCode.getWtcRate() / 100), this.getGlFcPrecisionUnit(companyCode));

                    this.addApDrEntry(apDebitMemo.getApDrNextLine(), "W-TAX", EJBCommon.TRUE, W_TAX_AMOUNT, apWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), apDebitMemo, branchCode, companyCode);
                }

                // add payable distribution

                LocalAdBranchSupplier adBranchSupplier = null;

                try {

                    adBranchSupplier = adBranchSupplierHome.findBSplBySplCodeAndBrCode(apDebitMemo.getApSupplier().getSplCode(), branchCode, companyCode);

                } catch (FinderException ex) {
                }

                if (adBranchSupplier != null) {

                    this.addApDrEntry(apDebitMemo.getApDrNextLine(), "PAYABLE", EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT, adBranchSupplier.getBsplGlCoaPayableAccount(), apDebitMemo, branchCode, companyCode);

                } else {

                    this.addApDrEntry(apDebitMemo.getApDrNextLine(), "PAYABLE", EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT, apDebitMemo.getApSupplier().getSplCoaGlPayableAccount(), apDebitMemo, branchCode, companyCode);
                }

                // set invoice amount due

                apDebitMemo.setVouBillAmount(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT);

                if (adPreference.getPrfApDebitMemoOverrideCost() != 1) {
                    if (apDebitMemo.getVouBillAmount() > apVoucher.getVouAmountDue() - apVoucher.getVouAmountPaid()) {

                        throw new ApVOUOverapplicationNotAllowedException();
                    }
                }

                // create new voucher payment schedule lock

                Collection apVoucherPaymentSchedules = apVoucher.getApVoucherPaymentSchedules();

                i = apVoucherPaymentSchedules.iterator();

                while (i.hasNext()) {

                    LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) i.next();

                    apVoucherPaymentSchedule.setVpsLock(EJBCommon.TRUE);
                }

            } else {

                Iterator i = vliList.iterator();

                LocalInvItemLocation invItemLocation = null;

                while (i.hasNext()) {

                    ApModVoucherLineItemDetails mVliDetails = (ApModVoucherLineItemDetails) i.next();

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mVliDetails.getVliLocName(), mVliDetails.getVliIiName(), companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mVliDetails.getVliLine()));
                    }

                    // start date validation
                    Debug.print("Check C");
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(apDebitMemo.getVouDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }
                    Debug.print("Check D");
                }
            }

            // generate approval status

            String VOU_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if ap voucher approval is enabled

                if (adApproval.getAprEnableApDebitMemo() == EJBCommon.FALSE) {

                    VOU_APPRVL_STATUS = "N/A";

                } else {

                    // check if voucher is self approved

                    LocalAdUser adUser = adUserHome.findByUsrName(details.getVouLastModifiedBy(), companyCode);

                    VOU_APPRVL_STATUS = apApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getVouLastModifiedBy(), adUser.getUsrDescription(), "AP DEBIT MEMO", apVoucher.getVouCode(), apVoucher.getVouDocumentNumber(), apVoucher.getVouDate(), apVoucher.getVouBillAmount(), branchCode, companyCode);
                }
            }

            if (VOU_APPRVL_STATUS != null && VOU_APPRVL_STATUS.equals("N/A") && adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeApDebitMemoPost(apDebitMemo.getVouCode(), apDebitMemo.getVouLastModifiedBy(), branchCode, companyCode);
            }

            // set debit memo approval status

            apDebitMemo.setVouApprovalStatus(VOU_APPRVL_STATUS);

            return apDebitMemo.getVouCode();

        } catch (GlobalRecordAlreadyDeletedException | AdPRFCoaGlVarianceAccountNotFoundException |
                 GlobalBranchAccountNumberInvalidException | GlobalInventoryDateException |
                 GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
                 GlobalTransactionAlreadyLockedException | ApVOUOverapplicationNotAllowedException |
                 GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
                 GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
                 GlobalDocumentNumberNotUniqueException | GlobalNoRecordFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveApVouVliEntryMobile(ApVoucherDetails details, String SPL_SPPLR_CODE, String VB_NM, ArrayList vliList, boolean isDraft, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalNoRecordFoundException, GlobalDocumentNumberNotUniqueException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, ApVOUOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApDebitMemoEntryControllerBean saveApVouEntryMobile");

        LocalApVoucher apDebitMemo = null;
        LocalApVoucher apVoucher = null;
        LocalInvItemHome invItemHome = null;
        LocalInvLocationHome invLocationHome = null;

        try {

            // validate if debit memo is already deleted

            try {

                if (details.getVouCode() != null) {

                    apDebitMemo = apVoucherHome.findByPrimaryKey(details.getVouCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if debit memo is already posted, void, approved or pending or locked

            if (details.getVouCode() != null) {

                if (apDebitMemo.getVouApprovalStatus() != null) {

                    if (apDebitMemo.getVouApprovalStatus().equals("APPROVED") || apDebitMemo.getVouApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (apDebitMemo.getVouApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (apDebitMemo.getVouPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (apDebitMemo.getVouVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // debit memo void

            if (details.getVouCode() != null && details.getVouVoid() == EJBCommon.TRUE && apDebitMemo.getVouPosted() == EJBCommon.FALSE) {

                // release lock

                LocalApVoucher apLockedVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(apDebitMemo.getVouDmVoucherNumber(), EJBCommon.FALSE, branchCode, companyCode);

                Collection apLockedVoucherPaymentSchedules = apLockedVoucher.getApVoucherPaymentSchedules();

                for (Object apLockedVoucherPaymentSchedule : apLockedVoucherPaymentSchedules) {

                    LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) apLockedVoucherPaymentSchedule;

                    apVoucherPaymentSchedule.setVpsLock(EJBCommon.FALSE);
                }

                apDebitMemo.setVouVoid(EJBCommon.TRUE);
                apDebitMemo.setVouLastModifiedBy(details.getVouLastModifiedBy());
                apDebitMemo.setVouDateLastModified(details.getVouDateLastModified());

                return apDebitMemo.getVouCode();
            }

            // validate if voucher number exists

            try {

                apVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(details.getVouDmVoucherNumber(), EJBCommon.FALSE, branchCode, companyCode);

                if (apVoucher.getVouPosted() != EJBCommon.TRUE) {

                    throw new GlobalNoRecordFoundException();
                }

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            // validate if document number is unique document number is automatic then set
            // next sequence

            try {

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                if (details.getVouCode() == null) {

                    try {

                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AP DEBIT MEMO", companyCode);

                    } catch (FinderException ex) {
                    }

                    try {

                        adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);

                    } catch (FinderException ex) {
                    }

                    LocalApVoucher apExistingDebitMemo = null;

                    try {

                        apExistingDebitMemo = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(details.getVouDocumentNumber(), EJBCommon.TRUE, branchCode, companyCode);

                    } catch (FinderException ex) {
                    }

                    if (apExistingDebitMemo != null) {

                        throw new GlobalDocumentNumberNotUniqueException();
                    }

                    if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getVouDocumentNumber() == null || details.getVouDocumentNumber().trim().length() == 0)) {

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), EJBCommon.TRUE, branchCode, companyCode);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                } catch (FinderException ex) {

                                    details.setVouDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;
                                }

                            } else {

                                try {

                                    apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), EJBCommon.TRUE, branchCode, companyCode);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                } catch (FinderException ex) {

                                    details.setVouDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;
                                }
                            }
                        }
                    }

                } else {

                    LocalApVoucher apExistingDebitMemo = null;

                    try {

                        apExistingDebitMemo = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(details.getVouDocumentNumber(), EJBCommon.TRUE, branchCode, companyCode);

                    } catch (FinderException ex) {
                    }

                    if (apExistingDebitMemo != null && !apExistingDebitMemo.getVouCode().equals(details.getVouCode())) {

                        throw new GlobalDocumentNumberNotUniqueException();
                    }

                    if (apDebitMemo.getVouDocumentNumber() != details.getVouDocumentNumber() && (details.getVouDocumentNumber() == null || details.getVouDocumentNumber().trim().length() == 0)) {

                        details.setVouDocumentNumber(apDebitMemo.getVouDocumentNumber());
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

            // validate if voucher entered is already locked by dm or check

            Collection apValidateVoucherPaymentSchedules = apVoucherPaymentScheduleHome.findByVpsLockAndVouCode(EJBCommon.TRUE, apVoucher.getVouCode(), companyCode);

            if (details.getVouCode() == null && !apValidateVoucherPaymentSchedules.isEmpty() || details.getVouCode() != null && !details.getVouDmVoucherNumber().equals(apDebitMemo.getVouDmVoucherNumber()) && !apValidateVoucherPaymentSchedules.isEmpty()) {

                throw new GlobalTransactionAlreadyLockedException();
            }

            // used in checking if debit memo should re-generate distribution records and
            // re-calculate
            // taxes

            boolean isRecalculate = true;

            // create voucher

            if (details.getVouCode() == null) {

                apDebitMemo = apVoucherHome.create(EJBCommon.TRUE, details.getVouDescription(), details.getVouDate(), details.getVouDocumentNumber(), null, details.getVouDmVoucherNumber(), null, 0d, 0d, 0d, 0d, null, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, details.getVouCreatedBy(), details.getVouDateCreated(), details.getVouLastModifiedBy(), details.getVouDateLastModified(), null, null, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, branchCode, companyCode);

            } else {

                // release lock

                LocalApVoucher apLockedVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(apDebitMemo.getVouDmVoucherNumber(), EJBCommon.FALSE, branchCode, companyCode);

                Collection apLockedVoucherPaymentSchedules = apLockedVoucher.getApVoucherPaymentSchedules();

                for (Object apLockedVoucherPaymentSchedule : apLockedVoucherPaymentSchedules) {

                    LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) apLockedVoucherPaymentSchedule;

                    apVoucherPaymentSchedule.setVpsLock(EJBCommon.FALSE);
                }

                // check if critical fields are changed

                if (!apDebitMemo.getApSupplier().getSplSupplierCode().equals(SPL_SPPLR_CODE) || !apDebitMemo.getVouDmVoucherNumber().equals(details.getVouDmVoucherNumber()) || vliList.size() != apDebitMemo.getApVoucherLineItems().size() || !(apDebitMemo.getVouDate().equals(details.getVouDate()))) {

                    isRecalculate = true;

                } else if (vliList.size() == apDebitMemo.getApVoucherLineItems().size()) {

                    Iterator vliIter = apDebitMemo.getApVoucherLineItems().iterator();
                    Iterator vliListIter = vliList.iterator();

                    while (vliIter.hasNext()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) vliIter.next();
                        ApModVoucherLineItemDetails mdetails = (ApModVoucherLineItemDetails) vliListIter.next();

                        if (!apVoucherLineItem.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getVliIiName()) || !apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getVliLocName()) || !apVoucherLineItem.getInvUnitOfMeasure().getUomName().equals(mdetails.getVliUomName()) || apVoucherLineItem.getVliQuantity() != mdetails.getVliQuantity() || apVoucherLineItem.getVliUnitCost() != mdetails.getVliUnitCost()) {

                            isRecalculate = true;
                            break;
                        }

                        isRecalculate = false;
                    }

                } else {

                    isRecalculate = false;
                }

                apDebitMemo.setVouDescription(details.getVouDescription());
                apDebitMemo.setVouDate(details.getVouDate());
                apDebitMemo.setVouDocumentNumber(details.getVouDocumentNumber());
                apDebitMemo.setVouDmVoucherNumber(details.getVouDmVoucherNumber());
                apDebitMemo.setVouLastModifiedBy(details.getVouLastModifiedBy());
                apDebitMemo.setVouDateLastModified(details.getVouDateLastModified());
                apDebitMemo.setVouReasonForRejection(null);
            }

            LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, companyCode);
            apSupplier.addApVoucher(apDebitMemo);

            LocalApTaxCode apTaxCode = apVoucher.getApTaxCode();

            LocalApWithholdingTaxCode apWithholdingTaxCode = apVoucher.getApWithholdingTaxCode();

            try {

                LocalApVoucherBatch apVoucherBatch = apVoucherBatchHome.findByVbName(VB_NM, branchCode, companyCode);
                apVoucherBatch.addApVoucher(apDebitMemo);

            } catch (FinderException ex) {

            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (isRecalculate) {

                // remove all demo memo lines

                Collection apVoucherLineItems = apDebitMemo.getApVoucherLineItems();

                Iterator i = apVoucherLineItems.iterator();

                while (i.hasNext()) {

                    LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) i.next();

                    i.remove();

                    // apVoucherLineItem.entityRemove();
                    em.remove(apVoucherLineItem);
                }

                // remove all distribution records

                Collection apDistributionRecords = apDebitMemo.getApDistributionRecords();

                i = apDistributionRecords.iterator();

                while (i.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                    i.remove();

                    // apDistributionRecord.entityRemove();
                    em.remove(apDistributionRecord);
                }

                // add new invoice lines and distribution record

                double TOTAL_TAX = 0d;
                double TOTAL_LINE = 0d;

                i = vliList.iterator();

                LocalInvItemLocation invItemLocation = null;
                LocalInvItem item = null;
                LocalInvLocation location = null;
                while (i.hasNext()) {

                    ApModVoucherLineItemDetails mVliDetails = (ApModVoucherLineItemDetails) i.next();

                    try {

                        item = invItemHome.findByIiName(mVliDetails.getVliIiName(), companyCode);
                        location = invLocationHome.findByPrimaryKey(item.getIiDefaultLocation());
                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(location.getLocName(), mVliDetails.getVliIiName(), companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mVliDetails.getVliLine()));
                    }

                    // supply missing entries for the data collector
                    mVliDetails.setVliUnitCost(item.getIiUnitCost());
                    mVliDetails.setVliAmount(mVliDetails.getVliQuantity() * item.getIiUnitCost());
                    mVliDetails.setVliLocName(location.getLocName());
                    mVliDetails.setVliUomName(item.getInvUnitOfMeasure().getUomName());

                    // start date validation
                    Debug.print("Check A");

                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(apDebitMemo.getVouDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    Debug.print("Check B");
                    LocalApVoucherLineItem apVoucherLineItem = this.addApVliEntry(mVliDetails, apDebitMemo, apTaxCode, invItemLocation, companyCode);

                    // add cost of sales distribution and inventory

                    // double COST = apVoucherLineItem.getVliUnitCost();

                    double QTY_RCVD = this.convertByUomFromAndItemAndQuantity(apVoucherLineItem.getInvUnitOfMeasure(), apVoucherLineItem.getInvItemLocation().getInvItem(), apVoucherLineItem.getVliQuantity(), companyCode);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(invItemLocation.getIlCode(), branchCode, companyCode);

                    } catch (FinderException ex) {
                    }

                    if (adBranchItemLocation != null) {

                        this.addApDrEntry(apDebitMemo.getApDrNextLine(), "EXPENSE", EJBCommon.FALSE, apVoucherLineItem.getVliAmount(), adBranchItemLocation.getBilCoaGlInventoryAccount(), apDebitMemo, branchCode, companyCode);

                    } else {

                        this.addApDrEntry(apDebitMemo.getApDrNextLine(), "EXPENSE", EJBCommon.FALSE, apVoucherLineItem.getVliAmount(), invItemLocation.getIlGlCoaInventoryAccount(), apDebitMemo, branchCode, companyCode);
                    }

                    // TOTAL_LINE += COST * QTY_RCVD;
                    TOTAL_LINE += apVoucherLineItem.getVliAmount();
                    TOTAL_TAX += apVoucherLineItem.getVliTaxAmount();
                }

                // add tax distribution if necessary

                if (!apTaxCode.getTcType().equals("NONE") && !apTaxCode.getTcType().equals("EXEMPT")) {

                    this.addApDrEntry(apDebitMemo.getApDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, apTaxCode.getGlChartOfAccount().getCoaCode(), apDebitMemo, branchCode, companyCode);
                }

                // add wtax distribution if necessary

                double W_TAX_AMOUNT = 0d;

                if (apWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfApWTaxRealization().equals("VOUCHER")) {

                    W_TAX_AMOUNT = EJBCommon.roundIt(TOTAL_LINE * (apWithholdingTaxCode.getWtcRate() / 100), this.getGlFcPrecisionUnit(companyCode));

                    this.addApDrEntry(apDebitMemo.getApDrNextLine(), "W-TAX", EJBCommon.TRUE, W_TAX_AMOUNT, apWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), apDebitMemo, branchCode, companyCode);
                }

                // add payable distribution

                LocalAdBranchSupplier adBranchSupplier = null;

                try {

                    adBranchSupplier = adBranchSupplierHome.findBSplBySplCodeAndBrCode(apDebitMemo.getApSupplier().getSplCode(), branchCode, companyCode);

                } catch (FinderException ex) {
                }

                if (adBranchSupplier != null) {

                    this.addApDrEntry(apDebitMemo.getApDrNextLine(), "PAYABLE", EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT, adBranchSupplier.getBsplGlCoaPayableAccount(), apDebitMemo, branchCode, companyCode);

                } else {

                    this.addApDrEntry(apDebitMemo.getApDrNextLine(), "PAYABLE", EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT, apDebitMemo.getApSupplier().getSplCoaGlPayableAccount(), apDebitMemo, branchCode, companyCode);
                }

                // set invoice amount due

                apDebitMemo.setVouBillAmount(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT);

                if (adPreference.getPrfApDebitMemoOverrideCost() != 1) {
                    if (apDebitMemo.getVouBillAmount() > apVoucher.getVouAmountDue() - apVoucher.getVouAmountPaid()) {

                        throw new ApVOUOverapplicationNotAllowedException();
                    }
                }

                // create new voucher payment schedule lock

                Collection apVoucherPaymentSchedules = apVoucher.getApVoucherPaymentSchedules();

                i = apVoucherPaymentSchedules.iterator();

                while (i.hasNext()) {

                    LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) i.next();

                    apVoucherPaymentSchedule.setVpsLock(EJBCommon.TRUE);
                }

            } else {

                Iterator i = vliList.iterator();

                LocalInvItemLocation invItemLocation = null;

                while (i.hasNext()) {

                    ApModVoucherLineItemDetails mVliDetails = (ApModVoucherLineItemDetails) i.next();

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mVliDetails.getVliLocName(), mVliDetails.getVliIiName(), companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mVliDetails.getVliLine()));
                    }

                    // start date validation
                    Debug.print("Check C");
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(apDebitMemo.getVouDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }
                    Debug.print("Check D");
                }
            }

            // generate approval status

            String VOU_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if ap voucher approval is enabled

                if (adApproval.getAprEnableApDebitMemo() == EJBCommon.FALSE) {

                    VOU_APPRVL_STATUS = "N/A";

                } else {

                    // check if voucher is self approved

                    LocalAdUser adUser = adUserHome.findByUsrName(details.getVouLastModifiedBy(), companyCode);

                    VOU_APPRVL_STATUS = apApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getVouLastModifiedBy(), adUser.getUsrDescription(), "AP DEBIT MEMO", apVoucher.getVouCode(), apVoucher.getVouDocumentNumber(), apVoucher.getVouDate(), apVoucher.getVouBillAmount(), branchCode, companyCode);
                }
            }

            if (VOU_APPRVL_STATUS != null && VOU_APPRVL_STATUS.equals("N/A") && adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeApDebitMemoPost(apDebitMemo.getVouCode(), apDebitMemo.getVouLastModifiedBy(), branchCode, companyCode);
            }

            // set debit memo approval status

            apDebitMemo.setVouApprovalStatus(VOU_APPRVL_STATUS);

            return apDebitMemo.getVouCode();

        } catch (GlobalRecordAlreadyDeletedException | AdPRFCoaGlVarianceAccountNotFoundException |
                 GlobalBranchAccountNumberInvalidException | GlobalInventoryDateException |
                 GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
                 GlobalTransactionAlreadyLockedException | ApVOUOverapplicationNotAllowedException |
                 GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
                 GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
                 GlobalDocumentNumberNotUniqueException | GlobalNoRecordFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteApVouEntry(Integer VOU_CODE, String AD_USR, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("ApDebitMemoEntryControllerBean deleteApVouEntry");

        try {

            LocalApVoucher apDebitMemo = apVoucherHome.findByPrimaryKey(VOU_CODE);

            if (apDebitMemo.getVouApprovalStatus() != null && apDebitMemo.getVouApprovalStatus().equals("PENDING")) {

                Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AP DEBIT MEMO", apDebitMemo.getVouCode(), companyCode);

                for (Object approvalQueue : adApprovalQueues) {

                    LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                    // adApprovalQueue.entityRemove();
                    em.remove(adApprovalQueue);
                }
            }

            // release lock

            LocalApVoucher apVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(apDebitMemo.getVouDmVoucherNumber(), EJBCommon.FALSE, branchCode, companyCode);

            Collection apVoucherPaymentSchedules = apVoucher.getApVoucherPaymentSchedules();

            for (Object voucherPaymentSchedule : apVoucherPaymentSchedules) {

                LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) voucherPaymentSchedule;

                apVoucherPaymentSchedule.setVpsLock(EJBCommon.FALSE);
            }

            adDeleteAuditTrailHome.create("AP DEBIT MEMO", apDebitMemo.getVouDate(), apDebitMemo.getVouDocumentNumber(), apDebitMemo.getVouReferenceNumber(), apDebitMemo.getVouAmountDue(), AD_USR, new Date(), companyCode);

            // apDebitMemo.entityRemove();
            em.remove(apDebitMemo);

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

        Debug.print("ApDebitMemoEntryControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByVouCode(Integer VOU_CODE, Integer companyCode) {

        Debug.print("ApDebitMemoEntryControllerBean getAdApprovalNotifiedUsersByVouCode");

        ArrayList list = new ArrayList();

        try {

            LocalApVoucher apVoucher = apVoucherHome.findByPrimaryKey(VOU_CODE);

            if (apVoucher.getVouPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AP DEBIT MEMO", VOU_CODE, companyCode);

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

    public byte getAdPrfEnableApDebitMemoBatch(Integer companyCode) {

        Debug.print("ApDebitMemoEntryControllerBean getAdPrfEnableApDebitMemoBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfEnableApVoucherBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer companyCode) {

        Debug.print("ApVoucherEntryControllerBean getInvGpQuantityPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfApUseSupplierPulldown(Integer companyCode) {

        Debug.print("ApDebitMemoEntryControllerBean getAdPrfApUseSupplierPulldown");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfApUseSupplierPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getApSplNameBySplSupplierCode(String SPL_SPPLR_CODE, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApVoucherEntryControllerBean getApSplNameBySplSupplierCode");

        try {

            LocalApSupplier apSupplier = null;

            try {

                apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, companyCode);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            return apSupplier.getSplName();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private double getInvFifoCost(Date CST_DT, Integer IL_CODE, double CST_QTY, double CST_COST, boolean isAdjustFifo, Integer branchCode, Integer companyCode) {

        try {

            Collection invFifoCostings = invCostingHome.findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(CST_DT, IL_CODE, branchCode, companyCode);

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
                        return EJBCommon.roundIt(invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived(), this.getGlFcPrecisionUnit(companyCode));
                    } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold(), this.getGlFcPrecisionUnit(companyCode));
                    } else {
                        return EJBCommon.roundIt(invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity(), this.getGlFcPrecisionUnit(companyCode));
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

    private void addApDrEntry(ApModDistributionRecordDetails mdetails, LocalApVoucher apDebitMemo, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ApDebitMemoEntryControllerBean addApDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate if coa exists

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(mdetails.getDrCoaAccountNumber(), branchCode, companyCode);

                if (glChartOfAccount.getCoaEnable() == EJBCommon.FALSE) {
                    throw new GlobalBranchAccountNumberInvalidException();
                }

            } catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.create(mdetails.getDrLine(), mdetails.getDrClass(), EJBCommon.roundIt(mdetails.getDrAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()), mdetails.getDrDebit(), EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            apDebitMemo.addApDistributionRecord(apDistributionRecord);
            glChartOfAccount.addApDistributionRecord(apDistributionRecord);

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeApDebitMemoPost(Integer VOU_CODE, String USR_NM, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApDebitMemoEntryControllerBean executeApDebitMemoPost");

        LocalApVoucher apVoucher = null;
        LocalApVoucher apDebitedVoucher = null;

        try {

            // validate if voucher/debit memo is already deleted

            try {

                apVoucher = apVoucherHome.findByPrimaryKey(VOU_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if voucher/debit memo is already posted or void

            if (apVoucher.getVouPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

            } else if (apVoucher.getVouVoid() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidException();
            }

            if (apVoucher.getVouVoid() == EJBCommon.FALSE && apVoucher.getVouPosted() == EJBCommon.FALSE) {

                // get debited voucher

                apDebitedVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(apVoucher.getVouDmVoucherNumber(), EJBCommon.FALSE, branchCode, companyCode);

                // decrease supplier balance

                double VOU_AMNT = this.convertForeignToFunctionalCurrency(apDebitedVoucher.getGlFunctionalCurrency().getFcCode(), apDebitedVoucher.getGlFunctionalCurrency().getFcName(), apDebitedVoucher.getVouConversionDate(), apDebitedVoucher.getVouConversionRate(), apVoucher.getVouBillAmount(), companyCode) * -1;

                this.post(apVoucher.getVouDate(), VOU_AMNT, apVoucher.getApSupplier(), companyCode);

                // decrease voucher and vps amounts and release lock

                double DEBIT_PERCENT = EJBCommon.roundIt(apVoucher.getVouBillAmount() / apDebitedVoucher.getVouAmountDue(), (short) 6);

                apDebitedVoucher.setVouAmountPaid(apDebitedVoucher.getVouAmountPaid() + apVoucher.getVouBillAmount());

                double TOTAL_VOUCHER_PAYMENT_SCHEDULE = 0d;

                Collection apVoucherPaymentSchedules = apDebitedVoucher.getApVoucherPaymentSchedules();

                Iterator i = apVoucherPaymentSchedules.iterator();

                while (i.hasNext()) {

                    LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) i.next();

                    double VOUCHER_PAYMENT_SCHEDULE_AMOUNT = 0;

                    // if last payment schedule subtract to avoid rounding difference error

                    if (i.hasNext()) {

                        VOUCHER_PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt(apVoucherPaymentSchedule.getVpsAmountDue() * DEBIT_PERCENT, this.getGlFcPrecisionUnit(companyCode));

                    } else {

                        VOUCHER_PAYMENT_SCHEDULE_AMOUNT = apVoucher.getVouBillAmount() - TOTAL_VOUCHER_PAYMENT_SCHEDULE;
                    }

                    apVoucherPaymentSchedule.setVpsAmountPaid(apVoucherPaymentSchedule.getVpsAmountPaid() + VOUCHER_PAYMENT_SCHEDULE_AMOUNT);

                    apVoucherPaymentSchedule.setVpsLock(EJBCommon.FALSE);

                    TOTAL_VOUCHER_PAYMENT_SCHEDULE += VOUCHER_PAYMENT_SCHEDULE_AMOUNT;
                }

                Collection apVoucherLineItems = apVoucher.getApVoucherLineItems();

                if (apVoucherLineItems != null && !apVoucherLineItems.isEmpty()) {

                    for (Object voucherLineItem : apVoucherLineItems) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) voucherLineItem;

                        String II_NM = apVoucherLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_RCVD = this.convertByUomFromAndItemAndQuantity(apVoucherLineItem.getInvUnitOfMeasure(), apVoucherLineItem.getInvItemLocation().getInvItem(), apVoucherLineItem.getVliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;
                        // CREATE COSTING
                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(apVoucher.getVouDate(), II_NM, LOC_NM, branchCode, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = apVoucherLineItem.getVliUnitCost();

                        if (invCosting == null) {

                            this.postToInv(apVoucherLineItem, apVoucher.getVouDate(), -QTY_RCVD, -COST * QTY_RCVD, -QTY_RCVD, -COST * QTY_RCVD, 0d, null, branchCode, companyCode);

                        } else {

                            this.postToInv(apVoucherLineItem, apVoucher.getVouDate(), -QTY_RCVD, -COST * QTY_RCVD, invCosting.getCstRemainingQuantity() - QTY_RCVD, invCosting.getCstRemainingValue() - (COST * QTY_RCVD), 0d, null, branchCode, companyCode);
                        }
                    }
                }

            } else if (apVoucher.getVouVoid() == EJBCommon.TRUE && apVoucher.getVouPosted() == EJBCommon.FALSE) {

            }

            // set voucher post status

            apVoucher.setVouPosted(EJBCommon.TRUE);
            apVoucher.setVouPostedBy(USR_NM);
            apVoucher.setVouDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            if (adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(apVoucher.getVouDate(), companyCode);

                } catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), apVoucher.getVouDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if voucher is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection apDistributionRecords = apDistributionRecordHome.findImportableDrByVouCode(apVoucher.getVouCode(), companyCode);

                Iterator j = apDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    if (apVoucher.getVouDebitMemo() == EJBCommon.FALSE) {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apDebitedVoucher.getGlFunctionalCurrency().getFcCode(), apDebitedVoucher.getGlFunctionalCurrency().getFcName(), apDebitedVoucher.getVouConversionDate(), apDebitedVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), companyCode);
                    }

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

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS PAYABLES", apVoucher.getVouDebitMemo() == EJBCommon.FALSE ? "VOUCHERS" : "DEBIT MEMOS", companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (apDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (apDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                    }

                    LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                    // glChartOfAccount.addGlJournalLine(glOffsetJournalLine);
                    glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

                } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                    throw new GlobalJournalNotBalanceException();
                }

                // create journal batch if necessary

                LocalGlJournalBatch glJournalBatch = null;
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

                try {

                    if (adPreference.getPrfEnableApVoucherBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " " + apVoucher.getApVoucherBatch().getVbName(), branchCode, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " VOUCHERS", branchCode, companyCode);
                    }

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                    if (adPreference.getPrfEnableApVoucherBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " " + apVoucher.getApVoucherBatch().getVbName(), "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " VOUCHERS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);
                    }
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(apVoucher.getVouDmVoucherNumber(), apVoucher.getVouDescription(), apVoucher.getVouDate(), 0.0d, null, apVoucher.getVouDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), apVoucher.getApSupplier().getSplTin(), apVoucher.getApSupplier().getSplName(), EJBCommon.FALSE, null, branchCode, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS PAYABLES", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName(apVoucher.getVouDebitMemo() == EJBCommon.FALSE ? "VOUCHERS" : "DEBIT MEMOS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = apDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    if (apVoucher.getVouDebitMemo() == EJBCommon.FALSE) {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apDebitedVoucher.getGlFunctionalCurrency().getFcCode(), apDebitedVoucher.getGlFunctionalCurrency().getFcName(), apDebitedVoucher.getVouConversionDate(), apDebitedVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), companyCode);
                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(apDistributionRecord.getDrLine(), apDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    // apDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);
                    glJournalLine.setGlChartOfAccount(apDistributionRecord.getGlChartOfAccount());

                    // glJournal.addGlJournalLine(glJournalLine);
                    glJournalLine.setGlJournal(glJournal);

                    apDistributionRecord.setDrImported(EJBCommon.TRUE);
                }

                if (glOffsetJournalLine != null) {

                    // glJournal.addGlJournalLine(glOffsetJournalLine);
                    glOffsetJournalLine.setGlJournal(glJournal);
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

        } catch (GlJREffectiveDateNoPeriodExistException | AdPRFCoaGlVarianceAccountNotFoundException |
                 GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
                 GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void post(Date VOU_DT, double VOU_AMNT, LocalApSupplier apSupplier, Integer companyCode) {

        Debug.print("ApDebitMemoEntryControllerBean post");

        LocalApSupplierBalanceHome apSupplierBalanceHome = null;

        // Initialize EJB Home

        try {

            apSupplierBalanceHome = (LocalApSupplierBalanceHome) EJBHomeFactory.lookUpLocalHome(LocalApSupplierBalanceHome.JNDI_NAME, LocalApSupplierBalanceHome.class);

        } catch (NamingException ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        try {

            // find supplier balance before or equal voucher date

            Collection apSupplierBalances = apSupplierBalanceHome.findByBeforeOrEqualVouDateAndSplSupplierCode(VOU_DT, apSupplier.getSplSupplierCode(), companyCode);

            if (!apSupplierBalances.isEmpty()) {

                // get last voucher

                ArrayList apSupplierBalanceList = new ArrayList(apSupplierBalances);

                LocalApSupplierBalance apSupplierBalance = (LocalApSupplierBalance) apSupplierBalanceList.get(apSupplierBalanceList.size() - 1);

                if (apSupplierBalance.getSbDate().before(VOU_DT)) {

                    // create new balance

                    LocalApSupplierBalance apNewSupplierBalance = apSupplierBalanceHome.create(VOU_DT, apSupplierBalance.getSbBalance() + VOU_AMNT, companyCode);

                    // apSupplier.addApSupplierBalance(apNewSupplierBalance);
                    apNewSupplierBalance.setApSupplier(apSupplier);

                } else { // equals to voucher date

                    apSupplierBalance.setSbBalance(apSupplierBalance.getSbBalance() + VOU_AMNT);
                }

            } else {

                // create new balance

                LocalApSupplierBalance apNewSupplierBalance = apSupplierBalanceHome.create(VOU_DT, VOU_AMNT, companyCode);

                // apSupplier.addApSupplierBalance(apNewSupplierBalance);
                apNewSupplierBalance.setApSupplier(apSupplier);
            }

            // propagate to subsequent balances if necessary

            apSupplierBalances = apSupplierBalanceHome.findByAfterVouDateAndSplSupplierCode(VOU_DT, apSupplier.getSplSupplierCode(), companyCode);

            for (Object supplierBalance : apSupplierBalances) {

                LocalApSupplierBalance apSupplierBalance = (LocalApSupplierBalance) supplierBalance;

                apSupplierBalance.setSbBalance(apSupplierBalance.getSbBalance() + VOU_AMNT);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("ApDebitMemoEntryControllerBean convertForeignToFunctionalCurrency");

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

    private double convertFunctionalToForeignCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("ApDebitMemoEntryControllerBean convertFunctionalToForeignCurrency");

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

        } else if (CONVERSION_DATE != null) {

            try {

                // Get functional currency rate

                LocalGlFunctionalCurrencyRate glReceiptFunctionalCurrencyRate = null;

                if (!FC_NM.equals("USD")) {

                    glReceiptFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(FC_CODE, CONVERSION_DATE, companyCode);

                    AMOUNT = AMOUNT / glReceiptFunctionalCurrencyRate.getFrXToUsd();
                }

                // Get set of book functional currency rate if necessary

                if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, companyCode);

                    AMOUNT = AMOUNT * glCompanyFunctionalCurrencyRate.getFrXToUsd();
                }

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }
        }

        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("ApDebitMemoEntryControllerBean postToGl");


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

    private LocalApVoucherLineItem addApVliEntry(ApModVoucherLineItemDetails mdetails, LocalApVoucher apVoucher, LocalApTaxCode apTaxCode, LocalInvItemLocation invItemLocation, Integer companyCode) {

        Debug.print("ArCreditMemoEntryControllerBean addArIlEntry");

        try {

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode);

            double VLI_AMNT = 0d;
            double VLI_TAX_AMNT = 0d;

            // calculate net amount

            if (apTaxCode.getTcType().equals("INCLUSIVE")) {

                VLI_AMNT = EJBCommon.roundIt(mdetails.getVliAmount() / (1 + (apTaxCode.getTcRate() / 100)), precisionUnit);

            } else {

                // tax exclusive, none, zero rated or exempt

                VLI_AMNT = mdetails.getVliAmount();
            }

            // calculate tax

            if (!apTaxCode.getTcType().equals("NONE") && !apTaxCode.getTcType().equals("EXEMPT")) {

                if (apTaxCode.getTcType().equals("INCLUSIVE")) {

                    VLI_TAX_AMNT = EJBCommon.roundIt(mdetails.getVliAmount() - VLI_AMNT, precisionUnit);

                } else if (apTaxCode.getTcType().equals("EXCLUSIVE")) {

                    VLI_TAX_AMNT = EJBCommon.roundIt(mdetails.getVliAmount() * apTaxCode.getTcRate() / 100, precisionUnit);

                } else {

                    // tax none zero-rated or exempt

                }
            }

            LocalApVoucherLineItem apVoucherLineItem = apVoucherLineItemHome.create(mdetails.getVliLine(), mdetails.getVliQuantity(), mdetails.getVliUnitCost(), VLI_AMNT, VLI_TAX_AMNT, mdetails.getVliDiscount1(), mdetails.getVliDiscount2(), mdetails.getVliDiscount3(), mdetails.getVliDiscount4(), mdetails.getVliTotalDiscount(), null, null, null, mdetails.getVliTax(), companyCode);
            apVoucherLineItem.setApVoucher(apVoucher);
            apVoucherLineItem.setInvItemLocation(invItemLocation);

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getVliUomName(), companyCode);
            apVoucherLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

            return apVoucherLineItem;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addApDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalApVoucher apVoucher, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArCreditMemoEntryControllerBean addArDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);

                if (glChartOfAccount.getCoaEnable() == EJBCommon.FALSE) {
                    throw new GlobalBranchAccountNumberInvalidException();
                }

            } catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.create(DR_LN, DR_CLSS, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_DBT, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
            apDistributionRecord.setApVoucher(apVoucher);
            apDistributionRecord.setGlChartOfAccount(glChartOfAccount);

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInv(LocalApVoucherLineItem apVoucherLineItem, Date CST_DT, double CST_QTY_RCVD, double CST_ITM_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApDebitMemoEntryControllerBean postToInv");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = apVoucherLineItem.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_RCVD = EJBCommon.roundIt(CST_QTY_RCVD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ITM_CST = EJBCommon.roundIt(CST_ITM_CST, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            try {

                // generate line number
                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            // void subsequent cost variance adjustments
            Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT, invItemLocation.getIlCode(), branchCode, companyCode);
            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, CST_QTY_RCVD, CST_ITM_CST, 0d, 0d, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, branchCode, companyCode);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setApVoucherLineItem(apVoucherLineItem);

            invCosting.setCstQuantity(CST_QTY_RCVD);
            invCosting.setCstCost(CST_ITM_CST);

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "APVOU" + apVoucherLineItem.getApVoucher().getVouDocumentNumber(), apVoucherLineItem.getApVoucher().getVouDescription(), apVoucherLineItem.getApVoucher().getVouDate(), USR_NM, branchCode, companyCode);
            }

            // propagate balance if necessary

            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

            i = invCostings.iterator();

            while (i.hasNext()) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_QTY_RCVD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ITM_CST);
            }

            // regenerate cost variance
            this.regenerateCostVariance(invCostings, invCosting, branchCode, companyCode);

        } catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double QTY_RCVD, Integer companyCode) {

        Debug.print("ApDebitMemoEntryControllerBean convertByUomFromAndItemAndQuantity");

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

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) {

        Debug.print("ApVoucherEntryController voidInvAdjustment");

        try {

            Collection invDistributionRecords = invAdjustment.getInvDistributionRecords();
            ArrayList list = new ArrayList();

            Iterator i = invDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                list.add(invDistributionRecord);
            }

            i = list.iterator();

            while (i.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), invDistributionRecord.getDrClass(), invDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, invDistributionRecord.getDrAmount(), EJBCommon.TRUE, invDistributionRecord.getInvChartOfAccount().getCoaCode(), invAdjustment, branchCode, companyCode);
            }

            Collection invAjustmentLines = invAdjustment.getInvAdjustmentLines();
            i = invAjustmentLines.iterator();
            list.clear();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                list.add(invAdjustmentLine);
            }

            i = list.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                this.addInvAlEntry(invAdjustmentLine.getInvItemLocation(), invAdjustment, (invAdjustmentLine.getAlUnitCost()) * -1, EJBCommon.TRUE, companyCode);
            }

            invAdjustment.setAdjVoid(EJBCommon.TRUE);

            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), branchCode, companyCode);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void generateCostVariance(LocalInvItemLocation invItemLocation, double CST_VRNC_VL, String ADJ_RFRNC_NMBR, String ADJ_DSCRPTN, Date ADJ_DT, String USR_NM, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApVoucherEntryController generateCostVariance");
    }

    private void regenerateCostVariance(Collection invCostings, LocalInvCosting invCosting, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApVoucherEntryController regenerateCostVariance");
    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL, Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ApVoucherEntryController addInvDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);

            } catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_RVRSL, EJBCommon.FALSE, companyCode);
            invDistributionRecord.setInvAdjustment(invAdjustment);
            invDistributionRecord.setInvChartOfAccount(glChartOfAccount);

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ApDebitMemoEntryController executeInvAdjPost");

        try {

            // validate if adjustment is already deleted

            LocalInvAdjustment invAdjustment = null;

            try {

                invAdjustment = invAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted or void

            if (invAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                if (invAdjustment.getAdjVoid() != EJBCommon.TRUE) {
                    throw new GlobalTransactionAlreadyPostedException();
                }
            }

            Collection invAdjustmentLines = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);
            } else {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
            }

            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invAdjustment.getAdjDate(), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(), invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), branchCode, companyCode);

                this.postInvAdjustmentToInventory(invAdjustmentLine, invAdjustment.getAdjDate(), 0, invAdjustmentLine.getAlUnitCost(), invCosting.getCstRemainingQuantity(), invCosting.getCstRemainingValue() + invAdjustmentLine.getAlUnitCost(), branchCode, companyCode);
            }

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate if date has no period and period is closed

            LocalGlSetOfBook glJournalSetOfBook = null;

            try {

                glJournalSetOfBook = glSetOfBookHome.findByDate(invAdjustment.getAdjDate(), companyCode);

            } catch (FinderException ex) {

                throw new GlJREffectiveDateNoPeriodExistException();
            }

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), invAdjustment.getAdjDate(), companyCode);

            if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                throw new GlJREffectiveDatePeriodClosedException();
            }

            // check if invoice is balance if not check suspense posting

            LocalGlJournalLine glOffsetJournalLine = null;

            Collection invDistributionRecords = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {

                invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);

            } else {

                invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
            }

            Iterator j = invDistributionRecords.iterator();

            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

            while (j.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) j.next();

                double DR_AMNT = 0d;

                DR_AMNT = invDistributionRecord.getDrAmount();

                if (invDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

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

                    glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("INVENTORY", "INVENTORY ADJUSTMENTS", companyCode);

                } catch (FinderException ex) {

                    throw new GlobalJournalNotBalanceException();
                }

                if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                } else {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
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

                glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", branchCode, companyCode);

            } catch (FinderException ex) {

            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(), invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d, null, invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode, companyCode);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("INVENTORY", companyCode);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("INVENTORY ADJUSTMENTS", companyCode);
            glJournal.setGlJournalCategory(glJournalCategory);

            if (glJournalBatch != null) {

                glJournal.setGlJournalBatch(glJournalBatch);
            }

            // create journal lines

            j = invDistributionRecords.iterator();

            while (j.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) j.next();

                double DR_AMNT = 0d;

                DR_AMNT = invDistributionRecord.getDrAmount();

                LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(), invDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                glJournalLine.setGlChartOfAccount(invDistributionRecord.getInvChartOfAccount());

                glJournalLine.setGlJournal(glJournal);

                invDistributionRecord.setDrImported(EJBCommon.TRUE);
            }

            if (glOffsetJournalLine != null) {

                // glJournal.addGlJournalLine(glOffsetJournalLine);
                glOffsetJournalLine.setGlJournal(glJournal);
            }

            // post journal to gl

            Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
            i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

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
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), branchCode, companyCode);

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

            invAdjustment.setAdjPosted(EJBCommon.TRUE);

        } catch (GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyPostedException |
                 GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalInvAdjustmentLine addInvAlEntry(LocalInvItemLocation invItemLocation, LocalInvAdjustment invAdjustment, double CST_VRNC_VL, byte AL_VD, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean addInvAlEntry");

        try {

            // create dr entry
            LocalInvAdjustmentLine invAdjustmentLine = null;
            invAdjustmentLine = invAdjustmentLineHome.create(CST_VRNC_VL, null, null, 0, 0, AL_VD, companyCode);

            // map adjustment, unit of measure, item location
            invAdjustmentLine.setInvAdjustment(invAdjustment);
            invAdjustmentLine.setInvUnitOfMeasure(invItemLocation.getInvItem().getInvUnitOfMeasure());
            invAdjustmentLine.setInvItemLocation(invItemLocation);

            return invAdjustmentLine;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalInvAdjustment saveInvAdjustment(String ADJ_RFRNC_NMBR, String ADJ_DSCRPTN, Date ADJ_DATE, String USR_NM, Integer branchCode, Integer companyCode) {

        Debug.print("ApVoucherEntryController saveInvAdjustment");

        try {

            // generate adj document number
            String ADJ_DCMNT_NMBR = null;

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            try {

                adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV ADJUSTMENT", companyCode);

            } catch (FinderException ex) {

            }

            try {

                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);

            } catch (FinderException ex) {

            }

            while (true) {

                if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                    try {

                        invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode);
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                    } catch (FinderException ex) {

                        ADJ_DCMNT_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                        break;
                    }

                } else {

                    try {

                        invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode);
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                    } catch (FinderException ex) {

                        ADJ_DCMNT_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                        break;
                    }
                }
            }

            return invAdjustmentHome.create(ADJ_DCMNT_NMBR, ADJ_RFRNC_NMBR, ADJ_DSCRPTN, ADJ_DATE, "COST-VARIANCE", "N/A", EJBCommon.FALSE, USR_NM, ADJ_DATE, USR_NM, ADJ_DATE, null, null, USR_NM, ADJ_DATE, null, null, EJBCommon.TRUE, EJBCommon.FALSE, branchCode, companyCode);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT, double CST_ADJST_QTY, double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer branchCode, Integer companyCode) {

        Debug.print("ApVoucherEntryController postInvAdjustmentToInventory");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = invAdjustmentLine.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_ADJST_QTY = EJBCommon.roundIt(CST_ADJST_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ADJST_CST = EJBCommon.roundIt(CST_ADJST_CST, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (CST_ADJST_QTY < 0) {

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(CST_ADJST_QTY));
            }

            // create costing

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, CST_ADJST_QTY > 0 ? CST_ADJST_QTY : 0, branchCode, companyCode);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);

            invCosting.setCstQuantity(CST_ADJST_QTY);
            invCosting.setCstCost(CST_ADJST_CST);

            // propagate balance if necessary

            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

            for (Object costing : invCostings) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ADJST_QTY);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ADJST_CST);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApDebitMemoEntryControllerBean ejbCreate");
    }

}