/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;


public class GlJournalCategoryDetails implements java.io.Serializable {

   private Integer JC_CODE;
   private String JC_NM;
   private String JC_DESC;
   private char JC_RVRSL_MTHD;

   public GlJournalCategoryDetails() {
   }

   public GlJournalCategoryDetails (Integer JC_CODE, String JC_NM,
      String JC_DESC, char JC_RVRSL_MTHD) {

      this.JC_CODE = JC_CODE;
      this.JC_NM = JC_NM;
      this.JC_DESC = JC_DESC;
      this.JC_RVRSL_MTHD = JC_RVRSL_MTHD;

   }

   public GlJournalCategoryDetails (String JC_NM,
      String JC_DESC, char JC_RVRSL_MTHD) {

      this.JC_NM = JC_NM;
      this.JC_DESC = JC_DESC;
      this.JC_RVRSL_MTHD = JC_RVRSL_MTHD;

   }

   public Integer getJcCode() {
      return JC_CODE;
   }

   public String getJcName() {
      return JC_NM;
   }

   public String getJcDescription() {
      return JC_DESC;
   }

   public char getJcReversalMethod() {
      return JC_RVRSL_MTHD;
   }

   public String toString() {
       return "JC_CODE = " + JC_CODE + " JC_NM = " + JC_NM +
          " JC_DESC = " + JC_DESC + " JC_RVRSL_MTHD = " + JC_RVRSL_MTHD;
   }

} // GlJournalCategoryDetails class