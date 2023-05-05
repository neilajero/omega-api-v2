package com.ejb.txnreports.gl;

import com.ejb.exception.gl.GlRepBGTPeriodOutOfRangeException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface GlRepBudgetController {

    java.util.ArrayList getGlAcvAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlBoAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlBgtAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList executeGlRepBudget(java.lang.String BGT_BO_NM, java.lang.String BGT_NM, java.lang.String BGT_PRD, int BGT_YR, java.lang.String BGT_AMNT_TYP, boolean DIS_INCLD_UNPSTD, boolean DIS_INCLD_UNPSTD_SL, boolean DTB_SHW_ZRS, java.util.ArrayList branchList, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlRepBGTPeriodOutOfRangeException;

    void executeSpGlRepBudget(String storedProceedureName, String BUDGET_ORGANIZATION, String BUDGET_NAME, java.util.Date DT_FRM, java.util.Date DR_TO, boolean INCLUDE_UNPOSTED, boolean INCLUDE_UNPOSTED_SL, boolean SHOW_ZEROES, String branchCodes, Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlRepBGTPeriodOutOfRangeException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}