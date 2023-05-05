/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmRepDailyCashForecastSummaryControllerBean
 * @created January 25, 2006 3:58 PM
 * @author Farrah S. Garing
 */
package com.ejb.txnreports.cm;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBankAccountBalanceHome;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdBankAccountBalance;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ap.LocalApAppliedVoucher;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.entities.ap.LocalApVoucherPaymentSchedule;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.entities.ar.LocalArInvoicePaymentSchedule;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdCompanyDetails;
import com.util.reports.cm.CmRepDailyCashForecastSummaryAddDetails;
import com.util.reports.cm.CmRepDailyCashForecastSummaryDetails;
import com.util.reports.cm.CmRepDailyCashForecastSummaryLessDetails;
import jakarta.ejb.*;

import java.util.*;

;

@Stateless(name = "CmRepDailyCashForecastSummaryControllerEJB")
public class CmRepDailyCashForecastSummaryControllerBean extends EJBContextClass
        implements CmRepDailyCashForecastSummaryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;


    public ArrayList getAdBaAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashForecastSummaryControllerBean getAdBaAll");

        LocalAdBankAccount adBankAccount = null;

        ArrayList list = new ArrayList();

        try {

            Collection adBankAccounts = adBankAccountHome.findEnabledBaAll(AD_BRNCH, AD_CMPNY);

            for (Object bankAccount : adBankAccounts) {

                adBankAccount = (LocalAdBankAccount) bankAccount;

                list.add(adBankAccount.getBaName());
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public CmRepDailyCashForecastSummaryDetails executeCmRepDailyCashForecastSummary(HashMap criteria, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmRepDailyCashForecastSummaryControllerBean executeCmRepDailyCashForecastSummary");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            String[] selectedBankAccounts = (String[]) criteria.get("selectedBankAccount");
            Integer[] selectedBankAccountCodes = new Integer[selectedBankAccounts.length];

            for (int i = 0; i < selectedBankAccounts.length; i++) {

                LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(selectedBankAccounts[i], AD_CMPNY);
                selectedBankAccountCodes[i] = adBankAccount.getBaCode();
            }

            Date date = null;

            if (criteria.containsKey("date")) {

                date = (Date) criteria.get("date");
            }

            String reportType = null;

            if (criteria.containsKey("reportType")) {

                reportType = (String) criteria.get("reportType");
            }

            CmRepDailyCashForecastSummaryDetails mdetails = new CmRepDailyCashForecastSummaryDetails();

            if (reportType.equals("FIVE DAYS")) {

                mdetails.setDcfsDate5(date);
                mdetails.setDcfsDate4(computeDate(mdetails.getDcfsDate5()));
                mdetails.setDcfsDate3(computeDate(mdetails.getDcfsDate4()));
                mdetails.setDcfsDate2(computeDate(mdetails.getDcfsDate3()));
                mdetails.setDcfsDate1(computeDate(mdetails.getDcfsDate2()));

                criteria.put("dateTo", mdetails.getDcfsDate5());

            } else if (reportType.equals("SEVEN DAYS")) {

                mdetails.setDcfsDate7(date);
                mdetails.setDcfsDate6(computeDate(date));
                mdetails.setDcfsDate5(computeDate(mdetails.getDcfsDate6()));
                mdetails.setDcfsDate4(computeDate(mdetails.getDcfsDate5()));
                mdetails.setDcfsDate3(computeDate(mdetails.getDcfsDate4()));
                mdetails.setDcfsDate2(computeDate(mdetails.getDcfsDate3()));
                mdetails.setDcfsDate1(computeDate(mdetails.getDcfsDate2()));
                criteria.put("dateTo", mdetails.getDcfsDate7());
            }

            criteria.put("dateFrom", mdetails.getDcfsDate1());

            mdetails.setDcfsAddList(executeCmRepDailyCashForecastSummaryAdd(criteria, mdetails, AD_CMPNY));
            mdetails.setDcfsLessList(executeCmRepDailyCashForecastSummaryLess(criteria, mdetails, AD_CMPNY));

            if (mdetails.getDcfsAddList().isEmpty() && mdetails.getDcfsLessList().isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            double TTL_ACCNT_BLNC = 0d;

            for (Integer selectedBankAccountCode : selectedBankAccountCodes) {

                Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeDateAndBaCodeAndType(mdetails.getDcfsDate1(), selectedBankAccountCode, "BOOK", AD_CMPNY);

                LocalAdBankAccountBalance adBankAccountBalance = null;

                if (!adBankAccountBalances.isEmpty()) {

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;
                    }
                }

                TTL_ACCNT_BLNC = TTL_ACCNT_BLNC + (adBankAccountBalance != null ? adBankAccountBalance.getBabBalance() : 0);
            }

            mdetails.setDcfsBeginningBalance1(TTL_ACCNT_BLNC);

            mdetails = computeBeginningBalancesAndAvailableCashBalance(mdetails, AD_CMPNY);

            return mdetails;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashForecastSummaryControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());

            return details;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashForecastSummaryControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

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

    private Date computeDate(Date dateEntered) {

        Debug.print("CmRepDailyCashForecastSummaryControllerBean computeDate");

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateEntered);
        calendar.add(GregorianCalendar.DATE, -1);

        return calendar.getTime();
    }

    private ArrayList executeCmRepDailyCashForecastSummaryAdd(HashMap criteria, CmRepDailyCashForecastSummaryDetails details, Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashForecastSummaryControllerBean executeCmRepDailyCashForecastSummaryAdd");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            StringBuilder jbossQl = new StringBuilder();

            // get all invoice

            jbossQl.append("SELECT OBJECT(inv) FROM ArInvoice inv ");

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (criteria.containsKey("selectedBankAccount")) {

                criteriaSize--;
            }

            if (criteria.containsKey("date")) {

                criteriaSize--;
            }

            if (criteria.containsKey("reportType")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("bankAccount")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("inv.adBankAccount.baName=?").append(ctr + 1).append(" ");

                obj[ctr] = criteria.get("bankAccount");

                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (criteria.containsKey("dateTo")) {

                    jbossQl.append("inv.invDate>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");

                    ctr++;

                } else {

                    jbossQl.append("inv.invDate=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");
                    ctr++;
                }
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

            if (criteria.containsKey("includedUnposted")) {

                String unposted = (String) criteria.get("includedUnposted");

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (unposted.equals("NO")) {

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

            jbossQl.append(" inv.invCreditMemo = 0 AND inv.invAmountDue > inv.invAmountPaid AND inv.invAdCompany = ").append(AD_CMPNY).append(" ");

            Collection dcfas = arInvoiceHome.getInvByCriteria(jbossQl.toString(), obj, 0, 0);
            Iterator i = dcfas.iterator();
            ArrayList dcfaList = new ArrayList();
            CmRepDailyCashForecastSummaryAddDetails addDetails = new CmRepDailyCashForecastSummaryAddDetails();

            while (i.hasNext()) {

                LocalArInvoice arInvoice = (LocalArInvoice) i.next();

                addDetails.setDcfsaDescription("INVOICE");

                String reportType = (String) criteria.get("reportType");

                for (Object o : arInvoice.getArInvoicePaymentSchedules()) {

                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) o;

                    if (arInvoicePaymentSchedule.getIpsDueDate().equals(details.getDcfsDate1())) {

                        addDetails.setDcfsaAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getDcfsaAmount1() + arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(details.getDcfsDate2())) {

                        addDetails.setDcfsaAmount2(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getDcfsaAmount2() + arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(details.getDcfsDate3())) {

                        addDetails.setDcfsaAmount3(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getDcfsaAmount3() + arInvoice.getInvAmountDue() - arInvoice.getInvAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(details.getDcfsDate4())) {

                        addDetails.setDcfsaAmount4(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getDcfsaAmount4() + arInvoice.getInvAmountDue() - arInvoice.getInvAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(details.getDcfsDate5())) {

                        addDetails.setDcfsaAmount5(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getDcfsaAmount5() + arInvoice.getInvAmountDue() - arInvoice.getInvAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(details.getDcfsDate6())) {

                        addDetails.setDcfsaAmount6(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getDcfsaAmount6() + arInvoice.getInvAmountDue() - arInvoice.getInvAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(details.getDcfsDate7())) {

                        addDetails.setDcfsaAmount7(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getDcfsaAmount7() + arInvoice.getInvAmountDue() - arInvoice.getInvAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                    }
                }
            }

            if (addDetails.getDcfsaDescription() != null) {

                dcfaList.add(addDetails);
            }

            return dcfaList;

        }
        catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private ArrayList executeCmRepDailyCashForecastSummaryLess(HashMap criteria, CmRepDailyCashForecastSummaryDetails details, Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashForecastSummaryControllerBean executeCmRepDailyCashForecastSummaryLess");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            StringBuilder jbossQl = new StringBuilder();

            // get all invoice

            jbossQl.append("SELECT OBJECT(vou) FROM ApVoucher vou ");

            firstArgument = true;

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (criteria.containsKey("selectedBankAccount")) {

                criteriaSize--;
            }

            if (criteria.containsKey("date")) {

                criteriaSize--;
            }

            if (criteria.containsKey("reportType")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("bankAccount")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.adBankAccount.baName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("bankAccount");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (criteria.containsKey("dateTo")) {

                    jbossQl.append("vou.vouDate>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");
                    ctr++;

                } else {

                    jbossQl.append("vou.vouDate=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");
                    ctr++;
                }
            }

            if (criteria.containsKey("dateTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.vouDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            String unposted = null;

            if (criteria.containsKey("includedUnposted")) {

                unposted = (String) criteria.get("includedUnposted");

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (unposted.equals("NO")) {

                    jbossQl.append("vou.vouPosted = 1 ");

                } else {

                    jbossQl.append("vou.vouVoid = 0 ");
                }
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(" vou.vouDebitMemo = 0 AND vou.vouAmountDue > vou.vouAmountPaid AND vou.vouAdCompany = ").append(AD_CMPNY).append(" ");

            Collection dcfls = apVoucherHome.getVouByCriteria(jbossQl.toString(), obj, 0, 0);
            Iterator i = dcfls.iterator();
            ArrayList dcflList = new ArrayList();
            CmRepDailyCashForecastSummaryLessDetails lessDetails = new CmRepDailyCashForecastSummaryLessDetails();

            String reportType = (String) criteria.get("reportType");

            while (i.hasNext()) {

                LocalApVoucher apVoucher = (LocalApVoucher) i.next();
                lessDetails.setDcfslDescription("VOUCHER");

                double UN_PSTD_CHCKS = 0d;

                Collection apVoucherPaymentSchedules = apVoucher.getApVoucherPaymentSchedules();

                for (Object voucherPaymentSchedule : apVoucherPaymentSchedules) {

                    LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) voucherPaymentSchedule;

                    if (!unposted.equals("NO")) {
                        Collection arAppliedVoucher = apVoucherPaymentSchedule.getApAppliedVouchers();

                        for (Object o : arAppliedVoucher) {

                            LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) o;

                            if (apAppliedVoucher.getApCheck().getChkPosted() == EJBCommon.FALSE) {

                                UN_PSTD_CHCKS = UN_PSTD_CHCKS + apAppliedVoucher.getAvApplyAmount() + apAppliedVoucher.getAvTaxWithheld() + apAppliedVoucher.getAvDiscountAmount();
                            }
                        }
                    }

                    if (apVoucherPaymentSchedule.getVpsDueDate().equals(details.getDcfsDate1())) {

                        lessDetails.setDcfslAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getDcfslAmount1() + apVoucher.getVouAmountDue() - apVoucher.getVouAmountPaid() - UN_PSTD_CHCKS, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(details.getDcfsDate2())) {

                        lessDetails.setDcfslAmount2(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getDcfslAmount2() + apVoucher.getVouAmountDue() - apVoucher.getVouAmountPaid() - UN_PSTD_CHCKS, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(details.getDcfsDate3())) {

                        lessDetails.setDcfslAmount3(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getDcfslAmount3() + apVoucher.getVouAmountDue() - apVoucher.getVouAmountPaid() - UN_PSTD_CHCKS, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(details.getDcfsDate4())) {

                        lessDetails.setDcfslAmount4(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getDcfslAmount4() + apVoucher.getVouAmountDue() - apVoucher.getVouAmountPaid() - UN_PSTD_CHCKS, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(details.getDcfsDate5())) {

                        lessDetails.setDcfslAmount5(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getDcfslAmount5() + apVoucher.getVouAmountDue() - apVoucher.getVouAmountPaid() - UN_PSTD_CHCKS, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(details.getDcfsDate6())) {

                        lessDetails.setDcfslAmount6(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getDcfslAmount6() + apVoucher.getVouAmountDue() - apVoucher.getVouAmountPaid() - UN_PSTD_CHCKS, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(details.getDcfsDate7())) {

                        lessDetails.setDcfslAmount7(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getDcfslAmount7() + apVoucher.getVouAmountDue() - apVoucher.getVouAmountPaid() - UN_PSTD_CHCKS, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                    }
                }
            }

            if (lessDetails.getDcfslDescription() != null || (lessDetails.getDcfslAmount1() != 0 && lessDetails.getDcfslAmount2() != 0 && lessDetails.getDcfslAmount3() != 0 && lessDetails.getDcfslAmount4() != 0 && lessDetails.getDcfslAmount5() != 0 && lessDetails.getDcfslAmount6() != 0 && lessDetails.getDcfslAmount7() != 0)) {

                dcflList.add(lessDetails);
            }

            // get unreleased check

            firstArgument = true;
            ctr = 0;
            criteriaSize = criteria.size();

            jbossQl = new StringBuilder();

            jbossQl.append("SELECT OBJECT(chk) FROM ApCheck chk ");

            firstArgument = true;

            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (criteria.containsKey("selectedBankAccount")) {

                criteriaSize--;
            }

            if (criteria.containsKey("date")) {

                criteriaSize--;
            }

            if (criteria.containsKey("reportType")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("selectedBankAccount")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.adBankAccount.baName in (");

                boolean firstLoop = true;
                String[] adBankAccounts = (String[]) criteria.get("selectedBankAccount");

                for (String adBankAccount : adBankAccounts) {

                    if (adBankAccount != null) {

                        if (firstLoop == false) {

                            jbossQl.append(", ");

                        } else {

                            firstLoop = false;
                        }

                        jbossQl.append("'").append(adBankAccount).append("'");
                    }
                }

                jbossQl.append(") ");
                firstArgument = false;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (criteria.containsKey("dateTo")) {

                    jbossQl.append("chk.chkCheckDate>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");
                    ctr++;

                } else {

                    jbossQl.append("chk.chkCheckDate=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");
                    ctr++;
                }
            }

            if (criteria.containsKey("dateTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkCheckDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("includedUnposted")) {

                unposted = (String) criteria.get("includedUnposted");

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (unposted.equals("NO")) {

                    jbossQl.append("((chk.chkPosted = 1 AND chk.chkVoid = 0) OR (chk.chkPosted = 1 AND chk.chkVoid = 1 AND chk.chkVoidPosted = 0)) ");

                } else {

                    jbossQl.append("chk.chkVoid = 0 ");
                }
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(" chk.chkReleased = 0 AND chk.chkAdCompany = ").append(AD_CMPNY).append(" ");

            dcfls = apCheckHome.getChkByCriteria(jbossQl.toString(), obj, 0, 0);
            i = dcfls.iterator();
            lessDetails = new CmRepDailyCashForecastSummaryLessDetails();

            while (i.hasNext()) {

                LocalApCheck apCheck = (LocalApCheck) i.next();

                lessDetails.setDcfslDescription("UNRELEASED CHECKS");

                lessDetails.setDcfslAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), lessDetails.getDcfslAmount1() + apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
            }

            if (lessDetails.getDcfslDescription() != null) {

                dcflList.add(lessDetails);
            }

            return dcflList;

        }
        catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private CmRepDailyCashForecastSummaryDetails computeBeginningBalancesAndAvailableCashBalance(CmRepDailyCashForecastSummaryDetails details, Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashForecastSummaryControllerBean computeBeginningBalancesAndAvailableCashBalance");

        LocalAdCompany adCompany = null;

        double TOTAL_AMOUNT1 = 0;
        double TOTAL_AMOUNT2 = 0;
        double TOTAL_AMOUNT3 = 0;
        double TOTAL_AMOUNT4 = 0;
        double TOTAL_AMOUNT5 = 0;
        double TOTAL_AMOUNT6 = 0;
        double TOTAL_AMOUNT7 = 0;

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        }
        catch (FinderException ex) {

            throw new EJBException(ex.getMessage());
        }

        ArrayList list = details.getDcfsAddList();
        Iterator i = list.iterator();

        while (i.hasNext()) {

            CmRepDailyCashForecastSummaryAddDetails addDetails = (CmRepDailyCashForecastSummaryAddDetails) i.next();

            TOTAL_AMOUNT1 = TOTAL_AMOUNT1 + addDetails.getDcfsaAmount1();
            TOTAL_AMOUNT2 = TOTAL_AMOUNT2 + addDetails.getDcfsaAmount2();
            TOTAL_AMOUNT3 = TOTAL_AMOUNT3 + addDetails.getDcfsaAmount3();
            TOTAL_AMOUNT4 = TOTAL_AMOUNT4 + addDetails.getDcfsaAmount4();
            TOTAL_AMOUNT5 = TOTAL_AMOUNT5 + addDetails.getDcfsaAmount5();
            TOTAL_AMOUNT6 = TOTAL_AMOUNT6 + addDetails.getDcfsaAmount6();
            TOTAL_AMOUNT7 = TOTAL_AMOUNT7 + addDetails.getDcfsaAmount7();
        }

        list = details.getDcfsLessList();
        i = list.iterator();

        while (i.hasNext()) {

            CmRepDailyCashForecastSummaryLessDetails lessDetails = (CmRepDailyCashForecastSummaryLessDetails) i.next();

            TOTAL_AMOUNT1 = TOTAL_AMOUNT1 - lessDetails.getDcfslAmount1();
            TOTAL_AMOUNT2 = TOTAL_AMOUNT2 - lessDetails.getDcfslAmount2();
            TOTAL_AMOUNT3 = TOTAL_AMOUNT3 - lessDetails.getDcfslAmount3();
            TOTAL_AMOUNT4 = TOTAL_AMOUNT4 - lessDetails.getDcfslAmount4();
            TOTAL_AMOUNT5 = TOTAL_AMOUNT5 - lessDetails.getDcfslAmount5();
            TOTAL_AMOUNT6 = TOTAL_AMOUNT6 - lessDetails.getDcfslAmount6();
            TOTAL_AMOUNT7 = TOTAL_AMOUNT7 - lessDetails.getDcfslAmount7();
        }

        details.setDcfsAvailableCashBalance1(EJBCommon.roundIt(details.getDcfsBeginningBalance1() + TOTAL_AMOUNT1, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcfsBeginningBalance2(EJBCommon.roundIt(details.getDcfsAvailableCashBalance1(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcfsAvailableCashBalance2(EJBCommon.roundIt(details.getDcfsBeginningBalance2() + TOTAL_AMOUNT2, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcfsBeginningBalance3(EJBCommon.roundIt(details.getDcfsAvailableCashBalance2(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcfsAvailableCashBalance3(EJBCommon.roundIt(details.getDcfsBeginningBalance3() + TOTAL_AMOUNT3, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcfsBeginningBalance4(EJBCommon.roundIt(details.getDcfsAvailableCashBalance3(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcfsAvailableCashBalance4(EJBCommon.roundIt(details.getDcfsBeginningBalance4() + TOTAL_AMOUNT4, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcfsBeginningBalance5(EJBCommon.roundIt(details.getDcfsAvailableCashBalance4(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcfsAvailableCashBalance5(EJBCommon.roundIt(details.getDcfsBeginningBalance5() + TOTAL_AMOUNT5, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcfsBeginningBalance6(EJBCommon.roundIt(details.getDcfsAvailableCashBalance5(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcfsAvailableCashBalance6(EJBCommon.roundIt(details.getDcfsBeginningBalance6() + TOTAL_AMOUNT6, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcfsBeginningBalance7(EJBCommon.roundIt(details.getDcfsAvailableCashBalance6(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcfsAvailableCashBalance7(EJBCommon.roundIt(details.getDcfsBeginningBalance7() + TOTAL_AMOUNT7, adCompany.getGlFunctionalCurrency().getFcPrecision()));

        return details;
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("CmRepDailyCashForecastSummaryControllerBean ejbCreate");
    }

}