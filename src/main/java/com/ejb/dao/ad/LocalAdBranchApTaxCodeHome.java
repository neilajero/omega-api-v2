package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdBranchApTaxCode;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdBranchApTaxCodeHome {

	public static final String JNDI_NAME = "LocalAdBranchApTaxCodeHome!com.ejb.ad.LocalAdBranchApTaxCodeHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdBranchApTaxCodeHome() {
	}

	// FINDER METHODS

	public LocalAdBranchApTaxCode findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdBranchApTaxCode entity = (LocalAdBranchApTaxCode) em
					.find(new LocalAdBranchApTaxCode(), pk);
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

	public java.util.Collection findBtcByTcCodeAndRsName(java.lang.Integer BA_CODE, java.lang.String RS_NM,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(btc) FROM AdBranchApTaxCode btc, IN(btc.adBranch.adBranchResponsibilities) brs WHERE btc.apTaxCode.tcCode = ?1 AND brs.adResponsibility.rsName = ?2 AND btc.btcAdCompany = ?3");
			query.setParameter(1, BA_CODE);
			query.setParameter(2, RS_NM);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchApTaxCodeHome.findBtcByTcCodeAndRsName(java.lang.Integer BA_CODE, java.lang.String RS_NM, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBtcByTcCode(java.lang.Integer AP_TC_CODE, java.lang.Integer AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(btc) FROM AdBranchApTaxCode btc WHERE btc.apTaxCode.tcCode = ?1 AND btc.btcAdCompany = ?2");
			query.setParameter(1, AP_TC_CODE);
			query.setParameter(2, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchApTaxCodeHome.findBtcByTcCode(java.lang.Integer AP_TC_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBranchApTaxCode findBtcByTcCodeAndBrCode(java.lang.Integer BA_CODE, java.lang.Integer BR_CODE,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(btc) FROM AdBranchApTaxCode btc WHERE btc.apTaxCode.tcCode = ?1 AND btc.adBranch.brCode = ?2 AND btc.btcAdCompany = ?3");
			query.setParameter(1, BA_CODE);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, AD_CMPNY);
            return (LocalAdBranchApTaxCode) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdBranchApTaxCodeHome.findBtcByTcCodeAndBrCode(java.lang.Integer BA_CODE, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchApTaxCodeHome.findBtcByTcCodeAndBrCode(java.lang.Integer BA_CODE, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBBTCByTcCodeAndRsName(java.lang.Integer AP_TC_CODE, java.lang.String RS_NM,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(btc) FROM AdBranchApTaxCode btc, IN(btc.adBranch.adBranchResponsibilities) brs WHERE btc.apTaxCode.tcCode = ?1 AND brs.adResponsibility.rsName = ?2 AND btc.btcAdCompany = ?3");
			query.setParameter(1, AP_TC_CODE);
			query.setParameter(2, RS_NM);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchApTaxCodeHome.findBBTCByTcCodeAndRsName(java.lang.Integer AP_TC_CODE, java.lang.String RS_NM, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBTcByTcNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY,
			char NEW, char UPDATED, char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(btc) FROM AdBranchApTaxCode btc WHERE (btc.btcDownloadStatus = ?3 OR btc.btcDownloadStatus = ?4 OR btc.btcDownloadStatus = ?5) AND btc.adBranch.brCode = ?1 AND btc.btcAdCompany = ?2");
			query.setParameter(1, BR_CODE);
			query.setParameter(2, AD_CMPNY);
			query.setParameter(3, NEW);
			query.setParameter(4, UPDATED);
			query.setParameter(5, DOWNLOADED_UPDATED);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchApTaxCodeHome.findBTcByTcNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY, char NEW, char UPDATED, char DOWNLOADED_UPDATED)");
			throw ex;
		}
	}

	public LocalAdBranchApTaxCode findBtcByTcNameAndBrCode(java.lang.String BA_NM, java.lang.Integer BR_CODE,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(btc) FROM AdBranchApTaxCode btc WHERE btc.apTaxCode.tcName = ?1 AND btc.adBranch.brCode = ?2 AND btc.btcAdCompany = ?3");
			query.setParameter(1, BA_NM);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, AD_CMPNY);
            return (LocalAdBranchApTaxCode) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdBranchApTaxCodeHome.findBtcByTcNameAndBrCode(java.lang.String BA_NM, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchApTaxCodeHome.findBtcByTcNameAndBrCode(java.lang.String BA_NM, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalAdBranchApTaxCode create(Integer AP_BTC_CODE, Integer BTC_COA_GL_TX_CD, char BTC_DWNLD_STATUS,
                                         Integer BTC_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchApTaxCode entity = new LocalAdBranchApTaxCode();

			Debug.print("AdBranchApTaxCodeBean create");

			entity.setBtcCode(AP_BTC_CODE);
			entity.setBtcGlCoaTaxCode(BTC_COA_GL_TX_CD);
			entity.setBtcDownloadStatus(BTC_DWNLD_STATUS);
			entity.setBtcAdCompany(BTC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdBranchApTaxCode create(Integer BTC_COA_GL_TX_CD, char BTC_DWNLD_STATUS,
                                         Integer BTC_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchApTaxCode entity = new LocalAdBranchApTaxCode();

			Debug.print("AdBranchApTaxCodeBean create");

			entity.setBtcGlCoaTaxCode(BTC_COA_GL_TX_CD);
			entity.setBtcDownloadStatus(BTC_DWNLD_STATUS);
			entity.setBtcAdCompany(BTC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}