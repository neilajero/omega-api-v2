package com.ejb.txn.gl;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.gl.GlBudgetOrganizationDetails;

import jakarta.ejb.Local;


@Local
public interface GlBudgetOrganizationController {

    java.util.ArrayList getGenVsAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlBoAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void addGlBoEntry(GlBudgetOrganizationDetails details, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void updateGlBoEntry(GlBudgetOrganizationDetails details, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException;

    void deleteGlBoEntry(java.lang.Integer BO_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}