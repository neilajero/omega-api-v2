package com.ejb.txn.cm;

import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;

import jakarta.ejb.Local;
import java.util.*;

@Local
public interface CmApprovalController {

    ArrayList getAdAqByAqDocumentAndUserName(HashMap criteria, String username, Integer offset, Integer limit, String orderBy, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException;

    void executeCmApproval(String approvalQueueDocument, Integer approvalQueueDocumentCode, String username, boolean isApproved, String reasonForRejection, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException, GlobalConversionDateNotExistException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidPostedException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException;

    short getGlFcPrecisionUnit(Integer companyCode);
}