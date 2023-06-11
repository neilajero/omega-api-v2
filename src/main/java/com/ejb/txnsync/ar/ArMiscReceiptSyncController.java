package com.ejb.txnsync.ar;

import com.ejb.restfulapi.sync.ar.models.ArMiscReceiptSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArMiscReceiptSyncResponse;
import jakarta.ejb.Local;

@Local
public interface ArMiscReceiptSyncController {

    ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoid(ArMiscReceiptSyncRequest request);

    ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoidUS(ArMiscReceiptSyncRequest request);

    ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoidWithExpiryDate(ArMiscReceiptSyncRequest request);

    ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoidWithExpiryDateEnableItemAutoBuild(ArMiscReceiptSyncRequest request);

    ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoidWithExpiryDateAndEntries(ArMiscReceiptSyncRequest request);

    ArMiscReceiptSyncResponse setArMiscReceiptAllNewAndVoidWithSalesperson(ArMiscReceiptSyncRequest request);

}