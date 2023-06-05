package com.ejb.txnsync.inv;

import jakarta.ejb.Local;

@Local
public interface InvItemSyncController {
    int getInvItemAllNewLength(String branchCodeName, String itemLocation, Integer companyCode);
    int getInvItemAllNewLengthAnyLoc(String branchCodeName, Integer companyCode);
    int getInvItemAllUpdatedLength(String branchCodeName, String itemLocation, Integer companyCode);
    int getInvItemAllUpdatedLengthAnyLoc(String branchCodeName, Integer companyCode);
    String getInvItemAllDownloadedAnyLoc(String branchCodeName, Integer companyCode);
    String getInvItemAllDownloaded(String branchCodeName, String itemLocation, Integer companyCode);
    String getInvItemAllDownloadedWithUnitCost(String branchCodeName, String itemLocation, Integer companyCode);
    String[] getInvItemAllNewAndUpdatedAnyLoc(String branchCodeName, Integer companyCode);
    String[] getInvItemAllNewAndUpdated(String branchCodeName, String itemLocation, Integer companyCode);
    String[] getInvItemAllNewAndUpdatedPosUs(String branchCodeName, String itemLocation, Integer companyCode);
    String[] getInvItemAllNewAndUpdatedWithUnitPrice(String branchCodeName, String itemLocation, Integer companyCode);
    int setInvItemAllNewAndUpdatedSuccessConfirmationAnyLoc(String branchCodeName, Integer companyCode);
    int setInvItemAllNewAndUpdatedSuccessConfirmation(String branchCodeName, String itemLocation, Integer companyCode);
    String[] getAllMemoLineInvoice(String branchCodeName, Integer companyCode);
    String[] getAllConsolidatedItems(String[] ItemStr, Integer companyCode);
    String[] getAllPostedArSoMatchedInvoice(String branchCodeName, Integer companyCode);
    int setInvAdjustment(String[] ADJ, String branchCodeName, Integer companyCode);
    int setArBillingInvoiceAndCreditMemos(String[] cmTxn, String[] invTxn,String branchCodeName, Integer companyCode);
    String[] convertBulkToLooseUom(Double qty, String II_NM, String BULK_UOM_NM, String LOOSE_UOM_NM, Integer companyCode);
    int setApReceivingItem(String[] RR, String branchCodeName, Integer companyCode);
    int setApReceivingItemPoCondition(String[] RR, String branchCodeName, Integer companyCode);
    int setApPurchaseOrder(String[] PO, Boolean isReceiving, String branchCodeName, Integer companyCode);
    String setInvBST(String[] BST, String branchCodeName, Integer companyCode);
    String[] getInvBranchStockTransferAllIncoming(String branchCodeName, Integer companyCode);
    String[] getInvStockOnHand(String branchCodeName,String itemLocation, Integer companyCode);
    String[] getInvStockOnHandWithExpiryDate(String branchCodeName,String itemLocation, Integer companyCode);
    String[] getInvStockOnHandOnly(String[] invUploadOrig, Integer companyCode);
    String[] getLastPo(String invoiceDate, String location, String[] invUploadOrig, String branchCode, Integer companyCode);
    String[] getLastPoPerItem(String invoiceDate, String location, String[] invUploadOrig, String branchCode, Integer companyCode);
    String getCheckInsufficientStocks(String invoiceDateFrom, String invoiceDateTo, String location, String user, String invAdjAccount,
                                      String transactionType, String branchCode, Integer companyCode);
    String[] getSoMatchedInvBOAllPosted(String DateFrom, String DateTo, String branchCodeName, Integer companyCode);
    String[] getApPOAllPostedAndUnreceived(String branchCodeName, Integer companyCode);
    String getPaymentTermsAll(Integer companyCode);
    String getTaxCodesAll(Integer companyCode);
    String getFunctionalCurrenciesAll(Integer companyCode);
}