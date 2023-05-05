package com.ejb.dao.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApCanvass;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalApCanvassHome {

	public static final String JNDI_NAME = "LocalApCanvassHome!com.ejb.ap.LocalApCanvassHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalApCanvassHome() {
	}

	// FINDER METHODS

	public LocalApCanvass findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalApCanvass entity = (LocalApCanvass) em.find(new LocalApCanvass(), pk);
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

	public java.util.Collection findByPrCodeAndCnvPo(java.lang.Integer PR_CODE, byte CNV_PO,
			java.lang.Integer CNV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cnv) FROM ApCanvass cnv WHERE cnv.apPurchaseRequisitionLine.apPurchaseRequisition.prCode=?1 AND cnv.cnvPo=?2 AND cnv.cnvAdCompany = ?3");
			query.setParameter(1, PR_CODE);
			query.setParameter(2, CNV_PO);
			query.setParameter(3, CNV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCanvassHome.findByPrCodeAndCnvPo(java.lang.Integer PR_CODE, byte CNV_PO, java.lang.Integer CNV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByPrlCodeAndSplSupplierCode(java.lang.Integer PRL_CODE,
			java.lang.String SPL_SPPLR_CODE, java.lang.Integer CNV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cnv) FROM ApCanvass cnv WHERE cnv.apPurchaseRequisitionLine.prlCode=?1 AND cnv.apSupplier.splSupplierCode = ?2 AND cnv.cnvAdCompany = ?3");
			query.setParameter(1, PRL_CODE);
			query.setParameter(2, SPL_SPPLR_CODE);
			query.setParameter(3, CNV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCanvassHome.findByPrlCodeAndSplSupplierCode(java.lang.Integer PRL_CODE, java.lang.String SPL_SPPLR_CODE, java.lang.Integer CNV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByPrlCodeAndCnvPo(java.lang.Integer PRL_CODE, byte CNV_PO,
			java.lang.Integer CNV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cnv) FROM ApCanvass cnv WHERE cnv.apPurchaseRequisitionLine.prlCode=?1 AND cnv.cnvPo = ?2 AND cnv.cnvAdCompany = ?3");
			query.setParameter(1, PRL_CODE);
			query.setParameter(2, CNV_PO);
			query.setParameter(3, CNV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCanvassHome.findByPrlCodeAndCnvPo(java.lang.Integer PRL_CODE, byte CNV_PO, java.lang.Integer CNV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getCnvByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalApCanvass create(Integer CNV_CODE, short CNV_LN, String CNV_RMKS, double CNV_QTY,
                                 double CNV_UNT_CST, double CNV_AMNT, byte CNV_PO, Integer CNV_AD_CMPNY) throws CreateException {
		try {

			LocalApCanvass entity = new LocalApCanvass();

			Debug.print("ApCanvassBean create");
			entity.setCnvCode(CNV_CODE);
			entity.setCnvLine(CNV_LN);
			entity.setCnvRemarks(CNV_RMKS);
			entity.setCnvQuantity(CNV_QTY);
			entity.setCnvUnitCost(CNV_UNT_CST);
			entity.setCnvAmount(CNV_AMNT);
			entity.setCnvPo(CNV_PO);
			entity.setCnvAdCompany(CNV_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalApCanvass create(short CNV_LN, String CNV_RMKS, double CNV_QTY, double CNV_UNT_CST,
                                 double CNV_AMNT, byte CNV_PO, Integer CNV_AD_CMPNY) throws CreateException {
		try {

			LocalApCanvass entity = new LocalApCanvass();

			Debug.print("ApCanvassBean create");
			entity.setCnvLine(CNV_LN);
			entity.setCnvRemarks(CNV_RMKS);
			entity.setCnvQuantity(CNV_QTY);
			entity.setCnvUnitCost(CNV_UNT_CST);
			entity.setCnvAmount(CNV_AMNT);
			entity.setCnvPo(CNV_PO);
			entity.setCnvAdCompany(CNV_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}