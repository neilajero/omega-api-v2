/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArReceiptBatchEntryControllerBean
 * @created May 20, 2004, 3:52 PM
 * @author Neil Andrew M. Ajero
 **/
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import javax.naming.NamingException;

import com.ejb.entities.ar.LocalArReceiptBatch;
import com.ejb.dao.ar.LocalArReceiptBatchHome;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.exception.global.GlobalTransactionBatchCloseException;
import com.util.EJBContextClass;
import com.util.ar.ArReceiptBatchDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBHomeFactory;

@Stateless(name = "ArReceiptBatchEntryControllerEJB")
public class ArReceiptBatchEntryControllerBean extends EJBContextClass implements ArReceiptBatchEntryController {


    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalArReceiptBatchHome arReceiptBatchHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;

    public ArReceiptBatchDetails getArRbByRbCode(Integer RB_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArReceiptBatchEntryControllerBean getArRbByRbCode");
        
        try {

            LocalArReceiptBatch arReceiptBatch = null;

            try {

                arReceiptBatch = arReceiptBatchHome.findByPrimaryKey(RB_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();

            }

            ArReceiptBatchDetails details = new ArReceiptBatchDetails();
            details.setRbCode(arReceiptBatch.getRbCode());
            details.setRbName(arReceiptBatch.getRbName());
            details.setRbDescription(arReceiptBatch.getRbDescription());
            details.setRbStatus(arReceiptBatch.getRbStatus());
            details.setRbType(arReceiptBatch.getRbType());
            details.setRbDateCreated(arReceiptBatch.getRbDateCreated());
            details.setRbCreatedBy(arReceiptBatch.getRbCreatedBy());

            return details;


        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    public Integer saveArRbEntry(ArReceiptBatchDetails details, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyDeletedException, GlobalTransactionBatchCloseException, GlobalRecordAlreadyAssignedException {

        Debug.print("ArReceiptBatchEntryControllerBean saveArIbEntry");

        LocalArReceiptBatch arReceiptBatch = null;

        try {


            // validate if receipt batch is already deleted

            try {

                if (details.getRbCode() != null) {

                    arReceiptBatch = arReceiptBatchHome.findByPrimaryKey(details.getRbCode());

                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();

            }

            // validate if receipt batch exists

            try {

                LocalArReceiptBatch arExistingReceiptBatch = arReceiptBatchHome.findByRbName(details.getRbName(), AD_BRNCH, AD_CMPNY);

                if (details.getRbCode() == null || details.getRbCode() != null && !arExistingReceiptBatch.getRbCode().equals(details.getRbCode())) {

                    throw new GlobalRecordAlreadyExistException();

                }

            } catch (GlobalRecordAlreadyExistException ex) {

                throw ex;

            } catch (FinderException ex) {

            }

            // validate if receipt batch closing

            if (details.getRbStatus().equals("CLOSED")) {

                Collection arReceipts = arReceiptHome.findByRctPostedAndRctVoidAndRbName(EJBCommon.FALSE, EJBCommon.FALSE, details.getRbName(), AD_CMPNY);

                if (!arReceipts.isEmpty()) {

                    throw new GlobalTransactionBatchCloseException();

                }

            }

            // validate if receipt already assigned

            if (details.getRbCode() != null) {

                Collection arReceipts = arReceiptBatch.getArReceipts();

                if (!arReceiptBatch.getRbType().equals(details.getRbType()) && !arReceipts.isEmpty()) {

                    throw new GlobalRecordAlreadyAssignedException();

                }

            }


            if (details.getRbCode() == null) {

                arReceiptBatch = arReceiptBatchHome.create(details.getRbName(), details.getRbDescription(), details.getRbStatus(), details.getRbType(), details.getRbDateCreated(), details.getRbCreatedBy(), AD_BRNCH, AD_CMPNY);

            } else {

                arReceiptBatch.setRbName(details.getRbName());
                arReceiptBatch.setRbDescription(details.getRbDescription());
                arReceiptBatch.setRbStatus(details.getRbStatus());
                arReceiptBatch.setRbType(details.getRbType());

            }

            return arReceiptBatch.getRbCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalRecordAlreadyAssignedException |
                 GlobalTransactionBatchCloseException | GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }


    public void deleteArRbEntry(Integer RB_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("ArReceiptBatchEntryControllerBean deleteArRbEntry");

        try {

            LocalArReceiptBatch arReceiptBatch = arReceiptBatchHome.findByPrimaryKey(RB_CODE);

            Collection arReceipts = arReceiptBatch.getArReceipts();

            if (!arReceipts.isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();

            }

//        	arReceiptBatch.entityRemove();
            em.remove(arReceiptBatch);

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalRecordAlreadyDeletedException();

        } catch (GlobalRecordAlreadyAssignedException ex) {

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

        Debug.print("ArReceiptBatchEntryControllerBean ejbCreate");

    }

    // private methods


}