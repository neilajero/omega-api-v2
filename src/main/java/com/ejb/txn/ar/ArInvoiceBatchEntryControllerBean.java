/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArInvoiceBatchEntryControllerBean
 * @created May 20, 2004, 3:52 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ar;

import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArInvoiceBatch;
import com.ejb.dao.ar.LocalArInvoiceBatchHome;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.exception.global.GlobalTransactionBatchCloseException;
import com.util.ar.ArInvoiceBatchDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ArInvoiceBatchEntryControllerEJB")
public class ArInvoiceBatchEntryControllerBean extends EJBContextClass implements ArInvoiceBatchEntryController {

    @EJB
    public PersistenceBeanClass em;

    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArInvoiceBatchHome arInvoiceBatchHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;

    public ArInvoiceBatchDetails getArIbByIbCode(Integer IB_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArInvoiceBatchEntryControllerBean getArIbByIbCode");

        try {

            LocalArInvoiceBatch arInvoiceBatch = null;

            try {

                arInvoiceBatch = arInvoiceBatchHome.findByPrimaryKey(IB_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArInvoiceBatchDetails details = new ArInvoiceBatchDetails();
            details.setIbCode(arInvoiceBatch.getIbCode());
            details.setIbName(arInvoiceBatch.getIbName());
            details.setIbDescription(arInvoiceBatch.getIbDescription());
            details.setIbStatus(arInvoiceBatch.getIbStatus());
            details.setIbType(arInvoiceBatch.getIbType());
            details.setIbDateCreated(arInvoiceBatch.getIbDateCreated());
            details.setIbCreatedBy(arInvoiceBatch.getIbCreatedBy());

            // FOR ELF
            details.setIbAllowInterest(arInvoiceBatch.getIbAllowInterest());
            details.setIbInterestRate(arInvoiceBatch.getIbInterestRate());

            return details;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveArIbEntry(ArInvoiceBatchDetails details, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyDeletedException, GlobalTransactionBatchCloseException, GlobalRecordAlreadyAssignedException {

        Debug.print("ArInvoiceBatchEntryControllerBean saveArIbEntry");

        LocalArInvoiceBatch arInvoiceBatch = null;

        try {

            // validate if invoice batch is already deleted

            try {

                if (details.getIbCode() != null) {

                    arInvoiceBatch = arInvoiceBatchHome.findByPrimaryKey(details.getIbCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if invoice batch exists

            try {

                LocalArInvoiceBatch arExistingInvoiceBatch = arInvoiceBatchHome.findByIbName(details.getIbName(), AD_BRNCH, AD_CMPNY);

                if (details.getIbCode() == null || details.getIbCode() != null && !arExistingInvoiceBatch.getIbCode().equals(details.getIbCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (GlobalRecordAlreadyExistException ex) {

                throw ex;

            } catch (FinderException ex) {

            }

            // validate if invoice batch closing

            if (details.getIbStatus().equals("CLOSED")) {

                Collection arInvoices = arInvoiceHome.findByInvPostedAndInvVoidAndIbName(EJBCommon.FALSE, EJBCommon.FALSE, details.getIbName(), AD_CMPNY);

                if (!arInvoices.isEmpty()) {

                    throw new GlobalTransactionBatchCloseException();
                }
            }

            // validate if invoice already assigned

            if (details.getIbCode() != null) {

                Collection arInvoices = arInvoiceBatch.getArInvoices();

                if (!arInvoiceBatch.getIbType().equals(details.getIbType()) && !arInvoices.isEmpty()) {

                    throw new GlobalRecordAlreadyAssignedException();
                }
            }

            if (details.getIbCode() == null) {

                arInvoiceBatch = arInvoiceBatchHome.create(details.getIbName(), details.getIbDescription(), details.getIbStatus(), details.getIbType(), details.getIbDateCreated(), details.getIbCreatedBy(), AD_BRNCH, AD_CMPNY);

                // additional columns to add

                // FOR ELF
                arInvoiceBatch.setIbAllowInterest(details.getIbAllowInterest());
                arInvoiceBatch.setIbInterestRate(details.getIbInterestRate());

            } else {

                arInvoiceBatch.setIbName(details.getIbName());
                arInvoiceBatch.setIbDescription(details.getIbDescription());
                arInvoiceBatch.setIbStatus(details.getIbStatus());
                arInvoiceBatch.setIbType(details.getIbType());

                // additional columns to add

                // FOR ELF
                arInvoiceBatch.setIbAllowInterest(details.getIbAllowInterest());
                arInvoiceBatch.setIbInterestRate(details.getIbInterestRate());
            }

            return arInvoiceBatch.getIbCode();

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

    public void deleteArIbEntry(Integer IB_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("ArInvoiceBatchEntryControllerBean deleteArIbEntry");

        try {

            LocalArInvoiceBatch arInvoiceBatch = arInvoiceBatchHome.findByPrimaryKey(IB_CODE);

            Collection arInvoices = arInvoiceBatch.getArInvoices();

            if (!arInvoices.isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

            //        	arInvoiceBatch.entityRemove();
            em.remove(arInvoiceBatch);

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

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArInvoiceBatchEntryControllerBean getGlFcPrecisionUnit");

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

        Debug.print("ArInvoiceBatchEntryControllerBean ejbCreate");
    }

    // private methods

}