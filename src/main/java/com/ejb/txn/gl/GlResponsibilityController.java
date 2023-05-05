package com.ejb.txn.gl;

import com.ejb.exception.gl.GlORGNoOrganizationFoundException;
import com.ejb.exception.gl.GlRESNoResponsibilityFoundException;
import com.ejb.exception.gl.GlRESResponsibilityAlreadyDeletedException;
import com.ejb.exception.gl.GlRESResponsibilityAlreadyExistException;
import com.util.gl.GlOrganizationDetails;
import com.util.gl.GlResponsibilityDetails;

import jakarta.ejb.Local;


@Local
public interface GlResponsibilityController
   
{
   java.util.ArrayList getGlResByOrgName(java.lang.String ORG_NM, java.lang.Integer AD_CMPNY)
      throws GlRESNoResponsibilityFoundException, GlORGNoOrganizationFoundException;

   GlOrganizationDetails getGlOrgDescriptionByOrgName(java.lang.String ORG_NM, java.lang.Integer AD_CMPNY)
      throws GlORGNoOrganizationFoundException;

   java.util.ArrayList getGlOrgAll(java.lang.Integer AD_CMPNY)
      throws GlORGNoOrganizationFoundException;

   void addGlResEntry(GlResponsibilityDetails details, java.lang.String RES_ORG_NM, java.lang.Integer AD_CMPNY)
      throws GlRESResponsibilityAlreadyExistException, GlORGNoOrganizationFoundException;

   void updateGlResEntry(GlResponsibilityDetails details, java.lang.String RES_ORG_NM, java.lang.Integer AD_CMPNY)
      throws GlORGNoOrganizationFoundException, GlRESResponsibilityAlreadyDeletedException;

   void deleteGlResEntry(java.lang.Integer RES_CODE, java.lang.Integer AD_CMPNY)
      throws GlRESResponsibilityAlreadyDeletedException;

}