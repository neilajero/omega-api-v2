/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;


import com.util.ar.ArPersonelDetails;

public class ArModPersonelDetails extends ArPersonelDetails implements java.io.Serializable ,  Cloneable{

    private String PE_PT_SHRT_NM;
    private String PE_PT_NM;
    
    
    

    
    public ArModPersonelDetails () {
    }
    
  
    public Object clone() throws CloneNotSupportedException {
     	 

 		try {
 		   return super.clone();
 		 }
 		  catch (CloneNotSupportedException e) {
 			  throw e;
 		
 		  }	
 	 }
    
    
    public String getPePtShortName() {
    	return PE_PT_SHRT_NM;
    }
    
    public void setPePtShortName(String PE_PT_SHRT_NM) {
    	this.PE_PT_SHRT_NM = PE_PT_SHRT_NM;
    }
    
    
    public String getPePtName() {
    	return PE_PT_NM;
    }
    
    public void setPePtName(String PE_PT_NM) {
    	this.PE_PT_NM = PE_PT_NM;
    }

    
} // ArModCustomerDetails class   