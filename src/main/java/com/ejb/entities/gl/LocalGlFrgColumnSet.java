package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "GlFrgColumnSet")
@Table(name = "GL_FRG_CLMN_ST")
public class LocalGlFrgColumnSet extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CS_CODE", nullable = false)
    private Integer csCode;

    @Column(name = "CS_NM", columnDefinition = "VARCHAR")
    private String csName;

    @Column(name = "CS_DESC", columnDefinition = "VARCHAR")
    private String csDescription;

    @Column(name = "CS_AD_CMPNY", columnDefinition = "INT")
    private Integer csAdCompany;

    @OneToMany(mappedBy = "glFrgColumnSet", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalGlFrgFinancialReport> glFrgFinancialReports;

    @OneToMany(mappedBy = "glFrgColumnSet", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalGlFrgColumn> glFrgColumns;

    public Integer getCsCode() {

        return csCode;
    }

    public void setCsCode(Integer CS_CODE) {

        this.csCode = CS_CODE;
    }

    public String getCsName() {

        return csName;
    }

    public void setCsName(String CS_NM) {

        this.csName = CS_NM;
    }

    public String getCsDescription() {

        return csDescription;
    }

    public void setCsDescription(String CS_DESC) {

        this.csDescription = CS_DESC;
    }

    public Integer getCsAdCompany() {

        return csAdCompany;
    }

    public void setCsAdCompany(Integer CS_AD_CMPNY) {

        this.csAdCompany = CS_AD_CMPNY;
    }

    @XmlTransient
    public List getGlFrgFinancialReports() {

        return glFrgFinancialReports;
    }

    public void setGlFrgFinancialReports(List glFrgFinancialReports) {

        this.glFrgFinancialReports = glFrgFinancialReports;
    }

    @XmlTransient
    public List getGlFrgColumns() {

        return glFrgColumns;
    }

    public void setGlFrgColumns(List glFrgColumns) {

        this.glFrgColumns = glFrgColumns;
    }

    public void addGlFrgFinancialReport(LocalGlFrgFinancialReport entity) {

        try {
            entity.setGlFrgColumnSet(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlFrgFinancialReport(LocalGlFrgFinancialReport entity) {

        try {
            entity.setGlFrgColumnSet(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlFrgColumn(LocalGlFrgColumn entity) {

        try {
            entity.setGlFrgColumnSet(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlFrgColumn(LocalGlFrgColumn entity) {

        try {
            entity.setGlFrgColumnSet(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}