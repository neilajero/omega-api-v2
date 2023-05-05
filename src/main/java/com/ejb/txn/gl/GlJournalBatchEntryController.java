package com.ejb.txn.gl;

import com.ejb.exception.global.*;
import com.util.gl.GlJournalBatchDetails;

import jakarta.ejb.Local;

@Local
public interface GlJournalBatchEntryController {

    GlJournalBatchDetails getGlJbByJbCode(java.lang.Integer JB_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer saveGlJbEntry(GlJournalBatchDetails details, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyDeletedException, GlobalTransactionBatchCloseException;

    void deleteGlJbEntry(java.lang.Integer JB_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}