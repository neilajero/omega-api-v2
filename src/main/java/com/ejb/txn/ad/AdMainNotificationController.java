package com.ejb.txn.ad;

import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;

@Local
public interface AdMainNotificationController {

    int generateLoan(String username, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException, GlJREffectiveDatePeriodClosedException;

    int executeSalesOrderTransactionStatus(String soTransactionStatus, Integer branchCode, Integer companyCode);

    int executeGlRjbUserNotification(Integer userCode, Integer branchCode, Integer companyCode);

    byte getAdPrfEnableArInvoiceInterestGeneration(Integer companyCode);

    int executeGlJrForReversal(Integer branchCode, Integer companyCode);

    int executeArSalesOrdersForProcessing(Integer branchCode, Integer companyCode);

    int executeArJobOrdersForProcessing(Integer branchCode, Integer companyCode);

    int executeApRvNumberOfVouchersToGenerate(Integer userCode, Integer branchCode, Integer companyCode);

    int executeApPrNumberOfPurchaseRequisitionsToGenerate(Integer userCode, Integer branchCode, Integer companyCode);

    int executeApPurchaseReqForCanvass(Integer branchCode, Integer companyCode);

    int executeApCanvassRejected(Integer branchCode, String prLastModifiedBy, Integer companyCode);

    int executeApPurchaseOrderRejected(Integer branchCode, String prCreatedBy, Integer companyCode);

    int executeApPurchaseOrderReceivingRejected(Integer branchCode, String prCreatedBy, Integer companyCode);

    int executeApPurchaseReqRejected(Integer branchCode, String prCreatedBy, Integer companyCode);

    int executeApChecksForRelease(Integer branchCode, String prCreatedBy, Integer companyCode);

    int executeApPaymentRequestDraft(Integer branchCode, Integer companyCode);

    int executeApPaymentRequestGeneration(Integer branchCode, Integer companyCode);

    int executeApPaymentRequestProcessing(Integer branchCode, Integer companyCode);

    int executeApPaymentsForProcessing(Integer branchCode, Integer companyCode);

    int executeApPaymentRequestRejected(Integer branchCode, String vouCreatedBy, Integer companyCode);

    int executeApVoucherForProcessing(Integer branchCode, Integer companyCode);

    int executeApChecksRejected(Integer branchCode, String poCreatedBy, Integer companyCode);

    int executeApDirectChecksRejected(Integer branchCode, String poCreatedBy, Integer companyCode);

    int executeApVoucherRejected(Integer branchCode, String poCreatedBy, Integer companyCode);

    int executeApPurchaseOrdersForGeneration(Integer branchCode, Integer companyCode);

    int executeInvAdjustmentRequestToGenerate(Integer branchCode, Integer companyCode);

    int executeApPurchaseOrdersForPrinting(Integer branchCode, Integer companyCode);

    int executeArCustomerRejected(Integer branchCode, String customerCreatedBy, Integer companyCode);

    int executeArPdcNumberOfInvoicesToGenerate(Integer userCode, Integer branchCode, Integer companyCode);

    int executeArInvoiceForPosting(String invoiceType, Integer branchCode, Integer companyCode);

    int executeArInvoiceCreditMemoForPosting(Integer branchCode, Integer companyCode);

    int executeArReceiptForPosting(String receiptType, Integer branchCode, Integer companyCode);

    int executeAdDocumentToApprove(String approvalQueueDoc, Integer userCode, Integer branchCode, Integer companyCode);

    int executeInvStockTransferUnposted(Integer companyCode);

    int executeArOverDueInvoices(Integer branchCode, Integer companyCode);

    int executeArInvestorBonusAndInterest(Integer branchCode, Integer companyCode);

    int executeArGenerateAccruedInterestIS(Integer branchCode, Integer companyCode);

    int executeApGenerateLoan(Integer branchCode, Integer companyCode);

    int executeArGenerateAccruedInterestTB(Integer branchCode, Integer companyCode);

    int executeArPastDueInvoices(Integer branchCode, Integer companyCode);

    int executeInvBSTOrdersUnserved(Integer branchCode, Integer companyCode);

    int executeChecksForPrinting(Integer branchCode, Integer companyCode);

    int executeAdDocumentInDraft(String documentType, Integer branchCode, Integer companyCode);

}