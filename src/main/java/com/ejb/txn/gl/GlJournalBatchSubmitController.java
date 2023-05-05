package com.ejb.txn.gl;

import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;

import jakarta.ejb.Local;

@Local
public interface GlJournalBatchSubmitController {

    java.util.ArrayList getGlJcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlJsAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlFcAllWithDefault(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlOpenJbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableGlJournalBatch(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlJrByCriteria(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    void executeGlJrBatchSubmit(java.lang.Integer JR_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDatePeriodClosedException;

}