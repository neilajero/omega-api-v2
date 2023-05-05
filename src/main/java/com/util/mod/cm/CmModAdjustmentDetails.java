/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.cm;

import com.util.cm.CmAdjustmentDetails;

import java.util.Date;

public class CmModAdjustmentDetails extends CmAdjustmentDetails implements java.io.Serializable {

   private String ADJ_BA_NM;
   private String ADJ_BA_FC_NM;
   private double ADJ_TTL_DBT;
   private double ADJ_TTL_CDT;
   private String ADJ_CST_NM;
   private String ADJ_CST_CD;
   private String ADJ_SPL_CD;

   private String ADJ_SO_NMBR;
   private String ADJ_SO_RFRNC_NMBR;

   private Integer HR_PYRLL_PRD_CD;

   public CmModAdjustmentDetails () {
   }

   public CmModAdjustmentDetails (Integer ADJ_CODE, String ADJ_TYP, Date ADJ_DT,
   		 String ADJ_DCMNT_NMBR, String ADJ_RFRNC_NMBR, String ADJ_CHCK_NMBR, double ADJ_AMNT, double ADJ_AMNT_APPLD, Date ADJ_CNVRSN_DT,
         double ADJ_CNVRSN_RT, String ADJ_MM,
         String ADJ_VD_APPRVL_STATUS, byte ADJ_VD_PSTD,
         byte ADJ_VOID,
         byte ADJ_RFND,double ADJ_RFND_AMNT, String ADJ_RFND_RFRNC_NMBR,

         byte ADJ_RCNCLD, Date ADJ_DT_RCNCLD,
         String ADJ_APPRVL_STATUS, byte ADJ_PSTD,
         String ADJ_CRTD_BY, Date ADJ_DT_CRTD, String ADJ_LST_MDFD_BY,
         Date ADJ_DT_LST_MDFD, String ADJ_APPRVD_RJCTD_BY,
         Date ADJ_DT_APPRVD_RJCTD, String ADJ_PSTD_BY, Date ADJ_DT_PSTD,byte ADJ_PRNTD, String ADJ_RSN_FR_RJCTN,
         //String ADJ_BA_NM, String ADJ_BA_FC_NM,
         Integer ADJ_AD_BRNCH, Integer ADJ_AD_CMPNY) {


   }

   public String getAdjBaName() {

   	  return ADJ_BA_NM;

   }

   public void setAdjBaName(String ADJ_BA_NM) {

   	  this.ADJ_BA_NM = ADJ_BA_NM;

   }

   public String getAdjBaFcName() {

      return ADJ_BA_FC_NM;

   }

   public void setAdjBaFcName(String ADJ_BA_FC_NM) {

   	  this.ADJ_BA_FC_NM = ADJ_BA_FC_NM;

   }


   public double getAdjTotalDebit() {

      return ADJ_TTL_DBT;

   }

   public void setAdjTotalDebit(double ADJ_TTL_DBT) {

   	  this.ADJ_TTL_DBT = ADJ_TTL_DBT;

   }

   public double getAdjTotalCredit() {

	      return ADJ_TTL_CDT;

	   }

	   public void setAdjTotalCredit(double ADJ_TTL_CDT) {

	   	  this.ADJ_TTL_CDT = ADJ_TTL_CDT;

	   }

	   public String getAdjCustomerCode() {
			return ADJ_CST_CD;
		}

		public void setAdjCustomerCode(String ADJ_CST_CD){
			this.ADJ_CST_CD = ADJ_CST_CD;
		}


		public String getAdjSupplierCode() {
			return ADJ_SPL_CD;
		}

		public void setAdjSupplierCode(String ADJ_SPL_CD){
			this.ADJ_SPL_CD = ADJ_SPL_CD;
		}


		public String getAdjSoNumber() {
			return ADJ_SO_NMBR;
		}

		public void setAdjSoNumber(String ADJ_SO_NMBR){
			this.ADJ_SO_NMBR = ADJ_SO_NMBR;
		}


		public String getAdjSoReferenceNumber() {
			return ADJ_SO_RFRNC_NMBR;
		}

		public void setAdjSoReferenceNumber(String ADJ_SO_RFRNC_NMBR){
			this.ADJ_SO_RFRNC_NMBR = ADJ_SO_RFRNC_NMBR;
		}


		public Integer getAdjPayrollPeriodCode() {
			return HR_PYRLL_PRD_CD;
		}

		public void setAdjPayrollPeriodCode(Integer HR_PYRLL_PRD_CD){
			this.HR_PYRLL_PRD_CD = HR_PYRLL_PRD_CD;
		}

	public String getAdjCustomerName() {
		return ADJ_CST_NM;
	}

	public void setAdjCustomerName(String ADJ_CST_NM){
		this.ADJ_CST_NM = ADJ_CST_NM;
	}
}


// CmAdjustmentDetails class