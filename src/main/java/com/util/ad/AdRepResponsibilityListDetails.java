/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.ad;

import java.util.Date;


public class AdRepResponsibilityListDetails implements java.io.Serializable {

	private String RL_RSPNSBLTY_NM;
	private String RL_RSPNSBLTY_DESC;
	private Date RL_DT_FRM;
	private Date RL_DT_TO;
	private String RL_FRM_FNCTN;
	
	public AdRepResponsibilityListDetails() {
    }
	
	public String getRlResponsibilityName() {
		
		return RL_RSPNSBLTY_NM;
		
	}
	
	public void setRlResponsiblityName(String RL_RSPNSBLTY_NM) {
		
		this.RL_RSPNSBLTY_NM = RL_RSPNSBLTY_NM;
		
	}
	
	public String getRlResponsibilityDescription() {
		
		return RL_RSPNSBLTY_DESC;
		
	}
	
	public void setRlResponsibilityDescription(String RL_RSPNSBLTY_DESC) {
		
		this.RL_RSPNSBLTY_DESC = RL_RSPNSBLTY_DESC;
		
	}
	
	public Date getRlDateFrom() {
		
		return RL_DT_FRM;
		
	}
	
	public void setRlDateFrom(Date RL_DT_FRM) {
		
		this.RL_DT_FRM = RL_DT_FRM;
		
	}
	
	public Date getRlDateTo() {
		
		return RL_DT_TO;
		
	}
	
	public void setRlDateTo(Date RL_DT_TO) {
		
		this.RL_DT_TO = RL_DT_TO;
		
	}
	
	public String getRlFormFunction() {
		
		return RL_FRM_FNCTN;
		
	}
	
	public void setRlFormFunction(String RL_FRM_FNCTN) {
		
		this.RL_FRM_FNCTN = RL_FRM_FNCTN;
		
	}
	
} // AdRepResponsibilityListDetails class   