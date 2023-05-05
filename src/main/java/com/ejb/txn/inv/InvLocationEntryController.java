package com.ejb.txn.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.exception.inv.InvLocFixedAssetException;
import com.util.inv.InvLocationDetails;

import jakarta.ejb.Local;

@Local
public interface InvLocationEntryController {
    java.util.ArrayList getAllInvLocName(Integer AD_CMPNY);

    java.util.ArrayList getAdLvInvLocationBranchAll(java.lang.Integer AD_CMPNY);

    void saveInvLocEntry(InvLocationDetails details, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyExistException, InvLocFixedAssetException;

    InvLocationDetails getInvLocByLocCode(java.lang.Integer LOC_CODE, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    void deleteInvLocEntry(java.lang.Integer LOC_CODE, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}