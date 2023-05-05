/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.ad;


public class AdDocumentSequenceAssignmentDetails implements java.io.Serializable {

   private Integer DSA_CODE;
   private Integer DSA_SOB_CODE;
   private String DSA_NXT_SQNC;

   public AdDocumentSequenceAssignmentDetails () {
   }

   public AdDocumentSequenceAssignmentDetails (Integer DSA_CODE, Integer DSA_SOB_CODE,
      String DSA_NXT_SQNC) {

      this.DSA_CODE = DSA_CODE;
      this.DSA_SOB_CODE = DSA_SOB_CODE;
      this.DSA_NXT_SQNC = DSA_NXT_SQNC;

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

   public String toString() {
       return "DSA_CODE = " + DSA_CODE + " DSA_SOB_CODE = " + DSA_SOB_CODE +
      " DSA_NXT_SQNC = " + DSA_NXT_SQNC;
   }

} // AdDocumentSequenceAssignmentDetails class   