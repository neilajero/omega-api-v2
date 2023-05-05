package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "InvDistributionRecord")
@Table(name = "INV_DSTRBTN_RCRD")
public class LocalInvDistributionRecord extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INV_DR_CODE", nullable = false)
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

    @JoinColumn(name = "INV_ADJUSTMENT", referencedColumnName = "INV_ADJ_CODE")
    @ManyToOne
    private LocalInvAdjustment invAdjustment;

    @JoinColumn(name = "INV_BRANCH_STOCK_TRANSFER", referencedColumnName = "BST_CODE")
    @ManyToOne
    private LocalInvBranchStockTransfer invBranchStockTransfer;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount invChartOfAccount;

    @JoinColumn(name = "INV_STOCK_TRANSFER", referencedColumnName = "INV_ST_CODE")
    @ManyToOne
    private LocalInvStockTransfer invStockTransfer;

    public Integer getDrCode() {

        return drCode;
    }

    public void setDrCode(Integer INV_DR_CODE) {

        this.drCode = INV_DR_CODE;
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

    public LocalInvAdjustment getInvAdjustment() {

        return invAdjustment;
    }

    public void setInvAdjustment(LocalInvAdjustment invAdjustment) {

        this.invAdjustment = invAdjustment;
    }

    public LocalInvBranchStockTransfer getInvBranchStockTransfer() {

        return invBranchStockTransfer;
    }

    public void setInvBranchStockTransfer(LocalInvBranchStockTransfer invBranchStockTransfer) {

        this.invBranchStockTransfer = invBranchStockTransfer;
    }

    public LocalGlChartOfAccount getInvChartOfAccount() {

        return invChartOfAccount;
    }

    public void setInvChartOfAccount(LocalGlChartOfAccount invChartOfAccount) {

        this.invChartOfAccount = invChartOfAccount;
    }

    public LocalInvStockTransfer getInvStockTransfer() {

        return invStockTransfer;
    }

    public void setInvStockTransfer(LocalInvStockTransfer invStockTransfer) {

        this.invStockTransfer = invStockTransfer;
    }

}