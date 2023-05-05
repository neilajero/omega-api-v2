package com.ejb.txn.ap;

import com.ejb.exception.global.*;
import com.util.ap.ApCheckBatchDetails;

import jakarta.ejb.Local;


@Local
public interface ApCheckBatchEntryController {

    java.util.ArrayList getAdLvDEPARTMENT(java.lang.Integer AD_CMPNY);

    ApCheckBatchDetails getApCbByCbCode(java.lang.Integer VB_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer saveApCbEntry(ApCheckBatchDetails details, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyDeletedException, GlobalTransactionBatchCloseException, GlobalRecordAlreadyAssignedException;

    void deleteApCbEntry(java.lang.Integer VB_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}