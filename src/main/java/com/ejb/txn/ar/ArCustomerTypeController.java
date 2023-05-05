package com.ejb.txn.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ar.ArCustomerTypeDetails;

import jakarta.ejb.Local;

@Local
public interface ArCustomerTypeController {

    java.util.ArrayList getArCtAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    void addArCtEntry(ArCustomerTypeDetails details, java.lang.String CT_BA_NM, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void updateArCtEntry(ArCustomerTypeDetails details, java.lang.String CT_BA_NM, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void deleteArCtEntry(java.lang.Integer CT_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}