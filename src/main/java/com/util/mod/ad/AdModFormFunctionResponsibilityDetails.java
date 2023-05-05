/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;


import com.util.ad.AdFormFunctionResponsibilityDetails;

public class AdModFormFunctionResponsibilityDetails extends AdFormFunctionResponsibilityDetails implements java.io.Serializable {

   private String FR_FF_NM;
   private String FR_RS_NM;
   private Integer FR_FF_CODE;
   private Integer FR_RS_CODE;

   public AdModFormFunctionResponsibilityDetails () {
   }

  
   
   public String getFrFfFormFunctionName() {
   	
   	   return FR_FF_NM;
   	
   }
   
   public void setFrFfFormFunctionName(String FR_FF_NM) {
   	
   	   this.FR_FF_NM = FR_FF_NM;
   	
   }
   
   public Integer getFrFfCode() {
   	
   	   return FR_FF_CODE;
   	
   }
   
   public void setFrFfCode(Integer FR_FF_CODE) {
   	
   	   this.FR_FF_CODE = FR_FF_CODE;
   	
   }
   
    public Integer getFrRsCode() {
   	
   	   return FR_RS_CODE;
   	
   }
   
   public void setFrRsCode(Integer FR_RS_CODE) {
   	
   	   this.FR_RS_CODE = FR_RS_CODE;
   	
   }
   
   
}
 

// AdModFormFunctionResponsibilityDetails class   