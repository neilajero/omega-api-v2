package com.ejb.txnreports.gl;

import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface GlRepChartOfAccountListController {

    java.util.ArrayList executeGlRepChartOfAccountList(java.lang.String COA_ACCNT_NMBR_FRM, java.lang.String COA_ACCNT_NMBR_TO, boolean COA_ENBL, boolean COA_DSBL, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlobalAccountNumberInvalidException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGenSgAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}