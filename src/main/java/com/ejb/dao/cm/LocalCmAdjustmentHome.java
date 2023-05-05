package com.ejb.dao.cm;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.cm.LocalCmAdjustment;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalCmAdjustmentHome {

	public static final String JNDI_NAME = "LocalCmAdjustmentHome!com.ejb.cm.LocalCmAdjustmentHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalCmAdjustmentHome() {
	}

	// FINDER METHODS

	public LocalCmAdjustment findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalCmAdjustment entity = (LocalCmAdjustment) em
					.find(new LocalCmAdjustment(), pk);
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

	public java.util.Collection findUnreconciledPostedAdjByDateAndBaNameAndAdjTypeAndBrCode(java.util.Date DT,
			java.lang.String BA_NM, java.lang.String ADJ_TYP, java.lang.Integer ADJ_AD_BRNCH,
			java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM CmAdjustment adj WHERE adj.adjDate <= ?1 AND adj.adBankAccount.baName = ?2 AND adj.adjType = ?3 AND adj.adjReconciled = 0 AND adj.adjPosted = 1 AND adj.adjVoid = 0 AND adj.adjAdBranch = ?4 AND adj.adjAdCompany = ?5");
			query.setParameter(1, DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, ADJ_TYP);
			query.setParameter(4, ADJ_AD_BRNCH);
			query.setParameter(5, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmAdjustmentHome.findUnreconciledPostedAdjByDateAndBaNameAndAdjTypeAndBrCode(java.com.util.Date DT, java.lang.String BA_NM, java.lang.String ADJ_TYP, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedAdjByDateAndBaNameAndAdjType(java.util.Date DT, java.lang.String BA_NM,
			java.lang.String ADJ_TYP, java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM CmAdjustment adj WHERE adj.adjDate <= ?1 AND adj.adBankAccount.baName = ?2 AND adj.adjType = ?3 AND adj.adjPosted = 1 AND adj.adjVoid = 0 AND adj.adjAdCompany = ?4");
			query.setParameter(1, DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, ADJ_TYP);
			query.setParameter(4, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmAdjustmentHome.findPostedAdjByDateAndBaNameAndAdjType(java.com.util.Date DT, java.lang.String BA_NM, java.lang.String ADJ_TYP, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnreconciledPostedAdjByDateAndBaNameAndAdjType(java.util.Date DT,
			java.lang.String BA_NM, java.lang.String ADJ_TYP, java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM CmAdjustment adj WHERE adj.adjDate <= ?1 AND adj.adBankAccount.baName = ?2 AND adj.adjType = ?3 AND adj.adjReconciled = 0 AND adj.adjPosted = 1 AND adj.adjVoid = 0 AND adj.adjAdCompany = ?4");
			query.setParameter(1, DT);
			query.setParameter(2, BA_NM);
			query.setParameter(3, ADJ_TYP);
			query.setParameter(4, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmAdjustmentHome.findUnreconciledPostedAdjByDateAndBaNameAndAdjType(java.com.util.Date DT, java.lang.String BA_NM, java.lang.String ADJ_TYP, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalCmAdjustment findAdjByReferenceNumber(java.lang.String ADJ_RFRNC_NMBR, java.lang.Integer ADJ_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM CmAdjustment adj WHERE adj.adjReferenceNumber = ?1 AND adj.adjVoid = 0 AND adj.adjAdCompany = ?2");
			query.setParameter(1, ADJ_RFRNC_NMBR);
			query.setParameter(2, ADJ_AD_CMPNY);
            return (LocalCmAdjustment) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.cm.LocalCmAdjustmentHome.findAdjByReferenceNumber(java.lang.String ADJ_RFRNC_NMBR, java.lang.Integer ADJ_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmAdjustmentHome.findAdjByReferenceNumber(java.lang.String ADJ_RFRNC_NMBR, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}
	
	
	public java.util.Collection findAllAdjByReferenceNumber(java.lang.String ADJ_RFRNC_NMBR, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM CmAdjustment adj WHERE adj.adjReferenceNumber = ?1 AND adj.adjVoid = 0 AND adj.adjAdBranch = ?2 AND adj.adjAdCompany = ?3");
			query.setParameter(1, ADJ_RFRNC_NMBR);
			query.setParameter(2, ADJ_AD_BRNCH);
			query.setParameter(3, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.cm.LocalCmAdjustmentHome.findAllAdjByReferenceNumber(java.lang.String ADJ_RFRNC_NMBR, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmAdjustmentHome.findAllAdjByReferenceNumber(java.lang.String ADJ_RFRNC_NMBR, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedAdjByAdjDateRange(java.util.Date ADJ_DT_FRM, java.util.Date ADJ_DT_TO,
			java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM CmAdjustment adj WHERE adj.adjVoid = 0 AND adj.adjPosted = 1 AND adj.adjDate >= ?1 AND adj.adjDate <= ?2 AND adj.adjAdCompany = ?3");
			query.setParameter(1, ADJ_DT_FRM);
			query.setParameter(2, ADJ_DT_TO);
			query.setParameter(3, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmAdjustmentHome.findPostedAdjByAdjDateRange(java.com.util.Date ADJ_DT_FRM, java.com.util.Date ADJ_DT_TO, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedAdjByCustomerCode(java.lang.String CST_CSTMR_CODE,
			java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM CmAdjustment adj WHERE adj.adjVoid = 0 AND adj.adjType = 'ADVANCE' AND adj.arCustomer.cstCustomerCode = ?1 AND adj.adjAdCompany = ?2");
			query.setParameter(1, CST_CSTMR_CODE);
			query.setParameter(2, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmAdjustmentHome.findPostedAdjByCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedAdjByCustomerCode(java.lang.String CST_CSTMR_CODE,
															java.lang.Integer ADJ_AD_CMPNY,
															String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(adj) FROM CmAdjustment adj "
							+ "WHERE adj.adjVoid = 0 AND adj.adjType = 'ADVANCE' "
							+ "AND adj.arCustomer.cstCustomerCode = ?1 AND adj.adjAdCompany = ?2",
					companyShortName);
			query.setParameter(1, CST_CSTMR_CODE);
			query.setParameter(2, ADJ_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findAdjAll(java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(adj) FROM CmAdjustment adj WHERE adj.adjAdCompany = ?1");
			query.setParameter(1, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmAdjustmentHome.findAdjAll(java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftAdjAll(java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM CmAdjustment adj WHERE adj.adjApprovalStatus IS NULL AND adj.adjAdCompany = ?1");
			query.setParameter(1, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmAdjustmentHome.findDraftAdjAll(java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftAdjByBrCode(java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM CmAdjustment adj WHERE adj.adjApprovalStatus IS NULL AND adj.adjAdBranch = ?1 AND adj.adjAdCompany = ?2");
			query.setParameter(1, ADJ_AD_BRNCH);
			query.setParameter(2, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmAdjustmentHome.findDraftAdjByBrCode(java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedCmAdjByCmAdjDateRange(java.util.Date ADJ_DT_FRM, java.util.Date ADJ_DT_TO,
			java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM CmAdjustment adj  WHERE adj.adjPosted = 0 AND adj.adjVoid = 0 AND adj.adjDate >= ?1 AND adj.adjDate <= ?2 AND adj.adjAdCompany = ?3");
			query.setParameter(1, ADJ_DT_FRM);
			query.setParameter(2, ADJ_DT_TO);
			query.setParameter(3, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmAdjustmentHome.findUnpostedCmAdjByCmAdjDateRange(java.com.util.Date ADJ_DT_FRM, java.com.util.Date ADJ_DT_TO, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalCmAdjustment findByAdjDocumentNumberAndBrCode(java.lang.String ADJ_DCMNT_NMBR,
			java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM CmAdjustment adj  WHERE adj.adjDocumentNumber = ?1 AND adj.adjAdBranch = ?2 AND adj.adjAdCompany = ?3");
			query.setParameter(1, ADJ_DCMNT_NMBR);
			query.setParameter(2, ADJ_AD_BRNCH);
			query.setParameter(3, ADJ_AD_CMPNY);
            return (LocalCmAdjustment) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.cm.LocalCmAdjustmentHome.findByAdjDocumentNumberAndBrCode(java.lang.String ADJ_DCMNT_NMBR, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmAdjustmentHome.findByAdjDocumentNumberAndBrCode(java.lang.String ADJ_DCMNT_NMBR, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(java.lang.String BA_NM,
			java.util.Date ADJ_DT_FRM, java.util.Date ADJ_DT_TO, java.lang.String ADJ_TYP,
			java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM CmAdjustment adj  WHERE adj.adjPosted = 1 AND adj.adjVoid = 0 AND adj.adBankAccount.baIsCashAccount = 0 AND adj.adBankAccount.baName = ?1 AND adj.adjDate >= ?2 AND adj.adjDate <= ?3 AND adj.adjType = ?4 AND adj.adjAdBranch = ?5 AND adj.adjAdCompany = ?6");
			query.setParameter(1, BA_NM);
			query.setParameter(2, ADJ_DT_FRM);
			query.setParameter(3, ADJ_DT_TO);
			query.setParameter(4, ADJ_TYP);
			query.setParameter(5, ADJ_AD_BRNCH);
			query.setParameter(6, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmAdjustmentHome.findPostedAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(java.lang.String BA_NM, java.com.util.Date ADJ_DT_FRM, java.com.util.Date ADJ_DT_TO, java.lang.String ADJ_TYP, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findDraftAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(java.lang.String BA_NM,
			java.util.Date ADJ_DT_FRM, java.util.Date ADJ_DT_TO, java.lang.String ADJ_TYP,
			java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM CmAdjustment adj  WHERE adj.adjPosted = 0 AND adj.adjApprovalStatus IS NULL AND adj.adjVoid = 0 AND adj.adBankAccount.baIsCashAccount = 0 AND adj.adBankAccount.baName = ?1 AND adj.adjDate >= ?2 AND adj.adjDate <= ?3 AND adj.adjType = ?4 AND adj.adjAdBranch = ?5 AND adj.adjAdCompany = ?6");
			query.setParameter(1, BA_NM);
			query.setParameter(2, ADJ_DT_FRM);
			query.setParameter(3, ADJ_DT_TO);
			query.setParameter(4, ADJ_TYP);
			query.setParameter(5, ADJ_AD_BRNCH);
			query.setParameter(6, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmAdjustmentHome.findDraftAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(java.lang.String BA_NM, java.com.util.Date ADJ_DT_FRM, java.com.util.Date ADJ_DT_TO, java.lang.String ADJ_TYP, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findReconciledPostedAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(
			java.lang.String BA_NM, java.util.Date ADJ_DT_FRM, java.util.Date ADJ_DT_TO, java.lang.String ADJ_TYP,
			java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM CmAdjustment adj  WHERE adj.adjPosted = 1 AND adj.adjVoid = 0 AND adj.adjReconciled = 1 AND adj.adBankAccount.baIsCashAccount = 0 AND adj.adBankAccount.baName = ?1 AND adj.adjDate >= ?2 AND adj.adjDate <= ?3 AND adj.adjType = ?4 AND adj.adjAdBranch = ?5 AND adj.adjAdCompany = ?6");
			query.setParameter(1, BA_NM);
			query.setParameter(2, ADJ_DT_FRM);
			query.setParameter(3, ADJ_DT_TO);
			query.setParameter(4, ADJ_TYP);
			query.setParameter(5, ADJ_AD_BRNCH);
			query.setParameter(6, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmAdjustmentHome.findReconciledPostedAdjByBaNameAndAdjDateRangeAndAdjTypeAndBrCode(java.lang.String BA_NM, java.com.util.Date ADJ_DT_FRM, java.com.util.Date ADJ_DT_TO, java.lang.String ADJ_TYP, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalCmAdjustment findAdjByAdjDateAndPpCodeAndCstCodeAndBaName(java.util.Date ADJ_DT,
			java.lang.Integer PP_CODE, java.lang.Integer AR_CST_CODE, java.lang.String BA_NM,
			java.lang.Integer ADJ_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adj) FROM CmAdjustment adj WHERE adj.adjVoid = 0  AND adj.adjDate = ?1 AND adj.hrPayrollPeriod.ppCode = ?2 AND adj.arCustomer.cstCode = ?3 AND adj.adBankAccount.baName = ?4 AND  adj.adjAdCompany = ?5");
			query.setParameter(1, ADJ_DT);
			query.setParameter(2, PP_CODE);
			query.setParameter(3, AR_CST_CODE);
			query.setParameter(4, BA_NM);
			query.setParameter(5, ADJ_AD_CMPNY);
            return (LocalCmAdjustment) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.cm.LocalCmAdjustmentHome.findAdjByAdjDateAndPpCodeAndCstCodeAndBaName(java.com.util.Date ADJ_DT, java.lang.Integer PP_CODE, java.lang.Integer AR_CST_CODE, java.lang.String BA_NM, java.lang.Integer ADJ_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.cm.LocalCmAdjustmentHome.findAdjByAdjDateAndPpCodeAndCstCodeAndBaName(java.com.util.Date ADJ_DT, java.lang.Integer PP_CODE, java.lang.Integer AR_CST_CODE, java.lang.String BA_NM, java.lang.Integer ADJ_AD_CMPNY)");
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

	public LocalCmAdjustment create(Integer CM_ADJ_CODE, String ADJ_TYP, Date ADJ_DT, String ADJ_DCMNT_NMBR,
                                    String ADJ_RFRNC_NMBR, String ADJ_CHCK_NMBR, double ADJ_AMNT, double ADJ_AMNT_APPLD, Date ADJ_CNVRSN_DT,
                                    double ADJ_CNVRSN_RT, String ADJ_MM, String ADJ_VD_APPRVL_STATUS, byte ADJ_VD_PSTD, byte ADJ_VOID,
                                    byte ADJ_RFND, double ADJ_RFND_AMNT, String ADJ_RFND_RFRNC_NMBR, byte ADJ_RCNCLD, Date ADJ_DT_RCNCLD,
                                    String ADJ_APPRVL_STATUS, byte ADJ_PSTD, String ADJ_CRTD_BY, Date ADJ_DT_CRTD, String ADJ_LST_MDFD_BY,
                                    Date ADJ_DT_LST_MDFD, String ADJ_APPRVD_RJCTD_BY, Date ADJ_DT_APPRVD_RJCTD, String ADJ_PSTD_BY,
                                    Date ADJ_DT_PSTD, String ADJ_RSN_FR_RJCTN, Integer ADJ_AD_BRNCH, Integer ADJ_AD_CMPNY)
			throws CreateException {
		try {

			LocalCmAdjustment entity = new LocalCmAdjustment();

			Debug.print("CmAdjustmentBean create");

			entity.setAdjCode(CM_ADJ_CODE);
			entity.setAdjType(ADJ_TYP);
			entity.setAdjDate(ADJ_DT);
			entity.setAdjDocumentNumber(ADJ_DCMNT_NMBR);
			entity.setAdjReferenceNumber(ADJ_RFRNC_NMBR);
			entity.setAdjCheckNumber(ADJ_CHCK_NMBR);
			entity.setAdjAmount(ADJ_AMNT);
			entity.setAdjAmountApplied(ADJ_AMNT_APPLD);
			entity.setAdjConversionDate(ADJ_CNVRSN_DT);
			entity.setAdjConversionRate(ADJ_CNVRSN_RT);
			entity.setAdjMemo(ADJ_MM);
			entity.setAdjVoidApprovalStatus(ADJ_VD_APPRVL_STATUS);
			entity.setAdjVoidPosted(ADJ_VD_PSTD);
			entity.setAdjVoid(ADJ_VOID);
			entity.setAdjRefund(ADJ_RFND);
			entity.setAdjReconciled(ADJ_RCNCLD);
			entity.setAdjDateReconciled(ADJ_DT_RCNCLD);
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
			entity.setAdjReasonForRejection(ADJ_RSN_FR_RJCTN);
			entity.setAdjAdBranch(ADJ_AD_BRNCH);
			entity.setAdjAdCompany(ADJ_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalCmAdjustment create(String ADJ_TYP, Date ADJ_DT, String ADJ_DCMNT_NMBR,
                                    String ADJ_RFRNC_NMBR, String ADJ_CHCK_NMBR, double ADJ_AMNT, double ADJ_AMNT_APPLD, Date ADJ_CNVRSN_DT,
                                    double ADJ_CNVRSN_RT, String ADJ_MM, String ADJ_VD_APPRVL_STATUS, byte ADJ_VD_PSTD, byte ADJ_VOID,
                                    byte ADJ_RFND, double ADJ_RFND_AMNT, String ADJ_RFND_RFRNC_NMBR, byte ADJ_RCNCLD, Date ADJ_DT_RCNCLD,
                                    String ADJ_APPRVL_STATUS, byte ADJ_PSTD, String ADJ_CRTD_BY, Date ADJ_DT_CRTD, String ADJ_LST_MDFD_BY,
                                    Date ADJ_DT_LST_MDFD, String ADJ_APPRVD_RJCTD_BY, Date ADJ_DT_APPRVD_RJCTD, String ADJ_PSTD_BY,
                                    Date ADJ_DT_PSTD, String ADJ_RSN_FR_RJCTN, Integer ADJ_AD_BRNCH, Integer ADJ_AD_CMPNY)
			throws CreateException {
		try {

			LocalCmAdjustment entity = new LocalCmAdjustment();

			Debug.print("CmAdjustmentBean create");

			entity.setAdjType(ADJ_TYP);
			entity.setAdjDate(ADJ_DT);
			entity.setAdjDocumentNumber(ADJ_DCMNT_NMBR);
			entity.setAdjReferenceNumber(ADJ_RFRNC_NMBR);
			entity.setAdjCheckNumber(ADJ_CHCK_NMBR);
			entity.setAdjAmount(ADJ_AMNT);
			entity.setAdjAmountApplied(ADJ_AMNT_APPLD);
			entity.setAdjConversionDate(ADJ_CNVRSN_DT);
			entity.setAdjConversionRate(ADJ_CNVRSN_RT);
			entity.setAdjMemo(ADJ_MM);
			entity.setAdjVoidApprovalStatus(ADJ_VD_APPRVL_STATUS);
			entity.setAdjVoidPosted(ADJ_VD_PSTD);
			entity.setAdjVoid(ADJ_VOID);
			entity.setAdjRefund(ADJ_RFND);
			entity.setAdjReconciled(ADJ_RCNCLD);
			entity.setAdjDateReconciled(ADJ_DT_RCNCLD);
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
			entity.setAdjReasonForRejection(ADJ_RSN_FR_RJCTN);
			entity.setAdjAdBranch(ADJ_AD_BRNCH);
			entity.setAdjAdCompany(ADJ_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}