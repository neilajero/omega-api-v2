package com.ejb.txnreports.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;

@Local
public interface InvRepUsageVarianceController {

    java.util.ArrayList getAdLvInvItemCategoryAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvLocAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList executeInvRepUsageVariance(java.lang.String CTGRY_NM, java.lang.String LOC_NM, java.util.Date UV_DT, boolean INCLD_ASSMBLY, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}