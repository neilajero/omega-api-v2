/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;


public class GlModJournalLineInterfaceDetails implements java.io.Serializable {

   private Integer JLI_CODE;
   private short JLI_LN_NMBR;
   private byte JLI_DBT;
   private double JLI_AMNT;
   private String JLI_COA_ACCNT_NMBR;
   private String JLI_COA_DESC;

   public GlModJournalLineInterfaceDetails() {
   }

   public GlModJournalLineInterfaceDetails (Integer JLI_CODE, short JLI_LN_NMBR,
      byte JLI_DBT, double JLI_AMNT, String JLI_COA_ACCNT_NMBR) {

      this.JLI_CODE = JLI_CODE;
      this.JLI_LN_NMBR = JLI_LN_NMBR;
      this.JLI_DBT = JLI_DBT;
      this.JLI_AMNT = JLI_AMNT;
      this.JLI_COA_ACCNT_NMBR = JLI_COA_ACCNT_NMBR;

   }
   
   public GlModJournalLineInterfaceDetails (Integer JLI_CODE, short JLI_LN_NMBR,
      byte JLI_DBT, double JLI_AMNT, String JLI_COA_ACCNT_NMBR, String JLI_COA_DESC) {

      this.JLI_CODE = JLI_CODE;
      this.JLI_LN_NMBR = JLI_LN_NMBR;
      this.JLI_DBT = JLI_DBT;
      this.JLI_AMNT = JLI_AMNT;
      this.JLI_COA_ACCNT_NMBR = JLI_COA_ACCNT_NMBR;
      this.JLI_COA_DESC = JLI_COA_DESC;

   }

   public GlModJournalLineInterfaceDetails (short JLI_LN_NMBR,
      byte JLI_DBT, double JLI_AMNT, String JLI_COA_ACCNT_NMBR) {

      this.JLI_LN_NMBR = JLI_LN_NMBR;
      this.JLI_DBT = JLI_DBT;
      this.JLI_AMNT = JLI_AMNT;
      this.JLI_COA_ACCNT_NMBR = JLI_COA_ACCNT_NMBR;
   
   }

   public Integer getJliCode() {
      return JLI_CODE;
   }

   public short getJliLineNumber() {
      return JLI_LN_NMBR;
   }

   public byte getJliDebit() {
      return JLI_DBT;
   }

   public double getJliAmount() {
      return JLI_AMNT;
   }

   public String getJliCoaAccountNumber() {
      return JLI_COA_ACCNT_NMBR;
   }
   
   public String getJliCoaDescription() {
   	  return JLI_COA_DESC;
   }

   public String toString() {
       return "JLI_CODE = " + JLI_CODE + " JLI_LN_NMBR = " + JLI_LN_NMBR +
          " JLI_DBT = " + JLI_DBT +
      " JLI_AMNT = " + JLI_AMNT + " JLI_COA_ACCNT_NMBR = " + JLI_COA_ACCNT_NMBR +
      " JLI_COA_DESC = " + JLI_COA_DESC;
   }

} // GlJournalLineInterfaceDetails class   