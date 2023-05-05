/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;

import com.util.ar.ArDeliveryDetails;

public class ArModDeliveryDetails extends ArDeliveryDetails implements java.io.Serializable, Cloneable {

	
	
	private String SO_DCMNT_NMBR;
	private String SO_RFRNC_NMBR;
	private String CST_NM;
	private Integer SO_CODE;
	private String SO_SHPPNG_LN;
	private String SO_PRT;
	private String SO_TRNSCTN_STTS;
	

    public ArModDeliveryDetails() {
    }

    public Object clone() throws CloneNotSupportedException {
    	 

		try {
		   return super.clone();
		 }
		  catch (CloneNotSupportedException e) {
			  throw e;
		
		  }	
	    }
    
    public Integer getSoCode() {
    	return SO_CODE;
    }
    
    public void setSoCode(Integer SO_CODE) {
    	this.SO_CODE =  SO_CODE;
    }
    
    
    public String getSoDocumentNumber() {
    	return SO_DCMNT_NMBR;
    }
    
    public void setSoDocumentNumber(String SO_DCMNT_NMBR) {
    	this.SO_DCMNT_NMBR =  SO_DCMNT_NMBR;
    }
    
    public String getSoReferenceNumber() {
    	return SO_RFRNC_NMBR;
    }
    
    public void setSoReferenceNumber(String SO_RFRNC_NMBR) {
    	this.SO_RFRNC_NMBR =  SO_RFRNC_NMBR;
    }
    
    public String getCstName() {
    	return CST_NM;
    }
    
    public void setCstName(String CST_NM) {
    	this.CST_NM =  CST_NM;
    }
    
    
    public String getSoShippingLine() {
    	return SO_SHPPNG_LN;
    }
    
    public void setSoShippingLine(String SO_SHPPNG_LN) {
    	this.SO_SHPPNG_LN =  SO_SHPPNG_LN;
    }
    
    
    public String getSoPort() {
    	return SO_PRT;
    }
    
    public void setSoPort(String SO_PRT) {
    	this.SO_PRT =  SO_PRT;
    }
    
    public String getSoTransactionStatus() {
    	return SO_TRNSCTN_STTS;
    }
    
    public void setSoTransactionStatus(String SO_TRNSCTN_STTS) {
    	this.SO_TRNSCTN_STTS =  SO_TRNSCTN_STTS;
    }
    

} // ArModDeliveryDetails class