package com.ejb.txn.ad;

import jakarta.ejb.Local;
import java.util.*;
import java.lang.*;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdAmountLimitDetails;
import com.util.ad.AdApprovalDocumentDetails;
import com.util.mod.ad.AdModApprovalCoaLineDetails;

@Local
public interface AdAmountLimitController {

    ArrayList getAdCalByAdcCode(Integer approvalDocumentCode, Integer companyCode) throws GlobalNoRecordFoundException;

    AdApprovalDocumentDetails getAdAdcByAdcCode(Integer approvalDocumentCode, Integer companyCode);

    ArrayList getAdCalByAclCode(Integer approvalCoaLineCode, Integer companyCode) throws GlobalNoRecordFoundException;

    AdModApprovalCoaLineDetails getAdAclByAclCode(Integer approvalCoaLineCode, Integer companyCode);

    void addAdCalEntry(AdAmountLimitDetails details, Integer approvalDocumentCode, Integer approvalCoaLineCode, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void updateAdCalEntry(AdAmountLimitDetails details, Integer approvalDocumentCode, Integer approvalCoaLineCode, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void deleteAdCalEntry(Integer amountLimitCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(Integer companyCode);

}