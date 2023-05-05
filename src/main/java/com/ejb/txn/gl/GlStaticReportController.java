package com.ejb.txn.gl;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.gl.GlStaticReportDetails;

import jakarta.ejb.Local;
@Local
public interface GlStaticReportController {
    java.util.ArrayList getGlSrAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void addGlSrEntry(GlStaticReportDetails details, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void updateGlSrEntry(GlStaticReportDetails details, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void deleteGlSrEntry(java.lang.Integer SR_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

    GlStaticReportDetails getGlSrBySrCode(java.lang.Integer SR_CODE) throws GlobalNoRecordFoundException;

}