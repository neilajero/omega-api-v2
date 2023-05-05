package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdBranchResponsibilityHome {

	public static final String JNDI_NAME = "LocalAdBranchResponsibilityHome!com.ejb.ad.LocalAdBranchResponsibilityHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdBranchResponsibilityHome() {
	}

	// FINDER METHODS

	public LocalAdBranchResponsibility findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdBranchResponsibility entity = (LocalAdBranchResponsibility) em
					.find(new LocalAdBranchResponsibility(), pk);
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

	public java.util.Collection findBrsAll(java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(brs) FROM AdBranchResponsibility brs WHERE brs.brsAdCompany = ?1");
			query.setParameter(1, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchResponsibilityHome.findBrsAll(java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAdResponsibility(java.lang.Integer AD_RS_CODE, java.lang.Integer BRS_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(brs) FROM AdBranchResponsibility brs WHERE brs.adResponsibility.rsCode = ?1 AND brs.brsAdCompany = ?2");
			query.setParameter(1, AD_RS_CODE);
			query.setParameter(2, BRS_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchResponsibilityHome.findByAdResponsibility(java.lang.Integer AD_RS_CODE, java.lang.Integer BRS_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalAdBranchResponsibility create(Integer BRS_CODE, Integer BRS_AD_CMPNY)
			throws CreateException {
		try {

			LocalAdBranchResponsibility entity = new LocalAdBranchResponsibility();

			Debug.print("AdBranchResponsibilityBean create");
			entity.setBrsCode(BRS_CODE);
			entity.setBrsAdCompany(BRS_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdBranchResponsibility create(Integer BRS_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchResponsibility entity = new LocalAdBranchResponsibility();

			Debug.print("AdBranchResponsibilityBean create");
			entity.setBrsAdCompany(BRS_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}

	}

}