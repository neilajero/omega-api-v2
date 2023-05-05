package com.ejb.txn.cm;

import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;

import jakarta.ejb.Local;

@Local
public interface CmFundTransferBatchSubmitController {

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlFcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getCmFtByCriteria(java.util.HashMap criteria, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.String ORDER_BY, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void executeArFtBatchSubmit(java.lang.Integer FT_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}