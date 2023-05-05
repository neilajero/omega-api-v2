/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;


import com.util.ar.ArCustomerTypeDetails;

public class ArModCustomerTypeDetails extends ArCustomerTypeDetails implements java.io.Serializable {

    private String CT_BA_NM;

    public ArModCustomerTypeDetails() {
    }

    public String getCtBaName() {
    	
    	return CT_BA_NM;
    	
    }
    
    public void setCtBaName(String CT_BA_NM) {
    	
    	this.CT_BA_NM = CT_BA_NM;
    	
    }        
    	    	      
} // ArModCustomerTypeDetails class