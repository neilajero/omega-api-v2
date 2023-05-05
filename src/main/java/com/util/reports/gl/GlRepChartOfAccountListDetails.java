/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.gl;


public class GlRepChartOfAccountListDetails implements java.io.Serializable {

   private String COA_ACCNT_NMBR;
   private String COA_DESC;
   private String COA_ACCNT_TYP;
   private boolean COA_ENBL;

   public GlRepChartOfAccountListDetails() {
   }

   public GlRepChartOfAccountListDetails(String COA_ACCNT_NMBR, String COA_DESC, String COA_ACCNT_TYP,  
      boolean COA_ENBL) {

      this.COA_ACCNT_NMBR = COA_ACCNT_NMBR;
      this.COA_DESC = COA_DESC;
      this.COA_ACCNT_TYP = COA_ACCNT_TYP;
      this.COA_ENBL = COA_ENBL;

   }

   public String getCoaAccountNumber() {
   	
      return COA_ACCNT_NMBR;
      
   }
   
   public void setCoaAccountNumber(String COA_ACCNT_NMBR) {
   	
   	  this.COA_ACCNT_NMBR = COA_ACCNT_NMBR;
   	
   }

   public String getCoaDescription() {
   	
      return COA_DESC;
      
   }
   
   public void setCoaDescription(String COA_DESC) {
   	
   	  this.COA_DESC = COA_DESC;
   	
   }

   public String getCoaAccountType() {
   	
   	  return COA_ACCNT_TYP;
   	
   }
   
   public void setCoaAccountType(String COA_ACCNT_TYP) {
   	
   	  this.COA_ACCNT_TYP = COA_ACCNT_TYP;
   	
   }
   
   public boolean getCoaEnable() {
   	
   	  return COA_ENBL;
   	   	
   }
   
   public void setCoaEnable(boolean COA_ENBL) {
   	
   	  this.COA_ENBL = COA_ENBL;
   	     	     	
   }
   
} // GlRepChartOfAccountListDetails class   