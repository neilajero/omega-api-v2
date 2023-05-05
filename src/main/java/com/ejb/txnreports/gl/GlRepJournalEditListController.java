package com.ejb.txnreports.gl;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface GlRepJournalEditListController {

    java.util.ArrayList executeGlRepJournalEditList(java.util.ArrayList jrCodeList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}