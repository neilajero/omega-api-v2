package com.ejb.txn.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;

@Local
public interface ArReceiptBatchPrintController {

    java.util.ArrayList getArOpenRbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableArReceiptBatch(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArRctByCriteria(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}