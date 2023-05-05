package com.ejb.txn.gl;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.gl.GlFrgRowSetDetails;

import jakarta.ejb.Local;

@Local
public interface GlFrgRowSetController {

    java.util.ArrayList getGlFrgRsAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void addGlFrgRsEntry(GlFrgRowSetDetails details, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void copyGlFrgRsEntry(GlFrgRowSetDetails details, java.lang.Integer AD_CMPNY) throws java.lang.Exception;

    void updateGlFrgRsEntry(GlFrgRowSetDetails details, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void deleteGlFrgRsEntry(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyAssignedException, GlobalRecordAlreadyDeletedException;

}