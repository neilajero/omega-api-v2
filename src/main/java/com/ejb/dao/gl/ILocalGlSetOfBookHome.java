package com.ejb.dao.gl;

import com.ejb.entities.gl.LocalGlSetOfBook;

import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;

public interface ILocalGlSetOfBookHome {
	
	LocalGlSetOfBook findByPrimaryKey(java.lang.Integer pk) throws FinderException;

	java.util.Collection findSobAll(java.lang.Integer SOB_AD_CMPNY) throws FinderException;

	LocalGlSetOfBook findByAcCode(java.lang.Integer GL_AC_CODE, java.lang.Integer SOB_AD_CMPNY)
			throws FinderException;

	LocalGlSetOfBook findByTcCode(java.lang.Integer GL_TC_CODE, java.lang.Integer SOB_AD_CMPNY)
			throws FinderException;

	LocalGlSetOfBook findLatestTransactionalCalendar(java.lang.Integer SOB_AD_CMPNY) throws FinderException;

	LocalGlSetOfBook findByDate(java.util.Date DT, java.lang.Integer SOB_AD_CMPNY) throws FinderException;

	LocalGlSetOfBook findByDate(java.util.Date DT, java.lang.Integer SOB_AD_CMPNY, String companyShortName) throws FinderException;

	java.util.Collection findBySobYearEndClosed(byte SOB_YR_END_CLSD, java.lang.Integer SOB_AD_CMPNY)
			throws FinderException;

	java.util.Collection findByAcvPeriodPrefixAndDate(java.lang.String ACV_PRD_PFX, java.util.Date DT,
                                                      java.lang.Integer SOB_AD_CMPNY) throws FinderException;

	java.util.Collection findSubsequentSobByAcYear(java.lang.Integer AC_YR, java.lang.Integer SOB_AD_CMPNY)
			throws FinderException;

	java.util.Collection findSubsequentSobByAcYear(java.lang.Integer AC_YR, java.lang.Integer SOB_AD_CMPNY, String companyShortName)
			throws FinderException;

	java.util.Collection findPrecedingSobByAcYear(java.lang.Integer AC_YR, java.lang.Integer SOB_AD_CMPNY)
			throws FinderException;

	LocalGlSetOfBook findByAcYear(java.lang.Integer AC_YR, java.lang.Integer SOB_AD_CMPNY)
			throws FinderException;

	java.util.Collection findBySobYearEndClosedAndGreaterThanAcYear(byte SOB_YR_END_CLSD,
                                                                    java.lang.Integer AC_YR, java.lang.Integer SOB_AD_CMPNY) throws FinderException;

	LocalGlSetOfBook create(java.lang.Integer SOB_CODE, byte SOB_YR_END_CLSD, Integer SOB_AD_CMPNY)
			throws CreateException;

	LocalGlSetOfBook create(byte SOB_YR_END_CLSD, Integer SOB_AD_CMPNY) throws CreateException;

}