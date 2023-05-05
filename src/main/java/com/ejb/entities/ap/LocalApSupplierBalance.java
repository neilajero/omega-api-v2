package com.ejb.entities.ap;

import com.ejb.NativeQueryHome;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "ApSupplierBalance")
@Table(name = "AP_SPPLR_BLNC")
public class LocalApSupplierBalance extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SB_CODE", nullable = false)
    private Integer sbCode;

    @Column(name = "SB_DT", columnDefinition = "DATETIME")
    private Date sbDate;

    @Column(name = "SB_BLNC", columnDefinition = "DOUBLE")
    private double sbBalance = 0;

    @Column(name = "SB_AD_CMPNY", columnDefinition = "INT")
    private Integer sbAdCompany;

    @JoinColumn(name = "AP_SUPPLIER", referencedColumnName = "SPL_CODE")
    @ManyToOne
    private LocalApSupplier apSupplier;

    public Integer getSbCode() {

        return sbCode;
    }

    public void setSbCode(Integer SB_CODE) {

        this.sbCode = SB_CODE;
    }

    public Date getSbDate() {

        return sbDate;
    }

    public void setSbDate(Date SB_DT) {

        this.sbDate = SB_DT;
    }

    public double getSbBalance() {

        return sbBalance;
    }

    public void setSbBalance(double SB_BLNC) {

        this.sbBalance = SB_BLNC;
    }

    public Integer getSbAdCompany() {

        return sbAdCompany;
    }

    public void setSbAdCompany(Integer SB_AD_CMPNY) {

        this.sbAdCompany = SB_AD_CMPNY;
    }

    public LocalApSupplier getApSupplier() {

        return apSupplier;
    }

    public void setApSupplier(LocalApSupplier apSupplier) {

        this.apSupplier = apSupplier;
    }

}