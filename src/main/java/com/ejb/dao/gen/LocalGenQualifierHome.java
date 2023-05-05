package com.ejb.dao.gen;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gen.LocalGenQualifier;
import com.util.Debug;

@Stateless
public class LocalGenQualifierHome {

	public static final String JNDI_NAME = "LocalGenQualifierHome!com.ejb.genfld.LocalGenQualifierHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGenQualifierHome() {
	}

	// FINDER METHODS

	public LocalGenQualifier findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGenQualifier entity = (LocalGenQualifier) em
					.find(new LocalGenQualifier(), pk);
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

	public java.util.Collection findQlfrAll(java.lang.Integer QL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(ql) FROM GenQualifier ql WHERE ql.qlAdCompany = ?1");
			query.setParameter(1, QL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenQualifierHome.findQlfrAll(java.lang.Integer QL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGenQualifier findByQlAccountType(java.lang.String QL_ACCNT_TYP, java.lang.Integer QL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ql) FROM GenQualifier ql WHERE ql.qlAccountType = ?1 AND ql.qlAdCompany = ?2");
			query.setParameter(1, QL_ACCNT_TYP);
			query.setParameter(2, QL_AD_CMPNY);
            return (LocalGenQualifier) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.genfld.LocalGenQualifierHome.findByQlAccountType(java.lang.String QL_ACCNT_TYP, java.lang.Integer QL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenQualifierHome.findByQlAccountType(java.lang.String QL_ACCNT_TYP, java.lang.Integer QL_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGenQualifier create(java.lang.Integer QL_CODE, java.lang.String QL_ACCNT_TYP,
                                    byte QL_BDGTNG_ALLWD, byte QL_PSTNG_ALLWD, Integer QL_AD_CMPNY) throws CreateException {
		try {

			LocalGenQualifier entity = new LocalGenQualifier();

			Debug.print("GenQlfrBean create");

			entity.setQlCode(QL_CODE);
			entity.setQlAccountType(QL_ACCNT_TYP);
			entity.setQlBudgetingAllowed(QL_BDGTNG_ALLWD);
			entity.setQlPostingAllowed(QL_PSTNG_ALLWD);
			entity.setQlAdCompany(QL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGenQualifier create(java.lang.String QL_ACCNT_TYP, byte QL_BDGTNG_ALLWD,
                                    byte QL_PSTNG_ALLWD, Integer QL_AD_CMPNY) throws CreateException {
		try {

			LocalGenQualifier entity = new LocalGenQualifier();

			Debug.print("GenQlfrBean create");

			entity.setQlAccountType(QL_ACCNT_TYP);
			entity.setQlBudgetingAllowed(QL_BDGTNG_ALLWD);
			entity.setQlPostingAllowed(QL_PSTNG_ALLWD);
			entity.setQlAdCompany(QL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}