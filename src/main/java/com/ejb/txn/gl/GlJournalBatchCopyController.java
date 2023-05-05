package com.ejb.txn.gl;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalTransactionBatchCloseException;

import jakarta.ejb.Local;

@Local
public interface GlJournalBatchCopyController {

    java.util.ArrayList getGlOpenJbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    void executeGlJrBatchCopy(java.util.ArrayList list, java.lang.String JB_NM_TO, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalTransactionBatchCloseException;

    java.util.ArrayList getGlJrByJbName(java.lang.String JB_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}