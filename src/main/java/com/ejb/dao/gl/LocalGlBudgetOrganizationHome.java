package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlBudgetOrganization;
import com.util.Debug;

@Stateless
public class LocalGlBudgetOrganizationHome {

	public static final String JNDI_NAME = "LocalGlBudgetOrganizationHome!com.ejb.gl.LocalGlBudgetOrganizationHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlBudgetOrganizationHome() {
	}

	// FINDER METHODS

	public LocalGlBudgetOrganization findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlBudgetOrganization entity = (LocalGlBudgetOrganization) em
					.find(new LocalGlBudgetOrganization(), pk);
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

	public java.util.Collection findBoAll(java.lang.Integer BO_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(bo) FROM GlBudgetOrganization bo WHERE bo.boAdCompany = ?1");
			query.setParameter(1, BO_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlBudgetOrganizationHome.findBoAll(java.lang.Integer BO_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlBudgetOrganization findByBoName(java.lang.String BO_NM, java.lang.Integer BO_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bo) FROM GlBudgetOrganization bo WHERE bo.boName = ?1 AND bo.boAdCompany = ?2");
			query.setParameter(1, BO_NM);
			query.setParameter(2, BO_AD_CMPNY);
            return (LocalGlBudgetOrganization) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlBudgetOrganizationHome.findByBoName(java.lang.String BO_NM, java.lang.Integer BO_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlBudgetOrganizationHome.findByBoName(java.lang.String BO_NM, java.lang.Integer BO_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlBudgetOrganization create(java.lang.Integer BO_CODE, java.lang.String BO_NM,
                                            java.lang.String BO_DESC, java.lang.String BO_SGMNT_ORDR, java.lang.String BO_PSSWRD,
                                            java.lang.Integer BO_AD_CMPNY) throws CreateException {
		try {

			LocalGlBudgetOrganization entity = new LocalGlBudgetOrganization();

			Debug.print("GlBudgetOrganization create");

			entity.setBoCode(BO_CODE);
			entity.setBoName(BO_NM);
			entity.setBoDescription(BO_DESC);
			entity.setBoSegmentOrder(BO_SGMNT_ORDR);
			entity.setBoPassword(BO_PSSWRD);
			entity.setBoAdCompany(BO_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlBudgetOrganization create(java.lang.String BO_NM, java.lang.String BO_DESC,
                                            java.lang.String BO_SGMNT_ORDR, java.lang.String BO_PSSWRD, java.lang.Integer BO_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlBudgetOrganization entity = new LocalGlBudgetOrganization();

			Debug.print("GlBudgetOrganization create");

			entity.setBoName(BO_NM);
			entity.setBoDescription(BO_DESC);
			entity.setBoSegmentOrder(BO_SGMNT_ORDR);
			entity.setBoPassword(BO_PSSWRD);
			entity.setBoAdCompany(BO_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}