/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;

import com.util.ar.ArReceiptDetails;

import java.util.ArrayList;
import java.util.Comparator;

public class ArModReceiptDetails extends ArReceiptDetails implements java.io.Serializable {

	private String RCT_HR_PYRLL_PRD_NM;

   private String RCT_FC_NM;
   private String RCT_TC_NM;
   private String RCT_WTC_NM;
   private String RCT_CST_CSTMR_CODE;
   private String RCT_CST_NM;
   private String RCT_CST_ADDRSS;
   private String RCT_BA_NM;
   private String RCT_BA_CRD1_NM;
   private String RCT_BA_CRD2_NM;
   private String RCT_BA_CRD3_NM;
   private String RCT_BA_CRD1_TYP;
   private String RCT_BA_CRD2_TYP;
   private String RCT_BA_CRD3_TYP;
   private String RCT_RB_NM;
   private String RCT_TYP;
   private String RCT_POS_NM;
   private String RCT_RCPT_SRC;
   private String RCT_INVC_TYPE;

   private double RCT_CRDTD_BAL;

   private ArrayList rctAiList = new ArrayList();
   private ArrayList invIlList = new ArrayList();
   private ArrayList invIliList = new ArrayList();
   private byte RCT_SHW_DPLCT;
   private String RCT_AMNT_IN_WRDS;
   private String RCT_POS_TX_ACCNT_NMBR;
   private String RCT_POS_WTX_ACCNT_NMBR;
   private boolean IS_GFT_CHCK_LN = false;
   private String RCT_IL_NM;
   private String RCT_IL_DESC;
   private String RCT_IL_ITM_PRPRTY_CD;
   private String RCT_IL_ITM_SRL_NMBR;
   private String RCT_IL_ITM_SPCS;
   private String RCT_IL_ITM_CSTDN;
   private String RCT_IL_ITM_EXPRY_DT;
   private double RCT_IL_QTTY;
   private String RCT_IL_UOM_SHRT_NM;
   private double RCT_IL_UNT_PRC;
   private double RCT_IL_AMNT;
   private String RCT_AI_INVC_NMBR;
   private double RCT_AI_APPLY_AMNT;

   private String RCT_SLP_SLSPRSN_CODE;
   private String RCT_SLP_NM;
   private String RCT_CST_CTY;
   private String RCT_BA_BNK_NM;
   private String RCT_PYMNT_MTHD;
   private String RCT_CHCK_NO;
   private double RCT_TC_RT;
   private String RCT_TC_TYP;
   private String  RCT_MSC_TYP;

   private double RCT_IL_UNT_PRC_WO_VAT;
   private double RCT_ILI_DSCNT1;
   private double RCT_ILI_DSCNT2;
   private double RCT_ILI_DSCNT3;
   private double RCT_ILI_DSCNT4;
   private String RCT_ILI_DSCNT;
   private double RCT_TTL_TX;

   private double RCT_ILI_TTL_DSCNT;
   private String RCT_CST_TIN;

   private double RCT_POS_CSH_AMT;
   private double RCT_POS_CHK_AMT;
   private double RCT_POS_CRD_AMT;
   private double RCT_POS_TOTAL_AMT;
   private double RCT_POS_DSCNT;
   private double RCT_POS_VOID_AMT;
   private double RCT_POS_SC_AMT;
   private double RCT_POS_DC_AMT;
   private byte RCT_POS_ON_ACCNT;

   private double RCT_IL_TX;
   private String RCT_IL_AD_LV_CTGRY;
   private double RCT_RT;
   private ArrayList rctDrList = new ArrayList();



   private String RCT_CHQ_NMBR;
   private String RCT_VOU_NMBR;
   private String RCT_CRD_NMBR1;
   private String RCT_CRD_NMBR2;
   private String RCT_CRD_NMBR3;

   private double RCT_AMNT_CSH;
   private double RCT_AMNT_CHQ;
   private double RCT_AMNT_VCHR;
   private double RCT_AMNT_CRD1;
   private double RCT_AMNT_CRD2;
   private double RCT_AMNT_CRD3;
   private String RCT_BRNCH_CODE;
   private String RCT_STATUS;

   private boolean IS_INVESTOR_SUPPLIER = false;

   public ArModReceiptDetails() {
   }


   public String getRctHrPayrollPeriodName() {

   	   return RCT_HR_PYRLL_PRD_NM;

   }

   public void setRctHrPayrollPeriodName(String RCT_HR_PYRLL_PRD_NM) {

   	  this.RCT_HR_PYRLL_PRD_NM = RCT_HR_PYRLL_PRD_NM;

   }



   public String getRctFcName() {

   	   return RCT_FC_NM;

   }

   public void setRctFcName(String RCT_FC_NM) {

   	  this.RCT_FC_NM = RCT_FC_NM;

   }

   public String getRctTcName() {

   	   return RCT_TC_NM;

   }

   public void setRctTcName(String RCT_TC_NM) {

   	   this.RCT_TC_NM = RCT_TC_NM;

   }

   public String getRctWtcName() {

   	   return RCT_WTC_NM;

   }

   public void setRctWtcName(String RCT_WTC_NM) {

   	   this.RCT_WTC_NM = RCT_WTC_NM;

   }

   public String getRctCstCustomerCode() {

   	  return RCT_CST_CSTMR_CODE;

   }

   public void setRctCstCustomerCode(String RCT_CST_CSTMR_CODE) {

   	  this.RCT_CST_CSTMR_CODE = RCT_CST_CSTMR_CODE;

   }

   public String getRctCstName() {

   	   return RCT_CST_NM;

   }

   public void setRctCstName(String RCT_CST_NM) {

   	   this.RCT_CST_NM = RCT_CST_NM;

   }

   public String getRctCstAddress() {

   	   return RCT_CST_ADDRSS;

   }

   public void setRctCstAddress(String RCT_CST_ADDRSS) {

   	   this.RCT_CST_ADDRSS = RCT_CST_ADDRSS;

   }

   public String getRctBaName() {

   	  return RCT_BA_NM;

   }

   public void setRctBaName(String RCT_BA_NM) {

   	  this.RCT_BA_NM = RCT_BA_NM;

   }

   public String getRctBaCard1Name() {

	   	  return RCT_BA_CRD1_NM;

	   }

   public void setRctBaCard1Name(String RCT_BA_CRD1_NM) {

   	  this.RCT_BA_CRD1_NM = RCT_BA_CRD1_NM;

   }

   public String getRctBaCard2Name() {

   	  return RCT_BA_CRD2_NM;

   }

   public void setRctBaCard2Name(String RCT_BA_CRD2_NM) {

   	  this.RCT_BA_CRD2_NM = RCT_BA_CRD2_NM;

   }

   public String getRctBaCard3Name() {

   	  return RCT_BA_CRD3_NM;

   }

   public void setRctBaCard3Name(String RCT_BA_CRD3_NM) {

   	  this.RCT_BA_CRD3_NM = RCT_BA_CRD3_NM;

   }

   public String getRctCardType1() {

   	  return RCT_BA_CRD1_TYP;

   }

   public void setRctCardType1(String RCT_BA_CRD1_TYP) {

   	  this.RCT_BA_CRD1_TYP = RCT_BA_CRD1_TYP;

   }

   public String getRctCardType2() {

   	  return RCT_BA_CRD2_TYP;

   }

   public void setRctCardType2(String RCT_BA_CRD2_TYP) {

   	  this.RCT_BA_CRD2_TYP = RCT_BA_CRD2_TYP;

   }

   public String getRctCardType3() {

   	  return RCT_BA_CRD3_TYP;

   }

   public void setRctCardType3(String RCT_BA_CRD3_TYP) {

   	  this.RCT_BA_CRD3_TYP = RCT_BA_CRD3_TYP;

   }


   public String getRctRbName() {

   	  return RCT_RB_NM;

   }

   public void setRctRbName(String RCT_RB_NM) {

   	  this.RCT_RB_NM = RCT_RB_NM;

   }

   public String getRctType() {

   	  return RCT_TYP;

   }

   public void setRctType(String RCT_TYP) {

	   this.RCT_TYP = RCT_TYP;

   }

   public String getRctPOSName() {

   	  return RCT_POS_NM;

   }

   public void setRctPOSName(String RCT_POS_NM) {

	   this.RCT_POS_NM = RCT_POS_NM;

   }

   public String getRctSource() {

	   return RCT_RCPT_SRC;

   }

   public void setRctSource (String RCT_RCPT_SRC) {

	   this.RCT_RCPT_SRC = RCT_RCPT_SRC;

   }

   public String getRctInvoiceType() {

	   return RCT_INVC_TYPE;

   }

   public void setRctInvoiceType (String RCT_INVC_TYPE) {

	   this.RCT_INVC_TYPE = RCT_INVC_TYPE;

   }




	   public double getRctCreditedBalance() {

		   	  return RCT_CRDTD_BAL;

		   }

		   public void setRctCreditedBalance(double RCT_CRDTD_BAL) {

		   	  this.RCT_CRDTD_BAL = RCT_CRDTD_BAL;

		   }

   public ArrayList getRctAiList() {

   	   return rctAiList;

   }

   public void setRctAiList(ArrayList rctAiList) {

   	   this.rctAiList = rctAiList;

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

   public byte getRctShowDuplicate() {

   	  return RCT_SHW_DPLCT;

   }

   public void setRctShowDuplicate(byte RCT_SHW_DPLCT) {

   	  this.RCT_SHW_DPLCT = RCT_SHW_DPLCT;

   }

   public String getRctAmountInWords() {

   	  return RCT_AMNT_IN_WRDS;

   }

   public void setRctAmountInWords(String RCT_AMNT_IN_WRDS) {

   	  this.RCT_AMNT_IN_WRDS = RCT_AMNT_IN_WRDS;

   }

   public ArModAppliedInvoiceDetails getRctAiListByIndex(int index){

   	  return ((ArModAppliedInvoiceDetails)rctAiList.get(index));

   }

   public int getRctAiListSize(){

   	  return(rctAiList.size());

   }

   public void saveRctAiList(Object newRctAiList){

   	  rctAiList.add(newRctAiList);

   }

   public void saveInvIliList(Object newInvIliList){

   	  invIliList.add(newInvIliList);

   }

   public String getRctPosTaxAccountNumber() {

   	  return RCT_POS_TX_ACCNT_NMBR;

   }

   public void setRctPosTaxAccountNumber(String RCT_POS_TX_ACCNT_NMBR) {

   	  this.RCT_POS_TX_ACCNT_NMBR = RCT_POS_TX_ACCNT_NMBR;

   }

   public ArrayList getRctDrList() {

   	   return rctDrList;

   }

   public void setRctDrList(ArrayList rctDrList) {

   	   this.rctDrList = rctDrList;

   }

   public void saveRctDrList(Object newRctDrList){

   		rctDrList.add(newRctDrList);

   }

   public void saveInvIlList(Object newInvIlList){

   	  invIlList.add(newInvIlList);

   }

   public String getRctPosWtaxAccountNumber() {

   	  return RCT_POS_WTX_ACCNT_NMBR;

   }

   public void setRctPosWtaxAccountNumber(String RCT_POS_WTX_ACCNT_NMBR) {

   	  this.RCT_POS_WTX_ACCNT_NMBR = RCT_POS_WTX_ACCNT_NMBR;

   }

   public boolean getIsGiftCheckLine() {

   	  return IS_GFT_CHCK_LN;

   }

   public void setIsGiftCheckLine(boolean IS_GFT_CHCK_LN) {

   	  this.IS_GFT_CHCK_LN = IS_GFT_CHCK_LN;

   }

   public String getRctIlName() {

   	  return RCT_IL_NM;

   }

   public void setRctIlName(String RCT_IL_NM) {

   	  this.RCT_IL_NM = RCT_IL_NM;

   }

   public String getRctIlDescription() {

   	  return RCT_IL_DESC;

   }

   public void setRctIlDescription(String RCT_IL_DESC) {

   	  this.RCT_IL_DESC = RCT_IL_DESC;

   }


   public String getRctIlItemPropertyCode() {

   	  return RCT_IL_ITM_PRPRTY_CD;

   }

   public void setRctIlItemPropertyCode(String RCT_IL_ITM_PRPRTY_CD) {

   	  this.RCT_IL_ITM_PRPRTY_CD = RCT_IL_ITM_PRPRTY_CD;

   }


   public String getRctIlItemSerialNumber() {

   	  return RCT_IL_ITM_SRL_NMBR;

   }

   public void setRctIlItemSerialNumber(String RCT_IL_ITM_SRL_NMBR) {

   	  this.RCT_IL_ITM_SRL_NMBR = RCT_IL_ITM_SRL_NMBR;

   }

   public String getRctIlItemSpecs() {

   	  return RCT_IL_ITM_SPCS;

   }

   public void setRctIlItemSpecs(String RCT_IL_ITM_SPCS) {

   	  this.RCT_IL_ITM_SPCS = RCT_IL_ITM_SPCS;

   }

   public String getRctIlItemCustodian() {

   	  return RCT_IL_ITM_CSTDN;

   }

   public void setRctIlItemCustodian(String RCT_IL_ITM_CSTDN) {

   	  this.RCT_IL_ITM_CSTDN = RCT_IL_ITM_CSTDN;

   }


   public String getRctIlItemExpiryDate() {

   	  return RCT_IL_ITM_EXPRY_DT;

   }

   public void setRctIlItemExpiryDate(String RCT_IL_ITM_EXPRY_DT) {

   	  this.RCT_IL_ITM_EXPRY_DT = RCT_IL_ITM_EXPRY_DT;

   }

   public double getRctIlQuantity() {

   	  return RCT_IL_QTTY;

   }

   public void setRctIlQuantity(double RCT_IL_QTTY) {

   	  this.RCT_IL_QTTY = RCT_IL_QTTY;

   }

   public String getRctIlUomShortName() {

   	  return RCT_IL_UOM_SHRT_NM;

   }

   public void setRctIlUomShortName(String RCT_IL_UOM_SHRT_NM) {

   	  this.RCT_IL_UOM_SHRT_NM = RCT_IL_UOM_SHRT_NM;

   }

   public double getRctIlUnitPrice() {

   	  return RCT_IL_UNT_PRC;

   }

   public void setRctIlUnitPrice(double RCT_IL_UNT_PRC) {

   	  this.RCT_IL_UNT_PRC = RCT_IL_UNT_PRC;

   }

   public double getRctIlAmount() {

   	  return RCT_IL_AMNT;

   }

   public void setRctIlAmount(double RCT_IL_AMNT) {

   	  this.RCT_IL_AMNT = RCT_IL_AMNT;

   }

   public String getRctAiInvoiceNumber() {

   	  return RCT_AI_INVC_NMBR;

   }

   public void setRctAiInvoiceNumber(String RCT_AI_INVC_NMBR) {

   	  this.RCT_AI_INVC_NMBR = RCT_AI_INVC_NMBR;

   }

   public double getRctAiApplyAmount() {

   	  return RCT_AI_APPLY_AMNT;

   }

   public void setRctAiApplyAmount(double RCT_AI_APPLY_AMNT) {

   	  this.RCT_AI_APPLY_AMNT = RCT_AI_APPLY_AMNT;

   }

   public String getRctSlpSalespersonCode() {

   	  return RCT_SLP_SLSPRSN_CODE;

   }

   public void setRctSlpSalespersonCode(String RCT_SLP_SLSPRSN_CODE) {

   	  this.RCT_SLP_SLSPRSN_CODE = RCT_SLP_SLSPRSN_CODE;

   }

   public String getRctCstCity() {

   	  return RCT_CST_CTY;

   }

   public void setRctCstCity(String RCT_CST_CTY) {

   	  this.RCT_CST_CTY = RCT_CST_CTY;

   }

   public String getRctBaBankName() {

   	  return RCT_BA_BNK_NM;

   }

   public void setRctBaBankName(String RCT_BA_BNK_NM) {

   	  this.RCT_BA_BNK_NM = RCT_BA_BNK_NM;

   }

   public String getRctPaymentMethod() {

	   	  return RCT_PYMNT_MTHD;

	   }

	   public void setRctPaymentMethod(String RCT_PYMNT_MTHD) {

	   	  this.RCT_PYMNT_MTHD = RCT_PYMNT_MTHD;

	   }

	   public String getRctCheckNo() {

		   	  return RCT_CHCK_NO;

		   }

		   public void setRctCheckNo(String RCT_CHCK_NO) {

		   	  this.RCT_CHCK_NO = RCT_CHCK_NO;

		   }

   public String getRctSlpName() {

   	  return RCT_SLP_NM;

   }

   public void setRctSlpName(String RCT_SLP_NM) {

   	  this.RCT_SLP_NM = RCT_SLP_NM;

   }

   public double getRctTcRate() {

   	   return RCT_TC_RT;

   }

   public void setRctTcRate(double RCT_TC_RT) {

   	   this.RCT_TC_RT = RCT_TC_RT;

   }

   public String getRctTcType() {

   	   return RCT_TC_TYP;

   }

   public void setRctTcType(String RCT_TC_TYP) {

   	   this.RCT_TC_TYP = RCT_TC_TYP;

   }
   public double getRctIlUnitPriceWoVat() {

   	  return RCT_IL_UNT_PRC_WO_VAT;

   }

   public void setRctIlUnitPriceWoVat(double RCT_IL_UNT_PRC_WO_VAT) {

   	  this.RCT_IL_UNT_PRC_WO_VAT = RCT_IL_UNT_PRC_WO_VAT;

   }

	public double getRctIliDiscount1() {

		return RCT_ILI_DSCNT1;

	}

	public void setRctIliDiscount1(double RCT_ILI_DSCNT1) {

		this.RCT_ILI_DSCNT1 = RCT_ILI_DSCNT1;

	}

	public double getRctIliDiscount2() {

		return RCT_ILI_DSCNT2;

	}

	public void setRctIliDiscount2(double RCT_ILI_DSCNT2) {

		this.RCT_ILI_DSCNT2 = RCT_ILI_DSCNT2;

	}

	public double getRctIliDiscount3() {

		return RCT_ILI_DSCNT3;

	}

	public void setRctIliDiscount3(double RCT_ILI_DSCNT3) {

		this.RCT_ILI_DSCNT3 = RCT_ILI_DSCNT3;

	}

	public double getRctIliDiscount4() {

		return RCT_ILI_DSCNT4;

	}

	public void setRctIliDiscount4(double RCT_ILI_DSCNT4) {

		this.RCT_ILI_DSCNT4 = RCT_ILI_DSCNT4;

	}

	public String getRctIliDiscount() {

		return RCT_ILI_DSCNT;

	}

	public void setRctIliDiscount(String RCT_ILI_DSCNT) {

		this.RCT_ILI_DSCNT = RCT_ILI_DSCNT;

	}

	public double getRctTotalTax() {

		return RCT_TTL_TX;

	}

	public void setRctTotalTax(double RCT_TTL_TX) {

		this.RCT_TTL_TX = RCT_TTL_TX;

	}

	public String getRctMiscType() {

		return RCT_MSC_TYP;

	}

	public void setRctMiscType(String RCT_MSC_TYP) {

		this.RCT_MSC_TYP = RCT_MSC_TYP;

	}

	public double getRctIliTotalDiscount() {

		return RCT_ILI_TTL_DSCNT;

	}

	public void setRctIliTotalDiscount(double RCT_ILI_TTL_DSCNT) {

		this.RCT_ILI_TTL_DSCNT = RCT_ILI_TTL_DSCNT;

	}

	public String getRctCstTin() {

		return RCT_CST_TIN;

	}

	public void setRctCstTin(String RCT_CST_TIN) {

		this.RCT_CST_TIN = RCT_CST_TIN;

	}

	public double getRctPosCashAmount() {

		return RCT_POS_CSH_AMT;

	}

	public void setRctPosCashAmount(double RCT_POS_CSH_AMT) {

		this.RCT_POS_CSH_AMT = RCT_POS_CSH_AMT;

	}

	public double getRctPosCheckAmount() {

		return RCT_POS_CHK_AMT;

	}

	public void setRctPosCheckAmount(double RCT_POS_CHK_AMT) {

		this.RCT_POS_CHK_AMT = RCT_POS_CHK_AMT;

	}

	public double getRctPosCardAmount() {

		return RCT_POS_CRD_AMT;

	}

	public void setRctPosCardAmount(double RCT_POS_CRD_AMT) {

		this.RCT_POS_CRD_AMT = RCT_POS_CRD_AMT;

	}

	public double getRctPosTotalAmount() {

		return RCT_POS_TOTAL_AMT;

	}

	public void setRctPosTotalAmount(double RCT_POS_TOTAL_AMT) {

		this.RCT_POS_TOTAL_AMT = RCT_POS_TOTAL_AMT;

	}

	public double getRctPosDiscount() {

		return RCT_POS_DSCNT;

	}

	public void setRctPosDiscount(double RCT_POS_DSCNT) {

		this.RCT_POS_DSCNT = RCT_POS_DSCNT;

	}

	public double getRctPosVoidAmount() {

		return RCT_POS_VOID_AMT;

	}

	public void setRctPosVoidAmount(double RCT_POS_VOID_AMT) {

		this.RCT_POS_VOID_AMT = RCT_POS_VOID_AMT;

	}

	public double getRctPosScAmount() {

		return RCT_POS_SC_AMT;

	}

	public void setRctPosScAmount(double RCT_POS_SC_AMT) {

		this.RCT_POS_SC_AMT = RCT_POS_SC_AMT;

	}

	public double getRctPosDcAmount() {

		return RCT_POS_DC_AMT;

	}

	public void setRctPosDcAmount(double RCT_POS_DC_AMT) {

		this.RCT_POS_DC_AMT = RCT_POS_DC_AMT;

	}

	public short getRctPosOnAccount() {

		return RCT_POS_ON_ACCNT;
	}

	public void setRctPosOnAccount(byte RCT_POS_ON_ACCNT) {

		this.RCT_POS_ON_ACCNT = RCT_POS_ON_ACCNT;
	}

	public double getRctIlTax() {

		return RCT_IL_TX;

	}

	public void setRctIlTax(double RCT_IL_TX){

		this.RCT_IL_TX = RCT_IL_TX;

	}

	public String getRctIlAdLvCategory() {

		return RCT_IL_AD_LV_CTGRY;

	}

	public void setRctIlAdLvCategory(String RCT_IL_AD_LV_CTGRY) {

		this.RCT_IL_AD_LV_CTGRY = RCT_IL_AD_LV_CTGRY;

	}

	public double getRctRatio() {

		return RCT_RT;

	}

	public void setRctRatio(double RCT_RT) {

		this.RCT_RT = RCT_RT;

	}


	public String getRctChequeNumber() {

		return RCT_CHQ_NMBR;

	}

	public void setRctChequeNumber(String RCT_CHQ_NMBR) {

		this.RCT_CHQ_NMBR = RCT_CHQ_NMBR;

	}


	public String getRctVoucherNumber() {

		return RCT_VOU_NMBR;

	}

	public void setRctVoucherNumber(String RCT_VOU_NMBR) {

		this.RCT_VOU_NMBR = RCT_VOU_NMBR;

	}

public String getRctCardNumber1() {

		return RCT_CRD_NMBR1;

	}

	public void setRctCardNumber1(String RCT_CRD_NMBR1) {

		this.RCT_CRD_NMBR1 = RCT_CRD_NMBR1;

	}


public String getRctCardNumber2() {

		return RCT_CRD_NMBR2;

	}

	public void setRctCardNumber2(String RCT_CRD_NMBR2) {

		this.RCT_CRD_NMBR2= RCT_CRD_NMBR2;

	}

public String getRctCardNumber3() {

		return RCT_CRD_NMBR3;

	}

	public void setRctCardNumber3(String RCT_CRD_NMBR3) {

		this.RCT_CRD_NMBR3 = RCT_CRD_NMBR3;

	}



	public double getRctAmountCash() {

	   	  return RCT_AMNT_CSH;

	   }

	   public void setRctAmountCash(double RCT_AMNT_CSH) {

	   	  this.RCT_AMNT_CSH = RCT_AMNT_CSH;

	   }

	   public double getRctAmountCheque() {

		   	  return RCT_AMNT_CHQ;

		   }

		   public void setRctAmountCheque(double RCT_AMNT_CHQ) {

		   	  this.RCT_AMNT_CHQ = RCT_AMNT_CHQ;

		   }

		   public double getRctAmountVoucher() {

			   	  return RCT_AMNT_VCHR;

			   }

			   public void setRctAmountVoucher(double RCT_AMNT_VCHR) {

			   	  this.RCT_AMNT_VCHR = RCT_AMNT_VCHR;

			   }


	   public double getRctAmountCard1() {

		   	  return RCT_AMNT_CRD1;

		   }

		   public void setRctAmountCard1(double RCT_AMNT_CRD1) {

		   	  this.RCT_AMNT_CRD1 = RCT_AMNT_CRD1;

		   }

		   public double getRctAmountCard2() {

			   	  return RCT_AMNT_CRD2;

			   }

			   public void setRctAmountCard2(double RCT_AMNT_CRD2) {

			   	  this.RCT_AMNT_CRD2 = RCT_AMNT_CRD2;

			   }

        public double getRctAmountCard3() {

                       return RCT_AMNT_CRD3;

                }

        public void setRctAmountCard3(double RCT_AMNT_CRD3) {

               this.RCT_AMNT_CRD3 = RCT_AMNT_CRD3;

        }


        public boolean getIsInvestorSupplier() {

                       return IS_INVESTOR_SUPPLIER;

                }

        public void setIsInvestorSupplier(boolean IS_INVESTOR_SUPPLIER) {

               this.IS_INVESTOR_SUPPLIER = IS_INVESTOR_SUPPLIER;

        }
        
        
         public String getRctBranchCode() {

                       return RCT_BRNCH_CODE;

                }

        public void setRctBranchCode(String RCT_BRNCH_CODE) {

               this.RCT_BRNCH_CODE = RCT_BRNCH_CODE;

        }
        
        
           public String getRctStatus() {

                       return RCT_STATUS;

                }

        public void setRctStatus(String RCT_STATUS) {

               this.RCT_STATUS = RCT_STATUS;

        }


	public static Comparator sortByItemCategory = (r1, r2) -> {

        ArModReceiptDetails receipt1 = (ArModReceiptDetails) r1;
        ArModReceiptDetails receipt2 = (ArModReceiptDetails) r2;
        return receipt1.getRctIlAdLvCategory().compareTo(receipt2.getRctIlAdLvCategory());

    };

	public static Comparator sortByReceiptNumber = (r1, r2) -> {

        ArModReceiptDetails receipt1 = (ArModReceiptDetails) r1;
        ArModReceiptDetails receipt2 = (ArModReceiptDetails) r2;
        return receipt1.getRctNumber().compareTo(receipt2.getRctNumber());

    };

} // ArModReceiptDetails class