package com.ejb.txn.ap;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.ap.ApVOUOverapplicationNotAllowedException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.ap.ApVoucherDetails;
import com.util.mod.ap.ApModVoucherDetails;

import jakarta.ejb.Local;


@Local
public interface ApDebitMemoEntryController {

    java.util.ArrayList getInvUomByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY);

    double getInvIiUnitCostByIiNameAndUomName(java.lang.String VOU_DM_VCHR_NMBR, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.String UOM_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getAdPrfApJournalLineNumber(java.lang.Integer AD_CMPNY);

    ApModVoucherDetails getApVouByVouCode(java.lang.Integer VOU_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getApDrByVouDmVoucherNumberAndVouBillAmountAndSplSupplierCodeBrCode(java.lang.String VOU_DM_VCHR_NMBR, double VOU_BLL_AMNT, java.lang.String SPL_SPPLR_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, ApVOUOverapplicationNotAllowedException;

    java.lang.Integer saveApVouEntry(ApVoucherDetails details, java.lang.String SPL_SPPLR_CODE, java.lang.String VB_NM, java.util.ArrayList drList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalNoRecordFoundException, GlobalDocumentNumberNotUniqueException, GlobalBranchAccountNumberInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, ApVOUOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException;

    java.lang.Integer saveApVouVliEntry(ApVoucherDetails details, java.lang.String SPL_SPPLR_CODE, java.lang.String VB_NM, java.util.ArrayList vliList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalNoRecordFoundException, GlobalDocumentNumberNotUniqueException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, ApVOUOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException;

    java.lang.Integer saveApVouVliEntryMobile(ApVoucherDetails details, java.lang.String SPL_SPPLR_CODE, java.lang.String VB_NM, java.util.ArrayList vliList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalNoRecordFoundException, GlobalDocumentNumberNotUniqueException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, ApVOUOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException;

    void deleteApVouEntry(java.lang.Integer VOU_CODE, java.lang.String AD_USR, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdApprovalNotifiedUsersByVouCode(java.lang.Integer VOU_CODE, java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableApDebitMemoBatch(java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

    byte getAdPrfApUseSupplierPulldown(java.lang.Integer AD_CMPNY);

    java.lang.String getApSplNameBySplSupplierCode(java.lang.String SPL_SPPLR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}