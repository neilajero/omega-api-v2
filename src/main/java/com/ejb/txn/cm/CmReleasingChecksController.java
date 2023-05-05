package com.ejb.txn.cm;

import com.ejb.exception.ap.ApCHKCheckNumberNotUniqueException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;

import jakarta.ejb.Local;

@Local
public interface CmReleasingChecksController {

    java.util.ArrayList getApSplAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlFcAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApOpenCbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    byte getAdPrfEnableApCheckBatch(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getCmChkReleasableByCriteria(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void executeCmReleasingChecks(java.lang.Integer CHK_CODE, java.lang.String CHK_NMBR, java.util.Date CHK_CHCK_DT, java.util.Date CHK_DT_RLSD, java.lang.String CHK_RMRKS, java.lang.String USR_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, ApCHKCheckNumberNotUniqueException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    byte getAdPrfApUseSupplierPulldown(java.lang.Integer AD_CMPNY);

}