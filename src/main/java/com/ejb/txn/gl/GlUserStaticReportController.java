package com.ejb.txn.gl;

import com.util.gl.GlStaticReportDetails;
import com.util.mod.gl.GlModUserStaticReportDetails;

import jakarta.ejb.Local;

@Local
public interface GlUserStaticReportController {

    GlModUserStaticReportDetails getGlUstrBySrCodeAndUsrCode(java.lang.Integer SR_CODE, java.lang.Integer USR_CODE, java.lang.Integer AD_CMPNY);

    GlStaticReportDetails getGlSrBySrCode(java.lang.Integer SR_CODE);

    void saveGlUstrEntry(java.util.ArrayList list, java.lang.Integer SR_CODE, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdUsrAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlUstrBySrCode(java.lang.Integer SR_CODE, java.lang.Integer AD_CMPNY);

}