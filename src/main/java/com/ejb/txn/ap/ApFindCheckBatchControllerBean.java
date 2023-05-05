/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApFindCheckBatchControllerBean
 * @created
 * @author
 **/
package com.ejb.txn.ap;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import javax.naming.NamingException;

import com.ejb.entities.ap.LocalApCheckBatch;
import com.ejb.dao.ap.LocalApCheckBatchHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.EJBContextClass;
import com.util.ap.ApCheckBatchDetails;
import com.util.Debug;
import com.util.EJBHomeFactory;

@Stateless(name = "ApFindCheckBatchControllerEJB")
public class ApFindCheckBatchControllerBean extends EJBContextClass implements ApFindCheckBatchController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalApCheckBatchHome apCheckBatchHome;

    public ArrayList getApCbByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindCheckBatchControllerBean getApCbByCriteria");

        ArrayList list = new ArrayList();

        StringBuilder jbossQl = new StringBuilder();
        jbossQl.append("SELECT OBJECT(cb) FROM ApCheckBatch cb ");

        boolean firstArgument = true;
        short ctr = 0;
        Object[] obj;
        int criteriaSize = criteria.size();

        // Allocate the size of the object parameter


        if (criteria.containsKey("batchName")) {
            criteriaSize--;

        }


        obj = new Object[criteriaSize];


        if (criteria.containsKey("batchName")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");

            }

            jbossQl.append("cb.cbName LIKE '%").append(criteria.get("batchName")).append("%' ");

        }


        if (criteria.containsKey("status")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("cb.cbStatus=?").append(ctr + 1).append(" ");
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
            jbossQl.append("cb.cbDateCreated=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("dateCreated");
            ctr++;
        }

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }
        jbossQl.append("cb.cbAdBranch=").append(AD_BRNCH).append(" ");

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }
        jbossQl.append("cb.cbAdCompany=").append(AD_CMPNY).append(" ");


        String orderBy = null;

        if (ORDER_BY.equals("BATCH NAME")) {

            orderBy = "cb.cbName";

        } else if (ORDER_BY.equals("DATE CREATED")) {

            orderBy = "cb.cbDateCreated";

        }

        if (orderBy != null) {

            jbossQl.append("ORDER BY ").append(orderBy);

        }


        Debug.print("QL + " + jbossQl);


        Collection apCheckBatches = null;

        try {

            apCheckBatches = apCheckBatchHome.getCbByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        if (apCheckBatches.isEmpty()) throw new GlobalNoRecordFoundException();

        for (Object checkBatch : apCheckBatches) {

            LocalApCheckBatch apCheckBatch = (LocalApCheckBatch) checkBatch;

            ApCheckBatchDetails details = new ApCheckBatchDetails();

            details.setCbCode(apCheckBatch.getCbCode());
            details.setCbName(apCheckBatch.getCbName());
            details.setCbDescription(apCheckBatch.getCbDescription());
            details.setCbStatus(apCheckBatch.getCbStatus());
            details.setCbDateCreated(apCheckBatch.getCbDateCreated());
            details.setCbCreatedBy(apCheckBatch.getCbCreatedBy());

            list.add(details);

        }

        return list;

    }


    public Integer getApCbSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindCheckBatchControllerBean getApCbSizeByCriteria");

        StringBuilder jbossQl = new StringBuilder();
        jbossQl.append("SELECT OBJECT(cb) FROM ApCheckBatch cb ");

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

            jbossQl.append("cb.cbName LIKE '%").append(criteria.get("batchName")).append("%' ");

        }


        if (criteria.containsKey("status")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("cb.cbStatus=?").append(ctr + 1).append(" ");
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
            jbossQl.append("cb.cbDateCreated=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("dateCreated");
            ctr++;
        }

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }
        jbossQl.append("cb.cbAdBranch=").append(AD_BRNCH).append(" ");

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }
        jbossQl.append("cb.cbAdCompany=").append(AD_CMPNY).append(" ");

        Collection apCheckBatches = null;

        try {

            apCheckBatches = apCheckBatchHome.getCbByCriteria(jbossQl.toString(), obj, 0, 0);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        if (apCheckBatches.isEmpty()) throw new GlobalNoRecordFoundException();

        return apCheckBatches.size();

    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("ApCheckBatchControllerBean ejbCreate");

    }

    // private methods


}