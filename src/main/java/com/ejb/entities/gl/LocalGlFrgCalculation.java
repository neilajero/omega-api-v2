package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "GlFrgCalculation")
@Table(name = "GL_FRG_CLCLTN")
public class LocalGlFrgCalculation extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GL_CAL_CODE", nullable = false)
    private Integer calCode;

    @Column(name = "CAL_SQNC_NMBR", columnDefinition = "INT")
    private Integer calSequenceNumber;

    @Column(name = "CAL_OPRTR", columnDefinition = "VARCHAR")
    private String calOperator;

    @Column(name = "CAL_TYP", columnDefinition = "VARCHAR")
    private String calType;

    @Column(name = "CAL_CNSTNT", columnDefinition = "DOUBLE")
    private double calConstant = 0;

    @Column(name = "CAL_ROW_COL_NM", columnDefinition = "VARCHAR")
    private String calRowColName;

    @Column(name = "CAL_SQNC_LW", columnDefinition = "INT")
    private Integer calSequenceLow;

    @Column(name = "CAL_SQNC_HGH", columnDefinition = "INT")
    private Integer calSequenceHigh;

    @Column(name = "CAL_AD_CMPNY", columnDefinition = "INT")
    private Integer calAdCompany;

    @JoinColumn(name = "GL_FRG_COLUMN", referencedColumnName = "COL_CODE")
    @ManyToOne
    private LocalGlFrgColumn glFrgColumn;

    @JoinColumn(name = "GL_FRG_ROW", referencedColumnName = "ROW_CODE")
    @ManyToOne
    private LocalGlFrgRow glFrgRow;

    public Integer getCalCode() {

        return calCode;
    }

    public void setCalCode(Integer GL_CAL_CODE) {

        this.calCode = GL_CAL_CODE;
    }

    public Integer getCalSequenceNumber() {

        return calSequenceNumber;
    }

    public void setCalSequenceNumber(Integer CAL_SQNC_NMBR) {

        this.calSequenceNumber = CAL_SQNC_NMBR;
    }

    public String getCalOperator() {

        return calOperator;
    }

    public void setCalOperator(String CAL_OPRTR) {

        this.calOperator = CAL_OPRTR;
    }

    public String getCalType() {

        return calType;
    }

    public void setCalType(String CAL_TYP) {

        this.calType = CAL_TYP;
    }

    public double getCalConstant() {

        return calConstant;
    }

    public void setCalConstant(double CAL_CNSTNT) {

        this.calConstant = CAL_CNSTNT;
    }

    public String getCalRowColName() {

        return calRowColName;
    }

    public void setCalRowColName(String CAL_ROW_COL_NM) {

        this.calRowColName = CAL_ROW_COL_NM;
    }

    public Integer getCalSequenceLow() {

        return calSequenceLow;
    }

    public void setCalSequenceLow(Integer CAL_SQNC_LW) {

        this.calSequenceLow = CAL_SQNC_LW;
    }

    public Integer getCalSequenceHigh() {

        return calSequenceHigh;
    }

    public void setCalSequenceHigh(Integer CAL_SQNC_HGH) {

        this.calSequenceHigh = CAL_SQNC_HGH;
    }

    public Integer getCalAdCompany() {

        return calAdCompany;
    }

    public void setCalAdCompany(Integer CAL_AD_CMPNY) {

        this.calAdCompany = CAL_AD_CMPNY;
    }

    public LocalGlFrgColumn getGlFrgColumn() {

        return glFrgColumn;
    }

    public void setGlFrgColumn(LocalGlFrgColumn glFrgColumn) {

        this.glFrgColumn = glFrgColumn;
    }

    public LocalGlFrgRow getGlFrgRow() {

        return glFrgRow;
    }

    public void setGlFrgRow(LocalGlFrgRow glFrgRow) {

        this.glFrgRow = glFrgRow;
    }

}