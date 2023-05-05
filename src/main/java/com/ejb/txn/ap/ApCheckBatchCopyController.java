package com.ejb.txn.ap;

import com.ejb.exception.global.GlobalBatchCopyInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalTransactionBatchCloseException;

import jakarta.ejb.Local;


@Local
public interface ApCheckBatchCopyController {

    java.util.ArrayList getApOpenCbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    void executeApChkBatchCopy(java.util.ArrayList list, java.lang.String CB_NM_TO, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalTransactionBatchCloseException, GlobalBatchCopyInvalidException;

    java.util.ArrayList getApChkByCbName(java.lang.String CB_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}