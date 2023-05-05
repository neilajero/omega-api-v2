package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "ArInvoice")
@Table(name = "AR_INVC")
public class LocalArInvoice extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INV_CODE", nullable = false)
    private Integer invCode;

    @Column(name = "INV_DCMNT_TYP", columnDefinition = "VARCHAR")
    private String invDocumentType;

    @Column(name = "INV_TYP", columnDefinition = "VARCHAR")
    private String invType;

    @Column(name = "INV_PRTL_CM", columnDefinition = "TINYINT")
    private byte invPartialCm;

    @Column(name = "INV_CRDT_MMO", columnDefinition = "TINYINT")
    private byte invCreditMemo;

    @Column(name = "INV_DESC", columnDefinition = "VARCHAR")
    private String invDescription;

    @Column(name = "INV_DT", columnDefinition = "DATETIME")
    private Date invDate;

    @Column(name = "INV_NMBR", columnDefinition = "VARCHAR")
    private String invNumber;

    @Column(name = "INV_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String invReferenceNumber;

    @Column(name = "INV_UPLD_NMBR", columnDefinition = "VARCHAR")
    private String invUploadNumber;

    @Column(name = "INV_CM_INVC_NMBR", columnDefinition = "VARCHAR")
    private String invCmInvoiceNumber;

    @Column(name = "INV_CM_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String invCmReferenceNumber;

    @Column(name = "INV_AMNT_DUE", columnDefinition = "DOUBLE")
    private double invAmountDue = 0;

    @Column(name = "INV_DWN_PYMNT", columnDefinition = "DOUBLE")
    private double invDownPayment = 0;

    @Column(name = "INV_AMNT_PD", columnDefinition = "DOUBLE")
    private double invAmountPaid = 0;

    @Column(name = "INV_PNT_DUE", columnDefinition = "DOUBLE")
    private double invPenaltyDue = 0;

    @Column(name = "INV_PNT_PD", columnDefinition = "DOUBLE")
    private double invPenaltyPaid = 0;

    @Column(name = "INV_AMNT_UNEARND_INT", columnDefinition = "DOUBLE")
    private double invAmountUnearnedInterest = 0;

    @Column(name = "INV_CNVRSN_DT", columnDefinition = "DATETIME")
    private Date invConversionDate;

    @Column(name = "INV_CNVRSN_RT", columnDefinition = "DOUBLE")
    private double invConversionRate = 0;

    @Column(name = "INV_MMO", columnDefinition = "VARCHAR")
    private String invMemo;

    @Column(name = "INV_PRV_RDNG", columnDefinition = "DOUBLE")
    private double invPreviousReading = 0;

    @Column(name = "INV_PRSNT_RDNG", columnDefinition = "DOUBLE")
    private double invPresentReading = 0;

    @Column(name = "INV_BLL_TO_ADDRSS", columnDefinition = "VARCHAR")
    private String invBillToAddress;

    @Column(name = "INV_BLL_TO_CNTCT", columnDefinition = "VARCHAR")
    private String invBillToContact;

    @Column(name = "INV_BLL_TO_ALT_CNTCT", columnDefinition = "VARCHAR")
    private String invBillToAltContact;

    @Column(name = "INV_BLL_TO_PHN", columnDefinition = "VARCHAR")
    private String invBillToPhone;

    @Column(name = "INV_BLLNG_HDR", columnDefinition = "VARCHAR")
    private String invBillingHeader;

    @Column(name = "INV_BLLNG_FTR", columnDefinition = "VARCHAR")
    private String invBillingFooter;

    @Column(name = "INV_BLLNG_HDR2", columnDefinition = "VARCHAR")
    private String invBillingHeader2;

    @Column(name = "INV_BLLNG_FTR2", columnDefinition = "VARCHAR")
    private String invBillingFooter2;

    @Column(name = "INV_BLLNG_HDR3", columnDefinition = "VARCHAR")
    private String invBillingHeader3;

    @Column(name = "INV_BLLNG_FTR3", columnDefinition = "VARCHAR")
    private String invBillingFooter3;

    @Column(name = "INV_BLLNG_SGNTRY", columnDefinition = "VARCHAR")
    private String invBillingSignatory;

    @Column(name = "INV_SGNTRY_TTL", columnDefinition = "VARCHAR")
    private String invSignatoryTitle;

    @Column(name = "INV_SHP_TO_ADDRSS", columnDefinition = "VARCHAR")
    private String invShipToAddress;

    @Column(name = "INV_SHP_TO_CNTCT", columnDefinition = "VARCHAR")
    private String invShipToContact;

    @Column(name = "INV_SHP_TO_ALT_CNTCT", columnDefinition = "VARCHAR")
    private String invShipToAltContact;

    @Column(name = "INV_SHP_TO_PHN", columnDefinition = "VARCHAR")
    private String invShipToPhone;

    @Column(name = "INV_SHP_DT", columnDefinition = "DATETIME")
    private Date invShipDate;

    @Column(name = "INV_LV_FRGHT", columnDefinition = "VARCHAR")
    private String invLvFreight;

    @Column(name = "INV_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String invApprovalStatus;

    @Column(name = "INV_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String invReasonForRejection;

    @Column(name = "INV_PSTD", columnDefinition = "TINYINT")
    private byte invPosted;

    @Column(name = "INV_VD_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String invVoidApprovalStatus;

    @Column(name = "INV_VD_PSTD", columnDefinition = "TINYINT")
    private byte invVoidPosted;

    @Column(name = "INV_VD", columnDefinition = "TINYINT")
    private byte invVoid;

    @Column(name = "INV_CONT", columnDefinition = "TINYINT")
    private byte invContention;

    @Column(name = "INV_DSBL_INTRST", columnDefinition = "TINYINT")
    private byte invDisableInterest;

    @Column(name = "INV_INTRST", columnDefinition = "TINYINT")
    private byte invInterest;

    @Column(name = "INV_INTRST_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String invInterestReferenceNumber;

    @Column(name = "INV_INTRST_AMNT", columnDefinition = "DOUBLE")
    private double invInterestAmount = 0;

    @Column(name = "INV_INTRST_CRTD_BY", columnDefinition = "VARCHAR")
    private String invInterestCreatedBy;

    @Column(name = "INV_INTRST_DT_CRTD", columnDefinition = "DATETIME")
    private Date invInterestDateCreated;

    @Column(name = "INV_INTRST_NXT_RN_DT", columnDefinition = "DATETIME")
    private Date invInterestNextRunDate;

    @Column(name = "INV_INTRST_LST_RN_DT", columnDefinition = "DATETIME")
    private Date invInterestLastRunDate;

    @Column(name = "INV_CRTD_BY", columnDefinition = "VARCHAR")
    private String invCreatedBy;

    @Column(name = "INV_DT_CRTD", columnDefinition = "DATETIME")
    private Date invDateCreated;

    @Column(name = "INV_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String invLastModifiedBy;

    @Column(name = "INV_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date invDateLastModified;

    @Column(name = "INV_APPRVD_RJCTD_BY", columnDefinition = "VARCHAR")
    private String invApprovedRejectedBy;

    @Column(name = "INV_DT_APPRVD_RJCTD", columnDefinition = "DATETIME")
    private Date invDateApprovedRejected;

    @Column(name = "INV_PSTD_BY", columnDefinition = "VARCHAR")
    private String invPostedBy;

    @Column(name = "INV_DT_PSTD", columnDefinition = "DATETIME")
    private Date invDatePosted;

    @Column(name = "INV_PRNTD", columnDefinition = "TINYINT")
    private byte invPrinted;

    @Column(name = "INV_LV_SHFT", columnDefinition = "VARCHAR")
    private String invLvShift;

    @Column(name = "INV_SO_NMBR", columnDefinition = "VARCHAR")
    private String invSoNumber;

    @Column(name = "INV_JO_NMBR", columnDefinition = "VARCHAR")
    private String invJoNumber;

    @Column(name = "INV_DBT_MEMO", columnDefinition = "TINYINT")
    private byte invDebitMemo;

    @Column(name = "INV_SBJCT_TO_CMMSSN", columnDefinition = "TINYINT")
    private byte invSubjectToCommission;

    @Column(name = "INV_CLNT_PO", columnDefinition = "VARCHAR")
    private String invClientPO;

    @Column(name = "INV_EFFCTVTY_DT", columnDefinition = "DATETIME")
    private Date invEffectivityDate;

    @Column(name = "INV_RCVD_DT", columnDefinition = "DATETIME")
    private Date invReceiveDate;

    @Column(name = "INV_HR_CRRNT_JB_PSTN", columnDefinition = "VARCHAR")
    private String invHrCurrentJobPosition;

    @Column(name = "INV_HR_MNGNG_BRNCH", columnDefinition = "VARCHAR")
    private String invHrManagingBranch;

    @Column(name = "REPORT_PARAMETER", columnDefinition = "VARCHAR")
    private String reportParameter;

    @Column(name = "INV_AD_BRNCH", columnDefinition = "INT")
    private Integer invAdBranch;

    @Column(name = "INV_AD_CMPNY", columnDefinition = "INT")
    private Integer invAdCompany;

    @JoinColumn(name = "AD_PAYMENT_TERM", referencedColumnName = "PYT_CODE")
    @ManyToOne
    private LocalAdPaymentTerm adPaymentTerm;

    @JoinColumn(name = "AR_CUSTOMER", referencedColumnName = "AR_CST_CODE")
    @ManyToOne
    private LocalArCustomer arCustomer;

    @JoinColumn(name = "AR_INVOICE_BATCH", referencedColumnName = "IB_CODE")
    @ManyToOne
    private LocalArInvoiceBatch arInvoiceBatch;

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

    @OneToMany(mappedBy = "arInvoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArDistributionRecord> arDistributionRecords;

    @OneToMany(mappedBy = "arInvoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArInvoicePaymentSchedule> arInvoicePaymentSchedules;

    @OneToMany(mappedBy = "arInvoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArInvoiceLine> arInvoiceLines;

    @OneToMany(mappedBy = "arInvoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArInvoiceLineItem> arInvoiceLineItems;

    @OneToMany(mappedBy = "arInvoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArSalesOrderInvoiceLine> arSalesOrderInvoiceLines;

    @OneToMany(mappedBy = "arInvoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArJobOrderInvoiceLine> arJobOrderInvoiceLines;

    public Integer getInvCode() {

        return invCode;
    }

    public void setInvCode(Integer INV_CODE) {

        this.invCode = INV_CODE;
    }

    public String getInvDocumentType() {

        return invDocumentType;
    }

    public void setInvDocumentType(String INV_DCMNT_TYP) {

        this.invDocumentType = INV_DCMNT_TYP;
    }

    public String getInvType() {

        return invType;
    }

    public void setInvType(String INV_TYP) {

        this.invType = INV_TYP;
    }

    public byte getInvPartialCm() {

        return invPartialCm;
    }

    public void setInvPartialCm(byte INV_PRTL_CM) {

        this.invPartialCm = INV_PRTL_CM;
    }

    public byte getInvCreditMemo() {

        return invCreditMemo;
    }

    public void setInvCreditMemo(byte INV_CRDT_MMO) {

        this.invCreditMemo = INV_CRDT_MMO;
    }

    public String getInvDescription() {

        return invDescription;
    }

    public void setInvDescription(String INV_DESC) {

        this.invDescription = INV_DESC;
    }

    public Date getInvDate() {

        return invDate;
    }

    public void setInvDate(Date INV_DT) {

        this.invDate = INV_DT;
    }

    public String getInvNumber() {

        return invNumber;
    }

    public void setInvNumber(String INV_NMBR) {

        this.invNumber = INV_NMBR;
    }

    public String getInvReferenceNumber() {

        return invReferenceNumber;
    }

    public void setInvReferenceNumber(String INV_RFRNC_NMBR) {

        this.invReferenceNumber = INV_RFRNC_NMBR;
    }

    public String getInvUploadNumber() {

        return invUploadNumber;
    }

    public void setInvUploadNumber(String INV_UPLD_NMBR) {

        this.invUploadNumber = INV_UPLD_NMBR;
    }

    public String getInvCmInvoiceNumber() {

        return invCmInvoiceNumber;
    }

    public void setInvCmInvoiceNumber(String INV_CM_INVC_NMBR) {

        this.invCmInvoiceNumber = INV_CM_INVC_NMBR;
    }

    public String getInvCmReferenceNumber() {

        return invCmReferenceNumber;
    }

    public void setInvCmReferenceNumber(String INV_CM_RFRNC_NMBR) {

        this.invCmReferenceNumber = INV_CM_RFRNC_NMBR;
    }

    public double getInvAmountDue() {

        return invAmountDue;
    }

    public void setInvAmountDue(double INV_AMNT_DUE) {

        this.invAmountDue = INV_AMNT_DUE;
    }

    public double getInvDownPayment() {

        return invDownPayment;
    }

    public void setInvDownPayment(double INV_DWN_PYMNT) {

        this.invDownPayment = INV_DWN_PYMNT;
    }

    public double getInvAmountPaid() {

        return invAmountPaid;
    }

    public void setInvAmountPaid(double INV_AMNT_PD) {

        this.invAmountPaid = INV_AMNT_PD;
    }

    public double getInvPenaltyDue() {

        return invPenaltyDue;
    }

    public void setInvPenaltyDue(double INV_PNT_DUE) {

        this.invPenaltyDue = INV_PNT_DUE;
    }

    public double getInvPenaltyPaid() {

        return invPenaltyPaid;
    }

    public void setInvPenaltyPaid(double INV_PNT_PD) {

        this.invPenaltyPaid = INV_PNT_PD;
    }

    public double getInvAmountUnearnedInterest() {

        return invAmountUnearnedInterest;
    }

    public void setInvAmountUnearnedInterest(double INV_AMNT_UNEARND_INT) {

        this.invAmountUnearnedInterest = INV_AMNT_UNEARND_INT;
    }

    public Date getInvConversionDate() {

        return invConversionDate;
    }

    public void setInvConversionDate(Date INV_CNVRSN_DT) {

        this.invConversionDate = INV_CNVRSN_DT;
    }

    public double getInvConversionRate() {

        return invConversionRate;
    }

    public void setInvConversionRate(double INV_CNVRSN_RT) {

        this.invConversionRate = INV_CNVRSN_RT;
    }

    public String getInvMemo() {

        return invMemo;
    }

    public void setInvMemo(String INV_MMO) {

        this.invMemo = INV_MMO;
    }

    public double getInvPreviousReading() {

        return invPreviousReading;
    }

    public void setInvPreviousReading(double INV_PRV_RDNG) {

        this.invPreviousReading = INV_PRV_RDNG;
    }

    public double getInvPresentReading() {

        return invPresentReading;
    }

    public void setInvPresentReading(double INV_PRSNT_RDNG) {

        this.invPresentReading = INV_PRSNT_RDNG;
    }

    public String getInvBillToAddress() {

        return invBillToAddress;
    }

    public void setInvBillToAddress(String INV_BLL_TO_ADDRSS) {

        this.invBillToAddress = INV_BLL_TO_ADDRSS;
    }

    public String getInvBillToContact() {

        return invBillToContact;
    }

    public void setInvBillToContact(String INV_BLL_TO_CNTCT) {

        this.invBillToContact = INV_BLL_TO_CNTCT;
    }

    public String getInvBillToAltContact() {

        return invBillToAltContact;
    }

    public void setInvBillToAltContact(String INV_BLL_TO_ALT_CNTCT) {

        this.invBillToAltContact = INV_BLL_TO_ALT_CNTCT;
    }

    public String getInvBillToPhone() {

        return invBillToPhone;
    }

    public void setInvBillToPhone(String INV_BLL_TO_PHN) {

        this.invBillToPhone = INV_BLL_TO_PHN;
    }

    public String getInvBillingHeader() {

        return invBillingHeader;
    }

    public void setInvBillingHeader(String INV_BLLNG_HDR) {

        this.invBillingHeader = INV_BLLNG_HDR;
    }

    public String getInvBillingFooter() {

        return invBillingFooter;
    }

    public void setInvBillingFooter(String INV_BLLNG_FTR) {

        this.invBillingFooter = INV_BLLNG_FTR;
    }

    public String getInvBillingHeader2() {

        return invBillingHeader2;
    }

    public void setInvBillingHeader2(String INV_BLLNG_HDR2) {

        this.invBillingHeader2 = INV_BLLNG_HDR2;
    }

    public String getInvBillingFooter2() {

        return invBillingFooter2;
    }

    public void setInvBillingFooter2(String INV_BLLNG_FTR2) {

        this.invBillingFooter2 = INV_BLLNG_FTR2;
    }

    public String getInvBillingHeader3() {

        return invBillingHeader3;
    }

    public void setInvBillingHeader3(String INV_BLLNG_HDR3) {

        this.invBillingHeader3 = INV_BLLNG_HDR3;
    }

    public String getInvBillingFooter3() {

        return invBillingFooter3;
    }

    public void setInvBillingFooter3(String INV_BLLNG_FTR3) {

        this.invBillingFooter3 = INV_BLLNG_FTR3;
    }

    public String getInvBillingSignatory() {

        return invBillingSignatory;
    }

    public void setInvBillingSignatory(String INV_BLLNG_SGNTRY) {

        this.invBillingSignatory = INV_BLLNG_SGNTRY;
    }

    public String getInvSignatoryTitle() {

        return invSignatoryTitle;
    }

    public void setInvSignatoryTitle(String INV_SGNTRY_TTL) {

        this.invSignatoryTitle = INV_SGNTRY_TTL;
    }

    public String getInvShipToAddress() {

        return invShipToAddress;
    }

    public void setInvShipToAddress(String INV_SHP_TO_ADDRSS) {

        this.invShipToAddress = INV_SHP_TO_ADDRSS;
    }

    public String getInvShipToContact() {

        return invShipToContact;
    }

    public void setInvShipToContact(String INV_SHP_TO_CNTCT) {

        this.invShipToContact = INV_SHP_TO_CNTCT;
    }

    public String getInvShipToAltContact() {

        return invShipToAltContact;
    }

    public void setInvShipToAltContact(String INV_SHP_TO_ALT_CNTCT) {

        this.invShipToAltContact = INV_SHP_TO_ALT_CNTCT;
    }

    public String getInvShipToPhone() {

        return invShipToPhone;
    }

    public void setInvShipToPhone(String INV_SHP_TO_PHN) {

        this.invShipToPhone = INV_SHP_TO_PHN;
    }

    public Date getInvShipDate() {

        return invShipDate;
    }

    public void setInvShipDate(Date INV_SHP_DT) {

        this.invShipDate = INV_SHP_DT;
    }

    public String getInvLvFreight() {

        return invLvFreight;
    }

    public void setInvLvFreight(String INV_LV_FRGHT) {

        this.invLvFreight = INV_LV_FRGHT;
    }

    public String getInvApprovalStatus() {

        return invApprovalStatus;
    }

    public void setInvApprovalStatus(String INV_APPRVL_STATUS) {

        this.invApprovalStatus = INV_APPRVL_STATUS;
    }

    public String getInvReasonForRejection() {

        return invReasonForRejection;
    }

    public void setInvReasonForRejection(String INV_RSN_FR_RJCTN) {

        this.invReasonForRejection = INV_RSN_FR_RJCTN;
    }

    public byte getInvPosted() {

        return invPosted;
    }

    public void setInvPosted(byte INV_PSTD) {

        this.invPosted = INV_PSTD;
    }

    public String getInvVoidApprovalStatus() {

        return invVoidApprovalStatus;
    }

    public void setInvVoidApprovalStatus(String INV_VD_APPRVL_STATUS) {

        this.invVoidApprovalStatus = INV_VD_APPRVL_STATUS;
    }

    public byte getInvVoidPosted() {

        return invVoidPosted;
    }

    public void setInvVoidPosted(byte INV_VD_PSTD) {

        this.invVoidPosted = INV_VD_PSTD;
    }

    public byte getInvVoid() {

        return invVoid;
    }

    public void setInvVoid(byte INV_VD) {

        this.invVoid = INV_VD;
    }

    public byte getInvContention() {

        return invContention;
    }

    public void setInvContention(byte INV_CONT) {

        this.invContention = INV_CONT;
    }

    public byte getInvDisableInterest() {

        return invDisableInterest;
    }

    public void setInvDisableInterest(byte INV_DSBL_INTRST) {

        this.invDisableInterest = INV_DSBL_INTRST;
    }

    public byte getInvInterest() {

        return invInterest;
    }

    public void setInvInterest(byte INV_INTRST) {

        this.invInterest = INV_INTRST;
    }

    public String getInvInterestReferenceNumber() {

        return invInterestReferenceNumber;
    }

    public void setInvInterestReferenceNumber(String INV_INTRST_RFRNC_NMBR) {

        this.invInterestReferenceNumber = INV_INTRST_RFRNC_NMBR;
    }

    public double getInvInterestAmount() {

        return invInterestAmount;
    }

    public void setInvInterestAmount(double INV_INTRST_AMNT) {

        this.invInterestAmount = INV_INTRST_AMNT;
    }

    public String getInvInterestCreatedBy() {

        return invInterestCreatedBy;
    }

    public void setInvInterestCreatedBy(String INV_INTRST_CRTD_BY) {

        this.invInterestCreatedBy = INV_INTRST_CRTD_BY;
    }

    public Date getInvInterestDateCreated() {

        return invInterestDateCreated;
    }

    public void setInvInterestDateCreated(Date INV_INTRST_DT_CRTD) {

        this.invInterestDateCreated = INV_INTRST_DT_CRTD;
    }

    public Date getInvInterestNextRunDate() {

        return invInterestNextRunDate;
    }

    public void setInvInterestNextRunDate(Date INV_INTRST_NXT_RN_DT) {

        this.invInterestNextRunDate = INV_INTRST_NXT_RN_DT;
    }

    public Date getInvInterestLastRunDate() {

        return invInterestLastRunDate;
    }

    public void setInvInterestLastRunDate(Date INV_INTRST_LST_RN_DT) {

        this.invInterestLastRunDate = INV_INTRST_LST_RN_DT;
    }

    public String getInvCreatedBy() {

        return invCreatedBy;
    }

    public void setInvCreatedBy(String INV_CRTD_BY) {

        this.invCreatedBy = INV_CRTD_BY;
    }

    public Date getInvDateCreated() {

        return invDateCreated;
    }

    public void setInvDateCreated(Date INV_DT_CRTD) {

        this.invDateCreated = INV_DT_CRTD;
    }

    public String getInvLastModifiedBy() {

        return invLastModifiedBy;
    }

    public void setInvLastModifiedBy(String INV_LST_MDFD_BY) {

        this.invLastModifiedBy = INV_LST_MDFD_BY;
    }

    public Date getInvDateLastModified() {

        return invDateLastModified;
    }

    public void setInvDateLastModified(Date INV_DT_LST_MDFD) {

        this.invDateLastModified = INV_DT_LST_MDFD;
    }

    public String getInvApprovedRejectedBy() {

        return invApprovedRejectedBy;
    }

    public void setInvApprovedRejectedBy(String INV_APPRVD_RJCTD_BY) {

        this.invApprovedRejectedBy = INV_APPRVD_RJCTD_BY;
    }

    public Date getInvDateApprovedRejected() {

        return invDateApprovedRejected;
    }

    public void setInvDateApprovedRejected(Date INV_DT_APPRVD_RJCTD) {

        this.invDateApprovedRejected = INV_DT_APPRVD_RJCTD;
    }

    public String getInvPostedBy() {

        return invPostedBy;
    }

    public void setInvPostedBy(String INV_PSTD_BY) {

        this.invPostedBy = INV_PSTD_BY;
    }

    public Date getInvDatePosted() {

        return invDatePosted;
    }

    public void setInvDatePosted(Date INV_DT_PSTD) {

        this.invDatePosted = INV_DT_PSTD;
    }

    public byte getInvPrinted() {

        return invPrinted;
    }

    public void setInvPrinted(byte INV_PRNTD) {

        this.invPrinted = INV_PRNTD;
    }

    public String getInvLvShift() {

        return invLvShift;
    }

    public void setInvLvShift(String INV_LV_SHFT) {

        this.invLvShift = INV_LV_SHFT;
    }

    public String getInvSoNumber() {

        return invSoNumber;
    }

    public void setInvSoNumber(String INV_SO_NMBR) {

        this.invSoNumber = INV_SO_NMBR;
    }

    public String getInvJoNumber() {

        return invJoNumber;
    }

    public void setInvJoNumber(String INV_JO_NMBR) {

        this.invJoNumber = INV_JO_NMBR;
    }

    public byte getInvDebitMemo() {

        return invDebitMemo;
    }

    public void setInvDebitMemo(byte INV_DBT_MEMO) {

        this.invDebitMemo = INV_DBT_MEMO;
    }

    public byte getInvSubjectToCommission() {

        return invSubjectToCommission;
    }

    public void setInvSubjectToCommission(byte INV_SBJCT_TO_CMMSSN) {

        this.invSubjectToCommission = INV_SBJCT_TO_CMMSSN;
    }

    public String getInvClientPO() {

        return invClientPO;
    }

    public void setInvClientPO(String INV_CLNT_PO) {

        this.invClientPO = INV_CLNT_PO;
    }

    public Date getInvEffectivityDate() {

        return invEffectivityDate;
    }

    public void setInvEffectivityDate(Date INV_EFFCTVTY_DT) {

        this.invEffectivityDate = INV_EFFCTVTY_DT;
    }

    public Date getInvReceiveDate() {

        return invReceiveDate;
    }

    public void setInvReceiveDate(Date INV_RCVD_DT) {

        this.invReceiveDate = INV_RCVD_DT;
    }

    public String getInvHrCurrentJobPosition() {

        return invHrCurrentJobPosition;
    }

    public void setInvHrCurrentJobPosition(String INV_HR_CRRNT_JB_PSTN) {

        this.invHrCurrentJobPosition = INV_HR_CRRNT_JB_PSTN;
    }

    public String getInvHrManagingBranch() {

        return invHrManagingBranch;
    }

    public void setInvHrManagingBranch(String INV_HR_MNGNG_BRNCH) {

        this.invHrManagingBranch = INV_HR_MNGNG_BRNCH;
    }

    public String getReportParameter() {

        return reportParameter;
    }

    public void setReportParameter(String REPORT_PARAMETER) {

        this.reportParameter = REPORT_PARAMETER;
    }

    public Integer getInvAdBranch() {

        return invAdBranch;
    }

    public void setInvAdBranch(Integer INV_AD_BRNCH) {

        this.invAdBranch = INV_AD_BRNCH;
    }

    public Integer getInvAdCompany() {

        return invAdCompany;
    }

    public void setInvAdCompany(Integer INV_AD_CMPNY) {

        this.invAdCompany = INV_AD_CMPNY;
    }

    public LocalAdPaymentTerm getAdPaymentTerm() {

        return adPaymentTerm;
    }

    public void setAdPaymentTerm(LocalAdPaymentTerm adPaymentTerm) {

        this.adPaymentTerm = adPaymentTerm;
    }

    public LocalArCustomer getArCustomer() {

        return arCustomer;
    }

    public void setArCustomer(LocalArCustomer arCustomer) {

        this.arCustomer = arCustomer;
    }

    public LocalArInvoiceBatch getArInvoiceBatch() {

        return arInvoiceBatch;
    }

    public void setArInvoiceBatch(LocalArInvoiceBatch arInvoiceBatch) {

        this.arInvoiceBatch = arInvoiceBatch;
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

    @XmlTransient
    public List getArDistributionRecords() {

        return arDistributionRecords;
    }

    public void setArDistributionRecords(List arDistributionRecords) {

        this.arDistributionRecords = arDistributionRecords;
    }

    @XmlTransient
    public List getArInvoicePaymentSchedules() {

        return arInvoicePaymentSchedules;
    }

    public void setArInvoicePaymentSchedules(List arInvoicePaymentSchedules) {

        this.arInvoicePaymentSchedules = arInvoicePaymentSchedules;
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
    public List getArSalesOrderInvoiceLines() {

        return arSalesOrderInvoiceLines;
    }

    public void setArSalesOrderInvoiceLines(List arSalesOrderInvoiceLines) {

        this.arSalesOrderInvoiceLines = arSalesOrderInvoiceLines;
    }

    @XmlTransient
    public List getArJobOrderInvoiceLines() {

        return arJobOrderInvoiceLines;
    }

    public void setArJobOrderInvoiceLines(List arJobOrderInvoiceLines) {

        this.arJobOrderInvoiceLines = arJobOrderInvoiceLines;
    }

    public void addArDistributionRecord(LocalArDistributionRecord entity) {

        try {
            entity.setArInvoice(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArDistributionRecord(LocalArDistributionRecord entity) {

        try {
            entity.setArInvoice(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArInvoicePaymentSchedule(LocalArInvoicePaymentSchedule entity) {

        try {
            entity.setArInvoice(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArInvoicePaymentSchedule(LocalArInvoicePaymentSchedule entity) {

        try {
            entity.setArInvoice(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArInvoiceLine(LocalArInvoiceLine entity) {

        try {
            entity.setArInvoice(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArInvoiceLine(LocalArInvoiceLine entity) {

        try {
            entity.setArInvoice(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArInvoiceLineItem(LocalArInvoiceLineItem entity) {

        try {
            entity.setArInvoice(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArInvoiceLineItem(LocalArInvoiceLineItem entity) {

        try {
            entity.setArInvoice(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArSalesOrderInvoiceLine(LocalArSalesOrderInvoiceLine entity) {

        try {
            entity.setArInvoice(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArSalesOrderInvoiceLine(LocalArSalesOrderInvoiceLine entity) {

        try {
            entity.setArInvoice(null);
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