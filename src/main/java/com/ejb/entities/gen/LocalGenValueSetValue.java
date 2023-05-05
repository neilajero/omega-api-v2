package com.ejb.entities.gen;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "GenValueSetValue")
@Table(name = "GEN_VL_ST_VL")
public class LocalGenValueSetValue extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VSV_CODE", nullable = false)
    private Integer vsvCode;

    @Column(name = "VSV_VL", columnDefinition = "VARCHAR")
    private String vsvValue;

    @Column(name = "VSV_DESC", columnDefinition = "VARCHAR")
    private String vsvDescription;

    @Column(name = "VSV_PRNT", columnDefinition = "TINYINT")
    private byte vsvParent;

    @Column(name = "VSV_LVL", columnDefinition = "SMALLINT")
    private short vsvLevel;

    @Column(name = "VSV_ENBL", columnDefinition = "TINYINT")
    private byte vsvEnable;

    @Column(name = "VSV_AD_CMPNY", columnDefinition = "INT")
    private Integer vsvAdCompany;

    @JoinColumn(name = "GEN_QUALIFIER", referencedColumnName = "QL_CODE")
    @ManyToOne
    private LocalGenQualifier genQualifier;

    @JoinColumn(name = "GEN_VALUE_SET", referencedColumnName = "VS_CODE")
    @ManyToOne
    private LocalGenValueSet genValueSet;

    @OneToMany(mappedBy = "genValueSetValue", cascade = CascadeType.ALL)
    private List<LocalGenChildRange> genChildRanges;

    @OneToMany(mappedBy = "genValueSetValue", cascade = CascadeType.ALL)
    private List<LocalGenRollupGroupAssignment> genRollupGroupAssignments;

    public Integer getVsvCode() {

        return vsvCode;
    }

    public void setVsvCode(Integer VSV_CODE) {

        this.vsvCode = VSV_CODE;
    }

    public String getVsvValue() {

        return vsvValue;
    }

    public void setVsvValue(String VSV_VL) {

        this.vsvValue = VSV_VL;
    }

    public String getVsvDescription() {

        return vsvDescription;
    }

    public void setVsvDescription(String VSV_DESC) {

        this.vsvDescription = VSV_DESC;
    }

    public byte getVsvParent() {

        return vsvParent;
    }

    public void setVsvParent(byte VSV_PRNT) {

        this.vsvParent = VSV_PRNT;
    }

    public short getVsvLevel() {

        return vsvLevel;
    }

    public void setVsvLevel(short VSV_LVL) {

        this.vsvLevel = VSV_LVL;
    }

    public byte getVsvEnable() {

        return vsvEnable;
    }

    public void setVsvEnable(byte VSV_ENBL) {

        this.vsvEnable = VSV_ENBL;
    }

    public Integer getVsvAdCompany() {

        return vsvAdCompany;
    }

    public void setVsvAdCompany(Integer VSV_AD_CMPNY) {

        this.vsvAdCompany = VSV_AD_CMPNY;
    }

    public LocalGenQualifier getGenQualifier() {

        return genQualifier;
    }

    public void setGenQualifier(LocalGenQualifier genQualifier) {

        this.genQualifier = genQualifier;
    }

    public LocalGenValueSet getGenValueSet() {

        return genValueSet;
    }

    public void setGenValueSet(LocalGenValueSet genValueSet) {

        this.genValueSet = genValueSet;
    }

    @XmlTransient
    public List getGenChildRanges() {

        return genChildRanges;
    }

    public void setGenChildRanges(List genChildRanges) {

        this.genChildRanges = genChildRanges;
    }

    @XmlTransient
    public List getGenRollupGroupAssignments() {

        return genRollupGroupAssignments;
    }

    public void setGenRollupGroupAssignments(List genRollupGroupAssignments) {

        this.genRollupGroupAssignments = genRollupGroupAssignments;
    }

    public void addGenChildRange(LocalGenChildRange entity) {

        try {
            entity.setGenValueSetValue(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGenChildRange(LocalGenChildRange entity) {

        try {
            entity.setGenValueSetValue(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGenRollupGroupAssignment(LocalGenRollupGroupAssignment entity) {

        try {
            entity.setGenValueSetValue(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGenRollupGroupAssignment(LocalGenRollupGroupAssignment entity) {

        try {
            entity.setGenValueSetValue(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}