package com.ejb.txn.ap;

import com.ejb.exception.ap.ApCHKCheckNumberNotUniqueException;
import com.ejb.exception.ap.ApCHKVoucherHasNoWTaxCodeException;
import com.ejb.exception.ap.ApVOUOverapplicationNotAllowedException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.ap.ApCheckDetails;
import com.util.mod.ap.ApModCheckDetails;
import com.util.mod.ap.ApModSupplierDetails;

import jakarta.ejb.Local;

@Local
public interface ApPaymentEntryController {

    java.util.ArrayList getGlFcAllWithDefault(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    ApModCheckDetails getApChkByChkCode(java.lang.Integer CHK_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ApModSupplierDetails getApSplBySplSupplierCode(java.lang.String SPL_SPPLR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getApVpsBySplSupplierCode(java.lang.String SPL_SPPLR_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer saveApChkEntry(ApCheckDetails details, java.lang.String BA_NM, java.lang.String FC_NM, java.lang.String SPL_SPPLR_CODE, java.lang.String CB_NM, java.util.ArrayList avList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, ApCHKCheckNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, ApVOUOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, ApCHKVoucherHasNoWTaxCodeException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException;

    void deleteApChkEntry(java.lang.Integer CHK_CODE, java.lang.String AD_USR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdApprovalNotifiedUsersByChkCode(java.lang.Integer CHK_CODE, java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableApCheckBatch(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApOpenCbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    byte getAdPrfApUseSupplierPulldown(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc1(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc2(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc3(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc4(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc5(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc6(java.lang.Integer AD_CMPNY);

    java.lang.String getAdPrfApDefaultCheckDate(java.lang.Integer AD_CMPNY);

    double getFrRateByFrNameAndFrDate(java.lang.String FC_NM, java.util.Date CONVERSION_DATE, java.lang.Integer AD_CMPNY) throws GlobalConversionDateNotExistException;

    short getInvGpCostPrecisionUnit(java.lang.Integer AD_CMPNY);

}