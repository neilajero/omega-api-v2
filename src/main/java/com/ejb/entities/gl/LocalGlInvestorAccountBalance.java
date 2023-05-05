package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ar.LocalArReceipt;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;

@Entity(name = "GlInvestorAccountBalance")
@Table(name = "GL_INVTR_BLNC")
public class LocalGlInvestorAccountBalance extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IRAB_CODE", nullable = false)
    private Integer irabCode;

    @Column(name = "IRAB_TTL_DBT", columnDefinition = "DOUBLE")
    private double irabTotalDebit = 0;

    @Column(name = "IRAB_TTL_CRDT", columnDefinition = "DOUBLE")
    private double irabTotalCredit = 0;

    @Column(name = "IRAB_BNS", columnDefinition = "TINYINT")
    private byte irabBonus;

    @Column(name = "IRAB_INT", columnDefinition = "TINYINT")
    private byte irabInterest;

    @Column(name = "IRAB_TTL_BNS", columnDefinition = "DOUBLE")
    private double irabTotalBonus = 0;

    @Column(name = "IRAB_TTL_INT", columnDefinition = "DOUBLE")
    private double irabTotalInterest = 0;

    @Column(name = "IRAB_MNTHLY_BNS_RT", columnDefinition = "DOUBLE")
    private double irabMonthlyBonusRate = 0;

    @Column(name = "IRAB_MNTHLY_INT_RT", columnDefinition = "DOUBLE")
    private double irabMonthlyInterestRate = 0;

    @Column(name = "IRAB_BEG_BLNC", columnDefinition = "DOUBLE")
    private double irabBeginningBalance = 0;

    @Column(name = "IRAB_END_BLNC", columnDefinition = "DOUBLE")
    private double irabEndingBalance = 0;

    @Column(name = "IRAB_AD_CMPNY", columnDefinition = "INT")
    private Integer irabAdCompany;

    @JoinColumn(name = "AP_SUPPLIER", referencedColumnName = "SPL_CODE")
    @ManyToOne
    private LocalApSupplier apSupplier;

    @JoinColumn(name = "GL_ACCOUNTING_CALENDAR_VALUE", referencedColumnName = "ACV_CODE")
    @ManyToOne
    private LocalGlAccountingCalendarValue glAccountingCalendarValue;

    @OneToMany(mappedBy = "glInvestorAccountBalance", fetch = FetchType.LAZY)
    private List<LocalArReceipt> arReceipts;

    public Integer getIrabCode() {

        return irabCode;
    }

    public void setIrabCode(Integer IRAB_CODE) {

        this.irabCode = IRAB_CODE;
    }

    public double getIrabTotalDebit() {

        return irabTotalDebit;
    }

    public void setIrabTotalDebit(double IRAB_TTL_DBT) {

        this.irabTotalDebit = IRAB_TTL_DBT;
    }

    public double getIrabTotalCredit() {

        return irabTotalCredit;
    }

    public void setIrabTotalCredit(double IRAB_TTL_CRDT) {

        this.irabTotalCredit = IRAB_TTL_CRDT;
    }

    public byte getIrabBonus() {

        return irabBonus;
    }

    public void setIrabBonus(byte IRAB_BNS) {

        this.irabBonus = IRAB_BNS;
    }

    public byte getIrabInterest() {

        return irabInterest;
    }

    public void setIrabInterest(byte IRAB_INT) {

        this.irabInterest = IRAB_INT;
    }

    public double getIrabTotalBonus() {

        return irabTotalBonus;
    }

    public void setIrabTotalBonus(double IRAB_TTL_BNS) {

        this.irabTotalBonus = IRAB_TTL_BNS;
    }

    public double getIrabTotalInterest() {

        return irabTotalInterest;
    }

    public void setIrabTotalInterest(double IRAB_TTL_INT) {

        this.irabTotalInterest = IRAB_TTL_INT;
    }

    public double getIrabMonthlyBonusRate() {

        return irabMonthlyBonusRate;
    }

    public void setIrabMonthlyBonusRate(double IRAB_MNTHLY_BNS_RT) {

        this.irabMonthlyBonusRate = IRAB_MNTHLY_BNS_RT;
    }

    public double getIrabMonthlyInterestRate() {

        return irabMonthlyInterestRate;
    }

    public void setIrabMonthlyInterestRate(double IRAB_MNTHLY_INT_RT) {

        this.irabMonthlyInterestRate = IRAB_MNTHLY_INT_RT;
    }

    public double getIrabBeginningBalance() {

        return irabBeginningBalance;
    }

    public void setIrabBeginningBalance(double IRAB_BEG_BLNC) {

        this.irabBeginningBalance = IRAB_BEG_BLNC;
    }

    public double getIrabEndingBalance() {

        return irabEndingBalance;
    }

    public void setIrabEndingBalance(double IRAB_END_BLNC) {

        this.irabEndingBalance = IRAB_END_BLNC;
    }

    public Integer getIrabAdCompany() {

        return irabAdCompany;
    }

    public void setIrabAdCompany(Integer IRAB_AD_CMPNY) {

        this.irabAdCompany = IRAB_AD_CMPNY;
    }

    public LocalApSupplier getApSupplier() {

        return apSupplier;
    }

    public void setApSupplier(LocalApSupplier apSupplier) {

        this.apSupplier = apSupplier;
    }

    public LocalGlAccountingCalendarValue getGlAccountingCalendarValue() {

        return glAccountingCalendarValue;
    }

    public void setGlAccountingCalendarValue(
            LocalGlAccountingCalendarValue glAccountingCalendarValue) {

        this.glAccountingCalendarValue = glAccountingCalendarValue;
    }

    @XmlTransient
    public List getArReceipts() {

        return arReceipts;
    }

    public void setArReceipts(List arReceipts) {

        this.arReceipts = arReceipts;
    }

}