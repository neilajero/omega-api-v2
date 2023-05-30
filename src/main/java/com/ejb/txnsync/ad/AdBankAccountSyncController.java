package com.ejb.txnsync.ad;

import com.ejb.restfulapi.sync.ad.models.BankAccountSyncRequest;
import com.ejb.restfulapi.sync.ad.models.BankAccountSyncResponse;
import jakarta.ejb.Local;

@Local
public interface AdBankAccountSyncController {

    int getAdBankAccountAllNewLength(Integer branchCode, Integer companyCode, String companyShortName);

    int getAdBankAccountAllUpdatedLength(Integer branchCode, Integer companyCode, String companyShortName);

    String[] getAdBankAccountAllNewAndUpdated(Integer branchCode, Integer companyCode, String companyShortName);

    void setAdBankAccountsAllNewAndUpdatedSuccessConfirmation(Integer branchCode, Integer companyCode, String companyShortName);

    BankAccountSyncResponse getAllNewLength(BankAccountSyncRequest request);

    BankAccountSyncResponse getAllUpdatedLength(BankAccountSyncRequest request);

    BankAccountSyncResponse getAllNewAndUpdated(BankAccountSyncRequest request);

    BankAccountSyncResponse setAllNewAndUpdatedSuccessConfirmation(BankAccountSyncRequest request);

}