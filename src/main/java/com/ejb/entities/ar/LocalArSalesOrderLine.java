package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.entities.inv.LocalInvTag;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;

@Entity(name = "ArSalesOrderLine")
@Table(name = "AR_SLS_ORDR_LN")
public class LocalArSalesOrderLine extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SOL_CODE", nullable = false)
    private Integer solCode;

    @Column(name = "SOL_LN", columnDefinition = "SMALLINT")
    private short solLine;

    @Column(name = "SOL_IDESC", columnDefinition = "VARCHAR")
    private String solLineIDesc;

    @Column(name = "SOL_QTY", columnDefinition = "DOUBLE")
    private double solQuantity = 0;

    @Column(name = "SOL_UNT_PRC", columnDefinition = "DOUBLE")
    private double solUnitPrice = 0;

    @Column(name = "SOL_AMNT", columnDefinition = "DOUBLE")
    private double solAmount = 0;
    @Column(name = "SOL_GRSS_AMNT", columnDefinition = "DOUBLE")
    private double solGrossAmount = 0;
    @Column(name = "SOL_TX_AMNT", columnDefinition = "DOUBLE")
    private double solTaxAmount = 0;
    @Column(name = "SOL_DSCNT_1", columnDefinition = "DOUBLE")
    private double solDiscount1 = 0;
    @Column(name = "SOL_DSCNT_2", columnDefinition = "DOUBLE")
    private double solDiscount2 = 0;
    @Column(name = "SOL_DSCNT_3", columnDefinition = "DOUBLE")
    private double solDiscount3 = 0;
    @Column(name = "SOL_DSCNT_4", columnDefinition = "DOUBLE")
    private double solDiscount4 = 0;
    @Column(name = "SOL_TTL_DSCNT", columnDefinition = "DOUBLE")
    private double solTotalDiscount = 0;
    @Column(name = "SOL_RQST_QTY", columnDefinition = "DOUBLE")
    private double solRequestQuantity = 0;
    @Column(name = "SOL_MISC", columnDefinition = "VARCHAR")
    private String solMisc;
    @Column(name = "SOL_TX", columnDefinition = "TINYINT")
    private byte solTax;
    @Column(name = "SOL_AD_CMPNY", columnDefinition = "INT")
    private Integer solAdCompany;
    @JoinColumn(name = "AR_SALES_ORDER", referencedColumnName = "SO_CODE")
    @ManyToOne
    private LocalArSalesOrder arSalesOrder;
    @JoinColumn(name = "INV_ITEM_LOCATION", referencedColumnName = "INV_IL_CODE")
    @ManyToOne
    private LocalInvItemLocation invItemLocation;
    @JoinColumn(name = "INV_UNIT_OF_MEASURE", referencedColumnName = "UOM_CODE")
    @ManyToOne
    private LocalInvUnitOfMeasure invUnitOfMeasure;
    @OneToMany(mappedBy = "arSalesOrderLine", fetch = FetchType.LAZY)
    private List<LocalArSalesOrderInvoiceLine> arSalesOrderInvoiceLines;
    @OneToMany(mappedBy = "arSalesOrderLine", fetch = FetchType.LAZY)
    private List<LocalInvTag> invTags;

    public double getSolTaxAmount() {

        return solTaxAmount;
    }

    public void setSolTaxAmount(double solTaxAmount) {

        this.solTaxAmount = solTaxAmount;
    }

    public double getSolGrossAmount() {

        return solGrossAmount;
    }

    public void setSolGrossAmount(double solGrossAmount) {

        this.solGrossAmount = solGrossAmount;
    }

    public Integer getSolCode() {

        return solCode;
    }

    public void setSolCode(Integer SOL_CODE) {

        this.solCode = SOL_CODE;
    }

    public short getSolLine() {

        return solLine;
    }

    public void setSolLine(short SOL_LN) {

        this.solLine = SOL_LN;
    }

    public String getSolLineIDesc() {

        return solLineIDesc;
    }

    public void setSolLineIDesc(String SOL_IDESC) {

        this.solLineIDesc = SOL_IDESC;
    }

    public double getSolQuantity() {

        return solQuantity;
    }

    public void setSolQuantity(double SOL_QTY) {

        this.solQuantity = SOL_QTY;
    }

    public double getSolUnitPrice() {

        return solUnitPrice;
    }

    public void setSolUnitPrice(double SOL_UNT_PRC) {

        this.solUnitPrice = SOL_UNT_PRC;
    }

    public double getSolAmount() {

        return solAmount;
    }

    public void setSolAmount(double SOL_AMNT) {

        this.solAmount = SOL_AMNT;
    }

    public double getSolDiscount1() {

        return solDiscount1;
    }

    public void setSolDiscount1(double SOL_DSCNT_1) {

        this.solDiscount1 = SOL_DSCNT_1;
    }

    public double getSolDiscount2() {

        return solDiscount2;
    }

    public void setSolDiscount2(double SOL_DSCNT_2) {

        this.solDiscount2 = SOL_DSCNT_2;
    }

    public double getSolDiscount3() {

        return solDiscount3;
    }

    public void setSolDiscount3(double SOL_DSCNT_3) {

        this.solDiscount3 = SOL_DSCNT_3;
    }

    public double getSolDiscount4() {

        return solDiscount4;
    }

    public void setSolDiscount4(double SOL_DSCNT_4) {

        this.solDiscount4 = SOL_DSCNT_4;
    }

    public double getSolTotalDiscount() {

        return solTotalDiscount;
    }

    public void setSolTotalDiscount(double SOL_TTL_DSCNT) {

        this.solTotalDiscount = SOL_TTL_DSCNT;
    }

    public double getSolRequestQuantity() {

        return solRequestQuantity;
    }

    public void setSolRequestQuantity(double SOL_RQST_QTY) {

        this.solRequestQuantity = SOL_RQST_QTY;
    }

    public String getSolMisc() {

        return solMisc;
    }

    public void setSolMisc(String SOL_MISC) {

        this.solMisc = SOL_MISC;
    }

    public byte getSolTax() {

        return solTax;
    }

    public void setSolTax(byte SOL_TX) {

        this.solTax = SOL_TX;
    }

    public Integer getSolAdCompany() {

        return solAdCompany;
    }

    public void setSolAdCompany(Integer SOL_AD_CMPNY) {

        this.solAdCompany = SOL_AD_CMPNY;
    }

    public LocalArSalesOrder getArSalesOrder() {

        return arSalesOrder;
    }

    public void setArSalesOrder(LocalArSalesOrder arSalesOrder) {

        this.arSalesOrder = arSalesOrder;
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
    public List getArSalesOrderInvoiceLines() {

        return arSalesOrderInvoiceLines;
    }

    public void setArSalesOrderInvoiceLines(List arSalesOrderInvoiceLines) {

        this.arSalesOrderInvoiceLines = arSalesOrderInvoiceLines;
    }

    @XmlTransient
    public List getInvTags() {

        return invTags;
    }

    public void setInvTags(List invTags) {

        this.invTags = invTags;
    }

    public void addArSalesOrderInvoiceLine(LocalArSalesOrderInvoiceLine entity) {

        try {
            entity.setArSalesOrderLine(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArSalesOrderInvoiceLine(LocalArSalesOrderInvoiceLine entity) {

        try {
            entity.setArSalesOrderLine(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}