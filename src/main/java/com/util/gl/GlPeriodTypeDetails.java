/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;


public class GlPeriodTypeDetails implements java.io.Serializable {

   private Integer PT_CODE;
   private String PT_NM;
   private String PT_DESC;
   private short PT_PRD_PER_YR;
   private char PT_YR_TYP;

   public GlPeriodTypeDetails() {
   }

   public GlPeriodTypeDetails (Integer PT_CODE, String PT_NM,
      String PT_DESC, short PT_PRD_PER_YR, char PT_YR_TYP) {

      this.PT_CODE = PT_CODE;
      this.PT_NM = PT_NM;
      this.PT_DESC = PT_DESC;
      this.PT_PRD_PER_YR = PT_PRD_PER_YR;
      this.PT_YR_TYP = PT_YR_TYP;

   }

   public GlPeriodTypeDetails (String PT_NM,
      String PT_DESC, short PT_PRD_PER_YR, char PT_YR_TYP) {

      this.PT_NM = PT_NM;
      this.PT_DESC = PT_DESC;
      this.PT_PRD_PER_YR = PT_PRD_PER_YR;
      this.PT_YR_TYP = PT_YR_TYP;

   }

   public Integer getPtCode() {
      return PT_CODE;
   }

   public String getPtName() {
      return PT_NM;
   }

   public String getPtDescription() {
      return PT_DESC;
   }

   public short getPtPeriodPerYear() {
      return PT_PRD_PER_YR;
   }

   public char getPtYearType() {
      return PT_YR_TYP;
   }

   public String toString() {
       return PT_CODE + "&nbsp;&nbsp;&nbsp;&nbsp;" +
                  PT_NM + "&nbsp;&nbsp;&nbsp;&nbsp;" +
          PT_DESC + "&nbsp;&nbsp;&nbsp;&nbsp;" +
          PT_PRD_PER_YR + "&nbsp;&nbsp;&nbsp;&nbsp;" +
          PT_YR_TYP;
   }

} // GlPeriodTypeDetails class