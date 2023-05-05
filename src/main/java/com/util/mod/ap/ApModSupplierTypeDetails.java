/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ap;


import com.util.ap.ApSupplierTypeDetails;

public class ApModSupplierTypeDetails extends ApSupplierTypeDetails implements java.io.Serializable {

    private String ST_BA_NM;

    public ApModSupplierTypeDetails() {
    }

    public String getStBaName() {
    	
    	return ST_BA_NM;
    	
    }
    
    public void setStBaName(String ST_BA_NM) {
    	
    	this.ST_BA_NM = ST_BA_NM;
    	
    }        
    	    	      
} // ApModSupplierTypeDetails class