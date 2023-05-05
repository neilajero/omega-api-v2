package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ArPersonelType")
@Table(name = "AR_PRSNL_TYP")
public class LocalArPersonelType extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_PT_CODE", nullable = false)
    private Integer ptCode;

    @Column(name = "PT_SHRT_NM", columnDefinition = "VARCHAR")
    private String ptShortName;

    @Column(name = "PT_NM", columnDefinition = "VARCHAR")
    private String ptName;

    @Column(name = "PT_DESC", columnDefinition = "VARCHAR")
    private String ptDescription;

    @Column(name = "PT_RT", columnDefinition = "DOUBLE")
    private double ptRate = 0;

    @Column(name = "PT_AD_CMPNY", columnDefinition = "INT")
    private Integer ptAdCompany;

    @OneToMany(mappedBy = "arPersonelType", fetch = FetchType.LAZY)
    private List<LocalArPersonel> arPersonels;

    public Integer getPtCode() {

        return ptCode;
    }

    public void setPtCode(Integer AR_PT_CODE) {

        this.ptCode = AR_PT_CODE;
    }

    public String getPtShortName() {

        return ptShortName;
    }

    public void setPtShortName(String PT_SHRT_NM) {

        this.ptShortName = PT_SHRT_NM;
    }

    public String getPtName() {

        return ptName;
    }

    public void setPtName(String PT_NM) {

        this.ptName = PT_NM;
    }

    public String getPtDescription() {

        return ptDescription;
    }

    public void setPtDescription(String PT_DESC) {

        this.ptDescription = PT_DESC;
    }

    public double getPtRate() {

        return ptRate;
    }

    public void setPtRate(double PT_RT) {

        this.ptRate = PT_RT;
    }

    public Integer getPtAdCompany() {

        return ptAdCompany;
    }

    public void setPtAdCompany(Integer PT_AD_CMPNY) {

        this.ptAdCompany = PT_AD_CMPNY;
    }

    @XmlTransient
    public List getArPersonels() {

        return arPersonels;
    }

    public void setArPersonels(List arPersonels) {

        this.arPersonels = arPersonels;
    }

}