package com.ejb.txn.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;

@Local
public interface ArFindJobOrderController {

    java.util.ArrayList getArJoForProcessingByBrCode(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArJoByCriteria(java.util.HashMap criteria, boolean isChild, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.String ORDER_BY, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAllJobOrderTypeName(java.lang.Integer AD_CMPNY);

    java.lang.Integer getArJoSizeByCriteria(java.util.HashMap criteria, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}