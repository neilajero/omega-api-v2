package com.ejb.entities.ap;

import com.ejb.NativeQueryHome;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "ApCanvass")
@Table(name = "AP_CNVSS")
public class LocalApCanvass extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CNV_CODE", nullable = false)
    private Integer cnvCode;

    @Column(name = "CNV_LN", columnDefinition = "SMALLINT")
    private short cnvLine;

    @Column(name = "CNV_RMKS", columnDefinition = "VARCHAR")
    private String cnvRemarks;

    @Column(name = "CNV_QTY", columnDefinition = "DOUBLE")
    private double cnvQuantity = 0;

    @Column(name = "CNV_UNT_CST", columnDefinition = "DOUBLE")
    private double cnvUnitCost = 0;

    @Column(name = "CNV_AMNT", columnDefinition = "DOUBLE")
    private double cnvAmount = 0;

    @Column(name = "CNV_PO", columnDefinition = "TINYINT")
    private byte cnvPo;

    @Column(name = "CNV_AD_CMPNY", columnDefinition = "INT")
    private Integer cnvAdCompany;

    @JoinColumn(name = "AP_PURCHASE_REQUISITION_LINE", referencedColumnName = "PRL_CODE")
    @ManyToOne
    private LocalApPurchaseRequisitionLine apPurchaseRequisitionLine;

    @JoinColumn(name = "AP_SUPPLIER", referencedColumnName = "SPL_CODE")
    @ManyToOne
    private LocalApSupplier apSupplier;

    public Integer getCnvCode() {

        return cnvCode;
    }

    public void setCnvCode(Integer CNV_CODE) {

        this.cnvCode = CNV_CODE;
    }

    public short getCnvLine() {

        return cnvLine;
    }

    public void setCnvLine(short CNV_LN) {

        this.cnvLine = CNV_LN;
    }

    public String getCnvRemarks() {

        return cnvRemarks;
    }

    public void setCnvRemarks(String CNV_RMKS) {

        this.cnvRemarks = CNV_RMKS;
    }

    public double getCnvQuantity() {

        return cnvQuantity;
    }

    public void setCnvQuantity(double CNV_QTY) {

        this.cnvQuantity = CNV_QTY;
    }

    public double getCnvUnitCost() {

        return cnvUnitCost;
    }

    public void setCnvUnitCost(double CNV_UNT_CST) {

        this.cnvUnitCost = CNV_UNT_CST;
    }

    public double getCnvAmount() {

        return cnvAmount;
    }

    public void setCnvAmount(double CNV_AMNT) {

        this.cnvAmount = CNV_AMNT;
    }

    public byte getCnvPo() {

        return cnvPo;
    }

    public void setCnvPo(byte CNV_PO) {

        this.cnvPo = CNV_PO;
    }

    public Integer getCnvAdCompany() {

        return cnvAdCompany;
    }

    public void setCnvAdCompany(Integer CNV_AD_CMPNY) {

        this.cnvAdCompany = CNV_AD_CMPNY;
    }

    public LocalApPurchaseRequisitionLine getApPurchaseRequisitionLine() {

        return apPurchaseRequisitionLine;
    }

    public void setApPurchaseRequisitionLine(
            LocalApPurchaseRequisitionLine apPurchaseRequisitionLine) {

        this.apPurchaseRequisitionLine = apPurchaseRequisitionLine;
    }

    public LocalApSupplier getApSupplier() {

        return apSupplier;
    }

    public void setApSupplier(LocalApSupplier apSupplier) {

        this.apSupplier = apSupplier;
    }

}