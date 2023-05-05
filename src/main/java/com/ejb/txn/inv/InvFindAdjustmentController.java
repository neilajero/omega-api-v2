package com.ejb.txn.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;

@Local
public interface InvFindAdjustmentController {

   java.util.ArrayList getInvAdjByCriteria(java.util.HashMap criteria, java.lang.Integer OFFSET,
                                           java.lang.Integer LIMIT, java.lang.String ORDER_BY,
                                           java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
           throws GlobalNoRecordFoundException;

   java.lang.Integer getInvAdjSizeByCriteria(java.util.HashMap criteria,
                                             java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
           throws GlobalNoRecordFoundException;

}