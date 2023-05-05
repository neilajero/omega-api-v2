package com.ejb.txn.ar;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;

import jakarta.ejb.Local;

@Local
public interface ArInvoiceBatchSubmitController {

    byte getAdPrfEnableArInvoiceBatch(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArInvByCriteria(java.util.HashMap criteria, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.String ORDER_BY, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void executeArInvBatchSubmit(java.lang.Integer INV_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}