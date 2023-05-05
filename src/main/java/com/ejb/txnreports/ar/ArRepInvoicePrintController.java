package com.ejb.txnreports.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface ArRepInvoicePrintController {

    java.util.ArrayList executeArRepInvoicePrint(java.util.ArrayList invCodeList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    byte getInvDebitMemo(java.util.ArrayList invCodeList, java.lang.Integer AD_CMPNY);

    java.util.ArrayList executeArRepInvoicePrintSub(java.util.ArrayList invCodeList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList executeArRepInvoicePrintSubDr(java.util.ArrayList invCodeList, java.lang.Integer AD_CMPNY);

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    java.lang.String getPrfArSalesInvoiceDataSource(java.lang.Integer AD_CMPNY);

}