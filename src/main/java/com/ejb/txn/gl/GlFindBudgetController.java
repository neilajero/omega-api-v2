package com.ejb.txn.gl;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;


@Local
public interface GlFindBudgetController {

    java.util.ArrayList getGlBoAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlBgtAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlBgaByCriteria(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer getGlBgaSizeByCriteria(java.util.HashMap criteria, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}