package com.util.ad;

import com.ejb.entities.ad.LocalAdBranchResponsibility;


public class AdBranchResponsibilityDetails extends LocalAdBranchResponsibility implements java.io.Serializable{

    public AdBranchResponsibilityDetails(){}
    
    public Integer BR_CODE;
    public String BR_BRNCH_CODE;
    public String BR_NM;
    public byte BR_HD_QRTR;
    
    public Integer getBrCode(){
    	return BR_CODE;
    }
    
    public void setBrCode(Integer BR_CODE){
    	this.BR_CODE = BR_CODE;
    }
    
    
    public String getBrBranchCode(){
    	return BR_BRNCH_CODE;
    }
    
    public void setBrBranchCode(String BR_BRNCH_CODE){
    	this.BR_BRNCH_CODE = BR_BRNCH_CODE;
    }
    
    public String getBrName(){
    	return BR_NM;
    }
    
    public void setBrName(String BR_NM){
    	this.BR_NM = BR_NM;
    }
    
    public byte getBrHeadQuarter(){
    	return BR_HD_QRTR;
    }
    
    public void setBrHeadQuarter(byte BR_HD_QRTR){
    	this.BR_HD_QRTR = BR_HD_QRTR;
    }

}