package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArWithholdingTaxCode;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArWithholdingTaxCodeHome {

	public static final String JNDI_NAME = "LocalArWithholdingTaxCodeHome!com.ejb.ar.LocalArWithholdingTaxCodeHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArWithholdingTaxCodeHome() {
	}

	// FINDER METHODS

	public LocalArWithholdingTaxCode findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArWithholdingTaxCode entity = (LocalArWithholdingTaxCode) em
					.find(new LocalArWithholdingTaxCode(), pk);
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

	public java.util.Collection findEnabledWtcAll(java.lang.Integer WTC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(wtc) FROM ArWithholdingTaxCode wtc WHERE wtc.wtcEnable = 1 AND wtc.wtcAdCompany = ?1");
			query.setParameter(1, WTC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArWithholdingTaxCodeHome.findEnabledWtcAll(java.lang.Integer WTC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArWithholdingTaxCode findByWtcName(java.lang.String WTC_NM, java.lang.Integer WTC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(wtc) FROM ArWithholdingTaxCode wtc WHERE wtc.wtcName = ?1 AND wtc.wtcAdCompany = ?2");
			query.setParameter(1, WTC_NM);
			query.setParameter(2, WTC_AD_CMPNY);
            return (LocalArWithholdingTaxCode) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArWithholdingTaxCodeHome.findByWtcName(java.lang.String WTC_NM, java.lang.Integer WTC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArWithholdingTaxCodeHome.findByWtcName(java.lang.String WTC_NM, java.lang.Integer WTC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArWithholdingTaxCode findByWtcName(java.lang.String WTC_NM, Integer companyCode, String companyShortName) throws FinderException {
		Debug.print("LocalArWithholdingTaxCodeHome findByWtcName");
		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(wtc) FROM ArWithholdingTaxCode wtc WHERE wtc.wtcName = ?1 AND wtc.wtcAdCompany = ?2", companyShortName);
			query.setParameter(1, WTC_NM);
			query.setParameter(2, companyCode);
			return (LocalArWithholdingTaxCode) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findWtcAll(java.lang.Integer WTC_AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(wtc) FROM ArWithholdingTaxCode wtc WHERE wtc.wtcAdCompany = ?1");
			query.setParameter(1, WTC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArWithholdingTaxCodeHome.findWtcAll(java.lang.Integer WTC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getWtcByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
			throws FinderException {

		try {
			Query query = em.createQuery(jbossQl);
			int cnt = 1;
			for (Object data : args) {
				query.setParameter(cnt, data);
				cnt++;
			}
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalArWithholdingTaxCode create(Integer AR_WTC_CODE, String WTC_NM, String WTC_DESC, double WTC_RT,
                                            byte WTC_ENBL, Integer WTC_AD_CMPNY) throws CreateException {
		try {

			LocalArWithholdingTaxCode entity = new LocalArWithholdingTaxCode();

			Debug.print("ArWithholdingTaxCodeBean create");

			entity.setWtcCode(AR_WTC_CODE);
			entity.setWtcName(WTC_NM);
			entity.setWtcDescription(WTC_DESC);
			entity.setWtcRate(WTC_RT);
			entity.setWtcEnable(WTC_ENBL);
			entity.setWtcAdCompany(WTC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArWithholdingTaxCode create(String WTC_NM, String WTC_DESC, double WTC_RT, byte WTC_ENBL,
                                            Integer WTC_AD_CMPNY) throws CreateException {
		try {

			LocalArWithholdingTaxCode entity = new LocalArWithholdingTaxCode();

			Debug.print("ArWithholdingTaxCodeBean create");

			entity.setWtcName(WTC_NM);
			entity.setWtcDescription(WTC_DESC);
			entity.setWtcRate(WTC_RT);
			entity.setWtcEnable(WTC_ENBL);
			entity.setWtcAdCompany(WTC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}