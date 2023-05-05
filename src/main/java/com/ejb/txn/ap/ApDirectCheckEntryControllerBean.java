/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApDirectCheckEntryControllerBean
 * @created February 23, 2004, 5:30 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ap.*;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.*;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.ap.ApCHKCheckNumberNotUniqueException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ap.ApCheckDetails;
import com.util.ap.ApTaxCodeDetails;
import com.util.mod.ap.ApModCheckDetails;
import com.util.mod.ap.ApModDistributionRecordDetails;
import com.util.mod.ap.ApModSupplierDetails;
import com.util.mod.ap.ApModVoucherLineItemDetails;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;
import com.util.mod.inv.InvModLineItemDetails;
import com.util.mod.inv.InvModTagListDetails;
import com.util.mod.inv.InvModUnitOfMeasureDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Stateless(name = "ApDirectCheckEntryControllerEJB")
public class ApDirectCheckEntryControllerBean extends EJBContextClass implements ApDirectCheckEntryController {

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
    private ILocalGlInvestorAccountBalanceHome glInvestorAccountBalanceHome;
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
    private LocalAdBranchApTaxCodeHome adBranchApTaxCodeHome;
    @EJB
    private LocalAdBranchBankAccountHome adBranchBankAccountHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalAdBranchSupplierHome adBranchSupplierHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalApCheckBatchHome apCheckBatchHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApSupplierBalanceHome apSupplierBalanceHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;
    @EJB
    private LocalApVoucherLineItemHome apVoucherLineItemHome;
    @EJB
    private LocalApWithholdingTaxCodeHome apWithholdingTaxCodeHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;
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
    private LocalInvLineItemTemplateHome invLineItemTemplateHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvTagHome invTagHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;


    public ArrayList getGlFcAllWithDefault(Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean getGlFcAllWithDefault");

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

    public ArrayList getAdPytAll(Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean getAdPytAll");

        ArrayList list = new ArrayList();

        try {

            Collection adPaymentTerms = adPaymentTermHome.findEnabledPytAll(companyCode);

            for (Object paymentTerm : adPaymentTerms) {

                LocalAdPaymentTerm adPaymentTerm = (LocalAdPaymentTerm) paymentTerm;

                list.add(adPaymentTerm.getPytName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBaAll(Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean getAdBaAll");

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

    public ArrayList getApTcAll(Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean getApTcAll");

        ArrayList list = new ArrayList();

        try {

            Collection apTaxCodes = apTaxCodeHome.findEnabledTcAll(companyCode);

            for (Object taxCode : apTaxCodes) {

                LocalApTaxCode apTaxCode = (LocalApTaxCode) taxCode;

                list.add(apTaxCode.getTcName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApTaxCodeDetails getApTcByTcName(String TC_NM, Integer companyCode) {

        Debug.print("ApPurchaseOrderEntryControllerBean getArTcByTcName");

        ArrayList list = new ArrayList();

        try {

            LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(TC_NM, companyCode);

            ApTaxCodeDetails details = new ApTaxCodeDetails();
            details.setTcType(apTaxCode.getTcType());
            details.setTcRate(apTaxCode.getTcRate());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApWtcAll(Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean getApWtcAll");

        ArrayList list = new ArrayList();

        try {

            Collection apWithholdingTaxCodes = apWithholdingTaxCodeHome.findEnabledWtcAll(companyCode);

            for (Object withholdingTaxCode : apWithholdingTaxCodes) {

                LocalApWithholdingTaxCode apWithholdingTaxCode = (LocalApWithholdingTaxCode) withholdingTaxCode;

                list.add(apWithholdingTaxCode.getWtcName());
            }

            return list;

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

    public ApModCheckDetails getApChkByChkCode(Integer CHK_CODE, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApDirectCheckEntryControllerBean getApChkByChkCode");

        try {

            LocalApCheck apCheck = null;

            try {

                apCheck = apCheckHome.findByPrimaryKey(CHK_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get voucher line items if any

            Collection apVoucherLineItems = apCheck.getApVoucherLineItems();

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
                    vliDetails.setVliDiscount1(apVoucherLineItem.getVliDiscount1());
                    vliDetails.setVliDiscount2(apVoucherLineItem.getVliDiscount2());
                    vliDetails.setVliDiscount3(apVoucherLineItem.getVliDiscount3());
                    vliDetails.setVliDiscount4(apVoucherLineItem.getVliDiscount4());
                    vliDetails.setVliTotalDiscount(apVoucherLineItem.getVliTotalDiscount());
                    vliDetails.setVliMisc(apVoucherLineItem.getVliMisc());
                    if (apCheck.getApTaxCode().getTcType().equalsIgnoreCase("INCLUSIVE")) {
                        vliDetails.setVliAmount(apVoucherLineItem.getVliAmount() + apVoucherLineItem.getVliTaxAmount());
                    } else {
                        vliDetails.setVliAmount(apVoucherLineItem.getVliAmount());
                    }

                    ArrayList tagList = new ArrayList();

                    if (apVoucherLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() == EJBCommon.TRUE) {

                        tagList = this.getInvTagList(apVoucherLineItem);

                        vliDetails.setTagList(tagList);
                        vliDetails.setIsTraceMisc(true);
                    }

                    list.add(vliDetails);
                }

            } else {

                // get distribution records

                Collection apDistributionRecords = apDistributionRecordHome.findByChkCode(apCheck.getChkCode(), companyCode);

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

            ApModCheckDetails mChkDetails = new ApModCheckDetails();

            mChkDetails.setChkCode(apCheck.getChkCode());
            mChkDetails.setChkDate(apCheck.getChkDate());
            mChkDetails.setChkCheckDate(apCheck.getChkCheckDate());
            mChkDetails.setChkNumber(apCheck.getChkNumber());
            mChkDetails.setChkDocumentNumber(apCheck.getChkDocumentNumber());
            mChkDetails.setChkReferenceNumber(apCheck.getChkReferenceNumber());
            mChkDetails.setChkInfoType(apCheck.getChkInfoType());
            mChkDetails.setChkInfoBioNumber(apCheck.getChkInfoBioNumber());
            mChkDetails.setChkInfoBioDescription(apCheck.getChkInfoBioDescription());
            mChkDetails.setChkInfoTypeStatus(apCheck.getChkInfoTypeStatus());
            mChkDetails.setChkInfoRequestStatus(apCheck.getChkInfoRequestStatus());
            mChkDetails.setChkBillAmount(apCheck.getChkBillAmount());
            mChkDetails.setChkAmount(apCheck.getChkAmount());
            mChkDetails.setChkVoid(apCheck.getChkVoid());
            mChkDetails.setChkCrossCheck(apCheck.getChkCrossCheck());
            mChkDetails.setChkTotalDebit(TOTAL_DEBIT);
            mChkDetails.setChkTotalCredit(TOTAL_CREDIT);
            mChkDetails.setChkDescription(apCheck.getChkDescription());
            mChkDetails.setChkInfoType(apCheck.getChkInfoType());
            mChkDetails.setChkInfoBioNumber(apCheck.getChkInfoBioNumber());
            mChkDetails.setChkInfoBioDescription(apCheck.getChkInfoBioDescription());
            mChkDetails.setChkInfoTypeStatus(apCheck.getChkInfoTypeStatus());
            mChkDetails.setChkInfoRequestStatus(apCheck.getChkInfoRequestStatus());
            mChkDetails.setChkConversionDate(apCheck.getChkConversionDate());
            mChkDetails.setChkConversionRate(apCheck.getChkConversionRate());
            mChkDetails.setChkApprovalStatus(apCheck.getChkApprovalStatus());
            mChkDetails.setChkReasonForRejection(apCheck.getChkReasonForRejection());
            mChkDetails.setChkPosted(apCheck.getChkPosted());
            mChkDetails.setChkVoid(apCheck.getChkVoid());
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
            mChkDetails.setChkTcName(apCheck.getApTaxCode().getTcName());
            mChkDetails.setChkTcType(apCheck.getApTaxCode().getTcType());
            mChkDetails.setChkTcRate(apCheck.getApTaxCode().getTcRate());
            mChkDetails.setChkWtcName(apCheck.getApWithholdingTaxCode().getWtcName());
            mChkDetails.setChkCbName(apCheck.getApCheckBatch() != null ? apCheck.getApCheckBatch().getCbName() : null);
            mChkDetails.setChkSplName(apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName());
            mChkDetails.setChkMemo(apCheck.getChkMemo());

            mChkDetails.setChkInvtInscribedStock(apCheck.getChkInvtInscribedStock());
            mChkDetails.setChkInvtTreasuryBill(apCheck.getChkInvtTreasuryBill());
            mChkDetails.setChkInvtNextRunDate(apCheck.getChkInvtNextRunDate());
            mChkDetails.setChkInvtSettlementDate(apCheck.getChkInvtSettlementDate());
            mChkDetails.setChkInvtMaturityDate(apCheck.getChkInvtMaturityDate());
            mChkDetails.setChkInvtBidYield(apCheck.getChkInvtBidYield());
            mChkDetails.setChkInvtCouponRate(apCheck.getChkInvtCouponRate());
            mChkDetails.setChkInvtSettleAmount(apCheck.getChkInvtSettleAmount());
            mChkDetails.setChkInvtFaceValue(apCheck.getChkInvtFaceValue());
            mChkDetails.setChkInvtPremiumAmount(apCheck.getChkInvtPremiumAmount());

            mChkDetails.setChkSupplierClassName(apCheck.getApSupplier().getApSupplierClass().getScName());
            mChkDetails.setChkScInvestment(apCheck.getApSupplier().getApSupplierClass().getScIsInvestment());

            mChkDetails.setChkPytName(apCheck.getAdPaymentTerm() != null ? apCheck.getAdPaymentTerm().getPytName() : null);

            if (!apVoucherLineItems.isEmpty()) {

                mChkDetails.setChkVliList(list);

            } else {

                mChkDetails.setChkDrList(list);
            }

            return mChkDetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModSupplierDetails getApSplBySplSupplierCode(String SPL_SPPLR_CODE, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApDirectCheckEntryControllerBean getApSplBySplSupplierCode");

        try {

            LocalApSupplier apSupplier = null;

            try {

                apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, companyCode);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ApModSupplierDetails mdetails = new ApModSupplierDetails();

            mdetails.setSplStBaName(apSupplier.getAdBankAccount().getBaCode() != null ? apSupplier.getAdBankAccount().getBaName() : null);
            mdetails.setSplScWtcName(apSupplier.getApSupplierClass().getApWithholdingTaxCode() != null ? apSupplier.getApSupplierClass().getApWithholdingTaxCode().getWtcName() : null);
            mdetails.setSplName(apSupplier.getSplName());

            mdetails.setSplSupplierClass(apSupplier.getApSupplierClass().getScName());

            mdetails.setSplScInvestment(apSupplier.getApSupplierClass().getScIsInvestment());
            mdetails.setSplScLoan(apSupplier.getApSupplierClass().getScIsLoan());

            if (apSupplier.getApSupplierClass().getApTaxCode() != null) {

                mdetails.setSplScTcName(apSupplier.getApSupplierClass().getApTaxCode().getTcName());
                mdetails.setSplScTcType(apSupplier.getApSupplierClass().getApTaxCode().getTcType());
                mdetails.setSplScTcRate(apSupplier.getApSupplierClass().getApTaxCode().getTcRate());
            }

            if (apSupplier.getInvLineItemTemplate() != null) {
                mdetails.setSplLitName(apSupplier.getInvLineItemTemplate().getLitName());
            }

            return mdetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApDrBySplSupplierCodeAndTcNameAndWtcNameAndChkBillAmountAndBaName(String SPL_SPPLR_CODE, String TC_NM, String WTC_NM, double CHK_BLL_AMNT, String BA_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApDirectCheckEntryControllerBean getApDrBySplSupplierCodeAndTcNameAndWtcNameAndChkBillAmountAndBaName");

        ArrayList list = new ArrayList();

        try {

            LocalApSupplier apSupplier = null;
            LocalApTaxCode apTaxCode = null;
            LocalApWithholdingTaxCode apWithholdingTaxCode = null;

            try {

                apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, companyCode);
                apTaxCode = apTaxCodeHome.findByTcName(TC_NM, companyCode);
                apWithholdingTaxCode = apWithholdingTaxCodeHome.findByWtcName(WTC_NM, companyCode);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            double NET_AMOUNT = 0d;
            double TAX_AMOUNT = 0d;
            double W_TAX_AMOUNT = 0d;
            short LINE_NUMBER = 0;

            // create dr net expense
            if (apTaxCode.getTcType().equals("INCLUSIVE")) {

                NET_AMOUNT = EJBCommon.roundIt(CHK_BLL_AMNT / (1 + (apTaxCode.getTcRate() / 100)), this.getGlFcPrecisionUnit(companyCode));

            } else {

                // tax exclusive, none, zero rated or exempt

                NET_AMOUNT = CHK_BLL_AMNT;
            }

            ApModDistributionRecordDetails mdetails = new ApModDistributionRecordDetails();
            mdetails.setDrLine(++LINE_NUMBER);
            mdetails.setDrClass("EXPENSE");
            mdetails.setDrDebit(EJBCommon.TRUE);
            mdetails.setDrAmount(NET_AMOUNT);

            LocalAdBranchSupplier adBranchSupplier = null;

            try {

                adBranchSupplier = adBranchSupplierHome.findBSplBySplCodeAndBrCode(apSupplier.getSplCode(), AD_BRNCH, companyCode);

            } catch (FinderException ex) {

            }

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchSupplier.getBsplGlCoaExpenseAccount());
            mdetails.setDrCoaAccountNumber(glChartOfAccount.getCoaAccountNumber());
            mdetails.setDrCoaAccountDescription(glChartOfAccount.getCoaAccountDescription());

            list.add(mdetails);

            // create tax line if necessary

            if (!apTaxCode.getTcType().equals("NONE") && !apTaxCode.getTcType().equals("EXEMPT")) {

                if (apTaxCode.getTcType().equals("INCLUSIVE")) {

                    TAX_AMOUNT = EJBCommon.roundIt(CHK_BLL_AMNT - NET_AMOUNT, this.getGlFcPrecisionUnit(companyCode));

                } else if (apTaxCode.getTcType().equals("EXCLUSIVE")) {

                    TAX_AMOUNT = EJBCommon.roundIt(CHK_BLL_AMNT * apTaxCode.getTcRate() / 100, this.getGlFcPrecisionUnit(companyCode));

                } else {

                    // tax none zero-rated or exempt

                }

                mdetails = new ApModDistributionRecordDetails();
                mdetails.setDrLine(++LINE_NUMBER);
                mdetails.setDrClass("TAX");
                mdetails.setDrDebit(EJBCommon.TRUE);
                mdetails.setDrAmount(TAX_AMOUNT);

                LocalAdBranchApTaxCode adBranchTaxCode = null;

                try {

                    adBranchTaxCode = adBranchApTaxCodeHome.findBtcByTcCodeAndBrCode(apTaxCode.getTcCode(), AD_BRNCH, companyCode);

                } catch (FinderException ex) {

                }
                if (adBranchTaxCode != null) {
                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchTaxCode.getBtcGlCoaTaxCode());

                    mdetails.setDrCoaAccountNumber(glChartOfAccount.getCoaAccountNumber());
                    mdetails.setDrCoaAccountDescription(glChartOfAccount.getCoaAccountDescription());

                } else {
                    mdetails.setDrCoaAccountNumber(apTaxCode.getGlChartOfAccount().getCoaAccountNumber());
                    mdetails.setDrCoaAccountDescription(apTaxCode.getGlChartOfAccount().getCoaAccountDescription());
                }
                list.add(mdetails);
            }

            // create withholding tax if necessary

            if (apWithholdingTaxCode.getWtcRate() != 0) {
                W_TAX_AMOUNT = EJBCommon.roundIt(NET_AMOUNT * (apWithholdingTaxCode.getWtcRate() / 100), this.getGlFcPrecisionUnit(companyCode));

                mdetails = new ApModDistributionRecordDetails();
                mdetails.setDrLine(++LINE_NUMBER);
                mdetails.setDrClass("W-TAX");
                mdetails.setDrDebit(EJBCommon.FALSE);
                mdetails.setDrAmount(W_TAX_AMOUNT);

                mdetails.setDrCoaAccountNumber(apWithholdingTaxCode.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(apWithholdingTaxCode.getGlChartOfAccount().getCoaAccountDescription());

                list.add(mdetails);
            }

            // create cash account

            mdetails = new ApModDistributionRecordDetails();
            mdetails.setDrLine(++LINE_NUMBER);
            mdetails.setDrClass("CASH");
            mdetails.setDrDebit(EJBCommon.FALSE);
            mdetails.setDrAmount(NET_AMOUNT + TAX_AMOUNT - W_TAX_AMOUNT);

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);

            // add branch bank account
            LocalAdBranchBankAccount adBranchBankAccount = null;

            try {
                adBranchBankAccount = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(adBankAccount.getBaCode(), AD_BRNCH, companyCode);

            } catch (FinderException ex) {

            }

            LocalGlChartOfAccount glChartOfAccountBA = glChartOfAccountHome.findByPrimaryKey(adBranchBankAccount.getBbaGlCoaCashAccount());

            mdetails.setDrCoaAccountNumber(glChartOfAccountBA.getCoaAccountNumber());
            mdetails.setDrCoaAccountDescription(glChartOfAccountBA.getCoaAccountDescription());
            list.add(mdetails);

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveApChkEntry(ApCheckDetails details, String PYT_NM, String BA_NM, String TC_NM, String WTC_NM, String FC_NM, String SPL_SPPLR_CODE, String CB_NM, ArrayList drList, boolean isDraft, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, ApCHKCheckNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalBranchAccountNumberInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("ApDirectCheckEntryControllerBean saveApChkEntry");

        LocalApCheck apCheck = null;

        try {

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

                        ApModDistributionRecordDetails mDrDetails = new ApModDistributionRecordDetails();
                        mDrDetails.setDrLine(apCheck.getApDrNextLine());
                        mDrDetails.setDrClass(apDistributionRecord.getDrClass());
                        mDrDetails.setDrDebit(apDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE);
                        mDrDetails.setDrAmount(apDistributionRecord.getDrAmount());
                        mDrDetails.setDrReversed(EJBCommon.TRUE);
                        mDrDetails.setDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());

                        this.addApDrEntry(mDrDetails, apCheck, AD_BRNCH, companyCode);
                    }

                    LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

                    if (CHK_APPRVL_STATUS != null && CHK_APPRVL_STATUS.equals("N/A") && adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                        apCheck.setChkVoid(EJBCommon.TRUE);
                        this.executeApChkPost(apCheck.getChkCode(), details.getChkLastModifiedBy(), AD_BRNCH, companyCode);
                    }

                    // set void approval status

                    apCheck.setChkVoidApprovalStatus(CHK_APPRVL_STATUS);
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
                    LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

                    if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getChkConversionDate(), companyCode);

                    } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getChkConversionDate(), companyCode);
                    }
                }

            } catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // create direct check
            if (details.getChkCode() == null) {

                apCheck = apCheckHome.create("DIRECT", details.getChkDescription(), details.getChkDate(), details.getChkCheckDate(), details.getChkNumber(), details.getChkDocumentNumber(), details.getChkReferenceNumber(), details.getChkInfoType(), details.getChkInfoBioNumber(), details.getChkInfoBioDescription(), details.getChkInfoTypeStatus(), details.getChkInfoRequestStatus(), details.getChkInvtInscribedStock(), details.getChkInvtTreasuryBill(), details.getChkInvtNextRunDate(), details.getChkInvtSettlementDate(), details.getChkInvtMaturityDate(), details.getChkInvtBidYield(), details.getChkInvtCouponRate(), details.getChkInvtSettleAmount(), details.getChkInvtFaceValue(), details.getChkInvtPremiumAmount(), details.getChkLoan(), details.getChkLoanGenerated(), details.getChkConversionDate(), details.getChkConversionRate(), details.getChkBillAmount(), details.getChkAmount(), null, null, EJBCommon.FALSE, EJBCommon.FALSE, details.getChkCrossCheck(), null, EJBCommon.FALSE, details.getChkCreatedBy(), details.getChkDateCreated(), details.getChkLastModifiedBy(), details.getChkDateLastModified(), null, null, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, details.getChkMemo(), null, AD_BRNCH, companyCode);

            } else {

                apCheck.setChkDescription(details.getChkDescription());

                apCheck.setChkInfoType(details.getChkInfoType());
                apCheck.setChkInfoBioNumber(details.getChkInfoBioNumber());
                apCheck.setChkInfoBioDescription(details.getChkInfoBioDescription());
                apCheck.setChkInfoTypeStatus(details.getChkInfoTypeStatus());
                apCheck.setChkInfoRequestStatus(details.getChkInfoRequestStatus());

                apCheck.setChkDate(details.getChkDate());
                apCheck.setChkCheckDate(details.getChkCheckDate());
                apCheck.setChkNumber(details.getChkNumber());
                apCheck.setChkDocumentNumber(details.getChkDocumentNumber());
                apCheck.setChkReferenceNumber(details.getChkReferenceNumber());
                apCheck.setChkConversionDate(details.getChkConversionDate());
                apCheck.setChkConversionRate(details.getChkConversionRate());
                apCheck.setChkBillAmount(details.getChkBillAmount());
                apCheck.setChkAmount(details.getChkAmount());
                apCheck.setChkCrossCheck(details.getChkCrossCheck());
                apCheck.setChkLastModifiedBy(details.getChkLastModifiedBy());
                apCheck.setChkDateLastModified(details.getChkDateLastModified());
                apCheck.setChkReasonForRejection(null);
                apCheck.setChkMemo(details.getChkMemo());
                apCheck.setChkSupplierName(null);

                apCheck.setChkInvtInscribedStock(details.getChkInvtInscribedStock());
                apCheck.setChkInvtTreasuryBill(details.getChkInvtTreasuryBill());
                apCheck.setChkInvtNextRunDate(details.getChkInvtNextRunDate());
                apCheck.setChkInvtSettlementDate(details.getChkInvtSettlementDate());
                apCheck.setChkInvtMaturityDate(details.getChkInvtMaturityDate());
                apCheck.setChkInvtBidYield(details.getChkInvtBidYield());
                apCheck.setChkInvtCouponRate(details.getChkInvtCouponRate());
                apCheck.setChkInvtSettleAmount(details.getChkInvtSettleAmount());
                apCheck.setChkInvtFaceValue(details.getChkInvtFaceValue());
                apCheck.setChkInvtPremiumAmount(details.getChkInvtPremiumAmount());

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

            LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(TC_NM, companyCode);
            apCheck.setApTaxCode(apTaxCode);

            LocalApWithholdingTaxCode apWithholdingTaxCode = apWithholdingTaxCodeHome.findByWtcName(WTC_NM, companyCode);
            apCheck.setApWithholdingTaxCode(apWithholdingTaxCode);

            try {
                LocalApCheckBatch apCheckBatch = apCheckBatchHome.findByDirectCbName(CB_NM, AD_BRNCH, companyCode);
                apCheck.setApCheckBatch(apCheckBatch);
            } catch (FinderException ex) {
            }

            try {

                LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, companyCode);
                apCheck.setAdPaymentTerm(adPaymentTerm);
            } catch (Exception ex) {
            }

            // remove all voucher line items

            Collection apVoucherLineItems = apCheck.getApVoucherLineItems();

            Iterator i = apVoucherLineItems.iterator();

            while (i.hasNext()) {

                LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) i.next();

                i.remove();

                // apVoucherLineItem.entityRemove();
                em.remove(apVoucherLineItem);
            }

            // remove all distribution records

            Collection apDistributionRecords = apCheck.getApDistributionRecords();

            i = apDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                i.remove();

                em.remove(apDistributionRecord);
            }

            // add new distribution records

            i = drList.iterator();

            while (i.hasNext()) {

                ApModDistributionRecordDetails mDrDetails = (ApModDistributionRecordDetails) i.next();
                mDrDetails.setDrReversed(EJBCommon.FALSE);

                this.addApDrEntry(mDrDetails, apCheck, AD_BRNCH, companyCode);
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

                    CHK_APPRVL_STATUS = apApprovalController.getApprovalStatus(adUser.getUsrDept(), apCheck.getChkLastModifiedBy(), adUser.getUsrDescription(), "AP CHECK", apCheck.getChkCode(), apCheck.getChkDocumentNumber(), apCheck.getChkDate(), apCheck.getChkAmount(), AD_BRNCH, companyCode);
                }
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (CHK_APPRVL_STATUS != null && CHK_APPRVL_STATUS.equals("N/A") && adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeApChkPost(apCheck.getChkCode(), details.getChkLastModifiedBy(), AD_BRNCH, companyCode);
            }

            // set check approval status

            apCheck.setChkApprovalStatus(CHK_APPRVL_STATUS);

            return apCheck.getChkCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException | GlJREffectiveDateNoPeriodExistException |
                 GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
                 GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
                 GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
                 GlobalBranchAccountNumberInvalidException | GlobalConversionDateNotExistException |
                 ApCHKCheckNumberNotUniqueException | GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveApChkVliEntry(ApCheckDetails details, String BA_NM, String TC_NM, String WTC_NM, String FC_NM, String SPL_SPPLR_CODE, String CB_NM, ArrayList vliList, boolean isDraft, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, ApCHKCheckNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApDirectCheckEntryControllerBean saveApChkVliEntry");

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

                        ApModDistributionRecordDetails mDrDetails = new ApModDistributionRecordDetails();
                        mDrDetails.setDrLine(apCheck.getApDrNextLine());
                        mDrDetails.setDrClass(apDistributionRecord.getDrClass());
                        mDrDetails.setDrDebit(apDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE);
                        mDrDetails.setDrAmount(apDistributionRecord.getDrAmount());
                        mDrDetails.setDrReversed(EJBCommon.TRUE);
                        mDrDetails.setDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());

                        this.addApDrEntry(mDrDetails, apCheck, AD_BRNCH, companyCode);
                    }

                    if (CHK_APPRVL_STATUS != null && CHK_APPRVL_STATUS.equals("N/A") && adPreference.getPrfApGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                        apCheck.setChkVoid(EJBCommon.TRUE);
                        this.executeApChkPost(apCheck.getChkCode(), details.getChkLastModifiedBy(), AD_BRNCH, companyCode);
                    }

                    // set void approval status

                    apCheck.setChkVoidApprovalStatus(CHK_APPRVL_STATUS);
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
                }

            } catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // used in checking if check should re-generate distribution records and
            // re-calculate taxes
            boolean isRecalculate = true;

            // create direct check

            if (details.getChkCode() == null) {

                apCheck = apCheckHome.create("DIRECT", details.getChkDescription(), details.getChkDate(), details.getChkCheckDate(), details.getChkNumber(), details.getChkDocumentNumber(), details.getChkReferenceNumber(), details.getChkInfoType(), details.getChkInfoBioNumber(), details.getChkInfoBioDescription(), details.getChkInfoTypeStatus(), details.getChkInfoRequestStatus(), EJBCommon.FALSE, EJBCommon.FALSE, details.getChkInvtNextRunDate(), details.getChkInvtSettlementDate(), details.getChkInvtMaturityDate(), details.getChkInvtBidYield(), details.getChkInvtCouponRate(), details.getChkInvtSettleAmount(), details.getChkInvtFaceValue(), details.getChkInvtPremiumAmount(), details.getChkLoan(), details.getChkLoanGenerated(), details.getChkConversionDate(), details.getChkConversionRate(), 0d, 0d, null, null, EJBCommon.FALSE, EJBCommon.FALSE, details.getChkCrossCheck(), null, EJBCommon.FALSE, details.getChkCreatedBy(), details.getChkDateCreated(), details.getChkLastModifiedBy(), details.getChkDateLastModified(), null, null, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, details.getChkMemo(), null, AD_BRNCH, companyCode);

            } else {

                // check if critical fields are changed

                if (!apCheck.getApTaxCode().getTcName().equals(TC_NM) || !apCheck.getApWithholdingTaxCode().getWtcName().equals(WTC_NM) || !apCheck.getApSupplier().getSplSupplierCode().equals(SPL_SPPLR_CODE) || vliList.size() != apCheck.getApVoucherLineItems().size() || !(apCheck.getChkDate().equals(details.getChkDate()))) {

                    isRecalculate = true;

                } else if (vliList.size() == apCheck.getApVoucherLineItems().size()) {

                    Iterator ilIter = apCheck.getApVoucherLineItems().iterator();
                    Iterator ilListIter = vliList.iterator();

                    while (ilIter.hasNext()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) ilIter.next();
                        ApModVoucherLineItemDetails mdetails = (ApModVoucherLineItemDetails) ilListIter.next();

                        if (!apVoucherLineItem.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getVliIiName()) || !apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getVliLocName()) || !apVoucherLineItem.getInvUnitOfMeasure().getUomName().equals(mdetails.getVliUomName()) || apVoucherLineItem.getVliQuantity() != mdetails.getVliQuantity() || apVoucherLineItem.getVliUnitCost() != mdetails.getVliUnitCost() || apVoucherLineItem.getVliTotalDiscount() != mdetails.getVliTotalDiscount()) {

                            isRecalculate = true;
                            break;
                        }

                        // isRecalculate = false;

                    }

                } else {

                    // isRecalculate = false;

                }

                apCheck.setChkDescription(details.getChkDescription());
                apCheck.setChkInfoType(details.getChkInfoType());
                apCheck.setChkInfoBioNumber(details.getChkInfoBioNumber());
                apCheck.setChkInfoBioDescription(details.getChkInfoBioDescription());
                apCheck.setChkInfoTypeStatus(details.getChkInfoTypeStatus());
                apCheck.setChkInfoRequestStatus(details.getChkInfoRequestStatus());
                apCheck.setChkDate(details.getChkDate());
                apCheck.setChkCheckDate(details.getChkCheckDate());
                apCheck.setChkNumber(details.getChkNumber());
                apCheck.setChkDocumentNumber(details.getChkDocumentNumber());
                apCheck.setChkReferenceNumber(details.getChkReferenceNumber());
                apCheck.setChkConversionDate(details.getChkConversionDate());
                apCheck.setChkConversionRate(details.getChkConversionRate());
                apCheck.setChkBillAmount(details.getChkBillAmount());
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

            LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(TC_NM, companyCode);
            apCheck.setApTaxCode(apTaxCode);

            LocalApWithholdingTaxCode apWithholdingTaxCode = apWithholdingTaxCodeHome.findByWtcName(WTC_NM, companyCode);
            apCheck.setApWithholdingTaxCode(apWithholdingTaxCode);

            try {

                LocalApCheckBatch apCheckBatch = apCheckBatchHome.findByDirectCbName(CB_NM, AD_BRNCH, companyCode);
                apCheck.setApCheckBatch(apCheckBatch);

            } catch (Exception e) {
                // TODO: handle exception
            }

            if (isRecalculate) {

                // remove all voucher line items

                Collection apVoucherLineItems = apCheck.getApVoucherLineItems();

                Iterator i = apVoucherLineItems.iterator();

                while (i.hasNext()) {

                    LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) i.next();

                    // remove all invTags
                    Collection invTags = apVoucherLineItem.getInvTags();

                    Iterator x = invTags.iterator();

                    while (x.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) x.next();

                        x.remove();

                        // invTag.entityRemove();
                        em.remove(invTag);
                    }

                    i.remove();

                    // apVoucherLineItem.entityRemove();
                    em.remove(apVoucherLineItem);
                }

                // remove all distribution records

                Collection apDistributionRecords = apCheck.getApDistributionRecords();

                i = apDistributionRecords.iterator();

                while (i.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                    i.remove();

                    // apDistributionRecord.entityRemove();
                    em.remove(apDistributionRecord);
                }

                // add new voucher line item and distribution record

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
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(apCheck.getChkDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    LocalApVoucherLineItem apVoucherLineItem = this.addApVliEntry(mVliDetails, apCheck, invItemLocation, companyCode);

                    // add inventory distributions

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(apVoucherLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);

                    } catch (FinderException ex) {

                    }

                    if (adBranchItemLocation != null) {

                        // Use AdBranchItemLocation account
                        this.addApDrVliEntry(apCheck.getApDrNextLine(), "EXPENSE", EJBCommon.TRUE, apVoucherLineItem.getVliAmount(), adBranchItemLocation.getBilCoaGlInventoryAccount(), apCheck, AD_BRNCH, companyCode);

                    } else {

                        // Use default account
                        this.addApDrVliEntry(apCheck.getApDrNextLine(), "EXPENSE", EJBCommon.TRUE, apVoucherLineItem.getVliAmount(), apVoucherLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), apCheck, AD_BRNCH, companyCode);
                    }

                    TOTAL_LINE += apVoucherLineItem.getVliAmount();
                    TOTAL_TAX += apVoucherLineItem.getVliTaxAmount();
                }

                // add tax distribution if necessary

                if (!apTaxCode.getTcType().equals("NONE") && !apTaxCode.getTcType().equals("EXEMPT")) {

                    LocalAdBranchApTaxCode adBranchTaxCode = null;
                    Debug.print("ArInvoiceEntryControllerBean saveArInvIliEntry J");
                    try {
                        adBranchTaxCode = adBranchApTaxCodeHome.findBtcByTcCodeAndBrCode(apCheck.getApTaxCode().getTcCode(), AD_BRNCH, companyCode);

                    } catch (FinderException ex) {

                    }

                    this.addApDrVliEntry(apCheck.getApDrNextLine(), "TAX", EJBCommon.TRUE, TOTAL_TAX, adBranchTaxCode.getBtcGlCoaTaxCode(), apCheck, AD_BRNCH, companyCode);
                }

                // add wtax distribution if necessary

                double W_TAX_AMOUNT = 0d;

                if (apWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfApWTaxRealization().equals("VOUCHER")) {

                    W_TAX_AMOUNT = EJBCommon.roundIt(TOTAL_LINE * (apWithholdingTaxCode.getWtcRate() / 100), this.getGlFcPrecisionUnit(companyCode));

                    this.addApDrVliEntry(apCheck.getApDrNextLine(), "W-TAX", EJBCommon.FALSE, W_TAX_AMOUNT, apWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), apCheck, AD_BRNCH, companyCode);
                }

                // add cash distribution
                LocalAdBranchBankAccount adBranchBankAccount = null;
                Debug.print("ArInvoiceEntryControllerBean saveArInvIliEntry J");
                try {
                    adBranchBankAccount = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(apCheck.getAdBankAccount().getBaCode(), AD_BRNCH, companyCode);

                } catch (FinderException ex) {

                }

                // add payable distribution

                this.addApDrVliEntry(apCheck.getApDrNextLine(), "CASH", EJBCommon.FALSE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT, adBranchBankAccount.getBbaGlCoaCashAccount(), apCheck, AD_BRNCH, companyCode);

                // set voucher amount due

                apCheck.setChkAmount(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT);

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

                }

                // start date validation
                if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                    Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(apCheck.getChkDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                    if (!invNegTxnCosting.isEmpty()) {
                        throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                    }
                }
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

                this.executeApChkPost(apCheck.getChkCode(), details.getChkLastModifiedBy(), AD_BRNCH, companyCode);
            }

            // set check approval status

            apCheck.setChkApprovalStatus(CHK_APPRVL_STATUS);

            return apCheck.getChkCode();

        } catch (GlobalRecordAlreadyDeletedException | AdPRFCoaGlVarianceAccountNotFoundException |
                 GlobalBranchAccountNumberInvalidException | GlobalInventoryDateException |
                 GlobalInvItemLocationNotFoundException | GlobalNoApprovalApproverFoundException |
                 GlobalNoApprovalRequesterFoundException | GlobalTransactionAlreadyVoidException |
                 GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
                 GlobalTransactionAlreadyApprovedException | GlobalConversionDateNotExistException |
                 ApCHKCheckNumberNotUniqueException | GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteApChkEntry(Integer CHK_CODE, String AD_USR, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("ApDirectCheckEntryControllerBean deleteApChkEntry");

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

        Debug.print("ApDirectCheckEntryControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByChkCode(Integer CHK_CODE, Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean getAdApprovalNotifiedUsersByChkCode");

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

        Debug.print("ApDirectCheckEntryControllerBean getAdPrfEnableApCheckBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfEnableApCheckBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApOpenCbAll(String DPRTMNT, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean getApOpenCbAll");

        ArrayList list = new ArrayList();

        try {

            Collection apCheckBatches = apCheckBatchHome.findOpenCbByCbType("DIRECT", AD_BRNCH, companyCode);
            if (DPRTMNT.equals("") || DPRTMNT.equals("default") || DPRTMNT.equals("NO RECORD FOUND")) {
                apCheckBatches = apCheckBatchHome.findOpenCbByCbType("DIRECT", AD_BRNCH, companyCode);

            } else {
                apCheckBatches = apCheckBatchHome.findOpenCbByCbTypeDepartment("DIRECT", DPRTMNT, AD_BRNCH, companyCode);
            }

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

    public ArrayList getInvLocAll(Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean getInvLocAll");

        Collection invLocations = null;
        ArrayList list = new ArrayList();

        try {

            invLocations = invLocationHome.findLocAll(companyCode);

            if (invLocations.isEmpty()) {

                return null;
            }

            for (Object location : invLocations) {

                LocalInvLocation invLocation = (LocalInvLocation) location;
                String details = invLocation.getLocName();

                list.add(details);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvUomByIiName(String II_NM, Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean getInvUomByIiName");

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

    public double getInvIiUnitCostByIiNameAndUomName(String II_NM, String UOM_NM, Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean getInvIiUnitCostByIiNameAndUomName");

        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(invItem.getIiUnitCost() * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(companyCode));

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean getInvGpQuantityPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfApUseSupplierPulldown(Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean getAdPrfApUseSupplierPulldown");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfApUseSupplierPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvLitByCstLitName(String CST_LIT_NAME, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApDirectCheckEntryControllerBean getInvLitByCstLitName");

        try {

            LocalInvLineItemTemplate invLineItemTemplate = null;

            try {

                invLineItemTemplate = invLineItemTemplateHome.findByLitName(CST_LIT_NAME, companyCode);

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

    public double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("ApDirectCheckEntryControllerBean getFrRateByFrNameAndFrDate");

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

    private void addApDrEntry(ApModDistributionRecordDetails mdetails, LocalApCheck apCheck, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ApDirectCheckEntryControllerBean addApDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate if coa exists

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(mdetails.getDrCoaAccountNumber(), AD_BRNCH, companyCode);

                if (glChartOfAccount.getCoaEnable() == EJBCommon.FALSE) {
                    throw new GlobalBranchAccountNumberInvalidException();
                }

            } catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.create(mdetails.getDrLine(), mdetails.getDrClass(), EJBCommon.roundIt(mdetails.getDrAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()), mdetails.getDrDebit(), EJBCommon.FALSE, mdetails.getDrReversed(), companyCode);
            apDistributionRecord.setApCheck(apCheck);
            apDistributionRecord.setGlChartOfAccount(glChartOfAccount);

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addApDrVliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalApCheck apCheck, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ApDirectCheckEntryControllerBean addApDrVliEntry");

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

            LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.create(DR_LN, DR_CLSS, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_DBT, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            apDistributionRecord.setApCheck(apCheck);
            apDistributionRecord.setGlChartOfAccount(glChartOfAccount);

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalApVoucherLineItem addApVliEntry(ApModVoucherLineItemDetails mdetails, LocalApCheck apCheck, LocalInvItemLocation invItemLocation, Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean addApVliEntry");

        try {

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode);

            double VLI_AMNT = 0d;
            double VLI_TAX_AMNT = 0d;

            // calculate net amount

            LocalApTaxCode apTaxCode = apCheck.getApTaxCode();

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

            apVoucherLineItem.setApCheck(apCheck);

            apVoucherLineItem.setInvItemLocation(invItemLocation);

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getVliUomName(), companyCode);

            apVoucherLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

            apVoucherLineItem.setVliMisc(mdetails.getVliMisc());

            if (mdetails.getIsTraceMic()) {

                this.createInvTagList(apVoucherLineItem, mdetails.getTagList(), companyCode);
            }

            return apVoucherLineItem;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
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

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("ApPaymentEntryControllerBean convertForeignToFunctionalCurrency");

        // get company and extended precision
        LocalAdCompany adCompany = null;
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

    private void executeApChkPost(Integer CHK_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApDirectCheckEntryControllerBean executeApChkPost");

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
                // POSTING DIRECT PAYMENT

                if (apCheck.getChkType().equals("DIRECT") && !apCheck.getApVoucherLineItems().isEmpty()) {

                    for (Object o : apCheck.getApVoucherLineItems()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) o;

                        String II_NM = apVoucherLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_RCVD = this.convertByUomFromAndItemAndQuantity(apVoucherLineItem.getInvUnitOfMeasure(), apVoucherLineItem.getInvItemLocation().getInvItem(), apVoucherLineItem.getVliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(apCheck.getChkDate(), II_NM, LOC_NM, AD_BRNCH, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = apVoucherLineItem.getVliUnitCost();

                        if (invCosting == null) {

                            this.postToInv(apVoucherLineItem, apCheck.getChkDate(), QTY_RCVD, COST * QTY_RCVD, QTY_RCVD, COST * QTY_RCVD, 0d, null, AD_BRNCH, companyCode);

                        } else {

                            this.postToInv(apVoucherLineItem, apCheck.getChkDate(), QTY_RCVD, COST * QTY_RCVD, invCosting.getCstRemainingQuantity() + QTY_RCVD, invCosting.getCstRemainingValue() + (COST * QTY_RCVD), 0d, null, AD_BRNCH, companyCode);
                        }
                    }
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

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(apCheck.getChkCheckDate(), adBankAccountBalance.getBabBalance() - apCheck.getChkAmount(), "BOOK", companyCode);

                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - apCheck.getChkAmount());
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(apCheck.getChkCheckDate(), (0 - apCheck.getChkAmount()), "BOOK", companyCode);

                        adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(apCheck.getChkCheckDate(), apCheck.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - apCheck.getChkAmount());
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
                // VOIDING PAYMENT

                if (apCheck.getChkType().equals("DIRECT") && !apCheck.getApVoucherLineItems().isEmpty()) {
                    // VOIDING DIRECT PAYMENT

                    for (Object o : apCheck.getApVoucherLineItems()) {

                        LocalApVoucherLineItem apVoucherLineItem = (LocalApVoucherLineItem) o;

                        String II_NM = apVoucherLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = apVoucherLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_RCVD = this.convertByUomFromAndItemAndQuantity(apVoucherLineItem.getInvUnitOfMeasure(), apVoucherLineItem.getInvItemLocation().getInvItem(), apVoucherLineItem.getVliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(apCheck.getChkDate(), II_NM, LOC_NM, AD_BRNCH, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = apVoucherLineItem.getVliUnitCost();

                        if (invCosting == null) {

                            this.postToInv(apVoucherLineItem, apCheck.getChkDate(), -QTY_RCVD, -COST * QTY_RCVD, -QTY_RCVD, -COST * QTY_RCVD, 0d, null, AD_BRNCH, companyCode);

                        } else {

                            this.postToInv(apVoucherLineItem, apCheck.getChkDate(), -QTY_RCVD, -COST * QTY_RCVD, invCosting.getCstRemainingQuantity() - QTY_RCVD, invCosting.getCstRemainingValue() - (COST * QTY_RCVD), 0d, null, AD_BRNCH, companyCode);
                        }
                    }
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

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(apCheck.getChkCheckDate(), adBankAccountBalance.getBabBalance() + apCheck.getChkAmount(), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + apCheck.getChkAmount());
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(apCheck.getChkCheckDate(), (apCheck.getChkAmount()), "BOOK", companyCode);

                        // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                        adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(apCheck.getChkCheckDate(), apCheck.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + apCheck.getChkAmount());
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

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    if (apDistributionRecord.getApAppliedVoucher() != null) {

                        LocalApVoucher apVoucher = apDistributionRecord.getApAppliedVoucher().getApVoucherPaymentSchedule().getApVoucher();

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apDistributionRecord.getDrAmount(), companyCode);
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

                    double DR_AMNT = 0d;

                    LocalApVoucher apVoucher = null;

                    if (apDistributionRecord.getApAppliedVoucher() != null) {

                        apVoucher = apDistributionRecord.getApAppliedVoucher().getApVoucherPaymentSchedule().getApVoucher();

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apDistributionRecord.getDrAmount(), companyCode);
                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(apDistributionRecord.getDrLine(), apDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    // apDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);
                    glJournalLine.setGlChartOfAccount(apDistributionRecord.getGlChartOfAccount());
                    // glJournal.addGlJournalLine(glJournalLine);
                    glJournalLine.setGlJournal(glJournal);

                    apDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation

                    int FC_CODE = apDistributionRecord.getApAppliedVoucher() != null ? apVoucher.getGlFunctionalCurrency().getFcCode() : apCheck.getGlFunctionalCurrency().getFcCode();

                    if ((FC_CODE != adCompany.getGlFunctionalCurrency().getFcCode()) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (FC_CODE == glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode())) {

                        double CONVERSION_RATE = apDistributionRecord.getApAppliedVoucher() != null ? apVoucher.getVouConversionRate() : apCheck.getChkConversionRate();

                        Date DATE = apDistributionRecord.getApAppliedVoucher() != null ? apVoucher.getVouConversionDate() : apCheck.getChkConversionDate();

                        if (DATE != null && (CONVERSION_RATE == 0 || CONVERSION_RATE == 1)) {

                            CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);

                        } else if (CONVERSION_RATE == 0) {

                            CONVERSION_RATE = 1;
                        }

                        Collection glForexLedgers = null;

                        DATE = apDistributionRecord.getApAppliedVoucher() != null ? apVoucher.getVouDate() : apCheck.getChkDate();

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(DATE, glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        } catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(DATE) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = apDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        } else {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                        }

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        glForexLedger = glForexLedgerHome.create(DATE, FRL_LN, "CHK", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, 0d, companyCode);

                        // glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);
                        glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                        } catch (FinderException ex) {

                        }

                        for (Object forexLedger : glForexLedgers) {

                            glForexLedger = (LocalGlForexLedger) forexLedger;
                            FRL_AMNT = apDistributionRecord.getDrAmount();

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                            } else {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                            }

                            glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);
                        }
                    }
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

                if (apCheck.getApSupplier().getApSupplierClass().getScLedger() == EJBCommon.TRUE) {

                    // Post Investors Account balance

                    // post current to current acv
                    Debug.print("post gl part 0");
                    this.postToGlInvestor(glAccountingCalendarValue, apCheck.getApSupplier(), true, apCheck.getChkVoid() == EJBCommon.FALSE ? EJBCommon.TRUE : EJBCommon.FALSE, apCheck.getChkVoid() == EJBCommon.FALSE ? apCheck.getChkAmount() : -apCheck.getChkAmount(), companyCode);

                    Debug.print("post gl done");
                    // post to subsequent acvs (propagate)

                    Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                    for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                        this.postToGlInvestor(glSubsequentAccountingCalendarValue, apCheck.getApSupplier(), false, apCheck.getChkVoid() == EJBCommon.FALSE ? EJBCommon.TRUE : EJBCommon.FALSE, apCheck.getChkVoid() == EJBCommon.FALSE ? apCheck.getChkAmount() : -apCheck.getChkAmount(), companyCode);
                    }

                    // post to subsequent years if necessary

                    Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                    if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                        adCompany = adCompanyHome.findByPrimaryKey(companyCode);

                        for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                            LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                            // post to subsequent acvs of subsequent set of book(propagate)

                            Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                            for (Object accountingCalendarValue : glAccountingCalendarValues) {

                                LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                                this.postToGlInvestor(glSubsequentAccountingCalendarValue, apCheck.getApSupplier(), false, apCheck.getChkVoid() == EJBCommon.FALSE ? EJBCommon.TRUE : EJBCommon.FALSE, apCheck.getChkVoid() == EJBCommon.FALSE ? apCheck.getChkAmount() : -apCheck.getChkAmount(), companyCode);
                            }

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                                break;
                            }
                        }
                    }
                }
            }

        } catch (GlJREffectiveDateNoPeriodExistException | AdPRFCoaGlVarianceAccountNotFoundException |
                 GlobalTransactionAlreadyVoidPostedException | GlobalTransactionAlreadyPostedException |
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

    private void post(Date CHK_DT, double CHK_AMNT, LocalApSupplier apSupplier, Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean post");

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

        Debug.print("ApDirectCheckEntryControllerBean postToGl");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode);

            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

            if (((glChartOfAccount.getCoaAccountType().equals("ASSET") || glChartOfAccount.getCoaAccountType().equals("EXPENSE")) && isDebit == EJBCommon.TRUE) || (!glChartOfAccount.getCoaAccountType().equals("ASSET") && !glChartOfAccount.getCoaAccountType().equals("EXPENSE") && isDebit == EJBCommon.FALSE)) {

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

    private void postToGlInvestor(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalApSupplier apSupplier, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode) throws FinderException, GlJREffectiveDateNoPeriodExistException {

        Debug.print("ApDirectCheckEntryControllerBean postToGlInvestor");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlInvestorAccountBalance glInvestorAccountBalance = null;
            try {
                glInvestorAccountBalance = glInvestorAccountBalanceHome.findByAcvCodeAndSplCode(glAccountingCalendarValue.getAcvCode(), apSupplier.getSplCode(), companyCode);
            } catch (FinderException ex) {
                throw new GlJREffectiveDateNoPeriodExistException();
            }
            // LocalGlInvestorAccountBalance

            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

            glInvestorAccountBalance.setIrabEndingBalance(EJBCommon.roundIt(glInvestorAccountBalance.getIrabEndingBalance() - JL_AMNT, FC_EXTNDD_PRCSN));

            if (!isCurrentAcv) {

                glInvestorAccountBalance.setIrabBeginningBalance(EJBCommon.roundIt(glInvestorAccountBalance.getIrabBeginningBalance() - JL_AMNT, FC_EXTNDD_PRCSN));
            }

            if (isCurrentAcv) {

                glInvestorAccountBalance.setIrabTotalDebit(EJBCommon.roundIt(glInvestorAccountBalance.getIrabTotalDebit() + JL_AMNT, FC_EXTNDD_PRCSN));
            }
        } catch (GlJREffectiveDateNoPeriodExistException ex) {
            throw ex;
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInv(LocalApVoucherLineItem apVoucherLineItem, Date CST_DT, double CST_QTY_RCVD, double CST_ITM_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApDirectCheckEntryControllerBean postToInv");

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

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            // void subsequent cost variance adjustments
            Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT, invItemLocation.getIlCode(), AD_BRNCH, companyCode);
            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), AD_BRNCH, companyCode);
            }

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            try {
                LocalInvCosting prevCst = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(CST_DT, invItemLocation.getIlCode(), AD_BRNCH, companyCode);

                prevExpiryDates = prevCst.getCstExpiryDate();
                qtyPrpgt = prevCst.getCstRemainingQuantity();

                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

            } catch (Exception ex) {

            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, CST_QTY_RCVD, CST_ITM_CST, 0d, 0d, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, AD_BRNCH, companyCode);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setApVoucherLineItem(apVoucherLineItem);

            invCosting.setCstQuantity(CST_QTY_RCVD);
            invCosting.setCstCost(CST_ITM_CST);

            // Get Latest Expiry Dates

            if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {
                Debug.print("apPurchaseOrderLine.getPlMisc(): " + apVoucherLineItem.getVliMisc().length());
                if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                    double qty2Prpgt = Double.parseDouble(this.getQuantityExpiryDates(apVoucherLineItem.getVliMisc()));
                    String miscList2Prpgt = this.propagateExpiryDates(apVoucherLineItem.getVliMisc(), qty2Prpgt);

                    String propagateMiscPrpgt = miscList2Prpgt + prevExpiryDates;

                    invCosting.setCstExpiryDate(propagateMiscPrpgt);
                } else {
                    invCosting.setCstExpiryDate(prevExpiryDates);
                }

            } else {
                if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                    double initialQty = Double.parseDouble(this.getQuantityExpiryDates(apVoucherLineItem.getVliMisc()));
                    String initialPrpgt = this.propagateExpiryDates(apVoucherLineItem.getVliMisc(), initialQty);

                    invCosting.setCstExpiryDate(initialPrpgt);
                } else {
                    invCosting.setCstExpiryDate(prevExpiryDates);
                }
            }

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "APCHK" + apVoucherLineItem.getApCheck().getChkDocumentNumber(), apVoucherLineItem.getApCheck().getChkDescription(), apVoucherLineItem.getApCheck().getChkDate(), USR_NM, AD_BRNCH, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

            i = invCostings.iterator();
            String miscList = "";
            if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                double qty = Double.parseDouble(this.getQuantityExpiryDates(apVoucherLineItem.getVliMisc()));
                miscList = this.propagateExpiryDates(apVoucherLineItem.getVliMisc(), qty);

                Debug.print("miscList Propagate:" + miscList);
            }

            while (i.hasNext()) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_QTY_RCVD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ITM_CST);
                if (apVoucherLineItem.getVliMisc() != null && apVoucherLineItem.getVliMisc() != "" && apVoucherLineItem.getVliMisc().length() != 0) {
                    Debug.print("invPropagatedCosting.getCstExpiryDate() : " + invPropagatedCosting.getCstExpiryDate());
                    String propagateMisc = invPropagatedCosting.getCstExpiryDate() + miscList;

                    invPropagatedCosting.setCstExpiryDate(propagateMisc);
                } else {
                    invPropagatedCosting.setCstExpiryDate(prevExpiryDates);
                }
            }
            // regenerate cost variance
            this.regenerateCostVariance(invCostings, invCosting, AD_BRNCH, companyCode);

        } catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public String getQuantityExpiryDates(String qntty) {

        String[] arrayMisc = qntty.split("_");

        return arrayMisc[0];
    }

    public String propagateExpiryDates(String misc, double qty) throws Exception {
        // ActionErrors errors = new ActionErrors();

        return misc;
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double QTY_RCVD, Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean convertByUomFromAndItemAndQuantity");

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

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean voidInvAdjustment");

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

                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), invDistributionRecord.getDrClass(), invDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, invDistributionRecord.getDrAmount(), EJBCommon.TRUE, invDistributionRecord.getInvChartOfAccount().getCoaCode(), invAdjustment, AD_BRNCH, companyCode);
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

            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), AD_BRNCH, companyCode);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void generateCostVariance(LocalInvItemLocation invItemLocation, double CST_VRNC_VL, String ADJ_RFRNC_NMBR, String ADJ_DSCRPTN, Date ADJ_DT, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void regenerateCostVariance(Collection invCostings, LocalInvCosting invCosting, Integer AD_BRNCH, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL, Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ApDirectCheckEntryControllerBean addInvDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, companyCode);

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

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ApDirectCheckEntryControllerBean executeInvAdjPost");

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

                LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invAdjustment.getAdjDate(), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(), invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), AD_BRNCH, companyCode);

                this.postInvAdjustmentToInventory(invAdjustmentLine, invAdjustment.getAdjDate(), 0, invAdjustmentLine.getAlUnitCost(), invCosting.getCstRemainingQuantity(), invCosting.getCstRemainingValue() + invAdjustmentLine.getAlUnitCost(), AD_BRNCH, companyCode);
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

                glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", AD_BRNCH, companyCode);

            } catch (FinderException ex) {

            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(), invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d, null, invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, AD_BRNCH, companyCode);

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
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), AD_BRNCH, companyCode);

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

    private LocalInvAdjustment saveInvAdjustment(String ADJ_RFRNC_NMBR, String ADJ_DSCRPTN, Date ADJ_DATE, String USR_NM, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean saveInvAdjustment");

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

                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

            } catch (FinderException ex) {

            }

            while (true) {

                if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                    try {

                        invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, companyCode);
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                    } catch (FinderException ex) {

                        ADJ_DCMNT_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                        break;
                    }

                } else {

                    try {

                        invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, companyCode);
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                    } catch (FinderException ex) {

                        ADJ_DCMNT_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                        break;
                    }
                }
            }

            return invAdjustmentHome.create(ADJ_DCMNT_NMBR, ADJ_RFRNC_NMBR, ADJ_DSCRPTN, ADJ_DATE, "COST-VARIANCE", "N/A", EJBCommon.FALSE, USR_NM, ADJ_DATE, USR_NM, ADJ_DATE, null, null, USR_NM, ADJ_DATE, null, null, EJBCommon.TRUE, EJBCommon.FALSE, AD_BRNCH, companyCode);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void createInvTagList(LocalApVoucherLineItem apVoucherLineItem, ArrayList list, Integer companyCode) throws Exception {

        Debug.print("ApDirectCheckEntryControllerBean createInvTagList");

        try {

            Iterator t = list.iterator();

            LocalInvTag invTag = null;

            while (t.hasNext()) {
                InvModTagListDetails tgLstDetails = (InvModTagListDetails) t.next();

                if (tgLstDetails.getTgCode() == null) {
                    invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), companyCode, tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());

                    invTag.setApVoucherLineItem(apVoucherLineItem);
                    invTag.setInvItemLocation(apVoucherLineItem.getInvItemLocation());
                    LocalAdUser adUser = null;
                    try {
                        adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), companyCode);
                    } catch (FinderException ex) {

                    }
                    invTag.setAdUser(adUser);
                }
            }

        } catch (Exception ex) {
            throw ex;
        }
    }

    public ArrayList getAdUsrAll(Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean getAdUsrAll");

        LocalAdUser adUser = null;

        Collection adUsers = null;

        ArrayList list = new ArrayList();

        try {

            adUsers = adUserHome.findUsrAll(companyCode);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adUsers.isEmpty()) {

            return null;
        }

        for (Object user : adUsers) {

            adUser = (LocalAdUser) user;

            list.add(adUser.getUsrName());
        }

        return list;
    }

    private ArrayList getInvTagList(LocalApVoucherLineItem apVoucherLineItem) {

        Debug.print("ApDirectCheckEntryControllerBean getInvTagList");

        ArrayList list = new ArrayList();

        Collection invTags = apVoucherLineItem.getInvTags();
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

        }

        return list;
    }

    public boolean getInvTraceMisc(String II_NAME, Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean getInvTraceMisc");

        Collection invLocations = null;
        ArrayList list = new ArrayList();
        boolean isTraceMisc = false;


        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NAME, companyCode);
            if (invItem.getIiTraceMisc() == 1) {
                isTraceMisc = true;
            }
            return isTraceMisc;
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT, double CST_ADJST_QTY, double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ApDirectCheckEntryControllerBean postInvAdjustmentToInventory");

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

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, CST_ADJST_QTY > 0 ? CST_ADJST_QTY : 0, AD_BRNCH, companyCode);

            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);

            invCosting.setCstQuantity(CST_ADJST_QTY);
            invCosting.setCstCost(CST_ADJST_CST);

            // propagate balance if necessary

            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

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

        Debug.print("ApDirectCheckEntryControllerBean ejbCreate");
    }

}