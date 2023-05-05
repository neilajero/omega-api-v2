package com.ejb.txn.gl;

import com.ejb.exception.gl.*;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.exception.global.GlobalRecordInvalidException;
import com.util.mod.gl.GlModAccountingCalendarDetails;

import jakarta.ejb.Local;

@Local
public interface GlAccountingCalendarValueController {

    java.util.ArrayList getGlAcAll(java.lang.Integer AD_CMPNY) throws GlACNoAccountingCalendarFoundException;

    GlModAccountingCalendarDetails getGlPtNameAndAcDescriptionByGlAcName(java.lang.String AC_NM, java.lang.Integer AD_CMPNY) throws GlACNoAccountingCalendarFoundException, GlPTNoPeriodTypeFoundException;

    java.util.ArrayList getGlAcvByGlAcName(java.lang.String AC_NM, java.lang.Integer AD_CMPNY) throws GlACVNoAccountingCalendarValueFoundException, GlACNoAccountingCalendarFoundException;

    void saveGlAcvEntry(java.util.ArrayList acvList, java.lang.String AC_NM, java.lang.Integer AD_CMPNY) throws GlACAccountingCalendarAlreadyAssignedException, GlACNoAccountingCalendarFoundException, GlACVPeriodPrefixNotUniqueException, GlACVPeriodNumberNotUniqueException, GlACVPeriodOverlappedException, GlACVLastPeriodIncorrectException, GlACVQuarterIsNotSequentialOrHasGapException, GlACVDateIsNotSequentialOrHasGapException, GlACVNotTotalToOneYearException, GlobalRecordInvalidException, GlobalRecordAlreadyExistException;

}