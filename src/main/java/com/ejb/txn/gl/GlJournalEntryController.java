package com.ejb.txn.gl;

import com.ejb.exception.gl.*;
import com.ejb.exception.global.*;
import com.util.gl.GlJournalDetails;
import com.util.mod.gl.GlModJournalDetails;

import jakarta.ejb.Local;

@Local
public interface GlJournalEntryController {

    java.util.ArrayList getGlFcAllWithDefault(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlJcAll(java.lang.Integer AD_CMPNY);

    short getAdPrfGlJournalLineNumber(java.lang.Integer AD_CMPNY);

    GlModJournalDetails getGlJrByJrCode(java.lang.Integer JR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer saveGlJrEntry(GlJournalDetails details, java.lang.String JC_NM, java.lang.String FC_NM, java.lang.Integer RES_CODE, java.lang.String JS_NM, java.lang.String JB_NM, java.util.ArrayList jlList, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlJREffectiveDateViolationException, GlobalDocumentNumberNotUniqueException, GlJRDateReversalNoPeriodExistException, GlobalConversionDateNotExistException, GlJLChartOfAccountNotFoundException, GlJLChartOfAccountPermissionDeniedException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalNoRecordFoundException;

    void deleteGlJrEntry(java.lang.Integer JR_CODE, java.lang.String AD_USR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdApprovalNotifiedUsersByJrCode(java.lang.Integer JR_CODE, java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableGlJournalBatch(java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableAllowSuspensePosting(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlOpenJbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    double getFrRateByFrNameAndFrDate(java.lang.String FC_NM, java.util.Date CONVERSION_DATE, java.lang.Integer AD_CMPNY) throws GlobalConversionDateNotExistException;

}