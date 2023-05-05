/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.ad;

import java.util.Date;

public class AdDocumentSequenceDetails implements java.io.Serializable {

   private Integer DS_CODE;
   private String DS_SQNC_NM;
   private Date DS_DT_FRM;
   private Date DS_DT_TO;
   private char DS_NMBRNG_TYP;
   private String DS_INTL_VL;

   public AdDocumentSequenceDetails () {
   }

   public AdDocumentSequenceDetails (Integer DS_CODE, String DS_SQNC_NM,
      Date DS_DT_FRM, Date DS_DT_TO, char DS_NMBRNG_TYP,
      String DS_INTL_VL) {

      this.DS_CODE = DS_CODE;
      this.DS_SQNC_NM = DS_SQNC_NM;
      this.DS_DT_FRM = DS_DT_FRM;
      this.DS_DT_TO = DS_DT_TO;
      this.DS_NMBRNG_TYP = DS_NMBRNG_TYP;
      this.DS_INTL_VL = DS_INTL_VL;

   }

   public AdDocumentSequenceDetails (String DS_SQNC_NM,
      Date DS_DT_FRM, Date DS_DT_TO, char DS_NMBRNG_TYP,
      String DS_INTL_VL) {

      this.DS_SQNC_NM = DS_SQNC_NM;
      this.DS_DT_FRM = DS_DT_FRM;
      this.DS_DT_TO = DS_DT_TO;
      this.DS_NMBRNG_TYP = DS_NMBRNG_TYP;
      this.DS_INTL_VL = DS_INTL_VL;

   }

   public Integer getDsCode() {
      return DS_CODE;
   }

   public String getDsSequenceName() {
      return DS_SQNC_NM;
   }

   public Date getDsDateFrom() {
      return DS_DT_FRM;
   }

   public Date getDsDateTo() {
      return DS_DT_TO;
   }

   public char getDsNumberingType() {
      return DS_NMBRNG_TYP;
   }

   public String getDsInitialValue() {
      return DS_INTL_VL;
   }

   public String toString() {
       return "DS_CODE = " + DS_CODE + " DS_SQNC_NM = " + DS_SQNC_NM +
          " DS_DT_FRM = " + DS_DT_FRM + " DS_DT_TO = " + DS_DT_TO +
      " DS_NMBRNG_TYP = " + DS_NMBRNG_TYP + " DS_INTL_VL = "
      + DS_INTL_VL;
   }

} // AdDocumentSequenceDetails class   