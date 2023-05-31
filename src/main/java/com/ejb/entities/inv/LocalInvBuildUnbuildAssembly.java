package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ar.LocalArCustomer;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity(name = "InvBuildUnbuildAssemly")
@Table(name = "INV_BLD_UNBLD_ASSMBLY")
public class LocalInvBuildUnbuildAssembly extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BUA_CODE", nullable = false)
    private Integer buaCode;

    @Column(name = "BUA_RCVNG", columnDefinition = "TINYINT")
    private byte buaReceiving;

    @Column(name = "BUA_TYP", columnDefinition = "VARCHAR")
    private String buaType;

    @Column(name = "BUA_DCMNT_NMBR", columnDefinition = "VARCHAR")
    private String buaDocumentNumber;

    @Column(name = "BUA_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String buaReferenceNumber;

    @Column(name = "BUA_RCV_BO_NMBR", columnDefinition = "VARCHAR")
    private String buaReceiveBoNumber;

    @Column(name = "BUA_LCK", columnDefinition = "TINYINT")
    private byte buaLock;

    @Column(name = "BUA_DT", columnDefinition = "DATETIME")
    private Date buaDate;

    @Column(name = "BUA_DUE_DT", columnDefinition = "DATETIME")
    private Date buaDueDate;

    @Column(name = "BUA_DESC", columnDefinition = "VARCHAR")
    private String buaDescription;

    @Column(name = "BUA_VD", columnDefinition = "TINYINT")
    private byte buaVoid;

    @Column(name = "BUA_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String buaApprovalStatus;

    @Column(name = "BUA_PSTD", columnDefinition = "TINYINT")
    private byte buaPosted;

    @Column(name = "BUA_CRTD_BY", columnDefinition = "VARCHAR")
    private String buaCreatedBy;

    @Column(name = "BUA_DT_CRTD", columnDefinition = "DATETIME")
    private Date buaDateCreated;

    @Column(name = "BUA_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String buaLastModifiedBy;

    @Column(name = "BUA_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date buaDateLastModified;

    @Column(name = "BUA_APPRVD_RJCTD_BY", columnDefinition = "VARCHAR")
    private String buaApprovedRejectedBy;

    @Column(name = "BUA_DT_APPRVD_RJCTD", columnDefinition = "DATETIME")
    private Date buaDateApprovedRejected;

    @Column(name = "BUA_PSTD_BY", columnDefinition = "VARCHAR")
    private String buaPostedBy;

    @Column(name = "BUA_DT_PSTD", columnDefinition = "DATETIME")
    private Date buaDatePosted;
    @Column(name = "BUA_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String buaReasonForRejection;
    @Column(name = "BUA_AD_BRNCH", columnDefinition = "INT")
    private Integer buaAdBranch;
    @Column(name = "BUA_AD_CMPNY", columnDefinition = "INT")
    private Integer buaAdCompany;
    @JoinColumn(name = "AR_CUSTOMER", referencedColumnName = "AR_CST_CODE")
    @ManyToOne
    private LocalArCustomer arCustomer;
    @JoinColumn(name = "INV_BUA_BATCH", referencedColumnName = "BB_CODE")
    @ManyToOne
    private LocalInvBuildUnbuildAssemblyBatch invBuildUnbuildAssemblyBatch;

    public Integer getBuaCode() {

        return buaCode;
    }

    public void setBuaCode(Integer buaCode) {

        this.buaCode = buaCode;
    }

    public byte getBuaReceiving() {

        return buaReceiving;
    }

    public void setBuaReceiving(byte buaReceiving) {

        this.buaReceiving = buaReceiving;
    }

    public String getBuaType() {

        return buaType;
    }

    public void setBuaType(String buaType) {

        this.buaType = buaType;
    }

    public String getBuaDocumentNumber() {

        return buaDocumentNumber;
    }

    public void setBuaDocumentNumber(String buaDocumentNumber) {

        this.buaDocumentNumber = buaDocumentNumber;
    }

    public String getBuaReferenceNumber() {

        return buaReferenceNumber;
    }

    public void setBuaReferenceNumber(String buaReferenceNumber) {

        this.buaReferenceNumber = buaReferenceNumber;
    }

    public String getBuaReceiveBoNumber() {

        return buaReceiveBoNumber;
    }

    public void setBuaReceiveBoNumber(String buaReceiveBoNumber) {

        this.buaReceiveBoNumber = buaReceiveBoNumber;
    }

    public byte getBuaLock() {

        return buaLock;
    }

    public void setBuaLock(byte buaLock) {

        this.buaLock = buaLock;
    }

    public Date getBuaDate() {

        return buaDate;
    }

    public void setBuaDate(Date buaDate) {

        this.buaDate = buaDate;
    }

    public Date getBuaDueDate() {

        return buaDueDate;
    }

    public void setBuaDueDate(Date buaDueDate) {

        this.buaDueDate = buaDueDate;
    }

    public String getBuaDescription() {

        return buaDescription;
    }

    public void setBuaDescription(String buaDescription) {

        this.buaDescription = buaDescription;
    }

    public byte getBuaVoid() {

        return buaVoid;
    }

    public void setBuaVoid(byte buaVoid) {

        this.buaVoid = buaVoid;
    }

    public String getBuaApprovalStatus() {

        return buaApprovalStatus;
    }

    public void setBuaApprovalStatus(String buaApprovalStatus) {

        this.buaApprovalStatus = buaApprovalStatus;
    }

    public byte getBuaPosted() {

        return buaPosted;
    }

    public void setBuaPosted(byte buaPosted) {

        this.buaPosted = buaPosted;
    }

    public String getBuaCreatedBy() {

        return buaCreatedBy;
    }

    public void setBuaCreatedBy(String buaCreatedBy) {

        this.buaCreatedBy = buaCreatedBy;
    }

    public Date getBuaDateCreated() {

        return buaDateCreated;
    }

    public void setBuaDateCreated(Date buaDateCreated) {

        this.buaDateCreated = buaDateCreated;
    }

    public String getBuaLastModifiedBy() {

        return buaLastModifiedBy;
    }

    public void setBuaLastModifiedBy(String buaLastModifiedBy) {

        this.buaLastModifiedBy = buaLastModifiedBy;
    }

    public Date getBuaDateLastModified() {

        return buaDateLastModified;
    }

    public void setBuaDateLastModified(Date buaDateLastModified) {

        this.buaDateLastModified = buaDateLastModified;
    }

    public String getBuaApprovedRejectedBy() {

        return buaApprovedRejectedBy;
    }

    public void setBuaApprovedRejectedBy(String buaApprovedRejectedBy) {

        this.buaApprovedRejectedBy = buaApprovedRejectedBy;
    }

    public Date getBuaDateApprovedRejected() {

        return buaDateApprovedRejected;
    }

    public void setBuaDateApprovedRejected(Date buaDateApprovedRejected) {

        this.buaDateApprovedRejected = buaDateApprovedRejected;
    }

    public String getBuaPostedBy() {

        return buaPostedBy;
    }

    public void setBuaPostedBy(String buaPostedBy) {

        this.buaPostedBy = buaPostedBy;
    }

    public Date getBuaDatePosted() {

        return buaDatePosted;
    }

    public void setBuaDatePosted(Date buaDatePosted) {

        this.buaDatePosted = buaDatePosted;
    }

    public String getBuaReasonForRejection() {

        return buaReasonForRejection;
    }

    public void setBuaReasonForRejection(String buaReasonForRejection) {

        this.buaReasonForRejection = buaReasonForRejection;
    }

    public Integer getBuaAdBranch() {

        return buaAdBranch;
    }

    public void setBuaAdBranch(Integer buaAdBranch) {

        this.buaAdBranch = buaAdBranch;
    }

    public Integer getBuaAdCompany() {

        return buaAdCompany;
    }

    public void setBuaAdCompany(Integer buaAdCompany) {

        this.buaAdCompany = buaAdCompany;
    }

    public LocalArCustomer getArCustomer() {

        return arCustomer;
    }

    public void setArCustomer(LocalArCustomer arCustomer) {

        this.arCustomer = arCustomer;
    }

    public LocalInvBuildUnbuildAssemblyBatch getInvBuildUnbuildAssemblyBatch() {

        return invBuildUnbuildAssemblyBatch;
    }

    public void setInvBuildUnbuildAssemblyBatch(LocalInvBuildUnbuildAssemblyBatch invBuildUnbuildAssemblyBatch) {

        this.invBuildUnbuildAssemblyBatch = invBuildUnbuildAssemblyBatch;
    }

}