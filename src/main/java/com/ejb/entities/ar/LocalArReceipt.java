package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.cm.LocalCmFundTransferReceipt;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.entities.gl.LocalGlInvestorAccountBalance;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "ArReceipt")
@Table(name = "AR_RCPT")
public class LocalArReceipt extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RCT_CODE", nullable = false)
    private Integer rctCode;

    @Column(name = "RCT_TYP", columnDefinition = "VARCHAR")
    private String rctType;

    @Column(name = "RCT_DCMNT_TYP", columnDefinition = "VARCHAR")
    private String rctDocumentType;

    @Column(name = "RCT_DESC", columnDefinition = "VARCHAR")
    private String rctDescription;

    @Column(name = "RCT_DT", columnDefinition = "DATETIME")
    private Date rctDate;

    @Column(name = "RCT_NMBR", columnDefinition = "VARCHAR")
    private String rctNumber;

    @Column(name = "RCT_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String rctReferenceNumber;

    @Column(name = "RCT_CHCK_NO", columnDefinition = "VARCHAR")
    private String rctCheckNo;

    @Column(name = "RCT_PYFL_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String rctPayfileReferenceNumber;

    @Column(name = "RCT_CHQ_NMBR", columnDefinition = "VARCHAR")
    private String rctChequeNumber;

    @Column(name = "RCT_VCHR_NMBR", columnDefinition = "VARCHAR")
    private String rctVoucherNumber;

    @Column(name = "RCT_CRD_NMBR1", columnDefinition = "VARCHAR")
    private String rctCardNumber1;

    @Column(name = "RCT_CRD_NMBR2", columnDefinition = "VARCHAR")
    private String rctCardNumber2;

    @Column(name = "RCT_CRD_NMBR3", columnDefinition = "VARCHAR")
    private String rctCardNumber3;

    @Column(name = "RCT_AMNT", columnDefinition = "DOUBLE")
    private double rctAmount = 0;

    @Column(name = "RCT_AMNT_CSH", columnDefinition = "DOUBLE")
    private double rctAmountCash = 0;

    @Column(name = "RCT_AMNT_CHQ", columnDefinition = "DOUBLE")
    private double rctAmountCheque = 0;

    @Column(name = "RCT_AMNT_VCHR", columnDefinition = "DOUBLE")
    private double rctAmountVoucher = 0;

    @Column(name = "RCT_AMNT_CRD1", columnDefinition = "DOUBLE")
    private double rctAmountCard1 = 0;

    @Column(name = "RCT_AMNT_CRD2", columnDefinition = "DOUBLE")
    private double rctAmountCard2 = 0;

    @Column(name = "RCT_AMNT_CRD3", columnDefinition = "DOUBLE")
    private double rctAmountCard3 = 0;

    @Column(name = "RCT_CNVRSN_DT", columnDefinition = "DATETIME")
    private Date rctConversionDate;

    @Column(name = "RCT_CNVRSN_RT", columnDefinition = "DOUBLE")
    private double rctConversionRate = 0;

    @Column(name = "RCT_SLD_TO", columnDefinition = "VARCHAR")
    private String rctSoldTo;

    @Column(name = "RCT_PYMNT_MTHD", columnDefinition = "VARCHAR")
    private String rctPaymentMethod;

    @Column(name = "RCT_CSTMR_DPST", columnDefinition = "TINYINT")
    private byte rctCustomerDeposit;

    @Column(name = "RCT_APPLD_DPST", columnDefinition = "DOUBLE")
    private double rctAppliedDeposit = 0;

    @Column(name = "RCT_EXCSS_AMNT", columnDefinition = "DOUBLE")
    private double rctExcessAmount = 0;

    @Column(name = "RCT_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String rctApprovalStatus;

    @Column(name = "RCT_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String rctReasonForRejection;

    @Column(name = "RCT_ENBL_ADVNC_PYMNT", columnDefinition = "TINYINT")
    private byte rctEnableAdvancePayment;

    @Column(name = "RCT_PSTD", columnDefinition = "TINYINT")
    private byte rctPosted;

    @Column(name = "RCT_VD_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String rctVoidApprovalStatus;

    @Column(name = "RCT_VD_PSTD", columnDefinition = "TINYINT")
    private byte rctVoidPosted;

    @Column(name = "RCT_VD", columnDefinition = "TINYINT")
    private byte rctVoid;

    @Column(name = "RCT_RCNCLD", columnDefinition = "TINYINT")
    private byte rctReconciled;

    @Column(name = "RCT_RCNCLD_CHQ", columnDefinition = "TINYINT")
    private byte rctReconciledCheque;

    @Column(name = "RCT_RCNCLD_CRD1", columnDefinition = "TINYINT")
    private byte rctReconciledCard1;

    @Column(name = "RCT_RCNCLD_CRD2", columnDefinition = "TINYINT")
    private byte rctReconciledCard2;

    @Column(name = "RCT_RCNCLD_CRD3", columnDefinition = "TINYINT")
    private byte rctReconciledCard3;

    @Column(name = "RCT_DT_RCNCLD", columnDefinition = "DATETIME")
    private Date rctDateReconciled;

    @Column(name = "RCT_DT_RCNCLD_CHQ", columnDefinition = "DATETIME")
    private Date rctDateReconciledCheque;

    @Column(name = "RCT_DT_RCNCLD_CRD1", columnDefinition = "DATETIME")
    private Date rctDateReconciledCard1;

    @Column(name = "RCT_DT_RCNCLD_CRD2", columnDefinition = "DATETIME")
    private Date rctDateReconciledCard2;

    @Column(name = "RCT_DT_RCNCLD_CRD3", columnDefinition = "DATETIME")
    private Date rctDateReconciledCard3;

    @Column(name = "RCT_CRTD_BY", columnDefinition = "VARCHAR")
    private String rctCreatedBy;

    @Column(name = "RCT_DT_CRTD", columnDefinition = "DATETIME")
    private Date rctDateCreated;

    @Column(name = "RCT_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String rctLastModifiedBy;

    @Column(name = "RCT_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date rctDateLastModified;

    @Column(name = "RCT_APPRVD_RJCTD_BY", columnDefinition = "VARCHAR")
    private String rctApprovedRejectedBy;

    @Column(name = "RCT_DT_APPRVD_RJCTD", columnDefinition = "DATETIME")
    private Date rctDateApprovedRejected;

    @Column(name = "RCT_PSTD_BY", columnDefinition = "VARCHAR")
    private String rctPostedBy;

    @Column(name = "RCT_DT_PSTD", columnDefinition = "DATETIME")
    private Date rctDatePosted;

    @Column(name = "RCT_PRNTD", columnDefinition = "TINYINT")
    private byte rctPrinted;

    @Column(name = "RCT_LV_SHFT", columnDefinition = "VARCHAR")
    private String rctLvShift;

    @Column(name = "RCT_LCK", columnDefinition = "TINYINT")
    private byte rctLock;

    @Column(name = "RCT_SBJCT_TO_CMMSSN", columnDefinition = "TINYINT")
    private byte rctSubjectToCommission;

    @Column(name = "RCT_CST_NM", columnDefinition = "VARCHAR")
    private String rctCustomerName;

    @Column(name = "RCT_CST_ADRSS", columnDefinition = "VARCHAR")
    private String rctCustomerAddress;

    @Column(name = "RCT_INVTR_BGNNG_BLNC", columnDefinition = "TINYINT")
    private byte rctInvtrBeginningBalance;

    @Column(name = "RCT_INVTR_IF", columnDefinition = "TINYINT")
    private byte rctInvtrInvestorFund;

    @Column(name = "RCT_INVTR_NXT_RN_DT", columnDefinition = "DATETIME")
    private Date rctInvtrNextRunDate;

    @Column(name = "REPORT_PARAMETER", columnDefinition = "VARCHAR")
    private String reportParameter;

    @Column(name = "RCT_AD_BRNCH", columnDefinition = "INT")
    private Integer rctAdBranch;

    @Column(name = "RCT_AD_CMPNY", columnDefinition = "INT")
    private Integer rctAdCompany;

    @JoinColumn(name = "AD_BANK_ACCOUNT", referencedColumnName = "BA_CODE")
    @ManyToOne
    private LocalAdBankAccount adBankAccount;

    @JoinColumn(name = "AD_BANK_ACCOUNT_CARD1", referencedColumnName = "BA_CODE")
    @ManyToOne
    private LocalAdBankAccount adBankAccountCard1;

    @JoinColumn(name = "AD_BANK_ACCOUNT_CARD2", referencedColumnName = "BA_CODE")
    @ManyToOne
    private LocalAdBankAccount adBankAccountCard2;

    @JoinColumn(name = "AD_BANK_ACCOUNT_CARD3", referencedColumnName = "BA_CODE")
    @ManyToOne
    private LocalAdBankAccount adBankAccountCard3;

    @JoinColumn(name = "AR_CUSTOMER", referencedColumnName = "AR_CST_CODE")
    @ManyToOne
    private LocalArCustomer arCustomer;

    @JoinColumn(name = "AR_RECEIPT_BATCH", referencedColumnName = "RB_CODE")
    @ManyToOne
    private LocalArReceiptBatch arReceiptBatch;

    @JoinColumn(name = "AR_SALESPERSON", referencedColumnName = "SLP_CODE")
    @ManyToOne
    private LocalArSalesperson arSalesperson;

    @JoinColumn(name = "AR_TAX_CODE", referencedColumnName = "AR_TC_CODE")
    @ManyToOne
    private LocalArTaxCode arTaxCode;

    @JoinColumn(name = "AR_WITHHOLDING_TAX_CODE", referencedColumnName = "AR_WTC_CODE")
    @ManyToOne
    private LocalArWithholdingTaxCode arWithholdingTaxCode;

    @JoinColumn(name = "GL_FUNCTIONAL_CURRENCY", referencedColumnName = "FC_CODE")
    @ManyToOne
    private LocalGlFunctionalCurrency glFunctionalCurrency;

    @JoinColumn(name = "GL_INVESTOR_ACCOUNT_BALANCE", referencedColumnName = "IRAB_CODE")
    @ManyToOne
    private LocalGlInvestorAccountBalance glInvestorAccountBalance;

    @OneToMany(mappedBy = "arReceipt", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArDistributionRecord> arDistributionRecords;

    @OneToMany(mappedBy = "arReceipt", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArAppliedInvoice> arAppliedInvoices;

    @OneToMany(mappedBy = "arReceipt", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArInvoiceLine> arInvoiceLines;

    @OneToMany(mappedBy = "arReceipt", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArInvoiceLineItem> arInvoiceLineItems;

    @OneToMany(mappedBy = "arReceipt", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalCmFundTransferReceipt> cmFundTransferReceipts;

    public Integer getRctCode() {

        return rctCode;
    }

    public void setRctCode(Integer RCT_CODE) {

        this.rctCode = RCT_CODE;
    }

    public String getRctType() {

        return rctType;
    }

    public void setRctType(String RCT_TYP) {

        this.rctType = RCT_TYP;
    }

    public String getRctDocumentType() {

        return rctDocumentType;
    }

    public void setRctDocumentType(String RCT_DCMNT_TYP) {

        this.rctDocumentType = RCT_DCMNT_TYP;
    }

    public String getRctDescription() {

        return rctDescription;
    }

    public void setRctDescription(String RCT_DESC) {

        this.rctDescription = RCT_DESC;
    }

    public Date getRctDate() {

        return rctDate;
    }

    public void setRctDate(Date RCT_DT) {

        this.rctDate = RCT_DT;
    }

    public String getRctNumber() {

        return rctNumber;
    }

    public void setRctNumber(String RCT_NMBR) {

        this.rctNumber = RCT_NMBR;
    }

    public String getRctReferenceNumber() {

        return rctReferenceNumber;
    }

    public void setRctReferenceNumber(String RCT_RFRNC_NMBR) {

        this.rctReferenceNumber = RCT_RFRNC_NMBR;
    }

    public String getRctCheckNo() {

        return rctCheckNo;
    }

    public void setRctCheckNo(String RCT_CHCK_NO) {

        this.rctCheckNo = RCT_CHCK_NO;
    }

    public String getRctPayfileReferenceNumber() {

        return rctPayfileReferenceNumber;
    }

    public void setRctPayfileReferenceNumber(String RCT_PYFL_RFRNC_NMBR) {

        this.rctPayfileReferenceNumber = RCT_PYFL_RFRNC_NMBR;
    }

    public String getRctChequeNumber() {

        return rctChequeNumber;
    }

    public void setRctChequeNumber(String RCT_CHQ_NMBR) {

        this.rctChequeNumber = RCT_CHQ_NMBR;
    }

    public String getRctVoucherNumber() {

        return rctVoucherNumber;
    }

    public void setRctVoucherNumber(String RCT_VCHR_NMBR) {

        this.rctVoucherNumber = RCT_VCHR_NMBR;
    }

    public String getRctCardNumber1() {

        return rctCardNumber1;
    }

    public void setRctCardNumber1(String RCT_CRD_NMBR1) {

        this.rctCardNumber1 = RCT_CRD_NMBR1;
    }

    public String getRctCardNumber2() {

        return rctCardNumber2;
    }

    public void setRctCardNumber2(String RCT_CRD_NMBR2) {

        this.rctCardNumber2 = RCT_CRD_NMBR2;
    }

    public String getRctCardNumber3() {

        return rctCardNumber3;
    }

    public void setRctCardNumber3(String RCT_CRD_NMBR3) {

        this.rctCardNumber3 = RCT_CRD_NMBR3;
    }

    public double getRctAmount() {

        return rctAmount;
    }

    public void setRctAmount(double RCT_AMNT) {

        this.rctAmount = RCT_AMNT;
    }

    public double getRctAmountCash() {

        return rctAmountCash;
    }

    public void setRctAmountCash(double RCT_AMNT_CSH) {

        this.rctAmountCash = RCT_AMNT_CSH;
    }

    public double getRctAmountCheque() {

        return rctAmountCheque;
    }

    public void setRctAmountCheque(double RCT_AMNT_CHQ) {

        this.rctAmountCheque = RCT_AMNT_CHQ;
    }

    public double getRctAmountVoucher() {

        return rctAmountVoucher;
    }

    public void setRctAmountVoucher(double RCT_AMNT_VCHR) {

        this.rctAmountVoucher = RCT_AMNT_VCHR;
    }

    public double getRctAmountCard1() {

        return rctAmountCard1;
    }

    public void setRctAmountCard1(double RCT_AMNT_CRD1) {

        this.rctAmountCard1 = RCT_AMNT_CRD1;
    }

    public double getRctAmountCard2() {

        return rctAmountCard2;
    }

    public void setRctAmountCard2(double RCT_AMNT_CRD2) {

        this.rctAmountCard2 = RCT_AMNT_CRD2;
    }

    public double getRctAmountCard3() {

        return rctAmountCard3;
    }

    public void setRctAmountCard3(double RCT_AMNT_CRD3) {

        this.rctAmountCard3 = RCT_AMNT_CRD3;
    }

    public Date getRctConversionDate() {

        return rctConversionDate;
    }

    public void setRctConversionDate(Date RCT_CNVRSN_DT) {

        this.rctConversionDate = RCT_CNVRSN_DT;
    }

    public double getRctConversionRate() {

        return rctConversionRate;
    }

    public void setRctConversionRate(double RCT_CNVRSN_RT) {

        this.rctConversionRate = RCT_CNVRSN_RT;
    }

    public String getRctSoldTo() {

        return rctSoldTo;
    }

    public void setRctSoldTo(String RCT_SLD_TO) {

        this.rctSoldTo = RCT_SLD_TO;
    }

    public String getRctPaymentMethod() {

        return rctPaymentMethod;
    }

    public void setRctPaymentMethod(String RCT_PYMNT_MTHD) {

        this.rctPaymentMethod = RCT_PYMNT_MTHD;
    }

    public byte getRctCustomerDeposit() {

        return rctCustomerDeposit;
    }

    public void setRctCustomerDeposit(byte RCT_CSTMR_DPST) {

        this.rctCustomerDeposit = RCT_CSTMR_DPST;
    }

    public double getRctAppliedDeposit() {

        return rctAppliedDeposit;
    }

    public void setRctAppliedDeposit(double RCT_APPLD_DPST) {

        this.rctAppliedDeposit = RCT_APPLD_DPST;
    }

    public double getRctExcessAmount() {

        return rctExcessAmount;
    }

    public void setRctExcessAmount(double RCT_EXCSS_AMNT) {

        this.rctExcessAmount = RCT_EXCSS_AMNT;
    }

    public String getRctApprovalStatus() {

        return rctApprovalStatus;
    }

    public void setRctApprovalStatus(String RCT_APPRVL_STATUS) {

        this.rctApprovalStatus = RCT_APPRVL_STATUS;
    }

    public String getRctReasonForRejection() {

        return rctReasonForRejection;
    }

    public void setRctReasonForRejection(String RCT_RSN_FR_RJCTN) {

        this.rctReasonForRejection = RCT_RSN_FR_RJCTN;
    }

    public byte getRctEnableAdvancePayment() {

        return rctEnableAdvancePayment;
    }

    public void setRctEnableAdvancePayment(byte RCT_ENBL_ADVNC_PYMNT) {

        this.rctEnableAdvancePayment = RCT_ENBL_ADVNC_PYMNT;
    }

    public byte getRctPosted() {

        return rctPosted;
    }

    public void setRctPosted(byte RCT_PSTD) {

        this.rctPosted = RCT_PSTD;
    }

    public String getRctVoidApprovalStatus() {

        return rctVoidApprovalStatus;
    }

    public void setRctVoidApprovalStatus(String RCT_VD_APPRVL_STATUS) {

        this.rctVoidApprovalStatus = RCT_VD_APPRVL_STATUS;
    }

    public byte getRctVoidPosted() {

        return rctVoidPosted;
    }

    public void setRctVoidPosted(byte RCT_VD_PSTD) {

        this.rctVoidPosted = RCT_VD_PSTD;
    }

    public byte getRctVoid() {

        return rctVoid;
    }

    public void setRctVoid(byte RCT_VD) {

        this.rctVoid = RCT_VD;
    }

    public byte getRctReconciled() {

        return rctReconciled;
    }

    public void setRctReconciled(byte RCT_RCNCLD) {

        this.rctReconciled = RCT_RCNCLD;
    }

    public byte getRctReconciledCheque() {

        return rctReconciledCheque;
    }

    public void setRctReconciledCheque(byte RCT_RCNCLD_CHQ) {

        this.rctReconciledCheque = RCT_RCNCLD_CHQ;
    }

    public byte getRctReconciledCard1() {

        return rctReconciledCard1;
    }

    public void setRctReconciledCard1(byte RCT_RCNCLD_CRD1) {

        this.rctReconciledCard1 = RCT_RCNCLD_CRD1;
    }

    public byte getRctReconciledCard2() {

        return rctReconciledCard2;
    }

    public void setRctReconciledCard2(byte RCT_RCNCLD_CRD2) {

        this.rctReconciledCard2 = RCT_RCNCLD_CRD2;
    }

    public byte getRctReconciledCard3() {

        return rctReconciledCard3;
    }

    public void setRctReconciledCard3(byte RCT_RCNCLD_CRD3) {

        this.rctReconciledCard3 = RCT_RCNCLD_CRD3;
    }

    public Date getRctDateReconciled() {

        return rctDateReconciled;
    }

    public void setRctDateReconciled(Date RCT_DT_RCNCLD) {

        this.rctDateReconciled = RCT_DT_RCNCLD;
    }

    public Date getRctDateReconciledCheque() {

        return rctDateReconciledCheque;
    }

    public void setRctDateReconciledCheque(Date RCT_DT_RCNCLD_CHQ) {

        this.rctDateReconciledCheque = RCT_DT_RCNCLD_CHQ;
    }

    public Date getRctDateReconciledCard1() {

        return rctDateReconciledCard1;
    }

    public void setRctDateReconciledCard1(Date RCT_DT_RCNCLD_CRD1) {

        this.rctDateReconciledCard1 = RCT_DT_RCNCLD_CRD1;
    }

    public Date getRctDateReconciledCard2() {

        return rctDateReconciledCard2;
    }

    public void setRctDateReconciledCard2(Date RCT_DT_RCNCLD_CRD2) {

        this.rctDateReconciledCard2 = RCT_DT_RCNCLD_CRD2;
    }

    public Date getRctDateReconciledCard3() {

        return rctDateReconciledCard3;
    }

    public void setRctDateReconciledCard3(Date RCT_DT_RCNCLD_CRD3) {

        this.rctDateReconciledCard3 = RCT_DT_RCNCLD_CRD3;
    }

    public String getRctCreatedBy() {

        return rctCreatedBy;
    }

    public void setRctCreatedBy(String RCT_CRTD_BY) {

        this.rctCreatedBy = RCT_CRTD_BY;
    }

    public Date getRctDateCreated() {

        return rctDateCreated;
    }

    public void setRctDateCreated(Date RCT_DT_CRTD) {

        this.rctDateCreated = RCT_DT_CRTD;
    }

    public String getRctLastModifiedBy() {

        return rctLastModifiedBy;
    }

    public void setRctLastModifiedBy(String RCT_LST_MDFD_BY) {

        this.rctLastModifiedBy = RCT_LST_MDFD_BY;
    }

    public Date getRctDateLastModified() {

        return rctDateLastModified;
    }

    public void setRctDateLastModified(Date RCT_DT_LST_MDFD) {

        this.rctDateLastModified = RCT_DT_LST_MDFD;
    }

    public String getRctApprovedRejectedBy() {

        return rctApprovedRejectedBy;
    }

    public void setRctApprovedRejectedBy(String RCT_APPRVD_RJCTD_BY) {

        this.rctApprovedRejectedBy = RCT_APPRVD_RJCTD_BY;
    }

    public Date getRctDateApprovedRejected() {

        return rctDateApprovedRejected;
    }

    public void setRctDateApprovedRejected(Date RCT_DT_APPRVD_RJCTD) {

        this.rctDateApprovedRejected = RCT_DT_APPRVD_RJCTD;
    }

    public String getRctPostedBy() {

        return rctPostedBy;
    }

    public void setRctPostedBy(String RCT_PSTD_BY) {

        this.rctPostedBy = RCT_PSTD_BY;
    }

    public Date getRctDatePosted() {

        return rctDatePosted;
    }

    public void setRctDatePosted(Date RCT_DT_PSTD) {

        this.rctDatePosted = RCT_DT_PSTD;
    }

    public byte getRctPrinted() {

        return rctPrinted;
    }

    public void setRctPrinted(byte RCT_PRNTD) {

        this.rctPrinted = RCT_PRNTD;
    }

    public String getRctLvShift() {

        return rctLvShift;
    }

    public void setRctLvShift(String RCT_LV_SHFT) {

        this.rctLvShift = RCT_LV_SHFT;
    }

    public byte getRctLock() {

        return rctLock;
    }

    public void setRctLock(byte RCT_LCK) {

        this.rctLock = RCT_LCK;
    }

    public byte getRctSubjectToCommission() {

        return rctSubjectToCommission;
    }

    public void setRctSubjectToCommission(byte RCT_SBJCT_TO_CMMSSN) {

        this.rctSubjectToCommission = RCT_SBJCT_TO_CMMSSN;
    }

    public String getRctCustomerName() {

        return rctCustomerName;
    }

    public void setRctCustomerName(String RCT_CST_NM) {

        this.rctCustomerName = RCT_CST_NM;
    }

    public String getRctCustomerAddress() {

        return rctCustomerAddress;
    }

    public void setRctCustomerAddress(String RCT_CST_ADRSS) {

        this.rctCustomerAddress = RCT_CST_ADRSS;
    }

    public byte getRctInvtrBeginningBalance() {

        return rctInvtrBeginningBalance;
    }

    public void setRctInvtrBeginningBalance(byte RCT_INVTR_BGNNG_BLNC) {

        this.rctInvtrBeginningBalance = RCT_INVTR_BGNNG_BLNC;
    }

    public byte getRctInvtrInvestorFund() {

        return rctInvtrInvestorFund;
    }

    public void setRctInvtrInvestorFund(byte RCT_INVTR_IF) {

        this.rctInvtrInvestorFund = RCT_INVTR_IF;
    }

    public Date getRctInvtrNextRunDate() {

        return rctInvtrNextRunDate;
    }

    public void setRctInvtrNextRunDate(Date RCT_INVTR_NXT_RN_DT) {

        this.rctInvtrNextRunDate = RCT_INVTR_NXT_RN_DT;
    }

    public String getReportParameter() {

        return reportParameter;
    }

    public void setReportParameter(String REPORT_PARAMETER) {

        this.reportParameter = REPORT_PARAMETER;
    }

    public Integer getRctAdBranch() {

        return rctAdBranch;
    }

    public void setRctAdBranch(Integer RCT_AD_BRNCH) {

        this.rctAdBranch = RCT_AD_BRNCH;
    }

    public Integer getRctAdCompany() {

        return rctAdCompany;
    }

    public void setRctAdCompany(Integer RCT_AD_CMPNY) {

        this.rctAdCompany = RCT_AD_CMPNY;
    }

    public LocalAdBankAccount getAdBankAccount() {

        return adBankAccount;
    }

    public void setAdBankAccount(LocalAdBankAccount adBankAccount) {

        this.adBankAccount = adBankAccount;
    }

    public LocalAdBankAccount getAdBankAccountCard1() {

        return adBankAccountCard1;
    }

    public void setAdBankAccountCard1(LocalAdBankAccount adBankAccountCard1) {

        this.adBankAccountCard1 = adBankAccountCard1;
    }

    public LocalAdBankAccount getAdBankAccountCard2() {

        return adBankAccountCard2;
    }

    public void setAdBankAccountCard2(LocalAdBankAccount adBankAccountCard2) {

        this.adBankAccountCard2 = adBankAccountCard2;
    }

    public LocalAdBankAccount getAdBankAccountCard3() {

        return adBankAccountCard3;
    }

    public void setAdBankAccountCard3(LocalAdBankAccount adBankAccountCard3) {

        this.adBankAccountCard3 = adBankAccountCard3;
    }

    public LocalArCustomer getArCustomer() {

        return arCustomer;
    }

    public void setArCustomer(LocalArCustomer arCustomer) {

        this.arCustomer = arCustomer;
    }

    public LocalArReceiptBatch getArReceiptBatch() {

        return arReceiptBatch;
    }

    public void setArReceiptBatch(LocalArReceiptBatch arReceiptBatch) {

        this.arReceiptBatch = arReceiptBatch;
    }

    public LocalArSalesperson getArSalesperson() {

        return arSalesperson;
    }

    public void setArSalesperson(LocalArSalesperson arSalesperson) {

        this.arSalesperson = arSalesperson;
    }

    public LocalArTaxCode getArTaxCode() {

        return arTaxCode;
    }

    public void setArTaxCode(LocalArTaxCode arTaxCode) {

        this.arTaxCode = arTaxCode;
    }

    public LocalArWithholdingTaxCode getArWithholdingTaxCode() {

        return arWithholdingTaxCode;
    }

    public void setArWithholdingTaxCode(LocalArWithholdingTaxCode arWithholdingTaxCode) {

        this.arWithholdingTaxCode = arWithholdingTaxCode;
    }

    public LocalGlFunctionalCurrency getGlFunctionalCurrency() {

        return glFunctionalCurrency;
    }

    public void setGlFunctionalCurrency(LocalGlFunctionalCurrency glFunctionalCurrency) {

        this.glFunctionalCurrency = glFunctionalCurrency;
    }

    public LocalGlInvestorAccountBalance getGlInvestorAccountBalance() {

        return glInvestorAccountBalance;
    }

    public void setGlInvestorAccountBalance(LocalGlInvestorAccountBalance glInvestorAccountBalance) {

        this.glInvestorAccountBalance = glInvestorAccountBalance;
    }

    @XmlTransient
    public List getArDistributionRecords() {

        return arDistributionRecords;
    }

    public void setArDistributionRecords(List arDistributionRecords) {

        this.arDistributionRecords = arDistributionRecords;
    }

    @XmlTransient
    public List getArAppliedInvoices() {

        return arAppliedInvoices;
    }

    public void setArAppliedInvoices(List arAppliedInvoices) {

        this.arAppliedInvoices = arAppliedInvoices;
    }

    @XmlTransient
    public List getArInvoiceLines() {

        return arInvoiceLines;
    }

    public void setArInvoiceLines(List arInvoiceLines) {

        this.arInvoiceLines = arInvoiceLines;
    }

    @XmlTransient
    public List getArInvoiceLineItems() {

        return arInvoiceLineItems;
    }

    public void setArInvoiceLineItems(List arInvoiceLineItems) {

        this.arInvoiceLineItems = arInvoiceLineItems;
    }

    @XmlTransient
    public List getCmFundTransferReceipts() {

        return cmFundTransferReceipts;
    }

    public void setCmFundTransferReceipts(List cmFundTransferReceipts) {

        this.cmFundTransferReceipts = cmFundTransferReceipts;
    }

    public void addArDistributionRecord(LocalArDistributionRecord entity) {

        try {
            entity.setArReceipt(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArDistributionRecord(LocalArDistributionRecord entity) {

        try {
            entity.setArReceipt(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArAppliedInvoice(LocalArAppliedInvoice entity) {

        try {
            entity.setArReceipt(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArAppliedInvoice(LocalArAppliedInvoice entity) {

        try {
            entity.setArReceipt(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArInvoiceLine(LocalArInvoiceLine entity) {

        try {
            entity.setArReceipt(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArInvoiceLine(LocalArInvoiceLine entity) {

        try {
            entity.setArReceipt(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArInvoiceLineItem(LocalArInvoiceLineItem entity) {

        try {
            entity.setArReceipt(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArInvoiceLineItem(LocalArInvoiceLineItem entity) {

        try {
            entity.setArReceipt(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addCmFundTransferReceipt(LocalCmFundTransferReceipt entity) {

        try {
            entity.setArReceipt(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropCmFundTransferReceipt(LocalCmFundTransferReceipt entity) {

        try {
            entity.setArReceipt(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public short getArDrNextLine() {

        try {
            List lists = getArDistributionRecords();
            if (lists == null) {
                return 1;
            }
            return (short) (lists.size() + 1);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}