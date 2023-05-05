/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlForexRevaluationControllerBean
 * @created May 15, 2006, 6:26 PM
 * @author Farrah S. Garing
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
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdBranchDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdBranchDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.exception.gl.GlCOANoChartOfAccountFoundException;
import com.ejb.exception.gl.GlFCFunctionalCurrencyAlreadyAssignedException;
import com.ejb.exception.gl.GlFCNoFunctionalCurrencyFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.gl.GlJREffectiveDateViolationException;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordInvalidException;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gen.LocalGenSegment;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlForexLedger;
import com.ejb.dao.gl.LocalGlForexLedgerHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
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
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gen.GenModSegmentDetails;
import com.util.mod.gl.GlModAccountingCalendarValueDetails;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;

@Stateless(name = "GlForexRevaluationControllerEJB")
public class GlForexRevaluationControllerBean extends EJBContextClass implements GlForexRevaluationController {

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
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalGlForexLedgerHome glForexLedgerHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
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

    public ArrayList getGlFcAllWithDefault(Integer AD_CMPNY) {

        Debug.print("GlForexRevaluationControllerBean getGlFcAllWithDefault");

        LocalAdCompany adCompany = null;

        Collection glFunctionalCurrencies = null;

        LocalGlFunctionalCurrency glFunctionalCurrency = null;

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

    public ArrayList getGlAcAllEditableOpenAndFutureEntry(Integer AD_CMPNY) {

        Debug.print("GlForexRevaluationControllerBean getGlAcAllEditableOpenAndFutureEntry");

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

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GlForexRevaluationControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGenSgAll(Integer AD_CMPNY) {

        Debug.print("GlRepDetailIncomeStatementControllerBean getGenSgAll");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            Collection genSegments = genSegmentHome.findByFlCode(adCompany.getGenField().getFlCode(), AD_CMPNY);

            for (Object segment : genSegments) {

                LocalGenSegment genSegment = (LocalGenSegment) segment;

                GenModSegmentDetails mdetails = new GenModSegmentDetails();
                mdetails.setSgMaxSize(genSegment.getSgMaxSize());
                mdetails.setSgFlSegmentSeparator(genSegment.getGenField().getFlSegmentSeparator());

                list.add(mdetails);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void executeForexRevaluation(HashMap criteria, double CONVERSION_RATE, String UNRLZD_GN_LSS_ACCNT, int YR, String PRD_NM, String USR_NM, String JR_DCMNT_NMBR, Integer AD_BRNCH, Integer AD_CMPNY) throws GlJREffectiveDatePeriodClosedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDateViolationException, GlobalDocumentNumberNotUniqueException, GlFCNoFunctionalCurrencyFoundException, GlFCFunctionalCurrencyAlreadyAssignedException, GlCOANoChartOfAccountFoundException, GlobalAccountNumberInvalidException, GlobalRecordInvalidException, GlobalNoRecordFoundException {

        Debug.print("GlForexRevaluationControllerBean executeForexRevaluation");

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSSS");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            Date JR_EFFCTV_DT = null;

            // validate if period exists and open

            LocalGlSetOfBook glSetOfBook = null;

            try {

                // get selected set of book

                Collection glSetOfBooks = glSetOfBookHome.findByAcvPeriodPrefixAndDate(PRD_NM, EJBCommon.getIntendedDate(YR), AD_CMPNY);
                ArrayList glSetOfBookList = new ArrayList(glSetOfBooks);

                glSetOfBook = (LocalGlSetOfBook) glSetOfBookList.get(0);

                // get accounting calendar value

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodPrefix(glSetOfBook.getGlAccountingCalendar().getAcCode(), PRD_NM, AD_CMPNY);

                if (glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P' || glAccountingCalendarValue.getAcvStatus() == 'N') {

                    throw new GlJREffectiveDatePeriodClosedException(JR_DCMNT_NMBR);
                }

                JR_EFFCTV_DT = glAccountingCalendarValue.getAcvDateTo();

            } catch (FinderException ex) {

                throw new GlJREffectiveDateNoPeriodExistException(JR_DCMNT_NMBR);
            }

            // validate if document number is unique

            try {

                LocalGlJournal glExistingJournal = glJournalHome.findByJrDocumentNumberAndJsNameAndBrCode(JR_DCMNT_NMBR, "MANUAL", AD_BRNCH, AD_CMPNY);

                throw new GlobalDocumentNumberNotUniqueException(JR_DCMNT_NMBR);

            } catch (FinderException ex) {
            }

            // generate document number if necessary
            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;

            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("GL JOURNAL", AD_CMPNY);

            try {

                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

            } catch (FinderException ex) {

            }

            if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'M' && (JR_DCMNT_NMBR == null || JR_DCMNT_NMBR.trim().length() == 0)) {

                throw new GlobalRecordInvalidException();

            } else if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (JR_DCMNT_NMBR == null || JR_DCMNT_NMBR.trim().length() == 0)) {

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

            LocalGenField genField = adCompany.getGenField();
            String strSeparator = String.valueOf(genField.getFlSegmentSeparator());
            int genNumberOfSegment = genField.getFlNumberOfSegment();

            short ctr = 0;

            StringTokenizer stAccountFrom = new StringTokenizer((String) criteria.get("accountNumberFrom"), strSeparator);
            StringTokenizer stAccountTo = new StringTokenizer((String) criteria.get("accountNumberTo"), strSeparator);

            Object[] obj = new Object[criteria.size() - 2];

            // get COA selected

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT DISTINCT OBJECT(coa) FROM GlChartOfAccount coa WHERE ");

            // validate if account number is valid

            if (stAccountFrom.countTokens() != genNumberOfSegment || stAccountTo.countTokens() != genNumberOfSegment) {

                throw new GlobalAccountNumberInvalidException();
            }

            try {

                StringBuilder var = new StringBuilder("1");

                if (genNumberOfSegment > 1) {

                    for (int i = 0; i < genNumberOfSegment; i++) {

                        if (i == 0 && i < genNumberOfSegment - 1) {

                            // for first segment

                            jbossQl.append("SUBSTRING(coa.coaAccountNumber, 1, LOCATE('").append(strSeparator).append("', coa.coaAccountNumber, 1)-1) BETWEEN '").append(stAccountFrom.nextToken()).append("' AND '").append(stAccountTo.nextToken()).append("' AND ");

                            var = new StringBuilder("LOCATE('" + strSeparator + "', coa.coaAccountNumber, 1)+1 ");

                        } else if (i != 0 && i < genNumberOfSegment - 1) {

                            // for middle segments

                            jbossQl.append("SUBSTRING(coa.coaAccountNumber, ").append(var).append(", (LOCATE('").append(strSeparator).append("', coa.coaAccountNumber, ").append(var).append(")) - (").append(var).append(")) BETWEEN '").append(stAccountFrom.nextToken()).append("' AND '").append(stAccountTo.nextToken()).append("' AND ");

                            var = new StringBuilder("LOCATE('" + strSeparator + "', coa.coaAccountNumber, " + var + ")+1 ");

                        } else if (i != 0 && i == genNumberOfSegment - 1) {

                            // for last segment

                            jbossQl.append("SUBSTRING(coa.coaAccountNumber, ").append(var).append(", (LENGTH(coa.coaAccountNumber)+1) - (").append(var).append(")) BETWEEN '").append(stAccountFrom.nextToken()).append("' AND '").append(stAccountTo.nextToken()).append("' ");
                        }
                    }

                } else if (genNumberOfSegment == 1) {

                    String accountFrom = stAccountFrom.nextToken();
                    String accountTo = stAccountTo.nextToken();

                    jbossQl.append("SUBSTRING(coa.coaAccountNumber, 1, LOCATE('").append(strSeparator).append("', coa.coaAccountNumber, 1)-1) BETWEEN '").append(accountFrom).append("' AND '").append(accountTo).append("' OR ").append("coa.coaAccountNumber BETWEEN '").append(accountFrom).append("' AND '").append(accountTo).append("' ");
                }

                String GL_FUNCTIONAL_CURRENCY = (String) criteria.get("functionalCurrency");

                if (GL_FUNCTIONAL_CURRENCY.length() > 0 && GL_FUNCTIONAL_CURRENCY != null && (!GL_FUNCTIONAL_CURRENCY.equalsIgnoreCase("NO RECORD FOUND"))) {

                    LocalGlFunctionalCurrency glFunctionalCurrency = null;

                    try {

                        glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(GL_FUNCTIONAL_CURRENCY, AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new GlFCNoFunctionalCurrencyFoundException();
                    }
                    // check if FC selected is default company FC
                    if (Objects.equals(adCompany.getGlFunctionalCurrency().getFcCode(), glFunctionalCurrency.getFcCode()))
                        throw new GlFCFunctionalCurrencyAlreadyAssignedException();

                    jbossQl.append(" AND coa.glFunctionalCurrency.fcCode=?").append(ctr + 1).append(" ");
                    obj[ctr] = glFunctionalCurrency.getFcCode();
                    ctr++;
                }

                jbossQl.append("AND coa.coaEnable=1 AND coa.coaAdCompany=").append(AD_CMPNY).append(" ");

            } catch (NumberFormatException ex) {

                throw new GlobalAccountNumberInvalidException();
            }

            Collection glChartOfAccounts = glChartOfAccountHome.getCoaByCriteria(jbossQl.toString(), obj, 0, 0);

            if (glChartOfAccounts == null || glChartOfAccounts.isEmpty() || glChartOfAccounts.size() == 0) {

                throw new GlobalNoRecordFoundException();
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            // create jounal batch

            // create jounal
            LocalGlJournal glJournal = glJournalHome.create("REVALUATION " + formatter.format(new java.util.Date()), "REVALUATION " + formatter.format(new java.util.Date()), JR_EFFCTV_DT, 0d, null, JR_DCMNT_NMBR, null, 1d, null, null, 'N', EJBCommon.FALSE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, null, null, null, null, EJBCommon.FALSE, null, AD_BRNCH, AD_CMPNY);

            try {

                LocalGlJournalBatch glJournalBatch = adPreference.getPrfEnableGlJournalBatch() == EJBCommon.FALSE ? null : glJournalBatchHome.create("REVALUATION " + formatter.format(new java.util.Date()), "REVALUATION JOURNAL " + EJBCommon.getGcCurrentDateWoTime().getTime(), "OPEN", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, AD_CMPNY);
                glJournal.setGlJournalBatch(glJournalBatch);

            } catch (Exception ex) {

            }
            // map journal source
            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("REVALUATION", AD_CMPNY);
            glJournal.setGlJournalSource(glJournalSource);

            // map journal category
            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("GENERAL", AD_CMPNY);
            glJournal.setGlJournalCategory(glJournalCategory);

            glJournal.setGlFunctionalCurrency(adCompany.getGlFunctionalCurrency());

            Iterator i = glChartOfAccounts.iterator();

            double TTL_FRX_GN_LSS = 0d;
            short JL_LN = 1;

            LocalGlChartOfAccount glForexCoa = null;

            try {

                glForexCoa = glChartOfAccountHome.findByCoaAccountNumber(UNRLZD_GN_LSS_ACCNT, AD_BRNCH);

            } catch (FinderException ex) {

                throw new GlCOANoChartOfAccountFoundException();
            }

            while (i.hasNext()) {

                LocalGlChartOfAccount glChartOfAccount = (LocalGlChartOfAccount) i.next();

                // find all
                Collection glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(JR_EFFCTV_DT, glChartOfAccount.getCoaCode(), AD_CMPNY);

                if (glForexLedgers == null || glForexLedgers.isEmpty()) continue;

                LocalGlForexLedger glLatestForexLedger = (LocalGlForexLedger) glForexLedgers.iterator().next();

                int FRL_LN = (glLatestForexLedger.getFrlDate().compareTo(JR_EFFCTV_DT) != 0) ? 1 : glLatestForexLedger.getFrlLine() + 1;

                double FRL_BLNC = glLatestForexLedger.getFrlBalance();
                double FRX_GN_LSS = 0d;

                Iterator j = glForexLedgers.iterator();

                while (j.hasNext() && FRL_BLNC > 0) {

                    LocalGlForexLedger glForexLedger = (LocalGlForexLedger) j.next();

                    if (glForexLedger.getFrlType().equals("REVAL") || glForexLedger.getFrlAmount() <= 0d) continue;

                    if (FRL_BLNC - glForexLedger.getFrlAmount() <= 0d)
                        FRX_GN_LSS = FRX_GN_LSS + (FRL_BLNC * (CONVERSION_RATE - glForexLedger.getFrlRate()));
                    else
                        FRX_GN_LSS = FRX_GN_LSS + (glForexLedger.getFrlAmount() * (CONVERSION_RATE - glForexLedger.getFrlRate()));

                    FRL_BLNC = FRL_BLNC - glForexLedger.getFrlAmount();
                }

                if (FRX_GN_LSS != 0d) {

                    byte JL_DBT = EJBCommon.FALSE;

                    if (glChartOfAccount.getCoaAccountType().equalsIgnoreCase("ASSET"))
                        JL_DBT = (FRX_GN_LSS >= 0) ? EJBCommon.TRUE : EJBCommon.FALSE;
                    else JL_DBT = (FRX_GN_LSS < 0) ? EJBCommon.TRUE : EJBCommon.FALSE;

                    LocalGlJournalLine glJournalLine = glJournalLineHome.create(JL_LN, JL_DBT, Math.abs(FRX_GN_LSS), "", AD_CMPNY);

                    glJournal.addGlJournalLine(glJournalLine);
                    glChartOfAccount.addGlJournalLine(glJournalLine);
                    JL_LN++;

                    // create forex record

                    LocalGlForexLedger newGlForexLedger = glForexLedgerHome.create(JR_EFFCTV_DT, FRL_LN, "REVAL", 0d, CONVERSION_RATE, glLatestForexLedger.getFrlBalance(), FRX_GN_LSS, AD_CMPNY);

                    glChartOfAccount.addGlForexLedger(newGlForexLedger);

                    TTL_FRX_GN_LSS = TTL_FRX_GN_LSS + FRX_GN_LSS;

                    LocalGlJournalLine glFOREXJournalLine = glJournalLineHome.create(JL_LN, JL_DBT == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, Math.abs(FRX_GN_LSS), "", AD_CMPNY);

                    glJournal.addGlJournalLine(glFOREXJournalLine);
                    glForexCoa.addGlJournalLine(glFOREXJournalLine);
                    JL_LN++;
                }
            }

            if (glJournal.getGlJournalLines() == null || glJournal.getGlJournalLines().isEmpty() || glJournal.getGlJournalLines().size() == 0 || TTL_FRX_GN_LSS == 0)
                throw new GlobalNoRecordFoundException();

        } catch (GlobalNoRecordFoundException | GlobalAccountNumberInvalidException |
                 GlCOANoChartOfAccountFoundException | GlFCFunctionalCurrencyAlreadyAssignedException |
                 GlFCNoFunctionalCurrencyFoundException | GlobalDocumentNumberNotUniqueException |
                 GlJREffectiveDateNoPeriodExistException | GlJREffectiveDatePeriodClosedException |
                 GlobalRecordInvalidException ex) {

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

        Debug.print("GlForexRevaluationControllerBean ejbCreate");
    }
}