package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "InvBillOfMaterial")
@Table(name = "INV_BLL_OF_MTRL")
public class LocalInvBillOfMaterial extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOM_CODE", nullable = false)
    private Integer bomCode;
    @Column(name = "BOM_II_NM", columnDefinition = "VARCHAR")
    private String bomIiName;
    @Column(name = "BOM_LOC_NM", columnDefinition = "VARCHAR")
    private String bomLocName;
    @Column(name = "BOM_QNTTY_NDD", columnDefinition = "DOUBLE")
    private double bomQuantityNeeded;
    @Column(name = "BOM_DWNLD_STATUS", columnDefinition = "VARCHAR")
    private String bomDownloadStatus;
    @Column(name = "BOM_AD_CMPNY", columnDefinition = "INT")
    private Integer bomAdCompany;
    @JoinColumn(name = "INV_ITEM", referencedColumnName = "II_CODE")
    @ManyToOne
    private LocalInvItem invItem;
    @JoinColumn(name = "INV_UNIT_OF_MEASURE", referencedColumnName = "UOM_CODE")
    @ManyToOne
    private LocalInvUnitOfMeasure invUnitOfMeasure;

    public Integer getBomCode() {

        return bomCode;
    }

    public void setBomCode(Integer bomCode) {

        this.bomCode = bomCode;
    }

    public String getBomIiName() {

        return bomIiName;
    }

    public void setBomIiName(String bomIiName) {

        this.bomIiName = bomIiName;
    }

    public String getBomLocName() {

        return bomLocName;
    }

    public void setBomLocName(String bomLocName) {

        this.bomLocName = bomLocName;
    }

    public double getBomQuantityNeeded() {

        return bomQuantityNeeded;
    }

    public void setBomQuantityNeeded(double bomQuantityNeeded) {

        this.bomQuantityNeeded = bomQuantityNeeded;
    }

    public String getBomDownloadStatus() {

        return bomDownloadStatus;
    }

    public void setBomDownloadStatus(String bomDownloadStatus) {

        this.bomDownloadStatus = bomDownloadStatus;
    }

    public Integer getBomAdCompany() {

        return bomAdCompany;
    }

    public void setBomAdCompany(Integer bomAdCompany) {

        this.bomAdCompany = bomAdCompany;
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