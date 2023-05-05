package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "GlRecurringJournalLine")
@Table(name = "GL_RCRRNG_JRNL_LN")
public class LocalGlRecurringJournalLine extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RJL_CODE", nullable = false)
    private Integer rjlCode;

    @Column(name = "RJL_LN_NMBR", columnDefinition = "SMALLINT")
    private short rjlLineNumber;

    @Column(name = "RJL_DBT", columnDefinition = "TINYINT")
    private byte rjlDebit;

    @Column(name = "RJL_AMNT", columnDefinition = "DOUBLE")
    private double rjlAmount = 0;

    @Column(name = "RJL_AD_CMPNY", columnDefinition = "INT")
    private Integer rjlAdCompany;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    @JoinColumn(name = "GL_RECURRING_JOURNAL", referencedColumnName = "RJ_CODE")
    @ManyToOne
    private LocalGlRecurringJournal glRecurringJournal;

    public Integer getRjlCode() {

        return rjlCode;
    }

    public void setRjlCode(Integer RJL_CODE) {

        this.rjlCode = RJL_CODE;
    }

    public short getRjlLineNumber() {

        return rjlLineNumber;
    }

    public void setRjlLineNumber(short RJL_LN_NMBR) {

        this.rjlLineNumber = RJL_LN_NMBR;
    }

    public byte getRjlDebit() {

        return rjlDebit;
    }

    public void setRjlDebit(byte RJL_DBT) {

        this.rjlDebit = RJL_DBT;
    }

    public double getRjlAmount() {

        return rjlAmount;
    }

    public void setRjlAmount(double RJL_AMNT) {

        this.rjlAmount = RJL_AMNT;
    }

    public Integer getRjlAdCompany() {

        return rjlAdCompany;
    }

    public void setRjlAdCompany(Integer RJL_AD_CMPNY) {

        this.rjlAdCompany = RJL_AD_CMPNY;
    }

    public LocalGlChartOfAccount getGlChartOfAccount() {

        return glChartOfAccount;
    }

    public void setGlChartOfAccount(LocalGlChartOfAccount glChartOfAccount) {

        this.glChartOfAccount = glChartOfAccount;
    }

    public LocalGlRecurringJournal getGlRecurringJournal() {

        return glRecurringJournal;
    }

    public void setGlRecurringJournal(LocalGlRecurringJournal glRecurringJournal) {

        this.glRecurringJournal = glRecurringJournal;
    }

}