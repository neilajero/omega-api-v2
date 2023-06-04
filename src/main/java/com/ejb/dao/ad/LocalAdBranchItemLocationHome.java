package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@SuppressWarnings("ALL")
@Stateless
public class LocalAdBranchItemLocationHome {

	public static final String JNDI_NAME = "LocalAdBranchItemLocationHome!com.ejb.ad.LocalAdBranchItemLocationHome";

	@EJB
	public PersistenceBeanClass em;
	private final Integer BIL_CODE = null;
	private String BIL_RCK = null; 
	private String BIL_BN = null;
	private double BIL_RRDR_PNT = 0d; 
	private double BIL_RRDR_QTY = 0d;  
	private Integer BIL_COA_GL_SLS_ACCNT = null; 
	private Integer BIL_COA_GL_INVNTRY_ACCNT = null;
	private Integer BIL_COA_GL_CST_OF_SLS_ACCNT = null; 
	private Integer BIL_COA_GL_WIP_ACCNT = null; 
	private Integer BIL_COA_GL_ACCRD_INVNTRY_ACCNT = null;
	private Integer BIL_COA_GL_SLS_RTRN_ACCNT = null; 
	private Integer BIL_COA_GL_EXPNS_ACCNT = null;
	private byte BIL_SBJCT_TO_CMMSSN = 0; 
	private double BIL_HST1_SLS = 0d; 
	private double BIL_HST2_SLS = 0d;
	private double BIL_PRJ_SLS = 0d; 
	private double BIL_DLVRY_TME = 0d; 
	private double BIL_DLVRY_BUFF = 0d; 
	private double BIL_ORDR_PR_YR = 0d; 
	private char BIL_DS_ITM = '\u0000';
	private char BIL_DS_LOC = '\u0000';
	private char BIL_DS_IL = '\u0000'; 
	private Integer BIL_AD_CMPNY = null;

	public LocalAdBranchItemLocationHome() {
	}

	// FINDER METHODS

	public LocalAdBranchItemLocation findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdBranchItemLocation entity = (LocalAdBranchItemLocation) em
					.find(new LocalAdBranchItemLocation(), pk);
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

	public java.util.Collection findByInvIlAll(java.lang.Integer ITM_LCTN_CODE, java.lang.Integer BIL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bil) FROM AdBranchItemLocation bil WHERE bil.invItemLocation.ilCode = ?1 AND bil.bilAdCompany = ?2");
			query.setParameter(1, ITM_LCTN_CODE);
			query.setParameter(2, BIL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findBilByIlCodeAndRsCode(java.lang.Integer INV_IL_CODE, java.lang.Integer AD_RS_CODE,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bil) FROM AdBranchItemLocation bil, IN(bil.adBranch.adBranchResponsibilities)brs WHERE bil.invItemLocation.ilCode = ?1 AND brs.adResponsibility.rsCode = ?2 AND bil.bilAdCompany = ?3");
			query.setParameter(1, INV_IL_CODE);
			query.setParameter(2, AD_RS_CODE);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalAdBranchItemLocation findBilByIlCodeAndBrCode(java.lang.Integer INV_IL_CODE, java.lang.Integer BR_CODE,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bil) FROM AdBranchItemLocation bil WHERE bil.invItemLocation.ilCode = ?1 AND  bil.adBranch.brCode = ?2 AND bil.bilAdCompany = ?3");
			query.setParameter(1, INV_IL_CODE);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, AD_CMPNY);
            return (LocalAdBranchItemLocation) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalAdBranchItemLocation findBilByIlCodeAndBrCode(java.lang.Integer INV_IL_CODE, java.lang.Integer BR_CODE,
															  java.lang.Integer AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(bil) FROM AdBranchItemLocation bil "
							+ "WHERE bil.invItemLocation.ilCode = ?1 AND  bil.adBranch.brCode = ?2 AND bil.bilAdCompany = ?3",
					companyShortName);
			query.setParameter(1, INV_IL_CODE);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, AD_CMPNY);
			return (LocalAdBranchItemLocation) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findBilByIiCodeAndBrCode(java.lang.Integer II_CODE, java.lang.Integer BR_CODE,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bil) FROM AdBranchItemLocation bil WHERE bil.invItemLocation.invItem.iiCode = ?1 AND bil.adBranch.brCode = ?2 AND bil.bilAdCompany = ?3");
			query.setParameter(1, II_CODE);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findEnabledIiByIiNewAndUpdated(java.lang.Integer BR_CODE,
			java.lang.String II_IL_LOCATION, java.lang.Integer AD_CMPNY, char NEW, char UPDATED,
			char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bil) FROM AdBranchItemLocation bil WHERE (bil.bilItemDownloadStatus = ?4 OR bil.bilItemDownloadStatus = ?5 OR bil.bilItemDownloadStatus = ?6) AND bil.invItemLocation.invItem.iiEnable=1 AND bil.invItemLocation.invItem.iiRetailUom IS NOT NULL AND bil.adBranch.brCode = ?1 AND bil.invItemLocation.invLocation.locLvType = ?2 AND bil.bilAdCompany = ?3");
			query.setParameter(1, BR_CODE);
			query.setParameter(2, II_IL_LOCATION);
			query.setParameter(3, AD_CMPNY);
			query.setParameter(4, NEW);
			query.setParameter(5, UPDATED);
			query.setParameter(6, DOWNLOADED_UPDATED);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findAllIiByIiNewAndUpdated(java.lang.Integer BR_CODE, java.lang.String II_IL_LOCATION,
			java.lang.Integer AD_CMPNY, char NEW, char UPDATED, char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bil) FROM AdBranchItemLocation bil WHERE (bil.bilItemDownloadStatus = ?4 OR bil.bilItemDownloadStatus = ?5 OR bil.bilItemDownloadStatus = ?6) AND bil.invItemLocation.invItem.iiRetailUom IS NOT NULL AND bil.adBranch.brCode = ?1 AND bil.invItemLocation.invLocation.locLvType = ?2 AND bil.bilAdCompany = ?3");
			query.setParameter(1, BR_CODE);
			query.setParameter(2, II_IL_LOCATION);
			query.setParameter(3, AD_CMPNY);
			query.setParameter(4, NEW);
			query.setParameter(5, UPDATED);
			query.setParameter(6, DOWNLOADED_UPDATED);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findEnabledIiByIiNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY,
			char NEW, char UPDATED, char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bil) FROM AdBranchItemLocation bil WHERE (bil.bilItemDownloadStatus = ?3 OR bil.bilItemDownloadStatus = ?4 OR bil.bilItemDownloadStatus = ?5) AND bil.invItemLocation.invItem.iiEnable=1 AND bil.adBranch.brCode = ?1 AND bil.bilAdCompany = ?2");
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

	public java.util.Collection findLocByLocNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY,
			char NEW, char UPDATED, char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(bil) FROM AdBranchItemLocation bil "
					+ "WHERE (bil.bilLocationDownloadStatus = ?3 OR bil.bilLocationDownloadStatus = ?4 OR bil.bilLocationDownloadStatus = ?5) "
					+ "AND bil.adBranch.brCode = ?1 AND bil.bilAdCompany = ?2");
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

	public java.util.Collection findLocByLocNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY,
														  char NEW, char UPDATED, char DOWNLOADED_UPDATED, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany("SELECT OBJECT(bil) FROM AdBranchItemLocation bil "
					+ "WHERE (bil.bilLocationDownloadStatus = ?3 OR bil.bilLocationDownloadStatus = ?4 OR bil.bilLocationDownloadStatus = ?5) "
					+ "AND bil.adBranch.brCode = ?1 AND bil.bilAdCompany = ?2", companyShortName);
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

	public java.util.Collection findIlByIlNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY, char NEW,
			char UPDATED, char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bil) FROM AdBranchItemLocation bil WHERE (bil.bilItemLocationDownloadStatus = ?3 OR bil.bilItemLocationDownloadStatus = ?4 OR bil.bilItemLocationDownloadStatus = ?5) AND bil.adBranch.brCode = ?1 AND bil.bilAdCompany = ?2");
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

	public java.util.Collection findByBilGlCoaSalesAccount(java.lang.Integer BIL_CODE, java.lang.Integer BIL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bil) FROM AdBranchItemLocation bil WHERE bil.bilCoaGlSalesAccount=?1 AND bil.bilAdCompany = ?2");
			query.setParameter(1, BIL_CODE);
			query.setParameter(2, BIL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByBilGlCoaInventoryAccount(java.lang.Integer BIL_CODE,
			java.lang.Integer BIL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bil) FROM AdBranchItemLocation bil WHERE bil.bilCoaGlInventoryAccount=?1 AND bil.bilAdCompany = ?2");
			query.setParameter(1, BIL_CODE);
			query.setParameter(2, BIL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByBilGlCoaCostOfSalesAccount(java.lang.Integer BIL_CODE,
			java.lang.Integer BIL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bil) FROM AdBranchItemLocation bil WHERE bil.bilCoaGlCostOfSalesAccount=?1 AND bil.bilAdCompany = ?2");
			query.setParameter(1, BIL_CODE);
			query.setParameter(2, BIL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByBilGlCoaWipAccount(java.lang.Integer BIL_CODE, java.lang.Integer BIL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bil) FROM AdBranchItemLocation bil WHERE bil.bilCoaGlWipAccount=?1 AND bil.bilAdCompany = ?2");
			query.setParameter(1, BIL_CODE);
			query.setParameter(2, BIL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByBilGlCoaAccruedInventoryAccount(java.lang.Integer BIL_CODE,
			java.lang.Integer BIL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bil) FROM AdBranchItemLocation bil WHERE bil.bilCoaGlAccruedInventoryAccount=?1 AND bil.bilAdCompany = ?2");
			query.setParameter(1, BIL_CODE);
			query.setParameter(2, BIL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection getBilByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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
	public LocalAdBranchItemLocation buildBranchItemLocation() throws CreateException {
		try {

			LocalAdBranchItemLocation entity = new LocalAdBranchItemLocation();

			Debug.print("AdBranchItemLocationBean buildBranchItemLocation");
			entity.setBilCode(BIL_CODE);
			entity.setBilRack(BIL_RCK);
			entity.setBilBin(BIL_BN);
			entity.setBilReorderPoint(BIL_RRDR_PNT);
			entity.setBilReorderQuantity(BIL_RRDR_QTY);
			entity.setBilCoaGlSalesAccount(BIL_COA_GL_SLS_ACCNT);
			entity.setBilCoaGlInventoryAccount(BIL_COA_GL_INVNTRY_ACCNT);
			entity.setBilCoaGlCostOfSalesAccount(BIL_COA_GL_CST_OF_SLS_ACCNT);
			entity.setBilCoaGlWipAccount(BIL_COA_GL_WIP_ACCNT);
			entity.setBilCoaGlAccruedInventoryAccount(BIL_COA_GL_ACCRD_INVNTRY_ACCNT);
			entity.setBilCoaGlSalesReturnAccount(BIL_COA_GL_SLS_RTRN_ACCNT);
			entity.setBilCoaGlExpenseAccount(BIL_COA_GL_EXPNS_ACCNT);
			entity.setBilSubjectToCommission(BIL_SBJCT_TO_CMMSSN);
			entity.setBilItemDownloadStatus(BIL_DS_ITM);
			entity.setBilLocationDownloadStatus(BIL_DS_LOC);
			entity.setBilItemLocationDownloadStatus(BIL_DS_IL);
			entity.setBilAdCompany(BIL_AD_CMPNY);
			entity.setBilHist1Sales(BIL_HST1_SLS);
			entity.setBilHist2Sales(BIL_HST2_SLS);
			entity.setBilProjectedSales(BIL_PRJ_SLS);
			entity.setBilDeliveryTime(BIL_DLVRY_TME);
			entity.setBilDeliveryBuffer(BIL_DLVRY_BUFF);
			entity.setBilOrderPerYear(BIL_ORDR_PR_YR);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}
	
	public LocalAdBranchItemLocationHome BilRack(String BIL_RCK) {
		this.BIL_RCK = BIL_RCK;
		return this;
	}

	public LocalAdBranchItemLocationHome BilBin(String BIL_BN) {
		this.BIL_BN = BIL_BN;
		return this;
	}

	public LocalAdBranchItemLocationHome BilReorderPoint(double BIL_RRDR_PNT) {
		this.BIL_RRDR_PNT = BIL_RRDR_PNT;
		return this;
	}

	public LocalAdBranchItemLocationHome BilReorderQuantity(double BIL_RRDR_QTY) {
		this.BIL_RRDR_QTY = BIL_RRDR_QTY;
		return this;
	}

	public LocalAdBranchItemLocationHome BilGlCoaSalesAccount(Integer BIL_COA_GL_SLS_ACCNT) {
		this.BIL_COA_GL_SLS_ACCNT = BIL_COA_GL_SLS_ACCNT;
		return this;
	}

	public LocalAdBranchItemLocationHome BilGlCoaInventoryAccount(Integer BIL_COA_GL_INVNTRY_ACCNT) {
		this.BIL_COA_GL_INVNTRY_ACCNT = BIL_COA_GL_INVNTRY_ACCNT;
		return this;
	}

	public LocalAdBranchItemLocationHome BilGlCoaCostOfSalesAccount(Integer BIL_COA_GL_CST_OF_SLS_ACCNT) {
		this.BIL_COA_GL_CST_OF_SLS_ACCNT = BIL_COA_GL_CST_OF_SLS_ACCNT;
		return this;
	}

	public LocalAdBranchItemLocationHome BilGlCoaWipAccount(Integer BIL_COA_GL_WIP_ACCNT) {
		this.BIL_COA_GL_WIP_ACCNT = BIL_COA_GL_WIP_ACCNT;
		return this;
	}

	public LocalAdBranchItemLocationHome BilGlCoaAccruedInventoryAccount(Integer BIL_COA_GL_ACCRD_INVNTRY_ACCNT) {
		this.BIL_COA_GL_ACCRD_INVNTRY_ACCNT = BIL_COA_GL_ACCRD_INVNTRY_ACCNT;
		return this;
	}

	public LocalAdBranchItemLocationHome BilGlCoaSalesReturnAccount(Integer BIL_COA_GL_SLS_RTRN_ACCNT) {
		this.BIL_COA_GL_SLS_RTRN_ACCNT = BIL_COA_GL_SLS_RTRN_ACCNT;
		return this;
	}

	public LocalAdBranchItemLocationHome BilGlCoaExpenseAccount(Integer BIL_COA_GL_EXPNS_ACCNT) {
		this.BIL_COA_GL_EXPNS_ACCNT = BIL_COA_GL_EXPNS_ACCNT;
		return this;
	}

	public LocalAdBranchItemLocationHome BilSubjectToCommission(byte BIL_SBJCT_TO_CMMSSN) {
		this.BIL_SBJCT_TO_CMMSSN = BIL_SBJCT_TO_CMMSSN;
		return this;
	}
	
	public LocalAdBranchItemLocationHome BilItemDownloadStatus(char BIL_DS_ITM) {
		this.BIL_DS_ITM = BIL_DS_ITM;
		return this;
	}
	
	public LocalAdBranchItemLocationHome BilLocationDownloadStatus(char BIL_DS_LOC) {
		this.BIL_DS_LOC = BIL_DS_LOC;
		return this;
	}
	
	public LocalAdBranchItemLocationHome BilItemLocationDownloadStatus(char BIL_DS_IL){
		this.BIL_DS_IL = BIL_DS_IL;
		return this;
	}

	public LocalAdBranchItemLocationHome BilAdCompany(Integer BIL_AD_CMPNY) {
		this.BIL_AD_CMPNY = BIL_AD_CMPNY;
		return this;
	}
	
	public LocalAdBranchItemLocationHome BilHist1Sales(double BIL_HST1_SLS) {
		this.BIL_HST1_SLS = BIL_HST1_SLS;
		return this;
	}
	
	public LocalAdBranchItemLocationHome BilHist2Sales(double BIL_HST2_SLS) {
		this.BIL_HST2_SLS = BIL_HST2_SLS;
		return this;
	}
	
	public LocalAdBranchItemLocationHome BilProjectedSales(double BIL_PRJ_SLS) {
		this.BIL_PRJ_SLS = BIL_PRJ_SLS;
		return this;
	}
	
	public LocalAdBranchItemLocationHome BilDeliveryTime(double BIL_DLVRY_TME) {
		this.BIL_DLVRY_TME = BIL_DLVRY_TME;
		return this;
	}
	
	public LocalAdBranchItemLocationHome BilDeliveryBuffer(double BIL_DLVRY_BUFF) {
		this.BIL_DLVRY_BUFF = BIL_DLVRY_BUFF;
		return this;
	}
	
	public LocalAdBranchItemLocationHome BilOrderPerYear(double BIL_ORDR_PR_YR) {
		this.BIL_ORDR_PR_YR = BIL_ORDR_PR_YR;
		return this;
	}

}