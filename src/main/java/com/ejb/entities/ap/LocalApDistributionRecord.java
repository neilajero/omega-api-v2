package com.ejb.entities.ap;

import com.ejb.NativeQueryHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "ApDistributionRecord")
@Table(name = "AP_DSTRBTN_RCRD")
public class LocalApDistributionRecord extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AP_DR_CODE", nullable = false)
    private Integer drCode;

    @Column(name = "DR_LN", columnDefinition = "SMALLINT")
    private short drLine;

    @Column(name = "DR_CLSS", columnDefinition = "VARCHAR")
    private String drClass;

    @Column(name = "DR_DBT", columnDefinition = "TINYINT")
    private byte drDebit;

    @Column(name = "DR_AMNT", columnDefinition = "DOUBLE")
    private double drAmount = 0;

    @Column(name = "DR_IMPRTD", columnDefinition = "TINYINT")
    private byte drImported;

    @Column(name = "DR_RVRSD", columnDefinition = "TINYINT")
    private byte drReversed;

    @Column(name = "DR_AD_CMPNY", columnDefinition = "INT")
    private Integer drAdCompany;

    @JoinColumn(name = "AP_APPLIED_VOUCHER", referencedColumnName = "AP_AV_CODE")
    @ManyToOne
    private LocalApAppliedVoucher apAppliedVoucher;

    @JoinColumn(name = "AP_CHECK", referencedColumnName = "CHK_CODE")
    @ManyToOne
    private LocalApCheck apCheck;

    @JoinColumn(name = "AP_PURCHASE_ORDER", referencedColumnName = "PO_CODE")
    @ManyToOne
    private LocalApPurchaseOrder apPurchaseOrder;

    @JoinColumn(name = "AP_RECURRING_VOUCHER", referencedColumnName = "AP_RV_CODE")
    @ManyToOne
    private LocalApRecurringVoucher apRecurringVoucher;

    @JoinColumn(name = "AP_VOUCHER", referencedColumnName = "VOU_CODE")
    @ManyToOne
    private LocalApVoucher apVoucher;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    public Integer getDrCode() {

        return drCode;
    }

    public void setDrCode(Integer AP_DR_CODE) {

        this.drCode = AP_DR_CODE;
    }

    public short getDrLine() {

        return drLine;
    }

    public void setDrLine(short DR_LN) {

        this.drLine = DR_LN;
    }

    public String getDrClass() {

        return drClass;
    }

    public void setDrClass(String DR_CLSS) {

        this.drClass = DR_CLSS;
    }

    public byte getDrDebit() {

        return drDebit;
    }

    public void setDrDebit(byte DR_DBT) {

        this.drDebit = DR_DBT;
    }

    public double getDrAmount() {

        return drAmount;
    }

    public void setDrAmount(double DR_AMNT) {

        this.drAmount = DR_AMNT;
    }

    public byte getDrImported() {

        return drImported;
    }

    public void setDrImported(byte DR_IMPRTD) {

        this.drImported = DR_IMPRTD;
    }

    public byte getDrReversed() {

        return drReversed;
    }

    public void setDrReversed(byte DR_RVRSD) {

        this.drReversed = DR_RVRSD;
    }

    public Integer getDrAdCompany() {

        return drAdCompany;
    }

    public void setDrAdCompany(Integer DR_AD_CMPNY) {

        this.drAdCompany = DR_AD_CMPNY;
    }

    public LocalApAppliedVoucher getApAppliedVoucher() {

        return apAppliedVoucher;
    }

    public void setApAppliedVoucher(LocalApAppliedVoucher apAppliedVoucher) {

        this.apAppliedVoucher = apAppliedVoucher;
    }

    public LocalApCheck getApCheck() {

        return apCheck;
    }

    public void setApCheck(LocalApCheck apCheck) {

        this.apCheck = apCheck;
    }

    public LocalApPurchaseOrder getApPurchaseOrder() {

        return apPurchaseOrder;
    }

    public void setApPurchaseOrder(LocalApPurchaseOrder apPurchaseOrder) {

        this.apPurchaseOrder = apPurchaseOrder;
    }

    public LocalApRecurringVoucher getApRecurringVoucher() {

        return apRecurringVoucher;
    }

    public void setApRecurringVoucher(LocalApRecurringVoucher apRecurringVoucher) {

        this.apRecurringVoucher = apRecurringVoucher;
    }

    public LocalApVoucher getApVoucher() {

        return apVoucher;
    }

    public void setApVoucher(LocalApVoucher apVoucher) {

        this.apVoucher = apVoucher;
    }

    public LocalGlChartOfAccount getGlChartOfAccount() {

        return glChartOfAccount;
    }

    public void setGlChartOfAccount(LocalGlChartOfAccount glChartOfAccount) {

        this.glChartOfAccount = glChartOfAccount;
    }

}