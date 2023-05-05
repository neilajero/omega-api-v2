package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlFrgColumn;
import com.util.Debug;

@Stateless
public class LocalGlFrgColumnHome {

	public static final String JNDI_NAME = "LocalGlFrgColumnHome!com.ejb.gl.LocalGlFrgColumnHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlFrgColumnHome() {
	}

	// FINDER METHODS

	public LocalGlFrgColumn findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlFrgColumn entity = (LocalGlFrgColumn) em
					.find(new LocalGlFrgColumn(), pk);
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

	public java.util.Collection findByFrName(java.lang.String FR_NM, java.lang.Integer COL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(col) FROM GlFrgColumnSet cs, IN(cs.glFrgColumns) col, IN(cs.glFrgFinancialReports) fr WHERE fr.frName = ?1 AND col.colAdCompany = ?2");
			query.setParameter(1, FR_NM);
			query.setParameter(2, COL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgColumnHome.findByFrName(java.lang.String FR_NM, java.lang.Integer COL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByCsName(java.lang.String CS_NM, java.lang.Integer COL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(col) FROM GlFrgColumnSet cs, IN(cs.glFrgColumns) col WHERE cs.csName = ?1 AND col.colAdCompany = ?2");
			query.setParameter(1, CS_NM);
			query.setParameter(2, COL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgColumnHome.findByCsName(java.lang.String CS_NM, java.lang.Integer COL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlFrgColumn findByColNameAndCsName(java.lang.String COL_NM, java.lang.String CS_NM,
			java.lang.Integer COL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(col) FROM GlFrgColumnSet cs, IN(cs.glFrgColumns) col WHERE col.colName = ?1 AND cs.csName = ?2 AND col.colAdCompany = ?3");
			query.setParameter(1, COL_NM);
			query.setParameter(2, CS_NM);
			query.setParameter(3, COL_AD_CMPNY);
            return (LocalGlFrgColumn) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlFrgColumnHome.findByColNameAndCsName(java.lang.String COL_NM, java.lang.String CS_NM, java.lang.Integer COL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgColumnHome.findByColNameAndCsName(java.lang.String COL_NM, java.lang.String CS_NM, java.lang.Integer COL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlFrgColumn findByColumnNameAndCsCode(java.lang.String COL_NM, java.lang.Integer CS_CODE,
			java.lang.Integer COL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(col) FROM GlFrgColumnSet cs, IN(cs.glFrgColumns) col WHERE col.colName=?1 AND cs.csCode=?2 AND col.colAdCompany = ?3");
			query.setParameter(1, COL_NM);
			query.setParameter(2, CS_CODE);
			query.setParameter(3, COL_AD_CMPNY);
            return (LocalGlFrgColumn) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlFrgColumnHome.findByColumnNameAndCsCode(java.lang.String COL_NM, java.lang.Integer CS_CODE, java.lang.Integer COL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgColumnHome.findByColumnNameAndCsCode(java.lang.String COL_NM, java.lang.Integer CS_CODE, java.lang.Integer COL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlFrgColumn findBySequenceNumberAndCsCode(int COL_SQNC_NMBR, java.lang.Integer CS_CODE,
			java.lang.Integer COL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(col) FROM GlFrgColumnSet cs, IN(cs.glFrgColumns) col WHERE col.colSequenceNumber=?1 AND cs.csCode=?2 AND col.colAdCompany = ?3");
			query.setParameter(1, COL_SQNC_NMBR);
			query.setParameter(2, CS_CODE);
			query.setParameter(3, COL_AD_CMPNY);
            return (LocalGlFrgColumn) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlFrgColumnHome.findBySequenceNumberAndCsCode(int COL_SQNC_NMBR, java.lang.Integer CS_CODE, java.lang.Integer COL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgColumnHome.findBySequenceNumberAndCsCode(int COL_SQNC_NMBR, java.lang.Integer CS_CODE, java.lang.Integer COL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByCsCode(java.lang.Integer CS_CODE, java.lang.Integer COL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(col) FROM GlFrgColumnSet cs, IN(cs.glFrgColumns) col WHERE cs.csCode = ?1 AND col.colAdCompany = ?2");
			query.setParameter(1, CS_CODE);
			query.setParameter(2, COL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgColumnHome.findByCsCode(java.lang.Integer CS_CODE, java.lang.Integer COL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByColumnSetAndColSequenceNumber(java.lang.Integer CS_CODE, int COL_SQNC_NMBR,
			java.lang.Integer COL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(col) FROM GlFrgColumnSet cs, IN(cs.glFrgColumns) col WHERE cs.csCode = ?1 AND col.colSequenceNumber < ?2 AND col.colAdCompany = ?3");
			query.setParameter(1, CS_CODE);
			query.setParameter(2, COL_SQNC_NMBR);
			query.setParameter(3, COL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgColumnHome.findByColumnSetAndColSequenceNumber(java.lang.Integer CS_CODE, int COL_SQNC_NMBR, java.lang.Integer COL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findColumnAll(java.lang.Integer COL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(col) FROM GlFrgColumn col WHERE col.colAdCompany = ?1");
			query.setParameter(1, COL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgColumnHome.findColumnAll(java.lang.Integer COL_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlFrgColumn create(Integer COL_CODE, String COL_NM, int COL_PSTN, int COL_SQNC_NMBR,
                                   String COL_FRMT_MSK, String COL_FCTR, String COL_AMNT_TYP, int COL_OFFST, byte COL_OVRRD_ROW_CALC,
                                   Integer COL_AD_CMPNY) throws CreateException {
		try {

			LocalGlFrgColumn entity = new LocalGlFrgColumn();

			Debug.print("GlFrgColumnBean create");

			entity.setColCode(COL_CODE);
			entity.setColName(COL_NM);
			entity.setColPosition(COL_PSTN);
			entity.setColSequenceNumber(COL_SQNC_NMBR);
			entity.setColFormatMask(COL_FRMT_MSK);
			entity.setColFactor(COL_FCTR);
			entity.setColAmountType(COL_AMNT_TYP);
			entity.setColOffset(COL_OFFST);
			entity.setColOverrideRowCalculation(COL_OVRRD_ROW_CALC);
			entity.setColAdCompany(COL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlFrgColumn create(String COL_NM, int COL_PSTN, int COL_SQNC_NMBR, String COL_FRMT_MSK,
                                   String COL_FCTR, String COL_AMNT_TYP, int COL_OFFST, byte COL_OVRRD_ROW_CALC, Integer COL_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlFrgColumn entity = new LocalGlFrgColumn();

			Debug.print("GlFrgColumnBean create");

			entity.setColName(COL_NM);
			entity.setColPosition(COL_PSTN);
			entity.setColSequenceNumber(COL_SQNC_NMBR);
			entity.setColFormatMask(COL_FRMT_MSK);
			entity.setColFactor(COL_FCTR);
			entity.setColAmountType(COL_AMNT_TYP);
			entity.setColOffset(COL_OFFST);
			entity.setColOverrideRowCalculation(COL_OVRRD_ROW_CALC);
			entity.setColAdCompany(COL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}