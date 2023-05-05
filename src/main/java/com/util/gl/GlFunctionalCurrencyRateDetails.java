/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;

import java.util.Date;

public class GlFunctionalCurrencyRateDetails implements java.io.Serializable {

   private Integer FR_CODE;
   private double FR_X_USD;
   private Date FR_DT;

   public GlFunctionalCurrencyRateDetails() {
   }

   public GlFunctionalCurrencyRateDetails (Integer FR_CODE, double FR_X_USD, Date FR_DT) {

      this.FR_CODE = FR_CODE;
      this.FR_X_USD = FR_X_USD;
      this.FR_DT = FR_DT;
      
   }

   public GlFunctionalCurrencyRateDetails (double FR_X_USD, Date FR_DT) {

      this.FR_X_USD = FR_X_USD;
      this.FR_DT = FR_DT;

   }

   public Integer getFrCode() {
      return FR_CODE;
   }

   public double getFrXToUsd() {
      return FR_X_USD;
   }

   public Date getFrDate() {
      return FR_DT;
   }

   public String toString() {
       return "FR_CODE = " + FR_CODE + " FR_X_USD = " + FR_X_USD +
         " FR_DT = " + FR_DT;
   }

} // GlFunctionalCurrencyRateDetails