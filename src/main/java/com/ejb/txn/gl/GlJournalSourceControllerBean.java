package com.ejb.txn.gl;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import javax.naming.NamingException;

import com.ejb.exception.gl.GlJSJournalSourceAlreadyAssignedException;
import com.ejb.exception.gl.GlJSJournalSourceAlreadyDeletedException;
import com.ejb.exception.gl.GlJSJournalSourceAlreadyExistException;
import com.ejb.exception.gl.GlJSNoJournalSourceFoundException;
import com.ejb.entities.gl.LocalGlJournalSource;
import com.ejb.dao.gl.LocalGlJournalSourceHome;
import com.util.EJBContextClass;
import com.util.Debug;
import com.util.EJBHomeFactory;
import com.util.gl.GlJournalSourceDetails;

@Stateless(name = "GlJournalSourceControllerEJB")
public class GlJournalSourceControllerBean extends EJBContextClass implements GlJournalSourceController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;

    public ArrayList getGlJsAll(Integer AD_CMPNY) throws GlJSNoJournalSourceFoundException {

        Debug.print("GlJournalSourceControllerBean getGlJsAll");

        ArrayList jsAllList = new ArrayList();
        Collection glJournalCategories = null;

        try {
            glJournalCategories = glJournalSourceHome.findJsAll(AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glJournalCategories.size() == 0) throw new GlJSNoJournalSourceFoundException();

        for (Object glJournalCategory : glJournalCategories) {
            LocalGlJournalSource glJournalSource = (LocalGlJournalSource) glJournalCategory;
            GlJournalSourceDetails details = new GlJournalSourceDetails(glJournalSource.getJsCode(), glJournalSource.getJsName(), glJournalSource.getJsDescription(), glJournalSource.getJsFreezeJournal(), glJournalSource.getJsJournalApproval(), glJournalSource.getJsEffectiveDateRule());
            jsAllList.add(details);
        }

        return jsAllList;
    }


    public void addGlJsEntry(GlJournalSourceDetails details, Integer AD_CMPNY) throws GlJSJournalSourceAlreadyExistException {

        Debug.print("GlJournalSourceControllerBean addGlJsEntry");

        try {

            glJournalSourceHome.findByJsName(details.getJsName(), AD_CMPNY);

            getSessionContext().setRollbackOnly();
            throw new GlJSJournalSourceAlreadyExistException();

        } catch (FinderException ex) {


        }

        try {
            LocalGlJournalSource glJournalSource = glJournalSourceHome.create(details.getJsName(), details.getJsDescription(), details.getJsFreezeJournal(), details.getJsJournalApproval(), details.getJsEffectiveDateRule(), AD_CMPNY);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }


    public void updateGlJsEntry(GlJournalSourceDetails details, Integer AD_CMPNY) throws GlJSJournalSourceAlreadyExistException, GlJSJournalSourceAlreadyAssignedException, GlJSJournalSourceAlreadyDeletedException {

        Debug.print("GlJournalSourceControllerBean updateGlJsEntry");

        LocalGlJournalSource glJournalSource = null;

        try {
            glJournalSource = glJournalSourceHome.findByPrimaryKey(details.getJsCode());
        } catch (FinderException ex) {
            throw new GlJSJournalSourceAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (hasRelation(glJournalSource, AD_CMPNY) && !glJournalSource.getJsName().equals(details.getJsName())) {
            throw new GlJSJournalSourceAlreadyAssignedException();
        } else {

            LocalGlJournalSource glJournalSource2 = null;

            try {
                glJournalSource2 = glJournalSourceHome.findByJsName(details.getJsName(), AD_CMPNY);
            } catch (FinderException ex) {
                glJournalSource.setJsName(details.getJsName());
                glJournalSource.setJsDescription(details.getJsDescription());
                glJournalSource.setJsFreezeJournal(details.getJsFreezeJournal());
                glJournalSource.setJsJournalApproval(details.getJsJournalApproval());
                glJournalSource.setJsEffectiveDateRule(details.getJsEffectiveDateRule());

            } catch (Exception ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }

            if (glJournalSource2 != null && !glJournalSource.getJsCode().equals(glJournalSource2.getJsCode())) {
                getSessionContext().setRollbackOnly();
                throw new GlJSJournalSourceAlreadyExistException();
            } else if (glJournalSource2 != null && glJournalSource.getJsCode().equals(glJournalSource2.getJsCode())) {
                glJournalSource.setJsDescription(details.getJsDescription());
                glJournalSource.setJsFreezeJournal(details.getJsFreezeJournal());
                glJournalSource.setJsJournalApproval(details.getJsJournalApproval());
                glJournalSource.setJsEffectiveDateRule(details.getJsEffectiveDateRule());
            }
        }
    }


    public void deleteGlJsEntry(Integer JS_CODE, Integer AD_CMPNY) throws GlJSJournalSourceAlreadyDeletedException, GlJSJournalSourceAlreadyAssignedException {

        Debug.print("GlJournalSourceControllerBean deleteGlJsEntry");

        LocalGlJournalSource glJournalSource = null;

        try {
            glJournalSource = glJournalSourceHome.findByPrimaryKey(JS_CODE);
        } catch (FinderException ex) {
            throw new GlJSJournalSourceAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (hasRelation(glJournalSource, AD_CMPNY)){
            throw new GlJSJournalSourceAlreadyAssignedException();
        } else {
            try {

                em.remove(glJournalSource);
            } catch (Exception ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }
        }
    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {
        Debug.print("GlJournalSourceControllerBean ejbCreate");

    }

    // private methods

    private boolean hasRelation(LocalGlJournalSource glJournalSource, Integer AD_CMPNY) {

        Debug.print("GlJournalSourceControllerBean hasRelation");

        return !glJournalSource.getGlSuspenseAccounts().isEmpty() || !glJournalSource.getGlJournals().isEmpty();

    }

}