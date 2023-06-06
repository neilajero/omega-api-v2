package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "InvBuildOrderLine")
@Table(name = "INV_BLD_ORDR_LN")
public class LocalInvBuildOrderLine extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOL_CODE", nullable = false)
    private Integer bolCode;

    @Column(name = "BOL_QTY_RQRD", columnDefinition = "DOUBLE")
    private double bolQuantityRequired;

    @Column(name = "BOL_QTY_ASSMBLD", columnDefinition = "DOUBLE")
    private double bolQuantityAssembled;

    @Column(name = "BOL_QTY_AVLBL", columnDefinition = "DOUBLE")
    private double bolQuantityAvailable;

    @Column(name = "BOL_LCK", columnDefinition = "DOUBLE")
    private double bolLock;

    @Column(name = "BOL_EXPRY_DT", columnDefinition = "VARCHAR")
    private String bolMisc;
    @Column(name = "BOL_AD_CMPNY", columnDefinition = "VARCHAR")
    private String bolAdCompany;
    @JoinColumn(name = "INV_BUILD_ORDER", referencedColumnName = "BOR_CODE")
    @ManyToOne
    private LocalInvBuildOrder invBuildOrder;
    @JoinColumn(name = "INV_ITEM_LOCATION", referencedColumnName = "INV_IL_CODE")
    @ManyToOne
    private LocalInvItemLocation invItemLocation;

    public LocalInvItemLocation getInvItemLocation() {

        return invItemLocation;
    }

    public void setInvItemLocation(LocalInvItemLocation invItemLocation) {

        this.invItemLocation = invItemLocation;
    }

    public Integer getBolCode() {

        return bolCode;
    }

    public void setBolCode(Integer bolCode) {

        this.bolCode = bolCode;
    }

    public double getBolQuantityRequired() {

        return bolQuantityRequired;
    }

    public void setBolQuantityRequired(double bolQuantityRequired) {

        this.bolQuantityRequired = bolQuantityRequired;
    }

    public double getBolQuantityAssembled() {

        return bolQuantityAssembled;
    }

    public void setBolQuantityAssembled(double bolQuantityAssembled) {

        this.bolQuantityAssembled = bolQuantityAssembled;
    }

    public double getBolQuantityAvailable() {

        return bolQuantityAvailable;
    }

    public void setBolQuantityAvailable(double bolQuantityAvailable) {

        this.bolQuantityAvailable = bolQuantityAvailable;
    }

    public double getBolLock() {

        return bolLock;
    }

    public void setBolLock(double bolLock) {

        this.bolLock = bolLock;
    }

    public String getBolMisc() {

        return bolMisc;
    }

    public void setBolMisc(String bolMisc) {

        this.bolMisc = bolMisc;
    }

    public String getBolAdCompany() {

        return bolAdCompany;
    }

    public void setBolAdCompany(String bolAdCompany) {

        this.bolAdCompany = bolAdCompany;
    }

    public LocalInvBuildOrder getInvBuildOrder() {

        return invBuildOrder;
    }

    public void setInvBuildOrder(LocalInvBuildOrder invBuildOrder) {

        this.invBuildOrder = invBuildOrder;
    }

}