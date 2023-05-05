package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlOrganization;
import com.util.Debug;

@Stateless
public class LocalGlOrganizationHome {

	public static final String JNDI_NAME = "LocalGlOrganizationHome!com.ejb.gl.LocalGlOrganizationHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlOrganizationHome() {
	}

	// FINDER METHODS

	public LocalGlOrganization findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlOrganization entity = (LocalGlOrganization) em
					.find(new LocalGlOrganization(), pk);
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

	public java.util.Collection findByMasterCode(java.lang.Integer MSTR_CODE, java.lang.Integer ORG_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(org) FROM GlOrganization org WHERE org.orgMasterCode = ?1 AND org.orgAdCompany = ?2");
			query.setParameter(1, MSTR_CODE);
			query.setParameter(2, ORG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlOrganizationHome.findByMasterCode(java.lang.Integer MSTR_CODE, java.lang.Integer ORG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findOrgAll(java.lang.Integer ORG_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(org) FROM GlOrganization org WHERE org.orgAdCompany = ?1");
			query.setParameter(1, ORG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlOrganizationHome.findOrgAll(java.lang.Integer ORG_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlOrganization findByOrgName(java.lang.String ORG_NM, java.lang.Integer ORG_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(org) FROM GlOrganization org WHERE org.orgName=?1 AND org.orgAdCompany = ?2");
			query.setParameter(1, ORG_NM);
			query.setParameter(2, ORG_AD_CMPNY);
            return (LocalGlOrganization) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlOrganizationHome.findByOrgName(java.lang.String ORG_NM, java.lang.Integer ORG_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlOrganizationHome.findByOrgName(java.lang.String ORG_NM, java.lang.Integer ORG_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlOrganization create(java.lang.Integer ORG_CODE, java.lang.String ORG_NM,
                                      java.lang.String ORG_DESC, java.lang.Integer ORG_MSTR_CODE, java.lang.Integer ORG_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlOrganization entity = new LocalGlOrganization();

			Debug.print("GlOrganizationBean create");

			entity.setOrgCode(ORG_CODE);
			entity.setOrgName(ORG_NM);
			entity.setOrgDescription(ORG_DESC);
			entity.setOrgMasterCode(ORG_MSTR_CODE);
			entity.setOrgAdCompany(ORG_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlOrganization create(java.lang.String ORG_NM, java.lang.String ORG_DESC,
                                      java.lang.Integer ORG_MSTR_CODE, java.lang.Integer ORG_AD_CMPNY) throws CreateException {
		try {

			LocalGlOrganization entity = new LocalGlOrganization();

			Debug.print("GlOrganizationBean create");

			entity.setOrgName(ORG_NM);
			entity.setOrgDescription(ORG_DESC);
			entity.setOrgMasterCode(ORG_MSTR_CODE);
			entity.setOrgAdCompany(ORG_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}