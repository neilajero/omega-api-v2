package com.ejb.txn.gl;

import com.ejb.exception.gl.GlFrgROWLineNumberAlreadyExistException;
import com.ejb.exception.gl.GlFrgROWRowNameAlreadyExistException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.util.gl.GlFrgRowDetails;
import com.util.gl.GlFrgRowSetDetails;

import jakarta.ejb.Local;


@Local
public interface GlFrgRowEntryController {

    java.util.ArrayList getGlFrgRowByRowSetCode(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    GlFrgRowSetDetails getGlFrgRowSetByRowSetCode(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY);

    void addGlFrgRowEntry(GlFrgRowDetails details, java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlFrgROWRowNameAlreadyExistException, GlFrgROWLineNumberAlreadyExistException;

    void updateGlFrgRowEntry(GlFrgRowDetails details, java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlFrgROWRowNameAlreadyExistException, GlFrgROWLineNumberAlreadyExistException;

    void deleteGlFrgRowEntry(java.lang.Integer ROW_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

}