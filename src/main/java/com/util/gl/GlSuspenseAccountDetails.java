/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;


public class GlSuspenseAccountDetails implements java.io.Serializable {

   private Integer SA_CODE;
   private String SA_NM;
   private String SA_DESC;

   public GlSuspenseAccountDetails() {
   }
   
   public GlSuspenseAccountDetails (Integer SA_CODE, String SA_NM,
      String SA_DESC) {

      this.SA_CODE = SA_CODE;
      this.SA_NM = SA_NM;
      this.SA_DESC = SA_DESC;

   }

   public GlSuspenseAccountDetails (String SA_NM, String SA_DESC){

      this.SA_NM = SA_NM;
      this.SA_DESC = SA_DESC;

   }

   public Integer getSaCode() {
      return SA_CODE;
   }

   public String getSaName() {
      return SA_NM;
   }

   public String getSaDescription() {
      return SA_DESC;
   }

   public String toString() {
       return "SA_CODE = " + SA_CODE + " SA_NM = " + SA_NM +
          " SA_DESC = " + SA_DESC;
   }
 
} // GlSuspenseAccountDetails class