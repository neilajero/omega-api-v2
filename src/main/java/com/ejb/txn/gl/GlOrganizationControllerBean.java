package com.ejb.txn.gl;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import javax.naming.NamingException;

import com.ejb.exception.gl.GlORGNoOrganizationCodeFoundException;
import com.ejb.exception.gl.GlORGNoOrganizationFoundException;
import com.ejb.exception.gl.GlORGOrganizationAlreadyAssignedException;
import com.ejb.exception.gl.GlORGOrganizationAlreadyDeletedException;
import com.ejb.exception.gl.GlORGOrganizationAlreadyExistException;
import com.ejb.entities.gl.LocalGlOrganization;
import com.ejb.dao.gl.LocalGlOrganizationHome;
import com.util.EJBContextClass;
import com.util.Debug;
import com.util.EJBHomeFactory;
import com.util.gl.GlOrganizationDetails;

@Stateless(name = "GlOrganizationControllerEJB")
public class GlOrganizationControllerBean extends EJBContextClass implements GlOrganizationController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGlOrganizationHome glOrganizationHome;

    public ArrayList getGlOrgAll(Integer AD_CMPNY) throws GlORGNoOrganizationFoundException {

        Debug.print("GlOrganizationControllerBean getGlOrgAll");

        ArrayList orgAllList = new ArrayList();
        Collection glOrganizations;

        try {
            glOrganizations = glOrganizationHome.findOrgAll(AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glOrganizations.size() == 0) throw new GlORGNoOrganizationFoundException();

        for (Object organization : glOrganizations) {
            LocalGlOrganization glOrganization = (LocalGlOrganization) organization;

            GlOrganizationDetails details = new GlOrganizationDetails(glOrganization.getOrgCode(), glOrganization.getOrgName(), glOrganization.getOrgDescription(), glOrganization.getOrgMasterCode());
            orgAllList.add(details);
        }

        return orgAllList;

    }


    public void addGlOrgEntry(GlOrganizationDetails details, Integer AD_CMPNY) throws GlORGNoOrganizationCodeFoundException, GlORGOrganizationAlreadyExistException {

        Debug.print("GlOrganizationControllerBean addGlOrgEntry");

        LocalGlOrganization glOrganization = null;

        if (details.getOrgMasterCode() != null) {
            try {
                glOrganization = glOrganizationHome.findByPrimaryKey(details.getOrgMasterCode());
            } catch (FinderException ex) {
                throw new GlORGNoOrganizationCodeFoundException();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
        }

        try {

            glOrganizationHome.findByOrgName(details.getOrgName(), AD_CMPNY);

            getSessionContext().setRollbackOnly();
            throw new GlORGOrganizationAlreadyExistException();

        } catch (FinderException ex) {


        }

        try {
            glOrganization = glOrganizationHome.create(details.getOrgName(), details.getOrgDescription(), details.getOrgMasterCode(), AD_CMPNY);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

    }


    public void updateGlOrgEntry(GlOrganizationDetails details, Integer AD_CMPNY) throws GlORGNoOrganizationCodeFoundException, GlORGOrganizationAlreadyExistException, GlORGOrganizationAlreadyAssignedException, GlORGOrganizationAlreadyDeletedException {

        Debug.print("GlOrganizationBean updateGlOrgEntry");

        LocalGlOrganization glOrganization = null;


        if (details.getOrgMasterCode() != null) {
            try {
                glOrganization = glOrganizationHome.findByPrimaryKey(details.getOrgMasterCode());
            } catch (FinderException ex) {
                throw new GlORGNoOrganizationCodeFoundException();
            } catch (EJBException ex) {
                throw new EJBException(ex.getMessage());
            }
        }

        try {
            glOrganization = glOrganizationHome.findByPrimaryKey(details.getOrgCode());
        } catch (FinderException ex) {
            throw new GlORGOrganizationAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (hasRelation(glOrganization, AD_CMPNY)) throw new GlORGOrganizationAlreadyAssignedException();
        else {

            LocalGlOrganization glOrganization2 = null;

            try {
                glOrganization2 = glOrganizationHome.findByOrgName(details.getOrgName(), AD_CMPNY);
            } catch (FinderException ex) {
                try {
                    glOrganization.setOrgName(details.getOrgName());
                    glOrganization.setOrgDescription(details.getOrgDescription());
                    glOrganization.setOrgMasterCode(details.getOrgMasterCode());
                } catch (Exception e) {
                    getSessionContext().setRollbackOnly();
                    throw new EJBException(e.getMessage());
                }
            } catch (Exception ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }

            if (glOrganization2 != null && !glOrganization.getOrgCode().equals(glOrganization2.getOrgCode())) {

                getSessionContext().setRollbackOnly();
                throw new GlORGOrganizationAlreadyExistException();
            } else if (glOrganization2 != null && glOrganization.getOrgCode().equals(glOrganization2.getOrgCode())) {

                try {
                    glOrganization.setOrgDescription(details.getOrgDescription());
                    glOrganization.setOrgMasterCode(details.getOrgMasterCode());
                } catch (Exception e) {
                    getSessionContext().setRollbackOnly();
                    throw new EJBException(e.getMessage());
                }
            }
        }
    }


    public void deleteGlOrgEntry(Integer ORG_CODE, Integer AD_CMPNY) throws GlORGOrganizationAlreadyAssignedException, GlORGOrganizationAlreadyDeletedException {

        Debug.print("GlOrganizationBean deleteGlOrgEntry");

        LocalGlOrganization glOrganization = null;

        try {
            glOrganization = glOrganizationHome.findByPrimaryKey(ORG_CODE);
        } catch (FinderException ex) {
            throw new GlORGOrganizationAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (hasRelation(glOrganization, AD_CMPNY)) throw new GlORGOrganizationAlreadyAssignedException();
        else {
            try {

                em.remove(glOrganization);
            } catch (RemoveException ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }
        }

    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("GlOrganizationControllerBean ejbCreate");

    }

    // private methods

    private boolean hasRelation(LocalGlOrganization glOrganization, Integer AD_CMPNY) {

        Debug.print("GlOrganizationControllerBean hasRelation");

        try {

            Collection glOrganizations = glOrganizationHome.findByMasterCode(glOrganization.getOrgCode(), AD_CMPNY);

            return !glOrganization.getGlResponsibilities().isEmpty() || !glOrganization.getGlAccountRanges().isEmpty() || !glOrganizations.isEmpty();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

    }

}