package com.ejb.txnsync.ar;

import com.ejb.restfulapi.sync.ar.models.ArStandardMemoLineSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArStandardMemoLineSyncResponse;
import jakarta.ejb.Local;

@Local
public interface ArStandardMemoLineSyncController {

    int getArStandardMemoLineAllNewLength(Integer branchCode, Integer companyCode, String companyShortName);

    int getArStandardMemoLineHomeAllUpdatedLength(Integer branchCode, Integer companyCode, String companyShortName);

    String[] getArStandardMemoLineAllNewAndUpdated(Integer branchCode, Integer companyCode, String companyShortName);

    void setArStandardMemoLineAllNewAndUpdatedSuccessConfirmation(Integer branchCode, Integer companyCode, String companyShortName);

    ArStandardMemoLineSyncResponse getArStandardMemoLineAllNewLength(ArStandardMemoLineSyncRequest request);

    ArStandardMemoLineSyncResponse getArStandardMemoLineHomeAllUpdatedLength(ArStandardMemoLineSyncRequest request);

    ArStandardMemoLineSyncResponse getArStandardMemoLineAllNewAndUpdated(ArStandardMemoLineSyncRequest request);

    ArStandardMemoLineSyncResponse setArStandardMemoLineAllNewAndUpdatedSuccessConfirmation(ArStandardMemoLineSyncRequest request);
}