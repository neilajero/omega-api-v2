package com.ejb.txn.gl;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.gl.GlFrgColumnSetDetails;

import jakarta.ejb.Local;

@Local
public interface GlFrgColumnSetController {

    java.util.ArrayList getGlFrgCsAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void addGlFrgCsEntry(GlFrgColumnSetDetails details, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void updateGlFrgCsEntry(GlFrgColumnSetDetails details, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void deleteGlFrgCsEntry(java.lang.Integer CS_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyAssignedException, GlobalRecordAlreadyDeletedException;

}