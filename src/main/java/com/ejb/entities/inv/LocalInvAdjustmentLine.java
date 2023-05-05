package com.ejb.entities.inv;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.*;

@Entity(name = "InvAdjustmentLine")
@Table(name = "INV_ADJSTMNT_LN")
public class LocalInvAdjustmentLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AL_CODE", nullable = false)
    private Integer alCode;

    @Column(name = "AL_UNT_CST", columnDefinition = "DOUBLE")
    private double alUnitCost = 0;

    @Column(name = "AL_QC_NUM", columnDefinition = "VARCHAR")
    private String alQcNumber;

    @Column(name = "AL_QC_EXPRY_DT", columnDefinition = "DATETIME")
    private Date alQcExpiryDate;

    @Column(name = "AL_ADJST_QTY", columnDefinition = "DOUBLE")
    private double alAdjustQuantity = 0;

    @Column(name = "AL_SRVD", columnDefinition = "DOUBLE")
    private double alServed = 0;

    @Column(name = "AL_VD", columnDefinition = "TINYINT")
    private byte alVoid;

    @Column(name = "AL_ADJST_EXPRY_DT", columnDefinition = "VARCHAR") //TODO: Review this field
    private String alMisc;

    @Column(name = "AL_AD_CMPNY", columnDefinition = "INT")
    private Integer alAdCompany;

    @JoinColumn(name = "INV_ADJUSTMENT", referencedColumnName = "INV_ADJ_CODE")
    @ManyToOne
    private LocalInvAdjustment invAdjustment;

    @JoinColumn(name = "INV_ITEM_LOCATION", referencedColumnName = "INV_IL_CODE")
    @ManyToOne
    private LocalInvItemLocation invItemLocation;

    @JoinColumn(name = "INV_UNIT_OF_MEASURE", referencedColumnName = "UOM_CODE")
    @ManyToOne
    private LocalInvUnitOfMeasure invUnitOfMeasure;

    @OneToMany(mappedBy = "invAdjustmentLine", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvTag> invTags;

    @OneToMany(mappedBy = "invAdjustmentLine", fetch = FetchType.LAZY)
    private List<LocalInvCosting> invCostings;

    public Integer getAlCode() {

        return alCode;
    }

    public void setAlCode(Integer AL_CODE) {

        this.alCode = AL_CODE;
    }

    public double getAlUnitCost() {

        return alUnitCost;
    }

    public void setAlUnitCost(double AL_UNT_CST) {

        this.alUnitCost = AL_UNT_CST;
    }

    public String getAlQcNumber() {

        return alQcNumber;
    }

    public void setAlQcNumber(String AL_QC_NUM) {

        this.alQcNumber = AL_QC_NUM;
    }

    public Date getAlQcExpiryDate() {

        return alQcExpiryDate;
    }

    public void setAlQcExpiryDate(Date AL_QC_EXPRY_DT) {

        this.alQcExpiryDate = AL_QC_EXPRY_DT;
    }

    public double getAlAdjustQuantity() {

        return alAdjustQuantity;
    }

    public void setAlAdjustQuantity(double AL_ADJST_QTY) {

        this.alAdjustQuantity = AL_ADJST_QTY;
    }

    public double getAlServed() {

        return alServed;
    }

    public void setAlServed(double AL_SRVD) {

        this.alServed = AL_SRVD;
    }

    public byte getAlVoid() {

        return alVoid;
    }

    public void setAlVoid(byte AL_VD) {

        this.alVoid = AL_VD;
    }

    public String getAlMisc() {

        return alMisc;
    }

    public void setAlMisc(String AL_ADJST_EXPRY_DT) {

        this.alMisc = AL_ADJST_EXPRY_DT;
    }

    public Integer getAlAdCompany() {

        return alAdCompany;
    }

    public void setAlAdCompany(Integer AL_AD_CMPNY) {

        this.alAdCompany = AL_AD_CMPNY;
    }

    public LocalInvAdjustment getInvAdjustment() {

        return invAdjustment;
    }

    public void setInvAdjustment(LocalInvAdjustment invAdjustment) {

        this.invAdjustment = invAdjustment;
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