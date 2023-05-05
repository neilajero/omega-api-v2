package com.ejb.entities.cm;

import com.ejb.NativeQueryHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "CmDistributionRecord")
@Table(name = "CM_DSTRBTN_RCRD")
public class LocalCmDistributionRecord extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CM_DR_CODE", nullable = false)
    private Integer drCode;

    @Column(name = "DR_LN", columnDefinition = "SMALLINT")
    private short drLine;

    @Column(name = "DR_CLSS", columnDefinition = "VARCHAR")
    private String drClass;

    @Column(name = "DR_AMNT", columnDefinition = "DOUBLE")
    private double drAmount = 0;

    @Column(name = "DR_DBT", columnDefinition = "TINYINT")
    private byte drDebit;

    @Column(name = "DR_RVRSL", columnDefinition = "TINYINT")
    private byte drReversal;

    @Column(name = "DR_IMPRTD", columnDefinition = "TINYINT")
    private byte drImported;

    @Column(name = "DR_AD_CMPNY", columnDefinition = "INT")
    private Integer drAdCompany;

    @JoinColumn(name = "CM_ADJUSTMENT", referencedColumnName = "CM_ADJ_CODE")
    @ManyToOne
    private LocalCmAdjustment cmAdjustment;

    @JoinColumn(name = "CM_FUND_TRANSFER", referencedColumnName = "FT_CODE")
    @ManyToOne
    private LocalCmFundTransfer cmFundTransfer;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    public Integer getDrCode() {

        return drCode;
    }

    public void setDrCode(Integer CM_DR_CODE) {

        this.drCode = CM_DR_CODE;
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

    public double getDrAmount() {

        return drAmount;
    }

    public void setDrAmount(double DR_AMNT) {

        this.drAmount = DR_AMNT;
    }

    public byte getDrDebit() {

        return drDebit;
    }

    public void setDrDebit(byte DR_DBT) {

        this.drDebit = DR_DBT;
    }

    public byte getDrReversal() {

        return drReversal;
    }

    public void setDrReversal(byte DR_RVRSL) {

        this.drReversal = DR_RVRSL;
    }

    public byte getDrImported() {

        return drImported;
    }

    public void setDrImported(byte DR_IMPRTD) {

        this.drImported = DR_IMPRTD;
    }

    public Integer getDrAdCompany() {

        return drAdCompany;
    }

    public void setDrAdCompany(Integer DR_AD_CMPNY) {

        this.drAdCompany = DR_AD_CMPNY;
    }

    public LocalCmAdjustment getCmAdjustment() {

        return cmAdjustment;
    }

    public void setCmAdjustment(LocalCmAdjustment cmAdjustment) {

        this.cmAdjustment = cmAdjustment;
    }

    public LocalCmFundTransfer getCmFundTransfer() {

        return cmFundTransfer;
    }

    public void setCmFundTransfer(LocalCmFundTransfer cmFundTransfer) {

        this.cmFundTransfer = cmFundTransfer;
    }

    public LocalGlChartOfAccount getGlChartOfAccount() {

        return glChartOfAccount;
    }

    public void setGlChartOfAccount(LocalGlChartOfAccount glChartOfAccount) {

        this.glChartOfAccount = glChartOfAccount;
    }

}