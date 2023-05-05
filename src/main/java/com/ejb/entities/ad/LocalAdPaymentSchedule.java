package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "AdPaymentSchedule")
@Table(name = "AD_PYMNT_SCHDL")
public class LocalAdPaymentSchedule extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PS_CODE", nullable = false)
    private Integer psCode;

    @Column(name = "PS_LN_NMBR", columnDefinition = "SMALLINT")
    private short psLineNumber;

    @Column(name = "PS_RLTV_AMNT", columnDefinition = "DOUBLE")
    private double psRelativeAmount = 0;

    @Column(name = "PS_DUE_DY", columnDefinition = "SMALLINT")
    private short psDueDay;

    @Column(name = "PS_AD_CMPNY", columnDefinition = "INT")
    private Integer psAdCompany;

    @JoinColumn(name = "AD_PAYMENT_TERM", referencedColumnName = "PYT_CODE")
    @ManyToOne
    private LocalAdPaymentTerm adPaymentTerm;

    @OneToMany(mappedBy = "adPaymentSchedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalAdDiscount> adDiscounts;

    public Integer getPsCode() {

        return psCode;
    }

    public void setPsCode(Integer PS_CODE) {

        this.psCode = PS_CODE;
    }

    public short getPsLineNumber() {

        return psLineNumber;
    }

    public void setPsLineNumber(short PS_LN_NMBR) {

        this.psLineNumber = PS_LN_NMBR;
    }

    public double getPsRelativeAmount() {

        return psRelativeAmount;
    }

    public void setPsRelativeAmount(double PS_RLTV_AMNT) {

        this.psRelativeAmount = PS_RLTV_AMNT;
    }

    public short getPsDueDay() {

        return psDueDay;
    }

    public void setPsDueDay(short PS_DUE_DY) {

        this.psDueDay = PS_DUE_DY;
    }

    public Integer getPsAdCompany() {

        return psAdCompany;
    }

    public void setPsAdCompany(Integer PS_AD_CMPNY) {

        this.psAdCompany = PS_AD_CMPNY;
    }

    public LocalAdPaymentTerm getAdPaymentTerm() {

        return adPaymentTerm;
    }

    public void setAdPaymentTerm(LocalAdPaymentTerm adPaymentTerm) {

        this.adPaymentTerm = adPaymentTerm;
    }

    @XmlTransient
    public List getAdDiscounts() {

        return adDiscounts;
    }

    public void setAdDiscounts(List adDiscounts) {

        this.adDiscounts = adDiscounts;
    }

    public void addAdDiscount(LocalAdDiscount entity) {

        try {
            entity.setAdPaymentSchedule(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdDiscount(LocalAdDiscount entity) {

        try {
            entity.setAdPaymentSchedule(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}