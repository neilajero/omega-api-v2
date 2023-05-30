package com.ejb.restfulapi.sync.ad.models;

public class BankAccountSyncRequest {

    private String branchCode;
    private String companyCode;

    public String getBranchCode() {

        return branchCode;
    }

    public void setBranchCode(String branchCode) {

        this.branchCode = branchCode;
    }

    public String getCompanyCode() {

        return companyCode;
    }

    public void setCompanyCode(String companyCode) {

        this.companyCode = companyCode;
    }

}