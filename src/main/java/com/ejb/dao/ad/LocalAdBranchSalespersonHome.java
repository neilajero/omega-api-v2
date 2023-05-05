package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdBranchSalesperson;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdBranchSalespersonHome {

	public static final String JNDI_NAME = "LocalAdBranchSalespersonHome!com.ejb.ad.LocalAdBranchSalespersonHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdBranchSalespersonHome() {
	}

	// FINDER METHODS

	public LocalAdBranchSalesperson findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdBranchSalesperson entity = (LocalAdBranchSalesperson) em
					.find(new LocalAdBranchSalesperson(), pk);
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

	public java.util.Collection findBSLPBySLPCodeAndRsName(java.lang.Integer SLP_CODE, java.lang.String RS_NM,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bslp) FROM AdBranchSalesperson bslp, IN(bslp.adBranch.adBranchResponsibilities) brs WHERE bslp.arSalesperson.slpCode = ?1 AND brs.adResponsibility.rsName = ?2 AND bslp.bslpAdCompany = ?3");
			query.setParameter(1, SLP_CODE);
			query.setParameter(2, RS_NM);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchSalespersonHome.findBSLPBySLPCodeAndRsName(java.lang.Integer SLP_CODE, java.lang.String RS_NM, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBranchSalesperson findBySlpCodeAndBrCode(java.lang.Integer SLP_CODE, java.lang.Integer AD_BRNCH,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bslp) FROM AdBranchSalesperson bslp WHERE bslp.arSalesperson.slpCode = ?1 AND bslp.adBranch.brCode = ?2 AND bslp.bslpAdCompany = ?3");
			query.setParameter(1, SLP_CODE);
			query.setParameter(2, AD_BRNCH);
			query.setParameter(3, AD_CMPNY);
            return (LocalAdBranchSalesperson) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdBranchSalespersonHome.findBySlpCodeAndBrCode(java.lang.Integer SLP_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchSalespersonHome.findBySlpCodeAndBrCode(java.lang.Integer SLP_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalAdBranchSalesperson create(Integer BSLP_CODE, Integer BSLP_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchSalesperson entity = new LocalAdBranchSalesperson();

			Debug.print("AdBranchSalespersonBean create");
			entity.setBslpCode(BSLP_CODE);
			entity.setBslpAdCompany(BSLP_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdBranchSalesperson create(Integer BSLP_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchSalesperson entity = new LocalAdBranchSalesperson();

			Debug.print("AdBranchSalespersonBean create");
			entity.setBslpAdCompany(BSLP_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}