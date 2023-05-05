/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;

import com.util.gl.GlBudgetDetails;

public class GlModBudgetDetails extends GlBudgetDetails implements java.io.Serializable {

   private boolean BGT_IS_DFLT;

   public GlModBudgetDetails() {
   }

   public boolean getBgtIsDefault() {
   	
   	  return BGT_IS_DFLT;
   	 
   }
   
   public void setBgtIsDefault(boolean BGT_IS_DFLT) {
   	
   	  this.BGT_IS_DFLT = BGT_IS_DFLT;  
   	
   }

} // GlModBudgetAmountDetails class