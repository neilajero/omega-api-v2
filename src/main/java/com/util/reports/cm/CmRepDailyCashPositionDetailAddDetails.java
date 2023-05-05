/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

/*
 * Created on Jan 17, 2006
 *
 */
package com.util.reports.cm;

import java.util.Date;

/**
 * @author Farrah S. Garing 
 *
 */
public class CmRepDailyCashPositionDetailAddDetails {
	
	private String DCPDA_CSTMR;
	private String DCPDA_DSCRPTN;
	private String DCPDA_RCPT_NMBR;
	private Date DCPDA_DT_TRNSCTN;

	private double DCPDA_AMNT1;
	private double DCPDA_AMNT2;
	private double DCPDA_AMNT3;
	private double DCPDA_AMNT4;
	private double DCPDA_AMNT5;
	private double DCPDA_AMNT6;
	private double DCPDA_AMNT7;

	public Date getDcpdaDateTransaction(){
		
		return DCPDA_DT_TRNSCTN;
		
	}
	
	public void setDcpdaDateTransaction(Date DCPDA_DT_TRNSCTN){
		
		this.DCPDA_DT_TRNSCTN = DCPDA_DT_TRNSCTN;
		
	}
	
	public String getDcpdaCustomer(){
		
		return DCPDA_CSTMR;
		
	}
	
	public void setDcpdaCustomer(String DCPDA_CSTMR){
		
		this.DCPDA_CSTMR = DCPDA_CSTMR;
		
	}
	
	public String getDcpdaDescription(){
		
		return DCPDA_DSCRPTN;

	}
	
	public void setDcpdaDescription(String DCPDA_DSCRPTN){
		
		this.DCPDA_DSCRPTN = DCPDA_DSCRPTN;

	}

	public String getDcpdaReceiptNumber(){
		
		return DCPDA_RCPT_NMBR;		
	}
	
	public void setDcpdaReceiptNumber(String DCPDA_RCPT_NMBR){
		
		this.DCPDA_RCPT_NMBR = DCPDA_RCPT_NMBR;		
	}

	public double getDcpdaAmount1(){
		
		return DCPDA_AMNT1;
		
	}
	
	public void setDcpdaAmount1(double DCPDA_AMNT1){
		
		this.DCPDA_AMNT1 = DCPDA_AMNT1;
		
	}
	
	public double getDcpdaAmount2(){
		
		return DCPDA_AMNT2;
		
	}
	
	public void setDcpdaAmount2(double DCPDA_AMNT2){
		
		this.DCPDA_AMNT2 = DCPDA_AMNT2;
		
	}
	
	public double getDcpdaAmount3(){
		
		return DCPDA_AMNT3;

	}
	
	public void setDcpdaAmount3(double DCPDA_AMNT3){
		
		this.DCPDA_AMNT3 = DCPDA_AMNT3;

	}

	public double getDcpdaAmount4(){
		
		return DCPDA_AMNT4;		
	}
	
	public void setDcpdaAmount4(double DCPDA_AMNT4){
		
		this.DCPDA_AMNT4 = DCPDA_AMNT4;		
	}

	public double getDcpdaAmount5(){

		return DCPDA_AMNT5;

	}

	public void setDcpdaAmount5(double DCPDA_AMNT5){

		this.DCPDA_AMNT5 = DCPDA_AMNT5;

	}
	
	public double getDcpdaAmount6(){
		

		return DCPDA_AMNT6;
		
	}

	public void setDcpdaAmount6(double DCPDA_AMNT6){

		this.DCPDA_AMNT6 = DCPDA_AMNT6;
		
	}
	
	public double getDcpdaAmount7(){
		return DCPDA_AMNT7;
		
	}

	public void setDcpdaAmount7(double DCPDA_AMNT7){
		
		this.DCPDA_AMNT7 = DCPDA_AMNT7;
		
	}

}//CmRepDailyCashPositionDetailAddDetails