/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.inv;


import com.util.inv.InvLineItemDetails;

public class InvModLineItemDetails extends InvLineItemDetails implements java.io.Serializable {

   String LI_UOM_NM = null;
   String LI_LOC_NM = null; 
   String LI_II_NM = null;
   String LI_II_DESC = null;
   
   public InvModLineItemDetails() {
   }
   
   public String getLiUomName() {
   	
   	  return LI_UOM_NM;
   	
   }
   
   public void setLiUomName(String LI_UOM_NM) {
   	
   	  this.LI_UOM_NM = LI_UOM_NM;
   	
   }
   public String getLiLocName() {
   	
   	  return LI_LOC_NM;
   	
   }
   
   public void setLiLocName(String LI_LOC_NM) {
   	
   	  this.LI_LOC_NM = LI_LOC_NM;
   	
   }
   
   public String getLiIiName() {
   	
   	  return LI_II_NM;
   	
   }
   
   public void setLiIiName(String LI_II_NM) {
   	
   	  this.LI_II_NM = LI_II_NM;
   	
   }
   
   public String getLiIiDescription() {
   	
   	  return LI_II_DESC;
   	
   }
   
   public void setLiIiDescription(String LI_II_DESC) {
   	
   	  this.LI_II_DESC = LI_II_DESC;
   	
   }
   
} // InvModLineItemDetails class