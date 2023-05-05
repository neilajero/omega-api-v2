package com.ejb.txn.ar;

import com.ejb.exception.global.*;
import com.util.ar.ArWithholdingTaxCodeDetails;

import jakarta.ejb.Local;

@Local
public interface ArWithholdingTaxCodeController {

    java.util.ArrayList getArWtcAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void addArWtcEntry(ArWithholdingTaxCodeDetails details, java.lang.String WTC_COA_ACCNT_NMBR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException;

    void updateArWtcEntry(ArWithholdingTaxCodeDetails details, java.lang.String WTC_COA_ACCNT_NMBR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException, GlobalRecordAlreadyAssignedException;

    void deleteArWtcEntry(java.lang.Integer WTC_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}