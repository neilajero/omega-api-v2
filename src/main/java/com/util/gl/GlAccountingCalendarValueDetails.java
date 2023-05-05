/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;

import java.util.Date;

public class GlAccountingCalendarValueDetails implements java.io.Serializable {

   private Integer ACV_CODE;
   private String ACV_PRD_PRFX;
   private short ACV_QRTR;
   private short ACV_PRD_NMBR;
   private Date ACV_DT_FRM;
   private Date ACV_DT_TO;
   private char ACV_STATUS;
   private Date ACV_DT_OPND;
   private Date ACV_DT_CLSD;
   private Date ACV_DT_PRMNNTLY_CLSD;
   private Date ACV_DT_FTR_ENTRD;

   public GlAccountingCalendarValueDetails() {
   }

   public GlAccountingCalendarValueDetails (Integer ACV_CODE,
      String ACV_PRD_PRFX, short ACV_QRTR, short ACV_PRD_NMBR,
      Date ACV_DT_FRM, Date ACV_DT_TO,
      char ACV_STATUS, Date ACV_DT_OPND, Date ACV_DT_CLSD,
      Date ACV_DT_PRMNNTLY_CLSD, Date ACV_DT_FTR_ENTRD) {

      this.ACV_CODE = ACV_CODE;
      this.ACV_PRD_PRFX = ACV_PRD_PRFX;
      this.ACV_QRTR = ACV_QRTR;
      this.ACV_PRD_NMBR = ACV_PRD_NMBR;
      this.ACV_DT_FRM = ACV_DT_FRM;
      this.ACV_DT_TO = ACV_DT_TO;
      this.ACV_STATUS = ACV_STATUS;
      this.ACV_DT_OPND = ACV_DT_OPND;
      this.ACV_DT_CLSD = ACV_DT_CLSD;
      this.ACV_DT_PRMNNTLY_CLSD = ACV_DT_PRMNNTLY_CLSD;
      this.ACV_DT_FTR_ENTRD = ACV_DT_FTR_ENTRD;

   }

   public GlAccountingCalendarValueDetails ( 
      String ACV_PRD_PRFX, short ACV_QRTR, short ACV_PRD_NMBR,
      Date ACV_DT_FRM, Date ACV_DT_TO,
      char ACV_STATUS, Date ACV_DT_OPND, Date ACV_DT_CLSD,
      Date ACV_DT_PRMNNTLY_CLSD, Date ACV_DT_FTR_ENTRD) {

      this.ACV_PRD_PRFX = ACV_PRD_PRFX;
      this.ACV_QRTR = ACV_QRTR;
      this.ACV_PRD_NMBR = ACV_PRD_NMBR;
      this.ACV_DT_FRM = ACV_DT_FRM;
      this.ACV_DT_TO = ACV_DT_TO;
      this.ACV_STATUS = ACV_STATUS;
      this.ACV_DT_OPND = ACV_DT_OPND;
      this.ACV_DT_CLSD = ACV_DT_CLSD;
      this.ACV_DT_PRMNNTLY_CLSD = ACV_DT_PRMNNTLY_CLSD;
      this.ACV_DT_FTR_ENTRD = ACV_DT_FTR_ENTRD;
   }

   public GlAccountingCalendarValueDetails (
      String ACV_PRD_PRFX, short ACV_QRTR, short ACV_PRD_NMBR,
      Date ACV_DT_FRM, Date ACV_DT_TO) {

      this.ACV_PRD_PRFX = ACV_PRD_PRFX;
      this.ACV_QRTR = ACV_QRTR;
      this.ACV_PRD_NMBR = ACV_PRD_NMBR;
      this.ACV_DT_FRM = ACV_DT_FRM;
      this.ACV_DT_TO = ACV_DT_TO;
   }

   public GlAccountingCalendarValueDetails (
      Integer ACV_CODE, String ACV_PRD_PRFX, short ACV_QRTR, short ACV_PRD_NMBR,
      Date ACV_DT_FRM, Date ACV_DT_TO,
      char ACV_STATUS) {

      this.ACV_CODE = ACV_CODE;
      this.ACV_PRD_PRFX = ACV_PRD_PRFX;
      this.ACV_QRTR = ACV_QRTR;
      this.ACV_PRD_NMBR = ACV_PRD_NMBR;
      this.ACV_DT_FRM = ACV_DT_FRM;
      this.ACV_DT_TO = ACV_DT_TO;
      this.ACV_STATUS = ACV_STATUS;
   }

   public GlAccountingCalendarValueDetails (
      Integer ACV_CODE, String ACV_PRD_PRFX, short ACV_PRD_NMBR) {

      this.ACV_CODE = ACV_CODE;
      this.ACV_PRD_PRFX = ACV_PRD_PRFX;
      this.ACV_PRD_NMBR = ACV_PRD_NMBR;
   }

   public Integer getAcvCode() {
      return ACV_CODE;
   }

   public String getAcvPeriodPrefix() {
      return ACV_PRD_PRFX;
   }

   public short getAcvQuarter() {
      return ACV_QRTR;
   }

   public short getAcvPeriodNumber() {
      return ACV_PRD_NMBR;
   }

   public Date getAcvDateFrom() {
      return ACV_DT_FRM;
   }

   public Date getAcvDateTo() {
      return ACV_DT_TO;
   }

   public char getAcvStatus() {
      return ACV_STATUS;
   }

   public Date getAcvDateOpened() {
      return ACV_DT_OPND;
   }

   public Date getAcvDateClosed() {
      return ACV_DT_CLSD;
   }

   public Date getAcvDatePermanentlyClosed() {
      return ACV_DT_PRMNNTLY_CLSD;
   }

   public Date getAcvDateFutureEntered() {
      return ACV_DT_FTR_ENTRD;
   }

   public String toString() {
       return "ACV_CODE = " + ACV_CODE + " ACV_PRD_PRFX = " + ACV_PRD_PRFX +
          " ACV_QTR = " + ACV_QRTR + " ACV_PRD_NMBR = " + ACV_PRD_NMBR +
      " ACV_DT_FRM = " + ACV_DT_FRM + " ACV_DT_TO = " + ACV_DT_TO +
      " ACV_STATUS = " + ACV_STATUS +
      " ACV_DT_OPND = " + ACV_DT_OPND + " ACV_DT_CLSD = " + ACV_DT_CLSD +
      " ACV_DT_PRMNNTLY_CLSD = " + ACV_DT_PRMNNTLY_CLSD + " ACV_DT_FTR_ENTRD = " +
      ACV_DT_FTR_ENTRD;
   }

} // GlAccountingCalendarValueDetails class