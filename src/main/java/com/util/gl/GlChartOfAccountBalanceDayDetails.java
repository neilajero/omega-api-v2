/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;


public class GlChartOfAccountBalanceDayDetails implements java.io.Serializable {

   private Integer COABD_CODE;
   private short COABD_PTD_RNG;
   private short COABD_QTD_RNG;
   private short COABD_YTD_RNG;
   private double COABD_AGGRGT_PTD_BLNC;
   private double COABD_AGGRGT_QTD_BLNC;
   private double COABD_AGGRGT_YTD_BLNC;

   public GlChartOfAccountBalanceDayDetails() {
   }

   public GlChartOfAccountBalanceDayDetails (Integer COABD_CODE,  
      short COABD_PTD_RNG, short COABD_QTD_RNG, short COABD_YTD_RNG,
      double COABD_AGGRGT_PTD_BLNC, double COABD_AGGRGT_QTD_BLNC,
      double COABD_AGGRGT_YTD_BLNC) {

      this.COABD_CODE = COABD_CODE;
      this.COABD_PTD_RNG = COABD_PTD_RNG;
      this.COABD_QTD_RNG = COABD_QTD_RNG;
      this.COABD_YTD_RNG = COABD_YTD_RNG;
      this.COABD_AGGRGT_PTD_BLNC = COABD_AGGRGT_PTD_BLNC;
      this.COABD_AGGRGT_QTD_BLNC = COABD_AGGRGT_QTD_BLNC;
      this.COABD_AGGRGT_YTD_BLNC = COABD_AGGRGT_YTD_BLNC;

   }

   public Integer getCoabdCode() {
      return COABD_CODE;
   }

   public short	getCoabdPtdRange() {
      return COABD_PTD_RNG;
   }

   public short	getCoabdQtdRange() {
      return COABD_QTD_RNG;
   }

   public short	getCoabdYtdRange() {
      return COABD_YTD_RNG;
   }

   public double getCoabdAggregatePtdBalance() {
      return COABD_AGGRGT_PTD_BLNC;
   }

   public double getCoabdAggregateQtdBalance() {
      return COABD_AGGRGT_QTD_BLNC;
   }

   public double getCoabdAggregateYtdBalance() {
      return COABD_AGGRGT_YTD_BLNC;
   }

   public String toString() {
       return " COABD_CODE = " + COABD_CODE +
          " COABD_PTD_RNG = " + COABD_PTD_RNG +
      " COABD_QTD_RNG = " + COABD_QTD_RNG +
      " COABD_YTD_RNG = " + COABD_YTD_RNG +
      " COABD_AGGRGT_PTD_BLNC = " + COABD_AGGRGT_PTD_BLNC +
      " COABD_AGGRGT_QTD_BLNC = " + COABD_AGGRGT_QTD_BLNC +
      " COABD_AGGRGT_YTD_BLNC = " + COABD_AGGRGT_YTD_BLNC;
   }

 } // GlChartOfAccountBalanceDayDetails class