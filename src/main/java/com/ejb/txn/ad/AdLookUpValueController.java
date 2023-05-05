package com.ejb.txn.ad;

import jakarta.ejb.Local;
import java.util.ArrayList;

import com.ejb.exception.ad.AdLUNoLookUpFoundException;
import com.ejb.exception.ad.AdLVMasterCodeNotFoundException;
import com.ejb.exception.ad.AdLVNoLookUpValueFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdLookUpValueDetails;

@Local
public interface AdLookUpValueController {

    ArrayList getAdLuAll(Integer companyCode) throws GlobalNoRecordFoundException;

    ArrayList getAdLvByLuName(String lookUpName, Integer companyCode) throws AdLVNoLookUpValueFoundException, AdLUNoLookUpFoundException;

    void addAdLvEntry(AdLookUpValueDetails details, String lookUpName, Integer companyCode) throws GlobalRecordAlreadyExistException, AdLVMasterCodeNotFoundException;

    void updateAdLvEntry(AdLookUpValueDetails details, String lookUpName, Integer companyCode) throws GlobalRecordAlreadyExistException, AdLVMasterCodeNotFoundException;

    void deleteAdLvEntry(Integer LV_CODE, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}