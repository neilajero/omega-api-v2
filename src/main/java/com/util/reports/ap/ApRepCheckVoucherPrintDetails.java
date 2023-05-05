/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;

import com.util.Debug;

import java.util.Comparator;
import java.util.Date;

public class ApRepCheckVoucherPrintDetails implements java.io.Serializable {

	private String CV_CHK_DCMNT_NMBR;
	private String CV_CHK_NMBR;
	private Date CV_CHK_DT;
	private Date CV_CHK_CHCK_DT;
	private String CV_CHK_DESC;
	private String CV_CHK_CRTD_BY;
	private String CV_CHK_APPRVD_BY;
	private String CV_CHK_APPRVD_RJCTD_BY;
	private String CV_CHK_CHCKD_BY;
	private String CV_CHK_SPL_NM;
	private byte CV_CHK_DR_DBT;
	private double CV_CHK_DR_AMNT;
	private String CV_CHK_DR_ACCNT_DESC;
	private String CV_CHK_DR_ACCNT_NMBR;
	private char CV_CHK_CRRNCY_SYMBOL;
	private String CV_CHK_CRRNCY_DESC;
	private double CV_CHK_AMNT;
	private String CV_CHK_AMNT_IN_WRDS;
	private String CV_CHK_AD_BA_NM;
	private String CV_CHK_AD_BA_ACCNT_NMBR;
	private byte CV_SHW_DPLCT;
	
	// added fields
	private String CV_APPRVL_STTS;
	private byte CV_PSTD;
	private String CV_CHK_CRTD_BY_DESC;
	private String CV_CHK_CHCK_BY_DESC;
	private String CV_CHK_APPRVD_RJCTD_BY_DESC;
	private Date CV_CHK_DT_CRTD;
	private Date CV_CHK_DT_APPRVD_RJCTD;
	private String CV_CHK_VOU_DOC_NMBR;
	private Date CV_CHK_VOU_DT;
	private String CV_PO_DOC_NMBR;
	private Date CV_PO_DT;
	private byte CV_CRSS_CHCK;
	private String CV_CHK_TYP; 
	private double CV_VOU_GRSS_AMNT;
	private double CV_VOU_WTHHLDNG_TX_AMNT;
	private double CV_VOU_NET_AMNT;

	// added field for ap voucher option
	private String CV_VOU_NMBR;
	private Date CV_VOU_DT;
	private String CV_VOU_PSTD_BY;
	private String CV_VOU_DSCRPTN;
	private String CV_VOU_RFRNC_NMBR;
	private String CV_VOU_DR_COA_NTRL_DESC;
	private String CV_VOU_DR_ACCNT_NMBR;
	private String CV_CHK_DR_COA_NTRL_DESC;
	private double CV_APPLY_AMNT;
	private double CV_APPLY_TX_AMNT;
	private double CV_APPLY_WTHHLDNG_TX_AMNT;
	private double CV_APPLY_NET_AMNT;
	private double CV_APPLY_AMNT_DUE;

	private String CV_CHK_RFRNC_NMBR;
	private String CV_CHK_SPL_ADDRSS;
	private String CV_CHK_MMO;

	private String CV_CHK_BRNCH_CDE;
	private String CV_CHK_BRNCH_NM;
	
	private String CHK_MISC1;
	private String CHK_MISC2;
	private String CHK_MISC3;
	private String CHK_MISC4;
	private String CHK_MISC5;
	private String CHK_MISC6;
	
	private String CV_TX_CD;
	private String CV_W_TX_CD;
	private String CV_CHK_SPL_CD;

	public ApRepCheckVoucherPrintDetails() {
    }

	public String getCvChkDocumentNumber() {

		return CV_CHK_DCMNT_NMBR;

	}

	public void setCvChkDocumentNumber(String CV_CHK_DCMNT_NMBR) {

		this.CV_CHK_DCMNT_NMBR = CV_CHK_DCMNT_NMBR;

	}
	
	public String getCvChkApprovedBy() {

		return CV_CHK_APPRVD_BY;

	}
	
	public String getCvChkApprovedRejectedBy() {

		return CV_CHK_APPRVD_RJCTD_BY;

	}

	public void setCvChkApprovedRejectedBy(String CV_CHK_APPRVD_RJCTD_BY) {

		this.CV_CHK_APPRVD_RJCTD_BY = CV_CHK_APPRVD_RJCTD_BY;

	}

	public Date getCvChkDateApprovedRejected() {

		return CV_CHK_DT_APPRVD_RJCTD;

	}

	public void setCvChkDateApprovedRejected(Date CV_CHK_DT_APPRVD_RJCTD) {

		this.CV_CHK_DT_APPRVD_RJCTD = CV_CHK_DT_APPRVD_RJCTD;

	}

	public String getCvChkCheckedBy() {

		return CV_CHK_CHCKD_BY;

	}

	public void setCvChkCheckedBy(String CV_CHK_CHCKD_BY) {

		this.CV_CHK_CHCKD_BY = CV_CHK_CHCKD_BY;

	}

	public char getCvChkCurrencySymbol() {

		return CV_CHK_CRRNCY_SYMBOL;

	}

	public void setCvChkCurrencySymbol(char CV_CHK_CRRNCY_SYMBOL) {

		this.CV_CHK_CRRNCY_SYMBOL = CV_CHK_CRRNCY_SYMBOL;

	}

	public String getCvChkCurrencyDescription() {

		return CV_CHK_CRRNCY_DESC;

	}

	public void setCvChkCurrencyDescription(String CV_CHK_CRRNCY_DESC) {

		this.CV_CHK_CRRNCY_DESC = CV_CHK_CRRNCY_DESC;

	}

	public String getCvChkCreatedBy() {

		return CV_CHK_CRTD_BY;

	}

	public void setCvChkCreatedBy(String CV_CHK_CRTD_BY) {

		this.CV_CHK_CRTD_BY = CV_CHK_CRTD_BY;

	}

	public Date getCvChkDateCreated() {

		return CV_CHK_DT_CRTD;

	}

	public void setCvChkDateCreated(Date CV_CHK_DT_CRTD) {

		this.CV_CHK_DT_CRTD = CV_CHK_DT_CRTD;

	}

	public String getCvChkDrCoaAccountDescription() {

		return CV_CHK_DR_ACCNT_DESC;

	}

	public void setCvChkDrCoaAccountDescription(String CV_CHK_DR_ACCNT_DESC) {

		this.CV_CHK_DR_ACCNT_DESC = CV_CHK_DR_ACCNT_DESC;

	}

	public String getCvChkDrCoaAccountNumber() {

		return CV_CHK_DR_ACCNT_NMBR;

	}

	public void setCvChkDrCoaAccountNumber(String CV_CHK_DR_ACCNT_NMBR) {

		this.CV_CHK_DR_ACCNT_NMBR = CV_CHK_DR_ACCNT_NMBR;

	}

	public byte getCvChkDrDebit() {

		return CV_CHK_DR_DBT;

	}

	public void setCvChkDrDebit(byte CV_CHK_DR_DBT) {

		this.CV_CHK_DR_DBT = CV_CHK_DR_DBT;

	}

	public double getCvChkDrAmount() {

		return CV_CHK_DR_AMNT;

	}

	public void setCvChkDrAmount(double CV_CHK_DR_AMNT) {

		this.CV_CHK_DR_AMNT = CV_CHK_DR_AMNT;

	}

	public String getCvChkDescription() {

		return CV_CHK_DESC;

	}

	public void setCvChkDescription(String CV_CHK_DESC) {

		this.CV_CHK_DESC = CV_CHK_DESC;

	}

	public Date getCvChkDate() {

		return CV_CHK_DT;

	}

	public void setCvChkDate(Date CV_CHK_DT) {

		this.CV_CHK_DT = CV_CHK_DT;

	}

	public Date getCvChkCheckDate() {

		return CV_CHK_CHCK_DT;

	}

	public void setCvChkCheckDate(Date CV_CHK_CHCK_DT) {

		this.CV_CHK_CHCK_DT = CV_CHK_CHCK_DT;

	}

	public String getCvChkNumber() {

		return CV_CHK_NMBR;

	}

	public void setCvChkNumber(String CV_CHK_NMBR) {

		this.CV_CHK_NMBR = CV_CHK_NMBR;

	}

	public String getCvChkSplName() {

		return CV_CHK_SPL_NM;

	}

	public void setCvChkSplName(String CV_CHK_SPL_NM) {

		this.CV_CHK_SPL_NM = CV_CHK_SPL_NM;

	}

	public double getCvChkAmount() {

		return CV_CHK_AMNT;

	}

	public void setCvChkAmount(double CV_CHK_AMNT) {

		this.CV_CHK_AMNT = CV_CHK_AMNT;

	}

	public String getCvChkAmountInWords() {

		return CV_CHK_AMNT_IN_WRDS;

	}

	public void setCvChkAmountInWords(String CV_CHK_AMNT_IN_WRDS) {

		this.CV_CHK_AMNT_IN_WRDS = CV_CHK_AMNT_IN_WRDS;

	}

	public String getCvChkAdBaName() {

		return CV_CHK_AD_BA_NM;

	}

	public void setCvChkAdBaName(String CV_CHK_AD_BA_NM) {

		this.CV_CHK_AD_BA_NM = CV_CHK_AD_BA_NM;

	}

	public String getCvChkAdBaAccountNumber() {

		return CV_CHK_AD_BA_ACCNT_NMBR;

	}

	public void setCvChkAdBaAccountNumber(String CV_CHK_AD_BA_ACCNT_NMBR) {

		this.CV_CHK_AD_BA_ACCNT_NMBR = CV_CHK_AD_BA_ACCNT_NMBR;

	}

	public byte getCvShowDuplicate() {

		return CV_SHW_DPLCT;

	}

	public void setCvShowDuplicate(byte CV_SHW_DPLCT) {

		this.CV_SHW_DPLCT = CV_SHW_DPLCT;

	}

	public String getCvApprovalStatus() {

		return CV_APPRVL_STTS;

	}

	public void setCvApprovalStatus(String CV_APPRVL_STTS) {

		this.CV_APPRVL_STTS = CV_APPRVL_STTS;

	}

	public byte getCvPosted() {

		return CV_PSTD;

	}

	public void setCvPosted(byte CV_PSTD) {

		this.CV_PSTD = CV_PSTD;

	}

	public String getCvChkCreatedByDescription() {

		return CV_CHK_CRTD_BY_DESC;

	}

	public void setCvChkCreatedByDescription(String CV_CHK_CRTD_BY_DESC) {

		this.CV_CHK_CRTD_BY_DESC = CV_CHK_CRTD_BY_DESC;

	}
	
	public String getCvChkCheckByDescription() {

		return CV_CHK_CHCK_BY_DESC;

	}

	public void setCvChkCheckByDescription(String CV_CHK_CHCK_BY_DESC) {

		this.CV_CHK_CHCK_BY_DESC = CV_CHK_CHCK_BY_DESC;

	}

	public String getCvChkApprovedRejectedByDescription() {

		return CV_CHK_APPRVD_RJCTD_BY_DESC;

	}

	public void setCvChkApprovedRejectedByDescription(String CV_CHK_APPRVD_RJCTD_BY_DESC) {

		this.CV_CHK_APPRVD_RJCTD_BY_DESC = CV_CHK_APPRVD_RJCTD_BY_DESC;

	}

	public byte getCvChkCrossCheck() {

		return CV_CRSS_CHCK;

	}

	public void setCvChkCrossCheck(byte CV_CRSS_CHCK) {

		this.CV_CRSS_CHCK = CV_CRSS_CHCK;

	}

	public String getCvChkVoucherDocumentNumber() {

   		return CV_CHK_VOU_DOC_NMBR;

   }

   public void setCvChkVoucherDocumentNumber(String CV_CHK_VOU_DOC_NMBR) {

   		this.CV_CHK_VOU_DOC_NMBR = CV_CHK_VOU_DOC_NMBR;

   }

   public Date getCvChkVoucherDate() {

   		return CV_CHK_VOU_DT;

   }

   public void setCvChkVoucherDate(Date CV_CHK_VOU_DT) {

   		this.CV_CHK_VOU_DT = CV_CHK_VOU_DT;

   }

   public String getCvPoDocumentNumber() {

	   return CV_PO_DOC_NMBR;

   }

   public void setCvPoDocumentNumber(String CV_PO_DOC_NMBR) {

	   this.CV_PO_DOC_NMBR = CV_PO_DOC_NMBR;

   }

   public Date getCvPoDate() {

	   return CV_PO_DT;

   }

   public void setCvPoDate(Date CV_PO_DT) {

	   this.CV_PO_DT = CV_PO_DT;

   }

   public String getCvVouNumber() {

	   return CV_VOU_NMBR;

   }

   public void setCvVouNumber(String CV_VOU_NMBR) {

	   this.CV_VOU_NMBR = CV_VOU_NMBR;

   }

   public Date getCvVouDate() {

	   return CV_VOU_DT;

   }

   public void setCvVouDate(Date CV_VOU_DT) {

	   this.CV_VOU_DT = CV_VOU_DT;

   }

   public String getCvVouPostedBy() {

	   return CV_VOU_PSTD_BY;

   }

   public void setCvVouPostedBy(String CV_VOU_PSTD_BY) {

	   this.CV_VOU_PSTD_BY = CV_VOU_PSTD_BY;
   }

   public String getCvVouDescription() {

	   return CV_VOU_DSCRPTN;

   }

   public void setCvVouDescription(String CV_VOU_DSCRPTN) {

	   this.CV_VOU_DSCRPTN = CV_VOU_DSCRPTN;
   }

   public String getCvCheckType() {

	   return CV_CHK_TYP;

   }

   public void setCvCheckType(String CV_CHK_TYP) {

	   this.CV_CHK_TYP = CV_CHK_TYP;
   }

   public double getCvApplyAmount() {

	   return CV_APPLY_AMNT;

   }

   public void setCvApplyAmount(double CV_APPLY_AMNT) {

	   this.CV_APPLY_AMNT = CV_APPLY_AMNT;

   }


   public double getCvApplyTaxAmount(){

	   return CV_APPLY_TX_AMNT;

   }

   public void setCvApplyTaxAmount(double CV_APPLY_TX_AMNT){

	   this.CV_APPLY_TX_AMNT = CV_APPLY_TX_AMNT;

   }

   public double getCvApplyNetAmount() {

	   return CV_APPLY_NET_AMNT;

   }

   public void setCvApplyNetAmount(double CV_APPLY_NET_AMNT)
   {

	   this.CV_APPLY_NET_AMNT = CV_APPLY_NET_AMNT;

   }

   public double getCvApplyAmountDue() {

	   return CV_APPLY_AMNT_DUE;

   }

   public void setCvApplyAmmountDue(double CV_APPLY_AMNT_DUE)
   {

	   this.CV_APPLY_AMNT_DUE = CV_APPLY_AMNT_DUE;

   }
   
   public double getCvApplyWithholdingTaxAmount(){

	   return CV_APPLY_WTHHLDNG_TX_AMNT;

   }

   public void setCvApplyWithholdingTaxAmount(double CV_APPLY_WTHHLDNG_TX_AMNT){

	   this.CV_APPLY_WTHHLDNG_TX_AMNT = CV_APPLY_WTHHLDNG_TX_AMNT;

   }

   public String getCvVouDrCoaNaturalDesc(){

	   return CV_VOU_DR_COA_NTRL_DESC;

   }

   public void setCvVouDrCoaNaturalDesc(String CV_VOU_DR_COA_NTRL_DESC){

	   this.CV_VOU_DR_COA_NTRL_DESC = CV_VOU_DR_COA_NTRL_DESC;

   }

   public String getCvVouReferenceNumber(){

	   return CV_VOU_RFRNC_NMBR;

   }

   public void setCvVouReferenceNumber(String CV_VOU_RFRNC_NMBR){

	   this.CV_VOU_RFRNC_NMBR = CV_VOU_RFRNC_NMBR;

   }

   public String getCvChkReferenceNumber(){

	   return CV_CHK_RFRNC_NMBR;

   }

   public void setCvChkReferenceNumber(String CV_CHK_RFRNC_NMBR){

	   this.CV_CHK_RFRNC_NMBR = CV_CHK_RFRNC_NMBR;

   } 

   public String getCvVouDrCoaAccountNumber() {

	   return CV_VOU_DR_ACCNT_NMBR;

   }

   public void setCvVouDrCoaAccountNumber(String CV_VOU_DR_ACCNT_NMBR) {

	   this.CV_VOU_DR_ACCNT_NMBR = CV_VOU_DR_ACCNT_NMBR;

   }

   public String getCvChkDrCoaNaturalDesc(){

	   return CV_CHK_DR_COA_NTRL_DESC;

   }

   public void setCvChkDrCoaNaturalDesc(String CV_CHK_DR_COA_NTRL_DESC){

	   this.CV_CHK_DR_COA_NTRL_DESC = CV_CHK_DR_COA_NTRL_DESC;

   }

   public String getCvChkSplAddress() {

	   return CV_CHK_SPL_ADDRSS;

   }

   public void setCvChkSplAddress(String CV_CHK_SPL_ADDRSS) {

	   this.CV_CHK_SPL_ADDRSS = CV_CHK_SPL_ADDRSS;

   }

   public double getCvVouGrossAmount() {

	   return CV_VOU_GRSS_AMNT;

   }

   public void setCvVouGrossAmount(double CV_VOU_GRSS_AMNT) {

	   this.CV_VOU_GRSS_AMNT = CV_VOU_GRSS_AMNT;

   }

   public double getCvVouWithholdingTaxAmount() {

	   return CV_VOU_WTHHLDNG_TX_AMNT;

   }

   public void setCvVouWithholdingTaxAmount(double CV_VOU_WTHHLDNG_TX_AMNT) {

	   this.CV_VOU_WTHHLDNG_TX_AMNT = CV_VOU_WTHHLDNG_TX_AMNT;

   }

   public double getCvVouNetAmount() {

	   return CV_VOU_NET_AMNT;

   }

   public void setCvVouNetAmount(double CV_VOU_NET_AMNT) {

	   this.CV_VOU_NET_AMNT = CV_VOU_NET_AMNT;

   }

   public String getCvChkMemo() {

	   return CV_CHK_MMO;

   }

   public void setCvChkMemo(String CV_CHK_MMO) {

	   this.CV_CHK_MMO = CV_CHK_MMO;

   }

   public String getCvChkBranchCode() {

	   return CV_CHK_BRNCH_CDE;

   }

   public void setCvChkBranchCode(String CV_CHK_BRNCH_CDE) {

	   this.CV_CHK_BRNCH_CDE = CV_CHK_BRNCH_CDE;

   }

   public String getCvChkBranchName() {

	   return CV_CHK_BRNCH_NM;

   }

   public void setCvChkBranchName(String CV_CHK_BRNCH_NM) {

	   this.CV_CHK_BRNCH_NM = CV_CHK_BRNCH_NM;

   }

   public String getChkMisc1() {

		return CHK_MISC1;

	}

	public void setChkMisc1(String CHK_MISC1) {

		this.CHK_MISC1 = CHK_MISC1;

	}
	
	public String getChkMisc2() {

		return CHK_MISC2;

	}

	public void setChkMisc2(String CHK_MISC2) {

		this.CHK_MISC2 = CHK_MISC2;

	}
	
	public String getChkMisc3() {

		return CHK_MISC3;

	}

	public void setChkMisc3(String CHK_MISC3) {

		this.CHK_MISC3 = CHK_MISC3;

	}
	
	public String getChkMisc4() {

		return CHK_MISC4;

	}

	public void setChkMisc4(String CHK_MISC4) {

		this.CHK_MISC4 = CHK_MISC4;

	}
	
	public String getChkMisc5() {

		return CHK_MISC5;

	}

	public void setChkMisc5(String CHK_MISC5) {

		this.CHK_MISC5 = CHK_MISC5;

	}
	
	public String getChkMisc6() {

		return CHK_MISC6;

	}

	public void setChkMisc6(String CHK_MISC6) {

		this.CHK_MISC6 = CHK_MISC6;

	}
	
   public String getCvTaxCode() {

	   return CV_TX_CD;

   }

   public void setCvTaxCode(String CV_TX_CD) {

	   this.CV_TX_CD = CV_TX_CD;

   }
   
   public String getCvWithholdingTaxCode() {

	   return CV_W_TX_CD;

   }

   public void setCvWithholdingTaxCode(String CV_W_TX_CD) {

	   this.CV_W_TX_CD = CV_W_TX_CD;

   }
   public String getCvChkSplCode() {

		return CV_CHK_SPL_CD;

	}

	public void setCvChkSplCode(String CV_CHK_SPL_CD) {

		this.CV_CHK_SPL_CD = CV_CHK_SPL_CD;

	}

   
   public static Comparator sortByAccount = new Comparator() {
	   String asd = "";
		public int compare(Object r1, Object r2) {
			int cmp=1;
			try{
				String receipt1 = ((ApRepCheckVoucherPrintDetails) r1).getCvChkDrCoaAccountNumber();
				String receipt2 = ((ApRepCheckVoucherPrintDetails) r2).getCvChkDrCoaAccountNumber();
				String docNum1 = ((ApRepCheckVoucherPrintDetails) r1).getCvChkDocumentNumber();
				String docNum2 = ((ApRepCheckVoucherPrintDetails) r2).getCvChkDocumentNumber();
				
				Debug.print(docNum1 + "  ***********  " + docNum2);
				
				if(docNum1.equals(docNum2)){
					Debug.print(receipt1 + "  << ETO NA >>  " + receipt2);
					cmp = receipt1.compareTo(receipt2);
					asd=receipt2;
				}
				
				Debug.print("EY ES DI: "+asd);
			}catch(Exception e){
				cmp=0;
			}
			return cmp;
				
				
		}

	};

}  // ApRepCheckVoucherPrintDetails