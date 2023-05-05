package com.ejb.dao.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApSupplierType;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalApSupplierTypeHome {

	public static final String JNDI_NAME = "LocalApSupplierTypeHome!com.ejb.ap.LocalApSupplierTypeHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalApSupplierTypeHome() {
	}

	// FINDER METHODS

	public LocalApSupplierType findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalApSupplierType entity = (LocalApSupplierType) em
					.find(new LocalApSupplierType(), pk);
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

	public java.util.Collection findEnabledStAll(java.lang.Integer ST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(st) FROM ApSupplierType st WHERE st.stEnable = 1 AND st.stAdCompany = ?1");
			query.setParameter(1, ST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierTypeHome.findEnabledStAll(java.lang.Integer ST_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalApSupplierType findByStName(java.lang.String ST_NM, java.lang.Integer ST_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(st) FROM ApSupplierType st WHERE st.stName = ?1 AND st.stAdCompany = ?2");
			query.setParameter(1, ST_NM);
			query.setParameter(2, ST_AD_CMPNY);
            return (LocalApSupplierType) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApSupplierTypeHome.findByStName(java.lang.String ST_NM, java.lang.Integer ST_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierTypeHome.findByStName(java.lang.String ST_NM, java.lang.Integer ST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findStAll(java.lang.Integer ST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(st) FROM ApSupplierType st WHERE st.stAdCompany = ?1");
			query.setParameter(1, ST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierTypeHome.findStAll(java.lang.Integer ST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getStByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalApSupplierType create(Integer AP_ST_CODE, String ST_NM, String ST_DEST, byte ST_ENBL,
                                      Integer ST_AD_CMPNY) throws CreateException {
		try {

			LocalApSupplierType entity = new LocalApSupplierType();

			Debug.print("ApSupplierTypeBean create");
			entity.setStCode(AP_ST_CODE);
			entity.setStName(ST_NM);
			entity.setStDescription(ST_DEST);
			entity.setStEnable(ST_ENBL);
			entity.setStAdCompany(ST_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalApSupplierType create(String ST_NM, String ST_DEST, byte ST_ENBL, Integer ST_AD_CMPNY)
			throws CreateException {
		try {

			LocalApSupplierType entity = new LocalApSupplierType();

			Debug.print("ApSupplierTypeBean create");
			entity.setStName(ST_NM);
			entity.setStDescription(ST_DEST);
			entity.setStEnable(ST_ENBL);
			entity.setStAdCompany(ST_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}