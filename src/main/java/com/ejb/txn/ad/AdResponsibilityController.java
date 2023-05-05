package com.ejb.txn.ad;

import jakarta.ejb.Local;
import java.util.*;

import com.ejb.entities.ad.LocalAdResponsibility;
import com.ejb.exception.ad.AdRSResponsibilityNotAssignedToBranchException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdResponsibilityDetails;

@Local
public interface AdResponsibilityController {

    ArrayList getAdRsAll(Integer companyCode) throws GlobalNoRecordFoundException;

    ArrayList getAdBrAll(Integer companyCode) throws GlobalNoRecordFoundException;

    ArrayList getAdBrResAll(Integer responsibilityCode, Integer companyCode) throws GlobalNoRecordFoundException;

    LocalAdResponsibility getAdResByResNm(String responsibilityName, Integer companyCode) throws GlobalNoRecordFoundException;

    LocalAdResponsibility getAdResByResCode(Integer responsibilityCode, Integer companyCode) throws GlobalNoRecordFoundException;

    void addAdRsEntry(AdResponsibilityDetails details, ArrayList branchList, Integer companyCode) throws GlobalRecordAlreadyExistException, AdRSResponsibilityNotAssignedToBranchException;

    void copyAdRsEntry(AdResponsibilityDetails details, ArrayList branchList, Integer companyCode) throws Exception;

    void updateAdRsEntry(AdResponsibilityDetails details, ArrayList branchList, Integer companyCode) throws GlobalRecordAlreadyExistException, AdRSResponsibilityNotAssignedToBranchException;

    void deleteAdRsEntry(Integer responsibilityCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;
}