/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;


public class ArRepStandardMemoLineListDetails implements java.io.Serializable {

   private String SLL_SML_NM; 
   private String SLL_SML_DESC;
   private String SLL_SML_TYP;
   private double SLL_SML_UNT_PRC; 
   private String SLL_COA_GL_ACCNT_NMBR; 
   private String SLL_COA_GL_ACCNT_DESC;
   private byte SLL_TXBL;
   private byte SLL_ENBL;

   public ArRepStandardMemoLineListDetails() {
   }

   public String getSllCoaGlAccountDescription() {
   	
   	 return SLL_COA_GL_ACCNT_DESC;
   
   }
   
   public void setSllCoaGlAccountDescription(String SLL_COA_GL_ACCNT_DESC) {
   
   	 this.SLL_COA_GL_ACCNT_DESC = SLL_COA_GL_ACCNT_DESC;
   
   }
   
   public String getSllCoaGlAccountNumber() {
   
     return SLL_COA_GL_ACCNT_NMBR;
   
   }
   
   public void setSllCoaGlAccountNumber(String SLL_COA_GL_ACCNT_NMBR) {
   
     this.SLL_COA_GL_ACCNT_NMBR = SLL_COA_GL_ACCNT_NMBR;
   
   }
   
   public byte getSllTaxable() {
    
    return SLL_TXBL;
  
   }
  
   public void setSllTaxable(byte SLL_TXBL) {
  
  	 this.SLL_TXBL = SLL_TXBL;
  
   }
   
   public byte getSllEnable() {
     
     return SLL_ENBL;
   
   }
   
   public void setSllEnable(byte SLL_ENBL) {
   
   	 this.SLL_ENBL = SLL_ENBL;
   
   }
   
   public String getSllSmlDescription() {
   	 
   	 return SLL_SML_DESC;
   
   }
   public void setSllSmlDescription(String SLL_SML_DESC) {
   	
   	 this.SLL_SML_DESC = SLL_SML_DESC;
   
   }
   
   public String getSllSmlName() {
   	
   	 return SLL_SML_NM;
   
   }
   
   public void setSllSmlName(String SLL_SML_NM) {
   	
   	 this.SLL_SML_NM = SLL_SML_NM;
   
   }
   
   public double getSllSmlUnitPrice() {
   	 
   	 return SLL_SML_UNT_PRC;
   
   }
   
   public void setSllSmlUnitPrice(double SLL_SML_UNT_PRC) {
   	
   	 this.SLL_SML_UNT_PRC = SLL_SML_UNT_PRC;
   
   }
   
   public String getSllSmlType() {
   	
   	 return SLL_SML_TYP;
   
   }
   
   public void setSllSmlType(String SLL_SML_TYP) {
   	
   	 this.SLL_SML_TYP = SLL_SML_TYP;
   
   }
   
} // ApRepStandardMemoLineListDetails class