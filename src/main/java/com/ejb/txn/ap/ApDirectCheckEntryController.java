package com.ejb.txn.ap;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.ap.ApCHKCheckNumberNotUniqueException;
import com.ejb.exception.gl.*;
import com.ejb.exception.global.*;
import com.util.ap.ApCheckDetails;
import com.util.ap.ApTaxCodeDetails;
import com.util.mod.ap.ApModCheckDetails;
import com.util.mod.ap.ApModSupplierDetails;

import jakarta.ejb.Local;

@Local
public interface ApDirectCheckEntryController {

    java.util.ArrayList getGlFcAllWithDefault(Integer companyCode);

    java.util.ArrayList getAdPytAll(Integer companyCode);

    java.util.ArrayList getAdBaAll(Integer branchCode, Integer companyCode);

    java.util.ArrayList getApTcAll(Integer companyCode);

    ApTaxCodeDetails getApTcByTcName(String TC_NM, Integer companyCode);

    java.util.ArrayList getApWtcAll(Integer companyCode);

    short getAdPrfApJournalLineNumber(Integer companyCode);

    ApModCheckDetails getApChkByChkCode(Integer CHK_CODE, Integer companyCode) throws GlobalNoRecordFoundException;

    ApModSupplierDetails getApSplBySplSupplierCode(String SPL_SPPLR_CODE, Integer companyCode) throws GlobalNoRecordFoundException;

    java.util.ArrayList getApDrBySplSupplierCodeAndTcNameAndWtcNameAndChkBillAmountAndBaName(String SPL_SPPLR_CODE, String TC_NM, String WTC_NM, double CHK_BLL_AMNT, String BA_NM, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException;

    Integer saveApChkEntry(ApCheckDetails details, String PYT_NM, String BA_NM, String TC_NM, String WTC_NM, String FC_NM, String SPL_SPPLR_CODE, String CB_NM, java.util.ArrayList drList, boolean isDraft, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, ApCHKCheckNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalBranchAccountNumberInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException;

    Integer saveApChkVliEntry(ApCheckDetails details, String BA_NM, String TC_NM, String WTC_NM, String FC_NM, String SPL_SPPLR_CODE, String CB_NM, java.util.ArrayList vliList, boolean isDraft, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, ApCHKCheckNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException;

    void deleteApChkEntry(Integer CHK_CODE, String AD_USR, Integer companyCode) throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(Integer companyCode);

    java.util.ArrayList getAdApprovalNotifiedUsersByChkCode(Integer CHK_CODE, Integer companyCode);

    byte getAdPrfEnableApCheckBatch(Integer companyCode);

    java.util.ArrayList getApOpenCbAll(String DPRTMNT, Integer branchCode, Integer companyCode);

    java.util.ArrayList getInvLocAll(Integer companyCode);

    java.util.ArrayList getInvUomByIiName(String II_NM, Integer companyCode);

    double getInvIiUnitCostByIiNameAndUomName(String II_NM, String UOM_NM, Integer companyCode);

    short getInvGpQuantityPrecisionUnit(Integer companyCode);

    byte getAdPrfApUseSupplierPulldown(Integer companyCode);

    java.util.ArrayList getInvLitByCstLitName(String CST_LIT_NAME, Integer companyCode) throws GlobalNoRecordFoundException;

    double getFrRateByFrNameAndFrDate(String FC_NM, java.util.Date CONVERSION_DATE, Integer companyCode) throws GlobalConversionDateNotExistException;

    short getInvGpCostPrecisionUnit(Integer companyCode);

    java.util.ArrayList getAdUsrAll(Integer companyCode);

    boolean getInvTraceMisc(String II_NAME, Integer companyCode);

}