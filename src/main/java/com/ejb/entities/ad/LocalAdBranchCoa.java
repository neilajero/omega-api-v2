package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdBranchCoa")
@Table(name = "AD_BR_COA")
public class LocalAdBranchCoa extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BCOA_CODE", nullable = false)
    private Integer bcoaCode;

    @Column(name = "BCOA_AD_CMPNY", columnDefinition = "INT")
    private Integer bcoaAdCompany;

    @JoinColumn(name = "AD_BRANCH", referencedColumnName = "BR_CODE")
    @ManyToOne
    private LocalAdBranch adBranch;

    @JoinColumn(name = "GL_COA", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    public Integer getBcoaCode() {

        return bcoaCode;
    }

    public void setBcoaCode(Integer BCOA_CODE) {

        this.bcoaCode = BCOA_CODE;
    }

    public Integer getBcoaAdCompany() {

        return bcoaAdCompany;
    }

    public void setBcoaAdCompany(Integer BCOA_AD_CMPNY) {

        this.bcoaAdCompany = BCOA_AD_CMPNY;
    }

    public LocalAdBranch getAdBranch() {

        return adBranch;
    }

    public void setAdBranch(LocalAdBranch adBranch) {

        this.adBranch = adBranch;
    }

    public LocalGlChartOfAccount getGlChartOfAccount() {

        return glChartOfAccount;
    }

    public void setGlChartOfAccount(LocalGlChartOfAccount glChartOfAccount) {

        this.glChartOfAccount = glChartOfAccount;
    }

}