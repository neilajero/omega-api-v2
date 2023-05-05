package com.ejb.dao.ap;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApPurchaseRequisition;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalApPurchaseRequisitionHome {

	public static final String JNDI_NAME = "LocalApPurchaseRequisitionHome!com.ejb.ap.LocalApPurchaseRequisitionHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalApPurchaseRequisitionHome() {
	}

	// FINDER METHODS

	public LocalApPurchaseRequisition findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalApPurchaseRequisition entity = (LocalApPurchaseRequisition) em
					.find(new LocalApPurchaseRequisition(), pk);
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

	public LocalApPurchaseRequisition findByPrNumberAndBrCode(java.lang.String PR_NMBR, java.lang.Integer PR_AD_BRNCH,
			java.lang.Integer PR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pr) FROM ApPurchaseRequisition pr  WHERE pr.prNumber = ?1 AND pr.prAdBranch = ?2 AND pr.prAdCompany = ?3");
			query.setParameter(1, PR_NMBR);
			query.setParameter(2, PR_AD_BRNCH);
			query.setParameter(3, PR_AD_CMPNY);
            return (LocalApPurchaseRequisition) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApPurchaseRequisitionHome.findByPrNumberAndBrCode(java.lang.String PR_NMBR, java.lang.Integer PR_AD_BRNCH, java.lang.Integer PR_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApPurchaseRequisitionHome.findByPrNumberAndBrCode(java.lang.String PR_NMBR, java.lang.Integer PR_AD_BRNCH, java.lang.Integer PR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftPrByBrCode(java.lang.Integer PR_AD_BRNCH, java.lang.Integer PR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pr) FROM ApPurchaseRequisition pr WHERE pr.prApprovalStatus IS NULL AND pr.prReasonForRejection IS NULL AND pr.prVoid = 0 AND pr.prAdBranch = ?1 AND pr.prAdCompany = ?2");
			query.setParameter(1, PR_AD_BRNCH);
			query.setParameter(2, PR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApPurchaseRequisitionHome.findDraftPrByBrCode(java.lang.Integer PR_AD_BRNCH, java.lang.Integer PR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedAndCanvassUnPostedPr(java.lang.Integer PR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pr) FROM ApPurchaseRequisition pr WHERE pr.prPosted = 1 AND pr.prCanvassApprovalStatus IS NULL AND pr.prCanvassReasonForRejection IS NULL AND pr.prCanvassPosted = 0 AND pr.prVoid = 0 AND pr.prAdCompany = ?1");
			query.setParameter(1, PR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApPurchaseRequisitionHome.findPostedAndCanvassUnPostedPr(java.lang.Integer PR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findRejectedPrByBrCodeAndPrCreatedBy(java.lang.Integer PR_AD_BRNCH,
			java.lang.String PR_CRTD_BY, java.lang.Integer PR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pr) FROM ApPurchaseRequisition pr WHERE pr.prApprovalStatus IS NULL AND pr.prReasonForRejection IS NOT NULL AND pr.prAdBranch = ?1 AND pr.prCreatedBy=?2 AND pr.prAdCompany = ?3");
			query.setParameter(1, PR_AD_BRNCH);
			query.setParameter(2, PR_CRTD_BY);
			query.setParameter(3, PR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApPurchaseRequisitionHome.findRejectedPrByBrCodeAndPrCreatedBy(java.lang.Integer PR_AD_BRNCH, java.lang.String PR_CRTD_BY, java.lang.Integer PR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findRejectedCanvassByBrCodeAndPrLastModifiedBy(java.lang.String PR_LST_MDFD_BY,
			java.lang.Integer PR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pr) FROM ApPurchaseRequisition pr WHERE pr.prCanvassApprovalStatus IS NULL AND pr.prCanvassReasonForRejection IS NOT NULL AND pr.prLastModifiedBy=?1 AND pr.prAdCompany = ?2");
			query.setParameter(1, PR_LST_MDFD_BY);
			query.setParameter(2, PR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApPurchaseRequisitionHome.findRejectedCanvassByBrCodeAndPrLastModifiedBy(java.lang.String PR_LST_MDFD_BY, java.lang.Integer PR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnPostedGeneratedPr(java.lang.Integer PR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pr) FROM ApPurchaseRequisition pr WHERE pr.prPosted = 1 AND pr.prCanvassPosted = 1 AND pr.prGenerated = 0 AND pr.prVoid = 0 AND pr.prAdCompany = ?1");
			query.setParameter(1, PR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApPurchaseRequisitionHome.findUnPostedGeneratedPr(java.lang.Integer PR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPrToGenerateByAdUsrCodeAndDate(java.lang.Integer USR_CODE, java.util.Date DT,
			java.lang.Integer PR_AD_BRNCH, java.lang.Integer PR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pr) FROM ApPurchaseRequisition pr WHERE pr.prPosted = 1 AND (pr.prAdUserName1=?1 ) AND pr.prNextRunDate <= ?2 AND pr.prAdBranch = ?3 AND pr.prAdCompany = ?4");
			query.setParameter(1, USR_CODE);
			query.setParameter(2, DT);
			query.setParameter(3, PR_AD_BRNCH);
			query.setParameter(4, PR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApPurchaseRequisitionHome.findPrToGenerateByAdUsrCodeAndDate(java.lang.Integer USR_CODE, java.com.util.Date DT, java.lang.Integer PR_AD_BRNCH, java.lang.Integer PR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByUserCode1(java.lang.Integer USR_CODE, java.lang.Integer PR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pr) FROM ApPurchaseRequisition pr WHERE pr.prAdUserName1 = ?1 AND pr.prAdCompany = ?2");
			query.setParameter(1, USR_CODE);
			query.setParameter(2, PR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApPurchaseRequisitionHome.findByUserCode1(java.lang.Integer USR_CODE, java.lang.Integer PR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getPrByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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

	public LocalApPurchaseRequisition create(Integer PR_CODE, String PR_DESC, String PR_NMBR, Date PR_DT,
                                             Date PR_DLVRY_PRD, String PR_RFRNC_NMBR, Date PR_CNVRSN_DT, double PR_CNVRSN_RT, String PR_APPRVL_STATUS,
                                             byte PR_PSTD, byte PR_GNRTD, String PR_CNVSS_APPRVL_STATUS, byte PR_CNVSS_PSTD, byte PR_VD,
                                             String PR_RSN_FR_RJCTN, String PR_CNVSS_RSN_FR_RJCTN, String PR_CRTD_BY, Date PR_DT_CRTD,
                                             String PR_LST_MDFD_BY, Date PR_DT_LST_MDFD, String PR_APPRVD_RJCTD_BY, Date PR_DT_APPRVD_RJCTD,
                                             String PR_PSTD_BY, Date PR_DT_PSTD, String PR_CNVSS_APPRVD_RJCTD_BY, Date PR_CNVSS_DT_APPRVD_RJCTD,
                                             String PR_CNVSS_PSTD_BY, Date PR_CNVSS_DT_PSTD, String PR_TG_STATUS, String PR_TYPE,
                                             java.lang.Integer PR_AD_USR_NM1, String PR_SCHDL, Date PR_NXT_RN_DT, Date PR_LST_RN_DT, Integer PR_AD_BRNCH,
                                             Integer PR_AD_CMPNY) throws CreateException {
		try {

			LocalApPurchaseRequisition entity = new LocalApPurchaseRequisition();

			Debug.print("ApPurchaseRequisitionBean create");
			entity.setPrCode(PR_CODE);
			entity.setPrDescription(PR_DESC);
			entity.setPrNumber(PR_NMBR);
			entity.setPrDate(PR_DT);
			entity.setPrDeliveryPeriod(PR_DLVRY_PRD);
			entity.setPrReferenceNumber(PR_RFRNC_NMBR);
			entity.setPrConversionDate(PR_CNVRSN_DT);
			entity.setPrConversionRate(PR_CNVRSN_RT);
			entity.setPrApprovalStatus(PR_APPRVL_STATUS);
			entity.setPrPosted(PR_PSTD);
			entity.setPrGenerated(PR_GNRTD);
			entity.setPrCanvassApprovalStatus(PR_CNVSS_APPRVL_STATUS);
			entity.setPrCanvassPosted(PR_CNVSS_PSTD);
			entity.setPrVoid(PR_VD);
			entity.setPrReasonForRejection(PR_RSN_FR_RJCTN);
			entity.setPrCanvassReasonForRejection(PR_CNVSS_RSN_FR_RJCTN);
			entity.setPrCreatedBy(PR_CRTD_BY);
			entity.setPrDateCreated(PR_DT_CRTD);
			entity.setPrLastModifiedBy(PR_LST_MDFD_BY);
			entity.setPrDateLastModified(PR_DT_LST_MDFD);
			entity.setPrApprovedRejectedBy(PR_APPRVD_RJCTD_BY);
			entity.setPrDateApprovedRejected(PR_DT_APPRVD_RJCTD);
			entity.setPrPostedBy(PR_PSTD_BY);
			entity.setPrDatePosted(PR_DT_PSTD);
			entity.setPrCanvassApprovedRejectedBy(PR_CNVSS_APPRVD_RJCTD_BY);
			entity.setPrCanvassDateApprovedRejected(PR_CNVSS_DT_APPRVD_RJCTD);
			entity.setPrCanvassPostedBy(PR_CNVSS_PSTD_BY);
			entity.setPrCanvassDatePosted(PR_CNVSS_DT_PSTD);
			entity.setPrTagStatus(PR_TG_STATUS);
			entity.setPrType(PR_TYPE);
			entity.setPrAdUserName1(PR_AD_USR_NM1);
			entity.setPrSchedule(PR_SCHDL);
			entity.setPrNextRunDate(PR_NXT_RN_DT);
			entity.setPrLastRunDate(PR_LST_RN_DT);
			entity.setPrAdBranch(PR_AD_BRNCH);
			entity.setPrAdCompany(PR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalApPurchaseRequisition create(String PR_DESC, String PR_NMBR, Date PR_DT, Date PR_DLVRY_PRD,
                                             String PR_RFRNC_NMBR, Date PR_CNVRSN_DT, double PR_CNVRSN_RT, String PR_APPRVL_STATUS, byte PR_PSTD,
                                             byte PR_GNRTD, String PR_CNVSS_APPRVL_STATUS, byte PR_CNVSS_PSTD, byte PR_VD, String PR_RSN_FR_RJCTN,
                                             String PR_CNVSS_RSN_FR_RJCTN, String PR_CRTD_BY, Date PR_DT_CRTD, String PR_LST_MDFD_BY,
                                             Date PR_DT_LST_MDFD, String PR_APPRVD_RJCTD_BY, Date PR_DT_APPRVD_RJCTD, String PR_PSTD_BY, Date PR_DT_PSTD,
                                             String PR_CNVSS_APPRVD_RJCTD_BY, Date PR_CNVSS_DT_APPRVD_RJCTD, String PR_CNVSS_PSTD_BY,
                                             Date PR_CNVSS_DT_PSTD, String PR_TG_STATUS, String PR_TYPE, java.lang.Integer PR_AD_USR_NM1,
                                             String PR_SCHDL, Date PR_NXT_RN_DT, Date PR_LST_RN_DT, Integer PR_AD_BRNCH, Integer PR_AD_CMPNY)
			throws CreateException {
		try {

			LocalApPurchaseRequisition entity = new LocalApPurchaseRequisition();

			Debug.print("ApPurchaseRequisitionBean create");
			entity.setPrDescription(PR_DESC);
			entity.setPrNumber(PR_NMBR);
			entity.setPrDate(PR_DT);
			entity.setPrDeliveryPeriod(PR_DLVRY_PRD);
			entity.setPrReferenceNumber(PR_RFRNC_NMBR);
			entity.setPrConversionDate(PR_CNVRSN_DT);
			entity.setPrConversionRate(PR_CNVRSN_RT);
			entity.setPrApprovalStatus(PR_APPRVL_STATUS);
			entity.setPrPosted(PR_PSTD);
			entity.setPrGenerated(PR_GNRTD);
			entity.setPrCanvassApprovalStatus(PR_CNVSS_APPRVL_STATUS);
			entity.setPrCanvassPosted(PR_CNVSS_PSTD);
			entity.setPrVoid(PR_VD);
			entity.setPrReasonForRejection(PR_RSN_FR_RJCTN);
			entity.setPrCanvassReasonForRejection(PR_CNVSS_RSN_FR_RJCTN);
			entity.setPrCreatedBy(PR_CRTD_BY);
			entity.setPrDateCreated(PR_DT_CRTD);
			entity.setPrLastModifiedBy(PR_LST_MDFD_BY);
			entity.setPrDateLastModified(PR_DT_LST_MDFD);
			entity.setPrApprovedRejectedBy(PR_APPRVD_RJCTD_BY);
			entity.setPrDateApprovedRejected(PR_DT_APPRVD_RJCTD);
			entity.setPrPostedBy(PR_PSTD_BY);
			entity.setPrDatePosted(PR_DT_PSTD);
			entity.setPrCanvassApprovedRejectedBy(PR_CNVSS_APPRVD_RJCTD_BY);
			entity.setPrCanvassDateApprovedRejected(PR_CNVSS_DT_APPRVD_RJCTD);
			entity.setPrCanvassPostedBy(PR_CNVSS_PSTD_BY);
			entity.setPrCanvassDatePosted(PR_CNVSS_DT_PSTD);
			entity.setPrTagStatus(PR_TG_STATUS);
			entity.setPrType(PR_TYPE);
			entity.setPrAdUserName1(PR_AD_USR_NM1);
			entity.setPrSchedule(PR_SCHDL);
			entity.setPrNextRunDate(PR_NXT_RN_DT);
			entity.setPrLastRunDate(PR_LST_RN_DT);
			entity.setPrAdBranch(PR_AD_BRNCH);
			entity.setPrAdCompany(PR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}