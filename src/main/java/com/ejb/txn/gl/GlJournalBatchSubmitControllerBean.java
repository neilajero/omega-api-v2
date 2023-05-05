/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlJournalBatchSubmitControllerBean
 * @created
 * @author
 */
package com.ejb.txn.gl;

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
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.GlobalConversionDateNotExistException;
import com.ejb.exception.global.GlobalNoApprovalApproverFoundException;
import com.ejb.exception.global.GlobalNoApprovalRequesterFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalTransactionAlreadyApprovedException;
import com.ejb.exception.global.GlobalTransactionAlreadyPendingException;
import com.ejb.exception.global.GlobalTransactionAlreadyPostedException;
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
import com.ejb.entities.gl.LocalGlJournalSource;
import com.ejb.dao.gl.LocalGlJournalSourceHome;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;
import com.util.mod.gl.GlModJournalDetails;

@Stateless(name = "GlJournalBatchSubmitControllerEJB")
public class GlJournalBatchSubmitControllerBean extends EJBContextClass implements GlJournalBatchSubmitController {

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
    private LocalGlJournalSourceHome glJournalSourceHome;

    public ArrayList getGlJcAll(Integer AD_CMPNY) {

        Debug.print("GlJournalBatchSubmitControllerBean getGlJcAll");

        ArrayList list = new ArrayList();

        try {

            Collection glJournalCategories = glJournalCategoryHome.findJcAll(AD_CMPNY);

            for (Object journalCategory : glJournalCategories) {

                LocalGlJournalCategory glJournalCategory = (LocalGlJournalCategory) journalCategory;

                list.add(glJournalCategory.getJcName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlJsAll(Integer AD_CMPNY) {

        Debug.print("GlJournalBatchSubmitControllerBean getGlJsAll");

        ArrayList list = new ArrayList();

        try {

            Collection glJournalSources = glJournalSourceHome.findJsAll(AD_CMPNY);

            for (Object journalSource : glJournalSources) {

                LocalGlJournalSource glJournalSource = (LocalGlJournalSource) journalSource;

                list.add(glJournalSource.getJsName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlFcAllWithDefault(Integer AD_CMPNY) {

        Debug.print("GlJournalBatchSubmitControllerBean getGlFcAllWithDefault");

        Collection glFunctionalCurrencies = null;

        LocalGlFunctionalCurrency glFunctionalCurrency = null;
        LocalAdCompany adCompany = null;

        ArrayList list = new ArrayList();

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAllEnabled(EJBCommon.getGcCurrentDateWoTime().getTime(), AD_CMPNY);

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

    public ArrayList getGlOpenJbAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("GlJournalBatchSubmitControllerBean getGlOpenJbAll");

        ArrayList list = new ArrayList();

        try {

            Collection glJournalBatches = glJournalBatchHome.findOpenJbAll(AD_BRNCH, AD_CMPNY);

            for (Object journalBatch : glJournalBatches) {

                LocalGlJournalBatch glJournalBatch = (LocalGlJournalBatch) journalBatch;

                list.add(glJournalBatch.getJbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableGlJournalBatch(Integer AD_CMPNY) {

        Debug.print("GlJournalBatchSubmitControllerBean getAdPrfEnableGlJournalBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfEnableGlJournalBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlJrByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlJournalBatchSubmitControllerBean getGlJrByCriteria");

        ArrayList jrList = new ArrayList();

        StringBuilder jbossQl = new StringBuilder();
        jbossQl.append("SELECT OBJECT(jr) FROM GlJournal jr ");

        boolean firstArgument = true;
        short ctr = 0;
        int criteriaSize = criteria.size();
        Object[] obj;

        // Allocate the size of the object parameter

        if (criteria.containsKey("journalName")) {

            criteriaSize--;
        }

        obj = new Object[criteriaSize];

        if (criteria.containsKey("journalName")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("jr.jrName LIKE '%").append(criteria.get("journalName")).append("%' ");
        }

        if (criteria.containsKey("batchName")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("jr.glJournalBatch.jbName=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("batchName");
            ctr++;
        }

        if (criteria.containsKey("dateFrom")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("jr.jrEffectiveDate>=?").append(ctr + 1).append(" ");
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
            jbossQl.append("jr.jrEffectiveDate<=?").append(ctr + 1).append(" ");
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
            jbossQl.append("jr.jrDocumentNumber>=?").append(ctr + 1).append(" ");
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
            jbossQl.append("jr.jrDocumentNumber<=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("documentNumberTo");
            ctr++;
        }

        if (criteria.containsKey("category")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("jr.glJournalCategory.jcName=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("category");
            ctr++;
        }

        if (criteria.containsKey("source")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("jr.glJournalSource.jsName=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("source");
            ctr++;
        }

        if (criteria.containsKey("currency")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("jr.glFunctionalCurrency.fcName=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("currency");
            ctr++;
        }

        if (!firstArgument) {

            jbossQl.append("AND ");

        } else {

            firstArgument = false;
            jbossQl.append("WHERE ");
        }

        jbossQl.append("jr.jrApprovalStatus IS NULL AND jr.jrPosted=0 AND jr.jrAdBranch=").append(AD_BRNCH).append(" AND jr.jrAdCompany=").append(AD_CMPNY).append(" ");

        String orderBy = null;

        switch (ORDER_BY) {
            case "REFERENCE NUMBER":

                orderBy = "jr.jrName";

                break;
            case "DOCUMENT NUMBER":

                orderBy = "jr.jrDocumentNumber";

                break;
            case "CATEGORY":

                orderBy = "jr.glJournalCategory.jcName";

                break;
            case "SOURCE":

                orderBy = "jr.glJournalSource.jsName";
                break;
        }

        if (orderBy != null) {

            jbossQl.append("ORDER BY ").append(orderBy).append(", jr.jrEffectiveDate");

        } else {

            jbossQl.append("ORDER BY jr.jrEffectiveDate");
        }

        Debug.print("QL + " + jbossQl);

        Collection glJournals = null;

        double TOTAL_DEBIT = 0;
        double TOTAL_CREDIT = 0;

        try {

            glJournals = glJournalHome.getJrByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glJournals.isEmpty()) throw new GlobalNoRecordFoundException();

        for (Object journal : glJournals) {

            TOTAL_DEBIT = 0d;
            TOTAL_CREDIT = 0d;

            LocalGlJournal glJournal = (LocalGlJournal) journal;
            Collection glJournalLines = glJournal.getGlJournalLines();

            for (Object journalLine : glJournalLines) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {

                    TOTAL_DEBIT += this.convertForeignToFunctionalCurrency(glJournal.getGlFunctionalCurrency().getFcCode(), glJournal.getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), glJournal.getJrConversionRate(), glJournalLine.getJlAmount(), AD_CMPNY);

                } else {

                    TOTAL_CREDIT += this.convertForeignToFunctionalCurrency(glJournal.getGlFunctionalCurrency().getFcCode(), glJournal.getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), glJournal.getJrConversionRate(), glJournalLine.getJlAmount(), AD_CMPNY);
                }
            }

            GlModJournalDetails mdetails = new GlModJournalDetails();
            mdetails.setJrCode(glJournal.getJrCode());
            mdetails.setJrName(glJournal.getJrName());
            mdetails.setJrDescription(glJournal.getJrDescription());
            mdetails.setJrEffectiveDate(glJournal.getJrEffectiveDate());
            mdetails.setJrDocumentNumber(glJournal.getJrDocumentNumber());
            mdetails.setJrTotalDebit(TOTAL_DEBIT);
            mdetails.setJrTotalCredit(TOTAL_CREDIT);
            mdetails.setJrJcName(glJournal.getGlJournalCategory().getJcName());
            mdetails.setJrJsName(glJournal.getGlJournalSource().getJsName());
            mdetails.setJrFcName(glJournal.getGlFunctionalCurrency().getFcName());

            // get acv status

            try {

                LocalGlSetOfBook glJournalSetOfBook = glSetOfBookHome.findByDate(glJournal.getJrEffectiveDate(), AD_CMPNY);

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glJournal.getJrEffectiveDate(), AD_CMPNY);

                mdetails.setJrAcvStatus(glAccountingCalendarValue.getAcvStatus());

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            jrList.add(mdetails);
        }

        return jrList;
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GlJournalBatchSubmitControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void executeGlJrBatchSubmit(Integer JR_CODE, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDatePeriodClosedException {

        Debug.print("GlJournalBatchSubmitControllerBean executeGlJrBatchSubmit");

        try {

            LocalGlJournal glJournal = null;

            try {

                glJournal = glJournalHome.findByPrimaryKey(JR_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate journal

            if (glJournal.getJrApprovalStatus() != null) {

                if (glJournal.getJrApprovalStatus().equals("APPROVED") || glJournal.getJrApprovalStatus().equals("N/A")) {

                    throw new GlobalTransactionAlreadyApprovedException();

                } else if (glJournal.getJrApprovalStatus().equals("PENDING")) {

                    throw new GlobalTransactionAlreadyPendingException();
                }
            }

            if (glJournal.getJrPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();
            }

            // generate approval status

            String JR_APPRVL_STATUS = null;

            LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);

            // check if gl journal approval is enabled

            if (adApproval.getAprEnableGlJournal() == EJBCommon.FALSE) {

                JR_APPRVL_STATUS = "N/A";

            } else {

                // get highest coa amount coa line

                Integer HGHST_COA = null;
                double HGHST_COA_AMNT = 0d;

                Collection glHighestJournalLines = glJournal.getGlJournalLines();

                for (Object glHighestJournalLine : glHighestJournalLines) {

                    LocalGlJournalLine glJournalLine = (LocalGlJournalLine) glHighestJournalLine;

                    Collection adApprovalCoaLines = glJournalLine.getGlChartOfAccount().getAdApprovalCoaLines();

                    if (!adApprovalCoaLines.isEmpty()) {

                        if (glJournalLine.getJlAmount() > HGHST_COA_AMNT) {

                            HGHST_COA = glJournalLine.getGlChartOfAccount().getCoaCode();
                            HGHST_COA_AMNT = glJournalLine.getJlAmount();
                        }
                    }
                }

                if (HGHST_COA == null) {

                    JR_APPRVL_STATUS = "N/A";

                } else {

                    // check if journal is self approved

                    LocalAdAmountLimit adAmountLimit = null;

                    try {

                        adAmountLimit = adAmountLimitHome.findByCoaCodeAndAuTypeAndUsrName(HGHST_COA, "REQUESTER", glJournal.getJrLastModifiedBy(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new GlobalNoApprovalRequesterFoundException();
                    }

                    if (HGHST_COA_AMNT <= adAmountLimit.getCalAmountLimit()) {

                        JR_APPRVL_STATUS = "N/A";

                    } else {

                        // for approval, create approval queue

                        Collection adAmountLimits = adAmountLimitHome.findByCoaCodeAndGreaterThanCalAmountLimit(HGHST_COA, adAmountLimit.getCalAmountLimit(), AD_CMPNY);

                        if (adAmountLimits.isEmpty()) {

                            Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), AD_CMPNY);

                            if (adApprovalUsers.isEmpty()) {

                                throw new GlobalNoApprovalApproverFoundException();
                            }

                            for (Object approvalUser : adApprovalUsers) {

                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, AD_CMPNY, adApprovalQueueHome, glJournal, adAmountLimit, adApprovalUser);

                                adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                            }

                        } else {

                            boolean isApprovalUsersFound = false;

                            Iterator i = adAmountLimits.iterator();

                            while (i.hasNext()) {

                                LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();

                                if (HGHST_COA_AMNT <= adNextAmountLimit.getCalAmountLimit()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), AD_CMPNY);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, AD_CMPNY, adApprovalQueueHome, glJournal, adAmountLimit, adApprovalUser);

                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                    }

                                    break;

                                } else if (!i.hasNext()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adNextAmountLimit.getCalCode(), AD_CMPNY);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, AD_CMPNY, adApprovalQueueHome, glJournal, adNextAmountLimit, adApprovalUser);

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

                        JR_APPRVL_STATUS = "PENDING";
                    }
                }
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            if (JR_APPRVL_STATUS != null && JR_APPRVL_STATUS.equals("N/A") && adPreference.getPrfGlPostingType().equals("AUTO-POST UPON APPROVAL")) {

                this.executeGlJrPost(glJournal.getJrCode(), glJournal.getJrLastModifiedBy(), AD_BRNCH, AD_CMPNY);
            }

            // set journal approval status

            glJournal.setJrApprovalStatus(JR_APPRVL_STATUS);

        } catch (GlobalRecordAlreadyDeletedException | GlJREffectiveDatePeriodClosedException |
                 GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
                 GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
                 GlobalTransactionAlreadyApprovedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalAdApprovalQueue createApprovalQueue(Integer branchCode, Integer companyCode, LocalAdApprovalQueueHome adApprovalQueueHome, LocalGlJournal glJournal, LocalAdAmountLimit adAmountLimit, LocalAdApprovalUser adApprovalUser) throws CreateException {
        return adApprovalQueueHome.AqForApproval(EJBCommon.TRUE).AqDocument("GL JOURNAL").AqDocumentCode(glJournal.getJrCode()).AqDocumentNumber(glJournal.getJrDocumentNumber()).AqDate(glJournal.getJrEffectiveDate()).AqAndOr(adAmountLimit.getCalAndOr()).AqUserOr(adApprovalUser.getAuOr()).AqAdBranch(branchCode).AqAdCompany(companyCode).buildApprovalQueue();
    }


    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("GlJournalBatchControllerBean ejbCreate");
    }

    // private methods
    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("GlJournalBatchSubmitControllerBean convertForeignToFunctionalCurrency");

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

    private void executeGlJrPost(Integer JR_CODE, String USR_NM, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalTransactionAlreadyPostedException, GlJREffectiveDatePeriodClosedException, GlobalRecordAlreadyDeletedException {

        Debug.print("GlJournalBatchSubmitControllerBean executeGlJrPost");

        try {

            // get journal

            LocalGlJournal glJournal = null;

            try {

                glJournal = glJournalHome.findByPrimaryKey(JR_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if period is closed

            LocalGlSetOfBook glSetOfBook = glSetOfBookHome.findByDate(glJournal.getJrEffectiveDate(), AD_CMPNY);

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glSetOfBook.getGlAccountingCalendar().getAcCode(), glJournal.getJrEffectiveDate(), AD_CMPNY);

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

                double JL_AMNT = this.convertForeignToFunctionalCurrency(glJournal.getGlFunctionalCurrency().getFcCode(), glJournal.getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), glJournal.getJrConversionRate(), glJournalLine.getJlAmount(), AD_CMPNY);

                // post current to current acv

                this.post(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true, glJournalLine.getJlDebit(), JL_AMNT, AD_BRNCH, AD_CMPNY);

                // post to subsequent acvs (propagate)

                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), AD_CMPNY);

                for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                    this.post(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), JL_AMNT, AD_BRNCH, AD_CMPNY);
                }

                // post to subsequent years if necessary

                Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glSetOfBook.getGlAccountingCalendar().getAcYear(), AD_CMPNY);
                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

                if (!glSubsequentSetOfBooks.isEmpty() && glSetOfBook.getSobYearEndClosed() == 1) {

                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), AD_CMPNY);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), AD_CMPNY);

                        for (Object accountingCalendarValue : glAccountingCalendarValues) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                this.post(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), JL_AMNT, AD_BRNCH, AD_CMPNY);

                            } else { // revenue & expense

                                this.post(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, glJournalLine.getJlDebit(), JL_AMNT, AD_BRNCH, AD_CMPNY);
                            }
                        }

                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) break;
                    }
                }

                // for FOREX revaluation
                if ((!Objects.equals(glJournal.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(glJournal.getGlFunctionalCurrency().getFcCode()))) {

                    double CONVERSION_RATE = 1;

                    if (glJournal.getJrConversionRate() != 0 && glJournal.getJrConversionRate() != 1) {

                        CONVERSION_RATE = glJournal.getJrConversionRate();

                    } else if (glJournal.getJrConversionDate() != null) {

                        CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), AD_CMPNY);
                    }

                    Collection glForexLedgers = null;

                    try {

                        glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(glJournal.getJrEffectiveDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), AD_CMPNY);

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

                    glForexLedger = glForexLedgerHome.create(glJournal.getJrEffectiveDate(), FRL_LN, "JOURNAL", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, 0d, AD_CMPNY);

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
            glJournal.setJrPostedBy(USR_NM);
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

    private void post(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("GlJournalBatchSubmitControllerBean post");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), AD_CMPNY);

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

    private double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer AD_CMPNY) throws GlobalConversionDateNotExistException {

        Debug.print("GlJournalBatchSubmitControllerBean getFrRateByFrNameAndFrDate");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);

            double CONVERSION_RATE = 1;

            // Get functional currency rate

            if (!FC_NM.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), CONVERSION_DATE, AD_CMPNY);

                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, AD_CMPNY);

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
}