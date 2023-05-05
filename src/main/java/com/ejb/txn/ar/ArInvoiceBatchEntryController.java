package com.ejb.txn.ar;

import com.ejb.exception.global.*;
import com.util.ar.ArInvoiceBatchDetails;

import jakarta.ejb.Local;

@Local
public interface ArInvoiceBatchEntryController {

    ArInvoiceBatchDetails getArIbByIbCode(java.lang.Integer IB_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer saveArIbEntry(ArInvoiceBatchDetails details, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyDeletedException, GlobalTransactionBatchCloseException, GlobalRecordAlreadyAssignedException;

    short getGlFcPrecisionUnit(Integer AD_CMPNY);

    void deleteArIbEntry(java.lang.Integer IB_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}