package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "GlFrgRowSet")
@Table(name = "GL_FRG_RW_ST")
public class LocalGlFrgRowSet extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GL_RS_CODE", nullable = false)
    private Integer rsCode;

    @Column(name = "RS_NM", columnDefinition = "VARCHAR")
    private String rsName;

    @Column(name = "RS_DESC", columnDefinition = "VARCHAR")
    private String rsDescription;

    @Column(name = "RS_AD_CMPNY", columnDefinition = "INT")
    private Integer rsAdCompany;

    @OneToMany(mappedBy = "glFrgRowSet", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalGlFrgFinancialReport> glFrgFinancialReports;

    @OneToMany(mappedBy = "glFrgRowSet", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalGlFrgRow> glFrgRows;

    public Integer getRsCode() {

        return rsCode;
    }

    public void setRsCode(Integer GL_RS_CODE) {

        this.rsCode = GL_RS_CODE;
    }

    public String getRsName() {

        return rsName;
    }

    public void setRsName(String RS_NM) {

        this.rsName = RS_NM;
    }

    public String getRsDescription() {

        return rsDescription;
    }

    public void setRsDescription(String RS_DESC) {

        this.rsDescription = RS_DESC;
    }

    public Integer getRsAdCompany() {

        return rsAdCompany;
    }

    public void setRsAdCompany(Integer RS_AD_CMPNY) {

        this.rsAdCompany = RS_AD_CMPNY;
    }

    @XmlTransient
    public List getGlFrgFinancialReports() {

        return glFrgFinancialReports;
    }

    public void setGlFrgFinancialReports(List glFrgFinancialReports) {

        this.glFrgFinancialReports = glFrgFinancialReports;
    }

    @XmlTransient
    public List getGlFrgRows() {

        return glFrgRows;
    }

    public void setGlFrgRows(List glFrgRows) {

        this.glFrgRows = glFrgRows;
    }

    public void addGlFrgFinancialReport(LocalGlFrgFinancialReport entity) {

        try {
            entity.setGlFrgRowSet(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlFrgFinancialReport(LocalGlFrgFinancialReport entity) {

        try {
            entity.setGlFrgRowSet(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlFrgRow(LocalGlFrgRow entity) {

        try {
            entity.setGlFrgRowSet(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlFrgRow(LocalGlFrgRow entity) {

        try {
            entity.setGlFrgRowSet(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}