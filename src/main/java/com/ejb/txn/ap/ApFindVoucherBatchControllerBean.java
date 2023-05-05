/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApFindVoucherBatchControllerBean
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

import com.ejb.dao.ap.LocalApVoucherBatch;
import com.ejb.dao.ap.LocalApVoucherBatchHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.EJBContextClass;
import com.util.ap.ApVoucherBatchDetails;
import com.util.Debug;

@Stateless(name = "ApFindVoucherBatchControllerEJB")
public class ApFindVoucherBatchControllerBean extends EJBContextClass implements ApFindVoucherBatchController {
    
    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalApVoucherBatchHome apVoucherBatchHome;

    public ArrayList getApVbByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindVoucherBatchControllerBean getApVbByCriteria");

        ArrayList list = new ArrayList();

        StringBuilder jbossQl = new StringBuilder();
        jbossQl.append("SELECT OBJECT(vb) FROM ApVoucherBatch vb ");

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

            jbossQl.append("vb.vbName LIKE '%").append(criteria.get("batchName")).append("%' ");

        }


        if (criteria.containsKey("status")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("vb.vbStatus=?").append(ctr + 1).append(" ");
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
            jbossQl.append("vb.vbDateCreated=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("dateCreated");
            ctr++;
        }

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }
        jbossQl.append("vb.vbAdBranch=").append(AD_BRNCH).append(" ");

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }
        jbossQl.append("vb.vbAdCompany=").append(AD_CMPNY).append(" ");


        String orderBy = null;

        if (ORDER_BY.equals("BATCH NAME")) {

            orderBy = "vb.vbName";

        } else if (ORDER_BY.equals("DATE CREATED")) {

            orderBy = "vb.vbDateCreated";

        }

        if (orderBy != null) {

            jbossQl.append("ORDER BY ").append(orderBy);

        }


        Debug.print("QL + " + jbossQl);


        Collection apVoucherBatches = null;

        try {

            apVoucherBatches = apVoucherBatchHome.getVbByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        if (apVoucherBatches.isEmpty()) throw new GlobalNoRecordFoundException();

        for (Object voucherBatch : apVoucherBatches) {

            LocalApVoucherBatch apVoucherBatch = (LocalApVoucherBatch) voucherBatch;

            ApVoucherBatchDetails details = new ApVoucherBatchDetails();

            details.setVbCode(apVoucherBatch.getVbCode());
            details.setVbName(apVoucherBatch.getVbName());
            details.setVbDescription(apVoucherBatch.getVbDescription());
            details.setVbStatus(apVoucherBatch.getVbStatus());
            details.setVbDateCreated(apVoucherBatch.getVbDateCreated());
            details.setVbCreatedBy(apVoucherBatch.getVbCreatedBy());

            list.add(details);

        }

        return list;

    }


    public Integer getApVbSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindVoucherBatchControllerBean getApVbSizeByCriteria");

        StringBuilder jbossQl = new StringBuilder();
        jbossQl.append("SELECT OBJECT(vb) FROM ApVoucherBatch vb ");

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

            jbossQl.append("vb.vbName LIKE '%").append(criteria.get("batchName")).append("%' ");

        }


        if (criteria.containsKey("status")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("vb.vbStatus=?").append(ctr + 1).append(" ");
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
            jbossQl.append("vb.vbDateCreated=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("dateCreated");
            ctr++;
        }

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }
        jbossQl.append("vb.vbAdBranch=").append(AD_BRNCH).append(" ");

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }
        jbossQl.append("vb.vbAdCompany=").append(AD_CMPNY).append(" ");

        Collection apVoucherBatches = null;

        try {

            apVoucherBatches = apVoucherBatchHome.getVbByCriteria(jbossQl.toString(), obj, 0, 0);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        if (apVoucherBatches.isEmpty()) throw new GlobalNoRecordFoundException();

        return apVoucherBatches.size();

    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("ApVoucherBatchControllerBean ejbCreate");

    }

    // private methods


}