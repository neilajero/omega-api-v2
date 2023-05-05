package com.ejb.txn.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.mod.ar.ArModPersonelTypeDetails;

import jakarta.ejb.Local;

@Local
public interface ArPersonelTypeController {

    java.util.ArrayList getArPtAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void addArPtEntry(ArModPersonelTypeDetails details, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void updateArPtEntry(ArModPersonelTypeDetails details, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void deleteArPtEntry(java.lang.Integer PT_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}