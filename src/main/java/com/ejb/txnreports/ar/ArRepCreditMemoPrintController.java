package com.ejb.txnreports.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface ArRepCreditMemoPrintController {

    java.util.ArrayList executeArRepInvoicePrint(java.util.ArrayList invCodeList, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.util.ArrayList executeArRepInvoicePrintSub(java.util.ArrayList invCodeList, java.lang.Integer AD_CMPNY);

}