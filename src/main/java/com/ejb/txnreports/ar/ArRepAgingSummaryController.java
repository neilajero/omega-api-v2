package com.ejb.txnreports.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.ad.AdPreferenceDetails;

import jakarta.ejb.Local;
import java.sql.ResultSet;

@Local
public interface ArRepAgingSummaryController {

    void executeSpArRepAgingSummary( String STORED_PROCEDURE, java.util.Date CUTT_OFF_DT, boolean INCLUDE_UNPOSTED,  String AGING_BY, Integer AD_COMPANY)
            throws GlobalNoRecordFoundException;

    java.util.ArrayList executeArRepAgingSummarySP(ResultSet rs, java.util.HashMap criteria,
                                                   java.util.ArrayList branchList, java.lang.String AGNG_BY,
                                                   java.lang.String GROUP_BY, java.lang.String currency,
                                                   java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;


    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    AdPreferenceDetails getAdPreference(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArIbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

}