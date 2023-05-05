/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.ad;

import java.util.Date;


public class AdRepUserListDetails implements java.io.Serializable {

	private String UL_USR_NM;
	private String UL_USR_DESC;
	private short UL_PSSWRD_EXPRTN_CD;
	private short UL_PSSWRD_EXPRTN_DYS;
	private short UL_PSSWRD_EXPRTN_ACCSS;
	private Date UL_DT_FRM;
	private Date UL_DT_TO;
	private String UL_RSPNSBLTY_NM;
	private String UL_RSPNSBLTY_DESC;
	private Date UL_RSPNSBLTY_DT_FRM;
	private Date UL_RSPNSBLTY_DT_TO;
	
	public AdRepUserListDetails() {
    }
	
	public Date getUlDateFrom() {
		
		return UL_DT_FRM;
		
	}
	
	public void setUlDateFrom(Date UL_DT_FRM) {
	
		this.UL_DT_FRM = UL_DT_FRM;
	
	}
	
	public Date getUlDateTo() {
		
		return UL_DT_TO;
		
	}
	
	public void setUlDateTo(Date UL_DT_TO) {
		
		this.UL_DT_TO = UL_DT_TO;
		
	}
	
	public short getUlPasswordExpirationAccess() {
		
		return UL_PSSWRD_EXPRTN_ACCSS;
		
	}
	
	public void setUlPasswordExpirationAccess(short UL_PSSWRD_EXPRTN_ACCSS) {
		
		this.UL_PSSWRD_EXPRTN_ACCSS = UL_PSSWRD_EXPRTN_ACCSS;
		
	}
	
	public short getUlPasswordExpirationCode() {
		
		return UL_PSSWRD_EXPRTN_CD;
		
	}
	
	public void setUlPasswordExpirationCode(short UL_PSSWRD_EXPRTN_CD) {
		
		this.UL_PSSWRD_EXPRTN_CD = UL_PSSWRD_EXPRTN_CD;
		
	}
	
	public short getUlPasswordExpirationDays() {
		
		return UL_PSSWRD_EXPRTN_DYS;
		
	}
	
	public void setUlPasswordExpirationDays(short UL_PSSWRD_EXPRTN_DYS) {
		
		this.UL_PSSWRD_EXPRTN_DYS = UL_PSSWRD_EXPRTN_DYS;
		
	}
	
	public String getUlResponsibilityDescription() {
		
		return UL_RSPNSBLTY_DESC;
		
	}
	
	public void setUlResponsibilityDescription(String UL_RSPNSBLTY_DESC) {
		
		this.UL_RSPNSBLTY_DESC = UL_RSPNSBLTY_DESC;
		
	}
	
	public Date getUlResponsibilityDateFrom() {
		
		return UL_RSPNSBLTY_DT_FRM;
		
	}
	
	public void setUlResponsibilityDateFrom(Date UL_RSPNSBLTY_DT_FRM) {
		
		this.UL_RSPNSBLTY_DT_FRM = UL_RSPNSBLTY_DT_FRM;
		
	}
	
	public Date getUlResponsibilityDateTo() {
		
		return UL_RSPNSBLTY_DT_TO;
		
	}
	
	public void setUlResponsibilityDateTo(Date UL_RSPNSBLTY_DT_TO) {
		
		this.UL_RSPNSBLTY_DT_TO = UL_RSPNSBLTY_DT_TO;
		
	}
	
	public String getUlResponsibilityName() {
		
		return UL_RSPNSBLTY_NM;
		
	}
	
	public void setUlResponsibilityName(String UL_RSPNSBLTY_NM) {
		
		this.UL_RSPNSBLTY_NM = UL_RSPNSBLTY_NM;
		
	}
	
	public String getUlUserDescription() {
		
		return UL_USR_DESC;
		
	}
	
	public void setUlUserDescription(String UL_USR_DESC) {
		
		this.UL_USR_DESC = UL_USR_DESC;
		
	}
	
	public String getUlUserName() {
		
		return UL_USR_NM;
		
	}
	
	public void setUlUserName(String UL_USR_NM) {
		
		this.UL_USR_NM = UL_USR_NM;
		
	}
	
} // AdRepUserListDetails class   