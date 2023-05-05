/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class GlBudgetEntryControllerBean
 */
package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.gl.GlBGAPasswordInvalidException;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gen.LocalGenSegment;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlBudget;
import com.ejb.entities.gl.LocalGlBudgetAccountAssignment;
import com.ejb.entities.gl.LocalGlBudgetAmount;
import com.ejb.entities.gl.LocalGlBudgetAmountCoa;
import com.ejb.dao.gl.LocalGlBudgetAmountCoaHome;
import com.ejb.dao.gl.LocalGlBudgetAmountHome;
import com.ejb.entities.gl.LocalGlBudgetAmountPeriod;
import com.ejb.dao.gl.LocalGlBudgetAmountPeriodHome;
import com.ejb.dao.gl.LocalGlBudgetHome;
import com.ejb.entities.gl.LocalGlBudgetOrganization;
import com.ejb.dao.gl.LocalGlBudgetOrganizationHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModBudgetAmountCoaDetails;
import com.util.mod.gl.GlModBudgetAmountDetails;
import com.util.mod.gl.GlModBudgetAmountPeriodDetails;

@Stateless(name = "GlBudgetEntryControllerEJB")
public class GlBudgetEntryControllerBean extends EJBContextClass implements GlBudgetEntryController {

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
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;
    @EJB
    private LocalGlBudgetAmountCoaHome glBudgetAmountCoaHome;
    @EJB
    private LocalGlBudgetAmountHome glBudgetAmountHome;
    @EJB
    private LocalGlBudgetAmountPeriodHome glBudgetAmountPeriodHome;
    @EJB
    private LocalGlBudgetHome glBudgetHome;
    @EJB
    private LocalGlBudgetOrganizationHome glBudgetOrganizationHome;


    public ArrayList getGlBoAll(Integer AD_CMPNY) {

        Debug.print("GlBudgetEntryControllerBean getGlBoAll");

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

        Debug.print("GlBudgetEntryControllerBean getGlBgtAll");

        ArrayList list = new ArrayList();

        try {

            Collection glBudgets = glBudgetHome.findBgtAll(AD_CMPNY);

            for (Object budget : glBudgets) {

                LocalGlBudget glBudget = (LocalGlBudget) budget;

                list.add(glBudget.getBgtName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public GlModBudgetAmountDetails getGlBgaByBgaCode(Integer BGA_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlBudgetEntryControllerBean getGlBgaByBgaCode");

        try {

            LocalGlBudgetAmount glBudgetAmount = null;

            try {

                glBudgetAmount = glBudgetAmountHome.findByPrimaryKey(BGA_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList glBgaBudgetAmountCoaList = new ArrayList();

            // get budget amount coa lines

            Collection glBudgetAmountCoas = glBudgetAmount.getGlBudgetAmountCoas();

            for (Object budgetAmountCoa : glBudgetAmountCoas) {

                LocalGlBudgetAmountCoa glBudgetAmountCoa = (LocalGlBudgetAmountCoa) budgetAmountCoa;

                GlModBudgetAmountCoaDetails bcDetails = new GlModBudgetAmountCoaDetails();

                // get all budget amount periods per coa

                ArrayList glBcBudgetAmountPeriodList = new ArrayList();

                Collection glBudgetAmountPeriods = glBudgetAmountCoa.getGlBudgetAmountPeriods();

                for (Object budgetAmountPeriod : glBudgetAmountPeriods) {

                    LocalGlBudgetAmountPeriod glBudgetAmountPeriod = (LocalGlBudgetAmountPeriod) budgetAmountPeriod;

                    GlModBudgetAmountPeriodDetails bapDetails = new GlModBudgetAmountPeriodDetails();
                    bapDetails.setBapAmount(glBudgetAmountPeriod.getBapAmount());
                    bapDetails.setBapAcvPeriodPrefix(glBudgetAmountPeriod.getGlAccountingCalendarValue().getAcvPeriodPrefix());

                    GregorianCalendar gc = new GregorianCalendar();
                    gc.setTime(glBudgetAmountPeriod.getGlAccountingCalendarValue().getAcvDateTo());

                    bapDetails.setBapAcvYear(gc.get(Calendar.YEAR));

                    glBcBudgetAmountPeriodList.add(bapDetails);
                }

                bcDetails.setBcCoaAccountNumber(glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountNumber());
                bcDetails.setBcCoaAccountDescription(glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountDescription());
                bcDetails.setBcDescription(glBudgetAmountCoa.getBcDescription());
                bcDetails.setBcRuleType(glBudgetAmountCoa.getBcRuleType());
                bcDetails.setBcRuleAmount(glBudgetAmountCoa.getBcRuleAmount());
                bcDetails.setBcRulePercentage1(glBudgetAmountCoa.getBcRulePercentage1());
                bcDetails.setBcRulePercentage2(glBudgetAmountCoa.getBcRulePercentage2());
                bcDetails.setBcBudgetAmountPeriodList(glBcBudgetAmountPeriodList);

                glBgaBudgetAmountCoaList.add(bcDetails);
            }

            GlModBudgetAmountDetails mdetails = new GlModBudgetAmountDetails();
            mdetails.setBgaCode(glBudgetAmount.getBgaCode());
            mdetails.setBgaPassword(glBudgetAmount.getGlBudgetOrganization().getBoPassword());
            mdetails.setBgaCreatedBy(glBudgetAmount.getBgaCreatedBy());
            mdetails.setBgaDateCreated(glBudgetAmount.getBgaDateCreated());
            mdetails.setBgaLastModifiedBy(glBudgetAmount.getBgaLastModifiedBy());
            mdetails.setBgaDateLastModified(glBudgetAmount.getBgaDateLastModified());
            mdetails.setBgaBoName(glBudgetAmount.getGlBudgetOrganization().getBoName());
            mdetails.setBgaBgtName(glBudgetAmount.getGlBudget().getBgtName());
            mdetails.setBgaBudgetAmountCoaList(glBgaBudgetAmountCoaList);

            return mdetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlBgaTemplateByBoNameAndBgtName(String BO_NM, String BGT_NM, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlBudgetEntryControllerBean getGlBgtTemplateByBoNameAndBgtName");

        ArrayList list = new ArrayList();

        try {

            LocalGlBudget glBudget = glBudgetHome.findByBgtName(BGT_NM, AD_CMPNY);

            ArrayList glBcBudgetAmountPeriodList = new ArrayList();
            GlModBudgetAmountPeriodDetails periodDetails = new GlModBudgetAmountPeriodDetails();
            String periodName = null;

            // get first period

            periodName = glBudget.getBgtFirstPeriod();
            periodDetails.setBapAcvPeriodPrefix(periodName.substring(0, periodName.indexOf('-')));
            periodDetails.setBapAcvYear(Integer.parseInt(periodName.substring(periodName.indexOf('-') + 1)));
            glBcBudgetAmountPeriodList.add(periodDetails);

            // get remaining periods up to last period defined

            Collection glSetOfBooks = glSetOfBookHome.findByAcvPeriodPrefixAndDate(glBudget.getBgtFirstPeriod().substring(0, glBudget.getBgtFirstPeriod().indexOf('-')), EJBCommon.getIntendedDate(Integer.parseInt(glBudget.getBgtFirstPeriod().substring(glBudget.getBgtFirstPeriod().indexOf('-') + 1))), AD_CMPNY);
            ArrayList glSetOfBookList = new ArrayList(glSetOfBooks);
            LocalGlSetOfBook glSetOfBook = (LocalGlSetOfBook) glSetOfBookList.get(0);

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodPrefix(glSetOfBook.getGlAccountingCalendar().getAcCode(), glBudget.getBgtFirstPeriod().substring(0, glBudget.getBgtFirstPeriod().indexOf('-')), AD_CMPNY);

            while (!glBudget.getBgtFirstPeriod().equals(glBudget.getBgtLastPeriod())) {

                glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glSetOfBook.getGlAccountingCalendar().getAcCode(), (short) (glAccountingCalendarValue.getAcvPeriodNumber() + 1), AD_CMPNY);

                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(glAccountingCalendarValue.getAcvDateTo());

                periodName = glAccountingCalendarValue.getAcvPeriodPrefix() + "-" + gc.get(Calendar.YEAR);
                periodDetails = new GlModBudgetAmountPeriodDetails();
                periodDetails.setBapAcvPeriodPrefix(periodName.substring(0, periodName.indexOf('-')));
                periodDetails.setBapAcvYear(Integer.parseInt(periodName.substring(periodName.indexOf('-') + 1)));
                glBcBudgetAmountPeriodList.add(periodDetails);

                if (glBudget.getBgtLastPeriod().equals(glAccountingCalendarValue.getAcvPeriodPrefix() + "-" + gc.get(Calendar.YEAR))) {

                    break;
                }
            }

            if (glBcBudgetAmountPeriodList.isEmpty()) throw new GlobalNoRecordFoundException();

            return glBcBudgetAmountPeriodList;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveGlBgaEntry(GlModBudgetAmountDetails details, String BO_NM, String BGT_NM, String COA_ACCNT_NMBR, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException, GlBGAPasswordInvalidException {

        Debug.print("GlBudgetEntryControllerBean saveGlBgaEntry");

        try {

            LocalGlBudgetOrganization glBudgetOrganization = null;

            // validate if budget already exist

            try {
                Debug.print("**********************************");
                Debug.print("COA_ACCNT_NMBR: " + COA_ACCNT_NMBR);
                Debug.print("**********************************");
                Debug.print("**********************************");
                Debug.print("details.getBgaCode(): " + details.getBgaCode());
                Debug.print("**********************************");
                LocalGlBudgetAmount glExistingBudgetAmount = glBudgetAmountHome.findByBoNameAndBgtNameAndCoaAccountNumber(BO_NM, BGT_NM, COA_ACCNT_NMBR, AD_CMPNY);
                Debug.print("**********************************");
                Debug.print("glExistingBudgetAmount.getBgaCode(): " + glExistingBudgetAmount.getBgaCode());
                Debug.print("**********************************");
                if (details.getBgaCode() == null || details.getBgaCode() != null && !glExistingBudgetAmount.getBgaCode().equals(details.getBgaCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (FinderException ex) {

            }

            // validate if password is required
            Debug.print("BO_NM: " + BO_NM);
            Debug.print("AD_CMPNY: " + AD_CMPNY);
            glBudgetOrganization = glBudgetOrganizationHome.findByBoName(BO_NM, AD_CMPNY);

            if (glBudgetOrganization.getBoPassword() != null && !glBudgetOrganization.getBoPassword().equals(details.getBgaPassword())) {

                throw new GlBGAPasswordInvalidException();
            }

            LocalGlBudgetAmount glBudgetAmount = null;

            // create budget amount

            Debug.print("details.getBgaCreatedBy(): " + details.getBgaCreatedBy());
            if (details.getBgaCode() == null) {

                glBudgetAmount = glBudgetAmountHome.create(details.getBgaCreatedBy(), details.getBgaDateCreated(), details.getBgaLastModifiedBy(), details.getBgaDateLastModified(), AD_CMPNY);

            } else {

                glBudgetAmount = glBudgetAmountHome.findByPrimaryKey(details.getBgaCode());

                glBudgetAmount.setBgaLastModifiedBy(details.getBgaLastModifiedBy());
                glBudgetAmount.setBgaDateLastModified(details.getBgaDateLastModified());
            }

            glBudgetOrganization.addGlBudgetAmount(glBudgetAmount);

            LocalGlBudget glBudget = glBudgetHome.findByBgtName(BGT_NM, AD_CMPNY);
            glBudget.addGlBudgetAmount(glBudgetAmount);

            // remove all budget amount lines

            Collection glBudgetAmountCoas = glBudgetAmount.getGlBudgetAmountCoas();

            Iterator i = glBudgetAmountCoas.iterator();

            while (i.hasNext()) {

                LocalGlBudgetAmountCoa glBudgetAmountCoa = (LocalGlBudgetAmountCoa) i.next();

                i.remove();

                // glBudgetAmountCoa.entityRemove();
                em.remove(glBudgetAmountCoa);
            }

            // add new budget amount lines

            Collection glBgaBudgetAmountCoaList = details.getBgaBudgetAmountCoaList();

            i = glBgaBudgetAmountCoaList.iterator();

            while (i.hasNext()) {

                GlModBudgetAmountCoaDetails coaDetails = (GlModBudgetAmountCoaDetails) i.next();

                this.addGlBcEntry(coaDetails, glBudgetAmount, glBudgetOrganization, AD_CMPNY);
            }

            return glBudgetAmount.getBgaCode();

        } catch (GlobalRecordAlreadyExistException | GlBGAPasswordInvalidException |
                 GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GlBudgetEntryControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private void addGlBcEntry(GlModBudgetAmountCoaDetails coaDetails, LocalGlBudgetAmount glBudgetAmount, LocalGlBudgetOrganization glBudgetOrganization, Integer AD_CMPNY) throws GlobalAccountNumberInvalidException {

        Debug.print("GlBudgetEntryControllerBean addLines");

        try {

            // validate if coa exists

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(coaDetails.getBcCoaAccountNumber(), AD_CMPNY);

                if (glChartOfAccount.getCoaEnable() == EJBCommon.FALSE)
                    throw new GlobalAccountNumberInvalidException(coaDetails.getBcCoaAccountNumber());

                // validate if coa is included in budget organization

                Collection glBudgetAccountAssignments = glBudgetOrganization.getGlBudgetAccountAssignments();

                for (Object budgetAccountAssignment : glBudgetAccountAssignments) {

                    LocalGlBudgetAccountAssignment glBudgetAccountAssignment = (LocalGlBudgetAccountAssignment) budgetAccountAssignment;

                    LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
                    LocalGenField genField = adCompany.getGenField();
                    Collection genSegments = genSegmentHome.findByFlCode(genField.getFlCode(), AD_CMPNY);
                    String strSeparator = String.valueOf(genField.getFlSegmentSeparator());
                    int genNumberOfSegment = genField.getFlNumberOfSegment();

                    // get coa selected

                    StringBuffer jbossQl = new StringBuffer();
                    jbossQl.append("SELECT OBJECT(coa) FROM GlChartOfAccount coa WHERE ");

                    StringTokenizer stAccountFrom = new StringTokenizer(glBudgetAccountAssignment.getBaaAccountFrom(), strSeparator);
                    StringTokenizer stAccountTo = new StringTokenizer(glBudgetAccountAssignment.getBaaAccountTo(), strSeparator);
                    Debug.print("genNumberOfSegment: " + genNumberOfSegment);
                    try {

                        StringBuilder var = new StringBuilder("1");

                        if (genNumberOfSegment > 1) {

                            for (int i = 0; i < genNumberOfSegment; i++) {

                                if (i == 0 && i < genNumberOfSegment - 1) {

                                    // for first segment

                                    jbossQl.append("SUBSTRING(coa.coaAccountNumber, 1, LOCATE('").append(strSeparator).append("', coa.coaAccountNumber, 1)-1) BETWEEN '").append(stAccountFrom.nextToken()).append("' AND '").append(stAccountTo.nextToken()).append("' AND ");

                                    var = new StringBuilder("LOCATE('" + strSeparator + "', coa.coaAccountNumber, 1)+1 ");
                                    Debug.print("FIRST");

                                } else if (i != 0 && i < genNumberOfSegment - 1) {

                                    // for middle segments

                                    jbossQl.append("SUBSTRING(coa.coaAccountNumber, ").append(var).append(", (LOCATE('").append(strSeparator).append("', coa.coaAccountNumber, ").append(var).append(")) - (").append(var).append(")) BETWEEN '").append(stAccountFrom.nextToken()).append("' AND '").append(stAccountTo.nextToken()).append("' AND ");

                                    var = new StringBuilder("LOCATE('" + strSeparator + "', coa.coaAccountNumber, " + var + ")+1 ");
                                    Debug.print("SECOND");
                                } else if (i != 0 && i == genNumberOfSegment - 1) {

                                    // for last segment

                                    jbossQl.append("SUBSTRING(coa.coaAccountNumber, ").append(var).append(", (LENGTH(coa.coaAccountNumber)+1) - (").append(var).append(")) BETWEEN '").append(stAccountFrom.nextToken()).append("' AND '").append(stAccountTo.nextToken()).append("' ");
                                    Debug.print("THIRD");
                                }
                            }

                        } else if (genNumberOfSegment == 1) {

                            String accountFrom = stAccountFrom.nextToken();
                            String accountTo = stAccountTo.nextToken();

                            jbossQl.append("SUBSTRING(coa.coaAccountNumber, 1, LOCATE('").append(strSeparator).append("', coa.coaAccountNumber, 1)-1) BETWEEN '").append(accountFrom).append("' AND '").append(accountTo).append("' OR ").append("coa.coaAccountNumber BETWEEN '").append(accountFrom).append("' AND '").append(accountTo).append("' ");
                        }

                        jbossQl.append("AND coa.coaEnable=1 AND coa.coaAdCompany=").append(AD_CMPNY).append(" ");

                    } catch (NumberFormatException ex) {

                        throw new GlobalAccountNumberInvalidException();
                    }

                    // generate order by coa natural account

                    short accountSegmentNumber = 0;

                    try {

                        LocalGenSegment genSegment = genSegmentHome.findByFlCodeAndVsName(genField.getFlCode(), glBudgetAccountAssignment.getGlBudgetOrganization().getBoSegmentOrder(), AD_CMPNY);
                        accountSegmentNumber = genSegment.getSgSegmentNumber();

                    } catch (Exception ex) {

                        throw new EJBException(ex.getMessage());
                    }

                    jbossQl.append("ORDER BY coa.coaSegment").append(accountSegmentNumber).append(", coa.coaAccountNumber ");

                    Object[] obj = new Object[0];

                    Collection glChartOfAccounts = glChartOfAccountHome.getCoaByCriteria(jbossQl.toString(), obj, 0, 0);
                    if (!glChartOfAccounts.contains(glChartOfAccount)) {
                        throw new GlobalAccountNumberInvalidException(coaDetails.getBcCoaAccountNumber());
                    }
                }

            } catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException(coaDetails.getBcCoaAccountNumber());
            }

            // create budget amount coa and budget amount period

            LocalGlBudgetAmountCoa glBudgetAmountCoa = glBudgetAmountCoaHome.create(coaDetails.getBcRuleAmount(), coaDetails.getBcRulePercentage1(), coaDetails.getBcRulePercentage2(), coaDetails.getBcDescription(), coaDetails.getBcRuleType(), AD_CMPNY);

            glBudgetAmount.addGlBudgetAmountCoa(glBudgetAmountCoa);
            glChartOfAccount.addGlBudgetAmountCoa(glBudgetAmountCoa);

            Collection glBudgetAmountPeriods = coaDetails.getBcBudgetAmountPeriodList();

            for (Object budgetAmountPeriod : glBudgetAmountPeriods) {

                GlModBudgetAmountPeriodDetails bapDetails = (GlModBudgetAmountPeriodDetails) budgetAmountPeriod;

                LocalGlBudgetAmountPeriod glBudgetAmountPeriod = glBudgetAmountPeriodHome.create(bapDetails.getBapAmount(), AD_CMPNY);

                glBudgetAmountCoa.addGlBudgetAmountPeriod(glBudgetAmountPeriod);

                Collection glSetOfBooks = glSetOfBookHome.findByAcvPeriodPrefixAndDate(bapDetails.getBapAcvPeriodPrefix(), EJBCommon.getIntendedDate(bapDetails.getBapAcvYear()), AD_CMPNY);
                ArrayList glSetOfBookList = new ArrayList(glSetOfBooks);
                LocalGlSetOfBook glSetOfBook = (LocalGlSetOfBook) glSetOfBookList.get(0);

                LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodPrefix(glSetOfBook.getGlAccountingCalendar().getAcCode(), bapDetails.getBapAcvPeriodPrefix(), AD_CMPNY);

                glAccountingCalendarValue.addGlBudgetAmountPeriod(glBudgetAmountPeriod);
            }

        } catch (GlobalAccountNumberInvalidException ex) {

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

        Debug.print("GlBudgetEntryControllerBean ejbCreate");
    }
}