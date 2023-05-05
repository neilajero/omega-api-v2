package com.ejb.restfulapi.ar.models;

import java.io.Serializable;
import java.util.List;

import com.ejb.restfulapi.OfsApiRequest;

public class ReceiptRequest extends OfsApiRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String receiptDate;
	private Double applyAmount; // This value contains total apply amount which currently ignored by Omega ERP
	private String customerCode;
	private String currency;
	private String adjustmentReceiptNumber; // For receipt reversal
	private List<LineItemRequest> receiptItems;
	private List<ReceiptDetails> receiptDetails;

	private List<MemolineDetails> memoLineDetails;

	public String getCurrency() { return currency; }
	public void setCurrency(String currency) { this.currency = currency; }

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public Double getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(Double applyAmount) {
		this.applyAmount = applyAmount;
	}

	public String getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}

	public String getAdjustmentReceiptNumber() {
		return adjustmentReceiptNumber;
	}

	public void setAdjustmentReceiptNumber(String adjustmentReceiptNumber) { this.adjustmentReceiptNumber = adjustmentReceiptNumber; }

	public List<LineItemRequest> getReceiptItems() {
		return receiptItems;
	}

	public void setReceiptItems(List<LineItemRequest> receiptItems) {
		this.receiptItems = receiptItems;
	}

	public List<ReceiptDetails> getReceiptDetails() {
		return receiptDetails;
	}

	public void setReceiptDetails(List<ReceiptDetails> receiptDetails) {
		this.receiptDetails = receiptDetails;
	}

	public List<MemolineDetails> getMemoLineDetails() {
		return memoLineDetails;
	}

	public void setMemoLineDetails(List<MemolineDetails> memoLineDetails)
	{
		this.memoLineDetails = memoLineDetails;
	}

}