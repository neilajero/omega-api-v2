package com.ejb.entities.ap;

import com.ejb.NativeQueryHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "ApPurchaseRequisition")
@Table(name = "AP_PRCHS_RQSTN")
public class LocalApPurchaseRequisition extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PR_CODE", nullable = false)
    private Integer prCode;

    @Column(name = "PR_DESC", columnDefinition = "VARCHAR")
    private String prDescription;

    @Column(name = "PR_NMBR", columnDefinition = "VARCHAR")
    private String prNumber;

    @Column(name = "PR_DT", columnDefinition = "DATETIME")
    private Date prDate;

    @Column(name = "PR_DLVRY_PRD", columnDefinition = "DATETIME")
    private Date prDeliveryPeriod;

    @Column(name = "PR_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String prReferenceNumber;

    @Column(name = "PR_CNVRSN_DT", columnDefinition = "VARCHAR")
    private Date prConversionDate;

    @Column(name = "PR_CNVRSN_RT", columnDefinition = "DOUBLE")
    private double prConversionRate = 0;

    @Column(name = "PR_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String prApprovalStatus;

    @Column(name = "PR_CNVSS_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String prCanvassApprovalStatus;

    @Column(name = "PR_PSTD", columnDefinition = "TINYINT")
    private byte prPosted;

    @Column(name = "PR_GNRTD", columnDefinition = "TINYINT")
    private byte prGenerated;

    @Column(name = "PR_CNVSS_PSTD", columnDefinition = "TINYINT")
    private byte prCanvassPosted;

    @Column(name = "PR_VD", columnDefinition = "TINYINT")
    private byte prVoid;

    @Column(name = "PR_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String prReasonForRejection;

    @Column(name = "PR_CNVSS_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String prCanvassReasonForRejection;

    @Column(name = "PR_DPRTMNT", columnDefinition = "VARCHAR")
    private String prDepartment;

    @Column(name = "PR_CRTD_BY", columnDefinition = "VARCHAR")
    private String prCreatedBy;

    @Column(name = "PR_DT_CRTD", columnDefinition = "VARCHAR")
    private Date prDateCreated;

    @Column(name = "PR_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String prLastModifiedBy;

    @Column(name = "PR_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date prDateLastModified;

    @Column(name = "PR_APPRVD_RJCTD_BY", columnDefinition = "VARCHAR")
    private String prApprovedRejectedBy;

    @Column(name = "PR_CNVSS_APPRVD_RJCTD_BY", columnDefinition = "VARCHAR")
    private String prCanvassApprovedRejectedBy;

    @Column(name = "PR_DT_APPRVD_RJCTD", columnDefinition = "DATETIME")
    private Date prDateApprovedRejected;

    @Column(name = "PR_CNVSS_DT_APPRVD_RJCTD", columnDefinition = "DATETIME")
    private Date prCanvassDateApprovedRejected;

    @Column(name = "PR_PSTD_BY", columnDefinition = "VARCHAR")
    private String prPostedBy;

    @Column(name = "PR_CNVSS_PSTD_BY", columnDefinition = "VARCHAR")
    private String prCanvassPostedBy;

    @Column(name = "PR_DT_PSTD", columnDefinition = "DATETIME")
    private Date prDatePosted;

    @Column(name = "PR_CNVSS_DT_PSTD", columnDefinition = "DATETIME")
    private Date prCanvassDatePosted;

    @Column(name = "PR_TG_STATUS", columnDefinition = "VARCHAR")
    private String prTagStatus;

    @Column(name = "PR_TYPE", columnDefinition = "VARCHAR")
    private String prType;

    @Column(name = "PR_AD_USR_NM1", columnDefinition = "INT")
    private Integer prAdUserName1;

    @Column(name = "PR_SCHDL", columnDefinition = "VARCHAR")
    private String prSchedule;

    @Column(name = "PR_NXT_RN_DT", columnDefinition = "DATETIME")
    private Date prNextRunDate;

    @Column(name = "PR_LST_RN_DT", columnDefinition = "DATETIME")
    private Date prLastRunDate;

    @Column(name = "PR_AD_BRNCH", columnDefinition = "INT")
    private Integer prAdBranch;

    @Column(name = "PR_AD_CMPNY", columnDefinition = "INT")
    private Integer prAdCompany;

    @JoinColumn(name = "AP_TAX_CODE", referencedColumnName = "AP_TC_CODE")
    @ManyToOne
    private LocalApTaxCode apTaxCode;

    @JoinColumn(name = "GL_FUNCTIONAL_CURRENCY", referencedColumnName = "FC_CODE")
    @ManyToOne
    private LocalGlFunctionalCurrency glFunctionalCurrency;

    @OneToMany(mappedBy = "apPurchaseRequisition", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalApPurchaseRequisitionLine> apPurchaseRequisitionLines;

    public Integer getPrCode() {

        return prCode;
    }

    public void setPrCode(Integer PR_CODE) {

        this.prCode = PR_CODE;
    }

    public String getPrDescription() {

        return prDescription;
    }

    public void setPrDescription(String PR_DESC) {

        this.prDescription = PR_DESC;
    }

    public String getPrNumber() {

        return prNumber;
    }

    public void setPrNumber(String PR_NMBR) {

        this.prNumber = PR_NMBR;
    }

    public Date getPrDate() {

        return prDate;
    }

    public void setPrDate(Date PR_DT) {

        this.prDate = PR_DT;
    }

    public Date getPrDeliveryPeriod() {

        return prDeliveryPeriod;
    }

    public void setPrDeliveryPeriod(Date PR_DLVRY_PRD) {

        this.prDeliveryPeriod = PR_DLVRY_PRD;
    }

    public String getPrReferenceNumber() {

        return prReferenceNumber;
    }

    public void setPrReferenceNumber(String PR_RFRNC_NMBR) {

        this.prReferenceNumber = PR_RFRNC_NMBR;
    }

    public Date getPrConversionDate() {

        return prConversionDate;
    }

    public void setPrConversionDate(Date PR_CNVRSN_DT) {

        this.prConversionDate = PR_CNVRSN_DT;
    }

    public double getPrConversionRate() {

        return prConversionRate;
    }

    public void setPrConversionRate(double PR_CNVRSN_RT) {

        this.prConversionRate = PR_CNVRSN_RT;
    }

    public String getPrApprovalStatus() {

        return prApprovalStatus;
    }

    public void setPrApprovalStatus(String PR_APPRVL_STATUS) {

        this.prApprovalStatus = PR_APPRVL_STATUS;
    }

    public String getPrCanvassApprovalStatus() {

        return prCanvassApprovalStatus;
    }

    public void setPrCanvassApprovalStatus(String PR_CNVSS_APPRVL_STATUS) {

        this.prCanvassApprovalStatus = PR_CNVSS_APPRVL_STATUS;
    }

    public byte getPrPosted() {

        return prPosted;
    }

    public void setPrPosted(byte PR_PSTD) {

        this.prPosted = PR_PSTD;
    }

    public byte getPrGenerated() {

        return prGenerated;
    }

    public void setPrGenerated(byte PR_GNRTD) {

        this.prGenerated = PR_GNRTD;
    }

    public byte getPrCanvassPosted() {

        return prCanvassPosted;
    }

    public void setPrCanvassPosted(byte PR_CNVSS_PSTD) {

        this.prCanvassPosted = PR_CNVSS_PSTD;
    }

    public byte getPrVoid() {

        return prVoid;
    }

    public void setPrVoid(byte PR_VD) {

        this.prVoid = PR_VD;
    }

    public String getPrReasonForRejection() {

        return prReasonForRejection;
    }

    public void setPrReasonForRejection(String PR_RSN_FR_RJCTN) {

        this.prReasonForRejection = PR_RSN_FR_RJCTN;
    }

    public String getPrCanvassReasonForRejection() {

        return prCanvassReasonForRejection;
    }

    public void setPrCanvassReasonForRejection(String PR_CNVSS_RSN_FR_RJCTN) {

        this.prCanvassReasonForRejection = PR_CNVSS_RSN_FR_RJCTN;
    }

    public String getPrDepartment() {

        return prDepartment;
    }

    public void setPrDepartment(String PR_DPRTMNT) {

        this.prDepartment = PR_DPRTMNT;
    }

    public String getPrCreatedBy() {

        return prCreatedBy;
    }

    public void setPrCreatedBy(String PR_CRTD_BY) {

        this.prCreatedBy = PR_CRTD_BY;
    }

    public Date getPrDateCreated() {

        return prDateCreated;
    }

    public void setPrDateCreated(Date PR_DT_CRTD) {

        this.prDateCreated = PR_DT_CRTD;
    }

    public String getPrLastModifiedBy() {

        return prLastModifiedBy;
    }

    public void setPrLastModifiedBy(String PR_LST_MDFD_BY) {

        this.prLastModifiedBy = PR_LST_MDFD_BY;
    }

    public Date getPrDateLastModified() {

        return prDateLastModified;
    }

    public void setPrDateLastModified(Date PR_DT_LST_MDFD) {

        this.prDateLastModified = PR_DT_LST_MDFD;
    }

    public String getPrApprovedRejectedBy() {

        return prApprovedRejectedBy;
    }

    public void setPrApprovedRejectedBy(String PR_APPRVD_RJCTD_BY) {

        this.prApprovedRejectedBy = PR_APPRVD_RJCTD_BY;
    }

    public String getPrCanvassApprovedRejectedBy() {

        return prCanvassApprovedRejectedBy;
    }

    public void setPrCanvassApprovedRejectedBy(String PR_CNVSS_APPRVD_RJCTD_BY) {

        this.prCanvassApprovedRejectedBy = PR_CNVSS_APPRVD_RJCTD_BY;
    }

    public Date getPrDateApprovedRejected() {

        return prDateApprovedRejected;
    }

    public void setPrDateApprovedRejected(Date PR_DT_APPRVD_RJCTD) {

        this.prDateApprovedRejected = PR_DT_APPRVD_RJCTD;
    }

    public Date getPrCanvassDateApprovedRejected() {

        return prCanvassDateApprovedRejected;
    }

    public void setPrCanvassDateApprovedRejected(Date PR_CNVSS_DT_APPRVD_RJCTD) {

        this.prCanvassDateApprovedRejected = PR_CNVSS_DT_APPRVD_RJCTD;
    }

    public String getPrPostedBy() {

        return prPostedBy;
    }

    public void setPrPostedBy(String PR_PSTD_BY) {

        this.prPostedBy = PR_PSTD_BY;
    }

    public String getPrCanvassPostedBy() {

        return prCanvassPostedBy;
    }

    public void setPrCanvassPostedBy(String PR_CNVSS_PSTD_BY) {

        this.prCanvassPostedBy = PR_CNVSS_PSTD_BY;
    }

    public Date getPrDatePosted() {

        return prDatePosted;
    }

    public void setPrDatePosted(Date PR_DT_PSTD) {

        this.prDatePosted = PR_DT_PSTD;
    }

    public Date getPrCanvassDatePosted() {

        return prCanvassDatePosted;
    }

    public void setPrCanvassDatePosted(Date PR_CNVSS_DT_PSTD) {

        this.prCanvassDatePosted = PR_CNVSS_DT_PSTD;
    }

    public String getPrTagStatus() {

        return prTagStatus;
    }

    public void setPrTagStatus(String PR_TG_STATUS) {

        this.prTagStatus = PR_TG_STATUS;
    }

    public String getPrType() {

        return prType;
    }

    public void setPrType(String PR_TYPE) {

        this.prType = PR_TYPE;
    }

    public Integer getPrAdUserName1() {

        return prAdUserName1;
    }

    public void setPrAdUserName1(Integer PR_AD_USR_NM1) {

        this.prAdUserName1 = PR_AD_USR_NM1;
    }

    public String getPrSchedule() {

        return prSchedule;
    }

    public void setPrSchedule(String PR_SCHDL) {

        this.prSchedule = PR_SCHDL;
    }

    public Date getPrNextRunDate() {

        return prNextRunDate;
    }

    public void setPrNextRunDate(Date PR_NXT_RN_DT) {

        this.prNextRunDate = PR_NXT_RN_DT;
    }

    public Date getPrLastRunDate() {

        return prLastRunDate;
    }

    public void setPrLastRunDate(Date PR_LST_RN_DT) {

        this.prLastRunDate = PR_LST_RN_DT;
    }

    public Integer getPrAdBranch() {

        return prAdBranch;
    }

    public void setPrAdBranch(Integer PR_AD_BRNCH) {

        this.prAdBranch = PR_AD_BRNCH;
    }

    public Integer getPrAdCompany() {

        return prAdCompany;
    }

    public void setPrAdCompany(Integer PR_AD_CMPNY) {

        this.prAdCompany = PR_AD_CMPNY;
    }

    public LocalApTaxCode getApTaxCode() {

        return apTaxCode;
    }

    public void setApTaxCode(LocalApTaxCode apTaxCode) {

        this.apTaxCode = apTaxCode;
    }

    public LocalGlFunctionalCurrency getGlFunctionalCurrency() {

        return glFunctionalCurrency;
    }

    public void setGlFunctionalCurrency(LocalGlFunctionalCurrency glFunctionalCurrency) {

        this.glFunctionalCurrency = glFunctionalCurrency;
    }

    @XmlTransient
    public List getApPurchaseRequisitionLines() {

        return apPurchaseRequisitionLines;
    }

    public void setApPurchaseRequisitionLines(List apPurchaseRequisitionLines) {

        this.apPurchaseRequisitionLines = apPurchaseRequisitionLines;
    }

    public void addApPurchaseRequisitionLine(LocalApPurchaseRequisitionLine entity) {

        try {
            entity.setApPurchaseRequisition(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApPurchaseRequisitionLine(LocalApPurchaseRequisitionLine entity) {

        try {
            entity.setApPurchaseRequisition(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}