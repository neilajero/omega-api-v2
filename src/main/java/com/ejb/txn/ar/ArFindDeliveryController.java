package com.ejb.txn.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;

@Local
public interface ArFindDeliveryController {

    java.util.ArrayList getArDvByCriteria(java.util.HashMap criteria, boolean isChild, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.String ORDER_BY, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}