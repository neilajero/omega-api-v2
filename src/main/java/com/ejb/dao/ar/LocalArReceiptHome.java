package com.ejb.dao.ar;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArReceipt;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;
import com.util.EJBCommon;

@SuppressWarnings("ALL")
@Stateless
public class LocalArReceiptHome {

	public static final String JNDI_NAME = "LocalArReceiptHome!com.ejb.ar.LocalArReceiptHome";

	@EJB
	public PersistenceBeanClass em;

	private String RCT_TYP= null;
	private String RCT_DESC= null;
	private Date RCT_DT = null;
	private String RCT_NMBR = null;
	private String RCT_RFRNC_NMBR = null;
	private String RCT_CHCK_NO = null;
	private String RCT_PYFL_RFRNC_NMBR = null;
	private final String RCT_CHQ_NMBR = null;
	private final String RCT_VCHR_NMBR = null;
	private final String RCT_CRD_NMBR1 = null;
	private final String RCT_CRD_NMBR2 = null;
	private final String RCT_CRD_NMBR3 = null;
	private double RCT_AMNT = 0d;
	private double RCT_AMNT_CSH = 0d;
	private final double RCT_AMNT_CHQ = 0d;
	private final double RCT_AMNT_VCHR = 0d;
	private final double RCT_AMNT_CRD1 = 0d;
	private final double RCT_AMNT_CRD2 = 0d;
	private final double RCT_AMNT_CRD3 = 0d;
	private Date RCT_CNVRSN_DT = null;
	private double RCT_CNVRSN_RT = 0d;
	private final String RCT_SLD_TO = null;
	private String RCT_PYMNT_MTHD = null;
	private final byte RCT_CSTMR_DPST = EJBCommon.FALSE;
	private final double RCT_APPLD_DPST = 0d;
	private final String RCT_APPRVL_STATUS = null;
	private final String RCT_RSN_FR_RJCTN = null;
	private final byte RCT_PSTD = EJBCommon.FALSE;
	private final String RCT_VD_APPRVL_STATUS = null;
	private final byte RCT_VD_PSTD = EJBCommon.FALSE;
	private final byte RCT_VD = EJBCommon.FALSE;
	private final byte RCT_RCNCLD = EJBCommon.FALSE;
	private final byte RCT_RCNCLD_CHQ = EJBCommon.FALSE;
	private final byte RCT_RCNCLD_CRD1 = EJBCommon.FALSE;
	private final byte RCT_RCNCLD_CRD2 = EJBCommon.FALSE;
	private final byte RCT_RCNCLD_CRD3 = EJBCommon.FALSE;
	private final Date RCT_DT_RCNCLD = null;
	private final Date RCT_DT_RCNCLD_CHQ = null;
	private final Date RCT_DT_RCNCLD_CRD1 = null;
	private final Date RCT_DT_RCNCLD_CRD2 = null;
	private final Date RCT_DT_RCNCLD_CRD3 = null;
	private String RCT_CRTD_BY = null;
	private Date RCT_DT_CRTD = null;
	private final String RCT_LST_MDFD_BY = null;
	private final Date RCT_DT_LST_MDFD = null;
	private final String RCT_APPRVD_RJCTD_BY = null;
	private final Date RCT_DT_APPRVD_RJCTD = null;
	private final String RCT_PSTD_BY = null;
	private final Date RCT_DT_PSTD = null;
	private final byte RCT_PRNTD = EJBCommon.FALSE;
	private final String RCT_LV_SHFT = null;
	private final byte RCT_LCK = EJBCommon.FALSE;
	private final byte RCT_SBJCT_TO_CMMSSN = EJBCommon.FALSE;
	private final String RCT_CST_NM = null;
	private final String RCT_CST_ADRSS = null;
	private final byte RCT_INVTR_BGNNG_BLNC = EJBCommon.FALSE;
	private final byte RCT_INVTR_IF = EJBCommon.FALSE;
	private final Date RCT_INVTR_NXT_RN_DT = null;
	private Integer RCT_AD_BRNCH = null;
	private Integer RCT_AD_CMPNY = null;

	public LocalArReceiptHome() {
	}

	// FINDER METHODS

	public LocalArReceipt findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArReceipt entity = (LocalArReceipt) em.find(new LocalArReceipt(), pk);
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

	public LocalArReceipt findByPrimaryKey(java.lang.Integer pk, String companyShortName) throws FinderException {
		try {
			LocalArReceipt entity = (LocalArReceipt) em.findPerCompany(new LocalArReceipt(), pk, companyShortName);
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

	public LocalArReceipt findByRctNumberAndBrCode(java.lang.String RCT_NMBR, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctNumber = ?1 AND rct.rctAdBranch = ?2 AND rct.rctAdCompany = ?3");
			query.setParameter(1, RCT_NMBR);
			query.setParameter(2, RCT_AD_BRNCH);
			query.setParameter(3, RCT_AD_CMPNY);
            return (LocalArReceipt) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArReceiptHome.findByRctNumberAndBrCode(java.lang.String RCT_NMBR, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findByRctNumberAndBrCode(java.lang.String RCT_NMBR, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArReceipt findByRctNumberAndBrCode(java.lang.String RCT_NMBR, java.lang.Integer RCT_AD_BRNCH,
												   java.lang.Integer RCT_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(rct) FROM ArReceipt rct "
							+ "WHERE rct.rctNumber = ?1 AND rct.rctAdBranch = ?2 AND rct.rctAdCompany = ?3",
					companyShortName);
			query.setParameter(1, RCT_NMBR);
			query.setParameter(2, RCT_AD_BRNCH);
			query.setParameter(3, RCT_AD_CMPNY);
			return (LocalArReceipt) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findPostedRctByRctDateRange(java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctPosted = 1 AND rct.rctDate >= ?1 AND rct.rctDate <= ?2 AND rct.rctAdCompany = ?3");
			query.setParameter(1, RCT_DT_FRM);
			query.setParameter(2, RCT_DT_TO);
			query.setParameter(3, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findPostedRctByRctDateRange(java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection  findAllCollectionByReferenceNumber(java.lang.String INV_RFRNC_NMBR,
																	java.lang.Integer INV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctType = 'COLLECTION' AND rct.rctVoid = 0 AND rct.rctReferenceNumber = ?1 AND rct.rctAdCompany = ?2");
			query.setParameter(1, INV_RFRNC_NMBR);
			query.setParameter(2, INV_AD_CMPNY);
			java.util.Collection lists = query.getResultList();
			return lists;
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArReceiptHome.findAllCollectionByReferenceNumber(java.lang.String INV_RFRNC_NMBR, java.lang.Integer INV_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findAllCollectionByReferenceNumber(java.lang.String INV_RFRNC_NMBR, java.lang.Integer INV_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArReceipt findByReferenceNumberAndCompanyCode(java.lang.String INV_RFRNC_NMBR,
			java.lang.Integer INV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctPosted = 0 AND rct.rctVoid = 0 AND rct.rctReferenceNumber = ?1 AND rct.rctAdCompany = ?2");
			query.setParameter(1, INV_RFRNC_NMBR);
			query.setParameter(2, INV_AD_CMPNY);
            return (LocalArReceipt) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArReceiptHome.findByReferenceNumberAndCompanyCode(java.lang.String INV_RFRNC_NMBR, java.lang.Integer INV_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findByReferenceNumberAndCompanyCode(java.lang.String INV_RFRNC_NMBR, java.lang.Integer INV_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArReceipt findByPayfileReferenceNumberAndCompanyCode(java.lang.String RCT_PYFL_RFRNC_NMBR,
			java.lang.Integer INV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctVoid = 0 AND rct.rctPayfileReferenceNumber = ?1 AND rct.rctAdCompany = ?2");
			query.setParameter(1, RCT_PYFL_RFRNC_NMBR);
			query.setParameter(2, INV_AD_CMPNY);
            return (LocalArReceipt) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArReceiptHome.findByPayfileReferenceNumberAndCompanyCode(java.lang.String RCT_PYFL_RFRNC_NMBR, java.lang.Integer INV_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findByPayfileReferenceNumberAndCompanyCode(java.lang.String RCT_PYFL_RFRNC_NMBR, java.lang.Integer INV_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArReceipt findByPayfileReferenceNumberAndCompanyCode(java.lang.String RCT_PYFL_RFRNC_NMBR,
																	 java.lang.Integer INV_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(rct) FROM ArReceipt rct  "
							+ "WHERE rct.rctVoid = 0 AND rct.rctPayfileReferenceNumber = ?1 AND rct.rctAdCompany = ?2",
					companyShortName);
			query.setParameter(1, RCT_PYFL_RFRNC_NMBR);
			query.setParameter(2, INV_AD_CMPNY);
			return (LocalArReceipt) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findVoidPostedRctByRctDateRange(java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctPosted = 1 AND rct.rctVoid = 1 AND rct.rctVoidPosted = 1 AND rct.rctDate >= ?1 AND rct.rctDate <= ?2 AND rct.rctAdCompany = ?3");
			query.setParameter(1, RCT_DT_FRM);
			query.setParameter(2, RCT_DT_TO);
			query.setParameter(3, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findVoidPostedRctByRctDateRange(java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnPostedRctByRctDateRangeAndSplCode(java.util.Date RCT_DT_FRM,
			java.util.Date RCT_DT_TO, java.lang.Integer SPL_CODE, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctVoid = 0 AND rct.rctPosted = 0 AND rct.rctDate >= ?1 AND rct.rctDate <= ?2 AND rct.arCustomer.apSupplier.splCode = ?3 AND rct.rctAdBranch = ?4 AND rct.rctAdCompany = ?5 ORDER BY rct.rctDate, rct.rctNumber");
			query.setParameter(1, RCT_DT_FRM);
			query.setParameter(2, RCT_DT_TO);
			query.setParameter(3, SPL_CODE);
			query.setParameter(4, RCT_AD_BRNCH);
			query.setParameter(5, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findUnPostedRctByRctDateRangeAndSplCode(java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer SPL_CODE, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedRctByRctDateRangeAndSplCode(java.util.Date RCT_DT_FRM,
			java.util.Date RCT_DT_TO, java.lang.Integer SPL_CODE, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctVoid = 0 AND rct.rctPosted = 1 AND rct.rctDate >= ?1 AND rct.rctDate <= ?2 AND rct.arCustomer.apSupplier.splCode = ?3 AND rct.rctAdBranch = ?4 AND rct.rctAdCompany = ?5 ORDER BY rct.rctDate, rct.rctNumber");
			query.setParameter(1, RCT_DT_FRM);
			query.setParameter(2, RCT_DT_TO);
			query.setParameter(3, SPL_CODE);
			query.setParameter(4, RCT_AD_BRNCH);
			query.setParameter(5, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findPostedRctByRctDateRangeAndSplCode(java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer SPL_CODE, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedRctByDateAndBaName(java.util.Date DT, java.lang.String BA_NM,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctDate <= ?1 AND rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.adBankAccount.baName = ?2  AND rct.rctAdCompany = ?3");
			query.setParameter(1, DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findPostedRctByDateAndBaName(java.com.util.Date DT, java.lang.String BA_NM, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnreconciledPostedRctByDateAndBaNameAndBrCode(java.util.Date DT,
			java.lang.String BA_NM, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctReconciled = 0 AND rct.rctDate <= ?1 AND rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.adBankAccount.baIsCashAccount = 0 AND rct.adBankAccount.baName = ?2 AND rct.rctAdBranch = ?3 AND rct.rctAdCompany = ?4");
			query.setParameter(1, DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, RCT_AD_BRNCH);
			query.setParameter(4, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findUnreconciledPostedRctByDateAndBaNameAndBrCode(java.com.util.Date DT, java.lang.String BA_NM, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnreconciledPostedCard1RctByDateAndBaNameAndBrCode(java.util.Date DT,
			java.lang.String BA_NM, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctReconciledCard1 = 0 AND rct.rctDate <= ?1 AND rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.adBankAccountCard1.baIsCashAccount = 0 AND rct.adBankAccountCard1.baName = ?2 AND rct.rctAdBranch = ?3 AND rct.rctAdCompany = ?4");
			query.setParameter(1, DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, RCT_AD_BRNCH);
			query.setParameter(4, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findUnreconciledPostedCard1RctByDateAndBaNameAndBrCode(java.com.util.Date DT, java.lang.String BA_NM, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnreconciledPostedCard2RctByDateAndBaNameAndBrCode(java.util.Date DT,
			java.lang.String BA_NM, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctReconciledCard2 = 0 AND rct.rctDate <= ?1 AND rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.adBankAccountCard2.baIsCashAccount = 0 AND rct.adBankAccountCard2.baName = ?2 AND rct.rctAdBranch = ?3 AND rct.rctAdCompany = ?4");
			query.setParameter(1, DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, RCT_AD_BRNCH);
			query.setParameter(4, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findUnreconciledPostedCard2RctByDateAndBaNameAndBrCode(java.com.util.Date DT, java.lang.String BA_NM, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnreconciledPostedCard3RctByDateAndBaNameAndBrCode(java.util.Date DT,
			java.lang.String BA_NM, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctReconciledCard3 = 0 AND rct.rctDate <= ?1 AND rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.adBankAccountCard3.baIsCashAccount = 0 AND rct.adBankAccountCard3.baName = ?2 AND rct.rctAdBranch = ?3 AND rct.rctAdCompany = ?4");
			query.setParameter(1, DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, RCT_AD_BRNCH);
			query.setParameter(4, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findUnreconciledPostedCard3RctByDateAndBaNameAndBrCode(java.com.util.Date DT, java.lang.String BA_NM, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnreconciledPostedChequeRctByDateAndBaNameAndBrCode(java.util.Date DT,
			java.lang.String BA_NM, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctReconciledCheque = 0 AND rct.rctDate <= ?1 AND rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.adBankAccount.baIsCashAccount = 0 AND rct.adBankAccount.baName = ?2 AND rct.rctAdBranch = ?3 AND rct.rctAdCompany = ?4");
			query.setParameter(1, DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, RCT_AD_BRNCH);
			query.setParameter(4, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findUnreconciledPostedChequeRctByDateAndBaNameAndBrCode(java.com.util.Date DT, java.lang.String BA_NM, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnreconciledPostedRctByDateAndBaName(java.util.Date DT, java.lang.String BA_NM,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctReconciled = 0 AND rct.rctAmountCash > 0 AND rct.rctDate <= ?1 AND rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.adBankAccount.baIsCashAccount = 0 AND rct.adBankAccount.baName = ?2 AND rct.rctAdCompany = ?3");
			query.setParameter(1, DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findUnreconciledPostedRctByDateAndBaName(java.com.util.Date DT, java.lang.String BA_NM, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnreconciledPostedCard1RctByDateAndBaName(java.util.Date DT, java.lang.String BA_NM,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctReconciledCard1 = 0 AND rct.rctAmountCard1 > 0 AND rct.rctDate <= ?1 AND rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.adBankAccountCard1.baIsCashAccount = 0 AND rct.adBankAccountCard1.baName = ?2 AND rct.rctAdCompany = ?3");
			query.setParameter(1, DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findUnreconciledPostedCard1RctByDateAndBaName(java.com.util.Date DT, java.lang.String BA_NM, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnreconciledPostedCard2RctByDateAndBaName(java.util.Date DT, java.lang.String BA_NM,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctReconciledCard2 = 0 AND rct.rctAmountCard2 > 0 AND rct.rctDate <= ?1 AND rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.adBankAccountCard2.baIsCashAccount = 0 AND rct.adBankAccountCard2.baName = ?2 AND rct.rctAdCompany = ?3");
			query.setParameter(1, DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findUnreconciledPostedCard2RctByDateAndBaName(java.com.util.Date DT, java.lang.String BA_NM, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnreconciledPostedCard3RctByDateAndBaName(java.util.Date DT, java.lang.String BA_NM,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctReconciledCard3 = 0 AND rct.rctAmountCard3 > 0 AND rct.rctDate <= ?1 AND rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.adBankAccountCard3.baIsCashAccount = 0 AND rct.adBankAccountCard3.baName = ?2 AND rct.rctAdCompany = ?3");
			query.setParameter(1, DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findUnreconciledPostedCard3RctByDateAndBaName(java.com.util.Date DT, java.lang.String BA_NM, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnreconciledPostedChequeRctByDateAndBaName(java.util.Date DT,
			java.lang.String BA_NM, java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctReconciledCheque = 0 AND rct.rctAmountCheque > 0 AND rct.rctDate <= ?1 AND rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.adBankAccount.baIsCashAccount = 0 AND rct.adBankAccount.baName = ?2 AND rct.rctAdCompany = ?3");
			query.setParameter(1, DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findUnreconciledPostedChequeRctByDateAndBaName(java.com.util.Date DT, java.lang.String BA_NM, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByInvCodeAndRctVoid(java.lang.Integer INV_CODE, byte RCT_VD,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct, IN(rct.arAppliedInvoices) ai WHERE ai.arInvoicePaymentSchedule.arInvoice.invCode = ?1 AND rct.rctVoid = ?2 AND rct.rctAdCompany = ?3");
			query.setParameter(1, INV_CODE);
			query.setParameter(2, RCT_VD);
			query.setParameter(3, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findByInvCodeAndRctVoid(java.lang.Integer INV_CODE, byte RCT_VD, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByInvCodeAndRctVoidAndRctPosted(java.lang.Integer INV_CODE, byte RCT_VD,
			byte RCT_PSTD, java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct, IN(rct.arAppliedInvoices) ai WHERE ai.arInvoicePaymentSchedule.arInvoice.invCode = ?1 AND rct.rctVoid = ?2 AND rct.rctPosted = ?3 AND rct.rctAdCompany = ?4");
			query.setParameter(1, INV_CODE);
			query.setParameter(2, RCT_VD);
			query.setParameter(3, RCT_PSTD);
			query.setParameter(4, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findByInvCodeAndRctVoidAndRctPosted(java.lang.Integer INV_CODE, byte RCT_VD, byte RCT_PSTD, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByRctPostedAndRctVoidAndRbName(byte RCT_PSTD, byte RCT_VD, java.lang.String RB_NM,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctPosted=?1 AND rct.rctVoid = ?2 AND rct.arReceiptBatch.rbName = ?3 AND rct.rctAdCompany = ?4");
			query.setParameter(1, RCT_PSTD);
			query.setParameter(2, RCT_VD);
			query.setParameter(3, RB_NM);
			query.setParameter(4, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findByRctPostedAndRctVoidAndRbName(byte RCT_PSTD, byte RCT_VD, java.lang.String RB_NM, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedRctByRctTypeAndRctDateRange(java.lang.String RCT_TYP,
			java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.rctType = ?1 AND rct.rctDate >= ?2 AND rct.rctDate <= ?3 AND rct.rctAdCompany = ?4");
			query.setParameter(1, RCT_TYP);
			query.setParameter(2, RCT_DT_FRM);
			query.setParameter(3, RCT_DT_TO);
			query.setParameter(4, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findPostedRctByRctTypeAndRctDateRange(java.lang.String RCT_TYP, java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedRctByRctTypeAndRctDateToAndCstCode(java.lang.String RCT_TYP,
			java.util.Date RCT_DT_TO, java.lang.String CST_CSTMR_CODE, java.lang.Integer RCT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.rctType = ?1 AND rct.rctDate <= ?2 AND rct.arCustomer.cstCustomerCode = ?3 AND rct.rctAdCompany = ?4");
			query.setParameter(1, RCT_TYP);
			query.setParameter(2, RCT_DT_TO);
			query.setParameter(3, CST_CSTMR_CODE);
			query.setParameter(4, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findPostedRctByRctTypeAndRctDateToAndCstCode(java.lang.String RCT_TYP, java.com.util.Date RCT_DT_TO, java.lang.String CST_CSTMR_CODE, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedRctByRctDateToAndCstCode(java.util.Date RCT_DT_TO,
			java.lang.String CST_CSTMR_CODE, java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.rctDate <= ?1 AND rct.arCustomer.cstCustomerCode = ?2 AND rct.rctAdCompany = ?3");
			query.setParameter(1, RCT_DT_TO);
			query.setParameter(2, CST_CSTMR_CODE);
			query.setParameter(3, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findPostedRctByRctDateToAndCstCode(java.com.util.Date RCT_DT_TO, java.lang.String CST_CSTMR_CODE, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findNotVoidByCustomerCode(java.lang.String CST_CSTMR_CODE,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.arCustomer.cstCustomerCode = ?1 AND rct.rctVoid = 0 AND rct.rctAdCompany = ?2");
			query.setParameter(1, CST_CSTMR_CODE);
			query.setParameter(2, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findNotVoidByCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByRbName(java.lang.String RB_NM, java.lang.Integer RCT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.arReceiptBatch.rbName=?1 AND rct.rctAdCompany = ?2");
			query.setParameter(1, RB_NM);
			query.setParameter(2, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findByRbName(java.lang.String RB_NM, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftRctAll(java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctApprovalStatus IS NULL AND rct.rctVoid = 0 AND rct.rctAdCompany = ?1");
			query.setParameter(1, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findDraftRctAll(java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftRctByBrCode(java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctApprovalStatus IS NULL AND rct.rctVoid = 0 AND rct.rctAdBranch = ?1 AND rct.rctAdCompany = ?2");
			query.setParameter(1, RCT_AD_BRNCH);
			query.setParameter(2, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findDraftRctByBrCode(java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftRctByTypeBrCode(java.lang.String RCT_TYPE, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctApprovalStatus IS NULL AND rct.rctVoid = 0 AND rct.rctType=?1 AND rct.rctAdBranch = ?2 AND rct.rctAdCompany = ?3");
			query.setParameter(1, RCT_TYPE);
			query.setParameter(2, RCT_AD_BRNCH);
			query.setParameter(3, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findDraftRctByTypeBrCode(java.lang.String RCT_TYPE, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedRctByRctTypeAndRctDateRangeAndCstNameAndTcType(java.lang.String RCT_TYP,
			java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO, java.lang.String CST_NM, java.lang.String TC_TYP,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.rctType = ?1 AND rct.rctDate >= ?2 AND rct.rctDate <= ?3 AND rct.arCustomer.cstName = ?4 AND rct.arTaxCode.tcType = ?5 AND rct.rctAdCompany = ?6");
			query.setParameter(1, RCT_TYP);
			query.setParameter(2, RCT_DT_FRM);
			query.setParameter(3, RCT_DT_TO);
			query.setParameter(4, CST_NM);
			query.setParameter(5, TC_TYP);
			query.setParameter(6, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findPostedRctByRctTypeAndRctDateRangeAndCstNameAndTcType(java.lang.String RCT_TYP, java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.String CST_NM, java.lang.String TC_TYP, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedRctByRctDateRange(java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctPosted = 0 AND rct.rctVoid = 0 AND rct.rctDate >= ?1 AND rct.rctDate <= ?2 AND rct.rctAdCompany = ?3");
			query.setParameter(1, RCT_DT_FRM);
			query.setParameter(2, RCT_DT_TO);
			query.setParameter(3, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findUnpostedRctByRctDateRange(java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedRctByRctDateRangeByBranch(java.util.Date RCT_DT_FRM,
			java.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctPosted = 0 AND rct.rctVoid = 0 AND rct.rctDate >= ?1 AND rct.rctDate <= ?2 AND rct.rctAdBranch = ?3 AND rct.rctAdCompany = ?4");
			query.setParameter(1, RCT_DT_FRM);
			query.setParameter(2, RCT_DT_TO);
			query.setParameter(3, RCT_AD_BRNCH);
			query.setParameter(4, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findUnpostedRctByRctDateRangeByBranch(java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedRctByCstCustomerCode(java.lang.String CST_CSTMR_CODE,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctPosted = 0 AND rct.rctVoid = 0 AND rct.rctType = 'COLLECTION' AND rct.arCustomer.cstCustomerCode = ?1 AND rct.rctAdCompany = ?2");
			query.setParameter(1, CST_CSTMR_CODE);
			query.setParameter(2, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findUnpostedRctByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUndepositedPostedRctByBaNameAndBrCode(java.lang.String BA_NM,
			java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctLock = 0 AND rct.rctReconciled = 0 AND rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.adBankAccount.baName = ?1 AND rct.rctAdBranch = ?2 AND rct.rctAdCompany = ?3");
			query.setParameter(1, BA_NM);
			query.setParameter(2, RCT_AD_BRNCH);
			query.setParameter(3, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findUndepositedPostedRctByBaNameAndBrCode(java.lang.String BA_NM, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUndepositedPostedRctByBaNameAndBrCodeAndRctDateRange(java.util.Date RCT_DT_FRM,
			java.util.Date RCT_DT_TO, java.lang.String BA_NM, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctLock = 0 AND rct.rctReconciled = 0 AND rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.rctDate >= ?1 AND rct.rctDate <= ?2 AND rct.adBankAccount.baName = ?3 AND rct.rctAdBranch = ?4 AND rct.rctAdCompany = ?5");
			query.setParameter(1, RCT_DT_FRM);
			query.setParameter(2, RCT_DT_TO);
			query.setParameter(3, BA_NM);
			query.setParameter(4, RCT_AD_BRNCH);
			query.setParameter(5, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findUndepositedPostedRctByBaNameAndBrCodeAndRctDateRange(java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.String BA_NM, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByRctDateAndBaName(java.util.Date RCT_DT, java.lang.String BA_NM,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctDate = ?1 AND rct.adBankAccount.baName LIKE ?2 AND rct.rctAdCompany = ?3");
			query.setParameter(1, RCT_DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findByRctDateAndBaName(java.com.util.Date RCT_DT, java.lang.String BA_NM, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedRctByBeforeOrEqualRctDateAndRctTypeAndCstCustomerCode(java.util.Date RCT_DT,
			java.lang.String RCT_TYP, java.lang.String CST_CSTMR_CD, java.lang.Integer RCT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctPosted = 1 AND rct.rctDate <= ?1 AND rct.rctType = ?2 AND rct.arCustomer.cstCustomerCode = ?3 AND rct.rctAdCompany = ?4 ORDER BY rct.rctDate");
			query.setParameter(1, RCT_DT);
			query.setParameter(2, RCT_TYP);
			query.setParameter(3, CST_CSTMR_CD);
			query.setParameter(4, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findPostedRctByBeforeOrEqualRctDateAndRctTypeAndCstCustomerCode(java.com.util.Date RCT_DT, java.lang.String RCT_TYP, java.lang.String CST_CSTMR_CD, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findOpenDepositEnabledPostedRctByCstCustomerCode(java.lang.String CST_CSTMR_CODE,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctPosted = 1 AND rct.rctVoid = 0  AND rct.rctType = 'MISC' AND rct.rctCustomerDeposit = 1 AND rct.rctAmount > rct.rctAppliedDeposit AND rct.arCustomer.cstCustomerCode = ?1 AND rct.rctAdCompany = ?2");
			query.setParameter(1, CST_CSTMR_CODE);
			query.setParameter(2, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findOpenDepositEnabledPostedRctByCstCustomerCode (java.lang.String CST_CSTMR_CODE, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findOpenDepositEnabledPostedRctByCstCustomerCodeOrderBySlp(java.util.Date RCT_DT,
			java.lang.String CST_CSTMR_CODE, java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctPosted = 1 AND rct.rctVoid = 0  AND rct.rctDate <= ?1 AND rct.rctType = 'MISC' AND rct.rctCustomerDeposit = 1 AND rct.rctAmount > rct.rctAppliedDeposit AND rct.arCustomer.cstCustomerCode = ?2 AND rct.rctAdCompany = ?3");
			query.setParameter(1, RCT_DT);
			query.setParameter(2, CST_CSTMR_CODE);
			query.setParameter(3, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findOpenDepositEnabledPostedRctByCstCustomerCodeOrderBySlp (java.com.util.Date RCT_DT, java.lang.String CST_CSTMR_CODE, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAppliedDepositEnabledPostedRctByCstCustomerCode(java.lang.String CST_CSTMR_CODE,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctPosted = 1 AND rct.rctVoid = 0  AND rct.rctType = 'MISC' AND rct.rctCustomerDeposit = 1 AND rct.rctAppliedDeposit > 0 AND rct.arCustomer.cstCustomerCode = ?1 AND rct.rctAdCompany = ?2");
			query.setParameter(1, CST_CSTMR_CODE);
			query.setParameter(2, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findAppliedDepositEnabledPostedRctByCstCustomerCode (java.lang.String CST_CSTMR_CODE, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByRctLvShift(java.lang.String RCT_LV_SHFT, java.lang.Integer RCT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctLvShift = ?1 AND rct.rctAdCompany = ?2");
			query.setParameter(1, RCT_LV_SHFT);
			query.setParameter(2, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findByRctLvShift(java.lang.String RCT_LV_SHFT, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findRctByAdCompanyAll(java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctAdCompany = ?1");
			query.setParameter(1, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findRctByAdCompanyAll(java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByRctTypeAndRbName(java.lang.String RCT_TYPE, java.lang.String RB_NM,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctType = ?1 AND rct.arReceiptBatch.rbName=?2 AND rct.rctAdCompany = ?3");
			query.setParameter(1, RCT_TYPE);
			query.setParameter(2, RB_NM);
			query.setParameter(3, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findByRctTypeAndRbName(java.lang.String RCT_TYPE, java.lang.String RB_NM, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArReceipt findByRctDateAndRctNumberAndCstCustomerCodeAndBrCode(java.util.Date RCT_DT,
			java.lang.String RCT_NMBR, java.lang.String CST_CSTMR_CODE, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctDate = ?1 AND rct.rctNumber = ?2 AND rct.arCustomer.cstCustomerCode = ?3 AND rct.rctAdBranch = ?4 AND rct.rctAdCompany = ?5");
			query.setParameter(1, RCT_DT);
			query.setParameter(2, RCT_NMBR);
			query.setParameter(3, CST_CSTMR_CODE);
			query.setParameter(4, RCT_AD_BRNCH);
			query.setParameter(5, RCT_AD_CMPNY);
            return (LocalArReceipt) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArReceiptHome.findByRctDateAndRctNumberAndCstCustomerCodeAndBrCode(java.com.util.Date RCT_DT, java.lang.String RCT_NMBR, java.lang.String CST_CSTMR_CODE, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findByRctDateAndRctNumberAndCstCustomerCodeAndBrCode(java.com.util.Date RCT_DT, java.lang.String RCT_NMBR, java.lang.String CST_CSTMR_CODE, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedRctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM,
			java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctLock = 0 AND rct.rctPosted = 1 AND rct.rctAmountCash > 0 AND rct.rctVoid = 0 AND rct.adBankAccount.baIsCashAccount = 0 AND rct.adBankAccount.baName = ?1 AND rct.rctDate >= ?2 AND rct.rctDate <= ?3 AND rct.rctAdBranch = ?4 AND rct.rctAdCompany = ?5");
			query.setParameter(1, BA_NM);
			query.setParameter(2, RCT_DT_FRM);
			query.setParameter(3, RCT_DT_TO);
			query.setParameter(4, RCT_AD_BRNCH);
			query.setParameter(5, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findPostedRctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM, java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedCard1RctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM,
			java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctLock = 0 AND rct.rctPosted = 1 AND rct.rctAmountCard1 > 0 AND rct.rctVoid = 0 AND rct.adBankAccountCard1.baIsCashAccount = 0 AND rct.adBankAccountCard1.baName = ?1 AND rct.rctDate >= ?2 AND rct.rctDate <= ?3 AND rct.rctAdBranch = ?4 AND rct.rctAdCompany = ?5");
			query.setParameter(1, BA_NM);
			query.setParameter(2, RCT_DT_FRM);
			query.setParameter(3, RCT_DT_TO);
			query.setParameter(4, RCT_AD_BRNCH);
			query.setParameter(5, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findPostedCard1RctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM, java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedCard2RctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM,
			java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctLock = 0 AND rct.rctPosted = 1 AND rct.rctAmountCard2 > 0 AND rct.rctVoid = 0 AND rct.adBankAccountCard2.baIsCashAccount = 0 AND rct.adBankAccountCard2.baName = ?1 AND rct.rctDate >= ?2 AND rct.rctDate <= ?3 AND rct.rctAdBranch = ?4 AND rct.rctAdCompany = ?5");
			query.setParameter(1, BA_NM);
			query.setParameter(2, RCT_DT_FRM);
			query.setParameter(3, RCT_DT_TO);
			query.setParameter(4, RCT_AD_BRNCH);
			query.setParameter(5, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findPostedCard2RctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM, java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedCard3RctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM,
			java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctLock = 0 AND rct.rctPosted = 1 AND rct.rctAmountCard3 > 0 AND rct.rctVoid = 0 AND rct.adBankAccountCard3.baIsCashAccount = 0 AND rct.adBankAccountCard3.baName = ?1 AND rct.rctDate >= ?2 AND rct.rctDate <= ?3 AND rct.rctAdBranch = ?4 AND rct.rctAdCompany = ?5");
			query.setParameter(1, BA_NM);
			query.setParameter(2, RCT_DT_FRM);
			query.setParameter(3, RCT_DT_TO);
			query.setParameter(4, RCT_AD_BRNCH);
			query.setParameter(5, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findPostedCard3RctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM, java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedChequeRctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM,
			java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctLock = 0 AND rct.rctPosted = 1 AND rct.rctAmountCheque > 0 AND rct.rctVoid = 0 AND rct.adBankAccount.baIsCashAccount = 0 AND rct.adBankAccount.baName = ?1 AND rct.rctDate >= ?2 AND rct.rctDate <= ?3 AND rct.rctAdBranch = ?4 AND rct.rctAdCompany = ?5");
			query.setParameter(1, BA_NM);
			query.setParameter(2, RCT_DT_FRM);
			query.setParameter(3, RCT_DT_TO);
			query.setParameter(4, RCT_AD_BRNCH);
			query.setParameter(5, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findPostedChequeRctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM, java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findReconciledPostedRctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM,
			java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctLock = 0 AND rct.rctPosted = 1 AND rct.rctAmountCash > 0 AND rct.rctVoid = 0 AND rct.rctReconciled = 1 AND rct.adBankAccount.baIsCashAccount = 0 AND rct.adBankAccount.baName = ?1 AND rct.rctDate >= ?2 AND rct.rctDate <= ?3 AND rct.rctAdBranch = ?4 AND rct.rctAdCompany = ?5");
			query.setParameter(1, BA_NM);
			query.setParameter(2, RCT_DT_FRM);
			query.setParameter(3, RCT_DT_TO);
			query.setParameter(4, RCT_AD_BRNCH);
			query.setParameter(5, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findReconciledPostedRctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM, java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findReconciledPostedCard1RctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM,
			java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctLock = 0 AND rct.rctPosted = 1 AND rct.rctAmountCard1 > 0 AND rct.rctVoid = 0 AND rct.rctReconciledCard1 = 1 AND rct.adBankAccountCard1.baIsCashAccount = 0 AND rct.adBankAccountCard1.baName = ?1 AND rct.rctDate >= ?2 AND rct.rctDate <= ?3 AND rct.rctAdBranch = ?4 AND rct.rctAdCompany = ?5");
			query.setParameter(1, BA_NM);
			query.setParameter(2, RCT_DT_FRM);
			query.setParameter(3, RCT_DT_TO);
			query.setParameter(4, RCT_AD_BRNCH);
			query.setParameter(5, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findReconciledPostedCard1RctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM, java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findReconciledPostedCard2RctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM,
			java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctLock = 0 AND rct.rctPosted = 1 AND rct.rctAmountCard2 > 0 AND rct.rctVoid = 0 AND rct.rctReconciledCard2 = 1 AND rct.adBankAccountCard2.baIsCashAccount = 0 AND rct.adBankAccountCard2.baName = ?1 AND rct.rctDate >= ?2 AND rct.rctDate <= ?3 AND rct.rctAdBranch = ?4 AND rct.rctAdCompany = ?5");
			query.setParameter(1, BA_NM);
			query.setParameter(2, RCT_DT_FRM);
			query.setParameter(3, RCT_DT_TO);
			query.setParameter(4, RCT_AD_BRNCH);
			query.setParameter(5, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findReconciledPostedCard2RctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM, java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findReconciledPostedCard3RctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM,
			java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctLock = 0 AND rct.rctPosted = 1 AND rct.rctAmountCard3 > 0 AND rct.rctVoid = 0 AND rct.rctReconciledCard3 = 1 AND rct.adBankAccountCard3.baIsCashAccount = 0 AND rct.adBankAccountCard3.baName = ?1 AND rct.rctDate >= ?2 AND rct.rctDate <= ?3 AND rct.rctAdBranch = ?4 AND rct.rctAdCompany = ?5");
			query.setParameter(1, BA_NM);
			query.setParameter(2, RCT_DT_FRM);
			query.setParameter(3, RCT_DT_TO);
			query.setParameter(4, RCT_AD_BRNCH);
			query.setParameter(5, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findReconciledPostedCard3RctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM, java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findReconciledPostedChequeRctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM,
			java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctLock = 0 AND rct.rctPosted = 1 AND rct.rctAmountCheque > 0 AND rct.rctVoid = 0 AND rct.rctReconciledCheque = 1 AND rct.adBankAccount.baIsCashAccount = 0 AND rct.adBankAccount.baName = ?1 AND rct.rctDate >= ?2 AND rct.rctDate <= ?3 AND rct.rctAdBranch = ?4 AND rct.rctAdCompany = ?5");
			query.setParameter(1, BA_NM);
			query.setParameter(2, RCT_DT_FRM);
			query.setParameter(3, RCT_DT_TO);
			query.setParameter(4, RCT_AD_BRNCH);
			query.setParameter(5, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findReconciledPostedChequeRctByBaNameAndRctDateRangeAndBrCode(java.lang.String BA_NM, java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findInvestorReceiptByIrabCode(java.lang.Integer IRAB_CODE,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE rct.rctLock = 0 AND rct.rctPosted = 1 AND rct.rctVoid = 0 AND rct.glInvestorAccountBalance.irabCode = ?1 AND rct.rctAdCompany = ?2");
			query.setParameter(1, IRAB_CODE);
			query.setParameter(2, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findInvestorReceiptByIrabCode(java.lang.Integer IRAB_CODE, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findRctForPostingByBranch(java.lang.String RCT_TYP, java.lang.Integer RCT_AD_BRNCH,
			java.lang.Integer RCT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct  WHERE  rct.rctType=?1 AND rct.rctPosted = 0 AND rct.rctVoid = 0 AND (rct.rctApprovalStatus='APPROVED' OR rct.rctApprovalStatus='N/A') AND rct.rctAdBranch=?2 AND rct.rctAdCompany=?3");
			query.setParameter(1, RCT_TYP);
			query.setParameter(2, RCT_AD_BRNCH);
			query.setParameter(3, RCT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findRctForPostingByBranch(java.lang.String RCT_TYP, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer RCT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedTaxRctByRctDateRange(java.util.Date CHK_DT_FRM, java.util.Date CHK_DT_TO,
			java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct, IN(rct.arAppliedInvoices) ai WHERE rct.rctVoid = 0 AND ai.arInvoicePaymentSchedule.arInvoice.arTaxCode.tcType IN ('EXEMPT','ZERO-RATED','INCLUSIVE','EXCLUSIVE') AND rct.rctPosted = 1 AND rct.rctDate >= ?1 AND rct.rctDate <= ?2 AND rct.rctAdBranch = ?3 AND rct.rctAdCompany = ?4 ORDER BY rct.rctDate, rct.rctNumber");
			query.setParameter(1, CHK_DT_FRM);
			query.setParameter(2, CHK_DT_TO);
			query.setParameter(3, CHK_AD_BRNCH);
			query.setParameter(4, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findPostedTaxRctByRctDateRange(java.com.util.Date CHK_DT_FRM, java.com.util.Date CHK_DT_TO, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedTaxMiscRctByRctDateRange(java.util.Date CHK_DT_FRM, java.util.Date CHK_DT_TO,
			java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rct) FROM ArReceipt rct WHERE rct.rctType = 'MISC' AND rct.rctVoid = 0 AND rct.arTaxCode.tcType IN ('EXEMPT','ZERO-RATED','INCLUSIVE','EXCLUSIVE') AND rct.rctPosted = 1 AND rct.rctDate >= ?1 AND rct.rctDate <= ?2 AND rct.rctAdBranch = ?3 AND rct.rctAdCompany = ?4 ORDER BY rct.rctDate, rct.rctNumber");
			query.setParameter(1, CHK_DT_FRM);
			query.setParameter(2, CHK_DT_TO);
			query.setParameter(3, CHK_AD_BRNCH);
			query.setParameter(4, CHK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptHome.findPostedTaxMiscRctByRctDateRange(java.com.util.Date CHK_DT_FRM, java.com.util.Date CHK_DT_TO, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer CHK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getRctByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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

	public LocalArReceipt buildReceipt(String companyShortName) throws CreateException {
		try {
			LocalArReceipt entity = new LocalArReceipt();
			Debug.print("ArReceiptBean buildReceipt");
			entity.setRctType(RCT_TYP);
			entity.setRctDescription(RCT_DESC);
			entity.setRctDate(RCT_DT);
			entity.setRctNumber(RCT_NMBR);
			entity.setRctReferenceNumber(RCT_RFRNC_NMBR);
			entity.setRctCheckNo(RCT_CHCK_NO);
			entity.setRctPayfileReferenceNumber(RCT_PYFL_RFRNC_NMBR);
			entity.setRctChequeNumber(RCT_CHQ_NMBR);
			entity.setRctVoucherNumber(RCT_VCHR_NMBR);
			entity.setRctCardNumber1(RCT_CRD_NMBR1);
			entity.setRctCardNumber2(RCT_CRD_NMBR2);
			entity.setRctCardNumber3(RCT_CRD_NMBR3);
			entity.setRctAmount(RCT_AMNT);
			entity.setRctAmountCash(RCT_AMNT_CSH);
			entity.setRctAmountCheque(RCT_AMNT_CHQ);
			entity.setRctAmountVoucher(RCT_AMNT_VCHR);
			entity.setRctAmountCard1(RCT_AMNT_CRD1);
			entity.setRctAmountCard2(RCT_AMNT_CRD2);
			entity.setRctAmountCard3(RCT_AMNT_CRD3);
			entity.setRctConversionDate(RCT_CNVRSN_DT);
			entity.setRctConversionRate(RCT_CNVRSN_RT);
			entity.setRctSoldTo(RCT_SLD_TO);
			entity.setRctPaymentMethod(RCT_PYMNT_MTHD);
			entity.setRctCustomerDeposit(RCT_CSTMR_DPST);
			entity.setRctAppliedDeposit(RCT_APPLD_DPST);
			entity.setRctApprovalStatus(RCT_APPRVL_STATUS);
			entity.setRctReasonForRejection(RCT_RSN_FR_RJCTN);
			entity.setRctPosted(RCT_PSTD);
			entity.setRctVoidApprovalStatus(RCT_VD_APPRVL_STATUS);
			entity.setRctVoidPosted(RCT_VD_PSTD);
			entity.setRctVoid(RCT_VD);

			entity.setRctReconciled(RCT_RCNCLD);
			entity.setRctReconciledCheque(RCT_RCNCLD_CHQ);
			entity.setRctReconciledCard1(RCT_RCNCLD_CRD1);
			entity.setRctReconciledCard2(RCT_RCNCLD_CRD2);
			entity.setRctReconciledCard3(RCT_RCNCLD_CRD3);
			entity.setRctDateReconciled(RCT_DT_RCNCLD);
			entity.setRctDateReconciledCheque(RCT_DT_RCNCLD_CHQ);
			entity.setRctDateReconciledCard1(RCT_DT_RCNCLD_CRD1);
			entity.setRctDateReconciledCard2(RCT_DT_RCNCLD_CRD2);
			entity.setRctDateReconciledCard3(RCT_DT_RCNCLD_CRD3);

			entity.setRctCreatedBy(RCT_CRTD_BY);
			entity.setRctDateCreated(RCT_DT_CRTD);
			entity.setRctLastModifiedBy(RCT_LST_MDFD_BY);
			entity.setRctDateLastModified(RCT_DT_LST_MDFD);
			entity.setRctApprovedRejectedBy(RCT_APPRVD_RJCTD_BY);
			entity.setRctDateApprovedRejected(RCT_DT_APPRVD_RJCTD);
			entity.setRctPostedBy(RCT_PSTD_BY);
			entity.setRctDatePosted(RCT_DT_PSTD);
			entity.setRctPrinted(RCT_PRNTD);
			entity.setRctLvShift(RCT_LV_SHFT);
			entity.setRctLock(RCT_LCK);
			entity.setRctSubjectToCommission(RCT_SBJCT_TO_CMMSSN);
			entity.setRctCustomerName(RCT_CST_NM);
			entity.setRctCustomerAddress(RCT_CST_ADRSS);
			entity.setRctInvtrBeginningBalance(RCT_INVTR_BGNNG_BLNC);
			entity.setRctInvtrInvestorFund(RCT_INVTR_IF);
			entity.setRctInvtrNextRunDate(RCT_INVTR_NXT_RN_DT);
			entity.setRctAdBranch(RCT_AD_BRNCH);
			entity.setRctAdCompany(RCT_AD_CMPNY);

			em.persist(entity, companyShortName);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArReceipt buildReceipt() throws CreateException {
		try {
			LocalArReceipt entity = new LocalArReceipt();
			Debug.print("ArReceiptBean buildReceipt");
			entity.setRctType(RCT_TYP);
			entity.setRctDescription(RCT_DESC);
			entity.setRctDate(RCT_DT);
			entity.setRctNumber(RCT_NMBR);
			entity.setRctReferenceNumber(RCT_RFRNC_NMBR);
			entity.setRctCheckNo(RCT_CHCK_NO);
			entity.setRctPayfileReferenceNumber(RCT_PYFL_RFRNC_NMBR);
			entity.setRctChequeNumber(RCT_CHQ_NMBR);
			entity.setRctVoucherNumber(RCT_VCHR_NMBR);
			entity.setRctCardNumber1(RCT_CRD_NMBR1);
			entity.setRctCardNumber2(RCT_CRD_NMBR2);
			entity.setRctCardNumber3(RCT_CRD_NMBR3);
			entity.setRctAmount(RCT_AMNT);
			entity.setRctAmountCash(RCT_AMNT_CSH);
			entity.setRctAmountCheque(RCT_AMNT_CHQ);
			entity.setRctAmountVoucher(RCT_AMNT_VCHR);
			entity.setRctAmountCard1(RCT_AMNT_CRD1);
			entity.setRctAmountCard2(RCT_AMNT_CRD2);
			entity.setRctAmountCard3(RCT_AMNT_CRD3);
			entity.setRctConversionDate(RCT_CNVRSN_DT);
			entity.setRctConversionRate(RCT_CNVRSN_RT);
			entity.setRctSoldTo(RCT_SLD_TO);
			entity.setRctPaymentMethod(RCT_PYMNT_MTHD);
			entity.setRctCustomerDeposit(RCT_CSTMR_DPST);
			entity.setRctAppliedDeposit(RCT_APPLD_DPST);
			entity.setRctApprovalStatus(RCT_APPRVL_STATUS);
			entity.setRctReasonForRejection(RCT_RSN_FR_RJCTN);
			entity.setRctPosted(RCT_PSTD);
			entity.setRctVoidApprovalStatus(RCT_VD_APPRVL_STATUS);
			entity.setRctVoidPosted(RCT_VD_PSTD);
			entity.setRctVoid(RCT_VD);

			entity.setRctReconciled(RCT_RCNCLD);
			entity.setRctReconciledCheque(RCT_RCNCLD_CHQ);
			entity.setRctReconciledCard1(RCT_RCNCLD_CRD1);
			entity.setRctReconciledCard2(RCT_RCNCLD_CRD2);
			entity.setRctReconciledCard3(RCT_RCNCLD_CRD3);
			entity.setRctDateReconciled(RCT_DT_RCNCLD);
			entity.setRctDateReconciledCheque(RCT_DT_RCNCLD_CHQ);
			entity.setRctDateReconciledCard1(RCT_DT_RCNCLD_CRD1);
			entity.setRctDateReconciledCard2(RCT_DT_RCNCLD_CRD2);
			entity.setRctDateReconciledCard3(RCT_DT_RCNCLD_CRD3);

			entity.setRctCreatedBy(RCT_CRTD_BY);
			entity.setRctDateCreated(RCT_DT_CRTD);
			entity.setRctLastModifiedBy(RCT_LST_MDFD_BY);
			entity.setRctDateLastModified(RCT_DT_LST_MDFD);
			entity.setRctApprovedRejectedBy(RCT_APPRVD_RJCTD_BY);
			entity.setRctDateApprovedRejected(RCT_DT_APPRVD_RJCTD);
			entity.setRctPostedBy(RCT_PSTD_BY);
			entity.setRctDatePosted(RCT_DT_PSTD);
			entity.setRctPrinted(RCT_PRNTD);
			entity.setRctLvShift(RCT_LV_SHFT);
			entity.setRctLock(RCT_LCK);
			entity.setRctSubjectToCommission(RCT_SBJCT_TO_CMMSSN);
			entity.setRctCustomerName(RCT_CST_NM);
			entity.setRctCustomerAddress(RCT_CST_ADRSS);
			entity.setRctInvtrBeginningBalance(RCT_INVTR_BGNNG_BLNC);
			entity.setRctInvtrInvestorFund(RCT_INVTR_IF);
			entity.setRctInvtrNextRunDate(RCT_INVTR_NXT_RN_DT);
			entity.setRctAdBranch(RCT_AD_BRNCH);
			entity.setRctAdCompany(RCT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArReceiptHome RctType(String RCT_TYP) {
		this.RCT_TYP = RCT_TYP;
		return this;
	}

	public LocalArReceiptHome RctDescription(String RCT_DESC) {
		this.RCT_DESC = RCT_DESC;
		return this;
	}

	public LocalArReceiptHome RctDate(Date RCT_DT) {
		this.RCT_DT = RCT_DT;
		return this;
	}

	public LocalArReceiptHome RctNumber(String RCT_NMBR) {
		this.RCT_NMBR = RCT_NMBR;
		return this;
	}

	public LocalArReceiptHome RctReferenceNumber(String RCT_RFRNC_NMBR) {
		this.RCT_RFRNC_NMBR = RCT_RFRNC_NMBR;
		return this;
	}

	public LocalArReceiptHome RctCheckNo(String RCT_CHCK_NO) {
		this.RCT_CHCK_NO = RCT_CHCK_NO;
		return this;
	}

	public LocalArReceiptHome RctPayfileReferenceNumber(String RCT_PYFL_RFRNC_NMBR) {
		this.RCT_PYFL_RFRNC_NMBR = RCT_PYFL_RFRNC_NMBR;
		return this;
	}

	public LocalArReceiptHome RctAmount(double RCT_AMNT) {
		this.RCT_AMNT = RCT_AMNT;
		return this;
	}

	public LocalArReceiptHome RctAmountCash(double RCT_AMNT_CSH) {
		this.RCT_AMNT_CSH = RCT_AMNT_CSH;
		return this;
	}

	public LocalArReceiptHome RctConversionDate(Date RCT_CNVRSN_DT) {
		this.RCT_CNVRSN_DT = RCT_CNVRSN_DT;
		return this;
	}

	public LocalArReceiptHome RctConversionRate(double RCT_CNVRSN_RT) {
		this.RCT_CNVRSN_RT = RCT_CNVRSN_RT;
		return this;
	}

	public LocalArReceiptHome RctPaymentMethod(String RCT_PYMNT_MTHD) {
		this.RCT_PYMNT_MTHD = RCT_PYMNT_MTHD;
		return this;
	}

	public LocalArReceiptHome RctCreatedBy(String RCT_CRTD_BY) {
		this.RCT_CRTD_BY = RCT_CRTD_BY;
		return this;
	}

	public LocalArReceiptHome RctDateCreated(Date RCT_DT_CRTD) {
		this.RCT_DT_CRTD = RCT_DT_CRTD;
		return this;
	}

	public LocalArReceiptHome RctAdBranch(Integer RCT_AD_BRNCH) {
		this.RCT_AD_BRNCH = RCT_AD_BRNCH;
		return this;
	}

	public LocalArReceiptHome RctAdCompany(Integer RCT_AD_CMPNY) {
		this.RCT_AD_CMPNY = RCT_AD_CMPNY;
		return this;
	}

	public LocalArReceipt create(Integer RCT_CODE, String RCT_TYP, String RCT_DESC, Date RCT_DT,
                                 String RCT_NMBR, String RCT_RFRNC_NMBR, String RCT_CHCK_NO, String RCT_PYFL_RFRNC_NMBR, String RCT_CHQ_NMBR,
                                 String RCT_VCHR_NMBR, String RCT_CRD_NMBR1, String RCT_CRD_NMBR2, String RCT_CRD_NMBR3, double RCT_AMNT,
                                 double RCT_AMNT_CSH, double RCT_AMNT_CHQ, double RCT_AMNT_VCHR, double RCT_AMNT_CRD1, double RCT_AMNT_CRD2,
                                 double RCT_AMNT_CRD3, Date RCT_CNVRSN_DT, double RCT_CNVRSN_RT, String RCT_SLD_TO, String RCT_PYMNT_MTHD,
                                 byte RCT_CSTMR_DPST, double RCT_APPLD_DPST, String RCT_APPRVL_STATUS, String RCT_RSN_FR_RJCTN,
                                 byte RCT_PSTD, String RCT_VD_APPRVL_STATUS, byte RCT_VD_PSTD, byte RCT_VD, byte RCT_RCNCLD,
                                 byte RCT_RCNCLD_CHQ, byte RCT_RCNCLD_CRD1, byte RCT_RCNCLD_CRD2, byte RCT_RCNCLD_CRD3, Date RCT_DT_RCNCLD,
                                 Date RCT_DT_RCNCLD_CHQ, Date RCT_DT_RCNCLD_CRD1, Date RCT_DT_RCNCLD_CRD2, Date RCT_DT_RCNCLD_CRD3,
                                 String RCT_CRTD_BY, Date RCT_DT_CRTD, String RCT_LST_MDFD_BY, Date RCT_DT_LST_MDFD,
                                 String RCT_APPRVD_RJCTD_BY, Date RCT_DT_APPRVD_RJCTD, String RCT_PSTD_BY, Date RCT_DT_PSTD, byte RCT_PRNTD,
                                 String RCT_LV_SHFT, byte RCT_LCK, byte RCT_SBJCT_TO_CMMSSN, String RCT_CST_NM, String RCT_CST_ADRSS,
                                 byte RCT_INVTR_BGNNG_BLNC, byte RCT_INVTR_IF, Date RCT_INVTR_NXT_RN_DT, Integer RCT_AD_BRNCH,
                                 Integer RCT_AD_CMPNY) throws CreateException {
		try {

			LocalArReceipt entity = new LocalArReceipt();

			Debug.print("ArReceiptBean create");

			entity.setRctCode(RCT_CODE);
			entity.setRctType(RCT_TYP);
			entity.setRctDescription(RCT_DESC);
			entity.setRctDate(RCT_DT);
			entity.setRctNumber(RCT_NMBR);
			entity.setRctReferenceNumber(RCT_RFRNC_NMBR);
			entity.setRctCheckNo(RCT_CHCK_NO);
			entity.setRctPayfileReferenceNumber(RCT_PYFL_RFRNC_NMBR);
			entity.setRctChequeNumber(RCT_CHQ_NMBR);
			entity.setRctVoucherNumber(RCT_VCHR_NMBR);
			entity.setRctCardNumber1(RCT_CRD_NMBR1);
			entity.setRctCardNumber2(RCT_CRD_NMBR2);
			entity.setRctCardNumber3(RCT_CRD_NMBR3);
			entity.setRctAmount(RCT_AMNT);
			entity.setRctAmountCash(RCT_AMNT_CSH);
			entity.setRctAmountCheque(RCT_AMNT_CHQ);
			entity.setRctAmountVoucher(RCT_AMNT_VCHR);
			entity.setRctAmountCard1(RCT_AMNT_CRD1);
			entity.setRctAmountCard2(RCT_AMNT_CRD2);
			entity.setRctAmountCard3(RCT_AMNT_CRD3);
			entity.setRctConversionDate(RCT_CNVRSN_DT);
			entity.setRctConversionRate(RCT_CNVRSN_RT);
			entity.setRctSoldTo(RCT_SLD_TO);
			entity.setRctPaymentMethod(RCT_PYMNT_MTHD);
			entity.setRctCustomerDeposit(RCT_CSTMR_DPST);
			entity.setRctAppliedDeposit(RCT_APPLD_DPST);
			entity.setRctApprovalStatus(RCT_APPRVL_STATUS);
			entity.setRctReasonForRejection(RCT_RSN_FR_RJCTN);
			entity.setRctPosted(RCT_PSTD);
			entity.setRctVoidApprovalStatus(RCT_VD_APPRVL_STATUS);
			entity.setRctVoidPosted(RCT_VD_PSTD);
			entity.setRctVoid(RCT_VD);

			entity.setRctReconciled(RCT_RCNCLD);
			entity.setRctReconciledCheque(RCT_RCNCLD_CHQ);
			entity.setRctReconciledCard1(RCT_RCNCLD_CRD1);
			entity.setRctReconciledCard2(RCT_RCNCLD_CRD2);
			entity.setRctReconciledCard3(RCT_RCNCLD_CRD3);
			entity.setRctDateReconciled(RCT_DT_RCNCLD);
			entity.setRctDateReconciledCheque(RCT_DT_RCNCLD_CHQ);
			entity.setRctDateReconciledCard1(RCT_DT_RCNCLD_CRD1);
			entity.setRctDateReconciledCard2(RCT_DT_RCNCLD_CRD2);
			entity.setRctDateReconciledCard3(RCT_DT_RCNCLD_CRD3);

			entity.setRctCreatedBy(RCT_CRTD_BY);
			entity.setRctDateCreated(RCT_DT_CRTD);
			entity.setRctLastModifiedBy(RCT_LST_MDFD_BY);
			entity.setRctDateLastModified(RCT_DT_LST_MDFD);
			entity.setRctApprovedRejectedBy(RCT_APPRVD_RJCTD_BY);
			entity.setRctDateApprovedRejected(RCT_DT_APPRVD_RJCTD);
			entity.setRctPostedBy(RCT_PSTD_BY);
			entity.setRctDatePosted(RCT_DT_PSTD);
			entity.setRctPrinted(RCT_PRNTD);
			entity.setRctLvShift(RCT_LV_SHFT);
			entity.setRctLock(RCT_LCK);
			entity.setRctSubjectToCommission(RCT_SBJCT_TO_CMMSSN);
			entity.setRctCustomerName(RCT_CST_NM);
			entity.setRctCustomerAddress(RCT_CST_ADRSS);
			entity.setRctInvtrBeginningBalance(RCT_INVTR_BGNNG_BLNC);
			entity.setRctInvtrInvestorFund(RCT_INVTR_IF);
			entity.setRctInvtrNextRunDate(RCT_INVTR_NXT_RN_DT);
			entity.setRctAdBranch(RCT_AD_BRNCH);
			entity.setRctAdCompany(RCT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArReceipt create(String RCT_TYP, String RCT_DESC, Date RCT_DT, String RCT_NMBR,
                                 String RCT_RFRNC_NMBR, String RCT_CHCK_NO, String RCT_PYFL_RFRNC_NMBR, String RCT_CHQ_NMBR,
                                 String RCT_VCHR_NMBR, String RCT_CRD_NMBR1, String RCT_CRD_NMBR2, String RCT_CRD_NMBR3, double RCT_AMNT,
                                 double RCT_AMNT_CSH, double RCT_AMNT_CHQ, double RCT_AMNT_VCHR, double RCT_AMNT_CRD1, double RCT_AMNT_CRD2,
                                 double RCT_AMNT_CRD3, Date RCT_CNVRSN_DT, double RCT_CNVRSN_RT, String RCT_SLD_TO, String RCT_PYMNT_MTHD,
                                 byte RCT_CSTMR_DPST, double RCT_APPLD_DPST, String RCT_APPRVL_STATUS, String RCT_RSN_FR_RJCTN,
                                 byte RCT_PSTD, String RCT_VD_APPRVL_STATUS, byte RCT_VD_PSTD, byte RCT_VD, byte RCT_RCNCLD,
                                 byte RCT_RCNCLD_CHQ, byte RCT_RCNCLD_CRD1, byte RCT_RCNCLD_CRD2, byte RCT_RCNCLD_CRD3, Date RCT_DT_RCNCLD,
                                 Date RCT_DT_RCNCLD_CHQ, Date RCT_DT_RCNCLD_CRD1, Date RCT_DT_RCNCLD_CRD2, Date RCT_DT_RCNCLD_CRD3,
                                 String RCT_CRTD_BY, Date RCT_DT_CRTD, String RCT_LST_MDFD_BY, Date RCT_DT_LST_MDFD,
                                 String RCT_APPRVD_RJCTD_BY, Date RCT_DT_APPRVD_RJCTD, String RCT_PSTD_BY, Date RCT_DT_PSTD, byte RCT_PRNTD,
                                 String RCT_LV_SHFT, byte RCT_LCK, byte RCT_SBJCT_TO_CMMSSN, String RCT_CST_NM, String RCT_CST_ADRSS,
                                 byte RCT_INVTR_BGNNG_BLNC, byte RCT_INVTR_IF, Date RCT_INVTR_NXT_RN_DT, Integer RCT_AD_BRNCH,
                                 Integer RCT_AD_CMPNY) throws CreateException {
		try {

			LocalArReceipt entity = new LocalArReceipt();

			Debug.print("ArReceiptBean create");

			entity.setRctType(RCT_TYP);
			entity.setRctDescription(RCT_DESC);
			entity.setRctDate(RCT_DT);
			entity.setRctNumber(RCT_NMBR);
			entity.setRctReferenceNumber(RCT_RFRNC_NMBR);
			entity.setRctCheckNo(RCT_CHCK_NO);
			entity.setRctPayfileReferenceNumber(RCT_PYFL_RFRNC_NMBR);
			entity.setRctChequeNumber(RCT_CHQ_NMBR);
			entity.setRctVoucherNumber(RCT_VCHR_NMBR);
			entity.setRctCardNumber1(RCT_CRD_NMBR1);
			entity.setRctCardNumber2(RCT_CRD_NMBR2);
			entity.setRctCardNumber3(RCT_CRD_NMBR3);
			entity.setRctAmount(RCT_AMNT);
			entity.setRctAmountCash(RCT_AMNT_CSH);
			entity.setRctAmountCheque(RCT_AMNT_CHQ);
			entity.setRctAmountVoucher(RCT_AMNT_VCHR);
			entity.setRctAmountCard1(RCT_AMNT_CRD1);
			entity.setRctAmountCard2(RCT_AMNT_CRD2);
			entity.setRctAmountCard3(RCT_AMNT_CRD3);
			entity.setRctConversionDate(RCT_CNVRSN_DT);
			entity.setRctConversionRate(RCT_CNVRSN_RT);
			entity.setRctSoldTo(RCT_SLD_TO);
			entity.setRctPaymentMethod(RCT_PYMNT_MTHD);
			entity.setRctCustomerDeposit(RCT_CSTMR_DPST);
			entity.setRctAppliedDeposit(RCT_APPLD_DPST);
			entity.setRctApprovalStatus(RCT_APPRVL_STATUS);
			entity.setRctReasonForRejection(RCT_RSN_FR_RJCTN);
			entity.setRctPosted(RCT_PSTD);
			entity.setRctVoidApprovalStatus(RCT_VD_APPRVL_STATUS);
			entity.setRctVoidPosted(RCT_VD_PSTD);
			entity.setRctVoid(RCT_VD);

			entity.setRctReconciled(RCT_RCNCLD);
			entity.setRctReconciledCheque(RCT_RCNCLD_CHQ);
			entity.setRctReconciledCard1(RCT_RCNCLD_CRD1);
			entity.setRctReconciledCard2(RCT_RCNCLD_CRD2);
			entity.setRctReconciledCard3(RCT_RCNCLD_CRD3);
			entity.setRctDateReconciled(RCT_DT_RCNCLD);
			entity.setRctDateReconciledCheque(RCT_DT_RCNCLD_CHQ);
			entity.setRctDateReconciledCard1(RCT_DT_RCNCLD_CRD1);
			entity.setRctDateReconciledCard2(RCT_DT_RCNCLD_CRD2);
			entity.setRctDateReconciledCard3(RCT_DT_RCNCLD_CRD3);

			entity.setRctCreatedBy(RCT_CRTD_BY);
			entity.setRctDateCreated(RCT_DT_CRTD);
			entity.setRctLastModifiedBy(RCT_LST_MDFD_BY);
			entity.setRctDateLastModified(RCT_DT_LST_MDFD);
			entity.setRctApprovedRejectedBy(RCT_APPRVD_RJCTD_BY);
			entity.setRctDateApprovedRejected(RCT_DT_APPRVD_RJCTD);
			entity.setRctPostedBy(RCT_PSTD_BY);
			entity.setRctDatePosted(RCT_DT_PSTD);
			entity.setRctPrinted(RCT_PRNTD);
			entity.setRctLvShift(RCT_LV_SHFT);
			entity.setRctLock(RCT_LCK);
			entity.setRctSubjectToCommission(RCT_SBJCT_TO_CMMSSN);
			entity.setRctCustomerName(RCT_CST_NM);
			entity.setRctCustomerAddress(RCT_CST_ADRSS);
			entity.setRctInvtrBeginningBalance(RCT_INVTR_BGNNG_BLNC);
			entity.setRctInvtrInvestorFund(RCT_INVTR_IF);
			entity.setRctInvtrNextRunDate(RCT_INVTR_NXT_RN_DT);
			entity.setRctAdBranch(RCT_AD_BRNCH);
			entity.setRctAdCompany(RCT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}

	}

}