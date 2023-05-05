package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdBranchStandardMemoLine;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdBranchStandardMemoLineHome {

	public static final String JNDI_NAME = "LocalAdBranchStandardMemoLineHome!com.ejb.ad.LocalAdBranchStandardMemoLineHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdBranchStandardMemoLineHome() {
	}

	// FINDER METHODS

	public LocalAdBranchStandardMemoLine findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdBranchStandardMemoLine entity = (LocalAdBranchStandardMemoLine) em
					.find(new LocalAdBranchStandardMemoLine(), pk);
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

	public java.util.Collection findBSMLByBSMLNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY,
			char NEW, char UPDATED, char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT  OBJECT(bsml) FROM AdBranchStandardMemoLine bsml WHERE (bsml.bsmlStandardMemoLineDownloadStatus = ?3 OR bsml.bsmlStandardMemoLineDownloadStatus = ?4 OR bsml.bsmlStandardMemoLineDownloadStatus = ?5) AND bsml.adBranch.brCode = ?1 AND bsml.bsmlAdCompany = ?2");
			query.setParameter(1, BR_CODE);
			query.setParameter(2, AD_CMPNY);
			query.setParameter(3, NEW);
			query.setParameter(4, UPDATED);
			query.setParameter(5, DOWNLOADED_UPDATED);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchStandardMemoLineHome.findBSMLByBSMLNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY, char NEW, char UPDATED, char DOWNLOADED_UPDATED)");
			throw ex;
		}
	}

	public java.util.Collection findBSMLAll(java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bsml) FROM AdBranchStandardMemoLine bsml WHERE bsml.bsmlAdCompany = ?1");
			query.setParameter(1, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchStandardMemoLineHome.findBSMLAll(java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBSMLBySML(java.lang.Integer SML_CODE, java.lang.Integer AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bsml) FROM AdBranchStandardMemoLine bsml WHERE bsml.arStandardMemoLine.smlCode = ?1 AND bsml.bsmlAdCompany = ?2");
			query.setParameter(1, SML_CODE);
			query.setParameter(2, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchStandardMemoLineHome.findBSMLBySML(java.lang.Integer SML_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBSMLBySMLCodeAndRsName(java.lang.Integer SML_CODE, java.lang.String RS_NM,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bsml) FROM AdBranchStandardMemoLine bsml, IN(bsml.adBranch.adBranchResponsibilities) brs WHERE bsml.arStandardMemoLine.smlCode = ?1 AND brs.adResponsibility.rsName = ?2 AND bsml.bsmlAdCompany = ?3");
			query.setParameter(1, SML_CODE);
			query.setParameter(2, RS_NM);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchStandardMemoLineHome.findBSMLBySMLCodeAndRsName(java.lang.Integer SML_CODE, java.lang.String RS_NM, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBranchStandardMemoLine findBSMLBySMLCodeAndBrCode(java.lang.Integer SML_CODE,
			java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bsml) FROM AdBranchStandardMemoLine bsml WHERE bsml.arStandardMemoLine.smlCode = ?1 AND bsml.adBranch.brCode = ?2 AND bsml.bsmlAdCompany = ?3");
			query.setParameter(1, SML_CODE);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, AD_CMPNY);
            return (LocalAdBranchStandardMemoLine) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdBranchStandardMemoLineHome.findBSMLBySMLCodeAndBrCode(java.lang.Integer SML_CODE, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchStandardMemoLineHome.findBSMLBySMLCodeAndBrCode(java.lang.Integer SML_CODE, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBranchStandardMemoLine findBSMLBySMLCodeAndBrCode(java.lang.Integer SML_CODE,
																	java.lang.Integer BR_CODE,
																	java.lang.Integer AD_CMPNY,
																	String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(bsml) FROM AdBranchStandardMemoLine bsml "
							+ "WHERE bsml.arStandardMemoLine.smlCode = ?1 "
							+ "AND bsml.adBranch.brCode = ?2 AND bsml.bsmlAdCompany = ?3",
					companyShortName);
			query.setParameter(1, SML_CODE);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, AD_CMPNY);
			return (LocalAdBranchStandardMemoLine) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByBSMLGlAccount(java.lang.Integer SML_CODE, java.lang.Integer SML_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bsml) FROM AdBranchStandardMemoLine bsml WHERE bsml.bsmlGlAccount=?1 AND bsml.bsmlAdCompany = ?2");
			query.setParameter(1, SML_CODE);
			query.setParameter(2, SML_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchStandardMemoLineHome.findByBSMLGlAccount(java.lang.Integer SML_CODE, java.lang.Integer SML_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalAdBranchStandardMemoLine create(Integer BSML_CODE, Integer BSML_GL_ACCNT,
                                                Integer BSML_GL_COA_RCVBL_ACCNT, Integer BSML_GL_COA_RVNUE_ACCNT, byte BSML_SBJCT_TO_CMMSSN,
                                                char BSML_DWNLD_STATUS, Integer BSML_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchStandardMemoLine entity = new LocalAdBranchStandardMemoLine();

			Debug.print("AdBranchStandardMemoLineBean create");
			entity.setBsmlCode(BSML_CODE);
			entity.setBsmlGlAccount(BSML_GL_ACCNT);
			entity.setBsmlGlCoaReceivableAccount(BSML_GL_COA_RCVBL_ACCNT);
			entity.setBsmlGlCoaRevenueAccount(BSML_GL_COA_RVNUE_ACCNT);
			entity.setBsmlSubjectToCommission(BSML_SBJCT_TO_CMMSSN);
			entity.setBsmlStandardMemoLineDownloadStatus(BSML_DWNLD_STATUS);
			entity.setBsmlAdCompany(BSML_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdBranchStandardMemoLine create(Integer BSML_GL_ACCNT, Integer BSML_GL_COA_RCVBL_ACCNT,
                                                Integer BSML_GL_COA_RVNUE_ACCNT, byte BSML_SBJCT_TO_CMMSSN, char BSML_DWNLD_STATUS, Integer BSML_AD_CMPNY)
			throws CreateException {
		try {

			LocalAdBranchStandardMemoLine entity = new LocalAdBranchStandardMemoLine();

			Debug.print("AdBranchStandardMemoLineBean create");
			entity.setBsmlGlAccount(BSML_GL_ACCNT);
			entity.setBsmlGlCoaReceivableAccount(BSML_GL_COA_RCVBL_ACCNT);
			entity.setBsmlGlCoaRevenueAccount(BSML_GL_COA_RVNUE_ACCNT);
			entity.setBsmlSubjectToCommission(BSML_SBJCT_TO_CMMSSN);
			entity.setBsmlStandardMemoLineDownloadStatus(BSML_DWNLD_STATUS);
			entity.setBsmlAdCompany(BSML_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}

	}

}