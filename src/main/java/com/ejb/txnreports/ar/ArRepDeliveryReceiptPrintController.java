package com.ejb.txnreports.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface ArRepDeliveryReceiptPrintController {

    java.util.ArrayList executeArRepDeliveryReceiptPrint(java.util.ArrayList invCodeList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}