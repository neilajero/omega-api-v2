/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class ArRepCustomerListControllerBean
 * @created March 03, 2004, 9:29 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txnreports.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

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
import com.ejb.entities.ar.LocalArAppliedInvoice;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.dao.ar.LocalArCustomerBalanceHome;
import com.ejb.entities.ar.LocalArCustomerClass;
import com.ejb.dao.ar.LocalArCustomerClassHome;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.entities.ar.LocalArCustomerType;
import com.ejb.dao.ar.LocalArCustomerTypeHome;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepCustomerListDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ArRepCustomerListControllerEJB")
public class ArRepCustomerListControllerBean extends EJBContextClass implements ArRepCustomerListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalArCustomerBalanceHome arCustomerBalanceHome;
    @EJB
    private LocalArCustomerClassHome arCustomerClassHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalArCustomerTypeHome arCustomerTypeHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;


    public void executeSpArRepCustomerList(String STORED_PROCEDURE, String CUSTOMER_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepCustomerListControllerBean executeSpArRepCustomerList");

        try {
            StoredProcedureQuery spQuery = em.createStoredProcedureQuery(STORED_PROCEDURE)
                    .registerStoredProcedureParameter("customerCode", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("adCompany", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("resultCount", Integer.class, ParameterMode.OUT);

            spQuery.setParameter("customerCode", CUSTOMER_CODE);
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


    public ArrayList getArCcAll(Integer AD_CMPNY) {

        Debug.print("ArRepCustomerListControllerBean getArCcAll");

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

        Debug.print("ArRepCustomerListControllerBean getArCtAll");

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

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepCustomerListControllerBean getAdBrResAll");


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

    public ArrayList executeArRepCustomerList(HashMap criteria, ArrayList branchList, String date, String salesPerson, String ORDER_BY, String GROUP_BY, boolean splitBySalesperson, boolean includedNegativeBalances, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepCustomerListControllerBean executeArRepCustomerList");

        LocalAdCompany adCompany = null;

        ArrayList list = new ArrayList();

        try {

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT DISTINCT OBJECT(cst) FROM ArCustomer cst, IN(cst.adBranchCustomers)bcst WHERE (");

            if (branchList.isEmpty()) throw new GlobalNoRecordFoundException();

            Iterator brIter = branchList.iterator();

            AdBranchDetails brDetails = (AdBranchDetails) brIter.next();
            jbossQl.append("bcst.adBranch.brCode=").append(brDetails.getBrCode());

            while (brIter.hasNext()) {

                brDetails = (AdBranchDetails) brIter.next();

                jbossQl.append(" OR bcst.adBranch.brCode=").append(brDetails.getBrCode());
            }

            jbossQl.append(") ");
            firstArgument = false;

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("customerCode")) {

                criteriaSize--;
            }

            if (criteria.containsKey("date")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includeZeroes")) {

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

                jbossQl.append("cst.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
            }

            if (criteria.containsKey("customerType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.arCustomerType.ctName=?").append(ctr + 1).append(" ");
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

                jbossQl.append("cst.arCustomerClass.ccName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerClass");
                ctr++;
            }

            if (criteria.containsKey("customerRegion")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.cstAdLvRegion=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerRegion");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("cst.cstAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            switch (ORDER_BY) {
                case "CUSTOMER CODE":

                    orderBy = "cst.cstCustomerCode";
                    break;
                case "CUSTOMER NAME":

                    orderBy = "cst.cstName";

                    break;
                case "CUSTOMER TYPE":

                    orderBy = "cst.arCustomerType.ctName";

                    break;
                case "CUSTOMER CLASS":

                    orderBy = "cst.arCustomerClass.ccName";

                    break;
                case "CUSTOMER REGION":

                    orderBy = "cst.cstAdLvRegion";
                    break;
            }

            if (GROUP_BY.equals("CUSTOMER CODE")) {

                orderBy = "cst.cstCustomerCode";
            }

            if (GROUP_BY.equals("SALESPERSON")) {

                orderBy = "cst.cstCustomerCode";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Debug.print(jbossQl.toString());

            try {
                adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            Debug.print("jbossQl.toString()=" + jbossQl.toString());
            Collection arCustomers = arCustomerHome.getCstByCriteria(jbossQl.toString(), obj, 0, 0);

            if (arCustomers.size() == 0) throw new GlobalNoRecordFoundException();

            Iterator i = arCustomers.iterator();
            int a = 0;

            while (i.hasNext()) {

                LocalArCustomer arCustomer = (LocalArCustomer) i.next();

                if (splitBySalesperson) {

                    ArrayList salesPersonList = new ArrayList();
                    HashMap hmSalesPerson = new HashMap();
                    HashMap hmSalesPerson1 = new HashMap();

                    Collection arInvoice2 = arInvoiceHome.findByInvNumberAndInvCreditMemoAndInvDateAndCustomerCodeOrderBySlp((byte) 0, (Date) criteria.get("date"), arCustomer.getCstCustomerCode().trim(), AD_CMPNY);

                    if (!arInvoice2.isEmpty()) {

                        for (Object value : arInvoice2) {
                            double balance = 0d;
                            LocalArInvoice arInvoice = (LocalArInvoice) value;
                            Date date1 = (Date) criteria.get("date");
                            Date invDate = arInvoice.getInvDate();
                            String invNum = arInvoice.getInvNumber().trim();
                            double miscAm = 0d;

                            int results = date1.compareTo(invDate);
                            ArRepCustomerListDetails details = new ArRepCustomerListDetails();
                            if (results >= 0) {

                                if (arInvoice.getInvCreditMemo() == 0) {

                                    double invoiceAmount = 0d;
                                    double cmAmount = 0d;
                                    double aiAmount = 0d;
                                    double aiAmountMisc = 0d;
                                    invoiceAmount = arInvoice.getInvAmountDue();

                                    try {
                                        if (arInvoice.getArSalesperson().getSlpSalespersonCode().equals(null)) {
                                            details.setSalesPerson(null);
                                        } else {

                                            details.setSalesPerson(arInvoice.getArSalesperson().getSlpSalespersonCode());
                                            details.setSalesPersonCrt(salesPerson);
                                        }
                                    } catch (Exception e) {

                                        details.setSalesPerson(null);
                                    }

                                    // get CM

                                    Collection arInvoices1 = arCustomer.getArInvoices();
                                    for (Object o : arInvoices1) {
                                        LocalArInvoice arInvoice1 = (LocalArInvoice) o;
                                        Date invDate1 = arInvoice1.getInvDate();
                                        int results1 = date1.compareTo(invDate1);
                                        if (results1 >= 0) {
                                            if (arInvoice1.getInvCreditMemo() == 1) {

                                                if (arInvoice1.getInvCmInvoiceNumber().trim().equalsIgnoreCase(invNum)) {
                                                    cmAmount += arInvoice1.getInvAmountDue();
                                                }
                                            }
                                        }
                                    }

                                    // get OR/Collection

                                    Collection arReceipts = arReceiptHome.findByInvCodeAndRctVoid(arInvoice.getInvCode(), EJBCommon.FALSE, AD_CMPNY);
                                    for (Object receipt : arReceipts) {
                                        LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                                        Date recDate = arReceipt.getRctDate();
                                        int results2 = date1.compareTo(recDate);
                                        if (results2 >= 0) {
                                            if (arReceipt.getRctType().equals("COLLECTION")) {

                                                String arRctPk = arReceipt.getRctCode().toString().trim();

                                                Iterator iterAI = arReceipt.getArAppliedInvoices().iterator();
                                                String ipsInvCode = "";

                                                while (iterAI.hasNext()) {
                                                    LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) iterAI.next();
                                                    ipsInvCode = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode().toString().trim();

                                                    if (arInvoice.getInvCode().toString().trim().equals(ipsInvCode.trim())) {

                                                        aiAmount += arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount();
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    //
                                    // get Misc
                                    Collection arReceiptsMisc = arReceiptHome.findPostedRctByRctDateToAndCstCode((Date) criteria.get("date"), arCustomer.getCstCustomerCode(), AD_CMPNY);
                                    Iterator jjMisc = arReceiptsMisc.iterator();
                                    aiAmountMisc = 0;
                                    while (jjMisc.hasNext()) {

                                        LocalArReceipt arReceiptMisc = (LocalArReceipt) jjMisc.next();

                                        Date recDate1 = arReceiptMisc.getRctDate();
                                        int results3 = date1.compareTo(recDate1);
                                        if (results3 >= 0) {
                                            if (arReceiptMisc.getRctType().equals("MISC")) {
                                                try {

                                                    if (arReceiptMisc.getArCustomer().getCstCustomerCode().trim().equals(arCustomer.getCstCustomerCode().trim()) && arReceiptMisc.getArSalesperson().toString().trim().equals(arInvoice.getArSalesperson().toString().trim())) {

                                                        aiAmountMisc += arReceiptMisc.getRctAmount();
                                                    }
                                                } catch (Exception e) {

                                                }
                                            }
                                        }
                                    }

                                    invoiceAmount -= cmAmount;

                                    if (includedNegativeBalances) {
                                        double balance1 = invoiceAmount - aiAmount;
                                        balance = EJBCommon.roundIt(balance1, adCompany.getGlFunctionalCurrency().getFcPrecision());
                                        miscAm = aiAmountMisc;
                                    } else {
                                        double balance1 = invoiceAmount - aiAmount;
                                        balance = EJBCommon.roundIt(balance1, adCompany.getGlFunctionalCurrency().getFcPrecision());
                                    }
                                }
                            }
                            if (includedNegativeBalances) {

                                details.setClCstBalance(balance);
                                details.setClCstCustomerCode(arCustomer.getCstCustomerCode());
                                details.setClCstName(arCustomer.getCstName());
                                details.setClCstContact(arCustomer.getCstContact());
                                details.setClCstPhone(arCustomer.getCstPhone());
                                details.setClCstTin(arCustomer.getCstTin());
                                details.setClCstCreditLimit(arCustomer.getCstCreditLimit());
                                details.setClCstAddress(arCustomer.getCstAddress());
                                details.setClCstFax(arCustomer.getCstFax());
                                details.setClCstPaymentTerm(arCustomer.getAdPaymentTerm().getPytName());
                                details.setClCstType(arCustomer.getArCustomerType() == null ? "" : arCustomer.getArCustomerType().getCtName());
                                details.setCustomerRegion(arCustomer.getCstAdLvRegion());
                                details.setClCstDealPrice(arCustomer.getCstDealPrice());
                                details.setIncludedNegativeBalances(true);
                                details.setClRcptBalance(miscAm);
                                details.setSalesPersonCrt(salesPerson);

                                list.add(details);
                                balance = 0d;
                                miscAm = 0d;

                            } else {

                                details.setClCstBalance(balance);
                                details.setClCstCustomerCode(arCustomer.getCstCustomerCode());
                                details.setClCstName(arCustomer.getCstName());
                                details.setClCstContact(arCustomer.getCstContact());
                                details.setClCstPhone(arCustomer.getCstPhone());
                                details.setClCstTin(arCustomer.getCstTin());
                                details.setClCstCreditLimit(arCustomer.getCstCreditLimit());
                                details.setClCstAddress(arCustomer.getCstAddress());
                                details.setClCstFax(arCustomer.getCstFax());
                                details.setClCstPaymentTerm(arCustomer.getAdPaymentTerm().getPytName());
                                details.setClCstType(arCustomer.getArCustomerType() == null ? "" : arCustomer.getArCustomerType().getCtName());
                                details.setCustomerRegion(arCustomer.getCstAdLvRegion());
                                details.setClCstDealPrice(arCustomer.getCstDealPrice());
                                details.setClRcptBalance(0);
                                details.setSalesPersonCrt(salesPerson);

                                list.add(details);
                                balance = 0d;
                                miscAm = 0d;
                            }
                        }

                        // customer deposit

                        Collection customerDep = arReceiptHome.findOpenDepositEnabledPostedRctByCstCustomerCodeOrderBySlp((Date) criteria.get("date"), arCustomer.getCstCustomerCode(), AD_CMPNY);

                        Iterator jjCD = customerDep.iterator();

                        double cstmrDpst = 0d;

                        while (jjCD.hasNext()) {

                            LocalArReceipt cstDep = (LocalArReceipt) jjCD.next();
                            Collection arInvoice3 = arInvoiceHome.findByInvNumberAndInvCreditMemoAndInvDateAndCustomerCodeSlpCodeOrderBySlp((byte) 0, (Date) criteria.get("date"), arCustomer.getCstCustomerCode().trim(), cstDep.getArSalesperson().getSlpSalespersonCode().trim(), AD_CMPNY);

                            if (arInvoice3.isEmpty()) {
                                if (includedNegativeBalances) {
                                    ArRepCustomerListDetails details = new ArRepCustomerListDetails();

                                    details.setClCstBalance(0);
                                    details.setClCstCustomerCode(arCustomer.getCstCustomerCode());
                                    details.setClCstName(arCustomer.getCstName());
                                    details.setClCstContact(arCustomer.getCstContact());
                                    details.setClCstPhone(arCustomer.getCstPhone());
                                    details.setClCstTin(arCustomer.getCstTin());
                                    details.setClCstCreditLimit(arCustomer.getCstCreditLimit());
                                    details.setClCstAddress(arCustomer.getCstAddress());
                                    details.setClCstFax(arCustomer.getCstFax());
                                    details.setClCstPaymentTerm(arCustomer.getAdPaymentTerm().getPytName());
                                    details.setClCstType(arCustomer.getArCustomerType() == null ? "" : arCustomer.getArCustomerType().getCtName());
                                    details.setCustomerRegion(arCustomer.getCstAdLvRegion());
                                    details.setClCstDealPrice(arCustomer.getCstDealPrice());
                                    details.setClRcptBalance(cstDep.getRctAmount());
                                    details.setSalesPerson(cstDep.getArSalesperson().getSlpSalespersonCode().trim());
                                    details.setSalesPersonCrt(salesPerson);

                                    list.add(details);
                                }
                            }
                        }

                        // end customer deposit
                    } // if customr has no invoice
                    else {
                        if (includedNegativeBalances) {
                            // get Misc
                            Collection arReceiptsMisc = arReceiptHome.findPostedRctByRctDateToAndCstCode((Date) criteria.get("date"), arCustomer.getCstCustomerCode(), AD_CMPNY);
                            Iterator jjMisc = arReceiptsMisc.iterator();
                            double aiAmountMisc1 = 0d;
                            String salespersonMisc = "";
                            while (jjMisc.hasNext()) {

                                LocalArReceipt arReceiptMisc = (LocalArReceipt) jjMisc.next();

                                if (arReceiptMisc.getRctType().equals("MISC")) {
                                    try {
                                        salespersonMisc = arReceiptMisc.getArSalesperson().getSlpSalespersonCode().trim();
                                        if (arReceiptMisc.getArCustomer().getCstCustomerCode().trim().equals(arCustomer.getCstCustomerCode().trim())) {

                                            aiAmountMisc1 += arReceiptMisc.getRctAmount();
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            }
                            ArRepCustomerListDetails details = new ArRepCustomerListDetails();

                            details.setSalesPerson(salespersonMisc);
                            details.setClCstBalance(0);
                            details.setClCstCustomerCode(arCustomer.getCstCustomerCode());
                            details.setClCstName(arCustomer.getCstName());
                            details.setClCstContact(arCustomer.getCstContact());
                            details.setClCstPhone(arCustomer.getCstPhone());
                            details.setClCstTin(arCustomer.getCstTin());
                            details.setClCstCreditLimit(arCustomer.getCstCreditLimit());
                            details.setClCstAddress(arCustomer.getCstAddress());
                            details.setClCstFax(arCustomer.getCstFax());
                            details.setClCstPaymentTerm(arCustomer.getAdPaymentTerm().getPytName());
                            details.setClCstType(arCustomer.getArCustomerType() == null ? "" : arCustomer.getArCustomerType().getCtName());
                            details.setCustomerRegion(arCustomer.getCstAdLvRegion());
                            details.setClCstDealPrice(arCustomer.getCstDealPrice());
                            details.setClRcptBalance(aiAmountMisc1);
                            details.setIncludedNegativeBalances(true);
                            details.setSalesPersonCrt(salesPerson);

                            list.add(details);
                        }
                    }

                } else {

                    ArrayList salesPersonList = new ArrayList();
                    HashMap hmSalesPerson = new HashMap();
                    HashMap hmSalesPerson1 = new HashMap();

                    // Collection  arInvoiceNull = arCustomer.getArInvoices();

                    Collection arInvoice2 = arInvoiceHome.findByInvNumberAndInvCreditMemoAndInvDateAndCustomerCode((byte) 0, (Date) criteria.get("date"), arCustomer.getCstCustomerCode().trim(), AD_CMPNY);

                    if (!arInvoice2.isEmpty()) {
                        Iterator j = arInvoice2.iterator();
                        ArRepCustomerListDetails details = new ArRepCustomerListDetails();
                        double balance = 0d;
                        double miscAm = 0d;
                        double invoiceAmount = 0d;
                        double cmAmount = 0d;
                        double aiAmount = 0d;
                        double aiAmountMisc = 0d;
                        while (j.hasNext()) {

                            LocalArInvoice arInvoice = (LocalArInvoice) j.next();

                            Date date1 = (Date) criteria.get("date");
                            Date invDate = arInvoice.getInvDate();
                            String invNum = arInvoice.getInvNumber().trim();

                            int results = date1.compareTo(invDate);

                            if (results >= 0) {

                                if (arInvoice.getInvCreditMemo() == 0) {

                                    invoiceAmount += arInvoice.getInvAmountDue();

                                    try {
                                        if (arInvoice.getArSalesperson().getSlpSalespersonCode().equals(null)) {
                                            details.setSalesPerson(null);
                                        } else {

                                            details.setSalesPerson(arInvoice.getArSalesperson().getSlpSalespersonCode());
                                        }
                                    } catch (Exception e) {

                                        details.setSalesPerson(null);
                                    }

                                    // get CM

                                    Collection arInvoices1 = arCustomer.getArInvoices();
                                    for (Object o : arInvoices1) {
                                        LocalArInvoice arInvoice1 = (LocalArInvoice) o;
                                        Date invDate1 = arInvoice1.getInvDate();
                                        int results1 = date1.compareTo(invDate1);
                                        if (results1 >= 0) {
                                            if (arInvoice1.getInvCreditMemo() == 1) {

                                                if (arInvoice1.getInvCmInvoiceNumber().trim().equalsIgnoreCase(invNum)) {
                                                    cmAmount += arInvoice1.getInvAmountDue();
                                                }
                                            }
                                        }
                                    }

                                    // get OR/Collection

                                    Collection arReceipts = arReceiptHome.findByInvCodeAndRctVoid(arInvoice.getInvCode(), EJBCommon.FALSE, AD_CMPNY);
                                    for (Object receipt : arReceipts) {
                                        LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                                        Date recDate = arReceipt.getRctDate();
                                        int results2 = date1.compareTo(recDate);
                                        if (results2 >= 0) {
                                            if (arReceipt.getRctType().equals("COLLECTION")) {

                                                String arRctPk = arReceipt.getRctCode().toString().trim();

                                                Iterator iterAI = arReceipt.getArAppliedInvoices().iterator();
                                                String ipsInvCode = "";

                                                while (iterAI.hasNext()) {
                                                    LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) iterAI.next();
                                                    ipsInvCode = arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvCode().toString().trim();

                                                    if (arInvoice.getInvCode().toString().trim().equals(ipsInvCode.trim())) {

                                                        aiAmount += arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditableWTax() + arAppliedInvoice.getAiDiscountAmount();
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    //
                                    // get Misc
                                    Collection arReceiptsMisc = arReceiptHome.findPostedRctByRctDateToAndCstCode((Date) criteria.get("date"), arCustomer.getCstCustomerCode(), AD_CMPNY);
                                    Iterator jjMisc = arReceiptsMisc.iterator();
                                    aiAmountMisc = 0d;
                                    while (jjMisc.hasNext()) {

                                        LocalArReceipt arReceiptMisc = (LocalArReceipt) jjMisc.next();

                                        Date recDate1 = arReceiptMisc.getRctDate();
                                        int results3 = date1.compareTo(recDate1);
                                        if (results3 >= 0) {
                                            if (arReceiptMisc.getRctType().equals("MISC")) {
                                                try {

                                                    aiAmountMisc += arReceiptMisc.getRctAmount();

                                                } catch (Exception e) {

                                                }
                                            }
                                        }
                                    }
                                } // endwhile all inv customer
                            }
                        }

                        invoiceAmount -= cmAmount;

                        if (includedNegativeBalances) {

                            double balance1 = invoiceAmount - aiAmount;
                            balance = EJBCommon.roundIt(balance1, adCompany.getGlFunctionalCurrency().getFcPrecision());
                            miscAm = aiAmountMisc;

                        } else {
                            double balance1 = invoiceAmount - aiAmount;
                            balance = EJBCommon.roundIt(balance1, adCompany.getGlFunctionalCurrency().getFcPrecision());
                        }

                        if (includedNegativeBalances) {

                            details.setClCstBalance(balance);
                            details.setClCstCustomerCode(arCustomer.getCstCustomerCode());
                            details.setClCstName(arCustomer.getCstName());
                            details.setClCstContact(arCustomer.getCstContact());
                            details.setClCstPhone(arCustomer.getCstPhone());
                            details.setClCstTin(arCustomer.getCstTin());
                            details.setClCstCreditLimit(arCustomer.getCstCreditLimit());
                            details.setClCstAddress(arCustomer.getCstAddress());
                            details.setClCstFax(arCustomer.getCstFax());
                            details.setClCstPaymentTerm(arCustomer.getAdPaymentTerm().getPytName());
                            details.setClCstType(arCustomer.getArCustomerType() == null ? "" : arCustomer.getArCustomerType().getCtName());
                            details.setCustomerRegion(arCustomer.getCstAdLvRegion());
                            details.setClCstDealPrice(arCustomer.getCstDealPrice());
                            details.setIncludedNegativeBalances(true);
                            details.setClRcptBalance(miscAm);
                            details.setSalesPersonCrt(salesPerson);

                            list.add(details);
                            balance = 0d;
                            miscAm = 0d;

                        } else {

                            details.setClCstBalance(balance);
                            details.setClCstCustomerCode(arCustomer.getCstCustomerCode());
                            details.setClCstName(arCustomer.getCstName());
                            details.setClCstContact(arCustomer.getCstContact());
                            details.setClCstPhone(arCustomer.getCstPhone());
                            details.setClCstTin(arCustomer.getCstTin());
                            details.setClCstCreditLimit(arCustomer.getCstCreditLimit());
                            details.setClCstAddress(arCustomer.getCstAddress());
                            details.setClCstFax(arCustomer.getCstFax());
                            details.setClCstPaymentTerm(arCustomer.getAdPaymentTerm().getPytName());
                            details.setClCstType(arCustomer.getArCustomerType() == null ? "" : arCustomer.getArCustomerType().getCtName());
                            details.setCustomerRegion(arCustomer.getCstAdLvRegion());
                            details.setClCstDealPrice(arCustomer.getCstDealPrice());
                            details.setClRcptBalance(0);
                            details.setSalesPersonCrt(salesPerson);

                            list.add(details);
                            balance = 0d;
                        }
                    } // no invoice
                    else {
                        if (includedNegativeBalances) {
                            // get Misc
                            Collection arReceiptsMisc = arReceiptHome.findPostedRctByRctDateToAndCstCode((Date) criteria.get("date"), arCustomer.getCstCustomerCode(), AD_CMPNY);
                            Iterator jjMisc = arReceiptsMisc.iterator();
                            double aiAmountMisc1 = 0d;
                            String salespersonMisc = "";
                            while (jjMisc.hasNext()) {

                                LocalArReceipt arReceiptMisc = (LocalArReceipt) jjMisc.next();

                                if (arReceiptMisc.getRctType().equals("MISC")) {
                                    try {
                                        salespersonMisc = arReceiptMisc.getArSalesperson().getSlpSalespersonCode().trim();
                                        if (arReceiptMisc.getArCustomer().getCstCustomerCode().trim().equals(arCustomer.getCstCustomerCode().trim())) {

                                            aiAmountMisc1 += arReceiptMisc.getRctAmount();
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            }
                            ArRepCustomerListDetails details = new ArRepCustomerListDetails();

                            details.setSalesPerson(salespersonMisc);
                            details.setClCstBalance(0);
                            details.setClCstCustomerCode(arCustomer.getCstCustomerCode());
                            details.setClCstName(arCustomer.getCstName());
                            details.setClCstContact(arCustomer.getCstContact());
                            details.setClCstPhone(arCustomer.getCstPhone());
                            details.setClCstTin(arCustomer.getCstTin());
                            details.setClCstCreditLimit(arCustomer.getCstCreditLimit());
                            details.setClCstAddress(arCustomer.getCstAddress());
                            details.setClCstFax(arCustomer.getCstFax());
                            details.setClCstPaymentTerm(arCustomer.getAdPaymentTerm().getPytName());
                            details.setClCstType(arCustomer.getArCustomerType() == null ? "" : arCustomer.getArCustomerType().getCtName());
                            details.setCustomerRegion(arCustomer.getCstAdLvRegion());
                            details.setClCstDealPrice(arCustomer.getCstDealPrice());
                            details.setClRcptBalance(aiAmountMisc1);
                            details.setIncludedNegativeBalances(true);
                            details.setSalesPersonCrt(salesPerson);

                            list.add(details);
                        }
                    }
                }
            }

            if (GROUP_BY.equals("SALESPERSON")) {

                list.sort(ArRepCustomerListDetails.CustomerCodeComparator);
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

        Debug.print("ArRepCustomerListControllerBean getAdCompany");

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

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ArRepCustomerListControllerBean ejbCreate");
    }
}