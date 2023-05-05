/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;


import com.util.gl.GlReportValueDetails;

public class GlModReportValueDetails extends GlReportValueDetails implements java.io.Serializable {

   private String RV_COA_ACCNT_NMBR = null;
   private String RV_COA_ACCNT_DESC = null;


   public GlModReportValueDetails() {
   }


   public String getRvCoaAccountNumber() {
	   return RV_COA_ACCNT_NMBR;
   }

   public void setRvCoaAccountNumber(String RV_COA_ACCNT_NMBR) {
	   this.RV_COA_ACCNT_NMBR =  RV_COA_ACCNT_NMBR;
   }


   public String getRvCoaAccountDescription() {
	   return RV_COA_ACCNT_DESC;
   }

   public void setRvCoaAccountDescription(String RV_COA_ACCNT_DESC) {
	   this.RV_COA_ACCNT_DESC =  RV_COA_ACCNT_DESC;
   }



} // GlRepMonthlyVatDeclarationDetailsDetails class