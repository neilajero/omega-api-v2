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
public class CmRepDailyCashForecastSummaryDetails {
	
	private Date DCFS_DT1;
	private Date DCFS_DT2;
	private Date DCFS_DT3;
	private Date DCFS_DT4;
	private Date DCFS_DT5;
	private Date DCFS_DT6;
	private Date DCFS_DT7;

	private double DCFS_BGNNNG_BLNC1;
	private double DCFS_BGNNNG_BLNC2;
	private double DCFS_BGNNNG_BLNC3;
	private double DCFS_BGNNNG_BLNC4;
	private double DCFS_BGNNNG_BLNC5;
	private double DCFS_BGNNNG_BLNC6;
	private double DCFS_BGNNNG_BLNC7;
	
	private double DCFS_AVLBL_CSH_BLNC1;
	private double DCFS_AVLBL_CSH_BLNC2;
	private double DCFS_AVLBL_CSH_BLNC3;
	private double DCFS_AVLBL_CSH_BLNC4;
	private double DCFS_AVLBL_CSH_BLNC5;
	private double DCFS_AVLBL_CSH_BLNC6;
	private double DCFS_AVLBL_CSH_BLNC7;
	
	ArrayList dcfsAddList = new ArrayList();
	ArrayList dcfsLessList = new ArrayList();
	
	public Date getDcfsDate1(){
		
		return DCFS_DT1;
		
	}
	
	public void setDcfsDate1(Date DCFS_DT1){
		
		this.DCFS_DT1 = DCFS_DT1;
		
	}
	
	public Date getDcfsDate2(){
		
		return DCFS_DT2;
		
	}
	
	public void setDcfsDate2(Date DCFS_DT2){
		
		this.DCFS_DT2 = DCFS_DT2;
		
	}
	
	public Date getDcfsDate3(){
		
		return DCFS_DT3;

	}
	
	public void setDcfsDate3(Date DCFS_DT3){
		
		this.DCFS_DT3 = DCFS_DT3;

	}

	public Date getDcfsDate4(){
		
		return DCFS_DT4;		
	}
	
	public void setDcfsDate4(Date DCFS_DT4){
		
		this.DCFS_DT4 = DCFS_DT4;		
	}

	public Date getDcfsDate5(){

		return DCFS_DT5;

	}

	public void setDcfsDate5(Date DCFS_DT5){

		this.DCFS_DT5 = DCFS_DT5;

	}
	
	public Date getDcfsDate6(){
		

		return DCFS_DT6;
		
	}

	public void setDcfsDate6(Date DCFS_DT6){

		this.DCFS_DT6 = DCFS_DT6;
		
	}
	
	public Date getDcfsDate7(){
		return DCFS_DT7;
		
	}

	public void setDcfsDate7(Date DCFS_DT7){
		
		this.DCFS_DT7 = DCFS_DT7;
		
	}
	
	public double getDcfsBeginningBalance1(){
		
		return DCFS_BGNNNG_BLNC1;
		
	}
	
	public void setDcfsBeginningBalance1(double DCFS_BGNNNG_BLNC1){
		
		this.DCFS_BGNNNG_BLNC1 = DCFS_BGNNNG_BLNC1;
		
	}
	
	public double getDcfsBeginningBalance2(){
		
		return DCFS_BGNNNG_BLNC2;
		
	}
	
	public void setDcfsBeginningBalance2(double DCFS_BGNNNG_BLNC2){
		
		this.DCFS_BGNNNG_BLNC2 = DCFS_BGNNNG_BLNC2;
		
	}
	
	public double getDcfsBeginningBalance3(){
		
		return DCFS_BGNNNG_BLNC3;

	}
	
	public void setDcfsBeginningBalance3(double DCFS_BGNNNG_BLNC3){
		
		this.DCFS_BGNNNG_BLNC3 = DCFS_BGNNNG_BLNC3;

	}

	public double getDcfsBeginningBalance4(){
		
		return DCFS_BGNNNG_BLNC4;		
	}
	
	public void setDcfsBeginningBalance4(double DCFS_BGNNNG_BLNC4){
		
		this.DCFS_BGNNNG_BLNC4 = DCFS_BGNNNG_BLNC4;		
	}

	public double getDcfsBeginningBalance5(){

		return DCFS_BGNNNG_BLNC5;

	}

	public void setDcfsBeginningBalance5(double DCFS_BGNNNG_BLNC5){

		this.DCFS_BGNNNG_BLNC5 = DCFS_BGNNNG_BLNC5;

	}
	
	public double getDcfsBeginningBalance6(){
		

		return DCFS_BGNNNG_BLNC6;
		
	}

	public void setDcfsBeginningBalance6(double DCFS_BGNNNG_BLNC6){

		this.DCFS_BGNNNG_BLNC6 = DCFS_BGNNNG_BLNC6;
		
	}
	
	public double getDcfsBeginningBalance7(){
		return DCFS_BGNNNG_BLNC7;
		
	}

	public void setDcfsBeginningBalance7(double DCFS_BGNNNG_BLNC7){
		
		this.DCFS_BGNNNG_BLNC7 = DCFS_BGNNNG_BLNC7;
		
	}

	public double getDcfsAvailableCashBalance1(){
		
		return DCFS_AVLBL_CSH_BLNC1;
		
	}
	
	public void setDcfsAvailableCashBalance1(double DCFS_AVLBL_CSH_BLNC1){
		
		this.DCFS_AVLBL_CSH_BLNC1 = DCFS_AVLBL_CSH_BLNC1;
		
	}
	
	public double getDcfsAvailableCashBalance2(){
		
		return DCFS_AVLBL_CSH_BLNC2;
		
	}
	
	public void setDcfsAvailableCashBalance2(double DCFS_AVLBL_CSH_BLNC2){
		
		this.DCFS_AVLBL_CSH_BLNC2 = DCFS_AVLBL_CSH_BLNC2;
		
	}
	
	public double getDcfsAvailableCashBalance3(){
		
		return DCFS_AVLBL_CSH_BLNC3;

	}
	
	public void setDcfsAvailableCashBalance3(double DCFS_AVLBL_CSH_BLNC3){
		
		this.DCFS_AVLBL_CSH_BLNC3 = DCFS_AVLBL_CSH_BLNC3;

	}

	public double getDcfsAvailableCashBalance4(){
		
		return DCFS_AVLBL_CSH_BLNC4;		
	}
	
	public void setDcfsAvailableCashBalance4(double DCFS_AVLBL_CSH_BLNC4){
		
		this.DCFS_AVLBL_CSH_BLNC4 = DCFS_AVLBL_CSH_BLNC4;		
	}

	public double getDcfsAvailableCashBalance5(){

		return DCFS_AVLBL_CSH_BLNC5;

	}

	public void setDcfsAvailableCashBalance5(double DCFS_AVLBL_CSH_BLNC5){

		this.DCFS_AVLBL_CSH_BLNC5 = DCFS_AVLBL_CSH_BLNC5;

	}
	
	public double getDcfsAvailableCashBalance6(){
		

		return DCFS_AVLBL_CSH_BLNC6;
		
	}

	public void setDcfsAvailableCashBalance6(double DCFS_AVLBL_CSH_BLNC6){

		this.DCFS_AVLBL_CSH_BLNC6 = DCFS_AVLBL_CSH_BLNC6;
		
	}
	
	public double getDcfsAvailableCashBalance7(){
		return DCFS_AVLBL_CSH_BLNC7;
		
	}

	public void setDcfsAvailableCashBalance7(double DCFS_AVLBL_CSH_BLNC7){
		
		this.DCFS_AVLBL_CSH_BLNC7 = DCFS_AVLBL_CSH_BLNC7;
		
	}
	
	public ArrayList getDcfsAddList() {
		
		return dcfsAddList;
		
	}
	
	public void setDcfsAddList(ArrayList dcfsAddList) {
		
		this.dcfsAddList = dcfsAddList;
	} 

	public ArrayList getDcfsLessList() {
		
		return dcfsLessList;
		
	}
	
	public void setDcfsLessList(ArrayList dcfsLessList) {
		
		this.dcfsLessList = dcfsLessList;
		
	} 
	
}//CmRepDailyCashForecastSummaryDetails