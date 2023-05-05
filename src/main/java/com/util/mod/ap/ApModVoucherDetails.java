/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ap;

import com.util.ap.ApVoucherDetails;

import java.util.ArrayList;

public class ApModVoucherDetails extends ApVoucherDetails implements java.io.Serializable {

   private String VOU_FC_NM;
   private String VOU_TC_NM;
   private String VOU_TC_TYP;
   private double VOU_TC_RT;
   private String VOU_WTC_NM;
   private String VOU_SPL_SPPLR_CODE;
   private String VOU_SPL_NM;
   private String VOU_SPL_CLSS_NM;
   private byte VOU_SC_LN;
   private byte VOU_SC_VT_RLF_VCHR_ITM;

   private String VOU_SPL_SPPLR2_CODE;
   private String VOU_SPL_SPPLR_NM2;
   private String VOU_SPL_SPPLR2_TIN;
   private String VOU_SPL_SPPLR2_ADDRSS1;
   private String VOU_SPL_SPPLR2_ADDRSS2;
   private String VOU_PYT_NM;
   private String VOU_PYT_NM2;
   private String VOU_VB_NM;
   private String VOU_TYP;
   private double VOU_TTL_DBT;
   private double VOU_TTL_CRDT;
   private String VOU_BRNCH_CODE;
   private String VOU_STATUS;
   private ArrayList vouDrList;
   private ArrayList vouVliList;
   private ArrayList vouPlList;

   public ApModVoucherDetails() {
   }

   public String getVouFcName() {

   	  return VOU_FC_NM;

   }

   public void setVouFcName(String VOU_FC_NM) {

   	  this.VOU_FC_NM = VOU_FC_NM;

   }

   public String getVouTcName() {

   	  return VOU_TC_NM;

   }

   public void setVouTcName(String VOU_TC_NM) {

   	  this.VOU_TC_NM = VOU_TC_NM;

   }

   public String getVouWtcName() {

   	  return VOU_WTC_NM;

   }

   public void setVouWtcName(String VOU_WTC_NM) {

   	  this.VOU_WTC_NM = VOU_WTC_NM;

   }

   public String getVouSplSupplierCode() {

   	  return VOU_SPL_SPPLR_CODE;

   }

   public void setVouSplSupplierCode(String VOU_SPL_SPPLR_CODE) {

   	  this.VOU_SPL_SPPLR_CODE = VOU_SPL_SPPLR_CODE;

   }

   public String getVouSplName() {

   	  return VOU_SPL_NM;

   }

   public void setVouSplName(String VOU_SPL_NM) {

   	  this.VOU_SPL_NM = VOU_SPL_NM;

   }


   public String getVouSplClassName() {

	   	  return VOU_SPL_CLSS_NM;

	   }

	   public void setVouSplClassName(String VOU_SPL_CLSS_NM) {

	   	  this.VOU_SPL_CLSS_NM = VOU_SPL_CLSS_NM;

	   }


	   public byte getVouScLoan() {

			return VOU_SC_LN;

		}

		public void setVouScVatReliefVoucherItem(byte VOU_SC_VT_RLF_VCHR_ITM) {

			this.VOU_SC_VT_RLF_VCHR_ITM = VOU_SC_VT_RLF_VCHR_ITM;

		}


		 public byte getVouScVatReliefVoucherItem() {

				return VOU_SC_VT_RLF_VCHR_ITM;

			}

			public void setVouScLoan(byte VOU_SC_LN) {

				this.VOU_SC_LN = VOU_SC_LN;

			}


   public String getVouSplSupplier2Code() {

	   	  return VOU_SPL_SPPLR2_CODE;

	   }

	   public void setVouSplSupplier2Code(String VOU_SPL_SPPLR2_CODE) {

	   	  this.VOU_SPL_SPPLR2_CODE = VOU_SPL_SPPLR2_CODE;

	   }

	   public String getVouSplSupplier2Name2() {

	   	  return VOU_SPL_SPPLR_NM2;

	   }

	   public void setVouSplSupplier2Name2(String VOU_SPL_SPPLR_NM2) {

	   	  this.VOU_SPL_SPPLR_NM2 = VOU_SPL_SPPLR_NM2;

	   }

   public String getVouSplSupplier2Tin() {

   	  return VOU_SPL_SPPLR2_TIN;

   }

   public void setVouSplSupplier2Tin(String VOU_SPL_SPPLR2_TIN) {

   	  this.VOU_SPL_SPPLR2_TIN = VOU_SPL_SPPLR2_TIN;

   }

   public String getVouSplSupplier2Address1() {

   	  return VOU_SPL_SPPLR2_ADDRSS1;

   }

   public void setVouSplSupplier2Address1(String VOU_SPL_SPPLR2_ADDRSS1) {

   	  this.VOU_SPL_SPPLR2_ADDRSS1 = VOU_SPL_SPPLR2_ADDRSS1;

   }

   public String getVouSplSupplier2Address2() {

	   	  return VOU_SPL_SPPLR2_ADDRSS2;

	   }

	   public void setVouSplSupplier2Address2(String VOU_SPL_SPPLR2_ADDRSS2) {

	   	  this.VOU_SPL_SPPLR2_ADDRSS2 = VOU_SPL_SPPLR2_ADDRSS2;

	   }

   public String getVouPytName() {

   	  return VOU_PYT_NM;

   }

   public void setVouPytName(String VOU_PYT_NM) {

   	  this.VOU_PYT_NM = VOU_PYT_NM;

   }

   public String getVouPytName2() {

	   	  return VOU_PYT_NM2;

	   }

	   public void setVouPytName2(String VOU_PYT_NM2) {

	   	  this.VOU_PYT_NM2 = VOU_PYT_NM2;

	   }

   public String getVouVbName() {

   	  return VOU_VB_NM;

   }

   public void setVouVbName(String VOU_VB_NM) {

   	  this.VOU_VB_NM = VOU_VB_NM;

   }

   public String getVouType() {

   	  return VOU_TYP;

   }

   public void setVouType(String VOU_TYP) {

   	  this.VOU_TYP = VOU_TYP;

   }

   public double getVouTotalDebit() {

   	  return VOU_TTL_DBT;

   }

   public void setVouTotalDebit(double VOU_TTL_DBT) {

   	  this.VOU_TTL_DBT = VOU_TTL_DBT;

   }

   public double getVouTotalCredit() {

   	  return VOU_TTL_CRDT;

   }

   public void setVouTotalCredit(double VOU_TTL_CRDT) {

   	  this.VOU_TTL_CRDT = VOU_TTL_CRDT;

   }

   public ArrayList getVouDrList() {

   	  return vouDrList;

   }

   public void setVouDrList(ArrayList vouDrList) {

   	  this.vouDrList = vouDrList;

   }

   public ArrayList getVouVliList() {

   	  return vouVliList;

   }

   public void setVouVliList(ArrayList vouVliList) {

   	   this.vouVliList = vouVliList;

   }

   public ArrayList getVouPlList() {

   	  return vouPlList;

   }

   public void setVouPlList(ArrayList vouPlList) {

   	   this.vouPlList = vouPlList;

   }

   public String getVouTcType() {

   	  return VOU_TC_TYP;

   }

   public void setVouTcType(String VOU_TC_TYP) {

   	  this.VOU_TC_TYP = VOU_TC_TYP;

   }


   public double getVouTcRate() {

   	  return VOU_TC_RT;

   }

   public void setVouTcRate(double VOU_TC_RT) {

   	  this.VOU_TC_RT = VOU_TC_RT;

   }
   
   public String getVouBranchCode() {

      return VOU_BRNCH_CODE;

   }

   public void setVouBranchCode(String VOU_BRNCH_CODE) {

      this.VOU_BRNCH_CODE = VOU_BRNCH_CODE;

   }
   
   public String getVouStatus() {

      return VOU_STATUS;

   }

   public void setVouStatus(String VOU_STATUS) {

      this.VOU_STATUS = VOU_STATUS;

   }


} // ApModVoucherDetails class