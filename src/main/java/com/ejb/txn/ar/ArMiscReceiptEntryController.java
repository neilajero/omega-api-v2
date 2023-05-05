package com.ejb.txn.ar;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.ar.ArReceiptDetails;
import com.util.ar.ArStandardMemoLineDetails;
import com.util.ar.ArTaxCodeDetails;
import com.util.mod.ar.ArModCustomerDetails;
import com.util.mod.ar.ArModReceiptDetails;

import jakarta.ejb.Local;

@Local
public interface ArMiscReceiptEntryController {

    ArTaxCodeDetails getArTcByTcName(java.lang.String TC_NM, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvUomByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY);

    double getIiSalesPriceByInvCstCustomerCodeAndIiNameAndUomName(java.lang.String CST_CSTMR_CODE, java.lang.String II_NM, java.lang.String UOM_NM, java.lang.Integer AD_CMPNY);

    short getAdPrfArInvoiceLineNumber(java.lang.Integer AD_CMPNY);

    ArModReceiptDetails getArRctByRctCode(java.lang.Integer RCT_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ArModCustomerDetails getArCstByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ArStandardMemoLineDetails getArSmlByCstCstmrCodeSmlNm(java.lang.String CST_CSTMR_CODE, java.lang.String SML_NM, int AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ArStandardMemoLineDetails getArSmlBySmlName(java.lang.String SML_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer saveArRctEntry(ArReceiptDetails details, java.lang.String BA_NM, java.lang.String TC_NM, java.lang.String WTC_NM, java.lang.String FC_NM, java.lang.String CST_CSTMR_CODE, java.lang.String RB_NM, java.util.ArrayList ilList, boolean isDraft, java.lang.String SLP_SLSPRSN_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException, GlobalRecordAlreadyAssignedException;

    java.lang.Integer saveArRctIliEntry(ArReceiptDetails details, java.lang.String BA_NM, java.lang.String BA_CRD1_NM, java.lang.String BA_CRD2_NM, java.lang.String BA_CRD3_NM, java.lang.String TC_NM, java.lang.String WTC_NM, java.lang.String FC_NM, java.lang.String CST_CSTMR_CODE, java.lang.String RB_NM, java.util.ArrayList iliList, boolean isDraft, java.lang.String SLP_SLSPRSN_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, GlobalRecordAlreadyAssignedException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException, GlobalMiscInfoIsRequiredException, GlobalRecordInvalidException;

    void deleteArRctEntry(java.lang.Integer RCT_CODE, java.lang.String AD_USR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdApprovalNotifiedUsersByRctCode(java.lang.Integer RCT_CODE, java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableArMiscReceiptBatch(java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableInvShift(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArOpenRbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

    byte getAdPrfArDisableSalesPrice(java.lang.Integer AD_CMPNY);

    java.lang.String getInvItemClassByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    double getInvIiSalesPriceByIiNameAndUomName(java.lang.String II_NM, java.lang.String UOM_NM, java.lang.Integer AD_CMPNY);

    java.lang.String getInvIiClassByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getInvLitByCstLitName(java.lang.String CST_LIT_NAME, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    double getFrRateByFrNameAndFrDate(java.lang.String FC_NM, java.util.Date CONVERSION_DATE, java.lang.Integer AD_CMPNY) throws GlobalConversionDateNotExistException;

    boolean getArTraceMisc(java.lang.String II_NAME, java.lang.Integer AD_CMPNY);

    byte getAdPrfArUseCustomerPulldown(java.lang.Integer AD_CMPNY);

}