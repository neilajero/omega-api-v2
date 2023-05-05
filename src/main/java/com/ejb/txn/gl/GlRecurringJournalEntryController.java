package com.ejb.txn.gl;

import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.gl.GlRecurringJournalDetails;
import com.util.mod.gl.GlModRecurringJournalDetails;

import jakarta.ejb.Local;

@Local
public interface GlRecurringJournalEntryController {
    java.util.ArrayList getGlJcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdUsrAll(java.lang.Integer AD_CMPNY);

    short getAdPrfGlJournalLineNumber(java.lang.Integer AD_CMPNY);

    GlModRecurringJournalDetails getGlRjByRjCode(java.lang.Integer RJ_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void saveGlRjEntry(GlRecurringJournalDetails details, java.lang.String JC_NM, java.lang.String JB_NM, java.util.ArrayList rjlList, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyDeletedException, GlobalAccountNumberInvalidException;

    void deleteGlRjEntry(java.lang.Integer RJ_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableGlJournalBatch(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlOpenJbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

}