package com.ejb.txn.ad;

import com.ejb.exception.ad.AdAURequesterMustBeEnteredOnceException;
import com.ejb.exception.ad.AdAUUserCannotBeARequesterException;
import com.ejb.exception.ad.AdAUUserCannotBeAnApproverException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdAmountLimitDetails;
import com.util.ad.AdApprovalUserDetails;

import jakarta.ejb.Local;
import java.util.ArrayList;

@Local
public interface AdApprovalUserController {

    ArrayList getAdAuByCalCode(Integer amountLimitCode, Integer companyCode) throws GlobalNoRecordFoundException;

    AdAmountLimitDetails getAdCalByCalCode(Integer amountLimitCode, Integer companyCode);

    void addAdAuEntry(AdApprovalUserDetails details, Integer amountLimitCode, String username, Integer companyCode) throws GlobalRecordAlreadyExistException, AdAURequesterMustBeEnteredOnceException, AdAUUserCannotBeAnApproverException;

    void updateAdAuEntry(AdApprovalUserDetails details, Integer amountLimitCode, String username, Integer companyCode) throws GlobalRecordAlreadyExistException, AdAURequesterMustBeEnteredOnceException, AdAUUserCannotBeAnApproverException, AdAUUserCannotBeARequesterException;

    void deleteAdAuEntry(Integer approvalUserCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(Integer companyCode);

}