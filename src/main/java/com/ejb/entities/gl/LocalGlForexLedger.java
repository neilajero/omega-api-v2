package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "GlForexLedger")
@Table(name = "GL_FRX_LDGR")
public class LocalGlForexLedger extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FRL_CODE", nullable = false)
    private Integer frlCode;

    @Column(name = "FRL_DT", columnDefinition = "DATETIME")
    private Date frlDate;

    @Column(name = "FRL_LN", columnDefinition = "INT")
    private Integer frlLine;

    @Column(name = "FRL_TYP", columnDefinition = "VARCHAR")
    private String frlType;

    @Column(name = "FRL_AMNT", columnDefinition = "DOUBLE")
    private double frlAmount = 0;

    @Column(name = "FRL_RT", columnDefinition = "DOUBLE")
    private double frlRate = 0;

    @Column(name = "FRL_BLNC", columnDefinition = "DOUBLE")
    private double frlBalance = 0;

    @Column(name = "FRL_FRX_GN_LSS", columnDefinition = "DOUBLE")
    private double frlForexGainLoss = 0;

    @Column(name = "FRL_AD_CMPNY", columnDefinition = "INT")
    private Integer frlAdCompany;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    public Integer getFrlCode() {

        return frlCode;
    }

    public void setFrlCode(Integer FRL_CODE) {

        this.frlCode = FRL_CODE;
    }

    public Date getFrlDate() {

        return frlDate;
    }

    public void setFrlDate(Date FRL_DT) {

        this.frlDate = FRL_DT;
    }

    public Integer getFrlLine() {

        return frlLine;
    }

    public void setFrlLine(Integer FRL_LN) {

        this.frlLine = FRL_LN;
    }

    public String getFrlType() {

        return frlType;
    }

    public void setFrlType(String FRL_TYP) {

        this.frlType = FRL_TYP;
    }

    public double getFrlAmount() {

        return frlAmount;
    }

    public void setFrlAmount(double FRL_AMNT) {

        this.frlAmount = FRL_AMNT;
    }

    public double getFrlRate() {

        return frlRate;
    }

    public void setFrlRate(double FRL_RT) {

        this.frlRate = FRL_RT;
    }

    public double getFrlBalance() {

        return frlBalance;
    }

    public void setFrlBalance(double FRL_BLNC) {

        this.frlBalance = FRL_BLNC;
    }

    public double getFrlForexGainLoss() {

        return frlForexGainLoss;
    }

    public void setFrlForexGainLoss(double FRL_FRX_GN_LSS) {

        this.frlForexGainLoss = FRL_FRX_GN_LSS;
    }

    public Integer getFrlAdCompany() {

        return frlAdCompany;
    }

    public void setFrlAdCompany(Integer FRL_AD_CMPNY) {

        this.frlAdCompany = FRL_AD_CMPNY;
    }

    public LocalGlChartOfAccount getGlChartOfAccount() {

        return glChartOfAccount;
    }

    public void setGlChartOfAccount(LocalGlChartOfAccount glChartOfAccount) {

        this.glChartOfAccount = glChartOfAccount;
    }

}