package com.ejb.dao.ar;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArSalesOrder;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArSalesOrderHome {

	public static final String JNDI_NAME = "LocalArSalesOrderHome!com.ejb.ar.LocalArSalesOrderHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArSalesOrderHome() {
	}

	public LocalArSalesOrder findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArSalesOrder entity = (LocalArSalesOrder) em
					.find(new LocalArSalesOrder(), pk);
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

	public LocalArSalesOrder findByPrimaryKey(java.lang.Integer pk, String companyShortName) throws FinderException {

		try {

			LocalArSalesOrder entity = (LocalArSalesOrder) em
					.findPerCompany(new LocalArSalesOrder(), pk, companyShortName);
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

	public LocalArSalesOrder findBySoDocumentNumberAndBrCode(java.lang.String SO_DCMNT_NMBR,
			java.lang.Integer SO_AD_BRNCH, java.lang.Integer SO_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(so) FROM ArSalesOrder so  WHERE so.soDocumentNumber = ?1 AND so.soAdBranch = ?2 AND so.soAdCompany = ?3");
			query.setParameter(1, SO_DCMNT_NMBR);
			query.setParameter(2, SO_AD_BRNCH);
			query.setParameter(3, SO_AD_CMPNY);
            return (LocalArSalesOrder) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArSalesOrderHome.findBySoDocumentNumberAndBrCode(java.lang.String SO_DCMNT_NMBR, java.lang.Integer SO_AD_BRNCH, java.lang.Integer SO_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderHome.findBySoDocumentNumberAndBrCode(java.lang.String SO_DCMNT_NMBR, java.lang.Integer SO_AD_BRNCH, java.lang.Integer SO_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArSalesOrder findBySoDocumentNumberAndCstCustomerCode(java.lang.String SO_DCMNT_NMBR,
			java.lang.String CST_CSTMR_CODE, java.lang.Integer SO_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(so) FROM ArSalesOrder so  WHERE so.soDocumentNumber = ?1 AND so.arCustomer.cstCustomerCode = ?2 AND so.soVoid = 0 AND so.soAdCompany = ?3");
			query.setParameter(1, SO_DCMNT_NMBR);
			query.setParameter(2, CST_CSTMR_CODE);
			query.setParameter(3, SO_AD_CMPNY);
            return (LocalArSalesOrder) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArSalesOrderHome.findBySoDocumentNumberAndCstCustomerCode(java.lang.String SO_DCMNT_NMBR, java.lang.String CST_CSTMR_CODE, java.lang.Integer SO_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderHome.findBySoDocumentNumberAndCstCustomerCode(java.lang.String SO_DCMNT_NMBR, java.lang.String CST_CSTMR_CODE, java.lang.Integer SO_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedSoByCstCustomerCode(java.lang.String CST_CSTMR_CD,
			java.lang.Integer SO_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(so) FROM ArSalesOrder so WHERE so.soPosted = 1 AND so.arCustomer.cstCustomerCode = ?1 AND so.soAdCompany = ?2");
			query.setParameter(1, CST_CSTMR_CD);
			query.setParameter(2, SO_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderHome.findPostedSoByCstCustomerCode(java.lang.String CST_CSTMR_CD, java.lang.Integer SO_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findSoPostedAndApprovedByDateRangeAndAdCompany(java.util.Date SO_DT_FRM,
			java.util.Date SO_DT_TO, java.lang.Integer SO_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(so) FROM ArSalesOrder so "
					+ "WHERE so.soPosted = 1 AND so.soApprovalStatus = 'APPROVED' "
					+ "AND so.soDateApprovedRejected >= ?1 AND so.soDateApprovedRejected <= ?2 "
					+ "AND so.soAdCompany = ?3");
			query.setParameter(1, SO_DT_FRM);
			query.setParameter(2, SO_DT_TO);
			query.setParameter(3, SO_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findSoPostedAndApprovedByDateRangeAndAdCompany(
			java.util.Date SO_DT_FRM, java.util.Date SO_DT_TO, java.lang.Integer SO_AD_CMPNY, String companyShortName) {

		try {
			Query query = em.createQueryPerCompany("SELECT OBJECT(so) FROM ArSalesOrder so "
					+ "WHERE so.soPosted = 1 AND so.soApprovalStatus = 'APPROVED' "
					+ "AND so.soDateApprovedRejected >= ?1 AND so.soDateApprovedRejected <= ?2 "
					+ "AND so.soAdCompany = ?3", companyShortName.toLowerCase());
			query.setParameter(1, SO_DT_FRM);
			query.setParameter(2, SO_DT_TO);
			query.setParameter(3, SO_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findPostedSoByBrCode(java.lang.Integer SO_AD_BRNCH, java.lang.Integer SO_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(so) FROM ArSalesOrder so WHERE so.soPosted = 1 AND so.soVoid = 0 AND so.soAdBranch = ?1 AND so.soAdCompany = ?2");
			query.setParameter(1, SO_AD_BRNCH);
			query.setParameter(2, SO_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderHome.findPostedSoByBrCode(java.lang.Integer SO_AD_BRNCH, java.lang.Integer SO_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedSoByMobileBrCode(java.lang.Integer SO_AD_BRNCH, java.lang.Integer SO_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(so) FROM ArSalesOrder so WHERE so.soPosted = 1 AND so.soVoid = 0 AND so.soMobile = 1 AND so.soAdBranch = ?1 AND so.soAdCompany = ?2");
			query.setParameter(1, SO_AD_BRNCH);
			query.setParameter(2, SO_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderHome.findPostedSoByMobileBrCode(java.lang.Integer SO_AD_BRNCH, java.lang.Integer SO_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArSalesOrder findBySoDocumentNumberAndCstCustomerCodeAndBrCode(java.lang.String SO_DCMNT_NMBR,
			java.lang.String CST_CSTMR_CODE, java.lang.Integer SO_AD_BRNCH, java.lang.Integer SO_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(so) FROM ArSalesOrder so  WHERE so.soDocumentNumber = ?1 AND so.arCustomer.cstCustomerCode = ?2 AND so.soAdBranch = ?3 AND so.soVoid = 0 AND so.soAdCompany = ?4");
			query.setParameter(1, SO_DCMNT_NMBR);
			query.setParameter(2, CST_CSTMR_CODE);
			query.setParameter(3, SO_AD_BRNCH);
			query.setParameter(4, SO_AD_CMPNY);
            return (LocalArSalesOrder) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArSalesOrderHome.findBySoDocumentNumberAndCstCustomerCodeAndBrCode(java.lang.String SO_DCMNT_NMBR, java.lang.String CST_CSTMR_CODE, java.lang.Integer SO_AD_BRNCH, java.lang.Integer SO_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderHome.findBySoDocumentNumberAndCstCustomerCodeAndBrCode(java.lang.String SO_DCMNT_NMBR, java.lang.String CST_CSTMR_CODE, java.lang.Integer SO_AD_BRNCH, java.lang.Integer SO_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftSoByBrCode(java.lang.Integer SO_AD_BRNCH, java.lang.Integer SO_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(so) FROM ArSalesOrder so WHERE so.soPosted = 0 AND so.soApprovalStatus IS NULL AND so.soVoid = 0 AND so.soAdBranch = ?1 AND so.soAdCompany = ?2");
			query.setParameter(1, SO_AD_BRNCH);
			query.setParameter(2, SO_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderHome.findDraftSoByBrCode(java.lang.Integer SO_AD_BRNCH, java.lang.Integer SO_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftSoByTransactionStatus(java.lang.String SO_TRNSCTN_STTS,
			java.lang.Integer SO_AD_BRNCH, java.lang.Integer SO_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(so) FROM ArSalesOrder so WHERE so.soPosted = 0 AND so.soApprovalStatus IS NULL AND so.soVoid = 0 AND so.soTransactionStatus = ?1 AND so.soAdBranch = ?2 AND so.soAdCompany = ?3");
			query.setParameter(1, SO_TRNSCTN_STTS);
			query.setParameter(2, SO_AD_BRNCH);
			query.setParameter(3, SO_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderHome.findDraftSoByTransactionStatus(java.lang.String SO_TRNSCTN_STTS, java.lang.Integer SO_AD_BRNCH, java.lang.Integer SO_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getSOByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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

	public LocalArSalesOrder create(Integer SO_CODE, Date SO_DT, String SO_DCMNT_NMBR, String SO_RFRNC_NMBR,
                                    String SO_TRNSCTN_TYP, String SO_DESC, String SO_SHPPNG_LN, String SO_PRT, String SO_BLL_TO,
                                    String SO_SHP_TO, Date SO_CNVRSN_DT, double SO_CNVRSN_RT, byte SO_VD, byte SO_MBL, String SO_APPRVL_STATUS,
                                    byte SO_PSTD, String SO_RSN_FR_RJCTN, String SO_CRTD_BY, Date SO_DT_CRTD, String SO_LST_MDFD_BY,
                                    Date SO_DT_LST_MDFD, String SO_APPRVD_RJCTD_BY, Date SO_DT_APPRVD_RJCTD, String SO_PSTD_BY, Date SO_DT_PSTD,
                                    byte SO_LCK, byte SO_BO_LCK, String SO_MMO, String SO_TRNSCTN_STTS, Integer SO_AD_BRNCH,
                                    Integer SO_AD_CMPNY) throws CreateException {
		try {

			LocalArSalesOrder entity = new LocalArSalesOrder();

			Debug.print("ArSalesOrderBean create");

			entity.setSoCode(SO_CODE);
			entity.setSoDate(SO_DT);
			entity.setSoDocumentNumber(SO_DCMNT_NMBR);
			entity.setSoReferenceNumber(SO_RFRNC_NMBR);
			entity.setSoTransactionType(SO_TRNSCTN_TYP);
			entity.setSoDescription(SO_DESC);
			entity.setSoShippingLine(SO_SHPPNG_LN);
			entity.setSoPort(SO_PRT);
			entity.setSoBillTo(SO_BLL_TO);
			entity.setSoShipTo(SO_SHP_TO);
			entity.setSoConversionDate(SO_CNVRSN_DT);
			entity.setSoConversionRate(SO_CNVRSN_RT);
			entity.setSoVoid(SO_VD);
			entity.setSoMobile(SO_MBL);
			entity.setSoApprovalStatus(SO_APPRVL_STATUS);
			entity.setSoPosted(SO_PSTD);
			entity.setSoReasonForRejection(SO_RSN_FR_RJCTN);
			entity.setSoCreatedBy(SO_CRTD_BY);
			entity.setSoDateCreated(SO_DT_CRTD);
			entity.setSoLastModifiedBy(SO_LST_MDFD_BY);
			entity.setSoDateLastModified(SO_DT_LST_MDFD);
			entity.setSoApprovedRejectedBy(SO_APPRVD_RJCTD_BY);
			entity.setSoDateApprovedRejected(SO_DT_APPRVD_RJCTD);
			entity.setSoPostedBy(SO_PSTD_BY);
			entity.setSoDatePosted(SO_DT_PSTD);
			entity.setSoLock(SO_LCK);
			entity.setSoBoLock(SO_BO_LCK);
			entity.setSoMemo(SO_MMO);
			entity.setSoTransactionStatus(SO_TRNSCTN_STTS);
			entity.setSoAdBranch(SO_AD_BRNCH);
			entity.setSoAdCompany(SO_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArSalesOrder create(Date SO_DT, String SO_DCMNT_NMBR, String SO_RFRNC_NMBR,
                                    String SO_TRNSCTN_TYP, String SO_DESC, String SO_SHPPNG_LN, String SO_PRT, String SO_BLL_TO,
                                    String SO_SHP_TO, Date SO_CNVRSN_DT, double SO_CNVRSN_RT, byte SO_VD, byte SO_MBL, String SO_APPRVL_STATUS,
                                    byte SO_PSTD, String SO_RSN_FR_RJCTN, String SO_CRTD_BY, Date SO_DT_CRTD, String SO_LST_MDFD_BY,
                                    Date SO_DT_LST_MDFD, String SO_APPRVD_RJCTD_BY, Date SO_DT_APPRVD_RJCTD, String SO_PSTD_BY, Date SO_DT_PSTD,
                                    byte SO_LCK, byte SO_BO_LCK, String SO_MMO, String SO_TRNSCTN_STTS, Integer SO_AD_BRNCH,
                                    Integer SO_AD_CMPNY) throws CreateException {
		try {

			LocalArSalesOrder entity = new LocalArSalesOrder();

			Debug.print("ArSalesOrderBean create");

			entity.setSoDate(SO_DT);
			entity.setSoDocumentNumber(SO_DCMNT_NMBR);
			entity.setSoReferenceNumber(SO_RFRNC_NMBR);
			entity.setSoTransactionType(SO_TRNSCTN_TYP);
			entity.setSoDescription(SO_DESC);
			entity.setSoShippingLine(SO_SHPPNG_LN);
			entity.setSoPort(SO_PRT);
			entity.setSoBillTo(SO_BLL_TO);
			entity.setSoShipTo(SO_SHP_TO);
			entity.setSoConversionDate(SO_CNVRSN_DT);
			entity.setSoConversionRate(SO_CNVRSN_RT);
			entity.setSoVoid(SO_VD);
			entity.setSoMobile(SO_MBL);
			entity.setSoApprovalStatus(SO_APPRVL_STATUS);
			entity.setSoPosted(SO_PSTD);
			entity.setSoReasonForRejection(SO_RSN_FR_RJCTN);
			entity.setSoCreatedBy(SO_CRTD_BY);
			entity.setSoDateCreated(SO_DT_CRTD);
			entity.setSoLastModifiedBy(SO_LST_MDFD_BY);
			entity.setSoDateLastModified(SO_DT_LST_MDFD);
			entity.setSoApprovedRejectedBy(SO_APPRVD_RJCTD_BY);
			entity.setSoDateApprovedRejected(SO_DT_APPRVD_RJCTD);
			entity.setSoPostedBy(SO_PSTD_BY);
			entity.setSoDatePosted(SO_DT_PSTD);
			entity.setSoLock(SO_LCK);
			entity.setSoBoLock(SO_BO_LCK);
			entity.setSoMemo(SO_MMO);
			entity.setSoTransactionStatus(SO_TRNSCTN_STTS);
			entity.setSoAdBranch(SO_AD_BRNCH);
			entity.setSoAdCompany(SO_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}