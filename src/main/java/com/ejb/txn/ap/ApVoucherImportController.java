package com.ejb.txn.ap;

import com.ejb.exception.global.*;

import jakarta.ejb.Local;


@Local
public interface ApVoucherImportController {

    void importVoucher(java.util.ArrayList list, java.lang.String PYT_NM, java.lang.String TC_NM, java.lang.String WTC_NM, java.lang.String FC_NM, java.lang.String VB_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalDocumentNumberNotUniqueException, GlobalPaymentTermInvalidException, GlobalInvItemLocationNotFoundException, GlobalReferenceNumberNotUniqueException, GlobalNoRecordFoundException, GlobalJournalNotBalanceException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

}