package com.ejb.txn.gl;

import com.ejb.exception.gl.*;
import com.util.gl.GlSuspenseAccountDetails;

import jakarta.ejb.Local;

@Local
public interface GlSuspenseAccountController {

    java.util.ArrayList getGlJcAll(java.lang.Integer AD_CMPNY) throws GlJCNoJournalCategoryFoundException;

    java.util.ArrayList getGlJsAll(java.lang.Integer AD_CMPNY) throws GlJSNoJournalSourceFoundException;

    java.util.ArrayList getGlCoaAll(java.lang.Integer AD_CMPNY) throws GlCOANoChartOfAccountFoundException;

    java.util.ArrayList getGlSaAll(java.lang.Integer AD_CMPNY) throws GlSANoSuspenseAccountFoundException;

    void deleteGlSaEntry(java.lang.Integer SA_CODE, java.lang.Integer AD_CMPNY) throws GlSASuspenseAccountAlreadyDeletedException;

    void addGlSaEntry(GlSuspenseAccountDetails details, java.lang.String SA_JC_NM, java.lang.String SA_JS_NM, java.lang.String SA_COA_ACCNT_NMBR, java.lang.Integer AD_CMPNY) throws GlSASuspenseAccountAlreadyExistException, GlJSNoJournalSourceFoundException, GlJCNoJournalCategoryFoundException, GlCOANoChartOfAccountFoundException, GlSASourceCategoryCombinationAlreadyExistException;

    void updateGlSaEntry(GlSuspenseAccountDetails details, java.lang.String SA_JC_NM, java.lang.String SA_JS_NM, java.lang.String SA_COA_ACCNT_NMBR, java.lang.Integer AD_CMPNY) throws GlSASuspenseAccountAlreadyExistException, GlJSNoJournalSourceFoundException, GlJCNoJournalCategoryFoundException, GlCOANoChartOfAccountFoundException, GlSASourceCategoryCombinationAlreadyExistException, GlSASuspenseAccountAlreadyDeletedException;

}