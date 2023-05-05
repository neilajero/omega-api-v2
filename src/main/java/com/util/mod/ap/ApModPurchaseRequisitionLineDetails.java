/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ap;

import com.util.ap.ApPurchaseRequisitionLineDetails;

import java.util.ArrayList;
import java.util.Date;

public class ApModPurchaseRequisitionLineDetails extends ApPurchaseRequisitionLineDetails implements java.io.Serializable {
	
	private String PRL_PR_NMBR = null;
	private Date PRL_PR_DT = null;
	private String PRL_IL_II_NM = null;
	private String PRL_IL_LOC_NM = null;
	private String PRL_IL_II_DESC = null;
	private double PRL_RRDR_PNT;
	private double PRL_RRDR_QNTTY;
	private double PRL_BDGT_QNTTY;
	private String PRL_UOM_NM = null;
	private boolean regenerateCanvass = false;
	private String PRL_MISC = null;
	private ArrayList PRL_TG_LST;
	private byte PRL_TRC_MSC;
	private String PR_APPRVL_STATUS = null;
	
	private ArrayList cnvList = new ArrayList();
	
	public byte getTraceMisc() {
		
		return PRL_TRC_MSC;
		
	}
	
	public void setTraceMisc(byte PRL_TRC_MSC) {
		
		this.PRL_TRC_MSC = PRL_TRC_MSC;
		
	}
	
	public String getPrlPrNumber() {
		
		return PRL_PR_NMBR;
		
	}
	
	public void setPrlPrNumber(String PRL_PR_NMBR) {
		
		this.PRL_PR_NMBR = PRL_PR_NMBR;
		
	}

	public double getPrlReorderPoint() {
		
		return PRL_RRDR_PNT;
		
	}
	
	public void setPrlReorderPoint(double PRL_RRDR_PNT) {
		
		this.PRL_RRDR_PNT = PRL_RRDR_PNT;
		
	}
	
	public double getPrlReorderQuantity() {
		
		return PRL_RRDR_QNTTY;
		
	}
	
	public void setPrlReorderQuantity(double PRL_RRDR_QNTTY) {
		
		this.PRL_RRDR_QNTTY = PRL_RRDR_QNTTY;
		
	}
	
	
	
	public double getPrlBdgtQuantity() {
		
		return PRL_BDGT_QNTTY;
		
	}
	
	public void setPrlBdgtQuantity(double PRL_BDGT_QNTTY) {
		
		this.PRL_BDGT_QNTTY = PRL_BDGT_QNTTY;
		
	}
	
	public ArrayList getPrlTagList() {
		   	
		return PRL_TG_LST;
		   	  
	}
		   
	public void setPrlTagList(ArrayList PRL_TG_LST) {
		   	
		this.PRL_TG_LST = PRL_TG_LST;
		   	   
	}
	
	public String getPrlMisc() {

		return PRL_MISC;

	}

	public void setPrlMisc(String PRL_MISC) {

		this.PRL_MISC = PRL_MISC;

	}
	
	
	public Date getPrlPrDate() {
		
		return PRL_PR_DT;
		
	}
	
	public void setPrlPrDate(Date PRL_PR_DT) {
		
		this.PRL_PR_DT = PRL_PR_DT;
		
	}
	
	public String getPrlIlIiName() {
		
		return PRL_IL_II_NM;
		
	}
	
	public void setPrlIlIiName(String PRL_IL_II_NM) {
		
		this.PRL_IL_II_NM = PRL_IL_II_NM;
		
	}
	
	public String getPrlIlLocName() {
		
		return PRL_IL_LOC_NM;
		
	}
	
	public void setPrlIlLocName(String PRL_IL_LOC_NM) {
		
		this.PRL_IL_LOC_NM = PRL_IL_LOC_NM;
		
	}
	
	public String getPrlIlIiDescription() {
		
		return PRL_IL_II_DESC;
		
	}
	
	public void setPrlIlIiDescription(String PRL_IL_II_DESC) {
		
		this.PRL_IL_II_DESC = PRL_IL_II_DESC;
		
	}
	
	public String getPrlUomName() {
		
		return PRL_UOM_NM;
		
	}
	
	public void setPrlUomName(String PRL_UOM_NM) {
		
		this.PRL_UOM_NM = PRL_UOM_NM;
		
	}
	
	public boolean getRegenerateCanvass() {
		
		return regenerateCanvass;
		
	}
	
	public void setRegenerateCanvass(boolean regenerateCanvass) {
		
		this.regenerateCanvass = regenerateCanvass;
		
	}
	
	public String getPrApprovalStatus() {
		
		return PR_APPRVL_STATUS;
		
	}
	
	public void setPrApprovalStatus(String PR_APPRVL_STATUS) {
		
		this.PR_APPRVL_STATUS = PR_APPRVL_STATUS;
		
	}
	
	public ArrayList getCnvList() {
		
		return cnvList;
		
	}
	
	public void setCnvList(ArrayList cnvList) {
		
		this.cnvList = cnvList;
		
	}
	
	public void saveCnvList(ApModCanvassDetails mdetails) {
		
		cnvList.add(mdetails);
		
	}
	
	public void clearCnvList() {
		
		cnvList.clear();
		
	}
	
}