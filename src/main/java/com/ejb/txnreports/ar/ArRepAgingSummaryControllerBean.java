/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepAgingSummaryControllerBean
 * @created July 13, 2005, 02:47 PM
 * @author Arnel Masikip
 */
package com.ejb.txnreports.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.cm.LocalCmAdjustmentHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.ad.AdPreferenceDetails;
import com.util.reports.ar.ArRepAgingSummaryDetails;

import jakarta.ejb.*;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Stateless(name = "ArRepAgingSummaryControllerEJB")
public class ArRepAgingSummaryControllerBean extends EJBContextClass implements ArRepAgingSummaryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalArInvoiceBatchHome arInvoiceBatchHome;


    @Override
    public void executeSpArRepAgingSummary(String STORED_PROCEDURE, Date CUT_OFF_DT, boolean INCLUDE_UNPOSTED, String AGING_BY, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("ArRepAgingSummaryControllerBean executeSpArRepAgingSummary");

        try {

            StoredProcedureQuery spQuery = em.createStoredProcedureQuery(STORED_PROCEDURE)
                    .registerStoredProcedureParameter("cuttoffDate", Date.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("includeUnposted", Boolean.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("agingBy", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("adCompany", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("resultCount", Integer.class, ParameterMode.OUT);

            spQuery.setParameter("cuttoffDate", CUT_OFF_DT);
            spQuery.setParameter("includeUnposted", INCLUDE_UNPOSTED);
            spQuery.setParameter("agingBy", AGING_BY);
            spQuery.setParameter("adCompany", AD_CMPNY);

            spQuery.execute();

            Integer resultCount = (Integer) spQuery.getOutputParameterValue("resultCount");

            // IF NO RESULT FOUND
            if (resultCount <= 0) {
                throw new GlobalNoRecordFoundException();
            }
        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeArRepAgingSummarySP(ResultSet rs, HashMap criteria, ArrayList branchList,
                                                String AGNG_BY, String GROUP_BY, String currency, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        final long startTime = System.currentTimeMillis();
        final long endTime;

        Debug.print("ArRepAgingSummaryControllerBean executeArRepAgingSummarySP");

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

            if (includeAdvance.contains("YES") || includeAdvanceOnly.contains("YES")) {

            }

            if (includeAdvanceOnly.contains("NO")) {

                while (rs.next()) {

                    ArRepAgingSummaryDetails mdetails = new ArRepAgingSummaryDetails();

                    String customerCode = rs.getString("CST_CSTMR_CODE");
                    String customerName = rs.getString("CST_NM");
                    String customerClassName = rs.getString("CC_NM");
                    String invoiceCode = rs.getString("INV_CODE");
                    String invoiceDesc = rs.getString("INV_DESC");
                    Date invoiceDate = rs.getDate("INV_DT");
                    String invoiceNumber = rs.getString("INV_NMBR");
                    String customerTypeName = rs.getString("CT_NM");
                    char currencySymbol = rs.getString("FC_SYMBL").charAt(0);
                    double invoiceAmountDue = rs.getDouble("INV_AMNT_DUE");
                    Double amountPaid = rs.getDouble("AMNT_PD");
                    Double totalUnearnedInterest = rs.getDouble("TTL_UE_INT");
                    Double remainingUnearnedInterest = rs.getDouble("RM_UE_INT");
                    Double balanceAmount = rs.getDouble("BALANCE_AMNT");
                    Date lastPayment = rs.getDate("LST_PYMT");

                    String invoiceAgeText = rs.getString("INVOICE_AGE");
                    int invoiceAge = 0;

                    if (!invoiceAgeText.equals("CURRENT")) {
                        if (invoiceAgeText.equals("330 OVER")) {
                            invoiceAge = 331;
                        } else {
                            invoiceAge = Integer.parseInt(invoiceAgeText);
                        }
                    }

                    mdetails.setAgCustomerCode(customerCode);
                    mdetails.setAgCustomerName(customerName);
                    mdetails.setAgCustomerClass(customerClassName);
                    mdetails.setAgDescription(invoiceDesc);

                    if (customerTypeName == null) {

                        mdetails.setAgCustomerType("UNDEFINE");

                    } else {

                        mdetails.setAgCustomerType(customerTypeName);
                    }

                    mdetails.setGroupBy(GROUP_BY);

                    double AMNT_DUE = 0d;
                    AMNT_DUE = balanceAmount;

                    if (includePaid.equals("NO")) {

                    } else {

                    }

                    mdetails.setAgAmount(AMNT_DUE);

                    int INVOICE_AGE = 0;
                    INVOICE_AGE = invoiceAge;

                    switch (AGNG_BY) {
                        case "DUE DATE":

//						INVOICE_AGE = (short) ((agingDate.getTime()
//								- arInvoicePaymentSchedule.getIpsDueDate().getTime()) / (1000 * 60 * 60 * 24));

                            break;
                        case "INVOICE DATE":

//						INVOICE_AGE = (short) ((agingDate.getTime() - arInvoice.getInvDate().getTime())
//								/ (1000 * 60 * 60 * 24));

                            break;
                        case "RECEIVED DATE":

//						INVOICE_AGE = (short) ((agingDate.getTime() - arInvoice.getInvRecieveDate().getTime())
//								/ (1000 * 60 * 60 * 24));

                            break;
                        default:

//						GregorianCalendar calendar = new GregorianCalendar();
//						calendar.setTime(arInvoice.getInvDate());
//						calendar.add(GregorianCalendar.DATE, arInvoice.getArCustomer().getCstEffectivityDays());
//						Date effectivityDate = calendar.getTime();
//
//						INVOICE_AGE = (short) ((agingDate.getTime() - effectivityDate.getTime())
//								/ (1000 * 60 * 60 * 24));
                            break;
                    }

                    if (INVOICE_AGE <= 0) {

                        mdetails.setAgBucket0(AMNT_DUE);

                    } else if (INVOICE_AGE >= 1 && INVOICE_AGE <= agingBucket1) {

                        mdetails.setAgBucket1(AMNT_DUE);

                    } else if (INVOICE_AGE >= (agingBucket1 + 1) && INVOICE_AGE <= agingBucket2) {

                        mdetails.setAgBucket2(AMNT_DUE);

                    } else if (INVOICE_AGE >= (agingBucket2 + 1) && INVOICE_AGE <= agingBucket3) {

                        mdetails.setAgBucket3(AMNT_DUE);

                    } else if (INVOICE_AGE >= (agingBucket3 + 1) && INVOICE_AGE <= agingBucket4) {

                        mdetails.setAgBucket4(AMNT_DUE);

                    } else if (INVOICE_AGE > agingBucket4) {

                        mdetails.setAgBucket5(AMNT_DUE);
                    }

                    mdetails.setAgVouFcSymbol(currencySymbol);

                    list.add(mdetails);
                }

                if (list.isEmpty()) {

                    throw new GlobalNoRecordFoundException();
                }

                return list;
            }

            switch (GROUP_BY) {
                case "CUSTOMER CODE":

                    list.sort(ArRepAgingSummaryDetails.CustomerCodeComparator);

                    break;
                case "CUSTOMER NAME":
                    list.sort(ArRepAgingSummaryDetails.CustomerNameComparator);

                    break;
                case "CUSTOMER TYPE":

                    list.sort(ArRepAgingSummaryDetails.CustomerTypeComparator);

                    break;
                case "CUSTOMER CLASS":

                    list.sort(ArRepAgingSummaryDetails.CustomerClassComparator);
                    break;
            }

            endTime = System.currentTimeMillis();
            long seconds = TimeUnit.MILLISECONDS.toSeconds((endTime - startTime));

            Debug.print("Total execution time of executeArRepAgingSummary in seconds : " + seconds);

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {
        Debug.print("ArRepAgingSummaryControllerBean getAdCompany");
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

    public AdPreferenceDetails getAdPreference(Integer AD_CMPNY) {
        Debug.print("ArRepAgingSummaryControllerBean getAdPreference");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            AdPreferenceDetails details = new AdPreferenceDetails();
            details.setPrfArAgingBucket(adPreference.getPrfArAgingBucket());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArIbAll(Integer AD_BRNCH, Integer AD_CMPNY) {
        Debug.print("ArRepAgingSummaryControllerBean getArIbAll");
        ArrayList list = new ArrayList();
        try {

            Collection arInvoiceBatches = arInvoiceBatchHome.findOpenIbByIbType("INVOICE", AD_BRNCH, AD_CMPNY);

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


    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ApRepAgingSummaryControllerBean ejbCreate");
    }

}