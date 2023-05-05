package com.ejb.txn.ap;

import com.ejb.exception.ap.ApSCCoaGlExpenseAccountNotFoundException;
import com.ejb.exception.ap.ApSCCoaGlPayableAccountNotFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.mod.ap.ApModSupplierClassDetails;

import jakarta.ejb.Local;


@Local
public interface ApSupplierClassController {

    java.util.ArrayList getApScAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getApTcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApWtcAll(java.lang.Integer AD_CMPNY);

    void addApScEntry(ApModSupplierClassDetails mdetails, java.lang.String SC_TC_NM, java.lang.String SC_WTC_NM, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, ApSCCoaGlPayableAccountNotFoundException, ApSCCoaGlExpenseAccountNotFoundException;

    void updateApScEntry(ApModSupplierClassDetails mdetails, java.lang.String SC_TC_NM, java.lang.String SC_WTC_NM, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, ApSCCoaGlPayableAccountNotFoundException, ApSCCoaGlExpenseAccountNotFoundException;

    void deleteApScEntry(java.lang.Integer SC_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}