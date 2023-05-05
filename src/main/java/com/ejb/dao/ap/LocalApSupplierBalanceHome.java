package com.ejb.dao.ap;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApSupplierBalance;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalApSupplierBalanceHome {

	public static final String JNDI_NAME = "LocalApSupplierBalanceHome!com.ejb.ap.LocalApSupplierBalanceHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalApSupplierBalanceHome() {
	}

	// FINDER METHODS

	public LocalApSupplierBalance findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalApSupplierBalance entity = (LocalApSupplierBalance) em
					.find(new LocalApSupplierBalance(), pk);
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

	public java.util.Collection findByBeforeOrEqualVouDateAndSplSupplierCode(java.util.Date SB_DT,
			java.lang.String SPL_SPPLR_CODE, java.lang.Integer SB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sb) FROM ApSupplierBalance sb  WHERE sb.sbDate <= ?1 AND sb.apSupplier.splSupplierCode = ?2 AND sb.sbAdCompany = ?3");
			query.setParameter(1, SB_DT);
			query.setParameter(2, SPL_SPPLR_CODE);
			query.setParameter(3, SB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierBalanceHome.findByBeforeOrEqualVouDateAndSplSupplierCode(java.com.util.Date SB_DT, java.lang.String SPL_SPPLR_CODE, java.lang.Integer SB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAfterVouDateAndSplSupplierCode(java.util.Date SB_DT,
			java.lang.String SPL_SPPLR_CODE, java.lang.Integer SB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sb) FROM ApSupplierBalance sb  WHERE sb.sbDate > ?1 AND sb.apSupplier.splSupplierCode = ?2 AND sb.sbAdCompany = ?3");
			query.setParameter(1, SB_DT);
			query.setParameter(2, SPL_SPPLR_CODE);
			query.setParameter(3, SB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierBalanceHome.findByAfterVouDateAndSplSupplierCode(java.com.util.Date SB_DT, java.lang.String SPL_SPPLR_CODE, java.lang.Integer SB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBeforeOrEqualSbDateAndSplCode(java.util.Date SB_DT, java.lang.Integer SPL_CODE,
			java.lang.Integer SB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sb) FROM ApSupplierBalance sb  WHERE sb.sbDate <= ?1 AND sb.apSupplier.splCode = ?2 AND sb.sbAdCompany = ?3");
			query.setParameter(1, SB_DT);
			query.setParameter(2, SPL_CODE);
			query.setParameter(3, SB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierBalanceHome.findByBeforeOrEqualSbDateAndSplCode(java.com.util.Date SB_DT, java.lang.Integer SPL_CODE, java.lang.Integer SB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBeforeSbDateAndSplCode(java.util.Date SB_DT, java.lang.Integer SPL_CODE,
			java.lang.Integer SB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sb) FROM ApSupplierBalance sb  WHERE sb.sbDate < ?1 AND sb.apSupplier.splCode = ?2 AND sb.sbAdCompany = ?3");
			query.setParameter(1, SB_DT);
			query.setParameter(2, SPL_CODE);
			query.setParameter(3, SB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierBalanceHome.findByBeforeSbDateAndSplCode(java.com.util.Date SB_DT, java.lang.Integer SPL_CODE, java.lang.Integer SB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBySbDateFromAndSbDateToAndSplCode(java.util.Date SB_DT_FRM, java.util.Date SB_DT_TO,
			java.lang.Integer SPL_CODE, java.lang.Integer SB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sb) FROM ApSupplierBalance sb  WHERE sb.sbDate >= ?1 AND sb.sbDate <= ?2 AND sb.apSupplier.splCode = ?3 AND sb.sbAdCompany = ?4");
			query.setParameter(1, SB_DT_FRM);
			query.setParameter(2, SB_DT_TO);
			query.setParameter(3, SPL_CODE);
			query.setParameter(4, SB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierBalanceHome.findBySbDateFromAndSbDateToAndSplCode(java.com.util.Date SB_DT_FRM, java.com.util.Date SB_DT_TO, java.lang.Integer SPL_CODE, java.lang.Integer SB_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalApSupplierBalance create(Integer SB_CODE, Date SB_DT, double SB_BLNC, Integer SB_AD_CMPNY)
			throws CreateException {
		try {

			LocalApSupplierBalance entity = new LocalApSupplierBalance();

			Debug.print("ApSupplierBalanceBean create");
			entity.setSbCode(SB_CODE);
			entity.setSbDate(SB_DT);
			entity.setSbBalance(SB_BLNC);
			entity.setSbAdCompany(SB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalApSupplierBalance create(Date SB_DT, double SB_BLNC, Integer SB_AD_CMPNY)
			throws CreateException {
		try {

			LocalApSupplierBalance entity = new LocalApSupplierBalance();

			Debug.print("ApSupplierBalanceBean create");
			entity.setSbDate(SB_DT);
			entity.setSbBalance(SB_BLNC);
			entity.setSbAdCompany(SB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}