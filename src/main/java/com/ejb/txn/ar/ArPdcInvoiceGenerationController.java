package com.ejb.txn.ar;

import com.ejb.exception.global.GlobalBranchAccountNumberInvalidException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;

import jakarta.ejb.Local;

@Local
public interface ArPdcInvoiceGenerationController {

    java.util.ArrayList getArPdcByCriteria(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void executeArPdcInvoiceGeneration(java.lang.Integer PDC_CODE, java.lang.String INV_NMBR, java.lang.String USR_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalBranchAccountNumberInvalidException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

}