/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;

import com.util.gl.GlTaxInterfaceDetails;

public class GlModTaxInterfaceDetails extends GlTaxInterfaceDetails implements java.io.Serializable {

   private String TI_TXL_COA_NMBR = null;
   private double TI_TAX_AMNT = 0d;
   private String TI_TC_NM = null;
   private double TI_TC_RT = 0d;
   private String TI_WTC_NM = null;
   private double TI_WTC_RT = 0d;
   private byte TI_EDT_GL_DCMNT;
   
   public GlModTaxInterfaceDetails() {
   }

   public String getTiTxlCoaNumber() {
   	
   	  return TI_TXL_COA_NMBR;
   	
   }
   
   public void setTiTxlCoaNumber(String TI_TXL_COA_NMBR) {
   	
   	  this.TI_TXL_COA_NMBR = TI_TXL_COA_NMBR;
   	
   }
   
   public double getTiTaxAmount() {
   	
   	  return TI_TAX_AMNT;
   	  
   }
   
   public void setTiTaxAmount(double TI_TAX_AMNT) {
   	
   	  this.TI_TAX_AMNT = TI_TAX_AMNT;
   	  
   }
   
   public String getTiTcName() {
   	
   	  return TI_TC_NM;
   	  
   }
   
   public void setTiTcName(String TI_TC_NM) {
   	
   	  this.TI_TC_NM = TI_TC_NM;
   	  
   }
   
   public String getTiWtcName() {
   	
   	  return TI_WTC_NM;
   	  
   }
   
   public void setTiWtcName(String TI_WTC_NM) {
   	
   	  this.TI_WTC_NM = TI_WTC_NM;
   	  
   }
   
   public byte getTiEditGlDocument() {
   	
   	  return TI_EDT_GL_DCMNT;
   	
   }
   
   public void setTiEditGlDocument(byte TI_EDT_GL_DCMNT) {
   	
   	  this.TI_EDT_GL_DCMNT = TI_EDT_GL_DCMNT;
   	  
   }
   
   public double getTiTcRate() {
   	
   	  return TI_TC_RT;
   	  
   }
   
   public void setTiTcRate(double TI_TC_RT) {
   	
   	  this.TI_TC_RT = TI_TC_RT;
   	  
   }
   
   public double getTiWtcRate() {
   	
   	  return TI_WTC_RT;
   	  
   }
   
   public void setTiWtcRate(double TI_WTC_RT) {
   	
   	  this.TI_WTC_RT = TI_WTC_RT;
   	  
   }
   
} // GlModTaxInterfaceDetails class