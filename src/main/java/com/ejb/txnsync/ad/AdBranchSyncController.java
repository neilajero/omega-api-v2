package com.ejb.txnsync.ad;

import com.ejb.restfulapi.sync.ad.models.BranchSyncRequest;
import com.ejb.restfulapi.sync.ad.models.BranchSyncResponse;
import jakarta.ejb.Local;

@Local
public interface AdBranchSyncController {

    BranchSyncResponse getAdBranchAll(BranchSyncRequest request);

    BranchSyncResponse getAdBranchAllWithBranchName(BranchSyncRequest request);

    BranchSyncResponse setAdBranchAllSuccessConfirmation(BranchSyncRequest request);

}