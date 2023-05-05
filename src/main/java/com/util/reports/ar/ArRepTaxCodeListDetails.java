/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;


public class ArRepTaxCodeListDetails implements java.io.Serializable {

   private String TCL_TX_NM; 
   private String TCL_TX_DESC;
   private String TCL_TX_TYP;
   private double TCL_TX_RT; 
   private String TCL_COA_GL_TX_ACCNT_NMBR; 
   private String TCL_COA_GL_TX_ACCNT_DESC;
   private String TCL_COA_GL_INTRM_ACCNT_NMBR; 
   private String TCL_COA_GL_INTRM_ACCNT_DESC; 
   private byte TCL_ENBL;

   public ArRepTaxCodeListDetails() {
   }

   public String getTclCoaGlTaxAccountDescription() {
   	
   	 return TCL_COA_GL_TX_ACCNT_DESC;
   
   }
   
   public void setTclCoaGlTaxAccountDescription(String TCL_COA_GL_TX_ACCNT_DESC) {
   
   	 this.TCL_COA_GL_TX_ACCNT_DESC = TCL_COA_GL_TX_ACCNT_DESC;
   
   }
   
   public String getTclCoaGlTaxAccountNumber() {
   
     return TCL_COA_GL_TX_ACCNT_NMBR;
   
   }
   
   public void setTclCoaGlTaxAccountNumber(String TCL_COA_GL_TX_ACCNT_NMBR) {
   
     this.TCL_COA_GL_TX_ACCNT_NMBR = TCL_COA_GL_TX_ACCNT_NMBR;
   
   }
   
   public String getTclCoaGlInterimAccountDescription() {
   	
   	 return TCL_COA_GL_INTRM_ACCNT_DESC;
   
   }
   
   public void setTclCoaGlInterimAccountDescription(String TCL_COA_GL_INTRM_ACCNT_DESC) {
   
   	 this.TCL_COA_GL_INTRM_ACCNT_DESC = TCL_COA_GL_INTRM_ACCNT_DESC;
   
   }
   
   public String getTclCoaGlInterimAccountNumber() {
   
     return TCL_COA_GL_INTRM_ACCNT_NMBR;
   
   }
   
   public void setTclCoaGlInterimAccountNumber(String TCL_COA_GL_INTRM_ACCNT_NMBR) {
   
     this.TCL_COA_GL_INTRM_ACCNT_NMBR = TCL_COA_GL_INTRM_ACCNT_NMBR;
   
   }
   
   public byte getTclEnable() {
     
     return TCL_ENBL;
   
   }
   
   public void setTclEnable(byte TCL_ENBL) {
   
   	 this.TCL_ENBL = TCL_ENBL;
   
   }
   
   public String getTclTaxDescription() {
   	 
   	 return TCL_TX_DESC;
   
   }
   public void setTclTaxDescription(String TCL_TX_DESC) {
   	
   	 this.TCL_TX_DESC = TCL_TX_DESC;
   
   }
   
   public String getTclTaxName() {
   	
   	 return TCL_TX_NM;
   
   }
   
   public void setTclTaxName(String TCL_TX_NM) {
   	
   	 this.TCL_TX_NM = TCL_TX_NM;
   
   }
   
   public double getTclTaxRate() {
   	 
   	 return TCL_TX_RT;
   
   }
   
   public void setTclTaxRate(double TCL_TX_RT) {
   	
   	 this.TCL_TX_RT = TCL_TX_RT;
   
   }
   
   public String getTclTaxType() {
   	
   	 return TCL_TX_TYP;
   
   }
   
   public void setTclTaxType(String TCL_TX_TYP) {
   	
   	 this.TCL_TX_TYP = TCL_TX_TYP;
   
   }
   
} // ApRepTaxCodeListDetails class