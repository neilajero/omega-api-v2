/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApCheckBatchEntryControllerBean
 * @created
 * @author
 **/
package com.ejb.txn.ap;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import javax.naming.NamingException;

import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.entities.ap.LocalApCheckBatch;
import com.ejb.dao.ap.LocalApCheckBatchHome;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.exception.global.GlobalTransactionBatchCloseException;
import com.util.EJBContextClass;
import com.util.ap.ApCheckBatchDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBHomeFactory;

@Stateless(name = "ApCheckBatchEntryControllerEJB")
public class ApCheckBatchEntryControllerBean extends EJBContextClass implements ApCheckBatchEntryController {
    
    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApCheckBatchHome apCheckBatchHome;

    public ArrayList getAdLvDEPARTMENT(Integer AD_CMPNY) {

        Debug.print("ApDirectCheckEntryControllerBean getAdLvDEPARTMENT");

        ArrayList list = new ArrayList();


        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AD DEPARTMENT", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());

            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    public ApCheckBatchDetails getApCbByCbCode(Integer VB_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApCheckBatchEntryControllerBean getApCbByCbCode");

        try {

            LocalApCheckBatch apCheckBatch = null;


            try {

                apCheckBatch = apCheckBatchHome.findByPrimaryKey(VB_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();

            }

            ApCheckBatchDetails details = new ApCheckBatchDetails();
            details.setCbCode(apCheckBatch.getCbCode());
            details.setCbName(apCheckBatch.getCbName());
            details.setCbDescription(apCheckBatch.getCbDescription());
            details.setCbStatus(apCheckBatch.getCbStatus());
            details.setCbType(apCheckBatch.getCbType());
            details.setCbDepartment(apCheckBatch.getCbDepartment());
            details.setCbDateCreated(apCheckBatch.getCbDateCreated());
            details.setCbCreatedBy(apCheckBatch.getCbCreatedBy());

            return details;


        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    public Integer saveApCbEntry(ApCheckBatchDetails details, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyDeletedException, GlobalTransactionBatchCloseException, GlobalRecordAlreadyAssignedException {

        Debug.print("ApCheckBatchEntryControllerBean saveApCbEntry");

        LocalApCheckBatch apCheckBatch = null;

        try {


            // validate if check batch is already deleted

            try {

                if (details.getCbCode() != null) {

                    apCheckBatch = apCheckBatchHome.findByPrimaryKey(details.getCbCode());

                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();

            }

            // validate if check batch exists

            try {

                LocalApCheckBatch apExistingCheckBatch = apCheckBatchHome.findByCbName(details.getCbName(), AD_BRNCH, AD_CMPNY);

                if (details.getCbCode() == null || details.getCbCode() != null && !apExistingCheckBatch.getCbCode().equals(details.getCbCode())) {

                    throw new GlobalRecordAlreadyExistException();

                }

            } catch (GlobalRecordAlreadyExistException ex) {

                throw ex;

            } catch (FinderException ex) {

            }

            // validate if check batch closing

            if (details.getCbStatus().equals("CLOSED")) {

                Collection apChecks = apCheckHome.findByChkPostedAndChkVoidAndCbName(EJBCommon.FALSE, EJBCommon.FALSE, details.getCbName(), AD_CMPNY);

                if (!apChecks.isEmpty()) {

                    throw new GlobalTransactionBatchCloseException();

                }

            }

            // validate if check already assigned

            if (details.getCbCode() != null) {

                Collection apChecks = apCheckBatch.getApChecks();

                if (!apCheckBatch.getCbType().equals(details.getCbType()) && !apChecks.isEmpty()) {

                    throw new GlobalRecordAlreadyAssignedException();

                }

            }


            if (details.getCbCode() == null) {

                apCheckBatch = apCheckBatchHome.create(details.getCbName(), details.getCbDescription(), details.getCbStatus(), details.getCbType(), details.getCbDateCreated(), details.getCbCreatedBy(), details.getCbDepartment(), AD_BRNCH, AD_CMPNY);

            } else {

                apCheckBatch.setCbName(details.getCbName());
                apCheckBatch.setCbDescription(details.getCbDescription());
                apCheckBatch.setCbStatus(details.getCbStatus());
                apCheckBatch.setCbType(details.getCbType());
                apCheckBatch.setCbDepartment(details.getCbDepartment());

            }

            return apCheckBatch.getCbCode();

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


    public void deleteApCbEntry(Integer VB_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("ApCheckBatchEntryControllerBean deleteApCbEntry");

        try {

            LocalApCheckBatch apCheckBatch = apCheckBatchHome.findByPrimaryKey(VB_CODE);

            Collection apChecks = apCheckBatch.getApChecks();

            if (!apChecks.isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();

            }

            em.remove(apCheckBatch);

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

        Debug.print("ApCheckBatchEntryControllerBean ejbCreate");

    }

    // private methods


}