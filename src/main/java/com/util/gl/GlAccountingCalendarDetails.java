/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;


public class GlAccountingCalendarDetails implements java.io.Serializable {

   private Integer AC_CODE;
   private String AC_NM;
   private String AC_DESC;

   public GlAccountingCalendarDetails() {
   }

   public GlAccountingCalendarDetails (Integer AC_CODE,
      String AC_NM, String AC_DESC) {

      this.AC_CODE = AC_CODE;
      this.AC_NM = AC_NM;
      this.AC_DESC = AC_DESC;

   }

   public GlAccountingCalendarDetails (
      String AC_NM, String AC_DESC) {

      this.AC_NM = AC_NM;
      this.AC_DESC = AC_DESC;
   }

   public GlAccountingCalendarDetails (
      String AC_NM) {

      this.AC_NM = AC_NM;
   }

   public Integer getAcCode() {
      return AC_CODE;
   }

   public String getAcName() {
      return AC_NM;
   }

   public String getAcDescription() {
      return AC_DESC;
   }

   public String toString() {
       return "AC_CODE = " + AC_CODE + " AC_NM = " + AC_NM +
          " AC_DESC = " + AC_DESC;
   }

} // GlAccountingCalendarDetails class