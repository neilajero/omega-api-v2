package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdDiscount")
@Table(name = "AD_DSCNT")
public class LocalAdDiscount extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DSC_CODE", nullable = false)
    private Integer dscCode;

    @Column(name = "DSC_DSCNT_PRCNT", columnDefinition = "DOUBLE")
    private double dscDiscountPercent = 0;

    @Column(name = "DSC_PD_WTHN_DY", columnDefinition = "SMALLINT")
    private short dscPaidWithinDay;

    @Column(name = "DSC_AD_CMPNY", columnDefinition = "VARCHAR")
    private Integer dscAdCompany;

    @JoinColumn(name = "AD_PAYMENT_SCHEDULE", referencedColumnName = "PS_CODE")
    @ManyToOne
    private LocalAdPaymentSchedule adPaymentSchedule;

    public Integer getDscCode() {

        return dscCode;
    }

    public void setDscCode(Integer DSC_CODE) {

        this.dscCode = DSC_CODE;
    }

    public double getDscDiscountPercent() {

        return dscDiscountPercent;
    }

    public void setDscDiscountPercent(double DSC_DSCNT_PRCNT) {

        this.dscDiscountPercent = DSC_DSCNT_PRCNT;
    }

    public short getDscPaidWithinDay() {

        return dscPaidWithinDay;
    }

    public void setDscPaidWithinDay(short DSC_PD_WTHN_DY) {

        this.dscPaidWithinDay = DSC_PD_WTHN_DY;
    }

    public Integer getDscAdCompany() {

        return dscAdCompany;
    }

    public void setDscAdCompany(Integer DSC_AD_CMPNY) {

        this.dscAdCompany = DSC_AD_CMPNY;
    }

    public LocalAdPaymentSchedule getAdPaymentSchedule() {

        return adPaymentSchedule;
    }

    public void setAdPaymentSchedule(LocalAdPaymentSchedule adPaymentSchedule) {

        this.adPaymentSchedule = adPaymentSchedule;
    }

}