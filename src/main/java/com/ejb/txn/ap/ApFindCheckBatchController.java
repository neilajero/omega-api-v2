package com.ejb.txn.ap;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;


@Local
public interface ApFindCheckBatchController {

    java.util.ArrayList getApCbByCriteria(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer getApCbSizeByCriteria(java.util.HashMap criteria, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}