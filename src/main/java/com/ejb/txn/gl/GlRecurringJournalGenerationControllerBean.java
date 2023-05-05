package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdBranchDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdBranchDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.gl.GlJREffectiveDateViolationException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.dao.gl.ILocalGlTransactionCalendarValueHome;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
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
import com.ejb.entities.gl.LocalGlRecurringJournal;
import com.ejb.dao.gl.LocalGlRecurringJournalHome;
import com.ejb.entities.gl.LocalGlRecurringJournalLine;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.ejb.entities.gl.LocalGlTransactionCalendarValue;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModRecurringJournalDetails;

@Stateless(name = "GlRecurringJournalGenerationControllerEJB")
public class GlRecurringJournalGenerationControllerBean extends EJBContextClass implements GlRecurringJournalGenerationController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private ILocalGlTransactionCalendarValueHome glTransactionCalendarValueHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
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
    private LocalGlRecurringJournalHome glRecurringJournalHome;

    public ArrayList getGlJcAll(Integer AD_CMPNY) {

        Debug.print("GlRecurringJournalGenerationControllerBean getGlJcAll");

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

    public ArrayList getGlRjByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlRecurringJournalGenerationControllerBean getGlJrPostableByCriteria");

        ArrayList rjList = new ArrayList();

        StringBuilder jbossQl = new StringBuilder();
        jbossQl.append("SELECT OBJECT(rj) FROM GlRecurringJournal rj ");

        boolean firstArgument = true;
        short ctr = 0;
        int criteriaSize = criteria.size();
        Object[] obj;

        // Allocate the size of the object parameter

        if (criteria.containsKey("name")) {

            criteriaSize--;
        }

        obj = new Object[criteriaSize];

        if (criteria.containsKey("name")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("rj.rjName LIKE '%").append(criteria.get("name")).append("%' ");
        }

        if (criteria.containsKey("nextRunDateFrom")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("rj.rjNextRunDate>=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("nextRunDateFrom");
            ctr++;
        }

        if (criteria.containsKey("nextRunDateTo")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("rj.rjNextRunDate<=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("nextRunDateTo");
            ctr++;
        }

        if (criteria.containsKey("category")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("rj.glJournalCategory.jcName=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("category");
            ctr++;
        }

        if (!firstArgument) {

            jbossQl.append("AND ");

        } else {

            firstArgument = false;
            jbossQl.append("WHERE ");
        }

        jbossQl.append("rj.rjAdBranch=").append(AD_BRNCH).append(" AND rj.rjAdCompany=").append(AD_CMPNY).append(" ");

        String orderBy = null;

        switch (ORDER_BY) {
            case "NAME":

                orderBy = "rj.rjName";

                break;
            case "NEXT RUN DATE":

                orderBy = "rj.rjNextRunDate";

                break;
            case "CATEGORY":

                orderBy = "rj.glJournalCategory.jcName";
                break;
        }

        if (orderBy != null) {

            jbossQl.append("ORDER BY ").append(orderBy);
        }

        Debug.print("QL + " + jbossQl);

        Collection glRecurringJournals = null;

        double TOTAL_DEBIT = 0;
        double TOTAL_CREDIT = 0;

        try {

            glRecurringJournals = glRecurringJournalHome.getRjByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glRecurringJournals.isEmpty()) throw new GlobalNoRecordFoundException();

        for (Object recurringJournal : glRecurringJournals) {

            TOTAL_DEBIT = 0d;
            TOTAL_CREDIT = 0d;

            LocalGlRecurringJournal glRecurringJournal = (LocalGlRecurringJournal) recurringJournal;
            Collection glRecurringJournalLines = glRecurringJournal.getGlRecurringJournalLines();

            for (Object recurringJournalLine : glRecurringJournalLines) {

                LocalGlRecurringJournalLine glRecurringJournalLine = (LocalGlRecurringJournalLine) recurringJournalLine;

                if (glRecurringJournalLine.getRjlDebit() == EJBCommon.TRUE) {

                    TOTAL_DEBIT += glRecurringJournalLine.getRjlAmount();

                } else {

                    TOTAL_CREDIT += glRecurringJournalLine.getRjlAmount();
                }
            }

            // generate new next run date

            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(glRecurringJournal.getRjNextRunDate());

            switch (glRecurringJournal.getRjSchedule()) {
                case "DAILY":

                    gc.add(Calendar.DATE, 1);

                    break;
                case "WEEKLY":

                    gc.add(Calendar.DATE, 7);

                    break;
                case "SEMI MONTHLY":

                    gc.add(Calendar.DATE, 15);

                    break;
                case "MONTHLY":

                    gc.add(Calendar.MONTH, 1);

                    break;
                case "QUARTERLY":

                    gc.add(Calendar.MONTH, 3);

                    break;
                case "SEMI ANNUALLY":

                    gc.add(Calendar.MONTH, 6);

                    break;
                case "ANNUALLY":

                    gc.add(Calendar.YEAR, 1);
                    break;
            }

            GlModRecurringJournalDetails mdetails = new GlModRecurringJournalDetails();
            mdetails.setRjCode(glRecurringJournal.getRjCode());
            mdetails.setRjName(glRecurringJournal.getRjName());
            mdetails.setRjNextRunDate(glRecurringJournal.getRjNextRunDate());
            mdetails.setRjSchedule(glRecurringJournal.getRjSchedule());
            mdetails.setRjLastRunDate(glRecurringJournal.getRjLastRunDate());
            mdetails.setRjTotalDebit(TOTAL_DEBIT);
            mdetails.setRjTotalCredit(TOTAL_CREDIT);
            mdetails.setRjNewNextRunDate(gc.getTime());

            rjList.add(mdetails);
        }

        return rjList;
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GlRecurringJournalGenerationControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void executeGlRjGeneration(Integer RJ_CODE, Date RJ_NXT_RN_DT, String JR_DCMNT_NMBR, Date JR_EFFCTV_DT, String USR_NM, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlJREffectiveDateNoPeriodExistException, GlobalDocumentNumberNotUniqueException, GlJREffectiveDateViolationException, GlJREffectiveDatePeriodClosedException {

        Debug.print("GlJournalLineControllerBean executeGlRjGeneration");

        LocalGlSetOfBook glSetOfBook = null;

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSSS");

        try {

            // get recurring journal

            LocalGlRecurringJournal glRecurringJournal = null;

            try {

                glRecurringJournal = glRecurringJournalHome.findByPrimaryKey(RJ_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if period exists and open

            try {

                glSetOfBook = glSetOfBookHome.findByDate(JR_EFFCTV_DT, AD_CMPNY);

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glSetOfBook.getGlAccountingCalendar().getAcCode(), JR_EFFCTV_DT, AD_CMPNY);

                if (glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P' || glAccountingCalendarValue.getAcvStatus() == 'N') {

                    throw new GlJREffectiveDatePeriodClosedException(glRecurringJournal.getRjName());
                }

            } catch (FinderException ex) {

                throw new GlJREffectiveDateNoPeriodExistException(glRecurringJournal.getRjName());
            }

            // validate effective date violation

            LocalGlTransactionCalendarValue glTransactionCalendarValue = glTransactionCalendarValueHome.findByTcCodeAndTcvDate(glSetOfBook.getGlTransactionCalendar().getTcCode(), JR_EFFCTV_DT, AD_CMPNY);

            LocalGlJournalSource glValidateJournalSource = glJournalSourceHome.findByJsName("RECURRING", AD_CMPNY);

            if (glTransactionCalendarValue.getTcvBusinessDay() == EJBCommon.FALSE && glValidateJournalSource.getJsEffectiveDateRule() == 'F') {

                throw new GlJREffectiveDateViolationException(glRecurringJournal.getRjName());
            }

            // validate if document number is unique

            try {

                LocalGlJournal glExistingJournal = glJournalHome.findByJrDocumentNumberAndJsNameAndBrCode(JR_DCMNT_NMBR, "MANUAL", AD_BRNCH, AD_CMPNY);

                throw new GlobalDocumentNumberNotUniqueException(glRecurringJournal.getRjName());

            } catch (FinderException ex) {
            }

            // generate document number if necessary
            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("GL JOURNAL", AD_CMPNY);

            try {

                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

            } catch (FinderException ex) {

            }

            if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (JR_DCMNT_NMBR == null || JR_DCMNT_NMBR.trim().length() == 0)) {

                while (true) {

                    if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                        try {

                            glJournalHome.findByJrDocumentNumberAndJsNameAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), "MANUAL", AD_BRNCH, AD_CMPNY);
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                        } catch (FinderException ex) {

                            JR_DCMNT_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            break;
                        }

                    } else {

                        try {

                            glJournalHome.findByJrDocumentNumberAndJsNameAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), "MANUAL", AD_BRNCH, AD_CMPNY);
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                        } catch (FinderException ex) {

                            JR_DCMNT_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                            break;
                        }
                    }
                }
            }

            // generate approval status

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("RECURRING", AD_CMPNY);
            String JR_APPRVL_STATUS = null;

            if (glJournalSource.getJsJournalApproval() == EJBCommon.FALSE) {

                JR_APPRVL_STATUS = "N/A";
            }

            // general journal batch if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            LocalGlJournalBatch glJournalBatch = null;

            if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE) {

                glJournalBatch = glRecurringJournal.getGlJournalBatch();
            }

            // generate recurring journal

            LocalGlJournal glJournal = glJournalHome.create(glRecurringJournal.getRjName() + " " + formatter.format(new java.util.Date()), glRecurringJournal.getRjDescription(), JR_EFFCTV_DT, 0d, null, JR_DCMNT_NMBR, null, 1d, JR_APPRVL_STATUS, null, 'N', EJBCommon.FALSE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, null, null, null, null, EJBCommon.FALSE, null, AD_BRNCH, AD_CMPNY);

            glJournal.setGlJournalCategory(glRecurringJournal.getGlJournalCategory());
            glJournal.setGlJournalSource(glJournalSource);
            glJournal.setGlFunctionalCurrency(adCompany.getGlFunctionalCurrency());

            if (glJournalBatch != null) {

                glJournal.setGlJournalBatch(glJournalBatch);
            }

            Collection glRecurringJournalLines = glRecurringJournal.getGlRecurringJournalLines();

            for (Object recurringJournalLine : glRecurringJournalLines) {

                LocalGlRecurringJournalLine glRecurringJournalLine = (LocalGlRecurringJournalLine) recurringJournalLine;

                LocalGlJournalLine glJournalLine = glJournalLineHome.create(glRecurringJournalLine.getRjlLineNumber(), glRecurringJournalLine.getRjlDebit(), glRecurringJournalLine.getRjlAmount(), "", AD_CMPNY);

                glRecurringJournalLine.getGlChartOfAccount().addGlJournalLine(glJournalLine);

                glJournal.addGlJournalLine(glJournalLine);
            }

            // set new run date

            glRecurringJournal.setRjNextRunDate(RJ_NXT_RN_DT);
            glRecurringJournal.setRjLastRunDate(EJBCommon.getGcCurrentDateWoTime().getTime());

        } catch (GlobalDocumentNumberNotUniqueException | GlJREffectiveDateNoPeriodExistException |
                 GlJREffectiveDatePeriodClosedException | GlobalRecordAlreadyDeletedException |
                 GlJREffectiveDateViolationException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlJournalBatchControllerBean ejbCreate");
    }

    // private methods

}