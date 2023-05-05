package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlFrgFinancialReport;
import com.util.Debug;

@Stateless
public class LocalGlFrgFinancialReportHome {

	public static final String JNDI_NAME = "LocalGlFrgFinancialReportHome!com.ejb.gl.LocalGlFrgFinancialReportHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlFrgFinancialReportHome() {
	}

	// FINDER METHODS

	public LocalGlFrgFinancialReport findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlFrgFinancialReport entity = (LocalGlFrgFinancialReport) em
					.find(new LocalGlFrgFinancialReport(), pk);
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

	public LocalGlFrgFinancialReport findByFrName(java.lang.String FR_NM, java.lang.Integer FR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(fr) FROM GlFrgFinancialReport fr WHERE fr.frName = ?1 AND fr.frAdCompany = ?2");
			query.setParameter(1, FR_NM);
			query.setParameter(2, FR_AD_CMPNY);
            return (LocalGlFrgFinancialReport) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlFrgFinancialReportHome.findByFrName(java.lang.String FR_NM, java.lang.Integer FR_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgFinancialReportHome.findByFrName(java.lang.String FR_NM, java.lang.Integer FR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findFrAll(java.lang.Integer FR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(fr) FROM GlFrgFinancialReport fr WHERE fr.frAdCompany = ?1");
			query.setParameter(1, FR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgFinancialReportHome.findFrAll(java.lang.Integer FR_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlFrgFinancialReport create(Integer FR_CODE, String FR_NM, String FR_DESC, String FR_TTLE,
                                            int FR_FNT_SZ, String FR_FNT_STYL, String FR_HRZNTL_ALGN, Integer FR_AD_CMPNY) throws CreateException {
		try {

			LocalGlFrgFinancialReport entity = new LocalGlFrgFinancialReport();

			Debug.print("GlFrgFinancialReportBean create");

			entity.setFrCode(FR_CODE);
			entity.setFrName(FR_NM);
			entity.setFrDescription(FR_DESC);
			entity.setFrTitle(FR_TTLE);
			entity.setFrFontSize(FR_FNT_SZ);
			entity.setFrFontStyle(FR_FNT_STYL);
			entity.setFrHorizontalAlign(FR_HRZNTL_ALGN);
			entity.setFrAdCompany(FR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlFrgFinancialReport create(String FR_NM, String FR_DESC, String FR_TTLE, int FR_FNT_SZ,
                                            String FR_FNT_STYL, String FR_HRZNTL_ALGN, Integer FR_AD_CMPNY) throws CreateException {
		try {

			LocalGlFrgFinancialReport entity = new LocalGlFrgFinancialReport();

			Debug.print("GlFrgFinancialReportBean create");

			entity.setFrName(FR_NM);
			entity.setFrDescription(FR_DESC);
			entity.setFrTitle(FR_TTLE);
			entity.setFrFontSize(FR_FNT_SZ);
			entity.setFrFontStyle(FR_FNT_STYL);
			entity.setFrHorizontalAlign(FR_HRZNTL_ALGN);
			entity.setFrAdCompany(FR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}