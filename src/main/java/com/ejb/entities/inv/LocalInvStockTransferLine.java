package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "InvStockTransferLine")
@Table(name = "INV_STCK_TRNSFR_LN")
public class LocalInvStockTransferLine extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STL_CODE", nullable = false)
    private Integer stlCode;

    @Column(name = "STL_LCTN_FRM", columnDefinition = "INT")
    private Integer stlLocationFrom;

    @Column(name = "STL_LCTN_TO", columnDefinition = "INT")
    private Integer stlLocationTo;

    @Column(name = "STL_UNT_CST", columnDefinition = "DOUBLE")
    private double stlUnitCost = 0;

    @Column(name = "STL_QTY_DLVRD", columnDefinition = "DOUBLE")
    private double stlQuantityDelivered = 0;

    @Column(name = "STL_AMNT", columnDefinition = "DOUBLE")
    private double stlAmount = 0;

    @Column(name = "STL_EXPRY_DT", columnDefinition = "VARCHAR") //TODO: Review this field
    private String stlMisc;

    @Column(name = "STL_AD_CMPNY", columnDefinition = "INT")
    private Integer stlAdCompany;

    @JoinColumn(name = "INV_ITEM", referencedColumnName = "II_CODE")
    @ManyToOne
    private LocalInvItem invItem;

    @JoinColumn(name = "INV_STOCK_TRANSFER", referencedColumnName = "INV_ST_CODE")
    @ManyToOne
    private LocalInvStockTransfer invStockTransfer;

    @JoinColumn(name = "INV_UNIT_OF_MEASURE", referencedColumnName = "UOM_CODE")
    @ManyToOne
    private LocalInvUnitOfMeasure invUnitOfMeasure;

    @OneToMany(mappedBy = "invStockTransferLine", fetch = FetchType.LAZY)
    private List<LocalInvCosting> invCostings;

    @OneToMany(mappedBy = "invStockTransferLine", fetch = FetchType.LAZY)
    private List<LocalInvTag> invTags;

    public Integer getStlCode() {

        return stlCode;
    }

    public void setStlCode(Integer STL_CODE) {

        this.stlCode = STL_CODE;
    }

    public Integer getStlLocationFrom() {

        return stlLocationFrom;
    }

    public void setStlLocationFrom(Integer STL_LCTN_FRM) {

        this.stlLocationFrom = STL_LCTN_FRM;
    }

    public Integer getStlLocationTo() {

        return stlLocationTo;
    }

    public void setStlLocationTo(Integer STL_LCTN_TO) {

        this.stlLocationTo = STL_LCTN_TO;
    }

    public double getStlUnitCost() {

        return stlUnitCost;
    }

    public void setStlUnitCost(double STL_UNT_CST) {

        this.stlUnitCost = STL_UNT_CST;
    }

    public double getStlQuantityDelivered() {

        return stlQuantityDelivered;
    }

    public void setStlQuantityDelivered(double STL_QTY_DLVRD) {

        this.stlQuantityDelivered = STL_QTY_DLVRD;
    }

    public double getStlAmount() {

        return stlAmount;
    }

    public void setStlAmount(double STL_AMNT) {

        this.stlAmount = STL_AMNT;
    }

    public String getStlMisc() {

        return stlMisc;
    }

    public void setStlMisc(String STL_EXPRY_DT) {

        this.stlMisc = STL_EXPRY_DT;
    }

    public Integer getStlAdCompany() {

        return stlAdCompany;
    }

    public void setStlAdCompany(Integer STL_AD_CMPNY) {

        this.stlAdCompany = STL_AD_CMPNY;
    }

    public LocalInvItem getInvItem() {

        return invItem;
    }

    public void setInvItem(LocalInvItem invItem) {

        this.invItem = invItem;
    }

    public LocalInvStockTransfer getInvStockTransfer() {

        return invStockTransfer;
    }

    public void setInvStockTransfer(LocalInvStockTransfer invStockTransfer) {

        this.invStockTransfer = invStockTransfer;
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
            entity.setInvStockTransferLine(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvCosting(LocalInvCosting entity) {

        try {
            entity.setInvStockTransferLine(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}