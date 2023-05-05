package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArSalesOrderInvoiceLine;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArSalesOrderInvoiceLineHome {

	public static final String JNDI_NAME = "LocalArSalesOrderInvoiceLineHome!com.ejb.ar.LocalArSalesOrderInvoiceLineHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArSalesOrderInvoiceLineHome() {
	}

	// FINDER METHODS

	public LocalArSalesOrderInvoiceLine findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArSalesOrderInvoiceLine entity = (LocalArSalesOrderInvoiceLine) em
					.find(new LocalArSalesOrderInvoiceLine(), pk);
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

	public java.util.Collection findByInvCode(java.lang.Integer INV_CODE, java.lang.Integer ILI_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sil) FROM ArSalesOrderInvoiceLine sil WHERE sil.arInvoice.invCode=?1 AND sil.silAdCompany=?2");
			query.setParameter(1, INV_CODE);
			query.setParameter(2, ILI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderInvoiceLineHome.findByInvCode(java.lang.Integer INV_CODE, java.lang.Integer ILI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findSolBySoCodeAndInvAdBranch(java.lang.Integer SO_CODE, java.lang.Integer INV_AD_BRNCH,
			java.lang.Integer SIL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sil) FROM ArSalesOrderInvoiceLine sil WHERE sil.arInvoice.invVoid = 0 AND sil.arInvoice.invCreditMemo = 0 AND sil.arSalesOrderLine.arSalesOrder.soCode = ?1 AND sil.arInvoice.invAdBranch = ?2 AND sil.silAdCompany = ?3");
			query.setParameter(1, SO_CODE);
			query.setParameter(2, INV_AD_BRNCH);
			query.setParameter(3, SIL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderInvoiceLineHome.findSolBySoCodeAndInvAdBranch(java.lang.Integer SO_CODE, java.lang.Integer INV_AD_BRNCH, java.lang.Integer SIL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedInvcByIiNameAndLocNameAndLessEqualDateAndInvAdBranch(java.lang.String II_NM,
			java.lang.String LOC_NM, java.util.Date INV_DT, java.lang.Integer INV_AD_BRNCH,
			java.lang.Integer SIL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sil) FROM ArSalesOrderInvoiceLine sil WHERE sil.arInvoice.invPosted = 0 AND sil.arInvoice.invVoid = 0 AND sil.arInvoice.invCreditMemo = 0 AND sil.arSalesOrderLine.invItemLocation.invItem.iiName = ?1 AND sil.arSalesOrderLine.invItemLocation.invLocation.locName = ?2 AND sil.arInvoice.invDate <= ?3 AND sil.arInvoice.invAdBranch = ?4 AND sil.silAdCompany = ?5");
			query.setParameter(1, II_NM);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, INV_DT);
			query.setParameter(4, INV_AD_BRNCH);
			query.setParameter(5, SIL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderInvoiceLineHome.findUnpostedInvcByIiNameAndLocNameAndLessEqualDateAndInvAdBranch(java.lang.String II_NM, java.lang.String LOC_NM, java.com.util.Date INV_DT, java.lang.Integer INV_AD_BRNCH, java.lang.Integer SIL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findCommittedQtyByIiNameAndLocNameAndSoAdBranch(java.lang.String II_NM,
			java.lang.String LOC_NM, java.util.Date INV_DT_FRM, java.util.Date INV_DT_TO, java.lang.Integer SO_AD_BRNCH,
			java.lang.Integer SOL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sil) FROM ArSalesOrderInvoiceLine sil WHERE sil.arSalesOrderLine.invItemLocation.invItem.iiName = ?1 AND sil.arSalesOrderLine.invItemLocation.invLocation.locName = ?2 AND sil.arInvoice.invDate >= ?3 AND sil.arInvoice.invDate <= ?4 AND sil.arInvoice.invAdBranch = ?5 AND sil.silAdCompany = ?6");
			query.setParameter(1, II_NM);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, INV_DT_FRM);
			query.setParameter(4, INV_DT_TO);
			query.setParameter(5, SO_AD_BRNCH);
			query.setParameter(6, SOL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderInvoiceLineHome.findCommittedQtyByIiNameAndLocNameAndSoAdBranch(java.lang.String II_NM, java.lang.String LOC_NM, java.com.util.Date INV_DT_FRM, java.com.util.Date INV_DT_TO, java.lang.Integer SO_AD_BRNCH, java.lang.Integer SOL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedSoInvByLocNameAndAdBranch(java.lang.String LOC_NM,
			java.lang.Integer INV_AD_BRNCH, java.lang.Integer SIL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sil) FROM ArSalesOrderInvoiceLine sil WHERE sil.arInvoice.invPosted = 0 AND sil.arInvoice.invVoid = 0 AND sil.arSalesOrderLine.invItemLocation.invLocation.locName = ?1 AND sil.arInvoice.invAdBranch = ?2 AND sil.silAdCompany = ?3");
			query.setParameter(1, LOC_NM);
			query.setParameter(2, INV_AD_BRNCH);
			query.setParameter(3, SIL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderInvoiceLineHome.findUnpostedSoInvByLocNameAndAdBranch(java.lang.String LOC_NM, java.lang.Integer INV_AD_BRNCH, java.lang.Integer SIL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findSoInvByIiCodeAndLocNameAndAdBranch(java.lang.Integer II_CODE,
			java.lang.String LOC_NM, java.lang.Integer INV_AD_BRNCH, java.lang.Integer SIL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sil) FROM ArSalesOrderInvoiceLine sil WHERE sil.arInvoice.invVoid = 0 AND sil.arSalesOrderLine.invItemLocation.invItem.iiCode=?1 AND sil.arSalesOrderLine.invItemLocation.invLocation.locName = ?2 AND sil.arInvoice.invAdBranch = ?3 AND sil.silAdCompany = ?4");
			query.setParameter(1, II_CODE);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, INV_AD_BRNCH);
			query.setParameter(4, SIL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderInvoiceLineHome.findSoInvByIiCodeAndLocNameAndAdBranch(java.lang.Integer II_CODE, java.lang.String LOC_NM, java.lang.Integer INV_AD_BRNCH, java.lang.Integer SIL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getSalesOrderInvoiceLineByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalArSalesOrderInvoiceLine create(Integer AR_SIL_CODE, double SIL_QTY_DLVRD, double SIL_AMNT,
                                               double SIL_TX_AMNT, double SIL_DSCNT_1, double SIL_DSCNT_2, double SIL_DSCNT_3, double SIL_DSCNT_4,
                                               double TTL_SIL_DSCNT, String SIL_IMEI, byte SIL_TX, Integer SIL_AD_CMPNY) throws CreateException {
		try {

			LocalArSalesOrderInvoiceLine entity = new LocalArSalesOrderInvoiceLine();

			Debug.print("ArSalesOrderInvoiceLineBean create");

			entity.setSilCode(AR_SIL_CODE);
			entity.setSilQuantityDelivered(SIL_QTY_DLVRD);
			entity.setSilAmount(SIL_AMNT);
			entity.setSilTaxAmount(SIL_TX_AMNT);
			entity.setSilDiscount1(SIL_DSCNT_1);
			entity.setSilDiscount2(SIL_DSCNT_2);
			entity.setSilDiscount3(SIL_DSCNT_3);
			entity.setSilDiscount4(SIL_DSCNT_4);
			entity.setSilTotalDiscount(TTL_SIL_DSCNT);
			entity.setSilImei(SIL_IMEI);
			entity.setSilTax(SIL_TX);
			entity.setSilAdCompany(SIL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArSalesOrderInvoiceLine create(double SIL_QTY_DLVRD, double SIL_AMNT, double SIL_TX_AMNT,
                                               double SIL_DSCNT_1, double SIL_DSCNT_2, double SIL_DSCNT_3, double SIL_DSCNT_4, double TTL_SIL_DSCNT,
                                               String SIL_IMEI, byte SIL_TX, Integer SIL_AD_CMPNY) throws CreateException {
		try {

			LocalArSalesOrderInvoiceLine entity = new LocalArSalesOrderInvoiceLine();

			Debug.print("ArSalesOrderInvoiceLineBean create");

			entity.setSilQuantityDelivered(SIL_QTY_DLVRD);
			entity.setSilAmount(SIL_AMNT);
			entity.setSilTaxAmount(SIL_TX_AMNT);
			entity.setSilDiscount1(SIL_DSCNT_1);
			entity.setSilDiscount2(SIL_DSCNT_2);
			entity.setSilDiscount3(SIL_DSCNT_3);
			entity.setSilDiscount4(SIL_DSCNT_4);
			entity.setSilTotalDiscount(TTL_SIL_DSCNT);
			entity.setSilImei(SIL_IMEI);
			entity.setSilTax(SIL_TX);
			entity.setSilAdCompany(SIL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}