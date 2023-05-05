package com.ejb.txnapi.ad;

import com.ejb.exception.ad.AdAURequesterMustBeEnteredOnceException;
import com.ejb.exception.ad.AdAUUserCannotBeAnApproverException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdApprovalUserDetails;

import jakarta.ejb.Local;

@Local
public interface AdApprovalUserApiController {
    void addAdAuEntry(AdApprovalUserDetails details, Integer amountLimitCode, String username, Integer companyCode)
            throws GlobalRecordAlreadyExistException, AdAURequesterMustBeEnteredOnceException, AdAUUserCannotBeAnApproverException;

}