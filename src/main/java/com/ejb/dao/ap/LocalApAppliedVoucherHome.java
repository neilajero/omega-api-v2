package com.ejb.dao.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApAppliedVoucher;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalApAppliedVoucherHome {

	public static final String JNDI_NAME = "LocalApAppliedVoucherHome!com.ejb.ap.LocalApAppliedVoucherHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalApAppliedVoucherHome() {
	}

	// FINDER METHODS

	public LocalApAppliedVoucher findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalApAppliedVoucher entity = (LocalApAppliedVoucher) em
					.find(new LocalApAppliedVoucher(), pk);
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

	public java.util.Collection findByChkDateRangeAndWtcCode(java.util.Date CHK_DT_FRM, java.util.Date CHK_DT_TO,
			java.lang.Integer AP_WTC_CODE, java.lang.Integer AV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(av) FROM ApAppliedVoucher av WHERE av.apCheck.chkDate >= ?1 AND av.apCheck.chkDate <= ?2 AND av.apCheck.chkPosted = 1 AND av.apCheck.chkVoid = 0 AND av.apCheck.chkType = 'PAYMENT' AND av.apVoucherPaymentSchedule.apVoucher.apWithholdingTaxCode.wtcCode = ?3 AND av.avTaxWithheld > 0 AND av.avAdCompany = ?4");
			query.setParameter(1, CHK_DT_FRM);
			query.setParameter(2, CHK_DT_TO);
			query.setParameter(3, AP_WTC_CODE);
			query.setParameter(4, AV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApAppliedVoucherHome.findByChkDateRangeAndWtcCode(java.com.util.Date CHK_DT_FRM, java.com.util.Date CHK_DT_TO, java.lang.Integer AP_WTC_CODE, java.lang.Integer AV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedAvByVpsCode(java.lang.Integer VPS_CODE, java.lang.Integer AV_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(av) FROM ApAppliedVoucher av WHERE av.apVoucherPaymentSchedule.vpsCode = ?1 AND av.apCheck.chkVoid = 0 AND av.apCheck.chkPosted = 1 AND av.avAdCompany = ?2");
			query.setParameter(1, VPS_CODE);
			query.setParameter(2, AV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApAppliedVoucherHome.findPostedAvByVpsCode(java.lang.Integer VPS_CODE, java.lang.Integer AV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByChkCode(java.lang.Integer CHK_CODE, java.lang.Integer AV_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(av) FROM ApAppliedVoucher av WHERE av.apCheck.chkCode = ?1 AND av.avAdCompany = ?2");
			query.setParameter(1, CHK_CODE);
			query.setParameter(2, AV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApAppliedVoucherHome.findByChkCode(java.lang.Integer CHK_CODE, java.lang.Integer AV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getAvByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalApAppliedVoucher create(Integer AP_AV_CODE, double AV_APPLY_AMNT, double AV_TX_WTHHLD,
										double AV_DSCNT_AMNT, double AV_ALLCTD_PYMNT_AMNT, double AV_FRX_GN_LSS, Integer AV_AD_CMPNY)
			throws CreateException {
		try {

			LocalApAppliedVoucher entity = new LocalApAppliedVoucher();

			Debug.print("ApAppliedVoucherBean create");
			entity.setAvCode(AP_AV_CODE);
			entity.setAvApplyAmount(AV_APPLY_AMNT);
			entity.setAvTaxWithheld(AV_TX_WTHHLD);
			entity.setAvDiscountAmount(AV_DSCNT_AMNT);
			entity.setAvAllocatedPaymentAmount(AV_ALLCTD_PYMNT_AMNT);
			entity.setAvForexGainLoss(AV_FRX_GN_LSS);
			entity.setAvAdCompany(AV_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalApAppliedVoucher create(double AV_APPLY_AMNT, double AV_TX_WTHHLD, double AV_DSCNT_AMNT,
										double AV_ALLCTD_PYMNT_AMNT, double AV_FRX_GN_LSS, Integer AV_AD_CMPNY) throws CreateException {
		try {

			LocalApAppliedVoucher entity = new LocalApAppliedVoucher();

			Debug.print("ApAppliedVoucherBean create");
			entity.setAvApplyAmount(AV_APPLY_AMNT);
			entity.setAvTaxWithheld(AV_TX_WTHHLD);
			entity.setAvDiscountAmount(AV_DSCNT_AMNT);
			entity.setAvAllocatedPaymentAmount(AV_ALLCTD_PYMNT_AMNT);
			entity.setAvForexGainLoss(AV_FRX_GN_LSS);
			entity.setAvAdCompany(AV_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}