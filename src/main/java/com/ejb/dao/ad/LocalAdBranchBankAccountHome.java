package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdBranchBankAccount;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdBranchBankAccountHome {

	public static final String JNDI_NAME = "LocalAdBranchBankAccountHome!com.ejb.ad.LocalAdBranchBankAccountHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdBranchBankAccountHome() {
	}

	// FINDER METHODS

	public LocalAdBranchBankAccount findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdBranchBankAccount entity = (LocalAdBranchBankAccount) em
					.find(new LocalAdBranchBankAccount(), pk);
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

	public java.util.Collection findBbaByBaCodeAndRsName(java.lang.Integer BA_CODE, java.lang.String RS_NM,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bba) FROM AdBranchBankAccount bba, IN(bba.adBranch.adBranchResponsibilities) brs WHERE bba.adBankAccount.baCode = ?1 AND brs.adResponsibility.rsName = ?2 AND bba.bbaAdCompany = ?3");
			query.setParameter(1, BA_CODE);
			query.setParameter(2, RS_NM);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchBankAccountHome.findBbaByBaCodeAndRsName(java.lang.Integer BA_CODE, java.lang.String RS_NM, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBbaByBaCode(java.lang.Integer BA_CODE, java.lang.Integer AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bba) FROM AdBranchBankAccount bba WHERE bba.adBankAccount.baCode = ?1 AND bba.bbaAdCompany = ?2");
			query.setParameter(1, BA_CODE);
			query.setParameter(2, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchBankAccountHome.findBbaByBaCode(java.lang.Integer BA_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBranchBankAccount findBbaByBaCodeAndBrCode(java.lang.Integer BA_CODE, java.lang.Integer BR_CODE,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bba) FROM AdBranchBankAccount bba WHERE bba.adBankAccount.baCode = ?1 AND bba.adBranch.brCode = ?2 AND bba.bbaAdCompany = ?3");
			query.setParameter(1, BA_CODE);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, AD_CMPNY);
            return (LocalAdBranchBankAccount) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdBranchBankAccountHome.findBbaByBaCodeAndBrCode(java.lang.Integer BA_CODE, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchBankAccountHome.findBbaByBaCodeAndBrCode(java.lang.Integer BA_CODE, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBranchBankAccount findBbaByBaCodeAndBrCode(java.lang.Integer BA_CODE, java.lang.Integer BR_CODE,
															 java.lang.Integer AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(bba) FROM AdBranchBankAccount bba "
							+ "WHERE bba.adBankAccount.baCode = ?1 AND bba.adBranch.brCode = ?2 AND bba.bbaAdCompany = ?3",
					companyShortName);
			query.setParameter(1, BA_CODE);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, AD_CMPNY);
			return (LocalAdBranchBankAccount) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findBBaByBaNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY,
			char NEW, char UPDATED, char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bba) FROM AdBranchBankAccount bba WHERE (bba.bbaDownloadStatus = ?3 OR bba.bbaDownloadStatus = ?4 OR bba.bbaDownloadStatus = ?5) AND bba.adBranch.brCode = ?1 AND bba.bbaAdCompany = ?2");
			query.setParameter(1, BR_CODE);
			query.setParameter(2, AD_CMPNY);
			query.setParameter(3, NEW);
			query.setParameter(4, UPDATED);
			query.setParameter(5, DOWNLOADED_UPDATED);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchBankAccountHome.findBBaByBaNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY, char NEW, char UPDATED, char DOWNLOADED_UPDATED)");
			throw ex;
		}
	}

	public java.util.Collection findBBaByBaNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY,
														 char NEW, char UPDATED, char DOWNLOADED_UPDATED, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(bba) FROM AdBranchBankAccount bba "
							+ "WHERE (bba.bbaDownloadStatus = ?3 OR bba.bbaDownloadStatus = ?4 OR bba.bbaDownloadStatus = ?5) "
							+ "AND bba.adBranch.brCode = ?1 AND bba.bbaAdCompany = ?2", companyShortName);
			query.setParameter(1, BR_CODE);
			query.setParameter(2, AD_CMPNY);
			query.setParameter(3, NEW);
			query.setParameter(4, UPDATED);
			query.setParameter(5, DOWNLOADED_UPDATED);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalAdBranchBankAccount findBbaByBaNameAndBrCode(java.lang.String BA_NM, java.lang.Integer BR_CODE,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bba) FROM AdBranchBankAccount bba WHERE bba.adBankAccount.baName = ?1 AND bba.adBranch.brCode = ?2 AND bba.bbaAdCompany = ?3");
			query.setParameter(1, BA_NM);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, AD_CMPNY);
            return (LocalAdBranchBankAccount) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdBranchBankAccountHome.findBbaByBaNameAndBrCode(java.lang.String BA_NM, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchBankAccountHome.findBbaByBaNameAndBrCode(java.lang.String BA_NM, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalAdBranchBankAccount create(Integer BBA_CODE, Integer BBA_COA_GL_CSH_ACCNT,
                                           Integer BBA_COA_GL_BNK_CHRG_ACCNT, Integer BBA_COA_GL_INTRST_ACCNT, Integer BBA_COA_GL_ADJSTMNT_ACCNT,
                                           Integer BBA_COA_GL_SLS_DSCNT_ACCNT, Integer BBA_COA_GL_ADVNC_ACCNT, char BBA_DWNLD_STATUS,
                                           Integer BBA_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchBankAccount entity = new LocalAdBranchBankAccount();

			Debug.print("AdBranchBankAccountBean create");

			entity.setBbaCode(BBA_CODE);
			entity.setBbaGlCoaCashAccount(BBA_COA_GL_CSH_ACCNT);
			entity.setBbaGlCoaBankChargeAccount(BBA_COA_GL_BNK_CHRG_ACCNT);
			entity.setBbaGlCoaInterestAccount(BBA_COA_GL_INTRST_ACCNT);
			entity.setBbaGlCoaAdjustmentAccount(BBA_COA_GL_ADJSTMNT_ACCNT);
			entity.setBbaGlCoaSalesDiscountAccount(BBA_COA_GL_SLS_DSCNT_ACCNT);
			entity.setBbaGlCoaAdvanceAccount(BBA_COA_GL_ADVNC_ACCNT);
			entity.setBbaDownloadStatus(BBA_DWNLD_STATUS);
			entity.setBbaAdCompany(BBA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdBranchBankAccount create(Integer BBA_COA_GL_CSH_ACCNT, Integer BBA_COA_GL_BNK_CHRG_ACCNT,
                                           Integer BBA_COA_GL_INTRST_ACCNT, Integer BBA_COA_GL_ADJSTMNT_ACCNT, Integer BBA_COA_GL_SLS_DSCNT_ACCNT,
                                           Integer BBA_COA_GL_ADVNC_ACCNT, char BBA_DWNLD_STATUS, Integer BBA_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchBankAccount entity = new LocalAdBranchBankAccount();

			Debug.print("AdBranchBankAccountBean create");

			entity.setBbaGlCoaCashAccount(BBA_COA_GL_CSH_ACCNT);
			entity.setBbaGlCoaBankChargeAccount(BBA_COA_GL_BNK_CHRG_ACCNT);
			entity.setBbaGlCoaInterestAccount(BBA_COA_GL_INTRST_ACCNT);
			entity.setBbaGlCoaAdjustmentAccount(BBA_COA_GL_ADJSTMNT_ACCNT);
			entity.setBbaGlCoaSalesDiscountAccount(BBA_COA_GL_SLS_DSCNT_ACCNT);
			entity.setBbaGlCoaAdvanceAccount(BBA_COA_GL_ADVNC_ACCNT);
			entity.setBbaDownloadStatus(BBA_DWNLD_STATUS);
			entity.setBbaAdCompany(BBA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}