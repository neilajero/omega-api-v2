package com.ejb.txn.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;


@Local
public interface ArFindCustomerController {

    java.util.ArrayList getAdLvCustomerBatchAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArCtAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArCcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArCstByCriteria(java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.String ORDER_BY, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer getArCstSizeByCriteria(java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBrResAll(int resCode, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}