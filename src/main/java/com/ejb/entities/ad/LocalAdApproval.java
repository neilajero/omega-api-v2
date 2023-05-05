package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdApproval")
@Table(name = "AD_APPRVL")
public class LocalAdApproval extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APR_CODE", nullable = false)
    private Integer aprCode;

    @Column(name = "APR_ENBL_GL_JRNL", columnDefinition = "TINYINT")
    private byte aprEnableGlJournal;

    @Column(name = "APR_ENBL_AP_VCHR", columnDefinition = "TINYINT")
    private byte aprEnableApVoucher;

    @Column(name = "APR_ENBL_AP_VCHR_DEPT", columnDefinition = "TINYINT")
    private byte aprEnableApVoucherDepartment;

    @Column(name = "APR_ENBL_AP_DBT_MMO", columnDefinition = "TINYINT")
    private byte aprEnableApDebitMemo;

    @Column(name = "APR_ENBL_AP_CHCK", columnDefinition = "TINYINT")
    private byte aprEnableApCheck;

    @Column(name = "APR_ENBL_AR_INVC", columnDefinition = "TINYINT")
    private byte aprEnableArInvoice;

    @Column(name = "APR_ENBL_AR_CRDT_MMO", columnDefinition = "TINYINT")
    private byte aprEnableArCreditMemo;

    @Column(name = "APR_ENBL_AR_RCPT", columnDefinition = "TINYINT")
    private byte aprEnableArReceipt;

    @Column(name = "APR_ENBL_CM_FND_TRNSFR", columnDefinition = "TINYINT")
    private byte aprEnableCmFundTransfer;

    @Column(name = "APR_ENBL_CM_ADJSTMNT", columnDefinition = "TINYINT")
    private byte aprEnableCmAdjustment;

    @Column(name = "APR_APPRVL_QUEUE_EXPRTN", columnDefinition = "TINYINT")
    private Integer aprApprovalQueueExpiration;

    @Column(name = "APR_ENBL_INV_ADJSTMNT", columnDefinition = "TINYINT")
    private byte aprEnableInvAdjustment;

    @Column(name = "APR_ENBL_INV_BLD", columnDefinition = "TINYINT")
    private byte aprEnableInvBuild;

    @Column(name = "APR_ENBL_INV_BLD_ORDR", columnDefinition = "TINYINT")
    private byte aprEnableInvBuildOrder;

    @Column(name = "APR_ENBL_AP_PRCHS_RQSTN", columnDefinition = "TINYINT")
    private byte aprEnableApPurReq;

    @Column(name = "APR_ENBL_AP_CNVSS", columnDefinition = "TINYINT")
    private byte aprEnableApCanvass;

    @Column(name = "APR_ENBL_INV_ADJSTMNT_RQST", columnDefinition = "TINYINT")
    private byte aprEnableInvAdjustmentRequest;

    @Column(name = "APR_ENBL_INV_STCK_TRNSFR", columnDefinition = "TINYINT")
    private byte aprEnableInvStockTransfer;

    @Column(name = "APR_ENBL_AP_PRCHS_ORDR", columnDefinition = "TINYINT")
    private byte aprEnableApPurchaseOrder;

    @Column(name = "APR_ENBL_AP_RCVNG_ITM", columnDefinition = "TINYINT")
    private byte aprEnableApReceivingItem;

    @Column(name = "APR_ENBL_INV_BRNCH_STCK_TRNSFR", columnDefinition = "TINYINT")
    private byte aprEnableInvBranchStockTransfer;

    @Column(name = "APR_ENBL_INV_BRNCH_STCK_TRNSFR_ORDR", columnDefinition = "TINYINT")
    private byte aprEnableInvBranchStockTransferOrder;

    @Column(name = "APR_ENBL_AP_CHCK_PYMNT_RQST", columnDefinition = "TINYINT")
    private byte aprEnableApCheckPaymentRequest;

    @Column(name = "APR_ENBL_AP_CHCK_PYMNT_RQST_DEPT", columnDefinition = "TINYINT")
    private byte aprEnableApCheckPaymentRequestDepartment;

    @Column(name = "APR_ENBL_AR_SLS_ORDR", columnDefinition = "TINYINT")
    private byte aprEnableArSalesOrder;

    @Column(name = "APR_ENBL_AR_CSTMR", columnDefinition = "TINYINT")
    private byte aprEnableArCustomer;

    @Column(name = "APR_AD_CMPNY", columnDefinition = "INT")
    private Integer aprAdCompany;

    public Integer getAprCode() {

        return aprCode;
    }

    public void setAprCode(Integer APR_CODE) {

        this.aprCode = APR_CODE;
    }

    public byte getAprEnableGlJournal() {

        return aprEnableGlJournal;
    }

    public void setAprEnableGlJournal(byte APR_ENBL_GL_JRNL) {

        this.aprEnableGlJournal = APR_ENBL_GL_JRNL;
    }

    public byte getAprEnableApVoucher() {

        return aprEnableApVoucher;
    }

    public void setAprEnableApVoucher(byte APR_ENBL_AP_VCHR) {

        this.aprEnableApVoucher = APR_ENBL_AP_VCHR;
    }

    public byte getAprEnableApVoucherDepartment() {

        return aprEnableApVoucherDepartment;
    }

    public void setAprEnableApVoucherDepartment(byte APR_ENBL_AP_VCHR_DEPT) {

        this.aprEnableApVoucherDepartment = APR_ENBL_AP_VCHR_DEPT;
    }

    public byte getAprEnableApDebitMemo() {

        return aprEnableApDebitMemo;
    }

    public void setAprEnableApDebitMemo(byte APR_ENBL_AP_DBT_MMO) {

        this.aprEnableApDebitMemo = APR_ENBL_AP_DBT_MMO;
    }

    public byte getAprEnableApCheck() {

        return aprEnableApCheck;
    }

    public void setAprEnableApCheck(byte APR_ENBL_AP_CHCK) {

        this.aprEnableApCheck = APR_ENBL_AP_CHCK;
    }

    public byte getAprEnableArInvoice() {

        return aprEnableArInvoice;
    }

    public void setAprEnableArInvoice(byte APR_ENBL_AR_INVC) {

        this.aprEnableArInvoice = APR_ENBL_AR_INVC;
    }

    public byte getAprEnableArCreditMemo() {

        return aprEnableArCreditMemo;
    }

    public void setAprEnableArCreditMemo(byte APR_ENBL_AR_CRDT_MMO) {

        this.aprEnableArCreditMemo = APR_ENBL_AR_CRDT_MMO;
    }

    public byte getAprEnableArReceipt() {

        return aprEnableArReceipt;
    }

    public void setAprEnableArReceipt(byte APR_ENBL_AR_RCPT) {

        this.aprEnableArReceipt = APR_ENBL_AR_RCPT;
    }

    public byte getAprEnableCmFundTransfer() {

        return aprEnableCmFundTransfer;
    }

    public void setAprEnableCmFundTransfer(byte APR_ENBL_CM_FND_TRNSFR) {

        this.aprEnableCmFundTransfer = APR_ENBL_CM_FND_TRNSFR;
    }

    public byte getAprEnableCmAdjustment() {

        return aprEnableCmAdjustment;
    }

    public void setAprEnableCmAdjustment(byte APR_ENBL_CM_ADJSTMNT) {

        this.aprEnableCmAdjustment = APR_ENBL_CM_ADJSTMNT;
    }

    public Integer getAprApprovalQueueExpiration() {

        return aprApprovalQueueExpiration;
    }

    public void setAprApprovalQueueExpiration(Integer APR_APPRVL_QUEUE_EXPRTN) {

        this.aprApprovalQueueExpiration = APR_APPRVL_QUEUE_EXPRTN;
    }

    public byte getAprEnableInvAdjustment() {

        return aprEnableInvAdjustment;
    }

    public void setAprEnableInvAdjustment(byte APR_ENBL_INV_ADJSTMNT) {

        this.aprEnableInvAdjustment = APR_ENBL_INV_ADJSTMNT;
    }

    public byte getAprEnableInvBuild() {

        return aprEnableInvBuild;
    }

    public void setAprEnableInvBuild(byte APR_ENBL_INV_BLD) {

        this.aprEnableInvBuild = APR_ENBL_INV_BLD;
    }

    public byte getAprEnableInvBuildOrder() {

        return aprEnableInvBuildOrder;
    }

    public void setAprEnableInvBuildOrder(byte APR_ENBL_INV_BLD_ORDR) {

        this.aprEnableInvBuildOrder = APR_ENBL_INV_BLD_ORDR;
    }

    public byte getAprEnableApPurReq() {

        return aprEnableApPurReq;
    }

    public void setAprEnableApPurReq(byte APR_ENBL_AP_PRCHS_RQSTN) {

        this.aprEnableApPurReq = APR_ENBL_AP_PRCHS_RQSTN;
    }

    public byte getAprEnableApCanvass() {

        return aprEnableApCanvass;
    }

    public void setAprEnableApCanvass(byte APR_ENBL_AP_CNVSS) {

        this.aprEnableApCanvass = APR_ENBL_AP_CNVSS;
    }

    public byte getAprEnableInvAdjustmentRequest() {

        return aprEnableInvAdjustmentRequest;
    }

    public void setAprEnableInvAdjustmentRequest(byte APR_ENBL_INV_ADJSTMNT_RQST) {

        this.aprEnableInvAdjustmentRequest = APR_ENBL_INV_ADJSTMNT_RQST;
    }

    public byte getAprEnableInvStockTransfer() {

        return aprEnableInvStockTransfer;
    }

    public void setAprEnableInvStockTransfer(byte APR_ENBL_INV_STCK_TRNSFR) {

        this.aprEnableInvStockTransfer = APR_ENBL_INV_STCK_TRNSFR;
    }

    public byte getAprEnableApPurchaseOrder() {

        return aprEnableApPurchaseOrder;
    }

    public void setAprEnableApPurchaseOrder(byte APR_ENBL_AP_PRCHS_ORDR) {

        this.aprEnableApPurchaseOrder = APR_ENBL_AP_PRCHS_ORDR;
    }

    public byte getAprEnableApReceivingItem() {

        return aprEnableApReceivingItem;
    }

    public void setAprEnableApReceivingItem(byte APR_ENBL_AP_RCVNG_ITM) {

        this.aprEnableApReceivingItem = APR_ENBL_AP_RCVNG_ITM;
    }

    public byte getAprEnableInvBranchStockTransfer() {

        return aprEnableInvBranchStockTransfer;
    }

    public void setAprEnableInvBranchStockTransfer(byte APR_ENBL_INV_BRNCH_STCK_TRNSFR) {

        this.aprEnableInvBranchStockTransfer = APR_ENBL_INV_BRNCH_STCK_TRNSFR;
    }

    public byte getAprEnableInvBranchStockTransferOrder() {

        return aprEnableInvBranchStockTransferOrder;
    }

    public void setAprEnableInvBranchStockTransferOrder(byte APR_ENBL_INV_BRNCH_STCK_TRNSFR_ORDR) {

        this.aprEnableInvBranchStockTransferOrder = APR_ENBL_INV_BRNCH_STCK_TRNSFR_ORDR;
    }

    public byte getAprEnableApCheckPaymentRequest() {

        return aprEnableApCheckPaymentRequest;
    }

    public void setAprEnableApCheckPaymentRequest(byte APR_ENBL_AP_CHCK_PYMNT_RQST) {

        this.aprEnableApCheckPaymentRequest = APR_ENBL_AP_CHCK_PYMNT_RQST;
    }

    public byte getAprEnableApCheckPaymentRequestDepartment() {

        return aprEnableApCheckPaymentRequestDepartment;
    }

    public void setAprEnableApCheckPaymentRequestDepartment(byte APR_ENBL_AP_CHCK_PYMNT_RQST_DEPT) {

        this.aprEnableApCheckPaymentRequestDepartment = APR_ENBL_AP_CHCK_PYMNT_RQST_DEPT;
    }

    public byte getAprEnableArSalesOrder() {

        return aprEnableArSalesOrder;
    }

    public void setAprEnableArSalesOrder(byte APR_ENBL_AR_SLS_ORDR) {

        this.aprEnableArSalesOrder = APR_ENBL_AR_SLS_ORDR;
    }

    public byte getAprEnableArCustomer() {

        return aprEnableArCustomer;
    }

    public void setAprEnableArCustomer(byte APR_ENBL_AR_CSTMR) {

        this.aprEnableArCustomer = APR_ENBL_AR_CSTMR;
    }

    public Integer getAprAdCompany() {

        return aprAdCompany;
    }

    public void setAprAdCompany(Integer APR_AD_CMPNY) {

        this.aprAdCompany = APR_AD_CMPNY;
    }

}