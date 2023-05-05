package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "GlTransactionCalendar")
@Table(name = "GL_TRNSCTN_CLNDR")
public class LocalGlTransactionCalendar extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GL_TC_CODE", nullable = false)
    private Integer tcCode;

    @Column(name = "TC_NM", columnDefinition = "VARCHAR")
    private String tcName;

    @Column(name = "TC_DESC", columnDefinition = "VARCHAR")
    private String tcDescription;

    @Column(name = "TC_AD_CMPNY", columnDefinition = "INT")
    private Integer tcAdCompany;

    @OneToMany(mappedBy = "glTransactionCalendar", fetch = FetchType.LAZY)
    private List<LocalGlSetOfBook> glSetOfBooks;

    @OneToMany(mappedBy = "glTransactionCalendar", fetch = FetchType.LAZY)
    private List<LocalGlTransactionCalendarValue> glTransactionCalendarValues;
    public Integer getTcCode() {

        return tcCode;
    }

    public void setTcCode(Integer GL_TC_CODE) {

        this.tcCode = GL_TC_CODE;
    }

    public String getTcName() {

        return tcName;
    }

    public void setTcName(String TC_NM) {

        this.tcName = TC_NM;
    }

    public String getTcDescription() {

        return tcDescription;
    }

    public void setTcDescription(String TC_DESC) {

        this.tcDescription = TC_DESC;
    }

    public Integer getTcAdCompany() {

        return tcAdCompany;
    }

    public void setTcAdCompany(Integer TC_AD_CMPNY) {

        this.tcAdCompany = TC_AD_CMPNY;
    }

    @XmlTransient
    public List getGlSetOfBooks() {

        return glSetOfBooks;
    }

    public void setGlSetOfBooks(List glSetOfBooks) {

        this.glSetOfBooks = glSetOfBooks;
    }

    @XmlTransient
    public List getGlTransactionCalendarValues() {

        return glTransactionCalendarValues;
    }

    public void setGlTransactionCalendarValues(List glTransactionCalendarValues) {

        this.glTransactionCalendarValues = glTransactionCalendarValues;
    }

    public void addGlSetOfBook(LocalGlSetOfBook entity) {

        try {
            entity.setGlTransactionCalendar(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlSetOfBook(LocalGlSetOfBook entity) {

        try {
            entity.setGlTransactionCalendar(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlTransactionCalendarValue(LocalGlTransactionCalendarValue entity) {

        try {
            entity.setGlTransactionCalendar(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlTransactionCalendarValue(LocalGlTransactionCalendarValue entity) {

        try {
            entity.setGlTransactionCalendar(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}