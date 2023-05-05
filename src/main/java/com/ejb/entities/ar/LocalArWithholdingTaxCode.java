package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.gl.LocalGlChartOfAccount;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;

@Entity(name = "ArWithholdingTaxCode")
@Table(name = "AR_WTHHLDNG_TX_CD")
public class LocalArWithholdingTaxCode extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_WTC_CODE", nullable = false)
    private Integer wtcCode;
    @Column(name = "WTC_NM", columnDefinition = "VARCHAR")
    private String wtcName;
    @Column(name = "WTC_DESC", columnDefinition = "VARCHAR")
    private String wtcDescription;
    @Column(name = "WTC_RT", columnDefinition = "DOUBLE")
    private double wtcRate = 0;
    @Column(name = "WTC_ENBL", columnDefinition = "TINYINT")
    private byte wtcEnable;
    @Column(name = "WTC_AD_CMPNY", columnDefinition = "INT")
    private Integer wtcAdCompany;
    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;
    @OneToMany(mappedBy = "arWithholdingTaxCode", fetch = FetchType.LAZY)
    private List<LocalArCustomerClass> arCustomerClasses;
    @OneToMany(mappedBy = "arWithholdingTaxCode", fetch = FetchType.LAZY)
    private List<LocalArJobOrderType> arJobOrderTypes;
    @OneToMany(mappedBy = "arWithholdingTaxCode", fetch = FetchType.LAZY)
    private List<LocalArInvoice> arInvoices;
    @OneToMany(mappedBy = "arWithholdingTaxCode", fetch = FetchType.LAZY)
    private List<LocalArReceipt> arReceipts;
    @OneToMany(mappedBy = "arWithholdingTaxCode", fetch = FetchType.LAZY)
    private List<LocalAdPreference> adPreferences;
    @OneToMany(mappedBy = "arWithholdingTaxCode", fetch = FetchType.LAZY)
    private List<LocalArPdc> arPdcs;

    public LocalArWithholdingTaxCode() {

    }

    public Integer getWtcCode() {

        return wtcCode;
    }

    public void setWtcCode(Integer AR_WTC_CODE) {

        this.wtcCode = AR_WTC_CODE;
    }

    public String getWtcName() {

        return wtcName;
    }

    public void setWtcName(String WTC_NM) {

        this.wtcName = WTC_NM;
    }

    public String getWtcDescription() {

        return wtcDescription;
    }

    public void setWtcDescription(String WTC_DESC) {

        this.wtcDescription = WTC_DESC;
    }

    public double getWtcRate() {

        return wtcRate;
    }

    public void setWtcRate(double WTC_RT) {

        this.wtcRate = WTC_RT;
    }

    public byte getWtcEnable() {

        return wtcEnable;
    }

    public void setWtcEnable(byte WTC_ENBL) {

        this.wtcEnable = WTC_ENBL;
    }

    public Integer getWtcAdCompany() {

        return wtcAdCompany;
    }

    public void setWtcAdCompany(Integer WTC_AD_CMPNY) {

        this.wtcAdCompany = WTC_AD_CMPNY;
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
    public List getAdPreferences() {

        return adPreferences;
    }

    public void setAdPreferences(List adPreferences) {

        this.adPreferences = adPreferences;
    }

    @XmlTransient
    public List getArPdcs() {

        return arPdcs;
    }

    public void setArPdcs(List arPdcs) {

        this.arPdcs = arPdcs;
    }

    public void addArCustomerClass(LocalArCustomerClass entity) {

        try {
            entity.setArWithholdingTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArCustomerClass(LocalArCustomerClass entity) {

        try {
            entity.setArWithholdingTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArInvoice(LocalArInvoice entity) {

        try {
            entity.setArWithholdingTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArInvoice(LocalArInvoice entity) {

        try {
            entity.setArWithholdingTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArReceipt(LocalArReceipt entity) {

        try {
            entity.setArWithholdingTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArReceipt(LocalArReceipt entity) {

        try {
            entity.setArWithholdingTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdPreference(LocalAdPreference entity) {

        try {
            entity.setArWithholdingTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdPreference(LocalAdPreference entity) {

        try {
            entity.setArWithholdingTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArPdc(LocalArPdc entity) {

        try {
            entity.setArWithholdingTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArPdc(LocalArPdc entity) {

        try {
            entity.setArWithholdingTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}