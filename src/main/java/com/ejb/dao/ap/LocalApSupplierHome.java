package com.ejb.dao.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApSupplier;
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
public class LocalApSupplierHome implements ILocalApSupplierHome {

	public static final String JNDI_NAME = "LocalApSupplierHome!com.ejb.ap.LocalApSupplierHome";

	@EJB
	public PersistenceBeanClass em;
	
	private String SPL_SPPLR_CODE = null; 
	private final String SPL_ACCNT_NMBR = null;
	private String SPL_NM = null; 
	private final String SPL_ADDRSS = null;
	private final String SPL_CTY = null;
	private final String SPL_STT_PRVNC = null;
	private final String SPL_PSTL_CD = null;
	private final String SPL_CNTRY = null;
	private final String SPL_CNTCT = null;
	private final String SPL_PHN = null;
	private final String SPL_FX = null;
	private final String SPL_ALTRNT_PHN = null;
	private final String SPL_ALTRNT_CNTCT = null;
	private final String SPL_EML = null;
	private final String SPL_TIN = null;
	private final Integer SPL_COA_GL_PYBL_ACCNT = null;
	private final Integer SPL_COA_GL_EXPNS_ACCNT = null;
	private byte SPL_ENBL = 0;  
	private final String SPL_RMRKS = null;
	private Integer SPL_AD_CMPNY = null;

	public LocalApSupplierHome() {
	}

	// FINDER METHODS

	public LocalApSupplier findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalApSupplier entity = (LocalApSupplier) em.find(new LocalApSupplier(),
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
	
	public LocalApSupplier findById(java.lang.Integer pk) throws FinderException {

		try {

			LocalApSupplier entity = (LocalApSupplier) em.find(new LocalApSupplier(),
					pk);
			if (entity != null && entity.getSplEnable() == EJBCommon.TRUE) {
				return entity;
			}
			
			throw new FinderException();
			
		} catch (FinderException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	
	public java.util.Collection findEnabledSplAll(java.lang.Integer SPL_AD_BRNCH, java.lang.Integer SPL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(spl) FROM ApSupplier spl, IN(spl.adBranchSuppliers) bspl WHERE spl.splEnable = 1 AND bspl.adBranch.brCode = ?1 AND spl.splAdCompany = ?2 ORDER BY spl.splSupplierCode");
			query.setParameter(1, SPL_AD_BRNCH);
			query.setParameter(2, SPL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierHome.findEnabledSplAll(java.lang.Integer SPL_AD_BRNCH, java.lang.Integer SPL_AD_CMPNY)");
			throw ex;
		}
	}
	
	public java.util.Collection findSplAll(java.lang.Integer SPL_AD_BRNCH, java.lang.Integer SPL_AD_CMPNY)
			throws FinderException {

		try {
			
			//TODO: Modify Get All Suppliers based on the AD_BR_SPL data upon Supplier creation.
			
			Query query = em.createQuery(
					"SELECT OBJECT(spl) FROM ApSupplierModel spl, IN(spl.adBranchSuppliers) bspl WHERE spl.splEnable = 1 AND bspl.adBranch.brCode = ?1 AND spl.splAdCompany = ?2");
			query.setParameter(1, SPL_AD_BRNCH);
			query.setParameter(2, SPL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierHome.findEnabledSplAll(java.lang.Integer SPL_AD_BRNCH, java.lang.Integer SPL_AD_CMPNY)");
			throw ex;
		}
	}
	

	public java.util.Collection findEnabledSplTradeAll(java.lang.Integer SPL_AD_BRNCH, java.lang.Integer SPL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(spl) FROM ApSupplier spl, IN(spl.adBranchSuppliers) bspl WHERE spl.splEnable = 1 AND spl.apSupplierClass.scName = 'Trade' AND bspl.adBranch.brCode = ?1 AND spl.splAdCompany spl.splAdCompany = ?2");
			query.setParameter(1, SPL_AD_BRNCH);
			query.setParameter(2, SPL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierHome.findEnabledSplTradeAll(java.lang.Integer SPL_AD_BRNCH, java.lang.Integer SPL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findEnabledSplAllOrderBySplName(java.lang.Integer SPL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(spl) FROM ApSupplier spl WHERE spl.splEnable = 1 AND spl.splAdCompany = ?1");
			query.setParameter(1, SPL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierHome.findEnabledSplAllOrderBySplName(java.lang.Integer SPL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAllEnabledSplScLedger(java.lang.Integer SPL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(spl) FROM ApSupplier spl WHERE spl.splEnable = 1 AND spl.apSupplierClass.scLedger = 1 AND spl.splAdCompany = ?1");
			query.setParameter(1, SPL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierHome.findAllEnabledSplScLedger(java.lang.Integer SPL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAllSplByScCode(java.lang.Integer SC_CODE, java.lang.Integer SPL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(spl) FROM ApSupplier spl WHERE spl.apSupplierClass.scCode = ?1 AND spl.splAdCompany = ?2");
			query.setParameter(1, SC_CODE);
			query.setParameter(2, SPL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierHome.findAllSplByScCode(java.lang.Integer SC_CODE, java.lang.Integer SPL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAllSplInvestor(java.lang.Integer SPL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(spl) FROM ApSupplier spl WHERE spl.apSupplierClass.scName = 'Investors' AND spl.splAdCompany = ?1");
			query.setParameter(1, SPL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierHome.findAllSplInvestor(java.lang.Integer SPL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalApSupplier findBySplSupplierCode(java.lang.String SPL_SPPLR_CODE, java.lang.Integer SPL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(spl) FROM ApSupplier spl WHERE spl.splSupplierCode = ?1 AND spl.splAdCompany = ?2");
			query.setParameter(1, SPL_SPPLR_CODE);
			query.setParameter(2, SPL_AD_CMPNY);
            return (LocalApSupplier) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApSupplierHome.findBySplSupplierCode(java.lang.String SPL_SPPLR_CODE, java.lang.Integer SPL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierHome.findBySplSupplierCode(java.lang.String SPL_SPPLR_CODE, java.lang.Integer SPL_AD_CMPNY)");
			throw ex;
		}
	}
	
	public LocalApSupplier findBySplSupplierCode(java.lang.String SPL_NM, java.lang.Integer SPL_AD_BRNCH,
			java.lang.Integer SPL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(spl) FROM ApSupplier spl , IN(spl.adBranchSuppliers) bspl WHERE spl.splSupplierCode = ?1 AND bspl.adBranch.brCode = ?2 AND spl.splAdCompany = ?3");
			query.setParameter(1, SPL_NM);
			query.setParameter(2, SPL_AD_BRNCH);
			query.setParameter(3, SPL_AD_CMPNY);
            return (LocalApSupplier) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApSupplierHome.findBySplName(java.lang.String SPL_NM, java.lang.Integer SPL_AD_BRNCH, java.lang.Integer SPL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierHome.findBySplName(java.lang.String SPL_NM, java.lang.Integer SPL_AD_BRNCH, java.lang.Integer SPL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalApSupplier findBySplNameAndAddress(java.lang.String SPL_NM, java.lang.String SPL_ADDRSS,
			java.lang.Integer SPL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(spl) FROM ApSupplier spl WHERE spl.splName = ?1 AND spl.splAddress = ?2 AND spl.splAdCompany = ?3");
			query.setParameter(1, SPL_NM);
			query.setParameter(2, SPL_ADDRSS);
			query.setParameter(3, SPL_AD_CMPNY);
            return (LocalApSupplier) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApSupplierHome.findBySplNameAndAddress(java.lang.String SPL_NM, java.lang.String SPL_ADDRSS, java.lang.Integer SPL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierHome.findBySplNameAndAddress(java.lang.String SPL_NM, java.lang.String SPL_ADDRSS, java.lang.Integer SPL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBySplCoaGlPayableAccount(java.lang.Integer COA_CODE, java.lang.Integer SPL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(spl) FROM ApSupplier spl WHERE spl.splCoaGlPayableAccount = ?1 AND spl.splAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, SPL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierHome.findBySplCoaGlPayableAccount(java.lang.Integer COA_CODE, java.lang.Integer SPL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBySplCoaGlExpenseAccount(java.lang.Integer COA_CODE, java.lang.Integer SPL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(spl) FROM ApSupplier spl WHERE spl.splCoaGlExpenseAccount = ?1 AND spl.splAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, SPL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierHome.findBySplCoaGlExpenseAccount(java.lang.Integer COA_CODE, java.lang.Integer SPL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalApSupplier findBySplName(java.lang.String SPL_NM, java.lang.Integer SPL_AD_BRNCH,
			java.lang.Integer SPL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(spl) FROM ApSupplier spl , IN(spl.adBranchSuppliers) bspl WHERE spl.splName = ?1 AND bspl.adBranch.brCode = ?2 AND spl.splAdCompany = ?3");
			query.setParameter(1, SPL_NM);
			query.setParameter(2, SPL_AD_BRNCH);
			query.setParameter(3, SPL_AD_CMPNY);
            return (LocalApSupplier) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApSupplierHome.findBySplName(java.lang.String SPL_NM, java.lang.Integer SPL_AD_BRNCH, java.lang.Integer SPL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierHome.findBySplName(java.lang.String SPL_NM, java.lang.Integer SPL_AD_BRNCH, java.lang.Integer SPL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalApSupplier findBySplName(java.lang.String SPL_NM, java.lang.Integer SPL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(spl) FROM ApSupplier spl WHERE spl.splName = ?1 AND spl.splAdCompany = ?2");
			query.setParameter(1, SPL_NM);
			query.setParameter(2, SPL_AD_CMPNY);
            return (LocalApSupplier) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApSupplierHome.findBySplName(java.lang.String SPL_NM, java.lang.Integer SPL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierHome.findBySplName(java.lang.String SPL_NM, java.lang.Integer SPL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findEnabledSplAllOrderBySplSupplierCode(java.lang.Integer SPL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(spl) FROM ApSupplier spl WHERE spl.splEnable = 1 AND spl.splAdCompany = ?1");
			query.setParameter(1, SPL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierHome.findEnabledSplAllOrderBySplSupplierCode(java.lang.Integer SPL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findSplBySplNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY,
			char NEW, char UPDATED, char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT DISTINCT OBJECT(spl) FROM ApSupplier spl, IN(spl.adBranchSuppliers) bspl WHERE (bspl.bsplSupplierDownloadStatus = ?3 OR bspl.bsplSupplierDownloadStatus = ?4 OR bspl.bsplSupplierDownloadStatus = ?5) AND bspl.adBranch.brCode = ?1 AND bspl.bsplAdCompany = ?2");
			query.setParameter(1, BR_CODE);
			query.setParameter(2, AD_CMPNY);
			query.setParameter(3, NEW);
			query.setParameter(4, UPDATED);
			query.setParameter(5, DOWNLOADED_UPDATED);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierHome.findSplBySplNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY, char NEW, char UPDATED, char DOWNLOADED_UPDATED)");
			throw ex;
		}
	}

	public java.util.Collection getSplByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
			java.lang.Integer LIMIT, java.lang.Integer OFFSET) throws FinderException {

		try {
			Query query = em.createQuery(jbossQl);
			int cnt = 1;
			for (Object data : args) {
				query.setParameter(cnt, data);
				cnt++;
			}
			if(LIMIT>0){query.setMaxResults(LIMIT);}
            query.setFirstResult(OFFSET);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	// OTHER METHODS
	public void updateSupplier(LocalApSupplier apSupplier) {
		
		Debug.print("ApSupplierBean updateSupplier");
		
		em.merge(apSupplier);
	}
	
	public LocalApSupplier buildSupplier() throws CreateException {
		
		try {

			LocalApSupplier entity = new LocalApSupplier();

			Debug.print("ApSupplierBean buildSupplier");
			
			entity.setSplSupplierCode(SPL_SPPLR_CODE);
			entity.setSplAccountNumber(SPL_ACCNT_NMBR);
			entity.setSplName(SPL_NM);
			entity.setSplAddress(SPL_ADDRSS);
			entity.setSplCity(SPL_CTY);
			entity.setSplStateProvince(SPL_STT_PRVNC);
			entity.setSplPostalCode(SPL_PSTL_CD);
			entity.setSplCountry(SPL_CNTRY);
			entity.setSplContact(SPL_CNTCT);
			entity.setSplPhone(SPL_PHN);
			entity.setSplFax(SPL_FX);
			entity.setSplAlternatePhone(SPL_ALTRNT_PHN);
			entity.setSplAlternateContact(SPL_ALTRNT_CNTCT);
			entity.setSplEmail(SPL_EML);
			entity.setSplTin(SPL_TIN);
			entity.setSplCoaGlPayableAccount(SPL_COA_GL_PYBL_ACCNT);
			entity.setSplCoaGlExpenseAccount(SPL_COA_GL_EXPNS_ACCNT);
			entity.setSplEnable(SPL_ENBL);
			entity.setSplRemarks(SPL_RMRKS);
			entity.setSplAdCompany(SPL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}
	
	public ILocalApSupplierHome SplSupplierCode(String SPL_SPPLR_CODE) {
		this.SPL_SPPLR_CODE = SPL_SPPLR_CODE;
		return this;
	}
	
	public LocalApSupplierHome SplName(String SPL_NM) {
		this.SPL_NM = SPL_NM;
		return this;
	}
	
	public LocalApSupplierHome SplEnable(byte SPL_ENBL) {
		this.SPL_ENBL = SPL_ENBL;
		return this;
	}
	
	public LocalApSupplierHome SplAdCompany(Integer SPL_AD_CMPNY) {
		this.SPL_AD_CMPNY = SPL_AD_CMPNY;
		return this;
	}

	// CREATE METHODS

	public LocalApSupplier create(Integer SPL_CODE, String SPL_SPPLR_CODE, String SPL_ACCNT_NMBR,
                                  String SPL_NM, String SPL_ADDRSS, String SPL_CTY, String SPL_STT_PRVNC, String SPL_PSTL_CD,
                                  String SPL_CNTRY, String SPL_CNTCT, String SPL_PHN, String SPL_FX, String SPL_ALTRNT_PHN,
                                  String SPL_ALTRNT_CNTCT, String SPL_EML, String SPL_TIN, Integer SPL_COA_GL_PYBL_ACCNT,
                                  Integer SPL_COA_GL_EXPNS_ACCNT, byte SPL_ENBL, String SPL_RMRKS, Integer SPL_AD_CMPNY)
			throws CreateException {
		try {

			LocalApSupplier entity = new LocalApSupplier();

			Debug.print("ApSupplierBean create");
			entity.setSplCode(SPL_CODE);
			entity.setSplSupplierCode(SPL_SPPLR_CODE);
			entity.setSplAccountNumber(SPL_ACCNT_NMBR);
			entity.setSplName(SPL_NM);
			entity.setSplAddress(SPL_ADDRSS);
			entity.setSplCity(SPL_CTY);
			entity.setSplStateProvince(SPL_STT_PRVNC);
			entity.setSplPostalCode(SPL_PSTL_CD);
			entity.setSplCountry(SPL_CNTRY);
			entity.setSplContact(SPL_CNTCT);
			entity.setSplPhone(SPL_PHN);
			entity.setSplFax(SPL_FX);
			entity.setSplAlternatePhone(SPL_ALTRNT_PHN);
			entity.setSplAlternateContact(SPL_ALTRNT_CNTCT);
			entity.setSplEmail(SPL_EML);
			entity.setSplTin(SPL_TIN);
			entity.setSplCoaGlPayableAccount(SPL_COA_GL_PYBL_ACCNT);
			entity.setSplCoaGlExpenseAccount(SPL_COA_GL_EXPNS_ACCNT);
			entity.setSplEnable(SPL_ENBL);
			entity.setSplRemarks(SPL_RMRKS);
			entity.setSplAdCompany(SPL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalApSupplier create(String SPL_SPPLR_CODE, String SPL_ACCNT_NMBR, String SPL_NM,
                                  String SPL_ADDRSS, String SPL_CTY, String SPL_STT_PRVNC, String SPL_PSTL_CD, String SPL_CNTRY,
                                  String SPL_CNTCT, String SPL_PHN, String SPL_FX, String SPL_ALTRNT_PHN, String SPL_ALTRNT_CNTCT,
                                  String SPL_EML, String SPL_TIN, Integer SPL_COA_GL_PYBL_ACCNT, Integer SPL_COA_GL_EXPNS_ACCNT,
                                  byte SPL_ENBL, String SPL_RMRKS, Integer SPL_AD_CMPNY) throws CreateException {
		try {

			LocalApSupplier entity = new LocalApSupplier();

			Debug.print("ApSupplierBean create");
			entity.setSplSupplierCode(SPL_SPPLR_CODE);
			entity.setSplAccountNumber(SPL_ACCNT_NMBR);
			entity.setSplName(SPL_NM);
			entity.setSplAddress(SPL_ADDRSS);
			entity.setSplCity(SPL_CTY);
			entity.setSplStateProvince(SPL_STT_PRVNC);
			entity.setSplPostalCode(SPL_PSTL_CD);
			entity.setSplCountry(SPL_CNTRY);
			entity.setSplContact(SPL_CNTCT);
			entity.setSplPhone(SPL_PHN);
			entity.setSplFax(SPL_FX);
			entity.setSplAlternatePhone(SPL_ALTRNT_PHN);
			entity.setSplAlternateContact(SPL_ALTRNT_CNTCT);
			entity.setSplEmail(SPL_EML);
			entity.setSplTin(SPL_TIN);
			entity.setSplCoaGlPayableAccount(SPL_COA_GL_PYBL_ACCNT);
			entity.setSplCoaGlExpenseAccount(SPL_COA_GL_EXPNS_ACCNT);
			entity.setSplEnable(SPL_ENBL);
			entity.setSplRemarks(SPL_RMRKS);
			entity.setSplAdCompany(SPL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}