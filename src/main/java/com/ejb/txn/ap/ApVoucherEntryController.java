package com.ejb.txn.ap;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.global.*;
import com.ejb.exception.gl.*;
import com.util.ap.ApTaxCodeDetails;
import com.util.ap.ApVoucherDetails;
import com.util.mod.ap.ApModSupplierDetails;
import com.util.mod.ap.ApModVoucherDetails;

import jakarta.ejb.Local;

import java.util.ArrayList;

@Local
public interface ApVoucherEntryController {


    ApTaxCodeDetails getApTcByTcName(String TC_NM, Integer companyCode);

    String getApNoneTc(Integer companyCode);

    ArrayList getApSplTradeAll(Integer branchCode, Integer companyCode);

    ArrayList getApOpenVbAll(String DPRTMNT, Integer branchCode, Integer companyCode);

    ArrayList getApOpenPlByPoDcmntNmbr(String PO_DCMNT_NMBR, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException;

    ArrayList getApOpenPlBySplSupplierCode(String SPL_SPPLR_CODE, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException;

    ArrayList getApOpenPl(Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException;

    byte getInvIiIsVatReliefByIiName(String itemName, Integer companyCode);

    short getAdPrfApJournalLineNumber(Integer companyCode);

    byte getAdPrfEnableApVoucherBatch(Integer companyCode);

    ApModVoucherDetails getApVouByVouCode(Integer VOU_CODE, Integer companyCode) throws GlobalNoRecordFoundException;

    ApModSupplierDetails getApSplBySplSupplierCode(String SPL_SPPLR_CODE, Integer companyCode) throws GlobalNoRecordFoundException;

    ArrayList getApDrBySplSupplierCodeAndTcNameAndWtcNameAndVouBillAmount(String SPL_SPPLR_CODE, String TC_NM, String WTC_NM, double VOU_BLL_AMNT, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException;

    Integer saveApVouEntry(ApVoucherDetails details, String PYT_NM, String PYT_NM2, String TC_NM, String WTC_NM, String FC_NM, String SPL_SPPLR_CODE, String VB_NM, ArrayList drList, boolean isDraft, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalBranchAccountNumberInvalidException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalReferenceNumberNotUniqueException;

    Integer saveApVouVliEntry(ApVoucherDetails details, String PYT_NM, String PYT_NM2, String TC_NM, String WTC_NM, String FC_NM, String SPL_SPPLR_CODE, String VB_NM, ArrayList vliList, boolean isDraft, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalReferenceNumberNotUniqueException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalMiscInfoIsRequiredException;

    Integer saveApVouPlEntry(ApVoucherDetails details, String PYT_NM, String TC_NM, String WTC_NM, String FC_NM, String SPL_SPPLR_CODE, String VB_NM, ArrayList plList, boolean isDraft, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalPaymentTermInvalidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalReferenceNumberNotUniqueException, GlobalBranchAccountNumberInvalidException;

    void deleteApVouEntry(Integer VOU_CODE, String AD_USR, Integer companyCode) throws GlobalRecordAlreadyDeletedException;

    ArrayList getAdApprovalNotifiedUsersByVouCode(Integer VOU_CODE, Integer companyCode);

    ArrayList getInvLitByCstLitName(String CST_LIT_NAME, Integer companyCode) throws GlobalNoRecordFoundException;

}