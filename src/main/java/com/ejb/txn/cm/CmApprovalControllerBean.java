/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class CmApprovalControllerBean
 * @modified June 22, 2022, 18:26
 * @modified Neil M. Ajero
 */
package com.ejb.txn.cm;

import java.util.*;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.cm.*;
import com.ejb.dao.gl.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.cm.*;
import com.ejb.entities.gl.*;
import com.ejb.exception.gl.*;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.mod.ad.AdModApprovalQueueDetails;

@Stateless(name = "CmApprovalControllerEJB")
public class CmApprovalControllerBean extends EJBContextClass implements CmApprovalController {

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
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdBankAccountBalanceHome adBankAccountBalanceHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalCmAdjustmentHome cmAdjustmentHome;
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


    public ArrayList getAdAqByAqDocumentAndUserName(HashMap criteria, String username, Integer offset, Integer limit, String orderBy, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("CmApprovalControllerBean getAdAqByAqDocumentAndUserName");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(aq) FROM AdApprovalQueue aq ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            obj = new Object[criteriaSize];

            if (criteria.containsKey("document")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("aq.aqDocument=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("document");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("aq.aqDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("aq.aqDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("aq.aqDocumentNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("documentNumberTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("aq.aqDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("aq.aqAdBranch=").append(branchCode).append(" AND aq.aqAdCompany=").append(companyCode).append(" ").append("AND aq.adUser.usrName='").append(username).append("'");

            if (orderBy.equals("DOCUMENT NUMBER")) {

                orderBy = "aq.aqDocumentNumber";

            } else if (orderBy.equals("DATE")) {

                orderBy = "aq.aqDate";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", aq.aqDate");

            } else {

                jbossQl.append("ORDER BY aq.aqDate");
            }

            Collection adApprovalQueues = adApprovalQueueHome.getAqByCriteria(jbossQl.toString(), obj, limit, offset);

            String approvalQueueDocument = (String) criteria.get("document");

            if (adApprovalQueues.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object approvalQueue : adApprovalQueues) {

                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                AdModApprovalQueueDetails details = new AdModApprovalQueueDetails();

                details.setAqDocument(adApprovalQueue.getAqDocument());
                details.setAqDocumentCode(adApprovalQueue.getAqDocumentCode());

                if (approvalQueueDocument.equals("CM ADJUSTMENT")) {

                    LocalCmAdjustment cmAdjustment = cmAdjustmentHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());

                    details.setAqDate(cmAdjustment.getAdjDate());
                    details.setAqAmount(cmAdjustment.getAdjAmount());
                    details.setAqReferenceNumber(cmAdjustment.getAdjReferenceNumber());
                    details.setAqDocumentNumber(cmAdjustment.getAdjDocumentNumber());
                    details.setAqDocumentType(cmAdjustment.getAdjType());

                } else {

                    LocalCmFundTransfer cmFundTransfer = cmFundTransferHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());

                    details.setAqDate(cmFundTransfer.getFtDate());
                    details.setAqAmount(cmFundTransfer.getFtAmount());
                    details.setAqReferenceNumber(cmFundTransfer.getFtReferenceNumber());
                    details.setAqDocumentNumber(cmFundTransfer.getFtDocumentNumber());
                }

                list.add(details);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public void executeCmApproval(String approvalQueueDocument, Integer approvalQueueDocumentCode, String username, boolean isApproved, String reasonForRejection, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("CmApprovalControllerBean executeCmApproval");

        LocalAdApprovalQueue adApprovalQueue = null;

        try {

            // validate if approval queue is already deleted

            try {

                adApprovalQueue = adApprovalQueueHome.findByAqDocumentAndAqDocumentCodeAndUsrName(approvalQueueDocument, approvalQueueDocumentCode, username, companyCode);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // approve/reject

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);

            Collection adApprovalQueuesDesc = adApprovalQueueHome.findByAqDocumentAndAqDocumentCodeLessThanDesc(approvalQueueDocument, approvalQueueDocumentCode, adApprovalQueue.getAqCode(), companyCode);
            Collection adApprovalQueuesAsc = adApprovalQueueHome.findByAqDocumentAndAqDocumentCodeGreaterThanAsc(approvalQueueDocument, approvalQueueDocumentCode, adApprovalQueue.getAqCode(), companyCode);

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (approvalQueueDocument.equals("CM ADJUSTMENT")) {

                LocalCmAdjustment cmAdjustment = cmAdjustmentHome.findByPrimaryKey(approvalQueueDocumentCode);

                if (isApproved) {

                    if (adApprovalQueue.getAqAndOr().equals("AND")) {

                        if (adApprovalQueues.size() == 1) {

                            cmAdjustment.setAdjApprovalStatus("APPROVED");
                            cmAdjustment.setAdjApprovedRejectedBy(username);
                            cmAdjustment.setAdjDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                            if (adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                                this.executeCmAdjPost(cmAdjustment.getAdjCode(), username, branchCode, companyCode);
                            }

                            // adApprovalQueue.entityRemove();
                            em.remove(adApprovalQueue);

                        } else {

                            // looping up
                            Iterator i = adApprovalQueuesDesc.iterator();

                            while (i.hasNext()) {

                                LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                                if (adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {

                                    i.remove();
                                    // adRemoveApprovalQueue.entityRemove();
                                    em.remove(adRemoveApprovalQueue);

                                } else {

                                    break;
                                }
                            }

                            // looping down
                            if (adApprovalQueue.getAqUserOr() == (byte) 1) {

                                boolean first = true;

                                i = adApprovalQueuesAsc.iterator();

                                while (i.hasNext()) {

                                    LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                                    if (first || adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {

                                        i.remove();
                                        // adRemoveApprovalQueue.entityRemove();
                                        em.remove(adRemoveApprovalQueue);

                                        if (first) first = false;

                                    } else {

                                        break;
                                    }
                                }
                            }

                            // adApprovalQueue.entityRemove();
                            em.remove(adApprovalQueue);

                            Collection adRemoveApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);

                            if (adRemoveApprovalQueues.size() == 0) {

                                cmAdjustment.setAdjApprovalStatus("APPROVED");
                                cmAdjustment.setAdjApprovedRejectedBy(username);
                                cmAdjustment.setAdjDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                                if (adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                                    this.executeCmAdjPost(cmAdjustment.getAdjCode(), username, branchCode, companyCode);
                                }
                            }
                        }

                    } else if (adApprovalQueue.getAqAndOr().equals("OR")) {

                        cmAdjustment.setAdjApprovalStatus("APPROVED");
                        cmAdjustment.setAdjApprovedRejectedBy(username);
                        cmAdjustment.setAdjDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                        Iterator i = adApprovalQueues.iterator();

                        while (i.hasNext()) {

                            LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                            i.remove();
                            // adRemoveApprovalQueue.entityRemove();
                            em.remove(adRemoveApprovalQueue);
                        }

                        if (adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                            this.executeCmAdjPost(cmAdjustment.getAdjCode(), username, branchCode, companyCode);
                        }
                    }

                } else if (!isApproved) {

                    cmAdjustment.setAdjApprovalStatus(null);
                    cmAdjustment.setAdjReasonForRejection(reasonForRejection);
                    cmAdjustment.setAdjApprovedRejectedBy(username);
                    cmAdjustment.setAdjDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                    Iterator i = adApprovalQueues.iterator();

                    while (i.hasNext()) {

                        LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                        i.remove();
                        // adRemoveApprovalQueue.entityRemove();
                        em.remove(adRemoveApprovalQueue);
                    }
                }

            } else {

                LocalCmFundTransfer cmFundTransfer = cmFundTransferHome.findByPrimaryKey(approvalQueueDocumentCode);

                if (isApproved) {

                    if (adApprovalQueue.getAqAndOr().equals("AND")) {

                        if (adApprovalQueues.size() == 1) {

                            cmFundTransfer.setFtApprovalStatus("APPROVED");
                            cmFundTransfer.setFtApprovedRejectedBy(username);
                            cmFundTransfer.setFtDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                            if (adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                                this.executeCmFtPost(cmFundTransfer.getFtCode(), username, branchCode, companyCode);
                            }

                            // adApprovalQueue.entityRemove();
                            em.remove(adApprovalQueue);

                        } else {

                            // looping up
                            Iterator i = adApprovalQueuesDesc.iterator();

                            while (i.hasNext()) {

                                LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                                if (adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {

                                    i.remove();
                                    // adRemoveApprovalQueue.entityRemove();
                                    em.remove(adRemoveApprovalQueue);

                                } else {

                                    break;
                                }
                            }

                            // looping down
                            if (adApprovalQueue.getAqUserOr() == (byte) 1) {

                                boolean first = true;

                                i = adApprovalQueuesAsc.iterator();

                                while (i.hasNext()) {

                                    LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                                    if (first || adRemoveApprovalQueue.getAqUserOr() == (byte) 1) {

                                        i.remove();
                                        // adRemoveApprovalQueue.entityRemove();
                                        em.remove(adRemoveApprovalQueue);

                                        if (first) first = false;

                                    } else {

                                        break;
                                    }
                                }
                            }

                            // adApprovalQueue.entityRemove();
                            em.remove(adApprovalQueue);

                            Collection adRemoveApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode(approvalQueueDocument, approvalQueueDocumentCode, companyCode);

                            if (adRemoveApprovalQueues.size() == 0) {

                                cmFundTransfer.setFtApprovalStatus("APPROVED");
                                cmFundTransfer.setFtApprovedRejectedBy(username);
                                cmFundTransfer.setFtDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                                if (adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                                    this.executeCmFtPost(cmFundTransfer.getFtCode(), username, branchCode, companyCode);
                                }
                            }
                        }

                    } else if (adApprovalQueue.getAqAndOr().equals("OR")) {

                        cmFundTransfer.setFtApprovalStatus("APPROVED");
                        cmFundTransfer.setFtApprovedRejectedBy(username);
                        cmFundTransfer.setFtDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                        Iterator i = adApprovalQueues.iterator();

                        while (i.hasNext()) {

                            LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                            i.remove();
                            // adRemoveApprovalQueue.entityRemove();
                            em.remove(adRemoveApprovalQueue);
                        }

                        if (adPreference.getPrfCmGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                            this.executeCmFtPost(cmFundTransfer.getFtCode(), username, branchCode, companyCode);
                        }
                    }

                } else if (!isApproved) {

                    cmFundTransfer.setFtApprovalStatus(null);
                    cmFundTransfer.setFtApprovedRejectedBy(username);
                    cmFundTransfer.setFtReasonForRejection(reasonForRejection);
                    cmFundTransfer.setFtDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                    Iterator i = adApprovalQueues.iterator();

                    while (i.hasNext()) {

                        LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                        i.remove();
                        // adRemoveApprovalQueue.entityRemove();
                        em.remove(adRemoveApprovalQueue);
                    }
                }
            }

        } catch (GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException | GlJREffectiveDateNoPeriodExistException |
                 GlobalTransactionAlreadyVoidPostedException | GlobalTransactionAlreadyPostedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("CmApprovalControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private void executeCmAdjPost(Integer adjustmentCode, String username, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("CmApprovalControllerBean executeCmAdjPost");

        LocalCmAdjustment cmAdjustment = null;

        try {

            // validate if adjustment is already deleted

            try {

                cmAdjustment = cmAdjustmentHome.findByPrimaryKey(adjustmentCode);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if receipt is already posted

            if (cmAdjustment.getAdjVoid() == EJBCommon.FALSE && cmAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

                // validate if receipt void is already posted

            } else if (cmAdjustment.getAdjVoid() == EJBCommon.TRUE && cmAdjustment.getAdjVoidPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidPostedException();
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
                cmAdjustment.setAdjPosted(EJBCommon.TRUE);
                cmAdjustment.setAdjPostedBy(username);
                cmAdjustment.setAdjDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            } else if (cmAdjustment.getAdjVoid() == EJBCommon.TRUE && cmAdjustment.getAdjVoidPosted() == EJBCommon.FALSE) { // void receipt

                if (cmAdjustment.getAdjType().equals("INTEREST") || cmAdjustment.getAdjType().equals("DEBIT MEMO") || cmAdjustment.getAdjType().equals("ADVANCE")) {

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

                } else {

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
                }

                // set cmAdjustment post status

                cmAdjustment.setAdjVoidPosted(EJBCommon.TRUE);
                cmAdjustment.setAdjPostedBy(username);
                cmAdjustment.setAdjDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
            }

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

                    glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " BANK ADJUSTMENTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), username, branchCode, companyCode);
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(cmAdjustment.getAdjReferenceNumber(), cmAdjustment.getAdjMemo(), cmAdjustment.getAdjDate(), 0.0d, null, cmAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, username, new Date(), username, new Date(), null, null, username, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode, companyCode);

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

                        double conversionRate = 1;

                        if (cmAdjustment.getAdjConversionRate() != 0 && cmAdjustment.getAdjConversionRate() != 1) {

                            conversionRate = cmAdjustment.getAdjConversionRate();

                        } else if (cmAdjustment.getAdjConversionDate() != null) {

                            conversionRate = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);
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

                        glForexLedger = glForexLedgerHome.create(cmAdjustment.getAdjDate(), FRL_LN, "OTH", FRL_AMNT, conversionRate, COA_FRX_BLNC, 0d, companyCode);

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

        } catch (GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyVoidPostedException |
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

    private void executeCmFtPost(Integer fundTransferCode, String username, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException {

        Debug.print("CmApprovalControllerBean executeCmFtPost");

        LocalCmFundTransfer cmFundTransfer = null;

        try {

            // validate if fund transfer is already deleted

            try {

                cmFundTransfer = cmFundTransferHome.findByPrimaryKey(fundTransferCode);

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

                    if (!adBankAccountBalances.isEmpty()) {

                        // get last check

                        ArrayList adBankAccountBalanceList = new ArrayList(adBankAccountBalances);

                        LocalAdBankAccountBalance adBankAccountBalance = (LocalAdBankAccountBalance) adBankAccountBalanceList.get(adBankAccountBalanceList.size() - 1);

                        if (adBankAccountBalance.getBabDate().before(cmFundTransfer.getFtDate())) {

                            // create new balance

                            LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmFundTransfer.getFtDate(), adBankAccountBalance.getBabBalance() + cmFundTransfer.getFtAmount(), "BOOK", companyCode);

                            adBankAccountTo.addAdBankAccountBalance(adNewBankAccountBalance);

                        } else { // equals to check date

                            adBankAccountBalance.setBabBalance(adBankAccountBalance.getBabBalance() + cmFundTransfer.getFtAmount());
                        }

                    } else {

                        // create new balance

                        LocalAdBankAccountBalance adNewBankAccountBalance = adBankAccountBalanceHome.create(cmFundTransfer.getFtDate(), (cmFundTransfer.getFtAmount()), "BOOK", companyCode);

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
            cmFundTransfer.setFtPostedBy(username);
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

                    glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " FUND TRANSFERS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), username, branchCode, companyCode);
                }

                // create journal entry

                LocalGlJournal glJournal = glJournalHome.create(cmFundTransfer.getFtReferenceNumber(), cmFundTransfer.getFtMemo(), cmFundTransfer.getFtDate(), 0.0d, null, cmFundTransfer.getFtDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, username, new Date(), username, new Date(), null, null, username, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, branchCode, companyCode);

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

                        double conversionRate = 1;

                        if (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(adBankAccount.getGlFunctionalCurrency().getFcCode()) && cmFundTransfer.getFtConversionRateFrom() != 0 && cmFundTransfer.getFtConversionRateFrom() != 1) {

                            conversionRate = cmFundTransfer.getFtConversionRateFrom();

                        } else if (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(adBankAccountTo.getGlFunctionalCurrency().getFcCode()) && cmFundTransfer.getFtConversionRateTo() != 0 && cmFundTransfer.getFtConversionRateTo() != 1) {

                            conversionRate = cmFundTransfer.getFtConversionRateTo();

                        } else if (cmFundTransfer.getFtConversionDate() != null) {

                            conversionRate = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);
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

                        glForexLedger = glForexLedgerHome.create(cmFundTransfer.getFtDate(), FRL_LN, "OTH", FRL_AMNT, conversionRate, COA_FRX_BLNC, 0d, companyCode);

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

    private double convertForeignToFunctionalCurrency(Integer currencyCode, String currencyName, Date conversionDate, double conversionRate, double amount, Integer companyCode) {

        Debug.print("CmApprovalControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (conversionRate != 1 && conversionRate != 0) {

            amount = amount / conversionRate;
        }
        return EJBCommon.roundIt(amount, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer branchCode, Integer companyCode) {

        Debug.print("CmApprovalControllerBean postToGl");

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

    private double getFrRateByFrNameAndFrDate(String currencyName, Date conversionDate, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("CmApprovalControllerBean getFrRateByFrNameAndFrDate");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(currencyName, companyCode);

            double conversionRate = 1;

            // Get functional currency rate

            if (!currencyName.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), conversionDate, companyCode);

                conversionRate = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), conversionDate, companyCode);

                conversionRate = conversionRate / glCompanyFunctionalCurrencyRate.getFrXToUsd();
            }

            return conversionRate;

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalConversionDateNotExistException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("CmApprovalControllerBean ejbCreate");
    }
}