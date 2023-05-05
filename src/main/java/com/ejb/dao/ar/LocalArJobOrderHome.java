package com.ejb.dao.ar;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArJobOrder;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArJobOrderHome {

	public static final String JNDI_NAME = "LocalArJobOrderHome!com.ejb.ar.LocalArJobOrderHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArJobOrderHome() {
	}

	// FINDER METHODS

	public LocalArJobOrder findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArJobOrder entity = (LocalArJobOrder) em.find(new LocalArJobOrder(),
					pk);
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

	public LocalArJobOrder findByJoDocumentNumberAndBrCode(java.lang.String JO_DCMNT_NMBR,
			java.lang.Integer JO_AD_BRNCH, java.lang.Integer JO_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jo) FROM ArJobOrder jo  WHERE jo.joDocumentNumber = ?1 AND jo.joAdBranch = ?2 AND jo.joAdCompany = ?3");
			query.setParameter(1, JO_DCMNT_NMBR);
			query.setParameter(2, JO_AD_BRNCH);
			query.setParameter(3, JO_AD_CMPNY);
            return (LocalArJobOrder) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArJobOrderHome.findByJoDocumentNumberAndBrCode(java.lang.String JO_DCMNT_NMBR, java.lang.Integer JO_AD_BRNCH, java.lang.Integer JO_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderHome.findByJoDocumentNumberAndBrCode(java.lang.String JO_DCMNT_NMBR, java.lang.Integer JO_AD_BRNCH, java.lang.Integer JO_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArJobOrder findByJoDocumentNumberAndCstCustomerCode(java.lang.String JO_DCMNT_NMBR,
			java.lang.String CST_CSTMR_CODE, java.lang.Integer JO_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jo) FROM ArJobOrder jo WHERE jo.joDocumentNumber = ?1 AND jo.arCustomer.cstCustomerCode = ?2 AND jo.joVoid = 0 AND jo.joAdCompany = ?3");
			query.setParameter(1, JO_DCMNT_NMBR);
			query.setParameter(2, CST_CSTMR_CODE);
			query.setParameter(3, JO_AD_CMPNY);
            return (LocalArJobOrder) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArJobOrderHome.findByJoDocumentNumberAndCstCustomerCode(java.lang.String JO_DCMNT_NMBR, java.lang.String CST_CSTMR_CODE, java.lang.Integer JO_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderHome.findByJoDocumentNumberAndCstCustomerCode(java.lang.String JO_DCMNT_NMBR, java.lang.String CST_CSTMR_CODE, java.lang.Integer JO_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedJoByCstCustomerCode(java.lang.String CST_CSTMR_CD,
			java.lang.Integer JO_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jo) FROM ArJobOrder jo WHERE jo.joPosted = 1 AND jo.arCustomer.cstCustomerCode = ?1 AND jo.joAdCompany = ?2");
			query.setParameter(1, CST_CSTMR_CD);
			query.setParameter(2, JO_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderHome.findPostedJoByCstCustomerCode(java.lang.String CST_CSTMR_CD, java.lang.Integer JO_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findJoPostedAndApprovedByDateRangeAndAdCompany(java.util.Date JO_DT_FRM,
			java.util.Date JO_DT_TO, java.lang.Integer JO_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jo) FROM ArJobOrder jo  WHERE jo.joPosted = 1 AND jo.joApprovalStatus = 'APPROVED' AND jo.joDateApprovedRejected >= ?1 AND jo.joDateApprovedRejected <= ?2 AND jo.joAdCompany = ?3");
			query.setParameter(1, JO_DT_FRM);
			query.setParameter(2, JO_DT_TO);
			query.setParameter(3, JO_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderHome.findJoPostedAndApprovedByDateRangeAndAdCompany(java.com.util.Date JO_DT_FRM, java.com.util.Date JO_DT_TO, java.lang.Integer JO_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedJoByBrCode(java.lang.Integer JO_AD_BRNCH, java.lang.Integer SO_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jo) FROM ArJobOrder jo WHERE jo.joPosted = 1 AND jo.joVoid = 0 AND jo.joAdBranch = ?1 AND jo.joAdCompany = ?2");
			query.setParameter(1, JO_AD_BRNCH);
			query.setParameter(2, SO_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderHome.findPostedJoByBrCode(java.lang.Integer JO_AD_BRNCH, java.lang.Integer SO_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedJoByMobileBrCode(java.lang.Integer JO_AD_BRNCH, java.lang.Integer JO_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jo) FROM ArJobOrder jo WHERE jo.joPosted = 1 AND jo.joVoid = 0 AND jo.joMobile = 1 AND jo.joAdBranch = ?1 AND jo.joAdCompany = ?2");
			query.setParameter(1, JO_AD_BRNCH);
			query.setParameter(2, JO_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderHome.findPostedJoByMobileBrCode(java.lang.Integer JO_AD_BRNCH, java.lang.Integer JO_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArJobOrder findByJoDocumentNumberAndCstCustomerCodeAndBrCode(java.lang.String JO_DCMNT_NMBR,
			java.lang.String CST_CSTMR_CODE, java.lang.Integer JO_AD_BRNCH, java.lang.Integer JO_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jo) FROM ArJobOrder jo  WHERE jo.joDocumentNumber = ?1 AND jo.arCustomer.cstCustomerCode = ?2 AND jo.joAdBranch = ?3 AND jo.joVoid = 0 AND jo.joAdCompany = ?4");
			query.setParameter(1, JO_DCMNT_NMBR);
			query.setParameter(2, CST_CSTMR_CODE);
			query.setParameter(3, JO_AD_BRNCH);
			query.setParameter(4, JO_AD_CMPNY);
            return (LocalArJobOrder) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArJobOrderHome.findByJoDocumentNumberAndCstCustomerCodeAndBrCode(java.lang.String JO_DCMNT_NMBR, java.lang.String CST_CSTMR_CODE, java.lang.Integer JO_AD_BRNCH, java.lang.Integer JO_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderHome.findByJoDocumentNumberAndCstCustomerCodeAndBrCode(java.lang.String JO_DCMNT_NMBR, java.lang.String CST_CSTMR_CODE, java.lang.Integer JO_AD_BRNCH, java.lang.Integer JO_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftJoByBrCode(java.lang.Integer JO_AD_BRNCH, java.lang.Integer JO_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jo) FROM ArJobOrder jo WHERE jo.joPosted = 0 AND jo.joApprovalStatus IS NULL AND jo.joVoid = 0 AND jo.joAdBranch = ?1 AND jo.joAdCompany = ?2");
			query.setParameter(1, JO_AD_BRNCH);
			query.setParameter(2, JO_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderHome.findDraftJoByBrCode(java.lang.Integer JO_AD_BRNCH, java.lang.Integer JO_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getJOByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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

	public LocalArJobOrder create(Integer JO_CODE, Date JO_DT, String JO_DCMNT_NMBR, String JO_RFRNC_NMBR,
                                  String JO_TRNSCTN_TYP, String JO_DESC, String JO_BLL_TO, String JO_SHP_TO, String JO_TCHNCN,
                                  Date JO_CNVRSN_DT, double JO_CNVRSN_RT, byte JO_VD, byte JO_MBL, String JO_APPRVL_STATUS, byte JO_PSTD,
                                  String JO_RSN_FR_RJCTN, String JO_CRTD_BY, Date JO_DT_CRTD, String JO_LST_MDFD_BY, Date JO_DT_LST_MDFD,
                                  String JO_APPRVD_RJCTD_BY, Date JO_DT_APPRVD_RJCTD, String JO_PSTD_BY, Date JO_DT_PSTD, byte JO_LCK,
                                  byte JO_BO_LCK, String JO_MMO, String JO_JB_ORDR_STTS, Integer JO_AD_BRNCH, Integer JO_AD_CMPNY)
			throws CreateException {
		try {

			LocalArJobOrder entity = new LocalArJobOrder();

			Debug.print("ArJobOrderBean create");

			entity.setJoCode(JO_CODE);
			entity.setJoDate(JO_DT);
			entity.setJoDocumentNumber(JO_DCMNT_NMBR);
			entity.setJoReferenceNumber(JO_RFRNC_NMBR);
			entity.setJoTransactionType(JO_TRNSCTN_TYP);
			entity.setJoDescription(JO_DESC);
			entity.setJoBillTo(JO_BLL_TO);
			entity.setJoShipTo(JO_SHP_TO);
			entity.setJoTechnician(JO_TCHNCN);
			entity.setJoConversionDate(JO_CNVRSN_DT);
			entity.setJoConversionRate(JO_CNVRSN_RT);
			entity.setJoVoid(JO_VD);
			entity.setJoMobile(JO_MBL);
			entity.setJoApprovalStatus(JO_APPRVL_STATUS);
			entity.setJoPosted(JO_PSTD);
			entity.setJoReasonForRejection(JO_RSN_FR_RJCTN);
			entity.setJoCreatedBy(JO_CRTD_BY);
			entity.setJoDateCreated(JO_DT_CRTD);
			entity.setJoLastModifiedBy(JO_LST_MDFD_BY);
			entity.setJoDateLastModified(JO_DT_LST_MDFD);
			entity.setJoApprovedRejectedBy(JO_APPRVD_RJCTD_BY);
			entity.setJoDateApprovedRejected(JO_DT_APPRVD_RJCTD);
			entity.setJoPostedBy(JO_PSTD_BY);
			entity.setJoDatePosted(JO_DT_PSTD);
			entity.setJoLock(JO_LCK);
			entity.setJoBoLock(JO_BO_LCK);
			entity.setJoMemo(JO_MMO);
			entity.setJoJobOrderStatus(JO_JB_ORDR_STTS);
			entity.setJoAdBranch(JO_AD_BRNCH);
			entity.setJoAdCompany(JO_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArJobOrder create(Date JO_DT, String JO_DCMNT_NMBR, String JO_RFRNC_NMBR,
                                  String JO_TRNSCTN_TYP, String JO_DESC, String JO_BLL_TO, String JO_SHP_TO, String JO_TCHNCN,
                                  Date JO_CNVRSN_DT, double JO_CNVRSN_RT, byte JO_VD, byte JO_MBL, String JO_APPRVL_STATUS, byte JO_PSTD,
                                  String JO_RSN_FR_RJCTN, String JO_CRTD_BY, Date JO_DT_CRTD, String JO_LST_MDFD_BY, Date JO_DT_LST_MDFD,
                                  String JO_APPRVD_RJCTD_BY, Date JO_DT_APPRVD_RJCTD, String JO_PSTD_BY, Date JO_DT_PSTD, byte JO_LCK,
                                  byte JO_BO_LCK, String JO_MMO, String JO_JB_ORDR_STTS, Integer JO_AD_BRNCH, Integer JO_AD_CMPNY)
			throws CreateException {
		try {

			LocalArJobOrder entity = new LocalArJobOrder();

			Debug.print("ArJobOrderBean create");

			entity.setJoDate(JO_DT);
			entity.setJoDocumentNumber(JO_DCMNT_NMBR);
			entity.setJoReferenceNumber(JO_RFRNC_NMBR);
			entity.setJoTransactionType(JO_TRNSCTN_TYP);
			entity.setJoDescription(JO_DESC);
			entity.setJoBillTo(JO_BLL_TO);
			entity.setJoShipTo(JO_SHP_TO);
			entity.setJoTechnician(JO_TCHNCN);
			entity.setJoConversionDate(JO_CNVRSN_DT);
			entity.setJoConversionRate(JO_CNVRSN_RT);
			entity.setJoVoid(JO_VD);
			entity.setJoMobile(JO_MBL);
			entity.setJoApprovalStatus(JO_APPRVL_STATUS);
			entity.setJoPosted(JO_PSTD);
			entity.setJoReasonForRejection(JO_RSN_FR_RJCTN);
			entity.setJoCreatedBy(JO_CRTD_BY);
			entity.setJoDateCreated(JO_DT_CRTD);
			entity.setJoLastModifiedBy(JO_LST_MDFD_BY);
			entity.setJoDateLastModified(JO_DT_LST_MDFD);
			entity.setJoApprovedRejectedBy(JO_APPRVD_RJCTD_BY);
			entity.setJoDateApprovedRejected(JO_DT_APPRVD_RJCTD);
			entity.setJoPostedBy(JO_PSTD_BY);
			entity.setJoDatePosted(JO_DT_PSTD);
			entity.setJoLock(JO_LCK);
			entity.setJoBoLock(JO_BO_LCK);
			entity.setJoMemo(JO_MMO);
			entity.setJoJobOrderStatus(JO_JB_ORDR_STTS);
			entity.setJoAdBranch(JO_AD_BRNCH);
			entity.setJoAdCompany(JO_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}