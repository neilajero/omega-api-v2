package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArJobOrderInvoiceLine;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArJobOrderInvoiceLineHome {

	public static final String JNDI_NAME = "LocalArJobOrderInvoiceLineHome!com.ejb.ar.LocalArJobOrderInvoiceLineHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArJobOrderInvoiceLineHome() {
	}

	// FINDER METHODS

	public LocalArJobOrderInvoiceLine findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArJobOrderInvoiceLine entity = (LocalArJobOrderInvoiceLine) em
					.find(new LocalArJobOrderInvoiceLine(), pk);
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
					"SELECT OBJECT(jil) FROM ArJobOrderInvoiceLine jil WHERE jil.arInvoice.invCode=?1 AND jil.jilAdCompany=?2");
			query.setParameter(1, INV_CODE);
			query.setParameter(2, ILI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderInvoiceLineHome.findByInvCode(java.lang.Integer INV_CODE, java.lang.Integer ILI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findjolBySoCodeAndInvAdBranch(java.lang.Integer JO_CODE, java.lang.Integer INV_AD_BRNCH,
			java.lang.Integer JIL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jil) FROM ArJobOrderInvoiceLine jil WHERE jil.arInvoice.invVoid = 0 AND jil.arInvoice.invCreditMemo = 0 AND jil.arJobOrderLine.arJobOrder.joCode = ?1 AND jil.arInvoice.invAdBranch = ?2 AND jil.jilAdCompany = ?3");
			query.setParameter(1, JO_CODE);
			query.setParameter(2, INV_AD_BRNCH);
			query.setParameter(3, JIL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderInvoiceLineHome.findjolBySoCodeAndInvAdBranch(java.lang.Integer JO_CODE, java.lang.Integer INV_AD_BRNCH, java.lang.Integer JIL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedInvcByIiNameAndLocNameAndLessEqualDateAndInvAdBranch(java.lang.String II_NM,
			java.lang.String LOC_NM, java.util.Date INV_DT, java.lang.Integer INV_AD_BRNCH,
			java.lang.Integer JIL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jil) FROM ArJobOrderInvoiceLine jil WHERE jil.arInvoice.invPosted = 0 AND jil.arInvoice.invVoid = 0 AND jil.arInvoice.invCreditMemo = 0 AND jil.arJobOrderLine.invItemLocation.invItem.iiName = ?1 AND jil.arJobOrderLine.invItemLocation.invLocation.locName = ?2 AND jil.arInvoice.invDate <= ?3 AND jil.arInvoice.invAdBranch = ?4 AND jil.jilAdCompany = ?5");
			query.setParameter(1, II_NM);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, INV_DT);
			query.setParameter(4, INV_AD_BRNCH);
			query.setParameter(5, JIL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderInvoiceLineHome.findUnpostedInvcByIiNameAndLocNameAndLessEqualDateAndInvAdBranch(java.lang.String II_NM, java.lang.String LOC_NM, java.com.util.Date INV_DT, java.lang.Integer INV_AD_BRNCH, java.lang.Integer JIL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findCommittedQtyByIiNameAndLocNameAndJoAdBranch(java.lang.String II_NM,
			java.lang.String LOC_NM, java.util.Date INV_DT_FRM, java.util.Date INV_DT_TO, java.lang.Integer JO_AD_BRNCH,
			java.lang.Integer JOL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jil) FROM ArJobOrderInvoiceLine jil WHERE jil.arJobOrderLine.invItemLocation.invItem.iiName = ?1 AND jil.arJobOrderLine.invItemLocation.invLocation.locName = ?2 AND jil.arInvoice.invDate >= ?3 AND jil.arInvoice.invDate <= ?4 AND jil.arInvoice.invAdBranch = ?5 AND jil.jilAdCompany = ?6");
			query.setParameter(1, II_NM);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, INV_DT_FRM);
			query.setParameter(4, INV_DT_TO);
			query.setParameter(5, JO_AD_BRNCH);
			query.setParameter(6, JOL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderInvoiceLineHome.findCommittedQtyByIiNameAndLocNameAndJoAdBranch(java.lang.String II_NM, java.lang.String LOC_NM, java.com.util.Date INV_DT_FRM, java.com.util.Date INV_DT_TO, java.lang.Integer JO_AD_BRNCH, java.lang.Integer JOL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedJoInvByLocNameAndAdBranch(java.lang.String LOC_NM,
			java.lang.Integer INV_AD_BRNCH, java.lang.Integer JIL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jil) FROM ArJobOrderInvoiceLine jil WHERE jil.arInvoice.invPosted = 0 AND jil.arInvoice.invVoid = 0 AND jil.arJobOrderLine.invItemLocation.invLocation.locName = ?1 AND jil.arInvoice.invAdBranch = ?2 AND jil.jilAdCompany = ?3");
			query.setParameter(1, LOC_NM);
			query.setParameter(2, INV_AD_BRNCH);
			query.setParameter(3, JIL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderInvoiceLineHome.findUnpostedJoInvByLocNameAndAdBranch(java.lang.String LOC_NM, java.lang.Integer INV_AD_BRNCH, java.lang.Integer JIL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findJoInvByIiCodeAndLocNameAndAdBranch(java.lang.Integer II_CODE,
			java.lang.String LOC_NM, java.lang.Integer INV_AD_BRNCH, java.lang.Integer JIL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jil) FROM ArJobOrderInvoiceLine jil WHERE jil.arInvoice.invVoid = 0 AND jil.arJobOrderLine.invItemLocation.invItem.iiCode=?1 AND jil.arJobOrderLine.invItemLocation.invLocation.locName = ?2 AND jil.arInvoice.invAdBranch = ?3 AND jil.jilAdCompany = ?4");
			query.setParameter(1, II_CODE);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, INV_AD_BRNCH);
			query.setParameter(4, JIL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderInvoiceLineHome.findJoInvByIiCodeAndLocNameAndAdBranch(java.lang.Integer II_CODE, java.lang.String LOC_NM, java.lang.Integer INV_AD_BRNCH, java.lang.Integer JIL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getJobOrderInvoiceLineByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalArJobOrderInvoiceLine create(Integer JIL_CODE, double JIL_QTY_DLVRD, double JIL_AMNT,
                                             double JIL_TX_AMNT, double JIL_DSCNT_1, double JIL_DSCNT_2, double JIL_DSCNT_3, double JIL_DSCNT_4,
                                             double TTL_JIL_DSCNT, String JIL_IMEI, byte JIL_TX, Integer JIL_AD_CMPNY) throws CreateException {
		try {

			LocalArJobOrderInvoiceLine entity = new LocalArJobOrderInvoiceLine();

			Debug.print("ArJobOrderInvoiceLineBean create");

			entity.setJilCode(JIL_CODE);
			entity.setJilQuantityDelivered(JIL_QTY_DLVRD);
			entity.setJilAmount(JIL_AMNT);
			entity.setJilTaxAmount(JIL_TX_AMNT);
			entity.setJilDiscount1(JIL_DSCNT_1);
			entity.setJilDiscount2(JIL_DSCNT_2);
			entity.setJilDiscount3(JIL_DSCNT_3);
			entity.setJilDiscount4(JIL_DSCNT_4);
			entity.setJilTotalDiscount(TTL_JIL_DSCNT);
			entity.setJilImei(JIL_IMEI);
			entity.setJilTax(JIL_TX);
			entity.setJilAdCompany(JIL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArJobOrderInvoiceLine create(double JIL_QTY_DLVRD, double JIL_AMNT, double JIL_TX_AMNT,
                                             double JIL_DSCNT_1, double JIL_DSCNT_2, double JIL_DSCNT_3, double JIL_DSCNT_4, double TTL_JIL_DSCNT,
                                             String JIL_IMEI, byte JIL_TX, Integer JIL_AD_CMPNY) throws CreateException {
		try {

			LocalArJobOrderInvoiceLine entity = new LocalArJobOrderInvoiceLine();

			Debug.print("ArJobOrderInvoiceLineBean create");

			entity.setJilQuantityDelivered(JIL_QTY_DLVRD);
			entity.setJilAmount(JIL_AMNT);
			entity.setJilTaxAmount(JIL_TX_AMNT);
			entity.setJilDiscount1(JIL_DSCNT_1);
			entity.setJilDiscount2(JIL_DSCNT_2);
			entity.setJilDiscount3(JIL_DSCNT_3);
			entity.setJilDiscount4(JIL_DSCNT_4);
			entity.setJilTotalDiscount(TTL_JIL_DSCNT);
			entity.setJilImei(JIL_IMEI);
			entity.setJilTax(JIL_TX);
			entity.setJilAdCompany(JIL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}