package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ap.LocalApTaxCode;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdBranchApTaxCode")
@Table(name = "AD_BR_AP_TC")
public class LocalAdBranchApTaxCode extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AP_BTC_CODE", nullable = false)
    private Integer btcCode;

    @Column(name = "BTC_COA_GL_TX_CD", columnDefinition = "INT")
    private Integer btcGlCoaTaxCode;

    @Column(name = "BTC_DWNLD_STATUS", columnDefinition = "VARCHAR")
    private char btcDownloadStatus;

    @Column(name = "BTC_AD_CMPNY", columnDefinition = "INT")
    private Integer btcAdCompany;

    @JoinColumn(name = "AD_BRANCH", referencedColumnName = "BR_CODE")
    @ManyToOne
    private LocalAdBranch adBranch;

    @JoinColumn(name = "AP_TAX_CODE", referencedColumnName = "AP_TC_CODE")
    @ManyToOne
    private LocalApTaxCode apTaxCode;

    public Integer getBtcCode() {

        return btcCode;
    }

    public void setBtcCode(Integer AP_BTC_CODE) {

        this.btcCode = AP_BTC_CODE;
    }

    public Integer getBtcGlCoaTaxCode() {

        return btcGlCoaTaxCode;
    }

    public void setBtcGlCoaTaxCode(Integer BTC_COA_GL_TX_CD) {

        this.btcGlCoaTaxCode = BTC_COA_GL_TX_CD;
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

    public LocalApTaxCode getApTaxCode() {

        return apTaxCode;
    }

    public void setApTaxCode(LocalApTaxCode apTaxCode) {

        this.apTaxCode = apTaxCode;
    }

}