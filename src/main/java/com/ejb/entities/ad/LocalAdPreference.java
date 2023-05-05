package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ar.LocalArWithholdingTaxCode;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdPreference")
@Table(name = "AD_PRFRNC")
public class LocalAdPreference extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRF_CODE", nullable = false)
    private Integer prfCode;

    @Column(name = "PRF_ALLW_SSPNS_PSTNG", columnDefinition = "TINYINT")
    private byte prfAllowSuspensePosting;

    @Column(name = "PRF_GL_JRNL_LN_NMBR", columnDefinition = "SMALLTINT")
    private short prfGlJournalLineNumber;

    @Column(name = "PRF_AP_JRNL_LN_NMBR", columnDefinition = "SMALLTINT")
    private short prfApJournalLineNumber;

    @Column(name = "PRF_AR_INVC_LN_NMBR", columnDefinition = "SMALLTINT")
    private short prfArInvoiceLineNumber;

    @Column(name = "PRF_AP_W_TX_RLZTN", columnDefinition = "VARCHAR")
    private String prfApWTaxRealization;

    @Column(name = "PRF_AR_W_TX_RLZTN", columnDefinition = "VARCHAR")
    private String prfArWTaxRealization;

    @Column(name = "PRF_ENBL_GL_JRNL_BTCH", columnDefinition = "TINYINT")
    private byte prfEnableGlJournalBatch;

    @Column(name = "PRF_ENBL_GL_RCMPT_COA_BLNC", columnDefinition = "TINYINT")
    private byte prfEnableGlRecomputeCoaBalance;

    @Column(name = "PRF_ENBL_AP_VCHR_BTCH", columnDefinition = "TINYINT")
    private byte prfEnableApVoucherBatch;

    @Column(name = "PRF_ENBL_AP_PO_BTCH", columnDefinition = "TINYINT")
    private byte prfEnableApPOBatch;

    @Column(name = "PRF_ENBL_AP_CHCK_BTCH", columnDefinition = "TINYINT")
    private byte prfEnableApCheckBatch;

    @Column(name = "PRF_ENBL_AR_INVC_BTCH", columnDefinition = "TINYINT")
    private byte prfEnableArInvoiceBatch;

    @Column(name = "PRF_ENBL_AR_INVC_INT_GNRTN", columnDefinition = "TINYINT")
    private byte prfEnableArInvoiceInterestGeneration;

    @Column(name = "PRF_ENBL_AR_RCPT_BTCH", columnDefinition = "TINYINT")
    private byte prfEnableArReceiptBatch;

    @Column(name = "PRF_ENBL_AR_MISC_RCPT_BTCH", columnDefinition = "TINYINT")
    private byte prfEnableArMiscReceiptBatch;

    @Column(name = "PRF_AP_GL_PSTNG_TYP", columnDefinition = "VARCHAR")
    private String prfApGlPostingType;

    @Column(name = "PRF_AR_GL_PSTNG_TYP", columnDefinition = "VARCHAR")
    private String prfArGlPostingType;

    @Column(name = "PRF_CM_GL_PSTNG_TYP", columnDefinition = "VARCHAR")
    private String prfCmGlPostingType;

    @Column(name = "PRF_CM_USE_BNK_FRM", columnDefinition = "TINYINT")
    private byte prfCmUseBankForm;

    @Column(name = "PRF_INV_INVNTRY_LN_NMBR", columnDefinition = "SMALLINT")
    private short prfInvInventoryLineNumber;

    @Column(name = "PRF_INV_QTY_PRCSN_UNT", columnDefinition = "SMALLINT")
    private short prfInvQuantityPrecisionUnit;

    @Column(name = "PRF_INV_CST_PRCSN_UNT", columnDefinition = "SMALLINT")
    private short prfInvCostPrecisionUnit;

    @Column(name = "PRF_INV_GL_PSTNG_TYP", columnDefinition = "VARCHAR")
    private String prfInvGlPostingType;

    @Column(name = "PRF_GL_PSTNG_TYP", columnDefinition = "VARCHAR")
    private String prfGlPostingType;

    @Column(name = "PRF_INV_ENBL_SHFT", columnDefinition = "TINYINT")
    private byte prfInvEnableShift;

    @Column(name = "PRF_ENBL_INV_BUA_BTCH", columnDefinition = "TINYINT")
    private byte prfEnableInvBUABatch;

    @Column(name = "PRF_AP_FND_CHCK_DFLT_TYP", columnDefinition = "VARCHAR")
    private String prfApFindCheckDefaultType;

    @Column(name = "PRF_AR_FND_RCPT_DFLT_TYP", columnDefinition = "VARCHAR")
    private String prfArFindReceiptDefaultType;

    @Column(name = "PRF_AP_DFLT_CHCKR", columnDefinition = "VARCHAR")
    private String prfApDefaultChecker;

    @Column(name = "PRF_AP_DFLT_APPRVR", columnDefinition = "VARCHAR")
    private String prfApDefaultApprover;

    @Column(name = "PRF_AP_US_ACCRD_VAT", columnDefinition = "TINYINT")
    private byte prfApUseAccruedVat;

    @Column(name = "PRF_AP_DFLT_CHCK_DT", columnDefinition = "VARCHAR")
    private String prfApDefaultCheckDate;

    @Column(name = "PRF_AP_GL_COA_ACCRD_VAT_ACCNT", columnDefinition = "INT")
    private Integer prfApGlCoaAccruedVatAccount;

    @Column(name = "PRF_AP_GL_COA_PTTY_CSH_ACCNT", columnDefinition = "INT")
    private Integer prfApGlCoaPettyCashAccount;

    @Column(name = "PRF_AP_USE_SPPLR_PLLDWN", columnDefinition = "TINYINT")
    private byte prfApUseSupplierPulldown;

    @Column(name = "PRF_AR_USE_CSTMR_PLLDWN", columnDefinition = "TINYINT")
    private byte prfArUseCustomerPulldown;

    @Column(name = "PRF_AP_AT_GNRT_SPPLR_CODE", columnDefinition = "TINYINT")
    private byte prfApAutoGenerateSupplierCode;

    @Column(name = "PRF_AP_NXT_SPPLR_CODE", columnDefinition = "VARCHAR")
    private String prfApNextSupplierCode;

    @Column(name = "PRF_AP_RFRNC_NMBR_VLDTN", columnDefinition = "TINYINT")
    private byte prfApReferenceNumberValidation;

    @Column(name = "PRF_INV_ENBL_POS_INTGRTN", columnDefinition = "TINYINT")
    private byte prfInvEnablePosIntegration;

    @Column(name = "PRF_INV_ENBL_POS_AUTO_POST_UP", columnDefinition = "TINYINT")
    private byte prfInvEnablePosAutoPostUpload;

    @Column(name = "PRF_INV_POS_ADJSTMNT_ACCNT", columnDefinition = "INT")
    private Integer prfInvPosAdjustmentAccount;

    @Column(name = "PRF_INV_GNRL_ADJSTMNT_ACCNT", columnDefinition = "INT")
    private Integer prfInvGeneralAdjustmentAccount;

    @Column(name = "PRF_INV_ISSNC_ADJSTMNT_ACCNT", columnDefinition = "INT")
    private Integer prfInvIssuanceAdjustmentAccount;

    @Column(name = "PRF_INV_PRDCTN_ADJSTMNT_ACCNT", columnDefinition = "INT")
    private Integer prfInvProductionAdjustmentAccount;

    @Column(name = "PRF_INV_WSTG_ADJSTMNT_ACCNT", columnDefinition = "INT")
    private Integer prfInvWastageAdjustmentAccount;

    @Column(name = "PRF_INV_CNTRL_WRHS", columnDefinition = "VARCHAR")
    private String prfInvCentralWarehouse;

    @Column(name = "PRF_AP_CHK_VCHR_DT_SRC", columnDefinition = "VARCHAR")
    private String prfApCheckVoucherDataSource;

    @Column(name = "PRF_MISC_POS_DSCNT_ACCNT", columnDefinition = "INT")
    private Integer prfMiscPosDiscountAccount;

    @Column(name = "PRF_MISC_POS_GFT_CRTFCT_ACCNT", columnDefinition = "INT")
    private Integer prfMiscPosGiftCertificateAccount;

    @Column(name = "PRF_MISC_POS_SRVC_CHRG_ACCNT", columnDefinition = "INT")
    private Integer prfMiscPosServiceChargeAccount;

    @Column(name = "PRF_MISC_POS_DN_IN_CHRG_ACCNT", columnDefinition = "INT")
    private Integer prfMiscPosDineInChargeAccount;

    @Column(name = "PRF_AP_DFLT_PR_TX", columnDefinition = "VARCHAR")
    private String prfApDefaultPrTax;

    @Column(name = "PRF_AP_DFLT_PR_CRRNCY", columnDefinition = "VARCHAR")
    private String prfApDefaultPrCurrency;

    @Column(name = "PRF_AR_GL_COA_CSTMR_DPST_ACCNT", columnDefinition = "VARCHAR")
    private Integer prfArGlCoaCustomerDepositAccount;

    @Column(name = "PRF_AR_SLS_INVC_DT_SRC", columnDefinition = "VARCHAR")
    private String prfArSalesInvoiceDataSource;

    @Column(name = "PRF_INV_NXT_CSTDN_NMBR1", columnDefinition = "VARCHAR")
    private String prfInvNextCustodianNumber1;

    @Column(name = "PRF_INV_NXT_CSTDN_NMBR2", columnDefinition = "VARCHAR")
    private String prfInvNextCustodianNumber2;

    @Column(name = "PRF_INV_GL_COA_VRNC_ACCNT", columnDefinition = "VARCHAR")
    private Integer prfInvGlCoaVarianceAccount;

    @Column(name = "PRF_AR_AUTO_CMPUTE_COGS", columnDefinition = "TINYINT")
    private byte prfArAutoComputeCogs;

    @Column(name = "PRF_AR_MNTH_INT_RT", columnDefinition = "DOUBLE")
    private double prfArMonthlyInterestRate = 0;

    @Column(name = "PRF_AP_AGNG_BCKT", columnDefinition = "INT")
    private Integer prfApAgingBucket;

    @Column(name = "PRF_AR_AGNG_BCKT", columnDefinition = "INT")
    private Integer prfArAgingBucket;

    @Column(name = "PRF_AR_ALLW_PRR_DT", columnDefinition = "TINYINT")
    private byte prfArAllowPriorDate;

    @Column(name = "PRF_AR_CHK_INSFFCNT_STCK", columnDefinition = "TINYINT")
    private byte prfArCheckInsufficientStock;

    @Column(name = "PRF_AR_DTLD_RCVBL", columnDefinition = "TINYINT")
    private byte prfArDetailedReceivable;

    @Column(name = "PRF_AP_SHW_PR_CST", columnDefinition = "TINYINT")
    private byte prfApShowPrCost;

    @Column(name = "PRF_AR_ENBL_PYMNT_TRM", columnDefinition = "TINYINT")
    private byte prfArEnablePaymentTerm;

    @Column(name = "PRF_AR_DSBL_SLS_PRC", columnDefinition = "TINYINT")
    private byte prfArDisableSalesPrice;

    @Column(name = "PRF_AR_VLDT_CST_EMAIL", columnDefinition = "TINYINT")
    private byte prfArValidateCustomerEmail;

    @Column(name = "PRF_INV_IL_SHW_ALL", columnDefinition = "TINYINT")
    private byte prfInvItemLocationShowAll;

    @Column(name = "PRF_INV_IL_ADD_BY_ITM_LST", columnDefinition = "TINYINT")
    private byte prfInvItemLocationAddByItemList;

    @Column(name = "PRF_AP_DM_OVRRD_CST", columnDefinition = "TINYINT")
    private byte prfApDebitMemoOverrideCost;

    @Column(name = "PRF_GL_YR_END_CLS_RSTRCTN", columnDefinition = "TINYINT")
    private byte prfGlYearEndCloseRestriction;

    @Column(name = "PRF_AD_DSBL_MTPL_LGN", columnDefinition = "TINYINT")
    private byte prfAdDisableMultipleLogin;

    @Column(name = "PRF_AD_ENBL_EML_NTFCTN", columnDefinition = "TINYINT")
    private byte prfAdEnableEmailNotification;

    @Column(name = "PRF_AR_SO_SLSPRSN_RQRD", columnDefinition = "TINYINT")
    private byte prfArSoSalespersonRequired;

    @Column(name = "PRF_AR_INVC_SLSPRSN_RQRD", columnDefinition = "TINYINT")
    private byte prfArInvcSalespersonRequired;

    @Column(name = "PRF_ML_HST", columnDefinition = "VARCHAR")
    private String prfMailHost;

    @Column(name = "PRF_ML_SCKT_FCTRY_PRT", columnDefinition = "VARCHAR")
    private String prfMailSocketFactoryPort;

    @Column(name = "PRF_ML_PRT", columnDefinition = "VARCHAR")
    private String prfMailPort;

    @Column(name = "PRF_ML_FRM", columnDefinition = "VARCHAR")
    private String prfMailFrom;

    @Column(name = "PRF_ML_AUTH", columnDefinition = "TINYINT")
    private byte prfMailAuthenticator;

    @Column(name = "PRF_ML_PSSWRD", columnDefinition = "VARCHAR")
    private String prfMailPassword;

    @Column(name = "PRF_ML_TO", columnDefinition = "VARCHAR")
    private String prfMailTo;

    @Column(name = "PRF_ML_CC", columnDefinition = "VARCHAR")
    private String prfMailCc;

    @Column(name = "PRF_ML_BCC", columnDefinition = "VARCHAR")
    private String prfMailBcc;

    @Column(name = "PRF_ML_CNFG", columnDefinition = "VARCHAR")
    private String prfMailConfig;

    @Column(name = "PRF_USR_RST_WB_SRVC", columnDefinition = "VARCHAR")
    private String prfUserRestWebService;

    @Column(name = "PRF_PSS_RST_WB_SRVC", columnDefinition = "VARCHAR")
    private String prfPassRestWebService;

    @Column(name = "PRF_ATTCHMNT_PTH", columnDefinition = "VARCHAR")
    private String prfAttachmentPath;

    @Column(name = "PRF_AD_CMPNY", columnDefinition = "INT")
    private Integer prfAdCompany;

    @JoinColumn(name = "AR_WITHHOLDING_TAX_CODE", referencedColumnName = "AR_WTC_CODE")
    @ManyToOne
    private LocalArWithholdingTaxCode arWithholdingTaxCode;

    public Integer getPrfCode() {

        return prfCode;
    }

    public void setPrfCode(Integer PRF_CODE) {

        this.prfCode = PRF_CODE;
    }

    public byte getPrfAllowSuspensePosting() {

        return prfAllowSuspensePosting;
    }

    public void setPrfAllowSuspensePosting(byte PRF_ALLW_SSPNS_PSTNG) {

        this.prfAllowSuspensePosting = PRF_ALLW_SSPNS_PSTNG;
    }

    public short getPrfGlJournalLineNumber() {

        return prfGlJournalLineNumber;
    }

    public void setPrfGlJournalLineNumber(short PRF_GL_JRNL_LN_NMBR) {

        this.prfGlJournalLineNumber = PRF_GL_JRNL_LN_NMBR;
    }

    public short getPrfApJournalLineNumber() {

        return prfApJournalLineNumber;
    }

    public void setPrfApJournalLineNumber(short PRF_AP_JRNL_LN_NMBR) {

        this.prfApJournalLineNumber = PRF_AP_JRNL_LN_NMBR;
    }

    public short getPrfArInvoiceLineNumber() {

        return prfArInvoiceLineNumber;
    }

    public void setPrfArInvoiceLineNumber(short PRF_AR_INVC_LN_NMBR) {

        this.prfArInvoiceLineNumber = PRF_AR_INVC_LN_NMBR;
    }

    public String getPrfApWTaxRealization() {

        return prfApWTaxRealization;
    }

    public void setPrfApWTaxRealization(String PRF_AP_W_TX_RLZTN) {

        this.prfApWTaxRealization = PRF_AP_W_TX_RLZTN;
    }

    public String getPrfArWTaxRealization() {

        return prfArWTaxRealization;
    }

    public void setPrfArWTaxRealization(String PRF_AR_W_TX_RLZTN) {

        this.prfArWTaxRealization = PRF_AR_W_TX_RLZTN;
    }

    public byte getPrfEnableGlJournalBatch() {

        return prfEnableGlJournalBatch;
    }

    public void setPrfEnableGlJournalBatch(byte PRF_ENBL_GL_JRNL_BTCH) {

        this.prfEnableGlJournalBatch = PRF_ENBL_GL_JRNL_BTCH;
    }

    public byte getPrfEnableGlRecomputeCoaBalance() {

        return prfEnableGlRecomputeCoaBalance;
    }

    public void setPrfEnableGlRecomputeCoaBalance(byte PRF_ENBL_GL_RCMPT_COA_BLNC) {

        this.prfEnableGlRecomputeCoaBalance = PRF_ENBL_GL_RCMPT_COA_BLNC;
    }

    public byte getPrfEnableApVoucherBatch() {

        return prfEnableApVoucherBatch;
    }

    public void setPrfEnableApVoucherBatch(byte PRF_ENBL_AP_VCHR_BTCH) {

        this.prfEnableApVoucherBatch = PRF_ENBL_AP_VCHR_BTCH;
    }

    public byte getPrfEnableApPOBatch() {

        return prfEnableApPOBatch;
    }

    public void setPrfEnableApPOBatch(byte PRF_ENBL_AP_PO_BTCH) {

        this.prfEnableApPOBatch = PRF_ENBL_AP_PO_BTCH;
    }

    public byte getPrfEnableApCheckBatch() {

        return prfEnableApCheckBatch;
    }

    public void setPrfEnableApCheckBatch(byte PRF_ENBL_AP_CHCK_BTCH) {

        this.prfEnableApCheckBatch = PRF_ENBL_AP_CHCK_BTCH;
    }

    public byte getPrfEnableArInvoiceBatch() {

        return prfEnableArInvoiceBatch;
    }

    public void setPrfEnableArInvoiceBatch(byte PRF_ENBL_AR_INVC_BTCH) {

        this.prfEnableArInvoiceBatch = PRF_ENBL_AR_INVC_BTCH;
    }

    public byte getPrfEnableArInvoiceInterestGeneration() {

        return prfEnableArInvoiceInterestGeneration;
    }

    public void setPrfEnableArInvoiceInterestGeneration(byte PRF_ENBL_AR_INVC_INT_GNRTN) {

        this.prfEnableArInvoiceInterestGeneration = PRF_ENBL_AR_INVC_INT_GNRTN;
    }

    public byte getPrfEnableArReceiptBatch() {

        return prfEnableArReceiptBatch;
    }

    public void setPrfEnableArReceiptBatch(byte PRF_ENBL_AR_RCPT_BTCH) {

        this.prfEnableArReceiptBatch = PRF_ENBL_AR_RCPT_BTCH;
    }

    public byte getPrfEnableArMiscReceiptBatch() {

        return prfEnableArMiscReceiptBatch;
    }

    public void setPrfEnableArMiscReceiptBatch(byte PRF_ENBL_AR_MISC_RCPT_BTCH) {

        this.prfEnableArMiscReceiptBatch = PRF_ENBL_AR_MISC_RCPT_BTCH;
    }

    public String getPrfApGlPostingType() {

        return prfApGlPostingType;
    }

    public void setPrfApGlPostingType(String PRF_AP_GL_PSTNG_TYP) {

        this.prfApGlPostingType = PRF_AP_GL_PSTNG_TYP;
    }

    public String getPrfArGlPostingType() {

        return prfArGlPostingType;
    }

    public void setPrfArGlPostingType(String PRF_AR_GL_PSTNG_TYP) {

        this.prfArGlPostingType = PRF_AR_GL_PSTNG_TYP;
    }

    public String getPrfCmGlPostingType() {

        return prfCmGlPostingType;
    }

    public void setPrfCmGlPostingType(String PRF_CM_GL_PSTNG_TYP) {

        this.prfCmGlPostingType = PRF_CM_GL_PSTNG_TYP;
    }

    public byte getPrfCmUseBankForm() {

        return prfCmUseBankForm;
    }

    public void setPrfCmUseBankForm(byte PRF_CM_USE_BNK_FRM) {

        this.prfCmUseBankForm = PRF_CM_USE_BNK_FRM;
    }

    public short getPrfInvInventoryLineNumber() {

        return prfInvInventoryLineNumber;
    }

    public void setPrfInvInventoryLineNumber(short PRF_INV_INVNTRY_LN_NMBR) {

        this.prfInvInventoryLineNumber = PRF_INV_INVNTRY_LN_NMBR;
    }

    public short getPrfInvQuantityPrecisionUnit() {

        return prfInvQuantityPrecisionUnit;
    }

    public void setPrfInvQuantityPrecisionUnit(short PRF_INV_QTY_PRCSN_UNT) {

        this.prfInvQuantityPrecisionUnit = PRF_INV_QTY_PRCSN_UNT;
    }

    public short getPrfInvCostPrecisionUnit() {

        return prfInvCostPrecisionUnit;
    }

    public void setPrfInvCostPrecisionUnit(short PRF_INV_CST_PRCSN_UNT) {

        this.prfInvCostPrecisionUnit = PRF_INV_CST_PRCSN_UNT;
    }

    public String getPrfInvGlPostingType() {

        return prfInvGlPostingType;
    }

    public void setPrfInvGlPostingType(String PRF_INV_GL_PSTNG_TYP) {

        this.prfInvGlPostingType = PRF_INV_GL_PSTNG_TYP;
    }

    public String getPrfGlPostingType() {

        return prfGlPostingType;
    }

    public void setPrfGlPostingType(String PRF_GL_PSTNG_TYP) {

        this.prfGlPostingType = PRF_GL_PSTNG_TYP;
    }

    public byte getPrfInvEnableShift() {

        return prfInvEnableShift;
    }

    public void setPrfInvEnableShift(byte PRF_INV_ENBL_SHFT) {

        this.prfInvEnableShift = PRF_INV_ENBL_SHFT;
    }

    public byte getPrfEnableInvBUABatch() {

        return prfEnableInvBUABatch;
    }

    public void setPrfEnableInvBUABatch(byte PRF_ENBL_INV_BUA_BTCH) {

        this.prfEnableInvBUABatch = PRF_ENBL_INV_BUA_BTCH;
    }

    public String getPrfApFindCheckDefaultType() {

        return prfApFindCheckDefaultType;
    }

    public void setPrfApFindCheckDefaultType(String PRF_AP_FND_CHCK_DFLT_TYP) {

        this.prfApFindCheckDefaultType = PRF_AP_FND_CHCK_DFLT_TYP;
    }

    public String getPrfArFindReceiptDefaultType() {

        return prfArFindReceiptDefaultType;
    }

    public void setPrfArFindReceiptDefaultType(String PRF_AR_FND_RCPT_DFLT_TYP) {

        this.prfArFindReceiptDefaultType = PRF_AR_FND_RCPT_DFLT_TYP;
    }

    public String getPrfApDefaultChecker() {

        return prfApDefaultChecker;
    }

    public void setPrfApDefaultChecker(String PRF_AP_DFLT_CHCKR) {

        this.prfApDefaultChecker = PRF_AP_DFLT_CHCKR;
    }

    public String getPrfApDefaultApprover() {

        return prfApDefaultApprover;
    }

    public void setPrfApDefaultApprover(String PRF_AP_DFLT_APPRVR) {

        this.prfApDefaultApprover = PRF_AP_DFLT_APPRVR;
    }

    public byte getPrfApUseAccruedVat() {

        return prfApUseAccruedVat;
    }

    public void setPrfApUseAccruedVat(byte PRF_AP_US_ACCRD_VAT) {

        this.prfApUseAccruedVat = PRF_AP_US_ACCRD_VAT;
    }

    public String getPrfApDefaultCheckDate() {

        return prfApDefaultCheckDate;
    }

    public void setPrfApDefaultCheckDate(String PRF_AP_DFLT_CHCK_DT) {

        this.prfApDefaultCheckDate = PRF_AP_DFLT_CHCK_DT;
    }

    public Integer getPrfApGlCoaAccruedVatAccount() {

        return prfApGlCoaAccruedVatAccount;
    }

    public void setPrfApGlCoaAccruedVatAccount(Integer PRF_AP_GL_COA_ACCRD_VAT_ACCNT) {

        this.prfApGlCoaAccruedVatAccount = PRF_AP_GL_COA_ACCRD_VAT_ACCNT;
    }

    public Integer getPrfApGlCoaPettyCashAccount() {

        return prfApGlCoaPettyCashAccount;
    }

    public void setPrfApGlCoaPettyCashAccount(Integer PRF_AP_GL_COA_PTTY_CSH_ACCNT) {

        this.prfApGlCoaPettyCashAccount = PRF_AP_GL_COA_PTTY_CSH_ACCNT;
    }

    public byte getPrfApUseSupplierPulldown() {

        return prfApUseSupplierPulldown;
    }

    public void setPrfApUseSupplierPulldown(byte PRF_AP_USE_SPPLR_PLLDWN) {

        this.prfApUseSupplierPulldown = PRF_AP_USE_SPPLR_PLLDWN;
    }

    public byte getPrfArUseCustomerPulldown() {

        return prfArUseCustomerPulldown;
    }

    public void setPrfArUseCustomerPulldown(byte PRF_AR_USE_CSTMR_PLLDWN) {

        this.prfArUseCustomerPulldown = PRF_AR_USE_CSTMR_PLLDWN;
    }


    public byte getPrfApAutoGenerateSupplierCode() {

        return prfApAutoGenerateSupplierCode;
    }

    public void setPrfApAutoGenerateSupplierCode(byte PRF_AP_AT_GNRT_SPPLR_CODE) {

        this.prfApAutoGenerateSupplierCode = PRF_AP_AT_GNRT_SPPLR_CODE;
    }

    public String getPrfApNextSupplierCode() {

        return prfApNextSupplierCode;
    }

    public void setPrfApNextSupplierCode(String PRF_AP_NXT_SPPLR_CODE) {

        this.prfApNextSupplierCode = PRF_AP_NXT_SPPLR_CODE;
    }

    public byte getPrfApReferenceNumberValidation() {

        return prfApReferenceNumberValidation;
    }

    public void setPrfApReferenceNumberValidation(byte PRF_AP_RFRNC_NMBR_VLDTN) {

        this.prfApReferenceNumberValidation = PRF_AP_RFRNC_NMBR_VLDTN;
    }

    public byte getPrfInvEnablePosIntegration() {

        return prfInvEnablePosIntegration;
    }

    public void setPrfInvEnablePosIntegration(byte PRF_INV_ENBL_POS_INTGRTN) {

        this.prfInvEnablePosIntegration = PRF_INV_ENBL_POS_INTGRTN;
    }

    public byte getPrfInvEnablePosAutoPostUpload() {

        return prfInvEnablePosAutoPostUpload;
    }

    public void setPrfInvEnablePosAutoPostUpload(byte PRF_INV_ENBL_POS_AUTO_POST_UP) {

        this.prfInvEnablePosAutoPostUpload = PRF_INV_ENBL_POS_AUTO_POST_UP;
    }

    public Integer getPrfInvPosAdjustmentAccount() {

        return prfInvPosAdjustmentAccount;
    }

    public void setPrfInvPosAdjustmentAccount(Integer PRF_INV_POS_ADJSTMNT_ACCNT) {

        this.prfInvPosAdjustmentAccount = PRF_INV_POS_ADJSTMNT_ACCNT;
    }

    public Integer getPrfInvGeneralAdjustmentAccount() {

        return prfInvGeneralAdjustmentAccount;
    }

    public void setPrfInvGeneralAdjustmentAccount(Integer PRF_INV_GNRL_ADJSTMNT_ACCNT) {

        this.prfInvGeneralAdjustmentAccount = PRF_INV_GNRL_ADJSTMNT_ACCNT;
    }

    public Integer getPrfInvIssuanceAdjustmentAccount() {

        return prfInvIssuanceAdjustmentAccount;
    }

    public void setPrfInvIssuanceAdjustmentAccount(Integer PRF_INV_ISSNC_ADJSTMNT_ACCNT) {

        this.prfInvIssuanceAdjustmentAccount = PRF_INV_ISSNC_ADJSTMNT_ACCNT;
    }

    public Integer getPrfInvProductionAdjustmentAccount() {

        return prfInvProductionAdjustmentAccount;
    }

    public void setPrfInvProductionAdjustmentAccount(Integer PRF_INV_PRDCTN_ADJSTMNT_ACCNT) {

        this.prfInvProductionAdjustmentAccount = PRF_INV_PRDCTN_ADJSTMNT_ACCNT;
    }

    public Integer getPrfInvWastageAdjustmentAccount() {

        return prfInvWastageAdjustmentAccount;
    }

    public void setPrfInvWastageAdjustmentAccount(Integer PRF_INV_WSTG_ADJSTMNT_ACCNT) {

        this.prfInvWastageAdjustmentAccount = PRF_INV_WSTG_ADJSTMNT_ACCNT;
    }

    public String getPrfInvCentralWarehouse() {

        return prfInvCentralWarehouse;
    }

    public void setPrfInvCentralWarehouse(String PRF_INV_CNTRL_WRHS) {

        this.prfInvCentralWarehouse = PRF_INV_CNTRL_WRHS;
    }

    public String getPrfApCheckVoucherDataSource() {

        return prfApCheckVoucherDataSource;
    }

    public void setPrfApCheckVoucherDataSource(String PRF_AP_CHK_VCHR_DT_SRC) {

        this.prfApCheckVoucherDataSource = PRF_AP_CHK_VCHR_DT_SRC;
    }

    public Integer getPrfMiscPosDiscountAccount() {

        return prfMiscPosDiscountAccount;
    }

    public void setPrfMiscPosDiscountAccount(Integer PRF_MISC_POS_DSCNT_ACCNT) {

        this.prfMiscPosDiscountAccount = PRF_MISC_POS_DSCNT_ACCNT;
    }

    public Integer getPrfMiscPosGiftCertificateAccount() {

        return prfMiscPosGiftCertificateAccount;
    }

    public void setPrfMiscPosGiftCertificateAccount(Integer PRF_MISC_POS_GFT_CRTFCT_ACCNT) {

        this.prfMiscPosGiftCertificateAccount = PRF_MISC_POS_GFT_CRTFCT_ACCNT;
    }

    public Integer getPrfMiscPosServiceChargeAccount() {

        return prfMiscPosServiceChargeAccount;
    }

    public void setPrfMiscPosServiceChargeAccount(Integer PRF_MISC_POS_SRVC_CHRG_ACCNT) {

        this.prfMiscPosServiceChargeAccount = PRF_MISC_POS_SRVC_CHRG_ACCNT;
    }

    public Integer getPrfMiscPosDineInChargeAccount() {

        return prfMiscPosDineInChargeAccount;
    }

    public void setPrfMiscPosDineInChargeAccount(Integer PRF_MISC_POS_DN_IN_CHRG_ACCNT) {

        this.prfMiscPosDineInChargeAccount = PRF_MISC_POS_DN_IN_CHRG_ACCNT;
    }

    public String getPrfApDefaultPrTax() {

        return prfApDefaultPrTax;
    }

    public void setPrfApDefaultPrTax(String PRF_AP_DFLT_PR_TX) {

        this.prfApDefaultPrTax = PRF_AP_DFLT_PR_TX;
    }

    public String getPrfApDefaultPrCurrency() {

        return prfApDefaultPrCurrency;
    }

    public void setPrfApDefaultPrCurrency(String PRF_AP_DFLT_PR_CRRNCY) {

        this.prfApDefaultPrCurrency = PRF_AP_DFLT_PR_CRRNCY;
    }

    public Integer getPrfArGlCoaCustomerDepositAccount() {

        return prfArGlCoaCustomerDepositAccount;
    }

    public void setPrfArGlCoaCustomerDepositAccount(Integer PRF_AR_GL_COA_CSTMR_DPST_ACCNT) {

        this.prfArGlCoaCustomerDepositAccount = PRF_AR_GL_COA_CSTMR_DPST_ACCNT;
    }

    public String getPrfArSalesInvoiceDataSource() {

        return prfArSalesInvoiceDataSource;
    }

    public void setPrfArSalesInvoiceDataSource(String PRF_AR_SLS_INVC_DT_SRC) {

        this.prfArSalesInvoiceDataSource = PRF_AR_SLS_INVC_DT_SRC;
    }

    public String getPrfInvNextCustodianNumber1() {

        return prfInvNextCustodianNumber1;
    }

    public void setPrfInvNextCustodianNumber1(String PRF_INV_NXT_CSTDN_NMBR1) {

        this.prfInvNextCustodianNumber1 = PRF_INV_NXT_CSTDN_NMBR1;
    }

    public String getPrfInvNextCustodianNumber2() {

        return prfInvNextCustodianNumber2;
    }

    public void setPrfInvNextCustodianNumber2(String PRF_INV_NXT_CSTDN_NMBR2) {

        this.prfInvNextCustodianNumber2 = PRF_INV_NXT_CSTDN_NMBR2;
    }

    public Integer getPrfInvGlCoaVarianceAccount() {

        return prfInvGlCoaVarianceAccount;
    }

    public void setPrfInvGlCoaVarianceAccount(Integer PRF_INV_GL_COA_VRNC_ACCNT) {

        this.prfInvGlCoaVarianceAccount = PRF_INV_GL_COA_VRNC_ACCNT;
    }

    public byte getPrfArAutoComputeCogs() {

        return prfArAutoComputeCogs;
    }

    public void setPrfArAutoComputeCogs(byte PRF_AR_AUTO_CMPUTE_COGS) {

        this.prfArAutoComputeCogs = PRF_AR_AUTO_CMPUTE_COGS;
    }

    public double getPrfArMonthlyInterestRate() {

        return prfArMonthlyInterestRate;
    }

    public void setPrfArMonthlyInterestRate(double PRF_AR_MNTH_INT_RT) {

        this.prfArMonthlyInterestRate = PRF_AR_MNTH_INT_RT;
    }

    public Integer getPrfApAgingBucket() {

        return prfApAgingBucket;
    }

    public void setPrfApAgingBucket(Integer PRF_AP_AGNG_BCKT) {

        this.prfApAgingBucket = PRF_AP_AGNG_BCKT;
    }

    public Integer getPrfArAgingBucket() {

        return prfArAgingBucket;
    }

    public void setPrfArAgingBucket(Integer PRF_AR_AGNG_BCKT) {

        this.prfArAgingBucket = PRF_AR_AGNG_BCKT;
    }

    public byte getPrfArAllowPriorDate() {

        return prfArAllowPriorDate;
    }

    public void setPrfArAllowPriorDate(byte PRF_AR_ALLW_PRR_DT) {

        this.prfArAllowPriorDate = PRF_AR_ALLW_PRR_DT;
    }

    public byte getPrfArCheckInsufficientStock() {

        return prfArCheckInsufficientStock;
    }

    public void setPrfArCheckInsufficientStock(byte PRF_AR_CHK_INSFFCNT_STCK) {

        this.prfArCheckInsufficientStock = PRF_AR_CHK_INSFFCNT_STCK;
    }

    public byte getPrfArDetailedReceivable() {

        return prfArDetailedReceivable;
    }

    public void setPrfArDetailedReceivable(byte PRF_AR_DTLD_RCVBL) {

        this.prfArDetailedReceivable = PRF_AR_DTLD_RCVBL;
    }

    public byte getPrfApShowPrCost() {

        return prfApShowPrCost;
    }

    public void setPrfApShowPrCost(byte PRF_AP_SHW_PR_CST) {

        this.prfApShowPrCost = PRF_AP_SHW_PR_CST;
    }

    public byte getPrfArEnablePaymentTerm() {

        return prfArEnablePaymentTerm;
    }

    public void setPrfArEnablePaymentTerm(byte PRF_AR_ENBL_PYMNT_TRM) {

        this.prfArEnablePaymentTerm = PRF_AR_ENBL_PYMNT_TRM;
    }

    public byte getPrfArDisableSalesPrice() {

        return prfArDisableSalesPrice;
    }

    public void setPrfArDisableSalesPrice(byte PRF_AR_DSBL_SLS_PRC) {

        this.prfArDisableSalesPrice = PRF_AR_DSBL_SLS_PRC;
    }

    public byte getPrfArValidateCustomerEmail() {

        return prfArValidateCustomerEmail;
    }

    public void setPrfArValidateCustomerEmail(byte PRF_AR_VLDT_CST_EMAIL) {

        this.prfArValidateCustomerEmail = PRF_AR_VLDT_CST_EMAIL;
    }

    public byte getPrfInvItemLocationShowAll() {

        return prfInvItemLocationShowAll;
    }

    public void setPrfInvItemLocationShowAll(byte PRF_INV_IL_SHW_ALL) {

        this.prfInvItemLocationShowAll = PRF_INV_IL_SHW_ALL;
    }

    public byte getPrfInvItemLocationAddByItemList() {

        return prfInvItemLocationAddByItemList;
    }

    public void setPrfInvItemLocationAddByItemList(byte PRF_INV_IL_ADD_BY_ITM_LST) {

        this.prfInvItemLocationAddByItemList = PRF_INV_IL_ADD_BY_ITM_LST;
    }

    public byte getPrfApDebitMemoOverrideCost() {

        return prfApDebitMemoOverrideCost;
    }

    public void setPrfApDebitMemoOverrideCost(byte PRF_AP_DM_OVRRD_CST) {

        this.prfApDebitMemoOverrideCost = PRF_AP_DM_OVRRD_CST;
    }

    public byte getPrfGlYearEndCloseRestriction() {

        return prfGlYearEndCloseRestriction;
    }

    public void setPrfGlYearEndCloseRestriction(byte PRF_GL_YR_END_CLS_RSTRCTN) {

        this.prfGlYearEndCloseRestriction = PRF_GL_YR_END_CLS_RSTRCTN;
    }

    public byte getPrfAdDisableMultipleLogin() {

        return prfAdDisableMultipleLogin;
    }

    public void setPrfAdDisableMultipleLogin(byte PRF_AD_DSBL_MTPL_LGN) {

        this.prfAdDisableMultipleLogin = PRF_AD_DSBL_MTPL_LGN;
    }

    public byte getPrfAdEnableEmailNotification() {

        return prfAdEnableEmailNotification;
    }

    public void setPrfAdEnableEmailNotification(byte PRF_AD_ENBL_EML_NTFCTN) {

        this.prfAdEnableEmailNotification = PRF_AD_ENBL_EML_NTFCTN;
    }

    public byte getPrfArSoSalespersonRequired() {

        return prfArSoSalespersonRequired;
    }

    public void setPrfArSoSalespersonRequired(byte PRF_AR_SO_SLSPRSN_RQRD) {

        this.prfArSoSalespersonRequired = PRF_AR_SO_SLSPRSN_RQRD;
    }

    public byte getPrfArInvcSalespersonRequired() {

        return prfArInvcSalespersonRequired;
    }

    public void setPrfArInvcSalespersonRequired(byte PRF_AR_INVC_SLSPRSN_RQRD) {

        this.prfArInvcSalespersonRequired = PRF_AR_INVC_SLSPRSN_RQRD;
    }

    public String getPrfMailHost() {

        return prfMailHost;
    }

    public void setPrfMailHost(String PRF_ML_HST) {

        this.prfMailHost = PRF_ML_HST;
    }

    public String getPrfMailSocketFactoryPort() {

        return prfMailSocketFactoryPort;
    }

    public void setPrfMailSocketFactoryPort(String PRF_ML_SCKT_FCTRY_PRT) {

        this.prfMailSocketFactoryPort = PRF_ML_SCKT_FCTRY_PRT;
    }

    public String getPrfMailPort() {

        return prfMailPort;
    }

    public void setPrfMailPort(String PRF_ML_PRT) {

        this.prfMailPort = PRF_ML_PRT;
    }

    public String getPrfMailFrom() {

        return prfMailFrom;
    }

    public void setPrfMailFrom(String PRF_ML_FRM) {

        this.prfMailFrom = PRF_ML_FRM;
    }

    public byte getPrfMailAuthenticator() {

        return prfMailAuthenticator;
    }

    public void setPrfMailAuthenticator(byte PRF_ML_AUTH) {

        this.prfMailAuthenticator = PRF_ML_AUTH;
    }

    public String getPrfMailPassword() {

        return prfMailPassword;
    }

    public void setPrfMailPassword(String PRF_ML_PSSWRD) {

        this.prfMailPassword = PRF_ML_PSSWRD;
    }

    public String getPrfMailTo() {

        return prfMailTo;
    }

    public void setPrfMailTo(String PRF_ML_TO) {

        this.prfMailTo = PRF_ML_TO;
    }

    public String getPrfMailCc() {

        return prfMailCc;
    }

    public void setPrfMailCc(String PRF_ML_CC) {

        this.prfMailCc = PRF_ML_CC;
    }

    public String getPrfMailBcc() {

        return prfMailBcc;
    }

    public void setPrfMailBcc(String PRF_ML_BCC) {

        this.prfMailBcc = PRF_ML_BCC;
    }

    public String getPrfMailConfig() {

        return prfMailConfig;
    }

    public void setPrfMailConfig(String PRF_ML_CNFG) {

        this.prfMailConfig = PRF_ML_CNFG;
    }

    public String getPrfUserRestWebService() {

        return prfUserRestWebService;
    }

    public void setPrfUserRestWebService(String PRF_USR_RST_WB_SRVC) {

        this.prfUserRestWebService = PRF_USR_RST_WB_SRVC;
    }

    public String getPrfPassRestWebService() {

        return prfPassRestWebService;
    }

    public void setPrfPassRestWebService(String PRF_PSS_RST_WB_SRVC) {

        this.prfPassRestWebService = PRF_PSS_RST_WB_SRVC;
    }

    public String getPrfAttachmentPath() {

        return prfAttachmentPath;
    }

    public void setPrfAttachmentPath(String PRF_ATTCHMNT_PTH) {

        this.prfAttachmentPath = PRF_ATTCHMNT_PTH;
    }

    public Integer getPrfAdCompany() {

        return prfAdCompany;
    }

    public void setPrfAdCompany(Integer PRF_AD_CMPNY) {

        this.prfAdCompany = PRF_AD_CMPNY;
    }

    public LocalArWithholdingTaxCode getArWithholdingTaxCode() {

        return arWithholdingTaxCode;
    }

    public void setArWithholdingTaxCode(LocalArWithholdingTaxCode arWithholdingTaxCode) {

        this.arWithholdingTaxCode = arWithholdingTaxCode;
    }

}