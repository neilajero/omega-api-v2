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

@Entity(name = "ArSalesOrder")
@Table(name = "AR_SLS_ORDR")
public class LocalArSalesOrder extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SO_CODE", nullable = false)
    private Integer soCode;

    @Column(name = "SO_DT", columnDefinition = "DATETIME")
    private Date soDate;

    @Column(name = "SO_DCMNT_TYP", columnDefinition = "VARCHAR")
    private String soDocumentType;

    @Column(name = "SO_DCMNT_NMBR", columnDefinition = "VARCHAR")
    private String soDocumentNumber;

    @Column(name = "SO_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String soReferenceNumber;

    @Column(name = "SO_TRNSCTN_TYP", columnDefinition = "VARCHAR")
    private String soTransactionType;

    @Column(name = "SO_TRNSCTN_STTS", columnDefinition = "VARCHAR")
    private String soTransactionStatus;

    @Column(name = "SO_DESC", columnDefinition = "VARCHAR")
    private String soDescription;

    @Column(name = "SO_BLL_TO", columnDefinition = "VARCHAR")
    private String soBillTo;

    @Column(name = "SO_SHP_TO", columnDefinition = "VARCHAR")
    private String soShipTo;

    @Column(name = "SO_CNVRSN_DT", columnDefinition = "DATETIME")
    private Date soConversionDate;

    @Column(name = "SO_CNVRSN_RT", columnDefinition = "DOUBLE")
    private double soConversionRate = 0;

    @Column(name = "SO_VD", columnDefinition = "TINYINT")
    private byte soVoid;

    @Column(name = "SO_MBL", columnDefinition = "TINYINT")
    private byte soMobile;

    @Column(name = "SO_SHPPNG_LN", columnDefinition = "VARCHAR")
    private String soShippingLine;

    @Column(name = "SO_PRT", columnDefinition = "VARCHAR")
    private String soPort;

    @Column(name = "SO_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String soApprovalStatus;

    @Column(name = "SO_PSTD", columnDefinition = "TINYINT")
    private byte soPosted;

    @Column(name = "SO_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String soReasonForRejection;

    @Column(name = "SO_CRTD_BY", columnDefinition = "VARCHAR")
    private String soCreatedBy;

    @Column(name = "SO_DT_CRTD", columnDefinition = "DATETIME")
    private Date soDateCreated;

    @Column(name = "SO_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String soLastModifiedBy;

    @Column(name = "SO_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date soDateLastModified;

    @Column(name = "SO_APPRVD_RJCTD_BY", columnDefinition = "VARCHAR")
    private String soApprovedRejectedBy;

    @Column(name = "SO_DT_APPRVD_RJCTD", columnDefinition = "DATETIME")
    private Date soDateApprovedRejected;

    @Column(name = "SO_PSTD_BY", columnDefinition = "VARCHAR")
    private String soPostedBy;

    @Column(name = "SO_DT_PSTD", columnDefinition = "DATETIME")
    private Date soDatePosted;

    @Column(name = "SO_LCK", columnDefinition = "TINYINT")
    private byte soLock;

    @Column(name = "SO_BO_LCK", columnDefinition = "TINYINT")
    private byte soBoLock;

    @Column(name = "SO_MMO", columnDefinition = "VARCHAR")
    private String soMemo;

    @Column(name = "SO_ORDR_STTS", columnDefinition = "VARCHAR")
    private String soOrderStatus;

    @Column(name = "REPORT_PARAMETER", columnDefinition = "VARCHAR")
    private String reportParameter;

    @Column(name = "SO_AD_BRNCH", columnDefinition = "INT")
    private Integer soAdBranch;

    @Column(name = "SO_AD_CMPNY", columnDefinition = "INT")
    private Integer soAdCompany;

    @JoinColumn(name = "AD_PAYMENT_TERM", referencedColumnName = "PYT_CODE")
    @ManyToOne
    private LocalAdPaymentTerm adPaymentTerm;

    @JoinColumn(name = "AR_CUSTOMER", referencedColumnName = "AR_CST_CODE")
    @ManyToOne
    private LocalArCustomer arCustomer;

    @JoinColumn(name = "AR_SALESPERSON", referencedColumnName = "SLP_CODE")
    @ManyToOne
    private LocalArSalesperson arSalesperson;

    @JoinColumn(name = "AR_TAX_CODE", referencedColumnName = "AR_TC_CODE")
    @ManyToOne
    private LocalArTaxCode arTaxCode;

    @JoinColumn(name = "GL_FUNCTIONAL_CURRENCY", referencedColumnName = "FC_CODE")
    @ManyToOne
    private LocalGlFunctionalCurrency glFunctionalCurrency;

    @OneToMany(mappedBy = "arSalesOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArSalesOrderLine> arSalesOrderLines;

    @OneToMany(mappedBy = "arSalesOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalCmAdjustment> cmAdjustments;

    @OneToMany(mappedBy = "arSalesOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArDelivery> arDeliveries;

    public Integer getSoCode() {

        return soCode;
    }

    public void setSoCode(Integer SO_CODE) {

        this.soCode = SO_CODE;
    }

    public Date getSoDate() {

        return soDate;
    }

    public void setSoDate(Date SO_DT) {

        this.soDate = SO_DT;
    }

    public String getSoDocumentType() {

        return soDocumentType;
    }

    public void setSoDocumentType(String SO_DCMNT_TYP) {

        this.soDocumentType = SO_DCMNT_TYP;
    }

    public String getSoDocumentNumber() {

        return soDocumentNumber;
    }

    public void setSoDocumentNumber(String SO_DCMNT_NMBR) {

        this.soDocumentNumber = SO_DCMNT_NMBR;
    }

    public String getSoReferenceNumber() {

        return soReferenceNumber;
    }

    public void setSoReferenceNumber(String SO_RFRNC_NMBR) {

        this.soReferenceNumber = SO_RFRNC_NMBR;
    }

    public String getSoTransactionType() {

        return soTransactionType;
    }

    public void setSoTransactionType(String SO_TRNSCTN_TYP) {

        this.soTransactionType = SO_TRNSCTN_TYP;
    }

    public String getSoTransactionStatus() {

        return soTransactionStatus;
    }

    public void setSoTransactionStatus(String SO_TRNSCTN_STTS) {

        this.soTransactionStatus = SO_TRNSCTN_STTS;
    }

    public String getSoDescription() {

        return soDescription;
    }

    public void setSoDescription(String SO_DESC) {

        this.soDescription = SO_DESC;
    }

    public String getSoBillTo() {

        return soBillTo;
    }

    public void setSoBillTo(String SO_BLL_TO) {

        this.soBillTo = SO_BLL_TO;
    }

    public String getSoShipTo() {

        return soShipTo;
    }

    public void setSoShipTo(String SO_SHP_TO) {

        this.soShipTo = SO_SHP_TO;
    }

    public Date getSoConversionDate() {

        return soConversionDate;
    }

    public void setSoConversionDate(Date SO_CNVRSN_DT) {

        this.soConversionDate = SO_CNVRSN_DT;
    }

    public double getSoConversionRate() {

        return soConversionRate;
    }

    public void setSoConversionRate(double SO_CNVRSN_RT) {

        this.soConversionRate = SO_CNVRSN_RT;
    }

    public byte getSoVoid() {

        return soVoid;
    }

    public void setSoVoid(byte SO_VD) {

        this.soVoid = SO_VD;
    }

    public byte getSoMobile() {

        return soMobile;
    }

    public void setSoMobile(byte SO_MBL) {

        this.soMobile = SO_MBL;
    }

    public String getSoShippingLine() {

        return soShippingLine;
    }

    public void setSoShippingLine(String SO_SHPPNG_LN) {

        this.soShippingLine = SO_SHPPNG_LN;
    }

    public String getSoPort() {

        return soPort;
    }

    public void setSoPort(String SO_PRT) {

        this.soPort = SO_PRT;
    }

    public String getSoApprovalStatus() {

        return soApprovalStatus;
    }

    public void setSoApprovalStatus(String SO_APPRVL_STATUS) {

        this.soApprovalStatus = SO_APPRVL_STATUS;
    }

    public byte getSoPosted() {

        return soPosted;
    }

    public void setSoPosted(byte SO_PSTD) {

        this.soPosted = SO_PSTD;
    }

    public String getSoReasonForRejection() {

        return soReasonForRejection;
    }

    public void setSoReasonForRejection(String SO_RSN_FR_RJCTN) {

        this.soReasonForRejection = SO_RSN_FR_RJCTN;
    }

    public String getSoCreatedBy() {

        return soCreatedBy;
    }

    public void setSoCreatedBy(String SO_CRTD_BY) {

        this.soCreatedBy = SO_CRTD_BY;
    }

    public Date getSoDateCreated() {

        return soDateCreated;
    }

    public void setSoDateCreated(Date SO_DT_CRTD) {

        this.soDateCreated = SO_DT_CRTD;
    }

    public String getSoLastModifiedBy() {

        return soLastModifiedBy;
    }

    public void setSoLastModifiedBy(String SO_LST_MDFD_BY) {

        this.soLastModifiedBy = SO_LST_MDFD_BY;
    }

    public Date getSoDateLastModified() {

        return soDateLastModified;
    }

    public void setSoDateLastModified(Date SO_DT_LST_MDFD) {

        this.soDateLastModified = SO_DT_LST_MDFD;
    }

    public String getSoApprovedRejectedBy() {

        return soApprovedRejectedBy;
    }

    public void setSoApprovedRejectedBy(String SO_APPRVD_RJCTD_BY) {

        this.soApprovedRejectedBy = SO_APPRVD_RJCTD_BY;
    }

    public Date getSoDateApprovedRejected() {

        return soDateApprovedRejected;
    }

    public void setSoDateApprovedRejected(Date SO_DT_APPRVD_RJCTD) {

        this.soDateApprovedRejected = SO_DT_APPRVD_RJCTD;
    }

    public String getSoPostedBy() {

        return soPostedBy;
    }

    public void setSoPostedBy(String SO_PSTD_BY) {

        this.soPostedBy = SO_PSTD_BY;
    }

    public Date getSoDatePosted() {

        return soDatePosted;
    }

    public void setSoDatePosted(Date SO_DT_PSTD) {

        this.soDatePosted = SO_DT_PSTD;
    }

    public byte getSoLock() {

        return soLock;
    }

    public void setSoLock(byte SO_LCK) {

        this.soLock = SO_LCK;
    }

    public byte getSoBoLock() {

        return soBoLock;
    }

    public void setSoBoLock(byte SO_BO_LCK) {

        this.soBoLock = SO_BO_LCK;
    }

    public String getSoMemo() {

        return soMemo;
    }

    public void setSoMemo(String SO_MMO) {

        this.soMemo = SO_MMO;
    }

    public String getSoOrderStatus() {

        return soOrderStatus;
    }

    public void setSoOrderStatus(String SO_ORDR_STTS) {

        this.soOrderStatus = SO_ORDR_STTS;
    }

    public String getReportParameter() {

        return reportParameter;
    }

    public void setReportParameter(String REPORT_PARAMETER) {

        this.reportParameter = REPORT_PARAMETER;
    }

    public Integer getSoAdBranch() {

        return soAdBranch;
    }

    public void setSoAdBranch(Integer SO_AD_BRNCH) {

        this.soAdBranch = SO_AD_BRNCH;
    }

    public Integer getSoAdCompany() {

        return soAdCompany;
    }

    public void setSoAdCompany(Integer SO_AD_CMPNY) {

        this.soAdCompany = SO_AD_CMPNY;
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
    public List getArSalesOrderLines() {

        return arSalesOrderLines;
    }

    public void setArSalesOrderLines(List arSalesOrderLines) {

        this.arSalesOrderLines = arSalesOrderLines;
    }

    @XmlTransient
    public List getCmAdjustments() {

        return cmAdjustments;
    }

    public void setCmAdjustments(List cmAdjustments) {

        this.cmAdjustments = cmAdjustments;
    }

    @XmlTransient
    public List getArDeliveries() {

        return arDeliveries;
    }

    public void setArDeliveries(List arDeliveries) {

        this.arDeliveries = arDeliveries;
    }

    public void addArSalesOrderLine(LocalArSalesOrderLine entity) {

        try {
            entity.setArSalesOrder(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArSalesOrderLine(LocalArSalesOrderLine entity) {

        try {
            entity.setArSalesOrder(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}