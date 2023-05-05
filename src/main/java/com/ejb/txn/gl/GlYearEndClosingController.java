package com.ejb.txn.gl;

import com.ejb.exception.gl.GlDepreciationAlreadyMadeForThePeriodException;
import com.ejb.exception.gl.GlDepreciationPeriodInvalidException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalNotAllTransactionsArePostedException;
import com.util.gl.GlAccountingCalendarDetails;

import jakarta.ejb.Local;

@Local
public interface GlYearEndClosingController {

    GlAccountingCalendarDetails getGlAcForClosing(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getGlReportableAcvAll(java.lang.Integer AD_CMPNY);

    void executeFixedAssetDepreciation(java.lang.Integer AC_CODE, java.lang.String DTB_PRD, int DTB_YR, java.lang.String userName, java.lang.Integer resCode, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlobalAccountNumberInvalidException, GlJREffectiveDatePeriodClosedException, GlJREffectiveDateNoPeriodExistException, GlDepreciationAlreadyMadeForThePeriodException, GlDepreciationPeriodInvalidException;

    void executeGlYearEndClosing(java.lang.Integer AC_CODE, java.lang.Integer AD_CMPNY) throws GlobalNotAllTransactionsArePostedException, GlobalAccountNumberInvalidException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}