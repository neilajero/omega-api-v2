/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;


public class GlTransactionCalendarDetails implements java.io.Serializable {

   private Integer TC_CODE;
   private String TC_NM;
   private String TC_DESC;

   public GlTransactionCalendarDetails() {
   }
   
   public GlTransactionCalendarDetails (Integer TC_CODE,
      String TC_NM, String TC_DESC) {

      this.TC_CODE = TC_CODE;
      this.TC_NM = TC_NM;
      this.TC_DESC = TC_DESC;

   }

   public GlTransactionCalendarDetails (
      String TC_NM, String TC_DESC) {

      this.TC_NM = TC_NM;
      this.TC_DESC = TC_DESC;

   }

   public GlTransactionCalendarDetails (
      String TC_NM) {

      this.TC_NM = TC_NM;

   }

   public Integer getTcCode() {
      return TC_CODE;
   }

   public String getTcName() {
      return TC_NM;
   }

   public String getTcDescription() {
      return TC_DESC;
   }

   public String toString() {
       return "TC_CODE = " + TC_CODE + " TC_NM = " + TC_NM +
          " TC_DESC = " + TC_DESC;
   }

} // GlTransactionCalendarDetails class