/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;

import com.util.ar.ArJobOrderLineDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ArModJobOrderLineDetails extends ArJobOrderLineDetails implements Serializable {

	private String JOL_JO_DCMNT_NMBR = null;
	private Date JOL_JO_DT = null;
	
	
	private boolean JO_VD = false;
	private boolean JO_PSTD = false;
	private String JOL_UOM_NM = null;
    private String JOL_LOC_NM = null;
    private String JOL_II_NM = null;
    private String JOL_II_DESC = null;
    private String JOL_II_eDESC = null;
    private double JOL_QTY_DLVRD = 0d;
    private double JOL_RMNNG = 0d;
    private boolean JOL_ISSUE = false;
    private boolean JOL_II_SRVCS = false;
    private boolean JOL_II_JB_SRVCS = false;
    private double JOL_TTL_HRS = 0d;
    private double JOL_TTL_RT_PR_HRS = 0d;
    byte JOL_TRC_MSC;
    ArrayList jaList = new ArrayList();
    ArrayList jolTagList = new ArrayList();

    
    
    public String getJolJoDocumentNumber() {
    	return JOL_JO_DCMNT_NMBR;
    }
    
    public void setJolJoDocumentNumber(String JOL_JO_DCMNT_NMBR) {
    	this.JOL_JO_DCMNT_NMBR = JOL_JO_DCMNT_NMBR;
    }
    
    public Date setJolJoDate() {
    	return JOL_JO_DT;
    }
    
    public void setJolJoDate(Date JOL_JO_DT) {
    	this.JOL_JO_DT = JOL_JO_DT;
    }
    
    public boolean getJoVoid() {
    	return JO_VD;
    }
    
    public void setJoVoid(boolean JO_VD) {
    	this.JO_VD = JO_VD;
    }
    
    public boolean getJoPosted() {
    	return JO_PSTD;
    }
    
    public void setJoPosted(boolean JO_PSTD) {
    	this.JO_PSTD = JO_PSTD;
    }
    
    public byte getTraceMisc() {

 	   return JOL_TRC_MSC;

    }

    public void setTraceMisc(byte JOL_TRC_MSC) {

 	   this.JOL_TRC_MSC = JOL_TRC_MSC;

    }


    public ArrayList getJolTagList() {

 	   return jolTagList;

    }

    public void setJolTagList(ArrayList jolTagList) {

 	   this.jolTagList = jolTagList;

    }

    public ArModJobOrderLineDetails() {
    }

    public String getJolUomName() {

        return JOL_UOM_NM;

    }

    public void setJolUomName(String JOL_UOM_NM) {

        this.JOL_UOM_NM = JOL_UOM_NM;

    }

    public String getJolLocName() {

        return JOL_LOC_NM;

    }

    public void setJolLocName(String JOL_LOC_NM) {

        this.JOL_LOC_NM = JOL_LOC_NM;

    }

    public String getJolIiName() {

        return JOL_II_NM;

    }

    public void setJolIiName(String JOL_II_NM) {

        this.JOL_II_NM = JOL_II_NM;

    }

    public String getJolIiDescription() {

        return JOL_II_DESC;

    }

    public void setJolIiDescription(String JOL_II_DESC) {

        this.JOL_II_DESC = JOL_II_DESC;

    }

    public String getJolEDesc() {

        return JOL_II_eDESC;

    }

    public void setJolEDesc(String JOL_II_eDESC) {

        this.JOL_II_eDESC = JOL_II_eDESC;

    }

    public double getJolRemaining() {

        return JOL_RMNNG;

    }

    public void setJolRemaining(double JOL_RMNNG) {

        this.JOL_RMNNG = JOL_RMNNG;

    }
    
    
    public double getJolTotalHours() {

        return JOL_TTL_HRS;

    }

    public void setJolTotalHours(double JOL_TTL_HRS) {

        this.JOL_TTL_HRS = JOL_TTL_HRS;

    }
    
    
    public double getJolTotalRatePerHours() {

        return JOL_TTL_RT_PR_HRS;

    }

    public void setJolTotalRatePerHours(double JOL_TTL_RT_PR_HRS) {

        this.JOL_TTL_RT_PR_HRS = JOL_TTL_RT_PR_HRS;

    }

    public boolean getJolIssue() {

        return JOL_ISSUE;

    }

    public void setJolIssue(boolean JOL_ISSUE) {

        this.JOL_ISSUE = JOL_ISSUE;

    }
    
    public boolean getJolIiServices() {

        return JOL_II_SRVCS;

    }

    public void setJolIiServices(boolean JOL_II_SRVCS) {

        this.JOL_II_SRVCS = JOL_II_SRVCS;

    }
    
    public boolean getJolIiJobServices() {

        return JOL_II_JB_SRVCS;

    }

    public void setJolIiJobServices(boolean JOL_II_JB_SRVCS) {

        this.JOL_II_JB_SRVCS = JOL_II_JB_SRVCS;

    }

    public double getJolQuantityDelivered() {

        return JOL_QTY_DLVRD;

    }

    public void setJolQuantityDelivered(double JOL_QTY_DLVRD) {

        this.JOL_QTY_DLVRD = JOL_QTY_DLVRD;

    }
    
    public ArrayList getJaList() {
		
		return jaList;
		
	}
	
	public void setJaList(ArrayList jaList) {
		
		this.jaList = jaList;
		
	}
	
	public void saveJaList(ArModJobOrderAssignmentDetails mdetails) {
		
		jaList.add(mdetails);
		
	}
	
	public void clearJaList() {
		
		jaList.clear();
		
	}

} // ArModJobOrderLineDetails