package com.ejb.txn.inv;

import com.ejb.entities.inv.LocalInvBranchStockTransfer;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.exception.inv.InvTagSerialNumberNotFoundException;
import com.util.mod.inv.InvModBranchStockTransferDetails;

import jakarta.ejb.Local;


@Local
public interface InvBranchStockTransferOutEntryController {

    InvModBranchStockTransferDetails getInvBstByBstCode(java.util.Date BSTI_DT, java.lang.Integer BST_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    LocalInvBranchStockTransfer getInvBstOrderByBstNumberAndBstBranch(java.lang.String BST_NMBR, java.lang.String BR_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getInvBstNumberAllByBstTypeAndBstBranch(java.lang.String BST_TYP, java.lang.Integer AD_BRNCH, java.lang.Integer BST_AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getInvBranchUomByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY);

    java.lang.Integer saveInvBstEntry(InvModBranchStockTransferDetails details, java.util.ArrayList bslList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY, java.lang.String type) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvItemLocationNotFoundException, GlobalInvTagMissingException, InvTagSerialNumberNotFoundException, GlobalInvTagExistingException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalDocumentNumberNotUniqueException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalRecordInvalidException, GlobalNoRecordFoundException, GlobalExpiryDateNotFoundException, GlobalMiscInfoIsRequiredException;

    double getInvIiShippingCostByIiUnitCostAndAdBranch(double II_UNT_CST, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    void deleteInvBstEntry(java.lang.Integer BST_CODE, java.lang.String AD_USR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    LocalInvBranchStockTransfer getInvBstOutByBstNumberAndBstBranch(java.lang.String BST_NMBR, java.lang.Integer BR_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpInventoryLineNumber(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdApprovalNotifiedUsersByBstCode(java.lang.Integer BST_CODE, java.lang.Integer AD_CMPNY);

    double getInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate(java.lang.String II_NM, java.lang.String LOC_FRM, java.lang.String UOM_NM, java.util.Date ST_DT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArSoNumberPostedSoByBrCode(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    boolean getInvTraceMisc(java.lang.String II_NAME, java.lang.Integer AD_CMPNY);

}