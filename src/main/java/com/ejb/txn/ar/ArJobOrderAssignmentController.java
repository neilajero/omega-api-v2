package com.ejb.txn.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ar.ArPersonelDetails;
import com.util.mod.ar.ArModJobOrderLineDetails;

import jakarta.ejb.Local;

@Local
public interface ArJobOrderAssignmentController {

    java.util.ArrayList getArPeAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    ArModJobOrderLineDetails getArJolByJolCode(java.lang.Integer JOL_CODE, java.lang.Integer AD_CMPNY);

    void saveArJbOrdrAssgnmnt(java.util.ArrayList jaList, java.lang.Integer JOL_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

    short getAdPrfArInvoiceLineNumber(java.lang.Integer AD_CMPNY);

    ArPersonelDetails getArPrsnlByPeIdNmbr(java.lang.String PE_ID_NMBR, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}