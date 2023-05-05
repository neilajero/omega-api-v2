package com.ejb.txn.ap;

import com.ejb.exception.global.GlobalBranchAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;

@Local
public interface ApJournalController {

    java.util.ArrayList getApDrByChkCode(java.lang.Integer CHK_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getApDrByVouCode(java.lang.Integer VOU_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getApDrByPoCode(java.lang.Integer PO_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getAdPrfApJournalLineNumber(java.lang.Integer AD_CMPNY);

    void saveApDrEntry(java.lang.Integer PRMRY_KEY, java.util.ArrayList drList, java.lang.String transactionType, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalBranchAccountNumberInvalidException;

}