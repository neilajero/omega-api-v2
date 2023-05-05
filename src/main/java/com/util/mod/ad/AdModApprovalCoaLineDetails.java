/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;


import com.util.ad.AdApprovalCoaLineDetails;

public class AdModApprovalCoaLineDetails extends AdApprovalCoaLineDetails implements java.io.Serializable {

   private String ACL_COA_ACCNT_NMBR;
   private String ACL_COA_ACCNT_DESC;
   
   public AdModApprovalCoaLineDetails() {
   }

   public String getAclCoaAccountNumber() {
   	
   	  return ACL_COA_ACCNT_NMBR;
   	
   }
   
   public void setAclCoaAccountNumber(String ACL_COA_ACCNT_NMBR) {
   	
   	  this.ACL_COA_ACCNT_NMBR = ACL_COA_ACCNT_NMBR;
   	
   }
   
   public String getAclCoaDescription() {
   	
   	  return ACL_COA_ACCNT_DESC;
   	
   }
   
   public void setAclCoaDescription(String ACL_COA_ACCNT_DESC) {
   	
   	  this.ACL_COA_ACCNT_DESC = ACL_COA_ACCNT_DESC;
   	
   }

} // AdModApprovalCoaLineDetails class