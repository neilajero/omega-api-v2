package com.ejb.restfulapi.ar.models;

import java.io.Serializable;
import java.util.List;

import com.ejb.restfulapi.OfsApiRequest;

public class InvoiceItemRequest extends OfsApiRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String customerCode;
	private String invoiceDate;
	private String invoiceType;
	private Double applyAmount;
	private String adjustmentAccount;
	
	private List<LineItemRequest> invoiceItems;
	
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

	public Double getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(Double applyAmount) {
		this.applyAmount = applyAmount;
	}

	public String getAdjustmentAccount() {
		return adjustmentAccount;
	}

	public void setAdjustmentAccount(String adjustmentAccount) {
		this.adjustmentAccount = adjustmentAccount;
	}
	
	public List<LineItemRequest> getInvoiceItems() {
		return invoiceItems;
	}

	public void setInvoiceItems(List<LineItemRequest> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}
}