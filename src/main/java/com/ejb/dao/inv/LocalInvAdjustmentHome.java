package com.ejb.dao.inv;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvAdjustment;
import com.util.Debug;
import com.util.EJBCommon;

@SuppressWarnings("ALL")
@Stateless
public class LocalInvAdjustmentHome {

	public static final String JNDI_NAME = "LocalInvAdjustmentHome!com.ejb.inv.LocalInvAdjustmentHome";

	@EJB
	public PersistenceBeanClass em;
	
	private java.lang.String ADJ_DCMNT_NMBR = null; 
	private java.lang.String ADJ_RFRNC_NMBR = null; 
	private String ADJ_DESC = null;  
	private java.util.Date ADJ_DT = null;  
	private java.lang.String ADJ_TYP = null;  
	private java.lang.String ADJ_APPRVL_STATUS = null; 
	private byte ADJ_PSTD = EJBCommon.FALSE;  
	private final byte ADJ_GNRTD = EJBCommon.FALSE;
	private java.lang.String ADJ_CRTD_BY = null;  
	private java.util.Date ADJ_DT_CRTD = null; 
	private final java.lang.String ADJ_LST_MDFD_BY = null;
	private final java.util.Date ADJ_DT_LST_MDFD = null;
	private final java.lang.String ADJ_APPRVD_RJCTD_BY = null;
	private final java.util.Date ADJ_DT_APPRVD_RJCTD = null;
	private final java.lang.String ADJ_PSTD_BY = null;
	private final java.util.Date ADJ_DT_PSTD = null;
	private final String ADJ_NT_BY = null;
	private final String ADJ_RSN_FR_RJCTN = null;
	byte ADJ_IS_CST_VRNC = EJBCommon.FALSE;  
	byte ADJ_VD = EJBCommon.FALSE;  
	private Integer ADJ_AD_BRNCH = null; 
	private Integer ADJ_AD_CMPNY = null; 

	public LocalInvAdjustmentHome() {
	}

	// FINDER METHODS

	public LocalInvAdjustment findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvAdjustment entity = (LocalInvAdjustment) em
					.find(new LocalInvAdjustment(), pk);
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

	public java.util.Collection findPostedAdjByAdjDateRange(java.util.Date ADJ_DT_FRM, java.util.Date ADJ_DT_TO,
			java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM InvAdjustment adj WHERE adj.adjPosted = 1 AND adj.adjDate >= ?1 AND adj.adjDate <= ?2 AND adj.adjAdCompany = ?3");
			query.setParameter(1, ADJ_DT_FRM);
			query.setParameter(2, ADJ_DT_TO);
			query.setParameter(3, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentHome.findPostedAdjByAdjDateRange(java.com.util.Date ADJ_DT_FRM, java.com.util.Date ADJ_DT_TO, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftAdjAll(java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM InvAdjustment adj WHERE adj.adjApprovalStatus IS NULL AND adj.adjAdCompany = ?1");
			query.setParameter(1, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentHome.findDraftAdjAll(java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftAdjByBrCode(java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM InvAdjustment adj WHERE adj.adjApprovalStatus IS NULL AND adj.adjAdBranch = ?1 AND adj.adjAdCompany = ?2");
			query.setParameter(1, ADJ_AD_BRNCH);
			query.setParameter(2, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentHome.findDraftAdjByBrCode(java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedInvAdjByInvAdjDateRange(java.util.Date ADJ_DT_FRM, java.util.Date ADJ_DT_TO,
			java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM InvAdjustment adj  WHERE adj.adjPosted = 0 AND adj.adjDate >= ?1 AND adj.adjDate <= ?2 AND adj.adjAdCompany = ?3");
			query.setParameter(1, ADJ_DT_FRM);
			query.setParameter(2, ADJ_DT_TO);
			query.setParameter(3, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentHome.findUnpostedInvAdjByInvAdjDateRange(java.com.util.Date ADJ_DT_FRM, java.com.util.Date ADJ_DT_TO, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedInvAdjByInvAdjDateRangeByBranch(java.util.Date ADJ_DT_FRM,
			java.util.Date ADJ_DT_TO, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM InvAdjustment adj  WHERE adj.adjPosted = 0 AND adj.adjDate >= ?1 AND adj.adjDate <= ?2 AND adj.adjAdBranch = ?3 AND adj.adjAdCompany = ?4");
			query.setParameter(1, ADJ_DT_FRM);
			query.setParameter(2, ADJ_DT_TO);
			query.setParameter(3, ADJ_AD_BRNCH);
			query.setParameter(4, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentHome.findUnpostedInvAdjByInvAdjDateRangeByBranch(java.com.util.Date ADJ_DT_FRM, java.com.util.Date ADJ_DT_TO, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalInvAdjustment findByAdjDocumentNumberAndBrCode(java.lang.String ADJ_DCMNT_NMBR,
			java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM InvAdjustment adj  WHERE adj.adjDocumentNumber = ?1 AND adj.adjAdBranch = ?2 AND adj.adjAdCompany = ?3");
			query.setParameter(1, ADJ_DCMNT_NMBR);
			query.setParameter(2, ADJ_AD_BRNCH);
			query.setParameter(3, ADJ_AD_CMPNY);
            return (LocalInvAdjustment) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvAdjustmentHome.findByAdjDocumentNumberAndBrCode(java.lang.String ADJ_DCMNT_NMBR, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentHome.findByAdjDocumentNumberAndBrCode(java.lang.String ADJ_DCMNT_NMBR, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalInvAdjustment findByAdjReferenceNumberAndAdjDateAndBrCode(java.lang.String ADJ_RFRNC_NMBR,
			java.util.Date ADJ_DT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM InvAdjustment adj WHERE adj.adjReferenceNumber = ?1 AND adj.adjDate = ?2 AND adj.adjAdBranch = ?3 AND adj.adjAdCompany = ?4");
			query.setParameter(1, ADJ_RFRNC_NMBR);
			query.setParameter(2, ADJ_DT);
			query.setParameter(3, AD_BRNCH);
			query.setParameter(4, AD_CMPNY);
            return (LocalInvAdjustment) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvAdjustmentHome.findByAdjReferenceNumberAndAdjDateAndBrCode(java.lang.String ADJ_RFRNC_NMBR, java.com.util.Date ADJ_DT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentHome.findByAdjReferenceNumberAndAdjDateAndBrCode(java.lang.String ADJ_RFRNC_NMBR, java.com.util.Date ADJ_DT, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAdjReferenceNumber(java.lang.String ADJ_DCMNT_NMBR, java.lang.Integer AD_BRNCH,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM InvAdjustment adj WHERE adj.adjDocumentNumber = ?1  AND adj.adjAdBranch = ?2 AND adj.adjAdCompany = ?3");
			query.setParameter(1, ADJ_DCMNT_NMBR);
			query.setParameter(2, AD_BRNCH);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentHome.findByAdjReferenceNumber(java.lang.String ADJ_DCMNT_NMBR, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedAdjByAdjCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE,
			java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM InvAdjustment adj WHERE adj.adjPosted=0 AND adj.glChartOfAccount.coaCode=?1 AND adj.adjAdBranch = ?2 AND adj.adjAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, ADJ_AD_BRNCH);
			query.setParameter(3, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentHome.findUnpostedAdjByAdjCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAdjRequestToGenerateByAdCompanyAndBrCode(java.lang.Integer ADJ_AD_BRNCH,
			java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM InvAdjustment adj WHERE adj.adjGenerated=0 AND adj.adjType='REQUEST' AND (adj.adjApprovalStatus='APPROVED' OR adj.adjApprovalStatus='N/A') AND adj.adjPosted=1 AND adj.adjAdBranch = ?1 AND adj.adjAdCompany = ?2");
			query.setParameter(1, ADJ_AD_BRNCH);
			query.setParameter(2, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentHome.findAdjRequestToGenerateByAdCompanyAndBrCode(java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getAdjByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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
	public LocalInvAdjustment buildAdjustment() throws CreateException {
		try {

			LocalInvAdjustment entity = new LocalInvAdjustment();

			Debug.print("invAdjustmentBean buildAdjustment");

			entity.setAdjDocumentNumber(ADJ_DCMNT_NMBR);
			entity.setAdjReferenceNumber(ADJ_RFRNC_NMBR);
			entity.setAdjDescription(ADJ_DESC);
			entity.setAdjDate(ADJ_DT);
			entity.setAdjType(ADJ_TYP);
			entity.setAdjApprovalStatus(ADJ_APPRVL_STATUS);
			entity.setAdjPosted(ADJ_PSTD);
			entity.setAdjGenerated(ADJ_GNRTD);
			entity.setAdjCreatedBy(ADJ_CRTD_BY);
			entity.setAdjDateCreated(ADJ_DT_CRTD);
			entity.setAdjLastModifiedBy(ADJ_LST_MDFD_BY);
			entity.setAdjDateLastModified(ADJ_DT_LST_MDFD);
			entity.setAdjApprovedRejectedBy(ADJ_APPRVD_RJCTD_BY);
			entity.setAdjDateApprovedRejected(ADJ_DT_APPRVD_RJCTD);
			entity.setAdjPostedBy(ADJ_PSTD_BY);
			entity.setAdjDatePosted(ADJ_DT_PSTD);
			entity.setAdjNotedBy(ADJ_NT_BY);
			entity.setAdjReasonForRejection(ADJ_RSN_FR_RJCTN);
			entity.setAdjIsCostVariance(ADJ_IS_CST_VRNC);
			entity.setAdjVoid(ADJ_VD);
			entity.setAdjAdBranch(ADJ_AD_BRNCH);
			entity.setAdjAdCompany(ADJ_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}
	
	public LocalInvAdjustmentHome AdjDocumentNumber(String ADJ_DCMNT_NMBR) {
		this.ADJ_DCMNT_NMBR = ADJ_DCMNT_NMBR;
		return this;
	}
	
	public LocalInvAdjustmentHome AdjReferenceNumber(String ADJ_RFRNC_NMBR) {
		this.ADJ_RFRNC_NMBR = ADJ_RFRNC_NMBR;
		return this;
	}
	
	public LocalInvAdjustmentHome AdjDescription(String ADJ_DESC) {
		this.ADJ_DESC = ADJ_DESC;
		return this;
	}
	
	public LocalInvAdjustmentHome AdjDate(java.util.Date ADJ_DT) {
		this.ADJ_DT = ADJ_DT;
		return this;
	}
	
	public LocalInvAdjustmentHome AdjType(String ADJ_TYP) {
		this.ADJ_TYP = ADJ_TYP;
		return this;
	}
	
	public LocalInvAdjustmentHome AdjApprovalStatus(String ADJ_APPRVL_STATUS) {
		this.ADJ_APPRVL_STATUS = ADJ_APPRVL_STATUS;
		return this;
	}
	
	public LocalInvAdjustmentHome AdjPosted(byte ADJ_PSTD) {
		this.ADJ_PSTD = ADJ_PSTD;
		return this;
	}
	
	public LocalInvAdjustmentHome AdjCreatedBy(String ADJ_CRTD_BY) {
		this.ADJ_CRTD_BY = ADJ_CRTD_BY;
		return this;
	}
	
	public LocalInvAdjustmentHome AdjDateCreated(java.util.Date ADJ_DT_CRTD) {
		this.ADJ_DT_CRTD = ADJ_DT_CRTD;
		return this;
	}
	
	public LocalInvAdjustmentHome AdjAdBranch(Integer ADJ_AD_BRNCH) {
		this.ADJ_AD_BRNCH = ADJ_AD_BRNCH;
		return this;
	}
	
	public LocalInvAdjustmentHome AdjAdCompany(Integer ADJ_AD_CMPNY) {
		this.ADJ_AD_CMPNY = ADJ_AD_CMPNY;
		return this;
	}

	public LocalInvAdjustment create(java.lang.Integer INV_ADJ_CODE, java.lang.String ADJ_DCMNT_NMBR,
                                     java.lang.String ADJ_RFRNC_NMBR, String ADJ_DESC, java.util.Date ADJ_DT, java.lang.String ADJ_TYP,
                                     java.lang.String ADJ_APPRVL_STATUS, byte ADJ_PSTD, java.lang.String ADJ_CRTD_BY, java.util.Date ADJ_DT_CRTD,
                                     java.lang.String ADJ_LST_MDFD_BY, java.util.Date ADJ_DT_LST_MDFD, java.lang.String ADJ_APPRVD_RJCTD_BY,
                                     java.util.Date ADJ_DT_APPRVD_RJCTD, java.lang.String ADJ_PSTD_BY, java.util.Date ADJ_DT_PSTD,
                                     String ADJ_NT_BY, String ADJ_RSN_FR_RJCTN, byte ADJ_IS_CST_VRNC, byte ADJ_VD, Integer ADJ_AD_BRNCH,
                                     Integer ADJ_AD_CMPNY) throws CreateException {
		try {

			LocalInvAdjustment entity = new LocalInvAdjustment();

			Debug.print("invAdjustmentBean create");

			entity.setAdjCode(INV_ADJ_CODE);
			entity.setAdjDocumentNumber(ADJ_DCMNT_NMBR);
			entity.setAdjReferenceNumber(ADJ_RFRNC_NMBR);
			entity.setAdjDescription(ADJ_DESC);
			entity.setAdjDate(ADJ_DT);
			entity.setAdjType(ADJ_TYP);
			entity.setAdjApprovalStatus(ADJ_APPRVL_STATUS);
			entity.setAdjPosted(ADJ_PSTD);
			entity.setAdjCreatedBy(ADJ_CRTD_BY);
			entity.setAdjDateCreated(ADJ_DT_CRTD);
			entity.setAdjLastModifiedBy(ADJ_LST_MDFD_BY);
			entity.setAdjDateLastModified(ADJ_DT_LST_MDFD);
			entity.setAdjApprovedRejectedBy(ADJ_APPRVD_RJCTD_BY);
			entity.setAdjDateApprovedRejected(ADJ_DT_APPRVD_RJCTD);
			entity.setAdjPostedBy(ADJ_PSTD_BY);
			entity.setAdjDatePosted(ADJ_DT_PSTD);
			entity.setAdjNotedBy(ADJ_NT_BY);
			entity.setAdjReasonForRejection(ADJ_RSN_FR_RJCTN);
			entity.setAdjIsCostVariance(ADJ_IS_CST_VRNC);
			entity.setAdjVoid(ADJ_VD);
			entity.setAdjAdBranch(ADJ_AD_BRNCH);
			entity.setAdjAdCompany(ADJ_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvAdjustment create(java.lang.String ADJ_DCMNT_NMBR, java.lang.String ADJ_RFRNC_NMBR,
                                     String ADJ_DESC, java.util.Date ADJ_DT, java.lang.String ADJ_TYP, java.lang.String ADJ_APPRVL_STATUS,
                                     byte ADJ_PSTD, byte ADJ_GNRTD, java.lang.String ADJ_CRTD_BY, java.util.Date ADJ_DT_CRTD,
                                     java.lang.String ADJ_LST_MDFD_BY, java.util.Date ADJ_DT_LST_MDFD, java.lang.String ADJ_APPRVD_RJCTD_BY,
                                     java.util.Date ADJ_DT_APPRVD_RJCTD, java.lang.String ADJ_PSTD_BY, java.util.Date ADJ_DT_PSTD,
                                     String ADJ_NT_BY, String ADJ_RSN_FR_RJCTN, byte ADJ_IS_CST_VRNC, byte ADJ_VD, Integer ADJ_AD_BRNCH,
                                     Integer ADJ_AD_CMPNY) throws CreateException {
		try {

			LocalInvAdjustment entity = new LocalInvAdjustment();

			Debug.print("invAdjustmentBean create");

			entity.setAdjDocumentNumber(ADJ_DCMNT_NMBR);
			entity.setAdjReferenceNumber(ADJ_RFRNC_NMBR);
			entity.setAdjDescription(ADJ_DESC);
			entity.setAdjDate(ADJ_DT);
			entity.setAdjType(ADJ_TYP);
			entity.setAdjApprovalStatus(ADJ_APPRVL_STATUS);
			entity.setAdjPosted(ADJ_PSTD);
			entity.setAdjGenerated(ADJ_GNRTD);
			entity.setAdjCreatedBy(ADJ_CRTD_BY);
			entity.setAdjDateCreated(ADJ_DT_CRTD);
			entity.setAdjLastModifiedBy(ADJ_LST_MDFD_BY);
			entity.setAdjDateLastModified(ADJ_DT_LST_MDFD);
			entity.setAdjApprovedRejectedBy(ADJ_APPRVD_RJCTD_BY);
			entity.setAdjDateApprovedRejected(ADJ_DT_APPRVD_RJCTD);
			entity.setAdjPostedBy(ADJ_PSTD_BY);
			entity.setAdjDatePosted(ADJ_DT_PSTD);
			entity.setAdjNotedBy(ADJ_NT_BY);
			entity.setAdjReasonForRejection(ADJ_RSN_FR_RJCTN);
			entity.setAdjIsCostVariance(ADJ_IS_CST_VRNC);
			entity.setAdjVoid(ADJ_VD);
			entity.setAdjAdBranch(ADJ_AD_BRNCH);
			entity.setAdjAdCompany(ADJ_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvAdjustment create(java.lang.String ADJ_DCMNT_NMBR, java.lang.String ADJ_RFRNC_NMBR,
                                     String ADJ_DESC, java.util.Date ADJ_DT, java.lang.String ADJ_TYP, java.lang.String ADJ_APPRVL_STATUS,
                                     byte ADJ_PSTD, java.lang.String ADJ_CRTD_BY, java.util.Date ADJ_DT_CRTD, java.lang.String ADJ_LST_MDFD_BY,
                                     java.util.Date ADJ_DT_LST_MDFD, java.lang.String ADJ_APPRVD_RJCTD_BY, java.util.Date ADJ_DT_APPRVD_RJCTD,
                                     java.lang.String ADJ_PSTD_BY, java.util.Date ADJ_DT_PSTD, String ADJ_NT_BY, String ADJ_RSN_FR_RJCTN,
                                     byte ADJ_IS_CST_VRNC, byte ADJ_VD, Integer ADJ_AD_BRNCH, Integer ADJ_AD_CMPNY) throws CreateException {
		try {

			LocalInvAdjustment entity = new LocalInvAdjustment();

			Debug.print("invAdjustmentBean create");

			entity.setAdjDocumentNumber(ADJ_DCMNT_NMBR);
			entity.setAdjReferenceNumber(ADJ_RFRNC_NMBR);
			entity.setAdjDescription(ADJ_DESC);
			entity.setAdjDate(ADJ_DT);
			entity.setAdjType(ADJ_TYP);
			entity.setAdjApprovalStatus(ADJ_APPRVL_STATUS);
			entity.setAdjPosted(ADJ_PSTD);
			entity.setAdjCreatedBy(ADJ_CRTD_BY);
			entity.setAdjDateCreated(ADJ_DT_CRTD);
			entity.setAdjLastModifiedBy(ADJ_LST_MDFD_BY);
			entity.setAdjDateLastModified(ADJ_DT_LST_MDFD);
			entity.setAdjApprovedRejectedBy(ADJ_APPRVD_RJCTD_BY);
			entity.setAdjDateApprovedRejected(ADJ_DT_APPRVD_RJCTD);
			entity.setAdjPostedBy(ADJ_PSTD_BY);
			entity.setAdjDatePosted(ADJ_DT_PSTD);
			entity.setAdjNotedBy(ADJ_NT_BY);
			entity.setAdjReasonForRejection(ADJ_RSN_FR_RJCTN);
			entity.setAdjIsCostVariance(ADJ_IS_CST_VRNC);
			entity.setAdjVoid(ADJ_VD);
			entity.setAdjAdBranch(ADJ_AD_BRNCH);
			entity.setAdjAdCompany(ADJ_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}