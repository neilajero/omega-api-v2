package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArReceiptImportPreference;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArReceiptImportPreferenceHome {

	public static final String JNDI_NAME = "LocalArReceiptImportPreferenceHome!com.ejb.ar.LocalArReceiptImportPreferenceHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArReceiptImportPreferenceHome() {
	}

	// FINDER METHODS

	public LocalArReceiptImportPreference findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArReceiptImportPreference entity = (LocalArReceiptImportPreference) em
					.find(new LocalArReceiptImportPreference(), pk);
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

	public LocalArReceiptImportPreference findByRipType(java.lang.String RIP_TYP, java.lang.Integer RIP_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rip) FROM ArReceiptImportPreference rip WHERE rip.ripType=?1 AND rip.ripAdCompany=?2");
			query.setParameter(1, RIP_TYP);
			query.setParameter(2, RIP_AD_CMPNY);
            return (LocalArReceiptImportPreference) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArReceiptImportPreferenceHome.findByRipType(java.lang.String RIP_TYP, java.lang.Integer RIP_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptImportPreferenceHome.findByRipType(java.lang.String RIP_TYP, java.lang.Integer RIP_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getRipByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalArReceiptImportPreference create(Integer RIP_CODE, String RIP_TYP, byte RIP_IS_SMMRZD,
                                                 short RIP_CSTMR_CLMN, String RIP_CSTMR_FILE, short RIP_DT_CLMN, String RIP_DT_FILE,
                                                 short RIP_RCPT_NMBR_CLMN, String RIP_RCPT_NMBR_FILE, short RIP_RCPT_NMBR_LN_CLMN,
                                                 String RIP_RCPT_NMBR_LN_FILE, short RIP_RCPT_AMNT_CLMN, String RIP_RCPT_AMNT_FILE, short RIP_TC_CLMN,
                                                 String RIP_TC_FILE, short RIP_WTC_CLMN, String RIP_WTC_FILE, short RIP_ITM_NM_CLMN, String RIP_ITM_NM_FILE,
                                                 short RIP_LCTN_NM_CLMN, String RIP_LCTN_NM_FILE, short RIP_UOM_NM_CLMN, String RIP_UOM_NM_FILE,
                                                 short RIP_QTY_CLMN, String RIP_QTY_FILE, short RIP_UNT_PRC_CLMN, String RIP_UNT_PRC_FILE,
                                                 short RIP_TOTAL_CLMN, String RIP_TOTAL_FILE, short RIP_MEMO_LN_CLMN, String RIP_MEMO_LN_FILE,
                                                 short RIP_INVC_NMBR_CLMN, String RIP_INVC_NMBR_FILE, short RIP_APPLY_AMNT_CLMN, String RIP_APPLY_AMNT_FILE,
                                                 short RIP_APPLY_DSCNT_CLMN, String RIP_APPLY_DSCNT_FILE, Integer RIP_AD_CMPNY) throws CreateException {
		try {

			LocalArReceiptImportPreference entity = new LocalArReceiptImportPreference();

			Debug.print("ArReceiptImportPreferenceBean create");

			entity.setRipCode(RIP_CODE);
			entity.setRipType(RIP_TYP);
			entity.setRipIsSummarized(RIP_IS_SMMRZD);
			entity.setRipCustomerColumn(RIP_CSTMR_CLMN);
			entity.setRipCustomerFile(RIP_CSTMR_FILE);
			entity.setRipDateColumn(RIP_DT_CLMN);
			entity.setRipDateFile(RIP_DT_FILE);
			entity.setRipReceiptNumberColumn(RIP_RCPT_NMBR_CLMN);
			entity.setRipReceiptNumberFile(RIP_RCPT_NMBR_FILE);
			entity.setRipReceiptNumberLineColumn(RIP_RCPT_NMBR_LN_CLMN);
			entity.setRipReceiptNumberLineFile(RIP_RCPT_NMBR_LN_FILE);
			entity.setRipReceiptAmountColumn(RIP_RCPT_AMNT_CLMN);
			entity.setRipReceiptAmountFile(RIP_RCPT_AMNT_FILE);
			entity.setRipTcColumn(RIP_TC_CLMN);
			entity.setRipTcFile(RIP_TC_FILE);
			entity.setRipWtcColumn(RIP_WTC_CLMN);
			entity.setRipWtcFile(RIP_WTC_FILE);
			entity.setRipItemNameColumn(RIP_ITM_NM_CLMN);
			entity.setRipItemNameFile(RIP_ITM_NM_FILE);
			entity.setRipLocationNameColumn(RIP_LCTN_NM_CLMN);
			entity.setRipLocationNameFile(RIP_LCTN_NM_FILE);
			entity.setRipUomNameColumn(RIP_UOM_NM_CLMN);
			entity.setRipUomNameFile(RIP_UOM_NM_FILE);
			entity.setRipQuantityColumn(RIP_QTY_CLMN);
			entity.setRipQuantityFile(RIP_QTY_FILE);
			entity.setRipUnitPriceColumn(RIP_UNT_PRC_CLMN);
			entity.setRipUnitPriceFile(RIP_UNT_PRC_FILE);
			entity.setRipTotalColumn(RIP_TOTAL_CLMN);
			entity.setRipTotalFile(RIP_TOTAL_FILE);
			entity.setRipMemoLineColumn(RIP_MEMO_LN_CLMN);
			entity.setRipMemoLineFile(RIP_MEMO_LN_FILE);
			entity.setRipInvoiceNumberColumn(RIP_INVC_NMBR_CLMN);
			entity.setRipInvoiceNumberFile(RIP_INVC_NMBR_FILE);
			entity.setRipApplyAmountColumn(RIP_APPLY_AMNT_CLMN);
			entity.setRipApplyAmountFile(RIP_APPLY_AMNT_FILE);
			entity.setRipApplyDiscountColumn(RIP_APPLY_DSCNT_CLMN);
			entity.setRipApplyDiscountFile(RIP_APPLY_DSCNT_FILE);
			entity.setRipAdCompany(RIP_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArReceiptImportPreference create(String RIP_TYP, byte RIP_IS_SMMRZD, short RIP_CSTMR_CLMN,
                                                 String RIP_CSTMR_FILE, short RIP_DT_CLMN, String RIP_DT_FILE, short RIP_RCPT_NMBR_CLMN,
                                                 String RIP_RCPT_NMBR_FILE, short RIP_RCPT_NMBR_LN_CLMN, String RIP_RCPT_NMBR_LN_FILE,
                                                 short RIP_RCPT_AMNT_CLMN, String RIP_RCPT_AMNT_FILE, short RIP_TC_CLMN, String RIP_TC_FILE,
                                                 short RIP_WTC_CLMN, String RIP_WTC_FILE, short RIP_ITM_NM_CLMN, String RIP_ITM_NM_FILE,
                                                 short RIP_LCTN_NM_CLMN, String RIP_LCTN_NM_FILE, short RIP_UOM_NM_CLMN, String RIP_UOM_NM_FILE,
                                                 short RIP_QTY_CLMN, String RIP_QTY_FILE, short RIP_UNT_PRC_CLMN, String RIP_UNT_PRC_FILE,
                                                 short RIP_TOTAL_CLMN, String RIP_TOTAL_FILE, short RIP_MEMO_LN_CLMN, String RIP_MEMO_LN_FILE,
                                                 short RIP_INVC_NMBR_CLMN, String RIP_INVC_NMBR_FILE, short RIP_APPLY_AMNT_CLMN, String RIP_APPLY_AMNT_FILE,
                                                 short RIP_APPLY_DSCNT_CLMN, String RIP_APPLY_DSCNT_FILE, Integer RIP_AD_CMPNY) throws CreateException {
		try {

			LocalArReceiptImportPreference entity = new LocalArReceiptImportPreference();

			Debug.print("ArReceiptImportPreferenceBean create");

			entity.setRipType(RIP_TYP);
			entity.setRipIsSummarized(RIP_IS_SMMRZD);
			entity.setRipCustomerColumn(RIP_CSTMR_CLMN);
			entity.setRipCustomerFile(RIP_CSTMR_FILE);
			entity.setRipDateColumn(RIP_DT_CLMN);
			entity.setRipDateFile(RIP_DT_FILE);
			entity.setRipReceiptNumberColumn(RIP_RCPT_NMBR_CLMN);
			entity.setRipReceiptNumberFile(RIP_RCPT_NMBR_FILE);
			entity.setRipReceiptNumberLineColumn(RIP_RCPT_NMBR_LN_CLMN);
			entity.setRipReceiptNumberLineFile(RIP_RCPT_NMBR_LN_FILE);
			entity.setRipReceiptAmountColumn(RIP_RCPT_AMNT_CLMN);
			entity.setRipReceiptAmountFile(RIP_RCPT_AMNT_FILE);
			entity.setRipTcColumn(RIP_TC_CLMN);
			entity.setRipTcFile(RIP_TC_FILE);
			entity.setRipWtcColumn(RIP_WTC_CLMN);
			entity.setRipWtcFile(RIP_WTC_FILE);
			entity.setRipItemNameColumn(RIP_ITM_NM_CLMN);
			entity.setRipItemNameFile(RIP_ITM_NM_FILE);
			entity.setRipLocationNameColumn(RIP_LCTN_NM_CLMN);
			entity.setRipLocationNameFile(RIP_LCTN_NM_FILE);
			entity.setRipUomNameColumn(RIP_UOM_NM_CLMN);
			entity.setRipUomNameFile(RIP_UOM_NM_FILE);
			entity.setRipQuantityColumn(RIP_QTY_CLMN);
			entity.setRipQuantityFile(RIP_QTY_FILE);
			entity.setRipUnitPriceColumn(RIP_UNT_PRC_CLMN);
			entity.setRipUnitPriceFile(RIP_UNT_PRC_FILE);
			entity.setRipTotalColumn(RIP_TOTAL_CLMN);
			entity.setRipTotalFile(RIP_TOTAL_FILE);
			entity.setRipMemoLineColumn(RIP_MEMO_LN_CLMN);
			entity.setRipMemoLineFile(RIP_MEMO_LN_FILE);
			entity.setRipInvoiceNumberColumn(RIP_INVC_NMBR_CLMN);
			entity.setRipInvoiceNumberFile(RIP_INVC_NMBR_FILE);
			entity.setRipApplyAmountColumn(RIP_APPLY_AMNT_CLMN);
			entity.setRipApplyAmountFile(RIP_APPLY_AMNT_FILE);
			entity.setRipApplyDiscountColumn(RIP_APPLY_DSCNT_CLMN);
			entity.setRipApplyDiscountFile(RIP_APPLY_DSCNT_FILE);
			entity.setRipAdCompany(RIP_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}