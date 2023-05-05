/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;


public class GlAccountRangeDetails implements java.io.Serializable {

   private Integer AR_CODE;
   private short AR_LN;
   private String AR_ACCNT_LW;
   private String AR_ACCNT_HGH;

   public GlAccountRangeDetails() {
   }

   public GlAccountRangeDetails (Integer AR_CODE, short AR_LN, String AR_ACCNT_LW,
      String AR_ACCNT_HGH) {

      this.AR_CODE = AR_CODE;
      this.AR_LN = AR_LN;
      this.AR_ACCNT_LW = AR_ACCNT_LW;
      this.AR_ACCNT_HGH = AR_ACCNT_HGH;

   }

   public GlAccountRangeDetails (short AR_LN, String AR_ACCNT_LW,
      String AR_ACCNT_HGH) {

      this.AR_LN = AR_LN;
      this.AR_ACCNT_LW = AR_ACCNT_LW;
      this.AR_ACCNT_HGH = AR_ACCNT_HGH;
   }

   public Integer getArCode() {
      return AR_CODE;
   }

   public short getArLine() {
      return AR_LN;
   }

   public String getArAccountLow() {
      return AR_ACCNT_LW;
   }

   public String getArAccountHigh() {
      return AR_ACCNT_HGH;
   }

   public String toString() {
       return "AR_CODE = " + AR_CODE + " AR_LN = " + AR_LN + " AR_ACCNT_LW = "
          + AR_ACCNT_LW + " AR_ACCNT_HGH = " + AR_ACCNT_HGH;
   }

} // GlAccountRangeDetails class   