package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdApprovalUser")
@Table(name = "AD_APPRVL_USR")
public class LocalAdApprovalUser extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AU_CODE", nullable = false)
    private Integer auCode;

    @Column(name = "AU_LVL", columnDefinition = "VARCHAR")
    private String auLevel;

    @Column(name = "AU_TYP", columnDefinition = "VARCHAR")
    private String auType;

    @Column(name = "AU_OR", columnDefinition = "TINYINT")
    private byte auOr;

    @Column(name = "AU_AD_CMPNY", columnDefinition = "INT")
    private Integer auAdCompany;

    @JoinColumn(name = "AD_AMOUNT_LIMIT", referencedColumnName = "AD_CAL_CODE")
    @ManyToOne
    private LocalAdAmountLimit adAmountLimit;

    @JoinColumn(name = "AD_USER", referencedColumnName = "USR_CODE")
    @ManyToOne
    private LocalAdUser adUser;

    public Integer getAuCode() {

        return auCode;
    }

    public void setAuCode(Integer AU_CODE) {

        this.auCode = AU_CODE;
    }

    public String getAuLevel() {

        return auLevel;
    }

    public void setAuLevel(String AU_LVL) {

        this.auLevel = AU_LVL;
    }

    public String getAuType() {

        return auType;
    }

    public void setAuType(String AU_TYP) {

        this.auType = AU_TYP;
    }

    public byte getAuOr() {

        return auOr;
    }

    public void setAuOr(byte AU_OR) {

        this.auOr = AU_OR;
    }

    public Integer getAuAdCompany() {

        return auAdCompany;
    }

    public void setAuAdCompany(Integer AU_AD_CMPNY) {

        this.auAdCompany = AU_AD_CMPNY;
    }

    public LocalAdAmountLimit getAdAmountLimit() {

        return adAmountLimit;
    }

    public void setAdAmountLimit(LocalAdAmountLimit adAmountLimit) {

        this.adAmountLimit = adAmountLimit;
    }

    public LocalAdUser getAdUser() {

        return adUser;
    }

    public void setAdUser(LocalAdUser adUser) {

        this.adUser = adUser;
    }

}