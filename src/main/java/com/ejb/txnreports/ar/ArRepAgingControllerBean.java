/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepAgingControllerBean
 * @created March 12, 2004, 01:49 PM
 * @author Dennis Hilario
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
import com.util.*;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.ad.AdPreferenceDetails;
import com.util.reports.ar.ArRepAgingDetails;

import jakarta.ejb.*;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Stateless(name = "ArRepAgingControllerEJB")
public class ArRepAgingControllerBean extends EJBContextClass implements ArRepAgingController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArAppliedInvoiceHome arAppliedInvoiceHome;
    @EJB
    private LocalCmAdjustmentHome cmAdjustmentHome;
    @EJB
    private LocalArCustomerClassHome arCustomerClassHome;
    @EJB
    private LocalArCustomerTypeHome arCustomerTypeHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;


    @Override
    public void executeSpArRepAging(String STORED_PROCEDURE, Date CUT_OFF_DT, boolean INCLUDE_UNPOSTED, String AGING_BY, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("ArRepAgingControllerBean executeSpArRepAging");

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

    // Execute AR Aging
    public ArrayList executeArRepAging(HashMap criteria, ArrayList branchList, String AGNG_BY, String ORDER_BY,
                                       String GROUP_BY, String currency, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        final long startTime = System.currentTimeMillis();
        final long endTime;

        Debug.print("ArRepAgingControllerBean executeArRepAging");

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

            StringBuilder jbossQl = new StringBuilder();
            StringBuilder jbossQlAdv = new StringBuilder();
            StringBuilder jbossQlAdvPaid = new StringBuilder();
            jbossQl.append("SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE (");
            jbossQlAdv.append("SELECT OBJECT(adj) FROM CmAdjustment adj WHERE (");
            jbossQlAdvPaid.append("SELECT OBJECT(ai) FROM ArAppliedInvoice ai WHERE (");

            if (branchList.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            Iterator brIter = branchList.iterator();

            AdBranchDetails details = (AdBranchDetails) brIter.next();
            jbossQl.append("ips.arInvoice.invAdBranch=").append(details.getBrCode());
            jbossQlAdv.append("adj.adjAdBranch=").append(details.getBrCode());
            jbossQlAdvPaid.append("ai.arReceipt.rctAdBranch=").append(details.getBrCode());

            while (brIter.hasNext()) {
                details = (AdBranchDetails) brIter.next();
                jbossQl.append(" OR ips.arInvoice.invAdBranch=").append(details.getBrCode());
                jbossQlAdv.append(" OR adj.adjAdBranch=").append(details.getBrCode());
                jbossQlAdvPaid.append(" OR ai.arReceipt.rctAdBranch=").append(details.getBrCode());
            }

            jbossQl.append(") ");
            jbossQlAdv.append(") ");
            jbossQlAdvPaid.append(") ");
            firstArgument = false;

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("customerCode")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includeUnpostedTransaction")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includeAdvance")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includeAdvanceOnly")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includePaid")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");
                    jbossQlAdv.append("AND ");
                    jbossQlAdvPaid.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                    jbossQlAdv.append("WHERE ");
                    jbossQlAdvPaid.append("WHERE ");
                }

                jbossQl.append("ips.arInvoice.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
                jbossQlAdv.append("adj.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
                jbossQlAdvPaid.append("ai.arReceipt.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
            }

            if (criteria.containsKey("customerName")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                    jbossQlAdv.append("AND ");
                    jbossQlAdvPaid.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                    jbossQlAdv.append("WHERE ");
                    jbossQlAdvPaid.append("WHERE ");
                }

                jbossQl.append("ips.arInvoice.arCustomer.arCustomerName.ctName=?").append(ctr + 1).append(" ");
                jbossQlAdv.append("adj.arCustomer.arCustomerName.ctName=?").append(ctr + 1).append(" ");
                jbossQlAdvPaid.append("ai.arReceipt.arCustomer.arCustomerName.ctName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerName");
                ctr++;
            }

            if (criteria.containsKey("customerType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                    jbossQlAdv.append("AND ");
                    jbossQlAdvPaid.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                    jbossQlAdv.append("WHERE ");
                    jbossQlAdvPaid.append("WHERE ");
                }

                jbossQl.append("ips.arInvoice.arCustomer.arCustomerType.ctName=?").append(ctr + 1).append(" ");
                jbossQlAdv.append("adj.arCustomer.arCustomerType.ctName=?").append(ctr + 1).append(" ");
                jbossQlAdvPaid.append("ai.arReceipt.arCustomer.arCustomerType.ctName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerType");
                ctr++;
            }

            if (criteria.containsKey("customerBatch")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                    jbossQlAdv.append("AND ");
                    jbossQlAdvPaid.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                    jbossQlAdv.append("WHERE ");
                    jbossQlAdvPaid.append("WHERE ");
                }

                jbossQl.append("ips.arInvoice.arCustomer.cstCustomerBatch=?").append(ctr + 1).append(" ");
                jbossQlAdv.append("adj.arCustomer.cstCustomerBatch=?").append(ctr + 1).append(" ");
                jbossQlAdvPaid.append("ai.arReceipt.arCustomer.cstCustomerBatch=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerBatch");
                ctr++;
            }

            if (criteria.containsKey("customerClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                    jbossQlAdv.append("AND ");
                    jbossQlAdvPaid.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                    jbossQlAdv.append("WHERE ");
                    jbossQlAdvPaid.append("WHERE ");
                }

                jbossQl.append("ips.arInvoice.arCustomer.arCustomerClass.ccName=?").append(ctr + 1).append(" ");
                jbossQlAdv.append("adj.arCustomer.arCustomerClass.ccName=?").append(ctr + 1).append(" ");
                jbossQlAdvPaid.append("ai.arReceipt.arCustomer.arCustomerClass.ccName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerClass");
                ctr++;
            }

            if (criteria.containsKey("date")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                    jbossQlAdv.append("AND ");
                    jbossQlAdvPaid.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                    jbossQlAdv.append("WHERE ");
                    jbossQlAdvPaid.append("WHERE ");
                }
                jbossQl.append("ips.arInvoice.invDate<=?").append(ctr + 1).append(" ");
                jbossQlAdv.append("adj.adjDate<=?").append(ctr + 1).append(" ");
                jbossQlAdvPaid.append("ai.arReceipt.rctDate<=?").append(ctr + 1).append(" ");

                obj[ctr] = criteria.get("date");
                agingDate = (Date) criteria.get("date");
                ctr++;
            }

            if (criteria.containsKey("includeUnpostedTransaction")) {

                String unposted = (String) criteria.get("includeUnpostedTransaction");

                if (!firstArgument) {

                    jbossQl.append("AND ");
                    jbossQlAdv.append("AND ");
                    jbossQlAdvPaid.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                    jbossQlAdv.append("WHERE ");
                    jbossQlAdvPaid.append("WHERE ");
                }

                if (unposted.equals("NO")) {

                    jbossQl.append("ips.arInvoice.invPosted = 1 ");
                    jbossQlAdv.append("adj.adjPosted = 1 ");
                    jbossQlAdvPaid.append("ai.arReceipt.rctPosted = 1 ");

                } else {

                    jbossQl.append("ips.arInvoice.invVoid = 0 ");
                    jbossQlAdv.append("adj.adjVoid = 0 ");
                    jbossQlAdvPaid.append("ai.arReceipt.rctVoid = 0 ");
                }
            }

            if (!firstArgument) {

                jbossQl.append("AND ");
                jbossQlAdv.append("AND ");
                jbossQlAdvPaid.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
                jbossQlAdv.append("WHERE ");
                jbossQlAdvPaid.append("WHERE ");
            }

            jbossQl.append("ips.arInvoice.invCreditMemo = 0 AND ips.ipsAdCompany=").append(AD_CMPNY);
            jbossQlAdv.append("adj.adjType = 'ADVANCE' AND adj.adjAdCompany=").append(AD_CMPNY);
            jbossQlAdvPaid.append("ai.aiCreditBalancePaid > 0 AND ai.arReceipt.rctAdCompany=").append(AD_CMPNY);

            String includePaid = (String) criteria.get("includePaid");

            Debug.print(jbossQl.toString());

            String includeAdvance = (String) criteria.get("includeAdvance");
            String includeAdvanceOnly = (String) criteria.get("includeAdvanceOnly");

            if (includeAdvance.contains("YES") || includeAdvanceOnly.contains("YES")) {

                Collection advancePayments = cmAdjustmentHome.getAdjByCriteria(jbossQlAdv.toString(), obj, 0, 0);

                for (Object advancePayment : advancePayments) {

                    LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) advancePayment;

                    ArRepAgingDetails mdetails = new ArRepAgingDetails();
                    mdetails.setAgTransactionDate(cmAdjustment.getAdjDate());
                    mdetails.setAgInvoiceNumber(cmAdjustment.getAdjDocumentNumber());
                    mdetails.setAgCustomerCode(cmAdjustment.getArCustomer().getCstCustomerCode());
                    mdetails.setAgCustomerName(cmAdjustment.getArCustomer().getCstName());
                    mdetails.setAgCustomerClass(cmAdjustment.getArCustomer().getArCustomerClass().getCcName());
                    mdetails.setAgDescription("ADVANCE");

                    if (cmAdjustment.getArCustomer().getArCustomerType() == null) {
                        mdetails.setAgCustomerType("UNDEFINE");
                    } else {
                        mdetails.setAgCustomerType(cmAdjustment.getArCustomer().getArCustomerType().getCtName());
                    }

                    mdetails.setGroupBy(GROUP_BY);

                    double ADV_AMNT = 0d;

                    if (cmAdjustment.getAdjDate().before(agingDate) || cmAdjustment.getAdjDate().equals(agingDate)) {

                        if (currency.equals("USD") && cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcName().equals("USD")) {

                        } else {

                            Collection arAppliedCredits = cmAdjustment.getArAppliedCredits();
                            Iterator y = arAppliedCredits.iterator();
                            double totalAppliedCredit = 0d;

                            while (y.hasNext()) {
                                LocalArAppliedCredit arAppliedCredit = (LocalArAppliedCredit) y.next();
                                totalAppliedCredit += arAppliedCredit.getAcApplyCredit();
                            }

                            ADV_AMNT = this.convertForeignToFunctionalCurrency(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode(), cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcName(), cmAdjustment.getAdjConversionDate(), cmAdjustment.getAdjConversionRate(), cmAdjustment.getAdjAmount() - totalAppliedCredit - cmAdjustment.getAdjRefundAmount(), AD_CMPNY) * -1;
                        }

                        mdetails.setAgAmount(ADV_AMNT);

                        int INVOICE_AGE = 0;

                        switch (AGNG_BY) {
                            case "DUE DATE":

                                INVOICE_AGE = (short) ((agingDate.getTime() - cmAdjustment.getAdjDate().getTime()) / (1000 * 60 * 60 * 24));

                                break;
                            case "INVOICE DATE":

                                INVOICE_AGE = (short) ((agingDate.getTime() - cmAdjustment.getAdjDate().getTime()) / (1000 * 60 * 60 * 24));

                                break;
                            case "RECEIVED DATE":

                                INVOICE_AGE = (short) ((agingDate.getTime() - cmAdjustment.getAdjDate().getTime()) / (1000 * 60 * 60 * 24));

                                break;
                            default:

                                GregorianCalendar calendar = new GregorianCalendar();
                                calendar.setTime(cmAdjustment.getAdjDate());
                                calendar.add(GregorianCalendar.DATE, cmAdjustment.getArCustomer().getCstEffectivityDays());
                                Date effectivityDate = calendar.getTime();

                                INVOICE_AGE = (short) ((agingDate.getTime() - effectivityDate.getTime()) / (1000 * 60 * 60 * 24));
                                break;
                        }
                        if (INVOICE_AGE <= 0) {

                            mdetails.setAgBucket0(ADV_AMNT);

                        } else if (INVOICE_AGE >= 1 && INVOICE_AGE <= agingBucket1) {

                            mdetails.setAgBucket1(ADV_AMNT);

                        } else if (INVOICE_AGE >= (agingBucket1 + 1) && INVOICE_AGE <= agingBucket2) {

                            mdetails.setAgBucket2(ADV_AMNT);

                        } else if (INVOICE_AGE >= (agingBucket2 + 1) && INVOICE_AGE <= agingBucket3) {

                            mdetails.setAgBucket3(ADV_AMNT);

                        } else if (INVOICE_AGE >= (agingBucket3 + 1) && INVOICE_AGE <= agingBucket4) {

                            mdetails.setAgBucket4(ADV_AMNT);

                        } else if (INVOICE_AGE > agingBucket4) {

                            mdetails.setAgBucket5(ADV_AMNT);
                        }

                        mdetails.setAgVouFcSymbol(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcSymbol());
                        list.add(mdetails);
                    }
                }
            }

            if (includeAdvanceOnly.contains("NO")) {

                Date LATEST_OR_DATE = null;
                boolean isFirst = true;
                String CST_CODE = "";

                Collection arInvoicePaymentSchedules = arInvoicePaymentScheduleHome.getIpsByCriteria(jbossQl.toString(), obj);

                if (arInvoicePaymentSchedules.size() == 0) {
                    throw new GlobalNoRecordFoundException();
                }

                for (Object invoicePaymentSchedule : arInvoicePaymentSchedules) {

                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) invoicePaymentSchedule;

                    LocalArInvoice arInvoice = arInvoicePaymentSchedule.getArInvoice();
                    if (!CST_CODE.equals(arInvoice.getArCustomer().getCstCustomerCode())) {
                        LATEST_OR_DATE = null;
                        isFirst = true;
                    }

                    ArRepAgingDetails mdetails = new ArRepAgingDetails();
                    mdetails.setAgLastOrDate(LATEST_OR_DATE == null ? arInvoice.getInvDate() : LATEST_OR_DATE);

                    int DATE_DIFF = (int) ChronoUnit.DAYS.between(LATEST_OR_DATE == null ? EJBCommon.convertLocalDateObject(arInvoice.getInvDate()) : EJBCommon.convertLocalDateObject(LATEST_OR_DATE), EJBCommon.convertLocalDateObject(agingDate));

                    CST_CODE = arInvoice.getArCustomer().getCstCustomerCode();
                    mdetails.setAgAgedInMonth(DATE_DIFF);

                    mdetails.setAgCustomerName(arInvoice.getArCustomer().getCstName());
                    mdetails.setAgCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
                    mdetails.setAgInvoiceNumber(arInvoice.getInvNumber());
                    mdetails.setAgReferenceNumber(arInvoice.getInvReferenceNumber());
                    mdetails.setAgInstallmentNumber(arInvoicePaymentSchedule.getIpsNumber());
                    mdetails.setAgInstallmentAmount(arInvoicePaymentSchedule.getIpsAmountDue());
                    mdetails.setAgTransactionDate(arInvoice.getInvDate());
                    mdetails.setAgIpsDueDate(arInvoicePaymentSchedule.getIpsDueDate());
                    mdetails.setAgCustomerClass(arInvoice.getArCustomer().getArCustomerClass().getCcName());
                    mdetails.setAgDescription(arInvoice.getInvDescription());

                    try {

                        mdetails.setAgSalesPerson(arInvoice.getArSalesperson().getSlpSalespersonCode());

                    }
                    catch (Exception e) {
                        mdetails.setAgSalesPerson("");
                    }

                    if (arInvoice.getArCustomer().getArCustomerType() == null) {

                        mdetails.setAgCustomerType("UNDEFINE");

                    } else {

                        mdetails.setAgCustomerType(arInvoice.getArCustomer().getArCustomerType().getCtName());
                    }

                    mdetails.setOrderBy(ORDER_BY);
                    mdetails.setGroupBy(GROUP_BY);

                    double AMNT_DUE = 0d;

                    if (includePaid.equals("NO")) {

                        // Get Credit Memos
                        double CM_AMNT = getCreditMemo(AD_CMPNY, agingDate, precisionUnit, arInvoicePaymentSchedule, arInvoice);

                        // Get Receipts
                        double RCPT_AMNT = 0d;

                        Collection arAppliedInvoices = arAppliedInvoiceHome.findPostedAiByIpsCode(arInvoicePaymentSchedule.getIpsCode(), AD_CMPNY);

                        for (Object appliedInvoice : arAppliedInvoices) {

                            LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoice;

                            if (isFirst) {

                                LATEST_OR_DATE = arAppliedInvoice.getArReceipt().getRctDate();
                                isFirst = false;

                            } else {

                                LATEST_OR_DATE = arAppliedInvoice.getArReceipt().getRctDate().after(LATEST_OR_DATE) ? arAppliedInvoice.getArReceipt().getRctDate() : LATEST_OR_DATE;
                            }

                            mdetails.setAgLastOrDate(LATEST_OR_DATE);

                            DATE_DIFF = (int) ChronoUnit.DAYS.between(EJBCommon.convertLocalDateObject(LATEST_OR_DATE), EJBCommon.convertLocalDateObject(agingDate));

                            mdetails.setAgAgedInMonth(DATE_DIFF);

                            if (arAppliedInvoice.getArReceipt().getRctDate().after(agingDate)) {

                                RCPT_AMNT += arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditBalancePaid() + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiCreditableWTax();
                            }
                        }

                        if (currency.equals("USD") && arInvoice.getGlFunctionalCurrency().getFcName().equals("USD")) {

                            AMNT_DUE = arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid() + CM_AMNT + RCPT_AMNT;

                        } else {

                            AMNT_DUE = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid() + CM_AMNT + RCPT_AMNT, AD_CMPNY);
                        }

                        if (AMNT_DUE == 0) {
                            continue;
                        }

                    } else {

                        if (currency.equals("USD") && arInvoice.getGlFunctionalCurrency().getFcName().equals("USD")) {

                            AMNT_DUE = arInvoicePaymentSchedule.getIpsAmountDue();

                        } else {

                            AMNT_DUE = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arInvoicePaymentSchedule.getIpsAmountDue(), AD_CMPNY);
                        }
                    }

                    mdetails.setAgAmount(AMNT_DUE);

                    int INVOICE_AGE = 0;

                    switch (AGNG_BY) {
                        case "DUE DATE":

                            INVOICE_AGE = (short) ((agingDate.getTime() - arInvoicePaymentSchedule.getIpsDueDate().getTime()) / (1000 * 60 * 60 * 24));

                            break;
                        case "INVOICE DATE":

                            INVOICE_AGE = (short) ((agingDate.getTime() - arInvoice.getInvDate().getTime()) / (1000 * 60 * 60 * 24));

                            break;
                        case "RECEIVED DATE":

                            INVOICE_AGE = (short) ((agingDate.getTime() - arInvoice.getInvReceiveDate().getTime()) / (1000 * 60 * 60 * 24));

                            break;
                        default:

                            GregorianCalendar calendar = new GregorianCalendar();
                            calendar.setTime(arInvoice.getInvDate());
                            calendar.add(GregorianCalendar.DATE, arInvoice.getArCustomer().getCstEffectivityDays());
                            Date effectivityDate = calendar.getTime();

                            INVOICE_AGE = (short) ((agingDate.getTime() - effectivityDate.getTime()) / (1000 * 60 * 60 * 24));
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

                    mdetails.setAgInvoiceAge(INVOICE_AGE);

                    mdetails.setAgVouFcSymbol(arInvoice.getGlFunctionalCurrency().getFcSymbol());
                    list.add(mdetails);

                    if (includePaid.equals("YES") && arInvoicePaymentSchedule.getIpsAmountPaid() != 0) {

                        // get cm on or after aging date

                        double CM_AMNT = 0d;

                        Collection arCreditMemos = arInvoiceHome
                                .findByInvCreditMemoAndInvCmInvoiceNumberAndInvVoidAndInvPosted(
                                        EJBCommon.TRUE, arInvoice.getInvNumber(),
                                        EJBCommon.FALSE, EJBCommon.TRUE, AD_CMPNY);

                        for (Object creditMemo : arCreditMemos) {

                            LocalArInvoice arCreditMemo = (LocalArInvoice) creditMemo;

                            mdetails = new ArRepAgingDetails();

                            mdetails.setAgCustomerName(arInvoice.getArCustomer().getCstName());
                            mdetails.setAgCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
                            mdetails.setAgInvoiceNumber(arCreditMemo.getInvNumber());
                            mdetails.setAgReferenceNumber(arCreditMemo.getInvCmInvoiceNumber());
                            mdetails.setAgInstallmentNumber((short) 0);
                            mdetails.setAgTransactionDate(arCreditMemo.getInvDate());
                            mdetails.setAgIpsDueDate(arInvoicePaymentSchedule.getIpsDueDate());
                            mdetails.setAgCustomerClass(arInvoice.getArCustomer().getArCustomerClass().getCcName());
                            mdetails.setAgDescription(arInvoice.getInvDescription());
                            try {
                                mdetails.setAgSalesPerson(arInvoice.getArSalesperson().getSlpSalespersonCode());
                            }
                            catch (Exception e) {
                                mdetails.setAgSalesPerson("");
                            }
                            if (arInvoice.getArCustomer().getArCustomerType() == null) {

                                mdetails.setAgCustomerType("UNDEFINE");

                            } else {

                                mdetails.setAgCustomerType(arInvoice.getArCustomer().getArCustomerType().getCtName());
                            }
                            mdetails.setOrderBy(ORDER_BY);
                            mdetails.setGroupBy(GROUP_BY);

                            if (arCreditMemo.getInvDate().before(agingDate) || arCreditMemo.getInvDate().equals(agingDate)) {
                                if (currency.equals("USD") && arInvoice.getGlFunctionalCurrency().getFcName().equals("USD")) {

                                    CM_AMNT = (EJBCommon.roundIt(arCreditMemo.getInvAmountDue() * (arInvoicePaymentSchedule.getIpsAmountDue() / arInvoice.getInvAmountDue()), precisionUnit)) * -1;

                                } else {
                                    CM_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), EJBCommon.roundIt(arCreditMemo.getInvAmountDue() * (arInvoicePaymentSchedule.getIpsAmountDue() / arInvoice.getInvAmountDue()), precisionUnit), AD_CMPNY) * -1;
                                }

                                mdetails.setAgAmount(CM_AMNT);

                                if (INVOICE_AGE <= 0) {

                                    mdetails.setAgBucket0(CM_AMNT);
                                }
                                if (INVOICE_AGE >= 1 && INVOICE_AGE <= agingBucket1) {

                                    mdetails.setAgBucket1(CM_AMNT);

                                } else if (INVOICE_AGE >= (agingBucket1 + 1) && INVOICE_AGE <= agingBucket2) {

                                    mdetails.setAgBucket2(CM_AMNT);

                                } else if (INVOICE_AGE >= (agingBucket2 + 1) && INVOICE_AGE <= agingBucket3) {

                                    mdetails.setAgBucket3(CM_AMNT);

                                } else if (INVOICE_AGE >= (agingBucket3 + 1) && INVOICE_AGE <= agingBucket4) {

                                    mdetails.setAgBucket4(CM_AMNT);

                                } else if (INVOICE_AGE > agingBucket4) {

                                    mdetails.setAgBucket5(CM_AMNT);
                                }
                                mdetails.setAgVouFcSymbol(arInvoice.getGlFunctionalCurrency().getFcSymbol());
                                list.add(mdetails);
                            }
                        }

                        // get future receipts

                        double RCPT_AMNT = 0d;

                        Collection arAppliedInvoices = arAppliedInvoiceHome.findPostedAiByIpsCode(arInvoicePaymentSchedule.getIpsCode(), AD_CMPNY);

                        for (Object appliedInvoice : arAppliedInvoices) {

                            LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoice;

                            mdetails = new ArRepAgingDetails();

                            if (isFirst) {

                                LATEST_OR_DATE = arAppliedInvoice.getArReceipt().getRctDate();
                                isFirst = false;
                            } else {

                                LATEST_OR_DATE = arAppliedInvoice.getArReceipt().getRctDate().after(LATEST_OR_DATE) ? arAppliedInvoice.getArReceipt().getRctDate() : LATEST_OR_DATE;
                            }

                            mdetails.setAgLastOrDate(LATEST_OR_DATE);

                            DATE_DIFF = (int) ChronoUnit.DAYS.between(EJBCommon.convertLocalDateObject(LATEST_OR_DATE), EJBCommon.convertLocalDateObject(agingDate));

                            mdetails.setAgAgedInMonth(DATE_DIFF);

                            mdetails.setAgCustomerName(arInvoice.getArCustomer().getCstName());
                            mdetails.setAgCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
                            mdetails.setAgInvoiceNumber(arAppliedInvoice.getArReceipt().getRctNumber());
                            mdetails.setAgReferenceNumber(arAppliedInvoice.getArInvoicePaymentSchedule().getArInvoice().getInvNumber());
                            mdetails.setAgInstallmentNumber((short) 0);
                            mdetails.setAgTransactionDate(arAppliedInvoice.getArReceipt().getRctDate());
                            mdetails.setAgIpsDueDate(arInvoicePaymentSchedule.getIpsDueDate());
                            mdetails.setAgCustomerClass(arInvoice.getArCustomer().getArCustomerClass().getCcName());
                            mdetails.setAgDescription(arInvoice.getInvDescription());

                            try {

                                mdetails.setAgSalesPerson(arInvoice.getArSalesperson().getSlpSalespersonCode());

                            }
                            catch (Exception e) {
                                mdetails.setAgSalesPerson("");
                            }

                            if (arInvoice.getArCustomer().getArCustomerType() == null) {

                                mdetails.setAgCustomerType("UNDEFINE");

                            } else {

                                mdetails.setAgCustomerType(arInvoice.getArCustomer().getArCustomerType().getCtName());
                            }
                            mdetails.setOrderBy(ORDER_BY);
                            mdetails.setGroupBy(GROUP_BY);

                            if (arAppliedInvoice.getArReceipt().getRctDate().before(agingDate) || arAppliedInvoice.getArReceipt().getRctDate().equals(agingDate)) {

                                if (currency.equals("USD") && arInvoice.getGlFunctionalCurrency().getFcName().equals("USD")) {

                                    RCPT_AMNT = (arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditBalancePaid() + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiCreditableWTax()) * -1;

                                } else {
                                    RCPT_AMNT = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiCreditBalancePaid() + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiCreditableWTax(), AD_CMPNY) * -1;
                                }

                                mdetails.setAgAmount(RCPT_AMNT);

                                if (INVOICE_AGE <= 0) {

                                    mdetails.setAgBucket0(RCPT_AMNT);

                                } else if (INVOICE_AGE >= 1 && INVOICE_AGE <= agingBucket1) {

                                    mdetails.setAgBucket1(RCPT_AMNT);

                                } else if (INVOICE_AGE >= (agingBucket1 + 1) && INVOICE_AGE <= agingBucket2) {

                                    mdetails.setAgBucket2(RCPT_AMNT);

                                } else if (INVOICE_AGE >= (agingBucket2 + 1) && INVOICE_AGE <= agingBucket3) {

                                    mdetails.setAgBucket3(RCPT_AMNT);

                                } else if (INVOICE_AGE >= (agingBucket3 + 1) && INVOICE_AGE <= agingBucket4) {

                                    mdetails.setAgBucket4(RCPT_AMNT);

                                } else if (INVOICE_AGE > agingBucket4) {

                                    mdetails.setAgBucket5(RCPT_AMNT);
                                }

                                mdetails.setAgVouFcSymbol(arInvoice.getGlFunctionalCurrency().getFcSymbol());

                                list.add(mdetails);
                            }
                        }
                    }
                }

                if (list.isEmpty()) {

                    throw new GlobalNoRecordFoundException();
                }
            }

            switch (GROUP_BY) {
                case "CUSTOMER CODE":

                    list.sort(ArRepAgingDetails.CustomerCodeComparator);

                    break;
                case "CUSTOMER NAME":

                    list.sort(ArRepAgingDetails.CustomerNameComparator);

                    break;
                case "CUSTOMER TYPE":

                    list.sort(ArRepAgingDetails.CustomerTypeComparator);

                    break;
                case "CUSTOMER CLASS":

                    list.sort(ArRepAgingDetails.CustomerClassComparator);

                    break;
                case "SAR":

                    list.sort(ArRepAgingDetails.SARComparator);

                    break;
                default:

                    list.sort(ArRepAgingDetails.NoClassComparator);
                    break;
            }

            endTime = System.currentTimeMillis();
            long seconds = TimeUnit.MILLISECONDS.toSeconds((endTime - startTime));
            Debug.print("Total execution time of executeArRepAging in seconds : " + seconds);
            return list;
        }
        catch (GlobalNoRecordFoundException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    private double getCreditMemo(Integer AD_CMPNY, Date agingDate, short precisionUnit,
                                 LocalArInvoicePaymentSchedule arInvoicePaymentSchedule,
                                 LocalArInvoice arInvoice) throws FinderException { // Get Credit Memos
        double CM_AMNT = 0d;
        Collection arCreditMemos = arInvoiceHome.findByInvCreditMemoAndInvCmInvoiceNumberAndInvVoidAndInvPosted(
                EJBCommon.TRUE, arInvoice.getInvNumber(), EJBCommon.FALSE, EJBCommon.TRUE, AD_CMPNY);
        for (Object creditMemo : arCreditMemos) {
            LocalArInvoice arCreditMemo = (LocalArInvoice) creditMemo;
            if (arCreditMemo.getInvDate().after(agingDate)) {
                CM_AMNT += EJBCommon.roundIt(arCreditMemo.getInvAmountDue() *
                        (arInvoicePaymentSchedule.getIpsAmountDue() / arInvoice.getInvAmountDue()), precisionUnit);
            }
        }
        return CM_AMNT;
    }


    public void executeSpArRepAging(String STORED_PROCEDURE, String GL_ACCNT_NMBR_FRM, String GL_ACCNT_NMBR_TO,
                                    Date DT_FRM, Date DT_TO, String BRANCH_CODES,
                                    String AMOUNT_TYP, boolean INCLUDE_UNPOSTED, boolean INCLUDE_UNPOSTED_SL,
                                    boolean SHOW_ZEROES, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("ArRepAgingControllerBean executeSpArRepAging");

        try {

            StoredProcedureQuery spQuery = em.createStoredProcedureQuery(STORED_PROCEDURE)
                    .registerStoredProcedureParameter("accountFrom", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("accountTo", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("dateFrom", Date.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("dateTo", Date.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("branchCode", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("amountType", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("includeUnposted", Boolean.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("includeUnpostedSl", Boolean.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("showZeroes", Boolean.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("adCompany", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("resultCount", Integer.class, ParameterMode.OUT);

            spQuery.setParameter("accountFrom", GL_ACCNT_NMBR_FRM);
            spQuery.setParameter("accountTo", GL_ACCNT_NMBR_TO);
            spQuery.setParameter("dateFrom", DT_FRM);
            spQuery.setParameter("dateTo", DT_TO);
            spQuery.setParameter("branchCode", BRANCH_CODES);
            spQuery.setParameter("amountType", AMOUNT_TYP);
            spQuery.setParameter("includeUnposted", INCLUDE_UNPOSTED);
            spQuery.setParameter("includeUnpostedSl", INCLUDE_UNPOSTED_SL);
            spQuery.setParameter("showZeroes", SHOW_ZEROES);
            spQuery.setParameter("adCompany", AD_CMPNY);

            spQuery.execute();

            Integer resultCount = (Integer) spQuery.getOutputParameterValue("resultCount");
            if (resultCount <= 0) {
                throw new GlobalNoRecordFoundException();
            }
        }
        catch (GlobalNoRecordFoundException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ArRepAgingControllerBean getAdCompany");

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
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public AdPreferenceDetails getAdPreference(Integer AD_CMPNY) {
        Debug.print("ArRepAgingControllerBean getAdPreference");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            AdPreferenceDetails details = new AdPreferenceDetails();
            details.setPrfApAgingBucket(adPreference.getPrfApAgingBucket());
            details.setPrfArAgingBucket(adPreference.getPrfArAgingBucket());
            return details;
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE,
                                                      double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {
        Debug.print("ArRepAgingControllerBean convertForeignToFunctionalCurrency");
        LocalAdCompany adCompany;

        // get company and extended precision
        try {
            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
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

    // SessionBean methods
    public void ejbCreate() throws CreateException {
        Debug.print("ArRepAgingControllerBean ejbCreate");
    }

}