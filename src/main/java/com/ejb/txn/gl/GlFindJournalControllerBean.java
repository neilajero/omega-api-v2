/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlFindJournalControllerBean
 * @created
 * @author
 */
package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
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
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;
import com.util.mod.gl.GlModJournalDetails;

@Stateless(name = "GlFindJournalControllerEJB")
public class GlFindJournalControllerBean extends EJBContextClass implements GlFindJournalController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;


    public ArrayList getGlJcAll(Integer AD_CMPNY) {

        Debug.print("GlFindJournalControllerBean getGlJcAll");

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

        Debug.print("GlFindJournalControllerBean getGlJsAll");

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

        Debug.print("GlFindJournalControllerBean getGlFcAllWithDefault");

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

        Debug.print("GlFindJournalControllerBean getGlOpenJbAll");

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

        Debug.print("GlFindJournalControllerBean getAdPrfEnableGlJournalBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfEnableGlJournalBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlJrByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFindJournalControllerBean getGlJrByCriteria");

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

        if (criteria.containsKey("approvalStatus")) {

            String approvalStatus = (String) criteria.get("approvalStatus");

            if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {

                criteriaSize--;
            }
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

        if (criteria.containsKey("approvalStatus")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            String approvalStatus = (String) criteria.get("approvalStatus");

            if (approvalStatus.equals("DRAFT")) {

                jbossQl.append("jr.jrApprovalStatus IS NULL ");

            } else if (approvalStatus.equals("REJECTED")) {

                jbossQl.append("jr.jrReasonForRejection IS NOT NULL ");

            } else {

                jbossQl.append("jr.jrApprovalStatus=?").append(ctr + 1).append(" ");
                obj[ctr] = approvalStatus;
                ctr++;
            }
        }

        if (criteria.containsKey("posted")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("jr.jrPosted=?").append(ctr + 1).append(" ");

            String posted = (String) criteria.get("posted");

            if (posted.equals("YES")) {

                obj[ctr] = EJBCommon.TRUE;

            } else {

                obj[ctr] = EJBCommon.FALSE;
            }

            ctr++;
        }

        if (!firstArgument) {

            jbossQl.append("AND ");

        } else {

            firstArgument = false;
            jbossQl.append("WHERE ");
        }

        jbossQl.append("jr.jrAdBranch=").append(AD_BRNCH).append(" AND jr.jrAdCompany=").append(AD_CMPNY).append(" ");

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

            jrList.add(mdetails);
        }

        return jrList;
    }

    public Integer getGlJrSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFindJournalControllerBean getGlJrByCriteria");

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

        if (criteria.containsKey("approvalStatus")) {

            String approvalStatus = (String) criteria.get("approvalStatus");

            if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {

                criteriaSize--;
            }
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

        if (criteria.containsKey("approvalStatus")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            String approvalStatus = (String) criteria.get("approvalStatus");

            if (approvalStatus.equals("DRAFT")) {

                jbossQl.append("jr.jrApprovalStatus IS NULL ");

            } else if (approvalStatus.equals("REJECTED")) {

                jbossQl.append("jr.jrReasonForRejection IS NOT NULL ");

            } else {

                jbossQl.append("jr.jrApprovalStatus=?").append(ctr + 1).append(" ");
                obj[ctr] = approvalStatus;
                ctr++;
            }
        }

        if (criteria.containsKey("posted")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("jr.jrPosted=?").append(ctr + 1).append(" ");

            String posted = (String) criteria.get("posted");

            if (posted.equals("YES")) {

                obj[ctr] = EJBCommon.TRUE;

            } else {

                obj[ctr] = EJBCommon.FALSE;
            }

            ctr++;
        }

        if (!firstArgument) {

            jbossQl.append("AND ");

        } else {

            firstArgument = false;
            jbossQl.append("WHERE ");
        }

        jbossQl.append("jr.jrAdBranch=").append(AD_BRNCH).append(" AND jr.jrAdCompany=").append(AD_CMPNY).append(" ");

        Collection glJournals = null;

        try {

            glJournals = glJournalHome.getJrByCriteria(jbossQl.toString(), obj, 0, 0);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glJournals.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        return glJournals.size();
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GlFindJournalControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlJournalBatchControllerBean ejbCreate");
    }

    // private methods

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("GlFindJournalControllerBean convertForeignToFunctionalCurrency");

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
}