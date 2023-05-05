package com.ejb.entities.ap;

import com.ejb.NativeQueryHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ApWithholdingTaxCode")
@Table(name = "AP_WTHHLDNG_TX_CD")
public class LocalApWithholdingTaxCode extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AP_WTC_CODE", nullable = false)
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

    @OneToMany(mappedBy = "apWithholdingTaxCode", fetch = FetchType.LAZY)
    private List<LocalApSupplierClass> apSupplierClasses;

    @OneToMany(mappedBy = "apWithholdingTaxCode", fetch = FetchType.LAZY)
    private List<LocalApVoucher> apVouchers;

    @OneToMany(mappedBy = "apWithholdingTaxCode", fetch = FetchType.LAZY)
    private List<LocalApCheck> apChecks;

    @OneToMany(mappedBy = "apWithholdingTaxCode", fetch = FetchType.LAZY)
    private List<LocalApRecurringVoucher> apRecurringVouchers;

    public Integer getWtcCode() {

        return wtcCode;
    }

    public void setWtcCode(Integer AP_WTC_CODE) {

        this.wtcCode = AP_WTC_CODE;
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

    public void addApSupplierClass(LocalApSupplierClass entity) {

        try {
            entity.setApWithholdingTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApSupplierClass(LocalApSupplierClass entity) {

        try {
            entity.setApWithholdingTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApVoucher(LocalApVoucher entity) {

        try {
            entity.setApWithholdingTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApVoucher(LocalApVoucher entity) {

        try {
            entity.setApWithholdingTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApCheck(LocalApCheck entity) {

        try {
            entity.setApWithholdingTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApCheck(LocalApCheck entity) {

        try {
            entity.setApWithholdingTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApRecurringVoucher(LocalApRecurringVoucher entity) {

        try {
            entity.setApWithholdingTaxCode(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApRecurringVoucher(LocalApRecurringVoucher entity) {

        try {
            entity.setApWithholdingTaxCode(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}