package com.ejb.txnreports.cm;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.reports.cm.CmRepDailyCashPositionSummaryDetails;

import jakarta.ejb.Local;

@Local
public interface CmRepDailyCashPositionSummaryController {

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    CmRepDailyCashPositionSummaryDetails executeCmRepDailyCashPositionSummary(java.util.HashMap criteria, boolean includeDirectChecks, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}