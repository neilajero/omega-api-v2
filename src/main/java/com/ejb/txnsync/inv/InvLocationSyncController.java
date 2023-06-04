package com.ejb.txnsync.inv;

import com.ejb.restfulapi.sync.inv.models.InvLocationSyncRequest;
import com.ejb.restfulapi.sync.inv.models.InvLocationSyncResponse;
import jakarta.ejb.Local;

@Local
public interface InvLocationSyncController {
    int getInvLocationAllNewLength(Integer branchCode, Integer companyCode, String companyShortName);
    int getInvLocationAllUpdatedLength(Integer branchCode, Integer companyCode, String companyShortName);
    String[] getInvLocationAllNewAndUpdated(Integer branchCode, Integer companyCode, String companyShortName);
    void setInvLocationAllNewAndUpdatedSuccessConfirmation(Integer branchCode, Integer companyCode, String companyShortName);
    InvLocationSyncResponse getInvLocationAllNewLength(InvLocationSyncRequest request);
    InvLocationSyncResponse getInvLocationAllUpdatedLength(InvLocationSyncRequest request);
    InvLocationSyncResponse getInvLocationAllNewAndUpdated(InvLocationSyncRequest request);
    InvLocationSyncResponse setInvLocationAllNewAndUpdatedSuccessConfirmation(InvLocationSyncRequest request);
}