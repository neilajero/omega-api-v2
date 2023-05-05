package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdBranchSupplier;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdBranchSupplierHome {

	public static final String JNDI_NAME = "LocalAdBranchSupplierHome!com.ejb.ad.LocalAdBranchSupplierHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdBranchSupplierHome() {
	}

	// FINDER METHODS

	public LocalAdBranchSupplier findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdBranchSupplier entity = (LocalAdBranchSupplier) em
					.find(new LocalAdBranchSupplier(), pk);
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

	public java.util.Collection findBSplAll(java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(bspl) FROM AdBranchSupplier bspl WHERE bspl.bsplAdCompany = ?1");
			query.setParameter(1, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchSupplierHome.findBSplAll(java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBSplBySplCodeAndRsName(java.lang.Integer SPL_CODE, java.lang.String RS_NM,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bspl) FROM AdBranchSupplier bspl, IN(bspl.adBranch.adBranchResponsibilities) brs WHERE bspl.apSupplier.splCode = ?1 AND brs.adResponsibility.rsName = ?2 AND bspl.bsplAdCompany = ?3");
			query.setParameter(1, SPL_CODE);
			query.setParameter(2, RS_NM);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchSupplierHome.findBSplBySplCodeAndRsName(java.lang.Integer SPL_CODE, java.lang.String RS_NM, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBSplBySpl(java.lang.Integer SPL_CODE, java.lang.Integer AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bspl) FROM AdBranchSupplier bspl WHERE bspl.apSupplier.splCode = ?1 AND bspl.bsplAdCompany = ?2");
			query.setParameter(1, SPL_CODE);
			query.setParameter(2, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchSupplierHome.findBSplBySpl(java.lang.Integer SPL_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBranchSupplier findBSplBySplCodeAndBrCode(java.lang.Integer SPL_CODE, java.lang.Integer AD_BRNCH,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bspl) FROM AdBranchSupplier bspl WHERE bspl.apSupplier.splCode = ?1 AND bspl.adBranch.brCode = ?2 AND bspl.bsplAdCompany = ?3");
			query.setParameter(1, SPL_CODE);
			query.setParameter(2, AD_BRNCH);
			query.setParameter(3, AD_CMPNY);
            return (LocalAdBranchSupplier) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdBranchSupplierHome.findBSplBySplCodeAndBrCode(java.lang.Integer SPL_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchSupplierHome.findBSplBySplCodeAndBrCode(java.lang.Integer SPL_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBsplGlCoaPayableAccount(java.lang.Integer SPL_CODE,
			java.lang.Integer BSPL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bspl) FROM AdBranchSupplier bspl WHERE bspl.bsplGlCoaPayableAccount=?1 AND bspl.bsplAdCompany = ?2");
			query.setParameter(1, SPL_CODE);
			query.setParameter(2, BSPL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchSupplierHome.findByBsplGlCoaPayableAccount(java.lang.Integer SPL_CODE, java.lang.Integer BSPL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBsplGlCoaExpenseAccount(java.lang.Integer SPL_CODE,
			java.lang.Integer BSPL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bspl) FROM AdBranchSupplier bspl WHERE bspl.bsplGlCoaExpenseAccount=?1 AND bspl.bsplAdCompany = ?2");
			query.setParameter(1, SPL_CODE);
			query.setParameter(2, BSPL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchSupplierHome.findByBsplGlCoaExpenseAccount(java.lang.Integer SPL_CODE, java.lang.Integer BSPL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findSplBySplNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY,
			char NEW, char UPDATED, char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bspl) FROM AdBranchSupplier bspl WHERE (bspl.bsplSupplierDownloadStatus = ?3 OR bspl.bsplSupplierDownloadStatus = ?4 OR bspl.bsplSupplierDownloadStatus = ?5) AND bspl.adBranch.brCode = ?1 AND bspl.bsplAdCompany = ?2");
			query.setParameter(1, BR_CODE);
			query.setParameter(2, AD_CMPNY);
			query.setParameter(3, NEW);
			query.setParameter(4, UPDATED);
			query.setParameter(5, DOWNLOADED_UPDATED);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchSupplierHome.findSplBySplNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY, char NEW, char UPDATED, char DOWNLOADED_UPDATED)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalAdBranchSupplier create(Integer BSPL_CODE, Integer BSPL_GL_COA_PYBL_ACCNT,
                                        Integer BSPL_GL_COA_EXPNS_ACCNT, char BSPL_DWNLD_STATUS, Integer BSPL_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchSupplier entity = new LocalAdBranchSupplier();

			Debug.print("AdBranchSupplierBean create");
			entity.setBsplCode(BSPL_CODE);
			entity.setBsplGlCoaPayableAccount(BSPL_GL_COA_PYBL_ACCNT);
			entity.setBsplGlCoaExpenseAccount(BSPL_GL_COA_EXPNS_ACCNT);
			entity.setBsplSupplierDownloadStatus(BSPL_DWNLD_STATUS);
			entity.setBsplAdCompany(BSPL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdBranchSupplier create(Integer BSPL_GL_COA_PYBL_ACCNT, Integer BSPL_GL_COA_EXPNS_ACCNT,
                                        char BSPL_DWNLD_STATUS, Integer BSPL_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchSupplier entity = new LocalAdBranchSupplier();

			Debug.print("AdBranchSupplierBean create");
			entity.setBsplGlCoaPayableAccount(BSPL_GL_COA_PYBL_ACCNT);
			entity.setBsplGlCoaExpenseAccount(BSPL_GL_COA_EXPNS_ACCNT);
			entity.setBsplSupplierDownloadStatus(BSPL_DWNLD_STATUS);
			entity.setBsplAdCompany(BSPL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}