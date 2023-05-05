package com.ejb.txn.ap;

import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.mod.ap.ApModPurchaseRequisitionLineDetails;

import jakarta.ejb.Local;


@Local
public interface ApCanvassController {

    java.util.ArrayList getApSplAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    ApModPurchaseRequisitionLineDetails getApPrlByPrlCode(java.lang.Integer PRL_CODE, java.lang.Integer AD_CMPNY);

    void saveApCnvss(java.util.ArrayList cnvList, java.lang.Integer PRL_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getAdPrfApJournalLineNumber(java.lang.Integer AD_CMPNY);

    byte getAdPrfApUseSupplierPulldown(java.lang.Integer AD_CMPNY);

}