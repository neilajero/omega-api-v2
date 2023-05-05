package com.ejb.txn.gl;

import com.ejb.exception.gl.GlFrgCALRowAlreadyHasAccountAssignmentException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.gl.GlFrgCalculationDetails;
import com.util.gl.GlFrgColumnDetails;
import com.util.gl.GlFrgRowDetails;

import jakarta.ejb.Local;


@Local
public interface GlFrgCalculationController {

    java.util.ArrayList getGlFrgCalByRowCode(java.lang.Integer ROW_CODE, java.lang.String CAL_TYP, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getGlFrgCalByColCode(java.lang.Integer COL_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    GlFrgRowDetails getGlFrgRowByRowCode(java.lang.Integer ROW_CODE, java.lang.Integer AD_CMPNY);

    GlFrgColumnDetails getGlFrgColByColCode(java.lang.Integer COL_CODE, java.lang.Integer AD_CMPNY);

    void addGlFrgCalEntry(GlFrgCalculationDetails details, java.lang.Integer ROW_CODE, java.lang.Integer COL_CODE, java.lang.Integer AD_CMPNY) throws GlFrgCALRowAlreadyHasAccountAssignmentException, GlobalRecordAlreadyExistException;

    void updateGlFrgCalEntry(GlFrgCalculationDetails details, java.lang.Integer ROW_CODE, java.lang.Integer COL_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void deleteGlFrgCalEntry(java.lang.Integer CAL_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    java.util.ArrayList getGlFrgRowByRsCodeAndLessThanRowLineNumber(java.lang.Integer RS_CODE, int RW_LN_NMBR, java.lang.String CAL_TYP, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlFrgColByCsCodeAndLessThanColumnSequenceNumber(java.lang.Integer CS_CODE, int COL_SQNC_NMBR, java.lang.Integer AD_CMPNY);

}