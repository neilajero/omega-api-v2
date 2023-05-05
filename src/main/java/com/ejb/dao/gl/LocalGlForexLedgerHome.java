package com.ejb.dao.gl;

import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlForexLedger;
import com.util.Debug;

@Stateless
public class LocalGlForexLedgerHome {

	public static final String JNDI_NAME = "LocalGlForexLedgerHome!com.ejb.gl.LocalGlForexLedgerHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlForexLedgerHome() {
	}

	// FINDER METHODS

	public LocalGlForexLedger findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlForexLedger entity = (LocalGlForexLedger) em
					.find(new LocalGlForexLedger(), pk);
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

	public java.util.Collection findLatestGlFrlByFrlDateAndByCoaCode(java.util.Date FRL_DT, java.lang.Integer COA_CODE,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(frl) FROM GlForexLedger frl WHERE frl.frlDate<=?1 AND frl.glChartOfAccount.coaCode =?2 AND frl.frlAdCompany =?3");
			query.setParameter(1, FRL_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(java.com.util.Date FRL_DT, java.lang.Integer COA_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findLatestGlFrlByFrlDateAndByCoaCode(java.util.Date FRL_DT, java.lang.Integer COA_CODE,
																	 java.lang.Integer AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(frl) FROM GlForexLedger frl "
							+ "WHERE frl.frlDate<=?1 AND frl.glChartOfAccount.coaCode =?2 AND frl.frlAdCompany =?3",
					companyShortName);
			query.setParameter(1, FRL_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByGreaterThanFrlDateAndCoaCode(java.util.Date FRL_DT, java.lang.Integer COA_CODE,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(frl) FROM GlForexLedger frl WHERE frl.frlDate>?1 AND frl.glChartOfAccount.coaCode =?2 AND frl.frlAdCompany =?3");
			query.setParameter(1, FRL_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(java.com.util.Date FRL_DT, java.lang.Integer COA_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByGreaterThanFrlDateAndCoaCode(java.util.Date FRL_DT, java.lang.Integer COA_CODE,
																   java.lang.Integer AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(frl) FROM GlForexLedger frl "
							+ "WHERE frl.frlDate>?1 AND frl.glChartOfAccount.coaCode =?2 AND frl.frlAdCompany =?3",
					companyShortName);
			query.setParameter(1, FRL_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findLatestGlFrlByFrlTypeAndFrlDateAndCoaCode(java.lang.String FRL_TYP,
			java.util.Date FRL_DT, java.lang.Integer COA_CODE, java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(frl) FROM GlForexLedger frl WHERE frl.frlType=?1 AND frl.frlDate<=?2 AND frl.glChartOfAccount.coaCode =?3 AND frl.frlAdCompany =?4");
			query.setParameter(1, FRL_TYP);
			query.setParameter(2, FRL_DT);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlForexLedgerHome.findLatestGlFrlByFrlTypeAndFrlDateAndCoaCode(java.lang.String FRL_TYP, java.com.util.Date FRL_DT, java.lang.Integer COA_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByFrlDateFromAndFrlDateToAndCoaCode(java.util.Date FRL_DT_FRM,
			java.util.Date FRL_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(frl) FROM GlForexLedger frl WHERE frl.frlDate>=?1 AND frl.frlDate<=?2 AND frl.glChartOfAccount.coaCode =?3 AND frl.frlAdCompany =?4");
			query.setParameter(1, FRL_DT_FRM);
			query.setParameter(2, FRL_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlForexLedgerHome.findByFrlDateFromAndFrlDateToAndCoaCode(java.com.util.Date FRL_DT_FRM, java.com.util.Date FRL_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlForexLedger create(Integer FRL_CODE, Date FRL_DT, Integer FRL_LN, String FRL_TYP,
                                     double FRL_AMNT, double FRL_RT, double FRL_BLNC, double FRL_FRX_GN_LSS, Integer FRL_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlForexLedger entity = new LocalGlForexLedger();

			Debug.print("GlForexLedgerBean create");

			entity.setFrlCode(FRL_CODE);
			entity.setFrlDate(FRL_DT);
			entity.setFrlLine(FRL_LN);
			entity.setFrlType(FRL_TYP);
			entity.setFrlAmount(FRL_AMNT);
			entity.setFrlRate(FRL_RT);
			entity.setFrlBalance(FRL_BLNC);
			entity.setFrlForexGainLoss(FRL_FRX_GN_LSS);
			entity.setFrlAdCompany(FRL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlForexLedger create(Date FRL_DT, Integer FRL_LN, String FRL_TYP, double FRL_AMNT,
                                     double FRL_RT, double FRL_BLNC, double FRL_FRX_GN_LSS, Integer FRL_AD_CMPNY) throws CreateException {
		try {

			LocalGlForexLedger entity = new LocalGlForexLedger();

			Debug.print("GlForexLedgerBean create");

			entity.setFrlDate(FRL_DT);
			entity.setFrlLine(FRL_LN);
			entity.setFrlType(FRL_TYP);
			entity.setFrlAmount(FRL_AMNT);
			entity.setFrlRate(FRL_RT);
			entity.setFrlBalance(FRL_BLNC);
			entity.setFrlForexGainLoss(FRL_FRX_GN_LSS);
			entity.setFrlAdCompany(FRL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}