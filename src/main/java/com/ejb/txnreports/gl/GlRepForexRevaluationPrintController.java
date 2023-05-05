package com.ejb.txnreports.gl;

import com.ejb.exception.gl.*;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.gl.GlChartOfAccountDetails;

import jakarta.ejb.Local;

@Local
public interface GlRepForexRevaluationPrintController {

    GlChartOfAccountDetails getGlChartOfAccount(java.lang.String UNRLZD_GN_LSS_ACCNT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlCOANoChartOfAccountFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    java.util.ArrayList executeRepForexRevaluationPrint(java.util.HashMap criteria, double CONVERSION_RATE, int YR, java.lang.String PRD_NM, java.lang.String USR_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlJREffectiveDateViolationException, GlFCNoFunctionalCurrencyFoundException, GlFCFunctionalCurrencyAlreadyAssignedException, GlobalAccountNumberInvalidException, GlJREffectiveDateNoPeriodExistException, GlobalNoRecordFoundException;

}