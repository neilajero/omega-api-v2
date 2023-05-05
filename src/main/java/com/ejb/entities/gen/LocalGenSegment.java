package com.ejb.entities.gen;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "GenSegment")
@Table(name = "GEN_SGMNT")
public class LocalGenSegment extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SG_CODE", nullable = false)
    private Integer sgCode;

    @Column(name = "SG_NM", columnDefinition = "VARCHAR")
    private String sgName;

    @Column(name = "SG_DESC", columnDefinition = "VARCHAR")
    private String sgDescription;

    @Column(name = "SG_MAX_SZ", columnDefinition = "SMALLINT")
    private short sgMaxSize;

    @Column(name = "SG_DFLT_VL", columnDefinition = "VARCHAR")
    private String sgDefaultValue;

    @Column(name = "SG_SGMNT_TYP", columnDefinition = "VARCHAR")
    private char sgSegmentType;

    @Column(name = "SG_SGMNT_NMBR", columnDefinition = "SMALLINT")
    private short sgSegmentNumber;

    @Column(name = "SG_AD_CMPNY", columnDefinition = "INT")
    private Integer sgAdCompany;

    @JoinColumn(name = "GEN_FIELD", referencedColumnName = "FL_CODE")
    @ManyToOne
    private LocalGenField genField;

    @JoinColumn(name = "GEN_VALUE_SET", referencedColumnName = "VS_CODE")
    @ManyToOne
    private LocalGenValueSet genValueSet;

    public Integer getSgCode() {

        return sgCode;
    }

    public void setSgCode(Integer SG_CODE) {

        this.sgCode = SG_CODE;
    }

    public String getSgName() {

        return sgName;
    }

    public void setSgName(String SG_NM) {

        this.sgName = SG_NM;
    }

    public String getSgDescription() {

        return sgDescription;
    }

    public void setSgDescription(String SG_DESC) {

        this.sgDescription = SG_DESC;
    }

    public short getSgMaxSize() {

        return sgMaxSize;
    }

    public void setSgMaxSize(short SG_MAX_SZ) {

        this.sgMaxSize = SG_MAX_SZ;
    }

    public String getSgDefaultValue() {

        return sgDefaultValue;
    }

    public void setSgDefaultValue(String SG_DFLT_VL) {

        this.sgDefaultValue = SG_DFLT_VL;
    }

    public char getSgSegmentType() {

        return sgSegmentType;
    }

    public void setSgSegmentType(char SG_SGMNT_TYP) {

        this.sgSegmentType = SG_SGMNT_TYP;
    }

    public short getSgSegmentNumber() {

        return sgSegmentNumber;
    }

    public void setSgSegmentNumber(short SG_SGMNT_NMBR) {

        this.sgSegmentNumber = SG_SGMNT_NMBR;
    }

    public Integer getSgAdCompany() {

        return sgAdCompany;
    }

    public void setSgAdCompany(Integer SG_AD_CMPNY) {

        this.sgAdCompany = SG_AD_CMPNY;
    }

    public LocalGenField getGenField() {

        return genField;
    }

    public void setGenField(LocalGenField genField) {

        this.genField = genField;
    }

    public LocalGenValueSet getGenValueSet() {

        return genValueSet;
    }

    public void setGenValueSet(LocalGenValueSet genValueSet) {

        this.genValueSet = genValueSet;
    }

}