/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;


public class GlSummaryAccountTemplateDetails implements java.io.Serializable {

   private Integer SAT_CODE;
   private String SAT_NM;
   private String SAT_TMPLT;

   public GlSummaryAccountTemplateDetails() {
   }

   public GlSummaryAccountTemplateDetails (Integer SAT_CODE,
      String SAT_NM, String SAT_TMPLT) {

      this.SAT_CODE = SAT_CODE;
      this.SAT_NM = SAT_NM;
      this.SAT_TMPLT = SAT_TMPLT;

   }

   public Integer getSatCode() {
     return SAT_CODE;
   }

   public String getSatName() {
      return SAT_NM;
   }

   public String getSatTemplate() {
      return SAT_TMPLT;
   }

   public String toString() {
       return "SAT_CODE = " + SAT_CODE + " SAT_NM = " + SAT_NM +
          " SAT_TMPLT = " + SAT_TMPLT;
   }

} // GlSummaryAccountTemplateDetails class