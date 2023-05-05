package com.ejb.txnapi.ad;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.restfulapi.OfsApiResponse;

import jakarta.ejb.Local;
import java.util.ArrayList;

@Local
public interface AdBranchApiController {

    OfsApiResponse getAdBrAll(String companyShortName) throws GlobalNoRecordFoundException;
}