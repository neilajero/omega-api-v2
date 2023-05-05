package com.ejb.txn.gl;

import com.ejb.exception.gl.*;
import com.util.gl.GlAccountingCalendarDetails;

import jakarta.ejb.Local;


@Local
public interface GlAccountingCalendarController {

    java.util.ArrayList getGlAcAll(java.lang.Integer AD_CMPNY) throws GlACNoAccountingCalendarFoundException, GlPTNoPeriodTypeFoundException;

    java.util.ArrayList getGlPtAll(java.lang.Integer AD_CMPNY) throws GlPTNoPeriodTypeFoundException;

    void addGlAcEntry(GlAccountingCalendarDetails details, java.lang.String AC_PT_NM, java.lang.Integer AD_CMPNY) throws GlACAccountingCalendarAlreadyExistException, GlPTNoPeriodTypeFoundException;

    void updateGlAcEntry(GlAccountingCalendarDetails details, java.lang.String AC_PT_NM, java.lang.Integer AD_CMPNY) throws GlACAccountingCalendarAlreadyExistException, GlACAccountingCalendarAlreadyAssignedException, GlPTNoPeriodTypeFoundException, GlACAccountingCalendarAlreadyDeletedException;

    void deleteGlAcEntry(java.lang.Integer AC_CODE, java.lang.Integer AD_CMPNY) throws GlACAccountingCalendarAlreadyAssignedException, GlACAccountingCalendarAlreadyDeletedException;

}