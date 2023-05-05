package com.ejb.entities.ap;

import com.ejb.NativeQueryHome;
import com.ejb.dao.ap.LocalApVoucherBatch;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "ApVoucher")
@Table(name = "AP_VCHR")
public class LocalApVoucher extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VOU_CODE", nullable = false)
    private Integer vouCode;

    @Column(name = "VOU_DBT_MMO", columnDefinition = "TINYINT")
    private byte vouDebitMemo;

    @Column(name = "VOU_TYP", columnDefinition = "VARCHAR")
    private String vouType;

    @Column(name = "VOU_DESC", columnDefinition = "VARCHAR")
    private String vouDescription;

    @Column(name = "VOU_DT", columnDefinition = "DATETIME")
    private Date vouDate;

    @Column(name = "VOU_DCMNT_NMBR", columnDefinition = "VARCHAR")
    private String vouDocumentNumber;

    @Column(name = "VOU_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String vouReferenceNumber;

    @Column(name = "VOU_DM_VCHR_NMBR", columnDefinition = "VARCHAR")
    private String vouDmVoucherNumber;

    @Column(name = "VOU_CNVRSN_DT", columnDefinition = "DATETIME")
    private Date vouConversionDate;

    @Column(name = "VOU_CNVRSN_RT", columnDefinition = "DOUBLE")
    private double vouConversionRate = 0;

    @Column(name = "VOU_BLL_AMNT", columnDefinition = "DOUBLE")
    private double vouBillAmount = 0;

    @Column(name = "VOU_AMNT_DUE", columnDefinition = "DOUBLE")
    private double vouAmountDue = 0;

    @Column(name = "VOU_AMNT_PD", columnDefinition = "DOUBLE")
    private double vouAmountPaid = 0;

    @Column(name = "VOU_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String vouApprovalStatus;

    @Column(name = "VOU_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String vouReasonForRejection;

    @Column(name = "VOU_PSTD")
    private byte vouPosted;

    @Column(name = "VOU_GNRTD")
    private byte vouGenerated;

    @Column(name = "VOU_VD")
    private byte vouVoid;

    @Column(name = "VOU_CRTD_BY")
    private String vouCreatedBy;

    @Column(name = "VOU_DT_CRTD")
    private Date vouDateCreated;

    @Column(name = "VOU_LST_MDFD_BY")
    private String vouLastModifiedBy;

    @Column(name = "VOU_DT_LST_MDFD")
    private Date vouDateLastModified;

    @Column(name = "VOU_APPRVD_RJCTD_BY")
    private String vouApprovedRejectedBy;

    @Column(name = "VOU_DT_APPRVD_RJCTD")
    private Date vouDateApprovedRejected;

    @Column(name = "VOU_PSTD_BY")
    private String vouPostedBy;

    @Column(name = "VOU_DT_PSTD")
    private Date vouDatePosted;

    @Column(name = "VOU_PRNTD")
    private byte vouPrinted;

    @Column(name = "VOU_PO_NMBR")
    private String vouPoNumber;

    @Column(name = "VOU_LN")
    private byte vouLoan;

    @Column(name = "VOU_LN_GNRTD")
    private byte vouLoanGenerated;

    @Column(name = "VOU_AD_BRNCH")
    private Integer vouAdBranch;

    @Column(name = "VOU_AD_CMPNY")
    private Integer vouAdCompany;

    @JoinColumn(name = "AD_PAYMENT_TERM", referencedColumnName = "PYT_CODE")
    @ManyToOne
    private LocalAdPaymentTerm adPaymentTerm;

    @JoinColumn(name = "AD_PAYMENT_TERM2", referencedColumnName = "PYT_CODE")
    @ManyToOne
    private LocalAdPaymentTerm adPaymentTerm2;

    @JoinColumn(name = "AP_SUPPLIER", referencedColumnName = "SPL_CODE")
    @ManyToOne
    private LocalApSupplier apSupplier;

    @JoinColumn(name = "AP_TAX_CODE", referencedColumnName = "AP_TC_CODE")
    @ManyToOne
    private LocalApTaxCode apTaxCode;

    @JoinColumn(name = "AP_VOUCHER_BATCH", referencedColumnName = "VB_CODE")
    @ManyToOne
    private LocalApVoucherBatch apVoucherBatch;

    @JoinColumn(name = "AP_WITHHOLDING_TAX_CODE", referencedColumnName = "AP_WTC_CODE")
    @ManyToOne
    private LocalApWithholdingTaxCode apWithholdingTaxCode;

    @JoinColumn(name = "GL_FUNCTIONAL_CURRENCY", referencedColumnName = "FC_CODE")
    @ManyToOne
    private LocalGlFunctionalCurrency glFunctionalCurrency;

    @OneToMany(mappedBy = "apVoucher", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalApDistributionRecord> apDistributionRecords;

    @OneToMany(mappedBy = "apVoucher", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalApVoucherPaymentSchedule> apVoucherPaymentSchedules;

    @OneToMany(mappedBy = "apVoucher", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalApVoucherLineItem> apVoucherLineItems;

    @OneToMany(mappedBy = "apVoucher", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalApPurchaseOrderLine> apPurchaseOrderLines;

    public Integer getVouCode() {

        return vouCode;
    }

    public void setVouCode(Integer VOU_CODE) {

        this.vouCode = VOU_CODE;
    }

    public byte getVouDebitMemo() {

        return vouDebitMemo;
    }

    public void setVouDebitMemo(byte VOU_DBT_MMO) {

        this.vouDebitMemo = VOU_DBT_MMO;
    }

    public String getVouType() {

        return vouType;
    }

    public void setVouType(String VOU_TYP) {

        this.vouType = VOU_TYP;
    }

    public String getVouDescription() {

        return vouDescription;
    }

    public void setVouDescription(String VOU_DESC) {

        this.vouDescription = VOU_DESC;
    }

    public Date getVouDate() {

        return vouDate;
    }

    public void setVouDate(Date VOU_DT) {

        this.vouDate = VOU_DT;
    }

    public String getVouDocumentNumber() {

        return vouDocumentNumber;
    }

    public void setVouDocumentNumber(String VOU_DCMNT_NMBR) {

        this.vouDocumentNumber = VOU_DCMNT_NMBR;
    }

    public String getVouReferenceNumber() {

        return vouReferenceNumber;
    }

    public void setVouReferenceNumber(String VOU_RFRNC_NMBR) {

        this.vouReferenceNumber = VOU_RFRNC_NMBR;
    }

    public String getVouDmVoucherNumber() {

        return vouDmVoucherNumber;
    }

    public void setVouDmVoucherNumber(String VOU_DM_VCHR_NMBR) {

        this.vouDmVoucherNumber = VOU_DM_VCHR_NMBR;
    }

    public Date getVouConversionDate() {

        return vouConversionDate;
    }

    public void setVouConversionDate(Date VOU_CNVRSN_DT) {

        this.vouConversionDate = VOU_CNVRSN_DT;
    }

    public double getVouConversionRate() {

        return vouConversionRate;
    }

    public void setVouConversionRate(double VOU_CNVRSN_RT) {

        this.vouConversionRate = VOU_CNVRSN_RT;
    }

    public double getVouBillAmount() {

        return vouBillAmount;
    }

    public void setVouBillAmount(double VOU_BLL_AMNT) {

        this.vouBillAmount = VOU_BLL_AMNT;
    }

    public double getVouAmountDue() {

        return vouAmountDue;
    }

    public void setVouAmountDue(double VOU_AMNT_DUE) {

        this.vouAmountDue = VOU_AMNT_DUE;
    }

    public double getVouAmountPaid() {

        return vouAmountPaid;
    }

    public void setVouAmountPaid(double VOU_AMNT_PD) {

        this.vouAmountPaid = VOU_AMNT_PD;
    }

    public String getVouApprovalStatus() {

        return vouApprovalStatus;
    }

    public void setVouApprovalStatus(String VOU_APPRVL_STATUS) {

        this.vouApprovalStatus = VOU_APPRVL_STATUS;
    }

    public String getVouReasonForRejection() {

        return vouReasonForRejection;
    }

    public void setVouReasonForRejection(String VOU_RSN_FR_RJCTN) {

        this.vouReasonForRejection = VOU_RSN_FR_RJCTN;
    }

    public byte getVouPosted() {

        return vouPosted;
    }

    public void setVouPosted(byte VOU_PSTD) {

        this.vouPosted = VOU_PSTD;
    }

    public byte getVouGenerated() {

        return vouGenerated;
    }

    public void setVouGenerated(byte VOU_GNRTD) {

        this.vouGenerated = VOU_GNRTD;
    }

    public byte getVouVoid() {

        return vouVoid;
    }

    public void setVouVoid(byte VOU_VD) {

        this.vouVoid = VOU_VD;
    }

    public String getVouCreatedBy() {

        return vouCreatedBy;
    }

    public void setVouCreatedBy(String VOU_CRTD_BY) {

        this.vouCreatedBy = VOU_CRTD_BY;
    }

    public Date getVouDateCreated() {

        return vouDateCreated;
    }

    public void setVouDateCreated(Date VOU_DT_CRTD) {

        this.vouDateCreated = VOU_DT_CRTD;
    }

    public String getVouLastModifiedBy() {

        return vouLastModifiedBy;
    }

    public void setVouLastModifiedBy(String VOU_LST_MDFD_BY) {

        this.vouLastModifiedBy = VOU_LST_MDFD_BY;
    }

    public Date getVouDateLastModified() {

        return vouDateLastModified;
    }

    public void setVouDateLastModified(Date VOU_DT_LST_MDFD) {

        this.vouDateLastModified = VOU_DT_LST_MDFD;
    }

    public String getVouApprovedRejectedBy() {

        return vouApprovedRejectedBy;
    }

    public void setVouApprovedRejectedBy(String VOU_APPRVD_RJCTD_BY) {

        this.vouApprovedRejectedBy = VOU_APPRVD_RJCTD_BY;
    }

    public Date getVouDateApprovedRejected() {

        return vouDateApprovedRejected;
    }

    public void setVouDateApprovedRejected(Date VOU_DT_APPRVD_RJCTD) {

        this.vouDateApprovedRejected = VOU_DT_APPRVD_RJCTD;
    }

    public String getVouPostedBy() {

        return vouPostedBy;
    }

    public void setVouPostedBy(String VOU_PSTD_BY) {

        this.vouPostedBy = VOU_PSTD_BY;
    }

    public Date getVouDatePosted() {

        return vouDatePosted;
    }

    public void setVouDatePosted(Date VOU_DT_PSTD) {

        this.vouDatePosted = VOU_DT_PSTD;
    }

    public byte getVouPrinted() {

        return vouPrinted;
    }

    public void setVouPrinted(byte VOU_PRNTD) {

        this.vouPrinted = VOU_PRNTD;
    }

    public String getVouPoNumber() {

        return vouPoNumber;
    }

    public void setVouPoNumber(String VOU_PO_NMBR) {

        this.vouPoNumber = VOU_PO_NMBR;
    }

    public byte getVouLoan() {

        return vouLoan;
    }

    public void setVouLoan(byte VOU_LN) {

        this.vouLoan = VOU_LN;
    }

    public byte getVouLoanGenerated() {

        return vouLoanGenerated;
    }

    public void setVouLoanGenerated(byte VOU_LN_GNRTD) {

        this.vouLoanGenerated = VOU_LN_GNRTD;
    }

    public Integer getVouAdBranch() {

        return vouAdBranch;
    }

    public void setVouAdBranch(Integer VOU_AD_BRNCH) {

        this.vouAdBranch = VOU_AD_BRNCH;
    }

    public Integer getVouAdCompany() {

        return vouAdCompany;
    }

    public void setVouAdCompany(Integer VOU_AD_CMPNY) {

        this.vouAdCompany = VOU_AD_CMPNY;
    }

    public LocalAdPaymentTerm getAdPaymentTerm() {

        return adPaymentTerm;
    }

    public void setAdPaymentTerm(LocalAdPaymentTerm adPaymentTerm) {

        this.adPaymentTerm = adPaymentTerm;
    }

    public LocalAdPaymentTerm getAdPaymentTerm2() {

        return adPaymentTerm2;
    }

    public void setAdPaymentTerm2(LocalAdPaymentTerm adPaymentTerm2) {

        this.adPaymentTerm2 = adPaymentTerm2;
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

    public LocalApVoucherBatch getApVoucherBatch() {

        return apVoucherBatch;
    }

    public void setApVoucherBatch(LocalApVoucherBatch apVoucherBatch) {

        this.apVoucherBatch = apVoucherBatch;
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
    public List getApDistributionRecords() {

        return apDistributionRecords;
    }

    public void setApDistributionRecords(List apDistributionRecords) {

        this.apDistributionRecords = apDistributionRecords;
    }

    @XmlTransient
    public List getApVoucherPaymentSchedules() {

        return apVoucherPaymentSchedules;
    }

    public void setApVoucherPaymentSchedules(List apVoucherPaymentSchedules) {

        this.apVoucherPaymentSchedules = apVoucherPaymentSchedules;
    }

    @XmlTransient
    public List getApVoucherLineItems() {

        return apVoucherLineItems;
    }

    public void setApVoucherLineItems(List apVoucherLineItems) {

        this.apVoucherLineItems = apVoucherLineItems;
    }

    @XmlTransient
    public List getApPurchaseOrderLines() {

        return apPurchaseOrderLines;
    }

    public void setApPurchaseOrderLines(List apPurchaseOrderLines) {

        this.apPurchaseOrderLines = apPurchaseOrderLines;
    }

    public void addApDistributionRecord(LocalApDistributionRecord entity) {

        try {
            entity.setApVoucher(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApDistributionRecord(LocalApDistributionRecord entity) {

        try {
            entity.setApVoucher(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApVoucherPaymentSchedule(LocalApVoucherPaymentSchedule entity) {

        try {
            entity.setApVoucher(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApVoucherPaymentSchedule(LocalApVoucherPaymentSchedule entity) {

        try {
            entity.setApVoucher(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApVoucherLineItem(LocalApVoucherLineItem entity) {

        try {
            entity.setApVoucher(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApVoucherLineItem(LocalApVoucherLineItem entity) {

        try {
            entity.setApVoucher(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApPurchaseOrderLine(LocalApPurchaseOrderLine entity) {

        try {
            entity.setApVoucher(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApPurchaseOrderLine(LocalApPurchaseOrderLine entity) {

        try {
            entity.setApVoucher(null);
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