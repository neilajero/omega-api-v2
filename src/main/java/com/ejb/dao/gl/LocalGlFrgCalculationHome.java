package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlFrgCalculation;
import com.util.Debug;

@Stateless
public class LocalGlFrgCalculationHome {

	public static final String JNDI_NAME = "LocalGlFrgCalculationHome!com.ejb.gl.LocalGlFrgCalculationHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlFrgCalculationHome() {
	}

	// FINDER METHODS

	public LocalGlFrgCalculation findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlFrgCalculation entity = (LocalGlFrgCalculation) em
					.find(new LocalGlFrgCalculation(), pk);
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

	public java.util.Collection findByRowCodeAndCalType(java.lang.Integer ROW_CODE, java.lang.String CAL_TYP,
			java.lang.Integer CAL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cal) FROM GlFrgRow row, IN(row.glFrgCalculations) cal WHERE row.rowCode = ?1 AND cal.calType = ?2 AND cal.calAdCompany = ?3");
			query.setParameter(1, ROW_CODE);
			query.setParameter(2, CAL_TYP);
			query.setParameter(3, CAL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgCalculationHome.findByRowCodeAndCalType(java.lang.Integer ROW_CODE, java.lang.String CAL_TYP, java.lang.Integer CAL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByColCode(java.lang.Integer ROW_CODE, java.lang.Integer CAL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cal) FROM GlFrgColumn col, IN(col.glFrgCalculations) cal WHERE col.colCode = ?1 AND cal.calAdCompany = ?2");
			query.setParameter(1, ROW_CODE);
			query.setParameter(2, CAL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgCalculationHome.findByColCode(java.lang.Integer ROW_CODE, java.lang.Integer CAL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlFrgCalculation findByRowCodeAndCalSequenceNumberAndCalType(java.lang.Integer ROW_CODE,
			int CAL_SQNC_NMBR, java.lang.String CAL_TYP, java.lang.Integer CAL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cal) FROM GlFrgRow row, IN(row.glFrgCalculations) cal WHERE row.rowCode = ?1 AND cal.calSequenceNumber = ?2 AND cal.calType = ?3 AND cal.calAdCompany = ?4");
			query.setParameter(1, ROW_CODE);
			query.setParameter(2, CAL_SQNC_NMBR);
			query.setParameter(3, CAL_TYP);
			query.setParameter(4, CAL_AD_CMPNY);
            return (LocalGlFrgCalculation) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlFrgCalculationHome.findByRowCodeAndCalSequenceNumberAndCalType(java.lang.Integer ROW_CODE, int CAL_SQNC_NMBR, java.lang.String CAL_TYP, java.lang.Integer CAL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgCalculationHome.findByRowCodeAndCalSequenceNumberAndCalType(java.lang.Integer ROW_CODE, int CAL_SQNC_NMBR, java.lang.String CAL_TYP, java.lang.Integer CAL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlFrgCalculation findByColCodeAndCalSequenceNumber(java.lang.Integer COL_CODE, int CAL_SQNC_NMBR,
			java.lang.Integer CAL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cal) FROM GlFrgColumn col, IN(col.glFrgCalculations) cal WHERE col.colCode = ?1 AND cal.calSequenceNumber = ?2 AND cal.calAdCompany = ?3");
			query.setParameter(1, COL_CODE);
			query.setParameter(2, CAL_SQNC_NMBR);
			query.setParameter(3, CAL_AD_CMPNY);
            return (LocalGlFrgCalculation) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlFrgCalculationHome.findByColCodeAndCalSequenceNumber(java.lang.Integer COL_CODE, int CAL_SQNC_NMBR, java.lang.Integer CAL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgCalculationHome.findByColCodeAndCalSequenceNumber(java.lang.Integer COL_CODE, int CAL_SQNC_NMBR, java.lang.Integer CAL_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlFrgCalculation create(Integer GL_CAL_CODE, int CAL_SQNC_NMBR, String CAL_OPRTR,
                                        String CAL_TYP, double CAL_CNSTNT, String CAL_ROW_COL_NM, int CAL_SQNC_LW, int CAL_SQNC_HGH,
                                        Integer CAL_AD_CMPNY) throws CreateException {
		try {

			LocalGlFrgCalculation entity = new LocalGlFrgCalculation();

			Debug.print("GlFrgCalculationBean create");

			entity.setCalCode(GL_CAL_CODE);
			entity.setCalSequenceNumber(CAL_SQNC_NMBR);
			entity.setCalOperator(CAL_OPRTR);
			entity.setCalType(CAL_TYP);
			entity.setCalConstant(CAL_CNSTNT);
			entity.setCalRowColName(CAL_ROW_COL_NM);
			entity.setCalSequenceLow(CAL_SQNC_LW);
			entity.setCalSequenceHigh(CAL_SQNC_HGH);
			entity.setCalAdCompany(CAL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlFrgCalculation create(int CAL_SQNC_NMBR, String CAL_OPRTR, String CAL_TYP,
                                        double CAL_CNSTNT, String CAL_ROW_COL_NM, int CAL_SQNC_LW, int CAL_SQNC_HGH, Integer CAL_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlFrgCalculation entity = new LocalGlFrgCalculation();

			Debug.print("GlFrgCalculationBean create");

			entity.setCalSequenceNumber(CAL_SQNC_NMBR);
			entity.setCalOperator(CAL_OPRTR);
			entity.setCalType(CAL_TYP);
			entity.setCalConstant(CAL_CNSTNT);
			entity.setCalRowColName(CAL_ROW_COL_NM);
			entity.setCalSequenceLow(CAL_SQNC_LW);
			entity.setCalSequenceHigh(CAL_SQNC_HGH);
			entity.setCalAdCompany(CAL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}