package com.ejb.entities.ar;

import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.entities.inv.LocalInvTag;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ArSalesOrderInvoiceLine")
@Table(name = "AR_SLS_ORDR_INVC_LN")
public class LocalArSalesOrderInvoiceLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_SIL_CODE", nullable = false)
    private Integer silCode;

    @Column(name = "SIL_QTY_DLVRD", columnDefinition = "DOUBLE")
    private double silQuantityDelivered = 0;

    @Column(name = "SIL_AMNT", columnDefinition = "DOUBLE")
    private double silAmount = 0;

    @Column(name = "SIL_TX_AMNT", columnDefinition = "DOUBLE")
    private double silTaxAmount = 0;

    @Column(name = "SIL_DSCNT_1", columnDefinition = "DOUBLE")
    private double silDiscount1 = 0;

    @Column(name = "SIL_DSCNT_2", columnDefinition = "DOUBLE")
    private double silDiscount2 = 0;

    @Column(name = "SIL_DSCNT_3", columnDefinition = "DOUBLE")
    private double silDiscount3 = 0;

    @Column(name = "SIL_DSCNT_4", columnDefinition = "DOUBLE")
    private double silDiscount4 = 0;

    @Column(name = "SIL_TTL_DSCNT", columnDefinition = "DOUBLE")
    private double silTotalDiscount = 0;

    @Column(name = "SIL_IMEI", columnDefinition = "VARCHAR")
    private String silImei;

    @Column(name = "SIL_TX", columnDefinition = "TINYINT")
    private byte silTax;

    @Column(name = "SIL_AD_CMPNY", columnDefinition = "INT")
    private Integer silAdCompany;

    @JoinColumn(name = "AR_INVOICE", referencedColumnName = "INV_CODE")
    @ManyToOne
    private LocalArInvoice arInvoice;

    @JoinColumn(name = "AR_SALES_ORDER_LINE", referencedColumnName = "SOL_CODE")
    @ManyToOne
    private LocalArSalesOrderLine arSalesOrderLine;

    @OneToMany(mappedBy = "arSalesOrderInvoiceLine", fetch = FetchType.LAZY)
    private List<LocalInvTag> invTags;

    @OneToMany(mappedBy = "arSalesOrderInvoiceLine", fetch = FetchType.LAZY)
    private List<LocalInvCosting> invCostings;

    public Integer getSilCode() {

        return silCode;
    }

    public void setSilCode(Integer AR_SIL_CODE) {

        this.silCode = AR_SIL_CODE;
    }

    public double getSilQuantityDelivered() {

        return silQuantityDelivered;
    }

    public void setSilQuantityDelivered(double SIL_QTY_DLVRD) {

        this.silQuantityDelivered = SIL_QTY_DLVRD;
    }

    public double getSilAmount() {

        return silAmount;
    }

    public void setSilAmount(double SIL_AMNT) {

        this.silAmount = SIL_AMNT;
    }

    public double getSilTaxAmount() {

        return silTaxAmount;
    }

    public void setSilTaxAmount(double SIL_TX_AMNT) {

        this.silTaxAmount = SIL_TX_AMNT;
    }

    public double getSilDiscount1() {

        return silDiscount1;
    }

    public void setSilDiscount1(double SIL_DSCNT_1) {

        this.silDiscount1 = SIL_DSCNT_1;
    }

    public double getSilDiscount2() {

        return silDiscount2;
    }

    public void setSilDiscount2(double SIL_DSCNT_2) {

        this.silDiscount2 = SIL_DSCNT_2;
    }

    public double getSilDiscount3() {

        return silDiscount3;
    }

    public void setSilDiscount3(double SIL_DSCNT_3) {

        this.silDiscount3 = SIL_DSCNT_3;
    }

    public double getSilDiscount4() {

        return silDiscount4;
    }

    public void setSilDiscount4(double SIL_DSCNT_4) {

        this.silDiscount4 = SIL_DSCNT_4;
    }

    public double getSilTotalDiscount() {

        return silTotalDiscount;
    }

    public void setSilTotalDiscount(double SIL_TTL_DSCNT) {

        this.silTotalDiscount = SIL_TTL_DSCNT;
    }

    public String getSilImei() {

        return silImei;
    }

    public void setSilImei(String SIL_IMEI) {

        this.silImei = SIL_IMEI;
    }

    public byte getSilTax() {

        return silTax;
    }

    public void setSilTax(byte SIL_TX) {

        this.silTax = SIL_TX;
    }

    public Integer getSilAdCompany() {

        return silAdCompany;
    }

    public void setSilAdCompany(Integer SIL_AD_CMPNY) {

        this.silAdCompany = SIL_AD_CMPNY;
    }

    public LocalArInvoice getArInvoice() {

        return arInvoice;
    }

    public void setArInvoice(LocalArInvoice arInvoice) {

        this.arInvoice = arInvoice;
    }

    public LocalArSalesOrderLine getArSalesOrderLine() {

        return arSalesOrderLine;
    }

    public void setArSalesOrderLine(LocalArSalesOrderLine arSalesOrderLine) {

        this.arSalesOrderLine = arSalesOrderLine;
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