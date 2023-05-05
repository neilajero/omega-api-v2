package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity(name = "AdAgingBucket")
@Table(name = "AD_AGNG_BCKT")
public class LocalAdAgingBucket extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AB_CODE", nullable = false)
    private Integer abCode;

    @Column(name = "AB_NM", columnDefinition = "VARCHAR")
    private String abName;

    @Column(name = "AB_DESC", columnDefinition = "VARCHAR")
    private String abDescription;

    @Column(name = "AB_TYP", columnDefinition = "VARCHAR")
    private String abType;

    @Column(name = "AB_ENBL", columnDefinition = "TINYINT")
    private byte abEnable;

    @Column(name = "AB_AD_CMPNY", columnDefinition = "INT")
    private Integer abAdCompany;

    @OneToMany(mappedBy = "adAgingBucket", fetch = FetchType.LAZY)
    private List<LocalAdAgingBucketValue> adAgingBucketValues;

    public Integer getAbCode() {

        return abCode;
    }

    public void setAbCode(Integer AB_CODE) {

        this.abCode = AB_CODE;
    }

    public String getAbName() {

        return abName;
    }

    public void setAbName(String AB_NM) {

        this.abName = AB_NM;
    }

    public String getAbDescription() {

        return abDescription;
    }

    public void setAbDescription(String AB_DESC) {

        this.abDescription = AB_DESC;
    }

    public String getAbType() {

        return abType;
    }

    public void setAbType(String AB_TYP) {

        this.abType = AB_TYP;
    }

    public byte getAbEnable() {

        return abEnable;
    }

    public void setAbEnable(byte AB_ENBL) {

        this.abEnable = AB_ENBL;
    }

    public Integer getAbAdCompany() {

        return abAdCompany;
    }

    public void setAbAdCompany(Integer AB_AD_CMPNY) {

        this.abAdCompany = AB_AD_CMPNY;
    }

    @XmlTransient
    public Collection<LocalAdAgingBucketValue> getAdAgingBucketValues() {

        return adAgingBucketValues;
    }

    public void setAdAgingBucketValues(List<LocalAdAgingBucketValue> adAgingBucketValues) {

        this.adAgingBucketValues = adAgingBucketValues;
    }

    public void addAdAgingBucketValue(LocalAdAgingBucketValue entity) {

        try {
            entity.setAdAgingBucket(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdAgingBucketValue(LocalAdAgingBucketValue entity) {

        try {
            entity.setAdAgingBucket(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void setValues(Object entity) {

        LocalAdAgingBucket adAgingBucket = (LocalAdAgingBucket) entity;
        this.abCode = adAgingBucket.abCode;
        this.abName = adAgingBucket.abName;
        this.abDescription = adAgingBucket.abDescription;
        this.abType = adAgingBucket.abType;
        this.abEnable = adAgingBucket.abEnable;
        this.abAdCompany = adAgingBucket.abAdCompany;
    }

}