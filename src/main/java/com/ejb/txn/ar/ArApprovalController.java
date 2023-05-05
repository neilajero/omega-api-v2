package com.ejb.txn.ar;

import jakarta.ejb.Local;
import java.util.*;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;

@Local
public interface ArApprovalController {
    String getApprovalStatus(String userDepartment, String username, String userDesc, String approvalQueueDocument, Integer approvalQueueDocumentCode, String approvalQueueDocumentNumber, Date approvalQueueDate, Integer branchCode, Integer companyCode) throws GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException;

    ArrayList getArApproverList(String approvalQueueDocument, Integer approvalQueueDocumentCode, Integer companyCode);

    ArrayList getAdApprovalNotifiedUsersByAqCode(Integer approvalQueueCode, Integer companyCode);

    ArrayList getAdAqByAqDocumentAndUserName(HashMap criteria, String username, Integer offset, Integer limit, String orderBy, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException;

    void executeArApproval(String approvalQueueDocument, Integer approvalQueueDocumentCode, String username, boolean isApproved, String memo, String reasonForRejection, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException;

    short getGlFcPrecisionUnit(Integer companyCode);

}