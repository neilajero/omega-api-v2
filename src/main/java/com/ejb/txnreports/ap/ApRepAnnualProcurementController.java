package com.ejb.txnreports.ap;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface ApRepAnnualProcurementController {

    java.util.ArrayList executeApRepAnnualProcurement(java.util.HashMap criteria, java.lang.String month, java.util.ArrayList adBrnchList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvDEPARTMENT(java.lang.Integer AD_CMPNY);

    java.lang.String getAdUsrDeptartment(java.lang.String USR_NM, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}