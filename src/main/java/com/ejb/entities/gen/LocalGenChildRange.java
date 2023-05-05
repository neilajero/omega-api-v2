package com.ejb.entities.gen;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "GenChildRange")
@Table(name = "GEN_CHLD_RNG")
public class LocalGenChildRange extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CR_CODE", nullable = false)
    private Integer crCode;

    @Column(name = "CR_LW", columnDefinition = "INT")
    private Integer crLow;

    @Column(name = "CR_HGH", columnDefinition = "INT")
    private Integer crHigh;

    @Column(name = "CR_RNG_TYP", columnDefinition = "VARCHAR")
    private char crRangeType;

    @Column(name = "CR_AD_CMPNY", columnDefinition = "INT")
    private Integer crAdCompany;

    @JoinColumn(name = "GEN_VALUE_SET_VALUE", referencedColumnName = "VSV_CODE")
    @ManyToOne
    private LocalGenValueSetValue genValueSetValue;

    public Integer getCrCode() {

        return crCode;
    }

    public void setCrCode(Integer CR_CODE) {

        this.crCode = CR_CODE;
    }

    public Integer getCrLow() {

        return crLow;
    }

    public void setCrLow(Integer CR_LW) {

        this.crLow = CR_LW;
    }

    public Integer getCrHigh() {

        return crHigh;
    }

    public void setCrHigh(Integer CR_HGH) {

        this.crHigh = CR_HGH;
    }

    public char getCrRangeType() {

        return crRangeType;
    }

    public void setCrRangeType(char CR_RNG_TYP) {

        this.crRangeType = CR_RNG_TYP;
    }

    public Integer getCrAdCompany() {

        return crAdCompany;
    }

    public void setCrAdCompany(Integer CR_AD_CMPNY) {

        this.crAdCompany = CR_AD_CMPNY;
    }

    public LocalGenValueSetValue getGenValueSetValue() {

        return genValueSetValue;
    }

    public void setGenValueSetValue(LocalGenValueSetValue genValueSetValue) {

        this.genValueSetValue = genValueSetValue;
    }

}