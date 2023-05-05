package com.ejb.dao.gl;

import com.ejb.entities.gl.LocalGlAccountingCalendar;

import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;

public interface ILocalGlAccountingCalendarHome {

	LocalGlAccountingCalendar findByPrimaryKey(java.lang.Integer pk) throws FinderException;

	java.util.Collection findAcAll(java.lang.Integer AC_AD_CMPNY) throws FinderException;

	java.util.Collection findAcAllWithAcv(java.lang.Integer AC_AD_CMPNY) throws FinderException;

	LocalGlAccountingCalendar findByAcName(java.lang.String AC_NM, java.lang.Integer AC_AD_CMPNY)
			throws FinderException;

	LocalGlAccountingCalendar findByAcYear(java.lang.Integer AC_YR, java.lang.Integer AC_AD_CMPNY)
			throws FinderException;

	LocalGlAccountingCalendar create(java.lang.Integer GL_AC_CODE, java.lang.String AC_NM,
                                     java.lang.String AC_DESC, java.lang.Integer AC_YR, java.lang.Integer AC_AD_CMPNY) throws CreateException;

	LocalGlAccountingCalendar create(java.lang.String AC_NM, java.lang.String AC_DESC,
                                     java.lang.Integer AC_YR, java.lang.Integer AC_AD_CMPNY) throws CreateException;
}