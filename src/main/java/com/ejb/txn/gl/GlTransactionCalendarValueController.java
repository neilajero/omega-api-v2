package com.ejb.txn.gl;

import com.ejb.exception.gl.GlTCNoTransactionCalendarFoundException;
import com.ejb.exception.gl.GlTCVNoTransactionCalendarValueFoundException;
import com.ejb.exception.gl.GlTCVTransactionCalendarValueAlreadyDeletedException;
import com.util.gl.GlTransactionCalendarDetails;
import com.util.gl.GlTransactionCalendarValueDetails;

import jakarta.ejb.Local;

@Local
public interface GlTransactionCalendarValueController {

    java.util.ArrayList getGlTcAll(java.lang.Integer AD_CMPNY) throws GlTCNoTransactionCalendarFoundException;

    GlTransactionCalendarDetails getGlTcDescriptionByGlTcName(java.lang.String TC_NM, java.lang.Integer AD_CMPNY) throws GlTCNoTransactionCalendarFoundException;

    java.util.ArrayList getGlTcvByGlTcName(java.lang.String TC_NM, java.lang.Integer AD_CMPNY) throws GlTCVNoTransactionCalendarValueFoundException, GlTCNoTransactionCalendarFoundException;

    void updateGlTcvEntry(GlTransactionCalendarValueDetails details, java.lang.Integer AD_CMPNY) throws GlTCVTransactionCalendarValueAlreadyDeletedException;

}