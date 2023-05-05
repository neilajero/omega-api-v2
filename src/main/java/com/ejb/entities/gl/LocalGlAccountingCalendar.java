package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "GlAccountingCalendar")
@Table(name = "GL_ACCNTNG_CLNDR")
public class LocalGlAccountingCalendar extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GL_AC_CODE", nullable = false)
    private Integer acCode;

    @Column(name = "AC_NM", columnDefinition = "VARCHAR")
    private String acName;

    @Column(name = "AC_DESC", columnDefinition = "VARCHAR")
    private String acDescription;

    @Column(name = "AC_YR", columnDefinition = "INT")
    private Integer acYear;

    @Column(name = "AC_AD_CMPNY", columnDefinition = "INT")
    private Integer acAdCompany;

    @JoinColumn(name = "GL_PERIOD_TYPE", referencedColumnName = "GL_PT_CODE")
    @ManyToOne
    private LocalGlPeriodType glPeriodType;

    @OneToMany(mappedBy = "glAccountingCalendar", fetch = FetchType.LAZY)
    private List<LocalGlSetOfBook> glSetOfBooks;
    @OneToMany(mappedBy = "glAccountingCalendar", fetch = FetchType.LAZY)
    private List<LocalGlAccountingCalendarValue> glAccountingCalendarValues;

    public Integer getAcCode() {

        return acCode;
    }

    public void setAcCode(Integer GL_AC_CODE) {

        this.acCode = GL_AC_CODE;
    }

    public String getAcName() {

        return acName;
    }

    public void setAcName(String AC_NM) {

        this.acName = AC_NM;
    }

    public String getAcDescription() {

        return acDescription;
    }

    public void setAcDescription(String AC_DESC) {

        this.acDescription = AC_DESC;
    }

    public Integer getAcYear() {

        return acYear;
    }

    public void setAcYear(Integer AC_YR) {

        this.acYear = AC_YR;
    }

    public Integer getAcAdCompany() {

        return acAdCompany;
    }

    public void setAcAdCompany(Integer AC_AD_CMPNY) {

        this.acAdCompany = AC_AD_CMPNY;
    }

    public LocalGlPeriodType getGlPeriodType() {

        return glPeriodType;
    }

    public void setGlPeriodType(LocalGlPeriodType glPeriodType) {

        this.glPeriodType = glPeriodType;
    }

    @XmlTransient
    public List getGlSetOfBooks() {

        return glSetOfBooks;
    }

    public void setGlSetOfBooks(List glSetOfBooks) {

        this.glSetOfBooks = glSetOfBooks;
    }

    @XmlTransient
    public List getGlAccountingCalendarValues() {

        return glAccountingCalendarValues;
    }

    public void setGlAccountingCalendarValues(List glAccountingCalendarValues) {

        this.glAccountingCalendarValues = glAccountingCalendarValues;
    }

    public void addGlSetOfBook(LocalGlSetOfBook entity) {

        try {
            entity.setGlAccountingCalendar(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlSetOfBook(LocalGlSetOfBook entity) {

        try {
            entity.setGlAccountingCalendar(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlAccountingCalendarValue(LocalGlAccountingCalendarValue entity) {

        try {
            entity.setGlAccountingCalendar(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlAccountingCalendarValue(LocalGlAccountingCalendarValue entity) {

        try {
            entity.setGlAccountingCalendar(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}