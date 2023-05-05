/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;


public class GlSummaryAccountDetails implements java.io.Serializable {

   private Integer SMA_CODE;
   private String SMA_ACCNT_NMBR;
   private double SMA_BLNC;

   public GlSummaryAccountDetails() {
   }

   public GlSummaryAccountDetails (Integer SMA_CODE,
      String SMA_ACCNT_NMBR, double SMA_BLNC) {

      this.SMA_CODE = SMA_CODE;
      this.SMA_ACCNT_NMBR = SMA_ACCNT_NMBR;
      this.SMA_BLNC = SMA_BLNC;

   }

   public Integer getSmaCode() {
      return SMA_CODE;
   }

   public String getSmaAccountNumber() {
      return SMA_ACCNT_NMBR;
   }

   public double getSmaBalance() {
      return SMA_BLNC;
   }

   public String toString() {
       return " SMA_CODE = " + SMA_CODE + " SMA_ACCNT_NMBR = " + SMA_ACCNT_NMBR +
          " SMA_BLNC = " + SMA_BLNC;

   }

} // GlSummaryAccountDetails class