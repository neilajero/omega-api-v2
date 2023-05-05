/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;

import java.util.Date;

public class GlChartOfAccountDetails implements java.io.Serializable {

  private Integer COA_CODE;
  private String COA_ACCNT_NMBR;
  private String COA_ACCNT_DESC;
  private String COA_ACCNT_TYP;
  private String COA_TX_TYP;
  private String COA_CIT_CTGRY;
  private String COA_SAW_CTGRY;
  private String COA_IIT_CTGRY;
  private Date COA_DT_FRM;
  private Date COA_DT_TO;
  private String COA_SGMNT1;
  private String COA_SGMNT2;
  private String COA_SGMNT3;
  private String COA_SGMNT4;
  private String COA_SGMNT5;
  private String COA_SGMNT6;
  private String COA_SGMNT7;
  private String COA_SGMNT8;
  private String COA_SGMNT9;
  private String COA_SGMNT10;
  private byte COA_ENBL;
  private byte COA_FR_RVLTN;

  public GlChartOfAccountDetails() {
  }

  public GlChartOfAccountDetails(
      Integer COA_CODE,
      String COA_ACCNT_NMBR,
      String COA_ACCNT_DESC,
      String COA_ACCNT_TYP,
      String COA_TX_TYP,
      String COA_CIT_CTGRY,
      String COA_SAW_CTGRY,
      String COA_IIT_CTGRY,
      Date COA_DT_FRM,
      Date COA_DT_TO,
      byte COA_ENBL,
      byte COA_FR_RVLTN) {

    this.COA_CODE = COA_CODE;
    this.COA_ACCNT_NMBR = COA_ACCNT_NMBR;
    this.COA_ACCNT_DESC = COA_ACCNT_DESC;
    this.COA_ACCNT_TYP = COA_ACCNT_TYP;
    this.COA_TX_TYP = COA_TX_TYP;
    this.COA_CIT_CTGRY = COA_CIT_CTGRY;
    this.COA_SAW_CTGRY = COA_SAW_CTGRY;
    this.COA_IIT_CTGRY = COA_IIT_CTGRY;
    this.COA_DT_FRM = COA_DT_FRM;
    this.COA_DT_TO = COA_DT_TO;

    this.COA_ENBL = COA_ENBL;
    this.COA_FR_RVLTN = COA_FR_RVLTN;
  }

  public GlChartOfAccountDetails(
      String COA_ACCNT_NMBR,
      String COA_ACCNT_DESC,
      String COA_ACCNT_TYP,
      String COA_TX_TYP,
      String COA_CIT_CTGRY,
      String COA_SAW_CTGRY,
      String COA_IIT_CTGRY,
      Date COA_DT_FRM,
      Date COA_DT_TO,
      byte COA_ENBL,
      byte COA_FR_RVLTN) {

    this.COA_ACCNT_NMBR = COA_ACCNT_NMBR;
    this.COA_ACCNT_DESC = COA_ACCNT_DESC;
    this.COA_ACCNT_TYP = COA_ACCNT_TYP;
    this.COA_TX_TYP = COA_TX_TYP;
    this.COA_CIT_CTGRY = COA_CIT_CTGRY;
    this.COA_SAW_CTGRY = COA_SAW_CTGRY;
    this.COA_IIT_CTGRY = COA_IIT_CTGRY;
    this.COA_DT_FRM = COA_DT_FRM;
    this.COA_DT_TO = COA_DT_TO;
    this.COA_ENBL = COA_ENBL;
    this.COA_FR_RVLTN = COA_FR_RVLTN;
  }

  public GlChartOfAccountDetails(
      String COA_ACCNT_NMBR,
      String COA_ACCNT_DESC,
      String COA_ACCNT_TYP,
      String COA_TX_TYP,
      String COA_CIT_CTGRY,
      String COA_SAW_CTGRY,
      String COA_IIT_CTGRY,
      Date COA_DT_FRM,
      Date COA_DT_TO,
      String COA_SGMNT1,
      String COA_SGMNT2,
      String COA_SGMNT3,
      String COA_SGMNT4,
      String COA_SGMNT5,
      String COA_SGMNT6,
      String COA_SGMNT7,
      String COA_SGMNT8,
      String COA_SGMNT9,
      String COA_SGMNT10,
      byte COA_ENBL) {

    this.COA_ACCNT_NMBR = COA_ACCNT_NMBR;
    this.COA_ACCNT_DESC = COA_ACCNT_DESC;
    this.COA_ACCNT_TYP = COA_ACCNT_TYP;
    this.COA_TX_TYP = COA_TX_TYP;
    this.COA_CIT_CTGRY = COA_CIT_CTGRY;
    this.COA_SAW_CTGRY = COA_SAW_CTGRY;
    this.COA_IIT_CTGRY = COA_IIT_CTGRY;
    this.COA_DT_FRM = COA_DT_FRM;
    this.COA_DT_TO = COA_DT_TO;
    this.COA_SGMNT1 = COA_SGMNT1;
    this.COA_SGMNT2 = COA_SGMNT2;
    this.COA_SGMNT3 = COA_SGMNT3;
    this.COA_SGMNT4 = COA_SGMNT4;
    this.COA_SGMNT5 = COA_SGMNT5;
    this.COA_SGMNT6 = COA_SGMNT6;
    this.COA_SGMNT7 = COA_SGMNT7;
    this.COA_SGMNT8 = COA_SGMNT8;
    this.COA_SGMNT9 = COA_SGMNT9;
    this.COA_SGMNT10 = COA_SGMNT10;
    this.COA_ENBL = COA_ENBL;
  }

  public Integer getCoaCode() {
    return COA_CODE;
  }

  public String getCoaAccountNumber() {
    return COA_ACCNT_NMBR;
  }

  public String getCoaAccountDescription() {
    return COA_ACCNT_DESC;
  }

  public String getCoaAccountType() {
    return COA_ACCNT_TYP;
  }

  public String getCoaTaxType() {
    return COA_TX_TYP;
  }

  public String getCoaCitCategory() {
    return COA_CIT_CTGRY;
  }

  public String getCoaSawCategory() {
    return COA_SAW_CTGRY;
  }

  public String getCoaIitCategory() {
    return COA_IIT_CTGRY;
  }

  public Date getCoaDateFrom() {
    return COA_DT_FRM;
  }

  public Date getCoaDateTo() {
    return COA_DT_TO;
  }

  public String getCoaSegment1() {
    return COA_SGMNT1;
  }

  public String getCoaSegment2() {
    return COA_SGMNT2;
  }

  public String getCoaSegment3() {
    return COA_SGMNT3;
  }

  public String getCoaSegment4() {
    return COA_SGMNT4;
  }

  public String getCoaSegment5() {
    return COA_SGMNT5;
  }

  public String getCoaSegment6() {
    return COA_SGMNT6;
  }

  public String getCoaSegment7() {
    return COA_SGMNT7;
  }

  public String getCoaSegment8() {
    return COA_SGMNT8;
  }

  public String getCoaSegment9() {
    return COA_SGMNT9;
  }

  public String getCoaSegment10() {
    return COA_SGMNT10;
  }

  public byte getCoaEnable() {
    return COA_ENBL;
  }

  public byte getCoaForRevaluation() {
    return COA_FR_RVLTN;
  }

  public String toString() {
      return "<p style=\"margin-top: 0; margin-bottom: 0\">"
          + COA_CODE
          + "&nbsp;&nbsp;&nbsp;&nbsp;"
          + COA_ACCNT_NMBR
          + "&nbsp;&nbsp;&nbsp;&nbsp;"
          + COA_ACCNT_DESC
          + "&nbsp;&nbsp;&nbsp;&nbsp;"
          + COA_ACCNT_TYP
          + "&nbsp;&nbsp;&nbsp;&nbsp;"
          + COA_DT_FRM
          + "&nbsp;&nbsp;&nbsp;&nbsp;"
          + COA_DT_TO
          + "&nbsp;&nbsp;&nbsp;&nbsp;"
          + COA_ENBL
          + "</p>";
  }
} // GlChartOfAccountDetails class