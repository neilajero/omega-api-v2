package com.ejb.txn.ap;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ap.ApSupplierTypeDetails;

import jakarta.ejb.Local;

@Local
public interface ApSupplierTypeController {

    java.util.ArrayList getApStAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    void addApStEntry(ApSupplierTypeDetails details, java.lang.String ST_BA_NM, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void updateApStEntry(ApSupplierTypeDetails details, java.lang.String ST_BA_NM, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void deleteApStEntry(java.lang.Integer ST_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}