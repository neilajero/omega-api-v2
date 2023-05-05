package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "GlJournalSource")
@Table(name = "GL_JRNL_SRC")
public class LocalGlJournalSource extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JS_CODE", nullable = false)
    private Integer jsCode;

    @Column(name = "JS_NM", columnDefinition = "VARCHAR")
    private String jsName;

    @Column(name = "JS_DESC", columnDefinition = "VARCHAR")
    private String jsDescription;

    @Column(name = "JS_FRZ_JRNL", columnDefinition = "TINYINT")
    private byte jsFreezeJournal;

    @Column(name = "JS_JRNL_APPRVL", columnDefinition = "TINYINT")
    private byte jsJournalApproval;

    @Column(name = "JS_EFFCTV_DT_RL", columnDefinition = "VARCHAR")
    private char jsEffectiveDateRule;

    @Column(name = "JS_AD_CMPNY", columnDefinition = "INT")
    private Integer jsAdCompany;

    @OneToMany(mappedBy = "glJournalSource", fetch = FetchType.LAZY)
    private List<LocalGlSuspenseAccount> glSuspenseAccounts;

    @OneToMany(mappedBy = "glJournalSource", fetch = FetchType.LAZY)
    private List<LocalGlJournal> glJournals;

    public Integer getJsCode() {

        return jsCode;
    }

    public void setJsCode(Integer JS_CODE) {

        this.jsCode = JS_CODE;
    }

    public String getJsName() {

        return jsName;
    }

    public void setJsName(String JS_NM) {

        this.jsName = JS_NM;
    }

    public String getJsDescription() {

        return jsDescription;
    }

    public void setJsDescription(String JS_DESC) {

        this.jsDescription = JS_DESC;
    }

    public byte getJsFreezeJournal() {

        return jsFreezeJournal;
    }

    public void setJsFreezeJournal(byte JS_FRZ_JRNL) {

        this.jsFreezeJournal = JS_FRZ_JRNL;
    }

    public byte getJsJournalApproval() {

        return jsJournalApproval;
    }

    public void setJsJournalApproval(byte JS_JRNL_APPRVL) {

        this.jsJournalApproval = JS_JRNL_APPRVL;
    }

    public char getJsEffectiveDateRule() {

        return jsEffectiveDateRule;
    }

    public void setJsEffectiveDateRule(char JS_EFFCTV_DT_RL) {

        this.jsEffectiveDateRule = JS_EFFCTV_DT_RL;
    }

    public Integer getJsAdCompany() {

        return jsAdCompany;
    }

    public void setJsAdCompany(Integer JS_AD_CMPNY) {

        this.jsAdCompany = JS_AD_CMPNY;
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

    public void addGlSuspenseAccount(LocalGlSuspenseAccount entity) {

        try {
            entity.setGlJournalSource(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlSuspenseAccount(LocalGlSuspenseAccount entity) {

        try {
            entity.setGlJournalSource(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlJournal(LocalGlJournal entity) {

        try {
            entity.setGlJournalSource(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlJournal(LocalGlJournal entity) {

        try {
            entity.setGlJournalSource(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}