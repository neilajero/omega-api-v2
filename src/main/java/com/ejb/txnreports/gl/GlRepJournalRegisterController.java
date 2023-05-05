package com.ejb.txnreports.gl;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface GlRepJournalRegisterController {

    java.util.ArrayList executeGlRepJournalRegister(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.String GROUP_BY, java.util.ArrayList branchList, boolean SHOW_ENTRIES, boolean SUMMARIZE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getGlJsAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlJcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlJbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

}