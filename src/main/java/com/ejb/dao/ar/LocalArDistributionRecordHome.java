package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArDistributionRecord;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;
import com.util.EJBCommon;

@Stateless
public class LocalArDistributionRecordHome {

	public static final String JNDI_NAME = "LocalArDistributionRecordHome!com.ejb.ar.LocalArDistributionRecordHome";

	@EJB
	public PersistenceBeanClass em;

	private short DR_LN = 0;
	private String DR_CLSS = null;
	private byte DR_DBT = EJBCommon.FALSE;
	private double DR_AMNT = 0d;
	private byte DR_IMPRTD = EJBCommon.FALSE;
	private byte DR_RVRSD = EJBCommon.FALSE;
	private Integer DR_AD_CMPNY = null;

	public LocalArDistributionRecordHome() {
	}

	// FINDER METHODS

	public LocalArDistributionRecord findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArDistributionRecord entity = (LocalArDistributionRecord) em
					.find(new LocalArDistributionRecord(), pk);
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

	public LocalArDistributionRecord findByDrClassDebitAndInvCode(java.lang.String DR_CLSS, java.lang.Integer INV_CODE,
															 java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArInvoice inv, IN(inv.arDistributionRecords) dr WHERE dr.drClass=?1 AND dr.drDebit=1 AND inv.invCode=?2 AND dr.drAdCompany = ?3 ");
			query.setParameter(1, DR_CLSS);
			query.setParameter(2, INV_CODE);
			query.setParameter(3, DR_AD_CMPNY);
			return (LocalArDistributionRecord) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArDistributionRecordHome.findByDrClassDebitAndInvCode(java.lang.String DR_CLSS, java.lang.Integer INV_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findByDrClassDebitAndInvCode(java.lang.String DR_CLSS, java.lang.Integer INV_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArDistributionRecord findByDrClassDebitAndInvCode(java.lang.String DR_CLSS, java.lang.Integer INV_CODE,
																  java.lang.Integer DR_AD_CMPNY, String companyShortName) throws FinderException {
		Debug.print("LocalArDistributionRecord findByDrClassDebitAndInvCode");

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(dr) FROM ArInvoice inv, IN(inv.arDistributionRecords) dr "
							+ "WHERE dr.drClass=?1 AND dr.drDebit=1 AND inv.invCode=?2 AND dr.drAdCompany = ?3 ",
					companyShortName);
			query.setParameter(1, DR_CLSS);
			query.setParameter(2, INV_CODE);
			query.setParameter(3, DR_AD_CMPNY);
			return (LocalArDistributionRecord) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalArDistributionRecord findByDrClassAndInvCode(java.lang.String DR_CLSS, java.lang.Integer INV_CODE,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArInvoice inv, IN(inv.arDistributionRecords) dr WHERE dr.drClass=?1 AND inv.invCode=?2 AND dr.drAdCompany = ?3 ");
			query.setParameter(1, DR_CLSS);
			query.setParameter(2, INV_CODE);
			query.setParameter(3, DR_AD_CMPNY);
            return (LocalArDistributionRecord) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArDistributionRecordHome.findByDrClassAndInvCode(java.lang.String DR_CLSS, java.lang.Integer INV_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findByDrClassAndInvCode(java.lang.String DR_CLSS, java.lang.Integer INV_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArDistributionRecord findByDrClassAndInvCode(java.lang.String DR_CLSS, java.lang.Integer INV_CODE,
															 java.lang.Integer DR_AD_CMPNY, String companyShortName) throws FinderException {
		Debug.print("LocalArDistributionRecord findByDrClassAndInvCode");

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(dr) FROM ArInvoice inv, IN(inv.arDistributionRecords) dr "
							+ "WHERE dr.drClass=?1 AND inv.invCode=?2 AND dr.drAdCompany = ?3 ",
					companyShortName);
			query.setParameter(1, DR_CLSS);
			query.setParameter(2, INV_CODE);
			query.setParameter(3, DR_AD_CMPNY);
			return (LocalArDistributionRecord) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalArDistributionRecord findByDrClassAndRctCode(java.lang.String DR_CLSS, java.lang.Integer CHK_CODE,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArReceipt rct, IN(rct.arDistributionRecords) dr WHERE dr.drClass=?1 AND rct.rctCode=?2 AND dr.drAdCompany = ?3 ");
			query.setParameter(1, DR_CLSS);
			query.setParameter(2, CHK_CODE);
			query.setParameter(3, DR_AD_CMPNY);
            return (LocalArDistributionRecord) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArDistributionRecordHome.findByDrClassAndRctCode(java.lang.String DR_CLSS, java.lang.Integer CHK_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findByDrClassAndRctCode(java.lang.String DR_CLSS, java.lang.Integer CHK_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDrsByDrClassAndInvCode(java.lang.String DR_CLSS, java.lang.Integer INV_CODE,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArInvoice inv, IN(inv.arDistributionRecords) dr WHERE dr.drClass=?1 AND inv.invCode=?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, DR_CLSS);
			query.setParameter(2, INV_CODE);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findDrsByDrClassAndInvCode(java.lang.String DR_CLSS, java.lang.Integer INV_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDrsByDrClassAndInvCode(java.lang.String DR_CLSS, java.lang.Integer INV_CODE,
														   java.lang.Integer DR_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(dr) FROM ArInvoice inv, IN(inv.arDistributionRecords) dr WHERE dr.drClass=?1 AND inv.invCode=?2 AND dr.drAdCompany = ?3",
					companyShortName);
			query.setParameter(1, DR_CLSS);
			query.setParameter(2, INV_CODE);
			query.setParameter(3, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findDrsByDrClassAndRctCode(java.lang.String DR_CLSS, java.lang.Integer CHK_CODE,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArReceipt rct, IN(rct.arDistributionRecords) dr WHERE dr.drClass=?1 AND rct.rctCode=?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, DR_CLSS);
			query.setParameter(2, CHK_CODE);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findDrsByDrClassAndRctCode(java.lang.String DR_CLSS, java.lang.Integer CHK_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDrsByDrClassAndAiCode(java.lang.String DR_CLSS, java.lang.Integer AI_CODE,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArAppliedInvoice ai, IN(ai.arDistributionRecords) dr WHERE dr.drClass=?1 AND ai.aiCode=?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, DR_CLSS);
			query.setParameter(2, AI_CODE);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findDrsByDrClassAndAiCode(java.lang.String DR_CLSS, java.lang.Integer AI_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findImportableDrByInvCode(java.lang.Integer INV_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArInvoice inv, IN(inv.arDistributionRecords) dr WHERE dr.drImported = 0 AND inv.invCode = ?1 AND dr.drAdCompany = ?2");
			query.setParameter(1, INV_CODE);
			query.setParameter(2, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findImportableDrByInvCode(java.lang.Integer INV_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findImportableDrByDrReversedAndRctCode(byte DR_RVRSD, java.lang.Integer RCT_CODE,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArReceipt rct, IN(rct.arDistributionRecords) dr WHERE dr.drImported = 0 AND dr.drReversed = ?1 AND rct.rctCode = ?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, DR_RVRSD);
			query.setParameter(2, RCT_CODE);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findImportableDrByDrReversedAndRctCode(byte DR_RVRSD, java.lang.Integer RCT_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findImportableDrByDrReversedAndRctCode(byte DR_RVRSD, java.lang.Integer RCT_CODE,
																	   java.lang.Integer DR_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(dr) FROM ArReceipt rct, IN(rct.arDistributionRecords) dr "
							+ "WHERE dr.drImported = 0 AND dr.drReversed = ?1 AND rct.rctCode = ?2 AND dr.drAdCompany = ?3",
					companyShortName);
			query.setParameter(1, DR_RVRSD);
			query.setParameter(2, RCT_CODE);
			query.setParameter(3, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByDrClassAndInvCmInvoiceNumber(java.lang.String DR_CLSS,
			java.lang.String INV_CM_INVC_NMBR, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArInvoice inv, IN(inv.arDistributionRecords) dr WHERE dr.drClass=?1 AND inv.invCmInvoiceNumber = ?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, DR_CLSS);
			query.setParameter(2, INV_CM_INVC_NMBR);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findByDrClassAndInvCmInvoiceNumber(java.lang.String DR_CLSS, java.lang.String INV_CM_INVC_NMBR, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByInvCode(java.lang.Integer INV_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArInvoice inv, IN(inv.arDistributionRecords) dr WHERE inv.invCode =?1 AND dr.drAdCompany=?2 ORDER BY dr.drDebit DESC");
			query.setParameter(1, INV_CODE);
			query.setParameter(2, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findByInvCode(java.lang.Integer INV_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByRctCode(java.lang.Integer INV_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArReceipt rct, IN(rct.arDistributionRecords) dr WHERE rct.rctCode =?1 AND dr.drAdCompany=?2 ORDER BY dr.drDebit DESC");
			query.setParameter(1, INV_CODE);
			query.setParameter(2, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findByRctCode(java.lang.Integer INV_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findInvByDateRangeAndCoaAccountNumber(byte INV_CRDT_MMO, java.util.Date INV_DT_FRM,
			java.util.Date INV_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArInvoice inv, IN(inv.arDistributionRecords) dr WHERE inv.invPosted=1 AND inv.invCreditMemo=?1 AND inv.invDate>=?2 AND inv.invDate<=?3 AND dr.glChartOfAccount.coaCode=?4 AND dr.drAdCompany=?5");
			query.setParameter(1, INV_CRDT_MMO);
			query.setParameter(2, INV_DT_FRM);
			query.setParameter(3, INV_DT_TO);
			query.setParameter(4, COA_CODE);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findInvByDateRangeAndCoaAccountNumber(byte INV_CRDT_MMO, java.com.util.Date INV_DT_FRM, java.com.util.Date INV_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findRctByDateRangeAndCoaAccountNumber(java.util.Date RCT_DT_FRM,
			java.util.Date RCT_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArReceipt rct, IN(rct.arDistributionRecords) dr WHERE rct.rctPosted=1 AND rct.rctDate>=?1 AND rct.rctDate<=?2 AND dr.glChartOfAccount.coaCode=?3 AND dr.drAdCompany=?4");
			query.setParameter(1, RCT_DT_FRM);
			query.setParameter(2, RCT_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findRctByDateRangeAndCoaAccountNumber(java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedInvByDateRangeAndCoaAccountNumber(byte INV_CRDT_MMO,
			java.util.Date INV_DT_FRM, java.util.Date INV_DT_TO, java.lang.Integer COA_CODE,
			java.lang.Integer INV_AD_BRNCH, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArInvoice inv, IN(inv.arDistributionRecords) dr WHERE inv.invVoid=0 AND inv.invPosted=0 AND inv.invCreditMemo=?1 AND inv.invDate>=?2 AND inv.invDate<=?3 AND dr.glChartOfAccount.coaCode=?4 AND inv.invAdBranch=?5 AND dr.drAdCompany=?6");
			query.setParameter(1, INV_CRDT_MMO);
			query.setParameter(2, INV_DT_FRM);
			query.setParameter(3, INV_DT_TO);
			query.setParameter(4, COA_CODE);
			query.setParameter(5, INV_AD_BRNCH);
			query.setParameter(6, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findUnpostedInvByDateRangeAndCoaAccountNumber(byte INV_CRDT_MMO, java.com.util.Date INV_DT_FRM, java.com.util.Date INV_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer INV_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedRctByDateRangeAndCoaAccountNumber(java.util.Date RCT_DT_FRM,
			java.util.Date RCT_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArReceipt rct, IN(rct.arDistributionRecords) dr WHERE rct.rctVoid=0 AND rct.rctPosted=0 AND rct.rctDate>=?1 AND rct.rctDate<=?2 AND dr.glChartOfAccount.coaCode=?3 AND rct.rctAdBranch=?4 AND dr.drAdCompany=?5");
			query.setParameter(1, RCT_DT_FRM);
			query.setParameter(2, RCT_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, RCT_AD_BRNCH);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findUnpostedRctByDateRangeAndCoaAccountNumber(java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedInvByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE,
			java.lang.Integer INV_AD_BRNCH, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArInvoice inv, IN(inv.arDistributionRecords) dr WHERE inv.invPosted=0 AND dr.glChartOfAccount.coaCode=?1 AND inv.invAdBranch = ?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, INV_AD_BRNCH);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findUnpostedInvByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE, java.lang.Integer INV_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedRctByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE,
			java.lang.Integer RCT_AD_BRNCH, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArReceipt rct, IN(rct.arDistributionRecords) dr WHERE rct.rctPosted=0 AND dr.glChartOfAccount.coaCode=?1 AND rct.rctAdBranch = ?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, RCT_AD_BRNCH);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findUnpostedRctByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findInvByDateAndCoaAccountNumberAndCurrencyAndInvPosted(java.util.Date INV_DT,
			java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte INV_PSTD, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArInvoice inv, IN(inv.arDistributionRecords) dr WHERE inv.invCreditMemo=0 AND inv.invDate<=?1 AND dr.glChartOfAccount.coaCode=?2 AND inv.glFunctionalCurrency.fcCode=?3 AND inv.invPosted=?4 AND inv.invVoid=0 AND dr.drAdCompany=?5");
			query.setParameter(1, INV_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, FC_CODE);
			query.setParameter(4, INV_PSTD);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findInvByDateAndCoaAccountNumberAndCurrencyAndInvPosted(java.com.util.Date INV_DT, java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte INV_PSTD, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findInvCreditMemoByDateAndCoaAccountNumberAndCurrencyAndInvPosted(java.util.Date INV_DT,
			java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte INV_PSTD, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArInvoice cm, IN(cm.arDistributionRecords) dr, ArInvoice inv WHERE cm.invCreditMemo=1 AND cm.invCmInvoiceNumber=inv.invNumber AND cm.invDate<=?1 AND dr.glChartOfAccount.coaCode=?2 AND inv.glFunctionalCurrency.fcCode=?3 AND cm.invPosted=?4 AND cm.invVoid=0 AND dr.drAdCompany=?5");
			query.setParameter(1, INV_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, FC_CODE);
			query.setParameter(4, INV_PSTD);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findInvCreditMemoByDateAndCoaAccountNumberAndCurrencyAndInvPosted(java.com.util.Date INV_DT, java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte INV_PSTD, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findRctByDateAndCoaAccountNumberAndCurrencyAndRctPosted(java.util.Date RCT_DT,
			java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte RCT_PSTD, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArReceipt rct, IN(rct.arDistributionRecords) dr, IN(rct.arAppliedInvoices) ai WHERE rct.rctType='COLLECTION' AND rct.rctDate<=?1 AND dr.glChartOfAccount.coaCode=?2 AND rct.glFunctionalCurrency.fcCode=?3 AND rct.rctPosted=?4 AND rct.rctVoid=0 AND dr.drAdCompany=?5");
			query.setParameter(1, RCT_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, FC_CODE);
			query.setParameter(4, RCT_PSTD);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findRctByDateAndCoaAccountNumberAndCurrencyAndRctPosted(java.com.util.Date RCT_DT, java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte RCT_PSTD, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findRctMiscByDateAndCoaAccountNumberAndCurrencyAndRctPosted(java.util.Date RCT_DT,
			java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte RCT_PSTD, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArReceipt rct, IN(rct.arDistributionRecords) dr WHERE rct.rctType='MISC' AND rct.rctDate<=?1 AND dr.glChartOfAccount.coaCode=?2 AND rct.glFunctionalCurrency.fcCode=?3 AND rct.rctPosted=?4 AND rct.rctVoid=0 AND dr.drAdCompany=?5");
			query.setParameter(1, RCT_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, FC_CODE);
			query.setParameter(4, RCT_PSTD);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findRctMiscByDateAndCoaAccountNumberAndCurrencyAndRctPosted(java.com.util.Date RCT_DT, java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte RCT_PSTD, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedInvByDateAndCoaAccountNumber(byte INV_CRDT_MMO, java.util.Date INV_DT_TO,
			java.lang.Integer COA_CODE, java.lang.Integer INV_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArInvoice inv, IN(inv.arDistributionRecords) dr WHERE inv.invVoid=0 AND inv.invPosted=0 AND inv.invCreditMemo=?1 AND inv.invDate<=?2 AND dr.glChartOfAccount.coaCode=?3 AND inv.invAdBranch=?4 AND dr.drAdCompany=?5");
			query.setParameter(1, INV_CRDT_MMO);
			query.setParameter(2, INV_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, INV_AD_BRNCH);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findUnpostedInvByDateAndCoaAccountNumber(byte INV_CRDT_MMO, java.com.util.Date INV_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer INV_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedRctByDateAndCoaAccountNumber(java.util.Date RCT_DT_TO,
			java.lang.Integer COA_CODE, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ArReceipt rct, IN(rct.arDistributionRecords) dr WHERE rct.rctVoid=0 AND rct.rctPosted=0 AND rct.rctDate<=?1 AND dr.glChartOfAccount.coaCode=?2 AND rct.rctAdBranch=?3 AND dr.drAdCompany=?4");
			query.setParameter(1, RCT_DT_TO);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, RCT_AD_BRNCH);
			query.setParameter(4, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDistributionRecordHome.findUnpostedRctByDateAndCoaAccountNumber(java.com.util.Date RCT_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getDrByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
			throws FinderException {

		try {
			Query query = em.createQuery(jbossQl);
			int cnt = 1;
			for (Object data : args) {
				query.setParameter(cnt, data);
				cnt++;
			}
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS
	public void deleteRecord(LocalArDistributionRecord arDistributionRecord) {

		Debug.print("ArDistributionRecordBean deleteRecord");

		try {
			
			em.remove(arDistributionRecord);
			
		} catch (RemoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public LocalArDistributionRecordHome DrLine(short DR_LN) {
		this.DR_LN = DR_LN;
		return this;
	}

	public LocalArDistributionRecordHome DrClass(String DR_CLSS) {
		this.DR_CLSS = DR_CLSS;
		return this;
	}

	public LocalArDistributionRecordHome DrDebit(byte DR_DBT) {
		this.DR_DBT = DR_DBT;
		return this;
	}

	public LocalArDistributionRecordHome DrAmount(double DR_AMNT) {
		this.DR_AMNT = DR_AMNT;
		return this;
	}

	public LocalArDistributionRecordHome DrImported(byte DR_IMPRTD) {
		this.DR_IMPRTD = DR_IMPRTD;
		return this;
	}

	public LocalArDistributionRecordHome DrReversed(byte DR_RVRSD) {
		this.DR_RVRSD = DR_RVRSD;
		return this;
	}

	public LocalArDistributionRecordHome DrAdCompany(Integer DR_AD_CMPNY) {
		this.DR_AD_CMPNY = DR_AD_CMPNY;
		return this;
	}

	public LocalArDistributionRecord buildDistributionRecords(String companyShortName) throws CreateException {
		try {

			LocalArDistributionRecord entity = new LocalArDistributionRecord();

			Debug.print("ArDistributionRecordBean buildDistributionRecords");
			entity.setDrLine(DR_LN);
			entity.setDrClass(DR_CLSS);
			entity.setDrDebit(DR_DBT);
			entity.setDrAmount(DR_AMNT);
			entity.setDrImported(DR_IMPRTD);
			entity.setDrReversed(DR_RVRSD);
			entity.setDrAdCompany(DR_AD_CMPNY);

			em.persist(entity, companyShortName);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArDistributionRecord create(Integer AR_DR_CODE, short DR_LN, String DR_CLSS, byte DR_DBT,
                                            double DR_AMNT, byte DR_IMPRTD, byte DR_RVRSD, Integer DR_AD_CMPNY) throws CreateException {
		try {

			LocalArDistributionRecord entity = new LocalArDistributionRecord();

			Debug.print("ArDistributionRecordBean create");
			entity.setDrCode(AR_DR_CODE);
			entity.setDrLine(DR_LN);
			entity.setDrClass(DR_CLSS);
			entity.setDrDebit(DR_DBT);
			entity.setDrAmount(DR_AMNT);
			entity.setDrImported(DR_IMPRTD);
			entity.setDrReversed(DR_RVRSD);
			entity.setDrAdCompany(DR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArDistributionRecord create(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT,
                                            byte DR_IMPRTD, byte DR_RVRSD, Integer DR_AD_CMPNY) throws CreateException {
		try {

			LocalArDistributionRecord entity = new LocalArDistributionRecord();

			Debug.print("ArDistributionRecordBean create");
			entity.setDrLine(DR_LN);
			entity.setDrClass(DR_CLSS);
			entity.setDrDebit(DR_DBT);
			entity.setDrAmount(DR_AMNT);
			entity.setDrImported(DR_IMPRTD);
			entity.setDrReversed(DR_RVRSD);
			entity.setDrAdCompany(DR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}