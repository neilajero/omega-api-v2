package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "AdBankAccountBalance")
@Table(name = "AD_BNK_ACCNT_BLNC")
public class LocalAdBankAccountBalance extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BAB_CODE", nullable = false)
    private Integer babCode;

    @Column(name = "BAB_DT", columnDefinition = "DATETIME")
    private Date babDate;

    @Column(name = "BAB_BLNC", columnDefinition = "DOUBLE")
    private double babBalance = 0;

    @Column(name = "BAB_TYP", columnDefinition = "VARCHAR")
    private String babType;

    @Column(name = "BAB_AD_CMPNY", columnDefinition = "INT")
    private Integer babAdCompany;

    @JoinColumn(name = "AD_BANK_ACCOUNT", referencedColumnName = "BA_CODE")
    @ManyToOne
    private LocalAdBankAccount adBankAccount;

    public Integer getBabCode() {

        return babCode;
    }

    public void setBabCode(Integer BAB_CODE) {

        this.babCode = BAB_CODE;
    }

    public Date getBabDate() {

        return babDate;
    }

    public void setBabDate(Date BAB_DT) {

        this.babDate = BAB_DT;
    }

    public double getBabBalance() {

        return babBalance;
    }

    public void setBabBalance(double BAB_BLNC) {

        this.babBalance = BAB_BLNC;
    }

    public String getBabType() {

        return babType;
    }

    public void setBabType(String BAB_TYP) {

        this.babType = BAB_TYP;
    }

    public Integer getBabAdCompany() {

        return babAdCompany;
    }

    public void setBabAdCompany(Integer BAB_AD_CMPNY) {

        this.babAdCompany = BAB_AD_CMPNY;
    }

    public LocalAdBankAccount getAdBankAccount() {

        return adBankAccount;
    }

    public void setAdBankAccount(LocalAdBankAccount adBankAccount) {

        this.adBankAccount = adBankAccount;
    }

}