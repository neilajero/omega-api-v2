package com.ejb.restfulapi.sync.gl.models;

public class GlCurrencySyncRequest {

    private String companyCode;
    private String branchCode;

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