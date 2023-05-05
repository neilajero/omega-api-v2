package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ar.LocalArSalesperson;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdBranchSalesperson")
@Table(name = "AD_BR_SLP")
public class LocalAdBranchSalesperson extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BSLP_CODE", nullable = false)
    private Integer bslpCode;

    @Column(name = "BSLP_AD_CMPNY", columnDefinition = "INT")
    private Integer bslpAdCompany;

    @JoinColumn(name = "AD_BRANCH", referencedColumnName = "BR_CODE")
    @ManyToOne
    private LocalAdBranch adBranch;

    @JoinColumn(name = "AR_SALESPERSON", referencedColumnName = "SLP_CODE")
    @ManyToOne
    private LocalArSalesperson arSalesperson;

    public Integer getBslpCode() {

        return bslpCode;
    }

    public void setBslpCode(Integer BSLP_CODE) {

        this.bslpCode = BSLP_CODE;
    }

    public Integer getBslpAdCompany() {

        return bslpAdCompany;
    }

    public void setBslpAdCompany(Integer BSLP_AD_CMPNY) {

        this.bslpAdCompany = BSLP_AD_CMPNY;
    }

    public LocalAdBranch getAdBranch() {

        return adBranch;
    }

    public void setAdBranch(LocalAdBranch adBranch) {

        this.adBranch = adBranch;
    }

    public LocalArSalesperson getArSalesperson() {

        return arSalesperson;
    }

    public void setArSalesperson(LocalArSalesperson arSalesperson) {

        this.arSalesperson = arSalesperson;
    }

}