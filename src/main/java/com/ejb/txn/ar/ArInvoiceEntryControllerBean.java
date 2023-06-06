package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gen.LocalGenValueSetValue;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.ar.ArINVAmountExceedsCreditLimitException;
import com.ejb.exception.ar.ArINVInvoiceHasReceipt;
import com.ejb.exception.ar.ArINVNoSalesOrderLinesFoundException;
import com.ejb.exception.ar.ArInvDuplicateUploadNumberException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.txn.OmegaCommonDataController;
import com.ejb.txnreports.inv.InvRepItemCostingController;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.ar.ArInvoiceDetails;
import com.util.ar.ArStandardMemoLineDetails;
import com.util.mod.ar.*;
import com.util.mod.inv.InvModLineItemDetails;
import com.util.mod.inv.InvModTagListDetails;
import com.util.mod.inv.InvModUnitOfMeasureDetails;
import jakarta.ejb.*;

import java.util.*;

@Stateless(name = "ArInvoiceEntryControllerEJB")
public class ArInvoiceEntryControllerBean extends EJBContextClass implements ArInvoiceEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private OmegaCommonDataController commonData;
    @EJB
    private ArApprovalController arApprovalController;
    @EJB
    private InvRepItemCostingController ejbRIC;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvPriceLevelHome invPriceLevelHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private LocalArSalespersonHome arSalespersonHome;
    @EJB
    private LocalArStandardMemoLineClassHome arStandardMemoLineClassHome;
    @EJB
    private LocalArSalesOrderHome arSalesOrderHome;
    @EJB
    private LocalArSalesOrderLineHome arSalesOrderLineHome;
    @EJB
    private LocalArSalesOrderInvoiceLineHome arSalesOrderInvoiceLineHome;
    @EJB
    private LocalArJobOrderHome arJobOrderHome;
    @EJB
    private LocalArJobOrderLineHome arJobOrderLineHome;
    @EJB
    private LocalArJobOrderInvoiceLineHome arJobOrderInvoiceLineHome;
    @EJB
    private LocalArInvoiceBatchHome arInvoiceBatchHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdPaymentScheduleHome adPaymentScheduleHome;
    @EJB
    private LocalAdDiscountHome adDiscountHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalArCustomerBalanceHome arCustomerBalanceHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalAdBranchCustomerHome adBranchCustomerHome;
    @EJB
    private LocalAdBranchArTaxCodeHome adBranchArTaxCodeHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalArInvoiceLineItemHome arInvoiceLineItemHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalInvLineItemTemplateHome invLineItemTemplateHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private LocalArInvoiceLineHome arInvoiceLineHome;
    @EJB
    private LocalArAutoAccountingSegmentHome arAutoAccountingSegmentHome;
    @EJB
    private LocalAdBranchStandardMemoLineHome adBranchStandardMemoLineHome;
    @EJB
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalGlForexLedgerHome glForexLedgerHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvTagHome invTagHome;
    @EJB
    private LocalGenValueSetValueHome getValueSetValueHome;
    @EJB
    private LocalArPdcHome arPdcHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalArAppliedInvoiceHome arAppliedInvoiceHome;


    @Override
    public Integer saveArInvEntry(
            ArModInvoiceDetails details, String taxCode, int customerCode, double drTotalAmount,
            Integer branchCode, Integer companyCode)
            throws GlobalDocumentNumberNotUniqueException {

        Debug.print("ArInvoiceEntryControllerBean saveArInvEntry");
        LocalArInvoice arInvoice;

        try {

            // validate if document number is unique document number is automatic then set next sequence
            LocalArInvoice arExistingInvoice = null;
            try {
                arExistingInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(
                        details.getInvNumber(), EJBCommon.FALSE, branchCode, companyCode);
            }
            catch (FinderException ex) {
            }

            // create invoice
            details.setInvConversionDate(null);
            details.setInvConversionRate(1);

            arInvoice = arInvoiceHome.create("ITEMS", EJBCommon.FALSE,
                    details.getInvDescription(), details.getInvDate(),
                    details.getInvNumber(), details.getInvReferenceNumber(), details.getInvUploadNumber(), null, null,
                    0d, 0d, 0d, 0d, 0d, 0d, details.getInvConversionDate(), details.getInvConversionRate(), details.getInvMemo(),
                    0d, 0d, details.getInvBillToAddress(), details.getInvBillToContact(), details.getInvBillToAltContact(),
                    details.getInvBillToPhone(), details.getInvBillingHeader(), details.getInvBillingFooter(),
                    details.getInvBillingHeader2(), details.getInvBillingFooter2(), details.getInvBillingHeader3(),
                    details.getInvBillingFooter3(), details.getInvBillingSignatory(), details.getInvSignatoryTitle(),
                    details.getInvShipToAddress(), details.getInvShipToContact(), details.getInvShipToAltContact(),
                    details.getInvShipToPhone(), details.getInvShipDate(), details.getInvLvFreight(),
                    null, null,
                    EJBCommon.FALSE,
                    null, EJBCommon.FALSE, EJBCommon.FALSE,
                    EJBCommon.FALSE, EJBCommon.FALSE,
                    EJBCommon.FALSE, null, 0d, null, null, null, null,
                    details.getInvCreatedBy(), details.getInvDateCreated(),
                    details.getInvLastModifiedBy(), details.getInvDateLastModified(),
                    null, null, null, null, EJBCommon.FALSE, null, null, null, details.getInvDebitMemo(),
                    details.getInvSubjectToCommission(), null, details.getInvDate(), branchCode, companyCode);

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalArCustomer arCustomer = arCustomerHome.findByPrimaryKey(customerCode);
            arInvoice.setArCustomer(arCustomer);

            LocalAdPaymentTerm adPaymentTerm = arCustomer.getAdPaymentTerm();
            arInvoice.setAdPaymentTerm(adPaymentTerm);

            LocalGlFunctionalCurrency glFunctionalCurrency = adCompany.getGlFunctionalCurrency();
            arInvoice.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(taxCode, companyCode);
            arInvoice.setArTaxCode(arTaxCode);

            LocalArWithholdingTaxCode arWithholdingTaxCode = arCustomer.getArCustomerClass().getArWithholdingTaxCode();
            arInvoice.setArWithholdingTaxCode(arWithholdingTaxCode);

            double amountDue = 0;

            // add new invoice lines and distribution record
            double TOTAL_TAX = 0d;
            double TOTAL_LINE = 0d;
            double TOTAL_UNTAXABLE = 0d;

            Iterator i = details.getInvIlList().iterator();
            while (i.hasNext()) {

                ArModInvoiceLineDetails mInvDetails = (ArModInvoiceLineDetails) i.next();
                LocalArInvoiceLine arInvoiceLine = this.addArIlEntry(mInvDetails, arInvoice, companyCode);

                // add revenue/credit distributions
                this.addArDrEntry(arInvoice.getArDrNextLine(),
                        "REVENUE", EJBCommon.FALSE, arInvoiceLine.getIlAmount(),
                        this.getArGlCoaRevenueAccount(arInvoiceLine, branchCode, companyCode), null, arInvoice, branchCode, companyCode);

                TOTAL_LINE += arInvoiceLine.getIlAmount();
                TOTAL_TAX += arInvoiceLine.getIlTaxAmount();

                if (arInvoiceLine.getIlTax() == EJBCommon.FALSE) {
                    TOTAL_UNTAXABLE += arInvoiceLine.getIlAmount();
                }
            }

            // add tax distribution if necessary
            if (!arTaxCode.getTcType().equals("NONE") &&
                    !arTaxCode.getTcType().equals("EXEMPT")) {

                if (arTaxCode.getTcInterimAccount() == null) {
                    this.addArDrEntry(arInvoice.getArDrNextLine(),
                            "TAX", EJBCommon.FALSE, TOTAL_TAX, arTaxCode.getGlChartOfAccount().getCoaCode(),
                            null, arInvoice, branchCode, companyCode);
                } else {
                    this.addArDrEntry(arInvoice.getArDrNextLine(),
                            "DEFERRED TAX", EJBCommon.FALSE, TOTAL_TAX, arTaxCode.getTcInterimAccount(),
                            null, arInvoice, branchCode, companyCode);
                }
            }

            // add wtax distribution if necessary
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            double W_TAX_AMOUNT = 0d;
            if (arWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals("INVOICE")) {
                W_TAX_AMOUNT = EJBCommon.roundIt((TOTAL_LINE - TOTAL_UNTAXABLE) * (arWithholdingTaxCode.getWtcRate() / 100), commonData.getGlFcPrecisionUnit(companyCode));
                this.addArDrEntry(arInvoice.getArDrNextLine(), "W-TAX",
                        EJBCommon.TRUE, W_TAX_AMOUNT, arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(),
                        null, arInvoice, branchCode, companyCode);

            }

            // add payment discount if necessary
            double DISCOUNT_AMT = 0d;

            if (adPaymentTerm.getPytDiscountOnInvoice() == EJBCommon.TRUE) {

                Collection adPaymentSchedules = adPaymentScheduleHome.findByPytCode(adPaymentTerm.getPytCode(), companyCode);
                ArrayList adPaymentScheduleList = new ArrayList(adPaymentSchedules);
                LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) adPaymentScheduleList.get(0);

                Collection adDiscounts = adDiscountHome.findByPsCode(adPaymentSchedule.getPsCode(), companyCode);
                ArrayList adDiscountList = new ArrayList(adDiscounts);
                LocalAdDiscount adDiscount = (LocalAdDiscount) adDiscountList.get(0);

                double rate = adDiscount.getDscDiscountPercent();
                DISCOUNT_AMT = (TOTAL_LINE + TOTAL_TAX) * (rate / 100d);

                this.addArDrIliEntry(arInvoice.getArDrNextLine(), "DISCOUNT",
                        EJBCommon.TRUE, DISCOUNT_AMT,
                        adPaymentTerm.getGlChartOfAccount().getCoaCode(),
                        arInvoice, branchCode, companyCode);

            }

            // add receivable distribution
            try {

                LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), branchCode, companyCode);

                this.addArDrEntry(arInvoice.getArDrNextLine(), "RECEIVABLE",
                        EJBCommon.TRUE, drTotalAmount + TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT_AMT,
                        adBranchCustomer.getBcstGlCoaReceivableAccount(),
                        null, arInvoice, branchCode, companyCode);

                this.addArDrEntry(arInvoice.getArDrNextLine(), "REVENUE",
                        EJBCommon.FALSE, drTotalAmount, adBranchCustomer.getBcstGlCoaReceivableAccount(),
                        null, arInvoice, branchCode, companyCode);

            }
            catch (FinderException ex) {

            }

            // compute invoice amount due
            amountDue = TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT_AMT;

            //set invoice amount due
            arInvoice.setInvAmountDue(amountDue);

            // remove all payment schedule
            Collection arInvoicePaymentSchedules = arInvoice.getArInvoicePaymentSchedules();
            i = arInvoicePaymentSchedules.iterator();
            while (i.hasNext()) {
                LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) i.next();
                i.remove();
                em.remove(arInvoicePaymentSchedule);
            }

            // create invoice payment schedule
            short precisionUnit = commonData.getGlFcPrecisionUnit(companyCode);
            double TOTAL_PAYMENT_SCHEDULE = 0d;

            GregorianCalendar gcPrevDateDue = new GregorianCalendar();
            GregorianCalendar gcDateDue = new GregorianCalendar();
            gcPrevDateDue.setTime(arInvoice.getInvEffectivityDate());

            Collection adPaymentSchedules = adPaymentTerm.getAdPaymentSchedules();
            i = adPaymentSchedules.iterator();
            while (i.hasNext()) {
                LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) i.next();
                // get date due
                switch (arInvoice.getAdPaymentTerm().getPytScheduleBasis()) {
                    case "DEFAULT" -> {
                        gcDateDue.setTime(arInvoice.getInvEffectivityDate());
                        gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());
                    }
                    case "MONTHLY" -> {
                        gcDateDue = gcPrevDateDue;
                        gcDateDue.add(Calendar.MONTH, 1);
                        gcPrevDateDue = gcDateDue;
                    }
                    case "BI-MONTHLY" -> {
                        gcDateDue = gcPrevDateDue;
                        if (gcPrevDateDue.get(Calendar.MONTH) != 1) {
                            if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 31
                                    && gcPrevDateDue.get(Calendar.DATE) > 15 && gcPrevDateDue.get(Calendar.DATE) < 31) {
                                gcDateDue.add(Calendar.DATE, 16);
                            } else {
                                gcDateDue.add(Calendar.DATE, 15);
                            }
                        } else if (gcPrevDateDue.get(Calendar.MONTH) == 1) {
                            if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28
                                    && gcPrevDateDue.get(Calendar.DATE) == 14) {
                                gcDateDue.add(Calendar.DATE, 14);
                            } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28
                                    && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 28) {
                                gcDateDue.add(Calendar.DATE, 13);
                            } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 29
                                    && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 29) {
                                gcDateDue.add(Calendar.DATE, 14);
                            } else {
                                gcDateDue.add(Calendar.DATE, 15);
                            }
                        }
                        gcPrevDateDue = gcDateDue;
                    }
                }

                // create a payment schedule
                double PAYMENT_SCHEDULE_AMOUNT = 0;

                // if last payment schedule subtract to avoid rounding difference error
                if (i.hasNext()) {
                    PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt(
                            (adPaymentSchedule.getPsRelativeAmount() / adPaymentTerm.getPytBaseAmount()) * arInvoice.getInvAmountDue(), precisionUnit);
                } else {
                    PAYMENT_SCHEDULE_AMOUNT = arInvoice.getInvAmountDue() - TOTAL_PAYMENT_SCHEDULE;

                }

                LocalArInvoicePaymentSchedule arInvoicePaymentSchedule =
                        arInvoicePaymentScheduleHome.create(gcDateDue.getTime(),
                                adPaymentSchedule.getPsLineNumber(),
                                PAYMENT_SCHEDULE_AMOUNT,
                                0d, EJBCommon.FALSE,
                                (short) 0, gcDateDue.getTime(), 0d, 0d,
                                companyCode);

                arInvoicePaymentSchedule.setArInvoice(arInvoice);
                TOTAL_PAYMENT_SCHEDULE += PAYMENT_SCHEDULE_AMOUNT;
            }
            arInvoice.setInvApprovalStatus(null);
            return 1;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveArInvEntry(ArInvoiceDetails details, String paymentName, String taxCode, String withholdingTaxCode,
                                  String currencyName, String customerCode, String invoiceBatchName, ArrayList invoiceLines,
                                  boolean isDraft, String salesPersonCode, String projectCode, String projectTypeCode,
                                  String projectPhaseName, String contractTermName, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException,
            GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, ArINVAmountExceedsCreditLimitException,
            GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException,
            GlobalBranchAccountNumberInvalidException, ArInvDuplicateUploadNumberException {

        Debug.print("ArInvoiceEntryControllerBean saveArInvEntry");
        LocalArInvoice arInvoice = null;
        try {
            // validate if invoice is already deleted
            arInvoice = isInvoiceDeleted(details, arInvoice);
            // validate if invoice is already posted, void, approved or pending
            if (details.getInvCode() != null) {
                validateInvoice(arInvoice);
            }
            // invoice void
            if (isInvoiceVoid(details, arInvoice)) {
                return arInvoice.getInvCode();
            }
            // validate if document number is unique document number is automatic then set next sequence
            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
            LocalArInvoice arExistingInvoice = null;
            if (details.getInvCode() == null) {
                String documentType = details.getInvDocumentType();
                try {
                    if (documentType != null) {
                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode);
                    } else {
                        documentType = EJBCommon.AR_INVOICE;
                    }
                }
                catch (FinderException ex) {
                    documentType = EJBCommon.AR_INVOICE;
                }

                try {
                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode);
                }
                catch (FinderException ex) {
                }

                try {
                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                try {
                    arExistingInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(details.getInvNumber(), EJBCommon.FALSE, branchCode, companyCode);
                    if (arExistingInvoice != null) {
                        throw new GlobalDocumentNumberNotUniqueException();
                    }
                }
                catch (FinderException ex) {
                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getInvNumber() == null || details.getInvNumber().trim().length() == 0)) {
                    setupDocumentSequenceAssignment(details, branchCode, companyCode, adBranchDocumentSequenceAssignment, adDocumentSequenceAssignment);
                }
                // Confirm if Upload Number have duplicate
                if (details.getInvUploadNumber() != null) {
                    if (!details.getInvUploadNumber().equals("")) {
                        try {
                            arInvoiceHome.findByUploadNumberAndCompanyCode(details.getInvUploadNumber(), companyCode);
                            // throw exception if found duplicate upload number
                            throw new ArInvDuplicateUploadNumberException();
                        }
                        catch (FinderException ex) {

                        }
                    }
                }
            } else {
                try {
                    arExistingInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(details.getInvNumber(), EJBCommon.FALSE, branchCode, companyCode);
                }
                catch (FinderException ex) {
                }
                if (arExistingInvoice != null && !arExistingInvoice.getInvCode().equals(details.getInvCode())) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }
                if (!arInvoice.getInvNumber().equals(details.getInvNumber()) && (details.getInvNumber() == null || details.getInvNumber().trim().length() == 0)) {
                    details.setInvNumber(arInvoice.getInvNumber());
                }
                // Confirm if Upload Number have duplicate
                isUploadNumberDuplicate(details, companyCode, arExistingInvoice);
            }

            // validate if conversion date exists
            isConversionDateExistsSaveArInvEntry(details, currencyName, companyCode);

            // validate if payment term has at least one payment schedule
            if (adPaymentTermHome.findByPytName(paymentName, companyCode).getAdPaymentSchedules().isEmpty()) {
                throw new GlobalPaymentTermInvalidException();
            }

            // used in checking if invoice should re-generate distribution records and re-calculate taxes
            boolean isRecalculate = true;

            // create invoice
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(details.getInvDate());
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            details.setInvInterestNextRunDate(calendar.getTime());

            if (details.getInvCode() == null) {
                arInvoice = createInvoice(details, branchCode, companyCode);
            } else {
                // check if critical fields are changed
                isRecalculate = isCriticalFieldsChanged(paymentName, taxCode, withholdingTaxCode, customerCode, invoiceLines, arInvoice, isRecalculate);
                updateInvoice(details, arInvoice);
            }

            arInvoice.setReportParameter(details.getReportParameter());
            arInvoice.setInvDocumentType(details.getInvDocumentType());

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(paymentName, companyCode);
            arInvoice.setAdPaymentTerm(adPaymentTerm);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(currencyName, companyCode);
            arInvoice.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(taxCode, companyCode);
            arInvoice.setArTaxCode(arTaxCode);
            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(withholdingTaxCode, companyCode);
            arInvoice.setArWithholdingTaxCode(arWithholdingTaxCode);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(customerCode, companyCode);
            arInvoice.setArCustomer(arCustomer);

            tagSalesPerson(salesPersonCode, companyCode, arInvoice);

            setInvoiceBatch(invoiceBatchName, branchCode, companyCode, arInvoice);

            double amountDue = 0;
            if (isRecalculate) {

                // remove all invoice line items
                Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();
                Iterator i = arInvoiceLineItems.iterator();
                while (i.hasNext()) {
                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();

                    // remove all inv tag
                    Collection invTags = arInvoiceLineItem.getInvTags();
                    Iterator x = invTags.iterator();
                    while (x.hasNext()) {
                        LocalInvTag invTag = (LocalInvTag) x.next();
                        x.remove();
                        em.remove(invTag);
                    }

                    double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                    arInvoiceLineItem.getInvItemLocation().setIlCommittedQuantity(arInvoiceLineItem.getInvItemLocation().getIlCommittedQuantity() - convertedQuantity);
                    i.remove();
                    em.remove(arInvoiceLineItem);
                }

                // remove all invoice lines
                Collection arInvoiceLines = arInvoice.getArInvoiceLines();
                i = arInvoiceLines.iterator();
                while (i.hasNext()) {
                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) i.next();
                    i.remove();
                    em.remove(arInvoiceLine);
                }

                // remove all sales order lines
                Collection arSalesOrderInvoiceLines = arInvoice.getArSalesOrderInvoiceLines();
                i = arSalesOrderInvoiceLines.iterator();
                while (i.hasNext()) {
                    LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) i.next();

                    // remove all inv tag
                    Collection invTags = arSalesOrderInvoiceLine.getInvTags();
                    Iterator x = invTags.iterator();
                    while (x.hasNext()) {
                        LocalInvTag invTag = (LocalInvTag) x.next();
                        x.remove();
                        em.remove(invTag);
                    }
                    i.remove();
                    em.remove(arSalesOrderInvoiceLine);
                }

                // remove all distribution records
                Collection arDistributionRecords = arInvoice.getArDistributionRecords();
                i = arDistributionRecords.iterator();
                while (i.hasNext()) {
                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();
                    i.remove();
                    em.remove(arDistributionRecord);
                }

                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

                // add new invoice lines and distribution record
                double totalTax = 0d;
                double totalLine = 0d;
                double totalNonTaxable = 0d;

                i = invoiceLines.iterator();

                while (i.hasNext()) {

                    ArModInvoiceLineDetails mInvDetails = (ArModInvoiceLineDetails) i.next();
                    LocalArInvoiceLine arInvoiceLine = this.addArIlEntry(mInvDetails, arInvoice, companyCode);

                    double invoiceLine = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arInvoiceLine.getIlAmount(), companyCode);

                    double invoiceTax = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arInvoiceLine.getIlTaxAmount(), companyCode);

                    if (adPaymentTerm.getPytDiscountOnInvoice() == EJBCommon.TRUE) {
                        Collection<LocalAdPaymentSchedule> adPaymentSchedules = adPaymentScheduleHome.findByPytCode(adPaymentTerm.getPytCode(), companyCode);

                        ArrayList<LocalAdPaymentSchedule> adPaymentScheduleList = new ArrayList(adPaymentSchedules);

                        LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) adPaymentScheduleList.get(0);
                        Collection adDiscounts = adDiscountHome.findByPsCode(adPaymentSchedule.getPsCode(), companyCode);
                    }

                    // add revenue/credit distributions Check If Memo Line Type is Service Charge
                    isDistributionRecordServiceCharge(branchCode, companyCode, arInvoice, arInvoiceLine, invoiceLine);
                    totalLine += arInvoiceLine.getIlAmount();
                    totalTax += arInvoiceLine.getIlTaxAmount();
                    if (arInvoiceLine.getIlTax() == EJBCommon.FALSE) {
                        totalNonTaxable += arInvoiceLine.getIlAmount();
                    }
                }

                // add tax distribution if necessary
                addTaxDistributionRecord(branchCode, companyCode, arInvoice, arTaxCode, totalTax);

                // add wtax distribution if necessary
                adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
                double withholdingTaxAmount = calculateWithholdingTax(branchCode, companyCode, arInvoice, arWithholdingTaxCode, adPreference, totalLine - totalNonTaxable);

                // add payment discount if necessary
                double discountAmount = 0d;
                if (adPaymentTerm.getPytDiscountOnInvoice() == EJBCommon.TRUE) {
                    Collection adPaymentSchedules = adPaymentScheduleHome.findByPytCode(adPaymentTerm.getPytCode(), companyCode);
                    ArrayList adPaymentScheduleList = new ArrayList(adPaymentSchedules);
                    LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) adPaymentScheduleList.get(0);
                    Collection adDiscounts = adDiscountHome.findByPsCode(adPaymentSchedule.getPsCode(), companyCode);
                    ArrayList adDiscountList = new ArrayList(adDiscounts);
                    LocalAdDiscount adDiscount = (LocalAdDiscount) adDiscountList.get(0);
                    double rate = adDiscount.getDscDiscountPercent();
                    discountAmount = (totalLine + totalTax) * (rate / 100d);
                    double convertedDiscountAmount = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), discountAmount, companyCode);
                    this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.DISCOUNT, EJBCommon.TRUE, convertedDiscountAmount, adPaymentTerm.getGlChartOfAccount().getCoaCode(), arInvoice, branchCode, companyCode);
                }

                // add receivable distribution
                amountDue = totalLine + totalTax - withholdingTaxAmount - discountAmount;
                double convertedAmountDue = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), amountDue, companyCode);
                try {
                    LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), branchCode, companyCode);
                    this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.RECEIVABLE, EJBCommon.TRUE, convertedAmountDue, adBranchCustomer.getBcstGlCoaReceivableAccount(), null, arInvoice, branchCode, companyCode);
                }
                catch (FinderException ex) {
                    this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.RECEIVABLE, EJBCommon.TRUE, convertedAmountDue, arCustomer.getCstGlCoaReceivableAccount(), null, arInvoice, branchCode, companyCode);
                }

                // compute invoice amount due set invoice amount due
                arInvoice.setInvAmountDue(amountDue);

                // remove all payment schedule
                removeAllPaymentSchedule(arInvoice);

                // create invoice payment schedule
                short precisionUnit = commonData.getGlFcPrecisionUnit(companyCode);
                double totalPaymentSchedule = 0d;
                double totalPaymentScheduleInterest = totalLine + totalTax;

                GregorianCalendar gcPrevDateDue = new GregorianCalendar();
                GregorianCalendar gcDateDue = new GregorianCalendar();
                gcPrevDateDue.setTime(arInvoice.getInvEffectivityDate());

                Collection adPaymentSchedules = adPaymentTerm.getAdPaymentSchedules();
                i = adPaymentSchedules.iterator();
                while (i.hasNext()) {
                    LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) i.next();
                    // get date due
                    switch (arInvoice.getAdPaymentTerm().getPytScheduleBasis()) {
                        case EJBCommon.MONTHLY:
                            gcDateDue = gcPrevDateDue;
                            gcDateDue.add(Calendar.MONTH, 1);
                            gcPrevDateDue = gcDateDue;
                            break;
                        case EJBCommon.BI_MONTHLY:
                            gcDateDue = gcPrevDateDue;
                            if (gcPrevDateDue.get(Calendar.MONTH) != 1) {
                                if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 31 && gcPrevDateDue.get(Calendar.DATE) > 15 && gcPrevDateDue.get(Calendar.DATE) < 31) {
                                    gcDateDue.add(Calendar.DATE, 16);
                                } else {
                                    gcDateDue.add(Calendar.DATE, 15);
                                }
                            } else if (gcPrevDateDue.get(Calendar.MONTH) == 1) {
                                if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28 && gcPrevDateDue.get(Calendar.DATE) == 14) {
                                    gcDateDue.add(Calendar.DATE, 14);
                                } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28 && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 28) {
                                    gcDateDue.add(Calendar.DATE, 13);
                                } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 29 && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 29) {
                                    gcDateDue.add(Calendar.DATE, 14);
                                } else {
                                    gcDateDue.add(Calendar.DATE, 15);
                                }
                            }
                            gcPrevDateDue = gcDateDue;
                            break;
                        default:
                            gcDateDue.setTime(arInvoice.getInvEffectivityDate());
                            gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());
                            break;
                    }

                    // create a payment schedule
                    double paymentScheduleAmount = 0;
                    double paymentScheduleInterest = 0;
                    double paymentSchedulePrincipal = 0;

                    // if last payment schedule subtract to avoid rounding difference error
                    if (i.hasNext()) {

                        paymentScheduleAmount = EJBCommon.roundIt((adPaymentSchedule.getPsRelativeAmount() / adPaymentTerm.getPytBaseAmount()) * arInvoice.getInvAmountDue(), precisionUnit);
                        paymentScheduleInterest = EJBCommon.roundIt((totalPaymentScheduleInterest) * (adPaymentTerm.getPytMonthlyInterestRate() / 100), precisionUnit);
                        paymentSchedulePrincipal = -(paymentScheduleAmount) + paymentScheduleInterest;

                    } else {

                        paymentScheduleAmount = arInvoice.getInvAmountDue() - totalPaymentSchedule;
                        paymentScheduleInterest = EJBCommon.roundIt((totalPaymentScheduleInterest) * (adPaymentTerm.getPytMonthlyInterestRate() / 100), precisionUnit);
                        paymentSchedulePrincipal = -(paymentScheduleAmount) + paymentScheduleInterest;
                    }

                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arInvoicePaymentScheduleHome.create(gcDateDue.getTime(), adPaymentSchedule.getPsLineNumber(), paymentScheduleAmount, 0d, EJBCommon.FALSE, (short) 0, gcDateDue.getTime(), 0d, 0d, companyCode);

                    arInvoicePaymentSchedule.setIpsInterestDue(paymentScheduleInterest);
                    arInvoice.addArInvoicePaymentSchedule(arInvoicePaymentSchedule);

                    totalPaymentSchedule += paymentScheduleAmount;
                    totalPaymentScheduleInterest += paymentSchedulePrincipal;
                }
            }

            // generate approval status
            String invoiceApprovalStatus = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // validate if amount due + unposted invoices' amount + current balance
                // +unposted receipts' amount does not exceed customer's credit limit
                double balance = 0;
                LocalAdApprovalDocument adInvoiceApprovalDocument = adApprovalDocumentHome.findByAdcType(EJBCommon.AR_INVOICE, companyCode);

                if (arCustomer.getCstCreditLimit() > 0) {

                    balance = computeTotalBalance(details.getInvCode(), customerCode, companyCode);
                    balance += amountDue;

                    if (arCustomer.getCstCreditLimit() < balance && (adApproval.getAprEnableArInvoice() == EJBCommon.FALSE ||
                            (adApproval.getAprEnableArInvoice() == EJBCommon.TRUE &&
                                    adInvoiceApprovalDocument.getAdcEnableCreditLimitChecking() == EJBCommon.FALSE))) {

                        throw new ArINVAmountExceedsCreditLimitException();
                    }
                }

                // find overdue invoices
                Collection arOverdueInvoices = arInvoicePaymentScheduleHome.findOverdueIpsByInvDateAndCstCustomerCode(arInvoice.getInvDate(), customerCode, companyCode);

                // check if ar invoice approval is enabled
                if (adApproval.getAprEnableArInvoice() == EJBCommon.FALSE || (arCustomer.getCstCreditLimit() > balance && arOverdueInvoices.size() == 0)) {
                    invoiceApprovalStatus = "N/A";
                } else {

                    // check if invoice is self approved
                    LocalAdUser adUser = adUserHome.findByUsrName(details.getInvLastModifiedBy(), companyCode);
                    invoiceApprovalStatus = arApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getInvLastModifiedBy(),
                            adUser.getUsrDescription(), EJBCommon.AR_INVOICE, arInvoice.getInvCode(), arInvoice.getInvNumber(),
                            arInvoice.getInvDate(), branchCode, companyCode);
                }
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (invoiceApprovalStatus != null && invoiceApprovalStatus.equals("N/A") && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                this.executeArInvPost(arInvoice.getInvCode(), arInvoice.getInvLastModifiedBy(), branchCode, companyCode);
            }

            // set invoice approval status
            arInvoice.setInvApprovalStatus(invoiceApprovalStatus);
            return arInvoice.getInvCode();

        }
        catch (GlobalRecordAlreadyDeletedException | ArInvDuplicateUploadNumberException |
               GlobalBranchAccountNumberInvalidException | GlobalJournalNotBalanceException |
               GlJREffectiveDatePeriodClosedException | GlJREffectiveDateNoPeriodExistException |
               GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
               GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
               GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
               ArINVAmountExceedsCreditLimitException | GlobalPaymentTermInvalidException |
               GlobalConversionDateNotExistException | GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveArInvIliEntry(ArInvoiceDetails invoiceDetails, String paymentName, String taxCode, String withholdingTaxCode, String currencyName, String customerCode, String invoiceBatchName, ArrayList invoiceLines, boolean isDraft, String salesPersonCode, String deployedBranchName, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, ArINVAmountExceedsCreditLimitException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalRecordInvalidException, GlobalExpiryDateNotFoundException, GlobalMiscInfoIsRequiredException, ArInvDuplicateUploadNumberException {

        Debug.print("ArInvoiceEntryControllerBean saveArInvIliEntry");
        LocalArInvoice arInvoice = null;
        int lineNumberError = 0;
        try {

            // validate if invoice is already deleted
            arInvoice = isInvoiceDeleted(invoiceDetails, arInvoice);

            // validate if invoice is already posted, void, approved or pending
            if (invoiceDetails.getInvCode() != null) {
                validateInvoice(arInvoice);
            }

            // validate if invoice void
            if (isInvoiceVoid(invoiceDetails, arInvoice)) {
                return arInvoice.getInvCode();
            }

            // validate if document number is unique document number is automatic then set next sequence
            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
            LocalArInvoice arExistingInvoice = null;

            if (invoiceDetails.getInvCode() == null) {
                try {
                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(EJBCommon.AR_INVOICE, companyCode);
                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);
                    arExistingInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(invoiceDetails.getInvNumber(), EJBCommon.FALSE, branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                if (arExistingInvoice != null) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (invoiceDetails.getInvNumber() == null || invoiceDetails.getInvNumber().trim().length() == 0)) {
                    setupDocumentSequenceAssignment(invoiceDetails, branchCode, companyCode, adBranchDocumentSequenceAssignment, adDocumentSequenceAssignment);
                }

                // Confirm if Upload Number have duplicate
                isUploadNumberHaveDuplicate(invoiceDetails, companyCode);

            } else {
                try {
                    arExistingInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(invoiceDetails.getInvNumber(), EJBCommon.FALSE, branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                if (arExistingInvoice != null && !arExistingInvoice.getInvCode().equals(invoiceDetails.getInvCode())) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (!arInvoice.getInvNumber().equals(invoiceDetails.getInvNumber()) && (invoiceDetails.getInvNumber() == null || invoiceDetails.getInvNumber().trim().length() == 0)) {
                    invoiceDetails.setInvNumber(arInvoice.getInvNumber());
                }

                // Confirm if Upload Number have duplicate
                if (invoiceDetails.getInvUploadNumber() != null && !invoiceDetails.getInvUploadNumber().trim().equals("") && !invoiceDetails.getInvUploadNumber().equals(arExistingInvoice.getInvUploadNumber())) {
                    try {
                        arInvoiceHome.findByUploadNumberAndCompanyCode(invoiceDetails.getInvUploadNumber(), companyCode);
                        // throw exception if found duplicate upload number
                        throw new ArInvDuplicateUploadNumberException();
                    }
                    catch (FinderException ex) {
                    }
                }
            }

            // validate if conversion date exists
            isConversionDateExists(invoiceDetails, currencyName, companyCode);

            // validate if payment term has at least one payment schedule
            if (adPaymentTermHome.findByPytName(paymentName, companyCode).getAdPaymentSchedules().isEmpty()) {
                throw new GlobalPaymentTermInvalidException();
            }

            // used in checking if invoice should re-generate distribution records and
            // re-calculate taxes
            boolean isRecalculate = true;

            // create invoice
            if (invoiceDetails.getInvCode() == null) {
                arInvoice = arInvoiceHome
                        .InvType(invoiceDetails.getInvType())
                        .InvDescription(invoiceDetails.getInvDescription())
                        .InvDate(invoiceDetails.getInvDate())
                        .InvNumber(invoiceDetails.getInvNumber())
                        .InvReferenceNumber(invoiceDetails.getInvReferenceNumber())
                        .InvEffectivityDate(invoiceDetails.getInvEffectivityDate())
                        .InvConversionRate(1.0)
                        .InvCreatedBy(invoiceDetails.getInvCreatedBy())
                        .InvDateCreated(invoiceDetails.getInvDateCreated())
                        .InvAdBranch(branchCode)
                        .InvAdCompany(companyCode)
                        .buildInvoice(invoiceDetails.getCompanyShortName());
            } else {
                // check if critical fields are changed
                if (!arInvoice.getArTaxCode().getTcName().equals(taxCode) || !arInvoice.getArWithholdingTaxCode().getWtcName().equals(withholdingTaxCode) || !arInvoice.getArCustomer().getCstCustomerCode().equals(customerCode) || !arInvoice.getAdPaymentTerm().getPytName().equals(paymentName) || !arInvoice.getInvDate().equals(invoiceDetails.getInvDate()) || !arInvoice.getInvEffectivityDate().equals(invoiceDetails.getInvEffectivityDate()) || arInvoice.getInvDownPayment() != invoiceDetails.getInvDownPayment() || invoiceLines.size() != arInvoice.getArInvoiceLineItems().size()) {
                    isRecalculate = true;
                } else if (invoiceLines.size() == arInvoice.getArInvoiceLineItems().size()) {
                    Iterator iliIter = arInvoice.getArInvoiceLineItems().iterator();
                    Iterator iliListIter = invoiceLines.iterator();
                    while (iliIter.hasNext()) {
                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) iliIter.next();
                        ArModInvoiceLineItemDetails mdetails = (ArModInvoiceLineItemDetails) iliListIter.next();
                        if (!arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getIliIiName()) || !arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getIliLocName()) || !arInvoiceLineItem.getInvUnitOfMeasure().getUomName().equals(mdetails.getIliUomName()) || arInvoiceLineItem.getIliQuantity() != mdetails.getIliQuantity() || arInvoiceLineItem.getIliUnitPrice() != mdetails.getIliUnitPrice() || arInvoiceLineItem.getIliTotalDiscount() != mdetails.getIliTotalDiscount() || arInvoiceLineItem.getIliTax() != mdetails.getIliTax()) {
                            isRecalculate = true;
                            break;
                        }
                    }
                }
                arInvoice.setInvDownPayment(invoiceDetails.getInvDownPayment());
                arInvoice.setInvDocumentType(invoiceDetails.getInvDocumentType());
                arInvoice.setInvType(invoiceDetails.getInvType());
                arInvoice.setInvDocumentType(invoiceDetails.getInvDocumentType());
                arInvoice.setInvDescription(invoiceDetails.getInvDescription());
                arInvoice.setInvDate(invoiceDetails.getInvDate());
                arInvoice.setInvMemo(invoiceDetails.getInvMemo());
                arInvoice.setInvNumber(invoiceDetails.getInvNumber());
                arInvoice.setInvReferenceNumber(invoiceDetails.getInvReferenceNumber());
                arInvoice.setInvUploadNumber(invoiceDetails.getInvUploadNumber());
                arInvoice.setInvConversionDate(invoiceDetails.getInvConversionDate());
                arInvoice.setInvConversionRate(invoiceDetails.getInvConversionRate());
                arInvoice.setInvPreviousReading(arInvoice.getInvPreviousReading());
                arInvoice.setInvPresentReading(arInvoice.getInvPresentReading());
                arInvoice.setInvBillToAddress(invoiceDetails.getInvBillToAddress());
                arInvoice.setInvBillToContact(invoiceDetails.getInvBillToContact());
                arInvoice.setInvBillToAltContact(invoiceDetails.getInvBillToAltContact());
                arInvoice.setInvBillToPhone(invoiceDetails.getInvBillToPhone());
                arInvoice.setInvBillingHeader(invoiceDetails.getInvBillingHeader());
                arInvoice.setInvBillingFooter(invoiceDetails.getInvBillingFooter());
                arInvoice.setInvBillingHeader2(invoiceDetails.getInvBillingHeader2());
                arInvoice.setInvBillingFooter2(invoiceDetails.getInvBillingFooter2());
                arInvoice.setInvBillingHeader3(invoiceDetails.getInvBillingHeader3());
                arInvoice.setInvBillingFooter3(invoiceDetails.getInvBillingFooter3());
                arInvoice.setInvBillingSignatory(invoiceDetails.getInvBillingSignatory());
                arInvoice.setInvSignatoryTitle(invoiceDetails.getInvSignatoryTitle());
                arInvoice.setInvShipToAddress(invoiceDetails.getInvShipToAddress());
                arInvoice.setInvShipToContact(invoiceDetails.getInvShipToContact());
                arInvoice.setInvShipToAltContact(invoiceDetails.getInvShipToAltContact());
                arInvoice.setInvShipToPhone(invoiceDetails.getInvShipToPhone());
                arInvoice.setInvShipDate(invoiceDetails.getInvShipDate());
                arInvoice.setInvLvFreight(invoiceDetails.getInvLvFreight());
                arInvoice.setInvLastModifiedBy(invoiceDetails.getInvLastModifiedBy());
                arInvoice.setInvDateLastModified(invoiceDetails.getInvDateLastModified());
                arInvoice.setInvReasonForRejection(null);
                arInvoice.setInvLvShift(invoiceDetails.getInvLvShift());
                arInvoice.setInvDebitMemo(invoiceDetails.getInvDebitMemo());
                arInvoice.setInvSubjectToCommission(invoiceDetails.getInvSubjectToCommission());
                arInvoice.setInvClientPO(invoiceDetails.getInvClientPO());
                arInvoice.setInvEffectivityDate(invoiceDetails.getInvEffectivityDate());
                arInvoice.setInvReceiveDate(invoiceDetails.getInvReceiveDate());
            }

            arInvoice.setInvDocumentType(invoiceDetails.getInvDocumentType());
            arInvoice.setReportParameter(invoiceDetails.getReportParameter());

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(paymentName, companyCode);
            arInvoice.setAdPaymentTerm(adPaymentTerm);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(currencyName, companyCode);
            arInvoice.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(taxCode, companyCode);
            arInvoice.setArTaxCode(arTaxCode);

            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(withholdingTaxCode, companyCode);
            arInvoice.setArWithholdingTaxCode(arWithholdingTaxCode);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(customerCode, companyCode);
            arInvoice.setArCustomer(arCustomer);

            setInvoiceBatch(invoiceBatchName, branchCode, companyCode, arInvoice);

            tagSalesPerson(salesPersonCode, companyCode, arInvoice);

            double amountDue = 0;
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            if (isRecalculate) {

                // remove all invoice line items
                Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();
                Iterator i = arInvoiceLineItems.iterator();
                while (i.hasNext()) {
                    clearInvoiceLineItem(companyCode, i);
                }
                clearOtherRecords(arInvoice);

                // add new invoice line item and distribution record
                double totalTax = 0d;
                double totalLine = 0d;

                double totalInterestException = 0d;
                double totalPaymentTermException = 0d;
                double totalSalesOrderDebitAmount = 0d;
                double totalDownpayment = arInvoice.getInvDownPayment();
                double totalNetDiscountAmount = 0;
                double totalTaxDiscountAmount = 0;

                short precisionUnit = commonData.getGlFcPrecisionUnit(companyCode);
                // Make sure Global AD Preference apply validation to avoid this fall back value.
                String centralWarehouse = !adPreference.getPrfInvCentralWarehouse().equals("") && adPreference.getPrfInvCentralWarehouse() != null ? adPreference.getPrfInvCentralWarehouse() : "POM WAREHOUSE LOCATION";
                LocalAdBranch centralWarehouseBranchCode = adBranchHome.findByBrName(centralWarehouse, companyCode);
                if (centralWarehouseBranchCode == null) {
                    throw new Exception();
                }

                i = invoiceLines.iterator();
                LocalInvItemLocation invItemLocation = null;
                while (i.hasNext()) {
                    ArModInvoiceLineItemDetails mIliDetails = (ArModInvoiceLineItemDetails) i.next();
                    // Get Item Location
                    invItemLocation = getItemLocation(companyCode, mIliDetails);
                    // Is Allowed Prior Date Transaction
                    isAllowedPriorDate(companyCode, arInvoice, adPreference, centralWarehouse, centralWarehouseBranchCode, invItemLocation);
                    // Attached Invoice Line Item to Invoice Class
                    LocalArInvoiceLineItem arInvoiceLineItem = this.addArIliEntry(mIliDetails, arInvoice, invItemLocation, companyCode);
                    // Calculate Net Discount for Invoice Line
                    double netDiscountAmount = EJBCommon.calculateNetAmount(mIliDetails.getIliTotalDiscount(), mIliDetails.getIliTax(), arTaxCode.getTcRate(), arTaxCode.getTcType(), precisionUnit);
                    double taxDiscountAmount = EJBCommon.calculateTaxAmount(mIliDetails.getIliTotalDiscount(), mIliDetails.getIliTax(), arTaxCode.getTcRate(), arTaxCode.getTcType(), netDiscountAmount, precisionUnit);
                    totalNetDiscountAmount += netDiscountAmount;
                    totalTaxDiscountAmount += taxDiscountAmount;
                    // add cost of sales distribution and inventory
                    double unitCost = 0d;
                    try {
                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), centralWarehouse, centralWarehouseBranchCode.getBrCode(), companyCode);
                        // Check if Remaining Value and Remaining Quantity is equal to zero
                        if (invItemLocation.getInvItem().getIiNonInventoriable() == (byte) 0 && invCosting.getCstRemainingQuantity() <= 0 && invCosting.getCstRemainingValue() <= 0) {
                            HashMap criteria = new HashMap();
                            criteria.put(EJBCommon.itemName, invItemLocation.getInvItem().getIiName());
                            criteria.put(EJBCommon.location, centralWarehouse);
                            ArrayList branchList = new ArrayList();
                            AdBranchDetails mdetails = new AdBranchDetails();
                            mdetails.setBrCode(branchCode);
                            branchList.add(mdetails);
                            ejbRIC.executeInvFixItemCosting(criteria, branchList, companyCode);
                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), centralWarehouse, centralWarehouseBranchCode.getBrCode(), companyCode);
                        }

                        if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.Average)) {
                            if (invCosting.getCstRemainingQuantity() <= 0) {
                                unitCost = invItemLocation.getInvItem().getIiUnitCost();
                            } else {
                                unitCost = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                                if (unitCost <= 0) {
                                    unitCost = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                                }
                            }
                        }
                        if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.FIFO)) {
                            unitCost = this.getInvFifoCost(arInvoice.getInvDate(), invItemLocation.getIlCode(), arInvoiceLineItem.getIliQuantity(), arInvoiceLineItem.getIliUnitPrice(), false, centralWarehouseBranchCode.getBrCode(), companyCode);
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.Standard)) {
                            unitCost = invItemLocation.getInvItem().getIiUnitCost();
                        }
                    }
                    catch (FinderException ex) {
                        unitCost = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double quantitySold = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                    LocalAdBranchItemLocation adBranchItemLocation = null;
                    try {
                        lineNumberError = arInvoiceLineItem.getIliLine();
                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), branchCode, companyCode);
                    }
                    catch (FinderException ex) {
                    }

                    if (adPreference.getPrfArAutoComputeCogs() == EJBCommon.TRUE && arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {
                        if (adBranchItemLocation != null) {
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.COST_OF_GOODS_SOLD, EJBCommon.TRUE, unitCost * quantitySold, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arInvoice, branchCode, companyCode);
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.INVENTORY, EJBCommon.FALSE, unitCost * quantitySold, adBranchItemLocation.getBilCoaGlInventoryAccount(), arInvoice, branchCode, companyCode);
                        } else {
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.COST_OF_GOODS_SOLD, EJBCommon.TRUE, unitCost * quantitySold, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, branchCode, companyCode);
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.INVENTORY, EJBCommon.FALSE, unitCost * quantitySold, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, branchCode, companyCode);
                        }
                        // add quantity to item location committed quantity
                        double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                        invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);
                    }

                    double invoiceLine = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arInvoiceLineItem.getIliAmount(), companyCode);

                    // This process was due to the DEFERRED SALES ACCOUNT
                    String distributionRecordClass = getDistributionRecordClass(companyCode, adBranchItemLocation);

                    // add inventory sale distributions
                    if (adBranchItemLocation != null) {
                        // this will trigger by services(DEBIT)
                        if (adBranchItemLocation.getInvItemLocation().getInvItem().getIiServices() == EJBCommon.TRUE) {
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), distributionRecordClass, EJBCommon.TRUE, invoiceLine, adBranchItemLocation.getBilCoaGlSalesAccount(), arInvoice, branchCode, companyCode);
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.OTHER, EJBCommon.FALSE, invoiceLine, adBranchItemLocation.getBilCoaGlSalesReturnAccount(), arInvoice, branchCode, companyCode);
                            totalSalesOrderDebitAmount += invoiceLine;
                        } else {
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), distributionRecordClass, EJBCommon.FALSE, invoiceLine, adBranchItemLocation.getBilCoaGlSalesAccount(), arInvoice, branchCode, companyCode);
                        }
                    } else {
                        // this will trigger by services(DEBIT)
                        if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiServices() == EJBCommon.TRUE) {
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), distributionRecordClass, EJBCommon.TRUE, invoiceLine, arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arInvoice, branchCode, companyCode);
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.OTHER, EJBCommon.FALSE, invoiceLine, arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesReturnAccount(), arInvoice, branchCode, companyCode);
                            totalSalesOrderDebitAmount += arInvoiceLineItem.getIliAmount();
                        } else {
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), distributionRecordClass, EJBCommon.FALSE, invoiceLine, arInvoiceLineItem.getInvItemLocation().getIlGlCoaSalesAccount(), arInvoice, branchCode, companyCode);
                        }
                    }
                    totalLine += arInvoiceLineItem.getIliAmount();
                    if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiInterestException() == EJBCommon.TRUE) {
                        totalInterestException += arInvoiceLineItem.getIliAmount();
                    }
                    if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiPaymentTermException() == EJBCommon.TRUE) {
                        totalPaymentTermException += arInvoiceLineItem.getIliAmount();
                    }
                    totalTax += arInvoiceLineItem.getIliTaxAmount();
                }

                // add tax distribution if necessary
                double convertedTotalTax = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), totalTax, companyCode);

                double convertedTotalLine = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), totalLine, companyCode);

                double covertedTotalDownPayment = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), totalDownpayment, companyCode);

                if (!arTaxCode.getTcType().equals(EJBCommon.NONE) && !arTaxCode.getTcType().equals(EJBCommon.EXEMPT)) {
                    if (arTaxCode.getTcInterimAccount() == null) {
                        // add branch tax code
                        LocalAdBranchArTaxCode adBranchTaxCode = null;
                        try {
                            adBranchTaxCode = adBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(arInvoice.getArTaxCode().getTcCode(), branchCode, companyCode);
                        }
                        catch (FinderException ex) {
                        }
                        if (adBranchTaxCode != null) {
                            this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.TAX, EJBCommon.FALSE, convertedTotalTax, adBranchTaxCode.getBtcGlCoaTaxCode(), null, arInvoice, branchCode, companyCode);
                        } else {

                            this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.TAX, EJBCommon.FALSE, convertedTotalTax, arTaxCode.getGlChartOfAccount().getCoaCode(), null, arInvoice, branchCode, companyCode);
                        }
                    } else {
                        this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.DEFERRED_TAX, EJBCommon.FALSE, convertedTotalTax, arTaxCode.getTcInterimAccount(), null, arInvoice, branchCode, companyCode);
                    }
                }

                // add unearned interest
                addUnearnedInterest(invoiceBatchName, branchCode, companyCode, arInvoice, adPaymentTerm, adPreference, totalTax, totalLine, totalInterestException, totalDownpayment);

                // add wtax distribution if necessary
                double withholdingTaxAmount = calculateWithholdingTax(branchCode, companyCode, arInvoice, arWithholdingTaxCode, adPreference, totalLine);

                // add payment discount if necessary
                double discountAmount = 0d;
                if (adPaymentTerm.getPytDiscountOnInvoice() == EJBCommon.TRUE) {
                    Collection adPaymentSchedules = adPaymentScheduleHome.findByPytCode(adPaymentTerm.getPytCode(), companyCode);
                    ArrayList adPaymentScheduleList = new ArrayList(adPaymentSchedules);
                    LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) adPaymentScheduleList.get(0);

                    Collection adDiscounts = adDiscountHome.findByPsCode(adPaymentSchedule.getPsCode(), companyCode);
                    ArrayList adDiscountList = new ArrayList(adDiscounts);
                    LocalAdDiscount adDiscount = (LocalAdDiscount) adDiscountList.get(0);

                    double rate = adDiscount.getDscDiscountPercent();
                    discountAmount = (totalLine + totalTax) * (rate / 100d);

                    double convertedDiscountAmount = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), discountAmount, companyCode);

                    this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.DISCOUNT, EJBCommon.TRUE, convertedDiscountAmount, adPaymentTerm.getGlChartOfAccount().getCoaCode(), null, arInvoice, branchCode, companyCode);
                }
                amountDue = totalLine + totalTax - withholdingTaxAmount - discountAmount - totalNetDiscountAmount - totalTaxDiscountAmount;

                // add receivable distribution
                try {
                    LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), branchCode, companyCode);
                    double convertedAmountDue = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), amountDue, companyCode);
                    this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.RECEIVABLE, EJBCommon.TRUE, convertedAmountDue, adBranchCustomer.getBcstGlCoaReceivableAccount(), arInvoice, branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                if (totalNetDiscountAmount > 0) {
                    LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(branchCode);
                    Integer discountCoa = null;
                    try {
                        List<LocalGlChartOfAccount> defaultCoa = new ArrayList(glChartOfAccountHome.findHoCoaAllByCoaCategory(EJBCommon.DISCOUNT, adBranch.getBrName(), companyCode));
                        for (LocalGlChartOfAccount chartOfAccount : defaultCoa) {
                            // Get only discount with REVENUE class type
                            if (chartOfAccount.getCoaAccountType().equals(EJBCommon.REVENUE)) {
                                discountCoa = chartOfAccount.getCoaCode();
                            }
                        }
                    }
                    catch (FinderException ex) {
                        throw new GlobalBranchAccountNumberInvalidException();
                    }

                    // ADD discount JOURNAL
                    double convertedTotalDiscount = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), totalNetDiscountAmount, companyCode);

                    this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.DISCOUNT, EJBCommon.TRUE, convertedTotalDiscount, discountCoa, arInvoice, branchCode, companyCode);

                    // ADD invoiceTax discount JOURNAL
                    double convertedTotalInvoiceTaxDiscount = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), totalTaxDiscountAmount, companyCode);

                    LocalAdBranchArTaxCode adBranchTaxCode = null;
                    try {
                        adBranchTaxCode = adBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(arInvoice.getArTaxCode().getTcCode(), branchCode, companyCode);
                    }
                    catch (FinderException ex) {
                    }

                    if (adBranchTaxCode != null) {
                        this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.TAX, EJBCommon.TRUE, convertedTotalInvoiceTaxDiscount, adBranchTaxCode.getBtcGlCoaTaxCode(), null, arInvoice, branchCode, companyCode);
                    } else {

                        this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.TAX, EJBCommon.TRUE, convertedTotalInvoiceTaxDiscount, arTaxCode.getGlChartOfAccount().getCoaCode(), null, arInvoice, branchCode, companyCode);
                    }

                }
                // compute invoice amount due set invoice amount due inclusive interest
                arInvoice.setInvAmountDue(amountDue);
                // remove all payment schedule
                removeAllPaymentSchedule(arInvoice);

                // create invoice payment schedule
                double totalPaymentSchedule = 0d;
                double totalPaymentScheduleInterest = totalLine + totalTax - totalDownpayment;

                GregorianCalendar gcPrevDateDue = new GregorianCalendar();
                GregorianCalendar gcDateDue = new GregorianCalendar();
                gcPrevDateDue.setTime(arInvoice.getInvEffectivityDate());
                Collection adPaymentSchedules = adPaymentTerm.getAdPaymentSchedules();
                boolean first = true;
                i = adPaymentSchedules.iterator();
                while (i.hasNext()) {
                    LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) i.next();
                    // get date due
                    switch (arInvoice.getAdPaymentTerm().getPytScheduleBasis()) {
                        case EJBCommon.MONTHLY:
                            gcDateDue = gcPrevDateDue;
                            gcDateDue.add(Calendar.MONTH, 1);
                            gcPrevDateDue = gcDateDue;
                            break;
                        case EJBCommon.BI_MONTHLY:
                            gcDateDue = gcPrevDateDue;
                            if (gcPrevDateDue.get(Calendar.MONTH) != 1) {
                                if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 31 && gcPrevDateDue.get(Calendar.DATE) > 15 && gcPrevDateDue.get(Calendar.DATE) < 31) {
                                    gcDateDue.add(Calendar.DATE, 16);
                                } else {
                                    gcDateDue.add(Calendar.DATE, 15);
                                }
                            } else if (gcPrevDateDue.get(Calendar.MONTH) == 1) {
                                if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28 && gcPrevDateDue.get(Calendar.DATE) == 14) {
                                    gcDateDue.add(Calendar.DATE, 14);
                                } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28 && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 28) {
                                    gcDateDue.add(Calendar.DATE, 13);
                                } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 29 && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 29) {
                                    gcDateDue.add(Calendar.DATE, 14);
                                } else {
                                    gcDateDue.add(Calendar.DATE, 15);
                                }
                            }
                            gcPrevDateDue = gcDateDue;
                            break;
                        default:
                            gcDateDue.setTime(arInvoice.getInvEffectivityDate());
                            gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());
                            break;
                    }

                    if (first == true) {
                        LocalArInvoicePaymentSchedule arInvoicePaymentScheduleDownPayment;
                        if (arInvoice.getInvDownPayment() > 0) {
                            arInvoicePaymentScheduleDownPayment = arInvoicePaymentScheduleHome.create(arInvoice.getInvEffectivityDate(), (short) 0, arInvoice.getInvDownPayment(), 0d, EJBCommon.FALSE, (short) 0, arInvoice.getInvEffectivityDate(), 0d, 0d, companyCode);
                            arInvoice.addArInvoicePaymentSchedule(arInvoicePaymentScheduleDownPayment);
                        }

                        if (totalPaymentTermException > 0) {
                            arInvoicePaymentScheduleDownPayment = arInvoicePaymentScheduleHome.create(arInvoice.getInvEffectivityDate(), (short) 0, totalPaymentTermException, 0d, EJBCommon.FALSE, (short) 0, arInvoice.getInvEffectivityDate(), 0d, 0d, companyCode);
                            arInvoice.addArInvoicePaymentSchedule(arInvoicePaymentScheduleDownPayment);
                        }
                        first = false;
                    }

                    // create a payment schedule
                    double paymentScheduleAmount = 0;
                    double paymentScheduleInterest = 0;
                    double paymentSchedulePrincipal = 0;

                    // if last payment schedule subtract to avoid rounding difference error
                    if (i.hasNext()) {
                        paymentScheduleAmount = EJBCommon.roundIt((adPaymentSchedule.getPsRelativeAmount() / adPaymentTerm.getPytBaseAmount()) * (arInvoice.getInvAmountDue() - arInvoice.getInvDownPayment()), precisionUnit);
                        paymentScheduleInterest = EJBCommon.roundIt((totalPaymentScheduleInterest) * (adPaymentTerm.getPytMonthlyInterestRate() / 100), precisionUnit);
                        paymentSchedulePrincipal = -(paymentScheduleAmount) + paymentScheduleInterest;
                    } else {
                        paymentScheduleAmount = (arInvoice.getInvAmountDue() - arInvoice.getInvDownPayment()) - totalPaymentSchedule;
                        paymentScheduleInterest = EJBCommon.roundIt((totalPaymentScheduleInterest) * (adPaymentTerm.getPytMonthlyInterestRate() / 100), precisionUnit);
                        paymentSchedulePrincipal = -(paymentScheduleAmount) + paymentScheduleInterest;
                    }
                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arInvoicePaymentScheduleHome.create(gcDateDue.getTime(), adPaymentSchedule.getPsLineNumber(), paymentScheduleAmount, 0d, EJBCommon.FALSE, (short) 0, gcDateDue.getTime(), 0d, 0d, companyCode);
                    arInvoicePaymentSchedule.setIpsInterestDue(paymentScheduleInterest);
                    arInvoice.addArInvoicePaymentSchedule(arInvoicePaymentSchedule);
                    totalPaymentSchedule += paymentScheduleAmount;
                    totalPaymentScheduleInterest += paymentSchedulePrincipal;
                }
            } else {
                Iterator i = invoiceLines.iterator();
                LocalInvItemLocation invItemLocation;
                while (i.hasNext()) {
                    ArModInvoiceLineItemDetails mIliDetails = (ArModInvoiceLineItemDetails) i.next();
                    invItemLocation = getItemLocation(companyCode, mIliDetails);
                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }
                }
            }

            // insufficient stock checking
            if (adPreference.getPrfArCheckInsufficientStock() == EJBCommon.TRUE && !isDraft) {
                boolean hasInsufficientItems = false;
                StringBuilder insufficientItems = new StringBuilder();
                Collection arInvoicelineItems = arInvoiceLineItemHome.findByInvCode(arInvoice.getInvCode(), companyCode);
                for (Object arInvoicelineItem : arInvoicelineItems) {
                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) arInvoicelineItem;
                    if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.TRUE) {
                        continue;
                    }
                    LocalInvCosting invCosting = null;
                    double invoiceLineItemQuantity = this.convertByUomAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), Math.abs(arInvoiceLineItem.getIliQuantity()), companyCode);

                    try {
                        invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName(), arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName(), branchCode, companyCode);
                    }
                    catch (FinderException ex) {
                    }

                    double lowestQuantity = this.convertByUomAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), 1, companyCode);

                    if ((invCosting == null || invCosting.getCstRemainingQuantity() == 0 || invCosting.getCstRemainingQuantity() - invoiceLineItemQuantity <= -lowestQuantity) && arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == (byte) 0) {
                        hasInsufficientItems = true;
                        insufficientItems.append(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName()).append(", ");
                    }
                }
                if (hasInsufficientItems) {
                    throw new GlobalRecordInvalidException(insufficientItems.substring(0, insufficientItems.lastIndexOf(",")));
                }
            }

            // generate approval status
            String invoiceApprovalStatus = null;
            if (!isDraft) {
                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);
                // validate if total amount + unposted invoices' amount + current balance + unposted receipts's amount does not exceed customer's credit limit
                double balance = 0;
                LocalAdApprovalDocument adInvoiceApprovalDocument = adApprovalDocumentHome.findByAdcType(EJBCommon.AR_INVOICE, companyCode);
                if (arCustomer.getCstCreditLimit() > 0) {
                    balance = computeTotalBalance(invoiceDetails.getInvCode(), customerCode, companyCode);
                    balance += amountDue;
                    if (arCustomer.getCstCreditLimit() < balance && (adApproval.getAprEnableArInvoice() == EJBCommon.FALSE || (adApproval.getAprEnableArInvoice() == EJBCommon.TRUE && adInvoiceApprovalDocument.getAdcEnableCreditLimitChecking() == EJBCommon.FALSE))) {
                        throw new ArINVAmountExceedsCreditLimitException();
                    }
                }
                // find overdue invoices
                Collection arOverdueInvoices = arInvoicePaymentScheduleHome.findOverdueIpsByInvDateAndCstCustomerCode(arInvoice.getInvDate(), customerCode, companyCode);
                // check if ar invoice approval is enabled
                if (adApproval.getAprEnableArInvoice() == EJBCommon.FALSE || (arCustomer.getCstCreditLimit() > balance && arOverdueInvoices.size() == 0)) {
                    invoiceApprovalStatus = EJBCommon.NOT_APPLICABLE;
                } else {
                    // check if invoice is self approved
                    LocalAdUser adUser = adUserHome.findByUsrName(invoiceDetails.getInvLastModifiedBy(), companyCode);
                    invoiceApprovalStatus = arApprovalController.getApprovalStatus(adUser.getUsrDept(), invoiceDetails.getInvLastModifiedBy(), adUser.getUsrDescription(), EJBCommon.AR_INVOICE, arInvoice.getInvCode(), arInvoice.getInvNumber(), arInvoice.getInvDate(), branchCode, companyCode);
                }
            }

            if (invoiceApprovalStatus != null && invoiceApprovalStatus.equals(EJBCommon.NOT_APPLICABLE) && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                this.executeArInvPost(arInvoice.getInvCode(), arInvoice.getInvLastModifiedBy(), branchCode, companyCode);
            }

            // set invoice approval status
            arInvoice.setInvApprovalStatus(invoiceApprovalStatus);
            return arInvoice.getInvCode();

        }
        catch (GlobalRecordAlreadyDeletedException | ArInvDuplicateUploadNumberException |
               GlobalMiscInfoIsRequiredException | GlobalExpiryDateNotFoundException | GlobalRecordInvalidException |
               AdPRFCoaGlVarianceAccountNotFoundException | GlobalInventoryDateException |
               GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
               GlJREffectiveDateNoPeriodExistException | GlobalInvItemLocationNotFoundException |
               GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
               GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
               GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
               ArINVAmountExceedsCreditLimitException | GlobalPaymentTermInvalidException |
               GlobalConversionDateNotExistException | GlobalDocumentNumberNotUniqueException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (GlobalBranchAccountNumberInvalidException ex) {
            // Retrieve Error a Line Number
            ex.setLineNumberError(lineNumberError);
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }


    public Integer saveArInvSolEntry(ArInvoiceDetails details, String paymentName, String taxCode, String withholdingTaxCode, String currencyName, String customerCode, String invoiceBatchName, ArrayList solList, boolean isDraft, String salesPersonCode, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalTransactionAlreadyLockedException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, ArINVAmountExceedsCreditLimitException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException, ArInvDuplicateUploadNumberException {

        Debug.print("ArInvoiceEntryControllerBean saveArInvSolEntry");
        LocalArInvoice arInvoice = null;
        Date txnStartDate = new Date();
        int lineNumberError = 0;
        try {

            // validate if invoice is already deleted
            arInvoice = isInvoiceDeleted(details, arInvoice);

            // validate if invoice is already posted, void, approved or pending
            if (details.getInvCode() != null) {
                validateInvoice(arInvoice);
            }

            // invoice void
            if (isInvoiceVoid(details, arInvoice)) {
                return arInvoice.getInvCode();
            }

            // validate if document number is unique document number is automatic then set next sequence
            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
            if (details.getInvCode() == null) {
                String documentType = details.getInvDocumentType();
                try {
                    if (documentType != null) {
                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode);
                    } else {
                        documentType = EJBCommon.AR_INVOICE;
                    }
                }
                catch (FinderException ex) {
                    documentType = EJBCommon.AR_INVOICE;
                }

                try {
                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode);
                }
                catch (FinderException ex) {
                }
                try {
                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);
                }
                catch (FinderException ex) {

                }

                LocalArInvoice arExistingInvoice = null;
                try {
                    arExistingInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(details.getInvNumber(), EJBCommon.FALSE, branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                if (arExistingInvoice != null) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getInvNumber() == null || details.getInvNumber().trim().length() == 0)) {
                    setupDocumentSequenceAssignment(details, branchCode, companyCode, adBranchDocumentSequenceAssignment, adDocumentSequenceAssignment);
                }

                // Confirm if Upload Number have duplicate
                isUploadNumberHaveDuplicate(details, companyCode);
            } else {
                LocalArInvoice arExistingInvoice = null;
                try {
                    arExistingInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(details.getInvNumber(), EJBCommon.FALSE, branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                if (arExistingInvoice != null && !arExistingInvoice.getInvCode().equals(details.getInvCode())) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (arInvoice.getInvNumber() != details.getInvNumber() && (details.getInvNumber() == null || details.getInvNumber().trim().length() == 0)) {
                    details.setInvNumber(arInvoice.getInvNumber());
                }

                // Confirm if Upload Number have duplicate
                isUploadNumberDuplicate(details, companyCode, arExistingInvoice);
            }

            // validate if conversion date exists
            isConversionDateExists(details, currencyName, companyCode);

            // validate if payment term has at least one payment schedule
            if (adPaymentTermHome.findByPytName(paymentName, companyCode).getAdPaymentSchedules().isEmpty()) {
                throw new GlobalPaymentTermInvalidException();
            }

            // lock sales order
            LocalArSalesOrder arExistingSalesOrder = null;
            try {
                arExistingSalesOrder = arSalesOrderHome.findBySoDocumentNumberAndBrCode(details.getInvSoNumber(), branchCode, companyCode);
                if ((arInvoice == null || (arInvoice != null && arInvoice.getInvSoNumber() != null && (!arInvoice.getInvSoNumber().equals(details.getInvSoNumber()))))) {
                    if (arExistingSalesOrder.getSoLock() == EJBCommon.TRUE) {
                        throw new GlobalTransactionAlreadyLockedException();
                    }
                    arExistingSalesOrder.setSoLock(EJBCommon.TRUE);
                }
            }
            catch (FinderException ex) {
            }

            // used in checking if invoice should re-generate distribution records and re-calculate taxes
            boolean isRecalculate = true;
            // create invoice
            if (details.getInvCode() == null) {
                arInvoice = createInvoice(details, branchCode, companyCode);
            } else {
                // check if critical fields are changed
                if ((arInvoice.getInvSoNumber() != null && !arInvoice.getInvSoNumber().equals(details.getInvSoNumber())) || !arInvoice.getArTaxCode().getTcName().equals(taxCode) || !arInvoice.getArWithholdingTaxCode().getWtcName().equals(withholdingTaxCode) || !arInvoice.getArCustomer().getCstCustomerCode().equals(customerCode) || !arInvoice.getAdPaymentTerm().getPytName().equals(paymentName) || !arInvoice.getInvDate().equals(details.getInvDate()) || !arInvoice.getInvEffectivityDate().equals(details.getInvEffectivityDate()) || arInvoice.getInvDownPayment() != details.getInvDownPayment() || solList.size() != arInvoice.getArSalesOrderInvoiceLines().size()) {
                    isRecalculate = true;
                } else if (solList.size() == arInvoice.getArSalesOrderInvoiceLines().size()) {
                    Iterator solIter = arInvoice.getArSalesOrderInvoiceLines().iterator();
                    Iterator solListIter = solList.iterator();
                    while (solIter.hasNext()) {
                        LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) solIter.next();
                        LocalArSalesOrderLine arSalesOrderLine = arSalesOrderInvoiceLine.getArSalesOrderLine();
                        ArModSalesOrderLineDetails mdetails = (ArModSalesOrderLineDetails) solListIter.next();
                        if (!arSalesOrderLine.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getSolIiName()) || !arSalesOrderLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getSolLocName()) || !arSalesOrderLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getSolUomName()) || arSalesOrderInvoiceLine.getSilQuantityDelivered() != mdetails.getSolQuantityDelivered() || arSalesOrderInvoiceLine.getSilTax() != mdetails.getSolTax() || arSalesOrderLine.getSolUnitPrice() != mdetails.getSolUnitPrice()) {
                            isRecalculate = true;
                            break;
                        }
                    }
                }
                arInvoice.setInvDownPayment(details.getInvDownPayment());
                arInvoice.setInvDocumentType(details.getInvDocumentType());
                arInvoice.setInvType(details.getInvType());
                arInvoice.setInvDescription(details.getInvDescription());
                arInvoice.setInvDate(details.getInvDate());
                arInvoice.setInvNumber(details.getInvNumber());
                arInvoice.setInvMemo(details.getInvMemo());
                arInvoice.setInvReferenceNumber(details.getInvReferenceNumber());
                arInvoice.setInvUploadNumber(details.getInvUploadNumber());
                arInvoice.setInvConversionDate(details.getInvConversionDate());
                arInvoice.setInvConversionRate(details.getInvConversionRate());
                arInvoice.setInvPreviousReading(arInvoice.getInvPreviousReading());
                arInvoice.setInvPresentReading(arInvoice.getInvPresentReading());
                arInvoice.setInvBillToAddress(details.getInvBillToAddress());
                arInvoice.setInvBillToContact(details.getInvBillToContact());
                arInvoice.setInvBillToAltContact(details.getInvBillToAltContact());
                arInvoice.setInvBillToPhone(details.getInvBillToPhone());
                arInvoice.setInvBillingHeader(details.getInvBillingHeader());
                arInvoice.setInvBillingFooter(details.getInvBillingFooter());
                arInvoice.setInvBillingHeader2(details.getInvBillingHeader2());
                arInvoice.setInvBillingFooter2(details.getInvBillingFooter2());
                arInvoice.setInvBillingSignatory(details.getInvBillingSignatory());
                arInvoice.setInvSignatoryTitle(details.getInvSignatoryTitle());
                arInvoice.setInvShipToAddress(details.getInvShipToAddress());
                arInvoice.setInvShipToContact(details.getInvShipToContact());
                arInvoice.setInvShipToAltContact(details.getInvShipToAltContact());
                arInvoice.setInvShipToPhone(details.getInvShipToPhone());
                arInvoice.setInvShipDate(details.getInvShipDate());
                arInvoice.setInvLvFreight(details.getInvLvFreight());
                arInvoice.setInvLastModifiedBy(details.getInvLastModifiedBy());
                arInvoice.setInvDateLastModified(details.getInvDateLastModified());
                arInvoice.setInvReasonForRejection(null);
                arInvoice.setInvLvShift(details.getInvLvShift());
                arInvoice.setInvSoNumber(details.getInvSoNumber());
                arInvoice.setInvJoNumber(details.getInvJoNumber());
                arInvoice.setInvDebitMemo(details.getInvDebitMemo());
                arInvoice.setInvSubjectToCommission(details.getInvSubjectToCommission());
                arInvoice.setInvClientPO(details.getInvClientPO());
                arInvoice.setInvEffectivityDate(details.getInvEffectivityDate());
            }
            arInvoice.setInvDocumentType(details.getInvDocumentType());
            arInvoice.setReportParameter(details.getReportParameter());

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(paymentName, companyCode);
            arInvoice.setAdPaymentTerm(adPaymentTerm);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(currencyName, companyCode);
            arInvoice.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(taxCode, companyCode);
            arInvoice.setArTaxCode(arTaxCode);

            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(withholdingTaxCode, companyCode);
            arInvoice.setArWithholdingTaxCode(arWithholdingTaxCode);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(customerCode, companyCode);
            arInvoice.setArCustomer(arCustomer);

            tagSalesPerson(salesPersonCode, companyCode, arInvoice);
            setInvoiceBatch(invoiceBatchName, branchCode, companyCode, arInvoice);

            double amountDue = 0;
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            if (isRecalculate) {
                // remove all invoice line items
                Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();
                Iterator i = arInvoiceLineItems.iterator();
                while (i.hasNext()) {
                    clearInvoiceLineItem(companyCode, i);
                }
                // remove all invoice lines
                Collection arInvoiceLines = arInvoice.getArInvoiceLines();
                i = arInvoiceLines.iterator();
                while (i.hasNext()) {
                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) i.next();
                    i.remove();
                    em.remove(arInvoiceLine);
                }
                // remove all sales order lines
                Collection arSalesOrderInvoiceLines = arInvoice.getArSalesOrderInvoiceLines();
                i = arSalesOrderInvoiceLines.iterator();
                while (i.hasNext()) {
                    LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) i.next();
                    // release previous sales order
                    if (!arExistingSalesOrder.getSoDocumentNumber().equals(arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().getSoDocumentNumber())) {
                        arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().setSoLock(EJBCommon.FALSE);
                    }
                    i.remove();
                    em.remove(arSalesOrderInvoiceLine);
                }
                // remove all distribution records
                Collection arDistributionRecords = arInvoice.getArDistributionRecords();
                i = arDistributionRecords.iterator();
                while (i.hasNext()) {
                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();
                    i.remove();
                    em.remove(arDistributionRecord);
                }
                // add new sales order line and distribution record
                double totalTax = 0d;
                double totalLine = 0d;
                double totalSalesOrderDebitAmount = 0d;
                double totalDownpayment = arInvoice.getInvDownPayment();
                i = solList.iterator();
                LocalInvItemLocation invItemLocation;
                while (i.hasNext()) {
                    ArModSalesOrderLineDetails mSolDetails = (ArModSalesOrderLineDetails) i.next();
                    try {
                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mSolDetails.getSolLocName(), mSolDetails.getSolIiName(), companyCode);
                    }
                    catch (FinderException ex) {
                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mSolDetails.getSolLine()));
                    }
                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }
                    LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = this.addArSolEntry(mSolDetails, arInvoice, companyCode);
                    LocalArSalesOrderLine arSalesOrderLine = arSalesOrderInvoiceLine.getArSalesOrderLine();

                    // add cost of sales distribution and inventory
                    double unitCost = invItemLocation.getInvItem().getIiUnitCost();

                    try {
                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

                        // check if rmning vl is not zero and rmng qty is 0
                        if (invCosting.getCstRemainingQuantity() <= 0) {
                            HashMap criteria = new HashMap();
                            criteria.put("itemName", invItemLocation.getInvItem().getIiName());
                            criteria.put("location", invItemLocation.getInvLocation().getLocName());
                            ArrayList branchList = new ArrayList();
                            AdBranchDetails mdetails = new AdBranchDetails();
                            mdetails.setBrCode(branchCode);
                            branchList.add(mdetails);
                            ejbRIC.executeInvFixItemCosting(criteria, branchList, companyCode);
                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        }

                        // dist
                        if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.Average)) {
                            unitCost = invCosting.getCstRemainingQuantity() <= 0 ? unitCost : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                        }

                        if (unitCost <= 0) {
                            unitCost = invItemLocation.getInvItem().getIiUnitCost();
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.FIFO)) {
                            unitCost = invCosting.getCstRemainingQuantity() == 0 ? unitCost : this.getInvFifoCost(arInvoice.getInvDate(), invItemLocation.getIlCode(), arSalesOrderInvoiceLine.getSilQuantityDelivered(), arSalesOrderInvoiceLine.getArSalesOrderLine().getSolUnitPrice(), false, branchCode, companyCode);
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.Standard)) {
                            unitCost = invItemLocation.getInvItem().getIiUnitCost();
                        }

                    }
                    catch (FinderException ex) {

                        unitCost = arSalesOrderLine.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double quantitySold = this.convertByUomFromAndItemAndQuantity(arSalesOrderLine.getInvUnitOfMeasure(), arSalesOrderLine.getInvItemLocation().getInvItem(), arSalesOrderInvoiceLine.getSilQuantityDelivered(), companyCode);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        lineNumberError = arSalesOrderLine.getSolLine();
                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arSalesOrderLine.getInvItemLocation().getIlCode(), branchCode, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    if (adPreference.getPrfArAutoComputeCogs() == EJBCommon.TRUE && arSalesOrderLine.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {
                        lineNumberError = arInvoice.getArDrNextLine();
                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.COST_OF_GOODS_SOLD, EJBCommon.TRUE, unitCost * quantitySold, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.INVENTORY, EJBCommon.FALSE, unitCost * quantitySold, adBranchItemLocation.getBilCoaGlInventoryAccount(), arInvoice, branchCode, companyCode);

                        } else {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.COST_OF_GOODS_SOLD, EJBCommon.TRUE, unitCost * quantitySold, arSalesOrderLine.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.INVENTORY, EJBCommon.FALSE, unitCost * quantitySold, arSalesOrderLine.getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, branchCode, companyCode);
                        }
                    }

                    // add quantity to item location committed quantity

                    double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arSalesOrderLine.getInvUnitOfMeasure(), arSalesOrderLine.getInvItemLocation().getInvItem(), arSalesOrderInvoiceLine.getSilQuantityDelivered(), companyCode);
                    invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);

                    // add inventory sale distributions
                    double salesOrderInvoiceLine = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arSalesOrderInvoiceLine.getSilAmount(), companyCode);

                    if (adBranchItemLocation != null) {
                        // this will trigger by services(DEBIT)
                        if (adBranchItemLocation.getInvItemLocation().getInvItem().getIiServices() == EJBCommon.TRUE) {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.REVENUE, EJBCommon.TRUE, salesOrderInvoiceLine, adBranchItemLocation.getBilCoaGlSalesAccount(), arInvoice, branchCode, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.OTHER, EJBCommon.FALSE, salesOrderInvoiceLine, adBranchItemLocation.getBilCoaGlSalesReturnAccount(), arInvoice, branchCode, companyCode);
                            totalSalesOrderDebitAmount += arSalesOrderInvoiceLine.getSilAmount();
                        } else {
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.REVENUE, EJBCommon.FALSE, salesOrderInvoiceLine, adBranchItemLocation.getBilCoaGlSalesAccount(), arInvoice, branchCode, companyCode);
                        }

                    } else {

                        // this will trigger by services(DEBIT)
                        if (arSalesOrderLine.getInvItemLocation().getInvItem().getIiServices() == EJBCommon.TRUE) {
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.REVENUE, EJBCommon.TRUE, salesOrderInvoiceLine, arSalesOrderLine.getInvItemLocation().getIlGlCoaSalesAccount(), arInvoice, branchCode, companyCode);
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.OTHER, EJBCommon.FALSE, salesOrderInvoiceLine, arSalesOrderLine.getInvItemLocation().getIlGlCoaSalesReturnAccount(), arInvoice, branchCode, companyCode);
                            totalSalesOrderDebitAmount += arSalesOrderInvoiceLine.getSilAmount();
                        } else {
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.REVENUE, EJBCommon.FALSE, salesOrderInvoiceLine, arSalesOrderLine.getInvItemLocation().getIlGlCoaSalesAccount(), arInvoice, branchCode, companyCode);
                        }
                    }
                    totalLine += arSalesOrderInvoiceLine.getSilAmount();
                    totalTax += arSalesOrderInvoiceLine.getSilTaxAmount();
                }

                // add tax distribution if necessary
                if (!arTaxCode.getTcType().equals(EJBCommon.NONE) && !arTaxCode.getTcType().equals(EJBCommon.EXEMPT)) {
                    double convertedTotalTax = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), totalTax, companyCode);
                    if (arTaxCode.getTcInterimAccount() == null) {
                        // add branch tax code
                        LocalAdBranchArTaxCode adBranchTaxCode = null;
                        try {
                            adBranchTaxCode = adBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(arInvoice.getArTaxCode().getTcCode(), branchCode, companyCode);
                        }
                        catch (FinderException ex) {
                        }

                        if (adBranchTaxCode != null) {
                            this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.TAX, EJBCommon.FALSE, convertedTotalTax, adBranchTaxCode.getBtcGlCoaTaxCode(), null, arInvoice, branchCode, companyCode);
                        } else {
                            this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.TAX, EJBCommon.FALSE, convertedTotalTax, arTaxCode.getGlChartOfAccount().getCoaCode(), null, arInvoice, branchCode, companyCode);
                        }
                    } else {
                        this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.DEFERRED_TAX, EJBCommon.FALSE, convertedTotalTax, arTaxCode.getTcInterimAccount(), null, arInvoice, branchCode, companyCode);
                    }
                }

                // add un earned interest
                double unearnedInterestAmount = 0d;
                if (arInvoice.getArCustomer().getCstAutoComputeInterest() == EJBCommon.TRUE && arInvoice.getArCustomer().getCstMonthlyInterestRate() > 0 && adPaymentTerm.getPytEnableInterest() == EJBCommon.TRUE) {
                    try {

                        LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), branchCode, companyCode);
                        unearnedInterestAmount = EJBCommon.roundIt((totalLine + totalTax - totalDownpayment) * adPaymentTerm.getAdPaymentSchedules().size() * (arInvoice.getArCustomer().getCstMonthlyInterestRate() / 100), commonData.getGlFcPrecisionUnit(companyCode));

                        double convertedUnearnedInterestAmount = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), unearnedInterestAmount, companyCode);

                        this.addArDrEntry(arInvoice.getArDrNextLine(), "UNINTEREST", EJBCommon.FALSE, convertedUnearnedInterestAmount, adBranchCustomer.getBcstGlCoaUnEarnedInterestAccount(), null, arInvoice, branchCode, companyCode);

                        this.addArDrIliEntry(arInvoice.getArDrNextLine(), "RECEIVABLE INTEREST", EJBCommon.TRUE, convertedUnearnedInterestAmount, adBranchCustomer.getBcstGlCoaReceivableAccount(), arInvoice, branchCode, companyCode);
                    }
                    catch (FinderException ex) {
                    }
                }
                arInvoice.setInvAmountUnearnedInterest(unearnedInterestAmount);

                // add wtax distribution if necessary
                double withholdingTaxAmount = 0d;
                if (arWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals(EJBCommon.INVOICE)) {
                    withholdingTaxAmount = EJBCommon.roundIt(totalLine * (arWithholdingTaxCode.getWtcRate() / 100), commonData.getGlFcPrecisionUnit(companyCode));
                    double convertedWithholdingTaxAmount = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), withholdingTaxAmount, companyCode);
                    this.addArDrEntry(arInvoice.getArDrNextLine(), "W-TAX", EJBCommon.TRUE, convertedWithholdingTaxAmount, arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), null, arInvoice, branchCode, companyCode);
                }

                // add payment discount if necessary
                double discountAmount = 0d;
                if (adPaymentTerm.getPytDiscountOnInvoice() == EJBCommon.TRUE) {
                    Collection adPaymentSchedules = adPaymentScheduleHome.findByPytCode(adPaymentTerm.getPytCode(), companyCode);
                    ArrayList adPaymentScheduleList = new ArrayList(adPaymentSchedules);
                    LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) adPaymentScheduleList.get(0);

                    Collection adDiscounts = adDiscountHome.findByPsCode(adPaymentSchedule.getPsCode(), companyCode);
                    ArrayList adDiscountList = new ArrayList(adDiscounts);
                    LocalAdDiscount adDiscount = (LocalAdDiscount) adDiscountList.get(0);

                    double rate = adDiscount.getDscDiscountPercent();
                    discountAmount = (totalLine + totalTax) * (rate / 100d);

                    double convertedDiscountAmount = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), discountAmount, companyCode);

                    this.addArDrEntry(arInvoice.getArDrNextLine(), "DISCOUNT", EJBCommon.TRUE, convertedDiscountAmount, adPaymentTerm.getGlChartOfAccount().getCoaCode(), null, arInvoice, branchCode, companyCode);
                }
                amountDue = totalLine + totalTax - withholdingTaxAmount - discountAmount + unearnedInterestAmount;

                // add receivable distribution
                try {
                    LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), branchCode, companyCode);
                    double convertedAmountDue = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), amountDue, companyCode);
                    this.addArDrIliEntry(arInvoice.getArDrNextLine(), "RECEIVABLE", EJBCommon.TRUE, convertedAmountDue, adBranchCustomer.getBcstGlCoaReceivableAccount(), arInvoice, branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                // compute invoice amount due
                // set invoice amount due
                arInvoice.setInvAmountDue(amountDue + unearnedInterestAmount);

                // remove all payment schedule
                removeAllPaymentSchedule(arInvoice);

                // create invoice payment schedule
                short precisionUnit = commonData.getGlFcPrecisionUnit(companyCode);
                double totalPaymentSchedule = 0d;
                double totalPaymentInterest = 0d;
                double totalPaymentScheduleInterest = totalLine + totalTax - totalDownpayment;

                GregorianCalendar gcPrevDateDue = new GregorianCalendar();
                GregorianCalendar gcDateDue = new GregorianCalendar();
                gcPrevDateDue.setTime(arInvoice.getInvEffectivityDate());

                Collection adPaymentSchedules = adPaymentTerm.getAdPaymentSchedules();
                boolean first = true;
                i = adPaymentSchedules.iterator();
                while (i.hasNext()) {

                    LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) i.next();
                    // get date due
                    switch (arInvoice.getAdPaymentTerm().getPytScheduleBasis()) {
                        case "DEFAULT":
                            gcDateDue.setTime(arInvoice.getInvEffectivityDate());
                            gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());
                            break;
                        case "MONTHLY":
                            gcDateDue = gcPrevDateDue;
                            gcDateDue.add(Calendar.MONTH, 1);
                            gcPrevDateDue = gcDateDue;
                            break;
                        case "BI-MONTHLY":
                            gcDateDue = gcPrevDateDue;
                            if (gcPrevDateDue.get(Calendar.MONTH) != 1) {
                                if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 31 && gcPrevDateDue.get(Calendar.DATE) > 15 && gcPrevDateDue.get(Calendar.DATE) < 31) {
                                    gcDateDue.add(Calendar.DATE, 16);
                                } else {
                                    gcDateDue.add(Calendar.DATE, 15);
                                }
                            } else if (gcPrevDateDue.get(Calendar.MONTH) == 1) {
                                if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28 && gcPrevDateDue.get(Calendar.DATE) == 14) {
                                    gcDateDue.add(Calendar.DATE, 14);
                                } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28 && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 28) {
                                    gcDateDue.add(Calendar.DATE, 13);
                                } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 29 && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 29) {
                                    gcDateDue.add(Calendar.DATE, 14);
                                } else {
                                    gcDateDue.add(Calendar.DATE, 15);
                                }
                            }
                            gcPrevDateDue = gcDateDue;
                            break;
                    }

                    if (first == true && arInvoice.getInvDownPayment() > 0) {
                        LocalArInvoicePaymentSchedule arInvoicePaymentScheduleDownPayment = arInvoicePaymentScheduleHome.create(arInvoice.getInvEffectivityDate(), (short) 0, arInvoice.getInvDownPayment(), 0d, EJBCommon.FALSE, (short) 0, arInvoice.getInvEffectivityDate(), 0d, 0d, companyCode);
                        arInvoice.addArInvoicePaymentSchedule(arInvoicePaymentScheduleDownPayment);
                        first = false;
                    }

                    // create a payment schedule
                    double paymentScheduleAmount = 0;
                    double paymentScheduleInterest = 0;
                    double paymentSchedulePrincipal = 0;

                    // if last payment schedule subtract to avoid rounding difference error
                    if (i.hasNext()) {
                        paymentScheduleAmount = EJBCommon.roundIt((adPaymentSchedule.getPsRelativeAmount() / adPaymentTerm.getPytBaseAmount()) * (arInvoice.getInvAmountDue() - arInvoice.getInvDownPayment()), precisionUnit);
                        paymentScheduleInterest = EJBCommon.roundIt((totalPaymentScheduleInterest) * (adPaymentTerm.getPytMonthlyInterestRate() / 100), precisionUnit);
                        paymentSchedulePrincipal = -(paymentScheduleAmount) + paymentScheduleInterest;
                    } else {
                        paymentScheduleAmount = (arInvoice.getInvAmountDue() - arInvoice.getInvDownPayment()) - totalPaymentSchedule;
                        paymentScheduleInterest = EJBCommon.roundIt((totalPaymentScheduleInterest) * (adPaymentTerm.getPytMonthlyInterestRate() / 100), precisionUnit);
                        paymentSchedulePrincipal = -(paymentScheduleAmount) + paymentScheduleInterest;
                    }

                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arInvoicePaymentScheduleHome.create(gcDateDue.getTime(), adPaymentSchedule.getPsLineNumber(), paymentScheduleAmount, 0d, EJBCommon.FALSE, (short) 0, gcDateDue.getTime(), 0d, 0d, companyCode);

                    arInvoicePaymentSchedule.setIpsInterestDue(paymentScheduleInterest);
                    arInvoice.addArInvoicePaymentSchedule(arInvoicePaymentSchedule);

                    totalPaymentSchedule += paymentScheduleAmount;
                    totalPaymentInterest += paymentScheduleInterest;
                    totalPaymentScheduleInterest += paymentSchedulePrincipal;
                }
            }

            // generate approval status
            String invoiceApprovalStatus = null;
            if (!isDraft) {
                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);
                // validate if total amount + unposted invoices' amount + current balance + unposted receipts's amount does not exceed customer's credit limit
                double balance = 0;
                LocalAdApprovalDocument adInvoiceApprovalDocument = adApprovalDocumentHome.findByAdcType(EJBCommon.AR_INVOICE, companyCode);

                if (arCustomer.getCstCreditLimit() > 0) {
                    balance = computeTotalBalance(details.getInvCode(), customerCode, companyCode);
                    balance += amountDue;
                    if (arCustomer.getCstCreditLimit() < balance && (adApproval.getAprEnableArInvoice() == EJBCommon.FALSE || (adApproval.getAprEnableArInvoice() == EJBCommon.TRUE && adInvoiceApprovalDocument.getAdcEnableCreditLimitChecking() == EJBCommon.FALSE))) {
                        throw new ArINVAmountExceedsCreditLimitException();
                    }
                }

                // find overdue invoices
                Collection arOverdueInvoices = arInvoicePaymentScheduleHome.findOverdueIpsByInvDateAndCstCustomerCode(arInvoice.getInvDate(), customerCode, companyCode);

                // check if ar invoice approval is enabled
                if (adApproval.getAprEnableArInvoice() == EJBCommon.FALSE || (arCustomer.getCstCreditLimit() > balance && arOverdueInvoices.isEmpty())) {
                    invoiceApprovalStatus = EJBCommon.NOT_APPLICABLE;
                } else {
                    // check if invoice is self approved
                    LocalAdUser adUser = adUserHome.findByUsrName(details.getInvLastModifiedBy(), companyCode);
                    invoiceApprovalStatus = arApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getInvLastModifiedBy(), adUser.getUsrDescription(), EJBCommon.AR_INVOICE, arInvoice.getInvCode(), arInvoice.getInvNumber(), arInvoice.getInvDate(), branchCode, companyCode);
                }
            }

            if (invoiceApprovalStatus != null && invoiceApprovalStatus.equals("N/A") && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeArInvPost(arInvoice.getInvCode(), arInvoice.getInvLastModifiedBy(), branchCode, companyCode);

                // Set SO Lock if Fully Served
                Iterator solIter = arExistingSalesOrder.getArSalesOrderLines().iterator();
                boolean isOpenSO = false;
                while (solIter.hasNext()) {
                    LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) solIter.next();
                    Iterator soInvLnIter = arSalesOrderLine.getArSalesOrderInvoiceLines().iterator();
                    double quantitySold = 0d;

                    while (soInvLnIter.hasNext()) {
                        LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) soInvLnIter.next();
                        if (arSalesOrderInvoiceLine.getArInvoice().getInvPosted() == EJBCommon.TRUE) {
                            quantitySold += arSalesOrderInvoiceLine.getSilQuantityDelivered();
                        }
                    }

                    double totalRemainingQuantity = arSalesOrderLine.getSolQuantity() - quantitySold;
                    if (totalRemainingQuantity > 0) {
                        isOpenSO = true;
                        break;
                    }
                }

                if (isOpenSO) {
                    arExistingSalesOrder.setSoLock(EJBCommon.FALSE);
                } else {
                    arExistingSalesOrder.setSoLock(EJBCommon.TRUE);
                }
            }

            // set invoice approval status
            arInvoice.setInvApprovalStatus(invoiceApprovalStatus);
            return arInvoice.getInvCode();

        }
        catch (GlobalRecordAlreadyDeletedException | ArInvDuplicateUploadNumberException |
               GlobalExpiryDateNotFoundException | AdPRFCoaGlVarianceAccountNotFoundException |
               ArINVAmountExceedsCreditLimitException | GlobalInventoryDateException |
               GlobalTransactionAlreadyLockedException | GlobalJournalNotBalanceException |
               GlJREffectiveDatePeriodClosedException | GlJREffectiveDateNoPeriodExistException |
               GlobalInvItemLocationNotFoundException | GlobalNoApprovalApproverFoundException |
               GlobalNoApprovalRequesterFoundException | GlobalTransactionAlreadyVoidException |
               GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
               GlobalTransactionAlreadyApprovedException | GlobalPaymentTermInvalidException |
               GlobalConversionDateNotExistException | GlobalDocumentNumberNotUniqueException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (GlobalBranchAccountNumberInvalidException ex) {
            // Retrive Error a Line Number
            ex.setLineNumberError(lineNumberError);
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveArInvJolEntry(ArInvoiceDetails details, String paymentName, String taxCode, String withholdingTaxCode, String currencyName, String customerCode, String invoiceBatchName, ArrayList jolList, boolean isDraft, String salesPersonCode, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalTransactionAlreadyLockedException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, ArINVAmountExceedsCreditLimitException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException, ArInvDuplicateUploadNumberException {

        Debug.print("ArInvoiceEntryControllerBean saveArInvJolEntry");
        LocalArInvoice arInvoice = null;
        int lineNumberError = 0;
        try {
            // validate if invoice is already deleted
            arInvoice = isInvoiceDeleted(details, arInvoice);
            // validate if invoice is already posted, void, approved or pending
            if (details.getInvCode() != null) {
                validateInvoice(arInvoice);
            }
            // invoice void
            if (isInvoiceVoid(details, arInvoice)) {
                return arInvoice.getInvCode();
            }
            // validate if document number is unique document number is automatic then set next sequence
            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
            if (details.getInvCode() == null) {
                String documentType = details.getInvDocumentType().trim().equals("") ? null : details.getInvDocumentType();
                try {
                    if (documentType != null) {
                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode);
                    } else {
                        documentType = EJBCommon.AR_INVOICE;
                    }
                }
                catch (FinderException ex) {
                    documentType = EJBCommon.AR_INVOICE;
                }

                try {
                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName(documentType, companyCode);
                }
                catch (FinderException ex) {
                }

                try {
                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                LocalArInvoice arExistingInvoice = null;
                try {

                    arExistingInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(details.getInvNumber(), EJBCommon.FALSE, branchCode, companyCode);
                }
                catch (FinderException ex) {
                }

                if (arExistingInvoice != null) {
                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getInvNumber() == null || details.getInvNumber().trim().length() == 0)) {

                    setupDocumentSequenceAssignment(details, branchCode, companyCode, adBranchDocumentSequenceAssignment, adDocumentSequenceAssignment);
                }

                // Confirm if Upload Number have duplicate
                isUploadNumberHaveDuplicate(details, companyCode);

            } else {

                LocalArInvoice arExistingInvoice = null;

                try {

                    arExistingInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(details.getInvNumber(), EJBCommon.FALSE, branchCode, companyCode);

                }
                catch (FinderException ex) {
                }

                if (arExistingInvoice != null && !arExistingInvoice.getInvCode().equals(details.getInvCode())) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (arInvoice.getInvNumber() != details.getInvNumber() && (details.getInvNumber() == null || details.getInvNumber().trim().length() == 0)) {

                    details.setInvNumber(arInvoice.getInvNumber());
                }

                // Confirm if Upload Number have duplicate

                if (details.getInvUploadNumber() != null && !details.getInvUploadNumber().trim().equals("") && !details.getInvUploadNumber().equals(arExistingInvoice.getInvUploadNumber())) {
                    try {
                        arInvoiceHome.findByUploadNumberAndCompanyCode(details.getInvUploadNumber(), companyCode);
                        // throw exception if found duplicate upload number
                        throw new ArInvDuplicateUploadNumberException();
                    }
                    catch (FinderException ex) {

                    }
                }
            }


            // validate if conversion date exists
            isConversionDateExists(details, currencyName, companyCode);

            // validate if payment term has at least one payment schedule
            if (adPaymentTermHome.findByPytName(paymentName, companyCode).getAdPaymentSchedules().isEmpty()) {
                throw new GlobalPaymentTermInvalidException();
            }

            // lock sales order
            LocalArJobOrder arExistingJobOrder = null;
            try {
                arExistingJobOrder = arJobOrderHome.findByJoDocumentNumberAndBrCode(details.getInvJoNumber(), branchCode, companyCode);
                if ((arInvoice == null || (arInvoice != null && arInvoice.getInvJoNumber() != null && (!arInvoice.getInvJoNumber().equals(details.getInvJoNumber()))))) {
                    if (arExistingJobOrder.getJoLock() == EJBCommon.TRUE) {
                        throw new GlobalTransactionAlreadyLockedException();
                    }
                    arExistingJobOrder.setJoLock(EJBCommon.TRUE);
                }
            }
            catch (FinderException ex) {
            }

            // used in checking if invoice should re-generate distribution records and re-calculate taxes
            boolean isRecalculate = true;

            // create invoice
            if (details.getInvCode() == null) {

                arInvoice = arInvoiceHome.create(details.getInvType(), EJBCommon.FALSE, details.getInvDescription(), details.getInvDate(), details.getInvNumber(), details.getInvReferenceNumber(), details.getInvUploadNumber(), null, null, 0d, details.getInvDownPayment(), 0d, 0d, 0d, 0d, details.getInvConversionDate(), details.getInvConversionRate(), details.getInvMemo(), details.getInvPreviousReading(), details.getInvPresentReading(), details.getInvBillToAddress(), details.getInvBillToContact(), details.getInvBillToAltContact(), details.getInvBillToPhone(), details.getInvBillingHeader(), details.getInvBillingFooter(), details.getInvBillingHeader2(), details.getInvBillingFooter2(), details.getInvBillingHeader3(), details.getInvBillingFooter3(), details.getInvBillingSignatory(), details.getInvSignatoryTitle(), details.getInvShipToAddress(), details.getInvShipToContact(), details.getInvShipToAltContact(), details.getInvShipToPhone(), details.getInvShipDate(), details.getInvLvFreight(), null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, null, 0d, null, null, null, null, details.getInvCreatedBy(), details.getInvDateCreated(), details.getInvLastModifiedBy(), details.getInvDateLastModified(), null, null, null, null, EJBCommon.FALSE, details.getInvLvShift(), null, details.getInvJoNumber(), details.getInvDebitMemo(), details.getInvSubjectToCommission(), details.getInvClientPO(), details.getInvEffectivityDate(), branchCode, companyCode);

            } else {

                // check if critical fields are changed

                if ((arInvoice.getInvJoNumber() != null && !arInvoice.getInvJoNumber().equals(details.getInvJoNumber())) || !arInvoice.getArTaxCode().getTcName().equals(taxCode) || !arInvoice.getArWithholdingTaxCode().getWtcName().equals(withholdingTaxCode) || !arInvoice.getArCustomer().getCstCustomerCode().equals(customerCode) || !arInvoice.getAdPaymentTerm().getPytName().equals(paymentName) || !arInvoice.getInvDate().equals(details.getInvDate()) || !arInvoice.getInvEffectivityDate().equals(details.getInvEffectivityDate()) || arInvoice.getInvDownPayment() != details.getInvDownPayment() || jolList.size() != arInvoice.getArJobOrderInvoiceLines().size()) {

                    isRecalculate = true;

                } else if (jolList.size() == arInvoice.getArJobOrderInvoiceLines().size()) {

                    Iterator jolIter = arInvoice.getArJobOrderInvoiceLines().iterator();
                    Iterator jolListIter = jolList.iterator();

                    while (jolIter.hasNext()) {
                        LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) jolIter.next();
                        LocalArJobOrderLine arJobOrderLine = arJobOrderInvoiceLine.getArJobOrderLine();

                        ArModJobOrderLineDetails mdetails = (ArModJobOrderLineDetails) jolListIter.next();

                        if (!arJobOrderLine.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getJolIiName()) || !arJobOrderLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getJolLocName()) || !arJobOrderLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getJolUomName()) || arJobOrderInvoiceLine.getJilQuantityDelivered() != mdetails.getJolQuantityDelivered() || arJobOrderInvoiceLine.getJilTax() != mdetails.getJolTax() || arJobOrderLine.getJolUnitPrice() != mdetails.getJolUnitPrice()) {

                            isRecalculate = true;
                            break;
                        }

                        // isRecalculate = false;

                    }

                } else {

                    // isRecalculate = false;

                }

                arInvoice.setInvDownPayment(details.getInvDownPayment());
                arInvoice.setInvDocumentType(details.getInvDocumentType());
                arInvoice.setInvType(details.getInvType());
                arInvoice.setInvDescription(details.getInvDescription());
                arInvoice.setInvDate(details.getInvDate());
                arInvoice.setInvNumber(details.getInvNumber());
                arInvoice.setInvReferenceNumber(details.getInvReferenceNumber());
                arInvoice.setInvMemo(details.getInvMemo());
                arInvoice.setInvUploadNumber(details.getInvUploadNumber());
                arInvoice.setInvConversionDate(details.getInvConversionDate());
                arInvoice.setInvConversionRate(details.getInvConversionRate());
                arInvoice.setInvPreviousReading(arInvoice.getInvPreviousReading());
                arInvoice.setInvPresentReading(arInvoice.getInvPresentReading());
                arInvoice.setInvBillToAddress(details.getInvBillToAddress());
                arInvoice.setInvBillToContact(details.getInvBillToContact());
                arInvoice.setInvBillToAltContact(details.getInvBillToAltContact());
                arInvoice.setInvBillToPhone(details.getInvBillToPhone());
                arInvoice.setInvBillingHeader(details.getInvBillingHeader());
                arInvoice.setInvBillingFooter(details.getInvBillingFooter());
                arInvoice.setInvBillingHeader2(details.getInvBillingHeader2());
                arInvoice.setInvBillingFooter2(details.getInvBillingFooter2());
                arInvoice.setInvBillingSignatory(details.getInvBillingSignatory());
                arInvoice.setInvSignatoryTitle(details.getInvSignatoryTitle());
                arInvoice.setInvShipToAddress(details.getInvShipToAddress());
                arInvoice.setInvShipToContact(details.getInvShipToContact());
                arInvoice.setInvShipToAltContact(details.getInvShipToAltContact());
                arInvoice.setInvShipToPhone(details.getInvShipToPhone());
                arInvoice.setInvShipDate(details.getInvShipDate());
                arInvoice.setInvLvFreight(details.getInvLvFreight());
                arInvoice.setInvLastModifiedBy(details.getInvLastModifiedBy());
                arInvoice.setInvDateLastModified(details.getInvDateLastModified());
                arInvoice.setInvReasonForRejection(null);
                arInvoice.setInvLvShift(details.getInvLvShift());
                arInvoice.setInvJoNumber(details.getInvJoNumber());
                arInvoice.setInvDebitMemo(details.getInvDebitMemo());
                arInvoice.setInvSubjectToCommission(details.getInvSubjectToCommission());
                arInvoice.setInvClientPO(details.getInvClientPO());
                arInvoice.setInvEffectivityDate(details.getInvEffectivityDate());
            }
            arInvoice.setInvDocumentType(details.getInvDocumentType());
            arInvoice.setReportParameter(details.getReportParameter());

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(paymentName, companyCode);
            // adPaymentTerm.addArInvoice(arInvoice);
            arInvoice.setAdPaymentTerm(adPaymentTerm);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(currencyName, companyCode);
            // glFunctionalCurrency.addArInvoice(arInvoice);
            arInvoice.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(taxCode, companyCode);
            arInvoice.setArTaxCode(arTaxCode);

            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(withholdingTaxCode, companyCode);
            arInvoice.setArWithholdingTaxCode(arWithholdingTaxCode);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(customerCode, companyCode);
            arInvoice.setArCustomer(arCustomer);

            tagSalesPerson(salesPersonCode, companyCode, arInvoice);
            setInvoiceBatch(invoiceBatchName, branchCode, companyCode, arInvoice);

            double amountDue = 0;
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            if (isRecalculate) {
                // remove all invoice line items
                Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();
                Iterator i = arInvoiceLineItems.iterator();
                while (i.hasNext()) {
                    clearInvoiceLineItem(companyCode, i);
                }
                // remove all invoice lines
                Collection arInvoiceLines = arInvoice.getArInvoiceLines();
                i = arInvoiceLines.iterator();
                while (i.hasNext()) {
                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) i.next();
                    i.remove();
                    em.remove(arInvoiceLine);
                }
                // remove all job order lines
                Collection arJobOrderInvoiceLines = arInvoice.getArJobOrderInvoiceLines();
                i = arJobOrderInvoiceLines.iterator();
                while (i.hasNext()) {
                    LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) i.next();
                    // release previous sales order
                    if (!arExistingJobOrder.getJoDocumentNumber().equals(arJobOrderInvoiceLine.getArJobOrderLine().getArJobOrder().getJoDocumentNumber())) {
                        arJobOrderInvoiceLine.getArJobOrderLine().getArJobOrder().setJoLock(EJBCommon.FALSE);
                    }
                    i.remove();
                    em.remove(arJobOrderInvoiceLine);
                }
                // remove all distribution records
                Collection arDistributionRecords = arInvoice.getArDistributionRecords();
                i = arDistributionRecords.iterator();
                while (i.hasNext()) {
                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();
                    i.remove();
                    em.remove(arDistributionRecord);
                }

                // add new sales order line and distribution record
                double totalTax = 0d;
                double totalLine = 0d;
                double totalSalesOrderDebitAmount = 0d;
                double totalDownpayment = arInvoice.getInvDownPayment();
                i = jolList.iterator();
                LocalInvItemLocation invItemLocation;
                while (i.hasNext()) {
                    ArModJobOrderLineDetails mjolDetails = (ArModJobOrderLineDetails) i.next();
                    try {
                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mjolDetails.getJolLocName(), mjolDetails.getJolIiName(), companyCode);
                    }
                    catch (FinderException ex) {
                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mjolDetails.getJolLine()));
                    }
                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }
                    LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = this.addArJolEntry(mjolDetails, arInvoice, companyCode);
                    LocalArJobOrderLine arJobOrderLine = arJobOrderInvoiceLine.getArJobOrderLine();
                    // add cost of sales distribution and inventory
                    double unitCost = invItemLocation.getInvItem().getIiUnitCost();
                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

                        // check if rmning vl is not zero and rmng qty is 0
                        if (invCosting.getCstRemainingQuantity() <= 0) {

                            HashMap criteria = new HashMap();
                            criteria.put("itemName", invItemLocation.getInvItem().getIiName());
                            criteria.put("location", invItemLocation.getInvLocation().getLocName());

                            ArrayList branchList = new ArrayList();

                            AdBranchDetails mdetails = new AdBranchDetails();
                            mdetails.setBrCode(branchCode);
                            branchList.add(mdetails);

                            ejbRIC.executeInvFixItemCosting(criteria, branchList, companyCode);
                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        }

                        if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.Average)) {
                            unitCost = invCosting.getCstRemainingQuantity() <= 0 ? unitCost : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                        }

                        if (unitCost <= 0) {
                            unitCost = invItemLocation.getInvItem().getIiUnitCost();
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.FIFO)) {
                            unitCost = invCosting.getCstRemainingQuantity() == 0 ? unitCost : this.getInvFifoCost(arInvoice.getInvDate(), invItemLocation.getIlCode(), arJobOrderInvoiceLine.getJilQuantityDelivered(), arJobOrderInvoiceLine.getArJobOrderLine().getJolUnitPrice(), false, branchCode, companyCode);
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.Standard)) {
                            unitCost = invItemLocation.getInvItem().getIiUnitCost();
                        }

                    }
                    catch (FinderException ex) {

                        unitCost = arJobOrderLine.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double quantitySold = this.convertByUomFromAndItemAndQuantity(arJobOrderLine.getInvUnitOfMeasure(), arJobOrderLine.getInvItemLocation().getInvItem(), arJobOrderInvoiceLine.getJilQuantityDelivered(), companyCode);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        lineNumberError = arJobOrderLine.getJolLine();
                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arJobOrderLine.getInvItemLocation().getIlCode(), branchCode, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    // Branch adding in Job Order
                    if (adPreference.getPrfArAutoComputeCogs() == EJBCommon.TRUE && arJobOrderLine.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {
                        lineNumberError = arInvoice.getArDrNextLine();
                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.COST_OF_GOODS_SOLD, EJBCommon.TRUE, unitCost * quantitySold, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.INVENTORY, EJBCommon.FALSE, unitCost * quantitySold, adBranchItemLocation.getBilCoaGlInventoryAccount(), arInvoice, branchCode, companyCode);

                        } else {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.COST_OF_GOODS_SOLD, EJBCommon.TRUE, unitCost * quantitySold, arJobOrderLine.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.INVENTORY, EJBCommon.FALSE, unitCost * quantitySold, arJobOrderLine.getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, branchCode, companyCode);
                        }
                    }

                    // add quantity to item location committed quantity

                    double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arJobOrderLine.getInvUnitOfMeasure(), arJobOrderLine.getInvItemLocation().getInvItem(), arJobOrderInvoiceLine.getJilQuantityDelivered(), companyCode);
                    invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);

                    // add inventory sale distributions

                    double jobOrderInvoiceLine = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arJobOrderInvoiceLine.getJilAmount(), companyCode);

                    double jobOrderInvoiceTax = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arJobOrderInvoiceLine.getJilTaxAmount(), companyCode);

                    if (adBranchItemLocation != null) {
                        // this will trigger by services(DEBIT)
                        if (adBranchItemLocation.getInvItemLocation().getInvItem().getIiServices() == EJBCommon.TRUE) {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.REVENUE, EJBCommon.TRUE, jobOrderInvoiceLine, adBranchItemLocation.getBilCoaGlSalesAccount(), arInvoice, branchCode, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.OTHER, EJBCommon.FALSE, jobOrderInvoiceLine, adBranchItemLocation.getBilCoaGlSalesReturnAccount(), arInvoice, branchCode, companyCode);

                            totalSalesOrderDebitAmount += arJobOrderInvoiceLine.getJilAmount();
                        } else {
                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.REVENUE, EJBCommon.FALSE, jobOrderInvoiceLine, adBranchItemLocation.getBilCoaGlSalesAccount(), arInvoice, branchCode, companyCode);
                        }

                    } else {

                        // this will trigger by services(DEBIT)
                        if (arJobOrderLine.getInvItemLocation().getInvItem().getIiServices() == EJBCommon.TRUE) {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.REVENUE, EJBCommon.TRUE, jobOrderInvoiceLine, arJobOrderLine.getInvItemLocation().getIlGlCoaSalesAccount(), arInvoice, branchCode, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.OTHER, EJBCommon.FALSE, jobOrderInvoiceLine, arJobOrderLine.getInvItemLocation().getIlGlCoaSalesReturnAccount(), arInvoice, branchCode, companyCode);

                            totalSalesOrderDebitAmount += jobOrderInvoiceLine;
                        } else {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.REVENUE, EJBCommon.FALSE, jobOrderInvoiceLine, arJobOrderLine.getInvItemLocation().getIlGlCoaSalesAccount(), arInvoice, branchCode, companyCode);
                        }
                    }

                    totalLine += arJobOrderInvoiceLine.getJilAmount();
                    totalTax += arJobOrderInvoiceLine.getJilTaxAmount();
                }

                // add tax distribution if necessary

                if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {

                    double convertedTotalTax = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), totalTax, companyCode);

                    if (arTaxCode.getTcInterimAccount() == null) {
                        // add branch tax code
                        LocalAdBranchArTaxCode adBranchTaxCode = null;
                        try {
                            adBranchTaxCode = adBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(arInvoice.getArTaxCode().getTcCode(), branchCode, companyCode);
                        }
                        catch (FinderException ex) {
                        }
                        if (adBranchTaxCode != null) {
                            this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.TAX, EJBCommon.FALSE, convertedTotalTax, adBranchTaxCode.getBtcGlCoaTaxCode(), null, arInvoice, branchCode, companyCode);
                        } else {
                            this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.TAX, EJBCommon.FALSE, convertedTotalTax, arTaxCode.getGlChartOfAccount().getCoaCode(), null, arInvoice, branchCode, companyCode);
                        }
                    } else {
                        this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.DEFERRED_TAX, EJBCommon.FALSE, convertedTotalTax, arTaxCode.getTcInterimAccount(), null, arInvoice, branchCode, companyCode);
                    }
                }

                // add un earned interest
                double unearnedInterestAmount = 0d;

                if (arInvoice.getArCustomer().getCstAutoComputeInterest() == EJBCommon.TRUE && arInvoice.getArCustomer().getCstMonthlyInterestRate() > 0 && adPaymentTerm.getPytEnableInterest() == EJBCommon.TRUE) {

                    try {

                        LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), branchCode, companyCode);

                        unearnedInterestAmount = EJBCommon.roundIt((totalLine + totalTax - totalDownpayment) * adPaymentTerm.getAdPaymentSchedules().size() * (arInvoice.getArCustomer().getCstMonthlyInterestRate() / 100), commonData.getGlFcPrecisionUnit(companyCode));

                        double convertedUnearnedInterestAmount = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), unearnedInterestAmount, companyCode);

                        this.addArDrEntry(arInvoice.getArDrNextLine(), "UNINTEREST", EJBCommon.FALSE, convertedUnearnedInterestAmount, adBranchCustomer.getBcstGlCoaUnEarnedInterestAccount(), null, arInvoice, branchCode, companyCode);

                        this.addArDrIliEntry(arInvoice.getArDrNextLine(), "RECEIVABLE INTEREST", EJBCommon.TRUE, convertedUnearnedInterestAmount, adBranchCustomer.getBcstGlCoaReceivableAccount(), arInvoice, branchCode, companyCode);

                    }
                    catch (FinderException ex) {

                    }
                }
                arInvoice.setInvAmountUnearnedInterest(unearnedInterestAmount);

                // add wtax distribution if necessary
                double withholdingTaxAmount = 0d;

                if (arWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals(EJBCommon.INVOICE)) {

                    withholdingTaxAmount = EJBCommon.roundIt(totalLine * (arWithholdingTaxCode.getWtcRate() / 100), commonData.getGlFcPrecisionUnit(companyCode));

                    double convertedWithholdingTaxAmount = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), withholdingTaxAmount, companyCode);

                    this.addArDrEntry(arInvoice.getArDrNextLine(), "W-TAX", EJBCommon.TRUE, convertedWithholdingTaxAmount, arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), null, arInvoice, branchCode, companyCode);
                }

                // add payment discount if necessary

                double discountAmount = 0d;

                if (adPaymentTerm.getPytDiscountOnInvoice() == EJBCommon.TRUE) {

                    Collection adPaymentSchedules = adPaymentScheduleHome.findByPytCode(adPaymentTerm.getPytCode(), companyCode);
                    ArrayList adPaymentScheduleList = new ArrayList(adPaymentSchedules);
                    LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) adPaymentScheduleList.get(0);

                    Collection adDiscounts = adDiscountHome.findByPsCode(adPaymentSchedule.getPsCode(), companyCode);
                    ArrayList adDiscountList = new ArrayList(adDiscounts);
                    LocalAdDiscount adDiscount = (LocalAdDiscount) adDiscountList.get(0);

                    double rate = adDiscount.getDscDiscountPercent();
                    discountAmount = (totalLine + totalTax) * (rate / 100d);

                    double convertedDiscountAmount = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), discountAmount, companyCode);

                    this.addArDrEntry(arInvoice.getArDrNextLine(), "DISCOUNT", EJBCommon.TRUE, convertedDiscountAmount, adPaymentTerm.getGlChartOfAccount().getCoaCode(), null, arInvoice, branchCode, companyCode);
                }

                // add receivable distribution
                try {
                    LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), branchCode, companyCode);
                    double convertedAmountDue = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), totalLine + totalTax - withholdingTaxAmount - discountAmount - totalSalesOrderDebitAmount, companyCode);
                    this.addArDrIliEntry(arInvoice.getArDrNextLine(), "RECEIVABLE", EJBCommon.TRUE, convertedAmountDue, adBranchCustomer.getBcstGlCoaReceivableAccount(), arInvoice, branchCode, companyCode);

                }
                catch (FinderException ex) {

                }

                // compute invoice amount due
                amountDue = totalLine + totalTax - withholdingTaxAmount - discountAmount + unearnedInterestAmount;

                // set invoice amount due
                arInvoice.setInvAmountDue(amountDue);

                // remove all payment schedule
                removeAllPaymentSchedule(arInvoice);

                // create invoice payment schedule
                short precisionUnit = commonData.getGlFcPrecisionUnit(companyCode);
                double totalPaymentSchedule = 0d;
                double totalPaymentInterest = 0d;
                double totalPaymentScheduleInterest = totalLine + totalTax - totalDownpayment;

                GregorianCalendar gcPrevDateDue = new GregorianCalendar();
                GregorianCalendar gcDateDue = new GregorianCalendar();
                gcPrevDateDue.setTime(arInvoice.getInvEffectivityDate());

                Collection adPaymentSchedules = adPaymentTerm.getAdPaymentSchedules();

                boolean first = true;

                i = adPaymentSchedules.iterator();

                while (i.hasNext()) {

                    LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) i.next();

                    // get date due

                    switch (arInvoice.getAdPaymentTerm().getPytScheduleBasis()) {
                        case "DEFAULT":

                            gcDateDue.setTime(arInvoice.getInvEffectivityDate());
                            gcDateDue.add(Calendar.DATE, adPaymentSchedule.getPsDueDay());

                            break;
                        case "MONTHLY":

                            gcDateDue = gcPrevDateDue;
                            gcDateDue.add(Calendar.MONTH, 1);
                            gcPrevDateDue = gcDateDue;

                            break;
                        case "BI-MONTHLY":

                            gcDateDue = gcPrevDateDue;

                            if (gcPrevDateDue.get(Calendar.MONTH) != 1) {
                                if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 31 && gcPrevDateDue.get(Calendar.DATE) > 15 && gcPrevDateDue.get(Calendar.DATE) < 31) {
                                    gcDateDue.add(Calendar.DATE, 16);
                                } else {
                                    gcDateDue.add(Calendar.DATE, 15);
                                }
                            } else if (gcPrevDateDue.get(Calendar.MONTH) == 1) {
                                if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28 && gcPrevDateDue.get(Calendar.DATE) == 14) {
                                    gcDateDue.add(Calendar.DATE, 14);
                                } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 28 && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 28) {
                                    gcDateDue.add(Calendar.DATE, 13);
                                } else if (gcPrevDateDue.getActualMaximum(Calendar.DATE) == 29 && gcPrevDateDue.get(Calendar.DATE) >= 15 && gcPrevDateDue.get(Calendar.DATE) < 29) {
                                    gcDateDue.add(Calendar.DATE, 14);
                                } else {
                                    gcDateDue.add(Calendar.DATE, 15);
                                }
                            }

                            gcPrevDateDue = gcDateDue;
                            break;
                    }

                    if (first == true && arInvoice.getInvDownPayment() > 0) {

                        LocalArInvoicePaymentSchedule arInvoicePaymentScheduleDownPayment = arInvoicePaymentScheduleHome.create(arInvoice.getInvEffectivityDate(), (short) 0, arInvoice.getInvDownPayment(), 0d, EJBCommon.FALSE, (short) 0, arInvoice.getInvEffectivityDate(), 0d, 0d, companyCode);

                        arInvoice.addArInvoicePaymentSchedule(arInvoicePaymentScheduleDownPayment);
                        first = false;
                    }

                    // create a payment schedule
                    double paymentScheduleAmount = 0;
                    double paymentScheduleInterest = 0;
                    double paymentSchedulePrincipal = 0;

                    // if last payment schedule subtract to avoid rounding difference error
                    if (i.hasNext()) {

                        paymentScheduleAmount = EJBCommon.roundIt((adPaymentSchedule.getPsRelativeAmount() / adPaymentTerm.getPytBaseAmount()) * (arInvoice.getInvAmountDue() - arInvoice.getInvDownPayment()), precisionUnit);

                        paymentScheduleInterest = EJBCommon.roundIt((totalPaymentScheduleInterest) * (adPaymentTerm.getPytMonthlyInterestRate() / 100), precisionUnit);

                        paymentSchedulePrincipal = -(paymentScheduleAmount) + paymentScheduleInterest;

                    } else {

                        paymentScheduleAmount = (arInvoice.getInvAmountDue() - arInvoice.getInvDownPayment()) - totalPaymentSchedule;

                        paymentScheduleInterest = EJBCommon.roundIt((totalPaymentScheduleInterest) * (adPaymentTerm.getPytMonthlyInterestRate() / 100), precisionUnit);

                        paymentSchedulePrincipal = -(paymentScheduleAmount) + paymentScheduleInterest;
                    }

                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arInvoicePaymentScheduleHome.create(gcDateDue.getTime(), adPaymentSchedule.getPsLineNumber(), paymentScheduleAmount, 0d, EJBCommon.FALSE, (short) 0, gcDateDue.getTime(), 0d, 0d, companyCode);

                    arInvoicePaymentSchedule.setIpsInterestDue(paymentScheduleInterest);
                    arInvoice.addArInvoicePaymentSchedule(arInvoicePaymentSchedule);

                    totalPaymentSchedule += paymentScheduleAmount;
                    totalPaymentInterest += paymentScheduleInterest;
                    totalPaymentScheduleInterest += paymentSchedulePrincipal;
                }
            }

            // generate approval status

            String invoiceApprovalStatus = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // validate if total amount + unposted invoices' amount + current balance +
                // unposted
                // receipts's amount
                // does not exceed customer's credit limit

                double balance = 0;
                LocalAdApprovalDocument adInvoiceApprovalDocument = adApprovalDocumentHome.findByAdcType(EJBCommon.AR_INVOICE, companyCode);

                if (arCustomer.getCstCreditLimit() > 0) {

                    balance = computeTotalBalance(details.getInvCode(), customerCode, companyCode);

                    balance += amountDue;

                    if (arCustomer.getCstCreditLimit() < balance && (adApproval.getAprEnableArInvoice() == EJBCommon.FALSE || (adApproval.getAprEnableArInvoice() == EJBCommon.TRUE && adInvoiceApprovalDocument.getAdcEnableCreditLimitChecking() == EJBCommon.FALSE))) {

                        throw new ArINVAmountExceedsCreditLimitException();
                    }
                }

                // find overdue invoices
                Collection arOverdueInvoices = arInvoicePaymentScheduleHome.findOverdueIpsByInvDateAndCstCustomerCode(arInvoice.getInvDate(), customerCode, companyCode);

                // check if ar invoice approval is enabled

                if (adApproval.getAprEnableArInvoice() == EJBCommon.FALSE || (arCustomer.getCstCreditLimit() > balance && arOverdueInvoices.size() == 0)) {

                    invoiceApprovalStatus = "N/A";

                } else {

                    // check if invoice is self approved

                    LocalAdUser adUser = adUserHome.findByUsrName(details.getInvLastModifiedBy(), companyCode);

                    invoiceApprovalStatus = arApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getInvLastModifiedBy(), adUser.getUsrDescription(), EJBCommon.AR_INVOICE, arInvoice.getInvCode(), arInvoice.getInvNumber(), arInvoice.getInvDate(), branchCode, companyCode);
                }
            }

            if (invoiceApprovalStatus != null && invoiceApprovalStatus.equals("N/A") && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeArInvPost(arInvoice.getInvCode(), arInvoice.getInvLastModifiedBy(), branchCode, companyCode);

                // Set SO Lock if Fully Served

                Iterator jolIter = arExistingJobOrder.getArJobOrderLines().iterator();

                boolean isOpenSO = false;

                while (jolIter.hasNext()) {

                    LocalArJobOrderLine arJobOrderLine = (LocalArJobOrderLine) jolIter.next();

                    Iterator soInvLnIter = arJobOrderLine.getArJobOrderInvoiceLines().iterator();
                    double quantitySold = 0d;

                    while (soInvLnIter.hasNext()) {

                        LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) soInvLnIter.next();

                        if (arJobOrderInvoiceLine.getArInvoice().getInvPosted() == EJBCommon.TRUE) {

                            quantitySold += arJobOrderInvoiceLine.getJilQuantityDelivered();
                        }
                    }

                    double totalRemainingQuantity = arJobOrderLine.getJolQuantity() - quantitySold;

                    if (totalRemainingQuantity > 0) {
                        isOpenSO = true;
                        break;
                    }
                }

                if (isOpenSO) {
                    arExistingJobOrder.setJoLock(EJBCommon.FALSE);
                } else {
                    arExistingJobOrder.setJoLock(EJBCommon.TRUE);
                }
            }

            // set invoice approval status

            arInvoice.setInvApprovalStatus(invoiceApprovalStatus);

            return arInvoice.getInvCode();

        }
        catch (GlobalRecordAlreadyDeletedException | ArInvDuplicateUploadNumberException |
               GlobalExpiryDateNotFoundException | AdPRFCoaGlVarianceAccountNotFoundException |
               ArINVAmountExceedsCreditLimitException | GlobalInventoryDateException |
               GlobalTransactionAlreadyLockedException | GlobalJournalNotBalanceException |
               GlJREffectiveDatePeriodClosedException | GlJREffectiveDateNoPeriodExistException |
               GlobalInvItemLocationNotFoundException | GlobalNoApprovalApproverFoundException |
               GlobalNoApprovalRequesterFoundException | GlobalTransactionAlreadyVoidException |
               GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
               GlobalTransactionAlreadyApprovedException | GlobalPaymentTermInvalidException |
               GlobalConversionDateNotExistException | GlobalDocumentNumberNotUniqueException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            // Retrive Error a Line Number
            ex.setLineNumberError(lineNumberError);
            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    //-- Other Public Methods
    public short getInvGpQuantityPrecisionUnit(Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getInvGpQuantityPrecisionUnit");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            return adPreference.getPrfInvQuantityPrecisionUnit();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpCostPrecisionUnit(Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getInvGpCostPrecisionUnit");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            return adPreference.getPrfInvCostPrecisionUnit();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfArInvoiceLineNumber(Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getAdPrfArInvoiceLineNumber");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            return adPreference.getPrfArInvoiceLineNumber();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableArInvoiceBatch(Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getAdPrfEnableArInvoiceBatch");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            return adPreference.getPrfEnableArInvoiceBatch();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfInvoiceSalespersonRequired(Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getAdPrfInvoiceSalespersonRequired");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            return adPreference.getPrfArInvcSalespersonRequired();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableInvShift(Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getAdPrfEnableInvShift");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            return adPreference.getPrfInvEnableShift();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfArUseCustomerPulldown(Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getAdPrfArUseCustomerPulldown");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            return adPreference.getPrfArUseCustomerPulldown();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfArDisableSalesPrice(Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getAdPrfArDisableSalesPrice");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            return adPreference.getPrfArDisableSalesPrice();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfArEnablePaymentTerm(Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getAdPrfArEnablePaymentTerm");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            return adPreference.getPrfArEnablePaymentTerm();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getFrRateByFrNameAndFrDate(String currencyName, Date conversionDate, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("ArInvoiceEntryControllerBean getFrRateByFrNameAndFrDate");
        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(currencyName, companyCode);
            double conversionRate = 1;
            // Get functional currency rate
            if (!currencyName.equals("USD")) {
                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), conversionDate, companyCode);
                conversionRate = glFunctionalCurrencyRate.getFrXToUsd();
            }
            // Get set of book functional currency rate if necessary
            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {
                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), conversionDate, companyCode);
                conversionRate = conversionRate / glCompanyFunctionalCurrencyRate.getFrXToUsd();
            }
            return conversionRate;
        }
        catch (FinderException ex) {
            getSessionContext().setRollbackOnly();
            throw new GlobalConversionDateNotExistException();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getIiSalesPriceByInvCstCustomerCodeAndIiNameAndUomName(String customerCode, String itemName, String unitOfMeasure, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getInvIiSalesPriceByIiNameAndUomName");
        try {
            LocalArCustomer arCustomer;
            try {
                arCustomer = arCustomerHome.findByCstCustomerCode(customerCode, companyCode);
            }
            catch (FinderException ex) {
                return 0d;
            }
            double unitPrice;
            LocalInvItem invItem = invItemHome.findByIiName(EJBCommon.itemName, companyCode);
            LocalInvPriceLevel invPriceLevel;
            try {
                invPriceLevel = invPriceLevelHome.findByIiNameAndAdLvPriceLevel(EJBCommon.itemName, arCustomer.getCstDealPrice(), companyCode);
                if (invPriceLevel.getPlAmount() == 0) {
                    unitPrice = invItem.getIiSalesPrice();
                } else {
                    unitPrice = invPriceLevel.getPlAmount();
                }
            }
            catch (FinderException ex) {
                unitPrice = invItem.getIiSalesPrice();
            }
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(EJBCommon.itemName, unitOfMeasure, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(EJBCommon.itemName, invItem.getInvUnitOfMeasure().getUomName(), companyCode);
            return EJBCommon.roundIt(unitPrice * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), commonData.getGlFcPrecisionUnit(companyCode));
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public boolean getArTraceMisc(String itemName, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getArTraceMisc");
        boolean isTraceMisc = false;
        try {
            LocalInvItem invItem = invItemHome.findByIiName(EJBCommon.itemName, companyCode);
            if (invItem.getIiTraceMisc() == 1) {
                isTraceMisc = true;
            }
            return isTraceMisc;
        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public boolean getInvIiNonInventoriableByIiName(String itemName, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getInvIiNonInventoriableByIiName");
        try {
            try {
                LocalInvItem invItem = invItemHome.findByIiName(EJBCommon.itemName, companyCode);
                return invItem.getIiNonInventoriable() != 1;
            }
            catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteArInvEntry(Integer invoiceCode, String userCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, ArINVInvoiceHasReceipt {

        Debug.print("ArInvoiceEntryControllerBean deleteArInvEntry");
        try {

            LocalArInvoice arInvoice = arInvoiceHome.findByPrimaryKey(invoiceCode);

            Collection arAppliedInvoices = arAppliedInvoiceHome.findAiByInvCode(invoiceCode, companyCode);
            if (arAppliedInvoices.size() > 0) {
                throw new ArINVInvoiceHasReceipt();
            }


            Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();
            for (Object invoiceLineItem : arInvoiceLineItems) {
                LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;
                double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                arInvoiceLineItem.getInvItemLocation().setIlCommittedQuantity(arInvoiceLineItem.getInvItemLocation().getIlCommittedQuantity() - convertedQuantity);
            }

            if (arInvoice.getInvApprovalStatus() != null && arInvoice.getInvApprovalStatus().equals(EJBCommon.PENDING)) {
                Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(EJBCommon.AR_INVOICE, arInvoice.getInvCode(), companyCode);
                for (Object approvalQueue : adApprovalQueues) {
                    LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;
                    em.remove(adApprovalQueue);
                }
            }

            if (!arInvoice.getArSalesOrderInvoiceLines().isEmpty()) {
                Iterator iterator = arInvoice.getArSalesOrderInvoiceLines().iterator();
                LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) iterator.next();
                LocalArSalesOrder arSalesOrder = arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder();
                arSalesOrderInvoiceLine.getArSalesOrderLine().getArSalesOrder().setSoLock(EJBCommon.FALSE);
                arSalesOrder.setSoApprovalStatus(null);
                arSalesOrder.setSoPosted(EJBCommon.FALSE);
                arSalesOrder.setSoPostedBy(null);
                arSalesOrder.setSoDatePosted(null);
            }

            if (!arInvoice.getArJobOrderInvoiceLines().isEmpty()) {
                Iterator iterator = arInvoice.getArJobOrderInvoiceLines().iterator();
                LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) iterator.next();
                LocalArJobOrder arJobOrder = arJobOrderInvoiceLine.getArJobOrderLine().getArJobOrder();
                arJobOrderInvoiceLine.getArJobOrderLine().getArJobOrder().setJoLock(EJBCommon.FALSE);
                arJobOrder.setJoApprovalStatus(null);
                arJobOrder.setJoPosted(EJBCommon.FALSE);
                arJobOrder.setJoPostedBy(null);
                arJobOrder.setJoDatePosted(null);
            }

            adDeleteAuditTrailHome.create(EJBCommon.AR_INVOICE, arInvoice.getInvDate(), arInvoice.getInvNumber(), arInvoice.getInvReferenceNumber(), arInvoice.getInvAmountDue(), userCode, new Date(), companyCode);
            em.remove(arInvoice);
        }
        catch (FinderException ex) {
            getSessionContext().setRollbackOnly();
            throw new GlobalRecordAlreadyDeletedException();
        }
        catch (ArINVInvoiceHasReceipt ex) {
            getSessionContext().setRollbackOnly();
            throw new ArINVInvoiceHasReceipt();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void recalculateCostOfSalesEntries() {

        Debug.print("ArInvoiceEntryControllerBean recalculateCostOfSalesEntries");
        Object[] obj;
        StringBuilder jbossQl = new StringBuilder();
        jbossQl.append("SELECT OBJECT(inv) FROM ArInvoice inv");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(1);
            obj = new Object[0];
            jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctType ='MISC' AND rct.rctVoid = 0");
            Collection arMiscReceipts = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, 0, 0);
            Iterator i = arMiscReceipts.iterator();
            while (i.hasNext()) {
                LocalArReceipt arMiscReceipt = (LocalArReceipt) i.next();
                Integer companyCode = arMiscReceipt.getRctAdCompany();
                Integer branchCode = arMiscReceipt.getRctAdBranch();
                Collection arDistributionRecords = arMiscReceipt.getArDistributionRecords();
                Iterator iDr = arDistributionRecords.iterator();
                while (iDr.hasNext()) {
                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) iDr.next();
                    if (arDistributionRecord.getDrClass().equals(EJBCommon.INVENTORY) || arDistributionRecord.getDrClass().equals(EJBCommon.COST_OF_GOODS_SOLD)) {
                        iDr.remove();
                        em.remove(arDistributionRecord);
                    }
                }
                Collection arInvoiceLineItems = arMiscReceipt.getArInvoiceLineItems();
                for (Object invoiceLineItem : arInvoiceLineItems) {
                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;
                    LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();
                    double unitCost = invItemLocation.getInvItem().getIiUnitCost();
                    try {
                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arMiscReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

                        // check if rmning vl is not zero and rmng qty is 0
                        if (invCosting.getCstRemainingQuantity() <= 0 && invCosting.getCstRemainingValue() <= 0) {
                            HashMap criteria = new HashMap();
                            criteria.put("itemName", invItemLocation.getInvItem().getIiName());
                            criteria.put("location", invItemLocation.getInvLocation().getLocName());
                            ArrayList branchList = new ArrayList();
                            AdBranchDetails mdetails = new AdBranchDetails();
                            mdetails.setBrCode(branchCode);
                            branchList.add(mdetails);
                            ejbRIC.executeInvFixItemCosting(criteria, branchList, companyCode);
                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arMiscReceipt.getRctDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        }

                        switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                            case EJBCommon.Average:
                                unitCost = invCosting.getCstRemainingQuantity() <= 0 ? invCosting.getInvItemLocation().getInvItem().getIiUnitCost() : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                                if (unitCost <= 0) {
                                    unitCost = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                                }
                                break;
                            case EJBCommon.FIFO:
                                unitCost = Math.abs(this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), arInvoiceLineItem.getIliQuantity(), arInvoiceLineItem.getIliUnitPrice(), false, branchCode, companyCode));
                                break;
                            case EJBCommon.Standard:
                                unitCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                                break;
                        }
                    }
                    catch (FinderException ex) {

                        unitCost = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double quantitySold = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                    LocalAdBranchItemLocation adBranchItemLocation = null;
                    try {
                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), branchCode, companyCode);
                    }
                    catch (FinderException ex) {

                    }

                    if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass().equals("Stock") && arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

                        if (adBranchItemLocation != null) {
                            this.addArDrIliEntry(arMiscReceipt.getArDrNextLine(), EJBCommon.COST_OF_GOODS_SOLD, EJBCommon.TRUE, unitCost * quantitySold, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arMiscReceipt, branchCode, companyCode);
                            this.addArDrIliEntry(arMiscReceipt.getArDrNextLine(), EJBCommon.INVENTORY, EJBCommon.FALSE, unitCost * quantitySold, adBranchItemLocation.getBilCoaGlInventoryAccount(), arMiscReceipt, branchCode, companyCode);

                        } else {

                            this.addArDrIliEntry(arMiscReceipt.getArDrNextLine(), EJBCommon.COST_OF_GOODS_SOLD, EJBCommon.TRUE, unitCost * quantitySold, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arMiscReceipt, branchCode, companyCode);

                            this.addArDrIliEntry(arMiscReceipt.getArDrNextLine(), EJBCommon.INVENTORY, EJBCommon.FALSE, unitCost * quantitySold, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arMiscReceipt, branchCode, companyCode);
                        }

                        // add quantity to item location committed quantity
                        double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                        invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);
                    }
                }
            }

            obj = new Object[0];
            jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(inv) FROM ArInvoice inv");
            Collection arInvoices = arInvoiceHome.getInvByCriteria(jbossQl.toString(), obj, 0, 0);

            i = arInvoices.iterator();

            while (i.hasNext()) {
                LocalArInvoice arInvoice = (LocalArInvoice) i.next();
                Integer companyCode = arInvoice.getInvAdCompany();
                Integer branchCode = arInvoice.getInvAdBranch();
                // Sales Order
                Iterator iSol = arInvoice.getArSalesOrderInvoiceLines().iterator();
                Collection arDistributionRecords = arInvoice.getArDistributionRecords();
                Iterator iDr = arDistributionRecords.iterator();
                while (iDr.hasNext()) {
                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) iDr.next();
                    if (arDistributionRecord.getDrClass().equals(EJBCommon.INVENTORY) || arDistributionRecord.getDrClass().equals(EJBCommon.COST_OF_GOODS_SOLD)) {
                        iDr.remove();
                        em.remove(arDistributionRecord);
                    }
                }

                // new dist
                LocalInvItemLocation invItemLocation;
                while (iSol.hasNext()) {
                    LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) iSol.next();
                    try {
                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvLocation().getLocName(), arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiName(), companyCode);
                    }
                    catch (FinderException ex) {
                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(arSalesOrderInvoiceLine.getArSalesOrderLine().getSolLine()));
                    }

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    // add cost of sales distribution and inventory
                    double unitCost = invItemLocation.getInvItem().getIiUnitCost();
                    try {
                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        // check if rmning vl is not zero and rmng qty is 0
                        if (invCosting.getCstRemainingQuantity() <= 0) {
                            HashMap criteria = new HashMap();
                            criteria.put("itemName", invItemLocation.getInvItem().getIiName());
                            criteria.put("location", invItemLocation.getInvLocation().getLocName());
                            ArrayList branchList = new ArrayList();
                            AdBranchDetails mdetails = new AdBranchDetails();
                            mdetails.setBrCode(branchCode);
                            branchList.add(mdetails);
                            ejbRIC.executeInvFixItemCosting(criteria, branchList, companyCode);
                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        }

                        if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.Average)) {
                            unitCost = invCosting.getCstRemainingQuantity() <= 0 ? unitCost : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                        }

                        if (unitCost <= 0) {
                            arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiUnitCost();
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.FIFO)) {
                            unitCost = invCosting.getCstRemainingQuantity() == 0 ? unitCost : this.getInvFifoCost(arInvoice.getInvDate(), invItemLocation.getIlCode(), arSalesOrderInvoiceLine.getSilQuantityDelivered(), arSalesOrderInvoiceLine.getArSalesOrderLine().getSolUnitPrice(), false, branchCode, companyCode);
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.Standard)) {
                            unitCost = invItemLocation.getInvItem().getIiUnitCost();
                        }

                    }
                    catch (FinderException ex) {

                        unitCost = arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double quantitySold = this.convertByUomFromAndItemAndQuantity(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvUnitOfMeasure(), arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem(), arSalesOrderInvoiceLine.getSilQuantityDelivered(), companyCode);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getIlCode(), branchCode, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    if (adPreference.getPrfArAutoComputeCogs() == EJBCommon.TRUE && arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.COST_OF_GOODS_SOLD, EJBCommon.TRUE, unitCost * quantitySold, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.INVENTORY, EJBCommon.FALSE, unitCost * quantitySold, adBranchItemLocation.getBilCoaGlInventoryAccount(), arInvoice, branchCode, companyCode);

                        } else {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.COST_OF_GOODS_SOLD, EJBCommon.TRUE, unitCost * quantitySold, arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.INVENTORY, EJBCommon.FALSE, unitCost * quantitySold, arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, branchCode, companyCode);
                        }
                    }
                }

                // line item

                Collection arInvoiceLineitems = arInvoice.getArInvoiceLineItems();

                for (Object arInvoiceLineitem : arInvoiceLineitems) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) arInvoiceLineitem;

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName(), arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(arInvoiceLineItem.getIliLine()));
                    }

                    // start date validation

                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {

                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    // add cost of sales distribution and inventory

                    double unitCost = invItemLocation.getInvItem().getIiUnitCost();

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

                        // check if rmning vl is not zero and rmng qty is 0
                        if (invItemLocation.getInvItem().getIiNonInventoriable() == (byte) 0 && invCosting.getCstRemainingQuantity() <= 0 && invCosting.getCstRemainingValue() <= 0) {

                            HashMap criteria = new HashMap();
                            criteria.put("itemName", invItemLocation.getInvItem().getIiName());
                            criteria.put("location", invItemLocation.getInvLocation().getLocName());

                            ArrayList branchList = new ArrayList();

                            AdBranchDetails mdetails = new AdBranchDetails();
                            mdetails.setBrCode(branchCode);
                            branchList.add(mdetails);

                            ejbRIC.executeInvFixItemCosting(criteria, branchList, companyCode);

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        }

                        if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.Average)) {

                            if (invCosting.getCstRemainingQuantity() <= 0) {
                                unitCost = invItemLocation.getInvItem().getIiUnitCost();
                            } else {
                                unitCost = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                if (unitCost <= 0) {
                                    unitCost = invItemLocation.getInvItem().getIiUnitCost();
                                }
                            }
                        }
                        if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.FIFO)) {

                            unitCost = this.getInvFifoCost(arInvoice.getInvDate(), invItemLocation.getIlCode(), arInvoiceLineItem.getIliQuantity(), arInvoiceLineItem.getIliUnitPrice(), false, branchCode, companyCode);

                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.Standard)) {

                            unitCost = invItemLocation.getInvItem().getIiUnitCost();
                        }

                    }
                    catch (FinderException ex) {

                        unitCost = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double quantitySold = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), branchCode, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    if (adPreference.getPrfArAutoComputeCogs() == EJBCommon.TRUE && arInvoiceLineItem.getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.COST_OF_GOODS_SOLD, EJBCommon.TRUE, unitCost * quantitySold, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.INVENTORY, EJBCommon.FALSE, unitCost * quantitySold, adBranchItemLocation.getBilCoaGlInventoryAccount(), arInvoice, branchCode, companyCode);

                        } else {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.COST_OF_GOODS_SOLD, EJBCommon.TRUE, unitCost * quantitySold, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.INVENTORY, EJBCommon.FALSE, unitCost * quantitySold, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, branchCode, companyCode);
                        }
                    }
                }

                // job order entry

                Collection jobOrderInvoiceLines = arInvoice.getArJobOrderInvoiceLines();

                for (Object jobOrderInvoiceLine : jobOrderInvoiceLines) {

                    LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) jobOrderInvoiceLine;

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getInvLocation().getLocName(), arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getInvItem().getIiName(), companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(arJobOrderInvoiceLine.getArJobOrderLine().getJolLine()));
                    }

                    // start date validation

                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {

                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    // add cost of sales distribution and inventory

                    double unitCost = invItemLocation.getInvItem().getIiUnitCost();

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

                        // check if rmning vl is not zero and rmng qty is 0
                        if (invCosting.getCstRemainingQuantity() <= 0) {

                            HashMap criteria = new HashMap();
                            criteria.put("itemName", invItemLocation.getInvItem().getIiName());
                            criteria.put("location", invItemLocation.getInvLocation().getLocName());

                            ArrayList branchList = new ArrayList();

                            AdBranchDetails mdetails = new AdBranchDetails();
                            mdetails.setBrCode(branchCode);
                            branchList.add(mdetails);

                            ejbRIC.executeInvFixItemCosting(criteria, branchList, companyCode);
                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                        }

                        if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.Average)) {
                            unitCost = invCosting.getCstRemainingQuantity() <= 0 ? unitCost : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                        }

                        if (unitCost <= 0) {
                            unitCost = invItemLocation.getInvItem().getIiUnitCost();
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.FIFO)) {
                            unitCost = invCosting.getCstRemainingQuantity() == 0 ? unitCost : this.getInvFifoCost(arInvoice.getInvDate(), invItemLocation.getIlCode(), arJobOrderInvoiceLine.getJilQuantityDelivered(), arJobOrderInvoiceLine.getArJobOrderLine().getJolUnitPrice(), false, branchCode, companyCode);
                        } else if (invCosting.getInvItemLocation().getInvItem().getIiCostMethod().equals(EJBCommon.Standard)) {
                            unitCost = invItemLocation.getInvItem().getIiUnitCost();
                        }

                    }
                    catch (FinderException ex) {

                        unitCost = arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double quantitySold = this.convertByUomFromAndItemAndQuantity(arJobOrderInvoiceLine.getArJobOrderLine().getInvUnitOfMeasure(), arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getInvItem(), arJobOrderInvoiceLine.getJilQuantityDelivered(), companyCode);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getIlCode(), branchCode, companyCode);

                    }
                    catch (FinderException ex) {

                    }

                    // Branch adding in Job Order
                    if (adPreference.getPrfArAutoComputeCogs() == EJBCommon.TRUE && arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getInvItem().getIiNonInventoriable() == EJBCommon.FALSE) {

                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.COST_OF_GOODS_SOLD, EJBCommon.TRUE, unitCost * quantitySold, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.INVENTORY, EJBCommon.FALSE, unitCost * quantitySold, adBranchItemLocation.getBilCoaGlInventoryAccount(), arInvoice, branchCode, companyCode);

                        } else {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.COST_OF_GOODS_SOLD, EJBCommon.TRUE, unitCost * quantitySold, arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, branchCode, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.INVENTORY, EJBCommon.FALSE, unitCost * quantitySold, arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, branchCode, companyCode);
                        }
                    }
                }

                // create journal entry
                try {
                    LocalGlJournal glJournal = glJournalHome.findByJrDocumentNumberAndJsNameAndBrCode(arInvoice.getInvNumber(), arInvoice.getInvReferenceNumber(), arInvoice.getInvAdBranch(), arInvoice.getInvAdCompany());

                    Collection glJournalLines = glJournal.getGlJournalLines();

                    for (Object journalLine : glJournalLines) {

                        LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                        // glJournalLine.remove();
                        em.remove(glJournalLine);
                    }

                    Collection arNewDistributionRecords = arInvoice.getArDistributionRecords();

                    for (Object distributionRecord : arDistributionRecords) {

                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                        double distributionRecordAmount = 0d;

                        if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                            distributionRecordAmount = arDistributionRecord.getDrAmount();

                        } else {

                            distributionRecordAmount = arDistributionRecord.getDrAmount();
                        }

                        LocalGlJournalLine glJournalLine = glJournalLineHome.create(arDistributionRecord.getDrLine(), arDistributionRecord.getDrDebit(), distributionRecordAmount, "", companyCode);

                        glJournalLine.setGlChartOfAccount(arDistributionRecord.getGlChartOfAccount());

                        glJournalLine.setGlJournal(glJournal);
                        arDistributionRecord.setDrImported(EJBCommon.TRUE);
                    }

                }
                catch (FinderException ex) {
                    continue;
                }

                // create journal lines

            }

        }
        catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveReceivedDate(ArInvoiceDetails details, Integer branchCode, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean saveRecievedDate");
        LocalArInvoice arInvoice = null;
        try {

            arInvoice = arInvoiceHome.findByPrimaryKey(details.getInvCode());
            arInvoice.setInvReceiveDate(details.getInvReceiveDate());

        }
        catch (FinderException ex) {
        }
    }

    public void saveDisableInterest(ArInvoiceDetails details, Integer branchCode, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean saveDisableInterest");
        LocalArInvoice arInvoice = null;
        try {

            arInvoice = arInvoiceHome.findByPrimaryKey(details.getInvCode());
            arInvoice.setInvDisableInterest(details.getInvDisableInterest());

        }
        catch (FinderException ex) {
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByInvCode(Integer invoiceCode, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getAdApprovalNotifiedUsersByInvCode");
        ArrayList list = new ArrayList();
        try {

            LocalArInvoice arInvoice = arInvoiceHome.findByPrimaryKey(invoiceCode);

            if (arInvoice.getInvPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(EJBCommon.AR_INVOICE, invoiceCode, companyCode);

            for (Object approvalQueue : adApprovalQueues) {

                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                list.add(adApprovalQueue.getAdUser().getUsrDescription());
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArNotIssuedSolBySoNumberAndCustomer(String soDocumentNumber, String customerCode, ArrayList issuedSolList, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArInvoiceEntryControllerBean getArNotIssuedSolBySoNumberAndCustomer");
        try {

            LocalArSalesOrder arSalesOrder = null;

            try {

                arSalesOrder = arSalesOrderHome.findBySoDocumentNumberAndCstCustomerCode(soDocumentNumber, customerCode, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList solList = new ArrayList();

            Collection arSalesOrderLines = arSalesOrder.getArSalesOrderLines();

            for (Object salesOrderLine : arSalesOrderLines) {

                LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) salesOrderLine;

                boolean issued = false;

                for (Object o : issuedSolList) {

                    Integer issuedSolCode = (Integer) o;

                    if (arSalesOrderLine.getSolCode().equals(issuedSolCode)) {

                        issued = true;
                        break;
                    }
                }

                if (!issued) {

                    ArModSalesOrderLineDetails mdetails = new ArModSalesOrderLineDetails();

                    mdetails.setSolCode(arSalesOrderLine.getSolCode());
                    mdetails.setSolLine(arSalesOrderLine.getSolLine());
                    mdetails.setSolUnitPrice(arSalesOrderLine.getSolUnitPrice());
                    mdetails.setSolIiName(arSalesOrderLine.getInvItemLocation().getInvItem().getIiName());
                    mdetails.setSolLocName(arSalesOrderLine.getInvItemLocation().getInvLocation().getLocName());
                    mdetails.setSolUomName(arSalesOrderLine.getInvUnitOfMeasure().getUomName());
                    mdetails.setSolIiDescription(arSalesOrderLine.getInvItemLocation().getInvItem().getIiDescription());
                    mdetails.setSolRemaining(arSalesOrderLine.getSolQuantity());
                    mdetails.setSolQuantity(arSalesOrderLine.getSolQuantity());
                    mdetails.setSolIssue(false);
                    mdetails.setSolDiscount1(arSalesOrderLine.getSolDiscount1());
                    mdetails.setSolDiscount2(arSalesOrderLine.getSolDiscount2());
                    mdetails.setSolDiscount3(arSalesOrderLine.getSolDiscount3());
                    mdetails.setSolDiscount4(arSalesOrderLine.getSolDiscount4());
                    mdetails.setSolTotalDiscount(arSalesOrderLine.getSolTotalDiscount());
                    mdetails.setSolTax(arSalesOrderLine.getSolTax());
                    mdetails.setTraceMisc(arSalesOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc());

                    if (mdetails.getTraceMisc() == 1) {
                        ArrayList tagList = this.getInvTagList(arSalesOrderLine);

                        mdetails.setSolTagList(tagList);
                    }

                    solList.add(mdetails);
                }
            }

            return solList;

        }
        catch (GlobalNoRecordFoundException ex) {

            Debug.printStackTrace(ex);
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArNotIssuedJolByJoNumberAndCustomer(String joDocumentNumber, String customerCode, ArrayList issuedJolList, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArInvoiceEntryControllerBean getArNotIssuedJolByJoNumberAndCustomer");
        try {

            LocalArJobOrder arJobOrder = null;

            try {

                arJobOrder = arJobOrderHome.findByJoDocumentNumberAndCstCustomerCode(joDocumentNumber, customerCode, companyCode);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList jolList = new ArrayList();

            Collection arJobOrderLines = arJobOrder.getArJobOrderLines();

            for (Object jobOrderLine : arJobOrderLines) {

                LocalArJobOrderLine arJobOrderLine = (LocalArJobOrderLine) jobOrderLine;

                boolean issued = false;

                for (Object o : issuedJolList) {

                    Integer issuedJolCode = (Integer) o;

                    if (arJobOrderLine.getJolCode().equals(issuedJolCode)) {

                        issued = true;
                        break;
                    }
                }

                if (!issued) {

                    ArModJobOrderLineDetails mdetails = new ArModJobOrderLineDetails();

                    mdetails.setJolCode(arJobOrderLine.getJolCode());
                    mdetails.setJolLine(arJobOrderLine.getJolLine());
                    mdetails.setJolUnitPrice(arJobOrderLine.getJolUnitPrice());
                    mdetails.setJolIiName(arJobOrderLine.getInvItemLocation().getInvItem().getIiName());
                    mdetails.setJolLocName(arJobOrderLine.getInvItemLocation().getInvLocation().getLocName());
                    mdetails.setJolUomName(arJobOrderLine.getInvUnitOfMeasure().getUomName());
                    mdetails.setJolIiDescription(arJobOrderLine.getInvItemLocation().getInvItem().getIiDescription());
                    mdetails.setJolRemaining(arJobOrderLine.getJolQuantity());
                    mdetails.setJolQuantity(arJobOrderLine.getJolQuantity());
                    mdetails.setJolIssue(false);
                    mdetails.setJolDiscount1(arJobOrderLine.getJolDiscount1());
                    mdetails.setJolDiscount2(arJobOrderLine.getJolDiscount2());
                    mdetails.setJolDiscount3(arJobOrderLine.getJolDiscount3());
                    mdetails.setJolDiscount4(arJobOrderLine.getJolDiscount4());
                    mdetails.setJolTotalDiscount(arJobOrderLine.getJolTotalDiscount());
                    mdetails.setJolTax(arJobOrderLine.getJolTax());
                    mdetails.setTraceMisc(arJobOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc());

                    if (mdetails.getTraceMisc() == 1) {
                        ArrayList tagList = this.getInvTagList(arJobOrderLine);

                        mdetails.setJolTagList(tagList);
                    }

                    jolList.add(mdetails);
                }
            }

            return jolList;

        }
        catch (GlobalNoRecordFoundException ex) {

            Debug.printStackTrace(ex);
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvLitByCstLitName(String lineItemTemplateName, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArInvoiceEntryControllerBean getInvLitByCstLitName");
        try {

            LocalInvLineItemTemplate invLineItemTemplate = null;

            try {

                invLineItemTemplate = invLineItemTemplateHome.findByLitName(lineItemTemplateName, companyCode);

            }
            catch (FinderException ex) {

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

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArCstAllForGeneration(Integer branchCode, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getArCstAllForGeneration");
        ArrayList list = new ArrayList();
        try {
            Collection arCustomers = arCustomerHome.findEnabledCstAll(branchCode, companyCode);
            for (Object customer : arCustomers) {
                LocalArCustomer arCustomer = (LocalArCustomer) customer;
                // filtering payroll customer to ar customer
                if (arCustomer.getCstEnablePayroll() == EJBCommon.FALSE) {
                    list.add(arCustomer.getCstCustomerCode() + "#" + arCustomer.getCstNumbersParking() + "#" + arCustomer.getCstSquareMeter() + "#" + arCustomer.getCstRealPropertyTaxRate() + "#" + arCustomer.getArCustomerClass().getArTaxCode().getTcName() + "#" + arCustomer.getCstAssociationDuesRate());
                }
            }
            return list;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvCustomerBatchAll(Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getAdLvCustomerBatchAll");
        ArrayList list = new ArrayList();
        try {
            Collection adLookUpValues = adLookUpValueHome.findByLuName("AR CUSTOMER BATCH - SOA", companyCode);
            for (Object lookUpValue : adLookUpValues) {
                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;
                list.add(adLookUpValue.getLvName());
            }
            return list;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArCstAllByCustomerBatch(ArrayList customerBatchList, Integer branchCode, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getArCstAllByCustomerBatch");
        ArrayList list = new ArrayList();
        try {
            for (Object o : customerBatchList) {
                String customerBatch = o.toString();
                Collection arCustomers = arCustomerHome.findEnabledCstAllbyCustomerBatch(customerBatch, branchCode, companyCode);
                for (Object customer : arCustomers) {
                    LocalArCustomer arCustomer = (LocalArCustomer) customer;
                    list.add(arCustomer.getCstCustomerCode() + "#" + arCustomer.getCstNumbersParking() + "#" + arCustomer.getCstSquareMeter() + "#" + arCustomer.getCstRealPropertyTaxRate() + "#" + arCustomer.getArCustomerClass().getArTaxCode().getTcName() + "#" + arCustomer.getCstAssociationDuesRate());
                }
            }
            return list;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvUomByIiName(String itemName, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getInvUomByIiName");
        ArrayList list = new ArrayList();
        try {
            LocalInvItem invItem;
            LocalInvUnitOfMeasure invItemUnitOfMeasure;
            invItem = invItemHome.findByIiName(EJBCommon.itemName, companyCode);
            invItemUnitOfMeasure = invItem.getInvUnitOfMeasure();
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
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getInvIiClassByIiName(String itemName, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getInvIiClassByIiName");
        try {
            LocalInvItem invItem = invItemHome.findByIiName(itemName, companyCode);
            return invItem.getIiClass();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModInvoiceDetails getArInvByInvCode(Integer invoiceCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArInvoiceEntryControllerBean getArInvByInvCode");
        try {
            LocalArInvoice arInvoice;
            LocalArTaxCode arTaxCode;
            try {
                arInvoice = arInvoiceHome.findByPrimaryKey(invoiceCode);
                arTaxCode = arInvoice.getArTaxCode();
            }
            catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get invoice line items if any
            Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();
            Collection arInvoiceLines = arInvoice.getArInvoiceLines();
            Collection arSalesOrderInvoiceLines = arInvoice.getArSalesOrderInvoiceLines();
            Collection arJobOrderInvoiceLines = arInvoice.getArJobOrderInvoiceLines();

            if (!arInvoiceLineItems.isEmpty()) {
                invoiceLineItems(arTaxCode, list, arInvoiceLineItems);
            } else if (!arInvoiceLines.isEmpty()) {
                invoiceLines(arTaxCode, list, arInvoiceLines);
            } else if (!arSalesOrderInvoiceLines.isEmpty()) {
                salesOrderLines(arTaxCode, list, arSalesOrderInvoiceLines);
            } else if (!arJobOrderInvoiceLines.isEmpty()) {
                jobOrderLines(arTaxCode, list, arJobOrderInvoiceLines);
            }

            ArModInvoiceDetails invoiceDetails = getModInvoiceDetails(arInvoice);
            if (arInvoice.getArSalesperson() != null) {
                invoiceDetails.setInvSlpSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                invoiceDetails.setInvSlpName(arInvoice.getArSalesperson().getSlpName());
            }

            if (!arInvoiceLineItems.isEmpty()) {
                invoiceDetails.setInvIliList(list);
            } else if (!arInvoiceLines.isEmpty()) {
                invoiceDetails.setInvIlList(list);
            } else if (!arSalesOrderInvoiceLines.isEmpty()) {
                invoiceDetails.setInvSoNumber(arInvoice.getInvSoNumber());
                invoiceDetails.setInvSoList(list);
            } else if (!arJobOrderInvoiceLines.isEmpty()) {
                invoiceDetails.setInvJoNumber(arInvoice.getInvJoNumber());
                invoiceDetails.setInvJoList(list);
            }

            // get last due date
            Collection arInvoicePaymentSchedules = arInvoice.getArInvoicePaymentSchedules();
            if (!arInvoicePaymentSchedules.isEmpty()) {
                ArrayList arInvoicePaymentScheduleList = new ArrayList(arInvoicePaymentSchedules);
                LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) arInvoicePaymentScheduleList.get(arInvoicePaymentScheduleList.size() - 1);
                invoiceDetails.setInvDueDate(arInvoicePaymentSchedule.getIpsDueDate());
            }
            return invoiceDetails;

        }
        catch (GlobalNoRecordFoundException ex) {
            throw ex;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModCustomerDetails getArCstByCstCustomerCode(String customerCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArInvoiceEntryControllerBean getArCstByCstCustomerCode");
        try {
            LocalArCustomer arCustomer;
            try {
                arCustomer = arCustomerHome.findByCstCustomerCode(customerCode, companyCode);
            }
            catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }

            ArModCustomerDetails mdetails = new ArModCustomerDetails();
            mdetails.setCstCustomerCode(arCustomer.getCstCustomerCode());
            mdetails.setCstCcName((arCustomer.getArCustomerClass() == null) ? null : arCustomer.getArCustomerClass().getCcName());
            mdetails.setCstPytName(arCustomer.getAdPaymentTerm() != null ? arCustomer.getAdPaymentTerm().getPytName() : null);
            mdetails.setCstCcWtcName(arCustomer.getArCustomerClass().getArWithholdingTaxCode() != null ? arCustomer.getArCustomerClass().getArWithholdingTaxCode().getWtcName() : null);
            mdetails.setCstBillToAddress(arCustomer.getCstBillToAddress());
            mdetails.setCstShipToAddress(arCustomer.getCstShipToAddress());
            mdetails.setCstBillToAddress(arCustomer.getCstBillToAddress());
            mdetails.setCstBillToContact(arCustomer.getCstBillToContact());
            mdetails.setCstBillToAltContact(arCustomer.getCstBillToAltContact());
            mdetails.setCstBillToPhone(arCustomer.getCstBillToPhone());
            mdetails.setCstBillingHeader(arCustomer.getCstBillingHeader());
            mdetails.setCstBillingFooter(arCustomer.getCstBillingFooter());
            mdetails.setCstBillingHeader2(arCustomer.getCstBillingHeader2());
            mdetails.setCstBillingFooter2(arCustomer.getCstBillingFooter2());
            mdetails.setCstBillingHeader3(arCustomer.getCstBillingHeader3());
            mdetails.setCstBillingFooter3(arCustomer.getCstBillingFooter3());
            mdetails.setCstBillingSignatory(arCustomer.getCstBillingSignatory());
            mdetails.setCstSignatoryTitle(arCustomer.getCstSignatoryTitle());
            mdetails.setCstShipToAddress(arCustomer.getCstShipToAddress());
            mdetails.setCstShipToContact(arCustomer.getCstShipToContact());
            mdetails.setCstShipToAltContact(arCustomer.getCstShipToAltContact());
            mdetails.setCstShipToPhone(arCustomer.getCstShipToPhone());
            mdetails.setCstName(arCustomer.getCstName());
            mdetails.setCstNumbersParking(arCustomer.getCstNumbersParking());
            mdetails.setCstSquareMeter(arCustomer.getCstSquareMeter());
            mdetails.setCstAssociationDuesRate(arCustomer.getCstAssociationDuesRate());
            mdetails.setCstRealPropertyTaxRate(arCustomer.getCstRealPropertyTaxRate());

            if (arCustomer.getArSalesperson() != null && arCustomer.getCstArSalesperson2() == null) {
                mdetails.setCstSlpSalespersonCode(arCustomer.getArSalesperson().getSlpSalespersonCode());
                mdetails.setCstSlpName(arCustomer.getArSalesperson().getSlpName());
            } else if (arCustomer.getArSalesperson() == null && arCustomer.getCstArSalesperson2() != null) {
                LocalArSalesperson arSalesperson2;
                arSalesperson2 = arSalespersonHome.findByPrimaryKey(arCustomer.getCstArSalesperson2());
                mdetails.setCstSlpSalespersonCode(arSalesperson2.getSlpSalespersonCode());
                mdetails.setCstSlpName(arSalesperson2.getSlpName());
            }

            if (arCustomer.getArSalesperson() != null && arCustomer.getCstArSalesperson2() != null) {
                mdetails.setCstSlpSalespersonCode(arCustomer.getArSalesperson().getSlpSalespersonCode());
                mdetails.setCstSlpName(arCustomer.getArSalesperson().getSlpName());
                LocalArSalesperson arSalesperson2;
                arSalesperson2 = arSalespersonHome.findByPrimaryKey(arCustomer.getCstArSalesperson2());
                mdetails.setCstSlpSalespersonCode2(arSalesperson2.getSlpSalespersonCode());
                mdetails.setCstSlpName2(arSalesperson2.getSlpName());
            }

            if (arCustomer.getArCustomerClass().getArTaxCode() != null) {
                mdetails.setCstCcTcName(arCustomer.getArCustomerClass().getArTaxCode().getTcName());
                mdetails.setCstCcTcRate(arCustomer.getArCustomerClass().getArTaxCode().getTcRate());
                mdetails.setCstCcTcType(arCustomer.getArCustomerClass().getArTaxCode().getTcType());
            }
            return mdetails;

        }
        catch (GlobalNoRecordFoundException ex) {
            throw ex;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArStandardMemoLineDetails getArSmlByCstCstmrCodeSmlNm(String customerCode, String standardMemoLineName, int branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArInvoiceEntryControllerBean getArSmlByCstCstmrCodeSmlNm");
        try {
            LocalArStandardMemoLine arStandardMemoLine;
            LocalArStandardMemoLineClass arStandardMemoLineClass;
            LocalArCustomer arCustomer;
            ArStandardMemoLineDetails details = new ArStandardMemoLineDetails();
            try {
                arStandardMemoLine = arStandardMemoLineHome.findBySmlName(standardMemoLineName, companyCode);
                details.setSmlDescription(arStandardMemoLine.getSmlDescription());
                details.setSmlUnitPrice(arStandardMemoLine.getSmlUnitPrice());
                details.setSmlTax(arStandardMemoLine.getSmlTax());
            }
            catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }

            try {
                arCustomer = arCustomerHome.findByCstCustomerCode(customerCode, companyCode);
                if (arCustomer.getArCustomerClass() != null) {
                    String customerClass = arCustomer.getArCustomerClass().getCcName();
                    // find memo line class in null customer code
                    try {
                        arStandardMemoLineClass = arStandardMemoLineClassHome.findSmcByCcNameSmlNameCstCstmrCodeAdBrnch(customerClass, standardMemoLineName, customerCode, branchCode, companyCode);
                        details.setSmlDescription(arStandardMemoLineClass.getSmcStandardMemoLineDescription());
                        details.setSmlUnitPrice(arStandardMemoLineClass.getSmcUnitPrice());
                    }
                    catch (FinderException ex) {
                        // find memo line class in not null customre code
                        arStandardMemoLineClass = arStandardMemoLineClassHome.findSmcByCcNameSmlNameAdBrnch(customerClass, standardMemoLineName, branchCode, companyCode);
                        details.setSmlDescription(arStandardMemoLineClass.getSmcStandardMemoLineDescription());
                        details.setSmlUnitPrice(arStandardMemoLineClass.getSmcUnitPrice());
                    }
                }
            }
            catch (FinderException ex) {
            }
            return details;

        }
        catch (GlobalNoRecordFoundException ex) {
            throw ex;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArStandardMemoLineDetails getArSmlBySmlName(String standardMemoLineName, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArInvoiceEntryControllerBean getArSmlBySmlName");
        try {
            LocalArStandardMemoLine arStandardMemoLine;
            try {
                arStandardMemoLine = arStandardMemoLineHome.findBySmlName(standardMemoLineName, companyCode);
            }
            catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }
            ArStandardMemoLineDetails details = new ArStandardMemoLineDetails();
            details.setSmlDescription(arStandardMemoLine.getSmlDescription());
            details.setSmlUnitPrice(arStandardMemoLine.getSmlUnitPrice());
            details.setSmlTax(arStandardMemoLine.getSmlTax());
            return details;

        }
        catch (GlobalNoRecordFoundException ex) {
            throw ex;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModSalesOrderDetails getArSoBySoDocumentNumberAndCstCustomerCodeAndAdBranch(String soDocumentNumber, String customerCode, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException, ArINVNoSalesOrderLinesFoundException {

        Debug.print("ArInvoiceEntryControllerBean getArSoBySoDocumentNumberAndCstCustomerCodeAndAdBranch");
        ArrayList list = new ArrayList();
        LocalArSalesOrder arSalesOrder;
        try {
            if (customerCode == null || customerCode.equals("")) {
                arSalesOrder = arSalesOrderHome.findBySoDocumentNumberAndBrCode(soDocumentNumber, branchCode, companyCode);
            } else {
                arSalesOrder = arSalesOrderHome.findBySoDocumentNumberAndCstCustomerCodeAndBrCode(soDocumentNumber, customerCode, branchCode, companyCode);
            }
            if (arSalesOrder.getSoPosted() != EJBCommon.TRUE) {
                throw new GlobalNoRecordFoundException();
            }
            // get sales order line
            Collection arSalesOrderLines = arSalesOrder.getArSalesOrderLines();
            for (Object salesOrderLine : arSalesOrderLines) {
                LocalArSalesOrderLine arSalesOrderLine = (LocalArSalesOrderLine) salesOrderLine;
                ArModSalesOrderLineDetails solDetails = new ArModSalesOrderLineDetails();
                solDetails.setSolCode(arSalesOrderLine.getSolCode());
                solDetails.setSolLine(arSalesOrderLine.getSolLine());
                solDetails.setSolUnitPrice(arSalesOrderLine.getSolUnitPrice());
                solDetails.setSolIiName(arSalesOrderLine.getInvItemLocation().getInvItem().getIiName());
                solDetails.setSolLocName(arSalesOrderLine.getInvItemLocation().getInvLocation().getLocName());
                solDetails.setSolUomName(arSalesOrderLine.getInvUnitOfMeasure().getUomName());
                solDetails.setSolIiDescription(arSalesOrderLine.getSolLineIDesc());

                Iterator j = arSalesOrderLine.getArSalesOrderInvoiceLines().iterator();
                double quantitySold = 0d;
                double totalSoldAmount = 0d;
                double totalDiscountAmount = 0d;

                // get qty sold
                while (j.hasNext()) {
                    LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) j.next();
                    if (arSalesOrderInvoiceLine.getArInvoice().getInvPosted() == EJBCommon.TRUE) {
                        quantitySold += arSalesOrderInvoiceLine.getSilQuantityDelivered();
                        if (arSalesOrder.getArTaxCode().getTcType().equals(EJBCommon.EXCLUSIVE)) {
                            totalSoldAmount += arSalesOrderInvoiceLine.getSilAmount() + arSalesOrderInvoiceLine.getSilTaxAmount();
                        } else {
                            totalSoldAmount += arSalesOrderInvoiceLine.getSilAmount();
                        }
                        totalDiscountAmount += arSalesOrderInvoiceLine.getSilTotalDiscount();
                    }
                }

                // total qty - qty sold
                double totalRemainingQuantity = arSalesOrderLine.getSolQuantity() - quantitySold;
                double totalRemainingAmount = arSalesOrderLine.getSolAmount() - totalSoldAmount;
                double totalRemainingDiscountAmount = arSalesOrderLine.getSolTotalDiscount() - totalDiscountAmount;
                if (totalRemainingQuantity == 0) {
                    continue;
                }

                solDetails.setSolRemaining(totalRemainingQuantity);
                solDetails.setSolQuantity(totalRemainingQuantity);
                solDetails.setSolIssue(false);
                solDetails.setSolDiscount1(arSalesOrderLine.getSolDiscount1());
                solDetails.setSolDiscount2(arSalesOrderLine.getSolDiscount2());
                solDetails.setSolDiscount3(arSalesOrderLine.getSolDiscount3());
                solDetails.setSolDiscount4(arSalesOrderLine.getSolDiscount4());
                solDetails.setSolTotalDiscount(totalRemainingDiscountAmount);
                solDetails.setSolMisc(arSalesOrderLine.getSolMisc());
                solDetails.setSolTax(arSalesOrderLine.getSolTax());
                solDetails.setSolAmount(totalRemainingAmount);
                solDetails.setTraceMisc(arSalesOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc());
                if (solDetails.getTraceMisc() == 1) {
                    ArrayList tagList = this.getInvTagList(arSalesOrderLine);
                    solDetails.setSolTagList(tagList);
                }
                list.add(solDetails);
            }

            if (list == null || list.size() == 0) {
                throw new ArINVNoSalesOrderLinesFoundException();
            }

            ArModSalesOrderDetails mdetails = new ArModSalesOrderDetails();
            mdetails.setSoCode(arSalesOrder.getSoCode());
            mdetails.setSoMemo(arSalesOrder.getSoMemo());
            mdetails.setSoTcName(arSalesOrder.getArTaxCode().getTcName());
            mdetails.setSoReferenceNumber(arSalesOrder.getSoReferenceNumber());
            mdetails.setSoTcType(arSalesOrder.getArTaxCode().getTcType());
            mdetails.setSoTcRate(arSalesOrder.getArTaxCode().getTcRate());
            mdetails.setSoFcName(arSalesOrder.getGlFunctionalCurrency().getFcName());
            mdetails.setSoPytName(arSalesOrder.getAdPaymentTerm().getPytName());
            mdetails.setSoSlpSalespersonCode(arSalesOrder.getArSalesperson() != null ? arSalesOrder.getArSalesperson().getSlpSalespersonCode() : null);
            mdetails.setSoConversionDate(arSalesOrder.getSoConversionDate());
            mdetails.setSoConversionRate(arSalesOrder.getSoConversionRate());
            mdetails.setSoShipTo(arSalesOrder.getSoShipTo());
            mdetails.setSoBillTo(arSalesOrder.getSoBillTo());
            if (customerCode == null || customerCode.equals("")) {
                mdetails.setSoCstCustomerCode(arSalesOrder.getArCustomer().getCstCustomerCode());
                mdetails.setSoCstName(arSalesOrder.getArCustomer().getCstName());
            }
            mdetails.setSoSolList(list);
            mdetails.setReportParameter(arSalesOrder.getReportParameter());
            return mdetails;
        }
        catch (FinderException ex) {
            throw new GlobalNoRecordFoundException();
        }
        catch (GlobalNoRecordFoundException | ArINVNoSalesOrderLinesFoundException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModJobOrderDetails getArJoByJoDocumentNumberAndCstCustomerCodeAndAdBranch(String joDocumentNumber, String customerCode, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException, ArINVNoSalesOrderLinesFoundException {

        Debug.print("ArInvoiceEntryControllerBean getArJoByJoDocumentNumberAndCstCustomerCodeAndAdBranch");
        ArrayList list = new ArrayList();
        LocalArJobOrder arJobOrder;
        try {
            if (customerCode == null || customerCode.equals("")) {
                arJobOrder = arJobOrderHome.findByJoDocumentNumberAndBrCode(joDocumentNumber, branchCode, companyCode);
            } else {
                arJobOrder = arJobOrderHome.findByJoDocumentNumberAndCstCustomerCodeAndBrCode(joDocumentNumber, customerCode, branchCode, companyCode);
            }
            if (arJobOrder.getJoPosted() != EJBCommon.TRUE) {
                throw new GlobalNoRecordFoundException();
            }

            // get sales order line
            Collection arJobOrderLines = arJobOrder.getArJobOrderLines();
            for (Object jobOrderLine : arJobOrderLines) {
                LocalArJobOrderLine arJobOrderLine = (LocalArJobOrderLine) jobOrderLine;
                ArModJobOrderLineDetails jolDetails = new ArModJobOrderLineDetails();
                jolDetails.setJolCode(arJobOrderLine.getJolCode());
                jolDetails.setJolLine(arJobOrderLine.getJolLine());
                jolDetails.setJolUnitPrice(arJobOrderLine.getJolUnitPrice());
                jolDetails.setJolIiName(arJobOrderLine.getInvItemLocation().getInvItem().getIiName());
                jolDetails.setJolLocName(arJobOrderLine.getInvItemLocation().getInvLocation().getLocName());
                jolDetails.setJolUomName(arJobOrderLine.getInvUnitOfMeasure().getUomName());
                jolDetails.setJolIiDescription(arJobOrderLine.getJolLineIDesc());

                Iterator j = arJobOrderLine.getArJobOrderInvoiceLines().iterator();
                double quantitySold = 0d;
                double totalSoldAmount = 0d;
                double totalDiscountAmount = 0d;

                // get qty sold
                while (j.hasNext()) {
                    LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) j.next();
                    if (arJobOrderInvoiceLine.getArInvoice().getInvPosted() == EJBCommon.TRUE) {
                        quantitySold += arJobOrderInvoiceLine.getJilQuantityDelivered();
                        if (arJobOrder.getArTaxCode().getTcType().equals("INCLUSIVE")) {
                            totalSoldAmount += arJobOrderInvoiceLine.getJilAmount() + arJobOrderInvoiceLine.getJilTaxAmount();
                        } else {
                            totalSoldAmount += arJobOrderInvoiceLine.getJilAmount();
                        }
                        totalDiscountAmount += arJobOrderInvoiceLine.getJilTotalDiscount();
                    }
                }

                // total qty - qty sold
                double totalRemainingQuantity = arJobOrderLine.getJolQuantity() - quantitySold;
                double totalRemainingAmount = arJobOrderLine.getJolAmount() - totalSoldAmount;
                double totalRemainingDiscountAmount = arJobOrderLine.getJolTotalDiscount() - totalDiscountAmount;
                if (totalRemainingQuantity == 0) {
                    continue;
                }

                jolDetails.setJolRemaining(totalRemainingQuantity);
                jolDetails.setJolQuantity(totalRemainingQuantity);
                jolDetails.setJolIssue(false);
                jolDetails.setJolDiscount1(arJobOrderLine.getJolDiscount1());
                jolDetails.setJolDiscount2(arJobOrderLine.getJolDiscount2());
                jolDetails.setJolDiscount3(arJobOrderLine.getJolDiscount3());
                jolDetails.setJolDiscount4(arJobOrderLine.getJolDiscount4());
                jolDetails.setJolTotalDiscount(totalRemainingDiscountAmount);
                jolDetails.setJolMisc(arJobOrderLine.getJolMisc());
                jolDetails.setJolTax(arJobOrderLine.getJolTax());
                jolDetails.setJolAmount(totalRemainingAmount);
                jolDetails.setTraceMisc(arJobOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc());

                if (jolDetails.getTraceMisc() == 1) {
                    ArrayList tagList = this.getInvTagList(arJobOrderLine);
                    jolDetails.setJolTagList(tagList);
                }
                list.add(jolDetails);
            }

            if (list == null || list.size() == 0) {
                throw new ArINVNoSalesOrderLinesFoundException();
            }

            ArModJobOrderDetails mdetails = new ArModJobOrderDetails();
            mdetails.setJoCode(arJobOrder.getJoCode());
            mdetails.setJoMemo(arJobOrder.getJoMemo());
            mdetails.setJoTcName(arJobOrder.getArTaxCode().getTcName());
            mdetails.setJoReferenceNumber(arJobOrder.getJoReferenceNumber());
            mdetails.setJoTcType(arJobOrder.getArTaxCode().getTcType());
            mdetails.setJoTcRate(arJobOrder.getArTaxCode().getTcRate());
            mdetails.setJoFcName(arJobOrder.getGlFunctionalCurrency().getFcName());
            mdetails.setJoPytName(arJobOrder.getAdPaymentTerm().getPytName());
            mdetails.setJoSlpSalespersonCode(arJobOrder.getArSalesperson() != null ? arJobOrder.getArSalesperson().getSlpSalespersonCode() : null);
            mdetails.setJoConversionDate(arJobOrder.getJoConversionDate());
            mdetails.setJoConversionRate(arJobOrder.getJoConversionRate());
            mdetails.setJoShipTo(arJobOrder.getJoShipTo());
            mdetails.setJoBillTo(arJobOrder.getJoBillTo());

            if (customerCode == null || customerCode.equals("")) {
                mdetails.setJoCstCustomerCode(arJobOrder.getArCustomer().getCstCustomerCode());
                mdetails.setJoCstName(arJobOrder.getArCustomer().getCstName());
            }

            mdetails.setJoJolList(list);
            return mdetails;

        }
        catch (FinderException ex) {
            throw new GlobalNoRecordFoundException();
        }
        catch (GlobalNoRecordFoundException | ArINVNoSalesOrderLinesFoundException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    //-- Introduced Public Methods
    public int checkExpiryDates(String misc) throws Exception {

        Debug.print("ArInvoiceEntryControllerBean checkExpiryDates");
        String separator = "$";
        // Remove first $ character
        misc = misc.substring(1);
        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;
        int numberExpry = 0;
        StringBuilder miscList = new StringBuilder();
        String miscList2 = "";
        String g = "";
        try {
            while (g != "fin") {
                // Date
                start = nextIndex + 1;
                nextIndex = misc.indexOf(separator, start);
                length = nextIndex - start;
                g = misc.substring(start, start + length);
                if (g.length() != 0) {
                    if (g != null || g != "" || g != "null") {
                        if (g.contains("null")) {
                            miscList2 = "Error";
                        } else {
                            miscList.append("$").append(g);
                            numberExpry++;
                        }
                    } else {
                        miscList2 = "Error";
                    }

                } else {
                    miscList2 = "Error";
                }
            }
        }
        catch (Exception e) {
        }
        if (miscList2 == "") {
            miscList.append("$");
        } else {
            miscList = new StringBuilder(miscList2);
        }
        return (numberExpry);
    }

    public String propagateExpiryDates(String misc, double qty, String reverse) throws Exception {

        Debug.print("ArInvoiceEntryControllerBean propagateExpiryDates");
        StringBuilder miscList = new StringBuilder();
        try {
            String separator = "";
            if (reverse == "False") {
                separator = "$";
            } else {
                separator = " ";
            }
            // Remove first $ character
            misc = misc.substring(1);
            // Counter
            int start = 0;
            int nextIndex = misc.indexOf(separator, start);
            int length = nextIndex - start;
            for (int x = 0; x < qty; x++) {
                // Date
                start = nextIndex + 1;
                nextIndex = misc.indexOf(separator, start);
                length = nextIndex - start;
                String g = misc.substring(start, start + length);
                if (g.length() != 0) {
                    miscList.append("$").append(g);
                }
            }
            miscList.append("$");
        }
        catch (Exception e) {
            miscList = new StringBuilder();
        }
        return (miscList.toString());
    }

    public String getQuantityExpiryDates(String qntty) {

        Debug.print("ArInvoiceEntryControllerBean getQuantityExpiryDates");
        String separator = "$";
        String y = "";
        try {
            // Remove first $ character
            qntty = qntty.substring(1);
            // Counter
            int start = 0;
            int nextIndex = qntty.indexOf(separator, start);
            int length = nextIndex - start;
            y = (qntty.substring(start, start + length));
        }
        catch (Exception e) {
            y = "0";
        }
        return y;
    }

    public String propagateExpiryDates(String misc, double qty) throws Exception {

        Debug.print("ArInvoiceEntryControllerBean propagateExpiryDates");
        String separator = "$";
        StringBuilder miscList = new StringBuilder();
        // Remove first $ character
        try {
            misc = misc.substring(1);
            // Counter
            int start = 0;
            int nextIndex = misc.indexOf(separator, start);
            int length = nextIndex - start;
            for (int x = 0; x < qty; x++) {
                // Date
                start = nextIndex + 1;
                nextIndex = misc.indexOf(separator, start);
                length = nextIndex - start;
                try {
                    miscList.append("$").append(misc.substring(start, start + length));
                }
                catch (Exception ex) {
                    throw ex;
                }
            }
            miscList.append("$");
        }
        catch (Exception e) {
            miscList = new StringBuilder();
        }
        return (miscList.toString());
    }

    private String getDistributionRecordClass(Integer companyCode, LocalAdBranchItemLocation adBranchItemLocation) {

        Debug.print("ArInvoiceEntryControllerBean getDistributionRecordClass");
        LocalGlChartOfAccount glSalesCoa;
        LocalGenValueSetValue genValueSetValue = null;
        try {
            glSalesCoa = glChartOfAccountHome.findByCoaCode(adBranchItemLocation.getBilCoaGlSalesAccount(), companyCode);
            genValueSetValue = getValueSetValueHome.findByVsvValue(glSalesCoa.getCoaSegment2(), companyCode);
        }
        catch (FinderException ex) {
        }

        String distributionRecordClass = EJBCommon.REVENUE;
        if (genValueSetValue.getVsvDescription().equalsIgnoreCase("3G DEFERRED REVENUE") || genValueSetValue.getVsvDescription().equalsIgnoreCase("4G DEFERRED REVENUE")) {
            distributionRecordClass = "LIABILITY";
        }
        return distributionRecordClass;
    }

    public String checkExpiryDates(String misc, double qty, String reverse) throws Exception {

        Debug.print("ArInvoiceEntryControllerBean checkExpiryDates");
        String separator = "";
        if (reverse == "False") {
            separator = "$";
        } else {
            separator = " ";
        }
        // Remove first $ character
        misc = misc.substring(1);
        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;
        StringBuilder miscList = new StringBuilder();
        String miscList2 = "";
        for (int x = 0; x < qty; x++) {
            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;
            String g = misc.substring(start, start + length);
            if (g.length() != 0) {
                if (g != null || g != "" || g != "null") {
                    if (g.contains("null")) {
                        miscList2 = "Error";
                    } else {
                        miscList.append("$").append(g);
                    }
                } else {
                    miscList2 = "Error";
                }
            } else {
                miscList2 = "Error";
            }
        }
        if (miscList2 == "") {
            miscList.append("$");
        } else {
            miscList = new StringBuilder(miscList2);
        }
        return (miscList.toString());
    }

    //-- Introduced Private Methods
    private void setupDocumentSequenceAssignment(ArInvoiceDetails invoiceDetails, Integer branchCode, Integer companyCode, LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment, LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment) {

        Debug.print("ArInvoiceEntryControllerBean setupDocumentSequenceAssignment");
        while (true) {
            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {
                try {
                    arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), EJBCommon.FALSE, branchCode, companyCode);
                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                }
                catch (FinderException ex) {
                    invoiceDetails.setInvNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                    break;
                }
            } else {
                try {
                    arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), EJBCommon.FALSE, branchCode, companyCode);
                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                }
                catch (FinderException ex) {
                    invoiceDetails.setInvNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                    break;
                }
            }
        }
    }

    private void isUploadNumberHaveDuplicate(ArInvoiceDetails invoiceDetails, Integer companyCode) throws ArInvDuplicateUploadNumberException {

        Debug.print("ArInvoiceEntryControllerBean isUploadNumberHaveDuplicate");
        if (invoiceDetails.getInvUploadNumber() != null) {
            if (!invoiceDetails.getInvUploadNumber().trim().equals("")) {
                try {
                    arInvoiceHome.findByUploadNumberAndCompanyCode(invoiceDetails.getInvUploadNumber(), companyCode);
                    // throw exception if found duplicate upload number
                    throw new ArInvDuplicateUploadNumberException();
                }
                catch (FinderException ex) {
                }
            }
        }
    }

    private LocalArInvoice isInvoiceDeleted(ArInvoiceDetails invoiceDetails, LocalArInvoice arInvoice) throws GlobalRecordAlreadyDeletedException {

        Debug.print("ArInvoiceEntryControllerBean isInvoiceDeleted");
        try {
            if (invoiceDetails.getInvCode() != null) {
                arInvoice = arInvoiceHome.findByPrimaryKey(invoiceDetails.getInvCode());
            }
        }
        catch (FinderException ex) {
            throw new GlobalRecordAlreadyDeletedException();
        }
        return arInvoice;
    }

    private double calculateWithholdingTax(Integer branchCode, Integer companyCode, LocalArInvoice arInvoice, LocalArWithholdingTaxCode arWithholdingTaxCode, LocalAdPreference adPreference, double totalLine) throws GlobalBranchAccountNumberInvalidException {

        double withholdingTaxAmount = 0d;
        if (arWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals(EJBCommon.INVOICE)) {
            withholdingTaxAmount = EJBCommon.roundIt(totalLine * (arWithholdingTaxCode.getWtcRate() / 100), commonData.getGlFcPrecisionUnit(companyCode));
            double convertedWithholdingTaxAmount = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), withholdingTaxAmount, companyCode);
            this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.WITHHOLDING_TAX, EJBCommon.TRUE, convertedWithholdingTaxAmount, arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), null, arInvoice, branchCode, companyCode);
        }
        return withholdingTaxAmount;
    }

    private void addUnearnedInterest(String invoiceBatchName, Integer branchCode, Integer companyCode, LocalArInvoice arInvoice, LocalAdPaymentTerm adPaymentTerm, LocalAdPreference adPreference, double totalTax, double totalLine, double totalInterestException, double totalDownpayment) throws GlobalBranchAccountNumberInvalidException {

        double unearnedInterestAmount = 0d;
        if (arInvoice.getArCustomer().getCstAutoComputeInterest() == EJBCommon.TRUE && adPaymentTerm.getPytEnableInterest() == EJBCommon.TRUE) {
            try {
                double MONTHLY_INTEREST_RATE = 0;
                MONTHLY_INTEREST_RATE = adPaymentTerm.getPytMonthlyInterestRate();
                if (adPreference.getPrfEnableArInvoiceBatch() == EJBCommon.TRUE) {
                    LocalArInvoiceBatch arInvoiceBatch = arInvoiceBatchHome.findByIbName(invoiceBatchName, branchCode, companyCode);
                    if (arInvoiceBatch.getIbAllowInterest() == EJBCommon.TRUE) {
                        MONTHLY_INTEREST_RATE = arInvoiceBatch.getIbInterestRate();
                    }
                }
                if (arInvoice.getArCustomer().getCstMonthlyInterestRate() > 0) {
                    MONTHLY_INTEREST_RATE = arInvoice.getArCustomer().getCstMonthlyInterestRate();
                }

                LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoice.getArCustomer().getCstCode(), branchCode, companyCode);

                unearnedInterestAmount = EJBCommon.roundIt((totalLine + totalTax - totalDownpayment - totalInterestException) * adPaymentTerm.getAdPaymentSchedules().size() * (MONTHLY_INTEREST_RATE / 100), commonData.getGlFcPrecisionUnit(companyCode));

                double convertedUnearnedInterestAmount = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), unearnedInterestAmount, companyCode);

                this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.UNINTEREST, EJBCommon.FALSE, convertedUnearnedInterestAmount, adBranchCustomer.getBcstGlCoaUnEarnedInterestAccount(), null, arInvoice, branchCode, companyCode);

                this.addArDrIliEntry(arInvoice.getArDrNextLine(), EJBCommon.RECEIVABLE_INTEREST, EJBCommon.TRUE, convertedUnearnedInterestAmount, adBranchCustomer.getBcstGlCoaReceivableAccount(), arInvoice, branchCode, companyCode);

            }
            catch (FinderException ex) {
            }
        }
        arInvoice.setInvAmountUnearnedInterest(unearnedInterestAmount);
    }

    private void isAllowedPriorDate(Integer companyCode, LocalArInvoice arInvoice, LocalAdPreference adPreference, String centralWarehouse, LocalAdBranch centralWarehouseBranchCode, LocalInvItemLocation invItemLocation) throws FinderException, GlobalInventoryDateException {

        Debug.print("ArInvoiceEntryControllerBean isAllowedPriorDate");
        if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
            Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), centralWarehouse, centralWarehouseBranchCode.getBrCode(), companyCode);
            if (!invNegTxnCosting.isEmpty()) {
                throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
            }
        }
    }

    private LocalInvItemLocation getItemLocation(Integer companyCode, ArModInvoiceLineItemDetails mIliDetails) throws GlobalInvItemLocationNotFoundException {

        Debug.print("ArInvoiceEntryControllerBean getItemLocation");
        LocalInvItemLocation invItemLocation;
        try {
            invItemLocation = invItemLocationHome.findByLocNameAndIiName(mIliDetails.getIliLocName(), mIliDetails.getIliIiName(), companyCode);
        }
        catch (FinderException ex) {
            throw new GlobalInvItemLocationNotFoundException(String.valueOf(mIliDetails.getIliLine()));
        }
        return invItemLocation;
    }

    private void setInvoiceBatch(String invoiceBatchName, Integer branchCode, Integer companyCode, LocalArInvoice arInvoice) {

        Debug.print("ArInvoiceEntryControllerBean setInvoiceBatch");
        try {
            LocalArInvoiceBatch arInvoiceBatch = arInvoiceBatchHome.findByIbName(invoiceBatchName, branchCode, companyCode);
            arInvoice.setArInvoiceBatch(arInvoiceBatch);
        }
        catch (FinderException ex) {
        }
    }

    private void isUploadNumberDuplicate(ArInvoiceDetails details, Integer companyCode, LocalArInvoice arExistingInvoice) throws ArInvDuplicateUploadNumberException {

        Debug.print("ArInvoiceEntryControllerBean isUploadNumberDuplicate");
        if (details.getInvUploadNumber() != null) {
            if (!details.getInvUploadNumber().trim().equals("")) {
                if (!details.getInvUploadNumber().equals(arExistingInvoice.getInvUploadNumber())) {
                    try {
                        arInvoiceHome.findByUploadNumberAndCompanyCode(details.getInvUploadNumber(), companyCode);
                        // throw exception if found duplicate upload number
                        throw new ArInvDuplicateUploadNumberException();
                    }
                    catch (FinderException ex) {
                    }
                }
            }
        }
    }

    private boolean isInvoiceVoid(ArInvoiceDetails details, LocalArInvoice arInvoice) {

        Debug.print("ArInvoiceEntryControllerBean isInvoiceVoid");
        if (details.getInvCode() != null && details.getInvVoid() == EJBCommon.TRUE && arInvoice.getInvPosted() == EJBCommon.FALSE) {
            arInvoice.setInvVoid(EJBCommon.TRUE);
            arInvoice.setInvLastModifiedBy(details.getInvLastModifiedBy());
            arInvoice.setInvDateLastModified(details.getInvDateLastModified());
            return true;
        }
        return false;
    }

    private void removeAllPaymentSchedule(LocalArInvoice arInvoice) throws RemoveException {

        Debug.print("ArInvoiceEntryControllerBean removeAllPaymentSchedule");
        Iterator i;
        Collection arInvoicePaymentSchedules = arInvoice.getArInvoicePaymentSchedules();
        i = arInvoicePaymentSchedules.iterator();
        while (i.hasNext()) {
            LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) i.next();
            i.remove();
            em.remove(arInvoicePaymentSchedule);
        }
    }

    private void addTaxDistributionRecord(Integer branchCode, Integer companyCode, LocalArInvoice arInvoice, LocalArTaxCode arTaxCode, double totalTax) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArInvoiceEntryControllerBean addTaxDistributionRecord");
        if (!arTaxCode.getTcType().equals(EJBCommon.NONE) && !arTaxCode.getTcType().equals(EJBCommon.EXEMPT)) {
            double convertedTotalTax = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), totalTax, companyCode);
            if (arTaxCode.getTcInterimAccount() == null) {
                // add branch tax code
                try {
                    LocalAdBranchArTaxCode adBranchTaxCode = adBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(arInvoice.getArTaxCode().getTcCode(), branchCode, companyCode);
                    this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.TAX, EJBCommon.FALSE, convertedTotalTax, adBranchTaxCode.getBtcGlCoaTaxCode(), null, arInvoice, branchCode, companyCode);
                }
                catch (FinderException ex) {
                    this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.TAX, EJBCommon.FALSE, convertedTotalTax, arTaxCode.getGlChartOfAccount().getCoaCode(), null, arInvoice, branchCode, companyCode);
                }
            } else {
                this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.DEFERRED_TAX, EJBCommon.FALSE, convertedTotalTax, arTaxCode.getTcInterimAccount(), null, arInvoice, branchCode, companyCode);
            }
        }
    }

    private void isDistributionRecordServiceCharge(Integer branchCode, Integer companyCode, LocalArInvoice arInvoice, LocalArInvoiceLine arInvoiceLine, double invoiceLine) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArInvoiceEntryControllerBean isDistributionRecordServiceCharge");
        if (arInvoiceLine.getArStandardMemoLine().getSmlType().equals(EJBCommon.SERVICE_CHARGE)) {
            if (arInvoiceLine.getArStandardMemoLine().getSmlInterimAccount() == null) {
                throw new GlobalBranchAccountNumberInvalidException();
            }
            this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.SERVICE_CHARGE, EJBCommon.FALSE, invoiceLine, arInvoiceLine.getArStandardMemoLine().getSmlInterimAccount(), this.getArGlCoaRevenueAccount(arInvoiceLine, branchCode, companyCode), arInvoice, branchCode, companyCode);
        } else {
            this.addArDrEntry(arInvoice.getArDrNextLine(), EJBCommon.REVENUE, EJBCommon.FALSE, invoiceLine, this.getArGlCoaRevenueAccount(arInvoiceLine, branchCode, companyCode), null, arInvoice, branchCode, companyCode);
        }
    }

    private void isConversionDateExistsSaveArInvEntry(ArInvoiceDetails details, String currencyName, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("ArInvoiceEntryControllerBean isConversionDateExistsSaveArInvEntry");
        try {
            if (details.getInvConversionDate() != null) {
                LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(currencyName, companyCode);
                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {
                    glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getInvConversionDate(), companyCode);
                } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {
                    glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getInvConversionDate(), companyCode);
                }
            }
        }
        catch (FinderException ex) {
            throw new GlobalConversionDateNotExistException();
        }
    }

    private void isConversionDateExists(ArInvoiceDetails invoiceDetails, String currencyName, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("ArInvoiceEntryControllerBean isConversionDateExists");
        try {
            if (invoiceDetails.getInvConversionDate() != null) {
                LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(currencyName, companyCode);
                glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), invoiceDetails.getInvConversionDate(), companyCode);
            }
        }
        catch (FinderException ex) {
            throw new GlobalConversionDateNotExistException();
        }
    }

    private void validateInvoice(LocalArInvoice arInvoice) throws GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException {

        Debug.print("ArInvoiceEntryControllerBean validateInvoice");
        if (arInvoice.getInvApprovalStatus() != null) {
            if (arInvoice.getInvApprovalStatus().equals(EJBCommon.APPROVED) || arInvoice.getInvApprovalStatus().equals(EJBCommon.NOT_APPLICABLE)) {
                throw new GlobalTransactionAlreadyApprovedException();
            } else if (arInvoice.getInvApprovalStatus().equals(EJBCommon.PENDING)) {
                throw new GlobalTransactionAlreadyPendingException();
            }
        }
        if (arInvoice.getInvPosted() == EJBCommon.TRUE) {
            throw new GlobalTransactionAlreadyPostedException();
        } else if (arInvoice.getInvVoid() == EJBCommon.TRUE) {
            throw new GlobalTransactionAlreadyVoidException();
        }
    }

    private double calculateIlNetAmount(ArModInvoiceLineDetails mdetails, double tcRate, String tcType, short precisionUnit) {

        Debug.print("ArInvoiceEntryControllerBean calculateIlNetAmount");
        double amount = 0d;
        if (tcType.equals("INCLUSIVE")) {
            amount = EJBCommon.roundIt(mdetails.getIlAmount() / (1 + (tcRate / 100)), precisionUnit);
        } else {
            // tax exclusive, none, zero rated or exempt
            amount = mdetails.getIlAmount();
        }
        return amount;
    }

    private double calculateIlTaxAmount(ArModInvoiceLineDetails mdetails, double tcRate, String tcType, double amount, short precisionUnit) {

        Debug.print("ArInvoiceEntryControllerBean calculateIlTaxAmount");
        double taxAmount = 0d;
        if (!tcType.equals("NONE") && !tcType.equals("EXEMPT")) {
            if (tcType.equals("INCLUSIVE")) {
                taxAmount = EJBCommon.roundIt(mdetails.getIlAmount() - amount, precisionUnit);
            } else if (tcType.equals("EXCLUSIVE")) {
                taxAmount = EJBCommon.roundIt(mdetails.getIlAmount() * tcRate / 100, precisionUnit);
            }
        }
        return taxAmount;
    }

    private double getInvFifoCost(Date costDate, Integer itemLineCode, double costQuantity, double cstCost, boolean isAdjustFifo, Integer branchCode, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getInvFifoCost");
        try {
            Collection invFifoCostings = invCostingHome.findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(costDate, itemLineCode, branchCode, companyCode);
            if (invFifoCostings.size() > 0) {
                Iterator x = invFifoCostings.iterator();
                if (isAdjustFifo) {
                    // executed during POST transaction
                    double totalCost = 0d;
                    double cost;
                    if (costQuantity < 0) {
                        // for negative quantities
                        double neededQty = -(costQuantity);
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

                        // if needed qty is not yet satisfied but no more quantities to fetch, get the default cost
                        if (neededQty != 0) {
                            LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(itemLineCode);
                            totalCost += (neededQty * invItemLocation.getInvItem().getIiUnitCost());
                        }
                        cost = totalCost / -costQuantity;
                    } else {
                        // for positive quantities
                        cost = cstCost;
                    }
                    return cost;
                } else {
                    // executed during ENTRY transaction
                    LocalInvCosting invFifoCosting = (LocalInvCosting) x.next();
                    if (invFifoCosting.getApPurchaseOrderLine() != null || invFifoCosting.getApVoucherLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived(), commonData.getGlFcPrecisionUnit(companyCode));
                    } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold(), commonData.getGlFcPrecisionUnit(companyCode));
                    } else {
                        return EJBCommon.roundIt(invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity(), commonData.getGlFcPrecisionUnit(companyCode));
                    }
                }
            } else {
                // most applicable in 1st entries of data
                LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(itemLineCode);
                return invItemLocation.getInvItem().getIiUnitCost();
            }
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double quantitySold, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean convertByUomFromAndItemAndQuantity");
        try {
            Debug.print("ArInvoiceEntryControllerBean convertByUomFromAndItemAndQuantity A");
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);
            Debug.print("ArInvoiceEntryControllerBean convertByUomFromAndItemAndQuantity B");
            return EJBCommon.roundIt(quantitySold * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double computeTotalBalance(Integer invoiceCode, String customerCode, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean computeTotalBalance");
        double customerBalance = 0;
        try {
            // get latest balance
            Collection arCustomerBalances = arCustomerBalanceHome.findByCstCustomerCode(customerCode, companyCode);
            if (!arCustomerBalances.isEmpty()) {
                ArrayList customerBalanceList = new ArrayList(arCustomerBalances);
                customerBalance = ((LocalArCustomerBalance) customerBalanceList.get(customerBalanceList.size() - 1)).getCbBalance();
            }
            // get amount of unposted invoices/credit memos
            Collection arInvoices = arInvoiceHome.findUnpostedInvByCstCustomerCode(customerCode, companyCode);
            for (Object arInvoice : arInvoices) {
                LocalArInvoice mdetails = (LocalArInvoice) arInvoice;
                if (!mdetails.getInvCode().equals(invoiceCode)) {
                    if (mdetails.getInvCreditMemo() == EJBCommon.TRUE) {
                        customerBalance = customerBalance - mdetails.getInvAmountDue();
                    } else {
                        customerBalance = customerBalance + (mdetails.getInvAmountDue() - mdetails.getInvAmountPaid());
                    }
                }
            }
            // get amount of unposted receipts
            Collection arReceipts = arReceiptHome.findUnpostedRctByCstCustomerCode(customerCode, companyCode);
            for (Object receipt : arReceipts) {
                LocalArReceipt arReceipt = (LocalArReceipt) receipt;
                customerBalance = customerBalance - arReceipt.getRctAmount();
            }
            // get amount of pdc (unposted or posted) type PR findPdcByPdcType("PR",companyCode)
            Collection arPdcs = arPdcHome.findPdcByPdcType(companyCode);
            for (Object pdc : arPdcs) {
                LocalArPdc arPdc = (LocalArPdc) pdc;
                customerBalance = customerBalance - arPdc.getPdcAmount();
            }
        }
        catch (FinderException ex) {
        }
        return customerBalance;
    }

    private double convertCostByUom(String itemName, String unitOfMeasure, double unitCost, boolean isFromDefault, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean convertCostByUom");
        try {
            LocalInvItem invItem = invItemHome.findByIiName(EJBCommon.itemName, companyCode);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(EJBCommon.itemName, unitOfMeasure, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(EJBCommon.itemName, invItem.getInvUnitOfMeasure().getUomName(), companyCode);
            if (isFromDefault) {
                return unitCost * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor();
            } else {
                return unitCost * invUnitOfMeasureConversion.getUmcConversionFactor() / invDefaultUomConversion.getUmcConversionFactor();
            }
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double adjustQuantity, Integer companyCode) {

        Debug.print("ArInvoicePostControllerBean convertByUomFromAndUomToAndQuantity");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);
            return EJBCommon.roundIt(adjustQuantity * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer currencyCode, String currencyName, Date conversionDate, double conversionRate, double amount, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean convertForeignToFunctionalCurrency");
        LocalAdCompany adCompany = null;
        // get company and extended precision
        try {
            adCompany = adCompanyHome.findByPrimaryKey(companyCode);
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        // Convert to functional currency if necessary
        if (conversionRate != 1 && conversionRate != 0) {
            amount = amount / conversionRate;
        }
        return EJBCommon.roundIt(amount, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private boolean isCriticalFieldsChanged(String paymentName, String taxCode, String withholdingTaxCode, String customerCode, ArrayList invoiceLines, LocalArInvoice arInvoice, boolean isRecalculate) {

        Debug.print("ArInvoiceEntryControllerBean isCriticalFieldsChanged");
        if (!arInvoice.getArTaxCode().getTcName().equals(taxCode) || !arInvoice.getArWithholdingTaxCode().getWtcName().equals(withholdingTaxCode) || !arInvoice.getArCustomer().getCstCustomerCode().equals(customerCode) || !arInvoice.getAdPaymentTerm().getPytName().equals(paymentName) || invoiceLines.size() != arInvoice.getArInvoiceLines().size()) {
            isRecalculate = true;
        } else if (invoiceLines.size() == arInvoice.getArInvoiceLines().size()) {
            Iterator ilIter = arInvoice.getArInvoiceLines().iterator();
            Iterator ilListIter = invoiceLines.iterator();
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
        return isRecalculate;
    }

    private Integer getArGlCoaRevenueAccount(LocalArInvoiceLine arInvoiceLine, Integer branchCode, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean getArGlCoaRevenueAccount");
        // generate revenue account
        try {
            StringBuilder chartOfAccount = new StringBuilder();
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGenField genField = adCompany.getGenField();
            String segmentSeparator = String.valueOf(genField.getFlSegmentSeparator());
            LocalAdBranchStandardMemoLine adBranchStandardMemoLine = null;
            try {
                adBranchStandardMemoLine = adBranchStandardMemoLineHome.findBSMLBySMLCodeAndBrCode(arInvoiceLine.getArStandardMemoLine().getSmlCode(), branchCode, companyCode);
            }
            catch (FinderException ex) {
            }

            Collection arAutoAccountingSegments = arAutoAccountingSegmentHome.findByAaAccountType(EJBCommon.REVENUE, companyCode);
            for (Object autoAccountingSegment : arAutoAccountingSegments) {
                LocalArAutoAccountingSegment arAutoAccountingSegment = (LocalArAutoAccountingSegment) autoAccountingSegment;
                LocalGlChartOfAccount glChartOfAccount = null;
                if (arAutoAccountingSegment.getAasClassType().equals(EJBCommon.AR_CUSTOMER)) {
                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arInvoiceLine.getArInvoice().getArCustomer().getCstGlCoaRevenueAccount());
                    StringTokenizer st = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), segmentSeparator);
                    int ctr = 0;
                    while (st.hasMoreTokens()) {
                        ++ctr;
                        if (ctr == arAutoAccountingSegment.getAasSegmentNumber()) {
                            chartOfAccount.append(segmentSeparator).append(st.nextToken());
                            break;
                        } else {
                            st.nextToken();
                        }
                    }
                } else if (arAutoAccountingSegment.getAasClassType().equals(EJBCommon.AR_STANDARD_MEMO_LINE)) {
                    if (adBranchStandardMemoLine != null) {
                        try {
                            glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlCoaRevenueAccount());
                        }
                        catch (FinderException ex) {
                        }
                    } else {
                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arInvoiceLine.getArStandardMemoLine().getSmlGlCoaReceivableAccount());
                    }
                    StringTokenizer st = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), segmentSeparator);
                    int ctr = 0;
                    while (st.hasMoreTokens()) {
                        ++ctr;
                        if (ctr == arAutoAccountingSegment.getAasSegmentNumber()) {
                            chartOfAccount.append(segmentSeparator).append(st.nextToken());
                            break;
                        } else {
                            st.nextToken();
                        }
                    }
                }
            }

            chartOfAccount = new StringBuilder(chartOfAccount.substring(1));
            try {
                LocalGlChartOfAccount glGeneratedChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(chartOfAccount.toString(), companyCode);
                return glGeneratedChartOfAccount.getCoaCode();
            }
            catch (FinderException ex) {
                if (adBranchStandardMemoLine != null) {
                    LocalGlChartOfAccount glChartOfAccount = null;
                    try {
                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlCoaRevenueAccount());
                    }
                    catch (FinderException e) {
                    }
                    return glChartOfAccount.getCoaCode();
                } else {
                    return arInvoiceLine.getArStandardMemoLine().getSmlGlCoaRevenueAccount();
                }
            }
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalArInvoiceLineItem addArIliEntry(ArModInvoiceLineItemDetails mdetails, LocalArInvoice arInvoice, LocalInvItemLocation invItemLocation, Integer companyCode) throws GlobalMiscInfoIsRequiredException {

        Debug.print("ArInvoiceEntryControllerBean addArIliEntry");
        try {
            short precisionUnit = commonData.getGlFcPrecisionUnit(companyCode);
            double invoiceLineItemAmount;
            double invoiceLineItemTaxAmount;
            LocalArTaxCode arTaxCode = arInvoice.getArTaxCode();
            // calculate net amount
            double amount = mdetails.getIliUnitPrice() * mdetails.getIliQuantity();
            invoiceLineItemAmount = EJBCommon.calculateNetAmount(amount, mdetails.getIliTax(), arTaxCode.getTcRate(), arTaxCode.getTcType(), precisionUnit);
            // calculate tax
            invoiceLineItemTaxAmount = EJBCommon.calculateTaxAmount(amount, mdetails.getIliTax(), arTaxCode.getTcRate(), arTaxCode.getTcType(), invoiceLineItemAmount, precisionUnit);

            LocalArInvoiceLineItem arInvoiceLineItem = arInvoiceLineItemHome.IliLine(mdetails.getIliLine()).IliQuantity(mdetails.getIliQuantity()).IliUnitPrice(mdetails.getIliUnitPrice()).IliAmount(invoiceLineItemAmount).IliTaxAmount(invoiceLineItemTaxAmount).IliDiscount1(mdetails.getIliDiscount1()).IliDiscount2(mdetails.getIliDiscount2()).IliDiscount3(mdetails.getIliDiscount3()).IliDiscount4(mdetails.getIliDiscount4()).IliTotalDiscount(mdetails.getIliTotalDiscount()).IliTax(mdetails.getIliTax()).IliAdCompany(companyCode).buildInvoiceLineItem();

            arInvoiceLineItem.setArInvoice(arInvoice);
            arInvoiceLineItem.setInvItemLocation(invItemLocation);

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getIliUomName(), companyCode);
            arInvoiceLineItem.setInvUnitOfMeasure(invUnitOfMeasure);

            if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() == 1) {
                this.createInvTagList(arInvoiceLineItem, mdetails.getiIliTagList(), companyCode);
                if (mdetails.getIliMisc() != null || !mdetails.getIliMisc().equals("")) {
                    int qty2Prpgt = 0;
                    String miscList2Prpgt = this.checkExpiryDates(mdetails.getIliMisc(), qty2Prpgt, "False");
                    if (miscList2Prpgt != "Error") {
                        arInvoiceLineItem.setIliMisc(mdetails.getIliMisc());
                    }
                }
            } else {
                arInvoiceLineItem.setIliMisc(mdetails.getIliMisc());
            }
            return arInvoiceLineItem;
        }
        catch (GlobalMiscInfoIsRequiredException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalArSalesOrderInvoiceLine addArSolEntry(ArModSalesOrderLineDetails mdetails, LocalArInvoice arInvoice, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean addArSolEntry");
        try {
            short precisionUnit = commonData.getGlFcPrecisionUnit(companyCode);
            double invoiceLineItemAmount;
            double invoiceLineItemTaxAmount = 0d;
            // calculate net amount
            LocalArTaxCode arTaxCode = arInvoice.getArTaxCode();
            invoiceLineItemAmount = getInvoiceLineItemAmount(mdetails.getSolAmount(), precisionUnit, arTaxCode);
            // calculate tax
            invoiceLineItemTaxAmount = getInvoiceLineItemTaxAmount(mdetails.getSolAmount(), precisionUnit, invoiceLineItemAmount, invoiceLineItemTaxAmount, arTaxCode);
            if (mdetails.getSolTax() == EJBCommon.FALSE) {
                invoiceLineItemAmount = mdetails.getSolAmount();
                invoiceLineItemTaxAmount = 0;
            }
            LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = arSalesOrderInvoiceLineHome.create(mdetails.getSolQuantityDelivered(), invoiceLineItemAmount, invoiceLineItemTaxAmount, mdetails.getSolDiscount1(), mdetails.getSolDiscount2(), mdetails.getSolDiscount3(), mdetails.getSolDiscount4(), mdetails.getSolTotalDiscount(), mdetails.getSolMisc(), mdetails.getSolTax(), companyCode);
            arInvoice.addArSalesOrderInvoiceLine(arSalesOrderInvoiceLine);
            LocalArSalesOrderLine arSalesOrderLine = arSalesOrderLineHome.findByPrimaryKey(mdetails.getSolCode());
            arSalesOrderLine.addArSalesOrderInvoiceLine(arSalesOrderInvoiceLine);
            ArrayList tagList = new ArrayList();
            if (arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiTraceMisc() == 1) {
                tagList = mdetails.getSolTagList();
                this.createInvTagList(arSalesOrderInvoiceLine, tagList, companyCode);
            }
            return arSalesOrderInvoiceLine;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getInvoiceLineItemTaxAmount(double itemAmount, short precisionUnit, double invoiceLineItemAmount, double invoiceLineItemTaxAmount, LocalArTaxCode arTaxCode) {

        Debug.print("ArInvoiceEntryControllerBean getInvoiceLineItemTaxAmount");
        if (!arTaxCode.getTcType().equals(EJBCommon.NONE) && !arTaxCode.getTcType().equals(EJBCommon.EXEMPT)) {
            if (arTaxCode.getTcType().equals(EJBCommon.INCLUSIVE)) {
                invoiceLineItemTaxAmount = EJBCommon.roundIt(itemAmount - invoiceLineItemAmount, precisionUnit);
            } else if (arTaxCode.getTcType().equals(EJBCommon.EXCLUSIVE)) {
                invoiceLineItemTaxAmount = EJBCommon.roundIt(itemAmount * arTaxCode.getTcRate() / 100, precisionUnit);
            }
        }
        return invoiceLineItemTaxAmount;
    }

    private double getInvoiceLineItemAmount(double itemAmount, short precisionUnit, LocalArTaxCode arTaxCode) {

        Debug.print("ArInvoiceEntryControllerBean getInvoiceLineItemTaxAmount");
        double invoiceLineItemAmount;
        if (arTaxCode.getTcType().equals(EJBCommon.INCLUSIVE)) {
            invoiceLineItemAmount = EJBCommon.roundIt(itemAmount / (1 + (arTaxCode.getTcRate() / 100)), precisionUnit);
        } else {
            // tax exclusive, none, zero rated or exempt
            invoiceLineItemAmount = itemAmount;
        }
        return invoiceLineItemAmount;
    }

    private LocalArJobOrderInvoiceLine addArJolEntry(ArModJobOrderLineDetails mdetails, LocalArInvoice arInvoice, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean addArJolEntry");
        try {
            short precisionUnit = commonData.getGlFcPrecisionUnit(companyCode);
            double invoiceLineItemAmount;
            double invoiceLineItemTaxAmount = 0d;
            // calculate net amount
            LocalArTaxCode arTaxCode = arInvoice.getArTaxCode();
            invoiceLineItemAmount = getInvoiceLineItemAmount(mdetails.getJolAmount(), precisionUnit, arTaxCode);
            // calculate tax
            invoiceLineItemTaxAmount = getInvoiceLineItemTaxAmount(mdetails.getJolAmount(), precisionUnit, invoiceLineItemAmount, invoiceLineItemTaxAmount, arTaxCode);
            if (mdetails.getJolTax() == EJBCommon.FALSE) {
                invoiceLineItemAmount = mdetails.getJolAmount();
                invoiceLineItemTaxAmount = 0;
            }
            LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = arJobOrderInvoiceLineHome.create(mdetails.getJolQuantityDelivered(), invoiceLineItemAmount, invoiceLineItemTaxAmount, mdetails.getJolDiscount1(), mdetails.getJolDiscount2(), mdetails.getJolDiscount3(), mdetails.getJolDiscount4(), mdetails.getJolTotalDiscount(), mdetails.getJolMisc(), mdetails.getJolTax(), companyCode);
            arJobOrderInvoiceLine.setArInvoice(arInvoice);
            LocalArJobOrderLine arJobOrderLine = arJobOrderLineHome.findByPrimaryKey(mdetails.getJolCode());
            arJobOrderInvoiceLine.setArJobOrderLine(arJobOrderLine);
            ArrayList tagList = new ArrayList();
            if (arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation().getInvItem().getIiTraceMisc() == 1) {
                tagList = mdetails.getJolTagList();
                this.createInvTagList(arJobOrderInvoiceLine, tagList, companyCode);
            }
            return arJobOrderInvoiceLine;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalArInvoiceLine addArIlEntry(ArModInvoiceLineDetails mdetails, LocalArInvoice arInvoice, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean addArIlEntry");
        try {
            short precisionUnit = commonData.getGlFcPrecisionUnit(companyCode);
            double invoiceLineAmount;
            double invoiceLineTaxAmount = 0d;
            if (mdetails.getIlTax() == EJBCommon.TRUE) {
                LocalArTaxCode arTaxCode = arInvoice.getArTaxCode();
                // calculate net amount
                invoiceLineAmount = this.calculateIlNetAmount(mdetails, arTaxCode.getTcRate(), arTaxCode.getTcType(), precisionUnit);
                // calculate tax
                invoiceLineTaxAmount = this.calculateIlTaxAmount(mdetails, arTaxCode.getTcRate(), arTaxCode.getTcType(), invoiceLineAmount, precisionUnit);
            } else {
                invoiceLineAmount = mdetails.getIlAmount();
            }
            LocalArInvoiceLine arInvoiceLine = arInvoiceLineHome.create(mdetails.getIlDescription(), mdetails.getIlQuantity(), mdetails.getIlUnitPrice(), invoiceLineAmount, invoiceLineTaxAmount, mdetails.getIlTax(), companyCode);
            arInvoiceLine.setArInvoice(arInvoice);
            LocalArStandardMemoLine arStandardMemoLine = arStandardMemoLineHome.findBySmlName(mdetails.getIlSmlName(), companyCode);
            arInvoiceLine.setArStandardMemoLine(arStandardMemoLine);
            return arInvoiceLine;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalArInvoice createInvoice(ArInvoiceDetails details, Integer branchCode, Integer companyCode) throws CreateException {

        Debug.print("ArInvoiceEntryControllerBean createInvoice");
        LocalArInvoice arInvoice;
        arInvoice = arInvoiceHome.
                InvType(details.getInvType())
                .InvCreditMemo(EJBCommon.FALSE)
                .InvDescription(details.getInvDescription())
                .InvDate(details.getInvDate())
                .InvNumber(details.getInvNumber())
                .InvReferenceNumber(details.getInvReferenceNumber())
                .InvUploadNumber(details.getInvUploadNumber())
                .InvConversionDate(details.getInvConversionDate())
                .InvConversionRate(details.getInvConversionRate())
                .InvMemo(details.getInvMemo())
                .InvPreviousReading(details.getInvPreviousReading())
                .InvPresentReading(details.getInvPresentReading())
                .InvBillToAddress(details.getInvBillToAddress())
                .InvBillToContact(details.getInvBillToContact())
                .InvBillToAltContact(details.getInvBillToAltContact())
                .InvBillToPhone(details.getInvBillToPhone())
                .InvBillingHeader(details.getInvBillingHeader())
                .InvBillingFooter(details.getInvBillingFooter())
                .InvBillingHeader2(details.getInvBillingHeader2())
                .InvBillingFooter2(details.getInvBillingFooter2())
                .InvBillingHeader3(details.getInvBillingHeader3())
                .InvBillingFooter3(details.getInvBillingFooter3())
                .InvBillingSignatory(details.getInvBillingSignatory())
                .InvSignatoryTitle(details.getInvSignatoryTitle())
                .InvShipToAddress(details.getInvShipToAddress())
                .InvShipToContact(details.getInvShipToContact())
                .InvShipToAltContact(details.getInvShipToAltContact())
                .InvShipToPhone(details.getInvShipToPhone())
                .InvShipDate(details.getInvShipDate())
                .InvLvFreight(details.getInvLvFreight())
                .InvDisableInterest(details.getInvDisableInterest())
                .InvInterest(details.getInvInterest())
                .InvInterestNextRunDate(details.getInvInterestNextRunDate())
                .InvInterestLastRunDate(details.getInvInterestLastRunDate())
                .InvCreatedBy(details.getInvCreatedBy())
                .InvDateCreated(details.getInvDateCreated())
                .InvLastModifiedBy(details.getInvLastModifiedBy())
                .InvDateLastModified(details.getInvDateLastModified())
                .InvDebitMemo(details.getInvDebitMemo())
                .InvSubjectToCommission(details.getInvSubjectToCommission())
                .InvEffectivityDate(details.getInvEffectivityDate())
                .InvAdBranch(branchCode)
                .InvAdCompany(companyCode)
                .buildInvoice(details.getCompanyShortName());
        return arInvoice;
    }

    private LocalInvAdjustmentLine addInvAlEntry(LocalInvItemLocation invItemLocation, LocalInvAdjustment invAdjustment, double costVarianceValue, byte isAdjustmentLineVoid, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean addInvAlEntry");
        try {
            // create dr entry
            LocalInvAdjustmentLine invAdjustmentLine = null;
            invAdjustmentLine = invAdjustmentLineHome.create(costVarianceValue, null, null, 0, 0, isAdjustmentLineVoid, companyCode);
            // map adjustment, unit of measure, item location
            invAdjustmentLine.setInvAdjustment(invAdjustment);
            invAdjustmentLine.setInvItemLocation(invItemLocation);
            invAdjustmentLine.setInvItemLocation(invItemLocation);
            return invAdjustmentLine;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private ArModInvoiceDetails getModInvoiceDetails(LocalArInvoice arInvoice) {

        Debug.print("ArInvoiceEntryControllerBean getModInvoiceDetails");
        ArModInvoiceDetails invoiceDetails = new ArModInvoiceDetails();
        invoiceDetails.setInvCode(arInvoice.getInvCode());
        invoiceDetails.setInvDocumentType(arInvoice.getInvDocumentType());
        invoiceDetails.setInvType(arInvoice.getInvType());
        invoiceDetails.setInvDescription(arInvoice.getInvDescription());
        invoiceDetails.setInvDate(arInvoice.getInvDate());
        invoiceDetails.setInvNumber(arInvoice.getInvNumber());
        invoiceDetails.setInvReferenceNumber(arInvoice.getInvReferenceNumber());
        invoiceDetails.setInvUploadNumber(arInvoice.getInvUploadNumber());
        invoiceDetails.setInvConversionDate(arInvoice.getInvConversionDate());
        invoiceDetails.setInvMemo(arInvoice.getInvMemo());
        invoiceDetails.setInvConversionRate(arInvoice.getInvConversionRate());
        invoiceDetails.setInvPreviousReading(arInvoice.getInvPreviousReading());
        invoiceDetails.setInvPresentReading(arInvoice.getInvPresentReading());
        invoiceDetails.setInvAmountDue(arInvoice.getInvAmountDue());
        invoiceDetails.setInvDownPayment(arInvoice.getInvDownPayment());
        invoiceDetails.setInvAmountPaid(arInvoice.getInvAmountPaid());
        invoiceDetails.setInvPenaltyDue(arInvoice.getInvPenaltyDue());
        invoiceDetails.setInvPenaltyPaid(arInvoice.getInvPenaltyPaid());
        invoiceDetails.setInvAmountUnearnedInterest(arInvoice.getInvAmountUnearnedInterest());
        invoiceDetails.setInvBillToAddress(arInvoice.getInvBillToAddress());
        invoiceDetails.setInvBillToContact(arInvoice.getInvBillToContact());
        invoiceDetails.setInvBillToAltContact(arInvoice.getInvBillToAltContact());
        invoiceDetails.setInvBillToPhone(arInvoice.getInvBillToPhone());
        invoiceDetails.setInvBillingHeader(arInvoice.getInvBillingHeader());
        invoiceDetails.setInvBillingFooter(arInvoice.getInvBillingFooter());
        invoiceDetails.setInvBillingHeader2(arInvoice.getInvBillingHeader2());
        invoiceDetails.setInvBillingFooter2(arInvoice.getInvBillingFooter2());
        invoiceDetails.setInvBillingHeader3(arInvoice.getInvBillingHeader3());
        invoiceDetails.setInvBillingFooter3(arInvoice.getInvBillingFooter3());
        invoiceDetails.setInvBillingSignatory(arInvoice.getInvBillingSignatory());
        invoiceDetails.setInvSignatoryTitle(arInvoice.getInvSignatoryTitle());
        invoiceDetails.setInvShipToAddress(arInvoice.getInvShipToAddress());
        invoiceDetails.setInvShipToContact(arInvoice.getInvShipToContact());
        invoiceDetails.setInvShipToAltContact(arInvoice.getInvShipToAltContact());
        invoiceDetails.setInvShipToPhone(arInvoice.getInvShipToPhone());
        invoiceDetails.setInvShipDate(arInvoice.getInvShipDate());
        invoiceDetails.setInvLvFreight(arInvoice.getInvLvFreight());
        invoiceDetails.setInvApprovalStatus(arInvoice.getInvApprovalStatus());
        invoiceDetails.setInvReasonForRejection(arInvoice.getInvReasonForRejection());
        invoiceDetails.setInvPosted(arInvoice.getInvPosted());
        invoiceDetails.setInvVoid(arInvoice.getInvVoid());
        invoiceDetails.setInvInterestNextRunDate(arInvoice.getInvInterestNextRunDate());
        invoiceDetails.setInvDisableInterest(arInvoice.getInvDisableInterest());
        invoiceDetails.setInvInterest(arInvoice.getInvInterest());
        invoiceDetails.setInvInterestReferenceNumber(arInvoice.getInvInterestReferenceNumber());
        invoiceDetails.setInvInterestAmount(arInvoice.getInvInterestAmount());
        invoiceDetails.setInvInterestCreatedBy(arInvoice.getInvInterestCreatedBy());
        invoiceDetails.setInvInterestDateCreated(arInvoice.getInvInterestDateCreated());
        invoiceDetails.setInvInterestNextRunDate(arInvoice.getInvInterestNextRunDate());
        invoiceDetails.setInvInterestLastRunDate(arInvoice.getInvInterestLastRunDate());
        invoiceDetails.setInvCreatedBy(arInvoice.getInvCreatedBy());
        invoiceDetails.setInvDateCreated(arInvoice.getInvDateCreated());
        invoiceDetails.setInvLastModifiedBy(arInvoice.getInvLastModifiedBy());
        invoiceDetails.setInvDateLastModified(arInvoice.getInvDateLastModified());
        invoiceDetails.setInvApprovedRejectedBy(arInvoice.getInvApprovedRejectedBy());
        invoiceDetails.setInvDateApprovedRejected(arInvoice.getInvDateApprovedRejected());
        invoiceDetails.setInvPostedBy(arInvoice.getInvPostedBy());
        invoiceDetails.setInvDatePosted(arInvoice.getInvDatePosted());
        invoiceDetails.setInvFcName(arInvoice.getGlFunctionalCurrency().getFcName());
        invoiceDetails.setInvTcName(arInvoice.getArTaxCode().getTcName());
        invoiceDetails.setInvWtcName(arInvoice.getArWithholdingTaxCode().getWtcName());
        invoiceDetails.setInvCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
        invoiceDetails.setInvPytName(arInvoice.getAdPaymentTerm().getPytName());
        invoiceDetails.setInvIbName(arInvoice.getArInvoiceBatch() != null ? arInvoice.getArInvoiceBatch().getIbName() : null);
        invoiceDetails.setInvLvShift(arInvoice.getInvLvShift());
        invoiceDetails.setInvSoNumber(arInvoice.getInvSoNumber());
        invoiceDetails.setInvJoNumber(arInvoice.getInvJoNumber());
        invoiceDetails.setInvDebitMemo(arInvoice.getInvDebitMemo());
        invoiceDetails.setInvSubjectToCommission(arInvoice.getInvSubjectToCommission());
        invoiceDetails.setInvCstName(arInvoice.getArCustomer().getCstName());
        invoiceDetails.setInvTcRate(arInvoice.getArTaxCode().getTcRate());
        invoiceDetails.setInvTcType(arInvoice.getArTaxCode().getTcType());
        invoiceDetails.setInvClientPO(arInvoice.getInvClientPO());
        invoiceDetails.setInvEffectivityDate(arInvoice.getInvEffectivityDate());
        invoiceDetails.setInvReceiveDate(arInvoice.getInvReceiveDate());
        invoiceDetails.setReportParameter(arInvoice.getReportParameter());
        return invoiceDetails;
    }

    private void addArDrIliEntry(short distributionRecordLine, String distributionRecordClass, byte isDebit, double distributionRecordAmount, Integer chartOfAccountCode, LocalArInvoice arInvoice, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArInvoiceEntryControllerBean addArDrIliEntry - Invoice");
        try {
            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(chartOfAccountCode, branchCode, companyCode);
            // create distribution record
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(distributionRecordLine, distributionRecordClass, isDebit, EJBCommon.roundIt(distributionRecordAmount, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
            arDistributionRecord.setArInvoice(arInvoice);
            arDistributionRecord.setGlChartOfAccount(glChartOfAccount);
        }
        catch (FinderException ex) {
            throw new GlobalBranchAccountNumberInvalidException();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrIliEntry(short distributionRecordLine, String distributionRecordClass, byte isDebit, double distributionRecordAmount, Integer chartOfAccountCode, LocalArReceipt arReceipt, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArInvoiceEntryControllerBean addArDrIliEntry - Receipt");
        try {
            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(chartOfAccountCode, branchCode, companyCode);
            // create distribution record
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(distributionRecordLine, distributionRecordClass, isDebit, EJBCommon.roundIt(distributionRecordAmount, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
            arDistributionRecord.setArReceipt(arReceipt);
            arDistributionRecord.setGlChartOfAccount(glChartOfAccount);
        }
        catch (FinderException ex) {
            throw new GlobalBranchAccountNumberInvalidException("" + distributionRecordLine);
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double journalLineAmount, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean postToGl");
        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode);
            String accountType = glChartOfAccount.getCoaAccountType();
            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();
            if (((accountType.equals(EJBCommon.ASSET) || accountType.equals(EJBCommon.EXPENSE)) && isDebit == EJBCommon.TRUE) || (!accountType.equals(EJBCommon.ASSET) && !accountType.equals(EJBCommon.EXPENSE) && isDebit == EJBCommon.FALSE)) {
                glChartOfAccountBalance.setCoabEndingBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() + journalLineAmount, FC_EXTNDD_PRCSN));
                if (!isCurrentAcv) {
                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() + journalLineAmount, FC_EXTNDD_PRCSN));
                }
            } else {
                glChartOfAccountBalance.setCoabEndingBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() - journalLineAmount, FC_EXTNDD_PRCSN));
                if (!isCurrentAcv) {
                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() - journalLineAmount, FC_EXTNDD_PRCSN));
                }
            }
            if (isCurrentAcv) {
                if (isDebit == EJBCommon.TRUE) {
                    glChartOfAccountBalance.setCoabTotalDebit(EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalDebit() + journalLineAmount, FC_EXTNDD_PRCSN));
                } else {
                    glChartOfAccountBalance.setCoabTotalCredit(EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalCredit() + journalLineAmount, FC_EXTNDD_PRCSN));
                }
            }
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInv(LocalArInvoiceLineItem arInvoiceLineItem, Date costDate, double costQuantitySold, double costOfSales, double remainingQuantity, double remainingValue, Integer branchCode, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("ArInvoiceEntryControllerBean postToInv");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();
            int costLineNumber = 0;
            costQuantitySold = EJBCommon.roundIt(costQuantitySold, adPreference.getPrfInvQuantityPrecisionUnit());
            costOfSales = EJBCommon.roundIt(costOfSales, adCompany.getGlFunctionalCurrency().getFcPrecision());
            remainingQuantity = EJBCommon.roundIt(remainingQuantity, adPreference.getPrfInvQuantityPrecisionUnit());
            remainingValue = EJBCommon.roundIt(remainingValue, adCompany.getGlFunctionalCurrency().getFcPrecision());
            if (costQuantitySold > 0) {
                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - costQuantitySold);
            }

            try {
                // generate line number
                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(costDate.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                costLineNumber = invCurrentCosting.getCstLineNumber() + 1;
            }
            catch (FinderException ex) {
                costLineNumber = 1;
            }

            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                // void subsequent cost variance adjustments
                Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(costDate, invItemLocation.getIlCode(), branchCode, companyCode);
                for (Object adjustmentLine : invAdjustmentLines) {
                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;
                    this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
                }
            }

            String prevExpiryDates;
            double remainingQty = 0;
            double qtyPrpgt2 = 0;
            try {
                LocalInvCosting prevCst = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(costDate, invItemLocation.getIlCode(), branchCode, companyCode);
                Debug.print("ArInvoicePostControllerBean getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode");
                prevExpiryDates = prevCst.getCstExpiryDate();
                remainingQty = prevCst.getCstRemainingQuantity();

                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

            }
            catch (Exception ex) {
                prevExpiryDates = "";
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(costDate, costDate.getTime(), costLineNumber, 0d, 0d, 0d, 0d, costQuantitySold, costOfSales, remainingQuantity, remainingValue, 0d, 0d, 0d, branchCode, companyCode);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setArInvoiceLineItem(arInvoiceLineItem);
            invCosting.setCstQuantity(costQuantitySold * -1);
            invCosting.setCstCost(costOfSales * -1);

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(costDate, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

            Iterator i = invCostings.iterator();
            String miscList = "";
            ArrayList miscList2 = null;

            String propagateMisc;
            StringBuilder ret = new StringBuilder();

            while (i.hasNext()) {
                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();
                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() - costQuantitySold);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() - costOfSales);

                if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                    if (arInvoiceLineItem.getIliMisc() != null && !arInvoiceLineItem.getIliMisc().equals("") && arInvoiceLineItem.getIliMisc().length() != 0) {
                        double qty = Double.parseDouble(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                        miscList = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), qty, "False");
                        miscList2 = this.expiryDates(arInvoiceLineItem.getIliMisc(), qty);
                        if (arInvoiceLineItem.getIliQuantity() < 0) {
                            Iterator mi = miscList2.iterator();
                            propagateMisc = invPropagatedCosting.getCstExpiryDate();
                            ret = new StringBuilder(invPropagatedCosting.getCstExpiryDate());
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();
                                int qTest = this.checkExpiryDates(ret + "fin$");
                                ArrayList miscList3 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));
                                Iterator m2 = miscList3.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();
                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }
                                    if (miscStr2.equals(miscStr)) {
                                        if (a == 0) {
                                            a = 1;
                                            ret2 = "1st";
                                        } else {
                                            a = a + 1;
                                            ret2 = "true";
                                        }
                                    }
                                    if (!miscStr2.equals(miscStr) || a > 1) {
                                        if ((ret2 != "1st") || (ret2 == "false") || (ret2 == "true")) {
                                            if (miscStr2 != "") {
                                                miscStr2 = "$" + miscStr2;
                                                ret.append(miscStr2);
                                                ret2 = "false";
                                            }
                                        }
                                    }
                                }
                                ret.append("$");
                                remainingQty = remainingQty - 1;
                            }
                        }
                    }

                    if (arInvoiceLineItem.getIliMisc() != null && !arInvoiceLineItem.getIliMisc().equals("") && arInvoiceLineItem.getIliMisc().length() != 0) {
                        if (costQuantitySold < 0) {
                            propagateMisc = miscList + invPropagatedCosting.getCstExpiryDate().substring(1);
                        } else {
                            Iterator mi = miscList2.iterator();
                            propagateMisc = prevExpiryDates;
                            ret = new StringBuilder(propagateMisc);
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();
                                if (remainingQty <= 0) {
                                    remainingQty = qtyPrpgt2;
                                }
                                int qTest = this.checkExpiryDates(ret + "fin$");
                                ArrayList miscList3 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));
                                Iterator m2 = miscList3.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();
                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }
                                    if (miscStr2.trim().equals(miscStr.trim())) {
                                        if (a == 0) {
                                            a = 1;
                                            ret2 = "1st";
                                        } else {
                                            a = a + 1;
                                            ret2 = "true";
                                        }
                                    }
                                    if (!miscStr2.trim().equals(miscStr.trim()) || a > 1) {
                                        if ((ret2 != "1st") || (ret2 == "false") || (ret2 == "true")) {
                                            if (miscStr2 != "") {
                                                miscStr2 = "$" + miscStr2.trim();
                                                ret.append(miscStr2);
                                                ret2 = "false";
                                            }
                                        }
                                    }
                                }
                                ret.append("$");
                                remainingQty = remainingQty - 1;
                            }
                            propagateMisc = ret.toString();
                        }
                        invPropagatedCosting.setCstExpiryDate(propagateMisc);
                    } else {
                        invPropagatedCosting.setCstExpiryDate(prevExpiryDates);
                    }
                }
            }
        }
        catch (AdPRFCoaGlVarianceAccountNotFoundException | GlobalExpiryDateNotFoundException ex) {
            throw ex;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInvSo(LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine, Date costDate, double costQuantitySold, double costOfSales, double costRemainingQuantity, double remainingValue, double costVarianceValue, String userName, Integer branchCode, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean postToInvSo");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalArSalesOrderLine arSalesOrderLine = arSalesOrderInvoiceLine.getArSalesOrderLine();
            LocalInvItemLocation invItemLocation = arSalesOrderLine.getInvItemLocation();
            int costLineNumber = 0;

            costQuantitySold = EJBCommon.roundIt(costQuantitySold, adPreference.getPrfInvQuantityPrecisionUnit());
            costOfSales = EJBCommon.roundIt(costOfSales, adCompany.getGlFunctionalCurrency().getFcPrecision());
            costRemainingQuantity = EJBCommon.roundIt(costRemainingQuantity, adPreference.getPrfInvQuantityPrecisionUnit());
            remainingValue = EJBCommon.roundIt(remainingValue, adCompany.getGlFunctionalCurrency().getFcPrecision());

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(costDate.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                costLineNumber = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                costLineNumber = 1;
            }

            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                // void subsequent cost variance adjustments
                Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(costDate, invItemLocation.getIlCode(), branchCode, companyCode);

                for (Object adjustmentLine : invAdjustmentLines) {

                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;
                    this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
                }
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(costDate, costDate.getTime(), costLineNumber, 0d, 0d, 0d, 0d, costQuantitySold, costOfSales, costRemainingQuantity, remainingValue, 0d, 0d, 0d, branchCode, companyCode);
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setArSalesOrderInvoiceLine(arSalesOrderInvoiceLine);

            invCosting.setCstQuantity(costQuantitySold * -1);
            invCosting.setCstCost(costOfSales * -1);

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(costDate, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
            for (Object costing : invCostings) {
                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;
                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + costQuantitySold);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + costOfSales);
            }
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInvJo(LocalArJobOrderInvoiceLine arJobOrderInvoiceLine, Date costDate, double costQuantitySold, double costOfSales, double costRemainingQuantity, double remainingValue, Integer branchCode, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean postToInvJo");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalArJobOrderLine arJobOrderLine = arJobOrderInvoiceLine.getArJobOrderLine();
            LocalInvItemLocation invItemLocation = arJobOrderLine.getInvItemLocation();
            int costLineNumber = 0;

            costQuantitySold = EJBCommon.roundIt(costQuantitySold, adPreference.getPrfInvQuantityPrecisionUnit());
            costOfSales = EJBCommon.roundIt(costOfSales, adCompany.getGlFunctionalCurrency().getFcPrecision());
            costRemainingQuantity = EJBCommon.roundIt(costRemainingQuantity, adPreference.getPrfInvQuantityPrecisionUnit());
            remainingValue = EJBCommon.roundIt(remainingValue, adCompany.getGlFunctionalCurrency().getFcPrecision());

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(costDate.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                costLineNumber = invCurrentCosting.getCstLineNumber() + 1;

            }
            catch (FinderException ex) {

                costLineNumber = 1;
            }

            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                // void subsequent cost variance adjustments
                Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(costDate, invItemLocation.getIlCode(), branchCode, companyCode);

                for (Object adjustmentLine : invAdjustmentLines) {

                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;
                    this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), branchCode, companyCode);
                }
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(costDate, costDate.getTime(), costLineNumber, 0d, 0d, 0d, 0d, costQuantitySold, costOfSales, costRemainingQuantity, remainingValue, 0d, 0d, 0d, branchCode, companyCode);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setArJobOrderInvoiceLine(arJobOrderInvoiceLine);
            invCosting.setCstQuantity(costQuantitySold * -1);
            invCosting.setCstCost(costOfSales * -1);

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(costDate, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);

            for (Object costing : invCostings) {
                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;
                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + costQuantitySold);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + costOfSales);
            }
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrEntry(short distributionRecordLine, String distributionRecordClass, byte isDebit, double distributionRecordAmount, Integer chartOfAccountCode, Integer serviceChargeCoa, LocalArInvoice arInvoice, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArInvoiceEntryControllerBean addArDrEntry");
        try {
            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(chartOfAccountCode, branchCode, companyCode);
            // create distribution record
            if (distributionRecordAmount < 0) {
                distributionRecordAmount = distributionRecordAmount * -1;
                if (isDebit == 0) {
                    isDebit = 1;
                } else if (isDebit == 1) {
                    isDebit = 0;
                }
            }
            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(distributionRecordLine, distributionRecordClass, isDebit, EJBCommon.roundIt(distributionRecordAmount, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
            arDistributionRecord.setArInvoice(arInvoice);
            arDistributionRecord.setGlChartOfAccount(glChartOfAccount);
            if (distributionRecordClass.equals("SC")) {
                if (serviceChargeCoa == null) {
                    throw new GlobalBranchAccountNumberInvalidException();
                } else {
                    arDistributionRecord.setDrScAccount(serviceChargeCoa);
                }
            }
        }
        catch (FinderException ex) {
            throw new GlobalBranchAccountNumberInvalidException();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void post(Date INV_DT, double INV_AMNT, LocalArCustomer arCustomer, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean post");
        try {
            // find customer balance before or equal invoice date
            Collection arCustomerBalances = arCustomerBalanceHome.findByBeforeOrEqualInvDateAndCstCustomerCode(INV_DT, arCustomer.getCstCustomerCode(), companyCode);
            if (!arCustomerBalances.isEmpty()) {
                // get last invoice
                ArrayList arCustomerBalanceList = new ArrayList(arCustomerBalances);
                LocalArCustomerBalance arCustomerBalance = (LocalArCustomerBalance) arCustomerBalanceList.get(arCustomerBalanceList.size() - 1);
                if (arCustomerBalance.getCbDate().before(INV_DT)) {
                    // create new balance
                    LocalArCustomerBalance apNewCustomerBalance = arCustomerBalanceHome.create(INV_DT, arCustomerBalance.getCbBalance() + INV_AMNT, companyCode);
                    apNewCustomerBalance.setArCustomer(arCustomer);
                } else {
                    arCustomerBalance.setCbBalance(arCustomerBalance.getCbBalance() + INV_AMNT);
                }
            } else {
                // create new balance
                LocalArCustomerBalance apNewCustomerBalance = arCustomerBalanceHome.create(INV_DT, INV_AMNT, companyCode);
                apNewCustomerBalance.setArCustomer(arCustomer);
            }
            // propagate to subsequent balances if necessary
            arCustomerBalances = arCustomerBalanceHome.findByAfterInvDateAndCstCustomerCode(INV_DT, arCustomer.getCstCustomerCode(), companyCode);
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

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine, Date costDate, double cstAdjustQuantity, double cstAdjustCosting, double costRemainingQuantity, double remainingValue, Integer branchCode, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean postInvAdjustmentToInventory");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = invAdjustmentLine.getInvItemLocation();
            int costLineNumber;
            cstAdjustQuantity = EJBCommon.roundIt(cstAdjustQuantity, adPreference.getPrfInvQuantityPrecisionUnit());
            cstAdjustCosting = EJBCommon.roundIt(cstAdjustCosting, adCompany.getGlFunctionalCurrency().getFcPrecision());
            costRemainingQuantity = EJBCommon.roundIt(costRemainingQuantity, adPreference.getPrfInvQuantityPrecisionUnit());
            remainingValue = EJBCommon.roundIt(remainingValue, adCompany.getGlFunctionalCurrency().getFcPrecision());
            if (cstAdjustQuantity < 0) {
                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(cstAdjustQuantity));
            }
            // create costing
            try {
                // generate line number
                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(costDate.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
                costLineNumber = invCurrentCosting.getCstLineNumber() + 1;
            }
            catch (FinderException ex) {
                costLineNumber = 1;
            }

            LocalInvCosting invCosting = invCostingHome.create(costDate, costDate.getTime(), costLineNumber, 0d, 0d, cstAdjustQuantity, cstAdjustCosting, 0d, 0d, costRemainingQuantity, remainingValue, 0d, 0d, cstAdjustQuantity > 0 ? cstAdjustQuantity : 0, branchCode, companyCode);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);
            invCosting.setCstQuantity(cstAdjustQuantity);
            invCosting.setCstCost(cstAdjustCosting);
            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(costDate, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), branchCode, companyCode);
            for (Object costing : invCostings) {
                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;
                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + cstAdjustQuantity);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + cstAdjustCosting);
            }
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void createInvTagList(LocalArInvoiceLineItem arInvoiceLineItem, ArrayList<InvModTagListDetails> list, Integer companyCode) throws Exception {

        Debug.print("ArInvoiceEntryControllerBean createInvTagList");
        try {
            Iterator t = list.iterator();
            LocalInvTag invTag;
            while (t.hasNext()) {
                InvModTagListDetails tgLstDetails = (InvModTagListDetails) t.next();
                if (tgLstDetails.getTgCode() == null) {
                    invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), companyCode, tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());
                    invTag.setArInvoiceLineItem(arInvoiceLineItem);
                    invTag.setInvItemLocation(arInvoiceLineItem.getInvItemLocation());
                    LocalAdUser adUser = null;
                    try {
                        adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), companyCode);
                    }
                    catch (FinderException ex) {
                    }
                    invTag.setAdUser(adUser);
                }
            }
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    private void executeArInvPost(Integer invoiceCode, String userName, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("ArInvoiceEntryControllerBean executeApInvPost");
        LocalArInvoice arInvoice;
        LocalArInvoice arCreditedInvoice;
        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            // validate if invoice/credit memo is already deleted
            try {
                arInvoice = arInvoiceHome.findByPrimaryKey(invoiceCode);
                arCreditedInvoice = arInvoiceHome.findByPrimaryKey(invoiceCode);
            }
            catch (FinderException ex) {
                throw new GlobalRecordAlreadyDeletedException();
            }
            // validate if invoice/credit memo is already posted or void
            if (arInvoice.getInvPosted() == EJBCommon.TRUE) {
                throw new GlobalTransactionAlreadyPostedException();
            } else if (arInvoice.getInvVoid() == EJBCommon.TRUE) {
                throw new GlobalTransactionAlreadyVoidException();
            }
            // post invoice/credit memo
            if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {
                // increase customer balance
                double invoiceAmount = arInvoice.getInvAmountDue();
                this.post(arInvoice.getInvDate(), invoiceAmount, arInvoice.getArCustomer(), companyCode);
                Collection arInvoiceLineItems = arInvoiceLineItemHome.findByInvCode(invoiceCode, companyCode);
                Collection arSalesOrderInvoiceLines = arInvoice.getArSalesOrderInvoiceLines();
                Collection arJobOrderInvoiceLines = arInvoice.getArJobOrderInvoiceLines();
                if (arInvoiceLineItems != null && arInvoiceLineItems.size() > 0) {
                    for (Object invoiceLineItem : arInvoiceLineItems) {
                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;
                        String itemName = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                        String locationName = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();
                        double quantitySold = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                        LocalInvCosting invCosting = null;
                        try {
                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), itemName, locationName, branchCode, companyCode);
                        }
                        catch (FinderException ex) {
                        }

                        double unitCost = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                        if (invCosting == null) {
                            this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), quantitySold, unitCost * quantitySold, -quantitySold, -unitCost * quantitySold, branchCode, companyCode);
                        } else {
                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case EJBCommon.Average:
                                    double avgCost = invCosting.getCstRemainingQuantity() <= 0 ? unitCost : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                                    this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), quantitySold, avgCost * quantitySold, invCosting.getCstRemainingQuantity() - quantitySold, invCosting.getCstRemainingValue() - (quantitySold * avgCost), branchCode, companyCode);
                                    break;
                                case EJBCommon.FIFO:
                                    double fifoCost = invCosting.getCstRemainingQuantity() == 0 ? unitCost : this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), quantitySold, arInvoiceLineItem.getIliUnitPrice(), true, branchCode, companyCode);
                                    this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), quantitySold, fifoCost * quantitySold, invCosting.getCstRemainingQuantity() - quantitySold, invCosting.getCstRemainingValue() - (fifoCost * quantitySold), branchCode, companyCode);
                                    break;
                                case EJBCommon.Standard:
                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                                    this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), quantitySold, standardCost * quantitySold, invCosting.getCstRemainingQuantity() - quantitySold, invCosting.getCstRemainingValue() - (standardCost * quantitySold), branchCode, companyCode);
                                    break;
                            }
                        }
                    }
                } else if (arSalesOrderInvoiceLines != null && !arSalesOrderInvoiceLines.isEmpty()) {

                    for (Object salesOrderInvoiceLine : arSalesOrderInvoiceLines) {

                        LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) salesOrderInvoiceLine;
                        LocalArSalesOrderLine arSalesOrderLine = arSalesOrderInvoiceLine.getArSalesOrderLine();

                        String itemName = arSalesOrderLine.getInvItemLocation().getInvItem().getIiName();
                        String locationName = arSalesOrderLine.getInvItemLocation().getInvLocation().getLocName();
                        double quantitySold = this.convertByUomFromAndItemAndQuantity(arSalesOrderLine.getInvUnitOfMeasure(), arSalesOrderLine.getInvItemLocation().getInvItem(), arSalesOrderInvoiceLine.getSilQuantityDelivered(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), itemName, locationName, branchCode, companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        double unitCost = arSalesOrderLine.getInvItemLocation().getInvItem().getIiUnitCost();

                        if (invCosting == null) {

                            this.postToInvSo(arSalesOrderInvoiceLine, arInvoice.getInvDate(), quantitySold, unitCost * quantitySold, -quantitySold, -(quantitySold * unitCost), 0d, null, branchCode, companyCode);

                        } else {

                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case EJBCommon.Average:

                                    double avgCost = invCosting.getCstRemainingQuantity() <= 0 ? unitCost : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                    this.postToInvSo(arSalesOrderInvoiceLine, arInvoice.getInvDate(), quantitySold, avgCost * quantitySold, invCosting.getCstRemainingQuantity() - quantitySold, invCosting.getCstRemainingValue() - (quantitySold * avgCost), 0d, null, branchCode, companyCode);

                                    break;
                                case EJBCommon.FIFO:

                                    double fifoCost = this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), quantitySold, arSalesOrderInvoiceLine.getArSalesOrderLine().getSolUnitPrice(), true, branchCode, companyCode);

                                    this.postToInvSo(arSalesOrderInvoiceLine, arInvoice.getInvDate(), quantitySold, fifoCost * quantitySold, invCosting.getCstRemainingQuantity() - quantitySold, invCosting.getCstRemainingValue() - (fifoCost * quantitySold), 0d, null, branchCode, companyCode);

                                    break;
                                case EJBCommon.Standard:

                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                    this.postToInvSo(arSalesOrderInvoiceLine, arInvoice.getInvDate(), quantitySold, standardCost * quantitySold, invCosting.getCstRemainingQuantity() - quantitySold, invCosting.getCstRemainingValue() - (standardCost * quantitySold), 0d, null, branchCode, companyCode);
                                    break;
                            }
                        }
                    }
                } else if (arJobOrderInvoiceLines != null && !arJobOrderInvoiceLines.isEmpty()) {

                    for (Object jobOrderInvoiceLine : arJobOrderInvoiceLines) {

                        LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) jobOrderInvoiceLine;
                        LocalArJobOrderLine arJobOrderLine = arJobOrderInvoiceLine.getArJobOrderLine();

                        String itemName = arJobOrderLine.getInvItemLocation().getInvItem().getIiName();
                        String locationName = arJobOrderLine.getInvItemLocation().getInvLocation().getLocName();
                        double quantitySold = this.convertByUomFromAndItemAndQuantity(arJobOrderLine.getInvUnitOfMeasure(), arJobOrderLine.getInvItemLocation().getInvItem(), arJobOrderInvoiceLine.getJilQuantityDelivered(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {
                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), itemName, locationName, branchCode, companyCode);
                        }
                        catch (FinderException ex) {
                        }

                        double unitCost = arJobOrderLine.getInvItemLocation().getInvItem().getIiUnitCost();
                        if (invCosting == null) {
                            this.postToInvJo(arJobOrderInvoiceLine, arInvoice.getInvDate(), quantitySold, unitCost * quantitySold, -quantitySold, -(quantitySold * unitCost), branchCode, companyCode);
                        } else {
                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case EJBCommon.Average:
                                    double avgCost = invCosting.getCstRemainingQuantity() <= 0 ? unitCost : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                                    this.postToInvJo(arJobOrderInvoiceLine, arInvoice.getInvDate(), quantitySold, avgCost * quantitySold, invCosting.getCstRemainingQuantity() - quantitySold, invCosting.getCstRemainingValue() - (quantitySold * avgCost), branchCode, companyCode);
                                    break;
                                case EJBCommon.FIFO:
                                    double fifoCost = this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), quantitySold, arJobOrderInvoiceLine.getArJobOrderLine().getJolUnitPrice(), true, branchCode, companyCode);
                                    this.postToInvJo(arJobOrderInvoiceLine, arInvoice.getInvDate(), quantitySold, fifoCost * quantitySold, invCosting.getCstRemainingQuantity() - quantitySold, invCosting.getCstRemainingValue() - (fifoCost * quantitySold), branchCode, companyCode);
                                    break;
                                case EJBCommon.Standard:
                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                                    this.postToInvJo(arJobOrderInvoiceLine, arInvoice.getInvDate(), quantitySold, standardCost * quantitySold, invCosting.getCstRemainingQuantity() - quantitySold, invCosting.getCstRemainingValue() - (standardCost * quantitySold), branchCode, companyCode);
                                    break;
                            }
                        }
                    }
                }
            }

            // set invoice post status
            arInvoice.setInvPosted(EJBCommon.TRUE);
            arInvoice.setInvPostedBy(userName);
            arInvoice.setInvDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary
            if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {
                // validate if date has no period and period is closed
                LocalGlSetOfBook glJournalSetOfBook = null;
                try {
                    glJournalSetOfBook = glSetOfBookHome.findByDate(arInvoice.getInvDate(), companyCode);
                }
                catch (FinderException ex) {
                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), arInvoice.getInvDate(), companyCode);
                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {
                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting
                LocalGlJournalLine glOffsetJournalLine = null;
                Collection arDistributionRecords = arDistributionRecordHome.findImportableDrByInvCode(arInvoice.getInvCode(), companyCode);
                Iterator j = arDistributionRecords.iterator();

                double totalDebit = 0d;
                double totalCredit = 0d;

                while (j.hasNext()) {
                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();
                    double distributionRecordAmount = 0d;
                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {
                        distributionRecordAmount = arDistributionRecord.getDrAmount();
                    } else {
                        distributionRecordAmount = arDistributionRecord.getDrAmount();
                    }

                    if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                        totalDebit += distributionRecordAmount;

                    } else {

                        totalCredit += distributionRecordAmount;
                    }
                }

                totalDebit = EJBCommon.roundIt(totalDebit, adCompany.getGlFunctionalCurrency().getFcPrecision());
                totalCredit = EJBCommon.roundIt(totalCredit, adCompany.getGlFunctionalCurrency().getFcPrecision());

                if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.TRUE && totalDebit != totalCredit) {

                    LocalGlSuspenseAccount glSuspenseAccount = null;

                    try {

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS RECEIVABLES", arInvoice.getInvCreditMemo() == EJBCommon.FALSE ? "SALES INVOICES" : "CREDIT MEMOS", companyCode);

                    }
                    catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (totalDebit - totalCredit < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.TRUE, totalCredit - totalDebit, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.FALSE, totalDebit - totalCredit, "", companyCode);
                    }

                    LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                    // glChartOfAccount.addGlJournalLine(glOffsetJournalLine);
                    glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

                } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && totalDebit != totalCredit) {

                    throw new GlobalJournalNotBalanceException();
                }

                // create journal batch if necessary
                LocalGlJournalBatch glJournalBatch = createJournalBatch(userName, branchCode, companyCode, arInvoice, adPreference);

                // create journal entry
                LocalGlJournal glJournal = glJournalHome.create(arInvoice.getInvReferenceNumber(), arInvoice.getInvDescription(), arInvoice.getInvDate(), 0.0d, null, arInvoice.getInvNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, userName, new Date(), userName, new Date(), null, null, userName, EJBCommon.getGcCurrentDateWoTime().getTime(), arInvoice.getArCustomer().getCstTin(), arInvoice.getArCustomer().getCstName(), EJBCommon.FALSE, null, branchCode, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS RECEIVABLES", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName(arInvoice.getInvCreditMemo() == EJBCommon.FALSE ? "SALES INVOICES" : "CREDIT MEMOS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = arDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    double distributionRecordAmount = 0d;

                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                        distributionRecordAmount = arDistributionRecord.getDrAmount();

                    } else {

                        distributionRecordAmount = arDistributionRecord.getDrAmount();
                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(arDistributionRecord.getDrLine(), arDistributionRecord.getDrDebit(), distributionRecordAmount, "", companyCode);

                    glJournalLine.setGlChartOfAccount(arDistributionRecord.getGlChartOfAccount());

                    glJournalLine.setGlJournal(glJournal);
                    arDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation

                    LocalArInvoice arInvoiceTemp = arInvoice.getInvCreditMemo() == EJBCommon.FALSE ? arInvoice : arCreditedInvoice;

                    if ((!Objects.equals(arInvoiceTemp.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(arInvoiceTemp.getGlFunctionalCurrency().getFcCode()))) {

                        double conversionRate = 1;

                        if (arInvoiceTemp.getInvConversionRate() != 0 && arInvoiceTemp.getInvConversionRate() != 1) {

                            conversionRate = arInvoiceTemp.getInvConversionRate();

                        } else if (arInvoiceTemp.getInvConversionDate() != null) {

                            conversionRate = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);
                        }

                        Collection glForexLedgers = null;

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(arInvoiceTemp.getInvDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        }
                        catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(arInvoiceTemp.getInvDate()) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                        // compute balance
                        double chartOfAccountsForexBalance = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double forexLedgerAmount = arDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase(EJBCommon.ASSET)) {
                            forexLedgerAmount = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? forexLedgerAmount : (-1 * forexLedgerAmount));
                        } else {
                            forexLedgerAmount = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * forexLedgerAmount) : forexLedgerAmount);
                        }

                        chartOfAccountsForexBalance = chartOfAccountsForexBalance + forexLedgerAmount;

                        glForexLedger = glForexLedgerHome.create(arInvoiceTemp.getInvDate(), FRL_LN, "SI", forexLedgerAmount, conversionRate, chartOfAccountsForexBalance, 0d, companyCode);

                        // glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);
                        glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                        }
                        catch (FinderException ex) {

                        }

                        for (Object forexLedger : glForexLedgers) {

                            glForexLedger = (LocalGlForexLedger) forexLedger;
                            forexLedgerAmount = arDistributionRecord.getDrAmount();

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                                forexLedgerAmount = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? forexLedgerAmount : (-1 * forexLedgerAmount));
                            } else {
                                forexLedgerAmount = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * forexLedgerAmount) : forexLedgerAmount);
                            }

                            glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + forexLedgerAmount);
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

                            String accountType = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                            // post to subsequent acvs of subsequent set of book(propagate)

                            Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                            for (Object accountingCalendarValue : glAccountingCalendarValues) {

                                LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                                if (accountType.equals("ASSET") || accountType.equals("LIABILITY") || accountType.equals("OWNERS EQUITY")) {

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

        }
        catch (GlJREffectiveDateNoPeriodExistException | GlobalExpiryDateNotFoundException |
               AdPRFCoaGlVarianceAccountNotFoundException | GlobalTransactionAlreadyVoidException |
               GlobalTransactionAlreadyPostedException | GlobalRecordAlreadyDeletedException |
               GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } /*
         * } catch (GlobalBranchAccountNumberInvalidException ex) {
         *
         * getSessionContext().setRollbackOnly(); throw ex;
         */
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalGlJournalBatch createJournalBatch(String userName, Integer branchCode, Integer companyCode, LocalArInvoice arInvoice, LocalAdPreference adPreference) throws CreateException {

        LocalGlJournalBatch glJournalBatch = null;
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");
        String invoiceBatchName = (adPreference.getPrfEnableArInvoiceBatch() == EJBCommon.TRUE) ? arInvoice.getArInvoiceBatch().getIbName() : EJBCommon.SALES_INVOICES;
        String journalBatchName = String.format("JOURNAL IMPORT %s %s", formatter.format(new Date()), invoiceBatchName);
        try {
            glJournalBatch = glJournalBatchHome.findByJbName(journalBatchName, branchCode, companyCode);
        }
        catch (FinderException ex) {
        }
        if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {
            glJournalBatch = glJournalBatchHome.create(journalBatchName, EJBCommon.JOURNAL_IMPORT, EJBCommon.CLOSED, EJBCommon.getGcCurrentDateWoTime().getTime(), userName, branchCode, companyCode);
        }
        return glJournalBatch;
    }

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean voidInvAdjustment");
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

                this.addInvAlEntry(invAdjustmentLine.getInvItemLocation(), invAdjustment, (invAdjustmentLine.getAlUnitCost()) * -1, EJBCommon.TRUE, companyCode);
            }

            invAdjustment.setAdjVoid(EJBCommon.TRUE);

            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), branchCode, companyCode);

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addInvDrEntry(short distributionRecordLine, String distributionRecordClass, byte isDebit, double distributionRecordAmount, byte isDistributionRecordReversal, Integer chartOfAccountCode, LocalInvAdjustment invAdjustment, Integer branchCode, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArInvoiceEntryControllerBean addInvDrEntry");
        try {
            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            // validate coa
            LocalGlChartOfAccount glChartOfAccount;
            try {
                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(chartOfAccountCode, branchCode, companyCode);
            }
            catch (FinderException ex) {
                throw new GlobalBranchAccountNumberInvalidException();
            }
            // create distribution record
            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(distributionRecordLine, distributionRecordClass, isDebit, EJBCommon.roundIt(distributionRecordAmount, adCompany.getGlFunctionalCurrency().getFcPrecision()), isDistributionRecordReversal, EJBCommon.FALSE, companyCode);
            invDistributionRecord.setInvAdjustment(invAdjustment);
            invDistributionRecord.setInvChartOfAccount(glChartOfAccount);
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

    private void executeInvAdjPost(Integer adjustmentCode, String userName, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("ArInvoiceEntryControllerBean executeInvAdjPost");
        try {
            // validate if adjustment is already deleted
            LocalInvAdjustment invAdjustment;
            try {
                invAdjustment = invAdjustmentHome.findByPrimaryKey(adjustmentCode);
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

            Collection invAdjustmentLines;
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
            LocalGlSetOfBook glJournalSetOfBook;
            try {
                glJournalSetOfBook = glSetOfBookHome.findByDate(invAdjustment.getAdjDate(), companyCode);
            }
            catch (FinderException ex) {
                throw new GlJREffectiveDateNoPeriodExistException();
            }

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), invAdjustment.getAdjDate(), companyCode);
            if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {
                throw new GlJREffectiveDatePeriodClosedException();
            }

            // check if invoice is balance if not check suspense posting
            LocalGlJournalLine glOffsetJournalLine = null;
            Collection invDistributionRecords;
            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);
            } else {
                invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
            }

            Iterator j = invDistributionRecords.iterator();
            double totalDebit = 0d;
            double totalCredit = 0d;
            while (j.hasNext()) {
                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) j.next();
                double distributionRecordAmount = invDistributionRecord.getDrAmount();
                if (invDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    totalDebit += distributionRecordAmount;
                } else {
                    totalCredit += distributionRecordAmount;
                }
            }
            totalDebit = EJBCommon.roundIt(totalDebit, adCompany.getGlFunctionalCurrency().getFcPrecision());
            totalCredit = EJBCommon.roundIt(totalCredit, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.TRUE && totalDebit != totalCredit) {
                LocalGlSuspenseAccount glSuspenseAccount;
                try {
                    glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName(EJBCommon.INVENTORY, "INVENTORY ADJUSTMENTS", companyCode);
                }
                catch (FinderException ex) {
                    throw new GlobalJournalNotBalanceException();
                }
                if (totalDebit - totalCredit < 0) {
                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.TRUE, totalCredit - totalDebit, "", companyCode);
                } else {
                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.FALSE, totalDebit - totalCredit, "", companyCode);
                }
                LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);
            } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && totalDebit != totalCredit) {
                throw new GlobalJournalNotBalanceException();
            }

            // create journal batch if necessary
            LocalGlJournalBatch glJournalBatch = null;
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");
            try {
                glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", branchCode, companyCode);
            }
            catch (FinderException ex) {
            }

            if (glJournalBatch == null) {
                glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), userName, branchCode, companyCode);
            }

            // create journal entry
            LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(), invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d, null, invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, userName, new Date(), userName, new Date(), null, null, userName, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode, companyCode);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName(EJBCommon.INVENTORY, companyCode);
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
                double distributionRecordAmount = invDistributionRecord.getDrAmount();
                LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(), invDistributionRecord.getDrDebit(), distributionRecordAmount, "", companyCode);

                glJournalLine.setGlChartOfAccount(invDistributionRecord.getInvChartOfAccount());
                glJournalLine.setGlJournal(glJournal);
                invDistributionRecord.setDrImported(EJBCommon.TRUE);
            }

            if (glOffsetJournalLine != null) {
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
                        String accountType = glJournalLine.getGlChartOfAccount().getCoaAccountType();
                        // post to subsequent acvs of subsequent set of book(propagate)
                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);
                        for (Object accountingCalendarValue : glAccountingCalendarValues) {
                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;
                            if (accountType.equals("ASSET") || accountType.equals("LIABILITY") || accountType.equals("OWNERS EQUITY")) {
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

    private void updateInvoice(ArInvoiceDetails details, LocalArInvoice arInvoice) {

        Debug.print("ArInvoiceEntryControllerBean updateInvoice");
        arInvoice.setInvType(details.getInvType());
        arInvoice.setInvDocumentType(details.getInvDocumentType());
        arInvoice.setInvDescription(details.getInvDescription());
        arInvoice.setInvMemo(details.getInvMemo());
        arInvoice.setInvDate(details.getInvDate());
        arInvoice.setInvNumber(details.getInvNumber());
        arInvoice.setInvReferenceNumber(details.getInvReferenceNumber());
        arInvoice.setInvUploadNumber(details.getInvUploadNumber());
        arInvoice.setInvConversionDate(details.getInvConversionDate());
        arInvoice.setInvConversionRate(details.getInvConversionRate());
        arInvoice.setInvPreviousReading(arInvoice.getInvPreviousReading());
        arInvoice.setInvPresentReading(arInvoice.getInvPresentReading());
        arInvoice.setInvBillToAddress(details.getInvBillToAddress());
        arInvoice.setInvBillToContact(details.getInvBillToContact());
        arInvoice.setInvBillToAltContact(details.getInvBillToAltContact());
        arInvoice.setInvBillToPhone(details.getInvBillToPhone());
        arInvoice.setInvBillingHeader(details.getInvBillingHeader());
        arInvoice.setInvBillingFooter(details.getInvBillingFooter());
        arInvoice.setInvBillingHeader2(details.getInvBillingHeader2());
        arInvoice.setInvBillingFooter2(details.getInvBillingFooter2());
        arInvoice.setInvBillingHeader3(details.getInvBillingHeader3());
        arInvoice.setInvBillingFooter3(details.getInvBillingFooter3());
        arInvoice.setInvBillingSignatory(details.getInvBillingSignatory());
        arInvoice.setInvSignatoryTitle(details.getInvSignatoryTitle());
        arInvoice.setInvShipToAddress(details.getInvShipToAddress());
        arInvoice.setInvShipToContact(details.getInvShipToContact());
        arInvoice.setInvShipToAltContact(details.getInvShipToAltContact());
        arInvoice.setInvShipToPhone(details.getInvShipToPhone());
        arInvoice.setInvShipDate(details.getInvShipDate());
        arInvoice.setInvLvFreight(details.getInvLvFreight());
        arInvoice.setInvLastModifiedBy(details.getInvLastModifiedBy());
        arInvoice.setInvDateLastModified(details.getInvDateLastModified());
        arInvoice.setInvReasonForRejection(null);
        arInvoice.setInvDebitMemo(details.getInvDebitMemo());
        arInvoice.setInvSubjectToCommission(details.getInvSubjectToCommission());
        arInvoice.setInvEffectivityDate(details.getInvEffectivityDate());
        arInvoice.setInvReceiveDate(details.getInvReceiveDate());
        arInvoice.setInvDisableInterest(details.getInvDisableInterest());
        arInvoice.setInvInterest(details.getInvInterest());
        arInvoice.setInvInterestNextRunDate(details.getInvInterestNextRunDate());
        arInvoice.setInvInterestLastRunDate(details.getInvInterestLastRunDate());
    }

    private void tagSalesPerson(String salesPersonCode, Integer companyCode, LocalArInvoice arInvoice) throws FinderException {

        Debug.print("ArInvoiceEntryControllerBean tagSalesPerson");
        LocalArSalesperson arSalesperson = null;
        if (salesPersonCode != null && salesPersonCode.length() > 0 && !salesPersonCode.equalsIgnoreCase("NO RECORD FOUND")) {
            // if he tagged a salesperson for this invoice
            arSalesperson = arSalespersonHome.findBySlpSalespersonCode(salesPersonCode, companyCode);
            arInvoice.setArSalesperson(arSalesperson);
        } else {
            // if he untagged a salesperson for this invoice
            if (arInvoice.getArSalesperson() != null) {
                arSalesperson = arSalespersonHome.findBySlpSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode(), companyCode);
                arSalesperson.dropArInvoice(arInvoice);
            }
            // if no salesperson is set, invoice should not be subject to commission
            arInvoice.setInvSubjectToCommission((byte) 0);
        }
    }

    private void clearOtherRecords(LocalArInvoice arInvoice) throws RemoveException {

        Debug.print("ArInvoiceEntryControllerBean clearOtherRecords");
        Iterator i;
        // remove all invoice lines
        Collection arInvoiceLines = arInvoice.getArInvoiceLines();
        i = arInvoiceLines.iterator();
        while (i.hasNext()) {
            LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) i.next();
            i.remove();
            em.remove(arInvoiceLine);
        }
        // remove all sales order lines
        Collection arSalesOrderInvoiceLines = arInvoice.getArSalesOrderInvoiceLines();
        i = arSalesOrderInvoiceLines.iterator();
        while (i.hasNext()) {
            LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) i.next();
            i.remove();
            em.remove(arSalesOrderInvoiceLine);
        }
        // remove all job order lines
        Collection arJobOrderInvoiceLines = arInvoice.getArJobOrderInvoiceLines();
        i = arJobOrderInvoiceLines.iterator();
        while (i.hasNext()) {
            LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) i.next();
            i.remove();
            em.remove(arJobOrderInvoiceLine);
        }
        // remove all distribution records
        Collection arDistributionRecords = arInvoice.getArDistributionRecords();
        i = arDistributionRecords.iterator();
        while (i.hasNext()) {
            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();
            i.remove();
            em.remove(arDistributionRecord);
        }
    }

    private void clearInvoiceLineItem(Integer companyCode, Iterator i) throws RemoveException {

        Debug.print("ArInvoiceEntryControllerBean clearInvoiceLineItem");
        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();
        double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
        arInvoiceLineItem.getInvItemLocation().setIlCommittedQuantity(arInvoiceLineItem.getInvItemLocation().getIlCommittedQuantity() - convertedQuantity);
        i.remove();
        em.remove(arInvoiceLineItem);
    }

    private void createInvTagList(LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine, ArrayList<InvModTagListDetails> list, Integer companyCode) throws Exception {

        Debug.print("ArInvoiceEntryControllerBean createInvTagList");
        try {
            Iterator t = list.iterator();
            LocalInvTag invTag;
            while (t.hasNext()) {
                InvModTagListDetails tgLstDetails = (InvModTagListDetails) t.next();
                if (tgLstDetails.getTgCode() == null) {
                    invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), companyCode, tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());
                    invTag.setArSalesOrderInvoiceLine(arSalesOrderInvoiceLine);
                    invTag.setInvItemLocation(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation());
                    LocalAdUser adUser = null;
                    try {
                        adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), companyCode);
                    }
                    catch (FinderException ex) {

                    }
                    invTag.setAdUser(adUser);
                }
            }
        }
        catch (Exception ex) {
        }
    }

    private void createInvTagList(LocalArJobOrderInvoiceLine arJobOrderInvoiceLine, ArrayList<InvModTagListDetails> list, Integer companyCode) throws Exception {

        Debug.print("ArInvoiceEntryControllerBean createInvTagList");
        try {
            Iterator t = list.iterator();
            LocalInvTag invTag;
            while (t.hasNext()) {
                InvModTagListDetails tgLstDetails = (InvModTagListDetails) t.next();
                if (tgLstDetails.getTgCode() == null) {
                    invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), companyCode, tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());
                    invTag.setArJobOrderInvoiceLine(arJobOrderInvoiceLine);
                    invTag.setInvItemLocation(arJobOrderInvoiceLine.getArJobOrderLine().getInvItemLocation());
                    LocalAdUser adUser = null;
                    try {
                        adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), companyCode);
                    }
                    catch (FinderException ex) {

                    }
                    invTag.setAdUser(adUser);
                }
            }
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    private void invoiceLineItems(LocalArTaxCode arTaxCode, ArrayList list, Collection arInvoiceLineItems) {

        Debug.print("ArInvoiceEntryControllerBean invoiceLineItems");
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
            iliDetails.setIliIiClass(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiClass());
            iliDetails.setIliDiscount1(arInvoiceLineItem.getIliDiscount1());
            iliDetails.setIliDiscount2(arInvoiceLineItem.getIliDiscount2());
            iliDetails.setIliDiscount3(arInvoiceLineItem.getIliDiscount3());
            iliDetails.setIliDiscount4(arInvoiceLineItem.getIliDiscount4());
            iliDetails.setIliTotalDiscount(arInvoiceLineItem.getIliTotalDiscount());
            iliDetails.setIliMisc(arInvoiceLineItem.getIliMisc());
            iliDetails.setIliTax(arInvoiceLineItem.getIliTax());

            if (arTaxCode.getTcType().equals("INCLUSIVE")) {
                iliDetails.setIliAmount(arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount());
            } else {
                iliDetails.setIliAmount(arInvoiceLineItem.getIliAmount());
            }
            iliDetails.setTraceMisc(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc());

            if (iliDetails.getTraceMisc() == 1) {
                ArrayList tagList = this.getInvTagList(arInvoiceLineItem);
                iliDetails.setIliTagList(tagList);
            }
            list.add(iliDetails);
        }
    }

    private void jobOrderLines(LocalArTaxCode arTaxCode, ArrayList<ArModJobOrderLineDetails> list, Collection arJobOrderInvoiceLines) {

        Debug.print("ArInvoiceEntryControllerBean jobOrderLines");
        for (Object arJobOrderinvoiceLine : arJobOrderInvoiceLines) {
            LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) arJobOrderinvoiceLine;
            LocalArJobOrderLine arJobOrderLine = arJobOrderInvoiceLine.getArJobOrderLine();
            ArModJobOrderLineDetails mdetails = new ArModJobOrderLineDetails();
            mdetails.setJolCode(arJobOrderLine.getJolCode());
            mdetails.setJolLine(arJobOrderLine.getJolLine());
            mdetails.setJolIiName(arJobOrderLine.getInvItemLocation().getInvItem().getIiName());
            mdetails.setJolIiDescription(arJobOrderLine.getInvItemLocation().getInvItem().getIiDescription());
            mdetails.setJolLocName(arJobOrderLine.getInvItemLocation().getInvLocation().getLocName());

            Iterator j = arJobOrderLine.getArJobOrderInvoiceLines().iterator();
            double soldItems = 0d;
            while (j.hasNext()) {
                LocalArJobOrderInvoiceLine arJOILine = (LocalArJobOrderInvoiceLine) j.next();
                soldItems += arJOILine.getJilQuantityDelivered();
            }

            mdetails.setJolRemaining(arJobOrderLine.getJolQuantity() - soldItems);
            mdetails.setJolQuantity(arJobOrderInvoiceLine.getJilQuantityDelivered());
            mdetails.setJolUomName(arJobOrderLine.getInvUnitOfMeasure().getUomName());
            mdetails.setJolUnitPrice(arJobOrderLine.getJolUnitPrice());

            if (arTaxCode.getTcType().equals("INCLUSIVE")) {
                mdetails.setJolAmount(arJobOrderInvoiceLine.getJilAmount() + arJobOrderInvoiceLine.getJilTaxAmount());
            } else {
                mdetails.setJolAmount(arJobOrderInvoiceLine.getJilAmount());
            }
            mdetails.setJolIssue(true);
            mdetails.setJolDiscount1(arJobOrderInvoiceLine.getJilDiscount1());
            mdetails.setJolDiscount2(arJobOrderInvoiceLine.getJilDiscount2());
            mdetails.setJolDiscount3(arJobOrderInvoiceLine.getJilDiscount3());
            mdetails.setJolDiscount4(arJobOrderInvoiceLine.getJilDiscount4());
            mdetails.setJolTotalDiscount(arJobOrderInvoiceLine.getJilTotalDiscount());
            mdetails.setJolMisc(arJobOrderInvoiceLine.getJilImei());
            mdetails.setJolTax(arJobOrderInvoiceLine.getJilTax());
            mdetails.setTraceMisc(arJobOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc());

            if (mdetails.getTraceMisc() == 1) {
                ArrayList tagList = this.getInvTagList(arJobOrderInvoiceLine);
                mdetails.setJolTagList(tagList);
            }
            list.add(mdetails);
        }
    }

    private void salesOrderLines(LocalArTaxCode arTaxCode, ArrayList list, Collection arSalesOrderInvoiceLines) {

        Debug.print("ArInvoiceEntryControllerBean salesOrderLines");
        for (Object salesOrderInvoiceLine : arSalesOrderInvoiceLines) {
            LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) salesOrderInvoiceLine;
            LocalArSalesOrderLine arSalesOrderLine = arSalesOrderInvoiceLine.getArSalesOrderLine();
            ArModSalesOrderLineDetails mdetails = new ArModSalesOrderLineDetails();
            mdetails.setSolCode(arSalesOrderLine.getSolCode());
            mdetails.setSolLine(arSalesOrderLine.getSolLine());
            mdetails.setSolIiName(arSalesOrderLine.getInvItemLocation().getInvItem().getIiName());
            mdetails.setSolIiDescription(arSalesOrderLine.getInvItemLocation().getInvItem().getIiDescription());
            mdetails.setSolLocName(arSalesOrderLine.getInvItemLocation().getInvLocation().getLocName());
            Iterator j = arSalesOrderLine.getArSalesOrderInvoiceLines().iterator();
            double soldItems = 0d;
            while (j.hasNext()) {
                LocalArSalesOrderInvoiceLine arSOILine = (LocalArSalesOrderInvoiceLine) j.next();
                soldItems += arSOILine.getSilQuantityDelivered();
            }
            mdetails.setSolRemaining(arSalesOrderLine.getSolQuantity() - soldItems);
            mdetails.setSolQuantity(arSalesOrderInvoiceLine.getSilQuantityDelivered());
            mdetails.setSolUomName(arSalesOrderLine.getInvUnitOfMeasure().getUomName());
            mdetails.setSolUnitPrice(arSalesOrderLine.getSolUnitPrice());

            if (arTaxCode.getTcType().equals("INCLUSIVE")) {
                mdetails.setSolAmount(arSalesOrderInvoiceLine.getSilAmount() + arSalesOrderInvoiceLine.getSilTaxAmount());
            } else {
                mdetails.setSolAmount(arSalesOrderInvoiceLine.getSilAmount());
            }

            mdetails.setSolIssue(true);
            mdetails.setSolDiscount1(arSalesOrderInvoiceLine.getSilDiscount1());
            mdetails.setSolDiscount2(arSalesOrderInvoiceLine.getSilDiscount2());
            mdetails.setSolDiscount3(arSalesOrderInvoiceLine.getSilDiscount3());
            mdetails.setSolDiscount4(arSalesOrderInvoiceLine.getSilDiscount4());
            mdetails.setSolTotalDiscount(arSalesOrderInvoiceLine.getSilTotalDiscount());
            mdetails.setSolMisc(arSalesOrderInvoiceLine.getSilImei());
            mdetails.setSolTax(arSalesOrderInvoiceLine.getSilTax());
            mdetails.setTraceMisc(arSalesOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc());

            if (mdetails.getTraceMisc() == 1) {
                ArrayList tagList = this.getInvTagList(arSalesOrderInvoiceLine);
                mdetails.setSolTagList(tagList);
            }
            list.add(mdetails);
        }
    }

    private void invoiceLines(LocalArTaxCode arTaxCode, ArrayList list, Collection arInvoiceLines) {

        Debug.print("ArInvoiceEntryControllerBean invoiceLines");
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

    private ArrayList expiryDates(String misc, double qty) throws Exception {

        Debug.print("ArInvoiceEntryControllerBean expiryDates");
        ArrayList miscList = new ArrayList();
        try {
            String separator = "$";

            // Remove first $ character
            misc = misc.substring(1);

            // Counter
            int start = 0;
            int nextIndex = misc.indexOf(separator, start);
            int length = nextIndex - start;

            for (int x = 0; x < qty; x++) {

                // Date
                start = nextIndex + 1;
                nextIndex = misc.indexOf(separator, start);
                length = nextIndex - start;
                String checker = misc.substring(start, start + length);
                if (checker.length() != 0 || checker != "null") {
                    miscList.add(checker);
                } else {
                    miscList.add("null");
                }
            }
        }
        catch (Exception ex) {
        }
        return miscList;
    }

    private ArrayList<InvModTagListDetails> getInvTagList(LocalArSalesOrderLine arSalesOrderLine) {

        Debug.print("ArInvoiceEntryControllerBean getInvTagList - Sales Order Line");
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
            }
            catch (Exception ex) {
                tgLstDetails.setTgCustodian("");
            }
            list.add(tgLstDetails);
        }
        return list;
    }

    private ArrayList getInvTagList(LocalArJobOrderLine arJobOrderLine) {

        Debug.print("ArInvoiceEntryControllerBean getInvTagList - Job Order Line");
        ArrayList list = new ArrayList();
        Collection invTags = arJobOrderLine.getInvTags();
        for (Object tag : invTags) {
            LocalInvTag invTag = (LocalInvTag) tag;
            InvModTagListDetails tgLstDetails = new InvModTagListDetails();
            tgLstDetails.setTgPropertyCode(invTag.getTgPropertyCode());
            tgLstDetails.setTgSpecs(invTag.getTgSpecs());
            tgLstDetails.setTgExpiryDate(invTag.getTgExpiryDate());
            tgLstDetails.setTgSerialNumber(invTag.getTgSerialNumber());
            try {

                tgLstDetails.setTgCustodian(invTag.getAdUser().getUsrName());
            }
            catch (Exception ex) {
                tgLstDetails.setTgCustodian("");
            }

            list.add(tgLstDetails);
        }
        return list;
    }

    private ArrayList getInvTagList(LocalArJobOrderInvoiceLine arJobOrderInvoiceLine) {

        Debug.print("ArInvoiceEntryControllerBean getInvTagList - Job Order Invoice Line");
        ArrayList list = new ArrayList();
        Collection invTags = arJobOrderInvoiceLine.getInvTags();
        for (Object tag : invTags) {
            LocalInvTag invTag = (LocalInvTag) tag;
            InvModTagListDetails tgLstDetails = new InvModTagListDetails();
            tgLstDetails.setTgPropertyCode(invTag.getTgPropertyCode());
            tgLstDetails.setTgSpecs(invTag.getTgSpecs());
            tgLstDetails.setTgExpiryDate(invTag.getTgExpiryDate());
            tgLstDetails.setTgSerialNumber(invTag.getTgSerialNumber());
            try {

                tgLstDetails.setTgCustodian(invTag.getAdUser().getUsrName());
            }
            catch (Exception ex) {
                tgLstDetails.setTgCustodian("");
            }

            list.add(tgLstDetails);
        }
        return list;
    }

    private ArrayList getInvTagList(LocalArSalesOrderInvoiceLine arSalsOrderInvoiceLine) {

        Debug.print("ArInvoiceEntryControllerBean getInvTagList - Sales Order Invoice Line");
        ArrayList list = new ArrayList();
        Collection invTags = arSalsOrderInvoiceLine.getInvTags();
        for (Object tag : invTags) {
            LocalInvTag invTag = (LocalInvTag) tag;
            InvModTagListDetails tgLstDetails = new InvModTagListDetails();
            tgLstDetails.setTgPropertyCode(invTag.getTgPropertyCode());
            tgLstDetails.setTgSpecs(invTag.getTgSpecs());
            tgLstDetails.setTgExpiryDate(invTag.getTgExpiryDate());
            tgLstDetails.setTgSerialNumber(invTag.getTgSerialNumber());
            try {

                tgLstDetails.setTgCustodian(invTag.getAdUser().getUsrName());
            }
            catch (Exception ex) {
                tgLstDetails.setTgCustodian("");
            }

            list.add(tgLstDetails);
        }
        return list;
    }

    private ArrayList getInvTagList(LocalArInvoiceLineItem arInvoiceLineItem) {

        Debug.print("ArInvoiceEntryControllerBean getInvTagList - Invoice Line Item");
        ArrayList list = new ArrayList();
        Collection invTags = arInvoiceLineItem.getInvTags();
        for (Object tag : invTags) {
            LocalInvTag invTag = (LocalInvTag) tag;
            InvModTagListDetails tgLstDetails = new InvModTagListDetails();
            tgLstDetails.setTgPropertyCode(invTag.getTgPropertyCode());
            tgLstDetails.setTgSpecs(invTag.getTgSpecs());
            tgLstDetails.setTgExpiryDate(invTag.getTgExpiryDate());
            tgLstDetails.setTgSerialNumber(invTag.getTgSerialNumber());
            try {

                tgLstDetails.setTgCustodian(invTag.getAdUser().getUsrName());
            }
            catch (Exception ex) {
                tgLstDetails.setTgCustodian("");
            }

            list.add(tgLstDetails);
        }
        return list;
    }

    public void ejbCreate() throws CreateException {

        Debug.print("ArInvoiceEntryControllerBean ejbCreate");
    }

}