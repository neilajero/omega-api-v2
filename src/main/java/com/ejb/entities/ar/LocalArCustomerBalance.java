package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "ArCustomerBalance")
@Table(name = "AR_CSTMR_BLNC")
public class LocalArCustomerBalance extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_CB_CODE", nullable = false)
    private Integer cbCode;

    @Column(name = "CB_DT", columnDefinition = "DATETIME")
    private Date cbDate;

    @Column(name = "CB_BLNC", columnDefinition = "DOUBLE")
    private double cbBalance = 0;

    @Column(name = "CB_AD_CMPNY", columnDefinition = "INT")
    private Integer cbAdCompany;

    @JoinColumn(name = "AR_CUSTOMER", referencedColumnName = "AR_CST_CODE")
    @ManyToOne
    private LocalArCustomer arCustomer;

    public Integer getCbCode() {

        return cbCode;
    }

    public void setCbCode(Integer AR_CB_CODE) {

        this.cbCode = AR_CB_CODE;
    }

    public Date getCbDate() {

        return cbDate;
    }

    public void setCbDate(Date CB_DT) {

        this.cbDate = CB_DT;
    }

    public double getCbBalance() {

        return cbBalance;
    }

    public void setCbBalance(double CB_BLNC) {

        this.cbBalance = CB_BLNC;
    }

    public Integer getCbAdCompany() {

        return cbAdCompany;
    }

    public void setCbAdCompany(Integer CB_AD_CMPNY) {

        this.cbAdCompany = CB_AD_CMPNY;
    }

    public LocalArCustomer getArCustomer() {

        return arCustomer;
    }

    public void setArCustomer(LocalArCustomer arCustomer) {

        this.arCustomer = arCustomer;
    }

}