package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArTaxCode;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArTaxCodeHome {

	public static final String JNDI_NAME = "LocalArTaxCodeHome!com.ejb.ar.LocalArTaxCodeHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArTaxCodeHome() {
	}

	// FINDER METHODS

	public LocalArTaxCode findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArTaxCode entity = (LocalArTaxCode) em.find(new LocalArTaxCode(), pk);
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

	public java.util.Collection findTcAll(java.lang.Integer TC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(tc) FROM ArTaxCode tc WHERE tc.tcAdCompany = ?1");
			query.setParameter(1, TC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArTaxCodeHome.findTcAll(java.lang.Integer TC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findEnabledTcAll(java.lang.Integer TC_AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(tc) FROM ArTaxCode tc WHERE tc.tcEnable = 1 AND tc.tcAdCompany = ?1");
			query.setParameter(1, TC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArTaxCodeHome.findEnabledTcAll(java.lang.Integer TC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArTaxCode findByTcName(java.lang.String TC_NM, java.lang.Integer TC_AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(tc) FROM ArTaxCode tc WHERE tc.tcName = ?1 AND tc.tcAdCompany = ?2");
			query.setParameter(1, TC_NM);
			query.setParameter(2, TC_AD_CMPNY);
            return (LocalArTaxCode) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArTaxCodeHome.findByTcName(java.lang.String TC_NM, java.lang.Integer TC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArTaxCodeHome.findByTcName(java.lang.String TC_NM, java.lang.Integer TC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArTaxCode findByTcName(java.lang.String TC_NM, Integer companyCode, String companyShortName) throws FinderException {
		Debug.print("LocalArTaxCodeHome findByTcName");
		try {
			Query query = em.createQueryPerCompany("SELECT OBJECT(tc) FROM ArTaxCode tc WHERE tc.tcName = ?1 AND tc.tcAdCompany = ?2", companyShortName);
			query.setParameter(1, TC_NM);
			query.setParameter(2, companyCode);
			return (LocalArTaxCode) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection getTcByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalArTaxCode create(Integer AR_TC_CODE, String TC_NM, String TC_DESC, String TC_TYP,
                                 Integer TC_INTRM_ACCNT, double TC_RT, byte TC_ENBL, Integer TC_AD_CMPNY) throws CreateException {
		try {

			LocalArTaxCode entity = new LocalArTaxCode();

			Debug.print("ArTaxCodeBean create");

			entity.setTcCode(AR_TC_CODE);
			entity.setTcName(TC_NM);
			entity.setTcDescription(TC_DESC);
			entity.setTcType(TC_TYP);
			entity.setTcInterimAccount(TC_INTRM_ACCNT);
			entity.setTcRate(TC_RT);
			entity.setTcEnable(TC_ENBL);
			entity.setTcAdCompany(TC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArTaxCode create(String TC_NM, String TC_DESC, String TC_TYP, Integer TC_INTRM_ACCNT,
                                 double TC_RT, byte TC_ENBL, Integer TC_AD_CMPNY) throws CreateException {
		try {

			LocalArTaxCode entity = new LocalArTaxCode();

			Debug.print("ArTaxCodeBean create");

			entity.setTcName(TC_NM);
			entity.setTcDescription(TC_DESC);
			entity.setTcType(TC_TYP);
			entity.setTcInterimAccount(TC_INTRM_ACCNT);
			entity.setTcRate(TC_RT);
			entity.setTcEnable(TC_ENBL);
			entity.setTcAdCompany(TC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}