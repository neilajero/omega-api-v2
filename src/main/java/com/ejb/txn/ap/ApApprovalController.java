package com.ejb.txn.ap;

import jakarta.ejb.Local;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.util.SendEmailDetails;

import java.util.*;

@Local
public interface ApApprovalController {

    String getApprovalStatus(String userDepartment, String username, String userDesc, String approvalQueueDocument, Integer approvalQueueDocumentCode, String approvalDocumentNumber, Date approvalQueueDate, double totalAmount, Integer branchCode, Integer companyCode) throws GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException;

    ArrayList getApApproverList(String approvalQueueDocument, Integer approvalQueueDocumentCode, Integer companyCode);

    ArrayList getAdApprovalNotifiedUsersByAqCode(Integer approvalQueueCode, SendEmailDetails sendEmailDetails, Integer companyCode);

    ArrayList getAdAqByAqDocumentAndUserName(HashMap criteria, String username, Integer offset, Integer limit, String orderBy, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException;

    void executeApApproval(String approvalQueueDocument, Integer approvalQueueDocumentCode, String username, boolean isApproved, String reasonForRejection, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalTransactionAlreadyVoidPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, AdPRFCoaGlVarianceAccountNotFoundException;

    short getGlFcPrecisionUnit(Integer companyCode);

}