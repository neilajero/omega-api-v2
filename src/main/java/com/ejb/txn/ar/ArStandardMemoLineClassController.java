package com.ejb.txn.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.util.ar.ArCustomerDetails;
import com.util.ar.ArStandardMemoLineDetails;
import com.util.mod.ar.ArModStandardMemoLineClassDetails;

import jakarta.ejb.FinderException;
import jakarta.ejb.Local;

@Local
public interface ArStandardMemoLineClassController {

    void saveArSmcEntry(java.util.ArrayList list, java.lang.String USR_NM, java.lang.String CC_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void deleteArSmcEntry(int SMC_CODE) throws GlobalRecordAlreadyDeletedException;

    java.util.ArrayList getAllSmlNm(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAllCcNm(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ArModStandardMemoLineClassDetails getSmcByCcNmSmlNmCstCstmrCodeBrNm(java.lang.String CC_NM, java.lang.String SML_NM, java.lang.String CST_CSTMR_CODE, java.lang.String BR_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException, FinderException;

    java.util.ArrayList getAllCst(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAllBrNm(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAllSmcByCcNm(java.lang.String CC_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ArStandardMemoLineDetails getArSmlBySmlName(java.lang.String SML_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ArCustomerDetails getArCstByCstCstmrCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}