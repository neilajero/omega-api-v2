package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdBranchDocumentSequenceAssignment;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdBranchDocumentSequenceAssignmentHome {

	public static final String JNDI_NAME = "LocalAdBranchDocumentSequenceAssignmentHome!com.ejb.ad.LocalAdBranchDocumentSequenceAssignmentHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdBranchDocumentSequenceAssignmentHome() {
	}

	// FINDER METHODS

	public LocalAdBranchDocumentSequenceAssignment findByPrimaryKey(java.lang.Integer pk)
			throws FinderException {

		try {

			LocalAdBranchDocumentSequenceAssignment entity = (LocalAdBranchDocumentSequenceAssignment) em
					.find(new LocalAdBranchDocumentSequenceAssignment(), pk);
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

	public java.util.Collection findBdsByDsaCodeAndRsName(java.lang.Integer DSA_CODE, java.lang.String RS_NM,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bds) FROM AdBranchDocumentSequenceAssignment bds, IN(bds.adBranch.adBranchResponsibilities) brs WHERE bds.adDocumentSequenceAssignment.dsaCode = ?1 AND brs.adResponsibility.rsName = ?2 AND bds.bdsAdCompany = ?3");
			query.setParameter(1, DSA_CODE);
			query.setParameter(2, RS_NM);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndRsName(java.lang.Integer DSA_CODE, java.lang.String RS_NM, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBranchDocumentSequenceAssignment findBdsByDsaCodeAndBrCode(java.lang.Integer DSA_CODE,
			java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bds) FROM AdBranchDocumentSequenceAssignment bds WHERE bds.adDocumentSequenceAssignment.dsaCode = ?1 AND bds.adBranch.brCode = ?2 AND bds.bdsAdCompany = ?3");
			query.setParameter(1, DSA_CODE);
			query.setParameter(2, AD_BRNCH);
			query.setParameter(3, AD_CMPNY);
            return (LocalAdBranchDocumentSequenceAssignment) query
                    .getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(java.lang.Integer DSA_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(java.lang.Integer DSA_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBranchDocumentSequenceAssignment findBdsByDsaCodeAndBrCode(java.lang.Integer DSA_CODE, Integer branchCode, Integer companyCode, String companyShortName) throws FinderException {
		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(bds) FROM AdBranchDocumentSequenceAssignment bds "
							+ "WHERE bds.adDocumentSequenceAssignment.dsaCode = ?1 "
							+ "AND bds.adBranch.brCode = ?2 AND bds.bdsAdCompany = ?3", companyShortName);
			query.setParameter(1, DSA_CODE);
			query.setParameter(2, branchCode);
			query.setParameter(3, companyCode);
			return (LocalAdBranchDocumentSequenceAssignment) query
					.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findBdsByDsaCode(java.lang.Integer DSA_CODE, java.lang.Integer AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bds) FROM AdBranchDocumentSequenceAssignment bds WHERE bds.adDocumentSequenceAssignment.dsaCode = ?1 AND bds.bdsAdCompany = ?2");
			query.setParameter(1, DSA_CODE);
			query.setParameter(2, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchDocumentSequenceAssignmentHome.findBdsByDsaCode(java.lang.Integer DSA_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalAdBranchDocumentSequenceAssignment create(Integer BDS_CODE, String BDS_NXT_SQNC,
                                                          Integer BDS_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchDocumentSequenceAssignment entity = new LocalAdBranchDocumentSequenceAssignment();

			Debug.print("AdBranchDocumentSequenceAssignmentBean create");

			entity.setBdsCode(BDS_CODE);
			entity.setBdsNextSequence(BDS_NXT_SQNC);
			entity.setBdsAdCompany(BDS_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdBranchDocumentSequenceAssignment create(String BDS_NXT_SQNC, Integer BDS_AD_CMPNY)
			throws CreateException {
		try {

			LocalAdBranchDocumentSequenceAssignment entity = new LocalAdBranchDocumentSequenceAssignment();

			Debug.print("AdBranchDocumentSequenceAssignmentBean create");

			entity.setBdsNextSequence(BDS_NXT_SQNC);
			entity.setBdsAdCompany(BDS_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}