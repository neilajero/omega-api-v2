package com.ejb.txn.gl;

import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.gl.GlJREffectiveDateViolationException;
import com.ejb.exception.gl.GlJRJournalAlreadyReversedException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;

import jakarta.ejb.Local;

@Local
public interface GlJournalReversalController {

    java.util.ArrayList getGlJcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlJsAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlFcAllWithDefault(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlJrReversibleByCriteria(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    void executeGlJrReverse(java.lang.Integer JR_CODE, java.lang.String JR_DCMNT_NMBR, java.util.Date JR_DT_RVRSL, java.lang.String USR_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyDeletedException, GlJREffectiveDateNoPeriodExistException, GlobalDocumentNumberNotUniqueException, GlJREffectiveDateViolationException, GlJREffectiveDatePeriodClosedException, GlJRJournalAlreadyReversedException;

}