/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;

import com.util.ar.ArInvoicePaymentScheduleDetails;

import java.util.Date;

public class ArModInvoicePaymentScheduleDetails extends ArInvoicePaymentScheduleDetails implements java.io.Serializable {

    private String IPS_INV_NMBR;
    private String IPS_AR_CSTMR_CODE;
    private Date IPS_INV_DT;
    private String IPS_INV_RFRNC_NMBR;
    private String IPS_INV_FC_NM;
    private double IPS_AI_APPLY_AMNT;
    private double IPS_AI_PNLTY_APPLY_AMNT;
    private double IPS_AI_CRDTBL_W_TX;
    private double IPS_AI_DSCNT_AMNT;
    private double IPS_AI_REBATE;
    private boolean IPS_AI_APPLY_RBT;
    private int IPS_AD_BRANCH;

    public ArModInvoicePaymentScheduleDetails() { }

    public String getIpsInvNumber() {
        return IPS_INV_NMBR;
    }

    public void setIpsInvNumber(String IPS_INV_NMBR) {
        this.IPS_INV_NMBR = IPS_INV_NMBR;
    }

    public String getIpsArCustomerCode() {
        return IPS_AR_CSTMR_CODE;
    }

    public void setIpsArCustomerCode(String IPS_AR_CSTMR_CODE) {
        this.IPS_AR_CSTMR_CODE = IPS_AR_CSTMR_CODE;
    }

    public Date getIpsInvDate() {
        return IPS_INV_DT;
    }

    public void setIpsInvDate(Date IPS_INV_DT) {
        this.IPS_INV_DT = IPS_INV_DT;
    }

    public String getIpsInvReferenceNumber() {
        return IPS_INV_RFRNC_NMBR;
    }

    public void setIpsInvReferenceNumber(String IPS_INV_RFRNC_NMBR) {
        this.IPS_INV_RFRNC_NMBR = IPS_INV_RFRNC_NMBR;
    }

    public String getIpsInvFcName() {
        return IPS_INV_FC_NM;
    }

    public void setIpsInvFcName(String IPS_INV_FC_NM) {
        this.IPS_INV_FC_NM = IPS_INV_FC_NM;
    }

    public double getIpsAiApplyAmount() {
        return IPS_AI_APPLY_AMNT;
    }

    public void setIpsAiApplyAmount(double IPS_AI_APPLY_AMNT) {
        this.IPS_AI_APPLY_AMNT = IPS_AI_APPLY_AMNT;
    }

    public double getIpsAiPenaltyApplyAmount() {
        return IPS_AI_PNLTY_APPLY_AMNT;
    }

    public void setIpsAiPenaltyApplyAmount(double IPS_AI_PNLTY_APPLY_AMNT) {
        this.IPS_AI_PNLTY_APPLY_AMNT = IPS_AI_PNLTY_APPLY_AMNT;
    }

    public double getIpsAiCreditableWTax() {
        return IPS_AI_CRDTBL_W_TX;
    }

    public void setIpsAiCreditableWTax(double IPS_AI_CRDTBL_W_TX) {
        this.IPS_AI_CRDTBL_W_TX = IPS_AI_CRDTBL_W_TX;
    }

    public double getIpsAiDiscountAmount() {
        return IPS_AI_DSCNT_AMNT;
    }

    public void setIpsAiDiscountAmount(double IPS_AI_DSCNT_AMNT) {
        this.IPS_AI_DSCNT_AMNT = IPS_AI_DSCNT_AMNT;
    }

    public double getIpsAiRebate() {
        return IPS_AI_REBATE;
    }

    public void setIpsAiRebate(double IPS_AI_REBATE) {
        this.IPS_AI_REBATE = IPS_AI_REBATE;
    }

    public boolean getIpsAiApplyRebate() {
        return IPS_AI_APPLY_RBT;
    }

    public void setIpsAiApplyRebate(boolean IPS_AI_APPLY_RBT) {
        this.IPS_AI_APPLY_RBT = IPS_AI_APPLY_RBT;
    }

    public int getIpsAdBranch() {
        return IPS_AD_BRANCH;
    }

    public void setIpsAdBranch(int IPS_AD_BRANCH) {
        this.IPS_AD_BRANCH = IPS_AD_BRANCH;
    }


} // ArModInvoicePaymentScheduleDetails class