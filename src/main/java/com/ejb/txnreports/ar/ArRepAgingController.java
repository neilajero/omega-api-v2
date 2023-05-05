package com.ejb.txnreports.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.ad.AdPreferenceDetails;

import jakarta.ejb.Local;
import java.sql.ResultSet;

@Local
public interface ArRepAgingController {

    void executeSpArRepAging(String STORED_PROCEDURE, java.util.Date CUTT_OFF_DT, boolean INCLUDE_UNPOSTED, String AGING_BY, Integer AD_COMPANY) throws GlobalNoRecordFoundException;

    void executeSpArRepAging(String STORED_PROCEDURE, String GL_ACCNT_NMBR_FRM, String GL_ACCNT_NMBR_TO, java.util.Date DT_FRM, java.util.Date DT_TO, String BRANCH_CODES, String AMOUNT_TYP, boolean INCLUDE_UNPOSTED, boolean INCLUDE_UNPOSTED_SL, boolean SHOW_ZEROES, Integer AD_COMPANY) throws GlobalNoRecordFoundException;

    java.util.ArrayList executeArRepAging(java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.String AGNG_BY, java.lang.String ORDER_BY, java.lang.String GROUP_BY, java.lang.String currency, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    AdPreferenceDetails getAdPreference(java.lang.Integer AD_CMPNY);

}