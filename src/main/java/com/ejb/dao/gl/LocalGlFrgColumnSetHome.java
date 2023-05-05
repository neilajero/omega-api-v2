package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlFrgColumnSet;
import com.util.Debug;

@Stateless
public class LocalGlFrgColumnSetHome {

	public static final String JNDI_NAME = "LocalGlFrgColumnSetHome!com.ejb.gl.LocalGlFrgColumnSetHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlFrgColumnSetHome() {
	}

	// FINDER METHODS

	public LocalGlFrgColumnSet findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlFrgColumnSet entity = (LocalGlFrgColumnSet) em
					.find(new LocalGlFrgColumnSet(), pk);
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

	public java.util.Collection findCsAll(java.lang.Integer CS_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(cs) FROM GlFrgColumnSet cs WHERE cs.csAdCompany = ?1");
			query.setParameter(1, CS_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgColumnSetHome.findCsAll(java.lang.Integer CS_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlFrgColumnSet findByCsName(java.lang.String CS_NM, java.lang.Integer CS_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(Cs) FROM GlFrgColumnSet cs WHERE cs.csName = ?1 AND cs.csAdCompany = ?2");
			query.setParameter(1, CS_NM);
			query.setParameter(2, CS_AD_CMPNY);
            return (LocalGlFrgColumnSet) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlFrgColumnSetHome.findByCsName(java.lang.String CS_NM, java.lang.Integer CS_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgColumnSetHome.findByCsName(java.lang.String CS_NM, java.lang.Integer CS_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlFrgColumnSet create(Integer CS_CODE, String CS_NM, String CS_DESC, Integer CS_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlFrgColumnSet entity = new LocalGlFrgColumnSet();

			Debug.print("GlFrgColumnSetBean create");

			entity.setCsCode(CS_CODE);
			entity.setCsName(CS_NM);
			entity.setCsDescription(CS_DESC);
			entity.setCsAdCompany(CS_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlFrgColumnSet create(String CS_NM, String CS_DESC, Integer CS_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlFrgColumnSet entity = new LocalGlFrgColumnSet();

			Debug.print("GlFrgColumnSetBean create");

			entity.setCsName(CS_NM);
			entity.setCsDescription(CS_DESC);
			entity.setCsAdCompany(CS_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}