package com.ejb.txn.gl;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;

@Local
public interface GlFindChartOfAccountController {

    java.util.ArrayList getGlCoaByCriteria(java.util.HashMap criteria, java.lang.String COA_ACCNT_FRM, java.lang.String COA_ACCNT_TO, java.util.ArrayList vsvDescList, java.lang.String COA_ACCNT_TYP, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.util.ArrayList adBrnchList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer getGlCoaSizeByCriteria(java.util.HashMap criteria, java.lang.String COA_ACCNT_FRM, java.lang.String COA_ACCNT_TO, java.util.ArrayList vsvDescList, java.lang.String COA_ACCNT_TYP, java.util.ArrayList adBrnchList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getGenQlAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGenVsAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}