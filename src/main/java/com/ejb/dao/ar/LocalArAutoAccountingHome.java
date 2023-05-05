package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArAutoAccounting;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArAutoAccountingHome {

	public static final String JNDI_NAME = "LocalArAutoAccountingHome!com.ejb.ar.LocalArAutoAccountingHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArAutoAccountingHome() {
	}

	// FINDER METHODS

	public LocalArAutoAccounting findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArAutoAccounting entity = (LocalArAutoAccounting) em
					.find(new LocalArAutoAccounting(), pk);
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

	public java.util.Collection findAaAll(java.lang.Integer AA_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(aa) FROM ArAutoAccounting aa WHERE aa.aaAdCompany = ?1");
			query.setParameter(1, AA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArAutoAccountingHome.findAaAll(java.lang.Integer AA_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArAutoAccounting findByAaAccountType(java.lang.String AA_ACCNT_TYP, java.lang.Integer AA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(aa) FROM ArAutoAccounting aa WHERE aa.aaAccountType = ?1 AND aa.aaAdCompany = ?2");
			query.setParameter(1, AA_ACCNT_TYP);
			query.setParameter(2, AA_AD_CMPNY);
            return (LocalArAutoAccounting) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArAutoAccountingHome.findByAaAccountType(java.lang.String AA_ACCNT_TYP, java.lang.Integer AA_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArAutoAccountingHome.findByAaAccountType(java.lang.String AA_ACCNT_TYP, java.lang.Integer AA_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalArAutoAccounting create(Integer AR_AA_CODE, String AA_ACCNT_TYP, Integer AA_AD_CMPNY)
			throws CreateException {
		try {

			LocalArAutoAccounting entity = new LocalArAutoAccounting();

			Debug.print("ArAutoAccountingBean create");
			entity.setAaCode(AR_AA_CODE);
			entity.setAaAccountType(AA_ACCNT_TYP);
			entity.setAaAdCompany(AA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArAutoAccounting create(String AA_ACCNT_TYP, Integer AA_AD_CMPNY) throws CreateException {
		try {

			LocalArAutoAccounting entity = new LocalArAutoAccounting();

			Debug.print("ArAutoAccountingBean create");
			entity.setAaAccountType(AA_ACCNT_TYP);
			entity.setAaAdCompany(AA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}