package com.ejb.txn.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;

@Local
public interface InvFindLocationController {

    java.util.ArrayList getInvLocByCriteria(
            java.util.HashMap criteria, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    java.lang.Integer getInvLocSizeByCriteria(java.util.HashMap criteria, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

}