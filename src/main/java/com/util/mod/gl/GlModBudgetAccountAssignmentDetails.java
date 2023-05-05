/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;

import com.util.gl.GlBudgetAccountAssignmentDetails;

public class GlModBudgetAccountAssignmentDetails extends GlBudgetAccountAssignmentDetails implements java.io.Serializable {

   private String BAA_BO_NM;
   private String BAA_ACCNT_FRM_DESC;
   private String BAA_ACCNT_TO_DESC;

   public GlModBudgetAccountAssignmentDetails() {
   }


   public String getBaaBudgetOrganizationName() {
   	
   	  return BAA_BO_NM;
   	
   }
   
   public void setBaaBudgetOrganizationName(String BAA_BO_NM) {
   	
   	  this.BAA_BO_NM = BAA_BO_NM;
   	
   }
   
   public String getBaaAccountFromDescription() {
   	
   	  return BAA_ACCNT_FRM_DESC;
   	
   }
   
   public void setBaaAccountFromDescription(String BAA_ACCNT_FRM_DESC) {
   	
   	  this.BAA_ACCNT_FRM_DESC = BAA_ACCNT_FRM_DESC;
   	
   }
   
   public String getBaaAccountToDescription() {
   	
   	  return BAA_ACCNT_TO_DESC;
   	
   }
   
   public void setBaaAccountToDescription(String BAA_ACCNT_TO_DESC) {
   	
   	  this.BAA_ACCNT_TO_DESC = BAA_ACCNT_TO_DESC;
   	
   }

} // GlModBudgetAccountAssignmentDetails class