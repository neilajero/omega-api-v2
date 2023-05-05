package com.ejb.txn.ar;

import com.ejb.exception.ar.ArTCCoaGlTaxCodeAccountNotFoundException;
import com.ejb.exception.ar.ArTCInterimAccountInvalidException;
import com.ejb.exception.global.*;
import com.util.ad.AdResponsibilityDetails;
import com.util.ar.ArTaxCodeDetails;

import jakarta.ejb.Local;

@Local
public interface ArTaxCodeController {

    AdResponsibilityDetails getAdRsByRsCode(java.lang.Integer RS_CODE) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBrTcAll(java.lang.Integer BTC_CODE, java.lang.String RS_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getArTcAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void addArTcEntry(ArTaxCodeDetails details, java.lang.String TC_COA_ACCNT_NMBR, java.lang.String TC_INTRM_ACCNT_NMBR, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException, ArTCInterimAccountInvalidException;

    void updateArTcEntry(ArTaxCodeDetails details, java.lang.String TC_COA_ACCNT_NMBR, java.lang.String TC_INTRM_ACCNT_NMBR, java.lang.String RS_NM, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException, GlobalRecordAlreadyAssignedException, ArTCCoaGlTaxCodeAccountNotFoundException, ArTCInterimAccountInvalidException;

    void deleteArTcEntry(java.lang.Integer TC_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}