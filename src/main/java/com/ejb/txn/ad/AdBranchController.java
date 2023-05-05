package com.ejb.txn.ad;

import jakarta.ejb.Local;
import java.util.ArrayList;

import com.ejb.exception.ad.AdBRBranchCodeAlreadyExistsException;
import com.ejb.exception.ad.AdBRBranchNameAlreadyExistsException;
import com.ejb.exception.ad.AdBRHeadQuarterAlreadyExistsException;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.util.ad.AdBranchDetails;

@Local
public interface AdBranchController {

    ArrayList getAdBrAll(Integer companyCode) throws GlobalNoRecordFoundException;

    void addAdBrEntry(AdBranchDetails details, String coaAccountNumber, Integer companyCode) throws AdBRHeadQuarterAlreadyExistsException, AdBRBranchCodeAlreadyExistsException, AdBRBranchNameAlreadyExistsException, GlobalAccountNumberInvalidException;

    void updateAdBrEntry(AdBranchDetails details, String coaAccountNumber, Integer companyCode) throws AdBRHeadQuarterAlreadyExistsException, AdBRBranchCodeAlreadyExistsException, AdBRBranchNameAlreadyExistsException, GlobalAccountNumberInvalidException;

    void deleteAdBrEntry(Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

    short getGlFcPrecisionUnit(Integer companyCode);

}