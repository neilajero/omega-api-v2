package com.ejb.entities.cm;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "CmFundTransfer")
@Table(name = "CM_FND_TRNSFR")
public class LocalCmFundTransfer extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FT_CODE", nullable = false)
    private Integer ftCode;

    @Column(name = "FT_DT")
    private Date ftDate;

    @Column(name = "FT_DCMNT_NMBR", columnDefinition = "VARCHAR")
    private String ftDocumentNumber;

    @Column(name = "FT_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String ftReferenceNumber;

    @Column(name = "FT_MM", columnDefinition = "VARCHAR")
    private String ftMemo;

    @Column(name = "FT_AD_BA_ACCNT_FRM", columnDefinition = "INT")
    private Integer ftAdBaAccountFrom;

    @Column(name = "FT_AD_BA_ACCNT_TO", columnDefinition = "INT")
    private Integer ftAdBaAccountTo;

    @Column(name = "FT_AMNT", columnDefinition = "DOUBLE")
    private double ftAmount = 0;

    @Column(name = "FT_CNVRSN_DT", columnDefinition = "DATETIME")
    private Date ftConversionDate;

    @Column(name = "FT_CNVRSN_RT_FRM", columnDefinition = "DOUBLE")
    private double ftConversionRateFrom = 0;

    @Column(name = "FT_ACCNT_FRM_RCNCLD", columnDefinition = "TINYINT")
    private byte ftAccountFromReconciled;

    @Column(name = "FT_ACCNT_TO_RCNCLD", columnDefinition = "TINYINT")
    private byte ftAccountToReconciled;

    @Column(name = "FT_VOID", columnDefinition = "TINYINT")
    private byte ftVoid;

    @Column(name = "FT_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String ftApprovalStatus;

    @Column(name = "FT_PSTD", columnDefinition = "TINYINT")
    private byte ftPosted;

    @Column(name = "FT_CRTD_BY", columnDefinition = "VARCHAR")
    private String ftCreatedBy;

    @Column(name = "FT_DT_CRTD", columnDefinition = "DATETIME")
    private Date ftDateCreated;

    @Column(name = "FT_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String ftLastModifiedBy;

    @Column(name = "FT_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date ftDateLastModified;

    @Column(name = "FT_APPRVD_RJCTD_BY", columnDefinition = "VARCHAR")
    private String ftApprovedRejectedBy;

    @Column(name = "FT_DT_APPRVD_RJCTD", columnDefinition = "DATETIME")
    private Date ftDateApprovedRejected;

    @Column(name = "FT_PSTD_BY", columnDefinition = "VARCHAR")
    private String ftPostedBy;

    @Column(name = "FT_DT_PSTD", columnDefinition = "DATETIME")
    private Date ftDatePosted;

    @Column(name = "FT_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String ftReasonForRejection;

    @Column(name = "FT_TYP", columnDefinition = "VARCHAR")
    private String ftType;

    @Column(name = "FT_CNVRSN_RT_TO", columnDefinition = "DOUBLE")
    private double ftConversionRateTo = 0;

    @Column(name = "FT_ACCNT_FRM_DT_RCNCLD", columnDefinition = "DATETIME")
    private Date ftAccountFromDateReconciled;

    @Column(name = "FT_ACCNT_TO_DT_RCNCLD", columnDefinition = "DATETIME")
    private Date ftAccountToDateReconciled;

    @Column(name = "FT_AD_BRNCH", columnDefinition = "INT")
    private Integer ftAdBranch;

    @Column(name = "FT_AD_CMPNY", columnDefinition = "INT")
    private Integer ftAdCompany;

    @OneToMany(mappedBy = "cmFundTransfer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalCmDistributionRecord> cmDistributionRecords;

    @OneToMany(mappedBy = "cmFundTransfer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalCmFundTransferReceipt> cmFundTransferReceipts;

    public Integer getFtCode() {

        return ftCode;
    }

    public void setFtCode(Integer FT_CODE) {

        this.ftCode = FT_CODE;
    }

    public Date getFtDate() {

        return ftDate;
    }

    public void setFtDate(Date FT_DT) {

        this.ftDate = FT_DT;
    }

    public String getFtDocumentNumber() {

        return ftDocumentNumber;
    }

    public void setFtDocumentNumber(String FT_DCMNT_NMBR) {

        this.ftDocumentNumber = FT_DCMNT_NMBR;
    }

    public String getFtReferenceNumber() {

        return ftReferenceNumber;
    }

    public void setFtReferenceNumber(String FT_RFRNC_NMBR) {

        this.ftReferenceNumber = FT_RFRNC_NMBR;
    }

    public String getFtMemo() {

        return ftMemo;
    }

    public void setFtMemo(String FT_MM) {

        this.ftMemo = FT_MM;
    }

    public Integer getFtAdBaAccountFrom() {

        return ftAdBaAccountFrom;
    }

    public void setFtAdBaAccountFrom(Integer FT_AD_BA_ACCNT_FRM) {

        this.ftAdBaAccountFrom = FT_AD_BA_ACCNT_FRM;
    }

    public Integer getFtAdBaAccountTo() {

        return ftAdBaAccountTo;
    }

    public void setFtAdBaAccountTo(Integer FT_AD_BA_ACCNT_TO) {

        this.ftAdBaAccountTo = FT_AD_BA_ACCNT_TO;
    }

    public double getFtAmount() {

        return ftAmount;
    }

    public void setFtAmount(double FT_AMNT) {

        this.ftAmount = FT_AMNT;
    }

    public Date getFtConversionDate() {

        return ftConversionDate;
    }

    public void setFtConversionDate(Date FT_CNVRSN_DT) {

        this.ftConversionDate = FT_CNVRSN_DT;
    }

    public double getFtConversionRateFrom() {

        return ftConversionRateFrom;
    }

    public void setFtConversionRateFrom(double FT_CNVRSN_RT_FRM) {

        this.ftConversionRateFrom = FT_CNVRSN_RT_FRM;
    }

    public byte getFtAccountFromReconciled() {

        return ftAccountFromReconciled;
    }

    public void setFtAccountFromReconciled(byte FT_ACCNT_FRM_RCNCLD) {

        this.ftAccountFromReconciled = FT_ACCNT_FRM_RCNCLD;
    }

    public byte getFtAccountToReconciled() {

        return ftAccountToReconciled;
    }

    public void setFtAccountToReconciled(byte FT_ACCNT_TO_RCNCLD) {

        this.ftAccountToReconciled = FT_ACCNT_TO_RCNCLD;
    }

    public byte getFtVoid() {

        return ftVoid;
    }

    public void setFtVoid(byte FT_VOID) {

        this.ftVoid = FT_VOID;
    }

    public String getFtApprovalStatus() {

        return ftApprovalStatus;
    }

    public void setFtApprovalStatus(String FT_APPRVL_STATUS) {

        this.ftApprovalStatus = FT_APPRVL_STATUS;
    }

    public byte getFtPosted() {

        return ftPosted;
    }

    public void setFtPosted(byte FT_PSTD) {

        this.ftPosted = FT_PSTD;
    }

    public String getFtCreatedBy() {

        return ftCreatedBy;
    }

    public void setFtCreatedBy(String FT_CRTD_BY) {

        this.ftCreatedBy = FT_CRTD_BY;
    }

    public Date getFtDateCreated() {

        return ftDateCreated;
    }

    public void setFtDateCreated(Date FT_DT_CRTD) {

        this.ftDateCreated = FT_DT_CRTD;
    }

    public String getFtLastModifiedBy() {

        return ftLastModifiedBy;
    }

    public void setFtLastModifiedBy(String FT_LST_MDFD_BY) {

        this.ftLastModifiedBy = FT_LST_MDFD_BY;
    }

    public Date getFtDateLastModified() {

        return ftDateLastModified;
    }

    public void setFtDateLastModified(Date FT_DT_LST_MDFD) {

        this.ftDateLastModified = FT_DT_LST_MDFD;
    }

    public String getFtApprovedRejectedBy() {

        return ftApprovedRejectedBy;
    }

    public void setFtApprovedRejectedBy(String FT_APPRVD_RJCTD_BY) {

        this.ftApprovedRejectedBy = FT_APPRVD_RJCTD_BY;
    }

    public Date getFtDateApprovedRejected() {

        return ftDateApprovedRejected;
    }

    public void setFtDateApprovedRejected(Date FT_DT_APPRVD_RJCTD) {

        this.ftDateApprovedRejected = FT_DT_APPRVD_RJCTD;
    }

    public String getFtPostedBy() {

        return ftPostedBy;
    }

    public void setFtPostedBy(String FT_PSTD_BY) {

        this.ftPostedBy = FT_PSTD_BY;
    }

    public Date getFtDatePosted() {

        return ftDatePosted;
    }

    public void setFtDatePosted(Date FT_DT_PSTD) {

        this.ftDatePosted = FT_DT_PSTD;
    }

    public String getFtReasonForRejection() {

        return ftReasonForRejection;
    }

    public void setFtReasonForRejection(String FT_RSN_FR_RJCTN) {

        this.ftReasonForRejection = FT_RSN_FR_RJCTN;
    }

    public String getFtType() {

        return ftType;
    }

    public void setFtType(String FT_TYP) {

        this.ftType = FT_TYP;
    }

    public double getFtConversionRateTo() {

        return ftConversionRateTo;
    }

    public void setFtConversionRateTo(double FT_CNVRSN_RT_TO) {

        this.ftConversionRateTo = FT_CNVRSN_RT_TO;
    }

    public Date getFtAccountFromDateReconciled() {

        return ftAccountFromDateReconciled;
    }

    public void setFtAccountFromDateReconciled(Date FT_ACCNT_FRM_DT_RCNCLD) {

        this.ftAccountFromDateReconciled = FT_ACCNT_FRM_DT_RCNCLD;
    }

    public Date getFtAccountToDateReconciled() {

        return ftAccountToDateReconciled;
    }

    public void setFtAccountToDateReconciled(Date FT_ACCNT_TO_DT_RCNCLD) {

        this.ftAccountToDateReconciled = FT_ACCNT_TO_DT_RCNCLD;
    }

    public Integer getFtAdBranch() {

        return ftAdBranch;
    }

    public void setFtAdBranch(Integer FT_AD_BRNCH) {

        this.ftAdBranch = FT_AD_BRNCH;
    }

    public Integer getFtAdCompany() {

        return ftAdCompany;
    }

    public void setFtAdCompany(Integer FT_AD_CMPNY) {

        this.ftAdCompany = FT_AD_CMPNY;
    }

    @XmlTransient
    public List getCmDistributionRecords() {

        return cmDistributionRecords;
    }

    public void setCmDistributionRecords(List cmDistributionRecords) {

        this.cmDistributionRecords = cmDistributionRecords;
    }

    @XmlTransient
    public List getCmFundTransferReceipts() {

        return cmFundTransferReceipts;
    }

    public void setCmFundTransferReceipts(List cmFundTransferReceipts) {

        this.cmFundTransferReceipts = cmFundTransferReceipts;
    }

    public void addCmDistributionRecord(LocalCmDistributionRecord entity) {

        try {
            entity.setCmFundTransfer(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropCmDistributionRecord(LocalCmDistributionRecord entity) {

        try {
            entity.setCmFundTransfer(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addCmFundTransferReceipt(LocalCmFundTransferReceipt entity) {

        try {
            entity.setCmFundTransfer(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropCmFundTransferReceipt(LocalCmFundTransferReceipt entity) {

        try {
            entity.setCmFundTransfer(null);
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