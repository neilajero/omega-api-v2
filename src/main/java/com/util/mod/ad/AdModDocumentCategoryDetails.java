/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;


public class AdModDocumentCategoryDetails implements java.io.Serializable {

   private Integer DC_CODE;
   private String DC_NM;
   private String DC_DESC;
   private String DC_APP_NM;

   public AdModDocumentCategoryDetails () {
   }

   public AdModDocumentCategoryDetails (Integer DC_CODE, String DC_NM,
      String DC_DESC, String DC_APP_NM) {

      this.DC_CODE = DC_CODE;
      this.DC_NM = DC_NM;
      this.DC_DESC = DC_DESC;
      this.DC_APP_NM = DC_APP_NM;

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
   
   public String getDcAppName() {
   	  return DC_APP_NM;
   }   	  

   public String toString() {
       return "DC_CODE = " + DC_CODE + " DC_NM = " + DC_NM +
          " DC_DESC = " + DC_DESC + " DC_APP_NM = " + DC_APP_NM;
   }

} // AdDocumentCategoryDetails class   