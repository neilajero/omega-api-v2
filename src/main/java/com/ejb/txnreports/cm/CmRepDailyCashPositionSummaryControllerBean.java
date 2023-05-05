/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmRepDailyCashPositionSummaryControllerBean
 * @created January 23, 2006 9:30 AM
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
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.dao.cm.LocalCmAdjustmentHome;
import com.ejb.entities.cm.LocalCmFundTransfer;
import com.ejb.dao.cm.LocalCmFundTransferHome;
import com.ejb.entities.cm.LocalCmFundTransferReceipt;
import com.ejb.dao.cm.LocalCmFundTransferReceiptHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.util.ad.AdCompanyDetails;
import com.util.reports.cm.CmRepDailyCashPositionSummaryAddDetails;
import com.util.reports.cm.CmRepDailyCashPositionSummaryDetails;
import com.util.reports.cm.CmRepDailyCashPositionSummaryLessDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "CmRepDailyCashPositionSummaryControllerEJB")
public class CmRepDailyCashPositionSummaryControllerBean extends EJBContextClass implements CmRepDailyCashPositionSummaryController {

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
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalCmAdjustmentHome cmAdjustmentHome;
    @EJB
    private LocalCmFundTransferHome cmFundTransferHome;
    @EJB
    private LocalCmFundTransferReceiptHome cmFundTransferReceiptHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;


    public ArrayList getAdBaAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashPositionSummaryControllerBean getAdBaAll");

        ArrayList list = new ArrayList();
        LocalAdBankAccount adBankAccount = null;

        try {

            Collection adBankAccounts = adBankAccountHome.findEnabledBaAll(AD_BRNCH, AD_CMPNY);

            for (Object bankAccount : adBankAccounts) {

                adBankAccount = (LocalAdBankAccount) bankAccount;

                if (adBankAccount.getBaIsCashAccount() == EJBCommon.FALSE) list.add(adBankAccount.getBaName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public CmRepDailyCashPositionSummaryDetails executeCmRepDailyCashPositionSummary(HashMap criteria, boolean includeDirectChecks, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmRepDailyCashPositionSummaryControllerBean executeCmRepDailyCashPositionSummary");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName((String) criteria.get("bankAccount"), AD_CMPNY);

            criteria.put("bankAccountCode", adBankAccount.getBaCode());

            Date date = null;

            if (criteria.containsKey("date")) {

                date = (Date) criteria.get("date");
            }

            String reportType = null;

            if (criteria.containsKey("reportType")) {

                reportType = (String) criteria.get("reportType");
            }

            CmRepDailyCashPositionSummaryDetails mdetails = new CmRepDailyCashPositionSummaryDetails();

            switch (reportType) {
                case "ONE DAY":

                    mdetails.setDcpsDate1(date);

                    break;
                case "FIVE DAYS":

                    mdetails.setDcpsDate5(date);
                    mdetails.setDcpsDate4(computeDate(mdetails.getDcpsDate5()));
                    mdetails.setDcpsDate3(computeDate(mdetails.getDcpsDate4()));
                    mdetails.setDcpsDate2(computeDate(mdetails.getDcpsDate3()));
                    mdetails.setDcpsDate1(computeDate(mdetails.getDcpsDate2()));

                    criteria.put("dateTo", mdetails.getDcpsDate5());

                    break;
                case "SEVEN DAYS":

                    mdetails.setDcpsDate7(date);
                    mdetails.setDcpsDate6(computeDate(date));
                    mdetails.setDcpsDate5(computeDate(mdetails.getDcpsDate6()));
                    mdetails.setDcpsDate4(computeDate(mdetails.getDcpsDate5()));
                    mdetails.setDcpsDate3(computeDate(mdetails.getDcpsDate4()));
                    mdetails.setDcpsDate2(computeDate(mdetails.getDcpsDate3()));
                    mdetails.setDcpsDate1(computeDate(mdetails.getDcpsDate2()));
                    criteria.put("dateTo", mdetails.getDcpsDate7());
                    break;
            }

            criteria.put("dateFrom", mdetails.getDcpsDate1());

            mdetails.setDcpsAddList(executeCmRepDailyCashPositionSummaryAdd(criteria, mdetails, AD_CMPNY));
            mdetails.setDcpsLessList(executeCmRepDailyCashPositionSummaryLess(criteria, mdetails, includeDirectChecks, AD_CMPNY));

            if (mdetails.getDcpsAddList().isEmpty() && mdetails.getDcpsLessList().isEmpty())
                throw new GlobalNoRecordFoundException();

            Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeDateAndBaCodeAndType(mdetails.getDcpsDate1(), adBankAccount.getBaCode(), "BOOK", AD_CMPNY);

            LocalAdBankAccountBalance adBankAccountBalance = null;

            if (!adBankAccountBalances.isEmpty()) {

                for (Object bankAccountBalance : adBankAccountBalances) {

                    adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;
                }
            }

            mdetails.setDcpsBeginningBalance1(adBankAccountBalance != null ? adBankAccountBalance.getBabBalance() : 0);

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

        Debug.print("CmRepDailyCashPositionSummaryControllerBean getAdCompany");

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

        Debug.print("CmRepDailyCashPositionSummaryControllerBean convertForeignToFunctionalCurrency");

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

        Debug.print("CmRepDailyCashPositionSummaryControllerBean computeDate");

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateEntered);
        calendar.add(GregorianCalendar.DATE, -1);

        return calendar.getTime();
    }

    private ArrayList executeCmRepDailyCashPositionSummaryAdd(HashMap criteria, CmRepDailyCashPositionSummaryDetails details, Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashPositionSummaryControllerBean executeCmRepDailyCashPositionSummaryAdd");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size() - 3;

            StringBuilder jbossQl = new StringBuilder();

            // get all cash receipt

            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct ");

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

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

                jbossQl.append("rct.adBankAccount.baName=?").append(ctr + 1).append(" ");

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

                    jbossQl.append("rct.rctDate>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");

                    ctr++;

                } else {

                    jbossQl.append("rct.rctDate=?").append(ctr + 1).append(" ");
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

                jbossQl.append("rct.rctDate<=?").append(ctr + 1).append(" ");
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

            jbossQl.append(" rct.rctAdCompany = ").append(AD_CMPNY).append(" ");

            Collection dcpas = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, 0, 0);
            Iterator i = dcpas.iterator();
            ArrayList dcpaList = new ArrayList();
            CmRepDailyCashPositionSummaryAddDetails addDetails = new CmRepDailyCashPositionSummaryAddDetails();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                addDetails.setDcpsaDescription("RECEIPT");

                String reportType = (String) criteria.get("reportType");

                if (arReceipt.getRctDate().equals(details.getDcpsDate1())) {

                    addDetails.setDcpsaAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount1() + arReceipt.getRctAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate2())) {

                    addDetails.setDcpsaAmount2(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount2() + arReceipt.getRctAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate3())) {

                    addDetails.setDcpsaAmount3(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount3() + arReceipt.getRctAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate4())) {

                    addDetails.setDcpsaAmount4(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount4() + arReceipt.getRctAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate5())) {

                    addDetails.setDcpsaAmount5(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount5() + arReceipt.getRctAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate6())) {

                    addDetails.setDcpsaAmount6(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount6() + arReceipt.getRctAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate7())) {

                    addDetails.setDcpsaAmount7(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount7() + arReceipt.getRctAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }
            }

            if (addDetails.getDcpsaDescription() != null) {

                dcpaList.add(addDetails);
            }

            firstArgument = true;
            ctr = 0;
            criteriaSize = criteria.size() - 3;

            jbossQl = new StringBuilder();

            // get all cheque receipt

            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct ");

            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

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

                jbossQl.append("rct.adBankAccount.baName=?").append(ctr + 1).append(" ");

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

                    jbossQl.append("rct.rctDate>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");

                    ctr++;

                } else {

                    jbossQl.append("rct.rctDate=?").append(ctr + 1).append(" ");
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

                jbossQl.append("rct.rctDate<=?").append(ctr + 1).append(" ");
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

            jbossQl.append(" rct.rctAdCompany = ").append(AD_CMPNY).append(" ");

            dcpas = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, 0, 0);
            i = dcpas.iterator();
            dcpaList = new ArrayList();
            addDetails = new CmRepDailyCashPositionSummaryAddDetails();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                addDetails.setDcpsaDescription("RECEIPT");

                String reportType = (String) criteria.get("reportType");

                if (arReceipt.getRctDate().equals(details.getDcpsDate1())) {

                    addDetails.setDcpsaAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount1() + arReceipt.getRctAmountCheque(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate2())) {

                    addDetails.setDcpsaAmount2(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount2() + arReceipt.getRctAmountCheque(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate3())) {

                    addDetails.setDcpsaAmount3(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount3() + arReceipt.getRctAmountCheque(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate4())) {

                    addDetails.setDcpsaAmount4(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount4() + arReceipt.getRctAmountCheque(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate5())) {

                    addDetails.setDcpsaAmount5(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount5() + arReceipt.getRctAmountCheque(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate6())) {

                    addDetails.setDcpsaAmount6(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount6() + arReceipt.getRctAmountCheque(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate7())) {

                    addDetails.setDcpsaAmount7(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount7() + arReceipt.getRctAmountCheque(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }
            }

            if (addDetails.getDcpsaDescription() != null) {

                dcpaList.add(addDetails);
            }

            firstArgument = true;
            ctr = 0;
            criteriaSize = criteria.size() - 3;

            jbossQl = new StringBuilder();

            // get all Card1 receipt

            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct ");

            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

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

                jbossQl.append("rct.adBankAccountCard1.baName=?").append(ctr + 1).append(" ");

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

                    jbossQl.append("rct.rctDate>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");

                    ctr++;

                } else {

                    jbossQl.append("rct.rctDate=?").append(ctr + 1).append(" ");
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

                jbossQl.append("rct.rctDate<=?").append(ctr + 1).append(" ");
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

            jbossQl.append(" rct.rctAdCompany = ").append(AD_CMPNY).append(" ");

            dcpas = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, 0, 0);
            i = dcpas.iterator();
            dcpaList = new ArrayList();
            addDetails = new CmRepDailyCashPositionSummaryAddDetails();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                addDetails.setDcpsaDescription("RECEIPT");

                String reportType = (String) criteria.get("reportType");

                if (arReceipt.getRctDate().equals(details.getDcpsDate1())) {

                    addDetails.setDcpsaAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount1() + arReceipt.getRctAmountCard1(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate2())) {

                    addDetails.setDcpsaAmount2(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount2() + arReceipt.getRctAmountCard1(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate3())) {

                    addDetails.setDcpsaAmount3(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount3() + arReceipt.getRctAmountCard1(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate4())) {

                    addDetails.setDcpsaAmount4(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount4() + arReceipt.getRctAmountCard1(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate5())) {

                    addDetails.setDcpsaAmount5(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount5() + arReceipt.getRctAmountCard1(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate6())) {

                    addDetails.setDcpsaAmount6(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount6() + arReceipt.getRctAmountCard1(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate7())) {

                    addDetails.setDcpsaAmount7(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount7() + arReceipt.getRctAmountCard1(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }
            }

            if (addDetails.getDcpsaDescription() != null) {

                dcpaList.add(addDetails);
            }

            firstArgument = true;
            ctr = 0;
            criteriaSize = criteria.size() - 3;

            jbossQl = new StringBuilder();

            // get all Card2 receipt

            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct ");

            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

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

                jbossQl.append("rct.adBankAccountCard2.baName=?").append(ctr + 1).append(" ");

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

                    jbossQl.append("rct.rctDate>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");

                    ctr++;

                } else {

                    jbossQl.append("rct.rctDate=?").append(ctr + 1).append(" ");
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

                jbossQl.append("rct.rctDate<=?").append(ctr + 1).append(" ");
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

            jbossQl.append(" rct.rctAdCompany = ").append(AD_CMPNY).append(" ");

            dcpas = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, 0, 0);
            i = dcpas.iterator();
            dcpaList = new ArrayList();
            addDetails = new CmRepDailyCashPositionSummaryAddDetails();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                addDetails.setDcpsaDescription("RECEIPT");

                String reportType = (String) criteria.get("reportType");

                if (arReceipt.getRctDate().equals(details.getDcpsDate1())) {

                    addDetails.setDcpsaAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount1() + arReceipt.getRctAmountCard2(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate2())) {

                    addDetails.setDcpsaAmount2(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount2() + arReceipt.getRctAmountCard2(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate3())) {

                    addDetails.setDcpsaAmount3(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount3() + arReceipt.getRctAmountCard2(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate4())) {

                    addDetails.setDcpsaAmount4(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount4() + arReceipt.getRctAmountCard2(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate5())) {

                    addDetails.setDcpsaAmount5(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount5() + arReceipt.getRctAmountCard2(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate6())) {

                    addDetails.setDcpsaAmount6(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount6() + arReceipt.getRctAmountCard2(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate7())) {

                    addDetails.setDcpsaAmount7(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount7() + arReceipt.getRctAmountCard2(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }
            }

            if (addDetails.getDcpsaDescription() != null) {

                dcpaList.add(addDetails);
            }

            firstArgument = true;
            ctr = 0;
            criteriaSize = criteria.size() - 3;

            jbossQl = new StringBuilder();

            // get all Card3 receipt

            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct ");

            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

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

                jbossQl.append("rct.adBankAccountCard3.baName=?").append(ctr + 1).append(" ");

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

                    jbossQl.append("rct.rctDate>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");

                    ctr++;

                } else {

                    jbossQl.append("rct.rctDate=?").append(ctr + 1).append(" ");
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

                jbossQl.append("rct.rctDate<=?").append(ctr + 1).append(" ");
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

            jbossQl.append(" rct.rctAdCompany = ").append(AD_CMPNY).append(" ");

            dcpas = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, 0, 0);
            i = dcpas.iterator();
            dcpaList = new ArrayList();
            addDetails = new CmRepDailyCashPositionSummaryAddDetails();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                addDetails.setDcpsaDescription("RECEIPT");

                String reportType = (String) criteria.get("reportType");

                if (arReceipt.getRctDate().equals(details.getDcpsDate1())) {

                    addDetails.setDcpsaAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount1() + arReceipt.getRctAmountCard3(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate2())) {

                    addDetails.setDcpsaAmount2(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount2() + arReceipt.getRctAmountCard3(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate3())) {

                    addDetails.setDcpsaAmount3(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount3() + arReceipt.getRctAmountCard3(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate4())) {

                    addDetails.setDcpsaAmount4(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount4() + arReceipt.getRctAmountCard3(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate5())) {

                    addDetails.setDcpsaAmount5(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount5() + arReceipt.getRctAmountCard3(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate6())) {

                    addDetails.setDcpsaAmount6(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount6() + arReceipt.getRctAmountCard3(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpsDate7())) {

                    addDetails.setDcpsaAmount7(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpsaAmount7() + arReceipt.getRctAmountCard3(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }
            }

            if (addDetails.getDcpsaDescription() != null) {

                dcpaList.add(addDetails);
            }

            //	get all deposit transfer receipt

            jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(ftr) FROM CmFundTransferReceipt ftr ");

            ctr = 0;
            criteriaSize = criteria.size() - 3;
            firstArgument = true;
            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("bankAccountCode")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ftr.cmFundTransfer.ftAdBaAccountTo=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("bankAccountCode");
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

                    jbossQl.append("ftr.cmFundTransfer.ftDate>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");
                    ctr++;

                } else {

                    jbossQl.append("ftr.cmFundTransfer.ftDate=?").append(ctr + 1).append(" ");
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

                jbossQl.append("ftr.cmFundTransfer.ftDate<=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("ftr.cmFundTransfer.ftPosted = 1 ");

                } else {

                    jbossQl.append("ftr.cmFundTransfer.ftVoid = 0 ");
                }
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(" ftr.ftrAdCompany = ").append(AD_CMPNY).append(" ");

            dcpas = cmFundTransferReceiptHome.getFtrByCriteria(jbossQl.toString(), obj);
            i = dcpas.iterator();
            addDetails = new CmRepDailyCashPositionSummaryAddDetails();

            while (i.hasNext()) {

                LocalCmFundTransferReceipt cmFundTransferReceipt = (LocalCmFundTransferReceipt) i.next();
                addDetails.setDcpsaDescription("DEPOSITED RECEIPT");
                String reportType = (String) criteria.get("reportType");

                if (cmFundTransferReceipt.getCmFundTransfer().getFtDate().equals(details.getDcpsDate1())) {

                    addDetails.setDcpsaAmount1(EJBCommon.roundIt(addDetails.getDcpsaAmount1() + cmFundTransferReceipt.getFtrAmountDeposited(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransferReceipt.getCmFundTransfer().getFtDate().equals(details.getDcpsDate2())) {

                    addDetails.setDcpsaAmount2(EJBCommon.roundIt(addDetails.getDcpsaAmount2() + cmFundTransferReceipt.getFtrAmountDeposited(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransferReceipt.getCmFundTransfer().getFtDate().equals(details.getDcpsDate3())) {

                    addDetails.setDcpsaAmount3(EJBCommon.roundIt(addDetails.getDcpsaAmount3() + cmFundTransferReceipt.getFtrAmountDeposited(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransferReceipt.getCmFundTransfer().getFtDate().equals(details.getDcpsDate4())) {

                    addDetails.setDcpsaAmount4(EJBCommon.roundIt(addDetails.getDcpsaAmount4() + cmFundTransferReceipt.getFtrAmountDeposited(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransferReceipt.getCmFundTransfer().getFtDate().equals(details.getDcpsDate5())) {

                    addDetails.setDcpsaAmount5(EJBCommon.roundIt(addDetails.getDcpsaAmount5() + cmFundTransferReceipt.getFtrAmountDeposited(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransferReceipt.getCmFundTransfer().getFtDate().equals(details.getDcpsDate6())) {

                    addDetails.setDcpsaAmount6(EJBCommon.roundIt(addDetails.getDcpsaAmount6() + cmFundTransferReceipt.getFtrAmountDeposited(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransferReceipt.getCmFundTransfer().getFtDate().equals(details.getDcpsDate7())) {

                    addDetails.setDcpsaAmount7(EJBCommon.roundIt(addDetails.getDcpsaAmount7() + cmFundTransferReceipt.getFtrAmountDeposited(), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }
            }

            if (addDetails.getDcpsaDescription() != null) {

                dcpaList.add(addDetails);
            }

            //	get all fund transfer to

            jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(ft) FROM CmFundTransfer ft ");

            ctr = 0;
            criteriaSize = criteria.size() - 3;
            firstArgument = true;
            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("bankAccountCode")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ft.ftAdBaAccountTo=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("bankAccountCode");
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

                    jbossQl.append("ft.ftDate>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");
                    ctr++;

                } else {

                    jbossQl.append("ft.ftDate=?").append(ctr + 1).append(" ");
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

                jbossQl.append("ft.ftDate<=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("ft.ftPosted = 1 ");

                } else {

                    jbossQl.append("ft.ftVoid = 0 ");
                }
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(" ft.ftAdCompany = ").append(AD_CMPNY).append(" ");

            dcpas = cmFundTransferHome.getFtByCriteria(jbossQl.toString(), obj, 0, 0);
            i = dcpas.iterator();
            addDetails = new CmRepDailyCashPositionSummaryAddDetails();

            while (i.hasNext()) {

                LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) i.next();

                Collection cmFundTransferReceipts = cmFundTransfer.getCmFundTransferReceipts();

                if (cmFundTransferReceipts.isEmpty()) {

                    addDetails.setDcpsaDescription("FUND TRANSFER");

                    String reportType = (String) criteria.get("reportType");

                    if (cmFundTransfer.getFtDate().equals(details.getDcpsDate1())) {

                        addDetails.setDcpsaAmount1(EJBCommon.roundIt(addDetails.getDcpsaAmount1() + cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (cmFundTransfer.getFtDate().equals(details.getDcpsDate2())) {

                        addDetails.setDcpsaAmount2(EJBCommon.roundIt(addDetails.getDcpsaAmount2() + cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (cmFundTransfer.getFtDate().equals(details.getDcpsDate3())) {

                        addDetails.setDcpsaAmount3(EJBCommon.roundIt(addDetails.getDcpsaAmount3() + cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (cmFundTransfer.getFtDate().equals(details.getDcpsDate4())) {

                        addDetails.setDcpsaAmount4(EJBCommon.roundIt(addDetails.getDcpsaAmount4() + cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (cmFundTransfer.getFtDate().equals(details.getDcpsDate5())) {

                        addDetails.setDcpsaAmount5(EJBCommon.roundIt(addDetails.getDcpsaAmount5() + cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (cmFundTransfer.getFtDate().equals(details.getDcpsDate6())) {

                        addDetails.setDcpsaAmount6(EJBCommon.roundIt(addDetails.getDcpsaAmount6() + cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (cmFundTransfer.getFtDate().equals(details.getDcpsDate7())) {

                        addDetails.setDcpsaAmount7(EJBCommon.roundIt(addDetails.getDcpsaAmount7() + cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                    }
                }
            }

            if (addDetails.getDcpsaDescription() != null) {

                dcpaList.add(addDetails);
            }

            //	get all positve adjustments

            jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(adj) FROM CmAdjustment adj ");

            ctr = 0;
            criteriaSize = criteria.size() - 3;
            firstArgument = true;
            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

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

                jbossQl.append("adj.adBankAccount.baName=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("adj.adjDate>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");
                    ctr++;

                } else {

                    jbossQl.append("adj.adjDate=?").append(ctr + 1).append(" ");
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

                jbossQl.append("adj.adjDate<=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("adj.adjPosted = 1 ");

                } else {

                    jbossQl.append("adj.adjVoid = 0 ");
                }
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(" (adj.adjType = 'INTEREST' OR adj.adjType = 'DEBIT MEMO' OR adj.adjType = 'ADVANCE') AND adj.adjAdCompany = ").append(AD_CMPNY).append(" ");

            dcpas = cmAdjustmentHome.getAdjByCriteria(jbossQl.toString(), obj, 0, 0);
            i = dcpas.iterator();
            addDetails = new CmRepDailyCashPositionSummaryAddDetails();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                addDetails.setDcpsaDescription("ADJUSTMENT");

                String reportType = (String) criteria.get("reportType");

                if (cmAdjustment.getAdjDate().equals(details.getDcpsDate1())) {

                    addDetails.setDcpsaAmount1(EJBCommon.roundIt(addDetails.getDcpsaAmount1() + cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpsDate2())) {

                    addDetails.setDcpsaAmount2(EJBCommon.roundIt(addDetails.getDcpsaAmount2() + cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpsDate3())) {

                    addDetails.setDcpsaAmount3(EJBCommon.roundIt(addDetails.getDcpsaAmount3() + cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpsDate4())) {

                    addDetails.setDcpsaAmount4(EJBCommon.roundIt(addDetails.getDcpsaAmount4() + cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpsDate5())) {

                    addDetails.setDcpsaAmount5(EJBCommon.roundIt(addDetails.getDcpsaAmount5() + cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpsDate6())) {

                    addDetails.setDcpsaAmount6(EJBCommon.roundIt(addDetails.getDcpsaAmount6() + cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpsDate7())) {

                    addDetails.setDcpsaAmount7(EJBCommon.roundIt(addDetails.getDcpsaAmount7() + cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }
            }

            if (addDetails.getDcpsaDescription() != null) {

                dcpaList.add(addDetails);
            }

            return dcpaList;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private ArrayList executeCmRepDailyCashPositionSummaryLess(HashMap criteria, CmRepDailyCashPositionSummaryDetails details, boolean includeDirectChecks, Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashPositionSummaryControllerBean executeCmRepDailyCashPositionSummaryLess");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size() - 3;

            StringBuilder jbossQl = new StringBuilder();

            // get all receipt

            jbossQl.append("SELECT OBJECT(chk) FROM ApCheck chk ");

            firstArgument = true;

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

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

                jbossQl.append("chk.adBankAccount.baName=?").append(ctr + 1).append(" ");
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

                String unposted = (String) criteria.get("includedUnposted");

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (unposted.equals("NO")) {

                    jbossQl.append("chk.chkPosted = 1 AND chk.chkVoid = 0 ");

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

            if (includeDirectChecks == true) {
                jbossQl.append("(chk.chkReleased = 1 OR (chk.chkType='DIRECT' AND chk.chkReleased = 0)) ");

            } else {
                jbossQl.append("chk.chkReleased = 1 ");
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(" chk.chkAdCompany = ").append(AD_CMPNY).append(" ");

            Collection dcpls = apCheckHome.getChkByCriteria(jbossQl.toString(), obj, 0, 0);
            Debug.print("jbossQl.toString(): " + jbossQl);
            Iterator i = dcpls.iterator();
            ArrayList dcplList = new ArrayList();
            CmRepDailyCashPositionSummaryLessDetails lessDetails = new CmRepDailyCashPositionSummaryLessDetails();

            while (i.hasNext()) {

                LocalApCheck apCheck = (LocalApCheck) i.next();
                lessDetails.setDcpslDescription("RELEASED CHECK");

                String reportType = (String) criteria.get("reportType");

                if (apCheck.getChkDate().equals(details.getDcpsDate1())) {

                    lessDetails.setDcpslAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), lessDetails.getDcpslAmount1() + apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (apCheck.getChkDate().equals(details.getDcpsDate2())) {

                    lessDetails.setDcpslAmount2(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), lessDetails.getDcpslAmount2() + apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (apCheck.getChkDate().equals(details.getDcpsDate3())) {

                    lessDetails.setDcpslAmount3(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), lessDetails.getDcpslAmount3() + apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (apCheck.getChkDate().equals(details.getDcpsDate4())) {

                    lessDetails.setDcpslAmount4(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), lessDetails.getDcpslAmount4() + apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (apCheck.getChkDate().equals(details.getDcpsDate5())) {

                    lessDetails.setDcpslAmount5(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), lessDetails.getDcpslAmount5() + apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (apCheck.getChkDate().equals(details.getDcpsDate6())) {

                    lessDetails.setDcpslAmount6(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), lessDetails.getDcpslAmount6() + apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (apCheck.getChkDate().equals(details.getDcpsDate7())) {

                    lessDetails.setDcpslAmount7(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), lessDetails.getDcpslAmount7() + apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }
            }

            if (lessDetails.getDcpslDescription() != null) {

                dcplList.add(lessDetails);
            }

            //	get all fund transfer from

            jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(ft) FROM CmFundTransfer ft ");

            ctr = 0;
            criteriaSize = criteria.size() - 3;
            firstArgument = true;
            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("bankAccountCode")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ft.ftAdBaAccountFrom=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("bankAccountCode");
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

                    jbossQl.append("ft.ftDate>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");
                    ctr++;

                } else {

                    jbossQl.append("ft.ftDate=?").append(ctr + 1).append(" ");
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

                jbossQl.append("ft.ftDate<=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("ft.ftPosted = 1 ");

                } else {

                    jbossQl.append("ft.ftVoid = 0 ");
                }
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(" ft.ftAdCompany = ").append(AD_CMPNY).append(" ");

            dcpls = cmFundTransferHome.getFtByCriteria(jbossQl.toString(), obj, 0, 0);
            i = dcpls.iterator();
            lessDetails = new CmRepDailyCashPositionSummaryLessDetails();

            while (i.hasNext()) {

                LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) i.next();

                lessDetails.setDcpslDescription("FUND TRANSFER");

                String reportType = (String) criteria.get("reportType");

                if (cmFundTransfer.getFtDate().equals(details.getDcpsDate1())) {

                    lessDetails.setDcpslAmount1(EJBCommon.roundIt(lessDetails.getDcpslAmount1() + cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransfer.getFtDate().equals(details.getDcpsDate2())) {

                    lessDetails.setDcpslAmount2(EJBCommon.roundIt(lessDetails.getDcpslAmount2() + cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransfer.getFtDate().equals(details.getDcpsDate3())) {

                    lessDetails.setDcpslAmount3(EJBCommon.roundIt(lessDetails.getDcpslAmount3() + cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransfer.getFtDate().equals(details.getDcpsDate4())) {

                    lessDetails.setDcpslAmount4(EJBCommon.roundIt(lessDetails.getDcpslAmount4() + cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransfer.getFtDate().equals(details.getDcpsDate5())) {

                    lessDetails.setDcpslAmount5(EJBCommon.roundIt(lessDetails.getDcpslAmount5() + cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransfer.getFtDate().equals(details.getDcpsDate6())) {

                    lessDetails.setDcpslAmount6(EJBCommon.roundIt(lessDetails.getDcpslAmount6() + cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransfer.getFtDate().equals(details.getDcpsDate7())) {

                    lessDetails.setDcpslAmount7(EJBCommon.roundIt(lessDetails.getDcpslAmount7() + cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }
            }

            if (lessDetails.getDcpslDescription() != null) {

                dcplList.add(lessDetails);
            }

            //	get all negative adjustments

            jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(adj) FROM CmAdjustment adj ");

            ctr = 0;
            criteriaSize = criteria.size() - 3;
            firstArgument = true;
            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

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

                jbossQl.append("adj.adBankAccount.baName=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("adj.adjDate>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");
                    ctr++;

                } else {

                    jbossQl.append("adj.adjDate=?").append(ctr + 1).append(" ");
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

                jbossQl.append("adj.adjDate<=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("adj.adjPosted = 1 ");

                } else {

                    jbossQl.append("adj.adjVoid = 0 ");
                }
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(" (adj.adjType = 'BANK CHARGE' OR adj.adjType = 'CREDIT MEMO') AND adj.adjAdCompany = ").append(AD_CMPNY).append(" ");

            dcpls = cmAdjustmentHome.getAdjByCriteria(jbossQl.toString(), obj, 0, 0);
            i = dcpls.iterator();
            lessDetails = new CmRepDailyCashPositionSummaryLessDetails();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                lessDetails.setDcpslDescription("ADJUSTMENT");

                String reportType = (String) criteria.get("reportType");

                if (cmAdjustment.getAdjDate().equals(details.getDcpsDate1())) {

                    lessDetails.setDcpslAmount1(EJBCommon.roundIt(lessDetails.getDcpslAmount1() + cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpsDate2())) {

                    lessDetails.setDcpslAmount2(EJBCommon.roundIt(lessDetails.getDcpslAmount2() + cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpsDate3())) {

                    lessDetails.setDcpslAmount3(EJBCommon.roundIt(lessDetails.getDcpslAmount3() + cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpsDate4())) {

                    lessDetails.setDcpslAmount4(EJBCommon.roundIt(lessDetails.getDcpslAmount4() + cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpsDate5())) {

                    lessDetails.setDcpslAmount5(EJBCommon.roundIt(lessDetails.getDcpslAmount5() + cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpsDate6())) {

                    lessDetails.setDcpslAmount6(EJBCommon.roundIt(lessDetails.getDcpslAmount6() + cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpsDate7())) {

                    lessDetails.setDcpslAmount7(EJBCommon.roundIt(lessDetails.getDcpslAmount7() + cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }
            }

            if (lessDetails.getDcpslDescription() != null) {

                dcplList.add(lessDetails);
            }

            return dcplList;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private CmRepDailyCashPositionSummaryDetails computeBeginningBalancesAndAvailableCashBalance(CmRepDailyCashPositionSummaryDetails details, Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashPositionSummaryControllerBean computeBeginningBalancesAndAvailableCashBalance");

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

        ArrayList list = details.getDcpsAddList();
        Iterator i = list.iterator();

        while (i.hasNext()) {

            CmRepDailyCashPositionSummaryAddDetails addDetails = (CmRepDailyCashPositionSummaryAddDetails) i.next();

            TOTAL_AMOUNT1 = TOTAL_AMOUNT1 + addDetails.getDcpsaAmount1();
            TOTAL_AMOUNT2 = TOTAL_AMOUNT2 + addDetails.getDcpsaAmount2();
            TOTAL_AMOUNT3 = TOTAL_AMOUNT3 + addDetails.getDcpsaAmount3();
            TOTAL_AMOUNT4 = TOTAL_AMOUNT4 + addDetails.getDcpsaAmount4();
            TOTAL_AMOUNT5 = TOTAL_AMOUNT5 + addDetails.getDcpsaAmount5();
            TOTAL_AMOUNT6 = TOTAL_AMOUNT6 + addDetails.getDcpsaAmount6();
            TOTAL_AMOUNT7 = TOTAL_AMOUNT7 + addDetails.getDcpsaAmount7();
        }

        list = details.getDcpsLessList();
        i = list.iterator();

        while (i.hasNext()) {

            CmRepDailyCashPositionSummaryLessDetails lessDetails = (CmRepDailyCashPositionSummaryLessDetails) i.next();

            TOTAL_AMOUNT1 = TOTAL_AMOUNT1 - lessDetails.getDcpslAmount1();
            TOTAL_AMOUNT2 = TOTAL_AMOUNT2 - lessDetails.getDcpslAmount2();
            TOTAL_AMOUNT3 = TOTAL_AMOUNT3 - lessDetails.getDcpslAmount3();
            TOTAL_AMOUNT4 = TOTAL_AMOUNT4 - lessDetails.getDcpslAmount4();
            TOTAL_AMOUNT5 = TOTAL_AMOUNT5 - lessDetails.getDcpslAmount5();
            TOTAL_AMOUNT6 = TOTAL_AMOUNT6 - lessDetails.getDcpslAmount6();
            TOTAL_AMOUNT7 = TOTAL_AMOUNT7 - lessDetails.getDcpslAmount7();
        }

        details.setDcpsAvailableCashBalance1(EJBCommon.roundIt(details.getDcpsBeginningBalance1() + TOTAL_AMOUNT1, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcpsBeginningBalance2(EJBCommon.roundIt(details.getDcpsAvailableCashBalance1(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcpsAvailableCashBalance2(EJBCommon.roundIt(details.getDcpsBeginningBalance2() + TOTAL_AMOUNT2, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcpsBeginningBalance3(EJBCommon.roundIt(details.getDcpsAvailableCashBalance2(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcpsAvailableCashBalance3(EJBCommon.roundIt(details.getDcpsBeginningBalance3() + TOTAL_AMOUNT3, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcpsBeginningBalance4(EJBCommon.roundIt(details.getDcpsAvailableCashBalance3(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcpsAvailableCashBalance4(EJBCommon.roundIt(details.getDcpsBeginningBalance4() + TOTAL_AMOUNT4, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcpsBeginningBalance5(EJBCommon.roundIt(details.getDcpsAvailableCashBalance4(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcpsAvailableCashBalance5(EJBCommon.roundIt(details.getDcpsBeginningBalance5() + TOTAL_AMOUNT5, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcpsBeginningBalance6(EJBCommon.roundIt(details.getDcpsAvailableCashBalance5(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcpsAvailableCashBalance6(EJBCommon.roundIt(details.getDcpsBeginningBalance6() + TOTAL_AMOUNT6, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcpsBeginningBalance7(EJBCommon.roundIt(details.getDcpsAvailableCashBalance6(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcpsAvailableCashBalance7(EJBCommon.roundIt(details.getDcpsBeginningBalance7() + TOTAL_AMOUNT7, adCompany.getGlFunctionalCurrency().getFcPrecision()));

        return details;
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("CmRepDailyCashPositionSummaryControllerBean ejbCreate");
    }
}