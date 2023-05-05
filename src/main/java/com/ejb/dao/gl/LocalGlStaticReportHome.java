package com.ejb.dao.gl;

import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlStaticReport;
import com.util.Debug;

@Stateless
public class LocalGlStaticReportHome {

	public static final String JNDI_NAME = "LocalGlStaticReportHome!com.ejb.gl.LocalGlStaticReportHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlStaticReportHome() {
	}

	// FINDER METHODS

	public LocalGlStaticReport findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlStaticReport entity = (LocalGlStaticReport) em
					.find(new LocalGlStaticReport(), pk);
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

	public java.util.Collection findSrAll(java.lang.Integer SR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(sr) FROM GlStaticReport sr WHERE sr.srAdCompany = ?1");
			query.setParameter(1, SR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlStaticReportHome.findSrAll(java.lang.Integer SR_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlStaticReport findBySrName(java.lang.String SR_NM, java.lang.Integer SR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sr) FROM GlStaticReport sr WHERE sr.srName = ?1 AND sr.srAdCompany = ?2");
			query.setParameter(1, SR_NM);
			query.setParameter(2, SR_AD_CMPNY);
            return (LocalGlStaticReport) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlStaticReportHome.findBySrName(java.lang.String SR_NM, java.lang.Integer SR_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlStaticReportHome.findBySrName(java.lang.String SR_NM, java.lang.Integer SR_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlStaticReport create(Integer SR_CODE, String SR_NM, String SR_DESC, String SR_FL_NM,
                                      Date SR_DT_FRM, Date SR_DT_TO, Integer SR_AD_CMPNY) throws CreateException {
		try {

			LocalGlStaticReport entity = new LocalGlStaticReport();

			Debug.print("GlStaticReportBean create");

			entity.setSrCode(SR_CODE);
			entity.setSrName(SR_NM);
			entity.setSrDescription(SR_DESC);
			entity.setSrFileName(SR_FL_NM);
			entity.setSrDateFrom(SR_DT_FRM);
			entity.setSrDateTo(SR_DT_TO);
			entity.setSrAdCompany(SR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlStaticReport create(String SR_NM, String SR_DESC, String SR_FL_NM, Date SR_DT_FRM,
                                      Date SR_DT_TO, Integer SR_AD_CMPNY) throws CreateException {
		try {

			LocalGlStaticReport entity = new LocalGlStaticReport();

			Debug.print("GlStaticReportBean create");

			entity.setSrName(SR_NM);
			entity.setSrDescription(SR_DESC);
			entity.setSrFileName(SR_FL_NM);
			entity.setSrDateFrom(SR_DT_FRM);
			entity.setSrDateTo(SR_DT_TO);
			entity.setSrAdCompany(SR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}