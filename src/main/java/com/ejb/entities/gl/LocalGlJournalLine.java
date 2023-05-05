package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "GlJournalLine")
@Table(name = "GL_JRNL_LN")
public class LocalGlJournalLine extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JL_CODE", nullable = false)
    private Integer jlCode;

    @Column(name = "JL_LN_NMBR", columnDefinition = "SMALLINT")
    private short jlLineNumber;

    @Column(name = "JL_DBT", columnDefinition = "TINYINT")
    private byte jlDebit;

    @Column(name = "JL_AMNT", columnDefinition = "DOUBLE")
    private double jlAmount = 0;

    @Column(name = "JL_DESC", columnDefinition = "VARCHAR")
    private String jlDescription;

    @Column(name = "JL_AD_CMPNY", columnDefinition = "INT")
    private Integer jlAdCompany;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    @JoinColumn(name = "GL_JOURNAL", referencedColumnName = "JR_CODE")
    @ManyToOne
    private LocalGlJournal glJournal;

    public Integer getJlCode() {

        return jlCode;
    }

    public void setJlCode(Integer JL_CODE) {

        this.jlCode = JL_CODE;
    }

    public short getJlLineNumber() {

        return jlLineNumber;
    }

    public void setJlLineNumber(short JL_LN_NMBR) {

        this.jlLineNumber = JL_LN_NMBR;
    }

    public byte getJlDebit() {

        return jlDebit;
    }

    public void setJlDebit(byte JL_DBT) {

        this.jlDebit = JL_DBT;
    }

    public double getJlAmount() {

        return jlAmount;
    }

    public void setJlAmount(double JL_AMNT) {

        this.jlAmount = JL_AMNT;
    }

    public String getJlDescription() {

        return jlDescription;
    }

    public void setJlDescription(String JL_DESC) {

        this.jlDescription = JL_DESC;
    }

    public Integer getJlAdCompany() {

        return jlAdCompany;
    }

    public void setJlAdCompany(Integer JL_AD_CMPNY) {

        this.jlAdCompany = JL_AD_CMPNY;
    }

    public LocalGlChartOfAccount getGlChartOfAccount() {

        return glChartOfAccount;
    }

    public void setGlChartOfAccount(LocalGlChartOfAccount glChartOfAccount) {

        this.glChartOfAccount = glChartOfAccount;
    }

    public LocalGlJournal getGlJournal() {

        return glJournal;
    }

    public void setGlJournal(LocalGlJournal glJournal) {

        this.glJournal = glJournal;
    }

}