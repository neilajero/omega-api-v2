/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;

import com.util.gl.GlBudgetAmountCoaDetails;

import java.util.ArrayList;

public class GlModBudgetAmountCoaDetails extends GlBudgetAmountCoaDetails implements java.io.Serializable {

   private String BC_COA_ACCNT_NMBR = null;
   private String BC_COA_ACCNT_DESC = null;
   private ArrayList glBcBudgetAmountPeriodList;

   public GlModBudgetAmountCoaDetails() {
   }
   
   public String getBcCoaAccountNumber() {
   	
   	   return BC_COA_ACCNT_NMBR;
   	
   }
   
   public void setBcCoaAccountNumber(String BC_COA_ACCNT_NMBR) {
   	
   	   this.BC_COA_ACCNT_NMBR = BC_COA_ACCNT_NMBR;
   	
   }
   
   public String getBcCoaAccountDescription() {
   	
   	   return BC_COA_ACCNT_DESC;
   	
   }
   
   public void setBcCoaAccountDescription(String BC_COA_ACCNT_DESC) {
   	
   	   this.BC_COA_ACCNT_DESC = BC_COA_ACCNT_DESC;
   	
   }

   
   public ArrayList getBcBudgetAmountPeriodList() {
   	
   	  return glBcBudgetAmountPeriodList;
   	
   }
   
   public void setBcBudgetAmountPeriodList(ArrayList glBcBudgetAmountPeriodList) {
   	
   	  this.glBcBudgetAmountPeriodList = glBcBudgetAmountPeriodList;
   	
   }

} // GlModBudgetAmountDetails class