/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.cm;


import com.util.cm.CmDistributionRecordDetails;

public class CmModDistributionRecordDetails extends CmDistributionRecordDetails implements java.io.Serializable {

   private String DR_COA_ACCNT_NMBR;
   private String DR_COA_ACCNT_DESC;

   public CmModDistributionRecordDetails () {
   }

   public CmModDistributionRecordDetails (Integer DR_CODE, 
         short DR_LN, String DR_CLSS, double DR_AMNT, byte DR_DBT, byte DR_RVRSL,
         byte DR_IMPRTD, String DR_COA_ACCNT_NMBR, String DR_COA_ACCNT_DESC) {
         	
    
        
      this.DR_COA_ACCNT_NMBR = DR_COA_ACCNT_NMBR;
      this.DR_COA_ACCNT_DESC = DR_COA_ACCNT_DESC;

   }
   
   public String getDrCoaAccountNumber() {
   	
   	  return DR_COA_ACCNT_NMBR;
   	
   }
   
   public void setDrCoaAccountNumber(String DR_COA_ACCNT_NMBR) {
   	
   	  this.DR_COA_ACCNT_NMBR = DR_COA_ACCNT_NMBR;
   	
   }
   
   public String getDrCoaAccountDescription() {
   	
   	  return DR_COA_ACCNT_DESC;
   	
   }
   
   public void setDrCoaAccountDescription(String DR_COA_ACCNT_DESC) {
   	
   	  this.DR_COA_ACCNT_DESC = DR_COA_ACCNT_DESC;
   	
   }
  
}
 

// CmModDistributionRecordDetails class   