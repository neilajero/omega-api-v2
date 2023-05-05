package com.ejb.entities.ar;

import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.entities.inv.LocalInvTag;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ArJobOrderInvoiceLine")
@Table(name = "AR_JB_ORDR_INVC_LN")
public class LocalArJobOrderInvoiceLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JIL_CODE", nullable = false)
    private Integer jilCode;

    @Column(name = "JIL_QTY_DLVRD", columnDefinition = "DOUBLE")
    private double jilQuantityDelivered = 0;

    @Column(name = "JIL_AMNT", columnDefinition = "DOUBLE")
    private double jilAmount = 0;

    @Column(name = "JIL_TX_AMNT", columnDefinition = "DOUBLE")
    private double jilTaxAmount = 0;

    @Column(name = "JIL_DSCNT_1", columnDefinition = "DOUBLE")
    private double jilDiscount1 = 0;

    @Column(name = "JIL_DSCNT_2", columnDefinition = "DOUBLE")
    private double jilDiscount2 = 0;

    @Column(name = "JIL_DSCNT_3", columnDefinition = "DOUBLE")
    private double jilDiscount3 = 0;

    @Column(name = "JIL_DSCNT_4", columnDefinition = "DOUBLE")
    private double jilDiscount4 = 0;

    @Column(name = "JIL_TTL_DSCNT", columnDefinition = "DOUBLE")
    private double jilTotalDiscount = 0;

    @Column(name = "JIL_IMEI", columnDefinition = "VARCHAR")
    private String jilImei;

    @Column(name = "JIL_TX", columnDefinition = "TINYINT")
    private byte jilTax;

    @Column(name = "JIL_AD_CMPNY", columnDefinition = "INT")
    private Integer jilAdCompany;

    @JoinColumn(name = "AR_INVOICE", referencedColumnName = "INV_CODE")
    @ManyToOne
    private LocalArInvoice arInvoice;

    @JoinColumn(name = "AR_JOB_ORDER_LINE", referencedColumnName = "JOL_CODE")
    @ManyToOne
    private LocalArJobOrderLine arJobOrderLine;

    @OneToMany(mappedBy = "arJobOrderInvoiceLine", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvTag> invTags;

    @OneToMany(mappedBy = "arJobOrderInvoiceLine", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvCosting> invCostings;

    public Integer getJilCode() {

        return jilCode;
    }

    public void setJilCode(Integer JIL_CODE) {

        this.jilCode = JIL_CODE;
    }

    public double getJilQuantityDelivered() {

        return jilQuantityDelivered;
    }

    public void setJilQuantityDelivered(double JIL_QTY_DLVRD) {

        this.jilQuantityDelivered = JIL_QTY_DLVRD;
    }

    public double getJilAmount() {

        return jilAmount;
    }

    public void setJilAmount(double JIL_AMNT) {

        this.jilAmount = JIL_AMNT;
    }

    public double getJilTaxAmount() {

        return jilTaxAmount;
    }

    public void setJilTaxAmount(double JIL_TX_AMNT) {

        this.jilTaxAmount = JIL_TX_AMNT;
    }

    public double getJilDiscount1() {

        return jilDiscount1;
    }

    public void setJilDiscount1(double JIL_DSCNT_1) {

        this.jilDiscount1 = JIL_DSCNT_1;
    }

    public double getJilDiscount2() {

        return jilDiscount2;
    }

    public void setJilDiscount2(double JIL_DSCNT_2) {

        this.jilDiscount2 = JIL_DSCNT_2;
    }

    public double getJilDiscount3() {

        return jilDiscount3;
    }

    public void setJilDiscount3(double JIL_DSCNT_3) {

        this.jilDiscount3 = JIL_DSCNT_3;
    }

    public double getJilDiscount4() {

        return jilDiscount4;
    }

    public void setJilDiscount4(double JIL_DSCNT_4) {

        this.jilDiscount4 = JIL_DSCNT_4;
    }

    public double getJilTotalDiscount() {

        return jilTotalDiscount;
    }

    public void setJilTotalDiscount(double JIL_TTL_DSCNT) {

        this.jilTotalDiscount = JIL_TTL_DSCNT;
    }

    public String getJilImei() {

        return jilImei;
    }

    public void setJilImei(String JIL_IMEI) {

        this.jilImei = JIL_IMEI;
    }

    public byte getJilTax() {

        return jilTax;
    }

    public void setJilTax(byte JIL_TX) {

        this.jilTax = JIL_TX;
    }

    public Integer getJilAdCompany() {

        return jilAdCompany;
    }

    public void setJilAdCompany(Integer JIL_AD_CMPNY) {

        this.jilAdCompany = JIL_AD_CMPNY;
    }

    public LocalArInvoice getArInvoice() {

        return arInvoice;
    }

    public void setArInvoice(LocalArInvoice arInvoice) {

        this.arInvoice = arInvoice;
    }

    public LocalArJobOrderLine getArJobOrderLine() {

        return arJobOrderLine;
    }

    public void setArJobOrderLine(LocalArJobOrderLine arJobOrderLine) {

        this.arJobOrderLine = arJobOrderLine;
    }

    @XmlTransient
    public List getInvTags() {

        return invTags;
    }

    public void setInvTags(List invTags) {

        this.invTags = invTags;
    }

    @XmlTransient
    public List getInvCostings() {

        return invCostings;
    }

    public void setInvCostings(List invCostings) {

        this.invCostings = invCostings;
    }

}