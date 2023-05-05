package com.ejb.txn.ar;

import com.ejb.exception.global.GlobalBatchCopyInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalTransactionBatchCloseException;

import jakarta.ejb.Local;

@Local
public interface ArReceiptBatchCopyController {

    void executeArRctBatchCopy(java.util.ArrayList list, java.lang.String RB_NM_TO, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalTransactionBatchCloseException, GlobalBatchCopyInvalidException;

    java.util.ArrayList getArRctByRbName(java.lang.String RB_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}