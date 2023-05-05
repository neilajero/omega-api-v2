package com.ejb.txn.ar;

import com.ejb.exception.global.GlobalBatchCopyInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalTransactionBatchCloseException;

import jakarta.ejb.Local;


@Local
public interface ArInvoiceBatchCopyController {

    void executeArInvcBatchCopy(java.util.ArrayList list, java.lang.String IB_NM_TO, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalTransactionBatchCloseException, GlobalBatchCopyInvalidException;

    java.util.ArrayList getArInvByIbName(java.lang.String IB_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}