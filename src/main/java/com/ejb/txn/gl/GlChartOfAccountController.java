package com.ejb.txn.gl;

import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.exception.gl.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdResponsibilityDetails;
import com.util.gl.GlChartOfAccountDetails;
import com.util.mod.gl.GlModChartOfAccountDetails;

import jakarta.ejb.FinderException;
import jakarta.ejb.Local;

@Local
public interface GlChartOfAccountController {

    java.util.ArrayList getAdLvCitCategoryAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvSAWCategoryAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdLvIiTCategoryAll(java.lang.Integer AD_CMPNY);

    GlModChartOfAccountDetails getGlCoaDescAndAccountTypeByCoaAccountNumber(java.lang.String COA_ACCNT_NMBR, java.lang.Integer AD_CMPNY) throws GlCOAAccountNumberIsInvalidException, GlCOAAccountNumberHasParentValueException;

    GlModChartOfAccountDetails getGlCoaByCoaCode(java.lang.Integer COA_CODE, java.lang.Integer AD_CMPNY);

    void addGlCoaEntry(GlChartOfAccountDetails details, java.util.ArrayList branchList, java.lang.String GL_FUNCTIONAL_CURRENCY, java.lang.Integer AD_CMPNY) throws GlCOAAccountNumberIsInvalidException, GlCOAAccountNumberAlreadyExistException, GlCOAAccountNumberHasParentValueException, GlFCNoFunctionalCurrencyFoundException, GlFCFunctionalCurrencyAlreadyAssignedException;

    void updateGlCoaEntry(GlChartOfAccountDetails details, java.lang.String RS_NM, java.util.ArrayList branchList, java.lang.String GL_FUNCTIONAL_CURRENCY, java.lang.Integer AD_CMPNY) throws GlCOAAccountNumberAlreadyAssignedException, GlCOAAccountNumberHasParentValueException, GlCOAAccountNumberIsInvalidException, GlCOAAccountNumberAlreadyExistException, GlCOAAccountNumberAlreadyDeletedException, GlFCNoFunctionalCurrencyFoundException, GlFCFunctionalCurrencyAlreadyAssignedException;

    void deleteGlCoaEntry(java.lang.Integer COA_CODE, java.lang.Integer AD_CMPNY) throws GlCOAAccountNumberAlreadyAssignedException, GlCOAAccountNumberAlreadyDeletedException;

    java.util.ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBrCoaAll(java.lang.Integer COA_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdResponsibilityDetails getAdRsByRsCode(java.lang.Integer RS_CODE) throws GlobalNoRecordFoundException;

    java.util.ArrayList getGlFcAllWithDefault(java.lang.Integer AD_CMPNY);

    LocalGlChartOfAccount findByCoaAccountNumberAndBranchCode(java.lang.String COA_ACCNT_NMBR, java.lang.Integer COA_AD_BRNCH, java.lang.Integer COA_AD_CMPNY) throws FinderException;

}