package com.ejb.restfulapi.sync.ar.models;

public class ArInvoiceSyncRequest {

    private String companyCode;
    private String branchCode;
    private String[] invoices;
    private String[] salesOrders;

    public String[] getSalesOrders() {

        return salesOrders;
    }

    public void setSalesOrders(String[] salesOrders) {

        this.salesOrders = salesOrders;
    }

    public String[] getInvoices() {

        return invoices;
    }

    public void setInvoices(String[] invoices) {

        this.invoices = invoices;
    }

    public String getCompanyCode() {

        return companyCode;
    }

    public void setCompanyCode(String companyCode) {

        this.companyCode = companyCode;
    }

    public String getBranchCode() {

        return branchCode;
    }

    public void setBranchCode(String branchCode) {

        this.branchCode = branchCode;
    }

}