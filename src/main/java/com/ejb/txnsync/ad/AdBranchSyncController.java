package com.ejb.txnsync.ad;

import com.ejb.restfulapi.sync.ad.models.BranchSyncRequest;
import com.ejb.restfulapi.sync.ad.models.BranchSyncResponse;
import jakarta.ejb.Local;

@Local
public interface AdBranchSyncController {

    String[] getAdBranchAll(Integer companyCode, String companyShortName);

    String[] getAdBranchAllWithBranchName(Integer companyCode, String companyShortName);

    void setAdBranchAllSuccessConfirmation(Integer companyCode, String companyShortName);

    BranchSyncResponse getAdBranchAll(BranchSyncRequest request);

    BranchSyncResponse getAdBranchAllWithBranchName(BranchSyncRequest request);

    BranchSyncResponse setAdBranchAllSuccessConfirmation(BranchSyncRequest request);

}