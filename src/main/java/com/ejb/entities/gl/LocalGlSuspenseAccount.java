package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "GlSuspenseAccount")
@Table(name = "GL_SSPNS_ACCNT")
public class LocalGlSuspenseAccount extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SA_CODE", nullable = false)
    private Integer saCode;

    @Column(name = "SA_NM", columnDefinition = "VARCHAR")
    private String saName;

    @Column(name = "SA_DESC", columnDefinition = "VARCHAR")
    private String saDescription;

    @Column(name = "SA_AD_CMPNY", columnDefinition = "INT")
    private Integer saAdCompany;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    @JoinColumn(name = "GL_JOURNAL_CATEGORY", referencedColumnName = "JC_CODE")
    @ManyToOne
    private LocalGlJournalCategory glJournalCategory;

    @JoinColumn(name = "GL_JOURNAL_SOURCE", referencedColumnName = "JS_CODE")
    @ManyToOne
    private LocalGlJournalSource glJournalSource;

    public Integer getSaCode() {

        return saCode;
    }

    public void setSaCode(Integer SA_CODE) {

        this.saCode = SA_CODE;
    }

    public String getSaName() {

        return saName;
    }

    public void setSaName(String SA_NM) {

        this.saName = SA_NM;
    }

    public String getSaDescription() {

        return saDescription;
    }

    public void setSaDescription(String SA_DESC) {

        this.saDescription = SA_DESC;
    }

    public Integer getSaAdCompany() {

        return saAdCompany;
    }

    public void setSaAdCompany(Integer SA_AD_CMPNY) {

        this.saAdCompany = SA_AD_CMPNY;
    }

    public LocalGlChartOfAccount getGlChartOfAccount() {

        return glChartOfAccount;
    }

    public void setGlChartOfAccount(LocalGlChartOfAccount glChartOfAccount) {

        this.glChartOfAccount = glChartOfAccount;
    }

    public LocalGlJournalCategory getGlJournalCategory() {

        return glJournalCategory;
    }

    public void setGlJournalCategory(LocalGlJournalCategory glJournalCategory) {

        this.glJournalCategory = glJournalCategory;
    }

    public LocalGlJournalSource getGlJournalSource() {

        return glJournalSource;
    }

    public void setGlJournalSource(LocalGlJournalSource glJournalSource) {

        this.glJournalSource = glJournalSource;
    }

}