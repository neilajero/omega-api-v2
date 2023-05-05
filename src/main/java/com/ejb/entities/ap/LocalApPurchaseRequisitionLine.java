package com.ejb.entities.ap;

import com.ejb.NativeQueryHome;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.entities.inv.LocalInvTag;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ApPurchaseRequisitionLine")
@Table(name = "AP_PRCHS_RQSTN_LN")
public class LocalApPurchaseRequisitionLine extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRL_CODE", nullable = false)
    private Integer prlCode;

    @Column(name = "PRL_LN", columnDefinition = "SMALLINT")
    private short prlLine;

    @Column(name = "PRL_QTY", columnDefinition = "DOUBLE")
    private double prlQuantity = 0;

    @Column(name = "PRL_AMNT", columnDefinition = "DOUBLE")
    private double prlAmount = 0;

    @Column(name = "PRL_RMRKS", columnDefinition = "VARCHAR")
    private String prlRemarks;

    @Column(name = "PRL_AD_CMPNY", columnDefinition = "INT")
    private Integer prlAdCompany;

    @JoinColumn(name = "AP_PURCHASE_REQUISITION", referencedColumnName = "PR_CODE")
    @ManyToOne
    private LocalApPurchaseRequisition apPurchaseRequisition;

    @JoinColumn(name = "INV_ITEM_LOCATION", referencedColumnName = "INV_IL_CODE")
    @ManyToOne
    private LocalInvItemLocation invItemLocation;

    @JoinColumn(name = "INV_UNIT_OF_MEASURE", referencedColumnName = "UOM_CODE")
    @ManyToOne
    private LocalInvUnitOfMeasure invUnitOfMeasure;

    @OneToMany(mappedBy = "apPurchaseRequisitionLine", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalApCanvass> apCanvasses;

    @OneToMany(mappedBy = "apPurchaseRequisitionLine", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvTag> invTags;

    public Integer getPrlCode() {

        return prlCode;
    }

    public void setPrlCode(Integer PRL_CODE) {

        this.prlCode = PRL_CODE;
    }

    public short getPrlLine() {

        return prlLine;
    }

    public void setPrlLine(short PRL_LN) {

        this.prlLine = PRL_LN;
    }

    public double getPrlQuantity() {

        return prlQuantity;
    }

    public void setPrlQuantity(double PRL_QTY) {

        this.prlQuantity = PRL_QTY;
    }

    public double getPrlAmount() {

        return prlAmount;
    }

    public void setPrlAmount(double PRL_AMNT) {

        this.prlAmount = PRL_AMNT;
    }


    public String getPrlRemarks() {

        return prlRemarks;
    }

    public void setPrlRemarks(String PRL_RMRKS) {

        this.prlRemarks = PRL_RMRKS;
    }


    public Integer getPrlAdCompany() {

        return prlAdCompany;
    }


    public void setPrlAdCompany(Integer PRL_AD_CMPNY) {

        this.prlAdCompany = PRL_AD_CMPNY;
    }

    public LocalApPurchaseRequisition getApPurchaseRequisition() {

        return apPurchaseRequisition;
    }

    public void setApPurchaseRequisition(LocalApPurchaseRequisition apPurchaseRequisition) {

        this.apPurchaseRequisition = apPurchaseRequisition;
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
    public List getApCanvasses() {

        return apCanvasses;
    }

    public void setApCanvasses(List apCanvasses) {

        this.apCanvasses = apCanvasses;
    }

    @XmlTransient
    public List getInvTags() {

        return invTags;
    }

    public void setInvTags(List invTags) {

        this.invTags = invTags;
    }

    public void addApCanvass(LocalApCanvass entity) {

        try {
            entity.setApPurchaseRequisitionLine(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApCanvass(LocalApCanvass entity) {

        try {
            entity.setApPurchaseRequisitionLine(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}