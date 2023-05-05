package com.ejb.txn.ar;

import com.ejb.exception.ar.ArINVInvoiceDoesNotExist;
import com.ejb.exception.ar.ArINVOverapplicationNotAllowedException;
import com.ejb.exception.ar.ArRICustomerRequiredException;
import com.ejb.exception.global.*;
import com.util.mod.ar.ArModReceiptImportPreferenceDetails;

import jakarta.ejb.Local;

@Local
public interface ArReceiptImportController {

    ArModReceiptImportPreferenceDetails getArRipByRipType(java.lang.String RIP_TYPE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    void importRctAi(java.util.ArrayList rctList, boolean isSummarized, java.lang.String RB_NM, java.lang.String TC_NM, java.lang.String CRTD_BY, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalDocumentNumberNotUniqueException, GlobalTransactionAlreadyLockedException, GlobalNotAllTransactionsArePostedException, ArINVInvoiceDoesNotExist, GlobalNoRecordFoundException, ArINVOverapplicationNotAllowedException, GlobalRecordInvalidException, GlobalRecordInvalidForCurrentBranchException, GlobalRecordDisabledException, GlobalJournalNotBalanceException, GlobalAmountInvalidException, ArRICustomerRequiredException;

    void importRctIli(java.util.ArrayList miscRctList, boolean isSummarized, java.lang.String RB_NM, java.lang.String TC_NM, boolean taxableServiceCharge, boolean taxableDiscount, java.lang.String CRTD_BY, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalInvItemLocationNotFoundException, GlobalBranchAccountNumberInvalidException, GlobalNoRecordFoundException, GlobalDocumentNumberNotUniqueException, GlobalRecordInvalidForCurrentBranchException, GlobalRecordInvalidException, GlobalRecordDisabledException, GlobalJournalNotBalanceException, GlobalAmountInvalidException, GlobalRecordAlreadyExistException;

    void importRctIl(java.util.ArrayList miscRctList, boolean isSummarized, java.lang.String RB_NM, java.lang.String TC_NM, boolean taxableServiceCharge, boolean taxableDiscount, java.lang.String CRTD_BY, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalDocumentNumberNotUniqueException, GlobalNoRecordFoundException, GlobalRecordInvalidForCurrentBranchException, GlobalRecordDisabledException, GlobalRecordInvalidException, GlobalBranchAccountNumberInvalidException, GlobalJournalNotBalanceException, GlobalAmountInvalidException, GlobalRecordAlreadyExistException;

}