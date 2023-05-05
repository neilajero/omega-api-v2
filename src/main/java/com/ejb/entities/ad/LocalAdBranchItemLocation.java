package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import com.ejb.entities.inv.LocalInvItemLocation;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdBranchItemLocation")
@Table(name = "AD_BR_IL")
public class LocalAdBranchItemLocation extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BIL_CODE", nullable = false)
    private Integer bilCode;

    @Column(name = "BIL_RCK", columnDefinition = "VARCHAR")
    private String bilRack;

    @Column(name = "BIL_BN", columnDefinition = "VARCHAR")
    private String bilBin;

    @Column(name = "BIL_RRDR_PNT", columnDefinition = "DOUBLE")
    private double bilReorderPoint = 0;

    @Column(name = "BIL_RRDR_QTY", columnDefinition = "DOUBLE")
    private double bilReorderQuantity = 0;

    @Column(name = "BIL_COA_GL_SLS_ACCNT", columnDefinition = "INT")
    private Integer bilCoaGlSalesAccount;

    @Column(name = "BIL_COA_GL_INVNTRY_ACCNT", columnDefinition = "INT")
    private Integer bilCoaGlInventoryAccount;

    @Column(name = "BIL_COA_GL_CST_OF_SLS_ACCNT", columnDefinition = "INT")
    private Integer bilCoaGlCostOfSalesAccount;

    @Column(name = "BIL_COA_GL_WIP_ACCNT", columnDefinition = "INT")
    private Integer bilCoaGlWipAccount;

    @Column(name = "BIL_COA_GL_ACCRD_INVNTRY_ACCNT", columnDefinition = "INT")
    private Integer bilCoaGlAccruedInventoryAccount;

    @Column(name = "BIL_COA_GL_SLS_RTRN_ACCNT", columnDefinition = "INT")
    private Integer bilCoaGlSalesReturnAccount;

    @Column(name = "BIL_COA_GL_EXPNS_ACCNT", columnDefinition = "INT")
    private Integer bilCoaGlExpenseAccount;

    @Column(name = "BIL_SBJCT_TO_CMMSSN", columnDefinition = "TINYINT")
    private byte bilSubjectToCommission;

    @Column(name = "BIL_HST1_SLS", columnDefinition = "DOUBLE")
    private double bilHist1Sales = 0;

    @Column(name = "BIL_HST2_SLS", columnDefinition = "DOUBLE")
    private double bilHist2Sales = 0;

    @Column(name = "BIL_PRJ_SLS", columnDefinition = "DOUBLE")
    private double bilProjectedSales = 0;

    @Column(name = "BIL_DLVRY_TME", columnDefinition = "DOUBLE")
    private double bilDeliveryTime = 0;

    @Column(name = "BIL_DLVRY_BUFF", columnDefinition = "DOUBLE")
    private double bilDeliveryBuffer = 0;

    @Column(name = "BIL_ORDR_PR_YR", columnDefinition = "DOUBLE")
    private double bilOrderPerYear = 0;

    @Column(name = "BIL_DS_ITM", columnDefinition = "VARCHAR")
    private char bilItemDownloadStatus;

    @Column(name = "BIL_DS_LOC", columnDefinition = "VARCHAR")
    private char bilLocationDownloadStatus;

    @Column(name = "BIL_DS_IL", columnDefinition = "VARCHAR")
    private char bilItemLocationDownloadStatus;

    @Column(name = "BIL_AD_CMPNY", columnDefinition = "INT")
    private Integer bilAdCompany;

    @JoinColumn(name = "AD_BRANCH", referencedColumnName = "BR_CODE")
    @ManyToOne
    private LocalAdBranch adBranch;

    @JoinColumn(name = "INV_ITEM_LOCATION", referencedColumnName = "INV_IL_CODE")
    @ManyToOne
    private LocalInvItemLocation invItemLocation;

    public Integer getBilCode() {

        return bilCode;
    }

    public void setBilCode(Integer BIL_CODE) {

        this.bilCode = BIL_CODE;
    }

    public String getBilRack() {

        return bilRack;
    }

    public void setBilRack(String BIL_RCK) {

        this.bilRack = BIL_RCK;
    }

    public String getBilBin() {

        return bilBin;
    }

    public void setBilBin(String BIL_BN) {

        this.bilBin = BIL_BN;
    }

    public double getBilReorderPoint() {

        return bilReorderPoint;
    }

    public void setBilReorderPoint(double BIL_RRDR_PNT) {

        this.bilReorderPoint = BIL_RRDR_PNT;
    }

    public double getBilReorderQuantity() {

        return bilReorderQuantity;
    }

    public void setBilReorderQuantity(double BIL_RRDR_QTY) {

        this.bilReorderQuantity = BIL_RRDR_QTY;
    }

    public Integer getBilCoaGlSalesAccount() {

        return bilCoaGlSalesAccount;
    }

    public void setBilCoaGlSalesAccount(Integer BIL_COA_GL_SLS_ACCNT) {

        this.bilCoaGlSalesAccount = BIL_COA_GL_SLS_ACCNT;
    }

    public Integer getBilCoaGlInventoryAccount() {

        return bilCoaGlInventoryAccount;
    }

    public void setBilCoaGlInventoryAccount(Integer BIL_COA_GL_INVNTRY_ACCNT) {

        this.bilCoaGlInventoryAccount = BIL_COA_GL_INVNTRY_ACCNT;
    }

    public Integer getBilCoaGlCostOfSalesAccount() {

        return bilCoaGlCostOfSalesAccount;
    }

    public void setBilCoaGlCostOfSalesAccount(Integer BIL_COA_GL_CST_OF_SLS_ACCNT) {

        this.bilCoaGlCostOfSalesAccount = BIL_COA_GL_CST_OF_SLS_ACCNT;
    }

    public Integer getBilCoaGlWipAccount() {

        return bilCoaGlWipAccount;
    }

    public void setBilCoaGlWipAccount(Integer BIL_COA_GL_WIP_ACCNT) {

        this.bilCoaGlWipAccount = BIL_COA_GL_WIP_ACCNT;
    }

    public Integer getBilCoaGlAccruedInventoryAccount() {

        return bilCoaGlAccruedInventoryAccount;
    }

    public void setBilCoaGlAccruedInventoryAccount(Integer BIL_COA_GL_ACCRD_INVNTRY_ACCNT) {

        this.bilCoaGlAccruedInventoryAccount = BIL_COA_GL_ACCRD_INVNTRY_ACCNT;
    }

    public Integer getBilCoaGlSalesReturnAccount() {

        return bilCoaGlSalesReturnAccount;
    }

    public void setBilCoaGlSalesReturnAccount(Integer BIL_COA_GL_SLS_RTRN_ACCNT) {

        this.bilCoaGlSalesReturnAccount = BIL_COA_GL_SLS_RTRN_ACCNT;
    }

    public Integer getBilCoaGlExpenseAccount() {

        return bilCoaGlExpenseAccount;
    }

    public void setBilCoaGlExpenseAccount(Integer BIL_COA_GL_EXPNS_ACCNT) {

        this.bilCoaGlExpenseAccount = BIL_COA_GL_EXPNS_ACCNT;
    }

    public byte getBilSubjectToCommission() {

        return bilSubjectToCommission;
    }

    public void setBilSubjectToCommission(byte BIL_SBJCT_TO_CMMSSN) {

        this.bilSubjectToCommission = BIL_SBJCT_TO_CMMSSN;
    }

    public double getBilHist1Sales() {

        return bilHist1Sales;
    }

    public void setBilHist1Sales(double BIL_HST1_SLS) {

        this.bilHist1Sales = BIL_HST1_SLS;
    }

    public double getBilHist2Sales() {

        return bilHist2Sales;
    }

    public void setBilHist2Sales(double BIL_HST2_SLS) {

        this.bilHist2Sales = BIL_HST2_SLS;
    }

    public double getBilProjectedSales() {

        return bilProjectedSales;
    }

    public void setBilProjectedSales(double BIL_PRJ_SLS) {

        this.bilProjectedSales = BIL_PRJ_SLS;
    }

    public double getBilDeliveryTime() {

        return bilDeliveryTime;
    }

    public void setBilDeliveryTime(double BIL_DLVRY_TME) {

        this.bilDeliveryTime = BIL_DLVRY_TME;
    }

    public double getBilDeliveryBuffer() {

        return bilDeliveryBuffer;
    }

    public void setBilDeliveryBuffer(double BIL_DLVRY_BUFF) {

        this.bilDeliveryBuffer = BIL_DLVRY_BUFF;
    }

    public double getBilOrderPerYear() {

        return bilOrderPerYear;
    }

    public void setBilOrderPerYear(double BIL_ORDR_PR_YR) {

        this.bilOrderPerYear = BIL_ORDR_PR_YR;
    }

    public char getBilItemDownloadStatus() {

        return bilItemDownloadStatus;
    }

    public void setBilItemDownloadStatus(char BIL_DS_ITM) {

        this.bilItemDownloadStatus = BIL_DS_ITM;
    }

    public char getBilLocationDownloadStatus() {

        return bilLocationDownloadStatus;
    }

    public void setBilLocationDownloadStatus(char BIL_DS_LOC) {

        this.bilLocationDownloadStatus = BIL_DS_LOC;
    }

    public char getBilItemLocationDownloadStatus() {

        return bilItemLocationDownloadStatus;
    }

    public void setBilItemLocationDownloadStatus(char BIL_DS_IL) {

        this.bilItemLocationDownloadStatus = BIL_DS_IL;
    }

    public Integer getBilAdCompany() {

        return bilAdCompany;
    }

    public void setBilAdCompany(Integer BIL_AD_CMPNY) {

        this.bilAdCompany = BIL_AD_CMPNY;
    }

    public LocalAdBranch getAdBranch() {

        return adBranch;
    }

    public void setAdBranch(LocalAdBranch adBranch) {

        this.adBranch = adBranch;
    }

    public LocalInvItemLocation getInvItemLocation() {

        return invItemLocation;
    }

    public void setInvItemLocation(LocalInvItemLocation invItemLocation) {

        this.invItemLocation = invItemLocation;
    }

}