/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.ad;

import java.util.Date;


public class AdRepTransactionSummaryDetails implements java.io.Serializable, Comparable{

   private String txlType;
   private Date txlDate;
   private String txlDocumentNumber;
   private double txlAmount;
   private String txlApprovalStatus;
   private byte txlPosted;
   private String txlCreatedBy;
   private Date txlDateCreated;
   private String txlLastModifiedBy;
   private Date txlDateLastModified;
   private String txlApprovedRejectedBy;
   private Date txlDateApprovedRejected;
   private String txlPostedBy;
   private Date txlDatePosted;
   private String txlOrderBy;
   

   public AdRepTransactionSummaryDetails() {
   }
      
   public double getTxlAmount() {
	
	return txlAmount;
	
   }

   public void setTxlAmount(double txlAmount) {
	
	this.txlAmount = txlAmount;
	
   }

	public String getTxlApprovalStatus() {
		
		return txlApprovalStatus;
	}
	
	public void setTxlApprovalStatus(String txlApprovalStatus) {
		
		this.txlApprovalStatus = txlApprovalStatus;
		
	}
	
	public String getTxlApprovedRejectedBy() {
		
		return txlApprovedRejectedBy;
		
	}
	
	public void setTxlApprovedRejectedBy(String txlApprovedRejectedBy) {
		
		this.txlApprovedRejectedBy = txlApprovedRejectedBy;
		
	}
	
	public String getTxlCreatedBy() {
		
		return txlCreatedBy;
		
	}
	
	public void setTxlCreatedBy(String txlCreatedBy) {
		
		this.txlCreatedBy = txlCreatedBy;
		
	}
	
	public Date getTxlDate() {
		
		return txlDate;
		
	}
	
	public void setTxlDate(Date txlDate) {
		
		this.txlDate = txlDate;
		
	}
	
	public Date getTxlDateApprovedRejected() {
		
		return txlDateApprovedRejected;
		
	}
	
	public void setTxlDateApprovedRejected(Date txlDateApprovedRejected) {
		
		this.txlDateApprovedRejected = txlDateApprovedRejected;
		
	}
	
	public Date getTxlDateCreated() {
		
		return txlDateCreated;
		
	}
	
	public void setTxlDateCreated(Date txlDateCreated) {
		
		this.txlDateCreated = txlDateCreated;
		
	}
	
	public Date getTxlDateLastModified() {
		
		return txlDateLastModified;
		
	}
	
	public void setTxlDateLastModified(Date txlDateLastModified) {
		
		this.txlDateLastModified = txlDateLastModified;
		
	}
	
	public Date getTxlDatePosted() {
		
		return txlDatePosted;
		
	}
	
	public void setTxlDatePosted(Date txlDatePosted) {
		
		this.txlDatePosted = txlDatePosted;
		
	}
	
	public String getTxlDocumentNumber() {
		
		return txlDocumentNumber;
		
	}
	
	public void setTxlDocumentNumber(String txlDocumentNumber) {
		
		this.txlDocumentNumber = txlDocumentNumber;
		
	}
	
	public String getTxlLastModifiedBy() {
		
		return txlLastModifiedBy;
		
	}
	
	public void setTxlLastModifiedBy(String txlLastModifiedBy) {
		
		this.txlLastModifiedBy = txlLastModifiedBy;
		
	}
	
	public byte getTxlPosted() {
		
		return txlPosted;
		
	}
	
	public void setTxlPosted(byte txlPosted) {
		
		this.txlPosted = txlPosted;
		
	}
	
	public String getTxlPostedBy() {
		
		return txlPostedBy;
		
	}
	
	public void setTxlPostedBy(String txlPostedBy) {
		
		this.txlPostedBy = txlPostedBy;
		
	}
	
	public String getTxlType() {
		
		return txlType;
		
	}
	
	public void setTxlType(String txlType) {
		
		this.txlType = txlType;
		
	}
	
	public String getTxlOrderBy() {
		
		return txlOrderBy;
		
	}
	
	public void setTxlOrderBy(String txlOrderBy) {
		
		this.txlOrderBy = txlOrderBy;
		
	}
	
	public int compareTo(Object o) {
		
		AdRepTransactionSummaryDetails details =(AdRepTransactionSummaryDetails)o;

        switch (this.txlOrderBy) {
            case "TYPE":

                return this.txlType.compareTo(details.getTxlType());

            case "TRANSACTION DATE":

                return this.txlDate.compareTo(details.getTxlDate());

            case "CREATED BY":

                return this.txlCreatedBy.compareTo(details.getTxlCreatedBy());

            case "DATE CREATED":

                return this.txlDateCreated.compareTo(details.getTxlDateCreated());

            case "LAST MODIFIED BY":

                return this.txlLastModifiedBy.compareTo(details.getTxlLastModifiedBy());

            default:

                return this.txlDateLastModified.compareTo(details.getTxlDateLastModified());

        }
				
	}
	
} // AdRepTransactionSummaryDetails class   