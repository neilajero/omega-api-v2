package com.ejb.entities.inv;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity(name = "InvPriceLevelDate")
@Table(name = "INV_PRC_LVL_DT")
public class LocalInvPriceLevelDate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PD_CODE", nullable = false)
    private Integer pdCode;

    @Column(name = "PD_DESC", columnDefinition = "VARCHAR")
    private String pdDesc;

    @Column(name = "PD_AMNT", columnDefinition = "DOUBLE")
    private double pdAmount;

    @Column(name = "PD_MRGN", columnDefinition = "DOUBLE")
    private double pdMargin;
    @Column(name = "PD_PRCNT_MRKUP", columnDefinition = "DOUBLE")
    private double pdPercentMarkup;
    @Column(name = "PD_SHPPNG_CST", columnDefinition = "DOUBLE")
    private double pdShippingCost;
    @Column(name = "PD_AD_LV_PRC_LVL", columnDefinition = "VARCHAR")
    private String pdAdLvPriceLevel;
    @Column(name = "PD_DT_FRM", columnDefinition = "DATETIME")
    private Date pdDateFrom;
    @Column(name = "PD_DT_TO", columnDefinition = "DATETIME")
    private Date pdDateTo;
    @Column(name = "PD_STATUS", columnDefinition = "VARCHAR")
    private String pdStatus;
    @Column(name = "PD_AD_CMPNY", columnDefinition = "INT")
    private Integer pdAdCompany;
    @JoinColumn(name = "INV_ITEM", referencedColumnName = "II_CODE")
    @ManyToOne
    private LocalInvItem invItem;

    public Integer getPdCode() {

        return pdCode;
    }

    public void setPdCode(Integer pdCode) {

        this.pdCode = pdCode;
    }

    public String getPdDesc() {

        return pdDesc;
    }

    public void setPdDesc(String pdDesc) {

        this.pdDesc = pdDesc;
    }

    public double getPdAmount() {

        return pdAmount;
    }

    public void setPdAmount(double pdAmount) {

        this.pdAmount = pdAmount;
    }

    public double getPdMargin() {

        return pdMargin;
    }

    public void setPdMargin(double pdMargin) {

        this.pdMargin = pdMargin;
    }

    public double getPdPercentMarkup() {

        return pdPercentMarkup;
    }

    public void setPdPercentMarkup(double pdPercentMarkup) {

        this.pdPercentMarkup = pdPercentMarkup;
    }

    public double getPdShippingCost() {

        return pdShippingCost;
    }

    public void setPdShippingCost(double pdShippingCost) {

        this.pdShippingCost = pdShippingCost;
    }

    public String getPdAdLvPriceLevel() {

        return pdAdLvPriceLevel;
    }

    public void setPdAdLvPriceLevel(String pdAdLvPriceLevel) {

        this.pdAdLvPriceLevel = pdAdLvPriceLevel;
    }

    public Date getPdDateFrom() {

        return pdDateFrom;
    }

    public void setPdDateFrom(Date pdDateFrom) {

        this.pdDateFrom = pdDateFrom;
    }

    public Date getPdDateTo() {

        return pdDateTo;
    }

    public void setPdDateTo(Date pdDateTo) {

        this.pdDateTo = pdDateTo;
    }

    public String getPdStatus() {

        return pdStatus;
    }

    public void setPdStatus(String pdStatus) {

        this.pdStatus = pdStatus;
    }

    public Integer getPdAdCompany() {

        return pdAdCompany;
    }

    public void setPdAdCompany(Integer pdAdCompany) {

        this.pdAdCompany = pdAdCompany;
    }

    public LocalInvItem getInvItem() {

        return invItem;
    }

    public void setInvItem(LocalInvItem invItem) {

        this.invItem = invItem;
    }

}