package com.ejb.txnreports.gl;

import com.util.ad.AdCompanyDetails;
import com.util.reports.gl.GlRepIncomeTaxReturnDetails;

import jakarta.ejb.Local;

@Local
public interface GlRepIncomeTaxReturnController {

    java.util.ArrayList getGlTaxInterfaceList(java.util.Date DT_FRM, java.util.Date DT_TO, java.lang.Integer AD_CMPNY);

    GlRepIncomeTaxReturnDetails executeGlIncomeTaxReturn(java.util.Date DT_FRM, java.util.Date DT_TO, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvReportTypeAll(java.lang.Integer AD_CMPNY);

    AdCompanyDetails getArCmp(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvProvincesAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAllReportValues(java.lang.Integer AD_CMPNY);

    void saveGlReportValue(java.util.ArrayList list, java.lang.Integer AD_CMPNY);

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}