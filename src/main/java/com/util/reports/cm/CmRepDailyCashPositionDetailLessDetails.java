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
public class CmRepDailyCashPositionDetailLessDetails {
	
	private String DCPDL_SPPLR;
	private String DCPDL_DSCRPTN;
	private String DCPDL_CHCK_NMBR;
	private Date DCPDL_DT_TRNSCTN;

	private double DCPDL_AMNT1;
	private double DCPDL_AMNT2;
	private double DCPDL_AMNT3;
	private double DCPDL_AMNT4;
	private double DCPDL_AMNT5;
	private double DCPDL_AMNT6;
	private double DCPDL_AMNT7;
	
	public Date getDcpdlDateTransaction(){
		
		return DCPDL_DT_TRNSCTN;
		
	}
	
	public void setDcpdlDateTransaction(Date DCPDL_DT_TRNSCTN){
		
		this.DCPDL_DT_TRNSCTN = DCPDL_DT_TRNSCTN;
		
	}
	
	public String getDcpdlSupplier(){
		
		return DCPDL_SPPLR;
		
	}
	
	public void setDcpdlSupplier(String DCPDL_SPPLR){
		
		this.DCPDL_SPPLR = DCPDL_SPPLR;
		
	}
	
	public String getDcpdlDescription(){
		
		return DCPDL_DSCRPTN;

	}
	
	public void setDcpdlDescription(String DCPDL_DSCRPTN){
		
		this.DCPDL_DSCRPTN = DCPDL_DSCRPTN;

	}

	public String getDcpdlCheckNumber(){
		
		return DCPDL_CHCK_NMBR;		
	}
	
	public void setDcpdlCheckNumber(String DCPDL_CHCK_NMBR){
		
		this.DCPDL_CHCK_NMBR = DCPDL_CHCK_NMBR;		
	}

	public double getDcpdlAmount1(){
		
		return DCPDL_AMNT1;
		
	}
	
	public void setDcpdlAmount1(double DCPDL_AMNT1){
		
		this.DCPDL_AMNT1 = DCPDL_AMNT1;
		
	}
	
	public double getDcpdlAmount2(){
		
		return DCPDL_AMNT2;
		
	}
	
	public void setDcpdlAmount2(double DCPDL_AMNT2){
		
		this.DCPDL_AMNT2 = DCPDL_AMNT2;
		
	}
	
	public double getDcpdlAmount3(){
		
		return DCPDL_AMNT3;

	}
	
	public void setDcpdlAmount3(double DCPDL_AMNT3){
		
		this.DCPDL_AMNT3 = DCPDL_AMNT3;

	}

	public double getDcpdlAmount4(){
		
		return DCPDL_AMNT4;		
	}
	
	public void setDcpdlAmount4(double DCPDL_AMNT4){
		
		this.DCPDL_AMNT4 = DCPDL_AMNT4;		
	}

	public double getDcpdlAmount5(){

		return DCPDL_AMNT5;

	}

	public void setDcpdlAmount5(double DCPDL_AMNT5){

		this.DCPDL_AMNT5 = DCPDL_AMNT5;

	}
	
	public double getDcpdlAmount6(){
		

		return DCPDL_AMNT6;
		
	}

	public void setDcpdlAmount6(double DCPDL_AMNT6){

		this.DCPDL_AMNT6 = DCPDL_AMNT6;
		
	}
	
	public double getDcpdlAmount7(){
		return DCPDL_AMNT7;
		
	}

	public void setDcpdlAmount7(double DCPDL_AMNT7){
		
		this.DCPDL_AMNT7 = DCPDL_AMNT7;
		
	}

}//CmRepDailyCashPositionDetailLessDetails