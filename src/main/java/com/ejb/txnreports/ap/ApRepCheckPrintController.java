package com.ejb.txnreports.ap;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface ApRepCheckPrintController {

    java.util.ArrayList executeApRepCheckPrint(java.util.ArrayList chkCodeList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    boolean getAdPrfUseBankForm(java.lang.Integer AD_CMPNY);

}