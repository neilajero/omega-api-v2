package com.ejb.txnreports.gl;

import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface GlRepDetailBalanceSheetController {
    void executeSpGlRepDetailBalanceSheet(String STORED_PROCEDURE, String GL_ACCNT_NMBR_FRM, String GL_ACCNT_NMBR_TO, java.util.Date DT_FRM, java.util.Date DT_TO, boolean INCLUDE_UNPOSTED, boolean INCLUDE_UNPOSTED_SL, boolean SHOW_ZEROES, String AMOUNT_TYP, String BRANCH_CODES, Integer AD_COMPANY) throws GlobalNoRecordFoundException, GlobalAccountNumberInvalidException;

    AdCompanyDetails getAdCompany(Integer AD_CMPNY);
}