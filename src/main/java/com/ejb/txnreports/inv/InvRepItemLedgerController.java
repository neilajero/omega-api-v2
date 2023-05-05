package com.ejb.txnreports.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface InvRepItemLedgerController {

    java.util.ArrayList getApSplAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArCstAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvReportTypeAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvInvItemCategoryAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getInvLocAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList executeInvRepItemLedger(java.util.HashMap criteria, boolean SHW_CMMTTD_QNTTY, java.lang.String referenceNumber, boolean INCLD_UNPSTD, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}