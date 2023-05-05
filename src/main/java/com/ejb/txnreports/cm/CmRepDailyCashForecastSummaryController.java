package com.ejb.txnreports.cm;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.reports.cm.CmRepDailyCashForecastSummaryDetails;

import jakarta.ejb.Local;

@Local
public interface CmRepDailyCashForecastSummaryController {

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    CmRepDailyCashForecastSummaryDetails executeCmRepDailyCashForecastSummary(java.util.HashMap criteria, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}