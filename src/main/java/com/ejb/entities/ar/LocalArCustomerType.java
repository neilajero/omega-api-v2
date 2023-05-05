package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdBankAccount;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;

@Entity(name = "ArCustomerType")
@Table(name = "AR_CSTMR_TYP")
public class LocalArCustomerType extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CT_CODE", nullable = false)
    private Integer ctCode;

    @Column(name = "CT_NM", columnDefinition = "VARCHAR")
    private String ctName;

    @Column(name = "CT_DESC", columnDefinition = "VARCHAR")
    private String ctDescription;

    @Column(name = "CT_ENBL", columnDefinition = "TINYINT")
    private byte ctEnable;

    @Column(name = "CT_AD_CMPNY", columnDefinition = "INT")
    private Integer ctAdCompany;

    @JoinColumn(name = "AD_BANK_ACCOUNT", referencedColumnName = "BA_CODE")
    @ManyToOne
    private LocalAdBankAccount adBankAccount;

    @OneToMany(mappedBy = "arCustomerType", fetch = FetchType.LAZY)
    private List<LocalArCustomer> arCustomers;

    public Integer getCtCode() {

        return ctCode;
    }

    public void setCtCode(Integer CT_CODE) {

        this.ctCode = CT_CODE;
    }

    public String getCtName() {

        return ctName;
    }

    public void setCtName(String CT_NM) {

        this.ctName = CT_NM;
    }

    public String getCtDescription() {

        return ctDescription;
    }

    public void setCtDescription(String CT_DESC) {

        this.ctDescription = CT_DESC;
    }

    public byte getCtEnable() {

        return ctEnable;
    }

    public void setCtEnable(byte CT_ENBL) {

        this.ctEnable = CT_ENBL;
    }

    public Integer getCtAdCompany() {

        return ctAdCompany;
    }

    public void setCtAdCompany(Integer CT_AD_CMPNY) {

        this.ctAdCompany = CT_AD_CMPNY;
    }

    public LocalAdBankAccount getAdBankAccount() {

        return adBankAccount;
    }

    public void setAdBankAccount(LocalAdBankAccount adBankAccount) {

        this.adBankAccount = adBankAccount;
    }

    @XmlTransient
    public List getArCustomers() {

        return arCustomers;
    }

    public void setArCustomers(List arCustomers) {

        this.arCustomers = arCustomers;
    }

    public void addArCustomer(LocalArCustomer entity) {

        try {
            entity.setArCustomerType(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArCustomer(LocalArCustomer entity) {

        try {
            entity.setArCustomerType(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void setValues(Object entity) {

        LocalArCustomerType arCustomerType = (LocalArCustomerType) entity;

        this.ctCode = arCustomerType.ctCode;
        this.ctName = arCustomerType.ctName;
        this.ctDescription = arCustomerType.ctDescription;
        this.ctEnable = arCustomerType.ctEnable;
        this.ctAdCompany = arCustomerType.ctAdCompany;
    }

}