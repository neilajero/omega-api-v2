/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlFindRecurringJournalControllerBean
 * @created
 * @author
 */
package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gl.LocalGlJournalCategory;
import com.ejb.dao.gl.LocalGlJournalCategoryHome;
import com.ejb.entities.gl.LocalGlRecurringJournal;
import com.ejb.dao.gl.LocalGlRecurringJournalHome;
import com.ejb.entities.gl.LocalGlRecurringJournalLine;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModRecurringJournalDetails;

@Stateless(name = "GlFindRecurringJournalControllerEJB")
public class GlFindRecurringJournalControllerBean extends EJBContextClass implements GlFindRecurringJournalController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalGlRecurringJournalHome glRecurringJournalHome;

    public ArrayList getGlJcAll(Integer AD_CMPNY) {

        Debug.print("GlFindRecurringJournalControllerBean getGlJcAll");

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

        Debug.print("GlFindRecurringJournalControllerBean getGlJrPostableByCriteria");

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

            GlModRecurringJournalDetails mdetails = new GlModRecurringJournalDetails();
            mdetails.setRjCode(glRecurringJournal.getRjCode());
            mdetails.setRjName(glRecurringJournal.getRjName());
            mdetails.setRjDescription(glRecurringJournal.getRjDescription());
            mdetails.setRjNextRunDate(glRecurringJournal.getRjNextRunDate());
            mdetails.setRjSchedule(glRecurringJournal.getRjSchedule());
            mdetails.setRjLastRunDate(glRecurringJournal.getRjLastRunDate());
            mdetails.setRjTotalDebit(TOTAL_DEBIT);
            mdetails.setRjTotalCredit(TOTAL_CREDIT);
            mdetails.setRjJcName(glRecurringJournal.getGlJournalCategory().getJcName());

            rjList.add(mdetails);
        }

        return rjList;
    }

    public Integer getGlRjSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFindRecurringJournalControllerBean getGlRjSizeByCriteria");

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

        Collection glRecurringJournals = null;

        try {

            glRecurringJournals = glRecurringJournalHome.getRjByCriteria(jbossQl.toString(), obj, 0, 0);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glRecurringJournals.isEmpty()) throw new GlobalNoRecordFoundException();

        return glRecurringJournals.size();
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GlFindRecurringJournalControllerBean getGlFcPrecisionUnit");

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

}