package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdBranchCoa;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdBranchCoaHome {

	public static final String JNDI_NAME = "LocalAdBranchCoaHome!com.ejb.ad.LocalAdBranchCoaHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdBranchCoaHome() {
	}

	// FINDER METHODS

	public LocalAdBranchCoa findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdBranchCoa entity = (LocalAdBranchCoa) em
					.find(new LocalAdBranchCoa(), pk);
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

	public java.util.Collection findBcoaByCoaCodeAndRsName(java.lang.Integer COA_CODE, java.lang.String RS_NM,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bcoa) FROM AdBranchCoa bcoa, IN(bcoa.adBranch.adBranchResponsibilities) brs WHERE bcoa.glChartOfAccount.coaCode = ?1 AND brs.adResponsibility.rsName = ?2 AND bcoa.bcoaAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, RS_NM);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchCoaHome.findBcoaByCoaCodeAndRsName(java.lang.Integer COA_CODE, java.lang.String RS_NM, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBcoaByCoaCode(java.lang.Integer COA_CODE, java.lang.Integer AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bcoa) FROM AdBranchCoa bcoa WHERE bcoa.glChartOfAccount.coaCode = ?1 AND bcoa.bcoaAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchCoaHome.findBcoaByCoaCode(java.lang.Integer COA_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalAdBranchCoa create(Integer BCOA_CODE, Integer BCOA_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchCoa entity = new LocalAdBranchCoa();

			Debug.print("AdBranchDocumentSequenceAssignmentBean create");

			entity.setBcoaCode(BCOA_CODE);
			entity.setBcoaAdCompany(BCOA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdBranchCoa create(Integer BCOA_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchCoa entity = new LocalAdBranchCoa();

			Debug.print("AdBranchDocumentSequenceAssignmentBean create");
			entity.setBcoaAdCompany(BCOA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}