/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.inv;

import com.util.inv.InvPhysicalInventoryDetails;

import java.util.ArrayList;

public class InvModPhysicalInventoryDetails extends InvPhysicalInventoryDetails implements java.io.Serializable {

	private String PI_LOC_NM;
	private ArrayList piPilList;
	private String PI_WSTG_ACCNT;
	private String PI_WSTG_ACCNT_DESC;
	private String PI_VRNC_ACCNT;
	private String PI_VRNC_ACCNT_DESC;

    public InvModPhysicalInventoryDetails() {
    }
    
    public String getPiLocName() {
    	
    	return PI_LOC_NM;
    	
    }
    
    public void setPiLocName(String PI_LOC_NM) {
    	
    	this.PI_LOC_NM = PI_LOC_NM;
    	
    }

	public ArrayList getPiPilList() {
		
		return piPilList;
		
	}
	
	public void setPiPilList(ArrayList piPilList) {
		
		this.piPilList = piPilList;
		
	}
	
	public void setPiWastageAccount(String PI_WSTG_ACCNT) {
		this.PI_WSTG_ACCNT = PI_WSTG_ACCNT;
	}
	
	public String getPiWastageAccount() {
		return PI_WSTG_ACCNT;
	}
	
	public void setPiWastageAccountDescription(String PI_WSTG_ACCNT_DESC) {
		this.PI_WSTG_ACCNT_DESC = PI_WSTG_ACCNT_DESC;
	}
	
	public String getPiWastageAccountDescription() {
		return PI_WSTG_ACCNT_DESC;
	}
	
	public void setPiVarianceAccount(String PI_VRNC_ACCNT) {
		this.PI_VRNC_ACCNT = PI_VRNC_ACCNT;
	}
	
	public String getPiVarianceAccount() {
		return PI_VRNC_ACCNT;
	}
	
	public void setPiVarianceAccountDescription(String PI_VRNC_ACCNT_DESC) {
		this.PI_VRNC_ACCNT_DESC = PI_VRNC_ACCNT_DESC;
	}
	
	public String getPiVarianceAccountDescription() {
		return PI_VRNC_ACCNT_DESC;
	}
	
} // InvModPhysicalInventoryDetails class