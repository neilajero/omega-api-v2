package com.ejb.txnsync.ar;

import com.ejb.restfulapi.sync.ar.models.ArMiscReceiptSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArMiscReceiptSyncResponse;
import jakarta.ejb.Local;

@Local
public interface ArMiscReceiptSyncController {

    int setArReceiptAllNewAndVoid(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier);

    int setArMiscReceiptAllNewAndVoid(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName);

    int setArMiscReceiptAllNewAndVoidUS(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName);

    int setArMiscReceiptAllNewAndVoidWithExpiryDate(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName);

    int setArMiscReceiptAllNewAndVoidWithExpiryDateEnableItemAutoBuild(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName);

    int setArMiscReceiptAllNewAndVoidWithExpiryDateAndEntries(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName);

    int setArMiscReceiptAllNewAndVoidWithSalesperson(String[] receipts, String[] voidReceipt, Integer branchCode, Integer companyCode, String cashier, String companyShortName);

    ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoid(ArMiscReceiptSyncRequest request);

    ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoidUS(ArMiscReceiptSyncRequest request);

    ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoidWithExpiryDate(ArMiscReceiptSyncRequest request);

    ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoidWithExpiryDateEnableItemAutoBuild(ArMiscReceiptSyncRequest request);

    ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoidWithExpiryDateAndEntries(ArMiscReceiptSyncRequest request);

    ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoidWithSalesperson(ArMiscReceiptSyncRequest request);

}