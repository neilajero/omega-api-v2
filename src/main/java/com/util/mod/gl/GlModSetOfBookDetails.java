/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;


public class GlModSetOfBookDetails implements java.io.Serializable {

   private Integer SOB_CODE;
   private String SOB_NM;
   private String SOB_DESC;
   private byte SOB_YR_END_CLSD;
   private String SOB_RTND_EARNNGS;
   private String SOB_DVDND;
   private String SOB_FL_NM;
   private String SOB_FC_NM;
   private String SOB_AC_NM;
   private String SOB_TC_NM;

   public GlModSetOfBookDetails() {
   }

   public GlModSetOfBookDetails (Integer SOB_CODE, String SOB_NM,
      String SOB_DESC, 
      byte SOB_YR_END_CLSD, String SOB_RTND_EARNNGS, String SOB_DVDND,
      String SOB_FL_NM, String SOB_FC_NM, String SOB_AC_NM, String SOB_TC_NM) {

      this.SOB_CODE = SOB_CODE;
      this.SOB_NM = SOB_NM;
      this.SOB_DESC = SOB_DESC;
      this.SOB_YR_END_CLSD = SOB_YR_END_CLSD;
      this.SOB_RTND_EARNNGS = SOB_RTND_EARNNGS;
      this.SOB_DVDND = SOB_DVDND;
      this.SOB_FL_NM = SOB_FL_NM;
      this.SOB_FC_NM = SOB_FC_NM;
      this.SOB_AC_NM = SOB_AC_NM;
      this.SOB_TC_NM = SOB_TC_NM;

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

   public byte getSobYearEndClosed() {
      return SOB_YR_END_CLSD;
   }

   public String getSobRetainedEarnings() {
      return SOB_RTND_EARNNGS;
   }

   public String getSobDividend() {
      return SOB_DVDND;
   }

   public String getSobFlName() {
      return SOB_FL_NM;
   }

   public String getSobFcName() {
      return SOB_FC_NM;
   }

   public String getSobAcName() {
      return SOB_AC_NM;
   }

   public String getSobTcName() {
      return SOB_TC_NM;
   }

   public String toString() {

       return "SOB_CODE = " + SOB_CODE + " SOB_NM = " + SOB_NM +
          " SOB_DESC = " + SOB_DESC +
      " SOB_YR_END_CLSD = " + SOB_YR_END_CLSD +
      " SOB_RTND_EARNNGS = " + SOB_RTND_EARNNGS +
      " SOB_DVDND = " + SOB_DVDND +
      " SOB_FL_NM = " + SOB_FL_NM +
      " SOB_FC_NM = " + SOB_FC_NM +
      " SOB_AC_NM = " + SOB_AC_NM +
      " SOB_TC_NM = " + SOB_TC_NM;
   }

} // GlSetOfBookDetails class