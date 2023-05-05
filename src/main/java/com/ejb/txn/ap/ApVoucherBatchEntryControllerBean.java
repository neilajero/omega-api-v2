/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApVoucherBatchEntryControllerBean
 * @created
 * @author
 **/
package com.ejb.txn.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ap.LocalApVoucherBatch;
import com.ejb.dao.ap.LocalApVoucherBatchHome;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.ap.ApVoucherBatchDetails;

import jakarta.ejb.*;
import java.util.Collection;


@Stateless(name = "ApVoucherBatchEntryControllerEJB")
public class ApVoucherBatchEntryControllerBean extends EJBContextClass implements ApVoucherBatchEntryController {


    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalApVoucherBatchHome apVoucherBatchHome;


    public ApVoucherBatchDetails getApVbByVbCode(Integer VB_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApVoucherBatchEntryControllerBean getApVbByVbCode");

        try {

            LocalApVoucherBatch apVoucherBatch = null;


            try {

                apVoucherBatch = apVoucherBatchHome.findByPrimaryKey(VB_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();

            }

            ApVoucherBatchDetails details = new ApVoucherBatchDetails();
            details.setVbCode(apVoucherBatch.getVbCode());
            details.setVbName(apVoucherBatch.getVbName());
            details.setVbDescription(apVoucherBatch.getVbDescription());
            details.setVbStatus(apVoucherBatch.getVbStatus());
            details.setVbType(apVoucherBatch.getVbType());
            details.setVbDateCreated(apVoucherBatch.getVbDateCreated());
            details.setVbDepartment(apVoucherBatch.getVbDepartment());
            details.setVbCreatedBy(apVoucherBatch.getVbCreatedBy());

            return details;


        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    public Integer saveApVbEntry(ApVoucherBatchDetails details, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyDeletedException, GlobalTransactionBatchCloseException, GlobalRecordAlreadyAssignedException {

        Debug.print("ApVoucherBatchEntryControllerBean saveApVbEntry");

        LocalApVoucherBatch apVoucherBatch = null;

        try {


            // validate if voucher batch is already deleted

            try {

                if (details.getVbCode() != null) {

                    apVoucherBatch = apVoucherBatchHome.findByPrimaryKey(details.getVbCode());

                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();

            }

            // validate if voucher batch exists

            try {

                LocalApVoucherBatch apExistingVoucherBatch = apVoucherBatchHome.findByVbName(details.getVbName(), AD_BRNCH, AD_CMPNY);

                if (details.getVbCode() == null || details.getVbCode() != null && !apExistingVoucherBatch.getVbCode().equals(details.getVbCode())) {

                    throw new GlobalRecordAlreadyExistException();

                }

            } catch (GlobalRecordAlreadyExistException ex) {

                throw ex;

            } catch (FinderException ex) {

            }

            // validate if voucher batch closing

            if (details.getVbStatus().equals("CLOSED")) {

                Collection apVouchers = apVoucherHome.findByVouPostedAndVouVoidAndVbName(EJBCommon.FALSE, EJBCommon.FALSE, details.getVbName(), AD_CMPNY);

                if (!apVouchers.isEmpty()) {

                    throw new GlobalTransactionBatchCloseException();

                }

            }

            // validate if voucher already assigned

            if (details.getVbCode() != null) {

                Collection apVouchers = apVoucherBatch.getApVouchers();

                if (!apVoucherBatch.getVbType().equals(details.getVbType()) && !apVouchers.isEmpty()) {

                    throw new GlobalRecordAlreadyAssignedException();

                }

            }


            if (details.getVbCode() == null) {

                apVoucherBatch = apVoucherBatchHome.create(details.getVbName(), details.getVbDescription(), details.getVbStatus(), details.getVbType(), details.getVbDateCreated(), details.getVbCreatedBy(), details.getVbDepartment(), AD_BRNCH, AD_CMPNY);

            } else {

                apVoucherBatch.setVbName(details.getVbName());
                apVoucherBatch.setVbDescription(details.getVbDescription());
                apVoucherBatch.setVbStatus(details.getVbStatus());
                apVoucherBatch.setVbType(details.getVbType());
                apVoucherBatch.setVbDepartment(details.getVbDepartment());

            }

            return apVoucherBatch.getVbCode();

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


    public void deleteApVbEntry(Integer VB_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("ApVoucherBatchEntryControllerBean deleteApVbEntry");

        try {

            LocalApVoucherBatch apVoucherBatch = apVoucherBatchHome.findByPrimaryKey(VB_CODE);

            Collection apVouchers = apVoucherBatch.getApVouchers();

            if (!apVouchers.isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();

            }

            em.remove(apVoucherBatch);

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

        Debug.print("ApVoucherBatchEntryControllerBean ejbCreate");

    }

    // private methods


}