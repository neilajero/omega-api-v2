package com.ejb.txn.ap;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.ap.ApRINoPurchaseOrderLinesFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.exception.inv.InvTagSerialNumberAlreadyExistException;
import com.util.ap.ApTaxCodeDetails;
import com.util.mod.ap.ApModPurchaseOrderDetails;
import com.util.mod.ap.ApModSupplierDetails;
import com.util.ap.ApPurchaseOrderDetails;

import jakarta.ejb.Local;

@Local
public interface ApReceivingItemEntryController {

    java.util.ArrayList getGlFcAllWithDefault(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdPytAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApTcAll(java.lang.Integer AD_CMPNY);

    ApTaxCodeDetails getApTcByTcName(java.lang.String TC_NM, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApRcvOpenPo(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvLocAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvUomByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY);

    boolean getInvTraceMisc(java.lang.String II_NAME, java.lang.Integer AD_CMPNY);

    ApModPurchaseOrderDetails getApPoByPoRcvPoNumberAndSplSupplierCodeAndAdBranch(java.lang.String PO_RCV_PO_NMBR, java.lang.String SPL_SPPLR_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, ApRINoPurchaseOrderLinesFoundException;

    ApModPurchaseOrderDetails getApByPoNumberAndAdBranch(String PO_NMBR, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException, ApRINoPurchaseOrderLinesFoundException;

    double getInvIiUnitCostByIiNameAndUomName(java.lang.String II_NM, java.lang.String UOM_NM, java.lang.Integer AD_CMPNY);

    double getInvUmcByIiNameAndUomName(java.lang.String II_NM, java.lang.String UOM_NM, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdUsrAll(java.lang.Integer AD_CMPNY);

    ApModPurchaseOrderDetails getApPoByPoCode(java.lang.Integer PO_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ApModSupplierDetails getApSplBySplSupplierCode(java.lang.String SPL_SPPLR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer saveApPoEntryMobile(ApPurchaseOrderDetails details, java.lang.String PYT_NM, java.lang.String FC_NM, java.util.ArrayList plList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalTransactionAlreadyLockedException, GlobalInventoryDateException, GlobalTransactionAlreadyVoidPostedException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalMiscInfoIsRequiredException, InvTagSerialNumberAlreadyExistException;

    java.lang.Integer saveApPoEntry(ApPurchaseOrderDetails details, boolean recalculateJournal, java.lang.String PYT_NM, java.lang.String TC_NM, java.lang.String FC_NM, java.lang.String SPL_SPPLR_CODE, java.util.ArrayList plList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvTagMissingException, GlobalInvTagExistingException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalTransactionAlreadyLockedException, GlobalInventoryDateException, GlobalTransactionAlreadyVoidPostedException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalMiscInfoIsRequiredException, GlobalRecordInvalidException, GlobalReferenceNumberNotUniqueException, InvTagSerialNumberAlreadyExistException;

    void deleteApPoEntry(java.lang.Integer PO_CODE, java.lang.String AD_USR, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdApprovalNotifiedUsersByPoCode(java.lang.Integer PO_CODE, java.lang.Integer AD_CMPNY);

    short getAdPrfApJournalLineNumber(java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpCostPrecisionUnit(java.lang.Integer AD_CMPNY);

    byte getAdPrfApUseSupplierPulldown(java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableGlJournalBatch(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlOpenJbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    double getFrRateByFrNameAndFrDate(java.lang.String FC_NM, java.util.Date CONVERSION_DATE, java.lang.Integer AD_CMPNY) throws GlobalConversionDateNotExistException;

    java.util.ArrayList getAdLvPurchaseRequisitionMisc1(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc2(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc3(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc4(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc5(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc6(java.lang.Integer AD_CMPNY);
}