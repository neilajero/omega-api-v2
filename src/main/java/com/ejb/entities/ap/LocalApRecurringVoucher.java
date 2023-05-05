package com.ejb.entities.ap;

import com.ejb.NativeQueryHome;
import com.ejb.dao.ap.LocalApVoucherBatch;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "ApRecurringVoucher")
@Table(name = "AP_RCRRNG_VCHR")
public class LocalApRecurringVoucher extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AP_RV_CODE", nullable = false)
    private Integer rvCode;

    @Column(name = "RV_NM", columnDefinition = "VARCHAR")
    private String rvName;

    @Column(name = "RV_DESC", columnDefinition = "VARCHAR")
    private String rvDescription;

    @Column(name = "RV_CNVRSN_DT", columnDefinition = "DATETIME")
    private Date rvConversionDate;

    @Column(name = "RV_CNVRSN_RT", columnDefinition = "DOUBLE")
    private double rvConversionRate = 0;

    @Column(name = "RV_AMNT", columnDefinition = "DOUBLE")
    private double rvAmount = 0;

    @Column(name = "RV_AMNT_DUE", columnDefinition = "DOUBLE")
    private double rvAmountDue = 0;

    @Column(name = "RV_AD_USR_NM1", columnDefinition = "INT")
    private Integer rvAdUserName1;

    @Column(name = "RV_AD_USR_NM2", columnDefinition = "INT")
    private Integer rvAdUserName2;

    @Column(name = "RV_AD_USR_NM3", columnDefinition = "INT")
    private Integer rvAdUserName3;

    @Column(name = "RV_AD_USR_NM4", columnDefinition = "INT")
    private Integer rvAdUserName4;

    @Column(name = "RV_AD_USR_NM5", columnDefinition = "INT")
    private Integer rvAdUserName5;

    @Column(name = "RV_SCHDL", columnDefinition = "VARCHAR")
    private String rvSchedule;

    @Column(name = "RV_NXT_RN_DT", columnDefinition = "DATETIME")
    private Date rvNextRunDate;

    @Column(name = "RV_LST_RN_DT", columnDefinition = "DATETIME")
    private Date rvLastRunDate;

    @Column(name = "RV_AD_BRNCH", columnDefinition = "INT")
    private Integer rvAdBranch;

    @Column(name = "RV_AD_CMPNY", columnDefinition = "INT")
    private Integer rvAdCompany;

    @JoinColumn(name = "AD_PAYMENT_TERM", referencedColumnName = "PYT_CODE")
    @ManyToOne
    private LocalAdPaymentTerm adPaymentTerm;

    @JoinColumn(name = "AP_SUPPLIER", referencedColumnName = "SPL_CODE")
    @ManyToOne
    private LocalApSupplier apSupplier;

    @JoinColumn(name = "AP_TAX_CODE", referencedColumnName = "AP_TC_CODE")
    @ManyToOne
    private LocalApTaxCode apTaxCode;

    @JoinColumn(name = "AP_VOUCHER_BATCH", referencedColumnName = "VB_CODE")
    @ManyToOne
    private LocalApVoucherBatch apVoucherBatch;

    @JoinColumn(name = "AP_WITHHOLDING_TAX_CODE", referencedColumnName = "AP_WTC_CODE")
    @ManyToOne
    private LocalApWithholdingTaxCode apWithholdingTaxCode;

    @JoinColumn(name = "GL_FUNCTIONAL_CURRENCY", referencedColumnName = "FC_CODE")
    @ManyToOne
    private LocalGlFunctionalCurrency glFunctionalCurrency;

    @OneToMany(mappedBy = "apRecurringVoucher", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalApDistributionRecord> apDistributionRecords;

    public Integer getRvCode() {

        return rvCode;
    }

    public void setRvCode(Integer AP_RV_CODE) {

        this.rvCode = AP_RV_CODE;
    }

    public String getRvName() {

        return rvName;
    }

    public void setRvName(String RV_NM) {

        this.rvName = RV_NM;
    }

    public String getRvDescription() {

        return rvDescription;
    }

    public void setRvDescription(String RV_DESC) {

        this.rvDescription = RV_DESC;
    }

    public Date getRvConversionDate() {

        return rvConversionDate;
    }

    public void setRvConversionDate(Date RV_CNVRSN_DT) {

        this.rvConversionDate = RV_CNVRSN_DT;
    }

    public double getRvConversionRate() {

        return rvConversionRate;
    }

    public void setRvConversionRate(double RV_CNVRSN_RT) {

        this.rvConversionRate = RV_CNVRSN_RT;
    }

    public double getRvAmount() {

        return rvAmount;
    }

    public void setRvAmount(double RV_AMNT) {

        this.rvAmount = RV_AMNT;
    }

    public double getRvAmountDue() {

        return rvAmountDue;
    }

    public void setRvAmountDue(double RV_AMNT_DUE) {

        this.rvAmountDue = RV_AMNT_DUE;
    }

    public Integer getRvAdUserName1() {

        return rvAdUserName1;
    }

    public void setRvAdUserName1(Integer RV_AD_USR_NM1) {

        this.rvAdUserName1 = RV_AD_USR_NM1;
    }

    public Integer getRvAdUserName2() {

        return rvAdUserName2;
    }

    public void setRvAdUserName2(Integer RV_AD_USR_NM2) {

        this.rvAdUserName2 = RV_AD_USR_NM2;
    }

    public Integer getRvAdUserName3() {

        return rvAdUserName3;
    }

    public void setRvAdUserName3(Integer RV_AD_USR_NM3) {

        this.rvAdUserName3 = RV_AD_USR_NM3;
    }

    public Integer getRvAdUserName4() {

        return rvAdUserName4;
    }

    public void setRvAdUserName4(Integer RV_AD_USR_NM4) {

        this.rvAdUserName4 = RV_AD_USR_NM4;
    }

    public Integer getRvAdUserName5() {

        return rvAdUserName5;
    }

    public void setRvAdUserName5(Integer RV_AD_USR_NM5) {

        this.rvAdUserName5 = RV_AD_USR_NM5;
    }

    public String getRvSchedule() {

        return rvSchedule;
    }

    public void setRvSchedule(String RV_SCHDL) {

        this.rvSchedule = RV_SCHDL;
    }

    public Date getRvNextRunDate() {

        return rvNextRunDate;
    }

    public void setRvNextRunDate(Date RV_NXT_RN_DT) {

        this.rvNextRunDate = RV_NXT_RN_DT;
    }

    public Date getRvLastRunDate() {

        return rvLastRunDate;
    }

    public void setRvLastRunDate(Date RV_LST_RN_DT) {

        this.rvLastRunDate = RV_LST_RN_DT;
    }

    public Integer getRvAdBranch() {

        return rvAdBranch;
    }

    public void setRvAdBranch(Integer RV_AD_BRNCH) {

        this.rvAdBranch = RV_AD_BRNCH;
    }

    public Integer getRvAdCompany() {

        return rvAdCompany;
    }

    public void setRvAdCompany(Integer RV_AD_CMPNY) {

        this.rvAdCompany = RV_AD_CMPNY;
    }

    public LocalAdPaymentTerm getAdPaymentTerm() {

        return adPaymentTerm;
    }

    public void setAdPaymentTerm(LocalAdPaymentTerm adPaymentTerm) {

        this.adPaymentTerm = adPaymentTerm;
    }

    public LocalApSupplier getApSupplier() {

        return apSupplier;
    }

    public void setApSupplier(LocalApSupplier apSupplier) {

        this.apSupplier = apSupplier;
    }

    public LocalApTaxCode getApTaxCode() {

        return apTaxCode;
    }

    public void setApTaxCode(LocalApTaxCode apTaxCode) {

        this.apTaxCode = apTaxCode;
    }

    public LocalApVoucherBatch getApVoucherBatch() {

        return apVoucherBatch;
    }

    public void setApVoucherBatch(LocalApVoucherBatch apVoucherBatch) {

        this.apVoucherBatch = apVoucherBatch;
    }

    public LocalApWithholdingTaxCode getApWithholdingTaxCode() {

        return apWithholdingTaxCode;
    }

    public void setApWithholdingTaxCode(LocalApWithholdingTaxCode apWithholdingTaxCode) {

        this.apWithholdingTaxCode = apWithholdingTaxCode;
    }

    public LocalGlFunctionalCurrency getGlFunctionalCurrency() {

        return glFunctionalCurrency;
    }

    public void setGlFunctionalCurrency(LocalGlFunctionalCurrency glFunctionalCurrency) {

        this.glFunctionalCurrency = glFunctionalCurrency;
    }

    @XmlTransient
    public List getApDistributionRecords() {

        return apDistributionRecords;
    }

    public void setApDistributionRecords(List apDistributionRecords) {

        this.apDistributionRecords = apDistributionRecords;
    }

    public void addApDistributionRecord(LocalApDistributionRecord entity) {

        try {
            entity.setApRecurringVoucher(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApDistributionRecord(LocalApDistributionRecord entity) {

        try {
            entity.setApRecurringVoucher(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}