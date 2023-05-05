package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.entities.ap.LocalApPurchaseRequisitionLine;
import com.ejb.entities.ap.LocalApVoucherLineItem;
import com.ejb.entities.ar.LocalArInvoiceLineItem;
import com.ejb.entities.ar.LocalArJobOrderLine;
import com.ejb.entities.ar.LocalArSalesOrderLine;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;

@Entity(name = "InvUnitOfMeasure")
@Table(name = "INV_UNT_OF_MSR")
public class LocalInvUnitOfMeasure extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UOM_CODE", nullable = false)
    @JsonProperty("id")
    private Integer uomCode;

    @Column(name = "UOM_NM", columnDefinition = "VARCHAR")
    @JsonProperty("name")
    private String uomName;

    @Column(name = "UOM_DESC", columnDefinition = "VARCHAR")
    @JsonProperty("description")
    private String uomDescription;

    @Column(name = "UOM_SHRT_NM", columnDefinition = "VARCHAR")
    @JsonProperty("shortName")
    private String uomShortName;

    @Column(name = "UOM_AD_LV_CLSS", columnDefinition = "VARCHAR")
    @JsonProperty("class")
    private String uomAdLvClass;

    @Column(name = "UOM_CNVRSN_FCTR", columnDefinition = "DOUBLE")
    @JsonProperty("conversionFactor")
    private double uomConversionFactor = 0;

    @Column(name = "UOM_BS_UNT", columnDefinition = "TINYINT")
    @JsonProperty("baseUnit")
    private byte uomBaseUnit;

    @Column(name = "UOM_ENBL", columnDefinition = "TINYINT")
    @JsonProperty("enable")
    private byte uomEnable;

    @Column(name = "UOM_DWNLD_STATUS", columnDefinition = "VARCHAR")
    @JsonProperty("downloadStatus")
    private char uomDownloadStatus;

    @Column(name = "UOM_AD_CMPNY", columnDefinition = "INT")
    @JsonProperty("companyId")
    private Integer uomAdCompany;

    @OneToMany(mappedBy = "invUnitOfMeasure", fetch = FetchType.LAZY)
    private List<LocalInvItem> invItems;

    @OneToMany(mappedBy = "invUnitOfMeasure", fetch = FetchType.LAZY)
    private List<LocalInvAdjustmentLine> invAdjustmentLines;

    @OneToMany(mappedBy = "invUnitOfMeasure", fetch = FetchType.LAZY)
    private List<LocalApVoucherLineItem> apVoucherLineItems;

    @OneToMany(mappedBy = "invUnitOfMeasure", fetch = FetchType.LAZY)
    private List<LocalArInvoiceLineItem> arInvoiceLineItems;

    @OneToMany(mappedBy = "invUnitOfMeasure", fetch = FetchType.LAZY)
    private List<LocalApPurchaseOrderLine> apPurchaseOrderLines;

    @OneToMany(mappedBy = "invUnitOfMeasure", fetch = FetchType.LAZY)
    private List<LocalInvTransactionalBudget> invTransactionalBudget;

    @OneToMany(mappedBy = "invUnitOfMeasure", fetch = FetchType.LAZY)
    private List<LocalInvStockTransferLine> invStockTransferLines;

    @OneToMany(mappedBy = "invUnitOfMeasure", fetch = FetchType.LAZY)
    private List<LocalArSalesOrderLine> arSalesOrderLines;

    @OneToMany(mappedBy = "invUnitOfMeasure", fetch = FetchType.LAZY)
    private List<LocalApPurchaseRequisitionLine> apPurchaseRequisitionLines;

    @OneToMany(mappedBy = "invUnitOfMeasure", fetch = FetchType.LAZY)
    private List<LocalArJobOrderLine> arJobOrderLines;

    @OneToMany(mappedBy = "invUnitOfMeasure", fetch = FetchType.LAZY)
    private List<LocalInvBranchStockTransferLine> invBranchStockTransferLines;

    @OneToMany(mappedBy = "invUnitOfMeasure", fetch = FetchType.LAZY)
    private List<LocalInvUnitOfMeasureConversion> invUnitOfMeasureConversions;

    @OneToMany(mappedBy = "invUnitOfMeasure", fetch = FetchType.LAZY)
    private List<LocalInvLineItem> invLineItems = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "invUnitOfMeasure", fetch = FetchType.LAZY)
    private List<LocalInvPhysicalInventoryLine> invPhysicalInventoryLines;

    public Integer getUomCode() {

        return uomCode;
    }

    public void setUomCode(Integer UOM_CODE) {

        this.uomCode = UOM_CODE;
    }

    public String getUomName() {

        return uomName;
    }

    public void setUomName(String UOM_NM) {

        this.uomName = UOM_NM;
    }

    public String getUomDescription() {

        return uomDescription;
    }

    public void setUomDescription(String UOM_DESC) {

        this.uomDescription = UOM_DESC;
    }

    public String getUomShortName() {

        return uomShortName;
    }

    public void setUomShortName(String UOM_SHRT_NM) {

        this.uomShortName = UOM_SHRT_NM;
    }

    public String getUomAdLvClass() {

        return uomAdLvClass;
    }

    public void setUomAdLvClass(String UOM_AD_LV_CLSS) {

        this.uomAdLvClass = UOM_AD_LV_CLSS;
    }

    public double getUomConversionFactor() {

        return uomConversionFactor;
    }

    public void setUomConversionFactor(double UOM_CNVRSN_FCTR) {

        this.uomConversionFactor = UOM_CNVRSN_FCTR;
    }

    public byte getUomBaseUnit() {

        return uomBaseUnit;
    }

    public void setUomBaseUnit(byte UOM_BS_UNT) {

        this.uomBaseUnit = UOM_BS_UNT;
    }

    public byte getUomEnable() {

        return uomEnable;
    }

    public void setUomEnable(byte UOM_ENBL) {

        this.uomEnable = UOM_ENBL;
    }

    public char getUomDownloadStatus() {

        return uomDownloadStatus;
    }

    public void setUomDownloadStatus(char UOM_DWNLD_STATUS) {

        this.uomDownloadStatus = UOM_DWNLD_STATUS;
    }

    public Integer getUomAdCompany() {

        return uomAdCompany;
    }

    public void setUomAdCompany(Integer UOM_AD_CMPNY) {

        this.uomAdCompany = UOM_AD_CMPNY;
    }

    @XmlTransient
    public List getInvItems() {

        return invItems;
    }

    public void setInvItems(List invItems) {

        this.invItems = invItems;
    }

    @XmlTransient
    public List getInvAdjustmentLines() {

        return invAdjustmentLines;
    }

    public void setInvAdjustmentLines(List invAdjustmentLines) {

        this.invAdjustmentLines = invAdjustmentLines;
    }

    @XmlTransient
    public List getApVoucherLineItems() {

        return apVoucherLineItems;
    }

    public void setApVoucherLineItems(List apVoucherLineItems) {

        this.apVoucherLineItems = apVoucherLineItems;
    }

    @XmlTransient
    public List getArInvoiceLineItems() {

        return arInvoiceLineItems;
    }

    public void setArInvoiceLineItems(List arInvoiceLineItems) {

        this.arInvoiceLineItems = arInvoiceLineItems;
    }

    @XmlTransient
    public List getApPurchaseOrderLines() {

        return apPurchaseOrderLines;
    }

    public void setApPurchaseOrderLines(List apPurchaseOrderLines) {

        this.apPurchaseOrderLines = apPurchaseOrderLines;
    }

    @XmlTransient
    public List getInvTransactionalBudget() {

        return invTransactionalBudget;
    }

    public void setInvTransactionalBudget(List invTransactionalBudget) {

        this.invTransactionalBudget = invTransactionalBudget;
    }

    @XmlTransient
    public List getInvStockTransferLines() {

        return invStockTransferLines;
    }

    public void setInvStockTransferLines(List invStockTransferLines) {

        this.invStockTransferLines = invStockTransferLines;
    }

    @XmlTransient
    public List getArSalesOrderLines() {

        return arSalesOrderLines;
    }

    public void setArSalesOrderLines(List arSalesOrderLines) {

        this.arSalesOrderLines = arSalesOrderLines;
    }

    @XmlTransient
    public List getApPurchaseRequisitionLines() {

        return apPurchaseRequisitionLines;
    }

    public void setApPurchaseRequisitionLines(List apPurchaseRequisitionLines) {

        this.apPurchaseRequisitionLines = apPurchaseRequisitionLines;
    }

    @XmlTransient
    public List getArJobOrderLines() {

        return arJobOrderLines;
    }

    public void setArJobOrderLines(List arJobOrderLines) {

        this.arJobOrderLines = arJobOrderLines;
    }

    @XmlTransient
    public List getInvBranchStockTransferLines() {

        return invBranchStockTransferLines;
    }

    public void setInvBranchStockTransferLines(List invBranchStockTransferLines) {

        this.invBranchStockTransferLines = invBranchStockTransferLines;
    }

    @XmlTransient
    public List getInvUnitOfMeasureConversions() {

        return invUnitOfMeasureConversions;
    }

    public void setInvUnitOfMeasureConversions(List invUnitOfMeasureConversions) {

        this.invUnitOfMeasureConversions = invUnitOfMeasureConversions;
    }

    @XmlTransient
    public List getInvLineItems() {

        return invLineItems;
    }

    public void setInvLineItems(List invLineItems) {

        this.invLineItems = invLineItems;
    }

    @XmlTransient
    public List getInvPhysicalInventoryLines() {

        return invPhysicalInventoryLines;
    }

    public void setInvPhysicalInventoryLines(List invPhysicalInventoryLines) {

        this.invPhysicalInventoryLines = invPhysicalInventoryLines;
    }

    public void addInvItem(LocalInvItem entity) {

        try {
            entity.setInvUnitOfMeasure(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvItem(LocalInvItem entity) {

        try {
            entity.setInvUnitOfMeasure(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvAdjustmentLine(LocalInvAdjustmentLine entity) {

        try {
            entity.setInvUnitOfMeasure(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvAdjustmentLine(LocalInvAdjustmentLine entity) {

        try {
            entity.setInvUnitOfMeasure(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApVoucherLineItem(LocalApVoucherLineItem entity) {

        try {
            entity.setInvUnitOfMeasure(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApVoucherLineItem(LocalApVoucherLineItem entity) {

        try {
            entity.setInvUnitOfMeasure(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArInvoiceLineItem(LocalArInvoiceLineItem entity) {

        try {
            entity.setInvUnitOfMeasure(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArInvoiceLineItem(LocalArInvoiceLineItem entity) {

        try {
            entity.setInvUnitOfMeasure(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApPurchaseOrderLine(LocalApPurchaseOrderLine entity) {

        try {
            entity.setInvUnitOfMeasure(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApPurchaseOrderLine(LocalApPurchaseOrderLine entity) {

        try {
            entity.setInvUnitOfMeasure(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvStockTransferLine(LocalInvStockTransferLine entity) {

        try {
            entity.setInvUnitOfMeasure(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvStockTransferLine(LocalInvStockTransferLine entity) {

        try {
            entity.setInvUnitOfMeasure(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArSalesOrderLine(LocalArSalesOrderLine entity) {

        try {
            entity.setInvUnitOfMeasure(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArSalesOrderLine(LocalArSalesOrderLine entity) {

        try {
            entity.setInvUnitOfMeasure(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApPurchaseRequisitionLine(LocalApPurchaseRequisitionLine entity) {

        try {
            entity.setInvUnitOfMeasure(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApPurchaseRequisitionLine(LocalApPurchaseRequisitionLine entity) {

        try {
            entity.setInvUnitOfMeasure(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvBranchStockTransferLine(LocalInvBranchStockTransferLine entity) {

        try {
            entity.setInvUnitOfMeasure(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvPhysicalInventoryLine(LocalInvPhysicalInventoryLine entity) {

        try {
            entity.setInvUnitOfMeasure(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvPhysicalInventoryLine(LocalInvPhysicalInventoryLine entity) {

        try {
            entity.setInvUnitOfMeasure(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvBranchStockTransferLine(LocalInvBranchStockTransferLine entity) {

        try {
            entity.setInvUnitOfMeasure(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvUnitOfMeasureConversion(LocalInvUnitOfMeasureConversion entity) {

        try {
            entity.setInvUnitOfMeasure(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvUnitOfMeasureConversion(LocalInvUnitOfMeasureConversion entity) {

        try {
            entity.setInvUnitOfMeasure(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvLineItem(LocalInvLineItem entity) {

        try {
            entity.setInvUnitOfMeasure(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvLineItem(LocalInvLineItem entity) {

        try {
            entity.setInvUnitOfMeasure(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}