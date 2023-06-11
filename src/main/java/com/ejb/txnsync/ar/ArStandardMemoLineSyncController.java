package com.ejb.txnsync.ar;

import com.ejb.restfulapi.sync.ar.models.ArStandardMemoLineSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArStandardMemoLineSyncResponse;
import jakarta.ejb.Local;

@Local
public interface ArStandardMemoLineSyncController {

    ArStandardMemoLineSyncResponse getArStandardMemoLineAllNewLength(ArStandardMemoLineSyncRequest request);

    ArStandardMemoLineSyncResponse getArStandardMemoLineHomeAllUpdatedLength(ArStandardMemoLineSyncRequest request);

    ArStandardMemoLineSyncResponse getArStandardMemoLineAllNewAndUpdated(ArStandardMemoLineSyncRequest request);

    ArStandardMemoLineSyncResponse setArStandardMemoLineAllNewAndUpdatedSuccessConfirmation(ArStandardMemoLineSyncRequest request);
}