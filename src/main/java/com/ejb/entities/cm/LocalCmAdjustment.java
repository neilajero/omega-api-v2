package com.ejb.entities.cm;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ar.LocalArAppliedCredit;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.entities.ar.LocalArJobOrder;
import com.ejb.entities.ar.LocalArSalesOrder;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "CmAdjustment")
@Table(name = "CM_ADJSTMNT")
public class LocalCmAdjustment extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CM_ADJ_CODE", nullable = false)
    private Integer adjCode;

    @Column(name = "ADJ_TYP", columnDefinition = "VARCHAR")
    private String adjType;

    @Column(name = "ADJ_DT", columnDefinition = "DATETIME")
    private Date adjDate;

    @Column(name = "ADJ_DCMNT_NMBR", columnDefinition = "VARCHAR")
    private String adjDocumentNumber;

    @Column(name = "ADJ_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String adjReferenceNumber;

    @Column(name = "ADJ_CHCK_NMBR", columnDefinition = "VARCHAR")
    private String adjCheckNumber;

    @Column(name = "ADJ_AMNT", columnDefinition = "DOUBLE")
    private double adjAmount = 0;

    @Column(name = "ADJ_AMNT_APPLD", columnDefinition = "DOUBLE")
    private double adjAmountApplied = 0;

    @Column(name = "ADJ_CNVRSN_DT", columnDefinition = "DATETIME")
    private Date adjConversionDate;

    @Column(name = "ADJ_CNVRSN_RT", columnDefinition = "DOUBLE")
    private double adjConversionRate = 0;

    @Column(name = "ADJ_MM", columnDefinition = "VARCHAR")
    private String adjMemo;

    @Column(name = "ADJ_VD_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String adjVoidApprovalStatus;

    @Column(name = "ADJ_VD_PSTD", columnDefinition = "TINYINT")
    private byte adjVoidPosted;

    @Column(name = "ADJ_VOID", columnDefinition = "TINYINT")
    private byte adjVoid;

    @Column(name = "ADJ_RFND", columnDefinition = "TINYINT")
    private byte adjRefund;

    @Column(name = "ADJ_RFND_AMNT", columnDefinition = "DOUBLE")
    private double adjRefundAmount = 0;

    @Column(name = "ADJ_RFND_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String adjRefundReferenceNumber;

    @Column(name = "ADJ_RCNCLD", columnDefinition = "TINYINT")
    private byte adjReconciled;

    @Column(name = "ADJ_DT_RCNCLD", columnDefinition = "DATETIME")
    private Date adjDateReconciled;

    @Column(name = "ADJ_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String adjApprovalStatus;

    @Column(name = "ADJ_PSTD", columnDefinition = "TINYINT")
    private byte adjPosted;

    @Column(name = "ADJ_CRTD_BY", columnDefinition = "VARCHAR")
    private String adjCreatedBy;

    @Column(name = "ADJ_DT_CRTD", columnDefinition = "DATETIME")
    private Date adjDateCreated;

    @Column(name = "ADJ_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String adjLastModifiedBy;

    @Column(name = "ADJ_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date adjDateLastModified;

    @Column(name = "ADJ_APPRVD_RJCTD_BY", columnDefinition = "VARCHAR")
    private String adjApprovedRejectedBy;

    @Column(name = "ADJ_DT_APPRVD_RJCTD", columnDefinition = "DATETIME")
    private Date adjDateApprovedRejected;

    @Column(name = "ADJ_PSTD_BY", columnDefinition = "VARCHAR")
    private String adjPostedBy;

    @Column(name = "ADJ_DT_PSTD", columnDefinition = "DATETIME")
    private Date adjDatePosted;

    @Column(name = "ADJ_PRNTD", columnDefinition = "TINYINT")
    private byte adjPrinted;

    @Column(name = "ADJ_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String adjReasonForRejection;

    @Column(name = "ADJ_AD_BRNCH", columnDefinition = "INT")
    private Integer adjAdBranch;

    @Column(name = "ADJ_AD_CMPNY", columnDefinition = "INT")
    private Integer adjAdCompany;

    @JoinColumn(name = "AD_BANK_ACCOUNT", referencedColumnName = "BA_CODE")
    @ManyToOne
    private LocalAdBankAccount adBankAccount;

    @JoinColumn(name = "AR_CUSTOMER", referencedColumnName = "AR_CST_CODE")
    @ManyToOne
    private LocalArCustomer arCustomer;

    @JoinColumn(name = "AR_JOB_ORDER", referencedColumnName = "JO_CODE")
    @ManyToOne
    private LocalArJobOrder arJobOrder;

    @JoinColumn(name = "AR_SALES_ORDER", referencedColumnName = "SO_CODE")
    @ManyToOne
    private LocalArSalesOrder arSalesOrder;

    @OneToMany(mappedBy = "cmAdjustment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalCmDistributionRecord> cmDistributionRecords;

    @OneToMany(mappedBy = "cmAdjustment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArAppliedCredit> arAppliedCredits;

    public Integer getAdjCode() {

        return adjCode;
    }

    public void setAdjCode(Integer CM_ADJ_CODE) {

        this.adjCode = CM_ADJ_CODE;
    }

    public String getAdjType() {

        return adjType;
    }

    public void setAdjType(String ADJ_TYP) {

        this.adjType = ADJ_TYP;
    }

    public Date getAdjDate() {

        return adjDate;
    }

    public void setAdjDate(Date ADJ_DT) {

        this.adjDate = ADJ_DT;
    }

    public String getAdjDocumentNumber() {

        return adjDocumentNumber;
    }

    public void setAdjDocumentNumber(String ADJ_DCMNT_NMBR) {

        this.adjDocumentNumber = ADJ_DCMNT_NMBR;
    }

    public String getAdjReferenceNumber() {

        return adjReferenceNumber;
    }

    public void setAdjReferenceNumber(String ADJ_RFRNC_NMBR) {

        this.adjReferenceNumber = ADJ_RFRNC_NMBR;
    }

    public String getAdjCheckNumber() {

        return adjCheckNumber;
    }

    public void setAdjCheckNumber(String ADJ_CHCK_NMBR) {

        this.adjCheckNumber = ADJ_CHCK_NMBR;
    }

    public double getAdjAmount() {

        return adjAmount;
    }

    public void setAdjAmount(double ADJ_AMNT) {

        this.adjAmount = ADJ_AMNT;
    }

    public double getAdjAmountApplied() {

        return adjAmountApplied;
    }

    public void setAdjAmountApplied(double ADJ_AMNT_APPLD) {

        this.adjAmountApplied = ADJ_AMNT_APPLD;
    }

    public Date getAdjConversionDate() {

        return adjConversionDate;
    }

    public void setAdjConversionDate(Date ADJ_CNVRSN_DT) {

        this.adjConversionDate = ADJ_CNVRSN_DT;
    }

    public double getAdjConversionRate() {

        return adjConversionRate;
    }

    public void setAdjConversionRate(double ADJ_CNVRSN_RT) {

        this.adjConversionRate = ADJ_CNVRSN_RT;
    }

    public String getAdjMemo() {

        return adjMemo;
    }

    public void setAdjMemo(String ADJ_MM) {

        this.adjMemo = ADJ_MM;
    }

    public String getAdjVoidApprovalStatus() {

        return adjVoidApprovalStatus;
    }

    public void setAdjVoidApprovalStatus(String ADJ_VD_APPRVL_STATUS) {

        this.adjVoidApprovalStatus = ADJ_VD_APPRVL_STATUS;
    }

    public byte getAdjVoidPosted() {

        return adjVoidPosted;
    }

    public void setAdjVoidPosted(byte ADJ_VD_PSTD) {

        this.adjVoidPosted = ADJ_VD_PSTD;
    }

    public byte getAdjVoid() {

        return adjVoid;
    }

    public void setAdjVoid(byte ADJ_VOID) {

        this.adjVoid = ADJ_VOID;
    }

    public byte getAdjRefund() {

        return adjRefund;
    }

    public void setAdjRefund(byte ADJ_RFND) {

        this.adjRefund = ADJ_RFND;
    }

    public double getAdjRefundAmount() {

        return adjRefundAmount;
    }

    public void setAdjRefundAmount(double ADJ_RFND_AMNT) {

        this.adjRefundAmount = ADJ_RFND_AMNT;
    }

    public String getAdjRefundReferenceNumber() {

        return adjRefundReferenceNumber;
    }

    public void setAdjRefundReferenceNumber(String ADJ_RFND_RFRNC_NMBR) {

        this.adjRefundReferenceNumber = ADJ_RFND_RFRNC_NMBR;
    }

    public byte getAdjReconciled() {

        return adjReconciled;
    }

    public void setAdjReconciled(byte ADJ_RCNCLD) {

        this.adjReconciled = ADJ_RCNCLD;
    }

    public Date getAdjDateReconciled() {

        return adjDateReconciled;
    }

    public void setAdjDateReconciled(Date ADJ_DT_RCNCLD) {

        this.adjDateReconciled = ADJ_DT_RCNCLD;
    }

    public String getAdjApprovalStatus() {

        return adjApprovalStatus;
    }

    public void setAdjApprovalStatus(String ADJ_APPRVL_STATUS) {

        this.adjApprovalStatus = ADJ_APPRVL_STATUS;
    }

    public byte getAdjPosted() {

        return adjPosted;
    }

    public void setAdjPosted(byte ADJ_PSTD) {

        this.adjPosted = ADJ_PSTD;
    }

    public String getAdjCreatedBy() {

        return adjCreatedBy;
    }

    public void setAdjCreatedBy(String ADJ_CRTD_BY) {

        this.adjCreatedBy = ADJ_CRTD_BY;
    }

    public Date getAdjDateCreated() {

        return adjDateCreated;
    }

    public void setAdjDateCreated(Date ADJ_DT_CRTD) {

        this.adjDateCreated = ADJ_DT_CRTD;
    }

    public String getAdjLastModifiedBy() {

        return adjLastModifiedBy;
    }

    public void setAdjLastModifiedBy(String ADJ_LST_MDFD_BY) {

        this.adjLastModifiedBy = ADJ_LST_MDFD_BY;
    }

    public Date getAdjDateLastModified() {

        return adjDateLastModified;
    }

    public void setAdjDateLastModified(Date ADJ_DT_LST_MDFD) {

        this.adjDateLastModified = ADJ_DT_LST_MDFD;
    }

    public String getAdjApprovedRejectedBy() {

        return adjApprovedRejectedBy;
    }

    public void setAdjApprovedRejectedBy(String ADJ_APPRVD_RJCTD_BY) {

        this.adjApprovedRejectedBy = ADJ_APPRVD_RJCTD_BY;
    }

    public Date getAdjDateApprovedRejected() {

        return adjDateApprovedRejected;
    }

    public void setAdjDateApprovedRejected(Date ADJ_DT_APPRVD_RJCTD) {

        this.adjDateApprovedRejected = ADJ_DT_APPRVD_RJCTD;
    }

    public String getAdjPostedBy() {

        return adjPostedBy;
    }

    public void setAdjPostedBy(String ADJ_PSTD_BY) {

        this.adjPostedBy = ADJ_PSTD_BY;
    }

    public Date getAdjDatePosted() {

        return adjDatePosted;
    }

    public void setAdjDatePosted(Date ADJ_DT_PSTD) {

        this.adjDatePosted = ADJ_DT_PSTD;
    }

    public byte getAdjPrinted() {

        return adjPrinted;
    }

    public void setAdjPrinted(byte ADJ_PRNTD) {

        this.adjPrinted = ADJ_PRNTD;
    }

    public String getAdjReasonForRejection() {

        return adjReasonForRejection;
    }

    public void setAdjReasonForRejection(String ADJ_RSN_FR_RJCTN) {

        this.adjReasonForRejection = ADJ_RSN_FR_RJCTN;
    }

    public Integer getAdjAdBranch() {

        return adjAdBranch;
    }

    public void setAdjAdBranch(Integer ADJ_AD_BRNCH) {

        this.adjAdBranch = ADJ_AD_BRNCH;
    }

    public Integer getAdjAdCompany() {

        return adjAdCompany;
    }

    public void setAdjAdCompany(Integer ADJ_AD_CMPNY) {

        this.adjAdCompany = ADJ_AD_CMPNY;
    }

    public LocalAdBankAccount getAdBankAccount() {

        return adBankAccount;
    }

    public void setAdBankAccount(LocalAdBankAccount adBankAccount) {

        this.adBankAccount = adBankAccount;
    }

    public LocalArCustomer getArCustomer() {

        return arCustomer;
    }

    public void setArCustomer(LocalArCustomer arCustomer) {

        this.arCustomer = arCustomer;
    }

    public LocalArJobOrder getArJobOrder() {

        return arJobOrder;
    }

    public void setArJobOrder(LocalArJobOrder arJobOrder) {

        this.arJobOrder = arJobOrder;
    }

    public LocalArSalesOrder getArSalesOrder() {

        return arSalesOrder;
    }

    public void setArSalesOrder(LocalArSalesOrder arSalesOrder) {

        this.arSalesOrder = arSalesOrder;
    }

    @XmlTransient
    public List getCmDistributionRecords() {

        return cmDistributionRecords;
    }

    public void setCmDistributionRecords(List cmDistributionRecords) {

        this.cmDistributionRecords = cmDistributionRecords;
    }

    @XmlTransient
    public List getArAppliedCredits() {

        return arAppliedCredits;
    }

    public void setArAppliedCredits(List arAppliedCredits) {

        this.arAppliedCredits = arAppliedCredits;
    }

    public void addArAppliedCredit(LocalArAppliedCredit entity) {

        try {
            entity.setCmAdjustment(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArAppliedCredit(LocalArAppliedCredit entity) {

        try {
            entity.setCmAdjustment(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addCmDistributionRecord(LocalCmDistributionRecord entity) {

        try {
            entity.setCmAdjustment(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropCmDistributionRecord(LocalCmDistributionRecord entity) {

        try {
            entity.setCmAdjustment(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public short getCmDrNextLine() {

        try {
            List lists = getCmDistributionRecords();
            return (short) (lists.size() + 1);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}