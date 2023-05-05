package com.ejb.dao.ar;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArCustomer;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;
import com.util.EJBCommon;

@SuppressWarnings("ALL")
@Stateless
public class LocalArCustomerHome {

	public static final String JNDI_NAME = "LocalArCustomerHome!com.ejb.ar.LocalArCustomerHome";

	@EJB
	public PersistenceBeanClass em;

	private String CST_CSTMR_CODE = null;
	private String CST_REF_CC = null;
	private String CST_NM = null;
	private String CST_DESC = null;
	private String CST_PYMNT_MTHD = null;
	private double CST_CRDT_LMT = 0d;
	private String CST_ADDRSS = null;
	private String CST_CTY = null;
	private String CST_STT_PRVNC = null;
	private String CST_PSTL_CD = null;
	private String CST_CNTRY = null;
	private String CST_CNTCT = null;
	private String CST_EMP_ID = null;
	private String CST_ACCNT_NO = null;
	private String CST_PHN = null;
	private String CST_MBL_PHN = null;
	private String CST_FX = null;
	private String CST_ALTRNT_PHN = null;
	private String CST_ALTRNT_MBL_PHN = null;
	private String CST_ALTRNT_CNTCT = null;
	private String CST_EML = null;
	private String CST_BLL_TO_ADDRSS = null;
	private String CST_BLL_TO_CNTCT = null;
	private String CST_BLL_TO_ALT_CNTCT = null;
	private String CST_BLL_TO_PHN = null;
	private String CST_BLLNG_HDR = null;
	private String CST_BLLNG_FTR = null;
	private String CST_BLLNG_HDR2 = null;
	private String CST_BLLNG_FTR2 = null;
	private String CST_BLLNG_HDR3 = null;
	private String CST_BLLNG_FTR3 = null;
	private String CST_BLLNG_SGNTRY = null;
	private String CST_SGNTRY_TTL = null;
	private String CST_SHP_TO_ADDRSS = null;
	private String CST_SHP_TO_CNTCT = null;
	private String CST_SHP_TO_ALT_CNTCT = null;
	private String CST_SHP_TO_PHN = null;
	private String CST_TIN = null;
	private double CST_NO_PRKNG = 0d;
	private double CST_MNTHLY_INT_RT = 0d;
	private String CST_PRKNG_ID = null;
	private double CST_ASD_RT = 0d;
	private double CST_RPT_RT = 0d;
	private String CST_WP_CSTMR_ID = null;
	private Integer CST_GL_COA_RCVBL_ACCNT = null;
	private Integer CST_GL_COA_RVNUE_ACCNT = null;
	private Integer CST_GL_COA_UNERND_INT_ACCNT = null;
	private Integer CST_GL_COA_ERND_INT_ACCNT = null;
	private Integer CST_GL_COA_UNERND_PNT_ACCNT = null;
	private Integer CST_GL_COA_ERND_PNT_ACCNT = null;
	private byte CST_ENBL = 0;
	private byte CST_ENBL_PYRLL = 0;
	private byte CST_ENBL_RTL_CSHR = 0;
	private byte CST_ENBL_RBT = 0;
	private byte CST_AUTO_CMPUTE_INT = 0;
	private byte CST_AUTO_CMPUTE_PNT = 0;
	private Date CST_BRTHDY = null;
	private String CST_DL_PRC = null;
	private String CST_AREA = null;
	private double CST_SQ_MTR = 0d;
	private Date CST_ENTRY_DT = null;
	private short CST_EFFCTVTY_DYS = 0;
	private String CST_AD_LV_RGN = null;
	private String CST_MMO = null;
	private String CST_CSTMR_BTCH = null;
	private String CST_CSTMR_DPRTMNT = null;
	private String CST_APPRVL_STATUS = null;
	private String CST_RSN_FR_RJCTN = null;
	private byte CST_PSTD = 0;
	private String CST_CRTD_BY = null;
	private Date CST_DT_CRTD = null;
	private String CST_LST_MDFD_BY = null;
	private Date CST_DT_LST_MDFD = null;
	private String CST_APPRVD_RJCTD_BY = null;
	private Date CST_DT_APPRVD_RJCTD = null;
	private String CST_PSTD_BY = null;
	private Date CST_DT_PSTD = null;
	private Integer CST_AD_BRNCH = null;
	private Integer CST_AD_CMPNY = null;

	// FINDER METHODS
	public LocalArCustomer findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArCustomer entity = (LocalArCustomer) em.find(new LocalArCustomer(),
					pk);
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

	public LocalArCustomer findByPrimaryKey(java.lang.Integer pk, String companyShortName) throws FinderException {

		try {

			LocalArCustomer entity = (LocalArCustomer) em.findPerCompany(new LocalArCustomer(),
					pk, companyShortName);
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

	public LocalArCustomer findById(java.lang.Integer pk) throws FinderException {

		try {

			LocalArCustomer entity = (LocalArCustomer) em.find(new LocalArCustomer(),
					pk);

			if (entity != null && entity.getCstEnable() == EJBCommon.TRUE) {
				return entity;
			}

			throw new FinderException();

		} catch (FinderException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findEnabledCstAll(java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst, IN(cst.adBranchCustomers) bcst WHERE cst.cstEnable = 1 AND bcst.adBranch.brCode=?1 AND cst.cstAdCompany = ?2 ORDER BY cst.cstCustomerCode");
			query.setParameter(1, CST_AD_BRNCH);
			query.setParameter(2, CST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findEnabledCstAll(java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findCstAll(java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomerModel cst, IN(cst.adBranchCustomers) bcst WHERE cst.cstEnable = 1 AND bcst.adBranch.brCode=?1 AND cst.cstAdCompany = ?2");
			query.setParameter(1, CST_AD_BRNCH);
			query.setParameter(2, CST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findCstAll(java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findActiveCstAll(java.lang.Integer CST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomerModel cst WHERE cst.cstEnable = 1 AND cst.cstAdCompany = ?1");
			query.setParameter(1, CST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findCstAll(java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findCstHrCashBondByCstHrDbReferenceID(java.lang.Integer DB_REF_ID,
			java.lang.Integer CST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstHrEnableCashBond = 1 AND cst.cstHrCashBondAmount > 0 AND cst.cstEnable = 1 AND cst.hrDeployedBranch.dbReferenceID = ?1 AND cst.cstAdCompany = ?2");
			query.setParameter(1, DB_REF_ID);
			query.setParameter(2, CST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findCstHrCashBondByCstHrDbReferenceID(java.lang.Integer DB_REF_ID, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findCstHrCashBondByCstHrAll(java.lang.Integer CST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstHrEnableCashBond = 1 AND cst.cstHrCashBondAmount > 0 AND cst.cstEnable = 1 AND cst.hrEmployee IS NOT NULL AND cst.cstAdCompany = ?1");
			query.setParameter(1, CST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findCstHrCashBondByCstHrAll(java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findCstHrInsMiscByHrDbReferenceID(java.lang.Integer DB_REF_ID,
			java.lang.Integer CST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstHrEnableInsMisc = 1 AND cst.cstHrInsMiscAmount > 0 AND cst.cstEnable = 1 AND cst.hrDeployedBranch.dbReferenceID =?1 AND cst.cstAdCompany = ?2");
			query.setParameter(1, DB_REF_ID);
			query.setParameter(2, CST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findCstHrInsMiscByHrDbReferenceID(java.lang.Integer DB_REF_ID, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findCstHrInsMiscByCstHrAll(java.lang.Integer CST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstHrEnableInsMisc = 1 AND cst.cstHrInsMiscAmount > 0 AND cst.cstEnable = 1 AND cst.hrEmployee IS NOT NULL AND cst.cstAdCompany = ?1");
			query.setParameter(1, CST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findCstHrInsMiscByCstHrAll(java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArCustomer findByCstHrEmpReferenceID(java.lang.Integer CST_HR_EMP_ID, java.lang.Integer CST_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.hrEmployee.empReferenceID = ?1 AND cst.cstAdCompany = ?2");
			query.setParameter(1, CST_HR_EMP_ID);
			query.setParameter(2, CST_AD_CMPNY);
            return (LocalArCustomer) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArCustomerHome.findByCstHrEmpReferenceID(java.lang.Integer CST_HR_EMP_ID, java.lang.Integer CST_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findByCstHrEmpReferenceID(java.lang.Integer CST_HR_EMP_ID, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArCustomer findByCstPmUsrReferenceID(java.lang.Integer CST_PM_USR_ID, java.lang.Integer CST_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.pmUser.usrReferenceID = ?1 AND cst.cstAdCompany = ?2");
			query.setParameter(1, CST_PM_USR_ID);
			query.setParameter(2, CST_AD_CMPNY);
            return (LocalArCustomer) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArCustomerHome.findByCstPmUsrReferenceID(java.lang.Integer CST_PM_USR_ID, java.lang.Integer CST_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findByCstPmUsrReferenceID(java.lang.Integer CST_PM_USR_ID, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArCustomer findByCstCustomerCodeAndHrEmpReferenceID(java.lang.String CST_CSTMR_CODE,
			java.lang.Integer CST_HR_EMP_ID, java.lang.Integer CST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstCustomerCode = ?1 AND cst.hrEmployee.empReferenceID = ?2 AND cst.cstAdCompany = ?3");
			query.setParameter(1, CST_CSTMR_CODE);
			query.setParameter(2, CST_HR_EMP_ID);
			query.setParameter(3, CST_AD_CMPNY);
            return (LocalArCustomer) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArCustomerHome.findByCstCustomerCodeAndHrEmpReferenceID(java.lang.String CST_CSTMR_CODE, java.lang.Integer CST_HR_EMP_ID, java.lang.Integer CST_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findByCstCustomerCodeAndHrEmpReferenceID(java.lang.String CST_CSTMR_CODE, java.lang.Integer CST_HR_EMP_ID, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findEnabledCstAllByCcName(java.lang.String CC_NM, java.lang.Integer CST_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.arCustomerClass.ccName = ?1 AND cst.cstEnable = 1 AND cst.cstAdCompany = ?2");
			query.setParameter(1, CC_NM);
			query.setParameter(2, CST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findEnabledCstAllByCcName(java.lang.String CC_NM, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findEnabledCstAllbyCustomerBatch(java.lang.String CST_CSTMR_BTCH,
			java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst, IN(cst.adBranchCustomers) bcst WHERE cst.cstEnable = 1 AND cst.cstCustomerBatch = ?1  AND bcst.adBranch.brCode=?2 AND cst.cstAdCompany = ?3");
			query.setParameter(1, CST_CSTMR_BTCH);
			query.setParameter(2, CST_AD_BRNCH);
			query.setParameter(3, CST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findEnabledCstAllbyCustomerBatch(java.lang.String CST_CSTMR_BTCH, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findCstArea(java.lang.Integer CST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstAdCompany = ?1");
			query.setParameter(1, CST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findCstArea(java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findEnabledCstAllOrderByCstName(java.lang.Integer CST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstEnable = 1 AND cst.cstAdCompany = ?1");
			query.setParameter(1, CST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findEnabledCstAllOrderByCstName(java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArCustomer findByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer CST_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstCustomerCode = ?1 AND cst.cstAdCompany = ?2");
			query.setParameter(1, CST_CSTMR_CODE);
			query.setParameter(2, CST_AD_CMPNY);
            return (LocalArCustomer) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArCustomerHome.findByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer CST_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArCustomer findByCstCustomerCode(java.lang.String CST_CSTMR_CODE, Integer companyCode, String companyShortName) throws FinderException {
		Debug.print("LocalArCustomerHome findByCstCustomerCode");
		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstCustomerCode = ?1 AND cst.cstAdCompany = ?2", companyShortName);
			query.setParameter(1, CST_CSTMR_CODE);
			query.setParameter(2, companyCode);
			return (LocalArCustomer) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalArCustomer findByCstCustomerCode(java.lang.String CST_NM, java.lang.Integer CST_AD_BRNCH,
			java.lang.Integer CST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst , IN(cst.adBranchCustomers) bcst WHERE cst.cstCustomerCode = ?1 AND bcst.adBranch.brCode=?2 AND cst.cstAdCompany = ?3");
			query.setParameter(1, CST_NM);
			query.setParameter(2, CST_AD_BRNCH);
			query.setParameter(3, CST_AD_CMPNY);
            return (LocalArCustomer) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArCustomerHome.findByCstCustomerCode(java.lang.String CST_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findByCstCustomerCode(java.lang.String CST_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}
	
	public LocalArCustomer findByCstRefCustomerCode(java.lang.String CST_NM, java.lang.Integer CST_AD_BRNCH,
			java.lang.Integer CST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst , IN(cst.adBranchCustomers) bcst WHERE cst.cstRefCustomerCode = ?1 AND bcst.adBranch.brCode=?2 AND cst.cstAdCompany = ?3");
			query.setParameter(1, CST_NM);
			query.setParameter(2, CST_AD_BRNCH);
			query.setParameter(3, CST_AD_CMPNY);
            return (LocalArCustomer) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArCustomerHome.findByCstRefCustomerCode(java.lang.String CST_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findByCstRefCustomerCode(java.lang.String CST_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArCustomer findByCstEmployeeID(java.lang.String CST_EMP_ID, java.lang.Integer CST_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstEmployeeID = ?1 AND cst.cstAdCompany = ?2");
			query.setParameter(1, CST_EMP_ID);
			query.setParameter(2, CST_AD_CMPNY);
            return (LocalArCustomer) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArCustomerHome.findByCstEmployeeID(java.lang.String CST_EMP_ID, java.lang.Integer CST_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findByCstEmployeeID(java.lang.String CST_EMP_ID, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findManyByCstEmployeeID(java.lang.String CST_EMP_ID, java.lang.Integer CST_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstEmployeeID = ?1 AND cst.cstAdCompany = ?2");
			query.setParameter(1, CST_EMP_ID);
			query.setParameter(2, CST_AD_CMPNY);
            return query.getResultList();

		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findManyByCstEmployeeID(java.lang.String CST_EMP_ID, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findManyByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer CST_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstCustomerCode = ?1 AND cst.cstAdCompany = ?2");
			query.setParameter(1, CST_CSTMR_CODE);
			query.setParameter(2, CST_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findManyByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByCstRejectedByByBrCodeAndCstLastModifiedBy(java.lang.Integer AD_BRNCH,
			java.lang.String VOU_CRTD_BY, java.lang.Integer VOU_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstReasonForRejection IS NOT NULL AND cst.cstAdBranch=?1 AND cst.cstLastModifiedBy = ?2 AND cst.cstAdCompany = ?3");
			query.setParameter(1, AD_BRNCH);
			query.setParameter(2, VOU_CRTD_BY);
			query.setParameter(3, VOU_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findByCstRejectedByByBrCodeAndCstLastModifiedBy(java.lang.Integer AD_BRNCH, java.lang.String VOU_CRTD_BY, java.lang.Integer VOU_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArCustomer findByCstNameAndAddress(java.lang.String CST_NM, java.lang.String CST_ADDRSS,
			java.lang.Integer CST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstName = ?1 AND cst.cstAddress = ?2 AND cst.cstAdCompany = ?3");
			query.setParameter(1, CST_NM);
			query.setParameter(2, CST_ADDRSS);
			query.setParameter(3, CST_AD_CMPNY);
            return (LocalArCustomer) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArCustomerHome.findByCstNameAndAddress(java.lang.String CST_NM, java.lang.String CST_ADDRSS, java.lang.Integer CST_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findByCstNameAndAddress(java.lang.String CST_NM, java.lang.String CST_ADDRSS, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByCstGlCoaReceivableAccount(java.lang.Integer COA_CODE,
			java.lang.Integer CST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstGlCoaReceivableAccount = ?1 AND cst.cstAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, CST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findByCstGlCoaReceivableAccount(java.lang.Integer COA_CODE, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByCstGlCoaRevenueAccount(java.lang.Integer COA_CODE, java.lang.Integer CST_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstGlCoaRevenueAccount = ?1 AND cst.cstAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, CST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findByCstGlCoaRevenueAccount(java.lang.Integer COA_CODE, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArCustomer findByCstName(java.lang.String CST_NM, java.lang.Integer CST_AD_BRNCH,
			java.lang.Integer CST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst , IN(cst.adBranchCustomers) bcst WHERE cst.cstName = ?1 AND bcst.adBranch.brCode=?2 AND cst.cstAdCompany = ?3");
			query.setParameter(1, CST_NM);
			query.setParameter(2, CST_AD_BRNCH);
			query.setParameter(3, CST_AD_CMPNY);
            return (LocalArCustomer) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArCustomerHome.findByCstName(java.lang.String CST_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findByCstName(java.lang.String CST_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArCustomer findByCstEmployeeID(java.lang.String CST_EMP_ID, java.lang.Integer CST_AD_BRNCH,
			java.lang.Integer CST_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst , IN(cst.adBranchCustomers) bcst WHERE cst.cstEmployeeID = ?1 AND bcst.adBranchCode=?2 AND cst.cstAdCompany = ?3");
			query.setParameter(1, CST_EMP_ID);
			query.setParameter(2, CST_AD_BRNCH);
			query.setParameter(3, CST_AD_CMPNY);
            return (LocalArCustomer) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArCustomerHome.findByCstEmployeeID(java.lang.String CST_EMP_ID, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findByCstEmployeeID(java.lang.String CST_EMP_ID, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArCustomer findByCstName(java.lang.String CST_NM, java.lang.Integer CST_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstName = ?1 AND cst.cstAdCompany = ?2");
			query.setParameter(1, CST_NM);
			query.setParameter(2, CST_AD_CMPNY);
            return (LocalArCustomer) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArCustomerHome.findByCstName(java.lang.String CST_NM, java.lang.Integer CST_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findByCstName(java.lang.String CST_NM, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArCustomer findByCstBySupplierCode(java.lang.Integer SPL_CODE, java.lang.Integer CST_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.apSupplier.splCode = ?1 AND cst.cstAdCompany = ?2");
			query.setParameter(1, SPL_CODE);
			query.setParameter(2, CST_AD_CMPNY);
            return (LocalArCustomer) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArCustomerHome.findByCstBySupplierCode(java.lang.Integer SPL_CODE, java.lang.Integer CST_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findByCstBySupplierCode(java.lang.Integer SPL_CODE, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByCstDealPrice(java.lang.String CST_DL_PRC, java.lang.Integer CST_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstDealPrice = ?1 AND cst.cstAdCompany = ?2");
			query.setParameter(1, CST_DL_PRC);
			query.setParameter(2, CST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findByCstDealPrice(java.lang.String CST_DL_PRC, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByCstRegion(java.lang.String CST_RGN, java.lang.Integer CST_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cst) FROM ArCustomer cst WHERE cst.cstAdLvRegion = ?1 AND cst.cstAdCompany = ?2");
			query.setParameter(1, CST_RGN);
			query.setParameter(2, CST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findByCstRegion(java.lang.String CST_RGN, java.lang.Integer CST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findCstByCstNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY,
			char NEW, char UPDATED, char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT DISTINCT OBJECT(cst) FROM ArCustomer cst, IN(cst.adBranchCustomers) bcst WHERE (bcst.bcstCustomerDownloadStatus = ?3 OR bcst.bcstCustomerDownloadStatus = ?4 OR bcst.bcstCustomerDownloadStatus = ?5) AND cst.cstEnableRetailCashier = 1 AND bcst.adBranch.brCode = ?1 AND bcst.bcstAdCompany = ?2");
			query.setParameter(1, BR_CODE);
			query.setParameter(2, AD_CMPNY);
			query.setParameter(3, NEW);
			query.setParameter(4, UPDATED);
			query.setParameter(5, DOWNLOADED_UPDATED);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerHome.findCstByCstNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY, char NEW, char UPDATED, char DOWNLOADED_UPDATED)");
			throw ex;
		}
	}

	public java.util.Collection getCstByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
			java.lang.Integer LIMIT, java.lang.Integer OFFSET) throws FinderException {

		try {
			Query query = em.createQuery(jbossQl);
			int cnt = 1;
			for (Object data : args) {
				query.setParameter(cnt, data);
				cnt++;
			}
			if (LIMIT > 0) {
				query.setMaxResults(LIMIT);
			}
            query.setFirstResult(OFFSET);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	// CREATE/UPDATE METHODS
	public void updateCustomer(LocalArCustomer arCustomer) {

		Debug.print("ArCustomerBean updateCustomer");

		em.merge(arCustomer);
	}

	public LocalArCustomer buildCustomer() throws CreateException {

		try {

			LocalArCustomer entity = new LocalArCustomer();

			Debug.print("ArCustomerBean buildCustomer");

			entity.setCstCustomerCode(CST_CSTMR_CODE);
			entity.setCstRefCustomerCode(CST_REF_CC);
			entity.setCstName(CST_NM);
			entity.setCstDescription(CST_DESC);
			entity.setCstPaymentMethod(CST_PYMNT_MTHD);
			entity.setCstCreditLimit(CST_CRDT_LMT);
			entity.setCstAddress(CST_ADDRSS);
			entity.setCstCity(CST_CTY);
			entity.setCstStateProvince(CST_STT_PRVNC);
			entity.setCstPostalCode(CST_PSTL_CD);
			entity.setCstCountry(CST_CNTRY);
			entity.setCstContact(CST_CNTCT);
			entity.setCstEmployeeID(CST_EMP_ID);
			entity.setCstAccountNumber(CST_ACCNT_NO);
			entity.setCstPhone(CST_PHN);
			entity.setCstMobilePhone(CST_MBL_PHN);
			entity.setCstFax(CST_FX);
			entity.setCstAlternatePhone(CST_ALTRNT_PHN);
			entity.setCstAlternateMobilePhone(CST_ALTRNT_MBL_PHN);
			entity.setCstAlternateContact(CST_ALTRNT_CNTCT);
			entity.setCstEmail(CST_EML);
			entity.setCstBillToAddress(CST_BLL_TO_ADDRSS);
			entity.setCstBillToContact(CST_BLL_TO_CNTCT);
			entity.setCstBillToAltContact(CST_BLL_TO_ALT_CNTCT);
			entity.setCstBillToPhone(CST_BLL_TO_PHN);
			entity.setCstBillingHeader(CST_BLLNG_HDR);
			entity.setCstBillingFooter(CST_BLLNG_FTR);
			entity.setCstBillingHeader2(CST_BLLNG_HDR2);
			entity.setCstBillingFooter2(CST_BLLNG_FTR2);
			entity.setCstBillingHeader3(CST_BLLNG_HDR3);
			entity.setCstBillingFooter3(CST_BLLNG_FTR3);
			entity.setCstBillingSignatory(CST_BLLNG_SGNTRY);
			entity.setCstSignatoryTitle(CST_SGNTRY_TTL);
			entity.setCstShipToAddress(CST_SHP_TO_ADDRSS);
			entity.setCstShipToContact(CST_SHP_TO_CNTCT);
			entity.setCstShipToAltContact(CST_SHP_TO_ALT_CNTCT);
			entity.setCstShipToPhone(CST_SHP_TO_PHN);
			entity.setCstTin(CST_TIN);
			entity.setCstNumbersParking(CST_NO_PRKNG);
			entity.setCstMonthlyInterestRate(CST_MNTHLY_INT_RT);
			entity.setCstParkingID(CST_PRKNG_ID);
			entity.setCstAssociationDuesRate(CST_ASD_RT);
			entity.setCstRealPropertyTaxRate(CST_RPT_RT);
			entity.setCstWordPressCustomerID(CST_WP_CSTMR_ID);
			entity.setCstGlCoaReceivableAccount(CST_GL_COA_RCVBL_ACCNT);
			entity.setCstGlCoaRevenueAccount(CST_GL_COA_RVNUE_ACCNT);
			entity.setCstGlCoaUnEarnedInterestAccount(CST_GL_COA_UNERND_INT_ACCNT);
			entity.setCstGlCoaEarnedInterestAccount(CST_GL_COA_ERND_INT_ACCNT);
			entity.setCstGlCoaUnEarnedPenaltyAccount(CST_GL_COA_UNERND_PNT_ACCNT);
			entity.setCstGlCoaEarnedPenaltyAccount(CST_GL_COA_ERND_PNT_ACCNT);
			entity.setCstEnable(CST_ENBL);
			entity.setCstEnablePayroll(CST_ENBL_PYRLL);
			entity.setCstEnableRetailCashier(CST_ENBL_RTL_CSHR);
			entity.setCstEnableRebate(CST_ENBL_RBT);
			entity.setCstAutoComputeInterest(CST_AUTO_CMPUTE_INT);
			entity.setCstAutoComputePenalty(CST_AUTO_CMPUTE_PNT);
			entity.setCstBirthday(CST_BRTHDY);
			entity.setCstDealPrice(CST_DL_PRC);
			entity.setCstArea(CST_AREA);
			entity.setCstSquareMeter(CST_SQ_MTR);
			entity.setCstEntryDate(CST_ENTRY_DT);
			entity.setCstEffectivityDays(CST_EFFCTVTY_DYS);
			entity.setCstAdLvRegion(CST_AD_LV_RGN);
			entity.setCstMemo(CST_MMO);
			entity.setCstCustomerBatch(CST_CSTMR_BTCH);
			entity.setCstCustomerDepartment(CST_CSTMR_DPRTMNT);
			entity.setCstApprovalStatus(CST_APPRVL_STATUS);
			entity.setCstReasonForRejection(CST_RSN_FR_RJCTN);
			entity.setCstPosted(CST_PSTD);
			entity.setCstCreatedBy(CST_CRTD_BY);
			entity.setCstDateCreated(CST_DT_CRTD);
			entity.setCstLastModifiedBy(CST_LST_MDFD_BY);
			entity.setCstDateLastModified(CST_DT_LST_MDFD);
			entity.setCstApprovedRejectedBy(CST_APPRVD_RJCTD_BY);
			entity.setCstDateApprovedRejected(CST_DT_APPRVD_RJCTD);
			entity.setCstPostedBy(CST_PSTD_BY);
			entity.setCstDatePosted(CST_DT_PSTD);
			entity.setCstAdBranch(CST_AD_BRNCH);
			entity.setCstAdCompany(CST_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArCustomer buildCustomer(String companyShortName) throws CreateException {

		try {

			LocalArCustomer entity = new LocalArCustomer();

			Debug.print("ArCustomerBean buildCustomer");

			entity.setCstCustomerCode(CST_CSTMR_CODE);
			entity.setCstRefCustomerCode(CST_REF_CC);
			entity.setCstName(CST_NM);
			entity.setCstDescription(CST_DESC);
			entity.setCstPaymentMethod(CST_PYMNT_MTHD);
			entity.setCstCreditLimit(CST_CRDT_LMT);
			entity.setCstAddress(CST_ADDRSS);
			entity.setCstCity(CST_CTY);
			entity.setCstStateProvince(CST_STT_PRVNC);
			entity.setCstPostalCode(CST_PSTL_CD);
			entity.setCstCountry(CST_CNTRY);
			entity.setCstContact(CST_CNTCT);
			entity.setCstEmployeeID(CST_EMP_ID);
			entity.setCstAccountNumber(CST_ACCNT_NO);
			entity.setCstPhone(CST_PHN);
			entity.setCstMobilePhone(CST_MBL_PHN);
			entity.setCstFax(CST_FX);
			entity.setCstAlternatePhone(CST_ALTRNT_PHN);
			entity.setCstAlternateMobilePhone(CST_ALTRNT_MBL_PHN);
			entity.setCstAlternateContact(CST_ALTRNT_CNTCT);
			entity.setCstEmail(CST_EML);
			entity.setCstBillToAddress(CST_BLL_TO_ADDRSS);
			entity.setCstBillToContact(CST_BLL_TO_CNTCT);
			entity.setCstBillToAltContact(CST_BLL_TO_ALT_CNTCT);
			entity.setCstBillToPhone(CST_BLL_TO_PHN);
			entity.setCstBillingHeader(CST_BLLNG_HDR);
			entity.setCstBillingFooter(CST_BLLNG_FTR);
			entity.setCstBillingHeader2(CST_BLLNG_HDR2);
			entity.setCstBillingFooter2(CST_BLLNG_FTR2);
			entity.setCstBillingHeader3(CST_BLLNG_HDR3);
			entity.setCstBillingFooter3(CST_BLLNG_FTR3);
			entity.setCstBillingSignatory(CST_BLLNG_SGNTRY);
			entity.setCstSignatoryTitle(CST_SGNTRY_TTL);
			entity.setCstShipToAddress(CST_SHP_TO_ADDRSS);
			entity.setCstShipToContact(CST_SHP_TO_CNTCT);
			entity.setCstShipToAltContact(CST_SHP_TO_ALT_CNTCT);
			entity.setCstShipToPhone(CST_SHP_TO_PHN);
			entity.setCstTin(CST_TIN);
			entity.setCstNumbersParking(CST_NO_PRKNG);
			entity.setCstMonthlyInterestRate(CST_MNTHLY_INT_RT);
			entity.setCstParkingID(CST_PRKNG_ID);
			entity.setCstAssociationDuesRate(CST_ASD_RT);
			entity.setCstRealPropertyTaxRate(CST_RPT_RT);
			entity.setCstWordPressCustomerID(CST_WP_CSTMR_ID);
			entity.setCstGlCoaReceivableAccount(CST_GL_COA_RCVBL_ACCNT);
			entity.setCstGlCoaRevenueAccount(CST_GL_COA_RVNUE_ACCNT);
			entity.setCstGlCoaUnEarnedInterestAccount(CST_GL_COA_UNERND_INT_ACCNT);
			entity.setCstGlCoaEarnedInterestAccount(CST_GL_COA_ERND_INT_ACCNT);
			entity.setCstGlCoaUnEarnedPenaltyAccount(CST_GL_COA_UNERND_PNT_ACCNT);
			entity.setCstGlCoaEarnedPenaltyAccount(CST_GL_COA_ERND_PNT_ACCNT);
			entity.setCstEnable(CST_ENBL);
			entity.setCstEnablePayroll(CST_ENBL_PYRLL);
			entity.setCstEnableRetailCashier(CST_ENBL_RTL_CSHR);
			entity.setCstEnableRebate(CST_ENBL_RBT);
			entity.setCstAutoComputeInterest(CST_AUTO_CMPUTE_INT);
			entity.setCstAutoComputePenalty(CST_AUTO_CMPUTE_PNT);
			entity.setCstBirthday(CST_BRTHDY);
			entity.setCstDealPrice(CST_DL_PRC);
			entity.setCstArea(CST_AREA);
			entity.setCstSquareMeter(CST_SQ_MTR);
			entity.setCstEntryDate(CST_ENTRY_DT);
			entity.setCstEffectivityDays(CST_EFFCTVTY_DYS);
			entity.setCstAdLvRegion(CST_AD_LV_RGN);
			entity.setCstMemo(CST_MMO);
			entity.setCstCustomerBatch(CST_CSTMR_BTCH);
			entity.setCstCustomerDepartment(CST_CSTMR_DPRTMNT);
			entity.setCstApprovalStatus(CST_APPRVL_STATUS);
			entity.setCstReasonForRejection(CST_RSN_FR_RJCTN);
			entity.setCstPosted(CST_PSTD);
			entity.setCstCreatedBy(CST_CRTD_BY);
			entity.setCstDateCreated(CST_DT_CRTD);
			entity.setCstLastModifiedBy(CST_LST_MDFD_BY);
			entity.setCstDateLastModified(CST_DT_LST_MDFD);
			entity.setCstApprovedRejectedBy(CST_APPRVD_RJCTD_BY);
			entity.setCstDateApprovedRejected(CST_DT_APPRVD_RJCTD);
			entity.setCstPostedBy(CST_PSTD_BY);
			entity.setCstDatePosted(CST_DT_PSTD);
			entity.setCstAdBranch(CST_AD_BRNCH);
			entity.setCstAdCompany(CST_AD_CMPNY);

			em.persist(entity, companyShortName);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArCustomerHome CstCustomerCode(String CST_CSTMR_CODE) {
		this.CST_CSTMR_CODE = CST_CSTMR_CODE;
		return this;
	}

	public LocalArCustomerHome CstRefCustomerCode(String CST_REF_CC) {
		this.CST_REF_CC = CST_REF_CC;
		return this;
	}

	public LocalArCustomerHome CstName(String CST_NM) {
		this.CST_NM = CST_NM;
		return this;
	}

	public LocalArCustomerHome CstDescription(String CST_DESC) {
		this.CST_DESC = CST_DESC;
		return this;
	}

	public LocalArCustomerHome CstDealPrice(String CST_DL_PRC) {
		this.CST_DL_PRC = CST_DL_PRC;
		return this;
	}

	public LocalArCustomerHome CstEnable(byte CST_ENBL) {
		this.CST_ENBL = CST_ENBL;
		return this;
	}

	public LocalArCustomerHome CstCreatedBy(String CST_CRTD_BY) {
		this.CST_CRTD_BY = CST_CRTD_BY;
		return this;
	}

	public LocalArCustomerHome CstDateCreated(Date CST_DT_CRTD) {
		this.CST_DT_CRTD = CST_DT_CRTD;
		return this;
	}

	public LocalArCustomerHome CstAdBranch(Integer CST_AD_BRNCH) {
		this.CST_AD_BRNCH = CST_AD_BRNCH;
		return this;
	}

	public LocalArCustomerHome CstAdCompany(Integer CST_AD_CMPNY) {
		this.CST_AD_CMPNY = CST_AD_CMPNY;
		return this;
	}

	public LocalArCustomer create(Integer AR_CST_CODE, String CST_CSTMR_CODE, String CST_NM, String CST_DESC,
                                  String CST_PYMNT_MTHD, double CST_CRDT_LMT, String CST_ADDRSS, String CST_CTY, String CST_STT_PRVNC,
                                  String CST_PSTL_CD, String CST_CNTRY, String CST_CNTCT, String CST_EMP_ID, String CST_ACCNT_NO,
                                  String CST_PHN, String CST_MBL_PHN, String CST_FX, String CST_ALTRNT_PHN, String CST_ALTRNT_MBL_PHN,
                                  String CST_ALTRNT_CNTCT, String CST_EML, String CST_BLL_TO_ADDRSS, String CST_BLL_TO_CNTCT,
                                  String CST_BLL_TO_ALT_CNTCT, String CST_BLL_TO_PHN, String CST_BLLNG_HDR, String CST_BLLNG_FTR,
                                  String CST_BLLNG_HDR2, String CST_BLLNG_FTR2, String CST_BLLNG_HDR3, String CST_BLLNG_FTR3,
                                  String CST_BLLNG_SGNTRY, String CST_SGNTRY_TTL, String CST_SHP_TO_ADDRSS, String CST_SHP_TO_CNTCT,
                                  String CST_SHP_TO_ALT_CNTCT, String CST_SHP_TO_PHN, String CST_TIN, double CST_NO_PRKNG,
                                  double CST_MNTHLY_INT_RT, String CST_PRKNG_ID, double CST_ASD_RT, double CST_RPT_RT, String CST_WP_CSTMR_ID,
                                  Integer CST_GL_COA_RCVBL_ACCNT, Integer CST_GL_COA_RVNUE_ACCNT, Integer CST_GL_COA_UNERND_INT_ACCNT,
                                  Integer CST_GL_COA_ERND_INT_ACCNT, Integer CST_GL_COA_UNERND_PNT_ACCNT, Integer CST_GL_COA_ERND_PNT_ACCNT,
                                  byte CST_ENBL, byte CST_ENBL_PYRLL, byte CST_ENBL_RTL_CSHR, byte CST_ENBL_RBT, byte CST_AUTO_CMPUTE_INT,
                                  byte CST_AUTO_CMPUTE_PNT, Date CST_BRTHDY, String CST_DL_PRC, String CST_AREA, double CST_SQ_MTR,
                                  Date CST_ENTRY_DT, short CST_EFFCTVTY_DYS, String CST_AD_LV_RGN, String CST_MMO, String CST_CSTMR_BTCH,
                                  String CST_CSTMR_DPRTMNT, String CST_APPRVL_STATUS, String CST_RSN_FR_RJCTN, byte CST_PSTD,
                                  String CST_CRTD_BY, Date CST_DT_CRTD, String CST_LST_MDFD_BY, Date CST_DT_LST_MDFD,
                                  String CST_APPRVD_RJCTD_BY, Date CST_DT_APPRVD_RJCTD, String CST_PSTD_BY, Date CST_DT_PSTD,
                                  Integer CST_AD_BRNCH, Integer CST_AD_CMPNY) throws CreateException {
		try {

			LocalArCustomer entity = new LocalArCustomer();

			Debug.print("ArCustomerBean create");
			entity.setCstCode(AR_CST_CODE);
			entity.setCstCustomerCode(CST_CSTMR_CODE);
			entity.setCstName(CST_NM);
			entity.setCstDescription(CST_DESC);
			entity.setCstPaymentMethod(CST_PYMNT_MTHD);
			entity.setCstCreditLimit(CST_CRDT_LMT);
			entity.setCstAddress(CST_ADDRSS);
			entity.setCstCity(CST_CTY);
			entity.setCstStateProvince(CST_STT_PRVNC);
			entity.setCstPostalCode(CST_PSTL_CD);
			entity.setCstCountry(CST_CNTRY);
			entity.setCstContact(CST_CNTCT);
			entity.setCstEmployeeID(CST_EMP_ID);
			entity.setCstAccountNumber(CST_ACCNT_NO);
			entity.setCstPhone(CST_PHN);
			entity.setCstMobilePhone(CST_MBL_PHN);
			entity.setCstFax(CST_FX);
			entity.setCstAlternatePhone(CST_ALTRNT_PHN);
			entity.setCstAlternateMobilePhone(CST_ALTRNT_MBL_PHN);
			entity.setCstAlternateContact(CST_ALTRNT_CNTCT);
			entity.setCstEmail(CST_EML);
			entity.setCstBillToAddress(CST_BLL_TO_ADDRSS);
			entity.setCstBillToContact(CST_BLL_TO_CNTCT);
			entity.setCstBillToAltContact(CST_BLL_TO_ALT_CNTCT);
			entity.setCstBillToPhone(CST_BLL_TO_PHN);
			entity.setCstBillingHeader(CST_BLLNG_HDR);
			entity.setCstBillingFooter(CST_BLLNG_FTR);
			entity.setCstBillingHeader2(CST_BLLNG_HDR2);
			entity.setCstBillingFooter2(CST_BLLNG_FTR2);
			entity.setCstBillingHeader3(CST_BLLNG_HDR3);
			entity.setCstBillingFooter3(CST_BLLNG_FTR3);
			entity.setCstBillingSignatory(CST_BLLNG_SGNTRY);
			entity.setCstSignatoryTitle(CST_SGNTRY_TTL);
			entity.setCstShipToAddress(CST_SHP_TO_ADDRSS);
			entity.setCstShipToContact(CST_SHP_TO_CNTCT);
			entity.setCstShipToAltContact(CST_SHP_TO_ALT_CNTCT);
			entity.setCstShipToPhone(CST_SHP_TO_PHN);
			entity.setCstTin(CST_TIN);
			entity.setCstNumbersParking(CST_NO_PRKNG);
			entity.setCstMonthlyInterestRate(CST_MNTHLY_INT_RT);
			entity.setCstParkingID(CST_PRKNG_ID);
			entity.setCstAssociationDuesRate(CST_ASD_RT);
			entity.setCstRealPropertyTaxRate(CST_RPT_RT);
			entity.setCstWordPressCustomerID(CST_WP_CSTMR_ID);
			entity.setCstGlCoaReceivableAccount(CST_GL_COA_RCVBL_ACCNT);
			entity.setCstGlCoaRevenueAccount(CST_GL_COA_RVNUE_ACCNT);
			entity.setCstGlCoaUnEarnedInterestAccount(CST_GL_COA_UNERND_INT_ACCNT);
			entity.setCstGlCoaEarnedInterestAccount(CST_GL_COA_ERND_INT_ACCNT);
			entity.setCstGlCoaUnEarnedPenaltyAccount(CST_GL_COA_UNERND_PNT_ACCNT);
			entity.setCstGlCoaEarnedPenaltyAccount(CST_GL_COA_ERND_PNT_ACCNT);
			entity.setCstEnable(CST_ENBL);
			entity.setCstEnablePayroll(CST_ENBL_PYRLL);
			entity.setCstEnableRetailCashier(CST_ENBL_RTL_CSHR);
			entity.setCstEnableRebate(CST_ENBL_RBT);
			entity.setCstAutoComputeInterest(CST_AUTO_CMPUTE_INT);
			entity.setCstAutoComputePenalty(CST_AUTO_CMPUTE_PNT);
			entity.setCstBirthday(CST_BRTHDY);
			entity.setCstDealPrice(CST_DL_PRC);
			entity.setCstArea(CST_AREA);
			entity.setCstSquareMeter(CST_SQ_MTR);
			entity.setCstEntryDate(CST_ENTRY_DT);
			entity.setCstEffectivityDays(CST_EFFCTVTY_DYS);
			entity.setCstAdLvRegion(CST_AD_LV_RGN);
			entity.setCstMemo(CST_MMO);
			entity.setCstCustomerBatch(CST_CSTMR_BTCH);
			entity.setCstCustomerDepartment(CST_CSTMR_DPRTMNT);
			entity.setCstApprovalStatus(CST_APPRVL_STATUS);
			entity.setCstReasonForRejection(CST_RSN_FR_RJCTN);
			entity.setCstPosted(CST_PSTD);
			entity.setCstCreatedBy(CST_CRTD_BY);
			entity.setCstDateCreated(CST_DT_CRTD);
			entity.setCstLastModifiedBy(CST_LST_MDFD_BY);
			entity.setCstDateLastModified(CST_DT_LST_MDFD);
			entity.setCstApprovedRejectedBy(CST_APPRVD_RJCTD_BY);
			entity.setCstDateApprovedRejected(CST_DT_APPRVD_RJCTD);
			entity.setCstPostedBy(CST_PSTD_BY);
			entity.setCstDatePosted(CST_DT_PSTD);
			entity.setCstAdBranch(CST_AD_BRNCH);
			entity.setCstAdCompany(CST_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArCustomer create(String CST_CSTMR_CODE, String CST_NM, String CST_DESC,
                                  String CST_PYMNT_MTHD, double CST_CRDT_LMT, String CST_ADDRSS, String CST_CTY, String CST_STT_PRVNC,
                                  String CST_PSTL_CD, String CST_CNTRY, String CST_CNTCT, String CST_EMP_ID, String CST_ACCNT_NO,
                                  String CST_PHN, String CST_MBL_PHN, String CST_FX, String CST_ALTRNT_PHN, String CST_ALTRNT_MBL_PHN,
                                  String CST_ALTRNT_CNTCT, String CST_EML, String CST_BLL_TO_ADDRSS, String CST_BLL_TO_CNTCT,
                                  String CST_BLL_TO_ALT_CNTCT, String CST_BLL_TO_PHN, String CST_BLLNG_HDR, String CST_BLLNG_FTR,
                                  String CST_BLLNG_HDR2, String CST_BLLNG_FTR2, String CST_BLLNG_HDR3, String CST_BLLNG_FTR3,
                                  String CST_BLLNG_SGNTRY, String CST_SGNTRY_TTL, String CST_SHP_TO_ADDRSS, String CST_SHP_TO_CNTCT,
                                  String CST_SHP_TO_ALT_CNTCT, String CST_SHP_TO_PHN, String CST_TIN, double CST_NO_PRKNG,
                                  double CST_MNTHLY_INT_RT, String CST_PRKNG_ID, double CST_ASD_RT, double CST_RPT_RT, String CST_WP_CSTMR_ID,
                                  Integer CST_GL_COA_RCVBL_ACCNT, Integer CST_GL_COA_RVNUE_ACCNT, Integer CST_GL_COA_UNERND_INT_ACCNT,
                                  Integer CST_GL_COA_ERND_INT_ACCNT, Integer CST_GL_COA_UNERND_PNT_ACCNT, Integer CST_GL_COA_ERND_PNT_ACCNT,
                                  byte CST_ENBL, byte CST_ENBL_PYRLL, byte CST_ENBL_RTL_CSHR, byte CST_ENBL_RBT, byte CST_AUTO_CMPUTE_INT,
                                  byte CST_AUTO_CMPUTE_PNT, Date CST_BRTHDY, String CST_DL_PRC, String CST_AREA, double CST_SQ_MTR,
                                  Date CST_ENTRY_DT, short CST_EFFCTVTY_DYS, String CST_AD_LV_RGN, String CST_MMO, String CST_CSTMR_BTCH,
                                  String CST_CSTMR_DPRTMNT, String CST_APPRVL_STATUS, String CST_RSN_FR_RJCTN, byte CST_PSTD,
                                  String CST_CRTD_BY, Date CST_DT_CRTD, String CST_LST_MDFD_BY, Date CST_DT_LST_MDFD,
                                  String CST_APPRVD_RJCTD_BY, Date CST_DT_APPRVD_RJCTD, String CST_PSTD_BY, Date CST_DT_PSTD,
                                  Integer CST_AD_BRNCH, Integer CST_AD_CMPNY) throws CreateException {
		try {

			LocalArCustomer entity = new LocalArCustomer();

			Debug.print("ArCustomerBean create");
			entity.setCstName(CST_NM);
			entity.setCstCustomerCode(CST_CSTMR_CODE);
			entity.setCstName(CST_NM);
			entity.setCstDescription(CST_DESC);
			entity.setCstPaymentMethod(CST_PYMNT_MTHD);
			entity.setCstCreditLimit(CST_CRDT_LMT);
			entity.setCstAddress(CST_ADDRSS);
			entity.setCstCity(CST_CTY);
			entity.setCstStateProvince(CST_STT_PRVNC);
			entity.setCstPostalCode(CST_PSTL_CD);
			entity.setCstCountry(CST_CNTRY);
			entity.setCstContact(CST_CNTCT);
			entity.setCstEmployeeID(CST_EMP_ID);
			entity.setCstAccountNumber(CST_ACCNT_NO);
			entity.setCstPhone(CST_PHN);
			entity.setCstMobilePhone(CST_MBL_PHN);
			entity.setCstFax(CST_FX);
			entity.setCstAlternatePhone(CST_ALTRNT_PHN);
			entity.setCstAlternateMobilePhone(CST_ALTRNT_MBL_PHN);
			entity.setCstAlternateContact(CST_ALTRNT_CNTCT);
			entity.setCstEmail(CST_EML);
			entity.setCstBillToAddress(CST_BLL_TO_ADDRSS);
			entity.setCstBillToContact(CST_BLL_TO_CNTCT);
			entity.setCstBillToAltContact(CST_BLL_TO_ALT_CNTCT);
			entity.setCstBillToPhone(CST_BLL_TO_PHN);
			entity.setCstBillingHeader(CST_BLLNG_HDR);
			entity.setCstBillingFooter(CST_BLLNG_FTR);
			entity.setCstBillingHeader2(CST_BLLNG_HDR2);
			entity.setCstBillingFooter2(CST_BLLNG_FTR2);
			entity.setCstBillingHeader3(CST_BLLNG_HDR3);
			entity.setCstBillingFooter3(CST_BLLNG_FTR3);
			entity.setCstBillingSignatory(CST_BLLNG_SGNTRY);
			entity.setCstSignatoryTitle(CST_SGNTRY_TTL);
			entity.setCstShipToAddress(CST_SHP_TO_ADDRSS);
			entity.setCstShipToContact(CST_SHP_TO_CNTCT);
			entity.setCstShipToAltContact(CST_SHP_TO_ALT_CNTCT);
			entity.setCstShipToPhone(CST_SHP_TO_PHN);
			entity.setCstTin(CST_TIN);
			entity.setCstNumbersParking(CST_NO_PRKNG);
			entity.setCstMonthlyInterestRate(CST_MNTHLY_INT_RT);
			entity.setCstParkingID(CST_PRKNG_ID);
			entity.setCstAssociationDuesRate(CST_ASD_RT);
			entity.setCstRealPropertyTaxRate(CST_RPT_RT);
			entity.setCstWordPressCustomerID(CST_WP_CSTMR_ID);
			entity.setCstGlCoaReceivableAccount(CST_GL_COA_RCVBL_ACCNT);
			entity.setCstGlCoaRevenueAccount(CST_GL_COA_RVNUE_ACCNT);
			entity.setCstGlCoaUnEarnedInterestAccount(CST_GL_COA_UNERND_INT_ACCNT);
			entity.setCstGlCoaEarnedInterestAccount(CST_GL_COA_ERND_INT_ACCNT);
			entity.setCstGlCoaUnEarnedPenaltyAccount(CST_GL_COA_UNERND_PNT_ACCNT);
			entity.setCstGlCoaEarnedPenaltyAccount(CST_GL_COA_ERND_PNT_ACCNT);
			entity.setCstEnable(CST_ENBL);
			entity.setCstEnablePayroll(CST_ENBL_PYRLL);
			entity.setCstEnableRetailCashier(CST_ENBL_RTL_CSHR);
			entity.setCstEnableRebate(CST_ENBL_RBT);
			entity.setCstAutoComputeInterest(CST_AUTO_CMPUTE_INT);
			entity.setCstAutoComputePenalty(CST_AUTO_CMPUTE_PNT);
			entity.setCstBirthday(CST_BRTHDY);
			entity.setCstDealPrice(CST_DL_PRC);
			entity.setCstArea(CST_AREA);
			entity.setCstSquareMeter(CST_SQ_MTR);
			entity.setCstEntryDate(CST_ENTRY_DT);
			entity.setCstEffectivityDays(CST_EFFCTVTY_DYS);
			entity.setCstAdLvRegion(CST_AD_LV_RGN);
			entity.setCstMemo(CST_MMO);
			entity.setCstCustomerBatch(CST_CSTMR_BTCH);
			entity.setCstCustomerDepartment(CST_CSTMR_DPRTMNT);
			entity.setCstApprovalStatus(CST_APPRVL_STATUS);
			entity.setCstReasonForRejection(CST_RSN_FR_RJCTN);
			entity.setCstPosted(CST_PSTD);
			entity.setCstCreatedBy(CST_CRTD_BY);
			entity.setCstDateCreated(CST_DT_CRTD);
			entity.setCstLastModifiedBy(CST_LST_MDFD_BY);
			entity.setCstDateLastModified(CST_DT_LST_MDFD);
			entity.setCstApprovedRejectedBy(CST_APPRVD_RJCTD_BY);
			entity.setCstDateApprovedRejected(CST_DT_APPRVD_RJCTD);
			entity.setCstPostedBy(CST_PSTD_BY);
			entity.setCstDatePosted(CST_DT_PSTD);
			entity.setCstAdBranch(CST_AD_BRNCH);
			entity.setCstAdCompany(CST_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}