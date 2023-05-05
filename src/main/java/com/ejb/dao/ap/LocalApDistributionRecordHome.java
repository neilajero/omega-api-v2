package com.ejb.dao.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApDistributionRecord;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalApDistributionRecordHome {

	public static final String JNDI_NAME = "LocalApDistributionRecordHome!com.ejb.ap.LocalApDistributionRecordHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalApDistributionRecordHome() {
	}

	// FINDER METHODS

	public LocalApDistributionRecord findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalApDistributionRecord entity = (LocalApDistributionRecord) em
					.find(new LocalApDistributionRecord(), pk);
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

	public LocalApDistributionRecord findByDrClassAndVouCode(java.lang.String DR_CLSS, java.lang.Integer VOU_CODE,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApVoucher vou, IN(vou.apDistributionRecords) dr WHERE dr.drClass=?1 AND vou.vouCode=?2 AND dr.drAdCompany=?3");
			query.setParameter(1, DR_CLSS);
			query.setParameter(2, VOU_CODE);
			query.setParameter(3, DR_AD_CMPNY);
            return (LocalApDistributionRecord) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApDistributionRecordHome.findByDrClassAndVouCode(java.lang.String DR_CLSS, java.lang.Integer VOU_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findByDrClassAndVouCode(java.lang.String DR_CLSS, java.lang.Integer VOU_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDrsByDrClassAndVouCode(java.lang.String DR_CLSS, java.lang.Integer VOU_CODE,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApVoucher vou, IN(vou.apDistributionRecords) dr WHERE dr.drClass=?1 AND vou.vouCode=?2 AND dr.drAdCompany=?3");
			query.setParameter(1, DR_CLSS);
			query.setParameter(2, VOU_CODE);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findDrsByDrClassAndVouCode(java.lang.String DR_CLSS, java.lang.Integer VOU_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDrsByDrTaxClassAndVouDateRange(java.util.Date VOU_DT_FRM, java.util.Date VOU_DT_TO,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApVoucher vou, IN(vou.apDistributionRecords) dr WHERE dr.drClass='TAX' AND vou.vouDate >= ?1 AND vou.vouDate <= ?2 AND dr.drAdCompany=?3");
			query.setParameter(1, VOU_DT_FRM);
			query.setParameter(2, VOU_DT_TO);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findDrsByDrTaxClassAndVouDateRange(java.com.util.Date VOU_DT_FRM, java.com.util.Date VOU_DT_TO, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDrsByDrTaxClassAndChkDateRange(java.util.Date VOU_DT_FRM, java.util.Date VOU_DT_TO,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApCheck chk, IN(chk.apDistributionRecords) dr WHERE dr.drClass='TAX' AND chk.chkDate >= ?1 AND chk.chkDate <= ?2 AND dr.drAdCompany=?3");
			query.setParameter(1, VOU_DT_FRM);
			query.setParameter(2, VOU_DT_TO);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findDrsByDrTaxClassAndChkDateRange(java.com.util.Date VOU_DT_FRM, java.com.util.Date VOU_DT_TO, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDrsByDrClassAndChkCode(java.lang.String DR_CLSS, java.lang.Integer CHK_CODE,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApCheck chk, IN(chk.apDistributionRecords) dr WHERE dr.drClass=?1 AND chk.chkCode=?2 AND dr.drAdCompany=?3");
			query.setParameter(1, DR_CLSS);
			query.setParameter(2, CHK_CODE);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findDrsByDrClassAndChkCode(java.lang.String DR_CLSS, java.lang.Integer CHK_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findImportableDrByVouCode(java.lang.Integer VOU_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApVoucher vou, IN(vou.apDistributionRecords) dr WHERE dr.drImported = 0 AND vou.vouCode = ?1 AND dr.drAdCompany=?2");
			query.setParameter(1, VOU_CODE);
			query.setParameter(2, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findImportableDrByVouCode(java.lang.Integer VOU_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findImportableDrByPoCode(java.lang.Integer PO_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApPurchaseOrder po, IN(po.apDistributionRecords) dr WHERE dr.drImported = 0 AND po.poCode = ?1 AND dr.drAdCompany=?2");
			query.setParameter(1, PO_CODE);
			query.setParameter(2, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findImportableDrByPoCode(java.lang.Integer PO_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findImportableDrByDrReversedAndChkCode(byte DR_RVRSD, java.lang.Integer CHK_CODE,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApCheck chk, IN(chk.apDistributionRecords) dr WHERE dr.drImported = 0 AND dr.drReversed = ?1 AND chk.chkCode = ?2 AND dr.drAdCompany=?3");
			query.setParameter(1, DR_RVRSD);
			query.setParameter(2, CHK_CODE);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findImportableDrByDrReversedAndChkCode(byte DR_RVRSD, java.lang.Integer CHK_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByDrClassAndVouDmVoucherNumber(java.lang.String DR_CLSS,
			java.lang.String VOU_DM_VCHR_NMBR, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApVoucher vou, IN(vou.apDistributionRecords) dr WHERE dr.drClass=?1 AND vou.vouDmVoucherNumber = ?2 AND dr.drAdCompany=?3");
			query.setParameter(1, DR_CLSS);
			query.setParameter(2, VOU_DM_VCHR_NMBR);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findByDrClassAndVouDmVoucherNumber(java.lang.String DR_CLSS, java.lang.String VOU_DM_VCHR_NMBR, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByDrClassAndVouAmountPaid(java.lang.String DR_CLSS, double VOU_AMNT_PD,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApVoucher vou, IN(vou.apDistributionRecords) dr WHERE dr.drClass=?1 AND vou.vouAmountDue=vou.vouAmountPaid AND dr.drAdCompany=?3");
			query.setParameter(1, DR_CLSS);
			query.setParameter(2, VOU_AMNT_PD);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findByDrClassAndVouAmountPaid(java.lang.String DR_CLSS, double VOU_AMNT_PD, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVouCode(java.lang.Integer VOU_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApVoucher vou, IN(vou.apDistributionRecords) dr WHERE vou.vouCode =?1 AND dr.drAdCompany=?2 ORDER BY dr.drDebit DESC");
			query.setParameter(1, VOU_CODE);
			query.setParameter(2, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findByVouCode(java.lang.Integer VOU_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByChkCode(java.lang.Integer CHK_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApCheck chk, IN(chk.apDistributionRecords) dr WHERE chk.chkCode =?1 AND dr.drAdCompany=?2 ORDER BY dr.drDebit DESC");
			query.setParameter(1, CHK_CODE);
			query.setParameter(2, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findByChkCode(java.lang.Integer CHK_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByRvCode(java.lang.Integer AP_RV_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApRecurringVoucher rv, IN(rv.apDistributionRecords) dr WHERE rv.rvCode =?1 AND dr.drAdCompany=?2");
			query.setParameter(1, AP_RV_CODE);
			query.setParameter(2, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findByRvCode(java.lang.Integer AP_RV_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByPoCode(java.lang.Integer PO_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApPurchaseOrder po, IN(po.apDistributionRecords) dr WHERE po.poCode =?1 AND dr.drAdCompany=?2 ORDER BY dr.drDebit DESC");
			query.setParameter(1, PO_CODE);
			query.setParameter(2, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findByPoCode(java.lang.Integer PO_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findVouByDateRangeAndCoaAccountNumber(byte VOU_DBT_MMO, java.util.Date VOU_DT_FRM,
			java.util.Date VOU_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApVoucher vou, IN(vou.apDistributionRecords) dr WHERE vou.vouPosted=1 AND vou.vouDebitMemo=?1 AND vou.vouDate>=?2 AND vou.vouDate<=?3 AND (dr.glChartOfAccount.coaCode=?4 OR vou.apTaxCode.tcType IN ('EXEMPT','ZERO-RATED')) AND dr.drAdCompany=?5");
			query.setParameter(1, VOU_DBT_MMO);
			query.setParameter(2, VOU_DT_FRM);
			query.setParameter(3, VOU_DT_TO);
			query.setParameter(4, COA_CODE);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findVouByDateRangeAndCoaAccountNumber(byte VOU_DBT_MMO, java.com.util.Date VOU_DT_FRM, java.com.util.Date VOU_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findChkByDateRangeAndCoaAccountNumber(java.util.Date CHK_DT_FRM,
			java.util.Date CHK_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApCheck chk, IN(chk.apDistributionRecords) dr WHERE chk.chkPosted=1 AND chk.chkDate>=?1 AND chk.chkDate<=?2 AND dr.glChartOfAccount.coaCode=?3 AND dr.drAdCompany=?4");
			query.setParameter(1, CHK_DT_FRM);
			query.setParameter(2, CHK_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findChkByDateRangeAndCoaAccountNumber(java.com.util.Date CHK_DT_FRM, java.com.util.Date CHK_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPoByDateRangeAndCoaAccountNumber(java.util.Date PO_DT_FRM, java.util.Date PO_DT_TO,
			java.lang.Integer COA_CODE, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApPurchaseOrder po, IN(po.apDistributionRecords) dr WHERE po.poReceiving=1 AND po.poPosted=1 AND po.poDate>=?1 AND po.poDate<=?2 AND dr.glChartOfAccount.coaCode=?3 AND dr.drAdCompany=?4");
			query.setParameter(1, PO_DT_FRM);
			query.setParameter(2, PO_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findPoByDateRangeAndCoaAccountNumber(java.com.util.Date PO_DT_FRM, java.com.util.Date PO_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedVouByDateRangeAndCoaAccountNumber(byte VOU_DBT_MMO,
			java.util.Date VOU_DT_FRM, java.util.Date VOU_DT_TO, java.lang.Integer COA_CODE,
			java.lang.Integer VOU_AD_BRNCH, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApVoucher vou, IN(vou.apDistributionRecords) dr WHERE vou.vouVoid=0 AND vou.vouPosted=0 AND vou.vouDebitMemo=?1 AND vou.vouDate>=?2 AND vou.vouDate<=?3 AND dr.glChartOfAccount.coaCode=?4 AND vou.vouAdBranch=?5 AND dr.drAdCompany=?6");
			query.setParameter(1, VOU_DBT_MMO);
			query.setParameter(2, VOU_DT_FRM);
			query.setParameter(3, VOU_DT_TO);
			query.setParameter(4, COA_CODE);
			query.setParameter(5, VOU_AD_BRNCH);
			query.setParameter(6, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findUnpostedVouByDateRangeAndCoaAccountNumber(byte VOU_DBT_MMO, java.com.util.Date VOU_DT_FRM, java.com.util.Date VOU_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedChkByDateRangeAndCoaAccountNumber(java.util.Date CHK_DT_FRM,
			java.util.Date CHK_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer CHK_AD_BRNCH,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApCheck chk, IN(chk.apDistributionRecords) dr WHERE chk.chkVoid=0 AND chk.chkPosted=0 AND chk.chkDate>=?1 AND chk.chkDate<=?2 AND dr.glChartOfAccount.coaCode=?3 AND chk.chkAdBranch=?4 AND dr.drAdCompany=?5");
			query.setParameter(1, CHK_DT_FRM);
			query.setParameter(2, CHK_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, CHK_AD_BRNCH);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findUnpostedChkByDateRangeAndCoaAccountNumber(java.com.util.Date CHK_DT_FRM, java.com.util.Date CHK_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedPoByDateRangeAndCoaAccountNumber(java.util.Date PO_DT_FRM,
			java.util.Date PO_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer PO_AD_BRNCH,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApPurchaseOrder po, IN(po.apDistributionRecords) dr WHERE po.poVoid=0 AND po.poReceiving=1 AND po.poPosted=0 AND po.poDate>=?1 AND po.poDate<=?2 AND dr.glChartOfAccount.coaCode=?3 AND po.poAdBranch=?4 AND dr.drAdCompany=?5");
			query.setParameter(1, PO_DT_FRM);
			query.setParameter(2, PO_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, PO_AD_BRNCH);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findUnpostedPoByDateRangeAndCoaAccountNumber(java.com.util.Date PO_DT_FRM, java.com.util.Date PO_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer PO_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedVouByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE,
			java.lang.Integer VOU_AD_BRNCH, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApVoucher vou, IN(vou.apDistributionRecords) dr WHERE vou.vouPosted=0 AND dr.glChartOfAccount.coaCode=?1 AND vou.vouAdBranch = ?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, VOU_AD_BRNCH);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findUnpostedVouByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedChkByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE,
			java.lang.Integer CHK_AD_BRNCH, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApCheck chk, IN(chk.apDistributionRecords) dr WHERE chk.chkPosted=0 AND dr.glChartOfAccount.coaCode=?1 AND chk.chkAdBranch = ?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, CHK_AD_BRNCH);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findUnpostedChkByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedPoByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE,
			java.lang.Integer PO_AD_BRNCH, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApPurchaseOrder po, IN(po.apDistributionRecords) dr WHERE po.poReceiving=1 AND po.poPosted=0 AND dr.glChartOfAccount.coaCode=?1 AND po.poAdBranch = ?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, PO_AD_BRNCH);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findUnpostedPoByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE, java.lang.Integer PO_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findVouByDateAndCoaAccountNumberAndCurrencyAndVouPosted(java.util.Date VOU_DT,
			java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte VOU_PSTD, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApVoucher vou, IN(vou.apDistributionRecords) dr WHERE vou.vouDebitMemo=0 AND vou.vouDate<=?1 AND dr.glChartOfAccount.coaCode=?2 AND vou.glFunctionalCurrency.fcCode=?3 AND vou.vouPosted=?4 AND vou.vouVoid=0 AND dr.drAdCompany=?5");
			query.setParameter(1, VOU_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, FC_CODE);
			query.setParameter(4, VOU_PSTD);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findVouByDateAndCoaAccountNumberAndCurrencyAndVouPosted(java.com.util.Date VOU_DT, java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte VOU_PSTD, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findVouDebitMemoByDateAndCoaAccountNumberAndCurrencyAndVouPosted(java.util.Date VOU_DT,
			java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte VOU_PSTD, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApVoucher dm, IN(dm.apDistributionRecords) dr, ApVoucher vou WHERE dm.vouDebitMemo=1 AND dm.vouDmVoucherNumber=vou.vouDocumentNumber AND dm.vouDate<=?1 AND dr.glChartOfAccount.coaCode=?2 AND vou.glFunctionalCurrency.fcCode=?3 AND dm.vouPosted=?4 AND dm.vouVoid=0 AND dr.drAdCompany=?5");
			query.setParameter(1, VOU_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, FC_CODE);
			query.setParameter(4, VOU_PSTD);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findVouDebitMemoByDateAndCoaAccountNumberAndCurrencyAndVouPosted(java.com.util.Date VOU_DT, java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte VOU_PSTD, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findChkByDateAndCoaAccountNumberAndCurrencyAndChkPosted(java.util.Date CHK_DT,
			java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte CHK_PSTD, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApCheck chk, IN(chk.apDistributionRecords) dr, IN(chk.apAppliedVouchers) av  WHERE chk.chkType='PAYMENT' AND chk.chkDate<=?1 AND dr.glChartOfAccount.coaCode=?2 AND chk.glFunctionalCurrency.fcCode=?3 AND chk.chkPosted=?4 AND chk.chkVoid=0 AND dr.drAdCompany=?5");
			query.setParameter(1, CHK_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, FC_CODE);
			query.setParameter(4, CHK_PSTD);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findChkByDateAndCoaAccountNumberAndCurrencyAndChkPosted(java.com.util.Date CHK_DT, java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte CHK_PSTD, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findChkDirectByDateAndCoaAccountNumberAndCurrencyAndChkPosted(java.util.Date CHK_DT,
			java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte CHK_PSTD, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApCheck chk, IN(chk.apDistributionRecords) dr WHERE chk.chkType='DIRECT' AND chk.chkDate<=?1 AND dr.glChartOfAccount.coaCode=?2 AND chk.glFunctionalCurrency.fcCode=?3 AND chk.chkPosted=?4 AND chk.chkVoid=0 AND dr.drAdCompany=?5");
			query.setParameter(1, CHK_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, FC_CODE);
			query.setParameter(4, CHK_PSTD);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findChkDirectByDateAndCoaAccountNumberAndCurrencyAndChkPosted(java.com.util.Date CHK_DT, java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte CHK_PSTD, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPoByDateAndCoaAccountNumberAndCurrencyAndPoPosted(java.util.Date PO_DT,
			java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte PO_PSTD, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApPurchaseOrder po, IN(po.apDistributionRecords) dr WHERE po.poReceiving=1 AND po.poDate<=?1 AND dr.glChartOfAccount.coaCode=?2 AND po.glFunctionalCurrency.fcCode=?3 AND po.poPosted=?4 AND po.poVoid=0 AND dr.drAdCompany=?5");
			query.setParameter(1, PO_DT);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, FC_CODE);
			query.setParameter(4, PO_PSTD);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findPoByDateAndCoaAccountNumberAndCurrencyAndPoPosted(java.com.util.Date PO_DT, java.lang.Integer COA_CODE, java.lang.Integer FC_CODE, byte PO_PSTD, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedVouByDateAndCoaAccountNumber(byte VOU_DBT_MMO, java.util.Date VOU_DT_TO,
			java.lang.Integer COA_CODE, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApVoucher vou, IN(vou.apDistributionRecords) dr WHERE vou.vouVoid=0 AND vou.vouPosted=0 AND vou.vouDebitMemo=?1 AND vou.vouDate<=?2 AND dr.glChartOfAccount.coaCode=?3 AND vou.vouAdBranch=?4 AND dr.drAdCompany=?5");
			query.setParameter(1, VOU_DBT_MMO);
			query.setParameter(2, VOU_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, VOU_AD_BRNCH);
			query.setParameter(5, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findUnpostedVouByDateAndCoaAccountNumber(byte VOU_DBT_MMO, java.com.util.Date VOU_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedChkByDateAndCoaAccountNumber(java.util.Date CHK_DT_TO,
			java.lang.Integer COA_CODE, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApCheck chk, IN(chk.apDistributionRecords) dr WHERE chk.chkVoid=0 AND chk.chkPosted=0 AND chk.chkDate<=?1 AND dr.glChartOfAccount.coaCode=?2 AND chk.chkAdBranch=?3 AND dr.drAdCompany=?4");
			query.setParameter(1, CHK_DT_TO);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, CHK_AD_BRNCH);
			query.setParameter(4, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findUnpostedChkByDateAndCoaAccountNumber(java.com.util.Date CHK_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedPoByDateAndCoaAccountNumber(java.util.Date PO_DT_TO,
			java.lang.Integer COA_CODE, java.lang.Integer PO_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApPurchaseOrder po, IN(po.apDistributionRecords) dr WHERE po.poVoid=0 AND po.poReceiving=1 AND po.poPosted=0 AND po.poDate<=?1 AND dr.glChartOfAccount.coaCode=?2 AND po.poAdBranch=?3 AND dr.drAdCompany=?4");
			query.setParameter(1, PO_DT_TO);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, PO_AD_BRNCH);
			query.setParameter(4, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findUnpostedPoByDateAndCoaAccountNumber(java.com.util.Date PO_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer PO_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findChkByDateAndCoaAccountDescriptionAndSupplier(java.util.Date CHK_DT_TO,
			java.lang.Integer SPL_CODE, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM ApCheck chk, IN(chk.apDistributionRecords) dr WHERE chk.chkVoid=0 AND chk.chkPosted=1 AND chk.chkType='DIRECT' AND chk.chkDate<=?1 AND dr.glChartOfAccount.coaApTag=1 AND chk.apSupplier.splCode=?2 AND chk.chkAdBranch=?3 AND dr.drAdCompany=?4");
			query.setParameter(1, CHK_DT_TO);
			query.setParameter(2, SPL_CODE);
			query.setParameter(3, CHK_AD_BRNCH);
			query.setParameter(4, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApDistributionRecordHome.findChkByDateAndCoaAccountDescriptionAndSupplier(java.com.util.Date CHK_DT_TO, java.lang.Integer SPL_CODE, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalApDistributionRecord create(Integer AP_DR_CODE, short DR_LN, String DR_CLSS, double DR_AMNT,
                                            byte DR_DBT, byte DR_IMPRTD, byte DR_RVRSD, Integer DR_AD_CMPNY) throws CreateException {
		try {

			LocalApDistributionRecord entity = new LocalApDistributionRecord();

			Debug.print("ApDistributionRecordBean create");
			entity.setDrLine(DR_LN);
			entity.setDrClass(DR_CLSS);
			entity.setDrAmount(DR_AMNT);
			entity.setDrDebit(DR_DBT);
			entity.setDrImported(DR_IMPRTD);
			entity.setDrReversed(DR_RVRSD);
			entity.setDrAdCompany(DR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalApDistributionRecord create(short DR_LN, String DR_CLSS, double DR_AMNT, byte DR_DBT,
                                            byte DR_IMPRTD, byte DR_RVRSD, Integer DR_AD_CMPNY) throws CreateException {
		try {

			LocalApDistributionRecord entity = new LocalApDistributionRecord();

			Debug.print("ApDistributionRecordBean create");
			entity.setDrLine(DR_LN);
			entity.setDrClass(DR_CLSS);
			entity.setDrAmount(DR_AMNT);
			entity.setDrDebit(DR_DBT);
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