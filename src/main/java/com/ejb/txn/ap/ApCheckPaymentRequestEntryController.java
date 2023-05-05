package com.ejb.txn.ap;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.ap.ApTaxCodeDetails;
import com.util.ap.ApVoucherDetails;
import com.util.mod.ap.ApModSupplierDetails;
import com.util.mod.ap.ApModVoucherDetails;

import jakarta.ejb.Local;


@Local
public interface ApCheckPaymentRequestEntryController {

    java.util.ArrayList getGlFcAllWithDefault(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdPytAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApTcAll(java.lang.Integer AD_CMPNY);

    ApTaxCodeDetails getApTcByTcName(java.lang.String TC_NM, java.lang.Integer AD_CMPNY);

    java.lang.String getApNoneTc(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApWtcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApSplAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApOpenVbAll(java.lang.String DPRTMNT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApOpenPlBySplSupplierCode(java.lang.String SPL_SPPLR_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getInvLocAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvUomByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY);

    double getInvIiUnitCostByIiNameAndUomName(java.lang.String II_NM, java.lang.String UOM_NM, java.lang.Integer AD_CMPNY);

    short getAdPrfApJournalLineNumber(java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableApVoucherBatch(java.lang.Integer AD_CMPNY);

    ApModVoucherDetails getApVouByVouCode(java.lang.Integer VOU_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ApModSupplierDetails getApSplBySplSupplierCode(java.lang.String SPL_SPPLR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getApDrBySplSupplierCodeAndTcNameAndWtcNameAndVouBillAmount(java.lang.String SPL_SPPLR_CODE, java.lang.String TC_NM, java.lang.String WTC_NM, double VOU_BLL_AMNT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer saveApVouEntry(ApVoucherDetails details, java.lang.String PYT_NM, java.lang.String TC_NM, java.lang.String WTC_NM, java.lang.String FC_NM, java.lang.String SPL_SPPLR_CODE, java.lang.String VB_NM, java.util.ArrayList drList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalBranchAccountNumberInvalidException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalReferenceNumberNotUniqueException;

    java.lang.Integer saveApVouVliEntry(ApVoucherDetails details, java.lang.String PYT_NM, java.lang.String TC_NM, java.lang.String WTC_NM, java.lang.String FC_NM, java.lang.String SPL_SPPLR_CODE, java.lang.String VB_NM, java.util.ArrayList vliList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalReferenceNumberNotUniqueException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalMiscInfoIsRequiredException;

    java.lang.Integer saveApVouPlEntry(ApVoucherDetails details, java.lang.String PYT_NM, java.lang.String TC_NM, java.lang.String WTC_NM, java.lang.String FC_NM, java.lang.String SPL_SPPLR_CODE, java.lang.String VB_NM, java.util.ArrayList plList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalPaymentTermInvalidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalReferenceNumberNotUniqueException, GlobalBranchAccountNumberInvalidException;

    void deleteApVouEntry(java.lang.Integer VOU_CODE, java.lang.String AD_USR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    java.lang.Integer generateApVoucher(java.lang.String DESC, java.util.ArrayList drList, java.lang.Integer PR_CODE, java.lang.String CRTD_BY, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdApprovalNotifiedUsersByVouCode(java.lang.Integer VOU_CODE, java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpCostPrecisionUnit(java.lang.Integer AD_CMPNY);

    byte getAdPrfApUseSupplierPulldown(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvLitByCstLitName(java.lang.String CST_LIT_NAME, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    double getFrRateByFrNameAndFrDate(java.lang.String FC_NM, java.util.Date CONVERSION_DATE, java.lang.Integer AD_CMPNY) throws GlobalConversionDateNotExistException;

}