package com.ejb.txn.gl;

import com.ejb.exception.gl.*;
import com.util.gl.GlAccountRangeDetails;
import com.util.gl.GlOrganizationDetails;

import jakarta.ejb.Local;

@Local
public interface GlOrganizationAccountAssignmentController {

    java.util.ArrayList getGlOrgAll(java.lang.Integer AD_CMPNY) throws GlORGNoOrganizationFoundException;

    GlOrganizationDetails getGlOrgDescriptionByGlOrgName(java.lang.String ORG_NM, java.lang.Integer AD_CMPNY) throws GlORGNoOrganizationFoundException;

    java.util.ArrayList getGlArByGlOrgName(java.lang.String ORG_NM, java.lang.Integer AD_CMPNY) throws GlORGNoOrganizationFoundException, GlARNoAccountRangeFoundException;

    void addGlArEntry(GlAccountRangeDetails details, java.lang.String ORG_NM, java.lang.Integer RES_CODE, java.lang.Integer AD_CMPNY) throws GlORGNoOrganizationFoundException, GlARAccountRangeOverlappedException, GlARAccountNumberOfSegmentInvalidException, GlARAccountRangeNoAccountFoundException, GlARResponsibilityNotAllowedException, GlARAccountRangeInvalidException;

    void updateGlArEntry(GlAccountRangeDetails details, java.lang.String ORG_NM, java.lang.Integer RES_CODE, java.lang.Integer AD_CMPNY) throws GlORGNoOrganizationFoundException, GlARAccountRangeOverlappedException, GlARAccountNumberOfSegmentInvalidException, GlARAccountRangeNoAccountFoundException, GlARAccountRangeAlreadyDeletedException, GlARResponsibilityNotAllowedException, GlARAccountRangeInvalidException;

    void deleteGlArEntry(java.lang.Integer AR_CODE, java.lang.Integer RES_CODE, java.lang.Integer AD_CMPNY) throws GlARAccountRangeAlreadyDeletedException, GlARResponsibilityNotAllowedException;

}