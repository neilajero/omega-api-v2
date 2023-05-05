package com.ejb.restfulapi.ad.models;

import com.ejb.restfulapi.OfsApiRequest;

import java.util.List;

public class AmountLimitRequest extends OfsApiRequest {

    private String department;
    private Double amountLimit;
    private String andOr;
    private String documentCode;
    private String coaLineCode;
    private List<ApprovalUser> approvalUsers;

    public String getDepartment()
    {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }

    public Double getAmountLimit()
    {
        return amountLimit;
    }
    public void setAmountLimit(Double amountLimit) {
        this.amountLimit = amountLimit;
    }

    public String getAndOr() { return andOr; }
    public void setAndOr(String andOr) { this.andOr = andOr; }

    public String getDocumentCode()
    {
        return documentCode;
    }
    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getCoaLineCode()
    {
        return coaLineCode;
    }
    public void setCoaLineCode(String coaLineCode) {
        this.coaLineCode = coaLineCode;
    }

    public List<ApprovalUser> getApprovalUsers() { return approvalUsers; }
    public void setApprovalUsers(List<ApprovalUser> approvalUsers) { this.approvalUsers = approvalUsers; }

}