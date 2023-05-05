package com.ejb.txnapi.ad;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ad.models.AmountLimitRequest;
import com.util.ad.AdAmountLimitDetails;

import jakarta.ejb.Local;
import java.util.ArrayList;

@Local
public interface AdAmountLimitApiController {
    ArrayList getAdCalByAdcCode(Integer approvalDocumentCode, Integer companyCode) throws GlobalNoRecordFoundException;

    void addAdCalEntry(AdAmountLimitDetails details, Integer approvalDocumentCode,
                       Integer approvalCoaLineCode, Integer companyCode, String companyShortName)
            throws GlobalRecordAlreadyExistException;

    OfsApiResponse saveAmountLimit(AmountLimitRequest request);

}