package com.ejb.txnreports.gl;

import com.util.ad.AdCompanyDetails;
import com.util.reports.gl.GlRepQuarterlyVatReturnDetails;

import jakarta.ejb.Local;

@Local
public interface GlRepQuarterlyVatReturnController {

    GlRepQuarterlyVatReturnDetails executeGlRepQuarterlyVatReturn(java.util.Date DT_FRM, java.util.Date DT_TO, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvReportTypeAll(java.lang.Integer AD_CMPNY);

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}