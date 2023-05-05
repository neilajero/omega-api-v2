package com.ejb.txn.ad;

import jakarta.ejb.Local;
import java.util.*;

import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdApprovalCoaLineDetails;
import com.util.ad.AdApprovalDocumentDetails;

import java.lang.*;

@Local
public interface AdApprovalCoaLineController {

    ArrayList getAdAclAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdApprovalDocumentDetails getAdAdcByAdcCode(Integer ADC_CODE, Integer AD_CMPNY);

    void addAdAclEntry(String COA_ACCNT_NMBR, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException;

    void updateAdAclEntry(AdApprovalCoaLineDetails details, String COA_ACCNT_NMBR, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException;

    void deleteAdAclEntry(Integer ACL_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(Integer AD_CMPNY);

}