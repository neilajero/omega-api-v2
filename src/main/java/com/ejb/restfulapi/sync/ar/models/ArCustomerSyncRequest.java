package com.ejb.restfulapi.sync.ar.models;

public class ArCustomerSyncRequest {

    private String companyCode;
    private String customerArea;
    private String branchCode;
    private String[] customerCodes;

    public String[] getCustomerCodes() {

        return customerCodes;
    }

    public void setCustomerCodes(String[] customerCodes) {

        this.customerCodes = customerCodes;
    }

    public String getBranchCode() {

        return branchCode;
    }

    public void setBranchCode(String branchCode) {

        this.branchCode = branchCode;
    }

    public String getCustomerArea() {

        return customerArea;
    }

    public void setCustomerArea(String customerArea) {

        this.customerArea = customerArea;
    }

    public String getCompanyCode() {

        return companyCode;
    }

    public void setCompanyCode(String companyCode) {

        this.companyCode = companyCode;
    }

}