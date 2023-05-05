package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlFrgRow;
import com.util.Debug;

@Stateless
public class LocalGlFrgRowHome {

	public static final String JNDI_NAME = "LocalGlFrgRowHome!com.ejb.gl.LocalGlFrgRowHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlFrgRowHome() {
	}

	// FINDER METHODS

	public LocalGlFrgRow findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlFrgRow entity = (LocalGlFrgRow) em.find(new LocalGlFrgRow(), pk);
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

	public java.util.Collection findByFrName(java.lang.String FR_NM, java.lang.Integer ROW_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(row) FROM GlFrgRowSet rs, IN(rs.glFrgRows) row, IN(rs.glFrgFinancialReports) fr WHERE fr.frName = ?1 AND row.rowAdCompany = ?2");
			query.setParameter(1, FR_NM);
			query.setParameter(2, ROW_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgRowHome.findByFrName(java.lang.String FR_NM, java.lang.Integer ROW_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByRsName(java.lang.String RS_NM, java.lang.Integer ROW_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(row) FROM GlFrgRowSet rs, IN(rs.glFrgRows) row WHERE rs.rsName = ?1 AND row.rowAdCompany = ?2");
			query.setParameter(1, RS_NM);
			query.setParameter(2, ROW_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgRowHome.findByRsName(java.lang.String RS_NM, java.lang.Integer ROW_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlFrgRow findByRowNameAndRsName(java.lang.String ROW_NM, java.lang.String RS_NM,
			java.lang.Integer ROW_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(row) FROM GlFrgRowSet rs, IN(rs.glFrgRows) row WHERE row.rowName = ?1 AND rs.rsName = ?2 AND row.rowAdCompany = ?3");
			query.setParameter(1, ROW_NM);
			query.setParameter(2, RS_NM);
			query.setParameter(3, ROW_AD_CMPNY);
            return (LocalGlFrgRow) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlFrgRowHome.findByRowNameAndRsName(java.lang.String ROW_NM, java.lang.String RS_NM, java.lang.Integer ROW_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgRowHome.findByRowNameAndRsName(java.lang.String ROW_NM, java.lang.String RS_NM, java.lang.Integer ROW_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlFrgRow findByRowNameAndRsCode(java.lang.String ROW_NM, java.lang.Integer GL_RS_CODE,
			java.lang.Integer ROW_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(row) FROM GlFrgRowSet rs, IN(rs.glFrgRows) row WHERE row.rowName=?1 AND rs.rsCode=?2 AND row.rowAdCompany = ?3");
			query.setParameter(1, ROW_NM);
			query.setParameter(2, GL_RS_CODE);
			query.setParameter(3, ROW_AD_CMPNY);
            return (LocalGlFrgRow) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlFrgRowHome.findByRowNameAndRsCode(java.lang.String ROW_NM, java.lang.Integer GL_RS_CODE, java.lang.Integer ROW_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgRowHome.findByRowNameAndRsCode(java.lang.String ROW_NM, java.lang.Integer GL_RS_CODE, java.lang.Integer ROW_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlFrgRow findByRowLineNumberAndRsCode(int ROW_LN_NMBR, java.lang.Integer GL_RS_CODE,
			java.lang.Integer ROW_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(row) FROM GlFrgRowSet rs, IN(rs.glFrgRows) row WHERE row.rowLineNumber=?1 AND rs.rsCode=?2 AND row.rowAdCompany = ?3");
			query.setParameter(1, ROW_LN_NMBR);
			query.setParameter(2, GL_RS_CODE);
			query.setParameter(3, ROW_AD_CMPNY);
            return (LocalGlFrgRow) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlFrgRowHome.findByRowLineNumberAndRsCode(int ROW_LN_NMBR, java.lang.Integer GL_RS_CODE, java.lang.Integer ROW_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgRowHome.findByRowLineNumberAndRsCode(int ROW_LN_NMBR, java.lang.Integer GL_RS_CODE, java.lang.Integer ROW_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByRsCode(java.lang.Integer GL_RS_CODE, java.lang.Integer ROW_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(row) FROM GlFrgRowSet rs, IN(rs.glFrgRows) row WHERE rs.rsCode = ?1 AND row.rowAdCompany = ?2");
			query.setParameter(1, GL_RS_CODE);
			query.setParameter(2, ROW_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgRowHome.findByRsCode(java.lang.Integer GL_RS_CODE, java.lang.Integer ROW_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByRowSetAndRowLineNumber(java.lang.Integer GL_RS_CODE, int RW_LN_NMBR,
			java.lang.Integer ROW_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(row) FROM GlFrgRowSet rs, IN(rs.glFrgRows) row WHERE rs.rsCode = ?1 AND row.rowLineNumber < ?2 AND row.rowAdCompany = ?3");
			query.setParameter(1, GL_RS_CODE);
			query.setParameter(2, RW_LN_NMBR);
			query.setParameter(3, ROW_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgRowHome.findByRowSetAndRowLineNumber(java.lang.Integer GL_RS_CODE, int RW_LN_NMBR, java.lang.Integer ROW_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findRowAll(java.lang.Integer ROW_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(row) FROM GlFrgRow row WHERE row.rowAdCompany = ?1");
			query.setParameter(1, ROW_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgRowHome.findRowAll(java.lang.Integer ROW_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlFrgRow create(Integer ROW_CODE, int ROW_LN_NMBR, String ROW_NM, int ROW_INDNT,
                                int ROW_LN_TO_SKP_BFR, int ROW_LN_TO_SKP_AFTR, int ROW_UNDRLN_CHAR_BFR, int ROW_UNDRLN_CHAR_AFTR,
                                int ROW_PG_BRK_BFR, int ROW_PG_BRK_AFTR, byte ROW_OVRRD_COL_CALC, byte ROW_HD_RW, String ROW_FNT_STYL,
                                String ROW_HRZNTL_ALGN, Integer ROW_AD_CMPNY) throws CreateException {
		try {

			LocalGlFrgRow entity = new LocalGlFrgRow();

			Debug.print("GlFrgRowBean create");

			entity.setRowCode(ROW_CODE);
			entity.setRowLineNumber(ROW_LN_NMBR);
			entity.setRowName(ROW_NM);
			entity.setRowIndent(ROW_INDNT);
			entity.setRowLineToSkipBefore(ROW_LN_TO_SKP_BFR);
			entity.setRowLineToSkipAfter(ROW_LN_TO_SKP_AFTR);
			entity.setRowUnderlineCharacterBefore(ROW_UNDRLN_CHAR_BFR);
			entity.setRowUnderlineCharacterAfter(ROW_UNDRLN_CHAR_AFTR);
			entity.setRowPageBreakBefore(ROW_PG_BRK_BFR);
			entity.setRowPageBreakAfter(ROW_PG_BRK_AFTR);
			entity.setRowOverrideColumnCalculation(ROW_OVRRD_COL_CALC);
			entity.setRowHideRow(ROW_HD_RW);
			entity.setRowFontStyle(ROW_FNT_STYL);
			entity.setRowHorizontalAlign(ROW_HRZNTL_ALGN);
			entity.setRowAdCompany(ROW_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlFrgRow create(int ROW_LN_NMBR, String ROW_NM, int ROW_INDNT, int ROW_LN_TO_SKP_BFR,
                                int ROW_LN_TO_SKP_AFTR, int ROW_UNDRLN_CHAR_BFR, int ROW_UNDRLN_CHAR_AFTR, int ROW_PG_BRK_BFR,
                                int ROW_PG_BRK_AFTR, byte ROW_OVRRD_COL_CALC, byte ROW_HD_RW, String ROW_FNT_STYL, String ROW_HRZNTL_ALGN,
                                Integer ROW_AD_CMPNY) throws CreateException {
		try {

			LocalGlFrgRow entity = new LocalGlFrgRow();

			Debug.print("GlFrgRowBean create");

			entity.setRowLineNumber(ROW_LN_NMBR);
			entity.setRowName(ROW_NM);
			entity.setRowIndent(ROW_INDNT);
			entity.setRowLineToSkipBefore(ROW_LN_TO_SKP_BFR);
			entity.setRowLineToSkipAfter(ROW_LN_TO_SKP_AFTR);
			entity.setRowUnderlineCharacterBefore(ROW_UNDRLN_CHAR_BFR);
			entity.setRowUnderlineCharacterAfter(ROW_UNDRLN_CHAR_AFTR);
			entity.setRowPageBreakBefore(ROW_PG_BRK_BFR);
			entity.setRowPageBreakAfter(ROW_PG_BRK_AFTR);
			entity.setRowOverrideColumnCalculation(ROW_OVRRD_COL_CALC);
			entity.setRowHideRow(ROW_HD_RW);
			entity.setRowFontStyle(ROW_FNT_STYL);
			entity.setRowHorizontalAlign(ROW_HRZNTL_ALGN);
			entity.setRowAdCompany(ROW_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}