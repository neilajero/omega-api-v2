package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdBranchProjectTypeType")
@Table(name = "AD_BR_PTT")
public class LocalAdBranchProjectTypeType extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BPTT_CODE", nullable = false)
    private Integer bpttCode;

    @Column(name = "BPTT_AD_CMPNY", columnDefinition = "INT")
    private Integer bpttAdCompany;

    @JoinColumn(name = "AD_BRANCH", referencedColumnName = "BR_CODE")
    @ManyToOne
    private LocalAdBranch adBranch;

    public Integer getBpttCode() {

        return bpttCode;
    }

    public void setBpttCode(Integer BPTT_CODE) {

        this.bpttCode = BPTT_CODE;
    }

    public Integer getBpttAdCompany() {

        return bpttAdCompany;
    }

    public void setBpttAdCompany(Integer BPTT_AD_CMPNY) {

        this.bpttAdCompany = BPTT_AD_CMPNY;
    }

    public LocalAdBranch getAdBranch() {

        return adBranch;
    }

    public void setAdBranch(LocalAdBranch adBranch) {

        this.adBranch = adBranch;
    }

}