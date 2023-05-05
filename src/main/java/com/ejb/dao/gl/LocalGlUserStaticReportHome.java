package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlUserStaticReport;
import com.util.Debug;

@Stateless
public class LocalGlUserStaticReportHome {

	public static final String JNDI_NAME = "LocalGlUserStaticReportHome!com.ejb.gl.LocalGlUserStaticReportHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlUserStaticReportHome() {
	}

	// FINDER METHODS

	public LocalGlUserStaticReport findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlUserStaticReport entity = (LocalGlUserStaticReport) em
					.find(new LocalGlUserStaticReport(), pk);
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

	public java.util.Collection findBySrCode(java.lang.Integer SR_CODE, java.lang.Integer USTR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ustr) FROM GlUserStaticReport ustr WHERE ustr.glStaticReport.srCode = ?1 AND ustr.ustrAdCompany = ?2");
			query.setParameter(1, SR_CODE);
			query.setParameter(2, USTR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlUserStaticReportHome.findBySrCode(java.lang.Integer SR_CODE, java.lang.Integer USTR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByUsrCode(java.lang.Integer USR_CODE, java.lang.Integer USTR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ustr) FROM GlUserStaticReport ustr WHERE ustr.adUser.usrCode = ?1 AND ustr.ustrAdCompany = ?2");
			query.setParameter(1, USR_CODE);
			query.setParameter(2, USTR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlUserStaticReportHome.findByUsrCode(java.lang.Integer USR_CODE, java.lang.Integer USTR_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlUserStaticReport findBySrCodeAndUsrCode(java.lang.Integer SR_CODE, java.lang.Integer USR_NM,
			java.lang.Integer USTR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ustr) FROM GlUserStaticReport ustr WHERE ustr.glStaticReport.srCode=?1 AND ustr.adUser.usrCode=?2 AND ustr.ustrAdCompany = ?3");
			query.setParameter(1, SR_CODE);
			query.setParameter(2, USR_NM);
			query.setParameter(3, USTR_AD_CMPNY);
            return (LocalGlUserStaticReport) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlUserStaticReportHome.findBySrCodeAndUsrCode(java.lang.Integer SR_CODE, java.lang.Integer USR_NM, java.lang.Integer USTR_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlUserStaticReportHome.findBySrCodeAndUsrCode(java.lang.Integer SR_CODE, java.lang.Integer USR_NM, java.lang.Integer USTR_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlUserStaticReport create(java.lang.Integer USTR_AD_CMNY) throws CreateException {
		try {

			LocalGlUserStaticReport entity = new LocalGlUserStaticReport();

			Debug.print("GlStaticReportBean create");
			entity.setUstrAdCompany(USTR_AD_CMNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}