/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;

public class GlCurrencyRateDetails implements java.io.Serializable {

   private Integer CRR_CODE;
   private String CRR_NM;
   private String CRR_DESC;
   private char CRR_TYP;

   public GlCurrencyRateDetails() {
   }

   public GlCurrencyRateDetails (Integer CRR_CODE, String CRR_NM,
      String CRR_DESC, char CRR_TYP) {
      
      this.CRR_CODE = CRR_CODE;
      this.CRR_NM = CRR_NM;
      this.CRR_DESC = CRR_DESC;
      this.CRR_TYP = CRR_TYP;

   }

   public Integer getCrrCode() {
      return CRR_CODE;
   }

   public String getCrrName() {
      return CRR_NM;
   }

   public String getCrrDescription() {
      return CRR_DESC;
   }

   public char getCrrType() {
      return CRR_TYP;
   }

   public String toString() {
       return "CRR_CODE = " + CRR_CODE + " CRR_NM = " + CRR_NM +
          " CRR_DESC = " + CRR_DESC + " CRR_TYP = " + CRR_TYP;
   }

} // GlCurrencyRateDetails class