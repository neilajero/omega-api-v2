package com.ejb.txnreports.ap;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.ad.AdPreferenceDetails;

import jakarta.ejb.Local;


@Local
public interface ApRepAgingController {

    java.util.ArrayList getApScAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApStAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList executeApRepAging(java.util.HashMap criteria, java.lang.String AGNG_BY, java.lang.String ORDER_BY, java.lang.String GROUP_BY, java.lang.String currency, java.util.ArrayList adBrnchList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdPreferenceDetails getAdPreference(java.lang.Integer AD_CMPNY);

}