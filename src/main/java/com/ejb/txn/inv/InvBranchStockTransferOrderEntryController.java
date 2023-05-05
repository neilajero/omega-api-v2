package com.ejb.txn.inv;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.mod.inv.InvModBranchStockTransferDetails;

import jakarta.ejb.Local;

@Local
public interface InvBranchStockTransferOrderEntryController {

    InvModBranchStockTransferDetails getInvBstByBstCode(java.lang.Integer BST_CODE, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    java.util.ArrayList getInvBranchUomByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY);

    java.lang.Integer saveInvBstEntry(InvModBranchStockTransferDetails details, java.util.ArrayList bslList,
                                      boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException,
            GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException,
            GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException,
            GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException,
            GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException,
            GlobalDocumentNumberNotUniqueException, GlobalBranchAccountNumberInvalidException,
            AdPRFCoaGlVarianceAccountNotFoundException, GlobalRecordInvalidException,
            GlobalNoRecordFoundException, GlobalExpiryDateNotFoundException, GlobalMiscInfoIsRequiredException;

    void deleteInvBstEntry(java.lang.Integer BST_CODE, java.lang.String AD_USR, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpInventoryLineNumber(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdApprovalNotifiedUsersByBstCode(java.lang.Integer BST_CODE, java.lang.Integer AD_CMPNY);

    double getInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate(
            java.lang.String II_NM, java.lang.String LOC_FRM, java.lang.String UOM_NM, java.util.Date ST_DT,
            java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    boolean getInvTraceMisc(java.lang.String II_NAME, java.lang.Integer AD_CMPNY);

}