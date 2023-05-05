package com.ejb.txn.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.mod.ar.ArModJobOrderTypeDetails;

import jakarta.ejb.Local;

@Local
public interface ArJobOrderTypeController {

    java.util.ArrayList getArJotAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void addArJotEntry(ArModJobOrderTypeDetails mdetails, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void updateArJotEntry(ArModJobOrderTypeDetails mdetails, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void deleteArJotEntry(java.lang.Integer JOT_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}