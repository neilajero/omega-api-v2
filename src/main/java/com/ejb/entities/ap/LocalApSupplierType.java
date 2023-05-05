package com.ejb.entities.ap;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdBankAccount;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;

@Entity(name = "ApSupplierType")
@Table(name = "AP_SPPLR_TYP")
public class LocalApSupplierType extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AP_ST_CODE", nullable = false)
    private Integer stCode;

    @Column(name = "ST_NM", columnDefinition = "VARCHAR")
    private String stName;

    @Column(name = "ST_DESC", columnDefinition = "VARCHAR")
    private String stDescription;

    @Column(name = "ST_ENBL", columnDefinition = "TINYINT")
    private byte stEnable;

    @Column(name = "ST_AD_CMPNY", columnDefinition = "INT")
    private Integer stAdCompany;

    @JoinColumn(name = "AD_BANK_ACCOUNT", referencedColumnName = "BA_CODE")
    @ManyToOne
    private LocalAdBankAccount adBankAccount;

    @OneToMany(mappedBy = "apSupplierType", fetch = FetchType.LAZY)
    private List<LocalApSupplier> apSuppliers;

    public Integer getStCode() {

        return stCode;
    }

    public void setStCode(Integer AP_ST_CODE) {

        this.stCode = AP_ST_CODE;
    }

    public String getStName() {

        return stName;
    }

    public void setStName(String ST_NM) {

        this.stName = ST_NM;
    }

    public String getStDescription() {

        return stDescription;
    }

    public void setStDescription(String ST_DESC) {

        this.stDescription = ST_DESC;
    }

    public byte getStEnable() {

        return stEnable;
    }

    public void setStEnable(byte ST_ENBL) {

        this.stEnable = ST_ENBL;
    }

    public Integer getStAdCompany() {

        return stAdCompany;
    }

    public void setStAdCompany(Integer ST_AD_CMPNY) {

        this.stAdCompany = ST_AD_CMPNY;
    }

    public LocalAdBankAccount getAdBankAccount() {

        return adBankAccount;
    }

    public void setAdBankAccount(LocalAdBankAccount adBankAccount) {

        this.adBankAccount = adBankAccount;
    }

    @XmlTransient
    public List getApSuppliers() {

        return apSuppliers;
    }

    public void setApSuppliers(List apSuppliers) {

        this.apSuppliers = apSuppliers;
    }

    public void addApSupplier(LocalApSupplier entity) {

        try {
            entity.setApSupplierType(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApSupplier(LocalApSupplier entity) {

        try {
            entity.setApSupplierType(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}