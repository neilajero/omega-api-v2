/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;


import com.util.gl.GlFrgFinancialReportDetails;

public class GlModFrgFinancialReportDetails extends GlFrgFinancialReportDetails implements java.io.Serializable {

   private String FR_RS_NM;  
   private String FR_CS_NM; 
   
   public GlModFrgFinancialReportDetails () {
   }

   public GlModFrgFinancialReportDetails (Integer FR_CODE, String FR_NM, String FR_DESC,
      String FR_TTLE, int FR_FNT_SZ, String FR_FNT_STYL, String FRG_HRZNTL_ALGN, String FR_RS_NM, String FR_CS_NM) {
   	
 
   	  this.FR_RS_NM = FR_RS_NM;
   	  this.FR_CS_NM = FR_CS_NM;
   	
   }
   
   public String getFrRowName() {
   	
   	   return FR_RS_NM;
   	
   }
   
   public void setFrRowName(String FR_RS_NM) {
   	
   	   this.FR_RS_NM = FR_RS_NM;
   	
   }
   
   public String getFrColumnName() {
   	
   	   return FR_CS_NM;
   	
   }
   
   public void setFrColumnName(String FR_CS_NM) {
   	
   	   this.FR_CS_NM = FR_CS_NM;
   	
   }
   	      	      	      	      	      	      	                 
} // GlModFrgFinancialReportDetails class   