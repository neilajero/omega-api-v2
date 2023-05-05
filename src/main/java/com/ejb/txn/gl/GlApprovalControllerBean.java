/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class GlApprovalControllerBean
 * @modified June 22, 2022, 18:26
 * @modified Neil M. Ajero
 */
package com.ejb.txn.gl;

import java.util.*;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.gl.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.gl.*;
import com.ejb.exception.gl.*;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.mod.ad.AdModApprovalQueueDetails;

@Stateless(name = "GlApprovalControllerEJB")
public class GlApprovalControllerBean extends EJBContextClass implements GlApprovalController {

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
    private LocalGlForexLedgerHome glForexLedgerHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalGlJournalHome glJournalHome;


    public ArrayList getAdAqByAqDocumentAndUserName(HashMap criteria, String username, Integer offset, Integer limit, String orderBy, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("GlApprovalControllerBean getAdAqByAqDocumentAndUserName");

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

                LocalGlJournal glJournal = glJournalHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());

                details.setAqJrName(glJournal.getJrName());
                details.setAqDate(glJournal.getJrEffectiveDate());
                details.setAqDocumentNumber(glJournal.getJrDocumentNumber());

                double TOTAL_DEBIT = 0d;

                Collection glJournalLines = glJournal.getGlJournalLines();

                for (Object journalLine : glJournalLines) {

                    LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                    if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {

                        TOTAL_DEBIT += glJournalLine.getJlAmount();
                    }
                }

                details.setAqAmount(TOTAL_DEBIT);

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

    public void executeGlApproval(String approvalQueueDocument, Integer approvalQueueDocumentCode, String username, boolean isApproved, String reasonForRejection, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlJREffectiveDatePeriodClosedException, GlobalTransactionAlreadyPostedException {

        Debug.print("GlApprovalControllerBean executeGlApproval");

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

            LocalGlJournal glJournal = glJournalHome.findByPrimaryKey(approvalQueueDocumentCode);

            if (isApproved) {

                if (adApprovalQueue.getAqAndOr().equals("AND")) {

                    if (adApprovalQueues.size() == 1) {

                        glJournal.setJrApprovalStatus("APPROVED");
                        glJournal.setJrApprovedRejectedBy(username);
                        glJournal.setJrDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                        if (adPreference.getPrfGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                            this.executeGlJrPost(glJournal.getJrCode(), username, branchCode, companyCode);
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

                            glJournal.setJrApprovalStatus("APPROVED");
                            glJournal.setJrApprovedRejectedBy(username);
                            glJournal.setJrDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                            if (adPreference.getPrfGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                                this.executeGlJrPost(glJournal.getJrCode(), username, branchCode, companyCode);
                            }
                        }
                    }

                } else if (adApprovalQueue.getAqAndOr().equals("OR")) {

                    glJournal.setJrApprovalStatus("APPROVED");
                    glJournal.setJrApprovedRejectedBy(username);
                    glJournal.setJrDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                    Iterator i = adApprovalQueues.iterator();

                    while (i.hasNext()) {

                        LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                        i.remove();
                        // adRemoveApprovalQueue.entityRemove();
                        em.remove(adRemoveApprovalQueue);
                    }

                    if (adPreference.getPrfGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                        this.executeGlJrPost(glJournal.getJrCode(), username, branchCode, companyCode);
                    }
                }

            } else if (!isApproved) {

                glJournal.setJrApprovalStatus(null);
                glJournal.setJrReasonForRejection(reasonForRejection);
                glJournal.setJrApprovedRejectedBy(username);
                glJournal.setJrDateApprovedRejected(EJBCommon.getGcCurrentDateWoTime().getTime());

                Iterator i = adApprovalQueues.iterator();

                while (i.hasNext()) {

                    LocalAdApprovalQueue adRemoveApprovalQueue = (LocalAdApprovalQueue) i.next();

                    i.remove();
                    // adRemoveApprovalQueue.entityRemove();
                    em.remove(adRemoveApprovalQueue);
                }
            }

        } catch (GlobalRecordAlreadyDeletedException | GlobalTransactionAlreadyPostedException |
                 GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("GlApprovalControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private double convertForeignToFunctionalCurrency(Integer currencyCode, String currencyName, Date conversionDate, double conversionRate, double amount, Integer companyCode) {

        Debug.print("GlApprovalControllerBean convertForeignToFunctionalCurrency");

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

    private void executeGlJrPost(Integer journalCode, String username, Integer branchCode, Integer companyCode) throws GlobalTransactionAlreadyPostedException, GlJREffectiveDatePeriodClosedException, GlobalRecordAlreadyDeletedException {

        Debug.print("GlApprovalControllerBean executeGlJrPost");

        try {

            // get journal

            LocalGlJournal glJournal = null;

            try {

                glJournal = glJournalHome.findByPrimaryKey(journalCode);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if period is closed

            LocalGlSetOfBook glSetOfBook = glSetOfBookHome.findByDate(glJournal.getJrEffectiveDate(), companyCode);

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glSetOfBook.getGlAccountingCalendar().getAcCode(), glJournal.getJrEffectiveDate(), companyCode);

            if (glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P' || glAccountingCalendarValue.getAcvStatus() == 'N') {

                throw new GlJREffectiveDatePeriodClosedException(glJournal.getJrName());
            }

            // validate if journal is already posted

            if (glJournal.getJrPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException(glJournal.getJrName());
            }

            // post journal

            Collection glJournalLines = glJournal.getGlJournalLines();

            for (Object journalLine : glJournalLines) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                double journalLineAmount = this.convertForeignToFunctionalCurrency(glJournal.getGlFunctionalCurrency().getFcCode(), glJournal.getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), glJournal.getJrConversionRate(), glJournalLine.getJlAmount(), companyCode);

                // post current to current acv

                this.post(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true, glJournalLine.getJlDebit(), journalLineAmount, branchCode, companyCode);

                // post to subsequent acvs (propagate)

                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                    this.post(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), journalLineAmount, branchCode, companyCode);
                }

                // post to subsequent years if necessary

                Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);
                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

                if (!glSubsequentSetOfBooks.isEmpty() && glSetOfBook.getSobYearEndClosed() == 1) {

                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), companyCode);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                        for (Object accountingCalendarValue : glAccountingCalendarValues) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                this.post(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), journalLineAmount, branchCode, companyCode);

                            } else { // revenue & expense

                                this.post(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, glJournalLine.getJlDebit(), journalLineAmount, branchCode, companyCode);
                            }
                        }

                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) break;
                    }
                }

                // for FOREX revaluation
                if ((!Objects.equals(glJournal.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(glJournal.getGlFunctionalCurrency().getFcCode()))) {

                    double conversionRate = 1;

                    if (glJournal.getJrConversionRate() != 0 && glJournal.getJrConversionRate() != 1) {

                        conversionRate = glJournal.getJrConversionRate();

                    } else if (glJournal.getJrConversionDate() != null) {

                        conversionRate = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);
                    }

                    Collection glForexLedgers = null;

                    try {

                        glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(glJournal.getJrEffectiveDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                    } catch (FinderException ex) {

                    }

                    LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                    int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(glJournal.getJrEffectiveDate()) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                    // compute balance
                    double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                    double FRL_AMNT = glJournalLine.getJlAmount();

                    if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                        FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                    else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                    COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                    glForexLedger = glForexLedgerHome.create(glJournal.getJrEffectiveDate(), FRL_LN, "JOURNAL", FRL_AMNT, conversionRate, COA_FRX_BLNC, 0d, companyCode);

                    glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);

                    // propagate balances
                    try {

                        glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                    } catch (FinderException ex) {

                    }

                    for (Object forexLedger : glForexLedgers) {

                        glForexLedger = (LocalGlForexLedger) forexLedger;
                        FRL_AMNT = glJournalLine.getJlAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        else FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);

                        glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);
                    }
                }
            }

            // set journal post status

            glJournal.setJrPosted(EJBCommon.TRUE);
            glJournal.setJrPostedBy(username);
            glJournal.setJrDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

        } catch (GlobalRecordAlreadyDeletedException | GlobalTransactionAlreadyPostedException |
                 GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void post(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double journalLineAmount, Integer branchCode, Integer companyCode) {

        Debug.print("GlJournalBatchSubmitControllerBean post");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode);

            String ACCOUNT_TYPE = glChartOfAccount.getCoaAccountType();
            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

            if (((ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("EXPENSE")) && isDebit == EJBCommon.TRUE) || (!ACCOUNT_TYPE.equals("ASSET") && !ACCOUNT_TYPE.equals("EXPENSE") && isDebit == EJBCommon.FALSE)) {

                glChartOfAccountBalance.setCoabEndingBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() + journalLineAmount, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() + journalLineAmount, FC_EXTNDD_PRCSN));
                }

            } else {

                glChartOfAccountBalance.setCoabEndingBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() - journalLineAmount, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() - journalLineAmount, FC_EXTNDD_PRCSN));
                }
            }

            if (isCurrentAcv) {

                if (isDebit == EJBCommon.TRUE) {

                    glChartOfAccountBalance.setCoabTotalDebit(EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalDebit() + journalLineAmount, FC_EXTNDD_PRCSN));

                } else {

                    glChartOfAccountBalance.setCoabTotalCredit(EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalCredit() + journalLineAmount, FC_EXTNDD_PRCSN));
                }
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double getFrRateByFrNameAndFrDate(String currencyName, Date conversionDate, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("GlApprovalControllerBean getFrRateByFrNameAndFrDate");

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

        Debug.print("GlApprovalControllerBean ejbCreate");
    }
}