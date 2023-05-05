package com.ejb.dao.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApTaxCode;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalApTaxCodeHome {

	public static final String JNDI_NAME = "LocalApTaxCodeHome!com.ejb.ap.LocalApTaxCodeHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalApTaxCodeHome() {
	}

	// FINDER METHODS

	public LocalApTaxCode findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalApTaxCode entity = (LocalApTaxCode) em.find(new LocalApTaxCode(), pk);
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
			Query query = em.createQuery("SELECT OBJECT(tc) FROM ApTaxCode tc WHERE tc.tcAdCompany = ?1");
			query.setParameter(1, TC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApTaxCodeHome.findTcAll(java.lang.Integer TC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findNoneTc(java.lang.Integer TC_AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(tc) FROM ApTaxCode tc WHERE tc.tcType='NONE' AND tc.tcAdCompany = ?1");
			query.setParameter(1, TC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApTaxCodeHome.findNoneTc(java.lang.Integer TC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findEnabledTcAll(java.lang.Integer TC_AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(tc) FROM ApTaxCode tc WHERE tc.tcEnable = 1 AND tc.tcAdCompany = ?1");
			query.setParameter(1, TC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApTaxCodeHome.findEnabledTcAll(java.lang.Integer TC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalApTaxCode findByTcName(java.lang.String TC_NM, java.lang.Integer TC_AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(tc) FROM ApTaxCode tc WHERE tc.tcName = ?1 AND tc.tcAdCompany = ?2");
			query.setParameter(1, TC_NM);
			query.setParameter(2, TC_AD_CMPNY);
            return (LocalApTaxCode) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApTaxCodeHome.findByTcName(java.lang.String TC_NM, java.lang.Integer TC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApTaxCodeHome.findByTcName(java.lang.String TC_NM, java.lang.Integer TC_AD_CMPNY)");
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

	public LocalApTaxCode create(Integer AP_TC_CODE, String TC_NM, String TC_DESC, String TC_TYP, double TC_RT,
                                 byte TC_ENBL, Integer TC_AD_CMPNY) throws CreateException {
		try {

			LocalApTaxCode entity = new LocalApTaxCode();

			Debug.print("ApTaxCodeBean create");
			entity.setTcCode(AP_TC_CODE);
			entity.setTcName(TC_NM);
			entity.setTcDescription(TC_DESC);
			entity.setTcType(TC_TYP);
			entity.setTcRate(TC_RT);
			entity.setTcEnable(TC_ENBL);
			entity.setTcAdCompany(TC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalApTaxCode create(String TC_NM, String TC_DESC, String TC_TYP, double TC_RT, byte TC_ENBL,
                                 Integer TC_AD_CMPNY) throws CreateException {
		try {

			LocalApTaxCode entity = new LocalApTaxCode();

			Debug.print("ApTaxCodeBean create");
			entity.setTcName(TC_NM);
			entity.setTcDescription(TC_DESC);
			entity.setTcType(TC_TYP);
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