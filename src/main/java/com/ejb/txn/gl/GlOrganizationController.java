package com.ejb.txn.gl;

import com.ejb.exception.gl.*;
import com.util.gl.GlOrganizationDetails;

import jakarta.ejb.Local;

@Local
public interface GlOrganizationController {
    java.util.ArrayList getGlOrgAll(java.lang.Integer AD_CMPNY) throws GlORGNoOrganizationFoundException;

    void addGlOrgEntry(GlOrganizationDetails details, java.lang.Integer AD_CMPNY) throws GlORGNoOrganizationCodeFoundException, GlORGOrganizationAlreadyExistException;

    void updateGlOrgEntry(GlOrganizationDetails details, java.lang.Integer AD_CMPNY) throws GlORGNoOrganizationCodeFoundException, GlORGOrganizationAlreadyExistException, GlORGOrganizationAlreadyAssignedException, GlORGOrganizationAlreadyDeletedException;

    void deleteGlOrgEntry(java.lang.Integer ORG_CODE, java.lang.Integer AD_CMPNY) throws GlORGOrganizationAlreadyAssignedException, GlORGOrganizationAlreadyDeletedException;

}