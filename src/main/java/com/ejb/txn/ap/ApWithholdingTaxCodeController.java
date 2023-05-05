package com.ejb.txn.ap;

import com.ejb.exception.global.*;
import com.util.ap.ApWithholdingTaxCodeDetails;

import jakarta.ejb.Local;

@Local
public interface ApWithholdingTaxCodeController {

    java.util.ArrayList getApWtcAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void addApWtcEntry(ApWithholdingTaxCodeDetails details, java.lang.String WTC_COA_ACCNT_NMBR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException;

    void updateApWtcEntry(ApWithholdingTaxCodeDetails details, java.lang.String WTC_COA_ACCNT_NMBR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException, GlobalRecordAlreadyAssignedException;

    void deleteApWtcEntry(java.lang.Integer WTC_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}