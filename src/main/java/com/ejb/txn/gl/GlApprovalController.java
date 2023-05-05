package com.ejb.txn.gl;

import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalTransactionAlreadyPostedException;

import jakarta.ejb.Local;
import java.util.*;

@Local
public interface GlApprovalController {

    ArrayList getAdAqByAqDocumentAndUserName(HashMap criteria, String username, Integer offset, Integer limit, String orderBy, Integer branchCode, Integer companyCode) throws GlobalNoRecordFoundException;

    void executeGlApproval(String approvalQueueDocument, Integer approvalQueueDocumentCode, String username, boolean isApproved, String reasonForRejection, Integer branchCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlJREffectiveDatePeriodClosedException, GlobalTransactionAlreadyPostedException;

    short getGlFcPrecisionUnit(Integer companyCode);

}