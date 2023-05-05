package com.ejb.dao.ap;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApVoucher;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalApVoucherHome {

	public static final String JNDI_NAME = "LocalApVoucherHome!com.ejb.ap.LocalApVoucherHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalApVoucherHome() {
	}

	// FINDER METHODS

	public LocalApVoucher findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalApVoucher entity = (LocalApVoucher) em.find(new LocalApVoucher(), pk);
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

	public LocalApVoucher findByVouDocumentNumberAndVouDebitMemoAndBrCode(java.lang.String VOU_DCMNT_NMBR,
			byte VOU_DBT_MMO, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.vouDocumentNumber = ?1 AND vou.vouDebitMemo = ?2 AND vou.vouAdBranch = ?3 AND vou.vouAdCompany = ?4");
			query.setParameter(1, VOU_DCMNT_NMBR);
			query.setParameter(2, VOU_DBT_MMO);
			query.setParameter(3, VOU_AD_BRNCH);
			query.setParameter(4, VOU_AD_CMPNY);
            return (LocalApVoucher) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(java.lang.String VOU_DCMNT_NMBR, byte VOU_DBT_MMO, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(java.lang.String VOU_DCMNT_NMBR, byte VOU_DBT_MMO, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalApVoucher findByVouDocumentNumberAndVouDebitMemoAndSplSupplierCodeBrCode(
			java.lang.String VOU_DCMNT_NMBR, byte VOU_DBT_MMO, java.lang.String SPL_SPPLR_CODE,
			java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.vouDocumentNumber = ?1 AND vou.vouDebitMemo = ?2 AND vou.apSupplier.splSupplierCode = ?3 AND vou.vouAdBranch = ?4 AND vou.vouAdCompany = ?5");
			query.setParameter(1, VOU_DCMNT_NMBR);
			query.setParameter(2, VOU_DBT_MMO);
			query.setParameter(3, SPL_SPPLR_CODE);
			query.setParameter(4, VOU_AD_BRNCH);
			query.setParameter(5, VOU_AD_CMPNY);
            return (LocalApVoucher) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndSplSupplierCodeBrCode(java.lang.String VOU_DCMNT_NMBR, byte VOU_DBT_MMO, java.lang.String SPL_SPPLR_CODE, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndSplSupplierCodeBrCode(java.lang.String VOU_DCMNT_NMBR, byte VOU_DBT_MMO, java.lang.String SPL_SPPLR_CODE, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalApVoucher findByVouDocumentNumber(java.lang.String VOU_DCMNT_NMBR, java.lang.Integer VOU_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.vouDocumentNumber = ?1 AND vou.vouAdCompany = ?2");
			query.setParameter(1, VOU_DCMNT_NMBR);
			query.setParameter(2, VOU_AD_CMPNY);
            return (LocalApVoucher) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApVoucherHome.findByVouDocumentNumber(java.lang.String VOU_DCMNT_NMBR, java.lang.Integer VOU_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findByVouDocumentNumber(java.lang.String VOU_DCMNT_NMBR, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedVouByVouDebitMemoAndVouDateRange(byte VOU_DBT_MMO, java.util.Date VOU_DT_FRM,
			java.util.Date VOU_DT_TO, java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.vouPosted = 1 AND vou.vouDebitMemo = ?1 AND vou.vouDate >= ?2 AND vou.vouDate <= ?3 AND vou.vouAdCompany = ?4");
			query.setParameter(1, VOU_DBT_MMO);
			query.setParameter(2, VOU_DT_FRM);
			query.setParameter(3, VOU_DT_TO);
			query.setParameter(4, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findPostedVouByVouDebitMemoAndVouDateRange(byte VOU_DBT_MMO, java.com.util.Date VOU_DT_FRM, java.com.util.Date VOU_DT_TO, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedVouByVouDebitMemoAndVouDateRangeExemptZero(byte VOU_DBT_MMO,
			java.util.Date VOU_DT_FRM, java.util.Date VOU_DT_TO, java.lang.Integer VOU_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.vouPosted = 1 AND vou.vouDebitMemo = ?1 AND vou.vouDate >= ?2 AND vou.vouDate <= ?3 AND vou.apTaxCode.tcType IN ('EXEMPT','ZERO-RATED') AND vou.vouAdCompany = ?4");
			query.setParameter(1, VOU_DBT_MMO);
			query.setParameter(2, VOU_DT_FRM);
			query.setParameter(3, VOU_DT_TO);
			query.setParameter(4, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findPostedVouByVouDebitMemoAndVouDateRangeExemptZero(byte VOU_DBT_MMO, java.com.util.Date VOU_DT_FRM, java.com.util.Date VOU_DT_TO, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVouDebitMemoAndVouDmVoucherNumberAndVouVoid(java.lang.String VOU_DM_VCHR_NMBR,
			java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.vouDebitMemo = 1 AND vou.vouVoid = 0 AND vou.vouDmVoucherNumber = ?1 AND vou.vouAdBranch = ?2 AND vou.vouAdCompany = ?3");
			query.setParameter(1, VOU_DM_VCHR_NMBR);
			query.setParameter(2, VOU_AD_BRNCH);
			query.setParameter(3, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findByVouDebitMemoAndVouDmVoucherNumberAndVouVoid(java.lang.String VOU_DM_VCHR_NMBR, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVouDebitMemoAndVouDmVoucherNumberAndVouVoidAndVouPosted(byte VOU_DBT_MMO,
			java.lang.String VOU_DM_VCHR_NMBR, byte VOU_VD, byte VOU_PSTD, java.lang.Integer VOU_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.vouDebitMemo = ?1 AND vou.vouDmVoucherNumber = ?2 AND vou.vouVoid = ?3 AND vou.vouPosted = ?4 AND vou.vouAdCompany = ?5");
			query.setParameter(1, VOU_DBT_MMO);
			query.setParameter(2, VOU_DM_VCHR_NMBR);
			query.setParameter(3, VOU_VD);
			query.setParameter(4, VOU_PSTD);
			query.setParameter(5, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findByVouDebitMemoAndVouDmVoucherNumberAndVouVoidAndVouPosted(byte VOU_DBT_MMO, java.lang.String VOU_DM_VCHR_NMBR, byte VOU_VD, byte VOU_PSTD, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVouDateRangeAndWtcCode(java.util.Date VOU_DT_FRM, java.util.Date VOU_DT_TO,
			java.lang.Integer AP_WTC_CODE, java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.vouPosted = 1 AND vou.vouDebitMemo = 0 AND vou.vouDate >= ?1 AND vou.vouDate <= ?2 AND vou.apWithholdingTaxCode.wtcCode = ?3 AND vou.vouAdCompany = ?4");
			query.setParameter(1, VOU_DT_FRM);
			query.setParameter(2, VOU_DT_TO);
			query.setParameter(3, AP_WTC_CODE);
			query.setParameter(4, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findByVouDateRangeAndWtcCode(java.com.util.Date VOU_DT_FRM, java.com.util.Date VOU_DT_TO, java.lang.Integer AP_WTC_CODE, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVouPostedAndVouVoidAndVbName(byte VOU_PSTD, byte VOU_VD, java.lang.String VB_NM,
			java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou WHERE vou.vouPosted=?1 AND vou.vouVoid = ?2 AND vou.apVoucherBatch.vbName = ?3 AND vou.vouAdCompany = ?4");
			query.setParameter(1, VOU_PSTD);
			query.setParameter(2, VOU_VD);
			query.setParameter(3, VB_NM);
			query.setParameter(4, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findByVouPostedAndVouVoidAndVbName(byte VOU_PSTD, byte VOU_VD, java.lang.String VB_NM, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVbName(java.lang.String VB_NM, java.lang.Integer VOU_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.apVoucherBatch.vbName=?1 AND vou.vouAdCompany = ?2");
			query.setParameter(1, VB_NM);
			query.setParameter(2, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findByVbName(java.lang.String VB_NM, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftVouByVouTypeAndVouDebitMemoAndBrCode(java.lang.String VOU_TYP,
			byte VOU_DBT_MMO, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou WHERE vou.vouReasonForRejection IS NULL AND vou.vouApprovalStatus IS NULL AND vou.vouVoid = 0 AND vou.vouType = ?1 AND vou.vouDebitMemo = ?2 AND vou.vouPosted=0 AND vou.vouAdBranch = ?3 AND vou.vouAdCompany = ?4");
			query.setParameter(1, VOU_TYP);
			query.setParameter(2, VOU_DBT_MMO);
			query.setParameter(3, VOU_AD_BRNCH);
			query.setParameter(4, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findDraftVouByVouTypeAndVouDebitMemoAndBrCode(java.lang.String VOU_TYP, byte VOU_DBT_MMO, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftVouAndVouDebitMemoAndBrCode(byte VOU_DBT_MMO, java.lang.Integer VOU_AD_BRNCH,
			java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou WHERE vou.vouReasonForRejection IS NULL AND vou.vouApprovalStatus IS NULL AND vou.vouVoid = 0 AND vou.vouDebitMemo = ?1 AND vou.vouPosted=0 AND vou.vouAdBranch = ?2 AND vou.vouAdCompany = ?3");
			query.setParameter(1, VOU_DBT_MMO);
			query.setParameter(2, VOU_AD_BRNCH);
			query.setParameter(3, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findDraftVouAndVouDebitMemoAndBrCode(byte VOU_DBT_MMO, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftRequestVouByBrCode(java.lang.Integer VOU_AD_BRNCH,
			java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou WHERE vou.vouApprovalStatus IS NULL AND vou.vouPosted = 0 AND vou.vouVoid = 0 AND vou.vouType = 'REQUEST' AND vou.vouGenerated = 0 AND vou.vouAdBranch = ?1 AND vou.vouAdCompany = ?2");
			query.setParameter(1, VOU_AD_BRNCH);
			query.setParameter(2, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findDraftRequestVouByBrCode(java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findForGenerationRequestVouByBrCode(java.lang.Integer VOU_AD_BRNCH,
			java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou WHERE vou.vouApprovalStatus IS NOT NULL AND vou.vouPosted = 1 AND vou.vouVoid = 0 AND vou.vouType = 'REQUEST' AND vou.vouGenerated = 0 AND vou.vouAdBranch = ?1 AND vou.vouAdCompany = ?2");
			query.setParameter(1, VOU_AD_BRNCH);
			query.setParameter(2, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findForGenerationRequestVouByBrCode(java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findForProcessingRequestVouByBrCode(java.lang.Integer VOU_AD_BRNCH,
			java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou WHERE vou.vouApprovalStatus IS NOT NULL AND vou.vouPosted = 0 AND vou.vouVoid = 0 AND vou.vouType = 'REQUEST' AND vou.vouAmountPaid < vou.vouAmountDue AND vou.vouGenerated = 0 AND vou.vouAdBranch = ?1 AND vou.vouAdCompany = ?2");
			query.setParameter(1, VOU_AD_BRNCH);
			query.setParameter(2, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findForProcessingRequestVouByBrCode(java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedVouByVouDebitMemoAndVouDateRangeAndSplNameAndTcType(byte INV_CRDT_MMO,
			java.util.Date INV_DT_FRM, java.util.Date INV_DT_TO, java.lang.String SPL_NM, java.lang.String TC_TYP,
			java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.vouPosted = 1 AND vou.vouDebitMemo = ?1 AND vou.vouDate >= ?2 AND vou.vouDate <= ?3 AND vou.apSupplier.splName = ?4 AND vou.apTaxCode.tcType = ?5 AND vou.vouAdCompany = ?6");
			query.setParameter(1, INV_CRDT_MMO);
			query.setParameter(2, INV_DT_FRM);
			query.setParameter(3, INV_DT_TO);
			query.setParameter(4, SPL_NM);
			query.setParameter(5, TC_TYP);
			query.setParameter(6, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findPostedVouByVouDebitMemoAndVouDateRangeAndSplNameAndTcType(byte INV_CRDT_MMO, java.com.util.Date INV_DT_FRM, java.com.util.Date INV_DT_TO, java.lang.String SPL_NM, java.lang.String TC_TYP, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedVouByVouDateRange(java.util.Date VOU_DT_FRM, java.util.Date VOU_DT_TO,
			java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.vouPosted = 0 AND vou.vouVoid = 0 AND vou.vouDate >= ?1 AND vou.vouDate <= ?2 AND vou.vouAdCompany = ?3");
			query.setParameter(1, VOU_DT_FRM);
			query.setParameter(2, VOU_DT_TO);
			query.setParameter(3, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findUnpostedVouByVouDateRange(java.com.util.Date VOU_DT_FRM, java.com.util.Date VOU_DT_TO, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVouReferenceNumber(java.lang.String VOU_RFRNC_NMBR,
			java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.vouReferenceNumber = ?1 AND vou.vouAdBranch = ?2 AND vou.vouAdCompany = ?3");
			query.setParameter(1, VOU_RFRNC_NMBR);
			query.setParameter(2, VOU_AD_BRNCH);
			query.setParameter(3, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findByVouReferenceNumber(java.lang.String VOU_RFRNC_NMBR, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVouAll(byte VOU_DBT_MMO, java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.vouDebitMemo = ?1 AND vou.vouAdCompany = ?2");
			query.setParameter(1, VOU_DBT_MMO);
			query.setParameter(2, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findByVouAll(byte VOU_DBT_MMO, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVouDebitMemoAndVouVbNameVouAll(byte VOU_DBT_MMO, java.lang.String VB_NM,
			java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.vouDebitMemo = ?1 AND vou.apVoucherBatch.vbName=?2 AND vou.vouAdCompany = ?3");
			query.setParameter(1, VOU_DBT_MMO);
			query.setParameter(2, VB_NM);
			query.setParameter(3, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findByVouDebitMemoAndVouVbNameVouAll(byte VOU_DBT_MMO, java.lang.String VB_NM, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVouReferenceNumberAndVouPostedAndSplName(java.lang.String VOU_RFRNC_NMBR,
			byte VOU_PSTD, java.lang.String SPL_NM, java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.vouReferenceNumber = ?1 AND vou.vouPosted = ?2 AND vou.apSupplier.splName = ?3 AND vou.vouAdCompany = ?4");
			query.setParameter(1, VOU_RFRNC_NMBR);
			query.setParameter(2, VOU_PSTD);
			query.setParameter(3, SPL_NM);
			query.setParameter(4, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findByVouReferenceNumberAndVouPostedAndSplName(java.lang.String VOU_RFRNC_NMBR, byte VOU_PSTD, java.lang.String SPL_NM, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBeforeOrEqualVouDateAndSplCode(java.util.Date VOU_DT, java.lang.Integer SPL_CODE,
			java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.vouDescription = 'Interest Income' AND vou.vouReferenceNumber LIKE 'INT-%' AND vou.vouDate <= ?1 AND vou.apSupplier.splCode = ?2 AND vou.vouAdCompany = ?3");
			query.setParameter(1, VOU_DT);
			query.setParameter(2, SPL_CODE);
			query.setParameter(3, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findByBeforeOrEqualVouDateAndSplCode(java.com.util.Date VOU_DT, java.lang.Integer SPL_CODE, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVouReferenceNumberAndSplName(java.lang.String VOU_RFRNC_NMBR,
			java.lang.String SPL_NM, java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.vouReferenceNumber = ?1 AND vou.apSupplier.splName = ?2 AND vou.vouAdCompany = ?3");
			query.setParameter(1, VOU_RFRNC_NMBR);
			query.setParameter(2, SPL_NM);
			query.setParameter(3, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findByVouReferenceNumberAndSplName(java.lang.String VOU_RFRNC_NMBR, java.lang.String SPL_NM, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVouRejectedCPRByByBrCodeAndVouCreatedBy(java.lang.Integer AD_BRNCH,
			java.lang.String VOU_CRTD_BY, java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou  WHERE vou.vouType = 'REQUEST' AND vou.vouReasonForRejection IS NOT NULL AND vou.vouAdBranch = ?1 AND vou.vouCreatedBy = ?2 AND vou.vouAdCompany = ?3");
			query.setParameter(1, AD_BRNCH);
			query.setParameter(2, VOU_CRTD_BY);
			query.setParameter(3, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findByVouRejectedCPRByByBrCodeAndVouCreatedBy(java.lang.Integer AD_BRNCH, java.lang.String VOU_CRTD_BY, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findRejectedVouByBrCodeAndPoCreatedBy(java.lang.Integer vou_AD_BRNCH,
			java.lang.String vou_CRTD_BY, java.lang.Integer vou_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou WHERE vou.vouVoid = 0 AND vou.vouReasonForRejection IS NOT NULL AND vou.vouAdBranch = ?1 AND vou.vouCreatedBy=?2 AND vou.vouAdCompany = ?3");
			query.setParameter(1, vou_AD_BRNCH);
			query.setParameter(2, vou_CRTD_BY);
			query.setParameter(3, vou_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findRejectedVouByBrCodeAndPoCreatedBy(java.lang.Integer vou_AD_BRNCH, java.lang.String vou_CRTD_BY, java.lang.Integer vou_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnPostedVouByVouDateRangeAndSplCode(byte VOU_DBT_MMO, java.util.Date VOU_DT_FRM,
			java.util.Date VOU_DT_TO, java.lang.Integer SPL_CODE, java.lang.Integer VOU_AD_BRNCH,
			java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou WHERE vou.vouPosted = 0 AND vou.vouDebitMemo = ?1 AND vou.vouDate >= ?2 AND vou.vouDate <= ?3 AND vou.apSupplier.splCode = ?4 AND vou.vouAdBranch = ?5 AND vou.vouAdCompany = ?6 ORDER BY vou.vouDate, vou.vouDocumentNumber");
			query.setParameter(1, VOU_DBT_MMO);
			query.setParameter(2, VOU_DT_FRM);
			query.setParameter(3, VOU_DT_TO);
			query.setParameter(4, SPL_CODE);
			query.setParameter(5, VOU_AD_BRNCH);
			query.setParameter(6, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findUnPostedVouByVouDateRangeAndSplCode(byte VOU_DBT_MMO, java.com.util.Date VOU_DT_FRM, java.com.util.Date VOU_DT_TO, java.lang.Integer SPL_CODE, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedVouByVouDateRangeAndSplCode(byte VOU_DBT_MMO, java.util.Date VOU_DT_FRM,
			java.util.Date VOU_DT_TO, java.lang.Integer SPL_CODE, java.lang.Integer VOU_AD_BRNCH,
			java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou WHERE vou.vouPosted = 1 AND vou.vouDebitMemo = ?1 AND vou.vouDate >= ?2 AND vou.vouDate <= ?3 AND vou.apSupplier.splCode = ?4 AND vou.vouAdBranch = ?5 AND vou.vouAdCompany = ?6 ORDER BY vou.vouDate, vou.vouDocumentNumber");
			query.setParameter(1, VOU_DBT_MMO);
			query.setParameter(2, VOU_DT_FRM);
			query.setParameter(3, VOU_DT_TO);
			query.setParameter(4, SPL_CODE);
			query.setParameter(5, VOU_AD_BRNCH);
			query.setParameter(6, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findPostedVouByVouDateRangeAndSplCode(byte VOU_DBT_MMO, java.com.util.Date VOU_DT_FRM, java.com.util.Date VOU_DT_TO, java.lang.Integer SPL_CODE, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedTaxVouByVouDateRange(byte VOU_DBT_MMO, java.util.Date VOU_DT_FRM,
			java.util.Date VOU_DT_TO, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou WHERE vou.apSupplier.apSupplierClass.scName NOT IN ('Officers and Employees') AND vou.vouType NOT IN ('ITEMS') AND vou.vouPosted = 1 AND vou.apTaxCode.tcType IN ('EXEMPT','ZERO-RATED','INCLUSIVE','EXCLUSIVE') AND vou.vouDebitMemo = ?1 AND vou.vouDate >= ?2 AND vou.vouDate <= ?3 AND vou.vouAdBranch = ?4 AND vou.vouAdCompany = ?5 ORDER BY vou.vouDate, vou.vouDocumentNumber");
			query.setParameter(1, VOU_DBT_MMO);
			query.setParameter(2, VOU_DT_FRM);
			query.setParameter(3, VOU_DT_TO);
			query.setParameter(4, VOU_AD_BRNCH);
			query.setParameter(5, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findPostedTaxVouByVouDateRange(byte VOU_DBT_MMO, java.com.util.Date VOU_DT_FRM, java.com.util.Date VOU_DT_TO, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedTaxVouByVouOaeDateRange(byte VOU_DBT_MMO, java.util.Date VOU_DT_FRM,
			java.util.Date VOU_DT_TO, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou WHERE vou.apSupplier.apSupplierClass.scName = 'Officers and Employees' AND vou.vouType = 'ITEMS' AND vou.vouPosted = 1 AND vou.apTaxCode.tcType IN ('EXEMPT','ZERO-RATED','INCLUSIVE','EXCLUSIVE') AND vou.vouDebitMemo = ?1 AND vou.vouDate >= ?2 AND vou.vouDate <= ?3 AND vou.vouAdBranch = ?4 AND vou.vouAdCompany = ?5 ORDER BY vou.vouDate, vou.vouDocumentNumber");
			query.setParameter(1, VOU_DBT_MMO);
			query.setParameter(2, VOU_DT_FRM);
			query.setParameter(3, VOU_DT_TO);
			query.setParameter(4, VOU_AD_BRNCH);
			query.setParameter(5, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findPostedTaxVouByVouOaeDateRange(byte VOU_DBT_MMO, java.com.util.Date VOU_DT_FRM, java.com.util.Date VOU_DT_TO, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findVouForLoanGeneration(java.lang.Integer VOU_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vou) FROM ApVoucher vou WHERE vou.vouDebitMemo = 0 AND vou.vouAmountDue <= vou.vouAmountPaid AND vou.vouPosted = 1 AND vou.vouType = 'ITEMS' AND vou.vouLoan = 1 AND vou.vouLoanGenerated = 0 AND vou.vouAdBranch = ?1 AND vou.vouAdCompany = ?2");
			query.setParameter(1, VOU_AD_BRNCH);
			query.setParameter(2, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherHome.findVouForLoanGeneration(java.lang.Integer VOU_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getVouByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
			java.lang.Integer LIMIT, java.lang.Integer OFFSET) throws FinderException {

		try {
			Query query = em.createQuery(jbossQl);
			int cnt = 1;
			for (Object data : args) {
				query.setParameter(cnt, data);
				cnt++;
			}
			if(LIMIT>0){query.setMaxResults(LIMIT);}
            query.setFirstResult(OFFSET);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalApVoucher create(Integer VOU_CODE, byte VOU_DBT_MMO, String VOU_DESC, Date VOU_DT,
                                 String VOU_DCMNT_NMBR, String VOU_RFRNC_NMBR, String VOU_DM_VCHR_NMBR, Date VOU_CNVRSN_DT,
                                 double VOU_CNVRSN_RT, double VOU_BLL_AMNT, double VOU_AMNT_DUE, double VOU_AMNT_PD,
                                 String VOU_APPRVL_STATUS, String VOU_RSN_FR_RJCTN, byte VOU_PSTD, byte VOU_GNRTD, byte VOU_VD,
                                 String VOU_CRTD_BY, Date VOU_DT_CRTD, String VOU_LST_MDFD_BY, Date VOU_DT_LST_MDFD,
                                 String VOU_APPRVD_RJCTD_BY, Date VOU_DT_APPRVD_RJCTD, String VOU_PSTD_BY, Date VOU_DT_PSTD, byte VOU_PRNTD,
                                 String VOU_PO_NMBR, byte VOU_LN, byte VOU_LN_GNRTD, Integer VOU_AD_BRNCH, Integer VOU_AD_CMPNY)
			throws CreateException {
		try {

			LocalApVoucher entity = new LocalApVoucher();

			Debug.print("ApVoucherBean create");
			entity.setVouCode(VOU_CODE);
			entity.setVouDebitMemo(VOU_DBT_MMO);
			entity.setVouDescription(VOU_DESC);
			entity.setVouDate(VOU_DT);
			entity.setVouDocumentNumber(VOU_DCMNT_NMBR);
			entity.setVouReferenceNumber(VOU_RFRNC_NMBR);
			entity.setVouDmVoucherNumber(VOU_DM_VCHR_NMBR);
			entity.setVouConversionDate(VOU_CNVRSN_DT);
			entity.setVouConversionRate(VOU_CNVRSN_RT);
			entity.setVouBillAmount(VOU_BLL_AMNT);
			entity.setVouAmountDue(VOU_AMNT_DUE);
			entity.setVouAmountPaid(VOU_AMNT_PD);
			entity.setVouApprovalStatus(VOU_APPRVL_STATUS);
			entity.setVouReasonForRejection(VOU_RSN_FR_RJCTN);
			entity.setVouPosted(VOU_PSTD);
			entity.setVouGenerated(VOU_GNRTD);
			entity.setVouVoid(VOU_VD);
			entity.setVouCreatedBy(VOU_CRTD_BY);
			entity.setVouDateCreated(VOU_DT_CRTD);
			entity.setVouLastModifiedBy(VOU_LST_MDFD_BY);
			entity.setVouDateLastModified(VOU_DT_LST_MDFD);
			entity.setVouApprovedRejectedBy(VOU_APPRVD_RJCTD_BY);
			entity.setVouDateApprovedRejected(VOU_DT_APPRVD_RJCTD);
			entity.setVouPostedBy(VOU_PSTD_BY);
			entity.setVouDatePosted(VOU_DT_PSTD);
			entity.setVouPrinted(VOU_PRNTD);
			entity.setVouPoNumber(VOU_PO_NMBR);
			entity.setVouLoan(VOU_LN);
			entity.setVouLoanGenerated(VOU_LN_GNRTD);
			entity.setVouAdBranch(VOU_AD_BRNCH);
			entity.setVouAdCompany(VOU_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalApVoucher create(byte VOU_DBT_MMO, String VOU_DESC, Date VOU_DT, String VOU_DCMNT_NMBR,
                                 String VOU_RFRNC_NMBR, String VOU_DM_VCHR_NMBR, Date VOU_CNVRSN_DT, double VOU_CNVRSN_RT,
                                 double VOU_BLL_AMNT, double VOU_AMNT_DUE, double VOU_AMNT_PD, String VOU_APPRVL_STATUS,
                                 String VOU_RSN_FR_RJCTN, byte VOU_PSTD, byte VOU_GNRTD, byte VOU_VD, String VOU_CRTD_BY, Date VOU_DT_CRTD,
                                 String VOU_LST_MDFD_BY, Date VOU_DT_LST_MDFD, String VOU_APPRVD_RJCTD_BY, Date VOU_DT_APPRVD_RJCTD,
                                 String VOU_PSTD_BY, Date VOU_DT_PSTD, byte VOU_PRNTD, String VOU_PO_NMBR, byte VOU_LN, byte VOU_LN_GNRTD,
                                 Integer VOU_AD_BRNCH, Integer VOU_AD_CMPNY) throws CreateException {
		try {

			LocalApVoucher entity = new LocalApVoucher();

			Debug.print("ApVoucherBean create");
			entity.setVouDebitMemo(VOU_DBT_MMO);
			entity.setVouDescription(VOU_DESC);
			entity.setVouDate(VOU_DT);
			entity.setVouDocumentNumber(VOU_DCMNT_NMBR);
			entity.setVouReferenceNumber(VOU_RFRNC_NMBR);
			entity.setVouDmVoucherNumber(VOU_DM_VCHR_NMBR);
			entity.setVouConversionDate(VOU_CNVRSN_DT);
			entity.setVouConversionRate(VOU_CNVRSN_RT);
			entity.setVouBillAmount(VOU_BLL_AMNT);
			entity.setVouAmountDue(VOU_AMNT_DUE);
			entity.setVouAmountPaid(VOU_AMNT_PD);
			entity.setVouApprovalStatus(VOU_APPRVL_STATUS);
			entity.setVouReasonForRejection(VOU_RSN_FR_RJCTN);
			entity.setVouPosted(VOU_PSTD);
			entity.setVouGenerated(VOU_GNRTD);
			entity.setVouVoid(VOU_VD);
			entity.setVouCreatedBy(VOU_CRTD_BY);
			entity.setVouDateCreated(VOU_DT_CRTD);
			entity.setVouLastModifiedBy(VOU_LST_MDFD_BY);
			entity.setVouDateLastModified(VOU_DT_LST_MDFD);
			entity.setVouApprovedRejectedBy(VOU_APPRVD_RJCTD_BY);
			entity.setVouDateApprovedRejected(VOU_DT_APPRVD_RJCTD);
			entity.setVouPostedBy(VOU_PSTD_BY);
			entity.setVouDatePosted(VOU_DT_PSTD);
			entity.setVouPrinted(VOU_PRNTD);
			entity.setVouPoNumber(VOU_PO_NMBR);
			entity.setVouLoan(VOU_LN);
			entity.setVouLoanGenerated(VOU_LN_GNRTD);
			entity.setVouAdBranch(VOU_AD_BRNCH);
			entity.setVouAdCompany(VOU_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalApVoucher create(String VOU_TYP, byte VOU_DBT_MMO, String VOU_DESC, Date VOU_DT,
                                 String VOU_DCMNT_NMBR, String VOU_RFRNC_NMBR, String VOU_DM_VCHR_NMBR, Date VOU_CNVRSN_DT,
                                 double VOU_CNVRSN_RT, double VOU_BLL_AMNT, double VOU_AMNT_DUE, double VOU_AMNT_PD,
                                 String VOU_APPRVL_STATUS, String VOU_RSN_FR_RJCTN, byte VOU_PSTD, byte VOU_GNRTD, byte VOU_VD,
                                 String VOU_CRTD_BY, Date VOU_DT_CRTD, String VOU_LST_MDFD_BY, Date VOU_DT_LST_MDFD,
                                 String VOU_APPRVD_RJCTD_BY, Date VOU_DT_APPRVD_RJCTD, String VOU_PSTD_BY, Date VOU_DT_PSTD, byte VOU_PRNTD,
                                 String VOU_PO_NMBR, byte VOU_LN, byte VOU_LN_GNRTD, Integer VOU_AD_BRNCH, Integer VOU_AD_CMPNY)
			throws CreateException {
		try {

			LocalApVoucher entity = new LocalApVoucher();

			Debug.print("ApVoucherBean create");

			entity.setVouType(VOU_TYP);
			entity.setVouDebitMemo(VOU_DBT_MMO);
			entity.setVouDescription(VOU_DESC);
			entity.setVouDate(VOU_DT);
			entity.setVouDocumentNumber(VOU_DCMNT_NMBR);
			entity.setVouReferenceNumber(VOU_RFRNC_NMBR);
			entity.setVouDmVoucherNumber(VOU_DM_VCHR_NMBR);
			entity.setVouConversionDate(VOU_CNVRSN_DT);
			entity.setVouConversionRate(VOU_CNVRSN_RT);
			entity.setVouBillAmount(VOU_BLL_AMNT);
			entity.setVouAmountDue(VOU_AMNT_DUE);
			entity.setVouAmountPaid(VOU_AMNT_PD);
			entity.setVouApprovalStatus(VOU_APPRVL_STATUS);
			entity.setVouReasonForRejection(VOU_RSN_FR_RJCTN);
			entity.setVouPosted(VOU_PSTD);
			entity.setVouGenerated(VOU_GNRTD);
			entity.setVouVoid(VOU_VD);
			entity.setVouCreatedBy(VOU_CRTD_BY);
			entity.setVouDateCreated(VOU_DT_CRTD);
			entity.setVouLastModifiedBy(VOU_LST_MDFD_BY);
			entity.setVouDateLastModified(VOU_DT_LST_MDFD);
			entity.setVouApprovedRejectedBy(VOU_APPRVD_RJCTD_BY);
			entity.setVouDateApprovedRejected(VOU_DT_APPRVD_RJCTD);
			entity.setVouPostedBy(VOU_PSTD_BY);
			entity.setVouDatePosted(VOU_DT_PSTD);
			entity.setVouPrinted(VOU_PRNTD);
			entity.setVouPoNumber(VOU_PO_NMBR);
			entity.setVouLoan(VOU_LN);
			entity.setVouLoanGenerated(VOU_LN_GNRTD);
			entity.setVouAdBranch(VOU_AD_BRNCH);
			entity.setVouAdCompany(VOU_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}