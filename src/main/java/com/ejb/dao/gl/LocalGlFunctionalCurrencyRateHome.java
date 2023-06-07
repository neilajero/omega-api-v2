package com.ejb.dao.gl;

import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.util.Debug;

@Stateless
public class LocalGlFunctionalCurrencyRateHome {

	public static final String JNDI_NAME = "LocalGlFunctionalCurrencyRateHome!com.ejb.gl.LocalGlFunctionalCurrencyRateHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlFunctionalCurrencyRateHome() {
	}

	// FINDER METHODS

	public LocalGlFunctionalCurrencyRate findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlFunctionalCurrencyRate entity = (LocalGlFunctionalCurrencyRate) em
					.find(new LocalGlFunctionalCurrencyRate(), pk);
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

	public java.util.Collection findByFrDate(java.util.Date FR_DT, java.lang.Integer FR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(fr) FROM GlFunctionalCurrencyRate fr WHERE fr.frDate=?1 AND fr.frAdCompany = ?2");
			query.setParameter(1, FR_DT);
			query.setParameter(2, FR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFunctionalCurrencyRateHome.findByFrDate(java.com.util.Date FR_DT, java.lang.Integer FR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByFrDate(java.util.Date FR_DT, java.lang.Integer FR_AD_CMPNY, String companyShortName)
			throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(fr) FROM GlFunctionalCurrencyRate fr "
							+ "WHERE fr.frDate=?1 AND fr.frAdCompany = ?2", companyShortName);
			query.setParameter(1, FR_DT);
			query.setParameter(2, FR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findFcrAll(java.lang.Integer FR_AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(fr) FROM GlFunctionalCurrencyRate fr WHERE fr.frAdCompany = ?1");
			query.setParameter(1, FR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFunctionalCurrencyRateHome.findFcrAll(java.lang.Integer FR_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlFunctionalCurrencyRate findByFcCodeAndDate(java.lang.Integer FC_CODE, java.util.Date FR_DT, java.lang.Integer FR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(fr) FROM GlFunctionalCurrency fc, IN(fc.glFunctionalCurrencyRates) fr WHERE fc.fcCode=?1 AND fr.frDate=?2 AND fr.frAdCompany = ?3");
			query.setParameter(1, FC_CODE);
			query.setParameter(2, FR_DT);
			query.setParameter(3, FR_AD_CMPNY);
            return (LocalGlFunctionalCurrencyRate) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlFunctionalCurrencyRateHome.findByFcCodeAndDate(java.lang.Integer FC_CODE, java.com.util.Date FR_DT, java.lang.Integer FR_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFunctionalCurrencyRateHome.findByFcCodeAndDate(java.lang.Integer FC_CODE, java.com.util.Date FR_DT, java.lang.Integer FR_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlFunctionalCurrencyRate findByFcCodeAndDate(java.lang.Integer FC_CODE, java.util.Date FR_DT, java.lang.Integer FR_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(fr) FROM GlFunctionalCurrency fc, IN(fc.glFunctionalCurrencyRates) fr "
							+ "WHERE fc.fcCode=?1 AND fr.frDate=?2 AND fr.frAdCompany = ?3",
					companyShortName);
			query.setParameter(1, FC_CODE);
			query.setParameter(2, FR_DT);
			query.setParameter(3, FR_AD_CMPNY);
			return (LocalGlFunctionalCurrencyRate) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findPriorByFcCodeAndDate(java.lang.Integer FC_CODE, java.util.Date FR_DT,
			java.lang.Integer FR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(fr) FROM GlFunctionalCurrency fc, IN(fc.glFunctionalCurrencyRates) fr WHERE fc.fcCode=?1 AND fr.frDate<?2 AND fr.frAdCompany = ?3");
			query.setParameter(1, FC_CODE);
			query.setParameter(2, FR_DT);
			query.setParameter(3, FR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFunctionalCurrencyRateHome.findPriorByFcCodeAndDate(java.lang.Integer FC_CODE, java.com.util.Date FR_DT, java.lang.Integer FR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPriorByFcCode(java.lang.Integer FC_CODE, java.lang.Integer FR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(fr) FROM GlFunctionalCurrencyRate fr WHERE fr.glFunctionalCurrency.fcCode = ?1 AND fr.frAdCompany = ?2");
			query.setParameter(1, FC_CODE);
			query.setParameter(2, FR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFunctionalCurrencyRateHome.findPriorByFcCode(java.lang.Integer FC_CODE, java.lang.Integer FR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getFrByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
			java.lang.Integer LIMIT, java.lang.Integer OFFSET) throws FinderException {

		try {
			Query query = em.createQuery(jbossQl);
			int cnt = 1;
			for (Object data : args) {
				query.setParameter(cnt, data);
				cnt++;
			}
			if(LIMIT>0){query.setMaxResults(LIMIT);}
            query.setFirstResult(OFFSET);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlFunctionalCurrencyRate create(java.lang.Integer FR_CODE, double FR_X_TO_USD, Date FR_DT,
                                                Integer FR_AD_CMPNY) throws CreateException {
		try {

			LocalGlFunctionalCurrencyRate entity = new LocalGlFunctionalCurrencyRate();

			Debug.print("GlFunctionalCurrencyRateBean create");

			entity.setFrCode(FR_CODE);
			entity.setFrXToUsd(FR_X_TO_USD);
			entity.setFrDate(FR_DT);
			entity.setFrAdCompany(FR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlFunctionalCurrencyRate create(double FR_X_TO_USD, Date FR_DT, Integer FR_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlFunctionalCurrencyRate entity = new LocalGlFunctionalCurrencyRate();

			Debug.print("GlFunctionalCurrencyRateBean create");

			Debug.print("create: " + FR_X_TO_USD);
			entity.setFrXToUsd(FR_X_TO_USD);
			entity.setFrDate(FR_DT);
			entity.setFrAdCompany(FR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}