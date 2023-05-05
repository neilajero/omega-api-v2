package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "GlFrgColumn")
@Table(name = "GL_FRG_CLMN")
public class LocalGlFrgColumn extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COL_CODE", nullable = false)
    private Integer colCode;

    @Column(name = "COL_NM", columnDefinition = "VARCHAR")
    private String colName;

    @Column(name = "COL_PSTN", columnDefinition = "INT")
    private Integer colPosition;

    @Column(name = "COL_SQNC_NMBR", columnDefinition = "INT")
    private Integer colSequenceNumber;

    @Column(name = "COL_FRMT_MSK", columnDefinition = "VARCHAR")
    private String colFormatMask;

    @Column(name = "COL_FCTR", columnDefinition = "VARCHAR")
    private String colFactor;

    @Column(name = "COL_AMNT_TYP", columnDefinition = "VARCHAR")
    private String colAmountType;

    @Column(name = "COL_OFFST", columnDefinition = "INT")
    private Integer colOffset;

    @Column(name = "COL_OVRRD_ROW_CALC", columnDefinition = "TINYINT")
    private byte colOverrideRowCalculation;

    @Column(name = "COL_AD_CMPNY", columnDefinition = "INT")
    private Integer colAdCompany;

    @JoinColumn(name = "GL_FRG_COLUMN_SET", referencedColumnName = "CS_CODE")
    @ManyToOne
    private LocalGlFrgColumnSet glFrgColumnSet;

    @JoinColumn(name = "GL_FUNCTIONAL_CURRENCY", referencedColumnName = "FC_CODE")
    @ManyToOne
    private LocalGlFunctionalCurrency glFunctionalCurrency;

    @OneToMany(mappedBy = "glFrgColumn", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalGlFrgCalculation> glFrgCalculations;

    public Integer getColCode() {

        return colCode;
    }

    public void setColCode(Integer COL_CODE) {

        this.colCode = COL_CODE;
    }

    public String getColName() {

        return colName;
    }

    public void setColName(String COL_NM) {

        this.colName = COL_NM;
    }

    public Integer getColPosition() {

        return colPosition;
    }

    public void setColPosition(Integer COL_PSTN) {

        this.colPosition = COL_PSTN;
    }

    public Integer getColSequenceNumber() {

        return colSequenceNumber;
    }

    public void setColSequenceNumber(Integer COL_SQNC_NMBR) {

        this.colSequenceNumber = COL_SQNC_NMBR;
    }

    public String getColFormatMask() {

        return colFormatMask;
    }

    public void setColFormatMask(String COL_FRMT_MSK) {

        this.colFormatMask = COL_FRMT_MSK;
    }

    public String getColFactor() {

        return colFactor;
    }

    public void setColFactor(String COL_FCTR) {

        this.colFactor = COL_FCTR;
    }

    public String getColAmountType() {

        return colAmountType;
    }

    public void setColAmountType(String COL_AMNT_TYP) {

        this.colAmountType = COL_AMNT_TYP;
    }

    public Integer getColOffset() {

        return colOffset;
    }

    public void setColOffset(Integer COL_OFFST) {

        this.colOffset = COL_OFFST;
    }

    public byte getColOverrideRowCalculation() {

        return colOverrideRowCalculation;
    }

    public void setColOverrideRowCalculation(byte COL_OVRRD_ROW_CALC) {

        this.colOverrideRowCalculation = COL_OVRRD_ROW_CALC;
    }

    public Integer getColAdCompany() {

        return colAdCompany;
    }

    public void setColAdCompany(Integer COL_AD_CMPNY) {

        this.colAdCompany = COL_AD_CMPNY;
    }

    public LocalGlFrgColumnSet getGlFrgColumnSet() {

        return glFrgColumnSet;
    }

    public void setGlFrgColumnSet(LocalGlFrgColumnSet glFrgColumnSet) {

        this.glFrgColumnSet = glFrgColumnSet;
    }

    public LocalGlFunctionalCurrency getGlFunctionalCurrency() {

        return glFunctionalCurrency;
    }

    public void setGlFunctionalCurrency(LocalGlFunctionalCurrency glFunctionalCurrency) {

        this.glFunctionalCurrency = glFunctionalCurrency;
    }

    @XmlTransient
    public List getGlFrgCalculations() {

        return glFrgCalculations;
    }

    public void setGlFrgCalculations(List glFrgCalculations) {

        this.glFrgCalculations = glFrgCalculations;
    }

    public void addGlFrgCalculation(LocalGlFrgCalculation entity) {

        try {
            entity.setGlFrgColumn(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlFrgCalculation(LocalGlFrgCalculation entity) {

        try {
            entity.setGlFrgColumn(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}