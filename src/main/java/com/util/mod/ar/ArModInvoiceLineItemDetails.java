/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;

import com.util.ar.ArInvoiceLineItemDetails;

import java.util.ArrayList;

public class ArModInvoiceLineItemDetails extends ArInvoiceLineItemDetails implements java.io.Serializable {

	String ILI_UOM_NM = null;
	String ILI_LOC_NM = null;
	String ILI_II_NM = null;
	String ILI_II_DESC = null;
	String ILI_II_CLSS = null;
	String ILI_RCPT_NMBR = null;
	String ILI_INV_DCMNT_NMBR = null;
	String ILI_MISC = null;
	String ILI_TX_CD = null;
	String ILI_TX_CD_NM = null;
	String ILI_WTX_CD_NM = null;
	String ILI_IMEI = null;
	Integer ILI_II_CD = null;
	String ILI_CMPNY_SHRT_NM = null;
	byte ILI_TRC_MSC;
	ArrayList iliTagList = new ArrayList();

	public ArModInvoiceLineItemDetails() {
    }

	public byte getTraceMisc() {

		return ILI_TRC_MSC;

	}

	public void setTraceMisc(byte ILI_TRC_MSC) {

		this.ILI_TRC_MSC = ILI_TRC_MSC;

	}

	public ArrayList getiIliTagList() {

		return iliTagList;

	}

	public void setIliTagList(ArrayList iliTagList) {

		this.iliTagList = iliTagList;

	}

	public String getILI_IMEI() {

		return ILI_IMEI;

	}

	public void setILI_IMEI(String iLI_IMEI) {

		ILI_IMEI = iLI_IMEI;
	}

	public String getIliCmpShrtName() {

		return ILI_CMPNY_SHRT_NM;

	}

	public void setIliCmpShrtName(String ILI_CMPNY_SHRT_NM) {

		this.ILI_CMPNY_SHRT_NM = ILI_CMPNY_SHRT_NM;

	}

	public String getIliUomName() {

		return ILI_UOM_NM;

	}

	public void setIliUomName(String ILI_UOM_NM) {

		this.ILI_UOM_NM = ILI_UOM_NM;

	}

	public String getIliLocName() {

		return ILI_LOC_NM;

	}

	public void setIliLocName(String ILI_LOC_NM) {

		this.ILI_LOC_NM = ILI_LOC_NM;

	}

	public String getIliIiName() {

		return ILI_II_NM;

	}

	public void setIliIiName(String ILI_II_NM) {

		this.ILI_II_NM = ILI_II_NM;

	}

	public String getIliIiClass() {

		return ILI_II_CLSS;

	}

	public void setIliIiClass(String ILI_II_CLSS) {

		this.ILI_II_CLSS = ILI_II_CLSS;

	}

	public String getIliIiDescription() {

		return ILI_II_DESC;

	}

	public void setIliIiDescription(String ILI_II_DESC) {

		this.ILI_II_DESC = ILI_II_DESC;

	}

	public String getIliReceiptNumber() {

		return ILI_RCPT_NMBR;

	}

	public void setIliReceiptNumber(String ILI_RCPT_NMBR) {

		this.ILI_RCPT_NMBR = ILI_RCPT_NMBR;

	}

	public String getIliInvDocumentNumber() {

		return ILI_INV_DCMNT_NMBR;

	}

	public void setIliInvDocumentNumber(String ILI_INV_DCMNT_NMBR) {

		this.ILI_INV_DCMNT_NMBR = ILI_INV_DCMNT_NMBR;

	}

	public String getIliMisc() {

		return ILI_MISC;

	}

	public void setIliMisc(String ILI_MISC) {

		this.ILI_MISC = ILI_MISC;

	}

	public String getIliTaxCode() {

		return ILI_TX_CD;

	}

	public void setIliTaxCode(String ILI_TX_CD) {

		this.ILI_TX_CD = ILI_TX_CD;

	}

	public String getIliTaxCodeName() {

		return ILI_TX_CD_NM;

	}

	public void setIliTaxCodeName(String ILI_TX_CD_NM) {

		this.ILI_TX_CD_NM = ILI_TX_CD_NM;

	}

	public String getIliWTaxCodeName() {

		return ILI_WTX_CD_NM;

	}

	public void setIliWTaxCodeName(String ILI_WTX_CD_NM) {

		this.ILI_WTX_CD_NM = ILI_WTX_CD_NM;

	}

} // ArModInvoiceLineItemDetails class