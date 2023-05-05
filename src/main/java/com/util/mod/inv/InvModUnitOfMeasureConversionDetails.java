/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.inv;


import com.util.inv.InvUnitOfMeasureConversionDetails;

public class InvModUnitOfMeasureConversionDetails extends InvUnitOfMeasureConversionDetails implements java.io.Serializable {
	
	// variables to hold umc list details : uom nm, uom symbol, uom class
	
	private String UOM_NM;
	private String UOM_SHRT_NM;
    private String UOM_AD_LV_CLSS;
    
    
    public InvModUnitOfMeasureConversionDetails() {
    }

    public String getUomName() {
    	
    	   return UOM_NM;
    	
    }  
    
    public void setUomName(String UOC_NM) {
    	
    	  this.UOM_NM = UOC_NM;
    	
    }
    
    
    public String getUomShortName() {
    	
    	   return UOM_SHRT_NM;
    	
    }  
    
    public void setUomShortName(String UOM_SHRT_NM) {
    	
    	  this.UOM_SHRT_NM = UOM_SHRT_NM;
    	
    }
    
    public String getUomAdLvClass() {
    	
    	   return UOM_AD_LV_CLSS;
    	
    }  
    
    public void setUomAdLvClass(String UOM_AD_LV_CLSS) {
    	
    	  this.UOM_AD_LV_CLSS = UOM_AD_LV_CLSS;
    	
    }
    
} // InvModUnitOfMeasureConversionDetails class