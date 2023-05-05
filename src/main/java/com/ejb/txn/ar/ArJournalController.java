package com.ejb.txn.ar;

import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;

@Local
public interface ArJournalController {

    java.util.ArrayList getArDrByInvCode(java.lang.Integer INV_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getArDrByRctCode(java.lang.Integer RCT_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getAdPrfArInvoiceLineNumber(java.lang.Integer AD_CMPNY);

    void saveArDrEntry(java.lang.Integer PRMRY_KEY, java.util.ArrayList drList, java.lang.String transactionType, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalAccountNumberInvalidException;

}