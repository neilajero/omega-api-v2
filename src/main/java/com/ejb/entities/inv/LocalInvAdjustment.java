package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.gl.LocalGlChartOfAccount;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.*;

@Entity(name = "InvAdjustment")
@Table(name = "INV_ADJSTMNT")
public class LocalInvAdjustment extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INV_ADJ_CODE", nullable = false)
    private Integer adjCode;

    @Column(name = "ADJ_DCMNT_NMBR", columnDefinition = "VARCHAR")
    private String adjDocumentNumber;

    @Column(name = "ADJ_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String adjReferenceNumber;

    @Column(name = "ADJ_DESC", columnDefinition = "VARCHAR")
    private String adjDescription;

    @Column(name = "ADJ_DT", columnDefinition = "DATETIME")
    private Date adjDate;

    @Column(name = "ADJ_TYP", columnDefinition = "VARCHAR")
    private String adjType;

    @Column(name = "ADJ_NT_BY", columnDefinition = "VARCHAR")
    private String adjNotedBy;

    @Column(name = "ADJ_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String adjApprovalStatus;

    @Column(name = "ADJ_PSTD", columnDefinition = "TINYINT")
    private byte adjPosted;

    @Column(name = "ADJ_GNRTD", columnDefinition = "TINYINT")
    private byte adjGenerated;

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

    @Column(name = "ADJ_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String adjReasonForRejection;

    @Column(name = "ADJ_IS_CST_VRNC", columnDefinition = "TINYINT")
    private byte adjIsCostVariance;

    @Column(name = "ADJ_VD", columnDefinition = "TINYINT")
    private byte adjVoid;

    @Column(name = "ADJ_AD_BRNCH", columnDefinition = "INT")
    private Integer adjAdBranch;

    @Column(name = "ADJ_AD_CMPNY", columnDefinition = "INT")
    private Integer adjAdCompany;

    @JoinColumn(name = "AP_SUPPLIER", referencedColumnName = "SPL_CODE")
    @ManyToOne
    private LocalApSupplier apSupplier;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    @OneToMany(mappedBy = "invAdjustment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvAdjustmentLine> invAdjustmentLines =
            new ArrayList<>();

    @OneToMany(mappedBy = "invAdjustment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvDistributionRecord> invDistributionRecords =
            new ArrayList<>();

    public Integer getAdjCode() {

        return adjCode;
    }

    public void setAdjCode(Integer INV_ADJ_CODE) {

        this.adjCode = INV_ADJ_CODE;
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

    public String getAdjDescription() {

        return adjDescription;
    }

    public void setAdjDescription(String ADJ_DESC) {

        this.adjDescription = ADJ_DESC;
    }

    public Date getAdjDate() {

        return adjDate;
    }

    public void setAdjDate(Date ADJ_DT) {

        this.adjDate = ADJ_DT;
    }

    public String getAdjType() {

        return adjType;
    }

    public void setAdjType(String ADJ_TYP) {

        this.adjType = ADJ_TYP;
    }

    public String getAdjNotedBy() {

        return adjNotedBy;
    }

    public void setAdjNotedBy(String ADJ_NT_BY) {

        this.adjNotedBy = ADJ_NT_BY;
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

    public byte getAdjGenerated() {

        return adjGenerated;
    }

    public void setAdjGenerated(byte ADJ_GNRTD) {

        this.adjGenerated = ADJ_GNRTD;
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

    public String getAdjReasonForRejection() {

        return adjReasonForRejection;
    }

    public void setAdjReasonForRejection(String ADJ_RSN_FR_RJCTN) {

        this.adjReasonForRejection = ADJ_RSN_FR_RJCTN;
    }

    public byte getAdjIsCostVariance() {

        return adjIsCostVariance;
    }

    public void setAdjIsCostVariance(byte ADJ_IS_CST_VRNC) {

        this.adjIsCostVariance = ADJ_IS_CST_VRNC;
    }

    public byte getAdjVoid() {

        return adjVoid;
    }

    public void setAdjVoid(byte ADJ_VD) {

        this.adjVoid = ADJ_VD;
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

    public LocalApSupplier getApSupplier() {

        return apSupplier;
    }

    public void setApSupplier(LocalApSupplier apSupplier) {

        this.apSupplier = apSupplier;
    }

    public LocalGlChartOfAccount getGlChartOfAccount() {

        return glChartOfAccount;
    }

    public void setGlChartOfAccount(LocalGlChartOfAccount glChartOfAccount) {

        this.glChartOfAccount = glChartOfAccount;
    }

    @XmlTransient
    public List getInvAdjustmentLines() {

        return invAdjustmentLines;
    }

    public void setInvAdjustmentLines(List invAdjustmentLines) {

        this.invAdjustmentLines = invAdjustmentLines;
    }

    @XmlTransient
    public List getInvDistributionRecords() {

        return invDistributionRecords;
    }

    public void setInvDistributionRecords(List invDistributionRecords) {

        this.invDistributionRecords = invDistributionRecords;
    }

    public void addInvAdjustmentLine(LocalInvAdjustmentLine entity) {

        try {
            entity.setInvAdjustment(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvAdjustmentLine(LocalInvAdjustmentLine entity) {

        try {
            entity.setInvAdjustment(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvDistributionRecord(LocalInvDistributionRecord entity) {

        try {
            entity.setInvAdjustment(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvDistributionRecord(LocalInvDistributionRecord entity) {

        try {
            entity.setInvAdjustment(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public short getInvDrNextLine() {

        try {
            List lists = getInvDistributionRecords();
            return (short) (lists.size() + 1);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}