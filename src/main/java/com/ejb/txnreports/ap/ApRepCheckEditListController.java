package com.ejb.txnreports.ap;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface ApRepCheckEditListController {

    java.util.ArrayList executeApRepCheckEditList(java.util.ArrayList chkCodeList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList executeApRepCheckEditListSub(java.util.ArrayList chkCodeList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}