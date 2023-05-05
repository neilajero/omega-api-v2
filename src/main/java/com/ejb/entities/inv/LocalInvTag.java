package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.entities.ap.LocalApPurchaseRequisitionLine;
import com.ejb.entities.ap.LocalApVoucherLineItem;
import com.ejb.entities.ar.*;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.*;

@Entity(name = "InvTag")
@Table(name = "INV_TAG")
public class LocalInvTag extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TG_CODE", nullable = false)
    private Integer tgCode;

    @Column(name = "TG_PRPRTY_CODE", columnDefinition = "VARCHAR")
    private String tgPropertyCode;

    @Column(name = "TG_SRL_NMBR", columnDefinition = "VARCHAR")
    private String tgSerialNumber;

    @Column(name = "TG_DCMNT_NMBR", columnDefinition = "VARCHAR")
    private String tgDocumentNumber;

    @Column(name = "TG_SPCS", columnDefinition = "VARCHAR")
    private String tgSpecs;

    @Column(name = "TG_EXPRY_DT", columnDefinition = "DATETIME")
    private Date tgExpiryDate;

    @Column(name = "TG_TXN_DT", columnDefinition = "DATETIME")
    private Date tgTransactionDate;

    @Column(name = "TG_TYP", columnDefinition = "VARCHAR")
    private String tgType;

    @Column(name = "TG_AD_CMPNY", columnDefinition = "INT")
    private Integer tgAdCompany;

    @JoinColumn(name = "AD_USER", referencedColumnName = "USR_CODE")
    @ManyToOne
    private LocalAdUser adUser;

    @JoinColumn(name = "AP_PURCHASE_ORDER_LINE", referencedColumnName = "AP_PL_CODE")
    @ManyToOne
    private LocalApPurchaseOrderLine apPurchaseOrderLine;

    @JoinColumn(name = "AP_PURCHASE_REQUISITION_LINE", referencedColumnName = "PRL_CODE")
    @ManyToOne
    private LocalApPurchaseRequisitionLine apPurchaseRequisitionLine;

    @JoinColumn(name = "AP_VOUCHER_LINE_ITEM", referencedColumnName = "VLI_CODE")
    @ManyToOne
    private LocalApVoucherLineItem apVoucherLineItem;

    @JoinColumn(name = "AR_INVOICE_LINE_ITEM", referencedColumnName = "ILI_CODE")
    @ManyToOne
    private LocalArInvoiceLineItem arInvoiceLineItem;

    @JoinColumn(name = "AR_JOB_ORDER_INVOICE_LINE", referencedColumnName = "JIL_CODE")
    @ManyToOne
    private LocalArJobOrderInvoiceLine arJobOrderInvoiceLine;

    @JoinColumn(name = "AR_JOB_ORDER_LINE", referencedColumnName = "JOL_CODE")
    @ManyToOne
    private LocalArJobOrderLine arJobOrderLine;

    @JoinColumn(name = "AR_SALES_ORDER_INVOICE_LINE", referencedColumnName = "AR_SIL_CODE")
    @ManyToOne
    private LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine;

    @JoinColumn(name = "AR_SALES_ORDER_LINE", referencedColumnName = "SOL_CODE")
    @ManyToOne
    private LocalArSalesOrderLine arSalesOrderLine;

    @JoinColumn(name = "INV_ADJUSTMENT_LINE", referencedColumnName = "AL_CODE")
    @ManyToOne
    private LocalInvAdjustmentLine invAdjustmentLine;

    @JoinColumn(name = "INV_BRANCH_STOCK_TRANSFER_LINE", referencedColumnName = "BSL_CODE")
    @ManyToOne
    private LocalInvBranchStockTransferLine invBranchStockTransferLine;

    @JoinColumn(name = "INV_ITEM_LOCATION", referencedColumnName = "INV_IL_CODE")
    @ManyToOne
    private LocalInvItemLocation invItemLocation;

    @JoinColumn(name = "INV_PHYSICAL_INVENTORY_LINE", referencedColumnName = "PIL_CODE")
    @ManyToOne
    private LocalInvPhysicalInventoryLine invPhysicalInventoryLine;

    @JoinColumn(name = "INV_STOCK_TRANSFER_LINE", referencedColumnName = "STL_CODE")
    @ManyToOne
    private LocalInvStockTransferLine invStockTransferLine;

    @OneToMany(mappedBy = "invTag", fetch = FetchType.LAZY)
    private List<LocalInvDepreciationLedger> invDepreciationLedgers;

    public Integer getTgCode() {

        return tgCode;
    }

    public void setTgCode(Integer TG_CODE) {

        this.tgCode = TG_CODE;
    }

    public String getTgPropertyCode() {

        return tgPropertyCode;
    }

    public void setTgPropertyCode(String TG_PRPRTY_CODE) {

        this.tgPropertyCode = TG_PRPRTY_CODE;
    }

    public String getTgSerialNumber() {

        return tgSerialNumber;
    }

    public void setTgSerialNumber(String TG_SRL_NMBR) {

        this.tgSerialNumber = TG_SRL_NMBR;
    }

    public String getTgDocumentNumber() {

        return tgDocumentNumber;
    }

    public void setTgDocumentNumber(String TG_DCMNT_NMBR) {

        this.tgDocumentNumber = TG_DCMNT_NMBR;
    }

    public String getTgSpecs() {

        return tgSpecs;
    }

    public void setTgSpecs(String TG_SPCS) {

        this.tgSpecs = TG_SPCS;
    }

    public Date getTgExpiryDate() {

        return tgExpiryDate;
    }

    public void setTgExpiryDate(Date TG_EXPRY_DT) {

        this.tgExpiryDate = TG_EXPRY_DT;
    }

    public Date getTgTransactionDate() {

        return tgTransactionDate;
    }

    public void setTgTransactionDate(Date TG_TXN_DT) {

        this.tgTransactionDate = TG_TXN_DT;
    }

    public String getTgType() {

        return tgType;
    }

    public void setTgType(String TG_TYP) {

        this.tgType = TG_TYP;
    }

    public Integer getTgAdCompany() {

        return tgAdCompany;
    }

    public void setTgAdCompany(Integer TG_AD_CMPNY) {

        this.tgAdCompany = TG_AD_CMPNY;
    }

    public LocalAdUser getAdUser() {

        return adUser;
    }

    public void setAdUser(LocalAdUser adUser) {

        this.adUser = adUser;
    }

    public LocalApPurchaseOrderLine getApPurchaseOrderLine() {

        return apPurchaseOrderLine;
    }

    public void setApPurchaseOrderLine(LocalApPurchaseOrderLine apPurchaseOrderLine) {

        this.apPurchaseOrderLine = apPurchaseOrderLine;
    }

    public LocalApPurchaseRequisitionLine getApPurchaseRequisitionLine() {

        return apPurchaseRequisitionLine;
    }

    public void setApPurchaseRequisitionLine(
            LocalApPurchaseRequisitionLine apPurchaseRequisitionLine) {

        this.apPurchaseRequisitionLine = apPurchaseRequisitionLine;
    }

    public LocalApVoucherLineItem getApVoucherLineItem() {

        return apVoucherLineItem;
    }

    public void setApVoucherLineItem(LocalApVoucherLineItem apVoucherLineItem) {

        this.apVoucherLineItem = apVoucherLineItem;
    }

    public LocalArInvoiceLineItem getArInvoiceLineItem() {

        return arInvoiceLineItem;
    }

    public void setArInvoiceLineItem(LocalArInvoiceLineItem arInvoiceLineItem) {

        this.arInvoiceLineItem = arInvoiceLineItem;
    }

    public LocalArJobOrderInvoiceLine getArJobOrderInvoiceLine() {

        return arJobOrderInvoiceLine;
    }

    public void setArJobOrderInvoiceLine(LocalArJobOrderInvoiceLine arJobOrderInvoiceLine) {

        this.arJobOrderInvoiceLine = arJobOrderInvoiceLine;
    }

    public LocalArJobOrderLine getArJobOrderLine() {

        return arJobOrderLine;
    }

    public void setArJobOrderLine(LocalArJobOrderLine arJobOrderLine) {

        this.arJobOrderLine = arJobOrderLine;
    }

    public LocalArSalesOrderInvoiceLine getArSalesOrderInvoiceLine() {

        return arSalesOrderInvoiceLine;
    }

    public void setArSalesOrderInvoiceLine(LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine) {

        this.arSalesOrderInvoiceLine = arSalesOrderInvoiceLine;
    }

    public LocalArSalesOrderLine getArSalesOrderLine() {

        return arSalesOrderLine;
    }

    public void setArSalesOrderLine(LocalArSalesOrderLine arSalesOrderLine) {

        this.arSalesOrderLine = arSalesOrderLine;
    }

    public LocalInvAdjustmentLine getInvAdjustmentLine() {

        return invAdjustmentLine;
    }

    public void setInvAdjustmentLine(LocalInvAdjustmentLine invAdjustmentLine) {

        this.invAdjustmentLine = invAdjustmentLine;
    }

    public LocalInvBranchStockTransferLine getInvBranchStockTransferLine() {

        return invBranchStockTransferLine;
    }

    public void setInvBranchStockTransferLine(
            LocalInvBranchStockTransferLine invBranchStockTransferLine) {

        this.invBranchStockTransferLine = invBranchStockTransferLine;
    }

    public LocalInvItemLocation getInvItemLocation() {

        return invItemLocation;
    }

    public void setInvItemLocation(LocalInvItemLocation invItemLocation) {

        this.invItemLocation = invItemLocation;
    }

    public LocalInvPhysicalInventoryLine getInvPhysicalInventoryLine() {

        return invPhysicalInventoryLine;
    }

    public void setInvPhysicalInventoryLine(LocalInvPhysicalInventoryLine invPhysicalInventoryLine) {

        this.invPhysicalInventoryLine = invPhysicalInventoryLine;
    }

    public LocalInvStockTransferLine getInvStockTransferLine() {

        return invStockTransferLine;
    }

    public void setInvStockTransferLine(LocalInvStockTransferLine invStockTransferLine) {

        this.invStockTransferLine = invStockTransferLine;
    }

    @XmlTransient
    public List getInvDepreciationLedgers() {

        return invDepreciationLedgers;
    }

    public void setInvDepreciationLedgers(List invDepreciationLedgers) {

        this.invDepreciationLedgers = invDepreciationLedgers;
    }

    public void addInvDepreciationLedger(LocalInvDepreciationLedger entity) {

        try {
            entity.setInvTag(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvDepreciationLedger(LocalInvDepreciationLedger entity) {

        try {
            entity.setInvTag(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}