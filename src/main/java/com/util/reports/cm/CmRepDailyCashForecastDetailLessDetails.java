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
public class CmRepDailyCashForecastDetailLessDetails {
	
	private String DCFDL_SPPLR;
	private String DCFDL_DSCRPTN;
	private String DCFDL_VCHR_NMBR;
	private Date DCFDL_DT_TRNSCTN;

	private double DCFDL_AMNT1;
	private double DCFDL_AMNT2;
	private double DCFDL_AMNT3;
	private double DCFDL_AMNT4;
	private double DCFDL_AMNT5;
	private double DCFDL_AMNT6;
	private double DCFDL_AMNT7;
	
	public Date getDcfdlDateTransaction(){
		
		return DCFDL_DT_TRNSCTN;
		
	}
	
	public void setDcfdlDateTransaction(Date DCFDL_DT_TRNSCTN){
		
		this.DCFDL_DT_TRNSCTN = DCFDL_DT_TRNSCTN;
		
	}
	
	public String getDcfdlSupplier(){
		
		return DCFDL_SPPLR;
		
	}
	
	public void setDcfdlSupplier(String DCFDL_SPPLR){
		
		this.DCFDL_SPPLR = DCFDL_SPPLR;
		
	}
	
	public String getDcfdlDescription(){
		
		return DCFDL_DSCRPTN;

	}
	
	public void setDcfdlDescription(String DCFDL_DSCRPTN){
		
		this.DCFDL_DSCRPTN = DCFDL_DSCRPTN;

	}

	public String getDcfdlVoucherNumber(){
		
		return DCFDL_VCHR_NMBR;		
	}
	
	public void setDcfdlVoucherNumber(String DCFDL_VCHR_NMBR){
		
		this.DCFDL_VCHR_NMBR = DCFDL_VCHR_NMBR;		
	}

	public double getDcfdlAmount1(){
		
		return DCFDL_AMNT1;
		
	}
	
	public void setDcfdlAmount1(double DCFDL_AMNT1){
		
		this.DCFDL_AMNT1 = DCFDL_AMNT1;
		
	}
	
	public double getDcfdlAmount2(){
		
		return DCFDL_AMNT2;
		
	}
	
	public void setDcfdlAmount2(double DCFDL_AMNT2){
		
		this.DCFDL_AMNT2 = DCFDL_AMNT2;
		
	}
	
	public double getDcfdlAmount3(){
		
		return DCFDL_AMNT3;

	}
	
	public void setDcfdlAmount3(double DCFDL_AMNT3){
		
		this.DCFDL_AMNT3 = DCFDL_AMNT3;

	}

	public double getDcfdlAmount4(){
		
		return DCFDL_AMNT4;		
	}
	
	public void setDcfdlAmount4(double DCFDL_AMNT4){
		
		this.DCFDL_AMNT4 = DCFDL_AMNT4;		
	}

	public double getDcfdlAmount5(){

		return DCFDL_AMNT5;

	}

	public void setDcfdlAmount5(double DCFDL_AMNT5){

		this.DCFDL_AMNT5 = DCFDL_AMNT5;

	}
	
	public double getDcfdlAmount6(){
		

		return DCFDL_AMNT6;
		
	}

	public void setDcfdlAmount6(double DCFDL_AMNT6){

		this.DCFDL_AMNT6 = DCFDL_AMNT6;
		
	}
	
	public double getDcfdlAmount7(){
		return DCFDL_AMNT7;
		
	}

	public void setDcfdlAmount7(double DCFDL_AMNT7){
		
		this.DCFDL_AMNT7 = DCFDL_AMNT7;
		
	}

}//CmRepDailyCashForecastDetailLessDetails