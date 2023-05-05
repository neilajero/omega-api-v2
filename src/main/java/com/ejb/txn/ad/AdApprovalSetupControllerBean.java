package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;

import com.ejb.entities.ad.LocalAdApproval;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.util.EJBContextClass;
import com.util.ad.AdApprovalDetails;
import com.util.Debug;

@Stateless(name = "AdApprovalSetupControllerEJB")
public class AdApprovalSetupControllerBean extends EJBContextClass implements AdApprovalSetupController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdApprovalHome adApprovalHome;

    public AdApprovalDetails getAdApr(Integer companyCode) {

        Debug.print("AdApprovalControllerBean getAdApr");

        LocalAdApproval adApproval;
        try {

            adApproval = adApprovalHome.findByAprAdCompany(companyCode);

            AdApprovalDetails details = new AdApprovalDetails();
            details.setAprEnableGlJournal(adApproval.getAprEnableGlJournal());
            details.setAprEnableApVoucher(adApproval.getAprEnableApVoucher());
            details.setAprEnableApVoucherDepartment(adApproval.getAprEnableApVoucherDepartment());
            details.setAprEnableApDebitMemo(adApproval.getAprEnableApDebitMemo());
            details.setAprEnableApCheck(adApproval.getAprEnableApCheck());
            details.setAprEnableArInvoice(adApproval.getAprEnableArInvoice());
            details.setAprEnableArCreditMemo(adApproval.getAprEnableArCreditMemo());
            details.setAprEnableArReceipt(adApproval.getAprEnableArReceipt());
            details.setAprEnableCmFundTransfer(adApproval.getAprEnableCmFundTransfer());
            details.setAprEnableCmAdjustment(adApproval.getAprEnableCmAdjustment());
            details.setAprApprovalQueueExpiration(adApproval.getAprApprovalQueueExpiration());
            details.setAprEnableInvAdjustment(adApproval.getAprEnableInvAdjustment());
            details.setAprEnableInvBuild(adApproval.getAprEnableInvBuild());

            details.setAprEnableApPurReq(adApproval.getAprEnableApPurReq());
            details.setAprEnableApCanvass(adApproval.getAprEnableApCanvass());

            details.setAprEnableInvAdjustmentRequest(adApproval.getAprEnableInvAdjustmentRequest());

            details.setAprEnableInvBranchStockTransferOrder(adApproval.getAprEnableInvBranchStockTransferOrder());
            details.setAprEnableApCheckPaymentRequest(adApproval.getAprEnableApCheckPaymentRequest());
            details.setAprEnableApCheckPaymentRequestDepartment(adApproval.getAprEnableApCheckPaymentRequestDepartment());
            details.setAprEnableInvStockTransfer(adApproval.getAprEnableInvStockTransfer());
            details.setAprEnableApPurchaseOrder(adApproval.getAprEnableApPurchaseOrder());
            details.setAprEnableApReceivingItem(adApproval.getAprEnableApReceivingItem());
            details.setAprEnableInvBranchStockTransfer(adApproval.getAprEnableInvBranchStockTransfer());
            details.setAprEnableArSalesOrder(adApproval.getAprEnableArSalesOrder());
            details.setAprEnableArCustomer(adApproval.getAprEnableArCustomer());


            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }
    }

    public Integer saveAdAprEntry(AdApprovalDetails details, Integer companyCode) {

        Debug.print("AdApprovalControllerBean saveAdAprEntry");

        try {

            LocalAdApproval adApproval = null;

            // update approval setup

            adApproval = adApprovalHome.findByAprAdCompany(companyCode);

            adApproval.setAprEnableGlJournal(details.getAprEnableGlJournal());
            adApproval.setAprEnableApVoucher(details.getAprEnableApVoucher());
            adApproval.setAprEnableApVoucherDepartment(details.getAprEnableApVoucherDepartment());
            adApproval.setAprEnableApDebitMemo(details.getAprEnableApDebitMemo());
            adApproval.setAprEnableApCheck(details.getAprEnableApCheck());
            adApproval.setAprEnableArInvoice(details.getAprEnableArInvoice());
            adApproval.setAprEnableArCreditMemo(details.getAprEnableArCreditMemo());
            adApproval.setAprEnableArReceipt(details.getAprEnableArReceipt());
            adApproval.setAprEnableCmFundTransfer(details.getAprEnableCmFundTransfer());
            adApproval.setAprEnableCmAdjustment(details.getAprEnableCmAdjustment());
            adApproval.setAprApprovalQueueExpiration(details.getAprApprovalQueueExpiration());
            adApproval.setAprEnableInvAdjustment(details.getAprEnableInvAdjustment());
            adApproval.setAprEnableInvBuild(details.getAprEnableInvBuild());

            adApproval.setAprEnableApPurReq(details.getAprEnableApPurReq());
            adApproval.setAprEnableApCanvass(details.getAprEnableApCanvass());
            adApproval.setAprEnableInvAdjustmentRequest(details.getAprEnableInvAdjustmentRequest());
            adApproval.setAprEnableApCheckPaymentRequest(details.getAprEnableApCheckPaymentRequest());
            adApproval.setAprEnableApCheckPaymentRequestDepartment(details.getAprEnableApCheckPaymentRequestDepartment());
            adApproval.setAprEnableInvBranchStockTransferOrder(details.getAprEnableInvBranchStockTransferOrder());
            adApproval.setAprEnableInvStockTransfer(details.getAprEnableInvStockTransfer());
            adApproval.setAprEnableApPurchaseOrder(details.getAprEnableApPurchaseOrder());
            adApproval.setAprEnableApReceivingItem(details.getAprEnableApReceivingItem());
            adApproval.setAprEnableInvBranchStockTransfer(details.getAprEnableInvBranchStockTransfer());
            adApproval.setAprEnableArSalesOrder(details.getAprEnableArSalesOrder());
            adApproval.setAprEnableArCustomer(details.getAprEnableArCustomer());

            return adApproval.getAprCode();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdApprovalControllerBean ejbCreate");

    }

}