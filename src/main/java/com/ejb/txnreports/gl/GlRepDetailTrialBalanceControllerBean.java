package com.ejb.txnreports.gl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountBalanceHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlChartOfAccountBalance;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.util.ad.AdCompanyDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "GlRepDetailTrialBalanceControllerEJB")
public class GlRepDetailTrialBalanceControllerBean extends EJBContextClass implements GlRepDetailTrialBalanceController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    LocalGenSegmentHome genSegmentHome;
    @EJB
    LocalGlJournalLineHome glJournalLineHome;
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

    public void executeSpGlRepDetailTrialBalance(String STORED_PROCEDURE, String DTB_ACCNT_NMBR_FRM, String DTB_ACCNT_NMBR_TO, Date DT_FRM, Date DT_TO, boolean DTB_INCLD_UNPSTD, boolean DTB_INCLD_UNPSTD_SL, boolean DTB_SHW_ZRS, String AMOUNT_TYP, String BRANCH_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlRepDetailTrialBalanceControllerBean executeSpGlRepDetailTrialBalance");

        try {

            Debug.print("STORED_PROCEDURE=" + STORED_PROCEDURE);

            StoredProcedureQuery spQuery = em.createStoredProcedureQuery(STORED_PROCEDURE).registerStoredProcedureParameter("accountFrom", String.class, ParameterMode.IN).registerStoredProcedureParameter("accountTo", String.class, ParameterMode.IN).registerStoredProcedureParameter("dateFrom", Date.class, ParameterMode.IN).registerStoredProcedureParameter("dateTo", Date.class, ParameterMode.IN).registerStoredProcedureParameter("includeUnposted", Boolean.class, ParameterMode.IN).registerStoredProcedureParameter("includeUnpostedSl", Boolean.class, ParameterMode.IN).registerStoredProcedureParameter("showZeroes", Boolean.class, ParameterMode.IN).registerStoredProcedureParameter("amountType", String.class, ParameterMode.IN).registerStoredProcedureParameter("branchCode", String.class, ParameterMode.IN).registerStoredProcedureParameter("adCompany", Integer.class, ParameterMode.IN).registerStoredProcedureParameter("resultCount", Integer.class, ParameterMode.OUT);

            spQuery.setParameter("accountFrom", DTB_ACCNT_NMBR_FRM);
            spQuery.setParameter("accountTo", DTB_ACCNT_NMBR_TO);
            spQuery.setParameter("dateFrom", new java.sql.Date(DT_FRM.getTime()));
            spQuery.setParameter("dateTo", new java.sql.Date(DT_TO.getTime()));
            spQuery.setParameter("includeUnposted", DTB_INCLD_UNPSTD);
            spQuery.setParameter("includeUnpostedSl", DTB_INCLD_UNPSTD_SL);
            spQuery.setParameter("showZeroes", DTB_SHW_ZRS);
            spQuery.setParameter("amountType", AMOUNT_TYP);
            spQuery.setParameter("branchCode", BRANCH_CODE);
            spQuery.setParameter("adCompany", AD_CMPNY);

            spQuery.execute();

            Integer resultCount = (Integer) spQuery.getOutputParameterValue("resultCount");
            if (resultCount <= 0) {
                throw new GlobalNoRecordFoundException();
            }

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

    }

    public void executeGlRecomputeCoaBalance(String GL_ACCNT_NMBR_FRM, String GL_ACCNT_NMBR_TO, Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlobalAccountNumberInvalidException {

        Debug.print("GlRepGeneralLedgerControllerBean executeGlRecomputeCoaBalance");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGenField genField = adCompany.getGenField();
            Collection genSegments = genSegmentHome.findByFlCode(genField.getFlCode(), AD_CMPNY);
            String strSeparator = String.valueOf(genField.getFlSegmentSeparator());
            int genNumberOfSegment = genField.getFlNumberOfSegment();

            StringTokenizer stAccountFrom = new StringTokenizer(GL_ACCNT_NMBR_FRM, strSeparator);
            StringTokenizer stAccountTo = new StringTokenizer(GL_ACCNT_NMBR_TO, strSeparator);

            if (stAccountFrom.countTokens() != genNumberOfSegment || stAccountTo.countTokens() != genNumberOfSegment) {

                throw new GlobalAccountNumberInvalidException();
            }

            Collection journalLines = glJournalLineHome.findPostedJlByJrEffectiveDateAndCoaAccountNumberRange(GL_ACCNT_NMBR_FRM, GL_ACCNT_NMBR_TO, AD_CMPNY);

            for (Object journalLine : journalLines) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                String ACCNT_NMBR = glJournalLine.getGlChartOfAccount().getCoaAccountNumber();
                String ACCNT_TYP = glJournalLine.getGlChartOfAccount().getCoaAccountType();
                Date TRANS_DATE = glJournalLine.getGlJournal().getJrEffectiveDate();
                Integer AD_BRNCH = glJournalLine.getGlJournal().getJrAdBranch();

                byte DBT = glJournalLine.getJlDebit();
                double AMOUNT = glJournalLine.getJlAmount();

                // validate if period is closed

                LocalGlSetOfBook glSetOfBook = glSetOfBookHome.findByDate(TRANS_DATE, AD_CMPNY);

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glSetOfBook.getGlAccountingCalendar().getAcCode(), TRANS_DATE, AD_CMPNY);

                LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(ACCNT_NMBR, AD_CMPNY);

                // post current to current acv

                this.post(glAccountingCalendarValue, glChartOfAccount, true, DBT, AMOUNT, AD_CMPNY);

                // post to subsequent acvs (propagate)

                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), AD_CMPNY);

                for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                    this.post(glSubsequentAccountingCalendarValue, glChartOfAccount, false, DBT, AMOUNT, AD_CMPNY);
                }

                // post to subsequent years if necessary

                Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glSetOfBook.getGlAccountingCalendar().getAcYear(), AD_CMPNY);

                if (!glSubsequentSetOfBooks.isEmpty() && glSetOfBook.getSobYearEndClosed() == 1) {

                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), AD_CMPNY);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), AD_CMPNY);

                        for (Object accountingCalendarValue : glAccountingCalendarValues) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                            if (ACCNT_TYP.equals("ASSET") || ACCNT_TYP.equals("LIABILITY") || ACCNT_TYP.equals("OWNERS EQUITY")) {

                                this.post(glSubsequentAccountingCalendarValue, glChartOfAccount, false, DBT, AMOUNT, AD_CMPNY);

                            } else { // revenue & expense

                                this.post(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, DBT, AMOUNT, AD_CMPNY);
                            }
                        }

                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) break;
                    }
                }
            }

        } catch (GlobalAccountNumberInvalidException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void executeSpGlRecomputeCoaBalance(ResultSet rs, String GL_ACCNT_NMBR_FRM, String GL_ACCNT_NMBR_TO, Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlobalAccountNumberInvalidException {

        Debug.print("GlRepGeneralLedgerControllerBean executeSpGlRecomputeCoaBalance");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGenField genField = adCompany.getGenField();
            Collection genSegments = genSegmentHome.findByFlCode(genField.getFlCode(), AD_CMPNY);
            String strSeparator = String.valueOf(genField.getFlSegmentSeparator());
            int genNumberOfSegment = genField.getFlNumberOfSegment();

            StringTokenizer stAccountFrom = new StringTokenizer(GL_ACCNT_NMBR_FRM, strSeparator);
            StringTokenizer stAccountTo = new StringTokenizer(GL_ACCNT_NMBR_TO, strSeparator);

            if (stAccountFrom.countTokens() != genNumberOfSegment || stAccountTo.countTokens() != genNumberOfSegment) {

                throw new GlobalAccountNumberInvalidException();
            }


            while (rs.next()) {

                String ACCNT_NMBR = rs.getString("ACCNT_NMBR");
                String ACCNT_TYP = rs.getString("ACCNT_TYP");
                Date TRANS_DATE = rs.getDate("DATE");
                byte DEBIT = rs.getByte("DEBIT");
                double AMOUNT = rs.getDouble("AMOUNT");

                // validate if period is closed

                LocalGlSetOfBook glSetOfBook = glSetOfBookHome.findByDate(TRANS_DATE, AD_CMPNY);

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glSetOfBook.getGlAccountingCalendar().getAcCode(), TRANS_DATE, AD_CMPNY);

                LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(ACCNT_NMBR, AD_CMPNY);

                // post current to current acv

                this.post(glAccountingCalendarValue, glChartOfAccount, true, DEBIT, AMOUNT, AD_CMPNY);

                // post to subsequent acvs (propagate)

                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), AD_CMPNY);

                for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                    this.post(glSubsequentAccountingCalendarValue, glChartOfAccount, false, DEBIT, AMOUNT, AD_CMPNY);
                }

                // post to subsequent years if necessary

                Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glSetOfBook.getGlAccountingCalendar().getAcYear(), AD_CMPNY);

                if (!glSubsequentSetOfBooks.isEmpty() && glSetOfBook.getSobYearEndClosed() == 1) {

                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), AD_CMPNY);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), AD_CMPNY);

                        for (Object accountingCalendarValue : glAccountingCalendarValues) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                            if (ACCNT_TYP.equals("ASSET") || ACCNT_TYP.equals("LIABILITY") || ACCNT_TYP.equals("OWNERS EQUITY")) {

                                this.post(glSubsequentAccountingCalendarValue, glChartOfAccount, false, DEBIT, AMOUNT, AD_CMPNY);

                            } else { // revenue & expense

                                this.post(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, DEBIT, AMOUNT, AD_CMPNY);
                            }
                        }

                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) break;
                    }
                }
            }

        } catch (GlobalAccountNumberInvalidException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("GlRepDetailTrialBalanceControllerBean getAdCompany");

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

    public byte getPrfEnableGlRecomputeCoaBalance(Integer AD_CMPNY) {

        Debug.print("GlRepDetailTrialBalanceControllerBean getPrfEnableGlRecomputeCoaBalance");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfEnableGlRecomputeCoaBalance();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void post(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer AD_CMPNY) {

        Debug.print("GlRepDetailTrialBalanceControllerBean post");

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

    // SessionBean method
    public void ejbCreate() throws CreateException {

        Debug.print("GlRepDetailTrialBalanceControllerBean ejbCreate");
    }
}