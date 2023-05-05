/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArReceiptBatchCopyControllerBean
 * @created May 27, 2004, 8:27 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ar.LocalArReceiptBatchHome;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.entities.ar.LocalArReceiptBatch;
import com.ejb.exception.global.GlobalBatchCopyInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalTransactionBatchCloseException;
import com.util.mod.ar.ArModReceiptDetails;
import com.util.Debug;
import com.util.EJBContextClass;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;

@Stateless(name = "ArReceiptBatchCopyControllerEJB")
public class ArReceiptBatchCopyControllerBean extends EJBContextClass implements ArReceiptBatchCopyController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalArReceiptBatchHome arReceiptBatchHome;

    public void executeArRctBatchCopy(ArrayList list, String RB_NM_TO, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalTransactionBatchCloseException, GlobalBatchCopyInvalidException {
        Debug.print("ArReceiptBatchCopyControllerBean executeArRctBatchCopy");
        try {

            // find receipt batch to

            LocalArReceiptBatch arReceiptBatchTo = arReceiptBatchHome.findByRbName(RB_NM_TO, AD_BRNCH, AD_CMPNY);

            // validate if batch to is closed

            if (arReceiptBatchTo.getRbStatus().equals("CLOSED")) {
                throw new GlobalTransactionBatchCloseException();
            }

            for (Object o : list) {

                LocalArReceipt arReceipt = arReceiptHome.findByPrimaryKey((Integer) o);

                if (!arReceipt.getArReceiptBatch().getRbType().equals(arReceiptBatchTo.getRbType())) {

                    throw new GlobalBatchCopyInvalidException();
                }

                arReceipt.getArReceiptBatch().dropArReceipt(arReceipt);
                arReceiptBatchTo.addArReceipt(arReceipt);
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

    public ArrayList getArRctByRbName(String RB_NM, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArReceiptBatchCopyControllerBean getArRctByRbName");
        try {

            ArrayList rctList = new ArrayList();

            Collection arReceipts = null;

            try {

                arReceipts = arReceiptHome.findByRbName(RB_NM, AD_CMPNY);

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            if (arReceipts.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object receipt : arReceipts) {

                LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                ArModReceiptDetails mdetails = new ArModReceiptDetails();
                mdetails.setRctCode(arReceipt.getRctCode());
                mdetails.setRctType(arReceipt.getRctType());
                mdetails.setRctDate(arReceipt.getRctDate());
                mdetails.setRctNumber(arReceipt.getRctNumber());
                mdetails.setRctAmount(arReceipt.getRctAmount());
                mdetails.setRctCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode());
                mdetails.setRctBaName(arReceipt.getAdBankAccount().getBaName());

                rctList.add(mdetails);
            }

            return rctList;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {
        Debug.print("ArReceiptBatchCopyControllerBean getGlFcPrecisionUnit");
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

        Debug.print("ArReceiptBatchCopyControllerBean ejbCreate");
    }

}