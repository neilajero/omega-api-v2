/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ap;

import com.util.ap.ApCanvassDetails;

public class ApModCanvassDetails extends ApCanvassDetails implements java.io.Serializable {
	
	private String CNV_SPL_SPPLR_CD = null;
	private String CNV_SPL_SPPLR_NAME = null;
	
	public ApModCanvassDetails() {
    }
	
	public String getCnvSplSupplierCode() {
		
		return CNV_SPL_SPPLR_CD;
		
	}
	
	public void setCnvSplSupplierCode(String CNV_SPL_SPPLR_CD) {
		
		this.CNV_SPL_SPPLR_CD = CNV_SPL_SPPLR_CD;
		
	}
	
	public String getCnvSplSupplierName() {
		
		return CNV_SPL_SPPLR_NAME;
		
	}
	
	public void setCnvSplSupplierName(String CNV_SPL_SPPLR_NAME) {
		
		this.CNV_SPL_SPPLR_NAME = CNV_SPL_SPPLR_NAME;
		
	}
	
}