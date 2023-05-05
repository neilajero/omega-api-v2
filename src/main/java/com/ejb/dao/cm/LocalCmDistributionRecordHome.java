package com.ejb.dao.cm;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.cm.LocalCmDistributionRecord;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalCmDistributionRecordHome {

	public static final String JNDI_NAME = "LocalCmDistributionRecordHome!com.ejb.cm.LocalCmDistributionRecordHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalCmDistributionRecordHome() {
	}

	// FINDER METHODS

	public LocalCmDistributionRecord findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalCmDistributionRecord entity = (LocalCmDistributionRecord) em
					.find(new LocalCmDistributionRecord(), pk);
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

	public java.util.Collection findByDrReversalAndDrImportedAndFtCode(byte DR_RVRSL, byte DR_IMPRTD,
			java.lang.Integer FT_CODE, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM CmFundTransfer ft, IN(ft.cmDistributionRecords) dr WHERE dr.drReversal=?1  AND  dr.drImported=?2 AND ft.ftCode=?3 AND dr.drAdCompany = ?4");
			query.setParameter(1, DR_RVRSL);
			query.setParameter(2, DR_IMPRTD);
			query.setParameter(3, FT_CODE);
			query.setParameter(4, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmDistributionRecordHome.findByDrReversalAndDrImportedAndFtCode(byte DR_RVRSL, byte DR_IMPRTD, java.lang.Integer FT_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByDrReversalAndDrImportedAndAdjCode(byte DR_RVRSL, byte DR_IMPRTD,
			java.lang.Integer CM_ADJ_CODE, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM CmAdjustment adj, IN(adj.cmDistributionRecords) dr WHERE dr.drReversal=?1  AND  dr.drImported=?2 AND adj.adjCode=?3 AND dr.drAdCompany = ?4");
			query.setParameter(1, DR_RVRSL);
			query.setParameter(2, DR_IMPRTD);
			query.setParameter(3, CM_ADJ_CODE);
			query.setParameter(4, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmDistributionRecordHome.findByDrReversalAndDrImportedAndAdjCode(byte DR_RVRSL, byte DR_IMPRTD, java.lang.Integer CM_ADJ_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByFtCode(java.lang.Integer FT_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM CmFundTransfer ft, IN(ft.cmDistributionRecords) dr WHERE ft.ftCode=?1 AND dr.drAdCompany = ?2 ORDER BY dr.drDebit DESC");
			query.setParameter(1, FT_CODE);
			query.setParameter(2, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmDistributionRecordHome.findByFtCode(java.lang.Integer FT_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAdjCode(java.lang.Integer CM_ADJ_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM CmAdjustment adj, IN(adj.cmDistributionRecords) dr WHERE adj.adjCode=?1 AND dr.drAdCompany = ?2 ORDER BY dr.drDebit DESC");
			query.setParameter(1, CM_ADJ_CODE);
			query.setParameter(2, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmDistributionRecordHome.findByAdjCode(java.lang.Integer CM_ADJ_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedFtByDateRangeAndCoaAccountNumber(java.util.Date FT_DT_FRM,
			java.util.Date FT_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer FT_AD_BRNCH,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM CmFundTransfer ft, IN(ft.cmDistributionRecords) dr WHERE ft.ftVoid=0 AND ft.ftPosted=0 AND ft.ftDate>=?1 AND ft.ftDate<=?2 AND dr.glChartOfAccount.coaCode=?3 AND ft.ftAdBranch=?4 AND dr.drAdCompany=?5");
			query.setParameter(1, FT_DT_FRM);
			query.setParameter(2, FT_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, FT_AD_BRNCH);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmDistributionRecordHome.findUnpostedFtByDateRangeAndCoaAccountNumber(java.com.util.Date FT_DT_FRM, java.com.util.Date FT_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer FT_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedAdjByDateRangeAndCoaAccountNumber(java.util.Date ADJ_DT_FRM,
			java.util.Date ADJ_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer ADJ_AD_BRNCH,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM CmAdjustment adj, IN(adj.cmDistributionRecords) dr WHERE adj.adjVoid=0 AND adj.adjPosted=0 AND adj.adjDate>=?1 AND adj.adjDate<=?2 AND dr.glChartOfAccount.coaCode=?3 AND adj.adjAdBranch=?4 AND dr.drAdCompany=?5");
			query.setParameter(1, ADJ_DT_FRM);
			query.setParameter(2, ADJ_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, ADJ_AD_BRNCH);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmDistributionRecordHome.findUnpostedAdjByDateRangeAndCoaAccountNumber(java.com.util.Date ADJ_DT_FRM, java.com.util.Date ADJ_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedAdjByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE,
			java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM CmAdjustment adj, IN(adj.cmDistributionRecords) dr WHERE adj.adjPosted=0 AND dr.glChartOfAccount.coaCode=?1 AND adj.adjAdBranch = ?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, ADJ_AD_BRNCH);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmDistributionRecordHome.findUnpostedAdjByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedFtByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE,
			java.lang.Integer FT_AD_BRNCH, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM CmFundTransfer ft, IN(ft.cmDistributionRecords) dr WHERE ft.ftPosted=0 AND dr.glChartOfAccount.coaCode=?1 AND ft.ftAdBranch = ?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, FT_AD_BRNCH);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmDistributionRecordHome.findUnpostedFtByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE, java.lang.Integer FT_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAdjByDateAndCoaAccountNumberAndCurrencyAndAdjPosted(java.util.Date ADJ_DT,
			java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte ADJ_PSTD, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM CmAdjustment adj, IN(adj.cmDistributionRecords) dr WHERE adj.adjDate<=?1 AND dr.glChartOfAccount.coaCode=?2 AND adj.adBankAccount.glFunctionalCurrency.fcCode=?3 AND adj.adjPosted=?4 AND adj.adjVoid=0 AND dr.drAdCompany=?5");
			query.setParameter(1, ADJ_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, FC_CODE);
			query.setParameter(4, ADJ_PSTD);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmDistributionRecordHome.findAdjByDateAndCoaAccountNumberAndCurrencyAndAdjPosted(java.com.util.Date ADJ_DT, java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte ADJ_PSTD, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findFtByDateAndCoaAccountNumberAndCurrencyAndFtPosted(java.util.Date FT_DT,
			java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte FT_PSTD, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM CmFundTransfer ft, IN(ft.cmDistributionRecords) dr, AdBankAccount ba WHERE ft.ftDate<=?1 AND dr.glChartOfAccount.coaCode=?2 AND ft.ftAdBaAccountFrom=ba.baCode AND ba.glFunctionalCurrency.fcCode=?3 AND ft.ftPosted=?4 AND ft.ftVoid=0 AND dr.drAdCompany=?5");
			query.setParameter(1, FT_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, FC_CODE);
			query.setParameter(4, FT_PSTD);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmDistributionRecordHome.findFtByDateAndCoaAccountNumberAndCurrencyAndFtPosted(java.com.util.Date FT_DT, java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte FT_PSTD, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedFtByDateAndCoaAccountNumber(java.util.Date FT_DT_TO,
			java.lang.Integer COA_CODE, java.lang.Integer FT_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM CmFundTransfer ft, IN(ft.cmDistributionRecords) dr WHERE ft.ftVoid=0 AND ft.ftPosted=0 AND ft.ftDate<=?1 AND dr.glChartOfAccount.coaCode=?2 AND ft.ftAdBranch=?3 AND dr.drAdCompany=?4");
			query.setParameter(1, FT_DT_TO);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, FT_AD_BRNCH);
			query.setParameter(4, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmDistributionRecordHome.findUnpostedFtByDateAndCoaAccountNumber(java.com.util.Date FT_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer FT_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedAdjByDateAndCoaAccountNumber(java.util.Date ADJ_DT_TO,
			java.lang.Integer COA_CODE, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM CmAdjustment adj, IN(adj.cmDistributionRecords) dr WHERE adj.adjVoid=0 AND adj.adjPosted=0 AND adj.adjDate<=?1 AND dr.glChartOfAccount.coaCode=?2 AND adj.adjAdBranch=?3 AND dr.drAdCompany=?4");
			query.setParameter(1, ADJ_DT_TO);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, ADJ_AD_BRNCH);
			query.setParameter(4, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmDistributionRecordHome.findUnpostedAdjByDateAndCoaAccountNumber(java.com.util.Date ADJ_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	// CREATE METHODS
	public LocalCmDistributionRecord create(short DR_LN, String DR_CLSS, double DR_AMNT, byte DR_DBT,
                                            byte DR_RVRSL, byte DR_IMPRTD, Integer DR_AD_CMPNY) throws CreateException {
		try {

			LocalCmDistributionRecord entity = new LocalCmDistributionRecord();

			Debug.print("CmDistributionRecordBean create");

			entity.setDrLine(DR_LN);
			entity.setDrClass(DR_CLSS);
			entity.setDrAmount(DR_AMNT);
			entity.setDrDebit(DR_DBT);
			entity.setDrReversal(DR_RVRSL);
			entity.setDrImported(DR_IMPRTD);
			entity.setDrAdCompany(DR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}