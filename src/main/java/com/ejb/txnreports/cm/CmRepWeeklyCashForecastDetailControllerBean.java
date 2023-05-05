/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmRepWeeklyCashForecastDetailControllerBean
 * @created January 25, 2006 2:00 PM
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
import com.util.reports.cm.CmRepWeeklyCashForecastDetailAddDetails;
import com.util.reports.cm.CmRepWeeklyCashForecastDetailDetails;
import com.util.reports.cm.CmRepWeeklyCashForecastDetailLessDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "CmRepWeeklyCashForecastDetailControllerEJB")
public class CmRepWeeklyCashForecastDetailControllerBean extends EJBContextClass implements CmRepWeeklyCashForecastDetailController {

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

        Debug.print("CmRepWeeklyCashForecastDetailControllerBean getAdBaAll");
        
        ArrayList list = new ArrayList();
        LocalAdBankAccount adBankAccount = null;

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

    public CmRepWeeklyCashForecastDetailDetails executeCmRepWeeklyCashForecastDetail(HashMap criteria, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmRepWeeklyCashForecastDetailControllerBean executeCmRepWeeklyCashForecastDetail");

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

            CmRepWeeklyCashForecastDetailDetails mdetails = new CmRepWeeklyCashForecastDetailDetails();

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

            mdetails.setWcfdAddList(executeCmRepWeeklyCashForecastDetailAdd(criteria, dateFrom, dateTo, mdetails, AD_CMPNY));
            mdetails.setWcfdLessList(executeCmRepWeeklyCashForecastDetailLess(criteria, dateFrom, dateTo, mdetails, AD_CMPNY));

            if (mdetails.getWcfdAddList().isEmpty() && mdetails.getWcfdLessList().isEmpty())
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

            mdetails.setWcfdBeginningBalance1(TTL_ACCNT_BLNC);

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

        Debug.print("CmRepWeeklyCashForecastDetailControllerBean getAdCompany");

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

        Debug.print("CmRepWeeklyCashForecastDetailControllerBean convertForeignToFunctionalCurrency");

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

        Debug.print("CmRepWeeklyCashForecastDetailControllerBean computeDateSeven");

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateEntered);
        calendar.add(GregorianCalendar.DATE, -6);

        return calendar.getTime();
    }

    private ArrayList executeCmRepWeeklyCashForecastDetailAdd(HashMap criteria, Date[] dateFrom, Date[] dateTo, CmRepWeeklyCashForecastDetailDetails details, Integer AD_CMPNY) {

        Debug.print("CmRepWeeklyCashForecastDetailControllerBean executeCmRepWeeklyCashForecastDetailAdd");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            StringBuilder jbossQl = new StringBuilder();

            // get all receipt

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

            Collection wcfad = arInvoiceHome.getInvByCriteria(jbossQl.toString(), obj, 0, 0);
            Iterator i = wcfad.iterator();
            ArrayList dcfaList = new ArrayList();

            while (i.hasNext()) {

                LocalArInvoice arInvoice = (LocalArInvoice) i.next();

                CmRepWeeklyCashForecastDetailAddDetails addDetails = new CmRepWeeklyCashForecastDetailAddDetails();

                addDetails.setWcfdaCustomer(arInvoice.getArCustomer().getCstName());
                addDetails.setWcfdaDescription(arInvoice.getInvDescription());
                addDetails.setWcfdaInvoiceNumber(arInvoice.getInvNumber());

                String reportType = (String) criteria.get("reportType");

                for (Object o : arInvoice.getArInvoicePaymentSchedules()) {
                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) o;

                    addDetails.setWcfdaDateTransaction(arInvoicePaymentSchedule.getIpsDueDate());

                    double wcfAmount = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                    if (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[0]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[0]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[0]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[0]))) {

                        addDetails.setWcfdaAmount1(wcfAmount);

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[1]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[1]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[1]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[1]))) {

                        addDetails.setWcfdaAmount2(wcfAmount);

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[2]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[2]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[2]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[2]))) {

                        addDetails.setWcfdaAmount3(wcfAmount);

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[3]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[3]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[3]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[3]))) {

                        addDetails.setWcfdaAmount4(wcfAmount);

                    } else if (!reportType.equals("TWELVE WEEKS") && (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[4]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[4]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[4]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[4])))) {

                        addDetails.setWcfdaAmount5(wcfAmount);

                    } else if (reportType.equals("TWELVE WEEKS") && (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[5]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[5]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[5]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[5])))) {

                        addDetails.setWcfdaAmount6(wcfAmount);

                    } else if (reportType.equals("TWELVE WEEKS") && (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[6]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[6]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[6]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[6])))) {

                        addDetails.setWcfdaAmount7(wcfAmount);

                    } else if (reportType.equals("TWELVE WEEKS") && (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[7]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[7]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[7]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[7])))) {

                        addDetails.setWcfdaAmount8(wcfAmount);

                    } else if (reportType.equals("TWELVE WEEKS") && (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[8]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[8]) || arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[8]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[8]))) {

                        addDetails.setWcfdaAmount9(wcfAmount);

                    } else if (reportType.equals("TWELVE WEEKS") && (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[9]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[9]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[9]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[9])))) {

                        addDetails.setWcfdaAmount10(wcfAmount);

                    } else if (reportType.equals("TWELVE WEEKS") && (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[10]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[10]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[10]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[10])))) {

                        addDetails.setWcfdaAmount11(wcfAmount);

                    } else if (reportType.equals("TWELVE WEEKS") && (arInvoicePaymentSchedule.getIpsDueDate().equals(dateTo[11]) || arInvoicePaymentSchedule.getIpsDueDate().equals(dateFrom[11]) || (arInvoicePaymentSchedule.getIpsDueDate().before(dateTo[11]) && arInvoicePaymentSchedule.getIpsDueDate().after(dateFrom[11])))) {

                        addDetails.setWcfdaAmount12(wcfAmount);
                    }
                }
                dcfaList.add(addDetails);
            }

            return dcfaList;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private ArrayList executeCmRepWeeklyCashForecastDetailLess(HashMap criteria, Date[] dateFrom, Date[] dateTo, CmRepWeeklyCashForecastDetailDetails details, Integer AD_CMPNY) {

        Debug.print("CmRepWeeklyCashForecastDetailControllerBean executeCmRepWeeklyCashForecastDetailLess");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            StringBuilder jbossQl = new StringBuilder();

            // get all receipt

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

            Collection wcfld = apVoucherHome.getVouByCriteria(jbossQl.toString(), obj, 0, 0);
            Iterator i = wcfld.iterator();
            ArrayList dcflList = new ArrayList();

            while (i.hasNext()) {

                LocalApVoucher apVoucher = (LocalApVoucher) i.next();

                CmRepWeeklyCashForecastDetailLessDetails lessDetails = new CmRepWeeklyCashForecastDetailLessDetails();

                lessDetails.setWcfdlSupplier(apVoucher.getApSupplier().getSplName());
                lessDetails.setWcfdlDateTransaction(apVoucher.getVouDate());
                lessDetails.setWcfdlDescription(apVoucher.getVouDescription());
                lessDetails.setWcfdlVoucherNumber(apVoucher.getVouDocumentNumber());

                String reportType = (String) criteria.get("reportType");

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
                    double wcfdlAmount = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());
                    if (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[0]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[0]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[0]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[0]))) {

                        lessDetails.setWcfdlAmount1(wcfdlAmount);

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[1]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[1]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[1]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[1]))) {

                        lessDetails.setWcfdlAmount2(wcfdlAmount);

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[2]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[2]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[2]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[2]))) {

                        lessDetails.setWcfdlAmount3(wcfdlAmount);

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[3]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[3]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[3]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[3]))) {

                        lessDetails.setWcfdlAmount4(wcfdlAmount);

                    } else if (!reportType.equals("TWELVE WEEKS") && (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[4]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[4]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[4]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[4])))) {

                        lessDetails.setWcfdlAmount5(wcfdlAmount);

                    } else if (reportType.equals("TWELVE WEEKS") && (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[5]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[5]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[5]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[5])))) {

                        lessDetails.setWcfdlAmount6(wcfdlAmount);

                    } else if (reportType.equals("TWELVE WEEKS") && (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[6]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[6]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[6]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[6])))) {

                        lessDetails.setWcfdlAmount7(wcfdlAmount);

                    } else if (reportType.equals("TWELVE WEEKS") && (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[7]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[7]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[7]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[7])))) {

                        lessDetails.setWcfdlAmount8(wcfdlAmount);

                    } else if (reportType.equals("TWELVE WEEKS") && (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[8]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[8]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[8]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[8])))) {

                        lessDetails.setWcfdlAmount9(wcfdlAmount);

                    } else if (reportType.equals("TWELVE WEEKS") && (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[9]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[9]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[9]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[9])))) {

                        lessDetails.setWcfdlAmount10(wcfdlAmount);

                    } else if (reportType.equals("TWELVE WEEKS") && (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[10]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[10]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[10]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[10])))) {

                        lessDetails.setWcfdlAmount11(wcfdlAmount);

                    } else if (reportType.equals("TWELVE WEEKS") && (apVoucherPaymentSchedule.getVpsDueDate().equals(dateTo[11]) || apVoucherPaymentSchedule.getVpsDueDate().equals(dateFrom[11]) || (apVoucherPaymentSchedule.getVpsDueDate().before(dateTo[11]) && apVoucherPaymentSchedule.getVpsDueDate().after(dateFrom[11])))) {

                        lessDetails.setWcfdlAmount12(wcfdlAmount);
                    }
                }

                if ((apVoucher.getVouAmountDue() - apVoucher.getVouAmountPaid() - UN_PSTD_CHCKS) != 0) {

                    dcflList.add(lessDetails);
                }
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

            wcfld = apCheckHome.getChkByCriteria(jbossQl.toString(), obj, 0, 0);
            i = wcfld.iterator();

            while (i.hasNext()) {

                LocalApCheck apCheck = (LocalApCheck) i.next();

                CmRepWeeklyCashForecastDetailLessDetails lessDetails = new CmRepWeeklyCashForecastDetailLessDetails();

                lessDetails.setWcfdlSupplier(apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName());
                lessDetails.setWcfdlDateTransaction(apCheck.getChkCheckDate());
                lessDetails.setWcfdlDescription(apCheck.getChkDescription());
                lessDetails.setWcfdlVoucherNumber(apCheck.getChkDocumentNumber());

                String reportType = (String) criteria.get("reportType");

                lessDetails.setWcfdlAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                dcflList.add(lessDetails);
            }

            return dcflList;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private CmRepWeeklyCashForecastDetailDetails computeBeginningBalancesAndAvailableCashBalance(CmRepWeeklyCashForecastDetailDetails details, Integer AD_CMPNY) {

        Debug.print("CmRepWeeklyCashForecastDetailControllerBean computeBeginningBalancesAndAvailableCashBalance");

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

        ArrayList list = details.getWcfdAddList();
        Iterator i = list.iterator();

        while (i.hasNext()) {

            CmRepWeeklyCashForecastDetailAddDetails addDetails = (CmRepWeeklyCashForecastDetailAddDetails) i.next();

            TOTAL_AMOUNT1 = TOTAL_AMOUNT1 + addDetails.getWcfdaAmount1();
            TOTAL_AMOUNT2 = TOTAL_AMOUNT2 + addDetails.getWcfdaAmount2();
            TOTAL_AMOUNT3 = TOTAL_AMOUNT3 + addDetails.getWcfdaAmount3();
            TOTAL_AMOUNT4 = TOTAL_AMOUNT4 + addDetails.getWcfdaAmount4();
            TOTAL_AMOUNT5 = TOTAL_AMOUNT5 + addDetails.getWcfdaAmount5();
            TOTAL_AMOUNT6 = TOTAL_AMOUNT6 + addDetails.getWcfdaAmount6();
            TOTAL_AMOUNT7 = TOTAL_AMOUNT7 + addDetails.getWcfdaAmount7();
            TOTAL_AMOUNT8 = TOTAL_AMOUNT8 + addDetails.getWcfdaAmount8();
            TOTAL_AMOUNT9 = TOTAL_AMOUNT9 + addDetails.getWcfdaAmount9();
            TOTAL_AMOUNT10 = TOTAL_AMOUNT10 + addDetails.getWcfdaAmount10();
            TOTAL_AMOUNT11 = TOTAL_AMOUNT11 + addDetails.getWcfdaAmount11();
            TOTAL_AMOUNT12 = TOTAL_AMOUNT12 + addDetails.getWcfdaAmount12();
        }

        list = details.getWcfdLessList();
        i = list.iterator();

        while (i.hasNext()) {

            CmRepWeeklyCashForecastDetailLessDetails lessDetails = (CmRepWeeklyCashForecastDetailLessDetails) i.next();

            TOTAL_AMOUNT1 = TOTAL_AMOUNT1 - lessDetails.getWcfdlAmount1();
            TOTAL_AMOUNT2 = TOTAL_AMOUNT2 - lessDetails.getWcfdlAmount2();
            TOTAL_AMOUNT3 = TOTAL_AMOUNT3 - lessDetails.getWcfdlAmount3();
            TOTAL_AMOUNT4 = TOTAL_AMOUNT4 - lessDetails.getWcfdlAmount4();
            TOTAL_AMOUNT5 = TOTAL_AMOUNT5 - lessDetails.getWcfdlAmount5();
            TOTAL_AMOUNT6 = TOTAL_AMOUNT6 - lessDetails.getWcfdlAmount6();
            TOTAL_AMOUNT7 = TOTAL_AMOUNT7 - lessDetails.getWcfdlAmount7();
            TOTAL_AMOUNT8 = TOTAL_AMOUNT8 - lessDetails.getWcfdlAmount8();
            TOTAL_AMOUNT9 = TOTAL_AMOUNT9 - lessDetails.getWcfdlAmount9();
            TOTAL_AMOUNT10 = TOTAL_AMOUNT10 - lessDetails.getWcfdlAmount10();
            TOTAL_AMOUNT11 = TOTAL_AMOUNT11 - lessDetails.getWcfdlAmount11();
            TOTAL_AMOUNT12 = TOTAL_AMOUNT12 - lessDetails.getWcfdlAmount12();
        }

        details.setWcfdAvailableCashBalance1(EJBCommon.roundIt(details.getWcfdBeginningBalance1() + TOTAL_AMOUNT1, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfdBeginningBalance2(EJBCommon.roundIt(details.getWcfdAvailableCashBalance1(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfdAvailableCashBalance2(EJBCommon.roundIt(details.getWcfdBeginningBalance2() + TOTAL_AMOUNT2, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfdBeginningBalance3(EJBCommon.roundIt(details.getWcfdAvailableCashBalance2(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfdAvailableCashBalance3(EJBCommon.roundIt(details.getWcfdBeginningBalance3() + TOTAL_AMOUNT3, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfdBeginningBalance4(EJBCommon.roundIt(details.getWcfdAvailableCashBalance3(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfdAvailableCashBalance4(EJBCommon.roundIt(details.getWcfdBeginningBalance4() + TOTAL_AMOUNT4, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfdBeginningBalance5(EJBCommon.roundIt(details.getWcfdAvailableCashBalance4(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfdAvailableCashBalance5(EJBCommon.roundIt(details.getWcfdBeginningBalance5() + TOTAL_AMOUNT5, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfdBeginningBalance6(EJBCommon.roundIt(details.getWcfdAvailableCashBalance5(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfdAvailableCashBalance6(EJBCommon.roundIt(details.getWcfdBeginningBalance6() + TOTAL_AMOUNT6, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfdBeginningBalance7(EJBCommon.roundIt(details.getWcfdAvailableCashBalance6(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfdAvailableCashBalance7(EJBCommon.roundIt(details.getWcfdBeginningBalance7() + TOTAL_AMOUNT7, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfdBeginningBalance8(EJBCommon.roundIt(details.getWcfdAvailableCashBalance7(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfdAvailableCashBalance8(EJBCommon.roundIt(details.getWcfdBeginningBalance8() + TOTAL_AMOUNT8, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfdBeginningBalance9(EJBCommon.roundIt(details.getWcfdAvailableCashBalance8(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfdAvailableCashBalance9(EJBCommon.roundIt(details.getWcfdBeginningBalance9() + TOTAL_AMOUNT9, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfdBeginningBalance10(EJBCommon.roundIt(details.getWcfdAvailableCashBalance9(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfdAvailableCashBalance10(EJBCommon.roundIt(details.getWcfdBeginningBalance10() + TOTAL_AMOUNT10, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfdBeginningBalance11(EJBCommon.roundIt(details.getWcfdAvailableCashBalance10(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfdAvailableCashBalance11(EJBCommon.roundIt(details.getWcfdBeginningBalance11() + TOTAL_AMOUNT11, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setWcfdBeginningBalance12(EJBCommon.roundIt(details.getWcfdAvailableCashBalance11(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setWcfdAvailableCashBalance12(EJBCommon.roundIt(details.getWcfdBeginningBalance12() + TOTAL_AMOUNT12, adCompany.getGlFunctionalCurrency().getFcPrecision()));

        return details;
    }

    private Date computeDateOne(Date dateEntered) {

        Debug.print("CmRepDailyCashForecastDetailControllerBean computeDateOne");

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

        Debug.print("CmRepWeeklyCashForecastDetailControllerBean ejbCreate");
    }
}