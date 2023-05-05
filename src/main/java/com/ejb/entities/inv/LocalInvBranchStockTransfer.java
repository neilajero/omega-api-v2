package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdBranch;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.*;

@Entity(name = "InvBranchStockTransfer")
@Table(name = "INV_BRNCH_STCK_TRNSFR")
public class LocalInvBranchStockTransfer extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BST_CODE", nullable = false)
    private Integer bstCode;

    @Column(name = "BST_DT", columnDefinition = "DATETIME")
    private Date bstDate;

    @Column(name = "BST_TYP", columnDefinition = "VARCHAR")
    private String bstType;

    @Column(name = "BST_NMBR", columnDefinition = "VARCHAR")
    private String bstNumber;

    @Column(name = "BST_TRNSFR_OUT_NMBR", columnDefinition = "VARCHAR")
    private String bstTransferOutNumber;

    @Column(name = "BST_TRNSFR_ORDER_NMBR", columnDefinition = "VARCHAR")
    private String bstTransferOrderNumber;

    @Column(name = "BST_DSCRPTN", columnDefinition = "VARCHAR")
    private String bstDescription;

    @Column(name = "BST_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String bstApprovalStatus;

    @Column(name = "BST_PSTD", columnDefinition = "TINYINT")
    private byte bstPosted;

    @Column(name = "BST_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String bstReasonForRejection;

    @Column(name = "BST_CRTD_BY", columnDefinition = "VARCHAR")
    private String bstCreatedBy;

    @Column(name = "BST_DT_CRTD", columnDefinition = "DATETIME")
    private Date bstDateCreated;

    @Column(name = "BST_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String bstLastModifiedBy;

    @Column(name = "BST_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date bstDateLastModified;

    @Column(name = "BST_APPRVD_RJCTD_BY", columnDefinition = "VARCHAR")
    private String bstApprovedRejectedBy;

    @Column(name = "BST_DT_APPRVD_RJCTD", columnDefinition = "DATETIME")
    private Date bstDateApprovedRejected;

    @Column(name = "BST_PSTD_BY", columnDefinition = "VARCHAR")
    private String bstPostedBy;

    @Column(name = "BST_DT_PSTD", columnDefinition = "DATETIME")
    private Date bstDatePosted;

    @Column(name = "BST_LCK", columnDefinition = "TINYINT")
    private byte bstLock;

    @Column(name = "BST_VOID", columnDefinition = "TINYINT")
    private byte bstVoid;

    @Column(name = "BST_AD_BRNCH", columnDefinition = "INT")
    private Integer bstAdBranch;

    @Column(name = "BST_AD_CMPNY", columnDefinition = "INT")
    private Integer bstAdCompany;

    @JoinColumn(name = "AD_BRANCH", referencedColumnName = "BR_CODE")
    @ManyToOne
    private LocalAdBranch adBranch;

    @JoinColumn(name = "INV_LOCATION", referencedColumnName = "LOC_CODE")
    @ManyToOne
    private LocalInvLocation invLocation;

    @OneToMany(mappedBy = "invBranchStockTransfer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvBranchStockTransferLine> invBranchStockTransferLines;

    @OneToMany(mappedBy = "invBranchStockTransfer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvDistributionRecord> invDistributionRecords;

    public Integer getBstCode() {

        return bstCode;
    }

    public void setBstCode(Integer BST_CODE) {

        this.bstCode = BST_CODE;
    }

    public Date getBstDate() {

        return bstDate;
    }

    public void setBstDate(Date BST_DT) {

        this.bstDate = BST_DT;
    }

    public String getBstType() {

        return bstType;
    }

    public void setBstType(String BST_TYP) {

        this.bstType = BST_TYP;
    }

    public String getBstNumber() {

        return bstNumber;
    }

    public void setBstNumber(String BST_NMBR) {

        this.bstNumber = BST_NMBR;
    }

    public String getBstTransferOutNumber() {

        return bstTransferOutNumber;
    }

    public void setBstTransferOutNumber(String BST_TRNSFR_OUT_NMBR) {

        this.bstTransferOutNumber = BST_TRNSFR_OUT_NMBR;
    }

    public String getBstTransferOrderNumber() {

        return bstTransferOrderNumber;
    }

    public void setBstTransferOrderNumber(String BST_TRNSFR_ORDER_NMBR) {

        this.bstTransferOrderNumber = BST_TRNSFR_ORDER_NMBR;
    }

    public String getBstDescription() {

        return bstDescription;
    }

    public void setBstDescription(String BST_DSCRPTN) {

        this.bstDescription = BST_DSCRPTN;
    }

    public String getBstApprovalStatus() {

        return bstApprovalStatus;
    }

    public void setBstApprovalStatus(String BST_APPRVL_STATUS) {

        this.bstApprovalStatus = BST_APPRVL_STATUS;
    }

    public byte getBstPosted() {

        return bstPosted;
    }

    public void setBstPosted(byte BST_PSTD) {

        this.bstPosted = BST_PSTD;
    }

    public String getBstReasonForRejection() {

        return bstReasonForRejection;
    }

    public void setBstReasonForRejection(String BST_RSN_FR_RJCTN) {

        this.bstReasonForRejection = BST_RSN_FR_RJCTN;
    }

    public String getBstCreatedBy() {

        return bstCreatedBy;
    }

    public void setBstCreatedBy(String BST_CRTD_BY) {

        this.bstCreatedBy = BST_CRTD_BY;
    }

    public Date getBstDateCreated() {

        return bstDateCreated;
    }

    public void setBstDateCreated(Date BST_DT_CRTD) {

        this.bstDateCreated = BST_DT_CRTD;
    }

    public String getBstLastModifiedBy() {

        return bstLastModifiedBy;
    }

    public void setBstLastModifiedBy(String BST_LST_MDFD_BY) {

        this.bstLastModifiedBy = BST_LST_MDFD_BY;
    }

    public Date getBstDateLastModified() {

        return bstDateLastModified;
    }

    public void setBstDateLastModified(Date BST_DT_LST_MDFD) {

        this.bstDateLastModified = BST_DT_LST_MDFD;
    }

    public String getBstApprovedRejectedBy() {

        return bstApprovedRejectedBy;
    }

    public void setBstApprovedRejectedBy(String BST_APPRVD_RJCTD_BY) {

        this.bstApprovedRejectedBy = BST_APPRVD_RJCTD_BY;
    }

    public Date getBstDateApprovedRejected() {

        return bstDateApprovedRejected;
    }

    public void setBstDateApprovedRejected(Date BST_DT_APPRVD_RJCTD) {

        this.bstDateApprovedRejected = BST_DT_APPRVD_RJCTD;
    }

    public String getBstPostedBy() {

        return bstPostedBy;
    }

    public void setBstPostedBy(String BST_PSTD_BY) {

        this.bstPostedBy = BST_PSTD_BY;
    }

    public Date getBstDatePosted() {

        return bstDatePosted;
    }

    public void setBstDatePosted(Date BST_DT_PSTD) {

        this.bstDatePosted = BST_DT_PSTD;
    }

    public byte getBstLock() {

        return bstLock;
    }

    public void setBstLock(byte BST_LCK) {

        this.bstLock = BST_LCK;
    }

    public byte getBstVoid() {

        return bstVoid;
    }

    public void setBstVoid(byte BST_VOID) {

        this.bstVoid = BST_VOID;
    }

    public Integer getBstAdBranch() {

        return bstAdBranch;
    }

    public void setBstAdBranch(Integer BST_AD_BRNCH) {

        this.bstAdBranch = BST_AD_BRNCH;
    }

    public Integer getBstAdCompany() {

        return bstAdCompany;
    }

    public void setBstAdCompany(Integer BST_AD_CMPNY) {

        this.bstAdCompany = BST_AD_CMPNY;
    }

    public LocalAdBranch getAdBranch() {

        return adBranch;
    }

    public void setAdBranch(LocalAdBranch adBranch) {

        this.adBranch = adBranch;
    }

    public LocalInvLocation getInvLocation() {

        return invLocation;
    }

    public void setInvLocation(LocalInvLocation invLocation) {

        this.invLocation = invLocation;
    }

    @XmlTransient
    public List getInvBranchStockTransferLines() {

        return invBranchStockTransferLines;
    }

    public void setInvBranchStockTransferLines(List invBranchStockTransferLines) {

        this.invBranchStockTransferLines = invBranchStockTransferLines;
    }

    @XmlTransient
    public List getInvDistributionRecords() {

        return invDistributionRecords;
    }

    public void setInvDistributionRecords(List invDistributionRecords) {

        this.invDistributionRecords = invDistributionRecords;
    }

    public void addInvBranchStockTransferLine(LocalInvBranchStockTransferLine entity) {

        try {
            entity.setInvBranchStockTransfer(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvBranchStockTransferLine(LocalInvBranchStockTransferLine entity) {

        try {
            entity.setInvBranchStockTransfer(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvDistributionRecord(LocalInvDistributionRecord entity) {

        try {
            entity.setInvBranchStockTransfer(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvDistributionRecord(LocalInvDistributionRecord entity) {

        try {
            entity.setInvBranchStockTransfer(null);
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