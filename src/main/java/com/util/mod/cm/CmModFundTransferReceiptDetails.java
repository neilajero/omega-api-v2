/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.cm;

import com.util.cm.CmFundTransferReceiptDetails;

import java.util.Comparator;
import java.util.Date;


public class CmModFundTransferReceiptDetails extends CmFundTransferReceiptDetails implements java.io.Serializable {
	
	private Date RCT_DT;
	private String RCT_NMBR;
	private double TTL_AMNT;
	private double AMNT_UNDPSTD;
	private String orderBy;
		
	public CmModFundTransferReceiptDetails() {
    }
	
	public CmModFundTransferReceiptDetails(Integer FTR_CODE, Date RCT_DT, double FTR_AMNT_DPST, String RCT_NMBR,
			double TTL_AMNT, Integer FTR_AD_CMPNY) {
		
	
		
		setFtrReceiptDate(RCT_DT);
		setFtrReceiptNumber(RCT_NMBR);
		setFtrTotalAmount(TTL_AMNT);
		setFtrAmountUndeposited(AMNT_UNDPSTD);

	}
	

	public Date getFtrReceiptDate(){
		
		return this.RCT_DT;
		
	}
	
	public void setFtrReceiptDate(Date RCT_DT){
		
		this.RCT_DT = RCT_DT;
		
	}
	
	public String getFtrReceiptNumber(){
		
		return this.RCT_NMBR;
		
	}
	
	public void setFtrReceiptNumber(String RCT_NMBR){
		
		this.RCT_NMBR = RCT_NMBR;
		
	}
	
	public double getFtrTotalAmount(){
		
		return this.TTL_AMNT;
		
	}
	
	public void setFtrTotalAmount(double TTL_AMNT){
		
		this.TTL_AMNT = TTL_AMNT;
		
	}
	
	public double getFtrAmountUndeposited(){
		
		return this.AMNT_UNDPSTD;
		
	}
	
	public void setFtrAmountUndeposited(double AMNT_UNDPSTD){
		
		this.AMNT_UNDPSTD = AMNT_UNDPSTD;
		
	}
	
	public String getOrderBy() {
		
		return this.orderBy;
		
	}
	
	public void setOrderBy(String orderBy) {
		
		this.orderBy = orderBy;
		
	}

	public static Comparator NoGroupComparator = (FT, anotherFT) -> {

        Date FTR_DT1 = ((CmModFundTransferReceiptDetails) FT).getFtrReceiptDate();
        String FTR_RCPT_NMBR1 = ((CmModFundTransferReceiptDetails) FT).getFtrReceiptNumber();

        Date FTR_DT2 = ((CmModFundTransferReceiptDetails) anotherFT).getFtrReceiptDate();
        String FTR_RCPT_NMBR2 = ((CmModFundTransferReceiptDetails) anotherFT).getFtrReceiptNumber();

        String ORDER_BY = ((CmModFundTransferReceiptDetails) FT).getOrderBy();

        if(ORDER_BY.equals("DATE") && !(FTR_DT1.equals(FTR_DT2))){

            return FTR_DT1.compareTo(FTR_DT2);

        } else {

            return FTR_RCPT_NMBR1.compareTo(FTR_RCPT_NMBR2);

        }

    };

}