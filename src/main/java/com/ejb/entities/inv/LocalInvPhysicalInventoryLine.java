package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "InvPhysicalInventoryLine")
@Table(name = "INV_PHYSCL_INVNTRY_LN")
public class LocalInvPhysicalInventoryLine extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PIL_CODE", nullable = false)
    private Integer pilCode;

    @Column(name = "PIL_ENDNG_INVNTRY", columnDefinition = "DOUBLE")
    private double pilEndingInventory = 0;

    @Column(name = "PIL_WSTG", columnDefinition = "DOUBLE")
    private double pilWastage = 0;

    @Column(name = "PIL_VRNC", columnDefinition = "DOUBLE")
    private double pilVariance = 0;

    @Column(name = "PIL_EXPRY_DT", columnDefinition = "VARCHAR")
    private String pilMisc;

    @Column(name = "PIL_AD_CMPNY", columnDefinition = "INT")
    private Integer pilAdCompany;

    @JoinColumn(name = "INV_ITEM_LOCATION", referencedColumnName = "INV_IL_CODE")
    @ManyToOne
    private LocalInvItemLocation invItemLocation;

    @JoinColumn(name = "INV_PHYSICAL_INVENTORY", referencedColumnName = "PI_CODE")
    @ManyToOne
    private LocalInvPhysicalInventory invPhysicalInventory;

    @JoinColumn(name = "INV_UNIT_OF_MEASURE", referencedColumnName = "UOM_CODE")
    @ManyToOne
    private LocalInvUnitOfMeasure invUnitOfMeasure;

    @OneToMany(mappedBy = "invPhysicalInventoryLine", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvTag> invTags;

    public Integer getPilCode() {

        return pilCode;
    }

    public void setPilCode(Integer PIL_CODE) {

        this.pilCode = PIL_CODE;
    }

    public double getPilEndingInventory() {

        return pilEndingInventory;
    }

    public void setPilEndingInventory(double PIL_ENDNG_INVNTRY) {

        this.pilEndingInventory = PIL_ENDNG_INVNTRY;
    }

    public double getPilWastage() {

        return pilWastage;
    }

    public void setPilWastage(double PIL_WSTG) {

        this.pilWastage = PIL_WSTG;
    }

    public double getPilVariance() {

        return pilVariance;
    }

    public void setPilVariance(double PIL_VRNC) {

        this.pilVariance = PIL_VRNC;
    }

    public String getPilMisc() {

        return pilMisc;
    }

    public void setPilMisc(String PIL_EXPRY_DT) {

        this.pilMisc = PIL_EXPRY_DT;
    }

    public Integer getPilAdCompany() {

        return pilAdCompany;
    }

    public void setPilAdCompany(Integer PIL_AD_CMPNY) {

        this.pilAdCompany = PIL_AD_CMPNY;
    }

    public LocalInvItemLocation getInvItemLocation() {

        return invItemLocation;
    }

    public void setInvItemLocation(LocalInvItemLocation invItemLocation) {

        this.invItemLocation = invItemLocation;
    }

    public LocalInvPhysicalInventory getInvPhysicalInventory() {

        return invPhysicalInventory;
    }

    public void setInvPhysicalInventory(LocalInvPhysicalInventory invPhysicalInventory) {

        this.invPhysicalInventory = invPhysicalInventory;
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

}