package com.ejb.txnreports.ap;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;


@Local
public interface ApRepApRegisterController {

    void executeSpApRepRegister(String STORED_PROCEDURE, String SUPPLIER_CODE, java.util.Date DT_FRM, java.util.Date DT_TO, boolean INCLUDE_UNPOSTED, boolean INCLUDE_PAYMENT, boolean INCLUDE_DIRECT_CHECK, String BRANCH_CODES, Integer AD_COMPANY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getApScAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApStAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAllVouchers(java.util.HashMap criteria, java.util.ArrayList adBrnchList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList executeApRepApRegister(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.String GROUP_BY, boolean SHOW_ENTRIES, boolean SUMMARIZE, boolean INCLUDE_DC, java.util.ArrayList adBrnchList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getApOpenVbAll(java.lang.String DPRTMNT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void getUpdateVoucherReferenceByDocumentNumber(java.lang.String VOU_DCMNT_NMBR, java.lang.String VOU_RFRNC_NMBR, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

}