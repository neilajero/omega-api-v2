package com.ejb.txnreports.gl;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.mod.ap.ApModSupplierDetails;

import jakarta.ejb.Local;

@Local
public interface GlRepInvestorLedgerController {

    java.util.ArrayList getApSplAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    byte getAdPrfApUseSupplierPulldown(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGenQlfrAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdLvReportTypeAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList executeGlRepInvestorLedger(java.lang.String SUPPLIER_CODE, java.util.Date GL_DT, boolean SL_INCLD_UNPSTD, java.lang.String ORDER_BY, java.util.ArrayList branchList, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ApModSupplierDetails getApSplBySplSupplierCode(java.lang.String SPL_SPPLR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getGlFcAllWithDefault(java.lang.Integer AD_CMPNY);

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}