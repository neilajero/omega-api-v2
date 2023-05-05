package com.ejb.txnapi.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import jakarta.ejb.Stateless;

import com.ejb.ConfigurationClass;
import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.cm.*;
import com.ejb.dao.gl.*;
import com.ejb.entities.gl.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.*;
import com.ejb.entities.cm.*;
import com.ejb.entities.ar.*;
import com.ejb.exception.ad.AdPRFCoaGlCustomerDepositAccountNotFoundException;
import com.ejb.exception.ar.*;
import com.ejb.exception.gl.*;
import com.ejb.exception.global.*;
import com.ejb.restfulapi.ar.models.ReceiptApiResponse;
import com.ejb.restfulapi.ar.models.ReceiptDetails;
import com.ejb.restfulapi.ar.models.ReceiptRequest;
import com.util.mod.ar.ArModAppliedInvoiceDetails;
import com.util.mod.ar.ArModInvoicePaymentScheduleDetails;
import com.util.ar.ArReceiptDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;

@Stateless(name = "ArReceiptEntryApiControllerEJB")
public class ArReceiptEntryApiControllerBean extends EJBContextClass implements ArReceiptEntryApiController {

    @EJB
    private PersistenceBeanClass em;
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
    private LocalAdUserHome adUserHome;
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
    private LocalAdBranchCustomerHome adBranchCustomerHome;
    @EJB
    private LocalAdBranchBankAccountHome adBranchBankAccountHome;
    @EJB
    private LocalArAppliedInvoiceHome arAppliedInvoiceHome;
    @EJB
    private LocalCmAdjustmentHome cmAdjustmentHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalArAppliedCreditHome arAppliedCreditHome;
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
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private LocalGlForexLedgerHome glForexLedgerHome;
    @EJB
    private LocalArCustomerBalanceHome arCustomerBalanceHome;

    @Override
    public Integer saveReceipt(ArReceiptDetails details, String bankAccount,
                               String foreignCurrency, String customerCode, String receiptBatch, ArrayList receiptLines,
                               Integer branchCode, Integer companyCode)
            throws ArReceiptEntryValidationException, GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException,
            GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException,
            GlobalTransactionAlreadyPendingException, ArINVOverCreditBalancePaidapplicationNotAllowedException,
            GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException,
            ArINVOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException,
            GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException,
            ArRCTInvoiceHasNoWTaxCodeException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException,
            GlobalBranchAccountNumberInvalidException, GlobalRecordAlreadyAssignedException,
            AdPRFCoaGlCustomerDepositAccountNotFoundException, ArREDuplicatePayfileReferenceNumberException {
        Debug.print("ArReceiptEntryApiControllerBean saveReceipt");

        LocalArReceipt arReceipt = null;

        try {

            // validate if receipt is already deleted
            try {
                if (details.getRctCode() != null) {
                    arReceipt = arReceiptHome.findByPrimaryKey(details.getRctCode(), details.getCompanyShortName());
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

            // validate if document number is unique document number is automatic then set
            // next sequence
            /*
             * receipt number will assign as default sequence if receipt number is default,
             * hence, if its have value, check if has a duplicate exist and throw exception
             * if true*
             */
            this.receiptNumberValidation(details, branchCode, companyCode, arReceipt);

            // validate if conversion date exists
            /*
             * This will check the conversion date if exist and currency is selected the
             * non-default
             */
            this.checkConversionDateValidation(details, foreignCurrency, companyCode);

            /* this still not used */
            // get remaining credit balance of customer and check if exceeds credit balance paid
            double creditBalanceRemaining = this.getArRctCreditBalanceByCstCustomerCode(customerCode, branchCode, companyCode, details.getCompanyShortName());

            // create receipt
            arReceipt = arReceiptHome
                    .RctType("COLLECTION")
                    .RctDescription(details.getRctDescription())
                    .RctDate(details.getRctDate())
                    .RctNumber(details.getRctNumber())
                    .RctReferenceNumber(details.getRctReferenceNumber())
                    .RctCheckNo(details.getRctCheckNo())
                    .RctPayfileReferenceNumber(details.getRctPayfileReferenceNumber())
                    .RctAmount(details.getRctAmount())
                    .RctAmountCash(details.getRctAmount())
                    .RctConversionDate(details.getRctConversionDate())
                    .RctConversionRate(details.getRctConversionRate())
                    .RctPaymentMethod(details.getRctPaymentMethod())
                    .RctCreatedBy(details.getRctCreatedBy())
                    .RctDateCreated(details.getRctDateCreated())
                    .RctAdBranch(branchCode)
                    .RctAdCompany(companyCode)
                    .buildReceipt(details.getCompanyShortName());

            arReceipt.setRctEnableAdvancePayment(details.getRctEnableAdvancePayment());
            arReceipt.setRctExcessAmount(details.getRctExcessAmount());

            arReceipt.setRctDocumentType(details.getRctDocumentType());
            arReceipt.setReportParameter(details.getReportParameter());

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(bankAccount, companyCode, details.getCompanyShortName());
            arReceipt.setAdBankAccount(adBankAccount);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(foreignCurrency, companyCode, details.getCompanyShortName());
            arReceipt.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(customerCode, companyCode, details.getCompanyShortName());
            arReceipt.setArCustomer(arCustomer);

            if (details.getRctCustomerName().length() > 0
                    && !arCustomer.getCstName().equals(details.getRctCustomerName())) {
                arReceipt.setRctCustomerName(details.getRctCustomerName());
            }

            try {
                LocalArReceiptBatch arReceiptBatch = arReceiptBatchHome.findByRbName(receiptBatch, branchCode, companyCode, details.getCompanyShortName());
                arReceipt.setArReceiptBatch(arReceiptBatch);
            } catch (FinderException ex) {
            }

            this.generateArDistributionRecords(receiptLines, branchCode, companyCode, arReceipt, details.getCompanyShortName());
            this.executeArRctPost(arReceipt, arReceipt.getRctLastModifiedBy(), branchCode, companyCode, details.getCompanyShortName());

            // set receipt approval status
            arReceipt.setRctApprovalStatus("N/A");
            return arReceipt.getRctCode();

        } catch (GlobalRecordAlreadyDeletedException | AdPRFCoaGlCustomerDepositAccountNotFoundException |
                GlobalBranchAccountNumberInvalidException | GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
                GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyLockedException | ArRCTInvoiceHasNoWTaxCodeException |
                ArINVOverCreditBalancePaidapplicationNotAllowedException | ArINVOverapplicationNotAllowedException | GlobalTransactionAlreadyVoidException |
                GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
                GlobalConversionDateNotExistException | GlobalDocumentNumberNotUniqueException | ArREDuplicatePayfileReferenceNumberException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void generateArDistributionRecords(ArrayList aiList, Integer branchCode, Integer companyCode, LocalArReceipt arReceipt, String companyShortName)
            throws RemoveException, FinderException, ArINVOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, ArRCTInvoiceHasNoWTaxCodeException,
            ArINVOverCreditBalancePaidapplicationNotAllowedException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlCustomerDepositAccountNotFoundException {
        Debug.print("ArReceiptEntryApiControllerBean generateArDistributionRecords");

        double totalApplyAmount = 0;
        double RCT_FRX_GN_LSS = 0d;
        double INVC_CONVERSION_RT = 0d;
        double RCT_CONVERSION_RT = 0d;
        boolean defaultBaCoa = false;
        boolean defaultCstCoa = false;

        LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode, companyShortName);
        LocalArAppliedInvoice arAppliedInvoice = null;
        LocalAdBranchBankAccount adBranchBankAccount = null;
        LocalAdBankAccount adBankAccount = arReceipt.getAdBankAccount();
        LocalArCustomer arCustomer = arReceipt.getArCustomer();
        LocalAdBranchCustomer adBranchCustomer = null;

        try {
            // Find Branch Bank Account if Exist.
            adBranchBankAccount = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(adBankAccount.getBaCode(), branchCode, companyCode, companyShortName);
        } catch (FinderException ex) {
            defaultBaCoa = true;
        }

        try {
            adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arCustomer.getCstCode(), branchCode,
                    companyCode, companyShortName);
        } catch (FinderException ex) {
            defaultCstCoa = true;
        }

        // add applied invoices and distribution record
        Iterator i = aiList.iterator();
        while (i.hasNext()) {

            ArModAppliedInvoiceDetails mAiDetails = (ArModAppliedInvoiceDetails) i.next();
            totalApplyAmount += mAiDetails.getAiApplyAmount() + mAiDetails.getAiPenaltyApplyAmount();

            // Create Applied Invoice
            arAppliedInvoice = this.addArAiEntry(mAiDetails, arReceipt, companyCode, companyShortName);
            arAppliedInvoice.setArReceipt(arReceipt);
            //arReceipt.addArAppliedInvoice(arAppliedInvoice);

            LocalGlFunctionalCurrency invcFC = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency();

            INVC_CONVERSION_RT = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate();
            RCT_CONVERSION_RT = arReceipt.getRctConversionRate();

            RCT_FRX_GN_LSS += arAppliedInvoice.getAiForexGainLoss();

            // create cred. withholding tax distribution record if necessary
            if (mAiDetails.getAiCreditableWTax() > 0) {
                Integer WTC_COA_CODE = null;

                double CREDITABLE_WTAX = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(),
                        arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(),
                        arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(),
                        arAppliedInvoice.getAiCreditableWTax(), companyCode, companyShortName);

                if (arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArWithholdingTaxCode().getGlChartOfAccount() != null) {
                    WTC_COA_CODE = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArWithholdingTaxCode().getGlChartOfAccount().getCoaCode();
                } else {
                    adPreference = adPreferenceHome.findByPrfAdCompany(companyCode, companyShortName);
                    WTC_COA_CODE = adPreference.getArWithholdingTaxCode().getGlChartOfAccount().getCoaCode();
                }
                //Overwrite the branchCode to retrieve the correct COA
                branchCode = 1; // HEAD OFFICE
                this.addArDrEntry(arReceipt.getArDrNextLine(), "W-TAX", EJBCommon.FALSE, CREDITABLE_WTAX,
                        EJBCommon.FALSE, WTC_COA_CODE, arReceipt, arAppliedInvoice, branchCode, companyCode, companyShortName);
            }

            // create discount distribution records if necessary
            if (arAppliedInvoice.getAiDiscountAmount() != 0) {
                double DISCOUNT_AMOUNT = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(),
                        arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(),
                        arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(),
                        arAppliedInvoice.getAiDiscountAmount(), companyCode, companyShortName);
                Integer discountCoaAccount = defaultBaCoa ? adBankAccount.getBaCoaGlSalesDiscount()
                        : adBranchBankAccount.getBbaGlCoaSalesDiscountAccount();
                this.addArDrEntry(arReceipt.getArDrNextLine(), "SALES DISCOUNT", EJBCommon.TRUE, DISCOUNT_AMOUNT,
                        EJBCommon.FALSE, discountCoaAccount, arReceipt, arAppliedInvoice, branchCode, companyCode, companyShortName);
            }

            // create applied deposit distribution records if necessary
            if (arAppliedInvoice.getAiCreditBalancePaid() != 0) {
                // add branch bank account
                double CREDIT_BALANCE_PAID = this.convertForeignToFunctionalCurrency(
                        arReceipt.getGlFunctionalCurrency().getFcCode(),
                        arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(),
                        arReceipt.getRctConversionRate(), arAppliedInvoice.getAiCreditBalancePaid(), companyCode, companyShortName);
                Integer advanceCoaAccount = defaultBaCoa ? adBankAccount.getBaCoaGlAdvanceAccount() : adBranchBankAccount.getBbaGlCoaAdvanceAccount();
                this.addArDrEntry(arReceipt.getArDrNextLine(), "CREDITED BALANCE", EJBCommon.TRUE, CREDIT_BALANCE_PAID,
                        EJBCommon.FALSE, advanceCoaAccount, arReceipt, arAppliedInvoice, branchCode, companyCode, companyShortName);
            }

            // create applied deposit distribution records if necessary
            if (arAppliedInvoice.getAiAppliedDeposit() != 0) {
                double APPLIED_DEPOSIT = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(),
                        arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(),
                        arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(),
                        arAppliedInvoice.getAiAppliedDeposit(), companyCode, companyShortName);
                adPreference = adPreferenceHome.findByPrfAdCompany(companyCode, companyShortName);
                if (adPreference.getPrfArGlCoaCustomerDepositAccount() == null) {
                    throw new AdPRFCoaGlCustomerDepositAccountNotFoundException();
                }
                this.addArDrEntry(arReceipt.getArDrNextLine(), "APPLIED DEPOSIT", EJBCommon.TRUE, APPLIED_DEPOSIT,
                        EJBCommon.FALSE, adPreference.getPrfArGlCoaCustomerDepositAccount(), arReceipt,
                        arAppliedInvoice, branchCode, companyCode, companyShortName);
            }

            // Get Service Charge Records
            double applyAmount = serviceChargeDR(branchCode, companyCode, arReceipt, arAppliedInvoice, invcFC, companyShortName);

            // create rebate distribution records if necessary
            if (arAppliedInvoice.getAiRebate() != 0) {
                rebateDR(branchCode, companyCode, arReceipt, arAppliedInvoice, invcFC, companyShortName);
            } else {
                // earned interest
                earnedInterest(branchCode, companyCode, arReceipt, defaultBaCoa, defaultCstCoa, arAppliedInvoice,
                        adBranchBankAccount, adBankAccount, arCustomer, adBranchCustomer, invcFC, applyAmount, companyShortName);
            }

            // earned penalty
            if (arAppliedInvoice.getAiPenaltyApplyAmount() != 0) {
                earnedPenalty(branchCode, companyCode, arReceipt, defaultCstCoa,
                        arAppliedInvoice, arCustomer, adBranchCustomer, invcFC, companyShortName);
            }

            // get receivable account TODO: ALWAYS GETTING ERROR BELOW
            receivables(branchCode, companyCode, arReceipt, arAppliedInvoice, invcFC, companyShortName);

            // reverse deferred tax if necessary
            LocalArTaxCode arTaxCode = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArTaxCode();
            if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT") && arTaxCode.getTcInterimAccount() != null) {
                deferredTax(branchCode, companyCode, arReceipt, arAppliedInvoice, invcFC, companyShortName);
            }
        }

        if (RCT_CONVERSION_RT > INVC_CONVERSION_RT) {
            this.addArDrEntry(arReceipt.getArDrNextLine(), "FOREX GAIN", EJBCommon.TRUE, Math.abs(RCT_FRX_GN_LSS),
                    EJBCommon.FALSE, adPreference.getPrfMiscPosGiftCertificateAccount(), arReceipt, null, branchCode,
                    companyCode, companyShortName);
        }

        if (INVC_CONVERSION_RT > RCT_CONVERSION_RT) {
            this.addArDrEntry(arReceipt.getArDrNextLine(), "FOREX LOSS", EJBCommon.FALSE, Math.abs(RCT_FRX_GN_LSS),
                    EJBCommon.FALSE, adPreference.getPrfMiscPosGiftCertificateAccount(), arReceipt, null, branchCode,
                    companyCode, companyShortName);
        }

        // create cash distribution record
        if (totalApplyAmount != 0) {
            totalApplyAmount = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(),
                    arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(),
                    arReceipt.getRctConversionRate(), totalApplyAmount, companyCode, companyShortName);
            Integer coaCashAccount = defaultBaCoa ? adBankAccount.getBaIsCashAccount()
                    : adBranchBankAccount.getBbaGlCoaCashAccount();

            this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE, totalApplyAmount, EJBCommon.FALSE,
                    coaCashAccount, arReceipt, arAppliedInvoice, branchCode, companyCode, companyShortName);
        }
    }

    private void receivables(Integer branchCode, Integer companyCode, LocalArReceipt arReceipt,
                             LocalArAppliedInvoice arAppliedInvoice, LocalGlFunctionalCurrency invcFC, String companyShortName) {
        Debug.print("ArReceiptEntryApiControllerBean receivables");

        try {
            LocalArDistributionRecord arDistributionRecord = null;
            if(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvNumber().contains("ARBEGBAL")) {
                arDistributionRecord = arDistributionRecordHome.findByDrClassDebitAndInvCode(
                        "RECEIVABLE", arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(),
                        companyCode, companyShortName);
            } else {
                arDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode(
                        "RECEIVABLE", arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(),
                        companyCode, companyShortName);
            }

            // Get net of creditable withholding tax from the apply amount
            double netOfCreditableWTax = arAppliedInvoice.getAiApplyAmount() - arAppliedInvoice.getAiCreditableWTax();
            arAppliedInvoice.setAiApplyAmount(netOfCreditableWTax);

            double amount_crd = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(),
                    arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(),
                    arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(),
                    arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditBalancePaid()
                            + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiAppliedDeposit()
                            + arAppliedInvoice.getAiDiscountAmount(),
                    companyCode, companyShortName);

            this.addArDrEntry(arReceipt.getArDrNextLine(), "RECEIVABLE", EJBCommon.FALSE, amount_crd,
                    EJBCommon.FALSE, arDistributionRecord.getGlChartOfAccount().getCoaCode(), arReceipt,
                    arAppliedInvoice, branchCode, companyCode, companyShortName);

        } catch (Exception ex) {

        }
    }

    private void deferredTax(Integer branchCode, Integer companyCode, LocalArReceipt arReceipt,
                             LocalArAppliedInvoice arAppliedInvoice, LocalGlFunctionalCurrency invcFC, String companyShortName)
            throws GlobalBranchAccountNumberInvalidException {
        Debug.print("ArReceiptEntryApiControllerBean deferredTax");

        try {

            LocalArDistributionRecord arDeferredDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode("DEFERRED TAX",
                    arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(), companyCode, companyShortName);

            double DR_AMNT = EJBCommon.roundIt((arDeferredDistributionRecord.getDrAmount()
                            / arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvAmountDue())
                            * (arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax()
                            + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiAppliedDeposit()),
                    this.getGlFcPrecisionUnit(companyCode, companyShortName));

            DR_AMNT = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(),
                    arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(),
                    arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(),
                    DR_AMNT, companyCode, companyShortName);

            this.addArDrEntry(arReceipt.getArDrNextLine(), "DEFERRED TAX", EJBCommon.TRUE, DR_AMNT,
                    EJBCommon.FALSE, arDeferredDistributionRecord.getGlChartOfAccount().getCoaCode(), arReceipt,
                    arAppliedInvoice, branchCode, companyCode, companyShortName);

            this.addArDrEntry(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, DR_AMNT, EJBCommon.FALSE,
                    arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArTaxCode()
                            .getGlChartOfAccount().getCoaCode(),
                    arReceipt, arAppliedInvoice, branchCode, companyCode, companyShortName);

        } catch (FinderException ex) {

        }
    }

    private void earnedPenalty(Integer branchCode, Integer companyCode, LocalArReceipt arReceipt,
                               boolean defaultCstCoa, LocalArAppliedInvoice arAppliedInvoice, LocalArCustomer arCustomer,
                               LocalAdBranchCustomer adBranchCustomer, LocalGlFunctionalCurrency invcFC, String companyShortName)
            throws GlobalBranchAccountNumberInvalidException {
        Debug.print("ArReceiptEntryApiControllerBean earnedPenalty");

        double applyPenaltyDue = arAppliedInvoice.getAiPenaltyApplyAmount();

        applyPenaltyDue = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(),
                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(),
                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(),
                applyPenaltyDue, companyCode, companyShortName);

        Integer cstUnearnedPenaltyCoa = defaultCstCoa ? arCustomer.getCstGlCoaUnEarnedPenaltyAccount()
                : adBranchCustomer.getBcstGlCoaUnEarnedPenaltyAccount();

        this.addArDrEntry(arReceipt.getArDrNextLine(), "UNPENALTY", EJBCommon.TRUE, applyPenaltyDue,
                EJBCommon.FALSE, cstUnearnedPenaltyCoa, arReceipt, arAppliedInvoice, branchCode, companyCode, companyShortName);

        Integer cstEarnedPenaltyCoa = defaultCstCoa ? arCustomer.getCstGlCoaEarnedPenaltyAccount()
                : adBranchCustomer.getBcstGlCoaEarnedPenaltyAccount();

        this.addArDrEntry(arReceipt.getArDrNextLine(), "EARNED PENALTY", EJBCommon.FALSE, applyPenaltyDue,
                EJBCommon.FALSE, cstUnearnedPenaltyCoa, arReceipt, arAppliedInvoice, branchCode, companyCode, companyShortName);

        Integer cstReceivableCoa = defaultCstCoa ? arCustomer.getCstGlCoaReceivableAccount()
                : adBranchCustomer.getBcstGlCoaReceivableAccount();

        this.addArDrEntry(arReceipt.getArDrNextLine(), "RECEIVABLE PENALTY", EJBCommon.FALSE,
                arAppliedInvoice.getAiPenaltyApplyAmount(), EJBCommon.FALSE, cstReceivableCoa, arReceipt,
                arAppliedInvoice, branchCode, companyCode, companyShortName);
    }

    private void earnedInterest(Integer branchCode, Integer companyCode, LocalArReceipt arReceipt,
                                boolean defaultBaCoa, boolean defaultCstCoa, LocalArAppliedInvoice arAppliedInvoice,
                                LocalAdBranchBankAccount adBranchBankAccount, LocalAdBankAccount adBankAccount,
                                LocalArCustomer arCustomer, LocalAdBranchCustomer adBranchCustomer, LocalGlFunctionalCurrency invcFC, double applyAmount,
                                String companyShortName)
            throws GlobalBranchAccountNumberInvalidException {
        Debug.print("ArReceiptEntryApiControllerBean earnedInterest");

        try {

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode(
                    "RECEIVABLE INTEREST",
                    arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(), companyCode, companyShortName);

            double dueInterest = arAppliedInvoice.getArInvoicePaymentSchedule().getIpsInterestDue();
            double dueAmountMonthly = arAppliedInvoice.getArInvoicePaymentSchedule().getIpsAmountDue();

            dueInterest = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(),
                    arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(),
                    arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(),
                    dueInterest, companyCode, companyShortName);

            dueAmountMonthly = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(),
                    arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(),
                    arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(),
                    dueAmountMonthly, companyCode, companyShortName);

            Integer cstUnearnedInterestCoa = defaultCstCoa ? arCustomer.getCstGlCoaUnEarnedInterestAccount()
                    : adBranchCustomer.getBcstGlCoaUnEarnedInterestAccount();

            this.addArDrEntry(arReceipt.getArDrNextLine(), "UNINTEREST", EJBCommon.TRUE,
                    (applyAmount / dueAmountMonthly) * dueInterest, EJBCommon.FALSE, cstUnearnedInterestCoa,
                    arReceipt, arAppliedInvoice, branchCode, companyCode, companyShortName);

            Integer cstEarnedInterestCoa = defaultCstCoa ? arCustomer.getCstGlCoaEarnedInterestAccount()
                    : adBranchCustomer.getBcstGlCoaEarnedInterestAccount();

            this.addArDrEntry(arReceipt.getArDrNextLine(), "EARNED INTEREST", EJBCommon.FALSE,
                    (applyAmount / dueAmountMonthly) * dueInterest, EJBCommon.FALSE, cstEarnedInterestCoa,
                    arReceipt, arAppliedInvoice, branchCode, companyCode, companyShortName);

            this.addArDrEntry(arReceipt.getArDrNextLine(), "RECEIVABLE INTEREST", EJBCommon.FALSE,
                    (applyAmount / dueAmountMonthly) * dueInterest, EJBCommon.FALSE,
                    arDistributionRecord.getGlChartOfAccount().getCoaCode(), arReceipt, arAppliedInvoice,
                    branchCode, companyCode, companyShortName);

            Integer coaCashAccount = defaultBaCoa ? adBankAccount.getBaIsCashAccount()
                    : adBranchBankAccount.getBbaGlCoaCashAccount();

            this.addArDrEntry(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE,
                    (applyAmount / dueAmountMonthly) * dueInterest, EJBCommon.FALSE, coaCashAccount, arReceipt,
                    arAppliedInvoice, branchCode, companyCode, companyShortName);

        } catch (FinderException ex) {

        }
    }

    private void rebateDR(Integer branchCode, Integer companyCode, LocalArReceipt arReceipt,
                          LocalArAppliedInvoice arAppliedInvoice, LocalGlFunctionalCurrency invcFC, String companyShortName)
            throws FinderException, GlobalBranchAccountNumberInvalidException {
        Debug.print("ArReceiptEntryApiControllerBean rebateDR");

        LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode(
                "UNINTEREST", arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(),
                companyCode, companyShortName);

        LocalArDistributionRecord arDistributionRecordReceivable = arDistributionRecordHome
                .findByDrClassAndInvCode("RECEIVABLE INTEREST",
                        arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(), companyCode, companyShortName);

        double REBATE_AMOUNT = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(),
                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(),
                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(),
                arAppliedInvoice.getAiRebate(), companyCode, companyShortName);

        this.addArDrEntry(arReceipt.getArDrNextLine(), "REBATE", EJBCommon.TRUE, REBATE_AMOUNT, EJBCommon.FALSE,
                arDistributionRecord.getGlChartOfAccount().getCoaCode(), arReceipt, arAppliedInvoice, branchCode,
                companyCode, companyShortName);

        this.addArDrEntry(arReceipt.getArDrNextLine(), "RECEIVABLE REBATE", EJBCommon.FALSE, REBATE_AMOUNT,
                EJBCommon.FALSE, arDistributionRecordReceivable.getGlChartOfAccount().getCoaCode(), arReceipt,
                arAppliedInvoice, branchCode, companyCode, companyShortName);
    }

    private double serviceChargeDR(Integer branchCode, Integer companyCode, LocalArReceipt arReceipt,
                                   LocalArAppliedInvoice arAppliedInvoice, LocalGlFunctionalCurrency invcFC, String companyShortName)
            throws FinderException, GlobalBranchAccountNumberInvalidException {
        Debug.print("ArReceiptEntryApiControllerBean serviceChargeDR");

        Collection arScDistributionRecords = arDistributionRecordHome.findDrsByDrClassAndInvCode("SC",
                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(), companyCode, companyShortName);
        Iterator scIter = arScDistributionRecords.iterator();

        double applyAmount = arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditBalancePaid()
                + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount()
                + arAppliedInvoice.getAiAppliedDeposit();

        double dueAmount = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvAmountDue();

        applyAmount = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(),
                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(),
                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), applyAmount,
                companyCode, companyShortName);

        dueAmount = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(),
                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(),
                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(), dueAmount,
                companyCode, companyShortName);

        double scAmount = 0;
        while (scIter.hasNext()) {
            LocalArDistributionRecord arScDistributionRecord = (LocalArDistributionRecord) scIter.next();
            double scDrAmount = this.convertForeignToFunctionalCurrency(invcFC.getFcCode(), invcFC.getFcName(),
                    arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(),
                    arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(),
                    arScDistributionRecord.getDrAmount(), companyCode, companyShortName);

            scAmount += scDrAmount * (applyAmount / dueAmount);

            this.addArDrEntry(arReceipt.getArDrNextLine(), "RECEIVABLE", EJBCommon.FALSE, scAmount, EJBCommon.FALSE,
                    arScDistributionRecord.getDrScAccount(), arReceipt, arAppliedInvoice, branchCode, companyCode, companyShortName);

            this.addArDrEntry(arReceipt.getArDrNextLine(), "SC", EJBCommon.TRUE, scAmount, EJBCommon.FALSE,
                    arScDistributionRecord.getGlChartOfAccount().getCoaCode(), arReceipt, arAppliedInvoice,
                    branchCode, companyCode, companyShortName);
        }
        return applyAmount;
    }

    private void addArDrEntry(short drLine, String drClass, byte drDebit, double drAmount, byte drReversed, Integer COA_CODE, LocalArReceipt arReceipt,
                              LocalArAppliedInvoice arAppliedInvoice, Integer branchCode, Integer companyCode, String companyShortName)
            throws GlobalBranchAccountNumberInvalidException {
        Debug.print("ArReceiptEntryApiControllerBean addArDrEntry");

        try {
            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode, companyShortName);

            // create distribution record
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome
                    .DrLine(drLine)
                    .DrClass(drClass)
                    .DrDebit(drDebit)
                    .DrAmount(EJBCommon.roundIt(drAmount, adCompany.getGlFunctionalCurrency().getFcPrecision()))
                    .DrReversed(drReversed)
                    .DrAdCompany(companyCode)
                    .buildDistributionRecords(companyShortName);

            arReceipt.addArDistributionRecord(arDistributionRecord);
            glChartOfAccount.addArDistributionRecord(arDistributionRecord);

            // to be used by gl journal interface for cross currency receipts
            if (arAppliedInvoice != null) {
                arAppliedInvoice.addArDistributionRecord(arDistributionRecord);
            }
        } catch (FinderException ex) {
            throw new GlobalBranchAccountNumberInvalidException(drClass);
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeArRctPost(LocalArReceipt arReceipt, String USR_NM, Integer branchCode, Integer companyCode, String companyShortName)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {
        Debug.print("ArReceiptEntryApiControllerBean executeArRctPost");

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
                    Collection arAppliedInvoices = arAppliedInvoiceHome.findByRctCode(arReceipt.getRctCode(), companyCode, companyShortName);
                    for (Object appliedInvoice : arAppliedInvoices) {
                        LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoice;
                        LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arAppliedInvoice.getArInvoicePaymentSchedule();

                        // Get the net of tax amount applied from API request
                        double netOfCreditableWTax = arAppliedInvoice.getAiApplyAmount() - arAppliedInvoice.getAiCreditableWTax();
                        arAppliedInvoice.setAiApplyAmount(netOfCreditableWTax);

                        double AMOUNT_PAID = arAppliedInvoice.getAiApplyAmount()
                                + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount()
                                + arAppliedInvoice.getAiAppliedDeposit() + arAppliedInvoice.getAiCreditBalancePaid()
                                + arAppliedInvoice.getAiRebate();

                        double PENALTY_PAID = arAppliedInvoice.getAiPenaltyApplyAmount();

                        RCT_CRDTS += this.convertForeignToFunctionalCurrency(
                                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcCode(),
                                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName(),
                                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(),
                                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(),
                                arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiCreditableWTax(),
                                companyCode, companyShortName);

                        RCT_AI_APPLD_DPSTS += this.convertForeignToFunctionalCurrency(
                                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcCode(),
                                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName(),
                                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(),
                                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(),
                                arAppliedInvoice.getAiAppliedDeposit(), companyCode, companyShortName);

                        arInvoicePaymentSchedule.setIpsAmountPaid(
                                EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountPaid() + AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode, companyShortName)));
                        arInvoicePaymentSchedule.setIpsPenaltyPaid(
                                EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsPenaltyPaid() + PENALTY_PAID, this.getGlFcPrecisionUnit(companyCode, companyShortName)));

                        arInvoicePaymentSchedule.getArInvoice().setInvAmountPaid(EJBCommon.roundIt(
                                arInvoicePaymentSchedule.getArInvoice().getInvAmountPaid() + AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode, companyShortName)));

                        arInvoicePaymentSchedule.getArInvoice().setInvPenaltyPaid(EJBCommon.roundIt(
                                arInvoicePaymentSchedule.getArInvoice().getInvPenaltyPaid() + PENALTY_PAID, this.getGlFcPrecisionUnit(companyCode, companyShortName)));

                        // release invoice lock
                        arInvoicePaymentSchedule.setIpsLock(EJBCommon.FALSE);
                    }

                    // decrease customer balance
                    double RCT_AMNT = this.convertForeignToFunctionalCurrency(
                            arReceipt.getGlFunctionalCurrency().getFcCode(),
                            arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(),
                            arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), companyCode, companyShortName);

                    this.updateCustomerBalance(arReceipt.getRctDate(), (RCT_AMNT + RCT_CRDTS + RCT_AI_APPLD_DPSTS) * -1,
                            arReceipt.getArCustomer(), companyCode, companyShortName);
                }

                // increase bank balance
                LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode(), companyShortName);

                try {

                    // find bank account balance before or equal receipt date
                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(
                            arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode, companyShortName);

                    if (!adBankAccountBalances.isEmpty()) {
                        // get last check
                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);
                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {
                            // create new balance
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome
                                    .BabDate(arReceipt.getRctDate())
                                    .BabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmount())
                                    .BabType("BOOK")
                                    .BabAdCompany(companyCode)
                                    .buildBankAccountBalance(companyShortName);
                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

                        } else {
                            // equals to check date
                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmount());
                        }
                    } else {
                        // create new balance
                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome
                                .BabDate(arReceipt.getRctDate())
                                .BabBalance(arReceipt.getRctAmount())
                                .BabType("BOOK")
                                .BabAdCompany(companyCode)
                                .buildBankAccountBalance(companyShortName);
                        adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                    }

                    // propagate to subsequent balances if necessary
                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(
                            arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode, companyShortName);

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

                        double AMOUNT_PAID = arAppliedInvoice.getAiApplyAmount()
                                + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount()
                                + arAppliedInvoice.getAiAppliedDeposit() + arAppliedInvoice.getAiCreditBalancePaid()
                                + arAppliedInvoice.getAiRebate();

                        double PENALTY_PAID = arAppliedInvoice.getAiPenaltyApplyAmount();

                        RCT_CRDTS += this.convertForeignToFunctionalCurrency(
                                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcCode(),
                                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName(),
                                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(),
                                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(),
                                arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiCreditableWTax(),
                                companyCode, companyShortName);

                        RCT_AI_APPLD_DPSTS += this.convertForeignToFunctionalCurrency(
                                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcCode(),
                                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getGlFunctionalCurrency().getFcName(),
                                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionDate(),
                                arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvConversionRate(),
                                arAppliedInvoice.getAiAppliedDeposit(), companyCode, companyShortName);

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

                        arInvoicePaymentSchedule.setIpsAmountPaid(
                                EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountPaid() - AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode, companyShortName)));
                        arInvoicePaymentSchedule.setIpsPenaltyPaid(
                                EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsPenaltyPaid() - PENALTY_PAID, this.getGlFcPrecisionUnit(companyCode, companyShortName)));

                        arInvoicePaymentSchedule.getArInvoice()
                                .setInvAmountPaid(EJBCommon.roundIt(
                                        arInvoicePaymentSchedule.getArInvoice().getInvAmountPaid() - AMOUNT_PAID, this.getGlFcPrecisionUnit(companyCode, companyShortName)));
                        arInvoicePaymentSchedule.getArInvoice()
                                .setInvPenaltyPaid(EJBCommon.roundIt(
                                        arInvoicePaymentSchedule.getArInvoice().getInvPenaltyPaid() - PENALTY_PAID, this.getGlFcPrecisionUnit(companyCode, companyShortName)));
                    }

                    // increase customer balance
                    double RCT_AMNT = this.convertForeignToFunctionalCurrency(
                            arReceipt.getGlFunctionalCurrency().getFcCode(),
                            arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(),
                            arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), companyCode, companyShortName);

                    this.updateCustomerBalance(arReceipt.getRctDate(), RCT_AMNT + RCT_CRDTS + RCT_AI_APPLD_DPSTS,
                            arReceipt.getArCustomer(), companyCode, companyShortName);
                }

                // decrease bank balance
                LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode(), companyShortName);
                try {
                    // find bankaccount balance before or equal receipt date
                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(
                            arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode, companyShortName);

                    if (!adBankAccountBalances.isEmpty()) {
                        // get last check
                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);
                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {
                            // create new balance
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(
                                    arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmount(), "BOOK", companyCode);
                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                        } else {
                            // equals to check date
                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmount());
                        }
                    } else {
                        // create new balance
                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome
                                .BabDate(arReceipt.getRctDate())
                                .BabBalance(0 - arReceipt.getRctAmount())
                                .BabType("BOOK")
                                .BabAdCompany(companyCode)
                                .buildBankAccountBalance(companyShortName);
                        adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                    }

                    // propagate to subsequent balances if necessary
                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(
                            arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode, companyShortName);

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
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode, companyShortName);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);

            if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed
                LocalGlSetOfBook glJournalSetOfBook = null;

                try {
                    glJournalSetOfBook = glSetOfBookHome.findByDate(arReceipt.getRctDate(), companyCode, companyShortName);
                } catch (FinderException ex) {
                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(
                        glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), arReceipt.getRctDate(), companyCode, companyShortName);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' ||
                        glAccountingCalendarValue.getAcvStatus() == 'C' ||
                        glAccountingCalendarValue.getAcvStatus() == 'P') {
                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting
                LocalGlJournalLine glOffsetJournalLine = null;
                Collection arDistributionRecords = null;
                if (arReceipt.getRctVoid() == EJBCommon.FALSE) {
                    arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(
                            EJBCommon.FALSE, arReceipt.getRctCode(), companyCode, companyShortName);
                } else {
                    arDistributionRecords = arDistributionRecordHome.findImportableDrByDrReversedAndRctCode(
                            EJBCommon.TRUE, arReceipt.getRctCode(), companyCode, companyShortName);
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
                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName(
                                "ACCOUNTS RECEIVABLES", "SALES RECEIPTS", companyCode, companyShortName);
                    } catch (FinderException ex) {
                        throw new GlobalJournalNotBalanceException();
                    }
                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {
                        glOffsetJournalLine = glJournalLineHome
                                .JlLineNumber((short)(arDistributionRecords.size() + 1))
                                .JlAmount(TOTAL_CREDIT - TOTAL_DEBIT)
                                .JlAdCompany(companyCode)
                                .buildJournalLine(companyShortName);
                    } else {
                        glOffsetJournalLine = glJournalLineHome
                                .JlLineNumber((short)(arDistributionRecords.size() + 1))
                                .JlDebit(EJBCommon.FALSE)
                                .JlAmount(TOTAL_DEBIT - TOTAL_CREDIT)
                                .JlAdCompany(companyCode)
                                .buildJournalLine(companyShortName);
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
                        glJournalBatch = glJournalBatchHome.findByJbName(
                                "JOURNAL IMPORT " + formatter.format(new Date()) + " " + arReceipt.getArReceiptBatch().getRbName(),
                                branchCode, companyCode, companyShortName);
                    } else {
                        glJournalBatch = glJournalBatchHome.findByJbName(
                                "JOURNAL IMPORT " + formatter.format(new Date()) + " SALES RECEIPTS",
                                branchCode, companyCode, companyShortName);
                    }

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {
                    if (adPreference.getPrfEnableArReceiptBatch() == EJBCommon.TRUE) {
                        glJournalBatch = glJournalBatchHome
                                .JbName("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arReceipt.getArReceiptBatch().getRbName())
                                .JbDescription("JOURNAL IMPORT")
                                .JbStatus("CLOSED")
                                .JbDateCreated(EJBCommon.getGcCurrentDateWoTime().getTime())
                                .JbCreatedBy(USR_NM)
                                .JbAdBranch(branchCode)
                                .JbAdCompany(companyCode)
                                .buildJournalBatch(companyShortName);
                    } else {
                        glJournalBatch = glJournalBatchHome
                                .JbName("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES RECEIPTS")
                                .JbDescription("JOURNAL IMPORT")
                                .JbStatus("CLOSED")
                                .JbDateCreated(EJBCommon.getGcCurrentDateWoTime().getTime())
                                .JbCreatedBy(USR_NM)
                                .JbAdBranch(branchCode)
                                .JbAdCompany(companyCode)
                                .buildJournalBatch(companyShortName);
                    }
                }

                // create journal entry
                String customerName = arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName();

                LocalGlJournal glJournal = glJournalHome
                        .JrName(arReceipt.getRctReferenceNumber())
                        .JrDescription(arReceipt.getRctDescription())
                        .JrEffectiveDate(arReceipt.getRctDate())
                        .JrDocumentNumber(arReceipt.getRctNumber())
                        .JrConversionRate(1d)
                        .JrPosted(EJBCommon.TRUE)
                        .JrCreatedBy(USR_NM)
                        .JrLastModifiedBy(USR_NM)
                        .JrPostedBy(USR_NM)
                        .JrDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime())
                        .JrTin(arReceipt.getArCustomer().getCstTin())
                        .JrSubLedger(customerName)
                        .JrAdBranch(branchCode)
                        .JrAdCompany(companyCode)
                        .buildJournal(companyShortName);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS RECEIVABLES", companyCode, companyShortName);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode, companyShortName);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("SALES RECEIPTS", companyCode, companyShortName);
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

                    LocalGlJournalLine glJournalLine = glJournalLineHome
                            .JlLineNumber(arDistributionRecord.getDrLine())
                            .JlDebit(arDistributionRecord.getDrDebit())
                            .JlAmount(DR_AMNT)
                            .JlAdCompany(companyCode)
                            .buildJournalLine(companyShortName);

                    arDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);
                    glJournal.addGlJournalLine(glJournalLine);

                    arDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation
                    int FC_CODE = arDistributionRecord.getArAppliedInvoice() != null ?
                            arInvoice.getGlFunctionalCurrency().getFcCode() :
                            arReceipt.getGlFunctionalCurrency().getFcCode();

                    if ((FC_CODE != adCompany.getGlFunctionalCurrency().getFcCode()) &&
                            glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null &&
                            (FC_CODE == glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode())) {

                        double CONVERSION_RATE = arDistributionRecord.getArAppliedInvoice() != null ?
                                arInvoice.getInvConversionRate() : arReceipt.getRctConversionRate();

                        Date DATE = arDistributionRecord.getArAppliedInvoice() != null ?
                                arInvoice.getInvConversionDate() : arReceipt.getRctConversionDate();

                        if (DATE != null && (CONVERSION_RATE == 0 || CONVERSION_RATE == 1)) {
                            CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(),
                                    glJournal.getJrConversionDate(), companyCode, companyShortName);

                        } else if (CONVERSION_RATE == 0) {
                            CONVERSION_RATE = 1;
                        }

                        Collection glForexLedgers = null;
                        DATE = arDistributionRecord.getArAppliedInvoice() != null ? arInvoice.getInvDate() : arReceipt.getRctDate();
                        try {
                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(
                                    DATE, glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode,
                                    companyShortName);
                        } catch (FinderException ex) {
                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();
                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(DATE) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = arDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        } else {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                        }

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

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(
                                    glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(),
                                    glForexLedger.getFrlAdCompany(), companyShortName);

                        } catch (FinderException ex) {
                        }

                        for (Object forexLedger : glForexLedgers) {
                            glForexLedger = (LocalGlForexLedger) forexLedger;
                            FRL_AMNT = arDistributionRecord.getDrAmount();

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
                    glJournal.addGlJournalLine(glOffsetJournalLine);
                }

                // post journal to gl
                Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode, companyShortName);
                for (Object journalLine : glJournalLines) {
                    LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                    // post current to current acv
                    this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true,
                            glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode, companyShortName);

                    // post to subsequent acvs (propagate)
                    Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(
                            glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode, companyShortName);

                    for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {
                        LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;
                        this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false,
                                glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode, companyShortName);
                    }

                    // post to subsequent years if necessary
                    Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(
                            glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode, companyShortName);

                    if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                        adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                        LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), companyCode, companyShortName);

                        for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {
                            LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;
                            String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                            // post to subsequent acvs of subsequent set of book(propagate)
                            Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(
                                    glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode, companyShortName);

                            for (Object accountingCalendarValue : glAccountingCalendarValues) {
                                LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                                if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {
                                    this.postToGl(glSubsequentAccountingCalendarValue,
                                            glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(),
                                            glJournalLine.getJlAmount(), companyCode, companyShortName);
                                } else {
                                    // revenue & expense
                                    this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false,
                                            glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode, companyShortName);
                                }
                            }

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0)
                                break;
                        }
                    }
                }

                if (arReceipt.getArCustomer().getApSupplier() != null) {

                    // Post Investors Account balance
                    byte scLedger = arReceipt.getArCustomer().getApSupplier().getApSupplierClass().getScLedger();

                    // post current to current acv
                    if (scLedger == EJBCommon.TRUE) {

                        this.postToGlInvestor(glAccountingCalendarValue, arReceipt.getArCustomer().getApSupplier(),
                                true, arReceipt.getRctInvtrBeginningBalance(),
                                arReceipt.getRctVoid() == EJBCommon.FALSE ? EJBCommon.FALSE : EJBCommon.TRUE,
                                arReceipt.getRctVoid() == EJBCommon.FALSE ? arReceipt.getRctAmount()
                                        : -arReceipt.getRctAmount(),
                                companyCode, companyShortName);

                        // post to subsequent acvs (propagate)
                        Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(
                                glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode, companyShortName);

                        for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {
                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                            this.postToGlInvestor(glSubsequentAccountingCalendarValue,
                                    arReceipt.getArCustomer().getApSupplier(), false, EJBCommon.FALSE,
                                    arReceipt.getRctVoid() == EJBCommon.FALSE ? EJBCommon.FALSE : EJBCommon.TRUE,
                                    arReceipt.getRctVoid() == EJBCommon.FALSE ? arReceipt.getRctAmount()
                                            : -arReceipt.getRctAmount(),
                                    companyCode, companyShortName);
                        }

                        // post to subsequent years if necessary
                        Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(
                                glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode, companyShortName);

                        if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                            adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);

                            for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                                LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                                // post to subsequent acvs of subsequent set of book(propagate)

                                Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(
                                        glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode, companyShortName);

                                for (Object accountingCalendarValue : glAccountingCalendarValues) {

                                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                                    this.postToGlInvestor(glSubsequentAccountingCalendarValue,
                                            arReceipt.getArCustomer().getApSupplier(), false,
                                            arReceipt.getRctInvtrBeginningBalance(),
                                            arReceipt.getRctVoid() == EJBCommon.FALSE ? EJBCommon.FALSE
                                                    : EJBCommon.TRUE,
                                            arReceipt.getRctVoid() == EJBCommon.FALSE ? arReceipt.getRctAmount()
                                                    : -arReceipt.getRctAmount(),
                                            companyCode, companyShortName);
                                }

                                if (glSubsequentSetOfBook.getSobYearEndClosed() == 0)
                                    break;
                            }
                        }
                    }
                }
            }

        } catch (GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyVoidPostedException | GlobalTransactionAlreadyPostedException | GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException ex) {

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

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String foreignCurrency, Date CONVERSION_DATE,
                                                      double CONVERSION_RATE, double AMOUNT, Integer companyCode, String companyShortName) {
        Debug.print("ArReceiptEntryApiControllerBean convertForeignToFunctionalCurrency");
        LocalAdCompany adCompany = null;
        try {
            adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        // Convert to functional currency if necessary
        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {
            AMOUNT = AMOUNT / CONVERSION_RATE;
        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private void receiptNumberValidation(ArReceiptDetails details, Integer branchCode, Integer companyCode, LocalArReceipt arReceipt)
            throws GlobalDocumentNumberNotUniqueException, ArREDuplicatePayfileReferenceNumberException {
        Debug.print("ArReceiptEntryApiControllerBean receiptNumberValidation");

        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

        if (details.getRctCode() == null) {
            String documentType = details.getRctDocumentType();
            try {
                if (documentType != null) {
                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode, details.getCompanyShortName());
                } else {
                    documentType = "AR RECEIPT";
                }
            } catch (FinderException ex) {
                documentType = "AR RECEIPT";
            }

            try {
                adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode, details.getCompanyShortName());
                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(
                        adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode, details.getCompanyShortName());
            } catch (FinderException ex) {
            }

            LocalArReceipt arExistingReceipt = null;
            try {
                arExistingReceipt = arReceiptHome.findByRctNumberAndBrCode(details.getRctNumber(), branchCode, companyCode, details.getCompanyShortName());
            } catch (FinderException ex) {
            }

            if (arExistingReceipt != null) {
                throw new GlobalDocumentNumberNotUniqueException();
            }

            if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' &&
                    (details.getRctNumber() == null || details.getRctNumber().trim().length() == 0)) {
                while (true) {
                    if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {
                        try {
                            arReceiptHome.findByRctNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(),
                                    branchCode, companyCode, details.getCompanyShortName());
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                        } catch (FinderException ex) {
                            details.setRctNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            break;
                        }
                    } else {
                        try {
                            arReceiptHome.findByRctNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(),
                                    branchCode, companyCode, details.getCompanyShortName());
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
                arExistingReceipt = arReceiptHome.findByRctNumberAndBrCode(details.getRctNumber(), branchCode, companyCode, details.getCompanyShortName());
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
                    arReceiptHome.findByPayfileReferenceNumberAndCompanyCode(details.getRctPayfileReferenceNumber(), companyCode, details.getCompanyShortName());
                    throw new ArREDuplicatePayfileReferenceNumberException();
                } catch (FinderException ex) {
                }
            }
        }
    }

    private void checkConversionDateValidation(ArReceiptDetails details, String foreignCurrency, Integer companyCode)
            throws GlobalConversionDateNotExistException {
        Debug.print("ArReceiptEntryApiControllerBean checkConversionDateValidation");

        try {

            if (details.getRctConversionDate() != null) {
                LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(foreignCurrency, companyCode, details.getCompanyShortName());
                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {
                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(
                            glValidateFunctionalCurrency.getFcCode(), details.getRctConversionDate(), companyCode, details.getCompanyShortName());
                } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {
                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(
                            adCompany.getGlFunctionalCurrency().getFcCode(), details.getRctConversionDate(), companyCode, details.getCompanyShortName());
                }
            }
        } catch (FinderException ex) {
            throw new GlobalConversionDateNotExistException();
        }
    }

    private double getArRctCreditBalanceByCstCustomerCode(String customerCode, Integer AD_BRANCH, Integer companyCode, String companyShortName)
            throws GlobalNoRecordFoundException {
        Debug.print("ArReceiptEntryApiControllerBean getArRctCreditBalanceByCstCustomerCode");
        try {
            double creditbalanceRemaining = 0d;
            Collection cmAdjustments = null;
            Collection arAppliedInvoices = null;
            try {
                cmAdjustments = cmAdjustmentHome.findPostedAdjByCustomerCode(customerCode, companyCode, companyShortName);
                for (Object adjustment : cmAdjustments) {
                    LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) adjustment;
                    Collection arAppliedCredits = cmAdjustment.getArAppliedCredits();
                    Iterator x = arAppliedCredits.iterator();
                    double totalAppliedCredit = 0d;
                    while (x.hasNext()) {
                        LocalArAppliedCredit arAppliedCredit = (LocalArAppliedCredit) x.next();
                        totalAppliedCredit += arAppliedCredit.getAcApplyCredit();
                    }
                    creditbalanceRemaining += cmAdjustment.getAdjAmount() - totalAppliedCredit
                            - cmAdjustment.getAdjRefundAmount();
                }
            } catch (FinderException ex) {
            }
            return EJBCommon.roundIt(creditbalanceRemaining, this.getGlFcPrecisionUnit(companyCode, companyShortName));

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private ArrayList getArIpsByCstCstmrCodeAndInvcRfrncNmbr(
            String customerCode, String AR_INVC_RFRNC_NMBR, Date RCT_DT,
            boolean ENBL_RBT, Integer branchCode, Integer companyCode, String companyShortName)
            throws GlobalNoRecordFoundException {
        Debug.print("ArReceiptEntryApiControllerBean getArIpsByCstCstmrCodeAndInvcRfrncNmbr");

        try {
            // if getting all invoice in all branch
            Collection arInvoicePaymentSchedules;
            arInvoicePaymentSchedules = arInvoicePaymentScheduleHome.findIpsByCstCstmrCodeAndInvNumberAdBranchCompny(
                    customerCode, AR_INVC_RFRNC_NMBR, branchCode, companyCode, companyShortName);
            if (arInvoicePaymentSchedules.isEmpty()) {
                return new ArrayList();
            }
            return this.getArModInvoicePaymentSchedules(RCT_DT, ENBL_RBT, companyCode, arInvoicePaymentSchedules, companyShortName);
        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private ArrayList getArModInvoicePaymentSchedules(Date RCT_DT, boolean ENBL_RBT, Integer companyCode,
                                                      Collection arInvoicePaymentSchedules, String companyShortName)
            throws FinderException, GlobalNoRecordFoundException {
        Debug.print("ArReceiptEntryApiControllerBean getArModInvoicePaymentSchedules");

        ArrayList list = new ArrayList();

        short precisionUnit = this.getGlFcPrecisionUnit(companyCode, companyShortName);
        for (Object invoicePaymentSchedule : arInvoicePaymentSchedules) {
            LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) invoicePaymentSchedule;
            // verification if ips is already closed
            /*
             * if (EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountDue(),
             * precisionUnit) == EJBCommon
             * .roundIt(arInvoicePaymentSchedule.getIpsAmountPaid(), precisionUnit))
             * continue;
             */
            ArModInvoicePaymentScheduleDetails mdetails = new ArModInvoicePaymentScheduleDetails();

            mdetails.setIpsCode(arInvoicePaymentSchedule.getIpsCode());
            mdetails.setIpsNumber(arInvoicePaymentSchedule.getIpsNumber());
            mdetails.setIpsArCustomerCode(arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstCustomerCode());
            mdetails.setIpsInvDate(arInvoicePaymentSchedule.getArInvoice().getInvDate());
            mdetails.setIpsDueDate(arInvoicePaymentSchedule.getIpsDueDate());
            mdetails.setIpsAmountDue(arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid());
            mdetails.setIpsPenaltyDue(arInvoicePaymentSchedule.getIpsPenaltyDue() - arInvoicePaymentSchedule.getIpsPenaltyPaid());

            double interestDue = arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstEnableRebate() == EJBCommon.TRUE ?
                    arInvoicePaymentSchedule.getIpsInterestDue() : 0d;

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
                Collection adDiscounts = adDiscountHome.findByPsLineNumberAndPytName(arInvoicePaymentSchedule.getIpsNumber(),
                        arInvoicePaymentSchedule.getArInvoice().getAdPaymentTerm().getPytName(), companyCode, companyShortName);
                for (Object discount : adDiscounts) {
                    LocalAdDiscount adDiscount = (LocalAdDiscount) discount;
                    if (adDiscount.getDscPaidWithinDay() >= INVOICE_AGE) {
                        IPS_DSCNT_AMNT = EJBCommon.roundIt(mdetails.getIpsAmountDue() * adDiscount.getDscDiscountPercent() / 100,
                                this.getGlFcPrecisionUnit(companyCode, companyShortName));
                        break;
                    }
                }
            }

            double IPS_REBATE = 0d;
            if (arInvoicePaymentSchedule.getArInvoice().getAdPaymentTerm().getPytDiscountOnInvoice() == EJBCommon.FALSE &&
                    arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstEnableRebate() == EJBCommon.TRUE && ENBL_RBT) {
                if (0 >= INVOICE_AGE) {
                    IPS_REBATE = EJBCommon.roundIt(mdetails.getIpsInterestDue(), this.getGlFcPrecisionUnit(companyCode, companyShortName));
                }
            }

            //TODO: Identify why does there is a requirement to compute for the tax withheld?
            // calculate default tax withheld
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode, companyShortName);
            double APPLY_AMOUNT = mdetails.getIpsAmountDue() - IPS_DSCNT_AMNT;
            double PENALTY_APPLY_AMOUNT = mdetails.getIpsPenaltyDue();
            double W_TAX_AMOUNT = 0d;

            if (arInvoicePaymentSchedule.getArInvoice().getArWithholdingTaxCode().getWtcRate() != 0 &&
                    adPreference.getPrfArWTaxRealization().equals("COLLECTION")) {
                LocalArTaxCode arTaxCode = arInvoicePaymentSchedule.getArInvoice().getArTaxCode();

                double NET_AMOUNT = 0d;
                if (arTaxCode.getTcType().equals("INCLUSIVE") || arTaxCode.getTcType().equals("EXCLUSIVE")) {
                    NET_AMOUNT = EJBCommon.roundIt(APPLY_AMOUNT / (1 + (arTaxCode.getTcRate() / 100)),
                            this.getGlFcPrecisionUnit(companyCode, companyShortName));
                } else {
                    NET_AMOUNT = APPLY_AMOUNT;
                }
                W_TAX_AMOUNT = EJBCommon.roundIt(NET_AMOUNT * (arInvoicePaymentSchedule.getArInvoice().getArWithholdingTaxCode().getWtcRate() / 100),
                        this.getGlFcPrecisionUnit(companyCode, companyShortName));
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
    }

    private short getGlFcPrecisionUnit(Integer companyCode, String companyShortName) {
        Debug.print("ArReceiptEntryApiControllerBean getGlFcPrecisionUnit");

        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
            return adCompany.getGlFunctionalCurrency().getFcPrecision();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalArAppliedInvoice addArAiEntry(ArModAppliedInvoiceDetails mdetails, LocalArReceipt arReceipt, Integer companyCode, String companyShortName)
            throws ArINVOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException,
            ArRCTInvoiceHasNoWTaxCodeException, ArINVOverCreditBalancePaidapplicationNotAllowedException {
        Debug.print("ArReceiptEntryApiControllerBean addArAiEntry");

        try {

            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode, companyShortName);

            // get functional currency name
            String foreignCurrency = adCompany.getGlFunctionalCurrency().getFcName();

            // validate over application
            LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arInvoicePaymentScheduleHome.findByPrimaryKey(mdetails.getAiIpsCode(), companyShortName);

            double ipsAmount = arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid();
            double totalAmountDue = EJBCommon.roundIt(ipsAmount, this.getGlFcPrecisionUnit(companyCode, companyShortName));

            // This is to remove withholding tax from the input apply amount
            double netOfCreditableWTax = mdetails.getAiApplyAmount() - mdetails.getAiCreditableWTax();
            mdetails.setAiApplyAmount(netOfCreditableWTax);

            double totalApplyAmount = EJBCommon.roundIt(
                    mdetails.getAiApplyAmount() +
                            mdetails.getAiCreditBalancePaid() +
                            mdetails.getAiCreditableWTax() +
                            mdetails.getAiDiscountAmount(), this.getGlFcPrecisionUnit(companyCode, companyShortName));

            if (totalAmountDue < totalApplyAmount) {
                throw new ArINVOverapplicationNotAllowedException(arInvoicePaymentSchedule.getArInvoice().getInvReferenceNumber());
            }

            double remainingCreditBalance = this.getArRctCreditBalanceByCstCustomerCode(
                    arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstCustomerCode(),
                    arInvoicePaymentSchedule.getArInvoice().getInvAdBranch(), companyCode, adCompany.getCmpShortName());

            if (remainingCreditBalance < mdetails.getAiCreditBalancePaid()) {
                throw new ArINVOverCreditBalancePaidapplicationNotAllowedException(arInvoicePaymentSchedule.getArInvoice().getInvReferenceNumber());
            }

            // validate if ips already locked
            // display invoice reference number for API response to TT
            if (arInvoicePaymentSchedule.getIpsLock() == EJBCommon.TRUE) {
                throw new GlobalTransactionAlreadyLockedException(arInvoicePaymentSchedule.getArInvoice().getInvReferenceNumber());
            }

            // validate invoice withholding tax code if necessary
            if (mdetails.getAiCreditableWTax() != 0 &&
                    arInvoicePaymentSchedule.getArInvoice().getArWithholdingTaxCode().getGlChartOfAccount() == null &&
                    (adPreference.getArWithholdingTaxCode() == null || adPreference.getArWithholdingTaxCode().getGlChartOfAccount() == null)) {
                throw new ArRCTInvoiceHasNoWTaxCodeException(arInvoicePaymentSchedule.getArInvoice().getInvReferenceNumber());
            }

            double AI_FRX_GN_LSS = 0d;
            if (!foreignCurrency.equals(arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName()) ||
                    !foreignCurrency.equals(arReceipt.getGlFunctionalCurrency().getFcName())) {

                double AI_ALLCTD_PYMNT_AMNT = this.convertForeignToFunctionalCurrency(
                        arReceipt.getGlFunctionalCurrency().getFcCode(),
                        arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(),
                        arReceipt.getRctConversionRate(),
                        mdetails.getAiApplyAmount() + mdetails.getAiCreditBalancePaid(), companyCode, companyShortName);

                double AI_APPLY_AMNT = this.convertForeignToFunctionalCurrency(
                        arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcCode(),
                        arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName(),
                        arInvoicePaymentSchedule.getArInvoice().getInvConversionDate(),
                        arInvoicePaymentSchedule.getArInvoice().getInvConversionRate(),
                        mdetails.getAiApplyAmount() + mdetails.getAiCreditBalancePaid(), companyCode, companyShortName);

                double AI_CRDTBL_W_TX = this.convertForeignToFunctionalCurrency(
                        arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcCode(),
                        arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName(),
                        arInvoicePaymentSchedule.getArInvoice().getInvConversionDate(),
                        arInvoicePaymentSchedule.getArInvoice().getInvConversionRate(), mdetails.getAiCreditableWTax(),
                        companyCode, companyShortName);

                double AI_DSCNT_AMNT = this.convertForeignToFunctionalCurrency(
                        arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcCode(),
                        arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName(),
                        arInvoicePaymentSchedule.getArInvoice().getInvConversionDate(),
                        arInvoicePaymentSchedule.getArInvoice().getInvConversionRate(), mdetails.getAiDiscountAmount(),
                        companyCode, companyShortName);

                double AI_APPLD_DPST = this.convertForeignToFunctionalCurrency(
                        arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcCode(),
                        arInvoicePaymentSchedule.getArInvoice().getGlFunctionalCurrency().getFcName(),
                        arInvoicePaymentSchedule.getArInvoice().getInvConversionDate(),
                        arInvoicePaymentSchedule.getArInvoice().getInvConversionRate(), mdetails.getAiAppliedDeposit(),
                        companyCode, companyShortName);

                AI_FRX_GN_LSS = EJBCommon.roundIt((AI_ALLCTD_PYMNT_AMNT + AI_CRDTBL_W_TX + AI_DSCNT_AMNT + AI_APPLD_DPST) -
                        (AI_APPLY_AMNT + AI_CRDTBL_W_TX + AI_DSCNT_AMNT + AI_APPLD_DPST), this.getGlFcPrecisionUnit(companyCode, companyShortName));

            }

            // create applied invoice
            LocalArAppliedInvoice arAppliedInvoice = arAppliedInvoiceHome
                    .AiApplyAmount(mdetails.getAiApplyAmount())
                    .AiPenaltyApplyAmount(mdetails.getAiPenaltyApplyAmount())
                    .AiCreditableWTax(mdetails.getAiCreditableWTax())
                    .AiDiscountAmount(mdetails.getAiDiscountAmount())
                    .AiRebate(mdetails.getAiRebate())
                    .AiAppliedDeposit(mdetails.getAiAppliedDeposit())
                    .AiAllocatedPaymentAmount(mdetails.getAiAllocatedPaymentAmount())
                    .AiForexGainLoss(AI_FRX_GN_LSS)
                    .AiApplyRebate(mdetails.getAiApplyRebate())
                    .AiAdCompany(companyCode)
                    .buildAppliedInvoice(companyShortName);

            arAppliedInvoice.setAiCreditBalancePaid(mdetails.getAiCreditBalancePaid());
            arInvoicePaymentSchedule.addArAppliedInvoice(arAppliedInvoice);

            // update cm adjustment advance remaining balance and create applied credit
            if (mdetails.getAiCreditBalancePaid() > 0) {
                Collection cmAdjustments = cmAdjustmentHome.findPostedAdjByCustomerCode(
                        arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstCustomerCode(), companyCode, companyShortName);
                Iterator i = cmAdjustments.iterator();
                double creditBalancePaid = this.convertForeignToFunctionalCurrency(
                        arReceipt.getGlFunctionalCurrency().getFcCode(),
                        arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(),
                        arReceipt.getRctConversionRate(), mdetails.getAiCreditBalancePaid(), companyCode, companyShortName);

                // Getting Customer Balance from cmAdjustment(s). and check how many left
                while (i.hasNext()) {
                    LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();
                    Collection arAppliedCredits = cmAdjustment.getArAppliedCredits();
                    Iterator x = arAppliedCredits.iterator();
                    double totalAppliedCredit = 0d;
                    while (x.hasNext()) {
                        LocalArAppliedCredit arAppliedCredit = (LocalArAppliedCredit) x.next();
                        totalAppliedCredit += arAppliedCredit.getAcApplyCredit();
                    }

                    // how many left credit balance of customer
                    double advanceRemaining = EJBCommon.roundIt(
                            cmAdjustment.getAdjAmount() - totalAppliedCredit - cmAdjustment.getAdjRefundAmount(),
                            this.getGlFcPrecisionUnit(companyCode, companyShortName));

                    if (advanceRemaining <= 0)
                        continue;

                    if (creditBalancePaid <= advanceRemaining) {
                        cmAdjustment.addArAppliedCredit(this.addArAcEntry(
                                EJBCommon.roundIt(creditBalancePaid, this.getGlFcPrecisionUnit(companyCode, companyShortName)),
                                arAppliedInvoice, companyCode));
                        creditBalancePaid -= creditBalancePaid;
                    } else {
                        cmAdjustment.addArAppliedCredit(this.addArAcEntry(
                                EJBCommon.roundIt(advanceRemaining, this.getGlFcPrecisionUnit(companyCode, companyShortName)),
                                arAppliedInvoice, companyCode));
                        creditBalancePaid -= advanceRemaining;
                    }
                    if (creditBalancePaid <= 0)
                        break;
                }
            }

            // arReceipt.addArAppliedInvoice(arAppliedInvoice);
            // arAppliedInvoice.setArReceipt(arReceipt);
            // arAppliedInvoice.setArInvoicePaymentSchedule(arInvoicePaymentSchedule);
            // arInvoicePaymentSchedule.addArAppliedInvoice(arAppliedInvoice);
            // lock invoice
            arInvoicePaymentSchedule.setIpsLock(EJBCommon.TRUE);
            return arAppliedInvoice;

        } catch (ArINVOverapplicationNotAllowedException | ArRCTInvoiceHasNoWTaxCodeException | GlobalTransactionAlreadyLockedException | ArINVOverCreditBalancePaidapplicationNotAllowedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalArAppliedCredit addArAcEntry(double APPLY_AMNT, LocalArAppliedInvoice arAppliedInvoice, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {
        Debug.print("ArReceiptEntryApiControllerBean addArAcEntry");

        try {
            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            // create distribution record
            LocalArAppliedCredit arAppliedCredit = arAppliedCreditHome.create(
                    EJBCommon.roundIt(APPLY_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), companyCode);
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

    private void updateCustomerBalance(Date RCT_DT, double RCT_AMNT, LocalArCustomer arCustomer, Integer companyCode, String companyShortName) {
        Debug.print("ArReceiptEntryApiControllerBean updateCustomerBalance");

        try {

            // find customer balance before or equal invoice date

            Collection arCustomerBalances = arCustomerBalanceHome.findByBeforeOrEqualInvDateAndCstCustomerCode(RCT_DT,
                    arCustomer.getCstCustomerCode(), companyCode, companyShortName);

            if (!arCustomerBalances.isEmpty()) {

                // get last invoice

                ArrayList arCustomerBalanceList = new ArrayList(arCustomerBalances);

                LocalArCustomerBalance arCustomerBalance = (LocalArCustomerBalance) arCustomerBalanceList
                        .get(arCustomerBalanceList.size() - 1);

                if (arCustomerBalance.getCbDate().before(RCT_DT)) {

                    // create new balance

                    LocalArCustomerBalance arNewCustomerBalance = arCustomerBalanceHome
                            .CbDate(RCT_DT)
                            .CbBalance(arCustomerBalance.getCbBalance() + RCT_AMNT)
                            .CbAdCompany(companyCode)
                            .buildCustomerBalance(companyShortName);

                    arCustomer.addArCustomerBalance(arNewCustomerBalance);

                } else { // equals to invoice date

                    arCustomerBalance.setCbBalance(arCustomerBalance.getCbBalance() + RCT_AMNT);
                }

            } else {

                // create new balance
                LocalArCustomerBalance arNewCustomerBalance = arCustomerBalanceHome
                        .CbDate(RCT_DT)
                        .CbBalance(RCT_AMNT)
                        .CbAdCompany(companyCode)
                        .buildCustomerBalance(companyShortName);

                arCustomer.addArCustomerBalance(arNewCustomerBalance);
            }

            // propagate to subsequent balances if necessary
            arCustomerBalances = arCustomerBalanceHome.findByAfterInvDateAndCstCustomerCode(RCT_DT,
                    arCustomer.getCstCustomerCode(), companyCode, companyShortName);

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

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount,
                          boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode, String companyShortName) {
        Debug.print("ArReceiptEntryApiControllerBean postToGl");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(
                    glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode, companyShortName);

            String ACCOUNT_TYPE = glChartOfAccount.getCoaAccountType();
            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

            if (((ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("EXPENSE")) && isDebit == EJBCommon.TRUE)
                    || (!ACCOUNT_TYPE.equals("ASSET") && !ACCOUNT_TYPE.equals("EXPENSE")
                    && isDebit == EJBCommon.FALSE)) {

                glChartOfAccountBalance.setCoabEndingBalance(
                        EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() + JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon
                            .roundIt(glChartOfAccountBalance.getCoabBeginningBalance() + JL_AMNT, FC_EXTNDD_PRCSN));
                }

            } else {

                glChartOfAccountBalance.setCoabEndingBalance(
                        EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() - JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon
                            .roundIt(glChartOfAccountBalance.getCoabBeginningBalance() - JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }

            if (isCurrentAcv) {

                if (isDebit == EJBCommon.TRUE) {

                    glChartOfAccountBalance.setCoabTotalDebit(
                            EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalDebit() + JL_AMNT, FC_EXTNDD_PRCSN));

                } else {

                    glChartOfAccountBalance.setCoabTotalCredit(
                            EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalCredit() + JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double getFrRateByFrNameAndFrDate(String foreignCurrency, Date CONVERSION_DATE, Integer companyCode, String companyShortName)
            throws GlobalConversionDateNotExistException {
        Debug.print("ArReceiptEntryApiControllerBean getFrRateByFrNameAndFrDate");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(foreignCurrency, companyCode, companyShortName);

            double CONVERSION_RATE = 1;

            // Get functional currency rate
            if (!foreignCurrency.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(
                        glFunctionalCurrency.getFcCode(), CONVERSION_DATE, companyCode, companyShortName);
                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary
            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {
                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(
                        adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, companyCode, companyShortName);
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

    private void postToGlInvestor(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalApSupplier apSupplier,
                                  boolean isCurrentAcv, byte isBeginningBalance, byte isDebit, double JL_AMNT, Integer companyCode,
                                  String companyShortName) {
        Debug.print("ArReceiptEntryApiControllerBean postToGlInvestor");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode, companyShortName);

            LocalGlInvestorAccountBalance glInvestorAccountBalance = glInvestorAccountBalanceHome
                    .findByAcvCodeAndSplCode(glAccountingCalendarValue.getAcvCode(), apSupplier.getSplCode(), companyCode, companyShortName);

            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

            glInvestorAccountBalance.setIrabEndingBalance(
                    EJBCommon.roundIt(glInvestorAccountBalance.getIrabEndingBalance() + JL_AMNT, FC_EXTNDD_PRCSN));

            if (!isCurrentAcv) {

                glInvestorAccountBalance.setIrabBeginningBalance(EJBCommon
                        .roundIt(glInvestorAccountBalance.getIrabBeginningBalance() + JL_AMNT, FC_EXTNDD_PRCSN));
            }

            if (isCurrentAcv) {

                glInvestorAccountBalance.setIrabTotalCredit(
                        EJBCommon.roundIt(glInvestorAccountBalance.getIrabTotalCredit() + JL_AMNT, FC_EXTNDD_PRCSN));
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

    @Override
    public ReceiptApiResponse createReceipt(ReceiptRequest receiptRequest) {
        Debug.print("ArReceiptEntryApiControllerBean createReceipt");

        ReceiptApiResponse apiResponse = new ReceiptApiResponse();
        LocalAdCompany adCompany = null;
        LocalAdBranch adBranch = null;
        LocalAdUser adUser = null;
        LocalArCustomer arCustomer = null;
        String currencyCode = receiptRequest.getCurrency();
        String paymentMethod = "CASH";
        String bankAccount = null;
        Date receiptDate = EJBCommon.convertStringToSQLDate(receiptRequest.getReceiptDate(), ConfigurationClass.DEFAULT_DATE_FORMAT);
        double applyAmount = 0;
        double applyCbAmount = 0;

        try {
            ArReceiptDetails details = new ArReceiptDetails();

            // Company Code
            try {
                if (receiptRequest.getCompanyCode() == null || receiptRequest.getCompanyCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return apiResponse;
                }
                adCompany = adCompanyHome.findByCmpShortName(receiptRequest.getCompanyCode());
                details.setRctAdCompany(adCompany.getCmpCode());
                details.setCompanyShortName(adCompany.getCmpShortName());
            } catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return apiResponse;
            }

            // Branch Code
            try {
                if (receiptRequest.getBranchCode() == null || receiptRequest.getBranchCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return apiResponse;
                }
                adBranch = adBranchHome.findByBrBranchCode(receiptRequest.getBranchCode(), adCompany.getCmpCode(), adCompany.getCmpShortName());
                details.setRctAdBranch(adBranch.getBrCode());
            } catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, receiptRequest.getBranchCode()));
                return apiResponse;
            }

            // User
            try {
                if (receiptRequest.getUsername() == null || receiptRequest.getUsername().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_042);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_042_MSG);
                    return apiResponse;
                }
                adUser = adUserHome.findByUsrName(receiptRequest.getUsername(), adCompany.getCmpCode(), adCompany.getCmpShortName());
                details.setRctCreatedBy(adUser.getUsrName());
                details.setRctLastModifiedBy(adUser.getUsrName());
            } catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_002);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_002_MSG);
                return apiResponse;
            }

            // Customer Code
            try {
                if (receiptRequest.getCustomerCode() == null || receiptRequest.getCustomerCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return apiResponse;
                }
                arCustomer = arCustomerHome.findByCstCustomerCode(receiptRequest.getCustomerCode(), adCompany.getCmpCode(), adCompany.getCmpShortName());
            } catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_009);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_009_MSG);
                return apiResponse;
            }

//            if (receiptRequest.getTransactionType().equals("D")) {
//                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_062);
//                apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_062_MSG, "transactionType", "D"));
//                return apiResponse;
//            }

            // Adding applied Invoice
            ArrayList receiptLines = new ArrayList();
            for (ReceiptDetails receiptDetails : receiptRequest.getReceiptDetails()) {

                // Get Credit Balance of Customer
                // NOTE: Credit Balance disabled. Receipt now need to full amount due of an invoice
                // creditBalance = this.getArRctCreditBalanceByCstCustomerCode(arCustomer.getCstCustomerCode(), adBranch.getBrCode(), adCompany.getCmpCode());
                // Find Invoice By Reference Number (Bmobile API)
                ArrayList invoiceLines = this.getArIpsByCstCstmrCodeAndInvcRfrncNmbr(arCustomer.getCstCustomerCode(),
                        receiptDetails.getInvoiceNumber(), receiptDate, false, adBranch.getBrCode(),
                        adCompany.getCmpCode(), adCompany.getCmpShortName());

                // Expecting the invoice/reference number must be unique. If in case there is a 2 duplicate invoices found, prior to get is the index 0
                ArModInvoicePaymentScheduleDetails arModInvoicePaymentScheduleDetail = null;
                if (invoiceLines.size() > 0) {
                    arModInvoicePaymentScheduleDetail = (ArModInvoicePaymentScheduleDetails) invoiceLines.get(0);
                } else {
                    // this will call "no invoice exist within that customer or ref invoice number
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_024);
                    apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_024_MSG, receiptDetails.getTransactionDetails()));
                    return apiResponse;
                }

                // Validate invoice: fully paid
                if (arModInvoicePaymentScheduleDetail.getIpsAmountPaid() >= arModInvoicePaymentScheduleDetail.getIpsAmountDue()) {
                    // this will call "already paid" invoice.
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_030);
                    apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_030_MSG, receiptDetails.getTransactionDetails()));
                    return apiResponse;
                }

                if (!isApplyAmountValid(receiptRequest, receiptDetails)) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_049);
                    apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_049_MSG, receiptDetails.getTransactionDetails()));
                    return apiResponse;
                }

                // Check the allowed apply amount
                ArModAppliedInvoiceDetails appliedDetails = new ArModAppliedInvoiceDetails();
                appliedDetails.setAiIpsCode(arModInvoicePaymentScheduleDetail.getIpsCode());
                appliedDetails.setAiPenaltyApplyAmount(0);
                appliedDetails.setAiCreditableWTax(arModInvoicePaymentScheduleDetail.getIpsAiCreditableWTax());
                appliedDetails.setAiDiscountAmount(arModInvoicePaymentScheduleDetail.getIpsAiDiscountAmount());
                appliedDetails.setAiAllocatedPaymentAmount(0d);
                appliedDetails.setAiAppliedDeposit(0d);
                appliedDetails.setAiCreditBalancePaid(applyCbAmount);
                appliedDetails.setAiRebate(arModInvoicePaymentScheduleDetail.getIpsAiRebate());
                appliedDetails.setAiApplyRebate(EJBCommon.FALSE);
                appliedDetails.setAiApplyAmount(receiptDetails.getApplyAmount());
                receiptLines.add(appliedDetails);
            }

            // Get Bank Account
            bankAccount = arCustomer.getAdBankAccount().getBaName();

            // Set receipt info
            details.setRctDate(receiptDate);
            details.setRctReferenceNumber(receiptRequest.getReferenceNumber());
            details.setRctVoid(EJBCommon.FALSE);
            details.setRctDescription(receiptRequest.getDescription());
            details.setRctPaymentMethod(paymentMethod);
            details.setRctCustomerName(arCustomer.getCstName());
            details.setRctConversionRate(1);
            details.setRctConversionDate(null);
            details.setRctEnableAdvancePayment(EJBCommon.FALSE);
            details.setRctAmount(applyAmount);
            details.setRctCreatedBy(adUser.getUsrName());
            details.setRctDateCreated(new java.util.Date());
            details.setRctLastModifiedBy(adUser.getUsrName());
            details.setRctDateLastModified(new java.util.Date());

            Integer receiptCode = this.saveReceipt(details, bankAccount, currencyCode, arCustomer.getCstCustomerCode(), null, receiptLines,
                    adBranch.getBrCode(), adCompany.getCmpCode());

            LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey(receiptCode, adCompany.getCmpShortName());

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            apiResponse.setDocumentNumber(arReceipt.getRctNumber());
            apiResponse.setStatus("Created receipt transaction successfully.");

        } catch (GlobalTransactionAlreadyLockedException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_069);
            apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_069_MSG, ex.getMessage()));
            return apiResponse;

        } catch (ArINVOverapplicationNotAllowedException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_068);
            apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_068_MSG, ex.getMessage()));
            return apiResponse;

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_067);
            apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_067_MSG, ex.getMessage()));
            return apiResponse;

        } catch (GlJREffectiveDateNoPeriodExistException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_015);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_015_MSG);
            return apiResponse;

        } catch (GlJREffectiveDatePeriodClosedException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_016);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_016_MSG);
            return apiResponse;

        } catch (GlobalJournalNotBalanceException ex) {

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_017);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_017_MSG);
            return apiResponse;

        } catch (Exception ex) {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return apiResponse;
        }
        return apiResponse;
    }

    private boolean isApplyAmountValid(ReceiptRequest request, ReceiptDetails receiptDetails) {
        Debug.print("ArReceiptEntryApiControllerBean isApplyAmountValid");

        // Get Apply amount from the request
        boolean isApplyAmountValid = true;
        if (receiptDetails.getApplyAmount() == null && receiptDetails.getApplyAmount().equals("")) {
            isApplyAmountValid = false;
        } else {
            //if (request.getTransactionType().equals("C") && receiptDetails.getApplyAmount() <= 0) {
            if (receiptDetails.getApplyAmount() <= 0) {
                isApplyAmountValid = false;
            }
        }
        return isApplyAmountValid;
    }

    // Use this function if the scenarion the apply amount is distributed according to invoices submitted from receipt request
    private double autoComputeApplyAmount(double applyAmount,
                                          ArModInvoicePaymentScheduleDetails arModInvoicePaymentScheduleDetail,
                                          ArModAppliedInvoiceDetails appliedDetails) {
        Debug.print("ArReceiptEntryApiControllerBean autoComputeApplyAmount");

        if (applyAmount > 0) {
            if (applyAmount > arModInvoicePaymentScheduleDetail.getIpsAmountDue()) {
                appliedDetails.setAiApplyAmount(arModInvoicePaymentScheduleDetail.getIpsAmountDue());
            } else {
                appliedDetails.setAiApplyAmount(applyAmount);
            }
            applyAmount -= arModInvoicePaymentScheduleDetail.getIpsAmountDue();
        }
        return applyAmount;
    }
}