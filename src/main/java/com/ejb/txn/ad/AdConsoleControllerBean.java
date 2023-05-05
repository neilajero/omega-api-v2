package com.ejb.txn.ad;

import java.nio.charset.StandardCharsets;
import java.util.*;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ap.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.gen.*;
import com.ejb.dao.gl.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.gen.*;
import com.ejb.entities.gl.*;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.gen.GenSegmentDetails;
import com.util.gen.GenValueSetValueDetails;
import com.util.gl.GlChartOfAccountDetails;

@Stateless(name = "AdConsoleControllerEJB")
public class AdConsoleControllerBean extends EJBContextClass implements AdConsoleController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalGenFieldHome genFieldHome;
    @EJB
    private LocalGenValueSetHome genValueSetHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;
    @EJB
    private LocalGenQualifierHome genQualifierHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdApplicationHome adApplicationHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalAdDocumentCategoryHome adDocumentCategoryHome;
    @EJB
    private LocalAdDocumentSequenceHome adDocumentSequenceHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalAdBankHome adBankHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdBranchBankAccountHome adBranchBankAccountHome;
    @EJB
    private LocalAdResponsibilityHome adResponsibilityHome;
    @EJB
    private LocalAdUserResponsibilityHome adUserResponsibilityHome;
    @EJB
    private LocalAdFormFunctionHome adFormFunctionHome;
    @EJB
    private LocalAdLookUpHome adLookUpHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdStoredProcedureHome adStoredProcedureHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalAdPaymentScheduleHome adPaymentScheduleHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdBranchCoaHome adBranchCoaHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;
    @EJB
    private LocalApWithholdingTaxCodeHome apWithholdingTaxCodeHome;
    @EJB
    private LocalApSupplierClassHome apSupplierClassHome;
    @EJB
    private LocalArAutoAccountingHome arAutoAccountingHome;
    @EJB
    private LocalArAutoAccountingSegmentHome arAutoAccountingSegmentHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalArCustomerClassHome arCustomerClassHome;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;
    @EJB
    private LocalGlOrganizationHome glOrganizationHome;
    @EJB
    private LocalGlAccountRangeHome glAccountRangeHome;
    @EJB
    private LocalGlResponsibilityHome glResponsibilityHome;
    @EJB
    private LocalGlPeriodTypeHome glPeriodTypeHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;

    public void executeAdConsole(String companyName, String companyShortName, String companyWelcomeNote, short numberOfSegment, String segmentSeparator, String companyRetainedEarningsAccount, ArrayList genSegmentList, ArrayList genValueSetValueList, ArrayList glCoaList) throws GlobalRecordAlreadyExistException, GlobalRecordInvalidException {
        Debug.print("AdConsoleControllerBean executeAdConsole");

        try {

            // Validate if the Company details exists
            validateCompanyDetails(companyName, companyShortName, adCompanyHome);

            // This is a default parameter that is required in setting up company
            if (companyRetainedEarningsAccount == null) throw new GlobalRecordInvalidException();

            // Create Company
            LocalAdCompany adCompany = adCompanyHome.CompanyName(companyName).CompanyShortName(companyShortName).WelcomeNote(companyWelcomeNote).RetainedEarnings(companyRetainedEarningsAccount).buildCompany();

            Integer companyCode = adCompany.getCmpCode();

            // Create Gen Field
            LocalGenField genField = genFieldHome.create(companyName, companyName, segmentSeparator.charAt(0), numberOfSegment, EJBCommon.FALSE, EJBCommon.TRUE, companyCode);

            genField.addAdCompany(adCompany);

            // Create Gen Qualifiers
            LocalGenQualifier genAssetQualifier = genQualifierHome.create("ASSET", EJBCommon.TRUE, EJBCommon.TRUE, companyCode);
            LocalGenQualifier genLiabilityQualifier = genQualifierHome.create("LIABILITY", EJBCommon.TRUE, EJBCommon.TRUE, companyCode);
            LocalGenQualifier genEquityQualifier = genQualifierHome.create("OWNERS EQUITY", EJBCommon.TRUE, EJBCommon.TRUE, companyCode);
            LocalGenQualifier genRevenueQualifier = genQualifierHome.create("REVENUE", EJBCommon.TRUE, EJBCommon.TRUE, companyCode);
            LocalGenQualifier genExpenseQualifier = genQualifierHome.create("EXPENSE", EJBCommon.TRUE, EJBCommon.TRUE, companyCode);

            // Process Segments Details from the UI
            Iterator segmentIter = genSegmentList.iterator();

            ArrayList<LocalAdBranch> branches = new ArrayList<>();
            String headOfficeCode = "";
            byte isBranch = 1;

            while (segmentIter.hasNext()) {

                GenSegmentDetails segmentDetails = (GenSegmentDetails) segmentIter.next();

                // Create Gen Value Set
                LocalGenValueSet genValueSet = genValueSetHome.create(segmentDetails.getSgName(), segmentDetails.getSgDescription(), EJBCommon.TRUE, companyCode);

                // Create Gen Segment
                LocalGenSegment genSegment = genSegmentHome.create(segmentDetails.getSgName(), segmentDetails.getSgDescription(), segmentDetails.getSgMaxSize(), "0", segmentDetails.getSgSegmentType(), segmentDetails.getSgSegmentNumber(), companyCode);

                genField.addGenSegment(genSegment);
                genValueSet.addGenSegment(genSegment);

                // Process Segment File from CSV

                for (Object o : genValueSetValueList) {

                    GenValueSetValueDetails vsvDetails = (GenValueSetValueDetails) o;

                    // Segment number from the UI should be match with CSV file
                    // vsvDetails.getVsvSegmentNumber() -- segment number set from UI
                    // segmentDetails.getSgSegmentNumber() -- segment number set in CSV
                    if (vsvDetails.getVsvSegmentNumber() == segmentDetails.getSgSegmentNumber()) {

                        // Create Gen Value Set Value based on the Segment CSV file
                        LocalGenValueSetValue genValueSetValue = genValueSetValueHome.create(vsvDetails.getVsvValue(), vsvDetails.getVsvDescription(), EJBCommon.FALSE, (short) 0, EJBCommon.TRUE, companyCode);

                        genValueSet.addGenValueSetValue(genValueSetValue);

                        // Process only natural accounts per qualifier
                        if (segmentDetails.getSgSegmentType() == 'N') {

                            switch (vsvDetails.getVsvAccountType()) {
                                case "ASSET":
                                    genAssetQualifier.addGenValueSetValue(genValueSetValue);
                                    break;
                                case "LIABILITY":
                                    genLiabilityQualifier.addGenValueSetValue(genValueSetValue);
                                    break;
                                case "OWNERS EQUITY":
                                    genEquityQualifier.addGenValueSetValue(genValueSetValue);
                                    break;
                                case "REVENUE":
                                    genRevenueQualifier.addGenValueSetValue(genValueSetValue);
                                    break;
                                case "EXPENSE":
                                    genExpenseQualifier.addGenValueSetValue(genValueSetValue);
                                    break;
                            }
                        }

                        if (segmentDetails.getSgName().equalsIgnoreCase("BRANCH")) {

                            // Create Default Branch
                            LocalAdBranch adBranch = null;


                            if (isBranch == 1) {

                                headOfficeCode = vsvDetails.getVsvValue();

                            }

                            adBranch = adBranchHome.create(vsvDetails.getVsvValue(), vsvDetails.getVsvDescription(), "", "", isBranch, "", "", "", 'N', (byte) 0, 0, companyCode);
                            isBranch = 0;
                            branches.add(adBranch);
                        }
                    }
                }
            }

            // Integer AD_BRANCH = adBranch.getBrCode();

            // Process COA List from CSV
            // Bmobile COA Format country-branch-region/bts-account e.g. 1-01-0001-00001

            LocalGlChartOfAccount glFirstCoa = null;

            for (Object o : glCoaList) {

                GlChartOfAccountDetails coaDetails = (GlChartOfAccountDetails) o;

                StringBuilder COA_DESC = new StringBuilder();
                String COA_ACCNT_TYP = "";

                Debug.print("------------->segmentSeparator=" + segmentSeparator);

                StringTokenizer st = new StringTokenizer(coaDetails.getCoaAccountNumber(), segmentSeparator);
                Collection genSegments = genSegmentHome.findByFlCode(genField.getFlCode(), companyCode);
                Iterator j = genSegments.iterator();
                int a = 0;
                while (st.hasMoreTokens()) {
                    LocalGenSegment genSegment = (LocalGenSegment) j.next();

                    String tokenValue = st.nextToken();
                    Debug.print("------------->tokenValue=" + tokenValue);
                    Debug.print("------------->genSegment.getGenValueSet().getVsCode()=" + genSegment.getGenValueSet().getVsCode());
                    Debug.print("------------->genSegment.getSgSegmentType()=" + genSegment.getSgSegmentType());
                    LocalGenValueSetValue genValueSetValue = genValueSetValueHome.findByVsCodeAndVsvValue(genSegment.getGenValueSet().getVsCode(), tokenValue, companyCode);

                    if (genSegment.getSgSegmentType() == 'N')
                        COA_ACCNT_TYP = genValueSetValue.getGenQualifier().getQlAccountType();

                    COA_DESC.append(genValueSetValue.getVsvDescription());

                    if (st.hasMoreTokens()) {

                        COA_DESC.append(segmentSeparator);
                    }
                    a++; // Counter
                }

                // Build default COA
                LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.coaAccountNumber(coaDetails.getCoaAccountNumber()).coaAccountDesc(COA_DESC.toString()).coaAccountType(COA_ACCNT_TYP).coaDateFrom(coaDetails.getCoaDateFrom()).coaDateTo(coaDetails.getCoaDateTo()).coaSegment1(coaDetails.getCoaSegment1()).coaSegment2(coaDetails.getCoaSegment2()).coaSegment3(coaDetails.getCoaSegment3()).coaAdCompany(companyCode).buildCoa();

                if (glFirstCoa == null) glFirstCoa = glChartOfAccount;

                for (LocalAdBranch branch : branches) {
                    // Create branch COA
                    LocalAdBranchCoa adBranchCoa = adBranchCoaHome.create(adCompany.getCmpCode());
                    adBranchCoa.setGlChartOfAccount(glChartOfAccount);
                    adBranchCoa.setAdBranch(branch);
                }
            }

            // Setup GL Functional Currency
            String[][] currencyList = {{"PHP", "PESO", "PHILIPPINES", "P"}};

            for (String[] strings : currencyList) {
                LocalGlFunctionalCurrency glFunctionalCurrency = null;
                glFunctionalCurrency = glFunctionalCurrencyHome
                        .FcName(strings[0])
                        .FcDescription(strings[1])
                        .FcCountry(strings[2])
                        .FcSymbol(strings[3].charAt(0))
                        .FcPrecision((short)2)
                        .FcExtendedPrecision((short)2)
                        .FcMinimumAccountUnit(0.01)
                        .FcDateFrom(new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime())
                        .FcDateTo(new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime())
                        .FcEnable(EJBCommon.TRUE)
                        .FcAdCompany(companyCode)
                        .buildFunctionalCurrency();
                glFunctionalCurrency.addAdCompany(adCompany);
            }

            // Create default form functions
            createDefaultFormFunctions(adFormFunctionHome);

            // Create default approval
            adApprovalHome.ApprovalQueueExpiration((short) 3).CompanyCode(companyCode).buildApproval();

            // Create default approval documents
            createApprovalDocuments(adApprovalDocumentHome, companyCode);

            // Create default applications and documents
            createDefaultApplications(adApplicationHome, adDocumentCategoryHome, adDocumentSequenceHome, adDocumentSequenceAssignmentHome, companyCode);

            // Setup default responsibilities
            ArrayList<LocalAdResponsibility> responsibilities = setupDefaultResponsibilities(adResponsibilityHome, adBranchResponsibilityHome, branches, companyCode);

            // Setup default form functions responsibilities
            setupFormFunctionResponsibilities(adFormFunctionHome, responsibilities, companyCode);

            // Create default admin user
            createDefaultAdminUser(adUserHome, adUserResponsibilityHome, responsibilities, companyShortName, companyCode);

            // Process default lookup values
            setupDefaultLookupValues(adLookUpHome, adLookUpValueHome, companyCode);

            // Create default Invetory lookups
            String[][] inventoryLookups = {{"INV LOCATION TYPE", "INV LOCATION TYPE"}, {"INV ITEM CATEGORY", "INV ITEM CATEGORY"}, {"INV UNIT OF MEASURE CLASS", "INV UNIT OF MEASURE CLASS"}};

            for (String[] inventoryLookup : inventoryLookups) {

                LocalAdLookUp adLookUpStarterData = null;
                LocalAdLookUpValue adLookUpValueStarterData = null;

                adLookUpStarterData = adLookUpHome.create(inventoryLookup[0], inventoryLookup[1], companyCode);
                adLookUpValueStarterData = adLookUpValueHome.create("DEFAULT", "DEFAULT", null, EJBCommon.TRUE, 'N', companyCode);
                adLookUpStarterData.addAdLookUpValue(adLookUpValueStarterData);
            }

            // Create default Preference Details
            // TODO: glFirstCoa is now removed to preference -- company should setup manually in OFS.
            createDefaultPreferenceDetails(adPreferenceHome, companyCode, glFirstCoa);

            // Create default Stored Procedure
            LocalAdStoredProcedure adStoredProcedure = adStoredProcedureHome.create(EJBCommon.FALSE, "{ call sp_GlRepGeneralLedger(?,?,?,?,?,?,?,?) }", EJBCommon.FALSE, "{ call sp_GlRepTrialBalance(?,?,?,?,?,?,?) }", EJBCommon.FALSE, "{ call sp_GlRepIncomeStatement(?,?,?,?,?,?,?) }", EJBCommon.FALSE, "{ call sp_GlRepBalanceSheet(?,?,?,?,?,?,?) }", EJBCommon.FALSE, "{ call sp_StatementOfAccount(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?}", companyCode);

            // Create default Payment Terms
            String[][] paymentTerms = {{"5 Days Upon Receipt", "5 Days Upon Receipt"}, {"IMMEDIATE", "IMMEDIATE"}};

            for (String[] paymentTerm : paymentTerms) {
                LocalAdPaymentTerm adPaymentTerm = null;
                adPaymentTerm = adPaymentTermHome.create(paymentTerm[0], paymentTerm[1], 100, 0, EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, null, "DEFAULT", companyCode);
                LocalAdPaymentSchedule adPaymentSchedule = adPaymentScheduleHome.create((short) 1, 100, this.getPaymentScheduleDue(paymentTerm[0]), companyCode);
                adPaymentTerm.addAdPaymentSchedule(adPaymentSchedule);
            }

            // Create Tax Codes
            LocalApTaxCode apInclusiveTaxCode = apTaxCodeHome.create("VAT INCLUSIVE", "INPUT TAX", "INCLUSIVE", 12, EJBCommon.TRUE, companyCode);
            glFirstCoa.addApTaxCode(apInclusiveTaxCode); // TODO: Assign the correct tax account for VAT inclusive

            LocalApTaxCode apTaxCode = apTaxCodeHome.create("VAT EXCLUSIVE", "INPUT TAX", "EXCLUSIVE", 12, EJBCommon.TRUE, companyCode);
            glFirstCoa.addApTaxCode(apTaxCode); // TODO: Assign the correct tax account for VAT exclusive

            apTaxCode = apTaxCodeHome.create("ZERO-RATED", "INPUT TAX", "ZERO-RATED", 0, EJBCommon.TRUE, companyCode);
            glFirstCoa.addApTaxCode(apTaxCode); // TODO: Assign the correct tax account for Zero-rated

            apTaxCode = apTaxCodeHome.create("EXEMPT", "INPUT TAX", "EXEMPT", 0, EJBCommon.TRUE, companyCode);
            apTaxCode = apTaxCodeHome.create("NONE", "INPUT TAX", "NONE", 0, EJBCommon.TRUE, companyCode);

            LocalApWithholdingTaxCode apNoneWithholdingTaxCode = apWithholdingTaxCodeHome.create("NONE", "NONE", 0, EJBCommon.TRUE, companyCode);

            // Create Withholding Tax Code
            String[][] withholdingTaxCodes = {{"WC010", "EWT-Payments of Professional fees (CPA’s, Lawyers, Engineers, etc.)", "10"}, {"WC100", "EWT-Payments of Rentals", "5"}, {"WC110", "EWT-Cinematographic film rentals", "5"}};

            for (String[] withholdingTaxCode : withholdingTaxCodes) {
                LocalApWithholdingTaxCode apWithholdingTaxCode = null;
                apWithholdingTaxCode = apWithholdingTaxCodeHome.create(withholdingTaxCode[0], withholdingTaxCode[1], Double.parseDouble(withholdingTaxCode[2]), EJBCommon.TRUE, companyCode);
                glFirstCoa.addApWithholdingTaxCode(apWithholdingTaxCode); // TODO: Assign the correct account for Withholding Tax
            }

            // Create default Supplier Class
            String[][] supplierClasses = {{"Trade", "Trade Vendors"}};

            for (String[] supplierClass : supplierClasses) {
                LocalApSupplierClass apSupplierClass = null;
                apSupplierClass = apSupplierClassHome.create(supplierClass[0], supplierClass[1], glFirstCoa.getCoaCode(), // TODO: Assign the correct payable account
                        glFirstCoa.getCoaCode(), // TODO: Assign the correct expense account
                        0d, 0d, EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, null, null, companyCode);
                apNoneWithholdingTaxCode.addApSupplierClass(apSupplierClass);
                apTaxCode.addApSupplierClass(apSupplierClass);
            }

            // Create default Auto Accounting
            LocalArAutoAccounting arAutoAccounting = arAutoAccountingHome.create("REVENUE", companyCode);
            for (int i = 0; i < genSegmentList.size(); i++) {
                LocalArAutoAccountingSegment arAutoAccountingSegment = arAutoAccountingSegmentHome.create((short) (i + 1), "AR STANDARD MEMO LINE", companyCode);
                arAutoAccounting.addArAutoAccountingSegment(arAutoAccountingSegment);
            }

            Integer nullInteger = null;

            LocalArTaxCode arInclusiveTaxCode = arTaxCodeHome.create("VAT INCLUSIVE", "OUTPUT TAX", "INCLUSIVE", nullInteger, 12, EJBCommon.TRUE, companyCode);
            glFirstCoa.addArTaxCode(arInclusiveTaxCode); // TODO: Assign the correct AR Tax Code for VAT Inclusive

            LocalArTaxCode arTaxCode = arTaxCodeHome.create("VAT EXCLUSIVE", "OUTPUT TAX", "EXCLUSIVE", nullInteger, 12, EJBCommon.TRUE, companyCode);
            glFirstCoa.addArTaxCode(arTaxCode); // TODO: Assign the correct AR Tax Code for VAT Exclusive

            arTaxCode = arTaxCodeHome.create("ZERO-RATED", "OUTPUT TAX", "ZERO-RATED", nullInteger, 0, EJBCommon.TRUE, companyCode);
            glFirstCoa.addArTaxCode(arTaxCode); // TODO: Assign the correct AR Tax Code for Zero-rated

            arTaxCode = arTaxCodeHome.create("EXEMPT", "OUTPUT TAX", "EXEMPT", nullInteger, 0, EJBCommon.TRUE, companyCode);
            arTaxCode = arTaxCodeHome.create("NONE", "OUTPUT TAX", "NONE", nullInteger, 0, EJBCommon.TRUE, companyCode);

            LocalArWithholdingTaxCode arNoneWithholdingTaxCode = arWithholdingTaxCodeHome.create("NONE", "NONE", 0, EJBCommon.TRUE, companyCode);

            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.create("WC010", "EWT-Payments of Professional fees (CPA’s, Lawyers, Engineers, etc.)", 10, EJBCommon.TRUE, companyCode);

            arWithholdingTaxCodeHome.create("WC050", "EWT-Management and technical consultants. If gross income for the current year did not exceed P720,000)", 10, EJBCommon.TRUE, companyCode);

            arWithholdingTaxCodeHome.create("WC051", "EWT-Management and technical consultants. If gross income exceed P720,000)", 15, EJBCommon.TRUE, companyCode);

            arWithholdingTaxCodeHome.create("WC100", "EWT-Payments of Rentals", 5, EJBCommon.TRUE, companyCode);

            glFirstCoa.addArWithholdingTaxCode(arWithholdingTaxCode); // TODO: Assign the correct AR Withholding Tax Account

            // Create default Customer Class
            String[][] customerClasses = {{"Trade-Acct", "Trade-Accounting"}};

            for (String[] customerClass : customerClasses) {

                LocalArCustomerClass arCustomerClass = null;

                arCustomerClass = arCustomerClassHome.create(customerClass[0], customerClass[1], null, null, null, 0d, 0, (short) 0, (short) 0, null, EJBCommon.FALSE, glFirstCoa.getCoaCode(), // TODO: Assign the correct receivable account
                        null, null, null, null, null, EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, 0, companyCode);
                arNoneWithholdingTaxCode.addArCustomerClass(arCustomerClass);
                arInclusiveTaxCode.addArCustomerClass(arCustomerClass);
            }
            String[][] standardMemolines = new String[0][];

            // Create default standard memo lines
            if (companyShortName.equals("MJG") || companyShortName.equals("MRC") || companyShortName.equals("FVJ") || companyShortName.equals("AFM") || companyShortName.equals("OGJ")) {

                standardMemolines = new String[][]{{"ICR Principal", "ICR Principal"}, {"Reimbursement", "Reimbursement"}, {"Security Deposit", "Security Deposit"}, {"Advance Collection", "Advance Collection"}, {"Unidentified Deposit", "Unidentified Deposit"}, {"Interest Income", "Interest Income"}, {"Processing Fees", "Processing Fees"}, {"PF and Connection", "PF and Connection"}, {"Rental Income", "Rental Income"}, {"Parking Fees", "Parking Fees"}, {"Electricity", "Electricity"}, {"Water", "Water"}, {"Repairs", "Repairs"}, {"Penalty Charges", "Penalty Charges"}, {"Bank Interest", "Bank Interest"}, {"Other Income", "Other Income"}, {"Management Fee", "Management Fee"}};


            } else if (companyShortName.equals("GNJ")) {

                standardMemolines = new String[][]{{"ICR Principal", "ICR Principal"}, {"Reimbursement", "Reimbursement"}, {"Advance Collection", "Advance Collection"}, {"Unidentified Deposit", "Unidentified Deposit"}, {"Interest Income", "Interest Income"}, {"PF and Connection", "PF and Connection"}, {"Penalty Charge", "Penalty Charge"}, {"Bank Interest", "Bank Interest"}, {"Other Income", "Other Income"}};

            } else {

            }


            for (String[] standardMemoline : standardMemolines) {

                LocalArStandardMemoLine arStandardMemoLine = null;

                arStandardMemoLine = arStandardMemoLineHome.create("LINE", standardMemoline[0], standardMemoline[1], "", 0, EJBCommon.TRUE, EJBCommon.TRUE, EJBCommon.FALSE, null, null, // TODO: Assign the correct interim account
                        glFirstCoa.getCoaCode(), // TODO: Assign the correct receivable account
                        glFirstCoa.getCoaCode(), // TODO: Assign the correct revenue account
                        companyCode);
                glFirstCoa.addArStandardMemoLine(arStandardMemoLine); // TODO: Assign the correct account
            }


            // Create default bank, bank account, branchmap
            createDefaultBankAccount(glFirstCoa, adBankHome, adBankAccountHome, adBranchBankAccountHome, glFunctionalCurrencyHome, branches, companyCode);

            // Create default organization
            LocalGlOrganization glOrganization = glOrganizationHome.create("ACCOUNTING HEAD", "ACCOUNTING HEAD", null, companyCode);

            segmentIter = genSegmentList.iterator();

            StringBuilder accountFrom = new StringBuilder();
            StringBuilder accountTo = new StringBuilder();

            while (segmentIter.hasNext()) {

                GenSegmentDetails details = (GenSegmentDetails) segmentIter.next();

                for (int j = 0; j < details.getSgMaxSize(); j++) {

                    accountFrom.append("0");
                    accountTo.append("Z");
                }

                if (segmentIter.hasNext()) {

                    accountFrom.append(segmentSeparator);
                    accountTo.append(segmentSeparator);
                }
            }

            // Create default Account Range
            LocalGlAccountRange glAccountRange = glAccountRangeHome.create((short) 1, accountFrom.toString(), accountTo.toString(), companyCode);
            glOrganization.addGlAccountRange(glAccountRange);

            // Setup General Ledger Responsibilities
            for (LocalAdResponsibility res : responsibilities) {
                LocalGlResponsibility glResponsibility = null;

                glResponsibility = glResponsibilityHome.create(res.getRsCode(), EJBCommon.TRUE, companyCode);
                glOrganization.addGlResponsibility(glResponsibility);
            }

            // Create default Period Type
            glPeriodTypeHome.create("CORPORATE", "CORPORATE", (short) 12, 'C', companyCode);


            // Create default Journal Source
            String[][] journalSources = {{"MANUAL", "MANUAL"}, {"RECURRING", "RECURRING JOURNALS"}, {"JOURNAL REVERSAL", "JOURNAL REVERSAL"}, {"ACCOUNTS RECEIVABLES", "ACCOUNT RECEIVABLES SYSTEM"}, {"ACCOUNTS PAYABLES", "ACCOUNTS PAYABLES"}, {"CASH MANAGEMENT", "CASH MANAGEMENT"}, {"INVENTORY", "INVENTORY"},
                    // {"PAYROLL", "PAYROLL"},
                    {"REVALUATION", "REVALUATION JOURNALS"}};

            for (String[] journalSource : journalSources) {

                glJournalSourceHome.create(journalSource[0], journalSource[1], (journalSource[0].equals("JOURNAL REVERSAL")) ? EJBCommon.TRUE : EJBCommon.FALSE, (journalSource[0].equals("MANUAL") || journalSource[0].equals("RECURRING") || journalSource[0].equals("JOURNAL REVERSAL")) ? EJBCommon.TRUE : EJBCommon.FALSE, 'F', companyCode);
            }

            // Create default Journal Categories
            String[] journalCategories = {"GENERAL", "BANK CHARGES", "CREDIT MEMOS", "SALES INVOICES", "SALES RECEIPTS", "DEPOSITS", "VOUCHERS", "DEBIT MEMOS", "CHECKS", "FINANCE CHARGES", "FUND TRANSFERS", "BANK ADJUSTMENTS", "INVENTORY ADJUSTMENTS", "INVENTORY ASSEMBLIES", "STOCK ISSUANCES", "ASSEMBLY TRANSFERS", "RECEIVING ITEMS", "STOCK TRANSFERS", "BRANCH STOCK TRANSFERS",};

            for (String journalCategory : journalCategories) {
                glJournalCategoryHome.create(journalCategory, journalCategory, 'S', companyCode);
            }

        } catch (GlobalRecordAlreadyExistException | GlobalRecordInvalidException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void validateCompanyDetails(String companyName, String companyShortName, ILocalAdCompanyHome adCompanyHome) throws GlobalRecordAlreadyExistException {
        try {

            // Validate Company Name
            LocalAdCompany adExistingCompany = adCompanyHome.findByCmpName(companyName);

            // Validate Company Short Name
            adExistingCompany = adCompanyHome.findByCmpShortName(companyShortName);

            if (adExistingCompany != null) {
                throw new GlobalRecordAlreadyExistException();
            }

        } catch (FinderException ex) {
        } catch (GlobalRecordAlreadyExistException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        }
    }

    private short getPaymentScheduleDue(String paymentName) {
        short retVal = 0;
        switch (paymentName) {
            case "5 Days Upon Receipt":
                retVal = (short) 0;
                break;
            case "IMMEDIATE":
                retVal = (short) 0;
                break;
            case "7 Days Net":
                retVal = (short) 7;
                break;
            case "10 Days Net":
                retVal = (short) 10;
                break;
            case "15 Days Net":
                retVal = (short) 15;
                break;
            case "30 Days Net":
                retVal = (short) 30;
                break;
            case "45 Days Net":
                retVal = (short) 45;
                break;
            case "90 Days Net":
                retVal = (short) 90;
                break;
            case "COD":
                retVal = (short) 0;
                break;
            case "COA PDC":
                retVal = (short) 25;
                break;
            case "12 Months to Pay":
                retVal = (short) 25;
                break;
            case "24 Months to Pay":
                retVal = (short) 25;
                break;
            case "36 Months to Pay":
                retVal = (short) 25;
                break;
            case "48 Months to Pay":
                retVal = (short) 25;
                break;
            case "60 Months to Pay":
                retVal = (short) 25;
                break;
        }
        return retVal;
    }

    private void createDefaultPreferenceDetails(ILocalAdPreferenceHome adPreferenceHome, Integer companyCode, LocalGlChartOfAccount glFirstCoa) throws CreateException {
        LocalAdPreference adPreference = adPreferenceHome.create(EJBCommon.TRUE, // AllowSuspensePosting
                (short) 4, // GlJournalLineNumber
                (short) 4, // ApJournalLineNumber
                (short) 4, // ArInvoiceLineNumber
                "VOUCHER", // ApWTaxRealization
                "COLLECTION", // ArWTaxRealization
                EJBCommon.FALSE, // EnableGlJournalBatch
                EJBCommon.FALSE, // EnableGlRecomputeCoaBalance
                EJBCommon.FALSE, // EnableApVoucherBatch
                EJBCommon.FALSE, // EnableApPOBatch
                EJBCommon.FALSE, // EnableApCheckBatch
                EJBCommon.FALSE, // EnableArInvoiceBatch
                EJBCommon.FALSE, // EnableArInvoiceInterestGeneration
                EJBCommon.FALSE, // EnableArReceiptBatch
                EJBCommon.FALSE, // EnableArMiscReceiptBatch
                "AUTO-POST UPON APPROVAL", // ApGlPostingType
                "AUTO-POST UPON APPROVAL", // ArGlPostingType
                "AUTO-POST UPON APPROVAL", // CmGlPostingType
                (short) 4, // InvInventoryLineNumber
                (short) 3, // InvQuantityPrecisionUnit
                (short) 2, // InvCostPrecisionUnit
                "AUTO-POST UPON APPROVAL", // InvGlPostingType
                "AUTO-POST UPON APPROVAL", // GlPostingType
                EJBCommon.FALSE, // InvEnableShift
                EJBCommon.FALSE, // EnableInvBUABatch
                "PAYMENT", // ApFindCheckDefaultType
                "COLLECTION", // ArFindReceiptDefaultType
                EJBCommon.FALSE, // ApUseAccruedVat
                "CURRENT DATE", // ApDefaultCheckDate
                null, // ApDefaultChecker
                null, // ApDefaultApprover
                null, // ApGlCoaAccruedVatAccount
                null, // ApGlCoaPettyCashAccount
                EJBCommon.FALSE, // CmUseBankForm
                EJBCommon.TRUE, // ApUseSupplierPulldown
                EJBCommon.TRUE, // ArUseCustomerPulldown
                EJBCommon.FALSE, // ApAutoGenerateSupplierCode
                null, // ApNextSupplierCode
                EJBCommon.FALSE, // ArAutoGenerateCustomerCode
                null, // ArNextCustomerCode
                EJBCommon.FALSE, // ApReferenceNumberValidation
                EJBCommon.FALSE, // InvEnablePosIntegration
                EJBCommon.TRUE, // InvEnablePosAutoPostUpload
                null, // glFirstCoa.getCoaCode(), // InvPosAdjustmentAccount
                "AP DISTRIBUTION RECORD", // ApCheckVoucherDataSource
                null, // glFirstCoa.getCoaCode(), // MiscPosDiscountAccount
                null, // glFirstCoa.getCoaCode(), // MiscPosGiftCertificateAccount
                null, // glFirstCoa.getCoaCode(), // MiscPosServiceChargeAccount
                null, // glFirstCoa.getCoaCode(), // MiscPosDineInChargeAccount
                null, // glFirstCoa.getCoaCode(), // ArGlCoaCustomerDepositAccount
                "NONE", // ApDefaultPrTax
                "PHP", // ApDefaultPrCurrency
                "AR DISTRIBUTION RECORD", // ArSalesInvoiceDataSource
                null, // glFirstCoa.getCoaCode(), // InvGlCoaVarianceAccount
                EJBCommon.FALSE, // ArAutoComputeCogs
                0d, // ArMonthlyInterestRate
                30, // ApAgingBucket
                30, // ArAgingBucket
                EJBCommon.FALSE, // ArAllowPriorDate
                EJBCommon.TRUE, // ArCheckInsufficientStock
                EJBCommon.TRUE, // ArDetailedReceivable
                EJBCommon.TRUE, // ApShowPrCost
                EJBCommon.TRUE, // ArEnablePaymentTerm
                EJBCommon.FALSE, // ArDisableSalesPrice
                EJBCommon.TRUE, // InvItemLocationShowAll
                EJBCommon.TRUE, // InvItemLocationAddByItemList
                EJBCommon.FALSE, // ApDebitMemoOverrideCost
                EJBCommon.TRUE, // GlYearEndCloseRestriction
                EJBCommon.TRUE, // AdDisableMultipleLogin
                EJBCommon.TRUE, // AdEnableEmailNotification
                EJBCommon.FALSE, // ArSoSalespersonRequired
                EJBCommon.FALSE, // ArInvcSalespersonRequired
                null, // MailHost
                null, // MailSocketFactoryPort
                null, // MailPort
                null, // MailFrom
                EJBCommon.FALSE, // MailAuthenticator
                null, // MailPassword
                null, // MailTo
                null, // MailCc
                null, // MailBcc
                null, // MailConfig
                null, // UserRestWebService
                null, // PassRestWebService
                null, // Attachment Path
                companyCode); // prfAdCompany
    }

    private void createDefaultAdminUser(LocalAdUserHome adUserHome, LocalAdUserResponsibilityHome adUserResponsibilityHome, ArrayList<LocalAdResponsibility> responsibilities, String companyShortName, Integer companyCode) throws CreateException {

        String[][] users = new String[0][];

        if (companyShortName.equals("FVJ")) {
            users = new String[][]{{"omega", "Omega Administrator", "omega"}, {"admin", "Marge Gabiano", "admin"}, {"jenny", "Jenelyn Cacanindin", "jenny"}, {"ghel", "Angel Buenavidez", "ghel"}, {"raineer", "Raineer Del Socorro", "raineer"}, {"nina", "Nina", "nina"},};
        } else if (companyShortName.equals("AFM")) {
            users = new String[][]{{"omega", "Omega Administrator", "omega"}, {"admin", "Marge Gabiano", "admin"}, {"jenny", "Jenelyn Cacanindin", "jenny"}, {"jeje", "Roberto Crisostomo", "jeje"}, {"raineer", "Raineer Del Socorro", "raineer"}, {"ryan", "Ryan Carlo", "ryan"},};
        } else if (companyShortName.equals("MRC")) {
            users = new String[][]{{"omega", "Omega Administrator", "omega"}, {"admin", "Marge Gabiano", "admin"}, {"jenny", "Jenelyn Cacanindin", "jenny"}, {"jeje", "Roberto Crisostomo", "jeje"}, {"raineer", "Raineer Del Socorro", "raineer"}, {"cleo", "Cleo Joy Eraña", "cleo"},

            };
        } else if (companyShortName.equals("GNJ")) {
            users = new String[][]{{"omega", "Omega Administrator", "omega"}, {"admin", "Marge Gabiano", "admin"}, {"jenny", "Jenelyn Cacanindin", "jenny"}, {"ghel", "Angel Buenavidez", "ghel"}, {"dj", "Darel Juayno", "dj"}, {"jerlyn", "Jerlyn Quejano", "jerlyn"},

            };

        } else if (companyShortName.equals("MJG")) {
            users = new String[][]{{"omega", "Omega Administrator", "omega"}, {"admin", "Marge Gabiano", "admin"}, {"jenny", "Jenelyn Cacanindin", "jenny"}, {"ghel", "Angel Buenavidez", "ghel"}, {"dj", "Darel Juayno", "dj"}, {"jerlyn", "Jerlyn Quejano", "jerlyn"},};
        } else if (companyShortName.equals("OGJ")) {
            users = new String[][]{{"omega", "Omega Administrator", "omega"}, {"admin", "Marge Gabiano", "admin"}, {"jenny", "Jenelyn Cacanindin", "jenny"}, {"ghel", "Angel Buenavidez", "ghel"}, {"dj", "Darel Juayno", "dj"}, {"jerlyn", "Jerlyn Quejano", "jerlyn"},};
        } else if (companyShortName.equals("HOA")) {
            users = new String[][]{{"omega", "Omega Administrator", "omega"}, {"admin", "Marge Gabiano", "admin"}, {"jenny", "Jenelyn Cacanindin", "jenny"}, {"ghel", "Angel Buenavidez", "ghel"}, {"raineer", "Raineer Del Socorro", "raineer"}, {"nina", "Jeanina Andaya", "nina"},};
        } else {
            users = new String[][]{{"omega", "Omega Administrator", "omega"}, {"admin", "Administrator", "admin"}, {"jenny", "Jenelyn Cacanindin", "jenny"},};
        }


        for (String[] user : users) {

            LocalAdUser adUser = adUserHome.create(user[0], "default", user[1], "", "", (byte) 0, (byte) 0, this.encryptPassword(user[2]), (short) 2, (short) 0, (short) 0, (short) 0, new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), companyCode);

            LocalAdUserResponsibility adUserResponsibility = adUserResponsibilityHome.create(new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), companyCode);

            adUserResponsibility.setAdUser(adUser);

            for (LocalAdResponsibility adResponsibility : responsibilities) {

                if (user[0].equals("omega") || user[0].equals("admin")) {

                    if (adResponsibility.getRsName().equals(EJBCommon.SUPER))
                        adUserResponsibility.setAdResponsibility(adResponsibility);

                } else {

                    if (adResponsibility.getRsName().equals(EJBCommon.ACCOUNTANT))
                        adUserResponsibility.setAdResponsibility(adResponsibility);

                }

            }

        }


    }

    private void createDefaultBankAccount(LocalGlChartOfAccount glFirstCoa, LocalAdBankHome adBankHome, LocalAdBankAccountHome adBankAccountHome, LocalAdBranchBankAccountHome adBranchBankAccountHome, LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome, ArrayList<LocalAdBranch> branches, Integer companyCode) throws CreateException, FinderException {

        LocalAdBank adBank = adBankHome.create("BANK", "BANK", 0, "BANK", "", "", EJBCommon.TRUE, companyCode);

        String[][] bankAccounts = {{"CIB-BDO", "CIB-BDO", "CIB-BDO"}, {"CIB-BPI", "CIB-BPI", "CIB-BPI"}, {"CIB-BPIF", "CIB-BPIF", "CIB-BPIF"}, {"CIB-BFB", "CIB-BFB", "CIB-BFB"}, {"CIB-MBTC", "CIB-MBTC", "CIB-MBTC"}};

        LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName("PHP", companyCode);

        for (String[] bankAccount : bankAccounts) {

            LocalAdBankAccount adBankAccount = null;
            LocalAdBranchBankAccount adBranchBankAccount = null;

            adBankAccount = adBankAccountHome.create(bankAccount[0], bankAccount[1], "DEFAULT", bankAccount[2], "INTERNAL", glFirstCoa.getCoaCode(), null, null, null, null, null, null, null, null, null, null, 0d, null, 0d, "1", EJBCommon.TRUE, //ENABLE
                    EJBCommon.FALSE, 0, 0, EJBCommon.FALSE, 0, 0, EJBCommon.FALSE, 0, 0, EJBCommon.FALSE, 0, 0, EJBCommon.FALSE, 0, 0, EJBCommon.FALSE, 0, 0, EJBCommon.FALSE, 0, 0, EJBCommon.FALSE, 0, 0, EJBCommon.FALSE, 0, 0, EJBCommon.FALSE, 0, 0, EJBCommon.FALSE, 0, 0, 10, "Helvetica", EJBCommon.FALSE, companyCode);

            adBankAccount.setAdBank(adBank);
            adBankAccount.setGlFunctionalCurrency(glFunctionalCurrency);

            for (LocalAdBranch branch : branches) {
                adBranchBankAccount = adBranchBankAccountHome.create(glFirstCoa.getCoaCode(), glFirstCoa.getCoaCode(), glFirstCoa.getCoaCode(), glFirstCoa.getCoaCode(), glFirstCoa.getCoaCode(), glFirstCoa.getCoaCode(), 'N', companyCode);
                adBranchBankAccount.setAdBankAccount(adBankAccount);
                adBranchBankAccount.setAdBranch(branch);
            }

        }


    }

    private ArrayList<LocalAdResponsibility> setupDefaultResponsibilities(LocalAdResponsibilityHome adResponsibilityHome, LocalAdBranchResponsibilityHome adBranchResponsibilityHome, ArrayList<LocalAdBranch> branches, Integer companyCode) throws CreateException { // Setup HQ and branch responsibilities

        String[] roles = {EJBCommon.SUPER, EJBCommon.ACCOUNTANT,};

        ArrayList<LocalAdResponsibility> responsibilities = new ArrayList<>();
        LocalAdBranch adBranch = null;


        for (String role : roles) {

            LocalAdResponsibility adResponsibility = null;
            LocalAdBranchResponsibility adBranchResponsibility = null;

            adResponsibility = adResponsibilityHome.create(role, role, new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), companyCode);
            responsibilities.add(adResponsibility);

            for (LocalAdBranch branch : branches) {

                adBranchResponsibility = adBranchResponsibilityHome.create(companyCode);
                adBranchResponsibility.setAdResponsibility(adResponsibility);
                adBranchResponsibility.setAdBranch(branch);

            }
        }
        return responsibilities;
    }

    private void setupFormFunctionResponsibilities(LocalAdFormFunctionHome adFormFunctionHome, ArrayList<LocalAdResponsibility> responsibilities, Integer companyCode) throws FinderException { // Setup Form Function Responsibility
        Integer[] journalFunctions = {1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 13, 14, 15, 37};

        // Journal Functions
        for (Integer journalFunction1 : journalFunctions) {

            LocalAdFormFunction journalFunction = null;
            ArrayList list = new ArrayList();

            journalFunction = adFormFunctionHome.findByPrimaryKey(journalFunction1);

            // Super, Accountant, Gl Clerk, Booker and Finance
            for (LocalAdResponsibility adResponsibility : responsibilities) {
                if (adResponsibility.getRsName().equals(EJBCommon.SUPER) || adResponsibility.getRsName().equals(EJBCommon.ACCOUNTANT) || adResponsibility.getRsName().equals(EJBCommon.GL_CLERK) || adResponsibility.getRsName().equals(EJBCommon.BOOKKEEPER) || adResponsibility.getRsName().equals(EJBCommon.FINANCE_OFFICER)) {

                    list.add(adResponsibility);
                }
            }

            this.setFormFunctionResponsibility(list, journalFunction, companyCode);
        }

        Integer[] bookingFunctions = {7};

        // Bookkeeping Functions
        for (Integer bookingFunction1 : bookingFunctions) {

            LocalAdFormFunction bookingFunction;
            ArrayList list = new ArrayList();

            bookingFunction = adFormFunctionHome.findByPrimaryKey(bookingFunction1);

            // Super, Bookkeeper and Finance Officer
            for (LocalAdResponsibility adResponsibility : responsibilities) {
                if (adResponsibility.getRsName().equals(EJBCommon.SUPER) || adResponsibility.getRsName().equals(EJBCommon.BOOKKEEPER) || adResponsibility.getRsName().equals(EJBCommon.FINANCE_OFFICER)) {

                    list.add(adResponsibility);
                }
            }

            this.setFormFunctionResponsibility(list, bookingFunction, companyCode);
        }

        Integer[] reportFunctions = {501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511, 512, 513, 515, 516, 517, 518, 519, 1503};

        // Report Functions
        for (Integer reportFunction1 : reportFunctions) {

            LocalAdFormFunction reportFunction = null;
            ArrayList list = new ArrayList();

            reportFunction = adFormFunctionHome.findByPrimaryKey(reportFunction1);

            // Super, Accountant, Gl Clerk and Finance Officer
            for (LocalAdResponsibility adResponsibility : responsibilities) {
                if (adResponsibility.getRsName().equals(EJBCommon.SUPER) || adResponsibility.getRsName().equals(EJBCommon.ACCOUNTANT) || adResponsibility.getRsName().equals(EJBCommon.GL_CLERK) || adResponsibility.getRsName().equals(EJBCommon.FINANCE_OFFICER)) {

                    list.add(adResponsibility);
                }
            }

            this.setFormFunctionResponsibility(list, reportFunction, companyCode);
        }

        Integer[] commonFunctions = {1008, 1009};

        // Common Functions
        for (Integer commonFunction1 : commonFunctions) {

            LocalAdFormFunction commonFunction = null;
            ArrayList list = new ArrayList();

            commonFunction = adFormFunctionHome.findByPrimaryKey(commonFunction1);

            // Super
            for (LocalAdResponsibility adResponsibility : responsibilities) {
                if (adResponsibility.getRsName().equals(EJBCommon.SUPER)) {

                    list.add(adResponsibility);
                }
            }

            this.setFormFunctionResponsibility(list, commonFunction, companyCode);
        }

        Integer[] sysAdminFunctions = {1002, 1003, 1006, 1007, 1013, 1014, 1015, 1016, 1017, 1020, 1026, 1027, 1501, 1504, 1505, 1506, 1507, 1508};

        // System Admin Functions
        for (Integer sysAdminFunction1 : sysAdminFunctions) {

            LocalAdFormFunction sysAdminFunction = null;
            ArrayList list = new ArrayList();

            sysAdminFunction = adFormFunctionHome.findByPrimaryKey(sysAdminFunction1);

            // Super and Sys Admin
            for (LocalAdResponsibility adResponsibility : responsibilities) {
                if (adResponsibility.getRsName().equals(EJBCommon.SUPER) || adResponsibility.getRsName().equals(EJBCommon.SYS_ADMIN)) {

                    list.add(adResponsibility);
                }
            }

            this.setFormFunctionResponsibility(list, sysAdminFunction, companyCode);
        }

        Integer[] adminFunctions = {1019};

        // Admin Functions
        for (Integer adminFunction1 : adminFunctions) {

            LocalAdFormFunction adminFunction = null;

            adminFunction = adFormFunctionHome.findByPrimaryKey(adminFunction1);

            this.setFormFunctionResponsibility(responsibilities, adminFunction, companyCode);
        }

        Integer[] arFunctions = {2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2307, 2308, 2309, 2310, 2311, 2312, 2313, 2314, 2315, 2316, 2317, 2318, 2319, 2320, 2321, 2322, 2501, 2502, 2503, 2504, 2505, 2506, 2507, 2508, 2509, 2510, 2511, 2512, 2513, 2514, 2515, 2516, 2517, 2518, 2519, 2520, 2521, 2522, 2523, 2524, 2525, 2526, 2527, 2528, 2529, 2530};

        // FF Responsibility, Accounts, Ar Clerk, Keeper and Finance
        for (Integer arFunction1 : arFunctions) {

            LocalAdFormFunction arFunction = null;
            ArrayList list = new ArrayList();

            arFunction = adFormFunctionHome.findByPrimaryKey(arFunction1);

            // Super, Accountant, AR Clerk, Bookkeeper and Finance Officer
            for (LocalAdResponsibility adResponsibility : responsibilities) {
                if (adResponsibility.getRsName().equals(EJBCommon.SUPER) || adResponsibility.getRsName().equals(EJBCommon.ACCOUNTANT) || adResponsibility.getRsName().equals(EJBCommon.AR_CLERK) || adResponsibility.getRsName().equals(EJBCommon.BOOKKEEPER) || adResponsibility.getRsName().equals(EJBCommon.FINANCE_OFFICER)) {

                    list.add(adResponsibility);
                }
            }

            this.setFormFunctionResponsibility(list, arFunction, companyCode);
        }

        Integer[] apFunctions = {2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 3003, 3004, 3005, 3006, 3007, 3008, 3009, 3010, 3011, 3012, 3013, 3014, 3015, 3016, 3017, 3018, 3019, 3020, 3021, 3022, 3023, 3024, 3025, 3026, 3027, 3028, 3029, 3030, 3031, 3032, 3033, 3034, 3501, 3502, 3503, 3504, 3511, 3512, 3517, 3518, 3519, 3520, 3521, 3522, 3523,};

        // Accounts Payable Functions
        for (Integer element : apFunctions) {

            LocalAdFormFunction apFunction = null;
            ArrayList list = new ArrayList();

            apFunction = adFormFunctionHome.findByPrimaryKey(element);

            // Super, Accountant, AP Clerk, Bookkeeper and Finance Officer
            for (LocalAdResponsibility adResponsibility : responsibilities) {
                if (adResponsibility.getRsName().equals(EJBCommon.SUPER) || adResponsibility.getRsName().equals(EJBCommon.ACCOUNTANT) || adResponsibility.getRsName().equals(EJBCommon.AP_CLERK) || adResponsibility.getRsName().equals(EJBCommon.BOOKKEEPER) || adResponsibility.getRsName().equals(EJBCommon.FINANCE_OFFICER)) {

                    list.add(adResponsibility);
                }
            }

            this.setFormFunctionResponsibility(list, apFunction, companyCode);
        }

        Integer[] ap2Functions = {3505, 3506, 3507, 3508, 3509, 3510, 3513, 3514, 3515, 3516};

        // Accounts Payable Functions less Bookkeeper
        for (Integer item : ap2Functions) {

            LocalAdFormFunction ap2Function = null;
            ArrayList list = new ArrayList();

            ap2Function = adFormFunctionHome.findByPrimaryKey(item);

            // Super, Accountant, AP Clerk and Finance Officer
            for (LocalAdResponsibility adResponsibility : responsibilities) {
                if (adResponsibility.getRsName().equals(EJBCommon.SUPER) || adResponsibility.getRsName().equals(EJBCommon.ACCOUNTANT) || adResponsibility.getRsName().equals(EJBCommon.AP_CLERK) || adResponsibility.getRsName().equals(EJBCommon.FINANCE_OFFICER)) {

                    list.add(adResponsibility);
                }
            }

            this.setFormFunctionResponsibility(list, ap2Function, companyCode);
        }

        Integer[] financeFunctions = {22, 23, 24, 25, 27, 33, 34, 35, 42, 43, 45, 46, 47, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 1004, 1005, 1010, 1011, 1012, 1018, 1021, 1022, 1023, 1024, 1025, 2001, 2002, 2301, 2302, 2303, 2305, 2306, 3001, 3002, 3301, 3302, 3303, 3304, 5301, 5302, 5303, 5304, 5305, 5306, 5307, 5308, 5309, 5310, 5311, 5312, 5313};

        // Finance Functions
        for (Integer value : financeFunctions) {

            LocalAdFormFunction financeFunction = null;
            ArrayList list = new ArrayList();

            financeFunction = adFormFunctionHome.findByPrimaryKey(value);

            // Super and Finance Officer
            for (LocalAdResponsibility adResponsibility : responsibilities) {
                if (adResponsibility.getRsName().equals(EJBCommon.SUPER) || adResponsibility.getRsName().equals(EJBCommon.FINANCE_OFFICER)) {

                    list.add(adResponsibility);
                }
            }

            this.setFormFunctionResponsibility(list, financeFunction, companyCode);
        }

        Integer[] bankFunctions = {1502, 5501, 5502, 5503, 5504, 5505, 5506, 5507};

        // Bank Functions
        for (Integer integer : bankFunctions) {

            LocalAdFormFunction bankFunction = null;
            ArrayList list = new ArrayList();

            bankFunction = adFormFunctionHome.findByPrimaryKey(integer);

            // Super, Accountant and Finance Officer
            for (LocalAdResponsibility adResponsibility : responsibilities) {
                if (adResponsibility.getRsName().equals(EJBCommon.SUPER) || adResponsibility.getRsName().equals(EJBCommon.ACCOUNTANT) || adResponsibility.getRsName().equals(EJBCommon.FINANCE_OFFICER)) {

                    list.add(adResponsibility);
                }
            }

            this.setFormFunctionResponsibility(list, bankFunction, companyCode);
        }

        Integer[] clerkFunctions = {4001, 4002, 4003, 4004, 4005, 4006, 4007, 4008, 4009, 4010, 4011, 4012, 4013, 4501, 4502, 4503, 4504, 4505, 4506, 4507, 4508, 4509, 4510, 4511, 5001, 5002, 5003, 5004, 5005, 5006, 5007, 5008, 5009, 5010, 5011, 5012, 5013, 5014, 5015, 5016, 5017, 5018, 5019, 5020, 5021, 5022, 5023, 5024, 5508, 5509, 5510, 5511, 5512, 5513, 5514, 5515, 5516, 5517, 5518, 5519, 5520, 5521, 5522, 5523, 5524, 5525, 5527, 5528, 5529, 5530, 5531};

        // FF Responsibility, Accounts, Keeper and Finance
        for (Integer function : clerkFunctions) {

            LocalAdFormFunction clerkFunction = null;
            ArrayList list = new ArrayList();

            clerkFunction = adFormFunctionHome.findByPrimaryKey(function);

            // Super, Accountant, Bookkeeper and Finance Officer
            for (LocalAdResponsibility adResponsibility : responsibilities) {
                if (adResponsibility.getRsName().equals(EJBCommon.SUPER) || adResponsibility.getRsName().equals(EJBCommon.ACCOUNTANT) || adResponsibility.getRsName().equals(EJBCommon.BOOKKEEPER) || adResponsibility.getRsName().equals(EJBCommon.FINANCE_OFFICER)) {

                    list.add(adResponsibility);
                }
            }

            this.setFormFunctionResponsibility(list, clerkFunction, companyCode);
        }
    }

    private void setupDefaultLookupValues(LocalAdLookUpHome adLookUpHome, LocalAdLookUpValueHome adLookUpValueHome, Integer companyCode) throws CreateException { // Setup default lookups
        String[] defaultLookups = {"AD DEPARTMENT", "AR FREIGHT", "AR REGION", "AR CUSTOMER BATCH - SOA", "AR CUSTOMER DEPARTMENT - SOA", "AR REPORT TYPE - STATEMENT OF ACCOUNT", "AR REPORT TYPE - SALES REGISTER", "AR REPORT TYPE - JOB ORDER", "AR INVOICE ITEM PRINT TYPE", "AR INVOICE MEMO LINE PRINT TYPE", "AR INVOICE SO MATCHED PRINT TYPE", "AR SALES ORDER PRINT TYPE", "AR SALES ORDER TYPE", "AR JOB ORDER - TECHNICIANS", "AR PRINT INVOICE PARAMETER", "AR PRINT CREDIT MEMO PARAMETER", "AR PRINT SALES ORDER PARAMETER", "AR PRINT RECEIPT PARAMETER", "AR PRINT MISC RECEIPT PARAMETER", "AP PR TYPE", "AP PURCHASE REQUISITION DOCUMENT TYPE", "AP CANVASS DOCUMENT TYPE", "AP PURCHASE ORDER DOCUMENT TYPE", "AP RECEIVING ITEM DOCUMENT TYPE", "AP VOUCHER DOCUMENT TYPE", "AP DEBIT MEMO DOCUMENT TYPE", "AP CHECK DOCUMENT TYPE", "AP CHECK PAYMENT REQUEST DOCUMENT TYPE", "AR SALES ORDER DOCUMENT TYPE", "AR JOB ORDER DOCUMENT TYPE", "AR INVOICE DOCUMENT TYPE", "AR CREDIT MEMO DOCUMENT TYPE", "AR RECEIPT DOCUMENT TYPE", "AR STATEMENT OF ACCOUNT DOCUMENT TYPE", "AR ACKNOWLEDGEMENT RECEIPT DOCUMENT TYPE", "GL REPORT TYPE - GENERAL LEDGER", "GL REPORT TYPE - MONTHLY VAT RETURN", "GL REPORT TYPE - QUARTERLY VAT RETURN", "GL REPORT TYPE - INCOME TAX WITHHELD", "GL JOURNAL DOCUMENT TYPE", "CM ADJUSTMENT DOCUMENT TYPE", "CM FUND TRANSFER DOCUMENT TYPE", "INV SHIFT", "INV DONENESS", "INV PRICE LEVEL", "INV REPORT TYPE - INVENTORY LIST", "INV REPORT TYPE - BUILD UNBUILD ASSEMBLY ORDER", "INV ADJUSTMENT REQUEST DOCUMENT TYPE", "INV ADJUSTMENT DOCUMENT TYPE", "INV BRANCH STOCK TRANSFER ORDER DOCUMENT TYPE", "INV BRANCH STOCK TRANSFER-OUT DOCUMENT TYPE", "INV BRANCH STOCK TRANSFER-IN DOCUMENT TYPE", "INV STOCK TRANSFER DOCUMENT TYPE", "INV BUILD ASSEMBLY ORDER DOCUMENT TYPE", "INV BUILD ASSEMBLY DOCUMENT TYPE", "INV OVERHEAD DOCUMENT TYPE", "INV BUILD ORDER DOCUMENT TYPE", "INV STOCK ISSUANCE DOCUMENT TYPE", "INV ASSEMBLY TRANSFER DOCUMENT TYPE", "INV TRANSACTIONAL BUDGET DOCUMENT TYPE", "AR JOB ORDER STATUS", "GL PROVINCES - MONTHLY VAT RETURN"};

        // Setup Province Lookups
        String[] defaultProvinceLookups = {"N.C.D."};

        for (String defaultLookup : defaultLookups) {

            LocalAdLookUp adLookUp = null;
            LocalAdLookUpValue adLookUpValue = null;
            LocalAdLookUp adLookUpProvinces = null;

            // Add freight lookups and lookup values
            if (defaultLookup.equals("AR FREIGHT")) {

                adLookUp = adLookUpHome.create(defaultLookup, defaultLookup, companyCode);
                adLookUpValue = adLookUpValueHome.create("DHL", "DHL", null, EJBCommon.TRUE, 'N', companyCode);
                adLookUp.addAdLookUpValue(adLookUpValue);

            } else if (defaultLookup.equals("GL PROVINCES - MONTHLY VAT RETURN")) {

                adLookUpProvinces = adLookUpHome.create(defaultLookup, defaultLookup, companyCode);

                // Province lookups
                for (String defaultProvinceLookup : defaultProvinceLookups) {
                    LocalAdLookUpValue adLookUpProvince = null;
                    adLookUpProvince = adLookUpValueHome.create(defaultProvinceLookup, defaultProvinceLookup, null, EJBCommon.TRUE, 'N', companyCode);

                    adLookUpProvinces.addAdLookUpValue(adLookUpProvince);
                }

            } else {

                // Add default lookup without look up values
                adLookUp = adLookUpHome.create(defaultLookup, defaultLookup, companyCode);
            }
        }
    }

    private void createDefaultApplications(LocalAdApplicationHome adApplicationHome, LocalAdDocumentCategoryHome adDocumentCategoryHome, LocalAdDocumentSequenceHome adDocumentSequenceHome, LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome, Integer companyCode) throws CreateException { // Create default applications
        LocalAdApplication adApplicationGl = adApplicationHome.create("OMEGA GENERAL LEDGER", "OMEGA GENERAL LEDGER", EJBCommon.TRUE, companyCode);
        LocalAdApplication adApplicationAp = adApplicationHome.create("OMEGA PAYABLES", "OMEGA PAYABLES", EJBCommon.TRUE, companyCode);
        LocalAdApplication adApplicationAr = adApplicationHome.create("OMEGA RECEIVABLES", "OMEGA RECEIVABLES", EJBCommon.TRUE, companyCode);
        LocalAdApplication adApplicationCm = adApplicationHome.create("OMEGA CASH MANAGEMENT", "OMEGA CASH MANAGEMENT", EJBCommon.TRUE, companyCode);
        LocalAdApplication adApplicationAd = adApplicationHome.create("OMEGA ADMINISTRATION", "OMEGA ADMINISTRATION", EJBCommon.TRUE, companyCode);
        LocalAdApplication adApplicationInv = adApplicationHome.create("OMEGA INVENTORY", "OMEGA INVENTORY", EJBCommon.FALSE, companyCode);

        // Create default document category, sequence and assignment
        LocalAdDocumentCategory adDocumentCategory = adDocumentCategoryHome.create("GL JOURNAL", "JOURNAL", companyCode);
        LocalAdDocumentSequence adDocumentSequence = adDocumentSequenceHome.create("GL JOURNAL DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "JV000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationGl.addAdDocumentSequence(adDocumentSequence);
        adApplicationGl.addAdDocumentCategory(adDocumentCategory);

        adDocumentCategory = adDocumentCategoryHome.create("AP PURCHASE REQUISITION", "PURCHASE REQUISITION", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("AP PURCHASE REQUISITION DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "PR000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationAp.addAdDocumentSequence(adDocumentSequence);
        adApplicationAp.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("AP CANVASS", "CANVASS", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("AP CANVASS DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "CS000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationAp.addAdDocumentSequence(adDocumentSequence);
        adApplicationAp.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("AP PURCHASE ORDER", "PURCHASE ORDER", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("AP PURCHASE ORDER DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "PO000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationAp.addAdDocumentSequence(adDocumentSequence);
        adApplicationAp.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("AP RECEIVING ITEM", "RECEIVING ITEM", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("AP RECEIVING ITEM DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "RI000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationAp.addAdDocumentSequence(adDocumentSequence);
        adApplicationAp.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("AP VOUCHER", "VOUCHER", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("AP VOUCHER DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "APV000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationAp.addAdDocumentSequence(adDocumentSequence);
        adApplicationAp.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("AP DEBIT MEMO", "DEBIT MEMO", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("AP DEBIT MEMO DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "DM00001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationAp.addAdDocumentSequence(adDocumentSequence);
        adApplicationAp.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("AP CHECK", "CHECK", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("AP CHECK DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "CV000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationAp.addAdDocumentSequence(adDocumentSequence);
        adApplicationAp.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("AP CHECK PAYMENT REQUEST", "CHECK PAYMENT REQUEST", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("AP CHECK PAYMENT REQUEST DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "CPR000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationAp.addAdDocumentSequence(adDocumentSequence);
        adApplicationAp.addAdDocumentCategory(adDocumentCategory);

        adDocumentCategory = adDocumentCategoryHome.create("AR SALES ORDER", "SALES ORDER", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("AR SALES ORDER DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "SO000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationInv.addAdDocumentSequence(adDocumentSequence);
        adApplicationInv.addAdDocumentCategory(adDocumentCategory);

        adDocumentCategory = adDocumentCategoryHome.create("AR JOB ORDER", "JOB ORDER", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("AR JOB ORDER DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "JO000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationInv.addAdDocumentSequence(adDocumentSequence);
        adApplicationInv.addAdDocumentCategory(adDocumentCategory);

        adDocumentCategory = adDocumentCategoryHome.create("AR INVOICE", "INVOICE", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("AR INVOICE DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "SI000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationAr.addAdDocumentSequence(adDocumentSequence);
        adApplicationAr.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("AR CREDIT MEMO", "CREDIT MEMO", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("AR CREDIT MEMO DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "CM000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationAr.addAdDocumentSequence(adDocumentSequence);
        adApplicationAr.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("AR RECEIPT", "RECEIPT", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("AR RECEIPT DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "OR000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationAr.addAdDocumentSequence(adDocumentSequence);
        adApplicationAr.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("AR STATEMENT OF ACCOUNT", "STATEMENT OF ACCOUNT", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("AR STATEMENT OF ACCOUNT DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "SOA000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationAr.addAdDocumentSequence(adDocumentSequence);
        adApplicationAr.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("AR ACKNOWLEDGEMENT RECEIPT", "ACKNOWLEDGEMENT RECEIPT", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("AR ACKNOWLEDGEMENT RECEIPT DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "AR000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationAr.addAdDocumentSequence(adDocumentSequence);
        adApplicationAr.addAdDocumentCategory(adDocumentCategory);

        adDocumentCategory = adDocumentCategoryHome.create("AR DELIVERY", "DELIVERY", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("AR DELIVERY DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationAr.addAdDocumentSequence(adDocumentSequence);
        adApplicationAr.addAdDocumentCategory(adDocumentCategory);

        adDocumentCategory = adDocumentCategoryHome.create("CM ADJUSTMENT", "ADJUSTMENT", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("CM ADJUSTMENT DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "CA000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationCm.addAdDocumentSequence(adDocumentSequence);
        adApplicationCm.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("CM FUND TRANSFER", "FUND TRANSFER", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("CM FUND TRANSFER DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "FT000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationCm.addAdDocumentSequence(adDocumentSequence);
        adApplicationCm.addAdDocumentCategory(adDocumentCategory);

        adDocumentCategory = adDocumentCategoryHome.create("INV ADJUSTMENT REQUEST", "ADJUSTMENT REQUEST", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("INV ADJUSTMENT REQUEST DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "IAR000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationInv.addAdDocumentSequence(adDocumentSequence);
        adApplicationInv.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("INV ADJUSTMENT", "ADJUSTMENT", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("INV ADJUSTMENT DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "IA000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationInv.addAdDocumentSequence(adDocumentSequence);
        adApplicationInv.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("INV BRANCH STOCK TRANSFER ORDER", "BRANCH STOCK TRANSFER ORDER", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("INV BRANCH STOCK TRANSFER ORDER DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "BSOS000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationInv.addAdDocumentSequence(adDocumentSequence);
        adApplicationInv.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("INV BRANCH STOCK TRANSFER-OUT", "BRANCH STOCK TRANSFER-OUT", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("INV BRANCH STOCK TRANSFER-OUT DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "BSTO000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationInv.addAdDocumentSequence(adDocumentSequence);
        adApplicationInv.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("INV BRANCH STOCK TRANSFER-IN", "BRANCH STOCK TRANSFER-IN", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("INV BRANCH STOCK TRANSFER-IN DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "BSTI000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationInv.addAdDocumentSequence(adDocumentSequence);
        adApplicationInv.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("INV STOCK TRANSFER", "STOCK TRANSFER", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("INV STOCK TRANSFER DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "STR000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationInv.addAdDocumentSequence(adDocumentSequence);
        adApplicationInv.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("INV BUILD ASSEMBLY ORDER", "BUILD ASSEMBLY", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("INV BUILD ASSEMBLY ORDER DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "BO000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationInv.addAdDocumentSequence(adDocumentSequence);
        adApplicationInv.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("INV BUILD ASSEMBLY", "BUILD ASSEMBLY", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("INV BUILD ASSEMBLY DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "BUA000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationInv.addAdDocumentSequence(adDocumentSequence);
        adApplicationInv.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("INV OVERHEAD", "OVERHEAD", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("INV OVERHEAD DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "OVH000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationInv.addAdDocumentSequence(adDocumentSequence);
        adApplicationInv.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("INV BUILD ORDER", "BUILD ORDER", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("INV BUILD ORDER DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "BOR000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationInv.addAdDocumentSequence(adDocumentSequence);
        adApplicationInv.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("INV STOCK ISSUANCE", "STOCK ISSUANCE", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("INV STOCK ISSUANCE DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "SI000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationInv.addAdDocumentSequence(adDocumentSequence);
        adApplicationInv.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("INV ASSEMBLY TRANSFER", "ASSEMBLY TRANSFER", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("INV ASSEMBLY TRANSFER DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "ATR000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationInv.addAdDocumentSequence(adDocumentSequence);
        adApplicationInv.addAdDocumentCategory(adDocumentCategory);
        adDocumentCategory = adDocumentCategoryHome.create("INV TRANSACTIONAL BUDGET", "TRANSACTIONAL BUDGET", companyCode);
        adDocumentSequence = adDocumentSequenceHome.create("INV TRANSACTIONAL BUDGET DOCUMENT", new GregorianCalendar(2000, 0, 1, 0, 0, 0).getTime(), new GregorianCalendar(2050, 0, 1, 0, 0, 0).getTime(), 'A', "1", companyCode);
        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.create(0, "TB000001", companyCode);
        adDocumentCategory.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adDocumentSequence.addAdDocumentSequenceAssignment(adDocumentSequenceAssignment);
        adApplicationInv.addAdDocumentSequence(adDocumentSequence);
        adApplicationInv.addAdDocumentCategory(adDocumentCategory);
    }

    private String encryptPassword(String password) {

        Debug.print("AdConsoleControllerBean encryptPassword");

        String encPassword = "";

        try {

            String keyString = "Some things are better left unread.";
            byte[] key = keyString.getBytes(StandardCharsets.UTF_8);

            // encode password to utf-8
            byte[] utf8 = password.getBytes(StandardCharsets.UTF_8);

            int length = key.length;
            if (utf8.length < key.length) length = utf8.length;

            // encrypt
            byte[] data = new byte[length];
            for (int i = 0; i < length; i++) {
                data[i] = (byte) (utf8[i] ^ key[i]);
            }

            encPassword = Base64.getEncoder().encodeToString(data);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        return encPassword;
    }

    private void createApprovalDocuments(LocalAdApprovalDocumentHome adApprovalDocumentHome, Integer companyCode) throws CreateException {
        adApprovalDocumentHome.create("GL JOURNAL", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("AP PURCHASE REQUISITION", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("AP CANVASS", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("AP PURCHASE ORDER", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("AP RECEIVING ITEM", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("AP VOUCHER", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("AP DEBIT MEMO", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("AP CHECK", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("AP CHECK PAYMENT REQUEST", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

        adApprovalDocumentHome.create("AR SALES ORDER", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("AR INVOICE", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("AR CREDIT MEMO", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("AR RECEIPT", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("AR STATEMENT OF ACCOUNT", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("AR ACKNOWLEDGEMENT RECEIPT", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

        adApprovalDocumentHome.create("CM ADJUSTMENT", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("CM FUND TRANSFER", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

        adApprovalDocumentHome.create("INV ADJUSTMENT REQUEST", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("INV ADJUSTMENT", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("INV BRANCH STOCK TRANSFER ORDER", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("INV BRANCH STOCK TRANSFER-OUT", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("INV BRANCH STOCK TRANSFER-IN", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("INV STOCK TRANSFER", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("INV BUILD ASSEMBLY", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("INV BUILD ASSEMBLY ORDER", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

        adApprovalDocumentHome.create("INV OVERHEAD", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("INV BUILD ORDER", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("INV STOCK ISSUANCE", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("INV ASSEMBLY TRANSFER", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
        adApprovalDocumentHome.create("INV TRANSACTIONAL BUDGET", "PRINT ANYTIME", EJBCommon.TRUE, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);
    }

    private void createDefaultFormFunctions(LocalAdFormFunctionHome adFormFunctionHome) throws CreateException {
        adFormFunctionHome.create(1, "Journal Entry");
        adFormFunctionHome.create(2, "Find Journal");
        adFormFunctionHome.create(3, "Journal Post");
        adFormFunctionHome.create(4, "Journal Import");
        adFormFunctionHome.create(5, "Journal Reversal");
        adFormFunctionHome.create(6, "Recurring Journal");
        adFormFunctionHome.create(7, "Year-end Closing");
        adFormFunctionHome.create(8, "Find Recurring Journal");
        adFormFunctionHome.create(9, "Recurring Journal Generation");
        adFormFunctionHome.create(10, "Gl Apporval");
        adFormFunctionHome.create(11, "Journal Batch Print");
        adFormFunctionHome.create(12, "Journal Batch Submit");
        adFormFunctionHome.create(13, "Journal Batch Entry");
        adFormFunctionHome.create(14, "Find Journal Batch");
        adFormFunctionHome.create(15, "Journal Batch Copy");
        adFormFunctionHome.create(22, "Chart Of Accounts");
        adFormFunctionHome.create(23, "Period Types");
        adFormFunctionHome.create(24, "Accounting Calendar");
        adFormFunctionHome.create(25, "Accounting Calendar Lines");
        adFormFunctionHome.create(27, "Transaction Calendar Lines");
        adFormFunctionHome.create(33, "Journal Sources");
        adFormFunctionHome.create(34, "Journal Categories");
        adFormFunctionHome.create(35, "Suspense Accounts");
        adFormFunctionHome.create(37, "Open Close Periods");
        adFormFunctionHome.create(42, "Currency Maintenance");
        adFormFunctionHome.create(43, "Daily Rates");
        adFormFunctionHome.create(45, "Organization Entry");
        adFormFunctionHome.create(46, "Organization Account Assignment");
        adFormFunctionHome.create(47, "Organization Responsibility Assignment");
        adFormFunctionHome.create(53, "Financial Report Entry");
        adFormFunctionHome.create(54, "Row Set");
        adFormFunctionHome.create(55, "Column Set");
        adFormFunctionHome.create(56, "Row Entry");
        adFormFunctionHome.create(57, "Column Entry");
        adFormFunctionHome.create(58, "Account Assignment");
        adFormFunctionHome.create(59, "Calculation");
        adFormFunctionHome.create(60, "Find Chart Of Account");
        adFormFunctionHome.create(61, "Find Daily Rate");
        adFormFunctionHome.create(62, "Find Journal Interface");
        adFormFunctionHome.create(63, "Journal Interface Maintenance");
        adFormFunctionHome.create(64, "Budget Definition");
        adFormFunctionHome.create(65, "Budget Organization");
        adFormFunctionHome.create(66, "Budget Account Assignment");
        adFormFunctionHome.create(67, "Budget Entry");
        adFormFunctionHome.create(68, "Find Budget");
        adFormFunctionHome.create(69, "Journal Interface Upload");
        adFormFunctionHome.create(70, "COA Generator");
        adFormFunctionHome.create(71, "Tax Interface Run");
        adFormFunctionHome.create(72, "Tax Interface Maintenance");
        adFormFunctionHome.create(73, "Static Report");
        adFormFunctionHome.create(74, "User Static Report");
        adFormFunctionHome.create(75, "Find Segment");
        adFormFunctionHome.create(76, "FOREX Revaluation");

        adFormFunctionHome.create(501, "Detail Trail Balance Report");
        adFormFunctionHome.create(502, "Detail Income Statement Report");
        adFormFunctionHome.create(503, "Detail Balance Sheet Report");
        adFormFunctionHome.create(504, "General Ledger Report");
        adFormFunctionHome.create(505, "Financial Report Run");
        adFormFunctionHome.create(506, "Chart Of Account List");
        adFormFunctionHome.create(507, "Journal Print");
        adFormFunctionHome.create(508, "Income Tax Withheld");
        adFormFunctionHome.create(509, "Monthly Vat Declaration");
        adFormFunctionHome.create(510, "Quarterly Vat Return");
        adFormFunctionHome.create(511, "Rep Journal Edit List");
        adFormFunctionHome.create(512, "Csv Quarterly Vat Return");
        adFormFunctionHome.create(513, "Rep Budget");
        adFormFunctionHome.create(515, "Static Report");
        adFormFunctionHome.create(516, "Journal Register");
        adFormFunctionHome.create(517, "FOREX Revaluation Print");
        adFormFunctionHome.create(518, "Daily Rate List");
        adFormFunctionHome.create(519, "Investor Ledger Report");
        adFormFunctionHome.create(520, "Income Tax Return");

        adFormFunctionHome.create(1002, "Document Sequences");
        adFormFunctionHome.create(1003, "Document Sequences Assignment");
        adFormFunctionHome.create(1004, "Banks");
        adFormFunctionHome.create(1005, "Bank Accounts");
        adFormFunctionHome.create(1006, "Look Up Values");
        adFormFunctionHome.create(1007, "Document Categories");
        adFormFunctionHome.create(1008, "Aging Bucket");
        adFormFunctionHome.create(1009, "Aging Bucket Value");
        adFormFunctionHome.create(1010, "Payment Terms");
        adFormFunctionHome.create(1011, "Payment Schedules");
        adFormFunctionHome.create(1012, "Discounts");
        adFormFunctionHome.create(1013, "Preference");
        adFormFunctionHome.create(1014, "User");
        adFormFunctionHome.create(1015, "User Responsibility");
        adFormFunctionHome.create(1016, "Responsibilities");
        adFormFunctionHome.create(1017, "Form Function Responsibilities");
        adFormFunctionHome.create(1018, "Value Set Value");
        adFormFunctionHome.create(1019, "Change Password");
        adFormFunctionHome.create(1020, "Company");
        adFormFunctionHome.create(1021, "Approval Setup");
        adFormFunctionHome.create(1022, "Approval Document");
        adFormFunctionHome.create(1023, "Amount Limit");
        adFormFunctionHome.create(1024, "Approval Coa Line");
        adFormFunctionHome.create(1025, "Approval User");
        adFormFunctionHome.create(1026, "User Sessions");
        adFormFunctionHome.create(1027, "Branches");

        adFormFunctionHome.create(1501, "Rep Transaction Log");
        adFormFunctionHome.create(1502, "Rep Bank Account List");
        adFormFunctionHome.create(1503, "Rep Value Set Value List");
        adFormFunctionHome.create(1504, "Rep Transaction Summary");
        adFormFunctionHome.create(1505, "Rep Responsibility List");
        adFormFunctionHome.create(1506, "Rep User List");
        adFormFunctionHome.create(1507, "Rep Approval List");
        adFormFunctionHome.create(1508, "Stored Procedure Setup");

        adFormFunctionHome.create(2001, "Customer Entry");
        adFormFunctionHome.create(2002, "Find Customer ");
        adFormFunctionHome.create(2003, "Invoice Entry");
        adFormFunctionHome.create(2004, "Find Invoice ");
        adFormFunctionHome.create(2005, "Credit Memo Entry");
        adFormFunctionHome.create(2006, "Find Receipt");
        adFormFunctionHome.create(2007, "Misc Receipt Entry");
        adFormFunctionHome.create(2008, "Receipt Entry");
        adFormFunctionHome.create(2009, "Invoice Post");
        adFormFunctionHome.create(2010, "Receipt Post");
        adFormFunctionHome.create(2011, "Ar Gl Journal Interface");
        adFormFunctionHome.create(2012, "Ar Journal");
        adFormFunctionHome.create(2013, "Ar Approval");
        adFormFunctionHome.create(2014, "PDC Entry");
        adFormFunctionHome.create(2015, "Find PDC");
        adFormFunctionHome.create(2016, "PDC Invoice Generation");
        adFormFunctionHome.create(2017, "Sales Order Entry");
        adFormFunctionHome.create(2018, "Find Sales Order");
        adFormFunctionHome.create(2019, "Invoice Import");
        adFormFunctionHome.create(2020, "Receipt Import");
        adFormFunctionHome.create(2021, "Job Order Entry");
        adFormFunctionHome.create(2022, "Find Job Order");
        adFormFunctionHome.create(2023, "Job Order Assignment");
        adFormFunctionHome.create(2024, "Find Personel");

        adFormFunctionHome.create(2301, "Ar Tax Codes");
        adFormFunctionHome.create(2302, "Ar Withholding Tax Codes");
        adFormFunctionHome.create(2303, "Customer Type");
        adFormFunctionHome.create(2305, "Customer Classes");
        adFormFunctionHome.create(2306, "Standard Memo Lines");
        adFormFunctionHome.create(2307, "Invoice Batch Print");
        adFormFunctionHome.create(2308, "Receipt Batch Print");
        adFormFunctionHome.create(2309, "Invoice Batch Submit");
        adFormFunctionHome.create(2310, "Receipt Batch Submit");
        adFormFunctionHome.create(2311, "Invoice Batch Entry");
        adFormFunctionHome.create(2312, "Find Invoice Batch ");
        adFormFunctionHome.create(2313, "Receipt Batch Entry");
        adFormFunctionHome.create(2314, "Find Receipt Batch ");
        adFormFunctionHome.create(2315, "Invoice Batch Copy");
        adFormFunctionHome.create(2316, "Receipt Batch Copy");
        adFormFunctionHome.create(2317, "Salesperson");
        adFormFunctionHome.create(2318, "Personel");
        adFormFunctionHome.create(2319, "Job Order Type");
        adFormFunctionHome.create(2320, "Personel Type");
        adFormFunctionHome.create(2321, "Delivery");
        adFormFunctionHome.create(2322, "Find Delivery");

        adFormFunctionHome.create(2501, "Rep Invoice Print");
        adFormFunctionHome.create(2502, "Rep Credit Memo Print");
        adFormFunctionHome.create(2503, "Rep Receipt Print");
        adFormFunctionHome.create(2504, "Statement");
        adFormFunctionHome.create(2505, "Ar Aging Detail");
        adFormFunctionHome.create(2506, "Rep Sales Register");
        adFormFunctionHome.create(2507, "Rep Or Register");
        adFormFunctionHome.create(2508, "Rep Customer List");
        adFormFunctionHome.create(2509, "Rep Output Tax ");
        adFormFunctionHome.create(2510, "Rep Creditable Withholding Tax");
        adFormFunctionHome.create(2511, "Rep Invoice Edit List");
        adFormFunctionHome.create(2512, "Rep Receipt Edit List");
        adFormFunctionHome.create(2513, "Rep Sales");
        adFormFunctionHome.create(2514, "Rep Tax Code List");
        adFormFunctionHome.create(2515, "Rep Withholding Tax Code List");
        adFormFunctionHome.create(2516, "Rep Customer Class List");
        adFormFunctionHome.create(2517, "Rep Customer Type List");
        adFormFunctionHome.create(2518, "Rep Standard Memo Line List");
        adFormFunctionHome.create(2519, "Rep Post-Dated Check");
        adFormFunctionHome.create(2520, "Ar Rep Aging Summary");
        adFormFunctionHome.create(2521, "Rep Sales Order Print");
        adFormFunctionHome.create(2522, "Rep Delivery Receipt Print");
        adFormFunctionHome.create(2523, "Rep Salesperson");
        adFormFunctionHome.create(2524, "Rep Transmittal Document Print");
        adFormFunctionHome.create(2525, "Rep Delivery Receipt");
        adFormFunctionHome.create(2526, "Rep Sales Order");
        adFormFunctionHome.create(2527, "Rep Pdc Print");
        adFormFunctionHome.create(2528, "Job Order Print");
        adFormFunctionHome.create(2529, "Job Status Report");
        adFormFunctionHome.create(2530, "Personel Summary");

        adFormFunctionHome.create(3001, "Supplier Entry");
        adFormFunctionHome.create(3002, "Find Supplier ");
        adFormFunctionHome.create(3003, "Voucher Entry");
        adFormFunctionHome.create(3004, "Find Voucher ");
        adFormFunctionHome.create(3005, "Debit Memo Entry");
        adFormFunctionHome.create(3006, "Recurring Voucher Entry");
        adFormFunctionHome.create(3007, "Recurring Voucher Generation");
        adFormFunctionHome.create(3008, "Find Recurring Voucher ");
        adFormFunctionHome.create(3009, "Payment Entry");
        adFormFunctionHome.create(3010, "Direct Check Entry");
        adFormFunctionHome.create(3011, "Find Check ");
        adFormFunctionHome.create(3012, "Voucher Post");
        adFormFunctionHome.create(3013, "Check Post");
        adFormFunctionHome.create(3014, "Gl Journal Interface");
        adFormFunctionHome.create(3015, "Ap Journal ");
        adFormFunctionHome.create(3016, "Ap Approval");
        adFormFunctionHome.create(3017, "Voucher Batch Print");
        adFormFunctionHome.create(3018, "Check Batch Print");
        adFormFunctionHome.create(3019, "Voucher Batch Submit");
        adFormFunctionHome.create(3020, "Check Batch Submit");
        adFormFunctionHome.create(3021, "Voucher Batch Entry");
        adFormFunctionHome.create(3022, "Check Batch Entry");
        adFormFunctionHome.create(3023, "Find Voucher Batch ");
        adFormFunctionHome.create(3024, "Find Check Batch ");
        adFormFunctionHome.create(3025, "Voucher Batch Copy");
        adFormFunctionHome.create(3026, "Check Batch Copy");
        adFormFunctionHome.create(3027, "Purchase Order Entry");
        adFormFunctionHome.create(3028, "Find Purchase Order");
        adFormFunctionHome.create(3029, "Receiving Item Entry");
        adFormFunctionHome.create(3030, "Voucher Import");
        adFormFunctionHome.create(3031, "Check Import");
        adFormFunctionHome.create(3032, "Generate Purchase Requisition");
        adFormFunctionHome.create(3033, "Purchase Requisition Entry");
        adFormFunctionHome.create(3034, "Find Purchase Requisition");
        adFormFunctionHome.create(3035, "Check Payment Request");
        adFormFunctionHome.create(3036, "CANVASS");
        adFormFunctionHome.create(3037, "Annual Procurement Plan Upload");

        adFormFunctionHome.create(3301, "Tax Codes");
        adFormFunctionHome.create(3302, "Supplier Classes");
        adFormFunctionHome.create(3303, "Supplier Types");
        adFormFunctionHome.create(3304, "Withholding Tax Codes");

        adFormFunctionHome.create(3501, "Rep Voucher Print");
        adFormFunctionHome.create(3502, "Rep Debit Memo Print");
        adFormFunctionHome.create(3503, "Rep Check Voucher Print");
        adFormFunctionHome.create(3504, "Rep Check Print");
        adFormFunctionHome.create(3505, "Rep Ap Register");
        adFormFunctionHome.create(3506, "Rep Check Register");
        adFormFunctionHome.create(3507, "Ar Rep Aging Detail");
        adFormFunctionHome.create(3508, "Rep Input Tax");
        adFormFunctionHome.create(3509, "Rep Withholding Tax Expanded ");
        adFormFunctionHome.create(3510, "Rep Supplier List");
        adFormFunctionHome.create(3511, "Rep Voucher Edit List");
        adFormFunctionHome.create(3512, "Rep Check Edit List");
        adFormFunctionHome.create(3513, "Rep Tax Code List");
        adFormFunctionHome.create(3514, "Rep Withholding Tax Code List");
        adFormFunctionHome.create(3515, "Rep Supplier Class List");
        adFormFunctionHome.create(3516, "Rep Supplier Type List");
        adFormFunctionHome.create(3517, "Rep Purchase Order Print");
        adFormFunctionHome.create(3518, "Rep Receiving Report Print");
        adFormFunctionHome.create(3519, "Ap Rep Aging Summary");
        adFormFunctionHome.create(3520, "Rep Receiving Items");
        adFormFunctionHome.create(3521, "Rep Purchase Order");
        adFormFunctionHome.create(3522, "Rep Purchase Requisition Print");
        adFormFunctionHome.create(3523, "Rep Canvass Report Print");
        adFormFunctionHome.create(3524, "Purchase Requisition Register");
        adFormFunctionHome.create(3525, "Rep Annual Procurement Plan");

        adFormFunctionHome.create(4001, "Bank Reconciliation");
        adFormFunctionHome.create(4002, "Adjustment Entry");
        adFormFunctionHome.create(4003, "Find Adjustment");
        adFormFunctionHome.create(4004, "Fund Transfer Entry");
        adFormFunctionHome.create(4005, "Find Fund Transfer");
        adFormFunctionHome.create(4006, "Cm Gl Journal Interface");
        adFormFunctionHome.create(4007, "Cm Journal ");
        adFormFunctionHome.create(4008, "Cm Approval");
        adFormFunctionHome.create(4009, "Adjustment Post");
        adFormFunctionHome.create(4010, "Fund Transfer");
        adFormFunctionHome.create(4011, "Adjustment Batch Submit");
        adFormFunctionHome.create(4012, "Fund Transfer Batch Submit");
        adFormFunctionHome.create(4013, "Releasing Checks");

        adFormFunctionHome.create(4501, "Deposit List");
        adFormFunctionHome.create(4502, "Released Checks");
        adFormFunctionHome.create(4503, "Daily Cash Position Detail");
        adFormFunctionHome.create(4504, "Daily Cash Position Summary");
        adFormFunctionHome.create(4505, "Daily Cash Forecast Detail");
        adFormFunctionHome.create(4506, "Daily Cash Forecast Summary");
        adFormFunctionHome.create(4507, "Weekly Cash Position Detail");
        adFormFunctionHome.create(4508, "Weekly Cash Position Summary");
        adFormFunctionHome.create(4509, "Bank Reconciliation");
        adFormFunctionHome.create(4510, "Cash Position");
        adFormFunctionHome.create(4511, "Rep Adjustment Print");

        adFormFunctionHome.create(5001, "Adjustment Entry");
        adFormFunctionHome.create(5002, "Find Adjustment");
        adFormFunctionHome.create(5003, "Build Unbuild Assembly Entry");
        adFormFunctionHome.create(5004, "Find Build Unbuild Assembly");
        adFormFunctionHome.create(5005, "Physical Inventory Entry");
        adFormFunctionHome.create(5006, "Find Physical Inventory");
        adFormFunctionHome.create(5007, "Inv Approval");
        adFormFunctionHome.create(5008, "Inv Adjustment Post");
        adFormFunctionHome.create(5009, "Inv Gl Journal Interface");
        adFormFunctionHome.create(5010, "Inv Journal");
        adFormFunctionHome.create(5011, "Overhead Entry");
        adFormFunctionHome.create(5012, "Find Overhead");
        adFormFunctionHome.create(5013, "Build Order Entry");
        adFormFunctionHome.create(5014, "Find Build Order");
        adFormFunctionHome.create(5015, "Stock Issuance Entry");
        adFormFunctionHome.create(5016, "Find Stock Issuance");
        adFormFunctionHome.create(5017, "Assembly Transfer Entry");
        adFormFunctionHome.create(5018, "Find Assembly Transfer");
        adFormFunctionHome.create(5019, "Find Build Order Line");
        adFormFunctionHome.create(5020, "Stock Transfer Entry");
        adFormFunctionHome.create(5021, "Find Stock Transfer");
        adFormFunctionHome.create(5022, "Branch Stock Transfer-Out");
        adFormFunctionHome.create(5023, "Branch Stock Transfer-In");
        adFormFunctionHome.create(5024, "Find Branch Stock Transfer");
        adFormFunctionHome.create(5025, "Branch Stock Transfer Request Entry");
        adFormFunctionHome.create(5026, "Adjustment Request");
        adFormFunctionHome.create(5027, "TRANSACTIONAL BUDGET");

        adFormFunctionHome.create(5301, "Item Entry");
        adFormFunctionHome.create(5302, "Find Item");
        adFormFunctionHome.create(5303, "Location Entry");
        adFormFunctionHome.create(5304, "Find Location");
        adFormFunctionHome.create(5305, "Item Location Entry");
        adFormFunctionHome.create(5306, "Find Item Location");
        adFormFunctionHome.create(5307, "Unit of Measures");
        adFormFunctionHome.create(5308, "Overhead Memo Lines");
        adFormFunctionHome.create(5309, "Build Unbuild Assembly Post");
        adFormFunctionHome.create(5310, "Assembly Item Entry");
        adFormFunctionHome.create(5311, "Unit Of Measure Conversion");
        adFormFunctionHome.create(5312, "Price Levels");
        adFormFunctionHome.create(5313, "Line Item Templates");

        adFormFunctionHome.create(5501, "Usage Variance Report");
        adFormFunctionHome.create(5502, "Item Costing Report");
        adFormFunctionHome.create(5503, "Item List");
        adFormFunctionHome.create(5504, "Assembly List");
        adFormFunctionHome.create(5505, "Count Variance Report");
        adFormFunctionHome.create(5506, "Cost of Sales");
        adFormFunctionHome.create(5507, "Inventory Profitability");
        adFormFunctionHome.create(5508, "Rep Build Order Print");
        adFormFunctionHome.create(5509, "Rep Stock Issuance Print");
        adFormFunctionHome.create(5510, "Rep Assembly Transfer Print");
        adFormFunctionHome.create(5511, "Rep Stock Transfer");
        adFormFunctionHome.create(5512, "Rep Stock On Hand");
        adFormFunctionHome.create(5513, "Rep Reorder Items");
        adFormFunctionHome.create(5514, "Rep Stock Transfer Print");
        adFormFunctionHome.create(5515, "Rep Branch Stock Transfer Out Print");
        adFormFunctionHome.create(5516, "Rep Branch Stock Transfer In Print");
        adFormFunctionHome.create(5517, "Rep Physical Inventory Worksheet");
        adFormFunctionHome.create(5518, "Rep Adjustment Print");
        adFormFunctionHome.create(5519, "Rep Stock Card");
        adFormFunctionHome.create(5520, "Rep Item Ledger");
        adFormFunctionHome.create(5521, "Rep Inventory List");
        adFormFunctionHome.create(5522, "Rep Adjustment Register");
        adFormFunctionHome.create(5523, "Rep Branch Stock Transfer Register");
        adFormFunctionHome.create(5524, "Rep Markup List");
        adFormFunctionHome.create(5525, "Rep Build Unbuild Print");
        adFormFunctionHome.create(5526, "Rep Adjustment Request Print");
        adFormFunctionHome.create(5527, "Find Build Unbuild Assembly Batch");
        adFormFunctionHome.create(5528, "Build Unbuild Assembly Batch Entry");
        adFormFunctionHome.create(5529, "Build Unbuild Assembly Order Entry");
        adFormFunctionHome.create(5530, "Build Unbuild Assembly Order");
        adFormFunctionHome.create(5531, "Physical Inventory Print");
    }

    private void setFormFunctionResponsibility(ArrayList list, LocalAdFormFunction adFormFunction, Integer companyCode) {

        Debug.print("AdConsoleControllerBean setFormFunctionResponsibility");

        LocalAdFormFunctionResponsibilityHome adFormFunctionResponsibilityHome = null;

        try {

            adFormFunctionResponsibilityHome = (LocalAdFormFunctionResponsibilityHome) EJBHomeFactory.lookUpLocalHome(LocalAdFormFunctionResponsibilityHome.JNDI_NAME, LocalAdFormFunctionResponsibilityHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            for (Object o : list) {

                LocalAdFormFunctionResponsibility adFormFunctionResponsibility = adFormFunctionResponsibilityHome.create("F", companyCode);
                adFormFunction.addAdFormFunctionResponsibility(adFormFunctionResponsibility);

                // Add Responsibility per Form Function
                LocalAdResponsibility adResponsibility = (LocalAdResponsibility) o;
                adResponsibility.addAdFormFunctionResponsibility(adFormFunctionResponsibility);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void ejbCreate() throws CreateException {

        Debug.print("AdConsoleControllerBean ejbCreate");
    }
}