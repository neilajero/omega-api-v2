package com.ejb.txn.ap;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;

import jakarta.ejb.Local;

@Local
public interface ApCheckBatchSubmitController {

    java.util.ArrayList getApSplAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlFcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApOpenCbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableApCheckBatch(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApChkByCriteria(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void executeApChkBatchSubmit(java.lang.Integer CHK_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, AdPRFCoaGlVarianceAccountNotFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    byte getAdPrfApUseSupplierPulldown(java.lang.Integer AD_CMPNY);

}