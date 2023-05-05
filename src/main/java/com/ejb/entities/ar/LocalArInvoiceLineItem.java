package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.entities.inv.LocalInvTag;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ArInvoiceLineItem")
@Table(name = "AR_INVC_LN_ITM")
public class LocalArInvoiceLineItem extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ILI_CODE", nullable = false)
    private Integer iliCode;

    @Column(name = "ILI_LN", columnDefinition = "SMALLINT")
    private short iliLine;

    @Column(name = "ILI_QTY", columnDefinition = "DOUBLE")
    private double iliQuantity = 0;

    @Column(name = "ILI_UNT_PRC", columnDefinition = "DOUBLE")
    private double iliUnitPrice = 0;

    @Column(name = "ILI_AMNT", columnDefinition = "DOUBLE")
    private double iliAmount = 0;

    @Column(name = "ILI_TX_AMNT", columnDefinition = "DOUBLE")
    private double iliTaxAmount = 0;

    @Column(name = "ILI_DSCNT_1", columnDefinition = "DOUBLE")
    private double iliDiscount1 = 0;

    @Column(name = "ILI_DSCNT_2", columnDefinition = "DOUBLE")
    private double iliDiscount2 = 0;

    @Column(name = "ILI_DSCNT_3", columnDefinition = "DOUBLE")
    private double iliDiscount3 = 0;

    @Column(name = "ILI_DSCNT_4", columnDefinition = "DOUBLE")
    private double iliDiscount4 = 0;

    @Column(name = "ILI_TTL_DSCNT", columnDefinition = "DOUBLE")
    private double iliTotalDiscount = 0;

    @Column(name = "ILI_EXPRY_DT", columnDefinition = "LONGTEXT") //TODO: Review this field as it make some confusion
    private String iliMisc;

    @Column(name = "ILI_IMEI", columnDefinition = "VARCHAR")
    private String iliImei;

    @Column(name = "ILI_TX", columnDefinition = "TINYINT")
    private byte iliTax;

    @Column(name = "ILI_AD_CMPNY", columnDefinition = "INT")
    private Integer iliAdCompany;

    @JoinColumn(name = "AR_INVOICE", referencedColumnName = "INV_CODE")
    @ManyToOne
    private LocalArInvoice arInvoice;

    @JoinColumn(name = "AR_PDC", referencedColumnName = "PDC_CODE")
    @ManyToOne
    private LocalArPdc arPdc;

    @JoinColumn(name = "AR_RECEIPT", referencedColumnName = "RCT_CODE")
    @ManyToOne
    private LocalArReceipt arReceipt;

    @JoinColumn(name = "INV_ITEM_LOCATION", referencedColumnName = "INV_IL_CODE")
    @ManyToOne
    private LocalInvItemLocation invItemLocation;

    @JoinColumn(name = "INV_UNIT_OF_MEASURE", referencedColumnName = "UOM_CODE")
    @ManyToOne
    private LocalInvUnitOfMeasure invUnitOfMeasure;

    @OneToMany(mappedBy = "arInvoiceLineItem", fetch = FetchType.LAZY)
    private List<LocalInvTag> invTags;

    @OneToMany(mappedBy = "arInvoiceLineItem", fetch = FetchType.LAZY)
    private List<LocalInvCosting> invCostings;

    public Integer getIliCode() {

        return iliCode;
    }

    public void setIliCode(Integer ILI_CODE) {

        this.iliCode = ILI_CODE;
    }

    public short getIliLine() {

        return iliLine;
    }

    public void setIliLine(short ILI_LN) {

        this.iliLine = ILI_LN;
    }

    public double getIliQuantity() {

        return iliQuantity;
    }

    public void setIliQuantity(double ILI_QTY) {

        this.iliQuantity = ILI_QTY;
    }

    public String getIliImei() {

        return iliImei;
    }

    public void setIliImei(String ILI_IMEI) {

        this.iliImei = ILI_IMEI;
    }

    public double getIliUnitPrice() {

        return iliUnitPrice;
    }

    public void setIliUnitPrice(double ILI_UNT_PRC) {

        this.iliUnitPrice = ILI_UNT_PRC;
    }

    public double getIliAmount() {

        return iliAmount;
    }

    public void setIliAmount(double ILI_AMNT) {

        this.iliAmount = ILI_AMNT;
    }

    public double getIliTaxAmount() {

        return iliTaxAmount;
    }

    public void setIliTaxAmount(double ILI_TX_AMNT) {

        this.iliTaxAmount = ILI_TX_AMNT;
    }

    public double getIliDiscount1() {

        return iliDiscount1;
    }

    public void setIliDiscount1(double ILI_DSCNT_1) {

        this.iliDiscount1 = ILI_DSCNT_1;
    }

    public double getIliDiscount2() {

        return iliDiscount2;
    }

    public void setIliDiscount2(double ILI_DSCNT_2) {

        this.iliDiscount2 = ILI_DSCNT_2;
    }

    public double getIliDiscount3() {

        return iliDiscount3;
    }

    public void setIliDiscount3(double ILI_DSCNT_3) {

        this.iliDiscount3 = ILI_DSCNT_3;
    }

    public double getIliDiscount4() {

        return iliDiscount4;
    }

    public void setIliDiscount4(double ILI_DSCNT_4) {

        this.iliDiscount4 = ILI_DSCNT_4;
    }

    public double getIliTotalDiscount() {

        return iliTotalDiscount;
    }

    public void setIliTotalDiscount(double ILI_TTL_DSCNT) {

        this.iliTotalDiscount = ILI_TTL_DSCNT;
    }

    public String getIliMisc() {

        return iliMisc;
    }

    public void setIliMisc(String ILI_EXPRY_DT) {

        this.iliMisc = ILI_EXPRY_DT;
    }

    public byte getIliTax() {

        return iliTax;
    }

    public void setIliTax(byte ILI_TX) {

        this.iliTax = ILI_TX;
    }

    public Integer getIliAdCompany() {

        return iliAdCompany;
    }

    public void setIliAdCompany(Integer ILI_AD_CMPNY) {

        this.iliAdCompany = ILI_AD_CMPNY;
    }

    public LocalArInvoice getArInvoice() {

        return arInvoice;
    }

    public void setArInvoice(LocalArInvoice arInvoice) {

        this.arInvoice = arInvoice;
    }

    public LocalArPdc getArPdc() {

        return arPdc;
    }

    public void setArPdc(LocalArPdc arPdc) {

        this.arPdc = arPdc;
    }

    public LocalArReceipt getArReceipt() {

        return arReceipt;
    }

    public void setArReceipt(LocalArReceipt arReceipt) {

        this.arReceipt = arReceipt;
    }

    public LocalInvItemLocation getInvItemLocation() {

        return invItemLocation;
    }

    public void setInvItemLocation(LocalInvItemLocation invItemLocation) {

        this.invItemLocation = invItemLocation;
    }

    public LocalInvUnitOfMeasure getInvUnitOfMeasure() {

        return invUnitOfMeasure;
    }

    public void setInvUnitOfMeasure(LocalInvUnitOfMeasure invUnitOfMeasure) {

        this.invUnitOfMeasure = invUnitOfMeasure;
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

    public void addInvCosting(LocalInvCosting entity) {

        try {
            entity.setArInvoiceLineItem(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvCosting(LocalInvCosting entity) {

        try {
            entity.setArInvoiceLineItem(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}