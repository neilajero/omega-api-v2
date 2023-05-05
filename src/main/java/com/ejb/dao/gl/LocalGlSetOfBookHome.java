package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.util.Debug;

@Stateless
public class LocalGlSetOfBookHome implements ILocalGlSetOfBookHome {

	public static final String JNDI_NAME = "LocalGlSetOfBookHome!com.ejb.gl.LocalGlSetOfBookHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlSetOfBookHome() {
	}

	// FINDER METHODS

	public LocalGlSetOfBook findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlSetOfBook entity = (LocalGlSetOfBook) em
					.find(new LocalGlSetOfBook(), pk);
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

	public java.util.Collection findSobAll(java.lang.Integer SOB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(sob) FROM GlSetOfBook sob WHERE sob.sobAdCompany = ?1");
			query.setParameter(1, SOB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlSetOfBookHome.findSobAll(java.lang.Integer SOB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlSetOfBook findByAcCode(java.lang.Integer GL_AC_CODE, java.lang.Integer SOB_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sob) FROM GlSetOfBook sob WHERE sob.glAccountingCalendar.acCode=?1 AND sob.sobAdCompany = ?2");
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, SOB_AD_CMPNY);
            return (LocalGlSetOfBook) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlSetOfBookHome.findByAcCode(java.lang.Integer GL_AC_CODE, java.lang.Integer SOB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlSetOfBookHome.findByAcCode(java.lang.Integer GL_AC_CODE, java.lang.Integer SOB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlSetOfBook findByTcCode(java.lang.Integer GL_TC_CODE, java.lang.Integer SOB_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sob) FROM GlSetOfBook sob WHERE sob.glTransactionCalendar.tcCode=?1 AND sob.sobAdCompany = ?2");
			query.setParameter(1, GL_TC_CODE);
			query.setParameter(2, SOB_AD_CMPNY);
            return (LocalGlSetOfBook) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlSetOfBookHome.findByTcCode(java.lang.Integer GL_TC_CODE, java.lang.Integer SOB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlSetOfBookHome.findByTcCode(java.lang.Integer GL_TC_CODE, java.lang.Integer SOB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlSetOfBook findLatestTransactionalCalendar(java.lang.Integer SOB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(sob) FROM GlSetOfBook sob WHERE sob.sobAdCompany = ?1");
			query.setParameter(1, SOB_AD_CMPNY);
            return (LocalGlSetOfBook) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlSetOfBookHome.findLatestTransactionalCalendar(java.lang.Integer SOB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlSetOfBookHome.findLatestTransactionalCalendar(java.lang.Integer SOB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlSetOfBook findByDate(java.util.Date DT, java.lang.Integer SOB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sob) FROM GlSetOfBook sob, IN(sob.glAccountingCalendar.glAccountingCalendarValues) acv WHERE acv.acvDateFrom <= ?1 AND acv.acvDateTo >= ?1 AND sob.sobAdCompany = ?2");
			query.setParameter(1, DT);
			query.setParameter(2, SOB_AD_CMPNY);
            return (LocalGlSetOfBook) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlSetOfBookHome.findByDate(java.com.util.Date DT, java.lang.Integer SOB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlSetOfBookHome.findByDate(java.com.util.Date DT, java.lang.Integer SOB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlSetOfBook findByDate(java.util.Date DT, java.lang.Integer SOB_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(sob) FROM GlSetOfBook sob, IN(sob.glAccountingCalendar.glAccountingCalendarValues) acv "
							+ "WHERE acv.acvDateFrom <= ?1 AND acv.acvDateTo >= ?1 AND sob.sobAdCompany = ?2",
					companyShortName);
			query.setParameter(1, DT);
			query.setParameter(2, SOB_AD_CMPNY);
			return (LocalGlSetOfBook) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findBySobYearEndClosed(byte SOB_YR_END_CLSD, java.lang.Integer SOB_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sob) FROM GlSetOfBook sob WHERE sob.sobYearEndClosed = ?1 AND sob.sobAdCompany = ?2");
			query.setParameter(1, SOB_YR_END_CLSD);
			query.setParameter(2, SOB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlSetOfBookHome.findBySobYearEndClosed(byte SOB_YR_END_CLSD, java.lang.Integer SOB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAcvPeriodPrefixAndDate(java.lang.String ACV_PRD_PFX, java.util.Date DT,
			java.lang.Integer SOB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sob) FROM GlSetOfBook sob, IN(sob.glAccountingCalendar.glAccountingCalendarValues) acv WHERE acv.acvPeriodPrefix = ?1 AND acv.acvDateTo >= ?2 AND sob.sobAdCompany = ?3");
			query.setParameter(1, ACV_PRD_PFX);
			query.setParameter(2, DT);
			query.setParameter(3, SOB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlSetOfBookHome.findByAcvPeriodPrefixAndDate(java.lang.String ACV_PRD_PFX, java.com.util.Date DT, java.lang.Integer SOB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findSubsequentSobByAcYear(java.lang.Integer AC_YR, java.lang.Integer SOB_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sob) FROM GlSetOfBook sob WHERE sob.glAccountingCalendar.acYear > ?1 AND sob.sobAdCompany = ?2");
			query.setParameter(1, AC_YR);
			query.setParameter(2, SOB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlSetOfBookHome.findSubsequentSobByAcYear(java.lang.Integer AC_YR, java.lang.Integer SOB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findSubsequentSobByAcYear(java.lang.Integer AC_YR, java.lang.Integer SOB_AD_CMPNY, String companyShortName)
			throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(sob) FROM GlSetOfBook sob WHERE sob.glAccountingCalendar.acYear > ?1 AND sob.sobAdCompany = ?2",
					companyShortName);
			query.setParameter(1, AC_YR);
			query.setParameter(2, SOB_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findPrecedingSobByAcYear(java.lang.Integer AC_YR, java.lang.Integer SOB_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sob) FROM GlSetOfBook sob WHERE sob.glAccountingCalendar.acYear < ?1 AND sob.sobAdCompany = ?2");
			query.setParameter(1, AC_YR);
			query.setParameter(2, SOB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlSetOfBookHome.findPrecedingSobByAcYear(java.lang.Integer AC_YR, java.lang.Integer SOB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlSetOfBook findByAcYear(java.lang.Integer AC_YR, java.lang.Integer SOB_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sob) FROM GlSetOfBook sob WHERE sob.glAccountingCalendar.acYear = ?1 AND sob.sobAdCompany = ?2");
			query.setParameter(1, AC_YR);
			query.setParameter(2, SOB_AD_CMPNY);
            return (LocalGlSetOfBook) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlSetOfBookHome.findByAcYear(java.lang.Integer AC_YR, java.lang.Integer SOB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlSetOfBookHome.findByAcYear(java.lang.Integer AC_YR, java.lang.Integer SOB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBySobYearEndClosedAndGreaterThanAcYear(byte SOB_YR_END_CLSD,
			java.lang.Integer AC_YR, java.lang.Integer SOB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sob) FROM GlSetOfBook sob WHERE sob.sobYearEndClosed = ?1 AND sob.glAccountingCalendar.acYear > ?2 AND sob.sobAdCompany = ?3");
			query.setParameter(1, SOB_YR_END_CLSD);
			query.setParameter(2, AC_YR);
			query.setParameter(3, SOB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlSetOfBookHome.findBySobYearEndClosedAndGreaterThanAcYear(byte SOB_YR_END_CLSD, java.lang.Integer AC_YR, java.lang.Integer SOB_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlSetOfBook create(java.lang.Integer SOB_CODE, byte SOB_YR_END_CLSD, Integer SOB_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlSetOfBook entity = new LocalGlSetOfBook();

			Debug.print("GlSetOfBookBean create");

			entity.setSobCode(SOB_CODE);
			entity.setSobYearEndClosed(SOB_YR_END_CLSD);
			entity.setSobAdCompany(SOB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlSetOfBook create(byte SOB_YR_END_CLSD, Integer SOB_AD_CMPNY) throws CreateException {
		try {

			LocalGlSetOfBook entity = new LocalGlSetOfBook();

			Debug.print("GlSetOfBookBean create");

			entity.setSobYearEndClosed(SOB_YR_END_CLSD);
			entity.setSobAdCompany(SOB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}