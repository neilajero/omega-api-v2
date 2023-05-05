package com.ejb.entities.ap;

import com.ejb.NativeQueryHome;
import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.entities.inv.LocalInvTag;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ApVoucherLineItem")
@Table(name = "AP_VCHR_LN_ITM")
public class LocalApVoucherLineItem extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VLI_CODE", nullable = false)
    private Integer vliCode;

    @Column(name = "VLI_LN", columnDefinition = "SMALLINT")
    private short vliLine;

    @Column(name = "VLI_QTY", columnDefinition = "DOUBLE")
    private double vliQuantity = 0;

    @Column(name = "VLI_UNT_CST", columnDefinition = "DOUBLE")
    private double vliUnitCost = 0;

    @Column(name = "VLI_AMNT", columnDefinition = "DOUBLE")
    private double vliAmount = 0;

    @Column(name = "VLI_TX_AMNT", columnDefinition = "DOUBLE")
    private double vliTaxAmount = 0;

    @Column(name = "VLI_DSCNT_1", columnDefinition = "DOUBLE")
    private double vliDiscount1 = 0;

    @Column(name = "VLI_DSCNT_2", columnDefinition = "DOUBLE")
    private double vliDiscount2 = 0;

    @Column(name = "VLI_DSCNT_3", columnDefinition = "DOUBLE")
    private double vliDiscount3 = 0;

    @Column(name = "VLI_DSCNT_4", columnDefinition = "DOUBLE")
    private double vliDiscount4 = 0;

    @Column(name = "VLI_TTL_DSCNT", columnDefinition = "DOUBLE")
    private double vliTotalDiscount = 0;

    @Column(name = "VLI_MISC", columnDefinition = "VARCHAR")
    private String vliMisc;

    @Column(name = "VLI_SPL_NM", columnDefinition = "VARCHAR")
    private String vliSplName;

    @Column(name = "VLI_SPL_TIN", columnDefinition = "VARCHAR")
    private String vliSplTin;

    @Column(name = "VLI_SPL_ADDRSS", columnDefinition = "VARCHAR")
    private String vliSplAddress;

    @Column(name = "VLI_TX", columnDefinition = "TINYINT")
    private byte vliTax;

    @Column(name = "VLI_AD_CMPNY", columnDefinition = "INT")
    private Integer vliAdCompany;

    @JoinColumn(name = "AP_CHECK", referencedColumnName = "CHK_CODE")
    @ManyToOne
    private LocalApCheck apCheck;

    @JoinColumn(name = "AP_VOUCHER", referencedColumnName = "VOU_CODE")
    @ManyToOne
    private LocalApVoucher apVoucher;

    @JoinColumn(name = "INV_ITEM_LOCATION", referencedColumnName = "INV_IL_CODE")
    @ManyToOne
    private LocalInvItemLocation invItemLocation;

    @JoinColumn(name = "INV_UNIT_OF_MEASURE", referencedColumnName = "UOM_CODE")
    @ManyToOne
    private LocalInvUnitOfMeasure invUnitOfMeasure;

    @OneToMany(mappedBy = "apVoucherLineItem", fetch = FetchType.LAZY)
    private List<LocalInvCosting> invCostings;

    @OneToMany(mappedBy = "apVoucherLineItem", fetch = FetchType.LAZY)
    private List<LocalInvTag> invTags;

    public Integer getVliCode() {

        return vliCode;
    }

    public void setVliCode(Integer VLI_CODE) {

        this.vliCode = VLI_CODE;
    }

    public short getVliLine() {

        return vliLine;
    }

    public void setVliLine(short VLI_LN) {

        this.vliLine = VLI_LN;
    }

    public double getVliQuantity() {

        return vliQuantity;
    }

    public void setVliQuantity(double VLI_QTY) {

        this.vliQuantity = VLI_QTY;
    }

    public double getVliUnitCost() {

        return vliUnitCost;
    }

    public void setVliUnitCost(double VLI_UNT_CST) {

        this.vliUnitCost = VLI_UNT_CST;
    }

    public double getVliAmount() {

        return vliAmount;
    }

    public void setVliAmount(double VLI_AMNT) {

        this.vliAmount = VLI_AMNT;
    }

    public double getVliTaxAmount() {

        return vliTaxAmount;
    }

    public void setVliTaxAmount(double VLI_TX_AMNT) {

        this.vliTaxAmount = VLI_TX_AMNT;
    }

    public double getVliDiscount1() {

        return vliDiscount1;
    }

    public void setVliDiscount1(double VLI_DSCNT_1) {

        this.vliDiscount1 = VLI_DSCNT_1;
    }

    public double getVliDiscount2() {

        return vliDiscount2;
    }

    public void setVliDiscount2(double VLI_DSCNT_2) {

        this.vliDiscount2 = VLI_DSCNT_2;
    }

    public double getVliDiscount3() {

        return vliDiscount3;
    }

    public void setVliDiscount3(double VLI_DSCNT_3) {

        this.vliDiscount3 = VLI_DSCNT_3;
    }

    public double getVliDiscount4() {

        return vliDiscount4;
    }

    public void setVliDiscount4(double VLI_DSCNT_4) {

        this.vliDiscount4 = VLI_DSCNT_4;
    }

    public double getVliTotalDiscount() {

        return vliTotalDiscount;
    }

    public void setVliTotalDiscount(double VLI_TTL_DSCNT) {

        this.vliTotalDiscount = VLI_TTL_DSCNT;
    }

    public String getVliMisc() {

        return vliMisc;
    }

    public void setVliMisc(String VLI_MISC) {

        this.vliMisc = VLI_MISC;
    }

    public String getVliSplName() {

        return vliSplName;
    }

    public void setVliSplName(String VLI_SPL_NM) {

        this.vliSplName = VLI_SPL_NM;
    }

    public String getVliSplTin() {

        return vliSplTin;
    }

    public void setVliSplTin(String VLI_SPL_TIN) {

        this.vliSplTin = VLI_SPL_TIN;
    }

    public String getVliSplAddress() {

        return vliSplAddress;
    }

    public void setVliSplAddress(String VLI_SPL_ADDRSS) {

        this.vliSplAddress = VLI_SPL_ADDRSS;
    }

    public byte getVliTax() {

        return vliTax;
    }

    public void setVliTax(byte VLI_TX) {

        this.vliTax = VLI_TX;
    }

    public Integer getVliAdCompany() {

        return vliAdCompany;
    }

    public void setVliAdCompany(Integer VLI_AD_CMPNY) {

        this.vliAdCompany = VLI_AD_CMPNY;
    }

    public LocalApCheck getApCheck() {

        return apCheck;
    }

    public void setApCheck(LocalApCheck apCheck) {

        this.apCheck = apCheck;
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
    public List getInvCostings() {

        return invCostings;
    }

    public void setInvCostings(List invCostings) {

        this.invCostings = invCostings;
    }

    @XmlTransient
    public List getInvTags() {

        return invTags;
    }

    public void setInvTags(List invTags) {

        this.invTags = invTags;
    }

    public void addInvCosting(LocalInvCosting entity) {

        try {
            entity.setApVoucherLineItem(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvCosting(LocalInvCosting entity) {

        try {
            entity.setApVoucherLineItem(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}