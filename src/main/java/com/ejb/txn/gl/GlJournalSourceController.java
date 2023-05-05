package com.ejb.txn.gl;

import com.ejb.exception.gl.GlJSJournalSourceAlreadyAssignedException;
import com.ejb.exception.gl.GlJSJournalSourceAlreadyDeletedException;
import com.ejb.exception.gl.GlJSJournalSourceAlreadyExistException;
import com.ejb.exception.gl.GlJSNoJournalSourceFoundException;
import com.util.gl.GlJournalSourceDetails;

import jakarta.ejb.Local;

@Local
public interface GlJournalSourceController {

    java.util.ArrayList getGlJsAll(java.lang.Integer AD_CMPNY) throws GlJSNoJournalSourceFoundException;

    void addGlJsEntry(GlJournalSourceDetails details, java.lang.Integer AD_CMPNY) throws GlJSJournalSourceAlreadyExistException;

    void updateGlJsEntry(GlJournalSourceDetails details, java.lang.Integer AD_CMPNY) throws GlJSJournalSourceAlreadyExistException, GlJSJournalSourceAlreadyAssignedException, GlJSJournalSourceAlreadyDeletedException;

    void deleteGlJsEntry(java.lang.Integer JS_CODE, java.lang.Integer AD_CMPNY) throws GlJSJournalSourceAlreadyDeletedException, GlJSJournalSourceAlreadyAssignedException;

}