package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "GlJournalCategory")
@Table(name = "GL_JRNL_CTGRY")
public class LocalGlJournalCategory extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JC_CODE", nullable = false)
    private Integer jcCode;

    @Column(name = "JC_NM", columnDefinition = "VARCHAR")
    private String jcName;

    @Column(name = "JC_DESC", columnDefinition = "VARCHAR")
    private String jcDescription;

    @Column(name = "JC_RVRSL_MTHD", columnDefinition = "VARCHAR")
    private char jcReversalMethod;

    @Column(name = "JC_AD_CMPNY", columnDefinition = "INT")
    private Integer jcAdCompany;

    @OneToMany(mappedBy = "glJournalCategory", fetch = FetchType.LAZY)
    private List<LocalGlSuspenseAccount> glSuspenseAccounts;

    @OneToMany(mappedBy = "glJournalCategory", fetch = FetchType.LAZY)
    private List<LocalGlJournal> glJournals;

    @OneToMany(mappedBy = "glJournalCategory", fetch = FetchType.LAZY)
    private List<LocalGlRecurringJournal> glRecurringJournals;

    public Integer getJcCode() {

        return jcCode;
    }

    public void setJcCode(Integer JC_CODE) {

        this.jcCode = JC_CODE;
    }

    public String getJcName() {

        return jcName;
    }

    public void setJcName(String JC_NM) {

        this.jcName = JC_NM;
    }

    public String getJcDescription() {

        return jcDescription;
    }

    public void setJcDescription(String JC_DESC) {

        this.jcDescription = JC_DESC;
    }

    public char getJcReversalMethod() {

        return jcReversalMethod;
    }

    public void setJcReversalMethod(char JC_RVRSL_MTHD) {

        this.jcReversalMethod = JC_RVRSL_MTHD;
    }

    public Integer getJcAdCompany() {

        return jcAdCompany;
    }

    public void setJcAdCompany(Integer JC_AD_CMPNY) {

        this.jcAdCompany = JC_AD_CMPNY;
    }

    @XmlTransient
    public List getGlSuspenseAccounts() {

        return glSuspenseAccounts;
    }

    public void setGlSuspenseAccounts(List glSuspenseAccounts) {

        this.glSuspenseAccounts = glSuspenseAccounts;
    }

    @XmlTransient
    public List getGlJournals() {

        return glJournals;
    }

    public void setGlJournals(List glJournals) {

        this.glJournals = glJournals;
    }

    @XmlTransient
    public List getGlRecurringJournals() {

        return glRecurringJournals;
    }

    public void setGlRecurringJournals(List glRecurringJournals) {

        this.glRecurringJournals = glRecurringJournals;
    }

    public void addGlSuspenseAccount(LocalGlSuspenseAccount entity) {

        try {
            entity.setGlJournalCategory(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlSuspenseAccount(LocalGlSuspenseAccount entity) {

        try {
            entity.setGlJournalCategory(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlJournal(LocalGlJournal entity) {

        try {
            entity.setGlJournalCategory(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlJournal(LocalGlJournal entity) {

        try {
            entity.setGlJournalCategory(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlRecurringJournal(LocalGlRecurringJournal entity) {

        try {
            entity.setGlJournalCategory(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlRecurringJournal(LocalGlRecurringJournal entity) {

        try {
            entity.setGlJournalCategory(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}