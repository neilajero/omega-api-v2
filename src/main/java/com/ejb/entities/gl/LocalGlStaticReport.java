package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "GlStaticReport")
@Table(name = "GL_STTC_RPRT")
public class LocalGlStaticReport extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SR_CODE", nullable = false)
    private Integer srCode;

    @Column(name = "SR_NM", columnDefinition = "VARCHAR")
    private String srName;

    @Column(name = "SR_DESC", columnDefinition = "VARCHAR")
    private String srDescription;

    @Column(name = "SR_FL_NM", columnDefinition = "VARCHAR")
    private String srFileName;

    @Column(name = "SR_DT_FRM", columnDefinition = "DATETIME")
    private Date srDateFrom;

    @Column(name = "SR_DT_TO", columnDefinition = "DATETIME")
    private Date srDateTo;

    @Column(name = "SR_AD_CMPNY", columnDefinition = "INT")
    private Integer srAdCompany;

    @OneToMany(mappedBy = "glStaticReport", fetch = FetchType.LAZY)
    private List<LocalGlUserStaticReport> glUserStaticReports;

    public Integer getSrCode() {

        return srCode;
    }

    public void setSrCode(Integer SR_CODE) {

        this.srCode = SR_CODE;
    }

    public String getSrName() {

        return srName;
    }

    public void setSrName(String SR_NM) {

        this.srName = SR_NM;
    }

    public String getSrDescription() {

        return srDescription;
    }

    public void setSrDescription(String SR_DESC) {

        this.srDescription = SR_DESC;
    }

    public String getSrFileName() {

        return srFileName;
    }

    public void setSrFileName(String SR_FL_NM) {

        this.srFileName = SR_FL_NM;
    }

    public Date getSrDateFrom() {

        return srDateFrom;
    }

    public void setSrDateFrom(Date SR_DT_FRM) {

        this.srDateFrom = SR_DT_FRM;
    }

    public Date getSrDateTo() {

        return srDateTo;
    }

    public void setSrDateTo(Date SR_DT_TO) {

        this.srDateTo = SR_DT_TO;
    }

    public Integer getSrAdCompany() {

        return srAdCompany;
    }

    public void setSrAdCompany(Integer SR_AD_CMPNY) {

        this.srAdCompany = SR_AD_CMPNY;
    }

    @XmlTransient
    public List getGlUserStaticReports() {

        return glUserStaticReports;
    }

    public void setGlUserStaticReports(List glUserStaticReports) {

        this.glUserStaticReports = glUserStaticReports;
    }

    public void addGlUserStaticReport(LocalGlUserStaticReport entity) {

        try {
            entity.setGlStaticReport(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlUserStaticReport(LocalGlUserStaticReport entity) {

        try {
            entity.setGlStaticReport(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}