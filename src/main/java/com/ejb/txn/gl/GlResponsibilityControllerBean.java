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

import com.ejb.exception.gl.GlORGNoOrganizationFoundException;
import com.ejb.exception.gl.GlRESNoResponsibilityFoundException;
import com.ejb.exception.gl.GlRESResponsibilityAlreadyDeletedException;
import com.ejb.exception.gl.GlRESResponsibilityAlreadyExistException;
import com.ejb.entities.gl.LocalGlOrganization;
import com.ejb.dao.gl.LocalGlOrganizationHome;
import com.ejb.entities.gl.LocalGlResponsibility;
import com.ejb.dao.gl.LocalGlResponsibilityHome;
import com.util.EJBContextClass;
import com.util.Debug;
import com.util.EJBHomeFactory;
import com.util.gl.GlOrganizationDetails;
import com.util.gl.GlResponsibilityDetails;

@Stateless(name = "GlResponsibilityControllerEJB")
public class GlResponsibilityControllerBean extends EJBContextClass implements GlResponsibilityController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGlOrganizationHome glOrganizationHome;
    @EJB
    private LocalGlResponsibilityHome glResponsibilityHome;

    public ArrayList getGlResByOrgName(String ORG_NM, Integer AD_CMPNY) throws GlRESNoResponsibilityFoundException, GlORGNoOrganizationFoundException {

        Debug.print("GlResponsibilityController getGlResByOrgName");

        ArrayList resAllList = new ArrayList();
        Collection glResponsibilities = null;
        LocalGlOrganization glOrg = null;

        Debug.print("ORG_NM=" + ORG_NM);

        try {
            glOrg = glOrganizationHome.findByOrgName(ORG_NM, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlORGNoOrganizationFoundException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        glResponsibilities = glOrg.getGlResponsibilities();
        if (glResponsibilities.size() == 0) {
            Debug.print("Checkpoint C");
            throw new GlRESNoResponsibilityFoundException();
        }
        for (Object responsibility : glResponsibilities) {
            LocalGlResponsibility glResponsibility = (LocalGlResponsibility) responsibility;
            GlResponsibilityDetails details = new GlResponsibilityDetails(glResponsibility.getResCode(), glResponsibility.getResEnable());
            resAllList.add(details);
        }

        return resAllList;
    }


    public GlOrganizationDetails getGlOrgDescriptionByOrgName(String ORG_NM, Integer AD_CMPNY) throws GlORGNoOrganizationFoundException {

        Debug.print("GlResponsibilityController getOrgDescriptionByOrgName");

        LocalGlOrganization glOrg = null;

        try {
            glOrg = glOrganizationHome.findByOrgName(ORG_NM, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlORGNoOrganizationFoundException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        return new GlOrganizationDetails(glOrg.getOrgName(), glOrg.getOrgDescription(), glOrg.getOrgMasterCode());
    }


    public ArrayList getGlOrgAll(Integer AD_CMPNY) throws GlORGNoOrganizationFoundException {

        Debug.print("GlResponsibilityController getGlOrgAll");

        ArrayList orgList = new ArrayList();
        Collection glOrganizations = null;

        try {
            glOrganizations = glOrganizationHome.findOrgAll(AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glOrganizations.size() == 0) throw new GlORGNoOrganizationFoundException();

        for (Object glOrganization : glOrganizations) {
            LocalGlOrganization glOrg = (LocalGlOrganization) glOrganization;

            GlOrganizationDetails details = new GlOrganizationDetails(glOrg.getOrgName(), glOrg.getOrgDescription(), glOrg.getOrgMasterCode());

            orgList.add(details);
        }

        return orgList;
    }


    public void addGlResEntry(GlResponsibilityDetails details, String RES_ORG_NM, Integer AD_CMPNY) throws GlRESResponsibilityAlreadyExistException, GlORGNoOrganizationFoundException {

        Debug.print("GlResponsibilityControllerBean addGlResEntry");

        LocalGlOrganization glOrg = null;

        try {
            glOrg = glOrganizationHome.findByOrgName(RES_ORG_NM, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlORGNoOrganizationFoundException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {
            LocalGlResponsibility glRes = glResponsibilityHome.create(details.getResCode(), details.getResEnable(), AD_CMPNY);

            glOrg.addGlResponsibility(glRes);
        } catch (CreateException ex) {
            throw new GlRESResponsibilityAlreadyExistException();
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }


    public void updateGlResEntry(GlResponsibilityDetails details, String RES_ORG_NM, Integer AD_CMPNY) throws GlORGNoOrganizationFoundException, GlRESResponsibilityAlreadyDeletedException {

        Debug.print("GlResponsibilityControllerBean updateGlResEntry");

        LocalGlOrganization glOrg = null;
        LocalGlResponsibility glRes = null;


        try {
            glOrg = glOrganizationHome.findByOrgName(RES_ORG_NM, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlORGNoOrganizationFoundException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {
            glRes = glResponsibilityHome.findByPrimaryKey(details.getResCode());
        } catch (FinderException ex) {
            throw new GlRESResponsibilityAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {
            glRes.setResEnable(details.getResEnable());
            glOrg.addGlResponsibility(glRes);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

    }


    public void deleteGlResEntry(Integer RES_CODE, Integer AD_CMPNY) throws GlRESResponsibilityAlreadyDeletedException {

        Debug.print("GlResponsibilityControllerBean updateGlResEntry");

        LocalGlResponsibility glRes = null;


        try {
            glRes = glResponsibilityHome.findByPrimaryKey(RES_CODE);
        } catch (FinderException ex) {
            throw new GlRESResponsibilityAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {
            em.remove(glRes);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }
    // Session Methods


    public void ejbCreate() throws CreateException {

        Debug.print("GlResponsibilityControllerBean ejbCreate");

    }
}