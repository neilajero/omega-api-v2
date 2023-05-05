package com.ejb.entities.ap;

import com.ejb.NativeQueryHome;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ApAppliedVoucher")
@Table(name = "AP_APPLD_VCHR")
public class LocalApAppliedVoucher extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AP_AV_CODE", nullable = false)
    private Integer avCode;

    @Column(name = "AV_APPLY_AMNT", columnDefinition = "DOUBLE")
    private double avApplyAmount = 0;

    @Column(name = "AV_TX_WTHHLD", columnDefinition = "DOUBLE")
    private double avTaxWithheld = 0;

    @Column(name = "AV_DSCNT_AMNT", columnDefinition = "DOUBLE")
    private double avDiscountAmount = 0;

    @Column(name = "AV_ALLCTD_PYMNT_AMNT", columnDefinition = "DOUBLE")
    private double avAllocatedPaymentAmount = 0;

    @Column(name = "AV_FRX_GN_LSS", columnDefinition = "DOUBLE")
    private double avForexGainLoss = 0;

    @Column(name = "AV_AD_CMPNY", columnDefinition = "INT")
    private Integer avAdCompany;

    @JoinColumn(name = "AP_CHECK", referencedColumnName = "CHK_CODE")
    @ManyToOne
    private LocalApCheck apCheck;

    @JoinColumn(name = "AP_VOUCHER_PAYMENT_SCHEDULE", referencedColumnName = "VPS_CODE")
    @ManyToOne
    private LocalApVoucherPaymentSchedule apVoucherPaymentSchedule;

    @OneToMany(mappedBy = "apAppliedVoucher", fetch = FetchType.LAZY)
    private List<LocalApDistributionRecord> apDistributionRecords;

    public Integer getAvCode() {

        return avCode;
    }

    public void setAvCode(Integer AP_AV_CODE) {

        this.avCode = AP_AV_CODE;
    }

    public double getAvApplyAmount() {

        return avApplyAmount;
    }

    public void setAvApplyAmount(double AV_APPLY_AMNT) {

        this.avApplyAmount = AV_APPLY_AMNT;
    }

    public double getAvTaxWithheld() {

        return avTaxWithheld;
    }

    public void setAvTaxWithheld(double AV_TX_WTHHLD) {

        this.avTaxWithheld = AV_TX_WTHHLD;
    }

    public double getAvDiscountAmount() {

        return avDiscountAmount;
    }

    public void setAvDiscountAmount(double AV_DSCNT_AMNT) {

        this.avDiscountAmount = AV_DSCNT_AMNT;
    }

    public double getAvAllocatedPaymentAmount() {

        return avAllocatedPaymentAmount;
    }

    public void setAvAllocatedPaymentAmount(double AV_ALLCTD_PYMNT_AMNT) {

        this.avAllocatedPaymentAmount = AV_ALLCTD_PYMNT_AMNT;
    }

    public double getAvForexGainLoss() {

        return avForexGainLoss;
    }

    public void setAvForexGainLoss(double AV_FRX_GN_LSS) {

        this.avForexGainLoss = AV_FRX_GN_LSS;
    }

    public Integer getAvAdCompany() {

        return avAdCompany;
    }

    public void setAvAdCompany(Integer AV_AD_CMPNY) {

        this.avAdCompany = AV_AD_CMPNY;
    }

    public LocalApCheck getApCheck() {

        return apCheck;
    }

    public void setApCheck(LocalApCheck apCheck) {

        this.apCheck = apCheck;
    }

    public LocalApVoucherPaymentSchedule getApVoucherPaymentSchedule() {

        return apVoucherPaymentSchedule;
    }

    public void setApVoucherPaymentSchedule(LocalApVoucherPaymentSchedule apVoucherPaymentSchedule) {

        this.apVoucherPaymentSchedule = apVoucherPaymentSchedule;
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
            entity.setApAppliedVoucher(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApDistributionRecord(LocalApDistributionRecord entity) {

        try {
            entity.setApAppliedVoucher(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}