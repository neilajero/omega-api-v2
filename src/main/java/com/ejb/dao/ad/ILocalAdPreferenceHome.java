package com.ejb.dao.ad;

import com.ejb.entities.ad.LocalAdPreference;

import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;

public interface ILocalAdPreferenceHome {
	
	LocalAdPreference findByPrimaryKey(java.lang.Integer pk) throws FinderException;

	LocalAdPreference findByPrfAdCompany(java.lang.Integer PRF_AD_CMPNY) throws FinderException;

	LocalAdPreference findByPrfAdCompany(java.lang.Integer PRF_AD_CMPNY, String companyShortName) throws FinderException;
	
	LocalAdPreference buildPreference() throws CreateException;

	LocalAdPreference create(java.lang.Integer PRF_CODE, byte PRF_ALLW_SSPNS_PSTNG,
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
                             String PRF_PSS_RST_WB_SRVC, String PRF_ATTCHMNT_PTH, Integer PRF_AD_CMPNY) throws CreateException;

	LocalAdPreference create(byte PRF_ALLW_SSPNS_PSTNG, short PRF_GL_JRNL_LN_NMBR,
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
                             String PRF_PSS_RST_WB_SRVC, String PRF_ATTCHMNT_PTH, Integer PRF_AD_CMPNY) throws CreateException;
	
	LocalAdPreference create(byte PRF_ALLW_SSPNS_PSTNG, short PRF_GL_JRNL_LN_NMBR,
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
                             Integer PRF_AD_CMPNY) throws CreateException;

	
	
}