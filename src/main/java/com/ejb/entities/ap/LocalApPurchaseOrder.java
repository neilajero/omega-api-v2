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

@Entity(name = "ApPurchaseOrder")
@Table(name = "AP_PRCHS_ORDR")
public class LocalApPurchaseOrder extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PO_CODE", nullable = false)
    private Integer poCode;

    @Column(name = "PO_RCVNG", columnDefinition = "TINYINT")
    private byte poReceiving;

    @Column(name = "PO_TYP", columnDefinition = "VARCHAR")
    private String poType;

    @Column(name = "PO_DT", columnDefinition = "DATETIME")
    private Date poDate;

    @Column(name = "PO_DLVRY_PRD", columnDefinition = "DATETIME")
    private Date poDeliveryPeriod;

    @Column(name = "PO_DCMNT_NMBR", columnDefinition = "VARCHAR")
    private String poDocumentNumber;

    @Column(name = "PO_SHPMNT_NMBR", columnDefinition = "VARCHAR")
    private String poShipmentNumber;

    @Column(name = "PO_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String poReferenceNumber;

    @Column(name = "PO_RCV_PO_NMBR", columnDefinition = "VARCHAR")
    private String poRcvPoNumber;

    @Column(name = "PO_DESC", columnDefinition = "VARCHAR")
    private String poDescription;

    @Column(name = "PO_BLL_TO", columnDefinition = "VARCHAR")
    private String poBillTo;

    @Column(name = "PO_SHP_TO", columnDefinition = "VARCHAR")
    private String poShipTo;

    @Column(name = "PO_CNVRSN_DT", columnDefinition = "DATETIME")
    private Date poConversionDate;

    @Column(name = "PO_CNVRSN_RT", columnDefinition = "DOUBLE")
    private double poConversionRate = 0;

    @Column(name = "PO_TTL_AMNT", columnDefinition = "DOUBLE")
    private double poTotalAmount = 0;

    @Column(name = "PO_PRNTD", columnDefinition = "TINYINT")
    private byte poPrinted;

    @Column(name = "PO_VD", columnDefinition = "TINYINT")
    private byte poVoid;

    @Column(name = "PO_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String poApprovalStatus;

    @Column(name = "PO_PSTD", columnDefinition = "TINYINT")
    private byte poPosted;

    @Column(name = "PO_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String poReasonForRejection;

    @Column(name = "PO_DPRTMNT", columnDefinition = "VARCHAR")
    private String poDepartment;

    @Column(name = "PO_CRTD_BY", columnDefinition = "VARCHAR")
    private String poCreatedBy;

    @Column(name = "PO_DT_CRTD", columnDefinition = "DATETIME")
    private Date poDateCreated;

    @Column(name = "PO_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String poLastModifiedBy;

    @Column(name = "PO_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date poDateLastModified;

    @Column(name = "PO_APPRVD_RJCTD_BY", columnDefinition = "VARCHAR")
    private String poApprovedRejectedBy;

    @Column(name = "PO_DT_APPRVD_RJCTD", columnDefinition = "DATETIME")
    private Date poDateApprovedRejected;

    @Column(name = "PO_PSTD_BY", columnDefinition = "VARCHAR")
    private String poPostedBy;

    @Column(name = "PO_DT_PSTD", columnDefinition = "DATETIME")
    private Date poDatePosted;

    @Column(name = "PO_LCK", columnDefinition = "TINYINT")
    private byte poLock;

    @Column(name = "PO_FRGHT", columnDefinition = "DOUBLE")
    private double poFreight = 0;

    @Column(name = "PO_DTS", columnDefinition = "DOUBLE")
    private double poDuties = 0;

    @Column(name = "PO_ENT_FEE", columnDefinition = "DOUBLE")
    private double poEntryFee = 0;

    @Column(name = "PO_STRG", columnDefinition = "DOUBLE")
    private double poStorage = 0;

    @Column(name = "PO_WHFG_HNDLNG", columnDefinition = "DOUBLE")
    private double poWharfageHandling = 0;

    @Column(name = "PO_AD_BRNCH", columnDefinition = "INT")
    private Integer poAdBranch;

    @Column(name = "PO_AD_CMPNY", columnDefinition = "INT")
    private Integer poAdCompany;

    @JoinColumn(name = "AD_PAYMENT_TERM", referencedColumnName = "PYT_CODE")
    @ManyToOne
    private LocalAdPaymentTerm adPaymentTerm;

    @JoinColumn(name = "AP_SUPPLIER", referencedColumnName = "SPL_CODE")
    @ManyToOne
    private LocalApSupplier apSupplier;

    @JoinColumn(name = "AP_TAX_CODE", referencedColumnName = "AP_TC_CODE")
    @ManyToOne
    private LocalApTaxCode apTaxCode;

    @JoinColumn(name = "AP_VOUCHER_BATCH", referencedColumnName = "VB_CODE")
    @ManyToOne
    private LocalApVoucherBatch apVoucherBatch;

    @JoinColumn(name = "GL_FUNCTIONAL_CURRENCY", referencedColumnName = "FC_CODE")
    @ManyToOne
    private LocalGlFunctionalCurrency glFunctionalCurrency;

    @OneToMany(mappedBy = "apPurchaseOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalApPurchaseOrderLine> apPurchaseOrderLines;

    @OneToMany(mappedBy = "apPurchaseOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalApDistributionRecord> apDistributionRecords;

    public Integer getPoCode() {

        return poCode;
    }

    public void setPoCode(Integer PO_CODE) {

        this.poCode = PO_CODE;
    }

    public byte getPoReceiving() {

        return poReceiving;
    }

    public void setPoReceiving(byte PO_RCVNG) {

        this.poReceiving = PO_RCVNG;
    }

    public String getPoType() {

        return poType;
    }

    public void setPoType(String PO_TYP) {

        this.poType = PO_TYP;
    }

    public Date getPoDate() {

        return poDate;
    }

    public void setPoDate(Date PO_DT) {

        this.poDate = PO_DT;
    }

    public Date getPoDeliveryPeriod() {

        return poDeliveryPeriod;
    }

    public void setPoDeliveryPeriod(Date PO_DLVRY_PRD) {

        this.poDeliveryPeriod = PO_DLVRY_PRD;
    }

    public String getPoDocumentNumber() {

        return poDocumentNumber;
    }

    public void setPoDocumentNumber(String PO_DCMNT_NMBR) {

        this.poDocumentNumber = PO_DCMNT_NMBR;
    }

    public String getPoShipmentNumber() {

        return poShipmentNumber;
    }

    public void setPoShipmentNumber(String PO_SHPMNT_NMBR) {

        this.poShipmentNumber = PO_SHPMNT_NMBR;
    }

    public String getPoReferenceNumber() {

        return poReferenceNumber;
    }

    public void setPoReferenceNumber(String PO_RFRNC_NMBR) {

        this.poReferenceNumber = PO_RFRNC_NMBR;
    }

    public String getPoRcvPoNumber() {

        return poRcvPoNumber;
    }

    public void setPoRcvPoNumber(String PO_RCV_PO_NMBR) {

        this.poRcvPoNumber = PO_RCV_PO_NMBR;
    }

    public String getPoDescription() {

        return poDescription;
    }

    public void setPoDescription(String PO_DESC) {

        this.poDescription = PO_DESC;
    }

    public String getPoBillTo() {

        return poBillTo;
    }

    public void setPoBillTo(String PO_BLL_TO) {

        this.poBillTo = PO_BLL_TO;
    }

    public String getPoShipTo() {

        return poShipTo;
    }

    public void setPoShipTo(String PO_SHP_TO) {

        this.poShipTo = PO_SHP_TO;
    }

    public Date getPoConversionDate() {

        return poConversionDate;
    }

    public void setPoConversionDate(Date PO_CNVRSN_DT) {

        this.poConversionDate = PO_CNVRSN_DT;
    }

    public double getPoConversionRate() {

        return poConversionRate;
    }

    public void setPoConversionRate(double PO_CNVRSN_RT) {

        this.poConversionRate = PO_CNVRSN_RT;
    }

    public double getPoTotalAmount() {

        return poTotalAmount;
    }

    public void setPoTotalAmount(double PO_TTL_AMNT) {

        this.poTotalAmount = PO_TTL_AMNT;
    }

    public byte getPoPrinted() {

        return poPrinted;
    }

    public void setPoPrinted(byte PO_PRNTD) {

        this.poPrinted = PO_PRNTD;
    }

    public byte getPoVoid() {

        return poVoid;
    }

    public void setPoVoid(byte PO_VD) {

        this.poVoid = PO_VD;
    }

    public String getPoApprovalStatus() {

        return poApprovalStatus;
    }

    public void setPoApprovalStatus(String PO_APPRVL_STATUS) {

        this.poApprovalStatus = PO_APPRVL_STATUS;
    }

    public byte getPoPosted() {

        return poPosted;
    }

    public void setPoPosted(byte PO_PSTD) {

        this.poPosted = PO_PSTD;
    }

    public String getPoReasonForRejection() {

        return poReasonForRejection;
    }

    public void setPoReasonForRejection(String PO_RSN_FR_RJCTN) {

        this.poReasonForRejection = PO_RSN_FR_RJCTN;
    }

    public String getPoDepartment() {

        return poDepartment;
    }

    public void setPoDepartment(String PO_DPRTMNT) {

        this.poDepartment = PO_DPRTMNT;
    }

    public String getPoCreatedBy() {

        return poCreatedBy;
    }

    public void setPoCreatedBy(String PO_CRTD_BY) {

        this.poCreatedBy = PO_CRTD_BY;
    }

    public Date getPoDateCreated() {

        return poDateCreated;
    }

    public void setPoDateCreated(Date PO_DT_CRTD) {

        this.poDateCreated = PO_DT_CRTD;
    }

    public String getPoLastModifiedBy() {

        return poLastModifiedBy;
    }

    public void setPoLastModifiedBy(String PO_LST_MDFD_BY) {

        this.poLastModifiedBy = PO_LST_MDFD_BY;
    }

    public Date getPoDateLastModified() {

        return poDateLastModified;
    }

    public void setPoDateLastModified(Date PO_DT_LST_MDFD) {

        this.poDateLastModified = PO_DT_LST_MDFD;
    }

    public String getPoApprovedRejectedBy() {

        return poApprovedRejectedBy;
    }

    public void setPoApprovedRejectedBy(String PO_APPRVD_RJCTD_BY) {

        this.poApprovedRejectedBy = PO_APPRVD_RJCTD_BY;
    }

    public Date getPoDateApprovedRejected() {

        return poDateApprovedRejected;
    }

    public void setPoDateApprovedRejected(Date PO_DT_APPRVD_RJCTD) {

        this.poDateApprovedRejected = PO_DT_APPRVD_RJCTD;
    }

    public String getPoPostedBy() {

        return poPostedBy;
    }

    public void setPoPostedBy(String PO_PSTD_BY) {

        this.poPostedBy = PO_PSTD_BY;
    }

    public Date getPoDatePosted() {

        return poDatePosted;
    }

    public void setPoDatePosted(Date PO_DT_PSTD) {

        this.poDatePosted = PO_DT_PSTD;
    }

    public byte getPoLock() {

        return poLock;
    }

    public void setPoLock(byte PO_LCK) {

        this.poLock = PO_LCK;
    }

    public double getPoFreight() {

        return poFreight;
    }

    public void setPoFreight(double PO_FRGHT) {

        this.poFreight = PO_FRGHT;
    }

    public double getPoDuties() {

        return poDuties;
    }

    public void setPoDuties(double PO_DTS) {

        this.poDuties = PO_DTS;
    }

    public double getPoEntryFee() {

        return poEntryFee;
    }

    public void setPoEntryFee(double PO_ENT_FEE) {

        this.poEntryFee = PO_ENT_FEE;
    }

    public double getPoStorage() {

        return poStorage;
    }

    public void setPoStorage(double PO_STRG) {

        this.poStorage = PO_STRG;
    }

    public double getPoWharfageHandling() {

        return poWharfageHandling;
    }

    public void setPoWharfageHandling(double PO_WHFG_HNDLNG) {

        this.poWharfageHandling = PO_WHFG_HNDLNG;
    }

    public Integer getPoAdBranch() {

        return poAdBranch;
    }

    public void setPoAdBranch(Integer PO_AD_BRNCH) {

        this.poAdBranch = PO_AD_BRNCH;
    }

    public Integer getPoAdCompany() {

        return poAdCompany;
    }

    public void setPoAdCompany(Integer PO_AD_CMPNY) {

        this.poAdCompany = PO_AD_CMPNY;
    }

    public LocalAdPaymentTerm getAdPaymentTerm() {

        return adPaymentTerm;
    }

    public void setAdPaymentTerm(LocalAdPaymentTerm adPaymentTerm) {

        this.adPaymentTerm = adPaymentTerm;
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

    public LocalGlFunctionalCurrency getGlFunctionalCurrency() {

        return glFunctionalCurrency;
    }

    public void setGlFunctionalCurrency(LocalGlFunctionalCurrency glFunctionalCurrency) {

        this.glFunctionalCurrency = glFunctionalCurrency;
    }

    @XmlTransient
    public List getApPurchaseOrderLines() {

        return apPurchaseOrderLines;
    }

    public void setApPurchaseOrderLines(List apPurchaseOrderLines) {

        this.apPurchaseOrderLines = apPurchaseOrderLines;
    }

    @XmlTransient
    public List getApDistributionRecords() {

        return apDistributionRecords;
    }

    public void setApDistributionRecords(List apDistributionRecords) {

        this.apDistributionRecords = apDistributionRecords;
    }

    public void addApPurchaseOrderLine(LocalApPurchaseOrderLine entity) {

        try {
            entity.setApPurchaseOrder(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApPurchaseOrderLine(LocalApPurchaseOrderLine entity) {

        try {
            entity.setApPurchaseOrder(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApDistributionRecord(LocalApDistributionRecord entity) {

        try {
            entity.setApPurchaseOrder(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApDistributionRecord(LocalApDistributionRecord entity) {

        try {
            entity.setApPurchaseOrder(null);
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