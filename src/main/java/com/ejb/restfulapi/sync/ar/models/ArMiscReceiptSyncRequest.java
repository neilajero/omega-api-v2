package com.ejb.restfulapi.sync.ar.models;

public class ArMiscReceiptSyncRequest {
    private String companyCode;
    private String branchCode;
    private String[] receipts;
    private String[] voidReceipts;

    public String[] getVoidReceipts() {

        return voidReceipts;
    }

    public void setVoidReceipts(String[] voidReceipts) {

        this.voidReceipts = voidReceipts;
    }

    public String[] getReceipts() {

        return receipts;
    }

    public void setReceipts(String[] receipts) {

        this.receipts = receipts;
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