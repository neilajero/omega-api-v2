/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArInvoiceBatchCopyControllerBean
 * @created May 27, 2004, 8:14 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ar.LocalArInvoiceBatchHome;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.entities.ar.LocalArInvoiceBatch;
import com.ejb.exception.global.GlobalBatchCopyInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalTransactionBatchCloseException;
import com.util.mod.ar.ArModInvoiceDetails;
import com.util.Debug;
import com.util.EJBContextClass;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;

@Stateless(name = "ArInvoiceBatchCopyControllerEJB")
public class ArInvoiceBatchCopyControllerBean extends EJBContextClass implements ArInvoiceBatchCopyController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArInvoiceBatchHome arInvoiceBatchHome;

    public void executeArInvcBatchCopy(ArrayList list, String IB_NM_TO, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalTransactionBatchCloseException, GlobalBatchCopyInvalidException {
        Debug.print("ArInvoiceBatchCopyControllerBean executeArInvcBatchCopy");
        try {

            // find invoice batch to

            LocalArInvoiceBatch arInvoiceBatchTo = arInvoiceBatchHome.findByIbName(IB_NM_TO, AD_BRNCH, AD_CMPNY);

            // validate if batch to is closed

            if (arInvoiceBatchTo.getIbStatus().equals("CLOSED")) {

                throw new GlobalTransactionBatchCloseException();
            }

            for (Object o : list) {

                LocalArInvoice arInvoice = arInvoiceHome.findByPrimaryKey((Integer) o);

                if (!arInvoice.getArInvoiceBatch().getIbType().equals(arInvoiceBatchTo.getIbType())) {

                    throw new GlobalBatchCopyInvalidException();
                }

                arInvoice.getArInvoiceBatch().dropArInvoice(arInvoice);
                arInvoiceBatchTo.addArInvoice(arInvoice);
            }

        } catch (GlobalTransactionBatchCloseException | GlobalBatchCopyInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArInvByIbName(String IB_NM, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArInvoiceBatchCopyControllerBean getArInvByIbName");
        ArrayList list = new ArrayList();
        try {

            Collection arInvoices = arInvoiceHome.findByIbName(IB_NM, AD_CMPNY);

            if (arInvoices.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object invoice : arInvoices) {

                LocalArInvoice arInvoice = (LocalArInvoice) invoice;

                ArModInvoiceDetails mdetails = new ArModInvoiceDetails();
                mdetails.setInvCode(arInvoice.getInvCode());
                mdetails.setInvCstCustomerCode(arInvoice.getArCustomer().getCstCustomerCode());
                mdetails.setInvDate(arInvoice.getInvDate());
                mdetails.setInvNumber(arInvoice.getInvNumber());
                mdetails.setInvReferenceNumber(arInvoice.getInvReferenceNumber());
                mdetails.setInvAmountDue(arInvoice.getInvAmountDue());
                mdetails.setInvAmountPaid(arInvoice.getInvAmountPaid());
                mdetails.setInvCreditMemo(arInvoice.getInvCreditMemo());

                list.add(mdetails);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArInvoiceBatchCopyControllerBean ejbCreate");
    }

}