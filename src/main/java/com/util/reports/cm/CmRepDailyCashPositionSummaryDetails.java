/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

/*
 * Created on Jan 23, 2006
 *
 */
package com.util.reports.cm;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Farrah S. Garing 
 *
 */
public class CmRepDailyCashPositionSummaryDetails {
	
	private Date DCPS_DT1;
	private Date DCPS_DT2;
	private Date DCPS_DT3;
	private Date DCPS_DT4;
	private Date DCPS_DT5;
	private Date DCPS_DT6;
	private Date DCPS_DT7;

	private double DCPS_BGNNNG_BLNC1;
	private double DCPS_BGNNNG_BLNC2;
	private double DCPS_BGNNNG_BLNC3;
	private double DCPS_BGNNNG_BLNC4;
	private double DCPS_BGNNNG_BLNC5;
	private double DCPS_BGNNNG_BLNC6;
	private double DCPS_BGNNNG_BLNC7;
	
	private double DCPS_AVLBL_CSH_BLNC1;
	private double DCPS_AVLBL_CSH_BLNC2;
	private double DCPS_AVLBL_CSH_BLNC3;
	private double DCPS_AVLBL_CSH_BLNC4;
	private double DCPS_AVLBL_CSH_BLNC5;
	private double DCPS_AVLBL_CSH_BLNC6;
	private double DCPS_AVLBL_CSH_BLNC7;
	
	ArrayList dcpsAddList = new ArrayList();
	ArrayList dcpsLessList = new ArrayList();
	
	public Date getDcpsDate1(){
		
		return DCPS_DT1;
		
	}
	
	public void setDcpsDate1(Date DCPS_DT1){
		
		this.DCPS_DT1 = DCPS_DT1;
		
	}
	
	public Date getDcpsDate2(){
		
		return DCPS_DT2;
		
	}
	
	public void setDcpsDate2(Date DCPS_DT2){
		
		this.DCPS_DT2 = DCPS_DT2;
		
	}
	
	public Date getDcpsDate3(){
		
		return DCPS_DT3;

	}
	
	public void setDcpsDate3(Date DCPS_DT3){
		
		this.DCPS_DT3 = DCPS_DT3;

	}

	public Date getDcpsDate4(){
		
		return DCPS_DT4;		
	}
	
	public void setDcpsDate4(Date DCPS_DT4){
		
		this.DCPS_DT4 = DCPS_DT4;		
	}

	public Date getDcpsDate5(){

		return DCPS_DT5;

	}

	public void setDcpsDate5(Date DCPS_DT5){

		this.DCPS_DT5 = DCPS_DT5;

	}
	
	public Date getDcpsDate6(){
		

		return DCPS_DT6;
		
	}

	public void setDcpsDate6(Date DCPS_DT6){

		this.DCPS_DT6 = DCPS_DT6;
		
	}
	
	public Date getDcpsDate7(){
		return DCPS_DT7;
		
	}

	public void setDcpsDate7(Date DCPS_DT7){
		
		this.DCPS_DT7 = DCPS_DT7;
		
	}
	
	public double getDcpsBeginningBalance1(){
		
		return DCPS_BGNNNG_BLNC1;
		
	}
	
	public void setDcpsBeginningBalance1(double DCPS_BGNNNG_BLNC1){
		
		this.DCPS_BGNNNG_BLNC1 = DCPS_BGNNNG_BLNC1;
		
	}
	
	public double getDcpsBeginningBalance2(){
		
		return DCPS_BGNNNG_BLNC2;
		
	}
	
	public void setDcpsBeginningBalance2(double DCPS_BGNNNG_BLNC2){
		
		this.DCPS_BGNNNG_BLNC2 = DCPS_BGNNNG_BLNC2;
		
	}
	
	public double getDcpsBeginningBalance3(){
		
		return DCPS_BGNNNG_BLNC3;

	}
	
	public void setDcpsBeginningBalance3(double DCPS_BGNNNG_BLNC3){
		
		this.DCPS_BGNNNG_BLNC3 = DCPS_BGNNNG_BLNC3;

	}

	public double getDcpsBeginningBalance4(){
		
		return DCPS_BGNNNG_BLNC4;		
	}
	
	public void setDcpsBeginningBalance4(double DCPS_BGNNNG_BLNC4){
		
		this.DCPS_BGNNNG_BLNC4 = DCPS_BGNNNG_BLNC4;		
	}

	public double getDcpsBeginningBalance5(){

		return DCPS_BGNNNG_BLNC5;

	}

	public void setDcpsBeginningBalance5(double DCPS_BGNNNG_BLNC5){

		this.DCPS_BGNNNG_BLNC5 = DCPS_BGNNNG_BLNC5;

	}
	
	public double getDcpsBeginningBalance6(){
		

		return DCPS_BGNNNG_BLNC6;
		
	}

	public void setDcpsBeginningBalance6(double DCPS_BGNNNG_BLNC6){

		this.DCPS_BGNNNG_BLNC6 = DCPS_BGNNNG_BLNC6;
		
	}
	
	public double getDcpsBeginningBalance7(){
		return DCPS_BGNNNG_BLNC7;
		
	}

	public void setDcpsBeginningBalance7(double DCPS_BGNNNG_BLNC7){
		
		this.DCPS_BGNNNG_BLNC7 = DCPS_BGNNNG_BLNC7;
		
	}

	public double getDcpsAvailableCashBalance1(){
		
		return DCPS_AVLBL_CSH_BLNC1;
		
	}
	
	public void setDcpsAvailableCashBalance1(double DCPS_AVLBL_CSH_BLNC1){
		
		this.DCPS_AVLBL_CSH_BLNC1 = DCPS_AVLBL_CSH_BLNC1;
		
	}
	
	public double getDcpsAvailableCashBalance2(){
		
		return DCPS_AVLBL_CSH_BLNC2;
		
	}
	
	public void setDcpsAvailableCashBalance2(double DCPS_AVLBL_CSH_BLNC2){
		
		this.DCPS_AVLBL_CSH_BLNC2 = DCPS_AVLBL_CSH_BLNC2;
		
	}
	
	public double getDcpsAvailableCashBalance3(){
		
		return DCPS_AVLBL_CSH_BLNC3;

	}
	
	public void setDcpsAvailableCashBalance3(double DCPS_AVLBL_CSH_BLNC3){
		
		this.DCPS_AVLBL_CSH_BLNC3 = DCPS_AVLBL_CSH_BLNC3;

	}

	public double getDcpsAvailableCashBalance4(){
		
		return DCPS_AVLBL_CSH_BLNC4;		
	}
	
	public void setDcpsAvailableCashBalance4(double DCPS_AVLBL_CSH_BLNC4){
		
		this.DCPS_AVLBL_CSH_BLNC4 = DCPS_AVLBL_CSH_BLNC4;		
	}

	public double getDcpsAvailableCashBalance5(){

		return DCPS_AVLBL_CSH_BLNC5;

	}

	public void setDcpsAvailableCashBalance5(double DCPS_AVLBL_CSH_BLNC5){

		this.DCPS_AVLBL_CSH_BLNC5 = DCPS_AVLBL_CSH_BLNC5;

	}
	
	public double getDcpsAvailableCashBalance6(){
		

		return DCPS_AVLBL_CSH_BLNC6;
		
	}

	public void setDcpsAvailableCashBalance6(double DCPS_AVLBL_CSH_BLNC6){

		this.DCPS_AVLBL_CSH_BLNC6 = DCPS_AVLBL_CSH_BLNC6;
		
	}
	
	public double getDcpsAvailableCashBalance7(){
		return DCPS_AVLBL_CSH_BLNC7;
		
	}

	public void setDcpsAvailableCashBalance7(double DCPS_AVLBL_CSH_BLNC7){
		
		this.DCPS_AVLBL_CSH_BLNC7 = DCPS_AVLBL_CSH_BLNC7;
		
	}
	
	public ArrayList getDcpsAddList() {
		
		return dcpsAddList;
		
	}
	
	public void setDcpsAddList(ArrayList dcpsAddList) {
		
		this.dcpsAddList = dcpsAddList;
		
	} 

	public ArrayList getDcpsLessList() {
		
		return dcpsLessList;
		
	}
	
	public void setDcpsLessList(ArrayList dcpsLessList) {
		
		this.dcpsLessList = dcpsLessList;
		
	} 
	
}//CmRepDailyCashPositionSummaryDetails