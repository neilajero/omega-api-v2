/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ap;

import com.util.mod.inv.InvModTagListDetails;
import com.util.ap.ApPurchaseOrderLineDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ApModPurchaseOrderLineDetails extends ApPurchaseOrderLineDetails implements java.io.Serializable {

   String PL_UOM_NM = null;
   String PL_LOC_NM = null;
   String PL_II_NM = null;
   String PL_II_DESC = null;
   String PL_CURRENCY = null;

   double PL_PO_QTY;
   double PL_PO_AMT;
   double PL_PO_CNVRSN_RT;
   Date PL_PO_CNVRSN_DT = null;
   String PL_II_PRT_NMBR = null;
   String PL_PO_DCMNT_NMBR = null;
   String PL_PO_RCV_PO_NMBR = null;
   String PL_MISC = null;
   String PL_REPORT2 = null;
   double PL_RMNNG;
   double PL_CNVRSN_FCTR;
   byte PL_TRC_MSC;
   private List<InvModTagListDetails> plTagList = new ArrayList();

   String PL_MISC1 = null;
   String PL_MISC2 = null;
   String PL_MISC3 = null;
   String PL_MISC4 = null;
   String PL_MISC5 = null;
   String PL_MISC6 = null;
   
   String PL_PO_SPPLR_CODE = null;
   
   Integer PL_PO_CODE = null;

   public ApModPurchaseOrderLineDetails() {
   }

   public byte getTraceMisc() {

	   return PL_TRC_MSC;

   }

   public void setTraceMisc(byte PL_TRC_MSC) {

	   this.PL_TRC_MSC = PL_TRC_MSC;

   }

   public String getPlPoDocumentNumber() {

   	  return PL_PO_DCMNT_NMBR;

   }

   public void setPlPoDocumentNumber(String PL_PO_DCMNT_NMBR) {

   	  this.PL_PO_DCMNT_NMBR = PL_PO_DCMNT_NMBR;

   }

   public String getPlPoReceivingPoNumber() {

   	  return PL_PO_RCV_PO_NMBR;

   }

   public void setPlPoReceivingPoNumber(String PL_PO_RCV_PO_NMBR) {

   	  this.PL_PO_RCV_PO_NMBR = PL_PO_RCV_PO_NMBR;

   }

   public String getPlUomName() {

   	  return PL_UOM_NM;

   }

   public void setPlUomName(String PL_UOM_NM) {

   	  this.PL_UOM_NM = PL_UOM_NM;

   }

   public List<InvModTagListDetails> getPlTagList() {

	   return plTagList;

   }

   public void setPlTagList(List<InvModTagListDetails> plTagList) {

	   this.plTagList = plTagList;

   }


   public String getPlLocName() {

   	  return PL_LOC_NM;

   }

   public void setPlLocName(String PL_LOC_NM) {

   	  this.PL_LOC_NM = PL_LOC_NM;

   }

   public String getPlIiName() {

   	  return PL_II_NM;

   }

   public void setPlIiName(String PL_II_NM) {

   	  this.PL_II_NM = PL_II_NM;

   }

   public String getPlIiDescription() {

   	  return PL_II_DESC;

   }

   public void setPlIiDescription(String PL_II_DESC) {

   	  this.PL_II_DESC = PL_II_DESC;

   }

   public String getPlCurrency() {

   	  return PL_CURRENCY;

   }

   public void setPlCurrency(String PL_CURRENCY) {

   	  this.PL_CURRENCY = PL_CURRENCY;

   }

   public double getPlPoQuantity() {

	   	  return PL_PO_QTY;

   }

   public void setPlPoQuantity(double PL_PO_QTY) {

	   	  this.PL_PO_QTY = PL_PO_QTY;

   }


   public double getPlPoAmount() {

	   	  return PL_PO_AMT;

	}

	public void setPlPoAmount(double PL_PO_AMT) {

		this.PL_PO_AMT = PL_PO_AMT;

	}




	public double getPlPoConversionRate() {

	   	  return PL_PO_CNVRSN_RT;

	}

	public void setPlPoConversionRate(double PL_PO_CNVRSN_RT) {

		this.PL_PO_CNVRSN_RT = PL_PO_CNVRSN_RT;

	}




	public Date getPlPoConversionDate() {

   	  return PL_PO_CNVRSN_DT;

   }

   public void setPlPoConversionDate(Date PL_PO_CNVRSN_DT) {

   	  this.PL_PO_CNVRSN_DT = PL_PO_CNVRSN_DT;

   }

   public String getPlPartNumber() {

   	  return PL_II_PRT_NMBR;

   }

   public void setPlPartNumber(String PL_II_PRT_NMBR) {

   	  this.PL_II_PRT_NMBR = PL_II_PRT_NMBR;

   }

   public String getPlMisc() {

	   return PL_MISC;

   }

   public void setPlMisc(String PL_MISC) {

	   this.PL_MISC = PL_MISC;

   }



   public String getPlMisc1() {

	   return PL_MISC1;

   }

   public void setPlMisc1(String PL_MISC1) {

	   this.PL_MISC1 = PL_MISC1;

   }
   public String getPlMisc2() {

	   return PL_MISC2;

   }

   public void setPlMisc2(String PL_MISC2) {

	   this.PL_MISC2 = PL_MISC2;

   }
   public String getPlMisc3() {

	   return PL_MISC3;

   }

   public void setPlMisc3(String PL_MISC3) {

	   this.PL_MISC3 = PL_MISC3;

   }

   public String getPlMisc4() {

	   return PL_MISC4;

   }

   public void setPlMisc4(String PL_MISC4) {

	   this.PL_MISC4 = PL_MISC4;

   }

   public String getPlMisc5() {

	   return PL_MISC5;

   }

   public void setPlMisc5(String PL_MISC5) {

	   this.PL_MISC5 = PL_MISC5;

   }
   public String getPlMisc6() {

	   return PL_MISC6;

   }

   public void setPlMisc6(String PL_MISC6) {

	   this.PL_MISC6 = PL_MISC6;

   }

   public String getPlReport2() {

	   return PL_REPORT2;

   }

   public void setPlReport2(String PL_REPORT2) {

	   this.PL_REPORT2 = PL_REPORT2;

   }

   public double getPlRemaining() {

   		return PL_RMNNG;

   }

   public void setPlRemaining(double PL_RMNNG) {

   		this.PL_RMNNG = PL_RMNNG;

   }

   public double getPlConversionFactor() {

   		return PL_CNVRSN_FCTR;

   }

   public void setPlConversionFactor(double PL_CNVRSN_FCTR) {

   		this.PL_CNVRSN_FCTR = PL_CNVRSN_FCTR;

   }
   
   public String getPlPoSupplierCode() {

  		return PL_PO_SPPLR_CODE;

  }

  public void setPlPoSupplierCode(String PL_PO_SPPLR_CODE) {

  		this.PL_PO_SPPLR_CODE = PL_PO_SPPLR_CODE;

  }
  
  
   public Integer getPlPoCode() {

      return PL_PO_CODE;

   }

   public void setPlPoCode(Integer PL_PO_CODE) {

      this.PL_PO_CODE = PL_PO_CODE;

   }

} // ApModPurchaseOrderLineDetails class