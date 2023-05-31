package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.entities.ap.LocalApVoucherLineItem;
import com.ejb.entities.ar.LocalArInvoiceLineItem;
import com.ejb.entities.ar.LocalArJobOrderInvoiceLine;
import com.ejb.entities.ar.LocalArSalesOrderInvoiceLine;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity(name = "InvCosting")
@Table(name = "INV_CSTNG")
public class LocalInvCosting extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INV_CST_CODE", nullable = false)
    private Integer cstCode;

    @Column(name = "CST_DT", columnDefinition = "DATETIME")
    private Date cstDate;

    @Column(name = "CST_DT_TO_LNG", columnDefinition = "BIGINT")
    private long cstDateToLong;

    @Column(name = "CST_LN_NMBR", columnDefinition = "INT")
    private Integer cstLineNumber;

    @Column(name = "CST_QNTTY", columnDefinition = "DOUBLE")
    private double cstQuantity = 0;

    @Column(name = "CST_CST", columnDefinition = "DOUBLE")
    private double cstCost = 0;

    @Column(name = "CST_QTY_RCVD", columnDefinition = "DOUBLE")
    private double cstQuantityReceived = 0;

    @Column(name = "CST_ITM_CST", columnDefinition = "DOUBLE")
    private double cstItemCost = 0;
    @Column(name = "CST_ASSMBLY_QTY", columnDefinition = "DOUBLE")
    private double cstAssemblyQuantity = 0;
    @Column(name = "CST_ASSMBLY_CST", columnDefinition = "DOUBLE")
    private double cstAssemblyCost = 0;
    @Column(name = "CST_ADJST_QTY", columnDefinition = "DOUBLE")
    private double cstAdjustQuantity = 0;
    @Column(name = "CST_ADJST_CST", columnDefinition = "DOUBLE")
    private double cstAdjustCost = 0;
    @Column(name = "CST_QTY_SLD", columnDefinition = "DOUBLE")
    private double cstQuantitySold = 0;
    @Column(name = "CST_CST_OF_SLS", columnDefinition = "DOUBLE")
    private double cstCostOfSales = 0;
    @Column(name = "CST_RMNNG_QTY", columnDefinition = "DOUBLE")
    private double cstRemainingQuantity = 0;
    @Column(name = "CST_RMNNG_VL", columnDefinition = "DOUBLE")
    private double cstRemainingValue = 0;
    @Column(name = "CST_VRNC_QTY", columnDefinition = "DOUBLE")
    private double cstVarianceQuantity = 0;
    @Column(name = "CST_VRNC_VL", columnDefinition = "DOUBLE")
    private double cstVarianceValue = 0;
    @Column(name = "CST_RMNNG_LIFO_QTY", columnDefinition = "DOUBLE")
    private double cstRemainingLifoQuantity = 0;
    @Column(name = "CST_RMNNG_LIFO_OUT_QTY", columnDefinition = "DOUBLE")
    private double cstRemainingLifoOutQuantity = 0;
    @Column(name = "CST_EXPRY_DT", columnDefinition = "VARCHAR")
    private String cstExpiryDate;
    @Column(name = "CST_QC_NM", columnDefinition = "VARCHAR")
    private String cstQCNumber;
    @Column(name = "CST_QC_EXPRY_DT", columnDefinition = "DATETIME")
    private Date cstQCExpiryDate;
    @Column(name = "CST_AD_BRNCH", columnDefinition = "INT")
    private Integer cstAdBranch;
    @Column(name = "CST_AD_CMPNY", columnDefinition = "INT")
    private Integer cstAdCompany;
    @JoinColumn(name = "AP_PURCHASE_ORDER_LINE", referencedColumnName = "AP_PL_CODE")
    @ManyToOne
    private LocalApPurchaseOrderLine apPurchaseOrderLine;
    @JoinColumn(name = "AP_VOUCHER_LINE_ITEM", referencedColumnName = "VLI_CODE")
    @ManyToOne
    private LocalApVoucherLineItem apVoucherLineItem;
    @JoinColumn(name = "AR_INVOICE_LINE_ITEM", referencedColumnName = "ILI_CODE")
    @ManyToOne
    private LocalArInvoiceLineItem arInvoiceLineItem;
    @JoinColumn(name = "AR_JOB_ORDER_INVOICE_LINE", referencedColumnName = "JIL_CODE")
    @ManyToOne
    private LocalArJobOrderInvoiceLine arJobOrderInvoiceLine;
    @JoinColumn(name = "AR_SALES_ORDER_INVOICE_LINE", referencedColumnName = "AR_SIL_CODE")
    @ManyToOne
    private LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine;
    @JoinColumn(name = "INV_ADJUSTMENT_LINE", referencedColumnName = "AL_CODE")
    @ManyToOne
    private LocalInvAdjustmentLine invAdjustmentLine;
    @JoinColumn(name = "INV_BRANCH_STOCK_TRANSFER_LINE", referencedColumnName = "BSL_CODE")
    @ManyToOne
    private LocalInvBranchStockTransferLine invBranchStockTransferLine;
    @JoinColumn(name = "INV_ITEM_LOCATION", referencedColumnName = "INV_IL_CODE")
    @ManyToOne
    private LocalInvItemLocation invItemLocation;
    @JoinColumn(name = "INV_STOCK_TRANSFER_LINE", referencedColumnName = "STL_CODE")
    @ManyToOne
    private LocalInvStockTransferLine invStockTransferLine;
    @JoinColumn(name = "INV_BUILD_UNBUILD_ASSEMBLY_LINE", referencedColumnName = "BL_CODE")
    @ManyToOne
    private LocalInvBuildUnbuildAssemblyLine invBuildUnbuildAssemblyLine;

    public double getCstAssemblyQuantity() {

        return cstAssemblyQuantity;
    }

    public void setCstAssemblyQuantity(double cstAssemblyQuantity) {

        this.cstAssemblyQuantity = cstAssemblyQuantity;
    }

    public double getCstAssemblyCost() {

        return cstAssemblyCost;
    }

    public void setCstAssemblyCost(double cstAssemblyCost) {

        this.cstAssemblyCost = cstAssemblyCost;
    }

    public LocalInvBuildUnbuildAssemblyLine getInvBuildUnbuildAssemblyLine() {

        return invBuildUnbuildAssemblyLine;
    }

    public void setInvBuildUnbuildAssemblyLine(LocalInvBuildUnbuildAssemblyLine invBuildUnbuildAssemblyLine) {

        this.invBuildUnbuildAssemblyLine = invBuildUnbuildAssemblyLine;
    }

    public Integer getCstCode() {

        return cstCode;
    }

    public void setCstCode(Integer INV_CST_CODE) {

        this.cstCode = INV_CST_CODE;
    }

    public Date getCstDate() {

        return cstDate;
    }

    public void setCstDate(Date CST_DT) {

        this.cstDate = CST_DT;
    }

    public long getCstDateToLong() {

        return cstDateToLong;
    }

    public void setCstDateToLong(long CST_DT_TO_LNG) {

        this.cstDateToLong = CST_DT_TO_LNG;
    }

    public Integer getCstLineNumber() {

        return cstLineNumber;
    }

    public void setCstLineNumber(Integer CST_LN_NMBR) {

        this.cstLineNumber = CST_LN_NMBR;
    }

    public double getCstQuantity() {

        return cstQuantity;
    }

    public void setCstQuantity(double CST_QNTTY) {

        this.cstQuantity = CST_QNTTY;
    }

    public double getCstCost() {

        return cstCost;
    }

    public void setCstCost(double CST_CST) {

        this.cstCost = CST_CST;
    }

    public double getCstQuantityReceived() {

        return cstQuantityReceived;
    }

    public void setCstQuantityReceived(double CST_QTY_RCVD) {

        this.cstQuantityReceived = CST_QTY_RCVD;
    }

    public double getCstItemCost() {

        return cstItemCost;
    }

    public void setCstItemCost(double CST_ITM_CST) {

        this.cstItemCost = CST_ITM_CST;
    }

    public double getCstAdjustQuantity() {

        return cstAdjustQuantity;
    }

    public void setCstAdjustQuantity(double CST_ADJST_QTY) {

        this.cstAdjustQuantity = CST_ADJST_QTY;
    }

    public double getCstAdjustCost() {

        return cstAdjustCost;
    }

    public void setCstAdjustCost(double CST_ADJST_CST) {

        this.cstAdjustCost = CST_ADJST_CST;
    }

    public double getCstQuantitySold() {

        return cstQuantitySold;
    }

    public void setCstQuantitySold(double CST_QTY_SLD) {

        this.cstQuantitySold = CST_QTY_SLD;
    }

    public double getCstCostOfSales() {

        return cstCostOfSales;
    }

    public void setCstCostOfSales(double CST_CST_OF_SLS) {

        this.cstCostOfSales = CST_CST_OF_SLS;
    }

    public double getCstRemainingQuantity() {

        return cstRemainingQuantity;
    }

    public void setCstRemainingQuantity(double CST_RMNNG_QTY) {

        this.cstRemainingQuantity = CST_RMNNG_QTY;
    }

    public double getCstRemainingValue() {

        return cstRemainingValue;
    }

    public void setCstRemainingValue(double CST_RMNNG_VL) {

        this.cstRemainingValue = CST_RMNNG_VL;
    }

    public double getCstVarianceQuantity() {

        return cstVarianceQuantity;
    }

    public void setCstVarianceQuantity(double CST_VRNC_QTY) {

        this.cstVarianceQuantity = CST_VRNC_QTY;
    }

    public double getCstVarianceValue() {

        return cstVarianceValue;
    }

    public void setCstVarianceValue(double CST_VRNC_VL) {

        this.cstVarianceValue = CST_VRNC_VL;
    }

    public double getCstRemainingLifoQuantity() {

        return cstRemainingLifoQuantity;
    }

    public void setCstRemainingLifoQuantity(double CST_RMNNG_LIFO_QTY) {

        this.cstRemainingLifoQuantity = CST_RMNNG_LIFO_QTY;
    }

    public double getCstRemainingLifoOutQuantity() {

        return cstRemainingLifoOutQuantity;
    }

    public void setCstRemainingLifoOutQuantity(double CST_RMNNG_LIFO_OUT_QTY) {

        this.cstRemainingLifoOutQuantity = CST_RMNNG_LIFO_OUT_QTY;
    }

    public String getCstExpiryDate() {

        return cstExpiryDate;
    }

    public void setCstExpiryDate(String CST_EXPRY_DT) {

        this.cstExpiryDate = CST_EXPRY_DT;
    }

    public String getCstQCNumber() {

        return cstQCNumber;
    }

    public void setCstQCNumber(String CST_QC_NM) {

        this.cstQCNumber = CST_QC_NM;
    }

    public Date getCstQCExpiryDate() {

        return cstQCExpiryDate;
    }

    public void setCstQCExpiryDate(Date CST_QC_EXPRY_DT) {

        this.cstQCExpiryDate = CST_QC_EXPRY_DT;
    }

    public Integer getCstAdBranch() {

        return cstAdBranch;
    }

    public void setCstAdBranch(Integer CST_AD_BRNCH) {

        this.cstAdBranch = CST_AD_BRNCH;
    }

    public Integer getCstAdCompany() {

        return cstAdCompany;
    }

    public void setCstAdCompany(Integer CST_AD_CMPNY) {

        this.cstAdCompany = CST_AD_CMPNY;
    }

    public LocalApPurchaseOrderLine getApPurchaseOrderLine() {

        return apPurchaseOrderLine;
    }

    public void setApPurchaseOrderLine(LocalApPurchaseOrderLine apPurchaseOrderLine) {

        this.apPurchaseOrderLine = apPurchaseOrderLine;
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

    public LocalArSalesOrderInvoiceLine getArSalesOrderInvoiceLine() {

        return arSalesOrderInvoiceLine;
    }

    public void setArSalesOrderInvoiceLine(LocalArSalesOrderInvoiceLine arSalesOrderInvoiceLine) {

        this.arSalesOrderInvoiceLine = arSalesOrderInvoiceLine;
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

    public LocalInvStockTransferLine getInvStockTransferLine() {

        return invStockTransferLine;
    }

    public void setInvStockTransferLine(LocalInvStockTransferLine invStockTransferLine) {

        this.invStockTransferLine = invStockTransferLine;
    }

}