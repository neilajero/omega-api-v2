package com.ejb.txnreports.ap;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface ApRepCheckRegisterController {
    void executeSpApRepCheckRegister(String STORED_PROCEDURE, String SUPPLIER_CODE, java.util.Date DT_FRM, java.util.Date DT_TO, boolean INCLUDE_UNPOSTED, boolean DIRECT_CHECK_ONLY, String BRANCH_CODES, Integer AD_COMPANY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getApScAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApStAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getEnableGlCoa(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}