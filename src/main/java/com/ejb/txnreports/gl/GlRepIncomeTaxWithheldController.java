package com.ejb.txnreports.gl;

import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface GlRepIncomeTaxWithheldController {

    java.util.ArrayList executeGlRepIncomeTaxWithheld(java.util.Date DT_FRM, java.util.Date DT_TO, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvReportTypeAll(java.lang.Integer AD_CMPNY);

    AdCompanyDetails getArCmp(java.lang.Integer AD_CMPNY);

}