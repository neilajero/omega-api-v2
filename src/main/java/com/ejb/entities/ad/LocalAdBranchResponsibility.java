package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdBranchResponsibility")
@Table(name = "AD_BR_RS")
public class LocalAdBranchResponsibility extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BRS_CODE", nullable = false)
    private Integer brsCode;

    @Column(name = "BRS_AD_CMPNY", columnDefinition = "INT")
    private Integer brsAdCompany;

    @JoinColumn(name = "AD_BRANCH", referencedColumnName = "BR_CODE")
    @ManyToOne
    private LocalAdBranch adBranch;

    @JoinColumn(name = "AD_RESPONSIBILITY", referencedColumnName = "AD_RS_CODE")
    @ManyToOne
    private LocalAdResponsibility adResponsibility;

    public Integer getBrsCode() {

        return brsCode;
    }

    public void setBrsCode(Integer BRS_CODE) {

        this.brsCode = BRS_CODE;
    }

    public Integer getBrsAdCompany() {

        return brsAdCompany;
    }

    public void setBrsAdCompany(Integer BRS_AD_CMPNY) {

        this.brsAdCompany = BRS_AD_CMPNY;
    }

    public LocalAdBranch getAdBranch() {

        return adBranch;
    }

    public void setAdBranch(LocalAdBranch adBranch) {

        this.adBranch = adBranch;
    }

    public LocalAdResponsibility getAdResponsibility() {

        return adResponsibility;
    }

    public void setAdResponsibility(LocalAdResponsibility adResponsibility) {

        this.adResponsibility = adResponsibility;
    }

}