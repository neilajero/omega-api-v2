/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.ad;


public class AdRepApprovalListDetails implements java.io.Serializable {

	private String AL_DCMNT_TYP;
	private boolean AL_ENBL;
	private double AL_AMNT;
	private String AL_USR_NM;
	private String AL_USR_TYP;
	private String AL_AND_OR;
	
	public AdRepApprovalListDetails() {
    }
      
	public String getAlDocumentType() {
		
		return AL_DCMNT_TYP;
		
	}
	
	public void setAlDocumentType(String AL_DCMNT_TYP) {
		
		this.AL_DCMNT_TYP = AL_DCMNT_TYP;
		
	}
	
	public boolean getAlEnable() {
		
		return AL_ENBL;
		
	}
	
	public void setAlEnable(boolean AL_ENBL) {
		
		this.AL_ENBL = AL_ENBL;
		
	}
	
	public double getAlAmount() {
		
		return AL_AMNT;
		
	}
	
	public void setAlAmount(double AL_AMNT) {
		
		this.AL_AMNT = AL_AMNT;
		
	}
	
	public String getAlUserName() {
		
		return AL_USR_NM;
		
	}
	
	public void setAlUserName(String AL_USR_NM) {
		
		this.AL_USR_NM = AL_USR_NM;
		
	}
	
	public String getAlUserType() {
		
		return AL_USR_TYP;
		
	}
	
	public void setAlUserType(String AL_USR_TYP) {
		
		this.AL_USR_TYP = AL_USR_TYP;
		
	}
	
	public String getAlAndOr() {
		
		return AL_AND_OR;
		
	}
	
	public void setAlAndOr(String AL_AND_OR) {
		
		this.AL_AND_OR = AL_AND_OR;
		
	}
	
} // AdRepApprovalListDetails class   