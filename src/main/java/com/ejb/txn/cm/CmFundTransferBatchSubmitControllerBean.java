/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmFundTransferBatchSubmitControllerBean
 * @created May 19, 2004, 1:37 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.cm;

import java.util.*;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.cm.*;
import com.ejb.dao.gl.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.cm.*;
import com.ejb.entities.gl.*;
import com.ejb.exception.gl.*;
import com.ejb.exception.global.*;
import com.util.mod.cm.CmModFundTransferEntryDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "CmFundTransferBatchSubmitControllerEJB")
public class CmFundTransferBatchSubmitControllerBean extends EJBContextClass implements CmFundTransferBatchSubmitController {

    @EJB
    public PersistenceBeanClass em;
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
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalCoaLineHome adApprovalCoaLineHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalCmDistributionRecordHome cmDistributionRecordHome;
    @EJB
    private LocalCmFundTransferHome cmFundTransferHome;
    @EJB
    private LocalGlForexLedgerHome glForexLedgerHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;


    public ArrayList getAdBaAll(Integer branchCode, Integer companyCode) {

        Debug.print("CmFundTransferBatchSubmitControllerBean getAdBaAll");

        ArrayList list = new ArrayList();

        try {

            Collection adBankAccounts = adBankAccountHome.findEnabledBaAll(branchCode, companyCode);

            for (Object bankAccount : adBankAccounts) {

                LocalAdBankAccount adBankAccount = (LocalAdBankAccount) bankAccount;

                list.add(adBankAccount.getBaName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlFcAll(Integer companyCode) {

        Debug.print("CmFundTransferBatchSubmitControllerBean getGlFcAll");

        ArrayList list = new ArrayList();

        try {

            Collection glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAll(companyCode);

            for (Object functionalCurrency : glFunctionalCurrencies) {

                LocalGlFunctionalCurrency glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;

                list.add(glFunctionalCurrency.getFcName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getCmFtByCriteria(HashMap criteria, Integer OFFSET, Integer LIMIT, String ORDER_BY, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("CmFundTransferBatchSubmitControllerBean getCmFtByCriteria");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(ft) FROM CmFundTransfer ft ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("referenceNumber")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("referenceNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ft.ftReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            if (criteria.containsKey("bankAccountFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                LocalAdBankAccount adBankAccountFrom = adBankAccountHome.findByBaName((String) criteria.get("bankAccountFrom"), companyCode);

                jbossQl.append("ft.ftAdBaAccountFrom=?").append(ctr + 1).append(" ");
                obj[ctr] = adBankAccountFrom.getBaCode();
                ctr++;
            }

            if (criteria.containsKey("bankAccountTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                LocalAdBankAccount adBankAccountTo = adBankAccountHome.findByBaName((String) criteria.get("bankAccountTo"), companyCode);

                jbossQl.append("ft.ftAdBaAccountTo=?").append(ctr + 1).append(" ");
                obj[ctr] = adBankAccountTo.getBaCode();
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

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("ft.ftAdBranch=").append(branchCode).append(" ");

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("ft.ftApprovalStatus IS NULL AND ft.ftPosted = 0 AND ft.ftVoid = 0 AND ft.ftAdCompany=").append(companyCode).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("BANK ACCOUNT")) {

                orderBy = "ft.ftAdBaAccountFrom";

            } else if (ORDER_BY.equals("REFERENCE NUMBER")) {

                orderBy = "ft.ftReferenceNumber";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", ft.ftDate");

            } else {

                jbossQl.append("ORDER BY ft.ftDate");
            }

            Collection cmFundTransfers = cmFundTransferHome.getFtByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (cmFundTransfers.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object fundTransfer : cmFundTransfers) {

                LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) fundTransfer;

                LocalAdBankAccount adBankAccountFrom = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountFrom());

                LocalAdBankAccount adBankAccountTo = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountTo());

                CmModFundTransferEntryDetails mdetails = new CmModFundTransferEntryDetails();
                mdetails.setFtCode(cmFundTransfer.getFtCode());
                mdetails.setFtAmount(cmFundTransfer.getFtAmount());
                mdetails.setFtDate(cmFundTransfer.getFtDate());
                mdetails.setFtReferenceNumber(cmFundTransfer.getFtReferenceNumber());
                mdetails.setFtAdBankAccountNameFrom(adBankAccountFrom.getBaName());
                mdetails.setFtAdBankAccountNameTo(adBankAccountTo.getBaName());
                mdetails.setFtCurrencyFrom(adBankAccountFrom.getGlFunctionalCurrency().getFcName());
                mdetails.setFtCurrencyTo(adBankAccountTo.getGlFunctionalCurrency().getFcName());

                list.add(mdetails);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public void executeArFtBatchSubmit(Integer FT_CODE, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("CmFundTransferBatchSubmitControllerBean executeArFtBatchSubmit");

        try {

            LocalCmFundTransfer cmFundTransfer = null;

            // validate if fund transfer is already deleted

            try {

                cmFundTransfer = cmFundTransferHome.findByPrimaryKey(FT_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate fund transfer

            if (cmFundTransfer.getFtApprovalStatus() != null) {

                if (cmFundTransfer.getFtApprovalStatus().equals("APPROVED") || cmFundTransfer.getFtApprovalStatus().equals("N/A")) {

                    throw new GlobalTransactionAlreadyApprovedException();

                } else if (cmFundTransfer.getFtApprovalStatus().equals("PENDING")) {

                    throw new GlobalTransactionAlreadyPendingException();
                }
            }

            if (cmFundTransfer.getFtPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();
            }

            // generate approval status

            String FT_APPRVL_STATUS = null;

            LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

            // check if cm fund transfer approval is enabled

            if (adApproval.getAprEnableCmFundTransfer() == EJBCommon.FALSE) {

                FT_APPRVL_STATUS = "N/A";

            } else {

                // check if fund transfer is self approved

                LocalAdAmountLimit adAmountLimit = null;

                try {

                    adAmountLimit = adAmountLimitHome.findByAdcTypeAndAuTypeAndUsrName("CM FUND TRANSFER", "REQUESTER", cmFundTransfer.getFtLastModifiedBy(), companyCode);

                } catch (FinderException ex) {

                    throw new GlobalNoApprovalRequesterFoundException();
                }

                if (cmFundTransfer.getFtAmount() <= adAmountLimit.getCalAmountLimit()) {

                    FT_APPRVL_STATUS = "N/A";

                } else {

                    // for approval, create approval queue

                    Collection adAmountLimits = adAmountLimitHome.findByAdcTypeAndGreaterThanCalAmountLimit("CM FUND TRANSFER", adAmountLimit.getCalAmountLimit(), companyCode);

                    if (adAmountLimits.isEmpty()) {

                        Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                        if (adApprovalUsers.isEmpty()) {

                            throw new GlobalNoApprovalApproverFoundException();
                        }

                        for (Object approvalUser : adApprovalUsers) {

                            LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                            LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode, adApprovalQueueHome, cmFundTransfer, adAmountLimit, adApprovalUser);

                            adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                        }

                    } else {

                        boolean isApprovalUsersFound = false;

                        Iterator i = adAmountLimits.iterator();

                        while (i.hasNext()) {

                            LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();

                            if (cmFundTransfer.getFtAmount() <= adNextAmountLimit.getCalAmountLimit()) {

                                Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                                for (Object approvalUser : adApprovalUsers) {

                                    isApprovalUsersFound = true;

                                    LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                    LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode, adApprovalQueueHome, cmFundTransfer, adAmountLimit, adApprovalUser);

                                    adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                }

                                break;

                            } else if (!i.hasNext()) {

                                Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("CM FUND TRANSFER", adNextAmountLimit.getCalCode(), companyCode);

                                for (Object approvalUser : adApprovalUsers) {

                                    isApprovalUsersFound = true;

                                    LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                    LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode, adApprovalQueueHome, cmFundTransfer, adNextAmountLimit, adApprovalUser);

                                    adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                }

                                break;
                            }

                            adAmountLimit = adNextAmountLimit;
                        }

                        if (!isApprovalUsersFound) {

                            throw new GlobalNoApprovalApproverFoundException();
                        }
                    }

                    FT_APPRVL_STATUS = "PENDING";
                }
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (FT_APPRVL_STATUS != null && FT_APPRVL_STATUS.equals("N/A") && adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeCmFtPost(cmFundTransfer.getFtCode(), cmFundTransfer.getFtLastModifiedBy(), branchCode, companyCode);
            }

            // set fund transfer approval status

            cmFundTransfer.setFtApprovalStatus(FT_APPRVL_STATUS);

        } catch (GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException | GlJREffectiveDateNoPeriodExistException |
                 GlobalTransactionAlreadyVoidException | GlobalNoApprovalApproverFoundException |
                 GlobalNoApprovalRequesterFoundException | GlobalTransactionAlreadyPostedException |
                 GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("CmFundTransferBatchSubmitControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeCmFtPost(Integer FT_CODE, String USR_NM, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("CmFundTransferPostControllerBean executeCmFtPost");

        LocalCmFundTransfer cmFundTransfer = null;

        try {

            // validate if fund transfer is already deleted

            try {

                cmFundTransfer = cmFundTransferHome.findByPrimaryKey(FT_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if fund transfer is already posted or void

            if (cmFundTransfer.getFtPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

            } else if (cmFundTransfer.getFtVoid() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidException();
            }

            // post fund transfer

            if (cmFundTransfer.getFtVoid() == EJBCommon.FALSE && cmFundTransfer.getFtPosted() == EJBCommon.FALSE) {

                // update bank account from balance (decrease)

                LocalAdBankAccount adBankAccountFrom = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountFrom());

                try {

                    // find bankaccount balance before or equal receipt date

                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(cmFundTransfer.getFtDate(), cmFundTransfer.getFtAdBaAccountFrom(), "BOOK", companyCode);

                    if (!adBankAccountBalances.isEmpty()) {

                        // get last check

                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(cmFundTransfer.getFtDate())) {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmFundTransfer.getFtDate(), adBankAccountBalance.getBabBalance() - cmFundTransfer.getFtAmount(), "BOOK", companyCode);

                            adBankAccountFrom.addAdBankAccountBalance(adNewBankAccountBalance);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - cmFundTransfer.getFtAmount());
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmFundTransfer.getFtDate(), (0 - cmFundTransfer.getFtAmount()), "BOOK", companyCode);

                        adBankAccountFrom.addAdBankAccountBalance(adNewBankAccountBalance);
                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(cmFundTransfer.getFtDate(), cmFundTransfer.getFtAdBaAccountFrom(), "BOOK", companyCode);

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() - cmFundTransfer.getFtAmount());
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

                // update bank account to balance (increase)

                LocalAdBankAccount adBankAccountTo = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountTo());

                try {

                    // find bankaccount balance before or equal receipt date

                    Collection adBankAccountBalances = adBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(cmFundTransfer.getFtDate(), cmFundTransfer.getFtAdBaAccountTo(), "BOOK", companyCode);

                    // get convert amount to bank to currency
                    double CNVRSN_RT_FRM_TO = cmFundTransfer.getFtConversionRateFrom() / cmFundTransfer.getFtConversionRateTo();
                    double CNVRTD_AMNT = EJBCommon.roundIt(cmFundTransfer.getFtAmount() * CNVRSN_RT_FRM_TO, getGlFcPrecisionUnit(companyCode));

                    if (!adBankAccountBalances.isEmpty()) {

                        // get last check

                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(cmFundTransfer.getFtDate())) {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmFundTransfer.getFtDate(), adBankAccountBalance.getBabBalance() + CNVRTD_AMNT, "BOOK", companyCode);

                            adBankAccountTo.addAdBankAccountBalance(adNewBankAccountBalance);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + CNVRTD_AMNT);
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmFundTransfer.getFtDate(), (CNVRTD_AMNT), "BOOK", companyCode);

                        adBankAccountTo.addAdBankAccountBalance(adNewBankAccountBalance);
                    }

                    // propagate to subsequent balances if necessary

                    adBankAccountBalances = adBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(cmFundTransfer.getFtDate(), cmFundTransfer.getFtAdBaAccountTo(), "BOOK", companyCode);

                    for (Object bankAccountBalance : adBankAccountBalances) {

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) bankAccountBalance;

                        adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + cmFundTransfer.getFtAmount());
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }
            }

            // set post status

            cmFundTransfer.setFtPosted(EJBCommon.TRUE);
            cmFundTransfer.setFtPostedBy(USR_NM);
            cmFundTransfer.setFtDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            if (adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                // validate if date has no period and period is closed

                LocalGlSetOfBook glJournalSetOfBook = null;

                try {

                    glJournalSetOfBook = glSetOfBookHome.findByDate(cmFundTransfer.getFtDate(), companyCode);

                } catch (FinderException ex) {

                    throw new GlJREffectiveDateNoPeriodExistException();
                }

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), cmFundTransfer.getFtDate(), companyCode);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException();
                }

                // check if invoice is balance if not check suspense posting

                LocalGlJournalLine glOffsetJournalLine = null;

                Collection cmDistributionRecords = cmDistributionRecordHome.findByDrReversalAndDrImportedAndFtCode(EJBCommon.FALSE, EJBCommon.FALSE, cmFundTransfer.getFtCode(), companyCode);

                Iterator j = cmDistributionRecords.iterator();

                double TOTAL_DEBIT = 0d;
                double TOTAL_CREDIT = 0d;

                while (j.hasNext()) {

                    LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) j.next();

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountFrom());

                    double DR_AMNT = this.convertForeignToFunctionalCurrency(adBankAccount.getGlFunctionalCurrency().getFcCode(), adBankAccount.getGlFunctionalCurrency().getFcName(), cmFundTransfer.getFtConversionDate(), cmFundTransfer.getFtConversionRateFrom(), cmDistributionRecord.getDrAmount(), companyCode);

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

                        glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("CASH MANAGEMENT", "FUND TRANSFERS", companyCode);

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

                    glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " FUND TRANSFERS", branchCode, companyCode);

                } catch (FinderException ex) {
                }

                if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                    glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " FUND TRANSFERS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, branchCode, companyCode);
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(cmFundTransfer.getFtReferenceNumber(), cmFundTransfer.getFtMemo(), cmFundTransfer.getFtDate(), 0.0d, null, cmFundTransfer.getFtDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode, companyCode);

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("CASH MANAGEMENT", companyCode);
                glJournal.setGlJournalSource(glJournalSource);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("FUND TRANSFERS", companyCode);
                glJournal.setGlJournalCategory(glJournalCategory);

                if (glJournalBatch != null) {

                    glJournal.setGlJournalBatch(glJournalBatch);
                }

                // create journal lines

                j = cmDistributionRecords.iterator();

                while (j.hasNext()) {

                    LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) j.next();

                    LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountFrom());

                    double DR_AMNT = this.convertForeignToFunctionalCurrency(adBankAccount.getGlFunctionalCurrency().getFcCode(), adBankAccount.getGlFunctionalCurrency().getFcName(), cmFundTransfer.getFtConversionDate(), cmFundTransfer.getFtConversionRateFrom(), cmDistributionRecord.getDrAmount(), companyCode);

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(cmDistributionRecord.getDrLine(), cmDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                    cmDistributionRecord.getGlChartOfAccount().addGlJournalLine(glJournalLine);

                    glJournal.addGlJournalLine(glJournalLine);

                    cmDistributionRecord.setDrImported(EJBCommon.TRUE);

                    LocalAdBankAccount adBankAccountTo = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountTo());

                    // for FOREX revaluation
                    if (((!Objects.equals(adBankAccount.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(adBankAccount.getGlFunctionalCurrency().getFcCode()))) || ((!Objects.equals(adBankAccountTo.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(adBankAccountTo.getGlFunctionalCurrency().getFcCode())))) {

                        double CONVERSION_RATE = 1;

                        if (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(adBankAccount.getGlFunctionalCurrency().getFcCode()) && cmFundTransfer.getFtConversionRateFrom() != 0 && cmFundTransfer.getFtConversionRateFrom() != 1) {

                            CONVERSION_RATE = cmFundTransfer.getFtConversionRateFrom();

                        } else if (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(adBankAccountTo.getGlFunctionalCurrency().getFcCode()) && cmFundTransfer.getFtConversionRateTo() != 0 && cmFundTransfer.getFtConversionRateTo() != 1) {

                            CONVERSION_RATE = cmFundTransfer.getFtConversionRateTo();

                        } else if (cmFundTransfer.getFtConversionDate() != null) {

                            CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);
                        }

                        Collection glForexLedgers = null;

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(cmFundTransfer.getFtDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        } catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(cmFundTransfer.getFtDate()) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = cmDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        glForexLedger = glForexLedgerHome.create(cmFundTransfer.getFtDate(), FRL_LN, "OTH", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, 0d, companyCode);

                        glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);

                        // propagate balances
                        try {

                            glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                        } catch (FinderException ex) {

                        }

                        for (Object forexLedger : glForexLedgers) {

                            glForexLedger = (LocalGlForexLedger) forexLedger;
                            FRL_AMNT = cmDistributionRecord.getDrAmount();

                            if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                                FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                            else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                            glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);
                        }
                    }
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

        Debug.print("CmFundTransferPostControllerBean convertForeignToFunctionalCurrency");

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

        Debug.print("CmFundTransferPostControllerBean postToGl");

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

    private double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("CmFundTransferBatchSubmitControllerBean getFrRateByFrNameAndFrDate");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);

            double CONVERSION_RATE = 1;

            // Get functional currency rate

            if (!FC_NM.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), CONVERSION_DATE, companyCode);

                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, companyCode);

                CONVERSION_RATE = CONVERSION_RATE / glCompanyFunctionalCurrencyRate.getFrXToUsd();
            }

            return CONVERSION_RATE;

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalConversionDateNotExistException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalAdApprovalQueue createApprovalQueue(Integer branchCode, Integer companyCode, LocalAdApprovalQueueHome adApprovalQueueHome, LocalCmFundTransfer cmFundTransfer, LocalAdAmountLimit adAmountLimit, LocalAdApprovalUser adApprovalUser) throws CreateException {

        return adApprovalQueueHome.AqForApproval(EJBCommon.TRUE).AqDocument("CM FUND TRANSFER").AqDocumentCode(cmFundTransfer.getFtCode()).AqDocumentNumber(cmFundTransfer.getFtDocumentNumber()).AqDate(cmFundTransfer.getFtDate()).AqAndOr(adAmountLimit.getCalAndOr()).AqUserOr(adApprovalUser.getAuOr()).AqAdBranch(branchCode).AqAdCompany(companyCode).buildApprovalQueue();
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("CmFundTransferBatchSubmitControllerBean ejbCreate");
    }
}