/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;


public class GlChartOfAccountBalancePeriodDetails implements java.io.Serializable {

   private Integer COABP_CODE;
   private short COABP_PRD_NMBR;

   public GlChartOfAccountBalancePeriodDetails() {
   }
   
   public GlChartOfAccountBalancePeriodDetails (Integer COABP_CODE,
      short COABP_PRD_NMBR) {

      this.COABP_CODE = COABP_CODE;
      this.COABP_PRD_NMBR = COABP_PRD_NMBR;

   }

   public Integer getCoabpCode() {
      return COABP_CODE;
   }

   public short getCoabpPeriodNumber() {
      return COABP_PRD_NMBR;
   }

   public String toString() {
       return "COABP_CODE = " + COABP_CODE +
          "COABP_PRD_NMBR = " + COABP_PRD_NMBR;
   }

}  // GlChartOfAccountBalancePeriodDetails class