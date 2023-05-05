package com.ejb.txnreports.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;

@Local
public interface InvRepItemCostingController {

    java.util.ArrayList getAdLvInvItemCategoryAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvLocAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList executeInvRepItemCosting(java.util.HashMap criteria, java.lang.String LCTN, java.lang.String CTGRY, boolean SHW_CMMTTD_QNTTY, boolean INCLD_UNPSTD, boolean SHW_ZRS, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    void executeInvFixItemCosting(java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void executeInvFixItemCosting(java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY, String companyShortName) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}