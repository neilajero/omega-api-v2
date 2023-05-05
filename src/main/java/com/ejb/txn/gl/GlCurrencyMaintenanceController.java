package com.ejb.txn.gl;

import com.ejb.exception.gl.GlFCFunctionalCurrencyAlreadyAssignedException;
import com.ejb.exception.gl.GlFCFunctionalCurrencyAlreadyDeletedException;
import com.ejb.exception.gl.GlFCFunctionalCurrencyAlreadyExistException;
import com.ejb.exception.gl.GlFCNoFunctionalCurrencyFoundException;
import com.util.gl.GlFunctionalCurrencyDetails;

import jakarta.ejb.Local;


@Local
public interface GlCurrencyMaintenanceController {

    java.util.ArrayList getGlFcAll(java.lang.Integer AD_CMPNY) throws GlFCNoFunctionalCurrencyFoundException;

    void addGlFcEntry(GlFunctionalCurrencyDetails details, java.lang.Integer AD_CMPNY) throws GlFCFunctionalCurrencyAlreadyExistException;

    void updateGlFcEntry(GlFunctionalCurrencyDetails details, java.lang.Integer AD_CMPNY) throws GlFCFunctionalCurrencyAlreadyExistException, GlFCFunctionalCurrencyAlreadyAssignedException, GlFCFunctionalCurrencyAlreadyDeletedException;

    void deleteGlFcEntry(java.lang.Integer FC_CODE, java.lang.Integer AD_CMPNY) throws GlFCFunctionalCurrencyAlreadyDeletedException, GlFCFunctionalCurrencyAlreadyAssignedException;

}