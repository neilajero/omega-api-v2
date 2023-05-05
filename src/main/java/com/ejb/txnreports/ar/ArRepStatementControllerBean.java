package com.ejb.txnreports.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.*;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.ad.AdPreferenceDetails;
import com.util.reports.ar.ArRepStatementDetails;

import jakarta.ejb.*;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Stateless(name = "ArRepStatementControllerEJB")
public class ArRepStatementControllerBean extends EJBContextClass implements ArRepStatementController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalArCustomerClassHome arCustomerClassHome;
    @EJB
    private LocalArCustomerTypeHome arCustomerTypeHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;
    @EJB
    private LocalArInvoiceBatchHome arInvoiceBatchHome;

    public void executeSpArRepStatementOfAccount(String STORED_PROCEDURE, Date CUTT_OF_DT, String CUSTOMER_BATCH, String CUSTOMER_CODE, boolean INCLUDE_UNPOSTED, boolean INCLUDE_ADVANCE, boolean INCLUDE_ADVANCE_ONLY, String BRANCH_CODES, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepStatementControllerBean executeSpArRepStatementOfAccount");

        try {
            StoredProcedureQuery spQuery = em.createStoredProcedureQuery(STORED_PROCEDURE)
                    .registerStoredProcedureParameter("cutoffDate", Date.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("customerBatch", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("customerCode", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("includeUnposted", Boolean.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("includeAdvance", Boolean.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("includeAdvanceOnly", Boolean.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("branchCode", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("adCompany", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("resultCount", Integer.class, ParameterMode.OUT);


            spQuery.setParameter("cutoffDate", CUTT_OF_DT);
            spQuery.setParameter("customerBatch", CUSTOMER_BATCH);
            spQuery.setParameter("customerCode", CUSTOMER_CODE);
            spQuery.setParameter("includeUnposted", INCLUDE_UNPOSTED);
            spQuery.setParameter("includeAdvance", INCLUDE_ADVANCE);
            spQuery.setParameter("includeAdvanceOnly", INCLUDE_ADVANCE_ONLY);
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

    public ArrayList getAdLvReportTypeAll(Integer AD_CMPNY) {

        Debug.print("ArRepStatementControllerBean getAdLvReportTypeAll");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AR REPORT TYPE - STATEMENT OF ACCOUNT", AD_CMPNY);

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

        Debug.print("ArRepStatementControllerBean getArCcAll");

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

        Debug.print("ArRepStatementControllerBean getArCtAll");
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

    public ArrayList getAdLvCustomerBatchAll(Integer AD_CMPNY) {

        Debug.print("ArRepStatementControllerBean getAdLvCustomerBatchAll");

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

    public ArrayList getAdLvCustomerDepartmentAll(Integer AD_CMPNY) {

        Debug.print("ArRepStatementControllerBean getAdLvCustomerDepartmentAll");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AR CUSTOMER DEPARTMENT - SOA", AD_CMPNY);

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

    public ArrayList getArCstAll(ArrayList customerBatchList, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArRepStatementControllerBean getArCstAll");

        ArrayList list = new ArrayList();

        try {

            Collection arCustomers = arCustomerHome.findEnabledCstAll(AD_BRNCH, AD_CMPNY);

            for (Object customer : arCustomers) {
                LocalArCustomer arCustomer = (LocalArCustomer) customer;

                if (customerBatchList.size() > 0) {

                    for (Object o : customerBatchList) {

                        if (arCustomer.getCstCustomerBatch().equals(o.toString())) {
                            list.add(arCustomer.getCstCustomerCode());
                            break;
                        }
                    }

                } else {
                    list.add(arCustomer.getCstCustomerCode());
                }
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArCstAllbyCustomerBatch(String CST_BTCH, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArRepStatementControllerBean getArCstAllbyCustomerBatch");

        ArrayList list = new ArrayList();

        try {

            Collection arCustomers = arCustomerHome.findEnabledCstAllbyCustomerBatch(CST_BTCH, AD_BRNCH, AD_CMPNY);

            for (Object customer : arCustomers) {

                LocalArCustomer arCustomer = (LocalArCustomer) customer;

                list.add(arCustomer.getCstCustomerCode());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ArRepStatementControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpZip(adCompany.getCmpZip());
            details.setCmpPhone(adCompany.getCmpPhone());
            details.setCmpFax(adCompany.getCmpFax());
            details.setCmpEmail(adCompany.getCmpEmail());
            details.setCmpTin(adCompany.getCmpTin());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepStatementControllerBean getAdBrResAll");

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

    public AdPreferenceDetails getAdPreference(Integer AD_CMPNY) {

        Debug.print("ArRepStatementControllerBean getAdPreference");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            AdPreferenceDetails details = new AdPreferenceDetails();
            details.setPrfApAgingBucket(adPreference.getPrfApAgingBucket());
            details.setPrfArAgingBucket(adPreference.getPrfArAgingBucket());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArSmlAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArRepStatementControllerBean getArSmlAll");

        ArrayList list = new ArrayList();

        try {

            Collection arStandardMemoLines = arStandardMemoLineHome.findEnabledSmlAll(AD_BRNCH, AD_CMPNY);

            for (Object standardMemoLine : arStandardMemoLines) {

                LocalArStandardMemoLine arStandardMemoLine = (LocalArStandardMemoLine) standardMemoLine;
                list.add(arStandardMemoLine.getSmlName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }


    public ArrayList executeSpArRepStatement(ResultSet rs, HashMap criteria, ArrayList branchList, String AGNG_BY, String GROUP_BY, String currency, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        final long startTime = System.currentTimeMillis();
        final long endTime;

        Debug.print("ArRepStatementControllerBean executeSpArRepStatement");

        ArrayList list = new ArrayList();
        Date agingDate = null;

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            short precisionUnit = adCompany.getGlFunctionalCurrency().getFcPrecision();

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(adCompany.getCmpCode());
            int agingBucket1 = adPreference.getPrfArAgingBucket();
            int agingBucket2 = adPreference.getPrfArAgingBucket() * 2;
            int agingBucket3 = adPreference.getPrfArAgingBucket() * 3;
            int agingBucket4 = adPreference.getPrfArAgingBucket() * 4;

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            String includeAdvance = (String) criteria.get("includeAdvance");
            String includeAdvanceOnly = (String) criteria.get("includeAdvanceOnly");
            String includePaid = (String) criteria.get("includePaid");

            while (rs.next()) {

                ArRepStatementDetails mdetails = new ArRepStatementDetails();

                mdetails.setSmtCustomerCode(rs.getString("CSTMR_CODE"));
                mdetails.setSmtCustomerName(rs.getString("CSTMR_NM"));

                mdetails.setSmtTransactionDate(rs.getDate("TRNSCTN_DT"));
                mdetails.setSmtTransactionNumber(rs.getString("TRNSCTN_NMBR"));
                mdetails.setSmtTransactionReferenceNumber(rs.getString("TRNSCTN_RFRNC_NMBR"));
                mdetails.setSmtTransactionDescription(rs.getString("TRNSCTN_DSCRPTN"));
                mdetails.setSmtTransactionAmount(rs.getDouble("TRNSCTN_AMNT"));

                String transactionAge = rs.getString("TRNSCTN_AGE");
                int transactionBranch = rs.getInt("TRNSCTN_BRNCH");

                Integer transAge = 0;

                if (!transactionAge.equals("CURRENT")) {
                    if (transactionAge.equals("330 OVER")) {
                        transAge = 331;
                    } else if (transactionAge.equals("N/A")) {
                        transAge = 331;
                    } else {
                        transAge = Integer.parseInt(transactionAge);
                    }
                }


                list.add(mdetails);
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }


            endTime = System.currentTimeMillis();
            long seconds = TimeUnit.MILLISECONDS.toSeconds((endTime - startTime));

            Debug.print("Total execution time of executeArRepStatement in seconds : " + seconds);

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ArRepStatementControllerBean ejbCreate");
    }

    public ArrayList getArOpenIbAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArRepStatementControllerBean getArOpenIbAll");

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

    public ArrayList getAdLvDocumentTypeAll(Integer AD_CMPNY) {

        Debug.print("ArRepStatementControllerBean getAdLvDocumentTypeAll");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AR INVOICE DOCUMENT TYPE", AD_CMPNY);

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

}