package com.ejb.txnapi.ar;

import com.ejb.exception.global.GlobalNoApprovalApproverFoundException;
import com.ejb.exception.global.GlobalNoApprovalRequesterFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public interface ArApprovalApiController {

    String getApprovalStatus(String approvalDocumentType, String username, String userDesc, String approvalQueueDocument,
                             Integer approvalQueueDocumentCode, String approvalQueueDocumentNumber, Date approvalQueueDate,
                             Integer branchCode, Integer companyCode, String companyShortName)
            throws GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException;

    ArrayList getAdApprovalNotifiedUsersByAqCode(Integer approvalQueueCode, Integer companyCode, String companyShortName);

    ArrayList getAdAqByAqDocumentAndUserName(HashMap criteria, String username, Integer offset, Integer limit,
                                             String orderBy, Integer branchCode, Integer companyCode, String companyShortName)
            throws GlobalNoRecordFoundException;

}