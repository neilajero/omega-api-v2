package com.ejb.txn.gl;

import com.ejb.exception.gl.GlPTNoPeriodTypeFoundException;
import com.ejb.exception.gl.GlPTPeriodTypeAlreadyAssignedException;
import com.ejb.exception.gl.GlPTPeriodTypeAlreadyDeletedException;
import com.ejb.exception.gl.GlPTPeriodTypeAlreadyExistException;
import com.util.gl.GlPeriodTypeDetails;

import jakarta.ejb.Local;

@Local
public interface GlPeriodTypeController {

    java.util.ArrayList getGlPtAll(java.lang.Integer AD_CMPNY) throws GlPTNoPeriodTypeFoundException;

    void addGlPtEntry(GlPeriodTypeDetails details, java.lang.Integer AD_CMPNY) throws GlPTPeriodTypeAlreadyExistException;

    void updateGlPtEntry(GlPeriodTypeDetails details, java.lang.Integer AD_CMPNY) throws GlPTPeriodTypeAlreadyExistException, GlPTPeriodTypeAlreadyAssignedException, GlPTPeriodTypeAlreadyDeletedException;

    void deleteGlPtEntry(java.lang.Integer PT_CODE, java.lang.Integer AD_CMPNY) throws GlPTPeriodTypeAlreadyDeletedException, GlPTPeriodTypeAlreadyAssignedException;

}