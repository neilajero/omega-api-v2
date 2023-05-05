package com.ejb.txn.gl;

import com.ejb.exception.gl.GlCOANoChartOfAccountFoundException;
import com.ejb.exception.gl.GlJLINoJournalLineInterfacesFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.gl.GlJournalInterfaceDetails;

import jakarta.ejb.Local;

@Local
public interface GlJournalInterfaceMaintenanceController {

    java.util.ArrayList getGlJliByJriCode(java.lang.Integer JRI_CODE, java.lang.Integer AD_CMPNY) throws GlJLINoJournalLineInterfacesFoundException;

    GlJournalInterfaceDetails getGlJriByJriCode(java.lang.Integer JRI_CODE, java.lang.Integer AD_CMPNY);

    void saveGlJriEntry(GlJournalInterfaceDetails details, java.util.ArrayList list, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlCOANoChartOfAccountFoundException;

    void deleteGlJriEntry(java.lang.Integer JRI_CODE, java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}