package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ar.LocalArTaxCode;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdBranchArTaxCode")
@Table(name = "AD_BR_AR_TC")
public class LocalAdBranchArTaxCode extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_BTC_CODE", nullable = false)
    private Integer btcCode;

    @Column(name = "BTC_COA_GL_TX_CD", columnDefinition = "INT")
    private Integer btcGlCoaTaxCode;

    @Column(name = "BTC_COA_GL_INTRM_CD", columnDefinition = "INT")
    private Integer btcGlCoaInterimCode;

    @Column(name = "BTC_DWNLD_STATUS", columnDefinition = "VARCHAR")
    private char btcDownloadStatus;

    @Column(name = "BTC_AD_CMPNY", columnDefinition = "INT")
    private Integer btcAdCompany;

    @JoinColumn(name = "AD_BRANCH", referencedColumnName = "BR_CODE")
    @ManyToOne
    private LocalAdBranch adBranch;

    @JoinColumn(name = "AR_TAX_CODE", referencedColumnName = "AR_TC_CODE")
    @ManyToOne
    private LocalArTaxCode arTaxCode;

    public Integer getBtcCode() {

        return btcCode;
    }

    public void setBtcCode(Integer AR_BTC_CODE) {

        this.btcCode = AR_BTC_CODE;
    }

    public Integer getBtcGlCoaTaxCode() {

        return btcGlCoaTaxCode;
    }

    public void setBtcGlCoaTaxCode(Integer BTC_COA_GL_TX_CD) {

        this.btcGlCoaTaxCode = BTC_COA_GL_TX_CD;
    }

    public Integer getBtcGlCoaInterimCode() {

        return btcGlCoaInterimCode;
    }

    public void setBtcGlCoaInterimCode(Integer BTC_COA_GL_INTRM_CD) {

        this.btcGlCoaInterimCode = BTC_COA_GL_INTRM_CD;
    }

    public char getBtcDownloadStatus() {

        return btcDownloadStatus;
    }

    public void setBtcDownloadStatus(char BTC_DWNLD_STATUS) {

        this.btcDownloadStatus = BTC_DWNLD_STATUS;
    }

    public Integer getBtcAdCompany() {

        return btcAdCompany;
    }

    public void setBtcAdCompany(Integer BTC_AD_CMPNY) {

        this.btcAdCompany = BTC_AD_CMPNY;
    }

    public LocalAdBranch getAdBranch() {

        return adBranch;
    }

    public void setAdBranch(LocalAdBranch adBranch) {

        this.adBranch = adBranch;
    }

    public LocalArTaxCode getArTaxCode() {

        return arTaxCode;
    }

    public void setArTaxCode(LocalArTaxCode arTaxCode) {

        this.arTaxCode = arTaxCode;
    }

}