/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.gl;

import java.util.Comparator;
import java.util.Date;

public class GlRepGeneralLedgerDetails implements java.io.Serializable, Comparable {

  private String GL_ACCNT_NMBR;
  private String GL_CIT_CTGRY;
  private String GL_SAW_CTGRY;
  private String GL_IIT_CTGRY;

  private double GL_BGNNG_BLNC;
  private double GL_ENDNG_BLNC;
  private String GL_ACCNT_DESC;
  private Date GL_JR_EFFCTV_DT;
  private String GL_JR_DCMNT_NMBR;
  private String GL_JR_CHCK_NMBR;
  private String GL_JR_NM;
  private String GL_JS_NM;
  private String GL_JR_TIN;
  private String GL_JR_DESC;
  private byte GL_JL_DBT;
  private double GL_JL_AMNT;
  private double GL_JL_TOT_AMNT;
  private double GL_BLNC;
  private String GL_ACCNT_TYP;
  private String GL_JR_SUB_LDGR;
  private byte GL_JR_PSTD;
  private String GL_JR_RFRNC_NMBR;

  private double GL_VAT_AMNT_DUE;
  private String GL_VAT_TAX_CD;
  private String GL_VAT_TIN;
  private String GL_VAT_SUB_LDGR_NM;
  private String GL_VAT_ADDRSS1;
  private String GL_VAT_ADDRSS2;

  private double GL_CNVRSN_RT;

  private boolean GL_SHW_ENTRS;
  private double GL_ACCNT_CTGRY_ONE;

  private String GL_CMPNY_NM;
  private String GL_CMPNY_TIN;
  private String GL_CMPNY_ADDRSS;
  private String GL_CMPNY_CTY;
  private String GL_CMPNY_RVN_OFFC;
  private String GL_CMPNY_FSCL_YR_ENDNG;

  public GlRepGeneralLedgerDetails() {
  }

  public String getGlAccountNumber() {

    return GL_ACCNT_NMBR;
  }

  public void setGlAccountNumber(String GL_ACCNT_NMBR) {

    this.GL_ACCNT_NMBR = GL_ACCNT_NMBR;
  }

  public double getGlBeginningBalance() {

    return GL_BGNNG_BLNC;
  }

  public void setGlBeginningBalance(double GL_BGNNG_BLNC) {

    this.GL_BGNNG_BLNC = GL_BGNNG_BLNC;
  }

  public double getGlEndingBalance() {

    return GL_ENDNG_BLNC;
  }

  public void setGlEndingBalance(double GL_ENDNG_BLNC) {

    this.GL_ENDNG_BLNC = GL_ENDNG_BLNC;
  }

  public String getGlAccountDescription() {

    return GL_ACCNT_DESC;
  }

  public void setGlAccountDescription(String GL_ACCNT_DESC) {

    this.GL_ACCNT_DESC = GL_ACCNT_DESC;
  }

  public Date getGlJrEffectiveDate() {

    return GL_JR_EFFCTV_DT;
  }

  public void setGlJrEffectiveDate(Date GL_JR_EFFCTV_DT) {

    this.GL_JR_EFFCTV_DT = GL_JR_EFFCTV_DT;
  }

  public String getGlJrDocumentNumber() {

    return GL_JR_DCMNT_NMBR;
  }

  public void setGlJrDocumentNumber(String GL_JR_DCMNT_NMBR) {

    this.GL_JR_DCMNT_NMBR = GL_JR_DCMNT_NMBR;
  }

  public String getGlJrCheckNumber() {

    return GL_JR_CHCK_NMBR;
  }

  public void setGlJrCheckNumber(String GL_JR_CHCK_NMBR) {

    this.GL_JR_CHCK_NMBR = GL_JR_CHCK_NMBR;
  }

  public String getGlJsName() {

    return GL_JS_NM;
  }

  public void setGlJsName(String GL_JS_NM) {

    this.GL_JS_NM = GL_JS_NM;
  }

  public String getGlJrTin() {

    return GL_JR_TIN;
  }

  public void setGlJrTin(String GL_JR_TIN) {

    this.GL_JR_TIN = GL_JR_TIN;
  }

  public String getGlJrName() {

    return GL_JR_NM;
  }

  public void setGlJrName(String GL_JR_NM) {

    this.GL_JR_NM = GL_JR_NM;
  }

  public String getGlJrDescription() {

    return GL_JR_DESC;
  }

  public void setGlJrDescription(String GL_JR_DESC) {

    this.GL_JR_DESC = GL_JR_DESC;
  }

  public byte getGlJlDebit() {

    return GL_JL_DBT;
  }

  public void setGlJlDebit(byte GL_JL_DBT) {

    this.GL_JL_DBT = GL_JL_DBT;
  }

  public double getGlJlTotalAmount() {

    return GL_JL_TOT_AMNT;
  }

  public void setGlJlTotalAmount(double GL_JL_TOT_AMNT) {

    this.GL_JL_TOT_AMNT = GL_JL_TOT_AMNT;
  }

  public double getGlJlAmount() {

    return GL_JL_AMNT;
  }

  public void setGlJlAmount(double GL_JL_AMNT) {

    this.GL_JL_AMNT = GL_JL_AMNT;
  }

  public double getGlBalance() {

    return GL_BLNC;
  }

  public void setGlBalance(double GL_BLNC) {

    this.GL_BLNC = GL_BLNC;
  }

  public String getGlAccountType() {

    return GL_ACCNT_TYP;
  }

  public void setGlAccountType(String GL_ACCNT_TYP) {

    this.GL_ACCNT_TYP = GL_ACCNT_TYP;
  }

  public String getGlJrSubLedger() {

    return GL_JR_SUB_LDGR;
  }

  public void setGlJrSubLedger(String GL_JR_SUB_LDGR) {

    this.GL_JR_SUB_LDGR = GL_JR_SUB_LDGR;
  }

  public byte getGlJrPosted() {

    return GL_JR_PSTD;
  }

  public void setGlJrPosted(byte GL_JR_PSTD) {

    this.GL_JR_PSTD = GL_JR_PSTD;
  }

  public String getGlJrReferenceNumber() {

    return GL_JR_RFRNC_NMBR;
  }

  public void setGlJrReferenceNumber(String GL_JR_RFRNC_NMBR) {

    this.GL_JR_RFRNC_NMBR = GL_JR_RFRNC_NMBR;
  }

  public double getGlVatAmountDue() {

    return GL_VAT_AMNT_DUE;
  }

  public void setGlVatAmountDue(double GL_VAT_AMNT_DUE) {

    this.GL_VAT_AMNT_DUE = GL_VAT_AMNT_DUE;
  }

  public String getGlVatTaxCode() {

    return GL_VAT_TAX_CD;
  }

  public void setGlVatTaxCode(String GL_VAT_TAX_CD) {

    this.GL_VAT_TAX_CD = GL_VAT_TAX_CD;
  }

  public String getGlVatTin() {

    return GL_VAT_TIN;
  }

  public void setGlVatTin(String GL_VAT_TIN) {

    this.GL_VAT_TIN = GL_VAT_TIN;
  }

  public String getGlVatSubLedgerName() {

    return GL_VAT_SUB_LDGR_NM;
  }

  public void setGlVatSubLedgerName(String GL_VAT_SUB_LDGR_NM) {

    this.GL_VAT_SUB_LDGR_NM = GL_VAT_SUB_LDGR_NM;
  }

  public String getGlVatAddress1() {

    return GL_VAT_ADDRSS1;
  }

  public void setGlVatAddress1(String GL_VAT_ADDRSS1) {

    this.GL_VAT_ADDRSS1 = GL_VAT_ADDRSS1;
  }

  public String getGlVatAddress2() {

    return GL_VAT_ADDRSS2;
  }

  public void setGlVatAddress2(String GL_VAT_ADDRSS2) {

    this.GL_VAT_ADDRSS2 = GL_VAT_ADDRSS2;
  }

  public double getGlConversionRate() {

    return GL_CNVRSN_RT;
  }

  public void setGlConversionRate(double GL_CNVRSN_RT) {

    this.GL_CNVRSN_RT = GL_CNVRSN_RT;
  }

  public boolean getGlShowEntries() {

    return GL_SHW_ENTRS;
  }

  public void setGlShowEntries(boolean GL_SHW_ENTRS) {

    this.GL_SHW_ENTRS = GL_SHW_ENTRS;
  }

  public double getGlAccountCategoryOne() {

    return GL_ACCNT_CTGRY_ONE;
  }

  public void setGlAccountCategoryOne(double GL_ACCNT_CTGRY_ONE) {

    this.GL_ACCNT_CTGRY_ONE = GL_ACCNT_CTGRY_ONE;
  }

  public String getGlCompanyName() {

    return GL_CMPNY_NM;
  }

  public void setGlCompanyName(String GL_CMPNY_NM) {

    this.GL_CMPNY_NM = GL_CMPNY_NM;
  }

  public String getGlCompanyTin() {

    return GL_CMPNY_TIN;
  }

  public void setGlCompanyTin(String GL_CMPNY_TIN) {

    this.GL_CMPNY_TIN = GL_CMPNY_TIN;
  }

  public String getGlCompanyAddress() {

    return GL_CMPNY_ADDRSS;
  }

  public void setGlCompanyAddress(String GL_CMPNY_ADDRSS) {

    this.GL_CMPNY_ADDRSS = GL_CMPNY_ADDRSS;
  }

  public String getGlCompanyCity() {

    return GL_CMPNY_CTY;
  }

  public void setGlCompanyCity(String GL_CMPNY_CTY) {

    this.GL_CMPNY_CTY = GL_CMPNY_CTY;
  }

  public String getGlCompanyRevenueOffice() {

    return GL_CMPNY_RVN_OFFC;
  }

  public void setGlCompanyRevenueOffice(String GL_CMPNY_RVN_OFFC) {

    this.GL_CMPNY_RVN_OFFC = GL_CMPNY_RVN_OFFC;
  }

  public String getGlCompanyFiscalYearEnding() {

    return GL_CMPNY_FSCL_YR_ENDNG;
  }

  public void setGlCompanyFiscalYearEnding(String GL_CMPNY_FSCL_YR_ENDNG) {

    this.GL_CMPNY_FSCL_YR_ENDNG = GL_CMPNY_FSCL_YR_ENDNG;
  }

  public String getGlCitCategory() {
    return GL_CIT_CTGRY;
  }

  public void setGlCitCategory(String GL_CIT_CTGRY) {
    this.GL_CIT_CTGRY = GL_CIT_CTGRY;
  }

  public String getGlSawCategory() {
    return GL_SAW_CTGRY;
  }

  public void setGlSawCategory(String GL_SAW_CTGRY) {
    this.GL_SAW_CTGRY = GL_SAW_CTGRY;
  }

  public String getGlIitCategory() {
    return GL_IIT_CTGRY;
  }

  public void setGlIitCategory(String GL_IIT_CTGRY) {
    this.GL_IIT_CTGRY = GL_IIT_CTGRY;
  }

  public int compareTo(Object o) {

    GlRepGeneralLedgerDetails details = (GlRepGeneralLedgerDetails) o;

    if (this.GL_VAT_SUB_LDGR_NM != null) {

      return this.GL_VAT_SUB_LDGR_NM.compareTo(details.getGlVatSubLedgerName());

    } else {

      return "".compareTo("");
    }
  }

  public static Comparator NoGroupComparator =
          (GL, anotherGL) -> {

            String GL_ACCNT_NMBR1 = ((GlRepGeneralLedgerDetails) GL).getGlAccountNumber();
            Date GL_JR_EFFCTV_DT1 = ((GlRepGeneralLedgerDetails) GL).getGlJrEffectiveDate();

            String GL_ACCNT_NMBR2 = ((GlRepGeneralLedgerDetails) anotherGL).getGlAccountNumber();
            Date GL_JR_EFFCTV_DT2 = ((GlRepGeneralLedgerDetails) anotherGL).getGlJrEffectiveDate();

            if (!(GL_ACCNT_NMBR1.equals(GL_ACCNT_NMBR2))) {

              return GL_ACCNT_NMBR1.compareTo(GL_ACCNT_NMBR2);

            } else {

              return GL_JR_EFFCTV_DT1.compareTo(GL_JR_EFFCTV_DT2);
            }
          };

  public static Comparator ReliefGroupComparator =
          (r1, r2) -> {

            String GL_VAT_SUB_LDGR_NM1 = ((GlRepGeneralLedgerDetails) r1).getGlVatSubLedgerName();
            String GL_VAT_TX_CD1 = ((GlRepGeneralLedgerDetails) r1).getGlVatTaxCode();

            String GL_VAT_SUB_LDGR_NM2 = ((GlRepGeneralLedgerDetails) r2).getGlVatSubLedgerName();
            String GL_VAT_TX_CD2 = ((GlRepGeneralLedgerDetails) r2).getGlVatTaxCode();

            if (!(GL_VAT_SUB_LDGR_NM1.equals(GL_VAT_SUB_LDGR_NM2))) {

              return GL_VAT_SUB_LDGR_NM1.compareTo(GL_VAT_SUB_LDGR_NM2);

            } else {

              return GL_VAT_SUB_LDGR_NM1.compareTo(GL_VAT_SUB_LDGR_NM2);
            }
          };

  public static Comparator OrderCheckNumberComparator =
          (GL, anotherGL) -> {

            String GL_CHCK_NMBR1 =
                ((GlRepGeneralLedgerDetails) GL).getGlJrCheckNumber() != null
                    ? ((GlRepGeneralLedgerDetails) GL).getGlJrCheckNumber()
                    : "";
            Date GL_JR_EFFCTV_DT1 = ((GlRepGeneralLedgerDetails) GL).getGlJrEffectiveDate();

            String GL_CHCK_NMBR2 =
                ((GlRepGeneralLedgerDetails) anotherGL).getGlJrCheckNumber() != null
                    ? ((GlRepGeneralLedgerDetails) anotherGL).getGlJrCheckNumber()
                    : "";
            Date GL_JR_EFFCTV_DT2 = ((GlRepGeneralLedgerDetails) anotherGL).getGlJrEffectiveDate();

            if (!(GL_CHCK_NMBR1.equals(GL_CHCK_NMBR2))) {

              return GL_CHCK_NMBR1.compareTo(GL_CHCK_NMBR2);

            } else {

              return GL_CHCK_NMBR1.compareTo(GL_CHCK_NMBR2);
            }
          };

  public static Comparator OrderDocumentNumberComparator =
          (GL, anotherGL) -> {

            String GL_DCMNT_NMBR1 =
                ((GlRepGeneralLedgerDetails) GL).getGlJrDocumentNumber() != null
                    ? ((GlRepGeneralLedgerDetails) GL).getGlJrDocumentNumber()
                    : "";
            Date GL_JR_EFFCTV_DT1 = ((GlRepGeneralLedgerDetails) GL).getGlJrEffectiveDate();

            String GL_DCMNT_NMBR2 =
                ((GlRepGeneralLedgerDetails) anotherGL).getGlJrDocumentNumber() != null
                    ? ((GlRepGeneralLedgerDetails) anotherGL).getGlJrDocumentNumber()
                    : "";
            Date GL_JR_EFFCTV_DT2 = ((GlRepGeneralLedgerDetails) anotherGL).getGlJrEffectiveDate();

            if (!(GL_DCMNT_NMBR1.equals(GL_DCMNT_NMBR2))) {

              return GL_DCMNT_NMBR1.compareTo(GL_DCMNT_NMBR2);

            } else {

              return GL_JR_EFFCTV_DT1.compareTo(GL_JR_EFFCTV_DT2);
            }
          };

  public static Comparator OrderDateComparator =
          (GL, anotherGL) -> {

            String GL_DCMNT_NMBR1 =
                ((GlRepGeneralLedgerDetails) GL).getGlJrDocumentNumber() != null
                    ? ((GlRepGeneralLedgerDetails) GL).getGlJrDocumentNumber()
                    : "";
            Date GL_JR_EFFCTV_DT1 = ((GlRepGeneralLedgerDetails) GL).getGlJrEffectiveDate();

            String GL_DCMNT_NMBR2 =
                ((GlRepGeneralLedgerDetails) anotherGL).getGlJrDocumentNumber() != null
                    ? ((GlRepGeneralLedgerDetails) anotherGL).getGlJrDocumentNumber()
                    : "";
            Date GL_JR_EFFCTV_DT2 = ((GlRepGeneralLedgerDetails) anotherGL).getGlJrEffectiveDate();

            if (!(GL_JR_EFFCTV_DT1.equals(GL_JR_EFFCTV_DT2))) {

              return GL_JR_EFFCTV_DT1.compareTo(GL_JR_EFFCTV_DT2);

            } else {

              return GL_DCMNT_NMBR1.compareTo(GL_DCMNT_NMBR2);
            }
          };
}