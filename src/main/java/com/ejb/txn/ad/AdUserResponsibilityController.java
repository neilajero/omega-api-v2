package com.ejb.txn.ad;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdUserDetails;
import com.util.ad.AdUserResponsibilityDetails;

import jakarta.ejb.Local;
import java.util.*;

@Local
public interface AdUserResponsibilityController {

    ArrayList getAdRsAll(Integer companyCode);

    ArrayList getAdUrByUsrCode(Integer userCode, Integer companyCode) throws GlobalNoRecordFoundException;

    AdUserDetails getAdUsrByUsrCode(Integer userCode, Integer companyCode);

    void addAdUrEntry(AdUserResponsibilityDetails details, Integer userCode, String responsibilityName, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void updateAdUrEntry(AdUserResponsibilityDetails details, Integer userCode, String responsibilityName, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void deleteAdUrEntry(Integer userResponsibilityCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException;

}