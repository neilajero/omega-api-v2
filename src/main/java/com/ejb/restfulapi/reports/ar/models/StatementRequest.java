package com.ejb.restfulapi.reports.ar.models;

import java.io.Serializable;
import java.util.List;

public class StatementRequest implements Serializable {

    private String customerCode;
    private String companyCode;
    private String username;
    private List<String> branchCodes;
    private String branchCode;
    private String cutOfDate;
    private String dateFrom;
    private String dateTo;

    public String getDateFrom() {

        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {

        this.dateFrom = dateFrom;
    }

    public String getDateTo() {

        return dateTo;
    }

    public void setDateTo(String dateTo) {

        this.dateTo = dateTo;
    }

    public String getBranchCode() {

        return branchCode;
    }

    public void setBranchCode(String branchCode) {

        this.branchCode = branchCode;
    }

    public String getCutOfDate() {

        return cutOfDate;
    }

    public void setCutOfDate(String cutOfDate) {

        this.cutOfDate = cutOfDate;
    }

    public List<String> getBranchCodes() {

        return branchCodes;
    }

    public void setBranchCodes(List<String> branchCodes) {

        this.branchCodes = branchCodes;
    }

    public String getCustomerCode() {

        return customerCode;
    }

    public void setCustomerCode(String customerCode) {

        this.customerCode = customerCode;
    }

    public String getCompanyCode() {

        return companyCode;
    }

    public void setCompanyCode(String companyCode) {

        this.companyCode = companyCode;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

}