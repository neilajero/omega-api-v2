/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;


public class GlModSuspenseAccountDetails implements java.io.Serializable {

   private Integer SA_CODE;
   private String SA_NM;
   private String SA_DESC;
   private String SA_JS_NM;
   private String SA_JC_NM;
   private String SA_COA_ACCNT_NMBR;
   private String SA_COA_ACCNT_DESC;

   public GlModSuspenseAccountDetails() {
   }

   public GlModSuspenseAccountDetails (Integer SA_CODE, String SA_NM, String SA_DESC,
      String SA_JC_NM, String SA_JS_NM, String SA_JS_COA_ACCNT_NMBR, String SA_JS_COA_ACCNT_DESC){   
      
      this.SA_CODE = SA_CODE;
      this.SA_NM = SA_NM;
      this.SA_DESC = SA_DESC;
      this.SA_JS_NM = SA_JS_NM;
      this.SA_JC_NM = SA_JC_NM;
      this.SA_COA_ACCNT_NMBR = SA_JS_COA_ACCNT_NMBR;
      this.SA_COA_ACCNT_DESC = SA_JS_COA_ACCNT_DESC;
      
   
   }

   public Integer getSaCode() {
      return SA_CODE;
   }

   public String getSaName() {
      return SA_NM;
   }

   public String getSaDescription() {
      return SA_DESC;
   }


   public String getSaJsName() {
      return SA_JS_NM;
   }

   public String getSaJcName() {
      return SA_JC_NM;
   }

   public String getSaCoaAccountNumber() {
      return SA_COA_ACCNT_NMBR;
   }
   
   public String getSaCoaAccountDescription() {
   	  return SA_COA_ACCNT_DESC;
   }

   public String toString() {

       return "SA_CODE = " + SA_CODE + " SA_NM = " + SA_NM +
          " SA_DESC = " + SA_DESC +
          " SA_JC_NM = " + SA_JC_NM +
          " SA_JS_NM = " + SA_JS_NM +
          " SA_COA_ACCNT_NMBR = " + SA_COA_ACCNT_NMBR;
   }
} // GlModSuspenseAccountDetails class