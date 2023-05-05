package com.ejb.dao.gl;

import com.ejb.entities.gl.LocalGlTransactionCalendarValue;

import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;

public interface ILocalGlTransactionCalendarValueHome {
	LocalGlTransactionCalendarValue findByPrimaryKey(java.lang.Integer pk) throws FinderException;

	LocalGlTransactionCalendarValue findByTcCodeAndTcvDate(java.lang.Integer GL_TC_CODE, java.util.Date TCV_DT,
                                                           java.lang.Integer TCV_AD_CMPNY) throws FinderException;

	LocalGlTransactionCalendarValue create(java.lang.Integer TCV_CODE, Date TCV_DT,
                                           short TCV_DY_OF_WK, byte TCV_BSNSS_DY, Integer TCV_AD_CMPNY) throws CreateException;

	LocalGlTransactionCalendarValue create(Date TCV_DT, short TCV_DY_OF_WK, byte TCV_BSNSS_DY,
                                           Integer TCV_AD_CMPNY) throws CreateException;

}