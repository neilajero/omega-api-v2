package com.ejb.dao.cm;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.cm.LocalCmFundTransfer;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalCmFundTransferHome {

	public static final String JNDI_NAME = "LocalCmFundTransferHome!com.ejb.cm.LocalCmFundTransferHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalCmFundTransferHome() {
	}

	// FINDER METHODS

	public LocalCmFundTransfer findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalCmFundTransfer entity = (LocalCmFundTransfer) em
					.find(new LocalCmFundTransfer(), pk);
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

	public java.util.Collection findUnreconciledPostedFtAccountToByDateAndBaCodeAndBrCode(java.util.Date DT,
			java.lang.Integer BA_CODE, java.lang.Integer FT_AD_BRNCH, java.lang.Integer FT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ft) FROM CmFundTransfer ft WHERE ft.ftDate <= ?1 AND ft.ftAdBaAccountTo = ?2 AND ft.ftAccountToReconciled = 0 AND ft.ftPosted = 1 AND ft.ftVoid = 0 AND ft.ftAdBranch = ?3 AND ft.ftAdCompany = ?4");
			query.setParameter(1, DT);
			query.setParameter(2, BA_CODE);
			query.setParameter(3, FT_AD_BRNCH);
			query.setParameter(4, FT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmFundTransferHome.findUnreconciledPostedFtAccountToByDateAndBaCodeAndBrCode(java.com.util.Date DT, java.lang.Integer BA_CODE, java.lang.Integer FT_AD_BRNCH, java.lang.Integer FT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnreconciledPostedFtAccountToByDateAndBaCode(java.util.Date DT,
			java.lang.Integer BA_CODE, java.lang.Integer FT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ft) FROM CmFundTransfer ft WHERE ft.ftDate <= ?1 AND ft.ftAdBaAccountTo = ?2 AND ft.ftAccountToReconciled = 0 AND ft.ftPosted = 1 AND ft.ftVoid = 0 AND ft.ftAdCompany = ?3");
			query.setParameter(1, DT);
			query.setParameter(2, BA_CODE);
			query.setParameter(3, FT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmFundTransferHome.findUnreconciledPostedFtAccountToByDateAndBaCode(java.com.util.Date DT, java.lang.Integer BA_CODE, java.lang.Integer FT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedFtAccountToByDateAndBaCode(java.util.Date DT, java.lang.Integer BA_CODE,
			java.lang.Integer FT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ft) FROM CmFundTransfer ft WHERE ft.ftDate <= ?1 AND ft.ftAdBaAccountTo = ?2 AND ft.ftPosted = 1 AND ft.ftVoid = 0 AND ft.ftAdCompany = ?3");
			query.setParameter(1, DT);
			query.setParameter(2, BA_CODE);
			query.setParameter(3, FT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmFundTransferHome.findPostedFtAccountToByDateAndBaCode(java.com.util.Date DT, java.lang.Integer BA_CODE, java.lang.Integer FT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnreconciledPostedFtAccountFromByDateAndBaCodeAndBrCode(java.util.Date DT,
			java.lang.Integer BA_CODE, java.lang.Integer FT_AD_BRNCH, java.lang.Integer FT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ft) FROM CmFundTransfer ft WHERE ft.ftDate <= ?1 AND ft.ftAdBaAccountFrom = ?2 AND ft.ftAccountFromReconciled = 0 AND ft.ftPosted = 1 AND ft.ftVoid = 0 AND ft.ftAdBranch = ?3 AND ft.ftAdCompany = ?4");
			query.setParameter(1, DT);
			query.setParameter(2, BA_CODE);
			query.setParameter(3, FT_AD_BRNCH);
			query.setParameter(4, FT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmFundTransferHome.findUnreconciledPostedFtAccountFromByDateAndBaCodeAndBrCode(java.com.util.Date DT, java.lang.Integer BA_CODE, java.lang.Integer FT_AD_BRNCH, java.lang.Integer FT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnreconciledPostedFtAccountFromByDateAndBaCode(java.util.Date DT,
			java.lang.Integer BA_CODE, java.lang.Integer FT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ft) FROM CmFundTransfer ft WHERE ft.ftDate <= ?1 AND ft.ftAdBaAccountFrom = ?2 AND ft.ftAccountFromReconciled = 0 AND ft.ftPosted = 1 AND ft.ftVoid = 0 AND ft.ftAdCompany = ?3");
			query.setParameter(1, DT);
			query.setParameter(2, BA_CODE);
			query.setParameter(3, FT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmFundTransferHome.findUnreconciledPostedFtAccountFromByDateAndBaCode(java.com.util.Date DT, java.lang.Integer BA_CODE, java.lang.Integer FT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedFtAccountFromByDateAndBaCode(java.util.Date DT, java.lang.Integer BA_CODE,
			java.lang.Integer FT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ft) FROM CmFundTransfer ft WHERE ft.ftDate <= ?1 AND ft.ftAdBaAccountFrom = ?2 AND ft.ftPosted = 1 AND ft.ftVoid = 0 AND ft.ftAdCompany = ?3");
			query.setParameter(1, DT);
			query.setParameter(2, BA_CODE);
			query.setParameter(3, FT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmFundTransferHome.findPostedFtAccountFromByDateAndBaCode(java.com.util.Date DT, java.lang.Integer BA_CODE, java.lang.Integer FT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedFtByFtDateRange(java.util.Date FT_DT_FRM, java.util.Date FT_DT_TO,
			java.lang.Integer FT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ft) FROM CmFundTransfer ft WHERE ft.ftPosted = 1 AND  ft.ftDate >= ?1 AND ft.ftDate <= ?2 AND ft.ftAdCompany = ?3");
			query.setParameter(1, FT_DT_FRM);
			query.setParameter(2, FT_DT_TO);
			query.setParameter(3, FT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmFundTransferHome.findPostedFtByFtDateRange(java.com.util.Date FT_DT_FRM, java.com.util.Date FT_DT_TO, java.lang.Integer FT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAdBankAccountFrom(java.lang.Integer BA_CODE, java.lang.Integer FT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ft) FROM CmFundTransfer ft WHERE ft.ftAdBaAccountFrom =?1 AND ft.ftAdCompany = ?2");
			query.setParameter(1, BA_CODE);
			query.setParameter(2, FT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmFundTransferHome.findByAdBankAccountFrom(java.lang.Integer BA_CODE, java.lang.Integer FT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAdBankAccountTo(java.lang.Integer BA_CODE, java.lang.Integer FT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ft) FROM CmFundTransfer ft WHERE ft.ftAdBaAccountTo =?1 AND ft.ftAdCompany = ?2");
			query.setParameter(1, BA_CODE);
			query.setParameter(2, FT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmFundTransferHome.findByAdBankAccountTo(java.lang.Integer BA_CODE, java.lang.Integer FT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftFtAll(java.lang.Integer FT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ft) FROM CmFundTransfer ft WHERE ft.ftApprovalStatus IS NULL AND ft.ftAdCompany = ?1");
			query.setParameter(1, FT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmFundTransferHome.findDraftFtAll(java.lang.Integer FT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftFtByBrCode(java.lang.Integer FT_AD_BRNCH, java.lang.Integer FT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ft) FROM CmFundTransfer ft WHERE ft.ftApprovalStatus IS NULL AND ft.ftAdBranch = ?1 AND ft.ftAdCompany = ?2");
			query.setParameter(1, FT_AD_BRNCH);
			query.setParameter(2, FT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmFundTransferHome.findDraftFtByBrCode(java.lang.Integer FT_AD_BRNCH, java.lang.Integer FT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedCmFtByCmFtDateRange(java.util.Date FT_DT_FRM, java.util.Date FT_DT_TO,
			java.lang.Integer FT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ft) FROM CmFundTransfer ft  WHERE ft.ftPosted = 0 AND ft.ftVoid = 0 AND ft.ftDate >= ?1 AND ft.ftDate <= ?2 AND ft.ftAdCompany = ?3");
			query.setParameter(1, FT_DT_FRM);
			query.setParameter(2, FT_DT_TO);
			query.setParameter(3, FT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmFundTransferHome.findUnpostedCmFtByCmFtDateRange(java.com.util.Date FT_DT_FRM, java.com.util.Date FT_DT_TO, java.lang.Integer FT_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalCmFundTransfer findByFtDocumentNumberAndBrCode(java.lang.String FT_DCMNT_NMBR,
			java.lang.Integer FT_AD_BRNCH, java.lang.Integer FT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ft) FROM CmFundTransfer ft  WHERE ft.ftDocumentNumber = ?1 AND ft.ftAdBranch = ?2 AND ft.ftAdCompany = ?3");
			query.setParameter(1, FT_DCMNT_NMBR);
			query.setParameter(2, FT_AD_BRNCH);
			query.setParameter(3, FT_AD_CMPNY);
            return (LocalCmFundTransfer) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.cm.LocalCmFundTransferHome.findByFtDocumentNumberAndBrCode(java.lang.String FT_DCMNT_NMBR, java.lang.Integer FT_AD_BRNCH, java.lang.Integer FT_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmFundTransferHome.findByFtDocumentNumberAndBrCode(java.lang.String FT_DCMNT_NMBR, java.lang.Integer FT_AD_BRNCH, java.lang.Integer FT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedFtAccountToByBaCodeAndDateRangeAndBrCode(java.lang.Integer BA_CODE,
			java.util.Date FT_DT_FRM, java.util.Date FT_DT_TO, java.lang.Integer FT_AD_BRNCH,
			java.lang.Integer FT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ft) FROM CmFundTransfer ft WHERE ft.ftPosted = 1 AND ft.ftVoid = 0 AND ft.ftAdBaAccountTo = ?1 AND ft.ftDate >= ?2 AND ft.ftDate <= ?3 AND ft.ftAdBranch = ?4 AND ft.ftAdCompany = ?5");
			query.setParameter(1, BA_CODE);
			query.setParameter(2, FT_DT_FRM);
			query.setParameter(3, FT_DT_TO);
			query.setParameter(4, FT_AD_BRNCH);
			query.setParameter(5, FT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmFundTransferHome.findPostedFtAccountToByBaCodeAndDateRangeAndBrCode(java.lang.Integer BA_CODE, java.com.util.Date FT_DT_FRM, java.com.util.Date FT_DT_TO, java.lang.Integer FT_AD_BRNCH, java.lang.Integer FT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedFtAccountFromByBaCodeAndDateRangeAndBrCode(java.lang.Integer BA_CODE,
			java.util.Date FT_DT_FRM, java.util.Date FT_DT_TO, java.lang.Integer FT_AD_BRNCH,
			java.lang.Integer FT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ft) FROM CmFundTransfer ft WHERE ft.ftPosted = 1 AND ft.ftVoid = 0 AND ft.ftAdBaAccountFrom = ?1 AND ft.ftDate >= ?2 AND ft.ftDate <= ?3 AND ft.ftAdBranch = ?4 AND ft.ftAdCompany = ?5");
			query.setParameter(1, BA_CODE);
			query.setParameter(2, FT_DT_FRM);
			query.setParameter(3, FT_DT_TO);
			query.setParameter(4, FT_AD_BRNCH);
			query.setParameter(5, FT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmFundTransferHome.findPostedFtAccountFromByBaCodeAndDateRangeAndBrCode(java.lang.Integer BA_CODE, java.com.util.Date FT_DT_FRM, java.com.util.Date FT_DT_TO, java.lang.Integer FT_AD_BRNCH, java.lang.Integer FT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findReconciledPostedFtAccountToByBaCodeAndDateRangeAndBrCode(java.lang.Integer BA_CODE,
			java.util.Date FT_DT_FRM, java.util.Date FT_DT_TO, java.lang.Integer FT_AD_BRNCH,
			java.lang.Integer FT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ft) FROM CmFundTransfer ft WHERE ft.ftPosted = 1 AND ft.ftVoid = 0 AND ft.ftAccountToReconciled = 1 AND ft.ftAdBaAccountTo = ?1 AND ft.ftDate >= ?2 AND ft.ftDate <= ?3 AND ft.ftAdBranch = ?4 AND ft.ftAdCompany = ?5");
			query.setParameter(1, BA_CODE);
			query.setParameter(2, FT_DT_FRM);
			query.setParameter(3, FT_DT_TO);
			query.setParameter(4, FT_AD_BRNCH);
			query.setParameter(5, FT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmFundTransferHome.findReconciledPostedFtAccountToByBaCodeAndDateRangeAndBrCode(java.lang.Integer BA_CODE, java.com.util.Date FT_DT_FRM, java.com.util.Date FT_DT_TO, java.lang.Integer FT_AD_BRNCH, java.lang.Integer FT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findReconciledPostedFtAccountFromByBaCodeAndDateRangeAndBrCode(
			java.lang.Integer BA_CODE, java.util.Date FT_DT_FRM, java.util.Date FT_DT_TO, java.lang.Integer FT_AD_BRNCH,
			java.lang.Integer FT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ft) FROM CmFundTransfer ft WHERE ft.ftPosted = 1 AND ft.ftVoid = 0 AND ft.ftAccountFromReconciled = 1 AND ft.ftAdBaAccountFrom = ?1 AND ft.ftDate >= ?2 AND ft.ftDate <= ?3 AND ft.ftAdBranch = ?4 AND ft.ftAdCompany = ?5");
			query.setParameter(1, BA_CODE);
			query.setParameter(2, FT_DT_FRM);
			query.setParameter(3, FT_DT_TO);
			query.setParameter(4, FT_AD_BRNCH);
			query.setParameter(5, FT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmFundTransferHome.findReconciledPostedFtAccountFromByBaCodeAndDateRangeAndBrCode(java.lang.Integer BA_CODE, java.com.util.Date FT_DT_FRM, java.com.util.Date FT_DT_TO, java.lang.Integer FT_AD_BRNCH, java.lang.Integer FT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getFtByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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

	public LocalCmFundTransfer create(Integer FT_CODE, Date FT_DT, String FT_DCMNT_NMBR,
                                      String FT_RFRNC_NMBR, String FT_MM, Integer FT_AD_BA_ACCNT_FRM, Integer FT_AD_BA_ACCNT_TO, double FT_AMNT,
                                      Date FT_CNVRSN_DT, double FT_CNVRSN_RT_FRM, byte FT_VOID, byte FT_ACCNT_FRM_RCNCLD, byte FT_ACCNT_TO_RCNCLD,
                                      String FT_APPRVL_STATUS, byte FT_PSTD, String FT_CRTD_BY, Date FT_DT_CRTD, String FT_LST_MDFD_BY,
                                      Date FT_DT_LST_MDFD, String FT_APPRVD_RJCTD_BY, Date FT_DT_APPRVD_RJCTD, String FT_PSTD_BY, Date FT_DT_PSTD,
                                      String FT_RSN_FR_RJCTN, String FT_TYP, double FT_CNVRSN_RT_TO, Date FT_ACCNT_FRM_DT_RCNCLD,
                                      Date FT_ACCNT_TO_DT_RCNCLD, Integer FT_AD_BRNCH, Integer FT_AD_CMPNY) throws CreateException {
		try {

			LocalCmFundTransfer entity = new LocalCmFundTransfer();

			Debug.print("CmFundTransferBean create");

			entity.setFtCode(FT_CODE);
			entity.setFtDate(FT_DT);
			entity.setFtDocumentNumber(FT_DCMNT_NMBR);
			entity.setFtReferenceNumber(FT_RFRNC_NMBR);
			entity.setFtMemo(FT_MM);
			entity.setFtAdBaAccountFrom(FT_AD_BA_ACCNT_FRM);
			entity.setFtAdBaAccountTo(FT_AD_BA_ACCNT_TO);
			entity.setFtAmount(FT_AMNT);
			entity.setFtConversionDate(FT_CNVRSN_DT);
			entity.setFtConversionRateFrom(FT_CNVRSN_RT_FRM);
			entity.setFtVoid(FT_VOID);
			entity.setFtAccountFromReconciled(FT_ACCNT_FRM_RCNCLD);
			entity.setFtAccountToReconciled(FT_ACCNT_TO_RCNCLD);
			entity.setFtApprovalStatus(FT_APPRVL_STATUS);
			entity.setFtPosted(FT_PSTD);
			entity.setFtCreatedBy(FT_CRTD_BY);
			entity.setFtDateCreated(FT_DT_CRTD);
			entity.setFtLastModifiedBy(FT_LST_MDFD_BY);
			entity.setFtDateLastModified(FT_DT_LST_MDFD);
			entity.setFtApprovedRejectedBy(FT_APPRVD_RJCTD_BY);
			entity.setFtDateApprovedRejected(FT_DT_APPRVD_RJCTD);
			entity.setFtPostedBy(FT_PSTD_BY);
			entity.setFtDatePosted(FT_DT_PSTD);
			entity.setFtReasonForRejection(FT_RSN_FR_RJCTN);
			entity.setFtType(FT_TYP);
			entity.setFtConversionRateTo(FT_CNVRSN_RT_TO);
			entity.setFtAccountFromDateReconciled(FT_ACCNT_FRM_DT_RCNCLD);
			entity.setFtAccountToDateReconciled(FT_ACCNT_TO_DT_RCNCLD);
			entity.setFtAdBranch(FT_AD_BRNCH);
			entity.setFtAdCompany(FT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalCmFundTransfer create(Date FT_DT, String FT_DCMNT_NMBR, String FT_RFRNC_NMBR, String FT_MM,
                                      Integer FT_AD_BA_ACCNT_FRM, Integer FT_AD_BA_ACCNT_TO, double FT_AMNT, Date FT_CNVRSN_DT,
                                      double FT_CNVRSN_RT_FRM, byte FT_VOID, byte FT_ACCNT_FRM_RCNCLD, byte FT_ACCNT_TO_RCNCLD,
                                      String FT_APPRVL_STATUS, byte FT_PSTD, String FT_CRTD_BY, Date FT_DT_CRTD, String FT_LST_MDFD_BY,
                                      Date FT_DT_LST_MDFD, String FT_APPRVD_RJCTD_BY, Date FT_DT_APPRVD_RJCTD, String FT_PSTD_BY, Date FT_DT_PSTD,
                                      String FT_RSN_FR_RJCTN, String FT_TYP, double FT_CNVRSN_RT_TO, Date FT_ACCNT_FRM_DT_RCNCLD,
                                      Date FT_ACCNT_TO_DT_RCNCLD, Integer FT_AD_BRNCH, Integer FT_AD_CMPNY) throws CreateException {
		try {

			LocalCmFundTransfer entity = new LocalCmFundTransfer();

			Debug.print("CmFundTransferBean create");

			entity.setFtDate(FT_DT);
			entity.setFtDocumentNumber(FT_DCMNT_NMBR);
			entity.setFtReferenceNumber(FT_RFRNC_NMBR);
			entity.setFtMemo(FT_MM);
			entity.setFtAdBaAccountFrom(FT_AD_BA_ACCNT_FRM);
			entity.setFtAdBaAccountTo(FT_AD_BA_ACCNT_TO);
			entity.setFtAmount(FT_AMNT);
			entity.setFtConversionDate(FT_CNVRSN_DT);
			entity.setFtConversionRateFrom(FT_CNVRSN_RT_FRM);
			entity.setFtVoid(FT_VOID);
			entity.setFtAccountFromReconciled(FT_ACCNT_FRM_RCNCLD);
			entity.setFtAccountToReconciled(FT_ACCNT_TO_RCNCLD);
			entity.setFtApprovalStatus(FT_APPRVL_STATUS);
			entity.setFtPosted(FT_PSTD);
			entity.setFtCreatedBy(FT_CRTD_BY);
			entity.setFtDateCreated(FT_DT_CRTD);
			entity.setFtLastModifiedBy(FT_LST_MDFD_BY);
			entity.setFtDateLastModified(FT_DT_LST_MDFD);
			entity.setFtApprovedRejectedBy(FT_APPRVD_RJCTD_BY);
			entity.setFtDateApprovedRejected(FT_DT_APPRVD_RJCTD);
			entity.setFtPostedBy(FT_PSTD_BY);
			entity.setFtDatePosted(FT_DT_PSTD);
			entity.setFtReasonForRejection(FT_RSN_FR_RJCTN);
			entity.setFtType(FT_TYP);
			entity.setFtConversionRateTo(FT_CNVRSN_RT_TO);
			entity.setFtAccountFromDateReconciled(FT_ACCNT_FRM_DT_RCNCLD);
			entity.setFtAccountToDateReconciled(FT_ACCNT_TO_DT_RCNCLD);
			entity.setFtAdBranch(FT_AD_BRNCH);
			entity.setFtAdCompany(FT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}