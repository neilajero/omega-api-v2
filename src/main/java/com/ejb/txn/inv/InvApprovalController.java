package com.ejb.txn.inv;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;

import jakarta.ejb.Local;
import java.util.*;

@Local
public interface InvApprovalController {

    ArrayList getAdApprovalNotifiedUsersByAqCode(Integer approvalQueueCode, Integer companyCode);

    ArrayList getAdAqByAqDocumentAndUserName(HashMap criteria, String username, Integer offset, Integer limit, String orderBy, Integer branchCode, Integer companyCode)
            throws GlobalNoRecordFoundException;

    void executeInvApproval(String approvalQueueDocument, Integer approvalQueueDocumentCode, String username, boolean isApproved,
                            String reasonForRejection, Integer branchCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException;

    short getGlFcPrecisionUnit(Integer companyCode);

}