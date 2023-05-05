package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "GlFunctionalCurrencyRate")
@Table(name = "GL_FC_RT")
public class LocalGlFunctionalCurrencyRate extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FR_CODE", nullable = false)
    private Integer frCode;

    @Column(name = "FR_X_TO_USD", columnDefinition = "DOUBLE")
    private double frXToUsd = 0;

    @Column(name = "FR_DT", columnDefinition = "DATETIME")
    private Date frDate;

    @Column(name = "FR_AD_CMPNY", columnDefinition = "INT")
    private Integer frAdCompany;

    @JoinColumn(name = "GL_FUNCTIONAL_CURRENCY", referencedColumnName = "FC_CODE")
    @ManyToOne
    private LocalGlFunctionalCurrency glFunctionalCurrency;

    public Integer getFrCode() {

        return frCode;
    }

    public void setFrCode(Integer FR_CODE) {

        this.frCode = FR_CODE;
    }

    public double getFrXToUsd() {

        return frXToUsd;
    }

    public void setFrXToUsd(double FR_X_TO_USD) {

        this.frXToUsd = FR_X_TO_USD;
    }

    public Date getFrDate() {

        return frDate;
    }

    public void setFrDate(Date FR_DT) {

        this.frDate = FR_DT;
    }

    public Integer getFrAdCompany() {

        return frAdCompany;
    }

    public void setFrAdCompany(Integer FR_AD_CMPNY) {

        this.frAdCompany = FR_AD_CMPNY;
    }

    public LocalGlFunctionalCurrency getGlFunctionalCurrency() {

        return glFunctionalCurrency;
    }

    public void setGlFunctionalCurrency(LocalGlFunctionalCurrency glFunctionalCurrency) {

        this.glFunctionalCurrency = glFunctionalCurrency;
    }

}