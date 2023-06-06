package com.ejb.txn.ap;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.SendEmailDetails;
import com.util.ap.ApPurchaseOrderDetails;
import com.util.ap.ApTaxCodeDetails;
import com.util.mod.ap.ApModPurchaseOrderDetails;
import com.util.mod.ap.ApModSupplierDetails;
import jakarta.ejb.Local;

import java.util.ArrayList;

@Local
public interface ApPurchaseOrderEntryController {

    java.util.ArrayList getApSplAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdPytAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdUsrAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc1(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc2(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc3(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc4(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc5(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvPurchaseRequisitionMisc6(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApTcAll(java.lang.Integer AD_CMPNY);

    ApTaxCodeDetails getApTcByTcName(java.lang.String TC_NM, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlFcAllWithDefault(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvLocAll(java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpCostPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getAdPrfApJournalLineNumber(java.lang.Integer AD_CMPNY);

    boolean getInvTraceMisc(java.lang.String II_NAME, java.lang.Integer AD_CMPNY);

    ApModPurchaseOrderDetails getApPoByPoCode(java.lang.Integer PO_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    Integer saveApPoEntry(ApPurchaseOrderDetails details, String PYT_NM, String TC_NM, String FC_NM, String SPL_SPPLR_CODE,
                          ArrayList plList, boolean isDraft, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException,
            GlobalPaymentTermInvalidException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalTransactionAlreadyLockedException,
            GlobalInventoryDateException, GlobalTransactionAlreadyVoidPostedException, GlobalBranchAccountNumberInvalidException,
            AdPRFCoaGlVarianceAccountNotFoundException;

    Integer saveApPoEntry2(ApPurchaseOrderDetails details, String PYT_NM, String TC_NM, String FC_NM, String SPL_SPPLR_CODE,
                           ArrayList plList, boolean isDraft, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException,
            GlobalPaymentTermInvalidException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalTransactionAlreadyLockedException,
            GlobalInventoryDateException, GlobalTransactionAlreadyVoidPostedException, GlobalBranchAccountNumberInvalidException,
            AdPRFCoaGlVarianceAccountNotFoundException;

    Integer saveApPoSyncEntry(ApPurchaseOrderDetails details, String PYT_NM, String TC_NM, String FC_NM, String SPL_SPPLR_CODE,
                              ArrayList plList, boolean isDraft, Integer AD_BRNCH, Integer AD_CMPNY) throws
            GlobalRecordAlreadyDeletedException,
            GlobalDocumentNumberNotUniqueException,
            GlobalConversionDateNotExistException,
            GlobalPaymentTermInvalidException,
            GlobalTransactionAlreadyApprovedException,
            GlobalTransactionAlreadyPendingException,
            GlobalTransactionAlreadyPostedException,
            GlobalTransactionAlreadyVoidException,
            GlobalInvItemLocationNotFoundException,
            GlobalNoApprovalRequesterFoundException,
            GlobalNoApprovalApproverFoundException,
            GlobalSupplierItemInvalidException;

    java.lang.Integer saveApPoEntry(ApPurchaseOrderDetails details, java.lang.String BTCH_NM, java.lang.String PYT_NM,
                                    java.lang.String TC_NM, java.lang.String FC_NM, java.lang.String SPL_SPPLR_CODE,
                                    java.util.ArrayList plList, boolean isDraft, boolean validateShipmentNumber,
                                    java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException,
            GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException,
            GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException,
            GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalSupplierItemInvalidException,
            GlobalRecordInvalidException;

    void deleteApPoEntry(java.lang.Integer PO_CODE, java.lang.String AD_USR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    ApModSupplierDetails getApSplBySplSupplierCode(java.lang.String SPL_SPPLR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getInvUomByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY);

    double getInvIiUnitCostByIiNameAndUomName(java.lang.String II_NM, java.lang.String UOM_NM, java.lang.Integer AD_CMPNY);

    byte getAdPrfApUseSupplierPulldown(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdApprovalNotifiedUsersByPoCode(java.lang.Integer PO_CODE, SendEmailDetails sendEmailDetails, java.lang.Integer AD_CMPNY);

    double getFrRateByFrNameAndFrDate(java.lang.String FC_NM, java.util.Date CONVERSION_DATE, java.lang.Integer AD_CMPNY) throws GlobalConversionDateNotExistException;

    byte getAdPrfEnableApPOBatch(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApOpenVbAll(java.lang.String DPRTMNT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    Integer getPrCodeByPrNumberAndBrCode(String PR_NMBR, Integer AD_BRNCH, Integer AD_CMPNY);

}