package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "ArInvoicePaymentSchedule")
@Table(name = "AR_INVC_PYMNT_SCHDL")
public class LocalArInvoicePaymentSchedule extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IPS_CODE", nullable = false)
    private Integer ipsCode;

    @Column(name = "IPS_DUE_DT", columnDefinition = "DATETIME")
    private Date ipsDueDate;

    @Column(name = "IPS_NMBR", columnDefinition = "SMALLINT")
    private short ipsNumber;

    @Column(name = "IPS_AMNT_DUE", columnDefinition = "DOUBLE")
    private double ipsAmountDue = 0;

    @Column(name = "IPS_AMNT_PD", columnDefinition = "DOUBLE")
    private double ipsAmountPaid = 0;

    @Column(name = "IPS_INT_DUE", columnDefinition = "DOUBLE")
    private double ipsInterestDue = 0;

    @Column(name = "IPS_LCK", columnDefinition = "TINYINT")
    private byte ipsLock;

    @Column(name = "IPS_PNT_CTR", columnDefinition = "SMALLINT")
    private short ipsPenaltyCounter;

    @Column(name = "IPS_PNT_DUE_DT", columnDefinition = "DATETIME")
    private Date ipsPenaltyDueDate;

    @Column(name = "IPS_PNT_DUE", columnDefinition = "DOUBLE")
    private double ipsPenaltyDue = 0;

    @Column(name = "IPS_PNT_PD", columnDefinition = "DOUBLE")
    private double ipsPenaltyPaid = 0;

    @Column(name = "IPS_AD_CMPNY", columnDefinition = "INT")
    private Integer ipsAdCompany;

    @JoinColumn(name = "AR_INVOICE", referencedColumnName = "INV_CODE")
    @ManyToOne
    private LocalArInvoice arInvoice;

    @OneToMany(mappedBy = "arInvoicePaymentSchedule", fetch = FetchType.LAZY)
    private List<LocalArAppliedInvoice> arAppliedInvoices;

    public Integer getIpsCode() {

        return ipsCode;
    }

    public void setIpsCode(Integer IPS_CODE) {

        this.ipsCode = IPS_CODE;
    }

    public Date getIpsDueDate() {

        return ipsDueDate;
    }

    public void setIpsDueDate(Date IPS_DUE_DT) {

        this.ipsDueDate = IPS_DUE_DT;
    }

    public short getIpsNumber() {

        return ipsNumber;
    }

    public void setIpsNumber(short IPS_NMBR) {

        this.ipsNumber = IPS_NMBR;
    }

    public double getIpsAmountDue() {

        return ipsAmountDue;
    }

    public void setIpsAmountDue(double IPS_AMNT_DUE) {

        this.ipsAmountDue = IPS_AMNT_DUE;
    }

    public double getIpsAmountPaid() {

        return ipsAmountPaid;
    }

    public void setIpsAmountPaid(double IPS_AMNT_PD) {

        this.ipsAmountPaid = IPS_AMNT_PD;
    }

    public double getIpsInterestDue() {

        return ipsInterestDue;
    }

    public void setIpsInterestDue(double IPS_INT_DUE) {

        this.ipsInterestDue = IPS_INT_DUE;
    }

    public byte getIpsLock() {

        return ipsLock;
    }

    public void setIpsLock(byte IPS_LCK) {

        this.ipsLock = IPS_LCK;
    }

    public short getIpsPenaltyCounter() {

        return ipsPenaltyCounter;
    }

    public void setIpsPenaltyCounter(short IPS_PNT_CTR) {

        this.ipsPenaltyCounter = IPS_PNT_CTR;
    }

    public Date getIpsPenaltyDueDate() {

        return ipsPenaltyDueDate;
    }

    public void setIpsPenaltyDueDate(Date IPS_PNT_DUE_DT) {

        this.ipsPenaltyDueDate = IPS_PNT_DUE_DT;
    }

    public double getIpsPenaltyDue() {

        return ipsPenaltyDue;
    }

    public void setIpsPenaltyDue(double IPS_PNT_DUE) {

        this.ipsPenaltyDue = IPS_PNT_DUE;
    }

    public double getIpsPenaltyPaid() {

        return ipsPenaltyPaid;
    }

    public void setIpsPenaltyPaid(double IPS_PNT_PD) {

        this.ipsPenaltyPaid = IPS_PNT_PD;
    }

    public Integer getIpsAdCompany() {

        return ipsAdCompany;
    }

    public void setIpsAdCompany(Integer IPS_AD_CMPNY) {

        this.ipsAdCompany = IPS_AD_CMPNY;
    }

    public LocalArInvoice getArInvoice() {

        return arInvoice;
    }

    public void setArInvoice(LocalArInvoice arInvoice) {

        this.arInvoice = arInvoice;
    }

    @XmlTransient
    public List getArAppliedInvoices() {

        return arAppliedInvoices;
    }

    public void setArAppliedInvoices(List arAppliedInvoices) {

        this.arAppliedInvoices = arAppliedInvoices;
    }

    public void addArAppliedInvoice(LocalArAppliedInvoice entity) {

        try {
            entity.setArInvoicePaymentSchedule(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArAppliedInvoice(LocalArAppliedInvoice entity) {

        try {
            entity.setArInvoicePaymentSchedule(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}