package com.ejb.txnreports.gl;

import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface GlRepDetailTrialBalanceController {
    void executeGlRecomputeCoaBalance(java.lang.String GL_ACCNT_NMBR_FRM, java.lang.String GL_ACCNT_NMBR_TO, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlobalAccountNumberInvalidException;

    void executeSpGlRepDetailTrialBalance(String STORED_PROCEDURE, String DTB_ACCNT_NMBR_FRM, String DTB_ACCNT_NMBR_TO, java.util.Date DT_FRM, java.util.Date DT_TO, boolean DTB_INCLD_UNPSTD, boolean DTB_INCLD_UNPSTD_SL, boolean DTB_SHW_ZRS, String AMOUNT_TYP, String BRANCH_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlobalAccountNumberInvalidException;

    void executeSpGlRecomputeCoaBalance(java.sql.ResultSet rs, java.lang.String GL_ACCNT_NMBR_FRM, java.lang.String GL_ACCNT_NMBR_TO, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlobalAccountNumberInvalidException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    byte getPrfEnableGlRecomputeCoaBalance(java.lang.Integer AD_CMPNY);

}