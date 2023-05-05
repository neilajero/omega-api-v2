/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ap;

import com.util.ap.ApPurchaseOrderDetails;

import java.util.ArrayList;

public class ApModPurchaseOrderDetails extends ApPurchaseOrderDetails implements java.io.Serializable {

   private String PO_FC_NM;
   private String PO_TC_NM;
   private String PO_TC_TYP;
   private double PO_TC_RT;
   private String PO_SPL_SPPLR_CODE;
   private String PO_BTCH_NM;
   private String PO_SPL_TIN;
   private String PO_SPL_ADDRSS;
   private String PO_PYT_NM;
   private double PO_RMNNG;
   private String PO_SPL_NM;
   private String PO_BRNCH_CODE;
   private String PO_STATUS;
   private Integer PO_PR_CODE;
   private ArrayList poPlList;
   
   private ArrayList poAPRList = new ArrayList();

   public ApModPurchaseOrderDetails() {
   }

   public String getPoFcName() {
   	
   	  return PO_FC_NM;
   	
   }
   
   public void setPoFcName(String PO_FC_NM) {
   	
   	  this.PO_FC_NM = PO_FC_NM;
   	
   }
   
   public String getPoTcName() {
   	
   	  return PO_TC_NM;
   	
   }
   
   public void setPoTcName(String PO_TC_NM) {
   	
   	  this.PO_TC_NM = PO_TC_NM;
   	
   }
   
   public String getPoSplSupplierCode() {
   	
   	  return PO_SPL_SPPLR_CODE;
   	
   }
   
   public void setPoSplSupplierCode(String PO_SPL_SPPLR_CODE) {
   	
   	  this.PO_SPL_SPPLR_CODE = PO_SPL_SPPLR_CODE;
   	
   }
   
   public String getPoBatchName() {
   	
   	  return PO_BTCH_NM;
   	
   }
   
   public void setPoBatchName(String PO_BTCH_NM) {
   	
   	  this.PO_BTCH_NM = PO_BTCH_NM;
   	
   }
   
   public String getPoSplTin() {
   	
   	  return PO_SPL_TIN;
   	  
   }
   
   public void setPoSplTin(String PO_SPL_TIN) {
   	
   	  this.PO_SPL_TIN = PO_SPL_TIN;
   	  
   }

   public String getPoSplAddress() {
   	
   	  return PO_SPL_ADDRSS;
   	  
   }
   
   public void setPoSplAddress(String PO_SPL_ADDRSS) {
   	
   	  this.PO_SPL_ADDRSS = PO_SPL_ADDRSS;
   	  
   }
   
   public String getPoPytName() {
   	
   	  return PO_PYT_NM;
   	
   }
   
   public void setPoPytName(String PO_PYT_NM) {
   	
   	  this.PO_PYT_NM = PO_PYT_NM;
   	
   }
      
   public ArrayList getPoPlList() {
   	
   	  return poPlList;
   	  
   }
   
   public void setPoPlList(ArrayList poPlList) {
   	
   	   this.poPlList = poPlList;
   	   
   }
   
   public double getPoRemaining() {
   	
   		return PO_RMNNG;
   	
   }
   
   public void setPoRemaining(double PO_RMNNG) {
   	
   		this.PO_RMNNG = PO_RMNNG;
   	
   }
   
   public String getPoSplName() {
   	
   	  return PO_SPL_NM;
   	
   }
   
   public void setPoSplName(String PO_SPL_NM) {
   	
   	  this.PO_SPL_NM = PO_SPL_NM;
   	
   }
   
   public String getPoTcType() {
   	
   	  return PO_TC_TYP;
   	
   }
   
   public void setPoTcType(String PO_TC_TYP) {
   	
   	  this.PO_TC_TYP = PO_TC_TYP;
   	
   }
   
   public double getPoTcRate() {
   	
   	  return PO_TC_RT;
   	
   }
   
   public void setPoTcRate(double PO_TC_RT) {
   	
   	  this.PO_TC_RT = PO_TC_RT;
   	
   }
   
   
   public String getPoBranchCode() {
    
      return PO_BRNCH_CODE;
    
   }
   
   public void setPoBranchCode(String PO_BRNCH_CODE) {
    
      this.PO_BRNCH_CODE = PO_BRNCH_CODE;
    
   }
   
   public String getPoStatus() {
    
      return PO_STATUS;
    
   }
   
   public void setPoStatus(String PO_STATUS) {
    
      this.PO_STATUS = PO_STATUS;
    
   }
   
    public Integer getPoPrCode() {
    
      return PO_PR_CODE;
    
   }
   
   public void setPoPrCode(Integer PO_PR_CODE) {
    
      this.PO_PR_CODE = PO_PR_CODE;
    
   }
   
      public ArrayList getPoAPRList() {
		
		return poAPRList;
		
	}
	
	public void setPoAPRList(ArrayList poAPRList) {
		
		this.poAPRList = poAPRList;
		
	}


} // ApModPurchaseOrderDetails class