package com.ejb.txnsync.inv;

import com.ejb.restfulapi.sync.inv.models.InvUnitOfMeasureSyncRequest;
import com.ejb.restfulapi.sync.inv.models.InvUnitOfMeasureSyncResponse;
import jakarta.ejb.Local;

@Local
public interface InvUnitOfMeasureSyncController {
    String[] getInvUnitOfMeasuresAll(Integer companyCode, String companyShortName);
    void setInvUnitOfMeasuresAllSuccessConfirmation(Integer companyCode, String companyShortName);

    InvUnitOfMeasureSyncResponse getInvUnitOfMeasuresAll(InvUnitOfMeasureSyncRequest request);

    InvUnitOfMeasureSyncResponse setInvUnitOfMeasuresAllSuccessConfirmation(InvUnitOfMeasureSyncRequest request);
}