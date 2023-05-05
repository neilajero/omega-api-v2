package com.ejb.dao.gl;

import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.util.Debug;

@Stateless
public class LocalGlAccountingCalendarValueHome implements ILocalGlAccountingCalendarValueHome {

	public static final String JNDI_NAME = "LocalGlAccountingCalendarValueHome!com.ejb.gl.LocalGlAccountingCalendarValueHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlAccountingCalendarValueHome() {
	}

	// FINDER METHODS

	public LocalGlAccountingCalendarValue findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlAccountingCalendarValue entity = (LocalGlAccountingCalendarValue) em
					.find(new LocalGlAccountingCalendarValue(), pk);
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

	public LocalGlAccountingCalendarValue findByAcCodeAndAcvPeriodPrefix(java.lang.Integer GL_AC_CODE,
			java.lang.String ACV_PRD_PRFX, java.lang.Integer ACV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(acv) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv WHERE ac.acCode=?1 AND acv.acvPeriodPrefix=?2 AND acv.acvAdCompany = ?3");
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, ACV_PRD_PRFX);
			query.setParameter(3, ACV_AD_CMPNY);
            return (LocalGlAccountingCalendarValue) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlAccountingCalendarValueHome.findByAcCodeAndAcvPeriodPrefix(java.lang.Integer GL_AC_CODE, java.lang.String ACV_PRD_PRFX, java.lang.Integer ACV_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarValueHome.findByAcCodeAndAcvPeriodPrefix(java.lang.Integer GL_AC_CODE, java.lang.String ACV_PRD_PRFX, java.lang.Integer ACV_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlAccountingCalendarValue findByAcCodeAndAcvPeriodNumber(java.lang.Integer GL_AC_CODE, short ACV_PRD_NMBR,
			java.lang.Integer ACV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(acv) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv WHERE ac.acCode=?1 AND acv.acvPeriodNumber=?2 AND acv.acvAdCompany = ?3");
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, ACV_PRD_NMBR);
			query.setParameter(3, ACV_AD_CMPNY);
            return (LocalGlAccountingCalendarValue) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(java.lang.Integer GL_AC_CODE, short ACV_PRD_NMBR, java.lang.Integer ACV_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(java.lang.Integer GL_AC_CODE, short ACV_PRD_NMBR, java.lang.Integer ACV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAcCodeAndPtPeriodPerYear(java.lang.Integer GL_AC_CODE, short PT_PRD_PER_YR,
			java.lang.Integer ACV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(acv) FROM GlPeriodType pt, IN(pt.glAccountingCalendars) ac, IN(ac.glAccountingCalendarValues) acv WHERE ac.acCode=?1 AND acv.acvPeriodNumber  > 0 AND acv.acvPeriodNumber <= ?2 AND acv.acvAdCompany = ?3");
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, PT_PRD_PER_YR);
			query.setParameter(3, ACV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarValueHome.findByAcCodeAndPtPeriodPerYear(java.lang.Integer GL_AC_CODE, short PT_PRD_PER_YR, java.lang.Integer ACV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAcCodeAndAcvStatus(java.lang.Integer GL_AC_CODE, char ACV_STATUS,
			java.lang.Integer ACV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(acv) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv WHERE ac.acCode=?1 AND acv.acvStatus=?2 AND acv.acvAdCompany = ?3 ORDER BY acv.acvPeriodNumber");
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, ACV_STATUS);
			query.setParameter(3, ACV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarValueHome.findByAcCodeAndAcvStatus(java.lang.Integer GL_AC_CODE, char ACV_STATUS, java.lang.Integer ACV_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlAccountingCalendarValue findByAcCodeAndDate(java.lang.Integer GL_AC_CODE, java.util.Date DT,
			java.lang.Integer ACV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(acv) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv WHERE ac.acCode=?1 AND acv.acvDateFrom <= ?2 AND acv.acvDateTo >= ?2 AND acv.acvAdCompany = ?3");
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, DT);
			query.setParameter(3, ACV_AD_CMPNY);
            return (LocalGlAccountingCalendarValue) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlAccountingCalendarValueHome.findByAcCodeAndDate(java.lang.Integer GL_AC_CODE, java.com.util.Date DT, java.lang.Integer ACV_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarValueHome.findByAcCodeAndDate(java.lang.Integer GL_AC_CODE, java.com.util.Date DT, java.lang.Integer ACV_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlAccountingCalendarValue findByAcCodeAndDate(java.lang.Integer GL_AC_CODE, java.util.Date DT,
															  java.lang.Integer ACV_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(acv) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv "
							+ "WHERE ac.acCode=?1 AND acv.acvDateFrom <= ?2 AND acv.acvDateTo >= ?2 AND acv.acvAdCompany = ?3",
					companyShortName);
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, DT);
			query.setParameter(3, ACV_AD_CMPNY);
			return (LocalGlAccountingCalendarValue) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}



	public java.util.Collection findAcvByAcCodeAndAcvPeriodNumberAndAcvStatus(java.lang.Integer GL_AC_CODE,
			short ACV_PRD_NMBR, char ACV_STATUS, java.lang.Integer ACV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(acv) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv WHERE ac.acCode=?1 AND acv.acvPeriodNumber < ?2 AND acv.acvStatus=?3 AND acv.acvAdCompany = ?4");
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, ACV_PRD_NMBR);
			query.setParameter(3, ACV_STATUS);
			query.setParameter(4, ACV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarValueHome.findAcvByAcCodeAndAcvPeriodNumberAndAcvStatus(java.lang.Integer GL_AC_CODE, short ACV_PRD_NMBR, char ACV_STATUS, java.lang.Integer ACV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findOpenAndFutureEntryAcvByAcCode(java.lang.Integer GL_AC_CODE, char ACV_STATUS1,
			char ACV_STATUS2, java.lang.Integer ACV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(acv) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv WHERE ac.acCode=?1 AND (acv.acvStatus=?2 OR acv.acvStatus=?3) AND acv.acvAdCompany = ?4");
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, ACV_STATUS1);
			query.setParameter(3, ACV_STATUS2);
			query.setParameter(4, ACV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarValueHome.findOpenAndFutureEntryAcvByAcCode(java.lang.Integer GL_AC_CODE, char ACV_STATUS1, char ACV_STATUS2, java.lang.Integer ACV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findCurrentAndFutureAcvByAcCodeAndCurrentDate(java.lang.Integer GL_AC_CODE,
			java.util.Date ACV_DT, java.lang.Integer ACV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(acv) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv WHERE ac.acCode=?1 AND acv.acvDateTo > ?2 AND acv.acvAdCompany = ?3");
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, ACV_DT);
			query.setParameter(3, ACV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarValueHome.findCurrentAndFutureAcvByAcCodeAndCurrentDate(java.lang.Integer GL_AC_CODE, java.com.util.Date ACV_DT, java.lang.Integer ACV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAcCodeAndAcvQuarterNumber(java.lang.Integer GL_AC_CODE, short ACV_QRTR_NMBR,
			java.lang.Integer ACV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(acv) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv WHERE ac.acCode=?1 AND acv.acvQuarter=?2 AND acv.acvAdCompany = ?3");
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, ACV_QRTR_NMBR);
			query.setParameter(3, ACV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarValueHome.findByAcCodeAndAcvQuarterNumber(java.lang.Integer GL_AC_CODE, short ACV_QRTR_NMBR, java.lang.Integer ACV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findClosedAndPermanentlyClosedAcvByAcCode(java.lang.Integer GL_AC_CODE, char ACV_STATUS1,
			char ACV_STATUS2, java.lang.Integer ACV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(acv) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv WHERE ac.acCode=?1 AND (acv.acvStatus=?2 OR acv.acvStatus=?3) AND acv.acvAdCompany = ?4 ORDER BY acv.acvPeriodNumber");
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, ACV_STATUS1);
			query.setParameter(3, ACV_STATUS2);
			query.setParameter(4, ACV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarValueHome.findClosedAndPermanentlyClosedAcvByAcCode(java.lang.Integer GL_AC_CODE, char ACV_STATUS1, char ACV_STATUS2, java.lang.Integer ACV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findClosedAndPermanentlyClosedAcvByAcCodeAndBackwardAcvDate(java.lang.Integer GL_AC_CODE,
			java.util.Date ACV_DT, char ACV_STATUS1, char ACV_STATUS2, java.lang.Integer ACV_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(acv) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv WHERE ac.acCode=?1 AND acv.acvDateTo<?2 AND (acv.acvStatus=?3 OR acv.acvStatus=?4) AND acv.acvAdCompany = ?5");
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, ACV_DT);
			query.setParameter(3, ACV_STATUS1);
			query.setParameter(4, ACV_STATUS2);
			query.setParameter(5, ACV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarValueHome.findClosedAndPermanentlyClosedAcvByAcCodeAndBackwardAcvDate(java.lang.Integer GL_AC_CODE, java.com.util.Date ACV_DT, char ACV_STATUS1, char ACV_STATUS2, java.lang.Integer ACV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findClosedAndPermanentlyClosedAcvByAcCodeAndForwardAcvDate(java.lang.Integer GL_AC_CODE,
			java.util.Date ACV_DT, char ACV_STATUS1, char ACV_STATUS2, java.lang.Integer ACV_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(acv) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv WHERE ac.acCode=?1 AND acv.acvDateFrom>?2 AND (acv.acvStatus=?3 OR acv.acvStatus=?4) AND acv.acvAdCompany = ?5");
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, ACV_DT);
			query.setParameter(3, ACV_STATUS1);
			query.setParameter(4, ACV_STATUS2);
			query.setParameter(5, ACV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarValueHome.findClosedAndPermanentlyClosedAcvByAcCodeAndForwardAcvDate(java.lang.Integer GL_AC_CODE, java.com.util.Date ACV_DT, char ACV_STATUS1, char ACV_STATUS2, java.lang.Integer ACV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findSubsequentAcvByAcCodeAndAcvPeriodNumber(java.lang.Integer GL_AC_CODE,
			short ACV_PRD_NMBR, java.lang.Integer ACV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(acv) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv WHERE ac.acCode=?1 AND acv.acvPeriodNumber>?2 AND acv.acvAdCompany = ?3");
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, ACV_PRD_NMBR);
			query.setParameter(3, ACV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(java.lang.Integer GL_AC_CODE, short ACV_PRD_NMBR, java.lang.Integer ACV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findSubsequentAcvByAcCodeAndAcvPeriodNumber(java.lang.Integer GL_AC_CODE,
																			short ACV_PRD_NMBR, java.lang.Integer ACV_AD_CMPNY,
																			String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(acv) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv "
							+ "WHERE ac.acCode=?1 AND acv.acvPeriodNumber>?2 AND acv.acvAdCompany = ?3",
					companyShortName);
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, ACV_PRD_NMBR);
			query.setParameter(3, ACV_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findReportableAcvByAcCodeAndAcvStatus(java.lang.Integer GL_AC_CODE, char ACV_STATUS1,
			char ACV_STATUS2, char ACV_STATUS3, java.lang.Integer ACV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(acv) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv WHERE ac.acCode=?1 AND (acv.acvStatus=?2 OR acv.acvStatus=?3 OR acv.acvStatus=?4) AND acv.acvAdCompany = ?5 ORDER BY acv.acvPeriodNumber");
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, ACV_STATUS1);
			query.setParameter(3, ACV_STATUS2);
			query.setParameter(4, ACV_STATUS3);
			query.setParameter(5, ACV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarValueHome.findReportableAcvByAcCodeAndAcvStatus(java.lang.Integer GL_AC_CODE, char ACV_STATUS1, char ACV_STATUS2, char ACV_STATUS3, java.lang.Integer ACV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAcCode(java.lang.Integer GL_AC_CODE, java.lang.Integer ACV_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(acv) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv WHERE ac.acCode=?1 AND acv.acvAdCompany = ?2");
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, ACV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlAccountingCalendarValueHome.findByAcCode(java.lang.Integer GL_AC_CODE, java.lang.Integer ACV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAcCode(java.lang.Integer GL_AC_CODE, java.lang.Integer ACV_AD_CMPNY, String companyShortName)
			throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(acv) FROM GlAccountingCalendar ac, IN(ac.glAccountingCalendarValues) acv"
							+ "WHERE ac.acCode=?1 AND acv.acvAdCompany = ?2",
					companyShortName);
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, ACV_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalGlAccountingCalendarValue create(java.lang.Integer ACV_CODE, java.lang.String ACV_PRD_PRFX,
                                                 short ACV_QRTR, short ACV_PRD_NMBR, Date ACV_DT_FRM, Date ACV_DT_TO, char ACV_STATUS, Date ACV_DT_OPND,
                                                 Date ACV_DT_CLSD, Date ACV_DT_PRMNNTLY_CLSD, Date ACV_DT_FTR_ENTRD, Integer ACV_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlAccountingCalendarValue entity = new LocalGlAccountingCalendarValue();

			Debug.print("GlAccountingCalendarValueBean create");
			entity.setAcvCode(ACV_CODE);
			entity.setAcvPeriodPrefix(ACV_PRD_PRFX);
			entity.setAcvQuarter(ACV_QRTR);
			entity.setAcvPeriodNumber(ACV_PRD_NMBR);
			entity.setAcvDateFrom(ACV_DT_FRM);
			entity.setAcvDateTo(ACV_DT_TO);
			entity.setAcvStatus(ACV_STATUS);
			entity.setAcvDateOpened(ACV_DT_OPND);
			entity.setAcvDateClosed(ACV_DT_CLSD);
			entity.setAcvDatePermanentlyClosed(ACV_DT_PRMNNTLY_CLSD);
			entity.setAcvDateFutureEntered(ACV_DT_FTR_ENTRD);
			entity.setAcvAdCompany(ACV_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlAccountingCalendarValue create(java.lang.String ACV_PRD_PRFX, short ACV_QRTR,
                                                 short ACV_PRD_NMBR, Date ACV_DT_FRM, Date ACV_DT_TO, char ACV_STATUS, Date ACV_DT_OPND, Date ACV_DT_CLSD,
                                                 Date ACV_DT_PRMNNTLY_CLSD, Date ACV_DT_FTR_ENTRD, Integer ACV_AD_CMPNY) throws CreateException {
		try {

			LocalGlAccountingCalendarValue entity = new LocalGlAccountingCalendarValue();

			Debug.print("GlAccountingCalendarValueBean create");

			entity.setAcvPeriodPrefix(ACV_PRD_PRFX);
			entity.setAcvQuarter(ACV_QRTR);
			entity.setAcvPeriodNumber(ACV_PRD_NMBR);
			entity.setAcvDateFrom(ACV_DT_FRM);
			entity.setAcvDateTo(ACV_DT_TO);
			entity.setAcvStatus(ACV_STATUS);
			entity.setAcvDateOpened(ACV_DT_OPND);
			entity.setAcvDateClosed(ACV_DT_CLSD);
			entity.setAcvDatePermanentlyClosed(ACV_DT_PRMNNTLY_CLSD);
			entity.setAcvDateFutureEntered(ACV_DT_FTR_ENTRD);
			entity.setAcvAdCompany(ACV_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}