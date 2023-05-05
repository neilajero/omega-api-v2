package com.ejb.txn.gl;

import com.ejb.exception.gl.GlJCJournalCategoryAlreadyAssignedException;
import com.ejb.exception.gl.GlJCJournalCategoryAlreadyDeletedException;
import com.ejb.exception.gl.GlJCJournalCategoryAlreadyExistException;
import com.ejb.exception.gl.GlJCNoJournalCategoryFoundException;
import com.util.gl.GlJournalCategoryDetails;

import jakarta.ejb.Local;

@Local
public interface GlJournalCategoryController {

    java.util.ArrayList getGlJcAll(java.lang.Integer AD_CMPNY) throws GlJCNoJournalCategoryFoundException;

    void addGlJcEntry(GlJournalCategoryDetails details, java.lang.Integer AD_CMPNY) throws GlJCJournalCategoryAlreadyExistException;

    void updateGlJcEntry(GlJournalCategoryDetails details, java.lang.Integer AD_CMPNY) throws GlJCJournalCategoryAlreadyExistException, GlJCJournalCategoryAlreadyAssignedException, GlJCJournalCategoryAlreadyDeletedException;

    void deleteGlJcEntry(java.lang.Integer JC_CODE, java.lang.Integer AD_CMPNY) throws GlJCJournalCategoryAlreadyDeletedException, GlJCJournalCategoryAlreadyAssignedException;

}