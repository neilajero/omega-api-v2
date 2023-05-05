package com.ejb.restfulapi;

import java.io.Serializable;

public class OfsApiRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String companyCode;
	private String username;
	private String branchCodeFrom;
	private String branchCode;
	private String branchName;
	private String referenceNumber;
	private String description;
	private String transactionType;
	private String transactionDate;
	
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

	public String getBranchCodeFrom() { return branchCodeFrom; }

	public void setBranchCodeFrom(String branchCodeFrom) {
		this.branchCodeFrom = branchCodeFrom;
	}

	public String getBranchCode() { return branchCode; }

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() { return branchName; }

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	
	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
}