package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdApprovalCoaLine;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdApprovalCoaLineHome {

	public static final String JNDI_NAME = "LocalAdApprovalCoaLineHome!com.ejb.ad.LocalAdApprovalCoaLineHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdApprovalCoaLineHome() {
	}

	// FINDER METHODS

	public LocalAdApprovalCoaLine findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdApprovalCoaLine entity = (LocalAdApprovalCoaLine) em
					.find(new LocalAdApprovalCoaLine(), pk);
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

	public LocalAdApprovalCoaLine findByPrimaryKey(java.lang.Integer pk, String companyShortName) throws FinderException {

		try {

			LocalAdApprovalCoaLine entity = (LocalAdApprovalCoaLine) em
					.findPerCompany(new LocalAdApprovalCoaLine(), pk, companyShortName);
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

	public java.util.Collection findAclAll(java.lang.Integer ACL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(acl) FROM AdApprovalCoaLine acl WHERE acl.aclAdCompany = ?1");
			query.setParameter(1, ACL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdApprovalCoaLineHome.findAclAll(java.lang.Integer ACL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdApprovalCoaLine findByAclCodeAndCoaAccountNumber(java.lang.Integer COA_CODE,
			java.lang.String COA_ACCNT_NMBR, java.lang.Integer ACL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(acl) FROM AdApprovalCoaLine acl WHERE acl.aclCode<>?1 AND acl.glChartOfAccount.coaAccountNumber=?2 AND acl.aclAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, COA_ACCNT_NMBR);
			query.setParameter(3, ACL_AD_CMPNY);
            return (LocalAdApprovalCoaLine) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdApprovalCoaLineHome.findByAclCodeAndCoaAccountNumber(java.lang.Integer COA_CODE, java.lang.String COA_ACCNT_NMBR, java.lang.Integer ACL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdApprovalCoaLineHome.findByAclCodeAndCoaAccountNumber(java.lang.Integer COA_CODE, java.lang.String COA_ACCNT_NMBR, java.lang.Integer ACL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdApprovalCoaLine findByCoaAccountNumber(java.lang.String COA_ACCNT_NMBR,
			java.lang.Integer ACL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(acl) FROM AdApprovalCoaLine acl WHERE acl.glChartOfAccount.coaAccountNumber=?1 AND acl.aclAdCompany = ?2");
			query.setParameter(1, COA_ACCNT_NMBR);
			query.setParameter(2, ACL_AD_CMPNY);
            return (LocalAdApprovalCoaLine) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdApprovalCoaLineHome.findByCoaAccountNumber(java.lang.String COA_ACCNT_NMBR, java.lang.Integer ACL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdApprovalCoaLineHome.findByCoaAccountNumber(java.lang.String COA_ACCNT_NMBR, java.lang.Integer ACL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdApprovalCoaLine findByCoaAccountNumber(java.lang.String COA_ACCNT_NMBR,
														 java.lang.Integer ACL_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(acl) FROM AdApprovalCoaLine acl WHERE acl.glChartOfAccount.coaAccountNumber=?1 AND acl.aclAdCompany = ?2",
					companyShortName);
			query.setParameter(1, COA_ACCNT_NMBR);
			query.setParameter(2, ACL_AD_CMPNY);
			return (LocalAdApprovalCoaLine) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalAdApprovalCoaLine create(Integer ACL_AD_CMPNY) throws CreateException {
		try {

			LocalAdApprovalCoaLine entity = new LocalAdApprovalCoaLine();

			Debug.print("AdApprovalCoaLineBean create");

			entity.setAclAdCompany(ACL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}

	}

}