package com.ejb.restfulapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OfsApiResponse {

	private String status;
	private String documentNumber;
	private List<String> errorList = new ArrayList<>();
	private List<String> smlNames = new ArrayList<>();
	private List<String> branchNames = new ArrayList<>();
	private String code;
	private String message;
	private String erpCustomerCode;

	public List<String> getBranchNames() {
		return branchNames;
	}

	public void setBranchNames(List<String> branchNames) {
		this.branchNames = branchNames;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

	public List<String> getSmlNames() {
		return smlNames;
	}

	public void setSmlNames(List<String> smlNames) {
		this.smlNames = smlNames;
	}
	
	public String getErpCustomerCode() {
		return erpCustomerCode;
	}

	public void setErpCustomerCode(String erpCustomerCode) {
		this.erpCustomerCode = erpCustomerCode;
	}
}