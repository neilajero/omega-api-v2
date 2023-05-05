package com.ejb.restfulapi.ar.models;

import com.ejb.restfulapi.OfsApiRequest;

import java.io.Serializable;
import java.util.List;

public class InvoiceMemoLineRequest extends OfsApiRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String customerCode;
    private String invoiceDate;
    private String currency;
    private String taxCode;
    private String withholdingTaxCode;
    private String paymentTerm;
    private List<MemolineDetails> memoLineDetails;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public void setWithholdingTaxCode(String withholdingTaxCode) {
        this.withholdingTaxCode = withholdingTaxCode;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public List<MemolineDetails> getMemoLineDetails() {
        return memoLineDetails;
    }

    public void setMemoLineDetails(List<MemolineDetails> memoLineDetails) {
        this.memoLineDetails = memoLineDetails;
    }
}