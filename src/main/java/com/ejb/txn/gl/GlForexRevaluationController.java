package com.ejb.txn.gl;

import com.ejb.exception.gl.*;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordInvalidException;

import jakarta.ejb.Local;


@Local
public interface GlForexRevaluationController {

    java.util.ArrayList getGlFcAllWithDefault(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlAcAllEditableOpenAndFutureEntry(java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGenSgAll(java.lang.Integer AD_CMPNY);

    void executeForexRevaluation(java.util.HashMap criteria, double CONVERSION_RATE, java.lang.String UNRLZD_GN_LSS_ACCNT, int YR, java.lang.String PRD_NM, java.lang.String USR_NM, java.lang.String JR_DCMNT_NMBR, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlJREffectiveDatePeriodClosedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDateViolationException, GlobalDocumentNumberNotUniqueException, GlFCNoFunctionalCurrencyFoundException, GlFCFunctionalCurrencyAlreadyAssignedException, GlCOANoChartOfAccountFoundException, GlobalAccountNumberInvalidException, GlobalRecordInvalidException, GlobalNoRecordFoundException;

}