package com.ejb.dao.gl;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlPeriodType;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalGlPeriodTypeHome {

	public static final String JNDI_NAME = "LocalGlPeriodTypeHome!com.ejb.gl.LocalGlPeriodTypeHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlPeriodTypeHome() {
	}

	// FINDER METHODS

	public LocalGlPeriodType findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlPeriodType entity = (LocalGlPeriodType) em
					.find(new LocalGlPeriodType(), pk);
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
			Query query = em.createQuery("SELECT OBJECT(pt) FROM GlPeriodType pt WHERE pt.ptAdCompany = ?1");
			query.setParameter(1, PT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlPeriodTypeHome.findPtAll(java.lang.Integer PT_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlPeriodType findByPtName(java.lang.String PT_NM, java.lang.Integer PT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(pt) FROM GlPeriodType pt WHERE pt.ptName=?1 AND pt.ptAdCompany = ?2");
			query.setParameter(1, PT_NM);
			query.setParameter(2, PT_AD_CMPNY);
            return (LocalGlPeriodType) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlPeriodTypeHome.findByPtName(java.lang.String PT_NM, java.lang.Integer PT_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlPeriodTypeHome.findByPtName(java.lang.String PT_NM, java.lang.Integer PT_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlPeriodType create(java.lang.Integer GL_PT_CODE, java.lang.String PT_NM,
                                    java.lang.String PT_DESC, short PT_PRD_PER_YR, char PT_YR_TYP, Integer PT_AD_CMPNY) throws CreateException {
		try {

			LocalGlPeriodType entity = new LocalGlPeriodType();

			Debug.print("GlPeriodTypeBean create");

			entity.setPtCode(GL_PT_CODE);
			entity.setPtName(PT_NM);
			entity.setPtDescription(PT_DESC);
			entity.setPtPeriodPerYear(PT_PRD_PER_YR);
			entity.setPtYearType(PT_YR_TYP);
			entity.setPtAdCompany(PT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlPeriodType create(java.lang.String PT_NM, java.lang.String PT_DESC, short PT_PRD_PER_YR,
                                    char PT_YR_TYP, Integer PT_AD_CMPNY) throws CreateException {
		try {

			LocalGlPeriodType entity = new LocalGlPeriodType();

			Debug.print("GlPeriodTypeBean create");

			entity.setPtName(PT_NM);
			entity.setPtDescription(PT_DESC);
			entity.setPtPeriodPerYear(PT_PRD_PER_YR);
			entity.setPtYearType(PT_YR_TYP);
			entity.setPtAdCompany(PT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}