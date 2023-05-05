package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlChartOfAccountBalance;
import com.util.Debug;

@Stateless
public class LocalGlChartOfAccountBalanceHome implements ILocalGlChartOfAccountBalanceHome {

	public static final String JNDI_NAME = "LocalGlChartOfAccountBalanceHome!com.ejb.gl.LocalGlChartOfAccountBalanceHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlChartOfAccountBalanceHome() {
	}

	// FINDER METHODS

	public LocalGlChartOfAccountBalance findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlChartOfAccountBalance entity = (LocalGlChartOfAccountBalance) em
					.find(new LocalGlChartOfAccountBalance(), pk);
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

	public LocalGlChartOfAccountBalance findByAcvCodeAndCoaCode(java.lang.Integer ACV_CODE, java.lang.Integer COA_CODE,
			java.lang.Integer COAB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(coab) FROM GlAccountingCalendarValue acv, IN(acv.glChartOfAccountBalances) coab WHERE acv.acvCode=?1 AND coab.glChartOfAccount.coaCode=?2 AND coab.coabAdCompany = ?3");
			query.setParameter(1, ACV_CODE);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, COAB_AD_CMPNY);
            return (LocalGlChartOfAccountBalance) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(java.lang.Integer ACV_CODE, java.lang.Integer COA_CODE, java.lang.Integer COAB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(java.lang.Integer ACV_CODE, java.lang.Integer COA_CODE, java.lang.Integer COAB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlChartOfAccountBalance findByAcvCodeAndCoaCode(java.lang.Integer ACV_CODE, java.lang.Integer COA_CODE,
																java.lang.Integer COAB_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(coab) FROM GlAccountingCalendarValue acv, IN(acv.glChartOfAccountBalances) coab "
							+ "WHERE acv.acvCode=?1 AND coab.glChartOfAccount.coaCode=?2 AND coab.coabAdCompany = ?3",
					companyShortName);
			query.setParameter(1, ACV_CODE);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, COAB_AD_CMPNY);
			return (LocalGlChartOfAccountBalance) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByAcvCodeAndCoaSegment1(java.lang.Integer ACV_CODE, java.lang.String COA_SEGMENT1,
			java.lang.Integer COAB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(coab) FROM GlAccountingCalendarValue acv, IN(acv.glChartOfAccountBalances) coab WHERE acv.acvCode=?1 AND coab.glChartOfAccount.coaSegment1=?2 AND coab.coabAdCompany = ?3");
			query.setParameter(1, ACV_CODE);
			query.setParameter(2, COA_SEGMENT1);
			query.setParameter(3, COAB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlChartOfAccountBalanceHome.findByAcvCodeAndCoaSegment1(java.lang.Integer ACV_CODE, java.lang.String COA_SEGMENT1, java.lang.Integer COAB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlChartOfAccountBalance findByOtEffectiveDateAndCoaCode(java.util.Date OT_EFFCTV_DT,
			java.lang.Integer COA_CODE, java.lang.Integer COAB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(coab) FROM GlChartOfAccount coa, IN(coa.glChartOfAccountBalances) coab WHERE coa.coaCode=?2 AND coab.glAccountingCalendarValue.acvDateFrom<=?1 AND coab.glAccountingCalendarValue.acvDateTo>=?1 AND coab.coabAdCompany = ?3");
			query.setParameter(1, OT_EFFCTV_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, COAB_AD_CMPNY);
            return (LocalGlChartOfAccountBalance) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlChartOfAccountBalanceHome.findByOtEffectiveDateAndCoaCode(java.com.util.Date OT_EFFCTV_DT, java.lang.Integer COA_CODE, java.lang.Integer COAB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlChartOfAccountBalanceHome.findByOtEffectiveDateAndCoaCode(java.com.util.Date OT_EFFCTV_DT, java.lang.Integer COA_CODE, java.lang.Integer COAB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlChartOfAccountBalance findByCoaCodeAndAcvCode(java.lang.Integer COA_CODE, java.lang.Integer ACV_CODE,
			java.lang.Integer COAB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(coab) FROM GlChartOfAccount coa, IN(coa.glChartOfAccountBalances) coab WHERE coa.coaCode=?1 AND coab.glAccountingCalendarValue.acvCode =?2 AND coab.coabAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, ACV_CODE);
			query.setParameter(3, COAB_AD_CMPNY);
            return (LocalGlChartOfAccountBalance) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlChartOfAccountBalanceHome.findByCoaCodeAndAcvCode(java.lang.Integer COA_CODE, java.lang.Integer ACV_CODE, java.lang.Integer COAB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlChartOfAccountBalanceHome.findByCoaCodeAndAcvCode(java.lang.Integer COA_CODE, java.lang.Integer ACV_CODE, java.lang.Integer COAB_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlChartOfAccountBalance create(java.lang.Integer COAB_CODE, double COAB_TTL_DBT,
                                               double COAB_TTL_CRDT, double COAB_BEG_BLNC, double COAB_END_BLNC, Integer COAB_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlChartOfAccountBalance entity = new LocalGlChartOfAccountBalance();

			Debug.print("GlChartOfAccountBalance create");

			entity.setCoabCode(COAB_CODE);
			entity.setCoabTotalDebit(COAB_TTL_DBT);
			entity.setCoabTotalCredit(COAB_TTL_CRDT);
			entity.setCoabBeginningBalance(COAB_BEG_BLNC);
			entity.setCoabEndingBalance(COAB_END_BLNC);
			entity.setCoabAdCompany(COAB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlChartOfAccountBalance create(double COAB_TTL_DBT, double COAB_TTL_CRDT,
                                               double COAB_BEG_BLNC, double COAB_END_BLNC, Integer COAB_AD_CMPNY) throws CreateException {
		try {

			LocalGlChartOfAccountBalance entity = new LocalGlChartOfAccountBalance();

			Debug.print("GlChartOfAccountBalance create");

			entity.setCoabTotalDebit(COAB_TTL_DBT);
			entity.setCoabTotalCredit(COAB_TTL_CRDT);
			entity.setCoabBeginningBalance(COAB_BEG_BLNC);
			entity.setCoabEndingBalance(COAB_END_BLNC);
			entity.setCoabAdCompany(COAB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}