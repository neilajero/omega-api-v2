package com.ejb.txn.gl;

import com.ejb.exception.gl.GlBgtAAAccountFromInvalidException;
import com.ejb.exception.gl.GlBgtAAAccountOverlappedException;
import com.ejb.exception.gl.GlBgtAAAccountToInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.util.gl.GlBudgetAccountAssignmentDetails;
import com.util.gl.GlBudgetOrganizationDetails;

import jakarta.ejb.Local;

@Local
public interface GlBudgetAccountAssignmentController {

    java.util.ArrayList getGlBaaByBoCode(java.lang.Integer BO_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    GlBudgetOrganizationDetails getGlBoByBoCode(java.lang.Integer BO_CODE, java.lang.Integer AD_CMPNY);

    void addGlBaaEntry(GlBudgetAccountAssignmentDetails details, java.lang.Integer BO_CODE, java.lang.Integer AD_CMPNY) throws GlBgtAAAccountFromInvalidException, GlBgtAAAccountToInvalidException, GlBgtAAAccountOverlappedException;

    void updateGlBaaEntry(GlBudgetAccountAssignmentDetails details, java.lang.Integer BO_CODE, java.lang.Integer AD_CMPNY) throws GlBgtAAAccountFromInvalidException, GlBgtAAAccountToInvalidException, GlBgtAAAccountOverlappedException;

    void deleteGlBaaEntry(java.lang.Integer BAA_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

}