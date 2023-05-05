/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;

import com.util.gl.GlBudgetAmountPeriodDetails;

public class GlModBudgetAmountPeriodDetails extends GlBudgetAmountPeriodDetails implements java.io.Serializable {

   private String BAP_ACV_PRD_PRFX = null;
   private int BAP_ACV_YR = 0;

   public GlModBudgetAmountPeriodDetails() {
   }

   
   public String getBapAcvPeriodPrefix() {
   	
   	  return BAP_ACV_PRD_PRFX;
   	
   }
   
   public void setBapAcvPeriodPrefix(String BAP_ACV_PRD_PRFX) {
   	
   	  this.BAP_ACV_PRD_PRFX = BAP_ACV_PRD_PRFX;
   	
   }
   
   public int getBapAcvYear() {
   	
   	  return BAP_ACV_YR;
   	  
   }
   
   public void setBapAcvYear(int BAP_ACV_YR) {
   	
   	  this.BAP_ACV_YR = BAP_ACV_YR;
   	  
   }

} // GlModBudgetAmountDetails class