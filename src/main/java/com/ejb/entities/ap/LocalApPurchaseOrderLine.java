package com.ejb.entities.ap;

import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.entities.inv.LocalInvTag;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "ApPurchaseOrderLine")
@Table(name = "AP_PRCHS_ORDR_LN")
public class LocalApPurchaseOrderLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AP_PL_CODE", nullable = false)
    private Integer plCode;

    @Column(name = "PL_LN", columnDefinition = "SMALLINT")
    private short plLine;

    @Column(name = "PL_QTY", columnDefinition = "DOUBLE")
    private double plQuantity = 0;

    @Column(name = "PL_UNT_CST", columnDefinition = "DOUBLE")
    private double plUnitCost = 0;

    @Column(name = "PL_AMNT", columnDefinition = "DOUBLE")
    private double plAmount = 0;

    @Column(name = "PL_UNT_CST_LCL", columnDefinition = "DOUBLE")
    private double plUnitCostLocal = 0;

    @Column(name = "PL_AMNT_LCL", columnDefinition = "DOUBLE")
    private double plAmountLocal = 0;

    @Column(name = "PL_ADDON_CST", columnDefinition = "DOUBLE")
    private double plAddonCost = 0;

    @Column(name = "PL_QC_NUM", columnDefinition = "VARCHAR")
    private String plQcNumber;

    @Column(name = "PL_QC_EXPRY_DT", columnDefinition = "DATETIME")
    private Date plQcExpiryDate;

    @Column(name = "PL_CNVRSN_FCTR", columnDefinition = "DOUBLE")
    private double plConversionFactor = 0;

    @Column(name = "PL_TX_AMNT", columnDefinition = "DOUBLE")
    private double plTaxAmount = 0;

    @Column(name = "PL_PL_CODE", columnDefinition = "INT")
    private Integer plPlCode;

    @Column(name = "PL_DSCNT_1", columnDefinition = "DOUBLE")
    private double plDiscount1 = 0;

    @Column(name = "PL_DSCNT_2", columnDefinition = "DOUBLE")
    private double plDiscount2 = 0;

    @Column(name = "PL_DSCNT_3", columnDefinition = "DOUBLE")
    private double plDiscount3 = 0;

    @Column(name = "PL_DSCNT_4", columnDefinition = "DOUBLE")
    private double plDiscount4 = 0;

    @Column(name = "PL_TTL_DSCNT", columnDefinition = "DOUBLE")
    private double plTotalDiscount = 0;

    @Column(name = "PL_RMRKS", columnDefinition = "VARCHAR")
    private String plRemarks;

    @Column(name = "PL_EXPRY_DT", columnDefinition = "VARCHAR")
    private String plMisc;

    @Column(name = "PL_AD_CMPNY", columnDefinition = "INT")
    private Integer plAdCompany;

    @JoinColumn(name = "AP_PURCHASE_ORDER", referencedColumnName = "PO_CODE")
    @ManyToOne
    private LocalApPurchaseOrder apPurchaseOrder;

    @JoinColumn(name = "AP_VOUCHER", referencedColumnName = "VOU_CODE")
    @ManyToOne
    private LocalApVoucher apVoucher;

    @JoinColumn(name = "INV_ITEM_LOCATION", referencedColumnName = "INV_IL_CODE")
    @ManyToOne
    private LocalInvItemLocation invItemLocation;

    @JoinColumn(name = "INV_UNIT_OF_MEASURE", referencedColumnName = "UOM_CODE")
    @ManyToOne
    private LocalInvUnitOfMeasure invUnitOfMeasure;

    @OneToMany(mappedBy = "apPurchaseOrderLine", fetch = FetchType.LAZY)
    private List<LocalInvTag> invTags;

    @OneToMany(mappedBy = "apPurchaseOrderLine", fetch = FetchType.LAZY)
    private List<LocalInvCosting> invCostings;

    public Integer getPlCode() {

        return plCode;
    }

    public void setPlCode(Integer AP_PL_CODE) {

        this.plCode = AP_PL_CODE;
    }

    public short getPlLine() {

        return plLine;
    }

    public void setPlLine(short PL_LN) {

        this.plLine = PL_LN;
    }

    public double getPlQuantity() {

        return plQuantity;
    }

    public void setPlQuantity(double PL_QTY) {

        this.plQuantity = PL_QTY;
    }

    public double getPlUnitCost() {

        return plUnitCost;
    }

    public void setPlUnitCost(double PL_UNT_CST) {

        this.plUnitCost = PL_UNT_CST;
    }

    public double getPlAmount() {

        return plAmount;
    }

    public void setPlAmount(double PL_AMNT) {

        this.plAmount = PL_AMNT;
    }

    public double getPlUnitCostLocal() {

        return plUnitCostLocal;
    }

    public void setPlUnitCostLocal(double PL_UNT_CST_LCL) {

        this.plUnitCostLocal = PL_UNT_CST_LCL;
    }

    public double getPlAmountLocal() {

        return plAmountLocal;
    }

    public void setPlAmountLocal(double PL_AMNT_LCL) {

        this.plAmountLocal = PL_AMNT_LCL;
    }

    public double getPlAddonCost() {

        return plAddonCost;
    }

    public void setPlAddonCost(double PL_ADDON_CST) {

        this.plAddonCost = PL_ADDON_CST;
    }

    public String getPlQcNumber() {

        return plQcNumber;
    }

    public void setPlQcNumber(String PL_QC_NUM) {

        this.plQcNumber = PL_QC_NUM;
    }

    public Date getPlQcExpiryDate() {

        return plQcExpiryDate;
    }

    public void setPlQcExpiryDate(Date PL_QC_EXPRY_DT) {

        this.plQcExpiryDate = PL_QC_EXPRY_DT;
    }

    public String getPlRemarks() {

        return plRemarks;
    }

    public void setPlRemarks(String PL_RMRKS) {

        this.plRemarks = PL_RMRKS;
    }

    public double getPlConversionFactor() {

        return plConversionFactor;
    }

    public void setPlConversionFactor(double PL_CNVRSN_FCTR) {

        this.plConversionFactor = PL_CNVRSN_FCTR;
    }

    public double getPlTaxAmount() {

        return plTaxAmount;
    }

    public void setPlTaxAmount(double PL_TX_AMNT) {

        this.plTaxAmount = PL_TX_AMNT;
    }

    public Integer getPlPlCode() {

        return plPlCode;
    }

    public void setPlPlCode(Integer PL_PL_CODE) {

        this.plPlCode = PL_PL_CODE;
    }

    public double getPlDiscount1() {

        return plDiscount1;
    }

    public void setPlDiscount1(double PL_DSCNT_1) {

        this.plDiscount1 = PL_DSCNT_1;
    }

    public double getPlDiscount2() {

        return plDiscount2;
    }

    public void setPlDiscount2(double PL_DSCNT_2) {

        this.plDiscount2 = PL_DSCNT_2;
    }

    public double getPlDiscount3() {

        return plDiscount3;
    }

    public void setPlDiscount3(double PL_DSCNT_3) {

        this.plDiscount3 = PL_DSCNT_3;
    }

    public double getPlDiscount4() {

        return plDiscount4;
    }

    public void setPlDiscount4(double PL_DSCNT_4) {

        this.plDiscount4 = PL_DSCNT_4;
    }

    public double getPlTotalDiscount() {

        return plTotalDiscount;
    }

    public void setPlTotalDiscount(double PL_TTL_DSCNT) {

        this.plTotalDiscount = PL_TTL_DSCNT;
    }

    public String getPlMisc() {

        return plMisc;
    }

    public void setPlMisc(String PL_EXPRY_DT) {

        this.plMisc = PL_EXPRY_DT;
    }

    public Integer getPlAdCompany() {

        return plAdCompany;
    }

    public void setPlAdCompany(Integer PL_AD_CMPNY) {

        this.plAdCompany = PL_AD_CMPNY;
    }

    public LocalApPurchaseOrder getApPurchaseOrder() {

        return apPurchaseOrder;
    }

    public void setApPurchaseOrder(LocalApPurchaseOrder apPurchaseOrder) {

        this.apPurchaseOrder = apPurchaseOrder;
    }

    public LocalApVoucher getApVoucher() {

        return apVoucher;
    }

    public void setApVoucher(LocalApVoucher apVoucher) {

        this.apVoucher = apVoucher;
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

}