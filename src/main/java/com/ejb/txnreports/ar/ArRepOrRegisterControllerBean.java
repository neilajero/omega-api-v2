package com.ejb.txnreports.ar;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import jakarta.ejb.*;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import javax.sql.DataSource;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.*;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepOrRegisterDetails;

@Stateless(name = "ArRepOrRegisterControllerEJB")
public class ArRepOrRegisterControllerBean extends EJBContextClass implements ArRepOrRegisterController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalArCustomerClassHome arCustomerClassHome;
    @EJB
    private LocalArCustomerTypeHome arCustomerTypeHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArPdcHome arPdcHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArReceiptBatchHome arReceiptBatchHome;

    public void executeSpArRepOrRegister(String STORED_PROCEDURE, String CUSTOMER_CODE, Date DT_FRM, Date DT_TO, boolean INCLUDE_UNPOSTED, boolean INCLUDE_MISC_RECEIPT, String BRANCH_CODES, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepOrRegisterControllerBean executeSpArRepOrRegister");

        try {
            StoredProcedureQuery spQuery = em.createStoredProcedureQuery(STORED_PROCEDURE).registerStoredProcedureParameter("customerCode", String.class, ParameterMode.IN).registerStoredProcedureParameter("dateFrom", Date.class, ParameterMode.IN).registerStoredProcedureParameter("dateTo", Date.class, ParameterMode.IN).registerStoredProcedureParameter("includeUnposted", Boolean.class, ParameterMode.IN).registerStoredProcedureParameter("includeMiscReceipt", Boolean.class, ParameterMode.IN).registerStoredProcedureParameter("branchCode", String.class, ParameterMode.IN).registerStoredProcedureParameter("adCompany", Integer.class, ParameterMode.IN).registerStoredProcedureParameter("resultCount", Integer.class, ParameterMode.OUT);

            spQuery.setParameter("customerCode", CUSTOMER_CODE);
            spQuery.setParameter("dateFrom", DT_FRM);
            spQuery.setParameter("dateTo", DT_TO);
            spQuery.setParameter("includeUnposted", INCLUDE_UNPOSTED);
            spQuery.setParameter("includeMiscReceipt", INCLUDE_MISC_RECEIPT);
            spQuery.setParameter("branchCode", BRANCH_CODES);
            spQuery.setParameter("adCompany", AD_CMPNY);

            spQuery.execute();

            Integer resultCount = (Integer) spQuery.getOutputParameterValue("resultCount");

            if (resultCount <= 0) {
                throw new GlobalNoRecordFoundException();
            }

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

    }

    public ArrayList getAdLvCustomerBatchAll(Integer AD_CMPNY) {

        Debug.print("ArRepOrRegisterControllerBean getAdLvCustomerBatchAll");

        ArrayList list = new ArrayList();

        try {
            Collection adLookUpValues = adLookUpValueHome.findByLuName("AR CUSTOMER BATCH - SOA", AD_CMPNY);
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

    public ArrayList getArCcAll(Integer AD_CMPNY) {

        Debug.print("ArRepOrRegisterControllerBean getArCcAll");

        LocalArCustomerClass arCustomerClass = null;

        ArrayList list = new ArrayList();
        try {
            Collection arCustomerClasses = arCustomerClassHome.findEnabledCcAll(AD_CMPNY);
            for (Object customerClass : arCustomerClasses) {
                arCustomerClass = (LocalArCustomerClass) customerClass;
                list.add(arCustomerClass.getCcName());
            }
            return list;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArCtAll(Integer AD_CMPNY) {

        Debug.print("ArRepOrRegisterControllerBean getArCtAll");

        LocalArCustomerType arCustomerType = null;
        ArrayList list = new ArrayList();

        try {
            Collection arCustomerTypes = arCustomerTypeHome.findEnabledCtAll(AD_CMPNY);
            for (Object customerType : arCustomerTypes) {
                arCustomerType = (LocalArCustomerType) customerType;
                list.add(arCustomerType.getCtName());
            }
            return list;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBaAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArRepOrRegisterControllerBean getAdBaAll");

        LocalAdBankAccount adBankAccount = null;
        ArrayList list = new ArrayList();

        try {
            Collection adBankAccounts = adBankAccountHome.findEnabledBaAll(AD_BRNCH, AD_CMPNY);
            for (Object bankAccount : adBankAccounts) {
                adBankAccount = (LocalAdBankAccount) bankAccount;
                list.add(adBankAccount.getBaName());
            }
            return list;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepOrRegisterControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility = null;
        LocalAdBranch adBranch = null;
        Collection adBranchResponsibilities = null;

        ArrayList list = new ArrayList();

        try {
            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(RS_CODE, AD_CMPNY);
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (adBranchResponsibilities.isEmpty()) {
            throw new GlobalNoRecordFoundException();
        }

        try {
            for (Object branchResponsibility : adBranchResponsibilities) {
                adBranchResponsibility = (LocalAdBranchResponsibility) branchResponsibility;
                adBranch = adBranchHome.findByPrimaryKey(adBranchResponsibility.getAdBranch().getBrCode());
                AdBranchDetails details = new AdBranchDetails();
                details.setBrCode(adBranch.getBrCode());
                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrName(adBranch.getBrName());
                details.setBrHeadQuarter(adBranch.getBrHeadQuarter());
                list.add(details);
            }
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        return list;
    }

    public ArrayList executeArRepOrRegisterSP(ResultSet rs, ArrayList branchList, String ORDER_BY, String GROUP_BY, boolean detailedReport, boolean SHOW_ENTRIES, boolean SUMMARIZE, boolean INCLUDE_PR, Integer AD_CMPNY, DataSource dataSource) {

        Debug.print("ArRepOrRegisterControllerBean executeArRepOrRegisterSP");

        ArrayList list = new ArrayList();

        try {

            if (SHOW_ENTRIES) {

            } else {
                generateORRegisterWoEntries(rs, ORDER_BY, dataSource, list);
            }

            return list;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private void generateORRegisterWoEntries(ResultSet rs, String ORDER_BY, DataSource dataSource, ArrayList list) throws SQLException {

        String spInvoiceLineItems = "{ call sp_ArInvoiceLineItems(?) }";
        String spInvoiceLines = "{ call sp_ArInvoiceLines(?) }";
        String spDistributionRecords = "{ call sp_OrCoaDescription(?) }";

        while (rs.next()) {

            Connection conn = null;
            CallableStatement stmt = null;
            ResultSet invoiceLineItems = null;
            ResultSet invoiceLines = null;
            ResultSet distributionRecords = null;

            String customerName = rs.getString("CUSTOMER_NAME");
            String receiptCustomerName = rs.getString("RCT_CUSTOMER_NAME");

            ArRepOrRegisterDetails mdetails = new ArRepOrRegisterDetails();
            mdetails.setOrrDate(EJBCommon.convertStringToSQLDate(rs.getString("RECEIPT_DATE")));
            mdetails.setOrrReceiptNumber(rs.getString("RECEIPT_NUMBER"));
            mdetails.setOrrPaymentMethod(rs.getString("RECEIPT_PAYMENT_METHOD"));
            mdetails.setOrrReferenceNumber(rs.getString("TT_RECEIPT_NUMBER"));
            mdetails.setOrrDescription(rs.getString("DESCRIPTION"));
            mdetails.setOrrCstCustomerCode(rs.getString("CUSTOMER_CODE"));
            mdetails.setOrrCstCustomerClass(customerName);

            conn = dataSource.getConnection();
            stmt = conn.prepareCall(spDistributionRecords);
            stmt.setString(1, rs.getString("RECEIPT_NUMBER"));
            distributionRecords = stmt.executeQuery();

            while (distributionRecords.next()) {
                mdetails.setOrrDrCoaAccountDescription(distributionRecords.getString("ACCNT_DESC"));
            }

            mdetails.setOrrCstCustomerType(rs.getString("CUSTOMER_TYPE"));
            mdetails.setOrrAmount(Double.parseDouble(rs.getString("APPLIED_AMOUNT")));
            mdetails.setOrrBankAccount(rs.getString("BANK_ACCOUNT_NAME"));
            mdetails.setOrderBy(ORDER_BY);
            mdetails.setOrrCstName(customerName == null ? customerName : receiptCustomerName);

            double TOTAL_NET_AMNT = 0d;
            double TOTAL_TAX_AMNT = 0d;

            conn = dataSource.getConnection();
            stmt = conn.prepareCall(spInvoiceLineItems);
            stmt.setString(1, rs.getString("INVOICE_NUMBER"));
            invoiceLineItems = stmt.executeQuery();

            stmt = conn.prepareCall(spInvoiceLines);
            stmt.setString(1, rs.getString("INVOICE_NUMBER"));
            invoiceLines = stmt.executeQuery();

            if (invoiceLineItems != null) {
                while (invoiceLineItems.next()) {
                    TOTAL_NET_AMNT += Double.parseDouble(invoiceLineItems.getString("TOTAL_NET_AMOUNT"));
                    TOTAL_TAX_AMNT += Double.parseDouble(invoiceLineItems.getString("TOTAL_TAX_AMOUNT"));
                }
            } else if (invoiceLines != null) {
                while (invoiceLines.next()) {
                    TOTAL_NET_AMNT += Double.parseDouble(invoiceLines.getString("TOTAL_NET_AMOUNT"));
                    TOTAL_TAX_AMNT += Double.parseDouble(invoiceLines.getString("TOTAL_TAX_AMOUNT"));
                }
            }
            mdetails.setOrrReceiptNetAmount(TOTAL_NET_AMNT);
            mdetails.setOrrReceiptTaxAmount(TOTAL_TAX_AMNT);
            mdetails.setOrrPosted(rs.getString("POSTED"));
            list.add(mdetails);
        }
    }

    public ArrayList executeArRepOrRegister(HashMap criteria, ArrayList branchList, String ORDER_BY, String GROUP_BY, boolean detailedReport, boolean SHOW_ENTRIES, boolean SUMMARIZE, boolean INCLUDE_PR, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepOrRegisterControllerBean executeArRepOrRegister");

        ArrayList list = new ArrayList();

        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct WHERE (");

            if (branchList.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            Iterator brIter = branchList.iterator();
            AdBranchDetails details = (AdBranchDetails) brIter.next();
            jbossQl.append("rct.rctAdBranch=").append(details.getBrCode());
            while (brIter.hasNext()) {
                details = (AdBranchDetails) brIter.next();
                jbossQl.append(" OR rct.rctAdBranch=").append(details.getBrCode());
            }

            jbossQl.append(") ");
            firstArgument = false;

            Object[] obj;
            if (criteria.containsKey("customerCode")) {
                criteriaSize--;
            }

            if (criteria.containsKey("unpostedOnly")) {
                criteriaSize--;
            }

            if (criteria.containsKey("includedUnposted")) {
                criteriaSize--;
            }

            if (criteria.containsKey("includedMiscReceipts")) {
                criteriaSize--;
            }

            if (criteria.containsKey("detailedReport")) {
                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("customerCode")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("rct.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
            }

            if (criteria.containsKey("receiptBatchName")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("rct.arReceiptBatch.rbName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptBatchName");
                ctr++;
            }

            if (criteria.containsKey("bankAccount")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.adBankAccount.baName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("bankAccount");
                ctr++;
            }

            if (criteria.containsKey("customerBatch")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.arCustomer.cstCustomerBatch=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerBatch");
                ctr++;
            }

            if (criteria.containsKey("customerType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.arCustomer.arCustomerType.ctName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerType");
                ctr++;
            }

            if (criteria.containsKey("customerClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.arCustomer.arCustomerClass.ccName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerClass");
                ctr++;
            }

            if (criteria.containsKey("salesperson")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.arSalesperson.slpName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("salesperson");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("rct.rctDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateFrom");
                ctr++;
            }

            if (criteria.containsKey("dateTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("rct.rctDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("receiptType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("rct.rctType>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptType");
                ctr++;
            }

            if (criteria.containsKey("receiptNumberFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("rct.rctNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("receiptNumberTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("rct.rctNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptNumberTo");
                ctr++;
            }

            boolean unpostedOnly = false;
            if (criteria.containsKey("unpostedOnly")) {
                String unposted = (String) criteria.get("unpostedOnly");
                if (unposted.equals("YES")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    unpostedOnly = true;
                    jbossQl.append("((rct.rctPosted = 0 AND rct.rctVoid = 0)) ");
                }
            }

            if (criteria.containsKey("includedUnposted") && !unpostedOnly) {
                String unposted = (String) criteria.get("includedUnposted");
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (unposted.equals("NO")) {
                    jbossQl.append("((rct.rctPosted = 1 AND rct.rctVoid = 0) OR (rct.rctPosted = 1 AND rct.rctVoid = 1 AND rct.rctVoidPosted = 0)) ");
                } else {
                    jbossQl.append("rct.rctVoid = 0 ");
                }
            }

            if (criteria.containsKey("includedMiscReceipts")) {
                String includeMiscReceipts = (String) criteria.get("includedMiscReceipts");
                if (includeMiscReceipts.equals("NO")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("rct.rctType = 'COLLECTION' ");
                }
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("rct.rctAdCompany=").append(AD_CMPNY).append(" ");
            if (GROUP_BY.equals("SALESPERSON")) {
                jbossQl.append(" ORDER BY rct.arCustomer.cstCustomerCode ");
            }

            Collection arReceipts = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, 0, 0);
            if (arReceipts.size() == 0) throw new GlobalNoRecordFoundException();
            if (SHOW_ENTRIES) {
                for (Object receipt : arReceipts) {
                    LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                    boolean first = true;
                    String appliedInvoiceNumbers = null;
                    String batchName = null;
                    double total_vat = 0d;

                    Collection arDistributionRecords = arReceipt.getArDistributionRecords();
                    for (Object distributionRecord : arDistributionRecords) {
                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                        ArRepOrRegisterDetails mdetails = new ArRepOrRegisterDetails();
                        mdetails.setOrrDate(arReceipt.getRctDate());
                        mdetails.setOrrReceiptNumber(arReceipt.getRctNumber());
                        mdetails.setOrrReferenceNumber(arReceipt.getRctReferenceNumber());
                        mdetails.setOrrDescription(arReceipt.getRctDescription());
                        mdetails.setOrrCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode());
                        mdetails.setOrrCstCustomerClass(arReceipt.getArCustomer().getArCustomerClass().getCcName());
                        mdetails.setOrrCheckNumber(arReceipt.getRctCheckNo());
                        if (arReceipt.getArCustomer().getArCustomerType() == null) {
                            mdetails.setOrrCstCustomerType("UNDEFINE");
                        } else {
                            mdetails.setOrrCstCustomerType(arReceipt.getArCustomer().getArCustomerType().getCtName());
                        }

                        if (first) {
                            mdetails.setOrrAmount(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                            Debug.print("------------------>arReceipt.getRctNumber()=" + arReceipt.getRctNumber());
                            Debug.print("0.1------------->");
                            // get list of applied invoices (invoice number)
                            if (!arReceipt.getArAppliedInvoices().isEmpty()) {
                                Debug.print("1------------->");
                                Collection arAppliedInvoices = arReceipt.getArAppliedInvoices();
                                Debug.print("2------------->");
                                for (Object appliedInvoice : arAppliedInvoices) {
                                    LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoice;
                                    Debug.print("3------------->");
                                    Debug.print("arAppliedInvoice.getAiCode()=" + arAppliedInvoice.getAiCode());

                                    Collection arDistRcrds = arDistributionRecordHome.findDrsByDrClassAndInvCode("TAX", arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(), AD_CMPNY);

                                    Iterator jVt = arDistRcrds.iterator();
                                    while (jVt.hasNext()) {
                                        LocalArDistributionRecord arDistRcrd = (LocalArDistributionRecord) jVt.next();
                                        total_vat += arDistRcrd.getDrAmount();
                                    }

                                    arDistributionRecords = arDistributionRecordHome.findDrsByDrClassAndInvCode("DEFERRED TAX", arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(), AD_CMPNY);

                                    jVt = arDistRcrds.iterator();
                                    while (jVt.hasNext()) {
                                        LocalArDistributionRecord arDistRcrd = (LocalArDistributionRecord) jVt.next();
                                        total_vat += arDistRcrd.getDrAmount();
                                    }

                                    if (appliedInvoiceNumbers == null) {
                                        try {
                                            batchName = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArInvoiceBatch().getIbName();
                                        } catch (Exception nz) {
                                            batchName = "";
                                        }
                                    } else if (appliedInvoiceNumbers != null && !appliedInvoiceNumbers.contains(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvNumber())) {
                                        appliedInvoiceNumbers += ";" + arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvNumber();
                                        batchName += ";" + arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArInvoiceBatch().getIbName();
                                    }
                                }
                            }
                            first = false;
                        }

                        mdetails.setOrrReceiptAppliedInvoices(appliedInvoiceNumbers);
                        mdetails.setOrrBatchName(batchName);
                        mdetails.setOrrPaymentMethod(arReceipt.getRctPaymentMethod());
                        mdetails.setOrrBankAccount(arReceipt.getAdBankAccount().getBaName());
                        mdetails.setOrderBy(ORDER_BY);
                        mdetails.setOrrCstName(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                        mdetails.setOrrTotalVat(total_vat);

                        if (arReceipt.getArSalesperson() != null) {
                            mdetails.setOrrSlsSalespersonCode(arReceipt.getArSalesperson().getSlpSalespersonCode());
                            mdetails.setOrrSlsName(arReceipt.getArSalesperson().getSlpName());
                        } else {
                            mdetails.setOrrSlsSalespersonCode("");
                            mdetails.setOrrSlsName("");
                        }

                        // distribution record details
                        mdetails.setOrrDrCoaAccountNumber(arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                        mdetails.setOrrDrCoaAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                        if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                            mdetails.setOrrDrDebitAmount(arDistributionRecord.getDrAmount());
                        } else {
                            mdetails.setOrrDrCreditAmount(arDistributionRecord.getDrAmount());
                        }
                        mdetails.setOrrPosted(arReceipt.getRctPosted() == 1 ? "YES" : "NO");
                        list.add(mdetails);
                    }
                }
            } else {
                for (Object receipt : arReceipts) {
                    LocalArReceipt arReceipt = (LocalArReceipt) receipt;
                    ArRepOrRegisterDetails mdetails = new ArRepOrRegisterDetails();
                    mdetails.setOrrDate(arReceipt.getRctDate());
                    mdetails.setOrrReceiptNumber(arReceipt.getRctNumber());
                    mdetails.setOrrPaymentMethod(arReceipt.getRctPaymentMethod());
                    mdetails.setOrrReferenceNumber(arReceipt.getRctReferenceNumber());
                    mdetails.setOrrDescription(arReceipt.getRctDescription());
                    mdetails.setOrrCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode());
                    mdetails.setOrrCstCustomerClass(arReceipt.getArCustomer().getArCustomerClass().getCcName());

                    Collection arDistributionRecords = arReceipt.getArDistributionRecords();
                    Debug.print("arReceipt.getRctNumber()=" + arReceipt.getRctNumber());
                    for (Object distributionRecord : arDistributionRecords) {
                        LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                        String accountType = arDistributionRecord.getGlChartOfAccount().getCoaAccountType();
                        String accountDesc = arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription();

                        if (accountType.equals("REVENUE")) {
                            if (accountDesc.contains("Service") || (accountDesc.contains("Sale") && !accountDesc.contains("Sales Parts"))) {
                                mdetails.setOrrDrCoaAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                            }
                        }
                    }

                    if (INCLUDE_PR) {
                        if (arReceipt.getRctReferenceNumber().length() > 0) {
                            StringBuilder invoiceNumber = new StringBuilder();
                            mdetails.setOrrPrNumber(arReceipt.getRctReferenceNumber());
                            try {
                                LocalArPdc arPdc = arPdcHome.findPdcByReferenceNumber(arReceipt.getRctReferenceNumber(), AD_CMPNY);
                                mdetails.setOrrPrDate(arPdc.getPdcDateReceived());
                                mdetails.setOrrCheckNumber(arPdc.getPdcCheckNumber());
                                mdetails.setOrrCheckDate(arPdc.getPdcMaturityDate());

                                Collection arAppliedInvoices = arPdc.getArAppliedInvoices();
                                Iterator x = arAppliedInvoices.iterator();
                                while (x.hasNext()) {
                                    LocalArAppliedInvoice arAppliedInvoiceNumber = (LocalArAppliedInvoice) x.next();
                                    if (x.hasNext()) {
                                        invoiceNumber.append(arAppliedInvoiceNumber.getArInvoicePaymentSchedule().getArInvoice().getInvNumber()).append(", ");
                                    } else {
                                        invoiceNumber.append(arAppliedInvoiceNumber.getArInvoicePaymentSchedule().getArInvoice().getInvNumber());
                                    }

                                    if (arAppliedInvoiceNumber.getArInvoicePaymentSchedule().getArInvoice().getArSalesperson() != null) {
                                        LocalArSalesperson arSalesperson = arAppliedInvoiceNumber.getArInvoicePaymentSchedule().getArInvoice().getArSalesperson();
                                        mdetails.setOrrSlsSalespersonCode(arSalesperson.getSlpSalespersonCode());
                                        mdetails.setOrrSlsName(arSalesperson.getSlpName());
                                    } else {
                                        LocalArSalesperson arSalesperson = arAppliedInvoiceNumber.getArInvoicePaymentSchedule().getArInvoice().getArSalesperson();
                                        mdetails.setOrrSlsSalespersonCode(" ");
                                        mdetails.setOrrSlsName(" ");
                                    }
                                    mdetails.setOrrInvoiceReferenceNumber(arAppliedInvoiceNumber.getArInvoicePaymentSchedule().getArInvoice().getInvReferenceNumber());
                                    mdetails.setOrrBatchName(arAppliedInvoiceNumber.getArInvoicePaymentSchedule().getArInvoice().getArInvoiceBatch().getIbName());
                                }
                                mdetails.setOrrInvoiceNumber(invoiceNumber.toString());

                            } catch (Exception ex) {
                                mdetails.setOrrPrDate(null);
                                mdetails.setOrrCheckNumber("");
                                mdetails.setOrrCheckDate(null);
                                mdetails.setOrrInvoiceNumber("");
                                mdetails.setOrrBatchName("");
                            }
                        }
                    }

                    if (arReceipt.getArCustomer().getArCustomerType() == null) {
                        mdetails.setOrrCstCustomerType("UNDEFINE");
                    } else {
                        mdetails.setOrrCstCustomerType(arReceipt.getArCustomer().getArCustomerType().getCtName());
                    }
                    mdetails.setOrrAmount(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                    mdetails.setOrrBankAccount(arReceipt.getAdBankAccount().getBaName());
                    mdetails.setOrderBy(ORDER_BY);
                    mdetails.setOrrCstName(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());

                    if (arReceipt.getArSalesperson() != null) {
                        mdetails.setOrrSlsSalespersonCode(arReceipt.getArSalesperson().getSlpSalespersonCode());
                        mdetails.setOrrSlsName(arReceipt.getArSalesperson().getSlpName());
                    } else {
                        mdetails.setOrrSlsSalespersonCode(" ");
                        mdetails.setOrrSlsName(" ");
                    }

                    double TOTAL_NET_AMNT = 0d;
                    double TOTAL_TAX_AMNT = 0d;
                    if (!arReceipt.getArInvoiceLineItems().isEmpty()) {
                        for (Object o : arReceipt.getArInvoiceLineItems()) {
                            LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) o;
                            TOTAL_NET_AMNT += arInvoiceLineItem.getIliAmount();
                            TOTAL_TAX_AMNT += arInvoiceLineItem.getIliTaxAmount();
                        }
                    } else if (!arReceipt.getArInvoiceLines().isEmpty()) {
                        for (Object o : arReceipt.getArInvoiceLines()) {
                            LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) o;
                            TOTAL_NET_AMNT += arInvoiceLine.getIlAmount();
                            TOTAL_TAX_AMNT += arInvoiceLine.getIlTaxAmount();
                        }
                    } else if (!arReceipt.getArAppliedInvoices().isEmpty()) {
                        for (Object o : arReceipt.getArAppliedInvoices()) {
                            LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) o;
                            TOTAL_NET_AMNT += arAppliedInvoice.getAiApplyAmount();
                            if (arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArSalesperson() != null) {
                                LocalArSalesperson arSalesperson = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArSalesperson();
                                mdetails.setOrrSlsSalespersonCode(arSalesperson.getSlpSalespersonCode());
                                mdetails.setOrrSlsName(arSalesperson.getSlpName());
                            } else {
                                LocalArSalesperson arSalesperson = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArSalesperson();
                                mdetails.setOrrSlsSalespersonCode(" ");
                                mdetails.setOrrSlsName(" ");
                            }
                            Collection arDistRcrds = arDistributionRecordHome.findDrsByDrClassAndInvCode("TAX", arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(), AD_CMPNY);

                            Iterator jVt = arDistributionRecords.iterator();
                            double total_vat = 0d;
                            while (jVt.hasNext()) {
                                LocalArDistributionRecord arDistRcrd = (LocalArDistributionRecord) jVt.next();
                                total_vat += arDistRcrd.getDrAmount();
                            }
                            arDistributionRecords = arDistributionRecordHome.findDrsByDrClassAndInvCode("DEFERRED TAX", arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode(), AD_CMPNY);

                            jVt = arDistributionRecords.iterator();
                            while (jVt.hasNext()) {
                                LocalArDistributionRecord arDistRcrd = (LocalArDistributionRecord) jVt.next();
                                total_vat += arDistRcrd.getDrAmount();
                            }
                            mdetails.setOrrTotalVat(total_vat);
                            mdetails.setOrrInvoiceReferenceNumber(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvReferenceNumber());

                            try {
                                mdetails.setOrrBatchName(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getArInvoiceBatch().getIbName());
                            } catch (Exception ex) {
                                mdetails.setOrrBatchName("");
                            }
                        }
                    }
                    mdetails.setOrrReceiptNetAmount(TOTAL_NET_AMNT);
                    mdetails.setOrrReceiptTaxAmount(TOTAL_TAX_AMNT);
                    mdetails.setOrrPosted(arReceipt.getRctPosted() == 1 ? "YES" : "NO");
                    list.add(mdetails);
                }
            }

            if (SHOW_ENTRIES) {
                list.sort(ArRepOrRegisterDetails.CoaAccountNumberComparator);
            }

            switch (GROUP_BY) {
                case "CUSTOMER CODE":
                    list.sort(ArRepOrRegisterDetails.CustomerCodeComparator);
                    break;
                case "CUSTOMER TYPE":
                    list.sort(ArRepOrRegisterDetails.CustomerTypeComparator);
                    break;
                case "CUSTOMER CLASS":
                    list.sort(ArRepOrRegisterDetails.CustomerClassComparator);
                    break;
                case "SALESPERSON":
                    list.sort(ArRepOrRegisterDetails.SalespersonComparator);
                    break;
                default:
                    list.sort(ArRepOrRegisterDetails.NoGroupComparator);
                    break;
            }

            if (SUMMARIZE) {
                list.sort(ArRepOrRegisterDetails.CoaAccountNumberComparator);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ArRepOrRegisterControllerBean getAdCompany");

        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpContact(adCompany.getCmpContact());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpEmail(adCompany.getCmpEmail());
            details.setCmpTin(adCompany.getCmpTin());
            details.setCmpDescription(adCompany.getCmpDescription());
            details.setCmpPhone(adCompany.getCmpPhone());
            return details;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArRbAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArRepORRegisterControllerBean getArRbAll");

        ArrayList list = new ArrayList();

        try {
            Collection arReceiptBatches = arReceiptBatchHome.findOpenRbAll(AD_BRNCH, AD_CMPNY);
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

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("ArRepOrRegisterControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        try {
            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {
            AMOUNT = AMOUNT / CONVERSION_RATE;
        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    public void ejbCreate() throws CreateException {
        Debug.print("ArRepOrRegisterControllerBean ejbCreate");
    }
}