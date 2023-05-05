package com.ejb.txn.ap;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;

@Local
public interface ApFindPurchaseOrderController {

    java.util.ArrayList getGlFcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApPoByCriteria(java.util.HashMap criteria, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.String ORDER_BY, java.lang.Integer AD_BRNCH, boolean isPoLookup, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    byte getAdPrfEnableApPOBatch(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApOpenVbAll(java.lang.String TYPE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.lang.Integer getApPoSizeByCriteria(java.util.HashMap criteria, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    byte getAdPrfApUseSupplierPulldown(java.lang.Integer AD_CMPNY);

}