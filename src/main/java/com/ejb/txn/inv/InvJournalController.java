package com.ejb.txn.inv;

import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;

@Local
public interface InvJournalController {

    java.util.ArrayList getInvDrByAdjCode(java.lang.Integer ADJ_CODE, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    java.util.ArrayList getInvDrByStCode(java.lang.Integer ST_CODE, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    java.util.ArrayList getInvDrByBstCode(java.lang.Integer BST_CODE, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getAdPrfInvJournalLineNumber(java.lang.Integer AD_CMPNY);

    void saveInvDrEntry(java.lang.Integer PRMRY_KEY, java.util.ArrayList drList,
                        java.lang.String transactionType, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalAccountNumberInvalidException;

}