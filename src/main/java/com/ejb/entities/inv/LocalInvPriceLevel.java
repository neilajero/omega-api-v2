package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "InvPriceLevel")
@Table(name = "INV_PRC_LVL")
public class LocalInvPriceLevel extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INV_PL_CODE", nullable = false)
    private Integer plCode;

    @Column(name = "PL_AMNT", columnDefinition = "DOUBLE")
    private double plAmount = 0;

    @Column(name = "PL_MRGN", columnDefinition = "DOUBLE")
    private double plMargin = 0;

    @Column(name = "PL_PRCNT_MRKUP", columnDefinition = "DOUBLE")
    private double plPercentMarkup = 0;

    @Column(name = "PL_SHPPNG_CST", columnDefinition = "DOUBLE")
    private double plShippingCost = 0;

    @Column(name = "PL_AD_LV_PRC_LVL", columnDefinition = "VARCHAR")
    private String plAdLvPriceLevel;

    @Column(name = "PL_DWNLD_STATUS", columnDefinition = "VARCHAR")
    private char plDownloadStatus;

    @Column(name = "PL_AD_CMPNY", columnDefinition = "INT")
    private Integer plAdCompany;

    @JoinColumn(name = "INV_ITEM", referencedColumnName = "II_CODE")
    @ManyToOne
    private LocalInvItem invItem;

    public Integer getPlCode() {

        return plCode;
    }

    public void setPlCode(Integer INV_PL_CODE) {

        this.plCode = INV_PL_CODE;
    }

    public double getPlAmount() {

        return plAmount;
    }

    public void setPlAmount(double PL_AMNT) {

        this.plAmount = PL_AMNT;
    }

    public double getPlMargin() {

        return plMargin;
    }

    public void setPlMargin(double PL_MRGN) {

        this.plMargin = PL_MRGN;
    }

    public double getPlPercentMarkup() {

        return plPercentMarkup;
    }

    public void setPlPercentMarkup(double PL_PRCNT_MRKUP) {

        this.plPercentMarkup = PL_PRCNT_MRKUP;
    }

    public double getPlShippingCost() {

        return plShippingCost;
    }

    public void setPlShippingCost(double PL_SHPPNG_CST) {

        this.plShippingCost = PL_SHPPNG_CST;
    }

    public String getPlAdLvPriceLevel() {

        return plAdLvPriceLevel;
    }

    public void setPlAdLvPriceLevel(String PL_AD_LV_PRC_LVL) {

        this.plAdLvPriceLevel = PL_AD_LV_PRC_LVL;
    }

    public char getPlDownloadStatus() {

        return plDownloadStatus;
    }

    public void setPlDownloadStatus(char PL_DWNLD_STATUS) {

        this.plDownloadStatus = PL_DWNLD_STATUS;
    }

    public Integer getPlAdCompany() {

        return plAdCompany;
    }

    public void setPlAdCompany(Integer PL_AD_CMPNY) {

        this.plAdCompany = PL_AD_CMPNY;
    }

    public LocalInvItem getInvItem() {

        return invItem;
    }

    public void setInvItem(LocalInvItem invItem) {

        this.invItem = invItem;
    }

}