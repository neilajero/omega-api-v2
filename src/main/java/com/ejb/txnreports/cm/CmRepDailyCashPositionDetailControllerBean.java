/**
 * @copyright 2022 Omega Business Consulting, Inc.
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
import com.ejb.dao.ad.*;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.dao.cm.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.entities.cm.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.ad.AdCompanyDetails;
import com.util.reports.cm.CmRepDailyCashPositionDetailAddDetails;
import com.util.reports.cm.CmRepDailyCashPositionDetailDetails;
import com.util.reports.cm.CmRepDailyCashPositionDetailLessDetails;

@Stateless(name = "CmRepDailyCashPositionDetailControllerEJB")
public class CmRepDailyCashPositionDetailControllerBean extends EJBContextClass implements CmRepDailyCashPositionDetailController {

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

        Debug.print("CmRepDailyCashPositionDetailControllerBean getAdBaAll");

        LocalAdBankAccount adBankAccount = null;
        ArrayList list = new ArrayList();
        try {
            Collection adBankAccounts = adBankAccountHome.findEnabledBaAll(AD_BRNCH, AD_CMPNY);
            for (Object bankAccount : adBankAccounts) {
                adBankAccount = (LocalAdBankAccount) bankAccount;
                if (adBankAccount.getBaIsCashAccount() == EJBCommon.FALSE) {
                    list.add(adBankAccount.getBaName());
                }
            }
            return list;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public CmRepDailyCashPositionDetailDetails executeCmRepDailyCashPositionDetail(HashMap criteria, boolean includeDirectChecks, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmRepDailyCashPositionDetailControllerBean executeCmRepDailyCashPositionDetail");

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

            CmRepDailyCashPositionDetailDetails mdetails = new CmRepDailyCashPositionDetailDetails();
            switch (reportType) {
                case "ONE DAY":
                    mdetails.setDcpdDate1(date);
                    break;
                case "FIVE DAYS":
                    mdetails.setDcpdDate5(date);
                    mdetails.setDcpdDate4(computeDate(mdetails.getDcpdDate5()));
                    mdetails.setDcpdDate3(computeDate(mdetails.getDcpdDate4()));
                    mdetails.setDcpdDate2(computeDate(mdetails.getDcpdDate3()));
                    mdetails.setDcpdDate1(computeDate(mdetails.getDcpdDate2()));
                    criteria.put("dateTo", mdetails.getDcpdDate5());
                    break;
                case "SEVEN DAYS":
                    mdetails.setDcpdDate7(date);
                    mdetails.setDcpdDate6(computeDate(date));
                    mdetails.setDcpdDate5(computeDate(mdetails.getDcpdDate6()));
                    mdetails.setDcpdDate4(computeDate(mdetails.getDcpdDate5()));
                    mdetails.setDcpdDate3(computeDate(mdetails.getDcpdDate4()));
                    mdetails.setDcpdDate2(computeDate(mdetails.getDcpdDate3()));
                    mdetails.setDcpdDate1(computeDate(mdetails.getDcpdDate2()));
                    criteria.put("dateTo", mdetails.getDcpdDate7());
                    break;
            }

            criteria.put("dateFrom", mdetails.getDcpdDate1());

            mdetails.setDcpdAddList(executeCmRepDailyCashPositionDetailAdd(criteria, mdetails, AD_CMPNY));
            mdetails.setDcpdLessList(executeCmRepDailyCashPositionDetailLess(criteria, mdetails, includeDirectChecks, AD_CMPNY));

            if (mdetails.getDcpdAddList().isEmpty() && mdetails.getDcpdLessList().isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeDateAndBaCodeAndType(mdetails.getDcpdDate1(), adBankAccount.getBaCode(), "BOOK", AD_CMPNY);

            LocalAdBankAccountBalance adBankAccountBalance = null;
            if (!adBankAccountBalances.isEmpty()) {
                for (Object bankAccountBalance : adBankAccountBalances) {
                    adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;
                }
            }

            double BEG_BALANCE = (adBankAccountBalance != null ? adBankAccountBalance.getBabBalance() : 0d);

            // Get Unposted Balance
            if (criteria.containsKey("includedUnposted")) {
                String unposted = (String) criteria.get("includedUnposted");
                if (!unposted.equals("NO")) {
                    double ADD_BAL = this.getUnpostedCmRepDailyCashPositionDetailAdd(criteria, mdetails.getDcpdDate1(), AD_CMPNY);
                    double LESS_BAL = this.getUnpostedCmRepDailyCashPositionDetailLess(criteria, mdetails.getDcpdDate1(), includeDirectChecks, AD_CMPNY);
                    BEG_BALANCE = BEG_BALANCE + ADD_BAL - LESS_BAL;
                }
            }

            mdetails.setDcpdBeginningBalance1(BEG_BALANCE);
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
        Debug.print("CmRepDailyCashPositionDetailControllerBean getAdCompany");
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

        Debug.print("CmRepDailyCashPositionDetailControllerBean convertForeignToFunctionalCurrency");

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

        Debug.print("CmRepDailyCashPositionDetailControllerBean computeDate");

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateEntered);
        calendar.add(GregorianCalendar.DATE, -1);

        return calendar.getTime();
    }

    private double getUnpostedCmRepDailyCashPositionDetailAdd(HashMap criteria, Date startDate, Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashPositionDetailControllerBean getCmRepDailyCashPositionDetailAdd");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size() - 3;
            double returnAmount = 0;

            StringBuilder jbossQl = new StringBuilder();

            // get all receipt

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
                Debug.print("OBJ1=" + obj[ctr]);

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
                    obj[ctr] = startDate;
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
                obj[ctr] = startDate;
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

                jbossQl.append(" rct.rctPosted = 0 AND rct.rctVoid = 0 ");
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(" rct.rctAdCompany = ").append(AD_CMPNY).append(" ");
            Debug.print("Criteria=" + criteria);
            Debug.print("jbossQl=" + jbossQl);
            Collection dcpas = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, 0, 0);
            Debug.print("----------->1");
            Iterator i = dcpas.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                returnAmount += EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());
            }

            //	get all deposit transfer receipt
            Debug.print("----------->2");
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

                jbossQl.append(" ftr.cmFundTransfer.ftPosted = 0  AND ftr.cmFundTransfer.ftVoid = 0 ");
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

            while (i.hasNext()) {
                LocalCmFundTransferReceipt cmFundTransferReceipt = (LocalCmFundTransferReceipt) i.next();

                returnAmount += EJBCommon.roundIt(cmFundTransferReceipt.getFtrAmountDeposited(), adCompany.getGlFunctionalCurrency().getFcPrecision());
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

                jbossQl.append("ft.ftVoid = 0 ");
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

            while (i.hasNext()) {
                LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) i.next();

                Collection cmFundTransferReceipts = cmFundTransfer.getCmFundTransferReceipts();

                if (cmFundTransferReceipts.isEmpty()) {
                    returnAmount += EJBCommon.roundIt(cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision());
                }
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

                jbossQl.append(" adj.adjPosted = 0 AND adj.adjVoid = 0 ");
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(" (adj.adjType = 'INTEREST' OR adj.adjType = 'DEBIT MEMO') AND adj.adjAdCompany = ").append(AD_CMPNY).append(" ");

            dcpas = cmAdjustmentHome.getAdjByCriteria(jbossQl.toString(), obj, 0, 0);

            i = dcpas.iterator();

            while (i.hasNext()) {
                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                returnAmount += EJBCommon.roundIt(cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision());
            }

            return returnAmount;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getUnpostedCmRepDailyCashPositionDetailLess(HashMap criteria, Date startDate, boolean includeDirectChecks, Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashPositionDetailControllerBean getUnpostedCmRepDailyCashPositionDetailLess");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size() - 3;
            double returnAmount = 0;

            StringBuilder jbossQl = new StringBuilder();

            // get all released checks

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

                jbossQl.append(" chk.chkPosted = 0 AND chk.chkVoid = 0 ");
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

            jbossQl.append("chk.chkAdCompany = ").append(AD_CMPNY).append(" ");
            Debug.print("jbossQl.toString()=" + jbossQl);
            Collection dcpls = apCheckHome.getChkByCriteria(jbossQl.toString(), obj, 0, 0);
            Debug.print("------------------>1");
            Iterator i = dcpls.iterator();
            ArrayList dcplList = new ArrayList();

            while (i.hasNext()) {

                LocalApCheck apCheck = (LocalApCheck) i.next();

                returnAmount += EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());
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

                jbossQl.append(" ft.ftPosted = 0 AND ft.ftVoid = 0 ");
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

            while (i.hasNext()) {

                LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) i.next();

                CmRepDailyCashPositionDetailLessDetails lessDetails = new CmRepDailyCashPositionDetailLessDetails();
                returnAmount += EJBCommon.roundIt(cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision());
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

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append(" adj.adjPosted = 0 AND adj.adjVoid = 0");
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

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                CmRepDailyCashPositionDetailLessDetails lessDetails = new CmRepDailyCashPositionDetailLessDetails();

                returnAmount += EJBCommon.roundIt(lessDetails.getDcpdlAmount1() + cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision());
            }

            return returnAmount;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private ArrayList executeCmRepDailyCashPositionDetailAdd(HashMap criteria, CmRepDailyCashPositionDetailDetails details, Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashPositionDetailControllerBean executeCmRepDailyCashPositionDetailAdd");

        ArrayList list = new ArrayList();

        try {
            // Cash Receipt
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size() - 3;

            StringBuilder jbossQl = new StringBuilder();

            // get all receipt

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

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                CmRepDailyCashPositionDetailAddDetails addDetails = new CmRepDailyCashPositionDetailAddDetails();

                addDetails.setDcpdaCustomer(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                addDetails.setDcpdaDateTransaction(arReceipt.getRctDate());
                addDetails.setDcpdaDescription(arReceipt.getRctDescription());
                addDetails.setDcpdaReceiptNumber(arReceipt.getRctNumber());

                String reportType = (String) criteria.get("reportType");

                if (arReceipt.getRctDate().equals(details.getDcpdDate1())) {

                    addDetails.setDcpdaAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCash(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate2())) {

                    addDetails.setDcpdaAmount2(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCash(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate3())) {

                    addDetails.setDcpdaAmount3(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCash(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate4())) {

                    addDetails.setDcpdaAmount4(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpdaAmount4() + arReceipt.getRctAmountCash(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate5())) {

                    addDetails.setDcpdaAmount5(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCash(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate6())) {

                    addDetails.setDcpdaAmount6(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCash(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate7())) {

                    addDetails.setDcpdaAmount7(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCash(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }

                dcpaList.add(addDetails);
            }

            // Cheque Receipt

            firstArgument = true;
            ctr = 0;
            criteriaSize = criteria.size() - 3;

            jbossQl = new StringBuilder();

            // get all receipt

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

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                CmRepDailyCashPositionDetailAddDetails addDetails = new CmRepDailyCashPositionDetailAddDetails();

                addDetails.setDcpdaCustomer(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                addDetails.setDcpdaDateTransaction(arReceipt.getRctDate());
                addDetails.setDcpdaDescription(arReceipt.getRctDescription());
                addDetails.setDcpdaReceiptNumber(arReceipt.getRctNumber());

                String reportType = (String) criteria.get("reportType");

                if (arReceipt.getRctDate().equals(details.getDcpdDate1())) {

                    addDetails.setDcpdaAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCheque(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate2())) {

                    addDetails.setDcpdaAmount2(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCheque(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate3())) {

                    addDetails.setDcpdaAmount3(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCheque(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate4())) {

                    addDetails.setDcpdaAmount4(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpdaAmount4() + arReceipt.getRctAmountCheque(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate5())) {

                    addDetails.setDcpdaAmount5(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCheque(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate6())) {

                    addDetails.setDcpdaAmount6(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCheque(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate7())) {

                    addDetails.setDcpdaAmount7(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCheque(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }

                dcpaList.add(addDetails);
            }

            // Card1 Receipt

            firstArgument = true;
            ctr = 0;
            criteriaSize = criteria.size() - 3;

            jbossQl = new StringBuilder();

            // get all receipt

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

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                CmRepDailyCashPositionDetailAddDetails addDetails = new CmRepDailyCashPositionDetailAddDetails();

                addDetails.setDcpdaCustomer(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                addDetails.setDcpdaDateTransaction(arReceipt.getRctDate());
                addDetails.setDcpdaDescription(arReceipt.getRctDescription());
                addDetails.setDcpdaReceiptNumber(arReceipt.getRctNumber());

                String reportType = (String) criteria.get("reportType");

                if (arReceipt.getRctDate().equals(details.getDcpdDate1())) {

                    addDetails.setDcpdaAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard1(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate2())) {

                    addDetails.setDcpdaAmount2(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard1(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate3())) {

                    addDetails.setDcpdaAmount3(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard1(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate4())) {

                    addDetails.setDcpdaAmount4(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpdaAmount4() + arReceipt.getRctAmountCard1(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate5())) {

                    addDetails.setDcpdaAmount5(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard1(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate6())) {

                    addDetails.setDcpdaAmount6(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard1(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate7())) {

                    addDetails.setDcpdaAmount7(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard1(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }

                dcpaList.add(addDetails);
            }

            // Card2 Receipt

            firstArgument = true;
            ctr = 0;
            criteriaSize = criteria.size() - 3;

            jbossQl = new StringBuilder();

            // get all receipt

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

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                CmRepDailyCashPositionDetailAddDetails addDetails = new CmRepDailyCashPositionDetailAddDetails();

                addDetails.setDcpdaCustomer(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                addDetails.setDcpdaDateTransaction(arReceipt.getRctDate());
                addDetails.setDcpdaDescription(arReceipt.getRctDescription());
                addDetails.setDcpdaReceiptNumber(arReceipt.getRctNumber());

                String reportType = (String) criteria.get("reportType");

                if (arReceipt.getRctDate().equals(details.getDcpdDate1())) {

                    addDetails.setDcpdaAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard2(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate2())) {

                    addDetails.setDcpdaAmount2(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard2(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate3())) {

                    addDetails.setDcpdaAmount3(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard2(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate4())) {

                    addDetails.setDcpdaAmount4(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpdaAmount4() + arReceipt.getRctAmountCard2(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate5())) {

                    addDetails.setDcpdaAmount5(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard2(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate6())) {

                    addDetails.setDcpdaAmount6(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard2(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate7())) {

                    addDetails.setDcpdaAmount7(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard2(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }

                dcpaList.add(addDetails);
            }

            // Card3 Receipt

            firstArgument = true;
            ctr = 0;
            criteriaSize = criteria.size() - 3;

            jbossQl = new StringBuilder();

            // get all receipt

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

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                CmRepDailyCashPositionDetailAddDetails addDetails = new CmRepDailyCashPositionDetailAddDetails();

                addDetails.setDcpdaCustomer(arReceipt.getRctCustomerName() == null ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                addDetails.setDcpdaDateTransaction(arReceipt.getRctDate());
                addDetails.setDcpdaDescription(arReceipt.getRctDescription());
                addDetails.setDcpdaReceiptNumber(arReceipt.getRctNumber());

                String reportType = (String) criteria.get("reportType");

                if (arReceipt.getRctDate().equals(details.getDcpdDate1())) {

                    addDetails.setDcpdaAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard3(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate2())) {

                    addDetails.setDcpdaAmount2(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard3(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate3())) {

                    addDetails.setDcpdaAmount3(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard3(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate4())) {

                    addDetails.setDcpdaAmount4(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), addDetails.getDcpdaAmount4() + arReceipt.getRctAmountCard3(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate5())) {

                    addDetails.setDcpdaAmount5(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard3(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate6())) {

                    addDetails.setDcpdaAmount6(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard3(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (arReceipt.getRctDate().equals(details.getDcpdDate7())) {

                    addDetails.setDcpdaAmount7(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arReceipt.getGlFunctionalCurrency().getFcCode(), arReceipt.getGlFunctionalCurrency().getFcName(), arReceipt.getRctConversionDate(), arReceipt.getRctConversionRate(), arReceipt.getRctAmountCard3(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }

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

            while (i.hasNext()) {

                LocalCmFundTransferReceipt cmFundTransferReceipt = (LocalCmFundTransferReceipt) i.next();

                CmRepDailyCashPositionDetailAddDetails addDetails = new CmRepDailyCashPositionDetailAddDetails();

                // addDetails.setDcpdaCustomer(cmFundTransferReceipt.getArReceipt().getArCustomer().getCstName());
                addDetails.setDcpdaCustomer(cmFundTransferReceipt.getArReceipt().getRctCustomerName() == null ? cmFundTransferReceipt.getArReceipt().getArCustomer().getCstName() : cmFundTransferReceipt.getArReceipt().getRctCustomerName());
                addDetails.setDcpdaDateTransaction(cmFundTransferReceipt.getCmFundTransfer().getFtDate());
                addDetails.setDcpdaDescription("DEPOSITED RECEIPT");
                addDetails.setDcpdaReceiptNumber(cmFundTransferReceipt.getArReceipt().getRctNumber());

                String reportType = (String) criteria.get("reportType");

                if (cmFundTransferReceipt.getCmFundTransfer().getFtDate().equals(details.getDcpdDate1())) {

                    addDetails.setDcpdaAmount1(EJBCommon.roundIt(cmFundTransferReceipt.getFtrAmountDeposited(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransferReceipt.getCmFundTransfer().getFtDate().equals(details.getDcpdDate2())) {

                    addDetails.setDcpdaAmount2(EJBCommon.roundIt(cmFundTransferReceipt.getFtrAmountDeposited(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransferReceipt.getCmFundTransfer().getFtDate().equals(details.getDcpdDate3())) {

                    addDetails.setDcpdaAmount3(EJBCommon.roundIt(cmFundTransferReceipt.getFtrAmountDeposited(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransferReceipt.getCmFundTransfer().getFtDate().equals(details.getDcpdDate4())) {

                    addDetails.setDcpdaAmount4(EJBCommon.roundIt(cmFundTransferReceipt.getFtrAmountDeposited(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransferReceipt.getCmFundTransfer().getFtDate().equals(details.getDcpdDate5())) {

                    addDetails.setDcpdaAmount5(EJBCommon.roundIt(cmFundTransferReceipt.getFtrAmountDeposited(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransferReceipt.getCmFundTransfer().getFtDate().equals(details.getDcpdDate6())) {

                    addDetails.setDcpdaAmount6(EJBCommon.roundIt(cmFundTransferReceipt.getFtrAmountDeposited(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransferReceipt.getCmFundTransfer().getFtDate().equals(details.getDcpdDate7())) {

                    addDetails.setDcpdaAmount7(EJBCommon.roundIt(cmFundTransferReceipt.getFtrAmountDeposited(), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }

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

            while (i.hasNext()) {

                LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) i.next();

                Collection cmFundTransferReceipts = cmFundTransfer.getCmFundTransferReceipts();

                if (cmFundTransferReceipts.isEmpty()) {

                    CmRepDailyCashPositionDetailAddDetails addDetails = new CmRepDailyCashPositionDetailAddDetails();

                    addDetails.setDcpdaDateTransaction(cmFundTransfer.getFtDate());
                    addDetails.setDcpdaDescription(cmFundTransfer.getFtMemo());
                    addDetails.setDcpdaReceiptNumber(cmFundTransfer.getFtDocumentNumber());
                    addDetails.setDcpdaCustomer(cmFundTransfer.getFtType());

                    String reportType = (String) criteria.get("reportType");

                    if (cmFundTransfer.getFtDate().equals(details.getDcpdDate1())) {

                        addDetails.setDcpdaAmount1(EJBCommon.roundIt(cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (cmFundTransfer.getFtDate().equals(details.getDcpdDate2())) {

                        addDetails.setDcpdaAmount2(EJBCommon.roundIt(cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (cmFundTransfer.getFtDate().equals(details.getDcpdDate3())) {

                        addDetails.setDcpdaAmount3(EJBCommon.roundIt(cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (cmFundTransfer.getFtDate().equals(details.getDcpdDate4())) {

                        addDetails.setDcpdaAmount4(EJBCommon.roundIt(cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (cmFundTransfer.getFtDate().equals(details.getDcpdDate5())) {

                        addDetails.setDcpdaAmount5(EJBCommon.roundIt(cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (cmFundTransfer.getFtDate().equals(details.getDcpdDate6())) {

                        addDetails.setDcpdaAmount6(EJBCommon.roundIt(cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    } else if (cmFundTransfer.getFtDate().equals(details.getDcpdDate7())) {

                        addDetails.setDcpdaAmount7(EJBCommon.roundIt(cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                    }

                    dcpaList.add(addDetails);
                }
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

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                CmRepDailyCashPositionDetailAddDetails addDetails = new CmRepDailyCashPositionDetailAddDetails();

                addDetails.setDcpdaDateTransaction(cmAdjustment.getAdjDate());
                addDetails.setDcpdaDescription(cmAdjustment.getAdjMemo());
                addDetails.setDcpdaCustomer(cmAdjustment.getAdjType());
                addDetails.setDcpdaReceiptNumber(cmAdjustment.getAdjDocumentNumber());

                String reportType = (String) criteria.get("reportType");

                if (cmAdjustment.getAdjDate().equals(details.getDcpdDate1())) {

                    addDetails.setDcpdaAmount1(EJBCommon.roundIt(cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpdDate2())) {

                    addDetails.setDcpdaAmount2(EJBCommon.roundIt(cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpdDate3())) {

                    addDetails.setDcpdaAmount3(EJBCommon.roundIt(cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpdDate4())) {

                    addDetails.setDcpdaAmount4(EJBCommon.roundIt(cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpdDate5())) {

                    addDetails.setDcpdaAmount5(EJBCommon.roundIt(cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpdDate6())) {

                    addDetails.setDcpdaAmount6(EJBCommon.roundIt(cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpdDate7())) {

                    addDetails.setDcpdaAmount7(EJBCommon.roundIt(cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }

                dcpaList.add(addDetails);
            }
            return dcpaList;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private ArrayList executeCmRepDailyCashPositionDetailLess(HashMap criteria, CmRepDailyCashPositionDetailDetails details, boolean includeDirectChecks, Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashPositionDetailControllerBean executeCmRepDailyCashPositionDetailLess");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size() - 3;

            StringBuilder jbossQl = new StringBuilder();

            // get all released checks

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
                Debug.print("Unposted: " + unposted);
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

            while (i.hasNext()) {

                LocalApCheck apCheck = (LocalApCheck) i.next();

                CmRepDailyCashPositionDetailLessDetails lessDetails = new CmRepDailyCashPositionDetailLessDetails();

                lessDetails.setDcpdlSupplier(apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName());
                lessDetails.setDcpdlDateTransaction(apCheck.getChkDate());
                lessDetails.setDcpdlDescription(apCheck.getChkDescription());
                lessDetails.setDcpdlCheckNumber(apCheck.getChkNumber());

                String reportType = (String) criteria.get("reportType");

                if (apCheck.getChkCheckDate().equals(details.getDcpdDate1())) {

                    lessDetails.setDcpdlAmount1(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (apCheck.getChkCheckDate().equals(details.getDcpdDate2())) {

                    lessDetails.setDcpdlAmount2(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (apCheck.getChkCheckDate().equals(details.getDcpdDate3())) {

                    lessDetails.setDcpdlAmount3(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (apCheck.getChkCheckDate().equals(details.getDcpdDate4())) {

                    lessDetails.setDcpdlAmount4(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (apCheck.getChkCheckDate().equals(details.getDcpdDate5())) {

                    lessDetails.setDcpdlAmount5(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (apCheck.getChkDate().equals(details.getDcpdDate6())) {

                    lessDetails.setDcpdlAmount6(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (apCheck.getChkCheckDate().equals(details.getDcpdDate7())) {

                    lessDetails.setDcpdlAmount7(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }

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

            while (i.hasNext()) {

                LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) i.next();

                CmRepDailyCashPositionDetailLessDetails lessDetails = new CmRepDailyCashPositionDetailLessDetails();

                lessDetails.setDcpdlDateTransaction(cmFundTransfer.getFtDate());
                lessDetails.setDcpdlDescription(cmFundTransfer.getFtMemo());
                lessDetails.setDcpdlSupplier(cmFundTransfer.getFtType());
                lessDetails.setDcpdlCheckNumber(cmFundTransfer.getFtDocumentNumber());

                String reportType = (String) criteria.get("reportType");

                if (cmFundTransfer.getFtDate().equals(details.getDcpdDate1())) {

                    lessDetails.setDcpdlAmount1(EJBCommon.roundIt(cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransfer.getFtDate().equals(details.getDcpdDate2())) {

                    lessDetails.setDcpdlAmount2(EJBCommon.roundIt(cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransfer.getFtDate().equals(details.getDcpdDate3())) {

                    lessDetails.setDcpdlAmount3(EJBCommon.roundIt(cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransfer.getFtDate().equals(details.getDcpdDate4())) {

                    lessDetails.setDcpdlAmount4(EJBCommon.roundIt(cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransfer.getFtDate().equals(details.getDcpdDate5())) {

                    lessDetails.setDcpdlAmount5(EJBCommon.roundIt(cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransfer.getFtDate().equals(details.getDcpdDate6())) {

                    lessDetails.setDcpdlAmount6(EJBCommon.roundIt(cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmFundTransfer.getFtDate().equals(details.getDcpdDate7())) {

                    lessDetails.setDcpdlAmount7(EJBCommon.roundIt(cmFundTransfer.getFtAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }

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

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                CmRepDailyCashPositionDetailLessDetails lessDetails = new CmRepDailyCashPositionDetailLessDetails();

                lessDetails.setDcpdlDateTransaction(cmAdjustment.getAdjDate());
                lessDetails.setDcpdlDescription(cmAdjustment.getAdjMemo());
                lessDetails.setDcpdlSupplier(cmAdjustment.getAdjType());
                lessDetails.setDcpdlCheckNumber(cmAdjustment.getAdjDocumentNumber());

                String reportType = (String) criteria.get("reportType");

                if (cmAdjustment.getAdjDate().equals(details.getDcpdDate1())) {

                    lessDetails.setDcpdlAmount1(EJBCommon.roundIt(lessDetails.getDcpdlAmount1() + cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpdDate2())) {

                    lessDetails.setDcpdlAmount2(EJBCommon.roundIt(lessDetails.getDcpdlAmount2() + cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpdDate3())) {

                    lessDetails.setDcpdlAmount3(EJBCommon.roundIt(cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpdDate4())) {

                    lessDetails.setDcpdlAmount4(EJBCommon.roundIt(cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpdDate5())) {

                    lessDetails.setDcpdlAmount5(EJBCommon.roundIt(cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpdDate6())) {

                    lessDetails.setDcpdlAmount6(EJBCommon.roundIt(cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                } else if (cmAdjustment.getAdjDate().equals(details.getDcpdDate7())) {

                    lessDetails.setDcpdlAmount7(EJBCommon.roundIt(cmAdjustment.getAdjAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                }

                dcplList.add(lessDetails);
            }
            return dcplList;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private CmRepDailyCashPositionDetailDetails computeBeginningBalancesAndAvailableCashBalance(CmRepDailyCashPositionDetailDetails details, Integer AD_CMPNY) {

        Debug.print("CmRepDailyCashPositionDetailControllerBean computeBeginningBalancesAndAvailableCashBalance");

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

        ArrayList list = details.getDcpdAddList();
        Iterator i = list.iterator();

        while (i.hasNext()) {

            CmRepDailyCashPositionDetailAddDetails addDetails = (CmRepDailyCashPositionDetailAddDetails) i.next();

            TOTAL_AMOUNT1 = TOTAL_AMOUNT1 + addDetails.getDcpdaAmount1();
            TOTAL_AMOUNT2 = TOTAL_AMOUNT2 + addDetails.getDcpdaAmount2();
            TOTAL_AMOUNT3 = TOTAL_AMOUNT3 + addDetails.getDcpdaAmount3();
            TOTAL_AMOUNT4 = TOTAL_AMOUNT4 + addDetails.getDcpdaAmount4();
            TOTAL_AMOUNT5 = TOTAL_AMOUNT5 + addDetails.getDcpdaAmount5();
            TOTAL_AMOUNT6 = TOTAL_AMOUNT6 + addDetails.getDcpdaAmount6();
            TOTAL_AMOUNT7 = TOTAL_AMOUNT7 + addDetails.getDcpdaAmount7();
        }

        list = details.getDcpdLessList();
        i = list.iterator();

        while (i.hasNext()) {

            CmRepDailyCashPositionDetailLessDetails lessDetails = (CmRepDailyCashPositionDetailLessDetails) i.next();

            TOTAL_AMOUNT1 = TOTAL_AMOUNT1 - lessDetails.getDcpdlAmount1();
            TOTAL_AMOUNT2 = TOTAL_AMOUNT2 - lessDetails.getDcpdlAmount2();
            TOTAL_AMOUNT3 = TOTAL_AMOUNT3 - lessDetails.getDcpdlAmount3();
            TOTAL_AMOUNT4 = TOTAL_AMOUNT4 - lessDetails.getDcpdlAmount4();
            TOTAL_AMOUNT5 = TOTAL_AMOUNT5 - lessDetails.getDcpdlAmount5();
            TOTAL_AMOUNT6 = TOTAL_AMOUNT6 - lessDetails.getDcpdlAmount6();
            TOTAL_AMOUNT7 = TOTAL_AMOUNT7 - lessDetails.getDcpdlAmount7();
        }

        details.setDcpdAvailableCashBalance1(EJBCommon.roundIt(details.getDcpdBeginningBalance1() + TOTAL_AMOUNT1, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcpdBeginningBalance2(EJBCommon.roundIt(details.getDcpdAvailableCashBalance1(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcpdAvailableCashBalance2(EJBCommon.roundIt(details.getDcpdBeginningBalance2() + TOTAL_AMOUNT2, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcpdBeginningBalance3(EJBCommon.roundIt(details.getDcpdAvailableCashBalance2(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcpdAvailableCashBalance3(EJBCommon.roundIt(details.getDcpdBeginningBalance3() + TOTAL_AMOUNT3, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcpdBeginningBalance4(EJBCommon.roundIt(details.getDcpdAvailableCashBalance3(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcpdAvailableCashBalance4(EJBCommon.roundIt(details.getDcpdBeginningBalance4() + TOTAL_AMOUNT4, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcpdBeginningBalance5(EJBCommon.roundIt(details.getDcpdAvailableCashBalance4(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcpdAvailableCashBalance5(EJBCommon.roundIt(details.getDcpdBeginningBalance5() + TOTAL_AMOUNT5, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcpdBeginningBalance6(EJBCommon.roundIt(details.getDcpdAvailableCashBalance5(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcpdAvailableCashBalance6(EJBCommon.roundIt(details.getDcpdBeginningBalance6() + TOTAL_AMOUNT6, adCompany.getGlFunctionalCurrency().getFcPrecision()));
        details.setDcpdBeginningBalance7(EJBCommon.roundIt(details.getDcpdAvailableCashBalance6(), adCompany.getGlFunctionalCurrency().getFcPrecision()));

        details.setDcpdAvailableCashBalance7(EJBCommon.roundIt(details.getDcpdBeginningBalance7() + TOTAL_AMOUNT7, adCompany.getGlFunctionalCurrency().getFcPrecision()));

        return details;
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("CmRepDailyCashPositionDetailControllerBean ejbCreate");
    }
}