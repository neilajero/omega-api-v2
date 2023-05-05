/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;


public class GlSetOfBookDetails implements java.io.Serializable {

   private Integer SOB_CODE;
   private String SOB_NM;
   private String SOB_DESC;
   private byte SOB_PRE_YR_END_CLSD;
   private byte SOB_YR_END_CLSD;
   private String SOB_RTND_EARNNGS;
   private String SOB_DVDND;

   public GlSetOfBookDetails() {
   }

   public GlSetOfBookDetails (Integer SOB_CODE, String SOB_NM,
      String SOB_DESC,
      byte SOB_PRE_YR_END_CLSD,
      byte SOB_YR_END_CLSD, String SOB_RTND_EARNNGS, 
      String SOB_DVDND) {

      this.SOB_CODE = SOB_CODE;
      this.SOB_NM = SOB_NM;
      this.SOB_DESC = SOB_DESC;
      this.SOB_PRE_YR_END_CLSD = SOB_PRE_YR_END_CLSD;
      this.SOB_YR_END_CLSD = SOB_YR_END_CLSD;
      this.SOB_RTND_EARNNGS = SOB_RTND_EARNNGS;
      this.SOB_DVDND = SOB_DVDND;

   }

   public GlSetOfBookDetails (String SOB_NM,
      String SOB_DESC, 
      byte SOB_PRE_YR_END_CLSD,
      byte SOB_YR_END_CLSD, String SOB_RTND_EARNNGS,
      String SOB_DVDND) {

      this.SOB_NM = SOB_NM;
      this.SOB_DESC = SOB_DESC;
      this.SOB_PRE_YR_END_CLSD = SOB_PRE_YR_END_CLSD;
      this.SOB_YR_END_CLSD = SOB_YR_END_CLSD;
      this.SOB_RTND_EARNNGS = SOB_RTND_EARNNGS;
      this.SOB_DVDND = SOB_DVDND;

   }

   public GlSetOfBookDetails (Integer SOB_CODE, String SOB_NM,
      String SOB_DESC, String SOB_RTND_EARNNGS,
      String SOB_DVDND) {

      this.SOB_CODE = SOB_CODE;
      this.SOB_NM = SOB_NM;
      this.SOB_DESC = SOB_DESC;
      this.SOB_RTND_EARNNGS = SOB_RTND_EARNNGS;
      this.SOB_DVDND = SOB_DVDND;

   }

   public GlSetOfBookDetails (String SOB_NM,
      String SOB_DESC, String SOB_RTND_EARNNGS,
      String SOB_DVDND) {

      this.SOB_NM = SOB_NM;
      this.SOB_DESC = SOB_DESC;
      this.SOB_RTND_EARNNGS = SOB_RTND_EARNNGS;
      this.SOB_DVDND = SOB_DVDND;

   }

   public Integer getSobCode() {
      return SOB_CODE;
   }

   public String getSobName() {
      return SOB_NM;
   }

   public String getSobDescription() {
      return SOB_DESC;
   }

   public byte getSobPreYearEndClosed() {
      return SOB_PRE_YR_END_CLSD;
   }

   public byte getSobYearEndClosed() {
      return SOB_YR_END_CLSD;
   }

   public String getSobRetainedEarnings() {
      return SOB_RTND_EARNNGS;
   }

   public String getSobDividend() {
      return SOB_DVDND;
   }

   public String toString() {
       return "SOB_CODE = " + SOB_CODE + " SOB_NM = " + SOB_NM +
          " SOB_DESC = " + SOB_DESC +
      " SOB_PRE_YR_END_CLSD = " + SOB_PRE_YR_END_CLSD +
      " SOB_YR_END_CLSD = " + SOB_YR_END_CLSD +
      " SOB_RTND_EARNNGS = " + SOB_RTND_EARNNGS +
      " SOB_DVDND = " + SOB_DVDND;
   }

} // GlSetOfBookDetails class