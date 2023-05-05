package com.ejb.txnreports.gl;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.mod.gl.GlModUserStaticReportDetails;

import jakarta.ejb.Local;

@Local
public interface GlRepStaticReportController {

    GlModUserStaticReportDetails getGlUstrBySrCodeAndUsrCode(java.lang.Integer SR_CODE, java.lang.Integer USR_CODE, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlUstrByUsrCode(java.lang.Integer USR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}