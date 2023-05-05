package com.ejb.txn.ap;

import com.ejb.exception.global.GlobalAmountInvalidException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;

import jakarta.ejb.Local;


@Local
public interface ApRecurringVoucherGenerationController {

    java.util.ArrayList getApSplAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApRvByCriteria(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.Integer OFFSET, java.lang.Integer LIMIT, double RV_INTRST_RT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void executeApRvGeneration(java.lang.Integer RV_CODE, java.util.Date RV_NXT_RN_DT, java.lang.String VOU_DCMNT_NMBR, java.util.Date VOU_DT, double RV_INTRST_RT, java.lang.String USR_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalAmountInvalidException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    byte getAdPrfApUseSupplierPulldown(java.lang.Integer AD_CMPNY);

}