/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

/*
 * Created on Aug 14, 2008
 *
 */
package com.util.reports.cm;

import java.util.Comparator;
import java.util.Date;

/**
 * Author: Ariel De Guzman & Reginald Pasco
 *
 */

public class CmRepCashPositionDetails implements java.io.Serializable {

	private double CP_BGNNNG_BLNC;
	private double CP_AMNT;
	private String CP_TYP;
	private String CP_ACCT;	
	private String CP_DESC;
	private String CP_NMBR;
	private String CP_REF_NUM;
	private Date CP_DT_FROM;
	private Date CP_DT_TO;
	private Date CP_DT;
	
	private String ORDER_BY;
		
	public double getCpBegBal(){
		
		return CP_BGNNNG_BLNC;
		
	}
	
	public void setCpBegBal(double CP_BGNNNG_BLNC){
		
		this.CP_BGNNNG_BLNC = CP_BGNNNG_BLNC;
		
	}
	
	public double getCpAmount(){
		
		return CP_AMNT;
		
	}
	
	public void setCpAmount(double CP_AMNT){
		
		this.CP_AMNT = CP_AMNT;
		
	}
	
	public String getCpType(){
		
		return CP_TYP;
		
	}
	
	public void setCpType(String CP_TYP){
		
		this.CP_TYP = CP_TYP;
		
	}
	
	public String getCpAccount(){
		
		return CP_ACCT;

	}
	
	public void setCpAccount(String CP_ACCT){
		
		this.CP_ACCT = CP_ACCT;

	}

	public Date getCpDate(){
		
		return CP_DT;		
	}
	
	public void setCpDate(Date CP_DT){
		
		this.CP_DT = CP_DT;		
	}

	public String getCpDesc(){

		return CP_DESC;

	}

	public void setCpDesc(String CP_DESC){

		this.CP_DESC = CP_DESC;

	}

	public String getCpNum(){
		

		return CP_NMBR;
		
	}

	public void setCpNum(String CP_NMBR){

		this.CP_NMBR = CP_NMBR;
		
	}
	
	public String getCpRefNum(){
		return CP_REF_NUM;
		
	}

	public void setCpRefNum(String CP_REF_NUM){
		
		this.CP_REF_NUM = CP_REF_NUM;
		
	}
	
	public Date getCpDateTo(){
		return CP_DT_TO;
		
	}

	public void setCpDateTo(Date CP_DT_TO){
		
		this.CP_DT_TO = CP_DT_TO;
		
	}
	public Date getCpDateFrom(){
		return CP_DT_FROM;
		
	}

	public void setCpDateFrom(Date CP_DT_FROM){
		
		this.CP_DT_FROM = CP_DT_FROM;
		
	}
	
	public String getOrderBy() {
		
		return ORDER_BY;
		
	}
	
	public void setOrderBy(String ORDER_BY) {
		
		this.ORDER_BY = ORDER_BY;
		
	}
	
	public static Comparator BankAccountComparator = (CP, anotherCP) -> {

        String CP_BNK_ACCNT1 = ((CmRepCashPositionDetails) CP).getCpAccount();
        String CP_TYPE1 = ((CmRepCashPositionDetails) CP).getCpType();
        Date CP_DT1 = ((CmRepCashPositionDetails) CP).getCpDate();
        String CP_NUM1 = ((CmRepCashPositionDetails) CP).getCpNum();

        String CP_BNK_ACCNT2 = ((CmRepCashPositionDetails) anotherCP).getCpAccount();
        String CP_TYPE2 = ((CmRepCashPositionDetails) anotherCP).getCpType();
           Date CP_DT2 = ((CmRepCashPositionDetails) anotherCP).getCpDate();
           String CP_NUM2 = ((CmRepCashPositionDetails) anotherCP).getCpNum();

           String ORDER_BY = ((CmRepCashPositionDetails) CP).getOrderBy();

           if (!(CP_BNK_ACCNT1.equals(CP_BNK_ACCNT2))) {

               return CP_BNK_ACCNT1.compareTo(CP_BNK_ACCNT2);

           }else {

               if (!(CP_TYPE1.equals(CP_TYPE2))) {

                   return CP_TYPE1.compareTo(CP_TYPE2);

               } else {


                   if (!(CP_DT1.equals(CP_DT2))) {

                       return CP_DT1.compareTo(CP_DT2);

                   } else {

                       return CP_NUM1.compareTo(CP_NUM2);

                   }

               }




           }
       };
	
}//CmRepCashPositionDetails