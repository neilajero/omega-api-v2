package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "ArJobOrder")
@Table(name = "AR_JB_ORDR")
public class LocalArJobOrder extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JO_CODE", nullable = false)
    private Integer joCode;

    @Column(name = "JO_DT", columnDefinition = "DATETIME")
    private Date joDate;

    @Column(name = "JO_DCMNT_TYP", columnDefinition = "VARCHAR")
    private String joDocumentType;

    @Column(name = "JO_DCMNT_NMBR", columnDefinition = "VARCHAR")
    private String joDocumentNumber;

    @Column(name = "JO_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String joReferenceNumber;

    @Column(name = "JO_TRNSCTN_TYP", columnDefinition = "VARCHAR")
    private String joTransactionType;

    @Column(name = "JO_DESC", columnDefinition = "VARCHAR")
    private String joDescription;

    @Column(name = "JO_BLL_TO", columnDefinition = "VARCHAR")
    private String joBillTo;

    @Column(name = "JO_SHP_TO", columnDefinition = "VARCHAR")
    private String joShipTo;

    @Column(name = "JO_TCHNCN", columnDefinition = "VARCHAR")
    private String joTechnician;

    @Column(name = "JO_CNVRSN_DT", columnDefinition = "DATETIME")
    private Date joConversionDate;

    @Column(name = "JO_CNVRSN_RT", columnDefinition = "DOUBLE")
    private double joConversionRate = 0;

    @Column(name = "JO_VD", columnDefinition = "TINYINT")
    private byte joVoid;

    @Column(name = "JO_MBL", columnDefinition = "TINYINT")
    private byte joMobile;

    @Column(name = "JO_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String joApprovalStatus;

    @Column(name = "JO_PSTD", columnDefinition = "TINYINT")
    private byte joPosted;

    @Column(name = "JO_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String joReasonForRejection;

    @Column(name = "JO_CRTD_BY", columnDefinition = "VARCHAR")
    private String joCreatedBy;

    @Column(name = "JO_DT_CRTD", columnDefinition = "DATETIME")
    private Date joDateCreated;

    @Column(name = "JO_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String joLastModifiedBy;

    @Column(name = "JO_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date joDateLastModified;

    @Column(name = "JO_APPRVD_RJCTD_BY", columnDefinition = "VARCHAR")
    private String joApprovedRejectedBy;

    @Column(name = "JO_DT_APPRVD_RJCTD", columnDefinition = "DATETIME")
    private Date joDateApprovedRejected;

    @Column(name = "JO_PSTD_BY", columnDefinition = "VARCHAR")
    private String joPostedBy;

    @Column(name = "JO_DT_PSTD", columnDefinition = "DATETIME")
    private Date joDatePosted;

    @Column(name = "JO_LCK", columnDefinition = "TINYINT")
    private byte joLock;

    @Column(name = "JO_BO_LCK", columnDefinition = "TINYINT")
    private byte joBoLock;

    @Column(name = "JO_MMO", columnDefinition = "VARCHAR")
    private String joMemo;

    @Column(name = "JO_JB_ORDR_STTS", columnDefinition = "VARCHAR")
    private String joJobOrderStatus;

    @Column(name = "JO_ORDR_STTS", columnDefinition = "VARCHAR")
    private String joOrderStatus;

    @Column(name = "REPORT_PARAMETER", columnDefinition = "VARCHAR")
    private String reportParameter;

    @Column(name = "JO_AD_BRNCH", columnDefinition = "INT")
    private Integer joAdBranch;

    @Column(name = "JO_AD_CMPNY", columnDefinition = "INT")
    private Integer joAdCompany;

    @JoinColumn(name = "AD_PAYMENT_TERM", referencedColumnName = "PYT_CODE")
    @ManyToOne
    private LocalAdPaymentTerm adPaymentTerm;

    @JoinColumn(name = "AR_CUSTOMER", referencedColumnName = "AR_CST_CODE")
    @ManyToOne
    private LocalArCustomer arCustomer;

    @JoinColumn(name = "AR_JOB_ORDER_TYPE", referencedColumnName = "JOT_CODE")
    @ManyToOne
    private LocalArJobOrderType arJobOrderType;

    @JoinColumn(name = "AR_SALESPERSON", referencedColumnName = "SLP_CODE")
    @ManyToOne
    private LocalArSalesperson arSalesperson;

    @JoinColumn(name = "AR_TAX_CODE", referencedColumnName = "AR_TC_CODE")
    @ManyToOne
    private LocalArTaxCode arTaxCode;

    @JoinColumn(name = "GL_FUNCTIONAL_CURRENCY", referencedColumnName = "FC_CODE")
    @ManyToOne
    private LocalGlFunctionalCurrency glFunctionalCurrency;

    @OneToMany(mappedBy = "arJobOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArJobOrderLine> arJobOrderLines;

    @OneToMany(mappedBy = "arJobOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalCmAdjustment> cmAdjustments;

    public Integer getJoCode() {

        return joCode;
    }

    public void setJoCode(Integer JO_CODE) {

        this.joCode = JO_CODE;
    }

    public Date getJoDate() {

        return joDate;
    }

    public void setJoDate(Date JO_DT) {

        this.joDate = JO_DT;
    }

    public String getJoDocumentType() {

        return joDocumentType;
    }

    public void setJoDocumentType(String JO_DCMNT_TYP) {

        this.joDocumentType = JO_DCMNT_TYP;
    }

    public String getJoDocumentNumber() {

        return joDocumentNumber;
    }

    public void setJoDocumentNumber(String JO_DCMNT_NMBR) {

        this.joDocumentNumber = JO_DCMNT_NMBR;
    }

    public String getJoReferenceNumber() {

        return joReferenceNumber;
    }

    public void setJoReferenceNumber(String JO_RFRNC_NMBR) {

        this.joReferenceNumber = JO_RFRNC_NMBR;
    }

    public String getJoTransactionType() {

        return joTransactionType;
    }

    public void setJoTransactionType(String JO_TRNSCTN_TYP) {

        this.joTransactionType = JO_TRNSCTN_TYP;
    }

    public String getJoDescription() {

        return joDescription;
    }

    public void setJoDescription(String JO_DESC) {

        this.joDescription = JO_DESC;
    }

    public String getJoBillTo() {

        return joBillTo;
    }

    public void setJoBillTo(String JO_BLL_TO) {

        this.joBillTo = JO_BLL_TO;
    }

    public String getJoShipTo() {

        return joShipTo;
    }

    public void setJoShipTo(String JO_SHP_TO) {

        this.joShipTo = JO_SHP_TO;
    }

    public String getJoTechnician() {

        return joTechnician;
    }

    public void setJoTechnician(String JO_TCHNCN) {

        this.joTechnician = JO_TCHNCN;
    }

    public Date getJoConversionDate() {

        return joConversionDate;
    }

    public void setJoConversionDate(Date JO_CNVRSN_DT) {

        this.joConversionDate = JO_CNVRSN_DT;
    }

    public double getJoConversionRate() {

        return joConversionRate;
    }

    public void setJoConversionRate(double JO_CNVRSN_RT) {

        this.joConversionRate = JO_CNVRSN_RT;
    }

    public byte getJoVoid() {

        return joVoid;
    }

    public void setJoVoid(byte JO_VD) {

        this.joVoid = JO_VD;
    }

    public byte getJoMobile() {

        return joMobile;
    }

    public void setJoMobile(byte JO_MBL) {

        this.joMobile = JO_MBL;
    }

    public String getJoApprovalStatus() {

        return joApprovalStatus;
    }

    public void setJoApprovalStatus(String JO_APPRVL_STATUS) {

        this.joApprovalStatus = JO_APPRVL_STATUS;
    }

    public byte getJoPosted() {

        return joPosted;
    }

    public void setJoPosted(byte JO_PSTD) {

        this.joPosted = JO_PSTD;
    }

    public String getJoReasonForRejection() {

        return joReasonForRejection;
    }

    public void setJoReasonForRejection(String JO_RSN_FR_RJCTN) {

        this.joReasonForRejection = JO_RSN_FR_RJCTN;
    }

    public String getJoCreatedBy() {

        return joCreatedBy;
    }

    public void setJoCreatedBy(String JO_CRTD_BY) {

        this.joCreatedBy = JO_CRTD_BY;
    }

    public Date getJoDateCreated() {

        return joDateCreated;
    }

    public void setJoDateCreated(Date JO_DT_CRTD) {

        this.joDateCreated = JO_DT_CRTD;
    }

    public String getJoLastModifiedBy() {

        return joLastModifiedBy;
    }

    public void setJoLastModifiedBy(String JO_LST_MDFD_BY) {

        this.joLastModifiedBy = JO_LST_MDFD_BY;
    }

    public Date getJoDateLastModified() {

        return joDateLastModified;
    }

    public void setJoDateLastModified(Date JO_DT_LST_MDFD) {

        this.joDateLastModified = JO_DT_LST_MDFD;
    }

    public String getJoApprovedRejectedBy() {

        return joApprovedRejectedBy;
    }

    public void setJoApprovedRejectedBy(String JO_APPRVD_RJCTD_BY) {

        this.joApprovedRejectedBy = JO_APPRVD_RJCTD_BY;
    }

    public Date getJoDateApprovedRejected() {

        return joDateApprovedRejected;
    }

    public void setJoDateApprovedRejected(Date JO_DT_APPRVD_RJCTD) {

        this.joDateApprovedRejected = JO_DT_APPRVD_RJCTD;
    }

    public String getJoPostedBy() {

        return joPostedBy;
    }

    public void setJoPostedBy(String JO_PSTD_BY) {

        this.joPostedBy = JO_PSTD_BY;
    }

    public Date getJoDatePosted() {

        return joDatePosted;
    }

    public void setJoDatePosted(Date JO_DT_PSTD) {

        this.joDatePosted = JO_DT_PSTD;
    }

    public byte getJoLock() {

        return joLock;
    }

    public void setJoLock(byte JO_LCK) {

        this.joLock = JO_LCK;
    }

    public byte getJoBoLock() {

        return joBoLock;
    }

    public void setJoBoLock(byte JO_BO_LCK) {

        this.joBoLock = JO_BO_LCK;
    }

    public String getJoMemo() {

        return joMemo;
    }

    public void setJoMemo(String JO_MMO) {

        this.joMemo = JO_MMO;
    }

    public String getJoJobOrderStatus() {

        return joJobOrderStatus;
    }

    public void setJoJobOrderStatus(String JO_JB_ORDR_STTS) {

        this.joJobOrderStatus = JO_JB_ORDR_STTS;
    }

    public String getJoOrderStatus() {

        return joOrderStatus;
    }

    public void setJoOrderStatus(String JO_ORDR_STTS) {

        this.joOrderStatus = JO_ORDR_STTS;
    }

    public String getReportParameter() {

        return reportParameter;
    }

    public void setReportParameter(String REPORT_PARAMETER) {

        this.reportParameter = REPORT_PARAMETER;
    }

    public Integer getJoAdBranch() {

        return joAdBranch;
    }

    public void setJoAdBranch(Integer JO_AD_BRNCH) {

        this.joAdBranch = JO_AD_BRNCH;
    }

    public Integer getJoAdCompany() {

        return joAdCompany;
    }

    public void setJoAdCompany(Integer JO_AD_CMPNY) {

        this.joAdCompany = JO_AD_CMPNY;
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

    public LocalArJobOrderType getArJobOrderType() {

        return arJobOrderType;
    }

    public void setArJobOrderType(LocalArJobOrderType arJobOrderType) {

        this.arJobOrderType = arJobOrderType;
    }

    public LocalArSalesperson getArSalesperson() {

        return arSalesperson;
    }

    public void setArSalesperson(LocalArSalesperson arSalesperson) {

        this.arSalesperson = arSalesperson;
    }

    public LocalArTaxCode getArTaxCode() {

        return arTaxCode;
    }

    public void setArTaxCode(LocalArTaxCode arTaxCode) {

        this.arTaxCode = arTaxCode;
    }

    public LocalGlFunctionalCurrency getGlFunctionalCurrency() {

        return glFunctionalCurrency;
    }

    public void setGlFunctionalCurrency(LocalGlFunctionalCurrency glFunctionalCurrency) {

        this.glFunctionalCurrency = glFunctionalCurrency;
    }

    @XmlTransient
    public List getArJobOrderLines() {

        return arJobOrderLines;
    }

    public void setArJobOrderLines(List arJobOrderLines) {

        this.arJobOrderLines = arJobOrderLines;
    }

    @XmlTransient
    public List getCmAdjustments() {

        return cmAdjustments;
    }

    public void setCmAdjustments(List cmAdjustments) {

        this.cmAdjustments = cmAdjustments;
    }

}