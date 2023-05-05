package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdPreference;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;
import com.util.EJBCommon;

@Stateless
public class LocalAdPreferenceHome implements ILocalAdPreferenceHome {

	public static final String JNDI_NAME = "LocalAdPreferenceHome!com.ejb.ad.LocalAdPreferenceHome";

	@EJB
	public PersistenceBeanClass em;
	
	private final java.lang.Integer PRF_CODE = null;
	private final byte PRF_ALLW_SSPNS_PSTNG = EJBCommon.FALSE;
	private final short PRF_GL_JRNL_LN_NMBR = (short)0;
	private final short PRF_AP_JRNL_LN_NMBR = (short)0;
	private final short PRF_AR_INVC_LN_NMBR = (short)0;
	private final String PRF_AP_W_TX_RLZTN = null;
	private final String PRF_AR_W_TX_RLZTN = null;
	private final byte PRF_ENBL_GL_JRNL_BTCH = EJBCommon.FALSE;
	private final byte PRF_ENBL_GL_RCMPT_COA_BLNC = EJBCommon.FALSE;
	private final byte PRF_ENBL_AP_VCHR_BTCH = EJBCommon.FALSE;
	private final byte PRF_ENBL_AP_PO_BTCH = EJBCommon.FALSE;
	private final byte PRF_ENBL_AP_CHCK_BTCH = EJBCommon.FALSE;
	private final byte PRF_ENBL_AR_INVC_BTCH = EJBCommon.FALSE;
	private final byte PRF_ENBL_AR_INVC_INT_GNRTN = EJBCommon.FALSE;
	private final byte PRF_ENBL_AR_RCPT_BTCH = EJBCommon.FALSE;
	private final byte PRF_ENBL_AR_MISC_RCPT_BTCH = EJBCommon.FALSE;
	private final String PRF_AP_GL_PSTNG_TYP = null;
	private final String PRF_AR_GL_PSTNG_TYP = null;
	private final String PRF_CM_GL_PSTNG_TYP = null;
	private final short PRF_INV_INVNTRY_LN_NMBR = (short)0;
	private final short PRF_INV_QTY_PRCSN_UNT = (short)0;
	private final short PRF_INV_CST_PRCSN_UNT = (short)0;
	private final String PRF_INV_GL_PSTNG_TYP = null;
	private final String PRF_GL_PSTNG_TYP = null;
	private final byte PRF_INV_ENBL_SHFT = EJBCommon.FALSE;
	private final byte PRF_ENBL_INV_BUA_BTCH = EJBCommon.FALSE;
	private final String PRF_AP_FND_CHCK_DFLT_TYP = null;
	private final String PRF_AR_FND_RCPT_DFLT_TYP = null;
	private final byte PRF_AP_US_ACCRD_VAT = EJBCommon.FALSE;
	private final String PRF_AP_DFLT_CHCK_DT = null;
	private final String PRF_AP_DFLT_CHCKR = null;
	private final String PRF_AP_DFLT_APPRVR = null;
	private final Integer PRF_AP_GL_COA_ACCRD_VAT_ACCNT = null;
	private final Integer PRF_AP_GL_COA_PTTY_CSH_ACCNT= null;
	private final byte PRF_CM_USE_BNK_FRM = EJBCommon.FALSE;
	private final byte PRF_AP_USE_SPPLR_PLLDWN = EJBCommon.FALSE;
	private final byte PRF_AR_USE_CSTMR_PLLDWN = EJBCommon.FALSE;
	private final byte PRF_AP_AT_GNRT_SPPLR_CODE = EJBCommon.FALSE;
	private final String PRF_AP_NXT_SPPLR_CODE= null;
	private final byte PRF_AR_AT_GNRT_CSTMR_CODE = EJBCommon.FALSE;
	private final String PRF_AR_NXT_CSTMR_CODE = null;
	private final byte PRF_AP_RFRNC_NMBR_VLDTN = EJBCommon.FALSE;
	private final byte PRF_INV_ENBL_POS_INTGRTN = EJBCommon.FALSE;
	private final byte PRF_INV_ENBL_POS_AUTO_POST_UP = EJBCommon.FALSE;
	private final Integer PRF_INV_POS_ADJSTMNT_ACCNT = null;
	private final String PRF_AP_CHK_VCHR_DT_SRC = null;
	private final Integer PRF_MISC_POS_DSCNT_ACCNT = null;
	private final Integer PRF_MISC_POS_GFT_CRTFCT_ACCNT = null;
	private final Integer PRF_MISC_POS_SRVC_CHRG_ACCNT = null;
	private final Integer PRF_MISC_POS_DN_IN_CHRG_ACCNT = null;
	private final Integer PRF_AR_GL_COA_CSTMR_DPST_ACCNT = null;
	private final String PRF_AP_DFLT_PR_TX = null;
	private final String PRF_AP_DFLT_PR_CRRNCY = null;
	private final String PRF_AR_SLS_INVC_DT_SRC = null;
	private final Integer PRF_INV_GL_COA_VRNC_ACCNT = null;
	private final byte PRF_AR_AUTO_CMPUTE_COGS = EJBCommon.FALSE;
	private final double PRF_AR_MNTH_INT_RT = 0d;
	private final int PRF_AP_AGNG_BCKT = 0;
	private final int PRF_AR_AGNG_BCKT = 0;
	private final byte PRF_AR_ALLW_PRR_DT = EJBCommon.FALSE;
	private final byte PRF_AR_CHK_INSFFCNT_STCK = EJBCommon.FALSE;
	private final byte PRF_AR_DTLD_RCVBL = EJBCommon.FALSE;
	private final byte PRF_AP_SHW_PR_CST = EJBCommon.FALSE;
	private final byte PRF_AR_ENBL_PYMNT_TRM = EJBCommon.FALSE;
	private final byte PRF_AR_SO_SLSPRSN_RQRD = EJBCommon.FALSE;
	private final byte PRF_AR_INVC_SLSPRSN_RQRD = EJBCommon.FALSE;
	private final String PRF_ML_HST = null;
	private final String PRF_ML_SCKT_FCTRY_PRT = null;
	private final String PRF_ML_PRT = null;
	private final String PRF_ML_FRM = null;
	private final byte PRF_ML_AUTH = EJBCommon.FALSE;
	private final String PRF_ML_PSSWRD = null;
	private final String PRF_ML_TO = null;
	private final String PRF_ML_CC = null;
	private final String PRF_ML_BCC = null;
	private final String PRF_ML_CNFG = null;
	private final String PRF_USR_RST_WB_SRVC = null;
	private final String PRF_PSS_RST_WB_SRVC = null;
	private final String PRF_ATTCHMNT_PTH = null;
	private final Integer PRF_AD_CMPNY = null;

	public LocalAdPreferenceHome() {
	}

	// FINDER METHODS

	public LocalAdPreference findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdPreference entity = (LocalAdPreference) em
					.find(new LocalAdPreference(), pk);
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

	public LocalAdPreference findByPrfAdCompany(java.lang.Integer PRF_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(prf) FROM AdPreference prf WHERE prf.prfAdCompany = ?1");
			query.setParameter(1, PRF_AD_CMPNY);
            return (LocalAdPreference) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException LocalAdPreferenceHome.findByPrfAdCompany(java.lang.Integer PRF_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception LocalAdPreferenceHome.findByPrfAdCompany(java.lang.Integer PRF_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdPreference findByPrfAdCompany(java.lang.Integer PRF_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany("SELECT OBJECT(prf) FROM AdPreference prf WHERE prf.prfAdCompany = ?1",companyShortName);
			query.setParameter(1, PRF_AD_CMPNY);
			return (LocalAdPreference) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS
	public LocalAdPreference buildPreference() throws CreateException {
		try {

			LocalAdPreference entity = new LocalAdPreference();

			Debug.print("AdPreferenceBean buildPreference");
			entity.setPrfCode(PRF_CODE);
			entity.setPrfAllowSuspensePosting(PRF_ALLW_SSPNS_PSTNG);
			entity.setPrfGlJournalLineNumber(PRF_GL_JRNL_LN_NMBR);
			entity.setPrfApJournalLineNumber(PRF_AP_JRNL_LN_NMBR);
			entity.setPrfArInvoiceLineNumber(PRF_AR_INVC_LN_NMBR);
			entity.setPrfApWTaxRealization(PRF_AP_W_TX_RLZTN);
			entity.setPrfArWTaxRealization(PRF_AR_W_TX_RLZTN);
			entity.setPrfEnableGlJournalBatch(PRF_ENBL_GL_JRNL_BTCH);
			entity.setPrfEnableGlRecomputeCoaBalance(PRF_ENBL_GL_RCMPT_COA_BLNC);
			entity.setPrfEnableApVoucherBatch(PRF_ENBL_AP_VCHR_BTCH);
			entity.setPrfEnableApPOBatch(PRF_ENBL_AP_PO_BTCH);
			entity.setPrfEnableApCheckBatch(PRF_ENBL_AP_CHCK_BTCH);
			entity.setPrfEnableArInvoiceBatch(PRF_ENBL_AR_INVC_BTCH);
			entity.setPrfEnableArInvoiceInterestGeneration(PRF_ENBL_AR_INVC_INT_GNRTN);
			entity.setPrfEnableArReceiptBatch(PRF_ENBL_AR_RCPT_BTCH);
			entity.setPrfEnableArMiscReceiptBatch(PRF_ENBL_AR_MISC_RCPT_BTCH);
			entity.setPrfApGlPostingType(PRF_AP_GL_PSTNG_TYP);
			entity.setPrfArGlPostingType(PRF_AR_GL_PSTNG_TYP);
			entity.setPrfCmGlPostingType(PRF_CM_GL_PSTNG_TYP);
			entity.setPrfInvInventoryLineNumber(PRF_INV_INVNTRY_LN_NMBR);
			entity.setPrfInvQuantityPrecisionUnit(PRF_INV_QTY_PRCSN_UNT);
			entity.setPrfInvCostPrecisionUnit(PRF_INV_CST_PRCSN_UNT);
			entity.setPrfInvGlPostingType(PRF_INV_GL_PSTNG_TYP);
			entity.setPrfGlPostingType(PRF_GL_PSTNG_TYP);
			entity.setPrfInvEnableShift(PRF_INV_ENBL_SHFT);
			entity.setPrfEnableInvBUABatch(PRF_ENBL_INV_BUA_BTCH);
			entity.setPrfApFindCheckDefaultType(PRF_AP_FND_CHCK_DFLT_TYP);
			entity.setPrfArFindReceiptDefaultType(PRF_AR_FND_RCPT_DFLT_TYP);
			entity.setPrfApUseAccruedVat(PRF_AP_US_ACCRD_VAT);
			entity.setPrfApDefaultCheckDate(PRF_AP_DFLT_CHCK_DT);
			entity.setPrfApDefaultChecker(PRF_AP_DFLT_CHCKR);
			entity.setPrfApDefaultApprover(PRF_AP_DFLT_APPRVR);
			entity.setPrfApGlCoaAccruedVatAccount(PRF_AP_GL_COA_ACCRD_VAT_ACCNT);
			entity.setPrfApGlCoaPettyCashAccount(PRF_AP_GL_COA_PTTY_CSH_ACCNT);
			entity.setPrfCmUseBankForm(PRF_CM_USE_BNK_FRM);
			entity.setPrfApUseSupplierPulldown(PRF_AP_USE_SPPLR_PLLDWN);
			entity.setPrfArUseCustomerPulldown(PRF_AR_USE_CSTMR_PLLDWN);
			entity.setPrfApAutoGenerateSupplierCode(PRF_AP_AT_GNRT_SPPLR_CODE);
			entity.setPrfApNextSupplierCode(PRF_AP_NXT_SPPLR_CODE);
			entity.setPrfApReferenceNumberValidation(PRF_AP_RFRNC_NMBR_VLDTN);
			entity.setPrfInvEnablePosIntegration(PRF_INV_ENBL_POS_INTGRTN);
			entity.setPrfInvEnablePosAutoPostUpload(PRF_INV_ENBL_POS_AUTO_POST_UP);
			entity.setPrfInvPosAdjustmentAccount(PRF_INV_POS_ADJSTMNT_ACCNT);
			entity.setPrfApCheckVoucherDataSource(PRF_AP_CHK_VCHR_DT_SRC);
			entity.setPrfMiscPosDiscountAccount(PRF_MISC_POS_DSCNT_ACCNT);
			entity.setPrfMiscPosGiftCertificateAccount(PRF_MISC_POS_GFT_CRTFCT_ACCNT);
			entity.setPrfMiscPosServiceChargeAccount(PRF_MISC_POS_SRVC_CHRG_ACCNT);
			entity.setPrfMiscPosDineInChargeAccount(PRF_MISC_POS_DN_IN_CHRG_ACCNT);
			entity.setPrfArGlCoaCustomerDepositAccount(PRF_AR_GL_COA_CSTMR_DPST_ACCNT);
			entity.setPrfApDefaultPrTax(PRF_AP_DFLT_PR_TX);
			entity.setPrfApDefaultPrCurrency(PRF_AP_DFLT_PR_CRRNCY);
			entity.setPrfArSalesInvoiceDataSource(PRF_AR_SLS_INVC_DT_SRC);
			entity.setPrfInvGlCoaVarianceAccount(PRF_INV_GL_COA_VRNC_ACCNT);
			entity.setPrfArAutoComputeCogs(PRF_AR_AUTO_CMPUTE_COGS);
			entity.setPrfArMonthlyInterestRate(PRF_AR_MNTH_INT_RT);
			entity.setPrfApAgingBucket(PRF_AP_AGNG_BCKT);
			entity.setPrfArAgingBucket(PRF_AR_AGNG_BCKT);
			entity.setPrfArAllowPriorDate(PRF_AR_ALLW_PRR_DT);
			entity.setPrfArCheckInsufficientStock(PRF_AR_CHK_INSFFCNT_STCK);
			entity.setPrfArDetailedReceivable(PRF_AR_DTLD_RCVBL);
			entity.setPrfApShowPrCost(PRF_AP_SHW_PR_CST);
			entity.setPrfArEnablePaymentTerm(PRF_AR_ENBL_PYMNT_TRM);
			entity.setPrfArSoSalespersonRequired(PRF_AR_INVC_SLSPRSN_RQRD);
			entity.setPrfArInvcSalespersonRequired(PRF_AR_INVC_SLSPRSN_RQRD);
			entity.setPrfMailHost(PRF_ML_HST);
			entity.setPrfMailSocketFactoryPort(PRF_ML_SCKT_FCTRY_PRT);
			entity.setPrfMailPort(PRF_ML_PRT);
			entity.setPrfMailFrom(PRF_ML_FRM);
			entity.setPrfMailAuthenticator(PRF_ML_AUTH);
			entity.setPrfMailPassword(PRF_ML_PSSWRD);
			entity.setPrfMailTo(PRF_ML_TO);
			entity.setPrfMailCc(PRF_ML_CC);
			entity.setPrfMailBcc(PRF_ML_BCC);
			entity.setPrfMailConfig(PRF_ML_CNFG);
			entity.setPrfUserRestWebService(PRF_USR_RST_WB_SRVC);
			entity.setPrfPassRestWebService(PRF_PSS_RST_WB_SRVC);
			entity.setPrfAttachmentPath(PRF_ATTCHMNT_PTH);
			entity.setPrfAdCompany(PRF_AD_CMPNY);
			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdPreference create(java.lang.Integer PRF_CODE, byte PRF_ALLW_SSPNS_PSTNG,
			short PRF_GL_JRNL_LN_NMBR, short PRF_AP_JRNL_LN_NMBR, short PRF_AR_INVC_LN_NMBR, String PRF_AP_W_TX_RLZTN,
			String PRF_AR_W_TX_RLZTN, byte PRF_ENBL_GL_JRNL_BTCH, byte PRF_ENBL_GL_RCMPT_COA_BLNC,
			byte PRF_ENBL_AP_VCHR_BTCH, byte PRF_ENBL_AP_PO_BTCH, byte PRF_ENBL_AP_CHCK_BTCH,
			byte PRF_ENBL_AR_INVC_BTCH, byte PRF_ENBL_AR_INVC_INT_GNRTN, byte PRF_ENBL_AR_RCPT_BTCH,
			byte PRF_ENBL_AR_MISC_RCPT_BTCH, String PRF_AP_GL_PSTNG_TYP, String PRF_AR_GL_PSTNG_TYP,
			String PRF_CM_GL_PSTNG_TYP, short PRF_INV_INVNTRY_LN_NMBR, short PRF_INV_QTY_PRCSN_UNT,
			short PRF_INV_CST_PRCSN_UNT, String PRF_INV_GL_PSTNG_TYP, String PRF_GL_PSTNG_TYP, byte PRF_INV_ENBL_SHFT,
			byte PRF_ENBL_INV_BUA_BTCH, String PRF_AP_FND_CHCK_DFLT_TYP, String PRF_AR_FND_RCPT_DFLT_TYP,
			byte PRF_AP_US_ACCRD_VAT, String PRF_AP_DFLT_CHCK_DT, String PRF_AP_DFLT_CHCKR, String PRF_AP_DFLT_APPRVR,
			Integer PRF_AP_GL_COA_ACCRD_VAT_ACCNT, Integer PRF_AP_GL_COA_PTTY_CSH_ACCNT, byte PRF_CM_USE_BNK_FRM,
			byte PRF_AP_USE_SPPLR_PLLDWN, byte PRF_AR_USE_CSTMR_PLLDWN, byte PRF_AP_AT_GNRT_SPPLR_CODE,
			String PRF_AP_NXT_SPPLR_CODE, byte PRF_AR_AT_GNRT_CSTMR_CODE, String PRF_AR_NXT_CSTMR_CODE,
			byte PRF_AP_RFRNC_NMBR_VLDTN, byte PRF_INV_ENBL_POS_INTGRTN, byte PRF_INV_ENBL_POS_AUTO_POST_UP,
			Integer PRF_INV_POS_ADJSTMNT_ACCNT, String PRF_AP_CHK_VCHR_DT_SRC, Integer PRF_MISC_POS_DSCNT_ACCNT,
			Integer PRF_MISC_POS_GFT_CRTFCT_ACCNT, Integer PRF_MISC_POS_SRVC_CHRG_ACCNT,
			Integer PRF_MISC_POS_DN_IN_CHRG_ACCNT, Integer PRF_AR_GL_COA_CSTMR_DPST_ACCNT, String PRF_AP_DFLT_PR_TX,
			String PRF_AP_DFLT_PR_CRRNCY, String PRF_AR_SLS_INVC_DT_SRC, Integer PRF_INV_GL_COA_VRNC_ACCNT,
			byte PRF_AR_AUTO_CMPUTE_COGS, double PRF_AR_MNTH_INT_RT, int PRF_AP_AGNG_BCKT, int PRF_AR_AGNG_BCKT,
			byte PRF_AR_ALLW_PRR_DT, byte PRF_AR_CHK_INSFFCNT_STCK, byte PRF_AR_DTLD_RCVBL, byte PRF_AP_SHW_PR_CST,
			byte PRF_AR_ENBL_PYMNT_TRM, byte PRF_AR_SO_SLSPRSN_RQRD, byte PRF_AR_INVC_SLSPRSN_RQRD, String PRF_ML_HST,
			String PRF_ML_SCKT_FCTRY_PRT, String PRF_ML_PRT, String PRF_ML_FRM, byte PRF_ML_AUTH, String PRF_ML_PSSWRD,
			String PRF_ML_TO, String PRF_ML_CC, String PRF_ML_BCC, String PRF_ML_CNFG, String PRF_USR_RST_WB_SRVC,
			String PRF_PSS_RST_WB_SRVC, String PRF_ATTCHMNT_PTH, Integer PRF_AD_CMPNY) throws CreateException {
		try {

			LocalAdPreference entity = new LocalAdPreference();

			Debug.print("AdPreferenceBean create");
			entity.setPrfCode(PRF_CODE);
			entity.setPrfAllowSuspensePosting(PRF_ALLW_SSPNS_PSTNG);
			entity.setPrfGlJournalLineNumber(PRF_GL_JRNL_LN_NMBR);
			entity.setPrfApJournalLineNumber(PRF_AP_JRNL_LN_NMBR);
			entity.setPrfArInvoiceLineNumber(PRF_AR_INVC_LN_NMBR);
			entity.setPrfApWTaxRealization(PRF_AP_W_TX_RLZTN);
			entity.setPrfArWTaxRealization(PRF_AR_W_TX_RLZTN);
			entity.setPrfEnableGlJournalBatch(PRF_ENBL_GL_JRNL_BTCH);
			entity.setPrfEnableGlRecomputeCoaBalance(PRF_ENBL_GL_RCMPT_COA_BLNC);
			entity.setPrfEnableApVoucherBatch(PRF_ENBL_AP_VCHR_BTCH);
			entity.setPrfEnableApPOBatch(PRF_ENBL_AP_PO_BTCH);
			entity.setPrfEnableApCheckBatch(PRF_ENBL_AP_CHCK_BTCH);
			entity.setPrfEnableArInvoiceBatch(PRF_ENBL_AR_INVC_BTCH);
			entity.setPrfEnableArInvoiceInterestGeneration(PRF_ENBL_AR_INVC_INT_GNRTN);
			entity.setPrfEnableArReceiptBatch(PRF_ENBL_AR_RCPT_BTCH);
			entity.setPrfEnableArMiscReceiptBatch(PRF_ENBL_AR_MISC_RCPT_BTCH);
			entity.setPrfApGlPostingType(PRF_AP_GL_PSTNG_TYP);
			entity.setPrfArGlPostingType(PRF_AR_GL_PSTNG_TYP);
			entity.setPrfCmGlPostingType(PRF_CM_GL_PSTNG_TYP);
			entity.setPrfInvInventoryLineNumber(PRF_INV_INVNTRY_LN_NMBR);
			entity.setPrfInvQuantityPrecisionUnit(PRF_INV_QTY_PRCSN_UNT);
			entity.setPrfInvCostPrecisionUnit(PRF_INV_CST_PRCSN_UNT);
			entity.setPrfInvGlPostingType(PRF_INV_GL_PSTNG_TYP);
			entity.setPrfGlPostingType(PRF_GL_PSTNG_TYP);
			entity.setPrfInvEnableShift(PRF_INV_ENBL_SHFT);
			entity.setPrfEnableInvBUABatch(PRF_ENBL_INV_BUA_BTCH);
			entity.setPrfApFindCheckDefaultType(PRF_AP_FND_CHCK_DFLT_TYP);
			entity.setPrfArFindReceiptDefaultType(PRF_AR_FND_RCPT_DFLT_TYP);
			entity.setPrfApUseAccruedVat(PRF_AP_US_ACCRD_VAT);
			entity.setPrfApDefaultCheckDate(PRF_AP_DFLT_CHCK_DT);
			entity.setPrfApDefaultChecker(PRF_AP_DFLT_CHCKR);
			entity.setPrfApDefaultApprover(PRF_AP_DFLT_APPRVR);
			entity.setPrfApGlCoaAccruedVatAccount(PRF_AP_GL_COA_ACCRD_VAT_ACCNT);
			entity.setPrfApGlCoaPettyCashAccount(PRF_AP_GL_COA_PTTY_CSH_ACCNT);
			entity.setPrfCmUseBankForm(PRF_CM_USE_BNK_FRM);
			entity.setPrfApUseSupplierPulldown(PRF_AP_USE_SPPLR_PLLDWN);
			entity.setPrfArUseCustomerPulldown(PRF_AR_USE_CSTMR_PLLDWN);
			entity.setPrfApAutoGenerateSupplierCode(PRF_AP_AT_GNRT_SPPLR_CODE);
			entity.setPrfApNextSupplierCode(PRF_AP_NXT_SPPLR_CODE);
			entity.setPrfApReferenceNumberValidation(PRF_AP_RFRNC_NMBR_VLDTN);
			entity.setPrfInvEnablePosIntegration(PRF_INV_ENBL_POS_INTGRTN);
			entity.setPrfInvEnablePosAutoPostUpload(PRF_INV_ENBL_POS_AUTO_POST_UP);
			entity.setPrfInvPosAdjustmentAccount(PRF_INV_POS_ADJSTMNT_ACCNT);
			entity.setPrfApCheckVoucherDataSource(PRF_AP_CHK_VCHR_DT_SRC);
			entity.setPrfMiscPosDiscountAccount(PRF_MISC_POS_DSCNT_ACCNT);
			entity.setPrfMiscPosGiftCertificateAccount(PRF_MISC_POS_GFT_CRTFCT_ACCNT);
			entity.setPrfMiscPosServiceChargeAccount(PRF_MISC_POS_SRVC_CHRG_ACCNT);
			entity.setPrfMiscPosDineInChargeAccount(PRF_MISC_POS_DN_IN_CHRG_ACCNT);
			entity.setPrfArGlCoaCustomerDepositAccount(PRF_AR_GL_COA_CSTMR_DPST_ACCNT);
			entity.setPrfApDefaultPrTax(PRF_AP_DFLT_PR_TX);
			entity.setPrfApDefaultPrCurrency(PRF_AP_DFLT_PR_CRRNCY);
			entity.setPrfArSalesInvoiceDataSource(PRF_AR_SLS_INVC_DT_SRC);
			entity.setPrfInvGlCoaVarianceAccount(PRF_INV_GL_COA_VRNC_ACCNT);
			entity.setPrfArAutoComputeCogs(PRF_AR_AUTO_CMPUTE_COGS);
			entity.setPrfArMonthlyInterestRate(PRF_AR_MNTH_INT_RT);
			entity.setPrfApAgingBucket(PRF_AP_AGNG_BCKT);
			entity.setPrfArAgingBucket(PRF_AR_AGNG_BCKT);
			entity.setPrfArAllowPriorDate(PRF_AR_ALLW_PRR_DT);
			entity.setPrfArCheckInsufficientStock(PRF_AR_CHK_INSFFCNT_STCK);
			entity.setPrfArDetailedReceivable(PRF_AR_DTLD_RCVBL);
			entity.setPrfApShowPrCost(PRF_AP_SHW_PR_CST);
			entity.setPrfArEnablePaymentTerm(PRF_AR_ENBL_PYMNT_TRM);
			entity.setPrfArSoSalespersonRequired(PRF_AR_INVC_SLSPRSN_RQRD);
			entity.setPrfArInvcSalespersonRequired(PRF_AR_INVC_SLSPRSN_RQRD);
			entity.setPrfMailHost(PRF_ML_HST);
			entity.setPrfMailSocketFactoryPort(PRF_ML_SCKT_FCTRY_PRT);
			entity.setPrfMailPort(PRF_ML_PRT);
			entity.setPrfMailFrom(PRF_ML_FRM);
			entity.setPrfMailAuthenticator(PRF_ML_AUTH);
			entity.setPrfMailPassword(PRF_ML_PSSWRD);
			entity.setPrfMailTo(PRF_ML_TO);
			entity.setPrfMailCc(PRF_ML_CC);
			entity.setPrfMailBcc(PRF_ML_BCC);
			entity.setPrfMailConfig(PRF_ML_CNFG);
			entity.setPrfUserRestWebService(PRF_USR_RST_WB_SRVC);
			entity.setPrfPassRestWebService(PRF_PSS_RST_WB_SRVC);
			entity.setPrfAttachmentPath(PRF_ATTCHMNT_PTH);
			entity.setPrfAdCompany(PRF_AD_CMPNY);
			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdPreference create(byte PRF_ALLW_SSPNS_PSTNG, short PRF_GL_JRNL_LN_NMBR,
			short PRF_AP_JRNL_LN_NMBR, short PRF_AR_INVC_LN_NMBR, String PRF_AP_W_TX_RLZTN, String PRF_AR_W_TX_RLZTN,
			byte PRF_ENBL_GL_JRNL_BTCH, byte PRF_ENBL_GL_RCMPT_COA_BLNC, byte PRF_ENBL_AP_VCHR_BTCH,
			byte PRF_ENBL_AP_PO_BTCH, byte PRF_ENBL_AP_CHCK_BTCH, byte PRF_ENBL_AR_INVC_BTCH,
			byte PRF_ENBL_AR_INVC_INT_GNRTN, byte PRF_ENBL_AR_RCPT_BTCH, byte PRF_ENBL_AR_MISC_RCPT_BTCH,
			String PRF_AP_GL_PSTNG_TYP, String PRF_AR_GL_PSTNG_TYP, String PRF_CM_GL_PSTNG_TYP,
			short PRF_INV_INVNTRY_LN_NMBR, short PRF_INV_QTY_PRCSN_UNT, short PRF_INV_CST_PRCSN_UNT,
			String PRF_INV_GL_PSTNG_TYP, String PRF_GL_PSTNG_TYP, byte PRF_INV_ENBL_SHFT, byte PRF_ENBL_INV_BUA_BTCH,
			String PRF_AP_FND_CHCK_DFLT_TYP, String PRF_AR_FND_RCPT_DFLT_TYP, byte PRF_AP_US_ACCRD_VAT,
			String PRF_AP_DFLT_CHCK_DT, String PRF_AP_DFLT_CHCKR, String PRF_AP_DFLT_APPRVR,
			Integer PRF_AP_GL_COA_ACCRD_VAT_ACCNT, Integer PRF_AP_GL_COA_PTTY_CSH_ACCNT, byte PRF_CM_USE_BNK_FRM,
			byte PRF_AP_USE_SPPLR_PLLDWN, byte PRF_AR_USE_CSTMR_PLLDWN, byte PRF_AP_AT_GNRT_SPPLR_CODE,
			String PRF_AP_NXT_SPPLR_CODE, byte PRF_AR_AT_GNRT_CSTMR_CODE, String PRF_AR_NXT_CSTMR_CODE,
			byte PRF_AP_RFRNC_NMBR_VLDTN, byte PRF_INV_ENBL_POS_INTGRTN, byte PRF_INV_ENBL_POS_AUTO_POST_UP,
			Integer PRF_INV_POS_ADJSTMNT_ACCNT, String PRF_AP_CHK_VCHR_DT_SRC, Integer PRF_MISC_POS_DSCNT_ACCNT,
			Integer PRF_MISC_POS_GFT_CRTFCT_ACCNT, Integer PRF_MISC_POS_SRVC_CHRG_ACCNT,
			Integer PRF_MISC_POS_DN_IN_CHRG_ACCNT, Integer PRF_AR_GL_COA_CSTMR_DPST_ACCNT, String PRF_AP_DFLT_PR_TX,
			String PRF_AP_DFLT_PR_CRRNCY, String PRF_AR_SLS_INVC_DT_SRC, Integer PRF_INV_GL_COA_VRNC_ACCNT,
			byte PRF_AR_AUTO_CMPUTE_COGS, double PRF_AR_MNTH_INT_RT, int PRF_AP_AGNG_BCKT, int PRF_AR_AGNG_BCKT,
			byte PRF_AR_ALLW_PRR_DT, byte PRF_AR_CHK_INSFFCNT_STCK, byte PRF_AR_DTLD_RCVBL, byte PRF_AP_SHW_PR_CST,
			byte PRF_AR_ENBL_PYMNT_TRM, byte PRF_AR_SO_SLSPRSN_RQRD, byte PRF_AR_INVC_SLSPRSN_RQRD, String PRF_ML_HST,
			String PRF_ML_SCKT_FCTRY_PRT, String PRF_ML_PRT, String PRF_ML_FRM, byte PRF_ML_AUTH, String PRF_ML_PSSWRD,
			String PRF_ML_TO, String PRF_ML_CC, String PRF_ML_BCC, String PRF_ML_CNFG, String PRF_USR_RST_WB_SRVC,
			String PRF_PSS_RST_WB_SRVC, String PRF_ATTCHMNT_PTH, Integer PRF_AD_CMPNY) throws CreateException {
		try {

			LocalAdPreference entity = new LocalAdPreference();

			Debug.print("AdPreferenceBean create");
			entity.setPrfAllowSuspensePosting(PRF_ALLW_SSPNS_PSTNG);
			entity.setPrfGlJournalLineNumber(PRF_GL_JRNL_LN_NMBR);
			entity.setPrfApJournalLineNumber(PRF_AP_JRNL_LN_NMBR);
			entity.setPrfArInvoiceLineNumber(PRF_AR_INVC_LN_NMBR);
			entity.setPrfApWTaxRealization(PRF_AP_W_TX_RLZTN);
			entity.setPrfArWTaxRealization(PRF_AR_W_TX_RLZTN);
			entity.setPrfEnableGlJournalBatch(PRF_ENBL_GL_JRNL_BTCH);
			entity.setPrfEnableGlRecomputeCoaBalance(PRF_ENBL_GL_RCMPT_COA_BLNC);
			entity.setPrfEnableApVoucherBatch(PRF_ENBL_AP_VCHR_BTCH);
			entity.setPrfEnableApPOBatch(PRF_ENBL_AP_PO_BTCH);
			entity.setPrfEnableApCheckBatch(PRF_ENBL_AP_CHCK_BTCH);
			entity.setPrfEnableArInvoiceBatch(PRF_ENBL_AR_INVC_BTCH);
			entity.setPrfEnableArInvoiceInterestGeneration(PRF_ENBL_AR_INVC_INT_GNRTN);
			entity.setPrfEnableArReceiptBatch(PRF_ENBL_AR_RCPT_BTCH);
			entity.setPrfEnableArMiscReceiptBatch(PRF_ENBL_AR_MISC_RCPT_BTCH);
			entity.setPrfApGlPostingType(PRF_AP_GL_PSTNG_TYP);
			entity.setPrfArGlPostingType(PRF_AR_GL_PSTNG_TYP);
			entity.setPrfCmGlPostingType(PRF_CM_GL_PSTNG_TYP);
			entity.setPrfInvInventoryLineNumber(PRF_INV_INVNTRY_LN_NMBR);
			entity.setPrfInvQuantityPrecisionUnit(PRF_INV_QTY_PRCSN_UNT);
			entity.setPrfInvCostPrecisionUnit(PRF_INV_CST_PRCSN_UNT);
			entity.setPrfInvGlPostingType(PRF_INV_GL_PSTNG_TYP);
			entity.setPrfGlPostingType(PRF_GL_PSTNG_TYP);
			entity.setPrfInvEnableShift(PRF_INV_ENBL_SHFT);
			entity.setPrfEnableInvBUABatch(PRF_ENBL_INV_BUA_BTCH);
			entity.setPrfApFindCheckDefaultType(PRF_AP_FND_CHCK_DFLT_TYP);
			entity.setPrfArFindReceiptDefaultType(PRF_AR_FND_RCPT_DFLT_TYP);
			entity.setPrfApUseAccruedVat(PRF_AP_US_ACCRD_VAT);
			entity.setPrfApDefaultCheckDate(PRF_AP_DFLT_CHCK_DT);
			entity.setPrfApDefaultChecker(PRF_AP_DFLT_CHCKR);
			entity.setPrfApDefaultApprover(PRF_AP_DFLT_APPRVR);
			entity.setPrfApGlCoaAccruedVatAccount(PRF_AP_GL_COA_ACCRD_VAT_ACCNT);
			entity.setPrfApGlCoaPettyCashAccount(PRF_AP_GL_COA_PTTY_CSH_ACCNT);
			entity.setPrfCmUseBankForm(PRF_CM_USE_BNK_FRM);
			entity.setPrfApUseSupplierPulldown(PRF_AP_USE_SPPLR_PLLDWN);
			entity.setPrfArUseCustomerPulldown(PRF_AR_USE_CSTMR_PLLDWN);
			entity.setPrfApAutoGenerateSupplierCode(PRF_AP_AT_GNRT_SPPLR_CODE);
			entity.setPrfApNextSupplierCode(PRF_AP_NXT_SPPLR_CODE);
			entity.setPrfApReferenceNumberValidation(PRF_AP_RFRNC_NMBR_VLDTN);
			entity.setPrfInvEnablePosIntegration(PRF_INV_ENBL_POS_INTGRTN);
			entity.setPrfInvEnablePosAutoPostUpload(PRF_INV_ENBL_POS_AUTO_POST_UP);
			entity.setPrfInvPosAdjustmentAccount(PRF_INV_POS_ADJSTMNT_ACCNT);
			entity.setPrfApCheckVoucherDataSource(PRF_AP_CHK_VCHR_DT_SRC);
			entity.setPrfMiscPosDiscountAccount(PRF_MISC_POS_DSCNT_ACCNT);
			entity.setPrfMiscPosGiftCertificateAccount(PRF_MISC_POS_GFT_CRTFCT_ACCNT);
			entity.setPrfMiscPosServiceChargeAccount(PRF_MISC_POS_SRVC_CHRG_ACCNT);
			entity.setPrfMiscPosDineInChargeAccount(PRF_MISC_POS_DN_IN_CHRG_ACCNT);
			entity.setPrfArGlCoaCustomerDepositAccount(PRF_AR_GL_COA_CSTMR_DPST_ACCNT);
			entity.setPrfApDefaultPrTax(PRF_AP_DFLT_PR_TX);
			entity.setPrfApDefaultPrCurrency(PRF_AP_DFLT_PR_CRRNCY);
			entity.setPrfArSalesInvoiceDataSource(PRF_AR_SLS_INVC_DT_SRC);
			entity.setPrfInvGlCoaVarianceAccount(PRF_INV_GL_COA_VRNC_ACCNT);
			entity.setPrfArAutoComputeCogs(PRF_AR_AUTO_CMPUTE_COGS);
			entity.setPrfArMonthlyInterestRate(PRF_AR_MNTH_INT_RT);
			entity.setPrfApAgingBucket(PRF_AP_AGNG_BCKT);
			entity.setPrfArAgingBucket(PRF_AR_AGNG_BCKT);
			entity.setPrfArAllowPriorDate(PRF_AR_ALLW_PRR_DT);
			entity.setPrfArCheckInsufficientStock(PRF_AR_CHK_INSFFCNT_STCK);
			entity.setPrfArDetailedReceivable(PRF_AR_DTLD_RCVBL);
			entity.setPrfApShowPrCost(PRF_AP_SHW_PR_CST);
			entity.setPrfArEnablePaymentTerm(PRF_AR_ENBL_PYMNT_TRM);
			entity.setPrfArSoSalespersonRequired(PRF_AR_INVC_SLSPRSN_RQRD);
			entity.setPrfArInvcSalespersonRequired(PRF_AR_INVC_SLSPRSN_RQRD);
			entity.setPrfMailHost(PRF_ML_HST);
			entity.setPrfMailSocketFactoryPort(PRF_ML_SCKT_FCTRY_PRT);
			entity.setPrfMailPort(PRF_ML_PRT);
			entity.setPrfMailFrom(PRF_ML_FRM);
			entity.setPrfMailAuthenticator(PRF_ML_AUTH);
			entity.setPrfMailPassword(PRF_ML_PSSWRD);
			entity.setPrfMailTo(PRF_ML_TO);
			entity.setPrfMailCc(PRF_ML_CC);
			entity.setPrfMailBcc(PRF_ML_BCC);
			entity.setPrfMailConfig(PRF_ML_CNFG);
			entity.setPrfUserRestWebService(PRF_USR_RST_WB_SRVC);
			entity.setPrfPassRestWebService(PRF_PSS_RST_WB_SRVC);
			entity.setPrfAttachmentPath(PRF_ATTCHMNT_PTH);
			entity.setPrfAdCompany(PRF_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdPreference create(byte PRF_ALLW_SSPNS_PSTNG, short PRF_GL_JRNL_LN_NMBR,
			short PRF_AP_JRNL_LN_NMBR, short PRF_AR_INVC_LN_NMBR, String PRF_AP_W_TX_RLZTN, String PRF_AR_W_TX_RLZTN,
			byte PRF_ENBL_GL_JRNL_BTCH, byte PRF_ENBL_GL_RCMPT_COA_BLNC, byte PRF_ENBL_AP_VCHR_BTCH,
			byte PRF_ENBL_AP_PO_BTCH, byte PRF_ENBL_AP_CHCK_BTCH, byte PRF_ENBL_AR_INVC_BTCH,
			byte PRF_ENBL_AR_INVC_INT_GNRTN, byte PRF_ENBL_AR_RCPT_BTCH, byte PRF_ENBL_AR_MISC_RCPT_BTCH,
			String PRF_AP_GL_PSTNG_TYP, String PRF_AR_GL_PSTNG_TYP, String PRF_CM_GL_PSTNG_TYP,
			short PRF_INV_INVNTRY_LN_NMBR, short PRF_INV_QTY_PRCSN_UNT, short PRF_INV_CST_PRCSN_UNT,
			String PRF_INV_GL_PSTNG_TYP, String PRF_GL_PSTNG_TYP, byte PRF_INV_ENBL_SHFT, byte PRF_ENBL_INV_BUA_BTCH,
			String PRF_AP_FND_CHCK_DFLT_TYP, String PRF_AR_FND_RCPT_DFLT_TYP, byte PRF_AP_US_ACCRD_VAT,
			String PRF_AP_DFLT_CHCK_DT, String PRF_AP_DFLT_CHCKR, String PRF_AP_DFLT_APPRVR,
			Integer PRF_AP_GL_COA_ACCRD_VAT_ACCNT, Integer PRF_AP_GL_COA_PTTY_CSH_ACCNT, byte PRF_CM_USE_BNK_FRM,
			byte PRF_AP_USE_SPPLR_PLLDWN, byte PRF_AR_USE_CSTMR_PLLDWN, byte PRF_AP_AT_GNRT_SPPLR_CODE,
			String PRF_AP_NXT_SPPLR_CODE, byte PRF_AR_AT_GNRT_CSTMR_CODE, String PRF_AR_NXT_CSTMR_CODE,
			byte PRF_AP_RFRNC_NMBR_VLDTN, byte PRF_INV_ENBL_POS_INTGRTN, byte PRF_INV_ENBL_POS_AUTO_POST_UP,
			Integer PRF_INV_POS_ADJSTMNT_ACCNT, String PRF_AP_CHK_VCHR_DT_SRC, Integer PRF_MISC_POS_DSCNT_ACCNT,
			Integer PRF_MISC_POS_GFT_CRTFCT_ACCNT, Integer PRF_MISC_POS_SRVC_CHRG_ACCNT,
			Integer PRF_MISC_POS_DN_IN_CHRG_ACCNT, Integer PRF_AR_GL_COA_CSTMR_DPST_ACCNT, String PRF_AP_DFLT_PR_TX,
			String PRF_AP_DFLT_PR_CRRNCY, String PRF_AR_SLS_INVC_DT_SRC, Integer PRF_INV_GL_COA_VRNC_ACCNT,
			byte PRF_AR_AUTO_CMPUTE_COGS, double PRF_AR_MNTH_INT_RT, int PRF_AP_AGNG_BCKT, int PRF_AR_AGNG_BCKT,
			byte PRF_AR_ALLW_PRR_DT, byte PRF_AR_CHK_INSFFCNT_STCK, byte PRF_AR_DTLD_RCVBL, byte PRF_AP_SHW_PR_CST,
			byte PRF_AR_ENBL_PYMNT_TRM, byte PRF_AR_DSBL_SLS_PRC, byte PRF_INV_IL_SHW_ALL,
			byte PRF_INV_IL_ADD_BY_ITM_LST, byte PRF_AP_DM_OVRRD_CST, byte PRF_GL_YR_END_CLS_RSTRCTN,
			byte PRF_AD_DSBL_MTPL_LGN, byte PRF_AD_ENBL_EML_NTFCTN, byte PRF_AR_SO_SLSPRSN_RQRD,
			byte PRF_AR_INVC_SLSPRSN_RQRD, String PRF_ML_HST, String PRF_ML_SCKT_FCTRY_PRT, String PRF_ML_PRT,
			String PRF_ML_FRM, byte PRF_ML_AUTH, String PRF_ML_PSSWRD, String PRF_ML_TO, String PRF_ML_CC,
			String PRF_ML_BCC, String PRF_ML_CNFG, String PRF_USR_RST_WB_SRVC, String PRF_PSS_RST_WB_SRVC, String PRF_ATTCHMNT_PTH,
			Integer PRF_AD_CMPNY) throws CreateException {
		try {

			LocalAdPreference entity = new LocalAdPreference();

			Debug.print("AdPreferenceBean create");
			entity.setPrfAllowSuspensePosting(PRF_ALLW_SSPNS_PSTNG);
			entity.setPrfGlJournalLineNumber(PRF_GL_JRNL_LN_NMBR);
			entity.setPrfApJournalLineNumber(PRF_AP_JRNL_LN_NMBR);
			entity.setPrfArInvoiceLineNumber(PRF_AR_INVC_LN_NMBR);
			entity.setPrfApWTaxRealization(PRF_AP_W_TX_RLZTN);
			entity.setPrfArWTaxRealization(PRF_AR_W_TX_RLZTN);
			entity.setPrfEnableGlJournalBatch(PRF_ENBL_GL_JRNL_BTCH);
			entity.setPrfEnableGlRecomputeCoaBalance(PRF_ENBL_GL_RCMPT_COA_BLNC);
			entity.setPrfEnableApVoucherBatch(PRF_ENBL_AP_VCHR_BTCH);
			entity.setPrfEnableApPOBatch(PRF_ENBL_AP_PO_BTCH);
			entity.setPrfEnableApCheckBatch(PRF_ENBL_AP_CHCK_BTCH);
			entity.setPrfEnableArInvoiceBatch(PRF_ENBL_AR_INVC_BTCH);
			entity.setPrfEnableArInvoiceInterestGeneration(PRF_ENBL_AR_INVC_INT_GNRTN);
			entity.setPrfEnableArReceiptBatch(PRF_ENBL_AR_RCPT_BTCH);
			entity.setPrfEnableArMiscReceiptBatch(PRF_ENBL_AR_MISC_RCPT_BTCH);
			entity.setPrfApGlPostingType(PRF_AP_GL_PSTNG_TYP);
			entity.setPrfArGlPostingType(PRF_AR_GL_PSTNG_TYP);
			entity.setPrfCmGlPostingType(PRF_CM_GL_PSTNG_TYP);
			entity.setPrfInvInventoryLineNumber(PRF_INV_INVNTRY_LN_NMBR);
			entity.setPrfInvQuantityPrecisionUnit(PRF_INV_QTY_PRCSN_UNT);
			entity.setPrfInvCostPrecisionUnit(PRF_INV_CST_PRCSN_UNT);
			entity.setPrfInvGlPostingType(PRF_INV_GL_PSTNG_TYP);
			entity.setPrfGlPostingType(PRF_GL_PSTNG_TYP);
			entity.setPrfInvEnableShift(PRF_INV_ENBL_SHFT);
			entity.setPrfEnableInvBUABatch(PRF_ENBL_INV_BUA_BTCH);
			entity.setPrfApFindCheckDefaultType(PRF_AP_FND_CHCK_DFLT_TYP);
			entity.setPrfArFindReceiptDefaultType(PRF_AR_FND_RCPT_DFLT_TYP);
			entity.setPrfApUseAccruedVat(PRF_AP_US_ACCRD_VAT);
			entity.setPrfApDefaultCheckDate(PRF_AP_DFLT_CHCK_DT);
			entity.setPrfApDefaultChecker(PRF_AP_DFLT_CHCKR);
			entity.setPrfApDefaultApprover(PRF_AP_DFLT_APPRVR);
			entity.setPrfApGlCoaAccruedVatAccount(PRF_AP_GL_COA_ACCRD_VAT_ACCNT);
			entity.setPrfApGlCoaPettyCashAccount(PRF_AP_GL_COA_PTTY_CSH_ACCNT);
			entity.setPrfCmUseBankForm(PRF_CM_USE_BNK_FRM);
			entity.setPrfApUseSupplierPulldown(PRF_AP_USE_SPPLR_PLLDWN);
			entity.setPrfArUseCustomerPulldown(PRF_AR_USE_CSTMR_PLLDWN);
			entity.setPrfApAutoGenerateSupplierCode(PRF_AP_AT_GNRT_SPPLR_CODE);
			entity.setPrfApNextSupplierCode(PRF_AP_NXT_SPPLR_CODE);
			entity.setPrfApReferenceNumberValidation(PRF_AP_RFRNC_NMBR_VLDTN);
			entity.setPrfInvEnablePosIntegration(PRF_INV_ENBL_POS_INTGRTN);
			entity.setPrfInvEnablePosAutoPostUpload(PRF_INV_ENBL_POS_AUTO_POST_UP);
			entity.setPrfInvPosAdjustmentAccount(PRF_INV_POS_ADJSTMNT_ACCNT);
			entity.setPrfApCheckVoucherDataSource(PRF_AP_CHK_VCHR_DT_SRC);
			entity.setPrfMiscPosDiscountAccount(PRF_MISC_POS_DSCNT_ACCNT);
			entity.setPrfMiscPosGiftCertificateAccount(PRF_MISC_POS_GFT_CRTFCT_ACCNT);
			entity.setPrfMiscPosServiceChargeAccount(PRF_MISC_POS_SRVC_CHRG_ACCNT);
			entity.setPrfMiscPosDineInChargeAccount(PRF_MISC_POS_DN_IN_CHRG_ACCNT);
			entity.setPrfArGlCoaCustomerDepositAccount(PRF_AR_GL_COA_CSTMR_DPST_ACCNT);
			entity.setPrfApDefaultPrTax(PRF_AP_DFLT_PR_TX);
			entity.setPrfApDefaultPrCurrency(PRF_AP_DFLT_PR_CRRNCY);
			entity.setPrfArSalesInvoiceDataSource(PRF_AR_SLS_INVC_DT_SRC);
			entity.setPrfInvGlCoaVarianceAccount(PRF_INV_GL_COA_VRNC_ACCNT);
			entity.setPrfArAutoComputeCogs(PRF_AR_AUTO_CMPUTE_COGS);
			entity.setPrfArMonthlyInterestRate(PRF_AR_MNTH_INT_RT);
			entity.setPrfApAgingBucket(PRF_AP_AGNG_BCKT);
			entity.setPrfArAgingBucket(PRF_AR_AGNG_BCKT);
			entity.setPrfArAllowPriorDate(PRF_AR_ALLW_PRR_DT);
			entity.setPrfArCheckInsufficientStock(PRF_AR_CHK_INSFFCNT_STCK);
			entity.setPrfArDetailedReceivable(PRF_AR_DTLD_RCVBL);
			entity.setPrfApShowPrCost(PRF_AP_SHW_PR_CST);
			entity.setPrfArEnablePaymentTerm(PRF_AR_ENBL_PYMNT_TRM);
			entity.setPrfArDisableSalesPrice(PRF_AR_DSBL_SLS_PRC);
			entity.setPrfInvItemLocationShowAll(PRF_INV_IL_SHW_ALL);
			entity.setPrfInvItemLocationAddByItemList(PRF_INV_IL_ADD_BY_ITM_LST);
			entity.setPrfApDebitMemoOverrideCost(PRF_AP_DM_OVRRD_CST);
			entity.setPrfGlYearEndCloseRestriction(PRF_GL_YR_END_CLS_RSTRCTN);
			entity.setPrfAdDisableMultipleLogin(PRF_AD_DSBL_MTPL_LGN);
			entity.setPrfAdEnableEmailNotification(PRF_AD_ENBL_EML_NTFCTN);
			entity.setPrfArSoSalespersonRequired(PRF_AR_INVC_SLSPRSN_RQRD);
			entity.setPrfArInvcSalespersonRequired(PRF_AR_INVC_SLSPRSN_RQRD);
			entity.setPrfMailHost(PRF_ML_HST);
			entity.setPrfMailSocketFactoryPort(PRF_ML_SCKT_FCTRY_PRT);
			entity.setPrfMailPort(PRF_ML_PRT);
			entity.setPrfMailFrom(PRF_ML_FRM);
			entity.setPrfMailAuthenticator(PRF_ML_AUTH);
			entity.setPrfMailPassword(PRF_ML_PSSWRD);
			entity.setPrfMailTo(PRF_ML_TO);
			entity.setPrfMailCc(PRF_ML_CC);
			entity.setPrfMailBcc(PRF_ML_BCC);
			entity.setPrfMailConfig(PRF_ML_CNFG);
			entity.setPrfUserRestWebService(PRF_USR_RST_WB_SRVC);
			entity.setPrfPassRestWebService(PRF_PSS_RST_WB_SRVC);
			entity.setPrfAttachmentPath(PRF_ATTCHMNT_PTH);
			entity.setPrfAdCompany(PRF_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}