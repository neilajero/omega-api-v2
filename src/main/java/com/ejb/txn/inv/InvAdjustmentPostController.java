package com.ejb.txn.inv;

import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;

import jakarta.ejb.Local;

@Local
public interface InvAdjustmentPostController {

    java.util.ArrayList getInvAdjPostableByCriteria(java.util.HashMap criteria, java.lang.Integer OFFSET,
                                                    java.lang.Integer LIMIT, java.lang.String ORDER_BY,
                                                    java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    void executeInvAdjPost(java.lang.Integer ADJ_CODE, java.lang.String USR_NM,
                           java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException,
            GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException,
            GlobalJournalNotBalanceException, GlobalInventoryDateException, GlobalBranchAccountNumberInvalidException,
            GlobalAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException,
            GlobalExpiryDateNotFoundException, GlobalRecordInvalidException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}