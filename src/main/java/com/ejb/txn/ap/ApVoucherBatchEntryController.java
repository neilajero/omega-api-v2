package com.ejb.txn.ap;

import com.ejb.exception.global.*;
import com.util.ap.ApVoucherBatchDetails;

import jakarta.ejb.Local;


@Local
public interface ApVoucherBatchEntryController {

    ApVoucherBatchDetails getApVbByVbCode(java.lang.Integer VB_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer saveApVbEntry(ApVoucherBatchDetails details, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyDeletedException, GlobalTransactionBatchCloseException, GlobalRecordAlreadyAssignedException;

    void deleteApVbEntry(java.lang.Integer VB_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}