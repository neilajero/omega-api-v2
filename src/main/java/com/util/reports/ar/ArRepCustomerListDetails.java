/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;

import java.util.Comparator;


public class ArRepCustomerListDetails implements java.io.Serializable {

	private String CL_CST_CSTMR_CODE;
	private String CL_CST_NM;
	private String CL_CST_CNTCT;
	private String CL_CST_PHN;
	private String CL_CST_TIN;
	private double CL_CST_BLNC;
	private double CL_CST_CRDT_LMT;
	private String CL_CST_ADDRSS;
	private String CL_CST_FX;
	private String CL_CST_PYMNT_TRM;
	private String CL_CST_TYP;
	private String CL_CST_DL_PRC;
	private String CST_AD_LV_RGN;
	private String CL_SLSPRSN;
	private String CL_SLSPRSN_CRT;
	private String ORDER_BY;
	private String CL_INV_DT;
	private boolean CL_INCLD_NGTV_BLNCS;
	private double CL_RCPT_BLNC;
	private String INV_NMBR;
	private String RFRNC_NMBR;
	private String CM_RFRNC_NMBR;
	private String CL_RCT_DT;
	private double CL_CM_BLNC;
	private double CL_AI_APPLY_AMNT;
	private double CL_AI_CRDTBL_W_TX;
	private double CL_AI_DSCNT_AMNT;
	private double CL_AI_APPLD_DPST;
	
	public ArRepCustomerListDetails() {
    }

	public String getClCstCustomerCode() {

		return CL_CST_CSTMR_CODE;

	}

	public void setClCstCustomerCode(String CL_CST_CSTMR_CODE) {

		this.CL_CST_CSTMR_CODE = CL_CST_CSTMR_CODE;

	}

	public String getClCstName() {

		return CL_CST_NM;

	}

	public void setClCstName(String CL_CST_NM) {

		this.CL_CST_NM = CL_CST_NM;

	}

	public String getClCstContact() {

		return CL_CST_CNTCT;

	}

	public void setClCstContact(String CL_CST_CNTCT) {

		this.CL_CST_CNTCT = CL_CST_CNTCT;

	}

	public String getClCstPhone() {

		return CL_CST_PHN;

	}

	public void setClCstPhone(String CL_CST_PHN) {

		this.CL_CST_PHN = CL_CST_PHN;

	}

	public String getClCstTin() {

		return CL_CST_TIN;

	}

	public void setClCstTin(String CL_CST_TIN) {

		this.CL_CST_TIN = CL_CST_TIN;

	}

	public double getClCstBalance() {

		return CL_CST_BLNC;

	}

	public void setClCstBalance(double CL_CST_BLNC) {

		this.CL_CST_BLNC = CL_CST_BLNC;

	}
	
	public double getClCmBalance() {

		return CL_CM_BLNC;

	}

	public void setClCmBalance(double CL_CM_BLNC) {

		this.CL_CM_BLNC = CL_CM_BLNC;

	}

	public double getClRcptBalance() {

		return CL_RCPT_BLNC;

	}

	public void setClRcptBalance(double CL_RCPT_BLNC) {

		this.CL_RCPT_BLNC = CL_RCPT_BLNC;

	}

	public double getClCstCreditLimit() {

		return CL_CST_CRDT_LMT;

	}

	public void setClCstCreditLimit(double CL_CST_CRDT_LMT) {

		this.CL_CST_CRDT_LMT = CL_CST_CRDT_LMT;

	}

	public String getOrderBy() {

		return ORDER_BY;

	}

	public void setOrderBy(String ORDER_BY) {

		this.ORDER_BY = ORDER_BY;

	}

	public String getClCstAddress(){

		return CL_CST_ADDRSS;

	}

	public void setClCstAddress(String CL_CST_ADDRSS){

		this.CL_CST_ADDRSS = CL_CST_ADDRSS;

	}

	public String getClCstFax(){

		return CL_CST_FX;

	}

	public void setClCstFax(String CL_CST_FX){

		this.CL_CST_FX = CL_CST_FX; 

	}

	public String getClCstPaymentTerm(){

		return CL_CST_PYMNT_TRM;

	}

	public void setClCstPaymentTerm(String CL_CST_PYMNT_TRM){

		this.CL_CST_PYMNT_TRM = CL_CST_PYMNT_TRM;

	}

	public String getClCstType() {

		return CL_CST_TYP;

	}

	public void setClCstType(String CL_CST_TYP) {

		this.CL_CST_TYP = CL_CST_TYP;

	}

	public String getCustomerRegion() {

		return CST_AD_LV_RGN;

	}

	public void setCustomerRegion(String CST_AD_LV_RGN) {

		this.CST_AD_LV_RGN = CST_AD_LV_RGN;       

	}

	public String getClCstDealPrice(){

		return CL_CST_DL_PRC;

	}

	public void setClCstDealPrice(String CL_CST_DL_PRC){

		this.CL_CST_DL_PRC = CL_CST_DL_PRC;

	}

	public String getSalesPerson() {

		return CL_SLSPRSN;

	}

	public void setSalesPerson(String CL_SLSPRSN) {

		this.CL_SLSPRSN = CL_SLSPRSN;

	}
	
	public String getSalesPersonCrt() {

		return CL_SLSPRSN_CRT;

	}

	public void setSalesPersonCrt(String CL_SLSPRSN_CRT) {

		this.CL_SLSPRSN_CRT = CL_SLSPRSN_CRT;

	}

	public String getInvNumber() {

		return INV_NMBR;

	}

	public void setInvNumber(String INV_NMBR) {

		this.INV_NMBR = INV_NMBR;

	}

	public String getInvReferenceNumber() {

		return RFRNC_NMBR;

	}

	public void setInvReferenceNumber(String RFRNC_NMBR) {

		this.RFRNC_NMBR = RFRNC_NMBR;

	}
	
	public String getInvCmReferenceNumber() {

		return CM_RFRNC_NMBR;

	}

	public void setInvCmReferenceNumber(String CM_RFRNC_NMBR) {

		this.CM_RFRNC_NMBR = CM_RFRNC_NMBR;

	}
	
	public String getDate() {

		return CL_INV_DT;

	}

	public void setDate(String CL_INV_DT) {

		this.CL_INV_DT = CL_INV_DT;

	}
	public String getRctDate() {

		return CL_RCT_DT;

	}

	public void setRctDate(String CL_RCT_DT) {

		this.CL_RCT_DT = CL_RCT_DT;

	}

	public boolean getIncludedNegativeBalances() {

		return CL_INCLD_NGTV_BLNCS;

	}

	public void setIncludedNegativeBalances(boolean CL_INCLD_NGTV_BLNCS) {

		this.CL_INCLD_NGTV_BLNCS = CL_INCLD_NGTV_BLNCS;

	}
	

	public double getCsAiApplyAmount() {

		return CL_AI_APPLY_AMNT;

	}

	public void setCsAiApplyAmount(double CL_AI_APPLY_AMNT) {

		this.CL_AI_APPLY_AMNT = CL_AI_APPLY_AMNT;

	}
	
	public double getCsAiCreditableWTax() {

		return CL_AI_CRDTBL_W_TX;

	}

	public void setCsAiCreditableWTax(double CL_AI_CRDTBL_W_TX) {

		this.CL_AI_CRDTBL_W_TX = CL_AI_CRDTBL_W_TX;

	}
	
	public double getCsAiDiscountAmount() {

		return CL_AI_DSCNT_AMNT;

	}

	public void setCsAiDiscountAmount(double CL_AI_DSCNT_AMNT) {

		this.CL_AI_DSCNT_AMNT = CL_AI_DSCNT_AMNT;

	}
	
	public double getCsAiAppliedDeposit() {

		return CL_AI_APPLD_DPST;

	}

	public void setCsAiAppliedDeposit(double CL_AI_APPLD_DPST) {

		this.CL_AI_APPLD_DPST = CL_AI_APPLD_DPST;

	}
	public static Comparator CustomerCodeComparator = (CS, anotherCS) -> {

        String CL_CST_SLSPRSN1 = ((ArRepCustomerListDetails) CS).getSalesPerson();
        String CL_CST_CSTMR_CODE1 = ((ArRepCustomerListDetails) CS).getClCstCustomerCode();
        String CL_CST_CSTMR_TYP1 = ((ArRepCustomerListDetails) CS).getClCstType();

        String CL_CST_SLSPRSN2 = ((ArRepCustomerListDetails) anotherCS).getSalesPerson();
        String CL_CST_CSTMR_CODE2 = ((ArRepCustomerListDetails) anotherCS).getClCstCustomerCode();
        String CL_CST_CSTMR_TYP2 = ((ArRepCustomerListDetails) anotherCS).getClCstType();
        String ORDER_BY = ((ArRepCustomerListDetails) CS).getOrderBy();

        String test = "";

        try{
            if (CL_CST_SLSPRSN1==null) {

                CL_CST_SLSPRSN1=test;
                CL_CST_SLSPRSN2=test;
                return CL_CST_SLSPRSN1.compareTo(CL_CST_SLSPRSN2);

            } else if (CL_CST_SLSPRSN2==null) {

                CL_CST_SLSPRSN1=test;
                CL_CST_SLSPRSN2=test;
                return CL_CST_SLSPRSN1.compareTo(CL_CST_SLSPRSN2);

            }
            else {

                return CL_CST_SLSPRSN1.compareTo(CL_CST_SLSPRSN2);
            }
        } catch(Exception e) {

            test="0";

        }

        if (!(CL_CST_CSTMR_CODE1.equals(CL_CST_CSTMR_CODE2))) {

            return CL_CST_CSTMR_CODE1.compareTo(CL_CST_CSTMR_CODE2);

        } else {

            if(ORDER_BY.equals("CUSTOMER TYPE") && !(CL_CST_CSTMR_TYP1.equals(CL_CST_CSTMR_TYP2))){

                return CL_CST_CSTMR_TYP1.compareTo(CL_CST_CSTMR_TYP2);

            } else if(ORDER_BY.equals("SALESPERSON") && !(CL_CST_SLSPRSN1.equals(CL_CST_SLSPRSN2))){

                return CL_CST_SLSPRSN1.compareTo(CL_CST_SLSPRSN2);

            }else {

                return CL_CST_CSTMR_CODE1.compareTo(CL_CST_CSTMR_CODE2);

            }

        }

    };
	public static Comparator SalespersonComparator = (CS, anotherCS) -> {

        String CL_CST_SLSPRSN1 = ((ArRepCustomerListDetails) CS).getSalesPerson();
        String CL_CST_CSTMR_CODE1 = ((ArRepCustomerListDetails) CS).getClCstCustomerCode();
        String CL_CST_CSTMR_TYP1 = ((ArRepCustomerListDetails) CS).getClCstType();

        String CL_CST_SLSPRSN2 = ((ArRepCustomerListDetails) anotherCS).getSalesPerson();
        String CL_CST_CSTMR_CODE2 = ((ArRepCustomerListDetails) anotherCS).getClCstCustomerCode();
        String CL_CST_CSTMR_TYP2 = ((ArRepCustomerListDetails) anotherCS).getClCstType();
        String ORDER_BY = ((ArRepCustomerListDetails) CS).getOrderBy();
        String test = "";

        try{
            if (CL_CST_SLSPRSN1==null) {

                CL_CST_SLSPRSN1=test;
                CL_CST_SLSPRSN2=test;
                return CL_CST_SLSPRSN1.compareTo(CL_CST_SLSPRSN2);

            } else if (CL_CST_SLSPRSN2==null) {

                CL_CST_SLSPRSN1=test;
                CL_CST_SLSPRSN2=test;
                return CL_CST_SLSPRSN1.compareTo(CL_CST_SLSPRSN2);

            }
            else {

                return CL_CST_SLSPRSN1.compareTo(CL_CST_SLSPRSN2);
            }
        } catch(Exception e) {

            test="0";

        }
        if (!(CL_CST_SLSPRSN1.equals(CL_CST_SLSPRSN2))) {

            return CL_CST_SLSPRSN1.compareTo(CL_CST_SLSPRSN2);

        } else {

            if(ORDER_BY.equals("CUSTOMER TYPE") && !(CL_CST_CSTMR_TYP1.equals(CL_CST_CSTMR_TYP2))){

                return CL_CST_CSTMR_TYP1.compareTo(CL_CST_CSTMR_TYP2);

            } else if(ORDER_BY.equals("CUSTOMER CODE") && !(CL_CST_CSTMR_CODE1.equals(CL_CST_CSTMR_CODE2))){

                return CL_CST_CSTMR_CODE1.compareTo(CL_CST_CSTMR_CODE2);

            } else {

                return CL_CST_SLSPRSN1.compareTo(CL_CST_SLSPRSN2);

            }

        }

    };
	public static Comparator NoGroupComparator = (CS, anotherCS) -> {

        String CL_CST_SLSPRSN1 = ((ArRepCustomerListDetails) CS).getSalesPerson();
        String CL_CST_CSTMR_CODE1 = ((ArRepCustomerListDetails) CS).getClCstCustomerCode();
        String CL_CST_CSTMR_TYP1 = ((ArRepCustomerListDetails) CS).getClCstType();

        String CL_CST_SLSPRSN2 = ((ArRepCustomerListDetails) anotherCS).getSalesPerson();
        String CL_CST_CSTMR_CODE2 = ((ArRepCustomerListDetails) anotherCS).getClCstCustomerCode();
        String CL_CST_CSTMR_TYP2 = ((ArRepCustomerListDetails) anotherCS).getClCstType();
        String ORDER_BY = ((ArRepCustomerListDetails) CS).getOrderBy();

        if(ORDER_BY.equals("CUSTOMER CODE") && !(CL_CST_CSTMR_CODE1.equals(CL_CST_CSTMR_CODE2))){

            return CL_CST_CSTMR_CODE1.compareTo(CL_CST_CSTMR_CODE2);

        } else if(ORDER_BY.equals("SALESPERSON") && !(CL_CST_SLSPRSN1.equals(CL_CST_SLSPRSN2))){

            return CL_CST_SLSPRSN1.compareTo(CL_CST_SLSPRSN2);

        } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(CL_CST_CSTMR_TYP1.equals(CL_CST_CSTMR_TYP2))){

            return CL_CST_CSTMR_TYP1.compareTo(CL_CST_CSTMR_TYP2);

        }  else {

            return CL_CST_CSTMR_CODE1.compareTo(CL_CST_CSTMR_CODE2);

        }

    };
} // ArRepCustomerListDetails class