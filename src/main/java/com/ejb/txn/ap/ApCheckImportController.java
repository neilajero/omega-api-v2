package com.ejb.txn.ap;

import com.ejb.exception.ap.ApCHKCheckNumberNotUniqueException;
import com.ejb.exception.ap.ApCHKVoucherHasNoWTaxCodeException;
import com.ejb.exception.ap.ApVOUOverapplicationNotAllowedException;
import com.ejb.exception.global.*;

import jakarta.ejb.Local;


@Local
public interface ApCheckImportController {

    void importCheck(java.util.ArrayList list, java.lang.String FC_NM, java.lang.String CB_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalDocumentNumberNotUniqueException, ApCHKCheckNumberNotUniqueException, ApCHKVoucherHasNoWTaxCodeException, GlobalNoRecordFoundException, GlobalJournalNotBalanceException, ApVOUOverapplicationNotAllowedException, GlobalTransactionAlreadyLockedException, GlobalRecordInvalidException, GlobalTransactionAlreadyPostedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}