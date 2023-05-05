package com.ejb.dao.inv;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvStockTransferLine;
import com.util.Debug;

@SuppressWarnings("ALL")
@Stateless
public class LocalInvStockTransferLineHome {

	public static final String JNDI_NAME = "LocalInvStockTransferLineHome!com.ejb.inv.LocalInvStockTransferLineHome";

	@EJB
	public PersistenceBeanClass em;
	private java.lang.Integer STL_LCTN_FRM = null;
	private java.lang.Integer STL_LCTN_TO = null;
	private double STL_UNT_CST = 0d;
	private double STL_QTY_DLVRD= 0d;
	private double STL_AMNT = 0d;
	private java.lang.Integer STL_AD_CMPNY = null;

	public LocalInvStockTransferLineHome() {
	}

	// FINDER METHODS

	public LocalInvStockTransferLine findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvStockTransferLine entity = (LocalInvStockTransferLine) em
					.find(new LocalInvStockTransferLine(), pk);
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

	public java.util.Collection findByIiCodeAndLocCodeAndAdBranch(java.lang.Integer II_CODE, java.lang.Integer LOC_CODE,
			java.lang.Integer ST_AD_BRNCH, java.lang.Integer STL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(stl) FROM InvStockTransferLine stl WHERE stl.invStockTransfer.stPosted = 0 AND stl.invItem.iiName = ?1 AND (stl.stlLocationFrom = ?2 OR stl.stlLocationTo = ?2) AND stl.invStockTransfer.stDate <= ?3 AND stl.invStockTransfer.stAdBranch=?4 AND stl.stlAdCompany = ?5");
			query.setParameter(1, II_CODE);
			query.setParameter(2, LOC_CODE);
			query.setParameter(3, ST_AD_BRNCH);
			query.setParameter(4, STL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvStockTransferLineHome.findByIiCodeAndLocCodeAndAdBranch(java.lang.Integer II_CODE, java.lang.Integer LOC_CODE, java.lang.Integer ST_AD_BRNCH, java.lang.Integer STL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedStByLocCodeAndAdBranch(java.lang.Integer LOC_CODE,
			java.lang.Integer ST_AD_BRNCH, java.lang.Integer STL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(stl) FROM InvStockTransferLine stl WHERE stl.invStockTransfer.stPosted = 0 AND stl.stlLocationFrom=?1 AND stl.invStockTransfer.stAdBranch = ?2 AND stl.stlAdCompany = ?3");
			query.setParameter(1, LOC_CODE);
			query.setParameter(2, ST_AD_BRNCH);
			query.setParameter(3, STL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvStockTransferLineHome.findUnpostedStByLocCodeAndAdBranch(java.lang.Integer LOC_CODE, java.lang.Integer ST_AD_BRNCH, java.lang.Integer STL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedStByLocToCodeAndAdBranch(java.lang.Integer LOC_CODE,
			java.lang.Integer ST_AD_BRNCH, java.lang.Integer STL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(stl) FROM InvStockTransferLine stl WHERE stl.invStockTransfer.stPosted = 0 AND stl.stlLocationTo=?1 AND stl.invStockTransfer.stAdBranch = ?2 AND stl.stlAdCompany = ?3");
			query.setParameter(1, LOC_CODE);
			query.setParameter(2, ST_AD_BRNCH);
			query.setParameter(3, STL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvStockTransferLineHome.findUnpostedStByLocToCodeAndAdBranch(java.lang.Integer LOC_CODE, java.lang.Integer ST_AD_BRNCH, java.lang.Integer STL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByStCode(java.lang.Integer INV_ST_CODE, java.lang.Integer STL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(stl) FROM InvStockTransferLine stl WHERE stl.invStockTransfer.stCode=?1 AND stl.stlAdCompany=?2");
			query.setParameter(1, INV_ST_CODE);
			query.setParameter(2, STL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvStockTransferLineHome.findByStCode(java.lang.Integer INV_ST_CODE, java.lang.Integer STL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedStlByIiNameAndLocCodeAndLessEqualDateAndStAdBranch(java.lang.String II_NM,
			java.lang.Integer LOC_CODE, java.util.Date ST_DT, java.lang.Integer ST_AD_BRNCH,
			java.lang.Integer STL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(stl) FROM InvStockTransferLine stl WHERE stl.invStockTransfer.stPosted = 0 AND stl.invItem.iiName = ?1 AND (stl.stlLocationFrom = ?2 OR stl.stlLocationTo = ?2) AND stl.invStockTransfer.stDate <= ?3 AND stl.invStockTransfer.stAdBranch=?4 AND stl.stlAdCompany = ?5");
			query.setParameter(1, II_NM);
			query.setParameter(2, LOC_CODE);
			query.setParameter(3, ST_DT);
			query.setParameter(4, ST_AD_BRNCH);
			query.setParameter(5, STL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvStockTransferLineHome.findUnpostedStlByIiNameAndLocCodeAndLessEqualDateAndStAdBranch(java.lang.String II_NM, java.lang.Integer LOC_CODE, java.com.util.Date ST_DT, java.lang.Integer ST_AD_BRNCH, java.lang.Integer STL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByStPostedAndStlLocationFromAndIiCodeAndBrCode(byte ST_PSTD,
			java.lang.Integer STL_LCTN_FRM, java.lang.Integer II_CODE, java.lang.Integer ST_AD_BRNCH,
			java.lang.Integer STL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(stl) FROM InvStockTransferLine stl WHERE stl.invStockTransfer.stPosted=?1 AND stl.stlLocationFrom=?2 AND stl.invItem.iiCode=?3 AND stl.invStockTransfer.stAdBranch=?4 AND stl.stlAdCompany=?5");
			query.setParameter(1, ST_PSTD);
			query.setParameter(2, STL_LCTN_FRM);
			query.setParameter(3, II_CODE);
			query.setParameter(4, ST_AD_BRNCH);
			query.setParameter(5, STL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvStockTransferLineHome.findByStPostedAndStlLocationFromAndIiCodeAndBrCode(byte ST_PSTD, java.lang.Integer STL_LCTN_FRM, java.lang.Integer II_CODE, java.lang.Integer ST_AD_BRNCH, java.lang.Integer STL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getStlByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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
	public LocalInvStockTransferLine build() throws CreateException {
		try {

			LocalInvStockTransferLine entity = new LocalInvStockTransferLine();

			Debug.print("invStockTransferLineBean build");

			entity.setStlLocationFrom(STL_LCTN_FRM);
			entity.setStlLocationTo(STL_LCTN_TO);
			entity.setStlUnitCost(STL_UNT_CST);
			entity.setStlQuantityDelivered(STL_QTY_DLVRD);
			entity.setStlAmount(STL_AMNT);
			entity.setStlAdCompany(STL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvStockTransferLineHome StlLocationFrom(Integer STL_LCTN_FRM) {
		this.STL_LCTN_FRM = STL_LCTN_FRM;
		return this;
	}

	public LocalInvStockTransferLineHome StlLocationTo(Integer STL_LCTN_TO) {
		this.STL_LCTN_TO = STL_LCTN_TO;
		return this;
	}

	public LocalInvStockTransferLineHome StlUnitCost(double STL_UNT_CST) {
		this.STL_UNT_CST = STL_UNT_CST;
		return this;
	}

	public LocalInvStockTransferLineHome StlQuantityDelivered(double STL_QTY_DLVRD) {
		this.STL_QTY_DLVRD = STL_QTY_DLVRD;
		return this;
	}

	public LocalInvStockTransferLineHome StlAmount(double STL_AMNT) {
		this.STL_AMNT = STL_AMNT;
		return this;
	}

	public LocalInvStockTransferLineHome StlAdCompany(Integer STL_AD_CMPNY) {
		this.STL_AD_CMPNY = STL_AD_CMPNY;
		return this;
	}

	public LocalInvStockTransferLine create(java.lang.Integer STL_CODE, java.lang.Integer STL_LCTN_FRM,
                                            java.lang.Integer STL_LCTN_TO, double STL_UNT_CST, double STL_QTY_DLVRD, double STL_AMNT,
                                            java.lang.Integer STL_AD_CMPNY) throws CreateException {
		try {

			LocalInvStockTransferLine entity = new LocalInvStockTransferLine();

			Debug.print("invStockTransferLineBean create");

			entity.setStlCode(STL_CODE);
			entity.setStlLocationFrom(STL_LCTN_FRM);
			entity.setStlLocationTo(STL_LCTN_TO);
			entity.setStlUnitCost(STL_UNT_CST);
			entity.setStlQuantityDelivered(STL_QTY_DLVRD);
			entity.setStlAmount(STL_AMNT);
			entity.setStlAdCompany(STL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvStockTransferLine create(java.lang.Integer STL_LCTN_FRM, java.lang.Integer STL_LCTN_TO,
                                            double STL_UNT_CST, double STL_QTY_DLVRD, double STL_AMNT, java.lang.Integer STL_AD_CMPNY)
			throws CreateException {
		try {

			LocalInvStockTransferLine entity = new LocalInvStockTransferLine();

			Debug.print("invStockTransferLineBean create");

			entity.setStlLocationFrom(STL_LCTN_FRM);
			entity.setStlLocationTo(STL_LCTN_TO);
			entity.setStlUnitCost(STL_UNT_CST);
			entity.setStlQuantityDelivered(STL_QTY_DLVRD);
			entity.setStlAmount(STL_AMNT);
			entity.setStlAdCompany(STL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}