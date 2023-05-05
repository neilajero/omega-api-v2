package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.entities.ap.LocalApPurchaseRequisitionLine;
import com.ejb.entities.ap.LocalApVoucherLineItem;
import com.ejb.entities.ar.LocalArInvoiceLineItem;
import com.ejb.entities.ar.LocalArJobOrderLine;
import com.ejb.entities.ar.LocalArSalesOrderLine;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;

@Entity(name = "InvItemLocation")
@Table(name = "INV_ITM_LCTN")
public class LocalInvItemLocation extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INV_IL_CODE", nullable = false)
    private Integer ilCode;

    @Column(name = "IL_RCK", columnDefinition = "VARCHAR")
    private String ilRack;

    @Column(name = "IL_BN", columnDefinition = "VARCHAR")
    private String ilBin;

    @Column(name = "IL_RRDR_PNT", columnDefinition = "DOUBLE")
    private double ilReorderPoint = 0;

    @Column(name = "IL_RRDR_QTY", columnDefinition = "DOUBLE")
    private double ilReorderQuantity = 0;

    @Column(name = "IL_RRDR_LVL", columnDefinition = "DOUBLE")
    private double ilReorderLevel = 0;

    @Column(name = "IL_CMMTTD_QTY", columnDefinition = "DOUBLE")
    private double ilCommittedQuantity = 0;

    @Column(name = "IL_GL_COA_SLS_ACCNT", columnDefinition = "INT")
    private Integer ilGlCoaSalesAccount;

    @Column(name = "IL_GL_COA_INVNTRY_ACCNT", columnDefinition = "INT")
    private Integer ilGlCoaInventoryAccount;

    @Column(name = "IL_GL_CST_OF_SLS_ACCNT", columnDefinition = "INT")
    private Integer ilGlCoaCostOfSalesAccount;

    @Column(name = "IL_GL_COA_WIP_ACCNT", columnDefinition = "INT")
    private Integer ilGlCoaWipAccount;

    @Column(name = "IL_GL_COA_ACCRD_INVNTRY_ACCNT", columnDefinition = "INT")
    private Integer ilGlCoaAccruedInventoryAccount;

    @Column(name = "IL_GL_COA_SLS_RTRN_ACCNT", columnDefinition = "INT")
    private Integer ilGlCoaSalesReturnAccount;

    @Column(name = "IL_GL_COA_EXPNS_ACCNT", columnDefinition = "INT")
    private Integer ilGlCoaExpenseAccount;

    @Column(name = "IL_SBJCT_TO_CMMSSN", columnDefinition = "TINYINT")
    private byte ilSubjectToCommission;

    @Column(name = "IL_AD_CMPNY", columnDefinition = "INT")
    private Integer ilAdCompany;

    @JoinColumn(name = "INV_ITEM", referencedColumnName = "II_CODE")
    @ManyToOne
    private LocalInvItem invItem;

    @JoinColumn(name = "INV_LOCATION", referencedColumnName = "LOC_CODE")
    @ManyToOne
    private LocalInvLocation invLocation;

    @OneToMany(mappedBy = "invItemLocation", fetch = FetchType.LAZY)
    private List<LocalInvAdjustmentLine> invAdjustmentLines;

    @OneToMany(mappedBy = "invItemLocation", fetch = FetchType.LAZY)
    private List<LocalInvTag> invTags;

    @OneToMany(mappedBy = "invItemLocation", fetch = FetchType.LAZY)
    private List<LocalInvPhysicalInventoryLine> invPhysicalInventoryLines;

    @OneToMany(mappedBy = "invItemLocation", fetch = FetchType.LAZY)
    private List<LocalInvCosting> invCostings;

    @OneToMany(mappedBy = "invItemLocation", fetch = FetchType.LAZY)
    private List<LocalApVoucherLineItem> apVoucherLineItems;

    @OneToMany(mappedBy = "invItemLocation", fetch = FetchType.LAZY)
    private List<LocalArInvoiceLineItem> arInvoiceLineItems;

    @OneToMany(mappedBy = "invItemLocation", fetch = FetchType.LAZY)
    private List<LocalApPurchaseOrderLine> apPurchaseOrderLines;

    @OneToMany(mappedBy = "invItemLocation", fetch = FetchType.LAZY)
    private List<LocalArSalesOrderLine> arSalesOrderLines;

    @OneToMany(mappedBy = "invItemLocation", fetch = FetchType.LAZY)
    private List<LocalAdBranchItemLocation> adBranchItemLocations;

    @OneToMany(mappedBy = "invItemLocation", fetch = FetchType.LAZY)
    private List<LocalApPurchaseRequisitionLine> apPurchaseRequisitionLines;

    @OneToMany(mappedBy = "invItemLocation", fetch = FetchType.LAZY)
    private List<LocalArJobOrderLine> arJobOrderLines;

    @OneToMany(mappedBy = "invItemLocation", fetch = FetchType.LAZY)
    private List<LocalInvBranchStockTransferLine> invBranchStockTransferLines;

    @OneToMany(mappedBy = "invItemLocation", fetch = FetchType.LAZY)
    private List<LocalInvLineItem> invLineItems;

    public Integer getIlCode() {

        return ilCode;
    }

    public void setIlCode(Integer INV_IL_CODE) {

        this.ilCode = INV_IL_CODE;
    }

    public String getIlRack() {

        return ilRack;
    }

    public void setIlRack(String IL_RCK) {

        this.ilRack = IL_RCK;
    }

    public String getIlBin() {

        return ilBin;
    }

    public void setIlBin(String IL_BN) {

        this.ilBin = IL_BN;
    }

    public double getIlReorderPoint() {

        return ilReorderPoint;
    }

    public void setIlReorderPoint(double IL_RRDR_PNT) {

        this.ilReorderPoint = IL_RRDR_PNT;
    }

    public double getIlReorderQuantity() {

        return ilReorderQuantity;
    }

    public void setIlReorderQuantity(double IL_RRDR_QTY) {

        this.ilReorderQuantity = IL_RRDR_QTY;
    }

    public double getIlReorderLevel() {

        return ilReorderLevel;
    }

    public void setIlReorderLevel(double IL_RRDR_LVL) {

        this.ilReorderLevel = IL_RRDR_LVL;
    }

    public double getIlCommittedQuantity() {

        return ilCommittedQuantity;
    }

    public void setIlCommittedQuantity(double IL_CMMTTD_QTY) {

        this.ilCommittedQuantity = IL_CMMTTD_QTY;
    }

    public Integer getIlGlCoaSalesAccount() {

        return ilGlCoaSalesAccount;
    }

    public void setIlGlCoaSalesAccount(Integer IL_GL_COA_SLS_ACCNT) {

        this.ilGlCoaSalesAccount = IL_GL_COA_SLS_ACCNT;
    }

    public Integer getIlGlCoaInventoryAccount() {

        return ilGlCoaInventoryAccount;
    }

    public void setIlGlCoaInventoryAccount(Integer IL_GL_COA_INVNTRY_ACCNT) {

        this.ilGlCoaInventoryAccount = IL_GL_COA_INVNTRY_ACCNT;
    }

    public Integer getIlGlCoaCostOfSalesAccount() {

        return ilGlCoaCostOfSalesAccount;
    }

    public void setIlGlCoaCostOfSalesAccount(Integer IL_GL_CST_OF_SLS_ACCNT) {

        this.ilGlCoaCostOfSalesAccount = IL_GL_CST_OF_SLS_ACCNT;
    }

    public Integer getIlGlCoaWipAccount() {

        return ilGlCoaWipAccount;
    }

    public void setIlGlCoaWipAccount(Integer IL_GL_COA_WIP_ACCNT) {

        this.ilGlCoaWipAccount = IL_GL_COA_WIP_ACCNT;
    }

    public Integer getIlGlCoaAccruedInventoryAccount() {

        return ilGlCoaAccruedInventoryAccount;
    }

    public void setIlGlCoaAccruedInventoryAccount(Integer IL_GL_COA_ACCRD_INVNTRY_ACCNT) {

        this.ilGlCoaAccruedInventoryAccount = IL_GL_COA_ACCRD_INVNTRY_ACCNT;
    }

    public Integer getIlGlCoaSalesReturnAccount() {

        return ilGlCoaSalesReturnAccount;
    }

    public void setIlGlCoaSalesReturnAccount(Integer IL_GL_COA_SLS_RTRN_ACCNT) {

        this.ilGlCoaSalesReturnAccount = IL_GL_COA_SLS_RTRN_ACCNT;
    }

    public Integer getIlGlCoaExpenseAccount() {

        return ilGlCoaExpenseAccount;
    }

    public void setIlGlCoaExpenseAccount(Integer IL_GL_COA_EXPNS_ACCNT) {

        this.ilGlCoaExpenseAccount = IL_GL_COA_EXPNS_ACCNT;
    }

    public byte getIlSubjectToCommission() {

        return ilSubjectToCommission;
    }

    public void setIlSubjectToCommission(byte IL_SBJCT_TO_CMMSSN) {

        this.ilSubjectToCommission = IL_SBJCT_TO_CMMSSN;
    }

    public Integer getIlAdCompany() {

        return ilAdCompany;
    }

    public void setIlAdCompany(Integer IL_AD_CMPNY) {

        this.ilAdCompany = IL_AD_CMPNY;
    }

    public LocalInvItem getInvItem() {

        return invItem;
    }

    public void setInvItem(LocalInvItem invItem) {

        this.invItem = invItem;
    }

    public LocalInvLocation getInvLocation() {

        return invLocation;
    }

    public void setInvLocation(LocalInvLocation invLocation) {

        this.invLocation = invLocation;
    }

    @XmlTransient
    public List getInvAdjustmentLines() {

        return invAdjustmentLines;
    }

    public void setInvAdjustmentLines(List invAdjustmentLines) {

        this.invAdjustmentLines = invAdjustmentLines;
    }

    @XmlTransient
    public List getInvTags() {

        return invTags;
    }

    public void setInvTags(List invTags) {

        this.invTags = invTags;
    }

    @XmlTransient
    public List getInvPhysicalInventoryLines() {

        return invPhysicalInventoryLines;
    }

    public void setInvPhysicalInventoryLines(List invPhysicalInventoryLines) {

        this.invPhysicalInventoryLines = invPhysicalInventoryLines;
    }

    @XmlTransient
    public List getInvCostings() {

        return invCostings;
    }

    public void setInvCostings(List invCostings) {

        this.invCostings = invCostings;
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
    public List getArSalesOrderLines() {

        return arSalesOrderLines;
    }

    public void setArSalesOrderLines(List arSalesOrderLines) {

        this.arSalesOrderLines = arSalesOrderLines;
    }

    @XmlTransient
    public List getAdBranchItemLocations() {

        return adBranchItemLocations;
    }

    public void setAdBranchItemLocations(List adBranchItemLocations) {

        this.adBranchItemLocations = adBranchItemLocations;
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
    public List getInvLineItems() {

        return invLineItems;
    }

    public void setInvLineItems(List invLineItems) {

        this.invLineItems = invLineItems;
    }

    public void addInvAdjustmentLine(LocalInvAdjustmentLine entity) {

        try {
            entity.setInvItemLocation(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvAdjustmentLine(LocalInvAdjustmentLine entity) {

        try {
            entity.setInvItemLocation(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvPhysicalInventoryLine(LocalInvPhysicalInventoryLine entity) {

        try {
            entity.setInvItemLocation(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvPhysicalInventoryLine(LocalInvPhysicalInventoryLine entity) {

        try {
            entity.setInvItemLocation(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvCosting(LocalInvCosting entity) {

        try {
            entity.setInvItemLocation(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvCosting(LocalInvCosting entity) {

        try {
            entity.setInvItemLocation(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApVoucherLineItem(LocalApVoucherLineItem entity) {

        try {
            entity.setInvItemLocation(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApVoucherLineItem(LocalApVoucherLineItem entity) {

        try {
            entity.setInvItemLocation(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArInvoiceLineItem(LocalArInvoiceLineItem entity) {

        try {
            entity.setInvItemLocation(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArInvoiceLineItem(LocalArInvoiceLineItem entity) {

        try {
            entity.setInvItemLocation(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApPurchaseOrderLine(LocalApPurchaseOrderLine entity) {

        try {
            entity.setInvItemLocation(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApPurchaseOrderLine(LocalApPurchaseOrderLine entity) {

        try {
            entity.setInvItemLocation(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArSalesOrderLine(LocalArSalesOrderLine entity) {

        try {
            entity.setInvItemLocation(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArSalesOrderLine(LocalArSalesOrderLine entity) {

        try {
            entity.setInvItemLocation(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchItemLocation(LocalAdBranchItemLocation entity) {

        try {
            entity.setInvItemLocation(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchItemLocation(LocalAdBranchItemLocation entity) {

        try {
            entity.setInvItemLocation(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApPurchaseRequisitionLine(LocalApPurchaseRequisitionLine entity) {

        try {
            entity.setInvItemLocation(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApPurchaseRequisitionLine(LocalApPurchaseRequisitionLine entity) {

        try {
            entity.setInvItemLocation(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvBranchStockTransferLine(LocalInvBranchStockTransferLine entity) {

        try {
            entity.setInvItemLocation(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvBranchStockTransferLine(LocalInvBranchStockTransferLine entity) {

        try {
            entity.setInvItemLocation(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvLineItem(LocalInvLineItem entity) {

        try {
            entity.setInvItemLocation(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvLineItem(LocalInvLineItem entity) {

        try {
            entity.setInvItemLocation(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}