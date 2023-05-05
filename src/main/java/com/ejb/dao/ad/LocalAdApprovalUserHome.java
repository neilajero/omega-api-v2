package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdApprovalUser;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;
import com.util.EJBCommon;

@Stateless
public class LocalAdApprovalUserHome {

	public static final String JNDI_NAME = "LocalAdApprovalUserHome!com.ejb.ad.LocalAdApprovalUserHome";

	@EJB
	public PersistenceBeanClass em;

	private String AU_LVL = null;
	private String AU_TYP = null;
	private byte AU_OR = EJBCommon.FALSE;
	private Integer AU_AD_CMPNY = null;

	public LocalAdApprovalUserHome() {
	}

	// FINDER METHODS

	public LocalAdApprovalUser findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdApprovalUser entity = (LocalAdApprovalUser) em
					.find(new LocalAdApprovalUser(), pk);
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

	public java.util.Collection findByAuTypeAndCalCode(java.lang.String AU_TYP, java.lang.Integer AD_CAL_CODE,
			java.lang.Integer AU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(au) FROM AdApprovalUser au WHERE au.auType = ?1 AND au.adAmountLimit.calCode = ?2 AND au.auAdCompany = ?3");
			query.setParameter(1, AU_TYP);
			query.setParameter(2, AD_CAL_CODE);
			query.setParameter(3, AU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdApprovalUserHome.findByAuTypeAndCalCode(java.lang.String AU_TYP, java.lang.Integer AD_CAL_CODE, java.lang.Integer AU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findApprovalUsersPerDepartment(java.lang.String CAL_DEPT, java.lang.String AU_TYP,
															   java.lang.Integer AD_CAL_CODE, java.lang.Integer AU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(au) FROM AdApprovalUser au WHERE au.adAmountLimit.calDept = ?1 AND au.auType = ?2 AND au.adAmountLimit.calCode = ?3 AND au.auAdCompany = ?4");
			query.setParameter(1, CAL_DEPT);
			query.setParameter(2, AU_TYP);
			query.setParameter(3, AD_CAL_CODE);
			query.setParameter(4, AU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdApprovalUserHome.findByCalDeptAndAuTypeAndCalCode(java.lang.String CAL_DEPT, java.lang.String AU_TYP, java.lang.Integer AD_CAL_CODE, java.lang.Integer AU_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findApprovalUsersPerDepartment(
			java.lang.String CAL_DEPT, java.lang.String AU_TYP, java.lang.Integer AD_CAL_CODE,
			java.lang.Integer AU_AD_CMPNY, String companyShortName)
			throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(au) FROM AdApprovalUser au WHERE au.adAmountLimit.calDept = ?1 "
							+ "AND au.auType = ?2 AND au.adAmountLimit.calCode = ?3 AND au.auAdCompany = ?4",
					companyShortName);
			query.setParameter(1, CAL_DEPT);
			query.setParameter(2, AU_TYP);
			query.setParameter(3, AD_CAL_CODE);
			query.setParameter(4, AU_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByCalCode(java.lang.Integer AD_CAL_CODE, java.lang.Integer AU_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(au) FROM AdAmountLimit cal, IN(cal.adApprovalUsers) au WHERE cal.calCode = ?1 AND au.auAdCompany = ?2");
			query.setParameter(1, AD_CAL_CODE);
			query.setParameter(2, AU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdApprovalUserHome.findByCalCode(java.lang.Integer AD_CAL_CODE, java.lang.Integer AU_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdApprovalUser findByUsrNameAndCalCode(java.lang.String USR_NM, java.lang.Integer AD_CAL_CODE,
			java.lang.Integer AU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(au) FROM AdAmountLimit cal, IN(cal.adApprovalUsers) au WHERE au.adUser.usrName = ?1 AND cal.calCode = ?2 AND au.auAdCompany = ?3");
			query.setParameter(1, USR_NM);
			query.setParameter(2, AD_CAL_CODE);
			query.setParameter(3, AU_AD_CMPNY);
            return (LocalAdApprovalUser) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdApprovalUserHome.findByUsrNameAndCalCode(java.lang.String USR_NM, java.lang.Integer AD_CAL_CODE, java.lang.Integer AU_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdApprovalUserHome.findByUsrNameAndCalCode(java.lang.String USR_NM, java.lang.Integer AD_CAL_CODE, java.lang.Integer AU_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdApprovalUser findByUsrNameAndCalCode(java.lang.String USR_NM, java.lang.Integer AD_CAL_CODE,
													   java.lang.Integer AU_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(au) FROM AdAmountLimit cal, IN(cal.adApprovalUsers) au "
							+ "WHERE au.adUser.usrName = ?1 AND cal.calCode = ?2 AND au.auAdCompany = ?3",
					companyShortName);
			query.setParameter(1, USR_NM);
			query.setParameter(2, AD_CAL_CODE);
			query.setParameter(3, AU_AD_CMPNY);
			return (LocalAdApprovalUser) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalAdApprovalUser findByAdcCodeAndApprovalTypeAndUsrNameAndAclDept(java.lang.String AU_TYP,
			java.lang.Integer ADC_CODE, java.lang.String USR_NM, java.lang.String CAL_DEPT, double CAL_AMNT_LMT,
			java.lang.Integer AU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(au) FROM AdApprovalDocument adc, IN(adc.adAmountLimits) cal, IN(cal.adApprovalUsers) au WHERE au.auType = ?1 AND adc.adcCode = ?2 AND au.adUser.usrName = ?3 AND cal.calDept = ?4 AND cal.calAmountLimit = ?5 AND au.auAdCompany = ?6");
			query.setParameter(1, AU_TYP);
			query.setParameter(2, ADC_CODE);
			query.setParameter(3, USR_NM);
			query.setParameter(4, CAL_DEPT);
			query.setParameter(5, CAL_AMNT_LMT);
			query.setParameter(6, AU_AD_CMPNY);
            return (LocalAdApprovalUser) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdApprovalUserHome.findByAdcCodeAndApprovalTypeAndUsrNameAndAclDept(java.lang.String AU_TYP, java.lang.Integer ADC_CODE, java.lang.String USR_NM, java.lang.String CAL_DEPT, double CAL_AMNT_LMT, java.lang.Integer AU_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdApprovalUserHome.findByAdcCodeAndApprovalTypeAndUsrNameAndAclDept(java.lang.String AU_TYP, java.lang.Integer ADC_CODE, java.lang.String USR_NM, java.lang.String CAL_DEPT, double CAL_AMNT_LMT, java.lang.Integer AU_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdApprovalUser findByAdcCodeAndUsrNameAndRequesterTypeLessThanAmountLimit(java.lang.String USR_NM,
			java.lang.String AU_TYP, double CAL_AMNT_LMT, java.lang.Integer ADC_CODE, java.lang.Integer AU_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(au) FROM AdApprovalDocument adc, IN(adc.adAmountLimits) cal, IN(cal.adApprovalUsers) au WHERE au.adUser.usrName = ?1 AND au.auType = ?2 AND cal.calAmountLimit < ?3 AND adc.adcCode = ?4 AND au.auAdCompany = ?5");
			query.setParameter(1, USR_NM);
			query.setParameter(2, AU_TYP);
			query.setParameter(3, CAL_AMNT_LMT);
			query.setParameter(4, ADC_CODE);
			query.setParameter(5, AU_AD_CMPNY);
            return (LocalAdApprovalUser) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdApprovalUserHome.findByAdcCodeAndUsrNameAndRequesterTypeLessThanAmountLimit(java.lang.String USR_NM, java.lang.String AU_TYP, double CAL_AMNT_LMT, java.lang.Integer ADC_CODE, java.lang.Integer AU_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdApprovalUserHome.findByAdcCodeAndUsrNameAndRequesterTypeLessThanAmountLimit(java.lang.String USR_NM, java.lang.String AU_TYP, double CAL_AMNT_LMT, java.lang.Integer ADC_CODE, java.lang.Integer AU_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdApprovalUser findByAdcCodeAndUsrNameAndApproverTypeLessThanAmountLimit(java.lang.String USR_NM,
			java.lang.String AU_TYP, double CAL_AMNT_LMT, java.lang.Integer ADC_CODE, java.lang.Integer AU_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(au) FROM AdApprovalDocument adc, IN(adc.adAmountLimits) cal, IN(cal.adApprovalUsers) au WHERE au.adUser.usrName = ?1 AND au.auType = ?2 AND cal.calAmountLimit > ?3 AND adc.adcCode = ?4 AND au.auAdCompany = ?5");
			query.setParameter(1, USR_NM);
			query.setParameter(2, AU_TYP);
			query.setParameter(3, CAL_AMNT_LMT);
			query.setParameter(4, ADC_CODE);
			query.setParameter(5, AU_AD_CMPNY);
            return (LocalAdApprovalUser) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdApprovalUserHome.findByAdcCodeAndUsrNameAndApproverTypeLessThanAmountLimit(java.lang.String USR_NM, java.lang.String AU_TYP, double CAL_AMNT_LMT, java.lang.Integer ADC_CODE, java.lang.Integer AU_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdApprovalUserHome.findByAdcCodeAndUsrNameAndApproverTypeLessThanAmountLimit(java.lang.String USR_NM, java.lang.String AU_TYP, double CAL_AMNT_LMT, java.lang.Integer ADC_CODE, java.lang.Integer AU_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdApprovalUser findByAclCodeAndApprovalTypeAndUsrName(java.lang.String AU_TYP,
			java.lang.Integer ACL_CODE, java.lang.String USR_NM, java.lang.Integer AU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(au) FROM AdApprovalCoaLine acl, IN(acl.adAmountLimits) cal, IN(cal.adApprovalUsers) au WHERE au.auType = ?1 AND acl.aclCode = ?2 AND au.adUser.usrName = ?3 AND au.auAdCompany = ?4");
			query.setParameter(1, AU_TYP);
			query.setParameter(2, ACL_CODE);
			query.setParameter(3, USR_NM);
			query.setParameter(4, AU_AD_CMPNY);
            return (LocalAdApprovalUser) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdApprovalUserHome.findByAclCodeAndApprovalTypeAndUsrName(java.lang.String AU_TYP, java.lang.Integer ACL_CODE, java.lang.String USR_NM, java.lang.Integer AU_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdApprovalUserHome.findByAclCodeAndApprovalTypeAndUsrName(java.lang.String AU_TYP, java.lang.Integer ACL_CODE, java.lang.String USR_NM, java.lang.Integer AU_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdApprovalUser findByAclCodeAndUsrNameAndRequesterTypeLessThanAmountLimit(java.lang.String USR_NM,
			java.lang.String AU_TYP, double CAL_AMNT_LMT, java.lang.Integer ACL_CODE, java.lang.Integer AU_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(au) FROM AdApprovalCoaLine acl, IN(acl.adAmountLimits) cal, IN(cal.adApprovalUsers) au WHERE au.adUser.usrName = ?1 AND au.auType = ?2 AND cal.calAmountLimit < ?3 AND acl.aclCode = ?4 AND au.auAdCompany = ?5");
			query.setParameter(1, USR_NM);
			query.setParameter(2, AU_TYP);
			query.setParameter(3, CAL_AMNT_LMT);
			query.setParameter(4, ACL_CODE);
			query.setParameter(5, AU_AD_CMPNY);
            return (LocalAdApprovalUser) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdApprovalUserHome.findByAclCodeAndUsrNameAndRequesterTypeLessThanAmountLimit(java.lang.String USR_NM, java.lang.String AU_TYP, double CAL_AMNT_LMT, java.lang.Integer ACL_CODE, java.lang.Integer AU_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdApprovalUserHome.findByAclCodeAndUsrNameAndRequesterTypeLessThanAmountLimit(java.lang.String USR_NM, java.lang.String AU_TYP, double CAL_AMNT_LMT, java.lang.Integer ACL_CODE, java.lang.Integer AU_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdApprovalUser findByAclCodeAndUsrNameAndApproverTypeLessThanAmountLimit(java.lang.String USR_NM,
			java.lang.String AU_TYP, double CAL_AMNT_LMT, java.lang.Integer ACL_CODE, java.lang.Integer AU_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(au) FROM AdApprovalCoaLine acl, IN(acl.adAmountLimits) cal, IN(cal.adApprovalUsers) au WHERE au.adUser.usrName = ?1 AND au.auType = ?2 AND cal.calAmountLimit > ?3 AND acl.aclCode = ?4 AND au.auAdCompany = ?5");
			query.setParameter(1, USR_NM);
			query.setParameter(2, AU_TYP);
			query.setParameter(3, CAL_AMNT_LMT);
			query.setParameter(4, ACL_CODE);
			query.setParameter(5, AU_AD_CMPNY);
            return (LocalAdApprovalUser) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdApprovalUserHome.findByAclCodeAndUsrNameAndApproverTypeLessThanAmountLimit(java.lang.String USR_NM, java.lang.String AU_TYP, double CAL_AMNT_LMT, java.lang.Integer ACL_CODE, java.lang.Integer AU_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdApprovalUserHome.findByAclCodeAndUsrNameAndApproverTypeLessThanAmountLimit(java.lang.String USR_NM, java.lang.String AU_TYP, double CAL_AMNT_LMT, java.lang.Integer ACL_CODE, java.lang.Integer AU_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdApprovalUserHome AuLevel(String AU_LVL) {
		this.AU_LVL = AU_LVL;
		return this;
	}

	public LocalAdApprovalUserHome AuType(String AU_TYP) {
		this.AU_TYP = AU_TYP;
		return this;
	}

	public LocalAdApprovalUserHome AuOr(byte AU_OR) {
		this.AU_OR = AU_OR;
		return this;
	}

	public LocalAdApprovalUserHome AuAdCompany(Integer AU_AD_CMPNY) {
		this.AU_AD_CMPNY = AU_AD_CMPNY;
		return this;
	}

	public LocalAdApprovalUser buildApprovalUser(String companyShortName)
			throws CreateException {
		try {

			LocalAdApprovalUser entity = new LocalAdApprovalUser();

			Debug.print("AdApprovalUserBean buildApprovalUser");

			entity.setAuLevel(AU_LVL);
			entity.setAuType(AU_TYP);
			entity.setAuOr(AU_OR);
			entity.setAuAdCompany(AU_AD_CMPNY);

			em.persist(entity, companyShortName);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}

	}

	public LocalAdApprovalUser create(Integer AU_CODE, String AU_LVL, String AU_TYP, byte AU_OR,
                                      Integer AU_AD_CMPNY) throws CreateException {
		try {

			LocalAdApprovalUser entity = new LocalAdApprovalUser();

			Debug.print("AdApprovalUserBean create");
			entity.setAuCode(AU_CODE);
			entity.setAuLevel(AU_LVL);
			entity.setAuType(AU_TYP);
			entity.setAuOr(AU_OR);
			entity.setAuAdCompany(AU_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}

	}

	public LocalAdApprovalUser create(String AU_LVL, String AU_TYP, byte AU_OR, Integer AU_AD_CMPNY)
			throws CreateException {
		try {

			LocalAdApprovalUser entity = new LocalAdApprovalUser();

			Debug.print("AdApprovalUserBean create");

			entity.setAuLevel(AU_LVL);
			entity.setAuType(AU_TYP);
			entity.setAuOr(AU_OR);
			entity.setAuAdCompany(AU_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}

	}

}