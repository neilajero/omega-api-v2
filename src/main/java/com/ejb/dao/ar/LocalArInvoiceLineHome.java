package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArInvoiceLine;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;
import com.util.EJBCommon;

@Stateless
public class LocalArInvoiceLineHome {

	public static final String JNDI_NAME = "LocalArInvoiceLineHome!com.ejb.ar.LocalArInvoiceLineHome";

	@EJB
	public PersistenceBeanClass em;

	private String IL_DESC = null;
	private double IL_QNTTY = 0d;
	private double IL_UNT_PRC = 0d;
	private double IL_AMNT = 0d;
	private double IL_TX_AMNT = 0d;
	private byte IL_TX = EJBCommon.FALSE;
	private Integer IL_AD_CMPNY = null;

	public LocalArInvoiceLineHome() {
	}

	// FINDER METHODS

	public LocalArInvoiceLine findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArInvoiceLine entity = (LocalArInvoiceLine) em
					.find(new LocalArInvoiceLine(), pk);
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

	public java.util.Collection findByAdCompanyAll(java.lang.Integer INV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(il) FROM ArInvoiceLine il WHERE il.ilAdCompany = ?1");
			query.setParameter(1, INV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArInvoiceLineHome.findByAdCompanyAll(java.lang.Integer INV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findInvoiceLineByInvCodeAndAdCompany(java.lang.Integer INV_CODE,
			java.lang.Integer INV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM ArInvoiceLine il WHERE il.arInvoice.invCode = ?1 AND il.ilAdCompany = ?2");
			query.setParameter(1, INV_CODE);
			query.setParameter(2, INV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArInvoiceLineHome.findInvoiceLineByInvCodeAndAdCompany(java.lang.Integer INV_CODE, java.lang.Integer INV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findInvoiceLineByRctCodeAndAdCompany(java.lang.Integer RCT_CODE,
			java.lang.Integer INV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(il) FROM ArInvoiceLine il WHERE il.arReceipt.rctCode = ?1 AND il.ilAdCompany = ?2");
			query.setParameter(1, RCT_CODE);
			query.setParameter(2, INV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArInvoiceLineHome.findInvoiceLineByRctCodeAndAdCompany(java.lang.Integer RCT_CODE, java.lang.Integer INV_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArInvoiceLineHome IlDescription(String IL_DESC) {
		this.IL_DESC = IL_DESC;
		return this;
	}

	public LocalArInvoiceLineHome IlQuantity(double IL_QNTTY) {
		this.IL_QNTTY = IL_QNTTY;
		return this;
	}

	public LocalArInvoiceLineHome IlUnitPrice(double IL_UNT_PRC) {
		this.IL_UNT_PRC = IL_UNT_PRC;
		return this;
	}

	public LocalArInvoiceLineHome IlAmount(double IL_AMNT) {
		this.IL_AMNT = IL_AMNT;
		return this;
	}

	public LocalArInvoiceLineHome IlTaxAmount(double IL_TX_AMNT) {
		this.IL_TX_AMNT = IL_TX_AMNT;
		return this;
	}

	public LocalArInvoiceLineHome IlTax(byte IL_TX) {
		this.IL_TX = IL_TX;
		return this;
	}

	public LocalArInvoiceLineHome IlAdCompany(Integer IL_AD_CMPNY) {
		this.IL_AD_CMPNY = IL_AD_CMPNY;
		return this;
	}

	public LocalArInvoiceLine buildInvoiceLine(String companyShortName) throws CreateException {
		try {

			LocalArInvoiceLine entity = new LocalArInvoiceLine();

			Debug.print("ArInvoiceLineBean buildInvoiceLine");

			entity.setIlDescription(IL_DESC);
			entity.setIlQuantity(IL_QNTTY);
			entity.setIlUnitPrice(IL_UNT_PRC);
			entity.setIlAmount(IL_AMNT);
			entity.setIlTaxAmount(IL_TX_AMNT);
			entity.setIlTax(IL_TX);
			entity.setIlAdCompany(IL_AD_CMPNY);

			em.persist(entity, companyShortName);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArInvoiceLine create(Integer AR_IL_CODE, String IL_DESC, double IL_QNTTY, double IL_UNT_PRC,
                                     double IL_AMNT, double IL_TX_AMNT, byte IL_TX, Integer IL_AD_CMPNY) throws CreateException {
		try {

			LocalArInvoiceLine entity = new LocalArInvoiceLine();

			Debug.print("ArInvoiceLineBean create");

			entity.setIlCode(AR_IL_CODE);
			entity.setIlDescription(IL_DESC);
			entity.setIlQuantity(IL_QNTTY);
			entity.setIlUnitPrice(IL_UNT_PRC);
			entity.setIlAmount(IL_AMNT);
			entity.setIlTaxAmount(IL_TX_AMNT);
			entity.setIlTax(IL_TX);
			entity.setIlAdCompany(IL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArInvoiceLine create(String IL_DESC, double IL_QNTTY, double IL_UNT_PRC, double IL_AMNT,
                                     double IL_TX_AMNT, byte IL_TX, Integer IL_AD_CMPNY) throws CreateException {
		try {

			LocalArInvoiceLine entity = new LocalArInvoiceLine();

			Debug.print("ArInvoiceLineBean create");

			entity.setIlDescription(IL_DESC);
			entity.setIlQuantity(IL_QNTTY);
			entity.setIlUnitPrice(IL_UNT_PRC);
			entity.setIlAmount(IL_AMNT);
			entity.setIlTaxAmount(IL_TX_AMNT);
			entity.setIlTax(IL_TX);
			entity.setIlAdCompany(IL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}