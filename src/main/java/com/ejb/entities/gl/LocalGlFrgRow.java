package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "GlFrgRow")
@Table(name = "GL_FRG_RW")
public class LocalGlFrgRow extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROW_CODE", nullable = false)
    private Integer rowCode;

    @Column(name = "ROW_LN_NMBR", columnDefinition = "INT")
    private Integer rowLineNumber;

    @Column(name = "ROW_NM", columnDefinition = "VARCHAR")
    private String rowName;

    @Column(name = "ROW_INDNT", columnDefinition = "INT")
    private Integer rowIndent;

    @Column(name = "ROW_LN_TO_SKP_BFR", columnDefinition = "INT")
    private Integer rowLineToSkipBefore;

    @Column(name = "ROW_LN_TO_SKP_AFTR", columnDefinition = "INT")
    private Integer rowLineToSkipAfter;

    @Column(name = "ROW_UNDRLN_CHAR_BFR", columnDefinition = "INT")
    private Integer rowUnderlineCharacterBefore;

    @Column(name = "ROW_UNDRLN_CHAR_AFTR", columnDefinition = "INT")
    private Integer rowUnderlineCharacterAfter;

    @Column(name = "ROW_PG_BRK_BFR", columnDefinition = "INT")
    private Integer rowPageBreakBefore;

    @Column(name = "ROW_PG_BRK_AFTR", columnDefinition = "INT")
    private Integer rowPageBreakAfter;

    @Column(name = "ROW_OVRRD_COL_CALC", columnDefinition = "TINYINT")
    private byte rowOverrideColumnCalculation;

    @Column(name = "ROW_HD_RW", columnDefinition = "TINYINT")
    private byte rowHideRow;

    @Column(name = "ROW_FNT_STYL", columnDefinition = "VARCHAR")
    private String rowFontStyle;

    @Column(name = "ROW_HRZNTL_ALGN", columnDefinition = "VARCHAR")
    private String rowHorizontalAlign;

    @Column(name = "ROW_AD_CMPNY", columnDefinition = "INT")
    private Integer rowAdCompany;

    @JoinColumn(name = "GL_FRG_ROW_SET", referencedColumnName = "GL_RS_CODE")
    @ManyToOne
    private LocalGlFrgRowSet glFrgRowSet;

    @OneToMany(mappedBy = "glFrgRow", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalGlFrgAccountAssignment> glFrgAccountAssignments;

    @OneToMany(mappedBy = "glFrgRow", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalGlFrgCalculation> glFrgCalculations;

    public Integer getRowCode() {

        return rowCode;
    }

    public void setRowCode(Integer ROW_CODE) {

        this.rowCode = ROW_CODE;
    }

    public Integer getRowLineNumber() {

        return rowLineNumber;
    }

    public void setRowLineNumber(Integer ROW_LN_NMBR) {

        this.rowLineNumber = ROW_LN_NMBR;
    }

    public String getRowName() {

        return rowName;
    }

    public void setRowName(String ROW_NM) {

        this.rowName = ROW_NM;
    }

    public Integer getRowIndent() {

        return rowIndent;
    }

    public void setRowIndent(Integer ROW_INDNT) {

        this.rowIndent = ROW_INDNT;
    }

    public Integer getRowLineToSkipBefore() {

        return rowLineToSkipBefore;
    }

    public void setRowLineToSkipBefore(Integer ROW_LN_TO_SKP_BFR) {

        this.rowLineToSkipBefore = ROW_LN_TO_SKP_BFR;
    }

    public Integer getRowLineToSkipAfter() {

        return rowLineToSkipAfter;
    }

    public void setRowLineToSkipAfter(Integer ROW_LN_TO_SKP_AFTR) {

        this.rowLineToSkipAfter = ROW_LN_TO_SKP_AFTR;
    }

    public Integer getRowUnderlineCharacterBefore() {

        return rowUnderlineCharacterBefore;
    }

    public void setRowUnderlineCharacterBefore(Integer ROW_UNDRLN_CHAR_BFR) {

        this.rowUnderlineCharacterBefore = ROW_UNDRLN_CHAR_BFR;
    }

    public Integer getRowUnderlineCharacterAfter() {

        return rowUnderlineCharacterAfter;
    }

    public void setRowUnderlineCharacterAfter(Integer ROW_UNDRLN_CHAR_AFTR) {

        this.rowUnderlineCharacterAfter = ROW_UNDRLN_CHAR_AFTR;
    }

    public Integer getRowPageBreakBefore() {

        return rowPageBreakBefore;
    }

    public void setRowPageBreakBefore(Integer ROW_PG_BRK_BFR) {

        this.rowPageBreakBefore = ROW_PG_BRK_BFR;
    }

    public Integer getRowPageBreakAfter() {

        return rowPageBreakAfter;
    }

    public void setRowPageBreakAfter(Integer ROW_PG_BRK_AFTR) {

        this.rowPageBreakAfter = ROW_PG_BRK_AFTR;
    }

    public byte getRowOverrideColumnCalculation() {

        return rowOverrideColumnCalculation;
    }

    public void setRowOverrideColumnCalculation(byte ROW_OVRRD_COL_CALC) {

        this.rowOverrideColumnCalculation = ROW_OVRRD_COL_CALC;
    }

    public byte getRowHideRow() {

        return rowHideRow;
    }

    public void setRowHideRow(byte ROW_HD_RW) {

        this.rowHideRow = ROW_HD_RW;
    }

    public String getRowFontStyle() {

        return rowFontStyle;
    }

    public void setRowFontStyle(String ROW_FNT_STYL) {

        this.rowFontStyle = ROW_FNT_STYL;
    }

    public String getRowHorizontalAlign() {

        return rowHorizontalAlign;
    }

    public void setRowHorizontalAlign(String ROW_HRZNTL_ALGN) {

        this.rowHorizontalAlign = ROW_HRZNTL_ALGN;
    }

    public Integer getRowAdCompany() {

        return rowAdCompany;
    }

    public void setRowAdCompany(Integer ROW_AD_CMPNY) {

        this.rowAdCompany = ROW_AD_CMPNY;
    }

    public LocalGlFrgRowSet getGlFrgRowSet() {

        return glFrgRowSet;
    }

    public void setGlFrgRowSet(LocalGlFrgRowSet glFrgRowSet) {

        this.glFrgRowSet = glFrgRowSet;
    }

    @XmlTransient
    public List getGlFrgAccountAssignments() {

        return glFrgAccountAssignments;
    }

    public void setGlFrgAccountAssignments(List glFrgAccountAssignments) {

        this.glFrgAccountAssignments = glFrgAccountAssignments;
    }

    @XmlTransient
    public List getGlFrgCalculations() {

        return glFrgCalculations;
    }

    public void setGlFrgCalculations(List glFrgCalculations) {

        this.glFrgCalculations = glFrgCalculations;
    }

    public void addGlFrgAccountAssignment(LocalGlFrgAccountAssignment entity) {

        try {
            entity.setGlFrgRow(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlFrgAccountAssignment(LocalGlFrgAccountAssignment entity) {

        try {
            entity.setGlFrgRow(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlFrgCalculation(LocalGlFrgCalculation entity) {

        try {
            entity.setGlFrgRow(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlFrgCalculation(LocalGlFrgCalculation entity) {

        try {
            entity.setGlFrgRow(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}