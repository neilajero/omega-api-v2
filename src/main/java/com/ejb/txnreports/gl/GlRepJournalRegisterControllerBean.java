package com.ejb.txnreports.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.global.GlobalNoRecordFoundException;
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
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.reports.gl.GlRepJournalRegisterDetails;

@Stateless(name = "GlRepJournalRegisterControllerEJB")
public class GlRepJournalRegisterControllerBean extends EJBContextClass implements GlRepJournalRegisterController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;


    public ArrayList executeGlRepJournalRegister(HashMap criteria, String ORDER_BY, String GROUP_BY, ArrayList branchList, boolean SHOW_ENTRIES, boolean SUMMARIZE, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlRepJournalRegisterControllerBean executeGlRepJournalRegister");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(jr) FROM GlJournal jr WHERE (");

            if (branchList.isEmpty()) throw new GlobalNoRecordFoundException();

            Iterator brIter = branchList.iterator();

            AdBranchDetails details = (AdBranchDetails) brIter.next();
            jbossQl.append("jr.jrAdBranch=").append(details.getBrCode());

            while (brIter.hasNext()) {

                details = (AdBranchDetails) brIter.next();

                jbossQl.append(" OR jr.jrAdBranch=").append(details.getBrCode());
            }

            jbossQl.append(") ");
            firstArgument = false;

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("includeUnposted")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("journalNumberFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("jr.jrDocumentNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("journalNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("journalNumberTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("jr.jrDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("journalNumberTo");
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

            if (criteria.containsKey("includeUnposted") && criteria.get("includeUnposted").equals("NO")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jr.jrPosted=1 ");
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("jr.jrAdCompany=").append(AD_CMPNY).append(" ");

            Collection glJournals = glJournalHome.getJrByCriteria(jbossQl.toString(), obj, 0, 0);

            if (glJournals.size() == 0) throw new GlobalNoRecordFoundException();

            Iterator i = glJournals.iterator();

            double AMNT = 0d;

            if (SHOW_ENTRIES) {

                while (i.hasNext()) {

                    LocalGlJournal glJournal = (LocalGlJournal) i.next();

                    AMNT = 0d;

                    Collection glJournalLines = glJournal.getGlJournalLines();
                    Iterator j = glJournalLines.iterator();

                    while (j.hasNext()) {

                        LocalGlJournalLine glJournalLine = (LocalGlJournalLine) j.next();

                        if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {

                            AMNT += glJournalLine.getJlAmount();
                        }
                    }

                    glJournalLines = glJournal.getGlJournalLines();
                    j = glJournalLines.iterator();

                    while (j.hasNext()) {

                        LocalGlJournalLine glJournalLine = (LocalGlJournalLine) j.next();

                        GlRepJournalRegisterDetails mdetails = new GlRepJournalRegisterDetails();

                        mdetails.setJrJrDocumentNumber(glJournal.getJrDocumentNumber());
                        mdetails.setJrJrReferenceNumber(glJournal.getJrName());
                        mdetails.setJrJrDescription(glJournal.getJrDescription());
                        mdetails.setJrJrDate(glJournal.getJrEffectiveDate());
                        mdetails.setJrJrSource(glJournal.getGlJournalSource().getJsName());
                        mdetails.setJrJrCategory(glJournal.getGlJournalCategory().getJcName());
                        mdetails.setJrJrBatch(glJournal.getGlJournalBatch() != null ? glJournal.getGlJournalBatch().getJbName() : null);
                        mdetails.setJrJrAmount(AMNT);
                        mdetails.setOrderBy(ORDER_BY);

                        mdetails.setJrJlCoaAccountNumber(glJournalLine.getGlChartOfAccount().getCoaAccountNumber());
                        mdetails.setJrJlCoaAccountDescription(glJournalLine.getGlChartOfAccount().getCoaAccountDescription());

                        if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {

                            mdetails.setJrJlDebit(glJournalLine.getJlAmount());

                        } else {

                            mdetails.setJrJlCredit(glJournalLine.getJlAmount());
                        }

                        list.add(mdetails);
                    }
                }

            } else {

                while (i.hasNext()) {

                    LocalGlJournal glJournal = (LocalGlJournal) i.next();

                    AMNT = 0d;

                    Collection glJournalLines = glJournal.getGlJournalLines();

                    for (Object journalLine : glJournalLines) {

                        LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                        if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {

                            AMNT += glJournalLine.getJlAmount();
                        }
                    }

                    GlRepJournalRegisterDetails mdetails = new GlRepJournalRegisterDetails();

                    mdetails.setJrJrDocumentNumber(glJournal.getJrDocumentNumber());
                    mdetails.setJrJrReferenceNumber(glJournal.getJrName());
                    mdetails.setJrJrDescription(glJournal.getJrDescription());
                    mdetails.setJrJrDate(glJournal.getJrEffectiveDate());
                    mdetails.setJrJrSource(glJournal.getGlJournalSource().getJsName());
                    mdetails.setJrJrCategory(glJournal.getGlJournalCategory().getJcName());
                    mdetails.setJrJrBatch(glJournal.getGlJournalBatch() != null ? glJournal.getGlJournalBatch().getJbName() : null);
                    mdetails.setJrJrAmount(AMNT);
                    mdetails.setOrderBy(ORDER_BY);

                    list.add(mdetails);
                }
            }

            // sort

            switch (GROUP_BY) {
                case "SOURCE":

                    list.sort(GlRepJournalRegisterDetails.JournalSourceComparator);

                    break;
                case "CATEGORY":

                    list.sort(GlRepJournalRegisterDetails.JournalCategoryComparator);

                    break;
                case "BATCH":

                    list.sort(GlRepJournalRegisterDetails.JournalBatchComparator);

                    break;
                default:

                    list.sort(GlRepJournalRegisterDetails.NoGroupComparator);
                    break;
            }

            if (SUMMARIZE) {

                list.sort(GlRepJournalRegisterDetails.CoaAccountNumberComparator);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("GlRepJournalRegisterControllerBean getGlReportableAcvAll");

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

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlRepJournalRegisterControllerBean getAdBrResAll");

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

    public ArrayList getGlJsAll(Integer AD_CMPNY) {

        Debug.print("GlRepGeneralLedgerControllerBean getGlJsAll");

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

    public ArrayList getGlJcAll(Integer AD_CMPNY) {

        Debug.print("GlRepGeneralLedgerControllerBean getGlJcAll");

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

    public ArrayList getGlJbAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("GlRepGeneralLedgerControllerBean getGlJbAll");

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

    // private methods

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("GlRepGeneralLedgerControllerBean convertForeignToFunctionalCurrency");

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

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlRepGeneralLedgerControllerBean ejbCreate");
    }
}