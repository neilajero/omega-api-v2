/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;


public class AdModDocumentSequenceAssignmentDetails implements java.io.Serializable {

   private Integer DSA_CODE;
   private Integer DSA_SOB_CODE;
   private String DSA_NXT_SQNC;
   private String DSA_DC_NM;
   private String DSA_DS_NM;

   public AdModDocumentSequenceAssignmentDetails () {
   }

   public AdModDocumentSequenceAssignmentDetails (Integer DSA_CODE, Integer DSA_SOB_CODE,
      String DSA_NXT_SQNC, String DSA_DC_NM,
      String DSA_DS_NM) {

      this.DSA_CODE = DSA_CODE;
      this.DSA_SOB_CODE = DSA_SOB_CODE;
      this.DSA_NXT_SQNC = DSA_NXT_SQNC;
      this.DSA_DC_NM = DSA_DC_NM;
      this.DSA_DS_NM = DSA_DS_NM;

   }

   public Integer getDsaCode() {
      return DSA_CODE;
   }

   public Integer getDsaSobCode() {
      return DSA_SOB_CODE;
   }

   public String getDsaNextSequence() {
      return DSA_NXT_SQNC;
   }
   
   public String getDsaDcName() {
      return DSA_DC_NM;
   }

   public String getDsaDsName() {
      return DSA_DS_NM;
   }

   public String toString() {
       return "DSA_CODE = " + DSA_CODE + " DSA_SOB_CODE = " + DSA_SOB_CODE +
      " DSA_NXT_SQNC = " + DSA_NXT_SQNC + " DSA_DS_NM = " + DSA_DS_NM +
      " DSA_DC_NM = " + DSA_DC_NM;
   }

} // AdDocumentSequenceAssignmentDetails class   