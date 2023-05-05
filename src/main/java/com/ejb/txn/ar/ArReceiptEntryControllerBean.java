/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArReceiptEntryControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ar;

import java.util.*;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdAmountLimitHome;
import com.ejb.entities.ad.LocalAdApproval;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.entities.ad.LocalAdApprovalQueue;
import com.ejb.dao.ad.LocalAdApprovalQueueHome;
import com.ejb.dao.ad.LocalAdApprovalUserHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdBankAccountBalance;
import com.ejb.dao.ad.LocalAdBankAccountBalanceHome;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchBankAccount;
import com.ejb.dao.ad.LocalAdBranchBankAccountHome;
import com.ejb.entities.ad.LocalAdBranchCustomer;
import com.ejb.dao.ad.LocalAdBranchCustomerHome;
import com.ejb.entities.ad.LocalAdBranchDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdBranchDocumentSequenceAssignmentHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ad.LocalAdDeleteAuditTrailHome;
import com.ejb.entities.ad.LocalAdDiscount;
import com.ejb.dao.ad.LocalAdDiscountHome;
import com.ejb.entities.ad.LocalAdDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ar.LocalArAppliedCredit;
import com.ejb.dao.ar.LocalArAppliedCreditHome;
import com.ejb.entities.ar.LocalArAppliedInvoice;
import com.ejb.dao.ar.LocalArAppliedInvoiceHome;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.entities.ar.LocalArCustomerBalance;
import com.ejb.dao.ar.LocalArCustomerBalanceHome;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.entities.ar.LocalArDistributionRecord;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.entities.ar.LocalArInvoiceLine;
import com.ejb.entities.ar.LocalArInvoicePaymentSchedule;
import com.ejb.dao.ar.LocalArInvoicePaymentScheduleHome;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.entities.ar.LocalArReceiptBatch;
import com.ejb.dao.ar.LocalArReceiptBatchHome;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.entities.ar.LocalArTaxCode;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.dao.cm.LocalCmAdjustmentHome;
import com.ejb.entities.cm.LocalCmDistributionRecord;
import com.ejb.dao.cm.LocalCmDistributionRecordHome;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountBalanceHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.ILocalGlInvestorAccountBalanceHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlChartOfAccountBalance;
import com.ejb.entities.gl.LocalGlForexLedger;
import com.ejb.dao.gl.LocalGlForexLedgerHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.gl.LocalGlInvestorAccountBalance;
import com.ejb.entities.gl.LocalGlJournal;
import com.ejb.entities.gl.LocalGlJournalBatch;
import com.ejb.dao.gl.LocalGlJournalBatchHome;
import com.ejb.entities.gl.LocalGlJournalCategory;
import com.ejb.dao.gl.LocalGlJournalCategoryHome;
import com.ejb.dao.gl.LocalGlJournalHome;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.entities.gl.LocalGlJournalSource;
import com.ejb.dao.gl.LocalGlJournalSourceHome;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.ejb.entities.gl.LocalGlSuspenseAccount;
import com.ejb.dao.gl.LocalGlSuspenseAccountHome;
import com.ejb.exception.ad.AdPRFCoaGlCustomerDepositAccountNotFoundException;
import com.ejb.exception.ar.*;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.mod.ar.ArModAppliedInvoiceDetails;
import com.util.mod.ar.ArModCustomerDetails;
import com.util.mod.ar.ArModInvoicePaymentScheduleDetails;
import com.util.mod.ar.ArModReceiptDetails;
import com.util.ar.ArReceiptDetails;
import com.util.cm.CmAdjustmentDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;

@Stateless(name = "ArReceiptEntryControllerEJB")
public class ArReceiptEntryControllerBean extends EJBContextClass implements ArReceiptEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private ArApprovalController arApprovalController;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalAdDiscountHome adDiscountHome;
    @EJB
    private LocalArReceiptBatchHome arReceiptBatchHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdBranchCustomerHome adBranchCustomerHome;
    @EJB
    private LocalAdBranchBankAccountHome adBranchBankAccountHome;
    @EJB
    private LocalArAppliedInvoiceHome arAppliedInvoiceHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalCmAdjustmentHome cmAdjustmentHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalArAppliedCreditHome arAppliedCreditHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalCmDistributionRecordHome cmDistributionRecordHome;
    @EJB
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private LocalGlForexLedgerHome glForexLedgerHome;
    @EJB
    private LocalArCustomerBalanceHome arCustomerBalanceHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private ILocalGlInvestorAccountBalanceHome glInvestorAccountBalanceHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;

    public ArrayList getGlFcAllWithDefault(Integer companyCode) {
        Debug.print("ArReceiptEntryControllerBean getGlFcAllWithDefault");

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
        Debug.print("ArReceiptEntryControllerBean getAdBaAll");

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

    public int getArCstAdBrnchByEmplyId(String EMP_ID, Integer companyCode) throws GlobalNoRecordFoundException, GlobalDuplicateEmployeeIdException {
        Debug.print("ArReceiptImportControllerBean getArCstAdBrnchByEmplyId");

        ArrayList list = new ArrayList();
        try {
            Collection arCustomers = arCustomerHome.findManyByCstEmployeeID(EMP_ID, companyCode);
            if (arCustomers.size() > 1) {
                throw new GlobalDuplicateEmployeeIdException();
            }
            LocalArCustomer arCustomer = arCustomerHome.findByCstEmployeeID(EMP_ID, companyCode);
            return arCustomer.getCstAdBranch();
        } catch (FinderException ex) {
            // throw new GlobalNoRecordFoundException();
            return 0;
        } catch (GlobalDuplicateEmployeeIdException ex) {
            throw new GlobalDuplicateEmployeeIdException();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int getArCstAdBrnchByCustomerCode(String CUSTOMER_CODE, Integer companyCode) throws GlobalNoRecordFoundException, GlobalDuplicateCustomerCodeException {
        Debug.print("ArReceiptImportControllerBean getArCstAdBrnchByCustomerCode");

        ArrayList list = new ArrayList();

        try {
            Collection arCustomers = arCustomerHome.findManyByCstCustomerCode(CUSTOMER_CODE, companyCode);
            if (arCustomers.size() > 1) {
                throw new GlobalDuplicateCustomerCodeException();
            }
            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(CUSTOMER_CODE, companyCode);
            return arCustomer.getCstAdBranch();
        } catch (FinderException ex) {
            // throw new GlobalNoRecordFoundException();
            return 0;
        } catch (GlobalDuplicateCustomerCodeException ex) {
            throw new GlobalDuplicateCustomerCodeException();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getArCstCstmrCodeByEmplyId(String EMP_ID, Integer companyCode) throws GlobalNoRecordFoundException, GlobalDuplicateEmployeeIdException {
        Debug.print("ArReceiptImportControllerBean getArCstCstmrCodeByEmplyId");

        ArrayList list = new ArrayList();
        try {
            Collection arCustomers = arCustomerHome.findManyByCstEmployeeID(EMP_ID, companyCode);
            if (arCustomers.size() > 1) {
                throw new GlobalDuplicateEmployeeIdException();
            }
            LocalArCustomer arCustomer = arCustomerHome.findByCstEmployeeID(EMP_ID, companyCode);
            return arCustomer.getCstCustomerCode();
        } catch (FinderException ex) {
            throw new GlobalNoRecordFoundException();
        } catch (GlobalDuplicateEmployeeIdException ex) {
            throw new GlobalDuplicateEmployeeIdException();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModReceiptDetails getArRctByRctNum(String RCT_NMBR, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException {
        Debug.print("ArReceiptEntryControllerBean getArRctByRctNum");

        try {
            // get applied invoices
            LocalArReceipt arReceipt = null;
            try {
                arReceipt = arReceiptHome.findByRctNumberAndBrCode(RCT_NMBR, AD_BRNCH, companyCode);
            } catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }
            return this.setArModReceiptDetails(arReceipt);
        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModReceiptDetails getArRctByRctCode(Integer RCT_CODE, Integer companyCode) throws GlobalNoRecordFoundException {
        Debug.print("ArReceiptEntryControllerBean getArRctByRctCode");

        try {
            LocalArReceipt arReceipt = null;
            try {
                arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);
            } catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }
            return this.setArModReceiptDetails(arReceipt);

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModCustomerDetails getArCstByCstCustomerCode(String CST_CSTMR_CODE, Integer companyCode) throws GlobalNoRecordFoundException {
        Debug.print("ArReceiptEntryControllerBean getArCstByCstCustomerCode");

        try {
            LocalArCustomer arCustomer = null;
            try {
                arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, companyCode);
            } catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }
            ArModCustomerDetails mdetails = new ArModCustomerDetails();
            mdetails.setCstCustomerCode(arCustomer.getCstCustomerCode());
            mdetails.setCstPaymentMethod(arCustomer.getCstPaymentMethod());
            mdetails.setCstCtBaName(arCustomer.getAdBankAccount().getBaName());
            mdetails.setCstName(arCustomer.getCstName());
            return mdetails;

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArIpsByInvcNmbr(String AR_INVC_NMBR, Date RCT_DT, boolean ENBL_RBT, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException {
        Debug.print("ArReceiptEntryControllerBean getArIpsByInvcNmbr");

        ArrayList list = new ArrayList();
        try {
            // if getting all invoice in all branch
            Collection arInvoicePaymentSchedules;
            if (AD_BRNCH <= 0) {
                arInvoicePaymentSchedules = arInvoicePaymentScheduleHome.findOpenIpsByIpsLockAndInvNumber(EJBCommon.FALSE, AR_INVC_NMBR, companyCode);
            } else {
                arInvoicePaymentSchedules = arInvoicePaymentScheduleHome.findOpenIpsByIpsLockAndInvNumberAdBranchCompny(EJBCommon.FALSE, AR_INVC_NMBR, AD_BRNCH, companyCode);
            }
            if (arInvoicePaymentSchedules.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode);
            for (Object invoicePaymentSchedule : arInvoicePaymentSchedules) {
                LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) invoicePaymentSchedule;

                // verification if ips is already closed
                if (EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountDue(), precisionUnit) == EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountPaid(), precisionUnit))
                    continue;

                ArModInvoicePaymentScheduleDetails mdetails = new ArModInvoicePaymentScheduleDetails();
                Collection arInvoiceLines = arInvoicePaymentSchedule.getArInvoice().getArInvoiceLines();
                Iterator x = arInvoiceLines.iterator();
                String reference = "";
                while (x.hasNext()) {
                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) x.next();
                    reference = arInvoiceLine.getArStandardMemoLine().getSmlName().replace("AR - ", "");
                    reference.replace("Association Dues", "ASD");
                    reference.replace("Parking Dues", "PD");
                    reference.replace("Water", "WTR");
                }

                mdetails.setIpsCode(arInvoicePaymentSchedule.getIpsCode());
                mdetails.setIpsNumber(arInvoicePaymentSchedule.getIpsNumber());
                mdetails.setIpsArCustomerCode(arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstCustomerCode());
                mdetails.setIpsInvDate(arInvoicePaymentSchedule.getArInvoice().getInvDate());
                mdetails.setIpsDueDate(arInvoicePaymentSchedule.getIpsDueDate());
                mdetails.setIpsAmountDue(arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid());
                mdetails.setIpsPenaltyDue(arInvoicePaymentSchedule.getIpsPenaltyDue() - arInvoicePaymentSchedule.getIpsPenaltyPaid());

                double interestDue = arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstEnableRebate() == EJBCommon.TRUE ? arInvoicePaymentSchedule.getIpsInterestDue() : 0d;
                mdetails.setIpsInterestDue(interestDue);

                mdetails.setIpsInvNumber(arInvoicePaymentSchedule.getArInvoice().getInvNumber());
                mdetails.setIpsInvReferenceNumber(reference + " " + arInvoicePaymentSchedule.getArInvoice().getInvReferenceNumber());
                mdetails.setIpsInvFcName(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName());
                mdetails.setIpsAdBranch(arInvoicePaymentSchedule.getArInvoice().getInvAdBranch());
                mdetails.setIpsArCustomerCode(arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstCustomerCode());

                // calculate default discount
                short INVOICE_AGE = (short) ((RCT_DT.getTime() - arInvoicePaymentSchedule.getIpsDueDate().getTime()) / (1000 * 60 * 60 * 24));
                double IPS_DSCNT_AMNT = 0d;

                if (arInvoicePaymentSchedule.getArInvoice().getAdPaymentTerm().getPytDiscountOnInvoice() == EJBCommon.FALSE) {

                    Collection adDiscounts = adDiscountHome.findByPsLineNumberAndPytName(arInvoicePaymentSchedule.getIpsNumber(), arInvoicePaymentSchedule.getArInvoice().getAdPaymentTerm().getPytName(), companyCode);

                    for (Object discount : adDiscounts) {
                        LocalAdDiscount adDiscount = (LocalAdDiscount) discount;
                        if (adDiscount.getDscPaidWithinDay() >= INVOICE_AGE) {
                            IPS_DSCNT_AMNT = EJBCommon.roundIt(mdetails.getIpsAmountDue() * adDiscount.getDscDiscountPercent() / 100, this.getGlFcPrecisionUnit(companyCode));
                            break;
                        }
                    }
                }

                double IPS_REBATE = 0d;
                if (arInvoicePaymentSchedule.getArInvoice().getAdPaymentTerm().getPytDiscountOnInvoice() == EJBCommon.FALSE && arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstEnableRebate() == EJBCommon.TRUE && ENBL_RBT) {
                    if (0 >= INVOICE_AGE) {
                        IPS_REBATE = EJBCommon.roundIt(mdetails.getIpsInterestDue(), this.getGlFcPrecisionUnit(companyCode));
                    }
                }

                // calculate default tax withheld
                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

                double APPLY_AMOUNT = mdetails.getIpsAmountDue() - IPS_DSCNT_AMNT;
                double PENALTY_APPLY_AMOUNT = mdetails.getIpsPenaltyDue();

                double W_TAX_AMOUNT = 0d;

                if (arInvoicePaymentSchedule.getArInvoice().getArWithholdingTaxCode().getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals("COLLECTION")) {

                    LocalArTaxCode arTaxCode = arInvoicePaymentSchedule.getArInvoice().getArTaxCode();
                    double NET_AMOUNT = 0d;
                    if (arTaxCode.getTcType().equals("INCLUSIVE") || arTaxCode.getTcType().equals("EXCLUSIVE")) {
                        NET_AMOUNT = EJBCommon.roundIt(APPLY_AMOUNT / (1 + (arTaxCode.getTcRate() / 100)), this.getGlFcPrecisionUnit(companyCode));
                    } else {
                        NET_AMOUNT = APPLY_AMOUNT;
                    }

                    W_TAX_AMOUNT = EJBCommon.roundIt(NET_AMOUNT * (arInvoicePaymentSchedule.getArInvoice().getArWithholdingTaxCode().getWtcRate() / 100), this.getGlFcPrecisionUnit(companyCode));
                    APPLY_AMOUNT -= W_TAX_AMOUNT;
                }

                mdetails.setIpsAiApplyAmount(APPLY_AMOUNT);
                mdetails.setIpsAiPenaltyApplyAmount(PENALTY_APPLY_AMOUNT);
                mdetails.setIpsAiCreditableWTax(W_TAX_AMOUNT);
                mdetails.setIpsAiDiscountAmount(IPS_DSCNT_AMNT);
                mdetails.setIpsAiRebate(IPS_REBATE);
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

    public ArrayList getArIpsByCstCustomerCode(String CST_CSTMR_CODE, Date RCT_DT, boolean ENBL_RBT, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArReceiptEntryControllerBean getArIpsByCstCustomerCode");

        ArrayList list = new ArrayList();
        try {
            // if getting all invoice in all branch
            Collection arInvoicePaymentSchedules;
            if (AD_BRNCH <= 0) {
                arInvoicePaymentSchedules = arInvoicePaymentScheduleHome.findOpenIpsByIpsLockAndCstCustomerCode(EJBCommon.FALSE, CST_CSTMR_CODE, companyCode);
            } else {
                arInvoicePaymentSchedules = arInvoicePaymentScheduleHome.findOpenIpsByIpsLockAndCstCustomerCodeAndBrCode(EJBCommon.FALSE, CST_CSTMR_CODE, AD_BRNCH, companyCode);
            }

            if (arInvoicePaymentSchedules.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode);
            for (Object invoicePaymentSchedule : arInvoicePaymentSchedules) {
                LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) invoicePaymentSchedule;

                // verification if ips is already closed
                if (EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountDue(), precisionUnit) == EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountPaid(), precisionUnit))
                    continue;

                ArModInvoicePaymentScheduleDetails mdetails = new ArModInvoicePaymentScheduleDetails();

                Collection arInvoiceLines = arInvoicePaymentSchedule.getArInvoice().getArInvoiceLines();
                Iterator x = arInvoiceLines.iterator();

                mdetails.setIpsCode(arInvoicePaymentSchedule.getIpsCode());
                mdetails.setIpsNumber(arInvoicePaymentSchedule.getIpsNumber());
                mdetails.setIpsInvDate(arInvoicePaymentSchedule.getArInvoice().getInvDate());
                mdetails.setIpsArCustomerCode(arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstCustomerCode());
                mdetails.setIpsDueDate(arInvoicePaymentSchedule.getIpsDueDate());
                mdetails.setIpsAmountDue(arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid());
                mdetails.setIpsPenaltyDue(arInvoicePaymentSchedule.getIpsPenaltyDue() - arInvoicePaymentSchedule.getIpsPenaltyPaid());

                double interestDue = arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstEnableRebate() == EJBCommon.TRUE ? arInvoicePaymentSchedule.getIpsInterestDue() : 0d;
                mdetails.setIpsInterestDue(interestDue);

                mdetails.setIpsInvNumber(arInvoicePaymentSchedule.getArInvoice().getInvNumber());
                mdetails.setIpsInvReferenceNumber(arInvoicePaymentSchedule.getArInvoice().getInvReferenceNumber());
                mdetails.setIpsInvFcName(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName());
                mdetails.setIpsAdBranch(arInvoicePaymentSchedule.getArInvoice().getInvAdBranch());
                mdetails.setIpsArCustomerCode(arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstCustomerCode());

                // calculate default discount
                short INVOICE_AGE = (short) ((RCT_DT.getTime() - arInvoicePaymentSchedule.getIpsDueDate().getTime()) / (1000 * 60 * 60 * 24));
                double IPS_DSCNT_AMNT = 0d;

                if (arInvoicePaymentSchedule.getArInvoice().getAdPaymentTerm().getPytDiscountOnInvoice() == EJBCommon.FALSE) {
                    Collection adDiscounts = adDiscountHome.findByPsLineNumberAndPytName(arInvoicePaymentSchedule.getIpsNumber(), arInvoicePaymentSchedule.getArInvoice().getAdPaymentTerm().getPytName(), companyCode);

                    for (Object discount : adDiscounts) {

                        LocalAdDiscount adDiscount = (LocalAdDiscount) discount;
                        if (adDiscount.getDscPaidWithinDay() >= INVOICE_AGE) {
                            IPS_DSCNT_AMNT = EJBCommon.roundIt(mdetails.getIpsAmountDue() * adDiscount.getDscDiscountPercent() / 100, this.getGlFcPrecisionUnit(companyCode));
                            break;
                        }
                    }
                }

                double IPS_REBATE = 0d;
                if (arInvoicePaymentSchedule.getArInvoice().getAdPaymentTerm().getPytDiscountOnInvoice() == EJBCommon.FALSE && arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstEnableRebate() == EJBCommon.TRUE && ENBL_RBT) {
                    if (0 >= INVOICE_AGE) {
                        IPS_REBATE = EJBCommon.roundIt(mdetails.getIpsInterestDue(), this.getGlFcPrecisionUnit(companyCode));
                    }
                }

                // calculate default tax withheld
                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

                double APPLY_AMOUNT = mdetails.getIpsAmountDue() - IPS_DSCNT_AMNT;
                double PENALTY_APPLY_AMOUNT = mdetails.getIpsPenaltyDue();

                double W_TAX_AMOUNT = 0d;

                if (arInvoicePaymentSchedule.getArInvoice().getArWithholdingTaxCode().getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals("COLLECTION")) {

                    LocalArTaxCode arTaxCode = arInvoicePaymentSchedule.getArInvoice().getArTaxCode();
                    double NET_AMOUNT = 0d;
                    if (arTaxCode.getTcType().equals("INCLUSIVE") || arTaxCode.getTcType().equals("EXCLUSIVE")) {
                        NET_AMOUNT = EJBCommon.roundIt(APPLY_AMOUNT / (1 + (arTaxCode.getTcRate() / 100)), this.getGlFcPrecisionUnit(companyCode));
                    } else {
                        NET_AMOUNT = APPLY_AMOUNT;
                    }
                    W_TAX_AMOUNT = EJBCommon.roundIt(NET_AMOUNT * (arInvoicePaymentSchedule.getArInvoice().getArWithholdingTaxCode().getWtcRate() / 100), this.getGlFcPrecisionUnit(companyCode));
                    APPLY_AMOUNT -= W_TAX_AMOUNT;
                }
                mdetails.setIpsAiApplyAmount(APPLY_AMOUNT);
                mdetails.setIpsAiPenaltyApplyAmount(PENALTY_APPLY_AMOUNT);
                mdetails.setIpsAiCreditableWTax(W_TAX_AMOUNT);
                mdetails.setIpsAiDiscountAmount(IPS_DSCNT_AMNT);
                mdetails.setIpsAiRebate(IPS_REBATE);
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


    public ArrayList getArIpsByReferenceAndCstCustomerCode(java.lang.String CST_CSTMR_CODE, Date RCT_DT, boolean ENBL_RBT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException, ArReceiptInvoiceAlreadyPaidException {

        Debug.print("ArReceiptEntryControllerBean getArIpsByReferenceAndCstCustomerCode");

        ArrayList list = new ArrayList();

        try {

            // if getting all invoice in all branch
            Collection arInvoicePaymentSchedules;

            if (AD_BRNCH <= 0) {

                arInvoicePaymentSchedules = arInvoicePaymentScheduleHome.findOpenIpsByIpsLockAndCstCustomerCodeAndBrCode(EJBCommon.FALSE, CST_CSTMR_CODE, AD_BRNCH, AD_CMPNY);

            } else {

                arInvoicePaymentSchedules = arInvoicePaymentScheduleHome.findOpenIpsByIpsLockAndCstCustomerCodeAndBrCode(EJBCommon.FALSE, CST_CSTMR_CODE, AD_BRNCH, AD_CMPNY);

            }

            if (arInvoicePaymentSchedules.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            short precisionUnit = this.getGlFcPrecisionUnit(AD_CMPNY);

            Iterator i = arInvoicePaymentSchedules.iterator();

            while (i.hasNext()) {

                LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) i.next();

                // verification if ips is already closed
                if (EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountDue(), precisionUnit) == EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountPaid(), precisionUnit)) {

                    throw new ArReceiptInvoiceAlreadyPaidException();

                }

                ArModInvoicePaymentScheduleDetails mdetails = new ArModInvoicePaymentScheduleDetails();

                Collection arInvoiceLines = arInvoicePaymentSchedule.getArInvoice().getArInvoiceLines();
                Iterator x = arInvoiceLines.iterator();

                mdetails.setIpsCode(arInvoicePaymentSchedule.getIpsCode());
                mdetails.setIpsNumber(arInvoicePaymentSchedule.getIpsNumber());
                mdetails.setIpsArCustomerCode(arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstCustomerCode());
                mdetails.setIpsInvDate(arInvoicePaymentSchedule.getArInvoice().getInvDate());

                mdetails.setIpsDueDate(arInvoicePaymentSchedule.getIpsDueDate());
                mdetails.setIpsAmountDue(arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid());
                mdetails.setIpsPenaltyDue(arInvoicePaymentSchedule.getIpsPenaltyDue() - arInvoicePaymentSchedule.getIpsPenaltyPaid());

                double interestDue = arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstEnableRebate() == EJBCommon.TRUE ? arInvoicePaymentSchedule.getIpsInterestDue() : 0d;
                mdetails.setIpsInterestDue(interestDue);

                mdetails.setIpsInvNumber(arInvoicePaymentSchedule.getArInvoice().getInvNumber());
                mdetails.setIpsInvReferenceNumber(arInvoicePaymentSchedule.getArInvoice().getInvReferenceNumber());
                mdetails.setIpsInvFcName(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName());
                mdetails.setIpsAdBranch(arInvoicePaymentSchedule.getArInvoice().getInvAdBranch());

                mdetails.setIpsArCustomerCode(arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstCustomerCode());
                // calculate default discount

                short INVOICE_AGE = (short) ((RCT_DT.getTime() - arInvoicePaymentSchedule.getIpsDueDate().getTime()) / (1000 * 60 * 60 * 24));

                double IPS_DSCNT_AMNT = 0d;

                if (arInvoicePaymentSchedule.getArInvoice().getAdPaymentTerm().getPytDiscountOnInvoice() == EJBCommon.FALSE) {

                    Collection adDiscounts = adDiscountHome.findByPsLineNumberAndPytName(arInvoicePaymentSchedule.getIpsNumber(), arInvoicePaymentSchedule.getArInvoice().getAdPaymentTerm().getPytName(), AD_CMPNY);

                    Iterator j = adDiscounts.iterator();

                    while (j.hasNext()) {

                        LocalAdDiscount adDiscount = (LocalAdDiscount) j.next();

                        if (adDiscount.getDscPaidWithinDay() >= INVOICE_AGE) {

                            IPS_DSCNT_AMNT = EJBCommon.roundIt(mdetails.getIpsAmountDue() * adDiscount.getDscDiscountPercent() / 100, this.getGlFcPrecisionUnit(AD_CMPNY));
                            break;
                        }
                    }
                }

                double IPS_REBATE = 0d;

                if (arInvoicePaymentSchedule.getArInvoice().getAdPaymentTerm().getPytDiscountOnInvoice() == EJBCommon.FALSE && arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstEnableRebate() == EJBCommon.TRUE && ENBL_RBT) {

                    if (0 >= INVOICE_AGE) {

                        IPS_REBATE = EJBCommon.roundIt(mdetails.getIpsInterestDue(), this.getGlFcPrecisionUnit(AD_CMPNY));

                    }
                }

                // calculate default tax withheld

                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

                double APPLY_AMOUNT = mdetails.getIpsAmountDue() - IPS_DSCNT_AMNT;
                double PENALTY_APPLY_AMOUNT = mdetails.getIpsPenaltyDue();

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
                mdetails.setIpsAiPenaltyApplyAmount(PENALTY_APPLY_AMOUNT);
                mdetails.setIpsAiCreditableWTax(W_TAX_AMOUNT);
                mdetails.setIpsAiDiscountAmount(IPS_DSCNT_AMNT);
                mdetails.setIpsAiRebate(IPS_REBATE);

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

    public ArrayList getArIpsByIpsCode(Integer IPS_CODE, boolean ENBL_RBT, Integer companyCode) throws GlobalNoRecordFoundException {
        Debug.print("ArReceiptEntryControllerBean getArIpsByIpsCode");

        ArrayList list = new ArrayList();

        try {
            Collection arInvoicePaymentSchedules = arInvoicePaymentScheduleHome.findOpenIpsByIpsLockAndIpsCode(EJBCommon.FALSE, IPS_CODE, companyCode);
            if (arInvoicePaymentSchedules.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode);
            for (Object invoicePaymentSchedule : arInvoicePaymentSchedules) {
                LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) invoicePaymentSchedule;

                // verification if ips is already closed
                if (EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountDue(), precisionUnit) == EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountPaid(), precisionUnit))
                    continue;

                ArModInvoicePaymentScheduleDetails mdetails = new ArModInvoicePaymentScheduleDetails();

                Collection arInvoiceLines = arInvoicePaymentSchedule.getArInvoice().getArInvoiceLines();
                Iterator x = arInvoiceLines.iterator();
                String reference = "";
                while (x.hasNext()) {
                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) x.next();
                    reference = arInvoiceLine.getArStandardMemoLine().getSmlName().replace("AR - ", "");
                    reference.replace("Association Dues", "ASD");
                    reference.replace("Parking Dues", "PD");
                    reference.replace("Water", "WTR");
                }

                mdetails.setIpsCode(arInvoicePaymentSchedule.getIpsCode());
                mdetails.setIpsNumber(arInvoicePaymentSchedule.getIpsNumber());
                mdetails.setIpsInvDate(arInvoicePaymentSchedule.getArInvoice().getInvDate());
                mdetails.setIpsArCustomerCode(arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstCustomerCode());
                mdetails.setIpsDueDate(arInvoicePaymentSchedule.getIpsDueDate());
                mdetails.setIpsAmountDue(arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid());
                mdetails.setIpsPenaltyDue(arInvoicePaymentSchedule.getIpsPenaltyDue() - arInvoicePaymentSchedule.getIpsPenaltyPaid());

                double interestDue = arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstEnableRebate() == EJBCommon.TRUE ? arInvoicePaymentSchedule.getIpsInterestDue() : 0d;
                mdetails.setIpsInterestDue(interestDue);
                mdetails.setIpsInvNumber(arInvoicePaymentSchedule.getArInvoice().getInvNumber());
                mdetails.setIpsInvReferenceNumber(reference + " " + arInvoicePaymentSchedule.getArInvoice().getInvReferenceNumber());
                mdetails.setIpsInvFcName(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName());
                mdetails.setIpsAdBranch(arInvoicePaymentSchedule.getArInvoice().getInvAdBranch());

                // calculate default tax withheld
                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

                double APPLY_AMOUNT = mdetails.getIpsAmountDue();
                double PENALTY_APPLY_AMOUNT = mdetails.getIpsPenaltyDue();

                double W_TAX_AMOUNT = 0d;

                if (arInvoicePaymentSchedule.getArInvoice().getArWithholdingTaxCode().getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals("COLLECTION")) {

                    LocalArTaxCode arTaxCode = arInvoicePaymentSchedule.getArInvoice().getArTaxCode();

                    double NET_AMOUNT = 0d;

                    if (arTaxCode.getTcType().equals("INCLUSIVE") || arTaxCode.getTcType().equals("EXCLUSIVE")) {
                        NET_AMOUNT = EJBCommon.roundIt(APPLY_AMOUNT / (1 + (arTaxCode.getTcRate() / 100)), this.getGlFcPrecisionUnit(companyCode));
                    } else {
                        NET_AMOUNT = APPLY_AMOUNT;
                    }
                    W_TAX_AMOUNT = EJBCommon.roundIt(NET_AMOUNT * (arInvoicePaymentSchedule.getArInvoice().getArWithholdingTaxCode().getWtcRate() / 100), this.getGlFcPrecisionUnit(companyCode));
                    APPLY_AMOUNT -= W_TAX_AMOUNT;
                }
                mdetails.setIpsAiApplyAmount(APPLY_AMOUNT);
                mdetails.setIpsAiPenaltyApplyAmount(PENALTY_APPLY_AMOUNT);
                mdetails.setIpsAiCreditableWTax(W_TAX_AMOUNT);
                mdetails.setIpsAiDiscountAmount(0d);
                mdetails.setIpsAiRebate(0d);
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

    public Integer saveArRctEntry(ArReceiptDetails details, boolean recalculateJournal, String BA_NM, String FC_NM, String CST_CSTMR_CODE, String RB_NM, ArrayList aiList, boolean isDraft, boolean isValidating, String PP_NM, Integer AD_BRNCH, Integer companyCode) throws ArReceiptEntryValidationException, GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, ArINVOverCreditBalancePaidapplicationNotAllowedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, ArINVOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, ArRCTInvoiceHasNoWTaxCodeException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException, GlobalRecordAlreadyAssignedException, AdPRFCoaGlCustomerDepositAccountNotFoundException, ArREDuplicatePayfileReferenceNumberException {
        Debug.print("ArReceiptEntryControllerBean saveArRctEntry");

        LocalArReceipt arReceipt = null;
        try {
            // validate if receipt is already deleted
            try {
                if (details.getRctCode() != null) {
                    arReceipt = arReceiptHome.findByPrimaryKey(details.getRctCode());
                }
            } catch (FinderException ex) {
                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if receipt is already posted, void, approved or pending
            if (details.getRctCode() != null && details.getRctVoid() == EJBCommon.FALSE) {
                if (arReceipt.getRctApprovalStatus() != null) {
                    if (arReceipt.getRctApprovalStatus().equals("APPROVED") || arReceipt.getRctApprovalStatus().equals("N/A")) {
                        throw new GlobalTransactionAlreadyApprovedException();
                    } else if (arReceipt.getRctApprovalStatus().equals("PENDING")) {
                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (arReceipt.getRctPosted() == EJBCommon.TRUE) {
                    throw new GlobalTransactionAlreadyPostedException();
                } else if (arReceipt.getRctVoid() == EJBCommon.TRUE) {
                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // check void
            /*
             * Check proceed if void is executed. Also if void is already void, throw and exception .
             */
            if (details.getRctCode() != null && details.getRctVoid() == EJBCommon.TRUE) {
                // Execute Void receipt
                this.executeVoidReceipt(details, isDraft, AD_BRNCH, companyCode, arReceipt);
                return arReceipt.getRctCode();
            }

            // validate if document number is unique document number is automatic then set
            // next sequence
            /*
             * receipt number will assign as default sequence if receipt number is default, hence, if its
             * have value, check if has a duplicate exist and throw exception if true*
             */
            this.receiptNumberValidation(details, AD_BRNCH, companyCode, arReceipt);

            // validate if conversion date exists
            /* This will check the conversion date if exist and currency is selected the non-default */
            this.checkConversionDateValidation(details, FC_NM, companyCode);

            /* this still not used */
            // get rem aining credit balance of customer and check if exceeds credibalance
            // paid
            double creditBalanceRemaining = this.getArRctCreditBalanceByCstCustomerCode(CST_CSTMR_CODE, AD_BRNCH, companyCode);

            /*
             * default by true, means. what ever changes or not. changes will apply entirely, including
             * the receipt line primary code will change too
             */
            // used in checking if receipt should re-generate distribution records and
            // re-calculate taxes
            boolean isRecalculate = true;

            // create new receipt
            /* NOT USED */
            boolean newReceipt = false;

            // create receipt
            if (details.getRctCode() == null) {
                arReceipt = arReceiptHome.RctType("COLLECTION").RctDescription(details.getRctDescription()).RctDate(details.getRctDate()).RctNumber(details.getRctNumber()).RctReferenceNumber(details.getRctReferenceNumber()).RctCheckNo(details.getRctCheckNo()).RctPayfileReferenceNumber(details.getRctPayfileReferenceNumber()).RctAmount(details.getRctAmount()).RctAmountCash(details.getRctAmount()).RctConversionDate(details.getRctConversionDate()).RctConversionRate(details.getRctConversionRate()).RctPaymentMethod(details.getRctPaymentMethod()).RctCreatedBy(details.getRctCreatedBy()).RctDateCreated(details.getRctDateCreated()).RctAdBranch(AD_BRNCH).RctAdCompany(companyCode).buildReceipt();

                arReceipt.setRctEnableAdvancePayment(details.getRctEnableAdvancePayment());
                arReceipt.setRctExcessAmount(details.getRctExcessAmount());

                recalculateJournal = true;
                newReceipt = true;

            } else {

                // check if critical fields are changed
                if (!arReceipt.getAdBankAccount().getBaName().equals(BA_NM) || !arReceipt.getArCustomer().getCstCustomerCode().equals(CST_CSTMR_CODE) || aiList.size() != arReceipt.getArAppliedInvoices().size()) {
                    isRecalculate = true;

                } else if (aiList.size() == arReceipt.getArAppliedInvoices().size()) {
                    Iterator aiIter = arReceipt.getArAppliedInvoices().iterator();
                    Iterator aiListIter = aiList.iterator();

                    while (aiIter.hasNext()) {
                        LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) aiIter.next();
                        ArModAppliedInvoiceDetails mdetails = (ArModAppliedInvoiceDetails) aiListIter.next();
                        if (!arAppliedInvoice.getArInvoicePaymentSchedule().getIpsCode().equals(mdetails.getAiIpsCode()) || arAppliedInvoice.getAiApplyAmount() != mdetails.getAiApplyAmount() || arAppliedInvoice.getAiPenaltyApplyAmount() != mdetails.getAiPenaltyApplyAmount() || arAppliedInvoice.getAiCreditableWTax() != mdetails.getAiCreditableWTax() || arAppliedInvoice.getAiDiscountAmount() != mdetails.getAiDiscountAmount() || arAppliedInvoice.getAiRebate() != mdetails.getAiRebate() || arAppliedInvoice.getAiAppliedDeposit() != mdetails.getAiAppliedDeposit() || arAppliedInvoice.getAiCreditBalancePaid() != mdetails.getAiCreditBalancePaid() || arAppliedInvoice.getAiAllocatedPaymentAmount() != mdetails.getAiAllocatedPaymentAmount()) {
                            isRecalculate = true;
                            break;
                        }
                        isRecalculate = false;
                    }
                } else {
                    isRecalculate = false;
                }

                arReceipt.setRctDescription(details.getRctDescription());
                arReceipt.setRctDocumentType(details.getRctDocumentType());
                arReceipt.setRctDate(details.getRctDate());
                arReceipt.setRctNumber(details.getRctNumber());
                arReceipt.setRctReferenceNumber(details.getRctReferenceNumber());
                arReceipt.setRctCheckNo(details.getRctCheckNo());
                arReceipt.setRctAmount(details.getRctAmount());
                arReceipt.setRctAmountCash(details.getRctAmount());
                arReceipt.setRctConversionDate(details.getRctConversionDate());
                arReceipt.setRctConversionRate(details.getRctConversionRate());
                arReceipt.setRctPaymentMethod(details.getRctPaymentMethod());
                arReceipt.setRctLastModifiedBy(details.getRctLastModifiedBy());
                arReceipt.setRctDateLastModified(details.getRctDateLastModified());
                arReceipt.setRctReasonForRejection(null);
                arReceipt.setRctCustomerName(null);
                arReceipt.setRctCustomerAddress(null);
                arReceipt.setRctEnableAdvancePayment(details.getRctEnableAdvancePayment());
                arReceipt.setRctExcessAmount(details.getRctExcessAmount());
            }

            arReceipt.setRctDocumentType(details.getRctDocumentType());
            arReceipt.setReportParameter(details.getReportParameter());

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);
            // adBankAccount.addArReceipt(arReceipt);
            arReceipt.setAdBankAccount(adBankAccount);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
            // glFunctionalCurrency.addArReceipt(arReceipt);
            arReceipt.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, companyCode);
            arReceipt.setArCustomer(arCustomer);
            // arCustomer.addArReceipt(arReceipt);

            if (details.getRctCustomerName().length() > 0 && !arCustomer.getCstName().equals(details.getRctCustomerName())) {
                arReceipt.setRctCustomerName(details.getRctCustomerName());
            }

            try {

                LocalArReceiptBatch arReceiptBatch = arReceiptBatchHome.findByRbName(RB_NM, AD_BRNCH, companyCode);
                arReceipt.setArReceiptBatch(arReceiptBatch);
                // arReceiptBatch.addArReceipt(arReceipt);

            } catch (FinderException ex) {

            }

            if (isRecalculate) {
                this.generateArDistributionRecords(recalculateJournal, aiList, AD_BRNCH, companyCode, arReceipt, adBankAccount);
            }

            // generate approval status
            String RCT_APPRVL_STATUS = null;
            if (!isDraft) {
                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if ar receipt approval is enabled
                if (adApproval.getAprEnableArReceipt() == EJBCommon.FALSE) {
                    RCT_APPRVL_STATUS = "N/A";

                    // create cm adjustment for inserting advance payment
                    if (details.getRctEnableAdvancePayment() != 0) {
                        this.postAdvncPymntByCmAdj(arReceipt, BA_NM, AD_BRNCH, companyCode);
                    }
                } else {
                    // check if receipt is self approved
                    LocalAdUser adUser = adUserHome.findByUsrName(details.getRctLastModifiedBy(), companyCode);
                    RCT_APPRVL_STATUS = arApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getRctLastModifiedBy(), adUser.getUsrDescription(), "AR RECEIPT", arReceipt.getRctCode(), arReceipt.getRctNumber(), arReceipt.getRctDate(), AD_BRNCH, companyCode);
                }
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            if (RCT_APPRVL_STATUS != null && RCT_APPRVL_STATUS.equals("N/A") && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                this.executeArRctPost(arReceipt, arReceipt.getRctLastModifiedBy(), AD_BRNCH, companyCode);
            }
            // set receipt approval status
            arReceipt.setRctApprovalStatus(RCT_APPRVL_STATUS);
            if (isValidating) {
                throw new ArReceiptEntryValidationException();
            }
            return arReceipt.getRctCode();

        } catch (ArReceiptEntryValidationException | AdPRFCoaGlCustomerDepositAccountNotFoundException |
                 GlobalRecordAlreadyAssignedException | GlobalBranchAccountNumberInvalidException |
                 GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
                 GlJREffectiveDateNoPeriodExistException | GlobalNoApprovalApproverFoundException |
                 GlobalNoApprovalRequesterFoundException | GlobalTransactionAlreadyLockedException |
                 ArRCTInvoiceHasNoWTaxCodeException | ArINVOverCreditBalancePaidapplicationNotAllowedException |
                 ArINVOverapplicationNotAllowedException | GlobalTransactionAlreadyVoidException |
                 GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
                 GlobalTransactionAlreadyApprovedException | GlobalConversionDateNotExistException |
                 GlobalDocumentNumberNotUniqueException | ArREDuplicatePayfileReferenceNumberException |
                 GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void generateArDistributionRecords(boolean recalculateJournal, ArrayList aiList, Integer AD_BRNCH, Integer companyCode, LocalArReceipt arReceipt, LocalAdBankAccount adBankAccount) throws RemoveException, FinderException, ArINVOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, ArRCTInvoiceHasNoWTaxCodeException, ArINVOverCreditBalancePaidapplicationNotAllowedException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlCustomerDepositAccountNotFoundException {


        // remove all distribution records
        Collection arDistributionRecords = arReceipt.getArDistributionRecords();
        Iterator i = arDistributionRecords.iterator();

        while (i.hasNext()) {

            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

            if (recalculateJournal) {
                i.remove();

                em.remove(arDistributionRecord);
            }
        }

        // release ips locks and remove all applied invoices
        Collection arAppliedInvoices = arReceipt.getArAppliedInvoices();

        i = arAppliedInvoices.iterator();

        while (i.hasNext()) {

            LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) i.next();

            arAppliedInvoice.getArInvoicePaymentSchedule().setIpsLock(EJBCommon.FALSE);

            // remove all applied credits
            try {

                Collection arAppliedCredits = arAppliedInvoice.getArAppliedCredits();

                Iterator x = arAppliedCredits.iterator();

                while (x.hasNext()) {

                    LocalArAppliedCredit arAppliedCredit = (LocalArAppliedCredit) x.next();

                    x.remove();

                    // arAppliedCredit.entityRemove();
                    em.remove(arAppliedCredit);
                }

            } catch (Exception ex) {

            }

            i.remove();

            // arAppliedInvoice.entityRemove();
            em.remove(arAppliedInvoice);
        }


        double totalApplyAmount = 0;
        double RCT_FRX_GN_LSS = 0d;

        double INVC_CONVERSION_RT = 0d;
        double RCT_CONVERSION_RT = 0d;


        LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
        LocalArAppliedInvoice arAppliedInvoice = null;

        // add new applied invoice and distribution record
        i = aiList.iterator();

        while (i.hasNext()) {

            ArModAppliedInvoiceDetails mAiDetails = (ArModAppliedInvoiceDetails) i.next();
            totalApplyAmount += mAiDetails.getAiApplyAmount() + mAiDetails.getAiPenaltyApplyAmount();

            //Create Applied Invoice
            arAppliedInvoice = this.addArAiEntry(mAiDetails, arReceipt, companyCode);
            arReceipt.addArAppliedInvoice(arAppliedInvoice);

            LocalGlFunctionalCurrency invcFC = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency();


            INVC_CONVERSION_RT = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate();
            RCT_CONVERSION_RT = arReceipt.getRctConversionRate();

            RCT_FRX_GN_LSS += arAppliedInvoice.getAiForexGainLoss();


            // create cred. withholding tax distribution record if necessary
            if (mAiDetails.getAiCreditableWTax() > 0) {

                Integer WTC_COA_CODE = null;

                double CREDITABLE_WTAX = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiCreditableWTax(), companyCode);

                if (arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArWithholdingTaxCode().getGlChartOfAccount() != null) {

                    WTC_COA_CODE = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArWithholdingTaxCode().getGlChartOfAccount().getCoaCode();

                } else {

                    adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
                    WTC_COA_CODE = adPreference.getArWithholdingTaxCode().getGlChartOfAccount().getCoaCode();
                }


                if (recalculateJournal) {
                    //Overwrite the AD_BRNCH to retrieve the correct COA
                    Integer branchCode = 1; // HEAD OFFICE
                    this.addArDrEntry(arReceipt.getArDrNextLine(), "W-TAX", EJBCommon.TRUE, CREDITABLE_WTAX, EJBCommon.FALSE, WTC_COA_CODE, arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);
                }
            }

            // create discount distribution records if necessary
            if (arAppliedInvoice.getAiDiscountAmount() != 0) {
                double DISCOUNT_AMOUNT = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiDiscountAmount(), companyCode);

                LocalAdBranchBankAccount adBranchBankAccount = null;
                try {
                    //Find Branch Bank Account if Exist.
                    adBranchBankAccount = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccount().getBaCode(), AD_BRNCH, companyCode);
                } catch (FinderException ex) {
                }

                if (recalculateJournal) {
                    this.addArDrEntry(arReceipt.getArDrNextLine(), "SALES DISCOUNT", EJBCommon.TRUE, DISCOUNT_AMOUNT, EJBCommon.FALSE, adBranchBankAccount.getBbaGlCoaSalesDiscountAccount(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);
                }
            }

            // create applied deposit distribution records if necessary
            if (arAppliedInvoice.getAiCreditBalancePaid() != 0) {

                // add branch bank account
                LocalAdBranchBankAccount adBranchBankAccount = null;

                double CREDIT_BALANCE_PAID = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arAppliedInvoice.getAiCreditBalancePaid(), companyCode);

                try {
                    adBranchBankAccount = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccount().getBaCode(), AD_BRNCH, companyCode);
                } catch (FinderException ex) {
                }
                if (recalculateJournal) {
                    this.addArDrEntry(arReceipt.getArDrNextLine(), "CREDITED BALANCE", EJBCommon.TRUE, CREDIT_BALANCE_PAID, EJBCommon.FALSE, adBranchBankAccount.getBbaGlCoaAdvanceAccount(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);
                }
            }

            // create applied deposit distribution records if necessary

            if (arAppliedInvoice.getAiAppliedDeposit() != 0) {

                double APPLIED_DEPOSIT = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiAppliedDeposit(), companyCode);

                adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

                if (adPreference.getPrfArGlCoaCustomerDepositAccount() == null) {
                    throw new AdPRFCoaGlCustomerDepositAccountNotFoundException();
                }

                if (recalculateJournal) {
                    this.addArDrEntry(arReceipt.getArDrNextLine(), "APPLIED DEPOSIT", EJBCommon.TRUE, APPLIED_DEPOSIT, EJBCommon.FALSE, adPreference.getPrfArGlCoaCustomerDepositAccount(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);
                }
            }

            // Get Service Charge Records
            Collection arScDistributionRecords = arDistributionRecordHome.findDrsByDrClassAndInvCode("SC", arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(), companyCode);

            Iterator scIter = arScDistributionRecords.iterator();

            double applyAmount = arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditBalancePaid() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiAppliedDeposit();
            double dueAmount = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvAmountDue();

            applyAmount = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), applyAmount, companyCode);

            dueAmount = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), dueAmount, companyCode);

            double scAmount = 0;
            while (scIter.hasNext()) {
                LocalArDistributionRecord arScDistributionRecord = (LocalArDistributionRecord) scIter.next();
                double scDrAmount = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arScDistributionRecord.getDrAmount(), companyCode);
                // scAmount += arScDistributionRecord.getDrAmount() * (applyAmount/dueAmount);
                scAmount += scDrAmount * (applyAmount / dueAmount);

                if (recalculateJournal) {
                    this.addArDrEntry(arReceipt.getArDrNextLine(), "RECEIVABLE", EJBCommon.FALSE, scAmount, EJBCommon.FALSE, arScDistributionRecord.getDrScAccount(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);
                    this.addArDrEntry(arReceipt.getArDrNextLine(), "SC", EJBCommon.TRUE, scAmount, EJBCommon.FALSE, arScDistributionRecord.getGlChartOfAccount().getCoaCode(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);
                }
            }

            // create rebate distribution records if necessary
            if (arAppliedInvoice.getAiRebate() != 0) {

                LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode("UNINTEREST", arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(), companyCode);

                LocalArDistributionRecord arDistributionRecordReceivable = arDistributionRecordHome.findByDrClassAndInvCode("RECEIVABLE INTEREST", arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(), companyCode);

                if (recalculateJournal) {
                    double REBATE_AMOUNT = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiRebate(), companyCode);

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "REBATE", EJBCommon.TRUE, REBATE_AMOUNT, EJBCommon.FALSE, arDistributionRecord.getGlChartOfAccount().getCoaCode(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);

                    this.addArDrEntry(arReceipt.getArDrNextLine(), "RECEIVABLE REBATE", EJBCommon.FALSE, REBATE_AMOUNT, EJBCommon.FALSE, arDistributionRecordReceivable.getGlChartOfAccount().getCoaCode(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);
                }

            } else {

                // earned interest
                try {
                    LocalAdBranchBankAccount adBranchBankAccount = null;
                    try {
                        adBranchBankAccount = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccount().getBaCode(), AD_BRNCH, companyCode);
                    } catch (FinderException ex) {
                    }

                    LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode("RECEIVABLE INTEREST", arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(), companyCode);
                    double dueInterest = arAppliedInvoice.getArInvoicePaymentSchedule().getIpsInterestDue();
                    double dueAmountMonthly = arAppliedInvoice.getArInvoicePaymentSchedule().getIpsAmountDue();

                    dueInterest = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), dueInterest, companyCode);

                    dueAmountMonthly = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), dueAmountMonthly, companyCode);

                    LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArCustomer().getCstCode(), AD_BRNCH, companyCode);

                    if (recalculateJournal) {

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "UNINTEREST", EJBCommon.TRUE, (applyAmount / dueAmountMonthly) * dueInterest, EJBCommon.FALSE, adBranchCustomer.getBcstGlCoaUnEarnedInterestAccount(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "EARNED INTEREST", EJBCommon.FALSE, (applyAmount / dueAmountMonthly) * dueInterest, EJBCommon.FALSE, adBranchCustomer.getBcstGlCoaEarnedInterestAccount(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "RECEIVABLE INTEREST", EJBCommon.FALSE, (applyAmount / dueAmountMonthly) * dueInterest, EJBCommon.FALSE, arDistributionRecord.getGlChartOfAccount().getCoaCode(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE, (applyAmount / dueAmountMonthly) * dueInterest, EJBCommon.FALSE, adBranchBankAccount.getBbaGlCoaCashAccount(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);
                    }
                } catch (FinderException ex) {

                }
            }

            // earned penalty
            if (arAppliedInvoice.getAiPenaltyApplyAmount() != 0) {

                try {
                    double applyPenaltyDue = arAppliedInvoice.getAiPenaltyApplyAmount();
                    applyPenaltyDue = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), applyPenaltyDue, companyCode);

                    LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArCustomer().getCstCode(), AD_BRNCH, companyCode);

                    if (recalculateJournal) {
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "UNPENALTY", EJBCommon.TRUE, applyPenaltyDue, EJBCommon.FALSE, adBranchCustomer.getBcstGlCoaUnEarnedPenaltyAccount(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "EARNED PENALTY", EJBCommon.FALSE, applyPenaltyDue, EJBCommon.FALSE, adBranchCustomer.getBcstGlCoaEarnedPenaltyAccount(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "RECEIVABLE PENALTY", EJBCommon.FALSE, arAppliedInvoice.getAiPenaltyApplyAmount(), EJBCommon.FALSE, adBranchCustomer.getBcstGlCoaReceivableAccount(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);
                    }
                } catch (FinderException ex) {
                }
            }

            // get receivable account TODO: ALWAYS GETTING ERROR BELOW
            try {
                LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode("RECEIVABLE", arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(), companyCode);

                double amount_crd = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditBalancePaid() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiAppliedDeposit() + arAppliedInvoice.getAiDiscountAmount(), companyCode);

                if (recalculateJournal) {
                    this.addArDrEntry(arReceipt.getArDrNextLine(), "RECEIVABLE", EJBCommon.FALSE, amount_crd, EJBCommon.FALSE, arDistributionRecord.getGlChartOfAccount().getCoaCode(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);
                }
            } catch (Exception ex) {
            }

            // reverse deferred tax if necessary
            LocalArTaxCode arTaxCode = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArTaxCode();

            if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT") && arTaxCode.getTcInterimAccount() != null) {
                try {
                    LocalArDistributionRecord arDeferredDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode("DEFERRED TAX", arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(), companyCode);

                    double DR_AMNT = EJBCommon.roundIt((arDeferredDistributionRecord.getDrAmount() / arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvAmountDue()) * (arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiAppliedDeposit()), this.getGlFcPrecisionUnit(companyCode));

                    DR_AMNT = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), DR_AMNT, companyCode);

                    if (recalculateJournal) {
                        this.addArDrEntry(arReceipt.getArDrNextLine(), "DEFERRED TAX", EJBCommon.TRUE, DR_AMNT, EJBCommon.FALSE, arDeferredDistributionRecord.getGlChartOfAccount().getCoaCode(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);

                        this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, DR_AMNT, EJBCommon.FALSE, arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArTaxCode().getGlChartOfAccount().getCoaCode(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);
                    }

                } catch (FinderException ex) {

                }
            }
        }


        if (RCT_CONVERSION_RT > INVC_CONVERSION_RT) {

            this.addArDrEntry(arReceipt.getArDrNextLine(), "FOREX GAIN", EJBCommon.TRUE, Math.abs(RCT_FRX_GN_LSS), EJBCommon.FALSE, adPreference.getPrfMiscPosGiftCertificateAccount(), arReceipt, null, AD_BRNCH, companyCode);
        }

        if (INVC_CONVERSION_RT > RCT_CONVERSION_RT) {

            this.addArDrEntry(arReceipt.getArDrNextLine(), "FOREX LOSS", EJBCommon.FALSE, Math.abs(RCT_FRX_GN_LSS), EJBCommon.FALSE, adPreference.getPrfMiscPosGiftCertificateAccount(), arReceipt, null, AD_BRNCH, companyCode);
        }

        // create cash distribution record

        // add branch bank account
        LocalAdBranchBankAccount adBranchBankAccount = null;

        try {
            adBranchBankAccount = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(adBankAccount.getBaCode(), AD_BRNCH, companyCode);

        } catch (FinderException ex) {

        }

        if (recalculateJournal) {
            if (totalApplyAmount != 0) {

                totalApplyAmount = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), totalApplyAmount, companyCode);

                this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE, totalApplyAmount, EJBCommon.FALSE, adBranchBankAccount.getBbaGlCoaCashAccount(), arReceipt, arAppliedInvoice, AD_BRNCH, companyCode);
            }
        }


        // Set New appliedInvoice
        arDistributionRecords = arReceipt.getArDistributionRecords();
        i = arDistributionRecords.iterator();

        while (i.hasNext()) {

            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();
            arDistributionRecord.setArAppliedInvoice(arAppliedInvoice);

        }
    }

    private void checkConversionDateValidation(ArReceiptDetails details, String FC_NM, Integer companyCode) throws GlobalConversionDateNotExistException {
        try {

            if (details.getRctConversionDate() != null) {

                LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

                if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getRctConversionDate(), companyCode);

                } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {
                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getRctConversionDate(), companyCode);
                }
            }
        } catch (FinderException ex) {
            throw new GlobalConversionDateNotExistException();
        }
    }

    private void receiptNumberValidation(ArReceiptDetails details, Integer AD_BRNCH, Integer companyCode, LocalArReceipt arReceipt) throws GlobalDocumentNumberNotUniqueException, ArREDuplicatePayfileReferenceNumberException {
        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

        if (details.getRctCode() == null) {

            String documentType = details.getRctDocumentType();

            try {
                if (documentType != null) {
                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode);
                } else {
                    documentType = "AR RECEIPT";
                }
            } catch (FinderException ex) {
                documentType = "AR RECEIPT";
            }

            try {

                adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode);

            } catch (FinderException ex) {

            }

            try {

                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

            } catch (FinderException ex) {

            }

            LocalArReceipt arExistingReceipt = null;
            try {
                arExistingReceipt = arReceiptHome.findByRctNumberAndBrCode(details.getRctNumber(), AD_BRNCH, companyCode);
            } catch (FinderException ex) {
            }

            if (arExistingReceipt != null) {
                throw new GlobalDocumentNumberNotUniqueException();
            }

            if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getRctNumber() == null || details.getRctNumber().trim().length() == 0)) {

                while (true) {

                    if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                        try {

                            arReceiptHome.findByRctNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, companyCode);
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                        } catch (FinderException ex) {

                            details.setRctNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            break;
                        }

                    } else {

                        try {

                            arReceiptHome.findByRctNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, companyCode);
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                        } catch (FinderException ex) {

                            details.setRctNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                            break;
                        }
                    }
                }
            }

        } else {
            LocalArReceipt arExistingReceipt = null;
            try {
                arExistingReceipt = arReceiptHome.findByRctNumberAndBrCode(details.getRctNumber(), AD_BRNCH, companyCode);
            } catch (FinderException ex) {
            }

            if (arExistingReceipt != null && !arExistingReceipt.getRctCode().equals(details.getRctCode())) {
                throw new GlobalDocumentNumberNotUniqueException();
            }

            if (arReceipt.getRctNumber() != details.getRctNumber() && (details.getRctNumber() == null || details.getRctNumber().trim().length() == 0)) {
                details.setRctNumber(arReceipt.getRctNumber());
            }
        }

        // validate if payfile Reference Number exist
        if (details.getRctPayfileReferenceNumber() != null) {
            if (!details.getRctPayfileReferenceNumber().equals("")) {
                try {
                    arReceiptHome.findByPayfileReferenceNumberAndCompanyCode(details.getRctPayfileReferenceNumber(), companyCode);
                    // if payfile referenceNumberFound
                    throw new ArREDuplicatePayfileReferenceNumberException();

                } catch (FinderException ex) {

                }
            }
        }
    }

    private void executeVoidReceipt(ArReceiptDetails details, boolean isDraft, Integer AD_BRNCH, Integer companyCode, LocalArReceipt arReceipt) throws GlobalTransactionAlreadyVoidException, GlobalRecordAlreadyAssignedException, FinderException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalBranchAccountNumberInvalidException, GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {
        if (arReceipt.getRctVoid() == EJBCommon.TRUE) {
            throw new GlobalTransactionAlreadyVoidException();
        }

        // check if receipt is already deposited
        if (!arReceipt.getCmFundTransferReceipts().isEmpty()) {
            throw new GlobalRecordAlreadyAssignedException();
        }

        if (arReceipt.getRctPosted() == EJBCommon.TRUE) {
            // generate approval status
            String RCT_APPRVL_STATUS = null;

            if (!isDraft) {
                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);
                // check if ar receipt approval is enabled
                if (adApproval.getAprEnableArReceipt() == EJBCommon.FALSE) {

                    RCT_APPRVL_STATUS = "N/A";

                } else {

                    // check if receipt is self approved

                    LocalAdUser adUser = adUserHome.findByUsrName(details.getRctLastModifiedBy(), companyCode);

                    RCT_APPRVL_STATUS = arApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getRctLastModifiedBy(), adUser.getUsrDescription(), "AR RECEIPT", arReceipt.getRctCode(), arReceipt.getRctNumber(), arReceipt.getRctDate(), AD_BRNCH, companyCode);
                }
            }

            // reverse distribution records

            Collection arDistributionRecords = arReceipt.getArDistributionRecords();
            ArrayList list = new ArrayList();

            Iterator i = arDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                list.add(arDistributionRecord);
            }

            i = list.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();
                this.addArDrEntry(arReceipt.getArDrNextLine(), arDistributionRecord.getDrClass(), arDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, arDistributionRecord.getDrAmount(), EJBCommon.TRUE, arDistributionRecord.getGlChartOfAccount().getCoaCode(), arReceipt, arDistributionRecord.getArAppliedInvoice(), AD_BRNCH, companyCode);
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (RCT_APPRVL_STATUS != null && RCT_APPRVL_STATUS.equals("N/A") && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                arReceipt.setRctVoid(EJBCommon.TRUE);
                this.executeArRctPost(
                        // arReceipt.getRctCode(), details.getRctLastModifiedBy(), AD_BRNCH, companyCode);
                        arReceipt, details.getRctLastModifiedBy(), AD_BRNCH, companyCode);
            }

            // set void approval status

            arReceipt.setRctVoidApprovalStatus(RCT_APPRVL_STATUS);

        } else {

            // release invoice lock

            Collection arLockedAppliedInvoices = arReceipt.getArAppliedInvoices();

            for (Object arLockedAppliedInvoice : arLockedAppliedInvoices) {

                LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) arLockedAppliedInvoice;

                arAppliedInvoice.getArInvoicePaymentSchedule().setIpsLock(EJBCommon.FALSE);
            }
        }

        arReceipt.setRctVoid(EJBCommon.TRUE);
        arReceipt.setRctLastModifiedBy(details.getRctLastModifiedBy());
        arReceipt.setRctDateLastModified(details.getRctDateLastModified());
    }

    public void deleteArRctEntry(Integer RCT_CODE, String AD_USR, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("ArReceiptEntryControllerBean deleteArRctEntry");

        try {

            LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);

            if (arReceipt.getRctApprovalStatus() != null && arReceipt.getRctApprovalStatus().equals("PENDING")) {

                Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AR RECEIPT", arReceipt.getRctCode(), companyCode);

                for (Object approvalQueue : adApprovalQueues) {

                    LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                    // adApprovalQueue.entityRemove();
                    em.remove(adApprovalQueue);
                }
            }

            // release lock and remove applied invoice

            Collection arAppliedInvoices = arReceipt.getArAppliedInvoices();

            Iterator i = arAppliedInvoices.iterator();

            while (i.hasNext()) {

                LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) i.next();

                arAppliedInvoice.getArInvoicePaymentSchedule().setIpsLock(EJBCommon.FALSE);

                // remove all applied credits
                try {
                    Collection arAppliedCredits = arAppliedInvoice.getArAppliedCredits();
                    Iterator x = arAppliedCredits.iterator();
                    while (x.hasNext()) {
                        LocalArAppliedCredit arAppliedCredit = (LocalArAppliedCredit) x.next();
                        x.remove();
                        // arAppliedCredit.entityRemove();
                        em.remove(arAppliedCredit);
                    }
                } catch (Exception ex) {
                }
                i.remove();
                // arAppliedInvoice.entityRemove();
                em.remove(arAppliedInvoice);
            }

            adDeleteAuditTrailHome.create("GL_JOURNAL", arReceipt.getRctDate(), arReceipt.getRctNumber(), arReceipt.getRctReferenceNumber(), arReceipt.getRctAmount(), AD_USR, new Date(), companyCode);

            // arReceipt.entityRemove();
            em.remove(arReceipt);

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

        Debug.print("ArReceiptEntryControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByRctCode(Integer RCT_CODE, Integer companyCode) {

        Debug.print("ArReceiptEntryControllerBean getAdApprovalNotifiedUsersByRctCode");

        ArrayList list = new ArrayList();

        try {

            LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);

            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;

            } else if (arReceipt.getRctVoid() == EJBCommon.TRUE && arReceipt.getRctVoidPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AR RECEIPT", RCT_CODE, companyCode);

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

    public byte getAdPrfEnableArReceiptBatch(Integer companyCode) {

        Debug.print("ArReceiptEntryControllerBean getAdPrfEnableArReceiptBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfEnableArReceiptBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArOpenRbAll(Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ArReceiptEntryControllerBean getArOpenRbAll");

        ArrayList list = new ArrayList();

        try {

            Collection arReceiptBatches = arReceiptBatchHome.findOpenRbByRbType("COLLECTION", AD_BRNCH, companyCode);

            for (Object receiptBatch : arReceiptBatches) {

                LocalArReceiptBatch arReceiptBatch = (LocalArReceiptBatch) receiptBatch;

                list.add(arReceiptBatch.getRbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getAiCreditBalancePaidByRctCodeByInvNum(Integer IPS_CODE, Integer RCT_CODE, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArReceiptEntryControllerBean getAiCreditBalancePaidByRctCodeByInvNum");

        double creditBalancePaid = 0d;

        try {
            Collection arAppliedInvoices = null;
            try {
                arAppliedInvoices = arAppliedInvoiceHome.findByIpsCodeAndRctCode(IPS_CODE, RCT_CODE, companyCode);
                for (Object appliedInvoice : arAppliedInvoices) {
                    LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoice;
                    creditBalancePaid += arAppliedInvoice.getAiCreditBalancePaid();
                }
            } catch (FinderException fx) {
                creditBalancePaid = 0d;
            }

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        return creditBalancePaid;
    }

    public double getArRctCreditBalanceByCstCustomerCode(String CST_CSTMR_CODE, Integer AD_BRANCH, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArReceiptEntryControllerBean getArRctCreditBalanceByCstCustomerCode");

        try {

            double creditbalanceRemaining = 0d;

            Collection cmAdjustments = null;
            Collection arAppliedInvoices = null;
            try {

                cmAdjustments = cmAdjustmentHome.findPostedAdjByCustomerCode(CST_CSTMR_CODE, companyCode);

                for (Object adjustment : cmAdjustments) {

                    LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) adjustment;
                    Collection arAppliedCredits = cmAdjustment.getArAppliedCredits();

                    Iterator x = arAppliedCredits.iterator();
                    double totalAppliedCredit = 0d;
                    while (x.hasNext()) {
                        LocalArAppliedCredit arAppliedCredit = (LocalArAppliedCredit) x.next();

                        totalAppliedCredit += arAppliedCredit.getAcApplyCredit();
                    }

                    creditbalanceRemaining += cmAdjustment.getAdjAmount() - totalAppliedCredit - cmAdjustment.getAdjRefundAmount();
                }

            } catch (FinderException ex) {

            }

            return EJBCommon.roundIt(creditbalanceRemaining, this.getGlFcPrecisionUnit(companyCode));

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public boolean checkIfExistArRctReferenceNumber(String RCT_RFRNC_NMBR, Integer companyCode) {
        Debug.print("ArReceiptEntryControllerBean checkIfExistArRctReferenceNumber");

        boolean isFound = false;
        try {

            LocalArReceipt arReceipt = null;

            Collection branchList = adBranchHome.findBrAll(companyCode);

            Iterator i = branchList.iterator();

            while (i.hasNext()) {
                try {

                    LocalAdBranch adBranch = (LocalAdBranch) i.next();
                    arReceipt = arReceiptHome.findByPayfileReferenceNumberAndCompanyCode(RCT_RFRNC_NMBR + " " + adBranch.getBrCode().toString(), companyCode);
                    return true;
                } catch (FinderException ex) {
                    isFound = false;
                }
            }
        } catch (FinderException ex) {
            return false;
        }
        return isFound;
    }

    public double getArRctDepositAmountByCstCustomerCode(String CST_CSTMR_CODE, Integer AD_BRANCH, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArReceiptEntryControllerBean getArRctDepositAmountByCstCustomerCode");

        try {

            double depositAmount = 0d;
            double draftAppliedDeposit = 0d;

            Collection arReceipts = null;

            try {

                arReceipts = arReceiptHome.findOpenDepositEnabledPostedRctByCstCustomerCode(CST_CSTMR_CODE, companyCode);

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

                arAppliedInvoices = arAppliedInvoiceHome.findUnpostedAiWithDepositByCstCustomerCode(CST_CSTMR_CODE, AD_BRANCH, companyCode);

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

    public double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("ArReceiptEntryControllerBean getFrRateByFrNameAndFrDate");

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
    private LocalArAppliedInvoice addArAiEntry(ArModAppliedInvoiceDetails mdetails, LocalArReceipt arReceipt, Integer companyCode) throws ArINVOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, ArRCTInvoiceHasNoWTaxCodeException, ArINVOverCreditBalancePaidapplicationNotAllowedException {

        Debug.print("ArReceiptEntryControllerBean addArAiEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // get functional currency name

            String FC_NM = adCompany.getGlFunctionalCurrency().getFcName();

            // validate overapplication
            LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arInvoicePaymentScheduleHome.findByPrimaryKey(mdetails.getAiIpsCode());

            double totalAmountDue = EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), this.getGlFcPrecisionUnit(companyCode));
            double totalApplyAmount = EJBCommon.roundIt(mdetails.getAiApplyAmount() + mdetails.getAiCreditBalancePaid() + mdetails.getAiCreditableWTax() + mdetails.getAiDiscountAmount(), this.getGlFcPrecisionUnit(companyCode));

            if (totalAmountDue < totalApplyAmount) {

                throw new ArINVOverapplicationNotAllowedException(arInvoicePaymentSchedule.getArInvoice().getInvNumber() + "-" + arInvoicePaymentSchedule.getIpsNumber());
            }

            double remainingCreditBalance = this.getArRctCreditBalanceByCstCustomerCode(arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstCustomerCode(), arInvoicePaymentSchedule.getArInvoice().getInvAdBranch(), companyCode);


            if (remainingCreditBalance < mdetails.getAiCreditBalancePaid()) {

                throw new ArINVOverCreditBalancePaidapplicationNotAllowedException(arInvoicePaymentSchedule.getArInvoice().getInvNumber() + "-" + arInvoicePaymentSchedule.getIpsNumber());
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

            if (!FC_NM.equals(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName()) || !FC_NM.equals(arReceipt.getGlFunctionalCurrency().getFcName())) {

                double AI_ALLCTD_PYMNT_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), mdetails.getAiApplyAmount() + mdetails.getAiCreditBalancePaid(), companyCode);

                double AI_APPLY_AMNT = this.convertForeignToFunctionalCurrency(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcCode(), arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName(), arInvoicePaymentSchedule.getArInvoice().getInvConversionDate(), arInvoicePaymentSchedule.getArInvoice().getInvConversionRate(), mdetails.getAiApplyAmount() + mdetails.getAiCreditBalancePaid(), companyCode);

                double AI_CRDTBL_W_TX = this.convertForeignToFunctionalCurrency(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcCode(), arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName(), arInvoicePaymentSchedule.getArInvoice().getInvConversionDate(), arInvoicePaymentSchedule.getArInvoice().getInvConversionRate(), mdetails.getAiCreditableWTax(), companyCode);

                double AI_DSCNT_AMNT = this.convertForeignToFunctionalCurrency(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcCode(), arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName(), arInvoicePaymentSchedule.getArInvoice().getInvConversionDate(), arInvoicePaymentSchedule.getArInvoice().getInvConversionRate(), mdetails.getAiDiscountAmount(), companyCode);

                double AI_APPLD_DPST = this.convertForeignToFunctionalCurrency(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcCode(), arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName(), arInvoicePaymentSchedule.getArInvoice().getInvConversionDate(), arInvoicePaymentSchedule.getArInvoice().getInvConversionRate(), mdetails.getAiAppliedDeposit(), companyCode);

                AI_FRX_GN_LSS = EJBCommon.roundIt((AI_ALLCTD_PYMNT_AMNT + AI_CRDTBL_W_TX + AI_DSCNT_AMNT + AI_APPLD_DPST) - (AI_APPLY_AMNT + AI_CRDTBL_W_TX + AI_DSCNT_AMNT + AI_APPLD_DPST), this.getGlFcPrecisionUnit(companyCode));

            }

            // create applied invoice

            LocalArAppliedInvoice arAppliedInvoice = arAppliedInvoiceHome.create(mdetails.getAiApplyAmount(), mdetails.getAiPenaltyApplyAmount(), mdetails.getAiCreditableWTax(), mdetails.getAiDiscountAmount(), mdetails.getAiRebate(), mdetails.getAiAppliedDeposit(), mdetails.getAiAllocatedPaymentAmount(), AI_FRX_GN_LSS, mdetails.getAiApplyRebate(), companyCode);

            Debug.print("------------------------>new arAppliedInvoice=" + arAppliedInvoice);
            arAppliedInvoice.setAiCreditBalancePaid(mdetails.getAiCreditBalancePaid());
            arInvoicePaymentSchedule.addArAppliedInvoice(arAppliedInvoice);

            // update cm ajustment advance remaining balance and create applied credit
            if (mdetails.getAiCreditBalancePaid() > 0) {

                Collection cmAdjustments = cmAdjustmentHome.findPostedAdjByCustomerCode(arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstCustomerCode(), companyCode);
                Iterator i = cmAdjustments.iterator();


                double creditBalancePaid = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), mdetails.getAiCreditBalancePaid(), companyCode);


                //Getting Customer Balance from cmAdjustment(s). and check how many left
                while (i.hasNext()) {

                    LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                    // StringBuilder strB = cmAdjustment.getAdjAmountAppliedLog();
                    Collection arAppliedCredits = cmAdjustment.getArAppliedCredits();

                    Iterator x = arAppliedCredits.iterator();
                    double totalAppliedCredit = 0d;
                    while (x.hasNext()) {
                        LocalArAppliedCredit arAppliedCredit = (LocalArAppliedCredit) x.next();

                        totalAppliedCredit += arAppliedCredit.getAcApplyCredit();
                    }

                    //how many left credit balance of customer
                    double advanceRemaining = EJBCommon.roundIt(cmAdjustment.getAdjAmount() - totalAppliedCredit - cmAdjustment.getAdjRefundAmount(), this.getGlFcPrecisionUnit(companyCode));


                    if (advanceRemaining <= 0) continue;

                    if (creditBalancePaid <= advanceRemaining) {

                        cmAdjustment.addArAppliedCredit(this.addArAcEntry(EJBCommon.roundIt(creditBalancePaid, this.getGlFcPrecisionUnit(companyCode)), arAppliedInvoice, companyCode));
                        creditBalancePaid -= creditBalancePaid;

                    } else {
                        cmAdjustment.addArAppliedCredit(this.addArAcEntry(EJBCommon.roundIt(advanceRemaining, this.getGlFcPrecisionUnit(companyCode)), arAppliedInvoice, companyCode));
                        creditBalancePaid -= advanceRemaining;
                    }

                    if (creditBalancePaid <= 0) break;
                }
            }

            // arReceipt.addArAppliedInvoice(arAppliedInvoice);
            // arAppliedInvoice.setArReceipt(arReceipt);
            // arAppliedInvoice.setArInvoicePaymentSchedule(arInvoicePaymentSchedule);
            // arInvoicePaymentSchedule.addArAppliedInvoice(arAppliedInvoice);

            // lock invoice

            arInvoicePaymentSchedule.setIpsLock(EJBCommon.TRUE);

            return arAppliedInvoice;

        } catch (ArINVOverapplicationNotAllowedException | ArRCTInvoiceHasNoWTaxCodeException |
                 GlobalTransactionAlreadyLockedException |
                 ArINVOverCreditBalancePaidapplicationNotAllowedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalArAppliedCredit addArAcEntry(double APPLY_AMNT, LocalArAppliedInvoice arAppliedInvoice, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArReceiptEntryControllerBean addArAcEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // create distribution record

            LocalArAppliedCredit arAppliedCredit = arAppliedCreditHome.create(EJBCommon.roundIt(APPLY_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), companyCode);

            // to be used by gl journal interface for cross currency receipts
            if (arAppliedInvoice != null) {

                arAppliedInvoice.addArAppliedCredit(arAppliedCredit);
            }

            return arAppliedCredit;

        } catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSD, Integer COA_CODE, LocalArReceipt arReceipt, LocalArAppliedInvoice arAppliedInvoice, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArReceiptEntryControllerBean addArDrEntry");

        try {

            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, companyCode);

            // create distribution record
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, DR_RVRSD, companyCode);

            arReceipt.addArDistributionRecord(arDistributionRecord);
            glChartOfAccount.addArDistributionRecord(arDistributionRecord);

            // to be used by gl journal interface for cross currency receipts
            if (arAppliedInvoice != null) {

                arAppliedInvoice.addArDistributionRecord(arDistributionRecord);
            }

        } catch (FinderException ex) {
            throw new GlobalBranchAccountNumberInvalidException();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("ArReceiptEntryControllerBean convertForeignToFunctionalCurrency");

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

    public String createAdvancePayment(ArReceiptDetails rctDetails, String customer_code, String BA_NM, String payfileReference, double advanceAmount, int AD_BRNCH, int AD_CMPNY) throws Exception {
        Debug.print("ArReceiptEntryControllerBean createAdvancePayment");
        // CREATE CM ADJUSTMENT ADVANCE

        CmAdjustmentDetails details = new CmAdjustmentDetails();

        details.setAdjCode(null);
        details.setAdjType("ADVANCE");
        details.setAdjDate(rctDetails.getRctDate());
        details.setAdjDocumentNumber(rctDetails.getRctNumber());
        details.setAdjReferenceNumber(payfileReference);
        details.setAdjAmount(advanceAmount);
        details.setAdjConversionDate(rctDetails.getRctConversionDate());
        details.setAdjConversionRate(rctDetails.getRctConversionRate());
        details.setAdjVoid(rctDetails.getRctVoid());
        details.setAdjMemo(rctDetails.getRctDescription());
        details.setAdjCreatedBy(rctDetails.getRctCreatedBy());
        details.setAdjDateCreated(new java.util.Date());

        details.setAdjLastModifiedBy(rctDetails.getRctLastModifiedBy());
        details.setAdjDateLastModified(new java.util.Date());

        // validate reference Number

        if (!payfileReference.equals("")) {

            try {

                LocalCmAdjustment cmAdjustment = cmAdjustmentHome.findAdjByReferenceNumber(payfileReference, AD_CMPNY);

                // this code if there's existing reference number
                return "Already Posted/Created ADVANCE";
            } catch (FinderException ex) {
                // no dubplicate
            }
        }

        /// validate document number is unique

        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

        try {

            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("CM ADJUSTMENT", AD_CMPNY);

        } catch (FinderException ex) {

        }

        try {

            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

        } catch (FinderException ex) {

        }

        LocalCmAdjustment cmExistingAdjustment = null;

        try {

            cmExistingAdjustment = cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(details.getAdjDocumentNumber(), AD_BRNCH, AD_CMPNY);

        } catch (FinderException ex) {
        }

        if (cmExistingAdjustment != null) {

            return "Document Number not Unique";
            // throw new GlobalDocumentNumberNotUniqueException();

        }

        if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getAdjDocumentNumber() == null || details.getAdjDocumentNumber().trim().length() == 0)) {

            while (true) {

                if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                    try {

                        cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                    } catch (FinderException ex) {

                        details.setAdjDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                        break;
                    }

                } else {

                    try {

                        cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                    } catch (FinderException ex) {

                        details.setAdjDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                        break;
                    }
                }
            }
        }

        // validate if conversion date exists

        try {

            if (details.getAdjConversionDate() != null) {

                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
                LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), AD_CMPNY);

                if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getAdjConversionDate(), AD_CMPNY);

                } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getAdjConversionDate(), AD_CMPNY);
                }
            }

        } catch (FinderException ex) {

            return "Conversion Date Not Exist";
            // throw new GlobalConversionDateNotExistException();

        }

        LocalCmAdjustment cmAdjustment = null;
        try {

            cmAdjustment = cmAdjustmentHome.create(details.getAdjType(), details.getAdjDate(), details.getAdjDocumentNumber(), details.getAdjReferenceNumber(), details.getAdjCheckNumber(), details.getAdjAmount(), 0d, details.getAdjConversionDate(), details.getAdjConversionRate(), details.getAdjMemo(), null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, 0d, null, EJBCommon.FALSE, null, null, EJBCommon.FALSE, details.getAdjCreatedBy(), details.getAdjDateCreated(), details.getAdjLastModifiedBy(), details.getAdjDateLastModified(), null, null, null, null, null, AD_BRNCH, AD_CMPNY);

            cmAdjustment.setAdjApprovalStatus("N/A");

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, AD_CMPNY);
            adBankAccount.addCmAdjustment(cmAdjustment);

            // remove all distribution records
            Collection cmDistributionRecords = cmAdjustment.getCmDistributionRecords();
            Iterator i = cmDistributionRecords.iterator();
            while (i.hasNext()) {
                LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) i.next();
                i.remove();
                // cmDistributionRecord.entityRemove();
                em.remove(cmDistributionRecord);
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            // add branch bank account
            LocalAdBranchBankAccount adBranchBankAccount = null;

            try {
                adBranchBankAccount = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(adBankAccount.getBaCode(), AD_BRNCH, AD_CMPNY);

            } catch (FinderException ex) {

            }

            this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "CASH", EJBCommon.TRUE, cmAdjustment.getAdjAmount(), adBranchBankAccount.getBbaGlCoaCashAccount(), cmAdjustment, AD_BRNCH, AD_CMPNY);

            this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "ADVANCE", EJBCommon.FALSE, cmAdjustment.getAdjAmount(), adBranchBankAccount.getBbaGlCoaAdvanceAccount(), cmAdjustment, AD_BRNCH, AD_CMPNY);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(customer_code, AD_CMPNY);

            arCustomer.addCmAdjustment(cmAdjustment);


            this.executeCmAdjPost(cmAdjustment.getAdjCode(), cmAdjustment.getAdjLastModifiedBy(), AD_BRNCH, AD_CMPNY);
        } catch (Exception ex) {
            throw ex;
        }

        return "Success Creating ADVANCE";
    }

    private void postAdvncPymntByCmAdj(LocalArReceipt arReceipt, String BA_NM, Integer AD_BRNCH, Integer companyCode) throws Exception {

        Debug.print("ArReceiptEntryControllerBean saveCmAdjEntry");
        // CREATE CM ADJUSTMENT ADVANCE

        CmAdjustmentDetails details = new CmAdjustmentDetails();

        details.setAdjCode(null);
        details.setAdjType("ADVANCE");
        details.setAdjDate(arReceipt.getRctDate());
        details.setAdjDocumentNumber("");
        details.setAdjReferenceNumber(arReceipt.getRctNumber());
        details.setAdjAmount(arReceipt.getRctExcessAmount());
        details.setAdjConversionDate(arReceipt.getRctConversionDate());
        details.setAdjConversionRate(arReceipt.getRctConversionRate());
        details.setAdjMemo("");
        details.setAdjVoid(arReceipt.getRctVoid());

        details.setAdjCreatedBy(arReceipt.getRctCreatedBy());
        details.setAdjDateCreated(new java.util.Date());

        details.setAdjLastModifiedBy(arReceipt.getRctLastModifiedBy());
        details.setAdjDateLastModified(new java.util.Date());

        /// validate document number is unique

        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

        try {

            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("CM ADJUSTMENT", companyCode);

        } catch (FinderException ex) {

        }

        try {

            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

        } catch (FinderException ex) {

        }

        LocalCmAdjustment cmExistingAdjustment = null;

        try {

            cmExistingAdjustment = cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(details.getAdjDocumentNumber(), AD_BRNCH, companyCode);

        } catch (FinderException ex) {
        }

        if (cmExistingAdjustment != null) {

            throw new GlobalDocumentNumberNotUniqueException();
        }

        if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getAdjDocumentNumber() == null || details.getAdjDocumentNumber().trim().length() == 0)) {

            while (true) {

                if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                    try {

                        cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, companyCode);
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                    } catch (FinderException ex) {

                        details.setAdjDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                        break;
                    }

                } else {

                    try {

                        cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, companyCode);
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                    } catch (FinderException ex) {

                        details.setAdjDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                        break;
                    }
                }
            }
        }

        // validate if conversion date exists

        try {

            if (details.getAdjConversionDate() != null) {

                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);

                if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getAdjConversionDate(), companyCode);

                } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getAdjConversionDate(), companyCode);
                }
            }

        } catch (FinderException ex) {

            throw new GlobalConversionDateNotExistException();
        }

        LocalCmAdjustment cmAdjustment = null;
        try {

            cmAdjustment = cmAdjustmentHome.create(details.getAdjType(), details.getAdjDate(), details.getAdjDocumentNumber(), details.getAdjReferenceNumber(), details.getAdjCheckNumber(), details.getAdjAmount(), 0d, details.getAdjConversionDate(), details.getAdjConversionRate(), details.getAdjMemo(), null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, 0d, null, EJBCommon.FALSE, null, null, EJBCommon.FALSE, details.getAdjCreatedBy(), details.getAdjDateCreated(), details.getAdjLastModifiedBy(), details.getAdjDateLastModified(), null, null, null, null, null, AD_BRNCH, companyCode);

            cmAdjustment.setAdjApprovalStatus("N/A");

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);
            adBankAccount.addCmAdjustment(cmAdjustment);

            // remove all distribution records
            Collection cmDistributionRecords = cmAdjustment.getCmDistributionRecords();
            Iterator i = cmDistributionRecords.iterator();
            while (i.hasNext()) {
                LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) i.next();
                i.remove();
                // cmDistributionRecord.entityRemove();
                em.remove(cmDistributionRecord);
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // add branch bank account
            LocalAdBranchBankAccount adBranchBankAccount = null;

            try {
                adBranchBankAccount = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(adBankAccount.getBaCode(), AD_BRNCH, companyCode);

            } catch (FinderException ex) {

            }

            this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "CASH", EJBCommon.TRUE, cmAdjustment.getAdjAmount(), adBranchBankAccount.getBbaGlCoaCashAccount(), cmAdjustment, AD_BRNCH, companyCode);

            this.addCmDrEntry(cmAdjustment.getCmDrNextLine(), "ADVANCE", EJBCommon.FALSE, cmAdjustment.getAdjAmount(), adBranchBankAccount.getBbaGlCoaAdvanceAccount(), cmAdjustment, AD_BRNCH, companyCode);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode(), companyCode);

            arCustomer.addCmAdjustment(cmAdjustment);

            this.executeCmAdjPost(cmAdjustment.getAdjCode(), cmAdjustment.getAdjLastModifiedBy(), AD_BRNCH, companyCode);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void executeCmAdjPost(Integer ADJ_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("ArReceiptEntryControllerBean executeCmAdjPost");

        LocalCmAdjustment cmAdjustment = null;

        try {

            // validate if adjustment is already deleted

            try {

                cmAdjustment = cmAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted or void

            if (cmAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

            } else if (cmAdjustment.getAdjVoid() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidException();
            }

            // post adjustment

            if (cmAdjustment.getAdjVoid() == EJBCommon.FALSE && cmAdjustment.getAdjPosted() == EJBCommon.FALSE) {

                if (cmAdjustment.getAdjType().equals("INTEREST") || cmAdjustment.getAdjType().equals("DEBIT MEMO")) {

                    // increase bank account balances

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmAdjustment.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(cmAdjustment.getAdjDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), adBankAccountBalance.getBabBalance() + cmAdjustment.getAdjAmount(), "BOOK", companyCode);

                                adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + cmAdjustment.getAdjAmount());
                            }

                        } else {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), (cmAdjustment.getAdjAmount()), "BOOK", companyCode);

                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + cmAdjustment.getAdjAmount());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }

                } else {

                    // decrease bank account balances

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmAdjustment.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(cmAdjustment.getAdjDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), adBankAccountBalance.getBabBalance() - cmAdjustment.getAdjAmount(), "BOOK", companyCode);

                                adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - cmAdjustment.getAdjAmount());
                            }

                        } else {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), (0 - cmAdjustment.getAdjAmount()), "BOOK", companyCode);

                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - cmAdjustment.getAdjAmount());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
            }

            // set adjcher post status

            cmAdjustment.setAdjPosted(EJBCommon.TRUE);
            cmAdjustment.setAdjPostedBy(USR_NM);
            cmAdjustment.setAdjDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            if (adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(cmAdjustment.getAdjDate(), companyCode);

                } catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), cmAdjustment.getAdjDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection cmDistributionRecords = cmDistributionRecordHome.findByDrReversalAndDrImportedAndAdjCode(EJBCommon.FALSE, EJBCommon.FALSE, cmAdjustment.getAdjCode(), companyCode);

                Iterator j = cmDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) j.next();

                    double DR_AMNT = this.convertForeignToFunctionalCurrency(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode(), cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcName(), cmAdjustment.getAdjConversionDate(), cmAdjustment.getAdjConversionRate(), cmDistributionRecord.getDrAmount(), companyCode);

                    if (cmDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

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

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("CASH MANAGEMENT", "BANK ADJUSTMENTS", companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (cmDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (cmDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                    }

                    LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                    glChartOfAccount.addGlJournalLine(glOffsetJournalLine);

                } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                    throw new GlobalJournalNotBalanceException();
                }

                // create journal batch if necessary

                LocalGlJournalBatch glJournalBatch = null;
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

                try {

                    glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " BANK ADJUSTMENTS", AD_BRNCH, companyCode);

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                    glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " BANK ADJUSTMENTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(cmAdjustment.getAdjReferenceNumber(), cmAdjustment.getAdjMemo(), cmAdjustment.getAdjDate(), 0.0d, null, cmAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, AD_BRNCH, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("CASH MANAGEMENT", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("BANK ADJUSTMENTS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = cmDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) j.next();

                    double DR_AMNT = this.convertForeignToFunctionalCurrency(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode(), cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcName(), cmAdjustment.getAdjConversionDate(), cmAdjustment.getAdjConversionRate(), cmDistributionRecord.getDrAmount(), companyCode);

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(cmDistributionRecord.getDrLine(), cmDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    cmDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);

                    glJournal.addGlJournalLine(glJournalLine);

                    cmDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation
                    if ((!Objects.equals(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode()))) {

                        double CONVERSION_RATE = 1;

                        if (cmAdjustment.getAdjConversionRate() != 0 && cmAdjustment.getAdjConversionRate() != 1) {

                            CONVERSION_RATE = cmAdjustment.getAdjConversionRate();

                        } else if (cmAdjustment.getAdjConversionDate() != null) {

                            CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);
                        }

                        Collection glForexLedgers = null;

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(cmAdjustment.getAdjDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        } catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(cmAdjustment.getAdjDate()) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = cmDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        glForexLedger = glForexLedgerHome.create(cmAdjustment.getAdjDate(), FRL_LN, "OTH", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, 0d, companyCode);

                        glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                        } catch (FinderException ex) {

                        }

                        for (Object forexLedger : glForexLedgers) {

                            glForexLedger = (LocalGlForexLedger) forexLedger;
                            FRL_AMNT = cmDistributionRecord.getDrAmount();

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                            else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                            glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);
                        }
                    }
                }

                if (glOffsetJournalLine != null) {

                    glJournal.addGlJournalLine(glOffsetJournalLine);
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

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) break;
                        }
                    }
                }
            }

        } catch (GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyVoidException |
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

    public boolean validateDuplicateCollectionTransaction(ArReceiptDetails details, Integer AD_CMPNY) {
        Debug.print("ArReceiptEntryControllerBean validateDuplicateCollectionTransaction");

        try {

            String referenceNumber = details.getRctReferenceNumber();
            Collection lists = arReceiptHome.findAllCollectionByReferenceNumber(referenceNumber, AD_CMPNY);

            if (lists.size() > 0) {
                return false;
            } else {
                return true;
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

    }


    private void addCmDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalCmAdjustment cmAdjustment, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArReceiptEntryControllerBean addCmDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, companyCode);

            // create distribution record

            LocalCmDistributionRecord cmDistributionRecord = cmDistributionRecordHome.create(DR_LN, DR_CLSS, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_DBT, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            cmAdjustment.addCmDistributionRecord(cmDistributionRecord);
            glChartOfAccount.addCmDistributionRecord(cmDistributionRecord);
        } catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeArRctPost(LocalArReceipt arReceipt, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {
        Debug.print("ArReceiptEntryControllerBean executeArRctPost");
        // LocalArReceipt arReceipt = null;
        try {

            // validate if receipt is already deleted
            /*
             * try {
             *
             * arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);
             *
             * } catch (FinderException ex) {
             *
             * throw new GlobalRecordAlreadyDeletedException(); }
             */
            // validate if receipt is already posted

            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

                // validate if receipt void is already posted

            } else if (arReceipt.getRctVoid() == EJBCommon.TRUE && arReceipt.getRctVoidPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidPostedException();
            }

            // post receipt
            Collection arDepositReceipts = null;
            LocalArReceipt arDepositReceipt = null;
            LocalArCustomer arCustomer = arReceipt.getArCustomer();

            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctPosted() == EJBCommon.FALSE) {
                if (arReceipt.getRctType().equals("COLLECTION")) {
                    double RCT_CRDTS = 0d;
                    double RCT_AI_APPLD_DPSTS = 0d;
                    double RCT_EXCSS_AMNT = arReceipt.getRctExcessAmount();
                    // create adjustment for advance payment
                    if (arReceipt.getRctEnableAdvancePayment() != 0) {
                    }
                    // increase amount paid in invoice payment schedules and invoice
                    Collection arAppliedInvoices = arAppliedInvoiceHome.findByRctCode(arReceipt.getRctCode(), companyCode);

                    for (Object appliedInvoice : arAppliedInvoices) {
                        LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoice;

                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arAppliedInvoice.getArInvoicePaymentSchedule();

                        double AMOUNT_PAID = arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiAppliedDeposit() + arAppliedInvoice.getAiCreditBalancePaid() + arAppliedInvoice.getAiRebate();

                        double PENALTY_PAID = arAppliedInvoice.getAiPenaltyApplyAmount();

                        RCT_CRDTS += this.convertForeignToFunctionalCurrency(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcCode(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiCreditableWTax(), companyCode);

                        RCT_AI_APPLD_DPSTS += this.convertForeignToFunctionalCurrency(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcCode(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiAppliedDeposit(), companyCode);

                        arInvoicePaymentSchedule.setIpsAmountPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountPaid() + AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));
                        arInvoicePaymentSchedule.setIpsPenaltyPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsPenaltyPaid() + PENALTY_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        arInvoicePaymentSchedule.getArInvoice().setInvAmountPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getArInvoice().getInvAmountPaid() + AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        arInvoicePaymentSchedule.getArInvoice().setInvPenaltyPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getArInvoice().getInvPenaltyPaid() + PENALTY_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        // release invoice lock

                        arInvoicePaymentSchedule.setIpsLock(EJBCommon.FALSE);
                    }

                    // decrease customer balance
                    double RCT_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), companyCode);

                    this.updateCustomerBalance(arReceipt.getRctDate(), (RCT_AMNT + RCT_CRDTS + RCT_AI_APPLD_DPSTS) * -1, arReceipt.getArCustomer(), companyCode);
                }

                // increase bank balance
                LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());

                try {

                    // find bankaccount balance before or equal receipt date

                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    if (!adBankAccountBalances.isEmpty()) {

                        // get last check

                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmount(), "BOOK", companyCode);

                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmount());
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmount()), "BOOK", companyCode);

                        adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmount());
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

                // set receipt post status
                arReceipt.setRctPosted(EJBCommon.TRUE);
                arReceipt.setRctPostedBy(USR_NM);
                arReceipt.setRctDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            } else if (arReceipt.getRctVoid() == EJBCommon.TRUE && arReceipt.getRctVoidPosted() == EJBCommon.FALSE) { // void
                // receipt
                if (arReceipt.getRctType().equals("COLLECTION")) {

                    double RCT_CRDTS = 0d;
                    double RCT_AI_APPLD_DPSTS = 0d;

                    // decrease amount paid in invoice payment schedules and invoice
                    Collection arAppliedInvoices = arReceipt.getArAppliedInvoices();
                    for (Object appliedInvoice : arAppliedInvoices) {

                        LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoice;

                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arAppliedInvoice.getArInvoicePaymentSchedule();

                        double AMOUNT_PAID = arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiAppliedDeposit() + arAppliedInvoice.getAiCreditBalancePaid() + arAppliedInvoice.getAiRebate();

                        double PENALTY_PAID = arAppliedInvoice.getAiPenaltyApplyAmount();

                        RCT_CRDTS += this.convertForeignToFunctionalCurrency(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcCode(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiCreditableWTax(), companyCode);

                        RCT_AI_APPLD_DPSTS += this.convertForeignToFunctionalCurrency(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcCode(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(), arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), arAppliedInvoice.getAiAppliedDeposit(), companyCode);

                        try {
                            Collection arAppliedCredits = arAppliedInvoice.getArAppliedCredits();
                            Iterator x = arAppliedCredits.iterator();
                            while (x.hasNext()) {
                                LocalArAppliedCredit arAppliedCredit = (LocalArAppliedCredit) x.next();
                                x.remove();
                                // arAppliedCredit.entityRemove();
                                em.remove(arAppliedCredit);
                            }

                        } catch (Exception ex) {

                        }

                        arInvoicePaymentSchedule.setIpsAmountPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountPaid() - AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));
                        arInvoicePaymentSchedule.setIpsPenaltyPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsPenaltyPaid() - PENALTY_PAID, this.getGlFcPrecisionUnit(companyCode)));

                        arInvoicePaymentSchedule.getArInvoice().setInvAmountPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getArInvoice().getInvAmountPaid() - AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode)));
                        arInvoicePaymentSchedule.getArInvoice().setInvPenaltyPaid(EJBCommon.roundIt(arInvoicePaymentSchedule.getArInvoice().getInvPenaltyPaid() - PENALTY_PAID, this.getGlFcPrecisionUnit(companyCode)));
                    }

                    // increase customer balance
                    double RCT_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), companyCode);

                    this.updateCustomerBalance(arReceipt.getRctDate(), RCT_AMNT + RCT_CRDTS + RCT_AI_APPLD_DPSTS, arReceipt.getArCustomer(), companyCode);
                }

                // decrease bank balance
                LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());

                try {

                    // find bankaccount balance before or equal receipt date

                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    if (!adBankAccountBalances.isEmpty()) {

                        // get last check

                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmount(), "BOOK", companyCode);

                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmount());
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (0 - arReceipt.getRctAmount()), "BOOK", companyCode);

                        adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmount());
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

                // set receipt post status
                arReceipt.setRctVoidPosted(EJBCommon.TRUE);
                arReceipt.setRctPostedBy(USR_NM);
                arReceipt.setRctDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
            }

            // post to gl if necessary
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(arReceipt.getRctDate(), companyCode);

                } catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), arReceipt.getRctDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting
                LocalGlJournalLine glOffsetJournalLine = null;
                Collection arDistributionRecords = null;
                if (arReceipt.getRctVoid() == EJBCommon.FALSE) {

                    arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.FALSE, arReceipt.getRctCode(), companyCode);

                } else {

                    arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(EJBCommon.TRUE, arReceipt.getRctCode(), companyCode);
                }

                Iterator j = arDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    if (arDistributionRecord.getArAppliedInvoice() != null) {

                        LocalArInvoice arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();

                        DR_AMNT = arDistributionRecord.getDrAmount();

                    } else {

                        DR_AMNT = arDistributionRecord.getDrAmount();
                    }

                    if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

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

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS RECEIVABLES", "SALES RECEIPTS", companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                    }

                    LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                    glChartOfAccount.addGlJournalLine(glOffsetJournalLine);

                } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {
                    throw new GlobalJournalNotBalanceException();
                }

                // create journal batch if necessary

                LocalGlJournalBatch glJournalBatch = null;
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

                try {

                    if (adPreference.getPrfEnableArReceiptBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arReceipt.getArReceiptBatch().getRbName(), AD_BRNCH, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES RECEIPTS", AD_BRNCH, companyCode);
                    }

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                    if (adPreference.getPrfEnableArReceiptBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arReceipt.getArReceiptBatch().getRbName(), "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES RECEIPTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);
                    }
                }

                // create journal entry
                String customerName = arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName();

                LocalGlJournal glJournal = glJournalHome.create(arReceipt.getRctReferenceNumber(), arReceipt.getRctDescription(), arReceipt.getRctDate(), 0.0d, null, arReceipt.getRctNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), arReceipt.getArCustomer().getCstTin(), customerName, EJBCommon.FALSE, null, AD_BRNCH, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS RECEIVABLES", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("SALES RECEIPTS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = arDistributionRecords.iterator();
                boolean firstFlag = true;

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    double DR_AMNT = 0d;
                    LocalArInvoice arInvoice = null;

                    if (arDistributionRecord.getArAppliedInvoice() != null) {

                        arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();

                        DR_AMNT = arDistributionRecord.getDrAmount();
                    } else {

                        DR_AMNT = arDistributionRecord.getDrAmount();
                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(arDistributionRecord.getDrLine(), arDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    arDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);

                    glJournal.addGlJournalLine(glJournalLine);

                    arDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation

                    int FC_CODE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getGlFunctionalCurrency().getFcCode() : arReceipt.getGlFunctionalCurrency().getFcCode();

                    if ((FC_CODE != adCompany.getGlFunctionalCurrency().getFcCode()) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (FC_CODE == glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode())) {

                        double CONVERSION_RATE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getInvConversionRate() : arReceipt.getRctConversionRate();

                        Date DATE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getInvConversionDate() : arReceipt.getRctConversionDate();

                        if (DATE != null && (CONVERSION_RATE == 0 || CONVERSION_RATE == 1)) {

                            CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);

                        } else if (CONVERSION_RATE == 0) {

                            CONVERSION_RATE = 1;
                        }

                        Collection glForexLedgers = null;

                        DATE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getInvDate() : arReceipt.getRctDate();

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(DATE, glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        } catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(DATE) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = arDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        double FRX_GN_LSS = 0d;

                        if (glOffsetJournalLine != null && firstFlag) {

                            if (glOffsetJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                                FRX_GN_LSS = (glOffsetJournalLine.getJlDebit() == EJBCommon.TRUE ? glOffsetJournalLine.getJlAmount() : (-1 * glOffsetJournalLine.getJlAmount()));
                            else
                                FRX_GN_LSS = (glOffsetJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * glOffsetJournalLine.getJlAmount()) : glOffsetJournalLine.getJlAmount());

                            firstFlag = false;
                        }

                        glForexLedger = glForexLedgerHome.create(DATE, FRL_LN, "OR", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, FRX_GN_LSS, companyCode);

                        glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                        } catch (FinderException ex) {

                        }

                        for (Object forexLedger : glForexLedgers) {

                            glForexLedger = (LocalGlForexLedger) forexLedger;
                            FRL_AMNT = arDistributionRecord.getDrAmount();

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                            else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                            glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);
                        }
                    }
                }

                if (glOffsetJournalLine != null) {

                    glJournal.addGlJournalLine(glOffsetJournalLine);
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

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) break;
                        }
                    }
                }

                if (arReceipt.getArCustomer().getApSupplier() != null) {

                    // Post Investors Account balance
                    byte scLedger = arReceipt.getArCustomer().getApSupplier().getApSupplierClass().getScLedger();

                    // post current to current acv
                    if (scLedger == EJBCommon.TRUE) {

                        this.postToGlInvestor(glAccountingCalendarValue, arReceipt.getArCustomer().getApSupplier(), true, arReceipt.getRctInvtrBeginningBalance(), arReceipt.getRctVoid() == EJBCommon.FALSE ? EJBCommon.FALSE : EJBCommon.TRUE, arReceipt.getRctVoid() == EJBCommon.FALSE ? arReceipt.getRctAmount() : -arReceipt.getRctAmount(), companyCode);

                        // post to subsequent acvs (propagate)

                        Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                        for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                            this.postToGlInvestor(glSubsequentAccountingCalendarValue, arReceipt.getArCustomer().getApSupplier(), false, EJBCommon.FALSE, arReceipt.getRctVoid() == EJBCommon.FALSE ? EJBCommon.FALSE : EJBCommon.TRUE, arReceipt.getRctVoid() == EJBCommon.FALSE ? arReceipt.getRctAmount() : -arReceipt.getRctAmount(), companyCode);
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

                                    this.postToGlInvestor(glSubsequentAccountingCalendarValue, arReceipt.getArCustomer().getApSupplier(), false, arReceipt.getRctInvtrBeginningBalance(), arReceipt.getRctVoid() == EJBCommon.FALSE ? EJBCommon.FALSE : EJBCommon.TRUE, arReceipt.getRctVoid() == EJBCommon.FALSE ? arReceipt.getRctAmount() : -arReceipt.getRctAmount(), companyCode);
                                }

                                if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) break;
                            }
                        }
                    }
                }
            }

        } catch (GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyVoidPostedException |
                 GlobalTransactionAlreadyPostedException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }  // } catch (GlobalRecordAlreadyDeletedException ex) {
        // getSessionContext().setRollbackOnly();
        // throw ex;
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void updateCustomerBalance(Date RCT_DT, double RCT_AMNT, LocalArCustomer arCustomer, Integer companyCode) {
        Debug.print("ArReceiptEntryControllerBean updateCustomerBalance");
        try {
            // find customer balance before or equal invoice date
            Collection arCustomerBalances = arCustomerBalanceHome.findByBeforeOrEqualInvDateAndCstCustomerCode(RCT_DT, arCustomer.getCstCustomerCode(), companyCode);
            if (!arCustomerBalances.isEmpty()) {
                // get last invoice
                ArrayList arCustomerBalanceList = new ArrayList(arCustomerBalances);
                LocalArCustomerBalance arCustomerBalance = (LocalArCustomerBalance) arCustomerBalanceList.get(arCustomerBalanceList.size() - 1);
                if (arCustomerBalance.getCbDate().before(RCT_DT)) {
                    // create new balance
                    LocalArCustomerBalance arNewCustomerBalance = arCustomerBalanceHome.create(RCT_DT, arCustomerBalance.getCbBalance() + RCT_AMNT, companyCode);
                    arCustomer.addArCustomerBalance(arNewCustomerBalance);
                } else {
                    // equals to invoice date
                    arCustomerBalance.setCbBalance(arCustomerBalance.getCbBalance() + RCT_AMNT);
                }
            } else {
                // create new balance
                LocalArCustomerBalance arNewCustomerBalance = arCustomerBalanceHome.create(RCT_DT, RCT_AMNT, companyCode);
                arCustomer.addArCustomerBalance(arNewCustomerBalance);
            }
            // propagate to subsequent balances if necessary
            arCustomerBalances = arCustomerBalanceHome.findByAfterInvDateAndCstCustomerCode(RCT_DT, arCustomer.getCstCustomerCode(), companyCode);
            for (Object customerBalance : arCustomerBalances) {
                LocalArCustomerBalance arCustomerBalance = (LocalArCustomerBalance) customerBalance;
                arCustomerBalance.setCbBalance(arCustomerBalance.getCbBalance() + RCT_AMNT);
            }
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode) {

        /*
         * TODO:GL_COA_BLNC BUG
         *
         *
         *
         *
         */

        Debug.print("ArReceiptEntryControllerBean postToGl");

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

    private void postToGlInvestor(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalApSupplier apSupplier, boolean isCurrentAcv, byte isBeginningBalance, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("ArReceiptEntryControllerBean postToGlInvestor");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlInvestorAccountBalance glInvestorAccountBalance = glInvestorAccountBalanceHome.findByAcvCodeAndSplCode(glAccountingCalendarValue.getAcvCode(), apSupplier.getSplCode(), companyCode);

            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

            glInvestorAccountBalance.setIrabEndingBalance(EJBCommon.roundIt(glInvestorAccountBalance.getIrabEndingBalance() + JL_AMNT, FC_EXTNDD_PRCSN));

            if (!isCurrentAcv) {

                glInvestorAccountBalance.setIrabBeginningBalance(EJBCommon.roundIt(glInvestorAccountBalance.getIrabBeginningBalance() + JL_AMNT, FC_EXTNDD_PRCSN));
            }

            if (isCurrentAcv) {

                glInvestorAccountBalance.setIrabTotalCredit(EJBCommon.roundIt(glInvestorAccountBalance.getIrabTotalCredit() + JL_AMNT, FC_EXTNDD_PRCSN));
            }

            if (isBeginningBalance != 0) {
                glInvestorAccountBalance.setIrabBonus(EJBCommon.TRUE);
                glInvestorAccountBalance.setIrabInterest(EJBCommon.TRUE);
            } else {
                glInvestorAccountBalance.setIrabBonus(EJBCommon.FALSE);
                glInvestorAccountBalance.setIrabInterest(EJBCommon.FALSE);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArReceiptReportParameters(Integer companyCode) {

        Debug.print("ArReceiptEntryControllerBean getArReceiptReportParameters");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AR PRINT RECEIPT PARAMETER", companyCode);

            if (adLookUpValues.size() <= 0) {
                return list;
            }

            for (Object lookUpValue : adLookUpValues) {
                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;
                list.add(adLookUpValue.getLvName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfArUseCustomerPulldown(Integer companyCode) {

        Debug.print("ArReceiptEntryControllerBean getAdPrfArUseCustomerPulldown");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfArUseCustomerPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getDocumentTypeList(String DCMNT_TYP, Integer companyCode) {

        Debug.print("ArReceiptEntryControllerBean getDocumentTypeList");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AR RECEIPT DOCUMENT TYPE", companyCode);

            if (adLookUpValues.size() <= 0) {
                return list;
            }

            for (Object lookUpValue : adLookUpValues) {
                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;
                list.add(adLookUpValue.getLvName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private ArModReceiptDetails setArModReceiptDetails(LocalArReceipt arReceipt) {
        ArModReceiptDetails mRctDetails = new ArModReceiptDetails();
        Collection arAppliedInvoices = arReceipt.getArAppliedInvoices();
        ArrayList rctAiList = new ArrayList();
        double totalAmount = 0;

        for (Object appliedInvoice : arAppliedInvoices) {

            LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoice;

            ArModAppliedInvoiceDetails mdetails = new ArModAppliedInvoiceDetails();
            totalAmount += (arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiPenaltyApplyAmount() + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiCreditBalancePaid());

            mdetails.setAiApplyAmount(arAppliedInvoice.getAiApplyAmount());
            mdetails.setAiPenaltyApplyAmount(arAppliedInvoice.getAiPenaltyApplyAmount());
            mdetails.setAiCreditableWTax(arAppliedInvoice.getAiCreditableWTax());
            mdetails.setAiDiscountAmount(arAppliedInvoice.getAiDiscountAmount());

            mdetails.setAiAppliedDeposit(arAppliedInvoice.getAiAppliedDeposit());
            mdetails.setAiAllocatedPaymentAmount(arAppliedInvoice.getAiAllocatedPaymentAmount());
            mdetails.setAiIpsCode(arAppliedInvoice.getArInvoicePaymentSchedule().getIpsCode());
            mdetails.setAiIpsNumber(arAppliedInvoice.getArInvoicePaymentSchedule().getIpsNumber());
            mdetails.setAiIpsInvDate(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvDate());
            mdetails.setAiIpsDueDate(arAppliedInvoice.getArInvoicePaymentSchedule().getIpsDueDate());
            mdetails.setAiIpsAmountDue(arAppliedInvoice.getArInvoicePaymentSchedule().getIpsAmountDue());
            mdetails.setAiIpsPenaltyDue(arAppliedInvoice.getArInvoicePaymentSchedule().getIpsPenaltyDue());
            mdetails.setAiIpsInvNumber(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvNumber());
            mdetails.setAiIpsInvReferenceNumber(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvReferenceNumber());
            mdetails.setAiIpsInvFcName(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName());
            mdetails.setAiCreditBalancePaid(arAppliedInvoice.getAiCreditBalancePaid());
            // add apply deposit
            mdetails.setAiAppliedDeposit(arAppliedInvoice.getAiAppliedDeposit());

            mdetails.setAiRebate(arAppliedInvoice.getAiRebate());
            mdetails.setAiApplyRebate(arAppliedInvoice.getAiApplyRebate());

            rctAiList.add(mdetails);
        }


        mRctDetails.setRctCode(arReceipt.getRctCode());
        mRctDetails.setRctDocumentType(arReceipt.getRctDocumentType());
        mRctDetails.setRctDate(arReceipt.getRctDate());
        mRctDetails.setRctNumber(arReceipt.getRctNumber());
        mRctDetails.setRctReferenceNumber(arReceipt.getRctReferenceNumber());
        mRctDetails.setRctCheckNo(arReceipt.getRctCheckNo());
        mRctDetails.setRctPayfileReferenceNumber(arReceipt.getRctPayfileReferenceNumber());
        mRctDetails.setRctAmount(totalAmount);

        mRctDetails.setRctEnableAdvancePayment(arReceipt.getRctEnableAdvancePayment());
        mRctDetails.setRctExcessAmount(arReceipt.getRctExcessAmount());
        mRctDetails.setRctVoid(arReceipt.getRctVoid());
        mRctDetails.setRctDescription(arReceipt.getRctDescription());
        mRctDetails.setRctConversionDate(arReceipt.getRctConversionDate());
        mRctDetails.setRctConversionRate(arReceipt.getRctConversionRate());
        mRctDetails.setRctPaymentMethod(arReceipt.getRctPaymentMethod());
        mRctDetails.setRctApprovalStatus(arReceipt.getRctApprovalStatus());
        mRctDetails.setRctPosted(arReceipt.getRctPosted());
        mRctDetails.setRctVoid(arReceipt.getRctVoid());
        mRctDetails.setRctVoidApprovalStatus(arReceipt.getRctVoidApprovalStatus());
        mRctDetails.setRctVoidPosted(arReceipt.getRctVoidPosted());
        mRctDetails.setRctReasonForRejection(arReceipt.getRctReasonForRejection());
        mRctDetails.setRctCreatedBy(arReceipt.getRctCreatedBy());
        mRctDetails.setRctDateCreated(arReceipt.getRctDateCreated());
        mRctDetails.setRctLastModifiedBy(arReceipt.getRctLastModifiedBy());
        mRctDetails.setRctDateLastModified(arReceipt.getRctDateLastModified());
        mRctDetails.setRctApprovedRejectedBy(arReceipt.getRctApprovedRejectedBy());
        mRctDetails.setRctDateApprovedRejected(arReceipt.getRctDateApprovedRejected());
        mRctDetails.setRctPostedBy(arReceipt.getRctPostedBy());
        mRctDetails.setRctDatePosted(arReceipt.getRctDatePosted());
        mRctDetails.setRctFcName(arReceipt.getGlFunctionalCurrency().getFcName());
        mRctDetails.setRctCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode());
        mRctDetails.setRctBaName(arReceipt.getAdBankAccount().getBaName());
        mRctDetails.setRctRbName(arReceipt.getArReceiptBatch() != null ? arReceipt.getArReceiptBatch().getRbName() : null);
        mRctDetails.setRctCstName(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());

        // HRIS
        mRctDetails.setReportParameter(arReceipt.getReportParameter());
        mRctDetails.setRctAiList(rctAiList);
        return mRctDetails;
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArReceiptEntryControllerBean ejbCreate");
    }
}