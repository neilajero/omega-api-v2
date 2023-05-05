package com.ejb.txn.gl;

import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.gl.GlJREffectiveDateViolationException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;

import jakarta.ejb.Local;

@Local
public interface GlRecurringJournalGenerationController {
    java.util.ArrayList getGlJcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlRjByCriteria(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    void executeGlRjGeneration(java.lang.Integer RJ_CODE, java.util.Date RJ_NXT_RN_DT, java.lang.String JR_DCMNT_NMBR, java.util.Date JR_EFFCTV_DT, java.lang.String USR_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlJREffectiveDateNoPeriodExistException, GlobalDocumentNumberNotUniqueException, GlJREffectiveDateViolationException, GlJREffectiveDatePeriodClosedException;

}