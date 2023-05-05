package com.ejb.dao.ap;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApCheck;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalApCheckHome {

	public static final String JNDI_NAME = "LocalApCheckHome!com.ejb.ap.LocalApCheckHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalApCheckHome() {
	}

	// FINDER METHODS

	public LocalApCheck findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalApCheck entity = (LocalApCheck) em.find(new LocalApCheck(), pk);
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

	public LocalApCheck findByChkDocumentNumberAndBrCode(java.lang.String CHK_DCMNT_NMBR,
			java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk  WHERE chk.chkDocumentNumber = ?1 AND chk.chkAdBranch = ?2 AND chk.chkAdCompany = ?3");
			query.setParameter(1, CHK_DCMNT_NMBR);
			query.setParameter(2, CHK_AD_BRNCH);
			query.setParameter(3, CHK_AD_CMPNY);
            return (LocalApCheck) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApCheckHome.findByChkDocumentNumberAndBrCode(java.lang.String CHK_DCMNT_NMBR, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findByChkDocumentNumberAndBrCode(java.lang.String CHK_DCMNT_NMBR, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDirectChkForAccruedInterestISGeneration(java.lang.Integer CHK_AD_BRNCH,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk  WHERE chk.chkType = 'DIRECT' AND chk.chkInvtInscribedStock = 1 AND chk.chkVoid = 0 AND chk.chkAdBranch = ?1 AND chk.chkAdCompany = ?2");
			query.setParameter(1, CHK_AD_BRNCH);
			query.setParameter(2, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findDirectChkForAccruedInterestISGeneration(java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDirectChkForAccruedInterestTBGeneration(java.lang.Integer CHK_AD_BRNCH,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk  WHERE chk.chkType = 'DIRECT' AND chk.chkInvtTreasuryBill = 1 AND chk.chkVoid = 0 AND chk.chkAdBranch = ?1 AND chk.chkAdCompany = ?2");
			query.setParameter(1, CHK_AD_BRNCH);
			query.setParameter(2, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findDirectChkForAccruedInterestTBGeneration(java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDirectChkForLoanInterestGeneration(java.lang.Integer CHK_AD_BRNCH,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk  WHERE chk.chkPosted = 1 AND chk.chkType = 'DIRECT' AND chk.chkLoan = 1 AND chk.chkLoanGenerated = 0 AND chk.chkVoid = 0 AND chk.chkAdBranch = ?1 AND chk.chkAdCompany = ?2");
			query.setParameter(1, CHK_AD_BRNCH);
			query.setParameter(2, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findDirectChkForLoanInterestGeneration(java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalApCheck findByChkNumberAndBaName(java.lang.String CHK_NMBR, java.lang.String BA_NM,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk  WHERE chk.chkNumber = ?1 AND chk.adBankAccount.baName = ?2 AND chk.chkAdCompany = ?3");
			query.setParameter(1, CHK_NMBR);
			query.setParameter(2, BA_NM);
			query.setParameter(3, CHK_AD_CMPNY);
            return (LocalApCheck) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApCheckHome.findByChkNumberAndBaName(java.lang.String CHK_NMBR, java.lang.String BA_NM, java.lang.Integer CHK_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findByChkNumberAndBaName(java.lang.String CHK_NMBR, java.lang.String BA_NM, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedChkByChkDateRange(java.util.Date CHK_DT_FRM, java.util.Date CHK_DT_TO,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk  WHERE chk.chkPosted = 1 AND chk.chkDate >= ?1 AND chk.chkDate <= ?2 AND chk.chkAdCompany = ?3");
			query.setParameter(1, CHK_DT_FRM);
			query.setParameter(2, CHK_DT_TO);
			query.setParameter(3, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findPostedChkByChkDateRange(java.com.util.Date CHK_DT_FRM, java.com.util.Date CHK_DT_TO, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findVoidPostedChkByChkDateRange(java.util.Date CHK_DT_FRM, java.util.Date CHK_DT_TO,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk  WHERE chk.chkPosted = 1 AND chk.chkVoid = 1 AND chk.chkVoidPosted = 1 AND chk.chkDate >= ?1 AND chk.chkDate <= ?2 AND chk.chkAdCompany = ?3");
			query.setParameter(1, CHK_DT_FRM);
			query.setParameter(2, CHK_DT_TO);
			query.setParameter(3, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findVoidPostedChkByChkDateRange(java.com.util.Date CHK_DT_FRM, java.com.util.Date CHK_DT_TO, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnreconciledPostedChkByDateAndBaNameAndBrCode(java.util.Date DT,
			java.lang.String BA_NM, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk WHERE chk.chkReconciled = 0 AND chk.chkCheckDate <= ?1 AND chk.chkPosted = 1 AND chk.chkVoid = 0 AND chk.adBankAccount.baName = ?2 AND chk.chkAdBranch = ?3 AND chk.chkAdCompany = ?4");
			query.setParameter(1, DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, CHK_AD_BRNCH);
			query.setParameter(4, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findUnreconciledPostedChkByDateAndBaNameAndBrCode(java.com.util.Date DT, java.lang.String BA_NM, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedChkByDateAndBaName(java.util.Date DT, java.lang.String BA_NM,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk WHERE chk.chkCheckDate <= ?1 AND chk.chkPosted = 1 AND chk.chkVoid = 0 AND chk.adBankAccount.baName = ?2 AND chk.chkAdCompany = ?3");
			query.setParameter(1, DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findPostedChkByDateAndBaName(java.com.util.Date DT, java.lang.String BA_NM, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnreconciledPostedChkByDateAndBaName(java.util.Date DT, java.lang.String BA_NM,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk WHERE chk.chkReconciled = 0 AND chk.chkCheckDate <= ?1 AND chk.chkPosted = 1 AND chk.chkVoid = 0 AND chk.adBankAccount.baName = ?2 AND chk.chkAdCompany = ?3");
			query.setParameter(1, DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findUnreconciledPostedChkByDateAndBaName(java.com.util.Date DT, java.lang.String BA_NM, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVouCodeAndChkVoid(java.lang.Integer VOU_CODE, byte CHK_VD,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk, IN(chk.apAppliedVouchers) av WHERE av.apVoucherPaymentSchedule.apVoucher.vouCode = ?1 AND chk.chkVoid = ?2 AND chk.chkAdCompany = ?3");
			query.setParameter(1, VOU_CODE);
			query.setParameter(2, CHK_VD);
			query.setParameter(3, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findByVouCodeAndChkVoid(java.lang.Integer VOU_CODE, byte CHK_VD, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVouCodeAndChkVoidAndChkPosted(java.lang.Integer VOU_CODE, byte CHK_VD,
			byte CHK_PSTD, java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk, IN(chk.apAppliedVouchers) av WHERE av.apVoucherPaymentSchedule.apVoucher.vouCode = ?1 AND chk.chkVoid = ?2 AND chk.chkPosted = ?3 AND chk.chkAdCompany = ?4");
			query.setParameter(1, VOU_CODE);
			query.setParameter(2, CHK_VD);
			query.setParameter(3, CHK_PSTD);
			query.setParameter(4, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findByVouCodeAndChkVoidAndChkPosted(java.lang.Integer VOU_CODE, byte CHK_VD, byte CHK_PSTD, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByChkPostedAndChkVoidAndCbName(byte CHK_PSTD, byte CHK_VD, java.lang.String CB_NM,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk WHERE chk.chkPosted=?1 AND chk.chkVoid = ?2 AND chk.apCheckBatch.cbName = ?3 AND chk.chkAdCompany = ?4");
			query.setParameter(1, CHK_PSTD);
			query.setParameter(2, CHK_VD);
			query.setParameter(3, CB_NM);
			query.setParameter(4, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findByChkPostedAndChkVoidAndCbName(byte CHK_PSTD, byte CHK_VD, java.lang.String CB_NM, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findRejectedChkByBrCodeAndChkCreatedBy(java.lang.Integer CHK_AD_BRNCH,
			java.lang.String CHK_CRTD_BY, java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk WHERE chk.chkType = 'PAYMENT' AND chk.chkVoid = 0 AND chk.chkApprovalStatus IS NULL AND chk.chkReasonForRejection IS NOT NULL AND chk.chkAdBranch = ?1 AND chk.chkCreatedBy=?2 AND chk.chkAdCompany = ?3");
			query.setParameter(1, CHK_AD_BRNCH);
			query.setParameter(2, CHK_CRTD_BY);
			query.setParameter(3, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findRejectedChkByBrCodeAndChkCreatedBy(java.lang.Integer CHK_AD_BRNCH, java.lang.String CHK_CRTD_BY, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findRejectedDirectChkByBrCodeAndChkCreatedBy(java.lang.Integer CHK_AD_BRNCH,
			java.lang.String CHK_CRTD_BY, java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk WHERE chk.chkType = 'DIRECT' AND chk.chkVoid = 0 AND chk.chkApprovalStatus IS NULL AND chk.chkReasonForRejection IS NOT NULL AND chk.chkAdBranch = ?1 AND chk.chkCreatedBy=?2 AND chk.chkAdCompany = ?3");
			query.setParameter(1, CHK_AD_BRNCH);
			query.setParameter(2, CHK_CRTD_BY);
			query.setParameter(3, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findRejectedDirectChkByBrCodeAndChkCreatedBy(java.lang.Integer CHK_AD_BRNCH, java.lang.String CHK_CRTD_BY, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedChkByChkTypeAndChkDateRange(java.lang.String CHK_TYP,
			java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO, java.lang.Integer CHK_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk  WHERE chk.chkPosted = 1 AND chk.chkVoid = 0 AND chk.chkType = ?1 AND chk.chkDate >= ?2 AND chk.chkDate <= ?3 AND chk.chkAdCompany = ?4");
			query.setParameter(1, CHK_TYP);
			query.setParameter(2, RCT_DT_FRM);
			query.setParameter(3, RCT_DT_TO);
			query.setParameter(4, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findPostedChkByChkTypeAndChkDateRange(java.lang.String CHK_TYP, java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByChkDateRangeAndWtcCode(java.util.Date CHK_DT_FRM, java.util.Date CHK_DT_TO,
			java.lang.Integer AP_WTC_CODE, java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk, IN(chk.apDistributionRecords) dr WHERE chk.chkDate >= ?1 AND chk.chkDate <= ?2 AND chk.chkPosted = 1 AND chk.chkVoid = 0 AND chk.chkType = 'DIRECT' AND chk.apWithholdingTaxCode.wtcCode = ?3 AND dr.drClass='W-TAX' AND chk.chkAdCompany = ?4");
			query.setParameter(1, CHK_DT_FRM);
			query.setParameter(2, CHK_DT_TO);
			query.setParameter(3, AP_WTC_CODE);
			query.setParameter(4, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findByChkDateRangeAndWtcCode(java.com.util.Date CHK_DT_FRM, java.com.util.Date CHK_DT_TO, java.lang.Integer AP_WTC_CODE, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByCbName(java.lang.String CB_NM, java.lang.Integer CHK_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk WHERE chk.apCheckBatch.cbName=?1 AND chk.chkAdCompany = ?2");
			query.setParameter(1, CB_NM);
			query.setParameter(2, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findByCbName(java.lang.String CB_NM, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftChkAll(java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk WHERE chk.chkApprovalStatus IS NULL AND chk.chkVoid = 0 AND chk.chkAdCompany = ?1");
			query.setParameter(1, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findDraftChkAll(java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftChkByBrCode(java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk WHERE chk.chkType = 'PAYMENT' AND chk.chkApprovalStatus IS NULL AND chk.chkReasonForRejection IS NULL AND chk.chkVoid = 0 AND chk.chkAdBranch = ?1 AND chk.chkAdCompany = ?2");
			query.setParameter(1, CHK_AD_BRNCH);
			query.setParameter(2, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findDraftChkByBrCode(java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftDirectChkByBrCode(java.lang.Integer CHK_AD_BRNCH,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk WHERE chk.chkType = 'DIRECT' AND chk.chkApprovalStatus IS NULL AND chk.chkReasonForRejection IS NULL AND chk.chkVoid = 0 AND chk.chkAdBranch = ?1 AND chk.chkAdCompany = ?2");
			query.setParameter(1, CHK_AD_BRNCH);
			query.setParameter(2, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findDraftDirectChkByBrCode(java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedChkByChkTypeAndChkDateRangeAndSplNameAndTcType(java.lang.String CHK_TYP,
			java.util.Date INV_DT_FRM, java.util.Date INV_DT_TO, java.lang.String SPL_NM, java.lang.String TC_TYP,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk  WHERE chk.chkPosted = 1 AND chk.chkVoid = 0 AND chk.chkType = ?1 AND chk.chkDate >= ?2 AND chk.chkDate <= ?3 AND chk.apSupplier.splName = ?4 AND chk.apTaxCode.tcType = ?5 AND chk.chkAdCompany = ?6");
			query.setParameter(1, CHK_TYP);
			query.setParameter(2, INV_DT_FRM);
			query.setParameter(3, INV_DT_TO);
			query.setParameter(4, SPL_NM);
			query.setParameter(5, TC_TYP);
			query.setParameter(6, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findPostedChkByChkTypeAndChkDateRangeAndSplNameAndTcType(java.lang.String CHK_TYP, java.com.util.Date INV_DT_FRM, java.com.util.Date INV_DT_TO, java.lang.String SPL_NM, java.lang.String TC_TYP, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedChkByChkDateRange(java.util.Date CHK_DT_FRM, java.util.Date CHK_DT_TO,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk  WHERE chk.chkPosted = 0 AND chk.chkVoid = 0 AND chk.chkDate >= ?1 AND chk.chkDate <= ?2 AND chk.chkAdCompany = ?3");
			query.setParameter(1, CHK_DT_FRM);
			query.setParameter(2, CHK_DT_TO);
			query.setParameter(3, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findUnpostedChkByChkDateRange(java.com.util.Date CHK_DT_FRM, java.com.util.Date CHK_DT_TO, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAdCmpnyAll(java.lang.String CHK_TYP, java.lang.Integer CHK_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(chk) FROM ApCheck chk WHERE chk.chkType =?1 AND chk.chkAdCompany=?2");
			query.setParameter(1, CHK_TYP);
			query.setParameter(2, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findByAdCmpnyAll(java.lang.String CHK_TYP, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByChkTypeAndCbName(java.lang.String CHK_TYP, java.lang.String CB_NM,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk WHERE chk.chkType =?1 AND chk.apCheckBatch.cbName=?2 AND chk.chkAdCompany = ?3");
			query.setParameter(1, CHK_TYP);
			query.setParameter(2, CB_NM);
			query.setParameter(3, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findByChkTypeAndCbName(java.lang.String CHK_TYP, java.lang.String CB_NM, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedChkByBaNameAndChkDateRangeAndBrCode(java.lang.String BA_NM,
			java.util.Date CHK_DT_FRM, java.util.Date CHK_DT_TO, java.lang.Integer CHK_AD_BRNCH,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk  WHERE chk.chkPosted = 1 AND chk.chkVoid = 0 AND chk.adBankAccount.baIsCashAccount = 0 AND chk.adBankAccount.baName = ?1 AND chk.chkCheckDate >= ?2 AND chk.chkCheckDate <= ?3 AND chk.chkAdBranch = ?4 AND chk.chkAdCompany = ?5");
			query.setParameter(1, BA_NM);
			query.setParameter(2, CHK_DT_FRM);
			query.setParameter(3, CHK_DT_TO);
			query.setParameter(4, CHK_AD_BRNCH);
			query.setParameter(5, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findPostedChkByBaNameAndChkDateRangeAndBrCode(java.lang.String BA_NM, java.com.util.Date CHK_DT_FRM, java.com.util.Date CHK_DT_TO, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findReconciledPostedChkByBaNameAndChkDateRangeAndBrCode(java.lang.String BA_NM,
			java.util.Date CHK_DT_FRM, java.util.Date CHK_DT_TO, java.lang.Integer CHK_AD_BRNCH,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk  WHERE chk.chkPosted = 1 AND chk.chkVoid = 0 AND chk.chkReconciled = 1 AND chk.adBankAccount.baIsCashAccount = 0 AND chk.adBankAccount.baName = ?1 AND chk.chkCheckDate >= ?2 AND chk.chkCheckDate <= ?3 AND chk.chkAdBranch = ?4 AND chk.chkAdCompany = ?5");
			query.setParameter(1, BA_NM);
			query.setParameter(2, CHK_DT_FRM);
			query.setParameter(3, CHK_DT_TO);
			query.setParameter(4, CHK_AD_BRNCH);
			query.setParameter(5, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findReconciledPostedChkByBaNameAndChkDateRangeAndBrCode(java.lang.String BA_NM, java.com.util.Date CHK_DT_FRM, java.com.util.Date CHK_DT_TO, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findChecksForReleaseByBrCode(java.lang.Integer CHK_AD_BRNCH,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk  WHERE chk.chkPosted = 1 AND chk.chkVoid = 0 AND chk.chkAdBranch = ?1 AND chk.chkAdCompany = ?2");
			query.setParameter(1, CHK_AD_BRNCH);
			query.setParameter(2, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findChecksForReleaseByBrCode(java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findChecksForPrintingByBrCode(java.lang.Integer CHK_AD_BRNCH,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk  WHERE chk.chkPosted = 1 AND chk.chkApprovalStatus = 'APPROVED' AND chk.chkVoid = 0 AND chk.chkReleased = 1 AND chk.chkPrinted = 0 AND chk.chkAdBranch = ?1 AND chk.chkAdCompany = ?2");
			query.setParameter(1, CHK_AD_BRNCH);
			query.setParameter(2, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findChecksForPrintingByBrCode(java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnPostedChkByChkDateRangeAndSplCode(java.util.Date CHK_DT_FRM,
			java.util.Date CHK_DT_TO, java.lang.Integer SPL_CODE, java.lang.Integer CHK_AD_BRNCH,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk WHERE chk.chkVoid = 0 AND chk.chkPosted = 0 AND chk.chkDate >= ?1 AND chk.chkDate <= ?2 AND chk.apSupplier.splCode = ?3 AND chk.chkAdBranch = ?4 AND chk.chkAdCompany = ?5 ORDER BY chk.chkDate, chk.chkDocumentNumber");
			query.setParameter(1, CHK_DT_FRM);
			query.setParameter(2, CHK_DT_TO);
			query.setParameter(3, SPL_CODE);
			query.setParameter(4, CHK_AD_BRNCH);
			query.setParameter(5, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findUnPostedChkByChkDateRangeAndSplCode(java.com.util.Date CHK_DT_FRM, java.com.util.Date CHK_DT_TO, java.lang.Integer SPL_CODE, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedChkByChkDateRangeAndSplCode(java.util.Date CHK_DT_FRM,
			java.util.Date CHK_DT_TO, java.lang.Integer SPL_CODE, java.lang.Integer CHK_AD_BRNCH,
			java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk WHERE chk.chkVoid = 0 AND chk.chkPosted = 1 AND chk.chkDate >= ?1 AND chk.chkDate <= ?2 AND chk.apSupplier.splCode = ?3 AND chk.chkAdBranch = ?4 AND chk.chkAdCompany = ?5 ORDER BY chk.chkDate, chk.chkDocumentNumber");
			query.setParameter(1, CHK_DT_FRM);
			query.setParameter(2, CHK_DT_TO);
			query.setParameter(3, SPL_CODE);
			query.setParameter(4, CHK_AD_BRNCH);
			query.setParameter(5, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findPostedChkByChkDateRangeAndSplCode(java.com.util.Date CHK_DT_FRM, java.com.util.Date CHK_DT_TO, java.lang.Integer SPL_CODE, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedTaxDirectChkByChkDateRange(java.util.Date CHK_DT_FRM,
			java.util.Date CHK_DT_TO, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(chk) FROM ApCheck chk WHERE chk.chkVoid = 0 AND chk.chkPosted = 1 AND chk.apTaxCode.tcType IN ('EXEMPT','ZERO-RATED','INCLUSIVE','EXCLUSIVE') AND chk.chkDate >= ?1 AND chk.chkDate <= ?2 AND chk.chkAdBranch = ?3 AND chk.chkAdCompany = ?4 ORDER BY chk.chkDate, chk.chkDocumentNumber");
			query.setParameter(1, CHK_DT_FRM);
			query.setParameter(2, CHK_DT_TO);
			query.setParameter(3, CHK_AD_BRNCH);
			query.setParameter(4, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckHome.findPostedTaxDirectChkByChkDateRange(java.com.util.Date CHK_DT_FRM, java.com.util.Date CHK_DT_TO, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getChkByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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

	public LocalApCheck create(Integer CHK_CODE, String CHK_TYP, String CHK_DESC, Date CHK_DT,
                               Date CHK_CHCK_DT, String CHK_NMBR, String CHK_DCMNT_NMBR, String CHK_RFRNC_NMBR, String CHK_INFO_TYP,
                               String CHK_INFO_BIO_NMBR, String CHK_INFO_BIO_DESC, String CHK_INFO_TYP_STATUS, String CHK_INFO_RQST_STATUS,
                               byte CHK_INVT_IS, byte CHK_INVT_TB, Date CHK_INVT_NXT_RN_DT, Date CHK_INVT_STTLMNT_DT,
                               Date CHK_INVT_MTRTY_DT, double CHK_INVT_BD_YLD, double CHK_INVT_CPN_RT, double CHK_INVT_STTLMNT_AMNT,
                               double CHK_INVT_FC_VL, double CHK_INVT_PM_AMNT, byte CHK_LN, byte CHK_LN_GNRTD, Date CHK_CNVRSN_DT,
                               double CHK_CNVRSN_RT, double CHK_BLL_AMNT, double CHK_AMNT, String CHK_APPRVL_STATUS,
                               String CHK_RSN_FR_RJCTN, byte CHK_PSTD, byte CHK_VD, byte CHK_CRSS_CHCK, String CHK_VD_APPRVL_STATUS,
                               byte CHK_VD_PSTD, String CHK_CRTD_BY, Date CHK_DT_CRTD, String CHK_LST_MDFD_BY, Date CHK_DT_LST_MDFD,
                               String CHK_APPRVD_RJCTD_BY, Date CHK_DT_APPRVD_RJCTD, String CHK_PSTD_BY, Date CHK_DT_PSTD, byte CHK_RCNCLD,
                               Date CHK_DT_RCNCLD, byte CHK_RLSD, Date CHK_DT_RLSD, byte CHK_PRNTD, byte CHK_CV_PRNTD, String CHK_MMO,
                               String CHK_SPL_NM, Integer CHK_AD_BRNCH, Integer CHK_AD_CMPNY) throws CreateException {
		try {

			LocalApCheck entity = new LocalApCheck();

			Debug.print("ApCheckBean create");
			entity.setChkCode(CHK_CODE);
			entity.setChkType(CHK_TYP);
			entity.setChkDescription(CHK_DESC);
			entity.setChkDate(CHK_DT);
			entity.setChkCheckDate(CHK_CHCK_DT);
			entity.setChkNumber(CHK_NMBR);
			entity.setChkDocumentNumber(CHK_DCMNT_NMBR);
			entity.setChkInfoType(CHK_INFO_TYP);
			entity.setChkInfoBioNumber(CHK_INFO_BIO_NMBR);
			entity.setChkInfoBioDescription(CHK_INFO_BIO_DESC);
			entity.setChkInfoTypeStatus(CHK_INFO_TYP_STATUS);
			entity.setChkInfoRequestStatus(CHK_INFO_RQST_STATUS);
			entity.setChkInvtInscribedStock(CHK_INVT_IS);
			entity.setChkInvtTreasuryBill(CHK_INVT_TB);
			entity.setChkInvtNextRunDate(CHK_INVT_NXT_RN_DT);
			entity.setChkInvtSettlementDate(CHK_INVT_STTLMNT_DT);
			entity.setChkInvtMaturityDate(CHK_INVT_MTRTY_DT);
			entity.setChkInvtBidYield(CHK_INVT_BD_YLD);
			entity.setChkInvtCouponRate(CHK_INVT_CPN_RT);
			entity.setChkInvtSettleAmount(CHK_INVT_STTLMNT_AMNT);
			entity.setChkInvtFaceValue(CHK_INVT_FC_VL);
			entity.setChkInvtPremiumAmount(CHK_INVT_PM_AMNT);
			entity.setChkLoan(CHK_LN);
			entity.setChkLoanGenerated(CHK_LN_GNRTD);
			entity.setChkReferenceNumber(CHK_RFRNC_NMBR);
			entity.setChkConversionDate(CHK_CNVRSN_DT);
			entity.setChkConversionRate(CHK_CNVRSN_RT);
			entity.setChkBillAmount(CHK_BLL_AMNT);
			entity.setChkAmount(CHK_AMNT);
			entity.setChkApprovalStatus(CHK_APPRVL_STATUS);
			entity.setChkReasonForRejection(CHK_RSN_FR_RJCTN);
			entity.setChkPosted(CHK_PSTD);
			entity.setChkVoid(CHK_VD);
			entity.setChkCrossCheck(CHK_CRSS_CHCK);
			entity.setChkVoidApprovalStatus(CHK_VD_APPRVL_STATUS);
			entity.setChkVoidPosted(CHK_VD_PSTD);
			entity.setChkCreatedBy(CHK_CRTD_BY);
			entity.setChkDateCreated(CHK_DT_CRTD);
			entity.setChkLastModifiedBy(CHK_LST_MDFD_BY);
			entity.setChkDateLastModified(CHK_DT_LST_MDFD);
			entity.setChkApprovedRejectedBy(CHK_APPRVD_RJCTD_BY);
			entity.setChkDateApprovedRejected(CHK_DT_APPRVD_RJCTD);
			entity.setChkPostedBy(CHK_PSTD_BY);
			entity.setChkDatePosted(CHK_DT_PSTD);
			entity.setChkReconciled(CHK_RCNCLD);
			entity.setChkDateReconciled(CHK_DT_RCNCLD);
			entity.setChkReleased(CHK_RLSD);
			entity.setChkDateReleased(CHK_DT_RLSD);
			entity.setChkPrinted(CHK_PRNTD);
			entity.setChkCvPrinted(CHK_CV_PRNTD);
			entity.setChkMemo(CHK_MMO);
			entity.setChkSupplierName(CHK_SPL_NM);
			entity.setChkAdBranch(CHK_AD_BRNCH);
			entity.setChkAdCompany(CHK_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalApCheck create(String CHK_TYP, String CHK_DESC, Date CHK_DT, Date CHK_CHCK_DT,
                               String CHK_NMBR, String CHK_DCMNT_NMBR, String CHK_RFRNC_NMBR, String CHK_INFO_TYP,
                               String CHK_INFO_BIO_NMBR, String CHK_INFO_BIO_DESC, String CHK_INFO_TYP_STATUS, String CHK_INFO_RQST_STATUS,
                               byte CHK_INVT_IS, byte CHK_INVT_TB, Date CHK_INVT_NXT_RN_DT, Date CHK_INVT_STTLMNT_DT,
                               Date CHK_INVT_MTRTY_DT, double CHK_INVT_BD_YLD, double CHK_INVT_CPN_RT, double CHK_INVT_STTLMNT_AMNT,
                               double CHK_INVT_FC_VL, double CHK_INVT_PM_AMNT, byte CHK_LN, byte CHK_LN_GNRTD, Date CHK_CNVRSN_DT,
                               double CHK_CNVRSN_RT, double CHK_BLL_AMNT, double CHK_AMNT, String CHK_APPRVL_STATUS,
                               String CHK_RSN_FR_RJCTN, byte CHK_PSTD, byte CHK_VD, byte CHK_CRSS_CHCK, String CHK_VD_APPRVL_STATUS,
                               byte CHK_VD_PSTD, String CHK_CRTD_BY, Date CHK_DT_CRTD, String CHK_LST_MDFD_BY, Date CHK_DT_LST_MDFD,
                               String CHK_APPRVD_RJCTD_BY, Date CHK_DT_APPRVD_RJCTD, String CHK_PSTD_BY, Date CHK_DT_PSTD, byte CHK_RCNCLD,
                               Date CHK_DT_RCNCLD, byte CHK_RLSD, Date CHK_DT_RLSD, byte CHK_PRNTD, byte CHK_CV_PRNTD, String CHK_MMO,
                               String CHK_SPL_NM, Integer CHK_AD_BRNCH, Integer CHK_AD_CMPNY) throws CreateException {
		try {

			LocalApCheck entity = new LocalApCheck();

			Debug.print("ApCheckBean create");
			entity.setChkType(CHK_TYP);
			entity.setChkDescription(CHK_DESC);
			entity.setChkDate(CHK_DT);
			entity.setChkCheckDate(CHK_CHCK_DT);
			entity.setChkNumber(CHK_NMBR);
			entity.setChkDocumentNumber(CHK_DCMNT_NMBR);
			entity.setChkReferenceNumber(CHK_RFRNC_NMBR);
			entity.setChkInfoType(CHK_INFO_TYP);
			entity.setChkInfoBioNumber(CHK_INFO_BIO_NMBR);
			entity.setChkInfoBioDescription(CHK_INFO_BIO_DESC);
			entity.setChkInfoTypeStatus(CHK_INFO_TYP_STATUS);
			entity.setChkInfoRequestStatus(CHK_INFO_RQST_STATUS);
			entity.setChkInvtInscribedStock(CHK_INVT_IS);
			entity.setChkInvtTreasuryBill(CHK_INVT_TB);
			entity.setChkInvtNextRunDate(CHK_INVT_NXT_RN_DT);
			entity.setChkInvtSettlementDate(CHK_INVT_STTLMNT_DT);
			entity.setChkInvtMaturityDate(CHK_INVT_MTRTY_DT);
			entity.setChkInvtBidYield(CHK_INVT_BD_YLD);
			entity.setChkInvtCouponRate(CHK_INVT_CPN_RT);
			entity.setChkInvtSettleAmount(CHK_INVT_STTLMNT_AMNT);
			entity.setChkInvtFaceValue(CHK_INVT_FC_VL);
			entity.setChkInvtPremiumAmount(CHK_INVT_PM_AMNT);
			entity.setChkLoan(CHK_LN);
			entity.setChkLoanGenerated(CHK_LN_GNRTD);
			entity.setChkConversionDate(CHK_CNVRSN_DT);
			entity.setChkConversionRate(CHK_CNVRSN_RT);
			entity.setChkBillAmount(CHK_BLL_AMNT);
			entity.setChkAmount(CHK_AMNT);
			entity.setChkApprovalStatus(CHK_APPRVL_STATUS);
			entity.setChkReasonForRejection(CHK_RSN_FR_RJCTN);
			entity.setChkPosted(CHK_PSTD);
			entity.setChkVoid(CHK_VD);
			entity.setChkCrossCheck(CHK_CRSS_CHCK);
			entity.setChkVoidApprovalStatus(CHK_VD_APPRVL_STATUS);
			entity.setChkVoidPosted(CHK_VD_PSTD);
			entity.setChkCreatedBy(CHK_CRTD_BY);
			entity.setChkDateCreated(CHK_DT_CRTD);
			entity.setChkLastModifiedBy(CHK_LST_MDFD_BY);
			entity.setChkDateLastModified(CHK_DT_LST_MDFD);
			entity.setChkApprovedRejectedBy(CHK_APPRVD_RJCTD_BY);
			entity.setChkDateApprovedRejected(CHK_DT_APPRVD_RJCTD);
			entity.setChkPostedBy(CHK_PSTD_BY);
			entity.setChkDatePosted(CHK_DT_PSTD);
			entity.setChkReconciled(CHK_RCNCLD);
			entity.setChkDateReconciled(CHK_DT_RCNCLD);
			entity.setChkReleased(CHK_RLSD);
			entity.setChkDateReleased(CHK_DT_RLSD);
			entity.setChkPrinted(CHK_PRNTD);
			entity.setChkCvPrinted(CHK_CV_PRNTD);
			entity.setChkMemo(CHK_MMO);
			entity.setChkSupplierName(CHK_SPL_NM);
			entity.setChkAdBranch(CHK_AD_BRNCH);
			entity.setChkAdCompany(CHK_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}