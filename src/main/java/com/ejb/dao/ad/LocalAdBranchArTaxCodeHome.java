package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdBranchArTaxCode;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdBranchArTaxCodeHome {

	public static final String JNDI_NAME = "LocalAdBranchArTaxCodeHome!com.ejb.ad.LocalAdBranchArTaxCodeHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdBranchArTaxCodeHome() {
	}

	// FINDER METHODS

	public LocalAdBranchArTaxCode findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdBranchArTaxCode entity = (LocalAdBranchArTaxCode) em
					.find(new LocalAdBranchArTaxCode(), pk);
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
					"SELECT OBJECT(btc) FROM AdBranchArTaxCode btc, IN(btc.adBranch.adBranchResponsibilities) brs WHERE btc.arTaxCode.tcCode = ?1 AND brs.adResponsibility.rsName = ?2 AND btc.btcAdCompany = ?3");
			query.setParameter(1, BA_CODE);
			query.setParameter(2, RS_NM);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchArTaxCodeHome.findBtcByTcCodeAndRsName(java.lang.Integer BA_CODE, java.lang.String RS_NM, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBtcByTcCode(java.lang.Integer AR_TC_CODE, java.lang.Integer AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(btc) FROM AdBranchArTaxCode btc WHERE btc.arTaxCode.tcCode = ?1 AND btc.btcAdCompany = ?2");
			query.setParameter(1, AR_TC_CODE);
			query.setParameter(2, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchArTaxCodeHome.findBtcByTcCode(java.lang.Integer AR_TC_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBranchArTaxCode findBtcByTcCodeAndBrCode(java.lang.Integer BA_CODE, java.lang.Integer BR_CODE,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(btc) FROM AdBranchArTaxCode btc WHERE btc.arTaxCode.tcCode = ?1 AND btc.adBranch.brCode = ?2 AND btc.btcAdCompany = ?3");
			query.setParameter(1, BA_CODE);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, AD_CMPNY);
            return (LocalAdBranchArTaxCode) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(java.lang.Integer BA_CODE, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(java.lang.Integer BA_CODE, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBranchArTaxCode findBtcByTcCodeAndBrCode(java.lang.Integer BA_CODE, java.lang.Integer BR_CODE,
														   java.lang.Integer AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(btc) FROM AdBranchArTaxCode btc WHERE btc.arTaxCode.tcCode = ?1 AND btc.adBranch.brCode = ?2 AND btc.btcAdCompany = ?3",
					companyShortName);
			query.setParameter(1, BA_CODE);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, AD_CMPNY);
			return (LocalAdBranchArTaxCode) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(java.lang.Integer BA_CODE, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchArTaxCodeHome.findBtcByTcCodeAndBrCode(java.lang.Integer BA_CODE, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBBTCByTcCodeAndRsName(java.lang.Integer AR_TC_CODE, java.lang.String RS_NM,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(btc) FROM AdBranchArTaxCode btc, IN(btc.adBranch.adBranchResponsibilities) brs WHERE btc.arTaxCode.tcCode = ?1 AND brs.adResponsibility.rsName = ?2 AND btc.btcAdCompany = ?3");
			query.setParameter(1, AR_TC_CODE);
			query.setParameter(2, RS_NM);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchArTaxCodeHome.findBBTCByTcCodeAndRsName(java.lang.Integer AR_TC_CODE, java.lang.String RS_NM, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBTcByTcNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY,
			char NEW, char UPDATED, char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(btc) FROM AdBranchArTaxCode btc WHERE (btc.btcDownloadStatus = ?3 OR btc.btcDownloadStatus = ?4 OR btc.btcDownloadStatus = ?5) AND btc.adBranch.brCode = ?1 AND btc.btcAdCompany = ?2");
			query.setParameter(1, BR_CODE);
			query.setParameter(2, AD_CMPNY);
			query.setParameter(3, NEW);
			query.setParameter(4, UPDATED);
			query.setParameter(5, DOWNLOADED_UPDATED);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchArTaxCodeHome.findBTcByTcNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY, char NEW, char UPDATED, char DOWNLOADED_UPDATED)");
			throw ex;
		}
	}

	public LocalAdBranchArTaxCode findBtcByTcNameAndBrCode(java.lang.String BA_NM, java.lang.Integer BR_CODE,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(btc) FROM AdBranchArTaxCode btc WHERE btc.arTaxCode.tcName = ?1 AND btc.adBranch.brCode = ?2 AND btc.btcAdCompany = ?3");
			query.setParameter(1, BA_NM);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, AD_CMPNY);
            return (LocalAdBranchArTaxCode) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdBranchArTaxCodeHome.findBtcByTcNameAndBrCode(java.lang.String BA_NM, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchArTaxCodeHome.findBtcByTcNameAndBrCode(java.lang.String BA_NM, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalAdBranchArTaxCode create(Integer AR_BTC_CODE, Integer BTC_COA_GL_TX_CD,
                                         Integer BTC_COA_GL_INTRM_CD, char BTC_DWNLD_STATUS, Integer BTC_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchArTaxCode entity = new LocalAdBranchArTaxCode();

			Debug.print("AdBranchArTaxCodeBean create");

			entity.setBtcCode(AR_BTC_CODE);
			entity.setBtcGlCoaTaxCode(BTC_COA_GL_TX_CD);
			entity.setBtcGlCoaInterimCode(BTC_COA_GL_INTRM_CD);
			entity.setBtcDownloadStatus(BTC_DWNLD_STATUS);
			entity.setBtcAdCompany(BTC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdBranchArTaxCode create(Integer BTC_COA_GL_TX_CD, Integer BTC_COA_GL_INTRM_CD,
                                         char BTC_DWNLD_STATUS, Integer BTC_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchArTaxCode entity = new LocalAdBranchArTaxCode();

			Debug.print("AdBranchArTaxCodeBean create");

			entity.setBtcGlCoaTaxCode(BTC_COA_GL_TX_CD);
			entity.setBtcGlCoaInterimCode(BTC_COA_GL_INTRM_CD);
			entity.setBtcDownloadStatus(BTC_DWNLD_STATUS);
			entity.setBtcAdCompany(BTC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}