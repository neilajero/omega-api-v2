package com.ejb.txn.ar;

import com.ejb.exception.global.*;
import com.util.ar.ArReceiptBatchDetails;

import jakarta.ejb.Local;

@Local
public interface ArReceiptBatchEntryController {

    ArReceiptBatchDetails getArRbByRbCode(java.lang.Integer RB_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer saveArRbEntry(ArReceiptBatchDetails details, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyDeletedException, GlobalTransactionBatchCloseException, GlobalRecordAlreadyAssignedException;

    void deleteArRbEntry(java.lang.Integer RB_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}