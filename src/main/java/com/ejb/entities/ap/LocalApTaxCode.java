package com.ejb.entities.ap;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdBranchApTaxCode;
import com.ejb.entities.gl.LocalGlChartOfAccount;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;

@Entity(name = "ApTaxCode")
@Table(name = "AP_TX_CD")
public class LocalApTaxCode extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AP_TC_CODE", nullable = false)
    private Integer tcCode;

    @Column(name = "TC_NM", columnDefinition = "VARCHAR")
    private String tcName;

    @Column(name = "TC_DESC", columnDefinition = "VARCHAR")
    private String tcDescription;

    @Column(name = "TC_TYP", columnDefinition = "VARCHAR")
    private String tcType;

    @Column(name = "TC_RT", columnDefinition = "DOUBLE")
    private double tcRate = 0;

    @Column(name = "TC_ENBL", columnDefinition = "TINYINT")
    private byte tcEnable;

    @Column(name = "TC_AD_CMPNY", columnDefinition = "INT")
    private Integer tcAdCompany;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    @OneToMany(mappedBy = "apTaxCode", fetch = FetchType.LAZY)
    private List<LocalApSupplierClass> apSupplierClasses;

    @OneToMany(mappedBy = "apTaxCode", fetch = FetchType.LAZY)
    private List<LocalApVoucher> apVouchers;

    @OneToMany(mappedBy = "apTaxCode", fetch = FetchType.LAZY)
    private List<LocalApCheck> apChecks;

    @OneToMany(mappedBy = "apTaxCode", fetch = FetchType.LAZY)
    private List<LocalApRecurringVoucher> apRecurringVouchers;

    @OneToMany(mappedBy = "apTaxCode", fetch = FetchType.LAZY)
    private List<LocalApPurchaseOrder> apPurchaseOrders;

    @OneToMany(mappedBy = "apTaxCode", fetch = FetchType.LAZY)
    private List<LocalApPurchaseRequisition> apPurchaseRequisitions;

    @OneToMany(mappedBy = "apTaxCode", fetch = FetchType.LAZY)
    private List<LocalAdBranchApTaxCode> adBranchApTaxCodes;

    public Integer getTcCode() {

        return tcCode;
    }

    public void setTcCode(Integer AP_TC_CODE) {

        this.tcCode = AP_TC_CODE;
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
    public List getApSupplierClasses() {

        return apSupplierClasses;
    }

    public void setApSupplierClasses(List apSupplierClasses) {

        this.apSupplierClasses = apSupplierClasses;
    }

    @XmlTransient
    public List getApVouchers() {

        return apVouchers;
    }

    public void setApVouchers(List apVouchers) {

        this.apVouchers = apVouchers;
    }

    @XmlTransient
    public List getApChecks() {

        return apChecks;
    }

    public void setApChecks(List apChecks) {

        this.apChecks = apChecks;
    }

    @XmlTransient
    public List getApRecurringVouchers() {

        return apRecurringVouchers;
    }

    public void setApRecurringVouchers(List apRecurringVouchers) {

        this.apRecurringVouchers = apRecurringVouchers;
    }

    @XmlTransient
    public List getApPurchaseOrders() {

        return apPurchaseOrders;
    }

    public void setApPurchaseOrders(List apPurchaseOrders) {

        this.apPurchaseOrders = apPurchaseOrders;
    }

    @XmlTransient
    public List getApPurchaseRequisitions() {

        return apPurchaseRequisitions;
    }

    public void setApPurchaseRequisitions(List apPurchaseRequisitions) {

        this.apPurchaseRequisitions = apPurchaseRequisitions;
    }

    @XmlTransient
    public List getAdBranchApTaxCodes() {

        return adBranchApTaxCodes;
    }

    public void setAdBranchApTaxCodes(List adBranchApTaxCodes) {

        this.adBranchApTaxCodes = adBranchApTaxCodes;
    }

    public void addApSupplierClass(LocalApSupplierClass entity) {

        try {
            entity.setApTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApSupplierClass(LocalApSupplierClass entity) {

        try {
            entity.setApTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApVoucher(LocalApVoucher entity) {

        try {
            entity.setApTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApVoucher(LocalApVoucher entity) {

        try {
            entity.setApTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApCheck(LocalApCheck entity) {

        try {
            entity.setApTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApCheck(LocalApCheck entity) {

        try {
            entity.setApTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApRecurringVoucher(LocalApRecurringVoucher entity) {

        try {
            entity.setApTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApRecurringVoucher(LocalApRecurringVoucher entity) {

        try {
            entity.setApTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApPurchaseOrder(LocalApPurchaseOrder entity) {

        try {
            entity.setApTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApPurchaseOrder(LocalApPurchaseOrder entity) {

        try {
            entity.setApTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApPurchaseRequisition(LocalApPurchaseRequisition entity) {

        try {
            entity.setApTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApPurchaseRequisition(LocalApPurchaseRequisition entity) {

        try {
            entity.setApTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchApTaxCode(LocalAdBranchApTaxCode entity) {

        try {
            entity.setApTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchApTaxCode(LocalAdBranchApTaxCode entity) {

        try {
            entity.setApTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}