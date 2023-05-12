package com.ejb.restfulapi.reports.ar.models;

import java.util.Date;
import java.util.List;

public class StatementDetails {

    private String storedProcedure;
    private Date cutOfDate;
    private String customerBatch = "";
    private String customerCode = "";
    private boolean includePosted = false;
    private boolean includeAdvance = false;
    private boolean includeAdvanceOnly = false;
    private List<String> branchCodes;
    private String branchName;
    private Integer companyCode;
    private String companyShortName;
    private String companyName;
    private String companyAddress;
    private String companyPhone;
    private String companyFax;
    private String companyEmail;
    private String companyTinNumber;
    private String preparedBy;
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

    public String getCompanyName() {

        return companyName;
    }

    public void setCompanyName(String companyName) {

        this.companyName = companyName;
    }

    public String getCompanyAddress() {

        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {

        this.companyAddress = companyAddress;
    }

    public String getCompanyPhone() {

        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {

        this.companyPhone = companyPhone;
    }

    public String getCompanyFax() {

        return companyFax;
    }

    public void setCompanyFax(String companyFax) {

        this.companyFax = companyFax;
    }

    public String getCompanyEmail() {

        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {

        this.companyEmail = companyEmail;
    }

    public String getCompanyTinNumber() {

        return companyTinNumber;
    }

    public void setCompanyTinNumber(String companyTinNumber) {

        this.companyTinNumber = companyTinNumber;
    }

    public String getPreparedBy() {

        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {

        this.preparedBy = preparedBy;
    }

    public String getBranchName() {

        return branchName;
    }

    public void setBranchName(String branchName) {

        this.branchName = branchName;
    }

    public String getStoredProcedure() {

        return storedProcedure;
    }

    public void setStoredProcedure(String storedProcedure) {

        this.storedProcedure = storedProcedure;
    }

    public Date getCutOfDate() {

        return cutOfDate;
    }

    public void setCutOfDate(Date cutOfDate) {

        this.cutOfDate = cutOfDate;
    }

    public String getCustomerBatch() {

        return customerBatch;
    }

    public void setCustomerBatch(String customerBatch) {

        this.customerBatch = customerBatch;
    }

    public String getCustomerCode() {

        return customerCode;
    }

    public void setCustomerCode(String customerCode) {

        this.customerCode = customerCode;
    }

    public boolean isIncludePosted() {

        return includePosted;
    }

    public void setIncludePosted(boolean includePosted) {

        this.includePosted = includePosted;
    }

    public boolean isIncludeAdvance() {

        return includeAdvance;
    }

    public void setIncludeAdvance(boolean includeAdvance) {

        this.includeAdvance = includeAdvance;
    }

    public boolean isIncludeAdvanceOnly() {

        return includeAdvanceOnly;
    }

    public void setIncludeAdvanceOnly(boolean includeAdvanceOnly) {

        this.includeAdvanceOnly = includeAdvanceOnly;
    }

    public List<String> getBranchCodes() {

        return branchCodes;
    }

    public void setBranchCodes(List<String> branchCodes) {

        this.branchCodes = branchCodes;
    }

    public Integer getCompanyCode() {

        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {

        this.companyCode = companyCode;
    }

    public String getCompanyShortName() {

        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {

        this.companyShortName = companyShortName;
    }


}