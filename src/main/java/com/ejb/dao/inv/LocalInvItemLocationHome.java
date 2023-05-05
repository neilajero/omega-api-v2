package com.ejb.dao.inv;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.util.Debug;

@SuppressWarnings("ALL")
@Stateless
public class LocalInvItemLocationHome {

	public static final String JNDI_NAME = "LocalInvItemLocationHome!com.ejb.inv.LocalInvItemLocationHome";

	@EJB
	public PersistenceBeanClass em;

	private String IL_RCK = null;
	private String IL_BN = null;
	private double IL_RRDR_PNT = 0d;
	private double IL_RRDR_QTY = 0d;
	private double IL_RRDR_LVL = 0d;
	private double IL_CMMTTD_QTY = 0d;
	private Integer IL_GL_COA_SALES_ACCOUNT = null;
	private Integer IL_GL_COA_INVENTORY_ACCOUNT = null;
	private Integer IL_GL_COA_COST_OF_SALES_ACCOUNT = null;
	private Integer IL_GL_COA_WIP_ACCNT = null;
	private Integer IL_GL_COA_ACCRD_INVNTRY_ACCNT = null;
	private Integer IL_GL_COA_SLS_RTRN_ACCNT = null;
	private Integer IL_GL_COA_EXPNS_ACCNT = null;
	private byte IL_SBJCT_TO_CMMSSN = 0;
	private Integer IL_AD_CMPNY = null;

	public LocalInvItemLocationHome() {
	}

	public LocalInvItemLocation findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvItemLocation entity = (LocalInvItemLocation) em
					.find(new LocalInvItemLocation(), pk);
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

	public LocalInvItemLocation findByLocNameAndIiName(java.lang.String LOC_NM, java.lang.String II_NM,
			java.lang.Integer II_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il WHERE il.invLocation.locName=?1 AND il.invItem.iiName=?2 AND il.ilAdCompany=?3");
			query.setParameter(1, LOC_NM);
			query.setParameter(2, II_NM);
			query.setParameter(3, II_AD_CMPNY);

            return (LocalInvItemLocation) query.getSingleResult();

		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByIiClassAndLocNameAndIiAdLvCategory(java.lang.String II_CLSS,
			java.lang.String LOC_NM, java.lang.String II_AD_LV_CTGRY, java.lang.Integer II_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il WHERE il.invItem.iiClass=?1 AND il.invLocation.locName=?2 AND il.invItem.iiAdLvCategory=?3 AND il.ilAdCompany=?4");
			query.setParameter(1, II_CLSS);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, II_AD_LV_CTGRY);
			query.setParameter(4, II_AD_CMPNY);
            return query.getResultList();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByIlAll(java.lang.Integer II_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(il) FROM InvItemLocation il WHERE il.ilAdCompany=?1");
			query.setParameter(1, II_AD_CMPNY);
            return query.getResultList();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByLocName(java.lang.String LOC_NM, java.lang.Integer II_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il WHERE il.invLocation.locName=?1 AND il.ilAdCompany=?2");
			query.setParameter(1, LOC_NM);
			query.setParameter(2, II_AD_CMPNY);
            return query.getResultList();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findItemByLocNameAndIiAdLvCategory(java.lang.String LOC_NM,
			java.lang.String II_AD_LV_CTGRY, java.lang.Integer IL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il WHERE il.invLocation.locName=?1 AND il.invItem.iiAdLvCategory=?2 AND il.ilAdCompany = ?3");
			query.setParameter(1, LOC_NM);
			query.setParameter(2, II_AD_LV_CTGRY);
			query.setParameter(3, IL_AD_CMPNY);
            return query.getResultList();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByIlGlCoaSalesAccount(java.lang.Integer COA_CODE, java.lang.Integer IL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il WHERE il.ilGlCoaSalesAccount  = ?1 AND il.ilAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, IL_AD_CMPNY);
            return query.getResultList();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByIlGlCoaSalesReturnAccount(java.lang.Integer COA_CODE,
			java.lang.Integer IL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il WHERE il.ilGlCoaSalesReturnAccount  = ?1 AND il.ilAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, IL_AD_CMPNY);
            return query.getResultList();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByIlGlCoaInventoryAccount(java.lang.Integer COA_CODE, java.lang.Integer IL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il WHERE il.ilGlCoaInventoryAccount  = ?1 AND il.ilAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, IL_AD_CMPNY);
            return query.getResultList();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByIlGlCoaCostOfSalesAccount(java.lang.Integer COA_CODE,
			java.lang.Integer IL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il WHERE il.ilGlCoaCostOfSalesAccount  = ?1 AND il.ilAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, IL_AD_CMPNY);
            return query.getResultList();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByIiNameAndIiAdLvCategory(java.lang.String II_NM, java.lang.String II_AD_LV_CTGRY,
			java.lang.Integer II_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il WHERE il.invItem.iiName=?1 AND il.invItem.iiAdLvCategory=?2 AND il.ilAdCompany=?3");
			query.setParameter(1, II_NM);
			query.setParameter(2, II_AD_LV_CTGRY);
			query.setParameter(3, II_AD_CMPNY);
            return query.getResultList();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByIiName(java.lang.String II_NM, java.lang.Integer II_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il WHERE il.invItem.iiName=?1 AND il.ilAdCompany=?2");
			query.setParameter(1, II_NM);
			query.setParameter(2, II_AD_CMPNY);
            return query.getResultList();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByAdBranch(java.lang.Integer AD_BRNCH, java.lang.Integer II_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il, IN(il.adBranchItemLocations)bil WHERE bil.adBranch.brCode=?1 AND il.ilAdCompany=?2");
			query.setParameter(1, AD_BRNCH);
			query.setParameter(2, II_AD_CMPNY);
            return query.getResultList();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findItemByLocNameAndIiAdLvCategoryAndAdBranch(java.lang.String LOC_NM,
			java.lang.String II_AD_LV_CTGRY, java.lang.Integer AD_BRNCH, java.lang.Integer IL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il, IN(il.adBranchItemLocations)bil WHERE il.invItem.iiEnable=1 AND il.invItem.iiNonInventoriable=0 AND il.invLocation.locName=?1 AND il.invItem.iiAdLvCategory=?2 AND bil.adBranch.brCode=?3 AND il.ilAdCompany=?4");
			query.setParameter(1, LOC_NM);
			query.setParameter(2, II_AD_LV_CTGRY);
			query.setParameter(3, AD_BRNCH);
			query.setParameter(4, IL_AD_CMPNY);
            return query.getResultList();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByIiNameAndAdBranch(java.lang.String II_NM, java.lang.Integer AD_BRNCH,
			java.lang.Integer II_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il, IN(il.adBranchItemLocations)bil WHERE il.invItem.iiName=?1 AND bil.adBranch.brCode=?2 AND il.ilAdCompany=?3");
			query.setParameter(1, II_NM);
			query.setParameter(2, AD_BRNCH);
			query.setParameter(3, II_AD_CMPNY);
            return query.getResultList();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByIlReorderPointAndAdBranch(java.lang.Integer IL_RRDR_PNT,
			java.lang.Integer AD_BRNCH, java.lang.Integer II_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il, IN(il.adBranchItemLocations)bil WHERE il.ilReorderPoint > ?1 AND bil.adBranch.brCode=?2 AND il.ilAdCompany=?3");
			query.setParameter(1, IL_RRDR_PNT);
			query.setParameter(2, AD_BRNCH);
			query.setParameter(3, II_AD_CMPNY);
            return query.getResultList();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalInvItemLocation findByIiNameAndLocName(java.lang.String II_NM, java.lang.String LOC_NM,
			java.lang.Integer II_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il WHERE il.invItem.iiName = ?1 AND il.invLocation.locName = ?2 AND il.ilAdCompany = ?3");
			query.setParameter(1, II_NM);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, II_AD_CMPNY);
            return (LocalInvItemLocation) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalInvItemLocation findByIiNameAndLocNameAndAdBranch(java.lang.String II_NM, java.lang.String LOC_NM,
			java.lang.Integer AD_BRNCH, java.lang.Integer II_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il, IN(il.adBranchItemLocations)bil WHERE il.invItem.iiName = ?1 AND il.invLocation.locName = ?2 AND bil.adBranch.brCode=?3 AND  il.ilAdCompany = ?4");
			query.setParameter(1, II_NM);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, AD_BRNCH);
			query.setParameter(4, II_AD_CMPNY);
            return (LocalInvItemLocation) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalInvItemLocation findByIiCodeAndLocName(java.lang.Integer II_CD, java.lang.String LOC_NM,
			java.lang.Integer II_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il WHERE il.invItem.iiCode = ?1 AND il.invLocation.locName = ?2 AND il.ilAdCompany = ?3");
			query.setParameter(1, II_CD);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, II_AD_CMPNY);
            return (LocalInvItemLocation) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByIlGlCoaWipAccount(java.lang.Integer COA_CODE, java.lang.Integer IL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il WHERE il.ilGlCoaWipAccount  = ?1 AND il.ilAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, IL_AD_CMPNY);
            return query.getResultList();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByIlGlCoaAccruedInventoryAccount(java.lang.Integer COA_CODE,
			java.lang.Integer IL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM InvItemLocation il WHERE il.ilGlCoaAccruedInventoryAccount  = ?1 AND il.ilAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, IL_AD_CMPNY);
            return query.getResultList();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection getIlByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS
	public LocalInvItemLocation buildItemLocation() throws CreateException {

		try {

			LocalInvItemLocation entity = new LocalInvItemLocation();

			Debug.print("InvItemLocationBean buildItemLocation");

			entity.setIlRack(IL_RCK);
			entity.setIlBin(IL_BN);
			entity.setIlReorderPoint(IL_RRDR_PNT);
			entity.setIlReorderQuantity(IL_RRDR_QTY);
			entity.setIlReorderLevel(IL_RRDR_LVL);
			entity.setIlCommittedQuantity(IL_CMMTTD_QTY);
			entity.setIlGlCoaSalesAccount(IL_GL_COA_SALES_ACCOUNT);
			entity.setIlGlCoaInventoryAccount(IL_GL_COA_INVENTORY_ACCOUNT);
			entity.setIlGlCoaCostOfSalesAccount(IL_GL_COA_COST_OF_SALES_ACCOUNT);
			entity.setIlGlCoaWipAccount(IL_GL_COA_WIP_ACCNT);
			entity.setIlGlCoaAccruedInventoryAccount(IL_GL_COA_ACCRD_INVNTRY_ACCNT);
			entity.setIlGlCoaSalesReturnAccount(IL_GL_COA_SLS_RTRN_ACCNT);
			entity.setIlGlCoaExpenseAccount(IL_GL_COA_EXPNS_ACCNT);
			entity.setIlSubjectToCommission(IL_SBJCT_TO_CMMSSN);
			entity.setIlAdCompany(IL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvItemLocationHome IlRack(String IL_RCK) {
		this.IL_RCK = IL_RCK;
		return this;
	}

	public LocalInvItemLocationHome IlBin(String IL_BN) {
		this.IL_BN = IL_BN;
		return this;
	}

	public LocalInvItemLocationHome IlReorderPoint(double IL_RRDR_PNT) {
		this.IL_RRDR_PNT = IL_RRDR_PNT;
		return this;
	}

	public LocalInvItemLocationHome IlReorderQuantity(double IL_RRDR_QTY) {
		this.IL_RRDR_QTY = IL_RRDR_QTY;
		return this;
	}

	public LocalInvItemLocationHome IlReorderLevel(double IL_RRDR_LVL) {
		this.IL_RRDR_LVL = IL_RRDR_LVL;
		return this;
	}

	public LocalInvItemLocationHome IlCommittedQuantity(double IL_CMMTTD_QTY) {
		this.IL_CMMTTD_QTY = IL_CMMTTD_QTY;
		return this;
	}

	public LocalInvItemLocationHome IlGlCoaSalesAccount(Integer IL_GL_COA_SALES_ACCOUNT) {
		this.IL_GL_COA_SALES_ACCOUNT = IL_GL_COA_SALES_ACCOUNT;
		return this;
	}

	public LocalInvItemLocationHome IlGlCoaInventoryAccount(Integer IL_GL_COA_INVENTORY_ACCOUNT) {
		this.IL_GL_COA_INVENTORY_ACCOUNT = IL_GL_COA_INVENTORY_ACCOUNT;
		return this;
	}

	public LocalInvItemLocationHome IlGlCoaCostOfSalesAccount(Integer IL_GL_COA_COST_OF_SALES_ACCOUNT) {
		this.IL_GL_COA_COST_OF_SALES_ACCOUNT = IL_GL_COA_COST_OF_SALES_ACCOUNT;
		return this;
	}

	public LocalInvItemLocationHome IlGlCoaWipAccount(Integer IL_GL_COA_WIP_ACCNT) {
		this.IL_GL_COA_WIP_ACCNT = IL_GL_COA_WIP_ACCNT;
		return this;
	}

	public LocalInvItemLocationHome IlGlCoaAccruedInventoryAccount(Integer IL_GL_COA_ACCRD_INVNTRY_ACCNT) {
		this.IL_GL_COA_ACCRD_INVNTRY_ACCNT = IL_GL_COA_ACCRD_INVNTRY_ACCNT;
		return this;
	}

	public LocalInvItemLocationHome IlGlCoaSalesReturnAccount(Integer IL_GL_COA_SLS_RTRN_ACCNT) {
		this.IL_GL_COA_SLS_RTRN_ACCNT = IL_GL_COA_SLS_RTRN_ACCNT;
		return this;
	}

	public LocalInvItemLocationHome IlGlCoaExpenseAccount(Integer IL_GL_COA_EXPNS_ACCNT) {
		this.IL_GL_COA_EXPNS_ACCNT = IL_GL_COA_EXPNS_ACCNT;
		return this;
	}

	public LocalInvItemLocationHome IlSubjectToCommission(byte IL_SBJCT_TO_CMMSSN) {
		this.IL_SBJCT_TO_CMMSSN = IL_SBJCT_TO_CMMSSN;
		return this;
	}

	public LocalInvItemLocationHome IlAdCompany(Integer IL_AD_CMPNY) {
		this.IL_AD_CMPNY = IL_AD_CMPNY;
		return this;
	}

}