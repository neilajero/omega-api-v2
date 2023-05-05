package com.ejb.txnreports.gl;

import java.util.*;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.gl.GlCOANoChartOfAccountFoundException;
import com.ejb.exception.gl.GlFCFunctionalCurrencyAlreadyAssignedException;
import com.ejb.exception.gl.GlFCNoFunctionalCurrencyFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDateViolationException;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlForexLedger;
import com.ejb.dao.gl.LocalGlForexLedgerHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.util.ad.AdCompanyDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gl.GlChartOfAccountDetails;
import com.util.reports.gl.GlRepForexRevaluationPrintDetails;

@Stateless(name = "GlRepForexRevaluationPrintControllerEJB")
public class GlRepForexRevaluationPrintControllerBean extends EJBContextClass implements GlRepForexRevaluationPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlForexLedgerHome glForexLedgerHome;

    public GlChartOfAccountDetails getGlChartOfAccount(String UNRLZD_GN_LSS_ACCNT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlCOANoChartOfAccountFoundException {

        LocalGlChartOfAccount glForexCoa = null;

        try {

            glForexCoa = glChartOfAccountHome.findByCoaAccountNumber(UNRLZD_GN_LSS_ACCNT, AD_BRNCH);

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlCOANoChartOfAccountFoundException();
        }

        return new GlChartOfAccountDetails(glForexCoa.getCoaAccountNumber(), glForexCoa.getCoaAccountDescription(), glForexCoa.getCoaAccountType(), glForexCoa.getCoaTaxType(), glForexCoa.getCoaCitCategory(), glForexCoa.getCoaSawCategory(), glForexCoa.getCoaIitCategory(), glForexCoa.getCoaDateFrom(), glForexCoa.getCoaDateTo(), glForexCoa.getCoaEnable(), EJBCommon.TRUE);
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("GlRepForexRevaluationPrintControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GRepForexRevaluationPrintControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeRepForexRevaluationPrint(HashMap criteria, double CONVERSION_RATE, int YR, String PRD_NM, String USR_NM, Integer AD_BRNCH, Integer AD_CMPNY) throws GlJREffectiveDateViolationException, GlFCNoFunctionalCurrencyFoundException, GlFCFunctionalCurrencyAlreadyAssignedException, GlobalAccountNumberInvalidException, GlJREffectiveDateNoPeriodExistException, GlobalNoRecordFoundException {

        Debug.print("GlRepForexRevaluationPrintControllerBean executeRepForexRevaluationPrint");

        try {

            ArrayList list = new ArrayList();

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

                JR_EFFCTV_DT = glAccountingCalendarValue.getAcvDateTo();

            } catch (FinderException ex) {

                throw new GlJREffectiveDateNoPeriodExistException();
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

                if (GL_FUNCTIONAL_CURRENCY != null && GL_FUNCTIONAL_CURRENCY.length() > 0 && (!GL_FUNCTIONAL_CURRENCY.equalsIgnoreCase("NO RECORD FOUND"))) {

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

            for (Object chartOfAccount : glChartOfAccounts) {

                LocalGlChartOfAccount glChartOfAccount = (LocalGlChartOfAccount) chartOfAccount;

                Collection glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(JR_EFFCTV_DT, glChartOfAccount.getCoaCode(), AD_CMPNY);

                if (glForexLedgers == null || glForexLedgers.isEmpty()) continue;

                LocalGlForexLedger glLatestForexLedger = (LocalGlForexLedger) glForexLedgers.iterator().next();

                double FRL_BLNC = glLatestForexLedger.getFrlBalance();

                Iterator j = glForexLedgers.iterator();

                while (j.hasNext() && FRL_BLNC > 0) {

                    LocalGlForexLedger glForexLedger = (LocalGlForexLedger) j.next();

                    if (glForexLedger.getFrlType().equals("REVAL") || glForexLedger.getFrlAmount() <= 0d) continue;

                    double FRX_GN_LSS = 0d;

                    if (FRL_BLNC - glForexLedger.getFrlAmount() <= 0d)
                        FRX_GN_LSS = (FRL_BLNC * (CONVERSION_RATE - glForexLedger.getFrlRate()));
                    else FRX_GN_LSS = (glForexLedger.getFrlAmount() * (CONVERSION_RATE - glForexLedger.getFrlRate()));

                    FRL_BLNC = FRL_BLNC - glForexLedger.getFrlAmount();

                    GlRepForexRevaluationPrintDetails details = new GlRepForexRevaluationPrintDetails();

                    details.setFxpFrlCoaAccountNumber(glChartOfAccount.getCoaAccountNumber());
                    details.setFxpFrlCoaAccountDescription(glChartOfAccount.getCoaAccountDescription());
                    details.setFxpFrlCoaAccountType(glChartOfAccount.getCoaAccountType());
                    details.setFxpFrlRate(glForexLedger.getFrlRate());
                    details.setFxpFrlForexAmount(glForexLedger.getFrlAmount());
                    details.setFxpForexGainLoss(FRX_GN_LSS);
                    details.setFxpForexRemainingAmount(glLatestForexLedger.getFrlBalance());

                    list.add(details);
                }
            }

            if (list == null || list.isEmpty() || list.size() <= 0) throw new GlobalNoRecordFoundException();

            return list;

        } catch (GlobalNoRecordFoundException | GlobalAccountNumberInvalidException |
                 GlFCFunctionalCurrencyAlreadyAssignedException | GlFCNoFunctionalCurrencyFoundException |
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

        Debug.print("GlRepForexRevaluationPrintControllerBean ejbCreate");
    }
}