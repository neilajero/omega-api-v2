package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "GlFrgFinancialReport")
@Table(name = "GL_FRG_FNNCL_RPRT")
public class LocalGlFrgFinancialReport extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FR_CODE", nullable = false)
    private Integer frCode;

    @Column(name = "FR_NM", columnDefinition = "VARCHAR")
    private String frName;

    @Column(name = "FR_DESC", columnDefinition = "VARCHAR")
    private String frDescription;

    @Column(name = "FR_TTLE", columnDefinition = "VARCHAR")
    private String frTitle;

    @Column(name = "FR_FNT_SZ", columnDefinition = "INT")
    private Integer frFontSize;

    @Column(name = "FR_FNT_STYL", columnDefinition = "VARCHAR")
    private String frFontStyle;

    @Column(name = "FR_HRZNTL_ALGN", columnDefinition = "VARCHAR")
    private String frHorizontalAlign;

    @Column(name = "FR_AD_CMPNY", columnDefinition = "INT")
    private Integer frAdCompany;

    @JoinColumn(name = "GL_FRG_COLUMN_SET", referencedColumnName = "CS_CODE")
    @ManyToOne
    private LocalGlFrgColumnSet glFrgColumnSet;

    @JoinColumn(name = "GL_FRG_ROW_SET", referencedColumnName = "GL_RS_CODE")
    @ManyToOne
    private LocalGlFrgRowSet glFrgRowSet;

    public Integer getFrCode() {

        return frCode;
    }

    public void setFrCode(Integer FR_CODE) {

        this.frCode = FR_CODE;
    }

    public String getFrName() {

        return frName;
    }

    public void setFrName(String FR_NM) {

        this.frName = FR_NM;
    }

    public String getFrDescription() {

        return frDescription;
    }

    public void setFrDescription(String FR_DESC) {

        this.frDescription = FR_DESC;
    }

    public String getFrTitle() {

        return frTitle;
    }

    public void setFrTitle(String FR_TTLE) {

        this.frTitle = FR_TTLE;
    }

    public Integer getFrFontSize() {

        return frFontSize;
    }

    public void setFrFontSize(Integer FR_FNT_SZ) {

        this.frFontSize = FR_FNT_SZ;
    }

    public String getFrFontStyle() {

        return frFontStyle;
    }

    public void setFrFontStyle(String FR_FNT_STYL) {

        this.frFontStyle = FR_FNT_STYL;
    }

    public String getFrHorizontalAlign() {

        return frHorizontalAlign;
    }

    public void setFrHorizontalAlign(String FR_HRZNTL_ALGN) {

        this.frHorizontalAlign = FR_HRZNTL_ALGN;
    }

    public Integer getFrAdCompany() {

        return frAdCompany;
    }

    public void setFrAdCompany(Integer FR_AD_CMPNY) {

        this.frAdCompany = FR_AD_CMPNY;
    }

    public LocalGlFrgColumnSet getGlFrgColumnSet() {

        return glFrgColumnSet;
    }

    public void setGlFrgColumnSet(LocalGlFrgColumnSet glFrgColumnSet) {

        this.glFrgColumnSet = glFrgColumnSet;
    }

    public LocalGlFrgRowSet getGlFrgRowSet() {

        return glFrgRowSet;
    }

    public void setGlFrgRowSet(LocalGlFrgRowSet glFrgRowSet) {

        this.glFrgRowSet = glFrgRowSet;
    }

}