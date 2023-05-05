/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;

import java.io.Serializable;
import java.util.Date;

public class ApRepCanvassReportPrintDetails implements Serializable {
	
	private String CRP_PR_NMBR;
	private Date CRP_PR_DT;
	private String CRP_PR_DESC;
	private char CRP_PR_FC_SYMBL;
	private String CRP_PRL_II_NM;
	private String CRP_PRL_II_DESC;
	private String CRP_PRL_LOC_NM;
	private String CRP_PRL_UOM_NM;
	private String CRP_PRL_CNV_RMKS;
	private String CRP_PRL_CNV_SPL_SPPLR_CD;
	private String CRP_PRL_CNV_SPL_NM;
	private double CRP_PRL_CNV_QTTY;
	private double CRP_PRL_CNV_UNT_PRC;
	private double CRP_PRL_CNV_AMNT;
	private String CRP_PRL_LST_DLVRY_DT;
	private byte CRP_PRL_PO;
	
	public String getCrpPrNumber() {
		
		return CRP_PR_NMBR;
		
	}
	
	public void setCrpPrNumber(String CRP_PR_NMBR) {
		
		this.CRP_PR_NMBR = CRP_PR_NMBR;
		
	}
	
	public Date getCrpPrDate() {
		
		return CRP_PR_DT;
		
	}
	
	public void setCrpPrDate(Date CRP_PR_DT) {
		
		this.CRP_PR_DT = CRP_PR_DT;
		
	}
	
	public String getCrpPrDescription() {
		
		return CRP_PR_DESC;
		
	}
	
	public void setCrpPrDescription(String CRP_PR_DESC) {
		
		this.CRP_PR_DESC = CRP_PR_DESC;
		
	}
	
	public char getCrpPrFcSymbol() {
		
		return CRP_PR_FC_SYMBL;
		
	}
	
	public void setCrpPrFcSymbol(char CRP_PR_FC_SYMBL) {
		
		this.CRP_PR_FC_SYMBL = CRP_PR_FC_SYMBL;
		
	}
	
	public String getCrpPrlIiName() {
		
		return CRP_PRL_II_NM;
		
	}
	
	public void setCrpPrlIiName(String CRP_PRL_II_NM) {
		
		this.CRP_PRL_II_NM = CRP_PRL_II_NM;
		
	}
	
	public String getCrpPrlIiDescription() {
		
		return CRP_PRL_II_DESC;
		
	}
	
	public void setCrpPrlIiDescription(String CRP_PRL_II_DESC) {
		
		this.CRP_PRL_II_DESC = CRP_PRL_II_DESC;
		
	}
	
	public String getCrpPrlLocName() {
		
		return CRP_PRL_LOC_NM;
		
	}
	
	public void setCrpPrlLocName(String CRP_PRL_LOC_NM) {
		
		this.CRP_PRL_LOC_NM = CRP_PRL_LOC_NM;
		
	}
	
	public String getCrpPrlUomName() {
		
		return CRP_PRL_UOM_NM;
		
	}
	
	public void setCrpPrlUomName(String CRP_PRL_UOM_NM) {
		
		this.CRP_PRL_UOM_NM = CRP_PRL_UOM_NM;
		
	}
	
	public String getCrpPrlCnvRemarks() {
		
		return CRP_PRL_CNV_RMKS;
		
	}
	
	public void setCrpPrlCnvRemarks(String CRP_PRL_CNV_RMKS) {
		
		this.CRP_PRL_CNV_RMKS = CRP_PRL_CNV_RMKS;
		
	}
	
	public String getCrpPrlCnvSplSupplierCode() {
		
		return CRP_PRL_CNV_SPL_SPPLR_CD;
		
	}
	
	public void setCrpPrlCnvSplSupplierCode(String CRP_PRL_CNV_SPL_SPPLR_CD) {
		
		this.CRP_PRL_CNV_SPL_SPPLR_CD = CRP_PRL_CNV_SPL_SPPLR_CD;
		
	}
	
	public String getCrpPrlCnvSplName() {
		
		return CRP_PRL_CNV_SPL_NM;
		
	}
	
	public void setCrpPrlCnvSplName(String CRP_PRL_CNV_SPL_NM) {
		
		this.CRP_PRL_CNV_SPL_NM = CRP_PRL_CNV_SPL_NM;
		
	}
			
	public double getCrpPrlCnvQuantity() {
		
		return CRP_PRL_CNV_QTTY;
		
	}
	
	public void setCrpPrlCnvQuantity(double CRP_PRL_CNV_QTTY) {
		
		this.CRP_PRL_CNV_QTTY = CRP_PRL_CNV_QTTY;
		
	}
																
	public double getCrpPrlCnvUnitPrice() {
		
		return CRP_PRL_CNV_UNT_PRC;
		
	}
	
	public void setCrpPrlCnvUnitPrice(double CRP_PRL_CNV_UNT_PRC) {
		
		this.CRP_PRL_CNV_UNT_PRC = CRP_PRL_CNV_UNT_PRC;
		
	}
	
	public double getCrpPrlCnvAmount() {
		
		return CRP_PRL_CNV_AMNT;
		
	}
	
	public void setCrpPrlCnvAmount(double CRP_PRL_CNV_AMNT) {
		
		this.CRP_PRL_CNV_AMNT = CRP_PRL_CNV_AMNT;
		
	}
	
	public String getCrlPrlLastDeliveryDate() {
		
		return CRP_PRL_LST_DLVRY_DT;
		
	}
	
	public void setCrpPrlLastDeliveryDate(String CRP_PRL_LST_DLVRY_DT) {
		
		this.CRP_PRL_LST_DLVRY_DT = CRP_PRL_LST_DLVRY_DT;
		
	}
	
	public byte getCrlPrlPO() {
		
		return CRP_PRL_PO;
		
	}
	
	public void setCrpPrlPO(byte CRP_PRL_PO) {
		
		this.CRP_PRL_PO = CRP_PRL_PO;
		
	}
																			  
	
}