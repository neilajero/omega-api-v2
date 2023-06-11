package com.ejb.txnsync.ar;

import com.ejb.restfulapi.sync.ar.models.ArCustomerSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArCustomerSyncResponse;
import jakarta.ejb.Local;

@Local
public interface ArCustomerSyncController {

    ArCustomerSyncResponse getArCSTAreaAll(ArCustomerSyncRequest request);

    ArCustomerSyncResponse getArSoPostedAllByCstArea(ArCustomerSyncRequest request);

    ArCustomerSyncResponse getArCustomersAllNewLength(ArCustomerSyncRequest request);

    ArCustomerSyncResponse getArCustomersAllUpdatedLength(ArCustomerSyncRequest request);

    ArCustomerSyncResponse getArSalespersonAll(ArCustomerSyncRequest request);

    ArCustomerSyncResponse getArCustomerDraftBalances(ArCustomerSyncRequest request);

    ArCustomerSyncResponse getArCustomersNameCodeAddressSlp(ArCustomerSyncRequest request);

    ArCustomerSyncResponse getArCustomersAllNewAndUpdated(ArCustomerSyncRequest request);

    ArCustomerSyncResponse getArCustomersAllNewAndUpdatedWithSalesperson(ArCustomerSyncRequest request);

    ArCustomerSyncResponse getArCustomersAllNewAndUpdatedWithCustomerClass(ArCustomerSyncRequest request);

    ArCustomerSyncResponse getArCustomersBalanceAllDownloaded(ArCustomerSyncRequest request);

    ArCustomerSyncResponse setArCustomersAllNewAndUpdatedSuccessConfirmation(ArCustomerSyncRequest request);
}