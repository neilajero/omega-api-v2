package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlAccountingCalendar;
import com.util.Debug;

@Stateless
public class LocalGlAccountingCalendarHome implements ILocalGlAccountingCalendarHome {

	public static final String JNDI_NAME = "LocalGlAccountingCalendarHome!com.ejb.gl.LocalGlAccountingCalendarHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlAccountingCalendarHome() {
	}

	// FINDER METHODS

	public LocalGlAccountingCalendar findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlAccountingCalendar entity = (LocalGlAccountingCalendar) em
					.find(new LocalGlAccountingCalendar(), pk);
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

	public java.util.Collection findAcAll(java.lang.Integer AC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(ac) FROM GlAccountingCalendar ac WHERE ac.acAdCompany = ?1");
			query.setParameter(1, AC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarHome.findAcAll(java.lang.Integer AC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAcAllWithAcv(java.lang.Integer AC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT DISTINCT OBJECT(ac) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv WHERE acv.acvCode > 0 AND ac.acAdCompany = ?1");
			query.setParameter(1, AC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarHome.findAcAllWithAcv(java.lang.Integer AC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlAccountingCalendar findByAcName(java.lang.String AC_NM, java.lang.Integer AC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ac) FROM GlAccountingCalendar ac WHERE ac.acName = ?1 AND ac.acAdCompany = ?2");
			query.setParameter(1, AC_NM);
			query.setParameter(2, AC_AD_CMPNY);
            return (LocalGlAccountingCalendar) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlAccountingCalendarHome.findByAcName(java.lang.String AC_NM, java.lang.Integer AC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarHome.findByAcName(java.lang.String AC_NM, java.lang.Integer AC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlAccountingCalendar findByAcYear(java.lang.Integer AC_YR, java.lang.Integer AC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ac) FROM GlAccountingCalendar ac WHERE ac.acYear = ?1 AND ac.acAdCompany = ?2");
			query.setParameter(1, AC_YR);
			query.setParameter(2, AC_AD_CMPNY);
            return (LocalGlAccountingCalendar) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlAccountingCalendarHome.findByAcYear(java.lang.Integer AC_YR, java.lang.Integer AC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarHome.findByAcYear(java.lang.Integer AC_YR, java.lang.Integer AC_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlAccountingCalendar create(java.lang.Integer GL_AC_CODE, java.lang.String AC_NM,
                                            java.lang.String AC_DESC, java.lang.Integer AC_YR, java.lang.Integer AC_AD_CMPNY) throws CreateException {
		try {

			LocalGlAccountingCalendar entity = new LocalGlAccountingCalendar();

			Debug.print("GlAccountingCalendarBean create");

			entity.setAcCode(GL_AC_CODE);
			entity.setAcName(AC_NM);
			entity.setAcDescription(AC_DESC);
			entity.setAcYear(AC_YR);
			entity.setAcAdCompany(AC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlAccountingCalendar create(java.lang.String AC_NM, java.lang.String AC_DESC,
                                            java.lang.Integer AC_YR, java.lang.Integer AC_AD_CMPNY) throws CreateException {
		try {

			LocalGlAccountingCalendar entity = new LocalGlAccountingCalendar();

			Debug.print("GlAccountingCalendarBean create");

			entity.setAcName(AC_NM);
			entity.setAcDescription(AC_DESC);
			entity.setAcYear(AC_YR);
			entity.setAcAdCompany(AC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}