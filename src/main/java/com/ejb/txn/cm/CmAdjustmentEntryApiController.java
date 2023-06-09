package com.ejb.txn.cm;

import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.GlobalBranchAccountNumberInvalidException;
import com.ejb.exception.global.GlobalConversionDateNotExistException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalJournalNotBalanceException;
import com.ejb.exception.global.GlobalNoApprovalApproverFoundException;
import com.ejb.exception.global.GlobalNoApprovalRequesterFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalTransactionAlreadyApprovedException;
import com.ejb.exception.global.GlobalTransactionAlreadyPendingException;
import com.ejb.exception.global.GlobalTransactionAlreadyPostedException;
import com.ejb.exception.global.GlobalTransactionAlreadyVoidException;
import com.util.cm.CmAdjustmentDetails;


public interface CmAdjustmentEntryApiController {

    Integer saveCmAdjEntry(CmAdjustmentDetails details, String BA_NM, String CST_NM, String SO_NMBR, String FC_NM, boolean isDraft, Integer PYRLL_PRD, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalRecordAlreadyAssignedException, GlobalDocumentNumberNotUniqueException, GlobalBranchAccountNumberInvalidException;

}