package com.ejb.txnreports.gl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.dao.cm.LocalCmDistributionRecordHome;
import com.ejb.exception.gl.GlRepBGTPeriodOutOfRangeException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gen.LocalGenSegment;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountBalanceHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlBudget;
import com.ejb.entities.gl.LocalGlBudgetAmountCoa;
import com.ejb.dao.gl.LocalGlBudgetAmountHome;
import com.ejb.entities.gl.LocalGlBudgetAmountPeriod;
import com.ejb.dao.gl.LocalGlBudgetAmountPeriodHome;
import com.ejb.dao.gl.LocalGlBudgetHome;
import com.ejb.entities.gl.LocalGlBudgetOrganization;
import com.ejb.dao.gl.LocalGlBudgetOrganizationHome;
import com.ejb.entities.gl.LocalGlChartOfAccountBalance;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.gl.LocalGlJournal;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.ejb.dao.inv.LocalInvDistributionRecordHome;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.gl.GlModAccountingCalendarValueDetails;
import com.util.mod.gl.GlModBudgetDetails;
import com.util.reports.gl.GlRepBudgetDetails;

@Stateless(name = "GlRepBudgetControllerEJB")
public class GlRepBudgetControllerBean extends EJBContextClass implements GlRepBudgetController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    LocalGlBudgetOrganizationHome glBudgetOrganizationHome;
    @EJB
    LocalGlBudgetHome glBudgetHome;
    @EJB
    LocalGlBudgetAmountHome glBudgetAmountHome;
    @EJB
    LocalGlBudgetAmountPeriodHome glBudgetAmountPeriodHome;
    @EJB
    LocalGenValueSetValueHome genValueSetValueHome;
    @EJB
    LocalGenSegmentHome genSegmentHome;
    @EJB
    LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    LocalArInvoiceHome arInvoiceHome;
    @EJB
    LocalApVoucherHome apVoucherHome;
    @EJB
    LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    LocalCmDistributionRecordHome cmDistributionRecordHome;
    @EJB
    LocalAdBankAccountHome adBankAccountHome;
    @EJB
    LocalGlJournalLineHome glJournalLineHome;
    @EJB
    LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    LocalAdBranchHome adBranchHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;

    public ArrayList getGlAcvAll(Integer AD_CMPNY) {

        Debug.print("GlRepBudgetControllerBean getGlAcvAll");

        ArrayList list = new ArrayList();

        try {

            Collection glSetOfBooks = glSetOfBookHome.findSobAll(AD_CMPNY);

            for (Object setOfBook : glSetOfBooks) {

                LocalGlSetOfBook glSetOfBook = (LocalGlSetOfBook) setOfBook;

                Collection glAccountingCalendarValues = glSetOfBook.getGlAccountingCalendar().getGlAccountingCalendarValues();

                for (Object accountingCalendarValue : glAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                    GlModAccountingCalendarValueDetails mdetails = new GlModAccountingCalendarValueDetails();

                    mdetails.setAcvPeriodPrefix(glAccountingCalendarValue.getAcvPeriodPrefix());

                    GregorianCalendar gc = new GregorianCalendar();
                    gc.setTime(glAccountingCalendarValue.getAcvDateTo());

                    mdetails.setAcvYear(gc.get(Calendar.YEAR));

                    // is current

                    gc = EJBCommon.getGcCurrentDateWoTime();

                    if ((glAccountingCalendarValue.getAcvDateFrom().before(gc.getTime()) || glAccountingCalendarValue.getAcvDateFrom().equals(gc.getTime())) && (glAccountingCalendarValue.getAcvDateTo().after(gc.getTime()) || glAccountingCalendarValue.getAcvDateTo().equals(gc.getTime()))) {

                        mdetails.setAcvCurrent(true);
                    }

                    list.add(mdetails);
                }
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlBoAll(Integer AD_CMPNY) {

        Debug.print("GlRepBudgetControllerBean getGlBoAll");

        ArrayList list = new ArrayList();

        try {

            Collection glBudgetOrganizations = glBudgetOrganizationHome.findBoAll(AD_CMPNY);

            for (Object budgetOrganization : glBudgetOrganizations) {

                LocalGlBudgetOrganization glBudgetOrganization = (LocalGlBudgetOrganization) budgetOrganization;

                list.add(glBudgetOrganization.getBoName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlBgtAll(Integer AD_CMPNY) {

        Debug.print("GlRepBudgetControllerBean getGlBgtAll");

        ArrayList list = new ArrayList();

        try {

            Collection glBudgets = glBudgetHome.findBgtAll(AD_CMPNY);

            for (Object budget : glBudgets) {

                LocalGlBudget glBudget = (LocalGlBudget) budget;

                GlModBudgetDetails mdetails = new GlModBudgetDetails();
                mdetails.setBgtName(glBudget.getBgtName());

                if (glBudget.getBgtStatus().equals("CURRENT")) {

                    mdetails.setBgtIsDefault(true);
                }

                list.add(mdetails);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeGlRepBudget(String BGT_BO_NM, String BGT_NM, String BGT_PRD, int BGT_YR, String BGT_AMNT_TYP, boolean DIS_INCLD_UNPSTD, boolean DIS_INCLD_UNPSTD_SL, boolean DTB_SHW_ZRS, ArrayList branchList, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlRepBGTPeriodOutOfRangeException {

        Debug.print("GlRepBudgetControllerBean executeGlRepBudget");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGenField genField = adCompany.getGenField();
            String FL_SGMNT_SPRTR = String.valueOf(genField.getFlSegmentSeparator());
            Collection genSegments = genSegmentHome.findByFlCode(genField.getFlCode(), AD_CMPNY);

            Collection glSetOfBooks = glSetOfBookHome.findByAcvPeriodPrefixAndDate(BGT_PRD, EJBCommon.getIntendedDate(BGT_YR), AD_CMPNY);
            ArrayList glSetOfBookList = new ArrayList(glSetOfBooks);
            LocalGlSetOfBook glSetOfBook = (LocalGlSetOfBook) glSetOfBookList.get(0);

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodPrefix(glSetOfBook.getGlAccountingCalendar().getAcCode(), BGT_PRD, AD_CMPNY);

            short BGT_PRD_NMBR = 0;

            switch (BGT_AMNT_TYP) {
                case "PTD":

                    BGT_PRD_NMBR = glAccountingCalendarValue.getAcvPeriodNumber();

                    break;
                case "QTD":

                    Collection glQuarterAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCodeAndAcvQuarterNumber(glSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvQuarter(), AD_CMPNY);

                    ArrayList glQuarterAccountingCalendarValueList = new ArrayList(glQuarterAccountingCalendarValues);

                    LocalGlAccountingCalendarValue glQuarterAccountingCalendarValue = (LocalGlAccountingCalendarValue) glQuarterAccountingCalendarValueList.get(0);

                    BGT_PRD_NMBR = glQuarterAccountingCalendarValue.getAcvPeriodNumber();

                    break;
                case "YTD":

                    BGT_PRD_NMBR = 1;
                    break;
            }

            // get budget amount coa lines

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(bc) FROM GlBudgetAmountCoa bc ");

            if (BGT_BO_NM.equals("ALL")) {

                jbossQl.append("WHERE bc.glBudgetAmount.glBudget.bgtName = '").append(BGT_NM).append("' AND bc.bcAdCompany=").append(AD_CMPNY).append(" ");

                LocalGenSegment genSegment = genSegmentHome.findByFlCodeAndSegmentType(genField.getFlCode(), 'N', AD_CMPNY);

                jbossQl.append("ORDER BY bc.glChartOfAccount.coaSegment").append(genSegment.getSgSegmentNumber()).append(", bc.glChartOfAccount.coaAccountNumber ");

            } else {

                jbossQl.append("WHERE bc.glBudgetAmount.glBudget.bgtName = '").append(BGT_NM).append("' AND bc.glBudgetAmount.glBudgetOrganization.boName = '").append(BGT_BO_NM).append("' AND bc.bcAdCompany=").append(AD_CMPNY).append(" ");
            }

            Collection glBudgetAmountCoas = glBudgetAmountHome.getBgaByCriteria(jbossQl.toString(), new Object[0], 0, 0);
            Debug.print("jbossQl.toString()=" + jbossQl);

            Debug.print("DTB_SHW_ZRS=" + DTB_SHW_ZRS);
            if (DTB_SHW_ZRS) {

                for (Object budgetAmountCoa : glBudgetAmountCoas) {

                    LocalGlBudgetAmountCoa glBudgetAmountCoa = (LocalGlBudgetAmountCoa) budgetAmountCoa;

                    GlRepBudgetDetails details = new GlRepBudgetDetails();

                    details.setBgtAccountNumber(glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountNumber());
                    details.setBgtAccountDescription(glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountDescription());
                    details.setBgtDescription(glBudgetAmountCoa.getBcDescription());
                    details.setBgtDate(glAccountingCalendarValue.getAcvDateTo());
                    details.setBgtAccountBalance(0d);
                    details.setBgtRulePercentage1(glBudgetAmountCoa.getBcRulePercentage1());
                    details.setBgtRulePercentage2(glBudgetAmountCoa.getBcRulePercentage2());

                    int BGT_RNNG_PRD = BGT_PRD_NMBR;
                    double BGT_AMNT = 0d;

                    while (BGT_RNNG_PRD <= glAccountingCalendarValue.getAcvPeriodNumber()) {

                        try {

                            LocalGlAccountingCalendarValue glRunningAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glSetOfBook.getGlAccountingCalendar().getAcCode(), (short) BGT_RNNG_PRD, AD_CMPNY);

                            LocalGlBudgetAmountPeriod glBudgetAmountPeriod = glBudgetAmountPeriodHome.findByBcCodeAndAcvCode(glBudgetAmountCoa.getBcCode(), glRunningAccountingCalendarValue.getAcvCode(), AD_CMPNY);

                            BGT_AMNT += glBudgetAmountPeriod.getBapAmount();

                            BGT_RNNG_PRD++;

                        } catch (FinderException ex) {

                            throw new GlRepBGTPeriodOutOfRangeException();
                        }
                    }

                    details.setBgtAmount(BGT_AMNT);

                    list.add(details);
                }
            }

            for (Object budgetAmountCoa : glBudgetAmountCoas) {

                LocalGlBudgetAmountCoa glBudgetAmountCoa = (LocalGlBudgetAmountCoa) budgetAmountCoa;
                Debug.print("COA=" + glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountNumber());

                double COA_BLNC = 0d;

                // get beginning balance

                LocalGlAccountingCalendarValue glBeginningAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glSetOfBook.getGlAccountingCalendar().getAcCode(), BGT_PRD_NMBR, AD_CMPNY);

                LocalGlChartOfAccountBalance glBeginningChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glBeginningAccountingCalendarValue.getAcvCode(), glBudgetAmountCoa.getGlChartOfAccount().getCoaCode(), AD_CMPNY);

                // get ending balance

                LocalGlChartOfAccountBalance glEndingChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glAccountingCalendarValue.getAcvCode(), glBudgetAmountCoa.getGlChartOfAccount().getCoaCode(), AD_CMPNY);

                COA_BLNC = glEndingChartOfAccountBalance.getCoabEndingBalance() - glBeginningChartOfAccountBalance.getCoabBeginningBalance();
                Debug.print("COA_BLNC=" + COA_BLNC);

                // get coa debit or credit balance in unposted journals if necessary

                if (DIS_INCLD_UNPSTD) {

                    Collection glJournalLines = glJournalLineHome.findUnpostedJlByJrEffectiveDateRangeAndCoaCode(glBeginningAccountingCalendarValue.getAcvDateFrom(), glAccountingCalendarValue.getAcvDateTo(), glBudgetAmountCoa.getGlChartOfAccount().getCoaCode(), AD_CMPNY);
                    Debug.print("glJournalLines=" + glJournalLines.size());

                    for (Object journalLine : glJournalLines) {

                        LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                        LocalGlJournal glJournal = glJournalLine.getGlJournal();

                        double JL_AMNT = this.convertForeignToFunctionalCurrency(glJournal.getGlFunctionalCurrency().getFcCode(), glJournal.getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), glJournal.getJrConversionRate(), glJournalLine.getJlAmount(), AD_CMPNY);

                        if (((glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("ASSET") || glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("EXPENSE")) && glJournalLine.getJlDebit() == EJBCommon.TRUE) || (!glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("ASSET") && !glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("EXPENSE") && glJournalLine.getJlDebit() == EJBCommon.FALSE)) {

                            COA_BLNC += EJBCommon.roundIt(JL_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision());

                        } else {

                            COA_BLNC -= EJBCommon.roundIt(JL_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
                        }

                        Debug.print("1DIS_INCLD_UNPSTD=" + DIS_INCLD_UNPSTD);
                        Debug.print("glJournal=" + glJournal.getJrDocumentNumber());
                        Debug.print("JL_AMNT=" + JL_AMNT);

                        GlRepBudgetDetails details = new GlRepBudgetDetails();

                        details.setBgtAccountNumber(glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountNumber());
                        details.setBgtAccountDescription(glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountDescription());
                        details.setBgtDescription(glBudgetAmountCoa.getBcDescription());
                        details.setBgtDocumentNumber(glJournal.getJrDocumentNumber());
                        details.setBgtDate(glJournal.getJrEffectiveDate());
                        details.setBgtAccountBalance(JL_AMNT);
                        details.setBgtRulePercentage1(glBudgetAmountCoa.getBcRulePercentage1());
                        details.setBgtRulePercentage2(glBudgetAmountCoa.getBcRulePercentage2());

                        int BGT_RNNG_PRD = BGT_PRD_NMBR;
                        double BGT_AMNT = 0d;

                        while (BGT_RNNG_PRD <= glAccountingCalendarValue.getAcvPeriodNumber()) {

                            try {

                                LocalGlAccountingCalendarValue glRunningAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glSetOfBook.getGlAccountingCalendar().getAcCode(), (short) BGT_RNNG_PRD, AD_CMPNY);

                                LocalGlBudgetAmountPeriod glBudgetAmountPeriod = glBudgetAmountPeriodHome.findByBcCodeAndAcvCode(glBudgetAmountCoa.getBcCode(), glRunningAccountingCalendarValue.getAcvCode(), AD_CMPNY);

                                BGT_AMNT += glBudgetAmountPeriod.getBapAmount();

                                BGT_RNNG_PRD++;

                            } catch (FinderException ex) {

                                throw new GlRepBGTPeriodOutOfRangeException();
                            }
                        }

                        details.setBgtAmount(BGT_AMNT);

                        list.add(details);
                    }
                }

                // get coa debit or credit balance

                if (!DIS_INCLD_UNPSTD) {

                    Collection glJournalLines = glJournalLineHome.findPostedJlByJrEffectiveDateRangeAndCoaCode(glBeginningAccountingCalendarValue.getAcvDateFrom(), glAccountingCalendarValue.getAcvDateTo(), glBudgetAmountCoa.getGlChartOfAccount().getCoaCode(), AD_CMPNY);

                    for (Object journalLine : glJournalLines) {

                        LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                        LocalGlJournal glJournal = glJournalLine.getGlJournal();

                        double JL_AMNT = this.convertForeignToFunctionalCurrency(glJournal.getGlFunctionalCurrency().getFcCode(), glJournal.getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), glJournal.getJrConversionRate(), glJournalLine.getJlAmount(), AD_CMPNY);

                        if (((glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("ASSET") || glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("EXPENSE")) && glJournalLine.getJlDebit() == EJBCommon.TRUE) || (!glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("ASSET") && !glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("EXPENSE") && glJournalLine.getJlDebit() == EJBCommon.FALSE)) {

                            COA_BLNC += EJBCommon.roundIt(JL_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision());

                        } else {

                            COA_BLNC -= EJBCommon.roundIt(JL_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
                        }

                        Debug.print("2DIS_INCLD_UNPSTD=" + DIS_INCLD_UNPSTD);
                        Debug.print("glJournal=" + glJournal.getJrDocumentNumber());
                        Debug.print("JL_AMNT=" + JL_AMNT);

                        GlRepBudgetDetails details = new GlRepBudgetDetails();

                        details.setBgtAccountNumber(glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountNumber());
                        details.setBgtAccountDescription(glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountDescription());
                        details.setBgtDescription(glBudgetAmountCoa.getBcDescription());
                        details.setBgtDocumentNumber(glJournal.getJrDocumentNumber());
                        details.setBgtDate(glJournal.getJrEffectiveDate());
                        details.setBgtAccountBalance(JL_AMNT);
                        details.setBgtRulePercentage1(glBudgetAmountCoa.getBcRulePercentage1());
                        details.setBgtRulePercentage2(glBudgetAmountCoa.getBcRulePercentage2());

                        int BGT_RNNG_PRD = BGT_PRD_NMBR;
                        double BGT_AMNT = 0d;

                        while (BGT_RNNG_PRD <= glAccountingCalendarValue.getAcvPeriodNumber()) {

                            try {

                                LocalGlAccountingCalendarValue glRunningAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glSetOfBook.getGlAccountingCalendar().getAcCode(), (short) BGT_RNNG_PRD, AD_CMPNY);

                                LocalGlBudgetAmountPeriod glBudgetAmountPeriod = glBudgetAmountPeriodHome.findByBcCodeAndAcvCode(glBudgetAmountCoa.getBcCode(), glRunningAccountingCalendarValue.getAcvCode(), AD_CMPNY);

                                BGT_AMNT += glBudgetAmountPeriod.getBapAmount();

                                BGT_RNNG_PRD++;

                            } catch (FinderException ex) {

                                throw new GlRepBGTPeriodOutOfRangeException();
                            }
                        }

                        details.setBgtAmount(BGT_AMNT);

                        list.add(details);
                    }
                }

                Collection apVOUDrs = null;
                Collection apDMDrs = null;
                Collection apCHKDrs = null;

                // include unposted subledger transactions

                if (DIS_INCLD_UNPSTD_SL) {

                    apVOUDrs = apDistributionRecordHome.findUnpostedVouByDateRangeAndCoaAccountNumber(EJBCommon.FALSE, glBeginningAccountingCalendarValue.getAcvDateFrom(), glAccountingCalendarValue.getAcvDateTo(), glBudgetAmountCoa.getGlChartOfAccount().getCoaCode(), AD_BRNCH, AD_CMPNY);
                    Debug.print("apVOUDrs=" + apVOUDrs.size());
                    Iterator j = apVOUDrs.iterator();

                    while (j.hasNext()) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                        LocalApVoucher apVoucher = apDistributionRecord.getApVoucher();

                        double JL_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                        if (((glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("ASSET") || glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("EXPENSE")) && apDistributionRecord.getDrDebit() == EJBCommon.TRUE) || (!glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("ASSET") && !glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("EXPENSE") && apDistributionRecord.getDrDebit() == EJBCommon.FALSE)) {

                            COA_BLNC += JL_AMNT;

                        } else {

                            COA_BLNC -= JL_AMNT;
                        }

                        GlRepBudgetDetails details = new GlRepBudgetDetails();

                        details.setBgtAccountNumber(glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountNumber());
                        details.setBgtAccountDescription(glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountDescription());
                        details.setBgtDescription(glBudgetAmountCoa.getBcDescription());
                        details.setBgtDocumentNumber(apVoucher.getVouDocumentNumber());
                        details.setBgtDate(apVoucher.getVouDate());
                        details.setBgtAccountBalance(JL_AMNT);
                        details.setBgtRulePercentage1(glBudgetAmountCoa.getBcRulePercentage1());
                        details.setBgtRulePercentage2(glBudgetAmountCoa.getBcRulePercentage2());

                        int BGT_RNNG_PRD = BGT_PRD_NMBR;
                        double BGT_AMNT = 0d;

                        while (BGT_RNNG_PRD <= glAccountingCalendarValue.getAcvPeriodNumber()) {

                            try {

                                LocalGlAccountingCalendarValue glRunningAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glSetOfBook.getGlAccountingCalendar().getAcCode(), (short) BGT_RNNG_PRD, AD_CMPNY);

                                LocalGlBudgetAmountPeriod glBudgetAmountPeriod = glBudgetAmountPeriodHome.findByBcCodeAndAcvCode(glBudgetAmountCoa.getBcCode(), glRunningAccountingCalendarValue.getAcvCode(), AD_CMPNY);

                                BGT_AMNT += glBudgetAmountPeriod.getBapAmount();

                                BGT_RNNG_PRD++;

                            } catch (FinderException ex) {

                                throw new GlRepBGTPeriodOutOfRangeException();
                            }
                        }

                        details.setBgtAmount(BGT_AMNT);

                        list.add(details);
                    }

                    apDMDrs = apDistributionRecordHome.findUnpostedVouByDateRangeAndCoaAccountNumber(EJBCommon.TRUE, glBeginningAccountingCalendarValue.getAcvDateFrom(), glAccountingCalendarValue.getAcvDateTo(), glBudgetAmountCoa.getGlChartOfAccount().getCoaCode(), AD_BRNCH, AD_CMPNY);
                    Debug.print("apDMDrs=" + apDMDrs.size());
                    j = apDMDrs.iterator();

                    while (j.hasNext()) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                        LocalApVoucher apDebitMemo = apDistributionRecord.getApVoucher();

                        LocalApVoucher apVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(apDebitMemo.getVouDmVoucherNumber(), EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

                        double JL_AMNT = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                        if (((glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("ASSET") || glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("EXPENSE")) && apDistributionRecord.getDrDebit() == EJBCommon.TRUE) || (!glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("ASSET") && !glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("EXPENSE") && apDistributionRecord.getDrDebit() == EJBCommon.FALSE)) {

                            COA_BLNC += JL_AMNT;

                        } else {

                            COA_BLNC -= JL_AMNT;
                        }

                        GlRepBudgetDetails details = new GlRepBudgetDetails();

                        details.setBgtAccountNumber(glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountNumber());
                        details.setBgtAccountDescription(glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountDescription());
                        details.setBgtDescription(glBudgetAmountCoa.getBcDescription());
                        details.setBgtDocumentNumber(apVoucher.getVouDmVoucherNumber());
                        details.setBgtDate(apVoucher.getVouDate());
                        details.setBgtAccountBalance(JL_AMNT);
                        details.setBgtRulePercentage1(glBudgetAmountCoa.getBcRulePercentage1());
                        details.setBgtRulePercentage2(glBudgetAmountCoa.getBcRulePercentage2());

                        int BGT_RNNG_PRD = BGT_PRD_NMBR;
                        double BGT_AMNT = 0d;

                        while (BGT_RNNG_PRD <= glAccountingCalendarValue.getAcvPeriodNumber()) {

                            try {

                                LocalGlAccountingCalendarValue glRunningAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glSetOfBook.getGlAccountingCalendar().getAcCode(), (short) BGT_RNNG_PRD, AD_CMPNY);

                                LocalGlBudgetAmountPeriod glBudgetAmountPeriod = glBudgetAmountPeriodHome.findByBcCodeAndAcvCode(glBudgetAmountCoa.getBcCode(), glRunningAccountingCalendarValue.getAcvCode(), AD_CMPNY);

                                BGT_AMNT += glBudgetAmountPeriod.getBapAmount();

                                BGT_RNNG_PRD++;

                            } catch (FinderException ex) {

                                throw new GlRepBGTPeriodOutOfRangeException();
                            }
                        }

                        details.setBgtAmount(BGT_AMNT);

                        list.add(details);
                    }

                    apCHKDrs = apDistributionRecordHome.findUnpostedChkByDateRangeAndCoaAccountNumber(glBeginningAccountingCalendarValue.getAcvDateFrom(), glAccountingCalendarValue.getAcvDateTo(), glBudgetAmountCoa.getGlChartOfAccount().getCoaCode(), AD_BRNCH, AD_CMPNY);
                    Debug.print("glBeginningAccountingCalendarValue.getAcvDateFrom()=" + glBeginningAccountingCalendarValue.getAcvDateFrom());
                    Debug.print("glBeginningAccountingCalendarValue.getAcvDateTo()=" + glBeginningAccountingCalendarValue.getAcvDateTo());
                    Debug.print("glBudgetAmountCoa.getGlChartOfAccount().getCoaCode()=" + glBudgetAmountCoa.getGlChartOfAccount().getCoaCode());
                    Debug.print("apCHKDrs=" + apCHKDrs.size());
                    j = apCHKDrs.iterator();

                    while (j.hasNext()) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                        LocalApCheck apCheck = apDistributionRecord.getApCheck();

                        double JL_AMNT = this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);
                        Debug.print(apDistributionRecord.getApCheck().getChkDocumentNumber());

                        if (((glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("ASSET") || glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("EXPENSE")) && apDistributionRecord.getDrDebit() == EJBCommon.TRUE) || (!glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("ASSET") && !glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountType().equals("EXPENSE") && apDistributionRecord.getDrDebit() == EJBCommon.FALSE)) {

                            COA_BLNC += JL_AMNT;

                        } else {

                            COA_BLNC -= JL_AMNT;
                        }

                        Debug.print("JL_AMNT=" + JL_AMNT);

                        GlRepBudgetDetails details = new GlRepBudgetDetails();

                        details.setBgtAccountNumber(glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountNumber());
                        details.setBgtAccountDescription(glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountDescription());
                        details.setBgtDescription(glBudgetAmountCoa.getBcDescription());
                        details.setBgtDocumentNumber(apCheck.getChkDocumentNumber());
                        details.setBgtDate(apCheck.getChkDate());
                        details.setBgtAccountBalance(JL_AMNT);
                        details.setBgtRulePercentage1(glBudgetAmountCoa.getBcRulePercentage1());
                        details.setBgtRulePercentage2(glBudgetAmountCoa.getBcRulePercentage2());

                        int BGT_RNNG_PRD = BGT_PRD_NMBR;
                        double BGT_AMNT = 0d;

                        while (BGT_RNNG_PRD <= glAccountingCalendarValue.getAcvPeriodNumber()) {

                            try {

                                LocalGlAccountingCalendarValue glRunningAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glSetOfBook.getGlAccountingCalendar().getAcCode(), (short) BGT_RNNG_PRD, AD_CMPNY);

                                LocalGlBudgetAmountPeriod glBudgetAmountPeriod = glBudgetAmountPeriodHome.findByBcCodeAndAcvCode(glBudgetAmountCoa.getBcCode(), glRunningAccountingCalendarValue.getAcvCode(), AD_CMPNY);

                                BGT_AMNT += glBudgetAmountPeriod.getBapAmount();

                                BGT_RNNG_PRD++;

                            } catch (FinderException ex) {

                                throw new GlRepBGTPeriodOutOfRangeException();
                            }
                        }

                        details.setBgtAmount(BGT_AMNT);

                        list.add(details);
                    }
                }

                list.sort(GlRepBudgetDetails.AccountNumberComparator);
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        } catch (GlobalNoRecordFoundException | GlRepBGTPeriodOutOfRangeException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }


    public void executeSpGlRepBudget(String STORED_PROCEEDURE, String BUDGET_ORGANIZATION, String BUDGET_NAME, Date DT_FRM, Date DR_TO, boolean INCLUDE_UNPOSTED, boolean INCLUDE_UNPOSTED_SL, boolean SHOW_ZEROES, String branchCodes, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlRepGeneralLedgerControllerBean executeSpGlRepGeneralLedger");


        try {

            StoredProcedureQuery spQuery = em.createStoredProcedureQuery(STORED_PROCEEDURE).registerStoredProcedureParameter("BUDGET_ORGANIZATION", String.class, ParameterMode.IN).registerStoredProcedureParameter("BUDGET_NAME", String.class, ParameterMode.IN).registerStoredProcedureParameter("DATE_FROM", Date.class, ParameterMode.IN).registerStoredProcedureParameter("DATE_TO", Date.class, ParameterMode.IN).registerStoredProcedureParameter("INCLUDE_UNPOSTED", Boolean.class, ParameterMode.IN).registerStoredProcedureParameter("INCLUDE_UNPOSTED_SL", Boolean.class, ParameterMode.IN).registerStoredProcedureParameter("SHOW_ZEROES", Boolean.class, ParameterMode.IN).registerStoredProcedureParameter("BRANCH_CODE", String.class, ParameterMode.IN).registerStoredProcedureParameter("AD_CMPNY", Integer.class, ParameterMode.IN).registerStoredProcedureParameter("RESULT_COUNT", Integer.class, ParameterMode.OUT);

            spQuery.setParameter("BUDGET_ORGANIZATION", BUDGET_ORGANIZATION);
            spQuery.setParameter("BUDGET_NAME", BUDGET_NAME);
            spQuery.setParameter("DATE_FROM", DT_FRM);
            spQuery.setParameter("DATE_TO", DR_TO);
            spQuery.setParameter("INCLUDE_UNPOSTED", INCLUDE_UNPOSTED);
            spQuery.setParameter("INCLUDE_UNPOSTED_SL", INCLUDE_UNPOSTED_SL);
            spQuery.setParameter("SHOW_ZEROES", SHOW_ZEROES);
            spQuery.setParameter("BRANCH_CODE", branchCodes);
            spQuery.setParameter("AD_CMPNY", AD_CMPNY);


            spQuery.execute();

            Integer rowsCount = (Integer) spQuery.getOutputParameterValue("RESULT_COUNT");

            // IF NO RESULT FOUND
            if (rowsCount <= 0) {
                throw new GlobalNoRecordFoundException();
            }
        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }


    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("GlRepBudgetControllerBean getAdCompany");

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

    // private methods

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("GlRepBudgetControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT * CONVERSION_RATE;

        } else if (CONVERSION_DATE != null) {

            try {

                // Get functional currency rate

                LocalGlFunctionalCurrencyRate glReceiptFunctionalCurrencyRate = null;

                if (!FC_NM.equals("USD")) {

                    glReceiptFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(FC_CODE, CONVERSION_DATE, AD_CMPNY);

                    AMOUNT = AMOUNT * glReceiptFunctionalCurrencyRate.getFrXToUsd();
                }

                // Get set of book functional currency rate if necessary

                if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, AD_CMPNY);

                    AMOUNT = AMOUNT / glCompanyFunctionalCurrencyRate.getFrXToUsd();
                }

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }
        }

        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlRepBudgetControllerBean getAdBrResAll");

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

        Debug.print("GlRepBudgetControllerBean ejbCreate");
    }
}