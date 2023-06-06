package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name = "InvBuildOrder")
@Table(name = "INV_BLD_ORDR")
public class LocalInvBuildOrder extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOR_CODE", nullable = false)
    private Integer borCode;

    @Column(name = "BOR_DT", columnDefinition = "DATETIME")
    private Date borDate;

    @Column(name = "BOR_DCMNT_NMBR", columnDefinition = "VARCHAR")
    private String borDocumentNumber;

    @Column(name = "BOR_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String borReferenceNumber;

    @Column(name = "BOR_DESC", columnDefinition = "VARCHAR")
    private String borDescription;

    @Column(name = "BOR_VOID", columnDefinition = "TINYINT")
    private byte borVoid;
    @Column(name = "BOR_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String borApprovalStatus;
    @Column(name = "BOR_PSTD", columnDefinition = "TINYINT")
    private byte borPosted;
    @Column(name = "BOR_CRTD_BY", columnDefinition = "VARCHAR")
    private String borCreatedBy;
    @Column(name = "BOR_DT_CRTD", columnDefinition = "DATETIME")
    private Date borDateCreated;
    @Column(name = "BOR_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String borLastModifiedBy;
    @Column(name = "BOR_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date borDateLastModified;
    @Column(name = "BOR_APPRVD_RJCTD_BY", columnDefinition = "VARCHAR")
    private String borApprovedRejectedBy;
    @Column(name = "BOR_DT_APPRVD_RJCTD", columnDefinition = "DATETIME")
    private Date borDateApprovedRejected;
    @Column(name = "BOR_PSTD_BY", columnDefinition = "VARCHAR")
    private String borPostedBy;
    @Column(name = "BOR_DT_PSTD", columnDefinition = "DATETIME")
    private Date borDatePosted;
    @Column(name = "BOR_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String borReasonForRejection;
    @Column(name = "BOR_AD_BRNCH", columnDefinition = "INT")
    private Integer borAdBranch;
    @Column(name = "BOR_AD_CMPNY", columnDefinition = "INT")
    private Integer borAdCompany;
    @OneToMany(mappedBy = "invBuildOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvBuildOrderLine> invBuildOrderLines;

    public Integer getBorCode() {

        return borCode;
    }

    public void setBorCode(Integer borCode) {

        this.borCode = borCode;
    }

    public Date getBorDate() {

        return borDate;
    }

    public void setBorDate(Date borDate) {

        this.borDate = borDate;
    }

    public String getBorDocumentNumber() {

        return borDocumentNumber;
    }

    public void setBorDocumentNumber(String borDocumentNumber) {

        this.borDocumentNumber = borDocumentNumber;
    }

    public String getBorReferenceNumber() {

        return borReferenceNumber;
    }

    public void setBorReferenceNumber(String borReferenceNumber) {

        this.borReferenceNumber = borReferenceNumber;
    }

    public String getBorDescription() {

        return borDescription;
    }

    public void setBorDescription(String borDescription) {

        this.borDescription = borDescription;
    }

    public byte getBorVoid() {

        return borVoid;
    }

    public void setBorVoid(byte borVoid) {

        this.borVoid = borVoid;
    }

    public String getBorApprovalStatus() {

        return borApprovalStatus;
    }

    public void setBorApprovalStatus(String borApprovalStatus) {

        this.borApprovalStatus = borApprovalStatus;
    }

    public byte getBorPosted() {

        return borPosted;
    }

    public void setBorPosted(byte borPosted) {

        this.borPosted = borPosted;
    }

    public String getBorCreatedBy() {

        return borCreatedBy;
    }

    public void setBorCreatedBy(String borCreatedBy) {

        this.borCreatedBy = borCreatedBy;
    }

    public Date getBorDateCreated() {

        return borDateCreated;
    }

    public void setBorDateCreated(Date borDateCreated) {

        this.borDateCreated = borDateCreated;
    }

    public String getBorLastModifiedBy() {

        return borLastModifiedBy;
    }

    public void setBorLastModifiedBy(String borLastModifiedBy) {

        this.borLastModifiedBy = borLastModifiedBy;
    }

    public Date getBorDateLastModified() {

        return borDateLastModified;
    }

    public void setBorDateLastModified(Date borDateLastModified) {

        this.borDateLastModified = borDateLastModified;
    }

    public String getBorApprovedRejectedBy() {

        return borApprovedRejectedBy;
    }

    public void setBorApprovedRejectedBy(String borApprovedRejectedBy) {

        this.borApprovedRejectedBy = borApprovedRejectedBy;
    }

    public Date getBorDateApprovedRejected() {

        return borDateApprovedRejected;
    }

    public void setBorDateApprovedRejected(Date borDateApprovedRejected) {

        this.borDateApprovedRejected = borDateApprovedRejected;
    }

    public String getBorPostedBy() {

        return borPostedBy;
    }

    public void setBorPostedBy(String borPostedBy) {

        this.borPostedBy = borPostedBy;
    }

    public Date getBorDatePosted() {

        return borDatePosted;
    }

    public void setBorDatePosted(Date borDatePosted) {

        this.borDatePosted = borDatePosted;
    }

    public String getBorReasonForRejection() {

        return borReasonForRejection;
    }

    public void setBorReasonForRejection(String borReasonForRejection) {

        this.borReasonForRejection = borReasonForRejection;
    }

    public Integer getBorAdBranch() {

        return borAdBranch;
    }

    public void setBorAdBranch(Integer borAdBranch) {

        this.borAdBranch = borAdBranch;
    }

    public Integer getBorAdCompany() {

        return borAdCompany;
    }

    public void setBorAdCompany(Integer borAdCompany) {

        this.borAdCompany = borAdCompany;
    }

    public List<LocalInvBuildOrderLine> getInvBuildOrderLines() {

        return invBuildOrderLines;
    }

    public void setInvBuildOrderLines(List<LocalInvBuildOrderLine> invBuildOrderLines) {

        this.invBuildOrderLines = invBuildOrderLines;
    }

}