/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmBankReconciliationControllerBean
 * @created November 5, 2003, 11:13 AM
 * @author Dennis M. Hilario
 */
package com.ejb.txn.cm;

import java.util.*;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdBankAccountBalance;
import com.ejb.dao.ad.LocalAdBankAccountBalanceHome;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ad.LocalAdBranchDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdBranchDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.dao.cm.LocalCmAdjustmentHome;
import com.ejb.entities.cm.LocalCmDistributionRecord;
import com.ejb.dao.cm.LocalCmDistributionRecordHome;
import com.ejb.entities.cm.LocalCmFundTransfer;
import com.ejb.dao.cm.LocalCmFundTransferHome;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.GlobalJournalNotBalanceException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.exception.global.GlobalTransactionAlreadyPostedException;
import com.ejb.exception.global.GlobalTransactionAlreadyVoidException;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountBalanceHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlChartOfAccountBalance;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.gl.LocalGlJournal;
import com.ejb.entities.gl.LocalGlJournalBatch;
import com.ejb.dao.gl.LocalGlJournalBatchHome;
import com.ejb.entities.gl.LocalGlJournalCategory;
import com.ejb.dao.gl.LocalGlJournalCategoryHome;
import com.ejb.dao.gl.LocalGlJournalHome;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.entities.gl.LocalGlJournalSource;
import com.ejb.dao.gl.LocalGlJournalSourceHome;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.ejb.entities.gl.LocalGlSuspenseAccount;
import com.ejb.dao.gl.LocalGlSuspenseAccountHome;
import com.util.cm.CmAdjustmentDetails;
import com.util.mod.ad.AdModBankAccountDetails;
import com.util.mod.cm.CmModBankReconciliationDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;

@Stateless(name = "CmBankReconciliationControllerEJB")
public class CmBankReconciliationControllerBean extends EJBContextClass implements CmBankReconciliationController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    LocalAdBankAccountHome adBankAccountHome;
    @EJB
    LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    LocalArReceiptHome arReceiptHome;
    @EJB
    LocalCmAdjustmentHome cmAdjustmentHome;
    @EJB
    LocalCmFundTransferHome cmFundTransferHome;
    @EJB
    LocalApCheckHome apCheckHome;
    @EJB
    LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    LocalCmDistributionRecordHome cmDistributionRecordHome;
    @EJB
    LocalGlJournalHome glJournalHome;
    @EJB
    LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    LocalGlSuspenseAccountHome glSuspenseAccountHome;
    @EJB
    LocalGlJournalLineHome glJournalLineHome;
    @EJB
    LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;

    public ArrayList getAdBaAll(Integer branchCode, Integer companyCode) {

        Debug.print("CmBankReconciliationControllerBean getAdBaAll");

        Collection adBankAccounts = null;

        ArrayList list = new ArrayList();

        try {

            adBankAccounts = adBankAccountHome.findEnabledAndNotCashAccountBaAll(branchCode, companyCode);

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

    public ArrayList getGlFcAllWithDefault(Integer companyCode) {

        Debug.print("CmBankReconciliationControllerBean getGlFcAllWithDefault");

        Collection glFunctionalCurrencies = null;

        LocalGlFunctionalCurrency glFunctionalCurrency = null;
        LocalAdCompany adCompany = null;

        ArrayList list = new ArrayList();

        try {

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAllEnabled(EJBCommon.getGcCurrentDateWoTime().getTime(), companyCode);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glFunctionalCurrencies.isEmpty()) {

            return null;
        }

        for (Object functionalCurrency : glFunctionalCurrencies) {

            glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;

            GlModFunctionalCurrencyDetails mdetails = new GlModFunctionalCurrencyDetails(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), adCompany.getGlFunctionalCurrency().getFcName().equals(glFunctionalCurrency.getFcName()) ? EJBCommon.TRUE : EJBCommon.FALSE);

            list.add(mdetails);
        }

        return list;
    }

    public AdModBankAccountDetails getAdBaByBaName(String BA_NM, Date RCNCL_DT, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("CmBankReconciliationControllerBean getAdBaByBaName");

        double bankAccountBalance = 0;
        double lastReconciledBalance = 0;
        Date lastReconciledDate = null;

        try {

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);

            // check if there's existing prior bank recon

            Collection adExistingBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(RCNCL_DT, adBankAccount.getBaCode(), "RECON", companyCode);

            if (!adExistingBankAccountBalances.isEmpty()) throw new GlobalRecordAlreadyExistException();

            // get latest bank account balance for current bank account as type "BOOK"

            Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(RCNCL_DT, adBankAccount.getBaCode(), "BOOK", companyCode);

            if (!adBankAccountBalances.isEmpty()) {

                // get last check

                ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                bankAccountBalance = adBankAccountBalance.getBabBalance();
            }

            // get latest bank account balance for current bank account as type "RECON"

            adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(RCNCL_DT, adBankAccount.getBaCode(), "RECON", companyCode);

            if (!adBankAccountBalances.isEmpty()) {

                // get last check

                ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                lastReconciledBalance = adBankAccountBalance.getBabBalance();
                lastReconciledDate = adBankAccountBalance.getBabDate();
            }

            AdModBankAccountDetails mdetails = new AdModBankAccountDetails();

            mdetails.setBaAvailableBalance(bankAccountBalance);
            mdetails.setBaLastReconciledBalance(lastReconciledBalance);
            mdetails.setBaLastReconciledDate(lastReconciledDate);
            mdetails.setBaFcName(adBankAccount.getGlFunctionalCurrency().getFcName());

            return mdetails;

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getCmDepositInTransitByBaName(String BA_NM, Date RCNCL_DT, Integer branchCode, Integer companyCode) {

        Debug.print("CmBankReconciliationControllerBean getCmDepositInTransitByBaName");

        ArrayList list = new ArrayList();

        try {

            // get cash receipts

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);

            Collection arReceipts = arReceiptHome.findUnreconciledPostedRctByDateAndBaName(RCNCL_DT, BA_NM, companyCode);
            Debug.print("arReceipts.size()=" + arReceipts.size());
            Iterator i = arReceipts.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                CmModBankReconciliationDetails mdetails = new CmModBankReconciliationDetails();

                mdetails.setBrCode(arReceipt.getRctCode());
                mdetails.setBrType("RECEIPT CASH");
                mdetails.setBrDate(arReceipt.getRctDate());
                mdetails.setBrDocumentNumber(arReceipt.getRctNumber());
                mdetails.setBrReferenceNumber(arReceipt.getRctReferenceNumber());
                mdetails.setBrNumber("CASH");
                // mdetails.setBrAmount(arReceipt.getRctAmount());

                // pick up of receipt from convert to php
                LocalAdBankAccount adBankAccountTo = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());

                if ((Objects.equals(adBankAccount.getBaCode(), adBankAccountTo.getBaCode())) && (arReceipt.getRctConversionRate() > 1)) {
                    mdetails.setBrAmount(arReceipt.getRctAmountCash() * arReceipt.getRctConversionRate());
                } else {
                    mdetails.setBrAmount(arReceipt.getRctAmountCash());
                }

                mdetails.setBrName(arReceipt.getArCustomer().getCstCustomerCode());

                list.add(mdetails);
            }

            // get card 1

            adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);

            Collection arReceiptsCard1 = arReceiptHome.findUnreconciledPostedCard1RctByDateAndBaName(RCNCL_DT, BA_NM, companyCode);
            Debug.print("arReceipts.size()=" + arReceiptsCard1.size());
            i = arReceiptsCard1.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                CmModBankReconciliationDetails mdetails = new CmModBankReconciliationDetails();

                mdetails.setBrCode(arReceipt.getRctCode());
                mdetails.setBrType("RECEIPT CARD1");
                mdetails.setBrDate(arReceipt.getRctDate());
                mdetails.setBrDocumentNumber(arReceipt.getRctNumber());
                mdetails.setBrReferenceNumber(arReceipt.getRctReferenceNumber());
                mdetails.setBrNumber(arReceipt.getRctCardNumber1());

                // pick up of receipt from convert to php
                LocalAdBankAccount adBankAccountTo = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard1().getBaCode());

                if ((Objects.equals(adBankAccount.getBaCode(), adBankAccountTo.getBaCode())) && (arReceipt.getRctConversionRate() > 1)) {
                    mdetails.setBrAmount(arReceipt.getRctAmountCard1() * arReceipt.getRctConversionRate());
                } else {
                    mdetails.setBrAmount(arReceipt.getRctAmountCard1());
                }

                mdetails.setBrName(arReceipt.getArCustomer().getCstCustomerCode());

                list.add(mdetails);
            }

            // get card 2

            adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);

            Collection arReceiptsCard2 = arReceiptHome.findUnreconciledPostedCard2RctByDateAndBaName(RCNCL_DT, BA_NM, companyCode);
            Debug.print("arReceiptsCard2.size()=" + arReceiptsCard2.size());
            i = arReceiptsCard2.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                CmModBankReconciliationDetails mdetails = new CmModBankReconciliationDetails();

                mdetails.setBrCode(arReceipt.getRctCode());
                mdetails.setBrType("RECEIPT CARD2");
                mdetails.setBrDate(arReceipt.getRctDate());
                mdetails.setBrDocumentNumber(arReceipt.getRctNumber());
                mdetails.setBrReferenceNumber(arReceipt.getRctReferenceNumber());
                mdetails.setBrNumber(arReceipt.getRctCardNumber2());

                // pick up of receipt from convert to php
                LocalAdBankAccount adBankAccountTo = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard2().getBaCode());

                if ((Objects.equals(adBankAccount.getBaCode(), adBankAccountTo.getBaCode())) && (arReceipt.getRctConversionRate() > 1)) {
                    mdetails.setBrAmount(arReceipt.getRctAmountCard2() * arReceipt.getRctConversionRate());
                } else {
                    mdetails.setBrAmount(arReceipt.getRctAmountCard2());
                }

                mdetails.setBrName(arReceipt.getArCustomer().getCstCustomerCode());

                list.add(mdetails);
            }

            // get card 3

            adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);

            Collection arReceiptsCard3 = arReceiptHome.findUnreconciledPostedCard3RctByDateAndBaName(RCNCL_DT, BA_NM, companyCode);
            Debug.print("arReceiptsCard3.size()=" + arReceiptsCard3.size());
            i = arReceiptsCard3.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                CmModBankReconciliationDetails mdetails = new CmModBankReconciliationDetails();

                mdetails.setBrCode(arReceipt.getRctCode());
                mdetails.setBrType("RECEIPT CARD3");
                mdetails.setBrDate(arReceipt.getRctDate());
                mdetails.setBrDocumentNumber(arReceipt.getRctNumber());
                mdetails.setBrReferenceNumber(arReceipt.getRctReferenceNumber());
                mdetails.setBrNumber(arReceipt.getRctCardNumber3());

                // pick up of receipt from convert to php
                LocalAdBankAccount adBankAccountTo = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccountCard3().getBaCode());

                if ((Objects.equals(adBankAccount.getBaCode(), adBankAccountTo.getBaCode())) && (arReceipt.getRctConversionRate() > 1)) {
                    mdetails.setBrAmount(arReceipt.getRctAmountCard3() * arReceipt.getRctConversionRate());
                } else {
                    mdetails.setBrAmount(arReceipt.getRctAmountCard3());
                }

                mdetails.setBrName(arReceipt.getArCustomer().getCstCustomerCode());

                list.add(mdetails);
            }

            // get check receipts

            adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);

            Collection arReceiptsCheque = arReceiptHome.findUnreconciledPostedChequeRctByDateAndBaName(RCNCL_DT, BA_NM, companyCode);
            Debug.print("arReceiptsCheque.size()=" + arReceiptsCheque.size());
            i = arReceiptsCheque.iterator();

            while (i.hasNext()) {

                LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                CmModBankReconciliationDetails mdetails = new CmModBankReconciliationDetails();

                mdetails.setBrCode(arReceipt.getRctCode());
                mdetails.setBrType("RECEIPT CHEQUE");
                mdetails.setBrDate(arReceipt.getRctDate());
                mdetails.setBrDocumentNumber(arReceipt.getRctNumber());
                mdetails.setBrReferenceNumber(arReceipt.getRctReferenceNumber());
                mdetails.setBrNumber(arReceipt.getRctChequeNumber());

                // pick up of receipt from convert to php
                LocalAdBankAccount adBankAccountTo = adBankAccountHome.findByPrimaryKey(arReceipt.getAdBankAccount().getBaCode());

                if ((Objects.equals(adBankAccount.getBaCode(), adBankAccountTo.getBaCode())) && (arReceipt.getRctConversionRate() > 1)) {
                    mdetails.setBrAmount(arReceipt.getRctAmountCheque() * arReceipt.getRctConversionRate());
                } else {
                    mdetails.setBrAmount(arReceipt.getRctAmountCheque());
                }

                mdetails.setBrName(arReceipt.getArCustomer().getCstCustomerCode());

                list.add(mdetails);
            }

            // get bank debits

            Collection cmAdjustments = cmAdjustmentHome.findUnreconciledPostedAdjByDateAndBaNameAndAdjType(RCNCL_DT, BA_NM, "DEBIT MEMO", companyCode);
            Debug.print("DEBIT cmAdjustments.size()=" + cmAdjustments.size());
            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                CmModBankReconciliationDetails mdetails = new CmModBankReconciliationDetails();

                mdetails.setBrCode(cmAdjustment.getAdjCode());
                mdetails.setBrType("DEBIT MEMO");
                mdetails.setBrDate(cmAdjustment.getAdjDate());
                mdetails.setBrDocumentNumber(cmAdjustment.getAdjDocumentNumber());
                mdetails.setBrReferenceNumber(cmAdjustment.getAdjReferenceNumber());
                mdetails.setBrAmount(cmAdjustment.getAdjAmount());

                list.add(mdetails);
            }

            // get CM ADVANCE

            Collection cmAdjustmentAdvances = cmAdjustmentHome.findUnreconciledPostedAdjByDateAndBaNameAndAdjType(RCNCL_DT, BA_NM, "ADVANCE", companyCode);

            i = cmAdjustmentAdvances.iterator();
            Debug.print("cmAdjustmentAdvances=" + cmAdjustmentAdvances.size());
            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                CmModBankReconciliationDetails mdetails = new CmModBankReconciliationDetails();

                mdetails.setBrCode(cmAdjustment.getAdjCode());
                mdetails.setBrType("ADVANCE");
                mdetails.setBrDate(cmAdjustment.getAdjDate());
                mdetails.setBrDocumentNumber(cmAdjustment.getAdjDocumentNumber());
                mdetails.setBrReferenceNumber(cmAdjustment.getAdjReferenceNumber());
                mdetails.setBrName(cmAdjustment.getArCustomer().getCstCustomerCode());

                mdetails.setBrAmount(cmAdjustment.getAdjAmount());

                list.add(mdetails);
            }

            cmAdjustments = cmAdjustmentHome.findUnreconciledPostedAdjByDateAndBaNameAndAdjType(RCNCL_DT, BA_NM, "INTEREST", companyCode);
            Debug.print("Interest cmAdjustments=" + cmAdjustments.size());
            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                CmModBankReconciliationDetails mdetails = new CmModBankReconciliationDetails();

                mdetails.setBrCode(cmAdjustment.getAdjCode());
                mdetails.setBrType("INTEREST");
                mdetails.setBrDate(cmAdjustment.getAdjDate());
                mdetails.setBrDocumentNumber(cmAdjustment.getAdjDocumentNumber());
                mdetails.setBrReferenceNumber(cmAdjustment.getAdjReferenceNumber());
                mdetails.setBrAmount(cmAdjustment.getAdjAmount());

                list.add(mdetails);
            }

            // get fund transfers

            Collection cmFundTransfers = cmFundTransferHome.findUnreconciledPostedFtAccountToByDateAndBaCode(RCNCL_DT, adBankAccount.getBaCode(), companyCode);
            Debug.print("cmFundTransfers=" + cmFundTransfers.size());
            i = cmFundTransfers.iterator();

            while (i.hasNext()) {

                LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) i.next();

                CmModBankReconciliationDetails mdetails = new CmModBankReconciliationDetails();

                mdetails.setBrCode(cmFundTransfer.getFtCode());
                mdetails.setBrType("FUND TRANSFER IN");
                mdetails.setBrDate(cmFundTransfer.getFtDate());
                mdetails.setBrDocumentNumber(cmFundTransfer.getFtDocumentNumber());
                mdetails.setBrReferenceNumber(cmFundTransfer.getFtReferenceNumber());

                // pick up of fund transfer from convert to php
                LocalAdBankAccount adBankAccountTo = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountTo());

                if ((Objects.equals(adBankAccount.getBaCode(), adBankAccountTo.getBaCode())) && (cmFundTransfer.getFtConversionRateFrom() > 1)) {
                    mdetails.setBrAmount(cmFundTransfer.getFtAmount() * cmFundTransfer.getFtConversionRateFrom());
                } else {
                    mdetails.setBrAmount(cmFundTransfer.getFtAmount());
                }

                list.add(mdetails);
            }
            Debug.print("list=" + list.size());
            list.sort(CmModBankReconciliationDetails.NoGroupComparator);

            Debug.print("RETURN---------------------->");
            return list;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getCmOutstandingCheckByBaName(String BA_NM, Date RCNCL_DT, Integer branchCode, Integer companyCode) {

        Debug.print("CmBankReconciliationControllerBean getCmOutstandingCheckByBaName");

        ArrayList list = new ArrayList();

        try {

            Collection apChecks = apCheckHome.findUnreconciledPostedChkByDateAndBaName(RCNCL_DT, BA_NM, companyCode);

            Iterator i = apChecks.iterator();

            while (i.hasNext()) {

                LocalApCheck apCheck = (LocalApCheck) i.next();

                CmModBankReconciliationDetails mdetails = new CmModBankReconciliationDetails();

                mdetails.setBrCode(apCheck.getChkCode());
                mdetails.setBrType("CHECK");
                mdetails.setBrDate(apCheck.getChkDate());
                mdetails.setBrDocumentNumber(apCheck.getChkDocumentNumber());
                mdetails.setBrReferenceNumber(apCheck.getChkReferenceNumber());
                mdetails.setBrNumber(apCheck.getChkNumber());
                mdetails.setBrAmount(apCheck.getChkAmount());
                mdetails.setBrName(apCheck.getApSupplier().getSplSupplierCode());
                list.add(mdetails);
            }

            Debug.print("apChecks=" + apChecks.size());

            // get bank credits

            Collection cmAdjustments = cmAdjustmentHome.findUnreconciledPostedAdjByDateAndBaNameAndAdjType(RCNCL_DT, BA_NM, "CREDIT MEMO", companyCode);

            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                CmModBankReconciliationDetails mdetails = new CmModBankReconciliationDetails();

                mdetails.setBrCode(cmAdjustment.getAdjCode());
                mdetails.setBrType("CREDIT MEMO");
                mdetails.setBrDate(cmAdjustment.getAdjDate());
                mdetails.setBrDocumentNumber(cmAdjustment.getAdjDocumentNumber());
                mdetails.setBrReferenceNumber(cmAdjustment.getAdjReferenceNumber());

                mdetails.setBrAmount(cmAdjustment.getAdjAmount());

                list.add(mdetails);
            }

            cmAdjustments = cmAdjustmentHome.findUnreconciledPostedAdjByDateAndBaNameAndAdjType(RCNCL_DT, BA_NM, "BANK CHARGE", companyCode);

            i = cmAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                CmModBankReconciliationDetails mdetails = new CmModBankReconciliationDetails();

                mdetails.setBrCode(cmAdjustment.getAdjCode());
                mdetails.setBrType("BANK CHARGE");
                mdetails.setBrDate(cmAdjustment.getAdjDate());
                mdetails.setBrDocumentNumber(cmAdjustment.getAdjDocumentNumber());
                mdetails.setBrReferenceNumber(cmAdjustment.getAdjReferenceNumber());

                mdetails.setBrAmount(cmAdjustment.getAdjAmount());

                list.add(mdetails);
            }

            // get fund transfers

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);
            Collection cmFundTransfers = cmFundTransferHome.findUnreconciledPostedFtAccountFromByDateAndBaCode(RCNCL_DT, adBankAccount.getBaCode(), companyCode);

            i = cmFundTransfers.iterator();

            while (i.hasNext()) {

                LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) i.next();

                CmModBankReconciliationDetails mdetails = new CmModBankReconciliationDetails();

                mdetails.setBrCode(cmFundTransfer.getFtCode());
                mdetails.setBrType("FUND TRANSFER OUT");
                mdetails.setBrDate(cmFundTransfer.getFtDate());
                mdetails.setBrDocumentNumber(cmFundTransfer.getFtDocumentNumber());
                mdetails.setBrReferenceNumber(cmFundTransfer.getFtReferenceNumber());

                mdetails.setBrAmount(cmFundTransfer.getFtAmount());

                list.add(mdetails);
            }
            list.sort(CmModBankReconciliationDetails.NoGroupComparator);

            return list;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public void executeCmBankReconciliation(double OPNNG_BLNC, double ENDNG_BLNC, String BA_NM, CmAdjustmentDetails adjustmentDetails, CmAdjustmentDetails interestDetails, CmAdjustmentDetails serviceChargeDetails, ArrayList depositInTransitList, ArrayList outstandingCheckList, Date reconcileDate, boolean autoAdjust, Integer branchCode, Integer companyCode) throws GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalRecordAlreadyDeletedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("CmBankReconciliationControllerBean executeCmBankReconciliation");

        try {

            // get bank account

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, companyCode);

            // get last cleared balance

            double CLRD_BLNC = OPNNG_BLNC;

            // reconcile deposit in transit
            Debug.print("depositInTransitList.size()=" + depositInTransitList.size());
            Iterator i = depositInTransitList.iterator();

            while (i.hasNext()) {

                CmModBankReconciliationDetails mdetails = (CmModBankReconciliationDetails) i.next();

                switch (mdetails.getBrType()) {
                    case "RECEIPT CASH": {

                        LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey(mdetails.getBrCode());

                        arReceipt.setRctReconciled(EJBCommon.TRUE);
                        arReceipt.setRctDateReconciled(mdetails.getBrDateCleared());

                        // update cleared balance

                        CLRD_BLNC += arReceipt.getRctAmountCash();

                        break;
                    }
                    case "RECEIPT CHEQUE": {

                        LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey(mdetails.getBrCode());

                        arReceipt.setRctReconciledCheque(EJBCommon.TRUE);
                        arReceipt.setRctDateReconciledCheque(mdetails.getBrDateCleared());

                        // update cleared balance

                        CLRD_BLNC += arReceipt.getRctAmountCheque();

                        break;
                    }
                    case "RECEIPT CARD1": {

                        LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey(mdetails.getBrCode());

                        arReceipt.setRctReconciledCard1(EJBCommon.TRUE);
                        arReceipt.setRctDateReconciledCard1(mdetails.getBrDateCleared());

                        // update cleared balance

                        CLRD_BLNC += arReceipt.getRctAmountCard1();

                        break;
                    }
                    case "RECEIPT CARD2": {

                        LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey(mdetails.getBrCode());

                        arReceipt.setRctReconciledCard2(EJBCommon.TRUE);
                        arReceipt.setRctDateReconciledCard2(mdetails.getBrDateCleared());

                        // update cleared balance

                        CLRD_BLNC += arReceipt.getRctAmountCard2();

                        break;
                    }
                    case "RECEIPT CARD3": {

                        LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey(mdetails.getBrCode());

                        arReceipt.setRctReconciledCard3(EJBCommon.TRUE);
                        arReceipt.setRctDateReconciledCard3(mdetails.getBrDateCleared());

                        // update cleared balance

                        CLRD_BLNC += arReceipt.getRctAmountCard3();

                        break;
                    }
                    case "DEBIT MEMO":
                    case "INTEREST":
                    case "ADVANCE":

                        LocalCmAdjustment cmAdjustment = cmAdjustmentHome.findByPrimaryKey(mdetails.getBrCode());

                        cmAdjustment.setAdjReconciled(EJBCommon.TRUE);
                        cmAdjustment.setAdjDateReconciled(mdetails.getBrDateCleared());

                        // update cleared balance

                        CLRD_BLNC += cmAdjustment.getAdjAmount();

                        break;
                    default:

                        LocalCmFundTransfer cmFundTransfer = cmFundTransferHome.findByPrimaryKey(mdetails.getBrCode());

                        cmFundTransfer.setFtAccountToReconciled(EJBCommon.TRUE);
                        cmFundTransfer.setFtAccountToDateReconciled(mdetails.getBrDateCleared());

                        // update cleared balance

                        CLRD_BLNC += cmFundTransfer.getFtAmount();
                        break;
                }
            }

            // reconcile outstanding checks
            Debug.print("outstandingCheckList.size()=" + outstandingCheckList.size());
            i = outstandingCheckList.iterator();

            while (i.hasNext()) {

                CmModBankReconciliationDetails mdetails = (CmModBankReconciliationDetails) i.next();

                if (mdetails.getBrType().equals("CHECK")) {

                    LocalApCheck apCheck = apCheckHome.findByPrimaryKey(mdetails.getBrCode());

                    apCheck.setChkReconciled(EJBCommon.TRUE);
                    apCheck.setChkDateReconciled(mdetails.getBrDateCleared());

                    // update cleared balance

                    CLRD_BLNC -= apCheck.getChkAmount();

                } else if (mdetails.getBrType().equals("CREDIT MEMO") || mdetails.getBrType().equals("BANK CHARGE")) {

                    LocalCmAdjustment cmAdjustment = cmAdjustmentHome.findByPrimaryKey(mdetails.getBrCode());

                    cmAdjustment.setAdjReconciled(EJBCommon.TRUE);
                    cmAdjustment.setAdjDateReconciled(mdetails.getBrDateCleared());

                    // update cleared balance

                    CLRD_BLNC -= cmAdjustment.getAdjAmount();

                } else {

                    LocalCmFundTransfer cmFundTransfer = cmFundTransferHome.findByPrimaryKey(mdetails.getBrCode());

                    cmFundTransfer.setFtAccountFromReconciled(EJBCommon.TRUE);
                    cmFundTransfer.setFtAccountFromDateReconciled(mdetails.getBrDateCleared());

                    // update cleared balance

                    CLRD_BLNC -= cmFundTransfer.getFtAmount();
                }
            }

            // create interest adjustment if necessary

            if (interestDetails != null) {

                // generate document number

                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("CM ADJUSTMENT", companyCode);

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                try {

                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);

                } catch (FinderException ex) {

                }

                String ADJ_DCMNT_NMBR = null;

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (ADJ_DCMNT_NMBR == null || ADJ_DCMNT_NMBR.trim().length() == 0)) {

                    while (true) {

                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                            try {

                                cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                            } catch (FinderException ex) {

                                ADJ_DCMNT_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {

                                cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                            } catch (FinderException ex) {

                                ADJ_DCMNT_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }

                LocalCmAdjustment cmAdjustment = cmAdjustmentHome.create("INTEREST", interestDetails.getAdjDate(), ADJ_DCMNT_NMBR, interestDetails.getAdjReferenceNumber(), interestDetails.getAdjCheckNumber(), interestDetails.getAdjAmount(), 0d, interestDetails.getAdjConversionDate(), interestDetails.getAdjConversionRate(), interestDetails.getAdjMemo(), null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, 0d, null, EJBCommon.TRUE, interestDetails.getAdjDate(), "N/A", EJBCommon.FALSE, interestDetails.getAdjCreatedBy(), interestDetails.getAdjDateCreated(), interestDetails.getAdjLastModifiedBy(), interestDetails.getAdjDateLastModified(), null, null, interestDetails.getAdjPostedBy(), interestDetails.getAdjDatePosted(), null, branchCode, companyCode);

                adBankAccount.addCmAdjustment(cmAdjustment);

                // update bank balance

                try {

                    // find bankaccount balance before or equal check date

                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(interestDetails.getAdjDate(), adBankAccount.getBaCode(), "BOOK", companyCode);

                    if (!adBankAccountBalances.isEmpty()) {

                        // get last check

                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(interestDetails.getAdjDate())) {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(interestDetails.getAdjDate(), adBankAccountBalance.getBabBalance() + interestDetails.getAdjAmount(), "BOOK", companyCode);

                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + interestDetails.getAdjAmount());
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(interestDetails.getAdjDate(), (interestDetails.getAdjAmount()), "BOOK", companyCode);

                        adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(interestDetails.getAdjDate(), adBankAccount.getBaCode(), "BOOK", companyCode);

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + interestDetails.getAdjAmount());
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

                // create distribution record

                this.createDistributionRecord(cmAdjustment, (short) 1, interestDetails.getAdjDate(), "CASH", EJBCommon.TRUE, interestDetails.getAdjAmount(), EJBCommon.FALSE, adBankAccount.getBaCoaGlCashAccount(), companyCode);

                this.createDistributionRecord(cmAdjustment, (short) 2, interestDetails.getAdjDate(), "BANK INTEREST", EJBCommon.FALSE, interestDetails.getAdjAmount(), EJBCommon.FALSE, adBankAccount.getBaCoaGlInterestAccount(), companyCode);

                // update cleared balance

                CLRD_BLNC += interestDetails.getAdjAmount();

                this.executeCmAdjPost(cmAdjustment.getAdjCode(), interestDetails.getAdjLastModifiedBy(), branchCode, companyCode);
            }

            // create service charge adjustment if necessary

            if (serviceChargeDetails != null) {

                // generate document number

                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("CM ADJUSTMENT", companyCode);

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                try {

                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);

                } catch (FinderException ex) {

                }

                String ADJ_DCMNT_NMBR = null;

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (ADJ_DCMNT_NMBR == null || ADJ_DCMNT_NMBR.trim().length() == 0)) {

                    while (true) {

                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                            try {

                                cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                            } catch (FinderException ex) {

                                ADJ_DCMNT_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {

                                cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                            } catch (FinderException ex) {

                                ADJ_DCMNT_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }

                LocalCmAdjustment cmAdjustment = cmAdjustmentHome.create("BANK CHARGE", serviceChargeDetails.getAdjDate(), ADJ_DCMNT_NMBR, serviceChargeDetails.getAdjReferenceNumber(), serviceChargeDetails.getAdjCheckNumber(), serviceChargeDetails.getAdjAmount(), 0d, serviceChargeDetails.getAdjConversionDate(), serviceChargeDetails.getAdjConversionRate(), serviceChargeDetails.getAdjMemo(), null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, 0d, null, EJBCommon.TRUE, serviceChargeDetails.getAdjDate(), "N/A", EJBCommon.FALSE, serviceChargeDetails.getAdjCreatedBy(), serviceChargeDetails.getAdjDateCreated(), serviceChargeDetails.getAdjLastModifiedBy(), serviceChargeDetails.getAdjDateLastModified(), null, null, serviceChargeDetails.getAdjPostedBy(), serviceChargeDetails.getAdjDatePosted(), null, branchCode, companyCode);

                adBankAccount.addCmAdjustment(cmAdjustment);

                // update bank balance

                try {

                    // find bankaccount balance before or equal check date

                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(serviceChargeDetails.getAdjDate(), adBankAccount.getBaCode(), "BOOK", companyCode);

                    if (!adBankAccountBalances.isEmpty()) {

                        // get last check

                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(serviceChargeDetails.getAdjDate())) {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(serviceChargeDetails.getAdjDate(), adBankAccountBalance.getBabBalance() - serviceChargeDetails.getAdjAmount(), "BOOK", companyCode);

                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - serviceChargeDetails.getAdjAmount());
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(serviceChargeDetails.getAdjDate(), (0 - serviceChargeDetails.getAdjAmount()), "BOOK", companyCode);

                        adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(serviceChargeDetails.getAdjDate(), adBankAccount.getBaCode(), "BOOK", companyCode);

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - serviceChargeDetails.getAdjAmount());
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

                // create distribution record

                this.createDistributionRecord(cmAdjustment, (short) 1, serviceChargeDetails.getAdjDate(), "BANK CHARGE", EJBCommon.TRUE, serviceChargeDetails.getAdjAmount(), EJBCommon.FALSE, adBankAccount.getBaCoaGlBankChargeAccount(), companyCode);

                this.createDistributionRecord(cmAdjustment, (short) 2, serviceChargeDetails.getAdjDate(), "CASH", EJBCommon.FALSE, serviceChargeDetails.getAdjAmount(), EJBCommon.FALSE, adBankAccount.getBaCoaGlCashAccount(), companyCode);

                // update cleared balance

                CLRD_BLNC -= serviceChargeDetails.getAdjAmount();

                this.executeCmAdjPost(cmAdjustment.getAdjCode(), serviceChargeDetails.getAdjLastModifiedBy(), branchCode, companyCode);
            }

            // create adjustments if necessary

            CLRD_BLNC = EJBCommon.roundIt(CLRD_BLNC, this.getGlFcPrecisionUnit(companyCode));
            ENDNG_BLNC = EJBCommon.roundIt(ENDNG_BLNC, this.getGlFcPrecisionUnit(companyCode));

            // create recon bank account balance

            if (CLRD_BLNC != ENDNG_BLNC && autoAdjust) {

                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(reconcileDate, ENDNG_BLNC, "RECON", companyCode);

                adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

            } else {

                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(reconcileDate, CLRD_BLNC, "RECON", companyCode);

                adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
            }

            if (CLRD_BLNC != ENDNG_BLNC && autoAdjust) {

                // generate document number

                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("CM ADJUSTMENT", companyCode);

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

                try {

                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), branchCode, companyCode);

                } catch (FinderException ex) {

                }

                String ADJ_DCMNT_NMBR = null;

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (ADJ_DCMNT_NMBR == null || ADJ_DCMNT_NMBR.trim().length() == 0)) {

                    while (true) {

                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                            try {

                                cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), branchCode, companyCode);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                            } catch (FinderException ex) {

                                ADJ_DCMNT_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {

                                cmAdjustmentHome.findByAdjDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), branchCode, companyCode);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                            } catch (FinderException ex) {

                                ADJ_DCMNT_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }
                Debug.print("ADJ_DCMNT_NMBR=" + ADJ_DCMNT_NMBR);
                LocalCmAdjustment cmAdjustment = cmAdjustmentHome.create(ENDNG_BLNC - CLRD_BLNC > 0 ? "DEBIT MEMO" : "CREDIT MEMO", adjustmentDetails.getAdjDate(), ADJ_DCMNT_NMBR, "RECONCILIATION ADJUSTMENT", adjustmentDetails.getAdjCheckNumber(), Math.abs(ENDNG_BLNC - CLRD_BLNC), 0d, adjustmentDetails.getAdjConversionDate(), adjustmentDetails.getAdjConversionRate(), "ADJUSTMENT", null, EJBCommon.FALSE, EJBCommon.FALSE, EJBCommon.FALSE, 0d, null, EJBCommon.TRUE, adjustmentDetails.getAdjDateReconciled(), "N/A", EJBCommon.TRUE, adjustmentDetails.getAdjCreatedBy(), adjustmentDetails.getAdjDateCreated(), adjustmentDetails.getAdjLastModifiedBy(), adjustmentDetails.getAdjDateLastModified(), null, null, null, null, null, branchCode, companyCode);
                cmAdjustment.setAdjPosted((byte) 0);
                adBankAccount.addCmAdjustment(cmAdjustment);

                // create distribution record

                if (ENDNG_BLNC - CLRD_BLNC > 0) {

                    this.createDistributionRecord(cmAdjustment, (short) 1, adjustmentDetails.getAdjDate(), "CASH", EJBCommon.TRUE, Math.abs(ENDNG_BLNC - CLRD_BLNC), EJBCommon.FALSE, adBankAccount.getBaCoaGlCashAccount(), companyCode);

                    this.createDistributionRecord(cmAdjustment, (short) 2, adjustmentDetails.getAdjDate(), "ADJUSTMENT", EJBCommon.FALSE, Math.abs(ENDNG_BLNC - CLRD_BLNC), EJBCommon.FALSE, adBankAccount.getBaCoaGlAdjustmentAccount(), companyCode);

                } else {

                    this.createDistributionRecord(cmAdjustment, (short) 1, adjustmentDetails.getAdjDate(), "ADJUSTMENT", EJBCommon.TRUE, Math.abs(ENDNG_BLNC - CLRD_BLNC), EJBCommon.FALSE, adBankAccount.getBaCoaGlAdjustmentAccount(), companyCode);

                    this.createDistributionRecord(cmAdjustment, (short) 2, adjustmentDetails.getAdjDate(), "CASH", EJBCommon.FALSE, Math.abs(ENDNG_BLNC - CLRD_BLNC), EJBCommon.FALSE, adBankAccount.getBaCoaGlCashAccount(), companyCode);
                }
                Debug.print("pasok 111");
                this.executeCmAdjPost(cmAdjustment.getAdjCode(), adjustmentDetails.getAdjLastModifiedBy(), branchCode, companyCode);
            }

            // update bank account balances

            adBankAccount.setBaLastReconciledDate(EJBCommon.getGcCurrentDateWoTime().getTime());
            adBankAccount.setBaLastReconciledBalance(ENDNG_BLNC);

        } catch (GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException | GlJREffectiveDateNoPeriodExistException |
                 GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void recomputeAccountBalanceByBankCode(String BA_NM, Integer branchCode, Integer companyCode) {

        Debug.print("CmBankReconciliationControllerBean recomputeAccountBalance");

        try {

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaNameAndBrCode(BA_NM, branchCode, companyCode);

            Collection adBankAccountBalances = adBankAccount.getAdBankAccountBalances();

            for (Object bankAccountBalance : adBankAccountBalances) {

                LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                Date RCNCL_DT = adBankAccountBalance.getBabDate();

                double accountBalance = 0;
                Iterator i;
                Collection arReceipts = arReceiptHome.findUnreconciledPostedRctByDateAndBaName(RCNCL_DT, BA_NM, companyCode);

                i = arReceipts.iterator();

                while (i.hasNext()) {
                    LocalArReceipt arReceipt = (LocalArReceipt) i.next();

                    accountBalance += arReceipt.getRctAmountCash();
                }

                Collection cmDebitMemoAdjustments = cmAdjustmentHome.findUnreconciledPostedAdjByDateAndBaNameAndAdjType(RCNCL_DT, BA_NM, "Debit Memo", companyCode);

                i = cmDebitMemoAdjustments.iterator();

                while (i.hasNext()) {
                    LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                    accountBalance += cmAdjustment.getAdjAmount();
                }

                Collection cmAdvanceAdjustments = cmAdjustmentHome.findUnreconciledPostedAdjByDateAndBaNameAndAdjType(RCNCL_DT, BA_NM, "Advance", companyCode);

                i = cmAdvanceAdjustments.iterator();

                while (i.hasNext()) {
                    LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                    accountBalance += cmAdjustment.getAdjAmount();
                }

                Collection cmInterestAdjustments = cmAdjustmentHome.findUnreconciledPostedAdjByDateAndBaNameAndAdjType(RCNCL_DT, BA_NM, "Interest", companyCode);

                i = cmInterestAdjustments.iterator();

                while (i.hasNext()) {
                    LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                    accountBalance += cmAdjustment.getAdjAmount();
                }

                Collection cmAccountToFundTransfers = cmFundTransferHome.findUnreconciledPostedFtAccountToByDateAndBaCode(RCNCL_DT, adBankAccount.getBaCode(), companyCode);

                i = cmAccountToFundTransfers.iterator();

                while (i.hasNext()) {
                    LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) i.next();

                    accountBalance += cmFundTransfer.getFtAmount();
                }

                Collection apChecks = apCheckHome.findUnreconciledPostedChkByDateAndBaName(RCNCL_DT, BA_NM, companyCode);

                i = apChecks.iterator();

                while (i.hasNext()) {
                    LocalApCheck apCheck = (LocalApCheck) i.next();

                    accountBalance -= apCheck.getChkAmount();
                }

                Collection cmCreditMemoAdjustments = cmAdjustmentHome.findUnreconciledPostedAdjByDateAndBaNameAndAdjType(RCNCL_DT, BA_NM, "Credit Memo", companyCode);

                i = cmCreditMemoAdjustments.iterator();

                while (i.hasNext()) {
                    LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                    accountBalance -= cmAdjustment.getAdjAmount();
                }

                Collection cmBankChargeAdjustments = cmAdjustmentHome.findUnreconciledPostedAdjByDateAndBaNameAndAdjType(RCNCL_DT, BA_NM, "Bank Charge", companyCode);

                i = cmBankChargeAdjustments.iterator();

                while (i.hasNext()) {
                    LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) i.next();

                    accountBalance -= cmAdjustment.getAdjAmount();
                }

                Collection cmAccountFromFundTransfers = cmFundTransferHome.findUnreconciledPostedFtAccountFromByDateAndBaCode(RCNCL_DT, adBankAccount.getBaCode(), companyCode);

                i = cmAccountToFundTransfers.iterator();

                while (i.hasNext()) {
                    LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) i.next();

                    accountBalance -= cmFundTransfer.getFtAmount();
                }

                Debug.print("Account Balance in " + RCNCL_DT + " is: " + accountBalance);

                adBankAccountBalance.setBabBalance(accountBalance);
            }

            // Collection adBankAccountBalances = adBankAccountBalanceHome.finb(BA_CODE,
            // BAB_TYP,
            // BAB_companyCode)apCheck.getChkCheckDate(),
            // apCheck.getAdBankAccount().getBaCode(), "BOOK",
            // companyCode);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("CmBankReconciliationControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private void createDistributionRecord(LocalCmAdjustment cmAdjustment, short DR_LN, Date DR_DT, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL, Integer GL_COA, Integer companyCode) {

        Debug.print("CmBankReconciliationControllerBean createDistributionRecord");

        short FC_PRCSN = 0;

        // get company and precision

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            FC_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // create distribution record for adjustment

        try {

            LocalCmDistributionRecord cmDistributionRecord = cmDistributionRecordHome.create(DR_LN, DR_CLSS, EJBCommon.roundIt(DR_AMNT, FC_PRCSN), DR_DBT, DR_RVRSL, EJBCommon.FALSE, companyCode);

            cmAdjustment.addCmDistributionRecord(cmDistributionRecord);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(GL_COA);
            glChartOfAccount.addCmDistributionRecord(cmDistributionRecord);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    private void executeCmAdjPost(Integer ADJ_CODE, String USR_NM, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("CmBankReconciliationControllerBean executeCmAdjPost");

        LocalCmAdjustment cmAdjustment = null;

        try {

            // validate if adjustment is already deleted

            try {
                Debug.print(ADJ_CODE + " code");
                cmAdjustment = cmAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted or void

            if (cmAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

            } else if (cmAdjustment.getAdjVoid() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidException();
            }

            // post adjustment

            if (cmAdjustment.getAdjVoid() == EJBCommon.FALSE && cmAdjustment.getAdjPosted() == EJBCommon.FALSE) {

                if (cmAdjustment.getAdjType().equals("INTEREST") || cmAdjustment.getAdjType().equals("DEBIT MEMO") || cmAdjustment.getAdjType().equals("ADVANCE")) {

                    // increase bank account balances

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmAdjustment.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(cmAdjustment.getAdjDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), adBankAccountBalance.getBabBalance() + cmAdjustment.getAdjAmount(), "BOOK", companyCode);

                                adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + cmAdjustment.getAdjAmount());
                            }

                        } else {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), (cmAdjustment.getAdjAmount()), "BOOK", companyCode);

                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + cmAdjustment.getAdjAmount());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }

                } else {

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmAdjustment.getAdBankAccount().getBaCode());

                    try {

                        // find bankaccount balance before or equal receipt date

                        Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        if (!adBankAccountBalances.isEmpty()) {

                            // get last check

                            ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                            if (adBankAccountBalance.getBabDate().before(cmAdjustment.getAdjDate())) {

                                // create new balance

                                LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), adBankAccountBalance.getBabBalance() - cmAdjustment.getAdjAmount(), "BOOK", companyCode);

                                adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);

                            } else { // equals to check date

                                adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - cmAdjustment.getAdjAmount());
                            }

                        } else {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmAdjustment.getAdjDate(), (0 - cmAdjustment.getAdjAmount()), "BOOK", companyCode);

                            adBankAccount.addAdBankAccountBalance(adNewBankAccountBalance);
                        }

                        // propagate to subsequent balances if necessary

                        adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(cmAdjustment.getAdjDate(), cmAdjustment.getAdBankAccount().getBaCode(), "BOOK", companyCode);

                        for (Object bankAccountBalance : adBankAccountBalances) {

                            LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - cmAdjustment.getAdjAmount());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
            }

            // set adjcher post status

            cmAdjustment.setAdjPosted(EJBCommon.TRUE);
            cmAdjustment.setAdjPostedBy(USR_NM);
            cmAdjustment.setAdjDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            if (!adPreference.getPrfCmGlPostingType().equals("USE JOURNAL INTERFACE")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(cmAdjustment.getAdjDate(), companyCode);

                } catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), cmAdjustment.getAdjDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection cmDistributionRecords = cmDistributionRecordHome.findByDrReversalAndDrImportedAndAdjCode(EJBCommon.FALSE, EJBCommon.FALSE, cmAdjustment.getAdjCode(), companyCode);

                Iterator j = cmDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) j.next();

                    double DR_AMNT = this.convertForeignToFunctionalCurrency(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode(), cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcName(), cmAdjustment.getAdjConversionDate(), cmAdjustment.getAdjConversionRate(), cmDistributionRecord.getDrAmount(), companyCode);

                    if (cmDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                        TOTAL_DEBIT += DR_AMNT;

                    } else {

                        TOTAL_CREDIT += DR_AMNT;
                    }
                }

                TOTAL_DEBIT = EJBCommon.roundIt(TOTAL_DEBIT, adCompany.getGlFunctionalCurrency().getFcPrecision());
                TOTAL_CREDIT = EJBCommon.roundIt(TOTAL_CREDIT, adCompany.getGlFunctionalCurrency().getFcPrecision());

                if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.TRUE && TOTAL_DEBIT != TOTAL_CREDIT) {

                    LocalGlSuspenseAccount glSuspenseAccount = null;

                    try {

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("CASH MANAGEMENT", "BANK ADJUSTMENTS", companyCode);

                    } catch (FinderException ex) {

                        throw new GlobalJournalNotBalanceException();
                    }

                    if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                        glOffsetJournalLine = glJournalLineHome.create((short) (cmDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                    } else {

                        glOffsetJournalLine = glJournalLineHome.create((short) (cmDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                    }

                    LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                    glChartOfAccount.addGlJournalLine(glOffsetJournalLine);

                } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                    throw new GlobalJournalNotBalanceException();
                }

                // create journal batch if necessary

                LocalGlJournalBatch glJournalBatch = null;
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

                try {

                    glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " BANK ADJUSTMENTS", branchCode, companyCode);

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                    glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " BANK ADJUSTMENTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(cmAdjustment.getAdjReferenceNumber(), cmAdjustment.getAdjMemo(), cmAdjustment.getAdjDate(), 0.0d, null, cmAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("CASH MANAGEMENT", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("BANK ADJUSTMENTS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = cmDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) j.next();

                    double DR_AMNT = this.convertForeignToFunctionalCurrency(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode(), cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcName(), cmAdjustment.getAdjConversionDate(), cmAdjustment.getAdjConversionRate(), cmDistributionRecord.getDrAmount(), companyCode);

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(cmDistributionRecord.getDrLine(), cmDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    cmDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);

                    glJournal.addGlJournalLine(glJournalLine);

                    cmDistributionRecord.setDrImported(EJBCommon.TRUE);
                }

                if (glOffsetJournalLine != null) {

                    glJournal.addGlJournalLine(glOffsetJournalLine);
                }

                // post journal to gl
                Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
                for (Object journalLine : glJournalLines) {

                    LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                    // post current to current acv

                    this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), branchCode, companyCode);

                    // post to subsequent acvs (propagate)

                    Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                    for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                        LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                        this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), branchCode, companyCode);
                    }

                    // post to subsequent years if necessary

                    Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                    if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                        adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                        LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), companyCode);

                        for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                            LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                            String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                            // post to subsequent acvs of subsequent set of book(propagate)

                            Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                            for (Object accountingCalendarValue : glAccountingCalendarValues) {

                                LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                                if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), branchCode, companyCode);

                                } else { // revenue & expense

                                    this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), branchCode, companyCode);
                                }
                            }

                            if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) break;
                        }
                    }
                }
            }

        } catch (GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyVoidException |
                 GlobalTransactionAlreadyPostedException | GlobalRecordAlreadyDeletedException |
                 GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("CmBankReconciliationControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT / CONVERSION_RATE;
        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer branchCode, Integer companyCode) {

        Debug.print("CmBankReconciliationControllerBean postToGl");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode);

            String ACCOUNT_TYPE = glChartOfAccount.getCoaAccountType();
            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

            if (((ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("EXPENSE")) && isDebit == EJBCommon.TRUE) || (!ACCOUNT_TYPE.equals("ASSET") && !ACCOUNT_TYPE.equals("EXPENSE") && isDebit == EJBCommon.FALSE)) {

                glChartOfAccountBalance.setCoabEndingBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() + JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() + JL_AMNT, FC_EXTNDD_PRCSN));
                }

            } else {

                glChartOfAccountBalance.setCoabEndingBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() - JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() - JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }

            if (isCurrentAcv) {

                if (isDebit == EJBCommon.TRUE) {

                    glChartOfAccountBalance.setCoabTotalDebit(EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalDebit() + JL_AMNT, FC_EXTNDD_PRCSN));

                } else {

                    glChartOfAccountBalance.setCoabTotalCredit(EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalCredit() + JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("CmBankReconciliationControllerBean ejbCreate");
    }
}