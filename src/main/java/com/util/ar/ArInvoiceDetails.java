package com.util.ar;

import com.ejb.entities.ar.LocalArInvoice;

public class ArInvoiceDetails extends LocalArInvoice implements java.io.Serializable{

    private String paymentTerm = null;
    private String taxCode = null;
    private String withholdingTaxCode = null;
    private String currencyCode = null;
    private String customerCode = null;
    private String companyShortName = null;
    private boolean isDraft = false; // Default is draft

    public ArInvoiceDetails() { }

    public boolean getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(boolean isDraft) {
        this.isDraft = isDraft;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getWithholdingTaxCode() {
        return withholdingTaxCode;
    }

    public void setWithholdingTaxCode(String withholdingTaxCode) { this.withholdingTaxCode = withholdingTaxCode; }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }

    public String getCompanyShortName() { return companyShortName; }

    public void setCompanyShortName(String companyShortName) { this.companyShortName = companyShortName; }
}