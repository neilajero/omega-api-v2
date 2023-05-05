package com.ejb.txnreports.ar;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.entities.ar.LocalArAppliedInvoice;
import com.ejb.dao.ar.LocalArAppliedInvoiceHome;
import com.ejb.entities.ar.LocalArCustomerClass;
import com.ejb.dao.ar.LocalArCustomerClassHome;
import com.ejb.entities.ar.LocalArCustomerType;
import com.ejb.dao.ar.LocalArCustomerTypeHome;
import com.ejb.entities.ar.LocalArDistributionRecord;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.entities.ar.LocalArInvoiceBatch;
import com.ejb.dao.ar.LocalArInvoiceBatchHome;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.entities.ar.LocalArInvoiceLine;
import com.ejb.entities.ar.LocalArInvoiceLineItem;
import com.ejb.entities.ar.LocalArInvoicePaymentSchedule;
import com.ejb.dao.ar.LocalArInvoicePaymentScheduleHome;
import com.ejb.entities.ar.LocalArJobOrderInvoiceLine;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.entities.ar.LocalArReceiptBatch;
import com.ejb.dao.ar.LocalArReceiptBatchHome;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.entities.ar.LocalArSalesOrderInvoiceLine;
import com.ejb.entities.ar.LocalArSalesperson;
import com.ejb.dao.ar.LocalArSalespersonHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepSalesRegisterDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

import javax.naming.NamingException;

@Stateless(name = "ArRepSalesRegisterControllerEJB")
public class ArRepSalesRegisterControllerBean extends EJBContextClass implements ArRepSalesRegisterController {

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
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalArAppliedInvoiceHome arAppliedInvoiceHome;
    @EJB
    private LocalArSalespersonHome arSalespersonHome;
    @EJB
    private LocalArInvoiceBatchHome arInvoiceBatchHome;
    @EJB
    private LocalArReceiptBatchHome arReceiptBatchHome;


    public void executeSpArRepSalesRegister(String STORED_PROCEDURE, String CUSTOMER_CODE, Date DT_FRM, Date DT_TO, boolean INCLUDE_UNPOSTED, boolean INCLUDE_INVOICE, boolean INCLUDE_COLLECTION, boolean INCLUDE_MISC_RECEIPT, String BRANCH_CODES, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepSalesRegisterControllerBean executeSpArRepSalesRegister");

        try {
            StoredProcedureQuery spQuery = em.createStoredProcedureQuery(STORED_PROCEDURE)
                    .registerStoredProcedureParameter("customerCode", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("dateFrom", Date.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("dateTo", Date.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("includeUnposted", Boolean.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("includeInvoices", Boolean.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("includeCollections", Boolean.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("includeMiscReceipt", Boolean.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("branchCode", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("adCompany", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("resultCount", Integer.class, ParameterMode.OUT);


            spQuery.setParameter("customerCode", CUSTOMER_CODE);
            spQuery.setParameter("dateFrom", DT_FRM);
            spQuery.setParameter("dateTo", DT_TO);
            spQuery.setParameter("includeUnposted", INCLUDE_UNPOSTED);
            spQuery.setParameter("includeInvoices", INCLUDE_INVOICE);
            spQuery.setParameter("includeCollections", INCLUDE_COLLECTION);
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
        Debug.print("ArRepSalesRegisterControllerBean getAdLvCustomerBatchAll");
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

    public ArrayList getAdLvReportTypeAll(Integer AD_CMPNY) {
        Debug.print("ArRepSalesRegisterControllerBean getAdLvReportTypeAll");
        ArrayList list = new ArrayList();
        try {
            Collection adLookUpValues = adLookUpValueHome.findByLuName("AR REPORT TYPE - SALES REGISTER", AD_CMPNY);
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
        Debug.print("ArCustomerEntryControllerBean getArCcAll");
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
        Debug.print("ArCustomerEntryControllerBean getArCtAll");
        LocalArCustomerType arCustomerType;
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

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArCustomerEntryControllerBean getAdBrResAll");
        LocalAdBranchResponsibility adBranchResponsibility;
        LocalAdBranch adBranch;
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

    public ArrayList executeArRepSalesRegisterMemoLine(HashMap criteria, ArrayList branchList, String ORDER_BY, String GROUP_BY, boolean PRINT_SALES_RELATED_ONLY, boolean SHOW_ENTRIES, boolean SUMMARIZE, boolean summary, boolean detailedSales, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        final long startTime = System.currentTimeMillis();
        final long endTime;

        Debug.print("ArRepSalesRegisterControllerBean executeArRepSalesRegisterMemoLine");

        ArrayList list = new ArrayList();
        Collection arReceipts = null;
        String miscReceipts = null;

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            StringBuilder jbossQl = new StringBuilder();
            Iterator brIter = null;
            AdBranchDetails details = null;
            Object[] obj;
            Collection arInvoices = null;

            double GRAND_TOTAL_NET_AMNT = 0d;
            double GRAND_TOTAL_TAX_AMNT = 0d;
            double GRAND_TOTAL_AMNT = 0d;

            if (criteria.containsKey("customerCode")) {
                criteriaSize--;
            }
            if (criteria.containsKey("customerBatch")) {
                criteriaSize--;
            }
            if (criteria.containsKey("unpostedOnly")) {
                criteriaSize--;
            }
            if (criteria.containsKey("includedUnposted")) {
                criteriaSize--;
            }
            if (criteria.containsKey("includedInvoices")) {
                criteriaSize--;
            }
            if (criteria.containsKey("includedMiscReceipts")) {
                criteriaSize--;
            }
            if (criteria.containsKey("includedCreditMemos")) {
                criteriaSize--;
            }
            if (criteria.containsKey("includedDebitMemos")) {
                criteriaSize--;
            }
            if (criteria.containsKey("paymentStatus")) {
                criteriaSize--;
            }
            if (criteria.containsKey("includedPriorDues")) {
                criteriaSize--;
            }
            if (criteria.containsKey("includedCollections")) {
                criteriaSize--;
            }
            if (criteria.containsKey("posted")) {
                criteriaSize--;
            }
            if (criteria.containsKey("orderStatus")) {
                criteriaSize--;
            }
            if (criteria.containsKey("summary")) {
                criteriaSize--;
            }
            if (criteria.containsKey("detailedSales")) {
                criteriaSize--;
            }

            if (criteria.get("includedInvoices").equals("YES") || criteria.get("includedCreditMemos").equals("YES") || criteria.get("includedDebitMemos").equals("YES")) {
                jbossQl.append("SELECT OBJECT(inv) FROM ArInvoice inv WHERE (");

                if (branchList.isEmpty()) throw new GlobalNoRecordFoundException();
                brIter = branchList.iterator();
                details = (AdBranchDetails) brIter.next();
                jbossQl.append("inv.invAdBranch=").append(details.getBrCode());

                while (brIter.hasNext()) {
                    details = (AdBranchDetails) brIter.next();
                    jbossQl.append(" OR inv.invAdBranch=").append(details.getBrCode());
                }

                jbossQl.append(") ");
                firstArgument = false;

                // Allocate the size of the object parameter
                obj = new Object[criteriaSize];
                if (criteria.containsKey("customerCode")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
                }

                if (criteria.containsKey("customerBatch")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.arCustomer.cstCustomerBatch LIKE '%").append(criteria.get("customerBatch")).append("%' ");
                }

                if (criteria.containsKey("invoiceBatchName")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.arInvoiceBatch.ibName=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("invoiceBatchName");
                    ctr++;
                }

                if (criteria.containsKey("customerType")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.arCustomer.arCustomerType.ctName=?").append(ctr + 1).append(" ");
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
                    jbossQl.append("inv.arCustomer.arCustomerClass.ccName=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("customerClass");
                    ctr++;
                }

                if (criteria.containsKey("dateFrom")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.invDate>=?").append(ctr + 1).append(" ");
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
                    jbossQl.append("inv.invDate<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateTo");
                    ctr++;
                }

                if (criteria.containsKey("invoiceNumberFrom")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.invNumber>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("invoiceNumberFrom");
                    ctr++;
                }

                if (criteria.containsKey("invoiceNumberTo")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.invNumber<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("invoiceNumberTo");
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
                        jbossQl.append("inv.invPosted = 0 AND inv.invVoid = 0 ");
                    }
                }

                if (criteria.containsKey("includedUnposted") && unpostedOnly == false) {
                    String unposted = (String) criteria.get("includedUnposted");
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    if (unposted == null) unposted = "NO";
                    if (unposted.equals("NO")) {
                        jbossQl.append("inv.invPosted = 1 ");
                    } else {
                        jbossQl.append("inv.invVoid = 0 ");
                    }
                }

                if (criteria.containsKey("salesperson")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.arSalesperson.slpName=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("salesperson");
                    ctr++;
                }

                if (criteria.containsKey("paymentStatus")) {
                    String paymentStatus = (String) criteria.get("paymentStatus");
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    if (paymentStatus.equals("PAID")) {
                        jbossQl.append("inv.invAmountDue = inv.invAmountPaid ");
                    } else if (paymentStatus.equals("UNPAID")) {
                        jbossQl.append("inv.invAmountDue <> inv.invAmountPaid ");
                    }
                }

                if (criteria.containsKey("region")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.arCustomer.cstAdLvRegion=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("region");
                    ctr++;
                }

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                boolean firstSource = true;
                if (criteria.get("includedCreditMemos").equals("YES")) {
                    jbossQl.append("(inv.invCreditMemo = 1 ");
                    firstSource = false;
                }

                if (criteria.get("includedDebitMemos").equals("YES")) {
                    if (!firstSource) {
                        jbossQl.append("OR ");
                    } else {
                        jbossQl.append("(");
                    }
                    jbossQl.append("inv.invDebitMemo = 1 ");
                    firstSource = false;
                }

                if (criteria.get("includedInvoices").equals("YES")) {
                    if (!firstSource) {
                        jbossQl.append("OR ");
                    } else {
                        jbossQl.append("(");
                    }
                    jbossQl.append("(inv.invCreditMemo = 0 AND inv.invDebitMemo = 0)");
                }

                jbossQl.append(") ");
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.invAdCompany=").append(AD_CMPNY).append(" ");

                String orderBy = null;
                if (GROUP_BY.equals("SALESPERSON") || GROUP_BY.equals("PRODUCT")) {
                    orderBy = "inv.arCustomer.cstCustomerCode";
                }

                if (orderBy != null) {
                    jbossQl.append("ORDER BY ").append(orderBy);
                }

                arInvoices = arInvoiceHome.getInvByCriteria(jbossQl.toString(), obj, 0, 0);

                String refNum = "";
                if (!arInvoices.isEmpty()) {
                    for (Object invoice : arInvoices) {
                        LocalArInvoice arInvoice = (LocalArInvoice) invoice;
                        double REVENUE_AMOUNT = 0d;

                        Collection arDistributionRecords = arInvoice.getArDistributionRecords();
                        for (Object record : arDistributionRecords) {
                            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) record;
                            if (arDistributionRecord.getGlChartOfAccount().getCoaAccountType().equals("REVENUE")) {
                                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                                    REVENUE_AMOUNT -= arDistributionRecord.getDrAmount();
                                } else {
                                    REVENUE_AMOUNT += arDistributionRecord.getDrAmount();
                                }
                            }
                        }

                        if (PRINT_SALES_RELATED_ONLY) {
                            boolean isSalesRelated = false;
                            for (Object distributionRecord : arDistributionRecords) {
                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;
                                if (arDistributionRecord.getGlChartOfAccount().getCoaAccountType().equals("REVENUE")) {
                                    isSalesRelated = true;
                                    break;
                                }
                            }
                            if (!isSalesRelated) continue;
                        }

                        if (SHOW_ENTRIES) {
                            boolean first = true;
                            for (Object distributionRecord : arDistributionRecords) {
                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;
                                ArRepSalesRegisterDetails mdetails = new ArRepSalesRegisterDetails();
                                mdetails.setSrDate(arInvoice.getInvDate());
                                mdetails.setSrInvoiceNumber(arInvoice.getInvNumber());
                                mdetails.setSrReferenceNumber(arInvoice.getInvReferenceNumber());
                                refNum = (arInvoice.getInvReferenceNumber());
                                mdetails.setSrDescription(arInvoice.getInvDescription());
                                mdetails.setSrCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode() + "-" + arInvoice.getArCustomer().getCstName());
                                mdetails.setSrCstCustomerClass(arInvoice.getArCustomer().getArCustomerClass().getCcName());
                                mdetails.setSrCstName(arInvoice.getArCustomer().getCstName());
                                mdetails.setSrCstCustomerCode2(arInvoice.getArCustomer().getCstCustomerCode());
                                mdetails.setPosted(arInvoice.getInvPosted());
                                if (arInvoice.getArCustomer().getArCustomerType() == null) {
                                    mdetails.setSrCstCustomerType("UNDEFINE");
                                    mdetails.setPosted(arInvoice.getInvPosted());
                                } else {
                                    mdetails.setSrCstCustomerType(arInvoice.getArCustomer().getArCustomerType().getCtName());
                                    mdetails.setPosted(arInvoice.getInvPosted());
                                }

                                if (first) {
                                    double AMNT_DUE;
                                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {
                                        AMNT_DUE = EJBCommon.roundIt(arInvoice.getInvAmountDue(), adCompany.getGlFunctionalCurrency().getFcPrecision());
                                    } else {
                                        LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);
                                        AMNT_DUE = EJBCommon.roundIt(arInvoice.getInvAmountDue() * -1, adCompany.getGlFunctionalCurrency().getFcPrecision());
                                    }
                                    mdetails.setSrAmount(AMNT_DUE);
                                    mdetails.setPosted(arInvoice.getInvPosted());
                                }
                                mdetails.setOrderBy(ORDER_BY);
                                mdetails.setPosted(arInvoice.getInvPosted());

                                double TOTAL_NET_AMNT = 0d;
                                double TOTAL_TAX_AMNT = 0d;
                                double TOTAL_DISC_AMNT = 0d;

                                // compute net amount, tax amount and discount amount
                                if (!arInvoice.getArInvoiceLines().isEmpty()) {
                                    for (Object o : arInvoice.getArInvoiceLines()) {
                                        LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) o;
                                        TOTAL_NET_AMNT += arInvoiceLine.getIlAmount();
                                        TOTAL_TAX_AMNT += arInvoiceLine.getIlTaxAmount();

                                        mdetails.setSrMemoName(arInvoiceLine.getArStandardMemoLine().getSmlName());
                                        if (arInvoiceLine.getArStandardMemoLine().getSmlName().trim().equals("CPF 7%") && refNum == arInvoiceLine.getArInvoice().getInvReferenceNumber()) {
                                            mdetails.setSrCpf(arInvoiceLine.getIlAmount());
                                        }
                                        if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {
                                            if (TOTAL_NET_AMNT != 0) {
                                                TOTAL_NET_AMNT = TOTAL_NET_AMNT * -1;
                                            }
                                            if (TOTAL_TAX_AMNT != 0) {
                                                TOTAL_TAX_AMNT = TOTAL_TAX_AMNT * -1;
                                            }
                                        }
                                        mdetails.setSrNetAmount(TOTAL_NET_AMNT);
                                        mdetails.setSrTaxAmount(TOTAL_TAX_AMNT);
                                        mdetails.setPosted(arInvoice.getInvPosted());

                                        if (first) {
                                            mdetails.setSrDiscountAmount(TOTAL_DISC_AMNT);
                                            first = false;
                                        }

                                        if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {
                                            if (arInvoice.getArSalesperson() != null) {
                                                mdetails.setSrSlsSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                                                mdetails.setSrSlsName(arInvoice.getArSalesperson().getSlpName());
                                                mdetails.setPosted(arInvoice.getInvPosted());
                                            } else {
                                                mdetails.setSrSlsSalespersonCode("");
                                                mdetails.setSrSlsName("");
                                                mdetails.setPosted(arInvoice.getInvPosted());
                                            }
                                        } else {
                                            try {
                                                LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                                                if (arCreditedInvoice.getArSalesperson() != null) {
                                                    mdetails.setSrSlsSalespersonCode(arCreditedInvoice.getArSalesperson().getSlpSalespersonCode());
                                                    mdetails.setSrSlsName(arCreditedInvoice.getArSalesperson().getSlpName());
                                                    mdetails.setPosted(arInvoice.getInvPosted());
                                                } else {
                                                    mdetails.setSrSlsSalespersonCode("");
                                                    mdetails.setSrSlsName("");
                                                    mdetails.setPosted(arInvoice.getInvPosted());
                                                }

                                            } catch (FinderException ex) {

                                            }
                                        }

                                        // set distribution record details
                                        mdetails.setSrDrGlAccountNumber(arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                                        mdetails.setSrDrGlAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                                        mdetails.setPosted(arInvoice.getInvPosted());
                                        if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                                            mdetails.setSrDrDebitAmount(arDistributionRecord.getDrAmount());
                                            mdetails.setPosted(arInvoice.getInvPosted());
                                        } else {
                                            mdetails.setSrDrCreditAmount(arDistributionRecord.getDrAmount());
                                            mdetails.setPosted(arInvoice.getInvPosted());
                                        }
                                        mdetails.setSrRevenueAmount(REVENUE_AMOUNT);
                                        list.add(mdetails);
                                    }
                                }
                            }
                        } else {
                            String customerType = "";
                            byte pstd = 0;
                            if (arInvoice.getArCustomer().getArCustomerType() == null) {
                                customerType = "UNDEFINE";
                                pstd = arInvoice.getInvPosted();
                            } else {
                                customerType = arInvoice.getArCustomer().getArCustomerType().getCtName();
                                pstd = arInvoice.getInvPosted();
                            }

                            double TOTAL_NET_AMNT = 0d;
                            double TOTAL_TAX_AMNT = 0d;
                            double TOTAL_DISC_AMNT = 0d;
                            double TOTAL_QTY = 0d;
                            double AMNT = 0d;
                            double TAX_AMNT = 0d;
                            double TOTAL_AMNT = 0d;

                            double AMNT_DUE = 0d;

                            if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {
                                AMNT_DUE = EJBCommon.roundIt(arInvoice.getInvAmountDue(), adCompany.getGlFunctionalCurrency().getFcPrecision());
                            } else {
                                LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                                AMNT_DUE = EJBCommon.roundIt(arInvoice.getInvAmountDue() * -1, adCompany.getGlFunctionalCurrency().getFcPrecision());

                            }

                            // compute net amount, tax amount and discount amount
                            if (!arInvoice.getArInvoiceLines().isEmpty()) {

                                Iterator j = arInvoice.getArInvoiceLines().iterator();
                                String customerCode = "";
                                double AMOUNT = 0;

                                while (j.hasNext()) {
                                    ArRepSalesRegisterDetails mdetails = new ArRepSalesRegisterDetails();
                                    mdetails.setSrDate(arInvoice.getInvDate());
                                    mdetails.setSrInvoiceNumber(arInvoice.getInvNumber());
                                    mdetails.setSrReferenceNumber(arInvoice.getInvReferenceNumber());
                                    mdetails.setSrDescription(arInvoice.getInvDescription());
                                    customerCode = arInvoice.getArCustomer().getCstCustomerCode();
                                    mdetails.setSrCstCustomerCode(customerCode + "-" + arInvoice.getArCustomer().getCstName());
                                    mdetails.setSrCstCustomerClass(arInvoice.getArCustomer().getArCustomerClass().getCcName());
                                    mdetails.setSrCstName(arInvoice.getArCustomer().getCstName());
                                    mdetails.setSrCstCustomerCode2(arInvoice.getArCustomer().getCstCustomerCode());
                                    mdetails.setPosted(arInvoice.getInvPosted());

                                    mdetails.setSrCstCustomerType(customerType);
                                    mdetails.setPosted(pstd);
                                    mdetails.setSrAmount(AMNT_DUE);
                                    mdetails.setOrderBy(ORDER_BY);
                                    mdetails.setPosted(arInvoice.getInvPosted());

                                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) j.next();
                                    TOTAL_NET_AMNT += arInvoiceLine.getIlAmount();
                                    TOTAL_TAX_AMNT += arInvoiceLine.getIlTaxAmount();

                                    AMNT = EJBCommon.roundIt(arInvoiceLine.getIlAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision());

                                    TAX_AMNT = EJBCommon.roundIt(arInvoiceLine.getIlTaxAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision());

                                    TOTAL_AMNT = AMNT + TAX_AMNT;

                                    mdetails.setSrLineSAmount(AMNT);
                                    mdetails.setSrLineTaxAmount(TAX_AMNT);
                                    mdetails.setSrLineAmount(TOTAL_AMNT);

                                    mdetails.setSrMemoName(arInvoiceLine.getArStandardMemoLine().getSmlName());

                                    if (arInvoiceLine.getArStandardMemoLine().getSmlName().trim().equals("CPF 7%") && refNum == arInvoiceLine.getArInvoice().getInvReferenceNumber()) {
                                        mdetails.setSrCpf(arInvoiceLine.getIlAmount());
                                    }

                                    TOTAL_QTY = arInvoiceLine.getIlQuantity();

                                    mdetails.setSrQuantity(TOTAL_QTY);

                                    if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {
                                        mdetails.setSrInvCreditMemo(true);
                                        mdetails.setPosted(arInvoice.getInvPosted());
                                        mdetails.setCmInvoiceNumber(arInvoice.getInvCmInvoiceNumber());
                                    } else {
                                        mdetails.setSrInvCreditMemo(false);
                                        mdetails.setPosted(arInvoice.getInvPosted());
                                    }

                                    mdetails.setSrNetAmount(TOTAL_NET_AMNT);
                                    mdetails.setSrTaxAmount(TOTAL_TAX_AMNT);
                                    mdetails.setSrDiscountAmount(TOTAL_DISC_AMNT);

                                    mdetails.setSrServiceAmount(0d);

                                    mdetails.setPosted(arInvoice.getInvPosted());

                                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                                        if (arInvoice.getArSalesperson() != null) {

                                            mdetails.setSrSlsSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                                            mdetails.setSrSlsName(arInvoice.getArSalesperson().getSlpName());
                                            mdetails.setPosted(arInvoice.getInvPosted());
                                        } else {
                                            mdetails.setSrSlsSalespersonCode("");
                                            mdetails.setSrSlsName("");
                                            mdetails.setPosted(arInvoice.getInvPosted());
                                        }

                                    } else {

                                        try {

                                            LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                                            if (arCreditedInvoice.getArSalesperson() != null) {

                                                mdetails.setSrSlsSalespersonCode(arCreditedInvoice.getArSalesperson().getSlpSalespersonCode());
                                                mdetails.setSrSlsName(arCreditedInvoice.getArSalesperson().getSlpName());
                                                mdetails.setPosted(arInvoice.getInvPosted());
                                            } else {
                                                mdetails.setSrSlsSalespersonCode("");
                                                mdetails.setSrSlsName("");
                                                mdetails.setPosted(arInvoice.getInvPosted());
                                            }

                                        } catch (FinderException ex) {

                                        }
                                    }

                                    if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {

                                        GRAND_TOTAL_NET_AMNT -= TOTAL_NET_AMNT;
                                        GRAND_TOTAL_TAX_AMNT -= TOTAL_TAX_AMNT;
                                        GRAND_TOTAL_AMNT -= (TOTAL_NET_AMNT + TOTAL_TAX_AMNT);

                                    } else {

                                        GRAND_TOTAL_NET_AMNT += TOTAL_NET_AMNT;
                                        GRAND_TOTAL_TAX_AMNT += TOTAL_TAX_AMNT;
                                        GRAND_TOTAL_AMNT += (TOTAL_NET_AMNT + TOTAL_TAX_AMNT);
                                    }

                                    mdetails.setSrRctType("INVOICE RELEASES");
                                    mdetails.setPosted(arInvoice.getInvPosted());
                                    // get receipt info

                                    StringBuilder invoiceReceiptNumbers = null;
                                    StringBuilder invoiceReceiptDates = null;

                                    Collection arInvoiceReceipts = arReceiptHome.findByInvCodeAndRctVoid(arInvoice.getInvCode(), EJBCommon.FALSE, AD_CMPNY);

                                    for (Object arInvoiceReceipt : arInvoiceReceipts) {

                                        LocalArReceipt arReceipt = (LocalArReceipt) arInvoiceReceipt;

                                        if (invoiceReceiptNumbers == null) {
                                            invoiceReceiptNumbers = new StringBuilder(arReceipt.getRctNumber());
                                        } else if (invoiceReceiptNumbers != null && !invoiceReceiptNumbers.toString().contains(arReceipt.getRctNumber())) {
                                            invoiceReceiptNumbers.append("/").append(arReceipt.getRctNumber());
                                        }

                                        if (invoiceReceiptDates == null) {
                                            invoiceReceiptDates = new StringBuilder(EJBCommon.convertSQLDateToString(arReceipt.getRctDate()));
                                        } else if (invoiceReceiptDates != null && !invoiceReceiptDates.toString().contains(EJBCommon.convertSQLDateToString(arReceipt.getRctDate()))) {
                                            invoiceReceiptDates.append("/").append(EJBCommon.convertSQLDateToString(arReceipt.getRctDate()));
                                        }
                                    }

                                    mdetails.setSrInvReceiptNumbers(invoiceReceiptNumbers.toString());
                                    mdetails.setSrInvReceiptDates(invoiceReceiptDates.toString());
                                    mdetails.setPosted(arInvoice.getInvPosted());
                                    mdetails.setSrRevenueAmount(REVENUE_AMOUNT);
                                    list.add(mdetails);
                                }
                            }
                        }
                    }
                }
            }

            if (criteria.get("includedPriorDues").equals("YES")) {
                jbossQl = new StringBuilder();
                jbossQl.append("SELECT OBJECT(inv) FROM ArInvoice inv WHERE (");
                if (branchList.isEmpty()) throw new GlobalNoRecordFoundException();

                brIter = branchList.iterator();
                details = (AdBranchDetails) brIter.next();
                jbossQl.append("inv.invAdBranch=").append(details.getBrCode());

                while (brIter.hasNext()) {
                    details = (AdBranchDetails) brIter.next();
                    jbossQl.append(" OR inv.invAdBranch=").append(details.getBrCode());
                }

                jbossQl.append(") ");
                firstArgument = false;

                ctr = 0;

                // Allocate the size of the object parameter
                obj = new Object[criteriaSize];
                if (criteria.containsKey("customerCode")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
                }

                if (criteria.containsKey("customerBatch")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.arCustomer.cstCustomerBatch LIKE '%").append(criteria.get("customerBatch")).append("%' ");
                }

                if (criteria.containsKey("customerType")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.arCustomer.arCustomerType.ctName=?").append(ctr + 1).append(" ");
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
                    jbossQl.append("inv.arCustomer.arCustomerClass.ccName=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("customerClass");
                    ctr++;
                }

                if (criteria.containsKey("dateFrom")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.invDate<?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");
                    ctr++;
                }

                if (criteria.containsKey("salesperson")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.arSalesperson.slpName=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("salesperson");
                    ctr++;
                }

                if (criteria.containsKey("dateTo")) {
                    obj[ctr] = criteria.get("dateTo");
                    ctr++;
                }

                if (criteria.containsKey("invoiceNumberFrom")) {
                    obj[ctr] = criteria.get("invoiceNumberFrom");
                    ctr++;
                }

                if (criteria.containsKey("invoiceNumberTo")) {
                    obj[ctr] = criteria.get("invoiceNumberTo");
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
                        jbossQl.append("inv.invPosted = 0 AND inv.invVoid = 0 ");
                    }
                }

                if (criteria.containsKey("includedUnPosted") && unpostedOnly == false) {
                    String unPosted = (String) criteria.get("includedUnPosted");
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    if (unPosted == null) unPosted = "NO";
                    if (unPosted.equals("NO")) {
                        jbossQl.append("inv.invPosted = 1");
                    } else {
                        jbossQl.append("inv.invVoid = 0 ");
                    }
                }

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                boolean firstSource = true;
                if (criteria.get("includedCreditMemos").equals("YES")) {
                    jbossQl.append("(inv.invCreditMemo = 1 ");
                    firstSource = false;
                }

                if (criteria.get("includedDebitMemos").equals("YES")) {
                    if (!firstSource) {
                        jbossQl.append("OR ");
                    } else {
                        jbossQl.append("(");
                    }
                    jbossQl.append("inv.invDebitMemo = 1 ");
                    firstSource = false;
                }

                if (criteria.get("includedInvoices").equals("YES")) {
                    if (!firstSource) {
                        jbossQl.append("OR ");
                    } else {
                        jbossQl.append("(");
                    }
                    jbossQl.append("(inv.invCreditMemo = 0 AND inv.invDebitMemo = 0)");
                }

                jbossQl.append(") ");
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("inv.invAdCompany=").append(AD_CMPNY).append(" ");

                String orderBy = null;
                if (GROUP_BY.equals("SALESPERSON") || GROUP_BY.equals("PRODUCT")) {
                    orderBy = "inv.arCustomer.cstCustomerCode";
                }

                if (orderBy != null) {
                    jbossQl.append("ORDER BY ").append(orderBy);
                }

                String refNum = "";

                arInvoices = arInvoiceHome.getInvByCriteria(jbossQl.toString(), obj, 0, 0);

                if (!arInvoices.isEmpty()) {

                    for (Object invoice : arInvoices) {

                        LocalArInvoice arInvoice = (LocalArInvoice) invoice;

                        double REVENUE_AMOUNT = 0d;

                        Collection arDistributionRecords = arInvoice.getArDistributionRecords();

                        for (Object record : arDistributionRecords) {

                            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) record;

                            if (arDistributionRecord.getGlChartOfAccount().getCoaAccountType().equals("REVENUE")) {

                                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                                    REVENUE_AMOUNT -= arDistributionRecord.getDrAmount();
                                } else {
                                    REVENUE_AMOUNT += arDistributionRecord.getDrAmount();
                                }
                            }
                        }

                        if (PRINT_SALES_RELATED_ONLY) {

                            boolean isSalesRelated = false;

                            arDistributionRecords = arInvoice.getArDistributionRecords();

                            for (Object distributionRecord : arDistributionRecords) {

                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                                if (arDistributionRecord.getGlChartOfAccount().getCoaAccountType().equals("REVENUE")) {

                                    isSalesRelated = true;
                                    break;
                                }
                            }

                            if (!isSalesRelated) continue;
                        }

                        if (SHOW_ENTRIES) {

                            boolean first = true;

                            arDistributionRecords = arInvoice.getArDistributionRecords();

                            for (Object distributionRecord : arDistributionRecords) {

                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                                ArRepSalesRegisterDetails mdetails = new ArRepSalesRegisterDetails();
                                mdetails.setSrDate(arInvoice.getInvDate());
                                mdetails.setSrInvoiceNumber(arInvoice.getInvNumber());
                                mdetails.setSrReferenceNumber(arInvoice.getInvReferenceNumber());
                                refNum = arInvoice.getInvReferenceNumber();
                                mdetails.setSrDescription(arInvoice.getInvDescription());
                                mdetails.setSrCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode() + "-" + arInvoice.getArCustomer().getCstName());
                                mdetails.setSrCstCustomerClass(arInvoice.getArCustomer().getArCustomerClass().getCcName());
                                mdetails.setSrCstName(arInvoice.getArCustomer().getCstName());
                                mdetails.setSrCstCustomerCode2(arInvoice.getArCustomer().getCstCustomerCode());
                                mdetails.setPosted(arInvoice.getInvPosted());

                                // type
                                if (arInvoice.getArCustomer().getArCustomerType() == null) {

                                    mdetails.setSrCstCustomerType("UNDEFINE");

                                } else {

                                    mdetails.setSrCstCustomerType(arInvoice.getArCustomer().getArCustomerType().getCtName());
                                }

                                if (first) {

                                    double AMNT_DUE = 0d;

                                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                                        AMNT_DUE = EJBCommon.roundIt(arInvoice.getInvAmountDue(), adCompany.getGlFunctionalCurrency().getFcPrecision());

                                    } else {

                                        LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                                        AMNT_DUE = EJBCommon.roundIt(arInvoice.getInvAmountDue() * -1, adCompany.getGlFunctionalCurrency().getFcPrecision());

                                    }

                                    mdetails.setSrAmount(AMNT_DUE);
                                    mdetails.setPosted(arInvoice.getInvPosted());
                                    // first = false;

                                }

                                mdetails.setOrderBy(ORDER_BY);
                                mdetails.setPosted(arInvoice.getInvPosted());
                                double TOTAL_NET_AMNT = 0d;
                                double TOTAL_TAX_AMNT = 0d;
                                double TOTAL_DISC_AMNT = 0d;

                                // compute net amount, tax amount and discount amount
                                if (!arInvoice.getArInvoiceLines().isEmpty()) {

                                    for (Object o : arInvoice.getArInvoiceLines()) {

                                        LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) o;
                                        arInvoiceLine.getArStandardMemoLine().getSmlName();
                                        TOTAL_NET_AMNT += arInvoiceLine.getIlAmount();
                                        TOTAL_TAX_AMNT += arInvoiceLine.getIlTaxAmount();

                                        mdetails.setSrMemoName(arInvoiceLine.getArStandardMemoLine().getSmlName());

                                        if (arInvoiceLine.getArStandardMemoLine().getSmlName().trim().equals("CPF 7%") && refNum == arInvoiceLine.getArInvoice().getInvReferenceNumber()) {
                                            mdetails.setSrCpf(arInvoiceLine.getIlAmount());
                                        }
                                        mdetails.setSrQuantity(arInvoiceLine.getIlQuantity());

                                        if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {

                                            if (TOTAL_NET_AMNT != 0) {

                                                TOTAL_NET_AMNT = TOTAL_NET_AMNT * -1;
                                            }

                                            if (TOTAL_TAX_AMNT != 0) {

                                                TOTAL_TAX_AMNT = TOTAL_TAX_AMNT * -1;
                                            }
                                        }

                                        mdetails.setSrNetAmount(TOTAL_NET_AMNT);
                                        mdetails.setSrTaxAmount(TOTAL_TAX_AMNT);
                                        mdetails.setPosted(arInvoice.getInvPosted());

                                        if (first) {

                                            mdetails.setSrDiscountAmount(TOTAL_DISC_AMNT);
                                            first = false;
                                        }

                                        if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                                            if (arInvoice.getArSalesperson() != null) {

                                                mdetails.setSrSlsSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                                                mdetails.setSrSlsName(arInvoice.getArSalesperson().getSlpName());
                                                mdetails.setPosted(arInvoice.getInvPosted());

                                            } else {
                                                mdetails.setSrSlsSalespersonCode("");
                                                mdetails.setSrSlsName("");
                                                mdetails.setPosted(arInvoice.getInvPosted());
                                            }

                                        } else {

                                            try {

                                                LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                                                if (arCreditedInvoice.getArSalesperson() != null) {

                                                    mdetails.setSrSlsSalespersonCode(arCreditedInvoice.getArSalesperson().getSlpSalespersonCode());
                                                    mdetails.setSrSlsName(arCreditedInvoice.getArSalesperson().getSlpName());
                                                    mdetails.setPosted(arInvoice.getInvPosted());

                                                } else {
                                                    mdetails.setSrSlsSalespersonCode("");
                                                    mdetails.setSrSlsName("");
                                                    mdetails.setPosted(arInvoice.getInvPosted());
                                                }

                                            } catch (FinderException ex) {

                                            }
                                        }

                                        // set distribution record details
                                        mdetails.setSrDrGlAccountNumber(arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                                        mdetails.setSrDrGlAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                                        if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                                            mdetails.setSrDrDebitAmount(arDistributionRecord.getDrAmount());
                                            mdetails.setPosted(arInvoice.getInvPosted());

                                        } else {

                                            mdetails.setSrDrCreditAmount(arDistributionRecord.getDrAmount());
                                            mdetails.setPosted(arInvoice.getInvPosted());
                                        }

                                        mdetails.setSrRevenueAmount(REVENUE_AMOUNT);
                                        list.add(mdetails);
                                    }
                                }
                            }

                        } else {

                            ArRepSalesRegisterDetails mdetails = new ArRepSalesRegisterDetails();
                            mdetails.setSrDate(arInvoice.getInvDate());
                            mdetails.setSrInvoiceNumber(arInvoice.getInvNumber());
                            mdetails.setSrReferenceNumber(arInvoice.getInvReferenceNumber());
                            refNum = arInvoice.getInvReferenceNumber();
                            mdetails.setSrDescription(arInvoice.getInvDescription());
                            mdetails.setSrCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode() + "-" + arInvoice.getArCustomer().getCstName());
                            mdetails.setSrCstCustomerClass(arInvoice.getArCustomer().getArCustomerClass().getCcName());
                            mdetails.setSrCstName(arInvoice.getArCustomer().getCstName());
                            mdetails.setSrCstCustomerCode2(arInvoice.getArCustomer().getCstCustomerCode());
                            mdetails.setPosted(arInvoice.getInvPosted());

                            arDistributionRecords = arInvoice.getArDistributionRecords();

                            for (Object distributionRecord : arDistributionRecords) {

                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                                if (AD_CMPNY == 15) {
                                    if (arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Sales") || arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Service")) {
                                        mdetails.setSrDrGlAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                                    }
                                } else {
                                    if ((arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Sales") && (arDistributionRecord.getGlChartOfAccount().getCoaCode() != 5971 && arDistributionRecord.getGlChartOfAccount().getCoaCode() != 6259)) || arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Service")) {
                                        mdetails.setSrDrGlAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                                    }
                                }
                            }

                            // type
                            if (arInvoice.getArCustomer().getArCustomerType() == null) {

                                mdetails.setSrCstCustomerType("UNDEFINE");
                                mdetails.setPosted(arInvoice.getInvPosted());

                            } else {

                                mdetails.setSrCstCustomerType(arInvoice.getArCustomer().getArCustomerType().getCtName());
                                mdetails.setPosted(arInvoice.getInvPosted());
                            }

                            double AMNT_DUE = 0d;

                            if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                                AMNT_DUE = EJBCommon.roundIt(arInvoice.getInvAmountDue(), adCompany.getGlFunctionalCurrency().getFcPrecision());

                            } else {

                                LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                                AMNT_DUE = EJBCommon.roundIt(arInvoice.getInvAmountDue() * -1, adCompany.getGlFunctionalCurrency().getFcPrecision());

                            }

                            mdetails.setSrAmount(AMNT_DUE);
                            mdetails.setOrderBy(ORDER_BY);
                            mdetails.setPosted(arInvoice.getInvPosted());

                            double TOTAL_NET_AMNT = 0d;
                            double TOTAL_TAX_AMNT = 0d;
                            double TOTAL_DISC_AMNT = 0d;

                            // compute net amount, tax amount and discount amount
                            if (!arInvoice.getArInvoiceLines().isEmpty()) {

                                for (Object o : arInvoice.getArInvoiceLines()) {

                                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) o;

                                    TOTAL_NET_AMNT += arInvoiceLine.getIlAmount();
                                    TOTAL_TAX_AMNT += arInvoiceLine.getIlTaxAmount();

                                    mdetails.setSrMemoName(arInvoiceLine.getArStandardMemoLine().getSmlName());
                                    if (arInvoiceLine.getArStandardMemoLine().getSmlName().trim().equals("CPF 7%") && refNum == arInvoiceLine.getArInvoice().getInvReferenceNumber()) {
                                        mdetails.setSrCpf(arInvoiceLine.getIlAmount());
                                    }
                                    mdetails.setSrQuantity(arInvoiceLine.getIlQuantity());
                                    if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {

                                        mdetails.setSrInvCreditMemo(true);
                                        mdetails.setPosted(arInvoice.getInvPosted());

                                    } else {

                                        mdetails.setSrInvCreditMemo(false);
                                        mdetails.setPosted(arInvoice.getInvPosted());
                                    }

                                    mdetails.setSrNetAmount(TOTAL_NET_AMNT);
                                    mdetails.setSrTaxAmount(TOTAL_TAX_AMNT);
                                    mdetails.setSrDiscountAmount(TOTAL_DISC_AMNT);
                                    mdetails.setPosted(arInvoice.getInvPosted());

                                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                                        if (arInvoice.getArSalesperson() != null) {

                                            mdetails.setSrSlsSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                                            mdetails.setSrSlsName(arInvoice.getArSalesperson().getSlpName());
                                            mdetails.setPosted(arInvoice.getInvPosted());

                                        } else {
                                            mdetails.setSrSlsSalespersonCode("");
                                            mdetails.setSrSlsName("");
                                            mdetails.setPosted(arInvoice.getInvPosted());
                                        }

                                    } else {

                                        try {

                                            LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                                            if (arCreditedInvoice.getArSalesperson() != null) {

                                                mdetails.setSrSlsSalespersonCode(arCreditedInvoice.getArSalesperson().getSlpSalespersonCode());
                                                mdetails.setSrSlsName(arCreditedInvoice.getArSalesperson().getSlpName());
                                                mdetails.setPosted(arCreditedInvoice.getInvPosted());

                                            } else {
                                                mdetails.setSrSlsSalespersonCode("");
                                                mdetails.setSrSlsName("");
                                                mdetails.setPosted(arCreditedInvoice.getInvPosted());
                                            }

                                        } catch (FinderException ex) {

                                        }
                                    }

                                    if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {
                                        mdetails.setCmInvoiceNumber(arInvoice.getInvCmInvoiceNumber());
                                        GRAND_TOTAL_NET_AMNT -= TOTAL_NET_AMNT;
                                        GRAND_TOTAL_TAX_AMNT -= TOTAL_TAX_AMNT;
                                        GRAND_TOTAL_AMNT -= (TOTAL_NET_AMNT + TOTAL_TAX_AMNT);

                                    } else {

                                        GRAND_TOTAL_NET_AMNT += TOTAL_NET_AMNT;
                                        GRAND_TOTAL_TAX_AMNT += TOTAL_TAX_AMNT;
                                        GRAND_TOTAL_AMNT += (TOTAL_NET_AMNT + TOTAL_TAX_AMNT);
                                    }

                                    mdetails.setSrRctType("INVOICE");

                                    // get receipt info

                                    StringBuilder invoiceReceiptNumbers = null;
                                    StringBuilder invoiceReceiptDates = null;

                                    Collection arInvoiceReceipts = arReceiptHome.findByInvCodeAndRctVoid(arInvoice.getInvCode(), EJBCommon.FALSE, AD_CMPNY);

                                    for (Object arInvoiceReceipt : arInvoiceReceipts) {

                                        LocalArReceipt arReceipt = (LocalArReceipt) arInvoiceReceipt;

                                        if (invoiceReceiptNumbers == null) {
                                            invoiceReceiptNumbers = new StringBuilder(arReceipt.getRctNumber());
                                        } else if (invoiceReceiptNumbers != null && !invoiceReceiptNumbers.toString().contains(arReceipt.getRctNumber())) {
                                            invoiceReceiptNumbers.append("/").append(arReceipt.getRctNumber());
                                        }

                                        if (invoiceReceiptDates == null) {
                                            invoiceReceiptDates = new StringBuilder(EJBCommon.convertSQLDateToString(arReceipt.getRctDate()));
                                        } else if (invoiceReceiptDates != null && !invoiceReceiptDates.toString().contains(EJBCommon.convertSQLDateToString(arReceipt.getRctDate()))) {
                                            invoiceReceiptDates.append("/").append(EJBCommon.convertSQLDateToString(arReceipt.getRctDate()));
                                        }
                                    }

                                    mdetails.setSrInvReceiptNumbers(invoiceReceiptNumbers.toString());
                                    mdetails.setSrInvReceiptDates(invoiceReceiptDates.toString());
                                    mdetails.setSrRevenueAmount(REVENUE_AMOUNT);
                                    list = getPriorDues(arInvoice, mdetails, criteria, list);
                                }
                            }
                        }
                    }
                }
            }

            if (criteria.get("includedMiscReceipts").equals("YES")) {
                // misc receipt

                jbossQl = new StringBuilder();
                jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct WHERE (");

                brIter = branchList.iterator();

                details = (AdBranchDetails) brIter.next();
                jbossQl.append("rct.rctAdBranch=").append(details.getBrCode());

                while (brIter.hasNext()) {

                    details = (AdBranchDetails) brIter.next();

                    jbossQl.append(" OR rct.rctAdBranch=").append(details.getBrCode());
                }

                jbossQl.append(") ");
                firstArgument = false;

                ctr = 0;

                if (criteria.containsKey("invoiceNumberFrom")) {

                    criteriaSize--;
                }

                if (criteria.containsKey("invoiceNumberTo")) {

                    criteriaSize--;
                }

                if (criteria.containsKey("invoiceBatchName")) obj = new Object[criteriaSize - 1];
                else obj = new Object[criteriaSize];

                if (criteria.containsKey("customerCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("rct.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
                }

                if (criteria.containsKey("customerBatch")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("rct.arCustomer.cstCustomerBatch LIKE '%").append(criteria.get("customerBatch")).append("%' ");
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
                        jbossQl.append("rct.rctPosted = 0 AND rct.rctVoid = 0 ");
                    }
                }

                if (criteria.containsKey("includedUnposted") && unpostedOnly == false) {

                    String unposted = (String) criteria.get("includedUnposted");

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    if (unposted == null) unposted = "NO";
                    if (unposted.equals("NO")) {

                        jbossQl.append("rct.rctPosted = 1 ");

                    } else {

                        jbossQl.append("rct.rctVoid = 0 ");
                    }
                }

                if (criteria.containsKey("region")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("rct.arCustomer.cstAdLvRegion=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("region");
                    ctr++;
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctType='MISC' AND rct.rctAdCompany=").append(AD_CMPNY).append(" ");

                String orderBy = null;

                if (GROUP_BY.equals("SALESPERSON") || GROUP_BY.equals("PRODUCT")) {

                    orderBy = "rct.arCustomer.cstCustomerCode";
                }

                if (orderBy != null) {

                    jbossQl.append("ORDER BY ").append(orderBy);
                }

                arReceipts = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, 0, 0);

                if (!arReceipts.isEmpty()) {

                    for (Object receipt : arReceipts) {

                        LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                        Collection arDistributionRecords = arReceipt.getArDistributionRecords();

                        double REVENUE_AMOUNT = 0d;

                        for (Object distributionRecord : arDistributionRecords) {

                            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                            if (arDistributionRecord.getGlChartOfAccount().getCoaAccountType().equals("REVENUE")) {

                                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                                    REVENUE_AMOUNT -= arDistributionRecord.getDrAmount();
                                } else {
                                    REVENUE_AMOUNT += arDistributionRecord.getDrAmount();
                                }
                            }
                        }

                        for (Object o : arReceipt.getArInvoiceLines()) {

                            LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) o;

                            ArRepSalesRegisterDetails mdetails = new ArRepSalesRegisterDetails();
                            mdetails.setSrDate(arReceipt.getRctDate());
                            mdetails.setSrInvoiceNumber(arReceipt.getRctNumber());
                            mdetails.setSrMemoName(arInvoiceLine.getArStandardMemoLine().getSmlName());
                            mdetails.setSrAmount(arInvoiceLine.getIlAmount());
                            mdetails.setSrReferenceNumber(arReceipt.getRctReferenceNumber());
                            mdetails.setSrDescription(arReceipt.getRctDescription());
                            mdetails.setSrCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode() + "-" + (arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName()));
                            mdetails.setSrCstCustomerClass(arReceipt.getArCustomer().getArCustomerClass().getCcName());
                            mdetails.setSrCstName(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                            mdetails.setSrCstCustomerCode2(arReceipt.getArCustomer().getCstCustomerCode());
                            mdetails.setSrRctType("MISC");
                            mdetails.setSrRevenueAmount(REVENUE_AMOUNT);
                            list.add(mdetails);
                        }
                    }
                }
            }

            if (criteria.get("includedCollections").equals("YES")) {

                // collections

                jbossQl = new StringBuilder();
                jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct WHERE (");

                brIter = branchList.iterator();

                details = (AdBranchDetails) brIter.next();
                jbossQl.append("rct.rctAdBranch=").append(details.getBrCode());

                while (brIter.hasNext()) {

                    details = (AdBranchDetails) brIter.next();

                    jbossQl.append(" OR rct.rctAdBranch=").append(details.getBrCode());
                }

                jbossQl.append(") ");
                firstArgument = false;

                ctr = 0;

                if (criteria.containsKey("invoiceNumberFrom")) {

                    criteriaSize--;
                }

                if (criteria.containsKey("invoiceNumberTo")) {

                    criteriaSize--;
                }

                if (criteria.containsKey("invoiceBatchName")) obj = new Object[criteriaSize - 1];
                else obj = new Object[criteriaSize];

                if (criteria.containsKey("customerCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("rct.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
                }

                if (criteria.containsKey("customerBatch")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("rct.arCustomer.cstCustomerBatch LIKE '%").append(criteria.get("customerBatch")).append("%' ");
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
                        jbossQl.append("rct.rctPosted = 0 AND rct.rctVoid = 0 ");
                    }
                }

                if (criteria.containsKey("includedUnposted") && unpostedOnly == false) {

                    String unposted = (String) criteria.get("includedUnposted");

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    if (unposted == null) unposted = "NO";
                    if (unposted.equals("NO")) {

                        jbossQl.append("rct.rctPosted = 1 ");

                    } else {

                        jbossQl.append("rct.rctVoid = 0 ");
                    }
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctType='COLLECTION' AND rct.rctAdCompany=").append(AD_CMPNY).append(" ");

                String orderBy = null;

                if (GROUP_BY.equals("SALESPERSON") || GROUP_BY.equals("PRODUCT")) {

                    orderBy = "rct.arCustomer.cstCustomerCode";
                }

                if (orderBy != null) {

                    jbossQl.append("ORDER BY ").append(orderBy);
                }

                Debug.print("jbossQl.toString() 3 : " + jbossQl.toString());
                arReceipts = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, 0, 0);

                if (!arReceipts.isEmpty()) {

                    for (Object receipt : arReceipts) {

                        LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                        if (PRINT_SALES_RELATED_ONLY) {

                            boolean isSalesRelated = false;

                            Collection arDistributionRecords = arReceipt.getArDistributionRecords();

                            for (Object distributionRecord : arDistributionRecords) {

                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                                if (arDistributionRecord.getGlChartOfAccount().getCoaAccountType().equals("REVENUE")) {

                                    isSalesRelated = true;
                                    break;
                                }
                            }

                            if (!isSalesRelated) continue;
                        }

                        if (SHOW_ENTRIES) {

                            boolean first = true;

                            Collection arDistributionRecords = arReceipt.getArDistributionRecords();

                            for (Object distributionRecord : arDistributionRecords) {

                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                                ArRepSalesRegisterDetails mdetails = new ArRepSalesRegisterDetails();
                                mdetails.setSrDate(arReceipt.getRctDate());
                                mdetails.setSrInvoiceNumber(arReceipt.getRctNumber());
                                mdetails.setSrReferenceNumber(arReceipt.getRctReferenceNumber());
                                mdetails.setSrDescription(arReceipt.getRctDescription());
                                mdetails.setSrCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode() + "-" + (arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName()));
                                mdetails.setSrCstCustomerClass(arReceipt.getArCustomer().getArCustomerClass().getCcName());
                                mdetails.setSrCstName(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                                mdetails.setSrCstCustomerCode2(arReceipt.getArCustomer().getCstCustomerCode());
                                mdetails.setPosted(arReceipt.getRctPosted());
                                // type
                                if (arReceipt.getArCustomer().getArCustomerType() == null) {

                                    mdetails.setSrCstCustomerType("UNDEFINE");
                                    mdetails.setPosted(arReceipt.getRctPosted());

                                } else {

                                    mdetails.setSrCstCustomerType(arReceipt.getArCustomer().getArCustomerType().getCtName());
                                    mdetails.setPosted(arReceipt.getRctPosted());
                                }

                                double AMNT_DUE = 0d;

                                AMNT_DUE = EJBCommon.roundIt(arReceipt.getRctAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision());

                                mdetails.setSrAmount(AMNT_DUE);

                                mdetails.setOrderBy(ORDER_BY);

                                mdetails.setPosted(arReceipt.getRctPosted());
                                // set distribution record details
                                mdetails.setSrDrGlAccountNumber(arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                                mdetails.setSrDrGlAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                                    mdetails.setSrDrDebitAmount(arDistributionRecord.getDrAmount());
                                    mdetails.setPosted(arReceipt.getRctPosted());

                                } else {

                                    mdetails.setSrDrCreditAmount(arDistributionRecord.getDrAmount());
                                    mdetails.setPosted(arReceipt.getRctPosted());
                                }
                            }

                        } else {

                            ArRepSalesRegisterDetails mdetails = new ArRepSalesRegisterDetails();
                            mdetails.setSrDate(arReceipt.getRctDate());
                            mdetails.setSrInvoiceNumber(arReceipt.getRctNumber());
                            mdetails.setSrReferenceNumber(arReceipt.getRctReferenceNumber());
                            mdetails.setSrDescription(arReceipt.getRctDescription());
                            mdetails.setSrCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode() + "-" + (arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName()));
                            mdetails.setSrCstCustomerClass(arReceipt.getArCustomer().getArCustomerClass().getCcName());
                            mdetails.setSrCstName(arReceipt.getArCustomer().getCstName());
                            mdetails.setSrCstCustomerCode2(arReceipt.getArCustomer().getCstCustomerCode());
                            mdetails.setPosted(arReceipt.getRctPosted());

                            Collection arDistributionRecords = arReceipt.getArDistributionRecords();

                            for (Object distributionRecord : arDistributionRecords) {

                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                                if (AD_CMPNY == 15) {
                                    if (arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Sales") || arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Service")) {
                                        mdetails.setSrDrGlAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                                    }
                                } else {
                                    if ((arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Sales") && (arDistributionRecord.getGlChartOfAccount().getCoaCode() != 5971 && arDistributionRecord.getGlChartOfAccount().getCoaCode() != 6259)) || arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Service")) {
                                        mdetails.setSrDrGlAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                                    }
                                }
                            }

                            // type
                            if (arReceipt.getArCustomer().getArCustomerType() == null) {

                                mdetails.setSrCstCustomerType("UNDEFINE");

                            } else {

                                mdetails.setSrCstCustomerType(arReceipt.getArCustomer().getArCustomerType().getCtName());
                                mdetails.setPosted(arReceipt.getRctPosted());
                            }

                            double AMNT_DUE = 0d;

                            AMNT_DUE = EJBCommon.roundIt(arReceipt.getRctAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision());

                            mdetails.setSrAmount(AMNT_DUE * -1);

                            mdetails.setOrderBy(ORDER_BY);
                            mdetails.setPosted(arReceipt.getRctPosted());

                            double TOTAL_NET_AMNT = 0d;
                            double TOTAL_TAX_AMNT = 0d;

                            if (!arReceipt.getArInvoiceLines().isEmpty()) {

                                for (Object value : arReceipt.getArInvoiceLines()) {

                                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) value;

                                    TOTAL_NET_AMNT += arInvoiceLine.getIlAmount();
                                    TOTAL_TAX_AMNT += arInvoiceLine.getIlTaxAmount();

                                    mdetails.setSrMemoName(arInvoiceLine.getArStandardMemoLine().getSmlName());
                                    mdetails.setSrQuantity(arInvoiceLine.getIlQuantity());
                                    mdetails.setSrNetAmount(TOTAL_NET_AMNT * -1);
                                    mdetails.setSrTaxAmount(TOTAL_TAX_AMNT * -1);
                                    mdetails.setPosted(arReceipt.getRctPosted());

                                    if (arReceipt.getArSalesperson() != null) {

                                        mdetails.setSrSlsSalespersonCode(arReceipt.getArSalesperson().getSlpSalespersonCode());
                                        mdetails.setSrSlsName(arReceipt.getArSalesperson().getSlpName());
                                        mdetails.setPosted(arReceipt.getRctPosted());
                                    } else {
                                        mdetails.setSrSlsSalespersonCode("");
                                        mdetails.setSrSlsName("");
                                        mdetails.setPosted(arReceipt.getRctPosted());
                                    }

                                    GRAND_TOTAL_NET_AMNT -= TOTAL_NET_AMNT;
                                    GRAND_TOTAL_TAX_AMNT -= TOTAL_TAX_AMNT;
                                    GRAND_TOTAL_AMNT -= (TOTAL_NET_AMNT + TOTAL_TAX_AMNT);

                                    mdetails.setSrRctType("COLLECTION");

                                    double SR_AI_APPLY_AMNT = 0;
                                    double SR_AI_CRDTBL_W_TXAMNT = 0;
                                    double SR_AI_DSCNT_AMNT = 0;
                                    double SR_AI_APPLD_DPST = 0;

                                    for (Object o : arReceipt.getArAppliedInvoices()) {

                                        LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) o;
                                        SR_AI_APPLY_AMNT += arAppliedInvoice.getAiApplyAmount();
                                        SR_AI_CRDTBL_W_TXAMNT += arAppliedInvoice.getAiCreditableWTax();
                                        SR_AI_DSCNT_AMNT += arAppliedInvoice.getAiDiscountAmount();
                                        SR_AI_APPLD_DPST += arAppliedInvoice.getAiAppliedDeposit();
                                    }

                                    // Set Apllied Invoice
                                    mdetails.setSrAiApplyAmount(SR_AI_APPLY_AMNT);
                                    mdetails.setSrAiCreditableWTax(SR_AI_CRDTBL_W_TXAMNT);
                                    mdetails.setSrAiDiscountAmount(SR_AI_DSCNT_AMNT);
                                    mdetails.setSrAiAppliedDeposit(SR_AI_APPLD_DPST);

                                    list.add(mdetails);
                                }
                            }
                        }
                    }
                }
            }

            if (list.isEmpty() || list.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            if (SHOW_ENTRIES) {
                list.sort(ArRepSalesRegisterDetails.CoaAccountNumberComparator);
            }

            switch (GROUP_BY) {
                case "CUSTOMER CODE":
                    list.sort(ArRepSalesRegisterDetails.CustomerCodeComparator);
                    break;
                case "CUSTOMER TYPE":
                    list.sort(ArRepSalesRegisterDetails.CustomerTypeComparator);
                    break;
                case "CUSTOMER CLASS":
                    list.sort(ArRepSalesRegisterDetails.CustomerClassComparator);
                    break;
                case "MEMO LINE":
                    list.sort(ArRepSalesRegisterDetails.MemoLineComparator);
                    break;
                case "SALESPERSON":
                    list.sort(ArRepSalesRegisterDetails.SalespersonComparator);
                    break;
                case "PRODUCT":
                    list.sort(ArRepSalesRegisterDetails.ProductComparator);
                    break;
                default:
                    list.sort(ArRepSalesRegisterDetails.NoGroupComparator);
                    break;
            }

            if (SUMMARIZE) {
                list.sort(ArRepSalesRegisterDetails.CoaAccountNumberComparator);
            }

            ArRepSalesRegisterDetails mdetails = (ArRepSalesRegisterDetails) list.get(list.size() - 1);
            mdetails.setSrInvTotalNetAmount(GRAND_TOTAL_NET_AMNT);
            mdetails.setSrInvTotalTaxAmount(GRAND_TOTAL_TAX_AMNT);
            mdetails.setSrInvTotalAmount(GRAND_TOTAL_AMNT);

            endTime = System.currentTimeMillis();
            long seconds = TimeUnit.MILLISECONDS.toSeconds((endTime - startTime));
            Debug.print("Total execution time of executeArRepSalesRegisterMemoLine in seconds : " + seconds);

            return list;

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeArRepSalesRegister(HashMap criteria, ArrayList branchList, String ORDER_BY, String GROUP_BY, boolean PRINT_SALES_RELATED_ONLY, boolean SHOW_ENTRIES, boolean SUMMARIZE, boolean detailedSales, boolean summary, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        final long startTime = System.currentTimeMillis();
        final long endTime;

        Debug.print("ArRepSalesRegisterControllerBean executeArRepSalesRegister");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            StringBuilder jbossQl = new StringBuilder();
            Iterator brIter = null;
            AdBranchDetails details = null;
            Object[] obj;
            Collection arInvoices = null;

            double GRAND_TOTAL_NET_AMNT = 0d;
            double GRAND_TOTAL_TAX_AMNT = 0d;
            double GRAND_TOTAL_AMNT = 0d;
            double GRAND_TOTAL_GROSS_AMNT = 0d;

            criteriaSize = getCriteriaSize(criteria, criteriaSize);

            if (criteria.get("includedInvoices").equals("YES") || criteria.get("includedCreditMemos").equals("YES") || criteria.get("includedDebitMemos").equals("YES")) {

                jbossQl.append("SELECT OBJECT(inv) FROM ArInvoice inv WHERE ");

                if (branchList.isEmpty()) {
                    throw new GlobalNoRecordFoundException();
                }

                brIter = branchList.iterator();
                while (brIter.hasNext()) {
                    details = (AdBranchDetails) brIter.next();
                    jbossQl.append(" inv.invAdBranch IN (").append(details.getBrCode());
                }

                jbossQl.append(") ");
                firstArgument = false;

                // Allocate the size of the object parameter
                obj = new Object[criteriaSize];

                if (criteria.containsKey("customerCode")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.arCustomer.cstCustomerCode = '").append(criteria.get("customerCode")).append("' ");
                }

                if (criteria.containsKey("customerBatch")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.arCustomer.cstCustomerBatch = '").append(criteria.get("customerBatch")).append("' ");
                }

                if (criteria.containsKey("invoiceBatchName")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.arInvoiceBatch.ibName=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("invoiceBatchName");
                    ctr++;
                }

                if (criteria.containsKey("customerType")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.arCustomer.arCustomerType.ctName=?").append(ctr + 1).append(" ");
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
                    jbossQl.append("inv.arCustomer.arCustomerClass.ccName=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("customerClass");
                    ctr++;
                }

                if (criteria.containsKey("dateFrom")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.invDate>=?").append(ctr + 1).append(" ");
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
                    jbossQl.append("inv.invDate<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateTo");
                    ctr++;
                }

                if (criteria.containsKey("invoiceNumberFrom")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.invNumber>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("invoiceNumberFrom");
                    ctr++;
                }

                if (criteria.containsKey("invoiceNumberTo")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.invNumber<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("invoiceNumberTo");
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
                        jbossQl.append("inv.invPosted = 0 AND inv.invVoid = 0 ");
                    }
                }

                if (criteria.containsKey("includedUnposted") && unpostedOnly == false) {
                    String unposted = (String) criteria.get("includedUnposted");
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    if (unposted == null) unposted = "NO";
                    if (unposted.equals("NO")) {
                        jbossQl.append("inv.invPosted = 1 ");
                    } else {
                        jbossQl.append("inv.invVoid = 0 ");
                    }
                }

                if (criteria.containsKey("paymentStatus")) {
                    String paymentStatus = (String) criteria.get("paymentStatus");
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    if (paymentStatus.equals("PAID")) {
                        jbossQl.append("inv.invAmountDue = inv.invAmountPaid ");
                    } else if (paymentStatus.equals("UNPAID")) {
                        jbossQl.append("inv.invAmountDue <> inv.invAmountPaid ");
                    }
                }

                if (criteria.containsKey("region")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.arCustomer.cstAdLvRegion=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("region");
                    ctr++;
                }

                if (!firstArgument) {
                    jbossQl.append(" AND ");
                } else {
                    jbossQl.append("WHERE ");
                }

                boolean firstSource = true;
                boolean includeCM = criteria.get("includedCreditMemos").equals("YES");
                boolean includeDM = criteria.get("includedDebitMemos").equals("YES");
                boolean includeINV = criteria.get("includedInvoices").equals("YES");

                jbossQl.append("inv.invAdCompany=").append(AD_CMPNY).append(" ");

                String orderBy = null;
                if (GROUP_BY.equals("SALESPERSON") || GROUP_BY.equals("PRODUCT")) {
                    orderBy = "inv.arCustomer.cstCustomerCode";
                }
                if (orderBy != null) {
                    jbossQl.append("ORDER BY ").append(orderBy);
                }

                arInvoices = arInvoiceHome.getInvByCriteria(jbossQl.toString(), obj, 0, 0);
                String refNum = "";
                String cst = "";

                if (!arInvoices.isEmpty()) {
                    for (Object invoice : arInvoices) {
                        LocalArInvoice arInvoice = (LocalArInvoice) invoice;
                        if (includeINV) {
                            if (!includeCM && arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {
                                continue;
                            }
                            if (!includeDM && arInvoice.getInvDebitMemo() == EJBCommon.TRUE) {
                                continue;
                            }
                        }

                        if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {
                            try {
                                LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                            } catch (FinderException ex) {
                                continue;
                            }
                        }

                        double REVENUE_AMOUNT = 0d;
                        Collection arDistributionRecords = arInvoice.getArDistributionRecords();
                        for (Object record : arDistributionRecords) {
                            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) record;
                            if (arDistributionRecord.getGlChartOfAccount().getCoaAccountType().equals("REVENUE")) {
                                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                                    REVENUE_AMOUNT -= arDistributionRecord.getDrAmount();
                                } else {
                                    REVENUE_AMOUNT += arDistributionRecord.getDrAmount();
                                }
                            }
                        }

                        if (PRINT_SALES_RELATED_ONLY) {
                            boolean isSalesRelated = false;
                            arDistributionRecords = arInvoice.getArDistributionRecords();
                            for (Object distributionRecord : arDistributionRecords) {
                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;
                                if (arDistributionRecord.getGlChartOfAccount().getCoaAccountType().equals("REVENUE")) {
                                    isSalesRelated = true;
                                    break;
                                }
                            }
                            if (!isSalesRelated) continue;
                        }

                        if (SHOW_ENTRIES) {
                            boolean first = true;
                            arDistributionRecords = arInvoice.getArDistributionRecords();
                            for (Object distributionRecord : arDistributionRecords) {
                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;
                                ArRepSalesRegisterDetails mdetails = new ArRepSalesRegisterDetails();
                                mdetails.setSrDate(arInvoice.getInvDate());
                                mdetails.setSrInvoiceNumber(arInvoice.getInvNumber());
                                mdetails.setSrReferenceNumber(arInvoice.getInvReferenceNumber());
                                refNum = arInvoice.getInvReferenceNumber();
                                mdetails.setSrDescription(arInvoice.getInvDescription());
                                mdetails.setSrCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode() + "-" + arInvoice.getArCustomer().getCstName());
                                mdetails.setSrCstCustomerClass(arInvoice.getArCustomer().getArCustomerClass().getCcName());
                                mdetails.setSrCstName(arInvoice.getArCustomer().getCstName());
                                mdetails.setSrCstCustomerCode2(arInvoice.getArCustomer().getCstCustomerCode());
                                mdetails.setPosted(arInvoice.getInvPosted());
                                if (arInvoice.getArCustomer().getArCustomerType() == null) {
                                    mdetails.setSrCstCustomerType("UNDEFINE");
                                    mdetails.setPosted(arInvoice.getInvPosted());
                                } else {
                                    mdetails.setSrCstCustomerType(arInvoice.getArCustomer().getArCustomerType().getCtName());
                                    mdetails.setPosted(arInvoice.getInvPosted());
                                }

                                if (first) {
                                    double AMNT_DUE = 0d;
                                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {
                                        AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arInvoice.getInvAmountDue(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                                    } else {
                                        LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                                        AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arInvoice.getInvAmountDue(), AD_CMPNY) * -1, adCompany.getGlFunctionalCurrency().getFcPrecision());
                                    }
                                    mdetails.setSrAmount(AMNT_DUE);
                                    mdetails.setPosted(arInvoice.getInvPosted());
                                }
                                mdetails.setOrderBy(ORDER_BY);
                                mdetails.setPosted(arInvoice.getInvPosted());

                                double TOTAL_NET_AMNT = 0d;
                                double TOTAL_TAX_AMNT = 0d;
                                double TOTAL_DISC_AMNT = 0d;
                                double TOTAL_GROSS_AMNT = 0d;

                                // compute net amount, tax amount and discount amount
                                if (!arInvoice.getArInvoiceLineItems().isEmpty()) {
                                    for (Object o : arInvoice.getArInvoiceLineItems()) {
                                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) o;
                                        TOTAL_GROSS_AMNT += arInvoiceLineItem.getIliAmount() - arInvoiceLineItem.getIliTaxAmount();
                                        TOTAL_NET_AMNT += arInvoiceLineItem.getIliAmount();
                                        TOTAL_TAX_AMNT += arInvoiceLineItem.getIliTaxAmount();
                                        TOTAL_DISC_AMNT += arInvoiceLineItem.getIliTotalDiscount();
                                    }
                                } else if (!arInvoice.getArInvoiceLines().isEmpty()) {
                                    for (Object o : arInvoice.getArInvoiceLines()) {
                                        LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) o;
                                        TOTAL_NET_AMNT += arInvoiceLine.getIlAmount();
                                        TOTAL_TAX_AMNT += arInvoiceLine.getIlTaxAmount();
                                        TOTAL_GROSS_AMNT += arInvoiceLine.getIlAmount() - arInvoiceLine.getIlTaxAmount();
                                    }
                                } else if (!arInvoice.getArSalesOrderInvoiceLines().isEmpty()) {
                                    for (Object o : arInvoice.getArSalesOrderInvoiceLines()) {
                                        LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) o;
                                        TOTAL_NET_AMNT += arSalesOrderInvoiceLine.getSilAmount();
                                        TOTAL_TAX_AMNT += arSalesOrderInvoiceLine.getSilTaxAmount();
                                        TOTAL_DISC_AMNT += arSalesOrderInvoiceLine.getSilTotalDiscount();
                                        TOTAL_GROSS_AMNT += TOTAL_NET_AMNT - TOTAL_TAX_AMNT;
                                    }
                                } else if (!arInvoice.getArJobOrderInvoiceLines().isEmpty()) {
                                    for (Object o : arInvoice.getArJobOrderInvoiceLines()) {
                                        LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) o;
                                        TOTAL_NET_AMNT += arJobOrderInvoiceLine.getJilAmount();
                                        TOTAL_TAX_AMNT += arJobOrderInvoiceLine.getJilTaxAmount();
                                        TOTAL_DISC_AMNT += arJobOrderInvoiceLine.getJilTotalDiscount();
                                        TOTAL_GROSS_AMNT += TOTAL_NET_AMNT - TOTAL_TAX_AMNT;
                                    }
                                } else if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE && arInvoice.getArInvoiceLineItems().isEmpty()) {
                                    try {
                                        arDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode("TAX", arInvoice.getInvCode(), AD_CMPNY);
                                        TOTAL_TAX_AMNT = arDistributionRecord.getDrAmount();
                                        TOTAL_NET_AMNT = arInvoice.getInvAmountDue() - TOTAL_TAX_AMNT;
                                    } catch (FinderException ex) {
                                        TOTAL_TAX_AMNT = 0;
                                        TOTAL_NET_AMNT = arInvoice.getInvAmountDue();
                                    }
                                }

                                if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {
                                    if (TOTAL_NET_AMNT != 0) {
                                        TOTAL_NET_AMNT = TOTAL_NET_AMNT * -1;
                                    }
                                    if (TOTAL_TAX_AMNT != 0) {
                                        TOTAL_TAX_AMNT = TOTAL_TAX_AMNT * -1;
                                    }
                                }
                                mdetails.setSrGrossAmount(TOTAL_GROSS_AMNT);
                                mdetails.setSrNetAmount(TOTAL_NET_AMNT);
                                mdetails.setSrTaxAmount(TOTAL_TAX_AMNT);
                                mdetails.setPosted(arInvoice.getInvPosted());

                                if (first) {
                                    mdetails.setSrDiscountAmount(TOTAL_DISC_AMNT);
                                    first = false;
                                }

                                if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {
                                    if (arInvoice.getArSalesperson() != null) {
                                        if (criteria.containsKey("salesperson")) {
                                            String slsprsn = (String) criteria.get("salesperson");
                                            if (!slsprsn.equals(arInvoice.getArSalesperson().getSlpName())) {
                                                continue;
                                            }
                                        }
                                        mdetails.setSrSlsSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                                        mdetails.setSrSlsName(arInvoice.getArSalesperson().getSlpName());
                                        mdetails.setPosted(arInvoice.getInvPosted());
                                    } else {
                                        if (criteria.containsKey("salesperson")) {
                                            continue;
                                        }
                                        mdetails.setSrSlsSalespersonCode("");
                                        mdetails.setSrSlsName("");
                                        mdetails.setPosted(arInvoice.getInvPosted());
                                    }
                                } else {
                                    try {
                                        LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);
                                        if (arCreditedInvoice.getArSalesperson() != null) {
                                            if (criteria.containsKey("salesperson")) {
                                                String slsprsn = (String) criteria.get("salesperson");
                                                if (!slsprsn.equals(arCreditedInvoice.getArSalesperson().getSlpName())) {
                                                    continue;
                                                }
                                            }
                                            mdetails.setSrSlsSalespersonCode(arCreditedInvoice.getArSalesperson().getSlpSalespersonCode());
                                            mdetails.setSrSlsName(arCreditedInvoice.getArSalesperson().getSlpName());
                                            mdetails.setPosted(arInvoice.getInvPosted());
                                        } else {
                                            if (criteria.containsKey("salesperson")) {
                                                continue;
                                            }
                                            mdetails.setSrSlsSalespersonCode("");
                                            mdetails.setSrSlsName("");
                                            mdetails.setPosted(arInvoice.getInvPosted());
                                        }
                                    } catch (FinderException ex) {

                                    }
                                }

                                // set distribution record details
                                mdetails.setSrDrGlAccountNumber(arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                                mdetails.setSrDrGlAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                                mdetails.setPosted(arInvoice.getInvPosted());

                                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                                    mdetails.setSrDrDebitAmount(arDistributionRecord.getDrAmount());
                                    mdetails.setPosted(arInvoice.getInvPosted());
                                } else {
                                    mdetails.setSrDrCreditAmount(arDistributionRecord.getDrAmount());
                                    mdetails.setPosted(arInvoice.getInvPosted());
                                }
                                mdetails.setSrRevenueAmount(REVENUE_AMOUNT);
                                list.add(mdetails);
                            }
                        } else {
                            ArRepSalesRegisterDetails mdetails = new ArRepSalesRegisterDetails();
                            mdetails.setSrDate(arInvoice.getInvDate());
                            mdetails.setSrInvoiceNumber(arInvoice.getInvNumber());
                            mdetails.setSrReferenceNumber(arInvoice.getInvReferenceNumber());
                            refNum = arInvoice.getInvReferenceNumber();
                            mdetails.setSrDescription(arInvoice.getInvDescription());
                            mdetails.setSrCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode() + "-" + arInvoice.getArCustomer().getCstName());
                            mdetails.setSrCstCustomerClass(arInvoice.getArCustomer().getArCustomerClass().getCcName());
                            mdetails.setSrCstName(arInvoice.getArCustomer().getCstName());
                            mdetails.setSrCstCustomerCode2(arInvoice.getArCustomer().getCstCustomerCode());
                            mdetails.setPosted(arInvoice.getInvPosted());
                            if (arInvoice.getArCustomer().getArCustomerType() == null) {
                                mdetails.setSrCstCustomerType("UNDEFINE");
                                mdetails.setPosted(arInvoice.getInvPosted());
                            } else {
                                mdetails.setSrCstCustomerType(arInvoice.getArCustomer().getArCustomerType().getCtName());
                                mdetails.setPosted(arInvoice.getInvPosted());
                            }

                            double AMNT_DUE = 0d;
                            double AMOUNT = 0d;
                            if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {
                                AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arInvoice.getInvAmountDue(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());
                            } else {
                                try {
                                    LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);
                                    AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arInvoice.getInvAmountDue(), AD_CMPNY) * -1, adCompany.getGlFunctionalCurrency().getFcPrecision());

                                } catch (FinderException ex) {
                                }
                            }

                            mdetails.setSrAmount(AMNT_DUE);
                            mdetails.setOrderBy(ORDER_BY);
                            mdetails.setPosted(arInvoice.getInvPosted());

                            double TOTAL_NET_AMNT = 0d;
                            double TOTAL_GROSS_AMNT = 0d;
                            double TOTAL_TAX_AMNT = 0d;
                            double TOTAL_DISC_AMNT = 0d;

                            // compute net amount, tax amount and discount amount
                            if (!arInvoice.getArInvoiceLineItems().isEmpty()) {
                                for (Object o : arInvoice.getArInvoiceLineItems()) {
                                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) o;
                                    TOTAL_GROSS_AMNT += arInvoiceLineItem.getIliAmount() + arInvoiceLineItem.getIliTaxAmount() + arInvoiceLineItem.getIliTotalDiscount();
                                    TOTAL_NET_AMNT += arInvoiceLineItem.getIliAmount();
                                    TOTAL_TAX_AMNT += arInvoiceLineItem.getIliTaxAmount();
                                    TOTAL_DISC_AMNT += arInvoiceLineItem.getIliTotalDiscount();
                                }
                            } else if (!arInvoice.getArInvoiceLines().isEmpty()) {
                                for (Object o : arInvoice.getArInvoiceLines()) {
                                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) o;
                                    mdetails.setSrQuantity(arInvoiceLine.getIlQuantity());
                                    TOTAL_GROSS_AMNT += arInvoiceLine.getIlAmount() + arInvoiceLine.getIlTaxAmount();
                                    TOTAL_NET_AMNT += arInvoiceLine.getIlAmount();
                                    TOTAL_TAX_AMNT += arInvoiceLine.getIlTaxAmount();
                                }
                            } else if (!arInvoice.getArSalesOrderInvoiceLines().isEmpty()) {
                                for (Object o : arInvoice.getArSalesOrderInvoiceLines()) {
                                    LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) o;
                                    TOTAL_GROSS_AMNT += arSalesOrderInvoiceLine.getSilAmount() - arSalesOrderInvoiceLine.getSilTaxAmount();
                                    TOTAL_NET_AMNT += arSalesOrderInvoiceLine.getSilAmount();
                                    TOTAL_TAX_AMNT += arSalesOrderInvoiceLine.getSilTaxAmount();
                                    TOTAL_DISC_AMNT += arSalesOrderInvoiceLine.getSilTotalDiscount();
                                }
                            } else if (!arInvoice.getArJobOrderInvoiceLines().isEmpty()) {
                                for (Object o : arInvoice.getArJobOrderInvoiceLines()) {
                                    LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) o;
                                    TOTAL_GROSS_AMNT += arJobOrderInvoiceLine.getJilAmount() - arJobOrderInvoiceLine.getJilTaxAmount();
                                    TOTAL_NET_AMNT += arJobOrderInvoiceLine.getJilAmount();
                                    TOTAL_TAX_AMNT += arJobOrderInvoiceLine.getJilTaxAmount();
                                    TOTAL_DISC_AMNT += arJobOrderInvoiceLine.getJilTotalDiscount();
                                }
                            } else if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE && arInvoice.getArInvoiceLineItems().isEmpty()) {
                                try {
                                    LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode("TAX", arInvoice.getInvCode(), AD_CMPNY);
                                    TOTAL_TAX_AMNT = arDistributionRecord.getDrAmount();
                                    TOTAL_NET_AMNT = arInvoice.getInvAmountDue() - TOTAL_TAX_AMNT;
                                } catch (FinderException ex) {
                                    TOTAL_TAX_AMNT = 0;
                                    TOTAL_NET_AMNT = arInvoice.getInvAmountDue();
                                }
                            }
                            if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {
                                mdetails.setSrInvCreditMemo(true);
                                mdetails.setPosted(arInvoice.getInvPosted());
                                mdetails.setCmInvoiceNumber(arInvoice.getInvCmInvoiceNumber());
                            } else {
                                mdetails.setSrInvCreditMemo(false);
                                mdetails.setPosted(arInvoice.getInvPosted());
                            }

                            mdetails.setSrServiceAmount(0d);
                            mdetails.setSrGrossAmount(TOTAL_GROSS_AMNT);
                            mdetails.setSrNetAmount(TOTAL_NET_AMNT);
                            mdetails.setSrTaxAmount(TOTAL_TAX_AMNT);
                            mdetails.setSrDiscountAmount(TOTAL_DISC_AMNT);
                            mdetails.setPosted(arInvoice.getInvPosted());

                            if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {
                                if (arInvoice.getArSalesperson() != null) {
                                    if (criteria.containsKey("salesperson")) {
                                        String slsprsn = (String) criteria.get("salesperson");
                                        if (!slsprsn.equals(arInvoice.getArSalesperson().getSlpName())) {
                                            continue;
                                        }
                                    }
                                    mdetails.setSrSlsSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                                    mdetails.setSrSlsName(arInvoice.getArSalesperson().getSlpName());
                                    mdetails.setPosted(arInvoice.getInvPosted());
                                } else {
                                    if (criteria.containsKey("salesperson")) {
                                        continue;
                                    }
                                    mdetails.setSrSlsSalespersonCode("");
                                    mdetails.setSrSlsName("");
                                    mdetails.setPosted(arInvoice.getInvPosted());
                                }
                            } else {
                                try {
                                    LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                                    if (arCreditedInvoice.getArSalesperson() != null) {
                                        if (criteria.containsKey("salesperson")) {
                                            String slsprsn = (String) criteria.get("salesperson");
                                            if (!slsprsn.equals(arCreditedInvoice.getArSalesperson().getSlpName())) {
                                                continue;
                                            }
                                        }
                                        mdetails.setSrSlsSalespersonCode(arCreditedInvoice.getArSalesperson().getSlpSalespersonCode());
                                        mdetails.setSrSlsName(arCreditedInvoice.getArSalesperson().getSlpName());
                                        mdetails.setPosted(arInvoice.getInvPosted());
                                    } else {
                                        if (criteria.containsKey("salesperson")) {
                                            continue;
                                        }
                                        mdetails.setSrSlsSalespersonCode("");
                                        mdetails.setSrSlsName("");
                                        mdetails.setPosted(arInvoice.getInvPosted());
                                    }

                                } catch (FinderException ex) {

                                }
                            }

                            if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {
                                GRAND_TOTAL_GROSS_AMNT -= TOTAL_GROSS_AMNT;
                                GRAND_TOTAL_NET_AMNT -= TOTAL_NET_AMNT;
                                GRAND_TOTAL_TAX_AMNT -= TOTAL_TAX_AMNT;
                                GRAND_TOTAL_AMNT -= (TOTAL_NET_AMNT + TOTAL_TAX_AMNT);

                            } else {

                                GRAND_TOTAL_GROSS_AMNT += TOTAL_GROSS_AMNT;
                                GRAND_TOTAL_NET_AMNT += TOTAL_NET_AMNT;
                                GRAND_TOTAL_TAX_AMNT += TOTAL_TAX_AMNT;
                                GRAND_TOTAL_AMNT += (TOTAL_NET_AMNT + TOTAL_TAX_AMNT);
                            }

                            mdetails.setSrRctType("INVOICE RELEASES");
                            mdetails.setPosted(arInvoice.getInvPosted());
                            // get receipt info

                            StringBuilder invoiceReceiptNumbers = null;
                            StringBuilder invoiceReceiptDates = null;
                            double invoiceReceiptAmount = 0.0d;
                            Collection arInvoiceReceipts = arReceiptHome.findByInvCodeAndRctVoid(arInvoice.getInvCode(), EJBCommon.FALSE, AD_CMPNY);

                            for (Object arInvoiceReceipt : arInvoiceReceipts) {

                                LocalArReceipt arReceipt = (LocalArReceipt) arInvoiceReceipt;

                                if (invoiceReceiptNumbers == null) {
                                    invoiceReceiptNumbers = new StringBuilder(arReceipt.getRctNumber());
                                    invoiceReceiptAmount += arReceipt.getRctAmount();
                                } else if (invoiceReceiptNumbers != null && !invoiceReceiptNumbers.toString().contains(arReceipt.getRctNumber())) {
                                    invoiceReceiptNumbers.append("/").append(arReceipt.getRctNumber());
                                    invoiceReceiptAmount += arReceipt.getRctAmount();
                                }

                                if (invoiceReceiptDates == null) {
                                    invoiceReceiptDates = new StringBuilder(EJBCommon.convertSQLDateToString(arReceipt.getRctDate()));
                                } else if (invoiceReceiptDates != null && !invoiceReceiptDates.toString().contains(EJBCommon.convertSQLDateToString(arReceipt.getRctDate()))) {
                                    invoiceReceiptDates.append("/").append(EJBCommon.convertSQLDateToString(arReceipt.getRctDate()));
                                }
                            }

                            mdetails.setSrInvReceiptNumbers(invoiceReceiptNumbers == null ? "" : invoiceReceiptNumbers.toString());
                            //   mdetails.setSrInvReceiptDates(invoiceReceiptDates.toString());
                            mdetails.setPosted(arInvoice.getInvPosted());
                            mdetails.setSrOrAmount(invoiceReceiptAmount);
                            mdetails.setSrRevenueAmount(REVENUE_AMOUNT);
                            list.add(mdetails);
                        }
                    }
                }
            }

            // Prior Dues

            if (criteria.get("includedPriorDues").equals("YES")) {

                jbossQl = new StringBuilder();
                jbossQl.append("SELECT OBJECT(inv) FROM ArInvoice inv WHERE (");

                if (branchList.isEmpty()) throw new GlobalNoRecordFoundException();

                brIter = branchList.iterator();

                details = (AdBranchDetails) brIter.next();
                jbossQl.append("inv.invAdBranch=").append(details.getBrCode());

                while (brIter.hasNext()) {

                    details = (AdBranchDetails) brIter.next();

                    jbossQl.append(" OR inv.invAdBranch=").append(details.getBrCode());
                }

                jbossQl.append(") ");
                firstArgument = false;

                ctr = 0;
                // Allocate the size of the object parameter
                obj = new Object[criteriaSize];

                if (criteria.containsKey("customerCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("inv.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
                }

                if (criteria.containsKey("customerBatch")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("inv.arCustomer.cstCustomerBatch LIKE '%").append(criteria.get("customerBatch")).append("%' ");
                }

                if (criteria.containsKey("invoiceBatchName")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.arInvoiceBatch.ibName=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("invoiceBatchName");
                    ctr++;
                }

                if (criteria.containsKey("customerType")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("inv.arCustomer.arCustomerType.ctName=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("inv.arCustomer.arCustomerClass.ccName=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("customerClass");
                    ctr++;
                }

                if (criteria.containsKey("dateFrom")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("inv.invDate<?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");
                    ctr++;
                }

                if (criteria.containsKey("dateTo")) {

                    obj[ctr] = criteria.get("dateTo");
                    ctr++;
                }

                if (criteria.containsKey("invoiceNumberFrom")) {

                    obj[ctr] = criteria.get("invoiceNumberFrom");
                    ctr++;
                }

                if (criteria.containsKey("invoiceNumberTo")) {

                    obj[ctr] = criteria.get("invoiceNumberTo");
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
                        jbossQl.append("inv.invPosted = 0 AND inv.invVoid = 0 ");
                    }
                }

                if (criteria.containsKey("includedUnposted") && unpostedOnly == false) {
                    String unPosted = (String) criteria.get("includedUnPosted");

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    if (unPosted == null) unPosted = "NO";
                    if (unPosted.equals("NO")) {

                        jbossQl.append("inv.invPosted = 1 ");

                    } else {

                        jbossQl.append("inv.invVoid = 0 ");
                    }
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                boolean firstSource = true;

                if (criteria.get("includedCreditMemos").equals("YES")) {

                    jbossQl.append("(inv.invCreditMemo = 1 ");
                    firstSource = false;
                }

                if (criteria.get("includedDebitMemos").equals("YES")) {

                    if (!firstSource) {

                        jbossQl.append("OR ");

                    } else {

                        jbossQl.append("(");
                    }

                    jbossQl.append("inv.invDebitMemo = 1 ");
                    firstSource = false;
                }

                if (criteria.get("includedInvoices").equals("YES")) {

                    if (!firstSource) {

                        jbossQl.append("OR ");

                    } else {

                        jbossQl.append("(");
                    }

                    jbossQl.append("(inv.invCreditMemo = 0 AND inv.invDebitMemo = 0)");
                    firstSource = false;
                }

                if (criteria.get("includedMiscReceipts").equals("YES")) {
                    jbossQl.append(" ");

                    if (!firstArgument) {

                        jbossQl.append(" ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                } else {
                    jbossQl.append(") ");

                    if (!firstArgument) {

                        jbossQl.append(" AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                }
                String refNum = "";
                jbossQl.append("inv.invAdCompany=").append(AD_CMPNY).append(" ");

                String orderBy = null;

                if (GROUP_BY.equals("SALESPERSON")) {

                    orderBy = "inv.arCustomer.cstCustomerCode";
                }

                if (orderBy != null) {

                    jbossQl.append("ORDER BY ").append(orderBy);
                }

                arInvoices = arInvoiceHome.getInvByCriteria(jbossQl.toString(), obj, 0, 0);

                if (!arInvoices.isEmpty()) {

                    for (Object invoice : arInvoices) {

                        LocalArInvoice arInvoice = (LocalArInvoice) invoice;

                        double REVENUE_AMOUNT = 0d;
                        Collection arDistributionRecords = arInvoice.getArDistributionRecords();

                        for (Object record : arDistributionRecords) {

                            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) record;

                            if (arDistributionRecord.getGlChartOfAccount().getCoaAccountType().equals("REVENUE")) {

                                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                                    REVENUE_AMOUNT -= arDistributionRecord.getDrAmount();
                                } else {
                                    REVENUE_AMOUNT += arDistributionRecord.getDrAmount();
                                }
                            }
                        }

                        if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {
                            try {
                                LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                            } catch (FinderException ex) {

                                continue;
                            }
                        }

                        if (PRINT_SALES_RELATED_ONLY) {

                            boolean isSalesRelated = false;

                            arDistributionRecords = arInvoice.getArDistributionRecords();

                            for (Object distributionRecord : arDistributionRecords) {

                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                                if (arDistributionRecord.getGlChartOfAccount().getCoaAccountType().equals("REVENUE")) {

                                    isSalesRelated = true;
                                    break;
                                }
                            }

                            if (!isSalesRelated) continue;
                        }

                        if (SHOW_ENTRIES) {

                            boolean first = true;

                            arDistributionRecords = arInvoice.getArDistributionRecords();

                            for (Object distributionRecord : arDistributionRecords) {

                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                                ArRepSalesRegisterDetails mdetails = new ArRepSalesRegisterDetails();
                                mdetails.setSrDate(arInvoice.getInvDate());
                                mdetails.setSrInvoiceNumber(arInvoice.getInvNumber());
                                mdetails.setSrReferenceNumber(arInvoice.getInvReferenceNumber());
                                refNum = arInvoice.getInvReferenceNumber();
                                mdetails.setSrDescription(arInvoice.getInvDescription());
                                mdetails.setSrCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode() + "-" + arInvoice.getArCustomer().getCstName());
                                mdetails.setSrCstCustomerClass(arInvoice.getArCustomer().getArCustomerClass().getCcName());
                                mdetails.setSrCstName(arInvoice.getArCustomer().getCstName());
                                mdetails.setSrCstCustomerCode2(arInvoice.getArCustomer().getCstCustomerCode());
                                mdetails.setPosted(arInvoice.getInvPosted());

                                // type
                                if (arInvoice.getArCustomer().getArCustomerType() == null) {

                                    mdetails.setSrCstCustomerType("UNDEFINE");

                                } else {

                                    mdetails.setSrCstCustomerType(arInvoice.getArCustomer().getArCustomerType().getCtName());
                                }

                                if (first) {

                                    double AMNT_DUE = 0d;

                                    if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                                        AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arInvoice.getInvAmountDue(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                                    } else {

                                        LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                                        AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arInvoice.getInvAmountDue(), AD_CMPNY) * -1, adCompany.getGlFunctionalCurrency().getFcPrecision());
                                    }

                                    mdetails.setSrAmount(AMNT_DUE);
                                    mdetails.setPosted(arInvoice.getInvPosted());
                                    // first = false;

                                }

                                mdetails.setOrderBy(ORDER_BY);
                                mdetails.setPosted(arInvoice.getInvPosted());
                                double TOTAL_GROSS_AMNT = 0d;
                                double TOTAL_NET_AMNT = 0d;
                                double TOTAL_TAX_AMNT = 0d;
                                double TOTAL_DISC_AMNT = 0d;

                                // compute net amount, tax amount and discount amount
                                if (!arInvoice.getArInvoiceLineItems().isEmpty()) {

                                    for (Object o : arInvoice.getArInvoiceLineItems()) {

                                        LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) o;

                                        TOTAL_NET_AMNT += arInvoiceLineItem.getIliAmount();
                                        TOTAL_TAX_AMNT += arInvoiceLineItem.getIliTaxAmount();
                                        TOTAL_GROSS_AMNT += TOTAL_NET_AMNT - TOTAL_TAX_AMNT;
                                        TOTAL_DISC_AMNT += arInvoiceLineItem.getIliTotalDiscount();
                                    }

                                } else if (!arInvoice.getArInvoiceLines().isEmpty()) {

                                    for (Object o : arInvoice.getArInvoiceLines()) {

                                        LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) o;
                                        // arInvoiceLine.getArStandardMemoLine().getSmlName();
                                        mdetails.setSrQuantity(arInvoiceLine.getIlQuantity());
                                        TOTAL_NET_AMNT += arInvoiceLine.getIlAmount();
                                        TOTAL_TAX_AMNT += arInvoiceLine.getIlTaxAmount();
                                        TOTAL_GROSS_AMNT += TOTAL_NET_AMNT - TOTAL_TAX_AMNT;
                                    }

                                } else if (!arInvoice.getArSalesOrderInvoiceLines().isEmpty()) {

                                    for (Object o : arInvoice.getArSalesOrderInvoiceLines()) {

                                        LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) o;
                                        String cst = arInvoice.getArCustomer().getCstCustomerCode();
                                        TOTAL_NET_AMNT += arSalesOrderInvoiceLine.getSilAmount();
                                        TOTAL_TAX_AMNT += arSalesOrderInvoiceLine.getSilTaxAmount();
                                        TOTAL_GROSS_AMNT += TOTAL_NET_AMNT - TOTAL_TAX_AMNT;
                                        TOTAL_DISC_AMNT += arSalesOrderInvoiceLine.getSilTotalDiscount();
                                    }

                                } else if (!arInvoice.getArJobOrderInvoiceLines().isEmpty()) {

                                    for (Object o : arInvoice.getArJobOrderInvoiceLines()) {

                                        LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) o;
                                        String cst = arInvoice.getArCustomer().getCstCustomerCode();
                                        TOTAL_NET_AMNT += arJobOrderInvoiceLine.getJilAmount();
                                        TOTAL_TAX_AMNT += arJobOrderInvoiceLine.getJilTaxAmount();
                                        TOTAL_GROSS_AMNT += TOTAL_NET_AMNT - TOTAL_TAX_AMNT;
                                        TOTAL_DISC_AMNT += arJobOrderInvoiceLine.getJilTotalDiscount();
                                    }

                                } else if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE && arInvoice.getArInvoiceLineItems().isEmpty()) {

                                    try {

                                        arDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode("TAX", arInvoice.getInvCode(), AD_CMPNY);

                                        TOTAL_TAX_AMNT = arDistributionRecord.getDrAmount();
                                        TOTAL_NET_AMNT = arInvoice.getInvAmountDue() - TOTAL_TAX_AMNT;

                                    } catch (FinderException ex) {

                                        TOTAL_TAX_AMNT = 0;
                                        TOTAL_NET_AMNT = arInvoice.getInvAmountDue();
                                    }
                                }

                                if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {

                                    if (TOTAL_NET_AMNT != 0) {

                                        TOTAL_NET_AMNT = TOTAL_NET_AMNT * -1;
                                    }

                                    if (TOTAL_TAX_AMNT != 0) {

                                        TOTAL_TAX_AMNT = TOTAL_TAX_AMNT * -1;
                                    }
                                }

                                mdetails.setSrGrossAmount(TOTAL_GROSS_AMNT);
                                mdetails.setSrNetAmount(TOTAL_NET_AMNT);
                                mdetails.setSrTaxAmount(TOTAL_TAX_AMNT);
                                mdetails.setPosted(arInvoice.getInvPosted());

                                if (first) {

                                    mdetails.setSrDiscountAmount(TOTAL_DISC_AMNT);
                                    first = false;
                                }

                                if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                                    if (arInvoice.getArSalesperson() != null) {

                                        if (criteria.containsKey("salesperson")) {

                                            String slsprsn = (String) criteria.get("salesperson");

                                            if (!slsprsn.equals(arInvoice.getArSalesperson().getSlpName())) {
                                                continue;
                                            }
                                        }

                                        mdetails.setSrSlsSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                                        mdetails.setSrSlsName(arInvoice.getArSalesperson().getSlpName());
                                        mdetails.setPosted(arInvoice.getInvPosted());

                                    } else {

                                        if (criteria.containsKey("salesperson")) {
                                            continue;
                                        }

                                        mdetails.setSrSlsSalespersonCode("");
                                        mdetails.setSrSlsName("");
                                        mdetails.setPosted(arInvoice.getInvPosted());
                                    }

                                } else {

                                    try {

                                        LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                                        if (arCreditedInvoice.getArSalesperson() != null) {

                                            if (criteria.containsKey("salesperson")) {

                                                String slsprsn = (String) criteria.get("salesperson");

                                                if (!slsprsn.equals(arCreditedInvoice.getArSalesperson().getSlpName())) {
                                                    continue;
                                                }
                                            }

                                            mdetails.setSrSlsSalespersonCode(arCreditedInvoice.getArSalesperson().getSlpSalespersonCode());
                                            mdetails.setSrSlsName(arCreditedInvoice.getArSalesperson().getSlpName());
                                            mdetails.setPosted(arInvoice.getInvPosted());

                                        } else {

                                            if (criteria.containsKey("salesperson")) {
                                                continue;
                                            }
                                            mdetails.setSrSlsSalespersonCode("");
                                            mdetails.setSrSlsName("");
                                            mdetails.setPosted(arInvoice.getInvPosted());
                                        }

                                    } catch (FinderException ex) {

                                    }
                                }

                                // set distribution record details
                                mdetails.setSrDrGlAccountNumber(arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                                mdetails.setSrDrGlAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                                    mdetails.setSrDrDebitAmount(arDistributionRecord.getDrAmount());
                                    mdetails.setPosted(arInvoice.getInvPosted());

                                } else {

                                    mdetails.setSrDrCreditAmount(arDistributionRecord.getDrAmount());
                                    mdetails.setPosted(arInvoice.getInvPosted());
                                }

                                mdetails.setSrRevenueAmount(REVENUE_AMOUNT);
                                list.add(mdetails);
                            }

                        } else {

                            ArRepSalesRegisterDetails mdetails = new ArRepSalesRegisterDetails();
                            mdetails.setSrDate(arInvoice.getInvDate());
                            mdetails.setSrInvoiceNumber(arInvoice.getInvNumber());
                            mdetails.setSrReferenceNumber(arInvoice.getInvReferenceNumber());
                            refNum = arInvoice.getInvReferenceNumber();
                            mdetails.setSrDescription(arInvoice.getInvDescription());
                            mdetails.setSrCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode() + "-" + arInvoice.getArCustomer().getCstName());
                            mdetails.setSrCstCustomerClass(arInvoice.getArCustomer().getArCustomerClass().getCcName());
                            mdetails.setSrCstName(arInvoice.getArCustomer().getCstName());
                            mdetails.setSrCstCustomerCode2(arInvoice.getArCustomer().getCstCustomerCode());
                            mdetails.setPosted(arInvoice.getInvPosted());
                            arDistributionRecords = arInvoice.getArDistributionRecords();

                            for (Object distributionRecord : arDistributionRecords) {

                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                                if (AD_CMPNY == 15) {
                                    if (arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Sales") || arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Service")) {
                                        mdetails.setSrDrGlAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                                    }
                                } else {
                                    if ((arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Sales") && (arDistributionRecord.getGlChartOfAccount().getCoaCode() != 5971 && arDistributionRecord.getGlChartOfAccount().getCoaCode() != 6259)) || arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Service")) {
                                        mdetails.setSrDrGlAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                                    }
                                }
                            }
                            // type
                            if (arInvoice.getArCustomer().getArCustomerType() == null) {

                                mdetails.setSrCstCustomerType("UNDEFINE");
                                mdetails.setPosted(arInvoice.getInvPosted());

                            } else {

                                mdetails.setSrCstCustomerType(arInvoice.getArCustomer().getArCustomerType().getCtName());
                                mdetails.setPosted(arInvoice.getInvPosted());
                            }

                            double AMNT_DUE = 0d;

                            if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                                AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arInvoice.getInvAmountDue(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                            } else {

                                LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                                AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arCreditedInvoice.getGlFunctionalCurrency().getFcCode(), arCreditedInvoice.getGlFunctionalCurrency().getFcName(), arCreditedInvoice.getInvConversionDate(), arCreditedInvoice.getInvConversionRate(), arInvoice.getInvAmountDue(), AD_CMPNY) * -1, adCompany.getGlFunctionalCurrency().getFcPrecision());
                            }

                            mdetails.setSrAmount(AMNT_DUE);
                            mdetails.setOrderBy(ORDER_BY);
                            mdetails.setPosted(arInvoice.getInvPosted());

                            double TOTAL_GROSS_AMNT = 0d;
                            double TOTAL_NET_AMNT = 0d;
                            double TOTAL_TAX_AMNT = 0d;
                            double TOTAL_DISC_AMNT = 0d;

                            // compute net amount, tax amount and discount amount
                            if (!arInvoice.getArInvoiceLineItems().isEmpty()) {

                                for (Object o : arInvoice.getArInvoiceLineItems()) {

                                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) o;

                                    TOTAL_NET_AMNT += arInvoiceLineItem.getIliAmount();
                                    TOTAL_TAX_AMNT += arInvoiceLineItem.getIliTaxAmount();
                                    TOTAL_GROSS_AMNT += TOTAL_NET_AMNT - TOTAL_TAX_AMNT;
                                    TOTAL_DISC_AMNT += arInvoiceLineItem.getIliTotalDiscount();
                                }

                            } else if (!arInvoice.getArInvoiceLines().isEmpty()) {

                                for (Object o : arInvoice.getArInvoiceLines()) {

                                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) o;
                                    mdetails.setSrQuantity(arInvoiceLine.getIlQuantity());
                                    TOTAL_NET_AMNT += arInvoiceLine.getIlAmount();
                                    TOTAL_TAX_AMNT += arInvoiceLine.getIlTaxAmount();
                                    TOTAL_GROSS_AMNT += TOTAL_NET_AMNT - TOTAL_TAX_AMNT;
                                }

                            } else if (!arInvoice.getArSalesOrderInvoiceLines().isEmpty()) {

                                for (Object o : arInvoice.getArSalesOrderInvoiceLines()) {

                                    LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine = (LocalArSalesOrderInvoiceLine) o;

                                    TOTAL_NET_AMNT += arSalesOrderInvoiceLine.getSilAmount();
                                    TOTAL_TAX_AMNT += arSalesOrderInvoiceLine.getSilTaxAmount();
                                    TOTAL_GROSS_AMNT += TOTAL_NET_AMNT - TOTAL_TAX_AMNT;
                                    TOTAL_DISC_AMNT += arSalesOrderInvoiceLine.getSilTotalDiscount();
                                }

                            } else if (!arInvoice.getArJobOrderInvoiceLines().isEmpty()) {

                                for (Object o : arInvoice.getArJobOrderInvoiceLines()) {

                                    LocalArJobOrderInvoiceLine arJobOrderInvoiceLine = (LocalArJobOrderInvoiceLine) o;

                                    TOTAL_NET_AMNT += arJobOrderInvoiceLine.getJilAmount();
                                    TOTAL_TAX_AMNT += arJobOrderInvoiceLine.getJilTaxAmount();
                                    TOTAL_GROSS_AMNT += TOTAL_NET_AMNT - TOTAL_TAX_AMNT;
                                    TOTAL_DISC_AMNT += arJobOrderInvoiceLine.getJilTotalDiscount();
                                }

                            } else if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE && arInvoice.getArInvoiceLineItems().isEmpty()) {

                                try {

                                    LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.findByDrClassAndInvCode("TAX", arInvoice.getInvCode(), AD_CMPNY);

                                    TOTAL_TAX_AMNT = arDistributionRecord.getDrAmount();
                                    TOTAL_NET_AMNT = arInvoice.getInvAmountDue() - TOTAL_TAX_AMNT;

                                } catch (FinderException ex) {

                                    TOTAL_TAX_AMNT = 0;
                                    TOTAL_NET_AMNT = arInvoice.getInvAmountDue();
                                }
                            }

                            if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {

                                mdetails.setSrInvCreditMemo(true);
                                mdetails.setPosted(arInvoice.getInvPosted());

                            } else {

                                mdetails.setSrInvCreditMemo(false);
                                mdetails.setPosted(arInvoice.getInvPosted());
                            }

                            mdetails.setSrGrossAmount(TOTAL_GROSS_AMNT);
                            mdetails.setSrNetAmount(TOTAL_NET_AMNT);
                            mdetails.setSrTaxAmount(TOTAL_TAX_AMNT);
                            mdetails.setSrDiscountAmount(TOTAL_DISC_AMNT);
                            mdetails.setPosted(arInvoice.getInvPosted());

                            if (arInvoice.getInvCreditMemo() == EJBCommon.FALSE) {

                                if (arInvoice.getArSalesperson() != null) {

                                    if (criteria.containsKey("salesperson")) {

                                        String slsprsn = (String) criteria.get("salesperson");

                                        if (!slsprsn.equals(arInvoice.getArSalesperson().getSlpName())) {
                                            continue;
                                        }
                                    }

                                    mdetails.setSrSlsSalespersonCode(arInvoice.getArSalesperson().getSlpSalespersonCode());
                                    mdetails.setSrSlsName(arInvoice.getArSalesperson().getSlpName());
                                    mdetails.setPosted(arInvoice.getInvPosted());

                                } else {

                                    if (criteria.containsKey("salesperson")) {
                                        continue;
                                    }

                                    mdetails.setSrSlsSalespersonCode("");
                                    mdetails.setSrSlsName("");
                                    mdetails.setPosted(arInvoice.getInvPosted());
                                }

                            } else {

                                try {

                                    LocalArInvoice arCreditedInvoice = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                                    if (arCreditedInvoice.getArSalesperson() != null) {

                                        if (criteria.containsKey("salesperson")) {

                                            String slsprsn = (String) criteria.get("salesperson");

                                            if (!slsprsn.equals(arCreditedInvoice.getArSalesperson().getSlpName())) {
                                                continue;
                                            }
                                        }

                                        mdetails.setSrSlsSalespersonCode(arCreditedInvoice.getArSalesperson().getSlpSalespersonCode());
                                        mdetails.setSrSlsName(arCreditedInvoice.getArSalesperson().getSlpName());
                                        mdetails.setPosted(arCreditedInvoice.getInvPosted());

                                    } else {

                                        if (criteria.containsKey("salesperson")) {
                                            continue;
                                        }
                                        mdetails.setSrSlsSalespersonCode("");
                                        mdetails.setSrSlsName("");
                                        mdetails.setPosted(arCreditedInvoice.getInvPosted());
                                    }

                                } catch (FinderException ex) {

                                }
                            }

                            if (arInvoice.getInvCreditMemo() == EJBCommon.TRUE) {
                                mdetails.setCmInvoiceNumber(arInvoice.getInvCmInvoiceNumber());
                                GRAND_TOTAL_NET_AMNT -= TOTAL_NET_AMNT;
                                GRAND_TOTAL_TAX_AMNT -= TOTAL_TAX_AMNT;
                                GRAND_TOTAL_AMNT -= (TOTAL_NET_AMNT + TOTAL_TAX_AMNT);
                                GRAND_TOTAL_GROSS_AMNT -= TOTAL_GROSS_AMNT;

                            } else {

                                GRAND_TOTAL_NET_AMNT += TOTAL_NET_AMNT;
                                GRAND_TOTAL_TAX_AMNT += TOTAL_TAX_AMNT;
                                GRAND_TOTAL_AMNT += (TOTAL_NET_AMNT + TOTAL_TAX_AMNT);
                                GRAND_TOTAL_GROSS_AMNT += TOTAL_GROSS_AMNT;
                            }

                            mdetails.setSrRctType("INVOICE");

                            // get receipt info

                            StringBuilder invoiceReceiptNumbers = null;
                            StringBuilder invoiceReceiptDates = null;

                            Collection arInvoiceReceipts = arReceiptHome.findByInvCodeAndRctVoid(arInvoice.getInvCode(), EJBCommon.FALSE, AD_CMPNY);

                            for (Object arInvoiceReceipt : arInvoiceReceipts) {

                                LocalArReceipt arReceipt = (LocalArReceipt) arInvoiceReceipt;

                                if (invoiceReceiptNumbers == null) {
                                    invoiceReceiptNumbers = new StringBuilder(arReceipt.getRctNumber());
                                } else if (invoiceReceiptNumbers != null && !invoiceReceiptNumbers.toString().contains(arReceipt.getRctNumber())) {
                                    invoiceReceiptNumbers.append("/").append(arReceipt.getRctNumber());
                                }

                                if (invoiceReceiptDates == null) {
                                    invoiceReceiptDates = new StringBuilder(EJBCommon.convertSQLDateToString(arReceipt.getRctDate()));
                                } else if (invoiceReceiptDates != null && !invoiceReceiptDates.toString().contains(EJBCommon.convertSQLDateToString(arReceipt.getRctDate()))) {
                                    invoiceReceiptDates.append("/").append(EJBCommon.convertSQLDateToString(arReceipt.getRctDate()));
                                }
                            }

                            mdetails.setSrInvReceiptNumbers(invoiceReceiptNumbers.toString());
                            mdetails.setSrInvReceiptDates(invoiceReceiptDates.toString());
                            mdetails.setSrRevenueAmount(REVENUE_AMOUNT);
                            list = getPriorDues(arInvoice, mdetails, criteria, list);
                        }
                    }
                }
            }

            Collection arReceipts = null;
            String miscReceipts = null;

            if (criteria.get("includedMiscReceipts").equals("YES")) {

                // misc receipt

                jbossQl = new StringBuilder();
                jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct WHERE (");

                brIter = branchList.iterator();

                details = (AdBranchDetails) brIter.next();
                jbossQl.append("rct.rctAdBranch=").append(details.getBrCode());

                while (brIter.hasNext()) {

                    details = (AdBranchDetails) brIter.next();

                    jbossQl.append(" OR rct.rctAdBranch=").append(details.getBrCode());
                }

                jbossQl.append(") ");
                firstArgument = false;

                ctr = 0;

                if (criteria.containsKey("invoiceNumberFrom")) {

                    criteriaSize--;
                }

                if (criteria.containsKey("invoiceNumberTo")) {

                    criteriaSize--;
                }

                if (criteria.containsKey("invoiceBatchName")) obj = new Object[criteriaSize - 1];
                else obj = new Object[criteriaSize];

                if (criteria.containsKey("customerCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("rct.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
                }

                if (criteria.containsKey("customerBatch")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("rct.arCustomer.cstCustomerBatch LIKE '%").append(criteria.get("customerBatch")).append("%' ");
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
                        jbossQl.append("rct.rctPosted = 0 AND rct.rctVoid = 0 ");
                    }
                }

                if (criteria.containsKey("includedUnposted") && unpostedOnly == false) {

                    String unposted = (String) criteria.get("includedUnposted");

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    if (unposted == null) unposted = "NO";
                    if (unposted.equals("NO")) {

                        jbossQl.append("rct.rctPosted = 1 AND rct.rctVoid = 0 ");

                    } else {

                        jbossQl.append("rct.rctVoid = 0 ");
                    }
                }

                if (criteria.containsKey("region")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("rct.arCustomer.cstAdLvRegion=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("region");
                    ctr++;
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctType='MISC' AND rct.rctAdCompany=").append(AD_CMPNY).append(" ");

                String orderBy = null;

                if (GROUP_BY.equals("SALESPERSON") || GROUP_BY.equals("PRODUCT")) {

                    orderBy = "rct.arCustomer.cstCustomerCode";
                }

                if (orderBy != null) {

                    jbossQl.append("ORDER BY ").append(orderBy);
                }

                Debug.print("jbossQl.toString() 5 : " + jbossQl.toString());
                arReceipts = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, 0, 0);

                if (!arReceipts.isEmpty()) {

                    for (Object receipt : arReceipts) {

                        LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                        double REVENU_AMOUNT = 0d;

                        Collection arDistributionRecords = arReceipt.getArDistributionRecords();

                        for (Object record : arDistributionRecords) {

                            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) record;

                            if (arDistributionRecord.getGlChartOfAccount().getCoaAccountType().equals("REVENUE")) {

                                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                                    REVENU_AMOUNT -= arDistributionRecord.getDrAmount();
                                } else {
                                    REVENU_AMOUNT += arDistributionRecord.getDrAmount();
                                }
                            }
                        }

                        if (PRINT_SALES_RELATED_ONLY) {

                            boolean isSalesRelated = false;

                            arDistributionRecords = arReceipt.getArDistributionRecords();

                            for (Object distributionRecord : arDistributionRecords) {

                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                                if (arDistributionRecord.getGlChartOfAccount().getCoaAccountType().equals("REVENUE")) {

                                    isSalesRelated = true;
                                    break;
                                }
                            }

                            if (!isSalesRelated) continue;
                        }

                        if (SHOW_ENTRIES) {

                            boolean first = true;

                            arDistributionRecords = arReceipt.getArDistributionRecords();

                            for (Object distributionRecord : arDistributionRecords) {

                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                                ArRepSalesRegisterDetails mdetails = new ArRepSalesRegisterDetails();
                                mdetails.setSrDate(arReceipt.getRctDate());
                                mdetails.setSrInvoiceNumber(arReceipt.getRctNumber());
                                mdetails.setSrReferenceNumber(arReceipt.getRctReferenceNumber());
                                mdetails.setSrDescription(arReceipt.getRctDescription());
                                mdetails.setSrCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode() + "-" + (arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName()));
                                mdetails.setSrCstCustomerClass(arReceipt.getArCustomer().getArCustomerClass().getCcName());
                                mdetails.setSrCstName(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                                mdetails.setSrCstCustomerCode2(arReceipt.getArCustomer().getCstCustomerCode());
                                mdetails.setPosted(arReceipt.getRctPosted());
                                // type
                                if (arReceipt.getArCustomer().getArCustomerType() == null) {

                                    mdetails.setSrCstCustomerType("UNDEFINE");
                                    mdetails.setPosted(arReceipt.getRctPosted());

                                } else {

                                    mdetails.setSrCstCustomerType(arReceipt.getArCustomer().getArCustomerType().getCtName());
                                    mdetails.setPosted(arReceipt.getRctPosted());
                                }

                                if (first) {

                                    double AMNT_DUE = 0d;

                                    AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                                    mdetails.setSrAmount(AMNT_DUE);

                                    first = false;
                                }

                                mdetails.setOrderBy(ORDER_BY);
                                mdetails.setPosted(arReceipt.getRctPosted());
                                // set distribution record details
                                mdetails.setSrDrGlAccountNumber(arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                                mdetails.setSrDrGlAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                                    mdetails.setSrDrDebitAmount(arDistributionRecord.getDrAmount());
                                    mdetails.setPosted(arReceipt.getRctPosted());
                                } else {

                                    mdetails.setSrDrCreditAmount(arDistributionRecord.getDrAmount());
                                    mdetails.setPosted(arReceipt.getRctPosted());
                                }

                                mdetails.setSrRevenueAmount(REVENU_AMOUNT);
                                list.add(mdetails);
                            }

                        } else {

                            ArRepSalesRegisterDetails mdetails = new ArRepSalesRegisterDetails();
                            mdetails.setSrDate(arReceipt.getRctDate());
                            mdetails.setSrInvoiceNumber(arReceipt.getRctNumber());
                            mdetails.setSrReferenceNumber(arReceipt.getRctReferenceNumber());
                            mdetails.setSrDescription(arReceipt.getRctDescription());
                            mdetails.setSrCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode() + "-" + (arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName()));
                            mdetails.setSrCstCustomerClass(arReceipt.getArCustomer().getArCustomerClass().getCcName());
                            mdetails.setSrCstName(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                            mdetails.setSrCstCustomerCode2(arReceipt.getArCustomer().getCstCustomerCode());
                            mdetails.setPosted(arReceipt.getRctPosted());

                            arDistributionRecords = arReceipt.getArDistributionRecords();

                            for (Object distributionRecord : arDistributionRecords) {

                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                                if (AD_CMPNY == 15) {
                                    if (arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Sales") || arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Service")) {
                                        mdetails.setSrDrGlAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                                    }
                                } else {
                                    if ((arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Sales") && (arDistributionRecord.getGlChartOfAccount().getCoaCode() != 5971 && arDistributionRecord.getGlChartOfAccount().getCoaCode() != 6259)) || arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Service")) {
                                        mdetails.setSrDrGlAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                                    }
                                }
                            }
                            // type
                            if (arReceipt.getArCustomer().getArCustomerType() == null) {

                                mdetails.setSrCstCustomerType("UNDEFINE");
                                mdetails.setPosted(arReceipt.getRctPosted());
                            } else {

                                mdetails.setSrCstCustomerType(arReceipt.getArCustomer().getArCustomerType().getCtName());
                                mdetails.setPosted(arReceipt.getRctPosted());
                            }

                            boolean isCustDeposit = (arReceipt.getRctCustomerDeposit() == 1);

                            double AMNT_DUE = 0d;

                            AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                            // added to remove negative if negative

                            if ((AMNT_DUE * -1) < 0) {
                                mdetails.setSrAmount(AMNT_DUE);
                            } else {
                                mdetails.setSrAmount(AMNT_DUE * -1);
                            }

                            mdetails.setOrderBy(ORDER_BY);
                            mdetails.setPosted(arReceipt.getRctPosted());
                            double TOTAL_NET_AMNT = 0d;
                            double TOTAL_TAX_AMNT = 0d;
                            double TOTAL_GROSS_AMNT = 0d;
                            String smlName = null;

                            if (!arReceipt.getArInvoiceLineItems().isEmpty()) {

                                for (Object o : arReceipt.getArInvoiceLineItems()) {

                                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) o;

                                    TOTAL_NET_AMNT += arInvoiceLineItem.getIliAmount();
                                    TOTAL_TAX_AMNT += arInvoiceLineItem.getIliTaxAmount();
                                    TOTAL_GROSS_AMNT += TOTAL_NET_AMNT - TOTAL_TAX_AMNT;
                                }

                            } else if (!arReceipt.getArInvoiceLines().isEmpty()) {

                                for (Object o : arReceipt.getArInvoiceLines()) {

                                    LocalArInvoiceLine arInvoiceLine = (LocalArInvoiceLine) o;
                                    mdetails.setSrQuantity(arInvoiceLine.getIlQuantity());
                                    TOTAL_NET_AMNT += arInvoiceLine.getIlAmount();
                                    TOTAL_TAX_AMNT += arInvoiceLine.getIlTaxAmount();
                                    TOTAL_GROSS_AMNT += TOTAL_NET_AMNT - TOTAL_TAX_AMNT;
                                    smlName = arInvoiceLine.getArStandardMemoLine().getSmlName();
                                }
                            }

                            mdetails.setSrMemoName(smlName);
                            mdetails.setSrGrossAmount(TOTAL_GROSS_AMNT);
                            mdetails.setSrNetAmount(isCustDeposit ? (TOTAL_NET_AMNT * -1) : TOTAL_NET_AMNT);
                            mdetails.setSrTaxAmount(isCustDeposit ? (TOTAL_TAX_AMNT * -1) : TOTAL_TAX_AMNT);
                            mdetails.setPosted(arReceipt.getRctPosted());
                            if (arReceipt.getArSalesperson() != null) {

                                if (criteria.containsKey("salesperson")) {

                                    String slsprsn = (String) criteria.get("salesperson");

                                    if (!slsprsn.equals(arReceipt.getArSalesperson().getSlpName())) {
                                        continue;
                                    }
                                }

                                mdetails.setSrSlsSalespersonCode(arReceipt.getArSalesperson().getSlpSalespersonCode());
                                mdetails.setSrSlsName(arReceipt.getArSalesperson().getSlpName());
                                mdetails.setPosted(arReceipt.getRctPosted());
                            } else {

                                if (criteria.containsKey("salesperson")) {
                                    continue;
                                }
                                mdetails.setSrSlsSalespersonCode("");
                                mdetails.setSrSlsName("");
                                mdetails.setPosted(arReceipt.getRctPosted());
                            }

                            if (isCustDeposit) {
                                GRAND_TOTAL_NET_AMNT -= TOTAL_NET_AMNT;
                                GRAND_TOTAL_TAX_AMNT -= TOTAL_TAX_AMNT;
                                GRAND_TOTAL_AMNT -= (TOTAL_NET_AMNT + TOTAL_TAX_AMNT);
                                GRAND_TOTAL_GROSS_AMNT -= TOTAL_GROSS_AMNT;
                            } else {
                                GRAND_TOTAL_NET_AMNT += TOTAL_NET_AMNT;
                                GRAND_TOTAL_TAX_AMNT += TOTAL_TAX_AMNT;
                                GRAND_TOTAL_AMNT += (TOTAL_NET_AMNT + TOTAL_TAX_AMNT);
                                GRAND_TOTAL_GROSS_AMNT += TOTAL_GROSS_AMNT;
                            }

                            mdetails.setSrRctType("MISC");
                            mdetails.setPosted(arReceipt.getRctPosted());
                            mdetails.setSrRevenueAmount(REVENU_AMOUNT);
                            list.add(mdetails);
                        }
                    }
                }
            }

            if (criteria.get("includedCollections").equals("YES")) {

                // collections

                jbossQl = new StringBuilder();
                jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct WHERE (");

                brIter = branchList.iterator();

                details = (AdBranchDetails) brIter.next();
                jbossQl.append("rct.rctAdBranch=").append(details.getBrCode());

                while (brIter.hasNext()) {

                    details = (AdBranchDetails) brIter.next();

                    jbossQl.append(" OR rct.rctAdBranch=").append(details.getBrCode());
                }

                jbossQl.append(") ");
                firstArgument = false;

                ctr = 0;

                if (criteria.containsKey("invoiceNumberFrom")) {

                    criteriaSize--;
                }

                if (criteria.containsKey("invoiceNumberTo")) {

                    criteriaSize--;
                }

                if (criteria.containsKey("invoiceBatchName")) obj = new Object[criteriaSize - 1];
                else obj = new Object[criteriaSize];

                if (criteria.containsKey("customerCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("rct.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
                }

                if (criteria.containsKey("customerBatch")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("rct.arCustomer.cstCustomerBatch LIKE '%").append(criteria.get("customerBatch")).append("%' ");
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
                        jbossQl.append("rct.rctPosted = 0 AND rct.rctVoid = 0 ");
                    }
                }

                if (criteria.containsKey("includedUnposted") && unpostedOnly == false) {

                    String unposted = (String) criteria.get("includedUnposted");

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    if (unposted == null) unposted = "NO";
                    if (unposted.equals("NO")) {

                        jbossQl.append("rct.rctPosted = 1 AND rct.rctVoid = 0 ");

                    } else {

                        jbossQl.append("rct.rctVoid = 0 ");
                    }
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctType='COLLECTION' AND rct.rctAdCompany=").append(AD_CMPNY).append(" ");

                String orderBy = null;

                if (GROUP_BY.equals("SALESPERSON") || GROUP_BY.equals("PRODUCT")) {

                    orderBy = "rct.arCustomer.cstCustomerCode";
                }

                if (orderBy != null) {

                    jbossQl.append("ORDER BY ").append(orderBy);
                }

                Debug.print("jbossQl.toString() 6 : " + jbossQl.toString());
                arReceipts = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, 0, 0);

                if (!arReceipts.isEmpty()) {

                    for (Object receipt : arReceipts) {

                        LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                        double REVENUE_AMOUNT = 0d;

                        Collection arDistributionRecords = arReceipt.getArDistributionRecords();

                        for (Object record : arDistributionRecords) {

                            LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) record;

                            if (arDistributionRecord.getGlChartOfAccount().getCoaAccountType().equals("REVENUE")) {

                                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                                    REVENUE_AMOUNT -= arDistributionRecord.getDrAmount();
                                } else {
                                    REVENUE_AMOUNT += arDistributionRecord.getDrAmount();
                                }
                            }
                        }

                        if (PRINT_SALES_RELATED_ONLY) {

                            boolean isSalesRelated = false;

                            arDistributionRecords = arReceipt.getArDistributionRecords();

                            for (Object distributionRecord : arDistributionRecords) {

                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                                if (arDistributionRecord.getGlChartOfAccount().getCoaAccountType().equals("REVENUE")) {

                                    isSalesRelated = true;
                                    break;
                                }
                            }

                            if (!isSalesRelated) continue;
                        }

                        if (SHOW_ENTRIES) {

                            boolean first = true;

                            arDistributionRecords = arReceipt.getArDistributionRecords();

                            for (Object distributionRecord : arDistributionRecords) {

                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                                ArRepSalesRegisterDetails mdetails = new ArRepSalesRegisterDetails();
                                mdetails.setSrDate(arReceipt.getRctDate());
                                mdetails.setSrInvoiceNumber(arReceipt.getRctNumber());
                                mdetails.setSrReferenceNumber(arReceipt.getRctReferenceNumber());
                                mdetails.setSrDescription(arReceipt.getRctDescription());
                                mdetails.setSrCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode() + "-" + (arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName()));
                                mdetails.setSrCstCustomerClass(arReceipt.getArCustomer().getArCustomerClass().getCcName());
                                mdetails.setSrCstName(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                                mdetails.setSrCstCustomerCode2(arReceipt.getArCustomer().getCstCustomerCode());
                                mdetails.setPosted(arReceipt.getRctPosted());
                                // type
                                if (arReceipt.getArCustomer().getArCustomerType() == null) {

                                    mdetails.setSrCstCustomerType("UNDEFINE");
                                    mdetails.setPosted(arReceipt.getRctPosted());

                                } else {

                                    mdetails.setSrCstCustomerType(arReceipt.getArCustomer().getArCustomerType().getCtName());
                                    mdetails.setPosted(arReceipt.getRctPosted());
                                }

                                double AMNT_DUE = 0d;

                                AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                                mdetails.setSrAmount(AMNT_DUE);

                                mdetails.setOrderBy(ORDER_BY);

                                mdetails.setPosted(arReceipt.getRctPosted());
                                // set distribution record details
                                mdetails.setSrDrGlAccountNumber(arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                                mdetails.setSrDrGlAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                                    mdetails.setSrDrDebitAmount(arDistributionRecord.getDrAmount());
                                    mdetails.setPosted(arReceipt.getRctPosted());

                                } else {

                                    mdetails.setSrDrCreditAmount(arDistributionRecord.getDrAmount());
                                    mdetails.setPosted(arReceipt.getRctPosted());
                                }
                            }

                        } else {

                            ArRepSalesRegisterDetails mdetails = new ArRepSalesRegisterDetails();
                            mdetails.setSrDate(arReceipt.getRctDate());
                            mdetails.setSrInvoiceNumber(arReceipt.getRctReferenceNumber());
                            mdetails.setSrOrNumber(arReceipt == null ? "" : arReceipt.getRctNumber());

                            mdetails.setSrReferenceNumber(arReceipt.getRctReferenceNumber());
                            mdetails.setSrDescription(arReceipt.getRctDescription());
                            mdetails.setSrCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode() + "-" + (arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName()));
                            mdetails.setSrCstCustomerClass(arReceipt.getArCustomer().getArCustomerClass().getCcName());
                            mdetails.setSrCstName(arReceipt.getArCustomer().getCstName());
                            mdetails.setSrCstCustomerCode2(arReceipt.getArCustomer().getCstCustomerCode());
                            mdetails.setPosted(arReceipt.getRctPosted());

                            arDistributionRecords = arReceipt.getArDistributionRecords();

                            for (Object distributionRecord : arDistributionRecords) {

                                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                                if (AD_CMPNY == 15) {
                                    if (arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Sales") || arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Service")) {
                                        mdetails.setSrDrGlAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                                    }
                                } else {
                                    if ((arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Sales") && (arDistributionRecord.getGlChartOfAccount().getCoaCode() != 5971 && arDistributionRecord.getGlChartOfAccount().getCoaCode() != 6259)) || arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription().contains("Service")) {
                                        mdetails.setSrDrGlAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());
                                    }
                                }
                            }

                            // type
                            if (arReceipt.getArCustomer().getArCustomerType() == null) {

                                mdetails.setSrCstCustomerType("UNDEFINE");

                            } else {

                                mdetails.setSrCstCustomerType(arReceipt.getArCustomer().getArCustomerType().getCtName());
                                mdetails.setPosted(arReceipt.getRctPosted());
                            }

                            double AMNT_DUE = 0d;

                            AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                            mdetails.setSrAmount(AMNT_DUE * -1);

                            mdetails.setSrOrAmount(AMNT_DUE * -1);
                            mdetails.setOrderBy(ORDER_BY);
                            mdetails.setPosted(arReceipt.getRctPosted());

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
                                    mdetails.setSrQuantity(arInvoiceLine.getIlQuantity());
                                    TOTAL_NET_AMNT += arInvoiceLine.getIlAmount();
                                    TOTAL_TAX_AMNT += arInvoiceLine.getIlTaxAmount();
                                }
                            }

                            //  mdetails.setSrInvoiceNumber("");

                            mdetails.setSrNetAmount(TOTAL_NET_AMNT * -1);
                            mdetails.setSrTaxAmount(TOTAL_TAX_AMNT * -1);

                            mdetails.setSrGrossAmount((TOTAL_NET_AMNT * -1) - (TOTAL_TAX_AMNT * -1));
                            mdetails.setPosted(arReceipt.getRctPosted());

                            if (arReceipt.getArSalesperson() != null) {

                                if (criteria.containsKey("salesperson")) {

                                    String slsprsn = (String) criteria.get("salesperson");

                                    if (!slsprsn.equals(arReceipt.getArSalesperson().getSlpName())) {
                                        continue;
                                    }
                                }

                                mdetails.setSrSlsSalespersonCode(arReceipt.getArSalesperson().getSlpSalespersonCode());
                                mdetails.setSrSlsName(arReceipt.getArSalesperson().getSlpName());
                                mdetails.setPosted(arReceipt.getRctPosted());
                            } else {

                                if (criteria.containsKey("salesperson")) {
                                    continue;
                                }
                                mdetails.setSrSlsSalespersonCode("");
                                mdetails.setSrSlsName("");
                                mdetails.setPosted(arReceipt.getRctPosted());
                            }

                            GRAND_TOTAL_NET_AMNT -= TOTAL_NET_AMNT;
                            GRAND_TOTAL_TAX_AMNT -= TOTAL_TAX_AMNT;
                            GRAND_TOTAL_AMNT -= (TOTAL_NET_AMNT + TOTAL_TAX_AMNT);
                            GRAND_TOTAL_GROSS_AMNT -= TOTAL_NET_AMNT - TOTAL_TAX_AMNT;

                            mdetails.setSrRctType("COLLECTION");

                            double SR_AI_APPLY_AMNT = 0;
                            double SR_AI_CRDTBL_W_TXAMNT = 0;
                            double SR_AI_DSCNT_AMNT = 0;
                            double SR_AI_APPLD_DPST = 0;

                            for (Object o : arReceipt.getArAppliedInvoices()) {

                                LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) o;
                                SR_AI_APPLY_AMNT += arAppliedInvoice.getAiApplyAmount();
                                SR_AI_CRDTBL_W_TXAMNT += arAppliedInvoice.getAiCreditableWTax();
                                SR_AI_DSCNT_AMNT += arAppliedInvoice.getAiDiscountAmount();
                                SR_AI_APPLD_DPST += arAppliedInvoice.getAiAppliedDeposit();
                            }

                            // Set Apllied Invoice
                            mdetails.setSrAiApplyAmount(SR_AI_APPLY_AMNT);
                            mdetails.setSrAiCreditableWTax(SR_AI_CRDTBL_W_TXAMNT);
                            mdetails.setSrAiDiscountAmount(SR_AI_DSCNT_AMNT);
                            mdetails.setSrAiAppliedDeposit(SR_AI_APPLD_DPST);
                            mdetails.setSrRevenueAmount(REVENUE_AMOUNT);
                            list.add(mdetails);
                        }
                    }
                }
            }

            if (list.isEmpty() || list.size() == 0) {

                throw new GlobalNoRecordFoundException();
            }

            if (SHOW_ENTRIES) {
                list.sort(ArRepSalesRegisterDetails.CoaAccountNumberComparator);
            }

            switch (GROUP_BY) {
                case "CUSTOMER CODE":
                    list.sort(ArRepSalesRegisterDetails.CustomerCodeComparator);
                    break;
                case "CUSTOMER TYPE":
                    list.sort(ArRepSalesRegisterDetails.CustomerTypeComparator);
                    break;
                case "CUSTOMER CLASS":
                    list.sort(ArRepSalesRegisterDetails.CustomerClassComparator);
                    break;
                case "SALESPERSON":
                    list.sort(ArRepSalesRegisterDetails.SalespersonComparator);
                    break;
                case "PRODUCT":
                    list.sort(ArRepSalesRegisterDetails.ProductComparator);
                    break;
                default:
                    list.sort(ArRepSalesRegisterDetails.NoGroupComparator);
                    break;
            }

            if (SUMMARIZE) {
                list.sort(ArRepSalesRegisterDetails.CoaAccountNumberComparator);
            }

            ArRepSalesRegisterDetails mdetails = (ArRepSalesRegisterDetails) list.get(list.size() - 1);
            mdetails.setSrInvTotalNetAmount(GRAND_TOTAL_NET_AMNT);
            mdetails.setSrInvTotalTaxAmount(GRAND_TOTAL_TAX_AMNT);
            mdetails.setSrInvTotalAmount(GRAND_TOTAL_AMNT);

            endTime = System.currentTimeMillis();
            long seconds = TimeUnit.MILLISECONDS.toSeconds((endTime - startTime));
            Debug.print("Total execution time of executeArRepSalesRegister in seconds : " + seconds);

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeArRepSalesRegisterV2(ResultSet rsInv, ResultSet rsDr, ResultSet rsIli, HashMap criteria, ArrayList branchList, String ORDER_BY, String GROUP_BY, boolean PRINT_SALES_RELATED_ONLY, boolean SHOW_ENTRIES, boolean SUMMARIZE, boolean detailedSales, boolean summary, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepSalesRegisterControllerBean executeArRepSalesRegisterV2");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            double GRAND_TOTAL_NET_AMNT = 0d;
            double GRAND_TOTAL_TAX_AMNT = 0d;
            double GRAND_TOTAL_AMNT = 0d;
            double GRAND_TOTAL_GROSS_AMNT = 0d;

            if (criteria.get("includedInvoices").equals("YES")) {

                double REVENUE_AMOUNT = 0d;
                double TOTAL_NET_AMNT = 0d;
                double TOTAL_GROSS_AMNT = 0d;
                double TOTAL_TAX_AMNT = 0d;
                double TOTAL_DISC_AMNT = 0d;

                while (rsInv.next()) {

                    ArRepSalesRegisterDetails mdetails = new ArRepSalesRegisterDetails();

                    mdetails.setSrDate(EJBCommon.convertStringToSQLDate(rsInv.getString("INVOICE_DATE")));
                    mdetails.setSrInvoiceNumber(rsInv.getString("INVOICE_NUMBER"));
                    mdetails.setSrReferenceNumber(rsInv.getString("REFERENCE_NUMBER"));
                    mdetails.setSrDescription(rsInv.getString("INVOICE_DESC"));
                    mdetails.setSrCstCustomerCode(rsInv.getString("CUSTOMER_CODE") + "-" + rsInv.getString("CUSTOMER_NAME"));
                    mdetails.setSrCstCustomerClass(rsInv.getString("CUSTOMER_CLASS"));
                    mdetails.setSrCstName(rsInv.getString("CUSTOMER_NAME"));
                    mdetails.setSrCstCustomerCode2(rsInv.getString("CUSTOMER_CODE"));
                    mdetails.setPosted(EJBCommon.booleanToByte(rsInv.getString("INVOICE_POSTED") == "1" ? true : false));
                    mdetails.setSrCstCustomerType(rsInv.getString("CUSTOMER_TYPE") == null ? "UNDEFINE" : rsInv.getString("CUSTOMER_TYPE"));
                    mdetails.setSrAmount(EJBCommon.roundIt((rsInv.getString("CREDIT_MEMO") == "FALSE") ? Double.parseDouble(rsInv.getString("AMOUNT_DUE")) : (Double.parseDouble(rsInv.getString("AMOUNT_DUE")) * -1), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                    mdetails.setOrderBy(ORDER_BY);

                    while (rsIli.next()) {
                        TOTAL_GROSS_AMNT += Double.parseDouble(rsIli.getString("LINE_ITEM_AMOUNT")) + Double.parseDouble(rsIli.getString("LINE_ITEM_TAX_AMOUNT")) + Double.parseDouble(rsIli.getString("LINE_ITEM_TOTAL_DISCOUNT"));
                        TOTAL_NET_AMNT += Double.parseDouble(rsIli.getString("LINE_ITEM_AMOUNT"));
                        TOTAL_TAX_AMNT += Double.parseDouble(rsIli.getString("LINE_ITEM_TAX_AMOUNT"));
                        TOTAL_DISC_AMNT += Double.parseDouble(rsIli.getString("LINE_ITEM_TOTAL_DISCOUNT"));
                    }

                    mdetails.setSrServiceAmount(0d);
                    mdetails.setSrGrossAmount(TOTAL_GROSS_AMNT);
                    mdetails.setSrNetAmount(TOTAL_NET_AMNT);
                    mdetails.setSrTaxAmount(TOTAL_TAX_AMNT);
                    mdetails.setSrDiscountAmount(TOTAL_DISC_AMNT);

                    if (rsInv.getString("CREDIT_MEMO") == "1") {
                        GRAND_TOTAL_GROSS_AMNT -= TOTAL_GROSS_AMNT;
                        GRAND_TOTAL_NET_AMNT -= TOTAL_NET_AMNT;
                        GRAND_TOTAL_TAX_AMNT -= TOTAL_TAX_AMNT;
                        GRAND_TOTAL_AMNT -= (TOTAL_NET_AMNT + TOTAL_TAX_AMNT);
                    } else {
                        GRAND_TOTAL_GROSS_AMNT += TOTAL_GROSS_AMNT;
                        GRAND_TOTAL_NET_AMNT += TOTAL_NET_AMNT;
                        GRAND_TOTAL_TAX_AMNT += TOTAL_TAX_AMNT;
                        GRAND_TOTAL_AMNT += (TOTAL_NET_AMNT + TOTAL_TAX_AMNT);
                    }

                    mdetails.setSrRctType("INVOICE RELEASES");

                    // get receipt info
                    StringBuilder invoiceReceiptNumbers = null;
                    StringBuilder invoiceReceiptDates = null;
                    double invoiceReceiptAmount = 0.0d;

                    Collection arInvoiceReceipts = arReceiptHome.findByInvCodeAndRctVoid(Integer.parseInt(rsInv.getString("INVOICE_CODE")), EJBCommon.FALSE, AD_CMPNY);

                    for (Object arInvoiceReceipt : arInvoiceReceipts) {

                        LocalArReceipt arReceipt = (LocalArReceipt) arInvoiceReceipt;

                        if (invoiceReceiptNumbers == null) {
                            invoiceReceiptNumbers = new StringBuilder(arReceipt.getRctNumber());
                            invoiceReceiptAmount += arReceipt.getRctAmount();
                        } else if (invoiceReceiptNumbers != null && !invoiceReceiptNumbers.toString().contains(arReceipt.getRctNumber())) {
                            invoiceReceiptNumbers.append("/").append(arReceipt.getRctNumber());
                            invoiceReceiptAmount += arReceipt.getRctAmount();
                        }

                        if (invoiceReceiptDates == null) {
                            invoiceReceiptDates = new StringBuilder(EJBCommon.convertSQLDateToString(arReceipt.getRctDate()));
                        } else if (invoiceReceiptDates != null && !invoiceReceiptDates.toString().contains(EJBCommon.convertSQLDateToString(arReceipt.getRctDate()))) {
                            invoiceReceiptDates.append("/").append(EJBCommon.convertSQLDateToString(arReceipt.getRctDate()));
                        }
                    }
                    mdetails.setSrInvReceiptNumbers(invoiceReceiptNumbers == null ? "" : invoiceReceiptNumbers.toString());
                    mdetails.setSrOrAmount(invoiceReceiptAmount);
                    mdetails.setSrRevenueAmount(REVENUE_AMOUNT);

                    list.add(mdetails);
                }
            }

            if (list.isEmpty() || list.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            switch (GROUP_BY) {
                case "CUSTOMER CODE":
                    list.sort(ArRepSalesRegisterDetails.CustomerCodeComparator);
                    break;
                case "CUSTOMER TYPE":
                    list.sort(ArRepSalesRegisterDetails.CustomerTypeComparator);
                    break;
                case "CUSTOMER CLASS":
                    list.sort(ArRepSalesRegisterDetails.CustomerClassComparator);
                    break;
                case "SALESPERSON":
                    list.sort(ArRepSalesRegisterDetails.SalespersonComparator);
                    break;
                case "PRODUCT":
                    list.sort(ArRepSalesRegisterDetails.ProductComparator);
                    break;
                default:
                    list.sort(ArRepSalesRegisterDetails.NoGroupComparator);
                    break;
            }

            //TODO: Verify the end result of this grand totals?
//            ArRepSalesRegisterDetails mdetails = (ArRepSalesRegisterDetails) list.get(list.size() - 1);
//            mdetails.setSrInvTotalNetAmount(GRAND_TOTAL_NET_AMNT);
//            mdetails.setSrInvTotalTaxAmount(GRAND_TOTAL_TAX_AMNT);
//            mdetails.setSrInvTotalAmount(GRAND_TOTAL_AMNT);

            return list;

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }

    }

    private int getCriteriaSize(HashMap criteria, int criteriaSize) {
        if (criteria.containsKey("customerCode")) {
            criteriaSize--;
        }
        if (criteria.containsKey("customerBatch")) {
            criteriaSize--;
        }
        if (criteria.containsKey("unpostedOnly")) {
            criteriaSize--;
        }
        if (criteria.containsKey("includedUnposted")) {
            criteriaSize--;
        }
        if (criteria.containsKey("includedInvoices")) {
            criteriaSize--;
        }
        if (criteria.containsKey("includedMiscReceipts")) {
            criteriaSize--;
        }
        if (criteria.containsKey("includedCreditMemos")) {
            criteriaSize--;
        }
        if (criteria.containsKey("includedDebitMemos")) {
            criteriaSize--;
        }
        if (criteria.containsKey("paymentStatus")) {
            criteriaSize--;
        }
        if (criteria.containsKey("includedPriorDues")) {
            criteriaSize--;
        }
        if (criteria.containsKey("includedCollections")) {
            criteriaSize--;
        }
        if (criteria.containsKey("posted")) {
            criteriaSize--;
        }
        if (criteria.containsKey("orderStatus")) {
            criteriaSize--;
        }
        if (criteria.containsKey("summary")) {
            criteriaSize--;
        }
        if (criteria.containsKey("detailedSales")) {
            criteriaSize--;
        }
        if (criteria.containsKey("salesperson")) {
            criteriaSize--;
        }
        return criteriaSize;
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ArRepSalesRegisterControllerBean getAdCompany");

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

    public ArrayList getAdLvArRegionAll(Integer AD_CMPNY) {

        Debug.print("ArRepSalesRegisterControllerBean getAdLvArRegionAll");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AR REGION", AD_CMPNY);

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

    public ArrayList getArSlpAll(Integer AD_CMPNY) {

        Debug.print("ArRepSalesRegisterControllerBean getArSlpAll");

        ArrayList list = new ArrayList();

        try {

            Collection arSalespersons = arSalespersonHome.findSlpAll(AD_CMPNY);

            for (Object salesperson : arSalespersons) {
                LocalArSalesperson arSalesperson = (LocalArSalesperson) salesperson;
                list.add(arSalesperson.getSlpName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArIbAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArRepSalesRegisterControllerBean getArIbAll");

        ArrayList list = new ArrayList();

        try {

            Collection arInvoiceBatches = arInvoiceBatchHome.findOpenIbAll(AD_BRNCH, AD_CMPNY);

            for (Object invoiceBatch : arInvoiceBatches) {
                LocalArInvoiceBatch arInvoiceBatch = (LocalArInvoiceBatch) invoiceBatch;
                list.add(arInvoiceBatch.getIbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArRbAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArRepSalesRegisterControllerBean getArRbAll");

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

        Debug.print("ArRepSalesRegisterControllerBean convertForeignToFunctionalCurrency");

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

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT / CONVERSION_RATE;
        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private ArrayList getPriorDues(LocalArInvoice arInvoice, ArRepSalesRegisterDetails mdetails, HashMap criteria, ArrayList list) {
        ArrayList newList = new ArrayList();

        if (arInvoice.getInvAmountDue() == arInvoice.getInvAmountPaid()) {

            Collection arInvoicePaymentSchedules = arInvoice.getArInvoicePaymentSchedules();

            if (!arInvoicePaymentSchedules.isEmpty()) {

                for (Object invoicePaymentSchedule : arInvoicePaymentSchedules) {

                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) invoicePaymentSchedule;
                    Collection arAppliedInvoices = arInvoicePaymentSchedule.getArAppliedInvoices();

                    if (!arAppliedInvoices.isEmpty()) {

                        for (Object appliedInvoice : arAppliedInvoices) {

                            LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoice;

                            if (!(arAppliedInvoice.getArReceipt().getRctDate().before((Date) criteria.get("dateFrom")))) {

                                list.add(mdetails);
                            }
                        }
                    }
                }
            }
        } else {

            double newInvoiceAmount = arInvoice.getInvAmountDue() - arInvoice.getInvAmountPaid();

            Collection arInvoicePaymentSchedules = arInvoice.getArInvoicePaymentSchedules();

            if (!arInvoicePaymentSchedules.isEmpty()) {

                for (Object invoicePaymentSchedule : arInvoicePaymentSchedules) {

                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) invoicePaymentSchedule;
                    Collection arAppliedInvoices = arInvoicePaymentSchedule.getArAppliedInvoices();

                    if (!arAppliedInvoices.isEmpty()) {

                        for (Object appliedInvoice : arAppliedInvoices) {

                            LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoice;

                            if (!(arAppliedInvoice.getArReceipt().getRctDate().before((Date) criteria.get("dateFrom")))) {

                                newInvoiceAmount += arAppliedInvoice.getArReceipt().getRctAmount();
                                mdetails.setSrAmount(newInvoiceAmount);
                                list.add(mdetails);

                            } else {

                                mdetails.setSrAmount(newInvoiceAmount);
                                list.add(mdetails);
                            }
                        }
                    } else {
                        mdetails.setSrAmount(newInvoiceAmount);
                        list.add(mdetails);
                    }
                }
            }
        }

        newList = list;

        return newList;
    }

    public void ejbCreate() throws CreateException {

        Debug.print("ArRepSalesRegisterControllerBean ejbCreate");
    }
}