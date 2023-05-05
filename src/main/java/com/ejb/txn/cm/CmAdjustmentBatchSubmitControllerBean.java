/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmAdjustmentBatchSubmitControllerBean
 * @created May 19, 2004, 11:15 AM
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
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdAmountLimit;
import com.ejb.dao.ad.LocalAdAmountLimitHome;
import com.ejb.entities.ad.LocalAdApproval;
import com.ejb.dao.ad.LocalAdApprovalCoaLineHome;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.entities.ad.LocalAdApprovalQueue;
import com.ejb.dao.ad.LocalAdApprovalQueueHome;
import com.ejb.entities.ad.LocalAdApprovalUser;
import com.ejb.dao.ad.LocalAdApprovalUserHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdBankAccountBalance;
import com.ejb.dao.ad.LocalAdBankAccountBalanceHome;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.dao.cm.LocalCmAdjustmentHome;
import com.ejb.entities.cm.LocalCmDistributionRecord;
import com.ejb.dao.cm.LocalCmDistributionRecordHome;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.GlobalConversionDateNotExistException;
import com.ejb.exception.global.GlobalJournalNotBalanceException;
import com.ejb.exception.global.GlobalNoApprovalApproverFoundException;
import com.ejb.exception.global.GlobalNoApprovalRequesterFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalTransactionAlreadyApprovedException;
import com.ejb.exception.global.GlobalTransactionAlreadyPendingException;
import com.ejb.exception.global.GlobalTransactionAlreadyPostedException;
import com.ejb.exception.global.GlobalTransactionAlreadyVoidException;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountBalanceHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlChartOfAccountBalance;
import com.ejb.entities.gl.LocalGlForexLedger;
import com.ejb.dao.gl.LocalGlForexLedgerHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
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
import com.util.mod.cm.CmModAdjustmentDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "CmAdjustmentBatchSubmitControllerEJB")
public class CmAdjustmentBatchSubmitControllerBean extends EJBContextClass implements CmAdjustmentBatchSubmitController {

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
    private LocalCmAdjustmentHome cmAdjustmentHome;
    @EJB
    private LocalCmDistributionRecordHome cmDistributionRecordHome;
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

        Debug.print("CmAdjustmentBatchSubmitControllerBean getAdBaAll");

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

        Debug.print("CmAdjustmentBatchSubmitControllerBean getGlFcAll");

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

    public ArrayList getCmAdjByCriteria(HashMap criteria, Integer OFFSET, Integer LIMIT, String ORDER_BY, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("CmAdjustmentBatchSubmitControllerBean getCmAdjByCriteria");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(adj) FROM CmAdjustment adj ");

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

                jbossQl.append("adj.adjReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

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

            if (criteria.containsKey("adjustmentType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("adj.adjType=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("adjustmentType");
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

            if (criteria.containsKey("currency")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("adj.adBankAccount.glFunctionalCurrency.fcName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("currency");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("adj.adjAdBranch=").append(branchCode).append(" ");

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("adj.adjApprovalStatus IS NULL AND adj.adjPosted = 0 AND adj.adjVoid = 0 AND adj.adjAdCompany=").append(companyCode).append(" ");

            String orderBy = null;

            switch (ORDER_BY) {
                case "BANK ACCOUNT":

                    orderBy = "adj.adBankAccount.baName";

                    break;
                case "TYPE":

                    orderBy = "adj.adjType";

                    break;
                case "REFERENCE NUMBER":

                    orderBy = "adj.adjReferenceNumber";
                    break;
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", adj.adjDate");

            } else {

                jbossQl.append("ORDER BY adj.adjDate");
            }

            Collection cmAdjustments = cmAdjustmentHome.getAdjByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (cmAdjustments.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object adjustment : cmAdjustments) {

                LocalCmAdjustment cmAdjustment = (LocalCmAdjustment) adjustment;

                CmModAdjustmentDetails mdetails = new CmModAdjustmentDetails();
                mdetails.setAdjCode(cmAdjustment.getAdjCode());
                mdetails.setAdjType(cmAdjustment.getAdjType());
                mdetails.setAdjDate(cmAdjustment.getAdjDate());
                mdetails.setAdjReferenceNumber(cmAdjustment.getAdjReferenceNumber());
                mdetails.setAdjAmount(cmAdjustment.getAdjAmount());
                mdetails.setAdjBaName(cmAdjustment.getAdBankAccount().getBaName());

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

    public void executeArAdjBatchSubmit(Integer ADJ_CODE, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("CmAdjustmentBatchSubmitControllerBean executeArAdjBatchSubmit");

        try {

            LocalCmAdjustment cmAdjustment = null;

            // validate if adjustment is already deleted

            try {

                cmAdjustment = cmAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate adjustment

            if (cmAdjustment.getAdjApprovalStatus() != null) {

                if (cmAdjustment.getAdjApprovalStatus().equals("APPROVED") || cmAdjustment.getAdjApprovalStatus().equals("N/A")) {

                    throw new GlobalTransactionAlreadyApprovedException();

                } else if (cmAdjustment.getAdjApprovalStatus().equals("PENDING")) {

                    throw new GlobalTransactionAlreadyPendingException();
                }
            }

            if (cmAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();
            }

            // generate approval status

            String ADJ_APPRVL_STATUS = null;

            LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

            // check if cm adjustment approval is enabled

            if (adApproval.getAprEnableCmAdjustment() == EJBCommon.FALSE) {

                ADJ_APPRVL_STATUS = "N/A";

            } else {

                // check if adjustment is self approved

                LocalAdAmountLimit adAmountLimit = null;

                try {

                    adAmountLimit = adAmountLimitHome.findByAdcTypeAndAuTypeAndUsrName("CM ADJUSTMENT", "REQUESTER", cmAdjustment.getAdjLastModifiedBy(), companyCode);

                } catch (FinderException ex) {

                    throw new GlobalNoApprovalRequesterFoundException();
                }

                if (cmAdjustment.getAdjAmount() <= adAmountLimit.getCalAmountLimit()) {

                    ADJ_APPRVL_STATUS = "N/A";

                } else {

                    // for approval, create approval queue

                    Collection adAmountLimits = adAmountLimitHome.findByAdcTypeAndGreaterThanCalAmountLimit("CM ADJUSTMENT", adAmountLimit.getCalAmountLimit(), companyCode);

                    if (adAmountLimits.isEmpty()) {

                        Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                        if (adApprovalUsers.isEmpty()) {

                            throw new GlobalNoApprovalApproverFoundException();
                        }

                        for (Object approvalUser : adApprovalUsers) {

                            LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                            LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode, adApprovalQueueHome, cmAdjustment, adAmountLimit, adApprovalUser);

                            adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                        }

                    } else {

                        boolean isApprovalUsersFound = false;

                        Iterator i = adAmountLimits.iterator();

                        while (i.hasNext()) {

                            LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();

                            if (cmAdjustment.getAdjAmount() <= adNextAmountLimit.getCalAmountLimit()) {

                                Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), companyCode);

                                for (Object approvalUser : adApprovalUsers) {

                                    isApprovalUsersFound = true;

                                    LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                    LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode, adApprovalQueueHome, cmAdjustment, adAmountLimit, adApprovalUser);

                                    adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                }

                                break;

                            } else if (!i.hasNext()) {

                                Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("CM ADJUSTMENT", adNextAmountLimit.getCalCode(), companyCode);

                                for (Object approvalUser : adApprovalUsers) {

                                    isApprovalUsersFound = true;

                                    LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                    LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(branchCode, companyCode, adApprovalQueueHome, cmAdjustment, adNextAmountLimit, adApprovalUser);

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

                    ADJ_APPRVL_STATUS = "PENDING";
                }
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (ADJ_APPRVL_STATUS != null && ADJ_APPRVL_STATUS.equals("N/A") && adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeCmAdjPost(cmAdjustment.getAdjCode(), cmAdjustment.getAdjLastModifiedBy(), branchCode, companyCode);
            }

            // set adjustment approval status

            cmAdjustment.setAdjApprovalStatus(ADJ_APPRVL_STATUS);

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

        Debug.print("CmAdjustmentBatchSubmitControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeCmAdjPost(Integer ADJ_CODE, String USR_NM, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("CmAdjustmentBatchSubmitControllerBean executeCmAdjPost");

        LocalCmAdjustment cmAdjustment = null;

        try {

            // validate if adjustment is already deleted

            try {

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

                if (cmAdjustment.getAdjType().equals("INTEREST") || cmAdjustment.getAdjType().equals("DEBIT MEMO")) {

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

                    // decrease bank account balances

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

            if (adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

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

                    // for FOREX revaluation
                    if ((!Objects.equals(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(cmAdjustment.getAdBankAccount().getGlFunctionalCurrency().getFcCode()))) {

                        double CONVERSION_RATE = 1;

                        if (cmAdjustment.getAdjConversionRate() != 0 && cmAdjustment.getAdjConversionRate() != 1) {

                            CONVERSION_RATE = cmAdjustment.getAdjConversionRate();

                        } else if (cmAdjustment.getAdjConversionDate() != null) {

                            CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);
                        }

                        Collection glForexLedgers = null;

                        try {

                            glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(cmAdjustment.getAdjDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                        } catch (FinderException ex) {

                        }

                        LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                        int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(cmAdjustment.getAdjDate()) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                        // compute balance
                        double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                        double FRL_AMNT = cmDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                        COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                        glForexLedger = glForexLedgerHome.create(cmAdjustment.getAdjDate(), FRL_LN, "OTH", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, 0d, companyCode);

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

        Debug.print("CmAdjustmentBatchSubmitControllerBean convertForeignToFunctionalCurrency");

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

        Debug.print("CmAdjustmentEntryControllerBean postToGl");

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

        Debug.print("CmAdjustmentBatchSubmitControllerBean getFrRateByFrNameAndFrDate");

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

    private LocalAdApprovalQueue createApprovalQueue(Integer branchCode, Integer companyCode, LocalAdApprovalQueueHome adApprovalQueueHome, LocalCmAdjustment cmAdjustment, LocalAdAmountLimit adAmountLimit, LocalAdApprovalUser adApprovalUser) throws CreateException {

        return adApprovalQueueHome.AqForApproval(EJBCommon.TRUE).AqDocument("CM ADJUSTMENT").AqDocumentCode(cmAdjustment.getAdjCode()).AqDocumentNumber(cmAdjustment.getAdjDocumentNumber()).AqDate(cmAdjustment.getAdjDate()).AqAndOr(adAmountLimit.getCalAndOr()).AqUserOr(adApprovalUser.getAuOr()).AqAdBranch(branchCode).AqAdCompany(companyCode).buildApprovalQueue();
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("CmAdjustmentBatchSubmitControllerBean ejbCreate");
    }
}