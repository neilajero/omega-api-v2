package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdBranchArTaxCode;
import com.ejb.entities.gl.LocalGlChartOfAccount;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;

@Entity(name = "ArTaxCode")
@Table(name = "AR_TX_CD")
public class LocalArTaxCode extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_TC_CODE", nullable = false)
    private Integer tcCode;

    @Column(name = "TC_NM", columnDefinition = "VARCHAR")
    private String tcName;

    @Column(name = "TC_DESC", columnDefinition = "VARCHAR")
    private String tcDescription;

    @Column(name = "TC_TYP", columnDefinition = "VARCHAR")
    private String tcType;

    @Column(name = "TC_INTRM_ACCNT", columnDefinition = "INT")
    private Integer tcInterimAccount;

    @Column(name = "TC_RT", columnDefinition = "DOUBLE")
    private double tcRate = 0;

    @Column(name = "TC_ENBL", columnDefinition = "TINYINT")
    private byte tcEnable;

    @Column(name = "TC_AD_CMPNY", columnDefinition = "INT")
    private Integer tcAdCompany;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    @OneToMany(mappedBy = "arTaxCode", fetch = FetchType.LAZY)
    private List<LocalArCustomerClass> arCustomerClasses;

    @OneToMany(mappedBy = "arTaxCode", fetch = FetchType.LAZY)
    private List<LocalArJobOrderType> arJobOrderTypes;

    @OneToMany(mappedBy = "arTaxCode", fetch = FetchType.LAZY)
    private List<LocalArInvoice> arInvoices;

    @OneToMany(mappedBy = "arTaxCode", fetch = FetchType.LAZY)
    private List<LocalArReceipt> arReceipts;

    @OneToMany(mappedBy = "arTaxCode", fetch = FetchType.LAZY)
    private List<LocalArJobOrder> arJobOrders;

    @OneToMany(mappedBy = "arTaxCode", fetch = FetchType.LAZY)
    private List<LocalArPdc> arPdcs;

    @OneToMany(mappedBy = "arTaxCode", fetch = FetchType.LAZY)
    private List<LocalArSalesOrder> arSalesOrders;

    @OneToMany(mappedBy = "arTaxCode", fetch = FetchType.LAZY)
    private List<LocalAdBranchArTaxCode> adBranchArTaxCodes;

    public Integer getTcCode() {

        return tcCode;
    }

    public void setTcCode(Integer AR_TC_CODE) {

        this.tcCode = AR_TC_CODE;
    }

    public String getTcName() {

        return tcName;
    }

    public void setTcName(String TC_NM) {

        this.tcName = TC_NM;
    }

    public String getTcDescription() {

        return tcDescription;
    }

    public void setTcDescription(String TC_DESC) {

        this.tcDescription = TC_DESC;
    }

    public String getTcType() {

        return tcType;
    }

    public void setTcType(String TC_TYP) {

        this.tcType = TC_TYP;
    }

    public Integer getTcInterimAccount() {

        return tcInterimAccount;
    }

    public void setTcInterimAccount(Integer TC_INTRM_ACCNT) {

        this.tcInterimAccount = TC_INTRM_ACCNT;
    }

    public double getTcRate() {

        return tcRate;
    }

    public void setTcRate(double TC_RT) {

        this.tcRate = TC_RT;
    }

    public byte getTcEnable() {

        return tcEnable;
    }

    public void setTcEnable(byte TC_ENBL) {

        this.tcEnable = TC_ENBL;
    }

    public Integer getTcAdCompany() {

        return tcAdCompany;
    }

    public void setTcAdCompany(Integer TC_AD_CMPNY) {

        this.tcAdCompany = TC_AD_CMPNY;
    }

    public LocalGlChartOfAccount getGlChartOfAccount() {

        return glChartOfAccount;
    }

    public void setGlChartOfAccount(LocalGlChartOfAccount glChartOfAccount) {

        this.glChartOfAccount = glChartOfAccount;
    }

    @XmlTransient
    public List getArCustomerClasses() {

        return arCustomerClasses;
    }

    public void setArCustomerClasses(List arCustomerClasses) {

        this.arCustomerClasses = arCustomerClasses;
    }

    @XmlTransient
    public List getArJobOrderTypes() {

        return arJobOrderTypes;
    }

    public void setArJobOrderTypes(List arJobOrderTypes) {

        this.arJobOrderTypes = arJobOrderTypes;
    }

    @XmlTransient
    public List getArInvoices() {

        return arInvoices;
    }

    public void setArInvoices(List arInvoices) {

        this.arInvoices = arInvoices;
    }

    @XmlTransient
    public List getArReceipts() {

        return arReceipts;
    }

    public void setArReceipts(List arReceipts) {

        this.arReceipts = arReceipts;
    }

    @XmlTransient
    public List getArJobOrders() {

        return arJobOrders;
    }

    public void setArJobOrders(List arJobOrders) {

        this.arJobOrders = arJobOrders;
    }

    @XmlTransient
    public List getArPdcs() {

        return arPdcs;
    }

    public void setArPdcs(List arPdcs) {

        this.arPdcs = arPdcs;
    }

    @XmlTransient
    public List getArSalesOrders() {

        return arSalesOrders;
    }

    public void setArSalesOrders(List arSalesOrders) {

        this.arSalesOrders = arSalesOrders;
    }

    @XmlTransient
    public List getAdBranchArTaxCodes() {

        return adBranchArTaxCodes;
    }

    public void setAdBranchArTaxCodes(List adBranchArTaxCodes) {

        this.adBranchArTaxCodes = adBranchArTaxCodes;
    }

    public void addArCustomerClass(LocalArCustomerClass entity) {

        try {
            entity.setArTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArCustomerClass(LocalArCustomerClass entity) {

        try {
            entity.setArTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArInvoice(LocalArInvoice entity) {

        try {
            entity.setArTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArInvoice(LocalArInvoice entity) {

        try {
            entity.setArTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArReceipt(LocalArReceipt entity) {

        try {
            entity.setArTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArReceipt(LocalArReceipt entity) {

        try {
            entity.setArTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArPdc(LocalArPdc entity) {

        try {
            entity.setArTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArPdc(LocalArPdc entity) {

        try {
            entity.setArTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArSalesOrder(LocalArSalesOrder entity) {

        try {
            entity.setArTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArSalesOrder(LocalArSalesOrder entity) {

        try {
            entity.setArTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchArTaxCode(LocalAdBranchArTaxCode entity) {

        try {
            entity.setArTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchArTaxCode(LocalAdBranchArTaxCode entity) {

        try {
            entity.setArTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}