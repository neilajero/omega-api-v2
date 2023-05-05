package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.util.Debug;
import com.util.EJBCommon;

@Stateless
public class LocalGlJournalLineHome {

	public static final String JNDI_NAME = "LocalGlJournalLineHome!com.ejb.gl.LocalGlJournalLineHome";

	@EJB
	public PersistenceBeanClass em;

	private short JL_LN_NMBR = 0;
	private byte JL_DBT = EJBCommon.TRUE; // Default DEBIT
	private double JL_AMNT = 0d;
	private String JL_DESC = null;
	private Integer JL_AD_CMPNY = null;

	public LocalGlJournalLineHome() {
	}

	// FINDER METHODS

	public LocalGlJournalLine findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlJournalLine entity = (LocalGlJournalLine) em
					.find(new LocalGlJournalLine(), pk);
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

	public java.util.Collection findUnpostedJlByJrEffectiveDateAndCoaCode(java.util.Date JR_DT,
			java.lang.Integer COA_CODE, java.lang.Integer JL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jl) FROM GlJournal jr, IN(jr.glJournalLines) jl WHERE jr.jrPosted = 0 AND jr.jrEffectiveDate <= ?1 AND jl.glChartOfAccount.coaCode = ?2 AND jl.jlAdCompany = ?3");
			query.setParameter(1, JR_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, JL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalLineHome.findUnpostedJlByJrEffectiveDateAndCoaCode(java.com.util.Date JR_DT, java.lang.Integer COA_CODE, java.lang.Integer JL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedJlByJrEffectiveDateAndCoaAccountNumberRange(
			java.lang.String COA_ACCNT_NMBR_FROM, java.lang.String COA_ACCNT_NMBR_TO, java.lang.Integer JL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jl) FROM GlJournal jr, IN(jr.glJournalLines) jl WHERE jr.jrPosted = 1 AND jl.glChartOfAccount.coaAccountNumber BETWEEN ?1 AND ?2 AND jl.jlAdCompany = ?3");
			query.setParameter(1, COA_ACCNT_NMBR_FROM);
			query.setParameter(2, COA_ACCNT_NMBR_TO);
			query.setParameter(3, JL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalLineHome.findPostedJlByJrEffectiveDateAndCoaAccountNumberRange(java.lang.String COA_ACCNT_NMBR_FROM, java.lang.String COA_ACCNT_NMBR_TO, java.lang.Integer JL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedJlByJrEffectiveDateRangeAndCoaCode(java.util.Date JR_DT_FRM,
			java.util.Date JR_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer JL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jl) FROM GlJournal jr, IN(jr.glJournalLines) jl WHERE jr.jrPosted = 0 AND jr.jrEffectiveDate >= ?1 AND jr.jrEffectiveDate <= ?2 AND jl.glChartOfAccount.coaCode = ?3 AND jl.jlAdCompany = ?4");
			query.setParameter(1, JR_DT_FRM);
			query.setParameter(2, JR_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, JL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalLineHome.findUnpostedJlByJrEffectiveDateRangeAndCoaCode(java.com.util.Date JR_DT_FRM, java.com.util.Date JR_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer JL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedJlByJrEffectiveDateAndCoaCodeAndJsName(java.util.Date JR_DT,
			java.lang.Integer COA_CODE, java.lang.String JS_NM, java.lang.Integer JL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jl) FROM GlJournal jr, IN(jr.glJournalLines) jl WHERE jr.jrPosted = 0 AND jr.jrEffectiveDate <= ?1 AND jl.glChartOfAccount.coaCode = ?2 AND jl.glJournal.glJournalSource.jsName=?3 AND jl.jlAdCompany = ?4");
			query.setParameter(1, JR_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, JS_NM);
			query.setParameter(4, JL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalLineHome.findUnpostedJlByJrEffectiveDateAndCoaCodeAndJsName(java.com.util.Date JR_DT, java.lang.Integer COA_CODE, java.lang.String JS_NM, java.lang.Integer JL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedJlByJrEffectiveDateRangeAndCoaCode(java.util.Date JR_DT_FRM,
			java.util.Date JR_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer JL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jl) FROM GlJournal jr, IN(jr.glJournalLines) jl WHERE jr.jrPosted = 1 AND jr.jrEffectiveDate >= ?1 AND jr.jrEffectiveDate <= ?2 AND jl.glChartOfAccount.coaCode = ?3 AND jl.jlAdCompany = ?4 ORDER BY jr.jrEffectiveDate, jr.jrDocumentNumber");
			query.setParameter(1, JR_DT_FRM);
			query.setParameter(2, JR_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, JL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalLineHome.findPostedJlByJrEffectiveDateRangeAndCoaCode(java.com.util.Date JR_DT_FRM, java.com.util.Date JR_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer JL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedJlByJrEffectiveDateAndCoaCodeAndJsName(java.util.Date JR_DT,
			java.lang.Integer COA_CODE, java.lang.String JS_NM, java.lang.Integer JL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jl) FROM GlJournal jr, IN(jr.glJournalLines) jl WHERE jr.jrPosted = 1 AND jr.jrEffectiveDate <= ?1 AND jl.glChartOfAccount.coaCode = ?2 AND jl.glJournal.glJournalSource.jsName=?3 AND jl.jlAdCompany = ?4");
			query.setParameter(1, JR_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, JS_NM);
			query.setParameter(4, JL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalLineHome.findPostedJlByJrEffectiveDateAndCoaCodeAndJsName(java.com.util.Date JR_DT, java.lang.Integer COA_CODE, java.lang.String JS_NM, java.lang.Integer JL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByJrEffectiveDateRangeAndCoaCode(java.util.Date JR_DT_FRM, java.util.Date JR_DT_TO,
			java.lang.Integer COA_CODE, java.lang.Integer JL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jl) FROM GlJournal jr, IN(jr.glJournalLines) jl WHERE jr.jrEffectiveDate >= ?1 AND jr.jrEffectiveDate <= ?2 AND jl.glChartOfAccount.coaCode = ?3 AND jl.jlAdCompany = ?4 ORDER BY jr.jrEffectiveDate, jr.jrDocumentNumber");
			query.setParameter(1, JR_DT_FRM);
			query.setParameter(2, JR_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, JL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalLineHome.findByJrEffectiveDateRangeAndCoaCode(java.com.util.Date JR_DT_FRM, java.com.util.Date JR_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer JL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByJrEffectiveDateAndCoaCodeAndJsName(java.util.Date JR_DT,
			java.lang.Integer COA_CODE, java.lang.String JS_NM, java.lang.Integer JL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jl) FROM GlJournal jr, IN(jr.glJournalLines) jl WHERE jr.jrEffectiveDate <= ?1 AND jl.glChartOfAccount.coaCode = ?2 AND jl.glJournal.glJournalSource.jsName=?3 AND jl.jlAdCompany = ?4");
			query.setParameter(1, JR_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, JS_NM);
			query.setParameter(4, JL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalLineHome.findByJrEffectiveDateAndCoaCodeAndJsName(java.com.util.Date JR_DT, java.lang.Integer COA_CODE, java.lang.String JS_NM, java.lang.Integer JL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByJrCode(java.lang.Integer JR_CODE, java.lang.Integer JL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jl) FROM GlJournal jr, IN(jr.glJournalLines) jl WHERE jr.jrCode = ?1 AND jl.jlAdCompany = ?2");
			query.setParameter(1, JR_CODE);
			query.setParameter(2, JL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalLineHome.findByJrCode(java.lang.Integer JR_CODE, java.lang.Integer JL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByJrCode(java.lang.Integer JR_CODE, java.lang.Integer JL_AD_CMPNY, String companyShortName)
			throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(jl) FROM GlJournal jr, IN(jr.glJournalLines) jl WHERE jr.jrCode = ?1 AND jl.jlAdCompany = ?2",
					companyShortName);
			query.setParameter(1, JR_CODE);
			query.setParameter(2, JL_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findManualJrByEffectiveDateRangeAndCoaCode(java.util.Date JR_DT_FRM,
			java.util.Date JR_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer JR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jl) FROM GlJournal jr, IN(jr.glJournalLines) jl WHERE jr.glJournalSource.jsName='MANUAL' AND jr.jrPosted=1 AND jr.jrEffectiveDate>=?1 AND jr.jrEffectiveDate<=?2 AND jl.glChartOfAccount.coaCode=?3 AND jl.jlAdCompany=?4");
			query.setParameter(1, JR_DT_FRM);
			query.setParameter(2, JR_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, JR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalLineHome.findManualJrByEffectiveDateRangeAndCoaCode(java.com.util.Date JR_DT_FRM, java.com.util.Date JR_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer JR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedJrByJlCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE,
			java.lang.Integer JR_AD_BRNCH, java.lang.Integer JL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jl) FROM GlJournal jr, IN(jr.glJournalLines) jl WHERE jr.jrPosted=0 AND jl.glChartOfAccount.coaCode=?1 AND jr.jrAdBranch = ?2 AND jl.jlAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, JR_AD_BRNCH);
			query.setParameter(3, JL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalLineHome.findUnpostedJrByJlCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE, java.lang.Integer JR_AD_BRNCH, java.lang.Integer JL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findJrByDateAndCoaAccountNumberAndCurrencyAndJrPosted(java.util.Date JR_DT,
			java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte JR_PSTD, java.lang.Integer JL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jl) FROM GlJournal jr, IN(jr.glJournalLines) jl WHERE jr.glJournalSource.jsName='MANUAL' AND jr.jrEffectiveDate<=?1 AND jl.glChartOfAccount.coaCode=?2 AND jr.glFunctionalCurrency.fcCode=?3 AND jr.jrPosted=?4 AND jr.jrReversed=0 AND jl.jlAdCompany=?5");
			query.setParameter(1, JR_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, FC_CODE);
			query.setParameter(4, JR_PSTD);
			query.setParameter(5, JL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalLineHome.findJrByDateAndCoaAccountNumberAndCurrencyAndJrPosted(java.com.util.Date JR_DT, java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte JR_PSTD, java.lang.Integer JL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findJrByDateAndCoaAccountNumberAndCurrencyAndJrPostedAndJsName(java.util.Date JR_DT,
			java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte JR_PSTD, java.lang.String JS_NM,
			java.lang.Integer JL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jl) FROM GlJournal jr, IN(jr.glJournalLines) jl WHERE jr.jrEffectiveDate<=?1 AND jl.glChartOfAccount.coaCode=?2 AND jr.glFunctionalCurrency.fcCode=?3 AND jr.jrPosted=?4 AND jr.jrReversed=0 AND jl.glJournal.glJournalSource.jsName=?5 AND jl.jlAdCompany=?6");
			query.setParameter(1, JR_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, FC_CODE);
			query.setParameter(4, JR_PSTD);
			query.setParameter(5, JS_NM);
			query.setParameter(6, JL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalLineHome.findJrByDateAndCoaAccountNumberAndCurrencyAndJrPostedAndJsName(java.com.util.Date JR_DT, java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte JR_PSTD, java.lang.String JS_NM, java.lang.Integer JL_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS
	public LocalGlJournalLine buildJournalLine() throws CreateException {
		try {

			LocalGlJournalLine entity = new LocalGlJournalLine();

			Debug.print("glJournalLineBean buildJournalLine");

			entity.setJlLineNumber(JL_LN_NMBR);
			entity.setJlDebit(JL_DBT);
			entity.setJlAmount(JL_AMNT);
			entity.setJlDescription(JL_DESC);
			entity.setJlAdCompany(JL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlJournalLine buildJournalLine(String companyShortName) throws CreateException {
		try {

			LocalGlJournalLine entity = new LocalGlJournalLine();

			Debug.print("glJournalLineBean buildJournalLine");

			entity.setJlLineNumber(JL_LN_NMBR);
			entity.setJlDebit(JL_DBT);
			entity.setJlAmount(JL_AMNT);
			entity.setJlDescription(JL_DESC);
			entity.setJlAdCompany(JL_AD_CMPNY);

			em.persist(entity, companyShortName);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlJournalLineHome JlLineNumber(short JL_LN_NMBR) {
		this.JL_LN_NMBR = JL_LN_NMBR;
		return this;
	}

	public LocalGlJournalLineHome JlDebit(byte JL_DBT) {
		this.JL_DBT = JL_DBT;
		return this;
	}

	public LocalGlJournalLineHome JlAmount(double JL_AMNT) {
		this.JL_AMNT = JL_AMNT;
		return this;
	}

	public LocalGlJournalLineHome JlDescription(String JL_DESC) {
		this.JL_DESC = JL_DESC;
		return this;
	}

	public LocalGlJournalLineHome JlAdCompany(Integer JL_AD_CMPNY) {
		this.JL_AD_CMPNY = JL_AD_CMPNY;
		return this;
	}

	public LocalGlJournalLine create(java.lang.Integer JL_CODE, short JL_LN_NMBR, byte JL_DBT,
                                     double JL_AMNT, String JL_DESC, Integer JL_AD_CMPNY) throws CreateException {
		try {

			LocalGlJournalLine entity = new LocalGlJournalLine();

			Debug.print("glJournalLineBean create");

			entity.setJlCode(JL_CODE);
			entity.setJlLineNumber(JL_LN_NMBR);
			entity.setJlDebit(JL_DBT);
			entity.setJlAmount(JL_AMNT);
			entity.setJlDescription(JL_DESC);
			entity.setJlAdCompany(JL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlJournalLine create(short JL_LN_NMBR, byte JL_DBT, double JL_AMNT, String JL_DESC,
                                     Integer JL_AD_CMPNY) throws CreateException {
		try {

			LocalGlJournalLine entity = new LocalGlJournalLine();

			Debug.print("glJournalLineBean create");

			entity.setJlLineNumber(JL_LN_NMBR);
			entity.setJlDebit(JL_DBT);
			entity.setJlAmount(JL_AMNT);
			entity.setJlDescription(JL_DESC);
			entity.setJlAdCompany(JL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}