package com.ejb.dao.gl;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlTransactionCalendar;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalGlTransactionCalendarHome implements ILocalGlTransactionCalendarHome {

	public static final String JNDI_NAME = "LocalGlTransactionCalendarHome!com.ejb.gl.LocalGlTransactionCalendarHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlTransactionCalendarHome() {
	}

	// FINDER METHODS

	public LocalGlTransactionCalendar findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlTransactionCalendar entity = (LocalGlTransactionCalendar) em
					.find(new LocalGlTransactionCalendar(), pk);
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

	public java.util.Collection findTcAll(java.lang.Integer TC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(tc) FROM GlTransactionCalendar tc WHERE tc.tcAdCompany = ?1");
			query.setParameter(1, TC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlTransactionCalendarHome.findTcAll(java.lang.Integer TC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findTcAllWithTcv(java.lang.Integer TC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT DISTINCT OBJECT(tc) FROM GlTransactionCalendar tc, IN(tc.glTransactionCalendarValues) tcv WHERE tcv.tcvCode > 0 AND tc.tcAdCompany = ?1");
			query.setParameter(1, TC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlTransactionCalendarHome.findTcAllWithTcv(java.lang.Integer TC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlTransactionCalendar findByTcName(java.lang.String TC_NM, java.lang.Integer TC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tc) FROM GlTransactionCalendar tc WHERE tc.tcName=?1 AND tc.tcAdCompany = ?2");
			query.setParameter(1, TC_NM);
			query.setParameter(2, TC_AD_CMPNY);
            return (LocalGlTransactionCalendar) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlTransactionCalendarHome.findByTcName(java.lang.String TC_NM, java.lang.Integer TC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlTransactionCalendarHome.findByTcName(java.lang.String TC_NM, java.lang.Integer TC_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlTransactionCalendar create(java.lang.Integer GL_TC_CODE, java.lang.String TC_NM,
                                             java.lang.String TC_DESC, java.lang.Integer TC_AD_CMPNY) throws CreateException {
		try {

			LocalGlTransactionCalendar entity = new LocalGlTransactionCalendar();

			Debug.print("GlTransactionCalendarBean create");

			entity.setTcCode(GL_TC_CODE);
			entity.setTcName(TC_NM);
			entity.setTcDescription(TC_DESC);
			entity.setTcAdCompany(TC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlTransactionCalendar create(java.lang.String TC_NM, java.lang.String TC_DESC,
                                             java.lang.Integer TC_AD_CMPNY) throws CreateException {
		try {

			LocalGlTransactionCalendar entity = new LocalGlTransactionCalendar();

			Debug.print("GlTransactionCalendarBean create");

			entity.setTcName(TC_NM);
			entity.setTcDescription(TC_DESC);
			entity.setTcAdCompany(TC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}