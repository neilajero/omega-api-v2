package com.ejb.txn.ar;

import com.ejb.exception.ar.ArINVAmountExceedsCreditLimitException;
import com.ejb.exception.global.*;

import jakarta.ejb.Local;

@Local
public interface ArInvoiceImportController {

    void importInvoice(java.util.ArrayList list, java.lang.String PYT_NM, java.lang.String TC_NM, java.lang.String WTC_NM, java.lang.String FC_NM, java.lang.String IB_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, ArINVAmountExceedsCreditLimitException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException, GlobalInventoryDateException, GlobalNoRecordFoundException, GlobalJournalNotBalanceException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

}