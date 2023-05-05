package com.ejb.txnreports.gl;

import com.ejb.exception.gl.GlFCNoFunctionalCurrencyFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface GlRepDailyRateListController {

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlFcAll(java.lang.Integer AD_CMPNY) throws GlFCNoFunctionalCurrencyFoundException;

    java.util.ArrayList executeDailyRateList(java.util.HashMap criteria, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}