/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArFindReceiptBatchControllerBean
 * @created May 20, 2004, 4:47 PM
 * @author Neil Andrew M. Ajero
 **/
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import javax.naming.NamingException;

import com.ejb.entities.ar.LocalArReceiptBatch;
import com.ejb.dao.ar.LocalArReceiptBatchHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.EJBContextClass;
import com.util.ar.ArReceiptBatchDetails;
import com.util.Debug;
import com.util.EJBHomeFactory;

@Stateless(name = "ArFindReceiptBatchControllerEJB")
public class ArFindReceiptBatchControllerBean extends EJBContextClass implements ArFindReceiptBatchController {


    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalArReceiptBatchHome arReceiptBatchHome;

    public ArrayList getArRbByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArFindReceiptBatchControllerBean getArRbByCriteria");

        try {

            ArrayList list = new ArrayList();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(rb) FROM ArReceiptBatch rb ");

            boolean firstArgument = true;
            short ctr = 0;
            Object[] obj;

            // Allocate the size of the object parameter


            if (criteria.containsKey("batchName")) {

                obj = new Object[(criteria.size() - 1)];

            } else {

                obj = new Object[criteria.size() + 2];

            }


            if (criteria.containsKey("batchName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("rb.rbName LIKE '%").append(criteria.get("batchName")).append("%' ");

            }


            if (criteria.containsKey("status")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("rb.rbStatus=?").append(ctr + 1).append(" ");
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
                jbossQl.append("rb.rbDateCreated=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateCreated");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");

            }

            jbossQl.append("rb.rbAdBranch=").append(AD_BRNCH).append(" AND rb.rbAdCompany=").append(AD_CMPNY).append(" ");


            String orderBy = null;

            if (ORDER_BY.equals("BATCH NAME")) {

                orderBy = "rb.rbName";

            } else if (ORDER_BY.equals("DATE CREATED")) {

                orderBy = "rb.rbDateCreated";

            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);

            }

            Debug.print("QL + " + jbossQl);

            Collection arReceiptBatches = arReceiptBatchHome.getRbByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (arReceiptBatches.isEmpty()) throw new GlobalNoRecordFoundException();

            for (Object receiptBatch : arReceiptBatches) {

                LocalArReceiptBatch arReceiptBatch = (LocalArReceiptBatch) receiptBatch;

                ArReceiptBatchDetails details = new ArReceiptBatchDetails();

                details.setRbCode(arReceiptBatch.getRbCode());
                details.setRbName(arReceiptBatch.getRbName());
                details.setRbDescription(arReceiptBatch.getRbDescription());
                details.setRbStatus(arReceiptBatch.getRbStatus());
                details.setRbDateCreated(arReceiptBatch.getRbDateCreated());
                details.setRbCreatedBy(arReceiptBatch.getRbCreatedBy());

                list.add(details);

            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {


            ex.printStackTrace();
            throw new EJBException(ex.getMessage());

        }

    }


    public Integer getArRbSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArFindReceiptBatchControllerBean getArRbSizeByCriteria");

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(rb) FROM ArReceiptBatch rb ");

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

                jbossQl.append("rb.rbName LIKE '%").append(criteria.get("batchName")).append("%' ");

            }


            if (criteria.containsKey("status")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("rb.rbStatus=?").append(ctr + 1).append(" ");
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
                jbossQl.append("rb.rbDateCreated=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateCreated");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");

            }

            jbossQl.append("rb.rbAdBranch=").append(AD_BRNCH).append(" AND rb.rbAdCompany=").append(AD_CMPNY).append(" ");

            Collection arReceiptBatches = arReceiptBatchHome.getRbByCriteria(jbossQl.toString(), obj, 0, 0);

            if (arReceiptBatches.isEmpty()) throw new GlobalNoRecordFoundException();

            return arReceiptBatches.size();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {


            ex.printStackTrace();
            throw new EJBException(ex.getMessage());

        }

    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("ArReceiptBatchControllerBean ejbCreate");

    }

    // private methods


}