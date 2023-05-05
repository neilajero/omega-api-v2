/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.inv;

import com.util.inv.InvTagDetails;

public class InvModTagListDetails extends InvTagDetails implements java.io.Serializable {

  
   String TG_CSTDN = null;
  
   
   //ArrayList plTagList;
   
   public InvModTagListDetails() {
   }

   
   
   public String getTgCustodian() {
	   	
	   return TG_CSTDN;
	   	  
   }
	   
   public void setTgCustodian(String TG_CSTDN) {
	   	
	   this.TG_CSTDN = TG_CSTDN;
	   
   }
   
   
   
   
   
} // ApModPurchaseOrderLineDetails class