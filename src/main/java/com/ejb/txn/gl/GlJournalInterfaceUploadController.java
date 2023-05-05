package com.ejb.txn.gl;

import com.ejb.exception.gl.GlJRIJournalNotBalanceException;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.gl.GlJournalInterfaceDetails;

import jakarta.ejb.Local;

@Local
public interface GlJournalInterfaceUploadController {

    void saveGlJriEntry(GlJournalInterfaceDetails details, java.util.ArrayList glJournalLineInterfaceList, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException, GlJRIJournalNotBalanceException;

}