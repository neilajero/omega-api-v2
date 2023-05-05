/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;

import com.util.ar.ArInvoiceDetails;

import java.util.ArrayList;
import java.util.Date;

public class ArModInvoiceDetails extends ArInvoiceDetails implements java.io.Serializable {


   private String INV_HR_EMPLYEE_NMBR;
   private String INV_HR_BIO_NMBR;
   private String INV_HR_MNGNG_BRNCH;
   private String INV_HR_CRRNT_JB_PSTN;
   private String INV_HR_DPLYD_BRNCH_NM;

   private String INV_PM_PRJCT_CODE;
   private String INV_PM_PRJCT_TYP_CODE;
   private String INV_PM_PRJCT_PHSE_NM;
   private String INV_PM_CNTRCT_TRM_NM;

   private String INV_FC_NM;
   private String INV_TC_NM;
   private String INV_WTC_NM;
   private String INV_CST_CSTMR_CODE;
   private String INV_CST_NM;
   private String INV_CST_ADDRSS;
   private String INV_CST_CTY;
   private String INV_CST_CNTRY;
   private String INV_CST_TIN;
   private String INV_PYT_NM;
   private String INV_IB_NM;
   private String INV_TYP;
   private String INV_CLNT_PO;
   
   private String INV_TX_CD_NM;
   private String INV_WTX_CD_NM;
   
   private double INV_TTL_DBT;
   private double INV_TTL_CRDT;
   
   private String TG_SRL_NMBR;
   
   private double INV_TTL_TX_AMNT;
   private double INV_TTL_WTX_AMNT;

   private String INV_AR_BTCH_NM;
   private ArrayList invIlList;
   private ArrayList invIliList;
   private ArrayList invDrList;
   private ArrayList invSoList;
   private ArrayList invJoList;
   private ArrayList arModInvoiceLineDetailsList;

   // add new field grand total
   private double INV_GRND_TTL_TX;

   private String INV_SLP_SLSPRSN_CODE;
   private String INV_SLP_NM;
   private double INV_TC_RT;
   private String INV_TC_TYP;
   private Date INV_DUE_DT;
   private Date INV_RCVD_DT;
   private int IPS_CODE;
   
   private String INV_BRNCH_CODE;
   private String INV_STATUS;
   
   
   
   public ArModInvoiceDetails () {
   }


    public String getInvHrEmployeeNumber() {

   	   return INV_HR_EMPLYEE_NMBR;

   }

   public void setInvHrEmployeeNumber(String INV_HR_EMPLYEE_NMBR) {

   	  this.INV_HR_EMPLYEE_NMBR = INV_HR_EMPLYEE_NMBR;

   }

   public String getInvHrBioNumber() {

   	   return INV_HR_BIO_NMBR;

   }

   public void setInvHrBioNumber(String INV_HR_BIO_NMBR) {

   	  this.INV_HR_BIO_NMBR = INV_HR_BIO_NMBR;

   }


   public String getInvHrManagingBranch() {

   	   return INV_HR_MNGNG_BRNCH;

   }

   public void setInvHrManagingBranch(String INV_HR_MNGNG_BRNCH) {

   	  this.INV_HR_MNGNG_BRNCH = INV_HR_MNGNG_BRNCH;

   }

   public String getInvHrCurrentJobPosition() {

   	   return INV_HR_CRRNT_JB_PSTN;

   }

   public void setInvHrCurrentJobPosition(String INV_HR_CRRNT_JB_PSTN) {

   	  this.INV_HR_CRRNT_JB_PSTN = INV_HR_CRRNT_JB_PSTN;

   }

   public String getInvHrDeployedBranchName() {

   	   return INV_HR_DPLYD_BRNCH_NM;

   }

   public void setInvHrDeployedBranchName(String INV_HR_DPLYD_BRNCH_NM) {

   	  this.INV_HR_DPLYD_BRNCH_NM = INV_HR_DPLYD_BRNCH_NM;

   }


   public String getInvPmProjectCode() {

   	   return INV_PM_PRJCT_CODE;

   }

   public void setInvPmProjectCode(String INV_PM_PRJCT_CODE) {

   	  this.INV_PM_PRJCT_CODE = INV_PM_PRJCT_CODE;

   }



   public String getInvPmProjectTypeCode() {

   	   return INV_PM_PRJCT_TYP_CODE;

   }

   public void setInvPmProjectTypeCode(String INV_PM_PRJCT_TYP_CODE) {

   	  this.INV_PM_PRJCT_TYP_CODE = INV_PM_PRJCT_TYP_CODE;

   }


   public String getInvPmProjectPhaseName() {

   	   return INV_PM_PRJCT_PHSE_NM;

   }

   public void setInvPmProjectPhaseName(String INV_PM_PRJCT_PHSE_NM) {

   	  this.INV_PM_PRJCT_PHSE_NM = INV_PM_PRJCT_PHSE_NM;
   }


   public String getInvPmContractTermName() {

   	   return INV_PM_CNTRCT_TRM_NM;

   }

   public void setInvPmContractTermName(String INV_PM_CNTRCT_TRM_NM) {

   	  this.INV_PM_CNTRCT_TRM_NM = INV_PM_CNTRCT_TRM_NM;
   }

   public String getInvFcName() {

   	   return INV_FC_NM;

   }

   public void setInvFcName(String INV_FC_NM) {

   	  this.INV_FC_NM = INV_FC_NM;

   }

   public String getInvTcName() {

   	   return INV_TC_NM;

   }

   public void setInvTcName(String INV_TC_NM) {

   	   this.INV_TC_NM = INV_TC_NM;

   }

   public String getInvWtcName() {

   	   return INV_WTC_NM;

   }

   public void setInvWtcName(String INV_WTC_NM) {

   	   this.INV_WTC_NM = INV_WTC_NM;

   }

   public String getInvCstCustomerCode() {

   	   return INV_CST_CSTMR_CODE;

   }

   public void setInvCstCustomerCode(String INV_CST_CSTMR_CODE) {

   	   this.INV_CST_CSTMR_CODE = INV_CST_CSTMR_CODE;

   }

   public String getInvCstName() {

   	   return INV_CST_NM;

   }

   public void setInvCstName(String INV_CST_NM) {

   	   this.INV_CST_NM = INV_CST_NM;

   }

   public String getInvCstAddress() {

   	   return INV_CST_ADDRSS;

   }

   public void setInvCstAddress(String INV_CST_ADDRSS) {

   	   this.INV_CST_ADDRSS = INV_CST_ADDRSS;

   }

   public String getInvCstCity() {

   	   return INV_CST_CTY;

   }

   public void setInvCstCity(String INV_CST_CTY) {

   	   this.INV_CST_CTY = INV_CST_CTY;

   }

   public String getInvCstCountry() {

   	   return INV_CST_CNTRY;

   }

   public void setInvCstCountry(String INV_CST_CNTRY) {

   	   this.INV_CST_CNTRY = INV_CST_CNTRY;

   }

   public String getInvCstTin() {

   	   return INV_CST_TIN;

   }

   public void setInvCstTin(String INV_CST_TIN) {

   	   this.INV_CST_TIN = INV_CST_TIN;

   }


   public String getInvPytName() {

   	   return INV_PYT_NM;

   }

   public void setInvPytName(String INV_PYT_NM) {

   	   this.INV_PYT_NM = INV_PYT_NM;


   }

   public String getInvIbName() {

   	   return INV_IB_NM;

   }

   public void setInvIbName(String INV_IB_NM) {

   	   this.INV_IB_NM = INV_IB_NM;

   }

   public String getInvType() {

   	   return INV_TYP;

   }

   public void setInvType(String INV_TYP) {

   	   this.INV_TYP = INV_TYP;

   }

   public double getInvTotalDebit() {

   	  return INV_TTL_DBT;

   }

   public void setInvTotalDebit(double INV_TTL_DBT) {

   	  this.INV_TTL_DBT = INV_TTL_DBT;

   }

   public void setInvTotalCredit(double INV_TTL_CRDT) {

   	  this.INV_TTL_CRDT = INV_TTL_CRDT;

   }

   public double getInvTotalCredit() {

   	  return INV_TTL_CRDT;

   }
   
   
   
   public void setInvTotalTaxAmount(double INV_TTL_TX_AMNT) {

   	  this.INV_TTL_TX_AMNT = INV_TTL_TX_AMNT;

   }

   public double getInvTotalTaxAmount() {

   	  return INV_TTL_TX_AMNT;

   }
   
   
   public void setInvTotalWTaxAmount(double INV_TTL_WTX_AMNT) {

   	  this.INV_TTL_WTX_AMNT = INV_TTL_WTX_AMNT;

   }

   public double getInvTotalWTaxAmount() {

   	  return INV_TTL_WTX_AMNT;

   }

   public String getInvArBatchName(){
	   return INV_AR_BTCH_NM;
   }

   public void setInvArBatchName(String INV_AR_BTCH_NM){
	   this.INV_AR_BTCH_NM = INV_AR_BTCH_NM;
   }


   public ArrayList getInvIlList() {

   	   return invIlList;

   }

   public void setInvIlList(ArrayList invIlList) {

   	   this.invIlList = invIlList;

   }

   public ArrayList getInvIliList() {

   	   return invIliList;

   }

   public void setInvIliList(ArrayList invIliList) {

   	   this.invIliList = invIliList;

   }

   public ArrayList getInvDrList() {

   	   return invDrList;

   }

   public void setInvDrList(ArrayList invDrList) {

   	   this.invDrList = invDrList;

   }

   public ArrayList getInvSoList() {

   	   return invSoList;

   }

   public void setInvSoList(ArrayList invSoList) {

   	   this.invSoList = invSoList;

   }
   
   public ArrayList getInvJoList() {

   	   return invJoList;

   }

   public void setInvJoList(ArrayList invJoList) {

   	   this.invJoList = invJoList;

   }

   public ArrayList getArModInvoiceLineDetailsList() {

   	   return arModInvoiceLineDetailsList;

   }

   public void setArModInvoiceLineDetailsList(ArrayList arModInvoiceLineDetailsList) {

   	   this.arModInvoiceLineDetailsList = arModInvoiceLineDetailsList;

   }

   public String getInvSlpSalespersonCode() {

   	   return INV_SLP_SLSPRSN_CODE;

   }

   public void setInvSlpSalespersonCode(String INV_SLP_SLSPRSN_CODE) {

   	   this.INV_SLP_SLSPRSN_CODE = INV_SLP_SLSPRSN_CODE;

   }

   public String getInvSlpName() {

   	   return INV_SLP_NM;

   }

   public void setInvSlpName(String INV_SLP_NM) {

   	   this.INV_SLP_NM = INV_SLP_NM;

   }

   public double getInvTcRate() {

   	   return INV_TC_RT;

   }

   public void setInvTcRate(double INV_TC_RT) {

   	   this.INV_TC_RT = INV_TC_RT;

   }

   public String getInvTcType() {

   	   return INV_TC_TYP;

   }

   public void setInvTcType(String INV_TC_TYP) {

   	   this.INV_TC_TYP = INV_TC_TYP;

   }

   public String getInvClientPO() {

	   return INV_CLNT_PO;

   }

   public void setInvClientPO(String INV_CLNT_PO) {

	   this.INV_CLNT_PO = INV_CLNT_PO;

   }
   
   public String getInvTaxCodeName() {

	   return INV_TX_CD_NM;

   }

   public void setInvTaxCodeName(String INV_TX_CD_NM) {

	   this.INV_TX_CD_NM = INV_TX_CD_NM;

   }

   public String getInvWTaxCodeName() {

	   return INV_WTX_CD_NM;

   }

   public void setInvWTaxCodeName(String INV_WTX_CD_NM) {

	   this.INV_WTX_CD_NM = INV_WTX_CD_NM;

   }
   
   
    public String getTagSerialNumber() {

	   return TG_SRL_NMBR;

   }

   public void setTagSerialNumber(String TG_SRL_NMBR) {

	   this.TG_SRL_NMBR = TG_SRL_NMBR;

   }

   public Date getInvDueDate() {

	   return INV_DUE_DT;

   }

   public void setInvDueDate(Date INV_DUE_DT) {

	   this.INV_DUE_DT = INV_DUE_DT;

   }

   public int getIpsCode() {

	   return IPS_CODE;

   }

   public void setIpsCode(int IPS_CODE) {

	   this.IPS_CODE = IPS_CODE;

   }

   public Date getInvRecieveDate() {

	   return INV_RCVD_DT;

   }

   public void setInvRecieveDate(Date INV_RCVD_DT) {

	   this.INV_RCVD_DT = INV_RCVD_DT;

   }
   
    
    public String getInvBranchCode() {

       return INV_BRNCH_CODE;

   }

   public void setInvBranchCode(String INV_BRNCH_CODE) {

       this.INV_BRNCH_CODE = INV_BRNCH_CODE;

   }
   
   public String getInvStatus() {

       return INV_STATUS;

   }

   public void setInvStatus(String INV_STATUS) {

       this.INV_STATUS = INV_STATUS;

   }

} // ArModInvoiceDetails class