package com.ejb.txn.ap;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;

@Local
public interface ApVoucherBatchPrintController {

    byte getAdPrfEnableApVoucherBatch(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApVouByCriteria(java.util.HashMap criteria, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.String ORDER_BY, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    byte getAdPrfApUseSupplierPulldown(java.lang.Integer AD_CMPNY);

}