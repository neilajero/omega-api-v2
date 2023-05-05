package com.ejb.txn.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;

@Local
public interface ArFindPersonelController {

    java.util.ArrayList getArPtNmAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArPeByCriteria(java.util.HashMap criteria, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.String ORDER_BY, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer getArPeSizeByCriteria(java.util.HashMap criteria, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}