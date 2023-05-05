package com.ejb.txn.gl;

import jakarta.ejb.Local;

@Local
public interface GlJournalImportController {

    java.util.ArrayList getGlJsAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlAcAllEditableOpenAndFutureEntry(java.lang.Integer AD_CMPNY);

    long executeGlJrImport(java.lang.String JS_NM, java.lang.String PRD_NM, int YR, java.lang.String USR_NM, java.lang.Integer RES_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

}