package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlReportValue;
import com.util.Debug;

@Stateless
public class LocalGlReportValueHome {

	public static final String JNDI_NAME = "LocalGlReportValueHome!com.ejb.gl.LocalGlReportValueHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlReportValueHome() {
	}

	// FINDER METHODS

	public LocalGlReportValue findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlReportValue entity = (LocalGlReportValue) em
					.find(new LocalGlReportValue(), pk);
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

	public java.util.Collection findRvAll(java.lang.Integer PT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(rv) FROM GlReportValue rv WHERE rv.rvAdCompany = ?1");
			query.setParameter(1, PT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlReportValueHome.findRvAll(java.lang.Integer PT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findRvByRvType(java.lang.String RV_TYP, java.lang.Integer PT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rv) FROM GlReportValue rv WHERE rv.rvType = ?1 AND rv.rvAdCompany = ?2");
			query.setParameter(1, RV_TYP);
			query.setParameter(2, PT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlReportValueHome.findRvByRvType(java.lang.String RV_TYP, java.lang.Integer PT_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlReportValue create(java.lang.Integer GL_RV_CODE, java.lang.String RV_TYP,
                                     java.lang.String RV_PRMTR, String RV_DESC, String RV_DFLT_VL, Integer RV_AD_CMPNY) throws CreateException {
		try {

			LocalGlReportValue entity = new LocalGlReportValue();

			Debug.print("GlReportValueBean create");

			entity.setRvCode(GL_RV_CODE);
			entity.setRvType(RV_TYP);
			entity.setRvParameter(RV_PRMTR);
			entity.setRvDescription(RV_DESC);
			entity.setRvDefaultValue(RV_DFLT_VL);
			entity.setRvAdCompany(RV_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlReportValue create(java.lang.String RV_TYP, java.lang.String RV_PRMTR, String RV_DESC,
                                     String RV_DFLT_VL, Integer RV_AD_CMPNY) throws CreateException {
		try {

			LocalGlReportValue entity = new LocalGlReportValue();

			Debug.print("GlReportValueBean create");

			entity.setRvType(RV_TYP);
			entity.setRvParameter(RV_PRMTR);
			entity.setRvDescription(RV_DESC);
			entity.setRvDefaultValue(RV_DFLT_VL);
			entity.setRvAdCompany(RV_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}