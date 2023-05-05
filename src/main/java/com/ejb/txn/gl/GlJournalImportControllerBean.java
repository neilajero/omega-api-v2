package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.StringTokenizer;

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
import com.ejb.entities.gen.LocalGenField;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.dao.gl.ILocalGlTransactionCalendarValueHome;
import com.ejb.entities.gl.LocalGlAccountRange;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlChartOfAccount;
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
import com.ejb.entities.gl.LocalGlJournalInterface;
import com.ejb.dao.gl.LocalGlJournalInterfaceHome;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.entities.gl.LocalGlJournalLineInterface;
import com.ejb.dao.gl.LocalGlJournalLineInterfaceHome;
import com.ejb.entities.gl.LocalGlJournalSource;
import com.ejb.dao.gl.LocalGlJournalSourceHome;
import com.ejb.entities.gl.LocalGlOrganization;
import com.ejb.entities.gl.LocalGlResponsibility;
import com.ejb.dao.gl.LocalGlResponsibilityHome;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.ejb.entities.gl.LocalGlSuspenseAccount;
import com.ejb.dao.gl.LocalGlSuspenseAccountHome;
import com.ejb.entities.gl.LocalGlTransactionCalendarValue;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModAccountingCalendarValueDetails;

@Stateless(name = "GlJournalImportControllerEJB")
public class GlJournalImportControllerBean extends EJBContextClass implements GlJournalImportController {

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
    private ILocalGlTransactionCalendarValueHome glTransactionCalendarValueHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
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
    private LocalGlJournalInterfaceHome glJournalInterfaceHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlJournalLineInterfaceHome glJournalLineInterfaceHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlResponsibilityHome glResponsibilityHome;
    @EJB
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;

    public ArrayList getGlJsAll(Integer AD_CMPNY) {

        Debug.print("GlJournalImportControllerBean getGlJsAll");

        ArrayList list = new ArrayList();

        try {

            Collection glJournalSources = glJournalSourceHome.findJsAll(AD_CMPNY);

            for (Object journalSource : glJournalSources) {

                LocalGlJournalSource glJournalSource = (LocalGlJournalSource) journalSource;

                list.add(glJournalSource.getJsName());
            }

            return list;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlAcAllEditableOpenAndFutureEntry(Integer AD_CMPNY) {

        Debug.print("GlRepDetailIncomeStatementControllerBean getGlAcAllEditableOpenAndFutureEntry");

        ArrayList list = new ArrayList();

        try {

            Collection glSetOfBooks = glSetOfBookHome.findSobAll(AD_CMPNY);

            for (Object setOfBook : glSetOfBooks) {

                LocalGlSetOfBook glSetOfBook = (LocalGlSetOfBook) setOfBook;

                Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findOpenAndFutureEntryAcvByAcCode(glSetOfBook.getGlAccountingCalendar().getAcCode(), 'O', 'F', AD_CMPNY);

                for (Object accountingCalendarValue : glAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                    GlModAccountingCalendarValueDetails mdetails = new GlModAccountingCalendarValueDetails();

                    mdetails.setAcvPeriodPrefix(glAccountingCalendarValue.getAcvPeriodPrefix());

                    // get year

                    GregorianCalendar gc = new GregorianCalendar();
                    gc.setTime(glAccountingCalendarValue.getAcvDateTo());

                    mdetails.setAcvYear(gc.get(Calendar.YEAR));

                    list.add(mdetails);
                }
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public long executeGlJrImport(String JS_NM, String PRD_NM, int YR, String USR_NM, Integer RES_CODE, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("GlJournalImportControllerBean executeGlJrImport");

        long IMPORTED_JOURNALS = 0L;

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSSS");

        LocalGlJournalBatch glJournalBatch = null;

        try {

            // get preference

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            // get selected set of book

            Collection glSetOfBooks = glSetOfBookHome.findByAcvPeriodPrefixAndDate(PRD_NM, EJBCommon.getIntendedDate(YR), AD_CMPNY);
            ArrayList glSetOfBookList = new ArrayList(glSetOfBooks);

            LocalGlSetOfBook glSelectedSetOfBook = (LocalGlSetOfBook) glSetOfBookList.get(0);

            LocalGlFunctionalCurrency glFunctionalCurrency = null;
            LocalGlJournalCategory glJournalCategory = null;

            // get accounting calendar value

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodPrefix(glSelectedSetOfBook.getGlAccountingCalendar().getAcCode(), PRD_NM, AD_CMPNY);

            // get journal source

            ArrayList glJournalSourceList = null;

            if (JS_NM == null || JS_NM.length() == 0) {

                Collection glJournalSources = glJournalSourceHome.findJsAll(AD_CMPNY);
                glJournalSourceList = new ArrayList(glJournalSources);

            } else {

                LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName(JS_NM, AD_CMPNY);

                glJournalSourceList = new ArrayList();
                glJournalSourceList.add(glJournalSource);
            }

            for (Object o : glJournalSourceList) {

                LocalGlJournalSource glJournalSource = (LocalGlJournalSource) o;

                Collection glJournalInterfaces = glJournalInterfaceHome.findByJsNameAndDateRange(glJournalSource.getJsName(), glAccountingCalendarValue.getAcvDateFrom(), glAccountingCalendarValue.getAcvDateTo(), AD_CMPNY);

                if (!glJournalInterfaces.isEmpty()) {

                    Iterator i = glJournalInterfaces.iterator();

                    ArrayList glJournalInterfaceList = new ArrayList();

                    while (i.hasNext()) {

                        LocalGlJournalInterface glJournalInterface = (LocalGlJournalInterface) i.next();

                        // validate if calendar is open

                        if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                            continue;
                        }

                        // validate if reversal date has period and it is after journal

                        try {

                            if (glJournalInterface.getJriDateReversal() != null) {

                                LocalGlAccountingCalendarValue glReversalPeriodAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glSelectedSetOfBook.getGlAccountingCalendar().getAcCode(), glJournalInterface.getJriDateReversal(), AD_CMPNY);

                                if (!glJournalInterface.getJriDateReversal().after(glJournalInterface.getJriEffectiveDate())) {

                                    continue;
                                }
                            }

                        } catch (FinderException ex) {

                            continue;
                        }

                        // validate if date is valid

                        LocalGlTransactionCalendarValue glTransactionCalendarValue = glTransactionCalendarValueHome.findByTcCodeAndTcvDate(glSelectedSetOfBook.getGlTransactionCalendar().getTcCode(), glJournalInterface.getJriEffectiveDate(), AD_CMPNY);

                        LocalGlJournalSource glValidateJournalSource = glJournalSourceHome.findByJsName(glJournalSource.getJsName(), AD_CMPNY);

                        if (glTransactionCalendarValue.getTcvBusinessDay() == EJBCommon.FALSE && glValidateJournalSource.getJsEffectiveDateRule() == 'F') {

                            continue;
                        }

                        // validate conversion date and rate

                        try {

                            if (glJournalInterface.getJriConversionDate() == null && glJournalInterface.getJriConversionRate() == 0) {

                                continue;
                            }

                            if (glJournalInterface.getJriConversionDate() != null && glJournalInterface.getJriConversionRate() != 0) {

                                continue;
                            }

                            if (glJournalInterface.getJriConversionDate() != null) {

                                LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(glJournalInterface.getJriFunctionalCurrency(), AD_CMPNY);
                                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), glJournalInterface.getJriConversionDate(), AD_CMPNY);
                            }

                        } catch (FinderException ex) {

                            continue;
                        }

                        // validate if journal category exists

                        try {

                            glJournalCategory = glJournalCategoryHome.findByJcName(glJournalInterface.getJriJournalCategory(), AD_CMPNY);

                        } catch (FinderException ex) {

                            continue;
                        }

                        // validate if currency exists

                        try {

                            glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(glJournalInterface.getJriFunctionalCurrency(), AD_CMPNY);

                        } catch (FinderException ex) {

                            continue;
                        }

                        // validate journal line interfaces

                        Collection glValidateJournalLineInterfaces = glJournalInterface.getGlJournalLineInterfaces();

                        // validate if journal interface has lines

                        if (glValidateJournalLineInterfaces.size() < 2) {

                            continue;
                        }

                        boolean journalLineInterfaceHasErrors = false;

                        for (Object glValidateJournalLineInterface : glValidateJournalLineInterfaces) {

                            LocalGlJournalLineInterface glJournalLineInterface = (LocalGlJournalLineInterface) glValidateJournalLineInterface;

                            // validate if account exists

                            LocalGlChartOfAccount glChartOfAccount = null;

                            try {

                                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(glJournalLineInterface.getJliCoaAccountNumber(), AD_CMPNY);

                            } catch (FinderException ex) {

                                journalLineInterfaceHasErrors = true;
                                break;
                            }

                            // validate if chart of account has permission to user

                            LocalGlResponsibility glResponsibility = glResponsibilityHome.findByPrimaryKey(RES_CODE);

                            if (!this.isPermitted(glResponsibility.getGlOrganization(), glChartOfAccount, adCompany.getGenField(), AD_CMPNY)) {

                                journalLineInterfaceHasErrors = true;
                                break;
                            }
                        }

                        if (journalLineInterfaceHasErrors) {

                            continue;
                        }

                        // check if journal interface is balance if not check suspense posting

                        LocalGlJournalLine glOffsetJournalLine = null;

                        Collection glJournalLineInterfaces = glJournalInterface.getGlJournalLineInterfaces();

                        Iterator j = glJournalLineInterfaces.iterator();

                        double TOTAL_DEBIT = 0d;
                        double TOTAL_CREDIT = 0d;

                        while (j.hasNext()) {

                            LocalGlJournalLineInterface glJournalLineInterface = (LocalGlJournalLineInterface) j.next();

                            if (glJournalLineInterface.getJliDebit() == EJBCommon.TRUE) {

                                TOTAL_DEBIT += glJournalLineInterface.getJliAmount();

                            } else {

                                TOTAL_CREDIT += glJournalLineInterface.getJliAmount();
                            }
                        }

                        TOTAL_DEBIT = EJBCommon.roundIt(TOTAL_DEBIT, adCompany.getGlFunctionalCurrency().getFcPrecision());
                        TOTAL_CREDIT = EJBCommon.roundIt(TOTAL_CREDIT, adCompany.getGlFunctionalCurrency().getFcPrecision());

                        if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.TRUE && TOTAL_DEBIT != TOTAL_CREDIT) {

                            LocalGlSuspenseAccount glSuspenseAccount = null;

                            try {

                                glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName(glJournalSource.getJsName(), glJournalInterface.getJriJournalCategory(), AD_CMPNY);

                            } catch (FinderException ex) {

                                continue;
                            }

                            if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                                glOffsetJournalLine = glJournalLineHome.create((short) (glJournalLineInterfaces.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", AD_CMPNY);

                            } else {

                                glOffsetJournalLine = glJournalLineHome.create((short) (glJournalLineInterfaces.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", AD_CMPNY);
                            }

                            LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                            glChartOfAccount.addGlJournalLine(glOffsetJournalLine);

                        } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                            continue;
                        }

                        if (glJournalInterface.getJriJournalSource().equals("MANUAL")) {

                            String JR_DCMNT_NMBR = null;

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

                            glJournalInterface.setJriDocumentNumber(JR_DCMNT_NMBR);
                        }

                        // validate if document number is unique

                        try {

                            LocalGlJournal glExistingJournal = glJournalHome.findByJrDocumentNumberAndJsNameAndBrCode(glJournalInterface.getJriDocumentNumber(), "MANUAL", AD_BRNCH, AD_CMPNY);

                            continue;

                        } catch (FinderException ex) {

                        }

                        // create journal batch if necessary

                        if (adPreference.getPrfEnableGlJournalBatch() == EJBCommon.TRUE && glJournalBatch == null) {

                            glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()), "JOURNAL IMPORT", "OPEN", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, AD_CMPNY);
                        }

                        // create journal

                        LocalGlJournal glJournal = null;

                        try {

                            // generate approval status

                            String JR_APPRVL_STATUS = null;

                            if (glJournalSource.getJsJournalApproval() == EJBCommon.FALSE) {

                                JR_APPRVL_STATUS = "N/A";
                            }

                            glJournal = glJournalHome.create(glJournalInterface.getJriName(), glJournalInterface.getJriDescription(), glJournalInterface.getJriEffectiveDate(), 0.0d, glJournalInterface.getJriDateReversal(), glJournalInterface.getJriDocumentNumber(), glJournalInterface.getJriConversionDate(), glJournalInterface.getJriConversionRate(), JR_APPRVL_STATUS, null, glJournalInterface.getJriFundStatus(), EJBCommon.FALSE, glJournalInterface.getJriReversed(), USR_NM, new Date(), USR_NM, new Date(), null, null, null, null, glJournalInterface.getJriTin(), glJournalInterface.getJriSubLedger(), EJBCommon.FALSE, null, AD_BRNCH, AD_CMPNY);

                            glJournal.setGlJournalSource(glJournalSource);
                            glJournal.setGlJournalCategory(glJournalCategory);
                            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

                            if (glJournalBatch != null) {

                                glJournal.setGlJournalBatch(glJournalBatch);
                            }

                        } catch (CreateException ex) {

                            getSessionContext().setRollbackOnly();
                            continue;
                        }

                        // create journal lines

                        j = glJournalLineInterfaces.iterator();

                        while (j.hasNext()) {

                            LocalGlJournalLineInterface glJournalLineInterface = (LocalGlJournalLineInterface) j.next();

                            LocalGlJournalLine glJournalLine = glJournalLineHome.create(glJournalLineInterface.getJliLineNumber(), glJournalLineInterface.getJliDebit(), glJournalLineInterface.getJliAmount(), "", AD_CMPNY);

                            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(glJournalLineInterface.getJliCoaAccountNumber(), AD_CMPNY);
                            glChartOfAccount.addGlJournalLine(glJournalLine);

                            glJournal.addGlJournalLine(glJournalLine);
                        }

                        if (glOffsetJournalLine != null) {

                            glJournal.addGlJournalLine(glOffsetJournalLine);
                        }

                        ++IMPORTED_JOURNALS;
                        glJournalInterfaceList.add(glJournalInterface);
                    }

                    // delete imported journals

                    i = glJournalInterfaceList.iterator();

                    while (i.hasNext()) {

                        LocalGlJournalInterface glJournalInterface = (LocalGlJournalInterface) i.next();

                        i.remove();
                        em.remove(glJournalInterface);
                    }
                }
            }

            return IMPORTED_JOURNALS;

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private boolean isPermitted(LocalGlOrganization glOrganization, LocalGlChartOfAccount glChartOfAccount, LocalGenField genField, Integer AD_CMPNY) {

        Debug.print("GlJournalEntryControllerBean isPermitted");

        String strSeparator = null;
        short numberOfSegment = 0;
        short genNumberOfSegment = 0;

        try {
            char chrSeparator = genField.getFlSegmentSeparator();
            genNumberOfSegment = genField.getFlNumberOfSegment();
            strSeparator = String.valueOf(chrSeparator);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        Collection glAccountRanges = glOrganization.getGlAccountRanges();

        for (Object accountRange : glAccountRanges) {

            LocalGlAccountRange glAccountRange = (LocalGlAccountRange) accountRange;

            String[] chartOfAccountSegmentValue = new String[genNumberOfSegment];
            String[] accountLowSegmentValue = new String[genNumberOfSegment];
            String[] accountHighSegmentValue = new String[genNumberOfSegment];

            // tokenize coa

            StringTokenizer st = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), strSeparator);

            int j = 0;

            while (st.hasMoreTokens()) {

                chartOfAccountSegmentValue[j] = st.nextToken();
                j++;
            }

            // tokenize account low

            st = new StringTokenizer(glAccountRange.getArAccountLow(), strSeparator);

            j = 0;

            while (st.hasMoreTokens()) {

                accountLowSegmentValue[j] = st.nextToken();
                j++;
            }

            // tokenize account high
            st = new StringTokenizer(glAccountRange.getArAccountHigh(), strSeparator);

            j = 0;

            while (st.hasMoreTokens()) {

                accountHighSegmentValue[j] = st.nextToken();
                j++;
            }

            boolean isOverlapped = false;

            for (int k = 0; k < genNumberOfSegment; k++) {

                if (chartOfAccountSegmentValue[k].compareTo(accountLowSegmentValue[k]) >= 0 && chartOfAccountSegmentValue[k].compareTo(accountHighSegmentValue[k]) <= 0) {

                    isOverlapped = true;

                } else {

                    isOverlapped = false;
                    break;
                }
            }

            if (isOverlapped) return true;
        }

        return false;
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlJournalImportControllerBean ejbCreate");
    }
}