package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlBudget;
import com.util.Debug;

@Stateless
public class LocalGlBudgetHome {

	public static final String JNDI_NAME = "LocalGlBudgetHome!com.ejb.gl.LocalGlBudgetHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlBudgetHome() {
	}

	// FINDER METHODS

	public LocalGlBudget findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlBudget entity = (LocalGlBudget) em.find(new LocalGlBudget(), pk);
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

	public java.util.Collection findBgtAll(java.lang.Integer BGT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(bgt) FROM GlBudget bgt WHERE bgt.bgtAdCompany = ?1");
			query.setParameter(1, BGT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlBudgetHome.findBgtAll(java.lang.Integer BGT_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlBudget findByBgtName(java.lang.String BGT_NM, java.lang.Integer BGT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bgt) FROM GlBudget bgt WHERE bgt.bgtName = ?1 AND bgt.bgtAdCompany = ?2");
			query.setParameter(1, BGT_NM);
			query.setParameter(2, BGT_AD_CMPNY);
            return (LocalGlBudget) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlBudgetHome.findByBgtName(java.lang.String BGT_NM, java.lang.Integer BGT_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlBudgetHome.findByBgtName(java.lang.String BGT_NM, java.lang.Integer BGT_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlBudget findByBgtStatus(java.lang.String BGT_STATUS, java.lang.Integer BGT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bgt) FROM GlBudget bgt WHERE bgt.bgtStatus = ?1 AND bgt.bgtAdCompany = ?2");
			query.setParameter(1, BGT_STATUS);
			query.setParameter(2, BGT_AD_CMPNY);
            return (LocalGlBudget) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlBudgetHome.findByBgtStatus(java.lang.String BGT_STATUS, java.lang.Integer BGT_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlBudgetHome.findByBgtStatus(java.lang.String BGT_STATUS, java.lang.Integer BGT_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlBudget create(java.lang.Integer BGT_CODE, java.lang.String BGT_NM,
                                java.lang.String BGT_DESC, java.lang.String BGT_STATUS, java.lang.String BGT_FRST_PRD,
                                java.lang.String BGT_LST_PRD, java.lang.Integer BGT_AD_CMPNY) throws CreateException {
		try {

			LocalGlBudget entity = new LocalGlBudget();

			Debug.print("GlBudget create");

			entity.setBgtCode(BGT_CODE);
			entity.setBgtName(BGT_NM);
			entity.setBgtDescription(BGT_DESC);
			entity.setBgtStatus(BGT_STATUS);
			entity.setBgtFirstPeriod(BGT_FRST_PRD);
			entity.setBgtLastPeriod(BGT_LST_PRD);
			entity.setBgtAdCompany(BGT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlBudget create(java.lang.String BGT_NM, java.lang.String BGT_DESC,
                                java.lang.String BGT_STATUS, java.lang.String BGT_FRST_PRD, java.lang.String BGT_LST_PRD,
                                java.lang.Integer BGT_AD_CMPNY) throws CreateException {
		try {

			LocalGlBudget entity = new LocalGlBudget();

			Debug.print("GlBudget create");

			entity.setBgtName(BGT_NM);
			entity.setBgtDescription(BGT_DESC);
			entity.setBgtStatus(BGT_STATUS);
			entity.setBgtFirstPeriod(BGT_FRST_PRD);
			entity.setBgtLastPeriod(BGT_LST_PRD);
			entity.setBgtAdCompany(BGT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}