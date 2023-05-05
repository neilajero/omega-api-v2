/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmRepCashPositionControllerBean
 * @created August 14, 2008
 * @author
 */
package com.ejb.txnreports.cm;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJBException;


import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdBankAccountBalance;
import com.ejb.dao.ad.LocalAdBankAccountBalanceHome;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ad.ILocalAdCompanyHome;
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
import com.util.EJBContextClass;
import com.util.ad.AdCompanyDetails;
import com.util.reports.cm.CmRepCashPositionDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBHomeFactory;

@Stateless(name = "CmRepCashPositionControllerEJB")
public class CmRepCashPositionControllerBean extends EJBContextClass implements CmRepCashPositionController {

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


    public ArrayList getAdBaAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("CmRepCashPositionControllerBean getAdBaAll");

        LocalAdBankAccount adBankAccount = null;

        ArrayList list = new ArrayList();

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

    public ArrayList executeCmRepCashPosition(HashMap criteria, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmRepCashPositionControllerBean executeCmRepCashPosition");

        try {

            ArrayList list = new ArrayList();

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            // get last day of last month
            Date beginningBalanceDate = this.getBeginningBalanceDate((Date) criteria.get("dateFrom"));

            // get bank account balance and bankAccountCode IF a bank account is selected
            if (criteria.containsKey("bankAccount")) {
                // if a bank account is selected
                LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName((String) criteria.get("bankAccount"), AD_CMPNY);

                criteria.put("bankAccountCode", adBankAccount.getBaCode());
            }

            // For Cash Receipts

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size() - 1;

            StringBuilder jbossQl = new StringBuilder();

            // get all receipt

            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct ");

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (!(criteria.containsKey("bankAccount") || criteria.containsKey("bankAccountCode"))) {

                criteriaSize++;
            }

            obj = new Object[criteriaSize];

            Debug.print("CriteriaSize : " + criteriaSize);

            Debug.print("OBJ SIZE : " + obj.length);

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

                jbossQl.append("rct.rctDate>=?").append(ctr + 1).append(" ");
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

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                CmRepCashPositionDetails cpDetails = new CmRepCashPositionDetails();

                Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(beginningBalanceDate, arReceipt.getAdBankAccount().getBaCode(), "BOOK", AD_CMPNY);

                if (adBankAccountBalances.size() > 0) {
                    ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);
                    LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);
                    cpDetails.setCpBegBal(adBankAccountBalance.getBabBalance());
                }

                cpDetails.setCpAccount(arReceipt.getAdBankAccount().getBaName());
                cpDetails.setCpAmount(arReceipt.getRctAmountCash());
                cpDetails.setCpDate(arReceipt.getRctDate());
                cpDetails.setCpDesc(arReceipt.getRctDescription());
                cpDetails.setCpNum(arReceipt.getRctNumber());
                cpDetails.setCpRefNum(arReceipt.getRctReferenceNumber());
                cpDetails.setCpType("Receipts");
                cpDetails.setCpDateFrom(beginningBalanceDate);
                cpDetails.setCpDateTo((Date) criteria.get("dateTo"));

                list.add(cpDetails);
            }

            // get all receipt Card 1

            jbossQl = new StringBuilder();

            ctr = 0;
            criteriaSize = criteria.size() - 1;
            firstArgument = true;

            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct ");

            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (!(criteria.containsKey("bankAccount") || criteria.containsKey("bankAccountCode"))) {

                criteriaSize++;
            }

            obj = new Object[criteriaSize];

            Debug.print("CriteriaSize : " + criteriaSize);

            Debug.print("OBJ SIZE : " + obj.length);

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

                jbossQl.append("rct.rctDate>=?").append(ctr + 1).append(" ");
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

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                CmRepCashPositionDetails cpDetails = new CmRepCashPositionDetails();

                Collection adBankAccountCard1Balances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(beginningBalanceDate, arReceipt.getAdBankAccountCard1().getBaCode(), "BOOK", AD_CMPNY);

                if (adBankAccountCard1Balances.size() > 0) {
                    ArrayList adBankAccountCard1BalanceList = new ArrayList(adBankAccountCard1Balances);
                    LocalAdBankAccountBalance adBankAccountCard1Balance = (LocalAdBankAccountBalance) adBankAccountCard1BalanceList.get(adBankAccountCard1BalanceList.size() - 1);
                    cpDetails.setCpBegBal(adBankAccountCard1Balance.getBabBalance());
                }

                cpDetails.setCpAccount(arReceipt.getAdBankAccountCard1().getBaName());
                cpDetails.setCpAmount(arReceipt.getRctAmountCard1());
                cpDetails.setCpDate(arReceipt.getRctDate());
                cpDetails.setCpDesc(arReceipt.getRctDescription());
                cpDetails.setCpNum(arReceipt.getRctNumber());
                cpDetails.setCpRefNum(arReceipt.getRctReferenceNumber());
                cpDetails.setCpType("Receipts");
                cpDetails.setCpDateFrom(beginningBalanceDate);
                cpDetails.setCpDateTo((Date) criteria.get("dateTo"));

                list.add(cpDetails);
            }

            // get all receipt Card2

            jbossQl = new StringBuilder();

            ctr = 0;
            criteriaSize = criteria.size() - 1;
            firstArgument = true;

            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct ");

            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (!(criteria.containsKey("bankAccount") || criteria.containsKey("bankAccountCode"))) {

                criteriaSize++;
            }

            obj = new Object[criteriaSize];

            Debug.print("CriteriaSize : " + criteriaSize);

            Debug.print("OBJ SIZE : " + obj.length);

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

                jbossQl.append("rct.rctDate>=?").append(ctr + 1).append(" ");
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

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                CmRepCashPositionDetails cpDetails = new CmRepCashPositionDetails();

                Collection adBankAccountCard2Balances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(beginningBalanceDate, arReceipt.getAdBankAccountCard2().getBaCode(), "BOOK", AD_CMPNY);

                if (adBankAccountCard2Balances.size() > 0) {
                    ArrayList adBankAccountCard2BalanceList = new ArrayList(adBankAccountCard2Balances);
                    LocalAdBankAccountBalance adBankAccountCard2Balance = (LocalAdBankAccountBalance) adBankAccountCard2BalanceList.get(adBankAccountCard2BalanceList.size() - 1);
                    cpDetails.setCpBegBal(adBankAccountCard2Balance.getBabBalance());
                }

                cpDetails.setCpAccount(arReceipt.getAdBankAccountCard2().getBaName());
                cpDetails.setCpAmount(arReceipt.getRctAmountCard2());
                cpDetails.setCpDate(arReceipt.getRctDate());
                cpDetails.setCpDesc(arReceipt.getRctDescription());
                cpDetails.setCpNum(arReceipt.getRctNumber());
                cpDetails.setCpRefNum(arReceipt.getRctReferenceNumber());
                cpDetails.setCpType("Receipts");
                cpDetails.setCpDateFrom(beginningBalanceDate);
                cpDetails.setCpDateTo((Date) criteria.get("dateTo"));

                list.add(cpDetails);
            }

            // get all receipt Card3

            jbossQl = new StringBuilder();

            ctr = 0;
            criteriaSize = criteria.size() - 1;
            firstArgument = true;

            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct ");

            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (!(criteria.containsKey("bankAccount") || criteria.containsKey("bankAccountCode"))) {

                criteriaSize++;
            }

            obj = new Object[criteriaSize];

            Debug.print("CriteriaSize : " + criteriaSize);

            Debug.print("OBJ SIZE : " + obj.length);

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

                jbossQl.append("rct.rctDate>=?").append(ctr + 1).append(" ");
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

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                CmRepCashPositionDetails cpDetails = new CmRepCashPositionDetails();

                Collection adBankAccountCard3Balances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(beginningBalanceDate, arReceipt.getAdBankAccountCard3().getBaCode(), "BOOK", AD_CMPNY);

                if (adBankAccountCard3Balances.size() > 0) {
                    ArrayList adBankAccountCard3BalanceList = new ArrayList(adBankAccountCard3Balances);
                    LocalAdBankAccountBalance adBankAccountCard3Balance = (LocalAdBankAccountBalance) adBankAccountCard3BalanceList.get(adBankAccountCard3BalanceList.size() - 1);
                    cpDetails.setCpBegBal(adBankAccountCard3Balance.getBabBalance());
                }

                cpDetails.setCpAccount(arReceipt.getAdBankAccountCard3().getBaName());
                cpDetails.setCpAmount(arReceipt.getRctAmountCard3());
                cpDetails.setCpDate(arReceipt.getRctDate());
                cpDetails.setCpDesc(arReceipt.getRctDescription());
                cpDetails.setCpNum(arReceipt.getRctNumber());
                cpDetails.setCpRefNum(arReceipt.getRctReferenceNumber());
                cpDetails.setCpType("Receipts");
                cpDetails.setCpDateFrom(beginningBalanceDate);
                cpDetails.setCpDateTo((Date) criteria.get("dateTo"));

                list.add(cpDetails);
            }

            // get all receipt Cheque

            jbossQl = new StringBuilder();

            ctr = 0;
            criteriaSize = criteria.size() - 1;
            firstArgument = true;

            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct ");

            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (!(criteria.containsKey("bankAccount") || criteria.containsKey("bankAccountCode"))) {

                criteriaSize++;
            }

            obj = new Object[criteriaSize];

            Debug.print("CriteriaSize : " + criteriaSize);

            Debug.print("OBJ SIZE : " + obj.length);

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

                jbossQl.append("rct.rctDate>=?").append(ctr + 1).append(" ");
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

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                CmRepCashPositionDetails cpDetails = new CmRepCashPositionDetails();

                Collection adBankAccountChequeBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(beginningBalanceDate, arReceipt.getAdBankAccount().getBaCode(), "BOOK", AD_CMPNY);

                if (adBankAccountChequeBalances.size() > 0) {
                    ArrayList adBankAccountChequeBalanceList = new ArrayList(adBankAccountChequeBalances);
                    LocalAdBankAccountBalance adBankAccountChequeBalance = (LocalAdBankAccountBalance) adBankAccountChequeBalanceList.get(adBankAccountChequeBalanceList.size() - 1);
                    cpDetails.setCpBegBal(adBankAccountChequeBalance.getBabBalance());
                }

                cpDetails.setCpAccount(arReceipt.getAdBankAccount().getBaName());
                cpDetails.setCpAmount(arReceipt.getRctAmountCheque());
                cpDetails.setCpDate(arReceipt.getRctDate());
                cpDetails.setCpDesc(arReceipt.getRctDescription());
                cpDetails.setCpNum(arReceipt.getRctNumber());
                cpDetails.setCpRefNum(arReceipt.getRctReferenceNumber());
                cpDetails.setCpType("Receipts");
                cpDetails.setCpDateFrom(beginningBalanceDate);
                cpDetails.setCpDateTo((Date) criteria.get("dateTo"));

                list.add(cpDetails);
            }

            //	get all deposit transfer receipt

            jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(ftr) FROM CmFundTransferReceipt ftr ");

            ctr = 0;
            criteriaSize = criteria.size() - 1;
            firstArgument = true;
            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (!(criteria.containsKey("bankAccount") || criteria.containsKey("bankAccountCode"))) {

                criteriaSize++;
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

                jbossQl.append("ftr.cmFundTransfer.ftDate>=?").append(ctr + 1).append(" ");
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

                CmRepCashPositionDetails cpDetails = new CmRepCashPositionDetails();

                Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(beginningBalanceDate, cmFundTransferReceipt.getArReceipt().getAdBankAccount().getBaCode(), "BOOK", AD_CMPNY);

                if (adBankAccountBalances.size() > 0) {
                    ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);
                    LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);
                    cpDetails.setCpBegBal(adBankAccountBalance.getBabBalance());
                }
                cpDetails.setCpAccount(cmFundTransferReceipt.getArReceipt().getAdBankAccount().getBaName());
                cpDetails.setCpAmount(cmFundTransferReceipt.getFtrAmountDeposited());
                cpDetails.setCpDate(cmFundTransferReceipt.getCmFundTransfer().getFtDate());
                cpDetails.setCpDesc("DEPOSITED RECEIPT");
                cpDetails.setCpNum(cmFundTransferReceipt.getCmFundTransfer().getFtDocumentNumber());
                cpDetails.setCpRefNum(cmFundTransferReceipt.getCmFundTransfer().getFtReferenceNumber());
                cpDetails.setCpType("Receipts");
                cpDetails.setCpDateFrom(beginningBalanceDate);
                cpDetails.setCpDateTo((Date) criteria.get("dateTo"));

                list.add(cpDetails);
            }

            //	get all fund transfer to

            jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(ft) FROM CmFundTransfer ft ");

            ctr = 0;
            criteriaSize = criteria.size() - 1;
            firstArgument = true;
            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (!(criteria.containsKey("bankAccount") || criteria.containsKey("bankAccountCode"))) {

                criteriaSize++;
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

                jbossQl.append("ft.ftDate>=?").append(ctr + 1).append(" ");
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
                    CmRepCashPositionDetails cpDetails = new CmRepCashPositionDetails();

                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(beginningBalanceDate, cmFundTransfer.getFtAdBaAccountTo(), "BOOK", AD_CMPNY);

                    if (adBankAccountBalances.size() > 0) {
                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);
                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);
                        cpDetails.setCpBegBal(adBankAccountBalance.getBabBalance());
                    }

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountTo());

                    cpDetails.setCpAccount(adBankAccount.getBaName());
                    cpDetails.setCpAmount(cmFundTransfer.getFtAmount());
                    cpDetails.setCpDate(cmFundTransfer.getFtDate());
                    cpDetails.setCpDesc(cmFundTransfer.getFtMemo());
                    cpDetails.setCpNum(cmFundTransfer.getFtDocumentNumber());
                    cpDetails.setCpRefNum(cmFundTransfer.getFtReferenceNumber());
                    cpDetails.setCpType("Receipts");
                    cpDetails.setCpDateFrom(beginningBalanceDate);
                    cpDetails.setCpDateTo((Date) criteria.get("dateTo"));

                    list.add(cpDetails);
                }
            }

            //	get all positve adjustments

            jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(adj) FROM CmAdjustment adj ");

            ctr = 0;
            criteriaSize = criteria.size() - 1;
            firstArgument = true;
            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (!(criteria.containsKey("bankAccount") || criteria.containsKey("bankAccountCode"))) {

                criteriaSize++;
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

                jbossQl.append("adj.adjDate>=?").append(ctr + 1).append(" ");
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

                CmRepCashPositionDetails cpDetails = new CmRepCashPositionDetails();

                Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(beginningBalanceDate, cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", AD_CMPNY);

                if (adBankAccountBalances.size() > 0) {
                    ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);
                    LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);
                    cpDetails.setCpBegBal(adBankAccountBalance.getBabBalance());
                }
                cpDetails.setCpAccount(cmAdjustment.getAdBankAccount().getBaName());
                cpDetails.setCpAmount(cmAdjustment.getAdjAmount());
                cpDetails.setCpDate(cmAdjustment.getAdjDate());
                cpDetails.setCpDesc(cmAdjustment.getAdjMemo());
                cpDetails.setCpNum(cmAdjustment.getAdjDocumentNumber());
                cpDetails.setCpRefNum(cmAdjustment.getAdjReferenceNumber());
                cpDetails.setCpType("Receipts");
                cpDetails.setCpDateFrom(beginningBalanceDate);
                cpDetails.setCpDateTo((Date) criteria.get("dateTo"));

                list.add(cpDetails);
            }

            // For Disbursements

            // get bank account balance
            firstArgument = true;
            ctr = 0;
            criteriaSize = criteria.size() - 1;

            jbossQl = new StringBuilder();

            // get all released checks

            jbossQl.append("SELECT OBJECT(chk) FROM ApCheck chk ");

            firstArgument = true;

            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (!(criteria.containsKey("bankAccount") || criteria.containsKey("bankAccountCode"))) {

                criteriaSize++;
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

                jbossQl.append("chk.chkCheckDate>=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("chk.chkPosted = 1 AND chk.chkVoid = 0"); // AND chk.chkReleased = 1");

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

            jbossQl.append(" chk.chkAdCompany = ").append(AD_CMPNY).append(" ");

            Collection dcpls = apCheckHome.getChkByCriteria(jbossQl.toString(), obj, 0, 0);
            i = dcpls.iterator();

            while (i.hasNext()) {

                LocalApCheck apCheck = (LocalApCheck) i.next();

                CmRepCashPositionDetails cpDetails = new CmRepCashPositionDetails();

                Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(beginningBalanceDate, apCheck.getAdBankAccount().getBaCode(), "BOOK", AD_CMPNY);

                if (adBankAccountBalances.size() > 0) {
                    ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);
                    LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);
                    cpDetails.setCpBegBal(adBankAccountBalance.getBabBalance());
                }
                cpDetails.setCpAccount(apCheck.getAdBankAccount().getBaName());
                cpDetails.setCpAmount(apCheck.getChkAmount());
                cpDetails.setCpDate(apCheck.getChkDate());
                cpDetails.setCpDesc(apCheck.getChkDescription());
                cpDetails.setCpNum(apCheck.getChkDocumentNumber());
                cpDetails.setCpRefNum(apCheck.getChkReferenceNumber());
                cpDetails.setCpType("Disbursements");
                cpDetails.setCpDateFrom(beginningBalanceDate);
                cpDetails.setCpDateTo((Date) criteria.get("dateTo"));

                list.add(cpDetails);
            }

            //	get all fund transfer from

            jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(ft) FROM CmFundTransfer ft ");

            ctr = 0;
            criteriaSize = criteria.size() - 1;
            firstArgument = true;
            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (!(criteria.containsKey("bankAccount") || criteria.containsKey("bankAccountCode"))) {

                criteriaSize++;
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

                jbossQl.append("ft.ftDate>=?").append(ctr + 1).append(" ");
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

                CmRepCashPositionDetails cpDetails = new CmRepCashPositionDetails();

                Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(beginningBalanceDate, cmFundTransfer.getFtAdBaAccountFrom(), "BOOK", AD_CMPNY);

                if (adBankAccountBalances.size() > 0) {
                    ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);
                    LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);
                    cpDetails.setCpBegBal(adBankAccountBalance.getBabBalance());
                }

                LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountFrom());

                cpDetails.setCpAccount(adBankAccount.getBaName());
                cpDetails.setCpAmount(cmFundTransfer.getFtAmount());
                cpDetails.setCpDate(cmFundTransfer.getFtDate());
                cpDetails.setCpDesc(cmFundTransfer.getFtMemo());
                cpDetails.setCpNum(cmFundTransfer.getFtDocumentNumber());
                cpDetails.setCpRefNum(cmFundTransfer.getFtReferenceNumber());
                cpDetails.setCpType("Disbursements");
                cpDetails.setCpDateFrom(beginningBalanceDate);
                cpDetails.setCpDateTo((Date) criteria.get("dateTo"));

                list.add(cpDetails);
            }

            //	get all negative adjustments

            jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(adj) FROM CmAdjustment adj ");

            ctr = 0;
            criteriaSize = criteria.size() - 1;
            firstArgument = true;
            // Allocate the size of the object parameter

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (!(criteria.containsKey("bankAccount") || criteria.containsKey("bankAccountCode"))) {

                criteriaSize++;
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

                jbossQl.append("adj.adjDate>=?").append(ctr + 1).append(" ");
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

                CmRepCashPositionDetails cpDetails = new CmRepCashPositionDetails();

                Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(beginningBalanceDate, cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", AD_CMPNY);

                if (adBankAccountBalances.size() > 0) {
                    ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);
                    LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);
                    cpDetails.setCpBegBal(adBankAccountBalance.getBabBalance());
                }
                cpDetails.setCpAccount(cmAdjustment.getAdBankAccount().getBaName());
                cpDetails.setCpAmount(cmAdjustment.getAdjAmount());
                cpDetails.setCpDate(cmAdjustment.getAdjDate());
                cpDetails.setCpDesc(cmAdjustment.getAdjMemo());
                cpDetails.setCpNum(cmAdjustment.getAdjDocumentNumber());
                cpDetails.setCpRefNum(cmAdjustment.getAdjReferenceNumber());
                cpDetails.setCpType("Disbursements");
                cpDetails.setCpDateFrom(beginningBalanceDate);
                cpDetails.setCpDateTo((Date) criteria.get("dateTo"));

                list.add(cpDetails);
            }

            list.sort(CmRepCashPositionDetails.BankAccountComparator);

            if (list.isEmpty()) throw new GlobalNoRecordFoundException();

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("CmRepCashPositionControllerBean getAdCompany");

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
    private Date getBeginningBalanceDate(Date dateEntered) {

        Debug.print("CmRepCashPositionControllerBean getBeginningBalanceDate");

        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(dateEntered);
            calendar.add(GregorianCalendar.MONTH, -1);

            GregorianCalendar calendar2 = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMaximum(Calendar.DATE), 0, 0, 0);

            return calendar2.getTime();

        } finally {

        }
        // return calendar.getTime();

    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("CmRepCashPositionControllerBean ejbCreate");
    }
}