/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApVoucherBatchCopyControllerBean
 * @created May 26, 2004, 9:13 AM
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
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.dao.ap.LocalApVoucherBatch;
import com.ejb.dao.ap.LocalApVoucherBatchHome;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.exception.global.GlobalBatchCopyInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalTransactionBatchCloseException;
import com.util.mod.ap.ApModVoucherDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApVoucherBatchCopyControllerEJB")
public class ApVoucherBatchCopyControllerBean extends EJBContextClass implements ApVoucherBatchCopyController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalApVoucherBatchHome apVoucherBatchHome;

    public ArrayList getApOpenVbAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApVoucherBatchCopyControllerBean getApOpenVbAll");

        ArrayList list = new ArrayList();

        try {

            Collection apVoucherBatches = apVoucherBatchHome.findOpenVbAll(AD_BRNCH, AD_CMPNY);

            for (Object voucherBatch : apVoucherBatches) {

                LocalApVoucherBatch apVoucherBatch = (LocalApVoucherBatch) voucherBatch;

                list.add(apVoucherBatch.getVbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void executeApVouBatchCopy(ArrayList list, String VB_NM_TO, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalTransactionBatchCloseException, GlobalBatchCopyInvalidException {

        Debug.print("ApVoucherBatchCopyControllerBean executeApVouBatchCopy");

        try {

            // find voucher batch to

            LocalApVoucherBatch apVoucherBatchTo = apVoucherBatchHome.findByVbName(VB_NM_TO, AD_BRNCH, AD_CMPNY);

            // validate if batch to is closed

            if (apVoucherBatchTo.getVbStatus().equals("CLOSED")) {

                throw new GlobalTransactionBatchCloseException();
            }

            for (Object o : list) {

                LocalApVoucher apVoucher = apVoucherHome.findByPrimaryKey((Integer) o);

                if (!apVoucher.getApVoucherBatch().getVbType().equals(apVoucherBatchTo.getVbType())) {

                    throw new GlobalBatchCopyInvalidException();
                }

                apVoucher.getApVoucherBatch().dropApVoucher(apVoucher);
                apVoucherBatchTo.addApVoucher(apVoucher);
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

    public ArrayList getApVouByVbName(String VB_NM, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApVoucherBatchCopyControllerBean getApVouByVbName");

        ArrayList list = new ArrayList();

        try {

            Collection apVouchers = apVoucherHome.findByVbName(VB_NM, AD_CMPNY);

            if (apVouchers.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object voucher : apVouchers) {

                LocalApVoucher apVoucher = (LocalApVoucher) voucher;

                ApModVoucherDetails mdetails = new ApModVoucherDetails();
                mdetails.setVouCode(apVoucher.getVouCode());
                mdetails.setVouSplSupplierCode(apVoucher.getApSupplier().getSplSupplierCode());
                mdetails.setVouDate(apVoucher.getVouDate());
                mdetails.setVouDocumentNumber(apVoucher.getVouDocumentNumber());
                mdetails.setVouReferenceNumber(apVoucher.getVouReferenceNumber());

                if (apVoucher.getVouDebitMemo() == EJBCommon.FALSE) {

                    mdetails.setVouAmountDue(apVoucher.getVouAmountDue());

                } else {

                    mdetails.setVouAmountDue(apVoucher.getVouBillAmount());
                }

                mdetails.setVouAmountPaid(apVoucher.getVouAmountPaid());
                mdetails.setVouDebitMemo(apVoucher.getVouDebitMemo());

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

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApVoucherBatchCopyControllerBean getGlFcPrecisionUnit");

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

        Debug.print("ApVoucherBatchCopyControllerBean ejbCreate");
    }
}