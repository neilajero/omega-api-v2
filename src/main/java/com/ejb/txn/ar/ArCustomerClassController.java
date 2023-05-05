package com.ejb.txn.ar;

import com.ejb.exception.ar.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.mod.ar.ArModCustomerClassDetails;

import jakarta.ejb.Local;


@Local
public interface ArCustomerClassController {
    java.util.ArrayList getArCcAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void addArCcEntry(ArModCustomerClassDetails mdetails, java.lang.String TC_NM, java.lang.String WTC_NM, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, ArCCCoaGlReceivableAccountNotFoundException, ArCCCoaGlRevenueAccountNotFoundException, ArCCCoaGlUnEarnedInterestAccountNotFoundException, ArCCCoaGlEarnedInterestAccountNotFoundException, ArCCCoaGlUnEarnedPenaltyAccountNotFoundException, ArCCCoaGlEarnedPenaltyAccountNotFoundException;

    void updateArCcEntry(ArModCustomerClassDetails mdetails, java.lang.String TC_NM, java.lang.String WTC_NM, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, ArCCCoaGlReceivableAccountNotFoundException, ArCCCoaGlRevenueAccountNotFoundException, ArCCCoaGlUnEarnedInterestAccountNotFoundException, ArCCCoaGlEarnedInterestAccountNotFoundException, ArCCCoaGlUnEarnedPenaltyAccountNotFoundException, ArCCCoaGlEarnedPenaltyAccountNotFoundException;

    void deleteArCcEntry(java.lang.Integer CC_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    boolean getArCcGlCoaRevenueAccountEnable(java.lang.Integer AD_CMPNY);

}