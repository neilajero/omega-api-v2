/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;


import com.util.ar.ArJobOrderTypeDetails;

public class ArModJobOrderTypeDetails extends ArJobOrderTypeDetails implements java.io.Serializable {


	
    private String JOT_TC_NM;
    private String JOT_WTC_NM;
    
    private String JOT_GL_COA_JB_ORDR_ACCNT_NMBR;
    private String JOT_GL_COA_JB_ORDR_ACCNT_DESC;
    


    public ArModJobOrderTypeDetails() {
    }
        
   
    
    
    public String getJotTcName() {
    	
    	return JOT_TC_NM;
    	
    }
    
    public void setJotTcName(String JOT_TC_NM) {
    	
    	this.JOT_TC_NM = JOT_TC_NM;
    	
    }
    
    public String getJotWtcName() {
    	
    	return JOT_WTC_NM;
    	
    }
    
    public void setCcWtcName(String JOT_WTC_NM) {
    	
    	this.JOT_WTC_NM = JOT_WTC_NM;
    	
    }   

    public String getJotGlCoaJobOrderAccountNumber() {
   	
   	    return JOT_GL_COA_JB_ORDR_ACCNT_NMBR;
   	
    }
   
    public void setJotGlCoaJobOrderAccountNumber(String JOT_GL_COA_JB_ORDR_ACCNT_NMBR) {
   	
   	    this.JOT_GL_COA_JB_ORDR_ACCNT_NMBR = JOT_GL_COA_JB_ORDR_ACCNT_NMBR;
   	
    }
   
    public String getJotGlCoaJobOrderAccountDescription() {
   	
   	    return JOT_GL_COA_JB_ORDR_ACCNT_DESC;
   	 
    }
    
    public void setJotGlCoaJobOrderAccountDescription(String JOT_GL_COA_JB_ORDR_ACCNT_DESC) {
    	
    	this.JOT_GL_COA_JB_ORDR_ACCNT_DESC = JOT_GL_COA_JB_ORDR_ACCNT_DESC;
    	
    }
    
  
    
	    
    	    	    	      
} // ArModCustomerClassDetails class