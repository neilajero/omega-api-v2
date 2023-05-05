package com.ejb.txnreports.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.ad.AdPreferenceDetails;

import jakarta.ejb.Local;
import java.sql.ResultSet;
import java.util.Date;

@Local
public interface ArRepStatementController {
    void executeSpArRepStatementOfAccount(String STORED_PROCEDURE, Date CUTT_OF_DT, String CUSTOMER_BATCH, String CUSTOMER_CODE, boolean INCLUDE_UNPOSTED, boolean INCLUDE_ADVANCE, boolean INCLUDE_ADVANCE_ONLY, String BRANCH_CODES, Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdLvReportTypeAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArCcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArCtAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvCustomerBatchAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvCustomerDepartmentAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArCstAll(java.util.ArrayList customerBatchList, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArCstAllbyCustomerBatch(java.lang.String CST_BTCH, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdPreferenceDetails getAdPreference(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArSmlAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList executeSpArRepStatement(ResultSet rs, java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.String AGNG_BY, java.lang.String GROUP_BY, java.lang.String currency, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getArOpenIbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvDocumentTypeAll(java.lang.Integer AD_CMPNY);
}