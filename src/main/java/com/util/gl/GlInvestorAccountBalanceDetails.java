/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;

import java.util.Comparator;
import java.util.Date;


public class GlInvestorAccountBalanceDetails implements java.io.Serializable {

   private int IRAB_CODE;
   private Date IRAB_DT;
   private double IRAB_AMNT;
   private String IRAB_DCMNT_NMBR;
   private String IRAB_TYP;

   public GlInvestorAccountBalanceDetails() {
   }
   
   public int getGlIrabCode() {
	   	
	   	return IRAB_CODE;
	   	
   }    
	   
   public void setGlIrabCode(int IRAB_CODE) {
   	
   	this.IRAB_CODE = IRAB_CODE;
   	
   }
   
   
   

   public Date getGlIrabDate() {
   	
   	return IRAB_DT;
   	
   }    
   
   public void setGlIrabDate(Date IRAB_DT) {
   	
   	this.IRAB_DT = IRAB_DT;
   	
   }
   
   
   
   public double getGlIrabAmount() {
   	
      return IRAB_AMNT;
      
   }
   
   public void setGlIrabAmount(double IRAB_AMNT) {
   	
   	  this.IRAB_AMNT = IRAB_AMNT;
   	
   }
   
   
   public String getGlIrabDocumentNumber() {
	   	
      return IRAB_DCMNT_NMBR;
      
   }
   
   public void setGlIrabDocumentNumber(String IRAB_DCMNT_NMBR) {
   	
   	  this.IRAB_DCMNT_NMBR = IRAB_DCMNT_NMBR;
   	
   }

   
   public String getGlIrabType() {
	   	
      return IRAB_TYP;
      
   }
   
   public void setGlIrabType(String IRAB_TYP) {
   	
   	  this.IRAB_TYP = IRAB_TYP;
   	
   }
   
   
   
   
   public static Comparator DateComparator = (GL, anotherGL) -> {

       Date GL_IRAB_DT1 = ((GlInvestorAccountBalanceDetails) GL).getGlIrabDate();

       Date GL_IRAB_DT2 = ((GlInvestorAccountBalanceDetails) anotherGL).getGlIrabDate();

       if(!(GL_IRAB_DT1.equals(GL_IRAB_DT2))){

           return GL_IRAB_DT1.compareTo(GL_IRAB_DT2);

       } else {

           return GL_IRAB_DT1.compareTo(GL_IRAB_DT2);


       }

   };
   
   
   
   
   
} // GlRepDetailInvestorAccountBalanceDetails class   