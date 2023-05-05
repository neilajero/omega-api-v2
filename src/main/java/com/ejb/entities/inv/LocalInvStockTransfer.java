package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.*;

@Entity(name = "InvStockTransfer")
@Table(name = "INV_STCK_TRNSFR")
public class LocalInvStockTransfer extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INV_ST_CODE", nullable = false)
    private Integer stCode;

    @Column(name = "ST_DCMNT_NMBR", columnDefinition = "VARCHAR")
    private String stDocumentNumber;

    @Column(name = "ST_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String stReferenceNumber;

    @Column(name = "ST_DESC", columnDefinition = "VARCHAR")
    private String stDescription;

    @Column(name = "ST_DT", columnDefinition = "DATETIME")
    private Date stDate;

    @Column(name = "ST_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String stApprovalStatus;

    @Column(name = "ST_PSTD", columnDefinition = "TINYINT")
    private byte stPosted;

    @Column(name = "ST_CRTD_BY", columnDefinition = "VARCHAR")
    private String stCreatedBy;

    @Column(name = "ST_DT_CRTD", columnDefinition = "DATETIME")
    private Date stDateCreated;

    @Column(name = "ST_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String stLastModifiedBy;

    @Column(name = "ST_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date stDateLastModified;

    @Column(name = "ST_APPRVD_RJCTD_BY", columnDefinition = "VARCHAR")
    private String stApprovedRejectedBy;

    @Column(name = "ST_DT_APPRVD_RJCTD", columnDefinition = "DATETIME")
    private Date stDateApprovedRejected;

    @Column(name = "ST_PSTD_BY", columnDefinition = "VARCHAR")
    private String stPostedBy;

    @Column(name = "ST_DT_PSTD", columnDefinition = "DATETIME")
    private Date stDatePosted;

    @Column(name = "ST_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String stReasonForRejection;

    @Column(name = "ST_AD_BRNCH", columnDefinition = "INT")
    private Integer stAdBranch;

    @Column(name = "ST_AD_CMPNY", columnDefinition = "INT")
    private Integer stAdCompany;

    @OneToMany(mappedBy = "invStockTransfer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvStockTransferLine> invStockTransferLines;

    @OneToMany(mappedBy = "invStockTransfer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvDistributionRecord> invDistributionRecords;

    public Integer getStCode() {

        return stCode;
    }

    public void setStCode(Integer INV_ST_CODE) {

        this.stCode = INV_ST_CODE;
    }

    public String getStDocumentNumber() {

        return stDocumentNumber;
    }

    public void setStDocumentNumber(String ST_DCMNT_NMBR) {

        this.stDocumentNumber = ST_DCMNT_NMBR;
    }

    public String getStReferenceNumber() {

        return stReferenceNumber;
    }

    public void setStReferenceNumber(String ST_RFRNC_NMBR) {

        this.stReferenceNumber = ST_RFRNC_NMBR;
    }

    public String getStDescription() {

        return stDescription;
    }

    public void setStDescription(String ST_DESC) {

        this.stDescription = ST_DESC;
    }

    public Date getStDate() {

        return stDate;
    }

    public void setStDate(Date ST_DT) {

        this.stDate = ST_DT;
    }

    public String getStApprovalStatus() {

        return stApprovalStatus;
    }

    public void setStApprovalStatus(String ST_APPRVL_STATUS) {

        this.stApprovalStatus = ST_APPRVL_STATUS;
    }

    public byte getStPosted() {

        return stPosted;
    }

    public void setStPosted(byte ST_PSTD) {

        this.stPosted = ST_PSTD;
    }

    public String getStCreatedBy() {

        return stCreatedBy;
    }

    public void setStCreatedBy(String ST_CRTD_BY) {

        this.stCreatedBy = ST_CRTD_BY;
    }

    public Date getStDateCreated() {

        return stDateCreated;
    }

    public void setStDateCreated(Date ST_DT_CRTD) {

        this.stDateCreated = ST_DT_CRTD;
    }

    public String getStLastModifiedBy() {

        return stLastModifiedBy;
    }

    public void setStLastModifiedBy(String ST_LST_MDFD_BY) {

        this.stLastModifiedBy = ST_LST_MDFD_BY;
    }

    public Date getStDateLastModified() {

        return stDateLastModified;
    }

    public void setStDateLastModified(Date ST_DT_LST_MDFD) {

        this.stDateLastModified = ST_DT_LST_MDFD;
    }

    public String getStApprovedRejectedBy() {

        return stApprovedRejectedBy;
    }

    public void setStApprovedRejectedBy(String ST_APPRVD_RJCTD_BY) {

        this.stApprovedRejectedBy = ST_APPRVD_RJCTD_BY;
    }

    public Date getStDateApprovedRejected() {

        return stDateApprovedRejected;
    }

    public void setStDateApprovedRejected(Date ST_DT_APPRVD_RJCTD) {

        this.stDateApprovedRejected = ST_DT_APPRVD_RJCTD;
    }

    public String getStPostedBy() {

        return stPostedBy;
    }

    public void setStPostedBy(String ST_PSTD_BY) {

        this.stPostedBy = ST_PSTD_BY;
    }

    public Date getStDatePosted() {

        return stDatePosted;
    }

    public void setStDatePosted(Date ST_DT_PSTD) {

        this.stDatePosted = ST_DT_PSTD;
    }

    public String getStReasonForRejection() {

        return stReasonForRejection;
    }

    public void setStReasonForRejection(String ST_RSN_FR_RJCTN) {

        this.stReasonForRejection = ST_RSN_FR_RJCTN;
    }

    public Integer getStAdBranch() {

        return stAdBranch;
    }

    public void setStAdBranch(Integer ST_AD_BRNCH) {

        this.stAdBranch = ST_AD_BRNCH;
    }

    public Integer getStAdCompany() {

        return stAdCompany;
    }

    public void setStAdCompany(Integer ST_AD_CMPNY) {

        this.stAdCompany = ST_AD_CMPNY;
    }

    @XmlTransient
    public List getInvStockTransferLines() {

        return invStockTransferLines;
    }

    public void setInvStockTransferLines(List invStockTransferLines) {

        this.invStockTransferLines = invStockTransferLines;
    }

    @XmlTransient
    public List getInvDistributionRecords() {

        return invDistributionRecords;
    }

    public void setInvDistributionRecords(List invDistributionRecords) {

        this.invDistributionRecords = invDistributionRecords;
    }

    public void addInvStockTransferLine(LocalInvStockTransferLine entity) {

        try {
            entity.setInvStockTransfer(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvStockTransferLine(LocalInvStockTransferLine entity) {

        try {
            entity.setInvStockTransfer(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvDistributionRecord(LocalInvDistributionRecord entity) {

        try {
            entity.setInvStockTransfer(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvDistributionRecord(LocalInvDistributionRecord entity) {

        try {
            entity.setInvStockTransfer(null);
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