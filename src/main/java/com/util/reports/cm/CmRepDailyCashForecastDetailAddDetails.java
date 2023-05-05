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
public class CmRepDailyCashForecastDetailAddDetails {
	
	private String DCFDA_CSTMR;
	private String DCFDA_DSCRPTN;
	private String DCFDA_INVC_NMBR;
	private Date DCFDA_DT_TRNSCTN;

	private double DCFDA_AMNT1;
	private double DCFDA_AMNT2;
	private double DCFDA_AMNT3;
	private double DCFDA_AMNT4;
	private double DCFDA_AMNT5;
	private double DCFDA_AMNT6;
	private double DCFDA_AMNT7;

	public Date getDcfdaDateTransaction(){
		
		return DCFDA_DT_TRNSCTN;
		
	}
	
	public void setDcfdaDateTransaction(Date DCFDA_DT_TRNSCTN){
		
		this.DCFDA_DT_TRNSCTN = DCFDA_DT_TRNSCTN;
		
	}
	
	public String getDcfdaCustomer(){
		
		return DCFDA_CSTMR;
		
	}
	
	public void setDcfdaCustomer(String DCFDA_CSTMR){
		
		this.DCFDA_CSTMR = DCFDA_CSTMR;
		
	}
	
	public String getDcfdaDescription(){
		
		return DCFDA_DSCRPTN;

	}
	
	public void setDcfdaDescription(String DCFDA_DSCRPTN){
		
		this.DCFDA_DSCRPTN = DCFDA_DSCRPTN;

	}

	public String getDcfdaInvoiceNumber(){
		
		return DCFDA_INVC_NMBR;		
	}
	
	public void setDcfdaInvoiceNumber(String DCFDA_INVC_NMBR){
		
		this.DCFDA_INVC_NMBR = DCFDA_INVC_NMBR;		
	}

	public double getDcfdaAmount1(){
		
		return DCFDA_AMNT1;
		
	}
	
	public void setDcfdaAmount1(double DCFDA_AMNT1){
		
		this.DCFDA_AMNT1 = DCFDA_AMNT1;
		
	}
	
	public double getDcfdaAmount2(){
		
		return DCFDA_AMNT2;
		
	}
	
	public void setDcfdaAmount2(double DCFDA_AMNT2){
		
		this.DCFDA_AMNT2 = DCFDA_AMNT2;
		
	}
	
	public double getDcfdaAmount3(){
		
		return DCFDA_AMNT3;

	}
	
	public void setDcfdaAmount3(double DCFDA_AMNT3){
		
		this.DCFDA_AMNT3 = DCFDA_AMNT3;

	}

	public double getDcfdaAmount4(){
		
		return DCFDA_AMNT4;		
	}
	
	public void setDcfdaAmount4(double DCFDA_AMNT4){
		
		this.DCFDA_AMNT4 = DCFDA_AMNT4;		
	}

	public double getDcfdaAmount5(){

		return DCFDA_AMNT5;

	}

	public void setDcfdaAmount5(double DCFDA_AMNT5){

		this.DCFDA_AMNT5 = DCFDA_AMNT5;

	}
	
	public double getDcfdaAmount6(){
		

		return DCFDA_AMNT6;
		
	}

	public void setDcfdaAmount6(double DCFDA_AMNT6){

		this.DCFDA_AMNT6 = DCFDA_AMNT6;
		
	}
	
	public double getDcfdaAmount7(){
		return DCFDA_AMNT7;
		
	}

	public void setDcfdaAmount7(double DCFDA_AMNT7){
		
		this.DCFDA_AMNT7 = DCFDA_AMNT7;
		
	}

}//CmRepDailyCashForecastDetailAddDetails