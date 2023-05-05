package com.ejb.txn.gl;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.util.mod.gl.GlModTaxInterfaceDetails;

import jakarta.ejb.Local;


@Local
public interface GlTaxInterfaceMaintenanceController {

    java.util.ArrayList getArCstmrAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArTcAll(java.lang.Integer AD_CMPNY);

    double getArTcRate(java.lang.String TC_NM, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArWtcAll(java.lang.Integer AD_CMPNY);

    double getArWtcRate(java.lang.String WTC_NM, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApSpplrAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApTcAll(java.lang.Integer AD_CMPNY);

    double getApTcRate(java.lang.String TC_NM, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApWtcAll(java.lang.Integer AD_CMPNY);

    double getApWtcRate(java.lang.String WTC_NM, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlTiByCriteria(java.util.HashMap criteria, java.lang.Integer OFFSET, java.lang.Integer LIMIT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void saveGlTiMaintenance(GlModTaxInterfaceDetails mdetails, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}