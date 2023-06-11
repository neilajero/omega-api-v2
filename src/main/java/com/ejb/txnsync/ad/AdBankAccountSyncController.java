package com.ejb.txnsync.ad;

import com.ejb.restfulapi.sync.ad.models.BankAccountSyncRequest;
import com.ejb.restfulapi.sync.ad.models.BankAccountSyncResponse;
import jakarta.ejb.Local;

@Local
public interface AdBankAccountSyncController {

    BankAccountSyncResponse getAllNewLength(BankAccountSyncRequest request);

    BankAccountSyncResponse getAllUpdatedLength(BankAccountSyncRequest request);

    BankAccountSyncResponse getAllNewAndUpdated(BankAccountSyncRequest request);

    BankAccountSyncResponse setAllNewAndUpdatedSuccessConfirmation(BankAccountSyncRequest request);

}