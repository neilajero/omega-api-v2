package com.ejb.restfulapi.ar.models;

import java.io.Serializable;

import com.ejb.restfulapi.OfsApiRequest;

public class InvoicePostpaidRequest extends OfsApiRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private double applyAmount;
	private String customerCode;
	private String invoiceDate;
	private String invoiceType;

	public double getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(double applyAmount) {
		this.applyAmount = applyAmount;
	}
	
	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	
	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
}
