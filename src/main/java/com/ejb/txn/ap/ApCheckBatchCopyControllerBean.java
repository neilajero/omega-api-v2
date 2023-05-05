/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApCheckBatchCopyControllerBean
 * @created May 27, 2004, 8:10 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ap;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.entities.ap.LocalApCheckBatch;
import com.ejb.dao.ap.LocalApCheckBatchHome;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.exception.global.GlobalBatchCopyInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalTransactionBatchCloseException;
import com.util.mod.ap.ApModCheckDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApCheckBatchCopyControllerEJB")
public class ApCheckBatchCopyControllerBean extends EJBContextClass implements ApCheckBatchCopyController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApCheckBatchHome apCheckBatchHome;

    public ArrayList getApOpenCbAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApCheckBatchCopyControllerBean getApOpenCbAll");

        ArrayList list = new ArrayList();

        try {

            Collection apCheckBatches = apCheckBatchHome.findOpenCbAll(AD_BRNCH, AD_CMPNY);

            for (Object checkBatch : apCheckBatches) {

                LocalApCheckBatch apCheckBatch = (LocalApCheckBatch) checkBatch;

                list.add(apCheckBatch.getCbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void executeApChkBatchCopy(ArrayList list, String CB_NM_TO, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalTransactionBatchCloseException, GlobalBatchCopyInvalidException {

        Debug.print("ApCheckBatchCopyControllerBean executeApChkBatchCopy");

        try {

            // find check batch to

            LocalApCheckBatch apCheckBatchTo = apCheckBatchHome.findByCbName(CB_NM_TO, AD_BRNCH, AD_CMPNY);

            // validate if batch to is closed

            if (apCheckBatchTo.getCbStatus().equals("CLOSED")) {

                throw new GlobalTransactionBatchCloseException();
            }

            for (Object o : list) {

                LocalApCheck apCheck = apCheckHome.findByPrimaryKey((Integer) o);

                if (!apCheck.getApCheckBatch().getCbType().equals(apCheckBatchTo.getCbType())) {

                    throw new GlobalBatchCopyInvalidException();
                }

                apCheck.getApCheckBatch().dropApCheck(apCheck);
                apCheckBatchTo.addApCheck(apCheck);
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

    public ArrayList getApChkByCbName(String CB_NM, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApCheckBatchCopyControllerBean getApChkByCbName");

        try {

            ArrayList chkList = new ArrayList();

            Collection apChecks = null;

            try {

                apChecks = apCheckHome.findByCbName(CB_NM, AD_CMPNY);

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            if (apChecks.isEmpty()) throw new GlobalNoRecordFoundException();

            for (Object check : apChecks) {

                LocalApCheck apCheck = (LocalApCheck) check;

                ApModCheckDetails mdetails = new ApModCheckDetails();
                mdetails.setChkCode(apCheck.getChkCode());
                mdetails.setChkType(apCheck.getChkType());
                mdetails.setChkDate(apCheck.getChkDate());
                mdetails.setChkNumber(apCheck.getChkNumber());
                mdetails.setChkDocumentNumber(apCheck.getChkDocumentNumber());
                mdetails.setChkAmount(apCheck.getChkAmount());
                mdetails.setChkReleased(apCheck.getChkReleased());
                mdetails.setChkSplSupplierCode(apCheck.getApSupplier().getSplSupplierCode());
                mdetails.setChkBaName(apCheck.getAdBankAccount().getBaName());

                chkList.add(mdetails);
            }

            return chkList;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApCheckBatchCopyControllerBean getGlFcPrecisionUnit");

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

        Debug.print("ApCheckBatchCopyControllerBean ejbCreate");
    }
}