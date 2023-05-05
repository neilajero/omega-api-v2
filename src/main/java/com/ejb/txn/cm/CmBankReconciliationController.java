package com.ejb.txn.cm;

import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.cm.CmAdjustmentDetails;
import com.util.mod.ad.AdModBankAccountDetails;

import jakarta.ejb.Local;

@Local
public interface CmBankReconciliationController {

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlFcAllWithDefault(java.lang.Integer AD_CMPNY);

    AdModBankAccountDetails getAdBaByBaName(java.lang.String BA_NM, java.util.Date RCNCL_DT, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    java.util.ArrayList getCmDepositInTransitByBaName(java.lang.String BA_NM, java.util.Date RCNCL_DT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getCmOutstandingCheckByBaName(java.lang.String BA_NM, java.util.Date RCNCL_DT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    void executeCmBankReconciliation(double OPNNG_BLNC, double ENDNG_BLNC, java.lang.String BA_NM, CmAdjustmentDetails adjustmentDetails, CmAdjustmentDetails interestDetails, CmAdjustmentDetails serviceChargeDetails, java.util.ArrayList depositInTransitList, java.util.ArrayList outstandingCheckList, java.util.Date reconcileDate, boolean autoAdjust, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalRecordAlreadyDeletedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException;

    void recomputeAccountBalanceByBankCode(java.lang.String BA_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}