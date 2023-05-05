package com.ejb.txn.ap;

import com.ejb.exception.global.GlobalBatchCopyInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalTransactionBatchCloseException;

import jakarta.ejb.Local;


@Local
public interface ApVoucherBatchCopyController {

    java.util.ArrayList getApOpenVbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    void executeApVouBatchCopy(java.util.ArrayList list, java.lang.String VB_NM_TO, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalTransactionBatchCloseException, GlobalBatchCopyInvalidException;

    java.util.ArrayList getApVouByVbName(java.lang.String VB_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}