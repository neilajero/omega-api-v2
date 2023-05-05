package com.ejb.txn.gl;

import com.ejb.exception.gl.GlFrgCOLColumnNameAlreadyExistException;
import com.ejb.exception.gl.GlFrgCOLSequenceNumberAlreadyExistException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.util.gl.GlFrgColumnSetDetails;
import com.util.mod.gl.GlModFrgColumnDetails;

import jakarta.ejb.Local;

@Local
public interface GlFrgColumnEntryController {

    java.util.ArrayList getGlFrgColByCsCode(java.lang.Integer CS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    GlFrgColumnSetDetails getGlFrgCsByCsCode(java.lang.Integer CS_CODE, java.lang.Integer AD_CMPNY);

    void addGlFrgColEntry(GlModFrgColumnDetails mdetails, java.lang.Integer CS_CODE, java.lang.Integer AD_CMPNY) throws GlFrgCOLColumnNameAlreadyExistException, GlFrgCOLSequenceNumberAlreadyExistException;

    void updateGlFrgColEntry(GlModFrgColumnDetails mdetails, java.lang.Integer CS_CODE, java.lang.Integer AD_CMPNY) throws GlFrgCOLColumnNameAlreadyExistException, GlFrgCOLSequenceNumberAlreadyExistException;

    void deleteGlFrgColEntry(java.lang.Integer COL_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    java.util.ArrayList getGlFcAllWithDefault(java.lang.Integer AD_CMPNY);

}