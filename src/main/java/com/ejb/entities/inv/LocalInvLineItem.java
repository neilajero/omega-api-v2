package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "InvLineItem")
@Table(name = "INV_LN_ITM")
public class LocalInvLineItem extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LI_CODE", nullable = false)
    private Integer liCode;

    @Column(name = "LI_LN", columnDefinition = "SMALLINT")
    private short liLine;

    @Column(name = "LI_AD_CMPNY", columnDefinition = "INT")
    private Integer liAdCompany;

    @JoinColumn(name = "INV_ITEM_LOCATION", referencedColumnName = "INV_IL_CODE")
    @ManyToOne
    private LocalInvItemLocation invItemLocation;

    @JoinColumn(name = "INV_LINE_ITEM_TEMPLATE", referencedColumnName = "LIT_CODE")
    @ManyToOne
    private LocalInvLineItemTemplate invLineItemTemplate;

    @JoinColumn(name = "INV_UNIT_OF_MEASURE", referencedColumnName = "UOM_CODE")
    @ManyToOne
    private LocalInvUnitOfMeasure invUnitOfMeasure;

    public Integer getLiCode() {

        return liCode;
    }

    public void setLiCode(Integer LI_CODE) {

        this.liCode = LI_CODE;
    }

    public short getLiLine() {

        return liLine;
    }

    public void setLiLine(short LI_LN) {

        this.liLine = LI_LN;
    }

    public Integer getLiAdCompany() {

        return liAdCompany;
    }

    public void setLiAdCompany(Integer LI_AD_CMPNY) {

        this.liAdCompany = LI_AD_CMPNY;
    }

    public LocalInvItemLocation getInvItemLocation() {

        return invItemLocation;
    }

    public void setInvItemLocation(LocalInvItemLocation invItemLocation) {

        this.invItemLocation = invItemLocation;
    }

    public LocalInvLineItemTemplate getInvLineItemTemplate() {

        return invLineItemTemplate;
    }

    public void setInvLineItemTemplate(LocalInvLineItemTemplate invLineItemTemplate) {

        this.invLineItemTemplate = invLineItemTemplate;
    }

    public LocalInvUnitOfMeasure getInvUnitOfMeasure() {

        return invUnitOfMeasure;
    }

    public void setInvUnitOfMeasure(LocalInvUnitOfMeasure invUnitOfMeasure) {

        this.invUnitOfMeasure = invUnitOfMeasure;
    }

}