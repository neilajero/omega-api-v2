/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;


import com.util.ar.ArJobOrderAssignmentDetails;

public class ArModJobOrderAssignmentDetails extends ArJobOrderAssignmentDetails implements java.io.Serializable {
	
	private Integer JA_PE_CODE = null;
	private String JA_PE_ID_NMBR = null;
	private String JA_PE_NM = null;
	
	
	public ArModJobOrderAssignmentDetails() {
    }
	
	
	
	
	public Integer getJaPeCode() {
		
		return JA_PE_CODE;
		
	}
	
	public void setJaPeCode(Integer JA_PE_CODE) {
		
		this.JA_PE_CODE = JA_PE_CODE;
		
	}
	
	
	public String getJaPeIdNumber() {
		
		return JA_PE_ID_NMBR;
		
	}
	
	public void setJaPeIdNumber(String JA_PE_ID_NMBR) {
		
		this.JA_PE_ID_NMBR = JA_PE_ID_NMBR;
		
	}
	
	public String getJaPeName() {
		
		return JA_PE_NM;
		
	}
	
	public void setJaPeName(String JA_PE_NM) {
		
		this.JA_PE_NM = JA_PE_NM;
		
	}
	
}