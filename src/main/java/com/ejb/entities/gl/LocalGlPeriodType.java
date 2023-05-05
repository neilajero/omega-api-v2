package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "GlPeriodType")
@Table(name = "GL_PRD_TYP")
public class LocalGlPeriodType extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GL_PT_CODE", nullable = false)
    private Integer ptCode;

    @Column(name = "PT_NM", columnDefinition = "VARCHAR")
    private String ptName;

    @Column(name = "PT_DESC", columnDefinition = "VARCHAR")
    private String ptDescription;

    @Column(name = "PT_PRD_PER_YR", columnDefinition = "SMALLINT")
    private short ptPeriodPerYear;

    @Column(name = "PT_YR_TYP", columnDefinition = "VARCHAR")
    private char ptYearType;

    @Column(name = "PT_AD_CMPNY", columnDefinition = "INT")
    private Integer ptAdCompany;

    @OneToMany(mappedBy = "glPeriodType", fetch = FetchType.LAZY)
    private List<LocalGlAccountingCalendar> glAccountingCalendars;

    public Integer getPtCode() {

        return ptCode;
    }

    public void setPtCode(Integer GL_PT_CODE) {

        this.ptCode = GL_PT_CODE;
    }

    public String getPtName() {

        return ptName;
    }

    public void setPtName(String PT_NM) {

        this.ptName = PT_NM;
    }

    public String getPtDescription() {

        return ptDescription;
    }

    public void setPtDescription(String PT_DESC) {

        this.ptDescription = PT_DESC;
    }

    public short getPtPeriodPerYear() {

        return ptPeriodPerYear;
    }

    public void setPtPeriodPerYear(short PT_PRD_PER_YR) {

        this.ptPeriodPerYear = PT_PRD_PER_YR;
    }

    public char getPtYearType() {

        return ptYearType;
    }

    public void setPtYearType(char PT_YR_TYP) {

        this.ptYearType = PT_YR_TYP;
    }

    public Integer getPtAdCompany() {

        return ptAdCompany;
    }

    public void setPtAdCompany(Integer PT_AD_CMPNY) {

        this.ptAdCompany = PT_AD_CMPNY;
    }

    @XmlTransient
    public List getGlAccountingCalendars() {

        return glAccountingCalendars;
    }

    public void setGlAccountingCalendars(List glAccountingCalendars) {

        this.glAccountingCalendars = glAccountingCalendars;
    }

    public void addGlAccountingCalendar(LocalGlAccountingCalendar entity) {

        try {
            entity.setGlPeriodType(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlAccountingCalendar(LocalGlAccountingCalendar entity) {

        try {
            entity.setGlPeriodType(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}