package com.ejb.entities.gen;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "GenValueSet")
@Table(name = "GEN_VL_ST")
public class LocalGenValueSet extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VS_CODE", nullable = false)
    private Integer vsCode;

    @Column(name = "VS_NM", columnDefinition = "VARCHAR")
    private String vsName;

    @Column(name = "VS_DESC", columnDefinition = "VARCHAR")
    private String vsDescription;

    @Column(name = "VS_ENBL", columnDefinition = "TINYINT")
    private byte vsEnable;

    @Column(name = "VS_AD_CMPNY", columnDefinition = "INT")
    private Integer vsAdCompany;

    @OneToMany(mappedBy = "genValueSet", cascade = CascadeType.ALL)
    private List<LocalGenSegment> genSegments = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "genValueSet", cascade = CascadeType.ALL)
    private List<LocalGenValueSetValue> genValueSetValues;

    public Integer getVsCode() {

        return vsCode;
    }

    public void setVsCode(Integer VS_CODE) {

        this.vsCode = VS_CODE;
    }

    public String getVsName() {

        return vsName;
    }

    public void setVsName(String VS_NM) {

        this.vsName = VS_NM;
    }

    public String getVsDescription() {

        return vsDescription;
    }

    public void setVsDescription(String VS_DESC) {

        this.vsDescription = VS_DESC;
    }

    public byte getVsEnable() {

        return vsEnable;
    }

    public void setVsEnable(byte VS_ENBL) {

        this.vsEnable = VS_ENBL;
    }

    public Integer getVsAdCompany() {

        return vsAdCompany;
    }

    public void setVsAdCompany(Integer VS_AD_CMPNY) {

        this.vsAdCompany = VS_AD_CMPNY;
    }

    @XmlTransient
    public List getGenSegments() {

        return genSegments;
    }

    public void setGenSegments(List genSegments) {

        this.genSegments = genSegments;
    }

    @XmlTransient
    public List getGenValueSetValues() {

        return genValueSetValues;
    }

    public void setGenValueSetValues(List genValueSetValues) {

        this.genValueSetValues = genValueSetValues;
    }

    public void addGenSegment(LocalGenSegment entity) {

        try {
            entity.setGenValueSet(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGenSegment(LocalGenSegment entity) {

        try {
            entity.setGenValueSet(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGenValueSetValue(LocalGenValueSetValue entity) {

        try {
            entity.setGenValueSet(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGenValueSetValue(LocalGenValueSetValue entity) {

        try {
            entity.setGenValueSet(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}