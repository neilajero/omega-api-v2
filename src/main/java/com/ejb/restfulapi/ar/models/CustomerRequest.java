package com.ejb.restfulapi.ar.models;

import java.io.Serializable;
import com.ejb.restfulapi.OfsApiRequest;

public class CustomerRequest extends OfsApiRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String customerCode;
	private String customerName;
	private String customerType;
	private String dealPrice;
	private String bankAccountName;

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	
	public String getDealPrice() {
		return dealPrice;
	}

	public void setDealPrice(String dealPrice) {
		this.dealPrice = dealPrice;
	}

	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}
}