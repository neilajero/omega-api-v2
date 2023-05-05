/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;

import com.util.gl.GlUserStaticReportDetails;

import java.util.Date;


public class GlModUserStaticReportDetails extends GlUserStaticReportDetails implements java.io.Serializable {
	
	private Integer USTR_USR_CODE;
	private String USTR_USR_NM;
	private Integer USTR_SR_CODE;
	private String USTR_SR_NM;
	private String USTR_SR_FL_NM;
	private Date USTR_SR_DT_TO;

	public GlModUserStaticReportDetails () {
    }

	public Integer getUstrUsrCode() {
		
		return USTR_USR_CODE;
		
	}
	
	public void setUstrUsrCode(Integer USTR_USR_CODE) {
		
		this.USTR_USR_CODE = USTR_USR_CODE;
		
	}
	
	public String getUstrUsrName() {
		
		return USTR_USR_NM;
		
	}
	
	public void setUstrUsrName(String USTR_USR_NM) {
		
		this.USTR_USR_NM = USTR_USR_NM;
		
	}
	
	public String getUstrSrName() {
		
		return USTR_SR_NM;
		
	}
	
	public void setUstrSrName(String USTR_SR_NM) {
		
		this.USTR_SR_NM = USTR_SR_NM;
		
	}
	
	public Integer getUstrSrCode() {
		
		return USTR_SR_CODE;
		
	}
	
	public void setUstrSrCode(Integer USTR_SR_CODE) {
		
		this.USTR_SR_CODE = USTR_SR_CODE;
		
	}
	
	public void setUstrSrFlName(String USTR_SR_FL_NM) {
		
		this.USTR_SR_FL_NM = USTR_SR_FL_NM;
		
	}
	
	public String getUstrSrFlName() {
		
		return USTR_SR_FL_NM;
		
	}
	
	public void setUstrSrDateTo(Date USTR_SR_DT_TO) {
		
		this.USTR_SR_DT_TO = USTR_SR_DT_TO;
		
	}
	
	public Date getUstrSrDateTo() {
		
		return USTR_SR_DT_TO;
		
	}
	
}

// GlModUserStaticDetails class   