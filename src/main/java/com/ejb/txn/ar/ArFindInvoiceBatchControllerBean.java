/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArFindInvoiceBatchControllerBean
 * @created May 20, 2004, 2:36 PM
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

import com.ejb.entities.ar.LocalArInvoiceBatch;
import com.ejb.dao.ar.LocalArInvoiceBatchHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.EJBContextClass;
import com.util.ar.ArInvoiceBatchDetails;
import com.util.Debug;
import com.util.EJBHomeFactory;

@Stateless(name = "ArFindInvoiceBatchControllerEJB")
public class ArFindInvoiceBatchControllerBean extends EJBContextClass implements ArFindInvoiceBatchController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalArInvoiceBatchHome arInvoiceBatchHome;

    public ArrayList getArIbByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArFindInvoiceBatchControllerBean getArIbByCriteria");

        try {

            ArrayList list = new ArrayList();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(ib) FROM ArInvoiceBatch ib ");

            boolean firstArgument = true;
            short ctr = 0;
            Object[] obj;

            // Allocate the size of the object parameter
            int criteriaSize = criteria.size();

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

                jbossQl.append("ib.ibName LIKE '%").append(criteria.get("batchName")).append("%' ");

            }


            if (criteria.containsKey("status")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("ib.ibStatus=?").append(ctr + 1).append(" ");
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
                jbossQl.append("ib.ibDateCreated=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateCreated");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");

            }

            jbossQl.append("ib.ibAdBranch=").append(AD_BRNCH).append(" AND ib.ibAdCompany=").append(AD_CMPNY).append(" ");


            String orderBy = null;

            if (ORDER_BY.equals("BATCH NAME")) {

                orderBy = "ib.ibName";

            } else if (ORDER_BY.equals("DATE CREATED")) {

                orderBy = "ib.ibDateCreated";

            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);

            }


            Debug.print("QL + " + jbossQl);

            Collection arInvoiceBatches = arInvoiceBatchHome.getIbByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (arInvoiceBatches.isEmpty()) throw new GlobalNoRecordFoundException();

            for (Object invoiceBatch : arInvoiceBatches) {

                LocalArInvoiceBatch arInvoiceBatch = (LocalArInvoiceBatch) invoiceBatch;

                ArInvoiceBatchDetails details = new ArInvoiceBatchDetails();

                details.setIbCode(arInvoiceBatch.getIbCode());
                details.setIbName(arInvoiceBatch.getIbName());
                details.setIbDescription(arInvoiceBatch.getIbDescription());
                details.setIbStatus(arInvoiceBatch.getIbStatus());
                details.setIbDateCreated(arInvoiceBatch.getIbDateCreated());
                details.setIbCreatedBy(arInvoiceBatch.getIbCreatedBy());

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


    public Integer getArIbSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArFindInvoiceBatchControllerBean getArIbSizeByCriteria");

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(ib) FROM ArInvoiceBatch ib ");

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

                jbossQl.append("ib.ibName LIKE '%").append(criteria.get("batchName")).append("%' ");

            }


            if (criteria.containsKey("status")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("ib.ibStatus=?").append(ctr + 1).append(" ");
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
                jbossQl.append("ib.ibDateCreated=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateCreated");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");

            }

            jbossQl.append("ib.ibAdBranch=").append(AD_BRNCH).append(" AND ib.ibAdCompany=").append(AD_CMPNY).append(" ");

            Collection arInvoiceBatches = arInvoiceBatchHome.getIbByCriteria(jbossQl.toString(), obj, 0, 0);

            if (arInvoiceBatches.isEmpty()) throw new GlobalNoRecordFoundException();

            return arInvoiceBatches.size();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {


            ex.printStackTrace();
            throw new EJBException(ex.getMessage());

        }

    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("ArInvoiceBatchControllerBean ejbCreate");

    }

    // private methods


}