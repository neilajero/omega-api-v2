package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "GlReportValue")
@Table(name = "GL_RPRT_VL")
public class LocalGlReportValue extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GL_RV_CODE", nullable = false)
    private Integer rvCode;

    @Column(name = "RV_TYP", columnDefinition = "DATETIME")
    private String rvType;

    @Column(name = "RV_PRMTR", columnDefinition = "DATETIME")
    private String rvParameter;

    @Column(name = "RV_DESC", columnDefinition = "DATETIME")
    private String rvDescription;

    @Column(name = "RV_DFLT_VL", columnDefinition = "DATETIME")
    private String rvDefaultValue;

    @Column(name = "RV_AD_CMPNY", columnDefinition = "INT")
    private Integer rvAdCompany;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    public Integer getRvCode() {

        return rvCode;
    }

    public void setRvCode(Integer GL_RV_CODE) {

        this.rvCode = GL_RV_CODE;
    }

    public String getRvType() {

        return rvType;
    }

    public void setRvType(String RV_TYP) {

        this.rvType = RV_TYP;
    }

    public String getRvParameter() {

        return rvParameter;
    }

    public void setRvParameter(String RV_PRMTR) {

        this.rvParameter = RV_PRMTR;
    }

    public String getRvDescription() {

        return rvDescription;
    }

    public void setRvDescription(String RV_DESC) {

        this.rvDescription = RV_DESC;
    }

    public String getRvDefaultValue() {

        return rvDefaultValue;
    }

    public void setRvDefaultValue(String RV_DFLT_VL) {

        this.rvDefaultValue = RV_DFLT_VL;
    }

    public Integer getRvAdCompany() {

        return rvAdCompany;
    }

    public void setRvAdCompany(Integer RV_AD_CMPNY) {

        this.rvAdCompany = RV_AD_CMPNY;
    }

    public LocalGlChartOfAccount getGlChartOfAccount() {

        return glChartOfAccount;
    }

    public void setGlChartOfAccount(LocalGlChartOfAccount glChartOfAccount) {

        this.glChartOfAccount = glChartOfAccount;
    }

}