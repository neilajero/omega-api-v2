package com.ejb.txn.gl;

import com.ejb.exception.gl.GlCOAAccountNumberAlreadyAssignedException;
import com.ejb.exception.gl.GlFCFunctionalCurrencyAlreadyAssignedException;
import com.ejb.exception.gl.GlFCNoFunctionalCurrencyFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.util.gl.GlChartOfAccountDetails;

import jakarta.ejb.Local;

@Local
public interface GlCoaGeneratorController {

    java.util.ArrayList getGenVsAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGenVsvAllByVsName(java.lang.String VS_NM, java.lang.Integer AD_CMPNY);

    char getGenFlSgmntSeparator(java.lang.Integer AD_CMPNY);

    void generateGlCoa(GlChartOfAccountDetails details, java.lang.Integer RS_CODE, java.util.ArrayList branchList, java.lang.String GL_FUNCTIONAL_CURRENCY, java.lang.Integer AD_CMPNY) throws GlCOAAccountNumberAlreadyAssignedException, GlFCNoFunctionalCurrencyFoundException, GlFCFunctionalCurrencyAlreadyAssignedException;

    void deleteGlCoa(java.lang.String COA_ACCT_NMBR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyAssignedException;

    java.util.ArrayList getGenNaturalAccountAll(java.lang.String ACCT_FRM, java.lang.String ACCT_TO, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    short getGenFlNumberOfSegment(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlFcAllWithDefault(java.lang.Integer AD_CMPNY);

}