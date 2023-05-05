package com.ejb.entities.ap;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "ApCheck")
@Table(name = "AP_CHCK")
public class LocalApCheck extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHK_CODE", nullable = false)
    private Integer chkCode;

    @Column(name = "CHK_TYP", columnDefinition = "VARCHAR")
    private String chkType;

    @Column(name = "CHK_DESC", columnDefinition = "VARCHAR")
    private String chkDescription;

    @Column(name = "CHK_DT", columnDefinition = "DATETIME")
    private Date chkDate;

    @Column(name = "CHK_CHCK_DT", columnDefinition = "DATETIME")
    private Date chkCheckDate;

    @Column(name = "CHK_NMBR", columnDefinition = "VARCHAR")
    private String chkNumber;

    @Column(name = "CHK_DCMNT_NMBR", columnDefinition = "VARCHAR")
    private String chkDocumentNumber;

    @Column(name = "CHK_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String chkReferenceNumber;

    @Column(name = "CHK_INFO_TYP", columnDefinition = "VARCHAR")
    private String chkInfoType;

    @Column(name = "CHK_INFO_BIO_NMBR", columnDefinition = "VARCHAR")
    private String chkInfoBioNumber;

    @Column(name = "CHK_INFO_BIO_DESC", columnDefinition = "VARCHAR")
    private String chkInfoBioDescription;

    @Column(name = "CHK_INFO_TYP_STATUS", columnDefinition = "VARCHAR")
    private String chkInfoTypeStatus;

    @Column(name = "CHK_INFO_RQST_STATUS", columnDefinition = "VARCHAR")
    private String chkInfoRequestStatus;

    @Column(name = "CHK_INVT_IS", columnDefinition = "TINYINT")
    private byte chkInvtInscribedStock;

    @Column(name = "CHK_INVT_TB", columnDefinition = "TINYINT")
    private byte chkInvtTreasuryBill;

    @Column(name = "CHK_INVT_NXT_RN_DT", columnDefinition = "DATETIME")
    private Date chkInvtNextRunDate;

    @Column(name = "CHK_INVT_STTLMNT_DT", columnDefinition = "DATETIME")
    private Date chkInvtSettlementDate;

    @Column(name = "CHK_INVT_MTRTY_DT", columnDefinition = "DATETIME")
    private Date chkInvtMaturityDate;

    @Column(name = "CHK_INVT_BD_YLD", columnDefinition = "DOUBLE")
    private double chkInvtBidYield = 0;

    @Column(name = "CHK_INVT_CPN_RT", columnDefinition = "DOUBLE")
    private double chkInvtCouponRate = 0;

    @Column(name = "CHK_INVT_STTLMNT_AMNT", columnDefinition = "DOUBLE")
    private double chkInvtSettleAmount = 0;

    @Column(name = "CHK_INVT_FC_VL", columnDefinition = "DOUBLE")
    private double chkInvtFaceValue = 0;

    @Column(name = "CHK_INVT_PM_AMNT", columnDefinition = "DOUBLE")
    private double chkInvtPremiumAmount = 0;

    @Column(name = "CHK_LN", columnDefinition = "TINYINT")
    private byte chkLoan;

    @Column(name = "CHK_LN_GNRTD", columnDefinition = "TINYINT")
    private byte chkLoanGenerated;

    @Column(name = "CHK_CNVRSN_DT", columnDefinition = "DATETIME")
    private Date chkConversionDate;

    @Column(name = "CHK_CNVRSN_RT", columnDefinition = "DOUBLE")
    private double chkConversionRate = 0;

    @Column(name = "CHK_BLL_AMNT", columnDefinition = "DOUBLE")
    private double chkBillAmount = 0;

    @Column(name = "CHK_AMNT", columnDefinition = "DOUBLE")
    private double chkAmount = 0;

    @Column(name = "CHK_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String chkApprovalStatus;

    @Column(name = "CHK_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String chkReasonForRejection;

    @Column(name = "CHK_PSTD", columnDefinition = "TINYINT")
    private byte chkPosted;

    @Column(name = "CHK_VD", columnDefinition = "TINYINT")
    private byte chkVoid;

    @Column(name = "CHK_CRSS_CHCK", columnDefinition = "TINYINT")
    private byte chkCrossCheck;

    @Column(name = "CHK_VD_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String chkVoidApprovalStatus;

    @Column(name = "CHK_VD_PSTD", columnDefinition = "TINYINT")
    private byte chkVoidPosted;

    @Column(name = "CHK_CRTD_BY", columnDefinition = "VARCHAR")
    private String chkCreatedBy;

    @Column(name = "CHK_DT_CRTD", columnDefinition = "DATETIME")
    private Date chkDateCreated;

    @Column(name = "CHK_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String chkLastModifiedBy;

    @Column(name = "CHK_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date chkDateLastModified;

    @Column(name = "CHK_APPRVD_RJCTD_BY", columnDefinition = "VARCHAR")
    private String chkApprovedRejectedBy;

    @Column(name = "CHK_DT_APPRVD_RJCTD", columnDefinition = "DATETIME")
    private Date chkDateApprovedRejected;

    @Column(name = "CHK_PSTD_BY", columnDefinition = "VARCHAR")
    private String chkPostedBy;

    @Column(name = "CHK_DT_PSTD", columnDefinition = "DATETIME")
    private Date chkDatePosted;

    @Column(name = "CHK_RCNCLD", columnDefinition = "TINYINT")
    private byte chkReconciled;

    @Column(name = "CHK_DT_RCNCLD", columnDefinition = "DATETIME")
    private Date chkDateReconciled;

    @Column(name = "CHK_RLSD", columnDefinition = "TINYINT")
    private byte chkReleased;

    @Column(name = "CHK_DT_RLSD", columnDefinition = "DATETIME")
    private Date chkDateReleased;

    @Column(name = "CHK_PRNTD", columnDefinition = "TINYINT")
    private byte chkPrinted;

    @Column(name = "CHK_CV_PRNTD", columnDefinition = "TINYINT")
    private byte chkCvPrinted;

    @Column(name = "CHK_MMO", columnDefinition = "VARCHAR")
    private String chkMemo;

    @Column(name = "CHK_SPL_NM", columnDefinition = "VARCHAR")
    private String chkSupplierName;

    @Column(name = "CHK_AD_BRNCH", columnDefinition = "INT")
    private Integer chkAdBranch;

    @Column(name = "CHK_AD_CMPNY", columnDefinition = "INT")
    private Integer chkAdCompany;

    @JoinColumn(name = "AD_BANK_ACCOUNT", referencedColumnName = "BA_CODE")
    @ManyToOne
    private LocalAdBankAccount adBankAccount;

    @JoinColumn(name = "AD_PAYMENT_TERM", referencedColumnName = "PYT_CODE")
    @ManyToOne
    private LocalAdPaymentTerm adPaymentTerm;

    @JoinColumn(name = "AP_CHECK_BATCH", referencedColumnName = "AP_CB_CODE")
    @ManyToOne
    private LocalApCheckBatch apCheckBatch;

    @JoinColumn(name = "AP_SUPPLIER", referencedColumnName = "SPL_CODE")
    @ManyToOne
    private LocalApSupplier apSupplier;

    @JoinColumn(name = "AP_TAX_CODE", referencedColumnName = "AP_TC_CODE")
    @ManyToOne
    private LocalApTaxCode apTaxCode;

    @JoinColumn(name = "AP_WITHHOLDING_TAX_CODE", referencedColumnName = "AP_WTC_CODE")
    @ManyToOne
    private LocalApWithholdingTaxCode apWithholdingTaxCode;

    @JoinColumn(name = "GL_FUNCTIONAL_CURRENCY", referencedColumnName = "FC_CODE")
    @ManyToOne
    private LocalGlFunctionalCurrency glFunctionalCurrency;

    @OneToMany(mappedBy = "apCheck", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalApAppliedVoucher> apAppliedVouchers;

    @OneToMany(mappedBy = "apCheck", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalApDistributionRecord> apDistributionRecords;

    @OneToMany(mappedBy = "apCheck", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalApVoucherLineItem> apVoucherLineItems;

    public Integer getChkCode() {

        return chkCode;
    }

    public void setChkCode(Integer CHK_CODE) {

        this.chkCode = CHK_CODE;
    }

    public String getChkType() {

        return chkType;
    }

    public void setChkType(String CHK_TYP) {

        this.chkType = CHK_TYP;
    }

    public String getChkDescription() {

        return chkDescription;
    }

    public void setChkDescription(String CHK_DESC) {

        this.chkDescription = CHK_DESC;
    }

    public Date getChkDate() {

        return chkDate;
    }

    public void setChkDate(Date CHK_DT) {

        this.chkDate = CHK_DT;
    }

    public Date getChkCheckDate() {

        return chkCheckDate;
    }

    public void setChkCheckDate(Date CHK_CHCK_DT) {

        this.chkCheckDate = CHK_CHCK_DT;
    }

    public String getChkNumber() {

        return chkNumber;
    }

    public void setChkNumber(String CHK_NMBR) {

        this.chkNumber = CHK_NMBR;
    }

    public String getChkDocumentNumber() {

        return chkDocumentNumber;
    }

    public void setChkDocumentNumber(String CHK_DCMNT_NMBR) {

        this.chkDocumentNumber = CHK_DCMNT_NMBR;
    }

    public String getChkReferenceNumber() {

        return chkReferenceNumber;
    }

    public void setChkReferenceNumber(String CHK_RFRNC_NMBR) {

        this.chkReferenceNumber = CHK_RFRNC_NMBR;
    }

    public String getChkInfoType() {

        return chkInfoType;
    }

    public void setChkInfoType(String CHK_INFO_TYP) {

        this.chkInfoType = CHK_INFO_TYP;
    }

    public String getChkInfoBioNumber() {

        return chkInfoBioNumber;
    }

    public void setChkInfoBioNumber(String CHK_INFO_BIO_NMBR) {

        this.chkInfoBioNumber = CHK_INFO_BIO_NMBR;
    }

    public String getChkInfoBioDescription() {

        return chkInfoBioDescription;
    }

    public void setChkInfoBioDescription(String CHK_INFO_BIO_DESC) {

        this.chkInfoBioDescription = CHK_INFO_BIO_DESC;
    }

    public String getChkInfoTypeStatus() {

        return chkInfoTypeStatus;
    }

    public void setChkInfoTypeStatus(String CHK_INFO_TYP_STATUS) {

        this.chkInfoTypeStatus = CHK_INFO_TYP_STATUS;
    }

    public String getChkInfoRequestStatus() {

        return chkInfoRequestStatus;
    }

    public void setChkInfoRequestStatus(String CHK_INFO_RQST_STATUS) {

        this.chkInfoRequestStatus = CHK_INFO_RQST_STATUS;
    }

    public byte getChkInvtInscribedStock() {

        return chkInvtInscribedStock;
    }

    public void setChkInvtInscribedStock(byte CHK_INVT_IS) {

        this.chkInvtInscribedStock = CHK_INVT_IS;
    }

    public byte getChkInvtTreasuryBill() {

        return chkInvtTreasuryBill;
    }

    public void setChkInvtTreasuryBill(byte CHK_INVT_TB) {

        this.chkInvtTreasuryBill = CHK_INVT_TB;
    }

    public Date getChkInvtNextRunDate() {

        return chkInvtNextRunDate;
    }

    public void setChkInvtNextRunDate(Date CHK_INVT_NXT_RN_DT) {

        this.chkInvtNextRunDate = CHK_INVT_NXT_RN_DT;
    }

    public Date getChkInvtSettlementDate() {

        return chkInvtSettlementDate;
    }

    public void setChkInvtSettlementDate(Date CHK_INVT_STTLMNT_DT) {

        this.chkInvtSettlementDate = CHK_INVT_STTLMNT_DT;
    }

    public Date getChkInvtMaturityDate() {

        return chkInvtMaturityDate;
    }

    public void setChkInvtMaturityDate(Date CHK_INVT_MTRTY_DT) {

        this.chkInvtMaturityDate = CHK_INVT_MTRTY_DT;
    }

    public double getChkInvtBidYield() {

        return chkInvtBidYield;
    }

    public void setChkInvtBidYield(double CHK_INVT_BD_YLD) {

        this.chkInvtBidYield = CHK_INVT_BD_YLD;
    }

    public double getChkInvtCouponRate() {

        return chkInvtCouponRate;
    }

    public void setChkInvtCouponRate(double CHK_INVT_CPN_RT) {

        this.chkInvtCouponRate = CHK_INVT_CPN_RT;
    }

    public double getChkInvtSettleAmount() {

        return chkInvtSettleAmount;
    }

    public void setChkInvtSettleAmount(double CHK_INVT_STTLMNT_AMNT) {

        this.chkInvtSettleAmount = CHK_INVT_STTLMNT_AMNT;
    }

    public double getChkInvtFaceValue() {

        return chkInvtFaceValue;
    }

    public void setChkInvtFaceValue(double CHK_INVT_FC_VL) {

        this.chkInvtFaceValue = CHK_INVT_FC_VL;
    }

    public double getChkInvtPremiumAmount() {

        return chkInvtPremiumAmount;
    }

    public void setChkInvtPremiumAmount(double CHK_INVT_PM_AMNT) {

        this.chkInvtPremiumAmount = CHK_INVT_PM_AMNT;
    }

    public byte getChkLoan() {

        return chkLoan;
    }

    public void setChkLoan(byte CHK_LN) {

        this.chkLoan = CHK_LN;
    }

    public byte getChkLoanGenerated() {

        return chkLoanGenerated;
    }

    public void setChkLoanGenerated(byte CHK_LN_GNRTD) {

        this.chkLoanGenerated = CHK_LN_GNRTD;
    }

    public Date getChkConversionDate() {

        return chkConversionDate;
    }

    public void setChkConversionDate(Date CHK_CNVRSN_DT) {

        this.chkConversionDate = CHK_CNVRSN_DT;
    }

    public double getChkConversionRate() {

        return chkConversionRate;
    }

    public void setChkConversionRate(double CHK_CNVRSN_RT) {

        this.chkConversionRate = CHK_CNVRSN_RT;
    }

    public double getChkBillAmount() {

        return chkBillAmount;
    }

    public void setChkBillAmount(double CHK_BLL_AMNT) {

        this.chkBillAmount = CHK_BLL_AMNT;
    }

    public double getChkAmount() {

        return chkAmount;
    }

    public void setChkAmount(double CHK_AMNT) {

        this.chkAmount = CHK_AMNT;
    }

    public String getChkApprovalStatus() {

        return chkApprovalStatus;
    }

    public void setChkApprovalStatus(String CHK_APPRVL_STATUS) {

        this.chkApprovalStatus = CHK_APPRVL_STATUS;
    }

    public String getChkReasonForRejection() {

        return chkReasonForRejection;
    }

    public void setChkReasonForRejection(String CHK_RSN_FR_RJCTN) {

        this.chkReasonForRejection = CHK_RSN_FR_RJCTN;
    }

    public byte getChkPosted() {

        return chkPosted;
    }

    public void setChkPosted(byte CHK_PSTD) {

        this.chkPosted = CHK_PSTD;
    }

    public byte getChkVoid() {

        return chkVoid;
    }

    public void setChkVoid(byte CHK_VD) {

        this.chkVoid = CHK_VD;
    }

    public byte getChkCrossCheck() {

        return chkCrossCheck;
    }

    public void setChkCrossCheck(byte CHK_CRSS_CHCK) {

        this.chkCrossCheck = CHK_CRSS_CHCK;
    }

    public String getChkVoidApprovalStatus() {

        return chkVoidApprovalStatus;
    }

    public void setChkVoidApprovalStatus(String CHK_VD_APPRVL_STATUS) {

        this.chkVoidApprovalStatus = CHK_VD_APPRVL_STATUS;
    }

    public byte getChkVoidPosted() {

        return chkVoidPosted;
    }

    public void setChkVoidPosted(byte CHK_VD_PSTD) {

        this.chkVoidPosted = CHK_VD_PSTD;
    }

    public String getChkCreatedBy() {

        return chkCreatedBy;
    }

    public void setChkCreatedBy(String CHK_CRTD_BY) {

        this.chkCreatedBy = CHK_CRTD_BY;
    }

    public Date getChkDateCreated() {

        return chkDateCreated;
    }

    public void setChkDateCreated(Date CHK_DT_CRTD) {

        this.chkDateCreated = CHK_DT_CRTD;
    }

    public String getChkLastModifiedBy() {

        return chkLastModifiedBy;
    }

    public void setChkLastModifiedBy(String CHK_LST_MDFD_BY) {

        this.chkLastModifiedBy = CHK_LST_MDFD_BY;
    }

    public Date getChkDateLastModified() {

        return chkDateLastModified;
    }

    public void setChkDateLastModified(Date CHK_DT_LST_MDFD) {

        this.chkDateLastModified = CHK_DT_LST_MDFD;
    }

    public String getChkApprovedRejectedBy() {

        return chkApprovedRejectedBy;
    }

    public void setChkApprovedRejectedBy(String CHK_APPRVD_RJCTD_BY) {

        this.chkApprovedRejectedBy = CHK_APPRVD_RJCTD_BY;
    }

    public Date getChkDateApprovedRejected() {

        return chkDateApprovedRejected;
    }

    public void setChkDateApprovedRejected(Date CHK_DT_APPRVD_RJCTD) {

        this.chkDateApprovedRejected = CHK_DT_APPRVD_RJCTD;
    }

    public String getChkPostedBy() {

        return chkPostedBy;
    }

    public void setChkPostedBy(String CHK_PSTD_BY) {

        this.chkPostedBy = CHK_PSTD_BY;
    }

    public Date getChkDatePosted() {

        return chkDatePosted;
    }

    public void setChkDatePosted(Date CHK_DT_PSTD) {

        this.chkDatePosted = CHK_DT_PSTD;
    }

    public byte getChkReconciled() {

        return chkReconciled;
    }

    public void setChkReconciled(byte CHK_RCNCLD) {

        this.chkReconciled = CHK_RCNCLD;
    }

    public Date getChkDateReconciled() {

        return chkDateReconciled;
    }

    public void setChkDateReconciled(Date CHK_DT_RCNCLD) {

        this.chkDateReconciled = CHK_DT_RCNCLD;
    }

    public byte getChkReleased() {

        return chkReleased;
    }

    public void setChkReleased(byte CHK_RLSD) {

        this.chkReleased = CHK_RLSD;
    }

    public Date getChkDateReleased() {

        return chkDateReleased;
    }

    public void setChkDateReleased(Date CHK_DT_RLSD) {

        this.chkDateReleased = CHK_DT_RLSD;
    }

    public byte getChkPrinted() {

        return chkPrinted;
    }

    public void setChkPrinted(byte CHK_PRNTD) {

        this.chkPrinted = CHK_PRNTD;
    }

    public byte getChkCvPrinted() {

        return chkCvPrinted;
    }

    public void setChkCvPrinted(byte CHK_CV_PRNTD) {

        this.chkCvPrinted = CHK_CV_PRNTD;
    }

    public String getChkMemo() {

        return chkMemo;
    }

    public void setChkMemo(String CHK_MMO) {

        this.chkMemo = CHK_MMO;
    }

    public String getChkSupplierName() {

        return chkSupplierName;
    }

    public void setChkSupplierName(String CHK_SPL_NM) {

        this.chkSupplierName = CHK_SPL_NM;
    }

    public Integer getChkAdBranch() {

        return chkAdBranch;
    }

    public void setChkAdBranch(Integer CHK_AD_BRNCH) {

        this.chkAdBranch = CHK_AD_BRNCH;
    }

    public Integer getChkAdCompany() {

        return chkAdCompany;
    }

    public void setChkAdCompany(Integer CHK_AD_CMPNY) {

        this.chkAdCompany = CHK_AD_CMPNY;
    }

    public LocalAdBankAccount getAdBankAccount() {

        return adBankAccount;
    }

    public void setAdBankAccount(LocalAdBankAccount adBankAccount) {

        this.adBankAccount = adBankAccount;
    }

    public LocalAdPaymentTerm getAdPaymentTerm() {

        return adPaymentTerm;
    }

    public void setAdPaymentTerm(LocalAdPaymentTerm adPaymentTerm) {

        this.adPaymentTerm = adPaymentTerm;
    }

    public LocalApCheckBatch getApCheckBatch() {

        return apCheckBatch;
    }

    public void setApCheckBatch(LocalApCheckBatch apCheckBatch) {

        this.apCheckBatch = apCheckBatch;
    }

    public LocalApSupplier getApSupplier() {

        return apSupplier;
    }

    public void setApSupplier(LocalApSupplier apSupplier) {

        this.apSupplier = apSupplier;
    }

    public LocalApTaxCode getApTaxCode() {

        return apTaxCode;
    }

    public void setApTaxCode(LocalApTaxCode apTaxCode) {

        this.apTaxCode = apTaxCode;
    }

    public LocalApWithholdingTaxCode getApWithholdingTaxCode() {

        return apWithholdingTaxCode;
    }

    public void setApWithholdingTaxCode(LocalApWithholdingTaxCode apWithholdingTaxCode) {

        this.apWithholdingTaxCode = apWithholdingTaxCode;
    }

    public LocalGlFunctionalCurrency getGlFunctionalCurrency() {

        return glFunctionalCurrency;
    }

    public void setGlFunctionalCurrency(LocalGlFunctionalCurrency glFunctionalCurrency) {

        this.glFunctionalCurrency = glFunctionalCurrency;
    }

    @XmlTransient
    public List getApAppliedVouchers() {

        return apAppliedVouchers;
    }

    public void setApAppliedVouchers(List apAppliedVouchers) {

        this.apAppliedVouchers = apAppliedVouchers;
    }

    @XmlTransient
    public List getApDistributionRecords() {

        return apDistributionRecords;
    }

    public void setApDistributionRecords(List apDistributionRecords) {

        this.apDistributionRecords = apDistributionRecords;
    }

    @XmlTransient
    public List getApVoucherLineItems() {

        return apVoucherLineItems;
    }

    public void setApVoucherLineItems(List apVoucherLineItems) {

        this.apVoucherLineItems = apVoucherLineItems;
    }

    public void addApAppliedVoucher(LocalApAppliedVoucher entity) {

        try {
            entity.setApCheck(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApAppliedVoucher(LocalApAppliedVoucher entity) {

        try {
            entity.setApCheck(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApDistributionRecord(LocalApDistributionRecord entity) {

        try {
            entity.setApCheck(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApDistributionRecord(LocalApDistributionRecord entity) {

        try {
            entity.setApCheck(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApVoucherLineItem(LocalApVoucherLineItem entity) {

        try {
            entity.setApCheck(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApVoucherLineItem(LocalApVoucherLineItem entity) {

        try {
            entity.setApCheck(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public short getApDrNextLine() {

        try {
            List lists = getApDistributionRecords();
            return (short) (lists.size() + 1);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}