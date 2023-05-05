package com.ejb.dao.gl;

import com.ejb.entities.gl.LocalGlTransactionCalendar;

import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;

public interface ILocalGlTransactionCalendarHome {
	
	LocalGlTransactionCalendar findByPrimaryKey(java.lang.Integer pk) throws FinderException;

	java.util.Collection findTcAll(java.lang.Integer TC_AD_CMPNY) throws FinderException;

	java.util.Collection findTcAllWithTcv(java.lang.Integer TC_AD_CMPNY) throws FinderException;

	LocalGlTransactionCalendar findByTcName(java.lang.String TC_NM, java.lang.Integer TC_AD_CMPNY)
			throws FinderException;

	LocalGlTransactionCalendar create(java.lang.Integer GL_TC_CODE, java.lang.String TC_NM,
                                      java.lang.String TC_DESC, java.lang.Integer TC_AD_CMPNY) throws CreateException;

	LocalGlTransactionCalendar create(java.lang.String TC_NM, java.lang.String TC_DESC,
                                      java.lang.Integer TC_AD_CMPNY) throws CreateException;

}