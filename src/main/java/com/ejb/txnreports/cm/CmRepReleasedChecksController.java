package com.ejb.txnreports.cm;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface CmRepReleasedChecksController {

    java.util.ArrayList getApScAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApStAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList executeCmRepReleasedChecks(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.String GROUP_BY, java.util.ArrayList adBrnchList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}