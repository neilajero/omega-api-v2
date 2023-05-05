package com.ejb.txn.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;

@Local
public interface ArFindInvoiceController {

    java.util.ArrayList getAdLvCustomerBatchAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArCstAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlFcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArOpenIbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvInvShiftAll(java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableArInvoiceBatch(java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableInvShift(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArInvByCriteria(java.util.HashMap criteria, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.String ORDER_BY, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer getArInvSizeByCriteria(java.util.HashMap criteria, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}