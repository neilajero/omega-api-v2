package com.ejb.dao.gl;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlInvestorAccountBalance;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalGlInvestorAccountBalanceHome implements ILocalGlInvestorAccountBalanceHome {

	public static final String JNDI_NAME = "LocalGlInvestorAccountBalanceHome!com.ejb.gl.LocalGlInvestorAccountBalanceHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlInvestorAccountBalanceHome() {
	}

	// FINDER METHODS

	public LocalGlInvestorAccountBalance findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlInvestorAccountBalance entity = (LocalGlInvestorAccountBalance) em
					.find(new LocalGlInvestorAccountBalance(), pk);
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

	public LocalGlInvestorAccountBalance findByAcvCodeAndSplCode(java.lang.Integer ACV_CODE, java.lang.Integer SPL_CODE,
			java.lang.Integer IRAB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(irab) FROM GlAccountingCalendarValue acv, IN(acv.glInvestorAccountBalances) irab WHERE acv.acvCode=?1 AND irab.apSupplier.splCode=?2 AND irab.irabAdCompany = ?3");
			query.setParameter(1, ACV_CODE);
			query.setParameter(2, SPL_CODE);
			query.setParameter(3, IRAB_AD_CMPNY);
            return (LocalGlInvestorAccountBalance) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlInvestorAccountBalanceHome.findByAcvCodeAndSplCode(java.lang.Integer ACV_CODE, java.lang.Integer SPL_CODE, java.lang.Integer IRAB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlInvestorAccountBalanceHome.findByAcvCodeAndSplCode(java.lang.Integer ACV_CODE, java.lang.Integer SPL_CODE, java.lang.Integer IRAB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlInvestorAccountBalance findByAcvCodeAndSplCode(java.lang.Integer ACV_CODE, java.lang.Integer SPL_CODE,
																 java.lang.Integer IRAB_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(irab) FROM GlAccountingCalendarValue acv, IN(acv.glInvestorAccountBalances) irab "
							+ "WHERE acv.acvCode=?1 AND irab.apSupplier.splCode=?2 AND irab.irabAdCompany = ?3",
					companyShortName);
			query.setParameter(1, ACV_CODE);
			query.setParameter(2, SPL_CODE);
			query.setParameter(3, IRAB_AD_CMPNY);
			return (LocalGlInvestorAccountBalance) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findBonusAndInterestByAcCodeAndSplCode(java.lang.Integer GL_AC_CODE,
			java.lang.Integer SPL_CODE, java.lang.Integer IRAB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(irab) FROM GlInvestorAccountBalance irab WHERE irab.irabInterest = 0 AND irab.irabBonus = 0  AND irab.irabEndingBalance > 0 AND irab.glAccountingCalendarValue.glAccountingCalendar.acCode =?1 AND irab.apSupplier.splCode = ?2 AND irab.irabAdCompany = ?3");
			query.setParameter(1, GL_AC_CODE);
			query.setParameter(2, SPL_CODE);
			query.setParameter(3, IRAB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlInvestorAccountBalanceHome.findBonusAndInterestByAcCodeAndSplCode(java.lang.Integer GL_AC_CODE, java.lang.Integer SPL_CODE, java.lang.Integer IRAB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAcvCode(java.lang.Integer ACV_CODE, java.lang.Integer IRAB_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(irab) FROM GlAccountingCalendarValue acv, IN(acv.glInvestorAccountBalances) irab WHERE acv.acvCode=?1 AND irab.irabAdCompany = ?2");
			query.setParameter(1, ACV_CODE);
			query.setParameter(2, IRAB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlInvestorAccountBalanceHome.findByAcvCode(java.lang.Integer ACV_CODE, java.lang.Integer IRAB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAcvCodeBonusAndInterest(java.lang.Integer ACV_CODE,
			java.lang.Integer IRAB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(irab) FROM GlAccountingCalendarValue acv, IN(acv.glInvestorAccountBalances) irab WHERE acv.acvCode=?1 AND irab.irabInterest = 0 AND irab.irabBeginningBalance > 0 AND irab.irabAdCompany = ?2");
			query.setParameter(1, ACV_CODE);
			query.setParameter(2, IRAB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlInvestorAccountBalanceHome.findByAcvCodeBonusAndInterest(java.lang.Integer ACV_CODE, java.lang.Integer IRAB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlInvestorAccountBalance findByOtEffectiveDateAndSplCode(java.util.Date OT_EFFCTV_DT,
			java.lang.Integer SPL_CODE, java.lang.Integer IRAB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(irab) FROM ApSupplier spl, IN(spl.glInvestorAccountBalances) irab WHERE spl.splCode=?2 AND irab.glAccountingCalendarValue.acvDateFrom<=?1 AND irab.glAccountingCalendarValue.acvDateTo>=?1 AND irab.irabAdCompany = ?3");
			query.setParameter(1, OT_EFFCTV_DT);
			query.setParameter(2, SPL_CODE);
			query.setParameter(3, IRAB_AD_CMPNY);
            return (LocalGlInvestorAccountBalance) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlInvestorAccountBalanceHome.findByOtEffectiveDateAndSplCode(java.com.util.Date OT_EFFCTV_DT, java.lang.Integer SPL_CODE, java.lang.Integer IRAB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlInvestorAccountBalanceHome.findByOtEffectiveDateAndSplCode(java.com.util.Date OT_EFFCTV_DT, java.lang.Integer SPL_CODE, java.lang.Integer IRAB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlInvestorAccountBalance findBySplCodeAndAcvCode(java.lang.Integer SPL_CODE, java.lang.Integer ACV_CODE,
			java.lang.Integer IRAB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(irab) FROM ApSupplier spl, IN(spl.glInvestorAccountBalances) irab WHERE spl.splCode=?1 AND irab.glAccountingCalendarValue.acvCode =?2 AND irab.irabAdCompany = ?3");
			query.setParameter(1, SPL_CODE);
			query.setParameter(2, ACV_CODE);
			query.setParameter(3, IRAB_AD_CMPNY);
            return (LocalGlInvestorAccountBalance) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlInvestorAccountBalanceHome.findBySplCodeAndAcvCode(java.lang.Integer SPL_CODE, java.lang.Integer ACV_CODE, java.lang.Integer IRAB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlInvestorAccountBalanceHome.findBySplCodeAndAcvCode(java.lang.Integer SPL_CODE, java.lang.Integer ACV_CODE, java.lang.Integer IRAB_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlInvestorAccountBalance create(java.lang.Integer IRAB_CODE, double IRAB_TTL_DBT,
                                                double IRAB_TTL_CRDT, byte IRAB_BNS, byte IRAB_INT, double IRAB_TTL_BNS, double IRAB_TTL_INT,
                                                double IRAB_MNTHLY_BNS_RT, double IRAB_MNTHLY_INT_RT, double IRAB_BEG_BLNC, double IRAB_END_BLNC,
                                                Integer IRAB_AD_CMPNY) throws CreateException {
		try {

			LocalGlInvestorAccountBalance entity = new LocalGlInvestorAccountBalance();

			Debug.print("GlInvestorAccountBalance create");

			entity.setIrabCode(IRAB_CODE);
			entity.setIrabTotalDebit(IRAB_TTL_DBT);
			entity.setIrabTotalCredit(IRAB_TTL_CRDT);
			entity.setIrabBonus(IRAB_BNS);
			entity.setIrabInterest(IRAB_INT);
			entity.setIrabTotalBonus(IRAB_TTL_BNS);
			entity.setIrabTotalInterest(IRAB_TTL_INT);
			entity.setIrabMonthlyBonusRate(IRAB_MNTHLY_BNS_RT);
			entity.setIrabMonthlyInterestRate(IRAB_MNTHLY_INT_RT);
			entity.setIrabBeginningBalance(IRAB_BEG_BLNC);
			entity.setIrabEndingBalance(IRAB_END_BLNC);
			entity.setIrabAdCompany(IRAB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlInvestorAccountBalance create(double IRAB_TTL_DBT, double IRAB_TTL_CRDT, byte IRAB_BNS,
                                                byte IRAB_INT, double IRAB_TTL_BNS, double IRAB_TTL_INT, double IRAB_MNTHLY_BNS_RT,
                                                double IRAB_MNTHLY_INT_RT, double IRAB_BEG_BLNC, double IRAB_END_BLNC, Integer IRAB_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlInvestorAccountBalance entity = new LocalGlInvestorAccountBalance();

			Debug.print("GlInvestorAccountBalance create");

			entity.setIrabTotalDebit(IRAB_TTL_DBT);
			entity.setIrabTotalCredit(IRAB_TTL_CRDT);
			entity.setIrabBonus(IRAB_BNS);
			entity.setIrabInterest(IRAB_INT);
			entity.setIrabTotalBonus(IRAB_TTL_BNS);
			entity.setIrabTotalInterest(IRAB_TTL_INT);
			entity.setIrabMonthlyBonusRate(IRAB_MNTHLY_BNS_RT);
			entity.setIrabMonthlyInterestRate(IRAB_MNTHLY_INT_RT);
			entity.setIrabBeginningBalance(IRAB_BEG_BLNC);
			entity.setIrabEndingBalance(IRAB_END_BLNC);
			entity.setIrabAdCompany(IRAB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}