/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.ad;


public class AdApplicationDetails implements java.io.Serializable {

   private Integer APP_CODE;
   private String APP_NM;
   private String APP_DESC;

   public AdApplicationDetails () {
   }

   public AdApplicationDetails (Integer APP_CODE, String APP_NM,
      String APP_DESC) {

      this.APP_CODE = APP_CODE;
      this.APP_NM = APP_NM;
      this.APP_DESC = APP_DESC;

   }

   public Integer getAppCode() {
      return APP_CODE;
   }

   public String getAppName() {
      return APP_NM;
   }

   public String getAppDescription() {
      return APP_DESC;
   }

   public String toString() {
      return "APP_CODE = " + APP_CODE + " APP_NM = " + APP_NM +
         " APP_DESC = " + APP_DESC;
   }

} // AdRecurringApplicatoinDetails class   