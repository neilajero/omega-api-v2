package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArPersonelType;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArPersonelTypeHome {

	public static final String JNDI_NAME = "LocalArPersonelTypeHome!com.ejb.ar.LocalArPersonelTypeHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArPersonelTypeHome() {
	}

	// FINDER METHODS

	public LocalArPersonelType findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArPersonelType entity = (LocalArPersonelType) em
					.find(new LocalArPersonelType(), pk);
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

	public java.util.Collection findPtAll(java.lang.Integer PT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(pt) FROM ArPersonelType pt WHERE pt.ptAdCompany = ?1");
			query.setParameter(1, PT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArPersonelTypeHome.findPtAll(java.lang.Integer PT_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArPersonelType findByPtShortName(java.lang.String PT_NM, java.lang.Integer PT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pt) FROM ArPersonelType pt WHERE pt.ptName = ?1 AND pt.ptAdCompany = ?2");
			query.setParameter(1, PT_NM);
			query.setParameter(2, PT_AD_CMPNY);
            return (LocalArPersonelType) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArPersonelTypeHome.findByPtShortName(java.lang.String PT_NM, java.lang.Integer PT_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArPersonelTypeHome.findByPtShortName(java.lang.String PT_NM, java.lang.Integer PT_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArPersonelType findByPtName(java.lang.String PT_NM, java.lang.Integer PT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pt) FROM ArPersonelType pt WHERE pt.ptName = ?1 AND pt.ptAdCompany = ?2");
			query.setParameter(1, PT_NM);
			query.setParameter(2, PT_AD_CMPNY);
            return (LocalArPersonelType) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArPersonelTypeHome.findByPtName(java.lang.String PT_NM, java.lang.Integer PT_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArPersonelTypeHome.findByPtName(java.lang.String PT_NM, java.lang.Integer PT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getPtByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalArPersonelType create(Integer AR_PT_CODE, String PT_SHRT_NM, String PT_NM, String PT_DESC,
                                      double PT_RT, Integer PT_AD_CMPNY) throws CreateException {
		try {

			LocalArPersonelType entity = new LocalArPersonelType();

			Debug.print("ArPersonelTypeBean create");

			entity.setPtCode(AR_PT_CODE);
			entity.setPtShortName(PT_SHRT_NM);
			entity.setPtName(PT_NM);
			entity.setPtDescription(PT_DESC);
			entity.setPtRate(PT_RT);
			entity.setPtAdCompany(PT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArPersonelType create(String PT_SHRT_NM, String PT_NM, String PT_DESC, double PT_RT,
                                      Integer PT_AD_CMPNY) throws CreateException {
		try {

			LocalArPersonelType entity = new LocalArPersonelType();

			Debug.print("ArCustomerTypeBean create");

			entity.setPtShortName(PT_SHRT_NM);
			entity.setPtName(PT_NM);
			entity.setPtDescription(PT_DESC);
			entity.setPtRate(PT_RT);
			entity.setPtAdCompany(PT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}