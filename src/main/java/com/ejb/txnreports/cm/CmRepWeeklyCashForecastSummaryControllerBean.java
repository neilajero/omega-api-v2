/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmRepWeeklyCashForecastSummaryControllerBean
 * @created January 25, 2006 3:58 PM
 * @author Farrah S. Garing
 */
package com.ejb.txnreports.cm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdBankAccountBalance;
import com.ejb.dao.ad.LocalAdBankAccountBalanceHome;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ap.LocalApAppliedVoucher;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.entities.ap.LocalApVoucherPaymentSchedule;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.entities.ar.LocalArInvoicePaymentSchedule;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.util.ad.AdCompanyDetails;
import com.util.reports.cm.CmRepWeeklyCashForecastSummaryAddDetails;
import com.util.reports.cm.CmRepWeeklyCashForecastSummaryDetails;
import com.util.reports.cm.CmRepWeeklyCashForecastSummaryLessDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "CmRepWeeklyCashForecastSummaryControllerEJB")
public class CmRepWeeklyCashForecastSummaryControllerBean extends EJBContextClass implements CmRepWeeklyCashForecastSummaryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome = null;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome = null;
    @EJB
    private LocalApCheckHome apCheckHome = null;
    @EJB
    private LocalApVoucherHome apVoucherHome = null;
    @EJB
    private LocalArInvoiceHome arInvoiceHome = null;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome = null;


    public ArrayList getAdBaAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("CmRepWeeklyCashForecastSummaryControllerBean getAdBaAll");

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

    public CmRepWeeklyCashForecastSummaryDetails executeCmRepWeeklyCashForecastSummary(HashMap criteria, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmRepWeeklyCashForecastSummaryControllerBean executeCmRepWeeklyCashForecastSummary");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            String[] selectedBankAccounts = (String[]) criteria.get("selectedBankAccount");
            Integer[] selectedBankAccountCodes = new Integer[selectedBankAccounts.length];

            for (int i = 0; i < selectedBankAccounts.length; i++) {

                LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(selectedBankAccounts[i], AD_CMPNY);
                selectedBankAccountCodes[i] = adBankAccount.getBaCode();
            }

            Date[] dateTo = new Date[12];
            Date[] dateFrom = new Date[12];

            for (int i = 0; i < 12; i++) {

                dateTo[i] = new Date();
                dateFrom[i] = new Date();
            }

            String reportType = null;

            if (criteria.containsKey("reportType")) {

                reportType = (String) criteria.get("reportType");
            }

            CmRepWeeklyCashForecastSummaryDetails mdetails = new CmRepWeeklyCashForecastSummaryDetails();

            if (criteria.containsKey("date") && reportType.equals("FOUR WEEKS")) {

                dateTo[3] = (Date) criteria.get("date");
                dateFrom[3] = computeDateSeven(dateTo[3]);

                dateTo[2] = computeDateOne(dateFrom[3]);
                dateFrom[2] = computeDateSeven(dateTo[2]);

                dateTo[1] = computeDateOne(dateFrom[2]);
                dateFrom[1] = computeDateSeven(dateTo[1]);

                dateTo[0] = computeDateOne(dateFrom[1]);
                dateFrom[0] = computeDateSeven(dateTo[0]);

                criteria.put("dateTo", dateTo[3]);

            } else if (criteria.containsKey("date") && reportType.equals("TWELVE WEEKS")) {

                dateTo[11] = (Date) criteria.get("date");
                dateFrom[11] = computeDateSeven(dateTo[10]);

                dateTo[10] = computeDateOne(dateFrom[11]);
                dateFrom[10] = computeDateSeven(dateTo[10]);

                dateTo[9] = computeDateOne(dateFrom[10]);
                dateFrom[9] = computeDateSeven(dateTo[9]);

                dateTo[8] = computeDateOne(dateFrom[9]);
                dateFrom[8] = computeDateSeven(dateTo[8]);

                dateTo[7] = computeDateOne(dateFrom[8]);
                dateFrom[7] = computeDateSeven(dateTo[7]);

                dateTo[6] = computeDateOne(dateFrom[7]);
                dateFrom[6] = computeDateSeven(dateTo[6]);

                dateTo[5] = computeDateOne(dateFrom[6]);
                dateFrom[5] = computeDateSeven(dateTo[5]);

                dateTo[4] = computeDateOne(dateFrom[5]);
                dateFrom[4] = computeDateSeven(dateTo[4]);

                dateTo[3] = computeDateOne(dateFrom[4]);
                dateFrom[3] = computeDateSeven(dateTo[3]);

                dateTo[2] = computeDateOne(dateFrom[3]);
                dateFrom[2] = computeDateSeven(dateTo[2]);

                dateTo[1] = computeDateOne(dateFrom[2]);
                dateFrom[1] = computeDateSeven(dateTo[1]);

                dateTo[0] = computeDateOne(dateFrom[1]);
                dateFrom[0] = computeDateSeven(dateTo[0]);

                criteria.put("dateTo", dateTo[11]);
            }

            criteria.put("dateFrom", dateFrom[0]);

            mdetails.setWcfsAddList(executeCmRepWeeklyCashForecastSummaryAdd(criteria, dateFrom, dateTo, mdetails, AD_CMPNY));
            mdetails.setWcfsLessList(executeCmRepWeeklyCashForecastSummaryLess(criteria, dateFrom, dateTo, mdetails, AD_CMPNY));

            if (mdetails.getWcfsAddList().isEmpty() && mdetails.getWcfsLessList().isEmpty())
                throw new GlobalNoRecordFoundException();

            double TTL_ACCNT_BLNC = 0d;

            for (Integer selectedBankAccountCode : selectedBankAccountCodes) {

                Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeDateAndBaCodeAndType(dateFrom[0], selectedBankAccountCode, "BOOK", AD_CMPNY);

                LocalAdBankAccountBalance adBankAccountBalance = null;

                if (!adBankAccountBalances.isEmpty()) {

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;
                    }
                }

                TTL_ACCNT_BLNC = TTL_ACCNT_BLNC + (adBankAccountBalance != null ? adBankAccountBalance.getBabBalance() : 0);
            }

            mdetails.setWcfsBeginningBalance1(TTL_ACCNT_BLNC);

            mdetails = computeBeginningBalancesAndAvailableCashBalance(mdetails, AD_CMPNY);

            return mdetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("CmRepWeeklyCashForecastSummaryControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("CmRepWeeklyCashForecastSummaryControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

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

    private Date computeDateSeven(Date dateEntered) {

        Debug.print("CmRepWeeklyCashForecastSummaryControllerBean computeDateSeven");

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateEntered);
        calendar.add(GregorianCalendar.DATE, -6);

        return calendar.getTime();
    }

    private ArrayList executeCmRepWeeklyCashForecastSummaryAdd(HashMap criteria, Date[] dateFrom, Date[] dateTo, CmRepWeeklyCashForecastSummaryDetails details, Integer AD_CMPNY) {

        Debug.print("CmRepWeeklyCashForecastSummaryControllerBean executeCmRepWeeklyCashForecastSummaryAdd");

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

            Collection wcfas = arInvoiceHome.getInvByCriteria(jbossQl.toString(), obj, 0, 0);
            Iterator i = wcfas.iterator();
            ArrayList dcfaList = new ArrayList();
            CmRepWeeklyCashForecastSummaryAddDetails addDetails = new CmRepWeeklyCashForecastSummaryAddDetails();

            while (i.hasNext()) {

                LocalArInvoice arInvoice = (LocalArInvoice) i.next();

                addDetails.setWcfsaDescription("INVOICE");

                String reportType = (String) criteria.get("reportType");

                for (Object o : arInvoice.getArInvoicePaymentSchedules()) {
                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) o;

                    if (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[0]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[0]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[0]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[0]))) {

                        addDetails.setWcfsaAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getWcfsaAmount1() + arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[1]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[1]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[1]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[1]))) {

                        addDetails.setWcfsaAmount2(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getWcfsaAmount2() + arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[2]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[2]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[2]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[2]))) {

                        addDetails.setWcfsaAmount3(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getWcfsaAmount3() + arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[3]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[3]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[3]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[3]))) {

                        addDetails.setWcfsaAmount4(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getWcfsaAmount4() + arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (!reportType.equals("TWELVE WEEKS") && (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[4]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[4]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[4]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[4])))) {

                        addDetails.setWcfsaAmount5(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getWcfsaAmount5() + arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (reportType.equals("TWELVE WEEKS") && (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[5]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[5]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[5]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[5])))) {

                        addDetails.setWcfsaAmount6(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getWcfsaAmount6() + arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (reportType.equals("TWELVE WEEKS") && (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[6]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[6]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[6]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[6])))) {

                        addDetails.setWcfsaAmount7(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getWcfsaAmount7() + arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (reportType.equals("TWELVE WEEKS") && (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[7]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[7]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[7]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[7])))) {

                        addDetails.setWcfsaAmount8(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getWcfsaAmount8() + arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (reportType.equals("TWELVE WEEKS") && (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[8]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[8]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[8]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[8])))) {

                        addDetails.setWcfsaAmount9(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getWcfsaAmount9() + arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (reportType.equals("TWELVE WEEKS") && (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[9]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[9]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[9]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[9])))) {

                        addDetails.setWcfsaAmount10(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getWcfsaAmount10() + arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (reportType.equals("TWELVE WEEKS") && (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[10]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[10]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[10]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[10])))) {

                        addDetails.setWcfsaAmount11(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getWcfsaAmount11() + arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (reportType.equals("TWELVE WEEKS") && (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[11]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[11]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[11]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[11])))) {

                        addDetails.setWcfsaAmount12(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), addDetails.getWcfsaAmount12() + arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                    }
                }
            }

            if (addDetails.getWcfsaDescription() != null) {

                dcfaList.add(addDetails);
            }

            return dcfaList;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private ArrayList executeCmRepWeeklyCashForecastSummaryLess(HashMap criteria, Date[] dateFrom, Date[] dateTo, CmRepWeeklyCashForecastSummaryDetails details, Integer AD_CMPNY) {

        Debug.print("CmRepWeeklyCashForecastSummaryControllerBean executeCmRepWeeklyCashForecastSummaryLess");

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

                jbossQl.append("vou.vouDate>=?").append(ctr + 1).append(" ");
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

            Collection wcfls = apVoucherHome.getVouByCriteria(jbossQl.toString(), obj, 0, 0);
            Iterator i = wcfls.iterator();
            ArrayList dcflList = new ArrayList();
            CmRepWeeklyCashForecastSummaryLessDetails lessDetails = new CmRepWeeklyCashForecastSummaryLessDetails();

            while (i.hasNext()) {

                LocalApVoucher apVoucher = (LocalApVoucher) i.next();
                lessDetails.setWcfslDescription("VOUCHER");
                double UN_PSTD_CHCKS = 0d;

                String reportType = (String) criteria.get("reportType");

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

                    if (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[0]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[0]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[0]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[0]))) {

                        lessDetails.setWcfslAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getWcfslAmount1() + apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid() - UN_PSTD_CHCKS, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[1]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[1]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[1]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[1]))) {

                        lessDetails.setWcfslAmount2(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getWcfslAmount2() + apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid() - UN_PSTD_CHCKS, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[2]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[2]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[2]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[2]))) {

                        lessDetails.setWcfslAmount3(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getWcfslAmount3() + apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid() - UN_PSTD_CHCKS, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[3]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[3]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[3]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[3]))) {

                        lessDetails.setWcfslAmount4(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getWcfslAmount4() + apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid() - UN_PSTD_CHCKS, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (!reportType.equals("TWELVE WEEKS") && (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[4]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[4]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[4]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[4])))) {

                        lessDetails.setWcfslAmount5(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getWcfslAmount5() + apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid() - UN_PSTD_CHCKS, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (reportType.equals("TWELVE WEEKS") && (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[5]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[5]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[5]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[5])))) {

                        lessDetails.setWcfslAmount6(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getWcfslAmount6() + apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid() - UN_PSTD_CHCKS, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (reportType.equals("TWELVE WEEKS") && (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[6]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[6]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[6]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[6])))) {

                        lessDetails.setWcfslAmount7(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getWcfslAmount7() + apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid() - UN_PSTD_CHCKS, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (reportType.equals("TWELVE WEEKS") && (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[7]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[7]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[7]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[7])))) {

                        lessDetails.setWcfslAmount8(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getWcfslAmount8() + apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid() - UN_PSTD_CHCKS, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (reportType.equals("TWELVE WEEKS") && (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[8]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[8]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[8]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[8])))) {

                        lessDetails.setWcfslAmount9(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getWcfslAmount9() + apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid() - UN_PSTD_CHCKS, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (reportType.equals("TWELVE WEEKS") && (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[9]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[9]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[9]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[9])))) {

                        lessDetails.setWcfslAmount10(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getWcfslAmount10() + apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (reportType.equals("TWELVE WEEKS") && (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[10]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[10]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[10]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[10])))) {

                        lessDetails.setWcfslAmount11(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getWcfslAmount11() + apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (reportType.equals("TWELVE WEEKS") && (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[11]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[11]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[11]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[11])))) {

                        lessDetails.setWcfslAmount12(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), lessDetails.getWcfslAmount12() + apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                    }
                }
            }

            if (lessDetails.getWcfslDescription() != null || (lessDetails.getWcfslAmount1() != 0 && lessDetails.getWcfslAmount2() != 0 && lessDetails.getWcfslAmount3() != 0 && lessDetails.getWcfslAmount4() != 0 && lessDetails.getWcfslAmount5() != 0 && lessDetails.getWcfslAmount6() != 0 && lessDetails.getWcfslAmount7() != 0 && lessDetails.getWcfslAmount8() != 0 && lessDetails.getWcfslAmount9() != 0 && lessDetails.getWcfslAmount10() != 0 && lessDetails.getWcfslAmount11() != 0 && lessDetails.getWcfslAmount12() != 0)) {

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

            wcfls = apCheckHome.getChkByCriteria(jbossQl.toString(), obj, 0, 0);
            i = wcfls.iterator();
            lessDetails = new CmRepWeeklyCashForecastSummaryLessDetails();

            while (i.hasNext()) {

                LocalApCheck apCheck = (LocalApCheck) i.next();

                lessDetails.setWcfslDescription("UNRELEASED CHECKS");

                String reportType = (String) criteria.get("reportType");

                lessDetails.setWcfslAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), lessDetails.getWcfslAmount1() + apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
            }

            if (lessDetails.getWcfslDescription() != null) {

                dcflList.add(lessDetails);
            }

            return dcflList;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private CmRepWeeklyCashForecastSummaryDetails computeBeginningBalancesAndAvailableCashBalance(CmRepWeeklyCashForecastSummaryDetails details, Integer AD_CMPNY) {

        Debug.print("CmRepWeeklyCashForecastSummaryControllerBean computeBeginningBalancesAndAvailableCashBalance");

        LocalAdCompany adCompany = null;

        double TOTAL_AMOUNT1 = 0;
        double TOTAL_AMOUNT2 = 0;
        double TOTAL_AMOUNT3 = 0;
        double TOTAL_AMOUNT4 = 0;
        double TOTAL_AMOUNT5 = 0;
        double TOTAL_AMOUNT6 = 0;
        double TOTAL_AMOUNT7 = 0;
        double TOTAL_AMOUNT8 = 0;
        double TOTAL_AMOUNT9 = 0;
        double TOTAL_AMOUNT10 = 0;
        double TOTAL_AMOUNT11 = 0;
        double TOTAL_AMOUNT12 = 0;

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (FinderException ex) {

            throw new EJBException(ex.getMessage());
        }

        ArrayList list = details.getWcfsAddList();
        Iterator i = list.iterator();

        while (i.hasNext()) {

            CmRepWeeklyCashForecastSummaryAddDetails addDetails = (CmRepWeeklyCashForecastSummaryAddDetails) i.next();

            TOTAL_AMOUNT1 = TOTAL_AMOUNT1 + addDetails.getWcfsaAmount1();
            TOTAL_AMOUNT2 = TOTAL_AMOUNT2 + addDetails.getWcfsaAmount2();
            TOTAL_AMOUNT3 = TOTAL_AMOUNT3 + addDetails.getWcfsaAmount3();
            TOTAL_AMOUNT4 = TOTAL_AMOUNT4 + addDetails.getWcfsaAmount4();
            TOTAL_AMOUNT5 = TOTAL_AMOUNT5 + addDetails.getWcfsaAmount5();
            TOTAL_AMOUNT6 = TOTAL_AMOUNT6 + addDetails.getWcfsaAmount6();
            TOTAL_AMOUNT7 = TOTAL_AMOUNT7 + addDetails.getWcfsaAmount7();
            TOTAL_AMOUNT8 = TOTAL_AMOUNT8 + addDetails.getWcfsaAmount8();
            TOTAL_AMOUNT9 = TOTAL_AMOUNT9 + addDetails.getWcfsaAmount9();
            TOTAL_AMOUNT10 = TOTAL_AMOUNT10 + addDetails.getWcfsaAmount10();
            TOTAL_AMOUNT11 = TOTAL_AMOUNT11 + addDetails.getWcfsaAmount11();
            TOTAL_AMOUNT12 = TOTAL_AMOUNT12 + addDetails.getWcfsaAmount12();
        }

        list = details.getWcfsLessList();
        i = list.iterator();

        while (i.hasNext()) {

            CmRepWeeklyCashForecastSummaryLessDetails lessDetails = (CmRepWeeklyCashForecastSummaryLessDetails) i.next();

            TOTAL_AMOUNT1 = TOTAL_AMOUNT1 - lessDetails.getWcfslAmount1();
            TOTAL_AMOUNT2 = TOTAL_AMOUNT2 - lessDetails.getWcfslAmount2();
            TOTAL_AMOUNT3 = TOTAL_AMOUNT3 - lessDetails.getWcfslAmount3();
            TOTAL_AMOUNT4 = TOTAL_AMOUNT4 - lessDetails.getWcfslAmount4();
            TOTAL_AMOUNT5 = TOTAL_AMOUNT5 - lessDetails.getWcfslAmount5();
            TOTAL_AMOUNT6 = TOTAL_AMOUNT6 - lessDetails.getWcfslAmount6();
            TOTAL_AMOUNT7 = TOTAL_AMOUNT7 - lessDetails.getWcfslAmount7();
            TOTAL_AMOUNT8 = TOTAL_AMOUNT8 - lessDetails.getWcfslAmount8();
            TOTAL_AMOUNT9 = TOTAL_AMOUNT9 - lessDetails.getWcfslAmount9();
            TOTAL_AMOUNT10 = TOTAL_AMOUNT10 - lessDetails.getWcfslAmount10();
            TOTAL_AMOUNT11 = TOTAL_AMOUNT11 - lessDetails.getWcfslAmount11();
            TOTAL_AMOUNT12 = TOTAL_AMOUNT12 - lessDetails.getWcfslAmount12();
        }

        details.setWcfsAvailableCashBalance1(EJBCommon.roundIt(details.getWcfsBeginningBalance1() + TOTAL_AMOUNT1, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfsBeginningBalance2(EJBCommon.roundIt(details.getWcfsAvailableCashBalance1(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfsAvailableCashBalance2(EJBCommon.roundIt(details.getWcfsBeginningBalance2() + TOTAL_AMOUNT2, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfsBeginningBalance3(EJBCommon.roundIt(details.getWcfsAvailableCashBalance2(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfsAvailableCashBalance3(EJBCommon.roundIt(details.getWcfsBeginningBalance3() + TOTAL_AMOUNT3, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfsBeginningBalance4(EJBCommon.roundIt(details.getWcfsAvailableCashBalance3(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfsAvailableCashBalance4(EJBCommon.roundIt(details.getWcfsBeginningBalance4() + TOTAL_AMOUNT4, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfsBeginningBalance5(EJBCommon.roundIt(details.getWcfsAvailableCashBalance4(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfsAvailableCashBalance5(EJBCommon.roundIt(details.getWcfsBeginningBalance5() + TOTAL_AMOUNT5, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfsBeginningBalance6(EJBCommon.roundIt(details.getWcfsAvailableCashBalance5(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfsAvailableCashBalance6(EJBCommon.roundIt(details.getWcfsBeginningBalance6() + TOTAL_AMOUNT6, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfsBeginningBalance7(EJBCommon.roundIt(details.getWcfsAvailableCashBalance6(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfsAvailableCashBalance7(EJBCommon.roundIt(details.getWcfsBeginningBalance7() + TOTAL_AMOUNT7, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfsBeginningBalance8(EJBCommon.roundIt(details.getWcfsAvailableCashBalance7(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfsAvailableCashBalance8(EJBCommon.roundIt(details.getWcfsBeginningBalance8() + TOTAL_AMOUNT8, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfsBeginningBalance9(EJBCommon.roundIt(details.getWcfsAvailableCashBalance8(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfsAvailableCashBalance9(EJBCommon.roundIt(details.getWcfsBeginningBalance9() + TOTAL_AMOUNT9, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfsBeginningBalance10(EJBCommon.roundIt(details.getWcfsAvailableCashBalance9(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfsAvailableCashBalance10(EJBCommon.roundIt(details.getWcfsBeginningBalance10() + TOTAL_AMOUNT10, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfsBeginningBalance11(EJBCommon.roundIt(details.getWcfsAvailableCashBalance10(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfsAvailableCashBalance11(EJBCommon.roundIt(details.getWcfsBeginningBalance11() + TOTAL_AMOUNT11, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfsBeginningBalance12(EJBCommon.roundIt(details.getWcfsAvailableCashBalance11(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfsAvailableCashBalance12(EJBCommon.roundIt(details.getWcfsBeginningBalance12() + TOTAL_AMOUNT12, adCompany.getGlFunctionalCurrency().getFcPrecision()));

        return details;
    }

    private Date computeDateOne(Date dateEntered) {

        Debug.print("CmRepDailyCashForecastDetailControllerBean computeDate");

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateEntered);
        int date = 0;
        int month = calendar.get(Calendar.MONTH);
        int year = 0;

        if (calendar.get(Calendar.DATE) == 1) {

            int givenMonth = month;

            if (givenMonth == Calendar.DECEMBER) {

                month = Calendar.JANUARY;
                year = calendar.get(Calendar.YEAR) - 1;

            } else {

                month = givenMonth - 1;
                year = calendar.get(Calendar.YEAR);
            }

            if (month == Calendar.FEBRUARY) {

                if (calendar.get(Calendar.YEAR) % 4 == 0) {

                    date = 29;

                } else {

                    date = 28;
                }

            } else if (month == Calendar.APRIL || month == Calendar.JUNE || month == Calendar.SEPTEMBER || month == Calendar.NOVEMBER) {

                date = 30;

            } else {

                date = 31;
            }

        } else {

            date = calendar.get(Calendar.DATE) - 1;
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
        }

        calendar.set(year, month, date);

        return calendar.getTime();
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("CmRepWeeklyCashForecastSummaryControllerBean ejbCreate");
    }
}