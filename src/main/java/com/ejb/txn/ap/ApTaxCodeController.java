package com.ejb.txn.ap;

import com.ejb.exception.global.*;
import com.util.ad.AdResponsibilityDetails;
import com.util.ap.ApTaxCodeDetails;

import jakarta.ejb.Local;


@Local
public interface ApTaxCodeController {

    AdResponsibilityDetails getAdRsByRsCode(java.lang.Integer RS_CODE) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBrTcAll(java.lang.Integer BTC_CODE, java.lang.String RS_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getApTcAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void addApTcEntry(ApTaxCodeDetails details, java.lang.String TC_COA_ACCNT_NMBR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException;

    void updateApTcEntry(ApTaxCodeDetails details, java.lang.String TC_COA_ACCNT_NMBR, java.lang.String RS_NM, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException, GlobalRecordAlreadyAssignedException;

    void deleteApTcEntry(java.lang.Integer TC_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}