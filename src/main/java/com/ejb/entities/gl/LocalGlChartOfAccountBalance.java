package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "GlChartOfAccountBalance")
@Table(name = "GL_COA_BLNC")
public class LocalGlChartOfAccountBalance extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COAB_CODE", nullable = false)
    private Integer coabCode;

    @Column(name = "COAB_TTL_DBT", columnDefinition = "DOUBLE")
    private double coabTotalDebit = 0;

    @Column(name = "COAB_TTL_CRDT", columnDefinition = "DOUBLE")
    private double coabTotalCredit = 0;

    @Column(name = "COAB_BEG_BLNC", columnDefinition = "DOUBLE")
    private double coabBeginningBalance = 0;

    @Column(name = "COAB_END_BLNC", columnDefinition = "DOUBLE")
    private double coabEndingBalance = 0;

    @Column(name = "COAB_AD_CMPNY", columnDefinition = "INT")
    private Integer coabAdCompany;

    @JoinColumn(name = "GL_ACCOUNTING_CALENDAR_VALUE", referencedColumnName = "ACV_CODE")
    @ManyToOne
    private LocalGlAccountingCalendarValue glAccountingCalendarValue;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    public Integer getCoabCode() {

        return coabCode;
    }

    public void setCoabCode(Integer COAB_CODE) {

        this.coabCode = COAB_CODE;
    }

    public double getCoabTotalDebit() {

        return coabTotalDebit;
    }

    public void setCoabTotalDebit(double COAB_TTL_DBT) {

        this.coabTotalDebit = COAB_TTL_DBT;
    }

    public double getCoabTotalCredit() {

        return coabTotalCredit;
    }

    public void setCoabTotalCredit(double COAB_TTL_CRDT) {

        this.coabTotalCredit = COAB_TTL_CRDT;
    }

    public double getCoabBeginningBalance() {

        return coabBeginningBalance;
    }

    public void setCoabBeginningBalance(double COAB_BEG_BLNC) {

        this.coabBeginningBalance = COAB_BEG_BLNC;
    }

    public double getCoabEndingBalance() {

        return coabEndingBalance;
    }

    public void setCoabEndingBalance(double COAB_END_BLNC) {

        this.coabEndingBalance = COAB_END_BLNC;
    }

    public Integer getCoabAdCompany() {

        return coabAdCompany;
    }

    public void setCoabAdCompany(Integer COAB_AD_CMPNY) {

        this.coabAdCompany = COAB_AD_CMPNY;
    }

    public LocalGlAccountingCalendarValue getGlAccountingCalendarValue() {

        return glAccountingCalendarValue;
    }

    public void setGlAccountingCalendarValue(
            LocalGlAccountingCalendarValue glAccountingCalendarValue) {

        this.glAccountingCalendarValue = glAccountingCalendarValue;
    }

    public LocalGlChartOfAccount getGlChartOfAccount() {

        return glChartOfAccount;
    }

    public void setGlChartOfAccount(LocalGlChartOfAccount glChartOfAccount) {

        this.glChartOfAccount = glChartOfAccount;
    }

}