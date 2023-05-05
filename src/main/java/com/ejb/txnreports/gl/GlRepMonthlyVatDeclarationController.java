package com.ejb.txnreports.gl;

import com.util.ad.AdCompanyDetails;
import com.util.reports.gl.GlRepMonthlyVatDeclarationDetails;

import jakarta.ejb.Local;

@Local
public interface GlRepMonthlyVatDeclarationController {

    java.util.ArrayList getAdLvProvincesAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvReportTypeAll(java.lang.Integer AD_CMPNY);

    GlRepMonthlyVatDeclarationDetails executeGlRepMonthlyVatDeclaration(java.util.Date DT_FRM, java.util.Date DT_TO, java.lang.Integer AD_CMPNY);

    java.lang.StringBuilder executeGlRepReliefPurchases(java.util.Date DT_FRM, java.util.Date DT_TO, java.lang.Integer AD_CMPNY);

    java.lang.StringBuilder executeGlRepReliefSales(java.util.Date DT_FRM, java.util.Date DT_TO, java.lang.Integer AD_CMPNY);

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}