package com.ejb.txn.gl;

import com.ejb.exception.gl.GlBGTFirstLastPeriodDifferentAcYearException;
import com.ejb.exception.gl.GlBGTFirstPeriodGreaterThanLastPeriodException;
import com.ejb.exception.gl.GlBGTStatusAlreadyDefinedException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.gl.GlBudgetDetails;

import jakarta.ejb.Local;


@Local
public interface GlBudgetDefinitionController {

    java.util.ArrayList getGlAcvAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlBgtAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void addGlBgtEntry(GlBudgetDetails details, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlBGTStatusAlreadyDefinedException, GlBGTFirstPeriodGreaterThanLastPeriodException, GlBGTFirstLastPeriodDifferentAcYearException;

    void updateGlBgtEntry(GlBudgetDetails details, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyAssignedException, GlBGTStatusAlreadyDefinedException, GlBGTFirstPeriodGreaterThanLastPeriodException, GlBGTFirstLastPeriodDifferentAcYearException;

    void deleteGlBgtEntry(java.lang.Integer BGT_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}