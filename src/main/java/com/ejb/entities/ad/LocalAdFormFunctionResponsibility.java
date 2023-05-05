package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "AdFormFunctionResponsibility")
@Table(name = "AD_FF_RSPNSBLTY")
public class LocalAdFormFunctionResponsibility extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FR_CODE", nullable = false)
    private Integer frCode;

    @Column(name = "FR_PARAM", columnDefinition = "VARCHAR")
    private String frParameter;

    @Column(name = "FR_AD_CMPNY", columnDefinition = "INT")
    private Integer frAdCompany;

    @JoinColumn(name = "FR_FF_CODE", referencedColumnName = "FF_CODE")
    @ManyToOne
    private LocalAdFormFunction adFormFunction;

    @JoinColumn(name = "FR_RS_CODE", referencedColumnName = "AD_RS_CODE")
    //-- Verify if this change the relationship behavior
    @ManyToOne
    private LocalAdResponsibility adResponsibility;

    public Integer getFrCode() {

        return frCode;
    }

    public void setFrCode(Integer FR_CODE) {

        this.frCode = FR_CODE;
    }

    public String getFrParameter() {

        return frParameter;
    }

    public void setFrParameter(String FR_PARAM) {

        this.frParameter = FR_PARAM;
    }

    public Integer getFrAdCompany() {

        return frAdCompany;
    }

    public void setFrAdCompany(Integer FR_AD_CMPNY) {

        this.frAdCompany = FR_AD_CMPNY;
    }

    public LocalAdFormFunction getAdFormFunction() {

        return adFormFunction;
    }

    public void setAdFormFunction(LocalAdFormFunction adFormFunction) {

        this.adFormFunction = adFormFunction;
    }

    public LocalAdResponsibility getAdResponsibility() {

        return adResponsibility;
    }

    public void setAdResponsibility(LocalAdResponsibility adResponsibility) {

        this.adResponsibility = adResponsibility;
    }

}