package com.ejb.txnreports.gl;

import com.util.reports.gl.GlRepFinancialReportDetails;

import jakarta.ejb.Local;

@Local
public interface GlRepFinancialReportRunController {

    java.util.ArrayList getGlFrgFrAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlReportableAcvAll(java.lang.Integer AD_CMPNY);

    GlRepFinancialReportDetails getGlRepFrParameters(java.lang.String FR_NM, java.lang.String FR_PRD, int FR_YR, java.util.Date FR_DT, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlRepFrColumnHeadings(java.lang.String FR_NM, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlRepFrDetails(java.lang.String FR_NM, java.lang.String FR_PRD, int FR_YR, java.util.Date FR_DT, java.util.Date CONVERSION_DATE, java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}