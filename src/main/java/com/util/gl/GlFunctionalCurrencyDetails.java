/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;

import java.util.Date;

public class GlFunctionalCurrencyDetails implements java.io.Serializable {

   private Integer FC_CODE;
   private String FC_NM;
   private String FC_DESC;
   private String FC_CNTRY;
   private char FC_SYMBL;
   private short FC_PRCSN;
   private short FC_EXTNDD_PRCSN;
   private double FC_MIN_ACCNT_UNT;
   private Date FC_DT_FRM;
   private Date FC_DT_TO;
   private byte FC_ENBL;

   public GlFunctionalCurrencyDetails() {
   }

   public GlFunctionalCurrencyDetails (Integer FC_CODE, String FC_NM,
      String FC_DESC, String FC_CNTRY, char FC_SYMBL, short FC_PRCSN,
      short FC_EXTNDD_PRCSN, double FC_MIN_ACCNT_UNT, Date FC_DT_FRM,
      Date FC_DT_TO, byte FC_ENBL) {

      this.FC_CODE = FC_CODE;
      this.FC_NM = FC_NM;
      this.FC_DESC = FC_DESC;
      this.FC_CNTRY = FC_CNTRY;
      this.FC_SYMBL = FC_SYMBL;
      this.FC_PRCSN = FC_PRCSN;
      this.FC_EXTNDD_PRCSN = FC_EXTNDD_PRCSN;
      this.FC_MIN_ACCNT_UNT = FC_MIN_ACCNT_UNT;
      this.FC_DT_FRM = FC_DT_FRM;
      this.FC_DT_TO = FC_DT_TO;
      this.FC_ENBL = FC_ENBL;

   }

   public GlFunctionalCurrencyDetails (String FC_NM,
      String FC_DESC, String FC_CNTRY, char FC_SYMBL, short FC_PRCSN,
      short FC_EXTNDD_PRCSN, double FC_MIN_ACCNT_UNT, Date FC_DT_FRM,
      Date FC_DT_TO, byte FC_ENBL) {

      this.FC_NM = FC_NM;
      this.FC_DESC = FC_DESC;
      this.FC_CNTRY = FC_CNTRY;
      this.FC_SYMBL = FC_SYMBL;
      this.FC_PRCSN = FC_PRCSN;
      this.FC_EXTNDD_PRCSN = FC_EXTNDD_PRCSN;
      this.FC_MIN_ACCNT_UNT = FC_MIN_ACCNT_UNT;
      this.FC_DT_FRM = FC_DT_FRM;
      this.FC_DT_TO = FC_DT_TO;
      this.FC_ENBL = FC_ENBL;
   }

   public GlFunctionalCurrencyDetails (String FC_NM) {

      this.FC_NM = FC_NM;

   }

   public Integer getFcCode() {
      return FC_CODE;
   }
   
   public String getFcName() {
      return FC_NM;
   }
   
   public String getFcDescription() {
      return FC_DESC;
   }
   
   public String getFcCountry() {
      return FC_CNTRY;
   }
   
   public char getFcSymbol() {
      return FC_SYMBL;
   }
   
   public short getFcPrecision() {
      return FC_PRCSN;
   }
   
   public short getFcExtendedPrecision() {
      return FC_EXTNDD_PRCSN;
   }
   
   public double getFcMinimumAccountUnit() {
      return FC_MIN_ACCNT_UNT;
   }
   
   public Date getFcDateFrom() {
      return FC_DT_FRM;
   }
   
   public Date getFcDateTo() {
      return FC_DT_TO;
   }
   
   public byte getFcEnable() {
      return FC_ENBL;
   }
   
   public String toString() {
       return "FC_CODE = " + FC_CODE + " FC_NM = " + FC_NM +
          " FC_DESC = " + FC_DESC + " FC_CNTRY = " + FC_CNTRY +
      " FC_SYMBL = " + FC_SYMBL + " FC_PRCSN = " + FC_PRCSN +
      " FC_EXTNDD_PRCSN = " + FC_EXTNDD_PRCSN +
      " FC_MIN_ACCNT_UNT = " + FC_MIN_ACCNT_UNT +
      " FC_DT_FRM = " + FC_DT_FRM +
      " FC_DT_TO = " + FC_DT_TO +
      " FC_ENBL = " + FC_ENBL;
   }

} // GlFunctionalCurrencyDetails class