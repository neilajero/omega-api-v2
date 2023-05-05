package com.ejb.txn.cm;

import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;


@Local
public interface CmJournalController {

    java.util.ArrayList getCmDrByFtCode(java.lang.Integer FT_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getCmDrByAdjCode(java.lang.Integer ADJ_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    void saveCmDrEntry(java.lang.Integer PRMRY_KEY, java.util.ArrayList drList, java.lang.String transactionType, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalAccountNumberInvalidException;

    short getAdPrfGlJournalLineNumber(java.lang.Integer AD_CMPNY);

}