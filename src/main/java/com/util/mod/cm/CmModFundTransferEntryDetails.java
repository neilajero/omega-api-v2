/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.cm;

import com.util.cm.CmFundTransferDetails;

import java.util.ArrayList;
import java.util.Date;

public class CmModFundTransferEntryDetails extends CmFundTransferDetails implements java.io.Serializable {
   
   private String FT_AD_BNK_ACCNT_NM_FRM;
   private String FT_CRRNCY_FRM;
   private String FT_AD_BNK_ACCNT_NM_TO;
   private String FT_CRRNCY_TO; 
   private ArrayList cmFTRList = new ArrayList();   

   public CmModFundTransferEntryDetails () {
   }

   public CmModFundTransferEntryDetails (Integer FT_CODE, Date FT_DT, 
   		 String FT_DCMNT_NMBR, String FT_RFRNC_NMBR, String FT_MM, Integer FT_AD_BA_ACCNT_FRM,
         Integer FT_AD_BA_ACCNT_TO, double FT_AMNT, Date FT_CNVRSN_DT,
         double FT_CNVRSN_RT_FRM, byte FT_VOID, byte FT_ACCNT_FRM_RCNCLD, 
         byte FT_ACCNT_TO_RCNCLD, String FT_AD_BNK_ACCNT_NM_FRM, String FT_CRRNCY_FRM,
         String FT_AD_BNK_ACCNT_NM_TO, String FT_CRRNCY_TO, 
         String FT_APPRVL_STATUS, byte FT_PSTD, 
         String FT_CRTD_BY, Date FT_DT_CRTD, String FT_LST_MDFD_BY, 
         Date FT_DT_LST_MDFD, String FT_APPRVD_RJCTD_BY, 
         Date FT_DT_APPRVD_RJCTD, String FT_PSTD_BY, Date FT_DT_PSTD, String FT_RSN_FR_RJCTN, String FT_TYPE,
		 double FT_CNVRSN_RT_TO, Date FT_ACCNT_FRM_DT_RCNCLD, Date FT_ACCNT_TO_DT_RCNCLD, Integer FT_AD_BRNCH) { 
         
                
        
      this.FT_AD_BNK_ACCNT_NM_FRM = FT_AD_BNK_ACCNT_NM_FRM;
      this.FT_CRRNCY_FRM = FT_CRRNCY_FRM;
      this.FT_AD_BNK_ACCNT_NM_TO = FT_AD_BNK_ACCNT_NM_TO;
      this.FT_CRRNCY_TO = FT_CRRNCY_TO;
      
   }
   
   public String getFtAdBankAccountNameFrom() {
   	
   	  return FT_AD_BNK_ACCNT_NM_FRM;
   	  
   }
   
   public void setFtAdBankAccountNameFrom(String FT_AD_BNK_ACCNT_NM_FRM) {
   	
   	  this.FT_AD_BNK_ACCNT_NM_FRM = FT_AD_BNK_ACCNT_NM_FRM;
   	  
   }
   
   public String getFtCurrencyFrom() {
   	
   	  return FT_CRRNCY_FRM;
   	  
   }
   
   public void setFtCurrencyFrom(String FT_CRRNCY_FRM) {
   	
   	  this.FT_CRRNCY_FRM = FT_CRRNCY_FRM;
   	  
   }   
   
   public String getFtAdBankAccountNameTo() {
   	
   	  return FT_AD_BNK_ACCNT_NM_TO;
   	  
   }
   
   public void setFtAdBankAccountNameTo(String FT_AD_BNK_ACCNT_NM_TO) {
   	
   	  this.FT_AD_BNK_ACCNT_NM_TO = FT_AD_BNK_ACCNT_NM_TO;
   	  
   }
   
   public String getFtCurrencyTo() {
   	
   	  return FT_CRRNCY_TO;
   	  
   }
   
   public void setFtCurrencyTo(String FT_CRRNCY_TO) {
   	
   	  this.FT_CRRNCY_TO = FT_CRRNCY_TO;
   	  
   }
   
	public ArrayList getFtCmFTRList(){
		
		return this.cmFTRList;
		
	}
	
	public void setFtCmFTRList(ArrayList cmFTRList){
		
		this.cmFTRList = cmFTRList;
		
	}
  
}
 

// CmFundTransferEntryDetails class   