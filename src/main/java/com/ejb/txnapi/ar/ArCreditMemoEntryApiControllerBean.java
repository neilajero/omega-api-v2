package com.ejb.txnapi.ar;

import com.ejb.ConfigurationClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.gen.LocalGenValueSetValue;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.ar.ArINVOverapplicationNotAllowedException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ar.models.InvoiceItemRequest;
import com.ejb.restfulapi.ar.models.LineItemRequest;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;
import com.util.ar.ArInvoiceDetails;
import com.util.mod.ar.ArModInvoiceLineItemDetails;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Stateless(name = "ArCreditMemoEntryApiControllerEJB")
public class ArCreditMemoEntryApiControllerBean extends EJBContextClass
        implements ArCreditMemoEntryApiController {
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalAdBranchCustomerHome adBranchCustomerHome;
    @EJB
    private LocalAdBranchArTaxCodeHome adBranchArTaxCodeHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalArCustomerBalanceHome arCustomerBalanceHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;

    /**
     * This method saves credit memo information into Omega ERP.
     */
    @Override
    public Integer saveCreditMemo(ArInvoiceDetails invoiceDetails, String customerCode, ArrayList invoiceLines)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidPostedException, ArINVOverapplicationNotAllowedException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalTransactionAlreadyLockedException, GlobalJournalNotBalanceException,
            GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArCreditMemoEntryApiControllerBean saveCreditMemo");

        Integer companyCode = invoiceDetails.getInvAdCompany();
        Integer branchCode = invoiceDetails.getInvAdBranch();

        try {

            LocalArInvoice arCreditMemo;
            String documentType = invoiceDetails.getInvDocumentType() == null ? "AR CREDIT MEMO" : invoiceDetails.getInvDocumentType();

            generateDocumentNumber(invoiceDetails, companyCode, branchCode, documentType);

            // validate if invoice entered is already locked by cm or receipt
            Collection apValidateInvoicePaymentSchedules = arInvoicePaymentScheduleHome.findByIpsLockAndInvCode(EJBCommon.TRUE, invoiceDetails.getInvCode(), companyCode);
            if (!apValidateInvoicePaymentSchedules.isEmpty()) {
                throw new GlobalTransactionAlreadyLockedException();
            }

            // create invoice
            arCreditMemo = arInvoiceHome
                    .InvType(invoiceDetails.getInvType())
                    .InvNumber(invoiceDetails.getInvCmInvoiceNumber())
                    .InvReferenceNumber(invoiceDetails.getInvReferenceNumber())
                    .InvCmInvoiceNumber(invoiceDetails.getInvNumber()) // Reference of the Invoice applied Credit Memo
                    .InvCmReferenceNumber(invoiceDetails.getInvCmReferenceNumber())
                    .InvCreditMemo(EJBCommon.TRUE)
                    .InvDescription(invoiceDetails.getInvDescription())
                    .InvDate(invoiceDetails.getInvDate())
                    .InvEffectivityDate(invoiceDetails.getInvEffectivityDate())
                    .InvConversionRate(1.0)
                    .InvCreatedBy(invoiceDetails.getInvCreatedBy())
                    .InvDateCreated(invoiceDetails.getInvDateCreated())
                    .InvAdBranch(branchCode)
                    .InvAdCompany(companyCode)
                    .buildInvoice(invoiceDetails.getCompanyShortName());

            arCreditMemo.setInvPartialCm(invoiceDetails.getInvPartialCm());
            arCreditMemo.setReportParameter(invoiceDetails.getReportParameter());

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(customerCode, companyCode);
            arCustomer.addArInvoice(arCreditMemo);

            LocalArTaxCode arTaxCode = invoiceDetails.getArTaxCode();
            arCreditMemo.setArTaxCode(arTaxCode);

            LocalArWithholdingTaxCode arWithholdingTaxCode = invoiceDetails.getArWithholdingTaxCode();
            arCreditMemo.setArWithholdingTaxCode(arWithholdingTaxCode);

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            String centralWarehouse = !adPreference.getPrfInvCentralWarehouse().equals("") && adPreference.getPrfInvCentralWarehouse() != null
                    ? adPreference.getPrfInvCentralWarehouse()
                    : "POM WAREHOUSE LOCATION";
            LocalAdBranch centralWarehouseBranchCode = adBranchHome.findByBrName(centralWarehouse, companyCode);

            // add new invoice lines and distribution record
            double totalTaxAmount = 0d;
            double totalAmount = 0d;
            double totalDiscount = 0d;
            double totalSalesAccountCredit = 0d;

            Iterator i = invoiceLines.iterator();
            LocalInvItemLocation invItemLocation = null;
            while (i.hasNext()) {
                ArModInvoiceLineItemDetails lineDetails = (ArModInvoiceLineItemDetails) i.next();

                invItemLocation = getItemLocation(companyCode, lineDetails);
                isPriorDateAllowed(companyCode, branchCode, arCreditMemo, adPreference, invItemLocation);

//                LocalArInvoiceLineItem arInvoiceLineItem = this.addArIliEntry(lineDetails, arCreditMemo, arTaxCode,
//                        invItemLocation, arCreditMemo, branchCode, companyCode);

                LocalArInvoiceLineItem arInvoiceLineItem = this.addArIliEntry(lineDetails, arCreditMemo, invItemLocation, companyCode);

                double COST = getItemCosting(companyCode, branchCode, arCreditMemo, invItemLocation, arInvoiceLineItem);
                double quantitySold = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(),
                        arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(),
                        companyCode);

                LocalAdBranchItemLocation adBranchItemLocation = null;
                try {
                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                String distributionRecordClass = getDistributionRecordClass(companyCode, adBranchItemLocation);
                costOfGoodsSoldJournals(companyCode, branchCode, arCreditMemo, adPreference, arInvoiceLineItem, COST, quantitySold, adBranchItemLocation);

                if (lineDetails.getIliTotalDiscount() > 0) {
                    discountJournals(companyCode, branchCode, arCreditMemo, lineDetails);
                    totalDiscount += lineDetails.getIliTotalDiscount();
                }

                totalSalesAccountCredit = getSalesAccountCredit(companyCode, branchCode, arCreditMemo, totalSalesAccountCredit,
                        arInvoiceLineItem, adBranchItemLocation, distributionRecordClass);
                totalAmount += arInvoiceLineItem.getIliAmount();
                totalTaxAmount += arInvoiceLineItem.getIliTaxAmount();
            }

            Debug.print("Total Tax Amount : " + totalTaxAmount);
            taxJournals(companyCode, branchCode, arCreditMemo, arTaxCode, totalTaxAmount, centralWarehouseBranchCode);

            double unearnedInterestAmount = getUnearnedInterestAmount(companyCode, branchCode, arCreditMemo, totalAmount, totalTaxAmount);
            double withholdingTax = getWithholdingTax(companyCode, branchCode, arCreditMemo, arWithholdingTaxCode, totalAmount, adPreference);

            receivableJournals(companyCode, branchCode, arCreditMemo, totalAmount, totalTaxAmount, totalSalesAccountCredit, withholdingTax, totalDiscount);
            creditMemoAmountDue(companyCode, arCreditMemo, totalAmount, totalTaxAmount, unearnedInterestAmount, withholdingTax, totalDiscount);

            // create new invoice payment schedule lock
            Collection arInvoicePaymentSchedules = arCreditMemo.getArInvoicePaymentSchedules();
            i = arInvoicePaymentSchedules.iterator();
            while (i.hasNext()) {
                LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) i.next();
                arInvoicePaymentSchedule.setIpsLock(EJBCommon.TRUE);
            }

            this.executeArInvCreditMemoPost(arCreditMemo.getInvCode(), arCreditMemo.getInvLastModifiedBy(),
                    branchCode, companyCode);

            arCreditMemo.setInvApprovalStatus("N/A");
            return arCreditMemo.getInvCode();

        }
        catch (GlobalRecordAlreadyDeletedException | AdPRFCoaGlVarianceAccountNotFoundException |
               GlobalBranchAccountNumberInvalidException | GlobalInventoryDateException |
               GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
               GlJREffectiveDateNoPeriodExistException |
               ArINVOverapplicationNotAllowedException | GlobalTransactionAlreadyVoidPostedException |
               GlobalTransactionAlreadyPostedException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getSalesAccountCredit(Integer companyCode, Integer branchCode, LocalArInvoice arCreditMemo,
                                         double TOTAL_SALES_ACCOUNT_CREDIT, LocalArInvoiceLineItem arInvoiceLineItem,
                                         LocalAdBranchItemLocation adBranchItemLocation, String distributionRecordClass)
            throws GlobalBranchAccountNumberInvalidException {

        // add revenue/credit distributions
        if (adBranchItemLocation != null) {
            if (adBranchItemLocation.getInvItemLocation().getInvItem().getIiServices() == EJBCommon.TRUE) {
                // this will trigger by services(DEBIT)
                this.addArDrEntry(arCreditMemo.getArDrNextLine(), distributionRecordClass, EJBCommon.FALSE,
                        arInvoiceLineItem.getIliAmount(), adBranchItemLocation.getBilCoaGlSalesAccount(),
                        arCreditMemo, branchCode, companyCode);
                this.addArDrEntry(arCreditMemo.getArDrNextLine(), "OTHER", EJBCommon.TRUE,
                        arInvoiceLineItem.getIliAmount(),
                        adBranchItemLocation.getBilCoaGlSalesReturnAccount(), arCreditMemo, branchCode,
                        companyCode);
                TOTAL_SALES_ACCOUNT_CREDIT += arInvoiceLineItem.getIliAmount();

            } else {
                this.addArDrEntry(arCreditMemo.getArDrNextLine(), distributionRecordClass, EJBCommon.TRUE,
                        arInvoiceLineItem.getIliAmount(), adBranchItemLocation.getBilCoaGlSalesAccount(),
                        arCreditMemo, branchCode, companyCode);
            }

        } else {

            if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiServices() == EJBCommon.TRUE) {
                // this will trigger by services(DEBIT)
                this.addArDrEntry(arCreditMemo.getArDrNextLine(), distributionRecordClass, EJBCommon.FALSE,
                        arInvoiceLineItem.getIliAmount(),
                        arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arCreditMemo,
                        branchCode, companyCode);
                TOTAL_SALES_ACCOUNT_CREDIT += arInvoiceLineItem.getIliAmount();
            } else {
                this.addArDrEntry(arCreditMemo.getArDrNextLine(), distributionRecordClass, EJBCommon.TRUE,
                        arInvoiceLineItem.getIliAmount(),
                        arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arCreditMemo,
                        branchCode, companyCode);
            }
        }
        return TOTAL_SALES_ACCOUNT_CREDIT;
    }

    private String getDistributionRecordClass(Integer companyCode, LocalAdBranchItemLocation adBranchItemLocation) {

        LocalGlChartOfAccount glSalesCoa;
        LocalGenValueSetValue genValueSetValue = null;

        try {
            glSalesCoa = glChartOfAccountHome.findByCoaCode(adBranchItemLocation.getBilCoaGlSalesAccount(), companyCode);
            genValueSetValue = genValueSetValueHome.findByVsvValue(glSalesCoa.getCoaSegment2(), companyCode);
        }
        catch (FinderException ex) {
        }

        String distributionRecordClass = "REVENUE";
        if (genValueSetValue.getVsvDescription().equalsIgnoreCase("3G DEFERRED REVENUE") ||
                genValueSetValue.getVsvDescription().equalsIgnoreCase("4G DEFERRED REVENUE")) {
            distributionRecordClass = "LIABILITY";
        }
        return distributionRecordClass;
    }

    private void discountJournals(Integer companyCode, Integer branchCode, LocalArInvoice arCreditMemo, ArModInvoiceLineItemDetails lineDetails)
            throws GlobalBranchAccountNumberInvalidException, FinderException {

        LocalAdBranch branch = null;
        try {
            branch = adBranchHome.findByPrimaryKey(branchCode);
        }
        catch (FinderException ex) {

        }

        Integer discountCoa = null;
        try {
            List<LocalGlChartOfAccount> defaultCoa = new ArrayList(
                    glChartOfAccountHome.findHoCoaAllByCoaCategory("DISCOUNT", branch.getBrName(), companyCode));

            for (LocalGlChartOfAccount chartOfAccount : defaultCoa) {

                // Get only DISCOUNT with REVENUE class type
                if (chartOfAccount.getCoaAccountType().equals("REVENUE")) {
                    discountCoa = chartOfAccount.getCoaCode();
                }
            }

        }
        catch (FinderException ex) {
            throw new GlobalBranchAccountNumberInvalidException();
        }

        short precisionUnit = this.getGlFcPrecisionUnit(companyCode);
        double itemDiscountAmount = 0d;
        double itemDiscountTaxAmount = 0d;

        LocalArTaxCode arTaxCode = arCreditMemo.getArTaxCode();

        // calculate net amount
        itemDiscountAmount = EJBCommon.calculateNetAmount(lineDetails.getIliTotalDiscount(), lineDetails.getIliTax(), arTaxCode.getTcRate(), arTaxCode.getTcType(), precisionUnit);
        this.addArDrEntry(arCreditMemo.getArDrNextLine(), "SALES DISCOUNT", EJBCommon.FALSE, itemDiscountAmount, discountCoa, arCreditMemo, branchCode, companyCode);

        // calculate tax
        itemDiscountTaxAmount = EJBCommon.calculateTaxAmount(lineDetails.getIliTotalDiscount(), lineDetails.getIliTax(), arTaxCode.getTcRate(), arTaxCode.getTcType(), itemDiscountAmount, precisionUnit);
        LocalAdBranchArTaxCode adBranchTaxCode = null;

        try {
            adBranchTaxCode = adBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(arCreditMemo.getArTaxCode().getTcCode(), branchCode, companyCode);
            this.addArDrEntry(arCreditMemo.getArDrNextLine(), "TAX", EJBCommon.FALSE, itemDiscountTaxAmount,
                    adBranchTaxCode.getBtcGlCoaTaxCode(), arCreditMemo, branchCode, companyCode);
        }
        catch (FinderException ex) {
            this.addArDrEntry(arCreditMemo.getArDrNextLine(), "TAX", EJBCommon.FALSE, itemDiscountTaxAmount,
                    arTaxCode.getGlChartOfAccount().getCoaCode(), arCreditMemo, branchCode, companyCode);
        }
    }

    private void creditMemoAmountDue(Integer companyCode, LocalArInvoice arCreditMemo, double TOTAL_LINE,
                                     double TOTAL_TAX, double UNEARNED_INT_AMOUNT, double W_TAX_AMOUNT, double DISCOUNT)
            throws ArINVOverapplicationNotAllowedException {
        // set invoice amount due
        arCreditMemo.setInvAmountDue(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT + UNEARNED_INT_AMOUNT);
        if (EJBCommon.roundIt(arCreditMemo.getInvAmountDue(), this.getGlFcPrecisionUnit(companyCode)) > EJBCommon
                .roundIt(arCreditMemo.getInvAmountDue() - arCreditMemo.getInvAmountPaid(),
                        this.getGlFcPrecisionUnit(companyCode))) {
            Double amountPaidAmountDue = EJBCommon.roundIt(arCreditMemo.getInvAmountDue() - arCreditMemo.getInvAmountPaid(), this.getGlFcPrecisionUnit(companyCode));
            Double amountPaidOnly = EJBCommon.roundIt(arCreditMemo.getInvAmountDue(), this.getGlFcPrecisionUnit(companyCode));
            throw new ArINVOverapplicationNotAllowedException();
        }
    }

    private void receivableJournals(Integer companyCode, Integer branchCode, LocalArInvoice arCreditMemo,
                                    double TOTAL_LINE, double TOTAL_TAX, double TOTAL_SALES_ACCOUNT_CREDIT, double W_TAX_AMOUNT, double DISCOUNT)
            throws GlobalBranchAccountNumberInvalidException {
        // add receivable distribution
        try {

            LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome
                    .findBcstByCstCodeAndBrCode(arCreditMemo.getArCustomer().getCstCode(), branchCode, companyCode);

            // this is for Services (DEBIT)
            this.addArDrEntry(arCreditMemo.getArDrNextLine(), "RECEIVABLE", EJBCommon.FALSE,
                    TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT - TOTAL_SALES_ACCOUNT_CREDIT,
                    adBranchCustomer.getBcstGlCoaReceivableAccount(), arCreditMemo, branchCode, companyCode);

        }
        catch (FinderException ex) {

        }
    }

    private double getWithholdingTax(Integer companyCode, Integer branchCode, LocalArInvoice arCreditMemo,
                                     LocalArWithholdingTaxCode arWithholdingTaxCode, double TOTAL_LINE, LocalAdPreference adPreference)
            throws GlobalBranchAccountNumberInvalidException {
        // add wtax distribution if necessary
        double W_TAX_AMOUNT = 0d;
        if (arWithholdingTaxCode.getWtcRate() != 0
                && adPreference.getPrfArWTaxRealization().equals("INVOICE")) {
            W_TAX_AMOUNT = EJBCommon.roundIt(TOTAL_LINE * (arWithholdingTaxCode.getWtcRate() / 100),
                    this.getGlFcPrecisionUnit(companyCode));
            this.addArDrEntry(arCreditMemo.getArDrNextLine(), "W-TAX", EJBCommon.FALSE, W_TAX_AMOUNT,
                    arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), arCreditMemo, branchCode, companyCode);
        }
        return W_TAX_AMOUNT;
    }

    private double getUnearnedInterestAmount(Integer companyCode, Integer branchCode, LocalArInvoice arCreditMemo,
                                             double TOTAL_LINE, double TOTAL_TAX)
            throws GlobalBranchAccountNumberInvalidException {
        // add un earned interest
        double UNEARNED_INT_AMOUNT = 0d;
        double TOTAL_DOWN_PAYMENT = arCreditMemo.getInvDownPayment();

        if (arCreditMemo.getArCustomer().getCstAutoComputeInterest() == EJBCommon.TRUE
                && arCreditMemo.getArCustomer().getCstMonthlyInterestRate() > 0
                && arCreditMemo.getAdPaymentTerm().getPytEnableInterest() == EJBCommon.TRUE
                && arCreditMemo.getInvAmountUnearnedInterest() > 0) {

            try {
                LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(
                        arCreditMemo.getArCustomer().getCstCode(), branchCode, companyCode);

                UNEARNED_INT_AMOUNT = EJBCommon.roundIt(
                        (TOTAL_LINE + TOTAL_TAX - TOTAL_DOWN_PAYMENT)
                                * arCreditMemo.getAdPaymentTerm().getAdPaymentSchedules().size()
                                * (arCreditMemo.getArCustomer().getCstMonthlyInterestRate() / 100),
                        this.getGlFcPrecisionUnit(companyCode));

                this.addArDrIliEntry(arCreditMemo.getArDrNextLine(), "UNINTEREST", EJBCommon.TRUE,
                        UNEARNED_INT_AMOUNT, adBranchCustomer.getBcstGlCoaUnEarnedInterestAccount(),
                        arCreditMemo, branchCode, companyCode);

                this.addArDrIliEntry(arCreditMemo.getArDrNextLine(), "RECEIVABLE INTEREST", EJBCommon.FALSE,
                        UNEARNED_INT_AMOUNT, adBranchCustomer.getBcstGlCoaReceivableAccount(), arCreditMemo,
                        branchCode, companyCode);

            }
            catch (FinderException ex) {

            }
        }
        return UNEARNED_INT_AMOUNT;
    }

    private void taxJournals(Integer companyCode, Integer branchCode, LocalArInvoice arCreditMemo,
                             LocalArTaxCode arTaxCode, double TOTAL_TAX, LocalAdBranch centralWarehouseBranchCode)
            throws GlobalBranchAccountNumberInvalidException, FinderException {
        // add tax distribution if necessary
        if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {
            if (arTaxCode.getTcInterimAccount() == null) {
                // add branch tax code
                LocalAdBranchArTaxCode adBranchTaxCode = null;
                try {
                    adBranchTaxCode = adBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(arCreditMemo.getArTaxCode().getTcCode(), branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                this.addArDrEntry(arCreditMemo.getArDrNextLine(), "TAX", EJBCommon.TRUE, TOTAL_TAX,
                        (adBranchTaxCode != null) ? adBranchTaxCode.getBtcGlCoaTaxCode() : arTaxCode.getGlChartOfAccount().getCoaCode(),
                        arCreditMemo, (adBranchTaxCode != null) ? branchCode : centralWarehouseBranchCode.getBrCode(), companyCode);
            } else {
                this.addArDrEntry(arCreditMemo.getArDrNextLine(), "DEFERRED TAX", EJBCommon.TRUE, TOTAL_TAX,
                        arTaxCode.getTcInterimAccount(), arCreditMemo, branchCode, companyCode);
            }
        }
    }


    private void costOfGoodsSoldJournals(Integer companyCode, Integer branchCode, LocalArInvoice arCreditMemo, LocalAdPreference adPreference, LocalArInvoiceLineItem arInvoiceLineItem, double COST, double QTY_SLD, LocalAdBranchItemLocation adBranchItemLocation) throws GlobalBranchAccountNumberInvalidException {

        if (adPreference.getPrfArAutoComputeCogs() == EJBCommon.TRUE && arInvoiceLineItem
                .getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

            if (adBranchItemLocation != null) {
                this.addArDrEntry(arCreditMemo.getArDrNextLine(), "COGS", EJBCommon.FALSE, COST * QTY_SLD,
                        adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arCreditMemo, branchCode,
                        companyCode);
                this.addArDrEntry(arCreditMemo.getArDrNextLine(), "INVENTORY", EJBCommon.TRUE,
                        COST * QTY_SLD, adBranchItemLocation.getBilCoaGlInventoryAccount(), arCreditMemo,
                        branchCode, companyCode);
            } else {
                this.addArDrEntry(arCreditMemo.getArDrNextLine(), "COGS", EJBCommon.FALSE, COST * QTY_SLD,
                        arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arCreditMemo,
                        branchCode, companyCode);
                this.addArDrEntry(arCreditMemo.getArDrNextLine(), "INVENTORY", EJBCommon.TRUE,
                        COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(),
                        arCreditMemo, branchCode, companyCode);
            }
        }
    }

    private double getItemCosting(Integer companyCode, Integer branchCode, LocalArInvoice arCreditMemo, LocalInvItemLocation invItemLocation, LocalArInvoiceLineItem arInvoiceLineItem) {
        // add cost of sales distribution and inventory
        double COST = 0d;
        try {
            LocalInvCosting invCosting = invCostingHome
                    .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                            arCreditMemo.getInvDate(), invItemLocation.getInvItem().getIiName(),
                            invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
            if (invCosting.getCstRemainingQuantity() <= 0) {
                COST = Math.abs(invItemLocation.getInvItem().getIiUnitCost());
            } else {
                COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
            }
        }
        catch (FinderException ex) {
            COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
        }
        return COST;
    }

    private void isPriorDateAllowed(Integer companyCode, Integer branchCode, LocalArInvoice arCreditMemo, LocalAdPreference adPreference, LocalInvItemLocation invItemLocation) throws FinderException, GlobalInventoryDateException {
        // start date validation
        if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
            Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                    arCreditMemo.getInvDate(), invItemLocation.getInvItem().getIiName(),
                    invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
            if (!invNegTxnCosting.isEmpty()) {
                throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
            }
        }
    }

    private LocalInvItemLocation getItemLocation(Integer companyCode, ArModInvoiceLineItemDetails mIliDetails) throws GlobalInvItemLocationNotFoundException {

        LocalInvItemLocation invItemLocation;
        try {
            invItemLocation = invItemLocationHome.findByLocNameAndIiName(mIliDetails.getIliLocName(),
                    mIliDetails.getIliIiName(), companyCode);
        }
        catch (FinderException ex) {
            throw new GlobalInvItemLocationNotFoundException(String.valueOf(mIliDetails.getIliLine()));
        }
        return invItemLocation;
    }

    /**
     * This method create a credit memo object.
     */
    @Override
    public OfsApiResponse createCreditMemo(InvoiceItemRequest invoiceItemRequest) {

        Debug.print("ArCreditMemoEntryApiControllerBean createCreditMemo");

        OfsApiResponse apiResponse = new OfsApiResponse();

        LocalAdCompany adCompany = null;
        LocalAdBranch adBranch = null;
        LocalAdUser adUser = null;
        LocalArCustomer arCustomer = null;
        LocalArInvoice arInvoice = null;
        String defaultDateFormat = ConfigurationClass.DEFAULT_DATE_FORMAT;
        String defaultLocation = "DEFAULT";

        try {

            ArInvoiceDetails invoiceDetails = new ArInvoiceDetails();

            // Company Code
            try {
                if (invoiceItemRequest.getCompanyCode() == null || invoiceItemRequest.getCompanyCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return apiResponse;
                }
                adCompany = adCompanyHome.findByCmpShortName(invoiceItemRequest.getCompanyCode());
                if (adCompany != null) {
                    invoiceDetails.setInvAdCompany(adCompany.getCmpCode());
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return apiResponse;
            }

            // Branch Code
            try {
                if (invoiceItemRequest.getBranchCode() == null || invoiceItemRequest.getBranchCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return apiResponse;
                }
                adBranch = adBranchHome.findByBrBranchCode(invoiceItemRequest.getBranchCode(), adCompany.getCmpCode());
                if (adBranch != null) {
                    invoiceDetails.setInvAdBranch(adBranch.getBrCode());
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, invoiceItemRequest.getBranchCode()));
                return apiResponse;
            }

            // User
            try {
                if (invoiceItemRequest.getUsername() == null || invoiceItemRequest.getUsername().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_042);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_042_MSG);
                    return apiResponse;
                }
                adUser = adUserHome.findByUsrName(invoiceItemRequest.getUsername(), adCompany.getCmpCode());
                if (adUser != null) {
                    invoiceDetails.setInvCreatedBy(adUser.getUsrName());
                    invoiceDetails.setInvLastModifiedBy(adUser.getUsrName());
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_002);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_002_MSG);
                return apiResponse;
            }

            // Customer Code from TT
            // WARNING: Do not adopt this code because Tecnotree has issues at their end
            try {
                if (invoiceItemRequest.getCustomerCode() == null || invoiceItemRequest.getCustomerCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return apiResponse;
                }
                arCustomer = arCustomerHome.findByCstRefCustomerCode(invoiceItemRequest.getCustomerCode(),
                        adBranch.getBrCode(), adCompany.getCmpCode());
            }
            catch (FinderException ex) {
                try {
                    arCustomer = arCustomerHome.findByCstCustomerCode(invoiceItemRequest.getCustomerCode(),
                            adBranch.getBrCode(), adCompany.getCmpCode());
                }
                catch (FinderException e) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_009);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_009_MSG);
                    return apiResponse;
                }
            }

            String systemGenDesc = "DESC-TT";
            if (invoiceItemRequest.getDescription() != null) {
                systemGenDesc = invoiceItemRequest.getDescription();
            }

            LocalDateTime ldt = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(defaultDateFormat);
            String currentDate = ldt.format(dtf);
            Date creditMemoDate = EJBCommon.convertStringToSQLDate(currentDate, defaultDateFormat);

            // Invoice Data to be adjusted if exists in Omega ERP
            try {
                arInvoice = arInvoiceHome.findByInvoiceReferenceNumber(invoiceItemRequest.getAdjustmentAccount());
                invoiceDetails.setInvType(arInvoice.getInvType());
                invoiceDetails.setInvDate(creditMemoDate);
                invoiceDetails.setInvNumber(arInvoice.getInvNumber());
                invoiceDetails.setInvCmReferenceNumber(invoiceItemRequest.getReferenceNumber()); // Reference from TT system
                invoiceDetails.setInvDescription(systemGenDesc);
                invoiceDetails.setArTaxCode(arInvoice.getArTaxCode());
                invoiceDetails.setArWithholdingTaxCode(arInvoice.getArWithholdingTaxCode());
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_039);
                apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_039_MSG, invoiceItemRequest.getAdjustmentAccount()));
                return apiResponse;
            }

            // [R] - Return
            // [C] - Cancellation
            // [E] - Return due to exchange
            boolean validateTransactionType = false;
            switch (invoiceItemRequest.getTransactionType()) {
                case "R":
                    validateTransactionType = true;
                    break;
                case "C":
                    validateTransactionType = true;
                    break;
                case "E":
                    validateTransactionType = true;
                    break;
            }

            if (!validateTransactionType) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_038);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_038_MSG);
                return apiResponse;
            }

            double totalAmount = 0;
            ArrayList lineItems = new ArrayList();
            int lineItem = 1;

            for (LineItemRequest itemRequest : invoiceItemRequest.getInvoiceItems()) {

                ArModInvoiceLineItemDetails mdetails = new ArModInvoiceLineItemDetails();
                LocalInvItem invItem = null;

                // itemName
                try {
                    invItem = invItemHome.findByIiName(itemRequest.getItemName(), adCompany.getCmpCode());
                    mdetails.setIliIiName(invItem.getIiName());
                    mdetails.setIliIiDescription(invItem.getIiDescription());
                }
                catch (FinderException ex) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_010);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_010_MSG + lineItem);
                    return apiResponse;
                }

                // itemLocation
                mdetails.setIliLocName(defaultLocation);

                // confirm if item is in location
                try {
                    invItemLocationHome.findByIiNameAndLocNameAndAdBranch(itemRequest.getItemName(), defaultLocation,
                            adBranch.getBrCode(), adCompany.getCmpCode());
                }
                catch (FinderException ex) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_011);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_011_MSG + lineItem);
                    return apiResponse;
                }

                mdetails.setIliTax(EJBCommon.TRUE);
                mdetails.setIliUomName(invItem.getInvUnitOfMeasure().getUomName());
                mdetails.setIliQuantity(itemRequest.getItemQuantity());

                // Calculate the Invoice Amount based on the item invoiceDetails saved in OFS
                mdetails.setIliUnitPrice(itemRequest.getItemSalesPrice());
                mdetails.setIliAmount(itemRequest.getItemSalesPrice() * itemRequest.getItemQuantity());

                // Additional requirements from TT
                mdetails.setIliDiscount1(itemRequest.getNormalDiscAmt()); // This is required from TT by currently ignore by Omega ERP
                mdetails.setIliTaxAmount(itemRequest.getTaxAmt()); // This is required from TT by currently ignore by Omega ERP
                mdetails.setIliTotalDiscount(itemRequest.getTransDiscAmt());  // This is required from TT by currently ignore by Omega ERP

                // Update Item Sales Price and Percent Markup
                if (invItem.getIiUnitCost() > 0) {
                    updateSalesPrice(itemRequest, invItem);
                } else {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_031);
                    apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_031_MSG, lineItem));
                    return apiResponse;
                }
                totalAmount += mdetails.getIliAmount();
                lineItems.add(mdetails);
            }

            Integer invoiceCode = 0;
            invoiceCode = this.saveCreditMemo(invoiceDetails, arCustomer.getCstCustomerCode(), lineItems);
            arInvoice = arInvoiceHome.findByPrimaryKey(invoiceCode);

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            apiResponse.setDocumentNumber(arInvoice.getInvNumber());
            apiResponse.setStatus("Updated the existing invoice successfully.");

        }
        catch (Exception ex) {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return apiResponse;
        }
        return apiResponse;
    }

    private void addArDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE,
                              LocalArInvoice arInvoice, Integer branchCode, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArCreditMemoEntryApiControllerBean addArDrEntry");

        try {

            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);

            // create distribution record
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT,
                    EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE,
                    EJBCommon.FALSE, companyCode);

            arInvoice.addArDistributionRecord(arDistributionRecord);
            glChartOfAccount.addArDistributionRecord(arDistributionRecord);

        }
        catch (FinderException ex) {
            throw new GlobalBranchAccountNumberInvalidException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("ArCreditMemoEntryApiControllerBean getGlFcPrecisionUnit");

        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            return adCompany.getGlFunctionalCurrency().getFcPrecision();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrIliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE,
                                 LocalArInvoice arInvoice, Integer branchCode, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArCreditMemoEntryApiControllerBean addArDrIliEntry");

        try {

            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode,
                    companyCode);

            // create distribution record
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT,
                    EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE,
                    EJBCommon.FALSE, companyCode);

            arInvoice.addArDistributionRecord(arDistributionRecord);
            glChartOfAccount.addArDistributionRecord(arDistributionRecord);

        }
        catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException(ex.getMessage());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalArInvoiceLineItem addArIliEntry(ArModInvoiceLineItemDetails mdetails, LocalArInvoice arInvoice,
                                                 LocalInvItemLocation itemLocation, Integer companyCode) {

        Debug.print("ArCreditMemoEntryApiControllerBean addArIliEntry");

        try {

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode);
            double itemAmount = 0d;
            double itemTaxAmount = 0d;

            LocalArTaxCode arTaxCode = arInvoice.getArTaxCode();

            // calculate net amount
            itemAmount = EJBCommon.calculateNetAmount(mdetails.getIliAmount(), mdetails.getIliTax(), arTaxCode.getTcRate(), arTaxCode.getTcType(), precisionUnit);

            // calculate tax
            itemTaxAmount = EJBCommon.calculateTaxAmount(mdetails.getIliAmount(), mdetails.getIliTax(), arTaxCode.getTcRate(), arTaxCode.getTcType(), itemAmount, precisionUnit);

            LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome
                    .IliLine(mdetails.getIliLine())
                    .IliQuantity(mdetails.getIliQuantity())
                    .IliUnitPrice(mdetails.getIliUnitPrice())
                    .IliAmount(itemAmount)
                    .IliTaxAmount(itemTaxAmount)
                    .IliDiscount1(mdetails.getIliDiscount1())
                    .IliDiscount2(mdetails.getIliDiscount2())
                    .IliDiscount3(mdetails.getIliDiscount3())
                    .IliDiscount4(mdetails.getIliDiscount4())
                    .IliTotalDiscount(mdetails.getIliTotalDiscount()) // Normal Discount Amount
                    .IliTax(mdetails.getIliTax())
                    .IliAdCompany(companyCode)
                    .buildInvoiceLineItem();
            arInvoiceLineItem.setArInvoice(arInvoice);
            arInvoiceLineItem.setInvItemLocation(itemLocation);

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getIliUomName(),
                    companyCode);
            arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

            return arInvoiceLineItem;

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem,
                                                      double QTY_SLD, Integer companyCode) {

        Debug.print("ArCreditMemoEntryApiControllerBean convertByUomFromAndItemAndQuantity");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(),
                            companyCode);

            return EJBCommon.roundIt(
                    QTY_SLD * invDefaultUomConversion.getUmcConversionFactor()
                            / invUnitOfMeasureConversion.getUmcConversionFactor(),
                    adPreference.getPrfInvQuantityPrecisionUnit());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeArInvCreditMemoPost(Integer INV_CODE, String USR_NM, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException,
            AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArCreditMemoEntryApiControllerBean executeArInvCreditMemoPost");

        LocalArInvoice arCreditMemo = null;
        LocalArInvoice arCreditedInvoice = null;

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // validate if invoice/credit memo is already deleted
            try {
                arCreditMemo = arInvoiceHome.findByPrimaryKey(INV_CODE);
            }
            catch (FinderException ex) {
                throw new GlobalRecordAlreadyDeletedException();
            }

            // post credit memo
            arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(
                    arCreditMemo.getInvCmInvoiceNumber(), EJBCommon.FALSE, branchCode, companyCode);

            // decrease customer balance
            double INV_AMNT = this.convertForeignToFunctionalCurrency(
                    arCreditedInvoice.getGlFunctionalCurrency().getFcCode(),
                    arCreditedInvoice.getGlFunctionalCurrency().getFcName(),
                    arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(),
                    arCreditMemo.getInvAmountDue(), companyCode);

            this.updateCustomerBalance(arCreditMemo.getInvDate(), -INV_AMNT, arCreditMemo.getArCustomer(), companyCode);

            // decrease invoice and ips amounts and release lock
            double CREDIT_PERCENT = EJBCommon.roundIt(arCreditMemo.getInvAmountDue() / arCreditedInvoice.getInvAmountDue(), (short) 6);
            arCreditedInvoice.setInvAmountPaid(arCreditedInvoice.getInvAmountPaid() + arCreditMemo.getInvAmountDue());

            Collection arInvoiceLineItems = arCreditMemo.getArInvoiceLineItems();
            if (arInvoiceLineItems != null && !arInvoiceLineItems.isEmpty()) {
                for (Object invoiceLineItem : arInvoiceLineItems) {
                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;
                    String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                    String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();

                    double QTY_SLD = this.convertByUomFromAndItemAndQuantity(
                            arInvoiceLineItem.getInvUnitOfMeasure(),
                            arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(),
                            companyCode);

                    LocalInvCosting invCosting = null;
                    try {
                        invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                arCreditMemo.getInvDate(), II_NM, LOC_NM, branchCode, companyCode);
                    }
                    catch (FinderException ex) {
                    }

                    double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                    if (invCosting == null) {
                        this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), -QTY_SLD, -COST * QTY_SLD,
                                QTY_SLD, COST * QTY_SLD, 0d, null, branchCode, companyCode);
                    } else {

                        if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals("Average")) {
                            double avgCost = invCosting.getCstRemainingQuantity() <= 0 ? COST
                                    : Math.abs(invCosting.getCstRemainingValue()
                                    / invCosting.getCstRemainingQuantity());

                            this.postToInv(arInvoiceLineItem, arCreditMemo.getInvDate(), -QTY_SLD,
                                    -avgCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD,
                                    invCosting.getCstRemainingValue() + (avgCost * QTY_SLD), 0d, null, branchCode,
                                    companyCode);
                        }
                    }
                }
            }

            // set invoice post status
            arCreditMemo.setInvPosted(EJBCommon.TRUE);
            arCreditMemo.setInvPostedBy(USR_NM);
            arCreditMemo.setInvDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary
            // validate if date has no period and period is closed
            LocalGlSetOfBook glJournalSetOfBook = null;
            try {
                glJournalSetOfBook = glSetOfBookHome.findByDate(arCreditMemo.getInvDate(), companyCode);
            }
            catch (FinderException ex) {
                throw new GlJREffectiveDateNoPeriodExistException();
            }

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome
                    .findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(),
                            arCreditMemo.getInvDate(), companyCode);

            if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C'
                    || glAccountingCalendarValue.getAcvStatus() == 'P') {

                throw new GlJREffectiveDatePeriodClosedException();
            }

            // check if invoice is balance if not check suspense posting
            LocalGlJournalLine glOffsetJournalLine = null;
            Collection arDistributionRecords = arDistributionRecordHome
                    .findImportableDrByInvCode(arCreditMemo.getInvCode(), companyCode);
            Iterator j = arDistributionRecords.iterator();
            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

            while (j.hasNext()) {
                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();
                if (arDistributionRecord.getDrClass().equals("COGS") || arDistributionRecord.getDrClass().equals("INVENTORY")) {
                    continue;
                }

                double DR_AMNT = 0d;

                if (arCreditMemo.getInvCreditMemo() == EJBCommon.FALSE) {
                    DR_AMNT = this.convertForeignToFunctionalCurrency(
                            arCreditMemo.getGlFunctionalCurrency().getFcCode(),
                            arCreditMemo.getGlFunctionalCurrency().getFcName(), arCreditMemo.getInvConversionDate(),
                            arCreditMemo.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);

                } else {
                    DR_AMNT = this.convertForeignToFunctionalCurrency(
                            arCreditedInvoice.getGlFunctionalCurrency().getFcCode(),
                            arCreditedInvoice.getGlFunctionalCurrency().getFcName(),
                            arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(),
                            arDistributionRecord.getDrAmount(), companyCode);
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
                    glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS RECEIVABLES",
                            arCreditMemo.getInvCreditMemo() == EJBCommon.FALSE ? "SALES INVOICES" : "CREDIT MEMOS",
                            companyCode);
                }
                catch (FinderException ex) {
                    throw new GlobalJournalNotBalanceException();
                }

                if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {
                    glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1),
                            EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);
                } else {
                    glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1),
                            EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                }

                LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                glChartOfAccount.addGlJournalLine(glOffsetJournalLine);

            } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE
                    && TOTAL_DEBIT != TOTAL_CREDIT) {

                throw new GlobalJournalNotBalanceException();
            }

            // create journal entry
            String journalDesc = arCreditMemo.getInvVoid() == (byte) 1 ? " VOID" : " POST";

            LocalGlJournal glJournal = glJournalHome.create(arCreditMemo.getInvCmInvoiceNumber(),
                    arCreditMemo.getInvDescription() + journalDesc, arCreditMemo.getInvDate(), 0.0d, null,
                    arCreditMemo.getInvNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE,
                    USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM,
                    EJBCommon.getGcCurrentDateWoTime().getTime(), arCreditMemo.getArCustomer().getCstTin(),
                    arCreditMemo.getArCustomer().getCstName(), EJBCommon.FALSE, null, branchCode, companyCode);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS RECEIVABLES", companyCode);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName(
                    arCreditMemo.getInvCreditMemo() == EJBCommon.FALSE ? "SALES INVOICES" : "CREDIT MEMOS", companyCode);
            glJournal.setGlJournalCategory(glJournalCategory);

            // create journal lines
            j = arDistributionRecords.iterator();
            while (j.hasNext()) {
                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();
                double DR_AMNT = 0d;
                if (arCreditMemo.getInvCreditMemo() == EJBCommon.FALSE) {
                    DR_AMNT = this.convertForeignToFunctionalCurrency(
                            arCreditMemo.getGlFunctionalCurrency().getFcCode(),
                            arCreditMemo.getGlFunctionalCurrency().getFcName(), arCreditMemo.getInvConversionDate(),
                            arCreditMemo.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);
                } else {
                    DR_AMNT = this.convertForeignToFunctionalCurrency(
                            arCreditedInvoice.getGlFunctionalCurrency().getFcCode(),
                            arCreditedInvoice.getGlFunctionalCurrency().getFcName(),
                            arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(),
                            arDistributionRecord.getDrAmount(), companyCode);
                }

                LocalGlJournalLine glJournalLine = glJournalLineHome.create(arDistributionRecord.getDrLine(),
                        arDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);
                arDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);
                glJournal.addGlJournalLine(glJournalLine);
                arDistributionRecord.setDrImported(EJBCommon.TRUE);
            }

            if (glOffsetJournalLine != null) {
                glJournal.addGlJournalLine(glOffsetJournalLine);
            }

            // post journal to gl
            Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
            for (Object journalLine : glJournalLines) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                // post current to current acv
                this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true,
                        glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                // post to subsequent acvs (propagate)
                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome
                        .findSubsequentAcvByAcCodeAndAcvPeriodNumber(
                                glJournalSetOfBook.getGlAccountingCalendar().getAcCode(),
                                glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {
                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;
                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false,
                            glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                }

                // post to subsequent years if necessary
                Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(
                        glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                    adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome
                            .findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), companyCode);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {
                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;
                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)
                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(
                                glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);
                        for (Object accountingCalendarValue : glAccountingCalendarValues) {
                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY")
                                    || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {
                                this.postToGl(glSubsequentAccountingCalendarValue,
                                        glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(),
                                        glJournalLine.getJlAmount(), companyCode);

                            } else {
                                // revenue & expense
                                this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false,
                                        glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                            }
                        }

                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                            break;
                        }
                    }
                }
            }

        }
        catch (GlJREffectiveDateNoPeriodExistException | AdPRFCoaGlVarianceAccountNotFoundException |
               GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
               GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE,
                                                      double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("ArCreditMemoEntryApiControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision
        try {
            adCompany = adCompanyHome.findByPrimaryKey(companyCode);
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary
        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {
            AMOUNT = AMOUNT / CONVERSION_RATE;
        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private void updateCustomerBalance(Date INV_DT, double INV_AMNT, LocalArCustomer arCustomer, Integer companyCode) {

        Debug.print("ArCreditMemoEntryApiControllerBean updateCustomerBalance");

        try {

            // find customer balance before or equal invoice date
            Collection arCustomerBalances = arCustomerBalanceHome.findByBeforeOrEqualInvDateAndCstCustomerCode(INV_DT,
                    arCustomer.getCstCustomerCode(), companyCode);

            if (!arCustomerBalances.isEmpty()) {
                // get last invoice
                ArrayList arCustomerBalanceList = new ArrayList(arCustomerBalances);
                LocalArCustomerBalance arCustomerBalance = (LocalArCustomerBalance) arCustomerBalanceList
                        .get(arCustomerBalanceList.size() - 1);
                if (arCustomerBalance.getCbDate().before(INV_DT)) {
                    // create new balance
                    LocalArCustomerBalance apNewCustomerBalance = arCustomerBalanceHome.create(INV_DT,
                            arCustomerBalance.getCbBalance() + INV_AMNT, companyCode);
                    arCustomer.addArCustomerBalance(apNewCustomerBalance);
                } else { // equals to invoice date

                    arCustomerBalance.setCbBalance(arCustomerBalance.getCbBalance() + INV_AMNT);
                }
            } else {
                // create new balance
                LocalArCustomerBalance apNewCustomerBalance = arCustomerBalanceHome.create(INV_DT, INV_AMNT, companyCode);
                arCustomer.addArCustomerBalance(apNewCustomerBalance);
            }

            // propagate to subsequent balances if necessary
            arCustomerBalances = arCustomerBalanceHome.findByAfterInvDateAndCstCustomerCode(INV_DT,
                    arCustomer.getCstCustomerCode(), companyCode);

            for (Object customerBalance : arCustomerBalances) {
                LocalArCustomerBalance arCustomerBalance = (LocalArCustomerBalance) customerBalance;
                arCustomerBalance.setCbBalance(arCustomerBalance.getCbBalance() + INV_AMNT);
            }
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInv(LocalArInvoiceLineItem arInvoiceLineItem, Date CST_DT, double CST_QTY_SLD,
                           double CST_CST_OF_SLS, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM,
                           Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArCreditMemoEntryApiControllerBean postToInv");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_SLD = EJBCommon.roundIt(CST_QTY_SLD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_CST_OF_SLS = EJBCommon.roundIt(CST_CST_OF_SLS, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (CST_QTY_SLD > 0) {

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - CST_QTY_SLD);
            }

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome
                        .getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(),
                                invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(),
                                branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            // void subsequent cost variance adjustments
            Collection invAdjustmentLines = invAdjustmentLineHome
                    .findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT,
                            invItemLocation.getIlCode(), branchCode, companyCode);

            for (Object adjustmentLine : invAdjustmentLines) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;
                this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
            }

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            try {
                LocalInvCosting prevCst = invCostingHome
                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(
                                CST_DT, invItemLocation.getIlCode(), branchCode, companyCode);

                prevExpiryDates = prevCst.getCstExpiryDate();
                qtyPrpgt = prevCst.getCstRemainingQuantity();

                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

            }
            catch (Exception ex) {

            }

            Debug.print("INV COSTING CREATE CST_RMNNG_QTY=" + CST_RMNNG_QTY);
            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d,
                    0d, 0d, CST_QTY_SLD, CST_CST_OF_SLS, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, branchCode, companyCode);
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setArInvoiceLineItem(arInvoiceLineItem);
            invCosting.setInvItemLocation(invItemLocation);
            // Get Latest Expiry Dates

            invCosting.setCstQuantity(CST_QTY_SLD * -1);
            invCosting.setCstCost(CST_CST_OF_SLS * -1);

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL,
                        "ARCR" + arInvoiceLineItem.getArReceipt().getRctNumber(),
                        arInvoiceLineItem.getArReceipt().getRctDescription(),
                        arInvoiceLineItem.getArReceipt().getRctDate(), USR_NM, branchCode, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT,
                    invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode,
                    companyCode);

            Iterator i = invCostings.iterator();
            String miscList = "";

            // regenerate cost variance
            this.regenerateCostVariance(invCostings, invCosting, branchCode, companyCode);

        }
        catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {
            throw ex;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue,
                          LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT,
                          Integer companyCode) {

        Debug.print("ArCreditMemoEntryApiControllerBean postToGl");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(
                    glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode);

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
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) {

        Debug.print("ArCreditMemoEntryApiControllerBean voidInvAdjustment");

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
                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), invDistributionRecord.getDrClass(),
                        invDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE,
                        invDistributionRecord.getDrAmount(), EJBCommon.TRUE,
                        invDistributionRecord.getInvChartOfAccount().getCoaCode(), invAdjustment, branchCode, companyCode);
            }

            Collection invAdjustmentLines = invAdjustment.getInvAdjustmentLines();
            i = invAdjustmentLines.iterator();
            list.clear();

            while (i.hasNext()) {
                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                list.add(invAdjustmentLine);
            }

            i = list.iterator();

            while (i.hasNext()) {
                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                this.addInvAlEntry(invAdjustmentLine.getInvItemLocation(), invAdjustment,
                        (invAdjustmentLine.getAlUnitCost()) * -1, EJBCommon.TRUE, companyCode);
            }

            invAdjustment.setAdjVoid(EJBCommon.TRUE);

            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), branchCode,
                    companyCode);

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalInvAdjustmentLine addInvAlEntry(LocalInvItemLocation invItemLocation, LocalInvAdjustment invAdjustment,
                                                 double CST_VRNC_VL, byte AL_VD, Integer companyCode) {

        Debug.print("ArCreditMemoEntryApiControllerBean addInvAlEntry");

        try {

            // create dr entry
            LocalInvAdjustmentLine invAdjustmentLine = null;
            invAdjustmentLine = invAdjustmentLineHome.create(CST_VRNC_VL, null, null, 0, 0, AL_VD, companyCode);

            // map adjustment, unit of measure, item location
            invAdjustment.addInvAdjustmentLine(invAdjustmentLine);
            invItemLocation.getInvItem().getInvUnitOfMeasure().addInvAdjustmentLine(invAdjustmentLine);
            invItemLocation.addInvAdjustmentLine(invAdjustmentLine);

            return invAdjustmentLine;

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void generateCostVariance(LocalInvItemLocation invItemLocation, double CST_VRNC_VL, String ADJ_RFRNC_NMBR,
                                      String ADJ_DSCRPTN, Date ADJ_DT, String USR_NM, Integer branchCode, Integer companyCode)
            throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void regenerateCostVariance(Collection invCostings, LocalInvCosting invCosting, Integer branchCode,
                                        Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL,
                               Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArCreditMemoEntryApiControllerBean addInvDrEntry");

        try {
            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            // validate coa
            LocalGlChartOfAccount glChartOfAccount = null;
            try {
                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, branchCode, companyCode);
            }
            catch (FinderException ex) {
                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record
            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT,
                    EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_RVRSL,
                    EJBCommon.FALSE, companyCode);

            invAdjustment.addInvDistributionRecord(invDistributionRecord);
            glChartOfAccount.addInvDistributionRecord(invDistributionRecord);

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {
            throw new GlobalBranchAccountNumberInvalidException();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ArCreditMemoEntryApiControllerBean executeInvAdjPost");

        try {

            // validate if adjustment is already deleted
            LocalInvAdjustment invAdjustment = null;
            try {
                invAdjustment = invAdjustmentHome.findByPrimaryKey(ADJ_CODE);
            }
            catch (FinderException ex) {
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
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE,
                        invAdjustment.getAdjCode(), companyCode);
            } else {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.TRUE,
                        invAdjustment.getAdjCode(), companyCode);
            }

            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {
                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                LocalInvCosting invCosting = invCostingHome
                        .getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                invAdjustment.getAdjDate(),
                                invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(),
                                invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), branchCode,
                                companyCode);

                this.postInvAdjustmentToInventory(invAdjustmentLine, invAdjustment.getAdjDate(), 0,
                        invAdjustmentLine.getAlUnitCost(), invCosting.getCstRemainingQuantity(),
                        invCosting.getCstRemainingValue() + invAdjustmentLine.getAlUnitCost(), branchCode, companyCode);
            }

            // post to gl if necessary
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate if date has no period and period is closed
            LocalGlSetOfBook glJournalSetOfBook = null;
            try {
                glJournalSetOfBook = glSetOfBookHome.findByDate(invAdjustment.getAdjDate(), companyCode);
            }
            catch (FinderException ex) {
                throw new GlJREffectiveDateNoPeriodExistException();
            }

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome
                    .findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(),
                            invAdjustment.getAdjDate(), companyCode);

            if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C'
                    || glAccountingCalendarValue.getAcvStatus() == 'P') {
                throw new GlJREffectiveDatePeriodClosedException();
            }

            // check if invoice is balance if not check suspense posting
            LocalGlJournalLine glOffsetJournalLine = null;
            Collection invDistributionRecords = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                invDistributionRecords = invDistributionRecordHome
                        .findImportableDrByDrReversedAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);
            } else {
                invDistributionRecords = invDistributionRecordHome
                        .findImportableDrByDrReversedAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
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
                    glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("INVENTORY",
                            "INVENTORY ADJUSTMENTS", companyCode);
                }
                catch (FinderException ex) {
                    throw new GlobalJournalNotBalanceException();
                }

                if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {
                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1),
                            EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);
                } else {
                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1),
                            EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
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
                glJournalBatch = glJournalBatchHome.findByJbName(
                        "JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", branchCode,
                        companyCode);
            }
            catch (FinderException ex) {

            }

            if (glJournalBatch == null) {
                glJournalBatch = glJournalBatchHome.create(
                        "JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", "JOURNAL IMPORT",
                        "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);
            }

            // create journal entry
            LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(),
                    invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d, null,
                    invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE,
                    USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM,
                    EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode,
                    companyCode);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("INVENTORY", companyCode);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome
                    .findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("INVENTORY ADJUSTMENTS",
                    companyCode);
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

                LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(),
                        invDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);
                invDistributionRecord.getInvChartOfAccount().addGlJournalLine(glJournalLine);
                glJournal.addGlJournalLine(glJournalLine);
                invDistributionRecord.setDrImported(EJBCommon.TRUE);
            }

            if (glOffsetJournalLine != null) {
                glJournal.addGlJournalLine(glOffsetJournalLine);
            }

            // post journal to gl
            Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
            i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                // post current to current acv
                this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true,
                        glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                // post to subsequent acvs (propagate)
                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome
                        .findSubsequentAcvByAcCodeAndAcvPeriodNumber(
                                glJournalSetOfBook.getGlAccountingCalendar().getAcCode(),
                                glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {
                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false,
                            glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                }

                // post to subsequent years if necessary
                Collection glSubsequentSetOfBooks = glSetOfBookHome
                        .findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                    adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome
                            .findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), branchCode,
                                    companyCode);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {
                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;
                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)
                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome
                                .findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                        for (Object accountingCalendarValue : glAccountingCalendarValues) {
                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY")
                                    || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {
                                this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(),
                                        false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                            } else { // revenue & expense
                                this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false,
                                        glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                            }
                        }
                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                            break;
                        }
                    }
                }
            }
            invAdjustment.setAdjPosted(EJBCommon.TRUE);

        }
        catch (GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyPostedException |
               GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
               GlJREffectiveDatePeriodClosedException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT,
                                              double CST_ADJST_QTY, double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer branchCode,
                                              Integer companyCode) {

        Debug.print("ArCreditMemoEntryApiControllerBean postInvAdjustmentToInventory");

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
                invItemLocation
                        .setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(CST_ADJST_QTY));
            }

            // create costing
            try {

                // generate line number
                LocalInvCosting invCurrentCosting = invCostingHome
                        .getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(),
                                invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(),
                                branchCode, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {
                CST_LN_NMBR = 1;
            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d,
                    CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d,
                    CST_ADJST_QTY > 0 ? CST_ADJST_QTY : 0, branchCode, companyCode);
            invItemLocation.addInvCosting(invCosting);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);

            invCosting.setCstQuantity(CST_ADJST_QTY);
            invCosting.setCstCost(CST_ADJST_CST);

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT,
                    invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode,
                    companyCode);

            for (Object costing : invCostings) {
                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;
                invPropagatedCosting
                        .setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ADJST_QTY);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ADJST_CST);
            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void updateSalesPrice(LineItemRequest itemRequest, LocalInvItem invItem) {

        Debug.print("ArCreditMemoEntryApiControllerBean updateSalesPrice");
        double percentMarkup = ((itemRequest.getItemSalesPrice() - invItem.getIiUnitCost()) / invItem.getIiUnitCost())
                * 100;
        invItem.setIiSalesPrice(itemRequest.getItemSalesPrice());
        invItem.setIiPercentMarkup(percentMarkup);
        invItemHome.updateItem(invItem);
    }

    private void generateDocumentNumber(ArInvoiceDetails invoiceDetails, Integer companyCode, Integer branchCode, String documentType) {

        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

        try {
            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode);
            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(
                    adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);
        }
        catch (FinderException ex) {
        }

        if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' &&
                (invoiceDetails.getInvCmInvoiceNumber() == null || invoiceDetails.getInvCmInvoiceNumber().trim().length() == 0)) {
            while (true) {
                if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {
                    try {
                        arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(
                                adDocumentSequenceAssignment.getDsaNextSequence(), EJBCommon.FALSE, branchCode, companyCode);
                        invoiceDetails.setInvCmInvoiceNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                        adDocumentSequenceAssignment.setDsaNextSequence(
                                EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                    }
                    catch (FinderException ex) {
                        invoiceDetails.setInvCmInvoiceNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                        adDocumentSequenceAssignment.setDsaNextSequence(
                                EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                        break;
                    }
                } else {
                    try {
                        arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(
                                adBranchDocumentSequenceAssignment.getBdsNextSequence(), EJBCommon.FALSE, branchCode, companyCode);
                        invoiceDetails.setInvCmInvoiceNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon
                                .incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                    }
                    catch (FinderException ex) {
                        invoiceDetails.setInvCmInvoiceNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon
                                .incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                        break;
                    }
                }
            }
        }
    }

}