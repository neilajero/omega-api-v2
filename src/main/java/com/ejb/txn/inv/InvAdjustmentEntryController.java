package com.ejb.txn.inv;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.exception.inv.InvTagSerialNumberNotFoundException;
import com.util.inv.InvAdjustmentDetails;
import com.util.mod.ap.ApModSupplierDetails;
import com.util.mod.inv.InvModAdjustmentDetails;
import jakarta.ejb.Local;

import java.util.ArrayList;
import java.util.Date;

@Local
public interface InvAdjustmentEntryController {

    java.util.ArrayList getApSplAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    ApModSupplierDetails getApSplBySplSupplierCode(java.lang.String SPL_SPPLR_CODE, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    byte getAdPrfApUseSupplierPulldown(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvLocAll(java.lang.Integer AD_CMPNY);

    void executeInvAdjSubmit(java.lang.Integer ADJ_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalBranchAccountNumberInvalidException,
            GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException,
            GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException,
            GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalDocumentNumberNotUniqueException,
            GlobalInventoryDateException, GlobalInvTagMissingException,
            InvTagSerialNumberNotFoundException, GlobalInvTagExistingException,
            GlobalAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalExpiryDateNotFoundException, GlobalMiscInfoIsRequiredException,
            GlobalRecordInvalidException;

    boolean getInvTraceMisc(java.lang.String II_NAME, java.lang.Integer AD_CMPNY);

    InvModAdjustmentDetails getInvAdjByAdjCode(
            java.lang.Integer ADJ_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdUsrAll(java.lang.Integer AD_CMPNY);

    java.lang.String getNotedByMe(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvUomByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY);

    java.lang.Integer saveInvAdjEntryMobile(
            InvModAdjustmentDetails details, java.util.ArrayList alList, boolean isDraft,
            java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalBranchAccountNumberInvalidException,
            GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException,
            GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException,
            GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalDocumentNumberNotUniqueException,
            GlobalInventoryDateException, GlobalAccountNumberInvalidException,
            AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException,
            GlobalMiscInfoIsRequiredException, GlobalRecordInvalidException;

    java.lang.String getInvPrfDefaultAdjustmentAccount(java.lang.String ADJ_TYP, java.lang.Integer AD_CMPNY, java.lang.String USR_NM);

    Integer saveInvAdjEntry(
            InvAdjustmentDetails details, String COA_ACCOUNT_NUMBER, ArrayList alList,
            boolean isDraft, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalBranchAccountNumberInvalidException,
            GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException,
            GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException,
            GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException,
            GlobalDocumentNumberNotUniqueException;

    java.lang.Integer saveInvAdjEntry(
            InvAdjustmentDetails details, java.lang.String COA_ACCOUNT_NUMBER, java.lang.String SPL_SPPLR_CODE,
            java.util.ArrayList alList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalBranchAccountNumberInvalidException,
            GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException,
            GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException,
            GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalDocumentNumberNotUniqueException,
            GlobalInventoryDateException, GlobalInvTagMissingException,
            InvTagSerialNumberNotFoundException, GlobalInvTagExistingException,
            GlobalAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalExpiryDateNotFoundException, GlobalMiscInfoIsRequiredException,
            GlobalRecordInvalidException;

    void deleteInvAdjEntry(java.lang.Integer ADJ_CODE, java.lang.String AD_USR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpCostPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpInventoryLineNumber(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdApprovalNotifiedUsersByAdjCode(java.lang.Integer ADJ_CODE, java.lang.Integer AD_CMPNY);

    double getInvIiUnitCostByIiNameAndUomNameAndLocNameAndDateAndQty(
            java.lang.String II_NM, java.lang.String UOM_NM, java.lang.String LOC_NM, java.util.Date ADJ_DT,
            double quantity, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    Integer saveInvAdjEntry1(ArrayList arList, Integer ADJ_CODE, String CRTD_BY, Integer branchCode, Integer companyCode);

    double getQuantityByIiNameAndUomName(String II_NM, String LOC_NM, String UOM_NM, Date PI_DT, Integer branchCode, Integer companyCode);

}