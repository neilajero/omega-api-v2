package com.util.cm;

import com.ejb.entities.cm.LocalCmAdjustment;


public class CmAdjustmentDetails extends LocalCmAdjustment implements java.io.Serializable{

    public CmAdjustmentDetails(){}
    
    public String errorMessage = null;
    
    public String getErrorMessage(){
    	return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage){
    	this.errorMessage = errorMessage;
    }
    

}