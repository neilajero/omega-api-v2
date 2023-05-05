package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "ArDistributionRecord")
@Table(name = "AR_DSTRBTN_RCRD")
public class LocalArDistributionRecord extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_DR_CODE", nullable = false)
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

    @Column(name = "DR_SC_ACCNT", columnDefinition = "INT")
    private Integer drScAccount;

    @Column(name = "DR_AD_CMPNY", columnDefinition = "INT")
    private Integer drAdCompany;

    @JoinColumn(name = "AR_APPLIED_INVOICE", referencedColumnName = "AI_CODE")
    @ManyToOne
    private LocalArAppliedInvoice arAppliedInvoice;

    @JoinColumn(name = "AR_INVOICE", referencedColumnName = "INV_CODE")
    @ManyToOne
    private LocalArInvoice arInvoice;

    @JoinColumn(name = "AR_RECEIPT", referencedColumnName = "RCT_CODE")
    @ManyToOne
    private LocalArReceipt arReceipt;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    public Integer getDrCode() {

        return drCode;
    }

    public void setDrCode(Integer AR_DR_CODE) {

        this.drCode = AR_DR_CODE;
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

    public Integer getDrScAccount() {

        return drScAccount;
    }

    public void setDrScAccount(Integer DR_SC_ACCNT) {

        this.drScAccount = DR_SC_ACCNT;
    }

    public Integer getDrAdCompany() {

        return drAdCompany;
    }

    public void setDrAdCompany(Integer DR_AD_CMPNY) {

        this.drAdCompany = DR_AD_CMPNY;
    }

    public LocalArAppliedInvoice getArAppliedInvoice() {

        return arAppliedInvoice;
    }

    public void setArAppliedInvoice(LocalArAppliedInvoice arAppliedInvoice) {

        this.arAppliedInvoice = arAppliedInvoice;
    }

    public LocalArInvoice getArInvoice() {

        return arInvoice;
    }

    public void setArInvoice(LocalArInvoice arInvoice) {

        this.arInvoice = arInvoice;
    }

    public LocalArReceipt getArReceipt() {

        return arReceipt;
    }

    public void setArReceipt(LocalArReceipt arReceipt) {

        this.arReceipt = arReceipt;
    }

    public LocalGlChartOfAccount getGlChartOfAccount() {

        return glChartOfAccount;
    }

    public void setGlChartOfAccount(LocalGlChartOfAccount glChartOfAccount) {

        this.glChartOfAccount = glChartOfAccount;
    }

}