package com.ejb.dao.ad;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdBankAccountBalance;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdBankAccountBalanceHome {

	public static final String JNDI_NAME = "LocalAdBankAccountBalanceHome!com.ejb.ad.LocalAdBankAccountBalanceHome";

	@EJB
	public PersistenceBeanClass em;

	private Date BAB_DT = null;
	private double BAB_BLNC = 0d;
	private String BAB_TYP = null;
	private Integer BAB_AD_CMPNY = null;


	public LocalAdBankAccountBalanceHome() {
	}

	// FINDER METHODS

	public LocalAdBankAccountBalance findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdBankAccountBalance entity = (LocalAdBankAccountBalance) em
					.find(new LocalAdBankAccountBalance(), pk);
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

	public java.util.Collection findByBeforeOrEqualDateAndBaCodeAndType(java.util.Date DT, java.lang.Integer BA_CODE,
			java.lang.String BAB_TYP, java.lang.Integer BAB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bab) FROM AdBankAccountBalance bab WHERE bab.babDate <= ?1 AND bab.adBankAccount.baCode = ?2 AND bab.babType = ?3 AND bab.babAdCompany = ?4");
			query.setParameter(1, DT);
			query.setParameter(2, BA_CODE);
			query.setParameter(3, BAB_TYP);
			query.setParameter(4, BAB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountBalanceHome.findByBeforeOrEqualDateAndBaCodeAndType(java.com.util.Date DT, java.lang.Integer BA_CODE, java.lang.String BAB_TYP, java.lang.Integer BAB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBeforeOrEqualDateAndBaCodeAndType(java.util.Date DT, java.lang.Integer BA_CODE,
																		java.lang.String BAB_TYP, java.lang.Integer BAB_AD_CMPNY,
																		String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(bab) FROM AdBankAccountBalance bab "
							+ "WHERE bab.babDate <= ?1 AND bab.adBankAccount.baCode = ?2 "
							+ "AND bab.babType = ?3 AND bab.babAdCompany = ?4",
					companyShortName);
			query.setParameter(1, DT);
			query.setParameter(2, BA_CODE);
			query.setParameter(3, BAB_TYP);
			query.setParameter(4, BAB_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByAfterDateAndBaCodeAndType(java.util.Date DT, java.lang.Integer BA_CODE,
			java.lang.String BAB_TYP, java.lang.Integer BAB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bab) FROM AdBankAccountBalance bab WHERE bab.babDate > ?1 AND bab.adBankAccount.baCode = ?2 AND bab.babType = ?3 AND bab.babAdCompany = ?4");
			query.setParameter(1, DT);
			query.setParameter(2, BA_CODE);
			query.setParameter(3, BAB_TYP);
			query.setParameter(4, BAB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountBalanceHome.findByAfterDateAndBaCodeAndType(java.com.util.Date DT, java.lang.Integer BA_CODE, java.lang.String BAB_TYP, java.lang.Integer BAB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAfterDateAndBaCodeAndType(java.util.Date DT, java.lang.Integer BA_CODE,
																java.lang.String BAB_TYP, java.lang.Integer BAB_AD_CMPNY,
																String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(bab) FROM AdBankAccountBalance bab "
							+ "WHERE bab.babDate > ?1 AND bab.adBankAccount.baCode = ?2 "
							+ "AND bab.babType = ?3 AND bab.babAdCompany = ?4",
					companyShortName);
			query.setParameter(1, DT);
			query.setParameter(2, BA_CODE);
			query.setParameter(3, BAB_TYP);
			query.setParameter(4, BAB_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByBaCodeAndType(java.lang.Integer BA_CODE, java.lang.String BAB_TYP,
			java.lang.Integer BAB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bab) FROM AdBankAccountBalance bab WHERE bab.adBankAccount.baCode = ?1 AND bab.babType = ?2 AND bab.babAdCompany = ?3");
			query.setParameter(1, BA_CODE);
			query.setParameter(2, BAB_TYP);
			query.setParameter(3, BAB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountBalanceHome.findByBaCodeAndType(java.lang.Integer BA_CODE, java.lang.String BAB_TYP, java.lang.Integer BAB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBeforeDateAndBaCodeAndType(java.util.Date DT, java.lang.Integer BA_CODE,
			java.lang.String BAB_TYP, java.lang.Integer BAB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bab) FROM AdBankAccountBalance bab WHERE bab.babDate < ?1 AND bab.adBankAccount.baCode = ?2 AND bab.babType = ?3 AND bab.babAdCompany = ?4");
			query.setParameter(1, DT);
			query.setParameter(2, BA_CODE);
			query.setParameter(3, BAB_TYP);
			query.setParameter(4, BAB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountBalanceHome.findByBeforeDateAndBaCodeAndType(java.com.util.Date DT, java.lang.Integer BA_CODE, java.lang.String BAB_TYP, java.lang.Integer BAB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBankAccountBalanceHome BabDate(Date BAB_DT) {
		this.BAB_DT = BAB_DT;
		return this;
	}

	public LocalAdBankAccountBalanceHome BabBalance(double BAB_BLNC) {
		this.BAB_BLNC = BAB_BLNC;
		return this;
	}

	public LocalAdBankAccountBalanceHome BabType(String BAB_TYP) {
		this.BAB_TYP = BAB_TYP;
		return this;
	}

	public LocalAdBankAccountBalanceHome BabAdCompany(Integer BAB_AD_CMPNY) {
		this.BAB_AD_CMPNY = BAB_AD_CMPNY;
		return this;
	}

	public LocalAdBankAccountBalance buildBankAccountBalance(String companyShortName) throws CreateException {
		try {

			LocalAdBankAccountBalance entity = new LocalAdBankAccountBalance();

			Debug.print("AdBankAccountBalanceBean buildBankAccountBalance");

			entity.setBabDate(BAB_DT);
			entity.setBabBalance(BAB_BLNC);
			entity.setBabType(BAB_TYP);
			entity.setBabAdCompany(BAB_AD_CMPNY);

			em.persist(entity, companyShortName);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}

	}

	public LocalAdBankAccountBalance create(Integer BAB_CODE, Date BAB_DT, double BAB_BLNC, String BAB_TYP,
                                            Integer BAB_AD_CMPNY) throws CreateException {
		try {

			LocalAdBankAccountBalance entity = new LocalAdBankAccountBalance();

			Debug.print("AdBankAccountBalanceBean create");
			entity.setBabCode(BAB_CODE);
			entity.setBabDate(BAB_DT);
			entity.setBabBalance(BAB_BLNC);
			entity.setBabType(BAB_TYP);
			entity.setBabAdCompany(BAB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}

	}

	public LocalAdBankAccountBalance create(Date BAB_DT, double BAB_BLNC, String BAB_TYP,
                                            Integer BAB_AD_CMPNY) throws CreateException {
		try {

			LocalAdBankAccountBalance entity = new LocalAdBankAccountBalance();

			Debug.print("AdBankAccountBalanceBean create");

			entity.setBabDate(BAB_DT);
			entity.setBabBalance(BAB_BLNC);
			entity.setBabType(BAB_TYP);
			entity.setBabAdCompany(BAB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}

	}

}