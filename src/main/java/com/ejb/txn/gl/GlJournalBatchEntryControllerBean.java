/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlJournalBatchEntryControllerBean
 * @created
 * @author
 **/
package com.ejb.txn.gl;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import javax.naming.NamingException;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.exception.global.GlobalTransactionBatchCloseException;
import com.ejb.entities.gl.LocalGlJournalBatch;
import com.ejb.dao.gl.LocalGlJournalBatchHome;
import com.ejb.dao.gl.LocalGlJournalHome;
import com.util.EJBContextClass;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBHomeFactory;
import com.util.gl.GlJournalBatchDetails;


@Stateless(name = "GlJournalBatchEntryControllerEJB")
public class GlJournalBatchEntryControllerBean extends EJBContextClass implements GlJournalBatchEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;

    public GlJournalBatchDetails getGlJbByJbCode(Integer JB_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlJournalBatchEntryControllerBean getGlJbByJbCode");

        try {

            LocalGlJournalBatch glJournalBatch = null;


            try {

                glJournalBatch = glJournalBatchHome.findByPrimaryKey(JB_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();

            }

            GlJournalBatchDetails details = new GlJournalBatchDetails();
            details.setJbCode(glJournalBatch.getJbCode());
            details.setJbName(glJournalBatch.getJbName());
            details.setJbDescription(glJournalBatch.getJbDescription());
            details.setJbStatus(glJournalBatch.getJbStatus());
            details.setJbDateCreated(glJournalBatch.getJbDateCreated());
            details.setJbCreatedBy(glJournalBatch.getJbCreatedBy());

            return details;


        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    public Integer saveGlJbEntry(GlJournalBatchDetails details, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyDeletedException, GlobalTransactionBatchCloseException {

        Debug.print("GlJournalBatchEntryControllerBean saveGlJrEntry");

        LocalGlJournalBatch glJournalBatch = null;


        try {

            // validate if journal batch is already deleted

            try {

                if (details.getJbCode() != null) {

                    glJournalBatch = glJournalBatchHome.findByPrimaryKey(details.getJbCode());

                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();

            }

            // validate if journal batch exists

            try {

                LocalGlJournalBatch glExistingJournalBatch = glJournalBatchHome.findByJbName(details.getJbName(), AD_BRNCH, AD_CMPNY);

                if (details.getJbCode() == null || details.getJbCode() != null && !glExistingJournalBatch.getJbCode().equals(details.getJbCode())) {

                    throw new GlobalRecordAlreadyExistException();

                }

            } catch (GlobalRecordAlreadyExistException ex) {

                throw ex;

            } catch (FinderException ex) {

            }

            // validate if journal batch closing

            if (details.getJbStatus().equals("CLOSED")) {

                Collection glJournals = glJournalHome.findByJrPostedAndJbName(EJBCommon.FALSE, details.getJbName(), AD_CMPNY);

                if (!glJournals.isEmpty()) {

                    throw new GlobalTransactionBatchCloseException();

                }

            }


            if (details.getJbCode() == null) {

                glJournalBatch = glJournalBatchHome.create(details.getJbName(), details.getJbDescription(), details.getJbStatus(), details.getJbDateCreated(), details.getJbCreatedBy(), AD_BRNCH, AD_CMPNY);

            } else {

                glJournalBatch.setJbName(details.getJbName());
                glJournalBatch.setJbDescription(details.getJbDescription());
                glJournalBatch.setJbStatus(details.getJbStatus());

            }

            return glJournalBatch.getJbCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalTransactionBatchCloseException |
                 GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }


    public void deleteGlJbEntry(Integer JB_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("GlJournalBatchEntryControllerBean deleteGlJbEntry");

        try {

            LocalGlJournalBatch glJournalBatch = glJournalBatchHome.findByPrimaryKey(JB_CODE);

            if (!glJournalBatch.getGlJournals().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();

            }

            em.remove(glJournalBatch);

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

        Debug.print("GlJournalBatchEntryControllerBean ejbCreate");

    }

    // private methods


}