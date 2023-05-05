package com.ejb.txn.ad;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdLogDetails;

import jakarta.ejb.Local;
import java.util.ArrayList;
import java.util.Date;

@Local
public interface AdLogController {

    ArrayList getAdLogAllByLogModuleAndLogModuleKey(String LOG_MDL, Integer LOG_MDL_KY, Integer companyCode) throws GlobalNoRecordFoundException;

    void addAdLogEntry(AdLogDetails details, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void addAdLogEntry(Integer LOG_MDL_KY, String LOG_MDL, Date LOG_DT, String LOG_USRNM, String LOG_ACTN, String LOG_DESC, Integer companyCode) throws GlobalRecordAlreadyExistException;

}