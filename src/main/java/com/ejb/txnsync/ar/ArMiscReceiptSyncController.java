package com.ejb.txnsync.ar;

import com.ejb.restfulapi.sync.ar.models.ArMiscReceiptSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArMiscReceiptSyncResponse;
import jakarta.ejb.Local;

@Local
public interface ArMiscReceiptSyncController {

    int setArReceiptAllNewAndVoid(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier);

    int setArMiscReceiptAllNewAndVoid(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier);

    int setArMiscReceiptAllNewAndVoidUS(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier);

    int setArMiscReceiptAllNewAndVoidWithExpiryDate(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier);

    int setArMiscReceiptAllNewAndVoidWithExpiryDateEnableItemAutoBuild(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier);

    int setArMiscReceiptAllNewAndVoidWithExpiryDateAndEntries(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier);

    int setArMiscReceiptAllNewAndVoidWithSalesperson(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier);

    ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoid(ArMiscReceiptSyncRequest request);

}