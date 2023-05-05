/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;

import java.util.Date;

public class GlModChartOfAccountDetails implements java.io.Serializable {

   private Integer MCOA_CODE;
   private String MCOA_ACCNT_NMBR;
   private String MCOA_CIT_CTGRY;
   private String MCOA_SAW_CTGRY;
   private String MCOA_IIT_CTGRY;
   private Date MCOA_DT_FRM;
   private Date MCOA_DT_TO;
   private byte MCOA_ENBL;
   private String MCOA_DESC;
   private String MCOA_ACCNT_TYP;
   private String MCOA_TX_TYP;
   private double MCOA_AMNT;
   private byte MCOA_DBT;
   private String MCOA_GL_FNCTNL_CRRNCY;
   
   private byte MCOA_FR_RVLTN;
   
   public GlModChartOfAccountDetails() {
   }

   public GlModChartOfAccountDetails (Integer MCOA_CODE,
      String MCOA_ACCNT_NMBR, String MCOA_CIT_CTGRY, String MCOA_SAW_CTGRY, String MCOA_IIT_CTGRY, Date MCOA_DT_FRM, Date MCOA_DT_TO,
      byte MCOA_ENBL, String MCOA_DESC, String MCOA_ACCNT_TYP, String MCOA_TX_TYP, 
      String MCOA_GL_FNCTNL_CRRNCY, byte MCOA_FR_RVLTN) {

      this.MCOA_CODE = MCOA_CODE;
      this.MCOA_ACCNT_NMBR = MCOA_ACCNT_NMBR;
      this.MCOA_CIT_CTGRY = MCOA_CIT_CTGRY;
      this.MCOA_SAW_CTGRY = MCOA_SAW_CTGRY;
      this.MCOA_IIT_CTGRY = MCOA_IIT_CTGRY;
      this.MCOA_DT_FRM = MCOA_DT_FRM;
      this.MCOA_DT_TO = MCOA_DT_TO;
      this.MCOA_ENBL = MCOA_ENBL;
      this.MCOA_DESC = MCOA_DESC;
      this.MCOA_ACCNT_TYP = MCOA_ACCNT_TYP;
      this.MCOA_TX_TYP = MCOA_TX_TYP;
      this.MCOA_GL_FNCTNL_CRRNCY = MCOA_GL_FNCTNL_CRRNCY;
      this.MCOA_FR_RVLTN = MCOA_FR_RVLTN;
      

   }

   public GlModChartOfAccountDetails (String MCOA_ACCNT_NMBR, String MCOA_ACCNT_TYP,
      double MCOA_AMNT, byte MCOA_DBT) {

      this.MCOA_ACCNT_NMBR = MCOA_ACCNT_NMBR;
      this.MCOA_ACCNT_TYP = MCOA_ACCNT_TYP;
      this.MCOA_AMNT = MCOA_AMNT;
      this.MCOA_DBT = MCOA_DBT;

   }
   
   public GlModChartOfAccountDetails (String MCOA_DESC, String MCOA_ACCNT_TYP) {

      this.MCOA_ACCNT_TYP = MCOA_ACCNT_TYP;
      this.MCOA_DESC = MCOA_DESC;

   }

   public Integer getMcoaCode() {
      return MCOA_CODE;
   }

   public String getMcoaAccountNumber() {
      return MCOA_ACCNT_NMBR;
   }
   
   public String getMcoaCitCategory() {
	      return MCOA_CIT_CTGRY;
	   }
   
   public String getMcoaSawCategory() {
	      return MCOA_SAW_CTGRY;
	   }
   public String getMcoaIitCategory() {
	      return MCOA_IIT_CTGRY;
	   }

   public Date getMcoaDateFrom() {
      return MCOA_DT_FRM;
   }

   public Date getMcoaDateTo() {
      return MCOA_DT_TO;
   }

   public byte getMcoaEnable() {
      return MCOA_ENBL;
   }

   public String getMcoaDescription() {
      return MCOA_DESC;
   }

   public String getMcoaAccountType() {
      return MCOA_ACCNT_TYP;
   }
   
   public String getMcoaTaxType() {
	      return MCOA_TX_TYP;
	   }

   public double getMcoaAmount() {
      return MCOA_AMNT;
   }

   public byte getMcoaDebit() {
      return MCOA_DBT;
   }
   
   public String getMcoaGlFuntionalCurrency() {
    return MCOA_GL_FNCTNL_CRRNCY;
   }
   
   public byte getMcoaForRevaluation() {
	   return MCOA_FR_RVLTN;
   }
   
   public String toString() {
       return MCOA_CODE + "&nbsp;&nbsp;&nbsp;&nbsp;" +
          MCOA_ACCNT_NMBR + "&nbsp;&nbsp;&nbsp;&nbsp;" +
          MCOA_DT_FRM + "&nbsp;&nbsp;&nbsp;&nbsp;" +
          MCOA_DT_TO + "&nbsp;&nbsp;&nbsp;&nbsp;" +
          MCOA_ENBL + "&nbsp;&nbsp;&nbsp;&nbsp;" +
          MCOA_DESC + "&nbsp;&nbsp;&nbsp;&nbsp;" +
          MCOA_ACCNT_TYP + "&nbsp;&nbsp;&nbsp;&nbsp;" +
          MCOA_AMNT + "&nbsp;&nbsp;&nbsp;&nbsp;" +
          MCOA_DBT + "&nbsp;&nbsp;&nbsp;&nbsp;" +
          MCOA_GL_FNCTNL_CRRNCY;
   }
}