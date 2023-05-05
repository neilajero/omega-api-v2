/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlBudgetOrganizationControllerBean
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

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.entities.gen.LocalGenValueSet;
import com.ejb.dao.gen.LocalGenValueSetHome;
import com.ejb.entities.gl.LocalGlBudgetOrganization;
import com.ejb.dao.gl.LocalGlBudgetOrganizationHome;
import com.util.EJBContextClass;
import com.util.Debug;
import com.util.EJBHomeFactory;
import com.util.gl.GlBudgetOrganizationDetails;

@Stateless(name = "GlBudgetOrganizationControllerEJB")
public class GlBudgetOrganizationControllerBean extends EJBContextClass implements GlBudgetOrganizationController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGenValueSetHome genValueSetHome;
    @EJB
    private LocalGlBudgetOrganizationHome glBudgetOrganizationHome;

    public ArrayList getGenVsAll(Integer AD_CMPNY) {

        Debug.print("GlBudgetOrganizationControllerBean getGenVsAll");

        ArrayList list = new ArrayList();

        try {

            Collection genValueSets = genValueSetHome.findVsAll(AD_CMPNY);

            for (Object valueSet : genValueSets) {

                LocalGenValueSet genValueSet = (LocalGenValueSet) valueSet;

                list.add(genValueSet.getVsName());

            }

            return list;


        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    public ArrayList getGlBoAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlBudgetOrganizationControllerBean getGlBoAll");

        ArrayList list = new ArrayList();

        try {

            Collection glBudgetOrganizations = glBudgetOrganizationHome.findBoAll(AD_CMPNY);

            if (glBudgetOrganizations.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            }

            for (Object budgetOrganization : glBudgetOrganizations) {

                LocalGlBudgetOrganization glBudgetOrganization = (LocalGlBudgetOrganization) budgetOrganization;

                GlBudgetOrganizationDetails details = new GlBudgetOrganizationDetails();
                details.setBoCode(glBudgetOrganization.getBoCode());
                details.setBoName(glBudgetOrganization.getBoName());
                details.setBoDescription(glBudgetOrganization.getBoDescription());
                details.setBoSegmentOrder(glBudgetOrganization.getBoSegmentOrder());
                details.setBoPassword(glBudgetOrganization.getBoPassword());

                list.add(details);

            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    public void addGlBoEntry(GlBudgetOrganizationDetails details, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("GlBudgetOrganizationControllerBean addGlBoEntry");

        try {

            LocalGlBudgetOrganization glBudgetOrganization = null;

            try {

                glBudgetOrganization = glBudgetOrganizationHome.findByBoName(details.getBoName(), AD_CMPNY);

                throw new GlobalRecordAlreadyExistException();

            } catch (FinderException ex) {

            }

            // create new budget organization

            glBudgetOrganization = glBudgetOrganizationHome.create(details.getBoName(), details.getBoDescription(), details.getBoSegmentOrder(), details.getBoPassword(), AD_CMPNY);

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }


    public void updateGlBoEntry(GlBudgetOrganizationDetails details, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("GlBudgetOrganizationControllerBean updateGlBoEntry");


        try {

            try {

                LocalGlBudgetOrganization glExistingBudgetOrganization = glBudgetOrganizationHome.findByBoName(details.getBoName(), AD_CMPNY);

                if (glExistingBudgetOrganization != null && !glExistingBudgetOrganization.getBoCode().equals(details.getBoCode())) {

                    throw new GlobalRecordAlreadyExistException();

                }

            } catch (FinderException ex) {

            }

            LocalGlBudgetOrganization glBudgetOrganization = glBudgetOrganizationHome.findByPrimaryKey(details.getBoCode());

            // Find and Update Budget Organization

            glBudgetOrganization.setBoName(details.getBoName());
            glBudgetOrganization.setBoDescription(details.getBoDescription());
            glBudgetOrganization.setBoSegmentOrder(details.getBoSegmentOrder());
            glBudgetOrganization.setBoPassword(details.getBoPassword());

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }


    public void deleteGlBoEntry(Integer BO_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("GlBudgetOrganizationControllerBean deleteGlBoEntry");

        LocalGlBudgetOrganization glBudgetOrganization = null;

        try {

            glBudgetOrganization = glBudgetOrganizationHome.findByPrimaryKey(BO_CODE);

        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        }

        try {

            if (!glBudgetOrganization.getGlBudgetAmounts().isEmpty() || !glBudgetOrganization.getGlBudgetAccountAssignments().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

            em.remove(glBudgetOrganization);

        } catch (GlobalRecordAlreadyAssignedException ex) {

            throw ex;

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("GlBudgetOrganizationControllerBean ejbCreate");

    }

    // private methods

}