/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.ad;


public class AdDocumentCategoryDetails implements java.io.Serializable {

   private Integer DC_CODE;
   private String DC_NM;
   private String DC_DESC;

   public AdDocumentCategoryDetails () {
   }

   public AdDocumentCategoryDetails (Integer DC_CODE, String DC_NM,
      String DC_DESC) {

      this.DC_CODE = DC_CODE;
      this.DC_NM = DC_NM;
      this.DC_DESC = DC_DESC;

   }
   
   public AdDocumentCategoryDetails(String DC_NM, String DC_DESC) {
   	
   	  this.DC_NM = DC_NM;
   	  this.DC_DESC = DC_DESC;
   	  
   }   	  

   public Integer getDcCode() {
      return DC_CODE;
   }

   public String getDcName() {
      return DC_NM;
   }

   public String getDcDescription() {
      return DC_DESC;
   }

   public String toString() {
       return "DC_CODE = " + DC_CODE + " DC_NM = " + DC_NM +
          " DC_DESC = " + DC_DESC;
   }

} // AdDocumentCategoryDetails class   