package com.ejb.txnsync.ar;

import com.ejb.restfulapi.sync.ar.models.ArCustomerSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArCustomerSyncResponse;
import jakarta.ejb.Local;

@Local
public interface ArCustomerSyncController {

    String[] getArCSTAreaAll(Integer companyCode, String companyShortName);

    String[] getArSoPostedAllByCstArea(String customerArea, Integer companyCode, String companyShortName);

    int getArCustomersAllNewLength(Integer branchCode, Integer companyCode, String companyShortName);

    int getArCustomersAllUpdatedLength(Integer branchCode, Integer companyCode, String companyShortName);

    String[] getArSalespersonAll(Integer branchCode, Integer companyCode, String companyShortName);

    String[] getArCustomerDraftBalances(String[] CST_CSTMR_CODES, Integer companyCode, String companyShortName);

    String[] getArCustomersNameCodeAddressSlp(Integer branchCode, Integer companyCode, String companyShortName);

    String[] getArCustomersAllNewAndUpdated(Integer branchCode, Integer companyCode, String companyShortName);

    String[] getArCustomersAllNewAndUpdatedWithSalesperson(Integer branchCode, Integer companyCode, String companyShortName);

    String[] getArCustomersAllNewAndUpdatedWithCustomerClass(Integer branchCode, Integer companyCode, String companyShortName);

    String getArCustomersBalanceAllDownloaded(Integer branchCode, Integer companyCode, String companyShortName);

    void setArCustomersAllNewAndUpdatedSuccessConfirmation(Integer branchCode, Integer companyCode, String companyShortName);

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