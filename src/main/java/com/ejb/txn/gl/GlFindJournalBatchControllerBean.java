/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlFindJournalBatchControllerBean
 * @created
 * @author
 **/
package com.ejb.txn.gl;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import javax.naming.NamingException;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gl.LocalGlJournalBatch;
import com.ejb.dao.gl.LocalGlJournalBatchHome;
import com.util.EJBContextClass;
import com.util.Debug;
import com.util.EJBHomeFactory;
import com.util.gl.GlJournalBatchDetails;

@Stateless(name = "GlFindJournalBatchControllerEJB")
public class GlFindJournalBatchControllerBean extends EJBContextClass implements GlFindJournalBatchController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;

    public ArrayList getGlJbByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFindJournalBatchControllerBean getGlJbByCriteria");

        ArrayList list = new ArrayList();

        StringBuilder jbossQl = new StringBuilder();
        jbossQl.append("SELECT OBJECT(jb) FROM GlJournalBatch jb ");

        boolean firstArgument = true;
        short ctr = 0;
        Object[] obj;

        // Allocate the size of the object parameter


        if (criteria.containsKey("batchName")) {

            obj = new Object[(criteria.size() - 1)];

        } else {

            obj = new Object[criteria.size()];

        }


        if (criteria.containsKey("batchName")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");

            }

            jbossQl.append("jb.jbName LIKE '%").append(criteria.get("batchName")).append("%' ");

        }


        if (criteria.containsKey("status")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("jb.jbStatus=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("status");
            ctr++;
        }

        if (criteria.containsKey("dateCreated")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("jb.jbDateCreated=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("dateCreated");
            ctr++;
        }

        if (!firstArgument) {

            jbossQl.append("AND ");

        } else {

            firstArgument = false;
            jbossQl.append("WHERE ");

        }

        jbossQl.append("jb.jbAdBranch=").append(AD_BRNCH).append(" AND jb.jbAdCompany=").append(AD_CMPNY).append(" ");

        String orderBy = null;

        if (ORDER_BY.equals("BATCH NAME")) {

            orderBy = "jb.jbName";

        } else if (ORDER_BY.equals("DATE CREATED")) {

            orderBy = "jb.jbDateCreated";

        }

        if (orderBy != null) {

            jbossQl.append("ORDER BY ").append(orderBy);

        }


        Debug.print("QL + " + jbossQl);


        Collection glJournalBatches = null;

        try {

            glJournalBatches = glJournalBatchHome.getJbByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        if (glJournalBatches.isEmpty()) throw new GlobalNoRecordFoundException();

        for (Object journalBatch : glJournalBatches) {

            LocalGlJournalBatch glJournalBatch = (LocalGlJournalBatch) journalBatch;

            GlJournalBatchDetails details = new GlJournalBatchDetails();

            details.setJbCode(glJournalBatch.getJbCode());
            details.setJbName(glJournalBatch.getJbName());
            details.setJbDescription(glJournalBatch.getJbDescription());
            details.setJbStatus(glJournalBatch.getJbStatus());
            details.setJbDateCreated(glJournalBatch.getJbDateCreated());
            details.setJbCreatedBy(glJournalBatch.getJbCreatedBy());

            list.add(details);

        }

        return list;

    }


    public Integer getGlJbSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFindJournalBatchControllerBean getGlJbSizeByCriteria");

        StringBuilder jbossQl = new StringBuilder();
        jbossQl.append("SELECT OBJECT(jb) FROM GlJournalBatch jb ");

        boolean firstArgument = true;
        short ctr = 0;
        Object[] obj;

        // Allocate the size of the object parameter


        if (criteria.containsKey("batchName")) {

            obj = new Object[criteria.size() - 1];

        } else {

            obj = new Object[criteria.size()];

        }


        if (criteria.containsKey("batchName")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");

            }

            jbossQl.append("jb.jbName LIKE '%").append(criteria.get("batchName")).append("%' ");

        }


        if (criteria.containsKey("status")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("jb.jbStatus=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("status");
            ctr++;
        }

        if (criteria.containsKey("dateCreated")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("jb.jbDateCreated=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("dateCreated");
            ctr++;
        }

        if (!firstArgument) {

            jbossQl.append("AND ");

        } else {

            firstArgument = false;
            jbossQl.append("WHERE ");

        }

        jbossQl.append("jb.jbAdBranch=").append(AD_BRNCH).append(" AND jb.jbAdCompany=").append(AD_CMPNY).append(" ");

        Collection glJournalBatches = null;

        try {

            glJournalBatches = glJournalBatchHome.getJbByCriteria(jbossQl.toString(), obj, 0, 0);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        if (glJournalBatches.isEmpty()) throw new GlobalNoRecordFoundException();

        return glJournalBatches.size();

    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("GlJournalBatchControllerBean ejbCreate");

    }

    // private methods


}