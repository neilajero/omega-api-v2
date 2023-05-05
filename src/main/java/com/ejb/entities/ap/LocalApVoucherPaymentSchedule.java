package com.ejb.entities.ap;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "ApVoucherPaymentSchedule")
@Table(name = "AP_VCHR_PYMNT_SCHDL")
public class LocalApVoucherPaymentSchedule extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VPS_CODE", nullable = false)
    private Integer vpsCode;

    @Column(name = "VPS_DUE_DT", columnDefinition = "DATETIME")
    private Date vpsDueDate;

    @Column(name = "VPS_NMBR", columnDefinition = "SMALLINT")
    private short vpsNumber;

    @Column(name = "VPS_AMNT_DUE", columnDefinition = "DOUBLE")
    private double vpsAmountDue = 0;

    @Column(name = "VPS_AMNT_PD", columnDefinition = "DOUBLE")
    private double vpsAmountPaid = 0;

    @Column(name = "VPS_LCK", columnDefinition = "TINYINT")
    private byte vpsLock;

    @Column(name = "VPS_AD_CMPNY", columnDefinition = "INT")
    private Integer vpsAdCompany;

    @JoinColumn(name = "AP_VOUCHER", referencedColumnName = "VOU_CODE")
    @ManyToOne
    private LocalApVoucher apVoucher;

    @OneToMany(mappedBy = "apVoucherPaymentSchedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalApAppliedVoucher> apAppliedVouchers;

    public Integer getVpsCode() {

        return vpsCode;
    }

    public void setVpsCode(Integer VPS_CODE) {

        this.vpsCode = VPS_CODE;
    }

    public Date getVpsDueDate() {

        return vpsDueDate;
    }

    public void setVpsDueDate(Date VPS_DUE_DT) {

        this.vpsDueDate = VPS_DUE_DT;
    }

    public short getVpsNumber() {

        return vpsNumber;
    }

    public void setVpsNumber(short VPS_NMBR) {

        this.vpsNumber = VPS_NMBR;
    }

    public double getVpsAmountDue() {

        return vpsAmountDue;
    }

    public void setVpsAmountDue(double VPS_AMNT_DUE) {

        this.vpsAmountDue = VPS_AMNT_DUE;
    }

    public double getVpsAmountPaid() {

        return vpsAmountPaid;
    }

    public void setVpsAmountPaid(double VPS_AMNT_PD) {

        this.vpsAmountPaid = VPS_AMNT_PD;
    }

    public byte getVpsLock() {

        return vpsLock;
    }

    public void setVpsLock(byte VPS_LCK) {

        this.vpsLock = VPS_LCK;
    }

    public Integer getVpsAdCompany() {

        return vpsAdCompany;
    }

    public void setVpsAdCompany(Integer VPS_AD_CMPNY) {

        this.vpsAdCompany = VPS_AD_CMPNY;
    }

    public LocalApVoucher getApVoucher() {

        return apVoucher;
    }

    public void setApVoucher(LocalApVoucher apVoucher) {

        this.apVoucher = apVoucher;
    }

    @XmlTransient
    public List getApAppliedVouchers() {

        return apAppliedVouchers;
    }

    public void setApAppliedVouchers(List apAppliedVouchers) {

        this.apAppliedVouchers = apAppliedVouchers;
    }

    public void addApAppliedVoucher(LocalApAppliedVoucher entity) {

        try {
            entity.setApVoucherPaymentSchedule(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApAppliedVoucher(LocalApAppliedVoucher entity) {

        try {
            entity.setApVoucherPaymentSchedule(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}