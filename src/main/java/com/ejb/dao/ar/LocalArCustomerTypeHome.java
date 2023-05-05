package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArCustomerType;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArCustomerTypeHome {

	public static final String JNDI_NAME = "LocalArCustomerTypeHome!com.ejb.ar.LocalArCustomerTypeHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArCustomerTypeHome() {
	}

	// FINDER METHODS

	public LocalArCustomerType findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArCustomerType entity = (LocalArCustomerType) em
					.find(new LocalArCustomerType(), pk);
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

	public java.util.Collection findEnabledCtAll(java.lang.Integer CT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ct) FROM ArCustomerType ct WHERE ct.ctEnable = 1 AND ct.ctAdCompany = ?1");
			query.setParameter(1, CT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerTypeHome.findEnabledCtAll(java.lang.Integer CT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findCtAll(java.lang.Integer CT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(ct) FROM ArCustomerType ct WHERE ct.ctAdCompany = ?1");
			query.setParameter(1, CT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerTypeHome.findCtAll(java.lang.Integer CT_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArCustomerType findByCtName(java.lang.String CT_NM, java.lang.Integer CT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ct) FROM ArCustomerType ct WHERE ct.ctName = ?1 AND ct.ctAdCompany = ?2");
			query.setParameter(1, CT_NM);
			query.setParameter(2, CT_AD_CMPNY);
            return (LocalArCustomerType) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArCustomerTypeHome.findByCtName(java.lang.String CT_NM, java.lang.Integer CT_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerTypeHome.findByCtName(java.lang.String CT_NM, java.lang.Integer CT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getCtByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalArCustomerType create(Integer CT_CODE, String CT_NM, String CT_DESC, byte CT_ENBL,
                                      Integer CT_AD_CMPNY) throws CreateException {
		try {

			LocalArCustomerType entity = new LocalArCustomerType();

			Debug.print("ArCustomerTypeBean create");
			entity.setCtCode(CT_CODE);
			entity.setCtName(CT_NM);
			entity.setCtDescription(CT_DESC);
			entity.setCtEnable(CT_ENBL);
			entity.setCtAdCompany(CT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArCustomerType create(String CT_NM, String CT_DESC, byte CT_ENBL, Integer CT_AD_CMPNY)
			throws CreateException {
		try {

			LocalArCustomerType entity = new LocalArCustomerType();

			Debug.print("ArCustomerTypeBean create");
			entity.setCtName(CT_NM);
			entity.setCtDescription(CT_DESC);
			entity.setCtEnable(CT_ENBL);
			entity.setCtAdCompany(CT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}