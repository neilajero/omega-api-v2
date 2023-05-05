/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;

import java.util.Date;

public class GlTransactionCalendarValueDetails implements java.io.Serializable {

   private Integer TCV_CODE;
   private Date TCV_DT;
   private short TCV_DY_OF_WK;
   private byte TCV_BSNSS_DY;

   public GlTransactionCalendarValueDetails() {
   }

   public GlTransactionCalendarValueDetails (Integer TCV_CODE,
      Date TCV_DT, short TCV_DY_OF_WK, byte TCV_BSNSS_DY) {

      this.TCV_CODE = TCV_CODE;
      this.TCV_DT = TCV_DT;
      this.TCV_DY_OF_WK = TCV_DY_OF_WK;
      this.TCV_BSNSS_DY = TCV_BSNSS_DY;

   }

   public GlTransactionCalendarValueDetails (
      Date TCV_DT, short TCV_DY_OF_WK, byte TCV_BSNSS_DY) {

      this.TCV_DT = TCV_DT;
      this.TCV_DY_OF_WK = TCV_DY_OF_WK;
      this.TCV_BSNSS_DY = TCV_BSNSS_DY;

   }

   public GlTransactionCalendarValueDetails (
      Integer TCV_CODE, byte TCV_BSNSS_DY) {

      this.TCV_CODE = TCV_CODE;
      this.TCV_BSNSS_DY = TCV_BSNSS_DY;

   }

   public Integer getTcvCode() {
      return TCV_CODE;
   }

   public Date getTcvDate() {
      return TCV_DT;
   }

   public short getTcvDayOfWeek() {
      return TCV_DY_OF_WK;
   }

   public byte getTcvBusinessDay() {
      return TCV_BSNSS_DY;
   }

   public String toString() {
       return "TCV_CODE = " + TCV_CODE + " TCV_DT = " + TCV_DT +
          " TCV_DY_OF_WK = " + TCV_DY_OF_WK + " TCV_BSNSS_DY = " + TCV_BSNSS_DY;
   }

} // GlTransactionCalendarValueDetails class