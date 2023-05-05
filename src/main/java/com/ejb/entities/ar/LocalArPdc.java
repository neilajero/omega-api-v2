package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "ArPdc")
@Table(name = "AR_PDC")
public class LocalArPdc extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PDC_CODE", nullable = false)
    private Integer pdcCode;

    @Column(name = "PDC_STATUS", columnDefinition = "VARCHAR")
    private String pdcStatus;

    @Column(name = "PDC_LV_SHFT", columnDefinition = "VARCHAR")
    private String pdcLvShift;

    @Column(name = "PDC_CHCK_NMBR", columnDefinition = "VARCHAR")
    private String pdcCheckNumber;

    @Column(name = "PDC_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String pdcReferenceNumber;

    @Column(name = "PDC_DT_RCVD", columnDefinition = "DATETIME")
    private Date pdcDateReceived;

    @Column(name = "PDC_MTRTY_DT", columnDefinition = "DATETIME")
    private Date pdcMaturityDate;

    @Column(name = "PDC_DESC", columnDefinition = "VARCHAR")
    private String pdcDescription;

    @Column(name = "PDC_CNCLLD", columnDefinition = "TINYINT")
    private byte pdcCancelled;

    @Column(name = "PDC_AMNT", columnDefinition = "DOUBLE")
    private double pdcAmount = 0;

    @Column(name = "PDC_CNVRSN_RT", columnDefinition = "DOUBLE")
    private double pdcConversionRate = 0;

    @Column(name = "PDC_CNVRSN_DT", columnDefinition = "DATETIME")
    private Date pdcConversionDate;

    @Column(name = "PDC_LV_FRGHT", columnDefinition = "VARCHAR")
    private String pdcLvFreight;

    @Column(name = "PDC_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String pdcApprovalStatus;

    @Column(name = "PDC_PSTD", columnDefinition = "TINYINT")
    private byte pdcPosted;

    @Column(name = "PDC_CRTD_BY", columnDefinition = "VARCHAR")
    private String pdcCreatedBy;

    @Column(name = "PDC_DT_CRTD", columnDefinition = "DATETIME")
    private Date pdcDateCreated;

    @Column(name = "PDC_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String pdcLastModifiedBy;

    @Column(name = "PDC_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date pdcDateLastModified;

    @Column(name = "PDC_APPRVD_RJCTD_BY", columnDefinition = "VARCHAR")
    private String pdcApprovedRejectedBy;

    @Column(name = "PDC_DT_APPRVD_RJCTD", columnDefinition = "DATETIME")
    private Date pdcDateApprovedRejected;

    @Column(name = "PDC_PSTD_BY", columnDefinition = "VARCHAR")
    private String pdcPostedBy;

    @Column(name = "PDC_DT_PSTD", columnDefinition = "DATETIME")
    private Date pdcDatePosted;

    @Column(name = "PDC_PRNTD", columnDefinition = "TINYINT")
    private byte pdcPrinted;

    @Column(name = "PDC_EFFCTVTY_DT", columnDefinition = "DATETIME")
    private Date pdcEffectivityDate;

    @Column(name = "PDC_AD_BRNCH", columnDefinition = "INT")
    private Integer pdcAdBranch;

    @Column(name = "PDC_AD_CMPNY", columnDefinition = "INT")
    private Integer pdcAdCompany;

    @JoinColumn(name = "AD_BANK_ACCOUNT", referencedColumnName = "BA_CODE")
    @ManyToOne
    private LocalAdBankAccount adBankAccount;

    @JoinColumn(name = "AD_PAYMENT_TERM", referencedColumnName = "PYT_CODE")
    @ManyToOne
    private LocalAdPaymentTerm adPaymentTerm;

    @JoinColumn(name = "AR_CUSTOMER", referencedColumnName = "AR_CST_CODE")
    @ManyToOne
    private LocalArCustomer arCustomer;

    @JoinColumn(name = "AR_TAX_CODE", referencedColumnName = "AR_TC_CODE")
    @ManyToOne
    private LocalArTaxCode arTaxCode;

    @JoinColumn(name = "AR_WITHHOLDING_TAX_CODE", referencedColumnName = "AR_WTC_CODE")
    @ManyToOne
    private LocalArWithholdingTaxCode arWithholdingTaxCode;

    @JoinColumn(name = "GL_FUNCTIONAL_CURRENCY", referencedColumnName = "FC_CODE")
    @ManyToOne
    private LocalGlFunctionalCurrency glFunctionalCurrency;

    @OneToMany(mappedBy = "arPdc", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArAppliedInvoice> arAppliedInvoices;

    @OneToMany(mappedBy = "arPdc", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArInvoiceLine> arInvoiceLines;

    @OneToMany(mappedBy = "arPdc", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArInvoiceLineItem> arInvoiceLineItems;

    public Integer getPdcCode() {

        return pdcCode;
    }

    public void setPdcCode(Integer PDC_CODE) {

        this.pdcCode = PDC_CODE;
    }

    public String getPdcStatus() {

        return pdcStatus;
    }

    public void setPdcStatus(String PDC_STATUS) {

        this.pdcStatus = PDC_STATUS;
    }

    public String getPdcLvShift() {

        return pdcLvShift;
    }

    public void setPdcLvShift(String PDC_LV_SHFT) {

        this.pdcLvShift = PDC_LV_SHFT;
    }

    public String getPdcCheckNumber() {

        return pdcCheckNumber;
    }

    public void setPdcCheckNumber(String PDC_CHCK_NMBR) {

        this.pdcCheckNumber = PDC_CHCK_NMBR;
    }

    public String getPdcReferenceNumber() {

        return pdcReferenceNumber;
    }

    public void setPdcReferenceNumber(String PDC_RFRNC_NMBR) {

        this.pdcReferenceNumber = PDC_RFRNC_NMBR;
    }

    public Date getPdcDateReceived() {

        return pdcDateReceived;
    }

    public void setPdcDateReceived(Date PDC_DT_RCVD) {

        this.pdcDateReceived = PDC_DT_RCVD;
    }

    public Date getPdcMaturityDate() {

        return pdcMaturityDate;
    }

    public void setPdcMaturityDate(Date PDC_MTRTY_DT) {

        this.pdcMaturityDate = PDC_MTRTY_DT;
    }

    public String getPdcDescription() {

        return pdcDescription;
    }

    public void setPdcDescription(String PDC_DESC) {

        this.pdcDescription = PDC_DESC;
    }

    public byte getPdcCancelled() {

        return pdcCancelled;
    }

    public void setPdcCancelled(byte PDC_CNCLLD) {

        this.pdcCancelled = PDC_CNCLLD;
    }

    public double getPdcAmount() {

        return pdcAmount;
    }

    public void setPdcAmount(double PDC_AMNT) {

        this.pdcAmount = PDC_AMNT;
    }

    public double getPdcConversionRate() {

        return pdcConversionRate;
    }

    public void setPdcConversionRate(double PDC_CNVRSN_RT) {

        this.pdcConversionRate = PDC_CNVRSN_RT;
    }

    public Date getPdcConversionDate() {

        return pdcConversionDate;
    }

    public void setPdcConversionDate(Date PDC_CNVRSN_DT) {

        this.pdcConversionDate = PDC_CNVRSN_DT;
    }

    public String getPdcLvFreight() {

        return pdcLvFreight;
    }

    public void setPdcLvFreight(String PDC_LV_FRGHT) {

        this.pdcLvFreight = PDC_LV_FRGHT;
    }

    public String getPdcApprovalStatus() {

        return pdcApprovalStatus;
    }

    public void setPdcApprovalStatus(String PDC_APPRVL_STATUS) {

        this.pdcApprovalStatus = PDC_APPRVL_STATUS;
    }

    public byte getPdcPosted() {

        return pdcPosted;
    }

    public void setPdcPosted(byte PDC_PSTD) {

        this.pdcPosted = PDC_PSTD;
    }

    public String getPdcCreatedBy() {

        return pdcCreatedBy;
    }

    public void setPdcCreatedBy(String PDC_CRTD_BY) {

        this.pdcCreatedBy = PDC_CRTD_BY;
    }

    public Date getPdcDateCreated() {

        return pdcDateCreated;
    }

    public void setPdcDateCreated(Date PDC_DT_CRTD) {

        this.pdcDateCreated = PDC_DT_CRTD;
    }

    public String getPdcLastModifiedBy() {

        return pdcLastModifiedBy;
    }

    public void setPdcLastModifiedBy(String PDC_LST_MDFD_BY) {

        this.pdcLastModifiedBy = PDC_LST_MDFD_BY;
    }

    public Date getPdcDateLastModified() {

        return pdcDateLastModified;
    }

    public void setPdcDateLastModified(Date PDC_DT_LST_MDFD) {

        this.pdcDateLastModified = PDC_DT_LST_MDFD;
    }

    public String getPdcApprovedRejectedBy() {

        return pdcApprovedRejectedBy;
    }

    public void setPdcApprovedRejectedBy(String PDC_APPRVD_RJCTD_BY) {

        this.pdcApprovedRejectedBy = PDC_APPRVD_RJCTD_BY;
    }

    public Date getPdcDateApprovedRejected() {

        return pdcDateApprovedRejected;
    }

    public void setPdcDateApprovedRejected(Date PDC_DT_APPRVD_RJCTD) {

        this.pdcDateApprovedRejected = PDC_DT_APPRVD_RJCTD;
    }

    public String getPdcPostedBy() {

        return pdcPostedBy;
    }

    public void setPdcPostedBy(String PDC_PSTD_BY) {

        this.pdcPostedBy = PDC_PSTD_BY;
    }

    public Date getPdcDatePosted() {

        return pdcDatePosted;
    }

    public void setPdcDatePosted(Date PDC_DT_PSTD) {

        this.pdcDatePosted = PDC_DT_PSTD;
    }

    public byte getPdcPrinted() {

        return pdcPrinted;
    }

    public void setPdcPrinted(byte PDC_PRNTD) {

        this.pdcPrinted = PDC_PRNTD;
    }

    public Date getPdcEffectivityDate() {

        return pdcEffectivityDate;
    }

    public void setPdcEffectivityDate(Date PDC_EFFCTVTY_DT) {

        this.pdcEffectivityDate = PDC_EFFCTVTY_DT;
    }

    public Integer getPdcAdBranch() {

        return pdcAdBranch;
    }

    public void setPdcAdBranch(Integer PDC_AD_BRNCH) {

        this.pdcAdBranch = PDC_AD_BRNCH;
    }

    public Integer getPdcAdCompany() {

        return pdcAdCompany;
    }

    public void setPdcAdCompany(Integer PDC_AD_CMPNY) {

        this.pdcAdCompany = PDC_AD_CMPNY;
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

    public LocalArCustomer getArCustomer() {

        return arCustomer;
    }

    public void setArCustomer(LocalArCustomer arCustomer) {

        this.arCustomer = arCustomer;
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

    public void addArInvoiceLine(LocalArInvoiceLine entity) {

        try {
            entity.setArPdc(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArInvoiceLine(LocalArInvoiceLine entity) {

        try {
            entity.setArPdc(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArInvoiceLineItem(LocalArInvoiceLineItem entity) {

        try {
            entity.setArPdc(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArInvoiceLineItem(LocalArInvoiceLineItem entity) {

        try {
            entity.setArPdc(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArAppliedInvoice(LocalArAppliedInvoice entity) {

        try {
            entity.setArPdc(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArAppliedInvoice(LocalArAppliedInvoice entity) {

        try {
            entity.setArPdc(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}