package com.ejb.dao.inv;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvTag;
import com.util.Debug;

@Stateless
public class LocalInvTagHome {

	public static final String JNDI_NAME = "LocalInvTagHome!com.ejb.inv.LocalInvTagHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalInvTagHome() {
	}

	// FINDER METHODS

	public LocalInvTag findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvTag entity = (LocalInvTag) em.find(new LocalInvTag(), pk);
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

	public java.util.Collection findTgSerialNumberBySilCode(java.lang.String TG_SRL_NMBR, java.lang.Integer AR_SIL_CODE)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.tgSerialNumber=?1 AND tg.arSalesOrderInvoiceLine.silCode=?2");
			query.setParameter(1, TG_SRL_NMBR);
			query.setParameter(2, AR_SIL_CODE);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findTgSerialNumberBySilCode(java.lang.String TG_SRL_NMBR, java.lang.Integer AR_SIL_CODE )");
			throw ex;
		}
	}

	public java.util.Collection findTgSerialNumberByIliCode(java.lang.String TG_SRL_NMBR, java.lang.Integer ILI_CODE)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.tgSerialNumber=?1 AND tg.arInvoiceLineItem.iliCode=?2");
			query.setParameter(1, TG_SRL_NMBR);
			query.setParameter(2, ILI_CODE);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findTgSerialNumberByIliCode(java.lang.String TG_SRL_NMBR, java.lang.Integer ILI_CODE )");
			throw ex;
		}
	}

	public java.util.Collection findTgSerialNumberByJilCode(java.lang.String TG_SRL_NMBR, java.lang.Integer JIL_CODE)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.tgSerialNumber=?1 AND tg.arJobOrderInvoiceLine.jilCode=?2");
			query.setParameter(1, TG_SRL_NMBR);
			query.setParameter(2, JIL_CODE);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findTgSerialNumberByJilCode(java.lang.String TG_SRL_NMBR, java.lang.Integer JIL_CODE )");
			throw ex;
		}
	}

	public java.util.Collection findByTgCode(java.lang.Integer TG_CODE, java.lang.Integer TG_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(tg) FROM InvTag tg WHERE tg.tgCode=?1 AND tg.tgAdCompany=?2");
			query.setParameter(1, TG_CODE);
			query.setParameter(2, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findByTgCode(java.lang.Integer TG_CODE, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByTgSpecs(java.lang.String TG_SPCS, java.lang.Integer TG_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(tg) FROM InvTag tg WHERE tg.tgSpecs=?1 AND tg.tgAdCompany=?2");
			query.setParameter(1, TG_SPCS);
			query.setParameter(2, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findByTgSpecs(java.lang.String TG_SPCS, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByTgPropertyCode(java.lang.String TG_PRPRTY_CD, java.lang.Integer TG_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(tg) FROM InvTag tg WHERE tg.tgPropertyCode=?1 AND tg.tgAdCompany=?2");
			query.setParameter(1, TG_PRPRTY_CD);
			query.setParameter(2, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findByTgPropertyCode(java.lang.String TG_PRPRTY_CD, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByPlCode(java.lang.Integer AP_PURCHASE_ORDER_LINE, java.lang.Integer TG_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.apPurchaseOrderLine.plCode=?1 AND tg.tgAdCompany=?2");
			query.setParameter(1, AP_PURCHASE_ORDER_LINE);
			query.setParameter(2, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findByPlCode(java.lang.Integer AP_PURCHASE_ORDER_LINE, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAllByPlCodeNotNull(java.lang.Integer TG_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.apPurchaseOrderLine.plCode IS NOT NULL AND tg.tgAdCompany=?1");
			query.setParameter(1, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findAllByPlCodeNotNull(java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAllPlReceiving(java.lang.Integer TG_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.apPurchaseOrderLine.plCode IS NOT NULL AND tg.apPurchaseOrderLine.apPurchaseOrder.poReceiving = 1 AND tg.tgAdCompany=?1");
			query.setParameter(1, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findAllPlReceiving(java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAllPlReceivingByItem(java.lang.String II_NM, java.lang.Integer TG_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.apPurchaseOrderLine.apPurchaseOrder.poReceiving=1 AND tg.apPurchaseOrderLine.invItemLocation.invItem.iiName=?1 AND tg.tgAdCompany=?2 ORDER BY tg.tgSerialNumber ASC");
			query.setParameter(1, II_NM);
			query.setParameter(2, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findAllPlReceivingByItem(java.lang.String II_NM, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByTgDocumentNumber(java.lang.String TG_DCMNT_NMBR, java.lang.Integer TG_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(tg) FROM InvTag tg WHERE tg.tgDocumentNumber=?1 AND tg.tgAdCompany=?2");
			query.setParameter(1, TG_DCMNT_NMBR);
			query.setParameter(2, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findByTgDocumentNumber(java.lang.String TG_DCMNT_NMBR, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findTgSerialNumberByPlCode(java.lang.String TG_SRL_NMBR, java.lang.Integer AP_PL_CODE)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.tgSerialNumber=?1 AND tg.apPurchaseOrderLine.plCode=?2");
			query.setParameter(1, TG_SRL_NMBR);
			query.setParameter(2, AP_PL_CODE);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findTgSerialNumberByPlCode(java.lang.String TG_SRL_NMBR, java.lang.Integer AP_PL_CODE )");
			throw ex;
		}
	}

	public LocalInvTag findByPlCodeTemp(java.lang.Integer AP_PURCHASE_ORDER_LINE, java.lang.Integer TG_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.apPurchaseOrderLine.plCode=?1 AND tg.tgAdCompany=?2");
			query.setParameter(1, AP_PURCHASE_ORDER_LINE);
			query.setParameter(2, TG_AD_CMPNY);
            return (LocalInvTag) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvTagHome.findByPlCodeTemp(java.lang.Integer AP_PURCHASE_ORDER_LINE, java.lang.Integer TG_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findByPlCodeTemp(java.lang.Integer AP_PURCHASE_ORDER_LINE, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByPrlCode(java.lang.Integer AP_PURCHASE_REQUISITION_LINE,
			java.lang.Integer TG_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.apPurchaseRequisitionLine.prlCode=?1 AND tg.tgAdCompany=?2");
			query.setParameter(1, AP_PURCHASE_REQUISITION_LINE);
			query.setParameter(2, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findByPrlCode(java.lang.Integer AP_PURCHASE_REQUISITION_LINE, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBslCode(java.lang.Integer INV_BRANCH_STOCK_TRANSFER_LINE,
			java.lang.Integer TG_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.invBranchStockTransferLine.bslCode=?1 AND tg.tgAdCompany=?2");
			query.setParameter(1, INV_BRANCH_STOCK_TRANSFER_LINE);
			query.setParameter(2, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findByBslCode(java.lang.Integer INV_BRANCH_STOCK_TRANSFER_LINE, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAlCode(java.lang.Integer INV_ADJUSTMENT_LINE, java.lang.Integer TG_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.invAdjustmentLine.alCode=?1 AND tg.tgAdCompany=?2");
			query.setParameter(1, INV_ADJUSTMENT_LINE);
			query.setParameter(2, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findByAlCode(java.lang.Integer INV_ADJUSTMENT_LINE, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findTgAll(java.lang.Integer TG_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(tg) FROM InvTag tg WHERE tg.tgAdCompany=?1");
			query.setParameter(1, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findTgAll(java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByLessThanOrEqualToTransactionDateAndItemLocationAndAdBranchFromAdjLine(
			java.util.Date TG_TXN_DT, java.lang.Integer ITM_LCTN_CD, java.lang.Integer AD_BRNCH_CD,
			java.lang.Integer TG_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.invAdjustmentLine IS NOT NULL AND tg.tgTransactionDate<=?1 AND ((tg.invAdjustmentLine.invItemLocation.ilCode=?2 AND tg.invAdjustmentLine.invAdjustment.adjAdBranch=?3)) AND tg.tgAdCompany=?4 ORDER BY tg.tgPropertyCode DESC");
			query.setParameter(1, TG_TXN_DT);
			query.setParameter(2, ITM_LCTN_CD);
			query.setParameter(3, AD_BRNCH_CD);
			query.setParameter(4, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findByLessThanOrEqualToTransactionDateAndItemLocationAndAdBranchFromAdjLine(java.com.util.Date TG_TXN_DT, java.lang.Integer ITM_LCTN_CD, java.lang.Integer AD_BRNCH_CD, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByLessThanOrEqualToTransactionDateAndItemLocationAndAdBranchFromPOLine(
			java.util.Date TG_TXN_DT, java.lang.Integer ITM_LCTN_CD, java.lang.Integer AD_BRNCH_CD,
			java.lang.Integer TG_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.apPurchaseOrderLine IS NOT NULL AND tg.tgTransactionDate<=?1 AND ((tg.apPurchaseOrderLine.invItemLocation.ilCode=?2 AND tg.apPurchaseOrderLine.apPurchaseOrder.poAdBranch=?3)) AND tg.tgAdCompany=?4 ORDER BY tg.tgPropertyCode DESC");
			query.setParameter(1, TG_TXN_DT);
			query.setParameter(2, ITM_LCTN_CD);
			query.setParameter(3, AD_BRNCH_CD);
			query.setParameter(4, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findByLessThanOrEqualToTransactionDateAndItemLocationAndAdBranchFromPOLine(java.com.util.Date TG_TXN_DT, java.lang.Integer ITM_LCTN_CD, java.lang.Integer AD_BRNCH_CD, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByLessThanOrEqualToTransactionDateAndItemLocationAndAdBranchFromPORecevingLine(
			java.util.Date TG_TXN_DT, java.lang.Integer ITM_LCTN_CD, java.lang.Integer AD_BRNCH_CD,
			java.lang.Integer TG_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.apPurchaseOrderLine IS NOT NULL AND ((tg.apPurchaseOrderLine.apPurchaseOrder.poReceiving=1 AND tg.apPurchaseOrderLine.invItemLocation.ilCode=?2 AND tg.apPurchaseOrderLine.apPurchaseOrder.poAdBranch=?3)) AND tg.tgAdCompany=?4 ORDER BY tg.tgPropertyCode DESC");
			query.setParameter(1, TG_TXN_DT);
			query.setParameter(2, ITM_LCTN_CD);
			query.setParameter(3, AD_BRNCH_CD);
			query.setParameter(4, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findByLessThanOrEqualToTransactionDateAndItemLocationAndAdBranchFromPORecevingLine(java.com.util.Date TG_TXN_DT, java.lang.Integer ITM_LCTN_CD, java.lang.Integer AD_BRNCH_CD, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByTgPropertyCodeAndItemLocNameFromPOLine(java.lang.String TG_PRPRTY_CD,
			java.lang.String ITM_LCTN_NM, java.lang.Integer AD_BRNCH_CD, java.lang.Integer TG_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.tgPropertyCode=?1 AND (tg.apPurchaseOrderLine.invItemLocation.invLocation.locName=?2 AND tg.apPurchaseOrderLine.apPurchaseOrder.poAdBranch=?3) AND tg.tgAdCompany=?4");
			query.setParameter(1, TG_PRPRTY_CD);
			query.setParameter(2, ITM_LCTN_NM);
			query.setParameter(3, AD_BRNCH_CD);
			query.setParameter(4, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findByTgPropertyCodeAndItemLocNameFromPOLine(java.lang.String TG_PRPRTY_CD, java.lang.String ITM_LCTN_NM, java.lang.Integer AD_BRNCH_CD, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAllInByTgSerialNumberAndAdBranchAndAdCompany(java.lang.String TG_SRL_NMBR,
			java.lang.Integer AD_BRNCH_CD, java.lang.Integer TG_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.tgSerialNumber=?1 AND tg.tgType='IN' AND tg.apPurchaseOrderLine.apPurchaseOrder.poAdBranch=?2 AND tg.tgAdCompany=?3");
			query.setParameter(1, TG_SRL_NMBR);
			query.setParameter(2, AD_BRNCH_CD);
			query.setParameter(3, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findAllInByTgSerialNumberAndAdBranchAndAdCompany(java.lang.String TG_SRL_NMBR, java.lang.Integer AD_BRNCH_CD, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAllOutByTgSerialNumberAndAdBranchAndAdCompany(java.lang.String TG_SRL_NMBR,
			java.lang.Integer AD_BRNCH_CD, java.lang.Integer TG_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.tgSerialNumber=?1 AND tg.tgType='OUT' AND tg.tgAdCompany=?3");
			query.setParameter(1, TG_SRL_NMBR);
			query.setParameter(2, AD_BRNCH_CD);
			query.setParameter(3, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findAllOutByTgSerialNumberAndAdBranchAndAdCompany(java.lang.String TG_SRL_NMBR, java.lang.Integer AD_BRNCH_CD, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByTgPropertyCodeAndItemLocNameFromAdjLine(java.lang.String TG_PRPRTY_CD,
			java.lang.String ITM_LCTN_NM, java.lang.Integer AD_BRNCH_CD, java.lang.Integer TG_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.tgPropertyCode=?1 AND (tg.invAdjustmentLine.invItemLocation.invLocation.locName=?2 AND tg.invAdjustmentLine.invAdjustment.adjAdBranch=?3) AND tg.tgAdCompany=?4");
			query.setParameter(1, TG_PRPRTY_CD);
			query.setParameter(2, ITM_LCTN_NM);
			query.setParameter(3, AD_BRNCH_CD);
			query.setParameter(4, TG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTagHome.findByTgPropertyCodeAndItemLocNameFromAdjLine(java.lang.String TG_PRPRTY_CD, java.lang.String ITM_LCTN_NM, java.lang.Integer AD_BRNCH_CD, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getTgByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public java.util.Collection findByLatestDateFromAdjLine(java.util.Date TG_TXN_DT, java.lang.Integer ITM_LCTN_CD,
			java.lang.Integer AD_BRNCH_CD, java.lang.Integer TG_AD_CMPNY, java.lang.Integer LIMIT)
			throws FinderException {

		try {

			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.tgTransactionDate<=?1 AND tg.invAdjustmentLine.invItemLocation.ilCode=?2 AND tg.invAdjustmentLine.invAdjustment.adjAdBranch=?3 AND tg.tgAdCompany=?4 DESC");
			query.setParameter(1, TG_TXN_DT);
			query.setParameter(2, ITM_LCTN_CD);
			query.setParameter(3, AD_BRNCH_CD);
			query.setParameter(4, TG_AD_CMPNY);
			// query.setFirstResult(0);
			if(LIMIT>0){query.setMaxResults(LIMIT);}

            return query.getResultList();

		} catch (Exception ex) {
			throw ex;

		}
	}

	public java.util.Collection findByLatestDateFromPOLine(java.util.Date TG_TXN_DT, java.lang.Integer ITM_LCTN_CD,
			java.lang.Integer AD_BRNCH_CD, java.lang.Integer TG_AD_CMPNY, java.lang.Integer LIMIT)
			throws FinderException {

		try {

			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.tgTransactionDate<=?1 AND tg.apPurchaseOrderLine.invItemLocation.ilCode=?2 AND tg.apPurchaseOrderLine.apPurchaseOrder.poAdBranch=?3 AND tg.tgAdCompany=?4 DESC");
			query.setParameter(1, TG_TXN_DT);
			query.setParameter(2, ITM_LCTN_CD);
			query.setParameter(3, AD_BRNCH_CD);
			query.setParameter(4, TG_AD_CMPNY);
			// query.setFirstResult(0);
			if(LIMIT>0){query.setMaxResults(LIMIT);}

            return query.getResultList();

		} catch (Exception ex) {
			throw ex;

		}
	}

	public java.util.Collection findByLatestDate(java.util.Date TG_TXN_DT, java.lang.Integer TG_AD_CMPNY,
			java.lang.Integer LIMIT) throws FinderException {

		try {

			Query query = em.createQuery(
					"SELECT OBJECT(tg) FROM InvTag tg WHERE tg.tgTransactionDate<=?1 AND tg.tgAdCompany=?2 DESC");
			query.setParameter(1, TG_TXN_DT);
			query.setParameter(2, TG_AD_CMPNY);
			// query.setFirstResult(0);
			if(LIMIT>0){query.setMaxResults(LIMIT);}

            return query.getResultList();

		} catch (Exception ex) {
			throw ex;

		}
	}

	// CREATE METHODS

	public LocalInvTag create(Integer TG_CODE, String TG_PRPRTY_CODE, String TG_SRL_NMBR,
                              String TG_DCMNT_NMBR, java.util.Date TG_EXPRY_DT, String TG_SPECS, Integer TG_CMPNY,
                              java.util.Date TG_TXN_DT, String TG_TYP) throws CreateException {
		try {

			LocalInvTag entity = new LocalInvTag();

			Debug.print("invTagBean create");

			entity.setTgCode(TG_CODE);
			entity.setTgPropertyCode(TG_PRPRTY_CODE);
			entity.setTgSerialNumber(TG_SRL_NMBR);
			entity.setTgDocumentNumber(TG_DCMNT_NMBR);
			entity.setTgExpiryDate(TG_EXPRY_DT);
			entity.setTgSpecs(TG_SPECS);
			entity.setTgAdCompany(TG_CMPNY);
			entity.setTgTransactionDate(TG_TXN_DT);
			entity.setTgType(TG_TYP);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvTag create(String TG_PRPRTY_CODE, String TG_SRL_NMBR, String TG_DCMNT_NMBR,
                              java.util.Date TG_EXPRY_DT, String TG_SPECS, Integer TG_CMPNY, java.util.Date TG_TXN_DT, String TG_TYP)
			throws CreateException {
		try {

			LocalInvTag entity = new LocalInvTag();

			Debug.print("invTagBean create");

			entity.setTgPropertyCode(TG_PRPRTY_CODE);
			entity.setTgSerialNumber(TG_SRL_NMBR);
			entity.setTgDocumentNumber(TG_DCMNT_NMBR);
			entity.setTgExpiryDate(TG_EXPRY_DT);
			entity.setTgSpecs(TG_SPECS);
			entity.setTgAdCompany(TG_CMPNY);
			entity.setTgTransactionDate(TG_TXN_DT);
			entity.setTgType(TG_TYP);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}