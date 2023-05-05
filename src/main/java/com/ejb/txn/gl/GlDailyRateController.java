package com.ejb.txn.gl;

import com.ejb.exception.gl.GlFCNoFunctionalCurrencyFoundException;
import com.ejb.exception.gl.GlFCRFunctionalCurrencyRateAlreadyDeletedException;
import com.ejb.exception.gl.GlFCRFunctionalCurrencyRateAlreadyExistException;
import com.ejb.exception.gl.GlFCRNoFunctionalCurrencyRateFoundException;
import com.ejb.exception.global.GlobalConversionDateNotExistException;
import com.util.gl.GlFunctionalCurrencyRateDetails;
import com.util.mod.gl.GlModFunctionalCurrencyRateDetails;

import jakarta.ejb.Local;


@Local
public interface GlDailyRateController {

    GlModFunctionalCurrencyRateDetails getFrRateByFrName(java.lang.String FC_NM, java.lang.Integer AD_CMPNY) throws GlobalConversionDateNotExistException;

    double getFrRateByFrNameAndFrDate(java.lang.String FC_NM, java.util.Date CONVERSION_DATE, java.lang.Integer AD_CMPNY) throws GlobalConversionDateNotExistException;

    java.util.ArrayList getGlFcAll(java.lang.Integer AD_CMPNY) throws GlFCNoFunctionalCurrencyFoundException;

    GlModFunctionalCurrencyRateDetails getGlFcrByFrCode(java.lang.Integer FR_CODE, java.lang.Integer AD_CMPNY) throws GlFCRNoFunctionalCurrencyRateFoundException;

    void addGlFcrEntry(GlFunctionalCurrencyRateDetails details, java.lang.String FC_NM, java.lang.Integer AD_CMPNY) throws GlFCNoFunctionalCurrencyFoundException, GlFCRFunctionalCurrencyRateAlreadyExistException;

    void updateGlFcrEntry(GlFunctionalCurrencyRateDetails details, java.lang.String FC_NM, java.lang.Integer AD_CMPNY) throws GlFCNoFunctionalCurrencyFoundException, GlFCRFunctionalCurrencyRateAlreadyExistException, GlFCRFunctionalCurrencyRateAlreadyDeletedException;

    void deleteGlFcrEntry(java.lang.Integer FR_CODE, java.lang.Integer AD_CMPNY) throws GlFCRFunctionalCurrencyRateAlreadyDeletedException;

}