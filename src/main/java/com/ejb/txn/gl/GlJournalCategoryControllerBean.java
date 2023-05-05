/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlJournalCategoryControllerBean
 * @created
 * @author
 **/
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

import com.ejb.exception.gl.GlJCJournalCategoryAlreadyAssignedException;
import com.ejb.exception.gl.GlJCJournalCategoryAlreadyDeletedException;
import com.ejb.exception.gl.GlJCJournalCategoryAlreadyExistException;
import com.ejb.exception.gl.GlJCNoJournalCategoryFoundException;
import com.ejb.entities.gl.LocalGlJournalCategory;
import com.ejb.dao.gl.LocalGlJournalCategoryHome;
import com.util.EJBContextClass;
import com.util.Debug;
import com.util.EJBHomeFactory;
import com.util.gl.GlJournalCategoryDetails;

@Stateless(name = "GlJournalCategoryControllerEJB")
public class GlJournalCategoryControllerBean extends EJBContextClass implements GlJournalCategoryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;

    public ArrayList getGlJcAll(Integer AD_CMPNY) throws GlJCNoJournalCategoryFoundException {

        Debug.print("GlJournalCategoryControllerBean getGlJcAll");

        ArrayList jcAllList = new ArrayList();
        Collection glJournalCategories = null;


        try {
            glJournalCategories = glJournalCategoryHome.findJcAll(AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glJournalCategories.size() == 0) throw new GlJCNoJournalCategoryFoundException();

        for (Object journalCategory : glJournalCategories) {
            LocalGlJournalCategory glJournalCategory = (LocalGlJournalCategory) journalCategory;
            GlJournalCategoryDetails details = new GlJournalCategoryDetails(glJournalCategory.getJcCode(), glJournalCategory.getJcName(), glJournalCategory.getJcDescription(), glJournalCategory.getJcReversalMethod());
            jcAllList.add(details);
        }

        return jcAllList;
    }


    public void addGlJcEntry(GlJournalCategoryDetails details, Integer AD_CMPNY) throws GlJCJournalCategoryAlreadyExistException {

        Debug.print("GlJournalCategoryControllerBean addGlJcEntry");

        try {

            glJournalCategoryHome.findByJcName(details.getJcName(), AD_CMPNY);

            getSessionContext().setRollbackOnly();
            throw new GlJCJournalCategoryAlreadyExistException();

        } catch (FinderException ex) {

        }

        try {
            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.create(details.getJcName(), details.getJcDescription(), details.getJcReversalMethod(), AD_CMPNY);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }


    public void updateGlJcEntry(GlJournalCategoryDetails details, Integer AD_CMPNY) throws GlJCJournalCategoryAlreadyExistException, GlJCJournalCategoryAlreadyAssignedException, GlJCJournalCategoryAlreadyDeletedException {

        Debug.print("GlJournalCategoryControllerBean updateGlJcEntry");

        LocalGlJournalCategory glJournalCategory = null;

        try {
            glJournalCategory = glJournalCategoryHome.findByPrimaryKey(details.getJcCode());
        } catch (FinderException ex) {
            throw new GlJCJournalCategoryAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (hasRelation(glJournalCategory, AD_CMPNY)) throw new GlJCJournalCategoryAlreadyAssignedException();
        else {

            LocalGlJournalCategory glJournalCategory2 = null;

            try {
                glJournalCategory2 = glJournalCategoryHome.findByJcName(details.getJcName(), AD_CMPNY);
            } catch (FinderException ex) {
                glJournalCategory.setJcName(details.getJcName());
                glJournalCategory.setJcDescription(details.getJcDescription());
                glJournalCategory.setJcReversalMethod(details.getJcReversalMethod());
            } catch (Exception ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }

            if (glJournalCategory2 != null && !glJournalCategory.getJcCode().equals(glJournalCategory2.getJcCode())) {
                getSessionContext().setRollbackOnly();
                throw new GlJCJournalCategoryAlreadyExistException();
            } else if (glJournalCategory2 != null && glJournalCategory.getJcCode().equals(glJournalCategory2.getJcCode())) {
                glJournalCategory.setJcDescription(details.getJcDescription());
                glJournalCategory.setJcReversalMethod(details.getJcReversalMethod());
            }
        }
    }


    public void deleteGlJcEntry(Integer JC_CODE, Integer AD_CMPNY) throws GlJCJournalCategoryAlreadyDeletedException, GlJCJournalCategoryAlreadyAssignedException {

        Debug.print("GlJournalCategoryControllerBean deleteGlJcEntry");

        LocalGlJournalCategory glJournalCategory = null;

        try {
            glJournalCategory = glJournalCategoryHome.findByPrimaryKey(JC_CODE);
        } catch (FinderException ex) {
            throw new GlJCJournalCategoryAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (hasRelation(glJournalCategory, AD_CMPNY)) throw new GlJCJournalCategoryAlreadyAssignedException();
        else {
            try {
                em.remove(glJournalCategory);
            } catch (Exception ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }
        }
    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("GlJournalCategoryControllerBean ejbCreate");

    }

    // private methods

    private boolean hasRelation(LocalGlJournalCategory glJournalCategory, Integer AD_CMPNY) {

        Debug.print("GlJournalCategoryControllerBean hasRelation");

        return !glJournalCategory.getGlSuspenseAccounts().isEmpty() || !glJournalCategory.getGlRecurringJournals().isEmpty() || !glJournalCategory.getGlJournals().isEmpty();

    }


}