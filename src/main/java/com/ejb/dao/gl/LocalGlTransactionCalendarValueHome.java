package com.ejb.dao.gl;

import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlTransactionCalendarValue;
import com.util.Debug;

@Stateless
public class LocalGlTransactionCalendarValueHome implements ILocalGlTransactionCalendarValueHome {

	public static final String JNDI_NAME = "LocalGlTransactionCalendarValueHome!com.ejb.gl.LocalGlTransactionCalendarValueHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlTransactionCalendarValueHome() {
	}

	// FINDER METHODS

	public LocalGlTransactionCalendarValue findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlTransactionCalendarValue entity = (LocalGlTransactionCalendarValue) em
					.find(new LocalGlTransactionCalendarValue(), pk);
			if (entity == null) {
				throw new FinderException();
			}
			return entity;
		} catch (FinderException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalGlTransactionCalendarValue findByTcCodeAndTcvDate(java.lang.Integer GL_TC_CODE, java.util.Date TCV_DT,
			java.lang.Integer TCV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tcv) FROM GlTransactionCalendar tc, IN(tc.glTransactionCalendarValues) tcv WHERE tc.tcCode=?1 AND tcv.tcvDate=?2 AND tcv.tcvAdCompany=?3");
			query.setParameter(1, GL_TC_CODE);
			query.setParameter(2, TCV_DT);
			query.setParameter(3, TCV_AD_CMPNY);
            return (LocalGlTransactionCalendarValue) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlTransactionCalendarValueHome.findByTcCodeAndTcvDate(java.lang.Integer GL_TC_CODE, java.com.util.Date TCV_DT, java.lang.Integer TCV_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlTransactionCalendarValueHome.findByTcCodeAndTcvDate(java.lang.Integer GL_TC_CODE, java.com.util.Date TCV_DT, java.lang.Integer TCV_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlTransactionCalendarValue create(java.lang.Integer TCV_CODE, Date TCV_DT,
                                                  short TCV_DY_OF_WK, byte TCV_BSNSS_DY, Integer TCV_AD_CMPNY) throws CreateException {
		try {

			LocalGlTransactionCalendarValue entity = new LocalGlTransactionCalendarValue();

			Debug.print("GlTransactionCalendarValueBean create");

			entity.setTcvCode(TCV_CODE);
			entity.setTcvDate(TCV_DT);
			entity.setTcvDayOfWeek(TCV_DY_OF_WK);
			entity.setTcvBusinessDay(TCV_BSNSS_DY);
			entity.setTcvAdCompany(TCV_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlTransactionCalendarValue create(Date TCV_DT, short TCV_DY_OF_WK, byte TCV_BSNSS_DY,
                                                  Integer TCV_AD_CMPNY) throws CreateException {
		try {

			LocalGlTransactionCalendarValue entity = new LocalGlTransactionCalendarValue();

			Debug.print("GlTransactionCalendarValueBean create");

			entity.setTcvDate(TCV_DT);
			entity.setTcvDayOfWeek(TCV_DY_OF_WK);
			entity.setTcvBusinessDay(TCV_BSNSS_DY);
			entity.setTcvAdCompany(TCV_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}