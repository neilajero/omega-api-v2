package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "InvBranchStockTransferLine")
@Table(name = "INV_BRNCH_STCK_TRNSFR_LN")
public class LocalInvBranchStockTransferLine extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BSL_CODE", nullable = false)
    private Integer bslCode;

    @Column(name = "BSL_QTY", columnDefinition = "DOUBLE")
    private double bslQuantity = 0;

    @Column(name = "BSL_QTY_RCVD", columnDefinition = "DOUBLE")
    private double bslQuantityReceived = 0;

    @Column(name = "BSL_UNT_CST", columnDefinition = "DOUBLE")
    private double bslUnitCost = 0;

    @Column(name = "BSL_AMNT", columnDefinition = "DOUBLE")
    private double bslAmount = 0;

    @Column(name = "BSL_AD_CMPNY", columnDefinition = "INT")
    private Integer bslAdCompany;

    @Column(name = "BSL_EXPRY_DT", columnDefinition = "VARCHAR") //TODO: Review this field
    private String bslMisc;

    @JoinColumn(name = "INV_BRANCH_STOCK_TRANSFER", referencedColumnName = "BST_CODE")
    @ManyToOne
    private LocalInvBranchStockTransfer invBranchStockTransfer;

    @JoinColumn(name = "INV_ITEM_LOCATION", referencedColumnName = "INV_IL_CODE")
    @ManyToOne
    private LocalInvItemLocation invItemLocation;

    @JoinColumn(name = "INV_UNIT_OF_MEASURE", referencedColumnName = "UOM_CODE")
    @ManyToOne
    private LocalInvUnitOfMeasure invUnitOfMeasure;

    @OneToMany(mappedBy = "invBranchStockTransferLine", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvCosting> invCostings;

    @OneToMany(mappedBy = "invBranchStockTransferLine", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvTag> invTags;

    public Integer getBslCode() {

        return bslCode;
    }

    public void setBslCode(Integer BSL_CODE) {

        this.bslCode = BSL_CODE;
    }

    public double getBslQuantity() {

        return bslQuantity;
    }

    public void setBslQuantity(double BSL_QTY) {

        this.bslQuantity = BSL_QTY;
    }

    public double getBslQuantityReceived() {

        return bslQuantityReceived;
    }

    public void setBslQuantityReceived(double BSL_QTY_RCVD) {

        this.bslQuantityReceived = BSL_QTY_RCVD;
    }

    public double getBslUnitCost() {

        return bslUnitCost;
    }

    public void setBslUnitCost(double BSL_UNT_CST) {

        this.bslUnitCost = BSL_UNT_CST;
    }

    public double getBslAmount() {

        return bslAmount;
    }

    public void setBslAmount(double BSL_AMNT) {

        this.bslAmount = BSL_AMNT;
    }

    public Integer getBslAdCompany() {

        return bslAdCompany;
    }

    public void setBslAdCompany(Integer BSL_AD_CMPNY) {

        this.bslAdCompany = BSL_AD_CMPNY;
    }

    public String getBslMisc() {

        return bslMisc;
    }

    public void setBslMisc(String BSL_EXPRY_DT) {

        this.bslMisc = BSL_EXPRY_DT;
    }

    public LocalInvBranchStockTransfer getInvBranchStockTransfer() {

        return invBranchStockTransfer;
    }

    public void setInvBranchStockTransfer(LocalInvBranchStockTransfer invBranchStockTransfer) {

        this.invBranchStockTransfer = invBranchStockTransfer;
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
            entity.setInvBranchStockTransferLine(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvCosting(LocalInvCosting entity) {

        try {
            entity.setInvBranchStockTransferLine(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}