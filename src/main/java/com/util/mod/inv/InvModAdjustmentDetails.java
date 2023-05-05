/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.inv;

import com.util.inv.InvAdjustmentDetails;

import java.util.ArrayList;

public class InvModAdjustmentDetails extends InvAdjustmentDetails implements java.io.Serializable {

    private String ADJ_COA_ACCNT_NMBR;
    private String NOTED_BY;
    private String ADJ_COA_ACCNT_DESC;
    private String ADJ_SPL_SPPLR_CODE;
    private String ADJ_SPL_SPPLR_NM;
    private ArrayList ADJ_AL_LST;
    private double ADJ_AMNT;
    private boolean ADJ_PRCHS_UNIT;
    
    private ArrayList ADJ_APR_LST;

    public InvModAdjustmentDetails() {
    }

    public String getAdjSplSupplierCode() {
       	
     	  return ADJ_SPL_SPPLR_CODE;
     	
     }
     
     public void setAdjSplSupplierCode(String ADJ_SPL_SPPLR_CODE) {
     	
     	  this.ADJ_SPL_SPPLR_CODE = ADJ_SPL_SPPLR_CODE;
     	
     }
     
     public String getAdjSplSupplierName() {
        	
    	  return ADJ_SPL_SPPLR_NM;
    	
    }
    
    public void setAdjSplSupplierName(String ADJ_SPL_SPPLR_NM) {
    	
    	  this.ADJ_SPL_SPPLR_NM = ADJ_SPL_SPPLR_NM;
    	
    }

	public String getAdjCoaAccountNumber() {
		
		return ADJ_COA_ACCNT_NMBR;
		
	}
	
	public void setAdjCoaAccountNumber(String ADJ_COA_ACCNT_NMBR) {
		
		this.ADJ_COA_ACCNT_NMBR = ADJ_COA_ACCNT_NMBR;
		
	}
	
	public String getNotedBy() {
		
		return NOTED_BY;
		
	}
	
	public void setNotedBy(String NOTED_BY) {
		
		this.NOTED_BY = NOTED_BY;
		
	}
	
	public String getAdjCoaAccountDescription() {
		
		return ADJ_COA_ACCNT_DESC;
		
	}
	
	public void setAdjCoaAccountDescription(String ADJ_COA_ACCNT_DESC) {
		
		this.ADJ_COA_ACCNT_DESC = ADJ_COA_ACCNT_DESC;
		
	}
	
	public ArrayList getAdjAlList() {
		
		return ADJ_AL_LST;
		
	}
	
	public void setAdjAlList(ArrayList ADJ_AL_LST) {
		
		this.ADJ_AL_LST = ADJ_AL_LST;
		
	}
	
	public double getAdjAmount() {
		
		return ADJ_AMNT;
		
	}
	
	public void setAdjAmount(double ADJ_AMNT) {
		
		this.ADJ_AMNT = ADJ_AMNT;
		
	}
	
	public boolean getAdjPurchaseUnit() {
		
		return ADJ_PRCHS_UNIT;
		
	}
	
	public void setAdjPurchaseUnit(boolean ADJ_PRCHS_UNIT) {
		
		this.ADJ_PRCHS_UNIT = ADJ_PRCHS_UNIT;
		
	}
	
	public ArrayList getAdjAPRList() {
		
		return ADJ_APR_LST;
		
	}
	
	public void setAdjAPRList(ArrayList ADJ_APR_LST) {
		
		this.ADJ_APR_LST = ADJ_APR_LST;
		
	}

} // InvModAdjustmentDetails class