package com.ejb.txnreports.gl;

import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface GlRepGeneralLedgerController {
    void executeSpGlRepGeneralLedger(String STORED_PROCEDURE, String GL_ACCNT_NMBR_FRM, String GL_ACCNT_NMBR_TO, java.util.Date DT_FRM, java.util.Date DR_TO, String ACCOUNT_TYPE, boolean INCLUDE_UNPOSTED, String BRANCH_CODE, Integer AD_COMPANY) throws GlobalNoRecordFoundException, GlobalAccountNumberInvalidException;

    AdCompanyDetails getAdCompany(Integer companyCode);

}