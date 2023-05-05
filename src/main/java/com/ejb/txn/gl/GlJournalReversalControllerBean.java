package com.ejb.txn.gl;

import java.util.*;

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
import com.ejb.exception.gl.GlJRJournalAlreadyReversedException;
import com.ejb.exception.global.GlobalConversionDateNotExistException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.dao.gl.ILocalGlTransactionCalendarValueHome;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
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
import com.ejb.entities.gl.LocalGlTransactionCalendarValue;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;
import com.util.mod.gl.GlModJournalDetails;

@Stateless(name = "GlJournalReversalControllerEJB")
public class GlJournalReversalControllerBean extends EJBContextClass implements GlJournalReversalController {

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

    public ArrayList getGlJcAll(Integer AD_CMPNY) {

        Debug.print("GlJournalReversalControllerBean getGlJcAll");

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

        Debug.print("GlJournalReversalControllerBean getGlJsAll");

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

        Debug.print("GlJournalReversalControllerBean getGlFcAllWithDefault");

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

    public ArrayList getGlJrReversibleByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlJournalReversalControllerBean getGlJrReversibleByCriteria");

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

        if (criteria.containsKey("reversalDateFrom")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("jr.jrDateReversal>=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("reversalDateFrom");
            ctr++;
        }

        if (criteria.containsKey("reversalDateTo")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("jr.jrDateReversal<=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("reversalDateTo");
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

        jbossQl.append("jr.jrReversed=0 AND jr.jrPosted=1 AND jr.jrAdBranch=").append(AD_BRNCH).append(" AND jr.jrAdCompany=").append(AD_CMPNY).append(" ");

        String orderBy = null;

        switch (ORDER_BY) {
            case "NAME":

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
            mdetails.setJrDateReversal(glJournal.getJrDateReversal());
            mdetails.setJrTotalDebit(TOTAL_DEBIT);
            mdetails.setJrTotalCredit(TOTAL_CREDIT);

            jrList.add(mdetails);
        }

        return jrList;
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GlJournalReversalControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void executeGlJrReverse(Integer JR_CODE, String JR_DCMNT_NMBR, Date JR_DT_RVRSL, String USR_NM, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyDeletedException, GlJREffectiveDateNoPeriodExistException, GlobalDocumentNumberNotUniqueException, GlJREffectiveDateViolationException, GlJREffectiveDatePeriodClosedException, GlJRJournalAlreadyReversedException {

        Debug.print("GlJournalLineControllerBean executeGlJrReverse");

        try {

            // get journal

            LocalGlJournal glJournal = null;

            try {

                glJournal = glJournalHome.findByPrimaryKey(JR_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if journal exists

            try {
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSSS");
                LocalGlJournal glExistingJournal = glJournalHome.findByJrName("REVERSED " + formatter.format(new Date()), AD_CMPNY);

                if (JR_CODE == null || JR_CODE != null && !glExistingJournal.getJrCode().equals(JR_CODE)) {

                    throw new GlobalRecordAlreadyExistException(glJournal.getJrName());
                }

            } catch (FinderException ex) {

            }

            // validate if effective date has no period and period is closed

            LocalGlSetOfBook glSetOfBook = null;

            try {

                glSetOfBook = glSetOfBookHome.findByDate(JR_DT_RVRSL, AD_CMPNY);

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glSetOfBook.getGlAccountingCalendar().getAcCode(), JR_DT_RVRSL, AD_CMPNY);

                if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                    throw new GlJREffectiveDatePeriodClosedException(glJournal.getJrName());
                }

            } catch (FinderException ex) {

                throw new GlJREffectiveDateNoPeriodExistException(glJournal.getJrName());
            }

            // validate reversal date violation

            LocalGlTransactionCalendarValue glTransactionCalendarValue = glTransactionCalendarValueHome.findByTcCodeAndTcvDate(glSetOfBook.getGlTransactionCalendar().getTcCode(), JR_DT_RVRSL, AD_CMPNY);

            LocalGlJournalSource glValidateJournalSource = glJournalSourceHome.findByJsName("JOURNAL REVERSAL", AD_CMPNY);

            if (glTransactionCalendarValue.getTcvBusinessDay() == EJBCommon.FALSE && glValidateJournalSource.getJsEffectiveDateRule() == 'F') {

                throw new GlJREffectiveDateViolationException(glJournal.getJrName());
            }

            // if validate if document number is unique document number is automatic then
            // set next
            // sequence

            try {

                LocalGlJournal glExistingDocumentJournal = glJournalHome.findByJrDocumentNumberAndJsNameAndBrCode(JR_DCMNT_NMBR, "MANUAL", AD_BRNCH, AD_CMPNY);

                throw new GlobalDocumentNumberNotUniqueException(glJournal.getJrName());

            } catch (FinderException ex) {

            }

            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("GL JOURNAL", AD_CMPNY);

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

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

            // validate if journal is already reversed

            if (glJournal.getJrReversed() == EJBCommon.TRUE) {

                throw new GlJRJournalAlreadyReversedException(glJournal.getJrName());
            }

            // generate approval status

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("JOURNAL REVERSAL", AD_CMPNY);
            String JR_APPRVL_STATUS = null;

            if (glJournalSource.getJsJournalApproval() == EJBCommon.FALSE) {

                JR_APPRVL_STATUS = "N/A";
            }

            // create reversal journal batch if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            LocalGlJournalBatch glJournalBatch = null;

            if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE) {

                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSSS");
                glJournalBatch = glJournalBatchHome.create(glJournal.getJrName() != null ? ("REVERSED " + glJournal.getJrName()) : ("JOURNAL REVERSAL " + formatter.format(new Date())), "JOURNAL REVERSAL", "OPEN", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, AD_CMPNY);
            }

            // create reversal journal

            LocalGlJournal glNewJournal = glJournalHome.create(glJournal.getJrName() != null ? ("REVERSED " + glJournal.getJrName()) : null, glJournal.getJrDescription(), JR_DT_RVRSL, 0.0d, null, JR_DCMNT_NMBR, glJournal.getJrConversionDate(), glJournal.getJrConversionRate(), JR_APPRVL_STATUS, null, 'N', EJBCommon.FALSE, EJBCommon.TRUE, USR_NM, new Date(), USR_NM, new Date(), null, null, null, null, glJournal.getJrTin(), glJournal.getJrSubLedger(), EJBCommon.FALSE, glJournal.getJrReferenceNumber(), AD_BRNCH, AD_CMPNY);

            // glJournalSource.addGlJournal(glNewJournal);
            glNewJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glJournal.getGlFunctionalCurrency();
            glNewJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournal.getGlJournalCategory();
            glNewJournal.setGlJournalCategory(glJournalCategory);

            if (glJournalBatch != null) {

                glNewJournal.setGlJournalBatch(glJournalBatch);
            }

            // reverse line

            Collection glJournalLines = glJournal.getGlJournalLines();

            for (Object journalLine : glJournalLines) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                LocalGlJournalLine glNewJournalLine = null;

                if (glJournal.getGlJournalCategory().getJcReversalMethod() == 'S') {

                    glNewJournalLine = glJournalLineHome.create(glJournalLine.getJlLineNumber(), glJournalLine.getJlDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, glJournalLine.getJlAmount(), glJournalLine.getJlDescription(), AD_CMPNY);

                    // glJournalLine.getGlChartOfAccount().addGlJournalLine(glNewJournalLine);
                    glNewJournalLine.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());
                    // glNewJournal.addGlJournalLine(glNewJournalLine);
                    glNewJournalLine.setGlJournal(glNewJournal);

                } else {

                    glNewJournalLine = glJournalLineHome.create(glJournalLine.getJlLineNumber(), glJournalLine.getJlDebit(), glJournalLine.getJlAmount() * -1, glJournalLine.getJlDescription(), AD_CMPNY);

                    // glJournalLine.getGlChartOfAccount().addGlJournalLine(glNewJournalLine);
                    glNewJournalLine.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());
                    // glNewJournal.addGlJournalLine(glNewJournalLine);
                    glNewJournalLine.setGlJournal(glNewJournal);
                }

                // for FOREX revaluation

                LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

                if ((!Objects.equals(glNewJournal.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glNewJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glNewJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(glNewJournal.getGlFunctionalCurrency().getFcCode()))) {

                    double CONVERSION_RATE = 1;

                    if (glJournal.getJrConversionRate() != 0 && glNewJournal.getJrConversionRate() != 1) {

                        CONVERSION_RATE = glNewJournal.getJrConversionRate();

                    } else if (glJournal.getJrConversionDate() != null) {

                        CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), AD_CMPNY);
                    }

                    Collection glForexLedgers = null;

                    try {

                        glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(glJournal.getJrEffectiveDate(), glNewJournalLine.getGlChartOfAccount().getCoaCode(), AD_CMPNY);

                    } catch (FinderException ex) {

                    }

                    LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                    int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(glJournal.getJrEffectiveDate()) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                    // compute balance
                    double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                    double FRL_AMNT = glNewJournalLine.getJlAmount();

                    if (glNewJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {

                        FRL_AMNT = (glNewJournalLine.getJlDebit() == 1 ? FRL_AMNT : (-1 * FRL_AMNT));

                    } else {

                        FRL_AMNT = (glNewJournalLine.getJlDebit() == 1 ? (-1 * FRL_AMNT) : FRL_AMNT);
                    }

                    COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                    glForexLedger = glForexLedgerHome.create(glNewJournal.getJrEffectiveDate(), FRL_LN, "JOURNAL", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, 0, AD_CMPNY);

                    // glNewJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);
                    glForexLedger.setGlChartOfAccount(glNewJournalLine.getGlChartOfAccount());

                    // propagate balances
                    try {

                        glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                    } catch (FinderException ex) {

                    }

                    for (Object forexLedger : glForexLedgers) {

                        glForexLedger = (LocalGlForexLedger) forexLedger;
                        FRL_AMNT = glNewJournalLine.getJlAmount();

                        if (glNewJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET"))
                            FRL_AMNT = (glNewJournalLine.getJlDebit() == 1 ? FRL_AMNT : (-1 * FRL_AMNT));
                        else FRL_AMNT = (glNewJournalLine.getJlDebit() == 1 ? (-1 * FRL_AMNT) : FRL_AMNT);

                        glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);
                    }
                }
            }

            // set journal status and log
            glJournal.setJrReversed(EJBCommon.TRUE);
            glJournal.setJrLastModifiedBy(USR_NM);
            glJournal.setJrDateLastModified(EJBCommon.getGcCurrentDateWoTime().getTime());

        } catch (GlobalRecordAlreadyExistException | GlJREffectiveDatePeriodClosedException |
                 GlobalRecordAlreadyDeletedException | GlJRJournalAlreadyReversedException |
                 GlJREffectiveDateViolationException | GlobalDocumentNumberNotUniqueException |
                 GlJREffectiveDateNoPeriodExistException ex) {

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

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("GlJournalReversalControllerBean convertForeignToFunctionalCurrency");

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

    private double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer AD_CMPNY) throws GlobalConversionDateNotExistException {

        Debug.print("GlJournalReversalControllerBean getFrRateByFrNameAndFrDate");

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