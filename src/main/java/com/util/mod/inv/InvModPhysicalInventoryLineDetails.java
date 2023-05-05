/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.inv;

import com.util.inv.InvPhysicalInventoryLineDetails;

public class InvModPhysicalInventoryLineDetails extends InvPhysicalInventoryLineDetails implements java.io.Serializable {

    private String PIL_IL_II_NM;
    private String PIL_IL_LOC_NM; 
    private String PIL_IL_II_DESC;
    private String PIL_IL_II_UNT;
    private double PIL_RMNNG_QTY;
    private String PIL_UOM_NM;
    private String PIL_IL_MISC;

    public InvModPhysicalInventoryLineDetails() {
    }

	public String getPilIlIiName() {
		
		return PIL_IL_II_NM;
		
	}
	
	public void setPilIlIiName(String PIL_IL_II_NM) {
		
		this.PIL_IL_II_NM = PIL_IL_II_NM;
		
	}
	
	
public String getPilIlLocName() {
		
		return PIL_IL_LOC_NM;
		
	}
	
	public void setPilIlLocName(String PIL_IL_LOC_NM) {
		
		this.PIL_IL_LOC_NM = PIL_IL_LOC_NM;
		
	}
	
	public String getPilIlIiDescription() {
		
		return PIL_IL_II_DESC;
		
	}
	
	public void setPilIlIiDescription(String PIL_IL_II_DESC) {
		
		this.PIL_IL_II_DESC = PIL_IL_II_DESC;
		
	}
	
	public String getPilIlIiUnit() {
		
		return PIL_IL_II_UNT;
		
	}
	
	public void setPilIlIiUnit(String PIL_IL_II_UNT) {
		
		this.PIL_IL_II_UNT = PIL_IL_II_UNT;
		
	}
	
	public double getPilRemainingQuantity() {
		
		return PIL_RMNNG_QTY;
		
	}
	
	public void setPilRemainingQuantity(double PIL_RMNNG_QTY) {
		
		this.PIL_RMNNG_QTY = PIL_RMNNG_QTY;
		
	}
	 public String getPilUomName() {
	   	
	   	  return PIL_UOM_NM;
	   	
	 }
	   
	 public void setPilUomName(String PIL_UOM_NM) {
	   	
	   	  this.PIL_UOM_NM = PIL_UOM_NM;
	   	
	 }
	 
	 public String getPilMisc() {
		 return PIL_IL_MISC;
	 }
	 
	 public void setPilMisc(String PIL_IL_MISC) {
		 this.PIL_IL_MISC = PIL_IL_MISC;
	 }
	
} // InvModPhysicalInventoryLineDetails class