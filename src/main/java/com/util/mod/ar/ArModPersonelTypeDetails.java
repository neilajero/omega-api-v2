/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;


import com.util.ar.ArPersonelTypeDetails;

public class ArModPersonelTypeDetails extends ArPersonelTypeDetails implements java.io.Serializable, Cloneable {

	 public Object clone() throws CloneNotSupportedException {
     	 

	 		try {
	 		   return super.clone();
	 		 }
	 		  catch (CloneNotSupportedException e) {
	 			  throw e;
	 		
	 		  }	
	 	 }
	    
    	    	      
} // ArModCustomerTypeDetails class