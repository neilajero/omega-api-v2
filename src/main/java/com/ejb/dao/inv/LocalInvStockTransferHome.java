package com.ejb.dao.inv;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvStockTransfer;
import com.util.Debug;
import com.util.EJBCommon;

@SuppressWarnings("ALL")
@Stateless
public class LocalInvStockTransferHome {

	public static final String JNDI_NAME = "LocalInvStockTransferHome!com.ejb.inv.LocalInvStockTransferHome";

	@EJB
	public PersistenceBeanClass em;

	private java.lang.String ST_DCMNT_NMBR = null;
	private java.lang.String ST_RFRNC_NMBR = null;
	private java.lang.String ST_DESC = null;
	private java.util.Date ST_DT = null;
	private java.lang.String ST_APPRVL_STATUS = null;
	private byte ST_PSTD = EJBCommon.FALSE;
	private java.lang.String ST_CRTD_BY = null;
	private java.util.Date ST_DT_CRTD = null;
	private java.lang.String ST_LST_MDFD_BY = null;
	private java.util.Date ST_DT_LST_MDFD = null;
	private final java.lang.String ST_APPRVD_RJCTD_BY = null;
	private final java.util.Date ST_DT_APPRVD_RJCTD = null;
	private java.lang.String ST_PSTD_BY = null;
	private java.util.Date ST_DT_PSTD = null;
	private final java.lang.String ST_RSN_FR_RJCTN = null;
	private Integer ST_AD_BRNCH = null;
	private Integer ST_AD_CMPNY = null;

	public LocalInvStockTransferHome() {
	}

	// FINDER METHODS

	public LocalInvStockTransfer findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvStockTransfer entity = (LocalInvStockTransfer) em
					.find(new LocalInvStockTransfer(), pk);
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

	public LocalInvStockTransfer findByStDocumentNumberAndBrCode(java.lang.String ST_DCMNT_NMBR,
			java.lang.Integer ST_AD_BRNCH, java.lang.Integer ST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(st) FROM InvStockTransfer st  WHERE st.stDocumentNumber = ?1 AND st.stAdBranch = ?2 AND st.stAdCompany = ?3");
			query.setParameter(1, ST_DCMNT_NMBR);
			query.setParameter(2, ST_AD_BRNCH);
			query.setParameter(3, ST_AD_CMPNY);
            return (LocalInvStockTransfer) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvStockTransferHome.findByStDocumentNumberAndBrCode(java.lang.String ST_DCMNT_NMBR, java.lang.Integer ST_AD_BRNCH, java.lang.Integer ST_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvStockTransferHome.findByStDocumentNumberAndBrCode(java.lang.String ST_DCMNT_NMBR, java.lang.Integer ST_AD_BRNCH, java.lang.Integer ST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedStByStDateRange(java.util.Date ST_DT_FRM, java.util.Date ST_DT_TO,
			java.lang.Integer ST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(st) FROM InvStockTransfer st  WHERE st.stPosted = 0 AND st.stDate >= ?1 AND st.stDate <= ?2 AND st.stAdCompany = ?3");
			query.setParameter(1, ST_DT_FRM);
			query.setParameter(2, ST_DT_TO);
			query.setParameter(3, ST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvStockTransferHome.findUnpostedStByStDateRange(java.com.util.Date ST_DT_FRM, java.com.util.Date ST_DT_TO, java.lang.Integer ST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedSt(java.lang.Integer ST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(st) FROM InvStockTransfer st  WHERE st.stPosted = 0 AND st.stAdCompany = ?1");
			query.setParameter(1, ST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvStockTransferHome.findUnpostedSt(java.lang.Integer ST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getStByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
			java.lang.Integer LIMIT, java.lang.Integer OFFSET) throws FinderException {

		try {
			Query query = em.createQuery(jbossQl);
			int cnt = 1;
			for (Object data : args) {
				query.setParameter(cnt, data);
				cnt++;
			}
			if (LIMIT > 0) {
				query.setMaxResults(LIMIT);
			}
            query.setFirstResult(OFFSET);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS
	public LocalInvStockTransfer buildStockTransfer() throws CreateException {
		try {

			LocalInvStockTransfer entity = new LocalInvStockTransfer();

			Debug.print("invStockTransferBean buildStockTransfer");

			entity.setStDocumentNumber(ST_DCMNT_NMBR);
			entity.setStReferenceNumber(ST_RFRNC_NMBR);
			entity.setStDescription(ST_DESC);
			entity.setStDate(ST_DT);
			entity.setStApprovalStatus(ST_APPRVL_STATUS);
			entity.setStPosted(ST_PSTD);
			entity.setStCreatedBy(ST_CRTD_BY);
			entity.setStDateCreated(ST_DT_CRTD);
			entity.setStLastModifiedBy(ST_LST_MDFD_BY);
			entity.setStDateLastModified(ST_DT_LST_MDFD);
			entity.setStApprovedRejectedBy(ST_APPRVD_RJCTD_BY);
			entity.setStDateApprovedRejected(ST_DT_APPRVD_RJCTD);
			entity.setStPostedBy(ST_PSTD_BY);
			entity.setStDatePosted(ST_DT_PSTD);
			entity.setStReasonForRejection(ST_RSN_FR_RJCTN);
			entity.setStAdBranch(ST_AD_BRNCH);
			entity.setStAdCompany(ST_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvStockTransferHome StDocumentNumber(String ST_DCMNT_NMBR) {
		this.ST_DCMNT_NMBR = ST_DCMNT_NMBR;
		return this;
	}
	
	public LocalInvStockTransferHome StReferenceNumber(String ST_RFRNC_NMBR) {
		this.ST_RFRNC_NMBR = ST_RFRNC_NMBR;
		return this;
	}
	
	public LocalInvStockTransferHome StDescription(String ST_DESC) {
		this.ST_DESC = ST_DESC;
		return this;
	}
	
	public LocalInvStockTransferHome StDate(java.util.Date ST_DT) {
		this.ST_DT = ST_DT;
		return this;
	}
	
	public LocalInvStockTransferHome StApprovalStatus(String ST_APPRVL_STATUS) {
		this.ST_APPRVL_STATUS = ST_APPRVL_STATUS;
		return this;
	}
	
	public LocalInvStockTransferHome StPosted(byte ST_PSTD) {
		this.ST_PSTD = ST_PSTD;
		return this;
	}
	
	public LocalInvStockTransferHome StCreatedBy(String ST_CRTD_BY) {
		this.ST_CRTD_BY = ST_CRTD_BY;
		return this;
	}
	
	public LocalInvStockTransferHome StDateCreated(java.util.Date ST_DT_CRTD) {
		this.ST_DT_CRTD = ST_DT_CRTD;
		return this;
	}
	
	public LocalInvStockTransferHome StLastModifiedBy(String ST_LST_MDFD_BY) {
		this.ST_LST_MDFD_BY = ST_LST_MDFD_BY;
		return this;
	}
	
	public LocalInvStockTransferHome StDateLastModified(java.util.Date ST_DT_LST_MDFD) {
		this.ST_DT_LST_MDFD = ST_DT_LST_MDFD;
		return this;
	}
	
	public LocalInvStockTransferHome StAdBranch(Integer ST_AD_BRNCH) {
		this.ST_AD_BRNCH = ST_AD_BRNCH;
		return this;
	}
	
	public LocalInvStockTransferHome StPostedBy(String ST_PSTD_BY) {
		this.ST_PSTD_BY = ST_PSTD_BY;
		return this;
	}
	
	public LocalInvStockTransferHome StDatePosted(java.util.Date ST_DT_PSTD) {
		this.ST_DT_PSTD = ST_DT_PSTD;
		return this;
	}
	
	public LocalInvStockTransferHome StAdCompany(Integer ST_AD_CMPNY) {
		this.ST_AD_CMPNY = ST_AD_CMPNY;
		return this;
	}

	public LocalInvStockTransfer create(java.lang.Integer INV_ST_CODE, java.lang.String ST_DCMNT_NMBR,
                                        java.lang.String ST_RFRNC_NMBR, java.lang.String ST_DESC, java.util.Date ST_DT,
                                        java.lang.String ST_APPRVL_STATUS, byte ST_PSTD, java.lang.String ST_CRTD_BY, java.util.Date ST_DT_CRTD,
                                        java.lang.String ST_LST_MDFD_BY, java.util.Date ST_DT_LST_MDFD, java.lang.String ST_APPRVD_RJCTD_BY,
                                        java.util.Date ST_DT_APPRVD_RJCTD, java.lang.String ST_PSTD_BY, java.util.Date ST_DT_PSTD,
                                        java.lang.String ST_RSN_FR_RJCTN, Integer ST_AD_BRNCH, Integer ST_AD_CMPNY) throws CreateException {
		try {

			LocalInvStockTransfer entity = new LocalInvStockTransfer();

			Debug.print("invStockTransferBean create");

			entity.setStCode(INV_ST_CODE);
			entity.setStDocumentNumber(ST_DCMNT_NMBR);
			entity.setStReferenceNumber(ST_RFRNC_NMBR);
			entity.setStDescription(ST_DESC);
			entity.setStDate(ST_DT);
			entity.setStApprovalStatus(ST_APPRVL_STATUS);
			entity.setStPosted(ST_PSTD);
			entity.setStCreatedBy(ST_CRTD_BY);
			entity.setStDateCreated(ST_DT_CRTD);
			entity.setStLastModifiedBy(ST_LST_MDFD_BY);
			entity.setStDateLastModified(ST_DT_LST_MDFD);
			entity.setStApprovedRejectedBy(ST_APPRVD_RJCTD_BY);
			entity.setStDateApprovedRejected(ST_DT_APPRVD_RJCTD);
			entity.setStPostedBy(ST_PSTD_BY);
			entity.setStDatePosted(ST_DT_PSTD);
			entity.setStReasonForRejection(ST_RSN_FR_RJCTN);
			entity.setStAdBranch(ST_AD_BRNCH);
			entity.setStAdCompany(ST_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvStockTransfer create(java.lang.String ST_DCMNT_NMBR, java.lang.String ST_RFRNC_NMBR,
                                        java.lang.String ST_DESC, java.util.Date ST_DT, java.lang.String ST_APPRVL_STATUS, byte ST_PSTD,
                                        java.lang.String ST_CRTD_BY, java.util.Date ST_DT_CRTD, java.lang.String ST_LST_MDFD_BY,
                                        java.util.Date ST_DT_LST_MDFD, java.lang.String ST_APPRVD_RJCTD_BY, java.util.Date ST_DT_APPRVD_RJCTD,
                                        java.lang.String ST_PSTD_BY, java.util.Date ST_DT_PSTD, java.lang.String ST_RSN_FR_RJCTN,
                                        Integer ST_AD_BRNCH, Integer ST_AD_CMPNY) throws CreateException {
		try {

			LocalInvStockTransfer entity = new LocalInvStockTransfer();

			Debug.print("invStockTransferBean create");

			entity.setStDocumentNumber(ST_DCMNT_NMBR);
			entity.setStReferenceNumber(ST_RFRNC_NMBR);
			entity.setStDescription(ST_DESC);
			entity.setStDate(ST_DT);
			entity.setStApprovalStatus(ST_APPRVL_STATUS);
			entity.setStPosted(ST_PSTD);
			entity.setStCreatedBy(ST_CRTD_BY);
			entity.setStDateCreated(ST_DT_CRTD);
			entity.setStLastModifiedBy(ST_LST_MDFD_BY);
			entity.setStDateLastModified(ST_DT_LST_MDFD);
			entity.setStApprovedRejectedBy(ST_APPRVD_RJCTD_BY);
			entity.setStDateApprovedRejected(ST_DT_APPRVD_RJCTD);
			entity.setStPostedBy(ST_PSTD_BY);
			entity.setStDatePosted(ST_DT_PSTD);
			entity.setStReasonForRejection(ST_RSN_FR_RJCTN);
			entity.setStAdBranch(ST_AD_BRNCH);
			entity.setStAdCompany(ST_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}