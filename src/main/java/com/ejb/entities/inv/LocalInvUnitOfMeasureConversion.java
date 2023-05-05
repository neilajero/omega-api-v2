package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "InvUnitOfMeasureConversion")
@Table(name = "INV_UNT_OF_MSR_CNVRSN")
public class LocalInvUnitOfMeasureConversion extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UMC_CODE", nullable = false)
    private Integer umcCode;

    @Column(name = "UMC_CNVRSN_FCTR", nullable = false, columnDefinition = "DOUBLE")
    private double umcConversionFactor;

    @Column(name = "UMC_BS_UNT", nullable = false, columnDefinition = "TINYINT")
    private byte umcBaseUnit;

    @Column(name = "UMC_DWNLD_STATUS", nullable = false, columnDefinition = "VARCHAR")
    private char umcDownloadStatus;

    @Column(name = "UMC_AD_CMPNY", columnDefinition = "INT")
    private Integer umcAdCompany;

    @JoinColumn(name = "INV_ITEM", referencedColumnName = "II_CODE")
    @ManyToOne
    private LocalInvItem invItem;

    @JoinColumn(name = "INV_UNIT_OF_MEASURE", referencedColumnName = "UOM_CODE")
    @ManyToOne
    private LocalInvUnitOfMeasure invUnitOfMeasure;

    public Integer getUmcCode() {

        return umcCode;
    }

    public void setUmcCode(Integer UMC_CODE) {

        this.umcCode = UMC_CODE;
    }

    public double getUmcConversionFactor() {

        return umcConversionFactor;
    }

    public void setUmcConversionFactor(double UMC_CNVRSN_FCTR) {

        this.umcConversionFactor = UMC_CNVRSN_FCTR;
    }

    public byte getUmcBaseUnit() {

        return umcBaseUnit;
    }

    public void setUmcBaseUnit(byte UMC_BS_UNT) {

        this.umcBaseUnit = UMC_BS_UNT;
    }

    public char getUmcDownloadStatus() {

        return umcDownloadStatus;
    }

    public void setUmcDownloadStatus(char UMC_DWNLD_STATUS) {

        this.umcDownloadStatus = UMC_DWNLD_STATUS;
    }

    public Integer getUmcAdCompany() {

        return umcAdCompany;
    }

    public void setUmcAdCompany(Integer UMC_AD_CMPNY) {

        this.umcAdCompany = UMC_AD_CMPNY;
    }

    public LocalInvItem getInvItem() {

        return invItem;
    }

    public void setInvItem(LocalInvItem invItem) {

        this.invItem = invItem;
    }

    public LocalInvUnitOfMeasure getInvUnitOfMeasure() {

        return invUnitOfMeasure;
    }

    public void setInvUnitOfMeasure(LocalInvUnitOfMeasure invUnitOfMeasure) {

        this.invUnitOfMeasure = invUnitOfMeasure;
    }

}