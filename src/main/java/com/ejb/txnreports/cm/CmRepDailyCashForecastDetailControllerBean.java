/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class CmRepDailyCashForecastDetailControllerBean
 * @created January 25, 2006 2:00 PM
 * @author Farrah S. Garing
 */
package com.ejb.txnreports.cm;

import java.util.ArrayList;
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
import com.util.reports.cm.CmRepDailyCashForecastDetailAddDetails;
import com.util.reports.cm.CmRepDailyCashForecastDetailDetails;
import com.util.reports.cm.CmRepDailyCashForecastDetailLessDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "CmRepDailyCashForecastDetailControllerEJB")
public class CmRepDailyCashForecastDetailControllerBean extends EJBContextClass implements CmRepDailyCashForecastDetailController {

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

        Debug.print("CmRepDailyCashForecastDetailControllerBean getAdBaAll");

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

    public CmRepDailyCashForecastDetailDetails executeCmRepDailyCashForecastDetail(HashMap criteria, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmRepDailyCashForecastDetailControllerBean executeCmRepDailyCashForecastDetail");

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

            CmRepDailyCashForecastDetailDetails mdetails = new CmRepDailyCashForecastDetailDetails();

            if (reportType.equals("FIVE DAYS")) {

                mdetails.setDcfdDate5(date);
                mdetails.setDcfdDate4(computeDate(mdetails.getDcfdDate5()));
                mdetails.setDcfdDate3(computeDate(mdetails.getDcfdDate4()));
                mdetails.setDcfdDate2(computeDate(mdetails.getDcfdDate3()));
                mdetails.setDcfdDate1(computeDate(mdetails.getDcfdDate2()));

                criteria.put("dateTo", mdetails.getDcfdDate5());

            } else if (reportType.equals("SEVEN DAYS")) {

                mdetails.setDcfdDate7(date);
                mdetails.setDcfdDate6(computeDate(date));
                mdetails.setDcfdDate5(computeDate(mdetails.getDcfdDate6()));
                mdetails.setDcfdDate4(computeDate(mdetails.getDcfdDate5()));
                mdetails.setDcfdDate3(computeDate(mdetails.getDcfdDate4()));
                mdetails.setDcfdDate2(computeDate(mdetails.getDcfdDate3()));
                mdetails.setDcfdDate1(computeDate(mdetails.getDcfdDate2()));
                criteria.put("dateTo", mdetails.getDcfdDate7());
            }

            criteria.put("dateFrom", mdetails.getDcfdDate1());

            mdetails.setDcfdAddList(executeCmRepDailyCashForecastDetailAdd(criteria, mdetails, AD_CMPNY));
            mdetails.setDcfdLessList(executeCmRepDailyCashForecastDetailLess(criteria, mdetails, AD_CMPNY));

            if (mdetails.getDcfdAddList().isEmpty() && mdetails.getDcfdLessList().isEmpty())
                throw new GlobalNoRecordFoundException();

            double TTL_ACCNT_BLNC = 0d;

            for (Integer selectedBankAccountCode : selectedBankAccountCodes) {

                Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeDateAndBaCodeAndType(mdetails.getDcfdDate1(), selectedBankAccountCode, "BOOK", AD_CMPNY);

                LocalAdBankAccountBalance adBankAccountBalance = null;

                if (!adBankAccountBalances.isEmpty()) {

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;
                    }
                }

                TTL_ACCNT_BLNC = TTL_ACCNT_BLNC + (adBankAccountBalance != null ? adBankAccountBalance.getBabBalance() : 0);
            }

            mdetails.setDcfdBeginningBalance1(TTL_ACCNT_BLNC);

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

        Debug.print("CmRepDailyCashForecastDetailControllerBean getAdCompany");

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

        Debug.print("CmRepDailyCashForecastDetailControllerBean convertForeignToFunctionalCurrency");

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

    private Date computeDate(Date dateEntered) {

        Debug.print("CmRepDailyCashForecastDetailControllerBean computeDate");

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateEntered);
        calendar.add(GregorianCalendar.DATE, -1);

        return calendar.getTime();
    }

    private ArrayList executeCmRepDailyCashForecastDetailAdd(HashMap criteria, CmRepDailyCashForecastDetailDetails details, Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashForecastDetailControllerBean executeCmRepDailyCashForecastDetailAdd");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            StringBuffer jbossQl = new StringBuffer();

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
            Collection dcfad = arInvoiceHome.getInvByCriteria(jbossQl.toString(), obj, 0, 0);
            Iterator i = dcfad.iterator();
            ArrayList dcfaList = new ArrayList();

            while (i.hasNext()) {

                LocalArInvoice arInvoice = (LocalArInvoice) i.next();

                CmRepDailyCashForecastDetailAddDetails addDetails = new CmRepDailyCashForecastDetailAddDetails();

                addDetails.setDcfdaCustomer(arInvoice.getArCustomer().getCstName());
                addDetails.setDcfdaDescription(arInvoice.getInvDescription());
                addDetails.setDcfdaInvoiceNumber(arInvoice.getInvNumber());

                String reportType = (String) criteria.get("reportType");

                for (Object o : arInvoice.getArInvoicePaymentSchedules()) {
                    LocalArInvoicePaymentSchedule arInvoicePaymentSchedule = (LocalArInvoicePaymentSchedule) o;

                    addDetails.setDcfdaDateTransaction(arInvoicePaymentSchedule.getIpsDueDate());

                    double dcfAmount = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arInvoicePaymentSchedule.getIpsAmountDue() - arInvoicePaymentSchedule.getIpsAmountPaid(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                    if (arInvoicePaymentSchedule.getIpsDueDate().equals(details.getDcfdDate1())) {

                        addDetails.setDcfdaAmount1(dcfAmount);

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(details.getDcfdDate2())) {

                        addDetails.setDcfdaAmount2(dcfAmount);

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(details.getDcfdDate3())) {

                        addDetails.setDcfdaAmount3(dcfAmount);

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(details.getDcfdDate4())) {

                        addDetails.setDcfdaAmount4(dcfAmount);

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(details.getDcfdDate5())) {

                        addDetails.setDcfdaAmount5(dcfAmount);

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(details.getDcfdDate6())) {

                        addDetails.setDcfdaAmount6(dcfAmount);

                    } else if (arInvoicePaymentSchedule.getIpsDueDate().equals(details.getDcfdDate7())) {

                        addDetails.setDcfdaAmount7(dcfAmount);
                    }

                    dcfaList.add(addDetails);
                }
            }

            return dcfaList;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private ArrayList executeCmRepDailyCashForecastDetailLess(HashMap criteria, CmRepDailyCashForecastDetailDetails details, Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashForecastDetailControllerBean executeCmRepDailyCashForecastDetailLess");

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

            Collection dcfld = apVoucherHome.getVouByCriteria(jbossQl.toString(), obj, 0, 0);
            Iterator i = dcfld.iterator();
            ArrayList dcflList = new ArrayList();

            while (i.hasNext()) {

                LocalApVoucher apVoucher = (LocalApVoucher) i.next();

                CmRepDailyCashForecastDetailLessDetails lessDetails = new CmRepDailyCashForecastDetailLessDetails();

                lessDetails.setDcfdlSupplier(apVoucher.getApSupplier().getSplName());
                lessDetails.setDcfdlDescription(apVoucher.getVouDescription());
                lessDetails.setDcfdlVoucherNumber(apVoucher.getVouDocumentNumber());

                String reportType = (String) criteria.get("reportType");

                double UN_PSTD_CHCKS = 0d;

                Collection apVoucherPaymentSchedules = apVoucher.getApVoucherPaymentSchedules();

                for (Object voucherPaymentSchedule : apVoucherPaymentSchedules) {

                    LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) voucherPaymentSchedule;

                    lessDetails.setDcfdlDateTransaction(apVoucher.getVouDate());

                    if (!unposted.equals("NO")) {
                        Collection arAppliedVoucher = apVoucherPaymentSchedule.getApAppliedVouchers();

                        for (Object o : arAppliedVoucher) {

                            LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) o;

                            if (apAppliedVoucher.getApCheck().getChkPosted() == EJBCommon.FALSE) {

                                UN_PSTD_CHCKS = UN_PSTD_CHCKS + apAppliedVoucher.getAvApplyAmount() + apAppliedVoucher.getAvTaxWithheld() + apAppliedVoucher.getAvDiscountAmount();
                            }
                        }
                    }

                    lessDetails.setDcfdlDateTransaction(apVoucher.getVouDate());

                    double dcfdlAmount = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apVoucherPaymentSchedule.getVpsAmountDue() - apVoucherPaymentSchedule.getVpsAmountPaid() - UN_PSTD_CHCKS, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                    if (apVoucherPaymentSchedule.getVpsDueDate().equals(details.getDcfdDate1())) {

                        lessDetails.setDcfdlAmount1(dcfdlAmount);

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(details.getDcfdDate2())) {

                        lessDetails.setDcfdlAmount2(dcfdlAmount);

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(details.getDcfdDate3())) {

                        lessDetails.setDcfdlAmount3(dcfdlAmount);

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(details.getDcfdDate4())) {

                        lessDetails.setDcfdlAmount4(dcfdlAmount);

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(details.getDcfdDate5())) {

                        lessDetails.setDcfdlAmount5(dcfdlAmount);

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(details.getDcfdDate6())) {

                        lessDetails.setDcfdlAmount6(dcfdlAmount);

                    } else if (apVoucherPaymentSchedule.getVpsDueDate().equals(details.getDcfdDate6())) {

                        lessDetails.setDcfdlAmount7(dcfdlAmount);
                    }

                    if (dcfdlAmount != 0) dcflList.add(lessDetails);
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
            dcfld = apCheckHome.getChkByCriteria(jbossQl.toString(), obj, 0, 0);
            i = dcfld.iterator();

            while (i.hasNext()) {

                LocalApCheck apCheck = (LocalApCheck) i.next();

                CmRepDailyCashForecastDetailLessDetails lessDetails = new CmRepDailyCashForecastDetailLessDetails();

                lessDetails.setDcfdlSupplier(apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName());
                lessDetails.setDcfdlDateTransaction(apCheck.getChkCheckDate());
                lessDetails.setDcfdlDescription(apCheck.getChkDescription());
                lessDetails.setDcfdlVoucherNumber(apCheck.getChkDocumentNumber());

                lessDetails.setDcfdlAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                dcflList.add(lessDetails);
            }

            return dcflList;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private CmRepDailyCashForecastDetailDetails computeBeginningBalancesAndAvailableCashBalance(CmRepDailyCashForecastDetailDetails details, Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashForecastDetailControllerBean computeBeginningBalancesAndAvailableCashBalance");

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

        } catch (FinderException ex) {

            throw new EJBException(ex.getMessage());
        }

        ArrayList list = details.getDcfdAddList();
        Iterator i = list.iterator();

        while (i.hasNext()) {

            CmRepDailyCashForecastDetailAddDetails addDetails = (CmRepDailyCashForecastDetailAddDetails) i.next();

            TOTAL_AMOUNT1 = TOTAL_AMOUNT1 + addDetails.getDcfdaAmount1();
            TOTAL_AMOUNT2 = TOTAL_AMOUNT2 + addDetails.getDcfdaAmount2();
            TOTAL_AMOUNT3 = TOTAL_AMOUNT3 + addDetails.getDcfdaAmount3();
            TOTAL_AMOUNT4 = TOTAL_AMOUNT4 + addDetails.getDcfdaAmount4();
            TOTAL_AMOUNT5 = TOTAL_AMOUNT5 + addDetails.getDcfdaAmount5();
            TOTAL_AMOUNT6 = TOTAL_AMOUNT6 + addDetails.getDcfdaAmount6();
            TOTAL_AMOUNT7 = TOTAL_AMOUNT7 + addDetails.getDcfdaAmount7();
        }

        list = details.getDcfdLessList();
        i = list.iterator();

        while (i.hasNext()) {

            CmRepDailyCashForecastDetailLessDetails lessDetails = (CmRepDailyCashForecastDetailLessDetails) i.next();

            TOTAL_AMOUNT1 = TOTAL_AMOUNT1 - lessDetails.getDcfdlAmount1();
            TOTAL_AMOUNT2 = TOTAL_AMOUNT2 - lessDetails.getDcfdlAmount2();
            TOTAL_AMOUNT3 = TOTAL_AMOUNT3 - lessDetails.getDcfdlAmount3();
            TOTAL_AMOUNT4 = TOTAL_AMOUNT4 - lessDetails.getDcfdlAmount4();
            TOTAL_AMOUNT5 = TOTAL_AMOUNT5 - lessDetails.getDcfdlAmount5();
            TOTAL_AMOUNT6 = TOTAL_AMOUNT6 - lessDetails.getDcfdlAmount6();
            TOTAL_AMOUNT7 = TOTAL_AMOUNT7 - lessDetails.getDcfdlAmount7();
        }

        details.setDcfdAvailableCashBalance1(EJBCommon.roundIt(details.getDcfdBeginningBalance1() + TOTAL_AMOUNT1, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcfdBeginningBalance2(EJBCommon.roundIt(details.getDcfdAvailableCashBalance1(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcfdAvailableCashBalance2(EJBCommon.roundIt(details.getDcfdBeginningBalance2() + TOTAL_AMOUNT2, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcfdBeginningBalance3(EJBCommon.roundIt(details.getDcfdAvailableCashBalance2(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcfdAvailableCashBalance3(EJBCommon.roundIt(details.getDcfdBeginningBalance3() + TOTAL_AMOUNT3, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcfdBeginningBalance4(EJBCommon.roundIt(details.getDcfdAvailableCashBalance3(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcfdAvailableCashBalance4(EJBCommon.roundIt(details.getDcfdBeginningBalance4() + TOTAL_AMOUNT4, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcfdBeginningBalance5(EJBCommon.roundIt(details.getDcfdAvailableCashBalance4(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcfdAvailableCashBalance5(EJBCommon.roundIt(details.getDcfdBeginningBalance5() + TOTAL_AMOUNT5, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcfdBeginningBalance6(EJBCommon.roundIt(details.getDcfdAvailableCashBalance5(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcfdAvailableCashBalance6(EJBCommon.roundIt(details.getDcfdBeginningBalance6() + TOTAL_AMOUNT6, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcfdBeginningBalance7(EJBCommon.roundIt(details.getDcfdAvailableCashBalance6(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcfdAvailableCashBalance7(EJBCommon.roundIt(details.getDcfdBeginningBalance7() + TOTAL_AMOUNT7, adCompany.getGlFunctionalCurrency().getFcPrecision()));

        return details;
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("CmRepDailyCashForecastDetailControllerBean ejbCreate");
    }
}