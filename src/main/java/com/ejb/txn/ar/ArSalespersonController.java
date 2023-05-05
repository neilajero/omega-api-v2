package com.ejb.txn.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdResponsibilityDetails;
import com.util.ar.ArSalespersonDetails;

import jakarta.ejb.Local;

@Local
public interface ArSalespersonController {

    java.util.ArrayList getAdBrAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBrSlpAll(java.lang.Integer BSLP_CODE, java.lang.String RS_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdResponsibilityDetails getAdRsByRsCode(java.lang.Integer RS_CODE) throws GlobalNoRecordFoundException;

    void addArSlpEntry(ArSalespersonDetails details, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void updateArSlpEntry(ArSalespersonDetails details, java.lang.String RS_NM, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void deleteArSlpEntry(java.lang.Integer SLP_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}