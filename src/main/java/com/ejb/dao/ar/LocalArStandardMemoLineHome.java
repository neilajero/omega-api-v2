package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArStandardMemoLine;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArStandardMemoLineHome {

	public static final String JNDI_NAME = "LocalArStandardMemoLineHome!com.ejb.ar.LocalArStandardMemoLineHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArStandardMemoLineHome() {
	}

	// FINDER METHODS

	public LocalArStandardMemoLine findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArStandardMemoLine entity = (LocalArStandardMemoLine) em
					.find(new LocalArStandardMemoLine(), pk);
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

	public java.util.Collection findEnabledSmlAll(java.lang.Integer SML_AD_BRNCH, java.lang.Integer SML_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sml) FROM ArStandardMemoLine sml, IN(sml.adBranchStandardMemoLines)bsml WHERE sml.smlEnable = 1 AND bsml.adBranch.brCode = ?1 AND sml.smlAdCompany = ?2");
			query.setParameter(1, SML_AD_BRNCH);
			query.setParameter(2, SML_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArStandardMemoLineHome.findEnabledSmlAll(java.lang.Integer SML_AD_BRNCH, java.lang.Integer SML_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArStandardMemoLine findBySmlName(java.lang.String SML_NM, java.lang.Integer SML_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sml) FROM ArStandardMemoLine sml WHERE sml.smlName = ?1 AND sml.smlAdCompany = ?2");
			query.setParameter(1, SML_NM);
			query.setParameter(2, SML_AD_CMPNY);
            return (LocalArStandardMemoLine) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArStandardMemoLineHome.findBySmlName(java.lang.String SML_NM, java.lang.Integer SML_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArStandardMemoLineHome.findBySmlName(java.lang.String SML_NM, java.lang.Integer SML_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArStandardMemoLine findBySmlName(java.lang.String SML_NM, java.lang.Integer SML_AD_CMPNY, String companyShortName)
			throws FinderException {
		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(sml) FROM ArStandardMemoLine sml WHERE sml.smlName = ?1 AND sml.smlAdCompany = ?2",
					companyShortName);
			query.setParameter(1, SML_NM);
			query.setParameter(2, SML_AD_CMPNY);
			return (LocalArStandardMemoLine) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findSmlAll(java.lang.Integer SML_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(sml) FROM ArStandardMemoLine sml WHERE sml.smlAdCompany = ?1");
			query.setParameter(1, SML_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArStandardMemoLineHome.findSmlAll(java.lang.Integer SML_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findSmlAll(java.lang.Integer SML_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany("SELECT OBJECT(sml) FROM ArStandardMemoLine sml WHERE sml.smlAdCompany = ?1", companyShortName);
			query.setParameter(1, SML_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArStandardMemoLineHome.findSmlAll(java.lang.Integer SML_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findSmlBySmlNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY,
			char NEW, char UPDATED, char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT DISTINCT OBJECT(sml) FROM ArStandardMemoLine sml, IN(sml.adBranchStandardMemoLines) bsml  WHERE (bsml.bsmlStandardMemoLineDownloadStatus = ?3 OR bsml.bsmlStandardMemoLineDownloadStatus = ?4 OR bsml.bsmlStandardMemoLineDownloadStatus = ?5) AND bsml.adBranch.brCode = ?1 AND bsml.bsmlAdCompany = ?2");
			query.setParameter(1, BR_CODE);
			query.setParameter(2, AD_CMPNY);
			query.setParameter(3, NEW);
			query.setParameter(4, UPDATED);
			query.setParameter(5, DOWNLOADED_UPDATED);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArStandardMemoLineHome.findSmlBySmlNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY, char NEW, char UPDATED, char DOWNLOADED_UPDATED)");
			throw ex;
		}
	}

	public java.util.Collection findSmlBySmlNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY,
														  char NEW, char UPDATED, char DOWNLOADED_UPDATED, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany("SELECT DISTINCT OBJECT(sml) FROM ArStandardMemoLine sml, IN(sml.adBranchStandardMemoLines) bsml "
					+ "WHERE (bsml.bsmlStandardMemoLineDownloadStatus = ?3 OR bsml.bsmlStandardMemoLineDownloadStatus = ?4 OR bsml.bsmlStandardMemoLineDownloadStatus = ?5) "
					+ "AND bsml.adBranch.brCode = ?1 AND bsml.bsmlAdCompany = ?2", companyShortName.toLowerCase());
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

	public LocalArStandardMemoLine findBySmlNameAndBrCode(java.lang.String SML_NM, java.lang.Integer BR_CODE,
			java.lang.Integer SML_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sml) FROM ArStandardMemoLine sml, IN(sml.adBranchStandardMemoLines) bsml WHERE sml.smlName = ?1 AND bsml.adBranch.brCode = ?2 AND sml.smlAdCompany = ?3");
			query.setParameter(1, SML_NM);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, SML_AD_CMPNY);
            return (LocalArStandardMemoLine) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArStandardMemoLineHome.findBySmlNameAndBrCode(java.lang.String SML_NM, java.lang.Integer BR_CODE, java.lang.Integer SML_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArStandardMemoLineHome.findBySmlNameAndBrCode(java.lang.String SML_NM, java.lang.Integer BR_CODE, java.lang.Integer SML_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getSmlByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
			throws FinderException {

		try {
			Query query = em.createQuery(jbossQl);
			int cnt = 1;
			for (Object data : args) {
				query.setParameter(cnt, data);
				cnt++;
			}
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalArStandardMemoLine create(Integer SML_CODE, String SML_TYP, String SML_NM, String SML_DESC,
                                          String SML_WP_PRDCT_ID, double SML_UNT_PRC, byte SML_TX, byte SML_ENBL, byte SML_SBJCT_TO_CMMSSN,
                                          String SML_UNT_OF_MSR, Integer SML_INTRM_ACCNT, Integer SML_GL_COA_RCVBL_ACCNT,
                                          Integer SML_GL_COA_RVNUE_ACCNT, Integer SML_AD_CMPNY) throws CreateException {
		try {

			LocalArStandardMemoLine entity = new LocalArStandardMemoLine();

			Debug.print("ArStandardMemoLineBean create");

			entity.setSmlCode(SML_CODE);
			entity.setSmlType(SML_TYP);
			entity.setSmlName(SML_NM);
			entity.setSmlDescription(SML_DESC);
			entity.setSmlWordPressProductID(SML_WP_PRDCT_ID);
			entity.setSmlUnitPrice(SML_UNT_PRC);
			entity.setSmlTax(SML_TX);
			entity.setSmlEnable(SML_ENBL);
			entity.setSmlSubjectToCommission(SML_SBJCT_TO_CMMSSN);
			entity.setSmlUnitOfMeasure(SML_UNT_OF_MSR);
			entity.setSmlInterimAccount(SML_INTRM_ACCNT);
			entity.setSmlGlCoaReceivableAccount(SML_GL_COA_RCVBL_ACCNT);
			entity.setSmlGlCoaRevenueAccount(SML_GL_COA_RVNUE_ACCNT);
			entity.setSmlAdCompany(SML_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArStandardMemoLine create(String SML_TYP, String SML_NM, String SML_DESC,
                                          String SML_WP_PRDCT_ID, double SML_UNT_PRC, byte SML_TX, byte SML_ENBL, byte SML_SBJCT_TO_CMMSSN,
                                          String SML_UNT_OF_MSR, Integer SML_INTRM_ACCNT, Integer SML_GL_COA_RCVBL_ACCNT,
                                          Integer SML_GL_COA_RVNUE_ACCNT, Integer SML_AD_CMPNY) throws CreateException {
		try {

			LocalArStandardMemoLine entity = new LocalArStandardMemoLine();

			Debug.print("ArStandardMemoLineBean create");

			entity.setSmlType(SML_TYP);
			entity.setSmlName(SML_NM);
			entity.setSmlDescription(SML_DESC);
			entity.setSmlWordPressProductID(SML_WP_PRDCT_ID);
			entity.setSmlUnitPrice(SML_UNT_PRC);
			entity.setSmlTax(SML_TX);
			entity.setSmlEnable(SML_ENBL);
			entity.setSmlSubjectToCommission(SML_SBJCT_TO_CMMSSN);
			entity.setSmlUnitOfMeasure(SML_UNT_OF_MSR);
			entity.setSmlInterimAccount(SML_INTRM_ACCNT);
			entity.setSmlGlCoaReceivableAccount(SML_GL_COA_RCVBL_ACCNT);
			entity.setSmlGlCoaRevenueAccount(SML_GL_COA_RVNUE_ACCNT);
			entity.setSmlAdCompany(SML_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}