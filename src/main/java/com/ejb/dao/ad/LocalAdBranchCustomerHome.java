package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdBranchCustomer;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@SuppressWarnings("ALL")
@Stateless
public class LocalAdBranchCustomerHome {

	public static final String JNDI_NAME = "LocalAdBranchCustomerHome!com.ejb.ad.LocalAdBranchCustomerHome";

	@EJB
	public PersistenceBeanClass em;

	private Integer BCST_GL_COA_RCVBL_ACCNT = null;
	private Integer BCST_GL_COA_RVN_ACCNT = null;
	private Integer BCST_GL_COA_UNERND_INT_ACCNT = null;
	private Integer BCST_GL_COA_ERND_INT_ACCNT = null;
	private Integer BCST_GL_COA_UNERND_PNT_ACCNT = null;
	private Integer BCST_GL_COA_ERND_PNT_ACCNT = null;
	private char BCST_DS_CSTMR = 'N';
	private Integer BCST_AD_CMPNY = null;

	public LocalAdBranchCustomerHome() {
	}

	// FINDER METHODS

	public LocalAdBranchCustomer findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdBranchCustomer entity = (LocalAdBranchCustomer) em
					.find(new LocalAdBranchCustomer(), pk);
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

	public java.util.Collection findBcstByCstCode(java.lang.Integer AR_CST_CODE, java.lang.Integer AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bcst) FROM AdBranchCustomer bcst WHERE bcst.arCustomer.cstCode = ?1 AND bcst.bcstAdCompany = ?2");
			query.setParameter(1, AR_CST_CODE);
			query.setParameter(2, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchCustomerHome.findBcstByCstCode(java.lang.Integer AR_CST_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBcstByCstCodeAndRsName(java.lang.Integer AR_CST_CODE, java.lang.String RS_NM,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bcst) FROM AdBranchCustomer bcst, IN(bcst.adBranch.adBranchResponsibilities) brs WHERE bcst.arCustomer.cstCode = ?1 AND brs.adResponsibility.rsName = ?2 AND bcst.bcstAdCompany = ?3");
			query.setParameter(1, AR_CST_CODE);
			query.setParameter(2, RS_NM);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchCustomerHome.findBcstByCstCodeAndRsName(java.lang.Integer AR_CST_CODE, java.lang.String RS_NM, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBranchCustomer findBcstByCstCodeAndBrCode(java.lang.Integer AR_CST_CODE, java.lang.Integer BR_CODE,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bcst) FROM AdBranchCustomer bcst WHERE bcst.arCustomer.cstCode = ?1 AND bcst.adBranch.brCode = ?2 AND bcst.bcstAdCompany = ?3");
			query.setParameter(1, AR_CST_CODE);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, AD_CMPNY);
            return (LocalAdBranchCustomer) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdBranchCustomerHome.findBcstByCstCodeAndBrCode(java.lang.Integer AR_CST_CODE, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchCustomerHome.findBcstByCstCodeAndBrCode(java.lang.Integer AR_CST_CODE, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBranchCustomer findBcstByCstCodeAndBrCode(java.lang.Integer AR_CST_CODE, java.lang.Integer BR_CODE,
															java.lang.Integer AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(bcst) FROM AdBranchCustomer bcst WHERE bcst.arCustomer.cstCode = ?1 AND bcst.adBranch.brCode = ?2 AND bcst.bcstAdCompany = ?3",
					companyShortName);
			query.setParameter(1, AR_CST_CODE);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, AD_CMPNY);
			return (LocalAdBranchCustomer) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findCstByCstNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY,
			char NEW, char UPDATED, char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bcst) FROM AdBranchCustomer bcst WHERE (bcst.bcstCustomerDownloadStatus = ?3 OR bcst.bcstCustomerDownloadStatus = ?4 OR bcst.bcstCustomerDownloadStatus = ?5) AND bcst.adBranch.brCode = ?1 AND bcst.bcstAdCompany = ?2");
			query.setParameter(1, BR_CODE);
			query.setParameter(2, AD_CMPNY);
			query.setParameter(3, NEW);
			query.setParameter(4, UPDATED);
			query.setParameter(5, DOWNLOADED_UPDATED);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchCustomerHome.findCstByCstNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY, char NEW, char UPDATED, char DOWNLOADED_UPDATED)");
			throw ex;
		}
	}

	public java.util.Collection findByBcstGlCoaReceivableAccount(java.lang.Integer BR_CODE,
			java.lang.Integer BCST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bcst) FROM AdBranchCustomer bcst WHERE bcst.bcstGlCoaReceivableAccount=?1 AND bcst.bcstAdCompany = ?2");
			query.setParameter(1, BR_CODE);
			query.setParameter(2, BCST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchCustomerHome.findByBcstGlCoaReceivableAccount(java.lang.Integer BR_CODE, java.lang.Integer BCST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBcstGlCoaRevenueAccount(java.lang.Integer BR_CODE,
			java.lang.Integer BCST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bcst) FROM AdBranchCustomer bcst WHERE bcst.bcstGlCoaRevenueAccount=?1 AND bcst.bcstAdCompany = ?2");
			query.setParameter(1, BR_CODE);
			query.setParameter(2, BCST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchCustomerHome.findByBcstGlCoaRevenueAccount(java.lang.Integer BR_CODE, java.lang.Integer BCST_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBranchCustomer buildBranchCustomer(String companyShortName) throws CreateException {
		try {

			LocalAdBranchCustomer entity = new LocalAdBranchCustomer();

			Debug.print("AdBranchCustomerBean buildBranchCustomer");

			entity.setBcstGlCoaReceivableAccount(BCST_GL_COA_RCVBL_ACCNT);
			entity.setBcstGlCoaRevenueAccount(BCST_GL_COA_RVN_ACCNT);
			entity.setBcstGlCoaUnEarnedInterestAccount(BCST_GL_COA_UNERND_INT_ACCNT);
			entity.setBcstGlCoaEarnedInterestAccount(BCST_GL_COA_ERND_INT_ACCNT);
			entity.setBcstGlCoaUnEarnedPenaltyAccount(BCST_GL_COA_UNERND_PNT_ACCNT);
			entity.setBcstGlCoaEarnedPenaltyAccount(BCST_GL_COA_ERND_PNT_ACCNT);
			entity.setBcstCustomerDownloadStatus(BCST_DS_CSTMR);
			entity.setBcstAdCompany(BCST_AD_CMPNY);

			em.persist(entity, companyShortName);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdBranchCustomer buildBranchCustomer() throws CreateException {
		try {

			LocalAdBranchCustomer entity = new LocalAdBranchCustomer();

			Debug.print("AdBranchCustomerBean buildBranchCustomer");

			entity.setBcstGlCoaReceivableAccount(BCST_GL_COA_RCVBL_ACCNT);
			entity.setBcstGlCoaRevenueAccount(BCST_GL_COA_RVN_ACCNT);
			entity.setBcstGlCoaUnEarnedInterestAccount(BCST_GL_COA_UNERND_INT_ACCNT);
			entity.setBcstGlCoaEarnedInterestAccount(BCST_GL_COA_ERND_INT_ACCNT);
			entity.setBcstGlCoaUnEarnedPenaltyAccount(BCST_GL_COA_UNERND_PNT_ACCNT);
			entity.setBcstGlCoaEarnedPenaltyAccount(BCST_GL_COA_ERND_PNT_ACCNT);
			entity.setBcstCustomerDownloadStatus(BCST_DS_CSTMR);
			entity.setBcstAdCompany(BCST_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}
	
	public LocalAdBranchCustomerHome BcstGlCoaReceivableAccount(Integer BCST_GL_COA_RCVBL_ACCNT) {
		this.BCST_GL_COA_RCVBL_ACCNT = BCST_GL_COA_RCVBL_ACCNT;
		return this;
	}
	
	public LocalAdBranchCustomerHome BcstGlCoaRevenueAccount(Integer BCST_GL_COA_RVN_ACCNT) {
		this.BCST_GL_COA_RVN_ACCNT = BCST_GL_COA_RVN_ACCNT;
		return this;
	}
	
	public LocalAdBranchCustomerHome BcstGlCoaUnEarnedInterestAccount(Integer BCST_GL_COA_UNERND_INT_ACCNT) {
		this.BCST_GL_COA_UNERND_INT_ACCNT = BCST_GL_COA_UNERND_INT_ACCNT;
		return this;
	}
	
	public LocalAdBranchCustomerHome BcstGlCoaEarnedInterestAccount(Integer BCST_GL_COA_ERND_INT_ACCNT) {
		this.BCST_GL_COA_ERND_INT_ACCNT = BCST_GL_COA_ERND_INT_ACCNT;
		return this;
	}
	
	public LocalAdBranchCustomerHome BcstGlCoaUnEarnedPenaltyAccount(Integer BCST_GL_COA_UNERND_PNT_ACCNT) {
		this.BCST_GL_COA_UNERND_PNT_ACCNT = BCST_GL_COA_UNERND_PNT_ACCNT;
		return this;
	}
	
	public LocalAdBranchCustomerHome BcstGlCoaEarnedPenaltyAccount(Integer BCST_GL_COA_ERND_PNT_ACCNT) {
		this.BCST_GL_COA_ERND_PNT_ACCNT = BCST_GL_COA_ERND_PNT_ACCNT;
		return this;
	}
	
	public LocalAdBranchCustomerHome BcstCustomerDownloadStatus(char BCST_DS_CSTMR) {
		this.BCST_DS_CSTMR = BCST_DS_CSTMR;
		return this;
	}

	public LocalAdBranchCustomerHome BcstAdCompany(Integer BCST_AD_CMPNY) {
		this.BCST_AD_CMPNY = BCST_AD_CMPNY;
		return this;
	}

	public LocalAdBranchCustomer create(Integer BCST_CODE, Integer BCST_GL_COA_RCVBL_ACCNT,
                                        Integer BCST_GL_COA_RVN_ACCNT, Integer BCST_GL_COA_UNERND_INT_ACCNT, Integer BCST_GL_COA_ERND_INT_ACCNT,
                                        Integer BCST_GL_COA_UNERND_PNT_ACCNT, Integer BCST_GL_COA_ERND_PNT_ACCNT, char BCST_DS_CSTMR,
                                        Integer BCST_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchCustomer entity = new LocalAdBranchCustomer();

			Debug.print("AdBranchCustomerBean create");
			entity.setBcstCode(BCST_CODE);
			entity.setBcstGlCoaReceivableAccount(BCST_GL_COA_RCVBL_ACCNT);
			entity.setBcstGlCoaRevenueAccount(BCST_GL_COA_RVN_ACCNT);
			entity.setBcstGlCoaUnEarnedInterestAccount(BCST_GL_COA_UNERND_INT_ACCNT);
			entity.setBcstGlCoaEarnedInterestAccount(BCST_GL_COA_ERND_INT_ACCNT);
			entity.setBcstGlCoaUnEarnedPenaltyAccount(BCST_GL_COA_UNERND_PNT_ACCNT);
			entity.setBcstGlCoaEarnedPenaltyAccount(BCST_GL_COA_ERND_PNT_ACCNT);
			entity.setBcstCustomerDownloadStatus(BCST_DS_CSTMR);
			entity.setBcstAdCompany(BCST_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdBranchCustomer create(Integer BCST_GL_COA_RCVBL_ACCNT, Integer BCST_GL_COA_RVN_ACCNT,
                                        Integer BCST_GL_COA_UNERND_INT_ACCNT, Integer BCST_GL_COA_ERND_INT_ACCNT,
                                        Integer BCST_GL_COA_UNERND_PNT_ACCNT, Integer BCST_GL_COA_ERND_PNT_ACCNT, char BCST_DS_CSTMR,
                                        Integer BCST_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchCustomer entity = new LocalAdBranchCustomer();

			Debug.print("AdBranchCustomerBean create");
			entity.setBcstGlCoaReceivableAccount(BCST_GL_COA_RCVBL_ACCNT);
			entity.setBcstGlCoaRevenueAccount(BCST_GL_COA_RVN_ACCNT);
			entity.setBcstGlCoaUnEarnedInterestAccount(BCST_GL_COA_UNERND_INT_ACCNT);
			entity.setBcstGlCoaEarnedInterestAccount(BCST_GL_COA_ERND_INT_ACCNT);
			entity.setBcstGlCoaUnEarnedPenaltyAccount(BCST_GL_COA_UNERND_PNT_ACCNT);
			entity.setBcstGlCoaEarnedPenaltyAccount(BCST_GL_COA_ERND_PNT_ACCNT);

			entity.setBcstCustomerDownloadStatus(BCST_DS_CSTMR);
			entity.setBcstAdCompany(BCST_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}