package com.ejb.txn.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.mod.ar.ArModPersonelDetails;

import jakarta.ejb.Local;

@Local
public interface ArPersonelController {

    ArModPersonelDetails getArPeByPeCode(java.lang.Integer PE_CODE) throws GlobalNoRecordFoundException;

    java.util.ArrayList getArPeAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void addArPeEntry(ArModPersonelDetails details, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void updateArPeEntry(ArModPersonelDetails details, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void deleteArPeEntry(java.lang.Integer PRL_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    double getPersonelTypeRateByPersonelTypeName(java.lang.String PT_NM, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArPtAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}