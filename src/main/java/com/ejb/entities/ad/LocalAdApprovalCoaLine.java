package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;

@Entity(name = "AdApprovalCoaLine")
@Table(name = "AD_APPRVL_COA_LN")
public class LocalAdApprovalCoaLine extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACL_CODE", nullable = false)
    private Integer aclCode;

    @Column(name = "ACL_AD_CMPNY", columnDefinition = "INT")
    private Integer aclAdCompany;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    @OneToMany(mappedBy = "adApprovalCoaLine", fetch = FetchType.LAZY)
    private List<LocalAdAmountLimit> adAmountLimits;

    public Integer getAclCode() {

        return aclCode;
    }

    public void setAclCode(Integer ACL_CODE) {

        this.aclCode = ACL_CODE;
    }

    public Integer getAclAdCompany() {

        return aclAdCompany;
    }

    public void setAclAdCompany(Integer ACL_AD_CMPNY) {

        this.aclAdCompany = ACL_AD_CMPNY;
    }

    public LocalGlChartOfAccount getGlChartOfAccount() {

        return glChartOfAccount;
    }

    public void setGlChartOfAccount(LocalGlChartOfAccount glChartOfAccount) {

        this.glChartOfAccount = glChartOfAccount;
    }

    @XmlTransient
    public List getAdAmountLimits() {

        return adAmountLimits;
    }

    public void setAdAmountLimits(List adAmountLimits) {

        this.adAmountLimits = adAmountLimits;
    }

    public void addAdAmountLimit(LocalAdAmountLimit entity) {

        try {
            entity.setAdApprovalCoaLine(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdAmountLimit(LocalAdAmountLimit entity) {

        try {
            entity.setAdApprovalCoaLine(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}