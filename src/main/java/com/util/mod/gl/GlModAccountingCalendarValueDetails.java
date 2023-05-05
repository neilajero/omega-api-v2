/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;

import java.util.Date;

public class GlModAccountingCalendarValueDetails implements java.io.Serializable {

   private Integer ACV_CODE;
   private String ACV_PRD_PRFX;
   private short ACV_QRTR;
   private short ACV_PRD_NMBR;
   private Date ACV_DT_FRM;
   private Date ACV_DT_TO;
   private char ACV_STATUS;
   private int ACV_YR;
   private boolean ACV_CRRNT;

   public GlModAccountingCalendarValueDetails() {
   }

   public GlModAccountingCalendarValueDetails (Integer ACV_CODE,
      String ACV_PRD_PRFX, short ACV_QRTR, short ACV_PRD_NMBR,
      Date ACV_DT_FRM, Date ACV_DT_TO,
      char ACV_STATUS, int ACV_YR) {

      this.ACV_CODE = ACV_CODE;
      this.ACV_PRD_PRFX = ACV_PRD_PRFX;
      this.ACV_QRTR = ACV_QRTR;
      this.ACV_PRD_NMBR = ACV_PRD_NMBR;
      this.ACV_DT_FRM = ACV_DT_FRM;
      this.ACV_DT_TO = ACV_DT_TO;
      this.ACV_STATUS = ACV_STATUS;
      this.ACV_YR = ACV_YR;

   }

   public GlModAccountingCalendarValueDetails (Integer ACV_CODE,
      String ACV_PRD_PRFX, short ACV_PRD_NMBR, int ACV_YR) {

      this.ACV_CODE = ACV_CODE;
      this.ACV_PRD_PRFX = ACV_PRD_PRFX;
      this.ACV_PRD_NMBR = ACV_PRD_NMBR;
      this.ACV_YR = ACV_YR;

   }

   public Integer getAcvCode() {
      return ACV_CODE;
   }

   public String getAcvPeriodPrefix() {
      return ACV_PRD_PRFX;
   }
   
   public void setAcvPeriodPrefix(String ACV_PRD_PRFX) {
   	  this.ACV_PRD_PRFX = ACV_PRD_PRFX;
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

   public int getAcvYear() {
      return ACV_YR;
   }
   
   public void setAcvYear(int ACV_YR) {
   	
   	  this.ACV_YR = ACV_YR;
   	
   }
   
   public boolean getAcvCurrent() {
   	
   	  return ACV_CRRNT;
   	  
   }
   
   public void setAcvCurrent(boolean ACV_CRRNT) {
   	
   	  this.ACV_CRRNT = ACV_CRRNT;
   	
   }

} // GlAccountingCalendarValueDetails class