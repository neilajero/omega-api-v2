package com.ejb.txn.ar;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;

import jakarta.ejb.Local;

@Local
public interface ArInvoicePostController {

    byte getAdPrfEnableArInvoiceBatch(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArInvPostableByCriteria(java.util.HashMap criteria, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.String ORDER_BY, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void executeArInvPost(java.lang.Integer INV_CODE, java.lang.String USR_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalExpiryDateNotFoundException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}