package com.ejb.txn.cm;

import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.cm.CmFundTransferDetails;
import com.util.mod.ad.AdModBankAccountDetails;
import com.util.mod.cm.CmModFundTransferEntryDetails;

import jakarta.ejb.Local;

@Local
public interface CmFundTransferEntryController {

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    CmModFundTransferEntryDetails getCmFtByFtCode(java.lang.Integer FT_CODE, java.lang.Integer AD_CMPNY);

    java.lang.Integer saveCmFtEntry(CmFundTransferDetails details, java.lang.String BNK_NM_FRM, java.lang.String BNK_NM_TO, java.lang.String FC_NM, boolean isDraft, java.util.ArrayList list, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalDocumentNumberNotUniqueException, GlobalBranchAccountNumberInvalidException, GlobalNoRecordFoundException, GlobalOverapplicationNotAllowedException;

    void deleteCmFtEntry(java.lang.Integer FT_CODE, java.lang.String AD_USR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    java.util.ArrayList getAdApprovalNotifiedUsersByFtCode(java.lang.Integer FT_CODE, java.lang.Integer AD_CMPNY);

    AdModBankAccountDetails getAdBaByBaName(java.lang.String BA_NM, java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    double getFrRateByFrNameAndFrDate(java.lang.String FC_NM, java.util.Date CONVERSION_DATE, java.lang.Integer AD_CMPNY) throws GlobalConversionDateNotExistException;

    java.util.ArrayList getPostedArReceiptByRctDateFromAndRctDateToAndBankAccountName(java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO, java.lang.String BA_NM, java.lang.String ORDER_BY, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void clearCmFundTransferReceipts(java.lang.Integer FT_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

}