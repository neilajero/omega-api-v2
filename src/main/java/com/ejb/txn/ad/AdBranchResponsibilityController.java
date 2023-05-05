package com.ejb.txn.ad;

import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdBranchResponsibilityDetails;

import jakarta.ejb.Local;
import java.util.ArrayList;

@Local
public interface AdBranchResponsibilityController {

    ArrayList getAdBranchResponsibilityAll(int responsibilityCode, Integer companyCode) throws GlobalNoRecordFoundException;

    void addAdBranchResponsibility(AdBranchResponsibilityDetails details, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void updateAdBranchResponsibility(AdBranchResponsibilityDetails details, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void deleteAdBranchResponsibility(Integer branchResponsibilityCode, Integer companyCode) throws GlobalRecordAlreadyAssignedException, GlobalRecordAlreadyDeletedException;

    LocalAdBranch getAdBranchByBrCode(Integer BR_CODE) throws GlobalNoRecordFoundException;
}