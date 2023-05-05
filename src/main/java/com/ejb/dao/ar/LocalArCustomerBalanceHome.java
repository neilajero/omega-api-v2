package com.ejb.dao.ar;

import java.util.Collection;
import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArCustomerBalance;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArCustomerBalanceHome {

	public static final String JNDI_NAME = "LocalArCustomerBalanceHome!com.ejb.ar.LocalArCustomerBalanceHome";

	@EJB
	public PersistenceBeanClass em;

	private Date CB_DT = null;
	private double CB_BLNC = 0d;
	private Integer CB_AD_CMPNY = null;

	public LocalArCustomerBalanceHome() {
	}

	// FINDER METHODS

	public LocalArCustomerBalance findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArCustomerBalance entity = (LocalArCustomerBalance) em
					.find(new LocalArCustomerBalance(), pk);
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

	public java.util.Collection findByBeforeOrEqualInvDateAndCstCustomerCode(java.util.Date INV_DT,
			java.lang.String CST_CSTMR_CODE, java.lang.Integer CB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cb) FROM ArCustomerBalance cb  WHERE cb.cbDate <= ?1 AND cb.arCustomer.cstCustomerCode = ?2 AND cb.cbAdCompany = ?3");
			query.setParameter(1, INV_DT);
			query.setParameter(2, CST_CSTMR_CODE);
			query.setParameter(3, CB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerBalanceHome.findByBeforeOrEqualInvDateAndCstCustomerCode(java.com.util.Date INV_DT, java.lang.String CST_CSTMR_CODE, java.lang.Integer CB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBeforeOrEqualInvDateAndCstCustomerCode(java.util.Date INV_DT,
																			 java.lang.String CST_CSTMR_CODE,
																			 java.lang.Integer CB_AD_CMPNY,
																			 String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(cb) FROM ArCustomerBalance cb  WHERE cb.cbDate <= ?1 AND cb.arCustomer.cstCustomerCode = ?2 AND cb.cbAdCompany = ?3",
					companyShortName);
			query.setParameter(1, INV_DT);
			query.setParameter(2, CST_CSTMR_CODE);
			query.setParameter(3, CB_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByAfterInvDateAndCstCustomerCode(java.util.Date INV_DT,
			java.lang.String CST_CSTMR_CODE, java.lang.Integer CB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cb) FROM ArCustomerBalance cb  WHERE cb.cbDate > ?1 AND cb.arCustomer.cstCustomerCode = ?2 AND cb.cbAdCompany = ?3");
			query.setParameter(1, INV_DT);
			query.setParameter(2, CST_CSTMR_CODE);
			query.setParameter(3, CB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerBalanceHome.findByAfterInvDateAndCstCustomerCode(java.com.util.Date INV_DT, java.lang.String CST_CSTMR_CODE, java.lang.Integer CB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAfterInvDateAndCstCustomerCode(java.util.Date INV_DT,
																	 java.lang.String CST_CSTMR_CODE,
																	 java.lang.Integer CB_AD_CMPNY,
																	 String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(cb) FROM ArCustomerBalance cb  WHERE cb.cbDate > ?1 AND cb.arCustomer.cstCustomerCode = ?2 AND cb.cbAdCompany = ?3",
					companyShortName);
			query.setParameter(1, INV_DT);
			query.setParameter(2, CST_CSTMR_CODE);
			query.setParameter(3, CB_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerBalanceHome.findByAfterInvDateAndCstCustomerCode(java.com.util.Date INV_DT, java.lang.String CST_CSTMR_CODE, java.lang.Integer CB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBeforeOrEqualCbDateAndCstCode(java.util.Date CB_DT, java.lang.Integer AR_CST_CODE,
			java.lang.Integer CB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cb) FROM ArCustomerBalance cb  WHERE cb.cbDate <= ?1 AND cb.arCustomer.cstCode = ?2 AND cb.cbAdCompany = ?3");
			query.setParameter(1, CB_DT);
			query.setParameter(2, AR_CST_CODE);
			query.setParameter(3, CB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerBalanceHome.findByBeforeOrEqualCbDateAndCstCode(java.com.util.Date CB_DT, java.lang.Integer AR_CST_CODE, java.lang.Integer CB_AD_CMPNY)");
			throw ex;
		}
	}

	public Collection<LocalArCustomerBalance> findByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer CB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(cb) FROM ArCustomerBalance cb WHERE cb.arCustomer.cstCustomerCode = ?1 AND cb.cbAdCompany = ?2");
			query.setParameter(1, CST_CSTMR_CODE);
			query.setParameter(2, CB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print("EXCEPTION: " + ex.getMessage());
			throw ex;
		}
	}

	public LocalArCustomerBalanceHome CbDate(Date CB_DT) {
		this.CB_DT = CB_DT;
		return this;
	}

	public LocalArCustomerBalanceHome CbBalance(double CB_BLNC) {
		this.CB_BLNC = CB_BLNC;
		return this;
	}

	public LocalArCustomerBalanceHome CbAdCompany(Integer CB_AD_CMPNY) {
		this.CB_AD_CMPNY = CB_AD_CMPNY;
		return this;
	}

	public LocalArCustomerBalance buildCustomerBalance(String companyShortName)
			throws CreateException {
		try {

			LocalArCustomerBalance entity = new LocalArCustomerBalance();

			Debug.print("ArCustomerBalanceBean buildCustomerBalance");
			entity.setCbDate(CB_DT);
			entity.setCbBalance(CB_BLNC);
			entity.setCbAdCompany(CB_AD_CMPNY);

			em.persist(entity, companyShortName);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}
	public LocalArCustomerBalance create(Integer AR_CB_CODE, Date CB_DT, double CB_BLNC, Integer CB_AD_CMPNY)
			throws CreateException {
		try {

			LocalArCustomerBalance entity = new LocalArCustomerBalance();

			Debug.print("ArCustomerBalanceBean create");
			entity.setCbCode(AR_CB_CODE);
			entity.setCbDate(CB_DT);
			entity.setCbBalance(CB_BLNC);
			entity.setCbAdCompany(CB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArCustomerBalance create(Date CB_DT, double CB_BLNC, Integer CB_AD_CMPNY)
			throws CreateException {
		try {

			LocalArCustomerBalance entity = new LocalArCustomerBalance();

			Debug.print("ArCustomerBalanceBean create");
			entity.setCbDate(CB_DT);
			entity.setCbBalance(CB_BLNC);
			entity.setCbAdCompany(CB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}