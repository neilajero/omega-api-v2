package com.ejb.txn.gl;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.gl.GlFrgFinancialReportDetails;

import jakarta.ejb.Local;


@Local
public interface GlFrgFinancialReportEntryController {

    java.util.ArrayList getGlFrgCsAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getGlFrgRsAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void addGlFrgFrEntry(GlFrgFinancialReportDetails details, java.lang.String RS_NM, java.lang.String CS_NM, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void updateGlFrgFrEntry(GlFrgFinancialReportDetails details, java.lang.String RS_NM, java.lang.String CS_NM, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void deleteGlFrgFrEntry(java.lang.Integer FR_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    java.util.ArrayList getGlFrgFrAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}