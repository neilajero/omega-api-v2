/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmRepBankReconciliationControllerBean
 * @created
 * @author
 */
package com.ejb.txnreports.cm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.dao.cm.LocalCmAdjustmentHome;
import com.ejb.entities.cm.LocalCmFundTransfer;
import com.ejb.dao.cm.LocalCmFundTransferHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.cm.CmRepBankReconciliationDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "CmRepBankReconciliationControllerEJB")
public class CmRepBankReconciliationControllerBean extends EJBContextClass implements CmRepBankReconciliationController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalCmAdjustmentHome cmAdjustmentHome;
    @EJB
    private LocalCmFundTransferHome cmFundTransferHome;


    public ArrayList getAdBaAll(Integer AD_CMPNY) {

        Debug.print("CmRepBankReconciliationControllerBean getAdBaAll");

        Collection adBankAccounts = null;

        ArrayList list = new ArrayList();

        try {

            adBankAccounts = adBankAccountHome.findEnabledAndNotCashAccountBaAll(AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBankAccounts.isEmpty()) {

            return null;
        }

        for (Object bankAccount : adBankAccounts) {

            LocalAdBankAccount adBankAccount = (LocalAdBankAccount) bankAccount;

            list.add(adBankAccount.getBaName());
        }

        return list;
    }

    public CmRepBankReconciliationDetails executeCmRepBankReconciliation(String BA_NM, Date DT_FRM, Date DT_TO, Integer AD_BRNCH, Integer AD_CMPNY, String isDraftBCBD) throws GlobalNoRecordFoundException {

        Debug.print("CmRepBankReconciliationControllerBean executeCmRepBankReconciliation");

        CmRepBankReconciliationDetails mdetails = new CmRepBankReconciliationDetails();

        try {

            double BR_BGNNNG_BLNC_BK = 0d;
            double BR_BGNNNG_BLNC_BNK = 0d;
            double BR_DPST_AMNT_BK = 0d;
            double BR_DPST_AMNT_BNK = 0d;
            double BR_DSBRSMNT_AMNT_BK = 0d;
            double BR_DSBRSMNT_AMNT_BNK = 0d;
            double BR_DPST_IN_TRNST_AMNT = 0d;
            double BR_OUTSTNDNG_CHCK_AMNT = 0d;
            double BR_BNK_CRDT_AMNT = 0d;
            double BR_BNK_DBT_AMNT = 0d;
            double receipt = 0d;
            double cmAdjustmentDebitMemo = 0d;
            double fundTransferFrom = 0d;

            LocalAdBankAccount adBankAccount = null;

            try {

                adBankAccount = adBankAccountHome.findByBaNameAndBrCode(BA_NM, AD_BRNCH, AD_CMPNY);

                mdetails.setBrBankAccount(adBankAccount.getBaName());
                mdetails.setBrDateFrom(EJBCommon.convertSQLDateToString(DT_FRM));
                mdetails.setBrDateTo(EJBCommon.convertSQLDateToString(DT_TO));

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            // BOOK
            // get beginning balance

            Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeDateAndBaCodeAndType(DT_FRM, adBankAccount.getBaCode(), "BOOK", AD_CMPNY);

            if (!adBankAccountBalances.isEmpty()) {

                ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);
                LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);
                BR_BGNNNG_BLNC_BK = adBankAccountBalance.getBabBalance();
            }

            // -------------------------------------------------------------------------------------
            // AR / BR_DPST_AMNT_BK

            Collection arReceipts = arReceiptHome.findPostedRctByBaNameAndRctDateRangeAndBrCode(BA_NM, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            Iterator i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();
                BR_DPST_AMNT_BK += arReceipt.getRctAmountCash();
            }

            arReceipts = arReceiptHome.findPostedCard1RctByBaNameAndRctDateRangeAndBrCode(BA_NM, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();
                BR_DPST_AMNT_BK += arReceipt.getRctAmountCard1();
            }

            arReceipts = arReceiptHome.findPostedCard2RctByBaNameAndRctDateRangeAndBrCode(BA_NM, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();
                BR_DPST_AMNT_BK += arReceipt.getRctAmountCard2();
            }

            arReceipts = arReceiptHome.findPostedCard3RctByBaNameAndRctDateRangeAndBrCode(BA_NM, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();
                BR_DPST_AMNT_BK += arReceipt.getRctAmountCard3();
            }

            arReceipts = arReceiptHome.findPostedChequeRctByBaNameAndRctDateRangeAndBrCode(BA_NM, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();
                BR_DPST_AMNT_BK += arReceipt.getRctAmountCheque();
            }

            Collection cmAdjustments = cmAdjustmentHome.findPostedAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(BA_NM, DT_FRM, DT_TO, "DEBIT MEMO", AD_BRNCH, AD_CMPNY);

            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();
                BR_DPST_AMNT_BK += cmAdjustment.getAdjAmount();
            }

            cmAdjustments = cmAdjustmentHome.findPostedAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(BA_NM, DT_FRM, DT_TO, "ADVANCE", AD_BRNCH, AD_CMPNY);

            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();
                BR_DPST_AMNT_BK += cmAdjustment.getAdjAmount();
            }

            Collection cmFundTransfers = cmFundTransferHome.findPostedFtAccountToByBaCodeAndDateRangeAndBrCode(adBankAccount.getBaCode(), DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            i = cmFundTransfers.iterator();

            while (i.hasNext()) {

                LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) i.next();
                BR_DPST_AMNT_BK += cmFundTransfer.getFtAmount();
            }

            if (isDraftBCBD.equals("Yes")) {
                cmAdjustments = cmAdjustmentHome.findDraftAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(BA_NM, DT_FRM, DT_TO, "INTEREST", AD_BRNCH, AD_CMPNY);
                i = cmAdjustments.iterator();

                while (i.hasNext()) {

                    LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                    BR_DPST_AMNT_BK += cmAdjustment.getAdjAmount();
                }
            }
            cmAdjustments = cmAdjustmentHome.findPostedAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(BA_NM, DT_FRM, DT_TO, "INTEREST", AD_BRNCH, AD_CMPNY);
            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                BR_DPST_AMNT_BK += cmAdjustment.getAdjAmount();
            }

            // -------------------------------------------------------------------------------------
            // AP / BR_DSBRSMNT_AMNT_BK
            Collection apChecks = apCheckHome.findPostedChkByBaNameAndChkDateRangeAndBrCode(BA_NM, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            i = apChecks.iterator();

            while (i.hasNext()) {

                LocalApCheck apCheck = (LocalApCheck) i.next();
                BR_DSBRSMNT_AMNT_BK += apCheck.getChkAmount();
            }

            cmAdjustments = cmAdjustmentHome.findPostedAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(BA_NM, DT_FRM, DT_TO, "CREDIT MEMO", AD_BRNCH, AD_CMPNY);

            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();
                BR_DSBRSMNT_AMNT_BK += cmAdjustment.getAdjAmount();
            }

            cmFundTransfers = cmFundTransferHome.findPostedFtAccountFromByBaCodeAndDateRangeAndBrCode(adBankAccount.getBaCode(), DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            i = cmFundTransfers.iterator();

            while (i.hasNext()) {

                LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) i.next();
                BR_DSBRSMNT_AMNT_BK += cmFundTransfer.getFtAmount();
            }

            if (isDraftBCBD.equals("Yes")) {
                cmAdjustments = cmAdjustmentHome.findDraftAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(BA_NM, DT_FRM, DT_TO, "BANK CHARGE", AD_BRNCH, AD_CMPNY);

                i = cmAdjustments.iterator();

                while (i.hasNext()) {

                    LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();
                    // BR_DSBRSMNT_AMNT_BK += cmAdjustment.getAdjAmount();
                    BR_DSBRSMNT_AMNT_BK += cmAdjustment.getAdjAmount();
                }
            }

            cmAdjustments = cmAdjustmentHome.findPostedAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(BA_NM, DT_FRM, DT_TO, "BANK CHARGE", AD_BRNCH, AD_CMPNY);

            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();
                // BR_DSBRSMNT_AMNT_BK += cmAdjustment.getAdjAmount();
                BR_DSBRSMNT_AMNT_BK += cmAdjustment.getAdjAmount();
            }

            // BANK RECONCILED
            // get beginning balance

            adBankAccountBalances = adBankAccountBalanceHome.findByBeforeDateAndBaCodeAndType(DT_FRM, adBankAccount.getBaCode(), "RECON", AD_CMPNY);
            Debug.print("DT_FRM: " + DT_FRM);
            Debug.print("adBankAccount.getBaCode(): " + adBankAccount.getBaCode());
            Debug.print("AD_CMPNY" + AD_CMPNY);
            Debug.print("adBankAccountBalancesSize: " + adBankAccountBalances.size());
            if (!adBankAccountBalances.isEmpty()) {

                ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);
                LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);
                BR_BGNNNG_BLNC_BNK = adBankAccountBalance.getBabBalance();
                System.out.print("adBankAccountBalance.getBabBalance(): " + adBankAccountBalance.getBabBalance());
            }

            // -------------------------------------------------------------------------------------
            // AR
            // RECONCILED TRANSACTIONS / BR_DPST_AMNT_BNK
            // DEPOSIT(RECEIPT) / DEBIT MEMO / ADVANCE / INTEREST / FUND TRANSFER IN

            arReceipts = arReceiptHome.findReconciledPostedRctByBaNameAndRctDateRangeAndBrCode(BA_NM, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();
                BR_DPST_AMNT_BNK += arReceipt.getRctAmountCash();
            }

            arReceipts = arReceiptHome.findReconciledPostedCard1RctByBaNameAndRctDateRangeAndBrCode(BA_NM, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();
                BR_DPST_AMNT_BNK += arReceipt.getRctAmountCard1();
            }

            arReceipts = arReceiptHome.findReconciledPostedCard2RctByBaNameAndRctDateRangeAndBrCode(BA_NM, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();
                BR_DPST_AMNT_BNK += arReceipt.getRctAmountCard2();
            }

            arReceipts = arReceiptHome.findReconciledPostedCard3RctByBaNameAndRctDateRangeAndBrCode(BA_NM, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();
                BR_DPST_AMNT_BNK += arReceipt.getRctAmountCard3();
            }

            arReceipts = arReceiptHome.findReconciledPostedChequeRctByBaNameAndRctDateRangeAndBrCode(BA_NM, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();
                BR_DPST_AMNT_BNK += arReceipt.getRctAmountCheque();
            }

            cmAdjustments = cmAdjustmentHome.findReconciledPostedAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(BA_NM, DT_FRM, DT_TO, "DEBIT MEMO", AD_BRNCH, AD_CMPNY);

            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();
                BR_DPST_AMNT_BNK += cmAdjustment.getAdjAmount();
            }

            cmAdjustments = cmAdjustmentHome.findReconciledPostedAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(BA_NM, DT_FRM, DT_TO, "ADVANCE", AD_BRNCH, AD_CMPNY);

            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();
                BR_DPST_AMNT_BNK += cmAdjustment.getAdjAmount();
            }

            cmAdjustments = cmAdjustmentHome.findReconciledPostedAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(BA_NM, DT_FRM, DT_TO, "INTEREST", AD_BRNCH, AD_CMPNY);

            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();
                BR_DPST_AMNT_BNK += cmAdjustment.getAdjAmount();
            }

            // get fund transfers

            cmFundTransfers = cmFundTransferHome.findReconciledPostedFtAccountToByBaCodeAndDateRangeAndBrCode(adBankAccount.getBaCode(), DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            i = cmFundTransfers.iterator();

            while (i.hasNext()) {

                LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) i.next();
                BR_DPST_AMNT_BNK += cmFundTransfer.getFtAmount();
            }

            // -------------------------------------------------------------------------------------
            // AP
            // RECONCILED TRANSACTIONS / BR_DSBRSMNT_AMNT_BNK
            // CHECK / CREDIT MEMO / BANK CHARGE / FUND TRANSFER OUT

            apChecks = apCheckHome.findReconciledPostedChkByBaNameAndChkDateRangeAndBrCode(BA_NM, DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            i = apChecks.iterator();

            while (i.hasNext()) {

                LocalApCheck apCheck = (LocalApCheck) i.next();
                BR_DSBRSMNT_AMNT_BNK += apCheck.getChkAmount();
            }

            cmAdjustments = cmAdjustmentHome.findReconciledPostedAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(BA_NM, DT_FRM, DT_TO, "CREDIT MEMO", AD_BRNCH, AD_CMPNY);

            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();
                BR_DSBRSMNT_AMNT_BNK += cmAdjustment.getAdjAmount();
            }

            cmAdjustments = cmAdjustmentHome.findReconciledPostedAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(BA_NM, DT_FRM, DT_TO, "BANK CHARGE", AD_BRNCH, AD_CMPNY);

            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();
                BR_DSBRSMNT_AMNT_BNK += cmAdjustment.getAdjAmount();
            }

            cmFundTransfers = cmFundTransferHome.findReconciledPostedFtAccountFromByBaCodeAndDateRangeAndBrCode(adBankAccount.getBaCode(), DT_FRM, DT_TO, AD_BRNCH, AD_CMPNY);

            i = cmFundTransfers.iterator();

            while (i.hasNext()) {

                LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) i.next();
                BR_DSBRSMNT_AMNT_BNK += cmFundTransfer.getFtAmount();
            }

            // -------------------------------------------------------------------------------------
            // AR
            // UNRECONCILED TRANSACTIONS / BR_DPST_IN_TRNST_AMNT
            // DEBIT

            arReceipts = arReceiptHome.findUnreconciledPostedRctByDateAndBaNameAndBrCode(DT_TO, BA_NM, AD_BRNCH, AD_CMPNY);

            i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();
                BR_DPST_IN_TRNST_AMNT += arReceipt.getRctAmountCash();
            }

            arReceipts = arReceiptHome.findUnreconciledPostedCard1RctByDateAndBaNameAndBrCode(DT_TO, BA_NM, AD_BRNCH, AD_CMPNY);

            i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();
                BR_DPST_IN_TRNST_AMNT += arReceipt.getRctAmountCard1();
            }

            arReceipts = arReceiptHome.findUnreconciledPostedCard2RctByDateAndBaNameAndBrCode(DT_TO, BA_NM, AD_BRNCH, AD_CMPNY);

            i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();
                BR_DPST_IN_TRNST_AMNT += arReceipt.getRctAmountCard2();
            }

            arReceipts = arReceiptHome.findUnreconciledPostedCard3RctByDateAndBaNameAndBrCode(DT_TO, BA_NM, AD_BRNCH, AD_CMPNY);

            i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();
                BR_DPST_IN_TRNST_AMNT += arReceipt.getRctAmountCard3();
            }

            arReceipts = arReceiptHome.findUnreconciledPostedChequeRctByDateAndBaNameAndBrCode(DT_TO, BA_NM, AD_BRNCH, AD_CMPNY);

            i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();
                BR_DPST_IN_TRNST_AMNT += arReceipt.getRctAmountCheque();
            }

            cmAdjustments = cmAdjustmentHome.findUnreconciledPostedAdjByDateAndBaNameAndAdjTypeAndBrCode(DT_TO, BA_NM, "DEBIT MEMO", AD_BRNCH, AD_CMPNY);

            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();
                BR_DPST_IN_TRNST_AMNT += cmAdjustment.getAdjAmount();
            }

            cmAdjustments = cmAdjustmentHome.findUnreconciledPostedAdjByDateAndBaNameAndAdjTypeAndBrCode(DT_TO, BA_NM, "ADVANCE", AD_BRNCH, AD_CMPNY);

            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();
                BR_DPST_IN_TRNST_AMNT += cmAdjustment.getAdjAmount();
            }

            cmFundTransfers = cmFundTransferHome.findUnreconciledPostedFtAccountToByDateAndBaCodeAndBrCode(DT_TO, adBankAccount.getBaCode(), AD_BRNCH, AD_CMPNY);

            i = cmFundTransfers.iterator();

            while (i.hasNext()) {

                LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) i.next();
                BR_DPST_IN_TRNST_AMNT += cmFundTransfer.getFtAmount();
            }

            cmAdjustments = cmAdjustmentHome.findUnreconciledPostedAdjByDateAndBaNameAndAdjTypeAndBrCode(DT_TO, BA_NM, "INTEREST", AD_BRNCH, AD_CMPNY);

            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();
                BR_DPST_IN_TRNST_AMNT += cmAdjustment.getAdjAmount();
            }

            if (isDraftBCBD.equals("Yes")) {
                cmAdjustments = cmAdjustmentHome.findDraftAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(BA_NM, DT_FRM, DT_TO, "INTEREST", AD_BRNCH, AD_CMPNY);
                i = cmAdjustments.iterator();

                while (i.hasNext()) {

                    LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                    BR_DPST_IN_TRNST_AMNT += cmAdjustment.getAdjAmount();
                }
            }

            // -------------------------------------------------------------------------------------
            // AP
            // UNRECONCILED TRANSACTIONS / BR_OUTSTNDNG_CHCK_AMNT
            // CREDIT

            apChecks = apCheckHome.findUnreconciledPostedChkByDateAndBaNameAndBrCode(DT_TO, BA_NM, AD_BRNCH, AD_CMPNY);

            i = apChecks.iterator();

            while (i.hasNext()) {

                LocalApCheck apCheck = (LocalApCheck) i.next();
                BR_OUTSTNDNG_CHCK_AMNT += apCheck.getChkAmount();
            }

            cmAdjustments = cmAdjustmentHome.findUnreconciledPostedAdjByDateAndBaNameAndAdjTypeAndBrCode(DT_TO, BA_NM, "CREDIT MEMO", AD_BRNCH, AD_CMPNY);

            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();
                BR_OUTSTNDNG_CHCK_AMNT += cmAdjustment.getAdjAmount();
            }

            cmFundTransfers = cmFundTransferHome.findUnreconciledPostedFtAccountFromByDateAndBaCodeAndBrCode(DT_TO, adBankAccount.getBaCode(), AD_BRNCH, AD_CMPNY);

            i = cmFundTransfers.iterator();

            while (i.hasNext()) {

                LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) i.next();
                BR_OUTSTNDNG_CHCK_AMNT += cmFundTransfer.getFtAmount();
            }

            cmAdjustments = cmAdjustmentHome.findUnreconciledPostedAdjByDateAndBaNameAndAdjTypeAndBrCode(DT_TO, BA_NM, "BANK CHARGE", AD_BRNCH, AD_CMPNY);

            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();
                BR_OUTSTNDNG_CHCK_AMNT += cmAdjustment.getAdjAmount();
            }

            if (isDraftBCBD.equals("Yes")) {
                cmAdjustments = cmAdjustmentHome.findDraftAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(BA_NM, DT_FRM, DT_TO, "BANK CHARGE", AD_BRNCH, AD_CMPNY);

                i = cmAdjustments.iterator();

                while (i.hasNext()) {

                    LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();
                    BR_OUTSTNDNG_CHCK_AMNT += cmAdjustment.getAdjAmount();
                }
            }

            // -------------------------------------------------------------------------------------

            mdetails.setBrBeginningBalancePerBook(BR_BGNNNG_BLNC_BK);
            mdetails.setBrBeginningBalancePerBank(BR_BGNNNG_BLNC_BNK);
            mdetails.setBrDepositAmountPerBook(BR_DPST_AMNT_BK);
            mdetails.setBrDepositAmountPerBank(BR_DPST_AMNT_BNK);
            mdetails.setBrDisbursementAmountPerBook(BR_DSBRSMNT_AMNT_BK);
            mdetails.setBrDisbursementAmountPerBank(BR_DSBRSMNT_AMNT_BNK);
            mdetails.setBrDepositInTransitAmount(BR_DPST_IN_TRNST_AMNT);
            mdetails.setBrOutstandingChecksAmount(BR_OUTSTNDNG_CHCK_AMNT);
            mdetails.setBrBankCreditAmount(BR_BNK_CRDT_AMNT);
            mdetails.setBrBankDebitAmount(BR_BNK_DBT_AMNT);

            return mdetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("CmRepBankReconciliationControllerBean getAdCompany");

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

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmRepBankReconciliationControllerBean getAdBrResAll");

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

                adBranch = adBranchResponsibility.getAdBranch();

                AdBranchDetails details = new AdBranchDetails();

                details.setBrCode(adBranch.getBrCode());
                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrName(adBranch.getBrName());
                details.setBrHeadQuarter(adBranch.getBrHeadQuarter());

                list.add(details);
            }

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("CmRepBankReconciliationControllerBean ejbCreate");
    }
}