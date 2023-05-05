/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class ArStandardMemoLineControllerBean
 * @created March 08, 2004, 3:30 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.dao.ar.*;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ar.*;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.ar.ArCCCoaGlReceivableAccountNotFoundException;
import com.ejb.exception.ar.ArCCCoaGlRevenueAccountNotFoundException;
import com.ejb.exception.ar.ArINVAmountExceedsCreditLimitException;
import com.ejb.exception.ar.ArTCInterimAccountInvalidException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdResponsibilityDetails;
import com.util.ar.ArInvoiceDetails;
import com.util.ar.ArReceiptDetails;
import com.util.gl.GlInvestorAccountBalanceDetails;
import com.util.mod.ad.AdModBranchStandardMemoLineDetails;
import com.util.mod.ar.ArModInvoiceDetails;
import com.util.mod.ar.ArModInvoiceLineDetails;
import com.util.mod.ar.ArModInvoiceLineItemDetails;
import com.util.mod.ar.ArModStandardMemoLineDetails;

import jakarta.ejb.*;
import javax.naming.NamingException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Stateless(name = "ArStandardMemoLineControllerEJB")
public class ArStandardMemoLineControllerBean extends EJBContextClass implements ArStandardMemoLineController {

    @EJB
    public PersistenceBeanClass em;
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
    private ILocalGlInvestorAccountBalanceHome glInvestorAccountBalanceHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdBranchCustomerHome adBranchCustomerHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchStandardMemoLineHome adBranchStandardMemoLineHome;
    @EJB
    private LocalAdDiscountHome adDiscountHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdPaymentScheduleHome adPaymentScheduleHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalAdResponsibilityHome adResHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalArAutoAccountingSegmentHome arAutoAccountingSegmentHome;
    @EJB
    private LocalArCustomerBalanceHome arCustomerBalanceHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArInvoiceBatchHome arInvoiceBatchHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArInvoiceLineHome arInvoiceLineHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArSalespersonHome arSalespersonHome;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
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
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;


    public String generateArInvestorBonusAndInterest(String SPL_SUPPLIER_CODE, boolean isRecalculate, String PERIOD_MONTH, int PERIOD_YEAR, String USER_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException, GlJREffectiveDatePeriodClosedException {

        Debug.print("ArStandardMemoLineControllerBean generateArInvestorInterest");
        try {

            int SUPPLIER_GENERATED = 0;
            int SUPPLIER_ALREADY_GENERATED = 0;
            int SUPPLIER_HAVE_DRAFT_AR = 0;
            int SUPPLIER_HAVE_DRAFT_AP = 0;
            int SUPPLIER_NO_DEPOST_WITHDRAW = 0;

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode);

            LocalGlSetOfBook glJournalSetOfBook = null;
            LocalGlAccountingCalendarValue glAccountingCalendarValue = null;
            Date dateNow = EJBCommon.getGcCurrentStartOfDay();
            // Date dateNow = DateTime.now().withTimeAtStartOfDay().toDate();

            try {

                glJournalSetOfBook = glSetOfBookHome.findByDate(dateNow, companyCode);

                Collection glSetOfBooks = glSetOfBookHome.findByAcvPeriodPrefixAndDate(PERIOD_MONTH, EJBCommon.getIntendedDate(PERIOD_YEAR), companyCode);
                ArrayList glSetOfBookList = new ArrayList(glSetOfBooks);
                LocalGlSetOfBook glSetOfBook = (LocalGlSetOfBook) glSetOfBookList.get(0);

                glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodPrefix(glSetOfBook.getGlAccountingCalendar().getAcCode(), PERIOD_MONTH, companyCode);

            } catch (FinderException ex) {

                throw new GlJREffectiveDateNoPeriodExistException();
            }

            Collection glInvestorAccountBalancesSupplierList = glInvestorAccountBalanceHome.findByAcvCode(glAccountingCalendarValue.getAcvCode(), companyCode);

            // loop for multiple supplier investors
            for (Object value : glInvestorAccountBalancesSupplierList) {

                LocalGlInvestorAccountBalance glInvestorAccountBalance = (LocalGlInvestorAccountBalance) value;

                if (!SPL_SUPPLIER_CODE.equals("")) {

                    if (!glInvestorAccountBalance.getApSupplier().getSplSupplierCode().equals(SPL_SUPPLIER_CODE)) {
                        continue;
                    }
                }

                double THRES_HOLD = 500000d;

                double BONUS_PRCNT = glInvestorAccountBalance.getApSupplier().getApSupplierClass().getScInvestorBonusRate() / 100;
                double INTEREST_PRCNT = glInvestorAccountBalance.getApSupplier().getApSupplierClass().getScInvestorInterestRate() / 100;

                Debug.print("BONUS_PRCNT=" + BONUS_PRCNT);
                Debug.print("INTEREST_PRCNT=" + INTEREST_PRCNT);

                Date dateNowEndOfTheMonth = glInvestorAccountBalance.getGlAccountingCalendarValue().getAcvDateTo();

                int supplierCode = glInvestorAccountBalance.getApSupplier().getSplCode();
                Debug.print("Supplier:" + supplierCode);

                Debug.print(glInvestorAccountBalance.getIrabBonus() + " and " + glInvestorAccountBalance.getIrabInterest());
                Debug.print("is Recalculate " + isRecalculate);
                double BEGGINING_BALANCE = glInvestorAccountBalance.getIrabBeginningBalance();
                double RUN_BLNC_BONUS = BEGGINING_BALANCE;
                double RUN_BLNC_INTEREST = BEGGINING_BALANCE;

                if (glInvestorAccountBalance.getIrabEndingBalance() == 0) {
                    Debug.print(supplierCode + " not generated");
                    SUPPLIER_NO_DEPOST_WITHDRAW++;
                    continue;
                }

                if (!isRecalculate) {

                    if ((glInvestorAccountBalance.getIrabBonus() != 0 && glInvestorAccountBalance.getIrabInterest() != 0)) {

                        Debug.print(supplierCode + " not generated");
                        SUPPLIER_ALREADY_GENERATED++;
                        continue;
                    }

                } else {

                    // void existing receipt investor

                    Collection arReceipts = arReceiptHome.findInvestorReceiptByIrabCode(glInvestorAccountBalance.getIrabCode(), companyCode);
                    Iterator y = arReceipts.iterator();
                    // loop for multiple supplier investors

                    Debug.print("Entering voiding ");
                    while (y.hasNext()) {

                        LocalArReceipt arReceipt = (LocalArReceipt) y.next();

                        ArrayList ilList = new ArrayList();

                        ArReceiptDetails details = new ArReceiptDetails();

                        details.setRctCode(arReceipt.getRctCode());
                        details.setRctVoid(EJBCommon.TRUE);

                        Debug.print("rctCode to void" + arReceipt.getRctCode());

                        this.saveArRctEntry(details, glInvestorAccountBalance, "INVESTOR FUND", "NONE", "NONE", "PGK", arReceipt.getArCustomer().getCstCustomerCode(), "", ilList, false, "", AD_BRNCH, companyCode);
                    }

                    BONUS_PRCNT = glInvestorAccountBalance.getIrabMonthlyBonusRate() / 100;
                    INTEREST_PRCNT = glInvestorAccountBalance.getIrabMonthlyInterestRate() / 100;
                }

                Date acvDateFrom = glInvestorAccountBalance.getGlAccountingCalendarValue().getAcvDateFrom();
                Date acvDateTo = glInvestorAccountBalance.getGlAccountingCalendarValue().getAcvDateTo();

                Date RUN_DATE = acvDateFrom;

                int CHECK_DATE_GAP = (int) ChronoUnit.DAYS.between(EJBCommon.convertLocalDateObject(acvDateTo), EJBCommon.convertLocalDateObject(dateNowEndOfTheMonth));

                // This will check if lines is not exceeding in current date
                if (CHECK_DATE_GAP < 0) {
                    Debug.print("Date exceed. Loop will now exit");
                    break;
                }

                Collection arDraftRCTDrs = null;
                Collection apDraftCHKDrs = null;
                Collection arPostedRCTDrs = null;
                Collection apPostedCHKDrs = null;

                double RUN_BONUS = 0d;
                double RUN_INTEREST = 0d;
                Debug.print(glAccountingCalendarValue.getAcvDateFrom() + " and  " + glAccountingCalendarValue.getAcvDateTo());
                try {

                    // check if draft exist

                    arDraftRCTDrs = arReceiptHome.findUnPostedRctByRctDateRangeAndSplCode(acvDateFrom, acvDateTo, supplierCode, AD_BRNCH, companyCode);

                    if (arDraftRCTDrs.size() > 0) {
                        SUPPLIER_HAVE_DRAFT_AR++;
                    }

                    // get posted
                    arPostedRCTDrs = arReceiptHome.findPostedRctByRctDateRangeAndSplCode(acvDateFrom, acvDateTo, supplierCode, AD_BRNCH, companyCode);

                } catch (Exception ex) {
                }

                try {

                    // check if draft exist

                    apDraftCHKDrs = apCheckHome.findUnPostedChkByChkDateRangeAndSplCode(acvDateFrom, acvDateTo, supplierCode, AD_BRNCH, companyCode);
                    if (apDraftCHKDrs.size() > 0) {
                        SUPPLIER_HAVE_DRAFT_AP++;
                    }

                    apPostedCHKDrs = apCheckHome.findPostedChkByChkDateRangeAndSplCode(acvDateFrom, acvDateTo, supplierCode, AD_BRNCH, companyCode);

                } catch (Exception ex) {
                }

                if ((SUPPLIER_HAVE_DRAFT_AR + SUPPLIER_HAVE_DRAFT_AP) > 0) {
                    continue;
                }
                Debug.print("RECEIPT: " + arPostedRCTDrs.size());
                Debug.print("CHECK: " + apPostedCHKDrs.size());

                ArrayList list = new ArrayList();

                if (arPostedRCTDrs != null) {

                    for (Object arPostedRCTDr : arPostedRCTDrs) {

                        LocalArReceipt arReceipt = (LocalArReceipt) arPostedRCTDr;
                        GlInvestorAccountBalanceDetails details = new GlInvestorAccountBalanceDetails();

                        if (arReceipt.getGlInvestorAccountBalance() == null) {
                            details.setGlIrabCode(glInvestorAccountBalance.getIrabCode());
                            details.setGlIrabDate(arReceipt.getRctDate());
                            details.setGlIrabDocumentNumber(arReceipt.getRctNumber());
                            details.setGlIrabType("ADD");
                            details.setGlIrabAmount(arReceipt.getRctAmount());

                            list.add(details);
                        }
                    }
                }

                //
                if (apPostedCHKDrs != null) {

                    for (Object apPostedCHKDr : apPostedCHKDrs) {

                        LocalApCheck apCheck = (LocalApCheck) apPostedCHKDr;
                        GlInvestorAccountBalanceDetails details = new GlInvestorAccountBalanceDetails();

                        details.setGlIrabCode(glInvestorAccountBalance.getIrabCode());
                        details.setGlIrabDate(apCheck.getChkDate());
                        details.setGlIrabDocumentNumber(apCheck.getChkDocumentNumber());
                        details.setGlIrabType("SUB");
                        details.setGlIrabAmount(apCheck.getChkAmount());

                        list.add(details);
                    }
                }

                list.sort(GlInvestorAccountBalanceDetails.DateComparator);

                for (Object o : list) {

                    GlInvestorAccountBalanceDetails details = (GlInvestorAccountBalanceDetails) o;
                    Date CURRENT_DATE = details.getGlIrabDate();

                    int DAYS_BETWEEN = (int) ChronoUnit.DAYS.between(EJBCommon.convertLocalDateObject(RUN_DATE), EJBCommon.convertLocalDateObject(CURRENT_DATE));

                    Debug.print(CURRENT_DATE + " BETWEEN " + RUN_DATE + " = " + DAYS_BETWEEN);

                    RUN_BLNC_BONUS = EJBCommon.roundIt(RUN_BLNC_BONUS, precisionUnit);

                    double CURRENT_BONUS = (RUN_BLNC_BONUS > THRES_HOLD) ? (RUN_BLNC_BONUS - THRES_HOLD) * (BONUS_PRCNT / 365) * DAYS_BETWEEN : 0d;
                    double CURRENT_INTEREST = RUN_BLNC_INTEREST * (INTEREST_PRCNT / 365) * DAYS_BETWEEN;

                    Debug.print("CURRENT_BONUS=" + CURRENT_BONUS);
                    Debug.print("DAYS BETWEEN=" + DAYS_BETWEEN);
                    Debug.print("RUN BAL BONUS=" + RUN_BLNC_BONUS);
                    Debug.print("CURRENT_INTEREST=" + CURRENT_INTEREST);
                    Debug.print("RUN BAL INTEREST=" + RUN_BLNC_INTEREST);
                    Debug.print("AMOUNT=" + details.getGlIrabAmount());

                    RUN_BLNC_BONUS += (details.getGlIrabType().equals("ADD") ? details.getGlIrabAmount() : details.getGlIrabAmount() * -1);
                    RUN_BLNC_INTEREST += (details.getGlIrabType().equals("ADD") ? details.getGlIrabAmount() : details.getGlIrabAmount() * -1);

                    Debug.print("------------------------------------------------------------------------");
                    RUN_DATE = CURRENT_DATE;
                    RUN_BONUS += CURRENT_BONUS;
                    RUN_INTEREST += CURRENT_INTEREST;
                }

                int DAYS_BETWEEN = (int) ChronoUnit.DAYS.between(EJBCommon.convertLocalDateObject(RUN_DATE), EJBCommon.convertLocalDateObject(acvDateTo)) + 1;

                double CURRENT_BONUS = (RUN_BLNC_BONUS > THRES_HOLD) ? (RUN_BLNC_BONUS - THRES_HOLD) * (BONUS_PRCNT / 365) * DAYS_BETWEEN : 0d;
                double CURRENT_INTEREST = RUN_BLNC_INTEREST * (INTEREST_PRCNT / 365) * DAYS_BETWEEN;

                RUN_BONUS += CURRENT_BONUS;
                RUN_INTEREST += CURRENT_INTEREST;
                double[] myList = {RUN_BONUS, RUN_INTEREST};

                Debug.print(acvDateTo + " BETWEEN " + RUN_DATE + " = " + DAYS_BETWEEN);
                Debug.print("CURRENT_BONUS=" + CURRENT_BONUS);
                Debug.print("CURRENT_INTEREST=" + CURRENT_INTEREST);
                Debug.print("RUN_BONUS=" + RUN_BONUS);
                Debug.print("RUN_INTEREST=" + RUN_INTEREST);

                glInvestorAccountBalance.setIrabTotalBonus(EJBCommon.roundIt(RUN_BONUS, precisionUnit));
                glInvestorAccountBalance.setIrabTotalInterest(EJBCommon.roundIt(RUN_INTEREST, precisionUnit));

                LocalArCustomer arCustomer = arCustomerHome.findByCstBySupplierCode(glInvestorAccountBalance.getApSupplier().getSplCode(), companyCode);

                ArModInvoiceLineDetails mdetails;
                ArReceiptDetails details;

                double BONUS_PERCENT_PRINT = BONUS_PRCNT * 100;
                double INTEREST_PERCENT_PRINT = INTEREST_PRCNT * 100;

                ArrayList ilList = new ArrayList();

                // save Bonus
                if (RUN_BONUS != 0) {
                    Debug.print("create bonus");
                    ilList = new ArrayList();
                    mdetails = new ArModInvoiceLineDetails();

                    mdetails.setIlSmlName("Interest Income - Inscribed Stock");
                    mdetails.setIlDescription("Interest Income - Inscribed Stock");
                    mdetails.setIlQuantity(1);
                    mdetails.setIlUnitPrice(RUN_BONUS);
                    mdetails.setIlAmount(RUN_BONUS);
                    mdetails.setIlTax((byte) 0);
                    ilList.add(mdetails);

                    details = new ArReceiptDetails();

                    details.setRctCode(null);
                    details.setRctDate(acvDateTo);
                    details.setRctNumber(null);
                    details.setRctReferenceNumber(null);
                    details.setRctVoid(EJBCommon.FALSE);
                    details.setRctDescription("Bonus in " + PERIOD_MONTH + " @ " + BONUS_PERCENT_PRINT + "%");
                    details.setRctConversionDate(EJBCommon.convertStringToSQLDate(null));
                    details.setRctConversionRate(1);
                    details.setRctSubjectToCommission(EJBCommon.FALSE);
                    details.setRctPaymentMethod("CASH");
                    details.setRctCustomerDeposit(EJBCommon.FALSE);
                    details.setRctCustomerName(arCustomer.getCstName());
                    details.setRctCustomerAddress(null);
                    details.setRctInvtrInvestorFund(EJBCommon.FALSE);
                    details.setRctInvtrNextRunDate(null);
                    details.setRctCreatedBy(USER_NM);
                    details.setRctDateCreated(new Date());
                    details.setRctLastModifiedBy(USER_NM);
                    details.setRctDateLastModified(new Date());

                    this.saveArRctEntry(details, glInvestorAccountBalance, "INVESTOR FUND", "NONE", "NONE", "PGK", arCustomer.getCstCustomerCode(), "", ilList, false, "", AD_BRNCH, companyCode);
                }

                // save Interest
                if (RUN_INTEREST != 0) {
                    Debug.print("create interest");
                    ilList = new ArrayList();
                    mdetails = new ArModInvoiceLineDetails();

                    mdetails.setIlSmlName("Interest Income - Inscribed Stock");
                    mdetails.setIlDescription("Interest Income - Inscribed Stock");
                    mdetails.setIlQuantity(1);
                    mdetails.setIlUnitPrice(RUN_INTEREST);
                    mdetails.setIlAmount(RUN_INTEREST);
                    mdetails.setIlTax((byte) 0);
                    ilList.add(mdetails);

                    details = new ArReceiptDetails();

                    details.setRctCode(null);
                    details.setRctDate(acvDateTo);
                    details.setRctNumber(null);
                    details.setRctReferenceNumber(null);
                    details.setRctVoid(EJBCommon.FALSE);
                    details.setRctDescription("Interest in " + PERIOD_MONTH + " @ " + INTEREST_PERCENT_PRINT + "%");
                    details.setRctConversionDate(EJBCommon.convertStringToSQLDate(null));
                    details.setRctConversionRate(1);
                    details.setRctSubjectToCommission(EJBCommon.FALSE);
                    details.setRctPaymentMethod("CASH");
                    details.setRctCustomerDeposit(EJBCommon.FALSE);
                    details.setRctCustomerName(arCustomer.getCstName());
                    details.setRctCustomerAddress(null);
                    details.setRctInvtrInvestorFund(EJBCommon.FALSE);
                    details.setRctInvtrNextRunDate(null);
                    details.setRctCreatedBy(USER_NM);
                    details.setRctDateCreated(new Date());
                    details.setRctLastModifiedBy(USER_NM);
                    details.setRctDateLastModified(new Date());

                    this.saveArRctEntry(details, glInvestorAccountBalance, "INVESTOR FUND", "NONE", "NONE", "PGK", arCustomer.getCstCustomerCode(), "", ilList, false, "", AD_BRNCH, companyCode);
                }
                SUPPLIER_GENERATED++;

                glInvestorAccountBalance.setIrabInterest(EJBCommon.TRUE);
                glInvestorAccountBalance.setIrabBonus(EJBCommon.TRUE);
                glInvestorAccountBalance.setIrabMonthlyBonusRate(BONUS_PRCNT * 100);
                glInvestorAccountBalance.setIrabMonthlyInterestRate(INTEREST_PRCNT * 100);
            }

            // SUPPLIER_GENERATED
            // SUPPLIER_ALREADY_GENERATED;
            // SUPPLIER_HAVE_DRAFT_AR
            // SUPPLIER_HAVE_DRAFT_AP

            return "SUPPLIER GENERATE=" + SUPPLIER_GENERATED + "  SUPPLIER ALREADY GENERATED=" + SUPPLIER_ALREADY_GENERATED + "  SUPPLIER HAVE DRAFT AR=" + SUPPLIER_HAVE_DRAFT_AR + "  SUPPLIER HAVE DRAFT AP=" + SUPPLIER_HAVE_DRAFT_AP + "  SUPPLIER NO DEPOST/WITHDRAW=" + SUPPLIER_NO_DEPOST_WITHDRAW;

        } catch (GlJREffectiveDatePeriodClosedException gx) {
            throw new GlJREffectiveDatePeriodClosedException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getSupplierInvestors(Integer companyCode) throws GlobalNoRecordFoundException, GlJREffectiveDatePeriodClosedException {

        Debug.print("ArStandardMemoLineControllerBean generateArInvestorInterest");
        ArrayList supplierList = new ArrayList();
        try {
            Collection glInvestorAccountBalancesSupplierList = glInvestorAccountBalanceHome.findByAcvCode(1, companyCode);

            // loop for multiple supplier investors
            for (Object o : glInvestorAccountBalancesSupplierList) {

                LocalGlInvestorAccountBalance glInvestorAccountBalance = (LocalGlInvestorAccountBalance) o;

                if (glInvestorAccountBalance.getApSupplier().getApSupplierClass().getScName().contains("Investors")) {
                    supplierList.add(glInvestorAccountBalance.getApSupplier().getSplSupplierCode());
                }
            }
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        return supplierList;
    }

    public int generateArAccruedInterestIS(String USER_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException, GlJREffectiveDatePeriodClosedException {

        Debug.print("ArStandardMemoLineControllerBean generateArAccruedInterestIS");
        boolean isError = false;
        int noOfSubmitted = 0;
        try {

            Collection accruedInvoices = apCheckHome.findDirectChkForAccruedInterestISGeneration(AD_BRNCH, companyCode);

            for (Object accruedInvoice : accruedInvoices) {

                LocalApCheck apCheck = (LocalApCheck) accruedInvoice;

                Date maturityDate = apCheck.getChkInvtMaturityDate();
                Date nextRunDate = apCheck.getChkInvtNextRunDate();

                int CHECK_MATURITY_DATE_GAP = (int) ChronoUnit.DAYS.between(EJBCommon.convertLocalDateObject(nextRunDate), EJBCommon.convertLocalDateObject(maturityDate));

                // This will check if lines is not exceeding in maturity date
                if (CHECK_MATURITY_DATE_GAP < 0) {
                    Debug.print("maturity date gap " + CHECK_MATURITY_DATE_GAP);
                    continue;
                }

                int CHECK_NEXTRUN_DATE_GAP = (int) ChronoUnit.DAYS.between(EJBCommon.convertLocalDateObject(nextRunDate), EJBCommon.convertLocalDateObject(new Date()));

                // This will check if lines is not exceeding in current date
                if (CHECK_NEXTRUN_DATE_GAP < 0) {
                    Debug.print("nextrundate date gap " + CHECK_NEXTRUN_DATE_GAP);
                    continue;
                }

                double interestAmount = EJBCommon.roundIt((apCheck.getChkInvtFaceValue() * (apCheck.getChkInvtCouponRate() / 100)) / 12, (short) 2);
                String customerCode = apCheck.getApSupplier().getSplSupplierCode();
                String referenceNumber = apCheck.getChkDocumentNumber();

                ArrayList ilList = new ArrayList();
                ArModInvoiceLineDetails mdetails = new ArModInvoiceLineDetails();

                mdetails.setIlSmlName("Accrued Interest - IS");
                mdetails.setIlDescription("Accrued Interest - IS");
                mdetails.setIlQuantity(1);
                mdetails.setIlUnitPrice(interestAmount);
                mdetails.setIlAmount(interestAmount);
                mdetails.setIlTax((byte) 0);
                ilList.add(mdetails);

                ArInvoiceDetails details = new ArInvoiceDetails();
                Date d = new Date();
                // SimpleDateFormat dtform = new SimpleDateFormat(Constants.DATE_FORMAT_INPUT);

                details.setInvCode(null);
                details.setInvType("MEMO LINES");
                details.setInvDate(apCheck.getChkInvtNextRunDate());
                details.setInvNumber(null);
                details.setInvReferenceNumber(referenceNumber);
                details.setInvVoid(EJBCommon.FALSE);
                details.setInvDescription("SYSTEM GENERATED ACCRUED INTEREST FOR " + referenceNumber);
                details.setInvBillToAddress("");
                details.setInvBillToContact("");
                details.setInvBillToAltContact("");
                details.setInvBillToPhone("");
                details.setInvBillingHeader("");
                details.setInvBillingFooter("");
                details.setInvBillingHeader2("");
                details.setInvBillingFooter2("");
                details.setInvBillingHeader3(null);
                details.setInvBillingFooter3(null);
                details.setInvBillingSignatory("");
                details.setInvSignatoryTitle("");
                details.setInvShipToAddress("");
                details.setInvShipToContact("");
                details.setInvShipToAltContact("");
                details.setInvShipToPhone("");
                details.setInvLvFreight("");
                details.setInvShipDate(EJBCommon.convertStringToSQLDate(null));
                details.setInvConversionDate(EJBCommon.convertStringToSQLDate(null));
                details.setInvConversionRate(1);
                details.setInvDebitMemo((byte) 0);
                details.setInvSubjectToCommission((byte) 0);
                details.setInvClientPO("");
                details.setInvEffectivityDate(apCheck.getChkInvtMaturityDate());
                details.setInvReceiveDate(EJBCommon.convertStringToSQLDate(""));
                details.setInvCreatedBy(USER_NM);
                details.setInvDateCreated(new Date());
                details.setInvLastModifiedBy(USER_NM);
                details.setInvDateLastModified(new Date());

                details.setInvDisableInterest(EJBCommon.TRUE);
                details.setInvInterest(EJBCommon.TRUE);
                details.setInvInterestReferenceNumber(referenceNumber);
                details.setInvInterestAmount(interestAmount);
                details.setInvInterestCreatedBy(USER_NM);
                details.setInvInterestDateCreated(new Date());

                // assign batch name , if not existed, create one

                String INV_NUMBER = this.saveArInvEntry(details, "IMMEDIATE", "NONE", "NONE", "PGK", customerCode, null, ilList, true, null, AD_BRNCH, companyCode);

                // update the next Run Date

                Calendar date = Calendar.getInstance();
                date.setTime(nextRunDate);
                date.add(Calendar.MONTH, +1);

                apCheck.setChkInvtNextRunDate(date.getTime());

                noOfSubmitted++;
            }

            Debug.print("accruedInvoices.size()=" + noOfSubmitted);
            return noOfSubmitted;
        } catch (GlJREffectiveDatePeriodClosedException gx) {
            getSessionContext().setRollbackOnly();
            throw new GlJREffectiveDatePeriodClosedException();

        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int generateArAccruedInterestTB(String USER_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException, GlJREffectiveDatePeriodClosedException {

        Debug.print("ArStandardMemoLineControllerBean generateArAccruedInterestTB");
        int noOfSubmitted = 0;
        try {

            Collection accruedInvoices = apCheckHome.findDirectChkForAccruedInterestISGeneration(AD_BRNCH, companyCode);

            for (Object accruedInvoice : accruedInvoices) {

                LocalApCheck apCheck = (LocalApCheck) accruedInvoice;

                Date maturityDate = apCheck.getChkInvtMaturityDate();
                Date nextRunDate = apCheck.getChkInvtNextRunDate();

                int CHECK_MATURITY_DATE_GAP = (int) ChronoUnit.DAYS.between(EJBCommon.convertLocalDateObject(nextRunDate), EJBCommon.convertLocalDateObject(maturityDate));

                // This will check if lines is not exceeding in maturity date
                if (CHECK_MATURITY_DATE_GAP < 0) {
                    Debug.print("maturity date gap " + CHECK_MATURITY_DATE_GAP);
                    continue;
                }

                int CHECK_NEXTRUN_DATE_GAP = (int) ChronoUnit.DAYS.between(EJBCommon.convertLocalDateObject(nextRunDate), EJBCommon.convertLocalDateObject(new Date()));

                // This will check if lines is not exceeding in current date
                if (CHECK_NEXTRUN_DATE_GAP < 0) {
                    Debug.print("nextrundate date gap " + CHECK_NEXTRUN_DATE_GAP);
                    continue;
                }
                double interestAmount = EJBCommon.roundIt((apCheck.getChkInvtFaceValue() * (apCheck.getChkInvtCouponRate() / 100)) / 12, (short) 2);
                String customerCode = apCheck.getApSupplier().getSplSupplierCode();
                String referenceNumber = apCheck.getChkDocumentNumber();

                ArrayList ilList = new ArrayList();
                ArModInvoiceLineDetails mdetails = new ArModInvoiceLineDetails();

                mdetails.setIlSmlName("Accrued Interest - TB");
                mdetails.setIlDescription("Accrued Interest - TB");
                mdetails.setIlQuantity(1);
                mdetails.setIlUnitPrice(interestAmount);
                mdetails.setIlAmount(interestAmount);
                mdetails.setIlTax((byte) 0);
                ilList.add(mdetails);

                ArInvoiceDetails details = new ArInvoiceDetails();
                Date d = new Date();
                // SimpleDateFormat dtform = new SimpleDateFormat(Constants.DATE_FORMAT_INPUT);

                details.setInvCode(null);
                details.setInvType("MEMO LINES");
                details.setInvDate(apCheck.getChkInvtMaturityDate());
                details.setInvNumber(null);
                details.setInvReferenceNumber(referenceNumber);
                details.setInvVoid(EJBCommon.FALSE);
                details.setInvDescription("SYSTEM GENERATED ACCRUED INTEREST FOR " + referenceNumber);
                details.setInvBillToAddress("");
                details.setInvBillToContact("");
                details.setInvBillToAltContact("");
                details.setInvBillToPhone("");
                details.setInvBillingHeader("");
                details.setInvBillingFooter("");
                details.setInvBillingHeader2("");
                details.setInvBillingFooter2("");
                details.setInvBillingHeader3(null);
                details.setInvBillingFooter3(null);
                details.setInvBillingSignatory("");
                details.setInvSignatoryTitle("");
                details.setInvShipToAddress("");
                details.setInvShipToContact("");
                details.setInvShipToAltContact("");
                details.setInvShipToPhone("");
                details.setInvLvFreight("");
                details.setInvShipDate(EJBCommon.convertStringToSQLDate(null));
                details.setInvConversionDate(EJBCommon.convertStringToSQLDate(null));
                details.setInvConversionRate(1);
                details.setInvDebitMemo((byte) 0);
                details.setInvSubjectToCommission((byte) 0);
                details.setInvClientPO("");
                details.setInvEffectivityDate(apCheck.getChkInvtMaturityDate());
                details.setInvReceiveDate(EJBCommon.convertStringToSQLDate(""));
                details.setInvCreatedBy(USER_NM);
                details.setInvDateCreated(new Date());
                details.setInvLastModifiedBy(USER_NM);
                details.setInvDateLastModified(new Date());

                details.setInvDisableInterest(EJBCommon.TRUE);
                details.setInvInterest(EJBCommon.TRUE);
                details.setInvInterestReferenceNumber(referenceNumber);
                details.setInvInterestAmount(interestAmount);
                details.setInvInterestCreatedBy(USER_NM);
                details.setInvInterestDateCreated(new Date());

                // assign batch name , if not existed, create one

                String INV_NUMBER = this.saveArInvEntry(details, "IMMEDIATE", "NONE", "NONE", "PHP", customerCode, null, ilList, true, null, AD_BRNCH, companyCode);

                // update the next Run Date

                Calendar date = Calendar.getInstance();
                date.setTime(nextRunDate);
                date.add(Calendar.MONTH, +1);

                apCheck.setChkInvtNextRunDate(date.getTime());
            }

            Debug.print("accruedInvoices.size()=" + accruedInvoices.size());
            return accruedInvoices.size();
        } catch (GlJREffectiveDatePeriodClosedException gx) {
            getSessionContext().setRollbackOnly();
            throw new GlJREffectiveDatePeriodClosedException();

        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int generateArOverDueInvoices(String USER_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException, GlJREffectiveDatePeriodClosedException {

        Debug.print("ArStandardMemoLineControllerBean generateArOverDueInvoices");
        int LIMIT_TRANSACTION = 100;
        int countTransaction = 0;
        try {

            String jbossSql = "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.arInvoice.invPosted = 1 AND ips.arInvoice.arCustomer.cstAutoComputeInterest = 1 AND ips.ipsLock = 0 AND ips.arInvoice.invPosted = 1 AND ips.arInvoice.invInterest = 0 AND ips.arInvoice.invDisableInterest = 0 AND ips.arInvoice.invInterestNextRunDate IS NOT NULL AND ips.arInvoice.invInterestNextRunDate <= ?1 AND ips.ipsAmountDue > ips.ipsAmountPaid AND ips.arInvoice.invAdBranch = ?2 AND ips.ipsAdCompany = ?3";

            Object[] obj = new Object[3];

            obj[0] = EJBCommon.convertStringToSQLDate(EJBCommon.convertSQLDateToString(new Date()));
            obj[1] = AD_BRNCH;
            obj[2] = companyCode;

            Collection overDueInvoices = arInvoicePaymentScheduleHome.getIpsByCriteria(jbossSql, obj);

            Iterator x = overDueInvoices.iterator();

            while (x.hasNext() && countTransaction < LIMIT_TRANSACTION) {
                ArInvoiceDetails details = new ArModInvoiceDetails();

                LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) x.next();

                double balance = EJBCommon.roundIt((arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid()), (short) 2);
                double interestAmount = EJBCommon.roundIt((arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid()) * 0.02, (short) 2);
                String customerCode = arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstCustomerCode();
                String referenceNumber = arInvoicePaymentSchedule.getArInvoice().getInvNumber();

                ArrayList ilList = new ArrayList();
                ArModInvoiceLineDetails mdetails = new ArModInvoiceLineDetails();

                Collection arSalesInvoiceLines = arInvoicePaymentSchedule.getArInvoice().getArInvoiceLines();
                Debug.print("arSalesInvoiceLines.size()=" + arSalesInvoiceLines.size());

                Iterator m = arSalesInvoiceLines.iterator();

                String memoLine = "";

                while (m.hasNext()) {

                    LocalArInvoiceLine arSalesInvoiceLine = (LocalArInvoiceLine) m.next();

                    if (arSalesInvoiceLine.getArStandardMemoLine().getSmlDescription().contains("Association Dues") || arSalesInvoiceLine.getArStandardMemoLine().getSmlDescription().contains("ASD")) {

                        memoLine = "AR - Interest ASD";

                    } else if (arSalesInvoiceLine.getArStandardMemoLine().getSmlDescription().contains("RPT")) {

                        memoLine = "AR - Interest RPT";

                    } else if (arSalesInvoiceLine.getArStandardMemoLine().getSmlDescription().contains("Parking Dues") || arSalesInvoiceLine.getArStandardMemoLine().getSmlDescription().contains("PD")) {

                        memoLine = "AR - Interest PD";

                    } else if (arSalesInvoiceLine.getArStandardMemoLine().getSmlDescription().contains("Water") || arSalesInvoiceLine.getArStandardMemoLine().getSmlDescription().contains("WTR")) {

                        memoLine = "AR - Interest WTR";

                    } else {

                        memoLine = "AR - Interest MISC";
                    }
                }

                mdetails.setIlSmlName(memoLine);
                mdetails.setIlDescription(memoLine);
                mdetails.setIlQuantity(1);
                mdetails.setIlUnitPrice(interestAmount);
                mdetails.setIlAmount(interestAmount);
                mdetails.setIlTax((byte) 0);

                ArrayList mdetailsList = new ArrayList();
                ilList.add(mdetails);

                Date d = new Date();
                // SimpleDateFormat dtform = new SimpleDateFormat(Constants.DATE_FORMAT_INPUT);

                details.setInvCode(null);
                details.setInvType("MEMO LINES");
                details.setInvDate(arInvoicePaymentSchedule.getArInvoice().getInvInterestNextRunDate());
                details.setInvNumber(null);
                details.setInvReferenceNumber(referenceNumber);
                details.setInvVoid(EJBCommon.FALSE);
                details.setInvDescription("SYSTEM GENERATED 2% INTEREST FOR " + referenceNumber);
                details.setInvBillToAddress("");
                details.setInvBillToContact("");
                details.setInvBillToAltContact("");
                details.setInvBillToPhone("");
                details.setInvBillingHeader("");
                details.setInvBillingFooter("");
                details.setInvBillingHeader2("");
                details.setInvBillingFooter2("");
                details.setInvBillingHeader3(null);
                details.setInvBillingFooter3(null);
                details.setInvBillingSignatory("");
                details.setInvSignatoryTitle("");
                details.setInvShipToAddress("");
                details.setInvShipToContact("");
                details.setInvShipToAltContact("");
                details.setInvShipToPhone("");
                details.setInvLvFreight("");
                details.setInvShipDate(EJBCommon.convertStringToSQLDate(null));
                details.setInvConversionDate(EJBCommon.convertStringToSQLDate(null));
                details.setInvConversionRate(1);
                details.setInvDebitMemo((byte) 0);
                details.setInvSubjectToCommission((byte) 0);
                details.setInvClientPO("");
                details.setInvEffectivityDate(arInvoicePaymentSchedule.getArInvoice().getInvInterestNextRunDate());
                details.setInvReceiveDate(EJBCommon.convertStringToSQLDate(""));
                details.setInvCreatedBy(USER_NM);
                details.setInvDateCreated(new java.util.Date());
                details.setInvLastModifiedBy(USER_NM);
                details.setInvDateLastModified(new java.util.Date());
                details.setInvDisableInterest(EJBCommon.TRUE);
                details.setInvInterest(EJBCommon.TRUE);
                details.setInvInterestReferenceNumber(referenceNumber);
                details.setInvInterestAmount(balance);
                details.setInvInterestCreatedBy(USER_NM);
                details.setInvInterestDateCreated(new java.util.Date());

                // assign batch name , if not existed, create one

                String batchName = new SimpleDateFormat("MMMM yyyy").format(arInvoicePaymentSchedule.getArInvoice().getInvInterestNextRunDate());
                Debug.print("Date to format: " + batchName + " -----------------------------");
                try {

                    Debug.print("Batch name detected: " + batchName);
                    LocalArInvoiceBatch arInvBatch = arInvoiceBatchHome.findByIbName(batchName, AD_BRNCH, companyCode);

                } catch (FinderException fx) {

                    Debug.print("New Batch name created: " + batchName);
                    arInvoiceBatchHome.create(batchName, batchName, "OPEN", "INVOICE", new Date(), USER_NM, AD_BRNCH, companyCode);
                }

                String INV_NUMBER = this.saveArInvEntry(details, "14 Days Net", "NONE", "NONE", "PHP", customerCode, batchName, ilList, false, null, AD_BRNCH, companyCode);

                arInvoicePaymentSchedule.getArInvoice().setInvInterest(EJBCommon.FALSE);
                arInvoicePaymentSchedule.getArInvoice().setInvInterestReferenceNumber(arInvoicePaymentSchedule.getArInvoice().getInvInterestReferenceNumber() + ":" + INV_NUMBER);
                arInvoicePaymentSchedule.getArInvoice().setInvInterestAmount(arInvoicePaymentSchedule.getArInvoice().getInvInterestAmount() + interestAmount);
                arInvoicePaymentSchedule.getArInvoice().setInvInterestCreatedBy(USER_NM);
                arInvoicePaymentSchedule.getArInvoice().setInvInterestDateCreated(new java.util.Date());
                arInvoicePaymentSchedule.getArInvoice().setInvInterestLastRunDate(new java.util.Date());

                Calendar calendar = new GregorianCalendar();
                // Add I month to InterestNextRunDate
                calendar.setTime(arInvoicePaymentSchedule.getArInvoice().getInvInterestNextRunDate());
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                Debug.print("OLD TIME IS : " + arInvoicePaymentSchedule.getArInvoice().getInvInterestNextRunDate().toString() + " -----------------------");

                Debug.print("NEW TIME IS : " + calendar.getTime() + " -----------------------");
                arInvoicePaymentSchedule.getArInvoice().setInvInterestNextRunDate(calendar.getTime());
                countTransaction++;
            }

            Debug.print("overDueInvoices.size()=" + overDueInvoices.size());
            return overDueInvoices.size();
        } catch (GlJREffectiveDatePeriodClosedException gx) {
            throw new GlJREffectiveDatePeriodClosedException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public int generateArPastDueInvoices(String USER_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArStandardMemoLineControllerBean generateArPastDueInvoices");
        try {

            Collection pastDueInvoices = arInvoicePaymentScheduleHome.findPastdueIpsByPenaltyDueDate(EJBCommon.convertStringToSQLDate(EJBCommon.convertSQLDateToString(new Date())), AD_BRNCH, companyCode);

            Iterator x = pastDueInvoices.iterator();

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode);

            while (x.hasNext()) {

                LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) x.next();

                double ipsAmountDue = arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid();
                double monthlyPenaltyRate = EJBCommon.roundIt(ipsAmountDue * arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstMonthlyInterestRate() / 100, precisionUnit);
                double ipsPenaltyDue = (arInvoicePaymentSchedule.getIpsPenaltyDue() + monthlyPenaltyRate);

                int ctr = arInvoicePaymentSchedule.getIpsPenaltyCounter() + 1;
                GregorianCalendar gcDateDue = new GregorianCalendar();
                gcDateDue.setTime(arInvoicePaymentSchedule.getIpsPenaltyDueDate());
                gcDateDue.add(Calendar.MONTH, 1);

                // Collection arInvoicePaymeSchedule =
                // arInvoicePaymentScheduleHome.findByInvCodeAndAdCompny(INV_CODE, IPS_companyCode)

                arInvoicePaymentSchedule.setIpsPenaltyCounter((short) ctr);
                arInvoicePaymentSchedule.setIpsPenaltyDueDate(gcDateDue.getTime());
                arInvoicePaymentSchedule.setIpsPenaltyDue(ipsPenaltyDue);
                arInvoicePaymentSchedule.getArInvoice().setInvPenaltyDue(arInvoicePaymentSchedule.getArInvoice().getInvPenaltyDue() + monthlyPenaltyRate);

                // create distribution for unearned penalty
                try {

                    LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arInvoicePaymentSchedule.getArInvoice().getArCustomer().getCstCode(), AD_BRNCH, companyCode);

                    this.addArDrEntry(arInvoicePaymentSchedule.getArInvoice().getArDrNextLine(), "UNPENALTY", EJBCommon.FALSE, monthlyPenaltyRate, adBranchCustomer.getBcstGlCoaUnEarnedPenaltyAccount(), null, arInvoicePaymentSchedule.getArInvoice(), AD_BRNCH, companyCode);

                    this.addArDrIliEntry(arInvoicePaymentSchedule.getArInvoice().getArDrNextLine(), "RECEIVABLE PENALTY", EJBCommon.TRUE, monthlyPenaltyRate, adBranchCustomer.getBcstGlCoaReceivableAccount(), arInvoicePaymentSchedule.getArInvoice(), AD_BRNCH, companyCode);

                } catch (FinderException ex) {

                }

                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

                LocalArInvoice arInvoice = arInvoicePaymentSchedule.getArInvoice();
                LocalArInvoice arCreditedInvoice = null;
                String USR_NM = arInvoice.getInvCreatedBy();
                // post to gl if necessary

                if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                    // validate if date has no period and period is closed

                    LocalGlSetOfBook glJournalSetOfBook = null;

                    try {

                        glJournalSetOfBook = glSetOfBookHome.findByDate(arInvoice.getInvDate(), companyCode);

                    } catch (FinderException ex) {

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

                    double TOTAL_DEBIT = 0d;
                    double TOTAL_CREDIT = 0d;

                    while (j.hasNext()) {

                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                        double DR_AMNT = 0d;

                        if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                            DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);

                        } else {

                            DR_AMNT = this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);
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

                            glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS RECEIVABLES", arInvoice.getInvCreditMemo() == EJBCommon.FALSE ? "SALES INVOICES" : "CREDIT MEMOS", companyCode);

                        } catch (FinderException ex) {

                            throw new GlobalJournalNotBalanceException();
                        }

                        if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                            glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                        } else {

                            glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
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

                        if (adPreference.getPrfEnableArInvoiceBatch() == EJBCommon.TRUE) {

                            glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arInvoice.getArInvoiceBatch().getIbName(), AD_BRNCH, companyCode);

                        } else {

                            glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES INVOICES", AD_BRNCH, companyCode);
                        }

                    } catch (FinderException ex) {
                    }

                    if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                        if (adPreference.getPrfEnableArInvoiceBatch() == EJBCommon.TRUE) {

                            glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arInvoice.getArInvoiceBatch().getIbName(), "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);

                        } else {

                            glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES INVOICES", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);
                        }
                    }

                    // create journal entry

                    LocalGlJournal glJournal = glJournalHome.create("IPS Number-" + arInvoicePaymentSchedule.getIpsNumber(), "AUTO GENERATE PENALTY", EJBCommon.convertStringToSQLDate(EJBCommon.convertSQLDateToString(new Date())), 0.0d, null, arInvoice.getInvNumber() + "-" + ctr, null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), arInvoice.getArCustomer().getCstTin(), arInvoice.getArCustomer().getCstName(), EJBCommon.FALSE, null, AD_BRNCH, companyCode);

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

                        double DR_AMNT = 0d;

                        if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                            DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);

                        } else {

                            DR_AMNT = this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);
                        }

                        LocalGlJournalLine glJournalLine = glJournalLineHome.create(arDistributionRecord.getDrLine(), arDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                        // arDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);
                        glJournalLine.setGlChartOfAccount(arDistributionRecord.getGlChartOfAccount());

                        // glJournal.addGlJournalLine(glJournalLine);
                        glJournalLine.setGlJournal(glJournal);
                        arDistributionRecord.setDrImported(EJBCommon.TRUE);

                        // for FOREX revaluation

                        LocalArInvoice arInvoiceTemp = arInvoice.getInvCreditMemo() == EJBCommon.FALSE ? arInvoice : arCreditedInvoice;

                        if ((!Objects.equals(arInvoiceTemp.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(arInvoiceTemp.getGlFunctionalCurrency().getFcCode()))) {

                            double CONVERSION_RATE = 1;

                            if (arInvoiceTemp.getInvConversionRate() != 0 && arInvoiceTemp.getInvConversionRate() != 1) {

                                CONVERSION_RATE = arInvoiceTemp.getInvConversionRate();

                            } else if (arInvoiceTemp.getInvConversionDate() != null) {

                                CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);
                            }

                            Collection glForexLedgers = null;

                            try {

                                glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(arInvoiceTemp.getInvDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                            } catch (FinderException ex) {

                            }

                            LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                            int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(arInvoiceTemp.getInvDate()) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                            // compute balance
                            double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                            double FRL_AMNT = arDistributionRecord.getDrAmount();

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                            } else {
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                            }

                            COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                            glForexLedger = glForexLedgerHome.create(arInvoiceTemp.getInvDate(), FRL_LN, "SI", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, 0d, companyCode);

                            // glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);
                            glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());

                            // propagate balances
                            try {

                                glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

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
            }

            Debug.print("pastDueInvoices.size()=" + pastDueInvoices.size());
            return pastDueInvoices.size();
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArSmlAll(Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArStandardMemoLineControllerBean getArSmlAll");
        LocalGlChartOfAccount glChartOfAccount = null;
        ArrayList list = new ArrayList();
        try {

            Collection arStandardMemoLines = arStandardMemoLineHome.findSmlAll(companyCode);

            if (arStandardMemoLines.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object standardMemoLine : arStandardMemoLines) {

                LocalArStandardMemoLine arStandardMemoLine = (LocalArStandardMemoLine) standardMemoLine;

                ArModStandardMemoLineDetails mdetails = new ArModStandardMemoLineDetails();
                mdetails.setSmlCode(arStandardMemoLine.getSmlCode());
                mdetails.setSmlType(arStandardMemoLine.getSmlType());
                mdetails.setSmlName(arStandardMemoLine.getSmlName());
                mdetails.setSmlDescription(arStandardMemoLine.getSmlDescription());
                mdetails.setSmlWordPressProductID(arStandardMemoLine.getSmlWordPressProductID());
                mdetails.setSmlUnitPrice(arStandardMemoLine.getSmlUnitPrice());
                mdetails.setSmlTax(arStandardMemoLine.getSmlTax());
                mdetails.setSmlEnable(arStandardMemoLine.getSmlEnable());
                mdetails.setSmlUnitOfMeasure(arStandardMemoLine.getSmlUnitOfMeasure());
                mdetails.setSmlSubjectToCommission(arStandardMemoLine.getSmlSubjectToCommission());

                if (arStandardMemoLine.getGlChartOfAccount() != null) {

                    mdetails.setSmlGlCoaAccountNumber(arStandardMemoLine.getGlChartOfAccount().getCoaAccountNumber());
                    mdetails.setSmlGlCoaAccountDescription(arStandardMemoLine.getGlChartOfAccount().getCoaAccountDescription());
                }

                if (arStandardMemoLine.getSmlGlCoaReceivableAccount() != null) {
                    try {

                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arStandardMemoLine.getSmlGlCoaReceivableAccount());

                    } catch (FinderException ex) {

                        throw new GlobalNoRecordFoundException();
                    }
                    mdetails.setSmlGlCoaReceivableAccountNumber(glChartOfAccount.getCoaAccountNumber());
                    mdetails.setSmlGlCoaReceivableAccountDescription(glChartOfAccount.getCoaAccountDescription());
                }

                if (arStandardMemoLine.getSmlGlCoaRevenueAccount() != null) {
                    try {

                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arStandardMemoLine.getSmlGlCoaRevenueAccount());

                    } catch (FinderException ex) {

                        throw new GlobalNoRecordFoundException();
                    }
                    mdetails.setSmlGlCoaRevenueAccountNumber(glChartOfAccount.getCoaAccountNumber());
                    mdetails.setSmlGlCoaRevenueAccountDescription(glChartOfAccount.getCoaAccountDescription());
                }

                if (arStandardMemoLine.getSmlInterimAccount() != null) {

                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arStandardMemoLine.getSmlInterimAccount());

                    mdetails.setSmlInterimAccountNumber(glChartOfAccount.getCoaAccountNumber());
                    mdetails.setSmlInterimAccountDescription(glChartOfAccount.getCoaAccountDescription());
                }

                list.add(mdetails);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrAll(Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdStandardMemoLineControllerBean getBrAll");
        LocalAdBranch adBranch = null;
        Collection adBranches = null;
        ArrayList list = new ArrayList();
        try {

            adBranches = adBranchHome.findBrAll(companyCode);

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranches.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }
        for (Object branch : adBranches) {

            adBranch = (LocalAdBranch) branch;

            AdBranchDetails details = new AdBranchDetails();

            details.setBrBranchCode(adBranch.getBrBranchCode());
            details.setBrCode(adBranch.getBrCode());
            details.setBrName(adBranch.getBrName());

            list.add(details);
        }
        return list;
    }

    public ArrayList getAdBrSMLAll(Integer BSML_CODE, String RS_NM, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ArStandardMemoLineControllerBean getAdBrSMLAll");
        LocalAdBranchStandardMemoLine adBranchStandardMemoLine = null;
        LocalAdBranch adBranch = null;
        Collection adBranchStandardMemoLines = null;
        ArrayList branchList = new ArrayList();

        try {

            adBranchStandardMemoLines = adBranchStandardMemoLineHome.findBSMLBySMLCodeAndRsName(BSML_CODE, RS_NM, companyCode);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchStandardMemoLines.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object branchStandardMemoLine : adBranchStandardMemoLines) {

                adBranchStandardMemoLine = (LocalAdBranchStandardMemoLine) branchStandardMemoLine;

                adBranch = adBranchHome.findByPrimaryKey(adBranchStandardMemoLine.getAdBranch().getBrCode());

                AdModBranchStandardMemoLineDetails mdetails = new AdModBranchStandardMemoLineDetails();
                mdetails.setBsmlBranchCode(adBranch.getBrCode());
                mdetails.setBsmlBranchName(adBranch.getBrName());

                LocalGlChartOfAccount glAccount = null;
                LocalGlChartOfAccount glInterimChartOfAccount = null;
                LocalGlChartOfAccount glReceivableChartOfAccount = null;
                LocalGlChartOfAccount glRevenueChartOfAccount = null;

                glAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlAccount());
                mdetails.setBsmlAccountNumber(glAccount.getCoaAccountNumber());
                mdetails.setBsmlAccountNumberDescription(glAccount.getCoaAccountDescription());

                glReceivableChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlCoaReceivableAccount());
                mdetails.setBsmlReceivableAccountNumber(glReceivableChartOfAccount.getCoaAccountNumber());
                mdetails.setBsmlReceivableAccountNumberDescription(glReceivableChartOfAccount.getCoaAccountDescription());

                if (adBranchStandardMemoLine.getBsmlGlCoaRevenueAccount() != null) {

                    glRevenueChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlCoaRevenueAccount());
                }
                mdetails.setBsmlRevenueAccountNumber(glRevenueChartOfAccount != null ? glRevenueChartOfAccount.getCoaAccountNumber() : null);
                mdetails.setBsmlRevenueAccountNumberDescription(glRevenueChartOfAccount != null ? glRevenueChartOfAccount.getCoaAccountDescription() : null);

                mdetails.setBsmlSubjectToCommission(adBranchStandardMemoLine.getBsmlSubjectToCommission());

                branchList.add(mdetails);
            }

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return branchList;
    }

    public AdResponsibilityDetails getAdRsByRsCode(Integer RS_CODE) throws GlobalNoRecordFoundException {

        Debug.print("ArStandardMemoLineControllerBean getAdRsByRsCode");
        LocalAdResponsibility adRes = null;
        try {
            adRes = adResHome.findByPrimaryKey(RS_CODE);
        } catch (FinderException ex) {
        }
        AdResponsibilityDetails details = new AdResponsibilityDetails();
        details.setRsName(adRes.getRsName());
        return details;
    }

    public void addArSmlEntry(ArModStandardMemoLineDetails mdetails, String SML_GL_COA_ACCNT_NMBR, String CST_GL_COA_RCVBL_ACCNT, String CST_GL_COA_RVNUE_ACCNT, String SML_INTRM_ACCNT_NMBR, ArrayList branchList, Integer companyCode) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException {

        Debug.print("ArStandardMemoLineControllerBean addArSml");
        LocalAdBranchStandardMemoLine adBranchStandardMemoLine = null;
        LocalAdBranch adBranch = null;
        try {

            LocalArStandardMemoLine arStandardMemoLine = null;
            LocalGlChartOfAccount glAccount = null;
            LocalGlChartOfAccount glInterimChartOfAccount = null;
            LocalGlChartOfAccount glReceivableChartOfAccount = null;
            LocalGlChartOfAccount glRevenueChartOfAccount = null;

            try {

                arStandardMemoLine = arStandardMemoLineHome.findBySmlName(mdetails.getSmlName(), companyCode);

                throw new GlobalRecordAlreadyExistException();

            } catch (FinderException ex) {

            }

            // get account to validate accounts

            try {

                glAccount = glChartOfAccountHome.findByCoaAccountNumber(SML_GL_COA_ACCNT_NMBR, companyCode);

            } catch (FinderException ex) {

                if (this.getArSmlGlCoaAccountNumberEnable(companyCode)) {

                    throw new GlobalAccountNumberInvalidException();
                }
            }

            // get glChartOfAccount to validate accounts

            try {

                if (SML_INTRM_ACCNT_NMBR != null && SML_INTRM_ACCNT_NMBR.length() > 0) {

                    glInterimChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(SML_INTRM_ACCNT_NMBR, companyCode);
                }

            } catch (FinderException ex) {

                throw new ArTCInterimAccountInvalidException();
            }

            try {

                glReceivableChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(CST_GL_COA_RCVBL_ACCNT, companyCode);

            } catch (FinderException ex) {

                throw new ArCCCoaGlReceivableAccountNotFoundException();
            }

            try {

                if (CST_GL_COA_RVNUE_ACCNT != null && CST_GL_COA_RVNUE_ACCNT.length() > 0) {

                    glRevenueChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(CST_GL_COA_RVNUE_ACCNT, companyCode);
                }

            } catch (FinderException ex) {

                throw new ArCCCoaGlRevenueAccountNotFoundException();
            }

            // create new standard memo line

            arStandardMemoLine = arStandardMemoLineHome.create(mdetails.getSmlType(), mdetails.getSmlName(), mdetails.getSmlDescription(), mdetails.getSmlWordPressProductID(), mdetails.getSmlUnitPrice(), mdetails.getSmlTax(), mdetails.getSmlEnable(), mdetails.getSmlSubjectToCommission(), mdetails.getSmlUnitOfMeasure(), glInterimChartOfAccount != null ? glInterimChartOfAccount.getCoaCode() : null, glReceivableChartOfAccount.getCoaCode(), glRevenueChartOfAccount != null ? glRevenueChartOfAccount.getCoaCode() : null, companyCode);

            if (glAccount != null) {

                glAccount.addArStandardMemoLine(arStandardMemoLine);
            }

            // create new Branch Standard Memo Line
            LocalGlChartOfAccount glChartofAccount = null;

            for (Object o : branchList) {

                AdModBranchStandardMemoLineDetails adBrSmlDetails = (AdModBranchStandardMemoLineDetails) o;

                try {

                    if (EJBCommon.validateRequired(adBrSmlDetails.getBsmlAccountNumber())) {
                        glChartofAccount = glAccount;
                    } else {
                        glChartofAccount = glChartOfAccountHome.findByCoaAccountNumber(adBrSmlDetails.getBsmlAccountNumber(), companyCode);
                    }

                } catch (FinderException ex) {

                    if (this.getArSmlGlCoaAccountNumberEnable(companyCode)) {
                        throw new GlobalAccountNumberInvalidException();
                    }
                }

                try {

                    if (!EJBCommon.validateRequired(adBrSmlDetails.getBsmlReceivableAccountNumber())) {
                        glReceivableChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(adBrSmlDetails.getBsmlReceivableAccountNumber(), companyCode);
                    }

                } catch (FinderException ex) {

                    throw new ArCCCoaGlReceivableAccountNotFoundException();
                }

                if (adBrSmlDetails.getBsmlRevenueAccountNumber() != null && adBrSmlDetails.getBsmlRevenueAccountNumber().length() > 0) {

                    try {
                        if (!EJBCommon.validateRequired(adBrSmlDetails.getBsmlRevenueAccountNumber())) {
                            glRevenueChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(adBrSmlDetails.getBsmlRevenueAccountNumber(), companyCode);
                        }
                    } catch (FinderException ex) {

                        throw new ArCCCoaGlRevenueAccountNotFoundException();
                    }
                }

                adBranchStandardMemoLine = adBranchStandardMemoLineHome.create(glChartofAccount.getCoaCode(), glReceivableChartOfAccount.getCoaCode(), glRevenueChartOfAccount.getCoaCode(), adBrSmlDetails.getBsmlSubjectToCommission(), 'N', companyCode);
                arStandardMemoLine.addAdBranchStandardMemoLine(adBranchStandardMemoLine);
                adBranch = adBranchHome.findByPrimaryKey(adBrSmlDetails.getBsmlBranchCode());
                adBranch.addAdBranchStandardMemoLine(adBranchStandardMemoLine);
            }

        } catch (GlobalRecordAlreadyExistException | GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateArSmlEntry(ArModStandardMemoLineDetails mdetails, String SML_GL_COA_ACCNT_NMBR, String SML_GL_COA_RCVBL_ACCNT_NMBR, String SML_GL_COA_RVN_ACCNT_NMBR, String SML_INTRM_ACCNT_NMBR, String RS_NM, ArrayList branchList, Integer companyCode) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException {

        Debug.print("ArStandardMemoLineControllerBean updateArSml");
        LocalAdBranchStandardMemoLine adBranchStandardMemoLine = null;
        LocalAdBranch adBranch = null;
        try {

            LocalArStandardMemoLine arStandardMemoLine = null;
            LocalGlChartOfAccount glAccount = null;
            LocalGlChartOfAccount glInterimChartOfAccount = null;
            LocalGlChartOfAccount glReceivableChartOfAccount = null;
            LocalGlChartOfAccount glRevenueChartOfAccount = null;

            try {

                LocalArStandardMemoLine arExistingStandardMemoLine = arStandardMemoLineHome.findBySmlName(mdetails.getSmlName(), companyCode);

                if (!arExistingStandardMemoLine.getSmlCode().equals(mdetails.getSmlCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (FinderException ex) {

            }

            // get account to validate accounts

            try {

                glAccount = glChartOfAccountHome.findByCoaAccountNumber(SML_GL_COA_ACCNT_NMBR, companyCode);

            } catch (FinderException ex) {

                if (this.getArSmlGlCoaAccountNumberEnable(companyCode)) {

                    throw new GlobalAccountNumberInvalidException();
                }
            }

            // get glChartOfAccount to validate accounts

            try {

                if (SML_INTRM_ACCNT_NMBR != null && SML_INTRM_ACCNT_NMBR.length() > 0) {

                    glInterimChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(SML_INTRM_ACCNT_NMBR, companyCode);
                }

            } catch (FinderException ex) {

                throw new ArTCInterimAccountInvalidException();
            }

            // get glChartOfAccount to validate accounts

            try {

                if (SML_GL_COA_RCVBL_ACCNT_NMBR != null && SML_GL_COA_RCVBL_ACCNT_NMBR.length() > 0) {

                    glReceivableChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(SML_GL_COA_RCVBL_ACCNT_NMBR, companyCode);
                }

            } catch (FinderException ex) {

                throw new ArCCCoaGlReceivableAccountNotFoundException();
            }

            // get glChartOfAccount to validate accounts

            try {

                if (SML_GL_COA_RVN_ACCNT_NMBR != null && SML_GL_COA_RVN_ACCNT_NMBR.length() > 0) {

                    glRevenueChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(SML_GL_COA_RVN_ACCNT_NMBR, companyCode);
                }

            } catch (FinderException ex) {

                throw new ArCCCoaGlRevenueAccountNotFoundException();
            }

            // find and update standard memo line

            arStandardMemoLine = arStandardMemoLineHome.findByPrimaryKey(mdetails.getSmlCode());
            Debug.print("--c-- " + mdetails.getSmlWordPressProductID());
            arStandardMemoLine.setSmlType(mdetails.getSmlType());
            arStandardMemoLine.setSmlName(mdetails.getSmlName());
            arStandardMemoLine.setSmlDescription(mdetails.getSmlDescription());
            arStandardMemoLine.setSmlWordPressProductID(mdetails.getSmlWordPressProductID());
            arStandardMemoLine.setSmlUnitPrice(mdetails.getSmlUnitPrice());
            arStandardMemoLine.setSmlTax(mdetails.getSmlTax());
            arStandardMemoLine.setSmlEnable(mdetails.getSmlEnable());
            arStandardMemoLine.setSmlUnitOfMeasure(mdetails.getSmlUnitOfMeasure());
            arStandardMemoLine.setSmlSubjectToCommission(mdetails.getSmlSubjectToCommission());
            arStandardMemoLine.setSmlInterimAccount(glInterimChartOfAccount != null ? glInterimChartOfAccount.getCoaCode() : null);

            arStandardMemoLine.setSmlGlCoaReceivableAccount(glReceivableChartOfAccount != null ? glReceivableChartOfAccount.getCoaCode() : null);
            arStandardMemoLine.setSmlGlCoaRevenueAccount(glRevenueChartOfAccount != null ? glRevenueChartOfAccount.getCoaCode() : null);

            Debug.print("--c-- 2" + arStandardMemoLine.getSmlWordPressProductID());

            if (glAccount != null) {

                glAccount.addArStandardMemoLine(arStandardMemoLine);
            }

            Collection adBranchStandardMemoLines = adBranchStandardMemoLineHome.findBSMLBySMLCodeAndRsName(arStandardMemoLine.getSmlCode(), RS_NM, companyCode);

            // remove all adBranchStandardMemoLine lines
            for (Object branchStandardMemoLine : adBranchStandardMemoLines) {

                adBranchStandardMemoLine = (LocalAdBranchStandardMemoLine) branchStandardMemoLine;

                arStandardMemoLine.dropAdBranchStandardMemoLine(adBranchStandardMemoLine);

                adBranch = adBranchHome.findByPrimaryKey(adBranchStandardMemoLine.getAdBranch().getBrCode());
                adBranch.dropAdBranchStandardMemoLine(adBranchStandardMemoLine);
                // adBranchStandardMemoLine.entityRemove();
                em.remove(adBranchStandardMemoLine);
            }

            // create new Branch Standard Memo Line
            LocalGlChartOfAccount glCoa = null;

            for (Object o : branchList) {

                AdModBranchStandardMemoLineDetails adBrSmlDetails = (AdModBranchStandardMemoLineDetails) o;

                try {
                    glCoa = glChartOfAccountHome.findByCoaAccountNumber(adBrSmlDetails.getBsmlAccountNumber(), companyCode);
                } catch (FinderException ex) {
                    if (this.getArSmlGlCoaAccountNumberEnable(companyCode)) {
                        throw new GlobalAccountNumberInvalidException();
                    }
                }

                try {

                    Debug.print("adBrSmlDetails.getBsmlReceivableAccountNumber()=" + adBrSmlDetails.getBsmlReceivableAccountNumber());

                    glReceivableChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(adBrSmlDetails.getBsmlReceivableAccountNumber(), companyCode);

                } catch (FinderException ex) {

                    throw new ArCCCoaGlReceivableAccountNotFoundException();
                }

                try {
                    Debug.print("adBrSmlDetails.getBsmlRevenueAccountNumber()=" + adBrSmlDetails.getBsmlRevenueAccountNumber());

                    glRevenueChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(adBrSmlDetails.getBsmlRevenueAccountNumber(), companyCode);

                } catch (FinderException ex) {

                    throw new ArCCCoaGlRevenueAccountNotFoundException();
                }

                adBranchStandardMemoLine = adBranchStandardMemoLineHome.create(glCoa.getCoaCode(), glReceivableChartOfAccount.getCoaCode(), glRevenueChartOfAccount.getCoaCode(), adBrSmlDetails.getBsmlSubjectToCommission(), adBrSmlDetails.getBsmlStandardMemoLineDownloadStatus(), companyCode);

                arStandardMemoLine.addAdBranchStandardMemoLine(adBranchStandardMemoLine);
                adBranch = adBranchHome.findByPrimaryKey(adBrSmlDetails.getBsmlBranchCode());
                adBranch.addAdBranchStandardMemoLine(adBranchStandardMemoLine);
            }

        } catch (GlobalRecordAlreadyExistException | GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteArSmlEntry(Integer SML_CODE, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("ArStandardMemoLineControllerBean deleteArSml");
        try {

            LocalArStandardMemoLine arStandardMemoLine = null;

            try {

                arStandardMemoLine = arStandardMemoLineHome.findByPrimaryKey(SML_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            if (!arStandardMemoLine.getArInvoiceLines().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

            // arStandardMemoLine.entityRemove();
            em.remove(arStandardMemoLine);

        } catch (GlobalRecordAlreadyDeletedException | GlobalRecordAlreadyAssignedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("ArStandardMemoLineControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public boolean getArSmlGlCoaAccountNumberEnable(Integer companyCode) {

        Debug.print("ArStandardMemoLineControllerBean getArSmlGlCoaAccountNumberEnable");
        try {

            Collection arAutoAccountingSegments = arAutoAccountingSegmentHome.findByAasClassTypeAndAaAccountType("AR STANDARD MEMO LINE", "REVENUE", companyCode);

            return !arAutoAccountingSegments.isEmpty();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer companyCode) {

        Debug.print("ArStandardMemoLineControllerBean getInvGpQuantityPrecisionUnit");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("ArStandardMemoLineControllerBean getFrRateByFrNameAndFrDate");
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

    public String saveArInvEntry(ArInvoiceDetails details, String PYT_NM, String TC_NM, String WTC_NM, String FC_NM, String CST_CSTMR_CODE, String IB_NM, ArrayList ilList, boolean isDraft, String SLP_SLSPRSN_CODE, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, ArINVAmountExceedsCreditLimitException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ArStandardMemoLineControllerBean saveArInvEntry");
        LocalArInvoice arInvoice = null;
        try {

            // validate if invoice is already deleted

            try {

                if (details.getInvCode() != null) {

                    arInvoice = arInvoiceHome.findByPrimaryKey(details.getInvCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if invoice is already posted, void, approved or pending

            if (details.getInvCode() != null) {

                if (arInvoice.getInvApprovalStatus() != null) {

                    if (arInvoice.getInvApprovalStatus().equals("APPROVED") || arInvoice.getInvApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (arInvoice.getInvApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (arInvoice.getInvPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (arInvoice.getInvVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // invoice void

            if (details.getInvCode() != null && details.getInvVoid() == EJBCommon.TRUE && arInvoice.getInvPosted() == EJBCommon.FALSE) {

                arInvoice.setInvVoid(EJBCommon.TRUE);
                arInvoice.setInvLastModifiedBy(details.getInvLastModifiedBy());
                arInvoice.setInvDateLastModified(details.getInvDateLastModified());

                return arInvoice.getInvNumber();
            }

            // validate if document number is unique document number is automatic then set
            // next sequence

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
            Debug.print("ArInvoiceEntryControllerBean saveArInvEntry A");
            if (details.getInvCode() == null) {

                try {

                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR INVOICE", companyCode);

                } catch (FinderException ex) {

                }

                try {
                    Debug.print("adDocumentSequenceAssignment.getDsaCode()= " + adDocumentSequenceAssignment.getDsaCode());
                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

                } catch (FinderException ex) {

                }

                Debug.print("trace 1");
                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getInvNumber() == null || details.getInvNumber().trim().length() == 0)) {

                    while (true) {
                        Debug.print("trace  loop 1");
                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                            try {
                                Debug.print("trace  loop 1 a");
                                arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), EJBCommon.FALSE, AD_BRNCH, companyCode);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                            } catch (FinderException ex) {
                                Debug.print("trace  loop 1 b");
                                details.setInvNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {
                                Debug.print("trace  loop 1 c");
                                arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), EJBCommon.FALSE, AD_BRNCH, companyCode);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                            } catch (FinderException ex) {
                                Debug.print("trace  loop 1 d");
                                details.setInvNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }

            } else {
                Debug.print("trace  loop 2");
                LocalArInvoice arExistingInvoice = null;

                try {

                    arExistingInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(details.getInvNumber(), EJBCommon.FALSE, AD_BRNCH, companyCode);

                } catch (FinderException ex) {
                }

                if (arExistingInvoice != null && !arExistingInvoice.getInvCode().equals(details.getInvCode())) {

                    throw new GlobalDocumentNumberNotUniqueException();
                }

                if (arInvoice.getInvNumber() != details.getInvNumber() && (details.getInvNumber() == null || details.getInvNumber().trim().length() == 0)) {

                    details.setInvNumber(arInvoice.getInvNumber());
                }
            }
            Debug.print("ArInvoiceEntryControllerBean saveArInvEntry A Done");

            // validate if conversion date exists

            try {

                if (details.getInvConversionDate() != null) {

                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
                    LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

                    if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getInvConversionDate(), companyCode);

                    } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getInvConversionDate(), companyCode);
                    }
                }

            } catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // validate if payment term has at least one payment schedule

            if (adPaymentTermHome.findByPytName(PYT_NM, companyCode).getAdPaymentSchedules().isEmpty()) {

                throw new GlobalPaymentTermInvalidException();
            }

            // used in checking if invoice should re-generate distribution records and
            // re-calculate taxes
            boolean isRecalculate = true;

            // create invoice
            Debug.print("ArInvoiceEntryControllerBean saveArInvEntry B");
            if (details.getInvCode() == null) {
                Debug.print("CHECKING A");

                arInvoice = arInvoiceHome.create(details.getInvType(), EJBCommon.FALSE, details.getInvDescription(), details.getInvDate(), details.getInvNumber(), details.getInvReferenceNumber(), details.getInvUploadNumber(), null, null, 0d, 0d, 0d, 0d, 0d, 0d, details.getInvConversionDate(), details.getInvConversionRate(), details.getInvMemo(), details.getInvPreviousReading(), details.getInvPresentReading(), details.getInvBillToAddress(), details.getInvBillToContact(), details.getInvBillToAltContact(), details.getInvBillToPhone(), details.getInvBillingHeader(), details.getInvBillingFooter(), details.getInvBillingHeader2(), details.getInvBillingFooter2(), details.getInvBillingHeader3(), details.getInvBillingFooter3(), details.getInvBillingSignatory(), details.getInvSignatoryTitle(), details.getInvShipToAddress(), details.getInvShipToContact(), details.getInvShipToAltContact(), details.getInvShipToPhone(), details.getInvShipDate(), details.getInvLvFreight(), null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, details.getInvDisableInterest(), details.getInvInterest(), details.getInvInterestReferenceNumber(), details.getInvInterestAmount(), details.getInvInterestCreatedBy(), details.getInvInterestDateCreated(), null, null, details.getInvCreatedBy(), details.getInvDateCreated(), details.getInvLastModifiedBy(), details.getInvDateLastModified(), null, null, null, null, EJBCommon.FALSE, null, null, null, details.getInvDebitMemo(), details.getInvSubjectToCommission(), null, details.getInvEffectivityDate(), AD_BRNCH, companyCode);

            } else {

                // check if critical fields are changed

                if (!arInvoice.getArTaxCode().getTcName().equals(TC_NM) || !arInvoice.getArWithholdingTaxCode().getWtcName().equals(WTC_NM) || !arInvoice.getArCustomer().getCstCustomerCode().equals(CST_CSTMR_CODE) || !arInvoice.getAdPaymentTerm().getPytName().equals(PYT_NM) || !arInvoice.getInvDate().equals(details.getInvDate()) || !arInvoice.getInvEffectivityDate().equals(details.getInvEffectivityDate()) || ilList.size() != arInvoice.getArInvoiceLines().size()) {

                    isRecalculate = true;

                } else if (ilList.size() == arInvoice.getArInvoiceLines().size()) {
                    Debug.print("CHECK");
                    Iterator ilIter = arInvoice.getArInvoiceLines().iterator();
                    Iterator ilListIter = ilList.iterator();

                    while (ilIter.hasNext()) {

                        LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) ilIter.next();
                        ArModInvoiceLineDetails mdetails = (ArModInvoiceLineDetails) ilListIter.next();
                        if (!arInvoiceLine.getArStandardMemoLine().getSmlName().equals(mdetails.getIlSmlName())) {
                            Debug.print("SML");
                        }

                        if (arInvoiceLine.getIlQuantity() != mdetails.getIlQuantity()) {
                            Debug.print("QTY");
                        }

                        if (arInvoiceLine.getIlUnitPrice() != mdetails.getIlUnitPrice()) {
                            Debug.print("UPRICE");
                        }

                        if (arInvoiceLine.getIlTax() != mdetails.getIlTax()) {
                            Debug.print("TAX");
                        }

                        if (!arInvoiceLine.getArStandardMemoLine().getSmlName().equals(mdetails.getIlSmlName()) || arInvoiceLine.getIlQuantity() != mdetails.getIlQuantity() || arInvoiceLine.getIlUnitPrice() != mdetails.getIlUnitPrice() || arInvoiceLine.getIlTax() != mdetails.getIlTax()) {
                            Debug.print("NO!");
                            isRecalculate = true;
                            break;
                        }

                        arInvoiceLine.setIlDescription(mdetails.getIlDescription());

                        isRecalculate = false;
                    }

                } else {

                    isRecalculate = false;
                }
                Debug.print("ArInvoiceEntryControllerBean saveArInvEntry B Done");

                arInvoice.setInvDescription(details.getInvDescription());
                arInvoice.setInvDate(details.getInvDate());
                arInvoice.setInvNumber(details.getInvNumber());
                arInvoice.setInvReferenceNumber(details.getInvReferenceNumber());
                arInvoice.setInvMemo(details.getInvMemo());
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
            }

            Debug.print("ArInvoiceEntryControllerBean saveArInvEntry C");

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, companyCode);
            // adPaymentTerm.addArInvoice(arInvoice);
            arInvoice.setAdPaymentTerm(adPaymentTerm);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
            // glFunctionalCurrency.addArInvoice(arInvoice);
            arInvoice.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(TC_NM, companyCode);
            // arTaxCode.addArInvoice(arInvoice);
            arInvoice.setArTaxCode(arTaxCode);

            Debug.print("WTC_NM: " + WTC_NM);
            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(WTC_NM, companyCode);
            // arWithholdingTaxCode.addArInvoice(arInvoice);
            arInvoice.setArWithholdingTaxCode(arWithholdingTaxCode);

            Debug.print("CST_CSTMR_CODE: " + CST_CSTMR_CODE);
            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, companyCode);
            // arCustomer.addArInvoice(arInvoice);
            arInvoice.setArCustomer(arCustomer);

            LocalArSalesperson arSalesperson = null;

            if (SLP_SLSPRSN_CODE != null && SLP_SLSPRSN_CODE.length() > 0 && !SLP_SLSPRSN_CODE.equalsIgnoreCase("NO RECORD FOUND")) {

                // if he tagged a salesperson for this invoice
                arSalesperson = arSalespersonHome.findBySlpSalespersonCode(SLP_SLSPRSN_CODE, companyCode);

                // arSalesperson.addArInvoice(arInvoice);
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

            try {

                LocalArInvoiceBatch arInvoiceBatch = arInvoiceBatchHome.findByIbName(IB_NM, AD_BRNCH, companyCode);
                // arInvoiceBatch.addArInvoice(arInvoice);
                arInvoice.setArInvoiceBatch(arInvoiceBatch);

            } catch (FinderException ex) {

            }
            Debug.print("ArInvoiceEntryControllerBean saveArInvEntry C Done");
            double amountDue = 0;

            Debug.print("ArInvoiceEntryControllerBean saveArInvEntry D");
            if (isRecalculate) {

                // remove all invoice line items

                Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();

                Iterator i = arInvoiceLineItems.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();

                    double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                    arInvoiceLineItem.getInvItemLocation().setIlCommittedQuantity(arInvoiceLineItem.getInvItemLocation().getIlCommittedQuantity() - convertedQuantity);

                    i.remove();

                    // arInvoiceLineItem.entityRemove();
                    em.remove(arInvoiceLineItem);
                }

                // remove all invoice lines

                Collection arInvoiceLines = arInvoice.getArInvoiceLines();

                i = arInvoiceLines.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) i.next();

                    i.remove();

                    // arInvoiceLine.entityRemove();
                    em.remove(arInvoiceLine);
                }

                // remove all sales order lines

                Collection arSalesOrderInvoiceLines = arInvoice.getArSalesOrderInvoiceLines();

                i = arSalesOrderInvoiceLines.iterator();

                while (i.hasNext()) {

                    LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) i.next();

                    i.remove();

                    // arSalesOrderInvoiceLine.entityRemove();
                    em.remove(arSalesOrderInvoiceLine);
                }

                // remove all distribution records

                Collection arDistributionRecords = arInvoice.getArDistributionRecords();

                i = arDistributionRecords.iterator();

                while (i.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                    i.remove();

                    // arDistributionRecord.entityRemove();
                    em.remove(arDistributionRecord);
                }

                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

                // add new invoice lines and distribution record

                double TOTAL_TAX = 0d;
                double TOTAL_LINE = 0d;
                double TOTAL_UNTAXABLE = 0d;

                i = ilList.iterator();

                while (i.hasNext()) {

                    ArModInvoiceLineDetails mInvDetails = (ArModInvoiceLineDetails) i.next();

                    LocalArInvoiceLine arInvoiceLine = this.addArIlEntry(mInvDetails, arInvoice, companyCode);

                    // add receivable/debit distributions
                    double LINE = arInvoiceLine.getIlAmount();
                    double TAX = arInvoiceLine.getIlTaxAmount();
                    double UNTAXABLE = 0d;
                    double W_TAX = 0d;
                    double DISCOUNT = 0d;

                    if (arInvoiceLine.getIlTax() == EJBCommon.FALSE) {
                        UNTAXABLE += arInvoiceLine.getIlAmount();
                    }

                    // compute w-tax per line

                    if (arWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals("INVOICE")) {

                        W_TAX = EJBCommon.roundIt((LINE - UNTAXABLE) * (arWithholdingTaxCode.getWtcRate() / 100), this.getGlFcPrecisionUnit(companyCode));
                    }

                    if (adPaymentTerm.getPytDiscountOnInvoice() == EJBCommon.TRUE) {

                        Collection adPaymentSchedules = adPaymentScheduleHome.findByPytCode(adPaymentTerm.getPytCode(), companyCode);
                        ArrayList adPaymentScheduleList = new ArrayList(adPaymentSchedules);
                        LocalAdPaymentSchedule adPaymentSchedule = (LocalAdPaymentSchedule) adPaymentScheduleList.get(0);

                        Collection adDiscounts = adDiscountHome.findByPsCode(adPaymentSchedule.getPsCode(), companyCode);
                        ArrayList adDiscountList = new ArrayList(adDiscounts);
                        LocalAdDiscount adDiscount = (LocalAdDiscount) adDiscountList.get(0);

                        double rate = adDiscount.getDscDiscountPercent();
                        DISCOUNT = (LINE + TAX) * (rate / 100d);
                    }

                    // add revenue/credit distributions

                    // Check If Memo Line Type is Service Charge

                    if (arInvoiceLine.getArStandardMemoLine().getSmlType().equals("SC")) {

                        if (arInvoiceLine.getArStandardMemoLine().getSmlInterimAccount() == null) {
                            throw new GlobalBranchAccountNumberInvalidException();
                        }

                        this.addArDrEntry(arInvoice.getArDrNextLine(), "SC", EJBCommon.FALSE, arInvoiceLine.getIlAmount(), arInvoiceLine.getArStandardMemoLine().getSmlInterimAccount(), this.getArGlCoaRevenueAccount(arInvoiceLine, AD_BRNCH, companyCode), arInvoice, AD_BRNCH, companyCode);
                    } else {

                        this.addArDrEntry(arInvoice.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arInvoiceLine.getIlAmount(), this.getArGlCoaRevenueAccount(arInvoiceLine, AD_BRNCH, companyCode), null, arInvoice, AD_BRNCH, companyCode);

                        this.addArDrEntry(arInvoice.getArDrNextLine(), "RECEIVABLE", EJBCommon.TRUE, LINE + TAX - W_TAX - DISCOUNT, this.getArGlCoaReceivableAccount(arInvoiceLine, AD_BRNCH, companyCode), null, arInvoice, AD_BRNCH, companyCode);
                    }

                    TOTAL_LINE += arInvoiceLine.getIlAmount();
                    TOTAL_TAX += arInvoiceLine.getIlTaxAmount();

                    if (arInvoiceLine.getIlTax() == EJBCommon.FALSE) {
                        TOTAL_UNTAXABLE += arInvoiceLine.getIlAmount();
                    }
                }

                // add tax distribution if necessary

                if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {

                    if (arTaxCode.getTcInterimAccount() == null) {

                        this.addArDrEntry(arInvoice.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, arTaxCode.getGlChartOfAccount().getCoaCode(), null, arInvoice, AD_BRNCH, companyCode);

                    } else {

                        this.addArDrEntry(arInvoice.getArDrNextLine(), "DEFERRED TAX", EJBCommon.FALSE, TOTAL_TAX, arTaxCode.getTcInterimAccount(), null, arInvoice, AD_BRNCH, companyCode);
                    }
                }

                // add wtax distribution if necessary

                adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

                double W_TAX_AMOUNT = 0d;

                if (arWithholdingTaxCode.getWtcRate() != 0 && adPreference.getPrfArWTaxRealization().equals("INVOICE")) {

                    W_TAX_AMOUNT = EJBCommon.roundIt((TOTAL_LINE - TOTAL_UNTAXABLE) * (arWithholdingTaxCode.getWtcRate() / 100), this.getGlFcPrecisionUnit(companyCode));

                    this.addArDrEntry(arInvoice.getArDrNextLine(), "W-TAX", EJBCommon.TRUE, W_TAX_AMOUNT, arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), null, arInvoice, AD_BRNCH, companyCode);
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

                    this.addArDrIliEntry(arInvoice.getArDrNextLine(), "DISCOUNT", EJBCommon.TRUE, DISCOUNT_AMT, adPaymentTerm.getGlChartOfAccount().getCoaCode(), arInvoice, AD_BRNCH, companyCode);
                }

                // add receivable distribution

                // compute invoice amount due

                amountDue = TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT - DISCOUNT_AMT;

                // set invoice amount due

                arInvoice.setInvAmountDue(amountDue);

                // remove all payment schedule

                Collection arInvoicePaymentSchedules = arInvoice.getArInvoicePaymentSchedules();

                i = arInvoicePaymentSchedules.iterator();

                while (i.hasNext()) {

                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) i.next();

                    i.remove();

                    // arInvoicePaymentSchedule.entityRemove();
                    em.remove(arInvoicePaymentSchedule);
                }

                // create invoice payment schedule

                short precisionUnit = this.getGlFcPrecisionUnit(companyCode);
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

                    // create a payment schedule

                    double PAYMENT_SCHEDULE_AMOUNT = 0;

                    // if last payment schedule subtract to avoid rounding difference error

                    if (i.hasNext()) {

                        PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt((adPaymentSchedule.getPsRelativeAmount() / adPaymentTerm.getPytBaseAmount()) * arInvoice.getInvAmountDue(), precisionUnit);

                    } else {

                        PAYMENT_SCHEDULE_AMOUNT = arInvoice.getInvAmountDue() - TOTAL_PAYMENT_SCHEDULE;
                    }

                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = arInvoicePaymentScheduleHome.create(gcDateDue.getTime(), adPaymentSchedule.getPsLineNumber(), PAYMENT_SCHEDULE_AMOUNT, 0d, EJBCommon.FALSE, (short) 0, gcDateDue.getTime(), 0d, 0d, companyCode);

                    // arInvoice.addArInvoicePaymentSchedule(arInvoicePaymentSchedule);

                    arInvoicePaymentSchedule.setArInvoice(arInvoice);
                    TOTAL_PAYMENT_SCHEDULE += PAYMENT_SCHEDULE_AMOUNT;
                }
            }

            Debug.print("ArInvoiceEntryControllerBean saveArInvEntry D Done");
            // generate approval status

            String INV_APPRVL_STATUS = null;

            Debug.print("ArInvoiceEntryControllerBean saveArInvEntry E");
            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // validate if amount due + unposted invoices' amount + current balance +
                // unposted receipts'
                // amount
                // does not exceed customer's credit limit

                double balance = 0;
                LocalAdApprovalDocument adInvoiceApprovalDocument = adApprovalDocumentHome.findByAdcType("AR INVOICE", companyCode);

                if (arCustomer.getCstCreditLimit() > 0) {

                    balance = computeTotalBalance(details.getInvCode(), CST_CSTMR_CODE, companyCode);

                    balance += amountDue;

                    if (arCustomer.getCstCreditLimit() < balance && (adApproval.getAprEnableArInvoice() == EJBCommon.FALSE || (adApproval.getAprEnableArInvoice() == EJBCommon.TRUE && adInvoiceApprovalDocument.getAdcEnableCreditLimitChecking() == EJBCommon.FALSE))) {

                        throw new ArINVAmountExceedsCreditLimitException();
                    }
                }

                // find overdue invoices
                Collection arOverdueInvoices = arInvoicePaymentScheduleHome.findOverdueIpsByInvDateAndCstCustomerCode(arInvoice.getInvDate(), CST_CSTMR_CODE, companyCode);

                // check if ar invoice approval is enabled

                if (adApproval.getAprEnableArInvoice() == EJBCommon.FALSE || (arCustomer.getCstCreditLimit() > balance && arOverdueInvoices.size() == 0)) {

                    INV_APPRVL_STATUS = "N/A";

                } else {

                    // check if invoice is self approved

                    LocalAdAmountLimit adAmountLimit = null;

                    try {

                        adAmountLimit = adAmountLimitHome.findByAdcTypeAndAuTypeAndUsrName("AR INVOICE", "REQUESTER", details.getInvLastModifiedBy(), companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalNoApprovalRequesterFoundException();
                    }

                    if (arInvoice.getInvAmountDue() < adAmountLimit.getCalAmountLimit() && (arCustomer.getCstCreditLimit() == 0 || arCustomer.getCstCreditLimit() > balance)) {

                        INV_APPRVL_STATUS = "N/A";

                    } else {

                        // for approval, create approval queue

                        Collection adAmountLimits = adAmountLimitHome.findByAdcTypeAndGreaterThanCalAmountLimit("AR INVOICE", adAmountLimit.getCalAmountLimit(), companyCode);

                        if (adAmountLimits.isEmpty()) {

                            Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                            if (adApprovalUsers.isEmpty()) {

                                throw new GlobalNoApprovalApproverFoundException();
                            }

                            for (Object approvalUser : adApprovalUsers) {

                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, companyCode, EJBCommon.TRUE, adApprovalQueueHome, arInvoice.getInvCode(), arInvoice.getInvNumber(), arInvoice.getInvDate(), adAmountLimit, adApprovalUser);

                                adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                            }

                        } else {

                            boolean isApprovalUsersFound = false;

                            Iterator i = adAmountLimits.iterator();

                            while (i.hasNext()) {

                                LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();

                                if (arInvoice.getInvAmountDue() < adNextAmountLimit.getCalAmountLimit()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, companyCode, EJBCommon.TRUE, adApprovalQueueHome, arInvoice.getInvCode(), arInvoice.getInvNumber(), arInvoice.getInvDate(), adAmountLimit, adApprovalUser);

                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                    }

                                    break;

                                } else if (!i.hasNext()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adNextAmountLimit.getCalCode(), companyCode);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, companyCode, EJBCommon.TRUE, adApprovalQueueHome, arInvoice.getInvCode(), arInvoice.getInvNumber(), arInvoice.getInvDate(), adAmountLimit, adApprovalUser);

                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                    }

                                    break;
                                }

                                adAmountLimit = adNextAmountLimit;
                            }

                            if (!isApprovalUsersFound) {

                                throw new GlobalNoApprovalApproverFoundException();
                            }
                        }

                        INV_APPRVL_STATUS = "PENDING";
                    }
                }
            }

            Debug.print("ArInvoiceEntryControllerBean saveArInvEntry E Done");
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (INV_APPRVL_STATUS != null && INV_APPRVL_STATUS.equals("N/A") && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeArInvPost(arInvoice.getInvCode(), arInvoice.getInvLastModifiedBy(), AD_BRNCH, companyCode);
            }

            // set invoice approval status

            arInvoice.setInvApprovalStatus(INV_APPRVL_STATUS);
            Debug.print("arInvoice.getInvCode()=" + arInvoice.getInvCode());
            Debug.print("arInvoice.getInvNumber()=" + arInvoice.getInvNumber());
            return arInvoice.getInvNumber();

        } catch (GlobalRecordAlreadyDeletedException | GlobalBranchAccountNumberInvalidException |
                 GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
                 GlJREffectiveDateNoPeriodExistException | GlobalNoApprovalApproverFoundException |
                 GlobalNoApprovalRequesterFoundException | GlobalTransactionAlreadyVoidException |
                 GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
                 GlobalTransactionAlreadyApprovedException | ArINVAmountExceedsCreditLimitException |
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

    // private methods
    private LocalArInvoiceLine addArIlEntry(ArModInvoiceLineDetails mdetails, LocalArInvoice arInvoice, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean addArIlEntry");
        try {

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode);

            double IL_AMNT = 0d;
            double IL_TAX_AMNT = 0d;

            if (mdetails.getIlTax() == EJBCommon.TRUE) {

                LocalArTaxCode arTaxCode = arInvoice.getArTaxCode();

                // calculate net amount
                IL_AMNT = this.calculateIlNetAmount(mdetails, arTaxCode.getTcRate(), arTaxCode.getTcType(), precisionUnit);

                // calculate tax
                IL_TAX_AMNT = this.calculateIlTaxAmount(mdetails, arTaxCode.getTcRate(), arTaxCode.getTcType(), IL_AMNT, precisionUnit);

            } else {

                IL_AMNT = mdetails.getIlAmount();
            }

            LocalArInvoiceLine arInvoiceLine = arInvoiceLineHome.create(mdetails.getIlDescription(), mdetails.getIlQuantity(), mdetails.getIlUnitPrice(), IL_AMNT, IL_TAX_AMNT, mdetails.getIlTax(), companyCode);

            // arInvoice.addArInvoiceLine(arInvoiceLine);
            arInvoiceLine.setArInvoice(arInvoice);

            LocalArStandardMemoLine arStandardMemoLine = arStandardMemoLineHome.findBySmlName(mdetails.getIlSmlName(), companyCode);
            // arStandardMemoLine.addArInvoiceLine(arInvoiceLine);
            arInvoiceLine.setArStandardMemoLine(arStandardMemoLine);

            return arInvoiceLine;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double calculateIlNetAmount(ArModInvoiceLineDetails mdetails, double tcRate, String tcType, short precisionUnit) {

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

        double taxAmount = 0d;

        if (!tcType.equals("NONE") && !tcType.equals("EXEMPT")) {

            if (tcType.equals("INCLUSIVE")) {

                taxAmount = EJBCommon.roundIt(mdetails.getIlAmount() - amount, precisionUnit);

            } else if (tcType.equals("EXCLUSIVE")) {

                taxAmount = EJBCommon.roundIt(mdetails.getIlAmount() * tcRate / 100, precisionUnit);

            } else {

                // tax none zero-rated or exempt

            }
        }

        return taxAmount;
    }

    private void addArDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, Integer SC_COA, LocalArInvoice arInvoice, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArStandardMemoLineControllerBean addArDrEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, companyCode);

            // create distribution record

            if (DR_AMNT < 0) {

                DR_AMNT = DR_AMNT * -1;

                if (DR_DBT == 0) {
                    DR_DBT = 1;
                } else if (DR_DBT == 1) {
                    DR_DBT = 0;
                }
            }

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            // arInvoice.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setArInvoice(arInvoice);
            // glChartOfAccount.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setGlChartOfAccount(glChartOfAccount);

            if (DR_CLSS.equals("SC")) {
                if (SC_COA == null) {
                    throw new GlobalBranchAccountNumberInvalidException();
                } else {
                    arDistributionRecord.setDrScAccount(SC_COA);
                }
            }

        } catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private Integer getArGlCoaRevenueAccount(LocalArInvoiceLine arInvoiceLine, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ArStandardMemoLineControllerBean getArGlCoaRevenueAccount");
        // generate revenue account
        try {

            StringBuilder GL_COA_ACCNT = new StringBuilder();

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGenField genField = adCompany.getGenField();

            String FL_SGMNT_SPRTR = String.valueOf(genField.getFlSegmentSeparator());

            LocalAdBranchStandardMemoLine adBranchStandardMemoLine = null;

            try {

                adBranchStandardMemoLine = adBranchStandardMemoLineHome.findBSMLBySMLCodeAndBrCode(arInvoiceLine.getArStandardMemoLine().getSmlCode(), AD_BRNCH, companyCode);

            } catch (FinderException ex) {

            }

            Collection arAutoAccountingSegments = arAutoAccountingSegmentHome.findByAaAccountType("REVENUE", companyCode);

            for (Object autoAccountingSegment : arAutoAccountingSegments) {

                LocalArAutoAccountingSegment arAutoAccountingSegment = (LocalArAutoAccountingSegment) autoAccountingSegment;

                LocalGlChartOfAccount glChartOfAccount = null;

                if (arAutoAccountingSegment.getAasClassType().equals("AR CUSTOMER")) {

                    Debug.print("is null 0");
                    Debug.print(arInvoiceLine.getArInvoice().getArCustomer().getCstGlCoaRevenueAccount().toString());
                    Debug.print("is pass");

                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arInvoiceLine.getArInvoice().getArCustomer().getCstGlCoaRevenueAccount());

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
                            Debug.print("is null 1");
                            Debug.print(adBranchStandardMemoLine.getBsmlGlCoaRevenueAccount().toString());
                            Debug.print("is pass");
                            glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlCoaRevenueAccount());

                        } catch (FinderException ex) {

                        }

                    } else {

                        Debug.print("is null 2");
                        Debug.print(arInvoiceLine.getArStandardMemoLine().getSmlGlCoaReceivableAccount().toString());
                        Debug.print("is pass");
                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arInvoiceLine.getArStandardMemoLine().getSmlGlCoaReceivableAccount());
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

                LocalGlChartOfAccount glGeneratedChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(GL_COA_ACCNT.toString(), companyCode);

                return glGeneratedChartOfAccount.getCoaCode();

            } catch (FinderException ex) {

                if (adBranchStandardMemoLine != null) {

                    LocalGlChartOfAccount glChartOfAccount = null;

                    try {
                        Debug.print("null 2");
                        Debug.print(adBranchStandardMemoLine.getBsmlGlCoaRevenueAccount().toString());
                        Debug.print("pass");
                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlCoaRevenueAccount());

                    } catch (FinderException e) {

                    }

                    return glChartOfAccount.getCoaCode();

                } else {

                    return arInvoiceLine.getArStandardMemoLine().getSmlGlCoaRevenueAccount();
                }
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private Integer getArGlCoaReceivableAccount(LocalArInvoiceLine arInvoiceLine, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ArStandardMemoLineControllerBean getArGlCoaReceivableAccount");
        // generate revenue account
        try {

            StringBuilder GL_COA_ACCNT = new StringBuilder();

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGenField genField = adCompany.getGenField();

            String FL_SGMNT_SPRTR = String.valueOf(genField.getFlSegmentSeparator());

            LocalAdBranchStandardMemoLine adBranchStandardMemoLine = null;

            try {

                adBranchStandardMemoLine = adBranchStandardMemoLineHome.findBSMLBySMLCodeAndBrCode(arInvoiceLine.getArStandardMemoLine().getSmlCode(), AD_BRNCH, companyCode);

            } catch (FinderException ex) {

            }

            Collection arAutoAccountingSegments = arAutoAccountingSegmentHome.findByAaAccountType("REVENUE", companyCode);

            for (Object autoAccountingSegment : arAutoAccountingSegments) {

                LocalArAutoAccountingSegment arAutoAccountingSegment = (LocalArAutoAccountingSegment) autoAccountingSegment;

                LocalGlChartOfAccount glChartOfAccount = null;

                if (arAutoAccountingSegment.getAasClassType().equals("AR CUSTOMER")) {
                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arInvoiceLine.getArInvoice().getArCustomer().getCstGlCoaReceivableAccount());

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
                    Debug.print("AR STANDARD MEMO LINE---------------->");
                    if (adBranchStandardMemoLine != null) {

                        try {

                            glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlCoaReceivableAccount());

                        } catch (FinderException ex) {

                        }

                    } else {

                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arInvoiceLine.getArStandardMemoLine().getSmlGlCoaReceivableAccount());
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
            Debug.print("GL_COA_ACCNT=" + GL_COA_ACCNT);
            GL_COA_ACCNT = new StringBuilder(GL_COA_ACCNT.substring(1));

            try {

                LocalGlChartOfAccount glGeneratedChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(GL_COA_ACCNT.toString(), companyCode);

                return glGeneratedChartOfAccount.getCoaCode();

            } catch (FinderException ex) {

                if (adBranchStandardMemoLine != null) {

                    LocalGlChartOfAccount glChartOfAccount = null;

                    try {

                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchStandardMemoLine.getBsmlGlCoaReceivableAccount());

                    } catch (FinderException e) {

                    }

                    return glChartOfAccount.getCoaCode();

                } else {

                    return arInvoiceLine.getArStandardMemoLine().getSmlGlCoaReceivableAccount();
                }
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeArInvPost(Integer INV_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("ArInvoiceEntryControllerBean executeApInvPost");

        LocalArInvoice arInvoice = null;
        LocalArInvoice arCreditedInvoice = null;

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // validate if invoice/credit memo is already deleted

            try {

                arInvoice = arInvoiceHome.findByPrimaryKey(INV_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if invoice/credit memo is already posted or void

            if (arInvoice.getInvPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

            } else if (arInvoice.getInvVoid() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidException();
            }

            // regenerate cogs & inventory dr
            if (adPreference.getPrfArAutoComputeCogs() == EJBCommon.TRUE) {
                this.regenerateInventoryDr(arInvoice, AD_BRNCH, companyCode);
            }

            // post invoice/credit memo

            if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                // increase customer balance

                double INV_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arInvoice.getInvAmountDue(), companyCode);

                this.post(arInvoice.getInvDate(), INV_AMNT, arInvoice.getArCustomer(), companyCode);

                Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();
                Collection arSalesOrderInvoiceLines = arInvoice.getArSalesOrderInvoiceLines();

                if (arInvoiceLineItems != null && !arInvoiceLineItems.isEmpty()) {

                    for (Object invoiceLineItem : arInvoiceLineItems) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;

                        String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), II_NM, LOC_NM, AD_BRNCH, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = 0d;

                        if (invCosting == null) {

                            COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                            this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), QTY_SLD, QTY_SLD * COST, -QTY_SLD, -(QTY_SLD * COST), 0d, null, AD_BRNCH, companyCode);

                        } else {

                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case "Average":

                                    COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                    this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), QTY_SLD, QTY_SLD * COST, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (QTY_SLD * COST), 0d, null, AD_BRNCH, companyCode);
                                    break;
                                case "FIFO":

                                    double fifoCost = this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), -QTY_SLD, arInvoiceLineItem.getIliUnitPrice(), true, AD_BRNCH, companyCode);

                                    // post entries to database
                                    this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), QTY_SLD, fifoCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (fifoCost * QTY_SLD), 0d, null, AD_BRNCH, companyCode);

                                    break;
                                case "Standard":

                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                    // post entries to database
                                    this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), QTY_SLD, standardCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (standardCost * QTY_SLD), 0d, null, AD_BRNCH, companyCode);
                                    break;
                            }
                        }
                    }

                } else if (arSalesOrderInvoiceLines != null && !arSalesOrderInvoiceLines.isEmpty()) {

                    for (Object salesOrderInvoiceLine : arSalesOrderInvoiceLines) {

                        LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) salesOrderInvoiceLine;
                        LocalArSalesOrderLine arSalesOrderLine = arSalesOrderInvoiceLine.getArSalesOrderLine();

                        String II_NM = arSalesOrderLine.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = arSalesOrderLine.getInvItemLocation().getInvLocation().getLocName();
                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arSalesOrderLine.getInvUnitOfMeasure(), arSalesOrderLine.getInvItemLocation().getInvItem(), arSalesOrderInvoiceLine.getSilQuantityDelivered(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), II_NM, LOC_NM, AD_BRNCH, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = 0d;

                        if (invCosting == null) {

                            COST = arSalesOrderLine.getInvItemLocation().getInvItem().getIiUnitCost();

                            this.postToInvSo(arSalesOrderInvoiceLine, arInvoice.getInvDate(), QTY_SLD, QTY_SLD * COST, -QTY_SLD, -(QTY_SLD * COST), 0d, null, AD_BRNCH, companyCode);

                        } else {

                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case "Average":
                                    COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                    this.postToInvSo(arSalesOrderInvoiceLine, arInvoice.getInvDate(), QTY_SLD, QTY_SLD * COST, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (QTY_SLD * COST), 0d, null, AD_BRNCH, companyCode);
                                    break;
                                case "FIFO":
                                    double fifoCost = this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), -QTY_SLD, arSalesOrderInvoiceLine.getArSalesOrderLine().getSolUnitPrice(), true, AD_BRNCH, companyCode);

                                    // post entries to database
                                    this.postToInvSo(arSalesOrderInvoiceLine, arInvoice.getInvDate(), QTY_SLD, fifoCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (fifoCost * QTY_SLD), 0d, null, AD_BRNCH, companyCode);
                                    break;
                                case "Standard":
                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                    // post entries to database
                                    this.postToInvSo(arSalesOrderInvoiceLine, arInvoice.getInvDate(), QTY_SLD, standardCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (standardCost * QTY_SLD), 0d, null, AD_BRNCH, companyCode);
                                    break;
                            }
                        }
                    }
                }

            } else { // credit memo

                // get credited invoice

                arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, AD_BRNCH, companyCode);

                // decrease customer balance

                double INV_AMNT = this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arInvoice.getInvAmountDue(), companyCode) * -1;

                this.post(arInvoice.getInvDate(), INV_AMNT, arInvoice.getArCustomer(), companyCode);

                // decrease invoice and ips amounts and release lock

                double CREDIT_PERCENT = EJBCommon.roundIt(arInvoice.getInvAmountDue() / arCreditedInvoice.getInvAmountDue(), (short) 6);

                arCreditedInvoice.setInvAmountPaid(arCreditedInvoice.getInvAmountPaid() + arInvoice.getInvAmountDue());

                double TOTAL_INVOICE_PAYMENT_SCHEDULE = 0d;

                Collection arInvoicePaymentSchedules = arCreditedInvoice.getArInvoicePaymentSchedules();

                Iterator i = arInvoicePaymentSchedules.iterator();

                while (i.hasNext()) {

                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) i.next();

                    double INVOICE_PAYMENT_SCHEDULE_AMOUNT = 0;

                    // if last payment schedule subtract to avoid rounding difference error

                    if (i.hasNext()) {

                        INVOICE_PAYMENT_SCHEDULE_AMOUNT = EJBCommon.roundIt(arInvoicePaymentSchedule.getIpsAmountDue() * CREDIT_PERCENT, this.getGlFcPrecisionUnit(companyCode));

                    } else {

                        INVOICE_PAYMENT_SCHEDULE_AMOUNT = arInvoice.getInvAmountDue() - TOTAL_INVOICE_PAYMENT_SCHEDULE;
                    }

                    arInvoicePaymentSchedule.setIpsAmountPaid(arInvoicePaymentSchedule.getIpsAmountPaid() + INVOICE_PAYMENT_SCHEDULE_AMOUNT);

                    arInvoicePaymentSchedule.setIpsLock(EJBCommon.FALSE);

                    TOTAL_INVOICE_PAYMENT_SCHEDULE += INVOICE_PAYMENT_SCHEDULE_AMOUNT;
                }

                Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();

                if (arInvoiceLineItems != null && !arInvoiceLineItems.isEmpty()) {

                    for (Object invoiceLineItem : arInvoiceLineItems) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) invoiceLineItem;

                        String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();
                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), II_NM, LOC_NM, AD_BRNCH, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = 0d;

                        if (invCosting == null) {

                            COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                            this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), -QTY_SLD, -(QTY_SLD * COST), QTY_SLD, QTY_SLD * COST, 0d, null, AD_BRNCH, companyCode);

                        } else {

                            // compute cost variance
                            double CST_VRNC_VL = 0d;

                            if (invCosting.getCstRemainingQuantity() < 0) {
                                CST_VRNC_VL = (invCosting.getCstRemainingQuantity() * COST - invCosting.getCstRemainingValue());
                            }

                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case "Average":
                                    COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                    this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), -QTY_SLD, -(QTY_SLD * COST), invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (QTY_SLD * COST), CST_VRNC_VL, USR_NM, AD_BRNCH, companyCode);

                                    break;
                                case "FIFO":

                                    double fifoCost = this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), QTY_SLD, arInvoiceLineItem.getIliUnitPrice(), true, AD_BRNCH, companyCode);

                                    // post entries to database
                                    this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), -QTY_SLD, fifoCost * -QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (fifoCost * QTY_SLD), CST_VRNC_VL, USR_NM, AD_BRNCH, companyCode);

                                    break;
                                case "Standard":

                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                    // post entries to database
                                    this.postToInv(arInvoiceLineItem, arInvoice.getInvDate(), -QTY_SLD, standardCost * -QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (standardCost * QTY_SLD), CST_VRNC_VL, USR_NM, AD_BRNCH, companyCode);
                                    break;
                            }
                        }
                    }
                }
            }

            // set invoice post status

            arInvoice.setInvPosted(EJBCommon.TRUE);
            arInvoice.setInvPostedBy(USR_NM);
            arInvoice.setInvDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary

            if (adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(arInvoice.getInvDate(), companyCode);

                } catch (FinderException ex) {

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

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);
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

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS RECEIVABLES", arInvoice.getInvCreditMemo() == EJBCommon.FALSE ? "SALES INVOICES" : "CREDIT MEMOS", companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (arDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
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

                    if (adPreference.getPrfEnableArInvoiceBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arInvoice.getArInvoiceBatch().getIbName(), AD_BRNCH, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES INVOICES", AD_BRNCH, companyCode);
                    }

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                    if (adPreference.getPrfEnableArInvoiceBatch() == EJBCommon.TRUE) {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " " + arInvoice.getArInvoiceBatch().getIbName(), "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);

                    } else {

                        glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " SALES INVOICES", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);
                    }
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(arInvoice.getInvReferenceNumber(), arInvoice.getInvDescription(), arInvoice.getInvDate(), 0.0d, null, arInvoice.getInvNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), arInvoice.getArCustomer().getCstTin(), arInvoice.getArCustomer().getCstName(), EJBCommon.FALSE, null, AD_BRNCH, companyCode);

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

                    double DR_AMNT = 0d;

                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);
                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(arDistributionRecord.getDrLine(), arDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    // arDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);
                    glJournalLine.setGlChartOfAccount(arDistributionRecord.getGlChartOfAccount());

                    // glJournal.addGlJournalLine(glJournalLine);
                    glJournalLine.setGlJournal(glJournal);
                    arDistributionRecord.setDrImported(EJBCommon.TRUE);

                    // for FOREX revaluation

                    LocalArInvoice arInvoiceTemp = arInvoice.getInvCreditMemo() == EJBCommon.FALSE ? arInvoice : arCreditedInvoice;

                    if ((!Objects.equals(arInvoiceTemp.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(arInvoiceTemp.getGlFunctionalCurrency().getFcCode()))) {

                        double CONVERSION_RATE = 1;

                        if (arInvoiceTemp.getInvConversionRate() != 0 && arInvoiceTemp.getInvConversionRate() != 1) {

                            CONVERSION_RATE = arInvoiceTemp.getInvConversionRate();

                        } else if (arInvoiceTemp.getInvConversionDate() != null) {

                            CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);
                        }

                        Collection glForexLedgers = null;

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(arInvoiceTemp.getInvDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        } catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(arInvoiceTemp.getInvDate()) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = arDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        } else {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                        }

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        glForexLedger = glForexLedgerHome.create(arInvoiceTemp.getInvDate(), FRL_LN, "SI", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, 0d, companyCode);

                        // glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);
                        glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

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

        } catch (GlJREffectiveDateNoPeriodExistException | GlobalExpiryDateNotFoundException |
                 AdPRFCoaGlVarianceAccountNotFoundException | GlobalBranchAccountNumberInvalidException |
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

    private void post(Date INV_DT, double INV_AMNT, LocalArCustomer arCustomer, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean post");

        LocalArCustomerBalanceHome arCustomerBalanceHome = null;

        // Initialize EJB Home

        try {

            arCustomerBalanceHome = (LocalArCustomerBalanceHome) EJBHomeFactory.lookUpLocalHome(LocalArCustomerBalanceHome.JNDI_NAME, LocalArCustomerBalanceHome.class);

        } catch (NamingException ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

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

                    // arCustomer.addArCustomerBalance(apNewCustomerBalance);
                    apNewCustomerBalance.setArCustomer(arCustomer);

                } else { // equals to invoice date

                    arCustomerBalance.setCbBalance(arCustomerBalance.getCbBalance() + INV_AMNT);
                }

            } else {

                // create new balance

                LocalArCustomerBalance apNewCustomerBalance = arCustomerBalanceHome.create(INV_DT, INV_AMNT, companyCode);

                // arCustomer.addArCustomerBalance(apNewCustomerBalance);
                apNewCustomerBalance.setArCustomer(arCustomer);
            }

            // propagate to subsequent balances if necessary

            arCustomerBalances = arCustomerBalanceHome.findByAfterInvDateAndCstCustomerCode(INV_DT, arCustomer.getCstCustomerCode(), companyCode);

            for (Object customerBalance : arCustomerBalances) {

                LocalArCustomerBalance arCustomerBalance = (LocalArCustomerBalance) customerBalance;

                arCustomerBalance.setCbBalance(arCustomerBalance.getCbBalance() + INV_AMNT);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean convertForeignToFunctionalCurrency");

        LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome = null;
        LocalAdCompany adCompany = null;

        // Initialize EJB Homes

        try {

            glFunctionalCurrencyRateHome = (LocalGlFunctionalCurrencyRateHome) EJBHomeFactory.lookUpLocalHome(LocalGlFunctionalCurrencyRateHome.JNDI_NAME, LocalGlFunctionalCurrencyRateHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

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

    public String checkExpiryDates(String misc, double qty, String reverse) throws Exception {
        // ActionErrors errors = new ActionErrors();
        Debug.print("ApReceivingItemControllerBean getExpiryDates");
        Debug.print("misc: " + misc);

        String separator = "";
        if (reverse == "False") {
            separator = "$";
        } else {
            separator = " ";
        }

        // Remove first $ character
        misc = misc.substring(1);
        Debug.print("misc: " + misc);
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
            Debug.print("g: " + g);
            Debug.print("g length: " + g.length());
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

                Debug.print("miscList G: " + miscList);
            } else {
                Debug.print("KABOOM");
                miscList2 = "Error";
            }
        }
        Debug.print("miscList2 :" + miscList2);
        if (miscList2 == "") {
            miscList.append("$");
        } else {
            miscList = new StringBuilder(miscList2);
        }

        Debug.print("miscList :" + miscList);
        return (miscList.toString());
    }

    private double calculateIliNetAmount(ArModInvoiceLineItemDetails mdetails, double tcRate, String tcType, short precisionUnit) {

        double amount = 0d;

        if (tcType.equals("INCLUSIVE")) {

            amount = EJBCommon.roundIt(mdetails.getIliAmount() / (1 + (tcRate / 100)), precisionUnit);

        } else {

            // tax exclusive, none, zero rated or exempt

            amount = mdetails.getIliAmount();
        }

        return amount;
    }

    private double calculateIliTaxAmount(ArModInvoiceLineItemDetails mdetails, double tcRate, String tcType, double amount, short precisionUnit) {

        double taxAmount = 0d;

        if (!tcType.equals("NONE") && !tcType.equals("EXEMPT")) {

            if (tcType.equals("INCLUSIVE")) {

                taxAmount = EJBCommon.roundIt(mdetails.getIliAmount() - amount, precisionUnit);

            } else if (tcType.equals("EXCLUSIVE")) {

                taxAmount = EJBCommon.roundIt(mdetails.getIliAmount() * tcRate / 100, precisionUnit);

            } else {

                // tax none zero-rated or exempt

            }
        }

        return taxAmount;
    }

    private void addArDrIliEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalArInvoice arInvoice, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArInvoiceEntryControllerBean addArDrIliEntry");

        LocalArDistributionRecordHome arDistributionRecordHome = null;

        // Initialize EJB Home

        try {

            arDistributionRecordHome = (LocalArDistributionRecordHome) EJBHomeFactory.lookUpLocalHome(LocalArDistributionRecordHome.JNDI_NAME, LocalArDistributionRecordHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, companyCode);

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            // arInvoice.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setArInvoice(arInvoice);
            // glChartOfAccount.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setGlChartOfAccount(glChartOfAccount);

        } catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean postToGl");

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

    private void postToInv(LocalArInvoiceLineItem arInvoiceLineItem, Date CST_DT, double CST_QTY_SLD, double CST_CST_OF_SLS, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("ArInvoiceEntryControllerBean postToInv");

        LocalInvCostingHome invCostingHome = null;
        LocalInvAdjustmentLineHome invAdjustmentLineHome = null;

        // Initialize EJB Home

        try {

            invCostingHome = (LocalInvCostingHome) EJBHomeFactory.lookUpLocalHome(LocalInvCostingHome.JNDI_NAME, LocalInvCostingHome.class);
            invAdjustmentLineHome = (LocalInvAdjustmentLineHome) EJBHomeFactory.lookUpLocalHome(LocalInvAdjustmentLineHome.JNDI_NAME, LocalInvAdjustmentLineHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

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

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                // void subsequent cost variance adjustments
                Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT, invItemLocation.getIlCode(), AD_BRNCH, companyCode);

                for (Object adjustmentLine : invAdjustmentLines) {

                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;
                    this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), AD_BRNCH, companyCode);
                }
            }

            String prevExpiryDates = "";
            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            double qtyPrpgt2 = 0;
            try {
                LocalInvCosting prevCst = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(CST_DT, invItemLocation.getIlCode(), AD_BRNCH, companyCode);
                Debug.print("ArInvoicePostControllerBean postToInv C");
                prevExpiryDates = prevCst.getCstExpiryDate();
                qtyPrpgt = prevCst.getCstRemainingQuantity();

                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

            } catch (Exception ex) {
                Debug.print("prevExpiryDates CATCH: " + prevExpiryDates);
                prevExpiryDates = "";
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, 0d, 0d, CST_QTY_SLD, CST_CST_OF_SLS, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, AD_BRNCH, companyCode);
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setArInvoiceLineItem(arInvoiceLineItem);

            invCosting.setCstQuantity(CST_QTY_SLD * -1);
            invCosting.setCstCost(CST_CST_OF_SLS * -1);
            // Get Latest Expiry Dates
            String check = "";
            if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        int qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));

                        String miscList2Prpgt = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), qty2Prpgt, "False");
                        ArrayList miscList = this.expiryDates(arInvoiceLineItem.getIliMisc(), qty2Prpgt);
                        String propagateMiscPrpgt = "";
                        StringBuilder ret = new StringBuilder();

                        Debug.print("CST_ST_QTY Before Trans: " + CST_QTY_SLD);
                        // ArrayList miscList2 = null;
                        if (CST_QTY_SLD < 0) {
                            prevExpiryDates = prevExpiryDates.substring(1);
                            propagateMiscPrpgt = miscList2Prpgt + prevExpiryDates;
                        } else {
                            Iterator mi = miscList.iterator();
                            propagateMiscPrpgt = prevExpiryDates;
                            ret = new StringBuilder(propagateMiscPrpgt);
                            String Checker = "";
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                int qTest = checkExpiryDates(ret + "fin$");
                                ArrayList miscList2 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));
                                Iterator m2 = miscList2.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;

                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();

                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }

                                    Debug.print("miscStr2: " + miscStr2);
                                    Debug.print("miscStr: " + miscStr);
                                    if (miscStr2.trim().equals(miscStr.trim())) {
                                        if (a == 0) {
                                            a = 1;
                                            ret2 = "1st";
                                            Checker = "true";
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
                                                qtyPrpgt2++;
                                                ret2 = "false";
                                            }
                                        }
                                    }
                                }
                                ret.append("$");
                                Debug.print("qtyPrpgt: " + qtyPrpgt);
                                if (qtyPrpgt2 == 0) {
                                    qtyPrpgt2 = qtyPrpgt;
                                }
                                qtyPrpgt = qtyPrpgt - 1;
                            }
                            // propagateMiscPrpgt = propagateMiscPrpgt.replace(" ", "$");
                            propagateMiscPrpgt = ret.toString();
                            Debug.print("propagateMiscPrpgt: " + propagateMiscPrpgt);
                            if (Checker.equals("true")) {
                                // invCosting.setCstExpiryDate(ret);
                                Debug.print("check: " + check);
                            } else {
                                Debug.print("exA");
                                throw new GlobalExpiryDateNotFoundException(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                            }
                        }
                        invCosting.setCstExpiryDate(propagateMiscPrpgt);

                    } else {
                        invCosting.setCstExpiryDate(prevExpiryDates);
                    }
                } else {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        int initialQty = Integer.parseInt(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                        String initialPrpgt = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), initialQty, "False");

                        invCosting.setCstExpiryDate(initialPrpgt);
                    } else {
                        invCosting.setCstExpiryDate(prevExpiryDates);
                        Debug.print("prevExpiryDates");
                    }
                }
            }

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0 && adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "ARCR" + arInvoiceLineItem.getArReceipt().getRctNumber(), arInvoiceLineItem.getArReceipt().getRctDescription(), arInvoiceLineItem.getArReceipt().getRctDate(), USR_NM, AD_BRNCH, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

            Iterator i = invCostings.iterator();
            String miscList = "";
            ArrayList miscList2 = null;

            Debug.print("miscList Propagate:" + miscList);
            String propagateMisc = "";
            StringBuilder ret = new StringBuilder();

            Debug.print("CST_ST_QTY: " + CST_QTY_SLD);
            while (i.hasNext()) {
                String Checker = "";
                String Checker2 = "";

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() - CST_QTY_SLD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() - CST_CST_OF_SLS);

                if (arInvoiceLineItem.getInvItemLocation().getInvItem().getIiTraceMisc() != 0) {
                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        Debug.print("BAGO ANG MALI: " + arInvoiceLineItem.getIliMisc());

                        double qty = Double.parseDouble(this.getQuantityExpiryDates(arInvoiceLineItem.getIliMisc()));
                        // double qty2 = this.checkExpiryDates2(arInvoiceLineItem.getIliMisc());
                        miscList = this.propagateExpiryDates(arInvoiceLineItem.getIliMisc(), qty, "False");
                        miscList2 = this.expiryDates(arInvoiceLineItem.getIliMisc(), qty);

                        Debug.print("invAdjustmentLine.getAlMisc(): " + arInvoiceLineItem.getIliMisc());
                        Debug.print("getAlAdjustQuantity(): " + arInvoiceLineItem.getIliQuantity());

                        if (arInvoiceLineItem.getIliQuantity() < 0) {
                            Iterator mi = miscList2.iterator();

                            propagateMisc = invPropagatedCosting.getCstExpiryDate();
                            ret = new StringBuilder(invPropagatedCosting.getCstExpiryDate());
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();

                                int qTest = checkExpiryDates(ret + "fin$");
                                ArrayList miscList3 = this.expiryDates("$" + ret, Double.parseDouble(Integer.toString(qTest)));

                                // ArrayList miscList3 = this.expiryDates("$" + ret, qtyPrpgt);
                                Debug.print("ret: " + ret);
                                Iterator m2 = miscList3.iterator();
                                ret = new StringBuilder();
                                String ret2 = "false";
                                int a = 0;
                                while (m2.hasNext()) {
                                    String miscStr2 = (String) m2.next();

                                    if (ret2 == "1st") {
                                        ret2 = "false";
                                    }
                                    Debug.print("2 miscStr: " + miscStr);
                                    Debug.print("2 miscStr2: " + miscStr2);
                                    if (miscStr2.equals(miscStr)) {
                                        if (a == 0) {
                                            a = 1;
                                            ret2 = "1st";
                                            Checker2 = "true";
                                        } else {
                                            a = a + 1;
                                            ret2 = "true";
                                        }
                                    }
                                    Debug.print("Checker: " + Checker2);
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
                                if (Checker2 != "true") {
                                    throw new GlobalExpiryDateNotFoundException(arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName());
                                } else {
                                    Debug.print("TAE");
                                }

                                ret.append("$");
                                qtyPrpgt = qtyPrpgt - 1;
                            }
                        }
                    }

                    if (arInvoiceLineItem.getIliMisc() != null && arInvoiceLineItem.getIliMisc() != "" && arInvoiceLineItem.getIliMisc().length() != 0) {
                        if (CST_QTY_SLD < 0) {
                            propagateMisc = miscList + invPropagatedCosting.getCstExpiryDate().substring(1);
                            Debug.print("propagateMiscPrpgt : " + propagateMisc);

                        } else {
                            Iterator mi = miscList2.iterator();

                            propagateMisc = prevExpiryDates;
                            ret = new StringBuilder(propagateMisc);
                            while (mi.hasNext()) {
                                String miscStr = (String) mi.next();
                                Debug.print("ret123: " + ret);
                                Debug.print("qtyPrpgt123: " + qtyPrpgt);
                                Debug.print("qtyPrpgt2: " + qtyPrpgt2);
                                if (qtyPrpgt <= 0) {
                                    qtyPrpgt = qtyPrpgt2;
                                }

                                int qTest = checkExpiryDates(ret + "fin$");
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

                                    Debug.print("miscStr2: " + miscStr2);
                                    Debug.print("miscStr: " + miscStr);
                                    if (miscStr2.trim().equals(miscStr.trim())) {
                                        if (a == 0) {
                                            a = 1;
                                            ret2 = "1st";
                                            Checker = "true";
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
                                qtyPrpgt = qtyPrpgt - 1;
                            }
                            propagateMisc = ret.toString();
                            Debug.print("propagateMiscPrpgt: " + propagateMisc);

                            if (Checker == "true") {
                                // invPropagatedCosting.setCstExpiryDate(propagateMisc);
                                Debug.print("Yes");
                            } else {
                                Debug.print("ex1");

                            }
                        }

                        invPropagatedCosting.setCstExpiryDate(propagateMisc);
                    } else {
                        invPropagatedCosting.setCstExpiryDate(prevExpiryDates);
                    }
                }
            }

            // regenerate cost variance
            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                this.regenerateCostVariance(invCostings, invCosting, AD_BRNCH, companyCode);
            }

        } catch (AdPRFCoaGlVarianceAccountNotFoundException | GlobalExpiryDateNotFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public int checkExpiryDates(String misc) throws Exception {

        String separator = "$";

        // Remove first $ character
        misc = misc.substring(1);
        // Debug.print("misc: " + misc);
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
        } catch (Exception e) {

        }

        if (miscList2 == "") {
            miscList.append("$");
        } else {
            miscList = new StringBuilder(miscList2);
        }

        return (numberExpry);
    }

    public String propagateExpiryDates(String misc, double qty, String reverse) throws Exception {
        // ActionErrors errors = new ActionErrors();
        Debug.print("ApReceivingItemControllerBean getExpiryDates");
        Debug.print("misc: " + misc);
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
            Debug.print("misc: " + misc);
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
                Debug.print("g: " + g);
                Debug.print("g length: " + g.length());
                if (g.length() != 0) {
                    miscList.append("$").append(g);
                    Debug.print("miscList G: " + miscList);
                }
            }

            miscList.append("$");
        } catch (Exception e) {
            miscList = new StringBuilder();
        }

        Debug.print("miscList :" + miscList);
        return (miscList.toString());
    }

    private ArrayList expiryDates(String misc, double qty) throws Exception {

        Debug.print("ApReceivingItemControllerBean getExpiryDates");
        ArrayList miscList = new ArrayList();
        try {
            Debug.print("misc: " + misc);
            String separator = "$";

            // Remove first $ character
            misc = misc.substring(1);

            // Counter
            int start = 0;
            int nextIndex = misc.indexOf(separator, start);
            int length = nextIndex - start;

            Debug.print("qty" + qty);

            for (int x = 0; x < qty; x++) {

                // Date
                start = nextIndex + 1;
                nextIndex = misc.indexOf(separator, start);
                length = nextIndex - start;
                Debug.print("x" + x);
                String checker = misc.substring(start, start + length);
                Debug.print("checker" + checker);
                if (checker.length() != 0 || checker != "null") {
                    miscList.add(checker);
                } else {
                    miscList.add("null");
                }
            }
        } catch (Exception e) {

            // miscList = "";
        }

        Debug.print("miscList :" + miscList);
        return miscList;
    }

    public String getQuantityExpiryDates(String qntty) {

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
            Debug.print("Y " + y);
        } catch (Exception e) {
            y = "0";
        }

        return y;
    }

    public String propagateExpiryDates(String misc, double qty) throws Exception {
        // ActionErrors errors = new ActionErrors();

        Debug.print("ApReceivingItemControllerBean getExpiryDates");

        String separator = "$";
        StringBuilder miscList = new StringBuilder();
        // Remove first $ character
        try {
            misc = misc.substring(1);

            // Counter
            int start = 0;
            int nextIndex = misc.indexOf(separator, start);
            int length = nextIndex - start;

            Debug.print("qty" + qty);

            for (int x = 0; x < qty; x++) {

                // Date
                start = nextIndex + 1;
                nextIndex = misc.indexOf(separator, start);
                length = nextIndex - start;

                try {

                    miscList.append("$").append(misc.substring(start, start + length));
                } catch (Exception ex) {

                    throw ex;
                }
            }
            miscList.append("$");
        } catch (Exception e) {

            miscList = new StringBuilder();
        }

        Debug.print("miscList :" + miscList);
        return (miscList.toString());
    }

    private void postToBua(LocalArInvoiceLineItem arInvoiceLineItem, Date CST_DT, double CST_ASSMBLY_QTY, double CST_ASSMBLY_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, String II_NM, String LOC_NM, double CST_VRNC_VL, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArInvoiceEntryControllerBean postToBua");

        LocalInvCostingHome invCostingHome = null;
        LocalInvItemLocationHome invItemLocationHome = null;
        LocalInvAdjustmentLineHome invAdjustmentLineHome = null;

        // Initialize EJB Home

        try {

            invCostingHome = (LocalInvCostingHome) EJBHomeFactory.lookUpLocalHome(LocalInvCostingHome.JNDI_NAME, LocalInvCostingHome.class);
            invItemLocationHome = (LocalInvItemLocationHome) EJBHomeFactory.lookUpLocalHome(LocalInvItemLocationHome.JNDI_NAME, LocalInvItemLocationHome.class);
            invAdjustmentLineHome = (LocalInvAdjustmentLineHome) EJBHomeFactory.lookUpLocalHome(LocalInvAdjustmentLineHome.JNDI_NAME, LocalInvAdjustmentLineHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = invItemLocationHome.findByLocNameAndIiName(LOC_NM, II_NM, companyCode);
            int CST_LN_NMBR = 0;

            CST_ASSMBLY_QTY = EJBCommon.roundIt(CST_ASSMBLY_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ASSMBLY_CST = EJBCommon.roundIt(CST_ASSMBLY_CST, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if ((CST_ASSMBLY_QTY < 0) || (CST_ASSMBLY_QTY < 0 && !arInvoiceLineItem.getInvItemLocation().getInvItem().equals(invItemLocation.getInvItem()))) {

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(CST_ASSMBLY_QTY));
            }

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                // void subsequent cost variance adjustments
                Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT, invItemLocation.getIlCode(), AD_BRNCH, companyCode);

                for (Object adjustmentLine : invAdjustmentLines) {

                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;
                    this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), AD_BRNCH, companyCode);
                }
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, 0d, 0d, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, CST_ASSMBLY_QTY > 0 ? CST_ASSMBLY_QTY : 0, AD_BRNCH, companyCode);
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setArInvoiceLineItem(arInvoiceLineItem);

            invCosting.setCstQuantity(CST_ASSMBLY_QTY);
            invCosting.setCstCost(CST_ASSMBLY_CST);

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0 && adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "ARCR" + arInvoiceLineItem.getArReceipt().getRctNumber(), arInvoiceLineItem.getArReceipt().getRctDescription(), arInvoiceLineItem.getArReceipt().getRctDate(), USR_NM, AD_BRNCH, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

            for (Object costing : invCostings) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ASSMBLY_QTY);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ASSMBLY_CST);
            }

            // regenerate cost variance
            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                this.regenerateCostVariance(invCostings, invCosting, AD_BRNCH, companyCode);
            }

        } catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToInvSo(LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine, Date CST_DT, double CST_QTY_SLD, double CST_CST_OF_SLS, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ArInvoiceEntryControllerBean postToInvSo");

        LocalInvCostingHome invCostingHome = null;
        LocalInvAdjustmentLineHome invAdjustmentLineHome = null;

        // Initialize EJB Home

        try {

            invCostingHome = (LocalInvCostingHome) EJBHomeFactory.lookUpLocalHome(LocalInvCostingHome.JNDI_NAME, LocalInvCostingHome.class);
            invAdjustmentLineHome = (LocalInvAdjustmentLineHome) EJBHomeFactory.lookUpLocalHome(LocalInvAdjustmentLineHome.JNDI_NAME, LocalInvAdjustmentLineHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalArSalesOrderLine arSalesOrderLine = arSalesOrderInvoiceLine.getArSalesOrderLine();
            LocalInvItemLocation invItemLocation = arSalesOrderLine.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_SLD = EJBCommon.roundIt(CST_QTY_SLD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_CST_OF_SLS = EJBCommon.roundIt(CST_CST_OF_SLS, adCompany.getGlFunctionalCurrency().getFcPrecision());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adCompany.getGlFunctionalCurrency().getFcPrecision());

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                // void subsequent cost variance adjustments
                Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT, invItemLocation.getIlCode(), AD_BRNCH, companyCode);

                for (Object adjustmentLine : invAdjustmentLines) {

                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;
                    this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), AD_BRNCH, companyCode);
                }
            }

            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, 0d, 0d, CST_QTY_SLD, CST_CST_OF_SLS, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, AD_BRNCH, companyCode);
            // invItemLocation.addInvCosting(invCosting);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setArSalesOrderInvoiceLine(arSalesOrderInvoiceLine);

            invCosting.setCstQuantity(CST_QTY_SLD * -1);
            invCosting.setCstCost(CST_CST_OF_SLS * -1);

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0 && adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "ARCM" + arSalesOrderInvoiceLine.getArInvoice().getInvNumber(), arSalesOrderInvoiceLine.getArInvoice().getInvDescription(), arSalesOrderInvoiceLine.getArInvoice().getInvDate(), USR_NM, AD_BRNCH, companyCode);
            }

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

            for (Object costing : invCostings) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_QTY_SLD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_CST_OF_SLS);
            }

            // regenerate cost varaince
            if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                this.regenerateCostVariance(invCostings, invCosting, AD_BRNCH, companyCode);
            }

        } catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double QTY_SLD, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean convertByUomFromAndItemAndQuantity");

        LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome = null;

        // Initialize EJB Home

        try {

            invUnitOfMeasureConversionHome = (LocalInvUnitOfMeasureConversionHome) EJBHomeFactory.lookUpLocalHome(LocalInvUnitOfMeasureConversionHome.JNDI_NAME, LocalInvUnitOfMeasureConversionHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {
            Debug.print("ArInvoiceEntryControllerBean convertByUomFromAndItemAndQuantity A");
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);
            Debug.print("ArInvoiceEntryControllerBean convertByUomFromAndItemAndQuantity B");
            return EJBCommon.roundIt(QTY_SLD * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double computeTotalBalance(Integer invoiceCode, String CST_CSTMR_CODE, Integer companyCode) {

        LocalArCustomerBalanceHome arCustomerBalanceHome = null;
        LocalArInvoiceHome arInvoiceHome = null;
        LocalArReceiptHome arReceiptHome = null;
        LocalArPdcHome arPdcHome = null;

        try {

            arInvoiceHome = (LocalArInvoiceHome) EJBHomeFactory.lookUpLocalHome(LocalArInvoiceHome.JNDI_NAME, LocalArInvoiceHome.class);
            arCustomerBalanceHome = (LocalArCustomerBalanceHome) EJBHomeFactory.lookUpLocalHome(LocalArCustomerBalanceHome.JNDI_NAME, LocalArCustomerBalanceHome.class);
            arReceiptHome = (LocalArReceiptHome) EJBHomeFactory.lookUpLocalHome(LocalArReceiptHome.JNDI_NAME, LocalArReceiptHome.class);
            arPdcHome = (LocalArPdcHome) EJBHomeFactory.lookUpLocalHome(LocalArPdcHome.JNDI_NAME, LocalArPdcHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        double customerBalance = 0;

        try {

            // get latest balance

            Collection arCustomerBalances = arCustomerBalanceHome.findByCstCustomerCode(CST_CSTMR_CODE, companyCode);

            if (!arCustomerBalances.isEmpty()) {

                ArrayList customerBalanceList = new ArrayList(arCustomerBalances);

                customerBalance = ((LocalArCustomerBalance) customerBalanceList.get(customerBalanceList.size() - 1)).getCbBalance();
            }

            // get amount of unposted invoices/credit memos

            Collection arInvoices = arInvoiceHome.findUnpostedInvByCstCustomerCode(CST_CSTMR_CODE, companyCode);

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

            Collection arReceipts = arReceiptHome.findUnpostedRctByCstCustomerCode(CST_CSTMR_CODE, companyCode);

            for (Object receipt : arReceipts) {

                LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                customerBalance = customerBalance - arReceipt.getRctAmount();
            }

            // get amount of pdc (unposted or posted) type PR findPdcByPdcType("PR",
            // companyCode)

            Collection arPdcs = arPdcHome.findPdcByPdcType(companyCode);

            for (Object pdc : arPdcs) {

                LocalArPdc arPdc = (LocalArPdc) pdc;

                customerBalance = customerBalance - arPdc.getPdcAmount();
            }

        } catch (FinderException ex) {

        }

        return customerBalance;
    }

    private void regenerateInventoryDr(LocalArInvoice arInvoice, Integer AD_BRNCH, Integer companyCode) throws GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ArInvoiceEntryControllerBean regenerateInventoryDr");

        LocalArDistributionRecordHome arDistributionRecordHome = null;
        LocalInvCostingHome invCostingHome = null;
        LocalInvItemHome invItemHome = null;
        LocalInvItemLocationHome invItemLocationHome = null;
        LocalAdBranchItemLocationHome adBranchItemLocationHome = null;

        // Initialize EJB Home

        try {

            arDistributionRecordHome = (LocalArDistributionRecordHome) EJBHomeFactory.lookUpLocalHome(LocalArDistributionRecordHome.JNDI_NAME, LocalArDistributionRecordHome.class);
            invCostingHome = (LocalInvCostingHome) EJBHomeFactory.lookUpLocalHome(LocalInvCostingHome.JNDI_NAME, LocalInvCostingHome.class);
            invItemHome = (LocalInvItemHome) EJBHomeFactory.lookUpLocalHome(LocalInvItemHome.JNDI_NAME, LocalInvItemHome.class);
            invItemLocationHome = (LocalInvItemLocationHome) EJBHomeFactory.lookUpLocalHome(LocalInvItemLocationHome.JNDI_NAME, LocalInvItemLocationHome.class);
            adBranchItemLocationHome = (LocalAdBranchItemLocationHome) EJBHomeFactory.lookUpLocalHome(LocalAdBranchItemLocationHome.JNDI_NAME, LocalAdBranchItemLocationHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // regenerate inventory distribution records

            Collection arDistributionRecords = arDistributionRecordHome.findImportableDrByInvCode(arInvoice.getInvCode(), companyCode);

            Iterator i = arDistributionRecords.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                if (arDistributionRecord.getDrClass().equals("COGS") || arDistributionRecord.getDrClass().equals("INVENTORY")) {

                    i.remove();
                    // arDistributionRecord.entityRemove();
                    em.remove(arDistributionRecord);
                }
            }

            Collection arInvoiceLineItems = arInvoice.getArInvoiceLineItems();
            Collection arSalesOrderInvoiceLines = arInvoice.getArSalesOrderInvoiceLines();

            if (arInvoiceLineItems != null && !arInvoiceLineItems.isEmpty()) {

                i = arInvoiceLineItems.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();
                    LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();

                    // start date validation

                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    // add cost of sales distribution and inventory

                    double COST = 0d;

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

                        switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                            case "Average":
                                COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                                break;
                            case "FIFO":
                                COST = this.getInvFifoCost(arInvoice.getInvDate(), invItemLocation.getIlCode(), arInvoiceLineItem.getIliQuantity(), arInvoiceLineItem.getIliUnitPrice(), false, AD_BRNCH, companyCode);
                                break;
                            case "Standard":
                                COST = invItemLocation.getInvItem().getIiUnitCost();
                                break;
                        }

                    } catch (FinderException ex) {

                        COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arInvoiceLineItem.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);

                    } catch (FinderException ex) {

                    }

                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arInvoice, AD_BRNCH, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlInventoryAccount(), arInvoice, AD_BRNCH, companyCode);

                        } else {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, AD_BRNCH, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, AD_BRNCH, companyCode);
                        }

                    } else {

                        if (adBranchItemLocation != null) {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.FALSE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arInvoice, AD_BRNCH, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.TRUE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlInventoryAccount(), arInvoice, AD_BRNCH, companyCode);

                        } else {

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.FALSE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, AD_BRNCH, companyCode);

                            this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.TRUE, COST * QTY_SLD, arInvoiceLineItem.getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, AD_BRNCH, companyCode);
                        }
                    }
                }

            } else if (arSalesOrderInvoiceLines != null && !arSalesOrderInvoiceLines.isEmpty()) {

                i = arSalesOrderInvoiceLines.iterator();

                while (i.hasNext()) {

                    LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) i.next();
                    LocalInvItemLocation invItemLocation = arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation();

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    // add cost of sales distribution and inventory

                    double COST = 0d;

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arInvoice.getInvDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);

                        switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                            case "Average":
                                COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                                break;
                            case "FIFO":
                                COST = this.getInvFifoCost(arInvoice.getInvDate(), invItemLocation.getIlCode(), arSalesOrderInvoiceLine.getSilQuantityDelivered(), arSalesOrderInvoiceLine.getArSalesOrderLine().getSolUnitPrice(), false, AD_BRNCH, companyCode);
                                break;
                            case "Standard":
                                COST = invItemLocation.getInvItem().getIiUnitCost();
                                break;
                        }

                    } catch (FinderException ex) {

                        COST = arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvUnitOfMeasure(), invItemLocation.getInvItem(), arSalesOrderInvoiceLine.getSilQuantityDelivered(), companyCode);

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {

                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);

                        this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlCostOfSalesAccount(), arInvoice, AD_BRNCH, companyCode);

                        this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, adBranchItemLocation.getBilCoaGlInventoryAccount(), arInvoice, AD_BRNCH, companyCode);

                    } catch (FinderException ex) {

                        this.addArDrIliEntry(arInvoice.getArDrNextLine(), "COGS", EJBCommon.TRUE, COST * QTY_SLD, arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getIlGlCoaCostOfSalesAccount(), arInvoice, AD_BRNCH, companyCode);

                        this.addArDrIliEntry(arInvoice.getArDrNextLine(), "INVENTORY", EJBCommon.FALSE, COST * QTY_SLD, arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getIlGlCoaInventoryAccount(), arInvoice, AD_BRNCH, companyCode);
                    }

                    // add quantity to item location committed quantity

                    double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arSalesOrderInvoiceLine.getArSalesOrderLine().getInvUnitOfMeasure(), arSalesOrderInvoiceLine.getArSalesOrderLine().getInvItemLocation().getInvItem(), arSalesOrderInvoiceLine.getSilQuantityDelivered(), companyCode);
                    invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);
                }
            }

        } catch (GlobalInventoryDateException | GlobalBranchAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertCostByUom(String II_NM, String UOM_NM, double unitCost, boolean isFromDefault, Integer companyCode) {

        Debug.print("ArInvoiceEntryControllerBean convertCostByUom");

        LocalInvItemHome invItemHome = null;
        LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome = null;

        // Initialize EJB Home

        try {

            invUnitOfMeasureConversionHome = (LocalInvUnitOfMeasureConversionHome) EJBHomeFactory.lookUpLocalHome(LocalInvUnitOfMeasureConversionHome.JNDI_NAME, LocalInvUnitOfMeasureConversionHome.class);
            invItemHome = (LocalInvItemHome) EJBHomeFactory.lookUpLocalHome(LocalInvItemHome.JNDI_NAME, LocalInvItemHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            if (isFromDefault) {

                return unitCost * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor();

            } else {

                return unitCost * invUnitOfMeasureConversion.getUmcConversionFactor() / invDefaultUomConversion.getUmcConversionFactor();
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean voidInvAdjustment");

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

        Debug.print("ArMiscReceiptEntryControllerBean addInvDrEntry");

        LocalInvDistributionRecordHome invDistributionRecordHome = null;

        // Initialize EJB Home

        try {

            invDistributionRecordHome = (LocalInvDistributionRecordHome) EJBHomeFactory.lookUpLocalHome(LocalInvDistributionRecordHome.JNDI_NAME, LocalInvDistributionRecordHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

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

            // invAdjustment.addInvDistributionRecord(invDistributionRecord);
            invDistributionRecord.setInvAdjustment(invAdjustment);
            // glChartOfAccount.addInvDistributionRecord(invDistributionRecord);
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

        Debug.print("ArMiscReceiptEntryControllerBean executeInvAdjPost");

        LocalInvAdjustmentHome invAdjustmentHome = null;
        LocalGlJournalHome glJournalHome = null;
        LocalGlJournalBatchHome glJournalBatchHome = null;
        LocalGlSuspenseAccountHome glSuspenseAccountHome = null;
        LocalGlJournalLineHome glJournalLineHome = null;
        LocalGlJournalSourceHome glJournalSourceHome = null;
        LocalGlJournalCategoryHome glJournalCategoryHome = null;
        LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome = null;
        LocalInvDistributionRecordHome invDistributionRecordHome = null;
        LocalInvCostingHome invCostingHome = null;
        LocalInvAdjustmentLineHome invAdjustmentLineHome = null;

        // Initialize EJB Home

        try {

            invAdjustmentHome = (LocalInvAdjustmentHome) EJBHomeFactory.lookUpLocalHome(LocalInvAdjustmentHome.JNDI_NAME, LocalInvAdjustmentHome.class);
            glJournalHome = (LocalGlJournalHome) EJBHomeFactory.lookUpLocalHome(LocalGlJournalHome.JNDI_NAME, LocalGlJournalHome.class);
            glJournalBatchHome = (LocalGlJournalBatchHome) EJBHomeFactory.lookUpLocalHome(LocalGlJournalBatchHome.JNDI_NAME, LocalGlJournalBatchHome.class);
            glSuspenseAccountHome = (LocalGlSuspenseAccountHome) EJBHomeFactory.lookUpLocalHome(LocalGlSuspenseAccountHome.JNDI_NAME, LocalGlSuspenseAccountHome.class);
            glJournalLineHome = (LocalGlJournalLineHome) EJBHomeFactory.lookUpLocalHome(LocalGlJournalLineHome.JNDI_NAME, LocalGlJournalLineHome.class);
            glJournalSourceHome = (LocalGlJournalSourceHome) EJBHomeFactory.lookUpLocalHome(LocalGlJournalSourceHome.JNDI_NAME, LocalGlJournalSourceHome.class);
            glJournalCategoryHome = (LocalGlJournalCategoryHome) EJBHomeFactory.lookUpLocalHome(LocalGlJournalCategoryHome.JNDI_NAME, LocalGlJournalCategoryHome.class);
            glFunctionalCurrencyHome = (LocalGlFunctionalCurrencyHome) EJBHomeFactory.lookUpLocalHome(LocalGlFunctionalCurrencyHome.JNDI_NAME, LocalGlFunctionalCurrencyHome.class);
            invDistributionRecordHome = (LocalInvDistributionRecordHome) EJBHomeFactory.lookUpLocalHome(LocalInvDistributionRecordHome.JNDI_NAME, LocalInvDistributionRecordHome.class);
            invCostingHome = (LocalInvCostingHome) EJBHomeFactory.lookUpLocalHome(LocalInvCostingHome.JNDI_NAME, LocalInvCostingHome.class);
            invAdjustmentLineHome = (LocalInvAdjustmentLineHome) EJBHomeFactory.lookUpLocalHome(LocalInvAdjustmentLineHome.JNDI_NAME, LocalInvAdjustmentLineHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

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
                // glChartOfAccount.addGlJournalLine(glOffsetJournalLine);
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

                // invDistributionRecord.getInvChartOfAccount().addGlJournalLine(glJournalLine);
                glJournalLine.setGlChartOfAccount(invDistributionRecord.getInvChartOfAccount());

                // glJournal.addGlJournalLine(glJournalLine);
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

        LocalInvAdjustmentLineHome invAdjustmentLineHome = null;

        // Initialize EJB Home

        try {

            invAdjustmentLineHome = (LocalInvAdjustmentLineHome) EJBHomeFactory.lookUpLocalHome(LocalInvAdjustmentLineHome.JNDI_NAME, LocalInvAdjustmentLineHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            // create dr entry
            LocalInvAdjustmentLine invAdjustmentLine = null;
            invAdjustmentLine = invAdjustmentLineHome.create(CST_VRNC_VL, null, null, 0, 0, AL_VD, companyCode);

            // map adjustment, unit of measure, item location
            // invAdjustment.addInvAdjustmentLine(invAdjustmentLine);
            invAdjustmentLine.setInvAdjustment(invAdjustment);
            // invItemLocation.getInvItem().getInvUnitOfMeasure().addInvAdjustmentLine(invAdjustmentLine);
            invAdjustmentLine.setInvItemLocation(invItemLocation);
            // invItemLocation.addInvAdjustmentLine(invAdjustmentLine);
            invAdjustmentLine.setInvItemLocation(invItemLocation);

            return invAdjustmentLine;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT, double CST_ADJST_QTY, double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean postInvAdjustmentToInventory");

        LocalInvCostingHome invCostingHome = null;

        // Initialize EJB Home

        try {

            invCostingHome = (LocalInvCostingHome) EJBHomeFactory.lookUpLocalHome(LocalInvCostingHome.JNDI_NAME, LocalInvCostingHome.class);

        } catch (NamingException ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

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
            // invItemLocation.addInvCosting(invCosting);
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

    public Integer saveArRctEntry(ArReceiptDetails details, LocalGlInvestorAccountBalance glInvestorAccountBalance, String BA_NM, String TC_NM, String WTC_NM, String FC_NM, String CST_CSTMR_CODE, String RB_NM, ArrayList ilList, boolean isDraft, String SLP_SLSPRSN_CODE, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException, GlobalRecordAlreadyAssignedException {

        Debug.print("ArMiscReceiptEntryControllerBean saveArRctEntry");

        LocalArReceiptHome arReceiptHome = null;
        LocalArReceiptBatchHome arReceiptBatchHome = null;
        LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome = null;
        LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome = null;
        LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome = null;
        LocalAdBankAccountHome adBankAccountHome = null;
        LocalArTaxCodeHome arTaxCodeHome = null;
        LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome = null;
        LocalArCustomerHome arCustomerHome = null;
        LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome = null;
        LocalAdApprovalHome adApprovalHome = null;
        LocalAdAmountLimitHome adAmountLimitHome = null;
        LocalAdApprovalUserHome adApprovalUserHome = null;
        LocalAdApprovalQueueHome adApprovalQueueHome = null;
        LocalArSalespersonHome arSalespersonHome = null;
        LocalArAppliedInvoiceHome arAppliedInvoiceHome = null;
        LocalAdBranchBankAccountHome adBranchBankAccountHome = null;
        LocalAdBranchArTaxCodeHome adBranchArTaxCodeHome = null;

        LocalArReceipt arReceipt = null;

        // Initialize EJB Home

        try {

            adBranchArTaxCodeHome = (LocalAdBranchArTaxCodeHome) EJBHomeFactory.lookUpLocalHome(LocalAdBranchArTaxCodeHome.JNDI_NAME, LocalAdBranchArTaxCodeHome.class);

            arReceiptHome = (LocalArReceiptHome) EJBHomeFactory.lookUpLocalHome(LocalArReceiptHome.JNDI_NAME, LocalArReceiptHome.class);
            arReceiptBatchHome = (LocalArReceiptBatchHome) EJBHomeFactory.lookUpLocalHome(LocalArReceiptBatchHome.JNDI_NAME, LocalArReceiptBatchHome.class);
            glFunctionalCurrencyHome = (LocalGlFunctionalCurrencyHome) EJBHomeFactory.lookUpLocalHome(LocalGlFunctionalCurrencyHome.JNDI_NAME, LocalGlFunctionalCurrencyHome.class);
            adDocumentSequenceAssignmentHome = (LocalAdDocumentSequenceAssignmentHome) EJBHomeFactory.lookUpLocalHome(LocalAdDocumentSequenceAssignmentHome.JNDI_NAME, LocalAdDocumentSequenceAssignmentHome.class);
            adBranchDocumentSequenceAssignmentHome = (LocalAdBranchDocumentSequenceAssignmentHome) EJBHomeFactory.lookUpLocalHome(LocalAdBranchDocumentSequenceAssignmentHome.JNDI_NAME, LocalAdBranchDocumentSequenceAssignmentHome.class);
            adBankAccountHome = (LocalAdBankAccountHome) EJBHomeFactory.lookUpLocalHome(LocalAdBankAccountHome.JNDI_NAME, LocalAdBankAccountHome.class);
            arTaxCodeHome = (LocalArTaxCodeHome) EJBHomeFactory.lookUpLocalHome(LocalArTaxCodeHome.JNDI_NAME, LocalArTaxCodeHome.class);
            arWithholdingTaxCodeHome = (LocalArWithholdingTaxCodeHome) EJBHomeFactory.lookUpLocalHome(LocalArWithholdingTaxCodeHome.JNDI_NAME, LocalArWithholdingTaxCodeHome.class);
            arCustomerHome = (LocalArCustomerHome) EJBHomeFactory.lookUpLocalHome(LocalArCustomerHome.JNDI_NAME, LocalArCustomerHome.class);
            glFunctionalCurrencyRateHome = (LocalGlFunctionalCurrencyRateHome) EJBHomeFactory.lookUpLocalHome(LocalGlFunctionalCurrencyRateHome.JNDI_NAME, LocalGlFunctionalCurrencyRateHome.class);
            adApprovalHome = (LocalAdApprovalHome) EJBHomeFactory.lookUpLocalHome(LocalAdApprovalHome.JNDI_NAME, LocalAdApprovalHome.class);
            adAmountLimitHome = (LocalAdAmountLimitHome) EJBHomeFactory.lookUpLocalHome(LocalAdAmountLimitHome.JNDI_NAME, LocalAdAmountLimitHome.class);
            adApprovalUserHome = (LocalAdApprovalUserHome) EJBHomeFactory.lookUpLocalHome(LocalAdApprovalUserHome.JNDI_NAME, LocalAdApprovalUserHome.class);
            adApprovalQueueHome = (LocalAdApprovalQueueHome) EJBHomeFactory.lookUpLocalHome(LocalAdApprovalQueueHome.JNDI_NAME, LocalAdApprovalQueueHome.class);
            arSalespersonHome = (LocalArSalespersonHome) EJBHomeFactory.lookUpLocalHome(LocalArSalespersonHome.JNDI_NAME, LocalArSalespersonHome.class);
            arAppliedInvoiceHome = (LocalArAppliedInvoiceHome) EJBHomeFactory.lookUpLocalHome(LocalArAppliedInvoiceHome.JNDI_NAME, LocalArAppliedInvoiceHome.class);
            adBranchBankAccountHome = (LocalAdBranchBankAccountHome) EJBHomeFactory.lookUpLocalHome(LocalAdBranchBankAccountHome.JNDI_NAME, LocalAdBranchBankAccountHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            // validate if misc receipt is already deleted

            try {

                if (details.getRctCode() != null) {

                    arReceipt = arReceiptHome.findByPrimaryKey(details.getRctCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if misc receipt is already posted, void, approved or pending

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

            // misc receipt is void

            if (details.getRctCode() != null && details.getRctVoid() == EJBCommon.TRUE) {

                if (arReceipt.getRctVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }

                // check if misc receipt is applied for customer deposit

                if (arReceipt.getRctType().equals("MISC") && arReceipt.getRctCustomerDeposit() == EJBCommon.TRUE) {

                    if (arReceipt.getRctAppliedDeposit() > 0) {

                        throw new GlobalRecordAlreadyAssignedException();
                    }

                    double draftAppliedDeposit = 0d;

                    Collection arUnpostedAppliedInvoices = null;

                    try {

                        arUnpostedAppliedInvoices = arAppliedInvoiceHome.findUnpostedAiWithDepositByCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode(), AD_BRNCH, companyCode);

                    } catch (FinderException ex) {

                    }

                    Iterator i = arUnpostedAppliedInvoices.iterator();

                    while (i.hasNext()) {

                        LocalArAppliedInvoice arUnpostedAppliedInvoice = (LocalArAppliedInvoice) i.next();

                        draftAppliedDeposit += arUnpostedAppliedInvoice.getAiAppliedDeposit();
                    }

                    Collection arDepositReceipts = null;

                    try {

                        arDepositReceipts = arReceiptHome.findOpenDepositEnabledPostedRctByCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode(), companyCode);

                    } catch (FinderException ex) {

                    }

                    i = arDepositReceipts.iterator();

                    while (i.hasNext()) {

                        LocalArReceipt arDepositReceipt = (LocalArReceipt) i.next();

                        if (arDepositReceipt.getRctCode().equals(arReceipt.getRctCode()) && draftAppliedDeposit > 0) {

                            throw new GlobalRecordAlreadyAssignedException();
                        }

                        draftAppliedDeposit -= arDepositReceipt.getRctAmount() - arDepositReceipt.getRctAppliedDeposit();
                    }
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

                            LocalAdAmountLimit adAmountLimit = null;

                            try {

                                adAmountLimit = adAmountLimitHome.findByAdcTypeAndAuTypeAndUsrName("AR RECEIPT", "REQUESTER", details.getRctLastModifiedBy(), companyCode);

                            } catch (FinderException ex) {

                                throw new GlobalNoApprovalRequesterFoundException();
                            }

                            if (arReceipt.getRctAmount() <= adAmountLimit.getCalAmountLimit()) {

                                RCT_APPRVL_STATUS = "N/A";

                            } else {

                                // for approval, create approval queue

                                Collection adAmountLimits = adAmountLimitHome.findByAdcTypeAndGreaterThanCalAmountLimit("AR RECEIPT", adAmountLimit.getCalAmountLimit(), companyCode);

                                if (adAmountLimits.isEmpty()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                                    if (adApprovalUsers.isEmpty()) {

                                        throw new GlobalNoApprovalApproverFoundException();
                                    }

                                    for (Object approvalUser : adApprovalUsers) {

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, companyCode, EJBCommon.FALSE, adApprovalQueueHome, arReceipt.getRctCode(), arReceipt.getRctNumber(), arReceipt.getRctDate(), adAmountLimit, adApprovalUser);

                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                    }

                                } else {

                                    boolean isApprovalUsersFound = false;

                                    Iterator i = adAmountLimits.iterator();

                                    while (i.hasNext()) {

                                        LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();

                                        if (arReceipt.getRctAmount() <= adNextAmountLimit.getCalAmountLimit()) {

                                            Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                                            for (Object approvalUser : adApprovalUsers) {

                                                isApprovalUsersFound = true;

                                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                                LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, companyCode, EJBCommon.FALSE, adApprovalQueueHome, arReceipt.getRctCode(), arReceipt.getRctNumber(), arReceipt.getRctDate(), adAmountLimit, adApprovalUser);

                                                adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                            }

                                            break;

                                        } else if (!i.hasNext()) {

                                            Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adNextAmountLimit.getCalCode(), companyCode);

                                            for (Object approvalUser : adApprovalUsers) {

                                                isApprovalUsersFound = true;

                                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                                LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, companyCode, EJBCommon.FALSE, adApprovalQueueHome, arReceipt.getRctCode(), arReceipt.getRctNumber(), arReceipt.getRctDate(), adAmountLimit, adApprovalUser);

                                                adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                            }

                                            break;
                                        }

                                        adAmountLimit = adNextAmountLimit;
                                    }

                                    if (!isApprovalUsersFound) {

                                        throw new GlobalNoApprovalApproverFoundException();
                                    }
                                }

                                RCT_APPRVL_STATUS = "PENDING";
                            }
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

                        this.addArDrEntryMisc(arReceipt.getArDrNextLine(), arDistributionRecord.getDrClass(), arDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, arDistributionRecord.getDrAmount(), arDistributionRecord.getGlChartOfAccount().getCoaCode(), EJBCommon.TRUE, arReceipt, AD_BRNCH, companyCode);
                    }

                    LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

                    if (RCT_APPRVL_STATUS != null && RCT_APPRVL_STATUS.equals("N/A") && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                        arReceipt.setRctVoid(EJBCommon.TRUE);
                        this.executeArRctPost(arReceipt.getRctCode(), details.getRctLastModifiedBy(), AD_BRNCH, companyCode);
                    }

                    // set void approval status

                    arReceipt.setRctVoidApprovalStatus(RCT_APPRVL_STATUS);
                }

                arReceipt.setRctVoid(EJBCommon.TRUE);
                arReceipt.setRctLastModifiedBy(details.getRctLastModifiedBy());
                arReceipt.setRctDateLastModified(details.getRctDateLastModified());

                return arReceipt.getRctCode();
            }

            // validate if receipt number is unique receipt number is automatic then set
            // next sequence

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            if (details.getRctCode() == null) {

                try {

                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AR RECEIPT", companyCode);

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

            // validate if conversion date exists

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

            // used in checking if receipt should re-generate distribution records and
            // re-calculate taxes
            boolean isRecalculate = true;

            // create misc receipt

            if (details.getRctCode() == null) {

                arReceipt = arReceiptHome.create("MISC", details.getRctDescription(), details.getRctDate(), details.getRctNumber(), details.getRctReferenceNumber(), details.getRctCheckNo(), details.getRctPayfileReferenceNumber(), details.getRctChequeNumber(), null, null, null, null, 0d, 0d, 0d, 0d, 0d, 0d, 0d, details.getRctConversionDate(), details.getRctConversionRate(), details.getRctSoldTo(), details.getRctPaymentMethod(), details.getRctCustomerDeposit(), 0d, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, null, null, null, null, null, details.getRctCreatedBy(), details.getRctDateCreated(), details.getRctLastModifiedBy(), details.getRctDateLastModified(), null, null, null, null, EJBCommon.FALSE, null, EJBCommon.FALSE, details.getRctSubjectToCommission(), null, null, details.getRctInvtrBeginningBalance(), details.getRctInvtrInvestorFund(), details.getRctInvtrNextRunDate(), AD_BRNCH, companyCode);

            } else {

                // check if critical fields are changed

                if (!arReceipt.getArTaxCode().getTcName().equals(TC_NM) || !arReceipt.getArWithholdingTaxCode().getWtcName().equals(WTC_NM) || !arReceipt.getArCustomer().getCstCustomerCode().equals(CST_CSTMR_CODE) || !arReceipt.getAdBankAccount().getBaName().equals(BA_NM) || ilList.size() != arReceipt.getArInvoiceLines().size()) {

                    isRecalculate = true;

                } else if (ilList.size() == arReceipt.getArInvoiceLines().size()) {

                    Iterator ilIter = arReceipt.getArInvoiceLines().iterator();
                    Iterator ilListIter = ilList.iterator();

                    while (ilIter.hasNext()) {

                        LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) ilIter.next();
                        ArModInvoiceLineDetails mdetails = (ArModInvoiceLineDetails) ilListIter.next();

                        if (!arInvoiceLine.getArStandardMemoLine().getSmlName().equals(mdetails.getIlSmlName()) || arInvoiceLine.getIlQuantity() != mdetails.getIlQuantity() || arInvoiceLine.getIlUnitPrice() != mdetails.getIlUnitPrice() || arInvoiceLine.getIlTax() != mdetails.getIlTax()) {

                            isRecalculate = true;
                            break;
                        }

                        isRecalculate = false;
                    }

                } else {

                    isRecalculate = false;
                }

                arReceipt.setRctDescription(details.getRctDescription());
                arReceipt.setRctDate(details.getRctDate());
                arReceipt.setRctNumber(details.getRctNumber());
                arReceipt.setRctReferenceNumber(details.getRctReferenceNumber());
                arReceipt.setRctConversionDate(details.getRctConversionDate());
                arReceipt.setRctConversionRate(details.getRctConversionRate());
                arReceipt.setRctSoldTo(details.getRctSoldTo());
                arReceipt.setRctPaymentMethod(details.getRctPaymentMethod());
                arReceipt.setRctCustomerDeposit(details.getRctCustomerDeposit());
                arReceipt.setRctLastModifiedBy(details.getRctLastModifiedBy());
                arReceipt.setRctDateLastModified(details.getRctDateLastModified());
                arReceipt.setRctReasonForRejection(null);
                arReceipt.setRctSubjectToCommission(details.getRctSubjectToCommission());
                arReceipt.setRctCustomerName(null);
                arReceipt.setRctInvtrInvestorFund(details.getRctInvtrInvestorFund());
                arReceipt.setRctInvtrNextRunDate(details.getRctInvtrNextRunDate());
            }

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);
            // adBankAccount.addArReceipt(arReceipt);
            arReceipt.setAdBankAccount(adBankAccount);
            arReceipt.setGlInvestorAccountBalance(glInvestorAccountBalance);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
            // glFunctionalCurrency.addArReceipt(arReceipt);
            arReceipt.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, companyCode);
            // arCustomer.addArReceipt(arReceipt);
            arReceipt.setArCustomer(arCustomer);

            if (details.getRctCustomerName().length() > 0 && !arCustomer.getCstName().equals(details.getRctCustomerName())) {
                arReceipt.setRctCustomerName(details.getRctCustomerName());
            }

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(TC_NM, companyCode);
            // arTaxCode.addArReceipt(arReceipt);
            arReceipt.setArTaxCode(arTaxCode);

            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(WTC_NM, companyCode);
            // arWithholdingTaxCode.addArReceipt(arReceipt);
            arReceipt.setArWithholdingTaxCode(arWithholdingTaxCode);

            LocalArSalesperson arSalesperson = null;

            if (SLP_SLSPRSN_CODE != null && SLP_SLSPRSN_CODE.length() > 0 && !SLP_SLSPRSN_CODE.equalsIgnoreCase("NO RECORD FOUND")) {

                // if he tagged a salesperson for this receipt
                arSalesperson = arSalespersonHome.findBySlpSalespersonCode(SLP_SLSPRSN_CODE, companyCode);
                // arSalesperson.addArReceipt(arReceipt);
                arReceipt.setArSalesperson(arSalesperson);

            } else {

                // if he untagged a salesperson for this receipt
                if (arReceipt.getArSalesperson() != null) {

                    arSalesperson = arSalespersonHome.findBySlpSalespersonCode(arReceipt.getArSalesperson().getSlpSalespersonCode(), companyCode);
                    arSalesperson.dropArReceipt(arReceipt);
                }

                // if no salesperson is set, invoice should not be subject to commission
                arReceipt.setRctSubjectToCommission((byte) 0);
            }

            try {

                LocalArReceiptBatch arReceiptBatch = arReceiptBatchHome.findByRbName(RB_NM, AD_BRNCH, companyCode);
                // arReceiptBatch.addArReceipt(arReceipt);
                arReceipt.setArReceiptBatch(arReceiptBatch);

            } catch (FinderException ex) {

            }

            if (isRecalculate) {

                // remove all voucher line items

                Collection arInvoiceLineItems = arReceipt.getArInvoiceLineItems();

                Iterator i = arInvoiceLineItems.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) i.next();

                    double convertedQuantity = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);
                    arInvoiceLineItem.getInvItemLocation().setIlCommittedQuantity(arInvoiceLineItem.getInvItemLocation().getIlCommittedQuantity() - convertedQuantity);

                    i.remove();

                    // arInvoiceLineItem.entityRemove();
                    em.remove(arInvoiceLineItem);
                }

                // remove all invoice lines

                Collection arInvoiceLines = arReceipt.getArInvoiceLines();

                i = arInvoiceLines.iterator();

                while (i.hasNext()) {

                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) i.next();

                    i.remove();

                    // arInvoiceLine.entityRemove();
                    em.remove(arInvoiceLine);
                }

                // remove all distribution records

                Collection arDistributionRecords = arReceipt.getArDistributionRecords();

                i = arDistributionRecords.iterator();

                while (i.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                    i.remove();

                    // arDistributionRecord.entityRemove();
                    em.remove(arDistributionRecord);
                }

                // add new invoice lines and distribution record

                double TOTAL_TAX = 0d;
                double TOTAL_LINE = 0d;
                double TOTAL_UNTAXABLE = 0d;

                i = ilList.iterator();

                while (i.hasNext()) {

                    ArModInvoiceLineDetails mInvDetails = (ArModInvoiceLineDetails) i.next();

                    LocalArInvoiceLine arInvoiceLine = this.addArIlEntryMisc(mInvDetails, arReceipt, companyCode);

                    // add revenue/credit distributions

                    this.addArDrEntryMisc(arReceipt.getArDrNextLine(), "REVENUE", EJBCommon.FALSE, arInvoiceLine.getIlAmount(), this.getArGlCoaRevenueAccount(arInvoiceLine, AD_BRNCH, companyCode), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                    TOTAL_LINE += arInvoiceLine.getIlAmount();
                    TOTAL_TAX += arInvoiceLine.getIlTaxAmount();

                    if (arInvoiceLine.getIlTax() == EJBCommon.FALSE) {
                        TOTAL_UNTAXABLE += arInvoiceLine.getIlAmount();
                    }
                }

                // add tax distribution if necessary

                if (!arTaxCode.getTcType().equals("NONE") && !arTaxCode.getTcType().equals("EXEMPT")) {
                    // add branch tax code
                    LocalAdBranchArTaxCode adBranchTaxCode = null;
                    Debug.print("ArInvoiceEntryControllerBean saveArInvIliEntry J");
                    try {
                        adBranchTaxCode = adBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(arReceipt.getArTaxCode().getTcCode(), AD_BRNCH, companyCode);

                    } catch (FinderException ex) {

                    }

                    if (adBranchTaxCode != null) {
                        this.addArDrEntryMisc(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, adBranchTaxCode.getBtcGlCoaTaxCode(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                    } else {

                        this.addArDrEntryMisc(arReceipt.getArDrNextLine(), "TAX", EJBCommon.FALSE, TOTAL_TAX, arReceipt.getArTaxCode().getGlChartOfAccount().getCoaCode(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                    }
                }

                // add wtax distribution if necessary

                double W_TAX_AMOUNT = 0d;

                if (arWithholdingTaxCode.getWtcRate() != 0) {

                    W_TAX_AMOUNT = EJBCommon.roundIt((TOTAL_LINE - TOTAL_UNTAXABLE) * (arWithholdingTaxCode.getWtcRate() / 100), this.getGlFcPrecisionUnit(companyCode));

                    this.addArDrEntryMisc(arReceipt.getArDrNextLine(), "W-TAX", EJBCommon.TRUE, W_TAX_AMOUNT, arWithholdingTaxCode.getGlChartOfAccount().getCoaCode(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                }

                // add cash distribution
                LocalAdBranchBankAccount adBranchBankAccount = null;
                Debug.print("ArInvoiceEntryControllerBean saveArInvIliEntry J");
                try {
                    adBranchBankAccount = adBranchBankAccountHome.findBbaByBaCodeAndBrCode(arReceipt.getAdBankAccount().getBaCode(), AD_BRNCH, companyCode);

                } catch (FinderException ex) {

                }

                if (adBranchBankAccount != null) {

                    this.addArDrEntryMisc(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT, adBranchBankAccount.getBbaGlCoaCashAccount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);

                } else {

                    this.addArDrEntryMisc(arReceipt.getArDrNextLine(), "CASH", EJBCommon.TRUE, TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT, arReceipt.getAdBankAccount().getBaCoaGlCashAccount(), EJBCommon.FALSE, arReceipt, AD_BRNCH, companyCode);
                }

                // set receipt amount

                arReceipt.setRctAmount(TOTAL_LINE + TOTAL_TAX - W_TAX_AMOUNT);
            }

            // generate approval status

            String RCT_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if ar receipt approval is enabled

                if (adApproval.getAprEnableArReceipt() == EJBCommon.FALSE) {

                    RCT_APPRVL_STATUS = "N/A";

                } else {

                    // check if receipt is self approved

                    LocalAdAmountLimit adAmountLimit = null;

                    try {

                        adAmountLimit = adAmountLimitHome.findByAdcTypeAndAuTypeAndUsrName("AR RECEIPT", "REQUESTER", details.getRctLastModifiedBy(), companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalNoApprovalRequesterFoundException();
                    }

                    if (arReceipt.getRctAmount() <= adAmountLimit.getCalAmountLimit()) {

                        RCT_APPRVL_STATUS = "N/A";

                    } else {

                        // for approval, create approval queue

                        Collection adAmountLimits = adAmountLimitHome.findByAdcTypeAndGreaterThanCalAmountLimit("AR RECEIPT", adAmountLimit.getCalAmountLimit(), companyCode);

                        if (adAmountLimits.isEmpty()) {

                            Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                            if (adApprovalUsers.isEmpty()) {

                                throw new GlobalNoApprovalApproverFoundException();
                            }

                            for (Object approvalUser : adApprovalUsers) {

                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, companyCode, EJBCommon.FALSE, adApprovalQueueHome, arReceipt.getRctCode(), arReceipt.getRctNumber(), arReceipt.getRctDate(), adAmountLimit, adApprovalUser);

                                adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                            }

                        } else {

                            boolean isApprovalUsersFound = false;

                            Iterator i = adAmountLimits.iterator();

                            while (i.hasNext()) {

                                LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();

                                if (arReceipt.getRctAmount() <= adNextAmountLimit.getCalAmountLimit()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, companyCode, EJBCommon.FALSE, adApprovalQueueHome, arReceipt.getRctCode(), arReceipt.getRctNumber(), arReceipt.getRctDate(), adAmountLimit, adApprovalUser);

                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                    }

                                    break;

                                } else if (!i.hasNext()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adNextAmountLimit.getCalCode(), companyCode);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, companyCode, EJBCommon.FALSE, adApprovalQueueHome, arReceipt.getRctCode(), arReceipt.getRctNumber(), arReceipt.getRctDate(), adAmountLimit, adApprovalUser);

                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                    }

                                    break;
                                }

                                adAmountLimit = adNextAmountLimit;
                            }

                            if (!isApprovalUsersFound) {

                                throw new GlobalNoApprovalApproverFoundException();
                            }
                        }

                        RCT_APPRVL_STATUS = "PENDING";
                    }
                }
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (RCT_APPRVL_STATUS != null && RCT_APPRVL_STATUS.equals("N/A") && adPreference.getPrfArGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeArRctPost(arReceipt.getRctCode(), arReceipt.getRctLastModifiedBy(), AD_BRNCH, companyCode);
            }

            // set receipt approval status

            arReceipt.setRctApprovalStatus(RCT_APPRVL_STATUS);

            return arReceipt.getRctCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalRecordAlreadyAssignedException |
                 GlobalBranchAccountNumberInvalidException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException | GlJREffectiveDateNoPeriodExistException |
                 GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
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

    private void executeArRctPost(Integer RCT_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException {

        Debug.print("ArMiscReceiptEntryControllerBean executeArRctPost");

        LocalArReceiptHome arReceiptHome = null;
        LocalGlJournalHome glJournalHome = null;
        LocalGlJournalBatchHome glJournalBatchHome = null;
        LocalGlSuspenseAccountHome glSuspenseAccountHome = null;
        LocalGlJournalLineHome glJournalLineHome = null;
        LocalGlJournalSourceHome glJournalSourceHome = null;
        LocalGlJournalCategoryHome glJournalCategoryHome = null;
        LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome = null;
        LocalArDistributionRecordHome arDistributionRecordHome = null;
        LocalInvCostingHome invCostingHome = null;
        LocalInvItemHome invItemHome = null;
        LocalAdBankAccountHome adBankAccountHome = null;
        LocalAdBankAccountBalanceHome adBankAccountBalanceHome = null;
        LocalGlForexLedgerHome glForexLedgerHome = null;
        LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome = null;

        LocalArReceipt arReceipt = null;

        // Initialize EJB Home

        try {

            arReceiptHome = (LocalArReceiptHome) EJBHomeFactory.lookUpLocalHome(LocalArReceiptHome.JNDI_NAME, LocalArReceiptHome.class);
            glJournalHome = (LocalGlJournalHome) EJBHomeFactory.lookUpLocalHome(LocalGlJournalHome.JNDI_NAME, LocalGlJournalHome.class);
            glJournalBatchHome = (LocalGlJournalBatchHome) EJBHomeFactory.lookUpLocalHome(LocalGlJournalBatchHome.JNDI_NAME, LocalGlJournalBatchHome.class);
            glSuspenseAccountHome = (LocalGlSuspenseAccountHome) EJBHomeFactory.lookUpLocalHome(LocalGlSuspenseAccountHome.JNDI_NAME, LocalGlSuspenseAccountHome.class);
            glJournalLineHome = (LocalGlJournalLineHome) EJBHomeFactory.lookUpLocalHome(LocalGlJournalLineHome.JNDI_NAME, LocalGlJournalLineHome.class);
            glJournalSourceHome = (LocalGlJournalSourceHome) EJBHomeFactory.lookUpLocalHome(LocalGlJournalSourceHome.JNDI_NAME, LocalGlJournalSourceHome.class);
            glJournalCategoryHome = (LocalGlJournalCategoryHome) EJBHomeFactory.lookUpLocalHome(LocalGlJournalCategoryHome.JNDI_NAME, LocalGlJournalCategoryHome.class);
            glFunctionalCurrencyHome = (LocalGlFunctionalCurrencyHome) EJBHomeFactory.lookUpLocalHome(LocalGlFunctionalCurrencyHome.JNDI_NAME, LocalGlFunctionalCurrencyHome.class);
            arDistributionRecordHome = (LocalArDistributionRecordHome) EJBHomeFactory.lookUpLocalHome(LocalArDistributionRecordHome.JNDI_NAME, LocalArDistributionRecordHome.class);
            invCostingHome = (LocalInvCostingHome) EJBHomeFactory.lookUpLocalHome(LocalInvCostingHome.JNDI_NAME, LocalInvCostingHome.class);
            invItemHome = (LocalInvItemHome) EJBHomeFactory.lookUpLocalHome(LocalInvItemHome.JNDI_NAME, LocalInvItemHome.class);
            adBankAccountHome = (LocalAdBankAccountHome) EJBHomeFactory.lookUpLocalHome(LocalAdBankAccountHome.JNDI_NAME, LocalAdBankAccountHome.class);
            adBankAccountBalanceHome = (LocalAdBankAccountBalanceHome) EJBHomeFactory.lookUpLocalHome(LocalAdBankAccountBalanceHome.JNDI_NAME, LocalAdBankAccountBalanceHome.class);
            glForexLedgerHome = (LocalGlForexLedgerHome) EJBHomeFactory.lookUpLocalHome(LocalGlForexLedgerHome.JNDI_NAME, LocalGlForexLedgerHome.class);
            glFunctionalCurrencyRateHome = (LocalGlFunctionalCurrencyRateHome) EJBHomeFactory.lookUpLocalHome(LocalGlFunctionalCurrencyRateHome.JNDI_NAME, LocalGlFunctionalCurrencyRateHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            // validate if receipt is already deleted

            try {

                arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if receipt is already posted

            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

                // validate if receipt void is already posted

            } else if (arReceipt.getRctVoid() == EJBCommon.TRUE && arReceipt.getRctVoidPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidPostedException();
            }

            // post receipt

            if (arReceipt.getRctVoid() == EJBCommon.FALSE && arReceipt.getRctPosted() == EJBCommon.FALSE) {

                if (arReceipt.getRctType().equals("MISC") && !arReceipt.getArInvoiceLineItems().isEmpty()) {

                    for (Object o : arReceipt.getArInvoiceLineItems()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) o;

                        String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), II_NM, LOC_NM, AD_BRNCH, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        if (invCosting == null) {

                            this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, COST * QTY_SLD, -QTY_SLD, -COST * QTY_SLD, 0d, null, AD_BRNCH, companyCode);
                        } else {

                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case "Average":

                                    double avgCost = invCosting.getCstRemainingQuantity() == 0 ? COST : Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, avgCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (avgCost * QTY_SLD), 0d, null, AD_BRNCH, companyCode);

                                    break;
                                case "FIFO":

                                    double fifoCost = invCosting.getCstRemainingQuantity() == 0 ? COST : this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), QTY_SLD, arInvoiceLineItem.getIliUnitPrice(), true, AD_BRNCH, companyCode);

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, fifoCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (fifoCost * QTY_SLD), 0d, null, AD_BRNCH, companyCode);

                                    break;
                                case "Standard":

                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), QTY_SLD, standardCost * QTY_SLD, invCosting.getCstRemainingQuantity() - QTY_SLD, invCosting.getCstRemainingValue() - (standardCost * QTY_SLD), 0d, null, AD_BRNCH, companyCode);
                                    break;
                            }
                        }
                    }
                }

                // increase bank balance CASH
                if (arReceipt.getRctAmountCash() > 0) {

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

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCash(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCash());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCash()=" + arReceipt.getRctAmountCash());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCash()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCash());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // increase bank balance CARD 1
                if (arReceipt.getRctAmountCard1() > 0) {

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard1().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard1()=" + arReceipt.getRctAmountCard1());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCard1()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard1());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // increase bank balance CARD 2
                if (arReceipt.getRctAmountCard2() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard2().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard2(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard2());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard2()=" + arReceipt.getRctAmountCard2());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCard2()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard2());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // increase bank balance CARD 3
                if (arReceipt.getRctAmountCard3() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard3().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard3()=" + arReceipt.getRctAmountCard3());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCard3()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
                // increase bank balance CHEQUE
                if (arReceipt.getRctAmountCheque() > 0) {
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

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCheque(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCheque());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCheque()=" + arReceipt.getRctAmountCheque());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (arReceipt.getRctAmountCheque()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCheque());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // set receipt post status

                arReceipt.setRctPosted(EJBCommon.TRUE);
                arReceipt.setRctPostedBy(USR_NM);
                arReceipt.setRctDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            } else if (arReceipt.getRctVoid() == EJBCommon.TRUE && arReceipt.getRctVoidPosted() == EJBCommon.FALSE) { // void
                // receipt
                // VOIDING MISC RECEIPT
                if (arReceipt.getRctType().equals("MISC") && !arReceipt.getArInvoiceLineItems().isEmpty()) {

                    for (Object o : arReceipt.getArInvoiceLineItems()) {

                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) o;

                        String II_NM = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiName();
                        String LOC_NM = arInvoiceLineItem.getInvItemLocation().getInvLocation().getLocName();

                        double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), companyCode);

                        LocalInvCosting invCosting = null;

                        try {

                            invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(arReceipt.getRctDate(), II_NM, LOC_NM, AD_BRNCH, companyCode);

                        } catch (FinderException ex) {

                        }

                        double COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();

                        if (invCosting == null) {

                            this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), -QTY_SLD, -COST * QTY_SLD, QTY_SLD, COST * QTY_SLD, 0d, null, AD_BRNCH, companyCode);

                        } else {

                            switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                                case "Average":

                                    double avgCost = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), -QTY_SLD, -avgCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (avgCost * QTY_SLD), 0d, USR_NM, AD_BRNCH, companyCode);

                                    break;
                                case "FIFO":

                                    double fifoCost = this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), QTY_SLD, arInvoiceLineItem.getIliUnitPrice(), true, AD_BRNCH, companyCode);

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), -QTY_SLD, -fifoCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (fifoCost * QTY_SLD), 0d, USR_NM, AD_BRNCH, companyCode);

                                    break;
                                case "Standard":

                                    double standardCost = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();

                                    this.postToInv(arInvoiceLineItem, arReceipt.getRctDate(), -QTY_SLD, -standardCost * QTY_SLD, invCosting.getCstRemainingQuantity() + QTY_SLD, invCosting.getCstRemainingValue() + (standardCost * QTY_SLD), 0d, USR_NM, AD_BRNCH, companyCode);
                                    break;
                            }
                        }
                    }
                }

                // decrease bank balance cash

                if (arReceipt.getRctAmountCash() > 0) {

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

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCash(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCash());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCash()=" + arReceipt.getRctAmountCash());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCash()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCash());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // decrease bank balance CARD 1
                if (arReceipt.getRctAmountCard1() > 0) {

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard1().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard1(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard1());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard1()=" + arReceipt.getRctAmountCard1());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCard1()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard1());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // decrease bank balance CARD 2
                if (arReceipt.getRctAmountCard2() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard2().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard2(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard2());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard2()=" + arReceipt.getRctAmountCard2());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCard2()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard2());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // decrease bank balance CARD 3
                if (arReceipt.getRctAmountCard3() > 0) {
                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard3().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(arReceipt.getRctDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard3(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + arReceipt.getRctAmountCard3());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCard3()=" + arReceipt.getRctAmountCard3());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCard3()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCard3());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
                // decrease bank balance CHEQUE
                if (arReceipt.getRctAmountCheque() > 0) {
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

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCheque(), "BOOK", companyCode);

                                // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                                adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCheque());
                            }

                        } else {

                            // create new balance
                            Debug.print("arReceipt.getRctAmountCheque()=" + arReceipt.getRctAmountCheque());
                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(arReceipt.getRctDate(), (-arReceipt.getRctAmountCheque()), "BOOK", companyCode);

                            // adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                            adNewBankAccountBalance.setAdBankAccount(adBankAccount);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(arReceipt.getRctDate(), arReceipt.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - arReceipt.getRctAmountCheque());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }

                // set receipt post status

                arReceipt.setRctPosted(EJBCommon.TRUE);
                arReceipt.setRctPostedBy(USR_NM);
                arReceipt.setRctDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
            }

            // post to gl if necessary

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

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arDistributionRecord.getDrAmount(), companyCode);
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
                    // glChartOfAccount.addGlJournalLine(glOffsetJournalLine);
                    glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

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

                while (j.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) j.next();

                    double DR_AMNT = 0d;

                    LocalArInvoice arInvoice = null;

                    if (arDistributionRecord.getArAppliedInvoice() != null) {

                        arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), companyCode);

                    } else {

                        DR_AMNT = this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arDistributionRecord.getDrAmount(), companyCode);
                    }

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(arDistributionRecord.getDrLine(), arDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    // arDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);
                    glJournalLine.setGlChartOfAccount(arDistributionRecord.getGlChartOfAccount());

                    // glJournal.addGlJournalLine(glJournalLine);
                    glJournalLine.setGlJournal(glJournal);

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

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        } else {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                        }

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        glForexLedger = glForexLedgerHome.create(DATE, FRL_LN, "OR", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, 0d, companyCode);

                        // glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);
                        glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

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

                if (arReceipt.getArCustomer().getApSupplier() != null) {

                    // Post Investors Account balance
                    String supplierClass = arReceipt.getArCustomer().getApSupplier().getApSupplierClass().getScName();
                    // post current to current acv
                    if (supplierClass.contains("Investors")) {
                        this.postToGlInvestor(glAccountingCalendarValue, arReceipt.getArCustomer().getApSupplier(), true, (byte) 0, arReceipt.getRctAmount(), companyCode);

                        // post to subsequent acvs (propagate)

                        Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                        for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                            this.postToGlInvestor(glSubsequentAccountingCalendarValue, arReceipt.getArCustomer().getApSupplier(), false, (byte) 0, arReceipt.getRctAmount(), companyCode);
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

                                    this.postToGlInvestor(glSubsequentAccountingCalendarValue, arReceipt.getArCustomer().getApSupplier(), false, (byte) 0, arReceipt.getRctAmount(), companyCode);
                                }

                                if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }

        } catch (GlJREffectiveDateNoPeriodExistException | GlobalExpiryDateNotFoundException |
                 GlobalTransactionAlreadyVoidPostedException | GlobalTransactionAlreadyPostedException |
                 GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalArInvoiceLine addArIlEntryMisc(ArModInvoiceLineDetails mdetails, LocalArReceipt arReceipt, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean addArIlEntry");

        LocalArInvoiceLineHome arInvoiceLineHome = null;
        LocalArStandardMemoLineHome arStandardMemoLineHome = null;

        // Initialize EJB Home

        try {

            arInvoiceLineHome = (LocalArInvoiceLineHome) EJBHomeFactory.lookUpLocalHome(LocalArInvoiceLineHome.JNDI_NAME, LocalArInvoiceLineHome.class);
            arStandardMemoLineHome = (LocalArStandardMemoLineHome) EJBHomeFactory.lookUpLocalHome(LocalArStandardMemoLineHome.JNDI_NAME, LocalArStandardMemoLineHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode);

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

            LocalArInvoiceLine arInvoiceLine = arInvoiceLineHome.create(mdetails.getIlDescription(), mdetails.getIlQuantity(), mdetails.getIlUnitPrice(), IL_AMNT, IL_TAX_AMNT, mdetails.getIlTax(), companyCode);

            // arReceipt.addArInvoiceLine(arInvoiceLine);
            arInvoiceLine.setArReceipt(arReceipt);

            LocalArStandardMemoLine arStandardMemoLine = arStandardMemoLineHome.findBySmlName(mdetails.getIlSmlName(), companyCode);
            // arStandardMemoLine.addArInvoiceLine(arInvoiceLine);
            arInvoiceLine.setArStandardMemoLine(arStandardMemoLine);

            return arInvoiceLine;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addArDrEntryMisc(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, byte DR_RVRSD, LocalArReceipt arReceipt, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ArMiscReceiptEntryControllerBean addArDrEntry");

        LocalArDistributionRecordHome arDistributionRecordHome = null;

        // Initialize EJB Home

        try {

            arDistributionRecordHome = (LocalArDistributionRecordHome) EJBHomeFactory.lookUpLocalHome(LocalArDistributionRecordHome.JNDI_NAME, LocalArDistributionRecordHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }
        Debug.print("COA_CODE1: " + COA_CODE);
        try {

            // get company

            Debug.print("COA_CODE:2 " + COA_CODE);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, companyCode);

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, DR_RVRSD, companyCode);

            // arReceipt.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setArReceipt(arReceipt);
            // glChartOfAccount.addArDistributionRecord(arDistributionRecord);

            System.out.print(glChartOfAccount.toString());

            arDistributionRecord.setGlChartOfAccount(glChartOfAccount);

        } catch (FinderException ex) {

            throw new GlobalBranchAccountNumberInvalidException(ex.getMessage());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postToGlInvestor(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalApSupplier apSupplier, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean postToGlInvestor");

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

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalAdApprovalQueue createApprovalQueue(Integer branchCode, Integer companyCode, byte isInvoice, LocalAdApprovalQueueHome adApprovalQueueHome, Integer documentCode, String documentNumber, Date documentDate, LocalAdAmountLimit adAmountLimit, LocalAdApprovalUser adApprovalUser) throws CreateException {

        String[] document = {"AR INVOICE", "AR RECEIPT"};
        String selectedDocument = (isInvoice == EJBCommon.TRUE) ? document[0] : document[1];
        return adApprovalQueueHome.AqForApproval(EJBCommon.TRUE).AqDocument(selectedDocument).AqDocumentCode(documentCode).AqDocumentNumber(documentNumber).AqDate(documentDate).AqAndOr(adAmountLimit.getCalAndOr()).AqUserOr(adApprovalUser.getAuOr()).AqAdBranch(branchCode).AqAdCompany(companyCode).buildApprovalQueue();

    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArStandardMemoLineControllerBean ejbCreate");
    }

}