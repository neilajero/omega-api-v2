/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ap;


import com.util.ap.ApSupplierDetails;

public class ApModSupplierDetails extends ApSupplierDetails implements java.io.Serializable {

	
   private String SPL_ST_NM;
   private String SPL_PYT_NM;
   private String SPL_SC_NM;
   private String SPL_SC_TC_NM;
   private double SPL_SC_TC_RT;
   private String SPL_SC_TC_TYP;
   private String SPL_SC_WTC_NM;
   private String SPL_ST_BA_NM;
   private String SPL_COA_GL_PYBL_ACCNT_NMBR;
   private String SPL_COA_GL_EXPNS_ACCNT_NMBR;
   private String SPL_COA_GL_PYBL_ACCNT_DESC;
   private String SPL_COA_GL_EXPNS_ACCNT_DESC;
   private String SPL_BA_NM;
   private String SPL_LIT_NM;
   // for purchases 2550Q BIR FORM
   private double SPL_EXMPT_PRCHS;
   private double SPL_ZR_RTD_PRCHS;
   private double SPL_INPT_TX;
   private double SPL_TTL_EXMPT_PRCHS;
   private double SPL_TTL_ZR_RTD_PRCHS;
   private double SPL_TTL_INPT_TX;
   private String SPL_SUPPLIER_CLASS;

   private byte SPL_SC_INVESTMENT;
   private byte SPL_SC_LOAN;
   private byte SPL_SC_VAT_RELIEF_VOUCHER_ITEM;

   public ApModSupplierDetails () {
   }


   

   public String getSplStName() {

   	  return SPL_ST_NM;

   }

   public void setSplStName(String SPL_ST_NM) {

   	  this.SPL_ST_NM = SPL_ST_NM;

   }

   public String getSplPytName() {

   	  return SPL_PYT_NM;

   }

   public void setSplPytName(String SPL_PYT_NM) {

   	  this.SPL_PYT_NM = SPL_PYT_NM;

   }

   public String getSplScName() {

   	  return SPL_SC_NM;

   }

   public void setSplScName(String SPL_SC_NM) {

   	  this.SPL_SC_NM = SPL_SC_NM;

   }

   public String getSplScTcName() {

   	  return SPL_SC_TC_NM;

   }

   public void setSplScTcName(String SPL_SC_TC_NM) {

   	  this.SPL_SC_TC_NM = SPL_SC_TC_NM;

   }

   public String getSplScWtcName() {

   	  return SPL_SC_WTC_NM;

   }

   public void setSplScWtcName(String SPL_SC_WTC_NM) {

   	  this.SPL_SC_WTC_NM = SPL_SC_WTC_NM;

   }

   public String getSplStBaName() {

   	  return SPL_ST_BA_NM;

   }

   public void setSplStBaName(String SPL_ST_BA_NM) {

   	  this.SPL_ST_BA_NM = SPL_ST_BA_NM;

   }

	public String getSplBaName() {

		return SPL_BA_NM;

	}

	public void setSplBaName(String spl_ba_nm) {

		SPL_BA_NM = spl_ba_nm;

	}

	public String getSplCoaGlExpenseAccountNumber() {

		return SPL_COA_GL_EXPNS_ACCNT_NMBR;

	}

	public void setSplCoaGlExpenseAccountNumber(String spl_coa_gl_expns_accnt_nmbr) {

		SPL_COA_GL_EXPNS_ACCNT_NMBR = spl_coa_gl_expns_accnt_nmbr;

	}

	public String getSplCoaGlPayableAccountNumber() {

		return SPL_COA_GL_PYBL_ACCNT_NMBR;

	}

	public void setSplCoaGlPayableAccountNumber(String spl_coa_gl_pybl_accnt_nmbr) {

		SPL_COA_GL_PYBL_ACCNT_NMBR = spl_coa_gl_pybl_accnt_nmbr;

	}

	public String getSplCoaGlPayableAccountDescription() {

		return SPL_COA_GL_PYBL_ACCNT_DESC;

	}

	public void setSplCoaGlPayableAccountDescription(String spl_coa_gl_pybl_accnt_desc) {

		SPL_COA_GL_PYBL_ACCNT_DESC = spl_coa_gl_pybl_accnt_desc;

	}

	public String getSplCoaGlExpenseAccountDescription() {

		return SPL_COA_GL_EXPNS_ACCNT_DESC;

	}

	public void setSplCoaGlExpenseAccountDescription(String spl_coa_gl_expns_accnt_desc) {

		SPL_COA_GL_EXPNS_ACCNT_DESC = spl_coa_gl_expns_accnt_desc;

	}

	public double getSplExemptPurchases() {

		return SPL_EXMPT_PRCHS;

    }

    public void setSplExemptPurchases(double spl_exmpt_prchs) {

    	SPL_EXMPT_PRCHS = spl_exmpt_prchs;

    }

    public double getSplInputTax() {

    	return SPL_INPT_TX;

    }

    public void setSplInputTax(double spl_inpt_tx) {

    	SPL_INPT_TX = spl_inpt_tx;

    }

    public double getSplTotalExemptPurchases() {

    	return SPL_TTL_EXMPT_PRCHS;

    }

    public void setSplTotalExemptPurchases(double spl_ttl_exmpt_prchs) {

    	SPL_TTL_EXMPT_PRCHS = spl_ttl_exmpt_prchs;

    }

    public double getSplTotalInputTax() {

    	return SPL_TTL_INPT_TX;

    }

    public void setSplTotalInputTax(double spl_ttl_inpt_tx) {

    	SPL_TTL_INPT_TX = spl_ttl_inpt_tx;

    }

    public double getSplTotalZeroRatedPurchase() {

    	return SPL_TTL_ZR_RTD_PRCHS;

    }

    public void setSplTotalZeroRatedPurchase(double spl_ttl_zr_rtd_prchs) {

    	SPL_TTL_ZR_RTD_PRCHS = spl_ttl_zr_rtd_prchs;

    }

    public double getSplZeroRatedPurchase() {

    	return SPL_ZR_RTD_PRCHS;

    }

    public void setSplZeroRatedPurchase(double spl_zr_rtd_prchs) {

    	SPL_ZR_RTD_PRCHS = spl_zr_rtd_prchs;

    }

    public double getSplScTcRate() {

    	return SPL_SC_TC_RT;

    }

    public void setSplScTcRate(double SPL_SC_TC_RT) {

    	this.SPL_SC_TC_RT = SPL_SC_TC_RT;

    }

    public String getSplScTcType() {

    	return SPL_SC_TC_TYP;

    }

    public void setSplScTcType(String SPL_SC_TC_TYP) {

    	this.SPL_SC_TC_TYP = SPL_SC_TC_TYP;

    }

    public String getSplLitName() {

    	return SPL_LIT_NM;

    }

    public void setSplLitName(String SPL_LIT_NM) {

    	this.SPL_LIT_NM = SPL_LIT_NM;

    }


    public String getSplSupplierClass(){
        return SPL_SUPPLIER_CLASS;
    }

    public void setSplSupplierClass(String SPL_SUPPLIER_CLASS){
        this.SPL_SUPPLIER_CLASS = SPL_SUPPLIER_CLASS;
    }



    public byte getSplScInvestment(){
        return SPL_SC_INVESTMENT;
    }

    public void setSplScInvestment(byte SPL_SC_INVESTMENT){
        this.SPL_SC_INVESTMENT = SPL_SC_INVESTMENT;
    }

    public byte getSplScLoan(){
        return SPL_SC_LOAN;
    }

    public void setSplScLoan(byte SPL_SC_LOAN){
        this.SPL_SC_LOAN = SPL_SC_LOAN;
    }

    public byte getSplScVatReliefVoucherItem(){
        return SPL_SC_VAT_RELIEF_VOUCHER_ITEM;
    }

    public void setSplScVatReliefVoucherItem(byte SPL_SC_VAT_RELIEF_VOUCHER_ITEM){
        this.SPL_SC_VAT_RELIEF_VOUCHER_ITEM = SPL_SC_VAT_RELIEF_VOUCHER_ITEM;
    }


} // ApModSupplierDetails class